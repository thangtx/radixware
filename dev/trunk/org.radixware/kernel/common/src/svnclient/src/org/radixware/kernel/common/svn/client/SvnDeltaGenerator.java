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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.radixware.kernel.common.svn.RadixSvnException;

public class SvnDeltaGenerator {

    private SvnDeltaAlgorithm xDelta = new SvnXDeltaAlgorithm();

    private byte[] sourceBuffer;
    private byte[] trgetBuffer;
    private int maximumBufferSize;

    public SvnDeltaGenerator() {
        this(1024 * 100);
    }

    public SvnDeltaGenerator(int maximumDiffWindowSize) {
        maximumBufferSize = maximumDiffWindowSize;
        int initialSize = Math.min(8192, maximumBufferSize);
        sourceBuffer = new byte[initialSize];
        trgetBuffer = new byte[initialSize];
    }

    public String sendDelta(String path, InputStream target, ISvnDeltaConsumer consumer, boolean computeChecksum) throws RadixSvnException {
        return sendDelta(path, new InputStream() {

            @Override
            public int read() throws IOException {
                return -1;
            }
        }, 0, target, consumer, computeChecksum);
    }

    public String sendDelta(String path, InputStream source, long sourceOffset, InputStream target, ISvnDeltaConsumer consumer, boolean computeChecksum) throws RadixSvnException {
        MessageDigest digest = null;
        if (computeChecksum) {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RadixSvnException("MD5 implementation not found", e);
            }
        }
        boolean windowSent = false;
        while (true) {
            int targetLength;
            int sourceLength;
            try {
                targetLength = readToBuffer(target, trgetBuffer);
            } catch (IOException e) {
                throw new RadixSvnException(e);
            }
            if (targetLength <= 0) {
                // send empty window, needed to create empty file. 
                // only when no windows was sent at all.
                if (!windowSent && consumer != null) {
                    consumer.textDeltaChunk(path, SvnDiffWindow.EMPTY);
                }
                break;
            }
            try {
                sourceLength = readToBuffer(source, sourceBuffer);
            } catch (IOException e) {
                throw new RadixSvnException(e);
            }
            if (sourceLength < 0) {
                sourceLength = 0;
            }
            // update digest,
            if (digest != null) {
                digest.update(trgetBuffer, 0, targetLength);
            }
            // generate and send window
            sendDelta(path, sourceOffset, sourceBuffer, sourceLength, trgetBuffer, targetLength, consumer);
            windowSent = true;
            sourceOffset += sourceLength;
        }
        if (consumer != null) {
            consumer.textDeltaEnd(path);
        }
        return SvnUtil.toHexDigest(digest);
    }

    public void sendDelta(String path, byte[] target, int targetLength, ISvnDeltaConsumer consumer) throws RadixSvnException {
        sendDelta(path, null, 0, 0, target, targetLength, consumer);
    }

    public void sendDelta(String path, byte[] source, int sourceLength, long sourceOffset, byte[] target,
            int targetLength, ISvnDeltaConsumer consumer) throws RadixSvnException {
        if (targetLength == 0 || target == null) {
            // send empty window, needed to create empty file. 
            // only when no windows was sent at all.
            if (consumer != null) {
                consumer.textDeltaChunk(path, SvnDiffWindow.EMPTY);
            }
            return;
        }
        if (source == null) {
            source = new byte[0];
            sourceLength = 0;
        } else if (sourceLength < 0) {
            sourceLength = 0;
        }
        // generate and send window
        sendDelta(path, sourceOffset, source == null ? new byte[0] : source, sourceLength, target, targetLength, consumer);
    }

    private void sendDelta(String path, long sourceOffset, byte[] source, int sourceLength, byte[] target, int targetLength, ISvnDeltaConsumer consumer) throws RadixSvnException {
        // always use x algorithm, v is deprecated now.
        SvnDeltaAlgorithm algorithm = xDelta;
        algorithm.computeDelta(source, sourceLength, target, targetLength);
        // send single diff window to the editor.
        if (consumer == null) {
            algorithm.reset();
            return;
        }
        int instructionsLength = algorithm.getInstructionsLength();
        int newDataLength = algorithm.getNewDataLength();
        SvnDiffWindow window = new SvnDiffWindow(sourceOffset, sourceLength, targetLength, instructionsLength, newDataLength);
        window.setData(algorithm.getData());
        OutputStream os = consumer.textDeltaChunk(path, window);
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException ex) {
            //ignore
        }
        algorithm.reset();
    }

    private int readToBuffer(InputStream is, byte[] buffer) throws IOException {
        int read = SvnUtil.readStreamData(is, buffer, 0, buffer.length);
        if (read <= 0) {
            return read;
        }
        if (read == buffer.length && read < maximumBufferSize) {
            byte[] expanded = new byte[maximumBufferSize];
            System.arraycopy(buffer, 0, expanded, 0, read);
            if (buffer == trgetBuffer) {
                trgetBuffer = expanded;
            } else {
                sourceBuffer = expanded;
            }
            buffer = expanded;

            int anotherRead = SvnUtil.readStreamData(is, buffer, read, buffer.length - read);
            if (anotherRead <= 0) {
                return read;
            }
            read += anotherRead;
        }
        return read;
    }
}
