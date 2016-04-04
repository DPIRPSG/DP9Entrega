package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

	@Query("select r from Room r where r.gym.id = ?1")
	Collection<Room> findAllByGymId(int gymId);

	@Query("select r from ServiceEntity s join s.gyms g join g.rooms r where s.id = ?1")
	Collection<Room> findAllByServiceId(int id);

}
