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

import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.swing.event.ChangeListener;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.IUserDefChangeSuppert;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.adsdef.AdsUserReportDefinitionDocument;
import org.radixware.schemas.adsdef.AdsUserReportExchangeDocument;
import org.radixware.schemas.adsdef.UserReportDefinitionType;
import org.radixware.schemas.adsdef.UserReportExchangeType;
import org.radixware.schemas.reports.*;

/**
 *
 * @author akrylov
 */
public class UserReport implements AdsReportClassDef.ContextParameterTypeRestriction {

    public static final Id REPORT_CLASS_ID = Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U");
    public static final Id REPORT_CREATE_PRESENTATION_ID = Id.Factory.loadFrom("epr4OVCICCOEVAKHDEDRPGTIZEOUA");
    public static final Id REPORT_EDIT_PRESENTATION_ID = Id.Factory.loadFrom("eprJIZO5FC2MVAC3NJW6QEZ7EV3PE");
    public static final Id REPORT_ID_PROP_ID = Id.Factory.loadFrom("colJENVO3VYOBBRXCG3GAOQDPK72Y");
    public static final Id REPORT_MODULE_ID_PROP_ID = Id.Factory.loadFrom("colIE7QFFHT6VAJZMYY6E5NAFR57M");
    public static final Id REPORT_NAME_PROP_ID = Id.Factory.loadFrom("colUVEMGNNQCVCRLKP7GTOHJKGL7I");
    public static final Id REPORT_DESCRIPTION_PROP_ID = Id.Factory.loadFrom("colSAPAWW6RG5HJLMVL34K2YQKHKY");
    public static final Id REPORT_FORMAT_VERSION_PROP_ID = Id.Factory.loadFrom("colS4YCAONUQVCXXJX2JRQCLCRFKA");
    public static final Id REPORT_CONTEXT_PARAM_TYPE_PROP_ID = Id.Factory.loadFrom("colMLR5RZCEI5DXRPTXTCRC46763A");
    public static final Id REPORT_CURRENT_VERSION_PROP_ID = Id.Factory.loadFrom("colRFGZDGVYONGERF2WDQ5THDREOE");
    public static final Id REPORT_CURRENT_ORDER_PROP_ID = Id.Factory.loadFrom("prdPAFYOEI4J5HBLI25YJYU7UICUE");
    public static final Id REPORT_LIST_VERSIONS_COMMAND_ID = Id.Factory.loadFrom("clc4SM5FTKAIRGEJOHXMFD6YFQ2CM");
    public static final Id REPORT_VERSION_GENERAL_PRESENTATION_ID = Id.Factory.loadFrom("eprA7COZLY7RZHOHOOA2FUCKMIVHM");
    public static final Id REPORT_VERSION_CLASS_ID = Id.Factory.loadFrom("aecM2NL42YXRRA5ZH27LCKIW5CQNI");
    public static final Id REPORT_VERSION_SOURCE_PROP_ID = Id.Factory.loadFrom("col3FPD27GEFBBTPFKKRBBVO5ODS4");
    public static final Id REPORT_VERSION_ORDER_PROP_ID = Id.Factory.loadFrom("prdNO2BBNB2YJCZZOZ3M26E6GHK34");
    public static final Id REPORT_VERSION_RUNTIME_PROP_ID = Id.Factory.loadFrom("colTV4V72I4NZDNHP2S3DMDKGZMEQ");
    public static final Id REPORT_VERSION_REPORT_ID_PROP_ID = Id.Factory.loadFrom("col443263VQHJDJTPL4VY3PNWTNCE");
    public static final Id REPORT_VERSION_REPORT_CLASS_GUID_PROP_ID = Id.Factory.loadFrom("colFCOKOL7E3JD3PDNES4EAW35XTE");
    public static final Id REPORT_VERSION_VERSION_PROP_ID = Id.Factory.loadFrom("colT7D35GBIUBGHLHVAUNSMFGFCJM");
    public static final Id REPORT_VERSION_PRESENTATION_ID = Id.Factory.loadFrom("eprA7COZLY7RZHOHOOA2FUCKMIVHM");
    public static final Id DISABLE_COMMAND_ID = Id.Factory.loadFrom("clcLEYNKJOI3FGL5DLPODQPLOCHTE");
    public static final long FORMAT_VERSION = 1;

    @Override
    public boolean isSuitableTypeForParameter(AdsTypeDeclaration decl) {
        return this.getContextParamType() == null ? decl == null : getContextParamType().equals(decl);
    }

    public static class ReportVersion implements RadixObject.NameProvider {

        private final long version;
        private final UserReport handle;
        private boolean isVisible;
        private long order;
        private volatile int searchLockCount = 0;
        private String description;
        private IUserDefChangeSuppert changeSupport;// = new VersionChangeSuppert(this);
        private Id uploadRuntimeId = null;

        public void rememberRuntimeId(Id id) {
            this.uploadRuntimeId = id;
        }

        public static Id buildReportVersion(Id reportId, long reportVersion) {
            return Id.Factory.loadFrom(reportId.toString() + "_urv_" + reportVersion);
        }

        public static boolean parseReportVersion(Id srcId, Id[] reportId, long[] reportVersion) {
            if (srcId == null) {
                return false;
            }
            String asStr = srcId.toString();
            int index = asStr.indexOf("_urv_");
            if (index > 0) {
                if (asStr.startsWith("mlb")) {
                    reportId[0] = Id.Factory.loadFrom(asStr.substring(3, index));
                } else {
                    reportId[0] = Id.Factory.loadFrom(asStr.substring(0, index));
                }
                try {
                    reportVersion[0] = Long.parseLong(asStr.substring(index + 5));
                } catch (NumberFormatException e) {
                    return false;
                }
                return true;
            } else {
                if (asStr.startsWith("mlb")) {
                    reportId[0] = Id.Factory.loadFrom(asStr.substring(3));
                } else {
                    reportId[0] = srcId;
                }
                reportVersion[0] = -1;
                return true;
            }
        }

        public ReportVersion(UserReport handle, long version, long order) {
            this(handle, version, order, null);
        }

