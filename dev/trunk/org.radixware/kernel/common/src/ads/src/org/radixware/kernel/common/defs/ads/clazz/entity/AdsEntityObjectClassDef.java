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

package org.radixware.kernel.common.defs.ads.clazz.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ClassPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityObjectPresentations;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.ArrRefType;
import org.radixware.kernel.common.defs.ads.type.ObjectType;
import org.radixware.kernel.common.defs.ads.type.ParentRefType;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.ClassDefinition.Entity;


public abstract class AdsEntityObjectClassDef extends AdsEntityBasedClassDef implements IAdsPresentableClass {

    private List<Id> allowedDetailRefIds;
    private final EntityObjectPresentations presentations;
    private ERuntimeEnvironmentType clientEnvironment;
    public static final Id USER_FUNC_TABLE_ID = Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM");

    protected AdsEntityObjectClassDef(ClassDefinition xDef) {
        super(xDef);
        ClassDefinition.Entity e = xDef.getEntity();
        if (e != null) {
            if (e.getAllowedDetailReferences() != null && !e.getAllowedDetailReferences().isEmpty()) {
                this.allowedDetailRefIds = new ArrayList<Id>(e.getAllowedDetailReferences());
            } else {
                this.allowedDetailRefIds = null;
            }
        } else {
            this.allowedDetailRefIds = null;
        }
        if (xDef.getClientEnvironment() != null && xDef.getClientEnvironment().isClientEnv()) {
            this.clientEnvironment = xDef.getClientEnvironment();
        }
        this.presentations = (EntityObjectPresentations) ClassPresentations.Factory.loadFrom(this, xDef.getPresentations());
    }

    protected AdsEntityObjectClassDef(AdsEntityObjectClassDef source) {
        super(source);
        this.allowedDetailRefIds = null;
        this.presentations = (EntityObjectPresentations) ClassPresentations.Factory.newInstance(this);
        this.clientEnvironment = source.clientEnvironment;
    }

    public AdsEntityObjectClassDef(Id id, String name) {
        super(id, name);
        this.allowedDetailRefIds = null;
        this.presentations = (EntityObjectPresentations) ClassPresentations.Factory.newInstance(this);
    }

    @Override
    protected void appendTo(ClassDefinition xClass, Entity xDef, ESaveMode saveMode) {
        super.appendTo(xClass, xDef, saveMode);
        if (this.allowedDetailRefIds != null) {
            xDef.setAllowedDetailReferences(new ArrayList<Id>(this.allowedDetailRefIds));
        }
        this.presentations.appendTo(xClass.addNewPresentations(), saveMode);
        if (clientEnvironment != null && clientEnvironment != ERuntimeEnvironmentType.COMMON_CLIENT) {
            xClass.setClientEnvironment(clientEnvironment);
        }
    }

    public boolean isPolymorphic() {
        DdsTableDef table = findTable(this);
        if (table != null) {
            return table.getExtOptions().contains(EDdsTableExtOption.ENABLE_APPLICATION_CLASSES);
        } else {
            return true;
        }
    }

