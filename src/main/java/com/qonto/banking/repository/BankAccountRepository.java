package com.qonto.banking.repository;

import com.qonto.banking.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

    Optional<BankAccount> findByIbanAndBic(String iban, String bic);

}
