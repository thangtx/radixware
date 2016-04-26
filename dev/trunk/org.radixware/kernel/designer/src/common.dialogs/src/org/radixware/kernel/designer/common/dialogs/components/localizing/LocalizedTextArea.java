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

package org.radixware.kernel.designer.common.dialogs.components.localizing;

import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.common.dialogs.components.SizableTextArea;


final class LocalizedTextArea extends SizableTextArea implements ILocalizedEditor {

    private EIsoLanguage language;
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public LocalizedTextArea(EIsoLanguage language, String initText) {
        this(language, initText, 0);
    }

    public LocalizedTextArea(EIsoLanguage language, String initText, int rowCount) {
        super(rowCount);

        super.setText(initText);

        setLanguage(language);
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fireChangeEvent();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fireChangeEvent();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fireChangeEvent();
            }
        });
        setWrapStyleWord(true);
        setLineWrap(true);
        invertFocusTraversalBehaviour();
        LocalizingEditorPanel.installUndoRedoAction(this);
    }

    private void invertFocusTraversalBehaviour() {
        Set<AWTKeyStroke> forwardKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        Set<AWTKeyStroke> backwardKeys = getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
        // check that we WANT to modify current focus traversal keystrokes
        if (forwardKeys.size() != 1 || backwardKeys.size() != 1) {
            return;
        }
        final AWTKeyStroke fks = forwardKeys.iterator().next();
        final AWTKeyStroke bks = backwardKeys.iterator().next();
        final int fkm = fks.getModifiers();
        final int bkm = bks.getModifiers();
        final int ctrlMask = KeyEvent.CTRL_MASK + KeyEvent.CTRL_DOWN_MASK;
        final int ctrlShiftMask = KeyEvent.SHIFT_MASK + KeyEvent.SHIFT_DOWN_MASK + ctrlMask;
        if (fks.getKeyCode() != KeyEvent.VK_TAB || (fkm & ctrlMask) == 0 || (fkm & ctrlMask) != fkm) {
            // not currently CTRL+TAB for forward focus traversal
            return;
        }
        if (bks.getKeyCode() != KeyEvent.VK_TAB || (bkm & ctrlShiftMask) == 0 || (bkm & ctrlShiftMask) != bkm) {
            // not currently CTRL+SHIFT+TAB for backward focus traversal
            return;
        }
        // bind our new forward focus traversal keys
        Set<AWTKeyStroke> newForwardKeys = new HashSet<>(1);
        newForwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.unmodifiableSet(newForwardKeys));
        // bind our new backward focus traversal keys
        Set<AWTKeyStroke> newBackwardKeys = new HashSet<>(1);
        newBackwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_MASK + KeyEvent.SHIFT_DOWN_MASK));
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, Collections.unmodifiableSet(newBackwardKeys));
        // Now, it's still useful to be able to type TABs in some cases.
        // Using this technique assumes that it's rare however (if the user
        // is expected to want to type TAB often, consider leaving text area's
        // behaviour unchanged...).  Let's add some key bindings, inspired
        // from a popular behaviour in instant messaging applications...
        TextInserter.applyTabBinding(this);
        // we could do the same stuff for RETURN and CTRL+RETURN for activating
        // the root pane's default button: omitted here for brevity
    }

    @Override
    public EIsoLanguage getLanguage() {
        return language;
    }

    @Override
    public void setLanguage(EIsoLanguage language) {
        this.language = language;
        //There must be no default value, bacause when new string is opened,
        //it has no any values, so text fields must be empty too.
        //setText(language == null ? "" : language.getValue());
    }

    @Override
    public void setText(String text) {
        String oldText = getText();
        super.setText(text);
        fireChangeEvent();
        firePropertyChange(ILocalizedEditor.CONTENT, oldText, text);
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    protected void fireChangeEvent() {
        changeSupport.fireChange();
    }

    @Override
    public void setReadonly(boolean readonly) {
        setEditable(!readonly);
    }

    public static class TextInserter extends AbstractAction {

        private JTextArea textArea;
        private String insertable;

        private TextInserter(JTextArea textArea, String insertable) {
            this.textArea = textArea;
            this.insertable = insertable;
        }

        public static void applyTabBinding(JTextArea textArea) {
            textArea.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_MASK + KeyEvent.CTRL_DOWN_MASK), "tab");
            textArea.getActionMap().put("tab", new TextInserter(textArea, "\t"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            // could be improved to overtype selected range
            textArea.insert(insertable, textArea.getCaretPosition());
        }
    }
}
