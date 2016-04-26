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

package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.Border;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.ads.editors.clazz.report.ScaleComboBox.Scale;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagramOptions;


public class ScaleComboBox extends JComboBox<Scale>{

    public class Scale {

        private final int value;

        public Scale(final int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value + "%";
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Scale) {
                final Scale scale = (Scale)obj;
                return scale.value == this.value;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 11 * hash + this.value;
            return hash;
        }

        public int getValue() {
            return value;
        }

    }

    private class ScaleTextField extends JTextField {

        private final Set<Integer> allowedKeys = new HashSet<>(8);
        private final JComponent parent;

        @Override
        protected void processKeyEvent(final KeyEvent ev) {
            final int keyCode = ev.getKeyCode();
            final char keyChar = ev.getKeyChar();
            if (Character.isDigit(keyChar) || allowedKeys.contains(keyCode)) {
                super.processKeyEvent(ev);
            } else if ((keyCode == KeyEvent.VK_ENTER) || (keyCode == KeyEvent.VK_ESCAPE)) {
                parent.requestFocusInWindow();
                prepare();
                ev.consume();
            } else {
                ev.consume();
            }
        }

        private void prepare() {
            if (!getText().endsWith("%")) {
                setText(getText() + "%");
            }
            while (getText().length() > 2 && getText().startsWith("0")) {
                setText(getText().substring(1));
            }
        }

        public ScaleTextField(final JComponent parent) {
            super();
            this.parent = parent;
            allowedKeys.add(Integer.valueOf(KeyEvent.VK_UP));
            allowedKeys.add(Integer.valueOf(KeyEvent.VK_DOWN));
            allowedKeys.add(Integer.valueOf(KeyEvent.VK_LEFT));
            allowedKeys.add(Integer.valueOf(KeyEvent.VK_RIGHT));
            allowedKeys.add(Integer.valueOf(KeyEvent.VK_BACK_SPACE));
            allowedKeys.add(Integer.valueOf(KeyEvent.VK_DELETE));
            allowedKeys.add(Integer.valueOf(KeyEvent.VK_HOME));
            allowedKeys.add(Integer.valueOf(KeyEvent.VK_END));
            this.addFocusListener(new FocusListener() {

                @Override
                public void focusGained(final FocusEvent e) {
                    if (getText().endsWith("%")) {
                        setText(getText().substring(0, getText().length() - 1));
                    }
                }

                @Override
                public void focusLost(final FocusEvent e) {
                    prepare();
                }
            });
        }

    }

    private class ScaleComboBoxEditor implements ComboBoxEditor {

        private final JTextField editor;

        public ScaleComboBoxEditor(final JComponent parent) {
            editor = new ScaleTextField(parent);
        }

        @Override
        public Component getEditorComponent() {
            return editor;
        }

        @Override
        public void setItem(final Object anObject) {
            if ( anObject != null )  {
                final Scale scale = (Scale)anObject;
                editor.setText(scale.getValue() + "%");
            } else {
                editor.setText("");
            }
        }

        @Override
        public Object getItem() {
            String str = editor.getText();
            if (str.endsWith("%")) {
                str = str.substring(0, str.length() - 1);
            }
            int scale = str.isEmpty() ? 25 : Integer.valueOf(str);
            if (scale < 25) {
                scale = 25;
            }
            editor.setText(scale + "%");
            return new Scale(scale);
        }

        @Override
        public void selectAll() {
            editor.selectAll();
            editor.requestFocus();
        }

        @Override
        public void addActionListener(final ActionListener l) {
            editor.addActionListener(l);
        }

        @Override
        public void removeActionListener(final ActionListener l) {
            editor.removeActionListener(l);
        }

    }

    private final IRadixEventListener<RadixEvent> listener = new IRadixEventListener<RadixEvent>() {

        @Override
        public void onEvent(final RadixEvent e) {
            final int scl = AdsReportFormDiagramOptions.getDefault().getScalePercent();
            final Scale scale = new Scale(scl);
            setSelectedItem(scale);
        }
    };

    public ScaleComboBox(final JComponent parent) {
        super();
        final int scl = AdsReportFormDiagramOptions.getDefault().getScalePercent();
        final Scale scale = new Scale(scl);
        final ArrayList<Scale> scales = new ArrayList<Scale>();
//        scales.add(new Scale(25));
        scales.add(new Scale(50));
        scales.add(new Scale(75));
        scales.add(new Scale(100));
        scales.add(new Scale(150));
        scales.add(new Scale(200));
        scales.add(new Scale(300));
        scales.add(new Scale(400));
        if (!scales.contains(scale)) {
            for (int i = 0; i <= scales.size(); ++i) {
                if (i == scales.size() || scales.get(i).getValue() > scale.getValue()) {
                    scales.add(i, scale);
                    break;
                }
            }
        }
        //DefaultComboBoxModel<Scale> model=new DefaultComboBoxModel(scales.toArray());
        Scale[] arr=new Scale[scales.size()];        
        this.setModel(new DefaultComboBoxModel<>(scales.toArray(arr)));
        this.setSelectedItem(scale);
        this.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent e) {
                AdsReportFormDiagramOptions.getDefault().setPercent(
                        ((Scale)ScaleComboBox.this.getSelectedItem()).getValue());
            }
        });
        AdsReportFormDiagramOptions.getDefault().addChangeListener(listener);
        this.setEditable(true);
        final Border border = ((JComponent)this.getEditor().getEditorComponent()).getBorder();
        this.setEditor(new ScaleComboBoxEditor(parent));
        ((JComponent)this.getEditor().getEditorComponent()).setBorder(border);
        this.setToolTipText("Scale");
        this.setMaximumSize(new Dimension(75, 24));
    }

}
