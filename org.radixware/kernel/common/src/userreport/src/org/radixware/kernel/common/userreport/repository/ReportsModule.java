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

package org.radixware.kernel.common.userreport.repository;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Dependences;
import org.radixware.kernel.common.defs.HierarchyWalker.Controller;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.AdsLibUserFuncWrapper;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.module.ModuleImages;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.defs.ads.userfunc.IUserDefModule;
import org.radixware.kernel.common.defs.ads.userfunc.UdsObserver;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Modules;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsDefinition;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.repository.UserReport.ReportVersion;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument;
import org.radixware.schemas.adsdef.MethodDefinition;
import org.radixware.schemas.adsdef.UserFuncProfile;


public class ReportsModule extends AdsModule implements IUserDefModule{  
    private final Map<Id, AdsDefinition> knownWrappers = new HashMap<>();
    
    public void resetKnownLibFuncs() {
        List<Id> ids = new LinkedList<>();
        for (AdsDefinition def : knownWrappers.values()) {
            if (def instanceof AdsLibUserFuncWrapper) {
                ids.add(def.getId());
                def.delete();
            }
        }
        for (Id id : ids) {
            knownWrappers.remove(id);
        }
    }
    
    @Override
    public AdsDefinition findDefWrapper(Id id) {
        if (id.getPrefix() == EDefinitionIdPrefix.LIB_USERFUNC_PREFIX) {
                AdsDefinition wrapperClass = knownWrappers.get(id);
                if (wrapperClass == null) {
                    if (getObserver() != null) {
                        AdsUserFuncDefinitionDocument.AdsUserFuncDefinition xDef = getObserver().getReportRequestor().getLibUserFuncXml(id);
                        MethodDefinition xMethod = MethodDefinition.Factory.newInstance();
                        if (xDef != null) {
                            UserFuncProfile xProfile = xDef.getUserFuncProfile();
                            final String libName;
                            if (xProfile != null) {
                                if (xProfile.isSetParameters()) {
                                    xMethod.addNewParameters().set(xProfile.getParameters());
                                }
                                if (xProfile.isSetReturnType()) {
                                    xMethod.addNewReturnType().set(xProfile.getReturnType());
                                }
                                if (xProfile.isSetThrownExceptions()) {
                                    xMethod.addNewThrownExceptions().set(xProfile.getThrownExceptions());
                                }

                                xMethod.setNature(EMethodNature.USER_DEFINED);
                                xMethod.setId(id);
                                xMethod.setName(xProfile.getMethodName());
                                libName = xProfile.getLibName();

                            } else {
                                AdsMethodDef meth = AdsUserFuncDef.Lookup.findMethod(getBranch(), xDef.getClassId(), xDef.getMethodId());
                                if (meth != null) {
                                    meth.appendTo(xMethod, ESaveMode.NORMAL);
                                }
                                xMethod.setId(id);
                                libName = "";
                            }

                            wrapperClass = new AdsLibUserFuncWrapper(xMethod, libName);
                            wrapperClass.setPublished(true);
                            knownWrappers.put(id, wrapperClass);
                            AdsDefinition def = this.getDefinitions().findById(id);
                            if (def != null) {
                                this.getDefinitions().remove(def);
                            }
                            this.getDefinitions().add(wrapperClass);
                        }
                    }
                }
                return wrapperClass;
            }
        return null;
    }

    private static class SharedDependences extends Dependences {

        private boolean rebuild = true;

