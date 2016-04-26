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
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.common.AdsCondition;
import org.radixware.kernel.common.defs.ads.common.IConditionProvider;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.enums.EPropAttrInheritance;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;


public class ParentRefPropertyPresentation extends PropertyPresentation {

    private static final long DEFAULT_PARENT_SELECTOR_RESTRICTION = ERestriction.toBitField(EnumSet.complementOf(EnumSet.of(ERestriction.VIEW)));
    private static final long DEFAULT_PARENT_EDITOR_RESTRICTION = ERestriction.toBitField(EnumSet.complementOf(EnumSet.of(ERestriction.VIEW)));

    public class ParentTitle {

        private AdsObjectTitleFormatDef titleFormat;

        public boolean isParentTitleFormatInherited() {
            return inheritanceMask.contains(EPropAttrInheritance.PARENT_TITLE_FORMAT);
        }

        public boolean setParentTitleFormatInherited(final boolean inherit) {
            if (inherit) {
                if (!isParentTitleFormatInherited()) {
                    inheritanceMask.add(EPropAttrInheritance.PARENT_TITLE_FORMAT);
                    setEditState(EEditState.MODIFIED);
                    this.titleFormat = null;
                    return true;
                } else {
                    return true;
                }
            } else {
                if (isParentTitleFormatInherited()) {
                    final AdsObjectTitleFormatDef ttf = getTitleFormat();
                    if (ttf != null) {
                        this.titleFormat = AdsObjectTitleFormatDef.Factory.newCopy(getOwnerProperty(), ttf);
                    } else {
                        this.titleFormat = AdsObjectTitleFormatDef.Factory.newInstance(getOwnerProperty());
                    }
                    inheritanceMask.remove(EPropAttrInheritance.PARENT_TITLE_FORMAT);
                    setEditState(EEditState.MODIFIED);
                }
                return true;
            }
        }

        private ParentTitle(final org.radixware.schemas.adsdef.PropertyPresentation xPres) {
            if (!isParentTitleFormatInherited()) {
                this.titleFormat = xPres == null || xPres.getParentTitle() == null ? AdsObjectTitleFormatDef.Factory.newInstance(getOwnerProperty()) : AdsObjectTitleFormatDef.Factory.loadFrom(getOwnerProperty(), xPres.getParentTitle().getTitleFormat());
            } else {
                this.titleFormat = null;
            }
        }

        private AdsObjectTitleFormatDef findTitleFormatForFinalProp(final ParentRefPropertyPresentation finalProp, final boolean unmodifiable) {
            if (finalProp.parentTitle.isParentTitleFormatInherited()) {
                final EntityPresentations prs = finalProp.findReferencedEntityPresentations();
                if (prs != null) {
                    return prs.getObjectTitleFormat().unmodifiableInstance();
                } else {
                    return unmodifiable ? finalProp.parentTitle.titleFormat.unmodifiableInstance() : finalProp.parentTitle.titleFormat;
                }
            } else {
                return unmodifiable ? finalProp.parentTitle.titleFormat.unmodifiableInstance() : finalProp.parentTitle.titleFormat;
            }

        }

        public AdsObjectTitleFormatDef getTitleFormat() {
            if (isParentTitleFormatInherited()) {
                final PropertyPresentation finalProp = findAttributeOwner(EPropAttrInheritance.PARENT_TITLE_FORMAT);
                if (finalProp instanceof ParentRefPropertyPresentation && finalProp != ParentRefPropertyPresentation.this) {
                    return findTitleFormatForFinalProp((ParentRefPropertyPresentation) finalProp, true);
                } else {
                    return findTitleFormatForFinalProp(ParentRefPropertyPresentation.this, false);
                }
            } else {
                return this.titleFormat;
            }
        }

        public void appendTo(final org.radixware.schemas.adsdef.PropertyPresentation xPres, final ESaveMode saveMode) {
            if (this.titleFormat != null) {
                final org.radixware.schemas.adsdef.PropertyPresentation.ParentTitle xDef = xPres.addNewParentTitle();
                this.titleFormat.appendTo(xDef.addNewTitleFormat(), saveMode);
            }
        }
    }

