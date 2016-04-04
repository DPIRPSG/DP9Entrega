package repositories;

import java.util.Collection;
import java.util.Date;

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
	
	@Query("select a from Activity a left join a.customers c group by a order by count(c) desc")
	Collection<Activity> activitiesByPopularity();
	
	@Query("select avg(s.activities.size) from Gym g left join g.services s group by s order by s")
	Collection<Double> averageNumberOfActivitiesPerGymByService();
	
	@Query("select s.name from Gym g left join g.services s group by s order by s")
	Collection<String> serviceAverageNumberOfActivitiesPerGymByService();

	@Query("select a from Activity a join a.room r join r.gym g where g.id = ?1 and a.deleted = false and a.startingMoment > ?2")
	Collection<Activity> findAllActivesByGymId(int gymId, Date moment);

}
