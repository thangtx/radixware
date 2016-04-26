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

import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IInheritableTitledDefinition;
import org.radixware.kernel.common.defs.ads.IModalDisplayable;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.common.ContextlessCommandUsage;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.AbstractPresentationDefinition;
import org.radixware.schemas.adsdef.Size;

public abstract class AdsPresentationDef extends AdsPresentationsMember implements IOverwritable<AdsSelectorPresentationDef>, ContextlessCommandUsage.IContextlessCommandsUser, IInheritableTitledDefinition, IModalDisplayable {

    private final Restrictions restrictions;
    private Id basePresentationId;
    private Id iconId;
    protected EnumSet<EPresentationAttrInheritance> inheritanceMask;
    protected AdsModelClassDef modelClass;
    protected final ContextlessCommandUsage contextlessCommands;
    private ModialViewSizeInfo explorerDialogSize = new ModialViewSizeInfo(this);
    private ModialViewSizeInfo webDialogSize = new ModialViewSizeInfo(this);

    protected AdsPresentationDef(AbstractPresentationDefinition xDef) {
        super(xDef);
        this.basePresentationId = xDef.getBasePresentationId();
        this.iconId = xDef.getIconId();
        this.inheritanceMask = EPresentationAttrInheritance.fromBitField(xDef.getInheritanceMask());
        this.restrictions = Restrictions.Factory.newInstance(this, xDef.getRestrictions(), xDef.getEnabledCommandIds(), null, null /*
         * enabled children and enabled pages aren't supported for
         * presentations
         */);
        if (xDef.getModel() != null && !RadixObjectInitializationPolicy.get().isRuntime()) {
            this.modelClass = AdsModelClassDef.Factory.loadFrom(this, xDef.getModel());
        } else {
            this.modelClass = null;
        }
        this.explorerDialogSize.loadFrom(xDef.getDialogSize());
        this.webDialogSize.loadFrom(xDef.getWebDialogSize());
        this.contextlessCommands = ContextlessCommandUsage.Factory.loadFrom(this, xDef.getUsedContextlessCommands());
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }

    @Override
    public void afterOverwrite() {
        this.inheritanceMask = EnumSet.allOf(EPresentationAttrInheritance.class);
        this.modelClass = null;//AdsModelClassDef.Factory.newInstance(this);
        fireModelStateChange();
    }

    protected AdsPresentationDef(EDefinitionIdPrefix idPrefix, String name) {
        this(Id.Factory.newInstance(idPrefix), name);
    }

    protected AdsPresentationDef(Id id, String name) {
        super(id, name, null);
        this.basePresentationId = null;
        this.iconId = null;
        this.inheritanceMask = EnumSet.noneOf(EPresentationAttrInheritance.class);
        this.restrictions = Restrictions.Factory.newInstance(this, 0L, null, null, null);
        this.modelClass = null;//AdsModelClassDef.Factory.newInstance(this);
        this.contextlessCommands = ContextlessCommandUsage.Factory.newInstance(this);
    }

    protected AdsPresentationDef(AdsPresentationDef source) {
        super(source.getId(), source.getName(), null);
        this.basePresentationId = source.basePresentationId;
        this.iconId = source.iconId;
        this.inheritanceMask = EnumSet.allOf(EPresentationAttrInheritance.class);
        this.restrictions = Restrictions.Factory.newInstance(this, 0L, null, null, null);
        this.modelClass = AdsModelClassDef.Factory.newInstance(this);
        this.contextlessCommands = ContextlessCommandUsage.Factory.loadFrom(this, source.contextlessCommands.getUsedContextlessCommandIds());
    }

    @Override
    EntityObjectPresentations getOwnerPresentations() {
        return (EntityObjectPresentations) super.getOwnerPresentations();
    }

