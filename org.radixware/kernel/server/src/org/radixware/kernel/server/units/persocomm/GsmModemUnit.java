/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.units.persocomm;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.persocomm.interfaces.ICommunicationAdapter;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.smslib.GatewayException;
import org.smslib.IOutboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

public class GsmModemUnit extends PersoCommUnit {

    private Service srv;
    private final Set<String> busy = new HashSet<>();
    private int modemCount;
    private int lastSendIdx;

    public GsmModemUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
    }

    @Override
    public ICommunicationAdapter getCommunicationAdapter(CommunicationMode mode) throws IOException {
        switch (mode) {
            case TRANSMIT:
                return this.new WriteGSMModemCommunicationAdapter();
            case RECEIVE:
                return this.new ReadGSMModemCommunicationAdapter();
            default:
                throw new UnsupportedOperationException("Communication mode [" + mode + "] is not supported in the [" + this.getClass().getSimpleName() + "] adapter!");
        }
    }

    @Override
    public OptionsGroup optionsGroup(final Options options) {
        return new OptionsGroup();
    }

    @Override
    public boolean supportsTransmitting() {
        return true;
    }

    @Override
    public boolean supportsReceiving() {
        return false;
    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.GSM_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_GSM_MODEM.getValue();
    }

    @Override
    protected void checkOptions(Options options) throws Exception {
    }

    @Override
    protected boolean prepareImpl() {
        if (super.prepareImpl()) {
            try {
                final Options4GSM[] opts = getDBQuery().getGsmModemOptions(getPrimaryUnitId());

                if (opts.length > 0) {
                    srv = new Service();

                    for (Options4GSM item : opts) {
                        final SerialModemGateway gateway = new SerialModemGateway("modem.com" + String.valueOf(item.comPort), "COM" + item.comPort, (int) item.speed, "", "");

                        gateway.setInbound(true);
                        gateway.setOutbound(true);
                        srv.addGateway(gateway);
                    }

                    modemCount = opts.length;
                    busy.clear();
                    srv.startService();

                    return true;
                }
            } catch (Throwable ex) {
                throw new RadixError(Messages.ERR_CANT_READ_OPTIONS + " " + ex.getMessage(), ex);
            }
        }
        return false;
    }

    @Override
    protected void unprepareImpl() {
        if (srv != null) {
            try {
                srv.stopService();
                srv = null;
            } catch (TimeoutException | GatewayException | IOException | InterruptedException ex) {
                Logger.getLogger(GsmModemUnit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        super.unprepareImpl();
    }

    private class WriteGSMModemCommunicationAdapter implements ICommunicationAdapter {

        public WriteGSMModemCommunicationAdapter() throws IOException {
        }

        @Override
        public MessageSendResult sendMessage(final MessageWithMeta messageWithMeta) throws DPCSendException {
            final Long messageId = messageWithMeta.id;
            final MessageDocument md = messageWithMeta.xDoc;

            final long now = System.nanoTime();
            final OutboundMessage msg = new OutboundMessage(md.getMessage().getAddressTo(), md.getMessage().getBody());
            lastSendIdx = (lastSendIdx + 1) % modemCount;

            final SerialModemGateway gateway = (SerialModemGateway) srv.getGatewayList().get(lastSendIdx);
            final String gatewayId = gateway.getGatewayId();

            while (busy.contains(gatewayId)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new DPCRecoverableSendException("Unable to send message #" + messageId + ": adapter thread was interrupted");
                }
            }
            
            gateway.setOutboundNotification(new IOutboundMessageNotification() {
                @Override
                public void process(String string, OutboundMessage om) {
                    busy.remove(gatewayId);
                    final MessageSendResult result = new MessageSendResult(messageId, now)
                            .setMessageWithMeta(messageWithMeta);
                    sendCallback(result);
                }
            });
            busy.add(gateway.getGatewayId());
            srv.queueMessage(msg, gateway.getGatewayId());
            return MessageSendResult.UNKNOWN;
        }

        @Override
        public MessageDocument receiveMessage() throws DPCRecvException {
            throw new IllegalStateException("This method can't be called for adapter to transmit");
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public boolean isPersistent() {
            return false;
        }
    }

    private class ReadGSMModemCommunicationAdapter implements ICommunicationAdapter {

        private List<InboundMessage> msgList;

        public ReadGSMModemCommunicationAdapter() throws IOException {
        }

        @Override
        public MessageSendResult sendMessage(MessageWithMeta messageWithMeta) throws DPCSendException {
            throw new IllegalStateException("This method can't be called for adapter to receive");
        }

        private InboundMessage getMessage() throws DPCRecvException {
            if (msgList == null) {
                msgList = new ArrayList<>();
//                try{srv.readMessages(msgList, MessageClasses.ALL);
//                } catch (TimeoutException | GatewayException | IOException |InterruptedException ex) {
//                    throw new DPCRecvException(ex.getMessage(),ex);
//                }
            }
            if (msgList.size() > 0) {
                return msgList.remove(0);
            } else {
                msgList = null;
                return null;
            }
        }

        @Override
        public MessageDocument receiveMessage() throws DPCRecvException {
            final InboundMessage msg = getMessage();
            if (msg == null) {
                return null;
            }
            final MessageDocument m = MessageDocument.Factory.newInstance();

            m.getMessage().setAddressFrom(msg.getOriginator());
            Timestamp ts = new Timestamp(msg.getDate().getTime());
            m.getMessage().setSentTime(ts);
            m.getMessage().setBody(msg.getText());

            return m;
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public boolean isPersistent() {
            return false;
        }
    }

    static final public class Options4GSM {

        public long comPort;
        public long speed;

        public Options4GSM() {
        }
    }
}
