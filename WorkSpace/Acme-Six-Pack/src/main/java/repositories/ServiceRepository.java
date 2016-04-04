package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.ServiceEntity;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Integer> {

	@Query("select s from ServiceEntity s join s.gyms g where g.id = ?1")
	Collection<ServiceEntity> findAllByGym(int gymId);

	@Query("select s from ServiceEntity s where s.name like ?1")
	ServiceEntity findOneByName(String name);

	@Query("select s from ServiceEntity s where s.name not like 'Fitness'")
	Collection<ServiceEntity> findAllWithoutFitness();

	
	/* == DASHBOARD == */
	
	/* Query 3 */
	@Query("select s from ServiceEntity s left join s.activities a group by s having count(a) >= all(select count(a) from ServiceEntity s left join s.activities a group by s)")
	Collection<ServiceEntity> findMostPopularService();
	
	/* Query 4 */
	@Query("select s from ServiceEntity s left join s.activities a group by s having count(a) <= all(select count(a) from ServiceEntity s left join s.activities a group by s)")
	Collection<ServiceEntity> findLeastPopularService();
	
	/* Query 10 */
	// The service/s that has/have more comments.
	@Query("select s from ServiceEntity s join s.comments c group by s having count(s.comments.size) = (select max(s.comments.size) from ServiceEntity s)")
	// Another option to do it:
	// @Query("select s1 from Comment c1 join c1.service s1 group by s1 having count(c1) >= all(select count(c2) from ServiceEntity s2 join s2.comments c2 group by s2)")
	Collection<ServiceEntity> findMostCommented();
	
	/* Query 13 */
	@Query("select avg(s.comments.size) from ServiceEntity s")
	Double findAverageNumberOfCommentsPerService();
	
	@Query("select avg(t.services.size) from Trainer t")
	Double averageNumberOfServiceWithSpecialisedTrainer();
	
	@Query("select s from ServiceEntity s left join s.trainers t group by s having count(t) >= all(select count(t) from ServiceEntity s left join s.trainers t group by s)")
	Collection<ServiceEntity> mostPopularServiceByNumberOfTrainer();
}
