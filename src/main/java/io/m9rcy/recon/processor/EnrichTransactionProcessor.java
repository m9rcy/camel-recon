package io.m9rcy.recon.processor;


import io.m9rcy.recon.domain.TransactionData;
import io.m9rcy.recon.domain.TransactionsRepository;
import io.m9rcy.recon.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class EnrichTransactionProcessor implements Processor {

    private TransactionsRepository transactionsRepository;

    public EnrichTransactionProcessor(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        var incomingData = (Transaction) exchange.getIn().getBody();
        log.info("Before {}", incomingData.toString());
        Optional <TransactionData> optionalTransactionData = transactionsRepository.findByPaymentId(incomingData.getPaymentId());

        if (optionalTransactionData.isPresent()) {
            TransactionData data = optionalTransactionData.get();
            log.info("DB {}", data.toString());
            incomingData.setTransactionId(data.getTransactionId());
            incomingData.setLocalAccountNumber(data.getAccountNo());
        }
        incomingData.setTransactionCode(incomingData.getTransactionType().equals('P') ? "50" : "00");
        log.info("After {}",incomingData.toString());

    }
}