        public ReportVersion(UserReport handle, long version, long order, AdsReportClassDef initialReport) {
            this.version = version;
            this.handle = handle;
            this.order = order;
            if (UserExtensionManagerCommon.getInstance().getUserReportManager() != null) {
                changeSupport = UserExtensionManagerCommon.getInstance().getUserReportManager().createVersionChangeSuppert(this);
            }
        }

        public long getVersion() {
            return version;
        }

        public long getOrder() {
            return order;
        }

        public void setVisible(boolean visible) {
            if (this.isVisible != visible) {
                this.isVisible = visible;
                saveVisibility();
                handle.getVersions().fireChange();
            }
        }

        private String visibilityPath() {
            return "/report-editor/report-data/" + handle.id + "/visibility/" + version;
        }

        public UserReport getUserReport() {
            return handle;
        }

        private void saveVisibility() {
            if (isVisible) {
                try {
                    Preferences.userRoot().node(visibilityPath()).sync();
                } catch (BackingStoreException ex) {
                    UserExtensionManagerCommon.getInstance().getUFRequestExecutor().processException(ex);
                }
            } else {
                try {
                    if (Preferences.userRoot().nodeExists(visibilityPath())) {
                        Preferences.userRoot().node(visibilityPath()).removeNode();
                        Preferences.userRoot().sync();
                    }
                } catch (BackingStoreException ex) {
                    UserExtensionManagerCommon.getInstance().getUFRequestExecutor().processException(ex);
                }
            }
        }

        private boolean isVisibleInConfig() {
            try {
                return Preferences.userRoot().nodeExists("/report-editor/report-data/" + handle.id + "/visibility/" + version);
            } catch (BackingStoreException ex) {
                return false;
            }
        }

        public boolean isVisible() {
            return isVisible;
        }

        @Override
        public String getName() {
            return handle.getName() + ".Version #" + order + " (" + version + ")";
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public UserReport getOwnerReport() {
            return handle;
        }

        public boolean isCurrent() {
            return handle.getVersions().getCurrent() == this;
        }

        public void removeChangeListener(ChangeListener listener) {
            //UserExtensionManagerCommon.getInstance().getUserReportManager().removeChangeListener(listener);
            changeSupport.removeChangeListener(listener);
        }

        public void addChangeListener(ChangeListener listener) {
            //UserExtensionManagerCommon.getInstance().getUserReportManager().addChangeListener(listener);
            changeSupport.addChangeListener(listener);
        }
        private volatile long lastUpdateRuntimeTime = 0;

        public void updateRuntime(File runtimeFile) throws IOException {
            saveImpl(runtimeFile);
            lastUpdateRuntimeTime = System.currentTimeMillis();
        }

        public void lockDefinitionSearch(boolean lock) {
            boolean fireChange = false;
            synchronized (this) {
                if (lock) {
                    searchLockCount++;
                } else {
                    searchLockCount--;
                }
                if (searchLockCount == 0) {
                    fireChange = true;
                }
            }
            if (fireChange) {
                getOwnerReport().getVersions().fireChange();
            }
        }

        private boolean isDefinitionSearchLocked() {
            return searchLockCount > 0;
        }

        /* public boolean makeCurrent(ProgressHandle progressHandle, boolean interactive){
         //LifecycleManager.getDefault().saveAll();
         if (isCurrent()) {
         return true;
         }

         //if (progressHandle != null) {
         //   progressHandle.progress("Compile version " + getName());
         //}
         AdsUserReportClassDef reportClass = findReportDefinition();
         if (reportClass == null) {
         return false;
         }            
         //UserExtensionManager.getInstance().compileOnSave(reportClass);

         //if (interactive && !UserExtensionManagerCommon.getInstance().canClose("Compilation errors detected. Continue?", false)) {
         //    return false;
         //}


         // if (progressHandle != null) {
         //     progressHandle.progress("Clean up report data for " + getName());
         // }

         //first step - cleanup this version
         lockDefinitionSearch(true);
         try {
         cleanup();
         ReportVersion current = handle.getVersions().getCurrent();
         if (current == null) {
         return false;
         }
         //second step - cleanup current version
         // if (progressHandle != null) {
         //     progressHandle.progress("Clean up report data for " + current.getName());
         // }
         current.lockDefinitionSearch(true);
         try {
         current.cleanup();
         //third step - set this version as current
         //  if (progressHandle != null) {
         //      progressHandle.progress("Registering new current version " + current.getName());
         //  }
         if (!handle.setCurrentVersionId(this)) {
         return false;
         }
         //   if (progressHandle != null) {
         //       progressHandle.progress("Applying changes... ");
         //   }
         handle.getVersions().setCurrent(this);
         } finally {
         current.lockDefinitionSearch(false);
         }
         } finally {
         lockDefinitionSearch(false);
         }

         reportClass = findReportDefinition();
         if (reportClass != null) {
         //UserExtensionManagerCommon.getInstance().compileOnSave(reportClass);
         }
         return true;
         }*/
        public void save() throws IOException {
            saveImpl(null);
            if (System.currentTimeMillis() - lastUpdateRuntimeTime > 1500) {
                AdsReportClassDef report = findReportDefinition();
                if (report != null) {
                    UserExtensionManagerCommon.getInstance().compileOnSave(report, false);
                }
            }
        }

        public void saveImpl(final File runtimeFile) throws IOException {
            handle.save();

            final AdsUserReportDefinitionDocument xDoc = AdsUserReportDefinitionDocument.Factory.newInstance();

            final AdsUserReportClassDef report = saveXml(xDoc.addNewAdsUserReportDefinition());
            if (report == null) {
                return;
            }
            UserExtensionManagerCommon.getInstance().getUFRequestExecutor().saveRuntime(runtimeFile, handle.id, report.getRuntimeId(), version, xDoc);
            /*UserExtensionManagerCommon.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
             @Override
             public void execute(IClientEnvironment env) {
             try {
             EntityModel versionModel = openReportVersionModel(UserExtensionManagerCommon.getInstance().getEnvironment(), handle.id, version);
             versionModel.getProperty(REPORT_VERSION_SOURCE_PROP_ID).setValueObject(xDoc);

             if (runtimeFile != null) {
             Bin runtimeData = new Bin(FileUtils.readBinaryFile(runtimeFile));
             versionModel.getProperty(REPORT_VERSION_RUNTIME_PROP_ID).setValueObject(runtimeData);
             versionModel.getProperty(REPORT_VERSION_REPORT_CLASS_GUID_PROP_ID).setValueObject(report.getRuntimeId().toString());
             }
             versionModel.update();
             } catch (final ModelException | ServiceClientException | InterruptedException | IOException ex) {
             UserExtensionManagerCommon.getInstance().getEnvironment().processException(ex);

             }
             }
             });*/
        }

        private AdsUserReportClassDef saveXml(UserReportDefinitionType xDef) {
            AdsUserReportClassDef report = findReportDefinition();
            if (report == null) {
                return null;
            }
            report.appendTo(xDef.addNewReport(), AdsDefinition.ESaveMode.NORMAL);
            xDef.getReport().setName("Version #" + order + " (" + version + ")");
            AdsLocalizingBundleDef sb = report.findExistingLocalizingBundle();
            if (sb != null) {
                sb.appendTo(xDef.addNewStrings(), AdsDefinition.ESaveMode.NORMAL);
            }
            return report;
        }

        public boolean isDeleted() {
            if (getOwnerReport() == null) {
                return true;
            }
            return !getOwnerReport().getVersions().contains(this);
        }
        private final ReentrantLock definitionLookupLock = new ReentrantLock();

        public AdsUserReportClassDef findReportDefinition() {
            return findReportDefinition(false);
        }

        public AdsUserReportClassDef findReportDefinition(boolean canWait) {
            try {
                if (canWait) {
                    while (!definitionLookupLock.tryLock()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                        }
                    }

                } else {
                    definitionLookupLock.lock();
                }
                if (isDeleted()) {
                    return null;
                }
                if (isDefinitionSearchLocked()) {
                    return null;
                }
                ReportsModule module = UserExtensionManagerCommon.getInstance().findModuleById(handle.moduleId);
                if (module == null) {
                    return null;
                }
                Id lookupId;
                if (isCurrent()) {
                    lookupId = handle.getId();
                } else {
                    lookupId = buildReportVersion(handle.getId(), version);
                }
                AdsDefinition def = module.getDefinitions().findById(lookupId);
                if (def instanceof AdsUserReportClassDef) {
                    if (def.getNameProvider() != this) {
                        def.setNameProvider(this);
                    }
                    AdsUserReportClassDef report = (AdsUserReportClassDef) def;
                    RadixObject.EEditState editState = report.getEditState();
                    RadixObject.getRegistry().disableFor(report);
                    try {
                        report.getInheritance().setSuperClassRef(AdsTypeDeclaration.Factory.newInstance(EValType.USER_CLASS, AdsReportClassDef.PREDEFINED_ID));

                        if (editState == RadixObject.EEditState.NONE) {
                            report.setEditState(editState);
                        }
                    } finally {
                        RadixObject.getRegistry().enableFor(report);
                    }
                    if (!report.hasRuntimeId()) {
                        report.resetRuntimeId(this.uploadRuntimeId);
                    }
                    if (report.getContextParamTypeRestriction() != this.handle) {
                        report.setContextParamTypeRestriction(this.handle);
                    }

                    return report;
                } else {
                    return null;
                }
            } finally {
                definitionLookupLock.unlock();
            }
        }

