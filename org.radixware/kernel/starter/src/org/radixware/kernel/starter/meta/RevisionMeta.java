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
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.stream.XMLStreamException;
import org.radixware.kernel.starter.filecache.CacheEntry;
import org.radixware.kernel.starter.radixloader.ERevisionMetaType;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderAccessor;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.kernel.starter.radixloader.RadixLoaderUtils;
import org.radixware.kernel.starter.utils.ClassPackage;

public class RevisionMeta {

    public static final String ABOUT_XML = "about.xml";
    private static final String LOCALIZING_LAYER_MARK = "$locale-";
    public static final String VERSIONS_STR_SEPARATOR = ",";
    public static final String REVISIONS_SEPARATOR = ":";
    private final RadixLoaderAccessor loaderAccessor;
    private volatile long revisionNum;
    private volatile long timestampMillis = -1;
    private final List<LayerMeta> topLayers = new ArrayList<>();
    private final Map<String, LayerMeta> allLayers = new HashMap<>();
    private final List<LayerMeta> allLayersTopologicallySortedFromBottom;
    private final Map<String, FileMeta> filesMap = new HashMap<>();
    private final Map<String, FileMeta> filesOverMap = new HashMap();
    private final Map<String, FileMeta> packagesOverMap = new HashMap<>();
    private final Map<String, FileMeta> resourcesOverMap = new HashMap<>();
    private final Map<String, FileMeta> tmpClassesMap = new HashMap<>();
    private final List<String> allLayersInRepo = new ArrayList<>();
    private final List<String> languages = new ArrayList<>();
    private final String explicitAppLayersVersionString;
    private final String explicitKernelLayersVersionString;
    private final String compatibleKernelRevisionsString;
    private final String kernelRevisionsString;
    private final ERevisionMetaType type;

    public RevisionMeta(
            final RadixLoaderAccessor loaderAccessor,
            final long revisionNum,
            final RadixLoader.HowGetFile howGet) throws IOException, XMLStreamException {
        this(loaderAccessor, revisionNum, howGet, ERevisionMetaType.FULL);
    }

    public RevisionMeta(
            final RadixLoaderAccessor loaderAccessor,
            final long revisionNum,
            final RadixLoader.HowGetFile howGet,
            final ERevisionMetaType type) throws IOException, XMLStreamException {
        this(loaderAccessor, revisionNum, howGet, null, null, null, null, type);
    }

    public RevisionMeta(
            final RadixLoaderAccessor loaderAccessor,
            final long revisionNum,
            final RadixLoader.HowGetFile howGet,
            final String explicitKernelLayersVersionString,
            final String explicitAppLayersVersionString,
            final String kernelRevisionsString,
            final String compatibleKernelRevisionsString) throws IOException, XMLStreamException {
        this(loaderAccessor,
                revisionNum,
                howGet,
                explicitKernelLayersVersionString,
                explicitAppLayersVersionString,
                kernelRevisionsString,
                compatibleKernelRevisionsString,
                ERevisionMetaType.FULL);
    }

