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
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.IInheritableTitledDefinition;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsExpressionPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.PropertyPresentationWriter;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.types.Id;

/**
 * Property presentation options for basic type properties Defines how to
 * display property in interactive applications
 *
 */
public class PropertyPresentation extends RadixObject implements IJavaSource, ILocalizedDef, IOverridable, IOverwritable, IInheritableTitledDefinition {

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new PropertyPresentationWriter(this, PropertyPresentation.this, purpose);
            }
        };
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        ids.add(new MultilingualStringInfo(PropertyPresentation.this) {
            @Override
            public Id getId() {
                return titleId;
            }

            @Override
            public void updateId(Id newId) {
                titleId = newId;
            }

            @Override
            public EAccess getAccess() {
                return getOwnerProperty().getAccessMode();
            }

            @Override
            public String getContextDescription() {
                return "Property Presentation Title";
            }

            @Override
            public boolean isPublished() {
                return getOwnerProperty().isPublished();
            }
        });
        ids.add(new MultilingualStringInfo(PropertyPresentation.this) {
            @Override
            public Id getId() {
                return hintId;
            }

            @Override
            public void updateId(Id newId) {
                hintId = newId;
            }

            @Override
            public EAccess getAccess() {
                return getOwnerProperty().getAccessMode();
            }

            @Override
            public String getContextDescription() {
                return "Property Presentation Hint";
            }

            @Override
            public boolean isPublished() {
                return getOwnerProperty().isPublished();
            }

            @Override
            public EMultilingualStringKind getKind() {
                return EMultilingualStringKind.TOOLTIP;
            }

        });
    }

    @Override
    public AdsMultilingualStringDef findLocalizedString(Id stringId) {
        return getOwnerProperty().findLocalizedString(stringId);
    }

    @Override
    public void afterOverride() {
        this.inheritanceMask = EnumSet.allOf(EPropAttrInheritance.class);
        final AdsPropertyDef owner = getOwnerProperty();
        if (owner != null) {
            afterRedefinition(owner.getHierarchy().findOverridden().get());
        }
    }

    private void afterRedefinition(AdsPropertyDef ovr) {
        if (ovr != null) {
            if (ovr instanceof IAdsPresentableProperty) {
                final ServerPresentationSupport support = ((IAdsPresentableProperty) ovr).getPresentationSupport();
                final PropertyPresentation presentation;
                if (support != null) {
                    presentation = ((IAdsPresentableProperty) ovr).getPresentationSupport().getPresentation();
                } else {
                    presentation = null;
                }

                if (presentation == null) {
                    this.inheritanceMask.clear();
                }
            } else {
                this.inheritanceMask.clear();
            }
        } else {
            this.inheritanceMask.clear();
        }
    }

    public EnumSet<EPropAttrInheritance> getInheritanceMask() {
        return EnumSet.copyOf(inheritanceMask);
    }

    @Override
    public boolean isOverwrite() {
        return false;
    }

    @Override
    public boolean setOverwrite(boolean override) {
        return false;
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }

    @Override
    public void afterOverwrite() {
        this.inheritanceMask = EnumSet.allOf(EPropAttrInheritance.class);
        final AdsPropertyDef owner = getOwnerProperty();
        if (owner != null) {
            afterRedefinition(owner.getHierarchy().findOverwritten().get());
        }
    }

    public static final class Factory {

        public static PropertyPresentation loadFrom(AdsPropertyDef propertyDef, org.radixware.schemas.adsdef.PropertyPresentation xPres) {
            if (propertyDef.getValue().getType().isBasedOn(EValType.PARENT_REF) || propertyDef.getValue().getType().isBasedOn(EValType.ARR_REF)) {
                return new ParentRefPropertyPresentation(propertyDef, xPres);
            } else if (propertyDef.getValue().getType().isBasedOn(EValType.OBJECT)) {
                return new ObjectPropertyPresentation(propertyDef, xPres);
            } else {
                return new PropertyPresentation(propertyDef, xPres);
            }
        }

        public static PropertyPresentation newInstance(AdsPropertyDef propertyDef) {
            if (propertyDef.getValue().getType().isBasedOn(EValType.PARENT_REF) || propertyDef.getValue().getType().isBasedOn(EValType.ARR_REF)) {
                return new ParentRefPropertyPresentation(propertyDef, (org.radixware.schemas.adsdef.PropertyPresentation) null);
            } else if (propertyDef.getValue().getType().isBasedOn(EValType.OBJECT)) {
                return new ObjectPropertyPresentation(propertyDef, null);
            } else {
                return new PropertyPresentation(propertyDef, (org.radixware.schemas.adsdef.PropertyPresentation) null);
            }
        }

        public static PropertyPresentation newOverride(AdsPropertyDef propertyDef, PropertyPresentation pres) {
            if (pres == null) {
                return newInstance(propertyDef);
            }
            if (pres instanceof ParentRefPropertyPresentation) {
                return new ParentRefPropertyPresentation(propertyDef, pres, true);
            } else {
                return new PropertyPresentation(propertyDef, pres, true);
            }
        }

        public static PropertyPresentation newOverwrite(AdsPropertyDef propertyDef, PropertyPresentation pres) {
            if (pres == null) {
                return newInstance(propertyDef);
            }
            if (pres instanceof ParentRefPropertyPresentation) {
                return new ParentRefPropertyPresentation(propertyDef, pres, false);
            } else {
                return new PropertyPresentation(propertyDef, pres, false);
            }
        }

        public static PropertyPresentation redefine(AdsPropertyDef propertyDef, PropertyPresentation pres) {
            if (propertyDef.getValue().getType().isBasedOn(EValType.PARENT_REF) || propertyDef.getValue().getType().isBasedOn(EValType.ARR_REF)) {
                if (pres instanceof ParentRefPropertyPresentation) {
                    return pres;
                } else {
                    return new ParentRefPropertyPresentation(propertyDef, pres, false);
                }
            } else if (propertyDef.getValue().getType().isBasedOn(EValType.OBJECT)) {
                if (pres instanceof ObjectPropertyPresentation) {
                    return pres;
                } else {
                    return new ObjectPropertyPresentation(propertyDef, pres, false);
                }
            } else {
                if (pres instanceof ParentRefPropertyPresentation || pres instanceof ObjectPropertyPresentation) {
                    return new PropertyPresentation(propertyDef, pres, false);
                } else {
                    return pres;

                }
            }
        }
    }
    protected EnumSet<EPropAttrInheritance> inheritanceMask;
    private PropertyEditOptions editing;
    private Id titleId;
    private Id hintId;
    private boolean isPresentable;

    private PropertyPresentation(AdsPropertyDef property, EnumSet<EPropAttrInheritance> mask) {
        super("PropertyPresentation");
        this.setContainer(property);
        this.isPresentable = true;
        this.inheritanceMask = mask;
        this.editing = PropertyEditOptions.Factory.newInstance(this);
        this.titleId = null;
        this.hintId = null;
    }

    protected PropertyPresentation(AdsPropertyDef property, org.radixware.schemas.adsdef.PropertyPresentation xPres) {
        this(property, xPres == null ? property.getNature() == EPropNature.PARENT_PROP ? EnumSet.allOf(EPropAttrInheritance.class) : EnumSet.of(EPropAttrInheritance.PARENT_TITLE_FORMAT) : EPropAttrInheritance.fromBitField(xPres.getInheritanceMask()));
        isPresentable = xPres == null ? true : xPres.isSetIsPresentable() ? xPres.getIsPresentable() : true;
        if (!isEditOptionsInherited()) {
            this.editing = xPres == null ? PropertyEditOptions.Factory.newInstance(this) : PropertyEditOptions.Factory.loadFrom(this, xPres.getEditing());
        }
        if (!isTitleInherited()) {
            this.titleId = xPres == null ? null : xPres.getTitleId();
        }
        if (!isHintInherited()) {
            this.hintId = xPres == null ? null : xPres.getHintId();
        }

    }

    private boolean isPresentableType(AdsTypeDeclaration decl) {
        if (decl == null) {
            return false;
        }
        if (decl.getTypeId() == null) {
            return false;
        }
        switch (decl.getTypeId()) {
            case JAVA_CLASS:
            case NATIVE_DB_TYPE:
            case JAVA_TYPE:
            case USER_CLASS:
            case ARR_CLOB:
            case ARR_BLOB:
                return false;
            default:
                return !decl.isArray();
        }
    }

    public final boolean isPresentable() {
        AdsPropertyDef prop = getOwnerProperty();
        if (prop == null) {
            return false;
        }

        if (!canBePresentable()) {
            return false;
        }
        AdsClassDef ownerClass = null;
        return isPresentable
                || prop.getNature() == EPropNature.SQL_CLASS_PARAMETER
                || prop.getNature() == EPropNature.GROUP_PROPERTY /*
                 * || (prop.getNature() == EPropNature.DYNAMIC && ((ownerClass =
                 * prop.getOwnerClass()) != null && ownerClass.getClassDefType()
                 * == EClassType.FORM_HANDLER))
                 */;
    }

    public final boolean setPresentable(boolean isPresentable) {
        if (!canBePresentable()) {
            return false;
        }
        this.isPresentable = isPresentable;
        setEditState(EEditState.MODIFIED);
        return true;
    }

    public final boolean canBePresentable() {
        AdsPropertyDef prop = getOwnerProperty();
        if (prop == null) {
            return false;
        }
        if (prop.getNature() == EPropNature.EXPRESSION) {
            if (((AdsExpressionPropertyDef) prop).isInvisibleForArte()) {
                return false;
            }
        }
        AdsTypeDeclaration decl = prop.getValue().getType();
        if (!isPresentableType(decl)) {
            return false;
        }

        AdsClassDef clazz = prop.getOwnerClass();
        if (clazz == null) {
            return false;
        }
        switch (clazz.getClassDefType()) {
            case ENTITY_GROUP:
                if (prop.getNature() != EPropNature.GROUP_PROPERTY) {
                    return false;
                }
                break;
            case PRESENTATION_ENTITY_ADAPTER:
                return false;
        }
        return true;
    }

    protected PropertyPresentation(AdsPropertyDef property, PropertyPresentation source, boolean forOverride) {
        this(property, forOverride ? EnumSet.allOf(EPropAttrInheritance.class) : source.inheritanceMask);
        isPresentable = source.isPresentable;

        if (!isTitleInherited()) {
            this.titleId = source.titleId;
        }
        if (!isHintInherited()) {
            this.hintId = source.hintId;
        }
        if (!isEditOptionsInherited()) {
            this.editing = PropertyEditOptions.Factory.newInstance(this, source.editing);
        }
    }

    /**
     * Returns owner property for the presentation options
     */
    public final AdsPropertyDef getOwnerProperty() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsPropertyDef) {
                return (AdsPropertyDef) owner;
            }
        }
        return null;
    }

    AdsLocalizingBundleDef findLocalizingBundle() {
        return getOwnerProperty().findExistingLocalizingBundle();
    }

