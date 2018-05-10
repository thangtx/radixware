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
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.units.persocomm.interfaces.ICommunicationAdapter;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.schemas.personalcommunications.MessageDocument;

public class LoopBackUnit extends PersoCommUnit {

    private final List<MessageDocument> queue = new ArrayList<>();

    public LoopBackUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
    }

    @Override
    public ICommunicationAdapter getCommunicationAdapter(CommunicationMode mode) throws IOException {
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

    @Override
    public boolean supportsTransmitting() {
        return true;
    }

    @Override
    public boolean supportsReceiving() {
        return true;
    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.FILE_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_FILE.getValue();
    }

    protected class LoopbackCommunicationAdapter implements ICommunicationAdapter {

        public LoopbackCommunicationAdapter() {
        }

        @Override
        public MessageSendResult sendMessage(MessageWithMeta messageWithMeta) throws DPCSendException {
            final MessageDocument msg = messageWithMeta.xDoc;
            queue.add(msg);
            return MessageSendResult.UNKNOWN;
        }

        @Override
        public MessageDocument receiveMessage() throws DPCRecvException {
            return queue.remove(0);
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public boolean isPersistent() {
            return false;
        }

    }
}