        public void cleanup() {
            lockDefinitionSearch(true);
            try {
                ReportsModule module = UserExtensionManagerCommon.getInstance().findModuleById(handle.moduleId);
                if (module == null) {
                    return;
                }

                final Id ownId = isCurrent() ? handle.id : buildReportVersion(handle.id, version);
                //final Id mlbId = Id.Factory.loadFrom("mlb" + ownId.toString());
                for (AdsDefinition def : module.getDefinitions()) {
                    if (def.getId() == ownId) {
//                        AdsLocalizingBundleDef bundle = def.findExistingLocalizingBundle();
//                        if (bundle != null) {
//                            bundle.delete();
//                        }
                        def.delete();
                        break;
                    }
                }

            } finally {
                lockDefinitionSearch(false);
            }
        }

        public void reload() {
            cleanup();
            getOwnerReport().getVersions().fireChange();
        }
    }
    private final Id id;
    private String name;
    private String description;
    private final ReportVersions versions;
    private Id moduleId;
    private boolean isModified = false;
    private AdsTypeDeclaration contextParameterType;
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private long currentFormatVersion = 0;

    public void setCurrentFormatVersion(long currentFormatVersion) {
        this.currentFormatVersion = currentFormatVersion;
    }

    public class ReportVersions {

        private ReportVersion currentVersion;
        private final Object currentVersionLock = new Object();
        private final Object versionListLock = new Object();
        private List<ReportVersion> versions = null;
        private final IUserDefChangeSuppert changeSupport;//= new ChangeSupport(this);

        private ReportVersions(ReportVersion currentVersion) {
            synchronized (currentVersionLock) {
                this.currentVersion = currentVersion;
            }
            if (UserExtensionManagerCommon.getInstance().getUserReportManager() != null) {
                changeSupport = UserExtensionManagerCommon.getInstance().getUserReportManager().createVersionsChangeSuppert(this);
            } else {
                changeSupport = null;
            }
        }

        public List<ReportVersion> list() {
            return new ArrayList<>(initializeVersions());
        }

        public List<ReportVersion> listVisible() {
            List<ReportVersion> all = list();

            int i = 0;
            while (i < all.size()) {
                ReportVersion v = all.get(i);
                if (!v.isVisible()) {
                    all.remove(i);
                } else {
                    i++;
                }
            }
            return all;
        }

        public boolean contains(ReportVersion version) {
            synchronized (this) {
                return initializeVersions().contains(version);
            }
        }

        public void setCurrent(ReportVersion version) {

            initializeVersions();
            synchronized (currentVersionLock) {
                currentVersion = version;
            }
            fireChange();
        }

