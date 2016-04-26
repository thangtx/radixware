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

package org.radixware.kernel.common.repository.ads.fs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.radixware.kernel.common.repository.fs.RepositoryInjection.ModuleInjectionInfo;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.dist.DistributionUtils;


class UpgradeInjection {

    private static class ZipFileModuleInjectionInfo extends ModuleInjectionInfo {

        private final String file;
        private final LayerInfo layer;
        private final boolean isPatch;

        public ZipFileModuleInjectionInfo(String name, String file, LayerInfo layer, boolean isPatch) {
            super(name);
            this.file = file;
            this.layer = layer;
            this.isPatch = isPatch;
        }

        private String getEntryName(String pathInModule) {
            if (isPatch) {
                return layer.uri + "/ads/" + getName() + "/" + pathInModule;
            } else {
                return FILES_LIST_PREFIX + layer.uri + "/ads/" + getName() + "/" + pathInModule;
            }
        }

        private InputStream getInputStream(ZipFile zip, ZipEntry e) throws IOException {
            InputStream in = zip.getInputStream(e);
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                FileUtils.copyStream(in, out);
                return new ByteArrayInputStream(out.toByteArray());
            } finally {
                in.close();
            }

        }

        private InputStream getFileData(String pathInModule) throws IOException {
            ZipFile zip = new ZipFile(file);
            try {
                String entryName = getEntryName(pathInModule);
                ZipEntry e = zip.getEntry(entryName);
                if (e == null) {
                    throw new IOException("No entry " + entryName + " found");
                }
                return getInputStream(zip, e);
            } finally {
                zip.close();
            }
        }

        @Override
        public InputStream getDescriptionFileStream() throws IOException {
            return getFileData(Module.MODULE_XML_FILE_NAME);
        }

        @Override
        public InputStream getDirectoryXmlFileStream() throws IOException {
            return getFileData(FileUtils.DIRECTORY_XML_FILE_NAME);
        }

        @Override
        public InputStream getAPIXmlFileStream() throws IOException {
            return getFileData(AdsModule.API_FILE_NAME);
        }

        @Override
        public InputStream getUsagesXmlFileStream() throws IOException {
            return getFileData(AdsModule.USAGES_FILE_NAME);
        }

        private String getJarPathInModule(String jarFileName) {
            return AdsModule.BINARIES_DIR_NAME + "/" + jarFileName;
        }

        @Override
        public InputStream getImgJarFileStream() throws IOException {
            return getFileData(getJarPathInModule(AdsModule.IMG_JAR_NAME));
        }

        @Override
        public InputStream getServerJarFileStream() throws IOException {
            return getFileData(getJarPathInModule(AdsModule.SERVER_JAR_NAME));
        }

        @Override
        public InputStream getExplorerJarFileStream() throws IOException {
            return getFileData(getJarPathInModule(AdsModule.EXPLORER_JAR_NAME));
        }

        @Override
        public InputStream getCommonJarFileStream() throws IOException {
            return getFileData(getJarPathInModule(AdsModule.COMMON_JAR_NAME));
        }

        @Override
        public ERepositorySegmentType getSegmentType() {
            return ERepositorySegmentType.ADS;
        }

