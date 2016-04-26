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

import java.util.LinkedList;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;
import org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode;


public class RightInheritanceComboModel extends AbstractListModel
                                      implements MutableComboBoxModel {

    private String selected;
    private boolean hasReplaced = false;

    public RightInheritanceComboModel(boolean hasReplaced){
        this.hasReplaced = hasReplaced;
    }

    @Override
    public Object getElementAt(int index) {
        EEditorPresentationRightsInheritanceMode[] values = EEditorPresentationRightsInheritanceMode.values();
        if (hasReplaced)
            return EEditorPresentationRightsInheritanceMode.values()[index].getName();
        LinkedList<EEditorPresentationRightsInheritanceMode> reducedValues = new LinkedList<EEditorPresentationRightsInheritanceMode>();
        for (int i=0, size = values.length; i<size; i++){
            if (!values[i].equals(EEditorPresentationRightsInheritanceMode.FROM_REPLACED)){
                reducedValues.add(values[i]);
            }
        }
        return reducedValues.get(index).getName();
    }

    @Override
    public int getSize() {
        return hasReplaced ? EEditorPresentationRightsInheritanceMode.values().length : EEditorPresentationRightsInheritanceMode.values().length - 1;
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if (selected != anItem){
            if (anItem instanceof EEditorPresentationRightsInheritanceMode){
                selected = ((EEditorPresentationRightsInheritanceMode)anItem).getName();
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


