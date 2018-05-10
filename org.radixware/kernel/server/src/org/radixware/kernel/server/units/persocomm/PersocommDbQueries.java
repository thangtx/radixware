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

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.enums.EPersoCommDeliveryStatus;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.units.persocomm.GsmModemUnit.Options4GSM;
import org.radixware.kernel.server.jdbc.IDatabaseConnectionAccess;
import org.radixware.kernel.server.jdbc.ExtendedDbQueries;
import org.radixware.kernel.server.jdbc.RadixResultSet;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.schemas.personalcommunications.Attachment;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageType;
import org.radixware.schemas.personalcommunications.MessageStatistics;

import static org.radixware.kernel.common.utils.Utils.nvlOf;

public final class PersocommDbQueries extends ExtendedDbQueries {

    //
    private static final int RETRY_AFTER_FAIL_DELAY_SEC = SystemPropUtils.getIntSystemProp("rdx.pc.retry.after.fail.delay.seconds", 60);
    //
    private static final int SQL_SELECT_MESSAGES = 1;
    private static final int SQL_SELECT_ATTACHMENTS = 2;
    private static final int SQL_MARK_MESSAGE_IN_PROCESS = 3;
    private static final int SQL_STORE_TO_SENT = 4;
    private static final int SQL_CLEAR_ALL_FLAGS = 5;
    private static final int SQL_PROCESS_EXPIRED_MESSAGES = 6;
    private static final int SQL_STORE_RECV = 7;
    private static final int SQL_STORE_RECV_ATT = 8;
    private static final int SQL_CLEAR_MESSAGE_FLAGS = 9;
//    private static final int SQL_GET_SUITABLE_CHANNELS = 10;
    private static final int SQL_ASSIGN_NEW_CHANNEL = 11;
    private static final int SQL_MARK_AS_FAILED = 12;
    private static final int SQL_READ_OPTIONS = 20;
    private static final int SQL_READ_SEQUENCE = 21;
    private static final int SQL_READ_GSM_OPTIONS = 22;
    private static final int SQL_STAT_FAKE = 30;
    private static final int SQL_STAT_SMPP = 31;
    private static final int SQL_STAT_OUTGOING_QUEUE_SIZE = 32;
    private static final int SQL_DELIVERY_STATUS_SMPP = 40;
    private static final int SQL_LOCK_MESSAGE_BY_SMPP_MESSAGE_ID = 41;
    // Reliable send callback
    private static final int SQL_SELECT_MESSAGES_FOR_SEND_CALLBACK_READY = 50;
    // Reliable deliver callback
    private static final int SQL_READ_SENT_MESSAGE = 60;
    private static final int SQL_LOCK_SENT_MESSAGE = 61;
    private static final int SQL_SET_MESSAGE_DELIVERY_STATUS = 62;
    private static final int SQL_SELECT_MESSAGES_WITH_EXPIRED_DELIVERY = 63;
    private static final int SQL_SET_MESSAGE_DELIVERY_EXPIRED = 64;
    private static final int SQL_SELECT_MESSAGES_READY_FOR_DELIVERY_CALLBACK = 65;
    private static final int SQL_LOCK_MESSAGE_JUST_BEFORE_DELIVERY_CALLBACK = 66;
    private static final int SQL_MARK_MESSAGE_JUST_BEFORE_DELIVER_CALLBACK = 67;
    