    protected SearchResult<? extends AdsPresentationDef> findAttributeOwnerSoAsItOn(EPresentationAttrInheritance attribute) {
        Set<AdsPresentationDef> lookup = new HashSet<>();
        PresentationHierarchyIterator<AdsPresentationDef> iter = getHierarchyIteratorImpl(EScope.ALL, HierarchyIterator.Mode.FIND_FIRST);

        AdsPresentationDef start = iter.next().first();

        assert start == this;
        List<AdsPresentationDef> result = new LinkedList<>();

        boolean lookWithinCurrentClass = attribute == EPresentationAttrInheritance.TITLE && getDefinitionType() == EDefType.EDITOR_PRESENTATION;

        while (iter.hasNext()) {
            HierarchyIterator.Chain<AdsPresentationDef> prs = iter.next();
            boolean chainCompleted = true;

            for (AdsPresentationDef pr : prs) {
                if (lookup.contains(pr)) {
                    break;
                }
                if (!pr.inheritanceMask.contains(attribute)) {
                    if (!lookWithinCurrentClass || pr.getOwnerClass() == start.getOwnerClass()) {
                        result.add(pr);
                    }
                } else {
                    chainCompleted = false;
                }
                lookup.add(pr);
            }
            if (chainCompleted) {
                break;
            }
        }

        if (result.isEmpty()) {
            return SearchResult.single(this);
        } else {
            return SearchResult.list(result);
        }
    }

    public SearchResult<? extends AdsPresentationDef> findAttributeOwner(EPresentationAttrInheritance attribute) {
        if (inheritanceMask.contains(attribute)) {
            return findAttributeOwnerSoAsItOn(attribute);
        } else {
            return SearchResult.single(this);
        }
    }

    abstract <T extends AdsPresentationDef> PresentationHierarchyIterator<T> getHierarchyIteratorImpl(EScope scope, HierarchyIterator.Mode mode);

    @SuppressWarnings("unchecked")
    public <T extends AdsPresentationDef> HierarchyIterator<T> getHierarchyIterator(EScope scope, HierarchyIterator.Mode mode) {
        return getHierarchyIteratorImpl(scope, mode);
    }

    // protected abstract ExtendableEntityPresentations<? extends AdsPresentationDef> getContainer();
    /**
     * Returns icon identifier for presentation (with insheritance support)
     */
    protected EntityPresentations findEntityPresentations() {
        AdsEntityObjectClassDef ownerClass = getOwnerClass();
        if (ownerClass != null) {
            AdsEntityClassDef basis = ownerClass.findRootBasis();
            if (basis != null) {
                return basis.getPresentations();
            }
        }
        return null;
    }

    protected EntityObjectPresentations findEntityObjectPresentations() {
        AdsEntityObjectClassDef ownerClass = getOwnerClass();
        if (ownerClass != null) {
            return ownerClass.getPresentations();
        }
        return null;
    }

    protected EntityObjectPresentations findEntityObjectPresentationsWithTitleDefined() {
        AdsEntityObjectClassDef ownerClass = getOwnerClass();
        while (ownerClass != null) {
            final EntityObjectPresentations eps = ownerClass.getPresentations();
            if (eps != null && eps.getObjectTitleId() != null) {
                return eps;
            }
            ownerClass = ownerClass.findBasis();
        }
        return null;
    }

    public Id getIconId() {
        if (isIconInherited()) {
            AdsPresentationDef p = findAttributeOwner(EPresentationAttrInheritance.ICON).get();
            if (p == this) {
                EntityPresentations eps = findEntityPresentations();
                if (eps != null) {
                    return eps.getIconId();
                }
            }
            return p == null ? null : p.iconId;
        } else {
            return iconId;
        }
    }

    public boolean setIconId(Id id) {
        if (!isIconInherited()) {
            iconId = id;
            setEditState(EEditState.MODIFIED);
            return true;
        } else {
            return false;
        }
    }

    public boolean isIconInherited() {
        return inheritanceMask.contains(EPresentationAttrInheritance.ICON);
    }

