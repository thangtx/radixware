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

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.types.Id;


public class ObjectPropertyPresentation extends PropertyPresentation implements CreatePresentationsList.ICreatePresentationListOwner {

    private volatile Id objectClassCatalogId;
    private final CreatePresentationsList createPresentationsList;

    public ObjectPropertyPresentation(final AdsPropertyDef property, final PropertyPresentation source, final boolean forOverride) {
        super(property, source, forOverride);
        if (source instanceof ObjectPropertyPresentation) {
            this.objectClassCatalogId = ((ObjectPropertyPresentation) source).objectClassCatalogId;
            createPresentationsList = CreatePresentationsList.Factory.newCopy(this, ((ObjectPropertyPresentation) source).createPresentationsList);
        } else {
            createPresentationsList = CreatePresentationsList.Factory.newInstance(this);
        }
    }

    public ObjectPropertyPresentation(final AdsPropertyDef property, final org.radixware.schemas.adsdef.PropertyPresentation xPres) {
        super(property, xPres);
        if (xPres != null && xPres.getObject() != null) {
            this.objectClassCatalogId = Id.Factory.loadFrom(xPres.getObject().getObjectClassCatalogId());
            createPresentationsList = CreatePresentationsList.Factory.loadFrom(this, xPres.getObject());
        } else {
            createPresentationsList = CreatePresentationsList.Factory.newInstance(this);
        }

    }

    /**
     * Returns creation class catalog id (with inheritance support)
     */
    public Id getCreationClassCatalogId() {
        return objectClassCatalogId;
    }
    

    public boolean setCreationClassCatalogId(final Id id) {
        objectClassCatalogId = id;
        setEditState(EEditState.MODIFIED);
        return true;
    }

    public SearchResult<AdsClassCatalogDef> findCreationClassCatalog() {
        final EntityObjectPresentations prs = findReferencedPresentations();
        if (prs != null) {
            return prs.getClassCatalogs().findById(objectClassCatalogId, EScope.ALL);
        } else {
            return SearchResult.<AdsClassCatalogDef>empty();
        }
    }

    public void appendTo(final org.radixware.schemas.adsdef.PropertyPresentation.Object xDef) {
        if (objectClassCatalogId != null) {
            xDef.setObjectClassCatalogId(this.objectClassCatalogId.toString());
        }
        if (!createPresentationsList.isEmpty()) {
            createPresentationsList.appendTo(xDef);
        }
    }

    @Override
    public void appendTo(final org.radixware.schemas.adsdef.PropertyPresentation xPres, final ESaveMode saveMode) {
        super.appendTo(xPres, saveMode);
        if (saveMode == ESaveMode.NORMAL) { 
            if (objectClassCatalogId != null || !createPresentationsList.isEmpty()) {
                appendTo(xPres.addNewObject());
            }
        }

    }

//    private SearchResult<AdsEditorPresentationDef> findEditorPesentation(final Id id) {
//        if (id == null) {
//            return SearchResult.<AdsEditorPresentationDef>empty();
//        } else {
//            final EntityObjectPresentations prs = findReferencedPresentations();
//            if (prs != null) {
//                return prs.getEditorPresentations().findById(id, EScope.ALL);
//            } else {
//                return SearchResult.<AdsEditorPresentationDef>empty();
//            }
//        }
//    }

    @Override
    public void collectDependences(final List<Definition> list) {
        this.collectDependences(false, list);
    }

    private void collectDependences(final boolean direct, final List<Definition> list) {
        super.collectDependences(list);
        if (isPresentable()) {
            final SearchResult<AdsClassCatalogDef> cc = findCreationClassCatalog();
            if (!cc.isEmpty()) {
                if (direct) {
                    list.addAll(cc.all());
                } else {
                    for (AdsClassCatalogDef c : cc.all()) {
                        for (AdsClassCatalogDef r : c.getAll()) {
                            if (!list.contains(r)) {
                                list.add(r);
                            }
                        }
                    }
                }
            }
//            final AdsEditorPresentationDef e = findObjectCreationEditorPresentation();
//            if (e != null) {
//                list.add(e);
//            }
        }
    }

    @Override
    public void collectDirectDependences(final List<Definition> list) {
        this.collectDependences(true, list);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        createPresentationsList.visit(visitor, provider);
    }

    @Override
    public CreatePresentationsList getCreatePresentationsList() {
        return createPresentationsList;
    }
}
