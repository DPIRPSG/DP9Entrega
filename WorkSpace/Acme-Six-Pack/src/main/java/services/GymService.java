package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.GymRepository;
import domain.Bulletin;
import domain.Comment;
import domain.Customer;
import domain.FeePayment;
import domain.Gym;
import domain.Room;
import domain.ServiceEntity;

@Service
@Transactional
public class GymService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private GymRepository gymRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ServiceService serviceService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private RoomService roomService;
	
	// Constructors -----------------------------------------------------------

	public GymService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Gym create() {
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can create gyms");
		
		Gym result;
		Collection<ServiceEntity> services;
		Collection<Comment> comments;
		Collection<FeePayment> feePayments;
		Collection<Bulletin> bulletins;
		Collection<Room> rooms;
		
		services = new ArrayList<>();
		feePayments = new ArrayList<>();
		comments = new ArrayList<>();
		bulletins = new ArrayList<>();
		rooms = new ArrayList<>();
		
		
		result = new Gym();
		
		result.setServices(services);
		result.setFeePayments(feePayments);
		result.setComments(comments);
		result.setRooms(rooms);
		result.setBulletins(bulletins);
		
		return result;
	}
	
	public Gym save(Gym gym) {
		Assert.notNull(gym);
		Gym result;
		//Assert.isTrue(actorService.checkAuthority("ADMIN"),
			//	"Only an admin can save gyms");
		
		result = gymRepository.save(gym);
		
		return result;
	}

	public void saveToEdit(Gym gym) {
		Assert.notNull(gym);
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can save gyms");
		
		Collection<ServiceEntity> services;
		Collection<ServiceEntity> servicesPreSave;
		ServiceEntity fitness;
		
		servicesPreSave = new ArrayList<ServiceEntity>();
		
		fitness = serviceService.findOneByName("Fitness");
		if(gym.getServices() == null){
			gym.setServices(new ArrayList<ServiceEntity>());
		}
		gym.addService(fitness);
		
		if(gym.getId() != 0) {
			Gym gymPreSave;
			Collection<FeePayment> feePayments;
			Collection<Comment> comments;
			
			gymPreSave = gymRepository.findOne(gym.getId());
			
			feePayments = gymPreSave.getFeePayments();
			comments = gymPreSave.getComments();
			servicesPreSave = new ArrayList<ServiceEntity>(gymPreSave.getServices());
			
			Assert.isTrue(gym.getFeePayments().containsAll(feePayments) && gym.getFeePayments().size() == feePayments.size());
			Assert.isTrue(gym.getComments().containsAll(comments) && gym.getComments().size() == comments.size());
		}else{
			Collection<Room> rooms;
			Room defaultRoom;
			
			defaultRoom = roomService.create();
			defaultRoom.setDescription("Para cualquier tipo de actividad");
			defaultRoom.setName("Habitación general");
			defaultRoom.setNumberOfSeats(30);
			defaultRoom.setGym(gym);
			
			rooms = new ArrayList<Room>();
			rooms.add(defaultRoom);
			
			gym.setRooms(rooms);
		}
		
		services = gym.getServices();
		
		gym = this.save(gym);

		
		for(ServiceEntity service : services) {
			if(!servicesPreSave.contains(service)){
				service.addGym(gym);
				serviceService.save(service);
			}			
		}
		
		for(ServiceEntity service : servicesPreSave) {
			if (!services.contains(service)) {
				service.removeGym(gym);
				serviceService.save(service);
			}			
		}
	}
	
	public void delete(Gym gym) {
		Assert.notNull(gym);
		Assert.isTrue(gym.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can delete gyms");
		Assert.isTrue(gym.getFeePayments().isEmpty());
		Assert.isTrue(gym.getComments().isEmpty());
		Assert.isTrue(gym.getBulletins().isEmpty());
		
		for(Room room : gym.getRooms()) {
			Assert.isTrue(room.getActivities().isEmpty());
		}
		
		Collection<ServiceEntity> services;
		
		services = serviceService.findAll();
		
		for(ServiceEntity service : services) {
			service.removeGym(gym);
			serviceService.save(service);
		}
		
		gymRepository.delete(gym);
		
	}
	
	
	

	// Other business methods -------------------------------------------------

	public Gym findOne(int gymId) {
		Gym result;
		
		result = gymRepository.findOne(gymId);
		
		return result;
	}
	
	
	/**
	 * Lista los gyms
	 */
	// req: 12.5
	public Collection<Gym> findAll() {
		Collection<Gym> result;

		result = gymRepository.findAll();

		return result;
	}
	
	public Collection<ArrayList<Integer>> numbersOfCustomersByGym(Collection<Gym> gyms) {
		Collection<ArrayList<Integer>> result;
		Integer customerNumber;
		ArrayList<Integer> result2;
		
		result = new ArrayList<ArrayList<Integer>>();
		
		for(Gym gym : gyms) {
			result2 = new ArrayList<Integer>();
			customerNumber = customerService.numbersOfCustomersByGym(gym.getId());
			result2.add(gym.getId());
			result2.add(customerNumber);
			result.add(result2);
		}
		
		return result;
	}

	public Collection<Gym> findBySingleKeyword(String keyword){
		Assert.notNull(keyword);
		Assert.isTrue(!keyword.isEmpty());
		
		Collection<Gym> result;

		result = gymRepository.findBySingleKeyword(keyword);
		
		return result;
	}

	public Collection<Gym> findAllByService(Integer serviceId) {
		Collection<Gym> result;

		result = gymRepository.findAllByService(serviceId);

		return result;
	}

	public Collection<Gym> findAllWithFeePaymentActive() {
		Collection<Gym> result;
		Date moment;
		Customer customer;
		
		customer = customerService.findByPrincipal();
		moment = new Date();
		
		result = gymRepository.findAllWithFeePaymentActive(moment, customer.getId());
		
		return result;
	}

	public Collection<Gym> findAllWithoutFeePaymentActive() {
		Collection<Gym> result;
		Collection<Gym> gymsPaid;
		Date moment;
		Customer customer;
		
		result = gymRepository.findAll();
		customer = customerService.findByPrincipal();
		moment = new Date();
		
		gymsPaid = gymRepository.findAllWithFeePaymentActive(moment, customer.getId());
		
		result.removeAll(gymsPaid);
		
		return result;
	}
	
	/* Query 1 */
	public Collection<Gym> findMostPopularGyms(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can open the dashboard");
		
		Collection<Gym> result;
		
		result = gymRepository.findMostPopularGyms();
		
		return result;
	}
	
	/* Query 2 */
	public Collection<Gym> findLeastPopularGym(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can open the dashboard");
		
		Collection<Gym> result;
		
		result = gymRepository.findLeastPopularGym();
		
		return result;
	}

	/* Query 9 */
	public Collection<Gym> findMostCommented(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can open the dashboard");
		
		Collection<Gym> result;
		
		result = gymRepository.findMostCommented();
		
		return result;
	}
	
	/* Query 12 */
	public Double findAverageNumberOfCommentsPerGym(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can open the dashboard");
		
		Double result;
		
		result = gymRepository.findAverageNumberOfCommentsPerGym();
		
		return result;
	}
	
	public Double findAverageRoomsPerGym(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can open the dashboard");
		
		Double result;
		
		result = gymRepository.findAverageRoomsPerGym();
		
		return result;
	}
	
	public Double standardDeviationRoomsPerGym(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can open the dashboard");

		Double result;
		
		result = gymRepository.standardDeviationRoomsPerGym();
		
		return result;
	}
	
	public Collection<Gym> gymsWithMoreRoomsThanAverage(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can open the dashboard");

		Collection<Gym> result;
		
		result = gymRepository.gymsWithMoreRoomsThanAverage();
		
		return result;
	}
	
	public Collection<Gym> gymsWithLessRoomsThanAverage(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can open the dashboard");

		Collection<Gym> result;
		
		result = gymRepository.gymsWithLessRoomsThanAverage();
		
		return result;
	}
	
	public void flush(){
		
		gymRepository.flush();
		
	}
}
