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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;
import org.radixware.kernel.common.enums.EDefinitionUploadMode;


public class DefinitionUploadComboModel extends AbstractListModel
                                            implements MutableComboBoxModel {

    private String selected;

    @Override
    public Object getElementAt(int index) {
        return EDefinitionUploadMode.values()[index].getName();
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }

    @Override
    public int getSize() {
        return EDefinitionUploadMode.values().length;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if (selected != anItem){
            if (anItem instanceof EDefinitionUploadMode){
                selected = ((EDefinitionUploadMode) anItem).getName();
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