    private static final Stmt[] SQL_LIST = {
        new Stmt(SQL_SELECT_MESSAGES, "select id,subject,body,importance,createTime,address,smppEncoding,addressFrom, "
        + "baseForwardTimeMillis, lastForwardTimeMillis, isUssd, ussdServiceOp, callbackClassName, callbackMethodName, "
        + "deliveryCallbackClassName, deliveryCallbackMethodName "
        + "from RDX_PC_outmessage where INPROCESS=0 and (SYSDATE>=dueTime) "
        + "and (expireTime is NULL or expireTime>SYSDATE) "
        + "and channelid = ? and failedIsUnrecoverable = 0 order by dueTime", Types.BIGINT),
        new Stmt(SQL_SELECT_ATTACHMENTS, "select seq, title, mimeType, data from RDX_PC_attachment where messId = ? order by seq", Types.BIGINT),
        new Stmt(SQL_MARK_MESSAGE_IN_PROCESS, "update RDX_PC_OUTMESSAGE set INPROCESS=1 where id = ?", Types.BIGINT),
        new Stmt(SQL_STORE_TO_SENT, "begin " // params: initialDeliveryStatus, smppMessageId, <sendCallbackRequired>, <deliveryCallbackRequired>, id, id
        + "insert into RDX_PC_sentmessage (id, subject, body, maskedBody, importance, createTime, dueTime, expireTime, channelKind, address, sourceEntityGuid, sourcePid, sourceMsgId, destEntityGuid, destPid, callbackClassName, callbackMethodName, channelId,          sentTime, histMode, smppEncoding, storeAttachInHist,   deliveryStatus,   smppMessageId, isUssd, ussdServiceOp,               sendError, stageNo, prevStageMessageId,   sendCallbackRequired, sendCallbackData,   deliveryCallbackRequired, deliveryCallbackClassName, deliveryCallbackMethodName, deliveryCallbackData, addressFrom, initialDueTime,                                                                 deliveryExpTimeMillis) "
        + "select                          id, subject, body, maskedBody, importance, createTime, dueTime, expireTime, channelKind, address, sourceEntityGuid, sourcePid, sourceMsgId, destEntityGuid, destPid, callbackClassName, callbackMethodName, channelId,  SYSDATE sentTime, histMode, smppEncoding, storeAttachInHist, ? deliveryStatus, ? smppMessageId, isUssd, ussdServiceOp, failedMessage sendError, stageNo, prevStageMessageId, ? sendCallbackRequired, sendCallbackData, ? deliveryCallbackRequired, deliveryCallbackClassName, deliveryCallbackMethodName, deliveryCallbackData, addressFrom, initialDueTime, nvl(deliveryExpTimeMillis, (RDX_Utils.getUnixEpochMillis() + deliveryTimeout * 1000)) from RDX_PC_outmessage where id = ? and histMode != 0 and inProcess = 1; "
        + "delete RDX_PC_OUTMESSAGE where id = ? and inProcess=1; end;", Types.VARCHAR, Types.VARCHAR, Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.BIGINT),
        new Stmt(SQL_CLEAR_ALL_FLAGS, "update RDX_PC_OUTMESSAGE set INPROCESS=0 where CHANNELID = ? and INPROCESS = 1", Types.BIGINT),
        new Stmt(SQL_CLEAR_MESSAGE_FLAGS, "update RDX_PC_OUTMESSAGE set INPROCESS=0, sentTime=sysdate where ID = ?", Types.BIGINT),
        new Stmt(SQL_PROCESS_EXPIRED_MESSAGES, "declare "
                + "curTime date;"
                + "begin "
                + "select sysdate - 10 * (1/24/60/60) into curTime from dual; "
                + "insert into RDX_PC_sentmessage (id, subject, body, maskedBody, importance, createTime, dueTime, expireTime, channelKind, address, sourceEntityGuid, sourcePid, sourceMsgId, destEntityGuid, destPid, callbackClassName, callbackMethodName, channelId, histMode, smppEncoding, storeAttachInHist, isUssd, ussdServiceOp,           sendError, stageNo, prevStageMessageId, sendCallbackData, deliveryCallbackClassName, deliveryCallbackMethodName, deliveryCallbackData, addressFrom, initialDueTime) "
                + "select                          id, subject, body, maskedBody, importance, createTime, dueTime, expireTime, channelKind, address, sourceEntityGuid, sourcePid, sourceMsgId, destEntityGuid, destPid, callbackClassName, callbackMethodName, channelId, histMode, smppEncoding, storeAttachInHist, isUssd, ussdServiceOp, 'EXPIRED' sendError, stageNo, prevStageMessageId, sendCallbackData, deliveryCallbackClassName, deliveryCallbackMethodName, deliveryCallbackData, addressFrom, initialDueTime  "
                + "from RDX_PC_OUTMESSAGE "
                + "where CHANNELID=? and (expireTime<=curTime) and histmode!=0; "
                + "delete RDX_PC_OUTMESSAGE where CHANNELID=? and expireTime<=curTime;"
                + "end;", Types.BIGINT, Types.BIGINT),
        new Stmt(SQL_STORE_RECV, "insert into RDX_PC_recvmessage (id,importance,subject,body,sendtime,recvtime,channelid,address) values(?,?,?,?,?,SYSDATE,?,?)", Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.CLOB, Types.DATE, Types.BIGINT, Types.VARCHAR),
        new Stmt(SQL_STORE_RECV_ATT, "insert into RDX_PC_attachment (messId,seq,title,mimeType,data) values (?, ?, ?, ?, ?)", Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.CLOB),
        //      new Stmt(SQL_GET_SUITABLE_CHANNELS, "select RDX_PC_Utils.findSuitableChannelId(?, ?, ?, ?, ?) as id from dual"),
        new Stmt(SQL_ASSIGN_NEW_CHANNEL, "update RDX_PC_outmessage set channelId = ?, inProcess = 0, sentTime=null, lastForwardTimeMillis = RDX_Utils.getUnixEpochMillis() where id = ?", Types.BIGINT, Types.BIGINT),
        new Stmt(SQL_READ_OPTIONS, "select sendPeriod, recvPeriod, sendTimeout, sendAddress, recvAddress, "
        + "smppSystemId, smppSystemType, smppSessionType, smppPassword, smppSourceAddress, "
        + "smppSourceAddressTon,smppSourceAddressNpi,smppDestinationTon,smppDestinationNpi, twitterConsumerToken, twitterConsumerSecret, twitterAccessToken, twitterAccessSecret, "
        + "emailLogin, pop3Address, emailPassword, emailSecureConnection, addressTemplate, subjectTemplate, encoding, fileFormat, smppEncoding, kind, routingPriority, minImportance, maxImportance,"
        + "apnsKeyAlias, gcmApiKey, messAddressRegexp, apnsMaxParallelSendCount, apnsSuccessfulAfterMillis, deliveryTrackingPolicy, deliveryTrackingPeriod, deliveryTrackingRetryPeriod, forwardDelaySec, "
        + "wnsClientId, wnsClientSecret, deliveryAckSapId "
        + "from RDX_PC_channelunit where id = ?", Types.BIGINT),
        new Stmt(SQL_READ_SEQUENCE, "select SQN_RDX_PC_MESSAGEID.NextVal id from DUAL"),
        new Stmt(SQL_READ_GSM_OPTIONS, "select COMPORT, SPEED from RDX_PC_ChannelGSMModem where CHANNELUNITID = ?", Types.BIGINT),
        new Stmt(SQL_STAT_FAKE, "update RDX_PC_SentMessage set channelKind = ? where id = ?", Types.BIGINT, Types.BIGINT),
        new Stmt(SQL_STAT_SMPP, "update RDX_PC_SentMessage set smppBytesSent = ?, smppCharsSent = ?, smppPartsSent = ? where id = ?", Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.BIGINT),
        new Stmt(SQL_STAT_OUTGOING_QUEUE_SIZE, "select count(*) from RDX_PC_OutMessage where channelId = ?", Types.BIGINT),
        new Stmt(SQL_MARK_AS_FAILED, // params: failedMessage, failedIsUnrecoverable, <inProcess = failedIsUnrecoverable>, baseForwardTimeMillis, id
                "update RDX_PC_OutMessage set failedMessage = ?, failedTryCount = failedTryCount + 1, failedLastSendDate = SYSDATE, failedIsUnrecoverable = ?, inprocess = ?, "
                + "dueTime = SYSDATE + " + RETRY_AFTER_FAIL_DELAY_SEC + " * (1/24/60/60), baseForwardTimeMillis = ? where id = ?", Types.VARCHAR, Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.BIGINT),
        new Stmt(SQL_DELIVERY_STATUS_SMPP, "update RDX_PC_SentMessage set deliveryStatus = ?, lastDeliveryStatusChangeDate = sysdate "
                + "where channelId = ? and smppMessageId = ? and deliveryStatus = 'Tracking'", Types.VARCHAR, Types.BIGINT, Types.VARCHAR),
        new Stmt(SQL_LOCK_MESSAGE_BY_SMPP_MESSAGE_ID, "select "
                + " id, sendCallbackRequired, deliveryCallbackRequired, deliveryCallbackClassName, deliveryCallbackMethodName, deliveryExpTimeMillis, deliveryStatus"
                + " from RDX_PC_SentMessage where channelId = ? and smppMessageId = ? and deliveryStatus = 'Tracking' for update nowait",
                Types.VARCHAR, Types.BIGINT),
        // Reliable send callback
        new Stmt(SQL_SELECT_MESSAGES_FOR_SEND_CALLBACK_READY, "select id, sendError"
                + " from RDX_PC_SentMessage where sendCallbackRequired = 1"
                + " and channelId = ? order by id", Types.BIGINT),
        // Reliable delivery callback
        new Stmt(SQL_READ_SENT_MESSAGE, "select "
            + " id, sendCallbackRequired, deliveryCallbackRequired, deliveryCallbackClassName, deliveryCallbackMethodName, deliveryExpTimeMillis, deliveryStatus"
            + " from RDX_PC_SentMessage where id = ?",
            Types.BIGINT),
        new Stmt(SQL_LOCK_SENT_MESSAGE, "select "
            + " id, sendCallbackRequired, deliveryCallbackRequired, deliveryCallbackClassName, deliveryCallbackMethodName, deliveryExpTimeMillis, deliveryStatus"
            + " from RDX_PC_SentMessage where id = ? for update nowait",
            Types.BIGINT),
        new Stmt(SQL_SET_MESSAGE_DELIVERY_STATUS, // deliveryStatus, id
                "update RDX_PC_SentMessage set deliveryStatus = ?, lastDeliveryStatusChangeDate = sysdate, "
                + "deliveryCallbackRequired = nvl2(deliveryCallbackRequired, 2, null) "
                + "where id = ?", Types.VARCHAR, Types.BIGINT),
        new Stmt(SQL_SELECT_MESSAGES_WITH_EXPIRED_DELIVERY, "select " // params: fromTimeMillis, toTimeMillis - NO PARAMS
            + " id, sendCallbackRequired, deliveryCallbackRequired, deliveryCallbackClassName, deliveryCallbackMethodName, deliveryExpTimeMillis, deliveryStatus"
            + " from RDX_PC_SentMessage where deliveryCallbackRequired = 1 and deliveryExpTimeMillis < RDX_Utils.getUnixEpochMillis() "
            + " order by deliveryExpTimeMillis"),
        new Stmt(SQL_SET_MESSAGE_DELIVERY_EXPIRED, "update RDX_PC_SentMessage set" // params: messageId
                + " deliveryCallbackRequired = 2, deliveryStatus = 'Unknown'"
                + " where id = ? and deliveryCallbackRequired = 1 and deliveryStatus = 'Tracking'",
                Types.BIGINT),
        new Stmt(SQL_SELECT_MESSAGES_READY_FOR_DELIVERY_CALLBACK, "select "
                + " id, sendCallbackRequired, deliveryCallbackRequired, deliveryCallbackClassName, deliveryCallbackMethodName, deliveryExpTimeMillis, deliveryStatus"
                + " from RDX_PC_SentMessage"
                + " where deliveryCallbackRequired = 2 and lastDeliveryCallbackTime <= Sysdate - (10.0 / (24 * 3600)) "
                + " order by deliveryExpTimeMillis"
            ),
        new Stmt(SQL_LOCK_MESSAGE_JUST_BEFORE_DELIVERY_CALLBACK, "select "
                + " id, sendCallbackRequired, deliveryCallbackRequired, deliveryCallbackClassName, deliveryCallbackMethodName, deliveryExpTimeMillis, deliveryStatus"
                + " from RDX_PC_SentMessage where id = ? and deliveryCallbackRequired = 2 and (lastDeliveryCallbackTime is NULL or lastDeliveryCallbackTime <= Sysdate - (10.0 / (24 * 3600))) "
                + " for update nowait",
                Types.BIGINT),
        new Stmt(SQL_MARK_MESSAGE_JUST_BEFORE_DELIVER_CALLBACK, "update RDX_PC_SentMessage set lastDeliveryCallbackTime = Sysdate where id = ?", Types.BIGINT),
    };

