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

package org.radixware.kernel.common.client.widgets.actions;

import org.radixware.kernel.common.client.types.Icon;


public interface IMenu extends IActionPane{  
     IMenu addSubMenu(String title);
     IMenu addSubMenu(Icon icon, String title);
     void addSubSeparator();
     void insertMenu(Action before, IMenu menu);
     void insertMenu(IMenu before, IMenu menu);
     void insertSeparator(IMenu before);
     void clear();
     void setTitle(String title);
     String getTitle();
     void setIcon(Icon icon);
     Icon getIcon();
}
