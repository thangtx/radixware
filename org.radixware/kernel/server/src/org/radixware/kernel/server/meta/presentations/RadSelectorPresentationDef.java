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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.enums.EPresentationAttrInheritance;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.roles.RadRoleDef;
import org.radixware.kernel.server.sqml.Sqml;
import org.radixware.kernel.server.types.Restrictions;
import org.radixware.schemas.xscml.SqmlDocument;

public class RadSelectorPresentationDef extends RadPresentationDef {

    /**
     * @return the editorPresentationIds
     */
    public List<Id> getEditorPresentationIds() {
        return editorPresentationIds;
    }
    private final List<Id> editorPresentationIds;
    private List<SelectorColumn> selectorColumns = null;
    private RadConditionDef condition = null;
    private Addons addons = null;
    private final Id classCatalogId;
    private final List<Id> creationEditorPresentationIds;
    private RadSelectorPresentationDef basePres = null;
    private boolean isBasePresLinked = false;
    private List<RadEditorPresentationDef> editorPresentations = null;

    public RadSelectorPresentationDef(
            final Id id,
            final String name,
            final Id basePresentationId,
            final int inheritanceMask,
            final SelectorColumn[] selectorColumns,
            final RadConditionDef condition,
            final Addons addons,
            final Id[] editorPresentationIds,
            final Id[] creationEditorPresentationIds,
            final Restrictions restrictions,
            final Id classCatalogId) {
        super(id, name, basePresentationId, inheritanceMask, null, restrictions);
        if ((inheritanceMask & CInheritance.SELECTOR_COLUMNS) == 0 && selectorColumns != null) {
            this.selectorColumns = Collections.unmodifiableList(Arrays.asList(selectorColumns));
        } else {
            this.selectorColumns = null;
        }
        if ((inheritanceMask & CInheritance.CONDITIONS) == 0) {
            this.condition = condition;
        }
        if ((inheritanceMask & CInheritance.ADDONS) == 0) {
            if (addons == null) {
                throw new WrongFormatError("Selector presentation #" + getId().toString() + " has no selector addons metainformation");
            }
            this.addons = addons;
        }
        if (editorPresentationIds != null && editorPresentationIds.length != 0) {
            this.editorPresentationIds = Collections.unmodifiableList(Arrays.asList(editorPresentationIds));
        } else {
            this.editorPresentationIds = Collections.emptyList();
        }
        this.classCatalogId = classCatalogId;
        if (creationEditorPresentationIds != null && creationEditorPresentationIds.length != 0) {
            this.creationEditorPresentationIds = Collections.unmodifiableList(Arrays.asList(creationEditorPresentationIds));
        } else {
            this.creationEditorPresentationIds = Collections.emptyList();
        }
    }

    @Override
    public void link() {
        super.link();
        getSelectorColumns();
        getCondition();
        getFiltersAndSortings();
        getBasePresentation();
        getEditorPresentations();
    }

    public List<Id> getCreationEditorPresentationIds() {
        return creationEditorPresentationIds;
    }

    public final Id getSelectionClassId() {
        return getClassPresentation().getId();
    }

    public final List<SelectorColumn> getSelectorColumns() {
        if (selectorColumns != null) // own or already linked
        {
            return selectorColumns;
        }
        if (getBasePresentationId() != null && (getInheritanceMask() & CInheritance.SELECTOR_COLUMNS) != 0) {
            //inherited from base presentation
            selectorColumns = getBasePresentation().getSelectorColumns(); // linking for feature
        }
        if (selectorColumns == null) {
            selectorColumns = Collections.emptyList();
        }
        return selectorColumns;
    }

    public final RadConditionDef getCondition() {
        if (condition != null) // own or already linked
        {
            return condition;
        }
        if (getBasePresentationId() != null && (getInheritanceMask() & CInheritance.CONDITIONS) != 0) {
            //inherited from base presentation
            condition = getBasePresentation().getCondition(); // linking for feature
        }
        return condition;
    }

    public final Id getClassCatalogId() {
        if (getBasePresentationId() != null && (getInheritanceMask() & CInheritance.CLASS_CATALOG) != 0) {
            return getBasePresentation().getClassCatalogId();
        }
        return classCatalogId;
    }

    /**
     * @return Always return an Addons structure (not null).
     */
    public final Addons getFiltersAndSortings() {
        if (addons != null) // own or already linked
        {
            return addons;
        }
        if (getBasePresentationId() != null && (getInheritanceMask() & CInheritance.ADDONS) != 0) {
            //inherited from base presentation
            addons = getBasePresentation().getFiltersAndSortings(); // linking for feature
        }
        if (addons == null) {
            throw new WrongFormatError("Selector presentation #" + getId().toString() + " has no selector addons metainformation");
        }
        return addons;
    }

