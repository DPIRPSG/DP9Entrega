package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
	
	@Query("select f.invoice from FeePayment where f.customer.id == ?1 and f.invoice != null")
	Collection<Invoice> findAllByCustomerId(int customerId);
	
}
