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

package org.radixware.kernel.server.meta.presentations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EPropAttrInheritance;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.server.arte.Release;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadParentPropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.types.Restrictions;


public class RadParentTitlePropertyPresentationDef extends RadPropertyPresentationDef {

    private final RadEntityTitleFormatDef parentTitleFormat; // if null inherits from parent entityDef
    private final RadConditionDef parentSelectCondition;
    private final Id parentSelectorPresentationId;
    private final List<Id> parentCreationEditorPresentationIds;
    private final Restrictions parentSelectorRestrictions;
    private final Id parentClassCatalogId;
    private final Id[] parentEditorPresentationIds;

    public RadParentTitlePropertyPresentationDef(
            final Id propId,
            final EEditPossibility editPossibility,
            final boolean isNotNull,
            final boolean readSeparately,
            final RadEntityTitleFormatDef parentTitleFormat,
            final Id parentSelectorPresentationId,
            final RadConditionDef parentSelectCondition,
            final Restrictions parentSelectorRestrictions,
            final Id[] editPresIds,
            final Id parentClassCatalogId,
            final Id[] parentCreationEditorPresentationIds,
            final EnumSet<EPropAttrInheritance> inheritanceMask) {
        super(propId, editPossibility, isNotNull, readSeparately, inheritanceMask);
        this.parentTitleFormat = parentTitleFormat;
        this.parentSelectorPresentationId = parentSelectorPresentationId;
        this.parentSelectCondition = parentSelectCondition;
        this.parentClassCatalogId = parentClassCatalogId;
        this.parentEditorPresentationIds = editPresIds;
        if (parentCreationEditorPresentationIds != null && parentCreationEditorPresentationIds.length != 0) {
            this.parentCreationEditorPresentationIds = Collections.unmodifiableList(Arrays.asList(parentCreationEditorPresentationIds));
        } else {
            this.parentCreationEditorPresentationIds = Collections.emptyList();
        }
        this.parentSelectorRestrictions = parentSelectorRestrictions != null ? parentSelectorRestrictions : Restrictions.ZERO;
    }

    public final RadConditionDef getParentSelectCondition(final RadClassPresentationDef classPres) {
        final RadParentTitlePropertyPresentationDef propPres = getPtPropPres(classPres, EPropAttrInheritance.PARENT_SELECT_CONDITION);
        return propPres != null ? propPres.parentSelectCondition : null;
    }

    public final RadEntityTitleFormatDef getTitleFormat(final RadClassPresentationDef classPres) {
        final RadParentTitlePropertyPresentationDef propPres = getPtPropPres(classPres, EPropAttrInheritance.PARENT_TITLE_FORMAT);
        return propPres != null ? propPres.parentTitleFormat : null;
    }

    private final RadParentTitlePropertyPresentationDef getPtPropPres(final RadClassPresentationDef classPres, final EPropAttrInheritance ihertBit) {
        final RadPropertyPresentationDef propPres = super.getPropPres(classPres, ihertBit);
        if (propPres == null) {
            return null;
        }
        if (propPres instanceof RadParentTitlePropertyPresentationDef) {
            return (RadParentTitlePropertyPresentationDef) propPres;
        }
        throw new WrongFormatError("Only RadParentTitlePropertyPresentation can be used as parent title property presentation", null);
    }

    private static Id getDestinationClassId(RadPropDef prop, final Release release) {
        if (prop instanceof RadParentPropDef) {
            final RadClassPresentationDef classPres = release.getClassDef(((RadParentPropDef) prop).getJoinedClassId()).getPresentation();
            prop = classPres.getClassDef().getPropById(((RadParentPropDef) prop).getJoinedPropId());
        }
        return ((IRadRefPropertyDef) prop).getDestinationClassId();
    }

    /**
     * @return the parentSelectorPresentationId
     */
    public Id getParentSelectorPresentationId(final RadClassPresentationDef classPres) {
        final RadParentTitlePropertyPresentationDef propPres = getPtPropPres(classPres, EPropAttrInheritance.PARENT_SELECTOR);
        if (propPres != null) {
            return propPres.parentSelectorPresentationId;
        }
        final RadPropDef prop = classPres.getClassDef().getPropById(getPropId());
        final Id parentClassId = getDestinationClassId(prop, classPres.getClassDef().getRelease());
        try {
            return classPres.getClassDef().getRelease().getClassDef(parentClassId).getPresentation().getDefaultSelectorPresentation().getId();
        } catch (DefinitionError error) {
            throw new DefinitionError("Can't get parent selector presentation for property " + prop.getId().toString(), error);
        }
    }

    public List<Id> getParentCreationEditorPresentationIds(final RadClassPresentationDef classPres) {
        final RadParentTitlePropertyPresentationDef propPres = getPtPropPres(classPres, null);
        final RadPropDef prop = classPres.getClassDef().getPropById(getPropId());
        if (prop.getValType() == EValType.OBJECT) {
            return propPres != null ? propPres.parentCreationEditorPresentationIds : null;
        } else {
            final Id parentSelPresId = getParentSelectorPresentationId(classPres);
            final Id parentClassId = getDestinationClassId(prop, classPres.getClassDef().getRelease());
            final RadClassDef parentClassDef = classPres.getClassDef().getRelease().getClassDef(parentClassId);
            return parentClassDef.getPresentation().getSelectorPresentationById(parentSelPresId).getCreationEditorPresentationIds();
        }
    }

    /**
     * @return the parentClassCatalogId
     */
    public Id getParentClassCatalogId(final RadClassPresentationDef classPres) {
        final RadParentTitlePropertyPresentationDef propPres =
                getPtPropPres(classPres, null /*RADIX-2903: classCatalog attribute is not inherited*/);
        return propPres != null ? propPres.parentClassCatalogId : null;
    }

    public Restrictions getParentSelectRestrictions() {
        return parentSelectorRestrictions;
    }

    public Collection<RadEditorPresentationDef> getParentEditorPresentations(final RadClassPresentationDef classPres) {
        final RadParentTitlePropertyPresentationDef propPres = getPtPropPres(classPres, EPropAttrInheritance.EDITING);
        if (propPres == null || propPres.parentEditorPresentationIds == null || propPres.parentEditorPresentationIds.length == 0) {
            return Collections.emptyList();
        }
        final Collection<RadEditorPresentationDef> result = new LinkedList<>();
        final RadPropDef prop = classPres.getClassDef().getPropById(getPropId());
        final Id parentClassId = getDestinationClassId(prop, classPres.getClassDef().getRelease());
        final RadClassDef parentClassDef = classPres.getClassDef().getRelease().getClassDef(parentClassId);
        for (Id editorPresentationId : propPres.parentEditorPresentationIds) {
            result.add(parentClassDef.getPresentation().getEditorPresentationById(editorPresentationId));
        }
        return result;
    }
}
