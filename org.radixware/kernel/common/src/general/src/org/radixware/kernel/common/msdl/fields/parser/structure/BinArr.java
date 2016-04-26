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


package org.radixware.kernel.common.msdl.fields.parser.structure;


public class BinArr {

        public byte[] data;

        public BinArr(byte from[]) {
            data = new byte[from.length];
            System.arraycopy(from, 0, data, 0, from.length);
        }

        @Override
        public boolean equals(Object compare) {
            if (compare instanceof BinArr) {
                return java.util.Arrays.equals(data, ((BinArr) compare).data);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            Byte b = data[0];
            return b.hashCode();
        }
    }