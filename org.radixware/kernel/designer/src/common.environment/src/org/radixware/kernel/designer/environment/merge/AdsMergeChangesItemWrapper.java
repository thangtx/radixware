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
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SvnEntryComparator;
import org.radixware.kernel.common.svn.SvnPathUtils;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.utils.Reference;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;

public class AdsMergeChangesItemWrapper extends RadixObject {

    public AdsMergeChangesItemWrapper(final Definition def, final String fileTitle) {
        this.def = def;
        this.fileTitle = fileTitle;
    }

    @Override
    public String getQualifiedName() {
        return def.getQualifiedName();
    }
    private Definition def;
    private int copyLevel;
    private boolean mayMerge;
    private boolean equals;
    private boolean done;
    private boolean fromNotExistsAndToExists = false;
    private String fromPath;
    private String toPath;
    private File fromFile;
    private File toFile;
    private final String fileTitle;
    private EIsoLanguage language;
    private boolean isPreview;

    public boolean isPreview() {
        return isPreview;
    }

    public void setIsPreview(boolean isPreview) {
        this.isPreview = isPreview;
    }
    
    public String getFileTitle() {
        return fileTitle;
    }
    private String xmlStringDataEmulator;

    public String getXmlStringDataEmulator() {
        return xmlStringDataEmulator;
    }

    public void setXmlStringDataEmulator(String xmlStringDataEmulator) {
        this.xmlStringDataEmulator = xmlStringDataEmulator;
    }
    private boolean mlb;

    public boolean isMlb() {
        return mlb;
    }

    public void setMlb(final boolean Mlb) {
        this.mlb = Mlb;
    }

    public EIsoLanguage getLanguage() {
        return language;
    }

    public void setLanguage(final EIsoLanguage language) {
        this.language = language;
    }
    private List<MergeChangesLangugeItemWrapper> newMlbItems = new ArrayList();

    public List<MergeChangesLangugeItemWrapper> getNewMlbItems() {
        return newMlbItems;
    }

    public void setNewMlbItems(final List<MergeChangesLangugeItemWrapper> newMlbItems) {
        this.newMlbItems = newMlbItems;
    }

    public MergeChangesLangugeItemWrapper findLangugeItem(final EIsoLanguage language) {
        for (MergeChangesLangugeItemWrapper lngItem : newMlbItems) {
            if (lngItem.language.equals(language)) {
                return lngItem;
            }
        }
        return null;
    }

    public void appendLangugeItem(final MergeChangesLangugeItemWrapper lngItem) {
        newMlbItems.add(lngItem);
    }

