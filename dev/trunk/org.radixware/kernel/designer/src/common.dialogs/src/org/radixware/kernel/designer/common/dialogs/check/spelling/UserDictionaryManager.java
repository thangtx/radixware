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

package org.radixware.kernel.designer.common.dialogs.check.spelling;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.utils.spellchecker.DictionaryProvider;
import org.radixware.kernel.utils.spellchecker.LayerDictionary;


public class UserDictionaryManager {

    public static boolean addWord(String strWord, String description, Layer layer, EIsoLanguage language) {

        if (strWord == null || strWord.isEmpty() || layer == null) {
            return false;
        }

        AddWordDisplayer displayer = new AddWordDisplayer(strWord, description, layer, language);
        if (displayer.showModal()) {
            final AddWordToUserDictionaryPanel panel = displayer.getPanel();

            layer = panel.getLayer();
            language = panel.getLanguage();
            description = panel.getWordDescription();

            DictionaryProvider provider = DictionaryProvider.getInstance(language);

            if (!provider.existLayerDictionary(layer)) {
                provider.createLayerDictionary(layer);
            }

            LayerDictionary dictionary = provider.getFullLayerDictionary(layer);

            assert dictionary != null : "Fail to load layer dictionary for layer:" + layer.getName();

            if (dictionary != null) {
                Word word = panel.getWord();
                if (dictionary.addWord(word)) {
                    dictionary.setComment(word, description);
                    return true;
                }
            }
        }
        return false;
    }

    public static class AddWordDisplayer extends ModalDisplayer {

        public AddWordDisplayer(String strWord, String description, Layer layer, EIsoLanguage language) {
            super(new AddWordToUserDictionaryPanel(strWord, description, layer, language), "Add new word");

            getComponent().addPropertyChangeListener(AddWordToUserDictionaryPanel.CHANGE_STATE_PROP_NAME, new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    getDialogDescriptor().setValid((Boolean) evt.getNewValue());
                }
            });
        }

        AddWordToUserDictionaryPanel getPanel() {
            return (AddWordToUserDictionaryPanel) getComponent();
        }
    }
}
