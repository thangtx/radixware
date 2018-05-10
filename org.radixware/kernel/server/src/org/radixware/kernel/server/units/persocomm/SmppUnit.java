/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
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

import com.cloudhopper.commons.charset.Charset;
import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.SmppSessionHandler;
import com.cloudhopper.smpp.impl.DefaultSmppClient;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.BaseSm;
import com.cloudhopper.smpp.pdu.EnquireLink;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.cloudhopper.smpp.pdu.Unbind;
import com.cloudhopper.smpp.tlv.Tlv;
import com.cloudhopper.smpp.tlv.TlvConvertException;
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppInvalidArgumentException;
import com.cloudhopper.smpp.type.SmppTimeoutException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;
import com.cloudhopper.smpp.util.SmppUtil;
import ie.omk.smpp.Address;
import ie.omk.smpp.AlreadyBoundException;
import ie.omk.smpp.BadCommandIDException;
import ie.omk.smpp.Connection;
import ie.omk.smpp.InvalidOperationException;
import ie.omk.smpp.message.BindResp;
import ie.omk.smpp.message.DataSM;
import ie.omk.smpp.message.DeliverSM;
import ie.omk.smpp.message.EnquireLinkResp;
import ie.omk.smpp.message.InvalidParameterValueException;
import ie.omk.smpp.message.SMPPPacket;
import ie.omk.smpp.message.SMPPProtocolException;
import ie.omk.smpp.message.SubmitSM;
import ie.omk.smpp.message.SubmitSMResp;
import ie.omk.smpp.message.UnbindResp;
import ie.omk.smpp.message.tlv.TLVTable;
import ie.omk.smpp.message.tlv.Tag;
import ie.omk.smpp.net.TcpLink;
import ie.omk.smpp.util.ASCIIEncoding;
import ie.omk.smpp.util.AlphabetEncoding;
import ie.omk.smpp.util.BinaryEncoding;
import ie.omk.smpp.util.EncodingFactory;
import ie.omk.smpp.util.HPRoman8Encoding;
import ie.omk.smpp.util.Latin1Encoding;
import ie.omk.smpp.util.MessageEncoding;
import ie.omk.smpp.util.PacketStatus;
import ie.omk.smpp.util.UCS2Encoding;
import ie.omk.smpp.util.UTF16Encoding;
import ie.omk.smpp.version.VersionException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.commons.lang.StringUtils;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EPersoCommDeliveryStatus;
import org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy;
import org.radixware.kernel.common.enums.ESmppEncoding;
import org.radixware.kernel.common.enums.ESmppSessionType;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.ShouldNeverHappenError;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.Utils;
import static org.radixware.kernel.common.utils.Utils.nvlOf;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.exceptions.SqlNoDataFound;
import org.radixware.kernel.server.exceptions.SqlResourceBusy;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.trace.AbstractTraceFilter;
import org.radixware.kernel.server.trace.ServerTrace;
import org.radixware.kernel.server.units.persocomm.interfaces.ICommunicationAdapter;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageStatistics;
import org.radixware.schemas.personalcommunications.MessageType;

class IeOmkSnoopOutputStream extends OutputStream {

    public final String prefix;
    public final ServerTrace trace;
    public IeOmkSnoopOutputStream otherSnoopStream = null;
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    IeOmkSnoopOutputStream(String prefix, ServerTrace trace) {
        this.prefix = prefix;
        this.trace = trace;
    }
    
    void log(String s) {
        trace.debug(prefix + ": " + s, EEventSource.PERSOCOMM_UNIT, true);
    }
    
    void log() {
        final byte[] data = baos.toByteArray();
        log(Hex.encode(data));
        baos.reset();
    }
    
    void flushOther() throws IOException {
        if (otherSnoopStream != null) {
            otherSnoopStream.flushSnoop();
        }
    }
    
    @Override
    public void write(int b) throws IOException {
        write(new byte[]{(byte) b});
    }
    
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        final byte[] b2 = Arrays.copyOfRange(b, off, off + len);
        write(b2);
    }

    @Override
    public void write(byte[] b) throws IOException {
        flushOther();
        baos.write(b);
    }

    @Override
    public void close() throws IOException {
        log("close()");
        log();
    }

    @Override
    public void flush() throws IOException {
        log("flush()");
        log();
    }
    
    public void flushSnoop() {
        if (baos.size() > 0) {
            log("flushSnoop()");
            log();
        }
    }

}

public class SmppUnit extends PersoCommUnit {
    
    private static final int PING_SMSC_PERIOD_MILLIS = SystemPropUtils.getIntSystemProp("rdx.pcunit.smpp.ping.smsc.period.millis", 60000);
    private static final boolean LOG_INCOMING_MESSAGE_BODY_HEX = SystemPropUtils.getBooleanSystemProp("rdx.pcunit.smpp.log.incoming.message.body.hex", false);
    private static final boolean LOG_OUTGOING_MESSAGE_BODY_HEX = SystemPropUtils.getBooleanSystemProp("rdx.pcunit.smpp.log.outgoing.message.body.hex", false);
    private static final int LIB_VARIANT = SystemPropUtils.getIntSystemProp("rdx.pcunit.smpp.lib.variant", 1);
    
    private static final boolean IE_OMK_USE_SNOOP_STREAMS = SystemPropUtils.getBooleanSystemProp("rdx.pcunit.smpp.ie_omk.use.snoop.streams", false);
    
    private static final int CLOUDHOPPER_SUBMIT_TIMEOUT_MILLIS = SystemPropUtils.getIntSystemProp("rdx.pcunit.smpp.cloudhopper.submit.timeout.millis", 10000);
    private static final boolean CLOUDHOPPER_LOG_INCOMING_REQUESTS = SystemPropUtils.getBooleanSystemProp("rdx.pcunit.smpp.cloudhopper.log.incoming.requests", false);

