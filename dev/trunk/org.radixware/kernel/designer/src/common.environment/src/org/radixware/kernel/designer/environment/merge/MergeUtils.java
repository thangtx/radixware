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

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.apache.xmlbeans.XmlException;
import org.netbeans.api.progress.ProgressUtils;
import org.netbeans.modules.subversion.Subversion;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;

import org.radixware.kernel.common.builder.BuildActionExecutor;
import org.radixware.kernel.common.builder.release.ReleaseSettings;
import org.radixware.kernel.common.constants.FileNames;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.dialogs.AuthenticationCancelledException;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.SvnPathUtils;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.Reference;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.common.version.Version;
import org.radixware.kernel.designer.common.dialogs.build.DesignerBuildEnvironment;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.subversion.util.SvnBridge;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.adsdef.LocalizedString.Value;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;

public class MergeUtils {

    static protected class MergeAnalizeException extends RuntimeException {

        public MergeAnalizeException(String message) {
            super(message);
        }
    }

    static protected void generateException(final String message) {
        throw new MergeAnalizeException(message);
    }

    static public AdsDefinitionDocument convertFromOldFormatToNew(final EIsoLanguage lng, final String oldMlbAsStr, final Reference<Boolean> lngExistsInOldFormat) throws Exception {
        final AdsDefinitionDocument oldDoc = AdsDefinitionDocument.Factory.parse(oldMlbAsStr);
        return convertFromOldFormatToNew(lng, oldDoc, lngExistsInOldFormat);
    }

    static public AdsDefinitionDocument convertFromOldFormatToNew(final EIsoLanguage lng, final File oldMlbFile, final Reference<Boolean> lngExistsInOldFormat) throws Exception {
        final AdsDefinitionDocument oldDoc = AdsDefinitionDocument.Factory.parse(oldMlbFile);
        return convertFromOldFormatToNew(lng, oldDoc, lngExistsInOldFormat);
    }

