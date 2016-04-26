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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public abstract class SvnFileProvider extends FileProvider {

    protected abstract SVNRepositoryAdapter getSvnRepository();

    protected abstract String[] getLayerPaths();

    protected abstract long getRevision();

    @Override
    protected final InputStream getInputStream(String fileName) {

        try {
            byte[] file = SVN.getFile(getSvnRepository(), fileName, getRevision());
            if (file != null) {
                return new ByteArrayInputStream(file);
            }
        } catch (RadixSvnException ex) {
            //...
        }

        return null;
    }
    private List<LayerEntry> layers;
    private final Object lock = new Object();

    @Override
    protected Collection<LayerEntry> getLayers() {
        synchronized (lock) {
            if (layers == null) {
                layers = new ArrayList<>();

                findModules();
            }

            return layers;
        }
    }

    private void findModules() {

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
                if (SVN.isFileExists(getSvnRepository(), layerXml, getRevision())) {

                    final LayerInfo layerInfo = parseLayerXml(layerXml);
                    if (layerInfo != null) {
                        layers.add(new LayerEntry(layer, layerInfo.uri, layerInfo.radixdocFormatVersion, modules));
                    }
                }
            }
        }
    }

    private void collectModules(String layer, String segment, List<ModuleEntry> modules) throws RadixSvnException {
        final String segmentPath = SvnPath.append(layer, segment);

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

                layerInfo.uri = layerUri;

                try {
                    layerInfo.radixdocFormatVersion = Integer.parseInt(radixdocFormatVersion);
                } catch (NumberFormatException e) {
                    layerInfo.radixdocFormatVersion = 0;
                }

                throw new StopParseException();
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
        } catch (StopParseException e) {
            return layerInfo;
        } catch (SAXException | IOException ex) {
//            Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
