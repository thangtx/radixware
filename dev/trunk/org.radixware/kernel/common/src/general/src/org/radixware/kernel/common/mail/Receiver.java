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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import org.radixware.kernel.common.enums.EMailPriority;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;

public class Receiver {

    public static ArrayList<Letter> receiveAll(SettingsForReceiveMail settings) throws MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.pop3.port", String.valueOf(settings.port));
        Session session = Session.getInstance(props);
        Store store = session.getStore("pop3");
        store.connect(settings.host, settings.login, settings.password);
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        try {
            Message messages[] = folder.getMessages();
            ArrayList<Letter> al = new ArrayList<Letter>();
            for (Message m : messages) {
                Letter l = new Letter();
                l.subject = m.getSubject();
                String type = m.getContentType();
                String mixed = "multipart/mixed";
                String textPlain = "text/plain";
                StringBuilder body = new StringBuilder();
                if (type.toLowerCase().startsWith(mixed)) {
                    Multipart mp = (Multipart) m.getContent();
                    for (int i = 0; i < mp.getCount(); i++) {
                        BodyPart bp = mp.getBodyPart(i);
                        type = bp.getContentType();
                        if (type.toLowerCase().startsWith(textPlain)) {
                            if (bp.getContent() instanceof String) {
                                body.append((String) bp.getContent());
                            }
                        } else {
                            File f;
                            if (settings.tempDir == null) {
                                f = File.createTempFile("persocomm_attachment", bp.getFileName());
                            } else {
                                f = File.createTempFile(settings.tempFilePrefix == null ? "persocomm_attachment" : settings.tempFilePrefix, "", settings.getTempDir());
                            }
                            DataHandler dh = bp.getDataHandler();
                            Files.copy(dh.getInputStream(), f.toPath());
                            l.getAttachementsList().add(new Letter.Attachment(f, type));
                        }
                    }
                } else {
                    if (m.getContent() instanceof String) {
                        body.append((String) m.getContent());
                    }
                }

                l.textContent = body.toString();
                StringBuilder sb = new StringBuilder();
                for (Address a : m.getFrom()) {
                    sb.append(a);
                    sb.append(';');
                }
                l.setAddressFrom(sb.substring(0, sb.length() - 1));

                StringBuilder sbTo = new StringBuilder();
                for (Address a : m.getAllRecipients()) {
                    sbTo.append(a);
                    sbTo.append(';');
                }
                l.setAddressTo(sbTo.substring(0, sbTo.length() - 1));
                l.sendTime = m.getSentDate();

                String prior[] = m.getHeader("X-Priority");
                if (prior != null && prior.length > 0) {
                    try {
                        final Long priority = Long.valueOf(prior[0]);
                        l.priority = EMailPriority.getForValue(priority);
                    } catch (NumberFormatException | NoConstItemWithSuchValueError e) {
                        l.priority = EMailPriority.NORMAL;
                    }
                }
                al.add(l);
                m.setFlag(Flags.Flag.DELETED, true);
            }
            return al;
        } finally {
            folder.close(true);
            store.close();
        }
    }
}
