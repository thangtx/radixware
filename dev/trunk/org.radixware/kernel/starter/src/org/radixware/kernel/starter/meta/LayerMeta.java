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
package org.radixware.kernel.starter.meta;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.starter.filecache.FileCacheEntry;
import org.radixware.kernel.starter.log.SafeLogger;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderAccessor;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class LayerMeta {

    public static final String LAYER_XML_FILE = "layer.xml";
    protected static final String TAG_LAYER = "Layer";
    protected static final String ATTR_TITLE = "Title";
    protected static final String ATTR_BASE_LAYER_URIS = "BaseLayerURIs";
    protected static final String ATTR_PREV_LAYER_URI = "PrevLayerUri";
    protected static final String ATTR_RELEASE_NUMBER = "ReleaseNumber";
    protected static final String ATTR_EMPTY = "IsEmpty";
    protected static final String ATTR_LANGUAGES = "Languages";
    private static final String ACCESSIBLE_ROOTS_TITLE = "AccessibleRoots";
    private static final String ACCESSIBLE_ROLES_TITLE = "AccessibleRoles";
    private static final String ATTR_ROOT_ID = "Id";
    private static final String ATTR_ROOT_LAYER_URI = "LayerUri";
    private final String layerUri;
    private final long revisionNum;
    private final File root;
    private final String title;
    private final String[] prevLayerUris;
    private final String releaseNumber;
    private final List<String> languages = new ArrayList<>(2);
    private final List<DirectoryMeta> directories;
    private final byte[] xmlBytes;
    private final RadixLoaderAccessor loaderAccessor;
    private final RadixLoader.HowGetFile howGet;
    private static final String[] no_prev_layers = new String[0];
    private static final DocumentBuilder documentBuilder;
    private final Map<String, String[]> accessibleRoots;
    private final Map<String, String[]> accessibleRoles;
    private final boolean empty;

    static {
        //temporary workaround for RADIX-6549
        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        DocumentBuilder builder = null;
        try {
            builder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            SafeLogger.getInstance().error(LayerMeta.class, "Unable to create DocumentBuilerFactory", ex);
        }
        documentBuilder = builder;
    }
    
    public LayerMeta(final String layerUri, final RadixLoaderAccessor loaderAccessor, final long revisionNum, final RadixLoader.HowGetFile howGet) throws IOException {
        try {
            this.layerUri = layerUri;
            this.revisionNum = revisionNum;
            this.loaderAccessor = loaderAccessor;
            this.howGet = howGet;
            final String layer_xml_path = layerUri + "/" + LAYER_XML_FILE;
            final FileCacheEntry layerFile = (FileCacheEntry) loaderAccessor.getFile(layer_xml_path, revisionNum, howGet);
            try {
                xmlBytes = layerFile.getData();
            } finally {
                layerFile.close();
            }
            try {
                final Document layerDoc = documentBuilder.parse(new ByteArrayInputStream(xmlBytes));
                Node layerNode = layerDoc.getFirstChild();

                title = findAttribute(layerNode, ATTR_TITLE);
                String prevLayerList = findAttribute(layerNode, ATTR_BASE_LAYER_URIS);
                if (prevLayerList == null || prevLayerList.isEmpty() || prevLayerList.trim().isEmpty()) {
                    prevLayerList = findAttribute(layerNode, ATTR_PREV_LAYER_URI);
                    if (prevLayerList == null || prevLayerList.isEmpty() || prevLayerList.trim().isEmpty()) {
                        prevLayerUris = no_prev_layers;
                    } else {
                        prevLayerUris = new String[]{prevLayerList.trim()};
                    }
                } else {
                    String[] prevs = prevLayerList.split(" ");
                    List<String> list = new ArrayList<>(prevs.length);
                    for (final String prev : prevs) {
                        final String checked = prev == null ? null : prev.trim();
                        if (checked == null || checked.isEmpty()) {
                            continue;
                        } else {
                            list.add(checked);
                        }
                    }
                    prevLayerUris = list.toArray(new String[list.size()]);
                }
                releaseNumber = findAttribute(layerNode, ATTR_RELEASE_NUMBER);
                final String languagesAttr = findAttribute(layerNode, ATTR_LANGUAGES);
                if (languagesAttr != null && !languagesAttr.isEmpty()) {
                    final String[] languagesArr = languagesAttr.split(" ");
                    for (String language : languagesArr) {
                        if (!language.isEmpty()) {
                            languages.add(language);
                        }
                    }
                }
                this.root = loaderAccessor.getLoader().getRoot();
                Map<String, List<String>> rootsIndex = loadAccessibleDefIndex(layerNode, ACCESSIBLE_ROOTS_TITLE);
                /* 
                 * if rootsIndex is null then accessibleRoots inherited from base layers
                 * if rootsIndex is empty then no roots from base layers included to result list
                 */
                if (rootsIndex == null) {
                    accessibleRoots = null;
                } else {
                    accessibleRoots = new HashMap<>();
                    for (Map.Entry<String, List<String>> e : rootsIndex.entrySet()) {
                        accessibleRoots.put(e.getKey(), e.getValue() == null ? null : e.getValue().toArray(new String[e.getValue().size()]));
                    }
                }
                rootsIndex = loadAccessibleDefIndex(layerNode, ACCESSIBLE_ROLES_TITLE);
                if (rootsIndex == null) {
                    accessibleRoles = null;
                } else {
                    accessibleRoles = new HashMap<>();
                    for (Map.Entry<String, List<String>> e : rootsIndex.entrySet()) {
                        accessibleRoles.put(e.getKey(), e.getValue() == null ? null : e.getValue().toArray(new String[e.getValue().size()]));
                    }
                }
                
                final String emptyStr = findAttribute(layerNode, ATTR_EMPTY);
                empty = "true".equals(emptyStr);

                directories = new ArrayList<>();

            } catch (SAXException xmlEx) {
                throw new IOException("XML exception", xmlEx);
            }
        } catch (IOException ex) {
            LogFactory.getLog(LayerMeta.class).error("Unable to load meta for layer '" + layerUri + "'", ex);
            throw ex;
        }
    }
    
    public static DocumentBuilder getDocumentBuilder() {
        return documentBuilder;
    }

    public boolean isEmpty() {
        return empty;
    }
    
    private Map<String, List<String>> loadAccessibleDefIndex(Node layerNode, String nodeName) {
        Map<String, List<String>> rootsIndex = null;
        for (int i = 0; i < layerNode.getChildNodes().getLength(); i++) {
            if (nodeName.equals(layerNode.getChildNodes().item(i).getNodeName())) {
                rootsIndex = new HashMap<>();
                final Node rootsNode = layerNode.getChildNodes().item(i);
                for (int j = 0; j < rootsNode.getChildNodes().getLength(); j++) {
                    String rootId = findAttribute(rootsNode.getChildNodes().item(j), ATTR_ROOT_ID);
                    String rootLayerUri = findAttribute(rootsNode.getChildNodes().item(j), ATTR_ROOT_LAYER_URI);
                    if (rootId != null && rootLayerUri != null) {
                        List<String> roots = rootsIndex.get(rootLayerUri);
                        if (roots == null) {
                            roots = new LinkedList<>();
                            rootsIndex.put(rootLayerUri, roots);
                        }
                        if (!roots.contains(rootId)) {
                            roots.add(rootId);
                        }
                    }

                }
                break;
            }
        }
        return rootsIndex;
    }

    private String findAttribute(final Node node, final String attrName) {
        if (node == null || node.getAttributes() == null || node.getAttributes().getNamedItem(attrName) == null) {
            return null;
        }
        return node.getAttributes().getNamedItem(attrName).getTextContent();
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void loadDirectories() throws IOException, XMLStreamException {
        initDirectories(DirectoryMeta.DIRECTORY_XML_FILE, loaderAccessor, howGet);
    }

    private void initDirectories(final String path, final RadixLoaderAccessor loaderAccessor, final RadixLoader.HowGetFile howGet) throws IOException, XMLStreamException {
        final FileCacheEntry directoryFile = (FileCacheEntry) loaderAccessor.getFile(layerUri + "/" + path, revisionNum, howGet);
        byte[] directory_xml_data;
        try {
            directory_xml_data = directoryFile.getData();
        } finally {
            directoryFile.close();
        }
        final DirectoryMeta xml = new DirectoryMeta(root, this, path, directory_xml_data);
        directories.add(xml);
        if (xml.getIncludes() != null) {
            for (String include : xml.getIncludes()) {
                String sub_path = null;
                final int index = path.lastIndexOf('/');
                if (index != -1) {
                    sub_path = path.substring(0, index) + "/" + include;
                } else {
                    sub_path = include;
                }
                initDirectories(sub_path, loaderAccessor, howGet);
            }
        }
    }

    public void drainClassesMap(final Map<String, FileMeta> classesMap) {
        for (DirectoryMeta dir : directories) {
            dir.drainClassesMap(classesMap);
        }
    }

    public void toMap(
            final Map<String, FileMeta> filesMap,
            final Map<String, FileMeta> filesOverMap,
            final Map<String, FileMeta> packagesOverMap,
            final Map<String, FileMeta> resourcesOverMap) throws RadixLoaderException {
        for (DirectoryMeta dir : directories) {
            if (filesMap != null) {
                filesMap.putAll(dir.getFilesMap());
            }
            if (filesOverMap != null) {
                filesOverMap.putAll(dir.getFilesOverMap());
            }
            if (packagesOverMap != null) {
                for (String pck : dir.getPackagesOverMap().keySet()) {
                    final FileMeta new_file = dir.getPackagesOverMap().get(pck);
                    final FileMeta file = packagesOverMap.get(pck);
                    if (file != null) {
                        file.addNext(new_file);
                    } else {
                        packagesOverMap.put(pck, new_file);
                    }
                }
            }
            if (resourcesOverMap != null) {
                for (String pck : dir.getResourcesOverMap().keySet()) {
                    final FileMeta new_file = dir.getResourcesOverMap().get(pck);
                    final FileMeta file = resourcesOverMap.get(pck);
                    if (file != null) {
                        file.addNext(new_file);
                    } else {
                        resourcesOverMap.put(pck, new_file);
                    }
                }
            }
        }
    }

    public String getUri() {
        return layerUri;
    }

    public String getTitle() {
        return title;
    }

    public Map<String, String[]> getAccessibleExplorerRoots() {
        return accessibleRoots == null ? null : Collections.unmodifiableMap(accessibleRoots);
    }

    public Map<String, String[]> getAccessibleRoles() {
        return accessibleRoles == null ? null : Collections.unmodifiableMap(accessibleRoles);
    }

    public String[] getBaseLayerUris() {
        return prevLayerUris;
    }

    public String getReleaseNumber() {
        return releaseNumber;
    }

    public Collection<String> getSupportedLanguages() {
        return Collections.unmodifiableCollection(languages);
    }

    public Iterable<DirectoryMeta> getDirectories() {
        return Collections.unmodifiableCollection(directories);
    }

    public InputStream getXmlInputStream() {
        return new ByteArrayInputStream(xmlBytes);
    }

    public long getLastModificationTime() {
        long mtime = 0;
        for (DirectoryMeta dir : directories) {
            final long t = dir.getFile().lastModified();
            if (t > mtime) {
                mtime = t;
            }
        }
        return mtime;
    }

    public boolean isLogicallyEquals(final LayerMeta otherMeta) {
        if (otherMeta == null) {
            return false;
        }
        try {
            final List<String> ignoredLayerAttrs = Arrays.asList(ATTR_TITLE, ATTR_RELEASE_NUMBER, "Copyright", "Name", "APIVersion");
            final List<String> ignoredElements = Arrays.asList("Compatibility");
            DocumentBuilder builder = documentBuilder;
            if (builder == null) {
                final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                builder = dbFactory.newDocumentBuilder();
            }
            Document thisDoc = builder.parse(getXmlInputStream());
            Document otherDoc = builder.parse(otherMeta.getXmlInputStream());
            removeIgnoredLayerAttributes(thisDoc, ignoredLayerAttrs);
            removeIgnoredLayerAttributes(otherDoc, ignoredLayerAttrs);
            removeIgnoredLayerSubElements(thisDoc, ignoredElements);
            removeIgnoredLayerSubElements(otherDoc, ignoredElements);
            return thisDoc.isEqualNode(otherDoc);
        } catch (Exception ex) {
            return false;
        }
    }

    private void removeIgnoredLayerSubElements(final Node layerNode, final List<String> ignoredLayerSubElements) {

        if (layerNode.getNodeName().equals(TAG_LAYER)) {
            for (int i = layerNode.getChildNodes().getLength() - 1; i >= 0; i--) {
                final Node childNode = layerNode.getChildNodes().item(i);
                if (ignoredLayerSubElements != null && ignoredLayerSubElements.contains(childNode.getNodeName())) {
                    layerNode.removeChild(childNode);
                }
            }
        }
        if (layerNode.getChildNodes() != null) {
            for (int i = 0; i < layerNode.getChildNodes().getLength(); i++) {
                removeIgnoredLayerSubElements(layerNode.getChildNodes().item(i), ignoredLayerSubElements);
            }
        }
    }

    private void removeIgnoredLayerAttributes(final Node layerNode, final List<String> ignoredLayerAttributes) {
        if (layerNode.getNodeName().equals(TAG_LAYER)) {
            for (final String ignoredAttr : ignoredLayerAttributes) {
                if (layerNode.getAttributes().getNamedItem(ignoredAttr) != null) {
                    layerNode.getAttributes().removeNamedItem(ignoredAttr);
                }
            }
        }
        if (layerNode.getChildNodes() != null) {
            for (int i = 0; i < layerNode.getChildNodes().getLength(); i++) {
                removeIgnoredLayerAttributes(layerNode.getChildNodes().item(i), ignoredLayerAttributes);
            }
        }
    }
}