    public boolean isExistsOneAdditionMlbFile(final boolean from) {
        for (MergeChangesLangugeItemWrapper lngItem : newMlbItems) {
            if (from) {
                if (lngItem.fromMlbFile.exists()) {
                    return true;
                }
            } else {
                if (lngItem.toMlbFile.exists()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static String removeHead(final URI uri, final AdsMergeChangesOptions options) throws RadixSvnException {
        final String dir1 = uri.toString();
        final String dir2 = options.getRepository().getRepositoryRoot();
        
        final int len2 = dir2.length();
        
        final String dir3 = dir1.substring(len2);
        final String dir4 = /*SvnPathUtils.removeHead*/(dir3);
        return dir4;
    }

    static public class MergeChangesLangugeItemWrapper {

        public MergeChangesLangugeItemWrapper(EIsoLanguage language) {
            this.language = language;
        }
        public EIsoLanguage language;
        public File fromMlbFile;
        public String fromPath;
        public File toMlbFile;
        public String toPath;
    }
    private String mlbShortName;

    public String getMlbShortName() {
        return mlbShortName;
    }

    public void setMlbShortName(final String mlbShortName) {
        this.mlbShortName = mlbShortName;
    }

    public File getSrcFile() {
        return fromFile;
    }

    public void setSrcFile(File fromFile) {
        this.fromFile = fromFile;
    }

    public File getDestFile() {
        return toFile;
    }

    public void setDestFile(File toFile) {
        this.toFile = toFile;
    }
    private File fromLocale;
    private File toLocale;

    public File getFromLocale() {
        return fromLocale;
    }

    public void setFromLocale(final File fromLocale) {
        this.fromLocale = fromLocale;
    }

    public File getToLocale() {
        return toLocale;
    }

    public void setToLocale(final File toLocale) {
        this.toLocale = toLocale;
    }

    public String getFromPath() {
        return fromPath;
    }

    public void setFromPath(final String fromPath) {
        this.fromPath = fromPath;
    }

    public String getToPath() {
        return toPath;
    }

    public void setToPath(final String toPath) {
        this.toPath = toPath;
    }

    public Definition getDef() {
        return def;
    }

    public boolean isMayCopy() {
        return copyLevel != SvnEntryComparator.NOT_MAY_COPY && copyLevel != 0;
    }

    public int getSrcChangesLevel() {
        return copyLevel;
    }

    public boolean isMayMerge() {
        return mayMerge;
    }

    public void setSrcChangesLevel(final int level) {
        this.copyLevel = level;
    }

    public void setMayMerge(final boolean mayMerge) {
        this.mayMerge = mayMerge;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }

    public boolean isEquals() {
        return equals;
    }

    public void setEquals(final boolean equals) {
        this.equals = equals;
    }

    public boolean isNotExists() {
        return !getDestFile().exists();
        //return notExists;
    }
//
    private String fromLocalePath;
    private String toLocalePath;

    public String getFromLocalePath() {
        return fromLocalePath;
    }

    public void setFromLocalePath(final String fromLocalePath) {
        this.fromLocalePath = fromLocalePath;
    }

    public String getToLocalePath() {
        return toLocalePath;
    }

    public void setToLocalePath(final String toLocalePath) {
        this.toLocalePath = toLocalePath;
    }

    public boolean isFromNotExistsAndToExists() {
        return fromNotExistsAndToExists;
    }

    public void setFromNotExistsAndToExists(final boolean fromNotExistsAndToExists) {
        this.fromNotExistsAndToExists = fromNotExistsAndToExists;
    }

    static public List<AdsMergeChangesItemWrapper> getSvnFileOptions(final AdsMergeChangesOptions options, final AdsDefinition def) throws Exception {
        List<AdsMergeChangesItemWrapper> normalList = new ArrayList();
        List<DefinitionAndFile> incorrectList = new ArrayList();
        tryAddDefinition(options, def, normalList, incorrectList);

        if (incorrectList.isEmpty() && !normalList.isEmpty()) {
            return normalList;
        }
        return null;

    }

    public static void tryAddDefinition(final AdsMergeChangesOptions options, final Definition def, final List<AdsMergeChangesItemWrapper> normalList, final List<DefinitionAndFile> incorrectList) throws Exception {
        final List<File> fromFiles = new ArrayList<>();

        if (def instanceof AdsImageDef) {
            final AdsImageDef image = (AdsImageDef) def;
            if (image.getImageFile() != null) {
                fromFiles.add(image.getImageFile());
            }
            if (def.getFile() != null) {
                fromFiles.add(def.getFile());
            }
        } else if (def instanceof AdsLocalizingBundleDef) {
            final AdsLocalizingBundleDef bundle = (AdsLocalizingBundleDef) def;
            if (bundle.findBundleOwner().getFile() != null) {
                fromFiles.add(bundle.findBundleOwner().getFile());
            }
        } else {
            if (def.getFile() != null) {
                fromFiles.add(def.getFile());
            }
        }
        final File previewFromFile;
        if (AdsUtils.isEnableHumanReadable(def)) {
            previewFromFile = AdsUtils.calcHumanReadableFile(def);
            if (previewFromFile != null) {
                fromFiles.add(previewFromFile);
            }
        } else {
            previewFromFile = null;
        }
        ISvnFSClient client = options.getFsClient();
        for (File fromFile : fromFiles) {
            final boolean isPreview = fromFile.equals(previewFromFile);
            final String mlbShortName = EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + fromFile.getName();

            final String toFileAbsolutePath = fromFile.getAbsolutePath();
            final String postfix = toFileAbsolutePath.substring(options.getFromBranchFile().getAbsolutePath().length());
            final File toFile = new File(options.getToBranchFile().getAbsolutePath() + postfix);

            final String fromFileUrl = SVN.getFileUrl(client, fromFile.getParentFile());
            String toFileUrl;

            String toModuleFileUrl;
            final File toModuleFile = toFile.getParentFile().getParentFile();

            if (toModuleFile.exists()) {
                toModuleFileUrl = SVN.getFileUrl(client, toModuleFile);
            } else {
                toModuleFileUrl = SVN.getFileUrl(client, toModuleFile.getParentFile()).toString() + "/" + toModuleFile.getName();
            }

            if (toFile.getParentFile().exists())//src not exists - src or locale
            {
                if (!SVN.isNormalSvnStatus(options.getFsClient(), toFile.getParentFile())) {//RADIX-14856
                    incorrectList.add(new DefinitionAndFile(def, toFile.getParentFile(), null));
                    continue;
                }
                toFileUrl = SVN.getFileUrl(client, toFile.getParentFile());
            } else {
                final File moduleFile = toFile.getParentFile().getParentFile();
                final File adsFile = moduleFile.getParentFile();
                toFileUrl = SVN.getFileUrl(client, adsFile);
                toFileUrl = toFileUrl.toString() + "/" + toFile.getParentFile().getParentFile().getName() + "/" + toFile.getParentFile().getName();
            }
            URI fromUri = new URI(fromFileUrl);
            URI toUri = new URI(toFileUrl);
            URI toModuleUri = new URI(toModuleFileUrl);

            if (!SVN.isNormalSvnStatus(options.getFsClient(), fromFile)) {
                incorrectList.add(new DefinitionAndFile(def, fromFile, null));
            } else {

                final String pathFromFile = removeHead(fromUri, options) + "/" + fromFile.getName();
                final String pathToFile = options.getToPreffix() + pathFromFile.substring(options.getFromPreffix().length());
                if (toFile.exists() && !SVN.isNormalSvnStatus(options.getFsClient(), toFile)) {
                    incorrectList.add(new DefinitionAndFile(def, toFile, null));

                } else {

                    final boolean notExist = !SVN.isExists(options.getRepository(), pathToFile);
                    int copyLevel = notExist ? SvnEntryComparator.MAY_COPY_NEW : SvnEntryComparator.NOT_MAY_COPY;
                    final boolean isEq = notExist ? false : SvnEntryComparator.equals(options.getRepository(), pathToFile, pathFromFile);

                    if (!isEq) {
                        if (!notExist) {
                            copyLevel = SvnEntryComparator.equalsToThePreviousVersions(options.getRepository(), pathToFile, pathFromFile);
                        }
                    }

                    final boolean mayMerge = !notExist && !isEq;

                    final AdsMergeChangesItemWrapper item = new AdsMergeChangesItemWrapper(def, fromFile.getName());
                    item.setMlbShortName(mlbShortName);

                    item.setSrcChangesLevel(copyLevel);
                    item.setMayMerge(mayMerge);
                    item.setEquals(isEq);
//                                item.setNotExists(notExist);

                    item.setFromPath(pathFromFile);
                    item.setToPath(pathToFile);
                    item.setSrcFile(fromFile);
                    item.setDestFile(toFile);
                    item.setIsPreview(isPreview);
                    normalList.add(item);

                }
            }
            if (!isPreview) {
                AdsMergeChangesItemWrapper item = new AdsMergeChangesItemWrapper(def, mlbShortName);
                item.setMlb(true);
                item.setMlbShortName(mlbShortName);

                l:
                while (true) {
                    //boolean isCorrectMlb = true;
                    if (options.getFromFormatVersion() < 2) {

                        String pathFromMlbPath = removeHead(fromUri, options) + "/" + mlbShortName;
                        File fromMlbFile = new File(fromFile.getParent() + "/" + mlbShortName);
                        item.setSrcFile(fromMlbFile);
                        item.setFromPath(pathFromMlbPath);

                        if (SVN.isExists(options.getRepository(), pathFromMlbPath)) {
                            if (!SVN.isNormalSvnStatus(options.getFsClient(), fromMlbFile) || !fromMlbFile.exists()) {
                                incorrectList.add(new DefinitionAndFile(def, fromMlbFile, null));
                                break l;
                            }
                        }
                    } else //if (changesOptions.getFromFormatVersion()>=2)
                    {
                        String fromModuleFileUrl = SVN.getFileUrl(client, fromFile.getParentFile().getParentFile());
                        URI fromModuleFileUri = new URI(fromModuleFileUrl);

                        File fromLocaleFile = new File(fromFile.getParentFile().getParentFile().getAbsolutePath() + "/" + LOCALE);
                        item.setFromLocale(fromLocaleFile);

                        item.setFromLocalePath(removeHead(fromModuleFileUri, options) + "/" + LOCALE);

                        for (EIsoLanguage lng : options.getCommonLangList()) {
                            AdsMergeChangesItemWrapper.MergeChangesLangugeItemWrapper lngItem = new AdsMergeChangesItemWrapper.MergeChangesLangugeItemWrapper(lng);
                            lngItem.fromMlbFile = new File(fromLocaleFile.getAbsolutePath() + "/" + lng.getValue() + "/" + mlbShortName);
                            lngItem.fromPath = removeHead(fromModuleFileUri, options) + "/" + LOCALE + "/" + lng.getValue() + "/" + mlbShortName;

                            if (lngItem.fromMlbFile.exists()) {
                                if (!SVN.isNormalSvnStatus(client, lngItem.fromMlbFile)) {
                                    incorrectList.add(new DefinitionAndFile(def, lngItem.fromMlbFile, "Language " + lng.getName()));
                                    break l;
                                    //continue;
                                }
                            }
                            item.appendLangugeItem(lngItem);
                        }
                    }

                    if (options.getToFormatVersion() < 2) {

                        String pathToMlbPath = removeHead(toUri, options) + "/" + mlbShortName;
                        File toMlbFile = new File(toFile.getParent() + "/" + mlbShortName);
                        if (toMlbFile.exists()) {
                            if (!SVN.isNormalSvnStatus(client, toMlbFile)) {
                                incorrectList.add(new DefinitionAndFile(def, toMlbFile, null));
                                break l;
                            }
                        }
                        item.setDestFile(toMlbFile);
                        item.setToPath(pathToMlbPath);
                    } else //if (changesOptions.getToFormatVersion()>=2)
                    {

                        //SVNURL toModuleFileUrl = SVN.getFileUrl(toFile.getParentFile().getParentFile());
                        File toLocaleFile = new File(toFile.getParentFile().getParentFile().getAbsolutePath() + "/" + LOCALE);
                        item.setToLocale(toLocaleFile);

                        item.setToLocalePath(removeHead(toModuleUri, options) + "/" + LOCALE);

                        for (EIsoLanguage lng : options.getCommonLangList()) {
                            AdsMergeChangesItemWrapper.MergeChangesLangugeItemWrapper lngItem = item.findLangugeItem(lng);
                            boolean mustAddThisLngItem = false;
                            if (lngItem == null) {
                                if (options.getFromFormatVersion() >= 2)//  svn stutus source files is realy incorrect
                                {
                                    continue;
                                }
                                lngItem = new AdsMergeChangesItemWrapper.MergeChangesLangugeItemWrapper(lng);
                                mustAddThisLngItem = true;
                            }
                            lngItem.toMlbFile = new File(toLocaleFile.getAbsolutePath() + "/" + lng.getValue() + "/" + mlbShortName);
                            lngItem.toPath = removeHead(toModuleUri, options) + "/" + LOCALE + "/" + lng.getValue() + "/" + mlbShortName;

                            if (lngItem.toMlbFile.exists()) {
                                if (!SVN.isNormalSvnStatus(client, lngItem.toMlbFile)) {
                                    incorrectList.add(new DefinitionAndFile(def, lngItem.toMlbFile, "Language " + lng.getName()));
                                    break l;
                                }
                            }
                            if (mustAddThisLngItem) {
                                item.appendLangugeItem(lngItem);
                            }
                        }
                    }
                    int copyLevel = SvnEntryComparator.NOT_MAY_COPY;
                    boolean mayMerge = false;

                    boolean isEq = false;

                    //boolean notExist = false;                                
                    if (options.getFromFormatVersion() < 2 && options.getToFormatVersion() < 2) {

                        boolean notExist = !SVN.isExists(options.getRepository(), item.getToPath());
                        copyLevel = notExist ? SvnEntryComparator.MAY_COPY_NEW : SvnEntryComparator.NOT_MAY_COPY;
                        isEq = notExist ? false : SvnEntryComparator.equals(options.getRepository(), item.getToPath(), item.getFromPath());
                        if (!isEq) {
                            if (!notExist) {
                                copyLevel = SvnEntryComparator.equalsToThePreviousVersions(options.getRepository(), item.getToPath(), item.getFromPath());
                            }
                        }
                        item.setFromNotExistsAndToExists(!item.getSrcFile().exists() && item.getDestFile().exists());
                    } else if (options.getFromFormatVersion() >= 2 && options.getToFormatVersion() >= 2) {

                        for (AdsMergeChangesItemWrapper.MergeChangesLangugeItemWrapper lngItem : item.getNewMlbItems()) {
                            if (!lngItem.fromMlbFile.exists() && !lngItem.toMlbFile.exists()) {
                                continue;
                            }
                            AdsMergeChangesItemWrapper newItem = new AdsMergeChangesItemWrapper(def,
                                    ".." + "/" + LOCALE + "/" + lngItem.language.getValue() + "/" + mlbShortName + " (" + lngItem.language.getName() + ")");
                            newItem.setMlb(true);
                            newItem.setFromLocale(item.getFromLocale());
                            newItem.setToLocale(item.getToLocale());
                            newItem.setMlbShortName(mlbShortName);

                            newItem.setSrcFile(lngItem.fromMlbFile);
                            newItem.setFromPath(lngItem.fromPath);

                            newItem.setDestFile(lngItem.toMlbFile);
                            newItem.setToPath(lngItem.toPath);

                            newItem.setFromNotExistsAndToExists(!lngItem.fromMlbFile.exists() && lngItem.toMlbFile.exists());

                            boolean notExist = !SVN.isExists(options.getRepository(), lngItem.toPath);
                            copyLevel = notExist ? SvnEntryComparator.MAY_COPY_NEW : SvnEntryComparator.NOT_MAY_COPY;
                            isEq = notExist ? false : SvnEntryComparator.equals(options.getRepository(), lngItem.toPath, lngItem.fromPath);
                            if (!isEq) {
                                if (!notExist) {
                                    copyLevel = SvnEntryComparator.equalsToThePreviousVersions(options.getRepository(), lngItem.toPath, lngItem.fromPath);
                                }
                            }
                            mayMerge = !notExist && !isEq;

                            newItem.setEquals(isEq);
                            newItem.setSrcChangesLevel(copyLevel);
                            newItem.setMayMerge(mayMerge);
                            normalList.add(newItem);
                        }
                        break l;
                    } else if (options.getFromFormatVersion() >= 2 && options.getToFormatVersion() < 2) {
                        boolean fromExists = item.isExistsOneAdditionMlbFile(true);
                        boolean toExists = item.getDestFile().exists();

                        if (!fromExists && !toExists) {
                            break l;
                        } else if (!fromExists && toExists) {
                            item.setFromNotExistsAndToExists(true);
                            copyLevel = SvnEntryComparator.NOT_MAY_COPY;
                            mayMerge = false;
                            isEq = false;
                        } else {
                            File toMlbFile = item.getSrcFile() == null || !item.getSrcFile().exists() ? null : item.getSrcFile();
                            AdsDefinitionDocument doc = MergeUtils.convertFromNewFormatToOld(options.getCommonLangList(), item.getFromLocale(), toMlbFile, item.getMlbShortName());
                            String toXmlAsString = MergeUtils.saveToString(doc);
                            item.setXmlStringDataEmulator(toXmlAsString);

                            if (!toExists) {
                                isEq = false;
                                copyLevel = SvnEntryComparator.MAY_COPY_NEW;
                                mayMerge = false;
                            } else {
                                SvnEntryComparator.EntrySizeAndEntrySha1Digest fromEntrySha1Digest = SvnEntryComparator.getEntrySizeAndEntrySha1DigestByString(toXmlAsString);
                                SvnEntryComparator.EntrySizeAndEntrySha1Digest toEntrySha1Digest = SvnEntryComparator.getEntrySizeAndEntryTopSha1Digest(options.getRepository(), item.getToPath());
                                isEq = Arrays.equals(fromEntrySha1Digest.entryDigest, toEntrySha1Digest.entryDigest);
                                if (isEq) {
                                    copyLevel = SvnEntryComparator.NOT_MAY_COPY;
                                } else {
                                    copyLevel = SvnEntryComparator.NOT_MAY_COPY;

                                    List<Long> allRevList = new ArrayList();
                                    for (AdsMergeChangesItemWrapper.MergeChangesLangugeItemWrapper lngItem : item.getNewMlbItems()) {
                                        if (lngItem.fromMlbFile.exists()) {
                                            List<Long> currList = SVN.getSvnLog(options.getRepository(), lngItem.fromPath);
                                            for (Long currRev : currList) {
                                                if (!allRevList.contains(currRev)) {
                                                    allRevList.add(currRev);
                                                }
                                            }
                                        }
                                    }
                                    Collections.sort(allRevList);

                                    List<String> lngListAsStr = new ArrayList();
                                    int curLevel = 0;
                                    for (int i = allRevList.size() - 1; i >= 0; i--) {// slow cycle 
                                        long rev = allRevList.get(i);
                                        lngListAsStr.clear();

                                        for (EIsoLanguage lng : options.getCommonLangList()) {
                                            String path = item.getFromLocalePath() + "/" + lng.getValue() + "/" + mlbShortName;
                                            if (SVN.isExists(options.getRepository(), path, rev)) {
                                                lngListAsStr.add(SVN.getFileAsStr(options.getRepository(), path, rev));
                                            } else {
                                                lngListAsStr.add(null);
                                            }
                                        }
                                        AdsDefinitionDocument defDoc = MergeUtils.convertFromNewFormatToOld(options.getCommonLangList(), lngListAsStr, toXmlAsString, mlbShortName);
                                        String fromXmlAsString = MergeUtils.saveToString(defDoc);

                                        if (Utils.equals(fromXmlAsString, toXmlAsString)) {
                                            copyLevel = curLevel;
                                            break;
                                        }
                                        curLevel++;
                                    }

                                }
                                mayMerge = !isEq;
                            }
                        }
                    } else if (options.getFromFormatVersion() < 2 && options.getToFormatVersion() >= 2) {

                        boolean fromFileExists = item.getSrcFile().exists();
                        boolean toFileExists = item.getDestFile() != null && item.getDestFile().exists();
                        if (!fromFileExists && !toFileExists) {
                            break l;
                        }

                        Reference<Boolean> fromFileRefExists = new Reference();

                        for (AdsMergeChangesItemWrapper.MergeChangesLangugeItemWrapper lngItem : item.getNewMlbItems()) {
                            final EIsoLanguage language = lngItem.language;

                            boolean currFromExists = false;
                            String currFromMlbAsStr = null;

                            if (fromFileExists) {
                                AdsDefinitionDocument docAsNewFormat = MergeUtils.convertFromOldFormatToNew(language, item.getSrcFile(), fromFileRefExists);
                                currFromExists = fromFileRefExists.get();
                                currFromMlbAsStr = MergeUtils.saveToString(docAsNewFormat);
                            }

                            if (!currFromExists && !lngItem.toMlbFile.exists()) {
                                continue;
                            }

                            AdsMergeChangesItemWrapper newItem = new AdsMergeChangesItemWrapper(def,
                                    ".." + "/" + LOCALE + "/" + language.getValue() + "/" + mlbShortName + " (" + language.getName() + ")");
                            newItem.setMlb(true);
                            newItem.setFromLocale(item.getFromLocale());
                            newItem.setToLocale(item.getToLocale());
                            newItem.setMlbShortName(mlbShortName);

                            newItem.setLanguage(language);

                            newItem.setSrcFile(item.getSrcFile());
                            newItem.setFromPath(item.getFromPath());
                            newItem.setXmlStringDataEmulator(currFromMlbAsStr);

                            newItem.setDestFile(lngItem.toMlbFile);
                            newItem.setToPath(lngItem.toPath);

                            newItem.setFromNotExistsAndToExists(!currFromExists);

                            if (!currFromExists) {
                                newItem.setEquals(false);
                                newItem.setSrcChangesLevel(SvnEntryComparator.NOT_MAY_COPY);
                                newItem.setMayMerge(false);
                            } else if (!lngItem.toMlbFile.exists()) {
                                newItem.setEquals(false);
                                newItem.setSrcChangesLevel(SvnEntryComparator.MAY_COPY_NEW);
                                newItem.setMayMerge(false);
                            } else {

                                SvnEntryComparator.EntrySizeAndEntrySha1Digest fromEntry = SvnEntryComparator.getEntrySizeAndEntrySha1DigestByString(currFromMlbAsStr);
                                SvnEntryComparator.EntrySizeAndEntrySha1Digest toEntry = SvnEntryComparator.getEntrySizeAndEntryTopSha1Digest(options.getRepository(), lngItem.toPath);
                                //String toMlbAsStr = SVN.getFileAsStr(options.getRepository(), lngItem.toPath, options.getRepository().getLatestRevision());

                                final String ff = currFromMlbAsStr;

                                isEq = fromEntry.equals(toEntry);
                                if (isEq) {
                                    copyLevel = SvnEntryComparator.NOT_MAY_COPY;
                                } else {
                                    copyLevel = SvnEntryComparator.equalsToThePreviousVersions(options.getRepository(), toEntry, item.getFromPath(),
                                            new SvnEntryComparator.StringConverter() {
                                                @Override
                                                public String convert(String str) throws Exception {
                                                    if (str == null) {
                                                        return "";
                                                    }
                                                    String s = MergeUtils.saveToString(MergeUtils.convertFromOldFormatToNew(language, str, null));
                                                    return s;
                                                }
                                            });
                                }

                                newItem.setEquals(isEq);
                                newItem.setSrcChangesLevel(copyLevel);
                                newItem.setMayMerge(!isEq);
                            }
                            normalList.add(newItem);
                        }
                        break l;

                    }

                    item.setMlbShortName(mlbShortName);
                    item.setSrcChangesLevel(copyLevel);
                    item.setMayMerge(mayMerge);
                    item.setEquals(isEq);

                    normalList.add(item);
                    break;//NOPMD
                }
            }
        }

    }

    public static class DefinitionAndFile extends Definition {

        final public File file;
        final public String message;
        final public Definition def;

        public DefinitionAndFile(Definition src, File file, String message) {
            super(src);
            this.file = file;
            this.message = message;
            def = src;
        }

        @Override
        public String getQualifiedName() {
            return def.getQualifiedName();
        }
    }
    static final private String LOCALE = "locale";
}
