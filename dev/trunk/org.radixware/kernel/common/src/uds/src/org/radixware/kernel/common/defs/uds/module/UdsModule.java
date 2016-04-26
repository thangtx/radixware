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

package org.radixware.kernel.common.defs.uds.module;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.uds.UdsDefinitionIcon;
import org.radixware.kernel.common.defs.uds.userfunc.UdsUserFuncDef;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Modules;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.uds.IRepositoryUdsModule;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.*;

import org.radixware.schemas.udsdef.UdsDefinitionDocument.UdsDefinition;
import org.radixware.schemas.udsdef.UdsDefinitionListDocument;
import org.radixware.schemas.udsdef.UdsDefinitionListDocument.UdsDefinitionList;


public class UdsModule extends AdsModule {

    public static final String ETC_DIR_NAME = "etc";

    @Override
    public File getBinDirContainer() {
        return getRepository().getBinariesDirContainer();
    }

    @Override
    public File getSrcDirContainer() {
        return getRepository().getJavaSrcDirContainer();
    }

    public static class Factory extends Module.Factory<UdsModule> {

        private static final Factory FACTORY_INSTANCE = new Factory();

        public static Factory getDefault() {
            return FACTORY_INSTANCE;
        }

        @Override
        public UdsModule newInstance(Id moduleId, String moduleName) {
            return new UdsModule(moduleId, moduleName);
        }
    }

    public File getEtcDir() {
        return new File(getDirectory(), ETC_DIR_NAME);
    }
    // private ModuleDefinitions definitions;

    public UdsModule(Id id, String name) {
        super(id, name);
    }

    @Override
    public AdsModule findCompanionModule() {
        return null;
    }

    @Override
    public ERepositorySegmentType getSegmentType() {
        return ERepositorySegmentType.UDS;
    }

    public static void enableUsageStatistics() {
    }

    public void resetUsagesStatistics() {
    }

    @Override
    public void collectDependences(final List<Definition> list) {
        final Set<Module> modules = new HashSet<>();
        final List<Definition> deps = new ArrayList<>(50);
        this.getDefinitions().visit(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                deps.clear();
                radixObject.collectDependences(deps);
                for (Definition def : deps) {
                    if (!modules.contains(def.getModule())) {
                        modules.add(def.getModule());
                        list.add(def.getModule());
                    }
                }
            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());

        final Module overwritten = findOverwritten();
        if (overwritten != null) {
            list.add(overwritten);
        }
    }

    private static class UdsSearcher extends DefinitionSearcher<Definition> {

        public UdsSearcher(Definition context) {
            super(context);
        }

        @Override
        public Definition findInsideById(Id id) {
            return null;
        }

        @Override
        public DefinitionSearcher<Definition> findSearcher(Module module) {
            if (module instanceof UdsModule) {
                return new UdsSearcher(module);
            } else {
                return null;
            }
        }
    }

    @Override
    public DefinitionSearcher<? extends Definition> getDefinitionSearcher() {
        return new UdsSearcher(this);
    }

    @Override
    public RadixIcon getIcon() {
        return UdsDefinitionIcon.MODULE;
    }

//    public ModuleDefinitions getDefinitions() {
//        synchronized (this) {
//            if (definitions == null) {
//                definitions = new ModuleDefinitions(this);
//            }
//            return definitions;
//        }
//    }
//
//    public ModuleDefinitions getDefinitionsIfLoaded() {
//        synchronized (this) {
//            return definitions;
//        }
//    }
    @Override
    public IRepositoryUdsModule getRepository() {
        return (IRepositoryUdsModule) super.getRepository();
    }

    @Override
    protected ModuleDefinitions createDefinitinosList() {
        return new org.radixware.kernel.common.defs.uds.module.ModuleDefinitions(this, true);
    }
    /**
     *
     */
    private boolean isImportInProgress = false;
    final Lock importLock = new ReentrantLock();

