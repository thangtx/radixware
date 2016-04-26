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

package org.radixware.kernel.utils.spellchecker;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.radixware.kernel.common.check.spelling.DictionaryChangeListener;
import org.radixware.kernel.common.check.spelling.EditableDictionary;
import org.radixware.kernel.common.check.spelling.IDictionary;
import org.radixware.kernel.common.check.spelling.IDictionaryDataProvider;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.uds.IRepositoryUdsSegment;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public final class LayerDictionary extends RadixObject implements IDictionary {

    private static final String NAME = "User Dictionary";
    private final EditStateChangeListener listener = new EditStateChangeListener() {

        @Override
        public void onEvent(EditStateChangedEvent e) {
            dictionary.getDataController().setDeallocateEnable(enable());
        }

        boolean enable() {
            return LayerDictionary.this.getEditState() == EEditState.NONE;
        }
    };
    private final EditableDictionary dictionary;

    LayerDictionary(EditableDictionary dictionary, RadixObject container) {
        super(NAME);

        setContainer(container);

        this.dictionary = dictionary;
        this.addEditStateListener(listener);
    }

    public LayerDictionary(EIsoLanguage language, RadixObject container) {
        this(new EditableDictionary(language, null), container);
    }

    public LayerDictionary(EIsoLanguage language, IDictionaryDataProvider provider, RadixObject container) {
        this(new EditableDictionary(language, provider), container);
    }

    /*
     *  RadixObject
     */
    @Override
    public RadixIcon getIcon() {
        return super.getIcon();
    }

    @Override
    public File getFile() {
        Layer layer = getLayer();
        if (layer != null) {
            IRepositoryUdsSegment repository = (IRepositoryUdsSegment) layer.getUds().getRepository();
            if (repository != null) {
                return repository.getLayerDictionaryFile(getLanguage());
            }
        }
        return null;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public void save() throws IOException {
        if (getEditState() != EEditState.NONE) {

            File file = getFile();

            if (file != null && dictionary.save(file)) {
                super.save();
                setEditState(EEditState.NONE);
            }
        }
    }

    /*
     *  IDictionary
     */
    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public boolean containsWord(Word word) {
        return dictionary.containsWord(word);
    }

    @Override
    public boolean containsWord(CharSequence sequence) {
        return dictionary.containsWord(sequence);
    }

    @Override
    public final EIsoLanguage getLanguage() {
        return dictionary.getLanguage();
    }

    @Override
    public int wordCount() {
        return dictionary.wordCount();
    }

    @Override
    public boolean isEmpty() {
        return dictionary.isEmpty();
    }

    @Override
    public Word getWordAt(int index) {
        return dictionary.getWordAt(index);
    }

    @Override
    public boolean addWord(Word word) {
        boolean add = dictionary.addWord(word);
        if (add) {
            setEditState(EEditState.MODIFIED);
        }
        return add;
    }

    @Override
    public boolean removeWord(Word word) {
        boolean remove = dictionary.removeWord(word);
        if (remove) {
            setEditState(EEditState.MODIFIED);
        }
        return remove;
    }

    @Override
    public Iterator<Word> iterator() {
        return dictionary.iterator();
    }

    @Override
    public int getIndexOf(CharSequence sequence) {
        return dictionary.getIndexOf(sequence);
    }

    @Override
    public int getIndexOf(Word word) {
        return dictionary.getIndexOf(word);
    }

    public void setComment(Word word, String comment) {
        dictionary.setComment(word, comment);
    }

    public void setDataProvider(IDictionaryDataProvider provider, boolean reload) {
        dictionary.setDataProvider(provider, reload);
    }

    public String getComment(Word word) {
        return dictionary.getComment(word);
    }

    public boolean setMode(EditableDictionary.EMode mode) {
        return dictionary.setMode(mode);
    }

    public EditableDictionary.EMode getMode() {
        return dictionary.getMode();
    }

    public void removeDictionaryChangeListener(DictionaryChangeListener listener) {
        dictionary.removeDictionaryChangeListener(listener);
    }

    public void addDictionaryChangeListener(DictionaryChangeListener listener) {
        dictionary.addDictionaryChangeListener(listener);
    }

    @Override
    public String toString() {
        RadixObject container = getContainer();
        if (container != null) {
            return dictionary.toString() + " for layer " + container.getName();
        }
        return dictionary.toString() + " for layer";
    }
}
