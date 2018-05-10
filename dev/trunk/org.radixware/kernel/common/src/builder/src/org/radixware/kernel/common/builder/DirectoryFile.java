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
package org.radixware.kernel.common.builder;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.build.directory.DirectoryFileSigner;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.src.JavaFileSupport;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.product.Directory;
import org.radixware.schemas.product.Directory.FileGroups.FileGroup.GroupType.Enum;
import org.radixware.schemas.product.DirectoryDocument;

public class DirectoryFile {

    public static final class Factory {

        private Factory() {
            super();
        }

        public static DirectoryFile loadFrom(final File file) throws XmlException, IOException {
            final DirectoryDocument xDoc = DirectoryDocument.Factory.parse(file);
            return new DirectoryFile(xDoc);
        }

        public static DirectoryFile loadFrom(final DirectoryDocument xDoc) {
            return new DirectoryFile(xDoc);
        }

        public static DirectoryFile loadFrom(final Directory xDef) {
            return new DirectoryFile(xDef);
        }

        public static DirectoryFile loadFrom(final AdsModule module, boolean withSources) throws IOException {
            return new DirectoryFile((AdsModule) module, withSources);
        }

        public static DirectoryFile loadFrom(final Segment segment) {
            return new DirectoryFile(segment);
        }

        public static DirectoryFile createAdsDirectoryForRelease(final AdsSegment segment, Collection<AdsDirectoryReleaseData> data) {
            return new DirectoryFile(segment, data);
        }

        public static DirectoryFile loadFromMainFile(final Layer layer) {
            return new DirectoryFile(layer, true, null);
        }

        public static DirectoryFile loadFromDirectoryLayer(final Layer layer) {
            return new DirectoryFile(layer, false, null);
        }

        public static DirectoryFile loadFromDirectoryLayer(final Layer layer, byte[] newLayerXmlContent) {
            return new DirectoryFile(layer, false, newLayerXmlContent);
        }
//        public static DirectoryFile newDirectoryLayer(final byte[] layerXmlBytes) {
//            return new DirectoryFile(layerXmlBytes);
//        }
    }

    public static class DirInclude {

        private final transient String fileName;

        private DirInclude(final String fileName) {
            this.fileName = fileName;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof DirInclude) {
                return fileName.equals(((DirInclude) obj).fileName);
            } else {
                return false;
            }
        }