    public boolean setIconInherited(boolean inherit) {
        if (inherit) {
            if (!isIconInherited()) {
                this.inheritanceMask.add(EPresentationAttrInheritance.ICON);
                setEditState(EEditState.MODIFIED);
            }
        } else {
            if (isIconInherited()) {
                this.inheritanceMask.remove(EPresentationAttrInheritance.ICON);
                setEditState(EEditState.MODIFIED);

            }
        }
        return true;
    }

    public boolean isRestrictionsInherited() {
        return inheritanceMask.contains(EPresentationAttrInheritance.RESTRICTIONS);
    }

    public boolean setRestrictionsInherited(boolean inherit) {
        if (inherit) {
            if (!isRestrictionsInherited()) {
                this.inheritanceMask.add(EPresentationAttrInheritance.RESTRICTIONS);
            }

        } else {
            if (isRestrictionsInherited()) {
                this.inheritanceMask.remove(EPresentationAttrInheritance.RESTRICTIONS);
            }

        }
        return true;
    }

    public Restrictions getRestrictions() {
        return getRestrictions(true);
    }

    /**
     * Returns restrictions for presentation (with insheritance support)
     */
    public Restrictions getRestrictions(boolean pure) {
        AdsPresentationDef owner = findAttributeOwner(EPresentationAttrInheritance.RESTRICTIONS).get();
        if (owner == this) {
            EntityPresentations eps = findEntityPresentations();
            if (this.inheritanceMask.contains(EPresentationAttrInheritance.RESTRICTIONS)) {
                if (eps != null) {
                    return Restrictions.Factory.unmodifiableInstance(eps.getRestrictions());
                } else {
                    return this.restrictions;
                }

            } else {
                if (!pure && eps != null) {
                    return Restrictions.Factory.complementarInstance(this.restrictions, eps.getRestrictions());
                } else {
                    return this.restrictions;
                }
            }

        } else {
            return Restrictions.Factory.unmodifiableInstance(owner.getRestrictions(pure));
        }

    }

    public AdsDefinition getRestrictionsOwner() {
        if (!this.inheritanceMask.contains(EPresentationAttrInheritance.RESTRICTIONS)) {
            return this;
        }
        AdsPresentationDef owner = findAttributeOwner(EPresentationAttrInheritance.RESTRICTIONS).get();
        if (owner == this) {
            EntityPresentations eps = findEntityPresentations();
            if (eps != null) {
                return eps.getOwnerClass();
            }
            return null;
        } else {
            return owner;
        }
    }

    @Override
    public boolean isTitleInherited() {
        return inheritanceMask.contains(EPresentationAttrInheritance.TITLE);
    }

    public AdsMultilingualStringDef findTitleStorage() {
        if (isTitleInherited()) {
            AdsPresentationDef p = findAttributeOwner(EPresentationAttrInheritance.TITLE).get();
            if (p == this) {
                if (getDefinitionType() == EDefType.EDITOR_PRESENTATION) {
                    final EntityObjectPresentations eps = findEntityObjectPresentationsWithTitleDefined();
                    if (eps != null) {
                        return eps.findLocalizedString(eps.getObjectTitleId());
                    } else {
                        return null;
                    }
                } else {
                    final EntityPresentations eps = findEntityPresentations();
                    if (eps != null) {
                        return eps.findLocalizedString(eps.getEntityTitleId());
                    } else {
                        return null;
                    }
                }
            }
            return p == null ? null : p.findLocalizedString(p.titleId);
        } else {
            return findLocalizedString(titleId);
        }
    }

    @Override
    public boolean setTitleInherited(boolean inherit) {
        if (inherit) {
            if (!isTitleInherited()) {
                this.inheritanceMask.add(EPresentationAttrInheritance.TITLE);
                setEditState(EEditState.MODIFIED);
                return true;
            }

        } else {
            if (isTitleInherited()) {
                AdsMultilingualStringDef string = findTitleStorage();
                if (string != null) {
                    AdsMultilingualStringDef clone = string.cloneString(findLocalizingBundle());
                    titleId = clone.getId();
                }
                this.inheritanceMask.remove(EPresentationAttrInheritance.TITLE);
                setEditState(EEditState.MODIFIED);
                return true;
            }

        }
        return false;
    }