//    private AdsModule getModule() {
//        return getOwnerProperty().getModule();
//    }
    /**
     * Returns true if edit options of the property are inherited from
     * overwritten or overridden property
     */
    public final boolean isEditOptionsInherited() {
        return inheritanceMask.contains(EPropAttrInheritance.EDITING);
    }

    /**
     * Tries to inherit edit options for the property Returns true if any of
     * overridden or overwritten properties found
     */
    public boolean setEditOptionsInherited(boolean inherit) {
        if (inherit) {
            if (!isEditOptionsInherited()) {
                this.inheritanceMask.add(EPropAttrInheritance.EDITING);
                setEditState(EEditState.MODIFIED);
                return true;

            } else {
                return true;
            }
        } else {
            if (isEditOptionsInherited()) {
                this.inheritanceMask.remove(EPropAttrInheritance.EDITING);
                this.editing = PropertyEditOptions.Factory.newInstance(this);
                setEditState(EEditState.MODIFIED);
            }
            return true;
        }
    }

    /**
     * Returns edit options setfor the property (final value)
     */
    public PropertyEditOptions getEditOptions() {
        if (isEditOptionsInherited()) {
            PropertyPresentation finalPresentation = findAttributeOwner(EPropAttrInheritance.EDITING);
            if (finalPresentation != null) {
                return new PropertyEditOptions.PropertyEditOptionsDelegate(this, finalPresentation.editing);
            }
        }
        return editing;
    }

    public PropertyPresentation findEditOptionsOwner() {
        if (isEditOptionsInherited()) {
            PropertyPresentation pp = findAttributeOwner(EPropAttrInheritance.EDITING);
            if (pp == this) {
                return null;
            } else {
                return pp;

            }
        } else {
            return this;
        }
    }

    /**
     * Returns true if title of the property are inherited from overwritten or
     * overridden property
     */
    @Override
    public final boolean isTitleInherited() {
        return inheritanceMask.contains(EPropAttrInheritance.TITLE);
    }

    public final boolean isHintInherited() {
        return inheritanceMask.contains(EPropAttrInheritance.HINT);
    }

    /**
     * Tries to inherit title for the property Returns true if any of overriden
     * or overwritten properties found
     */
    @Override
    public boolean setTitleInherited(boolean inherit) {
        if (inherit) {
            if (!isTitleInherited()) {

                this.inheritanceMask.add(EPropAttrInheritance.TITLE);

                setEditState(EEditState.MODIFIED);
                return true;

            } else {
                return true;
            }
        } else {
            if (isTitleInherited()) {
                this.inheritanceMask.remove(EPropAttrInheritance.TITLE);
                this.titleId = null;
                setEditState(EEditState.MODIFIED);
            }
            return true;
        }
    }

    public boolean setHintInherited(boolean inherit) {
        if (inherit) {
            if (!isHintInherited()) {

                this.inheritanceMask.add(EPropAttrInheritance.HINT);

                setEditState(EEditState.MODIFIED);
                return true;

            } else {
                return true;
            }
        } else {
            if (isHintInherited()) {
                this.inheritanceMask.remove(EPropAttrInheritance.HINT);
                this.hintId = null;
                setEditState(EEditState.MODIFIED);
            }
            return true;
        }
    }

    /**
     * Returns string containing title for the property (final value)
     */
    @Override
    public String getTitle(EIsoLanguage language) {
        AdsDefinition def = findTitleOwner();
        if (def == null) {
            return null;
        }
        Id id = getTitleId();
        if (id == null) {
            return null;
        }
        return def.getLocalizedStringValue(language, id);
    }

    /**
     * Returns string containing title for the property (final value)
     */
    public String getHint(EIsoLanguage language) {
        PropertyPresentation finalProp = findAttributeOwner(EPropAttrInheritance.HINT);
        if (finalProp.hintId != null) {
            AdsLocalizingBundleDef bundle = finalProp.findLocalizingBundle();
            if (bundle != null) {
                AdsMultilingualStringDef string = bundle.getStrings().findById(finalProp.hintId, EScope.LOCAL_AND_OVERWRITE).get();
                if (string != null) {
                    return string.getValue(language);
                }
            }
        }
        return null;
    }

    /**
     * Sets property title for given language
     */
    @Override
    public boolean setTitle(EIsoLanguage language, String title) {
        if (isTitleInherited()) {
            return false;
        }

        titleId = getOwnerProperty().setLocalizedStringValue(language, titleId, title);
        return titleId != null;
    }

    /**
     * Sets property hint for given language
     */
    public boolean setHint(EIsoLanguage language, String hint) {
        if (inheritanceMask.contains(EPropAttrInheritance.HINT)) {
            return false;
        }

        hintId = getOwnerProperty().setLocalizedStringValue(language, hintId, hint);
        return hintId != null;
    }

    @Override
    public final Id getTitleId() {
        if (isTitleInherited()) {
            final PropertyPresentation finalProp = findAttributeOwner(EPropAttrInheritance.TITLE);
            if (finalProp != null && finalProp != this) {
                return finalProp.getTitleId();
            } else if (finalProp == this) {
                final EntityObjectPresentations prs = findReferencedPresentations();
                if (prs != null) {
                    return prs.getObjectTitleId();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        return titleId;
    }

    public final Id getHintId() {
        if (isHintInherited()) {
            final PropertyPresentation finalProp = findAttributeOwner(EPropAttrInheritance.HINT);
            if (finalProp != null && finalProp != this) {
                return finalProp.getHintId();
            } else if (finalProp == this) {
//                final EntityObjectPresentations prs = findReferencedPresentations();
//                if (prs != null) {
//                    return prs.getObjectTitleId();
//                } else {
//                    return null;
//                }
                return null;
            } else {
                return null;
            }
        }
        return hintId;
    }

    public boolean setHintId(final Id id) {
        if (isHintInherited()) {
            return false;
        }
        this.hintId = id;
        setEditState(EEditState.MODIFIED);
        return true;
    }

    @Override
    public void setTitleId(final Id id) {
        assert !isTitleInherited() : "Title is inherited. Modifications of title not allowed.";

        if (isTitleInherited()) {
            return;
        }
        this.titleId = id;
        setEditState(EEditState.MODIFIED);
    }

    @Override
    public AdsDefinition findOwnerTitleDefinition() {
        return findTitleOwner();
    }

    public final AdsDefinition findTitleOwner() {
        if (isTitleInherited()) {
            final PropertyPresentation pp = findAttributeOwner(EPropAttrInheritance.TITLE);
            if (pp == this || pp == null) {
                final EntityObjectPresentations prs = findReferencedPresentations();
                if (prs == null) {
                    return null;
                } else {
                    return prs.getOwnerClass();
                }
            } else {
                return pp.getOwnerProperty();
            }
        } else {
            return this.getOwnerProperty();
        }
    }

    public final AdsDefinition findHintOwner() {
        if (isHintInherited()) {
            final PropertyPresentation pp = findAttributeOwner(EPropAttrInheritance.HINT);
            if (pp == this || pp == null) {
                return null;
            } else {
                return pp.getOwnerProperty();
            }
        } else {
            return this.getOwnerProperty();
        }
    }

    /**
     * Looks for final owner of given attribute mask
     */
    public PropertyPresentation findAttributeOwner(final EPropAttrInheritance attribute) {
        if (inheritanceMask.contains(attribute)) {
            final PropertyAttributeHierarchyIterator iter = new PropertyAttributeHierarchyIterator(this, EScope.ALL, HierarchyIterator.Mode.FIND_ALL);

            while (iter.hasNext()) {
                final PropertyPresentation next = iter.next().first();
                if (!next.inheritanceMask.contains(attribute)) {
                    return next;
                }
            }
            if (this.getOwnerProperty().getNature() == EPropNature.PARENT_PROP) {
                final AdsPropertyDef target = ((AdsParentPropertyDef) this.getOwnerProperty()).getParentInfo().findOriginalProperty();
                if (target != null && target instanceof IAdsPresentableProperty) {
                    final ServerPresentationSupport support = ((IAdsPresentableProperty) target).getPresentationSupport();
                    if (support != null) {
                        return support.getPresentation().findAttributeOwner(attribute);
                    }
                }
            }
            return this;
        } else {
            return this;
        }
    }

    public void inheritAll() {
        setEditOptionsInherited(true);
        setTitleInherited(true);
    }

    public void appendTo(final org.radixware.schemas.adsdef.PropertyPresentation xPres, final ESaveMode saveMode) {
        if (saveMode != ESaveMode.USAGE) {
            if (saveMode == ESaveMode.NORMAL || (saveMode == ESaveMode.API && !getOwnerProperty().isFinal())) {
                if (this.editing != null && !isEditOptionsInherited()) {
                    this.editing.appendTo(xPres.addNewEditing());
                }
            }

            if (this.titleId != null) {
                xPres.setTitleId(this.titleId);
            }
            if (this.hintId != null) {
                xPres.setHintId(this.hintId);
            }
            xPres.setInheritanceMask(EPropAttrInheritance.toBitField(inheritanceMask));
        }
        if (!isPresentable()) {
            xPres.setIsPresentable(false);
        }
    }

    protected EntityPresentations findReferencedEntityPresentations() {
        final AdsPropertyDef prop = this.getOwnerProperty();
        final AdsType type = prop.getValue().getType().resolve(prop).get();
        if (type instanceof AdsClassType.EntityObjectType) {
            final AdsEntityObjectClassDef clazz = ((AdsClassType.EntityObjectType) type).getSource();
            if (clazz == null) {
                return null;
            }

            final AdsEntityClassDef entity = clazz.findRootBasis();

            if (entity != null && entity.getPresentations() != null) {
                return entity.getPresentations();
            }

        }
        return null;
    }

    private class PresentationsRef extends DefinitionLink<AdsEntityObjectClassDef> {

        @Override
        protected AdsEntityObjectClassDef search() {
            final AdsPropertyDef prop = PropertyPresentation.this.getOwnerProperty();
            final AdsType type = prop.getValue().getType().resolve(prop).get();
            if (type instanceof AdsClassType.EntityObjectType) {
                final AdsEntityObjectClassDef entity = ((AdsClassType.EntityObjectType) type).getSource();
                if (entity != null && entity.getPresentations() != null) {
                    return entity;
                }

            }
            return null;
        }
    }
    private final PresentationsRef presentationsRef = new PresentationsRef();

    protected EntityObjectPresentations findReferencedPresentations() {
        AdsEntityObjectClassDef clazz = presentationsRef.find();
        if (clazz != null) {
            return clazz.getPresentations();
        } else {
            return null;
        }
    }

    @Override
    public void collectDependences(final List<Definition> list) {
        super.collectDependences(list);
        if (titleId != null && !(inheritanceMask.contains(EPropAttrInheritance.TITLE))) {
            final AdsLocalizingBundleDef bundle = getOwnerProperty().findExistingLocalizingBundle();
            if (bundle != null) {
                final AdsMultilingualStringDef string = bundle.getStrings().findById(titleId, EScope.LOCAL_AND_OVERWRITE).get();
                if (string != null) {
                    list.add(string);
                }
            }
        }
    }

    @Override
    public void visitChildren(final IVisitor visitor, final VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (!isEditOptionsInherited()) {
            this.editing.visit(visitor, provider);
        }
    }

    public final boolean isMayInheritTitle() {
        if (hasBaseItemInHierarchy()) {
            return true;
        } else {
            final EntityObjectPresentations prs = findReferencedPresentations();
            if (prs != null) {
                return true;
            } else {
                return false;
            }
        }
    }

    public final boolean isMayInheritHint() {
        return hasBaseItemInHierarchy();
    }

    public final boolean isMayInheritEditOptions() {
        return hasBaseItemInHierarchy();
    }

    private boolean hasBaseItemInHierarchy() {
        final PropertyAttributeHierarchyIterator iter = new PropertyAttributeHierarchyIterator(this, EScope.ALL, HierarchyIterator.Mode.FIND_ALL);
        final PropertyPresentation next = iter.next().first();
        assert next == this || next == null;
        if (iter.hasNext()) {
            return true;
        } else {
            if (this.getOwnerProperty().getNature() == EPropNature.PARENT_PROP) {
                final AdsPropertyDef target = ((AdsParentPropertyDef) this.getOwnerProperty()).getParentInfo().findOriginalProperty();
                if (target != null && target instanceof IAdsPresentableProperty) {
                    final ServerPresentationSupport support = ((IAdsPresentableProperty) target).getPresentationSupport();
                    if (support != null) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
