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

package org.radixware.kernel.common.userreport.repository.role;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsModule;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.extrepository.UserExtADSSegmentRepository;


public class RolesModuleRepository extends FSRepositoryAdsModule {

    public static final Id LIST_APP_ROLES_CMD_ID = Id.Factory.loadFrom("clcLRR7REE43JEINGXOLFOLEEPDIY");
    public static final String NAME = "AppRoles";
    private final UserExtADSSegmentRepository segmentRepo;
    private volatile boolean loaded = false;

    public RolesModuleRepository(UserExtADSSegmentRepository segmentRepo, File moduleDir) {
        super(moduleDir);
        this.segmentRepo = segmentRepo;
    }

    public RolesModuleRepository(UserExtADSSegmentRepository segmentRepo, AdsModule module) {
        super(module);
        this.segmentRepo = segmentRepo;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean containsDefinition(Id id) {
        listDefinitions();
        return super.containsDefinition(id);
    }

    @Override
    public IRepositoryAdsDefinition[] listDefinitions() {        
        synchronized (this) {
            if (!loaded) {                
                if (!UserExtensionManagerCommon.getInstance().isReady()) {
                    return new IRepositoryAdsDefinition[0];
                }
                try {
                    if(UserExtensionManagerCommon.getInstance().getUserRoleManager()!=null){
                        UserExtensionManagerCommon.getInstance().getUserRoleManager().listDefinitions(this);
                    }
                   /* final CountDownLatch lock = new CountDownLatch(1);

                    UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
                        @Override
                        public void execute(IClientEnvironment env) {
                            try {
                                AdsDefinitionListDocument xDefListDoc = (AdsDefinitionListDocument) env.getEasSession().executeContextlessCommand(LIST_APP_ROLES_CMD_ID, null, AdsDefinitionListDocument.class);
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
                                            if (xRole != null) {

                                                xDoc.addNewAdsDefinition().setAdsRoleDefinition(xRole);
                                                xRole = xDoc.getAdsDefinition().getAdsRoleDefinition();
                                                xRole.setName(name);
                                                xRole.setId(id);
                                                xRole.setDescription(description);
                                            } else {
                                                AdsRoleDef role = AdsRoleDef.Factory.newInstance(id, name);
                                                if (description != null) {
                                                    role.setDescription(description);
                                                }
                                                role.appendTo(xDoc.addNewAdsDefinition(), AdsDefinition.ESaveMode.NORMAL);
                                            }

                                            if (srcDir == null) {
                                                srcDir = new File(getDirectory(), "src");
                                                srcDir.mkdirs();
                                            }

                                            File roleFile = new File(srcDir, id.toString() + ".xml");
                                            try {
                                                xDoc.save(roleFile);
                                            } catch (IOException ex) {
                                                Exceptions.printStackTrace(ex);
                                            }

                                            AppRole appRole = new AppRole(id, name, description, readOnly);
                                            UserExtensionManager.getInstance().getAppRoles().registerRole(appRole);
                                        }
                                    }
                                }
                            } catch (final ServiceClientException | InterruptedException ex) {
                                if (ex instanceof ServiceCallFault && ex.getMessage().contains(LIST_APP_ROLES_CMD_ID.toString())) {
                                    //ignore
                                } else {
                                    env.processException(ex);
                                }
                            } finally {
                                lock.countDown();
                            }
                        }
                    });
                    try {
                        lock.await();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }*/
                } finally {
                    loaded = true;
                }
            }
        }


        return new IRepositoryAdsDefinition[0];//super.listDefinitions();
    }

    void reload() {
        synchronized (this) {
            if (loaded) {
                loaded = false;
            }
        }
    }

    File uploadRole(final AppRole role) {
        return  UserExtensionManagerCommon.getInstance().getUserRoleManager().uploadRole(role, this);
       /* final File[] result = new File[1];
        final CountDownLatch lock = new CountDownLatch(1);
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
            @Override
            public void execute(IClientEnvironment env) {
                try {
                    EntityModel roleModel = AppRole.openAppRoleModel(env, role.getId());
                    String name = (String) roleModel.getProperty(AppRole.APP_ROLE_NAME_PROP_ID).getValueObject();
                    String description = (String) roleModel.getProperty(AppRole.APP_ROLE_DESCRIPTION_PROP_ID).getValueObject();
                    Object definition = roleModel.getProperty(AppRole.APP_ROLE_DEFINITION_PROP_ID).getValueObject();
                    boolean readOnly = ((Boolean) roleModel.getProperty(AppRole.APP_ROLE_READ_ONLY_PROP_ID).getValueObject()).booleanValue();

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

                    File srcDir = new File(getDirectory(), "src");
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
                } finally {
                    lock.countDown();
                }
            }
        });
        try {
            lock.await();
        } catch (InterruptedException ex) {
            //Exceptions.printStackTrace(ex);
            env.processException(ex);
        }
        return result[0];*/
    }
}