    @Override
    public AdsDefinition findOwnerTitleDefinition() {
        if (isTitleInherited()) {
            final AdsPresentationDef attributeOwner = findAttributeOwner(EPresentationAttrInheritance.TITLE).get();
            if (attributeOwner != null && attributeOwner != this) {
                return attributeOwner.findOwnerTitleDefinition();
            }

            if (getDefinitionType() == EDefType.EDITOR_PRESENTATION) {
                EntityObjectPresentations eps = findEntityObjectPresentationsWithTitleDefined();
                if (eps != null) {
                    return eps.getOwnerClass();
                } else {
                    return null;
                }
            } else {
                final EntityPresentations eps = findEntityPresentations();
                if (eps != null) {
                    return eps.getOwnerClass();
                } else {
                    return null;
                }
            }
        }
        return this;
    }

    @Override
    public Id getTitleId() {
        if (isTitleInherited()) {
            AdsPresentationDef p = findAttributeOwner(EPresentationAttrInheritance.TITLE).get();
            if (p == this) {
                if (getDefinitionType() == EDefType.EDITOR_PRESENTATION) {
                    final EntityObjectPresentations eps = findEntityObjectPresentationsWithTitleDefined();
                    if (eps != null) {
                        return eps.getObjectTitleId();
                    } else {
                        return null;
                    }
                } else {
                    final EntityPresentations eps = findEntityPresentations();
                    if (eps != null) {
                        return eps.getEntityTitleId();
                    } else {
                        return null;
                    }
                }
            }
            return p == null ? null : p.titleId;
        } else {
            return titleId;
        }
    }

    @Override
    public void setTitleId(Id titleId) {
        if (isTitleInherited()) {
            return;
        }
        this.titleId = titleId;
        setEditState(EEditState.MODIFIED);
    }

    /**
     * Returns presentation title for given language (with insheritance support)
     */
    @Override
    public String getTitle(EIsoLanguage language) {
        final AdsDefinition ownerTitleDefinition = findOwnerTitleDefinition();
        return ownerTitleDefinition != null
                ? ownerTitleDefinition.getLocalizedStringValue(language, getTitleId()) : null;
    }

    @Override
    public boolean setTitle(EIsoLanguage language, String value) {
        if (this.isTitleInherited()) {
            throw new DefinitionError("Title is inherited.", this);
        } else {
            super.setTitle(language, value);
            return true;
        }

    }
    private RadixEventSource cvInheritance = null;

    public RadixEventSource getCustomViewInheritanceChangesSupport() {
        synchronized (this) {
            if (cvInheritance == null) {
                cvInheritance = new RadixEventSource();
            }
            return cvInheritance;
        }
    }

    @SuppressWarnings("unchecked")
    private void fireCustomViewInheritanceChange() {
        synchronized (this) {
            if (cvInheritance != null) {
                cvInheritance.fireEvent(new RadixEvent());
            }
        }
    }

    public boolean isCustomViewInherited() {
        return inheritanceMask.contains(EPresentationAttrInheritance.CUSTOM_DIALOG);
    }

    public boolean setCustomViewInherited(boolean inherit) {
        if (inherit) {
            if (!isCustomViewInherited()) {
                this.inheritanceMask.add(EPresentationAttrInheritance.CUSTOM_DIALOG);
                fireCustomViewInheritanceChange();
                setEditState(EEditState.MODIFIED);
                return true;
            }

        } else {
            if (isCustomViewInherited()) {
                this.inheritanceMask.remove(EPresentationAttrInheritance.CUSTOM_DIALOG);
                setEditState(EEditState.MODIFIED);
                fireCustomViewInheritanceChange();
                return true;
            }
        }
        return false;
    }

