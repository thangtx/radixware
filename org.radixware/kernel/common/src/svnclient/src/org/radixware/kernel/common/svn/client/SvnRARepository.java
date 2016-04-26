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
import java.io.OutputStream;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.client.impl.SvnPlainConnector;
import org.radixware.kernel.common.svn.client.impl.SvnSshConnector;

/**
 *
 * @author akrylov
 */
public class SvnRARepository extends SvnRepository {

    private Object lockMarker;

    public SvnRARepository(URI location, String path, SvnCredentials[] credentials) {
        super(location, path, credentials, null);
    }

    private void lock() throws RadixSvnException {
        synchronized (this) {
            if (lockMarker == null) {
                lockMarker = Thread.currentThread();
            } else {
                while (lockMarker != null) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        throw new RadixSvnException("Executor thread was interrupted");
                    }
                }
                lockMarker = Thread.currentThread();
            }
        }
    }

    private void unlock() {
        synchronized (this) {
            if (lockMarker == Thread.currentThread()) {
                lockMarker = null;
                notify();
            }
        }
    }

    @Override
    public String getExternalUserName() {
        return getCredentials().getUserName();
    }

    @Override
    protected SvnConnection createConnection() {
        SvnConnector connector = createConnector();
        return new SvnRAConnection(connector);
    }

    @Override
    public SvnRAConnection getConnection() {
        return (SvnRAConnection) super.getConnection();
    }

    @Override
    protected SvnConnector createConnector() {
        switch (getCredentials().getAuthType()) {
            case SSH_KEY_FILE:
            case SSH_PASSWORD:
                return new SvnSshConnector();
            case NONE:
            case SSL:
            case SVN_PASSWORD:
                return new SvnPlainConnector();
        }
        return null;
    }

    @Override
    protected void disconnect(boolean force) {
        super.disconnect(force);
        unlock();
    }

    @Override
    protected void disconnect() {
        disconnect(false);

    }

    @Override
    public void connect() throws RadixSvnException {
        lock();
        super.connect();
    }

    @Override
    public SvnEntry getDir(String path, long revision, boolean wantProps, boolean wantContents, boolean wantInheritedProps, SvnEntryHandler handler) throws RadixSvnException {
        try {
            connect();
            path = getPathRelativeToMine(path);
            String repositoryRoot = getRootUrl();
            if (handler != null) {
                List<RAMessage.MessageItem> params = new LinkedList<>();
                params.add(RAMessage.MessageItem.newString(path));

                List<RAMessage.MessageItem> individualProps = new LinkedList();

                individualProps.add(RAMessage.MessageItem.newWord(SvnEntry.DIRENT_KIND));

                individualProps.add(RAMessage.MessageItem.newWord(SvnEntry.DIRENT_SIZE));

                individualProps.add(RAMessage.MessageItem.newWord(SvnEntry.DIRENT_HAS_PROPS));

                individualProps.add(RAMessage.MessageItem.newWord(SvnEntry.DIRENT_CREATED_REV));

                individualProps.add(RAMessage.MessageItem.newWord(SvnEntry.DIRENT_TIME));

                individualProps.add(RAMessage.MessageItem.newWord(SvnEntry.DIRENT_LAST_AUTHOR));

                params.add(getRevisionItem(revision));
                params.add(RAMessage.MessageItem.newBoolean(true));
                params.add(RAMessage.MessageItem.newBoolean(true));
                params.add(RAMessage.MessageItem.newList(individualProps));

                getConnection().write(RAMessage.MessageItem.newWord("get-dir"), RAMessage.MessageItem.newList(params));

                authenticate();
                List<RAMessage.MessageItem> items = getConnection().read(true, RAMessage.NUMBER, RAMessage.LIST, RAMessage.LIST, RAMessage.LIST_OPT);
                long revNum = items.get(0).getLong();

                List<RAMessage.MessageItem> entries = items.get(2).getList();
                for (RAMessage.MessageItem item : entries) {
                    List<RAMessage.MessageItem> entry = item.getList();
                    SvnEntry e = extractEntry(path, repositoryRoot, entry, 0);
                    if (e != null) {
                        handler.accept(e);
                    }
                }
            }

            getConnection().write(RAMessage.MessageItem.newWord("stat"),
                    RAMessage.MessageItem.newList(
                            RAMessage.MessageItem.newString(path),
                            getRevisionItem(revision)));
            authenticate();
            List<RAMessage.MessageItem> items = getConnection().read(true, RAMessage.LIST);
            if (items.isEmpty()) {
                return null;
            }
            items = items.get(0).getList();
            if (items != null && !items.isEmpty()) {
                return extractEntry(path, repositoryRoot, items.get(0).getList(), 1);
            } else {
                return null;
            }
        } catch (RadixSvnException ex) {
            throw ex;
        } finally {
            disconnect();
        }
    }

    private SvnEntry extractEntry(String path, String repositoryRoot, List<RAMessage.MessageItem> entry, int shift) {
        String name;
        if (shift > 0) {
            name = SvnPath.tail(path);
        } else {
            name = entry.get(0 - shift).getString();
        }
        SvnEntry.Kind kind = SvnEntry.Kind.fromString(entry.get(1 - shift).getString());
        long size = entry.get(2 - shift).getLong();
        boolean hasProps = entry.get(3 - shift).getBoolean();
        long lastRevision = entry.get(4 - shift).getLong();
        Date lastModified = new Date();
        String author = "";
        if (entry.size() > 5 - shift) {
            lastModified = entry.get(5 - shift).getList().get(0).getDate();
            if (entry.size() > 6 - shift) {
                List<RAMessage.MessageItem> authorList = entry.get(6 - shift).getList();
                if (authorList.isEmpty()) {
                    //TODO: get author in another way
                } else {
                    author = authorList.get(0).getString();
                }
            }
        }
        String repositoryPath;
        if (shift > 0) {
            repositoryPath = SvnPath.append(repositoryRoot, path);
        } else {
            repositoryPath = SvnPath.append(SvnPath.append(repositoryRoot, path), name);
        }

        return new SvnEntry(path, name, repositoryPath, author, lastRevision, lastModified, size, kind, null);

    }

    @Override
    public long getLatestRevision() throws RadixSvnException {
        try {
            connect();
            getConnection().write(RAMessage.MessageItem.newWord("get-latest-rev"), RAMessage.MessageItem.emptyList());
            authenticate();
            List<RAMessage.MessageItem> items = getConnection().read(true, RAMessage.NUMBER);
            return items.get(0).getLong();

        } catch (RadixSvnException e) {
            throw e;
        } finally {
            disconnect();
        }
    }

    @Override
    public long getFile(String path, long revision, Map<String, String> props, OutputStream contents) throws RadixSvnException {
        boolean forceDisconnect = false;
        try {
            connect();

            path = getPathRelativeToMine(path);
            getConnection().write(RAMessage.MessageItem.newWord("get-file"),
                    RAMessage.MessageItem.newList(
                            RAMessage.MessageItem.newString(path),
                            getRevisionItem(revision),
                            RAMessage.MessageItem.newBoolean(props != null),
                            RAMessage.MessageItem.newBoolean(contents != null)));

            authenticate();
            List<RAMessage.MessageItem> values = getConnection().read(true, RAMessage.LIST, RAMessage.NUMBER, RAMessage.LIST);

            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RadixSvnException(e);
            }

            String svnDigest = values.get(0).getList().get(0).getString();
            try {
                if (contents != null) {
                    while (true) {
                        try {
                            RAMessage.MessageItem item = getConnection().readItem();
                            if (item == null || item.type != RAMessage.MessageItemType.BYTES) {
                                throw new RadixSvnException("File contexts expected");
                            }
                            byte[] bytes = item.getByteArray();
                            if (bytes.length == 0) {
                                break;
                            }
                            if (svnDigest != null) {
                                digest.update(bytes);
                            }

                            try {
                                contents.write(bytes);
                            } catch (IOException e) {
                                throw new RadixSvnException("File content write error", e);
                            }
                        } catch (IOException ex) {
                            throw new RadixSvnException("File content read error", ex);
                        }

                    }
                    getConnection().read(false);

                    if (svnDigest != null) {
                        String resultDigest = SvnUtil.toHexDigest(digest);
                        if (!svnDigest.equals(resultDigest)) {
                            throw new RadixSvnException("Checksum mismatch for svn entry " + path + ": expeted " + svnDigest + " but was: " + resultDigest);
                        }
                    }
                }
            } catch (Throwable e) {
                forceDisconnect = true;
                throw e;
            }
            long fileRevision = values.get(1).getLong();
            if (props != null && values.size() > 2) {
                RAMessage.MessageItem item = values.get(2);
                List<RAMessage.MessageItem> propsList = item.getList();
                if (propsList != null) {
                    for (RAMessage.MessageItem prop : propsList) {
                        List<RAMessage.MessageItem> propsData = prop.getList();
                        if (propsData != null && propsData.size() == 2) {
                            String propName = propsData.get(0).getString();
                            String propValue = propsData.get(1).getString();
                            if (propName != null && propValue != null) {
                                props.put(propName, propValue);
                            }
                        }
                    }
                }
                props.put("svn:entry:revision", String.valueOf(fileRevision));
                props.put("svn:entry:checksum", svnDigest);
            }
            return fileRevision;
        } catch (RadixSvnException e) {
            throw e;
        } finally {
            disconnect(forceDisconnect);
        }
    }

    @Override
    public SvnEntry info(String path, long revision) throws RadixSvnException {
        try {
            connect();
            path = getPathRelativeToMine(path);
            getConnection().write(RAMessage.MessageItem.newWord("stat"),
                    RAMessage.MessageItem.newList(
                            RAMessage.MessageItem.newString(path),
                            getRevisionItem(revision)));

            authenticate();
            List<RAMessage.MessageItem> values = getConnection().read(true, RAMessage.LIST);

            if (values == null || values.isEmpty() || values.get(0).type != RAMessage.MessageItemType.LIST) {
                return null;
            }
            List<RAMessage.MessageItem> list = values.get(0).getList();
            if (list != null && !list.isEmpty() && values.get(0).type == RAMessage.MessageItemType.LIST) {
                list = list.get(0).getList();
                return extractEntry(path, getRootUrl(), list, 1);
            }
            return null;
        } finally {
            disconnect();
        }
    }

    @Override
    public SvnEntry.Kind checkPath(String path, long revision) throws RadixSvnException {
        try {
            connect();
            path = getPathRelativeToMine(path);
            getConnection().write(RAMessage.MessageItem.newWord("check-path"),
                    RAMessage.MessageItem.newList(
                            RAMessage.MessageItem.newString(path),
                            getRevisionItem(revision)));

            authenticate();
            List<RAMessage.MessageItem> values = getConnection().read(true, RAMessage.WORD);
            if (values.isEmpty()) {
                throw new RadixSvnException("Malformed data received");
            } else {
                return SvnEntry.Kind.fromString(values.get(0).getString());
            }
        } catch (RadixSvnException ex) {
            throw new RadixSvnException("Unable to check kind of path '" + path + "'", ex);
        } finally {
            disconnect();
        }
    }

    @Override
    public void replay(long lowRevision, long highRevision, boolean sendDeltas, SvnEditor editor) throws RadixSvnException {
        throw new UnsupportedOperationException("Replay operation is not supported");
    }

    private RAMessage.MessageItem getRevisionItem(long revision) {
        if (revision < 0) {
            return RAMessage.MessageItem.emptyList();
        } else {
            return RAMessage.MessageItem.newList(RAMessage.MessageItem.newNumber(revision));
        }
    }

    @Override
    public void setRevisionPropertyValue(long revision, String propertyName, SvnProperties.Value value) throws RadixSvnException {
        if (revision <= 0) {
            throw new RadixSvnException("Incorrect revision number: " + revision);
        }
        byte[] bytes = value.getBytes();
        try {
            connect();
            getConnection().write(RAMessage.MessageItem.newWord("change-rev-prop"),
                    RAMessage.MessageItem.newList(
                            getRevisionItem(revision)),
                    RAMessage.MessageItem.newString(propertyName),
                    RAMessage.MessageItem.newBytes(bytes));

            authenticate();
            getConnection().read(false);
        } catch (RadixSvnException e) {
            throw e;
        } finally {
            disconnect();
        }
    }

    @Override
    public SvnProperties getRevisionProperties(long revision, SvnProperties properties) throws RadixSvnException {
        if (revision <= 0) {
            throw new RadixSvnException("Incorrect revision number: " + revision);
        }
        if (properties == null) {
            properties = new SvnProperties();
        }

        try {
            connect();
            getConnection().write(RAMessage.MessageItem.newWord("rev-proplist"),
                    RAMessage.MessageItem.newList(
                            getRevisionItem(revision)));

            authenticate();
            List<RAMessage.MessageItem> list = getConnection().read(true, RAMessage.LIST);
            //TODO: parse touple
            return properties;
        } catch (RadixSvnException e) {
            throw e;
        } finally {
            disconnect();
        }
    }

    @Override
    public SvnEditor getEditor(String logMessage) throws RadixSvnException {
        try {
            connect();
            getConnection().write(RAMessage.MessageItem.newWord("commit"),
                    logMessage == null
                            ? RAMessage.MessageItem.emptyList()
                            : RAMessage.MessageItem.newList(RAMessage.MessageItem.newString(logMessage)));

            authenticate();
            getConnection().read(false);
            return new SvnRAEditor(this, new SvnRAEditor.ICloseCallback() {

                @Override
                public void success() {
                    disconnect();
                }

                @Override
                public void failure(Throwable ex) {
                    disconnect();
                    close();
                }

            });
        } catch (RadixSvnException e) {
            disconnect();
            close();
            throw e;
        }
    }

    private String[] getRepositoryPaths(String[] paths) throws RadixSvnException {
        if (paths == null || paths.length == 0) {
            return paths;
        }
        String root = getRootUrl();
        String[] fullPaths = new String[paths.length];
        for (int i = 0; i < paths.length; i++) {
            fullPaths[i] = SvnPath.append(root, paths[i]);
        }
        return fullPaths;
    }

    private long logImpl(String[] pathes, long startRevision, long endRevision,
            boolean changedPaths, boolean strictNode, long limit,
            boolean includeMergedRevisions, String[] revisionPropertyNames,
            ISvnLogHandler handler) throws RadixSvnException {

        long count = 0;
        int nestLevel = 0;

        long latestRev = -1;
        if (startRevision <= 0) {
            startRevision = latestRev = getLatestRevision();
        }
        if (endRevision <= 0) {
            endRevision = latestRev != -1 ? latestRev : getLatestRevision();
        }
        boolean forceDisconnect = false;
        try {
            connect();
            String[] targetPaths = new String[pathes.length];
            for (int i = 0; i < targetPaths.length; i++) {
                targetPaths[i] = getPathRelativeToMine(pathes[i]);
            }
            Object[] buffer;
            boolean wantCustomRevProps = false;
            if (revisionPropertyNames != null && revisionPropertyNames.length > 0) {

                getConnection().write(RAMessage.MessageItem.newWord("log"),
                        RAMessage.MessageItem.newList(
                                RAMessage.MessageItem.newList(targetPaths),
                                RAMessage.MessageItem.newList(RAMessage.MessageItem.newNumber(startRevision)),
                                RAMessage.MessageItem.newList(RAMessage.MessageItem.newNumber(endRevision)),
                                RAMessage.MessageItem.newBoolean(changedPaths),
                                RAMessage.MessageItem.newBoolean(strictNode),
                                RAMessage.MessageItem.newNumber(limit > 0 ? limit : 0),
                                RAMessage.MessageItem.newBoolean(includeMergedRevisions),
                                RAMessage.MessageItem.newWord("revprops"),
                                RAMessage.MessageItem.newList(revisionPropertyNames)));
                for (int i = 0; i < revisionPropertyNames.length; i++) {
                    String propName = revisionPropertyNames[i];
                    if (!"svn:author".equals(propName)
                            && !"svn:date".equals(propName)
                            && !"svn:log".equals(propName)) {
                        wantCustomRevProps = true;
                        break;
                    }
                }
            } else {
                getConnection().write(RAMessage.MessageItem.newWord("log"),
                        RAMessage.MessageItem.newList(
                                RAMessage.MessageItem.newList(targetPaths),
                                RAMessage.MessageItem.newList(RAMessage.MessageItem.newNumber(startRevision)),
                                RAMessage.MessageItem.newList(RAMessage.MessageItem.newNumber(endRevision)),
                                RAMessage.MessageItem.newBoolean(changedPaths),
                                RAMessage.MessageItem.newBoolean(strictNode),
                                RAMessage.MessageItem.newNumber(limit > 0 ? limit : 0),
                                RAMessage.MessageItem.newBoolean(includeMergedRevisions),
                                RAMessage.MessageItem.newWord("all-revprops"),
                                RAMessage.MessageItem.newList(new String[]{})));
            }
            authenticate();

            while (true) {
                List<RAMessage.MessageItem> items = getConnection().read(false);
                if (items.isEmpty()) {
                    break;
                }
                if (items.size() == 1 && "done".equals(items.get(0).getString())) {
                    break;
                }
                long revision = items.get(1).getLong();
                List<RAMessage.MessageItem> list = items.get(2).getList();
                String author = "";
                if (!list.isEmpty()) {
                    author = list.get(0).getString();
                }

                Date date = new Date();

                list = items.get(3).getList();
                if (!list.isEmpty()) {
                    date = list.get(0).getDate();
                }
                list = items.get(4).getList();
                String message = "";
                if (!list.isEmpty()) {
                    message = list.get(0).getString();
                }
                List<RAMessage.MessageItem> changedPathes = items.get(0).getList();
                final Set<String> cpths = new HashSet<>();
                for (RAMessage.MessageItem item : changedPathes) {
                    cpths.add(item.getList().get(0).getString());
                }
                handler.accept(new SvnLogEntry(message, author, date, revision, null));

            }

            if (nestLevel == 0) {
                count++;
            }
            getConnection().read(false);
            return count;
        } catch (RadixSvnException e) {
            throw e;
        } catch (Throwable ex) {
            forceDisconnect = true;
            throw ex;
        } finally {
            disconnect(forceDisconnect);
        }
    }

    @Override
    public void log(String path, long startRevision, long endRevision, boolean changedPaths, ISvnLogHandler logHandler) throws RadixSvnException {
        log(new String[]{path}, startRevision, endRevision, changedPaths, logHandler);
    }

    @Override
    public void log(String[] pathes, long startRevision, long endRevision, boolean changedPaths, ISvnLogHandler logHandler) throws RadixSvnException {
        logImpl(pathes, startRevision, endRevision, changedPaths, true, 0, false, null, logHandler);
    }

    @Override
    public void log(String path, long startRevision, long endRevision, boolean changedPaths, boolean strictNode, ISvnLogHandler logHandler) throws RadixSvnException {
        log(new String[]{path}, startRevision, endRevision, changedPaths, strictNode, logHandler);
    }

    @Override
    public void log(String[] pathes, long startRevision, long endRevision, boolean changedPaths, boolean strictNode, ISvnLogHandler logHandler) throws RadixSvnException {
        logImpl(pathes, startRevision, endRevision, changedPaths, strictNode, 0, false, null, logHandler);
    }

    @Override
    public void close() {
        super.close();
    }

}
