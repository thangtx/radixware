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
import java.io.Writer;
import org.radixware.kernel.server.jdbc.DBOperationLoggerInterface;

public class RadixLobWriter extends Writer {

    private final Writer delegate;
    private final DBOperationLoggerInterface logger;
    private final String operStr;

    public RadixLobWriter(Writer delegate, DBOperationLoggerInterface logger, String lobType) {
        this.delegate = delegate;
        this.logger = logger;
        this.operStr = lobType == null ? "Writer" : (lobType + ":Writer");
    }

    private void beforeDbOper() {
        logger.beforeDbOperation(operStr);
    }

    private void afterDbOper() {
        logger.afterDbOperation();
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        beforeDbOper();
        try {
            delegate.write(cbuf, off, len);
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

    @Override
    public void write(int c) throws IOException {
        beforeDbOper();
        try {
            delegate.write(c);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public void write(char[] cbuf) throws IOException {
        beforeDbOper();
        try {
            delegate.write(cbuf);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public void write(String str) throws IOException {
        beforeDbOper();
        try {
            delegate.write(str);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        beforeDbOper();
        try {
            delegate.write(str, off, len);
        } finally {
            afterDbOper();
        }
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        beforeDbOper();
        try {
            delegate.append(csq);
            return this;
        } finally {
            afterDbOper();
        }
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        beforeDbOper();
        try {
            delegate.append(csq, start, end);
            return this;
        } finally {
            afterDbOper();
        }
    }

    @Override
    public Writer append(char c) throws IOException {
        beforeDbOper();
        try {
            delegate.append(c);
            return this;
        } finally {
            afterDbOper();
        }
    }

}
