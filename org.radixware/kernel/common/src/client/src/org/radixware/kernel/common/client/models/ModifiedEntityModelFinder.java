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

import org.radixware.kernel.common.client.types.Pid;


public class ModifiedEntityModelFinder implements IModelFinder{
    
    private final Pid objectPid;
    
    public ModifiedEntityModelFinder(final Pid pid){
        objectPid = pid;
    }

    @Override
    public boolean isTarget(final Model model) {
        if (model instanceof EntityModel){
            final EntityModel entityModel = ((EntityModel)model);
            return !entityModel.isNew()
                    && entityModel.getPid().equals(objectPid)
                    && entityModel.isEdited();
        }
        return false;

    }
    
}
