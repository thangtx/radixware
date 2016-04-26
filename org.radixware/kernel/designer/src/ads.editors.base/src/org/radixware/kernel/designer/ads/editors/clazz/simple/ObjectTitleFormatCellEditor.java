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

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;


class ObjectTitleFormatCellEditor extends AbstractCellEditor implements TableCellEditor {
 
    private class JComboBoxEx extends JComboBox
    {
        ObjectTitleFormatCellEditor objectTitleFormatCellEditor;
        JComboBoxEx(ObjectTitleFormatCellEditor objectTitleFormatCellEditor)
        {
            this.objectTitleFormatCellEditor = objectTitleFormatCellEditor;
        }

        @Override
        protected void fireItemStateChanged(ItemEvent e) {
            super.fireItemStateChanged(e);
            objectTitleFormatCellEditor.fireEditingStopped();
        }
    }

    private JComboBox selectorComboBox = new JComboBoxEx(this);
   
    public ObjectTitleFormatCellEditor(IAdsPropertiesListProvider propertiesListProvider) {
        super();

        final List<AdsPropertyDef> propertiesList = propertiesListProvider.getAdsPropertiesList();
        for (AdsPropertyDef adsPropertyDef : propertiesList) {
            selectorComboBox.addItem(new AdsPropertyDefWrapper(adsPropertyDef));
        }
    }

    @Override
    public Object getCellEditorValue() {
        return selectorComboBox.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        for (int i = 0, count = selectorComboBox.getItemCount(); i < count; ++i) {
            if (selectorComboBox.getItemAt(i).toString().equals((String) value)) {
                selectorComboBox.setSelectedIndex(i);
                break;
            }
        }

//        this.fireEditingStopped();
        return selectorComboBox;
    }
}
