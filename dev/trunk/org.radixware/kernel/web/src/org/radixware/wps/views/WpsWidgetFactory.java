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

package org.radixware.wps.views;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.ICommandPushButton;
import org.radixware.kernel.common.client.widgets.ICommandToolButton;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.WidgetFactory;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.rwt.PushButton;
import org.radixware.wps.rwt.RwtMenu;
import org.radixware.wps.rwt.ToolButton;


public class WpsWidgetFactory implements WidgetFactory {   

    @Override
    public ICommandPushButton newCommandPushButton(Command cmd) {
        return new CommandPushButton(cmd);
    }

    @Override
    public ICommandToolButton newCommandToolButton(Command cmd) {
        return new CommandToolButton(cmd);
    }
    
    @Override
    public ICommandToolButton newCommandToolButton(Command cmd, Property property) {
        return new CommandToolButton(cmd,property);
    }    

    @Override
    public IPushButton newPushButton() {
        return new PushButton();
    }
    
    @Override
    public IPushButton newDialogButton(final EDialogButtonType buttonType, final IClientEnvironment environment) {
        final IPushButton button = newPushButton();
        IMessageBox.StandardButton.setupDialogButton(button, environment, buttonType);
        return button;
    }    

    @Override
    public IButton newToolButton() {
        return new ToolButton();
    }

    @Override
    public Action newAction(final Icon icon, final String title) {
        return new RwtAction(icon, title);
    }        

    @Override
    public IMenu newMenu() {
        return new RwtMenu();
    }        
}
