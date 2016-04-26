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

import java.sql.*;
import oracle.sql.BLOB;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.ESmppEncoding;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.IDbQueries;
import org.radixware.kernel.server.units.Messages;
import org.radixware.schemas.personalcommunications.Attachment;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageType;
import org.radixware.schemas.personalcommunications.MessageType.Attachments;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;

final public class DbQueries implements IDbQueries {

    PCUnit unit;

    DbQueries(final PCUnit unit) {
        this.unit = unit;
    }
    private PreparedStatement qryDPCOptions = null;
    private PreparedStatement qryReadMessages = null;
    private PreparedStatement qryProcessExpiredMessages = null;
    private PreparedStatement qryReadAttachments = null;
    private PreparedStatement qrySaveOutMessage = null;
    private PreparedStatement qryUpdateInProcessOutMessage = null;
    private PreparedStatement qryClearOutMessageFlagAll = null;
    private PreparedStatement qryClearOutMessageFlag = null;
    private PreparedStatement qrySaveRecvMessage = null;
    private PreparedStatement qrySaveRecvAttach = null;
    private PreparedStatement qrySaveRecvGetId = null;
    private PreparedStatement qryGSMModem = null;
    private PreparedStatement qryFindSuitableChannelId = null;
    private PreparedStatement qryForwardMessage = null;

    public PCUnit.Options readOptions() throws SQLException {
        if (qryDPCOptions == null) {
            final Connection dbConnection = unit.getDbConnection();
            if (dbConnection == null) {
                return null;
            }
            qryDPCOptions = dbConnection.prepareStatement(
                    "select sendPeriod, recvPeriod, sendTimeout, sendAddress, recvAddress, "
                    + "smppSystemId, smppSystemType, smppPassword, smppSourceAddress, "
                    + "smppSourceAddressTon,smppSourceAddressNpi,smppDestinationTon,smppDestinationNpi, twitterConsumerToken, twitterConsumerSecret, twitterAccessToken, twitterAccessSecret, "
                    + "emailLogin, pop3Address, emailPassword, addressTemplate, subjectTemplate, encoding, fileFormat, smppEncoding, kind, routingPriority, minImportance, maxImportance,"
                    + "apnsKeyAlias, gcmApiKey, messAddressRegexp, apnsMaxParallelSendCount, apnsSuccessfulAfterMillis "
                    + "from RDX_PC_channelunit where id = ?");
        }
        qryDPCOptions.setLong(1, unit.getId());
        final ResultSet rs = qryDPCOptions.executeQuery();
        try {
            if (rs.next()) {
                final PCUnit.Options options = new PCUnit.Options();
                options.sendPeriod = rs.getLong("sendPeriod");
                if (rs.wasNull()) {
                    options.sendPeriod = null;
                }
                options.recvPeriod = rs.getLong("recvPeriod");
                if (rs.wasNull()) {
                    options.recvPeriod = null;
                }
                options.sendTimeout = rs.getLong("sendTimeout");
                if (rs.wasNull()) {
                    options.sendTimeout = null;
                }
                options.sendAddress = rs.getString("sendAddress");
                options.recvAddress = rs.getString("recvAddress");
                options.smppSystemId = rs.getString("smppSystemId");
                options.smppSystemType = rs.getString("smppSystemType");
                options.smppPassword = rs.getString("smppPassword");
                options.smppSourceAddress = rs.getString("smppSourceAddress");
                options.smppSourceAddressTon = rs.getLong("smppSourceAddressTon");
                options.smppSourceAddressNpi = rs.getLong("smppSourceAddressNpi");
                options.smppDestinationTon = rs.getLong("smppDestinationTon");
                options.smppDestinationNpi = rs.getLong("smppDestinationNpi");
                final String enc = rs.getString("smppEncoding");
                if (enc != null) {
                    options.smppEncoding = ESmppEncoding.getForValue(enc);
                } else {
                    options.smppEncoding = null;
                }
                options.pop3Address = rs.getString("pop3Address");
                options.emailLogin = rs.getString("emailLogin");
                options.emailPassword = rs.getString("emailPassword");
                options.addressTemplate = rs.getString("addressTemplate");
                options.subjectTemplate = rs.getString("subjectTemplate");
                options.encoding = rs.getString("encoding");
                options.fileFormat = rs.getString("fileFormat");
                options.kind = rs.getString("kind");
                options.routingPriority = rs.getLong("routingPriority");
                options.minImportance = rs.getLong("minImportance");
                options.maxImportance = rs.getLong("maxImportance");
                options.twitterConsumerToken = rs.getString("twitterConsumerToken");
                options.twitterConsumerSecret = rs.getString("twitterConsumerSecret");
                options.twitterAccessToken = rs.getString("twitterAccessToken");
                options.twitterAccessSecret = rs.getString("twitterAccessSecret");

                options.apnsKeyAlias = rs.getString("apnsKeyAlias");
                options.gcmApiKey = rs.getString("gcmApiKey");
                options.addressMask = rs.getString("messAddressRegexp");
                options.apnsMaxParallelSendCount = rs.getInt("apnsMaxParallelSendCount");
                options.apnsSendSuccessfulAfterMillis = rs.getInt("apnsSuccessfulAfterMillis");

                return options;
            } else {
                throw new IllegalUsageError("Unknown DPC_ChannelUnit #" + String.valueOf(unit.getId()));
            }
        } finally {
            rs.close();
        }
    }

