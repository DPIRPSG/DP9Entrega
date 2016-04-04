package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.GymService;
import services.RoomService;

import controllers.AbstractController;
import domain.Gym;
import domain.Room;

@Controller
@RequestMapping(value = "/room/administrator")
public class RoomAdministratorController extends AbstractController {

	// Services ----------------------------------------------------------

	@Autowired
	private RoomService roomService;

	@Autowired
	private GymService gymService;

	// Constructors ----------------------------------------------------------

	public RoomAdministratorController() {
		super();
	}

	// Listing ----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int gymId) {
		ModelAndView result;
		Collection<Room> rooms;
		Gym gym;

		rooms = roomService.findAllByGymId(gymId);
		gym = gymService.findOne(gymId);

		result = new ModelAndView("room/list");
		result.addObject("requestURI", "room/administrator/list.do");
		result.addObject("rooms", rooms);
		result.addObject("gym", gym);

		return result;
	}

	// Creation ----------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int gymId) {
		ModelAndView result;
		Room room;

		room = roomService.create(gymId);
		result = createEditModelAndView(room);

		return result;
	}

	// Edition ----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int roomId) {
		ModelAndView result;
		Room room;

		room = roomService.findOne(roomId);
		result = createEditModelAndView(room);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Room room, BindingResult binding) {
		ModelAndView result;
		
		if (binding.hasErrors()) {
			result = createEditModelAndView(room);
		} else {
			try {
				roomService.saveToEdit(room);
				result = new ModelAndView("redirect:list.do?" + "gymId=" + room.getGym().getId());
			} catch (Throwable oops) {
				result = createEditModelAndView(room, "room.commit.error");
			}
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Room room, BindingResult binding) {
		ModelAndView result;
		String gymId;
		
		gymId="gymId="+room.getGym().getId();

		try {
			roomService.delete(room);
			result = new ModelAndView("redirect:list.do?"+gymId);
		} catch (Throwable oops) {
			result = createEditModelAndView(room, "room.commit.error");
		}

		return result;
	}

	// Ancillary Methods
	// ----------------------------------------------------------

	protected ModelAndView createEditModelAndView(Room room) {
		ModelAndView result;

		result = createEditModelAndView(room, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Room room, String message) {
		ModelAndView result;
		Collection<Gym> gyms;

		gyms = gymService.findAll();

		result = new ModelAndView("room/edit");
		result.addObject("room", room);
		result.addObject("message", message);
		result.addObject("gyms", gyms);

		return result;
	}
}