    static {
        Arrays.sort(SQL_LIST);
    }

    private final PersoCommUnit unit;

    public PersocommDbQueries(final Connection conn) {
        super(conn, SQL_LIST);
        this.unit = null;
    }

    PersocommDbQueries(final IDatabaseConnectionAccess dbca, final PersoCommUnit unit) {
        super(dbca, SQL_LIST);
        if (unit == null) {
            throw new IllegalArgumentException("PCUnit reference can't be null!");
        } else {
            this.unit = unit;
        }
    }

    public void markMessageAsInProcess(long messageId) throws SQLException {
        execute(SQL_MARK_MESSAGE_IN_PROCESS, messageId);
    }
    
    public PersoCommUnit.Options getUnitOptions() throws SQLException {
        return load(PersoCommUnit.Options.class, new PersoCommUnit.Options(), SQL_READ_OPTIONS, unit.getPrimaryUnitId());
    }
    
    public Options4GSM[] getGsmModemOptions(final long channelId) throws SQLException {
        final List<Options4GSM> result;

        try {
            try (final IDbCursor<Options4GSM> query = query(Options4GSM.class, new Options4GSM(), SQL_READ_GSM_OPTIONS, channelId)) {
                result = query.collection(Options4GSM.class);
            }
            return result.toArray(new Options4GSM[result.size()]);
        } catch (Exception exc) {
            throw new SQLException(exc);
        }
    }

