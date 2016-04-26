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


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.check.spelling.EditableDictionary.EMode;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public final class DictionariesSuite extends RadixObject {

    private EIsoLanguage selLanguage;
    private LayerDictionary dictionary;
    private final EditStateChangeListener listener = new EditStateChangeListener() {

        @Override
        public void onEvent(EditStateChangedEvent e) {
            checkEditState(e.radixObject);
        }
    };

    public DictionariesSuite(Layer layer) {
        super(layer.getName() + " Dictionary");

        setContainer(layer);
        setEditState(EEditState.NONE);

        setSelectedLanguage(layer.getDefaultLanguage());
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public void save() throws IOException {

        if (DictionaryProvider.saveChanges(getLayer())) {
            setEditState(EEditState.NONE);
            super.save();
        }
    }

    public EIsoLanguage getSelectedLanguage() {
        return selLanguage;
    }

    public void setSelectedLanguage(EIsoLanguage selLanguage) {

        if (dictionary != null) {
            dictionary.removeEditStateListener(listener);
        }

        dictionary = getDictionary(selLanguage, getLayer());

        if (dictionary != null) {
            if (dictionary != null) {
                checkEditState(dictionary);
            }
            dictionary.addEditStateListener(listener);

            registrEditingDictionary(dictionary);
        }

        this.selLanguage = selLanguage;
    }

    public LayerDictionary getSelectedDictionary() {
        return dictionary;
    }

    private final Set<LayerDictionary> dictionaries = new HashSet<LayerDictionary>();

    private void registrEditingDictionary(LayerDictionary dictionary) {
        synchronized (dictionaries) {
            dictionaries.add(dictionary);
        }
    }

    public void releaseDictionaries() {
        synchronized (dictionaries) {
            for (LayerDictionary layerDictionary : dictionaries) {
                layerDictionary.setMode(EMode.TRANSIENT);
            }
            dictionaries.clear();
        }
    }

    private LayerDictionary getDictionary(EIsoLanguage selLanguage, Layer layer) {

        DictionaryProvider provider = DictionaryProvider.getInstance(selLanguage);

        if (!provider.existLayerDictionary(layer)) {
            provider.createLayerDictionary(layer);
        }

        return provider.getFullLayerDictionary(layer);
    }

    private void checkEditState(RadixObject object) {
        EEditState editState = object.getEditState();
        if (editState != EEditState.NONE) {
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return RadixWareIcons.DICTIONARY.DICTIONARY;
    }
}
