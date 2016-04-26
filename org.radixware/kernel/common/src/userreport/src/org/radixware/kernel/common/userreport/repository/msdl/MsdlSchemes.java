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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.IUserDefChangeSuppert;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.extrepository.UserExtAdsSegment;



public class MsdlSchemes {
    private Map<Id, MsdlScheme> msdlSchemes = null;
    private IUserDefChangeSuppert changeSupport ;//= new IUserDefChangeSuppert(this);

    public void registerMsdlScheme(MsdlScheme msdlScheme) {
        if (msdlSchemes == null) {
            msdlSchemes = new HashMap<>();
        }
        msdlSchemes.put(msdlScheme.getId(), msdlScheme);
    }

    private List<MsdlScheme> initializeSchemes() {        
        if (msdlSchemes == null) {            
            msdlSchemes = new HashMap<>();
            UserExtAdsSegment segment = UserExtensionManagerCommon.getInstance().getReportsSegment();
            if (segment == null) {
                return Collections.emptyList();
            }
            MsdlSchemesModule m = (MsdlSchemesModule) segment.getModules().findById(MsdlSchemesModule.MODULE_ID);
            if (m != null) {
                m.getDefinitions().list();
            }
            
        }
        return new ArrayList<>(msdlSchemes.values());
    }

    public List<MsdlScheme> getMsdlSchemes() {
        return initializeSchemes();
    }

    public AdsMsdlSchemeDef findMsdlSchemeDefById(Id id) {
        if (isDefinitionSearchLocked()) {
            return null;
        }
        MsdlSchemesModule m = (MsdlSchemesModule) UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().findById(MsdlSchemesModule.MODULE_ID);
        if (m != null) {
            return (AdsMsdlSchemeDef) m.getDefinitions().findById(id);
        }
        return null;
    }

    public MsdlSchemesModule findMsdlSchemesModule() {
        return (MsdlSchemesModule) UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().findById(MsdlSchemesModule.MODULE_ID);
    }
    
    public void initChangeListener() {
        if(changeSupport==null){
            changeSupport=UserExtensionManagerCommon.getInstance().getMsdlSchemesManager().createMsdlChangeSuppert(this);
        }
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public boolean hasListeners() {
        return changeSupport.hasListeners();
    }

    public void addChangeListener(ChangeListener listener) {
        initChangeListener();
        changeSupport.addChangeListener(listener);
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
            changeSupport.fireChange();
           // UserExtensionManagerCommon.getInstance().getMsdlSchemes().fireChange();
        }
    }

    private boolean isDefinitionSearchLocked() {
        synchronized (this) {
            return searchLockCount > 0;
        }
    }

