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

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EMailPriority;
import org.radixware.kernel.common.enums.EPersoCommImportance;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.mail.Letter;
import org.radixware.kernel.common.mail.Receiver;
import org.radixware.kernel.common.mail.SettingsForReceiveMail;
import org.radixware.kernel.common.mail.SettingsForSendMail;
import org.radixware.kernel.common.mail.enums.EMailAuthentication;
import org.radixware.kernel.common.mail.enums.EMailSecureConnection;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.units.persocomm.interfaces.ICommunicationAdapter;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.schemas.personalcommunications.Attachment;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageType;
import org.radixware.schemas.personalcommunications.MessageType.Attachments;

public class MailUnit extends PersoCommUnit {

    protected static final long MAX_MAIL_TIMEOUT_MILLIS = SystemPropUtils.getIntSystemProp("rdx.pcunit.mail.timeout.millis", 10000);
    private long attachmentsSeq = 0;
    
    // timeout enfocing variables
    private final ExecutorService timeoutExecutor;
    private TimeoutRunnable timeoutRunnable = null;
    final LocalTracer trace = getTrace().newTracer(getEventSource());

    public MailUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
        final TimeoutThreadFactory timeoutThreadFactory = new TimeoutThreadFactory(title);
        timeoutExecutor = Executors.newFixedThreadPool(1, timeoutThreadFactory);
    }

    @Override
    public ICommunicationAdapter getCommunicationAdapter(CommunicationMode mode) throws IOException {
        switch (mode) {
            case TRANSMIT:
                return this.new WriteMailCommunicationAdapter(options.sendAddress, options.emailLogin, options.emailPassword, options.emailSecureConnection == null ? EMailSecureConnection.NONE : options.emailSecureConnection);
            case RECEIVE:
                return options.pop3Address == null || options.pop3Address.isEmpty()
                        ? this.new EmptyReadMailCommunicationAdapter()
                        : this.new ReadMailCommunicationAdapter(options.pop3Address, options.emailLogin, options.emailPassword);
            default:
                throw new UnsupportedOperationException("Communication mode [" + mode + "] is not supported in the [" + this.getClass().getSimpleName() + "] adapter!");
        }
    }

    @Override
    public OptionsGroup optionsGroup(final Options options) {
        return new OptionsGroup()
                .add(PCMessages.SEND_PERIOD, options.sendPeriod)
                .add(PCMessages.RECV_PERIOD, options.recvPeriod)
                .add(PCMessages.SEND_ADDRESS, options.sendAddress)
                .add(PCMessages.RECV_ADDRESS, options.recvAddress)
                .add(PCMessages.POP3ADDRESS, options.pop3Address)
                .add(PCMessages.EMAIL_SECURE, options.emailSecureConnection == null ? EMailSecureConnection.NONE : options.emailSecureConnection);
    }

    @Override
    protected void checkOptions(final Options options) throws Exception {
        if (options.sendAddress == null || options.sendAddress.isEmpty()) {
            throw new IllegalArgumentException("Missing send address for mail unit!");
        } else if (options.pop3Address != null && !options.pop3Address.isEmpty()) {
            if (options.emailLogin == null || options.emailLogin.isEmpty()) {
                throw new IllegalArgumentException("Missing mail login for pop3Address!");
            } else if (options.emailPassword == null || options.emailPassword.isEmpty()) {
                throw new IllegalArgumentException("Missing mail password for pop3Address!");
            }
        }
    }

    @Override
    public boolean supportsTransmitting() {
        return true;
    }

    @Override
    public boolean supportsReceiving() {
        return options.pop3Address != null && !options.pop3Address.isEmpty();
    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.MAIL_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_SMTP.getValue();
    }

    protected MessageDocument letter2Message(final Letter letter) throws DPCRecvException {
        final MessageDocument result = MessageDocument.Factory.newInstance();
        final MessageType mt = result.addNewMessage();

        mt.setAddressFrom(letter.getAddressFrom());
        mt.setAddressTo(letter.getAddressTo());
        mt.setBody(letter.getTextContent());
        mt.setSubject(letter.getSubject());
        if (letter.getSentTime() != null) {
            mt.setSentTime(new Timestamp(letter.getSentTime().getTime()));
        }

        switch (letter.getPriority()) {
            case LOW:
                mt.setImportance(EPersoCommImportance.LOW.getValue());
                break;
            case NORMAL:
                mt.setImportance(EPersoCommImportance.NORMAL.getValue());
                break;
            case HIGH:
                mt.setImportance(EPersoCommImportance.HIGH.getValue());
                break;
            default:
                mt.setImportance(EPersoCommImportance.LOW.getValue());
                break;
        }

        ArrayList<Letter.Attachment> attList = letter.getAttachementsList();

        if (attList.size() > 0) {
            final Attachments atts = mt.addNewAttachments();

            for (Letter.Attachment letterAtt : attList) {
                try {
                    final Attachment att = atts.addNewAttachment();

                    att.setMimeType(letterAtt.getMimeType());
                    att.setData(Files.readAllBytes(letterAtt.getFile().toPath()));
                    Files.deleteIfExists(letterAtt.getFile().toPath());
                } catch (IOException ex) {
                    throw new DPCRecvException(PCMessages.READ_ATTACHMENT_ERROR + ex.getMessage(), ex);
                }
            }
        }
        return result;
    }

    protected Letter message2Letter(final MessageDocument msg) throws DPCSendException {
        final Letter result = new Letter();
        
        if (msg.getMessage().getAddressFrom() == null || msg.getMessage().getAddressFrom().isEmpty()) {
            throw new DPCSendException("Source (from) address for the mail message can't be null or empty!");
        } else if (msg.getMessage().getAddressTo() == null || msg.getMessage().getAddressTo().isEmpty()) {
            throw new DPCSendException("Destination (to) address for the mail message can't be null or empty!");
        } else {
            result.setAddressTo(msg.getMessage().getAddressTo());
            result.setAddressFrom(msg.getMessage().getAddressFrom());
            result.setSubject(msg.getMessage().getSubject());
            result.setTextContent(msg.getMessage().getBody());

            if (msg.getMessage().isSetImportance()) {
                switch (EPersoCommImportance.getForValue(msg.getMessage().getImportance())) {
                    case HIGH:
                        result.setPriority(EMailPriority.HIGH);
                        break;
                    case NORMAL:
                        result.setPriority(EMailPriority.NORMAL);
                        break;
                    case LOW:
                        result.setPriority(EMailPriority.LOW);
                        break;
                }
            }
            if (msg.getMessage().isSetAttachments()) {
                final List<Letter.Attachment> toAdd = result.getAttachementsList();
                final File attsDir = new File(getTempDir(), "atts_" + (attachmentsSeq++) + "_" + System.currentTimeMillis());
                attsDir.mkdir();

                for (Attachment messAtt : msg.getMessage().getAttachments().getAttachmentList()) {
                    try {
                        final File f = new File(attsDir, messAtt.getTitle());

                        Files.write(f.toPath(), messAtt.getData());
                        toAdd.add(new Letter.Attachment(f, messAtt.getMimeType()));
                    } catch (Exception ex) {
                        throw new DPCRecoverableSendException(PCMessages.WRITE_ATTACHMENT_ERROR + ": " + ex.getClass().getSimpleName() + " " + ex.getMessage(), ex);
                    }
                }
            }
            return result;
        }
    }
    
    void startTimeoutEnforcing(LocalTracer trace, long timeoutMillis, Transport transport) {
        timeoutRunnable = new MailTimeoutRunnable(trace, timeoutMillis, transport);
        timeoutExecutor.submit(timeoutRunnable);
    }
    
    void startTimeoutEnforcing(Transport transport) {
        if (options.sendTimeout != null) {
            final long timeoutMillis = options.sendTimeout * 1000;
            startTimeoutEnforcing(trace, timeoutMillis, transport);
        }
    }
    
    private void stopTimeoutEnforcing() {
        if (timeoutRunnable != null) {
            timeoutRunnable.cancel();
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        timeoutExecutor.shutdown();
        super.finalize();
    }

    private class WriteMailCommunicationAdapter implements ICommunicationAdapter {

        private final SettingsForSendMail sendSettings = new SettingsForSendMail();
        private final Properties props = new Properties();
        private final Session session;

        private final Transport tr;
        
        public WriteMailCommunicationAdapter(final String sendAddress, final String emailLogin, final String emailPassword, final EMailSecureConnection secureConnection) throws IOException {
            final InetSocketAddress addr = ValueFormatter.parseInetSocketAddress(sendAddress);

            sendSettings.setHost(addr.getHostString());
            sendSettings.setPort(addr.getPort() == 0 ? 25 : addr.getPort());
            sendSettings.setUser(emailLogin);
            sendSettings.setPassword(emailPassword);
            sendSettings.setSecureconnection(secureConnection);

            if (sendSettings.getHost() == null || sendSettings.getHost().isEmpty()) {
                throw new IOException("Mail SMTP server post address is not defined");
            } else {
                props.setProperty("mail.smtp.host", sendSettings.getHost());
                props.put("mail.smtp.port", String.valueOf(sendSettings.getPort()));
            }
            if (sendSettings.getAuthentiication() != EMailAuthentication.NONE) {
                props.put("mail.smtp.auth", "true");
            }
            if (sendSettings.getSecureconnection() == EMailSecureConnection.TLS_ENCRYPTION) {
                props.put("mail.smtp.starttls.required", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.ssl.trust", "*");
            }
            if (options.sendTimeout == null) {
                props.put("mail.smtp.connectiontimeout", String.valueOf(MAX_MAIL_TIMEOUT_MILLIS));
                props.put("mail.smtp.timeout", String.valueOf(MAX_MAIL_TIMEOUT_MILLIS));
                props.put("mail.smtp.writetimeout", String.valueOf(MAX_MAIL_TIMEOUT_MILLIS));
            }
            session = Session.getInstance(props);
            try {
                tr = session.getTransport("smtp");
                startTimeoutEnforcing(tr);
                if (sendSettings.getAuthentiication() != EMailAuthentication.NONE) {
                    tr.connect(sendSettings.getUser(), sendSettings.getPassword());
                } else {
                    tr.connect();
                }
            } catch (MessagingException ex) {
                throw new IOException("Unable to connect to mail server: " + ex.getMessage(), ex);
            } finally {
                stopTimeoutEnforcing();
            }
        }

        @Override
        public MessageSendResult sendMessage(MessageWithMeta messageWithMeta) throws DPCSendException {
            final Long messageId = messageWithMeta.id;
            final MessageDocument md = messageWithMeta.xDoc;
            
            final MessageSendResult result = new MessageSendResult(messageId)
                    .setMessageWithMeta(messageWithMeta);
            final Letter msg = message2Letter(md);
            try {
                final MimeMessage mm = new MimeMessage(session);
                final List<InternetAddress> recipients = new LinkedList<>();
                final String from = msg.getAddressFrom() != null && !msg.getAddressFrom().isEmpty() ? msg.getAddressFrom() : sendSettings.getDefaultSenderAddress();
                final boolean isMessageHtml = msg.getTextContent().startsWith("<html");

                try {
                    for (String addressTo : msg.getAddressTo().split(";")) {
                        if (!addressTo.trim().isEmpty()) {
                            recipients.add(new InternetAddress(addressTo.trim()));
                        }
                    }
                } catch (Exception ex) {
                    throw new DPCSendException("Bad target address '" + msg.getAddressTo() + "': " + ex.getMessage(), ex);
                }

                if (recipients.isEmpty()) {
                    throw new DPCSendException("There is not target address");
                } else {
                    mm.setRecipients(Message.RecipientType.TO, recipients.toArray(new InternetAddress[recipients.size()]));
                }

                if (from == null || from.length() == 0) {
                    throw new DPCSendException("Self mail address is not defined");
                } else {
                    mm.setFrom(new InternetAddress(from));
                }
                mm.addHeader("X-Priority", String.valueOf(msg.getPriority()));
                mm.addHeader("Content-Type", isMessageHtml ? "text/html; charset=UTF-8" : "text/plain; charset=UTF-8; format=flowed");
                mm.setSubject(msg.getSubject(), "UTF-8");
                mm.setSentDate(new Date());

                if (msg.getAttachementsList().size() > 0) {
                    final MimeMultipart mp = new MimeMultipart();
                    final MimeBodyPart mbp = new MimeBodyPart();

                    if (isMessageHtml) {
                        mbp.setContent(msg.getTextContent(), "text/html;charset=UTF-8");
                    } else {
                        mbp.setText(msg.getTextContent(), "UTF-8");
                    }
                    mp.addBodyPart(mbp);

                    for (Letter.Attachment att : msg.getAttachementsList()) {
                        final MimeBodyPart m = new MimeBodyPart();

                        try {
                            m.attachFile(att.getFile());
                            m.setHeader("Content-Type", att.getMimeType());
                            m.setHeader("Content-Type-Encoding", att.getMimeType());
                            mp.addBodyPart(m);
                        } catch (IOException ex) {
                            throw new DPCSendException("Can't attach file: " + att.getFile().getPath());
                        }
                    }
                    mm.setContent(mp);
                } else {
                    if (isMessageHtml) {
                        mm.setContent(msg.getTextContent(), "text/html;charset=UTF-8");
                    } else {
                        mm.setText(msg.getTextContent(), "UTF-8");
                    }
                }
                if (!tr.isConnected()) {
                    throw new DPCRecoverableSendException("Transport is not connected", new IOException("Transport is not connected"));
                }
                startTimeoutEnforcing(tr);
                tr.sendMessage(mm, mm.getAllRecipients());
            } catch (IllegalStateException ex) {
                if (!tr.isConnected()) {
                    throw new DPCRecoverableSendException("Transport was disconnected due to send timeout", ex);
                }
                throw ex;
            } catch (MessagingException ex) {
                final String message = ex.getClass().getSimpleName() + " on " + sendSettings.getHost() + ":" + sendSettings.getPort() + " sending (" + msg.getAddressFrom() + ")->(" + msg.getAddressTo() + "): " + ex.getMessage();
                boolean recoverable = true;
                if (ex instanceof SendFailedException || (ex.getCause() instanceof SendFailedException)) {
                    recoverable = false;
                }
                if (recoverable) {
                    throw new DPCRecoverableSendException(message, ex);
                } else {
                    throw new DPCSendException(message, ex);
                }
            } finally {
                stopTimeoutEnforcing();
                try {
                    if (!msg.getAttachementsList().isEmpty()) {
                        FileUtils.deleteDirectory(msg.getAttachementsList().get(0).getFile().getParentFile());
                    }
                } catch (Exception ex) {
                    getTrace().put(EEventSeverity.WARNING, "Unable to delete attachments: " + ExceptionTextFormatter.throwableToString(ex), null, null, getEventSource(), false);
                }
            }
            return sendCallback(result);
        }

        @Override
        public MessageDocument receiveMessage() throws DPCRecvException {
            throw new IllegalStateException("This method can't be called for adapter to transmit");
        }

        @Override
        public void close() throws IOException {
            if (tr != null) {
                try {
                    tr.close();
                } catch (MessagingException ex) {
                    throw new IOException(ex);
                }
            }
        }

        @Override
        public boolean isPersistent() {
            return false;
        }

    }

    private class ReadMailCommunicationAdapter implements ICommunicationAdapter {

        private final SettingsForReceiveMail recvSettings = new SettingsForReceiveMail();
        private ArrayList<Letter> letters = null;

        public ReadMailCommunicationAdapter(final String recvAddress, final String login, final String password) throws IOException {
            final InetSocketAddress addr = ValueFormatter.parseInetSocketAddress(recvAddress);

            recvSettings.setHost(addr.getHostString());
            recvSettings.setPort(addr.getPort() == 0 ? 110 : addr.getPort());
            recvSettings.setLogin(login);
            recvSettings.setPassword(password);
            recvSettings.setTempDir(getTempDir());
            recvSettings.setTempFilePrefix("attchmt_" + System.currentTimeMillis() + "_rcv_");
        }

        @Override
        public MessageSendResult sendMessage(MessageWithMeta messageWithMeta) throws DPCSendException {
            throw new IllegalStateException("This method can't be called for adapter to receive");
        }

        private Letter getMessage() throws DPCRecvException {
            if (letters == null) {
                try {
                    letters = Receiver.receiveAll(recvSettings);
                } catch (MessagingException | IOException ex) {
                    throw new DPCRecvException(ex.getClass().getSimpleName() + " on " + recvSettings.getHost() + ":" + recvSettings.getPort() + " : " + ex.toString());
                }
            }

            if (letters.size() > 0) {
                return letters.remove(0);
            } else {
                return null;
            }
        }

        @Override
        public MessageDocument receiveMessage() throws DPCRecvException {
            final Letter letter = getMessage();
            if (letter != null) {
                return letter2Message(letter);
            } else {
                return null;
            }
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public boolean isPersistent() {
            return false;
        }
    }

    private class EmptyReadMailCommunicationAdapter implements ICommunicationAdapter {

        public EmptyReadMailCommunicationAdapter() throws IOException {
        }

        @Override
        public MessageSendResult sendMessage(MessageWithMeta messageWithMeta) throws DPCSendException {
            throw new IllegalStateException("This method can't be called for adapter to receive");
        }

        @Override
        public MessageDocument receiveMessage() throws DPCRecvException {
            return null;
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public boolean isPersistent() {
            return false;
        }

    }
    
    static class SendTimeoutThread extends Thread {

        SendTimeoutThread(Runnable runnable, String title) {
            super(runnable, title);
        }
    }

    static class TimeoutThreadFactory implements ThreadFactory {

        final String title;

        TimeoutThreadFactory(String title) {
            this.title = title;
        }

        @Override
        public Thread newThread(Runnable r) {
            final Thread t = new SendTimeoutThread(r, "Timeout thread for '" + title + "'");
            t.setDaemon(true);
            return t;
        }
    }

    static abstract class TimeoutRunnable implements Runnable {

        protected final LocalTracer trace;
        private final long timeoutMillis;
        private final CountDownLatch latch = new CountDownLatch(1);
        private volatile boolean cancelled = false;

        TimeoutRunnable(LocalTracer trace, long timeoutMillis) {
            this.trace = trace;
            this.timeoutMillis = timeoutMillis;
        }

        abstract void onTimeout();

        @Override
        public void run() {
            try {
                latch.await(timeoutMillis, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                return;
            }

            if (!cancelled) {
                onTimeout();
            }
        }

        public void cancel() {
            cancelled = true;
            latch.countDown();
        }

    }

    static class MailTimeoutRunnable extends TimeoutRunnable {

        final Transport transport;

        public MailTimeoutRunnable(LocalTracer trace, long timeoutMillis, Transport transport) {
            super(trace, timeoutMillis);
            this.transport = transport;
        }

        @Override
        void onTimeout() {
            try {
                trace.put(EEventSeverity.DEBUG, "onTimeout: before transport close", null, null, false);
                transport.close();
                trace.put(EEventSeverity.DEBUG, "onTimeout: after transport close", null, null, false);
            } catch (MessagingException ex) {
                trace.put(EEventSeverity.DEBUG, "onTimeout: exception on transport close:\n" + ExceptionTextFormatter.exceptionStackToString(ex), null, null, false);
            }
        }

    } 
}