    public Long forwardMessage(final MessageDocument m, final Long id) throws SQLException {
        if (m == null) {
            return null;
        }
        final Connection dbConnection = unit.getDbConnection();
        if (dbConnection == null) {
            return null;
        }

        if (qryFindSuitableChannelId == null) {
            qryFindSuitableChannelId = dbConnection.prepareStatement(
                    "select RDX_PC_Utils.findSuitableChannelId(?, ?, ?, ?, ?) as id from dual");
        }
        qryFindSuitableChannelId.setString(1, unit.options.kind);
        qryFindSuitableChannelId.setString(2, m.getMessage().getAddressTo());
        qryFindSuitableChannelId.setLong(3, m.getMessage().getImportance());
        qryFindSuitableChannelId.setLong(4, unit.getId());
        qryFindSuitableChannelId.setLong(5, unit.options.routingPriority);
        final ResultSet rs = qryFindSuitableChannelId.executeQuery();
        try {
            if (rs.next()) {
                final Long newUnitId = rs.getLong("id");
                if (newUnitId == 0) {
                    return null;
                }
                if (qryForwardMessage == null) {
                    qryForwardMessage = dbConnection.prepareStatement(
                            "begin "
                            + " update RDX_PC_outmessage set channelId = ?, inProcess = 0, sentTime=null where id = ?; "
                            + "end;");
                }
                qryForwardMessage.setLong(1, newUnitId);
                qryForwardMessage.setLong(2, id);
                qryForwardMessage.execute();
                dbConnection.commit();
                return newUnitId;
            }
        } finally {
            rs.close();
        }
        return null;
    }

    public void processExpiredMessages() throws SQLException {
        final Connection dbConnection = unit.getDbConnection();
        if (dbConnection == null) {
            return;
        }
        if (qryProcessExpiredMessages == null) {
            qryProcessExpiredMessages = dbConnection.prepareStatement(
                    "declare "
                    + "curTime date;"
                    + "begin "
                    + "select sysdate - 10 * (1/24/60/60) into curTime from dual; "
                    + "insert into RDX_PC_sentmessage (id,subject,body,importance,createtime,duetime,"
                    + "expiretime,histmode,channelkind,address,sourceentityguid,"
                    + "sourcepid,sourcemsgid,destentityguid,destpid,callbackclassname,"
                    + "callbackmethodname,channelid,senderror,smppEncoding) "
                    + "select id,subject,body,importance,createtime,duetime,"
                    + "expiretime,histmode,channelkind,address,sourceentityguid,"
                    + "sourcepid,sourcemsgid,destentityguid,destpid,callbackclassname,"
                    + "callbackmethodname,channelid,nvl(failedMessage,'EXPIRED') senderror,smppEncoding from RDX_PC_OUTMESSAGE "
                    + "where CHANNELID=? and (expireTime<=curTime) and histmode!=0; "
                    + "delete RDX_PC_OUTMESSAGE where CHANNELID=? and expireTime<=curTime;"
                    + "end;");
        }
        qryProcessExpiredMessages.setLong(1, unit.getId());
        qryProcessExpiredMessages.setLong(2, unit.getId());
        qryProcessExpiredMessages.execute();
        dbConnection.commit();
    }

