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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEntityCreationResult;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.schemas.adsdef.AdsUserReportDefinitionDocument;
import org.radixware.schemas.reports.UserReportListRq;
import org.radixware.schemas.reports.UserReportListRqDocument;
import org.radixware.schemas.reports.UserReportListRsDocument;
import org.radixware.schemas.reports.UserReportVersionListRqDocument;
import org.radixware.schemas.reports.UserReportVersionListRs;
import org.radixware.schemas.reports.UserReportVersionListRsDocument;
import org.radixware.kernel.common.client.env.UserDefinitionSettings;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.userreport.common.IUserReportRequestExecutor;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.repository.ReportsModule;
import org.radixware.kernel.common.userreport.repository.ReportsModuleRepository;
import static org.radixware.kernel.common.userreport.repository.ReportsModuleRepository.CURRENT_FORMAT_VERSION;
import static org.radixware.kernel.common.userreport.repository.ReportsModuleRepository.REPORT_MODULE_CLASS_ID;
import static org.radixware.kernel.common.userreport.repository.ReportsModuleRepository.REPORT_MODULE_DESC_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.ReportsModuleRepository.REPORT_MODULE_EDITOR_ID;
import static org.radixware.kernel.common.userreport.repository.ReportsModuleRepository.REPORT_MODULE_FORMAT_VERSION_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.ReportsModuleRepository.REPORT_MODULE_IMAGES_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.ReportsModuleRepository.REPORT_MODULE_TITLE_PROP_ID;
import org.radixware.kernel.common.userreport.repository.UserReport;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_CLASS_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_CURRENT_VERSION_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_EDIT_PRESENTATION_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_VERSION_CLASS_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_VERSION_GENERAL_PRESENTATION_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_VERSION_ORDER_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_VERSION_REPORT_ID_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_VERSION_SOURCE_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_VERSION_SOURCE_WITH_ACTUAL_CHANGELOG_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.REPORT_VERSION_VERSION_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.UserReport.getReportPid;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.reporteditor.tree.ChooseImportActionDialog;
import org.radixware.schemas.adsdef.AdsUserReportExchangeDocument;
import org.radixware.schemas.adsdef.Report;
import org.radixware.schemas.commondef.ChangeLogDocument;
import org.radixware.schemas.eas.CommandRs;
import org.radixware.schemas.types.StrDocument;

public class URRequestExecutor implements IUserReportRequestExecutor {

