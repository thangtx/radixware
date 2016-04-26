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
package org.radixware.kernel.common.userreport.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.defs.ads.userfunc.UdsObserver;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.repository.ReportsModule;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.common.userreport.repository.UserReports;
import org.radixware.kernel.common.userreport.extrepository.UserExtAdsSegment;
import org.radixware.kernel.common.userreport.extrepository.UserExtLayerRepository;
import org.radixware.kernel.common.userreport.extrepository.UserExtRepository;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlSchemes;
import org.radixware.kernel.common.userreport.repository.role.AppRole;
import org.radixware.kernel.common.userreport.repository.role.AppRoles;
import org.radixware.kernel.common.utils.FileUtils;

public class UserExtensionManagerCommon implements BuildOptions.UserModeHandler {

    protected UdsObserver observer;
    protected IClientEnvironment env;
    private static UserExtensionManagerCommon instance;//= new UserExtensionManagerCommon();
    protected File projectDir;
    protected boolean isReady = false;
    protected final UserReports userReports;
    protected final AppRoles appRoles;
    protected final MsdlSchemes msdlSchemes;
    private final IBuildEnvironment buildEnvironment;

    public static UserExtensionManagerCommon getInstance() {
        synchronized (UserExtensionManagerCommon.class) {
            if (instance == null) {
                instance = new UserExtensionManagerCommon();
            }
            return instance;
        }
    }

    public static void setInstance(UserExtensionManagerCommon inst) {
        synchronized (UserExtensionManagerCommon.class) {
            instance = inst;
        }
    }

    protected UserExtensionManagerCommon() {
        userReports = new UserReports();
        appRoles = new AppRoles();
        msdlSchemes = new MsdlSchemes();
        buildEnvironment = new org.radixware.kernel.common.userreport.common.BuildUserReportEnvironment();
        observer = new ReportUdsObserverCommon();
    }

    public IBuildEnvironment getBuildEnvironment() {
        return buildEnvironment;
    }

    public boolean isReady() {
        return isReady;
    }

    public IUserRoleManager getUserRoleManager() {
        return null;
    }

    public IUserReportManager getUserReportManager() {
        return null;
    }

    public IMsdlSchemeManager getMsdlSchemesManager() {
        return null;
    }

    //Report
    /*public UserReport addNewReport(ReportsModule reportModule){        
     return null;
     }
    
     public ReportsModule addNewModule() {
     return null;
     }
    
     public void importReport(ReportsModule context, InputStream in) throws IOException {
        
     }
    
     public boolean createReportModule(final IRepositorySegment segment, final ReportsModule modul){
     return false;
     }
    
     public  UserReport createReport(final ReportsModule module, final String name, final Id id, final AdsReportClassDef initialReport) {
     return null;
     }
     
     public void deleteModule(Id id) {
       
     }
     
     public void addNewVersion(UserReport.ReportVersion src){
         
     }
     
     public boolean removeVersion(final UserReport.ReportVersion version, final Id id) {
     return false;
     }
     
     public UserReport.ReportVersion createVersionImpl(final AdsUserReportDefinitionDocument xDoc,final Id id,final UserReport userReport) {
     return null;
     }
     
     public void save(final AdsTypeDeclaration contextParameterType,final UserReport report) {         
     }
    
     
     public boolean moveTo(final AdsModule module,final Id id) throws IOException{
     return false;
     }

     public boolean makeCurrent( boolean interactive,UserReport.ReportVersion version,UserReport handle) {
     return false;
     }
      
     public boolean deleteReport(List<String> deletedPubs,final Throwable[] exs, final Id id) {
     return false;
     }
    
     public void openEditor(UserReport report) {
         
     }*/
    //public static void setUtils(IExtManagetUtils u){
    //    utils=u;
    //}
    // public void processException(BackingStoreException ex){
    // utils.processException(ex);
    // }
    //public void updateTitle() {
    //    utils.updateTitle();
    //}
    //public void setMode(String mode) {
    //    utils.setMode(mode);
    //}
    public void setEnvironment(IClientEnvironment env) {
        this.env = env;
    }

    public IClientEnvironment getEnvironment() {
        //synchronized (this) {
        //    if (env == null) {
        //         env = new DesignerClientEnvironment(env);
        //     }
        return env;
        //}
    }

    public void unregisterReport(UserReport report) {
        getUserReports().unregisterReport(report);
    }

    //public UserReport registerReport(Id moduleId, Id id, String name, String description, long currentVersion, long currentVersionOrder, AdsTypeDeclaration contextParamType, long currentFormatVersion) throws IOException{
    //    return userReports.registerReport(moduleId, id, name, description, currentVersion, currentVersionOrder, contextParamType, currentFormatVersion);
    //}
    //public UserReport registerReport(Id moduleId, Id id, String name, String description, long currentVersion, long currentVersionOrder, AdsReportClassDef reportData, AdsTypeDeclaration contextParamType, long currentFormatVersion) throws IOException{
    //    return utils.registerReport(moduleId, id, name, description, currentVersion, currentVersionOrder, contextParamType, currentFormatVersion);
    //}
    public ReportsModule findModuleById(Id id) {
        return userReports.findModuleById(id);
    }

