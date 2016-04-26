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

import java.io.UnsupportedEncodingException;

import ie.omk.smpp.Address;
import ie.omk.smpp.Connection;
import ie.omk.smpp.message.BindResp;
import ie.omk.smpp.message.SMPPPacket;
import ie.omk.smpp.message.SubmitSM;
import ie.omk.smpp.message.UnbindResp;
import ie.omk.smpp.net.TcpLink;
import ie.omk.smpp.util.ASCIIEncoding;
import ie.omk.smpp.util.BinaryEncoding;
import ie.omk.smpp.util.EncodingFactory;
import ie.omk.smpp.util.HPRoman8Encoding;
import ie.omk.smpp.util.Latin1Encoding;
import ie.omk.smpp.util.MessageEncoding;
import ie.omk.smpp.util.UCS2Encoding;
import ie.omk.smpp.util.UTF16Encoding;
import java.net.InetAddress;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ESmppEncoding;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageStatistics;
import org.radixware.schemas.personalcommunications.MessageStatistics.SMPP;
import org.radixware.schemas.personalcommunications.MessageType;

public final class SMPPUnit extends PCUnit {

    private String addr;
//    private String enc;
    private int port;
    private MessageEncoding messEnc = null;
    private String codePageName;
    private int message_seq = 0;
    private Connection sendConnection;

    public SMPPUnit(final Instance instance, final Long id, final String title) {
        super(instance, id, title);
    }

    @Override
    public String optionsToString() {
        return "{\n\t"
                + PCMessages.SEND_PERIOD + " " + String.valueOf(options.sendPeriod) + "; \n\t"
                + "}";
    }

