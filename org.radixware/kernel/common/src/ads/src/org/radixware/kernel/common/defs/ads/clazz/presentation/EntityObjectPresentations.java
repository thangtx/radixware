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

import java.util.Collection;
import java.util.EnumSet;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EEntityPresentationInheritance;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;

/**
 * Набор презентационных компонентов классв слоя ADS
 *
 */
public class EntityObjectPresentations extends EntityBasedPresentations  {

    private final Filters filters;
    private final Sortings sortings;
    private final EditorPresentations editorPresentations;
    private final SelectorPresentations selectorPresentations;
    private final ClassCatalogs classCatalogs;
    private Id objectTitleId;
    protected EnumSet<EEntityPresentationInheritance> inheritanceMask;
    private boolean searchTitleInherit = true;
    
    public EntityObjectPresentations(AdsEntityObjectClassDef owner, ClassDefinition.Presentations xDef) {
        super(owner, xDef);
        this.filters = new Filters(this, xDef);
        this.sortings = new Sortings(this, xDef);
        if(this.inheritanceMask == null){
             this.inheritanceMask = EnumSet.noneOf(EEntityPresentationInheritance.class);
        }
        this.editorPresentations = new EditorPresentations(this, xDef);
        this.selectorPresentations = new SelectorPresentations(this, xDef);
        this.classCatalogs = new ClassCatalogs(this, xDef);
        this.objectTitleId = xDef == null ? null : 
                xDef.getObjectTitleId() == null ? null:Id.Factory.loadFrom(xDef.getObjectTitleId());
    }

    public EntityObjectPresentations(AdsEntityObjectClassDef owner) {
        super(owner);
        this.filters = new Filters(this, null);
        this.sortings = new Sortings(this, null);
        this.editorPresentations = new EditorPresentations(this, null);
        this.selectorPresentations = new SelectorPresentations(this, null);
        this.classCatalogs = new ClassCatalogs(this, null);
        this.objectTitleId = null;
        this.inheritanceMask = EnumSet.noneOf(EEntityPresentationInheritance.class);
    }

    @Override
    public void appendTo(ClassDefinition.Presentations xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        this.selectorPresentations.appendTo(xDef, saveMode);
        this.editorPresentations.appendTo(xDef, saveMode);
        this.filters.appendTo(xDef, saveMode);
        this.sortings.appendTo(xDef, saveMode);
        this.classCatalogs.appendTo(xDef, saveMode);
        if (this.objectTitleId != null) {
            xDef.setObjectTitleId(this.objectTitleId.toString());
        }
    }

    public ExtendableDefinitions<AdsEditorPresentationDef> getEditorPresentations() {
        return editorPresentations;
    }

    public ExtendableDefinitions<AdsFilterDef> getFilters() {
        return filters;
    }

    public ExtendableDefinitions<AdsSelectorPresentationDef> getSelectorPresentations() {
        return selectorPresentations;
    }

    public ExtendableDefinitions<AdsSortingDef> getSortings() {
        return sortings;
    }

    public ExtendableDefinitions<AdsClassCatalogDef> getClassCatalogs() {
        return classCatalogs;
    }
    
    public Id getObjectTitleId() {
        return getObjectTitleId(isObjectTitleInherited());
    }

    public Id getObjectTitleId(boolean inherited) {
        if (inherited) {
            AdsEntityObjectClassDef def = findOwnerTitleDefinition();
            if (def != null) {
                return def.getPresentations().getObjectTitleId();
            }
            return null;
        }
        return objectTitleId;
    }
    
    public AdsEntityObjectClassDef findOwnerTitleDefinition() {
        if (isObjectTitleInherited()) {
            
            AdsEntityObjectClassDef ovr = findOverwrittenTitleOwner();
            if (ovr != null) {
                return ovr;
            }
            AdsEntityObjectClassDef def = getOwnerClass().findBasis();
            if (def != null) {
                return def.getPresentations().findOwnerTitleDefinition();
            }            
        }
        return getOwnerClass();
    }

    public void setObjectTitleId(Id id) {
        if (isObjectTitleInherited()) {
            return;
        }
        
        objectTitleId = id;
        setEditState(EEditState.MODIFIED);
    }

