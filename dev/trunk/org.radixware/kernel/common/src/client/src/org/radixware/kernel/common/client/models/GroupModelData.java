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

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.types.InstantiatableClass;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;


public class GroupModelData {
    
    private final List<EntityModel> entityModels;
    private final org.radixware.schemas.eas.CommonFilters commonFilters;
    private final Id filterId;
    private final Id sortingId;
    private final EnumSet<ERestriction> restrictions;
    private final List<Id> disabledCommands;
    private final List<InstantiatableClass> instantiatableClasses;
    private final boolean hasMore;
    
    public GroupModelData(final List<EntityModel> entityModels,
                          final org.radixware.schemas.eas.CommonFilters commonFilters,
                          final Id currentFilterId,
                          final Id currentSortingId,
                          final EnumSet<ERestriction> restrictions,
                          final List<Id> disabledCommands,
                          final List<InstantiatableClass> instantiatableClasses,
                          final boolean hasMore){
        this.entityModels = entityModels==null ? Collections.<EntityModel>emptyList() : new ArrayList<>(entityModels);
        this.commonFilters = commonFilters==null ? null : (org.radixware.schemas.eas.CommonFilters)commonFilters.copy();
        filterId = currentFilterId;
        sortingId = currentSortingId;
        this.restrictions = restrictions==null ? EnumSet.noneOf(ERestriction.class) : restrictions.clone();
        this.disabledCommands = disabledCommands==null ? Collections.<Id>emptyList() : new ArrayList<>(disabledCommands);
        this.instantiatableClasses = instantiatableClasses==null ? null : new ArrayList<>(instantiatableClasses);
        this.hasMore = hasMore;
    }

    public List<EntityModel> getEntityModels() {
        return Collections.<EntityModel>unmodifiableList(entityModels);
    }

    public org.radixware.schemas.eas.CommonFilters getCommonFilters() {
        return commonFilters;
    }

    public Id getFilterId() {
        return filterId;
    }

    public Id getSortingId() {
        return sortingId;
    }

    public EnumSet<ERestriction> getRestrictions() {
        return restrictions;
    }

    public List<Id> getDisabledCommands() {
        return Collections.<Id>unmodifiableList(disabledCommands);
    }

    public List<InstantiatableClass> getInstantiatableClasses() {
        return instantiatableClasses == null ? null : Collections.unmodifiableList(instantiatableClasses);
    }

    public boolean hasMore() {
        return hasMore;
    }
}