    public RevisionMeta(
            final RadixLoaderAccessor loaderAccessor,
            final long revisionNum,
            final RadixLoader.HowGetFile howGet,
            final String explicitKernelLayersVersionString,
            final String explicitAppLayersVersionString,
            final String kernelRevisionsString,
            final String compatibleKernelRevisionsString,
            final ERevisionMetaType type) throws IOException, XMLStreamException {
        this.loaderAccessor = loaderAccessor;
        this.revisionNum = revisionNum;
        this.type = type;
        
        if (type == ERevisionMetaType.FULL) {
            scanLocalReplacementDir(revisionNum, explicitKernelLayersVersionString, explicitAppLayersVersionString);
        }
        
        final List<String> repoLayers = loaderAccessor.getLoader().listAllLayerUrisInRepository(revisionNum, howGet);
        if (repoLayers != null) {
            allLayersInRepo.addAll(repoLayers);
        }
        computeLanguages(howGet);
        readLayersHierarchy(loaderAccessor.getLoader().getTopLayerUris(), howGet);
        tmpClassesMap.clear();//no conflicts detected, free memory
        if (loaderAccessor.getLoader().getTopLayerUris() != null) {
            for (String topLayerUri : loaderAccessor.getLoader().getTopLayerUris()) {
                topLayers.add(allLayers.get(topLayerUri));
            }
        }
        Collections.sort(topLayers, new Comparator<LayerMeta>() {
            @Override
            public int compare(LayerMeta layer1, LayerMeta layer2) {
                return layer1.getUri().compareTo(layer2.getUri());
            }
        });
        allLayersTopologicallySortedFromBottom = Collections.unmodifiableList(sortAllLayers());
        if (explicitAppLayersVersionString != null) {
            this.explicitAppLayersVersionString = explicitAppLayersVersionString;
            this.explicitKernelLayersVersionString = explicitKernelLayersVersionString;
            this.kernelRevisionsString = kernelRevisionsString == null ? "" : kernelRevisionsString;
            this.compatibleKernelRevisionsString = compatibleKernelRevisionsString == null ? "" : compatibleKernelRevisionsString;
        } else {
            CacheEntry aboutEntry = null;
            try {
                aboutEntry = loaderAccessor.getFile(ABOUT_XML, revisionNum, howGet);
            } catch (Exception ex) {
                aboutEntry = null;
            }
            if (aboutEntry != null) {
                final StringBuilder appVerBuilder = new StringBuilder();
                final StringBuilder kernelVerBuilder = new StringBuilder();
                final StringBuilder kernelRevsBuilder = new StringBuilder();
                final StringBuilder compatibleKernelRevsBuilder = new StringBuilder();
                try {
                    RadixLoaderUtils.readVersions(new ByteArrayInputStream(aboutEntry.getData(null)), kernelVerBuilder, appVerBuilder, kernelRevsBuilder, compatibleKernelRevsBuilder, getAllLayerUrisSortedFromBotton());
                    if (appVerBuilder.length() > 0 && kernelVerBuilder.length() > 0) {
                        this.explicitAppLayersVersionString = appVerBuilder.toString();
                        this.explicitKernelLayersVersionString = kernelVerBuilder.toString();
                    } else {
                        this.explicitAppLayersVersionString = null;
                        this.explicitKernelLayersVersionString = null;
                    }
                    this.kernelRevisionsString = kernelRevsBuilder.toString();
                    this.compatibleKernelRevisionsString = compatibleKernelRevsBuilder.toString();
                } catch (Exception ex) {
                    throw new RadixLoaderException("Unable to load " + ABOUT_XML, ex);
                }
            } else {
                this.explicitAppLayersVersionString = null;
                this.explicitKernelLayersVersionString = null;
                this.kernelRevisionsString = "";
                this.compatibleKernelRevisionsString = "";
            }
        }
    }
    
    private void scanLocalReplacementDir(final long revisionNum, String explicitKernelLayersVersionString, String explicitAppLayersVersionString) throws RadixLoaderException {
        String kernelVers = explicitKernelLayersVersionString;
        String appVers = explicitAppLayersVersionString;
        if (appVers == null) {
            final RevisionMeta layersMeta = RadixLoader.getInstance().getRevisionMeta(revisionNum, ERevisionMetaType.LAYERS_ONLY);
            kernelVers = layersMeta.getKernelLayerVersionsString();
            appVers = layersMeta.getAppLayerVersionsString();
        }
        RadixLoader.getInstance().getLocalFiles().loadForVersion(revisionNum, kernelVers, appVers);
    }

    public ERevisionMetaType getType() {
        return type;
    }

    public String getCompatibleKernelRevisionsString() {
        return compatibleKernelRevisionsString;
    }

    public String getKernelRevisionsString() {
        return kernelRevisionsString;
    }

