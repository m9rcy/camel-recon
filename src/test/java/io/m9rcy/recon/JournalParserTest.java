package io.m9rcy.recon;

import io.m9rcy.recon.model.ControlHeader;
import io.m9rcy.recon.model.ControlTrailer;
import io.m9rcy.recon.model.JournalGroup;
import io.m9rcy.recon.model.Transaction;
import org.beanio.BeanReader;
import org.beanio.BeanReaderException;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class JournalParserTest extends ParserTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(JournalParserTest.class);

	private StreamFactory factory;


	@Test
	public void testReadFixedLengthRecordWithXmlDefinition() throws IOException {
		LOGGER.info("Loading Stream Definition ...");
		factory = newStreamFactory("/mappings.xml");

		LOGGER.info("Testing Stream {} ...", "p-oe");
		BeanReader in = factory.createReader("p-oe", new InputStreamReader(
				getClass().getResourceAsStream("/sample.txt")));

		Object obj = in.read();
		JournalGroup journal = (obj instanceof JournalGroup ? (JournalGroup) obj : null);
		in.close();

		BeanWriter out = factory.createWriter("p-oe-ob", new File("generated.txt"));

		for (Transaction txn: journal.getTransactions()) {
			String txnCode = txn.getTransactionType().equals('P') ? "50" : "00";
			txn.setTransactionCode(txnCode);
			txn.setTransactionId("001");
			txn.setLocalAccountNumber("0212341234567123");
		}

		out.write(journal);
		out.flush();
		out.close();
	}

	@Test
	public void testWriteFixedLengthRecordWithXmlDefinition() throws IOException {
		LOGGER.info("Loading Stream Definition ...");
		factory = newStreamFactory("/mappings.xml");
		test("p-oe", "/sample.txt");
	}


	/**
     * Fully parses the given file.
     * @param name the name of the stream
     * @param filename the name of the file to test
     */
    protected void test(String name, String filename) {
        test(name, filename, -1);
    }


    	/**
     * Fully parses the given file.
     * @param streamName the name of the stream
     * @param filename the name of the file to test
     * @param errorLineNumber
     */
    protected void test(String streamName, String filename, int errorLineNumber) {

    	LOGGER.info("Testing Stream {} ...", streamName);
    	BeanReader in = factory.createReader(streamName, new InputStreamReader(
            getClass().getResourceAsStream(filename)));

    	test(in, filename, errorLineNumber);
    }

	protected void test(BeanReader in, String filename, int errorLineNumber) {
		try {
			int index = 1;
			LOGGER.info("Reading Iteration {} ...", index);
			Object item = in.read();
			while (item != null) {

				if (item instanceof JournalGroup) {
					JournalGroup functionGroup = (JournalGroup) item;
					LOGGER.info("Read FunctionGroup.");

					ControlHeader header = functionGroup.getControlHeader();
					if (header == null) {
						LOGGER.info("   -> No Function Group Header.");
					} else {
						String type = header.getType();
						String fileName = header.getFileName();
						LOGGER.info("   -> Function Group Header, type [{}], filename [{}].", type, fileName);
					}

					List<Transaction> transactions = functionGroup.getTransactions();
					if ((transactions == null) || (transactions.isEmpty())) {
						LOGGER.info("      -> No Transactions.");
					} else {
						int nbTransactions = transactions.size();
						LOGGER.info("      -> List of {} Transactions.", nbTransactions);
						for (int j = 0; j < nbTransactions; j++) {
							Transaction transaction = transactions.get(j);
							String type = transaction.getType();
							BigDecimal amount = transaction.getAmount();
							LOGGER.info("      -> Transaction {}, type [{}], amount [{}].", j + 1, type, amount);
						}
					}


					ControlTrailer trailer = functionGroup.getControlTrailer();
					if (trailer == null) {
						LOGGER.info("   -> No Function Group Trailer.");
					} else {
						LOGGER.info("   -> Function Group Trailer, type [{}].", trailer.getType());
					}

				} else {
					LOGGER.info("Read Item Class = {}.", item
							.getClass()
							.getSimpleName());
				}

				index++;
				LOGGER.info("Reading Iteration {} ...", index);
				item = in.read();
			}
			LOGGER.info("End at Iteration {}.", index);

		} catch (BeanReaderException ex) {
			if (errorLineNumber > 0) {
				// assert the line number from the exception matches expected
				Assertions.assertEquals(errorLineNumber, ex
						.getRecordContext()
						.getLineNumber());
			}
			throw ex;
		} finally {
			in.close();
		}
	}
}
