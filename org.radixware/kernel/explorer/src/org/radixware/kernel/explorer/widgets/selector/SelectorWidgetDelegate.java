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

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.core.QModelIndex;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.widgets.selector.ISelectorWidgetDelegate;
import org.radixware.kernel.common.exceptions.ServiceClientException;


final class SelectorWidgetDelegate implements ISelectorWidgetDelegate{
    
    private final SelectorModel model;
    private final QModelIndex currentIndex;
    private final boolean updateRowsCount;
    
    public SelectorWidgetDelegate(final SelectorModel model, final QModelIndex index, final boolean updateRowsCount){
        this.model = model;
        this.currentIndex = index;
        this.updateRowsCount = updateRowsCount;
    }
    
    public SelectorWidgetDelegate(final SelectorModel model, final QModelIndex index){
        this(model, index, true);
    }

    @Override
    public int rowCount() {
        return model.rowCount(currentIndex);
    }

    @Override
    public boolean readMore() throws ServiceClientException, InterruptedException{
        return model.readMore(currentIndex);
    }

    @Override
    public boolean canReadMore() {
        return model.canReadMore(currentIndex);
    }

    @Override
    public GroupModel getChildGroup() {
        return model.getChildGroup(currentIndex);
    }

    @Override
    public void updateRowsCount(final GroupModel groupModel) {
        if (updateRowsCount || model.rowCount(currentIndex)>0){
            model.updateRowsCount(currentIndex);
        }
    }

    @Override
    public void increaseRowsLimit() {        
        model.increaseRowsLimit();        
    }
}