        public SharedDependences(Module owner) {
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
        public List<Dependence> list() {
            rebuild();
            return super.list();
        }

        private void invalidate() {
            rebuild = true;
        }

        private void rebuild() {
            if (rebuild) {
                try {
                    Layer l = getModule().getLayer();
                    Layer.HierarchyWalker w = new Layer.HierarchyWalker();
                    final Id selfId = getModule().getId();
                    final Map<Id, Module> modules = new HashMap<>();
                    w.go(l, new Layer.HierarchyWalker.Acceptor<Module>() {
                        @Override
                        public void accept(Controller<Module> controller, Layer radixObject) {
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
                }
            }
        }
    }

    public void invalidateDependences() {
        ((SharedDependences) getDependences()).invalidate();
    }

    public ReportsModule(Id id, String name) {
        super(id, name);
//        initDeps();
    }

    public ReportsModule(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.MODULE), name);      
        //   initDeps();
    }

    public void removeFromDb() {
        ReportsModuleRepository rep = (ReportsModuleRepository) getRepository();
        if (rep != null) {
            if (delete()) {
                rep.deleteModule();
            }
        }
    }
//    private void initDeps() {
//        Dependences deps = getDependences();
//        Dependences.Dependence added = deps.new Dependence(Id.Factory.loadFrom("mdlTWAKCFNR3NHWLFJPOS2JANM4GM"), ERepositorySegmentType.ADS);
//        deps.add(added);
//    }

    @Override
    public Dependences createDependences() {
        return new SharedDependences(this);
    }

    public void preloadHeaders() {
        if (getRepository() != null) {
            ((ReportsModuleRepository) getRepository()).upload();
        }
    }

    public List<UserReport> getReports() {
        preloadHeaders();
        return UserExtensionManagerCommon.getInstance().getUserReports().listReports(getId());
    }

    @Override
    public void saveDescription() throws IOException {
        try {
            getDependences().actualize();
            super.saveDescription();
            ((ReportsModuleRepository) getRepository()).udpateRepository(this);
        } finally {
            ((SharedDependences) getDependences()).invalidate();

        }
    }

    @Override
    protected ModuleDefinitions createDefinitinosList() {
        ModuleDefinitions defs = new ReportsModuleDefinitions(this, true);
        //Actualize reports name providers.
        List<UserReport> reportsByModule = UserExtensionManagerCommon.getInstance().getUserReports().listReports(this.getId());
        if (reportsByModule != null) {
            for (UserReport report : reportsByModule) {
                for (AdsDefinition def : defs) {
                    if (def.getId() == report.getId()) {
                        def.setNameProvider(report.getVersions().getCurrent());
                    }
                }
            }
        }
        //doCreateImages();
        return defs;
    }
    
     @Override
    public UdsObserver getObserver() {
        return UserExtensionManagerCommon.getInstance().getObserver();
    }

   // @Override
   // public ClipboardSupport<? extends AdsModule> getClipboardSupport() {
    //    return new ReportsModuleClipboardSupport();
    //}

    private class ReportsModuleDefinitions extends ModuleDefinitions {

        private final ReentrantLock loadFindLock = new ReentrantLock();

        public ReportsModuleDefinitions(AdsModule owner, boolean upload) {
            super(owner, upload);
        }

        @Override
        protected boolean checkStringLoadFailures() {
            return false;
        }

        @Override
        public AdsDefinition reload(AdsDefinition oldDef) {
            try {
                loadFindLock.lock();

                if (oldDef instanceof AdsUserReportClassDef) {
                    final long reportVersion[] = new long[1];
                    final Id reportId[] = new Id[1];
                    if (UserReport.ReportVersion.parseReportVersion(oldDef.getId(), reportId, reportVersion)) {
                        UserReport report = UserExtensionManagerCommon.getInstance().getUserReports().findReportById(reportId[0]);
                        if (report != null) {
                            if (reportVersion[0] == -1) {
                                reportVersion[0] = report.getVersions().getCurrent().getVersion();
                            }
                            ReportVersion version = report.getVersions().get(reportVersion[0]);
                            if (version != null) {
                                version.reload();
                                return version.findReportDefinition(true);
                            }
                        }
                    }
                }
                return null;
            } finally {
                loadFindLock.unlock();
            }
        }

        @Override
        public void save(AdsDefinition def, ESaveMode saveMode) throws IOException {
            super.save(def, saveMode);
            if (def instanceof AdsReportClassDef || def instanceof AdsLocalizingBundleDef) {
                Id id = def.getId();
                Id[] reportId = new Id[]{null};
                long[] version = new long[]{0};
                if (UserReport.ReportVersion.parseReportVersion(id, reportId, version)) {
                    UserReport report = UserExtensionManagerCommon.getInstance().getUserReports().findReportById(reportId[0]);
                    if (report != null) {
                        UserReport.ReportVersion versionObj;
                        if (version[0] == -1) {
                            versionObj = report.getVersions().getCurrent();
                        } else {
                            versionObj = report.getVersions().get(version[0]);
                        }
                        if (versionObj != null) {
                            versionObj.save();
                        }
                    }
                }
            }
        }

        @Override
        protected void onAdd(AdsDefinition def) {
            super.onAdd(def);

        }

        @Override
        public AdsDefinition findById(Id id) {
            try {
                loadFindLock.lock();
                AdsDefinition def = super.findById(id);
                if (def != null) {
                    return def;
                } else {
                    final Id reportId[] = new Id[1];
                    final long reportVersion[] = new long[1];                    
                    if (UserReport.ReportVersion.parseReportVersion(id, reportId, reportVersion)) {
                        UserReport report = UserExtensionManagerCommon.getInstance().getUserReports().findReportById(reportId[0]);
                        if (report != null && report.getModuleId() == getModule().getId()) {
                            if (reportVersion[0] == -1) {
                                reportVersion[0] = report.getVersions().getCurrent().getVersion();
                            } else {
                                if (reportVersion[0] == report.getVersions().getCurrent().getVersion()) {
                                    def = super.findById(report.getId());
                                    if (def != null) {
                                        return def;
                                    }
                                }
                            }

                            File defFile = ((ReportsModuleRepository) getModule().getRepository()).loadAdditionalVersion(report, reportVersion[0]);
                            if (defFile != null) {
                                try {
                                    addFromRepository(new FSRepositoryAdsDefinition(defFile));
                                } catch (final IOException ex) {
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Exceptions.printStackTrace(ex);
                                            UserExtensionManagerCommon.getInstance().getUFRequestExecutor().processException(ex);
                                        }
                                    });

                                }
                                def = super.findById(id); 
                                
                                return def;
                            }
                        }
                    }
                    if(id.getPrefix()==EDefinitionIdPrefix.LIB_USERFUNC_PREFIX){
                        return findDefWrapper(id);
                    }
                    return null;
                }
            } finally {
                loadFindLock.unlock();
            }
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(List<ClipboardSupport.Transfer> transfers, ClipboardSupport.DuplicationResolver resolver) {
            if (super.canPaste(transfers, resolver) != ClipboardSupport.CanPasteResult.YES) {
                return ClipboardSupport.CanPasteResult.NO;
            }

            for (ClipboardSupport.Transfer transfer : transfers) {
                if (canPaste(transfer, resolver) != ClipboardSupport.CanPasteResult.YES) {
                    return ClipboardSupport.CanPasteResult.NO;
                }
            }
            return ClipboardSupport.CanPasteResult.YES;
        }

