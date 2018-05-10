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
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.dds.DdsModelManager;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERadixIconType;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsScripts;
import org.radixware.kernel.common.utils.FileUtils;

public final class RadixFileInfo {

    private RadixFileInfo() {
    }

    public static boolean isBranchDir(File file) {
        return Branch.isBranchDir(file);
    }

    public static boolean isLayerDir(File file) {
        final File parent = file.getParentFile();
        return parent != null && Layer.isLayerDir(file) && isBranchDir(parent);
    }

    public static boolean isSegmentDir(File file, ERepositorySegmentType segmentType) {
        final File parent = file.getParentFile();
        return parent != null && segmentType.getValue().equals(file.getName()) && isLayerDir(parent);
    }

    public static boolean isDdsSegmentDir(File file) {
        return isSegmentDir(file, ERepositorySegmentType.DDS);
    }

    public static boolean isAdsSegmentDir(File file) {
        return isSegmentDir(file, ERepositorySegmentType.ADS);
    }

    public static boolean isUdsSegmentDir(File file) {
        return isSegmentDir(file, ERepositorySegmentType.UDS);
    }

    public static boolean isDdsModuleDir(File file) {
        final File parent = file.getParentFile();
        return parent != null && Module.isModuleDir(file) && isDdsSegmentDir(parent);
    }

    public static boolean isAdsModuleDir(File file) {
        final File parent = file.getParentFile();
        return parent != null && Module.isModuleDir(file) && isAdsSegmentDir(parent);
    }

    public static boolean isUdsModuleDir(File file) {
        final File parent = file.getParentFile();
        return parent != null && Module.isModuleDir(file) && isUdsSegmentDir(parent);
    }

    public static boolean isDdsScriptsDir(File file) {
        final File parent = file.getParentFile();
        return parent != null && DdsScripts.SCRIPTS_DIR_NAME.equals(file.getName()) && isDdsSegmentDir(parent);
    }

    public static boolean isDdsDatabaseScriptsDir(File file) {
        final File parent = file.getParentFile();
        return isDdsScriptsDir(file) || parent != null && isDdsScriptsDir(parent);
    }

    public static boolean isDdsDatabaseScriptsSubDir(File file) {
        final File parent = file.getParentFile();
        return parent != null && isDdsDatabaseScriptsDir(parent);
    }

    public static boolean isAdsSourcesDir(File file) {
        final File parent = file.getParentFile();
        return parent != null && AdsModule.SOURCES_DIR_NAME.equals(file.getName()) && isAdsModuleDir(parent);
    }

    public static boolean isAdsLocaleDir(File file) {
        final File parent = file.getParentFile();
        return parent != null && AdsModule.LOCALE_DIR_NAME.equals(file.getName()) && isAdsModuleDir(parent);
    }

    public static boolean isUdsLocaleDir(File file) {
        final File parent = file.getParentFile();
        return parent != null && AdsModule.LOCALE_DIR_NAME.equals(file.getName()) && isUdsModuleDir(parent);
    }

    public static boolean isAdsLangDir(File file) {
        try {
            final File parent = file.getParentFile();
            return parent != null && isAdsLocaleDir(parent) && EIsoLanguage.getForValue(file.getName()) != null;
        } catch (NoConstItemWithSuchValueError ex) {
            return false;
        }
    }

    public static boolean isUdsLangDir(File file) {
        try {
            final File parent = file.getParentFile();
            return parent != null && isUdsLocaleDir(parent) && EIsoLanguage.getForValue(file.getName()) != null;
        } catch (NoConstItemWithSuchValueError ex) {
            return false;
        }
    }

    public static boolean isUdsSourcesDir(File file) {
        final File parent = file.getParentFile();
        return parent != null && AdsModule.SOURCES_DIR_NAME.equals(file.getName()) && isUdsModuleDir(parent);
    }

    public static boolean isAdsImagesDir(File file) {
        final File parent = file.getParentFile();
        return parent != null && AdsModule.IMAGES_DIR_NAME.equals(file.getName()) && isAdsModuleDir(parent);
    }

