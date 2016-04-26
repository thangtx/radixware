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

import java.util.LinkedHashMap;
import java.util.Map;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.widgets.ICommandToolBar;
import org.radixware.kernel.common.client.widgets.ICommandToolButton;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.rwt.UIObject;


public class CommandToolBar extends ToolBar implements ICommandToolBar {

    private static class CommandButtonsController extends ButtonPane implements ICommandButtonsController, IModelWidget {

        private final Map<ModelItem, ICommandToolButton> buttons = new LinkedHashMap<>();
        private CommandToolBar toolBar;

        public CommandButtonsController() {
        }

        @Override
        public void setUpdatesEnabled(boolean enabled) {
        }

        @Override
        public ICommandToolButton addButton(Command command) {
            ICommandToolButton tb = command.createToolButton();
            add((UIObject)tb);
            command.subscribe(this);
            buttons.put(command, tb);
            return tb;
        }

        @Override
        public boolean isSomeButtonVisible() {
            for (UIObject obj : getChildren()) {
                if (obj.isVisible()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean setFocus(Property aThis) {
            return false;
        }

        @Override
        public void bind() {
            for (Map.Entry<ModelItem, ICommandToolButton> entry : buttons.entrySet()) {
                entry.getKey().subscribe(this);
                refresh(entry.getKey());
            }
        }

        @Override
        public void refresh(ModelItem changedItem) {
            final ICommandToolButton button = buttons.get(changedItem);
            if (button != null) {
                button.refresh(changedItem);
            }
            if (toolBar != null) {
                toolBar.update();
            };

        }

        private void close() {
            for (Map.Entry<ModelItem, ICommandToolButton> entry : buttons.entrySet()) {
                entry.getKey().unsubscribe(this);
                entry.getKey().unsubscribe(entry.getValue());
            }
            buttons.clear();
        }
    }
    private Controller controller;
    private CommandButtonsController buttons;

    public CommandToolBar() {
        super(new CommandButtonsController());
        this.buttons = (CommandButtonsController) getButtonsPane();
        this.buttons.toolBar = this;
        controller = new Controller(this, buttons);
    }

    @Override
    public void setModel(Model model) {
        controller.setModel(model);
        setVisible(controller.computeVisibility());
    }

    @Override
    public void setButtonSize(int w, int h) {
        setIconSize(w, h);
    }

    @Override
    public void setPersistentHidden(boolean isHidden) {
        setHidden(isHidden);
    }

    @Override
    public boolean close() {
        buttons.close();
        return true;
    }

    private void update() {
        setVisible(controller.computeVisibility());
    }
}
