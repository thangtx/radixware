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

import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.ICommandToolButton;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.ToolButton;


class CommandToolButton extends ToolButton implements ICommandToolButton {

    private final ICommandToolButton.CommandController controller = new ICommandToolButton.CommandController();

    public CommandToolButton(Command command) {
        this(command, null);
    }

    public CommandToolButton(Command command, Property property) {
        super();        
        controller.open(command, property);
        refresh(command);        
        addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                controller.executeCommand();                
            }
        });
    }

    @Override
    public boolean close() {
        controller.getCommand().unsubscribe(this);
        return true;
    }

    @Override
    public void refresh(final ModelItem item) {
        final Command c = item == null ? controller.getCommand() : (Command) item;
        if (c.getIcon() != null) {
            super.setIcon((WpsIcon) c.getIcon());
        } else {
            this.setText("...");
        }
        setToolTip(ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), c.getTitle()));

        setVisible(controller.getProperty()==null ? c.isVisible() : c.isVisibleForProperty(controller.getProperty().getId()));
        if (controller.isRestrictedInView()){
            setEnabled(false);
        }else{
            setEnabled(controller.getProperty() == null ? c.isEnabled() : c.isEnabledForProperty(controller.getProperty().getId()));
        }
        setObjectName("rx_cmd_tbtn_#"+controller.getCommand().getId().toString());
    }

    @Override
    public boolean setFocus(Property aThis) {
        return false;
    }

    @Override
    public void bind() {
        controller.getCommand().subscribe(this);
    }
}
