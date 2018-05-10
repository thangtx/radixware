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

package org.radixware.kernel.common.client.meta;

import org.radixware.kernel.common.client.meta.filters.RadFilterDef;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchNameError;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.ESelectorColumnAlign;
import org.radixware.kernel.common.enums.ESelectorColumnSizePolicy;
import org.radixware.kernel.common.enums.ESelectorColumnVisibility;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;

public class RadSelectorPresentationDef extends RadPresentationDef {
    
    private final static long SELECTOR_PRESENTATION_RESTRICTIONS = 
        ERestriction.toBitField(EnumSet.of(ERestriction.INSERT_INTO_TREE, ERestriction.INSERT_ALL_INTO_TREE, ERestriction.RUN_EDITOR, 
                                           ERestriction.EDITOR, ERestriction.CREATE, ERestriction.DELETE, 
                                           ERestriction.DELETE_ALL, ERestriction.UPDATE, ERestriction.MULTIPLE_COPY,
                                           ERestriction.CONTEXTLESS_USAGE, ERestriction.ANY_COMMAND));
    

    protected SelectorColumn[] columns;
    private RadSelectorPresentationDef basePres = null;

    @Override
    protected RadSelectorPresentationDef getBasePresentation() {
        if (basePres == null && basePresentationId != null) {
            basePres = getDefManager().getSelectorPresentationDef(basePresentationId);
        }
        return basePres;
    }

    public final class SelectorColumn {

        private final Id propertyId;
        private final ESelectorColumnVisibility visibility;
        private final ESelectorColumnAlign alignment;
        private final ESelectorColumnSizePolicy sizePolicy;
        private final Id titleId;

        public SelectorColumn(final Id propId,
                final ESelectorColumnVisibility visibility,
                final ESelectorColumnAlign align,
                final ESelectorColumnSizePolicy sizePolicy,
                final Id titleId//равен NULL, eсли заголовок наследуется
                ) {
            this.propertyId = propId;
            this.visibility = visibility;
            this.sizePolicy = sizePolicy;
            this.alignment = align;
            this.titleId = titleId;            
        }
        
        public SelectorColumn(final Id propId,
                final ESelectorColumnVisibility visibility,
                final ESelectorColumnAlign align,
                final Id titleId//равен NULL, eсли заголовок наследуется
                ) {
            this(propId,visibility,align,ESelectorColumnSizePolicy.MANUAL_RESIZE,titleId);
        }        

        public Id getPropertyId() {
            return propertyId;
        }

        public ESelectorColumnVisibility getVisibility() {
            return visibility;
        }

        public ESelectorColumnAlign getAlignment() {
            return alignment;
        }
        
        public ESelectorColumnSizePolicy getSizePolicy(){
            return sizePolicy;
        }

        public String getTitle() {
            if (titleId == null) {
                return getClassPresentation().getPropertyDefById(propertyId).getTitle(null);
            } else {
                final Id defId = RadSelectorPresentationDef.this.getOwnerClassId();
                return RadSelectorPresentationDef.this.getTitle(defId, titleId);
            }
        }

        public SelectorColumnModelItem newSelectorColumn(GroupModel group) {
            return new SelectorColumnModelItem(group, propertyId);
        }
    }

    public final class SelectorColumns implements Iterable<SelectorColumn> {

        private final List<SelectorColumn> columns;

        public SelectorColumns() {
            if (RadSelectorPresentationDef.this.columns != null
                    && RadSelectorPresentationDef.this.columns.length > 0) {
                columns = new ArrayList<>(RadSelectorPresentationDef.this.columns.length);
                Collections.addAll(columns, RadSelectorPresentationDef.this.columns);
            } else {
                columns = Collections.emptyList();
            }
        }
        /*
        public int count(){
        return columns.size();
        }
         */

        public SelectorColumn findColumn(final Id propertyId) {
            for (SelectorColumn column : columns) {
                if (column.getPropertyId().equals(propertyId)) {
                    return column;
                }
            }
            return null;
        }

        @Override
        public Iterator<SelectorColumn> iterator() {
            return columns.iterator();
        }
    }
    private SelectorColumns selectorColumns = null;

