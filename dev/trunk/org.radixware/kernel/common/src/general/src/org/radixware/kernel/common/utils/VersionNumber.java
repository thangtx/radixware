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
package org.radixware.kernel.common.utils;

import java.util.Arrays;

public class VersionNumber implements Comparable<VersionNumber> {

    private final int[] versionComponents;

    public static VersionNumber valueOf(String versionString) {
        if (versionString == null || "null".equals(versionString)) {
            return null;
        }
        
        return new VersionNumber(versionString);
    }

    private VersionNumber(String versionString) {                
        String[] versionParts = versionString.split("\\.");
        versionComponents = new int[versionParts.length];

        for (int i = 0; i < versionParts.length; i++) {
            versionComponents[i] = Integer.parseInt(versionParts[i]);
        }
    }

    public int[] getVersionComponents() {
        return Arrays.copyOf(versionComponents, versionComponents.length);
    }

    @Override
    public int compareTo(VersionNumber o) {
        int length = o.versionComponents.length > versionComponents.length ? o.versionComponents.length : versionComponents.length;
        for (int i = 0; i < length; i++) {
            if (i >= versionComponents.length) {
                return -1;
            }

            if (i >= o.versionComponents.length) {
                return 1;
            }

            if (o.versionComponents[i] > versionComponents[i]) {
                return -1;
            }

            if (versionComponents[i] > o.versionComponents[i]) {
                return 1;
            }
        }

        return 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Arrays.hashCode(this.versionComponents);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VersionNumber other = (VersionNumber) obj;
        if (!Arrays.equals(this.versionComponents, other.versionComponents)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < versionComponents.length; i++) {
            resultBuilder.append(versionComponents[i]).append(i == versionComponents.length - 1 ? "" : ".");
        }

        return resultBuilder.toString();
    }
}
