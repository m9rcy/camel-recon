package io.m9rcy.recon.domain;

import jdk.jfr.DataAmount;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("TRANSACTIONS")
public class TransactionData {

    private Long id;
    private String paymentId;
    private String transactionId;
    private String accountNo;
    private String amount;

}
