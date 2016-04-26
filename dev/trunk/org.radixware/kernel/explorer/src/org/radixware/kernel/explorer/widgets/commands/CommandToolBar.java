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

package org.radixware.kernel.explorer.widgets.commands;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.widgets.ICommandToolBar;
import org.radixware.kernel.explorer.widgets.ExplorerToolBar;

public class CommandToolBar extends ExplorerToolBar implements ICommandToolBar {

    private final CommandButtonsPanel buttons;
    private Controller controller;

    public CommandToolBar(final IClientEnvironment environment) {
        this(environment, null);
    }

    public CommandToolBar(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        buttons = new CommandButtonsPanel(environment, null);
        this.controller = new Controller(this, buttons);
        addWidget(buttons);
        orientationChanged.connect(this, "onChangeOrientation(com.trolltech.qt.core.Qt$Orientation)");
        buttons.stateChanged.connect(this, "onStateChanged()");
        setFloatable(false);
        setHidden(true);
    }

    public QSize getButtonSize() {
        return buttons.getButtonSize();
    }

    public void setButtonSize(final QSize buttonSize) {
        buttons.setButtonsSize(buttonSize);
        setIconSize(buttonSize);
    }

    @Override
    public void setPersistentHidden(final boolean isHidden) {
        controller.setPersistentHidden(isHidden);
    }

    @Override
    public void setModel(Model model) {
        controller.setModel(model);
    }

    @SuppressWarnings("unused")
    private void onChangeOrientation(com.trolltech.qt.core.Qt.Orientation orientation) {
        buttons.setOrientation(orientation);
        adjustSize();
    }

    @SuppressWarnings("unused")
    private void onStateChanged() {
        setVisible(controller.computeVisibility());
    }

    @Override
    protected void showEvent(QShowEvent event) {
        if (!controller.computeVisibility()) {
            event.ignore();
            hide();
        } else {
            super.showEvent(event);
        }
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        clear();
        buttons.clear();
        super.closeEvent(event);
    }

    @Override
    public void setButtonSize(int w, int h) {
        setButtonSize(new QSize(w, h));
    }

    @Override
    public int getIconHeight() {
        return buttons.getButtonSize().height();
    }
}
