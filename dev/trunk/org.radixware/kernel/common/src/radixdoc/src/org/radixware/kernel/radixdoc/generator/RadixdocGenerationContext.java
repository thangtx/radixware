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
package org.radixware.kernel.radixdoc.generator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.components.IProgressHandle;
import org.radixware.kernel.common.defs.ads.doc.DocTopicBody;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.mml.Mml;
import org.radixware.kernel.common.radixdoc.IDocLogger;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.radixdoc.html.FileProvider;
import org.radixware.kernel.radixdoc.html.HtmlRadixdocGenerator;
import org.radixware.kernel.radixdoc.html.TmpFileInputStreamWrapper;
import org.radixware.kernel.radixdoc.html.TransformLib;
import org.radixware.kernel.radixdoc.xmlexport.DefinitionDocInfo;
import org.radixware.schemas.adsdef.DocumentationTopicBody;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.radixdoc.AbstractDocItem;
import org.radixware.schemas.radixdoc.AdsDefDocItem;
import org.radixware.schemas.radixdoc.DocumentationItems;
import org.radixware.schemas.radixdoc.ModuleDocItem;
import org.radixware.schemas.radixdoc.ModuleDocumentationItemsDocument;

public abstract class RadixdocGenerationContext {

    protected final FileProvider fileProvider;

    protected final String topLayerUri;

    protected final List<String> topLayerHierarhy = new LinkedList<>();

    private final Map<String, org.radixware.schemas.product.Layer> layer2LayerXml = new HashMap();

    protected Map<DocInfoKey, DefinitionDocInfo> defDocInfoCache;
    protected Map<EIsoLanguage, Map<String, Mml>> mmlMapCache;
    protected File topicBodyZip;

    public RadixdocGenerationContext(FileProvider fileProvider, String topLayerUri) { // add topLayerUri in constructor
        this.fileProvider = fileProvider;
        this.topLayerUri = topLayerUri;
        createTopLayerHierarhy(topLayerUri);
    }
    
     public RadixdocGenerationContext(FileProvider fileProvider, List<String> layerList) { // add topLayerUri in constructor
        this.fileProvider = fileProvider;
        this.topLayerUri = null;
        topLayerHierarhy.addAll(layerList);
    }

    public abstract DefinitionDocInfo getDefinitionDocInfo(final Id definitionId);

    public abstract Mml getMml(final Id definitionId, final EIsoLanguage lang);

    public abstract IDocLogger getDocLogger();

    public abstract IProgressHandle getProgressHandler();

    public abstract ICancellable getCancellable();

    protected abstract void addModuleInfo(ModuleDocItem moduleDocItem);

    protected boolean prepareDefDocInfoCache() {
        try (ZipInputStream radixdocXmlStream = getRadixdocXmlInputStream()) {
            defDocInfoCache = new HashMap<>();

            while ((radixdocXmlStream.getNextEntry()) != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                FileUtils.copyStream(radixdocXmlStream, baos);

                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

                ModuleDocumentationItemsDocument moduleDocItemsDocument = ModuleDocumentationItemsDocument.Factory.parse(bais);
                ModuleDocItem moduleDocItem = moduleDocItemsDocument.getModuleDocumentationItems();
                addModuleInfo(moduleDocItem);

                DocumentationItems docItems = moduleDocItem.getDocumentationItems();

                for (AbstractDocItem item : docItems.getDocumentationItemList()) {
                    if (item instanceof AdsDefDocItem) {
                        AdsDefDocItem defItem = (AdsDefDocItem) item;

                        defDocInfoCache.put(new DocInfoKey(defItem.getId(), moduleDocItem.getLayerUri()),
                                new DefinitionDocInfo(
                                        moduleDocItem.getId(),
                                        moduleDocItem.getName(),
                                        moduleDocItem.getModulePath(),
                                        moduleDocItem.getLayerUri(),
                                        getLayerVersion(moduleDocItem.getLayerUri()),
                                        getLayerName(moduleDocItem.getLayerUri()),
                                        defItem
                                ));
                    }
                }
            }
        } catch (XmlException | IOException ex) {
            Logger.getLogger(RadixdocGenerationContext.class.getName()).log(Level.SEVERE, null, ex);
            if (getDocLogger() != null) {
                getDocLogger().put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex));
            }

