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

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.wps.rwt.RwtMenu;


public class RwtAction implements Action {

    public interface IActionPresenter {

        public void actionStateChanged(Action a);
    }
    private boolean isEnabled = true;
    private boolean isVisible = true;
    private Icon icon;
    private ClientIcon clientIcon;
    private String title;
    private String toolTip;
    private RwtMenu menu;
    private Object userObject;
    private final List<ActionListener> listeners = new LinkedList<>();
    private final List<ActionStateListener> stateListeners = new LinkedList<>();
    private final List<ActionToggleListener> toggleListeners = new LinkedList<>();
    private final List<IActionPresenter> presenters = new LinkedList<>();
    private boolean isCheckable = false;
    private boolean isChecked = false;
    private boolean isTextShown = false;
    private String objectName;
    private IClientEnvironment env;

    public RwtAction(IClientEnvironment env) {
        this(env, (ClientIcon) null, (String) null);
    }

    public RwtAction(IClientEnvironment env, ClientIcon icon, String title) {
        this.env = env;
        this.clientIcon = icon;
        this.title = title;
    }

    public RwtAction(IClientEnvironment env, ClientIcon icon) {
        this(env, icon, null);
    }

    public RwtAction(Icon icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public void setObjectName(String name) {
        objectName = name;
    }

    public String getObjectName() {
        return objectName;
    }

    public RwtAction(Icon icon) {
        this.icon = icon;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    public boolean getVisible() {
        return isVisible();
    }

    public boolean getEnabled() {
        return isEnabled();
    }

    @Override
    public void setVisible(boolean visible) {
        isVisible = visible;
        firePresentationChange();
    }

    @Override
    public void addActionListener(ActionListener listener) {
        synchronized (listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }

    @Override
    public void removeActionListener(ActionListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    @Override
    public void trigger() {
        if (isCheckable){
            isChecked = !isChecked;
        }
        synchronized (listeners) {
            for (ActionListener l : listeners) {
                l.triggered(this);
            }
        }
    }

    public void addActionPresenter(IActionPresenter p) {
        synchronized (presenters) {
            if (!presenters.contains(p)) {
                presenters.add(p);
            }
        }
    }

    public void removeActionPresenter(IActionPresenter p) {
        synchronized (presenters) {
            presenters.remove(p);
        }
    }

    private void firePresentationChange() {
        synchronized (presenters) {
            for (IActionPresenter p : presenters) {
                p.actionStateChanged(this);
            }
        }
    }

    private void fireToggled() {
        synchronized (toggleListeners) {
            for (ActionToggleListener p : toggleListeners) {
                p.toggled(this, isChecked);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
        firePresentationChange();
    }

    @Override
    public void setIcon(Icon icon) {
        this.icon = icon;
        firePresentationChange();
    }

    @Override
    public Icon getIcon() {
        if (icon == null) {
            if (clientIcon != null && env != null) {
                try {
                    icon = env.getApplication().getImageManager().getIcon(clientIcon);
                } catch (Throwable e) {
                }
            }
        }
        return icon;
    }

    @Override
    public String getText() {
        if (isTextShown) {
            return title;
        } else {
            return "";
        }
    }

    public boolean isTextShown() {
        return isTextShown;
    }

    public void setTextShown(boolean textShown) {
        if (isTextShown != textShown) {
            isTextShown = textShown;
            firePresentationChange();
        }
    }

    @Override
    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
        firePresentationChange();
    }

    @Override
    public String getToolTip() {
        return toolTip;
    }

    @Override
    public void close() {
        synchronized (listeners) {
            listeners.clear();
        }
    }

    @Override
    public void addActionStateListener(ActionStateListener listener) {
        synchronized (stateListeners) {
            if (!stateListeners.contains(listener)) {
                stateListeners.add(listener);
            }
        }
    }

    @Override
    public void removeActionStateListener(ActionStateListener listener) {
        synchronized (stateListeners) {
            stateListeners.remove(listener);
        }
    }

    @Override
    public void addActionToggleListener(ActionToggleListener listener) {
        synchronized (toggleListeners) {
            if (!toggleListeners.contains(listener)) {
                toggleListeners.add(listener);
            }
        }
    }

    @Override
    public void removeActionToggleListener(ActionToggleListener listener) {
        synchronized (toggleListeners) {
            toggleListeners.remove(listener);
        }
    }

    @Override
    public void setText(String text) {
        //setToolTip(text);
        this.title = text;
        firePresentationChange();
    }

    @Override
    public boolean isCheckable() {
        return isCheckable;
    }

    @Override
    public void setCheckable(boolean isCheckable) {
        this.isCheckable = isCheckable;
        firePresentationChange();
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        firePresentationChange();
        fireToggled();
    }

    @Override
    public RwtMenu getActionMenu() {
        return menu;
    }

    @Override
    public void setActionMenu(final IMenu menu) {
        this.menu = (RwtMenu)menu;
        firePresentationChange();
    }        

    @Override
    public Object getUserObject() {
        return userObject;
    }

    @Override
    public void setUserObject(final Object object) {
        userObject = object;
    }
        
}