        private List<ReportVersion> initializeVersions() {
            synchronized (versionListLock) {
                if (versions == null) {
                    versions = new LinkedList<>();
                    if (!loadReportVersions(versions)) {
                        versions = null;
                        return Collections.emptyList();
                    }
                    for (ReportVersion v : versions) {
                        if (!v.isCurrent()) {
                            v.setVisible(v.isVisibleInConfig());
                        }
                    }
                }
                return new ArrayList<>(versions);
            }
        }

        private boolean loadReportVersions(final List<ReportVersion> store) {
            UserReportVersionListRs xReportVersionsRs = UserReportVersionListRs.Factory.newInstance();
            boolean res = UserExtensionManagerCommon.getInstance().getUFRequestExecutor().loadReportVersions(UserReport.this.id, xReportVersionsRs);
            if (res) {

                for (UserReportVersionListRs.Version v : xReportVersionsRs.getVersionList()) {
                    long id = v.getId();
                    ReportVersion current;
                    synchronized (currentVersionLock) {
                        current = currentVersion;
                    }
                    if (current != null && current.version == id) {
                        if (!store.contains(current)) {
                            store.add(current);
                        }
                    } else {
                        store.add(new ReportVersion(UserReport.this, v.getId(), v.getOrder()));
                    }
                }
            }
            return res;
        }

        public ReportVersion getCurrent() {
            synchronized (currentVersionLock) {
                return currentVersion;
            }
        }

        public ReportVersion get(long id) {

            List<ReportVersion> list = initializeVersions();
            for (ReportVersion v : list) {
                if (v.version == id) {
                    return v;
                }
            }

            return null;
        }

        public void removeChangeListener(ChangeListener listener) {
            if (changeSupport != null) {
                changeSupport.removeChangeListener(listener);
            }
        }

        private void fireChange() {
            if (changeSupport != null) {
                changeSupport.fireChange();
            }
        }

        public void addChangeListener(ChangeListener listener) {
            if (changeSupport != null) {
                changeSupport.addChangeListener(listener);
            }
        }

        public void addNewVersion(ReportVersion src) {
            boolean fire = false;
            synchronized (this) {

                AdsReportClassDef report = src.findReportDefinition();
                if (report == null) {
                    return;
                }
                initializeVersions();
                UserReportDefinitionType xDef = UserReportDefinitionType.Factory.newInstance();

                report.appendTo(xDef.addNewReport(), AdsDefinition.ESaveMode.NORMAL);
                AdsLocalizingBundleDef stringBundle = report.findExistingLocalizingBundle();
                if (stringBundle != null) {
                    stringBundle.appendTo(xDef.addNewStrings(), AdsDefinition.ESaveMode.NORMAL);
                }

                ReportVersion version = createVersion(xDef);
                if (version != null) {
                    synchronized (versionListLock) {
                        versions.add(version);
                    }
                    fire = true;
                }
            }
            if (fire) {
                fireChange();
            }
        }

        public ReportVersion addNewVersion(UserReportDefinitionType srcXml) {
            boolean fire = false;
            try {
                synchronized (this) {
                    if (srcXml == null) {
                        return null;
                    }
                    initializeVersions();
                    ReportVersion version = createVersion(srcXml);
                    if (version != null) {
                        synchronized (versionListLock) {
                            versions.add(version);
                        }
                        fire = true;
                    }
                    return version;
                }
            } finally {
                if (fire) {
                    fireChange();
                }
            }
        }

        public void removeVersion(final ReportVersion version) {
            boolean fire = false;
            synchronized (this) {

                if (version.isCurrent()) {
                    return;
                }

                final boolean done = UserExtensionManagerCommon.getInstance().getUFRequestExecutor().removeVersion(version, id);/*new boolean[]{false};
                 final CountDownLatch lock = new CountDownLatch(1);
                 UserExtensionManagerCommon.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
                 @Override
                 public void execute(IClientEnvironment env) {
                 try {
                 EntityModel model = openReportVersionModel(env, getId(), version.version);
                 if (model.delete(true)) {
                 done[0] = true;
                 }
                 } catch (final ServiceClientException | InterruptedException ex) {
                 env.processException(ex);
                 } finally {
                 lock.countDown();
                 }
                 }
                 });
                 try {
                 lock.await();
                 } catch (InterruptedException ex) {
                 }*/

                if (done) {
                    version.lockDefinitionSearch(true);
                    version.cleanup();
                    removeFromVersions(version);
                    fire = true;
                }

            }
            if (fire) {
                fireChange();
            }
        }

        private void removeFromVersions(ReportVersion v) {
            synchronized (versionListLock) {
                versions.remove(v);
                Collections.sort(versions, new Comparator<ReportVersion>() {
                    @Override
                    public int compare(ReportVersion o1, ReportVersion o2) {
                        return o1.version == o2.version ? 0 : o1.version > o2.version ? 1 : -1;
                    }
                });
                for (int i = 0; i < versions.size(); i++) {
                    if (versions.get(i).order != i + 1) {
                        versions.get(i).order = i + 1;
                        if (versions.get(i).changeSupport != null) {
                            versions.get(i).changeSupport.fireChange();
                        }
                    }
                }
            }
        }

        private ReportVersion createVersion(UserReportDefinitionType srcXml) {
            if (srcXml == null) {
                return null;
            }

            AdsUserReportDefinitionDocument xDoc = AdsUserReportDefinitionDocument.Factory.newInstance();
            xDoc.addNewAdsUserReportDefinition().setReport(srcXml.getReport());
            if (srcXml.getStrings() != null) {
                xDoc.getAdsUserReportDefinition().setStrings(srcXml.getStrings());
            }

            ReportVersion version = UserExtensionManagerCommon.getInstance().getUFRequestExecutor().createVersionImpl(xDoc, id, UserReport.this);
            if (version != null) {
                version.setVisible(true);
            }
            return version;
        }

        private void cleanup() {
            List<ReportVersion> vs;
            synchronized (versionListLock) {
                vs = versions == null ? null : new ArrayList<>(versions);
            }
            if (vs != null) {
                for (ReportVersion v : vs) {
                    v.cleanup();
                }
            }
            fireChange();
        }
    }

