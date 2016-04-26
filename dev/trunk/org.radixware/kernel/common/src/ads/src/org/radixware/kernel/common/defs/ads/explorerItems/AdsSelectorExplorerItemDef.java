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

package org.radixware.kernel.common.defs.ads.explorerItems;

import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsCondition;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.common.IConditionProvider;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.enums.EExplorerItemAttrInheritance;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.SelectorExplorerItemDefinition;


public abstract class AdsSelectorExplorerItemDef extends AdsNodeExplorerItemDef implements IConditionProvider {

    private final Restrictions restrictions;
    private static final long DEFAULT_RESTRICTION = 0;
    private final AdsCondition condition;
    private Id creationClassCatalogId;
    protected Id selectorPresentationId;
    protected Id iconId;

    protected AdsSelectorExplorerItemDef(SelectorExplorerItemDefinition xDef) {
        super(xDef);
        this.condition = AdsCondition.Factory.loadFrom(this, xDef.getCondition());
        this.creationClassCatalogId = xDef.getCreationClassCatalogId();
        this.selectorPresentationId = xDef.getSelectorPresentationId();
        this.restrictions = Restrictions.Factory.newInstance(this, xDef.getRestrictions(), xDef.getEnabledCommands(), null, null);
    }

    protected AdsSelectorExplorerItemDef(AdsEntityObjectClassDef clazz, Id id) {
        super(id);
        this.classId = clazz == null ? null : clazz.getId();
        this.restrictions = Restrictions.Factory.newInstance(this, DEFAULT_RESTRICTION, null, null, null);
        this.condition = AdsCondition.Factory.newInstance(this);
    }

    protected AdsSelectorExplorerItemDef(AdsEntityObjectClassDef clazz) {
        this(clazz, null);
    }

    public boolean isClassCatalogInherited() {
        return inheritanceMask.contains(EExplorerItemAttrInheritance.CLASS_CATALOG);
    }

    public boolean setClassCatalogInherited(boolean inherit) {
        if (inherit) {
            if (!inheritanceMask.contains(EExplorerItemAttrInheritance.CLASS_CATALOG)) {
                inheritanceMask.add(EExplorerItemAttrInheritance.CLASS_CATALOG);
                setEditState(EEditState.MODIFIED);
            }
            return true;
        } else {
            if (inheritanceMask.contains(EExplorerItemAttrInheritance.CLASS_CATALOG)) {
                inheritanceMask.remove(EExplorerItemAttrInheritance.CLASS_CATALOG);
                setEditState(EEditState.MODIFIED);
            }
            return true;
        }
    }

    public Restrictions getRestrictions() {
        final AdsPresentationDef presentation = findReferencedPresentation().get();
        if (presentation == null) {
            return Restrictions.Factory.unmodifiableInstance(this.restrictions);
        }
        if (isRestrictionInherited()) {
            return Restrictions.Factory.unmodifiableInstance(presentation.getRestrictions());
        } else {
            return Restrictions.Factory.complementarInstance(restrictions, presentation.getRestrictions());
        }
    }

    public Restrictions getOwnRestrictions() {
        return this.restrictions;
    }

    @Override
    public String getTitle(EIsoLanguage language) {
        if (isTitleInherited()) {
            final SearchResult<AdsSelectorPresentationDef> result = findReferencedPresentation();
            return result.isEmpty() ? null : result.get().getTitle(language);
        } else {
            return super.getLocalizedStringValue(language, titleId);
        }
    }

    @Override
    public Id getTitleId() {
        if (isTitleInherited()) {
            final SearchResult<AdsSelectorPresentationDef> result = findReferencedPresentation();
            return result.isEmpty() ? null : result.get().getTitleId();
        }
        return super.getTitleId();
    }

    @Override
    public boolean setTitleInherited(boolean inherit) {
        if (inherit) {
            if (!inheritanceMask.contains(EExplorerItemAttrInheritance.TITLE)) {
                inheritanceMask.add(EExplorerItemAttrInheritance.TITLE);
                setEditState(EEditState.MODIFIED);
            }
            return true;
        } else {
            if (inheritanceMask.contains(EExplorerItemAttrInheritance.TITLE)) {
                final AdsPresentationDef presentation = findReferencedPresentation().get();
                if (presentation != null) {
                    final AdsMultilingualStringDef string = presentation.findTitleStorage();
                    if (string != null) {
                        final AdsMultilingualStringDef clone = string.cloneString(findLocalizingBundle());
                        titleId = clone.getId();
                    }
                }
                inheritanceMask.remove(EExplorerItemAttrInheritance.TITLE);
                setEditState(EEditState.MODIFIED);
            }
            return true;
        }
    }

    @Override
    public boolean isConditionInheritanceErrorneous() {
        return false;
    }

    @Override
    public Definition findOwnerTitleDefinition() {
        if (isTitleInherited()) {
            return AdsUtils.findTitleOwnerDefinition(findReferencedPresentation().get());
        } else {
            return this;
        }
    }

    public boolean isIconInherited() {
        return inheritanceMask.contains(EExplorerItemAttrInheritance.ICON);
    }

    public void setIconInherited(boolean inherit) {
        if (inherit) {
            if (!inheritanceMask.contains(EExplorerItemAttrInheritance.ICON)) {
                inheritanceMask.add(EExplorerItemAttrInheritance.ICON);
                setEditState(EEditState.MODIFIED);
            }
        } else {
            if (inheritanceMask.contains(EExplorerItemAttrInheritance.ICON)) {
                if (iconId == null) {
                    final AdsPresentationDef presentation = findReferencedPresentation().get();
                    if (presentation != null) {
                        iconId = presentation.getIconId();
                    }
                }
                inheritanceMask.remove(EExplorerItemAttrInheritance.ICON);
                setEditState(EEditState.MODIFIED);
            }
        }
    }

