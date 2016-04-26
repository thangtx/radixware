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

import java.io.*;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import org.radixware.kernel.common.dialogs.AuthenticationCancelledException;
import org.radixware.kernel.common.enums.EMailPriority;
import org.radixware.kernel.common.enums.EPersoCommImportance;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.mail.*;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.schemas.personalcommunications.Attachment;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageType.Attachments;

public class MailUnit extends PCUnit {

    boolean isEnableRecv;
    private final String attachmentFilePrefix = "attchmt_" + System.currentTimeMillis();

    public MailUnit(Instance instance, Long id, String title) {
        super(instance, id, title);
    }

    private SettingsForSendMail getSendSettings() {
        final SettingsForSendMail sendSettings = new SettingsForSendMail();
        sendSettings.setHost(getAddress(options.sendAddress));
        sendSettings.setPort(Integer.parseInt(getPort(options.sendAddress, "25")));
        return sendSettings;
    }

    private SettingsForReceiveMail getRecvSettings() {
        final SettingsForReceiveMail recvSettings = new SettingsForReceiveMail();
        recvSettings.setHost(getAddress(options.pop3Address));
        recvSettings.setPort(Integer.parseInt(getPort(options.pop3Address, "110")));
        recvSettings.setLogin(options.emailLogin);
        recvSettings.setPassword(options.emailPassword);
        recvSettings.setTempDir(getTempDir());
        recvSettings.setTempFilePrefix(attachmentFilePrefix + "_rcv_");
        return recvSettings;
    }

    @Override
    public String optionsToString() {
        return "{\n\t"
                + PCMessages.SEND_PERIOD + " " + String.valueOf(options.sendPeriod) + "; \n\t"
                + PCMessages.RECV_PERIOD + " " + String.valueOf(options.recvPeriod) + "; \n\t"
                + PCMessages.SEND_ADDRESS + " " + options.sendAddress + "; \n\t"
                + PCMessages.RECV_ADDRESS + " " + options.recvAddress + "; \n\t"
                + PCMessages.POP3ADDRESS + " " + options.pop3Address + "; \n\t"
                + //                PCMessages.EMAIL_LOGIN + " " + options.emailLogin + "; \n\t" +
                //                PCMessages.EMAIL_PASSWORD + " " + options.emailPassword + "; \n\t" +
                "}";
    }

    @Override
    protected void recvMessages() throws DPCRecvException {
        if (isEnableRecv) {
            try {
                ArrayList<Letter> letters = Receiver.receiveAll(getRecvSettings());
                if (letters != null) {
                    for (Letter letter : letters) {
                        MessageDocument messageDocument = convertLetterToMessageDocument(letter);
                        try {
                            recvMessage(messageDocument);
                        } finally {
                            for (Letter.Attachment attachment : letter.getAttachementsList()) {
                                FileUtils.deleteFile(attachment.getFile());
                            }
                        }
                    }
                }
            } catch (MessagingException ex) {
                throw new DPCRecvException(ex.toString());
            } catch (IOException ex) {
                throw new DPCRecvException(ex.toString());
            }
        }
    }

