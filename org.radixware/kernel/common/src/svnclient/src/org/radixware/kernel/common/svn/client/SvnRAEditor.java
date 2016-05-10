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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.radixware.kernel.common.svn.RadixSvnException;

/**
 *
 * @author akrylov
 */
class SvnRAEditor implements SvnEditor, ISvnDeltaConsumer {

    private static class SvnDir {

        final String token;
        String name;

        public SvnDir(long token) {
            this.token = "d" + token;
        }

    }
    private final SvnRAConnection connection;
    private final SvnRepository repository;
    private final Stack<SvnDir> dirStack = new Stack<>();
    private ICloseCallback closeCallback;
    private final Map<String, String> file2TokenMap = new HashMap<>();
    private boolean aborted;
    private int diffWindowCount;
    private int token = 0;

    public interface ICloseCallback {

        void success();

        void failure(Throwable ex);
    }

    SvnRAEditor(SvnRARepository repository, ICloseCallback callback) {
        this.repository = repository;
        this.connection = (SvnRAConnection) repository.getConnection();
        this.closeCallback = callback;
    }

    private SvnDir getTopDir(long revision) throws RadixSvnException {
        if (dirStack.isEmpty()) {
            openRoot(revision);
        }
        return dirStack.peek();
    }

    public void absentDir(String path) throws RadixSvnException {
    }

    public void absentFile(String path) throws RadixSvnException {
    }

    public void targetRevision(long revision) throws RadixSvnException {
    }

    public void changeDirProperty(String name, SvnProperties.Value value)
            throws RadixSvnException {
        SvnDir dir = getTopDir(-1);
        connection.write(RAMessage.MessageItem.newWord("change-dir-prop"),
                RAMessage.MessageItem.newList(
                        RAMessage.MessageItem.newString(dir.token),
                        RAMessage.MessageItem.newString(name),
                        RAMessage.MessageItem.newList(
                                RAMessage.MessageItem.newBytes(value.getBytes()))));
    }

    public void changeFileProperty(String path, String name, SvnProperties.Value value) throws RadixSvnException {
        String fileToken = (String) file2TokenMap.get(path);
        byte[] bytes = value.getBytes();
        connection.write(RAMessage.MessageItem.newWord("change-file-prop"),
                RAMessage.MessageItem.newList(
                        RAMessage.MessageItem.newString(fileToken),
                        RAMessage.MessageItem.newString(name),
                        RAMessage.MessageItem.newList(
                                RAMessage.MessageItem.newBytes(bytes))));

    }

    @Override
    public void openRoot(long revision) throws RadixSvnException {
        SvnDir root = new SvnDir(token++);

        connection.write(RAMessage.MessageItem.newWord("open-root"),
                RAMessage.MessageItem.newList(
                        getRevisionItem(revision),
                        RAMessage.MessageItem.newString(root.token)));
        dirStack.push(root);
    }

    @Override
    public void openDir(String path, long revision) throws RadixSvnException {
        if (path == null) {
            path = "";
        }
        SvnDir topDir = getTopDir(revision);
        SvnDir curDir = new SvnDir(token++);
        curDir.name = path;
        connection.write(RAMessage.MessageItem.newWord("open-dir"),
                RAMessage.MessageItem.newList(
                        RAMessage.MessageItem.newString(path),
                        RAMessage.MessageItem.newString(topDir.token),
                        RAMessage.MessageItem.newString(curDir.token),
                        getRevisionItem(revision)));
        dirStack.push(curDir);
    }

    private RAMessage.MessageItem getRevisionItem(long revision) {
        if (revision < 0) {
            return RAMessage.MessageItem.emptyList();
        } else {
            return RAMessage.MessageItem.newNumber(revision);
        }
    }

