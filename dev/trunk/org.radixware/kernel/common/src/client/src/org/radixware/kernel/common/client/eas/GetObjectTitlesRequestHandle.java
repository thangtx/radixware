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

package org.radixware.kernel.common.client.eas;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.schemas.eas.GetObjectTitlesMess;
import org.radixware.schemas.eas.GetObjectTitlesRq;
import org.radixware.schemas.eas.GetObjectTitlesRs;


public final class GetObjectTitlesRequestHandle extends RequestHandle{
    
    private EntityObjectTitles objectTitles;
    private final GetObjectTitlesRq.Objects objects;

    GetObjectTitlesRequestHandle(final IClientEnvironment environment, final GetObjectTitlesRq request) {
        super(environment, request, GetObjectTitlesMess.class);
        objects = (GetObjectTitlesRq.Objects)request.getObjects().copy();
    }    

    public EntityObjectTitles getEntityObjectTitles() throws ServiceClientException, InterruptedException{
        if (objectTitles==null){
            objectTitles = new EntityObjectTitles(environment, (GetObjectTitlesRs)getResponse(), objects);
        }
        return objectTitles;
    }
    
}
