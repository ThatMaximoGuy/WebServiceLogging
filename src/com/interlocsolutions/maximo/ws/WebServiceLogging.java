package com.interlocsolutions.maximo.ws;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

/**
 * @author Martin Nichol
 *
 */
public class WebServiceLogging {

    /** to format the current date/time into a format for the temp filename. */
    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss.SSSS");

    /** the logger for reporting problems. */
    protected Logger logger = Logger.getLogger("maximo.meaweb");

    /**
     * Constructor.
     */
    public WebServiceLogging() {
        super();
    }

    /**
     * Log a request to a file on disk.
     *
     * @param name the webservice name being logged.
     * @param uuid a unique idendifier for grouping request/replies together.
     * @param stage whether the element represents a request or reply.
     * @param ome the element to log.
     */
    public void log(String name, String uuid, String stage, OMElement ome) {
        try {
            File xmlOut = createTemporaryFile(name, uuid.replace(':', '_'), stage, ".xml");

            BufferedWriter bw = new BufferedWriter(new FileWriter(xmlOut));
            bw.write(ome.toString());
            bw.close();
        } catch (IOException e) {
            logger.warn("Unable to save request.", e);
        }
    }

    /**
     * Create a temporary file including the prefix, the current timestamp,
     * the stage and the suffix plus a uniqifier from Java.
     *
     * @param prefix the file name prefix.
     * @param uuid a unique idendifier for grouping request/replies together.
     * @param stage the stage (request or reply)
     * @param suffix the filename extension.
     * @return a file for logging the request.
     * @throws IOException if an IO problem occurs.
     */
    protected File createTemporaryFile(String prefix, String uuid, String stage, String suffix) throws IOException {
        File directory = new File(System.getProperty("interloc.mea.log", "."));

        String dstamp = sdf.format(new Date());
        File rc = File.createTempFile(String.format("%s_%s_%s_%s", prefix, uuid, stage, dstamp), suffix, directory);
        return rc;
    }

}
