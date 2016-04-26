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

package org.radixware.kernel.designer.ads.editors.command.components;

import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;
import org.radixware.kernel.common.enums.ECommandAccessibility;


public class CommandAccessibilityComboModel extends AbstractListModel
                                            implements MutableComboBoxModel {

    private String selected;
    private List<ECommandAccessibility> groupModel;

    public CommandAccessibilityComboModel(boolean forGroup){
        if (forGroup){
            groupModel = new LinkedList<ECommandAccessibility>();
            groupModel.add(ECommandAccessibility.ALWAYS);
            groupModel.add(ECommandAccessibility.ONLY_FOR_FIXED);
        }
    }

    @Override
    public Object getElementAt(int index) {
        if (groupModel != null){
            return groupModel.get(index).getName();
        }
        return ECommandAccessibility.values()[index].getName();
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }

    @Override
    public int getSize() {
        if (groupModel != null){
            return groupModel.size();
        }
        return ECommandAccessibility.values().length;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if (selected != anItem){
            if (anItem instanceof ECommandAccessibility){
                selected = ((ECommandAccessibility)anItem).getName();
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
