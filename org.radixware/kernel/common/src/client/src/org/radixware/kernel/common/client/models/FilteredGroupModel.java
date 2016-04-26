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
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.exceptions.ServiceClientException;


public abstract class FilteredGroupModel extends ProxyGroupModel {

    private final List<Integer> entityIndexes = new ArrayList<>();
    private int lastSourceIndex = -1;

    public FilteredGroupModel(final GroupModel source) {
        super(source);
    }

    //                          self methods
    @Override
    public final int mapEntityIndexToSource(final int index) {
        return index >= 0 && index < entityIndexes.size() ? entityIndexes.get(index) : -1;
    }

    @Override
    public final int mapEntityIndexFromSource(final int index) {
        return entityIndexes.indexOf(index);
    }

    @Override
    public void invalidate() {
        entityIndexes.clear();
        lastSourceIndex = -1;
    }

    private void fillFromSource() throws ServiceClientException, InterruptedException {
        for (int i = lastSourceIndex + 1; i < getSourceGroupModel().getEntitiesCount(); i++) {
            try{
                if (filterAcceptsEntity(getSourceGroupModel().getEntity(i))) {
                    entityIndexes.add(i);
                }
            }
            catch(BrokenEntityObjectException exception){
                continue;//NOPMD
            }
        }
        lastSourceIndex = getSourceGroupModel().getEntitiesCount() - 1;
    }

    //                          abstract methods
    protected abstract boolean filterAcceptsEntity(final EntityModel entity);

    //                          reimplemented methods
    @Override
    protected void readMore() throws ServiceClientException, InterruptedException {
        if (getSourceGroupModel().hasMoreRows()) {
            readMoreImpl();
        }
        fillFromSource();
    }
    
    private void readMoreImpl() throws ServiceClientException, InterruptedException {
        final int keepPageSize = getSourceGroupModel().getReadPageSize();
        getSourceGroupModel().setReadPageSize(getReadPageSize());
        try{
            getSourceGroupModel().readMore();
        }finally{
            getSourceGroupModel().setReadPageSize(keepPageSize);
        }        
    }

    @Override
    public int readToEntity(Pid pid) throws InterruptedException, ServiceClientException {
        final int idx = getSourceGroupModel().readToEntity(pid);
        if (idx >= 0) {
            fillFromSource();
            return mapEntityIndexFromSource(idx);
        }
        return -1;
    }        

    @Override
    public int getEntitiesCount() {
        return entityIndexes.size();
    }

    @Override
    public EntityModel getEntity(int i) throws BrokenEntityObjectException, ServiceClientException, InterruptedException {
        if (i < 0) {
            return null;
        }
        while ((i >= getEntitiesCount()) && hasMoreRows()) {
            if (lastSourceIndex == (getSourceGroupModel().getEntitiesCount() - 1)) {
                readMoreImpl();
            }
            fillFromSource();
        }
        if (i < getEntitiesCount()) {
            return getSourceGroupModel().getEntity(mapEntityIndexToSource(i));
        }
        return null;
    }

    @Override
    public boolean hasMoreRows() {
        return super.hasMoreRows() || (lastSourceIndex < (getSourceGroupModel().getEntitiesCount() - 1));
    }

    @Override
    public void removeRow(int row) {
        if (row >= 0 && row < entityIndexes.size()) {
            entityIndexes.remove(row);
        }
    }

    @Override
    public boolean isEmpty() {
        return entityIndexes.isEmpty() && !hasMoreRows();
    }

    @Override
    public int findEntityByPid(Pid pid) {
        if (pid != null) {
            final int sourceEntitiesCount = getSourceGroupModel().getEntitiesCount();
            for (Integer i : entityIndexes) {                
                if (i < sourceEntitiesCount && getSourceGroupModel().getRow(i).getPid().equals(pid)) {
                    return mapEntityIndexFromSource(i);
                }
            }
        }
        return -1;
    }

    @Override
    public void reset() {
        lastSourceIndex = -1;
        super.reset();
    }
}
