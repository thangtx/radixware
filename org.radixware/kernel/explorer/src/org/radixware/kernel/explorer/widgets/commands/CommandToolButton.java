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

import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import java.util.LinkedList;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.ICommandToolButton;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.IToolButton;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.types.Id;


public class CommandToolButton extends QToolButton implements ICommandToolButton {
    
    private CommandButton cmd;
    private boolean reenterBlock;
    private final Id propertyId;    
    private final List<ClickHandler> clickHandlers = new LinkedList<>();
    private final ClickHandler cmdExec = new ClickHandler() {

        @Override
        public void onClick(final IButton source) {
            if (cmd != null) {
                cmd.onExecute();
            }
        }
    };    
        
    public CommandToolButton(final QWidget parent) {
        super(parent);
        cmd = null;
        propertyId = null;
        addClickHandler(cmdExec);
        clicked.connect(this, "clickSlot()");
    }

    public CommandToolButton(final Command command) {
        cmd = new CommandButton(command);
        setObjectName("command button #" + command.getId().toString());
        propertyId = null;
        this.setSizePolicy(Policy.Expanding, Policy.Expanding);
        addClickHandler(cmdExec);
        clicked.connect(this, "clickSlot()");
    }

    public CommandToolButton(final Command command, final Property property) {
        cmd = new CommandButton(command, property);
        setObjectName("command button #" + command.getId().toString());
        propertyId = property.getId();
        setSizePolicy(Policy.Expanding, Policy.Expanding);
        addClickHandler(cmdExec);
        clicked.connect(this, "clickSlot()");
    }

    public void setIconId(final Id iconId) {
        super.setIcon((QIcon) cmd.loadIcon(iconId));
    }

    public void setTitleId(final Id textId) {
        setText(cmd.loadTitle(textId));
    }

    public void setCommand(final Command command) {
        if (command == null) {
            throw new NullPointerException();
        }
        if (cmd != null) {
            cmd.unsubscribe(this);
        }
        cmd = new CommandButton(command);
    }

    @SuppressWarnings("unused")
    private void clickSlot() {
        synchronized (clickHandlers) {
            if (reenterBlock){
                final String commandDesc;
                if (cmd!=null && cmd.getCommand()!=null){
                    commandDesc = cmd.getCommand().getDefinition().getDescription();
                }else{
                    commandDesc = null;
                }
                if (commandDesc==null){
                    throw new IllegalStateException(String.format("Reenterant processing of command tool button click"));
                }else{
                    throw new IllegalStateException(String.format("Reenterant processing of tool button click for %1$s",commandDesc));
                }
            }
            reenterBlock = true;
            try{
                final List<ClickHandler> handlers = new LinkedList<>(clickHandlers);
                for (ClickHandler h : handlers) {
                    h.onClick(this);
                }
            }finally{
                reenterBlock = false;
            }
        }
    }

    @Override
    public final void addClickHandler(final ClickHandler handler) {
        synchronized (clickHandlers) {
            if (!clickHandlers.contains(handler)) {
                clickHandlers.add(handler);
            }
        }
    }

    @Override
    public void removeClickHandler(ClickHandler handler) {
        synchronized (clickHandlers) {
            clickHandlers.remove(handler);
        }
    }

    @Override
    public void clearClickHandlers() {
        synchronized (clickHandlers) {
            clickHandlers.clear();
        }
    }       

    @Override
    public Icon getIcon() {
        return cmd == null ? null : cmd.getCommand().getIcon();
    }

    @Override
    public String getTitle() {
        return text();
    }

    @Override
    public void setTitle(final String text) {
        setText(text);
    }

    @Override
    public void setIcon(final Icon icon) {
        if (icon instanceof QIcon) {
            setIcon((QIcon) icon);
        }
    }

    @Override
    public void bind() {
        if (cmd == null) {
            throw new IllegalStateException("command was not defined");
        }
        cmd.subscribe(this);
    }

    @Override
    public void refresh(final ModelItem changedItem) {
        final Command c = changedItem == null ? cmd.getCommand() : (Command) changedItem;
        if (c.getIcon() != null) {
            super.setIcon((QIcon) c.getIcon());
        } else {
            this.setText("...");
        }
        setToolTip(ClientValueFormatter.capitalizeIfNecessary(c.getEnvironment(), c.getTitle()));
        final boolean isVisible = propertyId == null ? c.isVisible() : c.isVisibleForProperty(propertyId);
        if (parentWidget() != null) {
            setVisible(isVisible);
        } else if (!isVisible) {
            setVisible(false);
        }
        if (cmd.isRestricted()){
            setEnabled(false);
        }else{
            setEnabled(propertyId == null ? c.isEnabled() : c.isEnabledForProperty(propertyId));
        }
    }

    @Override
    public boolean setFocus(final Property property) {
        return false;
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        if (cmd != null) {
            cmd.unsubscribe(this);
        }
        super.closeEvent(event);
    }

    public QWidget asQWidget() {
        return this;
    }

    @Override
    public String getObjectName() {
        return objectName();
    }

    @Override
    public boolean isDisposed() {
        return nativeId() == 0;
    }

    @Override
    public void addAction(Action action) {
        if (action instanceof QAction) {
            addAction((QAction) action);
        }
    }

    @Override
    public IToolButton.ToolButtonPopupMode getPopupMode() {
        QToolButton.ToolButtonPopupMode mode = super.popupMode();
        if (mode == null) {
            return null;
        }
        switch (mode) {
            case DelayedPopup:
                return IToolButton.ToolButtonPopupMode.DelayedPopup;
            case InstantPopup:
                return IToolButton.ToolButtonPopupMode.InstantPopup;
            case MenuButtonPopup:
                return IToolButton.ToolButtonPopupMode.MenuButtonPopup;
            default:
                return null;
        }
    }

    @Override
    public void setPopupMode(final IToolButton.ToolButtonPopupMode mode) {
        if (mode == null) {
            super.setPopupMode(null);
            return;
        }
        switch (mode) {
            case DelayedPopup:
                super.setPopupMode(QToolButton.ToolButtonPopupMode.DelayedPopup);
                break;
            case InstantPopup:
                super.setPopupMode(QToolButton.ToolButtonPopupMode.InstantPopup);
                break;
            case MenuButtonPopup:
                super.setPopupMode(QToolButton.ToolButtonPopupMode.MenuButtonPopup);
                break;
        }
    }

    @Override
    public boolean isAutoRaise() {
        return autoRaise();
    }
    
    @Override
    public IPeriodicalTask startTimer(final TimerEventHandler handler) {
        throw new UnsupportedOperationException("startTimer is not supported here.");
    }

    @Override
    public void killTimer(final IPeriodicalTask task) {
        throw new UnsupportedOperationException("killTimer is not supported here.");
    }    
}
