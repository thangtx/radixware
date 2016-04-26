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

package org.radixware.kernel.server.dialogs;

import java.util.List;
import javax.swing.JComboBox;

final class AsyncComboBox extends JComboBox {

    private static final long serialVersionUID = 1893768562048947623L;

    @Override
    public void setSelectedIndex(final int anIndex) {
        // due to multithread data filling
        // a race condition could take place
        // synchronizing
        final List<AsyncComboBoxModel.Item> data = getModel().getData();
        synchronized (data) {
            if (anIndex < data.size()) {
                super.setSelectedIndex(anIndex);
            }
        }
    }

    @Override
    public AsyncComboBoxModel getModel() {
        return (AsyncComboBoxModel) super.getModel();
    }

    AsyncComboBox(final Login dlg, final AsyncComboBoxModel.DataProvider dataProvider) {
        super(new AsyncComboBoxModel(dlg, dataProvider));
    }

    @Override
    public int getItemCount() {
        if (getModel() != null) {
            return getModel().getSize();
        }
        return 0;
    }

    final void clear() {
        setSelectedItem(null);
        getModel().clear();
        repaint();
    }

    final Integer getSelectedId() {
        final AsyncComboBoxModel.Item selected = getModel().getSelected();
        if (selected == null) {
            return null;
        }
        return selected.id;
    }

    final String getInstanceName() {
        final AsyncComboBoxModel.Item selected = getModel().getSelected();
        if (selected == null) {
            return null;
        }
        return selected.name;
    }

    public void setSelectedInstance(final long instanceId) {
        synchronized (getModel().getData()) {
            for (AsyncComboBoxModel.Item item : getModel().getData()) {
                if (item.id != null && item.id.intValue() == instanceId) {
                    setSelectedItem(item);
                    return;
                }
            }
        }
    }
}
