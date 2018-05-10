/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.jdbc.wrappers;

import java.io.IOException;
import java.io.OutputStream;
import org.radixware.kernel.server.jdbc.DBOperationLoggerInterface;

public class RadixLobOutputStream extends OutputStream {

    private final OutputStream delegate;
    private final DBOperationLoggerInterface logger;
    private final String operStr;

    public RadixLobOutputStream(OutputStream delegate, DBOperationLoggerInterface logger, String lobType) {
        this.delegate = delegate;
        this.logger = logger;
        this.operStr = lobType == null ? "OutputStream" : (lobType + ":OutputStream");
    }

    private void beforeDbOper() {
        logger.beforeDbOperation(operStr);
    }

    private void afterDbOper() {
        logger.afterDbOperation();
    }

    @Override
    public void write(int b) throws IOException {
        beforeDbOper();
        try {
            delegate.write(b);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        beforeDbOper();
        try {
            delegate.write(b);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        beforeDbOper();
        try {
            delegate.write(b, off, len);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public void flush() throws IOException {
        beforeDbOper();
        try {
            delegate.flush();
        } finally {
            afterDbOper();
        }

    }

    @Override
    public void close() throws IOException {
        beforeDbOper();
        try {
            delegate.close();
        } finally {
            afterDbOper();
        }
    }

}