    public Map<String, List<Long>> getCompatibleKernelRevisions() {
        return parseUrisAndRevisionsListString(compatibleKernelRevisionsString);
    }

    public Map<String, Long> getKernelRevisions() {
        final Map<String, List<Long>> tmp = parseUrisAndRevisionsListString(kernelRevisionsString);
        final Map<String, Long> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<Long>> entry : tmp.entrySet()) {
            result.put(entry.getKey(), entry.getValue().isEmpty() ? null : entry.getValue().get(0));
        }
        return result;
    }

    private Map<String, List<Long>> parseUrisAndRevisionsListString(final String str) {
        final Map<String, List<Long>> result = new LinkedHashMap<>();
        if (str == null || str.isEmpty()) {
            return result;
        }
        final String[] urisAndRevs = str.split(VERSIONS_STR_SEPARATOR);
        for (String uriAndRevsStr : urisAndRevs) {
            final String[] uriAndRevs = uriAndRevsStr.split("=");
            final List<Long> revs = new ArrayList<>();
            if (uriAndRevs.length > 1) {
                final String[] revStrings = uriAndRevs[1].split(REVISIONS_SEPARATOR);
                for (String revString : revStrings) {
                    try {
                        revs.add(Long.parseLong(revString));
                    } catch (Exception ex) {
                        ex.printStackTrace(System.out);
                    }
                }
            }
            result.put(uriAndRevs[0], revs);
        }
        return result;
    }

    public boolean isKernelCompatibleWhenUpgradingTo(final RevisionMeta target) {
        final Map<String, Long> thisKernelRevs = getKernelRevisions();
        final Map<String, List<Long>> targetCompatibleKernRevs = target.getCompatibleKernelRevisions();
        if (thisKernelRevs.isEmpty() || targetCompatibleKernRevs.isEmpty()) {
            return Objects.equals(getKernelLayerVersionsString(), target.getKernelLayerVersionsString());
        }
        if (getKernelRevisionsString().equals(target.getKernelRevisionsString())) {
            return true;
        }
        if (thisKernelRevs.size() != targetCompatibleKernRevs.size()) {
            return false;
        }
        for (Map.Entry<String, Long> thisKernRevEntry : thisKernelRevs.entrySet()) {
            final List<Long> compatRevs = targetCompatibleKernRevs.get(thisKernRevEntry.getKey());
            if (compatRevs == null || !compatRevs.contains(thisKernRevEntry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public void setTimestampMillis(final long millis) {
        timestampMillis = millis;
    }

    public long getTimestampMillis() {
        return timestampMillis;
    }

    public boolean isOlderThen(final long revNum, final long revTimestampMillis) {
        return getNum() < revNum || (getNum() == revNum && getTimestampMillis() != revTimestampMillis);
    }

    private List<LayerMeta> sortAllLayers() {
        final List<LayerMeta> all = new ArrayList<>(allLayers.values());
        Collections.sort(all, new Comparator<LayerMeta>() {
            @Override
            public int compare(LayerMeta o1, LayerMeta o2) {
                String match1 = o1.getTitle() == null ? o1.getUri() : o1.getTitle();
                String match2 = o2.getTitle() == null ? o2.getUri() : o2.getTitle();
                return match1.compareTo(match2);
            }
        });
        LinkedList<LayerMeta> result = new LinkedList<>();
        for (LayerMeta l : all) {
            if (result.contains(l)) {
                continue;
            }
            int index = -1;
            for (int i = 0; i < result.size(); i++) {
                LayerMeta added = result.get(i);
                if (isBaseLayer(l, added)) {
                    index = i;
                    break;
                }
            }

            if (index < 0 || index >= result.size()) {
                result.add(l);
            } else {
                result.add(index, l);
            }
        }
        return result;
    }

    public boolean isBaseLayer(LayerMeta candidate, LayerMeta target) {
        if (target != null && candidate != null && target.getBaseLayerUris() != null) {
            for (String baseUri : target.getBaseLayerUris()) {
                if (baseUri.equals(candidate.getUri()) || isBaseLayer(candidate, allLayers.get(baseUri))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void loadDataFromLayer(final LayerMeta layerMeta) throws RadixLoaderException {
        try {
            layerMeta.loadDirectories();
            final Map<String, FileMeta> files_map = new HashMap<>();
            final Map<String, FileMeta> files_over_map = new HashMap<>();
            final Map<String, FileMeta> packages_over_map = new HashMap<>();
            final Map<String, FileMeta> resources_map = new HashMap<>();
            layerMeta.toMap(files_map, files_over_map, packages_over_map, resources_map);
            filesMap.putAll(files_map);
            filesOverMap.putAll(files_over_map);
            for (Map.Entry<String, FileMeta> entry : packages_over_map.entrySet()) {
                final FileMeta new_file = entry.getValue();
                final FileMeta file = packagesOverMap.get(entry.getKey());
                if (file != null) {
                    file.addNext(new_file);
                } else {
                    packagesOverMap.put(entry.getKey(), new_file);
                }
            }

            for (Map.Entry<String, FileMeta> e : resources_map.entrySet()) {
                final FileMeta new_file = e.getValue();
                final FileMeta file = resourcesOverMap.get(e.getKey());
                if (file != null) {
                    file.addNext(new_file);
                } else {
                    resourcesOverMap.put(e.getKey(), new_file);
                }
            }

            final Map<String, FileMeta> classesFromLayer = new HashMap<>();
            layerMeta.drainClassesMap(classesFromLayer);
            checkClassesForAmbiguityAndAppend(this.tmpClassesMap, classesFromLayer);
        } catch (IOException | XMLStreamException ex) {
            if (ex instanceof RadixLoaderException) {
                throw (RadixLoaderException) ex;
            }
            throw new RadixLoaderException("Error", ex);
        }
    }

    private void checkClassesForAmbiguityAndAppend(final Map<String, FileMeta> destination, final Map<String, FileMeta> source) throws RadixLoaderException {
        for (Map.Entry<String, FileMeta> sourceEntry : source.entrySet()) {
            final FileMeta existingMeta = destination.get(sourceEntry.getKey());
            if (existingMeta == null) {
                destination.put(sourceEntry.getKey(), sourceEntry.getValue());
            } else {
                if (isBaseLayer(sourceEntry.getValue().getLayer(), existingMeta.getLayer())) {
                    continue;
                }
                if (!isBaseLayer(existingMeta.getLayer(), sourceEntry.getValue().getLayer())) {
                    if (existingMeta.getStore() == null || existingMeta.getStore().startsWith(existingMeta.getLayer().getUri() + "/ads")) {
                        throw new RadixLoaderException("File '" + existingMeta.getName() + "' is ambigiously defined in '" + existingMeta.getLayer().getUri() + "' and '" + sourceEntry.getValue().getLayer().getUri() + "'");
                    }
                } else {
                    destination.put(sourceEntry.getKey(), sourceEntry.getValue());
                }
            }
        }
    }

    private void computeLanguages(final RadixLoader.HowGetFile howGet) throws IOException {
        for (String topUri : loaderAccessor.getLoader().getTopLayerUris()) {
            final LayerMeta meta = new LayerMeta(topUri, loaderAccessor, revisionNum, howGet);
            for (String language : meta.getLanguages()) {
                if (!languages.contains(language)) {
                    languages.add(language);
                }
            }

            for (String uri : allLayersInRepo) {
                if (uri.startsWith(topUri + LOCALIZING_LAYER_MARK)) {
                    final String language = uri.substring(topUri.length() + LOCALIZING_LAYER_MARK.length());
                    if (!languages.contains(language)) {
                        languages.add(language);
                    }
                }
            }
        }
    }

    private void readLayersHierarchy(List<String> uris, final RadixLoader.HowGetFile howGet) throws IOException, XMLStreamException {
        if (uris == null) {
            return;
        }
        for (String uri : uris) {
            final List<String> baseLayers = new ArrayList<>();
            for (String language : languages) {
                final String localizationLayerUri = uri + LOCALIZING_LAYER_MARK + language;
                if (allLayersInRepo.contains(localizationLayerUri)) {
                    baseLayers.addAll(readLayer(localizationLayerUri, howGet));
                }
            }
            baseLayers.addAll(readLayer(uri, howGet));
            readLayersHierarchy(baseLayers, howGet);
        }
    }

    final List<String> readLayer(final String uri, final RadixLoader.HowGetFile howGet) throws IOException {
        if (allLayers.containsKey(uri)) {
            return Collections.emptyList();
        }
        final LayerMeta layer = new LayerMeta(uri, loaderAccessor, revisionNum, howGet);
        if (type == ERevisionMetaType.FULL) {
            loadDataFromLayer(layer);
        }
        allLayers.put(uri, layer);
        String[] base = layer.getBaseLayerUris();
        if (base == null || base.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(base);
    }

    public List<String> getLanguages() {
        return languages;
    }

    public long getNum() {
        return revisionNum;
    }

    /**
     * Service method must be used by RadixDirLoader only
     *
     * @param revisionNum
     * @throws RadixLoaderException
     */
    public void setNum(final long revisionNum) throws RadixLoaderException {
        if (this.revisionNum != 0) {
            throw new RadixLoaderException("Revision number is already defined");
        }
        this.revisionNum = revisionNum;
        RadixLoader.getInstance().getLocalFiles().setRevisionNum(revisionNum);
    }

    public List<LayerMeta> getTopLayers() {
        return Collections.unmodifiableList(topLayers);
    }

    public List<LayerMeta> getAllLayersSortedFromBottom() {
        return allLayersTopologicallySortedFromBottom;
    }

    public final List<String> getAllLayerUrisSortedFromBotton() {
        final List<String> result = new ArrayList<>();
        for (LayerMeta layerMeta : allLayersTopologicallySortedFromBottom) {
            result.add(layerMeta.getUri());
        }
        return result;
    }

    public List<LayerMeta> getAllLayersSortedFromTop() {
        List<LayerMeta> layers = new ArrayList(allLayersTopologicallySortedFromBottom);
        Collections.reverse(layers);
        return layers;
    }

    public String getKernelLayerVersionsString() {
        return explicitKernelLayersVersionString == null ? getLayerVersionsString() : explicitKernelLayersVersionString;
    }

    public String getAppLayerVersionsString() {
        return explicitAppLayersVersionString == null ? getLayerVersionsString() : explicitAppLayersVersionString;
    }

    public String getLayerVersionsString() {
        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (LayerMeta layerMeta : getAllLayersSortedFromBottom()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(VERSIONS_STR_SEPARATOR);
            }
            sb.append(layerMeta.getUri());
            sb.append('=');
            sb.append(layerMeta.getReleaseNumber());

        }
        return sb.toString();
    }

    public Map<String, String[]> getAccessibleExplorerRootIds() {
        List<LayerMeta> tls = getTopLayers();
        Map<String, String[]> result = new HashMap<>();
        for (LayerMeta l : tls) {
            collectAccessibleDefs(l, new DefinitinMapProducer() {

                @Override
                public Map<String, String[]> getAccessibleDefs(LayerMeta layer) {
                    return layer.getAccessibleExplorerRoots();
                }
            }, result);
        }
        return result;
    }

    public Map<String, String[]> getAccessibleRoleIds() {
        List<LayerMeta> tls = getTopLayers();
        Map<String, String[]> result = new HashMap<>();
        for (LayerMeta l : tls) {
            collectAccessibleDefs(l, new DefinitinMapProducer() {

                @Override
                public Map<String, String[]> getAccessibleDefs(LayerMeta layer) {
                    return layer.getAccessibleRoles();
                }
            }, result);
        }
        return result;
    }

    private interface DefinitinMapProducer {

        Map<String, String[]> getAccessibleDefs(LayerMeta layer);
    }

    private void collectAccessibleDefs(LayerMeta fromLayer, DefinitinMapProducer producer, Map<String, String[]> result) {
        Map<String, String[]> inherited = new HashMap<>();
        String[] baseLayers = fromLayer.getBaseLayerUris();
        if (baseLayers != null && baseLayers.length > 0) {
            for (String bl : baseLayers) {
                LayerMeta lm = allLayers.get(bl);
                if (lm != null) {
                    collectAccessibleDefs(lm, producer, inherited);
                }
            }
        }
        Map<String, String[]> accessibleDefs = producer.getAccessibleDefs(fromLayer);

        if (accessibleDefs != null) {//layer has explicit root list. use this list
            for (String layerUri : inherited.keySet()) {
                String[] accessible = accessibleDefs.get(layerUri);
                if (accessible == null) {// no roots from this layer are accessible here
                    result.put(layerUri, new String[0]);
                } else {
                    result.put(layerUri, accessible);
                }
            }
//            String[] accessible = accessibleRoots.get(fromLayer.getUri());
//            if (accessible != null) {
//                result.put(fromLayer.getUri(), accessible);
//            }
            result.put(fromLayer.getUri(), null);
        } else {//all roots from this layer are allowed and all resolved from base layrs;
            result.putAll(inherited);
            result.put(fromLayer.getUri(), null);
        }
    }

    public FileMeta findFile(final String fileName) {
        return filesMap.get(fileName);
    }

    public FileMeta findOverFile(final String fileName) {
        return filesOverMap.get(fileName);
    }

    public FileMeta findClass(final String fileName) {
        return packagesOverMap.get(ClassPackage.getPackage(fileName));
    }

    public FileMeta findResources(final String fileName) {
        return resourcesOverMap.get(fileName);
    }

    public void getStorePackages(final String file, final Collection<String> entries) {
        for (FileMeta meta : packagesOverMap.values()) {
            do {
                if (meta.getStore() != null && meta.getStore().equals(file)) {
                    entries.add(meta.getName());
                }
                meta = meta.getNext();
            } while (meta != null);
        }
    }

    public void getStoreResources(final String file, final Collection<String> entries) {
        for (FileMeta meta : resourcesOverMap.values()) {
            do {
                if (meta.getStore() != null && meta.getStore().equals(file)) {
                    entries.add(meta.getName());
                }
                meta = meta.getNext();
            } while (meta != null);
        }
    }

    public RevisionMetaDiff getDiff(final RevisionMeta another) throws IOException {
        if (another == null) {
            throw new NullPointerException("Another revision metadata can't ne null");
        }
        else {
            final Set<String> logicallyUnchanged = new HashSet<>();
            final Map<String,FileMeta> addedFiles = new HashMap<>();
            final Map<String,FileMeta[]> modifiedFiles = new HashMap<>();
            final Map<String,FileMeta> removedFiles = new HashMap<>();

            final Set<String> cg = new HashSet<>(), mf = new HashSet<>(), rf = new HashSet<>();
            
            for (Map.Entry<String, LayerMeta> layerMeta : allLayers.entrySet()) {
                final String layerUri = layerMeta.getValue().getUri();
                
                for (LayerMeta meta : another.getAllLayersSortedFromBottom()) {
                    if (meta.getUri().equals(layerUri)) {
                        if (layerMeta.getValue().isLogicallyEquals(meta)) {
                            logicallyUnchanged.add(layerUri + "/" + LayerMeta.LAYER_XML_FILE);
                        }
                        break;
                    }
                }
            }

            for (Map.Entry<String, FileMeta> file : filesMap.entrySet()) {
                final FileMeta new_file = file.getValue();
                final FileMeta old_file = another.filesMap.get(new_file.getName());
                
                if (old_file == null) {
                    addedFiles.put(new_file.getName(),new_file);
                } else if (!new_file.hasSameDigest(old_file) && !logicallyUnchanged.contains(new_file.getName())) {
                    modifiedFiles.put(new_file.getName(),new FileMeta[]{old_file,new_file});
                }
            }
            
            for (Map.Entry<String, FileMeta> file : another.filesMap.entrySet()) {
                if (filesMap.get(file.getValue().getName()) == null) {
                    removedFiles.put(file.getValue().getName(),file.getValue());
                }
            }

            final Set<String> addedClasses = new HashSet<>();
            final Set<String> modifiedClasses = new HashSet<>();
            final Set<String> removedClasses = new HashSet<>();
            
            for (Map.Entry<String, FileMeta> item : addedFiles.entrySet()) {  // Extract classes from new jars and mark them as "added"
                scanClassesInJar(RadixLoader.getInstance().readFileData(item.getValue(),this),new ScanJarCallback() {
                    @Override
                    public void processClass(final ZipEntry item) {
                        addedClasses.add(item.getName());
                    }
                });
            }

            for (Map.Entry<String, FileMeta> item : removedFiles.entrySet()) {  // Extract classes from old jars and mark them as "removed"
                scanClassesInJar(RadixLoader.getInstance().readFileData(item.getValue(),another),new ScanJarCallback() {
                    @Override
                    public void processClass(final ZipEntry item) {
                        removedClasses.add(item.getName());
                    }
                });
            }

            return new RevisionMetaDiff(addedClasses, removedClasses, modifiedFiles.keySet());
        }
    }
    
    public void getChanges(
            final RevisionMeta oldRevision,
            final Set<String> changedGroups,
            final Set<String> modifiedFiles,
            final Set<String> removedFiles) throws IOException {

        final Set<String> logicallyUnchanged = new HashSet<>();
        if (oldRevision != null) {
            for (LayerMeta layerMeta : allLayers.values()) {
                LayerMeta oldLayerMeta = null;
                for (LayerMeta meta : oldRevision.getAllLayersSortedFromBottom()) {
                    if (meta.getUri().equals(layerMeta.getUri())) {
                        oldLayerMeta = meta;
                        break;
                    }
                }
                if (layerMeta.isLogicallyEquals(oldLayerMeta)) {
                    logicallyUnchanged.add(layerMeta.getUri() + "/" + LayerMeta.LAYER_XML_FILE);
                }
            }
        }

        for (FileMeta file : filesMap.values()) {
            FileMeta old_file = null;
            if (oldRevision != null) {
                old_file = oldRevision.filesMap.get(file.getName());
            }
            if (old_file == null) {
                changedGroups.add(file.getGroupType());
            } else if (!file.hasSameDigest(old_file) && !logicallyUnchanged.contains(file.getName())) {
                modifiedFiles.add(file.getName());
                changedGroups.add(file.getGroupType());
            }
        }
        if (oldRevision != null) {
            for (FileMeta file : oldRevision.filesMap.values()) {
                final FileMeta new_file = filesMap.get(file.getName());
                if (new_file == null) {
                    removedFiles.add(file.getName());
                    changedGroups.add(file.getGroupType());
                }
            }
        }
    }

    public long getLastModificationTime() {
        long mtime = 0;
        for (LayerMeta layer : allLayersTopologicallySortedFromBottom) {
            final long t = layer.getLastModificationTime();
            if (t > mtime) {
                mtime = t;
            }
        }
        return mtime;
    }

    public Iterable<FileMeta> getFileMetas() {
        return Collections.unmodifiableCollection(filesMap.values());
    }


    private interface ScanJarCallback {
        void processClass(final ZipEntry item);
    }
    
    private static void scanClassesInJar(final byte[] content, final ScanJarCallback callback) throws IOException {
        try(final ByteArrayInputStream is = new ByteArrayInputStream(content);
            final ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry ze;

            while ((ze = zis.getNextEntry()) != null) {
                if (ze.getName().endsWith(".class")) {
                    callback.processClass(ze);
                }
                zis.closeEntry();
            }
        }
    }
}
