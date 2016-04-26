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

package org.radixware.kernel.common.check.spelling;

import java.util.Iterator;
import org.radixware.kernel.common.enums.EIsoLanguage;


abstract class AbstractDictionary implements IDictionary {

    private final EIsoLanguage language;
    private final boolean editable;
    protected final DataController controller;

    protected AbstractDictionary(EIsoLanguage language, boolean editable, DataController controller) {
        this.language = language;
        this.editable = editable;

        if (controller == null) {
            throw new NullPointerException("DataController is null");
        }

        this.controller = controller;
    }

    @Override
    public final boolean isEditable() {
        return editable;
    }

    @Override
    public final EIsoLanguage getLanguage() {
        return language;
    }

    @Override
    public boolean containsWord(Word word) {
        return getIndexOf(word) != -1;
    }

    @Override
    public boolean containsWord(CharSequence sequence) {
        if (sequence == null || sequence.length() == 0 || isEmpty()) {
            return false;
        }

        Word searchWord = Word.Factory.newInstance(sequence);
        return containsWord(searchWord);
    }

    @Override
    public boolean isEmpty() {
        return controller.getInitCollection().wordCount() == 0;
    }

    @Override
    public int wordCount() {
        return controller.getInitCollection().wordCount();
    }

    @Override
    public Word getWordAt(int index) {
        return controller.getInitCollection().getWordAt(index);
    }

    @Override
    public Iterator<Word> iterator() {
        return controller.getInitCollection().getWordIterator();
    }

    @Override
    public int getIndexOf(Word word) {
        return controller.getInitCollection().getIndexOf(word);
    }

    @Override
    public int getIndexOf(CharSequence sequence) {
        if (sequence == null || sequence.length() == 0 || isEmpty()) {
            return -1;
        }

        return getIndexOf(Word.Factory.newInstance(sequence));
    }

    public DataController getDataController() {
        return controller;
    }

    @Override
    public String toString() {
        final String str = getLanguage() != null ? getLanguage().getName() : "Common";
        return str + (isEditable() ? " editable dictionary" : "dictionary");
    }
}