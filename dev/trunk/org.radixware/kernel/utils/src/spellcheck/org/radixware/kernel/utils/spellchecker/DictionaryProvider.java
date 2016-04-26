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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.check.spelling.CommonDictionary;
import org.radixware.kernel.common.check.spelling.EditableDictionary;
import org.radixware.kernel.common.check.spelling.IDictionaryDataProvider;
import org.radixware.kernel.common.check.spelling.IDictionaryProvider;
import org.radixware.kernel.common.check.spelling.IDictionarySet;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Layer;


public class DictionaryProvider implements IDictionaryProvider {

    private static final Map<EIsoLanguage, DictionaryProvider> providers = new EnumMap<EIsoLanguage, DictionaryProvider>(EIsoLanguage.class);
    private static final DictionaryProvider DEFAULT_DICTIONARY_PROVIDER = new DictionaryProvider(null);

    public static DictionaryProvider getDefaultInstance() {
        return DEFAULT_DICTIONARY_PROVIDER;
    }

    public static DictionaryProvider getInstance(EIsoLanguage language) {

        if (language == null) {
            return getDefaultInstance();
        }

        DictionaryProvider provider;
        synchronized (providers) {
            provider = providers.get(language);

            if (provider == null) {
                provider = new DictionaryProvider(language);
                providers.put(language, provider);
            }
        }
        return provider;
    }

    public static boolean saveChanges(Layer context) {

        assert context != null;
        
        if (context == null) {
            return false;
        }
        String layerUri = context.getURI();

        if (!DEFAULT_DICTIONARY_PROVIDER.save(layerUri)) {
            return false;
        }

        synchronized (providers) {
            for (DictionaryProvider provider : providers.values()) {
                if (!provider.save(layerUri)) {
                    return false;
                }
            }
        }
        return true;
    }
    private EIsoLanguage language;
    private CommonDictionary commonDictionary;
    private final Map<String, LayerDictionary> layerDictionaryMap = new HashMap<String, LayerDictionary>();

    private DictionaryProvider(EIsoLanguage language) {
        this.language = language;
    }

    @Override
    public CommonDictionary getCommonDictionary() {
        synchronized (this) {
            if (commonDictionary == null) {
                commonDictionary = DictionaryLoader.loadCommonDictionary(language);
            }
        }
        return commonDictionary;
    }

    @Override
    public EIsoLanguage getLanguage() {
        return language;
    }

    @Override
    public LayerDictionary getLayerDictionary(RadixObject context) {

        LayerDictionary dictionary = null;
        Layer layer = getLayer(context);

        if (layer != null) {

            String layerUri = layer.getURI();

            synchronized (layerDictionaryMap) {
                dictionary = layerDictionaryMap.get(layerUri);

                if (dictionary == null) {
                    dictionary = DictionaryLoader.loadLayerDictionary(language, layer);
                    if (dictionary != null) {
                        layerDictionaryMap.put(layerUri, dictionary);
                    }
                } else if (dictionary.getMode() == EditableDictionary.EMode.TRANSIENT) {
                    IDictionaryDataProvider dataProvider = DictionaryLoader.getLayerDictionaryDataProvider(language, layer);

                    if (dataProvider != null) {
                        dictionary.setMode(EditableDictionary.EMode.CHECKING);
                        dictionary.setDataProvider(dataProvider, false);
                    }
                }
            }
        }

        return dictionary;
    }

    @Override
    public IDictionarySet getDictionarySet(RadixObject context) {
        return new DictionarySet(this, context);
    }

    public LayerDictionary getFullLayerDictionary(RadixObject context) {

        LayerDictionary dictionary = null;
        Layer layer = getLayer(context);

        if (layer != null) {

            String layerUri = layer.getURI();

            synchronized (layerDictionaryMap) {
                dictionary = layerDictionaryMap.get(layerUri);
            }

            if (dictionary == null) {
                dictionary = DictionaryLoader.fullLoadLayerDictionary(language, layer);
                if (dictionary != null) {
                    synchronized (layerDictionaryMap) {
                        layerDictionaryMap.put(layerUri, dictionary);
                    }
                }
            } else {
                if (dictionary.getMode() == EditableDictionary.EMode.CHECKING) {
                    IDictionaryDataProvider dataProvider = DictionaryLoader.getLayerDictionaryFullDataProvider(language, layer);

                    if (dataProvider != null) {
                        dictionary.setMode(EditableDictionary.EMode.EDITING);
                        dictionary.setDataProvider(dataProvider, true);
                    }
                }
            }
        }

        return dictionary;
    }

    public boolean existLayerDictionary(Layer layer) {
        assert layer != null : "Layer not specified";

        if (layer == null) {
            return false;
        }
        String layerUri = layer.getURI();
        synchronized (layerDictionaryMap) {
            if (layerDictionaryMap.containsKey(layerUri)) {
                return true;
            }
            LayerDictionary dictionary = DictionaryLoader.loadLayerDictionary(language, layer);
            if (dictionary != null) {
                layerDictionaryMap.put(layerUri, dictionary);
                return true;
            }
        }
        return false;
    }

    public LayerDictionary createLayerDictionary(Layer layer) {
        if (layer == null) {
            throw new NullPointerException("Layer not specified");
        }
        if (existLayerDictionary(layer)) {
            throw new IllegalStateException("Layer dictionary already exist");
        }
        LayerDictionary dictionary = new LayerDictionary(language, layer);

        synchronized (layerDictionaryMap) {
            layerDictionaryMap.put(layer.getURI(), dictionary);
        }

        return dictionary;
    }

    private boolean save(String layerUri) {

        final LayerDictionary dictionary;
        synchronized (layerDictionaryMap) {
            dictionary = layerDictionaryMap.get(layerUri);
        }

        if (dictionary != null) {
            try {
                dictionary.save();
                return dictionary.getEditState() == RadixObject.EEditState.NONE;
            } catch (IOException ex) {
                return false;
            }
        }
        return true;
    }

    private static Layer getLayer(RadixObject context) {
        assert context != null : "Context is null";

        if (context != null) {
            return context.getLayer();
        }
        return null;
    }
}
