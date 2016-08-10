package com.interlocsolutions.maximo.ws;

import java.rmi.RemoteException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;

import psdi.iface.webservices.EnterpriseWebService;

/**
 * An EnterpriseWebService that will log requests and replies.
 *
 * @author Martin Nichol
 *
 */
public class LoggingEnterpriseWebService extends EnterpriseWebService {

    /**
     * Constructor.
     */
    public LoggingEnterpriseWebService() {
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
    public OMElement processDocument(OMElement element) throws RemoteException {
        String serviceName = ((String) MessageContext.getCurrentMessageContext().getParameter("MaximoService").getValue());
        String logId = MessageContext.getCurrentMessageContext().getLogCorrelationID();
        WebServiceLogging logger = newWebServiceLogging();

        logger.log(serviceName, logId, "request", element);
        OMElement rc = super.processDocument(element);
        logger.log(serviceName, logId, "reply", rc);

        return rc;
    }

}
