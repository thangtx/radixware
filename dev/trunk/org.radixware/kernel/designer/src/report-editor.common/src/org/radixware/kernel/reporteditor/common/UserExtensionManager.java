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
package org.radixware.kernel.reporteditor.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ProxySelector;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.LifecycleManager;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.builder.BuildActionExecutor;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.api.IBuildProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.RadixProblemRegistry;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.ExportReportUtil;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import static org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon.sessionId;
import org.radixware.kernel.common.userreport.extrepository.UserExtAdsSegment;
import org.radixware.kernel.common.userreport.extrepository.UserExtLayerRepository;
import org.radixware.kernel.common.userreport.extrepository.UserExtRepository;
import org.radixware.kernel.common.userreport.repository.ReportsModule;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.common.userreport.repository.UserReport.ReportVersion;
import org.radixware.kernel.common.userreport.repository.UserReports;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlScheme;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlSchemesModule;
import org.radixware.kernel.common.userreport.repository.role.AppRole;
import org.radixware.kernel.common.userreport.repository.role.RolesModule;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.common.dialogs.build.DesignerBuildEnvironment;
import org.radixware.kernel.designer.common.dialogs.build.FlowLoggerFactory;
import org.radixware.kernel.designer.common.dialogs.check.CheckResultsTopComponent;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.eas.client.DesignerClientApplication;
import org.radixware.kernel.designer.eas.client.DesignerClientEnvironment;
import org.radixware.kernel.reporteditor.common.explorer.UdsExplorerTopComponent;
import org.radixware.kernel.reporteditor.tree.UserReportNode;

public class UserExtensionManager extends UserExtensionManagerCommon {

    private final static UserExtensionManager instance = new UserExtensionManager();

    public static UserExtensionManager getInstance() {
        return instance;
    }
    private ChangeSupport changeSupport = new ChangeSupport(this);
    private final java.util.Map<UserReport, UserReportNode> reportNodes = new WeakHashMap<>();
    private String mode = "";
    private List<CopiedReportInfo> copiedReportInfo;
    private final UserReportManager userReportManager;
    private final UserRoleManager userRoleManager;
    private final MsdlSchemeManager msdlSchemesManager;

    @Override
    public UserRoleManager getUserRoleManager() {
        return userRoleManager;
    }

    @Override
    public UserReportManager getUserReportManager() {
        return userReportManager;
    }

    @Override
    public MsdlSchemeManager getMsdlSchemesManager() {
        return msdlSchemesManager;
    }

    public class CopiedReportInfo {

        private final AdsUserReportClassDef copiedReport;
        private AdsLocalizingBundleDef sb;
        private boolean isVisible;
        private boolean isCurrent;

        CopiedReportInfo(final AdsUserReportClassDef copiedReport,/* final List<AdsUserReportDefinitionDocument> copiedReportVersions*/
                boolean isVisible, boolean isCurrent, AdsLocalizingBundleDef sb) {
            this.copiedReport = copiedReport;
            this.isVisible = isVisible;
            this.isCurrent = isCurrent;
            this.sb = sb;
        }

        public AdsUserReportClassDef getCopiedReport() {
            return copiedReport;
        }

        public boolean isVisible() {
            return isVisible;
        }

        public boolean isCurrent() {
            return isCurrent;
        }

        public AdsLocalizingBundleDef getStrings() {
            return sb;
        }
    }

    private UserExtensionManager() {
        super();
        env = new DesignerClientEnvironment();
        observer = new ReportUdsObserver();
        WindowManager.getDefault().getMainWindow().setTitle("User Extensions Designer");
        userReportManager = new UserReportManager();
        userRoleManager = new UserRoleManager();
        msdlSchemesManager = new MsdlSchemeManager();
        ProxySelector ps = ProxySelector.getDefault();
        Logger.getLogger(UserExtensionManager.class.getName()).log(Level.SEVERE, ps == null ? "No proxy selector found" : "Proxy selector: " + ps.getClass().getName());
        ProxySelector.setDefault(null);
        setInstance(this);
        setUFRequestExecutor(new URRequestExecutor());
    }