    @Override
    public void addDir(String path, String copyFromPath, long copyFromRevision) throws RadixSvnException {
        SvnDir topDir = getTopDir(copyFromRevision);
        SvnDir newDir = new SvnDir(token++);
        newDir.name = path;
        if (copyFromPath != null) {
            //System.out.println("Copy Dir: " + path + " from " + copyFromPath + ", revision " + copyFromRevision);
        } else {
            //System.out.println("Add Dir: " + path);
        }

        if (copyFromPath != null) {

            String rootURL = repository.getRootUrl();
            copyFromPath = SvnPath.append(SvnPath.append(rootURL, repository.getPath()), copyFromPath);
            connection.write(RAMessage.MessageItem.newWord("add-dir"),
                    RAMessage.MessageItem.newList(
                            RAMessage.MessageItem.newString(path),
                            RAMessage.MessageItem.newString(topDir.token),
                            RAMessage.MessageItem.newString(newDir.token),
                            RAMessage.MessageItem.newList(
                                    RAMessage.MessageItem.newString(copyFromPath),
                                    getRevisionItem(copyFromRevision))));
        } else {
            connection.write(RAMessage.MessageItem.newWord("add-dir"),
                    RAMessage.MessageItem.newList(
                            RAMessage.MessageItem.newString(path),
                            RAMessage.MessageItem.newString(topDir.token),
                            RAMessage.MessageItem.newString(newDir.token),
                            RAMessage.MessageItem.emptyList()));
        }
        dirStack.push(newDir);
    }

    @Override
    public void closeDir() throws RadixSvnException {
        SvnDir curDir = dirStack.pop();
        //System.out.println("Close Dir: " + curDir.name);

        connection.write(RAMessage.MessageItem.newWord("close-dir"),
                RAMessage.MessageItem.newList(
                        RAMessage.MessageItem.newString(curDir.token)));
    }

    @Override
    public void deleteEntry(String path, long revision) throws RadixSvnException {
        //System.out.println("Delete " + path + " at revision " + revision);
        SvnDir dir = getTopDir(revision);
        connection.write(RAMessage.MessageItem.newWord("delete-entry"),
                RAMessage.MessageItem.newList(
                        RAMessage.MessageItem.newString(path),
                        getRevisionItem(revision),
                        RAMessage.MessageItem.newString(dir.token)));
    }

    @Override
    public void abortEdit() throws RadixSvnException {
        if (aborted || closeCallback == null) {
            return;
        }
        aborted = true;
        Exception error = null;
        try {
            connection.write(RAMessage.MessageItem.newWord("abort-edit"),
                    RAMessage.MessageItem.emptyList());
            connection.read(true);
        } catch (RadixSvnException e) {
            error = e;
            throw e;
        } finally {
            if (error != null) {
                closeCallback.failure(error);
            } else {
                closeCallback.success();
            }
            closeCallback = null;
        }
    }

    @Override
    public SvnCommitSummary closeEdit() throws RadixSvnException {
        Exception error = null;
        try {
            connection.write(RAMessage.MessageItem.newWord("close-edit"), RAMessage.MessageItem.emptyList());
            connection.read(true);
            repository.authenticate();

            final List<RAMessage.MessageItem> result = connection.read(true);

            long revision = result.get(0).getLong();
            List<RAMessage.MessageItem> dateList = result.get(1).getList();
            List<RAMessage.MessageItem> authorList = result.get(2).getList();
            List<RAMessage.MessageItem> errorList = result.get(3).getList();
            Date date = null;
            String authror = null;
            String errorStr = null;
            if (!dateList.isEmpty()) {
                date = dateList.get(0).getDate();
            }
            if (!authorList.isEmpty()) {
                authror = authorList.get(0).getString();
            }
            if (!errorList.isEmpty()) {
                errorStr = errorList.get(0).getString();
            }
            return new SvnCommitSummary(revision, authror, date, errorStr);

        } catch (Exception exception) {
            error = exception;
            try {
                connection.write(RAMessage.MessageItem.newWord("abort-edit"));
            } catch (RadixSvnException e1) {
                //ignore
            }
            throw exception;
        } finally {
            if (error != null) {
                closeCallback.failure(error);
            } else {
                closeCallback.success();
            }
            closeCallback = null;
        }
    }

    @Override
    public void addFile(String path, String copyFromPath, long copyFromRevision) throws RadixSvnException {
        SvnDir dir = getTopDir(copyFromRevision);
        String fileToken = "c" + token++;

        if (copyFromPath != null) {
            String rootURL = repository.getRootUrl();
            copyFromPath = SvnPath.append(rootURL, copyFromPath);

            connection.write(RAMessage.MessageItem.newWord("add-file"),
                    RAMessage.MessageItem.newList(
                            RAMessage.MessageItem.newString(path),
                            RAMessage.MessageItem.newString(dir.token),
                            RAMessage.MessageItem.newString(fileToken),
                            RAMessage.MessageItem.newList(
                                    RAMessage.MessageItem.newString(copyFromPath),
                                    getRevisionItem(copyFromRevision))));
        } else {
            connection.write(RAMessage.MessageItem.newWord("add-file"),
                    RAMessage.MessageItem.newList(
                            RAMessage.MessageItem.newString(path),
                            RAMessage.MessageItem.newString(dir.token),
                            RAMessage.MessageItem.newString(fileToken),
                            RAMessage.MessageItem.emptyList()));
        }
        file2TokenMap.put(path, fileToken);
    }

