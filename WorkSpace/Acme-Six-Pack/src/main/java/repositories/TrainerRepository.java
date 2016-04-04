package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Trainer;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

	@Query("select t from Trainer t where t.userAccount.id = ?1")
	Trainer findByUserAccountId(int id);

//	@Query("select t from Trainer t where (t.name like concat('%',?1,'%') or t.surname like concat('%',?1,'%') or t.curriculum.statement like concat('%',?1,'%') or t.curriculum.skills like concat('%',?1,'%') or t.curriculum.likes like concat('%',?1,'%') or t.curriculum.dislikes like concat('%',?1,'%'))")
//	Collection<Trainer> findBySingleKeyword(String keyword);

	@Query("select t from Trainer t left join t.curriculum c where (t.name like concat('%',?1,'%') or t.surname like concat('%',?1,'%') or c.statement like concat('%',?1,'%') or c.skills like concat('%',?1,'%') or c.likes like concat('%',?1,'%') or c.dislikes like concat('%',?1,'%'))")
	Collection<Trainer> findBySingleKeyword(String keyword);


	@Query("select t from Trainer t where t.curriculum.id = ?1")
	Trainer findByCurriculumId(int curriculumId);

	@Query("select t from Trainer t left join t.services s where s.id = ?1")
	Collection<Trainer> findAllByServiceId(int serviceId);
	
}