    /**
     * @return true if all messages from send queue were sent, false otherwise.
     */
    public boolean readAndSendMessages() throws SQLException {
        final Connection dbConnection = unit.getDbConnection();
        if (dbConnection == null) {
            return true;
        }
        if (qryReadMessages == null) {
            qryReadMessages = dbConnection.prepareStatement(
                    "select id,subject,body,importance,createTime,address,smppEncoding,addressFrom "
                    + "from RDX_PC_outmessage where INPROCESS=0 and (SYSDATE>=dueTime) and (expireTime is NULL or expireTime>SYSDATE) and (senttime is null or sysdate - cast(senttime as date) > 10 * (1/24/60/60)) and channelid = ?");
        }

        if (qryReadAttachments == null) {
            qryReadAttachments = dbConnection.prepareStatement(
                    "select seq, title, mimeType, data from RDX_PC_attachment where messId = ? order by seq");
        }

        qryReadMessages.setLong(1, unit.getId());
        final ResultSet rs = qryReadMessages.executeQuery();
        try {
            final long startMillis = System.currentTimeMillis();
            while (System.currentTimeMillis() < startMillis + PCUnit.MAX_SYNC_DURATION_MILLIS && rs.next() && unit.canSend()) {
                final Long id = rs.getLong("id");
                final MessageDocument m = MessageDocument.Factory.newInstance();
                final MessageType mt = m.addNewMessage();
                String bd = rs.getString("body");
                if (bd == null) {
                    bd = "";
                }
                mt.setBody(bd);
                String sb = rs.getString("subject");
                if (sb == null) {
                    sb = "";
                }
                mt.setSubject(sb);
                mt.setAddressTo(rs.getString("address"));
                mt.setImportance(rs.getLong("importance"));
                mt.setAddressFrom(rs.getString("addressFrom"));
                if (rs.wasNull()) {
                    mt.setAddressFrom(unit.options.recvAddress);
                }
                mt.setSMPPEncoding(rs.getString("smppEncoding"));
                qryReadAttachments.setLong(1, id);
                try (ResultSet attachRs = qryReadAttachments.executeQuery()) {
                    if (attachRs.next()) {
                        final Attachments atts = mt.addNewAttachments();
                        do {
                            final Attachment att = atts.addNewAttachment();
                            att.setSeq(attachRs.getLong("seq"));
                            att.setTitle(attachRs.getString("title"));
                            att.setMimeType(attachRs.getString("mimeType"));
                            att.setData(attachRs.getBytes("data"));
                        } while (attachRs.next());
                    }
                }
                if (qryUpdateInProcessOutMessage == null) {
                    qryUpdateInProcessOutMessage = dbConnection.prepareStatement(
                            "update RDX_PC_OUTMESSAGE set INPROCESS=1 where id = ?");
                }
                qryUpdateInProcessOutMessage.setLong(1, id);
                qryUpdateInProcessOutMessage.execute();
                dbConnection.commit();
                unit.sendMessage(m, id);
            }
            return !rs.next();
        } finally {
            rs.close();
        }
    }

