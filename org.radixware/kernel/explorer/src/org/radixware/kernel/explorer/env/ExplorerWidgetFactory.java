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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QToolButton;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.ICommandPushButton;
import org.radixware.kernel.common.client.widgets.ICommandToolButton;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.IToolButton;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.common.client.widgets.WidgetFactory;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.widgets.ExplorerAction;
import org.radixware.kernel.explorer.widgets.TimersController;
import org.radixware.kernel.explorer.widgets.commands.CommandPushButton;
import org.radixware.kernel.explorer.widgets.commands.CommandToolButton;


public class ExplorerWidgetFactory implements WidgetFactory {

    @Override
    public ICommandPushButton newCommandPushButton(Command cmd) {
        return new CommandPushButton(cmd);
    }

    @Override
    public ICommandToolButton newCommandToolButton(Command cmd) {
        return new CommandToolButton(cmd);
    }

    @Override
    public ICommandToolButton newCommandToolButton(final Command cmd, final Property property) {
        return new CommandToolButton(cmd, property);
    }

    private static class PB extends QPushButton implements IPushButton {

        private final List<ClickHandler> handlers = new LinkedList<>();
        private final TimersController timers = new TimersController();

        public PB() {
            clicked.connect(this, "handleClick()");
        }

        @Override
        public String getTitle() {
            return text();
        }

        @Override
        public void setTitle(String text) {
            setText(text);
        }

        @Override
        public void setIcon(Icon icon) {
            setIcon((QIcon) icon);
        }

        @Override
        public Icon getIcon() {
            return (Icon) icon();
        }

        @Override
        public void addClickHandler(ClickHandler ch) {
            synchronized (handlers) {
                handlers.add(ch);
            }
        }

        @Override
        public void removeClickHandler(ClickHandler ch) {
            synchronized (handlers) {
                handlers.remove(ch);
            }
        }

        @Override
        public void clearClickHandlers() {
            synchronized (handlers) {
                handlers.clear();
            }
        }               

        @Override
        public String getObjectName() {
            return objectName();
        }

        @Override
        public boolean isDisposed() {
            return nativeId() == 0;
        }

        private void handleClick() {
            synchronized (handlers) {
                for (ClickHandler h : handlers) {
                    h.onClick(this);
                }
            }
        }

        @Override
        public void addAction(Action action) {
            if (action instanceof QAction) {
                addAction((QAction) action);
            }
        }
        
        public IPeriodicalTask startTimer(final TimerEventHandler handler, final int interval) {
            return timers.startTimer(handler, interval, this);
        }

        @Override
        public IPeriodicalTask startTimer(final TimerEventHandler handler) {
            return timers.startTimer(handler, this);
        }

        @Override
        public void killTimer(final IPeriodicalTask task) {
            timers.killTimer(task, this);
        }

        @Override
        protected void timerEvent(final QTimerEvent event) {
            if (!timers.processTimerEvent(event)){
                super.timerEvent(event);
            }
        }

        @Override
        protected void closeEvent(QCloseEvent event) {
            timers.clearTimers();
            super.closeEvent(event);
        }        
    }

    @Override
    public IPushButton newPushButton() {
        return new PB();
    }

    private static class TB extends QToolButton implements IToolButton {

        private final List<ClickHandler> clhs = new LinkedList<>();
        private final TimersController timers = new TimersController();
        private QToolButton src;

        public TB(QToolButton src) {
            super();
            src.clicked.connect(this, "defaultClickEmitter()");
            this.src = src;
        }

        public TB() {
            clicked.connect(this, "defaultClickEmitter()");
            this.src = this;
        }

        @Override
        public String getTitle() {
            return src.text();
        }

        @SuppressWarnings("unused")
        private void defaultClickEmitter() {
            synchronized (clhs) {
                for (ClickHandler h : clhs) {
                    h.onClick(this);
                }
            }
        }

        @Override
        public void setTitle(String text) {
            src.setText(text);
        }

        @Override
        public void setIcon(Icon icon) {
            if (icon instanceof QIcon) {
                src.setIcon((QIcon) icon);
            }
        }

        @Override
        public Icon getIcon() {
            QIcon icon = src.icon();
            if (icon instanceof Icon) {
                return (Icon) icon;
            } else {
                return null;
            }
        }

        @Override
        public void addClickHandler(ClickHandler ch) {
            synchronized (clhs) {
                if (!clhs.contains(ch)) {
                    clhs.add(ch);
                }
            }
        }

        @Override
        public void removeClickHandler(ClickHandler ch) {
            synchronized (clhs) {
                clhs.remove(ch);
            }
        }

        @Override
        public void clearClickHandlers() {
            synchronized (clhs) {
                clhs.clear();
            }
        }               

        @Override
        public String getObjectName() {
            return src.objectName();
        }

        @Override
        public boolean isDisposed() {
            return src.nativeId() == 0;
        }

        @Override
        public void addAction(Action action) {
            if (action instanceof QAction) {
                src.addAction((QAction) action);
            }
        }

        @Override
        public boolean isAutoRaise() {
            return src.autoRaise();
        }

        @Override
        public IToolButton.ToolButtonPopupMode getPopupMode() {
            QToolButton.ToolButtonPopupMode mode = src.popupMode();
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
        public void setPopupMode(IToolButton.ToolButtonPopupMode mode) {
            if (mode == null) {
                src.setPopupMode((QToolButton.ToolButtonPopupMode) null);
            }
            switch (mode) {
                case DelayedPopup:
                    src.setPopupMode(QToolButton.ToolButtonPopupMode.DelayedPopup);
                    break;
                case InstantPopup:
                    src.setPopupMode(QToolButton.ToolButtonPopupMode.InstantPopup);
                    break;
                case MenuButtonPopup:
                    src.setPopupMode(QToolButton.ToolButtonPopupMode.MenuButtonPopup);
                    break;
            }
        }
        
        public IPeriodicalTask startTimer(final TimerEventHandler handler, final int interval) {
            return timers.startTimer(handler, interval, this);
        }

        @Override
        public IPeriodicalTask startTimer(final TimerEventHandler handler) {
            return timers.startTimer(handler, this);
        }

        @Override
        public void killTimer(final IPeriodicalTask task) {
            timers.killTimer(task, this);
        }

        @Override
        protected void timerEvent(final QTimerEvent event) {
            if (!timers.processTimerEvent(event)){
                super.timerEvent(event);
            }
        }

        @Override
        protected void closeEvent(QCloseEvent event) {
            timers.clearTimers();
            super.closeEvent(event);
        }      
    }

    @Override
    public IButton newToolButton() {
        return new TB();
    }

    @Override
    public IPushButton newDialogButton(final EDialogButtonType buttonType, final IClientEnvironment environment) {
        final IPushButton button = newPushButton();
        IMessageBox.StandardButton.setupDialogButton(button, environment, buttonType);
        return button;
    }

    @Override
    public Action newAction(final Icon icon, final String title) {
        return new ExplorerAction(icon==null ? null :  ExplorerIcon.getQIcon(icon), title, null);
    }    

    public static IToolButton wrapToolButton(QToolButton tb) {
        return new TB(tb);
    }
}
