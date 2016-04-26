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

package org.radixware.kernel.designer.common.dialogs.usages;

import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;


public class FindUsagesCfg {

    public enum ESearchType {

        FIND_USAGES_GET,
        FIND_USAGES_SET,
        FIND_USAGES,
        FIND_REPLACERS,
        FIND_REPLACED,
        FIND_SUBPRESENTATIONS,
        FIND_ALL_SUBPRESENTATIONS,
        FIND_USED,
        FIND_ALL_DESCEDANTS, // override, overwrite or extend
        FIND_DIRECT_DESCENDANTS_ONLY
    }
    private final Definition definition;
    private ESearchType searchType = ESearchType.FIND_USAGES;
    private Set<? extends RadixObject> roots = null; // null - ignored

    public FindUsagesCfg(Definition definition) {
        this.definition = definition;
    }

    public Definition getDefinition() {
        return definition;
    }

    public ESearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(ESearchType searchType) {
        this.searchType = searchType;
    }

    public Set<? extends RadixObject> getRoots() {
        return roots;
    }

    public void setRoots(Set<? extends RadixObject> roots) {
        this.roots = roots;
    }
}
