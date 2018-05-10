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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import static java.util.zip.ZipEntry.DEFLATED;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.RadixdocStopParseException;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class SvnFileProvider extends FileProvider {

    protected abstract SVNRepositoryAdapter getSvnRepository();

    protected abstract String[] getLayerPaths();

    protected abstract long getRevision();

    @Override
    public final InputStream getInputStream(String fileName) {

        try {
            final byte[] file = SVN.isFileExists(getSvnRepository(), fileName, getRevision()) ?
                          SVN.getFile(getSvnRepository(), fileName, getRevision())
                    : null;
            if (file != null) {
                return new ByteArrayInputStream(file);
            }
        } catch (RadixSvnException ex) {
            throw new RuntimeException(ex);
        }

        return null;
    }

    @Override
    public ZipInputStream getDocDecorationInputStream(String layerUri) {
        Collection<LayerEntry> layerz = getLayers();
        for (LayerEntry layer : layerz) {
            if (layerUri.equals(layer.getIdentifier())) {
                try {
                    String decorPath = SvnPath.append(layer.getRootPath(), RadixdocConventions.RADIXDOC_DECORATIONS_PATH);
                    if  (SVN.isExists(getSvnRepository(), decorPath, getRevision())) {
                        final List<String> decorFilesList = new ArrayList<>();

                        getSvnRepository().getDir(decorPath, getRevision(), new SVNRepositoryAdapter.EntryHandler() {

                            @Override
                            public void accept(SvnEntry entry) throws RadixSvnException {
                                if (entry.getKind() == SvnEntry.Kind.FILE) {
                                    decorFilesList.add(entry.getName());
                                }
                            }
                        });

                        if (!decorFilesList.isEmpty()) {
                            final int random = (int) (1000000000 * Math.random());
                            File tmp = File.createTempFile("docDec" + random, ".zip");
                            FileOutputStream fos = new FileOutputStream(tmp);
                            ZipOutputStream zos = new ZipOutputStream(fos);

                            for (String fileName : decorFilesList) {
                                String filePath = SvnPath.append(decorPath, fileName);
                                if (SVN.isFileExists(getSvnRepository(), filePath)) {
                                    final byte[] fileBytes = SVN.getFile(getSvnRepository(), filePath, getRevision());

                                   zos.putNextEntry(new ZipEntry(fileName){{setMethod(DEFLATED);}});

                                    try (ByteArrayInputStream tmpBaos = new ByteArrayInputStream(fileBytes)) {
                                        FileUtils.copyStream(tmpBaos, zos);
                                    }
                                }
                            }

                            zos.flush();
                            zos.close();
                            ZipInputStream result = new ZipInputStream(new TmpFileInputStreamWrapper(tmp));

                            return result;
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(LocalFileProvider.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    private List<LayerEntry> layers;
    private final Object lock = new Object();
    private File tempDir;

    @Override
    public Collection<LayerEntry> getLayers() {
        synchronized (lock) {
            if (layers == null) {
                layers = new ArrayList<>();

                findModules();
            }

            return layers;
        }
    }

    private void findModules() {
        List<LayerInfo> localizingLayers = new ArrayList<>();
        Map<String, LayerEntry> layerEntries = new HashMap<>();

        for (final String layer : getLayerPaths()) {

            final List<ModuleEntry> modules = new ArrayList<>();
            try {
                collectModules(layer, "ads", modules);
                collectModules(layer, "dds", modules);
            } catch (RadixSvnException ex) {
                Logger.getLogger(SvnFileProvider.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (!modules.isEmpty()) {
                final String layerXml = SvnPath.append(layer, "layer.xml");
                if (isFileExists(layerXml)) {

                    final LayerInfo layerInfo = parseLayerXml(layerXml);
                    if (layerInfo != null) {
                        if (!layerInfo.isLocalizing) {
                            layerEntries.put(layerInfo.uri, new LayerEntry(layer, layerInfo.uri, layerInfo.radixdocFormatVersion, modules));
                        } else {
                            localizingLayers.add(layerInfo);
                        }
                    }
                }
            }
        }

        for (LayerInfo layerInfo : localizingLayers) {
            if (!layerInfo.baseLayerURIs.isEmpty()) {
                FileProvider.LayerEntry layerEntry = layerEntries.get(layerInfo.baseLayerURIs.get(0));
                if (!layerInfo.languages.isEmpty()) {
                    layerEntry.addLocalizingLayer(layerInfo.uri, layerInfo.languages.get(0));
                }
            }
        }

        layers.addAll(layerEntries.values());
    }

    @Override
    protected boolean isFileExists(String filePath) {
        return SVN.isFileExists(getSvnRepository(), filePath, getRevision());
    }

    private void collectModules(String layer, String segment, List<ModuleEntry> modules) throws RadixSvnException {
        final String segmentPath = SvnPath.append(layer, segment);
        if (!SVN.isExists(getSvnRepository(), segmentPath, getRevision())) {//RADIXMANAGER-305
            return;
        }

        final Set<String> documentedModules = new HashSet<>();
        getSvnRepository().getDir(segmentPath, getRevision(),  new SVNRepositoryAdapter.EntryHandler() {

            @Override
            public void accept(SvnEntry svnde) throws RadixSvnException {
                if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
                    documentedModules.add(svnde.getName());
                }
            }
        });

        for (final String mdl : documentedModules) {
            final String radixdocXml = SvnPath.append(SvnPath.append(segmentPath, mdl), RadixdocConventions.RADIXDOC_ZIP_FILE);
            if (SVN.isFileExists(getSvnRepository(), radixdocXml, getRevision())) {
                modules.add(new ModuleEntry(segment, mdl));
            }
        }
    }

    private static final class LayerInfo {

        String uri;
        List<String> baseLayerURIs = new ArrayList<>();
        List<EIsoLanguage> languages = new ArrayList<>();
        boolean isLocalizing;
        int radixdocFormatVersion;
    }

    private static final class LayerXmlParser extends DefaultHandler {

        LayerInfo layerInfo;

        public LayerXmlParser(LayerInfo layerInfo) {
            this.layerInfo = layerInfo;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("Layer")) {
                final String layerUri = attributes.getValue("Uri");
                final String radixdocFormatVersion = attributes.getValue("RadixdocFormatVersion");
                final String baseLayerURIs = attributes.getValue("BaseLayerURIs");
                final String languages = attributes.getValue("Languages");
                final String isLocalizing = attributes.getValue("IsLocalizing");

                layerInfo.uri = layerUri;

                try {
                    layerInfo.radixdocFormatVersion = Integer.parseInt(radixdocFormatVersion);
                } catch (NumberFormatException e) {
                    layerInfo.radixdocFormatVersion = 0;
                }

                if (!Utils.emptyOrNull(baseLayerURIs)) {
                    layerInfo.baseLayerURIs.addAll(Arrays.asList(baseLayerURIs.split(" ")));
                }

                for (String lang : languages.split(" ")) {
                    try {
                        layerInfo.languages.add(EIsoLanguage.getForValue(lang));
                    } catch (NoConstItemWithSuchValueError ex) {
                        Logger.getLogger(SvnFileProvider.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                layerInfo.isLocalizing = Boolean.parseBoolean(isLocalizing);

                throw new RadixdocStopParseException();
            }
        }
    }

    private LayerInfo parseLayerXml(String layerXml) {
        final SAXParser parser;
        try {
            parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (ParserConfigurationException | SAXException ex) {
            return null;
        }

        final LayerInfo layerInfo = new LayerInfo();

        try (final InputStream stream = getInputStream(layerXml)) {
            if (stream != null) {
                parser.parse(stream, new LayerXmlParser(layerInfo));
            }
        } catch (RadixdocStopParseException e) {
            return layerInfo;
        } catch (SAXException | IOException ex) {
//            Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    protected File getTempLocaleDir() {
        if (tempDir == null) {
            try {
                tempDir = File.createTempFile("radixdoc", "-locale");
                tempDir.delete();
                tempDir.mkdirs();
            } catch (IOException ex) {
                Logger.getLogger(SvnFileProvider.class.getName()).log(Level.SEVERE, "Unable to create temporary directory for localizing bundles", ex);
            }
        }
        return tempDir;
    }

    @Override
    protected InputStream getInputForBundle(String bundlePath) {
        File file = new File(getTempLocaleDir(), bundlePath);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException ex) {
            }
        } else {
            boolean create = false;
            try (InputStream in = getInputStream(bundlePath)) {

                if (in == null) {
                    return null;
                }

                file.getParentFile().mkdirs();
                try (FileOutputStream out = new FileOutputStream(file.getAbsolutePath())) {
                    FileUtils.copyStream(in, out);
                    create = true;
                } catch (IOException ex) {
                    Logger.getLogger(SvnFileProvider.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (IOException ex) {
            }

            if (create) {
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException ex) {
                }
            }
        }
        return getInputStream(bundlePath);
    }

    @Override
    public void clearCache() {
        if (tempDir != null) {
            FileUtils.deleteFile(tempDir);
        }
    }
}