    public UserExtAdsSegment getReportsSegment() {
        /*if (isReady) {
         int sc = 0;
         while (!isReady) {
         if (sc > 20) {
         return null;
         }
         try {
         Thread.sleep(100);
         sc++;
         } catch (InterruptedException e) {
         }
         }*/
        return UserExtRepository.getInstance().getUserExtSegment();
        //} else {
        //    return null;
        //}
    }

    public File getProjectDir(Id id) {
        return new File(UserExtRepository.getInstance().getProjectDir(), id.toString());
    }
    //public boolean isReady() {
    //    return utils.isReady();
    //}
    // public static String sessionId = UUID.randomUUID().toString();
    private volatile boolean isOpening = false;
    public static final String sessionId = UUID.randomUUID().toString();

    public boolean openReports() throws IOException {
        //open from dir mode
        synchronized (this) {
            if (!isReady && !isOpening) {
                isOpening = true;
                projectDir = File.createTempFile("rwurs", "root");
                projectDir.delete();
                projectDir.mkdirs();
                File markerFile = new File(projectDir, "uxpf.rw");
                try (FileOutputStream stream = new FileOutputStream(markerFile)) {
                    stream.write(sessionId.getBytes("UTF-8"));
                }
                UserExtRepository.getInstance().initialize(projectDir, env);
                isReady = true;

                isOpening = false;
            }
            return true;
        }
    }
    private IUserReportRequestExecutor urExecutor = null;

    public IUserReportRequestExecutor getUFRequestExecutor() {
        return urExecutor;
    }

    public void setUFRequestExecutor(IUserReportRequestExecutor urExecutor) {
        this.urExecutor = urExecutor;
    }

    public UserReports getUserReports() {
        return userReports;
    }

    public AppRoles getAppRoles() {
        return appRoles;
    }

    public MsdlSchemes getMsdlSchemes() {
        return msdlSchemes;
    }
    
    protected void doCleanup() {
        userReports.close();
        appRoles.close();
        msdlSchemes.close();
        env = null;
    }

    public void cleanup() {
        try {
            UserExtRepository.getInstance().cleanup();
            doCleanup();
        } finally {
            if (projectDir != null) {
                FileUtils.deleteDirectory(projectDir);
            }
        }
    }

    public void close() {
        try {
            UserExtRepository.getInstance().close();
            doCleanup();
        } finally {
            if (projectDir != null) {
                FileUtils.deleteDirectory(projectDir);
            }
        }
    }

    public interface IReportModifiedListener {

        public void reportModificationStateChanged(UserReport src);
    }

    public class ReportModifiedSupport {

        private final List<IReportModifiedListener> listeners = new LinkedList<>();

        public void addListener(IReportModifiedListener l) {
            synchronized (listeners) {
                if (!listeners.contains(l)) {
                    listeners.add(l);
                }
            }
        }

        public void removeListener(IReportModifiedListener l) {
            synchronized (listeners) {
                listeners.remove(l);
            }
        }

        public void fireChange(UserReport report) {
            final List<IReportModifiedListener> list;
            synchronized (listeners) {
                list = new ArrayList<>(listeners);
            }
            for (IReportModifiedListener l : list) {
                l.reportModificationStateChanged(report);
            }
        }
    }
    private ReportModifiedSupport modifiedSupport = new ReportModifiedSupport();

    public ReportModifiedSupport getModifiedSupport() {
        return modifiedSupport;
    }

    public void setReportModified(UserReport report, boolean modified) {
        modifiedSupport.fireChange(report);
    }

    public List<UserReport> listReports(Id moduleId) {
        return userReports.listReports(moduleId);
    }
    protected volatile boolean buildMode = false;

    @Override
    public void finishBuild() {
        buildMode = false;
    }

    @Override
    public void startBuild() {
        buildMode = true;
    }

