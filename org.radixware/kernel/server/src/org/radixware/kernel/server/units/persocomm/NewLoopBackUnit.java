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
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.units.persocomm.interfaces.IDatabaseConnectionAccess;
import org.radixware.kernel.server.units.persocomm.interfaces.IExtendedRadixTrace;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageStatistics;

public class NewLoopBackUnit extends NewPCUnit {

    private final List<MessageDocument> queue = new ArrayList<MessageDocument>();

    public NewLoopBackUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
    }

    protected NewLoopBackUnit(final Instance instModel, final Long id, final String title, final IDatabaseConnectionAccess dbca, final IExtendedRadixTrace trace) {
        super(instModel, id, title, dbca, trace);
    }

    @Override
    public CommunicationAdapter getCommunicationAdapter(CommunicationMode mode) throws DPCRecvException, DPCSendException {
        switch (mode) {
            case TRANSMIT:
            case RECEIVE:
                return this.new LoopbackCommunicationAdapter();
            default:
                throw new UnsupportedOperationException("Communication mode [" + mode + "] is not supported in the [" + this.getClass().getSimpleName() + "] adapter!");
        }
    }

    @Override
    public OptionsGroup optionsGroup(Options options) {
        return new OptionsGroup();
    }

    @Override
    protected void checkOptions(Options options) throws Exception {
    }

    @Override public boolean supportsTransmitting() {return true;}
    @Override public boolean supportsReceiving() {return true;}
    
    @Override
    public String getUnitTypeTitle() {
        return PCMessages.FILE_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_FILE.getValue();
    }

    @Override
    public MessageStatistics getTicket() {
        return MessageStatistics.Factory.newInstance();
    }
    
    protected class LoopbackCommunicationAdapter implements CommunicationAdapter<MessageDocument> {

        private MessageStatistics stat;

        public LoopbackCommunicationAdapter() {
        }

        @Override
        public MessageDocument prepareMessage(Long messageId, MessageDocument md) throws DPCSendException {
            return md;
        }

        @Override
        public boolean sendMessage(Long messageId, MessageDocument msg) throws DPCSendException {
            queue.add(msg);
            return true;
        }

        @Override
        public void setStatistics(final MessageStatistics stat) throws DPCSendException {
            this.stat = stat;
        }

        @Override
        public MessageStatistics getStatistics() throws DPCSendException {
            return stat;
        }

        @Override
        public MessageDocument receiveMessage() throws DPCRecvException {
            return queue.remove(0);
        }

        @Override
        public MessageDocument unprepareMessage(MessageDocument msg) throws DPCRecvException {
            return msg;
        }

        @Override
        public void close() throws IOException {
        }
    }
}