    MessageEncoding messageEncoding(ESmppEncoding enc) {
        final ESmppEncoding smppEncoding = enc == null ? options.smppEncoding : enc;

        messEnc = null;
        codePageName = null;
        if (smppEncoding == null) {
            messEnc = EncodingFactory.getInstance().getDefaultAlphabet();
            return messEnc;
        }
//        if (messEnc != null)
//            return messEnc;
        switch (smppEncoding) {
            case ALPHABET:
                messEnc = EncodingFactory.getInstance().getDefaultAlphabet();
                break;
            case ASCII:
                messEnc = new ASCIIEncoding();
                codePageName = "US-ASCII";
                break;
            case BINARY:
                messEnc = new BinaryEncoding();
                break;
            case HPROMAN8:
                messEnc = new HPRoman8Encoding();
                break;
            case LATIN:
                try {
                    messEnc = new Latin1Encoding();
                    codePageName = "ISO-8859-1";
                } catch (UnsupportedEncodingException ex) {
                    //TODO: MLS
                    getTrace().put(EEventSeverity.WARNING, "Unsupported SMPP Encoding: " + ex.getMessage() + ". Using default.", null, null, EEventSource.PERSOCOMM_UNIT, false);
                    messEnc = EncodingFactory.getInstance().getDefaultAlphabet();
//                    Logger.getLogger(SMPPUnit.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case UCS2:
                try {
                    messEnc = new UCS2Encoding();
                    codePageName = "UTF-16BE";
                } catch (UnsupportedEncodingException ex) {
                    //TODO: MLS
                    getTrace().put(EEventSeverity.WARNING, "Unsupported SMPP Encoding: " + ex.getMessage() + ". Using default.", null, null, EEventSource.PERSOCOMM_UNIT, false);
                    messEnc = EncodingFactory.getInstance().getDefaultAlphabet();
//                    Logger.getLogger(SMPPUnit.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case UTF16_BigEndian:
                try {
                    messEnc = new UTF16Encoding(true);
                    codePageName = "UTF-16BE";
                } catch (UnsupportedEncodingException ex) {
                    //TODO: MLS
                    getTrace().put(EEventSeverity.WARNING, "Unsupported SMPP Encoding: " + ex.getMessage() + ". Using default.", null, null, EEventSource.PERSOCOMM_UNIT, false);
                    messEnc = EncodingFactory.getInstance().getDefaultAlphabet();
//                  Logger.getLogger(SMPPUnit.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case UTF16_LittleEndian:
                try {
                    messEnc = new UTF16Encoding(false);
                    codePageName = "UTF-16LE";
                } catch (UnsupportedEncodingException ex) {
                    //TODO: MLS
                    getTrace().put(EEventSeverity.WARNING, "Unsupported SMPP Encoding: " + ex.getMessage() + ". Using default.", null, null, EEventSource.PERSOCOMM_UNIT, false);
                    messEnc = EncodingFactory.getInstance().getDefaultAlphabet();
//                  Logger.getLogger(SMPPUnit.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            default:
                messEnc = EncodingFactory.getInstance().getDefaultAlphabet();
        }
        return messEnc;
    }

    @Override
    protected void recvMessages() throws DPCRecvException {
        try {
            final InetAddress smscAddr = InetAddress.getByName(addr);
            final TcpLink smscLink = new TcpLink(smscAddr, port);
            smscLink.open();
            try {
                final Connection smppConnection = new Connection(smscLink, false);
                try {
                    final BindResp resp = smppConnection.bind(
                            Connection.RECEIVER,
                            options.smppSystemId,
                            options.smppPassword,
                            options.smppSystemType,
                            options.smppSourceAddressTon.intValue(),
                            options.smppSourceAddressNpi.intValue(),
                            options.smppSourceAddress);

                    if (resp.getCommandStatus() != 0) {
                        throw new DPCRecvException(PCMessages.SMSC_BIND_FAILED + ", command status: " + getErrorMess(resp.getCommandStatus()));
                    }

                    SMPPPacket p;
                    while (smppConnection.packetAvailable() > 0) {
                        while ((p = smppConnection.readNextPacket()) != null) {
                            final MessageDocument m = MessageDocument.Factory.newInstance();
                            final MessageType mt = m.addNewMessage();
                            mt.setAddressFrom(p.getSource().toString());
                            messageEncoding(null);
                            final byte[] mess = p.getMessage();
                            if (codePageName != null) {
                                mt.setBody(new String(mess, codePageName));
                            } else {
                                mt.setBody(new String(mess));
                            }
                            recvMessage(m);
                        }
                    }
                } finally {
                    if (smppConnection.isBound()) {
                        final UnbindResp ubr = smppConnection.unbind();
                        if (ubr.getCommandStatus() != 0) {
                            //TODO MLS
                            getTrace().put(EEventSeverity.ERROR, PCMessages.SMSC_UNBIND_ERROR + ", error code: " + getErrorMess(ubr.getErrorCode()), null, null, EEventSource.PERSOCOMM_UNIT, false);
                            //throw new DPCRecvException(PCMessages.SMSC_UNBIND_ERROR);
                        }
                    }
                    try {
                        if (smppConnection.getState() == Connection.UNBOUND) {
                            smppConnection.closeLink();
                        }
                    } catch (Exception e) {
                        //TODO MLS
                        getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(e), null, null, EEventSource.PERSOCOMM_UNIT, false);
                    }
                }
            } finally {
                smscLink.close();
            }
        } catch (Exception e) {
            throw new DPCRecvException(e.getMessage(), e);
        }
    }

    @Override
    protected void sendMessages() throws DPCSendException {
        try {
            sendConnection = new Connection(addr, port, false);
            try {
                final BindResp resp = sendConnection.bind(
                        Connection.TRANSMITTER,
                        options.smppSystemId,
                        options.smppPassword,
                        options.smppSystemType,
                        options.smppSourceAddressTon.intValue(),
                        options.smppSourceAddressNpi.intValue(),
                        options.smppSourceAddress);

                if (resp.getCommandStatus() != 0) {
                    throw new DPCSendException(PCMessages.SMSC_BIND_FAILED + ", command status: " + getErrorMess(resp.getCommandStatus()));
                }

                super.sendMessages();
            } finally {
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
        } catch (Exception ex) {
            if (!(ex instanceof DPCSendException)) {
                throw new DPCSendException("Error on sending messages", ex);
            } else {
                throw (DPCSendException) ex;
            }
        }
    }

    @Override
    protected void send(final MessageDocument m, final Long id, final MessageStatistics stat) throws DPCSendException {
        try {
            if (++message_seq > 125) {
                message_seq = 1;
            }

            int sizeInChars = 0, sizeInBytes = 0, partCount = 0;

            final String body = m.getMessage().getBody();

            sizeInChars += body.length();

            final String enc = m.getMessage().getSMPPEncoding();
            messageEncoding(enc != null ? ESmppEncoding.getForValue(enc) : null);

            byte[] mess;
            if (codePageName != null) {
                mess = body.getBytes(codePageName);
            } else {
                mess = body.getBytes();
            }

            if (mess.length <= 140) {
                SubmitSM sm = (SubmitSM) sendConnection.newInstance(SMPPPacket.SUBMIT_SM);
                sm.setDestination(new Address(options.smppDestinationTon.intValue(),
                        options.smppDestinationNpi.intValue(), m.getMessage().getAddressTo()));
                sm.setSource(new Address(options.smppSourceAddressTon.intValue(),
                        options.smppSourceAddressNpi.intValue(), options.smppSourceAddress));

                sm.setMessage(mess, messEnc);
                sendConnection.sendRequest(sm);

                sizeInBytes += mess.length;
                partCount++;
            } else {
                int numParts = (int) (mess.length / 134) + (mess.length % 134 != 0 ? 1 : 0);
                for (int i = 0; i < numParts; i++) {
                    int partLength = 134;
                    if ((i + 1) == numParts) {
                        partLength = mess.length - (i * 134);
                    }

                    SubmitSM sm = (SubmitSM) sendConnection.newInstance(SMPPPacket.SUBMIT_SM);
                    sm.setDestination(new Address(options.smppDestinationTon.intValue(),
                            options.smppDestinationNpi.intValue(), m.getMessage().getAddressTo()));
                    sm.setSource(new Address(options.smppSourceAddressTon.intValue(),
                            options.smppSourceAddressNpi.intValue(), options.smppSourceAddress));

                    int esm = sm.getEsmClass();
                    esm |= 64;
                    sm.setEsmClass(esm);

                    byte[] mess_with_udh = new byte[6 + partLength];
                    mess_with_udh[0] = (byte) 0x05;
                    mess_with_udh[1] = (byte) 0x00;
                    mess_with_udh[2] = (byte) 0x03;
                    mess_with_udh[3] = (byte) message_seq;
                    mess_with_udh[4] = (byte) numParts;
                    mess_with_udh[5] = (byte) (i + 1);
                    System.arraycopy(mess, (i * 134), mess_with_udh, 6, partLength);

                    sm.setMessage(mess_with_udh, messEnc);
                    sendConnection.sendRequest(sm);
                    sizeInBytes += mess_with_udh.length;
                    partCount++;
                }
            }

            stat.addNewSMPP();
            final SMPP smpp = stat.getSMPP();

            smpp.setStatSmppPartCount(Long.valueOf(partCount));
            smpp.setStatSmppSizeInBytes(Long.valueOf(sizeInBytes));
            smpp.setStatSmppSizeInChars(Long.valueOf(sizeInChars));

        } catch (Exception e) {
            throw new DPCSendException(e.getMessage(), e);
        }
        try {
            sendCallback(id, null);
        } catch (Exception e) {
            throw new DPCSendException(e.getMessage(), e);
        }
    }
    /*
     @Override
     void send(final MessageDocument m, final Long id) throws DPCSendException {
     try {
     final Connection myConnection = new Connection(addr, port, false);
     try {
     final BindResp resp = myConnection.bind(
     Connection.TRANSMITTER,
     options.smppSystemId,
     options.smppPassword,
     options.smppSystemType,
     options.smppSourceAddressTon.intValue(),
     options.smppSourceAddressNpi.intValue(),
     options.smppSourceAddress);

     if (resp.getCommandStatus() != 0) {
     throw new DPCSendException(PCMessages.SMSC_BIND_FAILED + ", command status: " + resp.getCommandStatus());
     }

     if (++message_seq>125) {
     message_seq = 1;
     }

     String body = m.getMessage().getBody();
     int start = 0;
     int seq = 1;
     boolean mustDivide = body.length() > 70;
     final int maxLen = 63;
     while (start < body.length()) {
     String cur;
     if (mustDivide) {
     int end = (int) (start + maxLen) + 1;
     if (end >= body.length())
     end = body.length();
     cur = body.substring(start, end);
     start = end;
     }
     else {
     cur = body;
     start = body.length();
     }
     final SubmitSM sm = (SubmitSM) myConnection.newInstance(SMPPPacket.SUBMIT_SM);
     sm.setDestination(new Address(options.smppDestinationTon.intValue(),
     options.smppDestinationNpi.intValue(), m.getMessage().getAddressTo()));
     sm.setSource(new Address(options.smppSourceAddressTon.intValue(),
     options.smppSourceAddressNpi.intValue(), options.smppSourceAddress));
     messageEncoding();
     byte[] mess;
     if (codePageName != null) {
     mess = cur.getBytes(codePageName);
     }
     else {
     mess = cur.getBytes();
     }
     sm.setMessageEncoding(messEnc);
     if (mustDivide) {
     int esm = sm.getEsmClass();
     esm |= 64;
     sm.setEsmClass(esm);
     //                      TLVTable t = new TLVTable();
     //                      t.set(Tag.SAR_TOTAL_SEGMENTS, body.length()/options.smppMaxLen+1);
     //                      t.set(Tag.SAR_MSG_REF_NUM, seq);
     //                      t.set(Tag.SAR_SEGMENT_SEQNUM, seq++);
     //                      sm.setTLVTable(t);

     final int len = mess.length;
     byte[] mess_with_udh = new byte[len + 6];
     mess_with_udh[0] = 5;
     mess_with_udh[1] = 0;
     mess_with_udh[2] = 3;
     mess_with_udh[3] = (byte)message_seq;
     mess_with_udh[4] = (byte) ((body.length() + maxLen)/(maxLen + 1)); //NEYVABANKRU-148 //(body.length() / maxLen + 1); 
     mess_with_udh[5] = (byte) seq++;
     for (int i=6; i<mess_with_udh.length; i++) {
     mess_with_udh[i] = mess[i-6];
     }
     sm.setMessage(mess_with_udh,messEnc);
     }
     else {
     sm.setMessage(mess,messEnc);
     }
     myConnection.sendRequest(sm);
     }
     } finally {
     if (myConnection.isBound()) {
     final UnbindResp ubr = myConnection.unbind();
     if (ubr.getCommandStatus() != 0) {
     //TODO MLS
     getTrace().put(EEventSeverity.ERROR, PCMessages.SMSC_UNBIND_ERROR + ", error code: " + getErrorMess(ubr.getErrorCode()), null, null, EEventSource.PERSOCOMM_UNIT, false);
     //throw new DPCSendException(PCMessages.SMSC_UNBIND_ERROR  + ", error code: " + ubr.getErrorCode());
     }
     }
     try {
     if (myConnection.getState() == Connection.UNBOUND)                    
     myConnection.closeLink();
     } catch (Exception e) {
     //TODO MLS
     getTrace().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(e), null, null, EEventSource.PERSOCOMM_UNIT, false);
     }
     }
     } catch (Exception e) {
     throw new DPCSendException(e.getMessage(), e);
     }
     try {
     sendCallback(id, null);
     } catch (Exception e) {
     throw new DPCSendException(e.getMessage(), e);
     }
     }
     */

    @Override
    protected void checkOptions() throws Exception {
        messEnc = null;
        codePageName = null;
        try {
            addr = getAddress(options.sendAddress);
            port = Long.valueOf(getPort(options.sendAddress)).intValue();
        } catch (Exception ex) {
            throw new DPCSendException(PCMessages.WRONG_SEND_ADDRESS_FORMAT, ex);
        }
//        enc = options.smppEncoding == null ? java.nio.charset.Charset.defaultCharset().name() : options.smppEncoding.getName();
    }

//    int findSubStr( String s,  int start) throws UnsupportedEncodingException {
//        if (options.smppMaxLen == null)
//        return  start + options.smppMaxLen.intValue();
////        int posEnd = start;
////        while (posEnd < s.length()) {
////            if (s.substring(start, posEnd).getBytes(enc).length > options.smppMaxLen) {
////                return posEnd - 1;
////            }
////            posEnd++;
////        }
////        return posEnd;
//    }
    @Override
    public String getUnitTypeTitle() {
        return PCMessages.SMPP_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_SMPP.getValue();
    }

    String getAddress(final String address) {
        final int idx = address.lastIndexOf(':');
        return address.substring(0, idx);
    }

    String getPort(final String address) {
        final int idx = address.lastIndexOf(':');
        return address.substring(idx + 1);
    }

    private static final String getErrorMess(final int errCode) {
        switch (errCode) {
            case 0x0000:
                return "0x0000 (ESME_ROK) No Error";
            case 0x0001:
                return "0x0001 (ESME_RINVMSGLEN) Message too long";
            case 0x0002:
                return "0x0002 (ESME_RINVCMDLEN) Command length is invalid";
            case 0x0003:
                return "0x0003 (ESME_RINVCMDID) Command ID is invalid or not supported";
            case 0x0004:
                return "0x0004 (ESME_RINVBNDSTS) Incorrect bind status for given command";
            case 0x0005:
                return "0x0005 (ESME_RALYBND) Already bound";
            case 0x0006:
                return "0x0006 (ESME_RINVPRTFLG) Invalid Priority Flag";
            case 0x0007:
                return "0x0007 (ESME_RINVREGDLVFLG) Invalid registered delivery flag";
            case 0x0008:
                return "0x0008 (ESME_RSYSERR) System error";
            case 0x000A:
                return "0x000A (ESME_RINVSRCADR) Invalid source address";
            case 0x000B:
                return "0x000B (ESME_RINVDSTADR) Invalid destination address";
            case 0x000C:
                return "0x000C (ESME_RINVMSGID) Message ID is invalid";
            case 0x000D:
                return "0x000D (ESME_RBINDFAIL) Bind failed";
            case 0x000E:
                return "0x000E (ESME_RINVPASWD) Invalid password";
            case 0x000F:
                return "0x000F (ESME_RINVSYSID) Invalid System ID";
            case 0x0011:
                return "0x0011 (ESME_RCANCELFAIL) Cancelling message failed";
            case 0x0013:
                return "0x0013 (ESME_RREPLACEFAIL) Message recplacement failed";
            case 0x0014:
                return "0x0014 (ESME_RMSSQFUL) Message queue full";
            case 0x0015:
                return "0x0015 (ESME_RINVSERTYP) Invalid service type";
            case 0x0033:
                return "0x0033 (ESME_RINVNUMDESTS) Invalid number of destinations";
            case 0x0034:
                return "0x0034 (ESME_RINVDLNAME) Invalid distribution list name";
            case 0x0040:
                return "0x0040 (ESME_RINVDESTFLAG) Invalid destination flag";
            case 0x0042:
                return "0x0042 (ESME_RINVSUBREP) Invalid submit with replace request";
            case 0x0043:
                return "0x0043 (ESME_RINVESMCLASS) Invalid esm class set";
            case 0x0044:
                return "0x0044 (ESME_RCNTSUBDL) Invalid submit to ditribution list";
            case 0x0045:
                return "0x0045 (ESME_RSUBMITFAIL) Submitting message has failed";
            case 0x0048:
                return "0x0048 (ESME_RINVSRCTON) Invalid source address type of number (TON)";
            case 0x0049:
                return "0x0049 (ESME_RINVSRCNPI) Invalid source address numbering plan (NPI)";
            case 0x0050:
                return "0x0050 (ESME_RINVDSTTON) Invalid destination address type of number (TON)";
            case 0x0051:
                return "0x0051 (ESME_RINVDSTNPI) Invalid destination address numbering plan (NPI)";
            case 0x0053:
                return "0x0053 (ESME_RINVSYSTYP) Invalid system type";
            case 0x0054:
                return "0x0054 (ESME_RINVREPFLAG) Invalid replace_if_present flag";
            case 0x0055:
                return "0x0055 (ESME_RINVNUMMSGS) Invalid number of messages";
            case 0x0058:
                return "0x0058 (ESME_RTHROTTLED) Throttling error";
            case 0x0061:
                return "0x0061 (ESME_RINVSCHED) Invalid scheduled delivery time";
            case 0x0062:
                return "0x0062 (ESME_RINVEXPIRY) Invalid Validty Period value";
            case 0x0063:
                return "0x0063 (ESME_RINVDFTMSGID) Predefined message not found";
            case 0x0064:
                return "0x0064 (ESME_RX_T_APPN) ESME Receiver temporary error";
            case 0x0065:
                return "0x0065 (ESME_RX_P_APPN) ESME Receiver permanent error";
            case 0x0066:
                return "0x0066 (ESME_RX_R_APPN) ESME Receiver reject message error";
            case 0x0067:
                return "0x0067 (ESME_RQUERYFAIL) Message query request failed";
            case 0x00C0:
                return "0x00C0 (ESME_RINVTLVSTREAM) Error in the optional part of the PDU body";
            case 0x00C1:
                return "0x00C1 (ESME_RTLVNOTALLWD) TLV not allowed";
            case 0x00C2:
                return "0x00C2 (ESME_RINVTLVLEN) Invalid parameter length";
            case 0x00C3:
                return "0x00C3 (ESME_RMISSINGTLV) Expected TLV missing";
            case 0x00C4:
                return "0x00C4 (ESME_RINVTLVVAL) Invalid TLV value";
            case 0x00FE:
                return "0x00FE (ESME_RDELIVERYFAILURE) Transaction delivery failure";
            case 0x00FF:
                return "0x00FF (ESME_RUNKNOWNERR) Unknown error";
            case 0x0100:
                return "0x0100 (ESME_RSERTYPUNAUTH) ESME not authorised to use specified servicetype";
            case 0x0101:
                return "0x0101 (ESME_RPROHIBITED) ESME prohibited from using specified operation";
            case 0x0102:
                return "0x0102 (ESME_RSERTYPUNAVAIL) Specified servicetype is unavailable";
            case 0x0103:
                return "0x0103 (ESME_RSERTYPDENIED) Specified servicetype is denied";
            case 0x0104:
                return "0x0104 (ESME_RINVDCS) Invalid data coding scheme";
            case 0x0105:
                return "0x0105 (ESME_RINVSRCADDRSUBUNIT) Invalid source address subunit";
            case 0x0106:
                return "0x0106 (ESME_RINVSTDADDRSUBUNIR) Invalid destination address subunit";
            case 0x040B:
                return "0x040B (ESME_RINVBALANCE) Insufficient credits to send message";
            case 0x040C:
                return "0x040C (ESME_RUNESME_SPRTDDESTADDR) Destination address blocked by the ActiveXperts SMPP Demo Server";
            default:
                return String.valueOf(errCode);
        }
    }

    @Override
    protected void send(MessageDocument m, Long id) throws DPCSendException {
        throw new UnsupportedOperationException("Don't use this method! Use send(MessageDocumet,Long,MessageStatistics) instead!");
    }
}
