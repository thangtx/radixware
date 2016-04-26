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

package org.radixware.kernel.server.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.cxf.BusException;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.ConduitInitiator;
import org.apache.cxf.transport.ConduitInitiatorManager;
import org.apache.cxf.transport.MessageObserver;
import org.apache.cxf.ws.addressing.EndpointReferenceType;


public class RadixClientConduitInitiatorManager implements ConduitInitiatorManager {

    private RadixClientConduit conduit;

    public RadixClientConduit prepare(final IConduitDelegate delegate) {
        this.conduit = new RadixClientConduit(delegate);
        return this.conduit;
    }

    public void reset() {
        this.conduit = null;
    }

    @Override
    public void registerConduitInitiator(String name, ConduitInitiator factory) {
    }

    @Override
    public void deregisterConduitInitiator(String name) {
    }

    @Override
    public ConduitInitiator getConduitInitiator(String name) throws BusException {
        return new RadixClientConduitInitator(conduit);
    }

    @Override
    public ConduitInitiator getConduitInitiatorForUri(String uri) {
        return new RadixClientConduitInitator(conduit);
    }

    public static class RadixClientConduitInitator implements ConduitInitiator {

        private final RadixClientConduit conduit;

        public RadixClientConduitInitator(final RadixClientConduit conduit) {
            this.conduit = conduit;
        }

        @Override
        public Conduit getConduit(EndpointInfo targetInfo) throws IOException {
            return getConduit(targetInfo, null);
        }

        @Override
        public Conduit getConduit(EndpointInfo localInfo, EndpointReferenceType target) throws IOException {
            conduit.setEndpointInfo(localInfo, target);
            return conduit;
        }

        @Override
        public Set<String> getUriPrefixes() {
            return Collections.emptySet();
        }

        @Override
        public List<String> getTransportIds() {
            return Collections.emptyList();
        }
    }

    public static class RadixClientConduit implements Conduit, IResponceDataListener {

        private static final int INITIAL_OUTPUT_BUFFER_SIZE = 1024;
        private MessageObserver observer = null;
        private EndpointReferenceType target;
        private final ByteArrayOutputStream bos = new ByteArrayOutputStream(INITIAL_OUTPUT_BUFFER_SIZE);
        private Message message;
        private final IConduitDelegate delegate;
        private Exception invocationException;
        private Map<String, String> lastResponceAttrs;

        public RadixClientConduit(final IConduitDelegate delegate) {
            this.delegate = delegate;
        }

        private void setEndpointInfo(final EndpointInfo endPointInfo, final EndpointReferenceType target) {
            if (target != null) {
                this.target = target;
            } else {
                this.target = endPointInfo.getTarget();
            }
        }

        @Override
        public void prepare(Message message) throws IOException {
            bos.reset();
            message.setContent(OutputStream.class, bos);
        }

        @Override
        public void close(Message message) throws IOException {
            try {
                if (message.getExchange().getInMessage() == message) {
                    return;
                }
                this.message = message;
                delegate.sendRequest(target, bos.toByteArray());
                delegate.setResponceDataListener(this);
            } catch (IOException ex) {
                invocationException = ex;
                throw ex;
            }
        }

        @Override
        public void onResponce(byte[] data) {
            Message inMessage = new MessageImpl();
            inMessage.setExchange(message.getExchange());
            message.getExchange().setInMessage(message);
            inMessage.setContent(InputStream.class, new ByteArrayInputStream(data));
            inMessage.put(Conduit.class, this);
            observer.onMessage(inMessage);
        }

        @Override
        public EndpointReferenceType getTarget() {
            return target;
        }

        @Override
        public void close() {
        }

        @Override
        public void setMessageObserver(MessageObserver observer) {
            this.observer = observer;
        }

        @Override
        public MessageObserver getMessageObserver() {
            return observer;
        }

        public Map<String, String> getLastResponceAttrs() {
            return lastResponceAttrs;
        }

        public Exception getInvocationException() {
            return invocationException;
        }
    }
}
