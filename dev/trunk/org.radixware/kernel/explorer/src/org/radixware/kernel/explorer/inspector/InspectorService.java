/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.explorer.inspector;

import com.compassplus.schemas.inspectorWsdl.ObjectInfoRqDocument;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.exceptions.AppError;
import org.w3c.dom.Node;


@WebServiceProvider
@ServiceMode(Service.Mode.MESSAGE)
public class InspectorService implements Provider<SOAPMessage> {

    private final InspectorImplementation inspector;
    private Endpoint endpoint;

    public InspectorService(final InspectorImplementation inspector) {
        this.inspector = inspector;
    }

    public synchronized void start() {
        if (endpoint != null) {
            throw new IllegalStateException("Endpoint has already been started");
        }
        
        endpoint = Endpoint.publish("http://localhost:9090/inspector", this);
    }

    public synchronized void stop() {
        if (endpoint != null) {
            endpoint.stop();
        }
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public SOAPMessage invoke(final SOAPMessage request) {
        try {
            final XmlObject xmlRq = XmlObject.Factory.parse(request.getSOAPBody().getFirstChild());
            final XmlObject xmlRs;
            if (xmlRq instanceof ObjectInfoRqDocument) {
                xmlRs = inspector.getObjectInfo((ObjectInfoRqDocument) xmlRq);
            } else {
                throw new IllegalArgumentException("Illegal request: " + request);
            }
            return createSOAPMessage(xmlRs);
        } catch (Exception ex) {
            throw new AppError("Request process exception",ex);
        }
    }

    private SOAPMessage createSOAPMessage(final XmlObject body) throws SOAPException {
        final SOAPMessage message = MessageFactory.newInstance().createMessage();
        final Node bodyContentNode = message.getSOAPBody().getOwnerDocument().importNode(body.getDomNode().getFirstChild(), true);
        message.getSOAPBody().appendChild(bodyContentNode);
        return message;
            
    }
}
