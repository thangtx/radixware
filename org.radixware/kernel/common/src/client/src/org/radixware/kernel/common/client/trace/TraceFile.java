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

package org.radixware.kernel.common.client.trace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.FileException;

public final class TraceFile {

    private final OutputStreamWriter out;
    private final String traceFileName;
    private final IClientEnvironment environment;

    public TraceFile(final IClientEnvironment environment, final String encoding, final boolean append) throws FileException {
        traceFileName = environment.getTraceFile();
        this.environment = environment;
        File traceFile = new File(traceFileName);
        try {
            traceFile.createNewFile();
        } catch (IOException e) {
            throw new FileException(environment, FileException.EExceptionCode.CANT_CREATE, traceFileName, e);
        }
        if (!traceFile.isFile()) {
            throw new FileException(environment, FileException.EExceptionCode.NOT_FILE, traceFileName);
        }
        if (!traceFile.canWrite()) {
            throw new FileException(environment, FileException.EExceptionCode.CANT_WRITE, traceFileName);
        }
        try {
            if (encoding != null) {
                out = new OutputStreamWriter(new FileOutputStream(traceFile, append), encoding);
            } else {
                out = new OutputStreamWriter(new FileOutputStream(traceFile, append));
            }
        } catch (Exception e) {
            throw new FileException(environment, FileException.EExceptionCode.CANT_CREATE, traceFileName, e);
        }
    }

    public void put(IClientTraceItem item) throws FileException {
        try {
            item.toWriter(out);
            //out.write(item.getFormattedMessage());
            out.write("\n");
            out.flush();
        } catch (IOException e) {
            throw new FileException(environment, FileException.EExceptionCode.CANT_WRITE, traceFileName, e);
        }
    }

    public void close() throws IOException {
        out.flush();
        out.close();
    }
}
