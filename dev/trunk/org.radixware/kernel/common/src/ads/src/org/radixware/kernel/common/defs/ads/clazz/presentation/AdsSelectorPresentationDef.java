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

import java.lang.reflect.Method;
import java.util.*;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.*;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.common.AdsCondition;
import org.radixware.kernel.common.defs.ads.common.IConditionProvider;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable.CustomViewSupport;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.radixdoc.SelectorPresentationRadixdoc;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.presentation.AdsSelectorPresentationWriter;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomSelectorDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomSelectorDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.SelectorPresentationDefinition;
import org.radixware.schemas.radixdoc.Page;


public class AdsSelectorPresentationDef extends AdsPresentationDef implements IJavaSource, ICustomViewable<AdsSelectorPresentationDef, AdsAbstractUIDef>, CreatePresentationsList.ICreatePresentationListOwner, IClientDefinition, IConditionProvider, IRadixdocProvider {

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsSelectorPresentationWriter(this, AdsSelectorPresentationDef.this, purpose);
            }

            @Override
            public Set<JavaSourceSupport.CodeType> getSeparateFileTypes(ERuntimeEnvironmentType sc) {

                ERuntimeEnvironmentType ownEnv = AdsSelectorPresentationDef.this.getClientEnvironment();
                if (ownEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    return sc == ERuntimeEnvironmentType.EXPLORER || sc == ERuntimeEnvironmentType.WEB ? EnumSet.of(CodeType.META) : null;
                } else {
                    if (ownEnv == sc) {
                        return EnumSet.of(CodeType.META);
                    } else {
                        return null;
                    }
                }

            }
        };
    }

    public static final class Factory {

        public static AdsSelectorPresentationDef newInstance() {
            return new AdsSelectorPresentationDef();
        }

        public static AdsSelectorPresentationDef newSelectorPresentationForUser2Role() {
            return new AdsSelectorPresentationDef(Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI"));
        }

        public static AdsSelectorPresentationDef newSelectorPresentationForUserGroup2Role() {
            return new AdsSelectorPresentationDef(Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"));
        }

        public static AdsSelectorPresentationDef loadFrom(SelectorPresentationDefinition xSpr) {
            return new AdsSelectorPresentationDef(xSpr);
        }
    }

    public static class SelectorColumn extends PropertyUsage implements ILocalizedDef {

        @Override
        public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
            ids.add(new MultilingualStringInfo(this) {
                @Override
                public Id getId() {
                    return titleId;
                }

                @Override
                public void updateId(Id newId) {
                    titleId = newId;
                    setEditState(EEditState.MODIFIED);
                }

                @Override
                public EAccess getAccess() {
                    return EAccess.PRIVATE;
                }

                @Override
                public String getContextDescription() {
                    return "Column Title";
                }

                @Override
                public boolean isPublished() {
                    return true;
                }
            });
        }

        @Override
        public AdsMultilingualStringDef findLocalizedString(Id stringId) {
            AdsClassDef clazz = getOwnerClass();
            return clazz == null ? null : clazz.findLocalizedString(stringId);
        }

        public static final class Factory {

            public static SelectorColumn newInstance(IAdsPresentableProperty prop) {
                return new SelectorColumn((AdsPropertyDef) prop);
            }
        }
        private ESelectorColumnAlign align;
        private Id titleId;
        private ESelectorColumnVisibility visibility;
        private ESelectorColumnSizePolicy sizePolicy = ESelectorColumnSizePolicy.MANUAL_RESIZE;

        private SelectorColumn(SelectorPresentationDefinition.SelectorColumns.Column xDef) {
            super(xDef.getPropId());
            this.align = xDef.getAlign();
            this.titleId = xDef.getTitleId();
            this.visibility = xDef.getVisibility();
            if (xDef.isSetSizePolicy()) {
                this.sizePolicy = xDef.getSizePolicy();
            }
        }

        private SelectorColumn(AdsSelectorPresentationDef owner, SelectorColumn source) {
            super(source.getPropertyId());
            this.align = source.align;
            this.titleId = null;
            if (source.titleId != null) {
                IMultilingualStringDef string = source.getOwnerSelectorPresentation().findLocalizedString(source.titleId);
                if (string != null) {
                    IMultilingualStringDef clone = string.cloneString(owner.findLocalizingBundle());
                    titleId = clone.getId();
                }
            }
            this.visibility = source.visibility;
        }

        private SelectorColumn(AdsPropertyDef prop) {
            super(prop);
            this.align = ESelectorColumnAlign.DEFAULT;
            this.titleId = null;
            this.visibility = ESelectorColumnVisibility.INITIAL;
            this.sizePolicy = ESelectorColumnSizePolicy.AUTO;
        }

        public String getTitle(EIsoLanguage language) {
            return getOwnerSelectorPresentation().getLocalizedStringValue(language, titleId);
        }

        public boolean setTitle(EIsoLanguage language, String value) {
            Id id = getOwnerSelectorPresentation().setLocalizedStringValue(language, titleId, value);
            if (id != null) {
                this.titleId = id;
                setEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        }

        public AdsSelectorPresentationDef getOwnerSelectorPresentation() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof AdsSelectorPresentationDef) {
                    return (AdsSelectorPresentationDef) owner;
                }
            }
            return null;
        }

        public ESelectorColumnSizePolicy getSizePolicy() {
            return sizePolicy;
        }

        public void setSizePolicy(ESelectorColumnSizePolicy sizePolicy) {
            if (sizePolicy != this.sizePolicy) {
                this.sizePolicy = sizePolicy;
                setEditState(EEditState.MODIFIED);
            }
        }

        @Override
        public AdsClassDef getOwnerClass() {
            AdsSelectorPresentationDef def = getOwnerSelectorPresentation();
            return def == null ? null : def.getOwnerClass();
        }

        private void appendTo(SelectorPresentationDefinition.SelectorColumns.Column xDef) {
            xDef.setAlign(align);
            xDef.setPropId(propertyId);
            xDef.setTitleId(titleId);
            xDef.setVisibility(visibility);
            if (this.sizePolicy != ESelectorColumnSizePolicy.MANUAL_RESIZE) {
                xDef.setSizePolicy(this.sizePolicy);
            }
        }

        public ESelectorColumnVisibility getVisibility() {
            return visibility;
        }

        public Id getTitleId() {
            return titleId;
        }

        public void setTitleId(Id id) {
            this.titleId = id;
            setEditState(EEditState.MODIFIED);
        }

        public void setVisibility(ESelectorColumnVisibility visibility) {
            this.visibility = visibility;
            this.setEditState(EEditState.MODIFIED);
        }

        public ESelectorColumnAlign getAlign() {
            return align;
        }

        public void setAlign(ESelectorColumnAlign align) {
            this.align = align;
            setEditState(EEditState.MODIFIED);
        }
    }

    private class SelectorColumns extends RadixObjects<SelectorColumn> {

        private SelectorColumns(SelectorPresentationDefinition.SelectorColumns xDef) {
            super(AdsSelectorPresentationDef.this);
            if (xDef != null) {
                List<SelectorPresentationDefinition.SelectorColumns.Column> xColumns = xDef.getColumnList();
                if (xColumns != null && !xColumns.isEmpty()) {
                    for (SelectorPresentationDefinition.SelectorColumns.Column column : xColumns) {
                        this.add(new SelectorColumn(column));
                    }
                }
            }
        }

        private void appendTo(SelectorPresentationDefinition xDef) {
            if (!isEmpty()) {
                SelectorPresentationDefinition.SelectorColumns cs = xDef.addNewSelectorColumns();
                for (SelectorColumn c : this) {
                    c.appendTo(cs.addNewColumn());
                }
            }
        }
    }

    public static class EditorPresentations extends RadixObject {

        public class PresentationInfo {

            private final Id id;

            private PresentationInfo(Id id) {
                this.id = id;
            }

            public final Id getId() {
                return id;
            }

            public final AdsEditorPresentationDef findPresentation() {
                return getOwnerSelectorPresentation().getOwnerPresentations().getEditorPresentations().findById(id, EScope.ALL).get();
            }
        }
        private List<Id> ids;

        private EditorPresentations(AdsSelectorPresentationDef spr, List<Id> ids) {
            this.ids = ids == null ? null : new ArrayList<>(ids);
            setContainer(spr);
        }

        public List<Id> getIds() {
            synchronized (this) {
                return ids == null ? new ArrayList<Id>() : new ArrayList<>(ids);
            }
        }

        public List<PresentationInfo> getPresentationInfos() {
            synchronized (this) {
                if (ids == null) {
                    return Collections.emptyList();
                } else {
                    ArrayList<PresentationInfo> infos = new ArrayList<>(ids.size());
                    for (Id id : ids) {
                        infos.add(new PresentationInfo(id));
                    }
                    return infos;
                }
            }
        }

        public boolean moveDown(Id id) {
            if (ids == null) {
                return false;
            }
            int index = ids.indexOf(id);
            if (index < 0) {
                return false;
            }
            if (index >= ids.size()) {
                return false;
            }
            Id next = ids.get(index + 1);
            ids.set(index, next);
            ids.set(index + 1, id);
            return true;
        }

        public boolean moveUp(Id id) {
            if (ids == null) {
                return false;
            }
            int index = ids.indexOf(id);
            if (index <= 0) {
                return false;
            }
            Id next = ids.get(index - 1);
            ids.set(index, next);
            ids.set(index - 1, id);
            return true;
        }

        public boolean add(int index, Id id) {
            if (ids == null || index < 0 || index >= ids.size()) {
                return add(id);
            } else {
                synchronized (this) {
                    if (!ids.contains(id)) {
                        setEditState(EEditState.MODIFIED);
                        ids.add(index, id);
                        return true;
                    }
                    return false;
                }
            }
        }

        public boolean add(Id id) {
            synchronized (this) {
                if (ids == null) {
                    ids = new ArrayList<>();
                }
                if (!ids.contains(id)) {
                    setEditState(EEditState.MODIFIED);
                    return ids.add(id);
                }

                return false;
            }
        }

        public boolean remove(Id id) {
            synchronized (this) {
                if (ids == null) {
                    return false;
                }
                setEditState(EEditState.MODIFIED);
                return this.ids.remove(id);
            }
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            if (ids != null) {
                for (Id id : ids) {
                    AdsEditorPresentationDef e = getOwnerSelectorPresentation().getOwnerPresentations().getEditorPresentations().findById(id, EScope.ALL).get();
                    if (e != null) {
                        list.add(e);
                    }
                }
            }
        }

        private AdsSelectorPresentationDef getOwnerSelectorPresentation() {
            return (AdsSelectorPresentationDef) getContainer();
        }
    }
    private AdsCondition condition;
    private Id creationClassCatalogId;
    private Id transferSelectorPresentationId;
    private final CreatePresentationsList createPresentationList;
    private final EditorPresentations editorPresentations;
    private final SelectorAddons addons;
    private final SelectorColumns selectorColumns;
    private boolean autoExpand;
    private boolean restorePosition = true;
    private ERuntimeEnvironmentType clientEnvironment;
    private final ICustomViewable.CustomViewSupport<AdsSelectorPresentationDef, AdsAbstractUIDef> customViewSuppoort = new CustomViewSupport<AdsSelectorPresentationDef, AdsAbstractUIDef>(this) {
        @Override
        protected AdsAbstractUIDef createOrLoadCustomView(AdsSelectorPresentationDef context, ERuntimeEnvironmentType env, AbstractDialogDefinition xDef) {
            if (env == ERuntimeEnvironmentType.EXPLORER) {
                if (xDef != null) {
                    return AdsCustomSelectorDef.Factory.loadFrom(context, xDef);
                } else {
                    return AdsCustomSelectorDef.Factory.newInstance(context);
                }
            } else if (env == ERuntimeEnvironmentType.WEB) {
                if (xDef != null) {
                    return AdsRwtCustomSelectorDef.Factory.loadFrom(context, xDef);
                } else {
                    return AdsRwtCustomSelectorDef.Factory.newInstance(context);
                }
            } else {
                throw new UnsupportedOperationException("Not supported yet");
            }
        }
    };

    @Override
    public CustomViewSupport<AdsSelectorPresentationDef, AdsAbstractUIDef> getCustomViewSupport() {
        return findAttributeOwner(EPresentationAttrInheritance.CUSTOM_DIALOG).get().customViewSuppoort;
    }

    @SuppressWarnings("deprecation")
    protected AdsSelectorPresentationDef(SelectorPresentationDefinition xDef) {
        super(xDef);

        this.addons = xDef.getAddons() == null ? new SelectorAddons(this) : new SelectorAddons(this, xDef.getAddons());
        this.condition = AdsCondition.Factory.loadFrom(this, xDef.getCondition());
        this.creationClassCatalogId = xDef.getCreationClassCatalogId();

        this.editorPresentations = new EditorPresentations(this, xDef.getEditorPresentationIds());
        this.selectorColumns = new SelectorColumns(xDef.getSelectorColumns());
        this.transferSelectorPresentationId = xDef.getTransferSelectorPresentationId();
        if (xDef.getView() != null) {
            customViewSuppoort.loadCustomView(ERuntimeEnvironmentType.EXPLORER, xDef.getView());
        }
        if (xDef.getWebView() != null) {
            customViewSuppoort.loadCustomView(ERuntimeEnvironmentType.WEB, xDef.getWebView());
        }
        this.createPresentationList = CreatePresentationsList.Factory.loadFrom(this, xDef);

        if (xDef.getAddons() != null && xDef.getAddons().isSetAutoExpand()) {
            this.autoExpand = xDef.getAddons().getAutoExpand();
        }

        if (xDef.isSetAutoExpand()) {
            this.autoExpand = xDef.getAutoExpand();
        }
        if (xDef.isSetRestorePosition()) {
            this.restorePosition = xDef.getRestorePosition();
        }
        if (xDef.getSuppressedWarnings() != null) {
            problems = new Problems(this, xDef.getSuppressedWarnings());
        }
        if (xDef.getClientEnvironment() != null && xDef.getClientEnvironment().isClientEnv()) {
            this.clientEnvironment = xDef.getClientEnvironment();
        }
    }

    protected AdsSelectorPresentationDef() {
        this(Id.Factory.newInstance(EDefinitionIdPrefix.SELECTOR_PRESENTATION));
    }

    protected AdsSelectorPresentationDef(Id id) {
        super(id, "NewSelectorPresentation");
        this.getRestrictions().deny(ERestriction.DELETE_ALL);
        this.addons = new SelectorAddons(this);

        this.condition = AdsCondition.Factory.newInstance(this);
        this.creationClassCatalogId = null;

        this.editorPresentations = new EditorPresentations(this, null);
        this.selectorColumns = new SelectorColumns(null);
        this.transferSelectorPresentationId = null;
        this.inheritanceMask = EnumSet.of(EPresentationAttrInheritance.TITLE, EPresentationAttrInheritance.ICON);
        this.createPresentationList = CreatePresentationsList.Factory.newInstance(this);
    }

    protected AdsSelectorPresentationDef(AdsSelectorPresentationDef source) {
        super(source);
        this.addons = new SelectorAddons(this);
        this.condition = AdsCondition.Factory.newInstance(this);
        this.creationClassCatalogId = null;

        this.editorPresentations = new EditorPresentations(this, null);
        this.selectorColumns = new SelectorColumns(null);
        this.transferSelectorPresentationId = source.transferSelectorPresentationId;
        this.createPresentationList = CreatePresentationsList.Factory.newCopy(this, source.getCreatePresentationsList());
    }

    public boolean isRestorePositionEnabled() {
        return this.restorePosition;
    }

    public void setRestorePositionEnabled(boolean enable) {
        if (this.restorePosition != enable) {
            this.restorePosition = enable;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        AdsEntityObjectClassDef clazz = getOwnerClass();
        ERuntimeEnvironmentType clazzEnv = ERuntimeEnvironmentType.COMMON_CLIENT;
        if (clazz != null) {
            clazzEnv = clazz.getClientEnvironment();
        }
        if (clazzEnv != ERuntimeEnvironmentType.COMMON_CLIENT) {
            return clazzEnv;
        } else {
            if (clientEnvironment == null || !clientEnvironment.isClientEnv()) {
                return ERuntimeEnvironmentType.COMMON_CLIENT;
            } else {
                return clientEnvironment;
            }
        }
    }

    public boolean canChangeClientEnvironment() {
        AdsEntityObjectClassDef clazz = getOwnerClass();
        if (clazz != null) {
            return clazz.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT;
        } else {
            return true;
        }
    }

    public void setClientEnvironment(ERuntimeEnvironmentType env) {
        ERuntimeEnvironmentType current = getClientEnvironment();
        if (env != current && env.isClientEnv()) {
            this.clientEnvironment = env;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isAutoExpandEnabled() {
        return autoExpand;
    }

    public void setAutoExpandEnabled(boolean autoExpand) {
        if (this.autoExpand != autoExpand) {
            this.autoExpand = autoExpand;
            setEditState(EEditState.MODIFIED);
        }
    }

    public EditorPresentations getEditorPresentations() {
        return editorPresentations;
    }

    @Override
    public AdsGroupModelClassDef getModel() {
        return (AdsGroupModelClassDef) modelClass;
    }

    public boolean setCreationClassCatalogId(Id id) {
        if (!isCreationClassCatalogInherited()) {
            this.creationClassCatalogId = id;
            setEditState(EEditState.MODIFIED);
            return true;
        }

        return false;
    }

    @Override
    public CreatePresentationsList getCreatePresentationsList() {
        return createPresentationList;
    }

    public Id getTransferSelectorPresentationId() {
        return transferSelectorPresentationId;
    }

    public void setTransferSelectorPresentationId(Id id) {
        this.transferSelectorPresentationId = id;
        setEditState(EEditState.MODIFIED);
    }

    /**
     * Looks for editor presentation used for newly created objects
     */
    public AdsSelectorPresentationDef findSelectorPresentationForTransfer() {
        if (transferSelectorPresentationId == null) {
            return null;
        } else {
            EntityObjectPresentations prs = getOwnerPresentations();
            return prs == null ? null : prs.getSelectorPresentations().findById(transferSelectorPresentationId, EScope.ALL).get();
        }
    }

    /**
     * Looks for base editor presentation
     */
    public SearchResult<AdsSelectorPresentationDef> findBaseSelectorPresentation() {
        EntityObjectPresentations eprs = getOwnerPresentations();
        if (eprs == null) {
            return SearchResult.empty();
        }
        return eprs.getSelectorPresentations().findById(getBasePresentationId(), EScope.ALL);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SearchResult<AdsSelectorPresentationDef> findAttributeOwner(EPresentationAttrInheritance attributeMask) {
        return (SearchResult<AdsSelectorPresentationDef>) super.findAttributeOwner(attributeMask);
    }

    public boolean isCreationClassCatalogInherited() {
        return inheritanceMask.contains(EPresentationAttrInheritance.CLASS_CATALOG);
    }

    public boolean setCreationClassCatalogInherited(boolean inherit) {
        if (inherit) {
            if (!isCreationClassCatalogInherited()) {
                this.inheritanceMask.add(EPresentationAttrInheritance.CLASS_CATALOG);
                setEditState(EEditState.MODIFIED);
                return true;
            }

        } else {
            if (isCreationClassCatalogInherited()) {
                this.inheritanceMask.remove(EPresentationAttrInheritance.CLASS_CATALOG);
                setEditState(EEditState.MODIFIED);
                return true;
            }

        }
        return false;
    }

    /**
     * Returns creation class catalog id (with insheritance support)
     */
    public Id getCreationClassCatalogId() {
        return findAttributeOwner(EPresentationAttrInheritance.CLASS_CATALOG).get().creationClassCatalogId;
    }

    public SearchResult<AdsClassCatalogDef> findCreationClassCatalog() {
        return this.getOwnerClass().getPresentations().getClassCatalogs().findById(getCreationClassCatalogId(), EScope.ALL);
    }

    @Override
    public boolean isConditionInherited() {
        return inheritanceMask.contains(EPresentationAttrInheritance.CONDITION);
    }

    @Override
    public boolean setConditionInherited(boolean inherit) {
        if (inherit) {
            if (!isConditionInherited()) {
                this.inheritanceMask.add(EPresentationAttrInheritance.CONDITION);
                setEditState(EEditState.MODIFIED);
            }
            return true;
        } else {
            if (isConditionInherited()) {
                this.inheritanceMask.remove(EPresentationAttrInheritance.CONDITION);
                setEditState(EEditState.MODIFIED);
            }
            return true;
        }
    }

    @Override
    public boolean isConditionInheritanceErrorneous() {
        if (isConditionInherited()) {
            return (getBasePresentationId() == null
                    && getHierarchy().findOverwritten() == null);
        } else {
            return false;
        }
    }

    @Override
    public boolean canInheritCondition() {
        return true;
    }

    /**
     * Returns additional select condition (with inheritance support)
     */
    @Override
    public AdsCondition getCondition() {
        return findAttributeOwner(EPresentationAttrInheritance.CONDITION).get().condition;
    }

    public boolean isColumnsInherited() {
        return inheritanceMask.contains(EPresentationAttrInheritance.COLUMNS);
    }

    public boolean setColumnsInherited(boolean inherit) {
        if (inherit) {
            if (!isColumnsInherited()) {
                this.inheritanceMask.add(EPresentationAttrInheritance.COLUMNS);
                setEditState(EEditState.MODIFIED);
            }
            return true;
        } else {
            if (isColumnsInherited()) {
                SelectorColumns cols = findAttributeOwner(EPresentationAttrInheritance.COLUMNS).get().selectorColumns;
                if (cols != null) {
                    this.selectorColumns.clear();
                    for (SelectorColumn c : cols) {
                        this.selectorColumns.add(new SelectorColumn(this, c));
                    }
                }
                this.inheritanceMask.remove(EPresentationAttrInheritance.COLUMNS);

                setEditState(EEditState.MODIFIED);
            }
            return true;
        }
    }

    /**
     * Returns selector column set (with insheritance support)
     */
    public RadixObjects<SelectorColumn> getColumns() {
        return findAttributeOwner(EPresentationAttrInheritance.COLUMNS).get().selectorColumns;
    }

    public boolean isAddonsInherited() {
        return inheritanceMask.contains(EPresentationAttrInheritance.ADDONS);
    }

    public boolean setAddonsInherited(boolean inherit) {
        if (inherit) {
            if (!isAddonsInherited()) {
                this.inheritanceMask.add(EPresentationAttrInheritance.ADDONS);
                setEditState(EEditState.MODIFIED);
            }
            return true;
        } else {
            if (isAddonsInherited()) {
                this.inheritanceMask.remove(EPresentationAttrInheritance.ADDONS);
                setEditState(EEditState.MODIFIED);
            }
            return true;
        }
    }

    /**
     * Returns add-on set (with inheritance support)
     */
    public SelectorAddons getAddons() {
        return findAttributeOwner(EPresentationAttrInheritance.ADDONS).get().addons;
    }
    private final Hierarchy<AdsSelectorPresentationDef> hierarchy = new MemberHierarchy<AdsSelectorPresentationDef>(this) {
        @Override
        protected AdsSelectorPresentationDef findMember(ClassPresentations clazz, Id id) {
            if (clazz instanceof EntityObjectPresentations) {
                return ((EntityObjectPresentations) clazz).getSelectorPresentations().getLocal().findById(id);
            } else {
                return null;
            }
        }
    };

    @Override
    @SuppressWarnings("unchecked")
    public Hierarchy<AdsSelectorPresentationDef> getHierarchy() {
        return hierarchy;
    }

    @SuppressWarnings("unchecked")
    @Override
    PresentationHierarchyIterator<AdsSelectorPresentationDef> getHierarchyIteratorImpl(EScope scope, HierarchyIterator.Mode mode) {
        return new PresentationHierarchyIterator<AdsSelectorPresentationDef>(this, scope, mode) {
            @Override
            public ExtendableDefinitions<AdsSelectorPresentationDef> getCollectionForClass(AdsEntityObjectClassDef c) {
                return c.getPresentations().getSelectorPresentations();
            }
        };
    }

    public void appendTo(SelectorPresentationDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (saveMode == ESaveMode.NORMAL) {
//            if (creationEditorPresentationId != null) {
//                xDef.setCreationEditorPresentationId(creationEditorPresentationId);
//            }
            createPresentationList.appendTo(xDef);
            if (transferSelectorPresentationId != null) {
                xDef.setTransferSelectorPresentationId(transferSelectorPresentationId);
            }
        }

        if (saveMode == ESaveMode.NORMAL || (saveMode == ESaveMode.API && !isFinal())) {
            this.addons.appendTo(xDef.addNewAddons());
            this.selectorColumns.appendTo(xDef);
            this.condition.appendTo(xDef.addNewCondition());
            if (creationClassCatalogId != null) {
                xDef.setCreationClassCatalogId(creationClassCatalogId);
            }
        }

        if (saveMode == ESaveMode.NORMAL) {
            if (this.editorPresentations.ids != null && !this.editorPresentations.ids.isEmpty()) {
                xDef.setEditorPresentationIds(this.editorPresentations.getIds());
            }
            if (autoExpand) {
                xDef.setAutoExpand(true);
            }
            if (!restorePosition) {
                xDef.setRestorePosition(false);
            }
        }
        if (!isCustomViewInherited()) {
            if (saveMode == ESaveMode.NORMAL || (saveMode == ESaveMode.API && !isFinal())) {
                if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                    ((AdsCustomSelectorDef) this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.EXPLORER)).appendTo(xDef, saveMode);
                }
                if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                    ((AdsRwtCustomSelectorDef) this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.WEB)).appendTo(xDef, saveMode);
                }
            }
        }

        if (clientEnvironment != null && clientEnvironment != ERuntimeEnvironmentType.COMMON_CLIENT) {
            xDef.setClientEnvironment(clientEnvironment);
        }

        if (problems != null && problems.getSuppressedWarnings().length > 0) {
            List<Integer> list = new ArrayList<>(problems.getSuppressedWarnings().length);
            for (int w : problems.getSuppressedWarnings()) {
                list.add(Integer.valueOf(w));
            }
            xDef.setSuppressedWarnings(list);

        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (!isAddonsInherited()) {
            this.addons.visit(visitor, provider);
        }
        if (!isConditionInherited()) {
            this.condition.visit(visitor, provider);
        }
        this.editorPresentations.visit(visitor, provider);
        if (!isColumnsInherited()) {
            this.selectorColumns.visit(visitor, provider);
        }
        if (!isCustomViewInherited()) {
            if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.EXPLORER).visit(visitor, provider);
            }
            if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.WEB).visit(visitor, provider);
            }
        }
        this.createPresentationList.visit(visitor, provider);
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.SELECTOR_PRESENTATION;
    }

    private void collectDependences(boolean direct, List<Definition> list) {
        super.collectDependences(list);
        findBaseSelectorPresentation().save(list);

        AdsSelectorPresentationDef s = findSelectorPresentationForTransfer();
        if (s != null) {
            list.add(s);
        }
        SearchResult<AdsClassCatalogDef> cc = findCreationClassCatalog();
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

        ModelClassInfo modelInfo = findActualModelClass();
        if (modelInfo.clazz != null) {
            list.add(modelInfo.clazz);
        }

    }

    @Override
    public void collectDependences(List<Definition> list) {
        collectDependences(false, list);
    }

    @Override
    public void collectDirectDependences(List<Definition> list) {
        collectDependences(true, list);
    }

    @Override
    public ClipboardSupport<AdsSelectorPresentationDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsSelectorPresentationDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                SelectorPresentationDefinition xDef = SelectorPresentationDefinition.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsSelectorPresentationDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof SelectorPresentationDefinition) {
                    return Factory.loadFrom((SelectorPresentationDefinition) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }

            @Override
            protected Method getDecoderMethod() {
                try {
                    return AdsSelectorPresentationDef.Factory.class.getDeclaredMethod("loadFrom", SelectorPresentationDefinition.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    return null;
                }
            }
        };
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ExtendablePresentations.ExtendablePresentationsLocal && collection.getContainer() instanceof SelectorPresentations;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.SELECTOR_PRESENTATION;
    }

    @Override
    public AdsModelClassDef findFinalModel() {
        if (modelClass == null) {
            Id baseId = getBasePresentationId();
            EntityObjectPresentations eprs = getOwnerPresentations();
            if (eprs == null) {
                return null;
            }
            while (baseId != null) {
                AdsSelectorPresentationDef epr = eprs.getSelectorPresentations().findById(baseId, EScope.ALL).get();
                if (epr.modelClass == null) {
                    baseId = epr.getBasePresentationId();
                } else {
                    return epr.modelClass;
                }
            }
            return null;

        } else {
            return modelClass;
        }
    }

    @Override
    public SearchResult<AdsSelectorPresentationDef> findBasePresentation() {
        return findBaseSelectorPresentation();
    }

    public static class ModelClassInfo {

        public final AdsClassDef clazz;
        public final boolean useDefaultModel;

        public ModelClassInfo(AdsClassDef clazz, boolean useDefaultModel) {
            this.clazz = clazz;
            this.useDefaultModel = useDefaultModel;
        }
    }

    public final ModelClassInfo findActualModelClass() {
        AdsModelClassDef model = findFinalModel();
        AdsClassDef clazz;
        boolean defaultModel = false;
        if (model == null) {
            clazz = getOwnerClass();
            defaultModel = true;
        } else {
            clazz = model;
        }
        return new ModelClassInfo(clazz, defaultModel);
    }

    /**
     *
     * @return
     */
    public ERuntimeEnvironmentType getClientEnvironmentByEditorPresentations() {
        ERuntimeEnvironmentType currentEnv = null;
        for (EditorPresentations.PresentationInfo info : this.getEditorPresentations().getPresentationInfos()) {
            AdsEditorPresentationDef epr = info.findPresentation();
            if (epr != null) {
                ERuntimeEnvironmentType env = epr.getEffectiveClientEnvironment();
                if (currentEnv == null) {
                    currentEnv = env;
                } else {
                    if (currentEnv != env) {
                        return ERuntimeEnvironmentType.COMMON_CLIENT;
                    }
                }
            }
        }
        return currentEnv == null ? ERuntimeEnvironmentType.COMMON_CLIENT : currentEnv;
    }

    private static class Problems extends AdsDefinitionProblems {

        public Problems(AdsDefinition owner) {
            super(owner);
        }

        public Problems(AdsDefinition owner, List warnings) {
            super(owner, warnings);
        }
    }
    private Problems problems;

    @Override
    public RadixProblem.WarningSuppressionSupport getWarningSuppressionSupport(boolean createIfAbsent) {
        synchronized (this) {
            if (problems == null) {
                problems = new Problems(this);
            }
            return problems;
        }
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsSelectorPresentationDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new SelectorPresentationRadixdoc(getSource(), page, options);
            }
        };
    }
}
