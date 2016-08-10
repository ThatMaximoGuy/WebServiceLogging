package com.interlocsolutions.maximo.ws;

import java.rmi.RemoteException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;

import psdi.iface.webservices.MOSWebService;

/**
 * A MOSWebService that will log requests and replies.
 *
 * @author Martin Nichol
 *
 */
public class LoggingMOSWebService extends MOSWebService {

    /**
     * Constructor.
     */
    public LoggingMOSWebService() {
        super();
    }

    /**
     * Get an object to handle logging the requests and replies.
     *
     * @return an object to handle logging the webservice request and reply.
     */
    protected WebServiceLogging newWebServiceLogging() {
        return new WebServiceLogging();
    }

    @Override
    public OMElement processDocument(OMElement arg0) throws RemoteException {
        String serviceName = ((String) MessageContext.getCurrentMessageContext().getParameter("MaximoService").getValue());
        String logId = MessageContext.getCurrentMessageContext().getLogCorrelationID();
        WebServiceLogging logger = newWebServiceLogging();

        logger.log(serviceName, logId, "request", arg0);
        OMElement rc = super.processDocument(arg0);
        logger.log(serviceName, logId, "reply", rc);

        return rc;
    }
}
