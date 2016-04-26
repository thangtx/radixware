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
package org.radixware.kernel.common.svn.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.client.auth.SvnSaslAuthenticator;

/**
 *
 * @author akrylov
 */
public class SvnRAConnection extends SvnConnection {

    private SvnConnector connector;
    private String realm;
    private URI uri;

    private List<String> serverCapabilities = new LinkedList<>();

    private static final String EDIT_PIPELINE = "edit-pipeline";
    private static final String SVNDIFF1 = "svndiff1";
    private static final String ABSENT_ENTRIES = "absent-entries";
    private static final String COMMIT_REVPROPS = "commit-revprops";
    private static final String MERGE_INFO = "mergeinfo";
    private static final String DEPTH = "depth";
    private static final String LOG_REVPROPS = "log-revprops";
    
    public SvnRAConnection(SvnConnector connector) {
        this.connector = connector;
    }

    @Override
    public void open(SvnRepository repository) throws RadixSvnException {
        connector.open(repository);
        uri = repository.getLocation();
        handshake();
        authenticate(repository);
    }

    @Override
    public boolean isAlive() {
        return !connector.isStale();
    }

    public InputStream getInputStream() throws IOException {
        return connector.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return connector.getOutputStream();
    }

    public List<RAMessage.MessageItem> read(boolean extraxtContent, RAMessage.MessageItemTemplate... pattern) throws RadixSvnException {
        try {
            RAMessage message = new RAMessage();

            List<RAMessage.MessageItem> result = new LinkedList<>();
            List<RAMessage.MessageItem> items = message.parseItems(getInputStream());
            int index = 0;
            if (items.isEmpty()) {
                throw new IOException("Unexpected response");
            }

            if (extraxtContent) {
                if (items.get(0).type == RAMessage.MessageItemType.WORD) {
                    if ("success".equals(items.get(0).getString())) {
                        if (items.get(1).type == RAMessage.MessageItemType.LIST) {
                            items = items.get(1).getList();
                        } else {
                            throw new IOException("Unexpected response");
                        }
                    } else if ("failure".equals(items.get(0).getString())) {
                        List<RAMessage.MessageItem> errorArguments = items.get(1).getList();
                        if (errorArguments != null) {
                            if (errorArguments.get(0).type == RAMessage.MessageItemType.LIST) {
                                errorArguments = errorArguments.get(0).getList();
                                long code = errorArguments.get(0).getLong();
                                String msg = errorArguments.get(1).getString();
                                throw new IOException("SVN ERROR " + code + ": " + msg);
                            } else {
                                throw new IOException("Unexpected response");
                            }
                        } else {
                            throw new IOException("Unexpected response");
                        }
                    } else {
                        throw new IOException("Unexpected response");
                    }
                } else {
                    if (items.isEmpty()) {
                        throw new IOException("Unexpected response");
                    }
                }
            }
            if (pattern == null || pattern.length == 0) {
                result.addAll(items);
                return result;
            }
            RAMessage.MessageItemTemplate template = pattern[index];
            loop:
            for (Iterator<RAMessage.MessageItem> iter = items.iterator(); iter.hasNext();) {
                RAMessage.MessageItem item = iter.next();
                if (item.type == template.getType()) {
                    result.add(item);
                    if (!template.isMultiple()) {
                        if (index + 1 < pattern.length) {
                            index++;
                            template = pattern[index];
                        }
                    }
                } else {
                    while (template.isOptional()) {
                        template = pattern[index];
                        if (item.type == template.getType()) {
                            result.add(item);
                            if (!template.isMultiple()) {
                                if (index + 1 < pattern.length) {
                                    index++;
                                    template = pattern[index];
                                }
                            }
                            continue loop;
                        } else {
                            if (index + 1 < pattern.length) {
                                index++;
                                template = pattern[index];
                            }
                        }
                    }
                    throw new IOException("Unexpected message item type: " + item.type.name() + ", expected " + template.getType().name());
                }
            }
            return result;
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        }
    }

    public void write(RAMessage.MessageItem... items) throws RadixSvnException {
        RAMessage message = new RAMessage();
        try {
            message.mergeItems(Arrays.asList(items), connector.getOutputStream());
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        }
    }

