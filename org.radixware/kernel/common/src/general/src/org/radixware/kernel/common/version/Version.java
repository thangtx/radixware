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

package org.radixware.kernel.common.version;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Version, for exapmle: 0, 1.1, 1.2.3.4
 *
 */
public class Version extends ArrayList<Integer> implements Comparable<Version> {

    public static final Version ZERO = new Version("0");

    public Version() {
        super(4);
        add(0);
    }

    public Version(Version ver) {
        super(ver);
    }

    public Version(String version) {
        if (version == null || version.isEmpty() || version.equals("null")) {
            add(0);
        } else {
            final StringTokenizer st = new StringTokenizer(version, ".");
            try {
                while (st.hasMoreTokens()) {
                    final String token = st.nextToken();
                    add(Integer.parseInt(token)); // exception for null or empty.
                }
            } catch (Exception ex) {
                throw new RuntimeException("Invalid version \"" + version + "\"");
            }
        }
    }

    public static boolean isParsed(String version) {
        if (version == null) {
            return false;
        }
        try {
            final Version ver = new Version(version);
            return ver != null && !ver.isEmpty() && !version.isEmpty();
        } catch (Exception ex) {
        }
        return false;
    }

    public Version fork() {
        return new Version(this);
    }

    public Version getParent() {
        final Version v = new Version();
        v.clear();
        for (int i = 0; i < size() - 1; i++) {
            v.add(get(i));
        }
        return v;
    }

    public boolean isZero() {
        return size() == 1 && get(0).equals(0);
    }

    public Version inc() {
        final Version ver = new Version(this);
        if (ver.isEmpty()) {
            ver.add(0);
        }
        ver.set(size() - 1, get(size() - 1) + 1);
        return ver;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            if (i != 0) {
                sb.append(".");
            }
            sb.append(Integer.toString(get(i)));
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Version ver) {
        int i = 0;
        if (ver == null) {
            return 1;
        }
        while (i < size() && i < ver.size()) {
            if (get(i) > ver.get(i)) {
                return 1;
            }
            if (get(i) < ver.get(i)) {
                return -1;
            }
            i++;
        }
        while (i < size()) {
            if (get(i++) != 0) {
                return 1;
            }
        }
        while (i < ver.size()) {
            if (ver.get(i++) != 0) {
                return -1;
            }
        }
        return 0;
    }
}
