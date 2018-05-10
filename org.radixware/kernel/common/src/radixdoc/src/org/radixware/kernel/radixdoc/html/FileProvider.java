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
package org.radixware.kernel.radixdoc.html;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.constants.FileNames;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.enums.EIsoLanguage;
import static org.radixware.kernel.radixdoc.html.EFileSource.INTERNAL;
import static org.radixware.kernel.radixdoc.html.EFileSource.PROVIDER;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.ddsdef.ModelDocument;

public abstract class FileProvider {

    private final Map<String, Map<Id, LocalizedString>> loadedBundles = new HashMap<>();

    public abstract InputStream getInputStream(String fileName);

    public abstract Collection<LayerEntry> getLayers();
    
    public abstract ZipInputStream getDocDecorationInputStream(String layerUri);

    protected abstract File getOutput();

    protected abstract boolean isFileExists(String filePath);

    final InputStream getInputStream(EFileSource source, String fileName) {
        switch (source) {
            case INTERNAL:
                return getInternalInputStream(fileName);
            case PROVIDER:
                return getInputStream(fileName);
        }

        return null;
    }

    final InputStream getRadixdocInputStream(ModuleEntry module) throws IOException {
        
        final InputStream inputStream = getInputStream(module.getInputPath());
        if (inputStream == null) { //RADIXMANAGER-365
            return null;
        }
        
        final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        final ZipEntry entry = zipInputStream.getNextEntry();
        
        if (entry == null || entry.isDirectory() || !entry.getName().equals(RadixdocConventions.RADIXDOC_XML_FILE)) {
            return null;
        }

        return zipInputStream;
    }

    public final InputStream getRadixdoc2InputStream(ModuleEntry module) throws IOException {
        if (getInputStream(module.getInputPath()) == null) {
            return null;
        }

        final ZipInputStream inputStream = new ZipInputStream(getInputStream(module.getInputPath()));

        ZipEntry entry = inputStream.getNextEntry();
        while (entry != null && !entry.getName().equals(RadixdocConventions.RADIXDOC_2_XML_FILE)) {
            entry = inputStream.getNextEntry();
        }

        if (entry == null || entry.isDirectory()) {
            return null;
        }

        return inputStream;
    }
    
    public final InputStream getLayerXmlInputStream(String layerUri) {
        return getInputStream(layerUri + "/layer.xml");
    }
    
    public final LayerEntry getLayer(String uri) {
        for (LayerEntry layer : getLayers()) {
            if (uri.equals(layer.getIdentifier())) {
                return layer;
            }
        }
        
        return null;
    }

    InputStream getInternalInputStream(String fileName) {
        return LocalFileProvider.class.getResourceAsStream(fileName);
    }

    InputStream getXsltSchemeStream() {
        return getInternalInputStream(Constants.RADIX_DOC_TRANSFORM_XLS_PATH);
    }

    public Map<Id, LocalizedString> getLocalizedBundleStrings(String modulePath, String bundleId, EIsoLanguage language) {
        final String bundleKey = getBundleKey(modulePath, bundleId, language);        
        Map<Id, LocalizedString> map;
        if (loadedBundles.containsKey(bundleKey)) {
            map = loadedBundles.get(bundleKey);
        } else {
            map = readLocalizedBundleStrings(modulePath, bundleId, language);
            loadedBundles.put(bundleKey, map);
        }
        return map;
    }

    private String getBundleKey(String modulePath, String bundleId, EIsoLanguage language) {
        return modulePath + "~" + bundleId + "~" + language;
    }

    private Map<Id, LocalizedString> readLocalizedBundleStrings(String modulePath, String bundleId, EIsoLanguage language) {
        if (modulePath.contains("/dds/")) {
            ModelDocument modelDocument = null;
            final String modifiedModel = modulePath + File.separator + FileNames.DDS_MODEL_MODIFIED_XML;
            if (isFileExists(modifiedModel)) {
                try (final InputStream inputStream = getInputStream(modifiedModel)) {
                    if (inputStream != null) {
                        modelDocument = ModelDocument.Factory.parse(inputStream);
                    }
                } catch (XmlException | IOException ex) {
                    Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.WARNING, "Can not parse " + bundleId + " in " + FileNames.DDS_MODEL_MODIFIED_XML, ex);
                    modelDocument = null;
                }

                if (modelDocument != null) {
                    ModelDocument.Model model = modelDocument.getModel();
                    if (model.isSetStringBundle()) {
                        return stringListToMap(model.getStringBundle().getStringList());
                    }
                    return null;
                }
            }
        }