    public static boolean isUdsDictionaryDir(File file) {
        final File parent = file.getParentFile();
        return "dictionaries".equals(file.getName()) && parent != null && isUdsSegmentDir(parent);
    }

    public static boolean isDdsLocaleDir(File file) {
        final File parent = file.getParentFile();
        return "locale".equals(file.getName()) && parent != null && isDdsModuleDir(parent);
    }

    private static boolean isDocDir(File file) {
        if (file == null) {
            return false;
        }
        final File parent = file.getParentFile();
        if (parent != null && isAdsSourcesDir(parent)) {
            return AdsModule.DOCUMENTATION_DIR_NAME.equals(file.getName()) && file.isDirectory();
        } else {
            return false;
        }
    }

    private static boolean isDocLangDir(File file) {
        try {
            final File parent = file.getParentFile();
            return parent != null && isDocDir(parent) && EIsoLanguage.getForValue(file.getName()) != null;
        } catch (NoConstItemWithSuchValueError ex) {
            return false;
        }
    }

    private static boolean isDocResourceDir(File file) {
        if (file == null) {
            return false;
        }
        final File parent = file.getParentFile();
        if (parent != null && isDocLangDir(parent)) {
            return AdsModule.DOCUMENTATION_RESOURCE_DIR_NAME.equals(file.getName()) && file.isDirectory();
        } else {
            return false;
        }
    }

    private static boolean isDirectoryShareable(File dir) {
        return isBranchDir(dir)
                || isLayerDir(dir)
                || isAdsSegmentDir(dir)
                || isDdsSegmentDir(dir)
                || isAdsModuleDir(dir)
                || isUdsSourcesDir(dir)
                || isUdsModuleDir(dir)
                || isUdsEtcDir(dir)
                || isInsideUdsEtcDir(dir)
                || isUdsSegmentDir(dir)
                || isAdsSourcesDir(dir)
                || isAdsLocaleDir(dir)
                || isAdsLangDir(dir)
                || isAdsImagesDir(dir)
                || isDdsModuleDir(dir)
                || isDdsScriptsDir(dir)
                || isDdsDatabaseScriptsDir(dir)
                || isDdsDatabaseScriptsSubDir(dir)
                || isUdsDictionaryDir(dir)
                || isUdsLocaleDir(dir)
                || isUdsLangDir(dir)
                || isDdsLocaleDir(dir)
                || isPreviewDir(dir)
                || isDocDir(dir)
                || isDocLangDir(dir)
                || isDocResourceDir(dir);
    }

    private static boolean isDdsLangFile(File file) {
        final String baseName = FileUtils.getFileBaseName(file);
        final String ext = FileUtils.getFileExt(file);
        try {
            return "xml".equals(ext) && EIsoLanguage.getForValue(baseName) != null;
        } catch (NoConstItemWithSuchValueError ex) {
            return false;
        }
    }