    public int importUserDefinitions(File fromFile, boolean removeExisting, List<Definition> duplicates) throws IOException {

        try {
            importLock.lock();
            enableAutoUpload(false);
            ((org.radixware.kernel.common.defs.uds.module.ModuleDefinitions) getDefinitions()).enableAutoDependences(false);
            isImportInProgress = true;
            int count = 0;

            try {
                if (removeExisting) {
                    List<AdsDefinition> defs = getDefinitions().list();
                    for (Definition def : defs) {
                        def.delete();
                    }

                }
                UdsDefinitionListDocument xDoc = UdsDefinitionListDocument.Factory.parse(fromFile);
                UdsDefinitionList xList = xDoc.getUdsDefinitionList();

                if (xList != null) {

                    List<UdsDefinition> xDefList = xList.getUdsDefinitionList();
                    if (xDefList != null) {
                        for (UdsDefinition xDef : xDefList) {
                            AdsDefinition def = Loader.loadFromXml(xDef, true);
                            if (def != null) {
                                getDefinitions().add(def);
                                count++;
                            }
                        }
                    }
                }
            } catch (XmlException ex) {
                try {
                    AdsUserFuncDefinitionDocument xDoc = AdsUserFuncDefinitionDocument.Factory.parse(fromFile);
                    AdsUserFuncDefinitionDocument.AdsUserFuncDefinition xDef = xDoc.getAdsUserFuncDefinition();
                    if (xDef != null && xDef.getClassId() != null && xDef.getMethodId() != null && xDef.getOwnerClassId() != null && xDef.getPropId() != null && xDef.getName() != null) {
                        AdsDefinition def = UdsUserFuncDef.Factory.loadFrom(xDef, true);
                        if (def != null) {
                            if (getDefinitions().findById(def.getId()) != null) {
                                duplicates.add(getDefinitions().findById(def.getId()));
                                return 0;
                            }
                            getDefinitions().add(def);
                            return 1;
                        }
                    }
                } catch (XmlException ex1) {
                    //not a user func. it is possibly user report
                    try {
                        AdsUserReportExchangeDocument xDoc = AdsUserReportExchangeDocument.Factory.parse(fromFile);
                        UserReportExchangeType xDefs = xDoc.getAdsUserReportExchange();
                        for (UserReportDefinitionType xReportBundle : xDefs.getAdsUserReportDefinitionList()) {
                            ClassDefinition xReport = xReportBundle.getReport();
                            if (xReport != null) {
                                AdsReportClassDef report = AdsReportClassDef.Factory.loadFrom(xReport);
                                if (getDefinitions().findById(report.getId()) != null) {
                                    duplicates.add(getDefinitions().findById(report.getId()));
                                    continue;
                                }
                                getDefinitions().add(report);

                                if (xReportBundle.getStrings() != null) {
                                    AdsLocalizingBundleDef bundle = report.findLocalizingBundle();
                                    if (bundle != null) {
                                        for (LocalizedString xStr : xReportBundle.getStrings().getStringList()) {
                                            AdsMultilingualStringDef string = AdsMultilingualStringDef.Factory.loadFrom(xStr);
                                            bundle.getStrings().getLocal().add(string);
                                        }
                                    }
                                    bundle.save();
                                    getDefinitions().reload(report);
                                }
                                count++;
                            }
                        }
                    } catch (XmlException e) {
                        throw new IOException("Invalid input file format", e);
                    }
                }
            }
            return count;
        } finally {
            isImportInProgress = false;
            enableAutoUpload(true);
            ((org.radixware.kernel.common.defs.uds.module.ModuleDefinitions) getDefinitions()).enableAutoDependences(false);
            importLock.unlock();
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        getDefinitions().visit(visitor, provider);
    }

    @Override
    public String getTypeTitle() {
        return "UDS Module";
    }

    @Override
    public String getTypesTitle() {
        return "UDS Modules";
    }

    private static class UdsModuleDependences extends Dependences {

        private boolean rebuild = true;

        @Override
        public boolean actualize() {
            return true;
        }

        @Override
        protected boolean shouldBeSaved() {
            return false;
        }

        public UdsModuleDependences(Module owner) {
            super(owner);
        }

        @Override
        public List<Module> findModuleById(Id moduleId) {
            rebuild();
            return super.findModuleById(moduleId);
        }

        @Override
        public void clear() {
            super.clear();
            rebuild = true;
        }

        @Override
        public Set<Id> getModuleIds() {
            rebuild();
            return super.getModuleIds();
        }

        @Override
        public Iterator iterator() {
            rebuild();
            return super.iterator();
        }

        @Override
        public List<Dependences.Dependence> list() {
            rebuild();
            return super.list();
        }

        private void invalidate() {
            rebuild = true;
        }
        private long lastKnownModulesState = 0;

        private boolean shouldRebuild() {
            if (rebuild) {
                return true;
            } else {
                if (lastKnownModulesState != Modules.modificationStamp) {
                    long diff = Math.abs(lastKnownModulesState - Modules.modificationStamp);
                    if (diff > 60000) {
                        return true;
                    }
                }
                return false;
            }
        }

        private void rebuild() {
            if (shouldRebuild()) {
                try {
                    Layer l = getModule().getLayer();
                    Layer.HierarchyWalker w = new Layer.HierarchyWalker();
                    final Id selfId = getModule().getId();
                    final Map<Id, Module> modules = new HashMap<>();
                    w.go(l, new Layer.HierarchyWalker.Acceptor<Module>() {
                        @Override
                        public void accept(org.radixware.kernel.common.defs.HierarchyWalker.Controller<Module> controller, Layer radixObject) {
                            for (ERepositorySegmentType st : new ERepositorySegmentType[]{ERepositorySegmentType.ADS, ERepositorySegmentType.DDS}) {
                                Segment s = radixObject.getSegmentByType(st);
                                Modules<Module> sms = s.getModules();
                                for (Module m : sms.list()) {
                                    if (m.getId() == selfId) {
                                        continue;
                                    }
                                    if (!modules.containsKey(m.getId())) {
                                        modules.put(m.getId(), m);
                                        addSilent(m);
                                    }
                                }
                            }
                        }
                    });
                } finally {
                    rebuild = false;
                    lastKnownModulesState = Modules.modificationStamp;
                }
            }
        }
    }

    public void invalidateDependences() {
        ((UdsModuleDependences) getDependences()).invalidate();
    }

    @Override
    public Dependences createDependences() {
        return new UdsModuleDependences(this);
    }
}