    @Override
    public List<ModuleInfo> listModules() {
        if (!UserExtensionManager.getInstance().needLoadReportModules()) {
            return Collections.emptyList();
        }
        final List<ModuleInfo> res = new ArrayList<>();
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(IClientEnvironment env) {
                GroupModel groupModel = GroupModel.openTableContextlessSelectorModel(env, ReportsModuleRepository.REPORT_MODULE_CLASS_ID, ReportsModuleRepository.REPORT_MODULE_SELECTOR_ID);
                GroupModelReader reader = new GroupModelReader(groupModel);

                for (EntityModel module : reader) {
                    Id moduleId = Id.Factory.loadFrom(((String) module.getProperty(ReportsModuleRepository.REPORT_MODULE_ID_PROP_ID).getValueObject()));
                    String moduleName = ((String) module.getProperty(ReportsModuleRepository.REPORT_MODULE_TITLE_PROP_ID).getValueObject());
                    String moduleDescription = ((String) module.getProperty(ReportsModuleRepository.REPORT_MODULE_DESC_PROP_ID).getValueObject());
                    res.add(new ModuleInfo(moduleId, moduleName, moduleDescription));
                            //try {
                    //registerModule(moduleId, createModuleRepository(moduleId, moduleName, moduleDescription), true);
                    //} catch (IOException e) {
                    //    env.processException(e);
                    //}
                }
            }
        });
        return res;
    }

    @Override
    public UserReportListRsDocument listReports(final Id moduleId) {
        final UserReportListRsDocument rs[] = new UserReportListRsDocument[1];
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(final IClientEnvironment env) {
                try {
                    //GroupModel group = GroupModel.openTableContextlessSelectorModel(env, REPORTS_CLASS_ID, REPORTS_SELECTOR_ID);
                    UserReportListRqDocument requestDoc = UserReportListRqDocument.Factory.newInstance();
                    UserReportListRq rq = requestDoc.addNewUserReportListRq();
                    rq.setModuleId(moduleId);
                    XmlObject output = UserExtensionManager.getInstance().getEnvironment().getEasSession().executeContextlessCommand(ReportsModuleRepository.LIST_REPORTS_COMMAND_ID, requestDoc, UserReportListRsDocument.class);
                    if (output == null) {
                        return;
                    }
                    rs[0] = XmlObjectProcessor.cast(getClass().getClassLoader(), output, UserReportListRsDocument.class);
                } catch (ServiceClientException | InterruptedException ex) {
                    UserExtensionManager.getInstance().getEnvironment().processException(ex);
                }
            }
        });
        return rs[0];
    }

    @Override
    public UserReportListRsDocument listReportsWithParam(final UserReport ownerReport, final Id paramId) {
        final UserReportListRsDocument rs[] = new UserReportListRsDocument[1];
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(final IClientEnvironment env) {
                try {
                    EntityModel model = UserReport.openReportVersionModel(UserExtensionManager.getInstance().getEnvironment(), ownerReport.getId(), ownerReport.getVersions().getCurrent().getVersion());
                    StrDocument requestDoc = StrDocument.Factory.newInstance();
                    requestDoc.setStr(paramId.toString());
                    XmlObject output = UserExtensionManager.getInstance().getEnvironment().getEasSession().executeCommand(model, null, ReportsModuleRepository.LIST_REPORTS_WITH_PARAM_COMMAND_ID, null, requestDoc);
                    if (output == null) {
                        return;
                    }
                    CommandRs response = (CommandRs) output;
                    rs[0] = UserReportListRsDocument.Factory.parse(response.getOutput().newInputStream());
                } catch (ServiceClientException | InterruptedException | XmlException | IOException ex) {
                    UserExtensionManager.getInstance().getEnvironment().processException(ex);
                }
            }
        });
        return rs[0];
    }

    @Override
    public ReportDataInfo loadReportData(final Id reportId, final long version) {
        final List<ReportDataInfo> reoprtInfo = new ArrayList<>();
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(final IClientEnvironment env) {
                try {
                    EntityModel model = UserReport.openReportVersionModel(UserExtensionManager.getInstance().getEnvironment(), reportId, version);
                    Object reportData = model.getProperty(REPORT_VERSION_SOURCE_WITH_ACTUAL_CHANGELOG_PROP_ID).getValueObject();
                    Id lastRuntimeId = Id.Factory.loadFrom((String) model.getProperty(ReportsModuleRepository.REPORT_VERSION_USER_CLASS_GUID_PROP_ID).getValueObject());
                    AdsUserReportDefinitionDocument xml = XmlObjectProcessor.cast(getClass().getClassLoader(), (XmlObject) reportData, AdsUserReportDefinitionDocument.class);
                    reoprtInfo.add(new ReportDataInfo(lastRuntimeId, xml));
                } catch (ObjectNotFoundError ex) {
                    //reoprtInfo.add(new ReportDataInfo(null,null)); 
                    //result.xml = null;//not found, so not loading
                } catch (ServiceClientException | InterruptedException ex) {
                    UserExtensionManager.getInstance().getEnvironment().processException(ex);
                }
            }
        });
        return reoprtInfo.isEmpty() ? new ReportDataInfo() : reoprtInfo.get(0);
    }

    @Override
    public byte[] listImages(final Id moduleId) {

        ModuleImagesAction imagesAction = new ModuleImagesAction(moduleId);
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(imagesAction);
        return imagesAction.getBytes();
    }

    @Override
    public boolean compile(AdsDefinition whatToCompile, boolean isForValidation, boolean sync) {
        return true;
    }

    @Override
    public boolean removeVersion(final UserReport.ReportVersion version, final Id id) {
        final boolean[] done = new boolean[]{false};
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(IClientEnvironment env) {
                try {
                    EntityModel model = UserReport.openReportVersionModel(env, id, version.getVersion());
                    if (model.delete(true)) {
                        done[0] = true;
                    }
                } catch (final ServiceClientException | InterruptedException ex) {
                    env.processException(ex);
                }
            }
        });

        return done[0];
    }

    class ModuleImagesAction extends RequestExecutor.ExplorerActionWithWaitImpl {

        private byte[] bytes;
        private final Id moduleId;

        ModuleImagesAction(Id moduleId) {
            this.moduleId = moduleId;
        }

        byte[] getBytes() {
            return bytes;
        }

        @Override
        public void execute(IClientEnvironment env) {
            try {
                Pid modulePid = new Pid(ReportsModuleRepository.REPORT_MODULE_CLASS_ID, moduleId.toString());
                EntityModel model = EntityModel.openContextlessModel(env, modulePid, ReportsModuleRepository.REPORT_MODULE_CLASS_ID, ReportsModuleRepository.REPORT_MODULE_EDITOR_ID);
                try {
                    Object fv = model.getProperty(ReportsModuleRepository.REPORT_MODULE_FORMAT_VERSION_PROP_ID).getValueObject();
                    long formatVersion = 0;
                    if (fv instanceof Long) {
                        formatVersion = ((Long) fv).longValue();
                    }

                    if (formatVersion > 0) {
                        Bin imgZip = (Bin) model.getProperty(ReportsModuleRepository.REPORT_MODULE_IMAGES_PROP_ID).getValueObject();
                        if (imgZip != null) {
                            bytes = imgZip.get();
                        }
                    } else {
                        String imgZipAsBase64 = (String) model.getProperty(ReportsModuleRepository.REPORT_MODULE_DESC_PROP_ID).getValueObject();
                        if (imgZipAsBase64 != null) {
                            bytes = Base64.decode(imgZipAsBase64);
                        }
                    }
                } catch (Exception e) {
                    //ignore
                }
            } catch (ObjectNotFoundError e) {
            } catch (ServiceClientException ex) {
                env.processException(ex);
            } catch (InterruptedException ex) {
                env.processException(ex);
            }
        }
    }

    @Override
    public void saveRuntime(final File runtimeFile, final Id reportId, final Id runtimeId, final long version, final AdsUserReportDefinitionDocument xDoc) {
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
            @Override
            public void execute(IClientEnvironment env) {
                try {
                    EntityModel versionModel = UserReport.openReportVersionModel(UserExtensionManager.getInstance().getEnvironment(), reportId, version);
                    versionModel.getProperty(UserReport.REPORT_VERSION_SOURCE_PROP_ID).setValueObject(xDoc);
                    Report xRep = xDoc.getAdsUserReportDefinition().getReport().getReport();
                    if (xRep.isSetChangeLog()) {
                        ChangeLogDocument xLog = ChangeLogDocument.Factory.newInstance();
                        xLog.addNewChangeLog().set(xRep.getChangeLog());
                        versionModel.getProperty(UserReport.REPORT_VERSION_CHANGE_LOG_PROP_ID).setValueObject(xLog);
                    }

                    if (runtimeFile != null) {
                        Bin runtimeData = new Bin(FileUtils.readBinaryFile(runtimeFile));
                        //reoprtInfo.add(new ReportDataInfo(runtimeId,runtimeData));
                        versionModel.getProperty(UserReport.REPORT_VERSION_RUNTIME_PROP_ID).setValueObject(runtimeData);
                        versionModel.getProperty(UserReport.REPORT_VERSION_REPORT_CLASS_GUID_PROP_ID).setValueObject(runtimeId.toString());
                    }
                    versionModel.update();
                } catch (final ModelException | ServiceClientException | InterruptedException | IOException ex) {
                    UserExtensionManager.getInstance().getEnvironment().processException(ex);

                }
            }
        });
    }

    @Override
    public boolean loadReportVersions(final Id reportId, final UserReportVersionListRs xReportVersionsRs) {
        final boolean[] result = new boolean[]{false};
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(final IClientEnvironment env) {
                try {
                    UserReportVersionListRqDocument requestDoc = UserReportVersionListRqDocument.Factory.newInstance();
                    requestDoc.addNewUserReportVersionListRq().setReportId(reportId.toString());
                    XmlObject output = UserExtensionManager.getInstance().getEnvironment().getEasSession().executeContextlessCommand(UserReport.REPORT_LIST_VERSIONS_COMMAND_ID, requestDoc, UserReportVersionListRsDocument.class);
                    // System.out.println(output);
                    if (output == null) {
                        return;
                    }
                    UserReportVersionListRsDocument rs = XmlObjectProcessor.cast(getClass().getClassLoader(), output, UserReportVersionListRsDocument.class);

                    if (rs == null) {
                        return;
                    }
                    //    System.out.println(rs.toString());
                    xReportVersionsRs.assignVersionList(rs.getUserReportVersionListRs().getVersionList());
                    //List<UserReportVersionListRs.Version> xVersions=xReportVersionsRs.getVersionList();
                    // for (UserReportVersionListRs.Version v : rs.getUserReportVersionListRs().getVersionList()) {
                    //    xVersions.add(v);

                    //}
                    result[0] = true;
                } catch (ServiceClientException | InterruptedException ex) {
                    final Exception e = new Exception("Unable to read report versions list", ex);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            env.processException(e);
                        }
                    });
                }
            }
        });

        return result[0];
    }

    @Override
    public List<EIsoLanguage> getLanguages() {
        final List<EIsoLanguage> languages = new ArrayList<>();
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(IClientEnvironment env) {
                UserDefinitionSettings defSettings = new UserDefinitionSettings(env);
                languages.addAll(defSettings.getSupportedLanguages());
            }
        });

        return languages;
    }

    @Override
    public void processException(final Throwable ex) {
        Exceptions.printStackTrace(ex);
    }

    @Override
    public Id importExistReport(final UserReport existingReport, final AdsUserReportExchangeDocument xDoc) throws IOException {
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
    }

    @Override
    public UserReport createReport(final ReportsModule module, final String name, final Id id, final AdsReportClassDef initialReport) {
        final UserReport result[] = new UserReport[1];
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(IClientEnvironment env) {
                try {
                    EntityModel prepareCreateModel = EntityModel.openPrepareCreateModel(null, UserReport.REPORT_CREATE_PRESENTATION_ID, UserReport.REPORT_CLASS_ID, null, new IContext.ContextlessCreating(env));
                    prepareCreateModel.getProperty(UserReport.REPORT_ID_PROP_ID).setValueObject(id.toString());
                    prepareCreateModel.getProperty(UserReport.REPORT_MODULE_ID_PROP_ID).setValueObject(module.getId().toString());
                    prepareCreateModel.getProperty(UserReport.REPORT_NAME_PROP_ID).setValueObject(name);
                    if (prepareCreateModel.create() != EEntityCreationResult.SUCCESS) {
                        return;
                    }
                    prepareCreateModel.read();
                    long currentVersionId = (Long) prepareCreateModel.getProperty(UserReport.REPORT_CURRENT_VERSION_PROP_ID).getValueObject();
                    long currentVersionOrder = (Long) prepareCreateModel.getProperty(UserReport.REPORT_CURRENT_ORDER_PROP_ID).getValueObject();
                    AdsTypeDeclaration contextParameterType = null;
                    if (initialReport instanceof AdsUserReportClassDef) {
                        contextParameterType = ((AdsUserReportClassDef) initialReport).getContextParameterType();
                    }

                    result[0] = UserExtensionManagerCommon.getInstance().getUserReports().registerReport(module.getId(), id, name, "", currentVersionId, currentVersionOrder, initialReport, contextParameterType, UserReport.FORMAT_VERSION);
                } catch (final ModelException | ServiceClientException | InterruptedException | IOException ex) {
                    env.processException(ex);
                }
            }
        });

        return result[0];
    }

    @Override
    public UserReport.ReportVersion createVersionImpl(final AdsUserReportDefinitionDocument xDoc, final Id id, final UserReport userReport) {
        final UserReport.ReportVersion[] version = new UserReport.ReportVersion[1];
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(IClientEnvironment env) {
                try {
                    Map<Id, Object> propInitVals = new HashMap<>();
                    propInitVals.put(REPORT_VERSION_REPORT_ID_PROP_ID, id.toString());
                    propInitVals.put(REPORT_VERSION_SOURCE_PROP_ID, xDoc);
                    EntityModel prepareCreateModel = EntityModel.openPrepareCreateModel(null, REPORT_VERSION_GENERAL_PRESENTATION_ID, REPORT_VERSION_CLASS_ID, null, propInitVals, new IContext.ContextlessCreating(env));
                    if (prepareCreateModel.create() == EEntityCreationResult.SUCCESS) {
                        prepareCreateModel = EntityModel.openContextlessModel(prepareCreateModel.getEnvironment(), prepareCreateModel.getPid(), prepareCreateModel.getClassId(), prepareCreateModel.getEditorPresentationDef().getId());
                        prepareCreateModel.read();
                        long versionId = ((Long) prepareCreateModel.getProperty(REPORT_VERSION_VERSION_PROP_ID).getValueObject()).longValue();
                        long order = ((Long) prepareCreateModel.getProperty(REPORT_VERSION_ORDER_PROP_ID).getValueObject()).longValue();
                        version[0] = new UserReport.ReportVersion(userReport, versionId, order);
                    }
                } catch (final ModelException | ServiceClientException | InterruptedException ex) {
                    env.processException(ex);
                }
            }
        });

        return version[0];
    }

    @Override
    public void updateModuleRepository(final ReportsModule module, final String content, final Bin images) {
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
            @Override
            public void execute(IClientEnvironment env) {
                try {

                    Pid modulePid = new Pid(REPORT_MODULE_CLASS_ID, module.getId().toString());
                    EntityModel model = EntityModel.openContextlessModel(UserExtensionManagerCommon.getInstance().getEnvironment(), modulePid, REPORT_MODULE_CLASS_ID, REPORT_MODULE_EDITOR_ID);
                    model.getProperty(REPORT_MODULE_TITLE_PROP_ID).setValueObject(module.getName());
                    if (content != null) {
                        model.getProperty(REPORT_MODULE_DESC_PROP_ID).setValueObject(content);
                    }
                    if (images != null) {
                        model.getProperty(REPORT_MODULE_IMAGES_PROP_ID).setValueObject(images);
                    }
                    model.getProperty(REPORT_MODULE_FORMAT_VERSION_PROP_ID).setValueObject(CURRENT_FORMAT_VERSION);
                    model.update();
                } catch (ObjectNotFoundError e) {
                    return;
                } catch (ModelException | ServiceClientException | InterruptedException ex) {
                    UserExtensionManagerCommon.getInstance().getEnvironment().processException(ex);
                }
            }
        });
    }

    @Override
    public boolean setCurrentVersionId(final UserReport.ReportVersion version, final Id id) {
        final boolean[] result = new boolean[]{false};
        final Throwable[] exs = new Throwable[]{null};//NOPMD
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
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
                }
            }
        });

        if (exs[0] != null) {
            throw new RadixError("Unable to change current version ID", exs[0]);
        }
        return result[0];
    }

    @Override
    public void executeTask(final Runnable task) {
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(IClientEnvironment env) {
                task.run();
            }
        });
    }
    
    @Override
    public boolean messageConfirmation(final String message) {
        return DialogUtils.messageConfirmation(message);
    }
}