        private ClipboardSupport.CanPasteResult canPaste(ClipboardSupport.Transfer objectInClipboard, ClipboardSupport.DuplicationResolver resolver) {
            RadixObject object = objectInClipboard.getObject();
            if (object instanceof AdsReportClassDef) {
                return ClipboardSupport.CanPasteResult.YES;
            } else {
                return ClipboardSupport.CanPasteResult.NO;
            }
        }
    }

    public boolean create() {
       return UserExtensionManagerCommon.getInstance().getUserReportManager().createReportModule( getSegment().getRepository(), this);
       //return repository != null;
    }

    @Override
    public boolean isUserModule() {
        return true;
    }
    private RadixObjects.ContainerChangesListener listener = new RadixObjects.ContainerChangesListener() {
        @Override
        public void onEvent(RadixObjects.ContainerChangedEvent e) {
            ReportsModule.this.setEditState(EEditState.MODIFIED);
        }
    };
    
    private ModuleImages doCreateImages() {
        return createImages();
    }

    @Override
    protected ModuleImages createImages() {
        ModuleImages images = super.createImages();
        images.getContainerChangesSupport().addEventListener(listener);
        return images;
    }

    /*private class ReportsModuleClipboardSupport extends AdsModule.AdsModuleClipboardSupport {

        public ReportsModuleClipboardSupport() {
        }

        @Override
        public void paste(final List<Transfer> transfers, DuplicationResolver resolver) {
            if (getDefinitions().getClipboardSupport().canPaste(transfers, resolver) == CanPasteResult.YES) {
                for (ClipboardSupport.Transfer transfer : transfers) {
                    final RadixObject obj = transfer.getObject();
                    if (obj instanceof AdsReportClassDef) {
                        super.paste(Collections.singletonList(transfer), resolver);
                        UserReport.create(ReportsModule.this, (AdsReportClassDef) obj);
                    }
                }
            }
        }
    }*/
}