    public UserReport(Id moduleId, Id id, String name, String description, long currentVersion, long currentVersionOrder) {
        this(moduleId, id, name, description, currentVersion, currentVersionOrder, null, null, FORMAT_VERSION);
    }

    public UserReport(Id moduleId, Id id, String name, String description, long currentVersion, long currentVersionOrder, AdsReportClassDef reportData, AdsTypeDeclaration contextParamType, long currentFormatVersion) {
        this.id = id;
        this.contextParameterType = contextParamType;
        this.moduleId = moduleId;
        this.name = name;
        this.description = description;
        this.versions = new ReportVersions(new ReportVersion(this, currentVersion, currentVersionOrder, reportData));
        this.versions.getCurrent().setVisible(true);
        this.currentFormatVersion = currentFormatVersion;
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public ReportVersions getVersions() {
        return versions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!Utils.equals(name, this.name)) {
            String oldname = this.name;
            this.name = name;
            changeSupport.firePropertyChange(new PropertyChangeEvent(this, "name", oldname, name));
            setModified(true);

        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (!Utils.equals(description, this.description)) {
            String oldname = this.description;
            this.description = description;
            changeSupport.firePropertyChange(new PropertyChangeEvent(this, "description", oldname, description));
            setModified(true);

        }
    }

    public Id getModuleId() {
        return moduleId;
    }

    public Id getId() {
        return id;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        if (isModified != modified) {
            isModified = modified;
            UserExtensionManagerCommon.getInstance().setReportModified(this, modified);
        }
    }

    public File getFile() {
        return UserExtensionManagerCommon.getInstance().getProjectDir(getId());
    }

    public boolean isReadOnly() {
        return false;
    }

    public static UserReport create(final ReportsModule module, final AdsReportClassDef initialReport) {
        if (initialReport == null) {
            return null;
        }
        return create(module, initialReport.getName(), initialReport.getId(), initialReport);

    }

    public static UserReport create(final ReportsModule module, final String name, final Id id, final AdsReportClassDef initialReport) {
        return UserExtensionManagerCommon.getInstance().getUFRequestExecutor().createReport(module, name, id, initialReport);
        /* final UserReport result[] = new UserReport[1];
         final CountDownLatch lock = new CountDownLatch(1);
         UserExtensionManagerCommon.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
         @Override
         public void execute(IClientEnvironment env) {
         try {
         EntityModel prepareCreateModel = EntityModel.openPrepareCreateModel(null, REPORT_CREATE_PRESENTATION_ID, REPORT_CLASS_ID, null, new IContext.ContextlessCreating(env));
         prepareCreateModel.getProperty(REPORT_ID_PROP_ID).setValueObject(id.toString());
         prepareCreateModel.getProperty(REPORT_MODULE_ID_PROP_ID).setValueObject(module.getId().toString());
         prepareCreateModel.getProperty(REPORT_NAME_PROP_ID).setValueObject(name);
         if (prepareCreateModel.create() != EEntityCreationResult.SUCCESS) {
         return;
         }
         prepareCreateModel.read();
         long currentVersionId = (Long) prepareCreateModel.getProperty(REPORT_CURRENT_VERSION_PROP_ID).getValueObject();
         long currentVersionOrder = (Long) prepareCreateModel.getProperty(REPORT_CURRENT_ORDER_PROP_ID).getValueObject();
         AdsTypeDeclaration contextParameterType = null;
         if (initialReport instanceof AdsUserReportClassDef) {
         contextParameterType = ((AdsUserReportClassDef) initialReport).getContextParameterType();
         }

         result[0] = UserExtensionManagerCommon.getInstance().getUserReports().registerReport(module.getId(), id, name, "", currentVersionId, currentVersionOrder, initialReport, contextParameterType, FORMAT_VERSION);
         } catch (final ModelException | ServiceClientException | InterruptedException | IOException ex) {
         env.processException(ex);
         } finally {
         lock.countDown();
         }
         }
         });
         try {
         lock.await();
         } catch (InterruptedException ex) {
         }
         return result[0];*/
    }

    /*private boolean setCurrentVersionId(final ReportVersion version) {
     final CountDownLatch lock = new CountDownLatch(1);
     final boolean[] result = new boolean[]{false};
     final Throwable[] exs = new Throwable[]{null};//NOPMD
     UserExtensionManagerCommon.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
     @Override
     public void execute(IClientEnvironment env) {
     try {
     EntityModel reportModel = EntityModel.openContextlessModel(env, getReportPid(id), REPORT_CLASS_ID, REPORT_EDIT_PRESENTATION_ID);
     reportModel.getProperty(REPORT_CURRENT_VERSION_PROP_ID).setValueObject(version.getVersion());
     if (!reportModel.update()) {
     return;
     }
     result[0] = true;
     } catch (final ModelException | ServiceClientException | InterruptedException ex) {
     env.processException(ex);
     exs[0] = ex;
     } finally {
     lock.countDown();
     }
     }
     });
     try {
     lock.await();
     } catch (InterruptedException ex) {
     }
     if (exs[0] != null) {
     throw new RadixError("Unable to change current version ID", exs[0]);
     }
     return result[0];
     }*/
    public static Pid getReportVersionPID(Id reportId, long version) {
        return new Pid(REPORT_VERSION_CLASS_ID, new ArrayList(Arrays.asList(reportId.toString(), version)));
    }

    public static Pid getReportPid(Id reportId) {
        return new Pid(REPORT_CLASS_ID, reportId.toString());
    }

    public static EntityModel openReportVersionModel(IClientEnvironment env, Id reportId, long version) throws ServiceClientException, InterruptedException {
        return EntityModel.openContextlessModel(env,
                getReportVersionPID(reportId, version),
                REPORT_VERSION_CLASS_ID, REPORT_VERSION_PRESENTATION_ID);
    }
    // private WeakReference<UserReportEditor> editorRef = null;
    //private final Object editorLock = new Object();

    public void openEditor() {
        UserExtensionManagerCommon.getInstance().getUserReportManager().openEditor(this);
        /* UserReportEditor editor;
         synchronized (editorLock) {
         editor = editorRef == null ? null : editorRef.get();
         if (editor == null) {
         editor = new UserReportEditor(this);
         editorRef = new WeakReference<>(editor);
         }
         }
         editor.open();
         editor.requestActive();*/
    }

    public void notifyUnloaded() {
        changeSupport.firePropertyChange("alive", true, false);
    }

    /*public enum PubModifyAction {

     ENABLE,
     DISABLE,
     REMOVE,
     PREVIEW_ENABLE,
     PREVIEW_DISABLE,
     PREVIEW_REMOVE
     }

     public void modifyPublications(final PubModifyAction action, final List<String> result) {

     final CountDownLatch latch = new CountDownLatch(1);
     final Throwable[] exs = new Throwable[1];//NOPMD
     UserExtensionManagerCommon.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
     @Override
     public void execute(IClientEnvironment env) {
     DisableReportPubsRqDocument rqDoc = DisableReportPubsRqDocument.Factory.newInstance();
     DisableReportPubsRq rq = rqDoc.addNewDisableReportPubsRq();
     rq.setAction(action.name().toLowerCase().replace("_", ":"));
     rq.setReportId(id);
     try {
     DisableReportPubsRsDocument rsDoc = (DisableReportPubsRsDocument) env.getEasSession().executeContextlessCommand(DISABLE_COMMAND_ID, rqDoc, DisableReportPubsRsDocument.class);
     if (rsDoc
     != null) {
     DisableReportPubsRs rs = rsDoc.getDisableReportPubsRs();
     if (rs != null) {
     for (DisableReportPubsRs.PubInfo info : rs.getPubInfoList()) {
     result.add(info.getPubName());
     }
     }
     }
     } catch (ServiceClientException | InterruptedException ex) {
     env.processException(ex);
     exs[0] = ex;

     } finally {
     latch.countDown();
     }
     }
     });
     try {
     latch.await();
     } catch (InterruptedException ex) {
     }
     if (exs[0] != null) {
     throw new RadixError("Unable to remove report publications", exs[0]);
     }
     }*/
    public void delete(List<String> deletedPubs) {

        final Throwable[] exs = new Throwable[1];
        final boolean done;
        if (UserExtensionManagerCommon.getInstance().getUserReportManager() != null) {
            done = UserExtensionManagerCommon.getInstance().getUserReportManager().deleteReport(deletedPubs, exs, id);

            /*final CountDownLatch lock = new CountDownLatch(1);
             UserExtensionManagerCommon.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
             @Override
             public void execute(IClientEnvironment env) {
             try {
             EntityModel model = EntityModel.openContextlessModel(env, getReportPid(id), REPORT_CLASS_ID, REPORT_EDIT_PRESENTATION_ID);
             boolean deletionResult = model.delete(true);
             done[0] = deletionResult;
             } catch (final ServiceClientException | InterruptedException ex) {
             env.processException(ex);
             exs[0] = ex;
             } finally {
             lock.countDown();
             }
             }
             });
             try {
             lock.await();
             } catch (InterruptedException ex) {
             }*/
            if (exs[0] != null) {
                throw new RadixError("Unable to delete report " + getName(), exs[0]);
            }
        } else {
            done = true;
        }
        if (done) {
            UserExtensionManagerCommon.getInstance().getUserReports().unregisterReport(this);
            versions.cleanup();
        }
    }

    private AdsDefinition findTopLevelAds(RadixObject obj) {
        if (obj == null) {
            return null;
        }
        for (RadixObject object = obj; object != null; object = object.getContainer()) {
            if (object instanceof AdsDefinition) {
                return ((AdsDefinition) object).findTopLevelDef();
            }
        }
        return null;

    }

    public Set<UserReport> collectDependences() {
        final List<Definition> dependences = new ArrayList<>();

        final AdsUserReportClassDef report = getVersions().getCurrent().findReportDefinition();
        if (report == null) {
            return Collections.emptySet();
        }

        final Set<UserReport> reports = new HashSet<>();
        for (Module module : UserExtensionManagerCommon.getInstance().getReportsSegment().getModules()) {
            if (module instanceof ReportsModule) {
                module.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        AdsDefinition topLevel = findTopLevelAds(radixObject);
                        if (topLevel != null) {
                            Id[] reportId = new Id[1];
                            long[] version = new long[1];
                            if (ReportVersion.parseReportVersion(topLevel.getId(), reportId, version)) {
                                if (reportId[0] == UserReport.this.getId()) {
                                    return;
                                }
                                UserReport rpt = UserExtensionManagerCommon.getInstance().getUserReports().findReportById(reportId[0]);
                                if (rpt != null) {
                                    dependences.clear();
                                    radixObject.collectDependences(dependences);
                                    if (dependences.contains(report)) {
                                        reports.add(rpt);
                                    }
                                }
                            }
                        }
                    }
                }, VisitorProviderFactory.createDefaultVisitorProvider());
            }
        }
        return reports;
    }

    public void save() {
        if (UserExtensionManagerCommon.getInstance().getUserReportManager() != null) {
            contextParameterType = getContextParamType();
            UserExtensionManagerCommon.getInstance().getUserReportManager().save(contextParameterType, this);
        }
        /*UserExtensionManagerCommon.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
         @Override
         public void execute(IClientEnvironment env) {
         try {
         EntityModel model = EntityModel.openContextlessModel(UserExtensionManagerCommon.getInstance().getEnvironment(), getReportPid(id), REPORT_CLASS_ID, REPORT_EDIT_PRESENTATION_ID);
         model.getProperty(REPORT_NAME_PROP_ID).setValueObject(getName());
         model.getProperty(REPORT_DESCRIPTION_PROP_ID).setValueObject(getDescription());
         model.getProperty(REPORT_FORMAT_VERSION_PROP_ID).setValueObject(FORMAT_VERSION);
         TypeDeclarationDocument xDoc = null;
         if (contextParameterType != null) {
         xDoc = TypeDeclarationDocument.Factory.newInstance();
         contextParameterType.appendTo(xDoc.addNewTypeDeclaration());
         }
         model.getProperty(REPORT_CONTEXT_PARAM_TYPE_PROP_ID).setValueObject(xDoc);
         model.update();
         setModified(false);
         currentFormatVersion = FORMAT_VERSION;

         } catch (final ServiceClientException | InterruptedException | ModelException ex) {
         UserExtensionManagerCommon.getInstance().getEnvironment().processException(ex);
         }

         }
         });*/
    }

    public UserReport moveTo(final AdsModule module) throws IOException {
        if (module == null) {
            return this;
        }
        if (module.getId() == moduleId) {
            return this;
        }
        save();
        ReportVersion cv = getVersions().getCurrent();
        long currentVersion = cv.getVersion();
        long currentOrder = cv.getOrder();
        this.getVersions().cleanup();
        final boolean done = UserExtensionManagerCommon.getInstance().getUserReportManager().moveTo(module, getId());
        /*final CountDownLatch lock = new CountDownLatch(1);
         final boolean[] done = new boolean[]{false};
         UserExtensionManagerCommon.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
         @Override
         public void execute(IClientEnvironment env) {
         try {
         EntityModel model = EntityModel.openContextlessModel(env, getReportPid(id), REPORT_CLASS_ID, REPORT_EDIT_PRESENTATION_ID);
         model.getProperty(REPORT_MODULE_ID_PROP_ID).setValueObject(module.getId().toString());
         model.update();
         done[0] = true;
         } catch (final ServiceClientException | InterruptedException | ModelException ex) {
         env.processException(ex);
         } finally {
         lock.countDown();
         }

         }
         });
         try {
         lock.await();
         } catch (InterruptedException ex) {
         }*/
        if (done) {
            UserExtensionManagerCommon.getInstance().unregisterReport(this);
            return UserExtensionManagerCommon.getInstance().getUserReports().registerReport(module.getId(), getId(), getName(), getDescription(), currentVersion, currentOrder, getContextParamType(), currentFormatVersion);
        } else {
            return null;

        }
    }

    public AdsTypeDeclaration getContextParamType() {
        if (currentFormatVersion == 0)//old style reports
        {
            ReportVersion current = getVersions().getCurrent();
            if (current != null) {
                AdsReportClassDef report = current.findReportDefinition();
                if (report != null) {
                    AdsPropertyDef prop = report.findContextParameter();
                    if (prop != null) {
                        return prop.getValue().getType();
                    }
                }
            }
        }
        return contextParameterType;
    }

    public void setContextParamType(AdsTypeDeclaration type) {
        this.contextParameterType = type;
        setModified(true);
    }
