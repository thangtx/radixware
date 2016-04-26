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

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.enums.ESmppEncoding;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.units.persocomm.NewGSMModemUnit.Options4GSM;
import org.radixware.kernel.server.units.persocomm.interfaces.IDatabaseConnectionAccess;
import org.radixware.kernel.server.units.persocomm.interfaces.IHighLevelDbQuery;
import org.radixware.kernel.server.units.persocomm.tools.BasicDbQuery;
import org.radixware.schemas.personalcommunications.Attachment;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageType;
import org.radixware.schemas.personalcommunications.MessageStatistics;

public final class NewDbQueries extends BasicDbQuery implements IHighLevelDbQuery {

    //
    private static final int RETRY_AFTER_FAIL_DELAY_SEC = SystemPropUtils.getIntSystemProp("rdx.pc.retry.after.fail.delay.seconds", 60);
    //
    private static final int SQL_SELECT_MESSAGES = 1;
    private static final int SQL_SELECT_ATTACHMENTS = 2;
    private static final int SQL_MARK_MESSAGES_IN_PROCESS = 3;
    private static final int SQL_STORE_TO_SENT = 4;
    private static final int SQL_CLEAR_ALL_FLAGS = 5;
    private static final int SQL_PROCESS_EXPIRED_MESSAGES = 6;
    private static final int SQL_STORE_RECV = 7;
    private static final int SQL_STORE_RECV_ATT = 8;
    private static final int SQL_CLEAR_MESSAGE_FLAGS = 9;
    private static final int SQL_GET_SUITABLE_CHANNELS = 10;
    private static final int SQL_ASSIGN_NEW_CHANNEL = 11;
    private static final int SQL_MARK_AS_FAILED = 12;
    private static final int SQL_READ_OPTIONS = 20;
    private static final int SQL_READ_SEQUENCE = 21;
    private static final int SQL_READ_GSM_OPTIONS = 22;
    private static final int SQL_STAT_FAKE = 30;
    private static final int SQL_STAT_SMPP = 31;

