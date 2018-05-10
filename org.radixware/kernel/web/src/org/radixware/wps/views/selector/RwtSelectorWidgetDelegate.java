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


package org.radixware.wps.views.selector;

import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.widgets.selector.ISelectorWidgetDelegate;


public final class RwtSelectorWidgetDelegate implements ISelectorWidgetDelegate {

    private final GroupModel model;
    private final SelectorWidgetController controller;

    public RwtSelectorWidgetDelegate(SelectorWidgetController selectorController, GroupModel g) {
        this.model = g;
        this.controller = selectorController;
        increaseRowsLimit();
    }

    @Override
    public int rowCount() {
        return controller.getRowCount();
    }

    @Override
    public boolean readMore() {
        if (model.getEntitiesCount()>controller.getRowCount()){
            controller.displayLoadedEntities(model);
            return true;
        }
        if (canReadMore()) {
            controller.readMore(model);
            if (model.getEntitiesCount()>controller.getRowCount()){
                controller.displayLoadedEntities(model);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canReadMore() {
        return /*rowCount() < model.getEntitiesCount() || */model.hasMoreRows();
    }

    @Override
    public GroupModel getChildGroup() {
        return model;
    }

    @Override
    public void updateRowsCount(final GroupModel g) {
        controller.displayLoadedEntities(g);
    }

    @Override
    public void increaseRowsLimit() {
        
    }
}
