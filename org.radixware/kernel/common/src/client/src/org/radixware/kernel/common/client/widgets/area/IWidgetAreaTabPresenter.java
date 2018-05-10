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
package org.radixware.kernel.common.client.widgets.area;

import org.radixware.kernel.common.client.IClientEnvironment;

public interface IWidgetAreaTabPresenter {
    
    public interface ItemListener{
        void onChangeGeometry(IWidgetAreaItemPresenter item, int gridLeft, int gridTop, int gridWidth, int gridHeight);
        boolean isResizable(IWidgetAreaItemPresenter item);
    }
    
    void setTitle(String title);
    void removeItem(IWidgetAreaItemPresenter item);
    IWidgetArea getWidgetsArea();
    void setClosable(boolean isClosable);
    void close();
    IWidgetAreaItemPresenter createItem(IWidgetAreaItemController itemController);
    void addItem(IWidgetAreaItemPresenter item, ItemListener listener, int gridLeft, int gridTop, int gridWidth, int gridHeight);
    IClientEnvironment getEnvironment();
}
