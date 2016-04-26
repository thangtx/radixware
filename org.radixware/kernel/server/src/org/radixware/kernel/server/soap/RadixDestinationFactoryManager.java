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
import java.net.Socket;
import java.util.List;
import java.util.Set;
import org.apache.cxf.BusException;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.transport.MessageObserver;
import org.apache.cxf.ws.addressing.EndpointReferenceType;


public class RadixDestinationFactoryManager implements DestinationFactoryManager {

    private byte[] incomingMessage;
    private final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
    private DestinationImpl destinationImpl;

    public RadixDestinationFactoryManager() {
    }
    
    public void process(byte[] incomingMessageData) {
        this.incomingMessage = incomingMessageData;
        destinationImpl.process();
    }

    public byte[] getOutMessageBytes() {
        return bos.toByteArray();
    }

    @Override
    public void registerDestinationFactory(String string, DestinationFactory df) {
    }

    @Override
    public void deregisterDestinationFactory(String string) {
    }

    @Override
    public DestinationFactory getDestinationFactory(String string) throws BusException {
        return new DestinationFactoryImpl();
    }

    @Override
    public DestinationFactory getDestinationFactoryForUri(String string) {
        return new DestinationFactoryImpl();
    }

    private class DestinationFactoryImpl implements DestinationFactory {

        @Override
        public Destination getDestination(EndpointInfo ei) throws IOException {
            return new DestinationImpl();
        }

        @Override
        public Set<String> getUriPrefixes() {
            return null;
        }

        @Override
        public List<String> getTransportIds() {
            return null;
        }
    }

    private class DestinationImpl implements Destination {

        private MessageObserver observer;

        public DestinationImpl() {
            RadixDestinationFactoryManager.this.destinationImpl = this;
        }

        @Override
        public EndpointReferenceType getAddress() {
            return new EndpointReferenceType();
        }

        @Override
        public Conduit getBackChannel(Message msg, Message msg1, EndpointReferenceType ert) throws IOException {
            return new Conduit() {
                @Override
                public void prepare(Message msg) throws IOException {
                    bos.reset();
                    msg.setContent(OutputStream.class, bos);
                }

                @Override
                public void close(Message msg) throws IOException {
                }

                @Override
                public EndpointReferenceType getTarget() {
                    return new EndpointReferenceType();
                }

                @Override
                public void close() {
                }

                @Override
                public void setMessageObserver(MessageObserver mo) {
                }

                @Override
                public MessageObserver getMessageObserver() {
                    return null;
                }
            };
        }

        @Override
        public void shutdown() {
        }

        @Override
        public MessageObserver getMessageObserver() {
            return observer;
        }

        public void process() {
            if (observer != null) {
                MessageImpl message = new MessageImpl();
                message.setContent(InputStream.class, new ByteArrayInputStream(incomingMessage));
                message.setDestination(DestinationImpl.this);
                observer.onMessage(message);
            }
        }

        @Override
        public void setMessageObserver(MessageObserver observer) {
            this.observer = observer;

        }
    }
}
