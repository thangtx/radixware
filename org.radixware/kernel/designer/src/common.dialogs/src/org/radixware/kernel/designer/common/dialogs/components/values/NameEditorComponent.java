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
 * 9/28/11 5:34 PM
 */
package org.radixware.kernel.designer.common.dialogs.components.values;

import java.util.Objects;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.components.values.NameEditorComponent.NamedContext;

/**
 * Editor for editing name of named objects
 *
 */
public class NameEditorComponent extends ContextableValueEditorComponent<String, NamedContext> {

    public static class Alphabet {

        public final String allowedChars;
        public final String invalidChars;
        public final String allowedStartChars;
        public final String invalidStartChars;

        public Alphabet(String allowedStartChars, String invalidStartChars, String allowedChars, String invalidChars) {
            this.allowedChars = allowedChars != null ? allowedChars : "";
            this.allowedStartChars = allowedStartChars != null ? allowedStartChars : "";
            this.invalidChars = invalidChars != null ? invalidChars : "";
            this.invalidStartChars = invalidStartChars != null ? invalidStartChars : "";
        }
    }

    protected static class EditorModel extends ContextableValueEditorComponent.DefaultSampleEditorModel<String, NamedContext> {

        private static final Alphabet DEFAULT_ALPHABET;
        private Alphabet alphabet;

        static {
            String allowedChars = "",
                    invalidChars = " ()*&^%#@!~`+-=\\|/?.,:;\'\"<>[]{}",
                    allowedStartChars = "",
                    invalidStartChars = invalidChars + "0123456789";

            DEFAULT_ALPHABET = new Alphabet(allowedStartChars, invalidStartChars, allowedChars, invalidChars);
        }

        public EditorModel() {
            this(DEFAULT_ALPHABET);
        }

        public EditorModel(Alphabet abc) {
            super();

            alphabet = abc;
        }

        @Override
        protected void commitImpl() {
            if (getContext().isValidName(getValue())) {
                getContext().setName(getValue());
            }
        }

        @Override
        protected String getValueFromContext(NamedContext context) {
            return context != null ? context.getName() : null;
        }

        @Override
        public void updateValue(Object... params) {
            String value = (String) params[0];
            setLocalValue(value, true);
        }

        @Override
        public String getNullValue() {
            return "";
        }

        @Override
        public boolean isValidValue(String value) {
            assert isOpened() : "Editor not opened";
            return isOpened() && getContext().isValidName(value) && isValidString(value) || Utils.equals(getNullValue(), value);
        }

        public Alphabet getAlphabet() {
            return alphabet;
        }

        public void setAlphabet(Alphabet alphabet) {
            this.alphabet = alphabet;
        }

        public String getValid(String str, boolean atFirst) {
            if (str == null) {
                return getNullValue();
            }

            final StringBuilder valid = new StringBuilder();

            boolean first = atFirst;
            for (char ch : str.toCharArray()) {
                if (!isValidChar(ch, first)) {
                    return null;
//                    continue;
                }
                valid.append(ch);
                if (first) {
                    first = false;
                }
            }
            return valid.toString();
        }

        public boolean isValidChar(char ch, boolean first) {
            String validStr = first ? alphabet.allowedStartChars : alphabet.allowedChars,
                    invalidStr = first ? alphabet.invalidStartChars : alphabet.invalidChars;

            if ((!validStr.isEmpty() && validStr.indexOf(ch) == -1)
                    || (!invalidStr.isEmpty() && invalidStr.indexOf(ch) >= 0)) {
                return false;
            }
            return true;
        }

        private boolean isValidString(String str) {
            boolean first = true;
            for (char ch : str.toCharArray()) {
                if (!isValidChar(ch, first)) {
                    return false;
                }
                if (first) {
                    first = false;
                }
            }
            return true;
        }
    }

    public static class RadixObjectNamedContext implements NamedContext {

        private final RadixObject object;

        public RadixObjectNamedContext(RadixObject object) {
            this.object = object;
        }

        @Override
        public String getName() {
            if (object != null) {
                return object.getName();
            }
            return null;
        }

        @Override
        public boolean isValidName(String name) {
            return name != null && !name.isEmpty();
        }

        @Override
        public void setName(String name) {
            if (!Objects.equals(name, getName())) {
                if (object != null) {
                    object.setName(name);
                }
            }
        }
    }

    public interface NamedContext {

        String getName();

        void setName(String name);

        boolean isValidName(String name);
    }

    private final JTextField editor = new JTextField();
    private final DocumentListener listener;

    public NameEditorComponent() {
        this(new EditorModel());
    }

    public NameEditorComponent(Alphabet alphabet) {
        this(new EditorModel(alphabet));
    }

    protected NameEditorComponent(EditorModel model) {
        super(model);

        AbstractDocument doc = (AbstractDocument) editor.getDocument();
        DocumentFilter filter = new DocumentFilter() {

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                text = getModel().getValid(text, offset == 0);
                if (text != null) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                string = getModel().getValid(string, offset == 0);
                if (string != null) {
                    super.insertString(fb, offset, string, attr);
                }
            }
        };
        doc.setDocumentFilter(filter);

        listener = new DocumentListener() {

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
    }

    @Override
    public JTextField getEditorComponent() {
        return editor;
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        editor.setEditable(editable);
    }

    @Override
    protected void updateModelValue() {
        getModel().updateValue(editor.getText());
    }

    @Override
    protected void updateEditorComponent() {
        String localValue = getModel().getLocalValue();
        editor.setText(localValue != null ? localValue.toString() : "");
    }

    @Override
    protected void connectEditorComponent() {
        editor.getDocument().addDocumentListener(listener);
    }

    @Override
    protected void disconnectEditorComponent() {
        editor.getDocument().removeDocumentListener(listener);
    }

    @Override
    protected EditorModel getModel() {
        return (EditorModel) super.getModel();
    }
}
