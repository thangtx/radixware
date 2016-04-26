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

package org.radixware.kernel.designer.common.dialogs.utils;

import org.radixware.kernel.common.components.ExtendableTextField;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * Table cell editor allowing additional embedded buttons within itself
 */
public class ExtendableTableCellEditor extends AbstractCellEditor implements TableCellEditor {

    private boolean readonly;
    protected ExtendableTextField extendableTextField;

    public ExtendableTableCellEditor(boolean readonly) {
        super();

        this.readonly = readonly;

        extendableTextField = new ExtendableTextField(readonly);
        extendableTextField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {

                //первый вариант
                //
                //if (e.getOppositeComponent() instanceof JButton){
                //    return;
                //}
                // ExtendableTableCellEditor.this.stopCellEditing();
                //
                
                //второй вариант
                final Component c = e.getOppositeComponent();
                final Vector<JButton> buttons = extendableTextField.getButtons();
                for (JButton xButton : buttons){
                    if (c == xButton){
                        return;
                    }
                }
                ExtendableTableCellEditor.this.stopCellEditing();
            }
        });
    }

    public void setReadonly(boolean readonly){
        this.readonly = readonly;
        extendableTextField.setReadOnly(readonly);
    }

    public boolean isReadonly(){
        return readonly;
    }

    public JButton addButton(){
        JButton button =  extendableTextField.addButton();
        button.setEnabled(!readonly);
        return button;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof String){
            extendableTextField.setValue((String) value);
        }
        return extendableTextField;
    }

    @Override
    public Object getCellEditorValue() {
        return extendableTextField.getValue();
    }
}
