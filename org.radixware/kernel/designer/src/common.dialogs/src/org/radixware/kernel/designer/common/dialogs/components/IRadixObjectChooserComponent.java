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

package org.radixware.kernel.designer.common.dialogs.components;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEventSource;


public interface IRadixObjectChooserComponent {

    public void updateContent();

    public void setReadonly(boolean readonly);

    public boolean isReadonly();

    public JComponent getVisualComponent();

    public JComponent getLabelComponent();

    public Integer getItemCount();

    public void addAllItems(Object[] objects);

    public void removeAll(Object[] objects);

    public boolean hasSelection();

    public Integer getSelectedIndex();

    public void setSelectedItem(Integer index);

    public Object[] getSelectedItems();
    
    public void setSelectedItems(Object[] items);

    public void addSelectionEventListener(ChangeListener listener);

    public void removeSelectionEventListener(ChangeListener listener);

    public RadixObjectChooserPanel.DoublieClickChooserSupport getDoubleClickSupport();
}
