package com.interlocsolutions.maximo.ws;

import java.rmi.RemoteException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;

import psdi.iface.webservices.action.ActionWebService;

/**
 * An ActionWebService that will log requests and replies.
 *
 * @author Martin Nichol
 *
 */
public class LoggingActionWebService extends ActionWebService {

    /**
     * Constructor.
     */
    public LoggingActionWebService() {
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
    public OMElement action(OMElement element) throws RemoteException {
        String serviceName = ((String) MessageContext.getCurrentMessageContext().getParameter("MaximoService").getValue());
        String logId = MessageContext.getCurrentMessageContext().getLogCorrelationID();
        WebServiceLogging logger = newWebServiceLogging();

        logger.log(serviceName, logId, "request", element);
        OMElement rc = super.action(element);
        logger.log(serviceName, logId, "reply", rc);

        return rc;
    }
}
