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

import com.trolltech.qt.QtPropertyWriter;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QToolButton;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.utils.ThreadDumper;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.ICommandPushButton;
import org.radixware.kernel.common.client.widgets.ICommandToolButton;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.IToolButton;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.common.client.widgets.WidgetFactory;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.utils.LeakedWidgetsDetector;
import org.radixware.kernel.explorer.widgets.ExplorerAction;
import org.radixware.kernel.explorer.widgets.ExplorerMenu;
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

        @Override
        @QtPropertyWriter(name = "menu", enabled = false)
        public void setMenu(IMenu menu) {
            super.setMenu((QMenu) menu);
        }

        @Override
        public IMenu getMenu() {
            return (IMenu) super.menu();
        }
    }

    @Override
    public IPushButton newPushButton() {
        return new PB();
    }
    
    private static class ToolButtonWrapper implements IToolButton{
        
        private final QToolButton src;
        private final List<ClickHandler> clhs = new LinkedList<>();
        private final TimersController timers = new TimersController();        
        
        private ToolButtonWrapper(final QToolButton button){
            src = button;
        }

        @Override
        public boolean isAutoRaise() {
            return src.autoRaise();
        }

        @Override
        public void setAutoRaise(final boolean isAutoRaise) {
            src.setAutoRaise(isAutoRaise);
        }

        @Override
        public ToolButtonPopupMode getPopupMode() {
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
        public void setPopupMode(ToolButtonPopupMode mode) {
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

        @Override
        public void setMenu(IMenu menu) {
            if (menu==null || menu instanceof QMenu){
                src.setMenu((QMenu)menu);
            }
        }

        @Override
        public IMenu getMenu() {
            final QMenu menu = src.menu();
            return menu instanceof IMenu ? (IMenu) menu : null;
        }

        @Override
        public String getTitle() {
            return src.text();
        }

        @Override
        public void setTitle(final String text) {
            src.setText(text);
        }

        @Override
        public void setIcon(final Icon icon) {
            if (icon==null || icon instanceof QIcon){
                src.setIcon((QIcon)icon);
            }
        }

        @Override
        public Icon getIcon() {
            final QIcon icon = src.icon();
            return icon instanceof Icon ? (Icon)icon : null;
        }

        @Override
        public void addClickHandler(final ClickHandler ch) {
            if (!clhs.contains(ch)) {
                clhs.add(ch);
            }
        }

        @Override
        public void removeClickHandler(final ClickHandler ch) {
            clhs.remove(ch);
        }

        @Override
        public void clearClickHandlers() {
            clhs.clear();
        }

        @Override
        public void addAction(final Action action) {
            if (action instanceof QAction) {
                src.addAction((QAction) action);
                final String selfObjectName = src.objectName();
                final String actionObjectName = action.getObjectName();
                if ((selfObjectName==null || selfObjectName.isEmpty())
                    && actionObjectName!=null && !actionObjectName.isEmpty()){
                    src.setObjectName("rx_tbtn_" + actionObjectName);
                }
                
            }
        }

        @Override
        public Object findChild(final Class<?> childClass, final String childObjectName) {
            return src.findChild(childClass, childObjectName);
        }

        @Override
        public int width() {
            return src.width();
        }

        @Override
        public int height() {
            return src.height();
        }

        @Override
        public void setToolTip(final String toolTipText) {
            src.setToolTip(toolTipText);
        }

        @Override
        public void setEnabled(final boolean enabled) {
            src.setEnabled(enabled);
        }

        @Override
        public boolean isEnabled() {
            return src.isEnabled();
        }

        @Override
        public boolean isDisposed() {
            return src.nativeId()==0;
        }

        @Override
        public IPeriodicalTask startTimer(final TimerEventHandler handler) {
            return timers.startTimer(handler, src);
        }

        @Override
        public void killTimer(IPeriodicalTask task) {
            timers.killTimer(task, src);
        }

        @Override
        public String getObjectName() {
            return src.objectName();
        }

        @Override
        public void setObjectName(final String name) {
            src.setObjectName(name);
        }

        @Override
        public boolean isVisible() {
            return src.isVisible();
        }

        @Override
        public void setVisible(final boolean visible) {
            src.setVisible(visible);
        }
        
    }

    private static class TB extends QToolButton implements IToolButton, LeakedWidgetsDetector.ITracableWidget {

        private final List<ClickHandler> clhs = new LinkedList<>();
        private final TimersController timers = new TimersController();
        private final String creationStackTrace;

        public TB() {
            clicked.connect(this, "defaultClickEmitter()");
            if (LeakedWidgetsDetector.getInstance().isEnabled()){
                creationStackTrace = ThreadDumper.dumpSync();
            }else{
                creationStackTrace = null;
            }            
        }

        @Override
        public String getTitle() {
            return text();
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
            setText(text);
        }

        @Override
        public void setIcon(Icon icon) {
            if (icon==null || icon instanceof QIcon) {
                setIcon((QIcon) icon);
            }
        }

        @Override
        public Icon getIcon() {
            QIcon icon = icon();
            return (icon instanceof Icon) ? (Icon) icon : null;
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
                final String selfObjectName = objectName();
                final String actionObjectName = action.getObjectName();
                if ((selfObjectName==null || selfObjectName.isEmpty())
                    && actionObjectName!=null && !actionObjectName.isEmpty()){
                    setObjectName("rx_tbtn_" + actionObjectName);
                }
            }
        }

        @Override
        public boolean isAutoRaise() {
            return autoRaise();
        }

        @Override
        public IToolButton.ToolButtonPopupMode getPopupMode() {
            QToolButton.ToolButtonPopupMode mode = popupMode();
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
                setPopupMode((QToolButton.ToolButtonPopupMode) null);
            }
            switch (mode) {
                case DelayedPopup:
                    setPopupMode(QToolButton.ToolButtonPopupMode.DelayedPopup);
                    break;
                case InstantPopup:
                    setPopupMode(QToolButton.ToolButtonPopupMode.InstantPopup);
                    break;
                case MenuButtonPopup:
                    setPopupMode(QToolButton.ToolButtonPopupMode.MenuButtonPopup);
                    break;
            }            
        }
        
        @Override
        public String getCreationStackTrace() {
            return creationStackTrace;
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

        @Override
        public void setMenu(IMenu menu) {
            super.setMenu((QMenu) menu);
        }

        @Override
        public IMenu getMenu() {
            return (IMenu) super.menu();
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

    public static IToolButton wrapToolButton(final QToolButton tb) {
        return new ToolButtonWrapper(tb);
    }

    @Override
    public IMenu newMenu() {
        return new ExplorerMenu();
    }        
}