    /**
     * Returns object title for the class
     *
     */
    public String getObjectTitle(EIsoLanguage language) {
        return findOwnerTitleDefinition().getLocalizedStringValue(language, getObjectTitleId());
    }

    /**
     * Updates object title
     *
     */
    public boolean setObjectTitle(EIsoLanguage language, String value) {
//        if (getOwnerClass().getHierarchy().findOverwriteBase() != this.getOwnerClass()) {
//            throw new RadixObjectError("Modification of object title is not allowed for owerwriting class presentations.", this);
//
//        }
        if (isObjectTitleInherited()) {
            return false;
        }
        this.objectTitleId = getOwnerClass().setLocalizedStringValue(language, objectTitleId, value);
        setEditState(EEditState.MODIFIED);
        return this.objectTitleId != null;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.editorPresentations.visit(visitor, provider);
        this.selectorPresentations.visit(visitor, provider);
        this.filters.visit(visitor, provider);
        this.sortings.visit(visitor, provider);
        this.classCatalogs.visit(visitor, provider);
    }

    @Override
    protected void cleanup() {
        super.cleanup();
        this.editorPresentations.getLocal().clear();
        this.editorPresentations.getLocal().clear();
        this.selectorPresentations.getLocal().clear();
        this.filters.getLocal().clear();
        this.sortings.getLocal().clear();
        this.classCatalogs.getLocal().clear();        
        this.objectTitleId = null;
        this.inheritanceMask = EnumSet.allOf(EEntityPresentationInheritance.class);
    }
    
    private AdsEntityObjectClassDef findOverwrittenTitleOwner() {
        AdsClassDef classDef = getOwnerClass().getHierarchy().findOverwritten().get();
        while (classDef != null) {
            if (classDef instanceof AdsEntityObjectClassDef) {
                AdsEntityObjectClassDef adsEntityObjectClassDef = (AdsEntityObjectClassDef) classDef;
                if (!adsEntityObjectClassDef.isTitleInherited()) {
                    return adsEntityObjectClassDef;
                }
            }
            classDef = classDef.getHierarchy().findOverwritten().get();
        }
        return null;
    }
    
    //for writer    
    public boolean getIsObjectTitleInherited() {
        AdsEntityObjectClassDef def = findOverwrittenTitleOwner();
        if (def != null) {
            return false;
        }
        return isObjectTitleInherited();
    }
    
    public boolean isObjectTitleInherited(){
        if (searchTitleInherit) {
            AdsEntityObjectClassDef owner = getOwnerClass();
            if (owner ==  null) {
                return inheritanceMask.contains(EEntityPresentationInheritance.SINGULAR_TITLE);
            }
            if (this.objectTitleId == null && (owner.findBasis() != null || (owner.isOverwrite() 
                    || owner.getHierarchy().findOverwritten().get()!= null))) {
                inheritanceMask.add(EEntityPresentationInheritance.SINGULAR_TITLE);
            }
            searchTitleInherit = false;
        }
        return inheritanceMask.contains(EEntityPresentationInheritance.SINGULAR_TITLE);
    }
    
    public boolean setObjectTitleInherited(final boolean inherit){
        boolean isInherit = isObjectTitleInherited();
        if (inherit) {
            if (!isInherit) {
                inheritanceMask.add(EEntityPresentationInheritance.SINGULAR_TITLE);
                this.objectTitleId = null;
                setEditState(EEditState.MODIFIED);
            }
        } else {
            if (isInherit) {
                inheritanceMask.remove(EEntityPresentationInheritance.SINGULAR_TITLE);
                setEditState(EEditState.MODIFIED);
            }
        }
        return true;
    }

    @Override
    public AdsEntityObjectClassDef getOwnerClass() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsEntityObjectClassDef) {
                return (AdsEntityObjectClassDef) owner;
            }
        }
        return null;
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        ids.add(new MultilingualStringInfo(EntityObjectPresentations.this) {
            @Override
            public Id getId() {
                return objectTitleId;
            }

            @Override
            public void updateId(Id newId) {
                objectTitleId = newId;
            }

            @Override
            public EAccess getAccess() {
                return getOwnerClass().getAccessMode();
            }

            @Override
            public String getContextDescription() {
                return "Object Title";
            }

            @Override
            public boolean isPublished() {
                return getOwnerClass().isPublished();
            }
        });
        
    }
}
