/*
 * Copyright 2010-2011 Kevin Seim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.m9rcy.recon;

import org.beanio.BeanReader;
import org.beanio.InvalidRecordException;
import org.beanio.RecordContext;
import org.beanio.StreamFactory;
import org.beanio.internal.util.IOUtil;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ParserTest {

    protected static String lineSeparator = System.getProperty("line.separator");
    
    /**
     * Loads the contents of a file into a String.
     * @param filename the name of the file to load
     * @return the file contents
     * @throws IOException if an I/O error occurs
     */
    public String load(String filename) {
        Reader in = new InputStreamReader(getClass().getResourceAsStream(filename));
        StringBuilder s = new StringBuilder();
        try {
            int n = -1;
            char [] c = new char[1024];
            while ((n = in.read(c)) != -1) {
                s.append(c, 0, n);
            }
            return s.toString();
        }
        catch (IOException ex) {
            throw new IllegalStateException("IOException caught", ex);
        }
        finally {
            IOUtil.closeQuietly(in);
        }
    }
    
    /**
     * 
     * @param config
     * @return
     * @throws IOException
     */
    protected StreamFactory newStreamFactory(String config) throws IOException {
        StreamFactory factory = StreamFactory.newInstance();
        loadMappingFile(factory, config);
        return factory;
    }
    
    protected void loadMappingFile(StreamFactory factory, String config) throws IOException {
        InputStream in = getClass().getResourceAsStream(config);
        try {
            factory.load(in);
        }
        finally {
            IOUtil.closeQuietly(in);
        }
    }

    protected void assertRecordError(BeanReader in, int lineNumber, String recordName, String message) {
        try {
            in.read();
            Assertions.fail("Record expected to fail validation");
        }
        catch (InvalidRecordException ex) {
            Assertions.assertEquals(recordName, in.getRecordName());
            Assertions.assertEquals(lineNumber, in.getLineNumber());

            RecordContext ctx = ex.getRecordContext();
            Assertions.assertEquals(recordName, ctx.getRecordName());
            Assertions.assertEquals(lineNumber, ctx.getLineNumber());
            for (String s : ctx.getRecordErrors()) {
                Assertions.assertEquals(message, s);
            }
        }
    }

    protected void assertFieldError(BeanReader in, int lineNumber, String recordName,
            String fieldName, String fieldText, String message) {
        assertFieldError(in, lineNumber, recordName, fieldName, 0, fieldText, message);
    }
    
    protected void assertFieldError(BeanReader in, int lineNumber, String recordName,
        String fieldName, int fieldIndex, String fieldText, String message) {
        try {
            in.read();
            Assertions.fail("Record expected to fail validation");
        }
        catch (InvalidRecordException ex) {
            Assertions.assertEquals(recordName, in.getRecordName());
            Assertions.assertEquals(lineNumber, in.getLineNumber());

            RecordContext ctx = ex.getRecordContext();
            Assertions.assertEquals(recordName, ctx.getRecordName());
            Assertions.assertEquals(lineNumber, ctx.getLineNumber());
            Assertions.assertEquals(fieldText, ctx.getFieldText(fieldName, fieldIndex));
            for (String s : ctx.getFieldErrors(fieldName)) {
                Assertions.assertEquals(message, s);
            }
        }
    }
}