    void cleanup(MsdlScheme msdlScheme) {
        try {
            lockDefinitionSearch(true);
            AdsMsdlSchemeDef def = null;

            MsdlSchemesModule m = (MsdlSchemesModule) UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().findById(MsdlSchemesModule.MODULE_ID);
            if (m != null) {
                for (AdsDefinition d : m.getDefinitions()) {
                    if (d.getId() == msdlScheme.getId() && d instanceof AdsMsdlSchemeDef) {
                        def = (AdsMsdlSchemeDef) d;
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

   /* void fireChange() {
        changeSupport.fireChange();
    }*/

    public MsdlScheme findMsdlScheme(Id id) {
        if (msdlSchemes == null) {
            return null;
        }
        return msdlSchemes.get(id);
    }

    public MsdlScheme create() {
        //step one - launch role creature 
        //step two. create new role model

       /* MsdlSchemesModule m = (MsdlSchemesModule) UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().findById(MsdlSchemesModule.MSDL_SCHEMES_MODULE_ID);
        if (m == null) {
            return null;
        }
        
        final MsdlCreature  creature = new MsdlCreature(m);

       final AdsNamedDefinitionCreature<AdsMsdlSchemeDef> creature = new AdsNamedDefinitionCreature<AdsMsdlSchemeDef>(m.getDefinitions(), "NewMsdlScheme", "New Msdl Scheme") {
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
        };

        ICreatureGroup.ICreature result = CreationWizard.execute(new ICreatureGroup[]{new ICreatureGroup() {
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
                final CountDownLatch lock = new CountDownLatch(1);
                UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
                    @Override
                    public void execute(IClientEnvironment env) {
                        try {
                            EntityModel model = EntityModel.openPrepareCreateModel(MsdlScheme.MSDL_SCHEME_CLASS_ID, MsdlScheme.MSDL_SCHEME_EDITOR_ID, MsdlScheme.MSDL_SCHEME_CLASS_ID, null, new IContext.ContextlessCreating(env));
                            model.getProperty(MsdlScheme.MSDL_SCHEME_NAME_PROP_ID).setValueObject(obj.getName());
                            model.getProperty(MsdlScheme.MSDL_SCHEME_GUID_PROP_ID).setValueObject(((AdsMsdlSchemeDef)obj).getId().toString());
                            AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
                            ((AdsMsdlSchemeDef)obj).appendTo(xDoc.addNewAdsDefinition(), AdsDefinition.ESaveMode.NORMAL);
                            model.getProperty(MSDL_SCHEME_SOURCE_PROP_ID).setValueObject(xDoc);
                            if (model.create() == EEntityCreationResult.SUCCESS) {                                
                                model.read();                                
                                //guid[0] = Id.Factory.loadFrom(model.getProperty(MsdlScheme.MSDL_SCHEME_GUID_PROP_ID).getValueAsString());
                                //readOnly[0] = ((Boolean) model.getProperty(MsdlScheme.MSDL_SCHEME_READ_ONLY_PROP_ID).getValueObject()).booleanValue();
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
                if (((AdsMsdlSchemeDef)obj).getId()!= null) {
                    MsdlScheme msdlScheme = new MsdlScheme(((AdsMsdlSchemeDef)obj).getId(), obj.getName(), null, readOnly[0]);
                    registerMsdlScheme(msdlScheme);
                    fireChange();
                    return msdlScheme;
                }
            }
        }
        return null;*/
        MsdlScheme msdlScheme = UserExtensionManagerCommon.getInstance().getMsdlSchemesManager().create();
        if(msdlScheme!=null){
            registerMsdlScheme(msdlScheme);
            changeSupport.fireChange();
            return msdlScheme;
        }
        return null;
    }

    public void reload() {
        try {
            lockDefinitionSearch(true);
            MsdlSchemesModule m = (MsdlSchemesModule) UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().findById(MsdlSchemesModule.MODULE_ID);
            if (m != null) {
                for (AdsDefinition d : m.getDefinitions()) {
                    AdsLocalizingBundleDef bundle = d.findExistingLocalizingBundle();
                    if (bundle != null) {
                        bundle.delete();
                    }
                    d.delete();
                }
                msdlSchemes.clear();
                ((MsdlSchemesModuleRepository) m.getRepository()).reload();
                try {
                    m.reload();
                } catch (IOException ex) {
                    UserExtensionManagerCommon.getInstance().getUFRequestExecutor().processException(ex);
                }
            }


        } finally {
            lockDefinitionSearch(false);
        }
    }

    void delete(final MsdlScheme msdlScheme) {
        try {
            lockDefinitionSearch(true);
            /*final boolean[] result = new boolean[]{false};
            final CountDownLatch lock = new CountDownLatch(1);
            UserExtensionManagerCommon.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
                @Override
                public void execute(IClientEnvironment env) {
                    try {
                        EntityModel model = MsdlScheme.openMsdlSchemeModel(env, msdlScheme.getId());
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
            boolean result=UserExtensionManagerCommon.getInstance().getMsdlSchemesManager().delete(  msdlScheme);
            if (result) {
                cleanup(msdlScheme);
                msdlSchemes.remove(msdlScheme.getId());
            }

        } finally {
            lockDefinitionSearch(false);
        }

    }

    public void close() {
        msdlSchemes = null;
        //changeSupport = UserExtensionManagerCommon.getInstance().getMsdlSchemesManager().createMsdlChangeSuppert(this);
        MsdlSchemesModule.dispose();
    }
}
