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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassMember;


public class ApiUsageVariantEditor extends javax.swing.JPanel {

    private AdsClassMember classMember;
    private CheckedNumberSpinnerEditor editor;
    private ChangeSupport changeSupport = new ChangeSupport(this);
    private ChangeListener changeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            ApiUsageVariantEditor.this.changeSupport.fireChange();
        }
    };

    public void addChangeListener(ChangeListener listener){
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener){
        changeSupport.removeChangeListener(listener); 
    }

    public Integer getCurrentValue() {
        Object value = editor.getSpinner().getValue();
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Long) {
            return ((Long) value).intValue();
        }
        return -1;
    }

    public ApiUsageVariantEditor() {
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, Short.MAX_VALUE, 1);
        JSpinner spinner = new JSpinner(model);
        editor = new CheckedNumberSpinnerEditor(spinner);
        spinner.setEditor(editor);
        spinner.addChangeListener(changeListener);

        setLayout(new BorderLayout());
        add(spinner, BorderLayout.CENTER);
    }

    public AdsClassMember getClassMember() {
        return classMember;
    }

    public void setClassMember(AdsClassMember classMember) {
        this.classMember = classMember;
    }

    public CheckedNumberSpinnerEditor getEditor() {
        return editor;
    }

    public void setEditor(CheckedNumberSpinnerEditor editor) {
        this.editor = editor;
    }

    @Override
    public Dimension getPreferredSize() {
        if (editor != null) {
            return editor.getPreferredSize();
        }
        return super.getPreferredSize();
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        if (editor != null && preferredSize != null) {
            editor.setPreferredSize(preferredSize);
        }
        super.setPreferredSize(preferredSize);
    }

    @Override
    public void setMaximumSize(Dimension maximumSize) {
        if (editor != null && maximumSize != null) {
            editor.setMaximumSize(maximumSize);
        }
        super.setMaximumSize(maximumSize);
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        if (editor != null && minimumSize != null) {
            editor.setMinimumSize(minimumSize);
        }
        super.setMinimumSize(minimumSize);
    }

    @Override
    public Dimension getMaximumSize() {
        if (editor != null) {
            return editor.getMaximumSize();
        }
        return super.getMaximumSize();
    }

    @Override
    public Dimension getMinimumSize() {
        if (editor != null) {
            return editor.getMinimumSize();
        }
        return super.getMinimumSize();
    }

    @Override
    public int getBaseline(int width, int height) {
        if (editor != null) {
            return editor.getBaseline(width, height);
        }
        return super.getBaseline(width, height);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (editor != null) {
            editor.setEnabled(enabled);
        }
    }

    @Override
    public boolean isEnabled() {
        if (editor != null) {
            return editor.isEnabled();
        }
        return super.isEnabled();
    }

    @Override
    public boolean requestFocusInWindow() {
        if (editor != null) {
            return editor.requestFocusInWindow();
        }
        return super.requestFocusInWindow();
    }

    @Override
    public Color getForeground() {
        if (editor != null) {
            return editor.getForeground();
        }
        return super.getForeground();
    }

    @Override
    public void setForeground(Color fg) {
        if (editor != null) {
            editor.setForeground(fg);
        }
    }

    public void open(AdsClassMember classMember) {
        this.classMember = classMember;
        editor.getModel().setValue(classMember.getApiUsageVariant());
        editor.setEnabled(!classMember.isReadOnly());
    }
}