    /**
     * Returns custom editor id (with insheritance support)
     */
    public final Id getCustomViewId() {
        return Id.Factory.changePrefix(findAttributeOwner(EPresentationAttrInheritance.CUSTOM_DIALOG).get().getId(), this instanceof AdsEditorPresentationDef ? EDefinitionIdPrefix.CUSTOM_EDITOR : EDefinitionIdPrefix.CUSTOM_SELECTOR);
    }

    /**
     * Returns id of base presentation. If the presentation overwrites antoher
     * presentation own settings ignored and used value of overwritten
     * presentation
     */
    public Id getBasePresentationId() {
        AdsPresentationDef ovr = (AdsPresentationDef) getHierarchy().findOverwritten().get();
        if (ovr != null) {
            return ovr.getBasePresentationId();
        } else {
            return basePresentationId;
        }

    }

    public boolean setBasePresentationId(Id id) {
        AdsPresentationDef ovr = (AdsPresentationDef) getHierarchy().findOverwritten().get();
        if (ovr != null) {
            return false;
        } else {
            basePresentationId = id;
            setEditState(EEditState.MODIFIED);
            return true;
        }
    }

    public void appendTo(AbstractPresentationDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (basePresentationId != null) {
            xDef.setBasePresentationId(basePresentationId);
        }

        if (iconId != null) {
            xDef.setIconId(iconId);
        }

        if (modelClass != null) {
            this.modelClass.appendTo(xDef.addNewModel(), saveMode);
        }
        xDef.setInheritanceMask(EPresentationAttrInheritance.toBitField(this.inheritanceMask));

        xDef.setRestrictions(ERestriction.toBitField(restrictions.getRestriction()));
        List<Id> enabledCommandIds = restrictions.getEnabledCommandIds();
        if (enabledCommandIds != null) {
            xDef.setEnabledCommandIds(enabledCommandIds);
        }
        if (saveMode == ESaveMode.NORMAL) {
            List<Id> usedClcs = contextlessCommands.getUsedContextlessCommandIds();
            if (usedClcs != null && !usedClcs.isEmpty()) {
                xDef.setUsedContextlessCommands(usedClcs);
            }
            Size xSize = Size.Factory.newInstance();
            if (this.webDialogSize.appendTo(xSize)) {
                xDef.setWebDialogSize(xSize);
            }
            xSize = Size.Factory.newInstance();
            if (this.explorerDialogSize.appendTo(xSize)) {
                xDef.setDialogSize(xSize);
            }
        }

    }

