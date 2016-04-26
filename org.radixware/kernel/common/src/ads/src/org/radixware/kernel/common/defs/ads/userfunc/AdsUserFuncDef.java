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
package org.radixware.kernel.common.defs.ads.userfunc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.impl.values.XmlValueOutOfRangeException;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionProblems;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsLibUserFuncWrapper;
import org.radixware.kernel.common.defs.ads.clazz.AdsWrapperClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.module.UsagesSupport;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.userfunc.AdsUserFuncWriter;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.jml.IJmlSource;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagDbEntity;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsModule;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument.AdsUserFuncDefinition;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;
import org.radixware.schemas.adsdef.MethodDefinition;
import org.radixware.schemas.adsdef.UserFuncProfile;
import org.radixware.schemas.adsdef.UserFuncSourceVersions;
import org.radixware.schemas.adsdef.UserFuncSourceVersions.SourceVersion;
import org.radixware.schemas.udsdef.UdsDefinitionDocument;
import org.radixware.schemas.udsdef.UdsDefinitionListDocument;
import org.radixware.schemas.udsdef.UserFunctionDefinition;
import org.radixware.schemas.xscml.JmlType;

public class AdsUserFuncDef extends AdsDefinition implements IJavaSource, IAdsTypeSource, IJmlSource, IUserFuncDef {

    public static final class Problems extends AdsDefinitionProblems {

        private Problems(AdsUserFuncDef prop, List warnings) {
            super(prop);
            if (warnings != null) {
                int arr[] = new int[warnings.size()];
                for (int i = 0; i < arr.length; i++) {
                    if (warnings.get(i) instanceof Integer) {
                        arr[i] = (Integer) warnings.get(i);
                    }
                }
                setSuppressedWarnings(arr);
            }
        }

        @Override
        public boolean canSuppressWarning(int code) {
            return super.canSuppressWarning(code);
        }
    }

    private class UFLocalizingBundle extends AdsLocalizingBundleDef {

        public UFLocalizingBundle(Id ownerId, LocalizingBundleDefinition xDef) {
            super(ownerId, xDef);
            setContainer(AdsUserFuncDef.this);
        }

        public UFLocalizingBundle(Id ownerId) {
            super(ownerId);
            setContainer(AdsUserFuncDef.this);
        }

        public void UpdateId() {
            setContainer(null);
            setId(AdsUserFuncDef.this.getLocalizingBundleId());
            setContainer(AdsUserFuncDef.this);
        }

        @Override
        public AdsDefinition findBundleOwner() {
            return AdsUserFuncDef.this; //To change body of generated methods, choose Tools | Templates.
        }
    }

    private final class UFJml extends Jml {

        private boolean loading = false;
        private Calendar lastUpdateTime = null;
        private String lastUpdateUserName = null;

        UFJml(RadixObject context, String name) {
            super(context, name);
        }

        UFJml(RadixObject context, String name, final String lastUpdateUserName, final Calendar lastUpdateTime) {
            super(context, name);
            this.lastUpdateUserName = lastUpdateUserName;
            this.lastUpdateTime = lastUpdateTime;
        }

        Calendar getLastUpdateTime() {
            return lastUpdateTime;
        }

        String getLastUpdateUserName() {
            return lastUpdateUserName;
        }

        @Override
        protected void onModified() {
            synchronized (this) {
                if (!loading) {
                    AdsUserFuncDef.this.touch();
                }
            }
        }

        @Override
        public void loadFrom(JmlType xJml) {
            synchronized (this) {
                try {
                    loading = true;
                    super.loadFrom(xJml);
                    for (Scml.Item item : this.getItems()) {
                        if (item instanceof JmlTagDbEntity) {
                            JmlTagDbEntity entityTag = (JmlTagDbEntity) item;
                            if (entityTag.isUFOwnerRef()) {
                                entityTag.update(AdsUserFuncDef.this.ownerClassId, AdsUserFuncDef.this.ownerPid);
                            }
                        }
                    }
                } finally {
                    loading = false;
                }
            }
        }
    }
    public static final Id UF_MODULE_ID = Id.Factory.newInstance(EDefinitionIdPrefix.MODULE);

    public static Layer getLayerForUDF(final Branch branch) {
        return branch.getLayers().findTop().get(0);
    }

    public static class UFModule extends AdsModule {

        private UFModuleRepository repository;
        private Map<Id, AdsDefinition> knownWrappers = new HashMap<>();
        private UdsObserver observer;

        UFModule(final AdsModule srcModule, UdsObserver observer) {
            this(srcModule.getSegment().getLayer().getBranch(), observer);
        }

        UFModule(final Branch branch, UdsObserver observer) {
            super(UF_MODULE_ID, "User Definitions");
            this.observer = observer;
            this.setContainer(getLayerForUDF(branch).getAds().getModules());
        }

