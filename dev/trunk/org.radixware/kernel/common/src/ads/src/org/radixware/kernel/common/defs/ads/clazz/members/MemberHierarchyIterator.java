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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.ClassHierarchyIterator;


public abstract class MemberHierarchyIterator<T extends AdsClassMember> extends HierarchyIterator<T> {

    private ClassHierarchyIterator internal;
    private T init;
    private List<T> next;

    public MemberHierarchyIterator(T member, EScope scope, HierarchyIterator.Mode mode) {
        super(mode);
        internal = new ClassHierarchyIterator(member.getOwnerClass(), scope, mode);
        this.init = member;

        this.next = null;
    }

    @Override
    public boolean hasNext() {
        if (this.next == null && internal.hasNext()) {

            List<T> result = new LinkedList<>();

            while (internal.hasNext()) {
                HierarchyIterator.Chain<AdsClassDef> nextClasses = internal.next();

                for (AdsClassDef clazz : nextClasses) {
                    T instance = findInClass(clazz);
                    if (instance != null && !result.contains(instance)) {
                        result.add(instance);
                    }
                }
                if (!result.isEmpty()) {
                    break;
                }
            }

            if (result.isEmpty()) {
                next = null;
            } else {
                next = new ArrayList<>(result);
            }
        }
        return next != null;
    }

    public abstract T findInClass(AdsClassDef clazz);

    @Override
    public HierarchyIterator.Chain<T> next() {
        if (hasNext()) {
            HierarchyIterator.Chain<T> result = Chain.newInstance(next);
            next = null;
            return result;
        } else {
            return HierarchyIterator.Chain.empty();
        }
    }
}
