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
package org.radixware.kernel.server.units.persocomm.interfaces;

import java.io.Closeable;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageStatistics;

/**
 * <p>
 * This interface describes interface to different communication adapters for
 * the communication unit</p>
 */
public interface ICommunicationUnit<T> {

    public interface CommunicationAdapter<T> extends Closeable {

        /**
         *
         * @param messageId
         * @param md
         * @return
         * @throws DPCSendException
         */
        T prepareMessage(Long messageId, MessageDocument md) throws DPCSendException;

        /**
         *
         * @param messageId
         * @param msg
         * @return true if adapter can send the new next message, otherwise
         * false
         * @throws DPCSendException
         */
        boolean sendMessage(Long messageId, T msg) throws DPCSendException;

        /**
         *
         * @param stat
         * @throws DPCSendException
         */
        void setStatistics(MessageStatistics stat) throws DPCSendException;

        /**
         *
         * @return @throws DPCSendException
         */
        MessageStatistics getStatistics() throws DPCSendException;

        /**
         *
         * @return @throws DPCRecvException
         */
        T receiveMessage() throws DPCRecvException;

        /**
         *
         * @param msg
         * @return
         * @throws DPCRecvException
         */
        MessageDocument unprepareMessage(T msg) throws DPCRecvException;
    }

    public enum CommunicationMode {

        TRANSMIT, RECEIVE
    }

    /**
     * <p>
     * Get communication adapter for the unit</p>
     *
     * @param <T> Intermediate data format for the communication adapter
     * @param mode communication mode
     * @return communication adapter
     * @throws DPCRecvException
     * @throws DPCSendException
     */
    <T> CommunicationAdapter<T> getCommunicationAdapter(CommunicationMode mode) throws DPCRecvException, DPCSendException;
}