    public void clearOutMessageFlag(final Long messageId) throws SQLException {
        execute(SQL_CLEAR_MESSAGE_FLAGS, messageId);
    }

    public void clearOutMessageFlagAll(final Long channelId) throws SQLException {
        execute(SQL_CLEAR_ALL_FLAGS, channelId);
    }

    public IDbCursor<MessageWithMeta> getMessages2Send(final Long channelId) throws SQLException {
        return query(MessageWithMeta.class, SQL_SELECT_MESSAGES, channelId);
    }
    
    public IDbCursor<SendCallbackData> getMessages2SendCallback(final Long channelId) throws SQLException {
        return query(SendCallbackData.class, SQL_SELECT_MESSAGES_FOR_SEND_CALLBACK_READY, channelId);
    }
    
    
    // Reliable Delivery Callback (begin)
    public SentMessageData readSentMessage(final long messageId) throws Exception {
        return record(SentMessageData.class, SQL_READ_SENT_MESSAGE, messageId);
    }
    
    public SentMessageData lockSentMessage(final long messageId) throws Exception {
        return record(SentMessageData.class, SQL_LOCK_SENT_MESSAGE, messageId);
    }
    
    public void setMessageDeliveryStatus(long messageId, EPersoCommDeliveryStatus status) throws SQLException {
        execute(SQL_SET_MESSAGE_DELIVERY_STATUS, status, messageId);
    }
    