    @Override
    public void openFile(String path, long revision) throws RadixSvnException {
        SvnDir dir = getTopDir(revision);;
        String fileToken = "c" + token++;
        connection.write(RAMessage.MessageItem.newWord("open-file"),
                RAMessage.MessageItem.newList(
                        RAMessage.MessageItem.newString(path),
                        RAMessage.MessageItem.newString(dir.token),
                        RAMessage.MessageItem.newString(fileToken),
                        getRevisionItem(revision)));

        file2TokenMap.put(path, fileToken);
    }

    private static final OutputStream DUMMY_STREAM = new OutputStream() {

        @Override
        public void write(int b) throws IOException {
            //ignore
        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public void flush() throws IOException {

        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {

        }

        @Override
        public void write(byte[] b) throws IOException {

        }

    };

    @Override
    public void applyTextDelta(String path, String baseChecksum) throws RadixSvnException {
        String fileToken = (String) file2TokenMap.get(path);
        diffWindowCount = 0;
        connection.write(RAMessage.MessageItem.newWord("apply-textdelta"),
                RAMessage.MessageItem.newList(
                        RAMessage.MessageItem.newString(fileToken),
                        baseChecksum == null ? RAMessage.MessageItem.emptyList()
                                : RAMessage.MessageItem.newList(
                                        RAMessage.MessageItem.newString(baseChecksum))));
    }

    @Override
    public OutputStream textDeltaChunk(String path, SvnDiffWindow diffWindow) throws RadixSvnException {
        try {
            String fileToken = (String) file2TokenMap.get(path);
            diffWindow.writeTo(connection.getDeltaStream(fileToken), diffWindowCount == 0, connection.isSvnDiffV1());
            diffWindowCount++;
            return DUMMY_STREAM;
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        }
    }

    @Override
    public void textDeltaEnd(String path) throws RadixSvnException {
        String fileToken = (String) file2TokenMap.get(path);
        diffWindowCount = 0;
        connection.write(RAMessage.MessageItem.newWord("textdelta-end"),
                RAMessage.MessageItem.newList(
                        RAMessage.MessageItem.newString(fileToken)));
    }

    @Override
    public void closeFile(String path, String textChecksum) throws RadixSvnException {
        String fileToken = (String) file2TokenMap.remove(path);
        diffWindowCount = 0;
        connection.write(RAMessage.MessageItem.newWord("close-file"),
                RAMessage.MessageItem.newList(
                        RAMessage.MessageItem.newString(fileToken), textChecksum == null
                                ? RAMessage.MessageItem.emptyList() : RAMessage.MessageItem.newList(
                                        RAMessage.MessageItem.newString(textChecksum))));
    }

    @Override
    public void appendFile(String path, long revision, InputStream data) throws RadixSvnException {
        //System.out.println("Append " + path);
        addFile(path, null, revision);
        applyTextDelta(path, null);
        String chk_sum = new SvnDeltaGenerator().sendDelta(path, data, this, true);
        closeFile(path, chk_sum);

    }

    @Override
    public void appendFile(String path, long revision, String content) throws RadixSvnException {
        //System.out.println("Append " + path);
        addFile(path, null, revision);
        applyTextDelta(path, null);
        String chk_sum = new SvnDeltaGenerator().sendDelta(path, new ByteArrayInputStream(content.getBytes()), this, true);
        closeFile(path, chk_sum);

    }

    @Override
    public void updateFile(String path, long revision, String content) throws RadixSvnException {
        //System.out.println("Update " + path + " at revision " + revision);
        openFile(path, revision);
        applyTextDelta(path, null);
        String chk_sum = new SvnDeltaGenerator().sendDelta(path, new ByteArrayInputStream(content.getBytes()), this, true);
        closeFile(path, chk_sum);

    }

    @Override
    public void updateFile(String path, long revision, InputStream content) throws RadixSvnException {
        //System.out.println("Update " + path + " at revision " + revision);
        openFile(path, revision);
        applyTextDelta(path, null);
        String chk_sum = new SvnDeltaGenerator().sendDelta(path, content, this, true);
        closeFile(path, chk_sum);

    }

}
