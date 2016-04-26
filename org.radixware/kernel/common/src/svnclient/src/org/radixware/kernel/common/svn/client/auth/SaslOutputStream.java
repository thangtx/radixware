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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import javax.security.sasl.SaslClient;

/**
 * @version 1.3
 * @author TMate Software Ltd.
 */
public class SaslOutputStream extends OutputStream {

    private OutputStream wrappedStream;
    private SaslClient saslClient;
    private final byte[] encodedLen = new byte[4];
    private ByteBuffer data;

    public SaslOutputStream(SaslClient client, int bufferSize, OutputStream out) {
        wrappedStream = out;
        saslClient = client;
        data = ByteBuffer.allocate(bufferSize);
    }

    @Override
    public void write(int b) throws IOException {
        write(new byte[]{(byte) (b & 0xFF)});
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        // write to buffer, then flush if necessary.
        while (len > 0) {
            int toPut = Math.min(data.remaining(), len);
            data.put(b, off, toPut);
            off += toPut;
            len -= toPut;
            if (data.remaining() == 0) {
                flush();
            }
        }
    }

    @Override
    public void close() throws IOException {
        flush();
        wrappedStream.close();
    }

    @Override
    public void flush() throws IOException {
        byte[] bytes = data.array();
        int off = data.arrayOffset();
        int len = data.position();
        if (len == 0) {
            return;
        }
        byte[] encoded = saslClient.wrap(bytes, off, len);
        encodedLen[0] = (byte) ((encoded.length & 0xFF000000) >> 24);
        encodedLen[1] = (byte) ((encoded.length & 0x00FF0000) >> 16);
        encodedLen[2] = (byte) ((encoded.length & 0x0000FF00) >> 8);
        encodedLen[3] = (byte) ((encoded.length & 0x000000FF));
        wrappedStream.write(encodedLen, 0, encodedLen.length);
        wrappedStream.write(encoded, 0, encoded.length);
        wrappedStream.flush();
        data.clear();
    }
}
