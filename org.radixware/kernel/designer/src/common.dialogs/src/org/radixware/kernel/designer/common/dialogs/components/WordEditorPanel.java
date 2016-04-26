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
 * 12/21/11 3:57 PM
 */
package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.designer.common.dialogs.components.values.NameEditorComponent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueEditorPanel;


public final class WordEditorPanel extends ValueEditorPanel<Word> {

    private static class WordContext implements NameEditorComponent.NamedContext {

        String word;

        public WordContext(String word) {
            this.word = word;
        }

        @Override
        public String getName() {
            return word;
        }

        @Override
        public void setName(String name) {
            word = name;
        }

        @Override
        public boolean isValidName(String name) {
            return name != null && !name.trim().isEmpty();
        }

    }

    private final NameEditorComponent editor;

    public WordEditorPanel() {
        super();

        setPreferredSize(new Dimension(100, 27));

        String invalid_chars = " ()*^%#!~`+-=\\|/?,:;\"<>[]{}";
        NameEditorComponent.Alphabet alphabet = new NameEditorComponent.Alphabet("", " ", "", invalid_chars);

        editor = new NameEditorComponent(alphabet);
        editor.open(new WordContext(null));
        editor.addValueChangeListener(new ValueChangeListener<String>() {

            @Override
            public void valueChanged(ValueChangeEvent<String> event) {
                Word oldValue = value;
                value = Word.Factory.newInstance(event.newValue);
                WordEditorPanel.this.fireValueChange(value, oldValue);
            }

        });

        setLayout(new BorderLayout());
        add(editor.getEditorComponent());
    }

    private Word value;

    @Override
    public Word getValue() {
        return value;
    }

    @Override
    public void setValue(Word value) {
        if (isValidValue(value)) {
            this.value = value;
            editor.open(new WordContext(value != null ? value.toString() : ""));
        }
    }

    @Override
    public boolean isSetValue() {
        return super.isSetValue() && editor.isSetValue();
    }

    @Override
    public Word getNullValue() {
        return null;
    }

    @Override
    public boolean isValidValue(Word value) {
        return value == null || editor.isValidValue(value.toString());
    }

    @Override
    public boolean isValueChanged() {
        return editor.isValueChanged();
    }

    @Override
    public void requestFocus() {
        editor.requestFocus();
    }

}
