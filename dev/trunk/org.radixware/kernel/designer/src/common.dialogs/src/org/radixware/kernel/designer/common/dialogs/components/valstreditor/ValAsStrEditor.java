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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;

public abstract class ValAsStrEditor extends JPanel {

    protected abstract ValueEditor getValueEditor();

    public String getNullValueIndicator() {
        return nullIndicator;
    }
    private String nullIndicator;

    public abstract EValType getValType();
    final ChangeSupport changeSupport = new ChangeSupport(this);

    public ValAsStrEditor() {
        this("<Not Defined>");
    }

    public ValAsStrEditor(String nullIndicator) {
        super(new BorderLayout());
        if (nullIndicator == null) {
            this.nullIndicator = "<Not Defined>";
        } else {
            this.nullIndicator = nullIndicator;
        }
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateVisibleEditor();
            }
        });
    }

    public void setValue(ValAsStr value) {
        if (value == null || "null".equals(value.toString())) {
            getValueEditor().setValue(null);
        } else {
            try {
                getValueEditor().setValue(value.toObject(getValType()));
            } catch (WrongFormatError ex) {
                Logger.getLogger(ValAsStrEditor.class.getName()).log(Level.SEVERE, "Unconvertable value as string, reset to null value");
                getValueEditor().setValue(null);
            }
        }
    }

    public void activateByKeyInput(KeyEvent e) {
        getValueEditor().activateByKeyInput(e);
    }

    public ValAsStr getValue() {
        return ValAsStr.Factory.newInstance(getValueEditor().getValue(), getValType());
    }

    public void setDefaultValue() {
        getValueEditor().setDefaultValue();
    }

    public void setNullAble(boolean nullAble) {
        getValueEditor().setNullAble(nullAble);
    }

    public void setModel(ValAsStrEditorModel model) {
        getValueEditor().setModel(model);
    }

    private void updateVisibleEditor() {
        if (getValueEditor() == null) {
            return;
        }
        final JComponent comp = getValue() == null && getValueEditor().getNullEditor() != null
                ? getValueEditor().getNullEditor()
                : getValueEditor().getNotNullEditor().getEditor();
        comp.setForeground(getForeground());
        if (this.getComponentCount() > 0 && this.getComponent(0).equals(comp)) {
            return;
        }
        this.removeAll();
        this.add(comp, BorderLayout.CENTER);
        comp.setVisible(true);
        comp.requestFocusInWindow();
        ValAsStrEditor.this.revalidate();
        ValAsStrEditor.this.repaint();

    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    @Override
    public boolean requestFocusInWindow() {
        if (getValue() == null && getValueEditor().getNullEditor() != null) {
            return getValueEditor().getNullEditor().requestFocusInWindow();
        } else {
            return getValueEditor().getNotNullEditor().getEditor().requestFocusInWindow();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (getValueEditor().getNullEditor() != null) {
            getValueEditor().getNullEditor().setEnabled(enabled);
        }
        getValueEditor().getNotNullEditor().getEditor().setEnabled(enabled);
        super.setEnabled(enabled);
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior() {
        if (getValue() == null && getValueEditor().getNullEditor() != null) {
            return getValueEditor().getNullEditor().getBaselineResizeBehavior();
        } else {
            return getValueEditor().getNotNullEditor().getEditor().getBaselineResizeBehavior();
        }
    }

    @Override
    public int getBaseline(int width, int height) {
        if (getValue() == null && getValueEditor().getNullEditor() != null) {
            return getValueEditor().getNullEditor().getBaseline(width, height);
        } else {
            return getValueEditor().getNotNullEditor().getEditor().getBaseline(width, height);
        }
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        if (getValueEditor().getNullEditor() != null) {
            getValueEditor().getNullEditor().setPreferredSize(preferredSize);
        }
        getValueEditor().getNotNullEditor().getEditor().setPreferredSize(preferredSize);
        super.setPreferredSize(preferredSize);
    }

    @Override
    public Dimension getPreferredSize() {
        if (getValue() == null && getValueEditor().getNullEditor() != null) {
            return getValueEditor().getNullEditor().getPreferredSize();
        } else {
            return getValueEditor().getNotNullEditor().getEditor().getPreferredSize();
        }
    }

    @Override
    public Dimension getMaximumSize() {
        if (getValue() == null && getValueEditor().getNullEditor() != null) {
            return getValueEditor().getNullEditor().getMaximumSize();
        } else {
            return getValueEditor().getNotNullEditor().getEditor().getMaximumSize();
        }
    }

    @Override
    public void setMaximumSize(Dimension maximumSize) {
        if (getValueEditor().getNullEditor() != null) {
            getValueEditor().getNullEditor().setMaximumSize(maximumSize);
        }
        getValueEditor().getNotNullEditor().getEditor().setMaximumSize(maximumSize);
        super.setMaximumSize(maximumSize);
    }

    @Override
    public Dimension getMinimumSize() {
        if (getValue() == null && getValueEditor().getNullEditor() != null) {
            return getValueEditor().getNullEditor().getMinimumSize();
        } else {
            return getValueEditor().getNotNullEditor().getEditor().getMinimumSize();
        }
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        if (getValueEditor().getNullEditor() != null) {
            getValueEditor().getNullEditor().setMinimumSize(minimumSize);
        }
        getValueEditor().getNotNullEditor().getEditor().setMinimumSize(minimumSize);
        super.setMinimumSize(minimumSize);
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        updateVisibleEditor();
    }
    
    
}
