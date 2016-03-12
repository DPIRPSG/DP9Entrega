package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

	@Query("select a from Activity a join a.customers c where c.id = ?1")
	Collection<Activity> findAllByCustomer(int customerId);

	@Query("select a from Activity a join a.room r join r.gym g where g.id = ?1")
	Collection<Activity> findAllByGymId(int gymId);
}
