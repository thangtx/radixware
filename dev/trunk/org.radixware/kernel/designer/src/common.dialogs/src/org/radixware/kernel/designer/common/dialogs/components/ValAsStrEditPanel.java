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
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.components.valstreditor.ValAsStrEditor;
import org.radixware.kernel.designer.common.dialogs.components.valstreditor.ValAsStrEditorModel;
import org.radixware.kernel.designer.common.dialogs.components.valstreditor.ValAsStrValueEditor;


public class ValAsStrEditPanel extends javax.swing.JPanel {

    private static final Map<EValType, EValType> valType2ArgumentMap = new HashMap<EValType, EValType>(8);

    static {
        valType2ArgumentMap.put(EValType.BOOL, EValType.BOOL);
        valType2ArgumentMap.put(EValType.CLOB, EValType.STR);
        valType2ArgumentMap.put(EValType.CHAR, EValType.CHAR);
        valType2ArgumentMap.put(EValType.DATE_TIME, EValType.DATE_TIME);
        valType2ArgumentMap.put(EValType.INT, EValType.INT);
        valType2ArgumentMap.put(EValType.NUM, EValType.NUM);
        valType2ArgumentMap.put(EValType.OBJECT, EValType.STR);
        valType2ArgumentMap.put(EValType.PARENT_REF, EValType.PARENT_REF);
    }

    public static EValType getValTypeForArgument(EValType valType) {
        return valType2ArgumentMap.containsKey(valType) ? valType2ArgumentMap.get(valType) : EValType.STR;
    }
    private ValAsStrEditor valAsStrEditor;
    private boolean nullAble = true;
    private final ValAsStrEditorModel model = new ValAsStrEditorModel();
    private final ChangeSupport changeSupport = new ChangeSupport(this);
    private String nullIndicator;

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }
    //   private RadixObject context;

    public ValAsStrEditPanel() {
        this(null);
    }

    public ValAsStrEditPanel(String nullIndicator) {
        super(new BorderLayout());
        this.nullIndicator = nullIndicator;
        setValAsStrEditor(new ValAsStrValueEditor(EValType.STR, null, nullIndicator));
    }

//    public void setContextObject(RadixObject obj) {
//        this.context = obj;
//    }
    protected void setValAsStrEditor(ValAsStrEditor editor) {
        assert (editor != null);
        if (Utils.equals(valAsStrEditor, editor)) {
            return;
        }
        valAsStrEditor = editor;
        valAsStrEditor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                changeSupport.fireChange();
            }
        });
        valAsStrEditor.setNullAble(nullAble);
//        valAsStrEditor.setModel(model);
        valAsStrEditor.setEnabled(this.isEnabled());
        this.removeAll();
        this.add(valAsStrEditor, BorderLayout.CENTER);
        valAsStrEditor.setPreferredSize(this.getPreferredSize());
        valAsStrEditor.setMinimumSize(this.getMinimumSize());
        valAsStrEditor.setMaximumSize(this.getMaximumSize());
        this.revalidate();
        this.repaint();
    }

    protected ValAsStrEditor getValAsStrEditor() {
        return valAsStrEditor;
    }

    public void setValue(EValType valType, DdsTableDef targetTable, ValAsStr value) {
        setValAsStrEditor(getValAsStrEditorForValType(valType, targetTable));
        getValAsStrEditor().setValue(value);
        getValAsStrEditor().setModel(model);
    }

    public void setValue(EValType valType, ValAsStr value) {
        setValue(valType, null, value);
    }

    public void setDefaultValue(EValType valType, DdsTableDef targetTable) {
        setValAsStrEditor(getValAsStrEditorForValType(valType, targetTable));
        getValAsStrEditor().setDefaultValue();
        getValAsStrEditor().setModel(model);
    }

    public void setDefaultValue(EValType valType) {
        setDefaultValue(valType, null);
    }

    public ValAsStr getValue() {
        return getValAsStrEditor().getValue();
    }

    public void setNullAble(boolean nullAble) {
        this.nullAble = nullAble;
        getValAsStrEditor().setNullAble(nullAble);
    }

    public void setMinValue(Object minValue) {
        model.setMinValue(minValue);
        getValAsStrEditor().setModel(model);
    }

    public void setMaxValue(Object maxValue) {
        model.setMaxValue(maxValue);
        getValAsStrEditor().setModel(model);
    }

    public void setStep(Object step) {
        model.setStep(step);
        getValAsStrEditor().setModel(model);
    }

    private ValAsStrEditor getValAsStrEditorForValType(EValType valType, DdsTableDef targetTable) {
        valType = getValTypeForArgument(valType);
        return new ValAsStrValueEditor(valType, targetTable, nullIndicator);
    }

    @Override
    public boolean requestFocusInWindow() {
        return getValAsStrEditor().requestFocusInWindow();
    }

    @Override
    public void setEnabled(boolean enabled) {
        getValAsStrEditor().setEnabled(enabled);
        super.setEnabled(enabled);
    }

    @Override
    public BaselineResizeBehavior getBaselineResizeBehavior() {
        return getValAsStrEditor().getBaselineResizeBehavior();
    }

    @Override
    public int getBaseline(int width, int height) {
        return getValAsStrEditor().getBaseline(width, height);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        if (preferredSize != null) {
            getValAsStrEditor().setPreferredSize(preferredSize);
        }
        super.setPreferredSize(preferredSize);
    }

    @Override
    public Dimension getPreferredSize() {
        return getValAsStrEditor().getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getValAsStrEditor().getMaximumSize();
    }

    @Override
    public void setMaximumSize(Dimension maximumSize) {
        if (maximumSize != null) {
            getValAsStrEditor().setMaximumSize(maximumSize);
        }
        super.setMaximumSize(maximumSize);
    }

    @Override
    public Dimension getMinimumSize() {
        return getValAsStrEditor().getMinimumSize();
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        if (minimumSize != null) {
            getValAsStrEditor().setMinimumSize(minimumSize);
        }
        super.setMinimumSize(minimumSize);
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        ValAsStrEditor editor = getValAsStrEditor();
        if (editor != null) {
            editor.setForeground(fg);
        }
    }
    
    
}
