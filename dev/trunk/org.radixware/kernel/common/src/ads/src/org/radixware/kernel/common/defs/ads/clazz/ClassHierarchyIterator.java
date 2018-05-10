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

package org.radixware.kernel.common.defs.ads.clazz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public final class ClassHierarchyIterator extends HierarchyIterator<AdsClassDef> {

    private AdsClassDef init;
    private List<AdsClassDef> current;
    private List<AdsClassDef> next;
    private final EScope scope;
    private final List<AdsClassDef> availableOverwrites = new LinkedList<>();

    public ClassHierarchyIterator(AdsClassDef init, EScope scope) {
        this(init, scope, Mode.FIND_FIRST);
    }

    public ClassHierarchyIterator(AdsClassDef init, EScope scope, HierarchyIterator.Mode mode) {
        super(mode);
        this.init = init;
        this.current = Collections.singletonList(init);
        this.next = Collections.singletonList(init);
        this.scope = scope;
    }

    private List<AdsClassDef> findSuperClass(AdsClassDef localCurrent) {
        AdsTypeDeclaration decl = localCurrent.getInheritance().getSuperClassRef();
        if (decl != null) {
            List<AdsType> types = decl.resolveAll(this.init);
            List<AdsClassDef> result = new LinkedList<>();
            for (AdsType type : types) {
                if (type instanceof AdsClassType) {
                    AdsClassDef base = ((AdsClassType) type).getSource();
                    if (base != localCurrent && base != null) {
                        result.add(base);
                    }
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean hasNext() {
        synchronized (this) {
            if (this.next == null && this.current != null) {
                availableOverwrites.clear();
                for (AdsClassDef clazz : this.current) {
                    if (clazz == null) {
                        continue;
                    }
                    clazz.getHierarchy().findOverwritten().iterate(new SearchResult.Acceptor<AdsClassDef>() {

                        @Override
                        public void accept(AdsClassDef object) {
                            if (!current.contains(object) && !availableOverwrites.contains(object)) {
                                availableOverwrites.add(object);
                            }
                        }
                    });
                }
                if (availableOverwrites.isEmpty()) {
                    if (scope == EScope.ALL) {
                        for (AdsClassDef clazz : this.current) {
                            List<AdsClassDef> superClasses = findSuperClass(clazz);
                            for (AdsClassDef superClass : superClasses) {
                                if (!availableOverwrites.contains(superClass) && !this.current.contains(superClass)) {
                                    availableOverwrites.add(superClass);
                                }
                            }
                        }
                        if (availableOverwrites.isEmpty()) {
                            next = null;
                        } else {
                            next = new ArrayList<>(availableOverwrites);
                        }
                    } else {
                        next = null;
                    }
                } else {
                    next = new ArrayList<>(availableOverwrites);
                }
            }
        }
        return next != null && !next.isEmpty();
    }

    @Override
    public Chain<AdsClassDef> next() {
        if (hasNext()) {
            current = next;
            next = null;
            return Chain.newInstance(current);
        } else {
            return Chain.empty();
        }
    }
}
