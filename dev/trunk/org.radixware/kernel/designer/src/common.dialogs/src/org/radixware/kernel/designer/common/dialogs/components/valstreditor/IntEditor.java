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

package org.radixware.kernel.designer.common.dialogs.components.valstreditor;

import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.designer.common.dialogs.components.CheckedNumberSpinnerEditor;


class IntEditor extends ValueEditor<Long> {
    
    private class NotNullIntEditor extends NotNullEditor<Long> {
        
        private final JSpinner spinner = new JSpinner() {
            @Override
            public boolean requestFocusInWindow() {
                return ((CheckedNumberSpinnerEditor) this.getEditor()).getTextField().requestFocusInWindow();
            }
        };
        
        public NotNullIntEditor(IntEditor editor) {
            super(editor);
            spinner.setModel(new SpinnerNumberModel(Long.valueOf(0),
                    Long.valueOf(Long.MIN_VALUE), Long.valueOf(Long.MAX_VALUE), Long.valueOf(1)));
            spinner.setEditor(new CheckedNumberSpinnerEditor(spinner));
            spinner.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    fireValueChanged();
                }
            });
        }
        
        @Override
        public void setValue(Long value) {
            if (value != null) {
                if (!value.equals(getValue())) {
//                            Integer val = value.intValue();
                    spinner.setValue(value);
                }
                fireValueChanged();
            }
        }
        
        @Override
        public void activateByKeyInput(KeyEvent e) {
            char kc = e.getKeyChar();
            if (Character.isDigit(kc)) {
                ((CheckedNumberSpinnerEditor) spinner.getEditor()).getTextField().setText(String.valueOf(kc));
            }
        }
        
        @Override
        public Long getValue() {
            return (Long) spinner.getValue();
//                    Long val = ((Integer)spinner.getValue()).longValue();
//                    return val;
        }
        
        @Override
        public JComponent getEditor() {
            return spinner;
        }
        
        @Override
        public void setModel(ValAsStrEditorModel model) {
            Long val = getValue();
            if ((model.getMinValue() == null || model.getMinValue() instanceof Long)
                    && (model.getMaxValue() == null || model.getMaxValue() instanceof Long)
                    && model.getStep() != null && model.getStep() instanceof Long && val != null) {
                Long min = (Long) model.getMinValue();
                Long max = (Long) model.getMaxValue();
                Long step = (Long) model.getStep();
                SpinnerNumberModel newModel;
                try {
                    newModel = new SpinnerNumberModel(val, min, max, step);
                    spinner.setModel(newModel);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
    }
    private final NotNullIntEditor notNullIntEditor = new NotNullIntEditor(this);
    private final NullEditor nullEditor = new NullEditor(this) {
        @Override
        protected boolean isValidChar(char c) {
            return Character.isDigit(c);
        }
        
        @Override
        protected Long processString(String str) {
            return str.length() > 0 ? Long.valueOf(str) : Long.valueOf(0);
        }
    };
    
    public IntEditor(ValAsStrEditor editor) {
        super(editor);
        registerSwitchToNullValueHotKey();
    }
    
    @Override
    public void setDefaultValue() {
        Comparable comp = ((SpinnerNumberModel) ((JSpinner) getNotNullEditor().getEditor()).getModel()).getMinimum();
        if (comp != null) {
            Long val = (Long) comp;
            notNullIntEditor.setValue(val);
            return;
        }
        comp = ((SpinnerNumberModel) ((JSpinner) getNotNullEditor().getEditor()).getModel()).getMaximum();
        if (comp != null) {
            Long val = (Long) comp;
            notNullIntEditor.setValue(val);
            return;
        }
        notNullIntEditor.setValue(Long.valueOf(0));
    }
    
    @Override
    public NotNullEditor getNotNullEditor() {
        return notNullIntEditor;
    }
    
    @Override
    public NullEditor getNullEditor() {
        return nullEditor;
    }
}
