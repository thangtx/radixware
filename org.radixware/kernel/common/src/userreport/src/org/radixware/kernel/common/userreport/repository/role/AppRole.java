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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.utils.Utils;

public class AppRole {

    public static final Id CLASS_ID = Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4");
    public static final Id EDITOR_ID = Id.Factory.loadFrom("eprSIJFTYEHKVAQLBWEFIMTRLF5AQ");
    public static final Id RUNTIME_PROP_ID = Id.Factory.loadFrom("colPJH2TGKX3JAIDCK2GK4H763RDQ");
    public static final Id DEF_PROP_ID = Id.Factory.loadFrom("colGQVSU7TJYRFCZFU7TADGWIRGHY");
    public static final Id NAME_PROP_ID = Id.Factory.loadFrom("colMFRUYXP6F3PBDIJEABQAQH3XQ4");
    public static final Id DESCR_PROP_ID = Id.Factory.loadFrom("colPJZG5RYZF7PBDIJEABQAQH3XQ4");
    public static final Id GUID_PROP_ID = Id.Factory.loadFrom("col6FMTUQ76F3PBDIJEABQAQH3XQ4");
    public static final Id CLASS_GUID_PROP_ID = Id.Factory.loadFrom("colIXVB57MI2RADLC5IAUFV5QB67Y");
    public static final Id READ_ONLY_PROP_ID = Id.Factory.loadFrom("prdPZ5BW3BSXBE2RBOY2D3QOUWQ7M");
    private final Id roleId;
    private String name;
    private String description;
    private boolean isReadOnly;
    private final PropertyChangeSupport propChangeSupport = new PropertyChangeSupport(this);

    public AppRole(final Id roleId, final String name, final String description, final boolean isReadOnly) {
        this.roleId = roleId;
        this.name = name;
        this.description = description;
        this.isReadOnly = isReadOnly;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setReadOnly(final boolean readOnly) {
        if (this.isReadOnly != readOnly) {
            this.isReadOnly = readOnly;
            propChangeSupport.firePropertyChange("readOnly", !this.isReadOnly, this.isReadOnly);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        if (!Utils.equals(this.name, name)) {
            final String oldName = this.name;
            this.name = name;
            propChangeSupport.firePropertyChange("name", oldName, name);
        }

    }

    public Id getId() {
        return roleId;
    }

    public AdsRoleDef findRoleDefinition() {
        return UserExtensionManagerCommon.getInstance().getAppRoles().findRoleDefById(roleId);
    }

    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        propChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propChangeSupport.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        propChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        propChangeSupport.addPropertyChangeListener(listener);
    }

    private static Pid getAppRolePid(final Id roleId) {
        return new Pid(CLASS_ID, roleId.toString());
    }

    public static EntityModel openAppRoleModel(final IClientEnvironment env, final Id roleId) throws ServiceClientException, InterruptedException {
        return EntityModel.openContextlessModel(env, getAppRolePid(roleId), CLASS_ID, EDITOR_ID);
    }

    public void save() {
        saveImpl(null);
        UserExtensionManagerCommon.getInstance().compileOnSave(findRoleDefinition(), false);
    }

    private void saveImpl(final File runtimeFile) {
        if (isReadOnly) {
            return;
        }
        final AdsRoleDef role = findRoleDefinition();
        if (role != null) {
            setName(role.getName());
            setDescription(role.getDescription());
        }
        UserExtensionManagerCommon.getInstance().getUserRoleManager().saveImpl(runtimeFile, role);
        /* UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {

         @Override
         public void execute(final IClientEnvironment env) {
         try {
         EntityModel model = openAppRoleModel(env, roleId);
         model.getProperty(APP_ROLE_NAME_PROP_ID).setValueObject(getName());
         model.getProperty(APP_ROLE_DESCRIPTION_PROP_ID).setValueObject(getDescription());
         if (role.isEmptyRole()) {
         model.getProperty(APP_ROLE_DEFINITION_PROP_ID).setValueObject(null);
         model.getProperty(APP_ROLE_RUNTIME_PROP_ID).setValueObject(null);
         model.getProperty(APP_ROLE_CLASS_GUID_PROP_ID).setValueObject(null);
         } else {
         AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
         role.appendTo(xDoc.addNewAdsDefinition(), AdsDefinition.ESaveMode.NORMAL);
         model.getProperty(APP_ROLE_DEFINITION_PROP_ID).setValueObject(xDoc);
         if (runtimeFile != null) {
         Bin runtimeData = new Bin(FileUtils.readBinaryFile(runtimeFile));
         model.getProperty(APP_ROLE_RUNTIME_PROP_ID).setValueObject(runtimeData);
         model.getProperty(APP_ROLE_CLASS_GUID_PROP_ID).setValueObject(role.getRuntimeId().toString());
         }
         }
         model.update();
         } catch (final ModelException | IOException | ServiceClientException | InterruptedException ex) {
         //Exceptions.printStackTrace(ex);
         env.processException(ex);
         }
         }
         });*/
    }

    public void updateRuntime(final File runtimeFile) throws IOException {
        saveImpl(runtimeFile);
    }

    private void cleanup() {
        UserExtensionManagerCommon.getInstance().getAppRoles().cleanup(this);
    }

    public void reload() {
        cleanup();
    }

    public void delete() {
        if (isReadOnly()) {
            return;
        }
        UserExtensionManagerCommon.getInstance().getAppRoles().delete(this);
    }
}
