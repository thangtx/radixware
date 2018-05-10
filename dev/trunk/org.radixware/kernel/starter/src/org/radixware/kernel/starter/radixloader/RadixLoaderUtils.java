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
package org.radixware.kernel.starter.radixloader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class RadixLoaderUtils {

    private static final String KERNEL_SEGMENT = "KernelSegment";
    private static final String APP_SEGMENT = "AppSegment";

    public static List<String> listAllLayersInDir(final File dir) {
        if (dir == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                final File childDir = new File(dir, name);
                if (childDir.exists() && childDir.isDirectory() && new File(childDir, LayerMeta.LAYER_XML_FILE).exists()) {
                    return true;
                }
                return false;
            }
        }));
    }

    public static void readVersions(final InputStream aboutDocInputStream, final StringBuilder kernelVersions, final StringBuilder appVersions, final StringBuilder kernelRevisions, final StringBuilder compatibleKernelRevisions, final List<String> usedLayersSortedFromBottom) throws SAXException, IOException {
        final Document aboutDoc = LayerMeta.getDocumentBuilder().parse(aboutDocInputStream);
        Node aboutNode = null;
        for (int i = 0; i < aboutDoc.getChildNodes().getLength(); i++) {
            Node n = aboutDoc.getChildNodes().item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE && "About".equals(n.getLocalName())) {
                aboutNode = n;
                break;
            }
        }
        if (aboutNode == null) {
            throw new IOException("About node not found in about.xml");
        }
        Node layersNode = null;
        for (int i = 0; i < aboutNode.getChildNodes().getLength(); i++) {
            Node n = aboutNode.getChildNodes().item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE && "Layers".equals(n.getLocalName())) {
                layersNode = n;
                break;
            }
        }
        if (layersNode == null) {
            throw new IOException("Layers node not found in about.xml");
        }
        final List<Node> layerNodes = new ArrayList<>();
        for (int i = 0; i < layersNode.getChildNodes().getLength(); i++) {
            if (layersNode.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE
                    && (usedLayersSortedFromBottom.contains(layersNode.getChildNodes().item(i).getAttributes().getNamedItem("Uri").getNodeValue()))) {
                layerNodes.add(layersNode.getChildNodes().item(i));
            }
        }
        if (usedLayersSortedFromBottom != null) {
            Collections.sort(layerNodes, new Comparator<Node>() {

                @Override
                public int compare(Node o1, Node o2) {
                    return usedLayersSortedFromBottom.indexOf(o1.getAttributes().getNamedItem("Uri").getNodeValue())
                            - usedLayersSortedFromBottom.indexOf(o2.getAttributes().getNamedItem("Uri").getNodeValue());
                }
            });
        }

        for (Node layerNode : layerNodes) {
            final String uri = layerNode.getAttributes().getNamedItem("Uri").getNodeValue();
            for (int i = 0; i < layerNode.getChildNodes().getLength(); i++) {
                final Node segmentNode = layerNode.getChildNodes().item(i);
                if (KERNEL_SEGMENT.equals(segmentNode.getLocalName())) {
                    appendVersion(kernelVersions, uri, segmentNode.getAttributes().getNamedItem("ReleaseNumber").getNodeValue());
                    appendVersion(kernelRevisions, uri, segmentNode.getAttributes().getNamedItem("Revision").getNodeValue());
                    final Node prevCompatibleRevsNode = segmentNode.getAttributes().getNamedItem("PrevCompatibleRevisions");
                    if (prevCompatibleRevsNode != null) {
                        appendVersion(compatibleKernelRevisions, uri, prevCompatibleRevsNode.getNodeValue().replace(" ", RevisionMeta.REVISIONS_SEPARATOR));
                    }
                }
                if (APP_SEGMENT.equals(segmentNode.getLocalName())) {
                    appendVersion(appVersions, uri, segmentNode.getAttributes().getNamedItem("ReleaseNumber").getNodeValue());
                }
            }
        }
    }

    private static void appendVersion(final StringBuilder sb, final String layerUri, final String version) {
        if (sb.length() > 0) {
            sb.append(RevisionMeta.VERSIONS_STR_SEPARATOR);
        }
        sb.append(layerUri);
        sb.append("=");
        sb.append(version);
    }
}
