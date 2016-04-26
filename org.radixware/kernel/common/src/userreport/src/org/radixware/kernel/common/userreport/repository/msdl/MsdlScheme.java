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

package org.radixware.kernel.common.userreport.repository.msdl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.utils.Utils;


public class MsdlScheme {
    public static final Id CLASS_ID = Id.Factory.loadFrom("aecMXLSZGBLI5ECNDF4PJK6CNKJPQ");
    public static final Id EDITOR_ID = Id.Factory.loadFrom("eprPQ5KOBANVZDZVHO7P76UKTJDNU");
    public static final Id RUNTIME_PROP_ID = Id.Factory.loadFrom("colYX26DW4DHJHOVMTS4TAJRYASG4");
    public static final Id SOURCE_PROP_ID = Id.Factory.loadFrom("colBIPJ6JCOKZBG3DOKDPMCQJJCAY");
    public static final Id NAME_PROP_ID = Id.Factory.loadFrom("col32KF5KAE2RB6JL7MLOASNV2NFU");
    //public static final Id MSDL_SCHEME_DESCRIPTION_PROP_ID = Id.Factory.loadFrom("colPJZG5RYZF7PBDIJEABQAQH3XQ4");
    public static final Id GUID_PROP_ID = Id.Factory.loadFrom("col4SI755YCLRE4BOBSKX7BVUVQY4");
    public static final Id CLASSGUID_PROP_ID = Id.Factory.loadFrom("colNVW7D7HU25EHPJ5HTPPZJM37CE");
    //public static final Id MSDL_SCHEME_READ_ONLY_PROP_ID = Id.Factory.loadFrom("prdPZ5BW3BSXBE2RBOY2D3QOUWQ7M");
    
    private final Id schemeId;
    private String name;
    private String description;
    private boolean isReadOnly;
    private final PropertyChangeSupport propChangeSupport = new PropertyChangeSupport(this);

    public MsdlScheme(final Id schemeId,final String name,final String description,final boolean isReadOnly) {
        this.schemeId = schemeId;
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
        return schemeId;
    }    
    

    public AdsMsdlSchemeDef findMsdlSchemeDefinition() {
        return UserExtensionManagerCommon.getInstance().getMsdlSchemes().findMsdlSchemeDefById(schemeId);
    }

    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        propChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propChangeSupport.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(final String propertyName,final PropertyChangeListener listener) {
        propChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        propChangeSupport.addPropertyChangeListener(listener);
    }

    private static Pid getMsdlSchemePid(final Id schemeId) {
        return new Pid(CLASS_ID, schemeId.toString());
    }

    public static EntityModel openMsdlSchemeModel(final IClientEnvironment env,final Id schemeId) throws ServiceClientException, InterruptedException {
        return EntityModel.openContextlessModel(env, getMsdlSchemePid(schemeId), CLASS_ID, EDITOR_ID);
    }

    public void save() {
        saveImpl(null);
        UserExtensionManagerCommon.getInstance().compileOnSave(findMsdlSchemeDefinition(),false);
    }

    private void saveImpl(final File runtimeFile) {
        if (isReadOnly) {
            return;
        }
        final AdsMsdlSchemeDef msdlScheme = findMsdlSchemeDefinition();
        if (msdlScheme != null) {
            setName(msdlScheme.getName());
            setDescription(msdlScheme.getDescription());
        }
        UserExtensionManagerCommon.getInstance().getMsdlSchemesManager().save( runtimeFile, msdlScheme);
        /*UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {

            @Override
            public void execute(final IClientEnvironment env) {
                try {
                    EntityModel model = openMsdlSchemeModel(env, id);
                    model.getProperty(MSDL_SCHEME_NAME_PROP_ID).setValueObject(getName());
                    //model.getProperty(MSDL_SCHEME_DESCRIPTION_PROP_ID).setValueObject(getDescription());
                    //if (msdlScheme.isEmptyRole()) {
                    //    model.getProperty(MSDL_SCHEME_DEFINITION_PROP_ID).setValueObject(null);
                   //     model.getProperty(MSDL_SCHEME_RUNTIME_PROP_ID).setValueObject(null);
                   //    model.getProperty(MSDL_SCHEME_CLASS_GUID_PROP_ID).setValueObject(null);
                   // } else {
                        AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
                        msdlScheme.appendTo(xDoc.addNewAdsDefinition(), AdsDefinition.ESaveMode.NORMAL);
                        model.getProperty(MSDL_SCHEME_SOURCE_PROP_ID).setValueObject(xDoc);
                        if (runtimeFile != null) {
                            Bin runtimeData = new Bin(FileUtils.readBinaryFile(runtimeFile));
                            model.getProperty(MSDL_SCHEME_RUNTIME_PROP_ID).setValueObject(runtimeData);
                            model.getProperty(MSDL_SCHEME_CLASS_GUID_PROP_ID).setValueObject(msdlScheme.getRuntimeId().toString());
                        }
                    //}
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
        UserExtensionManagerCommon.getInstance().getMsdlSchemes().cleanup(this);
    }

    public void reload() {
        cleanup();
    }

    public void delete() {
        if (isReadOnly()) {
            return;
        }
        UserExtensionManagerCommon.getInstance().getMsdlSchemes().delete(this);
    }
    
}
