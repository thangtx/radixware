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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.IMsdlSchemeManager;
import org.radixware.kernel.common.userreport.common.IUserDefChangeSuppert;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlScheme;
import static org.radixware.kernel.common.userreport.repository.msdl.MsdlScheme.CLASSGUID_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.msdl.MsdlScheme.RUNTIME_PROP_ID;
import static org.radixware.kernel.common.userreport.repository.msdl.MsdlScheme.SOURCE_PROP_ID;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlSchemes;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlSchemesModule;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlSchemesModuleRepository;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.ads.editors.msdl.creation.MsdlCreature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreationWizard;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionListDocument;
import org.radixware.schemas.adsdef.MsdlDefinition;


public class MsdlSchemeManager implements IMsdlSchemeManager{
     
    @Override
    public void save(final File runtimeFile,final AdsMsdlSchemeDef msdlScheme) {
       /* if (isReadOnly) {
            return;
        }
        final AdsMsdlSchemeDef msdlScheme = findMsdlSchemeDefinition();
        if (msdlScheme != null) {
            setName(msdlScheme.getName());
            setDescription(msdlScheme.getDescription());
        }*/
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {

            @Override
            public void execute(final IClientEnvironment env) {
                try {
                    EntityModel model = MsdlScheme.openMsdlSchemeModel(env, msdlScheme.getId());
                    model.getProperty(MsdlScheme.NAME_PROP_ID).setValueObject(msdlScheme.getName());
                    //model.getProperty(MSDL_SCHEME_DESCRIPTION_PROP_ID).setValueObject(getDescription());
                    //if (msdlScheme.isEmptyRole()) {
                    //    model.getProperty(MSDL_SCHEME_DEFINITION_PROP_ID).setValueObject(null);
                   //     model.getProperty(MSDL_SCHEME_RUNTIME_PROP_ID).setValueObject(null);
                   //    model.getProperty(MSDL_SCHEME_CLASS_GUID_PROP_ID).setValueObject(null);
                   // } else {
                        AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
                        msdlScheme.appendTo(xDoc.addNewAdsDefinition(), AdsDefinition.ESaveMode.NORMAL);
                        model.getProperty(SOURCE_PROP_ID).setValueObject(xDoc);
                        if (runtimeFile != null) {
                            Bin runtimeData = new Bin(FileUtils.readBinaryFile(runtimeFile));
                            model.getProperty(RUNTIME_PROP_ID).setValueObject(runtimeData);
                            model.getProperty(CLASSGUID_PROP_ID).setValueObject(msdlScheme.getRuntimeId().toString());
                        }
                    //}
                    model.update();
                } catch (final ModelException | IOException | ServiceClientException | InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                    //env.processException(ex);
                }
            }
        });
    }
     
    @Override
    public boolean delete(final MsdlScheme msdlScheme) {
            final boolean[] result = new boolean[]{false};
            UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
                @Override
                public void execute(IClientEnvironment env) {
                    try {
                        EntityModel model = MsdlScheme.openMsdlSchemeModel(env, msdlScheme.getId());
                        if (model != null) {
                            result[0] = model.delete(true);
                        }
                    } catch (final ServiceClientException | InterruptedException ex) {
                        env.processException(ex);
                    }
                }
            });

            return result[0];
    }
    
     @Override
    public void listDefinitions(final MsdlSchemesModuleRepository repository) {
        
       /* synchronized (this) {
            if (!loaded) {
                if (!UserExtensionManagerCommon.getInstance().isReady()) {
                    return new IRepositoryAdsDefinition[0];
                }
                try {*/
                    UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
                        @Override
                        public void execute(IClientEnvironment env) {
                            try {
                                AdsDefinitionListDocument xDefListDoc = (AdsDefinitionListDocument) env.getEasSession().executeContextlessCommand(MsdlSchemesModuleRepository.LIST_CMD_ID, null, AdsDefinitionListDocument.class);
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
                                            MsdlDefinition xMsdlScheme = xDef.getAdsMsdlSchemeDefinition();
                                            AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
                                            if (xMsdlScheme != null) {
                                                xDoc.addNewAdsDefinition().setAdsMsdlSchemeDefinition(xMsdlScheme);
                                                xMsdlScheme = xDoc.getAdsDefinition().getAdsMsdlSchemeDefinition();
                                                xMsdlScheme.setName(name);
                                                xMsdlScheme.setId(id);
                                                xMsdlScheme.setDescription(description);
                                            } else {
                                                AdsMsdlSchemeDef msdlScheme = AdsMsdlSchemeDef.Factory.newInstance(id,name);                                                
                                                if (description != null) {
                                                    msdlScheme.setDescription(description);
                                                }
                                                msdlScheme.appendTo(xDoc.addNewAdsDefinition(), AdsDefinition.ESaveMode.NORMAL);
                                            }

                                            if (srcDir == null) {
                                                srcDir = new File(repository.getDirectory(), "src");
                                                srcDir.mkdirs();
                                            }

                                            File msdlSchemeFile = new File(srcDir, id.toString() + ".xml");
                                            try {
                                                xDoc.save(msdlSchemeFile);
                                            } catch (IOException ex) {
                                                Exceptions.printStackTrace(ex);
                                            }

                                            MsdlScheme msdlScheme = new MsdlScheme(id, name, description, readOnly);
                                            UserExtensionManagerCommon.getInstance().getMsdlSchemes().registerMsdlScheme(msdlScheme);
                                        }
                                    }
                                }
                            } catch (final ServiceClientException | InterruptedException ex) {
                                if (ex instanceof ServiceCallFault && ex.getMessage().contains(MsdlSchemesModuleRepository.LIST_CMD_ID.toString())) {
                                    //ignore
                                } else {
                                    env.processException(ex);
                                }
                            }
                        }
                    });
                //} finally {
                //    loaded = true;
                //}
           // }
        //}

        //if(module==null){
            
       //}
        //return super.listDefinitions();
    }
     
    public File uploadMsdlScheme(final MsdlScheme msdlScheme, final MsdlSchemesModuleRepository repository) {
        final File[] result = new File[1];
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
            @Override
            public void execute(IClientEnvironment env) {
                try {
                    EntityModel msdlSchemeModel = MsdlScheme.openMsdlSchemeModel(env, msdlScheme.getId());
                    String name = (String) msdlSchemeModel.getProperty(MsdlScheme.NAME_PROP_ID).getValueObject();
                    //String description = (String) msdlSchemeModel.getProperty(MsdlScheme.MSDL_SCHEME_SOURCE_PROP_ID).getValueObject();
                    Object definition = msdlSchemeModel.getProperty(MsdlScheme.SOURCE_PROP_ID).getValueObject();
                    //boolean readOnly = ((Boolean) msdlSchemeModel.getProperty(MsdlScheme.MSDL_SCHEME_READ_ONLY_PROP_ID).getValueObject()).booleanValue();

                    AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
                    if (definition instanceof AdsDefinitionDocument) {
                        AdsDefinitionDocument doc = (AdsDefinitionDocument) definition;
                        if (doc.getAdsDefinition() != null && doc.getAdsDefinition().getAdsMsdlSchemeDefinition() != null) {
                            xDoc.addNewAdsDefinition().setAdsMsdlSchemeDefinition(doc.getAdsDefinition().getAdsMsdlSchemeDefinition());
                        }
                    }
                    if (xDoc.getAdsDefinition() == null || xDoc.getAdsDefinition().getAdsMsdlSchemeDefinition() == null) {
                        AdsMsdlSchemeDef newMsdlScheme= AdsMsdlSchemeDef.Factory.newInstance(msdlScheme.getId(),msdlScheme.getName());  
                        newMsdlScheme.appendTo(xDoc.addNewAdsDefinition(), AdsDefinition.ESaveMode.NORMAL);
                    }
                    MsdlDefinition xDef = xDoc.getAdsDefinition().getAdsMsdlSchemeDefinition();
                    xDef.setId(msdlScheme.getId());
                    xDef.setName(name);
                    //xDef.setDescription(description);

                    File srcDir = new File(repository.getDirectory(), "src");
                    srcDir.mkdirs();


                    File msdlSchemeFile = new File(srcDir, msdlScheme.getId().toString() + ".xml");
                    try {
                        xDoc.save(msdlSchemeFile);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    result[0] = msdlSchemeFile;

                    msdlScheme.setName(name);
                    //msdlScheme.setDescription(description);
                    //msdlScheme.setReadOnly(readOnly);

                } catch (final ServiceClientException | InterruptedException ex) {
                    env.processException(ex);
                }
            }
        });
        return result[0];
    }
    
     public MsdlScheme create(){
        //step one - launch role creature 
        //step two. create new role model

        MsdlSchemesModule m = (MsdlSchemesModule) UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().findById(MsdlSchemesModule.MODULE_ID);
        if (m == null) {
            return null;
        }
        
        final MsdlCreature  creature = new MsdlCreature(m);

       /* final AdsNamedDefinitionCreature<AdsMsdlSchemeDef> creature = new AdsNamedDefinitionCreature<AdsMsdlSchemeDef>(m.getDefinitions(), "NewMsdlScheme", "New Msdl Scheme") {
            @Override
            public AdsMsdlSchemeDef createInstance() {
                return AdsMsdlSchemeDef.Factory.newInstance();
            }

            @Override
            public RadixIcon getIcon() {
                return AdsDefinitionIcon.MSDL_SCHEME;
            }

            @Override
            public boolean afterCreate(AdsMsdlSchemeDef object) {
                name[0] = getName();
                return false;
            }
        };*/

        ICreatureGroup.ICreature<?> result = CreationWizard.execute(new ICreatureGroup[]{new ICreatureGroup() {
                @Override
                public List<ICreatureGroup.ICreature> getCreatures() {
                    return Collections.<ICreatureGroup.ICreature>singletonList(creature);
                }

                @Override
                public String getDisplayName() {
                    return "Msdl Schemes";
                }
            }}, creature);
        if (result != null) {
            final RadixObject obj=result.commit();
            if (obj instanceof AdsMsdlSchemeDef) {
                //final Id[] guid = new Id[]{null};
                final boolean[] readOnly = new boolean[]{false};
                UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {
                    @Override
                    public void execute(IClientEnvironment env) {
                        try {
                            EntityModel model = EntityModel.openPrepareCreateModel(MsdlScheme.CLASS_ID, MsdlScheme.EDITOR_ID, MsdlScheme.CLASS_ID, null, new IContext.ContextlessCreating(env));
                            model.getProperty(MsdlScheme.NAME_PROP_ID).setValueObject(obj.getName());
                            model.getProperty(MsdlScheme.GUID_PROP_ID).setValueObject(((AdsMsdlSchemeDef)obj).getId().toString());
                            AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
                            ((AdsMsdlSchemeDef)obj).appendTo(xDoc.addNewAdsDefinition(), AdsDefinition.ESaveMode.NORMAL);
                            model.getProperty(SOURCE_PROP_ID).setValueObject(xDoc);
                            if (model.create() == EEntityCreationResult.SUCCESS) {                                
                                model.read();                                
                                //guid[0] = Id.Factory.loadFrom(model.getProperty(MsdlScheme.MSDL_SCHEME_GUID_PROP_ID).getValueAsString());
                                //readOnly[0] = ((Boolean) model.getProperty(MsdlScheme.MSDL_SCHEME_READ_ONLY_PROP_ID).getValueObject()).booleanValue();
                            }

                        } catch (final ModelException | ServiceClientException | InterruptedException ex) {
                            env.processException(ex);
                        }
                    }
                });

                if (((AdsMsdlSchemeDef)obj).getId()!= null) {
                    MsdlScheme msdlScheme = new MsdlScheme(((AdsMsdlSchemeDef)obj).getId(), obj.getName(), null, readOnly[0]);
                    //registerMsdlScheme(msdlScheme);
                    //fireChange();
                    return msdlScheme;
                }
            }
        }
        return null;
    }
    

    @Override
    public IUserDefChangeSuppert createMsdlChangeSuppert(MsdlSchemes msdlSchemes) {
        return new VersionChangeSuppert(msdlSchemes);
    }
}