    private Letter convertMessageDocumentToLetter(MessageDocument messDoc, Long id) {
        Letter letter = new Letter();
        letter.setAddressTo(messDoc.getMessage().getAddressTo());
        letter.setAddressFrom(messDoc.getMessage().getAddressFrom());
        letter.setSubject(messDoc.getMessage().getSubject());
        letter.setTextContent(messDoc.getMessage().getBody());
        if (messDoc.getMessage().isSetImportance()) {
            EPersoCommImportance imp = EPersoCommImportance.getForValue(messDoc.getMessage().getImportance());
            switch (imp) {
                case HIGH:
                    letter.setPriority(EMailPriority.HIGH);
                    break;
                case NORMAL:
                    letter.setPriority(EMailPriority.NORMAL);
                    break;
                case LOW:
                    letter.setPriority(EMailPriority.LOW);
                    break;
            }
        }
        if (messDoc.getMessage().isSetAttachments()) {
            ArrayList<Letter.Attachment> letterAtts = letter.getAttachementsList();
            for (Attachment messAtt : messDoc.getMessage().getAttachments().getAttachmentList()) {
                try {
                    final File f = new File(getTempDir(), attachmentFilePrefix + "_snd_" + id + "_" + messAtt.getTitle());
                    Files.write(f.toPath(), messAtt.getData());
                    letterAtts.add(new Letter.Attachment(f, messAtt.getMimeType()));
                } catch (IOException ex) {
                    Logger.getLogger(MailUnit.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return letter;
    }

    private MessageDocument convertLetterToMessageDocument(Letter letter) throws DPCRecvException {
        MessageDocument messDoc = MessageDocument.Factory.newInstance();
        org.radixware.schemas.personalcommunications.MessageType mess = messDoc.addNewMessage();
        mess.setAddressFrom(letter.getAddressFrom());
        mess.setAddressTo(letter.getAddressTo());
        mess.setBody(letter.getTextContent());
        mess.setSubject(letter.getSubject());
        switch (letter.getPriority()) {
            case LOW:
                mess.setImportance(EPersoCommImportance.LOW.getValue());
                break;
            case NORMAL:
                mess.setImportance(EPersoCommImportance.NORMAL.getValue());
                break;
            case HIGH:
                mess.setImportance(EPersoCommImportance.HIGH.getValue());
                break;
        }
        Date d = letter.getSentTime();
        Timestamp ts = new Timestamp(d.getTime());
        mess.setSentTime(ts);
        ArrayList<Letter.Attachment> letterAtts = letter.getAttachementsList();
        if (letterAtts.size() > 0) {
            Attachments messageAtts = mess.addNewAttachments();
            for (Letter.Attachment letterAtt : letterAtts) {
                try {
                    Attachment messageAtt = messageAtts.addNewAttachment();
                    messageAtt.setData(Files.readAllBytes(letterAtt.getFile().toPath()));
                    messageAtt.setMimeType(letterAtt.getMimeType());
                } catch (IOException ex) {
                    throw new DPCRecvException(PCMessages.READ_ATTACHMENT_ERROR, ex);
                }
            }
        }
        return messDoc;
    }

    @Override
    protected void checkOptions() throws Exception {
        isEnableRecv = true;
        if (options.pop3Address == null) {
            isEnableRecv = false;
        } else {
            //if (options.pop3Address.lastIndexOf(':')==-1)
            //    throw new DPCRecvException(PCMessages.WRONG_POP3_ADDRESS);
        }
    }

    @Override
    protected boolean getIsEnableRecv() {
        return isEnableRecv;
    }

    @Override
    protected void send(MessageDocument m, Long id) throws DPCSendException {
        Letter letter = convertMessageDocumentToLetter(m, id);
        try {
            Sender.send(getSendSettings(), letter);
        } catch (AuthenticationCancelledException ex) {
            throw new DPCSendException(ex.toString());
        } catch (MessagingException ex) {
            throw new DPCSendException(ex.toString());
        }
        for (Letter.Attachment attachment : letter.getAttachementsList()) {
            FileUtils.deleteFile(attachment.getFile());
        }
        sendCallback(id, null);
    }

    String convertAddress(String address, String subject) {
        return address;
    }

    String convertSubject(String address, String subject) {
        return subject;
    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.MAIL_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_SMTP.getValue();
    }

    String getAddress(String address) {
        int idx = address.lastIndexOf(':');
        String addr = address;
        if (idx >= 0) {
            addr = address.substring(0, idx);
        }
        if (addr == null || addr.isEmpty()) {
            addr = "127.0.0.1";
        }
        return addr;
    }

    String getPort(String address, String defaultPort) {
        int idx = address.lastIndexOf(':');
        if (idx < 0) {
            return defaultPort;
        }
        return address.substring(idx + 1);
    }
}
