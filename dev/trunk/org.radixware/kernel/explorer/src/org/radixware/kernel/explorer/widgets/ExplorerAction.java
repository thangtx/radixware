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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QIcon;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.explorer.types.RdxIcon;


public class ExplorerAction extends QAction implements Action {

    private List<ActionListener> listeners = null;
    private List<ActionStateListener> stateListeners = null;
    private List<ActionToggleListener> toggleListeners = null;
    private Object userObject;
    private final ActionListener defaultListener = new ActionListener() {

        @Override
        public void triggered(Action action) {
            List<ActionListener> list = null;
            synchronized (this) {
                if (listeners != null) {
                    list = new ArrayList<ActionListener>(listeners);
                }
            }
            if (list != null) {
                for (ActionListener l : list) {
                    l.triggered(action);
                }
            }
        }
    };
    private final ActionStateListener defaultStateListener = new ActionStateListener() {

        @Override
        public void changed(Action action) {
            List<ActionStateListener> list = null;
            synchronized (this) {
                if (stateListeners != null) {
                    list = new ArrayList<ActionStateListener>(stateListeners);
                }
            }
            if (list != null) {
                for (ActionStateListener l : list) {
                    l.changed(action);
                }
            }            
        }
    };
    private final ActionToggleListener defaultToggleListener = new ActionToggleListener() {

        @Override
        public void toggled(final Action action, final boolean isChecked) {
            List<ActionToggleListener> list = null;
            synchronized (this) {
                if (toggleListeners != null) {
                    list = new ArrayList<ActionToggleListener>(toggleListeners);
                }
            }
            if (list != null) {
                for (ActionToggleListener l : list) {
                    l.toggled(action,isChecked);
                }
            }            
        }
    };

    public ExplorerAction(QIcon icon, String text, QObject parent) {
        super(icon, text, parent);
        triggered.connect(this, "triggered()");
        toggled.connect(this,"toggled(boolean)");
        changed.connect(this, "changed()");
    }

    public ExplorerAction(Icon icon, String text, QObject parent) {
        this(icon instanceof QIcon ? (QIcon) icon : null, text, parent);
    }

    @SuppressWarnings("unused")
    private void changed() {
        defaultStateListener.changed(this);
    }

    @SuppressWarnings("unused")
    private void toggled(boolean isChecked) {
        defaultToggleListener.toggled(this, isChecked);
    }

    @SuppressWarnings("unused")
    private void triggered() {
        defaultListener.triggered(this);
    }

    @Override
    public void addActionListener(ActionListener listener) {
        synchronized (defaultListener) {
            if (listeners == null) {
                listeners = new LinkedList<ActionListener>();
                listeners.add(listener);
            } else {
                if (!listeners.contains(listener)) {
                    listeners.add(listener);
                }
            }
        }
    }

    @Override
    public void removeActionListener(ActionListener listener) {
        synchronized (defaultListener) {
            if (listeners != null) {
                listeners.remove(listener);
            }
        }
    }

    @Override
    public void setIcon(Icon icon) {
        if (icon instanceof QIcon) {
            setIcon((QIcon) icon);
        }
    }

    @Override
    public void close() {
        if (listeners != null) {
            listeners.clear();
        }
        if (stateListeners!=null){
            stateListeners.clear();            
        }
        if (toggleListeners!=null){
            toggleListeners.clear();
        }
        disconnect();
    }

    @Override
    public Icon getIcon() {
        QIcon icon = icon();
        if (icon == null) {
            return null;
        } else if (icon instanceof RdxIcon) {
            return (RdxIcon) icon;
        } else {
            return new RdxIcon(icon);
        }

    }

    @Override
    public ExplorerMenu getActionMenu() {
        return (ExplorerMenu)menu();
    }

    @Override
    public void setActionMenu(final IMenu menu) {
        setMenu((ExplorerMenu)menu);
    }        

    @Override
    public String getToolTip() {
        return toolTip();
    }

    @Override
    public String getText() {
        return text();
    }

    @Override
    public String getObjectName() {
        return objectName();
    }        

    @Override
    public Object getUserObject() {
        return userObject;
    }

    @Override
    public void setUserObject(final Object object) {
        userObject = object;
    }    
    
    @Override
    public void addActionStateListener(ActionStateListener listener) {
        synchronized (defaultStateListener) {
            if (stateListeners == null) {
                stateListeners = new LinkedList<ActionStateListener>();
                stateListeners.add(listener);
            } else {
                if (!stateListeners.contains(listener)) {
                    stateListeners.add(listener);
                }
            }
        }
    }

    @Override
    public void removeActionStateListener(ActionStateListener listener) {
        synchronized (defaultStateListener) {
            if (stateListeners != null) {
                stateListeners.remove(listener);
            }
        }
    }

    @Override
    public void addActionToggleListener(ActionToggleListener listener) {
        synchronized (defaultToggleListener) {
            if (toggleListeners == null) {
                toggleListeners = new LinkedList<ActionToggleListener>();
                toggleListeners.add(listener);
            } else {
                if (!toggleListeners.contains(listener)) {
                    toggleListeners.add(listener);
                }
            }
        }
    }

    @Override
    public void removeActionToggleListener(ActionToggleListener listener) {
        synchronized (defaultToggleListener) {
            if (toggleListeners != null) {
                toggleListeners.remove(listener);
            }
        }
    }
}