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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.resources.RadixWareIcons;


class ObjectTitleItemCellEditor extends AbstractCellEditor implements TableCellEditor {

    //private AdsObjectTitleFormatDef objectTitleFormat;
    private ExtendableTextField extendableTextField;
    private AdsObjectTitleFormatDef.TitleItem titleItem;

    public ObjectTitleItemCellEditor(final AdsObjectTitleFormatDef objectTitleFormat) {
        super();

        //this.objectTitleFormat = objectTitleFormat;
        this.titleItem = null;

        extendableTextField = new ExtendableTextField();
        extendableTextField.setReadOnly(true);

        final JButton setStringFormatButton = extendableTextField.addButton();
        setStringFormatButton.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        setStringFormatButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                //invoke format string editor
                assert (titleItem != null);
              
                if (new StateFormatStringEditDialog(objectTitleFormat, titleItem).showModal()){
                    //if dialog has been closed with the "OK" status, then force cell editing stop
                    fireEditingStopped();
                }
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return extendableTextField.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        assert(table.getModel() instanceof ObjectTitleFormatTableModel);
        titleItem = ((ObjectTitleFormatTableModel) table.getModel()).getTitleItem(table.convertRowIndexToModel(row));
        extendableTextField.setValue(value == null ? "" : (String) value);
        return extendableTextField;
    }
}
