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
package org.radixware.kernel.server.units.persocomm;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.radixware.kernel.common.enums.EUnitType;
import org.smslib.GatewayException;
import org.smslib.IOutboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.MessageClasses;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.schemas.personalcommunications.MessageDocument;

public class GSMModemUnit extends PCUnit {

    private final Service srv;
    private int lastSendIdx;
    private int modemCount;
    private List<String> busy;

    public GSMModemUnit(Instance instance, Long id, String title) {
        super(instance, id, title);
        srv = new Service();
        lastSendIdx = 0;
    }

    @Override
    protected boolean startImpl() throws Exception {
        if (!super.startImpl()) {
            return false;
        }
        modemCount = queries.readGSMModem(srv);
        busy = new ArrayList<String>();
        srv.startService();
        return true;
    }

    @Override
    protected void stopImpl() {
        try {
            srv.stopService();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (GatewayException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.stopImpl();
    }

    @Override
    public String optionsToString() {
        return "";
    }

    @Override
    protected void recvMessages() throws DPCRecvException {
        List msgList = new ArrayList();
        try {
            srv.readMessages(msgList, MessageClasses.ALL);
            for (int i = 0; i < msgList.size(); i++) {
                InboundMessage msg = (InboundMessage) (msgList.get(i));
                MessageDocument m = MessageDocument.Factory.newInstance();
                m.getMessage().setAddressFrom(msg.getOriginator());
                Timestamp ts = new Timestamp(msg.getDate().getTime());
                m.getMessage().setSentTime(ts);
                m.getMessage().setBody(msg.getEncodedText());
                recvMessage(m);
            }
        } catch (Exception e) {
            throw new DPCRecvException(PCMessages.RECV_MESS_ERROR, e);
        } finally {
            try {
                srv.stopService();
            } catch (TimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (GatewayException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void send(MessageDocument m, Long id) throws DPCSendException {
        OutboundMessage msg = new OutboundMessage(m.getMessage().getAddressTo(), m.getMessage().getBody());
        lastSendIdx++;
        if (lastSendIdx >= modemCount) {
            lastSendIdx = 0;
        }
        List l = srv.getGatewayList();
        SerialModemGateway gateway = (SerialModemGateway) l.get(lastSendIdx);
        while (findBusyList(gateway.getGatewayId())) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        OutboundNotification outboundNotification = new OutboundNotification(this, id);
        gateway.setOutboundNotification(outboundNotification);
        busy.add(gateway.getGatewayId());
        srv.queueMessage(msg, gateway.getGatewayId());
    }

    public class OutboundNotification implements IOutboundMessageNotification {

        GSMModemUnit unit;
        Long id;

        public OutboundNotification(GSMModemUnit unit, Long id) {
            this.unit = unit;
            this.id = id;
        }

        @Override
        public void process(String gatewayId, OutboundMessage msg) {
            for (int i = 0; i < busy.size(); i++) {
                if (busy.get(i).equals(gatewayId)) {
                    busy.remove(i);
                    break;
                }
            }
            unit.sendCallback(id, null);
        }
    }

    boolean findBusyList(String gatewayId) {
        for (int i = 0; i < busy.size(); i++) {
            if (busy.get(i).equals(gatewayId)) {
                return true;
            }
        }
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
}