    private static boolean isFileShareable(File file) {
        final File parent = file.getParentFile();
        if (parent == null) {
            return false;
        }

        if (isAdsSourcesDir(parent)
                || isUdsSourcesDir(parent)
                || isAdsLocaleDir(parent)
                || isUdsLocaleDir(parent)
                || isDdsLangFile(file)
                || (isAdsLangDir(parent) && parent.getParentFile() != null && isAdsLocaleDir(parent.getParentFile()))
                || (isUdsLangDir(parent) && parent.getParentFile() != null && isUdsLocaleDir(parent.getParentFile()))) {
            final String ext = FileUtils.getFileExt(file);
            return "xml".equals(ext);
        }

        if (isAdsImagesDir(parent)) {
            final String ext = FileUtils.getFileExt(file);
            return "xml".equals(ext)
                    || ERadixIconType.GIF.getValue().equals(ext)
                    || ERadixIconType.JPG.getValue().equals(ext)
                    || ERadixIconType.PNG.getValue().equals(ext)
                    || ERadixIconType.SVG.getValue().equals(ext);
        }

        if (isBranchDir(parent)) {
            final String name = file.getName();
            return Branch.BRANCH_XML_FILE_NAME.equals(name);
        }

        if (isLayerDir(parent)) {
            final String name = file.getName();
            return Layer.LAYER_XML_FILE_NAME.equals(name)
                    || FileUtils.DIRECTORY_XML_FILE_NAME.equals(name)
                    || FileUtils.LICENSE_TXT_FILE_NAME.equals(name);
        }

        if (isDdsSegmentDir(parent)) {
            final String name = file.getName();
            return FileUtils.DIRECTORY_XML_FILE_NAME.equals(name) || FileUtils.LICENSE_TXT_FILE_NAME.equals(name);
        }
        if (isAdsSegmentDir(parent)) {
            final String name = file.getName();
            return FileUtils.LICENSE_TXT_FILE_NAME.equals(name);
        }
        if (isUdsSegmentDir(parent)) {
            final String name = file.getName();
            return FileUtils.LICENSE_TXT_FILE_NAME.equals(name);
        }
        if (isDdsModuleDir(parent)) {
            final String name = file.getName();
            return Module.MODULE_XML_FILE_NAME.equals(name)
                    || DdsModelManager.FIXED_MODEL_XML_FILE_NAME.equals(name)
                    || DdsModelManager.MODIFIED_MODEL_XML_FILE_NAME.equals(name)
                    || FileUtils.DIRECTORY_XML_FILE_NAME.equals(name)
                    || FileUtils.SQML_DEFINITIONS_XML_FILE_NAME.equals(name)
                    || FileUtils.LICENSE_TXT_FILE_NAME.equals(name);
        }

        if (isDdsScriptsDir(parent)) {
            final String name = file.getName();
            return DdsScripts.SCRIPTS_XML_FILE_NAME.equals(name);
        }

        if (isDdsDatabaseScriptsSubDir(parent)) {
            final String ext = FileUtils.getFileExt(file);
            return "sql".equals(ext);
        }

        if (isAdsModuleDir(parent)) {
            final String name = file.getName();
            return Module.MODULE_XML_FILE_NAME.equals(name)
                    || FileUtils.LICENSE_TXT_FILE_NAME.equals(name);
        }
        if (isUdsModuleDir(parent)) {
            final String name = file.getName();
            return Module.MODULE_XML_FILE_NAME.equals(name) || (UdsModule.ETC_DIR_NAME.equals(name) && file.isDirectory()) || FileUtils.LICENSE_TXT_FILE_NAME.equals(name);
        }
        if (isInsideUdsEtcDir(file)) {
            return true;
        }

        if (isUdsDictionaryDir(parent)) {
            final String name = file.getName();
            if ("common.xml".equals(name)) {
                return true;
            }
            for (EIsoLanguage lang : EIsoLanguage.values()) {
                if ((lang.getValue() + ".xml").equals(name)) {
                    return true;
                }
            }
        }

        if (isPreviewDir(parent)) {
            final String ext = FileUtils.getFileExt(file);
            return "java".equals(ext);
        }

        if (isDocDir(parent) || isDocLangDir(parent)) {
            final String ext = FileUtils.getFileExt(file);
            return "xml".equals(ext);
        }
        
        if (isDocResourceDir(parent)) {
            return true;
        }
        return false;
    }

    private static boolean isUdsEtcDir(File file) {
        if (file == null) {
            return false;
        }
        final File parent = file.getParentFile();
        if (parent != null && isUdsModuleDir(parent)) {
            return UdsModule.ETC_DIR_NAME.equals(file.getName()) && file.isDirectory();
        } else {
            return false;
        }
    }

    private static boolean isPreviewDir(File file) {
        if (file == null) {
            return false;
        }
        final File parent = file.getParentFile();
        if (parent != null && isAdsSourcesDir(parent)) {
            return Module.PREVIEW_DIR_NAME.equals(file.getName()) && file.isDirectory();
        } else {
            return false;
        }
    }

    private static boolean isInsideUdsEtcDir(File file) {
        if (file == null) {
            return false;
        }
        final File parent = file.getParentFile();
        return parent != null && isUdsEtcDir(parent) || isInsideUdsEtcDir(parent);
    }

    public static boolean isShareable(File file) {
        if (file.isDirectory()) {
            return isDirectoryShareable(file);
        } else if (file.isFile()) {
            return isFileShareable(file);
        } else {
            return false;
        }
    }
}
