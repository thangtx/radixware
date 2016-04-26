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
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;


public abstract class ExtendableMembers<T extends AdsDefinition & IAdsClassMember> extends ExtendableDefinitions<T> implements IAdsClassMember {

    private final class DefaultMemberListHierarchyIterator extends MemberListHierarchyIterator {

        @Override
        protected HierarchyIterator<AdsClassDef> createInternal(EScope scope, Mode mode) {
            return new ClassHierarchyIterator(getOwnerClass(), scope, mode);
        }

        public DefaultMemberListHierarchyIterator(EScope scope, Mode mode) {
            super(scope, mode);
        }
    }

    public abstract class MemberListHierarchyIterator<D> extends HierarchyIterator<ExtendableMembers<T>> {

        // private List<ExtendableMembers<T>> current;
        private List<ExtendableMembers<T>> next;
        private HierarchyIterator<T> internal;

        protected abstract HierarchyIterator<T> createInternal(EScope scope, Mode mode);

        protected MemberListHierarchyIterator(EScope scope, Mode mode) {
            super(mode);
            this.internal = createInternal(scope, mode);
            // this.current = Collections.singletonList(ExtendableMembers.this);
            this.next = null;
        }

        @Override
        public boolean hasNext() {
            if (next == null && internal.hasNext()) {
                List<ExtendableMembers<T>> results = new LinkedList<>();
                while (internal.hasNext()) {
                    Chain<T> nextDefinitions = internal.next();
                    for (T def : nextDefinitions) {
                        ExtendableMembers<T> list = (ExtendableMembers<T>) findInstance(def);
                        if (list != null && /*
                                 * !current.contains(list) &&
                                 */ !results.contains(list)) {
                            results.add(list);
                        }
                    }
                    if (!results.isEmpty()) {
                        break;
                    }
//                    if (nextClass != null) {
//                        ExtendableMembers<T> list = (ExtendableMembers<T>) findInstance(nextClass);
//                        if (list != null) {
//                            next = list;
//                            while (!nextClass.isParentOf(list)) {//protection from hierarchy jumps caused by collection inheritance
//                                //to prevent cycle inheritence protection triggering
//                                if (internal.hasNext()) {
//                                    nextClass = internal.next();
//                                } else {
//                                    break;
//                                }
//                            }
//                            break;
//                        }
//                    }
                }
                if (results.isEmpty()) {
                    next = null;
                } else {
                    next = new ArrayList<>(results);
                }
            }
            return next != null;
        }

        @Override
        public Chain<ExtendableMembers<T>> next() {
            if (hasNext()) {
                Chain<ExtendableMembers<T>> result = Chain.newInstance(new ArrayList<>(next));
                next = null;
                return result;
            } else {
                return Chain.empty();
            }
        }
    }

    protected ExtendableMembers(RadixObject owner, Definitions<T> localCollection) {
        super(owner, localCollection);
    }

//    protected ExtendableMembers(AdsClassDef owner) {
//        super(owner);
//    }
    protected ExtendableMembers(IAdsClassMember owner) {
        super((RadixObject) owner);
    }

    @Override
    public AdsClassDef getOwnerClass() {
        RadixObject container = getContainer();
        while (container != null) {
            if (container instanceof AdsClassDef) {
                return (AdsClassDef) container;
            }
            if (container instanceof IAdsClassMember) {
                return ((IAdsClassMember) container).getOwnerClass();
            }
            container = container.getContainer();
        }
        return null;
    }

    @Override
    public ExtendableDefinitions<T> findInstance(Definition owner) {
        if (owner instanceof AdsDefinition) {
            return findInstance((AdsDefinition) owner);
        } else {
            return null;
        }
    }

    public abstract ExtendableDefinitions<T> findInstance(AdsDefinition clazz);

    @Override
    @SuppressWarnings("unchecked")
    protected HierarchyIterator<ExtendableDefinitions<T>> newIterator(EScope scope, HierarchyIterator.Mode mode) {
        return new DefaultMemberListHierarchyIterator(scope, mode);
    }
}
