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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.util.*;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.types.Id;


abstract class MemberHierarchy<T extends AdsDefinition & IAdsClassMember> extends AdsDefinition.Hierarchy<T> {

    protected T member;

    MemberHierarchy(T member) {
        super(member);
        this.member = member;
    }

    @Override
    public SearchResult<T> findOverridden() {
        if (member.getOwnerClass() == null) {
            return SearchResult.empty();
        }
        List<T> result = new LinkedList<>();
        collectAllOverriden(result);
        if (result.isEmpty()) {
            return SearchResult.empty();
        } else {
            return SearchResult.list(result);
        }
    }

    private void collectAllOverriden(final List<T> result) {
        if (member.getOwnerClass() == null) {
            return;
        }
        AdsClassDef current = member.getOwnerClass();
        List<AdsType> superClasses = new ArrayList<>();
        AdsTypeDeclaration superRef = current.getInheritance().getSuperClassRef();

        superClasses.addAll(superRef.resolveAll(current));

        if (superClasses.isEmpty()) {
            return;
        }
        int mode = 0;

        final List<AdsClassDef> classes = new LinkedList<>();
        final Set<AdsClassDef> allClasses = new HashSet<>();

        while (!superClasses.isEmpty()) {
            if (mode == 0) {//lookup
                for (int i = 0; i < superClasses.size(); i++) {
                    AdsType type = superClasses.get(i);
                    if (type instanceof AdsClassType) {
                        AdsClassDef clazz = ((AdsClassType) type).getSource();
                        if (allClasses.contains(clazz)) {
                            continue;
                        }
                        if (clazz instanceof IAdsPresentableClass) {
                            T ovr = findMember(((IAdsPresentableClass) clazz).getPresentations(), member.getId());
                            if (ovr != null) {
                                result.add(ovr);
                            }
                        }
                        classes.add(clazz);
                        allClasses.add(clazz);
                    }
                }
                mode = 1;
            } else {
                superClasses.clear();
                for (AdsClassDef clazz : classes) {
                    superRef = clazz.getInheritance().getSuperClassRef();
                    if (superRef != null) {
                        superClasses.addAll(superRef.resolveAll(current));
                    }
                }
                classes.clear();
                mode = 0;
            }
        }
    }

    @Override
    public SearchResult<T> findOverwritten() {
        if (member.getOwnerClass() == null) {
            return SearchResult.empty();
        }
        List<T> result = new LinkedList<>();
        collectAllOverwritten(result);
        if (result.isEmpty()) {
            return SearchResult.empty();
        } else {
            return SearchResult.list(result);
        }
    }

    private void collectAllOverwritten(List<T> collection) {
        if (member.getOwnerClass() == null) {
            return;
        }
        List<AdsClassDef> ovrClasses = new LinkedList<>();

        member.getOwnerClass().getHierarchy().findOverwritten().save(ovrClasses);

        findInClasses(ovrClasses, collection);
    }

    private void findInClasses(List<AdsClassDef> ovrs, List<T> resultSet) {
        List<AdsClassDef> searchNext = null;
        for (AdsClassDef ovr : ovrs) {
            T ovr_member = findMember(((IAdsPresentableClass) ovr).getPresentations(), member.getId());
            if (ovr_member != null && !resultSet.contains(ovr_member)) {
                resultSet.add(ovr_member);
            } else {
                if (searchNext == null) {
                    searchNext = new LinkedList<>();
                }
                searchNext.add(ovr);
            }
        }
        if (searchNext != null) {
            for (AdsClassDef clazz : searchNext) {
                List<AdsClassDef> newOvrs = clazz.getHierarchy().findOverwritten().all();
                findInClasses(newOvrs, resultSet);
            }
        }
    }

    protected abstract T findMember(ClassPresentations clazz, Id id);
}