    @Override
    public AdsCondition getCondition() {
        return condition;
    }

    @Override
    public boolean isConditionInherited() {
        return false;
    }

    @Override
    public boolean canInheritCondition() {
        return false;
    }

    @Override
    public boolean setConditionInherited(boolean inherited) {
        return false;
    }

    public Id getCreationClassCatalogId() {
        if (isClassCatalogInherited()) {
            final AdsSelectorPresentationDef spr = findReferencedSelectorPresentation().get();
            if (spr == null) {
                return null;
            } else {
                return spr.getCreationClassCatalogId();
            }
        } else {
            return creationClassCatalogId;
        }
    }

    public void setCreationClassCatalogId(Id creationClassCatalogId) {
        if (isClassCatalogInherited()) {
            throw new DefinitionError("Class catalog is inherited. Modification is not allowed.", this);
        }
        this.creationClassCatalogId = creationClassCatalogId;
        setEditState(EEditState.MODIFIED);
    }

    public SearchResult<AdsClassCatalogDef> findCreationClassCatalog() {
        final AdsEntityObjectClassDef clazz = findReferencedEntityClass();
        if (clazz != null) {
            return clazz.getPresentations().getClassCatalogs().findById(getCreationClassCatalogId(), EScope.ALL);
        } else {
            return SearchResult.<AdsClassCatalogDef>empty();
        }
    }

    public Id getSelectorPresentationId() {
        return selectorPresentationId;
    }

    public SearchResult<AdsSelectorPresentationDef> findReferencedSelectorPresentation() {
        if (selectorPresentationId == null) {
            return SearchResult.<AdsSelectorPresentationDef>empty();
        } else {
            return findReferencedPresentation();
//            if (p instanceof AdsSelectorPresentationDef) {
//                return (AdsSelectorPresentationDef) p;
//            } else {
//                return null;
//            }
        }
    }

    public boolean setSelectorPresentationId(Id id) {
        final AdsEntityObjectClassDef clazz = findReferencedEntityClass();
        if (clazz != null && clazz.getPresentations().getSelectorPresentations().findById(id, EScope.ALL) != null) {
            this.selectorPresentationId = id;
            setEditState(EEditState.MODIFIED);
            fireNameChange();
            return true;
        } else {
            return false;
        }
    }

    protected SearchResult<AdsSelectorPresentationDef> findReferencedPresentation() {
        final AdsEntityObjectClassDef clazz = findReferencedEntityClass();
        if (clazz != null) {
            return clazz.getPresentations().getSelectorPresentations().findById(selectorPresentationId, EScope.ALL);
        } else {
            return SearchResult.<AdsSelectorPresentationDef>empty();
        }
    }

    public void appendTo(SelectorExplorerItemDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (saveMode == ESaveMode.NORMAL) {
            this.condition.appendTo(xDef.addNewCondition());
        }
        if (this.creationClassCatalogId != null) {
            xDef.setCreationClassCatalogId(creationClassCatalogId);
        }
        if (this.selectorPresentationId != null) {
            xDef.setSelectorPresentationId(selectorPresentationId);
        }
        xDef.setRestrictions(ERestriction.toBitField(this.restrictions.getRestriction()));
        final List<Id> ec = this.restrictions.getEnabledCommandIds();
        if (ec != null) {
            xDef.setEnabledCommands(ec);
        }
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        if (findReferencedEntityClass() != null) {
            final String classType = findReferencedEntityClass().getQualifiedName();
            sb.append("<br>Class: <br>&nbsp;<a href=\"\">").append(classType).append("</a>");
        }
        if (!findReferencedSelectorPresentation().isEmpty()) {
            final String basePresentation = findReferencedSelectorPresentation().get().getQualifiedName();
            sb.append("<br>Selector presentation: <br>&nbsp;<a href=\"\">").append(basePresentation).append("</a>");
        }
        if (!findCreationClassCatalog().isEmpty()) {
            sb.append("<br>Class catalog: <br>&nbsp;All");
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.condition.visit(visitor, provider);
        this.restrictions.visit(visitor, provider);
    }

    @Override
    public String getName() {
        final String ownName = super.getName();
        if (ownName == null || ownName.isEmpty() || "EI".equals(ownName)) {
            final AdsEntityObjectClassDef aec = findReferencedEntityClass();
            if (aec != null) {
                return aec.getName();
            } else {
                if (classId != null) {
                    return classId.toString();
                } else {
                    return "undefined";
                }
            }
        } else {
            return ownName;
        }
    }

    @Override
    protected void collectDependencesImpl(final boolean direct, final boolean forModule, final List<Definition> list) {
        super.collectDependencesImpl(true, true, list);
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

        final SearchResult<AdsSelectorPresentationDef> sps = findReferencedSelectorPresentation();
        if (!sps.isEmpty()) {
            if (!forModule) {
                sps.save(list);
            }
            if (!isRestrictionInherited()) {
                for (AdsSelectorPresentationDef s : sps.all()) {
                    final Restrictions restrs = getRestrictions();
                    if (restrs != null && restrs.isDenied(ERestriction.ANY_COMMAND)) {
                        final ExtendableDefinitions<AdsScopeCommandDef> lookup = s.getCommandsLookup();
                        if (lookup != null) {
                            for (Id id : restrs.getEnabledCommandIds()) {
                                final SearchResult<AdsScopeCommandDef> cmd = lookup.findById(id, EScope.ALL);
                                if (!cmd.isEmpty()) {
                                    cmd.save(list);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
