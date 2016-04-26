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

package org.radixware.kernel.common.client.models;

import java.util.EnumSet;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.enums.ERestriction;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.errors.ModelError;
import org.radixware.kernel.common.client.errors.OperationIsNotAllowedError;
import org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException;
import org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.types.GroupRestrictions;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.ColorScheme;
import org.radixware.kernel.common.client.models.groupsettings.Filters;
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.common.client.models.items.SelectorColumns;
import org.radixware.kernel.common.client.types.InstantiatableClass;


public abstract class ProxyGroupModel extends GroupModel {

    private final GroupModel source;

    public ProxyGroupModel(final GroupModel source) {
        super(source.getEnvironment(),source.getSelectorPresentationDef());
        this.source = source;
        source.linkedGroups.add(getThis());
        setContext(source.getContext());
    }

    private ProxyGroupModel getThis() {
        return this;
    }
    //                          self methods

    public final GroupModel getSourceGroupModel() {
        return source;
    }

    //                          abstract methods
    public abstract int mapEntityIndexToSource(final int index);

    public abstract int mapEntityIndexFromSource(final int index);

    public abstract void invalidate();

    //                          wrapped methods
    @Override
    public org.radixware.schemas.xscml.Sqml getCondition() {
        return source.getCondition();
    }

    @Override
    public FilterModel getCurrentFilter() {
        return source.getCurrentFilter();
    }
    
    @Override
    public RadSortingDef getCurrentSorting() {
        return source.getCurrentSorting();
    }    

    @Override
    public Filters getFilters() {
        return source.getFilters();
    }
    
    @Override
    public Sortings getSortings() {
        return source.getSortings();
    }    

    @Override
    public GroupRestrictions getRestrictions() {
        return source.getRestrictions();
    }

    @Override
    public SelectorColumnModelItem getSelectorColumn(Id propertyId) {
        return source.getSelectorColumn(propertyId);
    }

    @Override
    public SelectorColumns getSelectorColumns() {
        return source.getSelectorColumns();
    }

    @Override
    public RadSelectorPresentationDef getSelectorPresentationDef() {
        return source.getSelectorPresentationDef();
    }

    @Override
    public boolean hasMoreRows() {
        return source.hasMoreRows();
    }

    @Override
    public EntityModel openCreatingEntity(Id classId, EntityModel src, Map<Id, java.lang.Object> initialValues) throws ServiceClientException, InterruptedException {
        return source.openCreatingEntity(classId, src, initialValues);
    }

    @Override
    public EntityModel openCreatingEntity(Id classId, EntityModel src) throws ServiceClientException, InterruptedException {
        return source.openCreatingEntity(classId, src);
    }

    @Override
    public void clean() {
        reset();
    }

    @Override
    public void reset() {
        source.reset();
    }
    // events

    @Override
    protected Id onSelectCreationClass() {
        return source.onSelectCreationClass();
    }

    @Override
    protected void afterDeleteAll() {
        source.afterDeleteAll();
    }

    @Override
    protected boolean beforeDeleteAll() {
        return source.beforeDeleteAll();
    }

    @Override
    protected boolean beforePresentClassList(List<InstantiatableClass> classes) {
        return source.beforePresentClassList(classes);
    }

    @Override
    protected void afterRead(final List<EntityModel> newEntities, final List<Id> serverDisabledCommands, final EnumSet<ERestriction> serverRestrictions) {
        source.afterRead(newEntities, serverDisabledCommands, serverRestrictions);
    }

    //                          reimplemented methods
    @Override
    public EntityModel getEntity(int i) throws BrokenEntityObjectException, ServiceClientException, InterruptedException {
        if (i < 0) {
            return null;
        }
        while ((i >= getEntitiesCount()) && hasMoreRows()) {
            readMore();
        }
        if (i < getEntitiesCount()) {
            return source.getEntity(mapEntityIndexToSource(i));
        }
        return null;
    }

    @Override
    public final void setContext(IContext.Abstract context) {
        if (getContext() == null || context == null) {
            super.setContext(context);
        } else {
            throw new IllegalUsageError("Cannot change context of ProxyGroupModel");
        }
    }
    
    @Override
    public boolean canSafelyClean(final CleanModelController parameters){
        return true;
    }

    //                          not defined methods
    @Override
    public boolean deleteAll(boolean forced) throws ServiceClientException, InterruptedException {
        throw new OperationIsNotAllowedError(ModelError.ErrorType.CANT_DELETE_GROUP, this);
    }

    @Override
    public List<Id> getAccessibleCommandIds() {
        //no commands allowed in filtered group model
        return Collections.emptyList();
    }

    @Override
    public void removeCondition() throws ServiceClientException, InterruptedException {
        throw new IllegalUsageError("cannot remove condition from filtered model");
    }

    @Override
    public void reread() throws ServiceClientException {
        throw new IllegalUsageError("cannot reread filtered model");
    }

    @Override
    public void setColorScheme(ColorScheme cs) throws ServiceClientException, InterruptedException {
        throw new IllegalUsageError("cannot set color scheme in filtered model");
    }

    @Override
    public void setCondition(org.radixware.schemas.xscml.Sqml condition) throws ServiceClientException, InterruptedException {
        throw new IllegalUsageError("cannot set condition for filtered model");
    }

    @Override
    public void setCondition(SqmlExpression expression) throws ServiceClientException, InterruptedException {
        throw new IllegalUsageError("cannot set condition for filtered model");
    }

    @Override
    public void setFilter(FilterModel newFilter) throws PropertyIsMandatoryException, InvalidPropertyValueException, ServiceClientException, InterruptedException {
        throw new IllegalUsageError("cannot set filter for filtered model");
    }
}
