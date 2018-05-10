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
package org.radixware.kernel.designer.common.general.filesystem;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IDirectoryRadixObject;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsLocaleDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsModule;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;

public class RadixFileUtil {

    /**
     * Return FileObject for specified file.
     *
     * @return FileObject or null if file not exist.
     */
    public static FileObject toFileObject(final File file) {
        final File normalizedFile = FileUtil.normalizeFile(file); // required Netbeans, warning otherwise
        final FileObject fileObject = FileUtil.toFileObject(normalizedFile);
        return fileObject;
    }

    private static class FindRadixObjectVisitorProvider extends VisitorProvider {

        private final File file;
        RadixObject result = null;

        public FindRadixObjectVisitorProvider(File file) {
            this.file = file;
        }

        @Override
        public boolean isTarget(RadixObject radixObject) {
            if (radixObject instanceof IDirectoryRadixObject) {
                final File dir = ((IDirectoryRadixObject) radixObject).getDirectory();
                if (Utils.equals(dir, file)) {
                    result = radixObject;
                    return true;
                } else if (FileUtils.isParentOf(dir, file)) {
                    result = radixObject; // last appropriated
                }
            } else if (radixObject.isSaveable()) {
                if (radixObject instanceof AdsLocalizingBundleDef) {
                    AdsLocalizingBundleDef mlBundle = (AdsLocalizingBundleDef) radixObject;
                    if (mlBundle.getModule() != null && mlBundle.getModule().getRepository() != null) {
                        IRepositoryAdsModule rep = mlBundle.getModule().getRepository();
                        if (rep != null) {
                            IRepositoryAdsDefinition defRep = rep.getDefinitionRepository(mlBundle);
                            if (defRep != null && (defRep instanceof IRepositoryAdsLocaleDefinition)) {
                                if (loadLocalizingDefs((IRepositoryAdsLocaleDefinition) defRep)) {
                                    result = mlBundle;
                                    return true;
                                }
                            }
                        }
                    }
                }
                final File radixObjectFile = radixObject.getFile();
                if (Utils.equals(this.file, radixObjectFile)) {
                    result = radixObject;
                    return true;
                }
                if (AdsUtils.isEnableHumanReadable(radixObject)) {
                    File hrFile = AdsUtils.calcHumanReadableFile(radixObjectFile);
                    if (Utils.equals(this.file, hrFile)) {
                        result = radixObject;
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean loadLocalizingDefs(IRepositoryAdsLocaleDefinition defRep) {
            for (IRepositoryAdsDefinition localeDef : defRep.getRepositories().values()) {
                if (localeDef instanceof IRepositoryAdsLocaleDefinition) {
                    loadLocalizingDefs((IRepositoryAdsLocaleDefinition) localeDef);
                } else {
                    if (Utils.equals(this.file, localeDef.getFile())) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean isContainer(RadixObject radixObject) {
            if (radixObject instanceof IDirectoryRadixObject) {
                final File dir = ((IDirectoryRadixObject) radixObject).getDirectory();
                return FileUtils.isParentOf(dir, file);
            } else if (radixObject.isSaveable()) {
                return false;
            }
            return true;
        }
    }

    /**
     * File Radix object for specified file.
     *
     * @return Radix object or null if not found in opened projects.
     */
    public static RadixObject findRadixObject(File file) {
        for (Branch branch : getOpenedBranches()) {
            final FindRadixObjectVisitorProvider provider = new FindRadixObjectVisitorProvider(file);
            branch.find(provider);
            if (provider.result != null) {
                return provider.result;
            }
        }
        return null;
    }

    public static String getFileDisplayName(File file) {
        assert file != null;

        final RadixObject radixObject = findRadixObject(file);
        if (radixObject != null) {
            return radixObject.getName();
        } else {
            return file.getName();
        }
    }

    /**
     * File Radix object for specified fileObject.
     *
     * @return Radix object or null if not found in opened projects.
     */
    public static RadixObject findRadixObject(FileObject fileObject) {
        final File file = FileUtil.toFile(fileObject);
        return findRadixObject(file);
    }

    public static Collection<Branch> getOpenedBranches() {
        Collection<Branch> result = new ArrayList<Branch>();
        for (Project project : OpenProjects.getDefault().getOpenProjects()) {
            Branch branch = project.getLookup().lookup(Branch.class);
            if (branch != null) {
                result.add(branch);
            }
        }
        return result;
    }
    private static final String SELECTED_RADIX_OBJECT = "SELECTED_RADIX_OBJECT";

    /**
     * File Object can contains some RadixObjects. To realize navigation it is
     * possible to mark one of RadixObject in the FileObject.
     */
    public static final void setSelectedInFileObject(final RadixObject radixObject) {
        try {
            final File file = radixObject.getFile();
            if (file != null) {
                final FileObject fileObject = toFileObject(file);
                if (fileObject != null) {
                    fileObject.setAttribute(SELECTED_RADIX_OBJECT, radixObject);
                }
            }
        } catch (IOException cause) {
            throw new IllegalStateException(cause);
        }
    }

    /**
     * @see setSelectedRadixObject
     * @return RadixObject or null.
     */
    public static final RadixObject getLastSelectedRadixObject(final FileObject fileObject) {
        final RadixObject result = (RadixObject) fileObject.getAttribute(SELECTED_RADIX_OBJECT);
        return result;
    }

    public static boolean isDebugMode() {
        final String userDirPath = System.getProperty("user.dir");
        return userDirPath.endsWith("suite");
    }

    /**
     * @return directory if radix object is IDirectoryFileObject, or data files
     * otherwise
     */
    public static List<File> getVersioningFiles(RadixObject radixObject) {
        if (radixObject instanceof IDirectoryRadixObject) {
            final File dir = ((IDirectoryRadixObject) radixObject).getDirectory();
            if (dir != null) {
                return Collections.singletonList(dir);
            } else {
                return Collections.emptyList();
            }
        }

        final File file = radixObject.getFile();
        if (file != null) {
            final List<File> result = new ArrayList<>();
            result.add(file);
            if (radixObject instanceof AdsDefinition) {
                final AdsDefinition adsDefinition = (AdsDefinition) radixObject;
                    File hrFile = AdsUtils.calcHumanReadableFile(file);
                    if (hrFile.exists()) {
                        result.add(hrFile);
                }
                final AdsLocalizingBundleDef bundle = adsDefinition.findExistingLocalizingBundle();
                if (bundle != null) {
                    final List<File> bundleFiles = bundle.getFiles();
                    //check another locale files under module directory. Possibly there are deleted files
                    AdsModule module = adsDefinition.getModule();
                    File dir = module.getDirectory();
                    if (dir != null && dir.exists()) {
                        File localeDor = new File(dir, "locale");
                        File[] langFiles = localeDor.listFiles(new FileFilter() {

                            @Override
                            public boolean accept(File pathname) {
                                return pathname.isDirectory();
                            }
                        });
                        if (langFiles != null) {
                            final String fileName = bundle.getId().toString() + ".xml";
                            for (File langDir : langFiles) {
                                File[] added = langDir.listFiles(new FileFilter() {

                                    @Override
                                    public boolean accept(File pathname) {
                                        return fileName.equals(pathname.getName());
                                    }
                                });
                                if (added != null) {
                                    for (int i = 0; i < added.length; i++) {
                                        if (bundleFiles != null && bundleFiles.contains(added[i])) {
                                            continue;
                                        } else {
                                            bundleFiles.add(added[i]);
                                        }
                                    }
                                }
                            }
                        }

                    }
                    if (bundleFiles != null) {
                        result.addAll(bundleFiles);
                        return result;
                    }
                }
            }
            return result;
        }

        return Collections.emptyList();
    }

    /**
     * @return directory if radix object is IDirectoryFileObject, or data files
     * otherwise
     */
    public static List<FileObject> getVersioningFileObjects(RadixObject radixObject) {
        final List<File> files = getVersioningFiles(radixObject);
        final List<FileObject> result = new ArrayList<FileObject>(files.size());
        for (File file : files) {
            final FileObject fileObject = toFileObject(file);
            if (fileObject != null) {
                result.add(fileObject);
            }
        }
        return result;
    }

    public static File getUserDir() {
        final String userDirPath = System.getProperty("user.dir");
        return new File(userDirPath);
    }
    private static final FileSystem fileSystem;

    static {
        try {
            final File userDir = getUserDir();
            final FileObject fo = toFileObject(userDir);
            fileSystem = fo.getFileSystem();
        } catch (FileStateInvalidException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static FileSystem getFileSystem() {
        return fileSystem;
    }
}
