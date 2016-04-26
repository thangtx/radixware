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

package org.radixware.kernel.common.client.views;

import java.util.List;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.types.Id;


public interface IEmbeddedEditor extends IEmbeddedView {

    @Override
    public IEditor getView();

    @Override
    public EntityModel getModel();
    
    public void setPropertyRef(final PropertyRef property);
        
    public void setPresentations(final Id ownerClassId, final List<Id> editorPresentationIds);

    public void setPresentation(final Id ownerClassId, final Id editorPresentationId);

    public void setEntityPid(String pid);

    public String getEntityPid();    
    
    public void setClassOfNewEntity(final Id classId);
}
