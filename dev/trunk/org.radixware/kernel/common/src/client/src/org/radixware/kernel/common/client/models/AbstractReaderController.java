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

import java.util.LinkedList;
import java.util.List;


public abstract class AbstractReaderController implements IReaderController {
    private final List<EntityModel> newEntities = new LinkedList<>();
    private final int maxRowsCount;
    
    protected AbstractReaderController(final int maxRowsCount) {
        this.maxRowsCount = maxRowsCount;
    }

    protected AbstractReaderController() {
        this.maxRowsCount = Integer.MAX_VALUE;
    }
    
    boolean updateEntities(GroupModelData selectResult) {
        boolean hasMore = selectResult.hasMore();
    
        int idx = 0;
        for(EntityModel e : selectResult.getEntityModels()) {
            if (++idx <= maxRowsCount && hasMore(e)) {
                newEntities.add(e);
            } else {
                hasMore = false;
                break;
            }
        }
                        
        return hasMore;
    }
    
    protected List<EntityModel> getLastEntities() {
        return newEntities;
    }
    
    protected void clear() {
        newEntities.clear();
    }
    
    protected int getMaxRowsCount() {
        return maxRowsCount;
    }
}