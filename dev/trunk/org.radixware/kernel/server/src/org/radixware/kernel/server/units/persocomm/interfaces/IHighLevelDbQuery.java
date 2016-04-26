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

import java.sql.SQLException;
import org.radixware.kernel.server.IDbQueries;
import org.radixware.kernel.server.units.persocomm.NewGSMModemUnit.Options4GSM;
import org.radixware.kernel.server.units.persocomm.NewPCUnit.Options;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageStatistics;

/**
 * <p>This interface describes high level db queries for the PCUnit.</p>
 */
public interface IHighLevelDbQuery extends IDbQueries {
    /**
     * <p>Get options for the given module</p>
     * @return options loaded
     * @throws SQLException 
     */
    Options getOptions() throws SQLException;

    /**
     * <p>Get GSM modem options for the given channel</p>
     * @param channelId channel id to get options
     * @return modem options
     * @throws SQLException 
     */
    Options4GSM[] getGSMOptions(long channelId) throws SQLException;
    
    /**
     * <p>Get message iterator to send</p>
     * @param channelId unit id to select messages for
     * @return selected messages
     * @throws SQLException 
     */
    
    public interface MessageWithId {
        /**
         * <p>Get message id</p>
         * @return message id
         */
        Long getId();
        /**
         * <p>Get message content</p>
         * @return message content
         */
        MessageDocument getMessage();
    }
    
    Iterable<MessageWithId> getMessages2Send(final Long channelId) throws SQLException;

    /**
     * <p>Get suitable channels for the given message</p>
     * @param kind channel type
     * @param address send address
     * @param importance message importance
     * @param unitId actual channel id
     * @param prty routing priority
     * @return list of available channels
     * @throws SQLException 
     */
    Long[] getSuitableChannels(final String kind, final String address, final long importance, final long unitId, final long prty) throws SQLException;
    
    /**
     * <p>Save send message to the archive</p>
     * @param id message id
     * @param errorMess error description string
     * @throws SQLException 
     */
    void save2Sent(final Long id, final String errorMess) throws SQLException;

    /**
     * <p>Save statistics
     * @param messageId
     * @param ticket
     * @throws SQLException 
     */
    void saveStatistics(final Long messageId, final MessageStatistics ticket) throws SQLException;    

    /**
     * <p>Save received message</p>
     * @param m message to save
     * @return message id was created
     * @throws SQLException 
     */
    Long saveReceived(final MessageDocument m) throws SQLException;
    
    /**
     * <p>Process expired messages</p>
     * @throws SQLException 
     */
    void processExpiredMessages() throws SQLException;
    
    /**
     * <p>Clear out message flag</p>
     * @param messageId message id
     * @throws SQLException 
     */
    void clearOutMessageFlag(final Long messageId) throws SQLException;
    
    /**
     * <p>Clear out message flags for the given channel</p>
     * @param channelId channel id
     * @throws SQLException 
     */
    void clearOutMessageFlagAll(final Long channelId) throws SQLException;
    
    /**
     * <p>Assign new channel to the message</p>
     * @param messageId message id
     * @param newChannel new channel
     * @throws SQLException 
     */
    void assignNewChannel(final Long messageId, final Long newChannel) throws SQLException;
    
    /**
     * <p>Mark message as failed</p>
     * @param messageId message id
     * @param isRecoverable message transmission can be recovered
     * @param message error description
     * @throws SQLException 
     */
    void markMessageAsFailed(final Long messageId, final boolean isRecoverable, final String message) throws SQLException;
}
