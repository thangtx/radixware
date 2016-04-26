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
import javax.swing.InputVerifier;
import javax.swing.table.TableCellEditor;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.text.AbstractDocument;


public class LongCellEditor extends AbstractCellEditor implements TableCellEditor {

    private JTextField textField;
    private boolean isHexMode = false;

    public LongCellEditor(boolean isHexMode) {
        super();
        this.isHexMode = isHexMode;

        this.textField = new JTextField();
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter(isHexMode));
    }
/*
    public void setInputVerifier(InputVerifier inputVerifier){
        textField.setInputVerifier(inputVerifier);
    }
*/
    @Override
    public JComponent getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int rowIndex, int vColIndex) {

        if (value instanceof Long) {
            if (isHexMode){
                textField.setText(Long.toHexString((Long) value).toUpperCase());
            } else {
                textField.setText(((Long) value).toString());
            }
            
        } else {
            textField.setText("");
        }
        return textField;
    }

    @Override
    public Object getCellEditorValue() {
        if (isHexMode && !textField.getText().isEmpty()){
            try {
               long asLong = Long.parseLong(textField.getText(), 16);
               return Long.toString(asLong);
            } catch (NumberFormatException ex){
                return "";
            }
        }
        return textField.getText();
    }

    @Override
    public boolean stopCellEditing() {
        final String strValue = (String) getCellEditorValue();

        if (strValue.isEmpty()){
            return false;
        }
//        try {
//            Long.parseLong(strValue);
//        } catch (NumberFormatException e) {
//            return false;
//        }

        final InputVerifier inputVerifier = textField.getInputVerifier();
        if (inputVerifier != null){
            return inputVerifier.shouldYieldFocus(textField) && super.stopCellEditing();
        }else{
            return super.stopCellEditing();
        }
    }
}

