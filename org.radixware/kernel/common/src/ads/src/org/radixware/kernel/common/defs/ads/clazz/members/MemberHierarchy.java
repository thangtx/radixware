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

import java.util.*;
import org.radixware.kernel.common.defs.DefinitionLink;
import org.radixware.kernel.common.defs.DefinitionListLink;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.JavaClassType;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.types.Id;


abstract class MemberHierarchy<T extends AdsDefinition & IAdsClassMember> extends AdsDefinition.Hierarchy<T> {

    private T member;
    private boolean useInterfaces;

    MemberHierarchy(T member, boolean useInterfaces) {
        super(member);
        this.member = member;
        this.useInterfaces = useInterfaces;
    }

//    @Deprecated
//    private T findInSupers(AdsClassDef current, AdsTypeDeclaration starter, ArrayList<AdsClassDef> resolvedSuperclasses, AdsClassDef lastResolvedSuperType) {
//        AdsTypeDeclaration superRef = starter;
//
//        while (superRef != null) {
//            AdsType type = superRef.resolve(current);
//            AdsClassDef rt = null;
//
//            if (type instanceof AdsClassType) {
//                rt = ((AdsClassType) type).getSource();
//            } else if (type instanceof JavaClassType) {
//                JavaClassType ct = (JavaClassType) type;
//                IPlatformClassPublisher pub = ((AdsSegment) current.getModule().getSegment()).getBuildPath().getPlatformPublishers().findPublisherByName(ct.getJavaClassName());
//                if (pub instanceof AdsClassDef) {
//                    rt = (AdsClassDef) pub;
//                }
//            } else {
//                break;
//            }
//            if (rt == null) {
//                break;
//            }
//            if (lastResolvedSuperType == rt) {
//                break;
//            }
//            lastResolvedSuperType = rt;
//            resolvedSuperclasses.add(rt);
//            T ovr = findMember(rt, member.getId());
//            if (ovr != null) {
//                return ovr;
//            } else {
//                superRef = rt.getInheritance().getSuperClassRef();
//            }
//        }
//
//        return null;
//    }
    private T findAllInInterfaces(AdsClassDef current, List<T> result) {
        if (current == null) {
            current = member.getOwnerClass();
        }
        if (current == null) {
            return null;
        }

        List<AdsType> superInterfaces = new ArrayList<>();
        List<AdsTypeDeclaration> interfaceRefs = current.getInheritance().getOwnAndPlatformInerfaceRefList(EScope.LOCAL_AND_OVERWRITE);



        if (interfaceRefs != null) {
            for (AdsTypeDeclaration ifaceRef : interfaceRefs) {
                superInterfaces.addAll(ifaceRef.resolveAll(current));
            }
        }

        if (superInterfaces.isEmpty()) {
            return null;
        }
        int mode = 0;

        final List<AdsClassDef> classes = new ArrayList<>();
        final Set<AdsClassDef> allClasses = new HashSet<>();

        while (!superInterfaces.isEmpty()) {
            if (mode == 0) {//lookup
                for (int i = 0; i < superInterfaces.size(); i++) {
                    AdsType type = superInterfaces.get(i);
                    AdsClassDef rt = null;
                    if (type instanceof AdsClassType) {
                        rt = ((AdsClassType) type).getSource();
                    } else if (type instanceof JavaClassType) {
                        JavaClassType ct = (JavaClassType) type;
                        IPlatformClassPublisher pub = ((AdsSegment) current.getModule().getSegment()).getBuildPath().getPlatformPublishers().findPublisherByName(ct.getJavaClassName());
                        if (pub instanceof AdsClassDef) {
                            rt = (AdsClassDef) pub;
                        }
                    }
                    if (allClasses.contains(rt)) {
                        continue;
                    }
                    if (rt instanceof AdsInterfaceClassDef) {
                        T ovr = findMember(rt, member.getId(), EScope.LOCAL_AND_OVERWRITE);
                        if (ovr != null) {
                            if (result == null) {
                                return ovr;
                            } else {
                                result.add(ovr);
                            }
                        }
                        allClasses.add(rt);
                        classes.add(rt);
                    }
                }
                mode = 1;
            } else {
                superInterfaces.clear();
                for (AdsClassDef clazz : classes) {
                    interfaceRefs = clazz.getInheritance().getOwnAndPlatformInerfaceRefList(EScope.LOCAL_AND_OVERWRITE);
                    if (interfaceRefs != null) {
                        for (AdsTypeDeclaration ifaceRef : interfaceRefs) {
                            superInterfaces.addAll(ifaceRef.resolveAll(current));
                        }
                    }

                }
                classes.clear();
                mode = 0;
            }
        }
        return result == null || result.isEmpty() ? null : result.get(0);
    }

