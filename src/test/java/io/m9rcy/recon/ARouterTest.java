package io.m9rcy.recon;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.BootstrapWith;

import java.nio.charset.StandardCharsets;

import static org.apache.camel.builder.Builder.simple;

@CamelSpringBootTest
@SpringBootTest(classes = CamelReconApplication.class)
public class ARouterTest {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Test
    public void testAdvisingRouteAndMockingEndpoints() throws Exception {
        // Advising endpoint in AOP style by providing routeId
        AdviceWith.adviceWith(camelContext, "processFile",
                              a -> {
                                  // weaving particular node in the route by id
                                  a.weaveById("outSftpEndpoint")
                                   // providing advised (weaved) node replacement
                                   .replace()
                                   .to("mock:output");
                              });

        // Getting mock endpoint object from the context
        MockEndpoint mock = camelContext.getEndpoint("mock:output", MockEndpoint.class);

        // setting expectation for the contents of messages that will arrive at mock
        mock.expectedBodiesReceived(getFileContent("/generated.txt"));

        // Sending test data to route consumer
        producerTemplate.sendBody("direct:processFile", getFileContent("/generated.txt"));

        // Asserting expectations are satisfied
        mock.assertIsSatisfied();
    }


    private String getFileContent(String filename) throws Exception {
        return IOUtils.toString(this.getClass().getResourceAsStream(filename), StandardCharsets.UTF_8.name());
    }
}