    private static final Map<Integer, String> ERRORS = new HashMap<Integer, String>() {
        {
            put(0x0001, "0x0001 (ESME_RINVMSGLEN) Message too long");
            put(0x0002, "0x0002 (ESME_RINVCMDLEN) Command length is invalid");
            put(0x0003, "0x0003 (ESME_RINVCMDID) Command ID is invalid or not supported");
            put(0x0004, "0x0004 (ESME_RINVBNDSTS) Incorrect bind status for given command");
            put(0x0005, "0x0005 (ESME_RALYBND) Already bound");
            put(0x0006, "0x0006 (ESME_RINVPRTFLG) Invalid Priority Flag");
            put(0x0007, "0x0007 (ESME_RINVREGDLVFLG) Invalid registered delivery flag");
            put(0x0008, "0x0008 (ESME_RSYSERR) System error");
            put(0x000A, "0x000A (ESME_RINVSRCADR) Invalid source address");
            put(0x000B, "0x000B (ESME_RINVDSTADR) Invalid destination address");
            put(0x000C, "0x000C (ESME_RINVMSGID) Message ID is invalid");
            put(0x000D, "0x000D (ESME_RBINDFAIL) Bind failed");
            put(0x000E, "0x000E (ESME_RINVPASWD) Invalid password");
            put(0x000F, "0x000F (ESME_RINVSYSID) Invalid System ID");
            put(0x0011, "0x0011 (ESME_RCANCELFAIL) Cancelling message failed");
            put(0x0013, "0x0013 (ESME_RREPLACEFAIL) Message recplacement failed");
            put(0x0014, "0x0014 (ESME_RMSSQFUL) Message queue full");
            put(0x0015, "0x0015 (ESME_RINVSERTYP) Invalid service type");
            put(0x0033, "0x0033 (ESME_RINVNUMDESTS) Invalid number of destinations");
            put(0x0034, "0x0034 (ESME_RINVDLNAME) Invalid distribution list name");
            put(0x0040, "0x0040 (ESME_RINVDESTFLAG) Invalid destination flag");
            put(0x0042, "0x0042 (ESME_RINVSUBREP) Invalid submit with replace request");
            put(0x0043, "0x0043 (ESME_RINVESMCLASS) Invalid esm class set");
            put(0x0044, "0x0044 (ESME_RCNTSUBDL) Invalid submit to ditribution list");
            put(0x0045, "0x0045 (ESME_RSUBMITFAIL) Submitting message has failed");
            put(0x0048, "0x0048 (ESME_RINVSRCTON) Invalid source address type of number (TON)");
            put(0x0049, "0x0049 (ESME_RINVSRCNPI) Invalid source address numbering plan (NPI)");
            put(0x0050, "0x0050 (ESME_RINVDSTTON) Invalid destination address type of number (TON)");
            put(0x0051, "0x0051 (ESME_RINVDSTNPI) Invalid destination address numbering plan (NPI)");
            put(0x0053, "0x0053 (ESME_RINVSYSTYP) Invalid system type");
            put(0x0054, "0x0054 (ESME_RINVREPFLAG) Invalid replace_if_present flag");
            put(0x0055, "0x0055 (ESME_RINVNUMMSGS) Invalid number of messages");
            put(0x0058, "0x0058 (ESME_RTHROTTLED) Throttling error");
            put(0x0061, "0x0061 (ESME_RINVSCHED) Invalid scheduled delivery time");
            put(0x0062, "0x0062 (ESME_RINVEXPIRY) Invalid Validty Period value");
            put(0x0063, "0x0063 (ESME_RINVDFTMSGID) Predefined message not found");
            put(0x0064, "0x0064 (ESME_RX_T_APPN) ESME Receiver temporary error");
            put(0x0065, "0x0065 (ESME_RX_P_APPN) ESME Receiver permanent error");
            put(0x0066, "0x0066 (ESME_RX_R_APPN) ESME Receiver reject message error");
            put(0x0067, "0x0067 (ESME_RQUERYFAIL) Message query request failed");
            put(0x00C0, "0x00C0 (ESME_RINVTLVSTREAM) Error in the optional part of the PDU body");
            put(0x00C1, "0x00C1 (ESME_RTLVNOTALLWD) TLV not allowed");
            put(0x00C2, "0x00C2 (ESME_RINVTLVLEN) Invalid parameter length");
            put(0x00C3, "0x00C3 (ESME_RMISSINGTLV) Expected TLV missing");
            put(0x00C4, "0x00C4 (ESME_RINVTLVVAL) Invalid TLV value");
            put(0x00FE, "0x00FE (ESME_RDELIVERYFAILURE) Transaction delivery failure");
            put(0x00FF, "0x00FF (ESME_RUNKNOWNERR) Unknown error");
            put(0x0100, "0x0100 (ESME_RSERTYPUNAUTH) ESME not authorised to use specified servicetype");
            put(0x0101, "0x0101 (ESME_RPROHIBITED) ESME prohibited from using specified operation");
            put(0x0102, "0x0102 (ESME_RSERTYPUNAVAIL) Specified servicetype is unavailable");
            put(0x0103, "0x0103 (ESME_RSERTYPDENIED) Specified servicetype is denied");
            put(0x0104, "0x0104 (ESME_RINVDCS) Invalid data coding scheme");
            put(0x0105, "0x0105 (ESME_RINVSRCADDRSUBUNIT) Invalid source address subunit");
            put(0x0106, "0x0106 (ESME_RINVSTDADDRSUBUNIR) Invalid destination address subunit");
            put(0x040B, "0x040B (ESME_RINVBALANCE) Insufficient credits to send message");
            put(0x040C, "0x040C (ESME_RUNESME_SPRTDDESTADDR) Destination address blocked by the ActiveXperts SMPP Demo Server");

        }
    };

    private InetSocketAddress addr;
    private int message_seq = 1;
    private SmppCommunicationAdapter adapter;
    private long lastPingMillis = 0;