    public class ParentSelect implements IModelPublishableProperty.IParentSelectorPresentationLookup, IConditionProvider {

        private AdsCondition parentSelectCondition;
        private Id parentSelectorPresentationId;

        @Override
        public AdsCondition getCondition() {
            return getParentSelectorCondition();
        }

        @Override
        public boolean isConditionInherited() {
            return isParentSelectConditionInherited();
        }

        @Override
        public boolean canInheritCondition() {
            return false;
        }

        @Override
        public boolean setConditionInherited(boolean inherited) {
            return setParentSelectorInherited(inherited);
        }

        @Override
        public boolean isConditionInheritanceErrorneous() {
            return false;
        }

        public boolean isParentSelectConditionInherited() {
            return inheritanceMask.contains(EPropAttrInheritance.PARENT_SELECT_CONDITION);
        }

        public boolean setParentSelectConditionInherited(final boolean inherit) {
            if (inherit) {
                if (!isParentSelectConditionInherited()) {
                    inheritanceMask.add(EPropAttrInheritance.PARENT_SELECT_CONDITION);
                    setEditState(EEditState.MODIFIED);
                    this.parentSelectCondition = null;
                    return true;
                } else {
                    return true;
                }
            } else {
                if (isParentSelectConditionInherited()) {
                    this.parentSelectCondition = AdsCondition.Factory.newInstance(getOwnerProperty());
                    inheritanceMask.remove(EPropAttrInheritance.PARENT_SELECT_CONDITION);
                    setEditState(EEditState.MODIFIED);
                }
                return true;
            }
        }

        public AdsCondition getParentSelectorCondition() {
            if (isParentSelectConditionInherited()) {
                final PropertyPresentation finalProp = findAttributeOwner(EPropAttrInheritance.PARENT_SELECT_CONDITION);
                if (finalProp instanceof ParentRefPropertyPresentation && finalProp != ParentRefPropertyPresentation.this) {
                    return ((ParentRefPropertyPresentation) finalProp).parentSelect.parentSelectCondition.unmodifiableInstance(getOwnerProperty());
                } else {
                    return this.parentSelectCondition;
                }
            } else {
                return this.parentSelectCondition;
            }
        }

        public boolean isParentSelectorInherited() {
            return inheritanceMask.contains(EPropAttrInheritance.PARENT_SELECTOR);
        }

        public boolean setParentSelectorInherited(final boolean inherit) {
            if (inherit) {
                if (!isParentSelectorInherited()) {
                    inheritanceMask.add(EPropAttrInheritance.PARENT_SELECTOR);
                    setEditState(EEditState.MODIFIED);
                    this.parentSelectorPresentationId = null;
                    return true;
                } else {
                    return true;
                }
            } else {
                if (isParentSelectorInherited()) {
                    this.parentSelectorPresentationId = null;
                    inheritanceMask.remove(EPropAttrInheritance.PARENT_SELECTOR);
                    setEditState(EEditState.MODIFIED);
                }
                return true;
            }
        }

        @Override
        public boolean isReadOnly() {
            return ParentRefPropertyPresentation.this.isReadOnly();
        }

        @Override
        public Id getParentSelectorPresentationId() {
            if (isParentSelectorInherited()) {
                final PropertyPresentation finalProp = findAttributeOwner(EPropAttrInheritance.PARENT_SELECTOR);
                if (finalProp instanceof ParentRefPropertyPresentation && finalProp != ParentRefPropertyPresentation.this) {
                    return ((ParentRefPropertyPresentation) finalProp).parentSelect.getParentSelectorPresentationId();
                } else {
                    final EntityPresentations prs = findReferencedEntityPresentations();
                    if (prs != null) {
                        return prs.getDefaultSelectorPresentationId();
                    } else {
                        return this.parentSelectorPresentationId;
                    }
                }
            } else {
                return this.parentSelectorPresentationId;
            }
        }