    static private AdsDefinitionDocument convertFromOldFormatToNew(final EIsoLanguage lng, final AdsDefinitionDocument oldDoc, final Reference<Boolean> lngExistsInOldFormat) throws Exception {

        boolean exists = false;

        if (oldDoc != null) {
            final AdsDefinitionElementType adsDefinitionElementType = oldDoc.getAdsDefinition();
            if (adsDefinitionElementType != null) {
                if (adsDefinitionElementType.isSetFormatVersion()) {
                    adsDefinitionElementType.unsetFormatVersion();
                }
                adsDefinitionElementType.setFormatVersion(0);
                final LocalizingBundleDefinition oldLs = adsDefinitionElementType.getAdsLocalizingBundleDefinition();

                if (oldLs != null) {
                    final List<LocalizedString> oldStringList = oldLs.getStringList();
                    if (oldStringList != null) {
                        for (LocalizedString oldString : oldStringList) {
                            if (oldString.isSetVersion()) {
                                oldString.unsetVersion();
                            }
                            oldString.setVersion(0);
                            final List<Value> ValueList = oldString.getValueList();
                            if (ValueList != null) {
                                for (int i = ValueList.size() - 1; i >= 0; i--) {
                                    final Value value = ValueList.get(i);
                                    if (lng.equals(value.getLanguage())) {
                                        exists = true;
                                    } else {
                                        ValueList.remove(i);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (lngExistsInOldFormat != null) {
            lngExistsInOldFormat.set(exists);
        }

        return oldDoc;
    }

    static public AdsDefinitionDocument convertFromNewFormatToOld(final List<EIsoLanguage> commonLngList, final File newLocaleDir, final File oldMlbFile, final String shortName) throws Exception {

        final List<String> xmlAsStringList = new ArrayList();
        for (EIsoLanguage lng : commonLngList) {
            final File newLngFile = new File(newLocaleDir.getAbsolutePath() + "/" + lng.getValue() + "/" + shortName);
            if (!newLngFile.exists()) {
                xmlAsStringList.add(null);
                continue;
            }
            xmlAsStringList.add(FileUtils.readTextFile(newLngFile, FileUtils.XML_ENCODING));
        }

        String oldMlbFileAsStr = null;
        if (oldMlbFile != null && oldMlbFile.exists()) {
            oldMlbFileAsStr = FileUtils.readTextFile(oldMlbFile, FileUtils.XML_ENCODING);
        }
        final AdsDefinitionDocument def = convertFromNewFormatToOld(commonLngList, xmlAsStringList, oldMlbFileAsStr, shortName);
        return def;
    }

    static public AdsDefinitionDocument convertFromNewFormatToOld(final List<EIsoLanguage> commonLngList, final List<String> xmlAsStringList, final String oldMlbFileAsStr, final String shortName) throws Exception {

        List<LocalizedString> oldStringList = null;
        if (oldMlbFileAsStr != null) {
            final AdsDefinitionDocument oldDoc = AdsDefinitionDocument.Factory.parse(oldMlbFileAsStr);
            final LocalizingBundleDefinition oldLs = oldDoc.getAdsDefinition().getAdsLocalizingBundleDefinition();
            oldStringList = oldLs.getStringList();
        }

        final Map<String, LocalizingBundleDefinition> map = new HashMap();
        final List<String> allIds = new ArrayList();

        int i = -1;
        for (EIsoLanguage lng : commonLngList) {
            i++;
            final String newXmlAsStr = xmlAsStringList.get(i);
            if (newXmlAsStr == null) {
                continue;
            }

            final AdsDefinitionDocument newDoc = AdsDefinitionDocument.Factory.parse(newXmlAsStr);
            final LocalizingBundleDefinition newLs = newDoc.getAdsDefinition().getAdsLocalizingBundleDefinition();
            if (newLs != null) {

                map.put(lng.getValue(), newLs);

                final List<LocalizedString> stringList = newLs.getStringList();
                if (stringList != null) {
                    for (LocalizedString stringItem : stringList) {
                        if (stringItem.getId() != null && !allIds.contains(stringItem.getId().toString())) {
                            allIds.add(stringItem.getId().toString());
                        }
                    }
                }
            }
        }

        Collections.sort(allIds);

        final List<LocalizedString> rezultStringList = new ArrayList();

        for (String idAsStr : allIds) {

            LocalizedString findLocalizedString = null;
            final Id id = Id.Factory.loadFrom(idAsStr);

            final Iterator<LocalizingBundleDefinition> iterator = map.values().iterator();
            while (iterator.hasNext()) {
                final LocalizingBundleDefinition localizingBundleDefinition = iterator.next();
                final List<LocalizedString> stringList = localizingBundleDefinition.getStringList();
                if (stringList != null) {
                    for (LocalizedString stringItem : stringList) {
                        if (stringItem.getId() != null && stringItem.getId().equals(id)) {
                            findLocalizedString = stringItem;
                            break;
                        }
                    }
                }
                if (findLocalizedString != null) {
                    break;
                }
            }

            if (findLocalizedString != null) {
                findLocalizedString = (LocalizedString) findLocalizedString.copy();

                if (findLocalizedString.isSetVersion())// in old version its attribute absent
                {
                    findLocalizedString.unsetVersion();
                }

                for (int j = findLocalizedString.getValueList().size() - 1; j >= 0; j--) {
                    findLocalizedString.removeValue(j);
                }
                rezultStringList.add(findLocalizedString);
            }

            List<EIsoLanguage> oldLngOrder = null;
            if (oldStringList != null) { //search languages order ...

                for (LocalizedString stringItem : oldStringList) {
                    if (stringItem.getId() != null && idAsStr.equals(stringItem.getId().toString())) {
                        final List<LocalizedString.Value> valueList = stringItem.getValueList();
                        if (valueList != null) {
                            oldLngOrder = new ArrayList();
                            for (LocalizedString.Value value : valueList) {
                                final EIsoLanguage lng = value.getLanguage();
                                if (lng != null) {
                                    oldLngOrder.add(lng);
                                }
                            }
                        }
                        break;
                    }
                }
            }

            final List<LocalizedString.Value> valueList = new ArrayList();
            //before, filling old languages
            if (oldLngOrder != null) {
                for (EIsoLanguage currLng : oldLngOrder) {
                    final LocalizingBundleDefinition newBundleDefinition = map.get(currLng.getValue());
                    if (newBundleDefinition != null) {
                        final List<LocalizedString> newStringList = newBundleDefinition.getStringList();
                        if (newStringList != null) {
                            for (LocalizedString newLocalizedString : newStringList) {
                                if (newLocalizedString.getId() != null && newLocalizedString.getId().toString().equals(idAsStr)) {
                                    valueList.addAll(newLocalizedString.getValueList());
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            //after, filling  other languages
            if (oldLngOrder == null || oldLngOrder.size() != commonLngList.size()) {
                for (EIsoLanguage currLng : commonLngList) {
                    if (oldLngOrder != null && oldLngOrder.contains(currLng))// already processing this language
                    {
                        continue;
                    }

                    final LocalizingBundleDefinition newBundleDefinition = map.get(currLng.getValue());
                    if (newBundleDefinition != null) {
                        final List<LocalizedString> newStringList = newBundleDefinition.getStringList();
                        if (newStringList != null) {
                            for (LocalizedString newLocalizedString : newStringList) {
                                if (newLocalizedString.getId() != null && newLocalizedString.getId().toString().equals(idAsStr)) {
                                    valueList.addAll(newLocalizedString.getValueList());
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (findLocalizedString != null && !valueList.isEmpty()) {
                findLocalizedString.assignValueList(valueList);
            }

        }

        final AdsDefinitionDocument rezultDoc = AdsDefinitionDocument.Factory.newInstance();

        final AdsDefinitionElementType adsDefinitionElementType = rezultDoc.addNewAdsDefinition();
        final LocalizingBundleDefinition rezult = adsDefinitionElementType.addNewAdsLocalizingBundleDefinition();

        rezult.setId(Id.Factory.loadFrom(shortName.substring(0, shortName.length() - 4)));
        rezult.assignStringList(rezultStringList);

        if (adsDefinitionElementType.isSetFormatVersion()) {
            adsDefinitionElementType.unsetFormatVersion();
        }
        adsDefinitionElementType.setFormatVersion(0);

        return rezultDoc;
    }

    static public String saveToString(AdsDefinitionDocument def) {
        String s = def.xmlText(XmlUtils.getPrettyXmlOptions());
        s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + s;

        return s;
    }

    public static void messageError(final String mess) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DialogUtils.messageError(mess);
            }
        });
    }

    public static void messageError(final Exception ex) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DialogUtils.messageError(ex);
            }
        });
    }

    public static boolean canMergeAds() {
        return canMergeAds(MergeUtils.getAdsList());
    }

    public static boolean canMergeAds(final List<Definition> list) {
        if (list.isEmpty()) {
            return false;
        }

        final Definition def0 = list.get(0);
        final Layer layer = def0.getModule().getLayer();

        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).getModule().getLayer() != layer) {
                return false;
            }
        }
        return true;
    }

    public static List<DdsModule> getSelectedCapturedDdsModules() {
        final List<DdsModule> rez = new ArrayList();
        final List<RadixObject> curList = NodesManager.collectSelectedRadixObjects();
        for (RadixObject object : curList) {
            if (object instanceof DdsModule) {
                final DdsModule module = (DdsModule) object;
                if (module.isCapturedStructure()) {
                    rez.add(module);
                }
            }
        }
        return rez;
    }

    public static boolean canMergeDds() {
//        if (true){
//            return false;
//        }

        final List<DdsModule> list = getSelectedCapturedDdsModules();
        return !list.isEmpty();
    }

    static public List<Definition> getAdsList() {
        final List<RadixObject> curList = NodesManager.collectSelectedRadixObjects();
        final List<File> fileList = new ArrayList(curList.size());
        final List<Definition> newList = new ArrayList(curList.size());

        for (RadixObject obj2 : curList) {
            RadixObject obj = obj2;
            while (obj != null) {
                if (obj instanceof AdsDefinition && obj.getFile() != null) {
                    break;
                }
                obj = obj.getOwnerDefinition();
            }

            if (/*null != obj && */obj instanceof AdsDefinition && obj.getFile() != null) {
                AdsDefinition ads = (AdsDefinition) obj;
                ads = ads.findTopLevelDef();
                if (ads != null
                        && ads.getFile() != null
                        && !fileList.contains(ads.getFile()) // файл не был добавлен               
                        ) {
                    if (!((ads.getContainer().getContainer() instanceof AdsModule)
                            && fileList.contains(((AdsModule) ads.getContainer().getContainer()).getFile())))// модуль не был добавлен
                    {
                        fileList.add(ads.getFile());
                        newList.add(ads);
                    }
                }
            }
        }
        return newList;
    }

    private static Object getSVNClientAdapter(RadixObject srcRadixObject) {
        Subversion svn = Subversion.getInstance();
        try {
            return svn.getClient(srcRadixObject.getBranch().getDirectory());
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void fillBranchesInOptions(final AbstractMergeChangesOptions options, final RadixObject srcRadixObject) throws RadixSvnException, ISvnFSClient.SvnFsClientException, URISyntaxException {

        options.setFromBranchFile(srcRadixObject.getBranch().getFile().getParentFile());
        final Layer layer = srcRadixObject.getLayer();

//        ESvnAuthType authType;// = SVN.SVNPreferences.getAuthType();
//        String sshKeyFile;// = SVN.SVNPreferences.getSSHKeyFilePath();
//        String userName;// = SVN.SVNPreferences.getSSHKeyFilePath();
        try {
            final ReleaseSettings settings = new ReleaseSettings(srcRadixObject.getBranch(), null, new DesignerBuildEnvironment(false, BuildActionExecutor.EBuildActionType.RELEASE), false, SvnBridge.getClientAdapter(srcRadixObject.getBranch().getDirectory()));

            final ESvnAuthType authType = settings.getAuthType();
            final String sshKeyFile = settings.getSSHKeyFile();
            final String userName = settings.getUserName();

            try {
                ISvnFSClient client = org.radixware.kernel.designer.subversion.util.SvnBridge.getClientAdapter(options.getFromBranchFile());
                options.setRepository(SVNRepositoryAdapter.Factory.newInstance(client, options.getFromBranchFile(), userName, SVN.getForAuthType(authType), sshKeyFile));
                options.setFsClient(client);

            } catch (RadixSvnException ex) {
                if (ex.isAuthenticationCancelled()) {
                    return;
                } else {
                    throw ex;
                }
            }
        } catch (RuntimeException ex) {
            return;
        }

        final String toBranchFullName = chooseDirectory(null, options.getFromBranchFile(), srcRadixObject.getBranch().getBaseDevelopmentLayerUri());

        if (toBranchFullName == null || toBranchFullName.isEmpty()) {
            return;
        }

        options.setToBranchFile(new File(toBranchFullName));
        options.setToBranchFullName(toBranchFullName);

        options.setSrcLayer(layer);

        final URI fromBranchUrl = new URI(SVN.getFileUrl(options.getFsClient(), options.getFromBranchFile()));

        String fromPath;
        fromPath = fromBranchUrl.getPath();
        fromPath = SvnPathUtils.removeHead(fromPath);

        final URI toBranchUrl = new URI(SVN.getFileUrl(options.getFsClient(), options.getToBranchFile()));
        String toPath;
        toPath = toBranchUrl.getPath();
        toPath = SvnPathUtils.removeHead(toPath);

        options.setFromPreffix(fromPath);
        options.setToPreffix(toPath);

    }

    protected static AdsMergeChangesOptions collectAdsOptions() throws IOException, RadixSvnException, ISvnFSClient.SvnFsClientException {
        return collectAdsOptions(getAdsList());
    }

    protected static DdsMergeChangesOptions collectDdsOptions() throws IOException, XmlException, RadixSvnException, ISvnFSClient.SvnFsClientException {
        return collectDdsOptions(getSelectedCapturedDdsModules());
    }

    protected static boolean radixVersionNotLessThen_1_2_29(final File branchFile) throws XmlException, IOException {

        if (branchFile == null || !branchFile.exists() || !Branch.isBranchDir(branchFile)) {
            MergeUtils.generateException("Branch not found \'" + String.valueOf(branchFile) + "\'");
            return false;
        }

        String lastReleaseNumber = null;
        final File branchXmlFile = new File(branchFile.getAbsolutePath() + File.separator + org.radixware.kernel.common.constants.FileNames.BRANCH_XML);
        if (branchXmlFile.exists()) {
            final org.radixware.schemas.product.Branch xBranch = org.radixware.schemas.product.Branch.Factory.parse(branchXmlFile);
            lastReleaseNumber = xBranch.getLastRelease();
        }

        final File radixLayerFile = new File(branchFile.getAbsolutePath() + File.separator + FileNames.RADIX_LAYER_URI);

        if (!radixLayerFile.exists() || Layer.isLayerDir(radixLayerFile)) {
            return false;
        }

        final File radixLayerXmlFile = new File(radixLayerFile.getAbsolutePath() + File.separator + FileNames.LAYER_XML);
        if (!radixLayerXmlFile.exists()) {
            return false;
        }

        final org.radixware.schemas.product.Layer xLayer = org.radixware.schemas.product.Layer.Factory.parse(radixLayerXmlFile);
        String layerReleaseNumber = xLayer.getReleaseNumber();
        if (layerReleaseNumber == null || layerReleaseNumber.isEmpty()) {
            layerReleaseNumber = lastReleaseNumber;
        }
        if (layerReleaseNumber == null || layerReleaseNumber.isEmpty()) {
            return false;
        }
        final Version version = new Version(layerReleaseNumber);
        return new Version("1.2.29").compareTo(version) >= 0;

    }

    protected static DdsMergeChangesOptions collectDdsOptions(final List<DdsModule> list) throws RadixSvnException, IOException, XmlException, ISvnFSClient.SvnFsClientException {
        if (list == null || list.isEmpty()) {
            return null;
        }

        final DdsMergeChangesOptions options = new DdsMergeChangesOptions(list);
        try {
            fillBranchesInOptions(options, list.get(0));
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }

        if (options.getToBranchFile() == null || !options.getToBranchFile().exists()) {
            return null;
        }

        final AtomicBoolean ab = new AtomicBoolean();
        final List<DdsMergeChangesItemWrapper> addedItems = new ArrayList();
        final List<File> incorrectFiles = new ArrayList();

//        try {
//            ProgressUtils.runOffEventDispatchThread(
//                    new Runnable() {
//
//                        @Override
//                        public void run() {
//                            try {
        final boolean destBranchFormatIsNew = radixVersionNotLessThen_1_2_29(options.getToBranchFile());
        for (DdsModule module : list) {

            DdsMergeChangesItemWrapper.doWithModule(options, module, addedItems, incorrectFiles, options.getToBranchFile(), destBranchFormatIsNew);

        }
//                            } catch (XmlException | IOException ex) {
//                                DialogUtils.messageError(ex);
//                            }
//                            
//                        }
//                    }, "Please wait ...", ab, true, 0, 0);
//        } catch (java.lang.IllegalStateException ex) {
//            DialogUtils.messageError(ex);
//        }

//        
        options.setIncorrectFiles(incorrectFiles);
        options.setWrappers(addedItems);
        return options;
    }

    protected static AdsMergeChangesOptions collectAdsOptions(final List<Definition> list) throws IOException, RadixSvnException, ISvnFSClient.SvnFsClientException {
        if (list == null || list.isEmpty()) {
            return null;
        }
        final AdsMergeChangesOptions options = new AdsMergeChangesOptions(list);
        try {
            fillBranchesInOptions(options, list.get(0));
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }

        if (options.getToBranchFile() == null || !options.getToBranchFile().exists()) {
            return null;
        }

        final Branch fromBranch = Branch.Factory.loadFromDir(options.getFromBranchFile());
        final Branch toBranch = Branch.Factory.loadFromDir(options.getToBranchFile());

        options.setFromFormatVersion(fromBranch.getFormatVersion());
        options.setToFormatVersion(toBranch.getFormatVersion());

        final Layer fromLayer = list.get(0).getModule().getSegment().getLayer();
        final Layer toLayer = toBranch.getLayers().findByURI(fromLayer.getURI());
        if (toLayer == null) {
            messageError("Destination layer \'" + fromLayer.getURI() + "\' not found.");
            return null;
        }

        if (fromBranch.getFormatVersion() > 1 || toBranch.getFormatVersion() > 1) {
            if (!checkLang(fromLayer, toLayer)) {
                return null;
            }
        }

        options.setSrcLangList(fromLayer.getLanguages());
        options.setDestLangList(toLayer.getLanguages());

        final List<EIsoLanguage> common222LngList2 = new ArrayList();
        for (EIsoLanguage srcLng : options.getSrcLangList()) {
            if (options.getDestLangList().contains(srcLng)) {
                common222LngList2.add(srcLng);
            }
        }
        options.setCommonLangList(common222LngList2);

        final List<AdsMergeChangesItemWrapper> normalList = new ArrayList(list.size());
        final List<AdsMergeChangesItemWrapper.DefinitionAndFile> incorrectList = new ArrayList(list.size());

        if (options.getFromFormatVersion() <= 2 || options.getToFormatVersion() <= 2) //break;
        {
            ProgressUtils.showProgressDialogAndRun(new Runnable() {
                @Override
                public void run() {
                    try {

                        for (Definition def : list) {
                            AdsMergeChangesItemWrapper.tryAddDefinition(options, def, normalList, incorrectList);
                        }

                    } catch (Exception ex) {
                        messageError(ex);
                    }
                }
            }, "Generate definition list");
        }

        if (!incorrectList.isEmpty()) {
            RadixObjectsUtils.sortByQualifiedName(normalList);

            final StringBuilder sb = new StringBuilder();

            sb.append("The following definitions have not been added, since their SVN status is incorrect:");
            for (AdsMergeChangesItemWrapper.DefinitionAndFile def : incorrectList) {
                sb.append("\n");
                sb.append(def.getQualifiedName()).append(", ").append(def.file.getName()).append(def.message == null ? "" : " (" + def.message + ")");
            }
            messageError(sb.toString());
        }

        RadixObjectsUtils.sortByQualifiedName(normalList);

        options.setList(normalList);

        options.setFromBranchShortName(options.getFromBranchFile().getName());
        options.setToBranchShortName(options.getToBranchFile().getName());
        return options;

    }

    static public void doWithDefinitions(final List<Definition> list) throws Exception {
        if (list == null || list.isEmpty()) {
            messageError("Not found processed definitions.");
            return;
        }
        if (canMergeAds(list)) {
            AdsMergeChangesOptions options = null;
            boolean canRepeat = true;
            while (canRepeat) {//RADIX-10635
                try {
                    options = collectAdsOptions(list);
                    canRepeat = false;
                } catch (Throwable th) {
                    canRepeat = DialogUtils.messageConfirmation("Exception detected. Do you want try merge again?", th, null);
                }
            }

            if (options == null) {
                return;
            }
            final CopyMergeDialog dialog = new CopyMergeDialog(options);
            dialog.showModal();
        }
    }

    static public boolean isSelectedAds(final List<File> listOfFiles) {
        for (File f : listOfFiles) {
            final RadixObject rObj = RadixFileUtil.findRadixObject(f);
            if (rObj != null && (rObj instanceof AdsDefinition || rObj instanceof AdsModule)) {
                return true;
            }
        }
        return false;
    }

    static boolean flag = false;

    static public void doWithFiles(final List<File> listOfFiles) throws Exception {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                if (flag) {//RADIX-10367
                    DialogUtils.messageError("Merge dialog already executing.");
                    return;
                }
                flag = true;
                try {
                    final List<Definition> adsDefList = new ArrayList();
                    for (File f : listOfFiles) {
                        final RadixObject rObj = RadixFileUtil.findRadixObject(f);
                        if (rObj instanceof AdsDefinition) {
                            AdsDefinition adef = (AdsDefinition) rObj;

                            if (adef instanceof AdsLocalizingBundleDef) {
                                final AdsLocalizingBundleDef bundle = (AdsLocalizingBundleDef) adef;
                                adef = bundle.findBundleOwner();
                            }
                            if (adef != null && !adsDefList.contains(adef)) {
                                adsDefList.add(adef);
                            }
                        } else if (rObj instanceof AdsModule) {
                            adsDefList.add((AdsModule) rObj);
                        }
                    }
                    try {
                        doWithDefinitions(adsDefList);
                    } catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
                    }
                } finally {
                    flag = false;
                }

            }
        });

    }

    static public void doWithDefinitions() throws Exception {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                CopyMergeDialog dialog = null;
                if (canMergeAds()) {
                    try {
                        final AdsMergeChangesOptions options = collectAdsOptions();
                        if (options == null) {
                            return;
                        }
                        dialog = new CopyMergeDialog(options);
                    } catch (IOException | RadixSvnException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                } else if (canMergeDds()) {
                    try {
                        final DdsMergeChangesOptions options = collectDdsOptions();
                        if (options == null) {
                            return;
                        }
                        dialog = new CopyMergeDialog(options);
                    } catch (IOException | XmlException | RadixSvnException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

                if (dialog != null) {
                    dialog.showModal();
                }
            }
        });

    }

    private static String langListToStr(final List<EIsoLanguage> lngList) {
        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (EIsoLanguage lng : lngList) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(", ");
            }
            sb.append(lng.getName());
        }

        return sb.toString();
    }

    private static boolean checkLang(final Layer fromLayer, final Layer toLayer) {

        final List<EIsoLanguage> srcLngList = fromLayer.getLanguages();
        final List<EIsoLanguage> destLngList = toLayer.getLanguages();

        if (srcLngList.size() != destLngList.size() || !srcLngList.containsAll(destLngList)) {
            if (!DialogUtils.messageConfirmation(
                    "The list of supported languages ​at the source and destination layer are different.\n"
                    + "The changes will be transferred to the common subset of languages."
                    + "\nSounce: " + langListToStr(srcLngList)
                    + ".\nDestination: " + langListToStr(destLngList) + ". Continue?", NotifyDescriptor.WARNING_MESSAGE)) {
                return false;
            }

        }
        return true;
    }

    private static boolean isBranchWithDevUri(final File dir, final String DevUri) {
        if (!Branch.isBranchDir(dir)) {
            return false;
        }
        try {
            final Branch branch = Branch.Factory.loadFromDir(dir);
            if (branch != null
                    && branch.getBaseDevelopmentLayerUri() != null
                    && branch.getBaseDevelopmentLayerUri().equals(DevUri)) {
                return true;
            }
        } catch (IOException ex) {
            Logger.getLogger(MergeUtils.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        return false;
    }

    private static String chooseDirectory(final Component owner, final File trunk, final String devUri) {

        assert SwingUtilities.isEventDispatchThread();

        final String CFG_PATH_BRANCH = "ConfigPathBranchDirForMergeDlg";
        final String CFG_KEY = "CurrDir";

        String currDir = trunk.getAbsolutePath();
        final Preferences pref = Utils.findPreferencesWithoutException(CFG_PATH_BRANCH);
        String savedDir = null;
        if (pref != null) {
            savedDir = pref.get(CFG_KEY, null);
        }
        if (savedDir != null) {
            currDir = savedDir;
        }

        class JFileChooserEx extends JFileChooser {

            final protected File trunk;
            final protected String DevUri;

            JFileChooserEx(final File trunk, final String DevUri) {
                super();
                this.trunk = trunk;
                this.DevUri = DevUri;
            }

            @Override
            public Icon getIcon(File f) {
                if (f.isDirectory() && isBranchWithDevUri(f, DevUri) && !f.equals(trunk)) {
                    return RadixObjectIcon.BRANCH.getIcon();
                }
                return super.getIcon(f);
            }
        }

        final JFileChooserEx fileChooser = new JFileChooserEx(trunk, devUri);
        final FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Directories";
            }
        };
        fileChooser.setFileHidingEnabled(false);
        fileChooser.setDialogTitle("Choose Other Branch Directory");
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        if (currDir != null && !currDir.isEmpty()) {
            final File currDirFile = new File(currDir);
            fileChooser.setCurrentDirectory(currDirFile.getParentFile());
            fileChooser.setSelectedFile(currDirFile);
        }

        while (fileChooser.showOpenDialog(owner) == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
            if (Branch.isBranchDir(fileChooser.getSelectedFile())
                    && !fileChooser.getSelectedFile().equals(trunk)) {

                final String s = fileChooser.getSelectedFile().getAbsolutePath();
                final Preferences pref2 = Utils.findOrCreatePreferences(CFG_PATH_BRANCH);
                pref2.put(CFG_KEY, s);
                return s;
            }
            messageError("Incorrect branch directory");
        }
        return null;
    }

    static String getTableAsStr(List<String> comments, List<String> autors, List<Long> revisions, List<Date> dates) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<table border=\"1\" cellpadding=\"4\">");
        sb.append("<tbody>");
        sb.append("<tr>");
        sb.append("<th>Revision</th>");
        sb.append("<th>Autor</th>");
        sb.append("<th>Date</th>");
        sb.append("<th>Comment</th>");
        sb.append("<tr>");

        for (int i = 0; i < revisions.size(); i++) {
            sb.append("<tr>");
            sb.append("<td>").append(revisions.get(i)).append("</td>");
            sb.append("<td>").append(autors.get(i)).append("</td>");
            sb.append("<td>").append(dates.get(i)).append("</td>");
            sb.append("<td>").append(comments.get(i) == null ? "" : comments.get(i).replace('\n', ' ')).append("</td>");
            sb.append("<tr>");
        }

        sb.append("</tbody>");
        sb.append("</table>");

        return sb.toString();
    }

}
