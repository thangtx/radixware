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
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EEntityPresentationInheritance;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;


public class EntityPresentations extends EntityObjectPresentations {

    private Id entityTitleId;
    private Id defaultSelectorPresentationId;
    private Restrictions restrictions;
    private AdsObjectTitleFormatDef objectTitleFormat;
    private Id iconId;
   

    EntityPresentations(AdsEntityClassDef owner, ClassDefinition.Presentations xDef) {
        super(owner, xDef);
        this.entityTitleId = xDef == null ? null : xDef.getEntityTitleId() == null? null : Id.Factory.loadFrom(xDef.getEntityTitleId());
        if (this.entityTitleId == null){
            inheritanceMask.add(EEntityPresentationInheritance.PLURAL_TITLE);
        }
        this.defaultSelectorPresentationId = xDef == null ? null : Id.Factory.loadFrom(xDef.getDefaultSelectorPresentationId());
        this.restrictions = Restrictions.Factory.newInstance(this, xDef == null ? 0 : xDef.getRestrictions());
        
        this.objectTitleFormat = xDef == null ? 
                    AdsObjectTitleFormatDef.Factory.newInstance(owner) : xDef.getObjectTitleFormat() == null? null : AdsObjectTitleFormatDef.Factory.loadFrom(owner, xDef.getObjectTitleFormat());
        if (this.objectTitleFormat == null){
            inheritanceMask.add(EEntityPresentationInheritance.OBJECT_TITLE_FORMAT);
        }
        this.iconId = xDef == null ? null : Id.Factory.loadFrom(xDef.getIconId());
        
    }

    EntityPresentations(AdsEntityClassDef owner) {
        super(owner);
        this.entityTitleId = null;
        this.defaultSelectorPresentationId = null;
        this.restrictions = Restrictions.Factory.newInstance(this, 0L);
        this.objectTitleFormat = AdsObjectTitleFormatDef.Factory.newInstance(owner);
        this.iconId = null;

    }

    EntityPresentations(AdsEntityClassDef owner, EntityPresentations source) {
        super(owner);
        this.defaultSelectorPresentationId = source.defaultSelectorPresentationId;
        this.restrictions = source.restrictions;
        this.objectTitleFormat = AdsObjectTitleFormatDef.Factory.newInstance(owner);
        this.iconId = source.iconId;
        this.entityTitleId = null;
    }

    @Override
    public void appendTo(ClassDefinition.Presentations xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (this.entityTitleId != null) {
            xDef.setEntityTitleId(this.entityTitleId.toString());
        }
        if (saveMode == ESaveMode.NORMAL || saveMode == ESaveMode.API) {
            if (this.defaultSelectorPresentationId != null) {
                xDef.setDefaultSelectorPresentationId(this.defaultSelectorPresentationId.toString());
            }
        }
        if (iconId != null) {
            xDef.setIconId(this.iconId.toString());
        }

        xDef.setRestrictions(ERestriction.toBitField(this.restrictions.getRestriction()));
        
        if (this.objectTitleFormat != null) {
            this.objectTitleFormat.appendTo(xDef.addNewObjectTitleFormat(), saveMode);
        }
        
    }
    
    /**
     * Returns entity title format for the class; if current instance owerwrites
     * another class's presentations title format of owerwritten class will
     * return
     */
    public AdsObjectTitleFormatDef getObjectTitleFormat() {
        if (isObjectTitleFormatInherited()) {
                AdsClassDef adsClassDef = getOwnerClass().getHierarchy().findOverwritten().get();
                    if (adsClassDef instanceof AdsEntityClassDef){
                        EntityPresentations entityPresentations = ((AdsEntityClassDef) adsClassDef).getPresentations();
                        return entityPresentations.getObjectTitleFormat(); 
                    } else {
                        return AdsObjectTitleFormatDef.Factory.newInstance(getOwnerClass()).unmodifiableInstance();
                    }     
        } else {
            return this.objectTitleFormat;
        }
    }


    public boolean isObjectTitleFormatInherited (){
        return inheritanceMask.contains(EEntityPresentationInheritance.OBJECT_TITLE_FORMAT);
    }
    
    public boolean setTitleFormatInherited(final boolean inherit) {
        boolean isInherit = isObjectTitleFormatInherited();
        if (inherit) {
            if (!isInherit) {
                objectTitleFormat = null;
                inheritanceMask.add(EEntityPresentationInheritance.OBJECT_TITLE_FORMAT);
                setEditState(EEditState.MODIFIED);
                return true;
            }
        } else {
            if (isInherit) {
                final AdsObjectTitleFormatDef titleFormat = getObjectTitleFormat();
                if (titleFormat != null) {
                    objectTitleFormat = AdsObjectTitleFormatDef.Factory.newCopy(getOwnerClass(), titleFormat);
                } else {
                    objectTitleFormat = AdsObjectTitleFormatDef.Factory.newInstance(getOwnerClass());
                }
                inheritanceMask.remove(EEntityPresentationInheritance.OBJECT_TITLE_FORMAT);
                setEditState(EEditState.MODIFIED);
            }
        }
        return true;
    }

