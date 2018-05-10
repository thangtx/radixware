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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.components.ExtendableTextField.ExtendableTextChangeEvent;
import org.radixware.kernel.common.components.ExtendableTextField.ExtendableTextChangeListener;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.common.dialogs.components.MultilinedTextField;


class ExpandableLanguageTextField extends MultilinedTextField implements ILineLocalizedEditor {

    public ExpandableLanguageTextField(EIsoLanguage language, RadixObject context) {
        super(true, language, context);
        LocalizingEditorPanel.installUndoRedoAction(field);
        this.language = language;
        getChangeSupport().addEventListener(new ExtendableTextChangeListener() {

            @Override
            public void onEvent(ExtendableTextChangeEvent e) {
                fireChangeEvent();
            }
        });
    }

    private EIsoLanguage language;
    private ChangeSupport changeSupport = new ChangeSupport(this);
    private JLabel checkedInfoLable;
    private JLabel agreedInfoLable;
    private JButton openRichEditor;

    @Override
    public void setReadonly(boolean readonly) {
        this.setReadOnly(readonly);
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
        super.setValue(replaceNewLine2Square(text));
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

    protected void fireChangeEvent() {
        changeSupport.fireChange();
    }

    @Override
    public JLabel getCheckedInfoLable() {
        return checkedInfoLable;
    }

    @Override
    public void setCheckedInfoLable(JLabel checkedInfoLable) {
        this.checkedInfoLable = checkedInfoLable;
    }

    public void setAgreedInfoLable(JLabel agreedInfoLable) {
        this.agreedInfoLable = agreedInfoLable;
    }

    public JLabel getAgreedInfoLable() {
        return agreedInfoLable;
    }

    public JButton getOpenRichEditor() {
        return openRichEditor;
    }

    public void setOpenRichEditor(JButton openRichEditor) {
        this.openRichEditor = openRichEditor;
    }
    
    
}
