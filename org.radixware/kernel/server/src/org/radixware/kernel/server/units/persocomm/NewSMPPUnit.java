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

import ie.omk.smpp.Address;
import ie.omk.smpp.AlreadyBoundException;
import ie.omk.smpp.BadCommandIDException;
import ie.omk.smpp.Connection;
import ie.omk.smpp.InvalidOperationException;
import ie.omk.smpp.message.BindResp;
import ie.omk.smpp.message.InvalidParameterValueException;
import ie.omk.smpp.message.SMPPPacket;
import ie.omk.smpp.message.SMPPProtocolException;
import ie.omk.smpp.message.SMPPResponse;
import ie.omk.smpp.message.SubmitSM;
import ie.omk.smpp.message.UnbindResp;
import ie.omk.smpp.util.ASCIIEncoding;
import ie.omk.smpp.util.BinaryEncoding;
import ie.omk.smpp.util.EncodingFactory;
import ie.omk.smpp.util.HPRoman8Encoding;
import ie.omk.smpp.util.Latin1Encoding;
import ie.omk.smpp.util.MessageEncoding;
import ie.omk.smpp.util.UCS2Encoding;
import ie.omk.smpp.util.UTF16Encoding;
import ie.omk.smpp.version.VersionException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ESmppEncoding;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.trace.AbstractTraceFilter;
import org.radixware.kernel.server.trace.ITraceFilter;
import org.radixware.kernel.server.trace.ServerTrace;
import org.radixware.kernel.server.units.persocomm.interfaces.IDatabaseConnectionAccess;
import org.radixware.kernel.server.units.persocomm.interfaces.IExtendedRadixTrace;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageStatistics;
import org.radixware.schemas.personalcommunications.MessageType;

public class NewSMPPUnit extends NewPCUnit {

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