        @Override
        public String getLayerURI() {
            return layer.uri;
        }
    }
    private File upgradeFile;

    public UpgradeInjection(File file) {
        this.upgradeFile = file;
    }
    private static final String FILES_LIST_PREFIX = "files/";

    private static class LayerInfo {

        private String uri;
        private String upgradeFile;
        private final Map<String, ModuleInjectionInfo> adsModules = new HashMap<String, ModuleInjectionInfo>();
        private final boolean isPatch;

        public LayerInfo(String uri, String upgradeFile, boolean isPatch) {
            this.uri = uri;
            this.upgradeFile = upgradeFile;
            this.isPatch = isPatch;

        }

        ModuleInjectionInfo getAdsModuleForName(String name) {
            ModuleInjectionInfo info = adsModules.get(name);
            if (info == null) {
                info = new ZipFileModuleInjectionInfo(name, upgradeFile, this, isPatch);
                adsModules.put(name, info);
            }
            return info;
        }
    }
    private Map<String, LayerInfo> layers = new HashMap<String, LayerInfo>();

    private LayerInfo getLayerByURI(String uri) {
        LayerInfo info = layers.get(uri);
        if (info == null) {
            info = new LayerInfo(uri, upgradeFile.getPath(), isPatch);
            layers.put(uri, info);
        }
        return info;
    }
    private boolean isPatch = false;

    public Collection<? extends ModuleInjectionInfo> prepare() throws IOException {
        ZipFile zip = new ZipFile(upgradeFile);
        try {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            final String adsSegmentName = ERepositorySegmentType.ADS.getValue();

            ZipEntry e = zip.getEntry(DistributionUtils.PATCH_XML_FILE_NAME);

            int nameLenMatch = 4;
            if (e != null) {
                isPatch = true;
                nameLenMatch = 3;
            }
            while (entries.hasMoreElements()) {
                e = entries.nextElement();
                String name = e.getName();
                if (name.startsWith(FILES_LIST_PREFIX) || isPatch) {//we are in file list
                    String[] names = name.split("/");
                    if (names.length >= nameLenMatch) {
                        String layerUri = names[nameLenMatch - 3];
                        String segment = names[nameLenMatch - 2];
                        String moduleName = names[nameLenMatch - 1];
                        if (adsSegmentName.equals(segment)) {
                            LayerInfo layer = getLayerByURI(layerUri);
                            ModuleInjectionInfo module = layer.getAdsModuleForName(moduleName);
                            if (names.length > nameLenMatch) {
                                if (names.length == nameLenMatch + 1) {
                                    String fileName = names[nameLenMatch];
                                    if (AdsModule.API_FILE_NAME.equals(fileName)) {
                                        module.setHasAPIXml(true);
                                    } else if (AdsModule.USAGES_FILE_NAME.equals(fileName)) {
                                        module.setHasUsagesXml(true);
                                    } else if (AdsModule.MODULE_XML_FILE_NAME.equals(fileName)) {
                                        module.setHasModuleXml(true);
                                    } else if (FileUtils.DIRECTORY_XML_FILE_NAME.equals(fileName)) {
                                        module.setHasDirectoryXml(true);
                                    }
                                } else if (names.length == nameLenMatch + 2) {
                                    String dirName = names[nameLenMatch];
                                    if (AdsModule.BINARIES_DIR_NAME.equals(dirName)) {
                                        String fileName = names[nameLenMatch + 1];
                                        if (AdsModule.COMMON_JAR_NAME.equals(fileName)) {
                                            module.setHasCommonJar(true);
                                        } else if (AdsModule.SERVER_JAR_NAME.equals(fileName)) {
                                            module.setHasServerJar(true);
                                        } else if (AdsModule.EXPLORER_JAR_NAME.equals(fileName)) {
                                            module.setHasExplorerJar(true);
                                        } else if (AdsModule.IMG_JAR_NAME.equals(fileName)) {
                                            module.setHasImgJar(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (Iterator<String> keys = layers.keySet().iterator(); keys.hasNext();) {
                String layerURI = keys.next();
                LayerInfo info = layers.get(layerURI);
                if (info.adsModules.isEmpty()) {
                    keys.remove();
                } else {
                    for (Iterator<String> names = info.adsModules.keySet().iterator(); names.hasNext();) {
                        String name = names.next();
                        ModuleInjectionInfo moduleInfo = info.adsModules.get(name);
                        if (!moduleInfo.hasImportantChanges()) {
                            names.remove();
                        }
                    }
                }
            }
            return computeInjections();
        } finally {
            zip.close();
        }
    }

    private Collection<? extends ModuleInjectionInfo> computeInjections() {
        List<ModuleInjectionInfo> info = new LinkedList<ModuleInjectionInfo>();
        for (Map.Entry<String, LayerInfo> e : layers.entrySet()) {
            System.out.println(e.getKey());
            for (ModuleInjectionInfo moduleInfo : e.getValue().adsModules.values()) {
                info.add(moduleInfo);
                System.out.println("  " + info);
            }
        }
        return info;
    }
}