    @Override
    protected RadSelectorPresentationDef getBasePresentation() {
        if (!isBasePresLinked) {
            if (getBasePresentationId() == null) {
                basePres = null;
            } else {
                basePres = getClassPresentation().getSelectorPresentationById(getBasePresentationId());
            }
            isBasePresLinked = true;
        }
        return basePres;
    }

    public final List<RadEditorPresentationDef> getEditorPresentations() {
        if (editorPresentations == null) {
            editorPresentations = new ArrayList<RadEditorPresentationDef>(getEditorPresentationIds().size());
            for (Id presId : getEditorPresentationIds()) {
                editorPresentations.add(getClassPresentation().getEditorPresentationById(presId));
            }
            editorPresentations = Collections.unmodifiableList(editorPresentations);
        }
        return editorPresentations;
    }

    @Override
    public final Collection<RadPropDef> getUsedPropDefs(final RadClassPresentationDef classPres) {
        if ((this.getInheritanceMask() & CInheritance.SELECTOR_COLUMNS) == 0) { //if not inherited
            final List<SelectorColumn> selCols = getSelectorColumns();
            if (selCols.isEmpty()) {
                return Collections.emptyList();
            } else {
                final Collection<RadPropDef> usedPropDefs = new ArrayList<RadPropDef>(selCols.size());
                for (SelectorColumn column : selCols) {
                    usedPropDefs.add(classPres.getClassDef().getPropById(column.getPropId()));
                }
                return Collections.unmodifiableCollection(usedPropDefs);
            }
        } else { // inherited
            return getBasePresentation().getUsedPropDefs(classPres);
        }
    }//Protected method

    public final String getRequestedRoleIdsStr() {
        return RadClassPresentationDef.getRequestedRoleIdsStr(getEditorPresentations());
    }

    @Override
    protected Restrictions calcRoleRestr(final RadRoleDef role) {
        final String resHashKey = RadRoleDef.generateResHashKey(EDrcResourceType.SELECTOR_PRESENTATION, getClassPresentation().getId(), getId());
        return role.getResourceRestrictions(resHashKey);
    }

    static public final class CInheritance {

        static public final int CONDITIONS = EPresentationAttrInheritance.CONDITION.getValue().intValue();
        static public final int CUSTOM_SELECTOR = EPresentationAttrInheritance.CUSTOM_DIALOG.getValue().intValue();
        static public final int ADDONS = EPresentationAttrInheritance.ADDONS.getValue().intValue();
        static public final int RESTRICTIONS = EPresentationAttrInheritance.RESTRICTIONS.getValue().intValue();
        static public final int SELECTOR_COLUMNS = EPresentationAttrInheritance.COLUMNS.getValue().intValue();
        static public final int CLASS_CATALOG = EPresentationAttrInheritance.CLASS_CATALOG.getValue().intValue();
    }

    static public final class SelectorColumn {

        private final Id propId;
        private final boolean isVisible;
        //Constructor

        public SelectorColumn(
                final Id propId,
                final boolean isVisible) {
            this.propId = propId;
            this.isVisible = isVisible;
        }

        /**
         * @return the propId
         */
        public Id getPropId() {
            return propId;
        }

        /**
         * @return the isVisible
         */
        public boolean isVisible() {
            return isVisible;
        }
    }

    /**
     * Filters and sortings options
     */
    static public final class Addons {

        private final Id defaultSortingId;
        private final boolean isAnyBaseSortingEnabled;
        private final List<Id> enabledBaseSortingIds;
        private final boolean isCustomSortingEnabled;
        private final Id defaultFilterId;
        public final boolean isAnyBaseFilterEnabled;
        private final List<Id> enabledBaseFilterIds;
        private final boolean isFilterObligatory;
        private final boolean isCustomFilterEnabled;
        private final Sqml defaultHint;
        private RadSortingDef defaultSorting = null;

        //Constructor
        public Addons(
                final Id defaultSortingId,
                final boolean isAnyBaseSortingEnabled,
                final Id[] enabledBaseSortingIds,
                final boolean isAnyCustomUserSortingEnabled,
                final Id defaultFilterId,
                final boolean isAnyBaseFilterEnabled,
                final Id[] enabledBaseFilterIds,
                final boolean isFilterObligatory,
                final boolean isAnyCustomFilterEnabled,
                final String defaultHint //                ,final Id defaultColorSchemeId
        ) {
            this(defaultSortingId, isAnyBaseSortingEnabled, enabledBaseSortingIds, isAnyCustomUserSortingEnabled, defaultFilterId, isAnyBaseFilterEnabled, enabledBaseFilterIds, isFilterObligatory, isAnyCustomFilterEnabled, defaultHint, null);
        }

