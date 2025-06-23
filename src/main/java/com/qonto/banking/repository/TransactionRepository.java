package com.qonto.banking.repository;

import com.qonto.banking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    //@Query("Select t from Transaction t where t.status = 'FAILED'")
    /*@Query("Select * from Transaction") // this query NEEDS to change according to the commented query above
    List<Transaction> fetchFailedTransactions();*/
}
