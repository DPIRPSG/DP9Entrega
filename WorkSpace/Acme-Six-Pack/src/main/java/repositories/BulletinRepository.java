package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Bulletin;

@Repository
public interface BulletinRepository extends JpaRepository<Bulletin, Integer> {

	@Query("select b from Bulletin b where b.gym.id = ?1")
	Collection<Bulletin> findAllByGymId(int gymId);
	
	@Query("select b from Bulletin b where (b.title like concat('%',?1,'%') or b.description like concat('%',?1,'%')) and b.gym.id = ?2")
	Collection<Bulletin> findBySingleKeyword(String keyword, int gymId);

}