    private T findAllInSupers(List<T> result, List<AdsClassDef> allClasses) {
        if (member.getOwnerClass() == null) {
            return null;
        }
        AdsClassDef current = member.getOwnerClass();

        List<AdsType> superClasses = new ArrayList<>();
        AdsTypeDeclaration superRef = current.getInheritance().getSuperClassRef();

        if (superRef != null) {
            superClasses.addAll(superRef.resolveAll(current));
        }

        if (superClasses.isEmpty()) {
            return null;
        }
        int mode = 0;

        final List<AdsClassDef> classes = new LinkedList<>();


        while (!superClasses.isEmpty()) {
            if (mode == 0) {//lookup
                for (int i = 0; i < superClasses.size(); i++) {
                    AdsType type = superClasses.get(i);
                    if (type instanceof AdsClassType) {
                        AdsClassDef clazz = ((AdsClassType) type).getSource();
                        if (allClasses.contains(clazz)) {
                            continue;
                        }
                        //process class and it's overwrites
                        T ovr = findMember(clazz, member.getId(), EScope.LOCAL_AND_OVERWRITE);
                        if (ovr != null) {
                            if (result == null) {
                                return ovr;
                            } else {
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
        return result == null || result.isEmpty() ? null : result.get(0);
    }

    @Override
    public SearchResult<T> findOverridden() {
        if (member.getOwnerClass() == null) {
            return SearchResult.empty();
        }

        ArrayList<AdsClassDef> resolvedSuperClasses = new ArrayList<>();

        List<T> result = new LinkedList<>();
        findAllInSupers(result, resolvedSuperClasses);

        if (useInterfaces) {
            findAllInInterfaces(null, result);
            for (AdsClassDef clazz : resolvedSuperClasses) {
                findAllInInterfaces(clazz, result);
            }
        }
        if (result.isEmpty()) {
            return SearchResult.empty();
        } else {
            return SearchResult.list(result);
        }
    }

    @Override
    public SearchResult<T> findOverwritten() {
        if (member.getOwnerClass() == null) {
            return SearchResult.empty();
        }
        List<AdsClassDef> ovrClasses = new LinkedList<>();

        member.getOwnerClass().getHierarchy().findOverwritten().save(ovrClasses);
        final Set<AdsClassDef> looked = new HashSet<>();

        List<T> resultSet = new LinkedList<>();
        findInClasses(ovrClasses, resultSet, looked, EScope.LOCAL);
        if (resultSet.isEmpty()) {
            return SearchResult.empty();
        } else {
            return SearchResult.list(resultSet);
        }
    }

    private void findInClasses(List<AdsClassDef> ovrs, List<T> resultSet, final Set<AdsClassDef> looked, EScope scope) {
        List<AdsClassDef> searchNext = null;
        for (AdsClassDef ovr : ovrs) {
            if (looked.contains(ovr)) {
                continue;
            }
            looked.add(ovr);
            T ovr_member = findMember(ovr, member.getId(), scope);
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
                findInClasses(newOvrs, resultSet, looked, scope);
            }
        }
    }

    protected abstract T findMember(AdsClassDef clazz, Id id, EScope scope);
}
