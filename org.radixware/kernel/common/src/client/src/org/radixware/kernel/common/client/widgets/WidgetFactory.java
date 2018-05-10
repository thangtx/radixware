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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.enums.EDialogButtonType;


public interface WidgetFactory {

    ICommandPushButton newCommandPushButton(Command cmd);

    ICommandToolButton newCommandToolButton(Command cmd);
    
    ICommandToolButton newCommandToolButton(Command cmd, Property property);

    IPushButton newPushButton();
    
    IPushButton newDialogButton(EDialogButtonType buttonType, IClientEnvironment environment);

    IButton newToolButton();
    
    Action newAction(Icon icon, String title);
    
    IMenu newMenu();
}
