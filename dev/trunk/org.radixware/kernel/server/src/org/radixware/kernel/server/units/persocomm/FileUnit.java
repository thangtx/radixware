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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.enums.EPersoCommFileFormat;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.units.persocomm.interfaces.ICommunicationAdapter;
import org.radixware.kernel.server.units.persocomm.tools.Utils;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.schemas.personalcommunications.Attachment;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageType;
import org.radixware.schemas.personalcommunications.MessageType.Attachments;

public class FileUnit extends PersoCommUnit {

    private static final Comparator<String> FILE_SORT = new Comparator<String>() {
        @Override
        public int compare(final String o1, final String o2) {
            return o1.replace('.', ' ').compareTo(o2.replace('.', ' '));
        }
    };

    public FileUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
    }

    @Override
    public ICommunicationAdapter getCommunicationAdapter(final CommunicationMode mode) throws IOException {
        switch (mode) {
            case TRANSMIT:
                return this.new WriteFileCommunicationAdapter(options.sendAddress, options.fileFormat);
            case RECEIVE:
                return this.new ReadFileCommunicationAdapter(options.recvAddress);
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
                .add(PCMessages.FILE_FORMAT, options.fileFormat);
    }

    @Override
    public boolean supportsTransmitting() {
        return true;
    }

    @Override
    public boolean supportsReceiving() {
        return true;
    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.FILE_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_FILE.getValue();
    }

    @Override
    protected void checkOptions(final Options options) throws Exception {
        if (options.sendAddress == null || options.sendAddress.isEmpty()) {
            throw new DPCSendException(PCMessages.SEND_ADDRESS_MISSING);
        } else if (options.recvAddress == null || options.recvAddress.isEmpty()) {
            throw new DPCSendException(PCMessages.RECEIVE_ADDRESS_MISSING);
        } else if (options.fileFormat == null || options.fileFormat.isEmpty()) {
            throw new DPCSendException(PCMessages.FILE_FORMAT_MISSING);
        } else {
            try {
                EPersoCommFileFormat.getForValue(options.fileFormat);
            } catch (NoConstItemWithSuchValueError exc) {
                throw new IllegalArgumentException(String.format(PCMessages.WRONG_FILE_FORMAT, options.fileFormat, Utils.getAvailableEnumValues(EPersoCommFileFormat.values())));
            }
        }
    }

    private class WriteFileCommunicationAdapter implements ICommunicationAdapter {

        private final EPersoCommFileFormat format;
        private final File dir;

        public WriteFileCommunicationAdapter(final String sendAddress, final String fileFormat) throws IOException {
            if (sendAddress == null || sendAddress.isEmpty()) {
                throw new IOException(PCMessages.SEND_ADDRESS_MISSING);
            } else if (fileFormat == null || fileFormat.isEmpty()) {
                throw new IOException(PCMessages.FILE_FORMAT_MISSING);
            } else {
                dir = new File(sendAddress);

                if (!dir.exists()) {
                    throw new IOException(PCMessages.WRONG_DIRECTORY_NAME + sendAddress);
                } else if (!dir.canWrite()) {
                    throw new IOException(PCMessages.WRONG_DIRECTORY_ACCESS + sendAddress);
                } else {
                    try {
                        format = EPersoCommFileFormat.getForValue(fileFormat);
                    } catch (NoConstItemWithSuchValueError exc) {
                        throw new IllegalArgumentException(String.format(PCMessages.WRONG_FILE_FORMAT, fileFormat, Utils.getAvailableEnumValues(EPersoCommFileFormat.values())));
                    }
                }
            }
        }

        @Override
        public MessageSendResult sendMessage(MessageWithMeta messageWithMeta) throws DPCSendException {
            final Long messageId = messageWithMeta.id;
            final MessageDocument msg = messageWithMeta.xDoc;
            
            final MessageSendResult result = new MessageSendResult(messageId)
                    .setMessageWithMeta(messageWithMeta);
            switch (format) {
                case XML:
                    final File xmlFile = new File(dir, String.format("%1$012d.xml", Math.abs(messageId)));

                    try {
                        msg.save(xmlFile);
                    } catch (IOException ex) {
                        throw new DPCSendException(PCMessages.FILE_WRITE_ERROR + " [" + xmlFile.getAbsolutePath() + "]: " + ex.getMessage(), ex);
                    }
                    break;
                case BIN:
//                    final File      binFile = new File(dir,String.format("%1$012d.bin",messageId));
//                    
//                    try{msg.save(binFile);
//                    } catch (IOException ex) {
//                        throw new DPCSendException(PCMessages.FILE_WRITE_ERROR + " [" + binFile.getAbsolutePath() + "]: " + ex.getMessage(), ex);
//                    }
                    if (msg.getMessage().isSetAttachments()) {
                        for (int index = 0; index < msg.getMessage().getAttachments().sizeOfAttachmentArray(); index++) {
                            final Attachment att = msg.getMessage().getAttachments().getAttachmentArray(index);
                            final byte[] data = att.getData();

                            try {
                                final EMimeType mt = EMimeType.getForValue(att.getMimeType());
                                final File attFile = new File(dir, String.format("%1$012d-%2$06d%3$s", Math.abs(messageId), index + 1, mt.getExt().length() > 0 ? "." + mt.getExt() : ""));

                                try (final OutputStream os = new FileOutputStream(attFile)) {
                                    os.write(data);
                                } catch (IOException ex) {
                                    throw new DPCSendException(PCMessages.FILE_WRITE_ERROR + " [" + attFile.getAbsolutePath() + "]: " + ex.getMessage(), ex);
                                }
                            } catch (NoConstItemWithSuchValueError exc) {
                                throw new IllegalArgumentException(String.format(PCMessages.WRONG_MIME_FORMAT, att.getMimeType(), Utils.getAvailableEnumValues(EMimeType.values())));
                            }
                        }
                    } else {

                    }
                    break;
                default:
                    throw new UnsupportedOperationException("File format [" + format + "] is not supported now!");
            }
            
            return sendCallback(result);
        }

        @Override
        public MessageDocument receiveMessage() throws DPCRecvException {
            throw new IllegalStateException("This method can't be called for adapter to transmit");
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public boolean isPersistent() {
            return false;
        }
    }

    private class ReadFileCommunicationAdapter implements ICommunicationAdapter {

        private final File dir;
        private final String[] list;
        private int fileNo = 0;

        public ReadFileCommunicationAdapter(final String recvAddress) throws IOException {
            if (recvAddress == null || recvAddress.isEmpty()) {
                throw new IOException(PCMessages.RECEIVE_ADDRESS_MISSING);
            } else {
                dir = new File(recvAddress);

                if (!dir.exists()) {
                    throw new IOException(PCMessages.WRONG_DIRECTORY_NAME + recvAddress);
                } else if (!dir.canWrite()) {
                    throw new IOException(PCMessages.WRONG_DIRECTORY_ACCESS + recvAddress);
                } else {
                    list = dir.list();

                    Arrays.sort(list, FILE_SORT);
                }
            }
        }

        @Override
        public MessageSendResult sendMessage(MessageWithMeta messageWithMeta) throws DPCSendException {
            throw new IllegalStateException("This method can't be called for adapter to receive");
        }

        @Override
        public MessageDocument receiveMessage() throws DPCRecvException {
            if (fileNo >= list.length) {
                return null;
            } else {
                final File fin = new File(dir, list[fileNo]);
                final MessageDocument m;

                try {
                    if (fin.getName().matches("\\d*\\.xml")) {
                        m = MessageDocument.Factory.parse(fin);
                        fin.delete();
                        fileNo++;
                    } else if (fin.getName().matches("\\d*-\\d*\\..*")) {
                        m = MessageDocument.Factory.newInstance();

                        final MessageType mt = m.addNewMessage();
                        final String filePrefix = fin.getName().substring(0, fin.getName().indexOf('-')) + '-';
                        final List<String> listFiles = new ArrayList<>();

                        while (fileNo < list.length && list[fileNo].startsWith(filePrefix)) {
                            listFiles.add(list[fileNo++]);
                        }

                        if (listFiles.size() > 0) {
                            final Attachments container = mt.addNewAttachments();

                            for (String item : listFiles) {
                                final Attachment att = container.addNewAttachment();
                                final File attFile = new File(dir, item);
                                final Long seq = Long.valueOf(attFile.getName().replace('-', '.').split("\\.")[1]);

                                att.setMimeType(Utils.getMimeTypeByFileName(item).getValue());
                                att.setSeq(seq);
                                att.setTitle("");
                                att.setData(Files.readAllBytes(attFile.toPath()));

                                attFile.delete();
                            }
                        }
                    } else {
                        return null;
                    }

                    m.getMessage().setAddressFrom(fin.getPath());
                    return m;
                } catch (XmlException e) {
                    throw new DPCRecvException(PCMessages.FILE_PARSE_ERROR + " [" + fin.getPath() + "]: " + e.getMessage(), e);
                } catch (IOException e) {
                    throw new DPCRecvException(PCMessages.FILE_READ_ERROR + " [" + fin.getPath() + "]: " + e.getMessage(), e);
                }
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
}
