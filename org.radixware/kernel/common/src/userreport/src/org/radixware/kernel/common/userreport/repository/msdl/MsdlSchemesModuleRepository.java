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

import java.io.File;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsModule;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.extrepository.UserExtADSSegmentRepository;


public class MsdlSchemesModuleRepository  extends FSRepositoryAdsModule{
    public static final Id LIST_CMD_ID = Id.Factory.loadFrom("clcCNMTAQDFFRFGVB7UUWL2T6AAE4");
    public static final String NAME = "MsdlSchemes";
    //private final UserExtADSSegmentRepository segmentRepo;
    private volatile boolean loaded = false;

    public MsdlSchemesModuleRepository(final UserExtADSSegmentRepository segmentRepo,final File moduleDir) {
        super(moduleDir);
        //this.segmentRepo = segmentRepo;
    }

    public MsdlSchemesModuleRepository(final UserExtADSSegmentRepository segmentRepo,final AdsModule module) {
        super(module);
        //this.segmentRepo = segmentRepo;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean containsDefinition(final Id schemeId) {
        listDefinitions();
        return super.containsDefinition(schemeId);
    }

    @Override
    public IRepositoryAdsDefinition[] listDefinitions() {
       
        synchronized (this) {
            if (!loaded) {
                if (!UserExtensionManagerCommon.getInstance().isReady()) {
                    return new IRepositoryAdsDefinition[0];
                }
                try {
                    if(UserExtensionManagerCommon.getInstance().getMsdlSchemesManager()!=null){
                        UserExtensionManagerCommon.getInstance().getMsdlSchemesManager().listDefinitions(this);
                    }
                   /* final CountDownLatch lock = new CountDownLatch(1);

                    UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
                        @Override
                        public void execute(IClientEnvironment env) {
                            try {
                                AdsDefinitionListDocument xDefListDoc = (AdsDefinitionListDocument) env.getEasSession().executeContextlessCommand(LIST_APP_MSDL_SCHEMES_CMD_ID, null, AdsDefinitionListDocument.class);
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
                                                srcDir = new File(getDirectory(), "src");
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
                                if (ex instanceof ServiceCallFault && ex.getMessage().contains(LIST_APP_MSDL_SCHEMES_CMD_ID.toString())) {
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

        if(module==null){
            
        }
        return super.listDefinitions();
    }

    void reload() {
        synchronized (this) {
            if (loaded) {
                loaded = false;
            }
        }
    }

    File uploadMsdlScheme(final MsdlScheme msdlScheme) {
        /*final File[] result = new File[1];
        final CountDownLatch lock = new CountDownLatch(1);
        UserExtensionManagerCommon.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
            @Override
            public void execute(IClientEnvironment env) {
                try {
                    EntityModel msdlSchemeModel = MsdlScheme.openMsdlSchemeModel(env, msdlScheme.getId());
                    String name = (String) msdlSchemeModel.getProperty(MsdlScheme.MSDL_SCHEME_NAME_PROP_ID).getValueObject();
                    //String description = (String) msdlSchemeModel.getProperty(MsdlScheme.MSDL_SCHEME_SOURCE_PROP_ID).getValueObject();
                    Object definition = msdlSchemeModel.getProperty(MsdlScheme.MSDL_SCHEME_SOURCE_PROP_ID).getValueObject();
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

                    File srcDir = new File(getDirectory(), "src");
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
        return UserExtensionManagerCommon.getInstance().getMsdlSchemesManager().uploadMsdlScheme(msdlScheme, this);//result[0];
    }
}
