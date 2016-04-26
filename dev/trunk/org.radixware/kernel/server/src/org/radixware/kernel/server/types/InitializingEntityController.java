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

package org.radixware.kernel.server.types;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.enums.EEntityInitializationPhase;
import org.radixware.kernel.common.types.Id;


class InitializingEntityController {
    
    private final EEntityInitializationPhase phase;
    private Entity src;    
    private Set<Id> excludedFromCopyPropIds;
    private PropValHandlersByIdMap initialPropValsById;    
    
    public InitializingEntityController(final EEntityInitializationPhase phase){
        this.phase = phase;        
    }
    
    public final void setSrcEntity(final Entity src){
        this.src = src;
    }
    
    public final void setSrcEntity(final Entity src, final Collection<Id> excludedFromCopyProperties){
        this.src = src;
        excludedFromCopyPropIds = excludedFromCopyProperties==null ? null : new HashSet<>(excludedFromCopyProperties);
    }
    
    public final void setInitialPropVals(final PropValHandlersByIdMap initialPropVals){
        initialPropValsById = initialPropVals==null ? null : new PropValHandlersByIdMap(initialPropVals);;
    }
    
    public Entity getSrcEntity(){
        return src;
    }
    
    public PropValHandlersByIdMap getInitialPropVals(){
        return initialPropValsById==null ? new PropValHandlersByIdMap() : new PropValHandlersByIdMap(initialPropValsById);
    }
    
    public boolean canCopyPropertyValue(final Id propertyId){
        return excludedFromCopyPropIds==null || !excludedFromCopyPropIds.contains(propertyId);
    }
    
    public EEntityInitializationPhase getInitializationPhase(){
        return phase;
    }
}