    public final SelectorColumns getSelectorColumns() {
        if (selectorColumns == null) {
            if (this.inheritanceMask.isSelectorColumnsInherited() && getBasePresentation() != null) {
                selectorColumns = getBasePresentation().getSelectorColumns();
            } else {
                selectorColumns = new SelectorColumns();
            }
        }
        return selectorColumns;
    }

    @Override
    protected boolean isCommandEnabled(RadPresentationCommandDef command) {
        return command.scope == ECommandScope.GROUP;
    }
    //Sortings
    protected Id[] sortingIds;
    private List<RadSortingDef> sortings = null;
    protected boolean isCustomSortingEnabled;
    protected Id defaultSortingId;
    private RadSortingDef defaultSorting = null;

    private void fillSortings() {
        if (inheritanceMask.isAddonsInherited() && getBasePresentation() != null) {
            sortings = getBasePresentation().getSortings();
            return;
        }
        if (sortingIds == null) {
            sortings = new ArrayList<>();
            sortings.addAll(getClassPresentation().getSortings());
        } else if (sortingIds != null && sortingIds.length > 0) {
            sortings = new ArrayList<>(sortingIds.length * 2);
            for (Id sortingId : sortingIds) {
                sortings.add(getClassPresentation().getSortingDefById(sortingId));
            }
        } else {
            sortings = Collections.emptyList();
        }
    }

