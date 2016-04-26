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

package org.radixware.kernel.common.builder.check.java;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.compiler.core.lookup.AdsNameEnvironment;
import org.radixware.kernel.common.constants.FileNames;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.PlatformLibs;
import org.radixware.kernel.common.defs.ads.src.JavaFileSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.lang.ClassLinkageAnalyzer;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.fs.JarFileDataProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;
import org.radixware.kernel.common.utils.FileUtils;


public class LinkageAnalyzer extends ClassLinkageAnalyzer {

    @Override
    protected KernelClassLoader createKernelClassLoader(LayerInfo layer, ERuntimeEnvironmentType env) {
        return new MyKernelClassLoader(layer, env, buildEnv.getBuildProblemHandler());
    }

    @Override
    protected AdsClassLoader createAdsClassLoader(LayerInfo layer, ERuntimeEnvironmentType env) {
        return new MyAdsClassLoader(layer, env, buildEnv.getBuildProblemHandler());
    }

    @Override
    protected void acceptProblem(ModuleInfo module, String message) {
        buildEnv.getBuildProblemHandler().accept(RadixProblem.Factory.newError(((MyModuleInfo) module).module, message));
    }

    @Override
    protected void acceptMessage(String message) {
        buildEnv.getFlowLogger().message(message);
    }

    private class MyLayerInfo implements LayerInfo {

        private final Layer layer;

        public MyLayerInfo(Layer layer) {
            this.layer = layer;
        }

        @Override
        public String getURI() {
            return layer.getURI();
        }

        @Override
        public ModuleInfo[] findModulesByPackageNameId(Id id) {
            List<AdsModule> modules = new LinkedList<AdsModule>();
            for (Module module : layer.getAds().getModules().list()) {
                char[] moduleIdChars = JavaSourceSupport.getModulePackageName(module);
                Id moduleId = Id.Factory.loadFrom(String.valueOf(moduleIdChars));
                if (moduleId == id) {
                    modules.add((AdsModule) module);
                }
            }


            if (!modules.isEmpty()) {
                List<ModuleInfo> infos = new LinkedList<ModuleInfo>();
                for (AdsModule module : modules) {
                    ModuleInfo info = findModuleInfo(module);
                    if (info != null) {
                        infos.add(info);
                    }
                }
                return infos.toArray(new ModuleInfo[infos.size()]);
            } else {
                return null;
            }
        }

        @Override
        public List<LayerInfo> findPrevLayer() {

            List<Layer> prevs = layer.listBaseLayers();
            List<LayerInfo> infos = new LinkedList<>();
            for (Layer prev : prevs) {
                LayerInfo info = findLayerInfo(prev);;
                if (info != null) {
                    infos.add(info);
                }
            }
            return infos;
        }

        @Override
        public String getDisplayName() {
            return layer.getName();
        }
    }

    private class MyKernelClassLoader extends ClassLinkageAnalyzer.KernelClassLoader {

        private PlatformLibs platformLibs;
        private IProblemHandler problemHandler;
        private AdsSegment segment;

        public MyKernelClassLoader(LayerInfo layer, ERuntimeEnvironmentType env, IProblemHandler problemHandler) {
            super(layer, env);
            this.segment = (AdsSegment) ((MyLayerInfo) layer).layer.getAds();
            this.platformLibs = segment.getBuildPath().getPlatformLibs();
            this.problemHandler = problemHandler;
        }