    public SmppUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
        getTrace().addTraceFilter(new SMPPUnitTraceFilter());
    }
    
    @Override
    protected boolean prepareImpl() {
        boolean ok = super.prepareImpl();
        if (ok) {
            deliveryCallbackDispatcher = new DeliveryCallbackDispatcher();
            deliveryCallbackThread = new Thread(deliveryCallbackDispatcher, "PersoCommDeliveryCallbackThread for unit #" + getId());
            deliveryCallbackThread.start();
        }
        return ok;
    }

    @Override
    protected void unprepareImpl() {
        if (adapter != null) {
            try {
                adapter.close();
            } catch (Exception ex) {
                logErrorOnStop(ex);
            } finally {
                adapter = null;
            }
        }
        super.unprepareImpl();
    }
    
    @Override
    protected void maintenanceImpl() throws InterruptedException {
        super.maintenanceImpl();
        if (!isShuttingDown() && adapter != null) {
            try {
                ensureConnectedAdapter();
            } catch (IOException ex) {
                getTrace().put(EEventSeverity.WARNING, "Exception while checking connection to provider: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.UNIT);
            }
        }
    }

    @Override
    public ICommunicationAdapter getCommunicationAdapter(CommunicationMode mode) throws IOException {
        switch (mode) {
            case TRANSMIT:
            case RECEIVE:
                return ensureConnectedAdapter();
            default:
                throw new UnsupportedOperationException("Communication mode [" + mode + "] is not supported in the [" + this.getClass().getSimpleName() + "] adapter!");
        }
    }

    private ICommunicationAdapter ensureConnectedAdapter() throws IOException {
        boolean needClose = false;
        if (adapter != null) {
            if (!adapter.isConnected()) {
                needClose = true;
            } else if (PING_SMSC_PERIOD_MILLIS > 0 && Math.abs(System.currentTimeMillis() - lastPingMillis) > PING_SMSC_PERIOD_MILLIS) {
                if (adapter.ping()) {
                    lastPingMillis = System.currentTimeMillis();
                } else {
                    needClose = true;
                }
            }
        }
        if (needClose) {
            try {
                adapter.close();
            } catch (Exception ex) {
                getTrace().put(EEventSeverity.DEBUG, "Exception while closing connection to provider: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.UNIT);
            } finally {
                adapter = null;
            }
        }
        if (adapter != null) {
            return adapter;
        }
        adapter = LIB_VARIANT == 0 ? new IeOmkSmppApiCommunicationAdapter(addr, ESmppSessionType.getForValue(options.smppSessionType))
                : new CloudhopperSmppCommunicationAdapter(addr, ESmppSessionType.getForValue(options.smppSessionType));
        lastPingMillis = System.currentTimeMillis();
        return adapter;
    }

    @Override
    public OptionsGroup optionsGroup(final Options options) {
        return new OptionsGroup().add(PCMessages.SEND_PERIOD, options.sendPeriod).add(PCMessages.RECV_PERIOD, options.recvPeriod);
    }

    @Override
    public boolean supportsTransmitting() {
        return Objects.equals(options.smppSessionType, ESmppSessionType.TRX.getValue()) || Objects.equals(options.smppSessionType, ESmppSessionType.TX.getValue());
    }

    @Override
    public boolean supportsReceiving() {
        return Objects.equals(options.smppSessionType, ESmppSessionType.TRX.getValue()) || Objects.equals(options.smppSessionType, ESmppSessionType.RX.getValue());
    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.SMPP_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_SMPP.getValue();
    }

    @Override
    protected void checkOptions(Options options) throws Exception {
        if (options.smppSystemId == null || options.smppSystemId.isEmpty()) {
            throw new IllegalArgumentException("smppSystemId can't be null or empty!");
        } else if (options.smppSourceAddressTon == null) {
            throw new IllegalArgumentException("smppSourceAddressTon can't be null or empty!");
        } else if (options.smppSourceAddressNpi == null) {
            throw new IllegalArgumentException("smppSourceAddressNpi can't be null or empty!");
        } else if (options.smppEncoding == ESmppEncoding.HPROMAN8) {
            throw new IllegalArgumentException("HPROMAN8 encoding not supported!");
        } else {
            try {
                addr = ValueFormatter.parseInetSocketAddress(options.sendAddress);
            } catch (Exception ex) {
                throw new IllegalArgumentException(PCMessages.WRONG_SEND_ADDRESS_FORMAT, ex);
            }
        }
    }

    protected Connection getSMPPConnection(final InetSocketAddress addr) throws UnknownHostException {
        final IeOmkSnoopOutputStream in = new IeOmkSnoopOutputStream("in", getTrace());
        final IeOmkSnoopOutputStream out = new IeOmkSnoopOutputStream("out", getTrace());
        in.otherSnoopStream = out;
        out.otherSnoopStream = in;
        final TcpLink link = new TcpLink(addr.getHostString(), addr.getPort());
        link.setSnoopStreams(IE_OMK_USE_SNOOP_STREAMS ? in : null, IE_OMK_USE_SNOOP_STREAMS ? out : null);
        final Connection conn = new Connection(link, false);
        return conn;
    }

    public static class SMPP_Message {

        private final String address;
        private final EncodingParameters encodingParams;
        private final byte[][] data;
        
        public SMPP_Message(final String address, final EncodingParameters encodingParams, final byte[][] data) {
            this.address = address;
            this.encodingParams = encodingParams;
            this.data = data;
        }

        public String getAddress() {
            return address;
        }

        public EncodingParameters getEncodingParams() {
            return encodingParams;
        }

        public byte[][] getData() {
            return data;
        }
    }
    
    protected abstract class SmppCommunicationAdapter implements ICommunicationAdapter {
        protected MessageStatistics stat;

        public abstract boolean isConnected();

        public abstract boolean ping();

        protected void fillStatistics(final int sizeInChars, final int sizeInBytes, final int partCount) throws DPCSendException {
            final MessageStatistics.SMPP smpp = stat.addNewSMPP();
            smpp.setStatSmppPartCount(Long.valueOf(partCount));
            smpp.setStatSmppSizeInBytes(Long.valueOf(sizeInBytes));
            smpp.setStatSmppSizeInChars(Long.valueOf(sizeInChars));
        }
        
        @Override
        public boolean isPersistent() {
            return true;
        }

        protected SMPP_Message prepareMessage(MessageDocument md) throws DPCSendException {
            final String body = md.getMessage().getBody();
            final String encoding = md.getMessage().getSMPPEncoding();
            final EncodingParameters parm = messageEncoding(ESmppEncoding.getForValue(encoding), options.smppEncoding);
            final byte[] mess = parm.encodeBody(body);
            int sizeInChars = 0, sizeInBytes = 0, partCount = 0;
            message_seq = (message_seq + 1) & 0xFF;
            sizeInChars += body.length();
            if (mess.length < 140) {
                sizeInBytes += mess.length;
                partCount++;
                fillStatistics(sizeInChars, sizeInBytes, partCount);
                return new SMPP_Message(md.getMessage().getAddressTo(), parm, new byte[][]{mess});
            } else {
                final List<byte[]> parts = new ArrayList<>();
                final int numParts = (int) (mess.length / 134) + (mess.length % 134 != 0 ? 1 : 0);
                for (int i = 0; i < numParts; i++) {
                    final int partLength = (i + 1) == numParts ? mess.length - (i * 134) : 134;
                    final byte[] mess_with_udh = new byte[6 + partLength];
                    mess_with_udh[0] = (byte) 0x05;
                    mess_with_udh[1] = (byte) 0x00;
                    mess_with_udh[2] = (byte) 0x03;
                    mess_with_udh[3] = (byte) message_seq;
                    mess_with_udh[4] = (byte) numParts;
                    mess_with_udh[5] = (byte) (i + 1);
                    System.arraycopy(mess, i * 134, mess_with_udh, 6, partLength);
                    sizeInBytes += mess_with_udh.length;
                    partCount++;
                    parts.add(mess_with_udh);
                }
                fillStatistics(sizeInChars, sizeInBytes, partCount);
                return new SMPP_Message(md.getMessage().getAddressTo(), parm, parts.toArray(new byte[parts.size()][]));
            }
        }

        protected boolean isSmppSendFailureRecoverable(int commandStatus) {
            // SMPP 3.4 subset (5.1.3 command_status)
            // See SMPP 5.0 (Chapter 4.7.6 command_status, error_status_code) for description
            switch (commandStatus) {
                case PacketStatus.SYSTEM_ERROR:
// ESME_RSYSERR           0x00000008  System Error.
                case PacketStatus.MESSAGE_QUEUE_FULL:
// ESME_RMSGQFUL          0x00000014  Message Queue Full.
                case PacketStatus.SUBMIT_FAILED:
// ESME_RSUBMITFAIL       0x00000045  submit_sm, data_sm or submit_multi failed.
                case PacketStatus.THROTTLING_ERROR:
// ESME_RTHROTTLED        0x00000058  Throttling error (ESME has exceeded allowed message limits).
                case PacketStatus.DELIVERY_FAILED:
// ESME_RDELIVERYFAILURE  0x000000FE  Transaction Delivery Failure.
                case PacketStatus.INVALID_BIND_STATUS:
// 0x00000004 ESME_RINVBNDSTS - Incorrect BIND Status for given command.
                    return true;
            }
            return false;
        }
        
        void checkCommandStatus(long messageId, int commandStatus) throws DPCSendException {
            if (commandStatus != 0) {
                final String exMess = String.format("Unable to send message #%d, SMSC returned command_status = 0x%02x", messageId, commandStatus);
                if (isSmppSendFailureRecoverable(commandStatus)) {
                    throw new DPCRecoverableSendException(exMess);
                } else {
                    throw new DPCSendException(exMess);
                }
            }
        }
        
        protected EPersoCommDeliveryStatus getDeliveryStatus(int smppMessageState) {
            final EPersoCommDeliveryStatus deliveryStatus;
            if (smppMessageState == SMPPPacket.SM_STATE_INVALID) {
                        // SMPP 5.0 | 4.7.15 message_state
                // Message State: UNKNOWN; Value = 7; Type = N/A
                // Message is in invalid state
                // The message state is unknown. This may be due to some internal MC problem which may be intermediate or a permanent
                // This state should never be returned. A MC experiencing difficulties that prevents it from returning a message state, would use this state.
                deliveryStatus = null; // update later to some final status
            } else if (smppMessageState == SMPPPacket.SM_STATE_EN_ROUTE) {
                deliveryStatus = null; // to not update because matches with default Tracking status
            } else if (Utils.in(smppMessageState, SMPPPacket.SM_STATE_DELIVERED, SMPPPacket.SM_STATE_ACCEPTED)) {
                deliveryStatus = EPersoCommDeliveryStatus.DELIVERED;
            } else {
                deliveryStatus = EPersoCommDeliveryStatus.FAILED;
            }
            return deliveryStatus;
        }
        
        protected void processDeliveryReceipt(String smppMessageId, int smppMessageState) throws SQLException {
            // message_state | Should be present for MC Delivery  Receipts and Intermediate Notifications. | 4.8.4.37
            // receipted_message_id | MC message ID of message being receipted.
            // Should be present for MC Delivery Receipts and Intermediate Notifications. | 4.8.4.47
            final EPersoCommDeliveryStatus deliveryStatus = getDeliveryStatus(smppMessageState);
            debug("Delivery receipt received: smppMessageId = " + smppMessageId + ", smppMessageState = " + smppMessageState + ", deliveryStatus = " + (deliveryStatus == null ? "null" : deliveryStatus.getValue()));
            try {
                final SentMessageData message = getDBQuery().lockMessageBySmppMessageId(getPrimaryUnitId(), smppMessageId);
                if (message.deliveryStatus == EPersoCommDeliveryStatus.TRACKING) { // message.isStillTracking()
                    getDBQuery().setMessageDeliveryStatus(message.messageId, deliveryStatus);
                }
                getDbConnection().commit();
                deliveryCallbackDispatcher.addBlocking(new MessageDeliveryInfo(message.messageId, deliveryStatus));
            } catch (SqlNoDataFound ex) {
                debug("Message with SMPP Message Id = '" + smppMessageId + "' not found");
            } catch (SqlResourceBusy ex) {
                debug("Message with SMPP Message Id = '" + smppMessageId + "' is locked");
            } catch (SQLException ex) {
                throw ex;
            } catch (InterruptedException ex) {
            }catch (Exception ex) {
                throw new ShouldNeverHappenError(ex);
            }
        }
    
    }
    
    protected class IeOmkSmppApiCommunicationAdapter extends SmppCommunicationAdapter {

        final Connection connection;

        public IeOmkSmppApiCommunicationAdapter(final InetSocketAddress addr, final ESmppSessionType sessionType) throws IOException {
            try {
                this.connection = getSMPPConnection(addr);
                final int connectionType;
                switch (sessionType) {
                    case RX:
                        connectionType = Connection.RECEIVER;
                        break;
                    case TX:
                        connectionType = Connection.TRANSMITTER;
                        break;
                    case TRX:
                        connectionType = Connection.TRANSCEIVER;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown SMPP session type: " + sessionType);
                }
                final BindResp resp = connection.bind(
                        connectionType,
                        options.smppSystemId,
                        options.smppPassword,
                        options.smppSystemType,
                        options.smppSourceAddressTon.intValue(),
                        options.smppSourceAddressNpi.intValue(),
                        options.smppSourceAddress
                );

                if (resp.getCommandStatus() != 0) {
                    throw new IOException(PCMessages.SMSC_BIND_FAILED + ", command status: " + getErrorMess(resp.getCommandStatus()));
                }
            } catch (IOException | InvalidParameterValueException | IllegalArgumentException | AlreadyBoundException | VersionException | SMPPProtocolException ex) {
                throw new IOException(String.format("Error when establishing connection to %1$s: %2$s", addr, ex.getMessage()), ex);
            }
        }

        @Override
        public boolean isConnected() {
            return connection != null && connection.isBound();
        }

        @Override
        public boolean ping() {
            getTrace().put(EEventSeverity.DEBUG, "Trying to ping SMSC...", null, null, getEventSource(), false);
            try {
                EnquireLinkResp resp = connection.enquireLink();
                getTrace().put(EEventSeverity.DEBUG, "Response to ping received (seq=" + resp.getSequenceNum() + ")", null, null, getEventSource(), false);
                return true;
            } catch (Exception ex) {
                getTrace().put(EEventSeverity.WARNING, "SMSC ping failed: " + ExceptionTextFormatter.throwableToString(ex), null, null, getEventSource(), false);
                return false;
            }
        }


        @Override
        public MessageSendResult sendMessage(MessageWithMeta messageWithMeta) throws DPCSendException {
            final Long messageId = messageWithMeta.id;
            final MessageDocument md = messageWithMeta.xDoc;
            
            stat = MessageStatistics.Factory.newInstance();
            final MessageSendResult result = new MessageSendResult(messageId)
                    .setMessageWithMeta(messageWithMeta)
                    .setStatistics(stat);
            final SMPP_Message msg = prepareMessage(md);
            try {
                SubmitSMResp submitResp;
                final int esmClassMask = (msg.getData().length == 1) ? 0 : 64;
                String smppMessageId = null;
                for (byte[] item : msg.getData()) {
                    final SubmitSM sm = (SubmitSM) connection.newInstance(SMPPPacket.SUBMIT_SM);
                    
                    sm.setDestination(new Address(options.smppDestinationTon.intValue(), options.smppDestinationNpi.intValue(), msg.getAddress()));
                    sm.setSource(new Address(options.smppSourceAddressTon.intValue(), options.smppSourceAddressNpi.intValue(), options.smppSourceAddress));
                    sm.setEsmClass(sm.getEsmClass() | esmClassMask);
                    final boolean needReceipt = supportsReceiving() && options.deliveryTrackingPolicy == EPersoCommDeliveryTrackingPolicy.RECEIPT;
                    sm.setRegistered(needReceipt ? 0x01 : 0x00);
                    if (LOG_OUTGOING_MESSAGE_BODY_HEX) {
                        getTrace().put(EEventSeverity.DEBUG, "Outgoing message body hex = " + Hex.encode(item), EEventSource.PERSOCOMM_UNIT);
                    }
                    sm.setMessage(item, ((IeOmkSmppApiEncodingParameters) msg.getEncodingParams()).encoding);
                    submitResp = (SubmitSMResp)connection.sendRequest(sm);
                    final int commandStatus = submitResp.getCommandStatus();
                    smppMessageId = submitResp.getMessageId();
                    checkCommandStatus(messageId, commandStatus);
                }
                return sendCallback(result.setSmppMessageId(smppMessageId));
            } catch (IOException | AlreadyBoundException ex) {
                throw new DPCRecoverableSendException(ex.getMessage(), ex);
            } catch (BadCommandIDException | InvalidParameterValueException | SMPPProtocolException | ie.omk.smpp.UnsupportedOperationException | VersionException ex) {
                throw new DPCSendException(ex.getMessage(), ex);
            }
        }

        private SMPPPacket getMessage() throws DPCRecvException {
            while (connection.packetAvailable() > 0) {
                try {
                    final SMPPPacket packet = connection.readNextPacket();
                    if (packet instanceof ie.omk.smpp.message.EnquireLink) {
                        getTrace().put(EEventSeverity.DEBUG, "EnquireLink request received", EEventSource.PERSOCOMM_UNIT);
                        connection.ackEnquireLink((ie.omk.smpp.message.EnquireLink)packet);
                    } else {
                        return packet;
                    }
                } catch (IOException | InvalidOperationException | SMPPProtocolException ex) {
                    throw new DPCRecvException(ex.getMessage(), ex);
                }
            }
            return null;
        }

        @Override
        public MessageDocument receiveMessage() throws DPCRecvException, SQLException {
            final SMPPPacket msg = getMessage();
            if (msg == null) {
                return null;
            }
            
            if ((msg instanceof DeliverSM || msg instanceof DataSM) && (msg.getEsmClass() & 0x3C) == 0x04) {
                TLVTable tags = msg.getTLVTable();
                if (tags.get(Tag.RECEIPTED_MESSAGE_ID) != null && tags.get(Tag.MESSAGE_STATE) != null) {
                    // message_state | Should be present for MC Delivery  Receipts and Intermediate Notifications. | 4.8.4.37
                    // receipted_message_id | MC message ID of message being receipted.
                    // Should be present for MC Delivery Receipts and Intermediate Notifications. | 4.8.4.47
                    final String smppMessageId = (String) tags.get(Tag.RECEIPTED_MESSAGE_ID);
                    final int smppMessageState = (int) tags.get(Tag.MESSAGE_STATE);
                    
                    processDeliveryReceipt(smppMessageId, smppMessageState);
                    return deliveryReceiptDocument;
               }
            }
            
            final MessageDocument md = MessageDocument.Factory.newInstance();
            final MessageType mess = md.addNewMessage();
            final EncodingParameters encodingParams = messageEncoding(null, options.smppEncoding);
            final byte[] bodyBytes = msg.getMessage();
            if (LOG_INCOMING_MESSAGE_BODY_HEX) {
                final String bodyHex = Hex.encode(bodyBytes);
                getTrace().put(EEventSeverity.DEBUG, "Incoming message body hex = " + bodyHex, EEventSource.PERSOCOMM_UNIT);
            }
            if (msg.getSource() != null) {
                mess.setAddressFrom(msg.getSource().toString());
            }
            final String body = encodingParams.decodeBody(bodyBytes);
            mess.setBody(body);
            return md;
        }

        @Override
        public void close() throws IOException {
            try {
                if (connection.isBound()) {
                    final UnbindResp ubr = connection.unbind();
                    if (ubr.getCommandStatus() != 0) {
                        getTrace().put(EEventSeverity.ERROR, PCMessages.SMSC_UNBIND_ERROR + ", error code: " + getErrorMess(ubr.getErrorCode()), null, null, EEventSource.PERSOCOMM_UNIT, false);
                    }
                }
            } finally {
                try {
                    if (connection.getState() != Connection.UNBOUND) {
                        connection.force_unbind();
                    }
                    connection.closeLink();
                } catch (Exception e) {
                    getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(e), null, null, EEventSource.PERSOCOMM_UNIT, false);
                }
            }
        }
    }
    
    protected class CloudhopperSmppCommunicationAdapter extends SmppCommunicationAdapter {

        final DefaultSmppClient client = new DefaultSmppClient();
        final SmppSessionConfiguration sessionConfig = new SmppSessionConfiguration();
        SmppSessionHandler sessionHandler;
        SmppSession session = null;
        volatile boolean unbindReceived = false;
        final ArrayBlockingQueue<IncomingQueueItem> incomingQueue = new ArrayBlockingQueue<>(1000);
        
        protected class IncomingQueueItem {
            final MessageDocument messageDocument;
            final String deliveryReceiptMessageId;
            final Integer deliveryReceiptMessageState;

            public IncomingQueueItem(MessageDocument messageDocument, String deliveryReceiptMessageId, Integer deliveryReceiptMessageState) {
                this.messageDocument = messageDocument;
                this.deliveryReceiptMessageId = deliveryReceiptMessageId;
                this.deliveryReceiptMessageState = deliveryReceiptMessageState;
            }
            
            public IncomingQueueItem(MessageDocument messageDocument) {
                this(messageDocument, null, null);
            }
        }

        protected class CloudhopperSmppSessionHandler extends DefaultSmppSessionHandler {
            
            private PduResponse createResponse(PduRequest request, int commandStatus) {
                final PduResponse response = request.createResponse();
                response.setCommandStatus(commandStatus);
                return response;
            }
            
            @Override
            public PduResponse firePduRequestReceived(PduRequest pduRequest) {
                if (CLOUDHOPPER_LOG_INCOMING_REQUESTS) {
                    getTrace().put(EEventSeverity.DEBUG, "Received: pduRequest = " + pduRequest, EEventSource.PERSOCOMM_UNIT);
                }
                
                if (pduRequest instanceof EnquireLink) {
                    getTrace().put(EEventSeverity.DEBUG, "EnquireLink request received", EEventSource.PERSOCOMM_UNIT);
                    return ((EnquireLink)pduRequest).createResponse();
                } else if (pduRequest instanceof Unbind) {
                    event("Unbind request received");
                    unbindReceived = true;
                    return ((Unbind) pduRequest).createResponse();
                }
                
                if (!supportsReceiving()) {
                    getTrace().put(EEventSeverity.DEBUG, "Discarding unexpected received message", EEventSource.PERSOCOMM_UNIT);
                    return createResponse(pduRequest, SmppConstants.STATUS_INVCMDID);
                }

                if (!(pduRequest instanceof BaseSm)) {
                    return super.firePduRequestReceived(pduRequest);
                }

                final BaseSm request = (BaseSm) pduRequest;

                if (SmppUtil.isMessageTypeSmscDeliveryReceipt(request.getEsmClass())) {
                    debug("SmscDeliveryReceipt received");
                    final Tlv msgIdTlv = request.getOptionalParameter(SmppConstants.TAG_RECEIPTED_MSG_ID);
                    final Tlv msgStateTlv = request.getOptionalParameter(SmppConstants.TAG_MSG_STATE);

                    final String msgId;
                    try {
                        msgId = msgIdTlv.getValueAsString();
                    } catch (TlvConvertException ex) {
                        getTrace().put(EEventSeverity.ERROR, "Failed to get 'Receipted Message ID' tag: " + ExceptionTextFormatter.exceptionStackToString(ex), EEventSource.PERSOCOMM_UNIT);
                        return createResponse(request, SmppConstants.STATUS_INVOPTPARSTREAM); // Error in the optional part of the PDU Body
                    }

                    final int msgState;
                    try {
                        msgState = msgStateTlv.getValueAsByte();
                    } catch (TlvConvertException ex) {
                        getTrace().put(EEventSeverity.ERROR, "Failed to get 'Message State' tag: " + ExceptionTextFormatter.exceptionStackToString(ex), EEventSource.PERSOCOMM_UNIT);
                        return createResponse(request, SmppConstants.STATUS_INVOPTPARSTREAM); // Error in the optional part of the PDU Body
                    }

                    incomingQueue.add(new IncomingQueueItem(deliveryReceiptDocument, msgId, msgState));

                    return request.createResponse();
                } else {
                    final MessageDocument md = MessageDocument.Factory.newInstance();
                    final MessageType mess = md.addNewMessage();
                    final EncodingParameters encodingParams = messageEncoding(null, options.smppEncoding);

                    final byte[] bodyBytes = request.getShortMessage();
                    if (LOG_INCOMING_MESSAGE_BODY_HEX) {
                        final String bodyHex = Hex.encode(bodyBytes);
                        getTrace().put(EEventSeverity.DEBUG, "Incoming message body hex = " + bodyHex, EEventSource.PERSOCOMM_UNIT);
                    }
                    
                    String body;
                    try {
                        body = encodingParams.decodeBody(bodyBytes);
                    } catch (DPCRecvException ex) {
                        getTrace().put(EEventSeverity.ERROR, "Failed to decode message body: " + ExceptionTextFormatter.exceptionStackToString(ex), EEventSource.PERSOCOMM_UNIT);
                        
                        final PduResponse resp = request.createResponse();
                        resp.setCommandStatus(SmppConstants.STATUS_X_R_APPN); // ESME Receiver Reject Message Error Code
                        return resp;
                    }
                    mess.setBody(body);

                    if (request.getSourceAddress() != null && StringUtils.isNotBlank(request.getSourceAddress().getAddress())) {
                        mess.setAddressFrom(request.getSourceAddress().getAddress());
                    }
                    if (request.getDestAddress() != null && StringUtils.isNotBlank(request.getDestAddress().getAddress())) {
                        mess.setAddressTo(request.getDestAddress().getAddress());
                    }
                    
                    if ("USSD".equals(request.getServiceType())) {
                        mess.setIsUssd(true);
                        final Tlv ussdServiceOpTlv = request.getOptionalParameter(SmppConstants.TAG_USSD_SERVICE_OP);
                        if (ussdServiceOpTlv != null) {
                            try {
                                final long ussdServiceOp = ussdServiceOpTlv.getValueAsByte() & 0xFF;
                                mess.setUssdServiceOp(ussdServiceOp);
                            } catch (TlvConvertException ex) {
                                getTrace().put(EEventSeverity.ERROR, "Failed to get 0x0501 ussd_service_op tag value: " + ExceptionTextFormatter.exceptionStackToString(ex), EEventSource.PERSOCOMM_UNIT);
                                return createResponse(request, SmppConstants.STATUS_INVOPTPARSTREAM); // Error in the optional part of the PDU Body
                            }
                        }
                    }

                    incomingQueue.add(new IncomingQueueItem(md));
                    return pduRequest.createResponse();
                }
            }

            @Override
            public void fireChannelUnexpectedlyClosed() {
                unbindReceived = true;
            }
        }

        public CloudhopperSmppCommunicationAdapter(final InetSocketAddress addr, final ESmppSessionType sessionType) throws IOException {
            sessionConfig.setHost(addr.getHostString());
            sessionConfig.setPort(addr.getPort());
            sessionConfig.setSystemId(options.smppSystemId);
            sessionConfig.setSystemType(options.smppSystemType);
            sessionConfig.setPassword(options.smppPassword);
            sessionConfig.setName("SMPP ChanneUnit #" + getId());

            switch (sessionType) {
                case RX:
                    sessionConfig.setType(SmppBindType.RECEIVER);
                    break;
                case TX:
                    sessionConfig.setType(SmppBindType.TRANSMITTER);
                    break;
                case TRX:
                    sessionConfig.setType(SmppBindType.TRANSCEIVER);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown SMPP session type: " + sessionType);
            }

            bind();
        }
        
        private void bind() throws IOException {
            try {
                debug("Bind started");
                sessionHandler = new CloudhopperSmppSessionHandler();
                session = client.bind(sessionConfig, sessionHandler);
                debug("Bind finished");
            } catch (SmppTimeoutException | SmppChannelException | UnrecoverablePduException | InterruptedException ex) {
                client.destroy();
                throw new IOException(String.format("Error when establishing connection to %1$s: %2$s", addr, ex.getMessage()), ex);
            } catch (Throwable ex) {
                client.destroy();
                throw ex;
            }

            if (!session.isBound()) {
                throw new IOException("Unexpected session state: !session.isBound()");
            }

            if (session.isClosed()) {
                throw new IOException("Unexpected session state: session.isClosed()");
            }
            
//            if (!session.isOpen()) {
//                throw new IOException("Unexpected session state: !session.isOpen()");
//                // (!!!!) Commented due to permanent "!isOpen" condition
//            }
        }
        
        private void checkBind() throws IOException {
            if (unbindReceived) {
                debug("Closing and destroying session due to unbind...");
                if (session.isBound()) {
                    session.unbind(10000);
                }
                if (!session.isClosed()) {
                    session.close();
                }
                session.destroy();
                unbindReceived = false;
                debug("Closing and destroying session done");
                bind();
            }
        }

        @Override
        public boolean isConnected() {
            return session != null && session.isBound() && !session.isClosed();
        }

        @Override
        public boolean ping() {
            getTrace().put(EEventSeverity.DEBUG, "Trying to ping SMSC...", null, null, getEventSource(), false);
            try {
                checkBind();
                com.cloudhopper.smpp.pdu.EnquireLinkResp resp = session.enquireLink(new EnquireLink(), 10000);
                getTrace().put(EEventSeverity.DEBUG, "Response to ping received (seq=" + resp.getSequenceNumber() + ")", null, null, getEventSource(), false);
                return true;
            } catch (Exception ex) {
                getTrace().put(EEventSeverity.WARNING, "SMSC ping failed: " + ExceptionTextFormatter.throwableToString(ex), null, null, getEventSource(), false);
                return false;
            }
        }

        @Override
        public MessageSendResult sendMessage(MessageWithMeta messageWithMeta) throws DPCSendException {
            try {
                checkBind();
            } catch (IOException ex) {
                throw new DPCRecoverableSendException("Bind failed", ex);
            }
            
            final Long messageId = messageWithMeta.id;
            final MessageDocument md = messageWithMeta.xDoc;

            stat = MessageStatistics.Factory.newInstance();
            final MessageSendResult result = new MessageSendResult(messageId)
                    .setMessageWithMeta(messageWithMeta)
                    .setStatistics(stat);
            final SMPP_Message msg = prepareMessage(md);

            try {
                final byte esmClassMask = (msg.getData().length == 1) ? SmppConstants.ESM_CLASS_MM_DEFAULT : SmppConstants.ESM_CLASS_UDHI_MASK;
                String smppMessageId = null;

                for (byte[] item : msg.getData()) {
                    SubmitSm sm = new SubmitSm();
                    sm.setSourceAddress(new com.cloudhopper.smpp.type.Address(options.smppSourceAddressTon.byteValue(), options.smppSourceAddressNpi.byteValue(), options.smppSourceAddress));
                    sm.setDestAddress(new com.cloudhopper.smpp.type.Address(options.smppDestinationTon.byteValue(), options.smppDestinationNpi.byteValue(), msg.getAddress()));
                    sm.setEsmClass(esmClassMask);
                    sm.setDataCoding((byte) msg.getEncodingParams().dataCoding);
                    final boolean needReceipt = supportsReceiving() && options.deliveryTrackingPolicy == EPersoCommDeliveryTrackingPolicy.RECEIPT;
                    sm.setRegisteredDelivery(needReceipt ? (byte) 1 : (byte) 0);
                    if (LOG_OUTGOING_MESSAGE_BODY_HEX) {
                        getTrace().put(EEventSeverity.DEBUG, "Outgoing message body hex = " + Hex.encode(item), EEventSource.PERSOCOMM_UNIT);
                    }
                    sm.setShortMessage(item);
                    
                    final boolean isUssd = nvlOf(md.getMessage().getIsUssd(), false);
                    if (isUssd) {
                        sm.setServiceType("USSD");
                        if (md.getMessage().isSetUssdServiceOp()) {
                            final byte ussdServiceOp = md.getMessage().getUssdServiceOp().byteValue();
                            sm.setOptionalParameter(new Tlv(SmppConstants.TAG_USSD_SERVICE_OP, new byte[]{ ussdServiceOp } ));
                        }
                    }
                    
                    final SubmitSmResp resp = session.submit(sm, CLOUDHOPPER_SUBMIT_TIMEOUT_MILLIS);
                    final int commandStatus = resp.getCommandStatus();
                    smppMessageId = resp.getMessageId();
                    checkCommandStatus(messageId, commandStatus);
                }

                return sendCallback(result.setSmppMessageId(smppMessageId));
            } catch (SmppInvalidArgumentException ex) {
                throw new ShouldNeverHappenError(ex);
            } catch (RecoverablePduException | UnrecoverablePduException | SmppTimeoutException | SmppChannelException | InterruptedException ex) {
                final String exMess = ex.getMessage() != null ? ex.getMessage() : (ex.getClass().getCanonicalName() + " exception raised");
                throw new DPCRecoverableSendException(exMess, ex).setIoExceptionAdapter(true);
            }
        }

        @Override
        public MessageDocument receiveMessage() throws DPCRecvException, SQLException {
            final IncomingQueueItem item = incomingQueue.poll();
            if (item == null) {
                return null;
            }
            
            final MessageDocument md = item.messageDocument;
            if (md == deliveryReceiptDocument) {
                final String smppMessageId = item.deliveryReceiptMessageId;
                final int smppMessateState = item.deliveryReceiptMessageState;
                processDeliveryReceipt(smppMessageId, smppMessateState);
            }
            return md;
        }

        @Override
        public void close() throws IOException {
            if (session.isBound()) {
                session.unbind(10000);
            }
            if (!session.isClosed()) {
                session.close();
            }
            session.destroy();
            client.destroy();
        }
    }

    private static String getErrorMess(final int errCode) {
        if (errCode == 0) {
            return "0x0000 (ESME_ROK) No Error";
        } else if (ERRORS.containsKey(errCode)) {
            return ERRORS.get(errCode);
        } else {
            return String.valueOf(errCode);
        }
    }

    private EncodingParameters messageEncoding(final ESmppEncoding enc, final ESmppEncoding defaultEnc) {
        final ESmppEncoding smppEncoding = Utils.nvlOf(enc, defaultEnc, ESmppEncoding.ALPHABET);
        final byte dataCoding = getSmppDataCoding(smppEncoding);
        
        if (LIB_VARIANT == 0) {
            final MessageEncoding encoding = getIeOmkEncoding(smppEncoding);
            return new IeOmkSmppApiEncodingParameters(dataCoding, encoding);
        } else {
            final Charset charset = getCloudhopperCharset(smppEncoding);
            final String charsetName = getJavaCharsetName(smppEncoding);
            return new CloudhopperEncodingParameters(dataCoding, charsetName, charset);
        }
    }
    
    private byte getSmppDataCoding(ESmppEncoding smppEncoding) {
        switch (smppEncoding) {
            case ALPHABET: return 0; // SMSC Default Alphabet
            case ASCII: return 1; // IA5 (CCITT T.50)/ASCII (ANSI X3.4)
            case BINARY: return 4; // Octet unspecified (8-bit binary)
            case HPROMAN8: return 0;
            case LATIN: return 3; // Latin 1 (ISO-8859-1)
            case UCS2:
            case UTF16_BigEndian:
            case UTF16_LittleEndian: return 8; // UCS2 (ISO/IEC-10646)
        }
        throw new ShouldNeverHappenError("Unsupported SMPP Encoding: " + smppEncoding.getName());
    }
    
    private MessageEncoding getIeOmkEncoding(ESmppEncoding smppEncoding) {
        try {
            switch (smppEncoding) {
                case ALPHABET: return EncodingFactory.getInstance().getDefaultAlphabet();
                case ASCII: return new ASCIIEncoding();
                case BINARY: return new BinaryEncoding();
                case HPROMAN8: return new HPRoman8Encoding();
                case LATIN: return new Latin1Encoding();
                case UCS2: return new UCS2Encoding();
                case UTF16_BigEndian: return new UTF16Encoding(true);
                case UTF16_LittleEndian: return new UTF16Encoding(false);
            }
        } catch (UnsupportedEncodingException ex) {
            getTrace().put(EEventSeverity.WARNING, "Unsupported SMPP Encoding: " + ex.getMessage() + ". Using default.", null, null, EEventSource.PERSOCOMM_UNIT.getValue(), false);
        }
        return EncodingFactory.getInstance().getDefaultAlphabet(); 
    }
    
    private Charset getCloudhopperCharset(ESmppEncoding smppEncoding) {
        switch (Utils.nvlOf(smppEncoding, ESmppEncoding.ALPHABET)) {
            case ALPHABET: return CharsetUtil.CHARSET_GSM;
            case ASCII:
            case BINARY: return null; // JavaCharsetName used
            case HPROMAN8: return null; // not supported, used default
            case LATIN: return CharsetUtil.CHARSET_ISO_8859_1;
            case UCS2: return CharsetUtil.CHARSET_UCS_2;
            case UTF16_BigEndian:
            case UTF16_LittleEndian: return null; // JavaCharsetName used
        }
        
        return CharsetUtil.CHARSET_GSM;
    }
    
    private String getJavaCharsetName(ESmppEncoding smppEncoding) {
        switch (Utils.nvlOf(smppEncoding, ESmppEncoding.ALPHABET)) {
            case ALPHABET: return null;
            case ASCII: return "US-ASCII";
            case BINARY: return null;
            case HPROMAN8: return null; // not supported
            case LATIN: return null; // "ISO-8859-1", but smpp libs conversion used
            case UCS2: return null;
            case UTF16_BigEndian: return "UTF-16BE";
            case UTF16_LittleEndian: return "UTF-16LE";
        }

        return null;
    }
    
    protected static abstract class EncodingParameters {
        public final byte dataCoding;
        public final String charsetName;

        public EncodingParameters(byte dataCoding, String charsetName) {
            this.dataCoding = dataCoding;
            this.charsetName = charsetName;
        }
        
        public byte[] encodeBody(String body) throws DPCSendException {
            final byte[] result;
            try {
                result = charsetName == null ? body.getBytes() : body.getBytes(charsetName);
            } catch (UnsupportedEncodingException ex) {
                throw new DPCSendException(ex.getMessage(), ex);
            }
            return result;
        }

        public String decodeBody(byte[] body) throws DPCRecvException {
            final String result;
            try {
                result = charsetName == null ? new String(body) : new String(body, charsetName);
            } catch (UnsupportedEncodingException ex) {
                throw new DPCRecvException(ex.getMessage(), ex);
            }
            return result;
        }
    }
    
    protected static class IeOmkSmppApiEncodingParameters extends EncodingParameters {
        private final MessageEncoding encoding;
        
        public IeOmkSmppApiEncodingParameters(final byte dataCoding, final MessageEncoding encoding) {
            super(dataCoding, null);
            this.encoding = encoding;
        }
        
        @Override
        public byte[] encodeBody(String body) throws DPCSendException {
            final byte[] result = encoding instanceof AlphabetEncoding
                    ? ((AlphabetEncoding) encoding).encodeString(body)
                    : super.encodeBody(body);
            return result;
        }
        
        @Override
        public String decodeBody(byte[] body) throws DPCRecvException {
            final String result = encoding instanceof AlphabetEncoding
                    ? ((AlphabetEncoding) encoding).decodeString(body)
                    : super.decodeBody(body);
            return result;
        }
    }
    
    protected static class CloudhopperEncodingParameters extends EncodingParameters {

        public final Charset chCharset;

        public CloudhopperEncodingParameters(byte dataCoding, String charsetName, Charset chCharset) {
            super(dataCoding, charsetName);
            this.chCharset = chCharset;
        }

        @Override
        public byte[] encodeBody(String body) throws DPCSendException {
            byte[] result = chCharset == null ? super.encodeBody(body) : chCharset.encode(body);
            return result;
        }

        @Override
        public String decodeBody(byte[] body) throws DPCRecvException {
            final String result = chCharset == null ? super.decodeBody(body) : chCharset.decode(body);
            return result;
        }
    }

    private static class SMPPUnitTraceFilter extends AbstractTraceFilter {

        @Override
        public boolean canPut(EEventSeverity severity, String localizedMessage, String mlsId, List<String> mlsArgs, String source, String context, long millisOrMinusOne, boolean isSensitive, Collection<ServerTrace.ETraceDestination> targetDestinations, String floodKey) {
            if (severity == EEventSeverity.WARNING && localizedMessage.startsWith("ie.omk.smpp.Connection: Disabling optional parameter support")) {
                return false;
            }
            return true;
        }

    }
}
