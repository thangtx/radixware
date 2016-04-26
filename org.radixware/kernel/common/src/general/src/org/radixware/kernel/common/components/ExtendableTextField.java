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
package org.radixware.kernel.common.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;

public class ExtendableTextField extends javax.swing.JPanel {

    public static final Integer EDITOR_TEXTFIELD = 0;
    public static final Integer EDITOR_COMBO = 1;
    public static final Integer EDITOR_HTML_TEXTFIELD = 2;
    private JComponent field;
    private int fieldType;
    private int buttonsCount = 0;
    private Vector<JButton> buttons = new Vector<JButton>();
    private JButton priorButton;
    private Object prevValue = null;
    private MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (fieldType == EDITOR_COMBO
                        || ((fieldType == EDITOR_TEXTFIELD || fieldType == EDITOR_HTML_TEXTFIELD)
                        && !((JTextComponent) field).isEditable())) {
                    ExtendableTextField.this.onMouseClicked(e);
                    super.mouseClicked(e);
                }
            }
    };
        

    public ExtendableTextField() {
        this(false);
    }

    public ExtendableTextField(boolean readonly) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        fieldType = EDITOR_TEXTFIELD;
        field = new JTextField();
        
        addTextMouseListener();
        ((JTextField) field).setEditable(!readonly);
        add(field);
        textFieldChangeSupportSetup();
    }
    
    public final void addTextMouseListener(){
        if (fieldType != EDITOR_COMBO){
            field.addMouseListener(mouseAdapter);
        }
    }
    
    public final void removeTextMouseListener(){
        field.removeMouseListener(mouseAdapter);
    }

    protected void onMouseClicked(MouseEvent e) {
        int btn = e.getButton();
        if (btn == MouseEvent.BUTTON1) {
            if (buttons.size() > 0) {
                if (priorButton != null && priorButton.isEnabled() && priorButton.isVisible()) {
                    ActionListener[] btnListeners = priorButton.getActionListeners();
                    if (btnListeners != null && btnListeners.length > 0) {
                        for (ActionListener a : btnListeners) {
                            a.actionPerformed(new ActionEvent(this, 0, ""));
                        }
                    }
                } else {
                    if (buttons.get(0).isEnabled() && buttons.get(0).isVisible()) {
                        ActionListener[] btnListeners = buttons.get(0).getActionListeners();
                        if (btnListeners != null && btnListeners.length > 0) {
                            for (ActionListener a : btnListeners) {
                                a.actionPerformed(new ActionEvent(this, 0, ""));
                            }
                        }
                    }
                }
            }
        }
    }

    public JButton getPriorButton() {
        if (this.priorButton != null) {
            return priorButton;
        }
        if (buttons.size() > 0) {
            return buttons.get(0);
        }
        return null;
    }

    public boolean setPriorButton(JButton button) {
        if (button == null) {
            this.priorButton = button;
            return true;
        }
        if (buttons.contains(button)) {
            this.priorButton = button;
            return true;
        }
        return false;
    }

    public Integer getEditorType() {
        return fieldType;
    }

    public void setEditorType(int fieldType) {
        if (this.fieldType != fieldType) {
            this.fieldType = fieldType;
            if (fieldType == EDITOR_COMBO) {
                JComboBox combo = new JComboBox();
                combo.setToolTipText(field.getToolTipText());
                changeEditor(combo);
            } else {
                if (fieldType == EDITOR_HTML_TEXTFIELD) {
                    HTMLTextField text = new HTMLTextField();
                    text.setToolTipText(field.getToolTipText());
                    changeEditor(text);
                } else {
                    JTextField text = new JTextField();
                    text.setToolTipText(field.getToolTipText());
                    changeEditor(text);
                }
            }
        }
    }

    private void changeEditor(JComponent editor) {
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        if (fieldType == EDITOR_HTML_TEXTFIELD) {
            add(((HTMLTextField) editor).getTopComponent());
        } else {
            add(editor);
        }
        editor.setPreferredSize(field.getPreferredSize());
        field = editor;

        for (JButton b : buttons) {
            add(b);
        }
        if (fieldType == EDITOR_TEXTFIELD || fieldType == EDITOR_HTML_TEXTFIELD) {
            textFieldChangeSupportSetup();
        } else if (fieldType == EDITOR_COMBO) {
            comboFieldChangeSupportSetup();
        }
        updateUI();
        //field.repaint();
        //repaint();
        //field.requestFocusInWindow();
    }