        @Override
        public boolean setParentSelectorPresentationId(final Id id) {
            if (isParentSelectorInherited()) {
                return false;
            }
            this.parentSelectorPresentationId = id;
            setEditState(EEditState.MODIFIED);
            return true;
        }

        @Override
        public AdsSelectorPresentationDef findParentSelectorPresentation() {
            final EntityObjectPresentations prs = findReferencedPresentations();
            return prs == null ? null : prs.getSelectorPresentations().findById(getParentSelectorPresentationId(), EScope.ALL).get();
        }

        private ParentSelect(final org.radixware.schemas.adsdef.PropertyPresentation xPres) {
            if (!isParentSelectConditionInherited()) {
                this.parentSelectCondition = xPres == null || xPres.getParentSelect() == null ? AdsCondition.Factory.newInstance(getOwnerProperty()) : AdsCondition.Factory.loadFrom(getOwnerProperty(), xPres.getParentSelect().getParentSelectCondition());
            } else {
                this.parentSelectCondition = null;
            }
            if (!isParentSelectorInherited()) {
                this.parentSelectorPresentationId = xPres == null || xPres.getParentSelect() == null ? null : Id.Factory.loadFrom(xPres.getParentSelect().getParentSelectorPresentationId());
            } else {
                this.parentSelectorPresentationId = null;
            }
        }

        public void appendTo(final org.radixware.schemas.adsdef.PropertyPresentation xPres, ESaveMode saveMode) {

            final long parentEditorRestrictionValue = ERestriction.toBitField(parentEditorRestrictions.getRestriction());
            final long parentSelectorRestrictionValue = ERestriction.toBitField(parentSelectorRestrictions.getRestriction());

            if (parentSelectorPresentationId != null || !(parentSelectCondition != null && parentSelectCondition.isEmpty()) || parentSelectorRestrictionValue != DEFAULT_PARENT_SELECTOR_RESTRICTION || parentEditorRestrictionValue != DEFAULT_PARENT_EDITOR_RESTRICTION) {
                org.radixware.schemas.adsdef.PropertyPresentation.ParentSelect xDef = xPres.addNewParentSelect();
                if (parentSelectorPresentationId != null) {
                    xDef.setParentSelectorPresentationId(this.parentSelectorPresentationId.toString());
                }
                if (parentSelectCondition != null && !parentSelectCondition.isEmpty()) {
                    parentSelectCondition.appendTo(xDef.addNewParentSelectCondition());
                }

                if (parentEditorRestrictionValue != DEFAULT_PARENT_EDITOR_RESTRICTION) {
                    xDef.setParentEditorRestrictions(parentEditorRestrictionValue);
                }
                if (parentSelectorRestrictionValue != DEFAULT_PARENT_SELECTOR_RESTRICTION) {
                    xDef.setParentSelectorRestrictions(parentSelectorRestrictionValue);
                }
            }
        }
    }
    private transient Restrictions parentEditorRestrictions;
    private transient Restrictions parentSelectorRestrictions;
    private final transient ParentTitle parentTitle;
    private final transient ParentSelect parentSelect;

    ParentRefPropertyPresentation(final AdsPropertyDef property, final PropertyPresentation source, final boolean forOverride) {
        super(property, source, forOverride);
        this.parentTitle = new ParentTitle(null);
        this.parentSelect = new ParentSelect(null);
        if (source instanceof ParentRefPropertyPresentation) {
            this.parentEditorRestrictions = Restrictions.Factory.newInstance(this, ((ParentRefPropertyPresentation) source).parentEditorRestrictions.toBitField());
            this.parentSelectorRestrictions = Restrictions.Factory.newInstance(this, ((ParentRefPropertyPresentation) source).parentSelectorRestrictions.toBitField());
            this.inheritanceMask.addAll(EnumSet.allOf(EPropAttrInheritance.class));
        } else {
            this.parentEditorRestrictions = Restrictions.Factory.newInstance(this, DEFAULT_PARENT_EDITOR_RESTRICTION);
            this.parentSelectorRestrictions = Restrictions.Factory.newInstance(this, DEFAULT_PARENT_SELECTOR_RESTRICTION);
            this.inheritanceMask.add(EPropAttrInheritance.PARENT_TITLE_FORMAT);
            this.inheritanceMask.add(EPropAttrInheritance.PARENT_SELECTOR);
        }
    }

