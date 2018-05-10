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

package org.radixware.kernel.common.userreport.repository;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.AdsLibUserFuncWrapper;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.userreport.common.IUserReportRequestExecutor.ReportDataInfo;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsModule;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsImageDefinition;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.repository.fs.JarFileDataProvider;
import org.radixware.kernel.common.repository.fs.RepositoryInjection;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.utils.changelog.ChangeLog;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.extrepository.UserExtADSSegmentRepository;
import org.radixware.kernel.common.utils.JarFiles;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsUserReportDefinitionDocument;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;
import org.radixware.schemas.adsdef.UserReportDefinitionType;
import org.radixware.schemas.reports.UserReportHeader;
import org.radixware.schemas.reports.UserReportListRsDocument;
import org.radixware.schemas.xscml.TypeDeclaration;


public class ReportsModuleRepository extends FSRepositoryAdsModule {
    
    public static final Id LIST_REPORTS_WITH_PARAM_COMMAND_ID = Id.Factory.loadFrom("cmdIKNO6UWG5FFDFIXMLZAQGBNNKM");
    public static final Id LIST_REPORTS_COMMAND_ID = Id.Factory.loadFrom("clc5VOFMBGOGZAH5J25FDBJTWAD2U");
    public static final Id REPORT_MODULE_CLASS_ID = Id.Factory.loadFrom("aecN7LNYTJYXBDC7MTGOI4IP2THFA");
    public static final Id REPORT_MODULE_SELECTOR_ID = Id.Factory.loadFrom("sprUTXTF3TESJBNZGDA6TQ5NWWMUI");
    public static final Id REPORT_MODULE_EDITOR_ID = Id.Factory.loadFrom("eprWYPXTH4OWBEOJJAXL3WGBZ6GNU");
    public static final Id REPORT_MODULE_ID_PROP_ID = Id.Factory.loadFrom("colHL2DQINMTBFYBKFSXNSK43NHY4");
    public static final Id REPORT_MODULE_TITLE_PROP_ID = Id.Factory.loadFrom("colAW4TWZKC4ZCUJI4QSCYEBQ3DTA");
    public static final Id REPORT_MODULE_DESC_PROP_ID = Id.Factory.loadFrom("colGI2BEXFEXRCZZHEQ3HDSNPNN34");
    public static final Id REPORT_VERSION_CLASS_ID = Id.Factory.loadFrom("aecM2NL42YXRRA5ZH27LCKIW5CQNI");
    public static final Id REPORT_CURRENT_VERSION_PROP_ID = Id.Factory.loadFrom("colRFGZDGVYONGERF2WDQ5THDREOE");
    public static final Id REPORT_VERSION_REPORT_ID_PROP_ID = Id.Factory.loadFrom("col443263VQHJDJTPL4VY3PNWTNCE");
    public static final Id REPORT_VERSION_USER_CLASS_GUID_PROP_ID = Id.Factory.loadFrom("colFCOKOL7E3JD3PDNES4EAW35XTE");
    public static final Id REPORT_VERSION_VERSION_PROP_ID = Id.Factory.loadFrom("colT7D35GBIUBGHLHVAUNSMFGFCJM");
    public static final Id REPORT_MODULE_IMAGES_PROP_ID = Id.Factory.loadFrom("colTD5STVFJWZCWTAEXMFGIDXXUHU");
    public static final Id REPORT_MODULE_FORMAT_VERSION_PROP_ID = Id.Factory.loadFrom("colJIQZ4KJI75DQVKCF22MJY6QZMQ");
    public static final Id REPORT_CLASS_REPORT_MODULE_PROP_ID = Id.Factory.loadFrom("colIE7QFFHT6VAJZMYY6E5NAFR57M");
    public static final long CURRENT_FORMAT_VERSION = 1;

    private final UserExtADSSegmentRepository segment;
    private String name;
    private final Id id;
    private boolean readed = false;
    private boolean loaded = false;    
    private final Object uploadLock = new Object();

    public ReportsModuleRepository(UserExtADSSegmentRepository segment, File preparedDir, Id id, String name) {
        super(preparedDir);
        this.segment = segment;
        this.name = name;
        this.id = id;

    }

    public ReportsModuleRepository(UserExtADSSegmentRepository segment, ReportsModule module) {
        super(module);
        name = module.getName();
        id = module.getId();
        this.segment = segment;
    }

