package repositories;


import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	@Query("select c from Customer c where c.userAccount.id = ?1")
	Customer findByUserAccountId(int userAccountId);
	
	@Query("select distinct c from Activity a join a.customers c where a.service.id = ?1")
	Collection<Customer> findByServiceBooked(int serviceId);
	
	@Query("select distinct c from Activity a join a.customers c where a.room.gym.id = ?1")
	Collection<Customer> findByGymBooked(int gymId);
	
	/* == DASHBOARD == */

	/* Query 5 */
	@Query("select c from Customer c left join c.feePayments f group by c having count(f) >= all(select count(f) from Customer c left join c.feePayments f group by c)")
	Collection<Customer> findCustomerWhoHasPaidMoreFees();
	
	/* Query 6 */
	@Query("select c from Customer c left join c.feePayments f group by c having count(f) <= all(select count(f) from Customer c left join c.feePayments f group by c)")
	Collection<Customer> findCustomerWhoHasPaidLessFees();
	
	/* Query 14 */
	@Query("select c from Customer c left join c.madeComments m where m.deleted IS TRUE group by c having count(m) >= all(select count(m) from Customer c left join c.madeComments m where m.deleted is TRUE group by c)")
	Collection<Customer> findCustomerWhoHaveBeenRemovedMoreComments();
	
	@Query("select c1 from Customer c1 left join c1.feePayments f1 left join f1.invoice i1 where i1 IS NOT NULL group by c1 having count(distinct i1) >= all(select count(distinct i2) from Customer c2 left join c2.feePayments f2 left join f2.invoice i2 where i2 IS NOT NULL group by c2)")
	Collection<Customer> moreInvoicesIssuedCustomer();
	
	@Query("select c from Customer c left join c.feePayments f left join f.invoice i where i IS NULL group by c")
	Collection<Customer> noRequestedInvoicesCustomer();
}