        private void resetKnownLibFuncs() {
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

        private AdsDefinition findDefWrapper(Id id) {
            if (EDefinitionIdPrefix.USER_DEFINED_REPORT == id.getPrefix()) {
                AdsDefinition wrapperClass = knownWrappers.get(id);
                if (wrapperClass == null) {
                    if (observer != null) {
                        ClassDefinition xDef = observer.getReportRequestor().getClassDefXml(id);
                        if (xDef != null) {
                            wrapperClass = new AdsWrapperClassDef(xDef);
                            this.getDefinitions().add(wrapperClass);
                            knownWrappers.put(id, wrapperClass);
                        }
                    }
                }
                return wrapperClass;
            } else if (EDefinitionIdPrefix.APPLICATION_ROLE == id.getPrefix()) {
                AdsDefinition wrapperClass = knownWrappers.get(id);
                if (wrapperClass == null) {
                    if (observer != null) {
                        List<Lookup.DefInfo> infos = new LinkedList<>();
                        observer.addUserDefInfo(EnumSet.of(EDefType.ROLE), infos);
                        for (Lookup.DefInfo info : infos) {
                            if (info.path.length == 1 && info.path[0] == id) {
                                wrapperClass = AdsRoleDef.Factory.newInstance(id, info.name);
                                this.getDefinitions().add(wrapperClass);
                                knownWrappers.put(id, wrapperClass);
                                break;
                            }
                        }
                    }
                }
                return wrapperClass;
            } else if (id.getPrefix() == EDefinitionIdPrefix.LIB_USERFUNC_PREFIX) {
                AdsDefinition wrapperClass = knownWrappers.get(id);
                if (wrapperClass == null) {
                    if (observer != null) {
                        AdsUserFuncDefinition xDef = observer.getReportRequestor().getLibUserFuncXml(id);
                        MethodDefinition xMethod = MethodDefinition.Factory.newInstance();
                        if (xDef != null) {
                            UserFuncProfile xProfile = xDef.getUserFuncProfile();
                            final String libName;
                            boolean isDefined = false;
                            if (xProfile != null) {

                                if (xProfile.isSetParameters()) {
                                    isDefined = true;
                                    xMethod.addNewParameters().set(xProfile.getParameters());
                                }
                                if (xProfile.isSetReturnType()) {
                                    isDefined = true;
                                    xMethod.addNewReturnType().set(xProfile.getReturnType());
                                }
                                if (xProfile.isSetThrownExceptions()) {
                                    isDefined = true;
                                    xMethod.addNewThrownExceptions().set(xProfile.getThrownExceptions());
                                }

                                xMethod.setNature(EMethodNature.USER_DEFINED);
                                xMethod.setId(id);
                                xMethod.setName(xProfile.getMethodName());
                                libName = xProfile.getLibName();
                            } else {
                                libName = "";
                            }
                            if (!isDefined) {
                                AdsMethodDef meth = Lookup.findMethod(getBranch(), xDef.getClassId(), xDef.getMethodId());
                                if (meth != null) {
                                    meth.appendTo(xMethod, ESaveMode.NORMAL);
                                    if (xProfile != null) {
                                        xMethod.setNature(EMethodNature.USER_DEFINED);
                                        xMethod.setName(xProfile.getMethodName());
                                    }

                                }
                                xMethod.setId(id);
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

        private void close() {
            synchronized (this) {
                //observer = null;
                if (repository != null) {
                    repository.close();
                    repository = null;
                }
            }
        }

        @Override
        protected boolean neverUpload() {
            return true;
        }

        @Override
        public IRepositoryAdsModule getRepository() {
            synchronized (this) {
                if (repository == null) {
                    repository = new UFModuleRepository();
                }
                return repository;
            }
        }

        @Override
        protected ModuleDefinitions createDefinitinosList() {
            return new UFModuleDefinitions(this, false); //To change body of generated methods, choose Tools | Templates.
        }

        private class UFModuleDefinitions extends ModuleDefinitions {

            public UFModuleDefinitions(AdsModule owner, boolean upload) {
                super(owner, upload);
            }

            @Override
            public AdsDefinition findById(Id id) {
                AdsDefinition result = super.findById(id); //To change body of generated methods, choose Tools | Templates.
                if (result != null) {
                    return result;
                } else {
                    return findDefWrapper(id);
                }
            }
        }
    }

    @Override
    public Jml getSource(final String name) {
        if (name != null && sourceVersions.containsKey(name)) {
            return sourceVersions.get(name);
        }
        return source;
    }

    public LibFuncNameGetter getLibName() {
        return libFuncName;
    }

    public static class UserFuncType extends AdsDefinitionType {

        public UserFuncType(final IUserFuncDef def) {
            super((Definition) def);
        }

        @Override
        public TypeJavaSourceSupport getJavaSourceSupport() {
            return new TypeJavaSourceSupport(this) {
                @Override
                public char[][] getPackageNameComponents(final UsagePurpose purpose) {
                    return JavaSourceSupport.getPackageNameComponents(source, purpose);
                }

                @Override
                public char[] getLocalTypeName(final UsagePurpose purpose) {
                    return source.getId().toCharArray();
                }
            };
        }

        @Override
        protected void check(final RadixObject referenceContext, final ERuntimeEnvironmentType environment, final IProblemHandler problemHandler) {
            super.check(referenceContext, environment, problemHandler);
        }
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.SERVER);
    }

    @Override
    public AdsType getType(final EValType typeId, final String extStr) {
        return new UserFuncType(this);
    }

    public static final class Lookup {

        public static interface IDefInfoFilter {

            boolean isTarget(DefInfo item);
        }

        public static class DefInfo {

            @Override
            public int hashCode() {
                int hash = 7;
                hash = 67 * hash + Objects.hashCode(this.path);
                return hash;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final DefInfo other = (DefInfo) obj;
                return Arrays.equals(this.path, other.path);
            }
            private final Id[] path;
            private final String name;
            private String moduleName;
            private final ERuntimeEnvironmentType env;
            private final boolean isDeprecated;
            private final Id moduleId;
            private AdsDefinition definition;

            public DefInfo(final Id id, final String name, final String moduleName, boolean isDepricated, final ERuntimeEnvironmentType env, AdsDefinition definition) {
                this(id, name, moduleName, null, isDepricated, env, definition);
            }

            public DefInfo(final Id id, final String name, final String moduleName, Id moduleId, boolean isDepricated, final ERuntimeEnvironmentType env, AdsDefinition definition) {
                this(new Id[]{id}, name, moduleName, moduleId, isDepricated, env, definition);
            }

            public DefInfo(final Id[] id, final String name, final String moduleName, Id moduleId, boolean isDepricated, final ERuntimeEnvironmentType env, AdsDefinition definition) {
                this.path = id;
                this.name = name;
                this.moduleName = moduleName;
                this.env = env;
                this.isDeprecated = isDepricated;
                this.moduleId = moduleId;
                this.definition = definition;
            }

            public Id[] getPath() {
                return path;
            }

            public AdsDefinition getDefinition() {
                return definition;
            }

            public Id getModuleId() {
                return moduleId;
            }

            public String getName() {
                return name;
            }

            public String getModuleName() {
                return moduleName;
            }

            public boolean isDeprecated() {
                return isDeprecated;
            }

            public ERuntimeEnvironmentType getEnvironment() {
                return env;
            }
        }
        private static WeakHashMap<Branch, Lookup> lookups = null;
        //private final Map<Id, UFModule> ufModules = new HashMap<Id, UFModule>(5);
        private volatile UFModule module = null;
        private volatile List<Id> knownAdsModuleIds = null;
        private volatile List<Id> knownDdsModuleIds = null;

        @Override
        public void finalize() throws Throwable {
            synchronized (this) {
                if (module != null) {
                    module.close();
                }
            }
            super.finalize();
        }

        public static void clearCaches() {
            synchronized (Lookup.class) {
                if (lookups != null) {
                    lookups.clear();
                    lookups = null;
                }
            }
        }

        public static UFModule createTempModule(Branch branch, UdsObserver observer) {
            final Lookup lookup = findLookup(branch);
            synchronized (lookup) {
                lookup.scanModules(branch, null);
                final UFModule module = createModule(branch, lookup, observer);
                module.observer = observer;

                return module;
            }
        }

        private UFModule findModule(final AdsModule src, UdsObserver observer) {
            synchronized (this) {
                if (module == null) {
                    module = createModule(src.getSegment().getLayer().getBranch(), this, observer);
                    knownAdsModuleIds = Collections.emptyList();
                    knownDdsModuleIds = Collections.emptyList();
                }
                module.observer = observer;

                return module;
            }
        }

        //Use synchronized by lookup
        private static UFModule createModule(Branch branch, Lookup lookup, UdsObserver observer) {
            final UFModule module = new UFModule(branch, observer);
            //ufModules.put(src.getId(), ufModule);
            final Dependences deps = module.getDependences();

            if (lookup.knownAdsModuleIds != null) {
                for (Id id : lookup.knownAdsModuleIds) {
                    final Dependences.Dependence dep = deps.new Dependence(id, ERepositorySegmentType.ADS, false);
                    deps.add(dep);
                }
            }
            if (lookup.knownDdsModuleIds != null) {
                for (Id id : lookup.knownDdsModuleIds) {
                    final Dependences.Dependence dep = deps.new Dependence(id, ERepositorySegmentType.DDS, false);
                    deps.add(dep);
                }
            }

            return module;
        }

        private AdsEntityObjectClassDef scanModules(final Branch branch, final Id classGuid) {
            synchronized (this) {
                AdsEntityObjectClassDef clazz = null;
                final boolean updateModules = knownAdsModuleIds == null || knownDdsModuleIds == null;
                if (updateModules) {
                    knownAdsModuleIds = new LinkedList<>();
                    knownDdsModuleIds = new LinkedList<>();
                }

                for (Layer top : branch.getLayers().getTopsFirst()) {
                    if ("user.extensions".equals(top.getURI())) {
                        Logger.getLogger(AdsUserFuncDef.class.getCanonicalName()).warning("User extensions layer found in branch layers list.");
                        continue;
                    }
                    for (Module m : top.getAds().getModules()) {
                        if (updateModules) {
                            knownAdsModuleIds.add(m.getId());
                        }
                        if (classGuid == null) {
                            continue;
                        }
                        final IRepositoryAdsModule rep = ((AdsModule) m).getRepository();
                        if (clazz == null) {
                            if (rep != null && rep.containsDefinition(classGuid)) {
                                final AdsDefinition candidate = ((AdsModule) m).getDefinitions().findById(classGuid);
                                if (candidate instanceof AdsEntityObjectClassDef) {
                                    clazz = (AdsEntityObjectClassDef) candidate;
                                    if (!updateModules) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (updateModules) {
                        for (Module m : top.getDds().getModules()) {
                            knownDdsModuleIds.add(m.getId());
                        }
                    }
                }
                return clazz;
            }
        }

        private static Lookup findLookup(Branch branch) {
            synchronized (Lookup.class) {
                if (lookups == null) {
                    lookups = new WeakHashMap<>(2);
                }
                Lookup lookup = lookups.get(branch);
                if (lookup == null) {
                    lookup = new Lookup();
                    lookups.put(branch, lookup);
                }
                return lookup;
            }
        }

        public static AdsUserFuncDef lookup(Branch branch, final Id classGuid, final Id methodId, Id userClassGuid, Id ownerClassId, String ownerPid, LibFuncNameGetter libFuncName, AdsUserFuncDefinition xDef, UserFuncSourceVersions xSrcVersions, UdsObserver observer) {
            Lookup lookup = findLookup(branch);
            AdsEntityObjectClassDef clazz = lookup.scanModules(branch, classGuid);//user func in ads

            if (clazz != null) {
                return new AdsUserFuncDef(clazz, methodId, userClassGuid, ownerClassId, ownerPid, xDef, xSrcVersions, lookup.findModule(clazz.getModule(), observer), libFuncName);
            } else {
                return null;
            }
        }

        public static AdsMethodDef findMethod(Branch branch, final Id classId, final Id methodId) {
            Lookup lookup = findLookup(branch);
            AdsEntityObjectClassDef clazz = lookup.scanModules(branch, classId);

            if (clazz != null) {
                return clazz.getMethods().findById(methodId, EScope.ALL).get();
            } else {
                return null;
            }
        }

        private static void addInnerClasses(AdsClassDef clazz, List<DefInfo> defInfoList, String moduleName, RadixObject root) {
            for (AdsClassDef inner : clazz.getNestedClasses().get(EScope.LOCAL_AND_OVERWRITE)) {
                if (inner.isPublished() && inner.getAccessMode() == EAccess.PUBLIC) {
                    DefInfo defInfo = new DefInfo(inner.getIdPath(), inner.getQualifiedName(root), moduleName, null, inner.isDeprecated(), inner.getUsageEnvironment(), inner);
                    defInfoList.add(defInfo);
                    addInnerClasses(inner, defInfoList, moduleName, root);
                }
            }
        }

        public static Collection<DefInfo> listTopLevelDefinitions(AdsUserFuncDef context, final Set<EDefType> defTypes) {
            Layer top = context.getModule().getSegment().getLayer();
            final ArrayList<DefInfo> infos = new ArrayList<>();

            Layer.HierarchyWalker.walk(top, new Layer.HierarchyWalker.Acceptor<Object>() {
                @Override
                public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                    for (Module module : layer.getAds().getModules()) {
                        IRepositoryAdsModule rep = ((AdsModule) module).getRepository();
                        IRepositoryAdsDefinition[] defs = rep.listDefinitions();
                        if (defs != null) {
                            for (int i = 0; i < defs.length; i++) {
                                IRepositoryAdsDefinition def = defs[i];
                                if (!def.isPublished() || def.getAccessMode() != EAccess.PUBLIC) {
                                    continue;
                                }
                                ERuntimeEnvironmentType env = def.getEnvironment();
                                boolean isDepricated = false;
                                if (def.getPreLoadedDefinition() != null) {
                                    isDepricated = def.getPreLoadedDefinition().isDeprecated();
                                }
                                DefInfo defInfo = new DefInfo(def.getId(), def.getName(), module.getQualifiedName(), isDepricated, env, def.getPreLoadedDefinition());
                                if (defTypes.contains(def.getType()) && !infos.contains(defInfo)) {
                                    infos.add(defInfo);
                                }
                                if (defTypes.contains(EDefType.CLASS)) {
                                    if (def.getPreLoadedDefinition() instanceof AdsClassDef) {
                                        AdsClassDef clazz = (AdsClassDef) def.getPreLoadedDefinition();
                                        addInnerClasses(clazz, infos, module.getQualifiedName(), clazz);
                                    }
                                }
                            }
                        }
                    }
                }
            });

            if (((UFModule) context.getModule()).observer != null) {
                ((UFModule) context.getModule()).observer.addUserDefInfo(defTypes, infos);
            }
            for (DefInfo info : infos) {
                if (info.moduleName == null || info.moduleName.isEmpty()) {
                    info.moduleName = context.getModule().getName();
                }
            }

            return infos;
        }

        public static AdsDefinition findTopLevelDefinition(AdsUserFuncDef context, final Id id) {
            if (context.getModule() instanceof UFModule) {
                AdsDefinition wrapper = ((UFModule) context.getModule()).findDefWrapper(id);
                if (wrapper != null) {
                    return wrapper;
                }
            }

            Layer top = context.getModule().getSegment().getLayer();
            //ArrayList<DefInfo> infos = new ArrayList<DefInfo>();
            return Layer.HierarchyWalker.walk(top, new Layer.HierarchyWalker.Acceptor<AdsDefinition>() {
                @Override
                public void accept(HierarchyWalker.Controller<AdsDefinition> controller, Layer l) {
                    for (Module module : l.getAds().getModules()) {
                        IRepositoryAdsModule rep = ((AdsModule) module).getRepository();
                        if (rep.containsDefinition(id)) {
                            controller.setResultAndPathStop(((AdsModule) module).getDefinitions().findById(id));
                            break;
                        }
                    }
                }
            });
        }
    }
    private final UFJml source;
    private final AdsClassDef clazz;
    private Id methodId;
    private final Map<String, Jml> sourceVersions;
    private Problems warningsSupport = null;
    private UFLocalizingBundle strings;
    private final Id ownerClassId;
    private final String ownerPid;
    private AdsMethodDef method = null;
    private boolean isFreeForm = false;
    private LibFuncNameGetter libFuncName;

    private static final Id FREE_FORM_CLASS_ID = Id.Factory.loadFrom("aclO6J5TMLZSRD3NJRWZUT7F3EL5Y");

    public interface LibFuncNameGetter {

        String getLibFuncName();
    }

    private AdsUserFuncDef(AdsClassDef clazz, Id methodId, Id userClassGuid, Id ownerEntityId, String ownerPid, AdsUserFuncDefinition xDef, UserFuncSourceVersions xSrcVersions, UFModule module, LibFuncNameGetter libFuncName) {
        super(userClassGuid, "");
        setContainer(module);
        module.resetKnownLibFuncs();
        this.ownerClassId = ownerEntityId;
        this.ownerPid = ownerPid;
        this.clazz = clazz;
        this.libFuncName = libFuncName;
        if (xDef != null) {
            Calendar lastUpdateTime;
            try {
                lastUpdateTime = xDef.getLastUpdateTime();
            } catch (XmlValueOutOfRangeException e) {
                lastUpdateTime = Calendar.getInstance();
            }
            String lastUpdateUserName = xDef.getLastUpdateUserName();

            this.source = new UFJml(this, "source", lastUpdateUserName, lastUpdateTime);
            this.source.loadFrom(xDef.getSource());

            if (xDef.isSetSuppressedWarnings()) {
                List list = xDef.getSuppressedWarnings();
                if (!list.isEmpty()) {
                    this.warningsSupport = new Problems(this, list);
                }
            }
            if (xDef.getStrings() != null) {
                this.strings = new UFLocalizingBundle(userClassGuid, xDef.getStrings());
            }
            if (methodId != null) {
                this.methodId = methodId;
            } else if (xDef.isSetMethodId()) {
                this.methodId = xDef.getMethodId();
            } else {
                this.methodId = Id.Factory.newInstance(EDefinitionIdPrefix.ADS_CLASS_METHOD);
            }

            if (/*xDef.isSetUserFuncProfile()*/clazz.getId() == FREE_FORM_CLASS_ID) {
                isFreeForm = true;
                createMethod(xDef);
            } else {
                this.method = null;
            }
        } else {
            this.source = new UFJml(this, "source");
            this.methodId = methodId;
            this.method = null;
        }
        sourceVersions = new HashMap<>();
        if (xSrcVersions != null) {
            List<SourceVersion> srcVersions = xSrcVersions.getSourceVersionList();
            for (SourceVersion srcVersion : srcVersions) {
                String lastUpdateUserName = srcVersion.isSetLastUpdateUserName()
                        ? srcVersion.getLastUpdateUserName() : null;
                Calendar lastUpdateTime = srcVersion.isSetLastUpdateTime()
                        ? srcVersion.getLastUpdateTime() : null;
                UFJml version = new UFJml(this, srcVersion.getName(), lastUpdateUserName, lastUpdateTime);
                version.loadFrom(srcVersion);
                sourceVersions.put(srcVersion.getName(), version);
            }
        }
    }

    private void createMethod(AdsUserFuncDefinition xDef) {
        method = (AdsMethodDef) getModule().getDefinitions().findById(methodId);
        if (method != null) {
            getModule().getDefinitions().remove(method);
        }
        AbstractMethodDefinition xMeth = AbstractMethodDefinition.Factory.newInstance();
        xMeth.setId(this.methodId);
        UserFuncProfile ufProfile = xDef.getUserFuncProfile();
        if (ufProfile.isSetParameters()) {
            xMeth.addNewParameters().set(ufProfile.getParameters());
            //setParameters(ufProfile.getParameters());
        }
        if (ufProfile.isSetReturnType()) {
            xMeth.addNewReturnType().set(ufProfile.getReturnType());
        }
        if (ufProfile.isSetThrownExceptions()) {
            xMeth.addNewThrownExceptions().set(ufProfile.getThrownExceptions());
        }
        xMeth.setNature(EMethodNature.USER_DEFINED);

        method = AdsMethodDef.Factory.loadFrom(xMeth);
        String methodName = ufProfile.getMethodName();
        method.setName(methodName);
        method.setPublished(true);
        getModule().getDefinitions().add(method);
        //if(clazz.getMethods().findById(this.methodId,EScope.LOCAL)==null)
        //    clazz.getMethods().getLocal().add(method);
    }

    public boolean isFreeForm() {
        return isFreeForm;
    }

    public void touch() {
        RadixObject obj = getContainer();
        setContainer(null);
        setId(Id.Factory.newInstance(EDefinitionIdPrefix.USER_FUNC_CLASS));
        setContainer(obj);

        if (strings != null) {
            strings.UpdateId();
        }
    }

    public Id getOwnerClassId() {
        return ownerClassId;
    }

    public String getOwnerPid() {
        return ownerPid;
    }

    public Map<String, Jml> getSourceVersions() {
        return sourceVersions;
    }

    public Calendar getVersionUpdateTime(String name) {
        if (name == null) {
            return source.getLastUpdateTime();
        } else {
            UFJml version = (UFJml) sourceVersions.get(name);
            if (version != null) {
                return version.getLastUpdateTime();
            }
        }
        return null;
    }

    public String getVersionUserName(String name) {
        if (name == null) {
            return source.getLastUpdateUserName();
        } else {
            UFJml version = (UFJml) sourceVersions.get(name);
            if (version != null) {
                return version.getLastUpdateUserName();
            }
        }
        return null;
    }

    public boolean addSourceVersion(String name, String userName, Jml source) {
        if (name != null) {
            UFJml newSource;
            newSource = new UFJml(this, name, userName, Calendar.getInstance());
            if (source != null) {
                JmlType jmlType = JmlType.Factory.newInstance();
                source.appendTo(jmlType, ESaveMode.NORMAL);
                newSource.loadFrom(jmlType);
                newSource.setName(name);
            }
            sourceVersions.put(name, newSource);
            return true;
        }
        return false;
    }

    @Override
    public AdsMethodDef findMethod() {
        if (method != null) {
            return method;
        }
        if (methodId == null) {
            return null;
        }
        return clazz.getMethods().findById(methodId, EScope.ALL).get();
    }

    public AdsMethodDef.Profile findProfile() {
        if (this.method != null) {
            return this.method.getProfile();
        } else {
            AdsMethodDef m = findMethod();
            if (m == null) {
                return null;
            } else {
                return m.getProfile();
            }
        }
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.USER_FUNC;
    }

    @Override
    public Jml getSource() {
        return source;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        AdsMethodDef method = findMethod();
        if (method != null) {
            list.add(method);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        source.visit(visitor, provider);
        if (strings != null) {
            strings.visit(visitor, provider);
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(final UsagePurpose purpose) {
                return new AdsUserFuncWriter(this, AdsUserFuncDef.this, purpose);
            }

            @Override
            public EnumSet<ERuntimeEnvironmentType> getSupportedEnvironments() {
                return EnumSet.of(ERuntimeEnvironmentType.SERVER);
            }

            @Override
            public Set<CodeType> getSeparateFileTypes(final ERuntimeEnvironmentType sc) {
                return EnumSet.of(CodeType.EXCUTABLE);
            }
        };
    }

    public void check(final IProblemHandler problemHandler) {
        final AdsMethodDef method = findMethod();
        if (method == null) {
            problemHandler.accept(RadixProblem.Factory.newError(this, "Method #" + methodId != null ? methodId.toString() : "null" + " is not found in class " + clazz.getQualifiedName()));
        }
        getSource().check(problemHandler, null);
        //source.check(problemHandler, null);
    }

    public void appendTo(AdsUserFuncDefinition xDef) {
        xDef.setId(getId());
        if (clazz != null) {
            xDef.setClassId(clazz.getId());
        }
        //if(!isFreeForm)
        xDef.setMethodId(methodId);
        if (isFreeForm) {//(method!=null){
            MethodDefinition xMeth = MethodDefinition.Factory.newInstance();
            AdsMethodDef m = findMethod();
            m.appendTo(xMeth, ESaveMode.NORMAL);
            UserFuncProfile ufProfile = xDef.addNewUserFuncProfile();
            ufProfile.addNewParameters().set(xMeth.getParameters());
            ufProfile.addNewReturnType().set(xMeth.getReturnType());
            ufProfile.addNewThrownExceptions().set(xMeth.getThrownExceptions());
            ufProfile.setMethodName(m.getName());
        }
        xDef.setLastUpdateTime(source.getLastUpdateTime());
        xDef.setLastUpdateUserName(source.getLastUpdateUserName());

        if (warningsSupport != null && !warningsSupport.isEmpty()) {
            int[] warnings = warningsSupport.getSuppressedWarnings();
            List<Integer> list = new ArrayList<>(warnings.length);
            for (int w : warnings) {
                list.add(Integer.valueOf(w));
            }
            xDef.setSuppressedWarnings(list);
        }

        if (strings != null) {
            this.strings.appendTo(xDef.addNewStrings(), ESaveMode.NORMAL);
            xDef.getStrings().setId(getLocalizingBundleId());
        }

        source.appendTo(xDef.addNewSource(), ESaveMode.NORMAL);
        UsagesSupport support = new UsagesSupport(this.getModule());
        support.saveUsages(this, xDef.addNewUsages());
    }

    @Override
    protected void insertToolTipPrefix(StringBuilder sb) {
        super.insertToolTipPrefix(sb);
        String access = getDefaultAccess().getName().toUpperCase().charAt(0) + getDefaultAccess().getName().substring(1, getDefaultAccess().getName().length());
        sb.append("<b>").append(access).append(' ').append(getUsageEnvironment().getName()).append("</b>&nbsp;");
    }

    @Override
    protected AdsLocalizingBundleDef findLocalizingBundleImpl(boolean createIfNotExists) {
        if (strings == null) {
            if (createIfNotExists) {
                strings = new UFLocalizingBundle(clazz.getId());
            }
        }
        return strings;
    }

    @Override
    public RadixProblem.WarningSuppressionSupport getWarningSuppressionSupport(boolean createIfAbsent) {
        synchronized (this) {
            if (warningsSupport == null && createIfAbsent) {
                warningsSupport = new Problems(this, null);
            }
            return warningsSupport;
        }
    }

    public void appendVersionsTo(UserFuncSourceVersions xVersions) {
        for (String name : sourceVersions.keySet()) {
            SourceVersion version = xVersions.addNewSourceVersion();
            Jml sourceVersion = sourceVersions.get(name);
            sourceVersion.appendTo(version, ESaveMode.NORMAL);
            version.setName(name);
            if (sourceVersion instanceof UFJml) {
                if (((UFJml) sourceVersion).getLastUpdateTime() != null) {
                    version.setLastUpdateTime(((UFJml) sourceVersion).getLastUpdateTime());
                }
                if (((UFJml) sourceVersion).getLastUpdateUserName() != null) {
                    version.setLastUpdateUserName(((UFJml) sourceVersion).getLastUpdateUserName());
                }
            }
        }
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return ERuntimeEnvironmentType.SERVER;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    public void resetCompileTimeCaches() {
        AdsModule module = getModule();
        if (module instanceof UFModule) {
            ((UFModule) module).close();
        }
    }

    public void appendTo(UserFunctionDefinition xDef) {

        xDef.setId(getId());
        xDef.setMethodId(methodId);
        xDef.setClassId(clazz.getId());
        source.appendTo(xDef.addNewSource(), ESaveMode.NORMAL);

    }

    public interface UICallback {

        public void noSuitableFunctionFound();

        public Id chooseId(Map<Id, UserFunctionDefinition> id2title);
    }

    public void exportBody(File file, Id ownerClassId, Id ownerPropId, String name) throws IOException {
        UdsDefinitionListDocument xDoc = UdsDefinitionListDocument.Factory.newInstance();
        UdsDefinitionListDocument.UdsDefinitionList xList = xDoc.addNewUdsDefinitionList();
        UserFunctionDefinition xDef = xList.addNewUdsDefinition().addNewUserFunc();
        xDef.setId(getId());
        xDef.setClassId(clazz.getId());
        xDef.setMethodId(methodId);
        xDef.setOwnerClassId(ownerClassId);
        xDef.setPropId(ownerPropId);
        xDef.setName(name);
        this.source.appendTo(xDef.addNewSource(), ESaveMode.NORMAL);
        XmlUtils.saveXmlPretty(xDoc, file);
    }

    public void importBody(File file, UICallback cb) throws IOException {
        try {
            UdsDefinitionListDocument xDoc = UdsDefinitionListDocument.Factory.parse(file);
            UdsDefinitionListDocument.UdsDefinitionList xList = xDoc.getUdsDefinitionList();
            if (xList != null) {
                List<UserFunctionDefinition> sutable = new LinkedList<>();
                for (UdsDefinitionDocument.UdsDefinition xDef : xList.getUdsDefinitionList()) {
                    if (xDef.getUserFunc() != null) {

                        UserFunctionDefinition xUf = xDef.getUserFunc();

                        if (xUf.getClassId() == this.clazz.getId() && xUf.getMethodId() == methodId) {
                            sutable.add(xUf);
                        }

                    }
                }
                if (sutable.isEmpty()) {
                    cb.noSuitableFunctionFound();
                } else {
                    UserFunctionDefinition xUf;
                    if (sutable.size() == 1) {
                        xUf = sutable.get(0);
                    } else {
                        Map<Id, UserFunctionDefinition> map = new HashMap<>();
                        for (UserFunctionDefinition x : sutable) {
                            map.put(x.getId(), x);
                        }
                        Id choosen = cb.chooseId(map);
                        if (choosen != null) {
                            xUf = map.get(choosen);
                        } else {
                            return;
                        }
                    }

                    Jml jml = Jml.Factory.loadFrom(this, xUf.getSource(), "src");
                    this.source.getItems().clear();
                    for (Scml.Item item : jml.getItems().list()) {
                        item.delete();
                        this.source.getItems().add(item);
                    }
                }
            }

        } catch (XmlException ex) {
            throw new IOException(ex);
        }
    }

    public List<AdsDefinition> getUsedWrappers() {
        ((UFModule) getModule()).resetKnownLibFuncs();
        Map<Id, AdsDefinition> usedWrappers = new HashMap<>();
        for (Scml.Item item : this.getSource().getItems()) {
            AdsPath path = null;
            if (item instanceof JmlTagTypeDeclaration) {
                ((JmlTagTypeDeclaration) item).getType().resolve(this);
                path = ((JmlTagTypeDeclaration) item).getType().getPath();
            } else if (item instanceof JmlTagId) {
                ((JmlTagId) item).resolve(this);
                path = ((JmlTagId) item).getPath();
            }
            if (path != null) {
                Id[] ids = path.asArray();
                if (ids.length == 1) {
                    final Id id = ids[0];

                    AdsDefinition clazz = ((UFModule) getModule()).findDefWrapper(id);
                    if (clazz != null) {
                        if (clazz instanceof AdsLibUserFuncWrapper) {
                            AdsLibUserFuncWrapper libUf = (AdsLibUserFuncWrapper) clazz;
                            if (libUf.isEqualTo(getOwnerPid())) {
                                libUf.updateProfile(findProfile());
                            }
                        }
                        usedWrappers.put(ids[0], clazz);
                    }
                }
            }
        }
        return new ArrayList<>(usedWrappers.values());
    }

    @Override
    public boolean isUserExtension() {
        return true;
    }

    private static class FakeClassLoader extends ClassLoader {

        private String mainClassName;
        private Map<String, byte[]> bytes = new HashMap<>();

        private FakeClassLoader(Object context, Blob binaryData) {
            super(context.getClass().getClassLoader());
            InputStream stream = null;
            try {
                stream = binaryData.getBinaryStream(1, binaryData.length());
                loadJarToMap(stream);
                if (!bytes.isEmpty()) {
                    for (String name : bytes.keySet()) {
                        if (!name.contains("$")) {
                            mainClassName = name;
                            break;
                        }
                    }
                }
            } catch (SQLException | IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
            }
        }

        private byte[] readJarEntryData(final JarInputStream jar) throws IOException {
            final ByteArrayOutputStream bstream = new ByteArrayOutputStream();
            final byte[] block = new byte[4096];
            int blockSize;
            for (;;) {
                blockSize = jar.read(block);
                if (blockSize == -1) {
                    break;
                }
                if (blockSize != 0) {
                    bstream.write(block, 0, blockSize);
                }
            }
            return bstream.toByteArray();
        }

        private void loadJarToMap(InputStream stream) throws IOException {
            JarInputStream jar = null;
            try {
                jar = new JarInputStream(stream);
                JarEntry e = jar.getNextJarEntry();
                while (e != null) {
                    if (!e.isDirectory() && e.getName().endsWith(".class")) {
                        String name = e.getName().substring(0, e.getName().length() - 6).replace('/', '.');
                        bytes.put(name, readJarEntryData(jar));
                    }
                    e = jar.getNextJarEntry();
                }
            } finally {
                if (jar != null) {
                    jar.close();
                }
            }
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] data = bytes.get(name);
            if (data != null) {
                return defineClass(name, data, 0, data.length);
            }
            return super.findClass(name); //To change body of generated methods, choose Tools | Templates.
        }

        Class getUFClass() {
            if (mainClassName == null) {
                return null;
            }
            try {
                return findClass(mainClassName);
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                return null;
            }
        }
    }

    public static boolean canSafelyCopyRuntime(Object context, AdsUserFuncDefinitionDocument xDoc, Blob javaRuntime) {
        if (javaRuntime == null) {
            return true;
        }
        if (xDoc == null || xDoc.getAdsUserFuncDefinition() == null) {
            return false;
        } else {
            JmlType src = xDoc.getAdsUserFuncDefinition().getSource();
            if (src == null) {
                return false;
            }
            boolean classFormatCheckRequired = false;
            for (JmlType.Item xItem : src.getItemList()) {
                if (xItem.isSetDbEntity()) {
                    if (xItem.getDbEntity().getIsUFOwner()) {
                        classFormatCheckRequired = true;
                        break;
                    }
                }
            }
            if (classFormatCheckRequired) {
                FakeClassLoader cl = new FakeClassLoader(context, javaRuntime);
                Class ufClass = cl.getUFClass();
                if (ufClass != null) {
                    try {
                        Field field = ufClass.getDeclaredField(String.valueOf(JmlTagDbEntity.UF_OWNER_FIELD_NAME));
                        return true;
                    } catch (Exception ex) {
                        Logger.getLogger(AdsUserFuncDef.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                        ufClass.getFields();
                    }
                    return false;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }

    }

    public static final boolean areTypesBindable(Definition context, AdsTypeDeclaration lhs, AdsTypeDeclaration rhs) {
        if (lhs == null || rhs == null) {
            return false;
        }
        if (AdsTypeDeclaration.isAssignable(lhs, rhs, context)) {
            return true;
        }

        if (lhs.getTypeId() == EValType.JAVA_TYPE && "byte".equals(lhs.getExtStr()) && lhs.getArrayDimensionCount() == 1 && rhs.getTypeId() == EValType.BLOB) {
            return true;
        }
        if (rhs.getTypeId() == EValType.JAVA_TYPE && "byte".equals(rhs.getExtStr()) && rhs.getArrayDimensionCount() == 1 && lhs.getTypeId() == EValType.BLOB) {
            return true;
        }
        return false;
    }
}