    public IDbCursor<SentMessageData> getMessagesWithExpiredDelivery() throws SQLException {
        return query(SentMessageData.class, SQL_SELECT_MESSAGES_WITH_EXPIRED_DELIVERY);
    }
    
    public boolean setMessageDeliveryExpired(long messageId) throws SQLException {
        return execute(SQL_SET_MESSAGE_DELIVERY_EXPIRED, messageId) == 1;
    }
    
    public IDbCursor<SentMessageData> getMessages2DeliveryCallback() throws SQLException {
        return query(SentMessageData.class, SQL_SELECT_MESSAGES_READY_FOR_DELIVERY_CALLBACK);
    }
    
    public SentMessageData lockMessageJustBeforeDeliveryCallback(long messageId) throws Exception {
        return record(SentMessageData.class, SQL_LOCK_MESSAGE_JUST_BEFORE_DELIVERY_CALLBACK, messageId);
    }
    
    public void markMessageJustBeforeDeliveryCallback(long messageId) throws SQLException {
        execute(SQL_MARK_MESSAGE_JUST_BEFORE_DELIVER_CALLBACK, messageId);
    }
    // Reliable Delivery Callback (end)
    
    public void save2Sent(Long messageId, EPersoCommDeliveryStatus initialDeliveryStatus, String smppMessageId, Integer sendCallbackRequired, Integer deliveryCallbackRequired) throws SQLException {
        execute(SQL_STORE_TO_SENT, toSqlParam(initialDeliveryStatus), toSqlParam(smppMessageId), toSqlParam(sendCallbackRequired), toSqlParam(deliveryCallbackRequired), messageId, messageId);
    }
    
