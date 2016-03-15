package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
	
	@Query("select distinct(f.invoice) from FeePayment f where f.customer.id = ?1")
	Collection<Invoice> findAllByCustomerId(int customerId);
	
}
