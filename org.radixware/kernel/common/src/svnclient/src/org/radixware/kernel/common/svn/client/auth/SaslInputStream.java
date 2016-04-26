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
package org.radixware.kernel.common.svn.client.auth;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.security.sasl.SaslClient;

/**
 * @version 1.3
 * @author TMate Software Ltd.
 */
public class SaslInputStream extends InputStream {

    private InputStream wrappedStream;
    private SaslClient saslClient;
    private byte[] buffer;

    private ByteBuffer data;

    public SaslInputStream(SaslClient client, int bufferSize, InputStream in) {
        saslClient = client;
        wrappedStream = in;
        buffer = new byte[bufferSize * 2];
    }

    @Override
    public void close() throws IOException {
        wrappedStream.close();
    }

    @Override
    public int read() throws IOException {
        byte[] b = new byte[1];
        int r = read(b, 0, 1);
        if (r != 1) {
            return -1;
        }
        return b[0];
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = 0;
        while (true) {
            if (data == null) {
                ensureSomeDataAvailable();
            }
            int amountOfBytesToReadFrom = Math.min(len, data.remaining());
            data.get(b, off, amountOfBytesToReadFrom);
            len -= amountOfBytesToReadFrom;
            off += amountOfBytesToReadFrom;
            read += amountOfBytesToReadFrom;
            if (len == 0 || data.remaining() == 0) {
                if (data.remaining() == 0) {
                    data = null;
                }
                return read;
            }
        }
    }

    @Override
    public long skip(long n) throws IOException {
        return 0;
    }

    private void ensureSomeDataAvailable() throws IOException {
        DataInputStream dis = new DataInputStream(wrappedStream);
        int encodedLength = dis.readInt();
        if (buffer.length < encodedLength) {
            buffer = new byte[(encodedLength * 3) / 2];
        }
        dis.readFully(buffer, 0, encodedLength);
        data = ByteBuffer.wrap(saslClient.unwrap(buffer, 0, encodedLength));
    }
}
