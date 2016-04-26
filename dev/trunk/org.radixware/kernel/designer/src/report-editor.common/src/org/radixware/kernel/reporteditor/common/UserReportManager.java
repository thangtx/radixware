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

import java.io.IOException;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEntityCreationResult;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.repository.fs.IRepositorySegment;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.IUserReportManager;
import org.radixware.kernel.common.userreport.common.IUserDefChangeSuppert;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.repository.ReportsModule;
import org.radixware.kernel.common.userreport.repository.ReportsModuleRepository;
import static org.radixware.kernel.common.userreport.repository.ReportsModuleRepository.REPORT_MODULE_CLASS_ID;
import static org.radixware.kernel.common.userreport.repository.ReportsModuleRepository.REPORT_MODULE_EDITOR_ID;
import static org.radixware.kernel.common.userreport.repository.ReportsModuleRepository.REPORT_MODULE_ID_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.ReportsModuleRepository.REPORT_MODULE_TITLE_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.ReportsModuleRepository.REPORT_CLASS_REPORT_MODULE_PROP_ID;
import org.radixware.kernel.common.userreport.repository.UserReport;
import static org.radixware.kernel.common.userreport.repository.UserReport.FORMAT_VERSION;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_CLASS_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_CONTEXT_PARAM_TYPE_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_DESCRIPTION_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_EDIT_PRESENTATION_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_FORMAT_VERSION_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_NAME_PROP_ID;
import org.radixware.kernel.common.userreport.repository.UserReport.ReportVersion;
import org.radixware.kernel.common.userreport.repository.UserReport.ReportVersions;
import static org.radixware.kernel.common.userreport.repository.UserReport.getReportPid;
import org.radixware.kernel.designer.ads.editors.creation.AdsClassCreature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreationWizard;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.NamedRadixObjectCreature;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.reporteditor.editors.UserReportEditor;
import org.radixware.schemas.xscml.TypeDeclarationDocument;


public class UserReportManager implements IUserReportManager{
    private ChangeSupport versionsChangeSupport = new ChangeSupport(this);
    private ChangeSupport versionChangeSupport = new ChangeSupport(this);
    private final Map<Id, WeakReference<UserReportEditor>> reportEditors = Collections.synchronizedMap(new HashMap<Id, WeakReference<UserReportEditor>>());
    private final AtomicLong openReportEditorAttemptsCounter = new AtomicLong(0);
    private final static long REPORTS_MAP_CLEAN_UP_INTERVAL = 25;
    
    public void removeVersionChangeListener(ChangeListener listener) {
        versionChangeSupport.removeChangeListener(listener);
    }

    public void addVersionChangeListener(ChangeListener listener) {
        versionChangeSupport.addChangeListener(listener);
    }

    public void removeVersionsChangeListener(ChangeListener listener) {
        versionsChangeSupport.removeChangeListener(listener);
    }

    public void fireVersionsChange() {
        versionsChangeSupport.fireChange();
    }

    public void addVersionsChangeListener(ChangeListener listener) {
        versionsChangeSupport.addChangeListener(listener);
    }
    
    UserReportManager(){         
    }
    
    @Override
    public UserReport addNewReport(ReportsModule reportModule){
        final AdsClassCreature creature = AdsClassCreature.Factory.newInstance(reportModule, EClassType.REPORT, true);

        ICreatureGroup.ICreature<?> result = CreationWizard.execute(new ICreatureGroup[]{new ICreatureGroup() {
                @Override
                public List<ICreatureGroup.ICreature> getCreatures() {
                    return Collections.<ICreatureGroup.ICreature>singletonList(creature);
                }

                @Override
                public String getDisplayName() {
                    return "User Reports";
                }
            }}, creature);
        if (result != null) {

            final RadixObject radixObject = result.commit();
            if (radixObject instanceof AdsReportClassDef) {
                //try create user report in database using new report guid;
                AdsReportClassDef report = (AdsReportClassDef) radixObject;

                UserReport userReport = UserReport.create(reportModule, report);
                if (userReport != null) {
                     UserExtensionManager.getInstance().getUserReports().firePropertyChange("report-list");
                }
                return userReport;
            } else {
                if (radixObject != null) {
                    radixObject.delete();
                }
            }
        }
        return null;
    }

    //@Override
   // public void addNewVersion(ReportVersion src) {
   //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   // }

   
    private static final class UserReportModuleCreature extends NamedRadixObjectCreature<AdsModule> {

        public UserReportModuleCreature() {
            super(UserExtensionManager.getInstance().getReportsSegment().getModules(), "Create new Module");
        }

        @Override
        public AdsModule createInstance(String name) {
            return new ReportsModule(name);
        }

