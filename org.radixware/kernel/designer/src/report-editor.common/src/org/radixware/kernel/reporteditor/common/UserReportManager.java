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

public class UserReportManager implements IUserReportManager {

    private ChangeSupport versionsChangeSupport = new ChangeSupport(this);
    private ChangeSupport versionChangeSupport = new ChangeSupport(this);
    private final Map<Id, WeakReference<UserReportEditor>> reportEditors = Collections.synchronizedMap(new HashMap<Id, WeakReference<UserReportEditor>>());
    private final AtomicLong openReportEditorAttemptsCounter = new AtomicLong(0);
    private final static long REPORTS_MAP_CLEAN_UP_INTERVAL = 25;

    UserReportManager() {
    }

    @Override
    public UserReport addNewReport(ReportsModule reportModule) {
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
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean createReportModule(final IRepositorySegment segment, final ReportsModule module) {
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

    @Override
    public void save(final AdsTypeDeclaration contextParameterType, final UserReport report) {
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
                    report.refreshModified();
                }
            }
        });
    }

    @Override
    public boolean moveTo(final AdsModule module, final Id reportId) throws IOException {
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
    public boolean deleteReport(List<String> deletedPubs, final Throwable[] exs, final Id reportId) {
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
    public void closeEditor(UserReport report) {
        reportEditors.remove(report.getId());
    }

    @Override
    public IUserDefChangeSuppert createVersionChangeSuppert(ReportVersion reportVersion) {
        return new VersionChangeSuppert(reportVersion);
    }

    @Override
    public IUserDefChangeSuppert createVersionsChangeSuppert(ReportVersions reportVersions) {
        return new VersionChangeSuppert(reportVersions);
    }

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
}
