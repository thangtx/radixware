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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.enums.EPersoCommFileFormat;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.schemas.personalcommunications.Attachment;
import org.radixware.schemas.personalcommunications.MessageDocument;

@Deprecated
public final class FileUnit extends PCUnit {

    public FileUnit(Instance instModel, Long id, String title) {
        super(instModel, id, title);
    }

    @Override
    public String optionsToString() {
        return "{\n\t"
                + PCMessages.SEND_PERIOD + " " + String.valueOf(options.sendPeriod) + "; \n\t"
                + PCMessages.RECV_PERIOD + " " + String.valueOf(options.recvPeriod) + "; \n\t"
                + PCMessages.SEND_ADDRESS + " " + options.sendAddress + "; \n\t"
                + PCMessages.RECV_ADDRESS + " " + options.recvAddress + "; \n\t"
                + PCMessages.FILE_FORMAT + " " + options.fileFormat + "; \n\t"
                + "}";
    }

    @Override
    protected void recvMessages() throws DPCRecvException {
        final File dir = new File(options.recvAddress);
        final File files[] = dir.listFiles();
        if (files == null) {
            throw new DPCRecvException(PCMessages.WRONG_DIRECTORY_NAME + options.recvAddress);
        }
        Arrays.sort(files);
        for (File fin : files) {
            try {
                final MessageDocument m = MessageDocument.Factory.parse(fin);
                m.getMessage().setAddressFrom(fin.getPath());
                recvMessage(m);
                fin.delete();
            } catch (XmlException e) {
                throw new DPCRecvException(PCMessages.FILE_PARSE_ERROR + " '" + fin.getPath() + "'", e);
            } catch (IOException e) {
                throw new DPCRecvException(PCMessages.FILE_READ_ERROR + " '" + fin.getPath() + "'", e);
            }
        }
    }

    private String padLead(int len, String s) {
        final StringBuffer buf = new StringBuffer();
        final int l = s.length();
        for (int i = 0; i < len - l; i++) {
            buf.append('0');
        }
        buf.append(s);
        return buf.toString();
    }

    @Override
    protected void send(MessageDocument m, Long id) throws DPCSendException {
        final String prefix = padLead(12, String.valueOf(id));
        File fout = null;
        try {
            if (options.fileFormat.equals(EPersoCommFileFormat.XML.getValue())) {
                fout = new File(options.sendAddress, prefix + ".xml");
                m.save(fout);
            }
            if (options.fileFormat.equals(EPersoCommFileFormat.BIN.getValue())) {
                if (m.getMessage().isSetAttachments()) {
                    int i = 1;
                    for (Attachment att : m.getMessage().getAttachments().getAttachmentList()) {
                        final EMimeType mt = EMimeType.getForValue(att.getMimeType());
                        String postfix = "";
                        if (mt.getExt().length() > 0) {
                            postfix = "." + mt.getExt();
                        }
                        fout = new File(options.sendAddress, prefix + "-" + padLead(6, String.valueOf(i)) + postfix);
                        final FileOutputStream fos = new FileOutputStream(fout);
                        fos.write(att.getData());
                        fos.close();
                        i++;
                    }
                }
            }
        } catch (IOException e) {
            if (fout != null) {
                throw new DPCSendException(PCMessages.FILE_WRITE_ERROR + " '" + fout.getPath() + "': " + e.getMessage(), e);
            } else {
                throw new DPCSendException(PCMessages.FILE_WRITE_ERROR + ": " + e.getMessage(), e);
            }
        }
        sendCallback(id, null);
    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.FILE_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_FILE.getValue();
    }
}
