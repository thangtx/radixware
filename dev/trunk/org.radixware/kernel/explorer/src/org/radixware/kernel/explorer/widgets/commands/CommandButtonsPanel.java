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

import org.radixware.kernel.explorer.widgets.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QBoxLayout;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QToolBar;
import java.util.HashMap;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.widgets.ICommandToolBar.ICommandButtonsController;
import org.radixware.kernel.common.client.widgets.ICommandToolButton;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public class CommandButtonsPanel extends ExplorerFrame implements IModelWidget, ICommandButtonsController {

    private final Map<ModelItem, ICommandToolButton> buttons = new LinkedHashMap<>();
    private final Map<Id, QAction> actionsByCommandId = new HashMap<>();
    private QSize buttonSize;
    public final QSignalEmitter.Signal0 stateChanged;

    public CommandButtonsPanel(IClientEnvironment environment, final List<Command> commands) {
        super(environment);
        setFrameShape(QFrame.Shape.NoFrame);
        setFrameShadow(QFrame.Shadow.Raised);
        stateChanged = new QSignalEmitter.Signal0();
        buttonSize = (new QToolButton()).sizeHint();
        if (commands != null) {
            ICommandToolButton button;
            for (Command command : commands) {
                button = command.createToolButton();
                setupButton((QWidget)button,command.getId());
                buttons.put(command, button);
            }
        }
        setOrientation(Orientation.Horizontal);
    }

    @Override
    public ICommandToolButton addButton(Command command) {
        final ICommandToolButton button = command.createToolButton();
        setupButton((QWidget)button,command.getId());
        buttons.put(command, button);
        command.subscribe(this);
        refresh(command);
        return button;
    }
    
    private void setupButton(final QWidget button, final Id commandId){
        if (this.parentWidget() instanceof QToolBar){            
            //see gui/styles/qmacstyle_mac.mm QMacStyle::drawComplexControl case CC_ToolButton
            final QAction commandAction = ((QToolBar)this.parentWidget()).addWidget(button);
            actionsByCommandId.put(commandId, commandAction);
        }else{
            button.setParent(this);
            layout().addWidget(button);
        }
        button.setSizePolicy(Policy.Fixed, Policy.Fixed);
        if (button instanceof QAbstractButton){
            ((QAbstractButton)button).setIconSize(buttonSize);
        }
        if (button instanceof QToolButton){
            ((QToolButton)button).setAutoRaise(true);
        }
        button.setFocusPolicy(Qt.FocusPolicy.NoFocus);
    }

    public QSize getButtonSize() {
        return buttonSize;
    }

    public void setButtonsSize(final QSize newSize) {
        buttonSize = newSize;
        for (Map.Entry<ModelItem, ICommandToolButton> entry : buttons.entrySet()) {
            if (entry.getValue() instanceof QAbstractButton){
                ((QAbstractButton)entry.getValue()).setIconSize(buttonSize);
            }
        }
    }

    public void setOrientation(Orientation orientation) {
        if (layout() != null) {
            if (((layout() instanceof QHBoxLayout) && orientation == Orientation.Horizontal)
                    || ((layout() instanceof QVBoxLayout) && orientation == Orientation.Vertical)) {
                return;
            }
            for (Map.Entry<ModelItem, ICommandToolButton> entry : buttons.entrySet()) {
                layout().removeWidget((QWidget)entry.getValue());
            }
            layout().dispose();
        }
        final QBoxLayout layout;
        final Alignment alignment;
        if (orientation == Orientation.Vertical) {
            layout = WidgetUtils.createVBoxLayout(this);
            alignment = new Alignment(Qt.AlignmentFlag.AlignTop);
        } else {
            layout = WidgetUtils.createHBoxLayout(this);
            alignment = new Alignment(Qt.AlignmentFlag.AlignLeft);
        }
        layout.setAlignment(alignment);
        this.setLayout(layout);
        for (Map.Entry<ModelItem, ICommandToolButton> entry : buttons.entrySet()) {
            layout.addWidget((QWidget)entry.getValue());
        }
        adjustSize();
    }

    @Override
    public boolean isSomeButtonVisible() {
        for (QAction action: actionsByCommandId.values()){
            if (action.isVisible()){
                return true;
            }
        }
        for (Map.Entry<ModelItem, ICommandToolButton> entry : buttons.entrySet()) {
            if (!((QWidget)entry.getValue()).isHidden()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        final boolean isToolBar = this.parentWidget() instanceof QToolBar;
        for (Map.Entry<ModelItem, ICommandToolButton> entry : buttons.entrySet()) {
            entry.getKey().unsubscribe(this);
            if (isToolBar){
                ((QToolBar)this.parentWidget()).removeAction(actionsByCommandId.get(entry.getKey().getId()));
            }else{
                layout().removeWidget((QWidget)entry.getValue());
            }
            entry.getValue().close();
        }
        buttons.clear();
        actionsByCommandId.clear();
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        clear();
        super.closeEvent(event);
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
        final QAction action = actionsByCommandId.get(changedItem.getId());
        if (action!=null){
            action.setVisible(((Command)changedItem).isVisible());
        }
        stateChanged.emit();
    }

    @Override
    public boolean setFocus(final Property property) {
        return false;
    }

    public QWidget asQWidget() {
        return this;
    }
}
