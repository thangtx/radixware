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
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.exceptions.ServiceClientException;


public class AboveReaderController extends AbstractReaderController {
    
    private final GroupModel groupModel;
    private boolean rowLimitExceeded;
    private boolean hasMore;
            
    public AboveReaderController(final GroupModel groupModel) {
        super();
        this.groupModel = groupModel;
    }

    public AboveReaderController(final GroupModel groupModel, final int maxRowsCount) {
        super(maxRowsCount);
        this.groupModel = groupModel;
    }

    @Override
    protected boolean updateEntities(final GroupModelData selectResult) {        
        if (groupModel.getEntitiesCount()==0){
            hasMore = selectResult.hasMore();
            getLastEntities().clear();
            getLastEntities().addAll(selectResult.getEntityModels());
            return false;
        }else{
            hasMore = groupModel.hasMoreRows();
            return super.updateEntities(selectResult);
        }
    } 
        
    @Override
    public boolean hasMore(final EntityModel newModel) {
            try{
                return !groupModel.isEmpty() && !groupModel.getEntity(0).getPid().equals(newModel.getPid());
            } catch(ServiceClientException | InterruptedException e) {
                return false;//do not expect this exceptions here
            } catch(BrokenEntityObjectException exception) {
                return !exception.getPid().equals(newModel.getPid());
            }
    }

    @Override
    public List<EntityModel> merge(final List<EntityModel> currentModels, final List<EntityModel> newModels, final EntityModel currentModel) {
        if (newModels.isEmpty()) {
            return currentModels;
        } else {
            final List<EntityModel> resultModels = new ArrayList<>();
            resultModels.addAll(newModels);
            resultModels.addAll(currentModels);            
            final int size = resultModels.size();
            final int maxSize = getMaxRowsCount();
            rowLimitExceeded = size > maxSize;
            if (rowLimitExceeded) {
                resultModels.subList(maxSize,size).clear();
            }
            return resultModels;
        }
    }

    @Override
    public ScrollPosition scrollPosition() {
        return IReaderController.ScrollPosition.TOP;
    }   

    @Override
    public boolean hasMoreAfterMerge() {
        return hasMore || rowLimitExceeded || groupModel.hasMoreRows();
    }    
    
    @Override
    protected void clear() {
        rowLimitExceeded = false;
        hasMore = false;
        super.clear();
    }
}