    int readGSMModem(final Service srv) throws SQLException {
        if (qryGSMModem == null) {
            final Connection dbConnection = unit.getDbConnection();
            if (dbConnection == null) {
                return 0;
            }
            qryGSMModem = dbConnection.prepareStatement(
                    "select COMPORT, SPEED "
                    + "from RDX_PC_ChannelGSMModem where CHANNELUNITID = ?");
        }
        qryGSMModem.setLong(1, unit.getId());
        final ResultSet rs = qryGSMModem.executeQuery();
        int count = 0;
        try {
            if (rs.next()) {
                final Long n = rs.getLong("COMPORT");
                final Long s = rs.getLong("SPEED");
                final SerialModemGateway gateway = new SerialModemGateway("modem.com" + String.valueOf(n), "COM" + String.valueOf(n), s.intValue(), "", "");
                gateway.setInbound(true);
                gateway.setOutbound(true);
                srv.addGateway(gateway);
                count++;
            }
        } finally {
            rs.close();
        }
        return count;
    }

    void saveOutMessage(Long id, String errorMess) throws SQLException {
        final Connection dbConnection = unit.getDbConnection();
        if (qrySaveOutMessage == null) {
            qrySaveOutMessage = dbConnection.prepareStatement(
                    "begin "
                    + "insert into RDX_PC_sentmessage (id,subject,body,importance,createTime,dueTime,expireTime,channelKind,address,sourceEntityGuid,sourcePid,sourceMsgId,destEntityGuid,destPid,callbackClassName,callbackMethodName,channelId,sentTime,sendError,histMode,smppEncoding) "
                    + "select id,subject,body,importance,createTime,dueTime,expireTime,channelKind,address,sourceEntityGuid,sourcePid,sourceMsgId,destEntityGuid,destPid,callbackClassName,callbackMethodName,channelId,SYSDATE sentTime,? sendError,histMode,smppEncoding from RDX_PC_outmessage where id = ? and histMode!=0 and inProcess=1; "
                    + "delete RDX_PC_OUTMESSAGE where id = ? and inProcess=1; end;");
        }
        qrySaveOutMessage.setString(1, errorMess);
        qrySaveOutMessage.setLong(2, id);
        qrySaveOutMessage.setLong(3, id);
        qrySaveOutMessage.execute();
        dbConnection.commit();
    }

    @SuppressWarnings("boxing")
    public Long saveMessage(MessageDocument m) throws SQLException {
        final Connection dbConnection = unit.getDbConnection();
        if (qrySaveRecvGetId == null) {
            qrySaveRecvGetId = dbConnection.prepareStatement("select SQN_RDX_PC_MESSAGEID.NextVal id from DUAL");
        }
        if (qrySaveRecvMessage == null) {
            qrySaveRecvMessage = dbConnection.prepareStatement(
                    "insert into RDX_PC_recvmessage (id,importance,subject,body,sendtime,recvtime,channelid,address) "
                    + "select ? id, ? importance, ? subject, ? body, ? sendtime, SYSDATE recvtime, ? channelid, ? address from DUAL");
        }
        if (qrySaveRecvAttach == null) {
            qrySaveRecvAttach = dbConnection.prepareStatement(
                    "insert into RDX_PC_attachment (messId,seq,title,mimeType,data) "
                    + "values (?, ?, ?, ?, ?)");
        }
        Long id = null;
        try {
            dbConnection.setSavepoint();
            final ResultSet rs = qrySaveRecvGetId.executeQuery();
            try {
                if (rs.next()) {
                    id = rs.getLong("id");
                }
            } finally {
                rs.close();
            }

            qrySaveRecvMessage.setLong(1, id);
            if (m.getMessage().getImportance() != null) {
                qrySaveRecvMessage.setLong(2, m.getMessage().getImportance());
            } else {
                qrySaveRecvMessage.setLong(2, 1);
            }
            qrySaveRecvMessage.setString(3, m.getMessage().getSubject());
            qrySaveRecvMessage.setString(4, m.getMessage().getBody());
            if (m.getMessage().getSentTime() != null) {
                final Date d = new Date(m.getMessage().getSentTime().getTime());
                qrySaveRecvMessage.setDate(5, d);
            } else {
                qrySaveRecvMessage.setNull(5, Types.DATE);
            }
            qrySaveRecvMessage.setLong(6, unit.getId());
            qrySaveRecvMessage.setString(7, m.getMessage().getAddressFrom());
            qrySaveRecvMessage.execute();
            if (m.getMessage().isSetAttachments()) {
                for (Attachment att : m.getMessage().getAttachments().getAttachmentList()) {
                    qrySaveRecvAttach.setLong(1, id);
                    qrySaveRecvAttach.setLong(2, att.getSeq());
                    qrySaveRecvAttach.setString(3, att.getTitle());
                    qrySaveRecvAttach.setString(4, att.getMimeType());
                    final BLOB b = oracle.sql.BLOB.getEmptyBLOB();
                    qrySaveRecvAttach.setBlob(5, b);
                    b.setBytes(att.getData());
                    qrySaveRecvAttach.execute();
                }
            }
            dbConnection.commit();
        } catch (Throwable e) {
            dbConnection.rollback();
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
        }
        return id;
    }

