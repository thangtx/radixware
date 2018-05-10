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

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QActionEvent;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.widgets.ICommandToolBar;
import org.radixware.kernel.explorer.widgets.ExplorerToolBar;

public class CommandToolBar extends ExplorerToolBar implements ICommandToolBar {
    
    private final CommandButtonsPanel buttons;
    private final Controller controller;
    private final QEventFilter eventFilter = new QEventFilter(this){
        
        @Override
        public boolean eventFilter(final QObject target, final QEvent event) {
            if (event instanceof QActionEvent
                && event.type()==QEvent.Type.ActionRemoved){
                final QAction action = ((QActionEvent)event).action();
                if (action==CommandToolBar.this.panelAction){                    
                    panelAction = null;
                    CommandToolBar.this.removeEventFilter(this);
                    CommandToolBar.this.buttons.close();                    
                }else if (action!=null){
                    QApplication.postEvent(CommandToolBar.this.buttons, 
                                                        new QActionEvent(QEvent.Type.ActionRemoved.value(), action));
                }
            }
            return false;
        }        
    };        
    private QAction panelAction;
    private QSize buttonsSize;
    
    public CommandToolBar(final IClientEnvironment environment) {
        this(environment, null);
    }

    public CommandToolBar(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        buttons = new CommandButtonsPanel(environment, null);
        buttonsSize = buttons.getButtonSize();
        this.controller = new Controller(this, buttons);
        panelAction = addWidget(buttons);        
        orientationChanged.connect(this, "onChangeOrientation(com.trolltech.qt.core.Qt$Orientation)");
        buttons.stateChanged.connect(this, "onStateChanged()");
        setFloatable(false);
        setHidden(true);
        eventFilter.setProcessableEventTypes(EnumSet.of(QEvent.Type.ActionRemoved));
        installEventFilter(eventFilter);
    }

    public QSize getButtonSize() {        
        return buttonsSize;
    }

    public void setButtonSize(final QSize buttonSize) {
        this.buttonsSize = buttonSize;
        if (panelAction!=null){
            buttons.setButtonsSize(buttonSize);
        }
        setIconSize(buttonSize);
    }

    @Override
    public void setPersistentHidden(final boolean isHidden) {
        controller.setPersistentHidden(isHidden);
    }

    @Override
    public void setModel(final Model model) {
        if (panelAction!=null){
            controller.setModel(model);
        }
    }

    @SuppressWarnings("unused")
    private void onChangeOrientation(final com.trolltech.qt.core.Qt.Orientation orientation) {
        if (panelAction!=null){
            buttons.setOrientation(orientation);
        }
        adjustSize();
    }

    @SuppressWarnings("unused")
    private void onStateChanged() {
        setVisible(controller.computeVisibility());
    }
        
    @Override
    protected void showEvent(final QShowEvent event) {
        if (panelAction!=null && !controller.computeVisibility()) {
            event.ignore();
            hide();
        } else {
            super.showEvent(event);
        }
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        removeEventFilter(eventFilter);
        if (panelAction!=null){
            panelAction = null;        
            buttons.clear();
        }
        super.closeEvent(event);
    }                

    @Override
    public void setButtonSize(final int w, final int h) {
        setButtonSize(new QSize(w, h));
    }

    @Override
    public int getIconHeight() {
        return buttonsSize.height();
    }
}
