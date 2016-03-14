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

	@Query("select t from Trainer t where (t.name like concat('%',?1,'%') or t.surname like concat('%',?1,'%') or t.curriculum.statement like concat('%',?1,'%') or ?1 member of t.curriculum.skills or ?1 member of t.curriculum.likes or ?1 member of t.curriculum.dislikes)")
	Collection<Trainer> findBySingleKeyword(String keyword);

	@Query("select t from Trainer t where t.curriculum.id = ?1")
	Trainer findByCurriculumId(int curriculumId);
	
}
