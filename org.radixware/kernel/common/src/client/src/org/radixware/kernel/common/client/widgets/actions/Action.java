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

package org.radixware.kernel.common.client.widgets.actions;

import org.radixware.kernel.common.client.types.Icon;


public interface Action {

    public void trigger();

    public boolean isEnabled();

    public void setEnabled(boolean enabled);

    public boolean isVisible();

    public void setVisible(boolean visible);

    public interface ActionListener {

        public void triggered(Action action);
    }

    public interface ActionStateListener {

        public void changed(Action action);
    }

    public interface ActionToggleListener {

        public void toggled(Action action, boolean isChecked);
    }

    public void addActionListener(ActionListener listener);

    public void removeActionListener(ActionListener listener);

    public void addActionStateListener(ActionStateListener listener);

    public void removeActionStateListener(ActionStateListener listener);

    public void addActionToggleListener(ActionToggleListener listener);

    public void removeActionToggleListener(ActionToggleListener listener);

    public void setIcon(Icon icon);

    public Icon getIcon();

    public void setToolTip(String toolTip);

    public String getToolTip();

    public String getText();

    public void setText(String text);

    public boolean isCheckable();

    public void setCheckable(boolean isCheckable);

    public boolean isChecked();

    public void setChecked(boolean isChecked);

    public void close();
}