    public void processExpiredMessages() throws SQLException {
        execute(SQL_PROCESS_EXPIRED_MESSAGES, unit.getPrimaryUnitId(), unit.getPrimaryUnitId());
    }

    public void saveStatistics(final Long messageId, final MessageStatistics ticket) throws SQLException {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket can't be null!");
        } else {
            if (ticket.isSetSMPP()) {
                execute(SQL_STAT_SMPP, ticket.getSMPP().getStatSmppSizeInBytes(), ticket.getSMPP().getStatSmppSizeInChars(), ticket.getSMPP().getStatSmppPartCount(), messageId
                );
            } else if (ticket.isSetFake()) {
                execute(SQL_STAT_FAKE, ticket.getFake().getChannelKind(), messageId);
            } else {
                throw new UnsupportedOperationException("Save statistics for ticket type [" + ticket + "] is not implemented yet!");
            }
        }
    }

    @Override
    protected <T> T createRecord(final ResultSet _rs, final Class<T> awaited, final Map<String, Field> fiedls, final int stmtId, final T template) throws Exception {
        final RadixResultSet rs = (RadixResultSet) _rs;
        if (awaited == MessageWithMeta.class) {
            final MessageDocument md = MessageDocument.Factory.newInstance();
            final MessageType mt = md.addNewMessage();
            final long msgId = rs.getLong("id");
            final Long baseForwardTimeMillis = rs.getNullableLong("baseForwardTimeMillis");
            final Long lastForwardTimeMillis = rs.getNullableLong("lastForwardTimeMillis");
            final String sendCallbackClassName = rs.getString("callbackClassName");
            final String sendCallbackMethodName = rs.getString("callbackMethodName");
            final String deliveryCallbackClassName = rs.getString("deliveryCallbackClassName");
            final String deliveryCallbackMethodName = rs.getString("deliveryCallbackMethodName");
            
            mt.setBody(nvlOf(rs.getString("body"), ""));
            mt.setSubject(nvlOf(rs.getString("subject"), ""));
            mt.setAddressTo(rs.getString("address"));
            mt.setImportance(rs.getLong("importance"));
            mt.setAddressFrom(nvlOf(rs.getString("addressFrom"), unit.options.recvAddress));
            mt.setSMPPEncoding(rs.getString("smppEncoding"));
            mt.setIsUssd(rs.getNullableBool("isUssd"));
            mt.setUssdServiceOp(rs.getNullableLong("ussdServiceOp"));
            
            try (final IDbCursor<Attachment> attList = query(Attachment.class, Attachment.Factory.newInstance(), SQL_SELECT_ATTACHMENTS, msgId)) {
                if (!attList.isEmpty()) {
                    mt.addNewAttachments().assignAttachmentList(attList.collection(Attachment.class));
                }
            }
            final MessageWithMeta result = new MessageWithMeta(msgId, md, baseForwardTimeMillis, lastForwardTimeMillis,
                    sendCallbackClassName, sendCallbackMethodName, deliveryCallbackClassName, deliveryCallbackMethodName);
            return awaited.cast(result);
        } else if (awaited == Attachment.class) {
            final Attachment att = Attachment.Factory.newInstance();

            att.setSeq(rs.getLong("seq"));
            att.setTitle(rs.getString("title"));
            att.setMimeType(rs.getString("mimeType"));
            att.setData(rs.getBytes("data"));

            return awaited.cast(att);
        } else if (awaited == Long.class) {
            return awaited.cast(rs.getLong(1));
        } else if (awaited == SendCallbackData.class) {
            final long id = rs.getLong(1);
            final String failedMessage = rs.getString(2);
            final SendCallbackData result = new SendCallbackData(id, failedMessage);
            return awaited.cast(result);
        } else if (awaited == SentMessageData.class) {
            final long id = rs.getLong("id");
            final Long sendCallbackRequired = rs.getNullableLong("sendCallbackRequired");
            final Long deliveryCallbackRequired = rs.getNullableLong("deliveryCallbackRequired");
            final String deliveryCallbackClassName = rs.getString("deliveryCallbackClassName");
            final String deliveryCallbackMethodName = rs.getString("deliveryCallbackMethodName");
            final Long deliveryExpTimeMillis = rs.getNullableLong("deliveryExpTimeMillis");
            final String deliveryStatusStr = rs.getString("deliveryStatus");
            final EPersoCommDeliveryStatus deliveryStatus = deliveryStatusStr == null? null : EPersoCommDeliveryStatus.getForValue(deliveryStatusStr);
            
            final SentMessageData result = new SentMessageData(
                    id, sendCallbackRequired, deliveryCallbackRequired, deliveryCallbackClassName, deliveryCallbackMethodName, deliveryStatus, deliveryExpTimeMillis);
            return awaited.cast(result);
        }
        else {
            return super.createRecord(rs, awaited, fiedls, stmtId, template);
        }
    }

    /**
     * <p>
     * Save received message</p>
     *
     * @param m message to save
     * @return message id was created
     * @throws SQLException
     */
    public Long saveReceived(MessageDocument m) throws SQLException {
        final class Sequence {

            public long id;
        }
        final Sequence seq = load(Sequence.class, new Sequence(), SQL_READ_SEQUENCE);
        final MessageType msg = m.getMessage();

        getConnectionAccess().getConnection().setSavepoint();
        execute(SQL_STORE_RECV, seq.id, msg.getImportance() != null ? msg.getImportance() : 1, msg.getSubject() == null ? new Null(Types.VARCHAR) : msg.getSubject(), msg.getBody() == null ? new Null(Types.VARCHAR) : msg.getBody(), msg.getSentTime() != null ? new Date(msg.getSentTime().getTime()) : new Null(Types.DATE), unit.getPrimaryUnitId(), msg.getAddressFrom()
        );

        if (msg.isSetAttachments()) {
            for (Attachment att : msg.getAttachments().getAttachmentList()) {
                execute(SQL_STORE_RECV_ATT, seq.id, att.getSeq(), att.getTitle(), att.getMimeType(), att.getData()//)getConnectionAccess().createBlob(att.getData())
                );
            }
        }
        getConnectionAccess().getConnection().commit();

        return seq.id;
    }

    public void assignNewChannel(Long messageId, Long newChannel) throws SQLException {
        execute(SQL_ASSIGN_NEW_CHANNEL, newChannel, messageId);
    }

    public void markMessageAsFailed(Long messageId, boolean isUnrecoverable, Long baseForwardTimeMillis, String errorDescription) throws SQLException {
        execute(SQL_MARK_AS_FAILED, errorDescription, isUnrecoverable, isUnrecoverable, toSqlParam(baseForwardTimeMillis), messageId);
    }

    public void setSmppDeliveryStatus(String smppMessageId, Long channelId, EPersoCommDeliveryStatus deliveryStatus) throws SQLException {
        execute(SQL_DELIVERY_STATUS_SMPP, deliveryStatus.getValue(), channelId, smppMessageId);
    }
    
    public SentMessageData lockMessageBySmppMessageId(long channelId, String smppMessageId) throws Exception {
        return record(SentMessageData.class, SQL_LOCK_MESSAGE_BY_SMPP_MESSAGE_ID, channelId, smppMessageId);
    }
    
    public long getOutgoingQueueSize(final Long channelId) throws SQLException {
        Long res = load(Long.class, SQL_STAT_OUTGOING_QUEUE_SIZE, channelId);
        return res;
    }
}
