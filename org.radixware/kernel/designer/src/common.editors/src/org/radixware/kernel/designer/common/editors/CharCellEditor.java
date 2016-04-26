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

package org.radixware.kernel.designer.common.editors;

import javax.swing.AbstractCellEditor;
import javax.swing.event.DocumentEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.AbstractDocument;


public class CharCellEditor extends AbstractCellEditor implements TableCellEditor {

    private JTextField textField = new JTextField();
    private String original;
    private String val;
    private int row;
    private int col;
    private AbstractTableModel model;

    public CharCellEditor(final AbstractTableModel model) {
        this.model = model;

        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentSizeFilter(1));

        textField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                val = textField.getText();
            }
        });

    }

    @Override
    public JComponent getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int col) {

        this.row = row;
        this.col = col;
        this.val = value != null ? value.toString() : "";
        this.original = val;
        textField.setText(val);

        return textField;
    }

    @Override
    public Object getCellEditorValue() {
        return val;
    }

    @Override
    public boolean stopCellEditing() {
        boolean charFormat = val.length() <= 1;
        if (charFormat) {
            int count = model.getRowCount();
            if (row > -1 && row <= count - 1) {
                model.setValueAt(val, row, col);
                fireEditingStopped();
            }
        }
        return charFormat;
    }

    @Override
    public void cancelCellEditing() {
        if (!val.equals(original)) {
            int count = model.getRowCount();
            if (row > -1 && row <= count - 1) {
                textField.setText(original);
                model.setValueAt(original, row, col);
            }
        }
    }

}