        @Override
        protected byte[] findClassBytesBySlashSeparatedName(final String name) throws ClassNotFoundException {
            if (platformLibs == null) {
                throw new ClassNotFoundException(name);
            }

            PlatformLib lib = platformLibs.getKernelLib(env);
            if (lib == null) {
                throw new ClassNotFoundException(name);
            }

            final byte[][] bytes = new byte[1][];
            String[] names = name.split("/");
            final char[][] packageName = new char[names.length - 1][];
            for (int i = 0; i < names.length - 1; i++) {
                packageName[i] = names[i].toCharArray();
            }

            lib.getAdsNameEnvironment().invokeRequest(new AdsNameEnvironment.PackageNameRequest() {
                @Override
                public char[][] getPackageName() {
                    return packageName;
                }

                @Override
                public boolean accept(char[][] packageName, char[] className, AdsNameEnvironment.ClassDataProvider provider) {
                    if (wasCancelled()) {
                        return true;
                    }
                    char[] compoundName = CharOperations.merge(packageName, '/');
                    compoundName = CharOperations.merge(compoundName, className, '/');
                    final String matchName = String.valueOf(compoundName);
                    if (name.equals(matchName)) {
                        bytes[0] = provider.getClassFileBytes();
                        return true;
                    }
                    return false;
                }
            });

            return bytes[0];
        }

        @Override
        protected void acceptProblem(String message) {
            problemHandler.accept(RadixProblem.Factory.newError(segment, message));
        }

        @Override
        protected void acceptMessage(String message) {
            buildEnv.getFlowLogger().message(message);
        }

        @Override
        protected void close() {
            JarFileDataProvider.reset();
        }

        @Override
        protected void summary() {
        }
    }

    private class MyAdsClassLoader extends ClassLinkageAnalyzer.AdsClassLoader {

        private IProblemHandler problemHandler;
        private AdsSegment segment;

        public MyAdsClassLoader(LayerInfo info, ERuntimeEnvironmentType env, IProblemHandler problemHandler) {
            super(info, env);
            this.segment = (AdsSegment) ((MyLayerInfo) layer).layer.getAds();
            this.problemHandler = problemHandler;
        }

        @Override
        protected void acceptProblem(String message) {
            problemHandler.accept(RadixProblem.Factory.newError(segment, message));
        }

        @Override
        protected void acceptMessage(String message) {
            buildEnv.getFlowLogger().message(message);
        }
    }
    private final IBuildEnvironment buildEnv;

    public LinkageAnalyzer(IBuildEnvironment buildEnv) {
        this(buildEnv, true);
    }

    public LinkageAnalyzer(IBuildEnvironment buildEnv, boolean verbose) {
        super(verbose);
        this.buildEnv = buildEnv;
    }
    private final Cancellable cancel = new Cancellable() {
        @Override
        public boolean cancel() {
            LinkageAnalyzer.this.cancel();
            return true;
        }

        @Override
        public boolean wasCancelled() {
            return LinkageAnalyzer.this.wasCancelled();
        }
    };

    public void process(RadixObject obj) {
        if (buildEnv.getMutex().getLongProcessLock().tryLock()) {
            try {
                buildEnv.prepare();
                buildEnv.getBuildProblemHandler().clear();
                IProgressHandle handle = buildEnv.getBuildDisplayer().getProgressHandleFactory().createHandle("Verifing class linkage...", cancel);
                try {
                    handle.start();
                    JarFileDataProvider.reset();
                    List<AdsModule> modulesList = new ArrayList<AdsModule>();
                    collectModules(obj, modulesList);
                    processModuleList(handle, modulesList);
                    summary();
                } finally {
                    reset();
                    handle.finish();
                }
            } finally {
                buildEnv.getMutex().getLongProcessLock().unlock();
            }
        } else {
            buildEnv.getBuildDisplayer().getDialogUtils().messageInformation("Build or check action is already running");
        }
    }

    public void process(List<AdsModule> modules) {
        IProgressHandle handle = buildEnv.getBuildDisplayer().getProgressHandleFactory().createHandle("Verifing class linkage...", cancel);
        try {
            handle.start();
            JarFileDataProvider.reset();
            processModuleList(handle, modules);
            summary();
        } finally {
            reset();
            handle.finish();
        }
    }

