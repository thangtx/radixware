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

import java.io.IOException;
import java.util.*;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.IUserDefChangeSuppert;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.extrepository.UserExtAdsSegment;



public class AppRoles {
    private Map<Id, AppRole> appRoles = null;
    private IUserDefChangeSuppert changeSupport;
    //private ChangeSupport changeSupport = new ChangeSupport(this);
    
    public void registerRole(AppRole role) {
        if (appRoles == null) {
            appRoles = new HashMap<>();
        }
        appRoles.put(role.getId(), role);
    }

    private List<AppRole> initializeRoles() {   
        initChangeListener();
        if (appRoles == null) {            
            appRoles = new HashMap<>();
            UserExtAdsSegment segment = UserExtensionManagerCommon.getInstance().getReportsSegment();
            if (segment == null) {
                return Collections.emptyList();
            }
            RolesModule m = (RolesModule) segment.getModules().findById(RolesModule.MODULE_ID);
            if (m != null) {
                m.getDefinitions().list();
            }
        }
        return new ArrayList<>(appRoles.values());
    }

    public List<AppRole> getRoles() {
        return initializeRoles();
    }

    public AdsRoleDef findRoleDefById(Id id) {
        if (isDefinitionSearchLocked()) {
            return null;
        }
        RolesModule m = (RolesModule) UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().findById(RolesModule.MODULE_ID);
        if (m != null) {
            return (AdsRoleDef) m.getDefinitions().findById(id);
        }
        return null;
    }

    public RolesModule findRolesModule() {
        return (RolesModule) UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().findById(RolesModule.MODULE_ID);
    }

    public void initChangeListener() {
        if(changeSupport==null){
            changeSupport=UserExtensionManagerCommon.getInstance().getUserRoleManager().createAppRoleChangeSuppert(this);
        }
    }
    
    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
        //UserExtensionManagerCommon.getInstance().getUserRoleManager().removeRolesChangeListener(listener);
    }

    public boolean hasListeners() {
        return changeSupport.hasListeners();
    }

    public void addChangeListener(ChangeListener listener) {
        initChangeListener();
        changeSupport.addChangeListener(listener);
        //UserExtensionManagerCommon.getInstance().getUserRoleManager().addRolesChangeListener(listener);
    }
    private int searchLockCount = 0;

    private void lockDefinitionSearch(boolean lock) {
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
           UserExtensionManagerCommon.getInstance().getAppRoles().fireChange();
        }
    }

    private boolean isDefinitionSearchLocked() {
        synchronized (this) {
            return searchLockCount > 0;
        }
    }

    void cleanup(AppRole role) {
        try {
            lockDefinitionSearch(true);
            AdsRoleDef def = null;

            RolesModule m = (RolesModule) UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().findById(RolesModule.MODULE_ID);
            if (m != null) {
                for (AdsDefinition d : m.getDefinitions()) {
                    if (d.getId() == role.getId() && d instanceof AdsRoleDef) {
                        def = (AdsRoleDef) d;
                        break;
                    }
                }
            }

            if (def != null) {
                AdsLocalizingBundleDef bundle = def.findExistingLocalizingBundle();
                if (bundle != null) {
                    bundle.delete();
                }
                def.delete();
            }
        } finally {
            lockDefinitionSearch(false);
        }
    }

    void fireChange() {
        //UserExtensionManagerCommon.getInstance().getUserRoleManager().fireRolesChange();
        changeSupport.fireChange();
    }

    public AppRole findAppRole(Id id) {
        if (appRoles == null) {
            return null;
        }
        return appRoles.get(id);
    }

    public AppRole create() {
        //step one - launch role creature 
        //step two. create new role model

        /*RolesModule m = (RolesModule) UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().findById(RolesModule.USER_ROLES_MODULE_ID);
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

        ICreatureGroup.ICreature result = CreationWizard.execute(new ICreatureGroup[]{new ICreatureGroup() {
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
                final CountDownLatch lock = new CountDownLatch(1);
                UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
                    @Override
                    public void execute(IClientEnvironment env) {

                        try {
                            EntityModel model = EntityModel.openPrepareCreateModel(AppRole.APP_ROLE_CLASS_ID, AppRole.APP_ROLE_EDITOR_ID, AppRole.APP_ROLE_CLASS_ID, null, new IContext.ContextlessCreating(env));
                            model.getProperty(AppRole.APP_ROLE_NAME_PROP_ID).setValueObject(name[0]);
                            if (model.create() == EEntityCreationResult.SUCCESS) {
                                model.read();
                                guid[0] = Id.Factory.loadFrom(model.getProperty(AppRole.APP_ROLE_GUID_PROP_ID).getValueAsString());
                                readOnly[0] = ((Boolean) model.getProperty(AppRole.APP_ROLE_READ_ONLY_PROP_ID).getValueObject()).booleanValue();
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
                }*/
            AppRole role=UserExtensionManagerCommon.getInstance().getUserRoleManager().create();
            if (role != null) {
                //AppRole role = new AppRole(guid[0], name[0], null, readOnly[0]);
                registerRole(role);
                fireChange();
                return role;
            }
            //}
        //}
        return null;
    }

    public void reload() {
        try {
            lockDefinitionSearch(true);
            RolesModule m = (RolesModule) UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().findById(RolesModule.MODULE_ID);
            if (m != null) {
                for (AdsDefinition d : m.getDefinitions()) {
                    AdsLocalizingBundleDef bundle = d.findExistingLocalizingBundle();
                    if (bundle != null) {
                        bundle.delete();
                    }
                    d.delete();
                }
                appRoles.clear();
                ((RolesModuleRepository) m.getRepository()).reload();
                try {
                    m.reload();
                } catch (IOException ex) {
                    //Exceptions.printStackTrace(ex);
                    UserExtensionManagerCommon.getInstance().getUFRequestExecutor().processException(ex);
                }
            }


        } finally {
            lockDefinitionSearch(false);
        }
    }

    void delete(final AppRole role) {
        try {
            lockDefinitionSearch(true);
            final boolean result=UserExtensionManagerCommon.getInstance().getUserRoleManager().delete(role);
            /*final boolean[] result = new boolean[]{false};
            final CountDownLatch lock = new CountDownLatch(1);
            UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
                @Override
                public void execute(IClientEnvironment env) {
                    try {
                        EntityModel model = AppRole.openAppRoleModel(env, role.getId());
                        if (model != null) {
                            result[0] = model.delete(true);
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
                Exceptions.printStackTrace(ex);
            }*/
            if (result) {
                cleanup(role);
                appRoles.remove(role.getId());
            }

        } finally {
            lockDefinitionSearch(false);
        }

    }

    public void close() {
        appRoles = null;
     //   changeSupport = UserExtensionManagerCommon.getInstance().getUserRoleManager().createAppRoleChangeSuppert(this);
        RolesModule.dispose();
    }
}