//    @Override
//    public void updateUI() {
//        if (field != null) {
//            field.updateUI();
//        }
//        super.updateUI();
//    }
    public boolean setEditor(JComponent editor) {
        if (editor instanceof javax.swing.JTextField) {
            fieldType = EDITOR_TEXTFIELD;
            changeEditor(editor);
            return true;
        } else if (editor instanceof javax.swing.JComboBox) {
            fieldType = EDITOR_COMBO;
            changeEditor(editor);
            return true;
        } else if (editor instanceof HTMLTextField) {
            fieldType = EDITOR_HTML_TEXTFIELD;
            changeEditor(editor);
            return true;
        }
        return false;
    }

    public JTextComponent getAsTextComponent() {
        if (fieldType == EDITOR_TEXTFIELD) {
            return (JTextField) field;
        }
        return null;
    }

    private void textFieldChangeSupportSetup() {
        DocumentListener documentListener = new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (ExtendableTextField.this.support != null) {
                    ExtendableTextField.this.support.fireEvent(new ExtendableTextChangeEvent(ExtendableTextField.this.getValue(), e));
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }
        };
        ((JTextComponent) field).getDocument().addDocumentListener(documentListener);
    }

    private void comboFieldChangeSupportSetup() {
        ((JComboBox) field).addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final Object value = ExtendableTextField.this.getValue();
                if (!Utils.equals(value, prevValue)) {
                    prevValue = value;
                    if (ExtendableTextField.this.support != null) {
                        ExtendableTextField.this.support.fireEvent(new ExtendableTextChangeEvent(value, e));
                    }
                }
            }
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        setReadOnly(!enabled);
    }

    @Override
    public boolean isEnabled() {
        return field.isEnabled();
    }

    private class ExtButton extends JButton {

        ExtButton(String title) {
            super(title);
        }

        ExtButton(Icon icon) {
            super(icon);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension fSize = field.getPreferredSize();
            return new Dimension(fSize.height, fSize.height);
        }

        @Override
        public Dimension getMaximumSize() {
            Dimension fSize = field.getMaximumSize();
            return new Dimension(getPreferredSize().width, fSize.height);
        }

        @Override
        public Dimension getMinimumSize() {
            Dimension fSize = field.getMinimumSize();
            return new Dimension(getPreferredSize().width, fSize.height);
        }
    }

    @Override
    public void addFocusListener(FocusListener l) {
        field.addFocusListener(l);
    }

    @Override
    public void removeFocusListener(FocusListener l) {
        field.removeFocusListener(l);
    }

    public JButton addButton(Icon icon) {
        ExtButton btn = new ExtButton(icon);
        add(btn);
        buttonsCount++;
        buttons.add(btn);
        return btn;
    }

    public JButton addButton(String title) {
        ExtButton btn = new ExtButton(title);
        add(btn);
        buttonsCount++;
        buttons.add(btn);
        return btn;
    }

    public JButton addButton() {
        return this.addButton("");
    }

    public void removeButton(JButton btn) {
        if (buttons.contains(btn)) {
            buttons.remove(btn);
            buttonsCount--;
        }
        //this.repaint();
    }

    public boolean isAnyButtonInFocus() {
        for (JButton b : buttons) {
            if (b.isFocusOwner()) {
                return true;
            }
        }
        return false;
    }

    public Vector<JButton> getButtons() {
        return buttons;
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        if (preferredSize != null) {
            int w = preferredSize.width - buttonsCount * preferredSize.height;
            field.setPreferredSize(new Dimension(w, preferredSize.height));
        }
        super.setPreferredSize(preferredSize);
    }

    @Override
    public Dimension getPreferredSize() {
        if (buttonsCount != 0) {
            Dimension supersize = super.getPreferredSize();
            Dimension fieldsize = field.getPreferredSize();
            return new Dimension(supersize.width, fieldsize.height);
        }
        return field.getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        if (buttonsCount != 0) {
            Dimension supersize = super.getMaximumSize();
            Dimension fieldsize = field.getMaximumSize();
            return new Dimension(supersize.width, fieldsize.height);
        }
        return field.getMaximumSize();
    }

    @Override
    public void setMaximumSize(Dimension maximumSize) {
        if (maximumSize != null) {
            int w = maximumSize.width - buttonsCount * maximumSize.height;
            field.setMaximumSize(new Dimension(w, maximumSize.height));
        }
        super.setMaximumSize(maximumSize);
    }

    @Override
    public Dimension getMinimumSize() {
        if (buttonsCount != 0) {
            Dimension supersize = super.getMinimumSize();
            Dimension fieldsize = field.getMinimumSize();
            return new Dimension(supersize.width, fieldsize.height);
        }
        return field.getMinimumSize();
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        if (minimumSize != null) {
            int w = minimumSize.width - buttonsCount * minimumSize.height;
            field.setMinimumSize(new Dimension(w, minimumSize.height));
        }
        super.setMinimumSize(minimumSize);
    }

    @Override
    public int getBaseline(int width, int height) {
        return field.getBaseline(width, height);
    }

    @Override
    public boolean requestFocusInWindow() {
        return field.requestFocusInWindow();
    }

    @Override
    public Color getForeground() {
        return field != null ? field.getForeground() : super.getForeground();
    }

    @Override
    public void setForeground(Color color) {
        if (field != null) {
            field.setForeground(color);
        }
        super.setForeground(color);
    }

    public Object getValue() {
        if (fieldType == EDITOR_TEXTFIELD || fieldType == EDITOR_HTML_TEXTFIELD) {
            return ((JTextComponent) field).getText();
        } else if (fieldType == EDITOR_COMBO) {
            return ((JComboBox) field).getSelectedItem();
        }
        return null;
    }

    public void setValue(Object value) {
        prevValue = value;
        if (fieldType == EDITOR_TEXTFIELD) {
            // The first realization ignored null.
            // This caused a problem: the editor was not updated after the change of non empty value to null.
            // The second realization showed optional value instead of null and returns null in getValue().
            // This caused an exception: attempt to set null to mandatory value (xml.setValue(editor.getValue());
            // The third realization shows an empty string instead of null.
            ((JTextField) field).setText(value != null ? value.toString() : "");
        } else if (fieldType == EDITOR_COMBO) {
            ((JComboBox) field).setSelectedItem(value);
        } else if (fieldType == EDITOR_HTML_TEXTFIELD) {
            ((JTextComponent) field).setText(value != null ? value.toString() : "");
        }
    }

    public boolean setTextFieldValue(String value) {
        if (fieldType == EDITOR_TEXTFIELD || fieldType == EDITOR_HTML_TEXTFIELD) {
            setValue(value);
            return true;
        }
        return false;
    }

    public boolean setComboBoxRenderer(ListCellRenderer renderer) {
        if (fieldType == EDITOR_COMBO) {
            ((JComboBox) field).setRenderer(renderer);
            return true;
        }
        return false;
    }

    public boolean setComboBoxModel(ComboBoxModel aModel) {
        if (fieldType == EDITOR_COMBO) {
            ((JComboBox) field).setModel(aModel);
            return true;
        }
        return false;
    }

    public boolean setComboBoxSelectedItem(Object item) {
        if (fieldType == EDITOR_COMBO) {
            ((JComboBox) field).setSelectedItem(item);
            return true;
        }
        return false;
    }

    public Object getComboBoxSelectedItem() {
        if (fieldType == EDITOR_COMBO) {
            return ((JComboBox) field).getSelectedItem();
        }
        return null;
    }

    public boolean addComboItem(Object item) {
        if (fieldType == EDITOR_COMBO) {
            ((JComboBox) field).addItem(item);
            return true;
        }
        return false;
    }

    public boolean removeAllComboItems() {
        if (fieldType == EDITOR_COMBO) {
            ((JComboBox) field).removeAllItems();
            return true;
        }
        return false;
    }

    public boolean getReadOnly() {
        if (fieldType == EDITOR_TEXTFIELD || fieldType == EDITOR_HTML_TEXTFIELD) {
            return !((JTextComponent) field).isEditable();
        } else if (fieldType == EDITOR_COMBO) {
            return !((JComboBox) field).isEnabled();
        }
        return false;
    }

    public void setReadOnly(boolean readonly) {
        if (fieldType == EDITOR_TEXTFIELD) {
            ((JTextField) field).setEnabled(!readonly);
        } else if (fieldType == EDITOR_COMBO) {
            ((JComboBox) field).setEnabled(!readonly);
        } else if (fieldType == EDITOR_HTML_TEXTFIELD) {
            ((JTextComponent) field).setEnabled(!readonly);
        }
        for (JButton b : buttons) {
            b.setEnabled(!readonly);
        }
    }

    public boolean isEditable() {
        if (fieldType == EDITOR_TEXTFIELD) {
            return ((JTextField) field).isEditable();
        } else if (fieldType == EDITOR_COMBO) {
            return ((JComboBox) field).isEditable();
        } else if (fieldType == EDITOR_HTML_TEXTFIELD) {
            ((JTextComponent) field).isEditable();
        }
        return false;
    }

    public void setEditable(boolean editable) {
        if (fieldType == EDITOR_TEXTFIELD) {
            ((JTextField) field).setEditable(editable);
        } else if (fieldType == EDITOR_COMBO) {
            ((JComboBox) field).setEditable(editable);
        } else if (fieldType == EDITOR_HTML_TEXTFIELD) {
            ((JTextComponent) field).setEditable(editable);
        }
    }

    public void selectAll() {
        if (fieldType == EDITOR_TEXTFIELD) {
            ((JTextField) field).selectAll();
        } else if (fieldType == EDITOR_COMBO) {
            ((JComboBox) field).getEditor().selectAll();
        } else if (fieldType == EDITOR_HTML_TEXTFIELD) {
            ((JTextComponent) field).selectAll();
        }
    }

    @Override
    public void setToolTipText(String text) {
        field.setToolTipText(text);
        super.setToolTipText(text);
    }
    private ExtendableTextFieldChangeSupport support;

    public static class ExtendableTextChangeEvent extends RadixEvent {

        private Object value;
        private Object event;

        public ExtendableTextChangeEvent(Object value, Object event) {
            this.value = value;
            this.event = event;
        }

        public Object getCurrentTextValue() {
            return value;
        }

        public Object getActualEvent() {
            return event;
        }
    }

    public interface ExtendableTextChangeListener extends IRadixEventListener<ExtendableTextChangeEvent> {
    }

    public static class ExtendableTextFieldChangeSupport extends RadixEventSource<ExtendableTextChangeListener, ExtendableTextChangeEvent> {

        private javax.swing.event.EventListenerList listeners = new javax.swing.event.EventListenerList();

        @Override
        public synchronized void addEventListener(ExtendableTextChangeListener listener) {
            listeners.add(ExtendableTextChangeListener.class, listener);
        }

        @Override
        public synchronized void removeEventListener(ExtendableTextChangeListener listener) {
            listeners.remove(ExtendableTextChangeListener.class, listener);
        }

        @Override
        public void fireEvent(ExtendableTextChangeEvent event) {
            Object[] l = listeners.getListeners(ExtendableTextChangeListener.class);
            for (int i = 0; i <= l.length - 1; i++) {
                if (event != null) {
                    ((ExtendableTextChangeListener) l[i]).onEvent(event);
                }
            }
        }
    }

    public synchronized ExtendableTextFieldChangeSupport getChangeSupport() {
        if (support == null) {
            support = new ExtendableTextFieldChangeSupport();
        }
        return support;
    }

//    @Override
//    public void repaint() {
//        if (field != null) {
//            field.repaint();
//        }
//        super.repaint();
//    }
    public synchronized void addChangeListener(ExtendableTextChangeListener listener) {
        getChangeSupport().addEventListener(listener);
    }

    public synchronized void removeChangeListener(ExtendableTextChangeListener listener) {
        getChangeSupport().removeEventListener(listener);
    }

    public boolean setComboBoxPrototypeDisplayValue(Object value) {
        if (fieldType == EDITOR_COMBO) {
            JComboBox combo = (JComboBox) field;
            combo.setPrototypeDisplayValue(value);
            return true;
        }
        return false;
    }
}
