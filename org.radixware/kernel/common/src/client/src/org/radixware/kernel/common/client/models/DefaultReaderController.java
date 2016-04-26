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
import java.util.List;



public class DefaultReaderController extends AbstractReaderController {
   
    @Override
    public boolean hasMore(EntityModel newModel) {
        return true;
    }

    @Override
    public List<EntityModel> merge(List<EntityModel> currentModels, List<EntityModel> newModels, EntityModel currentEntity) {
        final List<EntityModel> result = new ArrayList<>(newModels);
        
        for(int i = 0; i < currentModels.size(); i++) {
            final EntityModel e = currentModels.get(i);            
            int j = GroupModel.listContainsModel(result, e);
            if (j >= 0){            
                if (e instanceof BrokenEntityModel==false){
                    final EntityModel passedEntity = result.get(j);                
                    if (currentEntity == e
                            || e.isEdited()
                            || e.getLastReadTime() > passedEntity.getLastReadTime()) {
                        result.set(j, e);
                    } else {
                        e.clean();
                    }
                }
            } else {
                if(i < result.size()) { 
                    result.add(i, e);
                } else {
                    result.add(e);
                }
            }            
        }
        
        return result;
    }

    @Override
    public ScrollPosition scrollPosition() {
        return ScrollPosition.CURRENT;
    }
}
