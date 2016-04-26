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
import org.radixware.kernel.common.svn.RadixSvnException;

/**
 *
 * @author akrylov
 */
public interface SvnEditor {

    public void openRoot(long revision) throws RadixSvnException;

    public void openDir(String path, long revision) throws RadixSvnException;

    public void addDir(String path, String copyFromPath, long copyFromRevision) throws RadixSvnException;

    public void closeDir() throws RadixSvnException;

    public void deleteEntry(String path, long revision) throws RadixSvnException;

    public void abortEdit() throws RadixSvnException;

    public SvnCommitSummary closeEdit() throws RadixSvnException;

    public void appendFile(String path, long revision, InputStream data) throws RadixSvnException;

    public void addFile(String path, String copyFromPath, long copyFromRevision) throws RadixSvnException;

    public void appendFile(String path, long revision, String content) throws RadixSvnException;

    public void updateFile(String path, long revision, String content) throws RadixSvnException;

    public void updateFile(String path, long revision, InputStream content) throws RadixSvnException;

    public void openFile(String path, long revision) throws RadixSvnException;

    public void closeFile(String path, String textChecksum) throws RadixSvnException;

    static final OutputStream DUMMY_STREAM = new OutputStream() {

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
}
