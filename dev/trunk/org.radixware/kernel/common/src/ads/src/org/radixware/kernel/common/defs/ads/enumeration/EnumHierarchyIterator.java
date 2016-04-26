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

package org.radixware.kernel.common.defs.ads.enumeration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.SearchResult;


public final class EnumHierarchyIterator extends HierarchyIterator<AdsEnumDef> {

    private AdsEnumDef init;
    private List<AdsEnumDef> current;
    private List<AdsEnumDef> next;
    private final List<AdsEnumDef> availableOverwtites = new LinkedList<>();
    private final EScope scope;

    public EnumHierarchyIterator(AdsEnumDef init, EScope scope, HierarchyIterator.Mode mode) {
        super(mode);
        this.init = init;
        this.current = Collections.singletonList(init);
        this.next = Collections.singletonList(init);
        this.scope = scope;
    }

    @Override
    public boolean hasNext() {
        synchronized (this) {
            if (this.next == null && this.current != null) {
                availableOverwtites.clear();
                for (AdsEnumDef clazz : this.current) {
                    clazz.getHierarchy().findOverwritten().iterate(new SearchResult.Acceptor<AdsEnumDef>() {

                        @Override
                        public void accept(AdsEnumDef object) {
                            if (!current.contains(object) && !availableOverwtites.contains(object)) {
                                availableOverwtites.add(object);
                            }
                        }
                    });
                }
                if (availableOverwtites.isEmpty()) {
                    next = null;
                } else {
                    next = new ArrayList<>(availableOverwtites);
                }

            }
        }

        return next != null && !next.isEmpty();
    }

    @Override
    public Chain<AdsEnumDef> next() {
        if (hasNext()) {
            current = next;
            next = null;
            return Chain.newInstance(current);
        } else {
            return Chain.empty();
        }
    }
}