    public final RadSortingDef getSortingDefById(final Id sortingId) {
        if (sortings == null) {
            fillSortings();
        }
        final RadSortingDef sorting = getClassPresentation().getSortingDefById(sortingId);
        if (sortings.contains(sorting)) {
            return sorting;
        }
        throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.SORTING, sortingId);
    }

    public final RadSortingDef getSortingDefByName(String name) {
        if (sortings == null) {
            fillSortings();
        }
        for (RadSortingDef sorting : sortings) {
            if (sorting.getName().equals(name)) {
                return sorting;
            }
        }
        throw new NoDefinitionWithSuchNameError((Definition) this, NoDefinitionWithSuchNameError.SubDefinitionType.SORTING, name);
    }

    public final boolean isSortingExists(final Id sortingId) {
        if (sortings == null) {
            fillSortings();
        }
        try {
            final RadSortingDef sorting = getClassPresentation().getSortingDefById(sortingId);
            return sortings.contains(sorting);
        } catch (DefinitionError err) {
            return false;
        }
    }

    public final boolean isAnySortingAcceptable() {
        if (inheritanceMask.isAddonsInherited() && getBasePresentation() != null) {
            return getBasePresentation().isAnySortingAcceptable();
        }
        return sortingIds == null;
    }
    
    public final Id[] getAcceptableBaseSortingIds(){
        if (inheritanceMask.isAddonsInherited() && getBasePresentation() != null) {
            return getBasePresentation().getAcceptableBaseSortingIds();
        }
        if (sortingIds==null){
            return null;
        }        
        final Id[] resultArray = new Id[sortingIds.length];
        if (sortingIds.length>0){
            System.arraycopy(sortingIds, 0, resultArray, 0, sortingIds.length);
        }
        return resultArray;
    }

    public final List<RadSortingDef> getSortings() {
        if (sortings == null) {
            fillSortings();
        }
        return Collections.unmodifiableList(sortings);
    }
    
    public final Id getDefaultSortingId(){
        if (inheritanceMask.isAddonsInherited() && getBasePresentation() != null) {
            return getBasePresentation().getDefaultSortingId();
        }
        return defaultSortingId;
    }

    public final RadSortingDef getDefaultSortingDef() {
        if (inheritanceMask.isAddonsInherited() && getBasePresentation() != null) {
            return getBasePresentation().getDefaultSortingDef();
        }
        if (defaultSortingId != null && defaultSorting == null) {
            try {
                defaultSorting = getSortingDefById(defaultSortingId);
            } catch (NoDefinitionWithSuchIdError err) {
                final String msg = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get default sorting #%s for selector presentation %s");
                getApplication().getTracer().error(String.format(msg, defaultSortingId, toString()), err);
            }
        }
        return defaultSorting;
    }

    public final boolean isCustomSortingEnabled() {
        if (inheritanceMask.isAddonsInherited() && getBasePresentation() != null) {
            return getBasePresentation().isCustomSortingEnabled();
        }
        return isCustomSortingEnabled;
    }
    
    
    //Filters
    protected Id[] filterIds;
    private List<RadFilterDef> filters = null;
    protected boolean isFilterObligatory;
    protected boolean isCustomFilterEnabled;
    protected Id defaultFilterId;    
    private RadFilterDef defaultFilter = null;

    private void fillFilters() {
        if (inheritanceMask.isAddonsInherited() && getBasePresentation() != null) {
            filters = getBasePresentation().getFilters();
        }else{
            if (filterIds == null) {
                filters = new ArrayList<>();
                filters.addAll(getClassPresentation().getFilters());
            } else if (filterIds != null && filterIds.length > 0) {
                filters = new ArrayList<>(filterIds.length * 2);
                for (Id filterId : filterIds) {
                    if (getClassPresentation().isFilterDefExists(filterId)){
                        filters.add(getClassPresentation().getFilterDefById(filterId));
                    }
                }
            } else {
                filters = Collections.emptyList();
            }
        }
    }

    public final RadFilterDef getFilterDefById(final Id filterId) {
        if (filters == null) {
            fillFilters();
        }
        if (getClassPresentation().isFilterDefExists(filterId)){
            final RadFilterDef filter = getClassPresentation().getFilterDefById(filterId);
            if (filters.contains(filter)) {
                return filter;
            }
        }
        throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.FILTER, filterId);
    }

    public final boolean isFilterExists(final Id filterId) {
        if (filters == null) {
            fillFilters();
        }
        try {
            final RadFilterDef filter = getClassPresentation().getFilterDefById(filterId);
            return filters.contains(filter);
        } catch (DefinitionError err) {
            return false;
        }
    }

    public final boolean isAnyFilterAcceptable() {
        if (inheritanceMask.isAddonsInherited() && getBasePresentation() != null) {
            return getBasePresentation().isAnyFilterAcceptable();
        }
        return filterIds == null;
    }

    public final RadFilterDef getFilterByName(final String name) {
        if (filters == null) {
            fillFilters();
        }
        for (RadFilterDef filter : filters) {
            if (filter.getName().equals(name)) {
                return filter;
            }
        }
        throw new NoDefinitionWithSuchNameError((Definition) this, NoDefinitionWithSuchNameError.SubDefinitionType.FILTER, name);
    }

    public final List<RadFilterDef> getFilters() {
        if (filters == null) {
            fillFilters();
        }
        return Collections.unmodifiableList(filters);
    }

    public final RadFilterDef getDefaultFilterDef() {
        if (inheritanceMask.isAddonsInherited() && getBasePresentation() != null) {
            return getBasePresentation().getDefaultFilterDef();
        }
        if (defaultFilterId != null && defaultFilter == null) {
            try {
                defaultFilter = getFilterDefById(defaultFilterId);
            } catch (NoDefinitionWithSuchIdError ex) {
                final String msg = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get default filter #%s for selector presentation %s");
                getApplication().getTracer().error(String.format(msg, defaultFilterId, toString()), ex);
            }
        }
        return defaultFilter;
    }

    public final boolean isFilterObligatory() {
        if (inheritanceMask.isAddonsInherited() && getBasePresentation() != null) {
            return getBasePresentation().isFilterObligatory();
        }
        return isFilterObligatory;
    }

    public final boolean isCustomFiltersEnabled() {
        if (inheritanceMask.isAddonsInherited() && getBasePresentation() != null) {
            return getBasePresentation().isCustomFiltersEnabled();
        }
        return isCustomFilterEnabled;
    }

    //Presentations
    private RadEditorPresentationDef creationPresentation = null;

    public RadEditorPresentationDef getCreationPresentation() {
        if (!getCreationPresentationIds().isEmpty() && creationPresentation == null) {
            final Id creationPresentationId = getCreationPresentationIds().get(0);
            try {
                creationPresentation = getDefManager().getEditorPresentationDef(creationPresentationId);
            } catch (DefinitionError err) {
                final String msg = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get creation presentation #%s for selector presentation %s");
                getApplication().getTracer().error(String.format(msg, creationPresentationId, toString()), err);
            }
        }
        return creationPresentation;
    }
    private Id[] editorPresentationIds;

    public List<Id> getEditorPresentationIds() {
        return editorPresentationIds == null ? Collections.<Id>emptyList() : Arrays.<Id>asList(editorPresentationIds);
    }
    private Id[] creationPresentationIds;

    public List<Id> getCreationPresentationIds() {
        return creationPresentationIds == null ? Collections.<Id>emptyList() : Arrays.<Id>asList(creationPresentationIds);
    }
    //Other attributes
    protected boolean isAutoExpandEnabled;

    public final boolean isAutoExpandEnabled() {
        return isAutoExpandEnabled;
    }
    
    protected final boolean isRestoringPositionEnabled;
    
    public final boolean isRestoringPositionEnabled(){
        return isRestoringPositionEnabled;
    }
    
    private final boolean isAutoSortInstantiatableClasses;
    
    public boolean autoSortInstantiatableClasses(){
        if (this.inheritanceMask.isClassCatalogInherited() && getBasePresentation() != null) {        
            return getBasePresentation().autoSortInstantiatableClasses();
        }else{
            return isAutoSortInstantiatableClasses;
        }
    }
    
    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    public RadSelectorPresentationDef(
            final Id id,
            final String name,
            final ERuntimeEnvironmentType type,
            final Id basePresentationId,
            final Id classId,
            final Id tableId,
            final Id titleId,
            final Id iconId,
            final Id[] contextlessCommandIds,
            final Id[] sortingIds,//Если null - доступны все. Если пустой - нет доступных
            final boolean isCustomSortingEnabled,
            final Id defaultSortingId,
            final Id[] filterIds,//Если null - доступны все. Если пустой - нет доступных
            final boolean isFilterObligatory,
            final boolean isCustomFilterEnabled,
            final Id defaultFilterId,
            final long restrictionsMask,
            final Id[] enabledCommandIds,
            final long inheritanceMask,
            final Id[] creationPresentationId,
            final Id[] editorPresentationIds, //RADIX-3734
            final boolean isAutoExpandEnabled,
            final boolean isRestoringPositionEnabled,
            final boolean isAutoSortInstantiatableClasses,
            final int sizeX,
            final int sizeY) {
        super(id, 
              name,
              type,
              basePresentationId,
              classId,
              tableId,
              titleId,
              iconId,
              contextlessCommandIds,
              fixRestrictionsMask(restrictionsMask),
              enabledCommandIds,
              inheritanceMask,
              sizeX,
              sizeY);
        this.creationPresentationIds = creationPresentationId;
        this.editorPresentationIds = editorPresentationIds;
        this.isAutoExpandEnabled = isAutoExpandEnabled;
        this.sortingIds = sortingIds;
        this.isCustomSortingEnabled = isCustomSortingEnabled;
        this.defaultSortingId = defaultSortingId;
        this.filterIds = filterIds;
        this.isFilterObligatory = isFilterObligatory;
        this.isCustomFilterEnabled = isCustomFilterEnabled;
        this.isAutoSortInstantiatableClasses = isAutoSortInstantiatableClasses;                 
        this.defaultFilterId = defaultFilterId;
        this.isRestoringPositionEnabled = isRestoringPositionEnabled;
    };
    

    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    public RadSelectorPresentationDef(
            final Id id,
            final String name,
            final ERuntimeEnvironmentType type,
            final Id basePresentationId,
            final Id classId,
            final Id tableId,
            final Id titleId,
            final Id iconId,
            final Id[] contextlessCommandIds,
            final Id[] sortingIds,//Если null - доступны все. Если пустой - нет доступных
            final boolean isCustomSortingEnabled,
            final Id defaultSortingId,
            final Id[] filterIds,//Если null - доступны все. Если пустой - нет доступных
            final boolean isFilterObligatory,
            final boolean isCustomFilterEnabled,
            final Id defaultFilterId,
            final long restrictionsMask,
            final Id[] enabledCommandIds,
            final long inheritanceMask,
            final Id[] creationPresentationId,
            final Id[] editorPresentationIds, //RADIX-3734
            final boolean isAutoExpandEnabled,
            final boolean isRestoringPositionEnabled,
            final int sizeX,
            final int sizeY) {
        this(id,
             name,
             type,
             basePresentationId,
             classId,
             tableId,
             titleId,
             iconId,
             contextlessCommandIds,
             sortingIds,//Если null - доступны все. Если пустой - нет доступных
             isCustomSortingEnabled,
             defaultSortingId,
             filterIds,//Если null - доступны все. Если пустой - нет доступных
             isFilterObligatory,
             isCustomFilterEnabled,
             defaultFilterId,
             restrictionsMask,
             enabledCommandIds,
             inheritanceMask,
             creationPresentationId,
             editorPresentationIds, //RADIX-3734
             isAutoExpandEnabled,
             isRestoringPositionEnabled,
             false,
             sizeX,
             sizeY);
    }
    
    public RadSelectorPresentationDef(
            final Id id,
            final String name,
            final ERuntimeEnvironmentType type,
            final Id basePresentationId,
            final Id classId,
            final Id tableId,
            final Id titleId,
            final Id iconId,
            final Id[] contextlessCommandIds,
            final Id[] sortingIds,//Если null - доступны все. Если пустой - нет доступных
            final boolean isCustomSortingEnabled,
            final Id defaultSortingId,
            final Id[] filterIds,//Если null - доступны все. Если пустой - нет доступных
            final boolean isFilterObligatory,
            final boolean isCustomFilterEnabled,
            final Id defaultFilterId,
            final long restrictionsMask,
            final Id[] enabledCommandIds,
            final long inheritanceMask,
            final Id[] creationPresentationId,
            final Id[] editorPresentationIds, //RADIX-3734
            final boolean isAutoExpandEnabled,
            final boolean isRestoringPositionEnabled) {
        this(id,
             name,
             type,
             basePresentationId,
             classId,
             tableId,
             titleId,
             iconId,
             contextlessCommandIds,
             sortingIds,//Если null - доступны все. Если пустой - нет доступных
             isCustomSortingEnabled,
             defaultSortingId,
             filterIds,//Если null - доступны все. Если пустой - нет доступных
             isFilterObligatory,
             isCustomFilterEnabled,
             defaultFilterId,
             restrictionsMask,
             enabledCommandIds,
             inheritanceMask,
             creationPresentationId,
             editorPresentationIds, //RADIX-3734
             isAutoExpandEnabled,
             isRestoringPositionEnabled,
             false,
             0,
             0);
    }
    
    private static long fixRestrictionsMask(final long rawRestrictions){
        return SELECTOR_PRESENTATION_RESTRICTIONS & rawRestrictions;
    }    

    @Override
    public boolean hasTitle() {
        if (inheritanceMask.isTitleInherited()) {
            return getBasePresentation() != null ? getBasePresentation().hasTitle() : getClassPresentation().hasGroupTitle();
        }
        return super.hasTitle();
    }

    @Override
    public String getTitle() {
        if (inheritanceMask.isTitleInherited()) {
            if (getBasePresentation() != null) {
                return getBasePresentation().getTitle();
            } else {
                return getClassPresentation().getGroupTitle();
            }
        }
        return super.getTitle();
    }

    //Реализация методов ModelDefinitions
    //Реализация методов ModelDefinitions
    @Override
    public GroupModel createModel(final IContext.Abstract context) {
        if (context == null) {
            throw new NullPointerException("Context must be not null");
        }
        final GroupModel model = (GroupModel) createModelImpl(context.getEnvironment());
        model.setContext(context);
        return model;
    }

    protected Model createModelImpl(final IClientEnvironment environment) {
        final Id modelClassId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_GROUP_MODEL_CLASS);
        try {
            Class<Model> classModel = environment.getApplication().getDefManager().getDefinitionModelClass(modelClassId);
            Constructor<Model> constructor = classModel.getConstructor(IClientEnvironment.class, RadSelectorPresentationDef.class);
            return constructor.newInstance(environment, this);
        } catch (Exception e) {
            throw new ModelCreationError(ModelCreationError.ModelType.GROUP_MODEL, this, null, e);
        }
    }

    @Override
    public IView createStandardView(final IClientEnvironment environment) {
        return getApplication().getStandardViewsFactory().newStandardSelector(environment);
    }
    
    @Override
    public final RadPropertyDef getPropertyDefById(final Id propertyId) {
        return getClassPresentation().getPropertyDefById(propertyId);
    }

    @Override
    public boolean isPropertyDefExistsById(final Id id) {
        return getClassPresentation().isPropertyDefExistsById(id);
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "selector presentation %s");
        return String.format(desc, super.getDescription());
    }
}
