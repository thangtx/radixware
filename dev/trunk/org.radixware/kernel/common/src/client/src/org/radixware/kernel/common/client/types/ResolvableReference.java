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

package org.radixware.kernel.common.client.types;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;


public final class ResolvableReference extends Reference{
    
    private static final long serialVersionUID = 1099334629308466759L;
    
    private final Id editorPresentationId;
    
    public ResolvableReference(final EntityModel entityModel){
        super(entityModel);
        editorPresentationId = entityModel.getEditorPresentationDef().getId();
    }
    
    public ResolvableReference(final ResolvableReference reference){        
        super(reference);
        editorPresentationId = reference.editorPresentationId;
    }    

    public ResolvableReference(final Pid pid, final Id classId, final Id editorPresentationId) {
        super(pid, null, null, null, classId);
        this.editorPresentationId = editorPresentationId;        
    }
            
    public Id getEditorPresentationId(){
        return editorPresentationId;
    }    
    
    public EntityModel resolve(final IClientEnvironment environment) throws ServiceClientException, InterruptedException{
        return EntityModel.openContextlessModel(environment, getPid(), getClassId(), editorPresentationId);
    }
    
    @Override
    public String toValAsStr(){
        return new ArrStr(getPid().getTableId().toString(), 
                          getPid().toString(), 
                          getClassId().toString(), 
                          editorPresentationId.toString()).toString();
    }
    
    @Override
    public Reference actualizeTitle(final IClientEnvironment environment, final Id tableId) throws InterruptedException, ServiceClientException{
        return super.actualizeTitle(environment, tableId, editorPresentationId);
    }
}
