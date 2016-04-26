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

/*
 * 9/28/11 9:32 AM
 */
package org.radixware.kernel.designer.ads.editors.common.adsvalasstr;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.designer.common.dialogs.components.LineTextField;


public class StringEditorComponent extends BaseEditorComponent<StringEditorComponent.LocalModel> {

    public static class LocalModel extends BaseEditorComponent.BaseEditorModel<String> {

        @Override
        protected String toLocal(AdsValAsStr value) {
            if (value == null || value == AdsValAsStr.NULL_VALUE) {
                return null;
            }
            return value.toString();
        }

        @Override
        protected AdsValAsStr toExternal(String local) {
            if (local == null) {
                return AdsValAsStr.NULL_VALUE;
            }
            return AdsValAsStr.Factory.newInstance(local);
        }

        @Override
        public void updateValue(Object... params) {
            String value = (String) params[0];
            setLocalValue(value, true);
        }
    }

    private JComponent editor;
    private DocumentListener docListener;

    private static JComponent createDefaultEditor() {
        return new LineTextField();
    }

    public StringEditorComponent() {
        this(createDefaultEditor());
    }

    protected StringEditorComponent(JComponent editor) {
        super(new LocalModel());

        this.editor = editor;
        docListener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                notifyComponentChanged();
            }
        };

        final JTextComponent textComponent = getTextComponent();
        if (textComponent != null) {
            textComponent.addFocusListener(new FocusListener() {

                @Override
                public void focusGained(FocusEvent e) {
                    if (!getModel().isSetValue()) {

                        lockModelChange();
                        updateEditorComponent();
                        unLockModelChange();
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (!getModel().isSetValue()) {

                        lockModelChange();
                        updateEditorComponent();
                        unLockModelChange();
                    }
                }
            });
        }
    }

    @Override
    public JComponent getEditorComponent() {
        return editor;
    }

    @Override
    protected void updateModelValue() {
        getModel().updateValue(getTextComponent().getText());
    }

    @Override
    protected void updateEditorComponent() {
        if (getModel().getLocalValue() != null) {
            getTextComponent().setText(getModel().getLocalValue().toString());
        } else {
            if (isFocusOwner()) {
                getTextComponent().setText("");
            } else {
                getTextComponent().setText(AdsValAsStr.NULL_VALUE.toString());
            }
        }
    }

    @Override
    protected void connectEditorComponent() {
        getTextComponent().getDocument().addDocumentListener(docListener);
    }

    @Override
    protected void disconnectEditorComponent() {
        getTextComponent().getDocument().removeDocumentListener(docListener);
//        getTextComponent().setDocument(new LineTextField());
//        updateEditorComponent();
    }

    @Override
    public boolean isFocusOwner() {
        return getTextComponent().isFocusOwner();
    }

    public final JTextComponent getTextComponent() {
        if (editor instanceof JTextComponent) {
            return (JTextComponent) editor;
        } else if (editor instanceof JScrollPane) {
            return (JTextComponent) ((JScrollPane) editor).getViewport().getView();
        }

        return null;
    }
}
