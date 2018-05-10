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

package org.radixware.kernel.server.arte.services.eas;

import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadSortingDef;
import org.radixware.kernel.server.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.presentations.ICommonSelectorAddon;
import org.radixware.kernel.server.meta.presentations.IRadFilter;
import org.radixware.kernel.server.meta.presentations.RadFilterDef;
import org.radixware.schemas.groupsettings.FilterParameters;


final class CommonSelectorFilter implements IRadFilter, ICommonSelectorAddon{
    
    private final Id id;
    private final Id tableId;
    private final RadFilterDef baseFilter;
    private final String title;  
    private final long timestamp;
    private final Sqml condition;    
    private final FilterParameters parameters;
    
    public CommonSelectorFilter(final Id id, 
                                final Id tableId,
                                final RadFilterDef baseFilter,
                                final String title,
                                final long modifyTime,
                                final Sqml condition,
                                final FilterParameters params){
        this.id = id;
        this.tableId = tableId;
        this.baseFilter = baseFilter;
        this.title = title;
        timestamp = modifyTime;
        this.condition = condition;
        this.parameters = params;
    }
    
    public void addToXml(final org.radixware.schemas.eas.CommonFilter filter){
        filter.setId(id);
        if (baseFilter!=null)
            filter.setBaseFilterId(baseFilter.getId());
        filter.setTitle(title);        
        filter.setLastUpdateTime(timestamp);
        if (parameters!=null)
            filter.setParameters(parameters);
        if (condition!=null){
            condition.appendTo(filter.addNewCondition());
        }
    }

    @Override
    public final Sqml getCondition() {
        return condition;
    }

    @Override
    public Sqml getAdditionalFrom() {
        return null;
    }
        

    @Override
    public final Id getId() {
        return id;
    }

    public final  Id getTableId() {
        return tableId;
    }

    public final String getTitle() {
        return title;
    }
    
    @Override
    public final long getLastUpdateTime(){
        return timestamp;
    }

    @Override
    public RadSortingDef getDefaultSorting(RadClassPresentationDef classPres) {
        return baseFilter==null ? null : baseFilter.getDefaultSorting(classPres);
    }

    @Override
    public Sqml getSortingHintById(Id srtId, Sqml sortingHint) {
        return baseFilter==null ? null : baseFilter.getSortingHintById(srtId, sortingHint);
    }

    @Override
    public boolean isAnyCustomSortingEnabled() {
        return baseFilter==null ? true : baseFilter.isAnyCustomSortingEnabled();
    }

    @Override
    public boolean isBaseSortingEnabledById(Id srtId) {
        return baseFilter==null ? true : baseFilter.isBaseSortingEnabledById(srtId);
    }

    @Override
    public String getInfo() {
        return "common filter "+String.valueOf(title) + " (#" + id.toString()+")";
    }
    
    public boolean isSameFiler(final org.radixware.schemas.eas.Filter fltXml){
        return fltXml.getLastUpdateTime()!=null && fltXml.getLastUpdateTime().longValue()==timestamp;
    }
}
