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

import java.util.List;
import org.radixware.kernel.common.client.meta.RadPresentationCommandDef;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.types.Id;


public interface ICommandToolBar extends IToolBar {

    public interface ICommandButtonsController {

        public void setUpdatesEnabled(boolean enabled);

        public void setVisible(boolean visible);

        public void clear();

        public ICommandToolButton addButton(Command command);

        public boolean isSomeButtonVisible();
    }

    public class Controller {

        private boolean isPersistentHidden;
        private ICommandButtonsController buttons;
        private ICommandToolBar toolBar;

        public Controller(ICommandToolBar toolBar, ICommandButtonsController buttons) {
            this.buttons = buttons;
            this.toolBar = toolBar;
        }

        public void setModel(Model model) {
            buttons.setUpdatesEnabled(false);
            buttons.setVisible(false);
            buttons.clear();

            if (model != null) {
                final List<Id> modelCommands = model.getAccessibleCommandIds();
                Command command;
                for (Id cmdId : modelCommands) {
                    command = model.getCommand(cmdId);
                    if (!(command.getDefinition() instanceof RadPresentationCommandDef)
                            || (((RadPresentationCommandDef) command.getDefinition()).scope != ECommandScope.PROPERTY)) {
                        buttons.addButton(command);
                    }
                }
                toolBar.setHidden(!buttons.isSomeButtonVisible() || isPersistentHidden);
            }
            buttons.setVisible(true);
            buttons.setUpdatesEnabled(true);
        }

        public void setPersistentHidden(final boolean isHidden) {
            isPersistentHidden = isHidden;
            toolBar.setHidden(isHidden);
        }

        public boolean computeVisibility() {
            return buttons.isSomeButtonVisible() && !isPersistentHidden;
        }
    }

    public void setModel(Model model);

    public void setButtonSize(int w, int h);

    public void setPersistentHidden(final boolean isHidden);

    public boolean close();
}
