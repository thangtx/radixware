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

import java.nio.ByteBuffer;
 
class SvnDiffInfo {

    public static final int INFO_TYPE_COPY_FROM_SOURCE = 0x00;
    public static final int INFO_TYPE_COPY_FROM_TARGET = 0x01;
    public static final int INFO_TYPE_COPY_FROM_NEW_DATA = 0x02;

    public int type;
    public int length;
    public int offset;

    public SvnDiffInfo(int t, int l, int o) {
        type = t;
        length = l;
        offset = o;
    }

    public SvnDiffInfo() {
        this(0, 0, 0);

    }

    public void writeTo(ByteBuffer target) {
        byte first = (byte) (type << 6);
        if (length <= 0x3f && length > 0) {
            first |= (length & 0x3f);
            target.put((byte) (first & 0xff));
        } else {
            target.put((byte) (first & 0xff));
            writeInt(target, length);
        }
        if (type == 0 || type == 1) {
            writeInt(target, offset);
        }
    }

    public static void writeInt(ByteBuffer os, int i) {
        if (i == 0) {
            os.put((byte) 0);
            return;
        }
        int count = 1;
        long v = i >> 7;
        while (v > 0) {
            v = v >> 7;
            count++;
        }
        byte b;
        int r;
        while (--count >= 0) {
            b = (byte) ((count > 0 ? 0x1 : 0x0) << 7);
            r = ((byte) ((i >> (7 * count)) & 0x7f)) | b;
            os.put((byte) r);
        }
    }

    public static void writeLong(ByteBuffer os, long i) {
        if (i == 0) {
            os.put((byte) 0);
            return;
        }
        // how many bytes there are:
        int count = 1;
        long v = i >> 7;
        while (v > 0) {
            v = v >> 7;
            count++;
        }
        byte b;
        int r;
        while (--count >= 0) {
            b = (byte) ((count > 0 ? 0x1 : 0x0) << 7);
            r = ((byte) ((i >> (7 * count)) & 0x7f)) | b;
            os.put((byte) r);
        }
    }
}
