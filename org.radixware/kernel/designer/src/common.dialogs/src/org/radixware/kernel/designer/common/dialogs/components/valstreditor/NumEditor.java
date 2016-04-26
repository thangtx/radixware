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
import java.math.BigDecimal;
import java.util.logging.Level;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.designer.common.dialogs.components.BigDecimalSpinnerModel;
import org.radixware.kernel.designer.common.dialogs.components.CheckedBigDecimalSpinnerEditor;


class NumEditor extends ValueEditor<BigDecimal> {

    private class NotNullNumEditor extends NotNullEditor<BigDecimal> {

        private final JSpinner spinner = new JSpinner() {
            @Override
            public boolean requestFocusInWindow() {
                return ((CheckedBigDecimalSpinnerEditor) this.getEditor()).getTextField().requestFocusInWindow();
            }
        };

        public NotNullNumEditor(NumEditor editor) {
            super(editor);
            spinner.setModel(new BigDecimalSpinnerModel());
            spinner.setEditor(new CheckedBigDecimalSpinnerEditor(spinner));
            spinner.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    fireValueChanged();
                }
            });
        }

        @Override
        public void setValue(BigDecimal value) {
            if (value != null) {
                if (!value.equals(getValue())) {
                    spinner.setValue(value);
                }
                fireValueChanged();
            }
        }

        @Override
        public void activateByKeyInput(KeyEvent e) {
            final char kc = e.getKeyChar();
            if (Character.isDigit(kc)) {
                ((CheckedBigDecimalSpinnerEditor) spinner.getEditor()).getTextField().setText(String.valueOf(kc));
            }
        }

        @Override
        public BigDecimal getValue() {
            return (BigDecimal) spinner.getValue();
        }

        @Override
        public JComponent getEditor() {
            return spinner;
        }

        @Override
        public void setModel(ValAsStrEditorModel model) {
            if (model == null) {
                return;
            }
            final BigDecimal val = getValue();
            if ((model.getMinValue() == null || model.getMinValue() instanceof BigDecimal)
                    && (model.getMaxValue() == null || model.getMaxValue() instanceof BigDecimal)
                    && model.getStep() != null && model.getStep() instanceof BigDecimal && val != null) {
                final BigDecimal min = (BigDecimal) model.getMinValue();
                final BigDecimal max = (BigDecimal) model.getMaxValue();
                final BigDecimal step = (BigDecimal) model.getStep();
                BigDecimalSpinnerModel newModel;
                try {
                    newModel = new BigDecimalSpinnerModel(val, min, max, step);
                    spinner.setModel(newModel);
                } catch (IllegalArgumentException e) {
                    java.util.logging.Logger.getLogger(NumEditor.class.getName()).log(Level.FINE, null, e);
                }
            }
        }
    }
    private final NotNullNumEditor notNullNumEditor = new NotNullNumEditor(this);
    private final NullEditor nullEditor = new NullEditor(this) {
        @Override
        protected boolean isValidChar(char c) {
            return Character.isDigit(c);
        }

        @Override
        protected BigDecimal processString(String str) {
            return str.length() > 0 ? new BigDecimal(str) : BigDecimal.ZERO;
        }
    };

    public NumEditor(ValAsStrEditor editor) {
        super(editor);
        registerSwitchToNullValueHotKey();
    }

    @Override
    public void setDefaultValue() {
        Comparable comp = ((BigDecimalSpinnerModel) ((JSpinner) getNotNullEditor().getEditor()).getModel()).getMinimum();
        if (comp != null) {
            final BigDecimal val = (BigDecimal) comp;
            notNullNumEditor.setValue(val);
            return;
        }
        comp = ((BigDecimalSpinnerModel) ((JSpinner) getNotNullEditor().getEditor()).getModel()).getMaximum();
        if (comp != null) {
            final BigDecimal val = (BigDecimal) comp;
            notNullNumEditor.setValue(val);
            return;
        }
        notNullNumEditor.setValue(BigDecimal.ZERO);
    }

    @Override
    public NotNullEditor getNotNullEditor() {
        return notNullNumEditor;
    }

    @Override
    public NullEditor getNullEditor() {
        return nullEditor;
    }
}
