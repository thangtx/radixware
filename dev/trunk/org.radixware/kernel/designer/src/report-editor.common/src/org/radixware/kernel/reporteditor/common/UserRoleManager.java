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
import java.util.Collections;
import java.util.List;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEntityCreationResult;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.utils.changelog.ChangeLog;
import org.radixware.kernel.common.userreport.common.IUserRoleManager;
import org.radixware.kernel.common.userreport.common.IUserDefChangeSuppert;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.repository.role.AppRole;
import static org.radixware.kernel.common.userreport.repository.role.AppRole.CLASS_GUID_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.role.AppRole.DEF_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.role.AppRole.RUNTIME_PROP_ID;
import org.radixware.kernel.common.userreport.repository.role.AppRoles;
import org.radixware.kernel.common.userreport.repository.role.RolesModule;
import org.radixware.kernel.common.userreport.repository.role.RolesModuleRepository;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.ads.editors.creation.AdsNamedDefinitionCreature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreationWizard;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionListDocument;
import org.radixware.schemas.adsdef.RoleDefinition;
import org.radixware.schemas.commondef.ChangeLogDocument;


public class UserRoleManager  implements IUserRoleManager{

    /*private ChangeSupport rolesChangeSupport = new ChangeSupport(this);
    
    public void removeRolesChangeListener(ChangeListener listener) {
        rolesChangeSupport.removeChangeListener(listener);
    }

    public boolean hasRolesListeners() {
        return rolesChangeSupport.hasListeners();
    }

    public void addRolesChangeListener(ChangeListener listener) {
        rolesChangeSupport.addChangeListener(listener);
    }
    
    public void fireRolesChange() {
        rolesChangeSupport.fireChange();
    }*/
    
    
    
    public UserRoleManager(){        
    }
    
