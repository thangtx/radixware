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

package org.radixware.kernel.designer.ads.editors.presentations;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;
import org.radixware.kernel.common.enums.ESelectorColumnSizePolicy;


public class SelectorColumnSizePolicyModel extends AbstractListModel
        implements MutableComboBoxModel {

    private String selected;

    @Override
    public Object getElementAt(int index) {
        return ESelectorColumnSizePolicy.values()[index].getName();
    }

    @Override
    public int getSize() {
        return ESelectorColumnSizePolicy.values().length;
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if (selected != anItem) {
            if (anItem instanceof ESelectorColumnSizePolicy) {
                selected = ((ESelectorColumnSizePolicy) anItem).getName();
            } else {
                selected = anItem.toString();
            }
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public void addElement(Object obj) {
    }

    @Override
    public void insertElementAt(Object obj, int index) {
    }

    @Override
    public void removeElementAt(int index) {
    }

    @Override
    public void removeElement(Object obj) {
    }
}