    ParentRefPropertyPresentation(final AdsPropertyDef property, final org.radixware.schemas.adsdef.PropertyPresentation xPres) {
        super(property, xPres);
        if (xPres == null || xPres.getParentSelect() == null) {
            this.parentEditorRestrictions = Restrictions.Factory.newInstance(this, DEFAULT_PARENT_EDITOR_RESTRICTION);
            this.parentSelectorRestrictions = Restrictions.Factory.newInstance(this, DEFAULT_PARENT_SELECTOR_RESTRICTION);
        } else {
            if (xPres.getParentSelect().isSetParentEditorRestrictions()) {
                this.parentEditorRestrictions = Restrictions.Factory.newInstance(this, xPres.getParentSelect().getParentEditorRestrictions());
            } else {
                this.parentEditorRestrictions = Restrictions.Factory.newInstance(this, DEFAULT_PARENT_EDITOR_RESTRICTION);
            }
            if (xPres.getParentSelect().isSetParentSelectorRestrictions()) {
                this.parentSelectorRestrictions = Restrictions.Factory.newInstance(this, xPres.getParentSelect().getParentSelectorRestrictions());
            } else {
                this.parentSelectorRestrictions = Restrictions.Factory.newInstance(this, DEFAULT_PARENT_SELECTOR_RESTRICTION);
            }
        }
        if (xPres == null) {
            this.inheritanceMask.add(EPropAttrInheritance.PARENT_TITLE_FORMAT);
            this.inheritanceMask.add(EPropAttrInheritance.PARENT_SELECTOR);
        }
        this.parentTitle = new ParentTitle(xPres);
        this.parentSelect = new ParentSelect(xPres);
    }

    public Restrictions getParentSelectorRestrictions() {
        return parentSelectorRestrictions;
    }

    public Restrictions getParentEditorRestrictions() {
        return parentEditorRestrictions;
    }

    @Override
    public void inheritAll() {
        super.inheritAll();
        parentTitle.setParentTitleFormatInherited(true);
        parentSelect.setParentSelectConditionInherited(true);
        parentSelect.setParentSelectorInherited(true);
    }

    public ParentTitle getParentTitle() {
        return parentTitle;


    }

    public ParentSelect getParentSelect() {
        return parentSelect;
    }

    @Override
    public void appendTo(final org.radixware.schemas.adsdef.PropertyPresentation xPres, final ESaveMode saveMode) {
        super.appendTo(xPres, saveMode);
        this.parentSelect.appendTo(xPres, saveMode);
        this.parentTitle.appendTo(xPres, saveMode);
    }

    @Override
    public void visitChildren(final IVisitor visitor, final VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (isPresentable()) {
            if (!this.parentTitle.isParentTitleFormatInherited()) {
                final AdsObjectTitleFormatDef tf = this.parentTitle.getTitleFormat();

                if (tf != null) {
                    tf.visit(visitor, provider);
                }
            }
            if (!this.parentSelect.isParentSelectConditionInherited()) {
                final AdsCondition c = this.parentSelect.getParentSelectorCondition();

                if (c != null) {
                    c.visit(visitor, provider);
                }
            }

            parentEditorRestrictions.visit(visitor, provider);
            parentSelectorRestrictions.visit(visitor, provider);
        }
    }

    @Override
    public void collectDependences(final List<Definition> list) {
        super.collectDependences(list);
        if (isPresentable()) {
            final AdsSelectorPresentationDef s = parentSelect.findParentSelectorPresentation();
            if (s != null) {
                list.add(s);
            }
        }
    }
}
