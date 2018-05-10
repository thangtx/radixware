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
import org.radixware.kernel.common.client.widgets.ICommandPushButton;
import org.radixware.kernel.common.client.widgets.ICommandToolButton;
import org.radixware.wps.rwt.PushButton;


public class CommandPushButton extends PushButton implements ICommandPushButton {
    
    private ICommandToolButton.CommandController controller = new ICommandToolButton.CommandController();
    
    public CommandPushButton(Command command) {
        controller.open(command, null);
        refresh(command);
        this.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(IButton source) {
                controller.executeCommand();
            }
        });
        setObjectName("rx_cmd_pbtn_#"+command.getId().toString());
    }
    private boolean useCommandTitle = true;
    private String ownTitle = "";
    
    public boolean isUseCommandTitle() {
        return useCommandTitle;
    }
    
    public boolean getUseCommandTitle() {
        return isUseCommandTitle();
    }
    
    public void setUseCommandTitle(boolean use) {
        this.useCommandTitle = use;        
        refresh(null);
    }
    
    @Override
    public void setText(String text) {
        if (useCommandTitle) {
            ownTitle = text;
        } else {
            super.setText(text);
        }
    }
    
    @Override
    public void refresh(final ModelItem aThis) {
        if (controller.getCommand() != null) {
            this.setIcon(controller.getCommand().getIcon());
            setToolTip(ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), controller.getCommand().getTitle()));
            if (isUseCommandTitle()) {
                super.setText(controller.getCommand().getTitle());
            } else {
                this.setText(ownTitle);
            }
            if (controller.isRestrictedInView()){
                setEnabled(false);
            }else{
                setEnabled(controller.getProperty() == null ? controller.getCommand().isEnabled() : controller.getCommand().isEnabledForProperty(controller.getProperty().getId()));
            }
        } else {
            setEnabled(false);
        }
    }
    
    @Override
    public boolean setFocus(Property aThis) {
        if (controller.getProperty() == aThis) {
            setFocused(true);
            return true;
        }
        return false;
    }
    
    @Override
    public void bind() {
        if (controller.getCommand() != null) {
            controller.getCommand().subscribe(this);
            refresh(controller.getCommand());
        }
    }
}