    private void processModuleList(IProgressHandle handle, List<AdsModule> modules) {
        if (modules.size() > 1) {
            handle.switchToDeterminate(modules.size());
            int count = 0;

            Map<Layer, List<AdsModule>> modulesByLayer = new HashMap<>();
            for (AdsModule module : modules) {
                if (module instanceof UdsModule) {
                    continue;
                }
                if (wasCancelled()) {
                    return;
                }

                Layer l = module.getLayer();
                List<AdsModule> list = modulesByLayer.get(l);
                if (list == null) {
                    list = new LinkedList<>();
                    modulesByLayer.put(l, list);
                }
                list.add(module);

            }
            for (Map.Entry<Layer, List<AdsModule>> e : modulesByLayer.entrySet()) {
                if (wasCancelled()) {
                    return;
                }
                List<AdsModule> list = e.getValue();
                LinkageAnalyzer analyzer = new LinkageAnalyzer(buildEnv);
                for (AdsModule module : list) {
                    if (wasCancelled()) {
                        return;
                    }
                    handle.progress("Verifing " + module.getQualifiedName(), count);
                    analyzer.process(findModuleInfo(module));
                    handle.progress(count++);
                }
            }
        } else {
            if (!modules.isEmpty() && !(modules.get(0) instanceof UdsModule)) {
                process(findModuleInfo(modules.get(0)));
            }
        }
    }

    private void collectModules(RadixObject obj, List<AdsModule> modules) {
        if (!obj.isInBranch() && !(obj instanceof Branch)) {
            return;
        }
        if (obj instanceof Branch) {
            for (Layer l : ((Branch) obj).getLayers().getInOrder()) {
                collectModulesImpl((AdsSegment) l.getAds(), modules);
            }
        } else if (obj instanceof Layer) {
            collectModulesImpl((AdsSegment) ((Layer) obj).getAds(), modules);
        } else if (obj instanceof Segment) {
            collectModulesImpl((AdsSegment) ((Segment) obj).getLayer().getAds(), modules);
        } else {
            Module module = obj.getModule();
            if (module instanceof AdsModule) {
                modules.add((AdsModule) module);
            }
        }
    }

    private void collectModulesImpl(AdsSegment segment, List<AdsModule> modules) {
        for (AdsModule module : segment.getModules()) {
            modules.add(module);
        }
    }
    private final Map<AdsModule, ModuleInfo> modules = new HashMap<AdsModule, ClassLinkageAnalyzer.ModuleInfo>();
    private final Map<Layer, LayerInfo> layers = new HashMap<Layer, LayerInfo>();

    private class MyModuleInfo implements ModuleInfo {

        private final AdsModule module;

        public MyModuleInfo(AdsModule module) {
            this.module = module;
        }

        @Override
        public Id getId() {
            return module.getId();
        }

        @Override
        public byte[] findDefinitionsIndexData() {
            try {
                return FileUtils.readBinaryFile(new File(module.getDirectory(), "definitions.xml"));
            } catch (IOException ex) {
                return null;
            }
        }
        
        @Override
        public byte[] findDirectoryIndexData() {
            try {
                return FileUtils.readBinaryFile(new File(module.getDirectory(), FileNames.DIRECTORY_XML));
            } catch (IOException ex) {
                return null;//evil
            }
        }

        @Override
        public File findBinaryFile(ERuntimeEnvironmentType env) {
            return JavaFileSupport.getBaseDirOrJarFile(module, env, JavaFileSupport.EKind.BINARY);
        }

        @Override
        public LayerInfo getLayer() {
            return findLayerInfo(module.getSegment().getLayer());
        }

        @Override
        public String getDisplayName() {
            return module.getQualifiedName();
        }

        @Override
        public void close() {
            //do nothing
        }
    }

    protected ModuleInfo findModuleInfo(AdsModule module) {
        synchronized (modules) {
            ModuleInfo info = modules.get(module);
            if (info == null) {
                info = new MyModuleInfo(module);
                modules.put(module, info);
            }
            return info;
        }
    }

    protected LayerInfo findLayerInfo(Layer layer) {
        synchronized (layers) {
            LayerInfo info = layers.get(layer);
            if (info == null) {
                info = new MyLayerInfo(layer);
                layers.put(layer, info);
            }
            return info;
        }
    }

    private void reset() {
        close();
        modules.clear();
        layers.clear();
    }
}
