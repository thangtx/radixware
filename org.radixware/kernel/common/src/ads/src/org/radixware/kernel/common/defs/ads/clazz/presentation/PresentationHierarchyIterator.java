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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.naming.spi.DirStateFactory;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.types.Id;


abstract class PresentationHierarchyIterator<T extends AdsPresentationDef> extends HierarchyIterator<T> {

    private List<T> current;
    private List<T> next;
    private final List<AdsEntityObjectClassDef> currentClasses = new ArrayList<>();
    private ExtendableDefinitions<T> init_collection;
    private static final boolean DEBUG = false;
    private Id lookupId;
    private Id baseId;
    private final EScope scope;

    public PresentationHierarchyIterator(T pr, EScope scope, HierarchyIterator.Mode mode) {
        super(mode);
        this.current = Collections.singletonList(pr);
        this.lookupId = pr.getId();
        this.baseId = pr.getBasePresentationId();
        AdsEntityObjectClassDef ownerClass = (AdsEntityObjectClassDef) pr.getOwnerClass();
        if (ownerClass != null) {
            this.currentClasses.add(ownerClass);
        }
        this.next = current;
        this.init_collection = ownerClass == null ? null : getCollectionForClass(ownerClass);
        this.scope = scope;
    }

    @Override
    public boolean hasNext() {
        if (scope != EScope.LOCAL) {
            if (next == null && current != null) {
                if (DEBUG) {
                    //System.out.println("Looking for base presentation of " + current.getQualifiedName() + "...");
                //    System.out.println("Checks overwritten presentations...");
                }
                //first look for overwritten presentation
                final List<T> result = new LinkedList<>();

                while (!currentClasses.isEmpty()) {
                    {
                        final List<AdsEntityObjectClassDef> nextLevelOfOverwrites = new LinkedList<>();
                        for (AdsEntityObjectClassDef clazz : currentClasses) {
                            clazz.getHierarchy().findOverwritten().iterate(new SearchResult.Acceptor<AdsClassDef>() {
                                @Override
                                public void accept(AdsClassDef object) {
                                    if (object instanceof AdsEntityObjectClassDef) {
                                        AdsEntityObjectClassDef c = (AdsEntityObjectClassDef) object;
                                        if (!currentClasses.contains(c) && !nextLevelOfOverwrites.contains(c)) {
                                            nextLevelOfOverwrites.add(c);
                                        }
                                    }
                                }
                            });
                        }
                        currentClasses.clear();
                        if (!nextLevelOfOverwrites.isEmpty()) {
                            currentClasses.addAll(nextLevelOfOverwrites);
                        }
                    }
                    for (AdsEntityObjectClassDef clazz : currentClasses) {
                        T ovr = getCollectionForClass((AdsEntityObjectClassDef) clazz).getLocal().findById(lookupId);
                        if (ovr != null && !result.contains(ovr) && !current.contains(ovr)) {
                            result.add(ovr);
                        }
                    }

                    if (!result.isEmpty()) {

                        break;
                    }
                }

                if (result.isEmpty()) {//nothing was found in overwrite hierarchy                    
                    if (scope == EScope.ALL) {
                        if (baseId != null) {
                            lookupId = baseId;
                            baseId = null;
                            if (init_collection == null) {
                                next = null;
                            } else {
                                init_collection.findById(lookupId, scope).iterate(new SearchResult.Acceptor<T>() {
                                    @Override
                                    public void accept(T object) {
                                        if (!result.contains(object) && !current.contains(object)) {
                                            result.add(object);
                                        }
                                    }
                                });
                                if (result.isEmpty()) {
                                    next = null;
                                } else {
                                    currentClasses.clear();
                                    for (T obj : result) {
                                        if (baseId == null) {
                                            baseId = obj.getBasePresentationId();
                                            if (DEBUG) {
                                                System.out.println("Base presentation found: " + obj.getQualifiedName() + " " + obj.getId());
                                            }
                                        }
                                        currentClasses.add(obj.getOwnerClass());
                                    }
                                    next = new ArrayList<>(result);
                                }
                            }
                        } else {
                            next = null;
                        }
                    } else {
                        next = null;
                    }
                } else {
                    next = new ArrayList<>(result);
                }
            }
        }

        return next != null;
    }

    @Override
    public Chain<T> next() {
        if (hasNext()) {
            current = next;
            next = null;
            return Chain.newInstance(current);
        } else {
            return Chain.empty();
        }
    }

    public abstract ExtendableDefinitions<T> getCollectionForClass(AdsEntityObjectClassDef c);
}