        public Addons(
                final Id defaultSortingId,
                final boolean isAnyBaseSortingEnabled,
                final Id[] enabledBaseSortingIds,
                final boolean isAnyCustomUserSortingEnabled,
                final Id defaultFilterId,
                final boolean isAnyBaseFilterEnabled,
                final Id[] enabledBaseFilterIds,
                final boolean isFilterObligatory,
                final boolean isAnyCustomFilterEnabled,
                final String defaultHint,
                //                final Id defaultColorSchemeId,
                final String layerUri) {
            this.defaultSortingId = defaultSortingId;
            this.isAnyBaseSortingEnabled = isAnyBaseSortingEnabled;
            if (enabledBaseSortingIds != null) {
                this.enabledBaseSortingIds = Collections.unmodifiableList(Arrays.asList(enabledBaseSortingIds));
            } else {
                this.enabledBaseSortingIds = Collections.emptyList();
            }
            this.isCustomSortingEnabled = isAnyCustomUserSortingEnabled;
            this.defaultFilterId = defaultFilterId;
            this.isAnyBaseFilterEnabled = isAnyBaseFilterEnabled;
            if (enabledBaseFilterIds != null) {
                this.enabledBaseFilterIds = Collections.unmodifiableList(Arrays.asList(enabledBaseFilterIds));
            } else {
                this.enabledBaseFilterIds = Collections.emptyList();
            }
            this.isFilterObligatory = isFilterObligatory;
            this.isCustomFilterEnabled = isAnyCustomFilterEnabled;
            final SqmlDocument expr;
            try {
                expr = defaultHint == null || defaultHint.length() == 0 ? null : SqmlDocument.Factory.parse(defaultHint);
            } catch (XmlException e) {
                throw new WrongFormatError("Can't parse default selector presentation hint SQML: " + ExceptionTextFormatter.getExceptionMess(e), e);
            }
            this.defaultHint = expr != null ? Sqml.Factory.loadFrom("SelPresDefHint", expr.getSqml()) : null;
            if (this.defaultHint != null) {
                this.defaultHint.setLayerUri(layerUri);
                this.defaultHint.switchOnWriteProtection();
            }

//            this.defaultColorSchemeId = defaultColorSchemeId;
        }

        public final boolean isBaseSortingEnabledById(final Id id) {
            if (isAnyBaseSortingEnabled()) {
                return true;
            }
            return enabledBaseSortingIds.contains(id);
        }

        public final boolean isBaseFilterEnabledById(final Id id) {
            if (isAnyBaseFilterEnabled) {
                return true;
            }
            return enabledBaseFilterIds.contains(id);
        }

        public final RadSortingDef getDefaultSorting(final RadClassPresentationDef classPres) {
            if (defaultSorting == null) {
                if (getDefaultSortingId() != null) {
                    defaultSorting = classPres.getSortingById(getDefaultSortingId());
                } else {
                    for (RadSortingDef srt : classPres.getSortings()) {
                        defaultSorting = srt;
                        if (!isBaseSortingEnabledById(defaultSorting.getId())) {
                            defaultSorting = null;
                        }
                        if (defaultSorting != null) {
                            break;
                        }
                    }
                }
            }
            return defaultSorting;
        }

        /**
         * @return the defaultSortingId
         */
        public Id getDefaultSortingId() {
            return defaultSortingId;
        }

        /**
         * @return the isAnyBaseSortingEnabled
         */
        public boolean isAnyBaseSortingEnabled() {
            return isAnyBaseSortingEnabled;
        }

        /**
         * @return the isCustomSortingEnabled
         */
        public boolean isCustomSortingEnabled() {
            return isCustomSortingEnabled;
        }

        /**
         * @return the defaultFilterId
         */
        public Id getDefaultFilterId() {
            return defaultFilterId;
        }

        /**
         * @return the isFilterObligatory
         */
        public boolean isFilterObligatory() {
            return isFilterObligatory;
        }

        /**
         * @return the isCustomFilterEnabled
         */
        public boolean isCustomFilterEnabled() {
            return isCustomFilterEnabled;
        }

        /**
         * @return the defaultHint
         */
        public Sqml getDefaultHint() {
            return defaultHint;
        }
    }
}