    @Override
    public void saveImpl(final File runtimeFile,final AdsRoleDef role) {
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {

            @Override
            public void execute(final IClientEnvironment env) {
                try {
                    EntityModel model = AppRole.openAppRoleModel(env, role.getId());
                    model.getProperty(AppRole.NAME_PROP_ID).setValueObject(role.getName());
                    model.getProperty(AppRole.DESCR_PROP_ID).setValueObject(role.getDescription());
                    
                    ChangeLogDocument xLog = ChangeLogDocument.Factory.newInstance();
                    final AppRole roleWrapper = UserExtensionManager.getInstance().getAppRoles().findAppRole(role.getId());
                    boolean changeLogDefined = false;
                    if (roleWrapper != null) {
                        changeLogDefined = !roleWrapper.getChangeLog().isEmpty();
                        if (roleWrapper.isChangeLogModified()) {
                            roleWrapper.getChangeLog().appendTo(xLog.addNewChangeLog(), true);
                            model.getProperty(AppRole.CHANGE_LOG_PROP_ID).setValueObject(xLog);
                        }
                    }
                    
                    if (role.isEmptyRole() && !changeLogDefined) {
                        model.getProperty(DEF_PROP_ID).setValueObject(null);
                        model.getProperty(RUNTIME_PROP_ID).setValueObject(null);
                        model.getProperty(CLASS_GUID_PROP_ID).setValueObject(null);
                    } else {
                        AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
                        role.appendTo(xDoc.addNewAdsDefinition(), AdsDefinition.ESaveMode.NORMAL);
                        model.getProperty(DEF_PROP_ID).setValueObject(xDoc);
                        if (runtimeFile != null) {
                            Bin runtimeData = new Bin(FileUtils.readBinaryFile(runtimeFile));
                            model.getProperty(RUNTIME_PROP_ID).setValueObject(runtimeData);
                            model.getProperty(CLASS_GUID_PROP_ID).setValueObject(role.getRuntimeId().toString());
                        }
                    }
                    model.update();
                    if (roleWrapper != null) {
                        roleWrapper.afterSave();
                    }
                } catch (final ModelException | IOException | ServiceClientException | InterruptedException ex) {
                    env.processException(ex);
                }
            }
        });
    }
    
    @Override
    public AppRole create() {
        //step one - launch role creature 
        //step two. create new role model

        RolesModule m = (RolesModule) UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().findById(RolesModule.MODULE_ID);
        if (m == null) {
            return null;
        }

        final String[] name = new String[]{null};
        final AdsNamedDefinitionCreature<AdsRoleDef> creature = new AdsNamedDefinitionCreature<AdsRoleDef>(m.getDefinitions(), "NewRole", "New Application Role") {
            @Override
            public AdsRoleDef createInstance() {
                return AdsRoleDef.Factory.newInstance();
            }

            @Override
            public RadixIcon getIcon() {
                return AdsDefinitionIcon.ROLE;
            }

            @Override
            public boolean afterCreate(AdsRoleDef object) {
                name[0] = getName();
                return false;
            }
        };

        ICreatureGroup.ICreature<?> result = CreationWizard.execute(new ICreatureGroup[]{new ICreatureGroup() {
                @Override
                public List<ICreatureGroup.ICreature> getCreatures() {
                    return Collections.<ICreatureGroup.ICreature>singletonList(creature);
                }

                @Override
                public String getDisplayName() {
                    return "Application Roles";
                }
            }}, creature);
        if (result != null) {

            result.commit();
            if (name[0] != null) {
                //try create user report in database using new report guid;

                final Id[] guid = new Id[]{null};
                final boolean[] readOnly = new boolean[]{false};
                UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
                    @Override
                    public void execute(IClientEnvironment env) {

                        try {
                            EntityModel model = EntityModel.openPrepareCreateModel(AppRole.CLASS_ID, AppRole.EDITOR_ID, AppRole.CLASS_ID, null, new IContext.ContextlessCreating(env));
                            model.getProperty(AppRole.NAME_PROP_ID).setValueObject(name[0]);
                            if (model.create() == EEntityCreationResult.SUCCESS) {
                                model.read();
                                guid[0] = Id.Factory.loadFrom(model.getProperty(AppRole.GUID_PROP_ID).getValueAsString());
                                readOnly[0] = ((Boolean) model.getProperty(AppRole.READ_ONLY_PROP_ID).getValueObject()).booleanValue();
                            }

                        } catch (final ModelException | ServiceClientException | InterruptedException ex) {
                            env.processException(ex);
                        }
                    }
                });

                if (guid[0] != null) {
                    AppRole role = new AppRole(guid[0], name[0], null, null, readOnly[0]);
                    return role;
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean delete(final AppRole role) {
        //try {
        //    lockDefinitionSearch(true);
            final boolean[] result = new boolean[]{false};
            UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
                @Override
                public void execute(IClientEnvironment env) {
                    try {
                        EntityModel model = AppRole.openAppRoleModel(env, role.getId());
                        if (model != null) {
                            result[0] = model.delete(true);
                        }
                    } catch (final ServiceClientException | InterruptedException ex) {
                        env.processException(ex);
                    }
                }
            });

            return result[0];
            /*if (result[0]) {
                cleanup(role);
                appRoles.remove(role.getId());
            }

        } finally {
            lockDefinitionSearch(false);
        }*/
    }
    
    @Override
    public IRepositoryAdsDefinition[] listDefinitions(final RolesModuleRepository repository) {        
       /* synchronized (this) {
            if (!loaded) {                
                if (!UserExtensionManagerCommon.getInstance().isReady()) {
                    return new IRepositoryAdsDefinition[0];
                }*/
                //try {

                    UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
                        @Override
                        public void execute(IClientEnvironment env) {
                            try {
                                AdsDefinitionListDocument xDefListDoc = (AdsDefinitionListDocument) env.getEasSession().executeContextlessCommand(RolesModuleRepository.LIST_APP_ROLES_CMD_ID, null, AdsDefinitionListDocument.class);
                                if (xDefListDoc != null && xDefListDoc.getAdsDefinitionList() != null) {
                                    AdsDefinitionListDocument.AdsDefinitionList xDefList = xDefListDoc.getAdsDefinitionList();
                                    final List<AdsDefinitionListDocument.AdsDefinitionList.Definition> xList = xDefList.getDefinitionList();
                                    File srcDir = null;
                                    if (xList != null) {
                                        for (AdsDefinitionListDocument.AdsDefinitionList.Definition xDef : xList) {
                                            Id id = xDef.getId();
                                            String name = xDef.getName();
                                            String description = xDef.getDescription();
                                            boolean readOnly = xDef.getReadOnly();
                                            RoleDefinition xRole = xDef.getAdsRoleDefinition();
                                            AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
                                            final ChangeLog changeLog;
                                            if (xRole != null) {
                                                changeLog = ChangeLog.Factory.loadFromXml(xRole.getChangeLog());
                                                xDoc.addNewAdsDefinition().setAdsRoleDefinition(xRole);
                                                xRole = xDoc.getAdsDefinition().getAdsRoleDefinition();
                                                xRole.setName(name);
                                                xRole.setId(id);
                                                xRole.setDescription(description);
                                            } else {
                                                changeLog = null;
                                                AdsRoleDef role = AdsRoleDef.Factory.newInstance(id, name);
                                                if (description != null) {
                                                    role.setDescription(description);
                                                }
                                                role.appendTo(xDoc.addNewAdsDefinition(), AdsDefinition.ESaveMode.NORMAL);
                                            }

                                            if (srcDir == null) {
                                                srcDir = new File(repository.getDirectory(), "src");
                                                srcDir.mkdirs();
                                            }

                                            File roleFile = new File(srcDir, id.toString() + ".xml");
                                            try {
                                                xDoc.save(roleFile);
                                            } catch (IOException ex) {
                                                Exceptions.printStackTrace(ex);
                                            }

                                            AppRole appRole = new AppRole(id, name, description, changeLog, readOnly);
                                            UserExtensionManager.getInstance().getAppRoles().registerRole(appRole);
                                        }
                                    }
                                }
                            } catch (final ServiceClientException | InterruptedException ex) {
                                if (ex instanceof ServiceCallFault && ex.getMessage().contains(RolesModuleRepository.LIST_APP_ROLES_CMD_ID.toString())) {
                                    //ignore
                                } else {
                                    env.processException(ex);
                                }
                            }
                        }
                    });
               /* } finally {
                    loaded = true;
                }
            }*/
       // }
        return new IRepositoryAdsDefinition[0];//super.listDefinitions();
    }
    
    @Override
    public File uploadRole(final AppRole role,final RolesModuleRepository repository) {
        final File[] result = new File[1];
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(IClientEnvironment env) {
                try {
                    EntityModel roleModel = AppRole.openAppRoleModel(env, role.getId());
                    String name = (String) roleModel.getProperty(AppRole.NAME_PROP_ID).getValueObject();
                    String description = (String) roleModel.getProperty(AppRole.DESCR_PROP_ID).getValueObject();
                    Object definition = roleModel.getProperty(AppRole.DEF_PROP_ID).getValueObject();
                    boolean readOnly = ((Boolean) roleModel.getProperty(AppRole.READ_ONLY_PROP_ID).getValueObject()).booleanValue();

                    AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
                    if (definition instanceof AdsDefinitionDocument) {
                        AdsDefinitionDocument doc = (AdsDefinitionDocument) definition;
                        if (doc.getAdsDefinition() != null && doc.getAdsDefinition().getAdsRoleDefinition() != null) {
                            xDoc.addNewAdsDefinition().setAdsRoleDefinition(doc.getAdsDefinition().getAdsRoleDefinition());
                        }
                    }
                    if (xDoc.getAdsDefinition() == null || xDoc.getAdsDefinition().getAdsRoleDefinition() == null) {
                        AdsRoleDef newRole = AdsRoleDef.Factory.newInstance(role.getId(), role.getName());
                        newRole.appendTo(xDoc.addNewAdsDefinition(), AdsDefinition.ESaveMode.NORMAL);
                    }
                    RoleDefinition xDef = xDoc.getAdsDefinition().getAdsRoleDefinition();
                    xDef.setId(role.getId());
                    xDef.setName(name);
                    xDef.setDescription(description);

                    File srcDir = new File(repository.getDirectory(), "src");
                    srcDir.mkdirs();


                    File roleFile = new File(srcDir, role.getId().toString() + ".xml");
                    try {
                        xDoc.save(roleFile);
                    } catch (IOException ex) {
                        //Exceptions.printStackTrace(ex);
                        env.processException(ex);
                    }
                    result[0] = roleFile;

                    role.setName(name);
                    role.setDescription(description);
                    role.setReadOnly(readOnly);

                } catch (final ServiceClientException | InterruptedException ex) {
                    env.processException(ex);
                }
            }
        });

        return result[0];
    }

     @Override
     public IUserDefChangeSuppert createAppRoleChangeSuppert(AppRoles appRoles){
         return new VersionChangeSuppert(appRoles);
     }  
}
