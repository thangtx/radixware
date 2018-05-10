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
package org.radixware.kernel.starter.meta;

import java.util.HashSet;
import java.util.Set;

public class RevisionMetaDiff {
    private final Set<String> addedClasses;
    private final Set<String> modifiedClasses;
    private final Set<String> removedClasses;
    private final Set<String> modifiedJars;
    
    RevisionMetaDiff(final Set<String> addedClasses, final Set<String> removedClasses, final Set<String> modifiedJars) {
        this.addedClasses = convert2ValidClassNames(addedClasses);
        this.modifiedClasses = new HashSet<>();
        this.removedClasses = convert2ValidClassNames(removedClasses);
        this.modifiedJars = modifiedJars;
    }

    RevisionMetaDiff(final Set<String> addedClasses, final Set<String> modifiedClasses, final Set<String> removedClasses, final Set<String> modifiedJars) {
        this.addedClasses = convert2ValidClassNames(addedClasses);
        this.modifiedClasses = convert2ValidClassNames(modifiedClasses);
        this.removedClasses = convert2ValidClassNames(removedClasses);
        this.modifiedJars = modifiedJars;
    }
    
    public boolean hasAddedClasses() {
        return addedClasses.size() > 0;
    }
    
    public Set<String> addedClasses() {
        return addedClasses;
    }

    public boolean hasRemovedClasses() {
        return removedClasses.size() > 0;
    }
    
    public Set<String> removedClasses() {
        return removedClasses;
    }

    public boolean hasModifiedJars() {
        return modifiedJars.size() > 0;
    }
    
    public Set<String> modifiedJars() {
        return modifiedJars;
    }
    
    public boolean hasModifiedClasses() {
        return modifiedClasses.size() > 0;
    }
    
    public Set<String> modifiedClasses() {
        return modifiedClasses;
    }    

    public static String partName2ClassName(final String partName) {
        if (partName == null || partName.isEmpty()) {
            throw new IllegalArgumentException("Part name to convert can't be null or empty");
        }
        else {
            return partName.replace('/','.').substring(0,partName.lastIndexOf(".class"));
        }
    }
    
    private static Set<String> convert2ValidClassNames(final Set<String> source) {
        final Set<String> result =  new HashSet<>();
        
        for (String item : source) {
            result.add(partName2ClassName(item));
        }
        return result;
    }

    @Override
    public String toString() {
        return "RevisionMetaDiff{" + "addedClasses=" + addedClasses + ", modifiedClasses=" + modifiedClasses + ", removedClasses=" + removedClasses + ", modifiedJars=" + modifiedJars + '}';
    }
}