    @Override
    protected void insertToolTipPrefix(StringBuilder sb) {
        super.insertToolTipPrefix(sb);
        String access = getDefaultAccess().getName().toUpperCase().charAt(0) + getDefaultAccess().getName().substring(1, getDefaultAccess().getName().length());
        sb.append("<b>").append(access).append(" ").append(getClientEnvironment().getName()).append("</b>&nbsp;");
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        if (getTitleId() != null) {
            AdsDefinition def = findOwnerTitleDefinition();
            if (def != null) {
                sb.append("<br>Title:");
                final AdsLocalizingBundleDef bundle = def.findExistingLocalizingBundle();
                if (bundle != null) {
                    for (EIsoLanguage lan : bundle.getLanguages()) {
                        sb.append("<br>&nbsp;").append("(").append(lan.toString()).append("): ").append(getTitle(lan));
                    }
                }
            }
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.contextlessCommands.visit(visitor, provider);
        this.restrictions.visit(visitor, provider);
        if (this.modelClass != null) {
            this.modelClass.visit(visitor, provider);
        }
    }

    @Override
    public AdsEntityObjectClassDef getOwnerClass() {
        return (AdsEntityObjectClassDef) super.getOwnerClass();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(super.toString());

        buf.append("\n Inheritance Mask:\n");
        for (EPresentationAttrInheritance item : inheritanceMask) {
            buf.append(item.name());
            buf.append(" ");
        }

        return buf.toString();
    }

    public boolean isUseDefaultModel() {
        return modelClass == null;
    }

    public abstract AdsModelClassDef findFinalModel();

    public void setUseDefaultModel(boolean useDefault) {
        synchronized (this) {
            if (useDefault) {
                if (modelClass != null) {
                    modelClass = null;
                    setEditState(EEditState.MODIFIED);
                    fireModelStateChange();
                }
            } else {
                if (modelClass == null) {
                    modelClass = AdsModelClassDef.Factory.newInstance(this);
                    setEditState(EEditState.MODIFIED);
                    fireModelStateChange();
                }
            }
        }
    }

    public AdsModelClassDef getModel() {
        return modelClass;
    }

    @Override
    public ContextlessCommandUsage getUsedContextlessCommands() {
        return contextlessCommands;
    }

    public EnumSet<EPresentationAttrInheritance> getInheritanceMask() {
        return EnumSet.copyOf(inheritanceMask);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (iconId != null && !isIconInherited()) {
            AdsSearcher.Factory.newImageSearcher(this).findById(iconId).save(list);
        }
        if (!isRestrictionsInherited()) {
            Restrictions restrs = getRestrictions();
            if (restrs != null && restrs.isDenied(ERestriction.ANY_COMMAND)) {
                DefinitionSearcher<AdsContextlessCommandDef> clcSearcer = AdsSearcher.Factory.newAdsContextlessCommandSearcher(getModule());
                ExtendableDefinitions<AdsScopeCommandDef> commandSet = getCommandsLookup();
                for (Id id : restrs.getEnabledCommandIds()) {
                    AdsCommandDef command = null;
                    if (id.getPrefix() == EDefinitionIdPrefix.CONTEXTLESS_COMMAND) {
                        clcSearcer.findById(id).save(list);
                    } else {
                        if (commandSet != null) {
                            commandSet.findById(id, EScope.ALL).save(list);
                        }
                    }
                }
            }
        }
    }

    public abstract SearchResult<? extends AdsPresentationDef> findBasePresentation();

    public final class ModelStateChangeEvent extends RadixEvent {
    }

    public interface IModelStateChangeListener extends IRadixEventListener<ModelStateChangeEvent> {
    }
    RadixEventSource<IModelStateChangeListener, ModelStateChangeEvent> modelStateSupport = null;

    private void fireModelStateChange() {
        synchronized (this) {
            if (modelStateSupport != null) {
                modelStateSupport.fireEvent(new ModelStateChangeEvent());

            }
        }
    }

    public RadixEventSource<IModelStateChangeListener, ModelStateChangeEvent> getModelStateChangeSupport() {
        synchronized (this) {
            if (modelStateSupport == null) {
                modelStateSupport = new RadixEventSource<>();
            }
            return modelStateSupport;
        }
    }

    public ExtendableDefinitions<AdsScopeCommandDef> getCommandsLookup() {
        ExtendableDefinitions<AdsScopeCommandDef> commandSet = null;
        if (getDefinitionType() == EDefType.EDITOR_PRESENTATION) {
            commandSet = getOwnerClass().getPresentations().getCommands();
        } else if (getDefinitionType() == EDefType.SELECTOR_PRESENTATION) {
            AdsEntityObjectClassDef clazz = getOwnerClass();
            if (clazz != null) {
                AdsEntityClassDef aec = clazz.findRootBasis();
                if (aec != null) {
                    AdsEntityGroupClassDef agc = aec.findEntityGroup();
                    if (agc != null) {
                        commandSet = agc.getPresentations().getCommands();
                    }
                }
            }
        }
        return commandSet;
    }

    public boolean isOwnModelAllowed() {
        AdsDefinition def = getHierarchy().findOverwritten().get();
        if (def == null) {
            return true;
        } else {
            return getOwnerClass().isCodeEditable();
        }
    }

    @Override
    public ModialViewSizeInfo getModialViewSizeInfo(ERuntimeEnvironmentType env) {
        switch (env) {
            case EXPLORER:
                return explorerDialogSize;
            case WEB:
                return webDialogSize;
            default:
                return null;
        }
    }
}
