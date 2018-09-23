/**
 * 
 */
package com.org.mycompany.positions.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.org.mycompany.positions.domain.Transaction;

/**
 * @author Dayanand Mishra
 *
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	@Query(value = "select new Transaction(a.instrument, a.transactionType,sum(a.transactionQuantity) as transactionQuantity) from Transaction a "
			// + "where a.txnDate=:txnDate"
			+ " group by a.instrument, a.transactionType")
	public Collection<Transaction> findTransactionByDateAndTransactionType(/* @Param("txnDate") String txnDate */);
}
