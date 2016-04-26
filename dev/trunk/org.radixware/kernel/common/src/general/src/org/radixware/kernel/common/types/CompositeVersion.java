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

package org.radixware.kernel.common.types;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.WrongFormatError;


public class CompositeVersion implements Comparable{
    private static final Integer ZERO = Integer.valueOf(0);
    private final List<Integer> versions;

    public CompositeVersion(final String version) {
        final String[] arrVer = version == null || version.isEmpty() ? new String[]{} : version.split("\\.");
        versions = new ArrayList<Integer>(4);
        try {
            for (String v : arrVer){
                versions.add(Integer.parseInt(v));
            }
        } catch (NumberFormatException e) {
            throw new WrongFormatError("Wrong version format: " + String.valueOf(version), e);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        //can't use versions list because it can be virtualy extended with ".0"s on comparation
//        final CompositeVersion other = (CompositeVersion) obj;
//        if (this.versions != other.versions && (this.versions == null || !this.versions.equals(other.versions))) {
//            return false;
//        }
//        return true;
        return compareTo(obj) == 0;
    }

    @Override
    public int hashCode() {
        //can't use versions list because it can be virtualy extended with ".0"s on comparation
        //int hash = 3;
        //hash = 71 * hash + (this.versions != null ? this.versions.hashCode() : 0);
        //return hash;
        return 13;
    }

    @Override
    public int compareTo(final Object o) {
        if (o == null){
            if (versions.isEmpty()) {
                return 0;
            }
            if (versions.get(0) == null || versions.get(0).equals(ZERO)){
                return 0;
            }
            return 1;
        }
        if (!(o instanceof CompositeVersion)){
            throw new IllegalUsageError("Wrong compareTo() argument type: " + o.getClass().getName());
        }
        final CompositeVersion that = (CompositeVersion)o;
        final int maxLen = Math.max(this.versions.size(), that.versions.size());
        for (int i = 0; i < maxLen; i++){
            final Integer thisVer = i < this.versions.size() ? this.versions.get(i) : ZERO;
            final Integer thatVer = i < that.versions.size() ? that.versions.get(i) : ZERO;
            final int comparationResult = thisVer.compareTo(thatVer);
            if (comparationResult != 0) {
                return comparationResult;
            }
        }
        return 0;
    }

    
}
