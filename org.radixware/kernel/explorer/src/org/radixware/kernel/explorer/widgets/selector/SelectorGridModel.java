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
import java.util.EnumSet;
import org.radixware.kernel.common.client.enums.EHierarchicalSelectionMode;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.exceptions.ServiceClientException;


public class SelectorGridModel extends SelectorModel {
    
    private boolean informationDialogActive;

    public SelectorGridModel(final GroupModel group) {
        super(group);
    }

    @Override
    public boolean canCreateChild(EntityModel parentEntity) {
        return false;
    }

    @Override
    protected GroupModel createChildGroupModel(EntityModel parentEntity) {
        return null;
    }

    @Override
    protected boolean hasChildren(EntityModel parent) {
        return parent == null;
    }

    @Override
    public boolean canFetchMore(final QModelIndex parent) {
        if (parent==null && canReadMore(null) && !informationDialogActive){
            final int rowCount = rowCount(null);
            final int rowsLimit = getRowsLimit();
            if (rowsLimit>0 && rowsLimit<=rowCount){
                final String message =
                        getEnvironment().getMessageProvider().translate("Selector", "Number of loaded objects is %1s.\nYou may want to use filter to find specific object");
                informationDialogActive = true;
                try{
                    getEnvironment().messageInformation(null, String.format(message, rowCount));
                    increaseRowsLimit();
                }finally{
                    informationDialogActive = false;
                }
            }
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void fetchMore(QModelIndex parent) {
        if (parent == null && !informationDialogActive) {
            try{
                super.readMore(null);
            }
            catch(InterruptedException exception){
                //interrupted by user - nothing to do
            }
            catch(ServiceClientException exception){
                showErrorOnReceivingData(exception);
            }
        }
    }

    @Override
    protected EnumSet<EHierarchicalSelectionMode> getPrimarySelectionMode(final SelectorNode node) {
        if (node==null){
            return EnumSet.of(EHierarchicalSelectionMode.EXPLICIT_NESTED_OBJECTS);
        }else{
            return super.getPrimarySelectionMode(node);
        }
    }
    
    
}
