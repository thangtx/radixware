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

import java.util.Collections;
import java.util.Iterator;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.EntireChangesSupport;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.ClassDefinition.Presentations;
import org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog;


class ClassCatalogs extends ExtendableEntityPresentations<AdsClassCatalogDef> {

    ClassCatalogs(EntityObjectPresentations owner, ClassDefinition.Presentations xClass) {
        super(owner, xClass);
        EntireChangesSupport support = EntireChangesSupport.getInstance(AdsClassCatalogDef.class);
        getLocal().getContainerChangesSupport().addEventListener(support);
        support.fireChange();
    }

    @Override
    protected void loadFrom(ClassDefinition.Presentations xPresentations) {
        ClassDefinition.Presentations.ClassCatalogs xCatalogs = xPresentations.getClassCatalogs();
        if (xCatalogs != null) {
            List<ClassCatalog> sortings = xCatalogs.getClassCatalogList();
            if (sortings != null && !sortings.isEmpty()) {
                for (ClassCatalog s : sortings) {
                    getLocal().add(AdsClassCatalogDef.Factory.loadFrom(s));
                }
            }
        }
    }

    @Override
    protected ExtendableDefinitions<AdsClassCatalogDef> findInstance(EntityObjectPresentations prs) {
        return prs.getClassCatalogs();
    }

    @Override
    void appendTo(Presentations xDef, ESaveMode saveMode) {
        if (!getLocal().isEmpty()) {
            AdsEntityObjectClassDef clazz = getOwnerClass();
            if (clazz == null || clazz.isPolymorphic()) {
                Presentations.ClassCatalogs set = xDef.addNewClassCatalogs();
                for (AdsClassCatalogDef s : getLocal()) {
                    if (saveMode == ESaveMode.API && !s.isPublished()) {
                        continue;
                    }
                    s.appendTo(set.addNewClassCatalog(), saveMode);
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Class Catalogs";
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_CATALOG;
    }

    @Override
    public AdsEntityObjectClassDef getOwnerClass() {
        return (AdsEntityObjectClassDef) super.getOwnerClass();
    }

//    private static class CCLocal extends ExtendablePresentationsLocal<AdsClassCatalogDef> {
//
//        private CCLocal() {
//        }
//
//        @Override
//        public AdsClassCatalogDef findById(Id id) {
//            AdsEntityObjectClassDef clazz = getOwnerClass();
//            if (clazz != null && clazz.isPolymorphic()) {
//                return null;
//            }
//            return super.findById(id);
//        }
//
//        @Override
//        public boolean isEmpty() {
//            AdsEntityObjectClassDef clazz = getOwnerClass();
//            if (clazz != null && clazz.isPolymorphic()) {
//                return true;
//            }
//            return super.isEmpty();
//        }
//
//        @Override
//        public Iterator<AdsClassCatalogDef> iterator() {
//            AdsEntityObjectClassDef clazz = getOwnerClass();
//            if (clazz != null && clazz.isPolymorphic()) {
//                return new Iterator<AdsClassCatalogDef>() {
//
//                    @Override
//                    public boolean hasNext() {
//                        return false;
//                    }
//
//                    @Override
//                    public AdsClassCatalogDef next() {
//                        return null;
//                    }
//
//                    @Override
//                    public void remove() {
//                    }
//                };
//            }
//            return super.iterator();
//        }
//
//        @Override
//        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
//            AdsEntityObjectClassDef clazz = getOwnerClass();
//            if (clazz != null && clazz.isPolymorphic()) {
//                return;
//            } else {
//                super.visitChildren(visitor, provider);
//            }
//        }
//
//        @Override
//        public List<AdsClassCatalogDef> list() {
//            AdsEntityObjectClassDef clazz = getOwnerClass();
//            if (clazz != null && clazz.isPolymorphic()) {
//                return Collections.emptyList();
//            }
//            return super.list();
//        }
//
//        @Override
//        public List<AdsClassCatalogDef> list(IFilter<AdsClassCatalogDef> filter) {
//            AdsEntityObjectClassDef clazz = getOwnerClass();
//            if (clazz != null && clazz.isPolymorphic()) {
//                return Collections.emptyList();
//            }
//            return super.list(filter);
//        }
//
//        @Override
//        public int size() {
//            AdsEntityObjectClassDef clazz = getOwnerClass();
//            if (clazz != null && clazz.isPolymorphic()) {
//                return 0;
//            }
//            return super.size();
//        }
//
//        AdsEntityObjectClassDef getOwnerClass() {
//            return (AdsEntityObjectClassDef) getOwnerDefinition();
//        }
//    }
    @Override
    public Definitions<AdsClassCatalogDef> getLocal() {
        return super.getLocal();
    }
}