        @Override
        public RadixIcon getIcon() {
            return AdsDefinitionIcon.MODULE;
        }
    }
    
    
    @Override
    public ReportsModule addNewModule() {
        final UserReportModuleCreature creature = new UserReportModuleCreature();
        ICreatureGroup.ICreature<?> result = CreationWizard.execute(new ICreatureGroup[]{new ICreatureGroup() {
                @Override
                public List<ICreatureGroup.ICreature> getCreatures() {
                    return Collections.<ICreatureGroup.ICreature>singletonList(creature);
                }

                @Override
                public String getDisplayName() {
                    return "User Report Modules";
                }
            }}, creature);
        if (result != null) {

            final RadixObject radixObject = result.commit();
            if (radixObject instanceof ReportsModule) {
                //try create user report in database using new report guid;
                ReportsModule module = (ReportsModule) radixObject;
                if (createReportModule(module.getSegment().getRepository(), module)) {
                    UserExtensionManager.getInstance().getUserReports().firePropertyChange("report-module-list");
                    return module;
                } else {
                    radixObject.delete();
                    return null;
                }
            } else {
                radixObject.delete();
                return null;
            }
        } else {
            return null;
        }
    } 
    
    
    @Override
    @SuppressWarnings({"unchecked","rawtypes"})
     public boolean createReportModule(final IRepositorySegment segment, final ReportsModule module){
        final ReportsModuleRepository[] result = new ReportsModuleRepository[1];
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(final IClientEnvironment env) {

                try {
                    final Map<Id, Object> initVals = new HashMap<>();
                    initVals.put(REPORT_MODULE_ID_PROP_ID, module.getId().toString());
                    initVals.put(REPORT_MODULE_TITLE_PROP_ID, module.getName());
                    EntityModel model = EntityModel.openPrepareCreateModel(REPORT_MODULE_CLASS_ID, REPORT_MODULE_EDITOR_ID, REPORT_MODULE_CLASS_ID, null, initVals, new IContext.ContextlessCreating(env));
                    if (model.create() == EEntityCreationResult.SUCCESS) {
                        result[0] = (ReportsModuleRepository) segment.getModuleRepository(module);
                    }
                } catch (ModelException | ServiceClientException | InterruptedException ex) {

                    env.processException(ex);

                } catch (final Throwable e) {
                    //SwingUtilities.invokeLater(new Runnable() {
                    //    @Override
                    //    public void run() {
                            //UserExtensionManagerCommon.getInstance().getEnvironment().processException(e);
                            //Exceptions.printStackTrace(e);
                     //   }
                    //});

                    System.out.println(e.getMessage());
                }
            }
        });
        //return result[0];
        //ReportsModuleRepository repository = ReportsModuleRepository.createNew((UserExtADSSegmentRepository) getSegment().getRepository(), this);

        return result[0] != null;
    }
    
   /* @Override
    public Id importExistReport(final UserReport existingReport,final  AdsUserReportExchangeDocument xDoc) throws IOException{
        //if (existingReport != null) {
        ChooseImportActionDialog dialog = new ChooseImportActionDialog(existingReport.getName());
        ModalDisplayer.showModal(dialog);
        switch (dialog.getOption()) {
            case ChooseImportActionDialog.OPT_CANCEL:
                return null;
            case ChooseImportActionDialog.OPT_ADD_VERSIONS:
                existingReport.importNewVersion(xDoc);
                return null;
            default:
                return Id.Factory.newInstance(EDefinitionIdPrefix.USER_DEFINED_REPORT);
        }
        //}
    }*/
    
   /* @Override
    public void importReport(ReportsModule context, InputStream in) throws IOException {
        try {
            AdsUserReportExchangeDocument xDoc = AdsUserReportExchangeDocument.Factory.parse(in);
            AdsUserReportExchangeDocument.AdsUserReportExchange xEx = xDoc.getAdsUserReportExchange();
            if (xEx.getAdsUserReportDefinitionList().isEmpty()) {
                throw new IOException("No report version information found");
            }

            boolean hasData = false;
            for (int i = 0; i < xEx.getAdsUserReportDefinitionList().size(); i++) {
                AdsUserReportDefinitionDocument.AdsUserReportDefinition xDef = xEx.getAdsUserReportDefinitionList().get(i);
                if (xDef.getReport() != null) {
                    hasData = true;
                    break;
                }
            }
            if (!hasData) {
                return;
            }

            //check existing report
            Id currentReportId = Id.Factory.changePrefix(xEx.getId(), EDefinitionIdPrefix.USER_DEFINED_REPORT);
            //upload reports;
            
            UserReport existingReport = null;
            for (ReportsModule module : UserExtensionManagerCommon.getInstance().getUserReports().getReportModules()) {
                if (module.getDefinitions().containsId(currentReportId)) {
                    existingReport = UserExtensionManagerCommon.getInstance().getUserReports().findReportById(currentReportId);
                    break;
                }
            }

            if (existingReport != null) {
                ChooseImportActionDialog dialog = new ChooseImportActionDialog(existingReport.getName());
                ModalDisplayer.showModal(dialog);
                switch (dialog.getOption()) {
                    case ChooseImportActionDialog.OPT_CANCEL:
                        return;
                    case ChooseImportActionDialog.OPT_ADD_VERSIONS:
                        existingReport.importNewVersion(xDoc);
                        return;
                    default:
                        currentReportId = Id.Factory.newInstance(EDefinitionIdPrefix.USER_DEFINED_REPORT);
                }

            }

            Id newReportId = currentReportId;

            UserReport report = UserReport.create(context, existingReport == null ? xEx.getName() : xEx.getName() + " - Copy", newReportId, null);

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

            if (report != null) {
                UserReport.ReportVersion current = report.getVersions().getCurrent();
                UserReport.ReportVersion newCurrent = null;
                report.setDescription(xEx.getDescription());

                for (int i = 0; i < xEx.getAdsUserReportDefinitionList().size(); i++) {
                    AdsUserReportDefinitionDocument.AdsUserReportDefinition xDef = xEx.getAdsUserReportDefinitionList().get(i);
                    if (xDef.getReport() != null) {
                        UserReport.ReportVersion newVersion = report.getVersions().addNewVersion(xEx.getAdsUserReportDefinitionList().get(i));
                        if (newCurrent == null) {
                            newCurrent = newVersion;
                        }
                    }
                }

                if (newCurrent != null) {
                    AdsReportClassDef reportDef = newCurrent.findReportDefinition();
                    AdsPropertyDef cp = reportDef.findContextParameter();
                    if (cp != null) {
                        report.setContextParamType(cp.getValue().getType());
                    }
                }
                UserExtensionManagerCommon.getInstance().startBuild();
                try {
                    report.save();
                    for (UserReport.ReportVersion v : report.getVersions().list()) {
                        v.save();

                    }
                } finally {
                    UserExtensionManagerCommon.getInstance().finishBuild();
                }
                List<UserReport.ReportVersion> locked = new LinkedList<>();
                if (newCurrent != null) {
                    makeCurrent(null,false,newCurrent,report);
                }
                try {

                    for (UserReport.ReportVersion v : report.getVersions().list()) {
                        v.lockDefinitionSearch(true);
                        locked.add(v);
                    }

                    if (current != null) {
                        report.getVersions().removeVersion(current);
                    }

                    for (UserReport.ReportVersion v : locked) {
                        v.reload();
                    }
                } finally {
                    for (UserReport.ReportVersion v : locked) {
                        v.lockDefinitionSearch(false);
                    }
                    for (UserReport.ReportVersion v : report.getVersions().list()) {
                        v.save();
                    }
                }

                context.invalidateDependences();
            }

        } catch (XmlException | IOException ex) {
            throw new IOException("Error on reading user report exchange data", ex);
        } finally {
            UserExtensionManager.getInstance().getUserReports().firePropertyChange("report-list");
        }

    }*/
    
    
    
    
    @Override
    public void deleteModule(final Id moduleId) {
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
            @Override
            public void execute(IClientEnvironment env) {
                Pid modulePid = new Pid(REPORT_MODULE_CLASS_ID, moduleId.toString());
                EntityModel model;
                try {
                    model = EntityModel.openContextlessModel(env, modulePid, REPORT_MODULE_CLASS_ID, REPORT_MODULE_EDITOR_ID);
                    model.delete(true);
                } catch (ServiceClientException | InterruptedException ex) {
                    env.processException(ex);
                }

            }
        });
    } 
     
     
   

    /*@Override
        public UserReport.ReportVersion createVersionImpl(final AdsUserReportDefinitionDocument xDoc,final Id id,final UserReport userReport) {
            final UserReport.ReportVersion[] version = new UserReport.ReportVersion[1];
            final CountDownLatch lock = new CountDownLatch(1);
            UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
                @Override
                public void execute(IClientEnvironment env) {
                    try {
                        EntityModel prepareCreateModel = EntityModel.openPrepareCreateModel(null, REPORT_VERSION_GENERAL_PRESENTATION_ID, REPORT_VERSION_CLASS_ID, null, new IContext.ContextlessCreating(env));
                        prepareCreateModel.getProperty(REPORT_VERSION_REPORT_ID_PROP_ID).setValueObject(id.toString());
                        prepareCreateModel.getProperty(REPORT_VERSION_SOURCE_PROP_ID).setValueObject(xDoc);
                        if (prepareCreateModel.create() == EEntityCreationResult.SUCCESS) {
                            prepareCreateModel.read();
                            long versionId = ((Long) prepareCreateModel.getProperty(REPORT_VERSION_VERSION_PROP_ID).getValueObject()).longValue();
                            long order = ((Long) prepareCreateModel.getProperty(REPORT_VERSION_ORDER_PROP_ID).getValueObject()).longValue();
                            version[0] = new UserReport.ReportVersion(userReport, versionId, order);
                        }
                    } catch (final ModelException | ServiceClientException | InterruptedException ex) {
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
            return version[0];
        }*/
        
       /*  public boolean makeCurrent(ProgressHandle progressHandle, boolean interactive,ReportVersion version,UserReport handle) {
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
            UserExtensionManager.getInstance().compileOnSave(reportClass);

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
                UserExtensionManager.getInstance().compileOnSave(reportClass);
            }
            return true;
        }*/
         
        /* private boolean setCurrentVersionId(final ReportVersion version, final Id id) {
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

        
    @Override
        public void save(final AdsTypeDeclaration contextParameterType,final UserReport report) {
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
            @Override
            public void execute(IClientEnvironment env) {
                try {
                    EntityModel model = EntityModel.openContextlessModel(UserExtensionManagerCommon.getInstance().getEnvironment(), UserReport.getReportPid(report.getId()), REPORT_CLASS_ID, REPORT_EDIT_PRESENTATION_ID);
                    model.getProperty(REPORT_NAME_PROP_ID).setValueObject(report.getName());
                    model.getProperty(REPORT_DESCRIPTION_PROP_ID).setValueObject(report.getDescription());
                    model.getProperty(REPORT_FORMAT_VERSION_PROP_ID).setValueObject(FORMAT_VERSION);
                    TypeDeclarationDocument xDoc = null;
                    if (contextParameterType != null) {
                        xDoc = TypeDeclarationDocument.Factory.newInstance();
                        contextParameterType.appendTo(xDoc.addNewTypeDeclaration());
                    }
                    model.getProperty(REPORT_CONTEXT_PARAM_TYPE_PROP_ID).setValueObject(xDoc);
                    model.update();
                    report.setModified(false);
                    report.setCurrentFormatVersion(FORMAT_VERSION);

                } catch (final ServiceClientException | InterruptedException | ModelException ex) {
                    UserExtensionManagerCommon.getInstance().getEnvironment().processException(ex);
                }

            }
        });
    }
        
    @Override
    public boolean moveTo(final AdsModule module,final Id reportId) throws IOException{
        final boolean[] done = new boolean[]{false};
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(IClientEnvironment env) {
                try {
                    EntityModel model = EntityModel.openContextlessModel(env, UserReport.getReportPid(reportId), REPORT_CLASS_ID, REPORT_EDIT_PRESENTATION_ID);
                    model.getProperty(REPORT_CLASS_REPORT_MODULE_PROP_ID).setValueObject(module.getId().toString());
                    model.update();
                    done[0] = true;
                } catch (final ServiceClientException | InterruptedException | ModelException ex) {
                    env.processException(ex);
                }

            }
        });
        return done[0];
    }
    
    @Override
     public boolean deleteReport(List<String> deletedPubs,final Throwable[] exs, final Id reportId) {
        final boolean[] done = new boolean[]{false};
        //final Throwable[] exs = new Throwable[1];
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(IClientEnvironment env) {
                try {
                    EntityModel model = EntityModel.openContextlessModel(env, getReportPid(reportId), REPORT_CLASS_ID, REPORT_EDIT_PRESENTATION_ID);
                    boolean deletionResult = model.delete(true);
                    done[0] = deletionResult;
                } catch (final ServiceClientException | InterruptedException ex) {
                    env.processException(ex);
                    exs[0] = ex;
                }
            }
        });
        return done[0];
    }
     
    
    @Override
     public void openEditor(final UserReport report) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WeakReference<UserReportEditor> editorRef = reportEditors.get(report.getId());
                UserReportEditor editor = null;
                if (editorRef != null) {
                    editor = editorRef.get();
                }

                if (editor == null) {
                    editor = new UserReportEditor(report);
                    reportEditors.put(report.getId(), new WeakReference<>(editor));
                }

                editor.open();
                editor.requestActive();
            }
        });
        
        if (openReportEditorAttemptsCounter.incrementAndGet() % REPORTS_MAP_CLEAN_UP_INTERVAL == 0) {
            synchronized (reportEditors) {
                Iterator<Map.Entry<Id, WeakReference<UserReportEditor>>> iter = reportEditors.entrySet().iterator();
                while (iter.hasNext()) {
                    if (iter.next().getValue().get() == null) {
                        iter.remove();
                    }
                }
            }
        }
    }
     
    @Override
     public IUserDefChangeSuppert createVersionChangeSuppert(ReportVersion reportVersion){
         return new VersionChangeSuppert(reportVersion);
     }
     
    @Override
     public IUserDefChangeSuppert createVersionsChangeSuppert(ReportVersions reportVersions){
         return new VersionChangeSuppert(reportVersions);
     }

}