    void udpateRepository(ReportsModule module) throws IOException {
        this.name = module.getName();

        //temporary use module description as image store

        File imgDir = getImagesDirectory();
        final String content;
        String dd = null;
        try {
            dd = FileUtils.readTextFile(getDescriptionFile(), FileUtils.XML_ENCODING);
        } catch (IOException e) {
        }
        content = dd;

        final Bin images;
        if (imgDir.exists() && imgDir.listFiles().length > 0) {
            File targetZip = File.createTempFile("aaa", "bbb");
            try {
                JarFiles.mkJar(imgDir, targetZip, new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return true;
                    }
                }, false);
                byte[] bytes = FileUtils.readBinaryFile(targetZip);
                images = new Bin(bytes);
            } finally {
                FileUtils.deleteFile(targetZip);
            }
        } else {
            images = null;
        }

        UserExtensionManagerCommon.getInstance().getUFRequestExecutor().updateModuleRepository(module,content,images);
    }

    void deleteModule() {
        UserExtensionManagerCommon.getInstance().getUserReportManager().deleteModule(id);
    }
    
    void upload() {
        readHeadersIfNesessary();
    }

    private boolean readHeadersIfNesessary() {
        synchronized (uploadLock) {
            if (!readed) {
                if (!UserExtensionManagerCommon.getInstance().isReady()) {
                    return false;
                }

                final UserReportListRsDocument rs =UserExtensionManagerCommon.getInstance().getUFRequestExecutor().listReports(getId());
                final UserReports registry = UserExtensionManagerCommon.getInstance().getUserReports();
                for (UserReportHeader h : rs.getUserReportListRs().getReportList()) {
                    try {
                        AdsTypeDeclaration paramType = h.getContextParamType() == null ? null : AdsTypeDeclaration.Factory.loadFrom(h.getContextParamType());
                        registry.registerReport(getId(), h.getId(), h.getName(), h.getDescription(), h.getCurrentVersion(), h.getCurrentOrder(), paramType, h.getFormatVersion());
                    } catch (IOException ex) {
                        UserExtensionManagerCommon.getInstance().getUFRequestExecutor().processException(ex);
                    }

                }
                readed = true;
                return true;
            } else {
                return false;
            }
        }
    }

    private ReportDataInfo loadReportData(final Id reportId, final long version) {
        return UserExtensionManagerCommon.getInstance().getUFRequestExecutor().loadReportData(reportId,version);
    }

    private static class VersionInfo {

        File file;
        Id lastRuntimeId;

        public VersionInfo(File file, Id lastRuntimeId) {
            this.file = file;
            this.lastRuntimeId = lastRuntimeId;
        }
    }

    private VersionInfo loadAndStoreReportVersion(Id reportId, long version, boolean isCurrentVersion) throws IOException {
        ReportDataInfo data = loadReportData(reportId, version);
        AdsUserReportDefinitionDocument xReportDoc = data.getXml();
        if (xReportDoc != null && xReportDoc.getAdsUserReportDefinition() != null) {
            UserReportDefinitionType xReportWithCorrectIds = AdsUserReportClassDef.getReportDefWithCorrectIds(
                    xReportDoc.getAdsUserReportDefinition(),reportId, version, isCurrentVersion);
            File srcDir = new File(getDirectory(), "src");
            srcDir.mkdirs();

            AdsDefinitionDocument xDefDoc = AdsDefinitionDocument.Factory.newInstance();
            xDefDoc.addNewAdsDefinition().setAdsClassDefinition(xReportWithCorrectIds.getReport());
            xDefDoc.getAdsDefinition().setFormatVersion(AdsSqlClassDef.FORMAT_VERSION);
            TypeDeclaration superType = TypeDeclaration.Factory.newInstance();
            superType.setTypeId(EValType.USER_CLASS);
            superType.setPath(Arrays.asList(AdsReportClassDef.PREDEFINED_ID));
            xDefDoc.getAdsDefinition().getAdsClassDefinition().setExtends(superType);

            File result = new File(srcDir, xReportWithCorrectIds.getReport().getId().toString() + ".xml");
            XmlFormatter.save(xDefDoc, result);

            File localeDir = new File(getDirectory(), "locale");
            if (!localeDir.isDirectory()) {
                localeDir.mkdirs();
            }

            if (xReportWithCorrectIds.getStrings() != null) {
                List<LocalizedString> emptyStrs = new ArrayList<>();
                Map<EIsoLanguage, LocalizingBundleDefinition> mapMlStrings = 
                        getSortMlStringsByLangs(xReportWithCorrectIds.getStrings(), emptyStrs);
                if (!emptyStrs.isEmpty()) {
                    for (EIsoLanguage lang : segment.getLayerRepository().getLayer().getLanguages()) {
                        if (!mapMlStrings.containsKey(lang)) {
                            mapMlStrings.put(lang, LocalizingBundleDefinition.Factory.newInstance());
                        }
                    }
                }

                final Id mlbId = xReportWithCorrectIds.getStrings().getId();
                for (Map.Entry<EIsoLanguage, LocalizingBundleDefinition> strInfo : mapMlStrings.entrySet()) {
                    File langDir = new File(localeDir, strInfo.getKey().getValue());
                    if (!langDir.isDirectory()) {
                        langDir.mkdirs();
                    }
                    xDefDoc = AdsDefinitionDocument.Factory.newInstance();
                    LocalizingBundleDefinition xLocalDef = strInfo.getValue();
                    xLocalDef.getStringList().addAll(emptyStrs);
                    xDefDoc.addNewAdsDefinition().setAdsLocalizingBundleDefinition(xLocalDef);
                    xDefDoc.getAdsDefinition().setFormatVersion(0);
                    xDefDoc.getAdsDefinition().getAdsLocalizingBundleDefinition().setId(mlbId);
                    XmlFormatter.save(xDefDoc, new File(langDir, mlbId.toString() + ".xml"));
                }
            }
            return new VersionInfo(result, data.getLastRuntimeId());
        }
        return null;
    }
    
    @Override
    public IRepositoryAdsDefinition getDefinitionRepository(Definition def) {
        if(def instanceof AdsLibUserFuncWrapper){
            return null;
        }else{
            return super.getDefinitionRepository(def);
        }
        
    }

    private Map<EIsoLanguage, LocalizingBundleDefinition> getSortMlStringsByLangs(LocalizingBundleDefinition xLocale, List<LocalizedString> emptyStrs) {
        Map<EIsoLanguage, LocalizingBundleDefinition> mapMlStrings = new HashMap();
        if (xLocale != null) {
            for (LocalizedString xStr : xLocale.getStringList()) {
                if (xStr.getValueList().isEmpty()) {
                    LocalizedString xNewStr = LocalizedString.Factory.newInstance();
                    xNewStr.set(xStr);
                    emptyStrs.add(xNewStr);
                } else {
                    for (LocalizedString.Value xVal : xStr.getValueList()) {
                        LocalizingBundleDefinition xl = mapMlStrings.get(xVal.getLanguage());
                        if (xl == null) {
                            xl = LocalizingBundleDefinition.Factory.newInstance();
                            mapMlStrings.put(xVal.getLanguage(), xl);
                        }

                        LocalizedString xNewStr = xl.addNewString();
                        xNewStr.setId(xStr.getId());
                        if (xStr.isSetDefinitionType()) {
                            xNewStr.setDefinitionType(xStr.getDefinitionType());
                        }
                        if (xStr.isSetEventSeverity()) {
                            xNewStr.setEventSeverity(xStr.getEventSeverity());
                        }
                        if (xStr.isSetEventSource()) {
                            xNewStr.setEventSource(xStr.getEventSource());
                        }
                        if (xStr.isSetCompilerWarnings()) {
                            xNewStr.setCompilerWarnings(xStr.getCompilerWarnings());
                        }
                        if (xStr.isSetSpellCheck()) {
                            xNewStr.setSpellCheck(xStr.getSpellCheck());
                        }
                        if (xStr.isSetIsOverwrite()) {
                            xNewStr.setIsOverwrite(xStr.getIsOverwrite());
                        }
                        xNewStr.setSrcKind(xStr.getSrcKind());
                        LocalizedString.Value xNewVal = xNewStr.addNewValue();
                        xNewVal.setLanguage(xVal.getLanguage());
                        xNewVal.setStringValue(xVal.getStringValue());
                        if (xVal.isSetAuthor()) {
                            xNewVal.setAuthor(xVal.getAuthor());
                        }
                        if (xVal.isSetLastModified()) {
                            xNewVal.setLastModified(xVal.getLastModified());
                        }
                        if (xVal.isSetNeedsCheck()) {
                            xNewVal.setNeedsCheck(xVal.getNeedsCheck());
                        }
                    }
                }
            }
        }
        return mapMlStrings;
    }

    @Override
    public IRepositoryAdsDefinition[] listDefinitions() {

        synchronized (uploadLock) {

            if (!UserExtensionManagerCommon.getInstance().isReady()) {
                return new IRepositoryAdsDefinition[0];
            }
            readHeadersIfNesessary();
            if (!loaded) {
                try {
                    final Runnable loadReportsTask = new Runnable() {
                        @Override
                        public void run() {
                            for (UserReport report : UserExtensionManagerCommon.getInstance().getUserReports().listReports(id)) {
                                try {
                                    VersionInfo info = loadAndStoreReportVersion(report.getId(), report.getVersions().getCurrent().getVersion(), true);
                                    if (info != null) {
                                        report.getVersions().getCurrent().rememberRuntimeId(info.lastRuntimeId);
                                    }
                                } catch (IOException ex) {
                                    UserExtensionManagerCommon.getInstance().getEnvironment().processException(ex);//.printStackTrace(ex);
                                }
                            }
                        }
                    };
                    UserExtensionManagerCommon.getInstance().getUFRequestExecutor().executeTask(loadReportsTask);
                } finally {
                    loaded = true;
                }
            }
        }
        return super.listDefinitions();
    }

    File loadAdditionalVersion(UserReport report, long version) {
        listDefinitions();
        synchronized (uploadLock) {
            try {
                VersionInfo info = loadAndStoreReportVersion(report.getId(), version, report.getVersions().getCurrent().getVersion() == version);
                if (info != null) {
                    UserReport.ReportVersion versionObject = report.getVersions().get(version);
                    if (versionObject != null) {
                        versionObject.rememberRuntimeId(id);
                    }
                    return info.file;
                }
                return null;
            } catch (IOException ex) {
                return null;
            }
        }
    }
    private volatile boolean imagesLoaded = false;

    @Override
    public IRepositoryAdsImageDefinition[] listImages() {
        if (!imagesLoaded) {
            try {
                byte[] bytes = UserExtensionManagerCommon.getInstance().getUFRequestExecutor().listImages(id);
                if (bytes != null) {
                    File tmp = File.createTempFile("aaa", "aaa");
                    FileUtils.writeBytes(tmp, bytes);
                    File imgDir = getImagesDirectory();
                    imgDir.mkdirs();
                    ZipFile zip = new ZipFile(tmp);
                    try {
                        Enumeration<? extends ZipEntry> entries = zip.entries();
                        while (entries.hasMoreElements()) {
                            ZipEntry e = entries.nextElement();
                            InputStream in = zip.getInputStream(e);
                            try {
                                File out = new File(imgDir, e.getName());
                                FileOutputStream outStream = new FileOutputStream(out);
                                try {
                                    FileUtils.copyStream(in, outStream);
                                } finally {
                                    outStream.close();
                                }
                            } finally {
                                in.close();
                            }
                        }
                    } finally {
                        zip.close();
                        FileUtils.deleteFile(tmp);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ReportsModuleRepository.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                imagesLoaded = true;
            }
        }
        return super.listImages();
    }

    @Override
    public File getImagesDirectory() {
        return new File(getDirectory(), "img");
    }

    @Override
    public File getJavaSrcDirContainer() {
        return getDirectory();
    }

    @Override
    public File getBinariesDirContainer() {
        return getDirectory();
    }

    @Override
    public IJarDataProvider getJarFile(String pathInModule) throws IOException {
        return JarFileDataProvider.getInstance(new File(getDirectory(), pathInModule));
    }

    @Override
    public boolean containsDefinition(Id id) {
        for (IRepositoryAdsDefinition def : listDefinitions()) {
            if (def.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void close() {
        //  reports.clear();
    }

    @Override
    public File getDirectory() {
        return new File(segment.getDirectory(), getId().toString());
    }

    @Override
    public File getDescriptionFile() {
        return new File(getDirectory(), org.radixware.kernel.common.defs.Module.MODULE_XML_FILE_NAME);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return "";
    }

    @Override
    public boolean isInjection() {
        return false;
    }

    @Override
    public void installInjection(RepositoryInjection.ModuleInjectionInfo injection) throws IOException {
    }

    @Override
    public void uninstallInjection() {
    }

    @Override
    public void processException(Throwable e) {
        segment.processException(e);
    }

    public Id getId() {
        return id;
    }
}