    @Override
    public void acceptUserDefinitionRuntime(Id id, File jarFile) {
        if (EDefinitionIdPrefix.APPLICATION_ROLE.equals(id.getPrefix())) {
            AppRole appRole = getAppRoles().findAppRole(id);
            if (appRole != null) {
                try {
                    appRole.updateRuntime(jarFile);
                } catch (IOException ex) {
                    UserExtensionManagerCommon.getInstance().getUFRequestExecutor().processException(ex);
                }
            }
        } else {
            Id[] reportId = new Id[1];
            long[] reportVersion = new long[1];
            if (UserReport.ReportVersion.parseReportVersion(id, reportId, reportVersion)) {//our variant
                UserReport report = getUserReports().findReportById(reportId[0]);
                if (report != null) {
                    UserReport.ReportVersion version = reportVersion[0] == -1 ? report.getVersions().getCurrent() : report.getVersions().get(reportVersion[0]);
                    if (version != null) {
                        try {
                            version.updateRuntime(jarFile);
                        } catch (IOException ex) {
                            UserExtensionManagerCommon.getInstance().getUFRequestExecutor().processException(ex);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void acceptBuildTarget(Definition def, Map<Id, Id> idsMap) {
        if (def instanceof AdsRoleDef) {
            ((AdsRoleDef) def).resetRuntimeId();
            idsMap.put(((AdsRoleDef) def).getRuntimeId(), def.getId());
        } else if (def instanceof AdsUserReportClassDef) {
            ((AdsUserReportClassDef) def).resetRuntimeId();
            idsMap.put(((AdsUserReportClassDef) def).getRuntimeId(), def.getId());
            AdsReportModelClassDef model = (AdsReportModelClassDef) ((AdsUserReportClassDef) def).getPresentations().getModel();
            idsMap.put(model.getRuntimeId(), model.getId());
        } else if (def instanceof AdsLocalizingBundleDef) {
            ((AdsLocalizingBundleDef) def).resetRuntimeId();
        } else if (def instanceof AdsReportModelClassDef) {
            AdsUserReportClassDef report = (AdsUserReportClassDef) ((AdsReportModelClassDef) def).getOwnerClass();
            if (idsMap.get(report.getRuntimeId()) == null) {
                report.resetRuntimeId();
                idsMap.put(report.getRuntimeId(), report.getId());
            }
            idsMap.put(((AdsReportModelClassDef) def).getRuntimeId(), def.getId());
        }
    }

    public void compileOnSave(final AdsDefinition whatToCompile, boolean sync) {
        getUFRequestExecutor().compile(whatToCompile, false, sync);
    }

    public void initializeExtRepository(File projectDir, IClientEnvironment env, final UserExtLayerRepository layerRepository) throws IOException {
    }

    public void uploadExtRepository(final UserExtLayerRepository layerRepository) {
    }

    public void exportReports(final UserReports reports, final ReportsModule context, File directory) {
        new ExportReportUtil().exportReports(UserExtensionManagerCommon.getInstance().getUserReports(), null, directory);
    }

    public void exportReport(final ExportReportUtil.SaveReportTask saveReportTask) {
        //task = new MyRunnable(false, isDbEntity, isParentRef, filter);
        saveReportTask.run();
    }

    public void processException(final IOException ex) {
        /*SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
         DialogUtils.messageError(ex);
         }
         });*/
    }

    public boolean fileExistConfirm(File file) {
        return true;
        //return DialogUtils.messageConfirmation("File " + file.getAbsolutePath() + " is already exists. Overwite?");
    }

    //@Override
    public boolean makeCurrent(boolean interactive, UserReport.ReportVersion version, UserReport handle) {
        // return makeCurrent(null,  interactive, version, handle);
        //}

        //public boolean makeCurrent(ProgressHandle progressHandle, boolean interactive,UserReport.ReportVersion version,UserReport handle) {
        //LifecycleManager.getDefault().saveAll();
        if (version.isCurrent()) {
            return true;
        }

        // if (progressHandle != null) {
        //     progressHandle.progress("Compile version " + version.getName());
        // }
        AdsUserReportClassDef reportClass = version.findReportDefinition();
        if (reportClass == null) {
            return false;
        }
        compileOnSave(reportClass, true);

        // if (interactive && !UserExtensionManager.getInstance().canClose("Compilation errors detected. Continue?", false)) {
        //    return false;
        // }
        //if (progressHandle != null) {
        //     progressHandle.progress("Clean up report data for " + version.getName());
        // }
        //first step - cleanup this version
        version.lockDefinitionSearch(true);
        try {
            version.cleanup();
            UserReport.ReportVersion current = handle.getVersions().getCurrent();
            if (current == null) {
                return false;
            }
            //second step - cleanup current version
            //if (progressHandle != null) {
            //      progressHandle.progress("Clean up report data for " + current.getName());
            //  }
            current.lockDefinitionSearch(true);
            try {
                current.cleanup();
                //third step - set this version as current
                //    if (progressHandle != null) {
                //        progressHandle.progress("Registering new current version " + current.getName());
                //     }
                if (!getUFRequestExecutor().setCurrentVersionId(version, handle.getId())) {
                    return false;
                }
                //     if (progressHandle != null) {
                //          progressHandle.progress("Applying changes... ");
                //      }
                handle.getVersions().setCurrent(version);
            } finally {
                current.lockDefinitionSearch(false);
            }
        } finally {
            version.lockDefinitionSearch(false);
        }

        reportClass = version.findReportDefinition();
        if (reportClass != null) {
            compileOnSave(reportClass, true);
        }
        return true;
    }

    public UdsObserver getObserver() {
        return observer;
    }
    /*private boolean setCurrentVersionId(final UserReport.ReportVersion version, final Id id) {
     final CountDownLatch lock = new CountDownLatch(1);
     final boolean[] result = new boolean[]{false};
     final Throwable[] exs = new Throwable[]{null};//NOPMD
     UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
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
}