    final void clearOutMessageFlagAll(Long channelId) throws SQLException {
        final Connection dbConnection = unit.getDbConnection();
        if (qryClearOutMessageFlagAll == null && dbConnection != null) {
            qryClearOutMessageFlagAll = dbConnection.prepareStatement("update RDX_PC_OUTMESSAGE set INPROCESS=0 where CHANNELID = ?");
        }
        if (qryClearOutMessageFlagAll != null) {
            qryClearOutMessageFlagAll.setLong(1, channelId);
            qryClearOutMessageFlagAll.execute();
        }
        if (dbConnection != null) {
            dbConnection.commit();
        }
    }

    final void clearOutMessageFlag(Long messId) throws SQLException {
        final Connection dbConnection = unit.getDbConnection();
        if (qryClearOutMessageFlag == null && dbConnection != null) {
            qryClearOutMessageFlag = dbConnection.prepareStatement("update RDX_PC_OUTMESSAGE set INPROCESS=0, sentTime=sysdate where ID = ?");
        }
        if (qryClearOutMessageFlag != null) {
            qryClearOutMessageFlag.setLong(1, messId);
            qryClearOutMessageFlag.execute();
            dbConnection.commit();
        }
    }

    @Override
    public void closeAll() {
        if (qryDPCOptions != null) {
            try {
                qryDPCOptions.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }
        if (qryReadMessages != null) {
            try {
                qryReadMessages.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }
        if (qryReadAttachments != null) {
            try {
                qryReadAttachments.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }
        if (qrySaveOutMessage != null) {
            try {
                qrySaveOutMessage.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }
        if (qryUpdateInProcessOutMessage != null) {
            try {
                qryUpdateInProcessOutMessage.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }
        if (qrySaveRecvMessage != null) {
            try {
                qrySaveRecvMessage.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }
        if (qrySaveRecvAttach != null) {
            try {
                qrySaveRecvAttach.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }
        if (qrySaveRecvGetId != null) {
            try {
                qrySaveRecvGetId.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }

        if (qryGSMModem != null) {
            try {
                qryGSMModem.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }

        if (qryProcessExpiredMessages != null) {
            try {
                qryProcessExpiredMessages.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }

        if (qryClearOutMessageFlagAll != null) {
            try {
                qryClearOutMessageFlagAll.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }

        if (qryClearOutMessageFlag != null) {
            try {
                qryClearOutMessageFlag.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }

        if (qryFindSuitableChannelId != null) {
            try {
                qryFindSuitableChannelId.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }

        if (qryForwardMessage != null) {
            try {
                qryForwardMessage.close();
            } catch (SQLException e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                unit.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(unit.getFullTitle(), exStack), unit.getEventSource(), false);
            }
        }

        qryDPCOptions = null;
        qryReadMessages = null;
        qryProcessExpiredMessages = null;
        qryReadAttachments = null;
        qrySaveOutMessage = null;
        qryUpdateInProcessOutMessage = null;
        qryClearOutMessageFlagAll = null;
        qryClearOutMessageFlag = null;
        qrySaveRecvMessage = null;
        qrySaveRecvAttach = null;
        qrySaveRecvGetId = null;
        qryGSMModem = null;

        qryFindSuitableChannelId = null;
        qryForwardMessage = null;
    }
}
