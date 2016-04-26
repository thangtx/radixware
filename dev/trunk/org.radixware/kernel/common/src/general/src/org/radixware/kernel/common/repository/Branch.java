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

package org.radixware.kernel.common.repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERepositoryBranchType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.repository.fs.FSRepositoryBranch;
import org.radixware.kernel.common.repository.fs.IRepositoryBranch;
import org.radixware.kernel.common.repository.fs.IRepositoryLayer;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.RadixResourceBundle;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.product.BranchDocument;


public class Branch extends RadixObject implements IDirectoryRadixObject {

    public static final int FORMAT_VERSION = 2;

    /**
     * Layers list for branch
     */
    public class LayerSorter {

        private final Set<Layer> layersList;

        public LayerSorter(Collection<Layer> layers) {
            if (layers == null || layers.isEmpty()) {
                layersList = null;
            } else {
                this.layersList = new HashSet<>();
                this.layersList.addAll(layers);
            }
        }

        public List<Layer> getInOrder() {

            synchronized (this) {
                List<Layer> all = getLayers().list();
                Collections.sort(all, new Comparator<Layer>() {
                    @Override
                    public int compare(Layer o1, Layer o2) {
                        String match1 = o1.getName() == null ? o1.getURI() : o1.getName();
                        String match2 = o2.getName() == null ? o2.getURI() : o2.getName();
                        return match1.compareTo(match2);
                    }
                });
                LinkedList<Layer> result = new LinkedList<>();
                for (Layer l : all) {
                    if (result.contains(l)) {
                        continue;
                    }
                    int index = -1;
                    for (int i = 0; i < result.size(); i++) {
                        Layer added = result.get(i);
                        if (added.isHigherThan(l)) {
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
//                final Map<Layer, Object> prev2layer = new HashMap<>();
//                final List<Layer> roots = new LinkedList<>();
//                for (Layer layer : getLayers()) {
//                    List<Layer> prevs = layer.listBaseLayers();
//                    if (prevs == null || prevs.isEmpty() && !roots.contains(layer)) {
//                        roots.add(layer);
//                    } else {
//                        for (Layer prev : prevs) {
//                            Object obj = prev2layer.get(prev);
//                            if (obj == null) {
//                                prev2layer.put(prev, layer);
//                            } else {
//                                if (obj instanceof Layer) {
//                                    List<Layer> layers = new LinkedList<>();
//                                    layers.add((Layer) obj);
//                                    layers.add(layer);
//                                    prev2layer.put(prev, layers);
//                                } else {
//                                    List<Layer> layers = (List) obj;
//                                    layers.add(layer);
//                                }
//                            }
//                        }
//                    }
//                }
//
//                for (Layer layer : roots) {
//                    addToList(layer, prev2layer, result);
//                }
                return result;

            }


        }

        private void addToList(Layer root, Map<Layer, Object> all, List<Layer> result) {
            if ((layersList == null || layersList.contains(root)) && !result.contains(root)) {
                result.add(root);
            }

            Object obj = all.get(root);
            if (obj != null) {
                if (obj instanceof Layer) {
                    addToList((Layer) obj, all, result);
                } else {
                    List<Layer> list = (List<Layer>) obj;
                    Collections.sort(list, new Comparator<Layer>() {
                        @Override
                        public int compare(Layer o1, Layer o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    for (Layer layer : list) {
                        addToList(layer, all, result);
                    }
                }
            }
        }
    }

    public class Layers extends RadixObjects<Layer> {

        private Layers() {
            super(Branch.this);
        }
        private boolean loading = false;

        @Override
        protected void onAdd(Layer layer) {
            super.onAdd(layer);

            if (!loading && layer.getDirectory() != null) {
                try {
                    layer.save();
                } catch (IOException cause) {
                    throw new RadixObjectError("Unable to save layer.", layer, cause);
                }
            }
            layersChanged();
        }

        /**
         * Adds new layer to the top of the branch and save the layer.
         */
        public Layer addNew(String uri, String name, String title, String copyright, List<EIsoLanguage> languages) throws IOException {
            synchronized (this) {
                List<Layer> topLayers = findTop();
                Layer topLayer = null;
                if (!topLayers.isEmpty()) {
                    topLayer = topLayers.get(0);
                }

                Layer newLayer = Layer.Factory.newInstance();

                newLayer.setURI(uri);
                newLayer.setName(name);
                newLayer.setCopyright(copyright);
                newLayer.setLanguages(languages);
                newLayer.setTitle(title);
                if (topLayer != null) {
                    newLayer.addBaseLayer(topLayer);
                }

                add(newLayer);
                return newLayer;
            }
        }

        /**
         * Returns layer for given uri (if any) or null
         */
        public Layer findByURI(String uri) {
            if (uri == null) {
                return null;
            }
            synchronized (this) {
                for (Layer layer : this) {
                    if (uri.equals(layer.getURI())) {
                        return layer;
                    }
                }
                return null;
            }
        }

        public Layer addFromRepository(final IRepositoryLayer layerRepository) throws IOException {
            final Layer layer;
            try {
                layer = Layer.Factory.loadFromRepository(layerRepository);
                final String uri = layer.getURI();
                if (this.findByURI(uri) != null) {
                    throw new IllegalStateException("Layer '" + uri + "' is already loaded.");
                }
            } catch (IOException | IllegalStateException cause) {
                throw new IOException("Unable to load layer from '" + layerRepository.getPath() + "'", cause);
            }

            synchronized (this) {
                loading = true;
                try {
                    add(layer);
                    try {
                        layer.installExtensions();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } finally {
                    loading = false;
                }
            }

            return layer;
        }

        private void upload() {
            if (branchRepository != null) {
                IRepositoryLayer[] layers = branchRepository.listLayers();
                for (IRepositoryLayer layer : layers) {
                    try {
                        addFromRepository(layer);
                    } catch (IOException | RadixError error) {
                        branchRepository.processException(error);
                    }
                }
                layersChanged();
            }
        }

        /**
         * Builds list, containing product layers in order of their basement
         * from top layer down to bottom layer of the branch
         *
         * @return ordered list of layers (get(0) returns 'org.radixware');
         */
        public List<Layer> getInOrder() {
            return new LayerSorter(null).getInOrder();
        }

        /**
         * returns layers topologically ordered from top layers downto base
         */
        public List<Layer> getTopsFirst() {
            LinkedList<Layer> result = new LinkedList<>();

            synchronized (this) {
                for (Layer top : findTop()) {
                    if (!result.contains(top)) {
                        result.add(top);
                    }
                    List<Layer> prevLayers = top.listBaseLayers();

                    for (Layer layer : prevLayers) {
                        if (result.contains(layer)) {
                            break;
                        } else {
                            result.add(layer);
                        }
                    }
                }
                for (Layer layer : this) {
                    if (!result.contains(layer)) {
                        result.add(layer);
                    }
                }
            }

            return result;
        }

        /**
         * Returns top-level layer for the branch or null if cycling or no
         * layers.
         */
        public List<Layer> findTop() {
            // find layer on which no links.

             synchronized (this) {
                final List<Layer> tops = new LinkedList<>();
                for (Layer layer : this) {
			if (layer.isLocalizing()) {
                            continue;
                        }
                    final String uri = layer.getURI();

                    boolean isTopLayer = true;
                    for (Layer prevLayer : this) {
                        if (prevLayer.isLocalizing()) {
                            continue;
                        }
                        if (prevLayer != layer && prevLayer.getBaseLayerURIs().contains(uri)) {
                            isTopLayer = false;
                            break;
                        }
                    }
                    if (isTopLayer) {
                        tops.add(layer);
                    }
                }
                return tops;
            }
        }

        /**
         * Returns base development layer for the branch or null if branch is
         * empty or readonly
         */
        public Layer findBaseOfDevelopment() {
            return findByURI(Branch.this.baseDevelopmentLayerUri);
        }

        @Override
        protected boolean isPersistent() {
            return false;
        }

        public boolean isDistributable(Layer layer) {
            return false;
        }

        private void layersChanged() {
            for (Layer l : new ArrayList<>(this.list())) {
                l.layersChanged();
            }
        }
    }
//
//    public class ReleaseMessageLayerGroup {
//
//        private List<String> layers;
//
//        private ReleaseMessageLayerGroup(List<String> layers) {
//            this.layers = new ArrayList<String>(layers);
//        }
//
//        public boolean contains(String url) {
//            return layers != null && layers.contains(url);
//        }
//
//        public boolean isEmpty() {
//            return layers == null || layers.isEmpty();
//        }
//
//        public List<String> getLayerUrls() {
//            if (layers == null) {
//                return Collections.emptyList();
//            } else {
//                return Collections.unmodifiableList(layers);
//            }
//        }
//    }
//
//    public class ReleaseMessageLayerGroups {
//
//        private List<ReleaseMessageLayerGroup> groups;
//
//        public ReleaseMessageLayerGroup addNewGroup(List<String> layers) {
//            synchronized (this) {
//                ReleaseMessageLayerGroup group = new ReleaseMessageLayerGroup(layers);
//                if (groups == null) {
//                    groups = new LinkedList<ReleaseMessageLayerGroup>();
//                } else {
//                    List<ReleaseMessageLayerGroup> toRemove = new LinkedList<ReleaseMessageLayerGroup>();
//                    for (ReleaseMessageLayerGroup g : groups) {
//                        for (String url : layers) {
//                            if (g.contains(url)) {
//                                toRemove.add(g);
//                            }
//                        }
//                    }
//                    for (ReleaseMessageLayerGroup g : toRemove) {
//                        groups.remove(g);
//                    }
//                }
//                groups.add(group);
//                setEditState(EEditState.MODIFIED);
//                return group;
//            }
//        }
//
//        public void deleteGroup(ReleaseMessageLayerGroup g) {
//            synchronized (this) {
//                if (groups != null) {
//                    groups.remove(g);
//                    if (groups.isEmpty()) {
//                        groups = null;
//                    }
//                    setEditState(EEditState.MODIFIED);
//                }
//            }
//        }
//
//        public List<ReleaseMessageLayerGroup> getGroups() {
//            synchronized (this) {
//                if (groups == null) {
//                    return Collections.emptyList();
//                } else {
//                    return Collections.unmodifiableList(groups);
//                }
//            }
//        }
//
//        public boolean isEmpty() {
//            return groups == null || groups.isEmpty();
//        }
//
//        public boolean isGrouped(Layer layer) {
//            if (groups != null) {
//                for (ReleaseMessageLayerGroup g : groups) {
//                    if (g.contains(layer.getURI())) {
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//
//        public ReleaseMessageLayerGroup findGroupForLayer(Layer layer) {
//            if (groups != null) {
//                for (ReleaseMessageLayerGroup g : groups) {
//                    if (g.contains(layer.getURI())) {
//                        return g;
//                    }
//                }
//            }
//            return null;
//        }
//    }

    public static class Factory {

        /**
         * Create branch copy in new directory
         *
         * @throws java.io.IOException
         */
        public static Branch newInstance(File newBranchDirectory, Branch sourceBranch) throws IOException {
            Branch newBranch = new Branch(newBranchDirectory, sourceBranch);
            newBranch.save();
            return newBranch;
        }

        /**
         * Create new branch in directory and save it.
         *
         * @throws IOException
         */
        public static Branch newInstance(File branchDirectory, String repositoryUrl, String baseReleaseVersion, String lastReleaseVersion, ERepositoryBranchType type/*
                 * , final List<String> prevReleases
                 */) throws IOException {
            Branch branch = new Branch(branchDirectory, "", baseReleaseVersion, lastReleaseVersion, type, null, FORMAT_VERSION /*
                     * baseDistrUris
                     */);
            branch.save();
            return branch;
        }

        /**
         * Create new branch in memory
         */
        public static Branch newInstanceInMemory() {
            Branch branch = new Branch(null, null, null, null, null, null, FORMAT_VERSION);
            return branch;
        }

        public static Branch newInstanceInMemory(IRepositoryBranch repository) {
            Branch branch = new Branch(repository);
            return branch;
        }

        /**
         * Load branch from directory.
         *
         * @throws IOException
         */
        public static Branch loadFromDir(File branchDir) throws IOException {
            assert branchDir != null;

            Branch branch = new Branch(branchDir);
            branch.reloadDescription();
            return branch;
        }

        public static Branch loadFrom(InputStream branchXmlInputStream) throws XmlException, IOException {
            final org.radixware.schemas.product.Branch xBranch = org.radixware.schemas.product.BranchDocument.Factory.parse(branchXmlInputStream).getBranch();
            final Branch branch = new Branch((File) null);
            branch.loadFrom(xBranch);
            return branch;
        }
    }
    public static final String BRANCH_XML_FILE_NAME = "branch.xml";
    private String baseDevelopmentLayerUri;
    private String sourceBranch;
    private String baseReleaseVersion;
    private String lastReleaseVersion;
    private ERepositoryBranchType type;
    private IRepositoryBranch branchRepository;
    private List<String> baseDistUris;
    private final Layers layers = new Layers();
    private String title;
    private int formatVersion = 0;

    //private List<String> prevReleases;
    //private ReleaseMessageLayerGroups releaseMessageLayerGroups = null;
    protected Branch(File branchDirectory, String baseDevelopmentLayerUri, String baseReleaseVersion, String lastReleaseVersion, ERepositoryBranchType type, List<String> baseDistrUris, int formatVersion) {
        this.baseDevelopmentLayerUri = "".equals(baseDevelopmentLayerUri) ? null : baseDevelopmentLayerUri;
        this.sourceBranch = null;
        this.baseReleaseVersion = "".equals(baseReleaseVersion) ? null : baseReleaseVersion;
        this.lastReleaseVersion = "".equals(lastReleaseVersion) ? null : lastReleaseVersion;
        this.type = type;
        this.branchRepository = new FSRepositoryBranch(branchDirectory);
        this.baseDistUris = (baseDistrUris != null && !baseDistrUris.isEmpty() ? new LinkedList(baseDistrUris) : null);
        this.formatVersion = formatVersion;
        //this.prevReleases = (prevReleases != null ? new ArrayList<String>(prevReleases) : null);
    }

    private Branch(IRepositoryBranch rep) {
        this.baseDevelopmentLayerUri = null;
        this.sourceBranch = null;
        this.baseReleaseVersion = null;
        this.lastReleaseVersion = null;
        this.type = null;
        this.branchRepository = rep;

        //this.prevReleases = null;
    }

    protected Branch(File branchDirectory) {
        this(branchDirectory, null, null, null, null, null, FORMAT_VERSION);
    }

    protected Branch(File branchDirectory, Branch sourceBranch) {
        this(branchDirectory,
                sourceBranch.getBaseDevelopmentLayerUri(),
                sourceBranch.getBaseReleaseVersion(),
                sourceBranch.getLastReleaseVersion(),
                sourceBranch.getType(),
                sourceBranch.getBaseDistURIs(),
                sourceBranch.getFormatVersion());
    }

    private void loadFrom(org.radixware.schemas.product.Branch xBranch) {
        String xBaseDevelopmentLayerUri = xBranch.getBaseDevUri();
        String xBaseReleaseVersion = xBranch.getBaseRelease();
        String xLastReleaseVersion = xBranch.getLastRelease();
        String xSourceBranch = xBranch.getSrcBranch();

        this.baseDevelopmentLayerUri = "".equals(xBaseDevelopmentLayerUri) ? null : xBaseDevelopmentLayerUri;
        this.sourceBranch = "".equals(xSourceBranch) ? null : xSourceBranch;
        this.baseReleaseVersion = "".equals(xBaseReleaseVersion) ? null : xBaseReleaseVersion;
        this.lastReleaseVersion = "".equals(xLastReleaseVersion) ? null : xLastReleaseVersion;
        this.type = xBranch.getType();
        if (xBranch.isSetTitle()) {
            this.title = xBranch.getTitle();
        }
//        if (xBranch.getReleaseMessageLayerGroups() != null) {
//            this.releaseMessageLayerGroups = new ReleaseMessageLayerGroups();
//            for (List<String> list : xBranch.getReleaseMessageLayerGroups().getGroupList()) {
//                releaseMessageLayerGroups.addNewGroup(list);
//            }
//        } else {
//            releaseMessageLayerGroups = null;
//        }
        if (xBranch.isSetBaseDistUri() && !xBranch.getBaseDistUri().isEmpty()) {
            this.baseDistUris = new LinkedList<>(xBranch.getBaseDistUri());
        } else {
            this.baseDistUris = null;
        }
        if (xBranch.isSetFormatVersion()) {
            formatVersion = xBranch.getFormatVersion();
        }
    }

    public int getFormatVersion() {
        return formatVersion;
    }

    public void setFormatVersion(int formatVersion) {
        this.formatVersion = formatVersion;
    }

    public Layers getLayers() {
        synchronized (this) {
            if (layers.isEmpty()) {
                layers.upload();
            }
            return layers;
        }
    }

    @Override
    public File getDirectory() {
        return branchRepository.getDirectory();
    }

    /**
     * Get branch description file location or null if layer is not in branch.
     */
    @Override
    public File getFile() {
        return branchRepository.getDescriptionFile();
    }

    /**
     * Get base development layer URI for the branch. See also
     * {@linkplain #getBaseDevelopmentLayer}.
     */
    public String getBaseDevelopmentLayerUri() {
        return baseDevelopmentLayerUri;
    }

    /**
     * Get base development layer URI for the branch. See also
     * {@linkplain #getBaseDevelopmentLayer}.
     */
    public Layer getBaseDevelopmentLayer() {
        return baseDevelopmentLayerUri == null ? null : getLayers().findByURI(baseDevelopmentLayerUri);
    }

    /**
     * Sets base development layer for the branch.
     *
     * @return true if given layer is applyed as base development layer false
     * otherwise
     */
    public void setBaseDevelopmentLayer(Layer layer) {
        assert (layer != null);
        final String uri = layer.getURI();
        if (!Utils.equals(this.baseDevelopmentLayerUri, uri)) {
            this.baseDevelopmentLayerUri = layer.getURI();
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return URI of source development branch if current branch was create by
     * copying, null otherwise. For information purposes.
     */
    public String getSourceBranch() {
        return sourceBranch;
    }

    /**
     * Used for patches, to known release when patch was created.
     */
    public String getBaseReleaseVersion() {
        return baseReleaseVersion;
    }

    public void setBaseReleaseVersion(String baseReleaseRevision) {
        if (!Utils.equals(this.baseReleaseVersion, baseReleaseRevision)) {
            this.baseReleaseVersion = baseReleaseRevision;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Required to generate next release number. Required for release generator
     * to make difference from previous to current release (for svn copy).
     *
     * @return last release that created for this branch; null or empty string
     * if not defined.
     */
    public String getLastReleaseVersion() {
        return lastReleaseVersion;
    }

    public void setLastReleaseVersion(String lastReleaseVersion) {
        if (!Utils.equals(this.lastReleaseVersion, lastReleaseVersion)) {
            this.lastReleaseVersion = lastReleaseVersion;
            setEditState(EEditState.MODIFIED);
        }

    }

//    public List<String> getPrevReleases() {
//        return this.prevReleases;
//    }
    public IRepositoryBranch getRepository() {
        return branchRepository;
    }

    /**
     * Get branch name.
     *
     * @return name of branch directory.
     */
    @Override
    public String getName() {
        return this.branchRepository.getName();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (!Objects.equals(this.title, title)) {
            this.title = title;
            fireNameChange();
        }
    }

    /**
     * Branch type, used as description, defined by release, putch (etc)
     * generator.
     *
     * @return branch type
     */
    public ERepositoryBranchType getType() {
        return type;
    }

    public void reloadDescription() throws IOException {
        File file = branchRepository.getDescriptionFile();
        assert file != null;

        long fileTime = -1;
        if (file != null) {
            fileTime = file.lastModified();
        }
        final org.radixware.schemas.product.Branch xBranch;

        try {
            try (InputStream stream = branchRepository.getDescriptionData()) {
                xBranch = org.radixware.schemas.product.BranchDocument.Factory.parse(stream).getBranch();
            }
        } catch (XmlException cause) {
            throw new IOException(RadixResourceBundle.getMessage(Branch.class, "DevelopmentBranch-InvalidFileFormat-Exception", file.getParentFile().getPath()), cause);
        }

        loadFrom(xBranch);

        this.fileLastModifiedTime = fileTime;
        setEditState(EEditState.NONE);
    }

    /**
     * Save branch into its directory.
     *
     * @throws java.io.IOException
     */
    @Override
    public void save() throws IOException {
        File file = branchRepository.getDescriptionFile();

        if (file == null) {
            throw new IOException("Branch is not file-based");
        }

        BranchDocument branchDoc = BranchDocument.Factory.newInstance();
        org.radixware.schemas.product.Branch xBranch = branchDoc.addNewBranch();
        Saver saver = new Saver(this);
        saver.saveTo(xBranch);
//        xBranch.setBaseDevUri(this.baseDevelopmentLayerUri);
//        xBranch.setBaseRelease(this.baseReleaseVersion);
//        xBranch.setLastRelease(this.lastReleaseVersion);
//        xBranch.setSrcBranch(this.sourceBranch);
//        if (baseDistUris != null && !baseDistUris.isEmpty()) {
//            xBranch.setBaseDistUri(baseDistUris);
//        }
//        if (type != null) {
//            xBranch.setType(type);
//        }
////        xBranch.setPrevReleases(this.prevReleases);
//
//        if (releaseMessageLayerGroups != null && !releaseMessageLayerGroups.isEmpty()) {
//            org.radixware.schemas.product.Branch.ReleaseMessageLayerGroups xGroups = xBranch.addNewReleaseMessageLayerGroups();
//            for (ReleaseMessageLayerGroup list : releaseMessageLayerGroups.getGroups()) {
//                if (!list.isEmpty()) {
//                    org.radixware.schemas.product.Branch.ReleaseMessageLayerGroups.Group xGroup = xGroups.addNewGroup();
//                    xGroup.setListValue(list.getLayerUrls());
//                }
//            }
//        }

        synchronized (this) {
            XmlUtils.saveXmlPretty(branchDoc, file);//formatter is not used because of release making procedure compatibility requirements
            //XmlFormatter.save(branchDoc, file);
            fileLastModifiedTime = file.lastModified();
        }

        setEditState(EEditState.NONE);
    }

    @Override
    public boolean isSaveable() {
        return true;
    }
    long fileLastModifiedTime = 0L;

    public long getFileLastModifiedTime() {
        synchronized (this) { // wait for save
            return fileLastModifiedTime;
        }
    }

//    @Override
//    public String toString() {
//        StringBuilder result = new StringBuilder();
//
//
//        result.append("RadixWare Branch:                " + this.getName());
//        result.append("\n        Base Layer:             " + this.baseDevelopmentLayerUri);
//        result.append("\n        Base Release:          " + this.baseReleaseVersion);
//        result.append("\n        Repository Url:        " + this.repositoryUrl);
//        result.append("\n        Source Branch:         " + this.sourceBranch);
//        result.append("\n        Last Release Revision: " + String.valueOf(this.lastReleaseRevision));
//        result.append("\n        Type:                  " + String.valueOf(this.type));
//        result.append("\n        Location:              " + String.valueOf(this.branchDirectory.getPath()));
//        result.append("\n        Layers:\n");
//
//
//        List<Layer> order = getLayers().getInOrder();
//        for (Layer layer : order) {
//            result.append(layer.toString());
//            result.append('\n');
//        }
//
//        return result.toString();
//    }
    /**
     * Visits ordered list of layers. It is important that layers are visited in
     * ordered sequence from top to base
     */
    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        List<Layer> order = getLayers().getInOrder();
        for (int i = order.size() - 1; i >= 0; i--) {
            order.get(i).visit(visitor, provider);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.BRANCH;
    }
    public static final String BRANCH_TYPE_TITLE = "Branch";

    @Override
    public String getTypeTitle() {
        return BRANCH_TYPE_TITLE;
    }
    public static final String BRANCHES_TYPES_TITLE = "Branches";

    @Override
    public String getTypesTitle() {
        return BRANCHES_TYPES_TITLE;
    }

    public static boolean isBranchDir(File dir) {
        File file = new File(dir, BRANCH_XML_FILE_NAME);
        return file.isFile();
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }
//    public ReleaseMessageLayerGroups getReleaseMessageLayerGroups() {
//        synchronized (this) {
//            if (releaseMessageLayerGroups == null) {
//                releaseMessageLayerGroups = new ReleaseMessageLayerGroups();
//            }
//            return releaseMessageLayerGroups;
//        }
//    }
    private boolean closing = false;
    private final Object closingLock = new Object();

    public boolean isClosing() {
        return closing;
    }

    public void close() {
        final Layers layersCopy = getLayers();

        synchronized (closingLock) {
            try {
                closing = true;
                for (Layer layer : layersCopy) {
                    try {
                        layer.uninstallExtensions();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                // See RADIX-2044
                layersCopy.clear(); // close all editors, problems, caches, etc.
            } finally {
                closing = false;
            }
        }        
    }
    //do not use in designer
    public void cleanup(){
        //do nothing; set branch pointer to null will be ok
    }

    public List<String> getBaseDistURIs() {
        return this.baseDistUris == null ? Collections.emptyList() : new ArrayList(this.baseDistUris);
    }

    public void setBaseDistURIs(List<String> uris) {
        if (uris == null || uris.isEmpty()) {
            if (this.baseDistUris != null) {
                this.baseDistUris = null;
                setEditState(EEditState.MODIFIED);
            }
        } else {
            if (this.baseDistUris == null || !this.baseDistUris.equals(uris)) {
                this.baseDistUris = new LinkedList<>(uris);
                setEditState(EEditState.MODIFIED);
            }
        }
    }

    public static class Saver {

        private String baseDevelopmentLayerUri;
        private String baseReleaseVersion;
        private String lastReleaseVersion;
        private String sourceBranch;
        private List<String> baseDistUris;
        private int formatVersion;
        private ERepositoryBranchType type;
        private List<List<String>> releaseMessageLayerGroups;
        private String title;

        public Saver(Branch branch) {
            this.baseDevelopmentLayerUri = branch.baseDevelopmentLayerUri;
            this.baseReleaseVersion = branch.baseReleaseVersion;
            this.lastReleaseVersion = branch.lastReleaseVersion;
            this.sourceBranch = branch.sourceBranch;
            this.baseDistUris = branch.baseDistUris == null ? null : new ArrayList<>(branch.baseDistUris);
            this.type = branch.type;
            this.title = branch.getTitle();
            this.formatVersion = branch.formatVersion;
//            if (branch.releaseMessageLayerGroups != null) {
//                this.releaseMessageLayerGroups = new LinkedList<List<String>>();
//                for (ReleaseMessageLayerGroup g : branch.releaseMessageLayerGroups.getGroups()) {
//                    List<String> groupLayers = new LinkedList<String>();
//                    for (String uri : g.getLayerUrls()) {
//                        groupLayers.add(uri);
//                    }
//                    this.releaseMessageLayerGroups.add(groupLayers);
//                }
//            }
        }

        public Saver(org.radixware.schemas.product.Branch xBranch) {
            String xBaseDevelopmentLayerUri = xBranch.getBaseDevUri();
            String xBaseReleaseVersion = xBranch.getBaseRelease();
            String xLastReleaseVersion = xBranch.getLastRelease();
            String xSourceBranch = xBranch.getSrcBranch();

            this.baseDevelopmentLayerUri = "".equals(xBaseDevelopmentLayerUri) ? null : xBaseDevelopmentLayerUri;
            this.sourceBranch = "".equals(xSourceBranch) ? null : xSourceBranch;
            this.baseReleaseVersion = "".equals(xBaseReleaseVersion) ? null : xBaseReleaseVersion;
            this.lastReleaseVersion = "".equals(xLastReleaseVersion) ? null : xLastReleaseVersion;
            this.type = xBranch.getType();
            if (xBranch.getReleaseMessageLayerGroups() != null) {
                this.releaseMessageLayerGroups = new LinkedList<>();
                for (List<String> list : xBranch.getReleaseMessageLayerGroups().getGroupList()) {
                    releaseMessageLayerGroups.add(new ArrayList<>(list));
                }
            } else {
                releaseMessageLayerGroups = null;
            }
            if (xBranch.isSetBaseDistUri() && !xBranch.getBaseDistUri().isEmpty()) {
                this.baseDistUris = new LinkedList<>(xBranch.getBaseDistUri());
            } else {
                this.baseDistUris = null;
            }
        }

        public void setFormatVersion(int formatVersion) {
            this.formatVersion = formatVersion;
        }

        public void setBaseDevelopmentLayerUri(String baseDevelopmentLayerUri) {
            this.baseDevelopmentLayerUri = baseDevelopmentLayerUri;
        }

        public void setBaseReleaseVersion(String baseReleaseVersion) {
            this.baseReleaseVersion = baseReleaseVersion;
        }

        public void setLastReleaseVersion(String lastReleaseVersion) {
            this.lastReleaseVersion = lastReleaseVersion;
        }

        public void setSourceBranch(String sourceBranch) {
            this.sourceBranch = sourceBranch;
        }

        public void setType(ERepositoryBranchType type) {
            this.type = type;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public byte[] save() throws IOException {
            final BranchDocument xNewDoc = BranchDocument.Factory.newInstance();
            saveTo(xNewDoc.addNewBranch());
            return XmlUtils.saveXmlPretty(xNewDoc);
        }

        public void saveTo(org.radixware.schemas.product.Branch xBranch) {
            xBranch.setBaseDevUri(this.baseDevelopmentLayerUri);
            xBranch.setBaseRelease(this.baseReleaseVersion);
            xBranch.setLastRelease(this.lastReleaseVersion);
            xBranch.setSrcBranch(this.sourceBranch);
            xBranch.setFormatVersion(this.formatVersion);
            if (baseDistUris != null && !baseDistUris.isEmpty()) {
                xBranch.setBaseDistUri(baseDistUris);
            }
            if (type != null) {
                xBranch.setType(type);
            }

            if (releaseMessageLayerGroups != null && !releaseMessageLayerGroups.isEmpty()) {
                org.radixware.schemas.product.Branch.ReleaseMessageLayerGroups xGroups = xBranch.addNewReleaseMessageLayerGroups();
                for (List<String> list : releaseMessageLayerGroups) {
                    if (!list.isEmpty()) {
                        org.radixware.schemas.product.Branch.ReleaseMessageLayerGroups.Group xGroup = xGroups.addNewGroup();
                        xGroup.setListValue(list);
                    }
                }
            }
            if (title != null && !title.isEmpty()) {
                xBranch.setTitle(title);
            }
        }
    }
}