//    private Pid getReportPID() {
//        return new Pid(REPORT_CLASS_ID, new ArrayList(Arrays.asList(id.toString())));
//    }
//
//    private EntityModel openReportModel() throws ServiceClientException, InterruptedException {
//        return EntityModel.openContextlessModel(ReportManager.getInstance().getEnvironment(),
//                getReportPID(),
//                REPORT_CLASS_ID, REPORT_EDIT_PRESENTATION_ID);
//    }

    public void exportReport(OutputStream out, List<Long> versions) throws IOException {
        AdsUserReportExchangeDocument xDoc = AdsUserReportExchangeDocument.Factory.newInstance();
        UserReportExchangeType xEx = xDoc.addNewAdsUserReportExchange();
        xEx.setName(name);
        xEx.setDescription(description);
        xEx.setId(id);
        List<UserReportDefinitionType> xDefs = new LinkedList<>();

        List<ReportVersion> sortedList = getVersions().list();

        if (versions != null) {
            for (int i = 0; i < sortedList.size();) {
                ReportVersion v = sortedList.get(i);
                if (!versions.contains(v.version)) {
                    sortedList.remove(i);
                } else {
                    i++;
                }
            }
        }
        ReportVersion current = getVersions().getCurrent();
        boolean addCurrent = false;
        if (sortedList.contains(current)) {
            sortedList.remove(current);
            addCurrent = true;
        }
        Collections.sort(sortedList, new Comparator<ReportVersion>() {
            @Override
            public int compare(ReportVersion o1, ReportVersion o2) {
                return o1.version == o2.version ? 0 : o1.version > o2.version ? 1 : -1;
            }
        });
        if (addCurrent) {
            sortedList.add(0, current);
        }

        final Map<Id, AdsImageDef> usedImages = new HashMap<>();
        final List<Definition> dependences = new ArrayList<>();
        for (ReportVersion v : sortedList) {
            UserReportDefinitionType xDef = UserReportDefinitionType.Factory.newInstance();
            if (v.saveXml(xDef) != null) {
                xDefs.add(xDef);
            }
            AdsReportClassDef report = v.findReportDefinition();
            if (report != null) {

                report.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        dependences.clear();
                        radixObject.collectDependences(dependences);
                        for (Definition def : dependences) {
                            if (def instanceof AdsImageDef) {
                                usedImages.put(def.getId(), (AdsImageDef) def);
                            }
                        }
                    }
                }, VisitorProviderFactory.createDefaultVisitorProvider());
            }
        }

        if (!usedImages.isEmpty()) {
            File imagesZipFile = File.createTempFile("imgJarTmp", "imgJarTmp");

            FileOutputStream zipOut = null;
            ZipOutputStream zip = null;
            boolean done = false;
            try {
                zipOut = new FileOutputStream(imagesZipFile);
                zip = new ZipOutputStream(zipOut);

                final Set<Map.Entry<Id, AdsImageDef>> entries = usedImages.entrySet();
                for (Map.Entry<Id, AdsImageDef> entry : entries) {
                    final Id oldId = entry.getKey();
                    final AdsImageDef image = entry.getValue();
                    File inputFile = image.getImageFile();
                    byte[] imageBytes = FileUtils.readBinaryFile(inputFile);

                    ZipEntry e = new ZipEntry(inputFile.getName().replace(oldId.toString(), oldId.toString()));
                    try {
                        zip.putNextEntry(e);
                        zip.write(imageBytes);
                    } finally {
                        zip.closeEntry();
                    }
                }
                /* for (Id oldId : usedImages.keySet()) {
                 final AdsImageDef image = usedImages.get(oldId);
                 File inputFile = image.getImageFile();
                 byte[] imageBytes = FileUtils.readBinaryFile(inputFile);

                 ZipEntry e = new ZipEntry(inputFile.getName().replace(oldId.toString(), oldId.toString()));
                 try {
                 zip.putNextEntry(e);
                 zip.write(imageBytes);
                 } finally {
                 zip.closeEntry();
                 }
                 }*/
                done = true;
            } catch (IOException e) {
                //ignore. lost of images is not so important
            } finally {
                if (zip != null) {
                    try {
                        zip.close();
                    } catch (IOException e) {
                    }
                }
                if (zipOut != null) {
                    try {
                        zipOut.close();
                    } catch (IOException e) {
                    }
                }
                if (done) {
                    byte[] zipBytes = FileUtils.readBinaryFile(imagesZipFile);
                    xEx.setImages(Base64.encode(zipBytes));
                }
                FileUtils.deleteFile(imagesZipFile);
            }
        }
        xEx.setAdsUserReportDefinitionArray(xDefs.toArray(new UserReportDefinitionType[xDefs.size()]));
        XmlFormatter.save(xDoc, out);
    }

    public void importNewVersion(InputStream in) throws IOException {
        try {
            AdsUserReportExchangeDocument xDoc = AdsUserReportExchangeDocument.Factory.parse(in);
            importNewVersion(xDoc);
        } catch (XmlException | IOException ex) {
            throw new IOException("Error on reading user report exchange data", ex);
        }
    }

    public void importNewVersion(AdsUserReportExchangeDocument xDoc) throws IOException {
        try {

            UserReportExchangeType xEx = xDoc.getAdsUserReportExchange();
            if (xEx.getAdsUserReportDefinitionList().isEmpty()) {
                throw new IOException("No report version information found");
            }

            boolean hasData = false;
            for (int i = 0; i < xEx.getAdsUserReportDefinitionList().size(); i++) {
                UserReportDefinitionType xDef = xEx.getAdsUserReportDefinitionList().get(i);
                if (xDef.getReport() != null) {
                    hasData = true;
                    break;
                }
            }
            if (!hasData) {
                return;
            }

            ReportsModule context = (ReportsModule) UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().findById(this.moduleId);
            if (context == null) {
                return;
            }

            //import images
            Map<Id, Id> idReplaceMap = new HashMap<>();
            if (xEx.getImages() != null) {
                byte[] imgZipBytes = Base64.decode(xEx.getImages());
                File zipFile = null;
                try {
                    zipFile = File.createTempFile("aaa", "bbb");
                    FileUtils.writeBytes(zipFile, imgZipBytes);
                    ZipFile zip = new ZipFile(zipFile);
                    Enumeration<? extends ZipEntry> entries = zip.entries();

                    while (entries.hasMoreElements()) {
                        ZipEntry e = entries.nextElement();
                        if (e.getName().endsWith(".xml")) {
                            continue;
                        }
                        byte[] bytes = FileUtils.getZipEntryByteContent(e, zip);
                        File output = new File(context.getDirectory(), e.getName());
                        Id oldId = Id.Factory.loadFrom(FileUtils.getFileBaseName(output));
                        FileUtils.writeBytes(output, bytes);
                        AdsImageDef imageDef = context.getImages().importImage(output);
                        FileUtils.deleteFile(output);
                        idReplaceMap.put(oldId, imageDef.getId());
                    }
                } catch (IOException e) {
                    //ignore
                } finally {
                    if (zipFile != null) {
                        FileUtils.deleteFile(zipFile);
                    }
                }

                if (!idReplaceMap.isEmpty()) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    xEx.setImages(null);
                    XmlFormatter.save(xDoc, out);
                    String asString = new String(out.toByteArray(), FileUtils.XML_ENCODING);
                    out = null;
                    for (Map.Entry<Id, Id> e : idReplaceMap.entrySet()) {
                        asString = asString.replace(e.getKey().toString(), e.getValue().toString());
            }
                    xDoc = AdsUserReportExchangeDocument.Factory.parse(asString);
                    xEx = xDoc.getAdsUserReportExchange();
                }
            }

            UserReport.ReportVersion current = this.getVersions().getCurrent();
            UserReport.ReportVersion newCurrent = null;
            this.setDescription(xEx.getDescription());

            for (int i = 0; i < xEx.getAdsUserReportDefinitionList().size(); i++) {
                UserReportDefinitionType xDef = xEx.getAdsUserReportDefinitionList().get(i);
                if (xDef.getReport() != null) {
                    UserReport.ReportVersion newVersion = getVersions().addNewVersion(xEx.getAdsUserReportDefinitionList().get(i));
                    if (newCurrent == null) {
                        newCurrent = newVersion;
                    }
                }
            }

            UserExtensionManagerCommon.getInstance().startBuild();
            try {
                save();
                for (UserReport.ReportVersion v : getVersions().list()) {
                    v.save();

                }
            } finally {
                UserExtensionManagerCommon.getInstance().finishBuild();
            }
            List<UserReport.ReportVersion> locked = new LinkedList<>();
            try {

                for (UserReport.ReportVersion v : getVersions().list()) {
                    v.lockDefinitionSearch(true);
                    locked.add(v);
                }

                if (newCurrent != null /*&& (UserExtensionManagerCommon.getInstance().getUserReportManager()!=null)*/) {
                    UserExtensionManagerCommon.getInstance().makeCurrent(false, newCurrent, this);
                    if (current != null) {
                        getVersions().removeVersion(current);
                    }
                }

                for (UserReport.ReportVersion v : locked) {
                    v.reload();
                }
            } finally {
                for (UserReport.ReportVersion v : locked) {
                    v.lockDefinitionSearch(false);
                }
                for (UserReport.ReportVersion v : getVersions().list()) {
                    v.save();
                }
            }

            context.invalidateDependences();

        } catch (XmlException | IOException ex) {
            throw new IOException("Error on reading user report exchange data", ex);
        } finally {
            this.getVersions().fireChange();
        }

    }

    public ReportsModule findModule() {
        return UserExtensionManagerCommon.getInstance().findModuleById(getModuleId());
    }
}
