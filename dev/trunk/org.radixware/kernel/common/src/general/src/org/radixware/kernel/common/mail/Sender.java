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
package org.radixware.kernel.common.mail;

import java.awt.HeadlessException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
import org.radixware.kernel.common.dialogs.AuthenticationCancelledException;
import org.radixware.kernel.common.dialogs.LoginDialog;
import org.radixware.kernel.common.exceptions.ExceptionHandler;
import org.radixware.kernel.common.mail.enums.EMailAuthentication;
import org.radixware.kernel.common.mail.enums.EMailSecureConnection;
import org.radixware.kernel.common.utils.Reference;

public class Sender {

    static final HashMap<String, String> passwordsCache = new HashMap<>();

    private static int getSendTimeout() {
        try {
            String prop = System.getProperty("radixware.mail.send.timeout");

            if (prop != null) {
                try {
                    return Integer.parseInt(prop);
                } catch (NumberFormatException ex) {
                }
            }
        } catch (Throwable ex) {
        }
        return 10;
    }

    static public void send(SettingsForSendMail settings, Letter letter) throws AuthenticationCancelledException, MessagingException {
        send(settings, letter, getSendTimeout());
    }

    static public void send(SettingsForSendMail settings, Letter letter, long timeout) throws AuthenticationCancelledException, MessagingException {
        send(settings, letter, timeout, null);
    }

    static public void send(SettingsForSendMail settings, Letter letter, long timeout, final ExceptionHandler exceptionHandler) throws AuthenticationCancelledException, MessagingException {
        Properties props = new Properties();
        if (settings.host == null || settings.host.length() == 0) {
            throw new MessagingException("Mail SMTP server is not defined");
        }
        props.put("mail.smtp.host", settings.host);
        props.put("mail.smtp.port", String.valueOf(settings.port));
        if (settings.authentication != EMailAuthentication.NONE) {
            props.put("mail.smtp.auth", "true");
        }
        if (settings.secureConnection == EMailSecureConnection.TLS_ENCRYPTION) {
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", "*");
        }
        Session session = Session.getInstance(props);
        final MimeMessage msg = new MimeMessage(session);

        List<InternetAddress> recipients = new LinkedList<>();
        for (String addressTo : letter.addressTo.split(";")) {
            if (!addressTo.isEmpty()) {
                recipients.add(new InternetAddress(addressTo));
            }
        }
        msg.setRecipients(Message.RecipientType.TO, recipients.toArray(new InternetAddress[0])/*new InternetAddress(letter.addressTo)*/);

        String from = settings.defaultSenderAddress;
        if (letter.addressFrom != null && letter.addressFrom.length() != 0) {
            from = letter.addressFrom;
        }
        if (from == null || from.length() == 0) {
            throw new MessagingException("Self mail address is not defined");
        }
        msg.setFrom(new InternetAddress(from));
        msg.addHeader("X-Priority", String.valueOf(letter.priority.getValue()));

        boolean isHtml = isHtml(letter.getTextContent());
        if (isHtml) {
            msg.addHeader("Content-Type", "text/html; charset=UTF-8");
        } else {
            msg.addHeader("Content-Type", "text/plain; charset=UTF-8; format=flowed");
        }

        msg.setSubject(letter.subject, "UTF-8");
        msg.setSentDate(new Date());
        if (letter.attachmentsList.size() > 0) {
            MimeMultipart mp = new MimeMultipart();
            MimeBodyPart mbp = new MimeBodyPart();
            if (isHtml) {
                mbp.setContent(letter.getTextContent(), "text/html;charset=UTF-8");
            } else {
                mbp.setText(letter.getTextContent(), "UTF-8");
            }
            mp.addBodyPart(mbp);
            for (Letter.Attachment att : letter.attachmentsList) {
                MimeBodyPart m = new MimeBodyPart();
                try {
                    m.attachFile(att.getFile());
                } catch (IOException ex) {
                    throw new MessagingException("Can't attach file: " + att.getFile().getPath());
                }
                m.setHeader("Content-Type", att.getMimeType()); //"binary"
                m.setHeader("Content-Type-Encoding", att.getMimeType());  //"binary"
                mp.addBodyPart(m);
            }
            msg.setContent(mp);
        } else {
            if (isHtml) {
                msg.setContent(letter.getTextContent(), "text/html;charset=UTF-8");
            } else {
                msg.setText(letter.getTextContent(), "UTF-8");
            }
        }

        if (msg.getAllRecipients() == null) {
            final MessagingException exception = new MessagingException("Incorrect address");
            if (exceptionHandler != null) {
                exceptionHandler.process(exception);
            } else {
                throw exception;
            }
            return;
        }

        final Transport tr = session.getTransport("smtp");
        String password = null;
        if (settings.authentication != EMailAuthentication.NONE) {
            if (settings.rememberPassword && settings.password != null) {
                password = settings.getPassword();
            } else {
                synchronized (passwordsCache) {
                    password = passwordsCache.get(settings.host + ":" + Integer.toString(settings.port));
                }
            }
            tr.connect(settings.getUser(), password);
            synchronized (passwordsCache) {
                passwordsCache.put(settings.host + ":" + Integer.toString(settings.port), password);
            }
            if (settings.rememberPassword) {
                settings.storePassword(password);
            }
        } else {
            tr.connect();
        }

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Reference<Exception> result = new Reference<>();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    tr.sendMessage(msg, msg.getAllRecipients());
                } catch (MessagingException e) {
                    result.set(e);
                }
            }
        };

        Future future = executor.submit(runnable);
        boolean mustClose = true;
        try {
            future.get(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException | HeadlessException e) {
            String smess = e.getMessage();
            if (smess == null || smess.isEmpty()) {
                result.set(new TimeoutException("Send mail timeout detected"));
            } else {
                result.set(e);
            }
            mustClose = false;
        }

        executor.shutdownNow();
        if (mustClose) {
            tr.close();
        }

        if (result.get() != null) {
            if (exceptionHandler != null) {
                exceptionHandler.process(result.get());
            } else {
                throw new MessagingException(result.get().getMessage(), result.get()); //Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, result.get());
            }
        }
    }

    static private boolean isHtml(String content) {
        return content != null && (content.startsWith("<html>") || content.startsWith("<html "));
    }
}