        public String getFileName() {
            return fileName;
        }
    }

    public static class DirFileGroup {

        private final transient Directory.FileGroups.FileGroup.GroupType.Enum type;
        private transient List<DirFile> files = null;

        private DirFileGroup(final Enum type) {
            this.type = type;
        }

        public void addFile(final DirFile file) {
            if (files == null) {
                files = new ArrayList<>();
            }
            files.add(file);
        }

        private void sort() {
            if (files != null) {
                Collections.sort(files, new Comparator<DirFile>() {
                    @Override
                    public int compare(DirFile t, DirFile t1) {
                        return t.name.compareTo(t1.name);
                    }
                });
            }
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof DirFileGroup) {
                DirFileGroup other = (DirFileGroup) obj;
                if (other.type != this.type) {
                    return false;
                }
                if (files == null) {
                    if (other.files != null) {
                        return false;
                    }
                } else {
                    if (other.files == null) {
                        return false;
                    }
                    int size = files.size();
                    if (other.files.size() != size) {
                        return false;
                    }
                    for (int i = 0; i < size; i++) {
                        if (!files.get(i).equals(other.files.get(i))) {
                            return false;
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public static class DirFile {

        private final transient String name;
        private transient byte[] digest;
        private transient String[] jarEntries;

        private DirFile(final Directory.FileGroups.FileGroup.File xFile) {
            this.name = xFile.getName();
            this.digest = xFile.getDigest();
            this.addJarContent(xFile);
        }

        private DirFile(final File homeDir, final File file) {
            this.name = getRelativePath(homeDir, file);
            final boolean isJar = name.toLowerCase().endsWith(".jar");
            final boolean isXml = isJar ? false : name.toLowerCase().endsWith(".xml");
            if (file.exists()) {
                try {
                    if (isJar) {
                        addJarContent(file);
                    }
                    this.digest = DirectoryFileSigner.calcFileDigest(file, isJar, isXml);
                } catch (NoSuchAlgorithmException | IOException ex) {
                    Logger.getLogger(DirectoryFile.class.getName()).log(Level.SEVERE, "Unable to update directory file", ex);
                }
            } else {
                this.digest = null;
            }
        }

        private DirFile(final String name, final byte[] content) {
            this.name = name;
            updateDigest(content);
        }

        private void updateDigest(byte[] content) {
            if (content != null) {
                try {
                    final File tmpFile = File.createTempFile("dgiest", "digest");
                    try {
                        try (FileOutputStream stream = new FileOutputStream(tmpFile)) {
                            stream.write(content);
                        }
                        try {
                            this.digest = DirectoryFileSigner.calcFileDigest(tmpFile, false, name.toLowerCase().endsWith(".xml"));
                        } catch (NoSuchAlgorithmException ex) {
                            this.digest = null;
                        }
                    } finally {
                        FileUtils.deleteFile(tmpFile);
                    }
                } catch (IOException ex) {
                    this.digest = null;
                }
            }
        }

        private void addJarContent(final File file) throws IOException {
            try (FileInputStream fileStream = new FileInputStream(file); ZipInputStream jar = new ZipInputStream(fileStream)) {
                final List<String> entries = new ArrayList<>();
                for (ZipEntry entry = jar.getNextEntry(); entry != null; entry = jar.getNextEntry()) {
                    if (!entry.isDirectory()) {
                        entries.add(entry.getName());
                    }
                }
                Collections.sort(entries, new Comparator<String>() {
                    @Override
                    public int compare(String t, String t1) {
                        return t.compareTo(t1);
                    }
                });
                this.jarEntries = new String[entries.size()];
                entries.toArray(this.jarEntries);
            }
        }

        private void addJarContent(final Directory.FileGroups.FileGroup.File xFile) {
            final List<Directory.FileGroups.FileGroup.File.Entry> entries = xFile.getEntryList();
            if (!entries.isEmpty()) {
                final List<String> entryNames = new ArrayList<String>(entries.size());

                for (Directory.FileGroups.FileGroup.File.Entry e : entries) {
                    entryNames.add(e.getName());
                }
                Collections.sort(entryNames, new Comparator<String>() {
                    @Override
                    public int compare(String t, String t1) {
                        return t.compareTo(t1);
                    }
                });
                this.jarEntries = new String[entries.size()];
                entryNames.toArray(this.jarEntries);
            }
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            } else if (obj instanceof DirFile) {
                DirFile other = (DirFile) obj;
                if (!name.equals(other.name)) {
                    return false;
                }
                if (digest == null) {
                    if (other.digest != null) {
                        return false;
                    }
                } else {
                    if (other.digest == null) {
                        return false;
                    } else {
                        if (!Arrays.equals(digest, other.digest)) {
                            return false;
                        }
                    }
                }
                if (jarEntries == null) {
                    if (other.jarEntries != null) {
                        return false;
                    }
                } else {
                    if (other.jarEntries == null) {
                        return false;
                    } else {
                        if (jarEntries.length != other.jarEntries.length) {
                            return false;
                        }
                        for (int i = 0; i < jarEntries.length; i++) {
                            if (!jarEntries[i].equals(other.jarEntries[i])) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }
    private transient List<DirInclude> includes = null;
    private transient Map<Directory.FileGroups.FileGroup.GroupType.Enum, DirFileGroup> groupsByType = null;

    private DirectoryFile(final DirectoryDocument xDoc) {
        this(xDoc.getDirectory());
    }

    private DirectoryFile(final Directory xDef) {
        if (xDef != null) {
            if (xDef.getIncludes() != null) {
                final List<Directory.Includes.Include> includesList = xDef.getIncludes().getIncludeList();
                if (!includesList.isEmpty()) {
                    this.includes = new ArrayList<>(includesList.size());
                    for (Directory.Includes.Include xInc : xDef.getIncludes().getIncludeList()) {
                        this.includes.add(new DirInclude(xInc.getFileName()));
                    }
                }
            }
            if (xDef.getFileGroups() != null) {
                final List<Directory.FileGroups.FileGroup> xGroups = xDef.getFileGroups().getFileGroupList();
                this.groupsByType = new HashMap<>();
                for (Directory.FileGroups.FileGroup xGroup : xGroups) {
                    final Directory.FileGroups.FileGroup.GroupType.Enum groupType = xGroup.getGroupType();
                    DirFileGroup group = this.groupsByType.get(groupType);
                    if (group == null) {
                        group = new DirFileGroup(groupType);
                        this.groupsByType.put(groupType, group);
                    }
                    final List<Directory.FileGroups.FileGroup.File> files = xGroup.getFileList();
                    for (Directory.FileGroups.FileGroup.File xFile : files) {
                        group.addFile(new DirFile(xFile));
                    }
                }
            }
        }
        sort();
    }

    private static FilenameFilter xmlFilter() {
        return new FilenameFilter() {
            @Override
            public boolean accept(final File parent, String name) {
                return name.endsWith(".xml") && !name.equals("directory.xml") && !name.equals(RadixdocConventions.RADIXDOC_XML_FILE);
            }
        };
    }
    
    private static FilenameFilter qtLinguistFilter() {
        return new FilenameFilter() {
            @Override
            public boolean accept(final File parent, String name) {
                return (name.endsWith(".qm") || name.endsWith(".ts")) && !name.equals("directory.xml") && !name.equals(RadixdocConventions.RADIXDOC_XML_FILE);
            }
        };
    }

    private static final Directory.FileGroups.FileGroup.GroupType.Enum getXmlGroupType(final ERuntimeEnvironmentType sc) {
        switch (sc) {
            case COMMON:
                return Directory.FileGroups.FileGroup.GroupType.ADS_COMMON;
            case SERVER:
                return Directory.FileGroups.FileGroup.GroupType.ADS_SERVER;
            case EXPLORER:
                return Directory.FileGroups.FileGroup.GroupType.ADS_EXPLORER;
            case COMMON_CLIENT:
                return Directory.FileGroups.FileGroup.GroupType.ADS_CLIENT;
            case WEB:
                return Directory.FileGroups.FileGroup.GroupType.ADS_WEB;
            default:
                throw new RadixError("Unsupported environment type");
        }
    }

    /**
     * no includes provided
     */
    private DirectoryFile(final AdsModule module, boolean withSources) throws IOException {

        final File adsModuleDir = module.getDirectory();
        DirFileGroup group = getGroup(Directory.FileGroups.FileGroup.GroupType.ADS_COMMON);
        addXmls(adsModuleDir, adsModuleDir, group);
        if (withSources) {
            final File srcDir = new File(adsModuleDir, AdsModule.SOURCES_DIR_NAME);
            if (srcDir.exists()) {
                addXmls(adsModuleDir, srcDir, group);
            }
            final File metaDir = new File(adsModuleDir, AdsModule.STRIP_SOURCES_DIR_NAME);
            if (metaDir.exists()) {
                addXmls(adsModuleDir, metaDir, group);
            }

            final File imgDir = new File(adsModuleDir, "img");
            if (imgDir.exists()) {
                final File imgJar = new File(imgDir, "img.jar");
                if (imgJar.exists()) {
                    addFile(adsModuleDir, imgJar, group);
                }
            }
        }

//        //locale
//        final File srcDir = new File(adsModuleDir, AdsModule.LOCALE_DIR_NAME);
//        if (srcDir.exists()) {
//            File[] locales = srcDir.listFiles(new FileFilter() {
//                @Override
//                public boolean accept(File pathname) {
//                    if (pathname.isDirectory()) {
//                        try {
//                            EIsoLanguage lang = EIsoLanguage.getForValue(pathname.getName());
//                            return true;
//                        } catch (NoConstItemWithSuchValueError e) {
//                            return false;
//                        }
//                    } else {
//                        return false;
//                    }
//                }
//            });
//            for (File locale : locales) {
//                addXmls(adsModuleDir, locale, group);
//            }
//        }
        for (ERuntimeEnvironmentType sc : ERuntimeEnvironmentType.values()) {
            group = getGroup(getXmlGroupType(sc));
            final File envJar = JavaFileSupport.getCompiledBinaryFile(module, sc);
            if (envJar != null && envJar.exists()) {
                addFile(adsModuleDir, envJar, group);
            }
            if (sc == ERuntimeEnvironmentType.COMMON) {
                final File binDir = JavaFileSupport.getBinDir(module);
                final File imgJar = new File(binDir, "img.jar");
                if (imgJar.exists()) {
                    addFile(adsModuleDir, imgJar, group);
                }
                File[] localeJars = binDir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        if (pathname.isDirectory()) {
                            return false;
                        } else {
                            final String name = pathname.getName();
                            if (name.startsWith("locale-") && name.endsWith(".jar")) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                });
                if (localeJars != null) {
                    for (File jar : localeJars) {
                        addFile(adsModuleDir, jar, group);
                    }
                }
            }
        }

        sort();
    }

    private DirectoryFile(final UdsModule module, boolean withSources) throws IOException {

        final File udsModuleDir = module.getDirectory();
        DirFileGroup group = getGroup(Directory.FileGroups.FileGroup.GroupType.ADS_COMMON);
        addXmls(udsModuleDir, udsModuleDir, group);
        if (withSources) {
            final File srcDir = new File(udsModuleDir, AdsModule.SOURCES_DIR_NAME);
            if (srcDir.exists()) {
                addXmls(udsModuleDir, srcDir, group);
            }

        }

        sort();
    }

    private DirectoryFile(final Layer layer, boolean main, byte[] newLayerXmlContent) {
        final File layerDir = layer.getDirectory();
        if (main) {
            for (ERepositorySegmentType st : ERepositorySegmentType.values()) {
                final File segmentDir = new File(layerDir, st.getValue());
                final File segmentIndexFile = new File(segmentDir, FileUtils.DIRECTORY_XML_FILE_NAME);
                if (segmentIndexFile.exists() && st != ERepositorySegmentType.UDS) {
                    if (includes == null) {
                        includes = new LinkedList<>();
                    }
                    includes.add(new DirInclude(getRelativePath(layerDir, segmentIndexFile)));
                }
            }
            final File dirLayerXmlFile = new File(layerDir, "directory-layer.xml");

            if (includes == null) {
                includes = new LinkedList<>();
            }
            includes.add(new DirInclude(getRelativePath(layerDir, dirLayerXmlFile)));

            if (layer.isLocalizing()) {
                File kernelDir = new File(layerDir, "kernel");
                if (kernelDir.exists() && kernelDir.isDirectory()) {
                    File explorerDir = new File(kernelDir, AdsModule.GEN_EXPLORER_DIR_NAME);
                    addQTFiles(Directory.FileGroups.FileGroup.GroupType.KERNEL_EXPLORER, layerDir, explorerDir);
                    
                    File webDir = new File(kernelDir, AdsModule.GEN_WEB_DIR_NAME);
                    addQTFiles(Directory.FileGroups.FileGroup.GroupType.KERNEL_WEB, layerDir, webDir);
                }
            }

        } else {
            final File layerXmlFile = new File(layerDir, Layer.LAYER_XML_FILE_NAME);
            if (layerXmlFile.exists()) {
                String[] xmls = layerXmlFile.getParentFile().list(xmlFilter());
                if (xmls != null) {
                    for (String xml : xmls) {
                        final boolean isDevLicenseFile;
                        if (FileUtils.LICENSES_XML_FILE_NAME.equals(xml)) {
                            isDevLicenseFile = !layer.isReadOnly();
                        } else {
                            isDevLicenseFile = false;
                        }
                        if (!FileUtils.DIRECTORY_XML_FILE_NAME.equals(xml) && !"directory-layer.xml".equals(xml) && !isDevLicenseFile) {
                            DirFile file = new DirFile(layerDir, new File(layerDir, xml));
                            getGroup(Directory.FileGroups.FileGroup.GroupType.KERNEL_COMMON).addFile(file);
                            if (newLayerXmlContent != null && Layer.LAYER_XML_FILE_NAME.equals(xml)) {
                                file.updateDigest(newLayerXmlContent);
                            }
                        }
                    }
                }
            }
        }
        sort();
    }

    private DirectoryFile(final byte[] bytes) {
        getGroup(Directory.FileGroups.FileGroup.GroupType.KERNEL_COMMON).addFile(new DirFile(Layer.LAYER_XML_FILE_NAME, bytes));
        sort();
    }

    private DirectoryFile(final Segment segment) {
        if (!segment.getModules().isEmpty()) {
            includes = new ArrayList<>(segment.getModules().size());
            for (Object obj : segment.getModules().list()) {
                Module module = (Module) obj;
                if (module.getDirectory() != null) {
                    File moduleDirFile = new File(module.getDirectory(), FileUtils.DIRECTORY_XML_FILE_NAME);
                    if (moduleDirFile.exists()) {
                        includes.add(new DirInclude(module.getName() + "/" + FileUtils.DIRECTORY_XML_FILE_NAME));
                    }
                }
            }
        }
        sort();
    }

    public final static class AdsDirectoryReleaseData {

        private final String name;
        private String prevReleaseName;
        private final Id id;
        private final boolean isUnderConstruction;

        public AdsDirectoryReleaseData(String name, Id id, boolean isUnderConstruction) {
            this.name = name;
            this.id = id;
            this.isUnderConstruction = isUnderConstruction;
        }

        public void setPrevReleaseName(String prevReleaseName) {
            this.prevReleaseName = prevReleaseName;
        }

        public String getName() {
            return name;
        }

        public String getPrevReleaseName() {
            return prevReleaseName;
        }

        public Id getId() {
            return id;
        }

        public boolean isUnderConstruction() {
            return isUnderConstruction;
        }

        @Override
        public String toString() {
            return name + /* + (isUnderConstruction ? "->" + String.valueOf(prevReleaseName) : "")+*/ " [" + id + "]";
        }
    }

    private DirectoryFile(final AdsSegment segment, Collection<AdsDirectoryReleaseData> data) {
        if (data != null && !data.isEmpty()) {
            includes = new ArrayList<>(data.size());

            for (final AdsDirectoryReleaseData moduleData : data) {
                if (moduleData.isUnderConstruction()) {
                    if (moduleData.getPrevReleaseName() != null && !moduleData.getPrevReleaseName().isEmpty()) {
                        includes.add(new DirInclude(moduleData.getPrevReleaseName() + "/" + FileUtils.DIRECTORY_XML_FILE_NAME));
                    }
                } else {
                    final AdsModule module = segment.getModules().findById(moduleData.getId());
                    if (module != null && Objects.equals(module.getName(), moduleData.getName())) {
                        if (module.getDirectory() != null) {
                            final File moduleDirFile = new File(module.getDirectory(), FileUtils.DIRECTORY_XML_FILE_NAME);
                            if (moduleDirFile.exists()) {
                                includes.add(new DirInclude(moduleData.getName() + "/" + FileUtils.DIRECTORY_XML_FILE_NAME));
                            }
                        }
                    }
                }
            }
        }
        sort();
    }

    private DirFileGroup getGroup(final Directory.FileGroups.FileGroup.GroupType.Enum groupType) {
        DirFileGroup group = null;
        if (groupsByType == null) {
            groupsByType = new HashMap<>();
        } else {
            group = this.groupsByType.get(groupType);
        }
        if (group == null) {
            group = new DirFileGroup(groupType);
            this.groupsByType.put(groupType, group);
        }
        return group;
    }

    private void addXmls(final File homeDir, final File lookupDir, final DirFileGroup group) throws IOException {
        final String[] xmls = lookupDir.list(xmlFilter());
        if (xmls != null) {
            for (int i = 0; i < xmls.length; i++) {
                final File xmlFile = new File(lookupDir, xmls[i]);
                addFile(homeDir, xmlFile, group);
            }
        }
    }

    private void addFile(final File homeDir, final File file, final DirFileGroup group) throws IOException {
        group.addFile(new DirFile(homeDir, file));
    }

    private static String getRelativePath(File home, File f) {
        final String result = FileUtils.getRelativePath(home, f);
        return result.replace('\\', '/'); // because of SVN operations
    }

    private void sort() {
        if (includes != null) {
            Collections.sort(includes, new Comparator<DirInclude>() {
                @Override
                public int compare(final DirInclude t, final DirInclude t1) {
                    return t.fileName.compareTo(t1.fileName);
                }
            });
        }
        if (groupsByType != null) {
            for (DirFileGroup g : groupsByType.values()) {
                g.sort();
            }
        }
    }

    public List<DirInclude> getIncludes() {
        if (includes == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(includes);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof DirectoryFile) {
            final DirectoryFile other = (DirectoryFile) obj;
            //check includes
            if (includes == null) {
                if (other.includes != null) {
                    return false;
                }
            } else {
                if (other.includes == null) {
                    return false;
                } else {
                    if (includes.size() != other.includes.size()) {
                        return false;
                    }
                    final int size = includes.size();
                    for (int i = 0; i < size; i++) {
                        if (!includes.get(i).equals(other.includes.get(i))) {
                            return false;
                        }

                    }
                }
            }
            //check file groups
            if (groupsByType == null) {
                if (other.groupsByType != null) {
                    return false;
                }
            } else {
                if (other.groupsByType == null) {
                    return false;
                }
                if (groupsByType.size() != other.groupsByType.size()) {
                    return false;
                }
                for (DirFileGroup group : groupsByType.values()) {
                    final DirFileGroup othergroup = other.groupsByType.get(group.type);
                    if (othergroup == null) {
                        return false;
                    }
                    if (!group.equals(othergroup)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void save(final File file) throws IOException {
        try (OutputStream stream = FileUtils.getOutputStreamNoLock(file)) {
            save(stream);
        }
    }

    public void save(final OutputStream stream) throws IOException {

        final DirectoryDocument xDoc = DirectoryDocument.Factory.newInstance();
        final Directory xDef = xDoc.addNewDirectory();
        if (includes != null && !includes.isEmpty()) {
            final Directory.Includes incs = xDef.addNewIncludes();
            for (DirInclude inc : includes) {
                incs.addNewInclude().setFileName(inc.fileName);
            }
        }
        long t = System.currentTimeMillis();
        if (groupsByType != null) {
            final Directory.FileGroups xGroups = xDef.addNewFileGroups();
            for (DirFileGroup group : groupsByType.values()) {
                final Directory.FileGroups.FileGroup xGroup = xGroups.addNewFileGroup();
                xGroup.setGroupType(group.type);
                if (group.files != null) {
                    for (DirFile groupFile : group.files) {
                        final Directory.FileGroups.FileGroup.File xFile = xGroup.addNewFile();
                        xFile.setName(groupFile.name);
                        xFile.setDigest(groupFile.digest);
                        if (groupFile.jarEntries != null) {
                            //------------------------------------------- old code (TOO slow)
//                            for (String e : groupFile.jarEntries) {
//                                xFile.addNewEntry().setName(e);
//                            }
                            //------------------------------------------- new code
                            final int len = groupFile.jarEntries.length;
                            Directory.FileGroups.FileGroup.File.Entry[] entries = new Directory.FileGroups.FileGroup.File.Entry[len];
                            for (int i = 0; i < len; i++) {
                                entries[i] = Directory.FileGroups.FileGroup.File.Entry.Factory.newInstance();
                                entries[i].setName(groupFile.jarEntries[i]);
                            }
                            xFile.setEntryArray(entries);
                            //------------------------------------------
                        }
                    }
                }
            }
        }
        XmlUtils.saveXmlPretty(xDoc, stream);
    }

    public byte[] getBytes() {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            save(stream);
            return stream.toByteArray();
        } catch (IOException ex) {
            return null;
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }
    
    private void addQTFiles(Directory.FileGroups.FileGroup.GroupType.Enum type, File layerDir, File dir) {
        if (dir.exists()) {
            String[] qtPaths = dir.list(qtLinguistFilter());
            for (String qtFile : qtPaths) {
                DirFile file = new DirFile(layerDir, new File(dir, qtFile));
                getGroup(type).addFile(file);
            }
        }
    }
}
