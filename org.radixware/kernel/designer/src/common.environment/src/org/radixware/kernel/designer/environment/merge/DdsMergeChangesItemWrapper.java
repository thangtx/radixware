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
package org.radixware.kernel.designer.environment.merge;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.swing.Icon;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.constants.FileNames;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsModelManager;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SvnEntryComparator;
import org.radixware.kernel.common.svn.SvnPathUtils;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Reference;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.tree.actions.dds.DdsModuleCaptureAction;

public class DdsMergeChangesItemWrapper {

    private final File srcModuleDir;
    private final File destModuleDir;
    private final DdsModule srcModule;
    private final File fromFile;

    private final File fromBranchFile;
    private final File toBranchFile;
    private final DdsMergeChangesOptions options;

    private DdsMergeChangesItemWrapper(final DdsModule srcModule, final File srcModuleDir, final File destModuleDir, final File srcFile,
            final File fromBranchFile, final File toBranchFile, final DdsMergeChangesOptions options) {
        this.srcModuleDir = srcModuleDir;
        this.destModuleDir = destModuleDir;
        this.srcModule = srcModule;
        this.fromFile = srcFile;
        this.fromBranchFile = fromBranchFile;
        this.toBranchFile = toBranchFile;
        this.options = options;
    }

    private Boolean mayCopy = null;
    private Boolean mayMerge = null;
    private Boolean equal = null;
    private Integer srcChangesLevel = null;

    private boolean isDone = false;
    private boolean isCaptured = false;

    public boolean isCaptured() {
        return isCaptured;
    }

    public void setCaptured() {
        this.isCaptured = true;
    }

    public int getSrcChangesLevel() {
        try {
            check();
        } catch (IOException | RadixSvnException  ex) {
            MergeUtils.messageError(ex);
            srcChangesLevel = 0;
        }
        return srcChangesLevel;
    }

    public boolean isMayCopy() {
        try {
            check();
        } catch (IOException | RadixSvnException  ex) {
            MergeUtils.messageError(ex);
            mayCopy = false;
        }
        return mayCopy;
    }

    public boolean isEqual() {
        try {
            check();
        } catch (IOException | RadixSvnException ex) {
            MergeUtils.messageError(ex);
            equal = false;
        }
        return equal;
    }

    public boolean isMayMerge() {
        try {
            check();
        } catch (IOException | RadixSvnException ex) {
            MergeUtils.messageError(ex);
            mayMerge = false;
        }
        return mayMerge;
    }

    public void setMayCopy(final boolean mayCopy) {
        this.mayCopy = mayCopy;
    }

    public void setMayMerge(final boolean mayMerge) {
        this.mayMerge = mayMerge;
    }

