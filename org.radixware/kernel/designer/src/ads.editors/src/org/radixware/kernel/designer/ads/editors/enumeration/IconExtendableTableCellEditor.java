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

package org.radixware.kernel.designer.ads.editors.enumeration;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.editors.module.images.ChooseImagesDialog;


class IconExtendableTableCellEditor extends AbstractCellEditor implements TableCellEditor {

    private Id iconId;
    private Id originalId;
    private javax.swing.JButton button;
    private EnumerationTableModel tableModel;
    private int row;
    private int col;

   // private final Icon UNDEFINDED_ICON = RadixObjectIcon.UNKNOWN.getIcon(16, 16);

    public IconExtendableTableCellEditor(final AdsEnumDef adsEnumDef,EnumerationTableModel tableModel) {

        this.tableModel = tableModel;

        button = new javax.swing.JButton();
        button.setToolTipText("Configure Icon");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ChooseImagesDialog dialog = ChooseImagesDialog.getInstanceFor(adsEnumDef.getModule());
                if (dialog.chooseImage()) {
                    iconId = dialog.getSelectedImageId();
                    final AdsEnumItemDef item = IconExtendableTableCellEditor.this.tableModel.getViewItemByRow(row);
                    if (item != null) {
                        final AdsImageDef imageDef = AdsSearcher.Factory.newImageSearcher(item).findById(iconId).get();
                        if (imageDef != null){
                            button.setIcon(imageDef.getIcon().getIcon(16, 16));
                        } else {
                            button.setIcon(null);
                        }
                    } else {
                        button.setIcon(null);
                    }
                }
                stopCellEditing();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.iconId = (value == null || value instanceof Id) ? (Id) value : null;
        this.originalId = iconId;
        this.row = row;
        this.col = column;
        if (value != null) {
            if (value instanceof Id) {
                final AdsEnumItemDef item = ((EnumerationTableModel) table.getModel()).getViewItemByRow(row);
                if (item != null) {
                    final AdsImageDef imageDef = AdsSearcher.Factory.newImageSearcher(item).findById(iconId).get();
                    if (imageDef != null){
                        button.setIcon(imageDef.getIcon().getIcon(16, 16));
                    } else {
                        button.setIcon(null);
                    }
                } else {
                    button.setIcon(null);
                }
            } else {
                button.setIcon(null);
            }
        } else {
            button.setIcon(null);
        }
        return button;
    }

    @Override
    public boolean stopCellEditing() {
        if (row > -1 && row < tableModel.getRowCount()) {
            if ((originalId != null && iconId != null && !originalId.equals(iconId)) ||
                (originalId == null && iconId != null) ||
                (originalId != null && iconId == null)) {
                tableModel.setValueAt(iconId, row, col);
            }
        }
        return true;
    }

    @Override
    public Object getCellEditorValue() {
        return iconId;
    }
};