    private static final Stmt[] SQL_LIST = {
        new Stmt(SQL_SELECT_MESSAGES, "select id,subject,body,importance,createTime,address,smppEncoding,addressFrom "
        + "from RDX_PC_outmessage where INPROCESS=0 and (SYSDATE>=dueTime) "
        + "and (expireTime is NULL or expireTime>SYSDATE) and (senttime is null or sysdate - cast(senttime as date) > 10 * (1/24/60/60)) "
        + "and (failedLastSendDate is null or sysdate - failedLastSendDate > " + RETRY_AFTER_FAIL_DELAY_SEC + " * (1/24/60/60)) and channelid = ? and failedIsUnrecoverable = 0"),
        new Stmt(SQL_SELECT_ATTACHMENTS, "select seq, title, mimeType, data from RDX_PC_attachment where messId = ? order by seq"),
        new Stmt(SQL_MARK_MESSAGES_IN_PROCESS, "update RDX_PC_OUTMESSAGE set INPROCESS=1 where id = ?"),
        new Stmt(SQL_STORE_TO_SENT, "begin "
        + "insert into RDX_PC_sentmessage (id,subject,body,importance,createTime,dueTime,expireTime,channelKind,address,sourceEntityGuid,sourcePid,sourceMsgId,destEntityGuid,destPid,callbackClassName,callbackMethodName,channelId,sentTime,sendError,histMode,smppEncoding) "
        + "select id,subject,body,importance,createTime,dueTime,expireTime,channelKind,address,sourceEntityGuid,sourcePid,sourceMsgId,destEntityGuid,destPid,callbackClassName,callbackMethodName,channelId,SYSDATE sentTime,? sendError,histMode,smppEncoding from RDX_PC_outmessage where id = ? and histMode!=0 and inProcess=1; "
        + "delete RDX_PC_OUTMESSAGE where id = ? and inProcess=1; end;"),
        new Stmt(SQL_CLEAR_ALL_FLAGS, "update RDX_PC_OUTMESSAGE set INPROCESS=0 where CHANNELID = ?"),
        new Stmt(SQL_CLEAR_MESSAGE_FLAGS, "update RDX_PC_OUTMESSAGE set INPROCESS=0, sentTime=sysdate where ID = ?"),
        new Stmt(SQL_PROCESS_EXPIRED_MESSAGES, "declare "
        + "curTime date;"
        + "begin "
        + "select sysdate - 10 * (1/24/60/60) into curTime from dual; "
        + "insert into RDX_PC_sentmessage (id,subject,body,importance,createtime,duetime,"
        + "expiretime,histmode,channelkind,address,sourceentityguid,"
        + "sourcepid,sourcemsgid,destentityguid,destpid,callbackclassname,"
        + "callbackmethodname,channelid,senderror,smppEncoding) "
        + " select id,subject,body,importance,createtime,duetime,"
        + "expiretime,histmode,channelkind,address,sourceentityguid,"
        + "sourcepid,sourcemsgid,destentityguid,destpid,callbackclassname,"
        + "callbackmethodname,channelid,'EXPIRED' senderror,smppEncoding from RDX_PC_OUTMESSAGE "
        + "where CHANNELID=? and (expireTime<=curTime) and histmode!=0; "
        + "delete RDX_PC_OUTMESSAGE where CHANNELID=? and expireTime<=curTime;"
        + "end;"),
        new Stmt(SQL_STORE_RECV, "insert into RDX_PC_recvmessage (id,importance,subject,body,sendtime,recvtime,channelid,address) values(?,?,?,?,?,SYSDATE,?,?)"),
        new Stmt(SQL_STORE_RECV_ATT, "insert into RDX_PC_attachment (messId,seq,title,mimeType,data) values (?, ?, ?, ?, ?)"),
        new Stmt(SQL_GET_SUITABLE_CHANNELS, "select RDX_PC_Utils.findSuitableChannelId(?, ?, ?, ?, ?) as id from dual"),
        new Stmt(SQL_ASSIGN_NEW_CHANNEL, "update RDX_PC_outmessage set channelId = ?, inProcess = 0, sentTime=null where id = ?"),
        new Stmt(SQL_READ_OPTIONS, "select sendPeriod, recvPeriod, sendTimeout, sendAddress, recvAddress, "
        + "smppSystemId, smppSystemType, smppPassword, smppSourceAddress, "
        + "smppSourceAddressTon,smppSourceAddressNpi,smppDestinationTon,smppDestinationNpi, twitterConsumerToken, twitterConsumerSecret, twitterAccessToken, twitterAccessSecret, "
        + "emailLogin, pop3Address, emailPassword, emailSecureConnection, addressTemplate, subjectTemplate, encoding, fileFormat, smppEncoding, kind, routingPriority, minImportance, maxImportance,"
        + "apnsKeyAlias, gcmApiKey, messAddressRegexp, apnsMaxParallelSendCount, apnsSuccessfulAfterMillis "
        + "from RDX_PC_channelunit where id = ?"),
        new Stmt(SQL_READ_SEQUENCE, "select SQN_RDX_PC_MESSAGEID.NextVal id from DUAL"),
        new Stmt(SQL_READ_GSM_OPTIONS, "select COMPORT, SPEED from RDX_PC_ChannelGSMModem where CHANNELUNITID = ?"),
        new Stmt(SQL_STAT_FAKE, "update RDX_PC_SentMessage set channelKind = ? where id = ?"),
        new Stmt(SQL_STAT_SMPP, "update RDX_PC_SentMessage set smppBytesSent = ?, smppCharsSent = ?, smppPartsSent = ? where id = ?"),
        new Stmt(SQL_MARK_AS_FAILED, "update RDX_PC_OutMessage set failedMessage = ?, failedTryCount = failedTryCount + 1, failedLastSendDate = SYSDATE, failedIsUnrecoverable = ?, inprocess = 0 where id = ?")

    };

    static {
        Arrays.sort(SQL_LIST);
    }

    private final NewPCUnit unit;

    NewDbQueries(final IDatabaseConnectionAccess dbca, final NewPCUnit unit) {
        super(dbca, SQL_LIST);
        if (unit == null) {
            throw new IllegalArgumentException("PCUnit reference can't be null!");
        } else {
            this.unit = unit;
        }
    }

    @Override
    public NewPCUnit.Options getOptions() throws SQLException {
        try {
            return load(NewPCUnit.Options.class, new NewPCUnit.Options(), SQL_READ_OPTIONS, unit.getId());
        } catch (IllegalStateException exc) {
            throw new IllegalUsageError("Unknown DPC_ChannelUnit #" + String.valueOf(unit.getId()));
        }
    }

    @Override
    public Options4GSM[] getGSMOptions(final long channelId) throws SQLException {
        final List<Options4GSM> result;

        try {
            try (final IDbCursor<Options4GSM> query = query(Options4GSM.class, new Options4GSM(), SQL_READ_GSM_OPTIONS, channelId)) {
                result = query.collection(Options4GSM.class);
            }
            return result.toArray(new Options4GSM[result.size()]);
        } catch (Exception exc) {
            throw new IllegalUsageError("Unknown RDX_PC_ChannelGSMModem #" + String.valueOf(channelId));
        }
    }

    @Override
    public Long[] getSuitableChannels(final String kind, final String address, final long importance, final long unitId, final long prty) throws SQLException {
        final List<Long> result;

        try {
            try (final IDbCursor<Long> query = query(Long.class, new Long(0), SQL_READ_GSM_OPTIONS, kind, address, importance, unitId, prty)) {
                result = query.collection(Long.class);
            }
            return result.toArray(new Long[result.size()]);
        } catch (Exception exc) {
            throw new IllegalUsageError("Unknown RDX_PC_ChannelGSMModem #" + String.valueOf(unit.getId()));
        }
    }

