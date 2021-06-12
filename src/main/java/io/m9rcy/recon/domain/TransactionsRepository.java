package io.m9rcy.recon.domain;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface TransactionsRepository extends Repository<TransactionData, Long> {

    @Query("SELECT * FROM TRANSACTIONS WHERE PAYMENT_ID = :paymentId")
    Optional<TransactionData> findByPaymentId(String paymentId);

}
