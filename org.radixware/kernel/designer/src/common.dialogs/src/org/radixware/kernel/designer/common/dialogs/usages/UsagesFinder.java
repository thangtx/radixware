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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;


public class UsagesFinder extends FindUsages {

    private final Map<RadixObject, List<RadixObject>> result;

    public UsagesFinder(FindUsagesCfg cfg) {
        super(cfg);

        result = new HashMap<>();
    }

    public UsagesFinder(UsagesFinder root, Definition definition) {
        super(root, definition);

        this.result = root.result;
    }

    @Override
    protected FindUsages createFindUsages(FindUsages root, Definition definition) {
        return new UsagesFinder(this, definition);
    }

    @Override
    protected void accept(RadixObject head, RadixObject usage) {
        List<RadixObject> usages = result.get(head);
        if (usages == null) {
            usages = new ArrayList<>();
            result.put(head, usages);
        }
        usages.add(usage);
    }

    @Override
    protected void complete() {
    }

    @Override
    protected void prepare() {
        if (isRoot()) {
            result.clear();
        }
    }

    public final Map<RadixObject, List<RadixObject>> search() {
        find();
        return result;
    }
    
    public static List<RadixObject> toList(Map<RadixObject, List<RadixObject>> result) {
        final Set<RadixObject> usages = new HashSet<>();

        for (final List<RadixObject> list : result.values()) {
            usages.addAll(list);
        }
        
        return new ArrayList<>(usages);
    }
}