    public NewSMPPUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
        getTrace().addTraceFilter(new SMPPUnitTraceFilter());
    }

    public NewSMPPUnit(final Instance instModel, final Long id, final String title, final IDatabaseConnectionAccess dbca, final IExtendedRadixTrace trace) {
        super(instModel, id, title, dbca, trace);
        getTrace().addTraceFilter(new SMPPUnitTraceFilter());
    }

    @Override
    public CommunicationAdapter getCommunicationAdapter(CommunicationMode mode) throws DPCRecvException, DPCSendException {
        switch (mode) {
            case TRANSMIT:
                return this.new WriteSMPPCommunicationAdapter(addr);
            case RECEIVE:
                return this.new ReadSMPPCommunicationAdapter(addr);
            default:
                throw new UnsupportedOperationException("Communication mode [" + mode + "] is not supported in the [" + this.getClass().getSimpleName() + "] adapter!");
        }
    }

    @Override
    public OptionsGroup optionsGroup(final Options options) {
        return new OptionsGroup().add(PCMessages.SEND_PERIOD, options.sendPeriod);
    }

    @Override
    public boolean supportsTransmitting() {
        return true;
    }

    @Override
    public boolean supportsReceiving() {
        return false;
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
        } else if (options.smppSourceAddress == null || options.smppSourceAddress.isEmpty()) {
            throw new IllegalArgumentException("smppSourceAddress can't be null or empty!");
        } else {
            try {
                addr = ValueFormatter.parseInetSocketAddress(options.sendAddress);
            } catch (Exception ex) {
                throw new IllegalArgumentException(PCMessages.WRONG_SEND_ADDRESS_FORMAT, ex);
            }
        }
    }

    @Override
    public MessageStatistics getTicket() {
        return MessageStatistics.Factory.newInstance();
    }

    protected Connection getSMPPConnection(final InetSocketAddress addr) throws UnknownHostException {
        return new Connection(addr.getHostString(), addr.getPort(), false);
    }

    public static class SMPP_Message {

        private final String address;
        private final MessageEncoding encoding;
        private final byte[][] data;

        public SMPP_Message(final String address, final MessageEncoding encoding, final byte[][] data) {
            this.address = address;
            this.encoding = encoding;
            this.data = data;
        }

        public String getAddress() {
            return address;
        }

        public MessageEncoding getEncodind() {
            return encoding;
        }

        public byte[][] getData() {
            return data;
        }
    }

    protected class WriteSMPPCommunicationAdapter implements CommunicationAdapter<SMPP_Message> {

        private final Connection sendConnection;
        private MessageStatistics stat;

        public WriteSMPPCommunicationAdapter(final InetSocketAddress addr) throws DPCSendException {
            try {
                this.sendConnection = getSMPPConnection(addr);
                final BindResp resp = sendConnection.bind(
                        Connection.TRANSMITTER,
                        options.smppSystemId,
                        options.smppPassword,
                        options.smppSystemType,
                        options.smppSourceAddressTon.intValue(),
                        options.smppSourceAddressNpi.intValue(),
                        options.smppSourceAddress
                );

                if (resp.getCommandStatus() != 0) {
                    throw new DPCSendException(PCMessages.SMSC_BIND_FAILED + ", command status: " + getErrorMess(resp.getCommandStatus()));
                }
            } catch (IOException | InvalidParameterValueException | IllegalArgumentException | AlreadyBoundException | VersionException | SMPPProtocolException ex) {
                ex.printStackTrace();
                throw new DPCSendException(String.format("Error when establishing connection to %1$s: %2$s", addr, ex.getMessage()), ex);
            }
        }

        @Override
        public SMPP_Message prepareMessage(Long messageId, MessageDocument md) throws DPCSendException {
            try {
                final String body = md.getMessage().getBody();
                final String encoding = md.getMessage().getSMPPEncoding();
                final SMPP_Parameters parm = messageEncoding(encoding != null ? ESmppEncoding.getForValue(encoding) : null, options.smppEncoding);
                final byte[] mess = parm.getCodePage() != null ? body.getBytes(parm.getCodePage()) : body.getBytes();
                int sizeInChars = 0, sizeInBytes = 0, partCount = 0;

                message_seq = (message_seq) % 125;
                sizeInChars += body.length();

                if (mess.length < 140) {
                    sizeInBytes += mess.length;
                    partCount++;

                    fillStatistics(sizeInChars, sizeInBytes, partCount);
                    return new SMPP_Message(md.getMessage().getAddressTo(), parm.getEncoding(), new byte[][]{mess});
                } else {
                    final List<byte[]> parts = new ArrayList<byte[]>();
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
                        System.arraycopy(mess, (i * 134), mess_with_udh, 6, partLength);

                        sizeInBytes += mess_with_udh.length;
                        partCount++;
                        parts.add(mess_with_udh);
                    }

                    fillStatistics(sizeInChars, sizeInBytes, partCount);
                    return new SMPP_Message(md.getMessage().getAddressTo(), parm.getEncoding(), parts.toArray(new byte[parts.size()][]));
                }
            } catch (UnsupportedEncodingException ex) {
                throw new DPCSendException(ex.getMessage(), ex);
            }
        }

        @Override
        public boolean sendMessage(Long messageId, SMPP_Message msg) throws DPCSendException {
            try {
                SMPPResponse smppRs = null;
                if (msg.getData().length == 1) {
                    final SubmitSM sm = (SubmitSM) sendConnection.newInstance(SMPPPacket.SUBMIT_SM);

                    sm.setDestination(new Address(options.smppDestinationTon.intValue(), options.smppDestinationNpi.intValue(), msg.getAddress()));
                    sm.setSource(new Address(options.smppSourceAddressTon.intValue(), options.smppSourceAddressNpi.intValue(), options.smppSourceAddress));

                    sm.setMessage(msg.getData()[0], msg.getEncodind());
                    smppRs = sendConnection.sendRequest(sm);
                } else {
                    for (byte[] item : msg.getData()) {
                        final SubmitSM sm = (SubmitSM) sendConnection.newInstance(SMPPPacket.SUBMIT_SM);

                        sm.setDestination(new Address(options.smppDestinationTon.intValue(), options.smppDestinationNpi.intValue(), msg.getAddress()));
                        sm.setSource(new Address(options.smppSourceAddressTon.intValue(), options.smppSourceAddressNpi.intValue(), options.smppSourceAddress));
                        sm.setEsmClass(sm.getEsmClass() | 64);

                        sm.setMessage(item, msg.getEncodind());
                        smppRs = sendConnection.sendRequest(sm);
                    }
                }
                sendCallback(messageId, null, getStatistics());
            } catch (IOException | AlreadyBoundException | VersionException | SMPPProtocolException | ie.omk.smpp.UnsupportedOperationException | BadCommandIDException ex) {
                throw new DPCSendException(ex.getMessage(), ex);
            }
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
        public SMPP_Message receiveMessage() throws DPCRecvException {
            throw new IllegalStateException("This method can't be called for adapter to transmit");
        }

        @Override
        public MessageDocument unprepareMessage(SMPP_Message msg) throws DPCRecvException {
            throw new IllegalStateException("This method can't be called for adapter to transmit");
        }

        @Override
        public void close() throws IOException {
            if (sendConnection.isBound()) {
                final UnbindResp ubr = sendConnection.unbind();
                if (ubr.getCommandStatus() != 0) {
                    getTrace().put(EEventSeverity.ERROR, PCMessages.SMSC_UNBIND_ERROR + ", error code: " + getErrorMess(ubr.getErrorCode()), null, null, EEventSource.PERSOCOMM_UNIT, false);
                }
            }
            try {
                if (sendConnection.getState() == Connection.UNBOUND) {
                    sendConnection.closeLink();
                }
            } catch (Exception e) {
                getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(e), null, null, EEventSource.PERSOCOMM_UNIT, false);
            }
        }

        private void fillStatistics(final int sizeInChars, final int sizeInBytes, final int partCount) throws DPCSendException {
            if (getStatistics() != null) {
                final MessageStatistics.SMPP smpp = getStatistics().addNewSMPP();

                smpp.setStatSmppPartCount(Long.valueOf(partCount));
                smpp.setStatSmppSizeInBytes(Long.valueOf(sizeInBytes));
                smpp.setStatSmppSizeInChars(Long.valueOf(sizeInChars));
            }
        }
    }

    protected class ReadSMPPCommunicationAdapter implements CommunicationAdapter<SMPPPacket> {

        private final Connection smppConnection;
        private MessageStatistics stat;

        public ReadSMPPCommunicationAdapter(final InetSocketAddress addr) throws DPCRecvException {
            try {
                this.smppConnection = getSMPPConnection(addr);

                final BindResp resp = smppConnection.bind(
                        Connection.RECEIVER,
                        options.smppSystemId,
                        options.smppPassword,
                        options.smppSystemType,
                        options.smppSourceAddressTon.intValue(),
                        options.smppSourceAddressNpi.intValue(),
                        options.smppSourceAddress
                );

                if (resp.getCommandStatus() != 0) {
                    throw new DPCRecvException(PCMessages.SMSC_BIND_FAILED + ", command status: " + getErrorMess(resp.getCommandStatus()));
                }
            } catch (IOException ex) {
                throw new DPCRecvException(String.format("Error when establishing connection to %1$s: %2$s", addr, ex.getMessage()), ex);
            }
        }

        @Override
        public SMPPPacket prepareMessage(Long messageId, MessageDocument md) throws DPCSendException {
            throw new IllegalStateException("This method can't be called for adapter to receive");
        }

        @Override
        public boolean sendMessage(Long messageId, SMPPPacket msg) throws DPCSendException {
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
        public SMPPPacket receiveMessage() throws DPCRecvException {
            if (smppConnection.packetAvailable() > 0) {
                try {
                    return smppConnection.readNextPacket();
                } catch (IOException | InvalidOperationException | SMPPProtocolException ex) {
                    throw new DPCRecvException(ex.getMessage(), ex);
                }
            } else {
                return null;
            }
        }

        @Override
        public MessageDocument unprepareMessage(SMPPPacket msg) throws DPCRecvException {
            try {
                final MessageDocument md = MessageDocument.Factory.newInstance();
                final MessageType mess = md.addNewMessage();
                final SMPP_Parameters parm = messageEncoding(null, options.smppEncoding);
                final byte[] body = msg.getMessage();

                mess.setAddressFrom(msg.getSource().toString());
                mess.setBody(parm.getCodePage() != null ? new String(body, parm.getCodePage()) : new String(body));

                return md;
            } catch (UnsupportedEncodingException ex) {
                throw new DPCRecvException(ex.getMessage(), ex);
            }
        }

        @Override
        public void close() throws IOException {
            if (smppConnection.isBound()) {
                final UnbindResp ubr = smppConnection.unbind();
                if (ubr.getCommandStatus() != 0) {
                    getTrace().put(EEventSeverity.ERROR, PCMessages.SMSC_UNBIND_ERROR + ", error code: " + getErrorMess(ubr.getErrorCode()), null, null, EEventSource.PERSOCOMM_UNIT, false);
                }
            }
            try {
                if (smppConnection.getState() == Connection.UNBOUND) {
                    smppConnection.closeLink();
                }
            } catch (Exception e) {
                getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(e), null, null, EEventSource.PERSOCOMM_UNIT, false);
            }
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

    private SMPP_Parameters messageEncoding(final ESmppEncoding enc, final ESmppEncoding defaultEnc) {
        final ESmppEncoding smppEncoding = enc == null ? defaultEnc : enc;

        if (smppEncoding == null) {
            return new SMPP_Parameters(EncodingFactory.getInstance().getDefaultAlphabet());
        } else {
            try {
                switch (smppEncoding) {
                    case ALPHABET:
                        return new SMPP_Parameters(EncodingFactory.getInstance().getDefaultAlphabet());
                    case ASCII:
                        return new SMPP_Parameters(new ASCIIEncoding(), "US-ASCII");
                    case BINARY:
                        return new SMPP_Parameters(new BinaryEncoding());
                    case HPROMAN8:
                        return new SMPP_Parameters(new HPRoman8Encoding());
                    case LATIN:
                        return new SMPP_Parameters(new Latin1Encoding(), "ISO-8859-1");
                    case UCS2:
                        return new SMPP_Parameters(new UCS2Encoding(), "UTF-16BE");
                    case UTF16_BigEndian:
                        return new SMPP_Parameters(new UTF16Encoding(true), "UTF-16BE");
                    case UTF16_LittleEndian:
                        return new SMPP_Parameters(new UTF16Encoding(false), "UTF-16LE");
                    default:
                        return new SMPP_Parameters(EncodingFactory.getInstance().getDefaultAlphabet());
                }
            } catch (UnsupportedEncodingException ex) {
                getTraceInterface().put(EEventSeverity.WARNING, "Unsupported SMPP Encoding: " + ex.getMessage() + ". Using default.", null, null, EEventSource.PERSOCOMM_UNIT, false);
                return new SMPP_Parameters(EncodingFactory.getInstance().getDefaultAlphabet());
            }
        }
    }

    private static class SMPP_Parameters {

        private final MessageEncoding encoding;
        private final String codePage;

        public SMPP_Parameters(final MessageEncoding encoding) {
            this.encoding = encoding;
            this.codePage = null;
        }

        public SMPP_Parameters(final MessageEncoding encoding, final String codePage) {
            this.encoding = encoding;
            this.codePage = codePage;
        }

        public MessageEncoding getEncoding() {
            return encoding;
        }

        public String getCodePage() {
            return codePage;
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