    public AdsPresentationEntityAdapterClassDef findPresentationAdapter() {
        AdsModule module = getModule();
        if (module == null) {
            return null;
        } else {
            AdsDefinition def = AdsSearcher.Factory.newAdsDefinitionSearcher(this).findById(Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_PRESENTATION_ENTITY_ADAPTER_CLASS)).get();
            if (def instanceof AdsPresentationEntityAdapterClassDef) {
                return (AdsPresentationEntityAdapterClassDef) def;
            } else {
                return null;
            }
        }
    }

    @Override
    public EntityObjectPresentations getPresentations() {
        return presentations;
    }

    public synchronized boolean isDetailAllowed(Id detailRefId) {
        if (allowedDetailRefIds != null && allowedDetailRefIds.contains(detailRefId)) {
            return true;
        } else {
            return isInheritedDetailAllowed(detailRefId);
        }
    }

    private synchronized boolean isInheritedDetailAllowed(Id detailTableId) {
        AdsEntityObjectClassDef cd = findBasis();
        if (cd != null) {
            return cd.isDetailAllowed(detailTableId);
        } else {
            return false;
        }

    }

    public class DetailReferenceInfo {

        private Id id;

        public Id getReferenceId() {
            return id;
        }

        public DdsReferenceDef findReference() {
            return AdsSearcher.Factory.newDdsReferenceSearcher(AdsEntityObjectClassDef.this).findById(id).get();
        }

        private DetailReferenceInfo(Id id) {
            this.id = id;
        }
    }

    public synchronized boolean setDetailAllowed(Id detailRefId, boolean allow) {
        if (isInheritedDetailAllowed(detailRefId)) {
            return false;
        } else {
            if (allow) {
                if (allowedDetailRefIds != null && allowedDetailRefIds.contains(detailRefId)) {
                    return false;
                }
                if (allowedDetailRefIds == null) {
                    allowedDetailRefIds = new ArrayList<Id>();
                }
                allowedDetailRefIds.add(detailRefId);
                setEditState(EEditState.MODIFIED);
                return true;
            } else {
                if (allowedDetailRefIds == null) {
                    return false;
                } else {
                    if (allowedDetailRefIds.remove(detailRefId)) {
                        setEditState(EEditState.MODIFIED);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    public synchronized List<DetailReferenceInfo> getAllowedDetailRefs() {
        HashSet<Id> ids = new HashSet<Id>();
        ArrayList<DetailReferenceInfo> infos = new ArrayList<DetailReferenceInfo>();
        AdsEntityObjectClassDef clazz = this;
        while (clazz != null) {
            if (clazz.allowedDetailRefIds != null) {
                for (Id id : clazz.allowedDetailRefIds) {
                    if (!ids.contains(id)) {
                        infos.add(new DetailReferenceInfo(id));
                        ids.add(id);
                    }
                }
            }
            clazz = clazz.findBasis();
        }
        return infos;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(Id id) {
        try {
            EDefinitionIdPrefix prefix = id.getPrefix();
            if (prefix == null) {
                return super.findComponentDefinition(id);
            }
            switch (prefix) {
                case COMMAND:
                    return getPresentations().getCommands().findById(id, EScope.ALL);
                case FILTER:
                    return getPresentations().getFilters().findById(id, EScope.ALL);
                case SORTING:
                    return getPresentations().getSortings().findById(id, EScope.ALL);
                case ADS_CREATION_CLASS_CATALOG:
                    return getPresentations().getClassCatalogs().findById(id, EScope.ALL);
                case EDITOR_PRESENTATION:
                    return getPresentations().getEditorPresentations().findById(id, EScope.ALL);
                case SELECTOR_PRESENTATION:
                    return getPresentations().getSelectorPresentations().findById(id, EScope.ALL);
                default:
                    return super.findComponentDefinition(id);
            }
        } catch (NoConstItemWithSuchValueError e) {
            return super.findComponentDefinition(id);
        }

    }

    @Override
    public AdsType getType(EValType typeId, String extStr) {
        switch (typeId) {
            case PARENT_REF:
                return ParentRefType.Factory.newInstance(this);
            case ARR_REF:
                return ArrRefType.Factory.newInstance(this);
            case OBJECT:
                return ObjectType.Factory.newInstance(this);
            default:
                return super.getType(typeId, extStr);
        }
    }

    public Id getEntityAdapterId() {
        return Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_PRESENTATION_ENTITY_ADAPTER_CLASS);
    }

    public boolean isDetailPropsAllowed() {
        return !getAllowedDetailRefs().isEmpty();
    }

    public boolean isUserPropsAllowed() {
        DdsTableDef table = findTable(this);
        if (table != null) {
            return table.getExtOptions().contains(EDdsTableExtOption.SUPPORT_USER_PROPERTIES);
        } else {
            return false;
        }
    }

    public boolean isParentPropsAllowed() {

        for (AdsPropertyDef p : getProperties().get(EScope.ALL)) {
            if (p.getValue().getType().getTypeId() == EValType.PARENT_REF) {
                return true;
            } else {
                AdsType type = p.getValue().getType().resolve(this).get();
                if (type instanceof AdsClassType.EntityObjectType) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getTitle(EIsoLanguage language) {
        return getPresentations().getObjectTitle(language);
    }

    @Override
    public Id getTitleId() {
        return getPresentations().getObjectTitleId();
    }

    @Override
    public boolean setTitle(EIsoLanguage language, String title) {
        return getPresentations().setObjectTitle(language, title);
    }

    @Override
    public void setTitleId(Id id) {
        getPresentations().setObjectTitleId(id);
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.SERVER, ERuntimeEnvironmentType.COMMON_CLIENT);
    }

    public Set<AdsEditorPresentationDef> listPublishableExternals() {
        Set<AdsEditorPresentationDef> result = new HashSet<AdsEditorPresentationDef>();
        collectPublishableExternals(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    private <T extends Definition> void collectPublishableExternals(Collection<T> defs) {
        ExtendableDefinitions<AdsEditorPresentationDef> eprs = getPresentations().getEditorPresentations();
        for (AdsEditorPresentationDef epr : eprs.get(EScope.LOCAL_AND_OVERWRITE)) {
            if (epr.getBasePresentationId() != null) {
                AdsEditorPresentationDef base = getPresentations().getEditorPresentations().findById(epr.getBasePresentationId(), EScope.ALL).get();
                if (base == null) {//external presentation
                    continue;
                } else {
                    if (base.getOwnerClass() != this && base.getOwnerClass().getId() != this.getId()) {
                        defs.add((T) base);
                    }
                }
            }
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        collectPublishableExternals(list);

    }

    @Override
    public boolean isTitleSupported() {
        return false;
    }

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        return clientEnvironment == null ? ERuntimeEnvironmentType.COMMON_CLIENT : clientEnvironment;
    }

    public void setClientEnvironment(ERuntimeEnvironmentType clientEnvironment) {
        if (clientEnvironment != this.clientEnvironment && (clientEnvironment == null || clientEnvironment.isClientEnv())) {
            this.clientEnvironment = clientEnvironment;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    protected void insertToolTipPrefix(StringBuilder sb) {
        super.insertToolTipPrefix(sb);
        String access = getDefaultAccess().getName().toUpperCase().charAt(0) + getDefaultAccess().getName().substring(1, getDefaultAccess().getName().length()); 
        sb.append("<b>").append(access).append(' ').append(getClientEnvironment().getName()).append("</b>&nbsp;");
    }
    
}
