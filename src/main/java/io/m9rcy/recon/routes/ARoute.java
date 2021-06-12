package io.m9rcy.recon.routes;

import io.m9rcy.recon.model.JournalGroup;
import io.m9rcy.recon.model.Transaction;
import io.m9rcy.recon.processor.ArrayListAggregationStrategy;
import io.m9rcy.recon.processor.EnrichTransactionProcessor;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.builder.ProcessClause;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.SimpleBuilder;
import org.apache.camel.component.file.remote.SftpEndpoint;
import org.apache.camel.dataformat.beanio.BeanIODataFormat;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ARoute extends RouteBuilder {

    private final SftpEndpoint inSFTPEndpoint;
    private final SftpEndpoint outSftpEndpoint;
    private final EnrichTransactionProcessor enrichTransactionProcessor;

    public ARoute(@Qualifier("inSFTPEndpoint") SftpEndpoint inSFTPEndpoint, @Qualifier("outSFTPEndpoint") SftpEndpoint outSftpEndpoint,
                  EnrichTransactionProcessor enrichTransactionProcessor) {
        this.inSFTPEndpoint = inSFTPEndpoint;
        this.outSftpEndpoint = outSftpEndpoint;
        this.enrichTransactionProcessor = enrichTransactionProcessor;
    }

    @Override
    public void configure() {
        DataFormat journalFormat = new BeanIODataFormat(
                "mappings.xml",
                "p-oe");
        DataFormat hostFormat = new BeanIODataFormat(
                "mappings.xml",
                "p-oe-ob");

        from(inSFTPEndpoint)
                .unmarshal(journalFormat)
                .log("Formatted to ${body[0].transactions}")
                .enrich("direct:enrichData", new AggregationStrategy() {
                    @Override
                    public Exchange aggregate(Exchange original, Exchange enrichResponse) {
                        final JournalGroup journalGroup = original.getIn().getBody(JournalGroup.class);
                        final List updatedList = enrichResponse.getIn().getBody(List.class);
                        journalGroup.setTransactions(updatedList);
                        original.getIn().setBody(journalGroup);
                        return original;
                    }
                })
                .log("Downloaded file .${file:name} complete.")
                .marshal(hostFormat)
                .log("Uploading contents ${body} / ${headers.CamelFileName}")

                .to(outSftpEndpoint);

        from("direct:enrichData")
                .split(ExpressionBuilder.simpleExpression("${body[0].transactions}"), new ArrayListAggregationStrategy())
                .process(enrichTransactionProcessor)
                .end()
                .log("${body}");
    }
}