    public RAMessage.MessageItem readItem() throws IOException {
        return new RAMessage().readItem(getInputStream());
    }

    private void handshake() throws RadixSvnException {
        try {
            List<RAMessage.MessageItem> greeting = read(true, RAMessage.NUMBER, RAMessage.NUMBER, RAMessage.LIST, RAMessage.LIST);
            long minProtocolVersion = greeting.get(0).getLong();
            long maxProtocolVersion = greeting.get(1).getLong();
            if (minProtocolVersion < 2 || maxProtocolVersion > 2) {
                throw new IOException("Unsupported protocol version: Min=" + minProtocolVersion + ", Max=" + maxProtocolVersion);
            }
            List<RAMessage.MessageItem> caps = greeting.get(3).getList();
            for (RAMessage.MessageItem cap : caps) {
                serverCapabilities.add(cap.getString());
            }
            RAMessage.MessageItem myCaps = RAMessage.MessageItem.newList(
                    RAMessage.MessageItem.newWord(EDIT_PIPELINE),
                    RAMessage.MessageItem.newWord(SVNDIFF1),
                    RAMessage.MessageItem.newWord(ABSENT_ENTRIES),
                    RAMessage.MessageItem.newWord(DEPTH),
                    RAMessage.MessageItem.newWord(MERGE_INFO),
                    RAMessage.MessageItem.newWord(LOG_REVPROPS)
            );            

            write(RAMessage.MessageItem.newNumber(2),
                    myCaps,
                    RAMessage.MessageItem.newString(uri.toString())
            );
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        }
    }

    @Override
    public void authenticate(SvnRepository repository) throws RadixSvnException {
        List<RAMessage.MessageItem> response;

        response = read(true, RAMessage.LIST, RAMessage.STRING);

        try {
            if (!response.isEmpty()) {
                realm = response.get(1).getString();
                List<String> mechs = new LinkedList<>();
                List<RAMessage.MessageItem> data = response.get(0).getList();
                for (RAMessage.MessageItem item : data) {
                    mechs.add(item.getString());
                }
                if (mechs.isEmpty()) {
                    return;
                }

                new SvnSaslAuthenticator(this).authenticate(mechs, realm, repository);

                List<RAMessage.MessageItem> repoInfo = read(true, RAMessage.STRING, RAMessage.STRING, RAMessage.LIST);
                if (repoInfo != null && !repoInfo.isEmpty()) {
                    String uuid = repoInfo.get(0).getString();
                    String rootUrl = repoInfo.get(1).getString();
                    List<RAMessage.MessageItem> caps = repoInfo.get(2).getList();
                    repository.setInfo(uuid, rootUrl);
                }

            } else {
                throw new IOException("Authentication error");
            }
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        }

    }

    public boolean isEncrypted() {
        return false;
    }

    public void setEncrypted(Object obj) {
    }

    public boolean isStale() {
        return connector.isStale();
    }

    public OutputStream getDeltaStream(final String fileToken) {

        return new OutputStream() {
            Object[] myPrefix = new Object[]{"textdelta-chunk", fileToken};

            @Override
            public void write(byte b[], int off, int len) throws IOException {

                byte[] portion = new byte[len];
                System.arraycopy(b, off, portion, 0, len);
                try {
                    SvnRAConnection.this.write(RAMessage.MessageItem.newWord("textdelta-chunk"),
                            RAMessage.MessageItem.newList(
                                    RAMessage.MessageItem.newString(fileToken),
                                    RAMessage.MessageItem.newBytes(portion)
                            ));
                } catch (RadixSvnException ex) {
                    throw new IOException(ex);
                }

            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }

            @Override
            public void write(int b) throws IOException {
                write(new byte[]{(byte) (b & 0xFF)});
            }

            @Override
            public void close() throws IOException {

            }

        };

    }

    public boolean isSvnDiffV1() {
        return false;
    }

    @Override
    public void close() {
        if (connector != null) {
            try {
                connector.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                connector = null;
            }
        }
    }

}