    @Override
    public void clearOutMessageFlag(final Long messageId) throws SQLException {
        execute(SQL_CLEAR_MESSAGE_FLAGS, messageId);
    }

    @Override
    public void clearOutMessageFlagAll(final Long channelId) throws SQLException {
        execute(SQL_CLEAR_ALL_FLAGS, channelId);
    }

    @Override
    public Iterable<MessageWithId> getMessages2Send(final Long channelId) throws SQLException {
        final MessageDocument md = MessageDocument.Factory.newInstance();
        final List<MessageWithId> result = new ArrayList<MessageWithId>();

        try (final IDbCursor<MessageDocument> query = query(MessageDocument.class, md, SQL_SELECT_MESSAGES, channelId)) {
            for (MessageDocument item : query.list(MessageDocument.class)) {
                result.add(new MessageWithIdImpl(query.actualId(), item));
            }
            return new Iterable<MessageWithId>() {
                @Override
                public Iterator<MessageWithId> iterator() {
                    return result.iterator();
                }
            };
        } catch (Exception exc) {
            throw new IllegalUsageError("Unknown RDX_PC_ChannelGSMModem #" + String.valueOf(unit.getId()));
        }
    }

    @Override
    public void save2Sent(Long id, String errorMess) throws SQLException {
        execute(SQL_STORE_TO_SENT, errorMess == null || errorMess.isEmpty() ? new Null(Types.VARCHAR) : errorMess, id, id);
    }

    @Override
    public void processExpiredMessages() throws SQLException {
        execute(SQL_PROCESS_EXPIRED_MESSAGES, unit.getId(), unit.getId());
    }

    @Override
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
    protected <T> T createRecord(final ResultSet rs, final Class<T> awaited, final Map<String, Field> fiedls, final int stmtId, final T template) throws Exception {
        if (awaited == MessageDocument.class) {
            MessageDocument md = MessageDocument.Factory.newInstance();
            final MessageType mt = md.addNewMessage();
            final long msgId = rs.getLong("id");

            mt.setBody(nvl(rs, rs.getString("body"), ""));
            mt.setSubject(nvl(rs, rs.getString("subject"), ""));
            mt.setAddressTo(rs.getString("address"));
            mt.setImportance(rs.getLong("importance"));
            mt.setAddressFrom(nvl(rs, rs.getString("addressFrom"), unit.options.recvAddress));
            mt.setSMPPEncoding(nvl(rs, rs.getString("smppEncoding"), ESmppEncoding.LATIN.getValue()));

            try (final IDbCursor<Attachment> attList = query(Attachment.class, Attachment.Factory.newInstance(), SQL_SELECT_ATTACHMENTS, msgId)) {
                if (!attList.isEmpty()) {
                    mt.addNewAttachments().assignAttachmentList(attList.collection(Attachment.class));
                }
            }
            execute(SQL_MARK_MESSAGES_IN_PROCESS, msgId);
            return awaited.cast(md);
        } else if (awaited == Attachment.class) {
            final Attachment att = Attachment.Factory.newInstance();

            att.setSeq(rs.getLong("seq"));
            att.setTitle(rs.getString("title"));
            att.setMimeType(rs.getString("mimeType"));
            att.setData(rs.getBytes("data"));

            return awaited.cast(att);
        } else if (awaited == Long.class) {
            return awaited.cast(Long.valueOf(rs.getLong(1)));
        } else {
            return super.createRecord(rs, awaited, fiedls, stmtId, template);
        }
    }

    @Override
    public Long saveReceived(MessageDocument m) throws SQLException {
        final class Sequence {

            public long id;
        }
        final Sequence seq = load(Sequence.class, new Sequence(), SQL_READ_SEQUENCE);
        final MessageType msg = m.getMessage();

        getConnectionAccess().getConnection().setSavepoint();
        execute(SQL_STORE_RECV, seq.id, msg.getImportance() != null ? msg.getImportance() : 1, msg.getSubject(), msg.getBody(), msg.getSentTime() != null ? new Date(msg.getSentTime().getTime()) : new Null(Types.DATE), unit.getId(), msg.getAddressFrom()
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

    @Override
    public void assignNewChannel(Long messageId, Long newChannel) throws SQLException {
        execute(SQL_ASSIGN_NEW_CHANNEL, newChannel, messageId);
    }

    @Override
    public void markMessageAsFailed(Long messageId, boolean isRecoverable, String message) throws SQLException {
        execute(SQL_MARK_AS_FAILED, message, isRecoverable, messageId);
    }

    private static class MessageWithIdImpl implements MessageWithId {

        private final Long id;
        private final MessageDocument md;

        public MessageWithIdImpl(final Long id, final MessageDocument md) {
            this.id = id;
            this.md = md;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public MessageDocument getMessage() {
            return md;
        }
    }
}
