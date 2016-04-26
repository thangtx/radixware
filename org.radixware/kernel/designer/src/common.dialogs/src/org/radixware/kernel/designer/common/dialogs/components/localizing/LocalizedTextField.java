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

import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.enums.EIsoLanguage;


final class LocalizedTextField extends JTextField implements ILocalizedEditor {
    EIsoLanguage language;
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public LocalizedTextField(EIsoLanguage language) {
        super();
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
        LocalizingEditorPanel.installUndoRedoAction(this);
    }

    @Override
    public EIsoLanguage getLanguage() {
        return language;
    }

    @Override
    public void setLanguage(EIsoLanguage language) {
        this.language = language;
    }

    @Override
    public void setText(String text) {
        String oldText = getText();
        super.setText(text);
        fireChangeEvent();
        firePropertyChange(CONTENT, oldText, text);
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    @Override
    public void setReadonly(boolean readonly) {
        setEditable(!readonly);
    }

    private void fireChangeEvent() {
        changeSupport.fireChange();
    }

}