    public void addCopiedReportInfo(AdsUserReportClassDef userreport, boolean isVisible, boolean isCurrent, AdsLocalizingBundleDef sb/*final  List<AdsUserReportDefinitionDocument> copiedReportVersions*/) {
        if (copiedReportInfo == null) {
            copiedReportInfo = new ArrayList<>();
        }
        copiedReportInfo.add(new CopiedReportInfo(userreport, isVisible, isCurrent, sb));
    }

    public List<CopiedReportInfo> getCopiedReportInfo() {
        return copiedReportInfo;
    }

    public void removeCopiedReportInfo() {
        copiedReportInfo = null;
    }
    
    boolean needLoadReportModules() {
        final String mode = (String) Config.getValue("features");
        if (mode != null) {
            switch (mode) {
                case "role-editor":
                case "msdl-editor":
                    return false;

                case "report-editor":
                default:
                    return true;
            }
        }
        return true;
    }

    public void updateTitle() {
        String title;
        switch (mode) {
            case "report-editor":
                title
                        = DesignerClientApplication.getProductName()
                        + " User-Defined Reports";
                break;
            case "role-editor":
                title
                        = DesignerClientApplication.getProductName()
                        + " Application Roles";

                break;
            case "msdl-editor":
                title
                        = DesignerClientApplication.getProductName()
                        + " Msdl Schemes";

                break;
            default:
                title = "RadixWare User Extensions Designer";
        }
        final String finalTitle;

        if (getEnvironment().getUserName() != null) {
            finalTitle = title + " - " + getEnvironment().getUserName();
        } else {
            finalTitle = title;
        }
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                WindowManager.getDefault().getMainWindow().setTitle(finalTitle);
            }
        });

    }

    public void setMode(String mode) {
        this.mode = mode;
        updateTitle();
    }

    public UserReportNode findOrCreateNode(UserReport report) {
        if (report == null) {
            return null;
        }
        synchronized (reportNodes) {
            UserReportNode node = reportNodes.get(report);
            if (node == null) {
                node = new UserReportNode(report);
                reportNodes.put(report, node);
            }
            return node;
        }
    }

    @Override
    public DesignerClientEnvironment getEnvironment() {
        return (DesignerClientEnvironment) env;
    }

    /* public UserReports getUserReports() {
     return userReports;
     }

     public AppRoles getAppRoles() {
     return appRoles;
     }
    
     public MsdlSchemes getMsdlSchemes() {
     return msdlSchemes;
     }*/
    @Override
    public UserExtAdsSegment getReportsSegment() {
        if (isReady) {
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
            }
            return UserExtRepository.getInstance().getUserExtSegment();
        } else {
            return null;
        }
    }

    @Override
    public boolean isReady() {
        return isReady;
    }
    //public final static String sessionId = UUID.randomUUID().toString();
    private volatile boolean isOpening = false;

    @Override
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
                UserExtRepository.getInstance().initialize(projectDir, getEnvironment());

                //UserExtRepository.getInstance().upload();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressUtils.showProgressDialogAndRun(new Runnable() {
                            @Override
                            public void run() {
                                openProject((DesignerClientEnvironment) env, projectDir);
                            }
                        }, "Initializing...");

                    }
                });
                t.start();

            }
            return true;
        }
    }

    private void openProject(final DesignerClientEnvironment env, final File projectDir) {
        //try to restore user password (if any)
        synchronized (this) {
            try {
                Object epwd = Config.getValue("epwd");
                char[] pwd = null;
                if (epwd instanceof char[]) {
                    pwd = (char[]) epwd;
                }

                if (env.connect(pwd)) {
                    //   try {
                    //      project = new UserExtensionProject(projectDir);
                    updateTitle();
//                        if (!OpenProjects.getDefault().isProjectOpen(project)) {
//                            OpenProjects.getDefault().open(new Project[]{project}, false /*
//                                     * open sub projects
//                                     */, true /*
//                                     * assinch
//                                     */);
//                        }

                    UserExtRepository.getInstance().upload();
                    isReady = true;
                    fireChange();
                    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
                        @Override
                        public void run() {
                            TopComponent tc = WindowManager.getDefault().findTopComponent("UdsExplorerTopComponent");
                            if (tc != null) {
                                tc.open();
                                tc.requestActive();
                            }
                            if (tc instanceof UdsExplorerTopComponent) {
                                ((UdsExplorerTopComponent) tc).load();
                            }
                            //   ProjectUtilities.selectAndExpandProject(project);
                        }
                    });

//                    } catch (IOException e) {
//                        Exceptions.printStackTrace(e);
//                        LifecycleManager.getDefault().exit(231);
//                    }
                }
            } finally {
                isOpening = false;
            }
        }
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    private void fireChange() {
        changeSupport.fireChange();
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    @Override
    public void acceptUserDefinitionRuntime(Id id, File jarFile) {
        if (EDefinitionIdPrefix.APPLICATION_ROLE.equals(id.getPrefix())) {
            AppRole appRole = getAppRoles().findAppRole(id);
            if (appRole != null) {
                try {
                    appRole.updateRuntime(jarFile);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        } else if (EDefinitionIdPrefix.MSDL_SCHEME.equals(id.getPrefix())) {
            MsdlScheme msdlScheme = getMsdlSchemes().findMsdlScheme(id);
            if (msdlScheme != null) {
                try {
                    msdlScheme.updateRuntime(jarFile);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
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
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            }
        }
    }
    private volatile RequestExecutor executor;

    public RequestExecutor getRequestExecutor() {
        if (executor == null) {
            executor = new RequestExecutor() {
                @Override
                protected IClientEnvironment getEnvironment() {
                    return UserExtensionManager.this.getEnvironment();
                }
            };
        }
        return executor;
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

    @Override
    protected void doCleanup() {
        isReady = false;
        userReports.close();
        appRoles.close();
        msdlSchemes.close();
        env = null;
        reportNodes.clear();
        changeSupport = new ChangeSupport(this);
        if (executor != null) {
            executor.stop();
            executor = null;
        }
    }

    @Override
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

    @Override
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

    /* public class ReportModifiedSupport {

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

     private void fireChange(UserReport report) {
     final List<IReportModifiedListener> list;
     synchronized (listeners) {
     list = new ArrayList<>(listeners);
     }
     for (IReportModifiedListener l : list) {
     l.reportModificationStateChanged(report);
     }
     }
     }*/
    private ReportModifiedSupport modifiedSupport = new ReportModifiedSupport();

    @Override
    public ReportModifiedSupport getModifiedSupport() {
        return modifiedSupport;
    }

    @Override
    public void setReportModified(UserReport report) {
        modifiedSupport.fireChange(report);
    }
    // private ExecutorService compileOnSaveExecutor = Executors.newFixedThreadPool(1);
    private final Lock compileOnSaveLock = new ReentrantLock();
    //private final Lock answerCanCloseLock = new ReentrantLock();
    private boolean errorsOnLastBuild = false;

    private class CompileOnSaveTask implements Runnable {

        protected final RadixObject[] whatToCompile;
        
        public CompileOnSaveTask(AdsDefinition whatToCompile) {
            this.whatToCompile = new RadixObject[]{whatToCompile};
        }

        public CompileOnSaveTask(RadixObject[] whatToCompile) {
            this.whatToCompile = whatToCompile;
        }
        
        protected void doCompile(IBuildEnvironment env) {
             try {
                compileOnSaveLock.lock();
                if (whatToCompile != null) {
                    List<RadixObject> filtered = new LinkedList<>();
                    for (RadixObject obj : whatToCompile) {
                        if (obj != null && obj.getBranch() != null) {
                            filtered.add(obj);
                        }
                    }
                    if (!filtered.isEmpty()) {
                        BuildActionExecutor executor = new BuildActionExecutor(env);
                        executor.execute(filtered.toArray(new RadixObject[filtered.size()]), BuildActionExecutor.EBuildActionType.COMPILE_SINGLE, null, true, UserExtensionManager.getInstance());
                    }
                }
            } finally {
                compileOnSaveLock.unlock();
            }
        }
        
        @Override
        public void run() {
            DesignerBuildEnvironment env = new DesignerBuildEnvironment(false, BuildActionExecutor.EBuildActionType.COMPILE_SINGLE);
            IBuildProblemHandler handler = env.getBuildProblemHandler();
            doCompile(env);
            errorsOnLastBuild = handler.wasErrors();

            final CheckResultsTopComponent checkResults = CheckResultsTopComponent.findInstance();
            if (!checkResults.isEmpty()) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        checkResults.open();
                        checkResults.requestVisible();
                        checkResults.requestActive();
                    }
                });
            }
        }
    }
    
    private final class CompileAllSubTask extends CompileOnSaveTask {
        
        private final IFlowLogger compileAllLogger;
        private final ECompileAllOrder order;

        public CompileAllSubTask(RadixObject[] whatToCompile, IFlowLogger compileAllLogger, ECompileAllOrder order) {
            super(whatToCompile);
            this.order = order;
            this.compileAllLogger = compileAllLogger;
        }

        public CompileAllSubTask(AdsDefinition whatToCompile, IFlowLogger compileAllLogger, ECompileAllOrder order) {
            super(whatToCompile);
            this.order = order;
            this.compileAllLogger = compileAllLogger;
        }
        
        @Override
        public void run() {
            final DesignerBuildEnvironment env = new DesignerBuildEnvironment(false, BuildActionExecutor.EBuildActionType.COMPILE_SINGLE, compileAllLogger);
            final BuildOptions buildOptions = env.getBuildOptions();
            buildOptions.setSuppressProblemsClear(order != ECompileAllOrder.FIRST);
            IBuildProblemHandler handler = env.getBuildProblemHandler();
            doCompile(env);
            errorsOnLastBuild = order != ECompileAllOrder.FIRST ? handler.wasErrors() || errorsOnLastBuild : handler.wasErrors();

            final CheckResultsTopComponent checkResults = CheckResultsTopComponent.findInstance();
            if (!checkResults.isEmpty()) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        checkResults.open();
                        checkResults.requestVisible();
                        checkResults.requestActive();
                    }
                });
            }
            
            if (order == ECompileAllOrder.LAST) {
                compilingAllReportsStarted = false;
            }
        }
    }
    
    private final Queue<CompileOnSaveTask> compileTasks = new LinkedList<>();
    private Thread compileTaskProcessor;
    private final Object lock = new Object();
    private volatile boolean compilingAllReportsStarted = false;
    
    private static enum ECompileAllOrder {
        FIRST, LAST, MIDDLE
    }
    
    public void compileAllReports(final List<RadixObject[]> reports) {
        if (reports.isEmpty() || compilingAllReportsStarted) {
            return;
        }
        compilingAllReportsStarted = true;
        
        final IFlowLogger compileAllLogger = 
                FlowLoggerFactory.newBuildLogger(BuildActionExecutor.EBuildActionType.COMPILE_SINGLE);
        
        startCompileTaskProcessor();
        
        int index = 0;
        for (; index < reports.size(); index++) {
            RadixObject[] whatToCompile = reports.get(index);
            ECompileAllOrder order;
            if (index == 0) {
                order = ECompileAllOrder.FIRST;
            } else if (index == reports.size() - 1) {
                order = ECompileAllOrder.LAST;
            } else {
                order = ECompileAllOrder.MIDDLE;
            }
            
            submitNextCompileAllTask(new CompileAllSubTask(whatToCompile, compileAllLogger, order));
        }
    }
    
    private void submitNextCompileAllTask(final CompileAllSubTask task) {
        synchronized (compileTasks) {
            compileTasks.add(task);
            compileTasks.notifyAll();
        }
    }
    
    private void startCompileTaskProcessor() {
        synchronized (lock) {
            if (compileTaskProcessor == null) {
                compileTaskProcessor = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (;;) {
                            try {
                                synchronized (compileTasks) {
                                    if (compileTasks.isEmpty()) {
                                        try {
                                            compileTasks.wait();
                                        } catch (InterruptedException ex) {
                                        }
                                    } else {
                                        CompileOnSaveTask task = compileTasks.poll();
                                        try {
                                            buildMode = true;
                                            task.run();
                                        } finally {
                                            buildMode = false;
                                        }
                                    }
                                }
                            } catch (Throwable e) {
                                Logger.getLogger(UserExtensionManager.class.getName()).log(Level.SEVERE, e.getMessage(), e);
                            }
                        }
                    }
                });
                compileTaskProcessor.setDaemon(true);
                compileTaskProcessor.start();
            }
        }
    }

    public boolean isCompilingAllReportsStarted() {
        return compilingAllReportsStarted;
    }

    @Override
    public void compileOnSave(final AdsDefinition whatToCompile, boolean sync) {
        compileOnSave(new RadixObject[]{whatToCompile}, sync);
    }

    public void compileOnSave(final RadixObject[] whatToCompile, boolean sync) {
        if (buildMode) {
            return;
        }
        if (sync) {
            try {
                buildMode = true;
                new CompileOnSaveTask(whatToCompile).run();
            } catch (Throwable e) {
                Logger.getLogger(UserExtensionManager.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            } finally {
                buildMode = false;
            }
            return;
        }
        
        startCompileTaskProcessor();
        
        synchronized (compileTasks) {
            compileTasks.add(new CompileOnSaveTask(whatToCompile));
            compileTasks.notifyAll();

        }
    }

    public boolean canClose() {
        return canClose("There were errors on last build. Exit anyway?", false);
    }

    public boolean canClose(String faultMessage, boolean failOnErrors) {

        try {
            for (;;) {
                synchronized (compileTasks) {
                    if (compileTasks.isEmpty()) {
                        buildMode = true;
                        break;
                    } else {
                        try {
                            compileTasks.wait(100);
                        } catch (InterruptedException ex) {
                        }
                    }
                }
            }
            Set<RadixProblem> problems = RadixProblemRegistry.getDefault().getAllProblemSet();
            for (RadixProblem p : problems) {
                if (p.getSeverity() == RadixProblem.ESeverity.ERROR) {
                    if (failOnErrors) {
                        DialogUtils.messageError(faultMessage);
                        return false;
                    } else {
                        if (DialogUtils.messageConfirmation(faultMessage)) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
            return true;
        } finally {
            buildMode = false;
        }
    }
    /* private volatile boolean buildMode = false;

     @Override
     public void finishBuild() {
     buildMode = false;
     }

     @Override
     public void startBuild() {
     buildMode = true;
     }*/

    @Override
    public void initializeExtRepository(File projectDir, IClientEnvironment env, final UserExtLayerRepository layerRepository) throws IOException {
    }

    @Override
    public void uploadExtRepository(final UserExtLayerRepository layerRepository) {
        final ProgressHandle handle = ProgressHandleFactory.createHandle("Scanning Workspace...");
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handle.start();
                    final List<Module> modules = new LinkedList<>();

                    layerRepository.getBranch().visit(new IVisitor() {
                        @Override
                        public void accept(RadixObject radixObject) {
                            modules.add((Module) radixObject);
                        }
                    }, new VisitorProvider() {
                        @Override
                        public boolean isTarget(RadixObject radixObject) {
                            return radixObject instanceof Module && !(radixObject instanceof ReportsModule) && !(radixObject instanceof RolesModule) && !(radixObject instanceof MsdlSchemesModule);
                        }

                        @Override
                        public boolean isContainer(RadixObject radixObject) {
                            RadixObject check = radixObject;
                            while (check instanceof RadixObjects) {
                                check = check.getContainer();
                            }
                            if (check == null) {
                                return false;
                            }
                            return check instanceof Branch
                                    || radixObject instanceof Layer || radixObject instanceof Segment;
                        }
                    });
                    handle.switchToDeterminate(modules.size());
                    int progress = 0;
                    for (Module m : modules) {
                        m.visit(new IVisitor() {
                            @Override
                            public void accept(RadixObject radixObject) {
                            }
                        }, VisitorProviderFactory.createDefaultVisitorProvider());
                        progress++;
                        handle.progress(progress);
                    }
                } finally {
                    handle.finish();
                }
            }
        };
        //RequestProcessor.submit(runnable);
        ProgressUtils.showProgressDialogAndRun(runnable, handle, true);

    }

    @Override
    public void exportReports(final UserReports reports, final ReportsModule context, final File directory) {
        ProgressUtils.showProgressDialogAndRun(new Runnable() {
            @Override
            public void run() {
                exportReports(reports, context, directory);
            }
        }, "Export Reports...");
    }

    @Override
    public void exportReport(final ExportReportUtil.SaveReportTask saveReportTask) {
        ProgressUtils.showProgressDialogAndRun(saveReportTask, "Export Report...");
    }

    @Override
    public void processException(final IOException ex) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DialogUtils.messageError(ex);
            }
        });
    }

    @Override
    public boolean fileExistConfirm(File file) {
        return DialogUtils.messageConfirmation("File " + file.getAbsolutePath() + " is already exists. Overwite?");
    }

    public boolean makeCurrent(ProgressHandle progressHandle, boolean interactive, ReportVersion version, UserReport handle) {
        LifecycleManager.getDefault().saveAll();
        if (version.isCurrent()) {
            return true;
        }

        if (progressHandle != null) {
            progressHandle.progress("Compile version " + version.getName());
        }
        AdsUserReportClassDef reportClass = version.findReportDefinition();
        if (reportClass == null) {
            return false;
        }
        UserExtensionManager.getInstance().compileOnSave(reportClass, true);

        if (interactive && !UserExtensionManager.getInstance().canClose("Compilation errors detected. Continue?", false)) {
            return false;
        }

        if (progressHandle != null) {
            progressHandle.progress("Clean up report data for " + version.getName());
        }

        //first step - cleanup this version
        version.lockDefinitionSearch(true);
        try {
            version.cleanup();
            ReportVersion current = handle.getVersions().getCurrent();
            if (current == null) {
                return false;
            }
            //second step - cleanup current version
            if (progressHandle != null) {
                progressHandle.progress("Clean up report data for " + current.getName());
            }
            current.lockDefinitionSearch(true);
            try {
                current.cleanup();
                //third step - set this version as current
                if (progressHandle != null) {
                    progressHandle.progress("Registering new current version " + current.getName());
                }
                if (!getUFRequestExecutor().setCurrentVersionId(version, handle.getId())) {
                    return false;
                }
                if (progressHandle != null) {
                    progressHandle.progress("Applying changes... ");
                }
                handle.getVersions().setCurrent(version);
            } finally {
                current.lockDefinitionSearch(false);
            }
        } finally {
            version.lockDefinitionSearch(false);
        }

        reportClass = version.findReportDefinition();
        if (reportClass != null) {
            UserExtensionManager.getInstance().compileOnSave(reportClass, true);
        }
        return true;
    }

    @Override
    public String getLayerVersionsString() {
        return getEnvironment().getLayerVersionsString();
    }
}