        final AdsDefinitionDocument definitionDocument;
        try (final InputStream inputStream = getLocalizedBandle(modulePath, bundleId, language)) {
            if (inputStream != null) {
                definitionDocument = AdsDefinitionDocument.Factory.parse(inputStream);
                final AdsDefinitionElementType element = definitionDocument.getAdsDefinition();
                if (element.isSetAdsLocalizingBundleDefinition()) {
                    return stringListToMap(element.getAdsLocalizingBundleDefinition().getStringList());
                }
            }
        } catch (XmlException | IOException ex) {
            Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.WARNING, "Can not parse " + bundleId, ex);
        }
        return null;
    }

    private final Map<Id, LocalizedString> stringListToMap(final List<LocalizedString> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyMap();
        }
        final Map<Id, LocalizedString> map = new HashMap<>();
        for (LocalizedString str : list) {
            map.put(str.getId(), str);
        }
        return map;
    }

    /**
     * Get Localized Bundle defined as a AdsDefinitionDocument
     *
     */
    public InputStream getLocalizedBandle(String modulePath, String bundleId, EIsoLanguage language) {
        final String path;
        if (modulePath.contains("/dds/")) {
            path = modulePath + "/locale/" + language.getValue() + ".xml";
        } else {
            path = modulePath + "/locale/" + language.getValue() + "/" + bundleId + ".xml";
        }

        final InputStream inputStream = getInputForBundle(path);
        if (inputStream != null) {
            return inputStream;
        }

        return loadFromJarLocalizedBandle(modulePath, bundleId, language);
    }

    InputStream loadFromJarLocalizedBandle(String modulePath, String bundleId, EIsoLanguage language) {
        final String fileName = "locale-" + language.getValue() + ".jar";
        StringBuilder sb = new StringBuilder(modulePath);
        sb.append("/bin/");
        sb.append(fileName);
        String path = sb.toString();
        InputStream inputStream = getInputForBundle(path);

        if (inputStream == null) {
            return null;
        }

        try {
            final JarInputStream stream = new JarInputStream(inputStream);
            JarEntry entry = stream.getNextJarEntry();

            while (entry != null) {
                if (entry.getName().length() <= 4) {
                    continue;
                }

                final int index = entry.getName().lastIndexOf('/');
                if (index + 1 >= entry.getName().length() - 4) {
                    continue;
                }
                final String id = entry.getName().substring(index + 1, entry.getName().length() - 4);

                if (Objects.equals(id, bundleId)) {
                    return stream;
                }

                entry = stream.getNextJarEntry();
            }
        } catch (IOException e) {
            return null;
        }

        return null;
    }

    protected InputStream getInputForBundle(String bundlePath) {
        return getInputStream(bundlePath);
    }

    public void clearCache() {
    }

    public static abstract class Entry {

        private String id;
        private Entry parent;

        public Entry(String identifier) {
            this.id = identifier;
        }

        public final String getIdentifier() {
            return id;
        }

        final Entry getParentEntry() {
            return parent;
        }

        void setParent(Entry parent) {
            this.parent = parent;
        }

        boolean isProcessable() {
            return true;
        }

        abstract String getOutputPath();

        abstract String getInputPath();

        abstract String getRootPath();

        abstract String getSubPath();
    }

    public static final class LayerEntry extends Entry {

        private final int radixdocFormatVersion;
        private final List<ModuleEntry> modules;
        private final Map<EIsoLanguage, String> localizingLayers = new HashMap<>();
        private final String rootPath;

        public LayerEntry(String rootPath, String uri, int radixdocFormatVersion, List<ModuleEntry> modules) {
            super(uri);
            this.radixdocFormatVersion = radixdocFormatVersion;
            this.modules = new ArrayList<>(modules.size());

            for (final ModuleEntry module : modules) {
                addModule(module);
            }
            this.rootPath = rootPath;
        }

        public LayerEntry(String rootPath, String uri, int radixdocFormatVersion) {
            super(uri);
            this.modules = new ArrayList<>();
            this.radixdocFormatVersion = radixdocFormatVersion;
            this.rootPath = rootPath;
        }

        @Override
        protected String getRootPath() {
            return rootPath;
        }

        @Override
        String getInputPath() {
            return null;
        }

        @Override
        boolean isProcessable() {
            return false;
        }

        @Override
        String getSubPath() {
            return getIdentifier();
        }

        public void addModule(ModuleEntry module) {
            modules.add(module);
            module.setParent(this);
        }

        public Collection<ModuleEntry> getModules() {
            return Collections.unmodifiableCollection(modules);
        }

        public int getRadixdocFormatVersion() {
            return radixdocFormatVersion;
        }

        @Override
        String getOutputPath() {
            return getSubPath();
        }
        
        public void addLocalizingLayer(String layerUri, EIsoLanguage localizationLanguage) {
            if (!localizingLayers.keySet().contains(localizationLanguage)) {
                localizingLayers.put(localizationLanguage, layerUri);
            }
        }
        
        public boolean hasLocalizingLayer(EIsoLanguage localizationLanguage) {
            return localizingLayers.keySet().contains(localizationLanguage);
        }
        
        public String getLocalizingLayer(EIsoLanguage localizationLanguage) {
            return localizingLayers.get(localizationLanguage);
        }
    }

    public static final class ModuleEntry extends Entry {

        private final String segmentName;

        public ModuleEntry(String segmentName, String moduleName) {
            super(moduleName);
            this.segmentName = segmentName;
        }

        @Override
        public String getRootPath() {
            return getParentEntry().getRootPath() + "/" + getSubPath();
        }

        @Override
        public String getInputPath() {
            return getRootPath() + "/" + RadixdocConventions.RADIXDOC_ZIP_FILE;
        }

        @Override
        String getSubPath() {
            return segmentName + "/" + getIdentifier();
        }

        @Override
        String getOutputPath() {
            return getParentEntry().getOutputPath() + "/" + getSubPath();
        }
    }

}
