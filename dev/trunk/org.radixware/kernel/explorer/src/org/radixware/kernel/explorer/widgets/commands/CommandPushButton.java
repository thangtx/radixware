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
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.ICommandPushButton;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.types.Id;


public class CommandPushButton extends QPushButton implements ICommandPushButton {

    private CommandButton cmd;
    private boolean titleWasChanged;

    public CommandPushButton(QWidget parent) {
        super(parent);
        cmd = null;
        this.clicked.connect(this, "clickSlot()");
        addClickHandler(cmdExec);
    }

    public CommandPushButton(Command command) {
        cmd = new CommandButton(command);
        this.clicked.connect(this, "clickSlot()");
        addClickHandler(cmdExec);
    }

    public CommandPushButton(Command command, final Property property) {
        cmd = new CommandButton(command, property);
        this.clicked.connect(this, "clickSlot()");
        addClickHandler(cmdExec);
    }

    public void setIconId(final Id iconId) {
        super.setIcon(cmd.loadIcon(iconId));
    }

    public void setTitleId(final Id textId) {
        setText(cmd.loadTitle(textId));
    }
    //ibutton support
    private final List<ClickHandler> clickHandlers = new LinkedList<ClickHandler>();
    private final ClickHandler cmdExec = new ClickHandler() {

        @Override
        public void onClick(IButton source) {
            if (cmd != null) {
                cmd.onExecute();
            }
        }
    };

    private void clickSlot() {
        synchronized (clickHandlers) {
            for (ClickHandler h : clickHandlers) {
                h.onClick(this);
            }
        }
    }

    @Override
    public void addClickHandler(ClickHandler handler) {
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
    public void setTitle(String text) {
        titleWasChanged = true;
        setText(text);
    }

    @Override
    public void setIcon(Icon icon) {
        if (icon instanceof QIcon) {
            setIcon((QIcon) icon);
        }
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

    @Override
    public void bind() {
        if (cmd == null) {
            throw new IllegalStateException("command was not defined");
        }
        cmd.subscribe(this);
    }

    @Override
    public void refresh(ModelItem changedItem) {
        final Command c = (Command) changedItem;
        setIcon(c.getIcon());
        if (!titleWasChanged){
            setText(c.getTitle());
        }
        setToolTip(ClientValueFormatter.capitalizeIfNecessary(c.getEnvironment(), c.getTitle()));
        if (parentWidget() != null) {
            setVisible(c.isVisible());
        } else if (!c.isVisible()) {
            setVisible(false);
        }
        if (cmd.isRestricted()){
            setEnabled(false);
        }else{
            setEnabled(c.isEnabled());
        }
    }

    @Override
    public boolean setFocus(Property property) {
        return false;
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        if (cmd != null) {
            cmd.unsubscribe(this);
        }
        super.closeEvent(event);
    }
    
    @Override
    public boolean isDisposed() {
        return nativeId()==0;
    }    

    @Override
    public String getObjectName() {
        return objectName();
    }

    @Override
    public void addAction(Action action) {
        if(action instanceof QAction){
            addAction((QAction)action);
        }
    }
    
    @Override
    public IPeriodicalTask startTimer(TimerEventHandler handler) {
        throw new UnsupportedOperationException("startTimer is not supported here.");
    }

    @Override
    public void killTimer(IPeriodicalTask task) {
        throw new UnsupportedOperationException("killTimer is not supported here.");
    }    
}