            return false;
        }
        return true;
    }

    protected boolean prepareTopicBodyCache(EIsoLanguage lang) {

        try (ZipInputStream topicBodyZIS = getRadixdocTopicBodyInputStream(lang)) {
            if (topicBodyZIS == null) {
                return false;
            }

            if (mmlMapCache == null) {
                mmlMapCache = new HashMap<>();
            }

            Map<String, Mml> mmlMap = new HashMap<String, Mml>();
            mmlMapCache.put(lang, mmlMap);

            ZipEntry entry = topicBodyZIS.getNextEntry();
            while (entry != null) {
                ZipFile topicBodyZipZ = new ZipFile(topicBodyZip);
                InputStream is = topicBodyZipZ.getInputStream(entry);
                DocumentationTopicBody document = DocumentationTopicBody.Factory.parse(is);

                Mml mml = Mml.Factory.loadFrom(null, document, "Mml");
                mmlMap.put(entry.getName(), mml);

                entry = topicBodyZIS.getNextEntry();
            }
        } catch (XmlException | IOException ex) {
            Logger.getLogger(RadixdocGenerationContext.class.getName()).log(Level.SEVERE, null, ex);
            if (getDocLogger() != null) {
                getDocLogger().put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex));
            }
            return false;
        }

        return true;
    }

    public LocalizedString getLocalizedStringByDefId(final Id defId, final Id bunldeId, final Id stringId, final EIsoLanguage lang) {
        DefinitionDocInfo docInfo = getDefinitionDocInfo(defId);
        if (docInfo == null) {
            return null;
        }

        if (docInfo.getDocItem() instanceof AdsDefDocItem) {
            for (String layer : topLayerHierarhy) {
                docInfo = defDocInfoCache.get(new DocInfoKey(defId, layer));
                if (docInfo == null) {
                    continue;
                }
                AdsDefDocItem docItem = (AdsDefDocItem) docInfo.getDocItem();

                String modulePath = docInfo.getModulePath().contains("\\") ? docInfo.getModulePath().replace("\\", "/") : docInfo.getModulePath();
                Map<Id, LocalizedString> localizedStrings = fileProvider.getLocalizedBundleStrings(modulePath, bunldeId.toString(), lang);
                localizedStrings = localizedStrings == null ? getLocalizedStringFromLocalizingLayer(modulePath, bunldeId, lang) : localizedStrings;
                if (localizedStrings != null && !localizedStrings.isEmpty()) {
                    LocalizedString string = localizedStrings.get(stringId);
                    if (string != null) {
                        return string;
                    }
                }
                if (!docItem.isSetIsOverwrite()) {
                    break;
                }
            }
        } else {
            String modulePath = docInfo.getModulePath().contains("\\") ? docInfo.getModulePath().replace("\\", "/") : docInfo.getModulePath();
            Map<Id, LocalizedString> localizedStrings = fileProvider.getLocalizedBundleStrings(modulePath, bunldeId.toString(), lang);
            localizedStrings = localizedStrings == null ? getLocalizedStringFromLocalizingLayer(modulePath, bunldeId, lang) : localizedStrings;
            if (localizedStrings != null && !localizedStrings.isEmpty()) {
                LocalizedString string = localizedStrings.get(stringId);
                return string;
            }
        }

        return null;
    }

    public LocalizedString getLocalizedStringByModulePath(final String modulePath, final Id bunldeId, final Id stringId, final EIsoLanguage lang) {
        Map<Id, LocalizedString> localizedStrings = fileProvider.getLocalizedBundleStrings(modulePath, bunldeId.toString(), lang);
        localizedStrings = localizedStrings == null ? getLocalizedStringFromLocalizingLayer(modulePath, bunldeId, lang) : localizedStrings;
        if (localizedStrings != null && !localizedStrings.isEmpty()) {
            LocalizedString string = localizedStrings.get(stringId);
            return string;
        }

        return null;
    }

    private Map<Id, LocalizedString> getLocalizedStringFromLocalizingLayer(String modulePath, Id bundleId, EIsoLanguage language) {
        String[] entryParts = modulePath.replace("\\", "/").split("/");

        if (entryParts.length > 0) {
            String uri = entryParts[0];
            FileProvider.LayerEntry entryLayer = fileProvider.getLayer(uri);
            if (entryLayer != null && entryLayer.hasLocalizingLayer(language)) {
                entryParts[0] = entryLayer.getLocalizingLayer(language);
                String newEntry = TransformLib.constructEntry(entryParts);

                Map<Id, LocalizedString> strings = fileProvider.getLocalizedBundleStrings(newEntry, bundleId.toString(), language);
                return strings;
            }
        }

        return null;
    }

    public ZipInputStream getDecorationsStream(String layerUri) {
        return fileProvider.getDocDecorationInputStream(layerUri);
    }

    protected ZipInputStream getRadixdocXmlInputStream() {
        try {
            File radixDocXmlTmpFile = File.createTempFile("radixdoc2", ".zip");

            FileOutputStream fos = new FileOutputStream(radixDocXmlTmpFile);
            try (ZipOutputStream zos = new ZipOutputStream(fos)) {
                for (final FileProvider.LayerEntry layer : fileProvider.getLayers()) {
                    if (!topLayerHierarhy.contains(layer.getIdentifier())) {
                        continue;
                    }
                    modules_loop:
                    for (final FileProvider.ModuleEntry module : layer.getModules()) {
                        try (final InputStream inputStream = fileProvider.getRadixdoc2InputStream(module)) {
                            if (inputStream == null) {
                                continue;
                            }

                            final ModuleDocumentationItemsDocument document = ModuleDocumentationItemsDocument.Factory.parse(inputStream);
                            if (document != null) {
                                ZipEntry entry = new ZipEntry(module.getRootPath().replace("/", "-") + ".xsd");
                                zos.putNextEntry(entry);
                                document.save(zos);
                            }
                        } catch (XmlException | IOException ex) {
                            Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);

                            if (getDocLogger() != null) {
                                getDocLogger().put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex));
                            }

                            return null;
                        }
                    }
                }

                ZipInputStream radixdocXmlStream = new ZipInputStream(new TmpFileInputStreamWrapper(radixDocXmlTmpFile));

                return radixdocXmlStream;
            }
        } catch (IOException ex) {
            if (getDocLogger() != null) {
                getDocLogger().put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex));
            }
            return null;
        }
    }

    public File copyResources(File targetDir, EIsoLanguage lang) {
        for (final FileProvider.LayerEntry layer : fileProvider.getLayers()) {
            for (final FileProvider.ModuleEntry module : layer.getModules()) {

                InputStream radixDocZipIS = fileProvider.getInputStream(module.getInputPath());
                if (radixDocZipIS == null) {
                    continue;
                }

                try (final ZipInputStream radixDocZipZIS = new ZipInputStream(radixDocZipIS)) {
                    if (radixDocZipZIS == null) {
                        continue;
                    }
                    ZipEntry entryRadixDocZip = radixDocZipZIS.getNextEntry();

                    while (entryRadixDocZip != null) {
                        // TODO: !!! ignore lang
                        if (entryRadixDocZip.getName().startsWith("resources/") && !entryRadixDocZip.getName().endsWith("/")) {
                            File exportDocItemFile = new File(targetDir, entryRadixDocZip.getName());
                            if (!exportDocItemFile.getParentFile().exists()) {
                                exportDocItemFile.getParentFile().mkdirs();
                            }
                            try (FileOutputStream tmpFos = new FileOutputStream(exportDocItemFile)) {
                                FileUtils.copyStream(radixDocZipZIS, tmpFos);
                            }
                        }
                        entryRadixDocZip = radixDocZipZIS.getNextEntry();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);
                    if (getDocLogger() != null) {
                        getDocLogger().put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex));
                    }
                }
            }
        }

        return new File(targetDir, "resources");
    }

    protected ZipInputStream getRadixdocTopicBodyInputStream(EIsoLanguage lang) {
        try {
            topicBodyZip = File.createTempFile(lang.getValue() + "-radixdocTopicBody", ".zip");

            FileOutputStream fos = new FileOutputStream(topicBodyZip);
            try (ZipOutputStream zos = new ZipOutputStream(fos)) {
                for (final FileProvider.LayerEntry layer : fileProvider.getLayers()) {
                    for (final FileProvider.ModuleEntry module : layer.getModules()) {

                        InputStream radixDocZipIS = fileProvider.getInputStream(module.getInputPath());
                        if (radixDocZipIS == null) {
                            continue;
                        }
                        try (final ZipInputStream radixDocZipZIS = new ZipInputStream(radixDocZipIS)) {
                            if (radixDocZipZIS == null) {
                                continue;
                            }

                            ZipEntry entryRadixDocZip = radixDocZipZIS.getNextEntry();
                            while (entryRadixDocZip != null) {

                                if (entryRadixDocZip.getName().startsWith("body/" + lang.getValue() + "/" + DocTopicBody.FILE_FULL_PREFIX)) {

                                    // copy
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    byte[] buffer = new byte[1024];
                                    int len;
                                    while ((len = radixDocZipZIS.read(buffer)) > -1) {
                                        baos.write(buffer, 0, len);
                                    }
                                    baos.flush();
                                    InputStream is = new ByteArrayInputStream(baos.toByteArray());
                                    DocumentationTopicBody document = DocumentationTopicBody.Factory.parse(is);

                                    // save doc
                                    if (document != null) {
                                        ZipEntry entryTopicBodyZip = new ZipEntry(entryRadixDocZip.getName());
                                        zos.putNextEntry(entryTopicBodyZip);
                                        document.save(zos);
                                    }
                                }

                                entryRadixDocZip = radixDocZipZIS.getNextEntry();
                            }

                        } catch (XmlException | IOException ex) {
                            Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);
                            if (getDocLogger() != null) {
                                getDocLogger().put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex));
                            }
                            return null;
                        }
                    }
                }

                ZipInputStream topicBodyZIS = new ZipInputStream(new TmpFileInputStreamWrapper(topicBodyZip));
                return topicBodyZIS;
            }
        } catch (IOException ex) {
            if (getDocLogger() != null) {
                getDocLogger().put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex));
            }
            return null;
        }
    }

    private org.radixware.schemas.product.Layer getLayerXml(final String layerUri) {
        if (layer2LayerXml.containsKey(layerUri)) {
            return layer2LayerXml.get(layerUri);
        }

        org.radixware.schemas.product.Layer result = null;
        try (InputStream layerXmlStream = fileProvider.getLayerXmlInputStream(layerUri)) {
            org.radixware.schemas.product.LayerDocument xLayerDocument = org.radixware.schemas.product.LayerDocument.Factory.parse(layerXmlStream);
            result = xLayerDocument.getLayer();
        } catch (IOException | XmlException ex) {
            if (getDocLogger() != null) {
                getDocLogger().put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex));
            }
        }
        layer2LayerXml.put(layerUri, result);
        return result;
    }

    protected String getLayerVersion(final String layerUri) {
        String result = "";
        org.radixware.schemas.product.Layer xLayer = getLayerXml(layerUri);
        if (xLayer != null) {
            if (!Utils.emptyOrNull(xLayer.getReleaseNumber())) {
                result = xLayer.getReleaseNumber();
            }
        }
        return result;
    }

    protected String getLayerName(final String layerUri) {
        String result = "";
        org.radixware.schemas.product.Layer xLayer = getLayerXml(layerUri);
        if (xLayer != null) {
            if (!Utils.emptyOrNull(xLayer.getName())) {
                result = xLayer.getName();
            }
        }
        return result;
    }

    private void createTopLayerHierarhy(final String layer) {
        Queue<String> layersQueue = new LinkedList<>();
        layersQueue.add(layer);

        while (!layersQueue.isEmpty()) {
            String currentLayer = layersQueue.poll();
            if (!topLayerHierarhy.contains(currentLayer)) {
                topLayerHierarhy.add(currentLayer);
            }

            org.radixware.schemas.product.Layer layerXml = getLayerXml(currentLayer);
            List<String> baseLayers = layerXml == null ? null : layerXml.getBaseLayerURIs();
            String prevLayer = layerXml == null ? null : layerXml.getPrevLayerUri();

            if (baseLayers != null) {
                layersQueue.addAll(baseLayers);
            } else if (!Utils.emptyOrNull(prevLayer)) {
                layersQueue.add(prevLayer);
            }
        }
    }

    protected DefinitionDocInfo getDefDocInfoFromCache(final Id definitionId, boolean isNeedOverridenInfo) {
        DefinitionDocInfo result = null;
        for (String layerUri : topLayerHierarhy) {
            DocInfoKey candidateKey = new DocInfoKey(definitionId, layerUri);
            DefinitionDocInfo candidate = defDocInfoCache.get(candidateKey);

            if (candidate != null) {
                result = candidate;
                if (isNeedOverridenInfo) {
                    break;
                }
            }
        }

        return result;
    }
}
