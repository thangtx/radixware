/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
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
import org.radixware.kernel.server.units.persocomm.interfaces.IDatabaseConnectionAccess;
import org.radixware.kernel.server.units.persocomm.interfaces.IExtendedRadixTrace;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageStatistics;
import org.smslib.GatewayException;
import org.smslib.IOutboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

public class NewGSMModemUnit extends NewPCUnit {

    private Service srv;
    private Set<String> busy = new HashSet<String>();
    private int modemCount;
    private int lastSendIdx;
    private Thread serviceThread = null;

    public NewGSMModemUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
    }

    public NewGSMModemUnit(final Instance instModel, final Long id, final String title, final IDatabaseConnectionAccess dbca, final IExtendedRadixTrace trace) {
        super(instModel, id, title, dbca, trace);
    }

    @Override
    public CommunicationAdapter getCommunicationAdapter(CommunicationMode mode) throws DPCRecvException, DPCSendException {
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

    @Override public boolean supportsTransmitting() {return true;}
    @Override public boolean supportsReceiving() {return false;}
    
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
            try{final Options4GSM[]     opts = getDBQuery().getGSMOptions(getId());
            
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
                Logger.getLogger(NewGSMModemUnit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        super.unprepareImpl();
    }

    private class WriteGSMModemCommunicationAdapter implements CommunicationAdapter<OutboundMessage> {

        private MessageStatistics stat;

        public WriteGSMModemCommunicationAdapter() throws DPCSendException {
        }

        @Override
        public OutboundMessage prepareMessage(final Long messageId, final MessageDocument md) throws DPCSendException {
            return new OutboundMessage(md.getMessage().getAddressTo(), md.getMessage().getBody());
        }

        @Override
        public boolean sendMessage(final Long messageId, final OutboundMessage msg) throws DPCSendException {
            lastSendIdx = (lastSendIdx + 1) % modemCount;

            final SerialModemGateway gateway = (SerialModemGateway) srv.getGatewayList().get(lastSendIdx);
            final String gatewayId = gateway.getGatewayId();

            while (busy.contains(gatewayId)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return false;
                }
            }

            gateway.setOutboundNotification(new IOutboundMessageNotification() {
                @Override
                public void process(String string, OutboundMessage om) {
                    busy.remove(gatewayId);
                    sendCallback(messageId, string);
                }
            });
            busy.add(gateway.getGatewayId());
            srv.queueMessage(msg, gateway.getGatewayId());
            return true;
        }

        @Override
        public void setStatistics(MessageStatistics stat) throws DPCSendException {
            this.stat = stat;
        }

        @Override
        public MessageStatistics getStatistics() throws DPCSendException {
            return stat;
        }

        @Override
        public OutboundMessage receiveMessage() throws DPCRecvException {
            throw new IllegalStateException("This method can't be called for adapter to transmit");
        }

        @Override
        public MessageDocument unprepareMessage(OutboundMessage msg) throws DPCRecvException {
            throw new IllegalStateException("This method can't be called for adapter to transmit");
        }

        @Override
        public void close() throws IOException {
        }
    }

    private class ReadGSMModemCommunicationAdapter implements CommunicationAdapter<InboundMessage> {

        private MessageStatistics stat;
        private List<InboundMessage> msgList;

        public ReadGSMModemCommunicationAdapter() throws DPCRecvException {
        }

        @Override
        public InboundMessage prepareMessage(Long messageId, MessageDocument md) throws DPCSendException {
            throw new IllegalStateException("This method can't be called for adapter to receive");
        }

        @Override
        public boolean sendMessage(Long messageId, InboundMessage msg) throws DPCSendException {
            throw new IllegalStateException("This method can't be called for adapter to receive");
        }

        @Override
        public void setStatistics(MessageStatistics stat) throws DPCSendException {
            this.stat = stat;
        }

        @Override
        public MessageStatistics getStatistics() throws DPCSendException {
            return stat;
        }

        @Override
        public InboundMessage receiveMessage() throws DPCRecvException {
            if (msgList == null) {
                msgList = new ArrayList<InboundMessage>();
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
        public MessageDocument unprepareMessage(InboundMessage msg) throws DPCRecvException {
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
    }

    static final public class Options4GSM {

        public long comPort;
        public long speed;

        public Options4GSM() {
        }
    }
}
