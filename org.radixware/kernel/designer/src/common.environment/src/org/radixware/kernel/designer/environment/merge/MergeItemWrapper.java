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
package org.radixware.kernel.designer.environment.merge;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class MergeItemWrapper implements Comparable<MergeItemWrapper> {

    public Long revision;
    public String comment;
    public String author;
    public Date date;
    public byte[] hash;
    public String xmlAsString;

    MergeItemWrapper() {
    }

    MergeItemWrapper(MergeItemWrapper src) {
        this.revision = src.revision;
        this.comment = src.comment;
        this.author = src.author;
        this.date = src.date;
        this.hash = Arrays.copyOf(src.hash, src.hash.length);
    }

    @Override
    public String toString() {
        return "<html><b>Revision</b>: " + String.valueOf(revision)
                + "<br><b>Author</b>: " + String.valueOf(author)
                + "<br><b>Date</b>: " + String.valueOf(date)
                + "<br><b>Comment</b>: " + String.valueOf(comment)
                + "<br><b>Hash</b>: " + hashToString(hash)
                + "</html>";
        //"+"This is a"+"<br>"+"tool tip"+"</html>"
    }

    private static String hashToString(byte x[]) {
        if (x == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder(x.length * 3);
        for (int i = 0; i < x.length; i++) {
            byte b = x[i];
            String rr = Integer.toHexString(b);
            int l = rr.length();
            if (l > 2) {
                rr = rr.substring(l - 2);
            }
            if (l == 1) {
                rr = "0" + rr;
            }
            if (i != 0) {
                sb.append(" ");
            }
            sb.append(rr);
        }
        return sb.toString();
    }

    @Override
    public int compareTo(final MergeItemWrapper t) {
        return -this.revision.compareTo(t.revision);
    }
}
