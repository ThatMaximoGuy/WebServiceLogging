package com.interlocsolutions.maximo.ws;

import static junitx.framework.FileAssert.assertBinaryEquals;
import static junitx.framework.StringAssert.assertEndsWith;
import static junitx.framework.StringAssert.assertStartsWith;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the WebServiceLogging class.
 *
 * @author Martin Nichol
 *
 */
public class WebServiceLoggingTest {

    /**
     * Delete any files created by testLogSuccess().
     */
    @Before
    @After
    public void cleanupFiles() {
        File[] files = getLogSuccessFiles();
        for (File f : files) {
            f.delete();
        }
    }

    /**
     * Test creating a temporary file when {@code interloc.mea.log property} is
     * not defined.
     *
     * @throws IOException if an IO problem occurs.
     */
    @Test
    public void testCreateTemporaryFile() throws IOException {
        File f = null;
        try {
            WebServiceLogging logger = new WebServiceLogging();
            f = logger.createTemporaryFile("1A", "2B", "3C", ".xml");

            assertStartsWith(".", f.getPath());
            assertStartsWith("1A_2B_3C", f.getName());
            assertEndsWith(".xml", f.getName());
        } finally {
            if (f != null) {
                f.delete();
            }
        }
    }

    /**
     * Test creating a temporary file when {@code interloc.mea.log property} is
     * set.
     *
     * @throws IOException if an IO problem occurs.
     */
    @Test
    public void testCreateTemporaryFileAltPath() throws IOException {
        File f = null;
        try {
            String iotmp = System.getProperty("java.io.tmpdir");
            System.setProperty("interloc.mea.log", iotmp);

            WebServiceLogging logger = new WebServiceLogging();
            f = logger.createTemporaryFile("1A", "2B", "3C", ".xml");

            assertStartsWith(iotmp, f.getPath());
            assertStartsWith("1A_2B_3C", f.getName());
            assertEndsWith(".xml", f.getName());
        } finally {
            System.clearProperty("interloc.mea.log");
            if (f != null) {
                f.delete();
            }
        }
    }

    /**
     * Test logging a message when everything works.
     */
    @Test
    public void testLogSuccess() {
        // create a factory
        OMFactory factory = OMAbstractFactory.getOMFactory();
        // use the factory to create two namespace objects
        OMNamespace ns1 = factory.createOMNamespace("bar", "x");
        // use the factory to create three elements
        OMElement root = factory.createOMElement("root", ns1);

        WebServiceLogging logger = new WebServiceLogging();
        logger.log("A1", "B2", "C3", root);

        File[] files = getLogSuccessFiles();
        assertEquals(1, files.length);

        URL u = getClass().getClassLoader().getResource("com/interlocsolutions/maximo/ws/testLogSuccess.xml");
        File f = new File(u.getFile());
        assertBinaryEquals(files[0], f);
    }

    /**
     * @return the list of files created by testLogSuccess method.
     */
    protected File[] getLogSuccessFiles() {
        File pwd = new File(".");
        File[] files = pwd.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().startsWith("A1_B2_C3") && pathname.getName().endsWith(".xml");
            }
        });
        return files;
    }
}
