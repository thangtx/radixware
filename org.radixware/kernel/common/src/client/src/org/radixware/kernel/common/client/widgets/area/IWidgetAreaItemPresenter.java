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

import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;

public interface IWidgetAreaItemPresenter {

    void setBounds(int gridTop, int gridLeft, int gridWidth, int gridHeight);

    void setContent(IWidget widget);
    
    void setTitle(String title);
    
    void addAction(Action action);
    
    void addAction(int pos, Action action);
    
    void removeAction(Action action);
    
    Action getRemoveAction();
    
    Action getResizeAction();
}
