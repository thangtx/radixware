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

package org.radixware.kernel.common.client.widgets;

import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.actions.Action;


public interface IButton extends IWidget {

    public interface ClickHandler {

        void onClick(IButton source);
    }

    String getTitle();

    void setTitle(String text);

    void setIcon(Icon icon);

    Icon getIcon();

    void addClickHandler(ClickHandler ch);
    
    void removeClickHandler(ClickHandler ch);
    
    void clearClickHandlers();

    public void addAction(Action action);        
}