    public void setEquals(final boolean equal) {
        this.equal = equal;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone() {
        this.isDone = true;
        mayCopy = false;
        mayMerge = false;
        equal = true;
    }

    public String getFromPath() throws ISvnFSClient.SvnFsClientException, URISyntaxException {
        final String fromFileUrl = SVN.getFileUrl(options.getFsClient(), fromFile.getParentFile());
        URI uri = new URI(fromFileUrl);
        final String pathFromFile = SvnPathUtils.removeHead(uri.getPath()) + "/" + fromFile.getName();
        return pathFromFile;
    }

    public String getToPath() throws ISvnFSClient.SvnFsClientException, URISyntaxException {
        final String pathFromFile = getFromPath();
        final String pathToFile = options.getToPreffix() + pathFromFile.substring(options.getFromPreffix().length());
        return pathToFile;
    }

    boolean checked = false;

    String xmlStringDataEmulator = null;

    public String getXmlStringDataEmulator() throws IOException, RadixSvnException, ISvnFSClient.SvnFsClientException {
        check();
        return xmlStringDataEmulator;
    }

    public void getDone() {
        if (checked) {
            checked = false;
        }
    }

    boolean isMlb() {
        final String name = getSrcFile().getName();
        return !FileNames.DDS_MODEL_XML.equals(name) && !FileNames.DDS_MODULE_XML.equals(name);
    }

    private byte[] newModelXmlVersion;

    public byte[] getNewModelXmlVersion() {
        return newModelXmlVersion;
    }

    public void setNewModelXmlVersion(final byte[] newModelXmlVersion) {
        this.newModelXmlVersion = newModelXmlVersion;
    }

    boolean isModel() {
        final String name = getSrcFile().getName();
        return FileNames.DDS_MODEL_XML.equals(name);
    }

    private boolean isNewModel = false;

    boolean isNewModel() {
        return isNewModel;
    }

    void setAsNewModel() {
        isNewModel = true;
    }

    private void check() throws IOException, RadixSvnException, ISvnFSClient.SvnFsClientException {
        if (checked) {
            return;
        }
        checked = true;

        final String srcFileAsStr = FileUtils.readTextFile(fromFile, FileUtils.XML_ENCODING);
        xmlStringDataEmulator = srcFileAsStr;

        if (!destModuleDir.exists()) {
            mayCopy = true;
            mayMerge = false;
            equal = false;
            srcChangesLevel = 0;
            return;
        }
        final File destFile = getDestFile();
        if (!destFile.exists()) {
            mayCopy = true;
            mayMerge = false;
            equal = false;
            srcChangesLevel = 0;
            return;
        }

        final String destAsStr = FileUtils.readTextFile(destFile, FileUtils.XML_ENCODING);

        if (Utils.equals(srcFileAsStr, destAsStr)) {
            mayCopy = false;
            mayMerge = false;
            equal = true;
            srcChangesLevel = 0;
            return;
        }

//        final SVNURL fromFileUrl = SVN.getFileUrl(fromFile.getParentFile());
//        final String pathFromFile = SvnPathUtils.removeHead(fromFileUrl.getPath()) + "/" + fromFile.getName();
        try {
            final String pathFromFile = getFromPath();
            final String pathToFile = getToPath();

            srcChangesLevel = SvnEntryComparator.equalsToThePreviousVersions(options.getRepository(), pathToFile, pathFromFile);

            mayCopy = srcChangesLevel > 0;
            mayMerge = true;
            equal = false;
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }

        //SvnEntryComparator.EntrySizeAndEntrySha1Digest srcEntry = SvnEntryComparator.getEntrySizeAndEntrySha1DigestByString(srcFileAsStr);
        //byte bytes[] = srcEntry.entryDigest;
        //                           SvnEntryComparator.EntrySizeAndEntrySha1Digest fromEntrySha1Digest = SvnEntryComparator.getEntrySizeAndEntrySha1DigestByString(toXmlAsString);
//                            SvnEntryComparator.EntrySizeAndEntrySha1Digest toEntrySha1Digest = SvnEntryComparator.getEntrySizeAndEntryTopSha1Digest(options.getRepository(), item.getToPath());        
//        
//        
//        
//        SvnEntryComparator.EntrySizeAndEntrySha1Digest toEntry = SvnEntryComparator.getEntrySizeAndEntryTopSha1Digest(options.getRepository(), lngItem.toPath);
//        MergeUtils.canMergeAds();
    }

    public DdsModule getSrcDdsModule() {
        return srcModule;
    }

    public File getSrcModuleDir() {
        return srcModuleDir;
    }

    public File getDestModuleDir() {
        return destModuleDir;
    }

    public boolean isDestModuleExists() {
        return destModuleDir.exists();
    }

    String getWrapperName() {
        if (isLocale()) {
            try {
                return EIsoLanguage.getForValue(fromFile.getName().replaceFirst("[.][^.]+$", "")).getName();
            } catch (Throwable th) {
                return fromFile.getName();
            }
        }
        return srcModule.getName();
    }

    private String calculateDestinationFilePath() {
        final String srcBranchPath = this.fromBranchFile.getAbsolutePath();
        final String destBranchPath = this.toBranchFile.getAbsolutePath();
        final String path = fromFile.getAbsolutePath();
        return destBranchPath + path.substring(srcBranchPath.length());
    }

    public String getRelativeFilePath() {
        final String srcBranchPath = this.fromBranchFile.getAbsolutePath();
        final String definitionPath = fromFile.getAbsolutePath();
        return definitionPath.substring(srcBranchPath.length());
    }

    public File getSrcFile() {
        return fromFile;
    }

    public File getDestFile() {
        return new File(calculateDestinationFilePath());
    }

    Icon getIcon() {
        if (isLocale()) {
            return AdsDefinitionIcon.LOCALIZING_BUNDLE.getIcon();
        }
        return srcModule.getIcon().getIcon();
    }

    boolean isLocale() {
        return FileNames.DDS_LOCALE_DIR.equals(fromFile.getParentFile().getName());
        //return !Utils.equals(srcModule.getFile(), srcFile);
    }

//    protected static void doWithFile(final DdsModule module, final List<DdsMergeChangesItemWrapper> addedItems, final List<File> incorrectFiles, final File destBranchFile, final boolean destBranchFormatIsNew) throws XmlException, IOException{
//        //
//    }
    private static File srcModureAsDir(final DdsModule srcModule) {
        final String modulePath = srcModule.getFile().getParentFile().getAbsolutePath();
        return new File(modulePath);
    }

    private static File destModureAsDir(final DdsModule srcModule, final File destBranchFile) {
        final String layerPath = destBranchFile.getAbsolutePath() + File.separator + srcModule.getSegment().getLayer().getFile().getParentFile().getName();
        final String modulePath = layerPath + File.separator + FileNames.DDS_SEGMENT_NAME + File.separator + srcModule.getFile().getParentFile().getName();
        return new File(modulePath);
    }

    private static boolean isCorrectSvnStatus(final ISvnFSClient repository, final File fileOrDir, final Reference<File> firstIncorrectFileOrDir) {
        if (".svn".equals(fileOrDir.getName())) { // RADIX-10076
            return true;
        }

        if (!SVN.isNormalSvnStatus(repository, fileOrDir) && !SVN.getSvnFileStatusType(repository, fileOrDir).isNone()) {
            firstIncorrectFileOrDir.set(fileOrDir);
            return false;
        }

        if (fileOrDir.isDirectory()) {
            for (File childFile : fileOrDir.listFiles()) {
                if (!isCorrectSvnStatus(repository, childFile, firstIncorrectFileOrDir)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isCapturedStructure(final File moduleDir) {
        return (new File(moduleDir.getAbsolutePath() + "/" + FileNames.DDS_MODEL_MODIFIED_XML).exists());
    }

    private static File moduleToModel(final File module) {
        return new File(module.getParentFile().getAbsolutePath() + "/" + FileNames.DDS_MODEL_XML);
    }

    protected static void doWithModule(final DdsMergeChangesOptions options, final DdsModule module, final List<DdsMergeChangesItemWrapper> addedItems, final List<File> incorrectFiles, final File destBranchFile, final boolean destBranchFormatIsNew) throws XmlException, IOException {

        final File srcModuleAsDir = srcModureAsDir(module);
        if (!module.isCapturedStructure()) {
            incorrectFiles.add(srcModuleAsDir);
            return;
        }

        final Reference<File> firstIncorrectFileOrDir = new Reference();
        if (!isCorrectSvnStatus(options.getFsClient(), srcModuleAsDir, firstIncorrectFileOrDir)) {
            incorrectFiles.add(firstIncorrectFileOrDir.get());
            return;
        }

        final File destModuleAsDir = destModureAsDir(module, destBranchFile);
        if (destModuleAsDir.exists()) {
            if (isCapturedStructure(destModuleAsDir)) {
                incorrectFiles.add(destModuleAsDir);
                return;
            }

            if (!isCorrectSvnStatus(options.getFsClient(), destModuleAsDir, firstIncorrectFileOrDir)) {
                incorrectFiles.add(firstIncorrectFileOrDir.get());
                return;
            }
        }

        final DdsMergeChangesItemWrapper wrapper = new DdsMergeChangesItemWrapper(module, srcModuleAsDir, destModuleAsDir, moduleToModel(module.getFile()), options.getFromBranchFile(), options.getToBranchFile(), options);

        //wrapper.
        addedItems.add(wrapper);

        final File localeDir = new File(srcModuleAsDir.getAbsolutePath() + "/" + FileNames.DDS_LOCALE_DIR);
        if (localeDir.exists() && localeDir.isDirectory()) {
            for (File localeFile : localeDir.listFiles()) {
                final DdsMergeChangesItemWrapper localeWrapper = new DdsMergeChangesItemWrapper(module, srcModuleAsDir, destModuleAsDir, localeFile, options.getFromBranchFile(), options.getToBranchFile(), options);
                //if (localeWrapper.tryCaptureDestinationModule())
                {
                    addedItems.add(localeWrapper);
                }
            }

        }

    }

    protected boolean tryCaptureDestinationModule() throws IOException, XmlException {
        if (!getDestModuleDir().exists()) {
            return true;
        }

        if (isCapturedStructure(this.getDestModuleDir())) {
            return true;
        }

        final File fixedModelFile = new File(getDestModuleDir().getAbsolutePath() + "/" + FileNames.DDS_MODEL_XML);
        if (!fixedModelFile.exists()) {
            return false;
        }

        final DdsModuleCaptureAction.Cookie Cookie = new DdsModuleCaptureAction.Cookie(DdsModelManager.Support.loadAndGetDdsModule(fixedModelFile));
        Cookie.run();

        return true;
    }

}