    /**
     * Returns id of default selector presentation
     */
    public Id getDefaultSelectorPresentationId() {
        return defaultSelectorPresentationId;
    }

    public AdsSelectorPresentationDef findDefaultSelectorPresentation() {
        if (defaultSelectorPresentationId != null) {
            return getSelectorPresentations().findById(defaultSelectorPresentationId, EScope.ALL).get();
        } else {
            return null;
        }
    }

    /**
     * Sets id of default selector presentation
     */
    public boolean setDefaultSelectorPresentationId(Id defaultSelectorPresentationId) {
        this.defaultSelectorPresentationId = defaultSelectorPresentationId;
        setEditState(EEditState.MODIFIED);
        return true;
    }
    
    /**
     * Returns restrictions binmask
     */
    public Restrictions getRestrictions() {
        return ((AdsEntityClassDef) getOwnerClass().getHierarchy().findOverwriteBase().get()).getPresentations().restrictions;
    }

    /**
     * Returns icon id for the class if current instance owerwrites another
     * class's presentations icon id of owerwritten class will return
     */
    public Id getIconId() {
        return ((AdsEntityClassDef) getOwnerClass().getHierarchy().findOverwriteBase().get()).getPresentations().iconId;
    }

    /**
     * Updates class icon id
     *
     * @throws {@linkplain DefinitionError} if current class overwrites another
     * class
     */
    public boolean setIconId(Id iconId) {
        if (getOwnerClass().getHierarchy().findOverwriteBase().get() != this.getOwnerClass()) {
            throw new RadixObjectError("Modification of icon id is not allowed for owerwriting class presentations.", this);

        }
        this.iconId = iconId;
        setEditState(EEditState.MODIFIED);
        return true;
    }

    public Id getEntityTitleId() {
        if (isEntityTitleInherit()) {
            AdsClassDef clazz = getOwnerClass().getHierarchy().findOverwritten().get();
            if (clazz instanceof AdsEntityClassDef) {
                return ((AdsEntityClassDef) clazz).getPresentations().getEntityTitleId();
            } else {
                return null;
            }
        }
        return entityTitleId;
    }

    public void setEntityTitleId(Id id) {
        if (isEntityTitleInherit()) {
            return;
        }
        
        entityTitleId = id;
        setEditState(EEditState.MODIFIED);
    }
    
    public boolean isEntityTitleInherit(){
        return inheritanceMask.contains(EEntityPresentationInheritance.PLURAL_TITLE);
    }
    
    public boolean setEntityTitleInherited(final boolean inherit){
        boolean isInherit = isEntityTitleInherit();
        if (inherit) {
            if (!isInherit) {
                inheritanceMask.add(EEntityPresentationInheritance.PLURAL_TITLE);
                this.entityTitleId = null;
                setEditState(EEditState.MODIFIED);
            }
        } else {
            if (isInherit) {
                inheritanceMask.remove(EEntityPresentationInheritance.PLURAL_TITLE);
                setEditState(EEditState.MODIFIED);
            }
        }
        return true;
    }

    /**
     * Returns class title for the class if current instance owerwrites another
     * class's presentations class title of owerwritten class will return
     */
    public String getEntityTitle(EIsoLanguage language) {
        return getOwnerClass().getLocalizedStringValue(language, getEntityTitleId());
    }

    /**
     * Updates class title
     *
     * @throws {@linkplain DefinitionError} if current class overwrites another
     * class
     */
    public boolean setEntityTitle(EIsoLanguage language, String value) {
//        if (getOwnerClass().getHierarchy().findOverwriteBase().get() != this.getOwnerClass()) {
//            throw new RadixObjectError("Modification of entity title is not allowed for owerwriting class presentations.", this);
//
//        }
        if (isEntityTitleInherit()) {
            return false;
        }
        this.entityTitleId = getOwnerClass().setLocalizedStringValue(language, entityTitleId, value);
        setEditState(EEditState.MODIFIED);
        return this.entityTitleId != null;
    }

    @Override
    public AdsEntityClassDef getOwnerClass() {
        return (AdsEntityClassDef) super.getOwnerClass();
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (objectTitleFormat != null){
            objectTitleFormat.visit(visitor, provider);
        }
        if (restrictions != null) {
            restrictions.visit(visitor, provider);
        }
    }
    
    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        super.collectUsedMlStringIds(ids);
        ids.add(new MultilingualStringInfo(EntityPresentations.this) {

            @Override
            public Id getId() {
                return entityTitleId;
            }

            @Override
            public void updateId(Id newId) {
                entityTitleId = newId;
            }

            @Override
            public EAccess getAccess() {
                return getOwnerClass().getAccessMode();
            }

            @Override
            public String getContextDescription() {
                return "Entity Title";//getTypeTitle().concat(" Title");
            }

            @Override
            public boolean isPublished() {
                return getOwnerClass().isPublished();
            }
        });
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (iconId != null) {
            AdsImageDef image = AdsSearcher.Factory.newImageSearcher(getOwnerClass()).findById(iconId).get();
            if (image != null) {
                list.add(image);
            }
        }
    }

    @Override
    protected void cleanup() {
        super.cleanup();
        this.entityTitleId = null;
    }
    
    
}
