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

package org.radixware.kernel.common.builder;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;


public abstract class AbstractAdsBuilder extends RadixObjectsProcessor {

    //we have to sort context objects by definition type to prevent partial compilation
    //results on skipping unused java classes
    //in  following order: enums,xmls,others
    private class Collector implements ICollector {

        private int count;

        public class Lists {

            private ArrayList<RadixObject> enums = null;
            private ArrayList<RadixObject> xmls = null;
            private ArrayList<RadixObject> other = null;
        }
        private Map<Layer, Lists> lists = new HashMap<>();
        private final Branch branch;
        private List<RadixObject> result = null;

        public Collector(Branch branch) {
            this.branch = branch;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Collection<RadixObject> get() {
            synchronized (this) {
                if (result != null) {
                    return result;
                }
                final List<Layer> layers = branch.getLayers().getInOrder();
                result = new ArrayList<>(count);
                for (int i = 0, len = layers.size(); i < len; i++) {
                    Lists list = lists.get(layers.get(i));
                    if (list != null) {

                        if (list.enums != null) {
                            result.addAll(list.enums);
                            list.enums.clear();
                            list.enums = null;
                        }
                        if (list.xmls != null) {
                            result.addAll(list.xmls);
                            list.xmls.clear();
                            list.xmls = null;
                        }
                        if (list.other != null) {
                            result.addAll(list.other);
                            list.other.clear();
                            list.other = null;
                        }
                    }
                    lists.put(layers.get(i), null);
                }
                lists.clear();

                return result;
            }
        }

        @Override
        public void accept(RadixObject radixObject) {
            final Layer layer = radixObject.getLayer();
            if (layer == null) {
                return;
            }
            Lists ll = lists.get(layer);
            if (ll == null) {
                ll = new Lists();
                lists.put(layer, ll);
            }
            if (radixObject instanceof AdsEnumDef) {
                if (ll.enums == null) {
                    ll.enums = new ArrayList<>(10);
                }
                ll.enums.add(radixObject);
            } else if (radixObject instanceof IXmlDefinition) {
                if (ll.xmls == null) {
                    ll.xmls = new ArrayList<>(10);
                }
                ll.xmls.add(radixObject);
            } else {
                if (ll.other == null) {
                    ll.other = new ArrayList<>(50);
                }
                ll.other.add(radixObject);
            }
            count++;
        }
    }

    public static final class Factory {

        public static List<? extends AbstractAdsBuilder> newInstance(Branch branch, EBuildActionType type, EnumSet<ERuntimeEnvironmentType> envs, IBuildEnvironment buildEnv, BuildOptions options, final Map<Id, Id> idReplaceMap) {
            switch (type) {
                case CLEAN:
                    return Collections.singletonList(new Cleaner(branch, envs, buildEnv, options));
                case BUILD:
                case COMPILE_SINGLE:
                    return Collections.singletonList(new Builder(branch, envs, buildEnv, options, idReplaceMap));
                case RELEASE:
                    return Arrays.asList(new AbstractAdsBuilder[]{new Cleaner(branch, envs, buildEnv, options), new Builder(branch, envs, buildEnv, options, idReplaceMap)});
                case CLEAN_AND_BUILD:
                    return Arrays.asList(new AbstractAdsBuilder[]{new Cleaner(branch, envs, buildEnv, options), new Builder(branch, envs, buildEnv, options, idReplaceMap)});
                default:
                    throw new IllegalStateException();
            }
        }
    }
    protected final EnumSet<ERuntimeEnvironmentType> envs;
    protected final IBuildEnvironment buildEnv;
    protected final BuildOptions options;
    private final Branch branch;

    protected AbstractAdsBuilder(Branch branch, EnumSet<ERuntimeEnvironmentType> envs, IBuildEnvironment buildEnv, BuildOptions options) {
        super(buildEnv.getFlowLogger().getCancellable());
        this.envs = envs;
        this.buildEnv = buildEnv;
        this.options = options;
        this.branch = branch;
    }

    @Override
    protected final ICollector createCollector() {
        return new Collector(branch);
    }

    protected abstract void prepare();

    protected abstract void complete();

    protected void build(RadixObject radixObject, ProcessFlow flow) {
    }

    protected void build(Collection<RadixObject> radixObjects, ProcessFlow flow, IProgressHandle progressHandle, ICancellable cancellable) {
    }

    @Override
    protected void processObject(final RadixObject radixObject, final ProcessFlow flow) {
        buildEnv.getMutex().readAccess(new Runnable() {
            @Override
            public void run() {
                if (radixObject.isInBranch()) {
                    build(radixObject, flow);
                }
            }
        });
    }

    @Override
    protected void processObjects(final Collection<RadixObject> radixObject, final ProcessFlow flow, final IProgressHandle progressHandle, final ICancellable cancellable) {
        buildEnv.getMutex().readAccess(new Runnable() {
            @Override
            public void run() {
                build(radixObject, flow, progressHandle, cancellable);
            }
        });
    }

    

    protected void afterLookup(ProcessFlow flow, List<Module> lookupModules) {
    }

    @Override
    protected String getPreparingProcessName() {
        return "Determinig Targets...";
    }
    //protected ESystemComponent currentEnvironment = ESystemComponent.COMMON;
    private Set<Module> processedModules = null;

    protected void moduleProcessed(Module module) {
        synchronized (this) {
            if (!processedModules.contains(module)) {
                processedModules.add(module);
            }
        }
    }

    protected boolean isModuleProcessed(AdsModule module) {
        synchronized (this) {
            return processedModules.contains(module);
        }
    }

    private class TaskScheduler {

        private final Set<ERuntimeEnvironmentType> envs;
        private final Collection<? extends RadixObject> contexts;
        private final List<Module> lookupModules;

        public TaskScheduler(Set<ERuntimeEnvironmentType> envs, Collection<? extends RadixObject> contexts, List<Module> lookupModules) {
            this.envs = envs;
            this.contexts = contexts;
            this.lookupModules = lookupModules;
        }

        private void execute() {
            final CountDownLatch latch = new CountDownLatch(envs.size());

            for (ERuntimeEnvironmentType sc : envs) {
                run(sc, latch);
            }

            try {
                latch.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }

        protected void onEnvCompleted(ERuntimeEnvironmentType e) {
        }

        private void run(final ERuntimeEnvironmentType e, final CountDownLatch latch) {
            final Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        processEnvironment(contexts, lookupModules, e);
                    } finally {
                        onEnvCompleted(e);
                        latch.countDown();
                    }

                }
            });
            t.start();
        }
    }

    public final void execute(final Collection<? extends RadixObject> contexts, final List<Module> lookupModules, Set<Module> processedModules) {
        synchronized (this) {
            this.processedModules = processedModules;
        }
        try {
            prepare();
            if (wasCancelled()) {
                return;
            }
            if (envs.contains(ERuntimeEnvironmentType.COMMON) && envs.contains(ERuntimeEnvironmentType.SERVER) && envs.contains(ERuntimeEnvironmentType.COMMON_CLIENT) && envs.contains(ERuntimeEnvironmentType.WEB) && envs.contains(ERuntimeEnvironmentType.EXPLORER)) {
                if (processAllEnvironments(contexts, lookupModules)) {
                    return;
                }
            }
            if (envs.contains(ERuntimeEnvironmentType.COMMON) || buildEnv.getActionType() == EBuildActionType.COMPILE_SINGLE) {
                processEnvironment(contexts, lookupModules, ERuntimeEnvironmentType.COMMON);
            }
            if (this.options.isMultythread() && envs.contains(ERuntimeEnvironmentType.SERVER) && (envs.contains(ERuntimeEnvironmentType.COMMON_CLIENT) || envs.contains(ERuntimeEnvironmentType.WEB) || envs.contains(ERuntimeEnvironmentType.EXPLORER))) {
                if (wasCancelled()) {
                    return;
                }
                if (envs.contains(ERuntimeEnvironmentType.COMMON_CLIENT)) {
                    final EnumSet<ERuntimeEnvironmentType> forBuild = EnumSet.of(ERuntimeEnvironmentType.SERVER, ERuntimeEnvironmentType.COMMON_CLIENT);
                    TaskScheduler scheduler = new TaskScheduler(forBuild, contexts, lookupModules) {
                        @Override
                        public void onEnvCompleted(ERuntimeEnvironmentType e) {
                            if (e == ERuntimeEnvironmentType.COMMON_CLIENT) {
                                final EnumSet<ERuntimeEnvironmentType> set = EnumSet.noneOf(ERuntimeEnvironmentType.class);
                                if (envs.contains(ERuntimeEnvironmentType.WEB)) {
                                    set.add(ERuntimeEnvironmentType.WEB);
                                }
                                if (envs.contains(ERuntimeEnvironmentType.EXPLORER)) {
                                    set.add(ERuntimeEnvironmentType.EXPLORER);
                                }
                                final TaskScheduler scheduler = new TaskScheduler(set, contexts, lookupModules);
                                scheduler.execute();
                            }
                        }
                    };
                    scheduler.execute();
                } else {
                    final EnumSet<ERuntimeEnvironmentType> forBuild = EnumSet.of(ERuntimeEnvironmentType.SERVER);
                    if (envs.contains(ERuntimeEnvironmentType.EXPLORER)) {
                        forBuild.add(ERuntimeEnvironmentType.EXPLORER);
                    }
                    if (envs.contains(ERuntimeEnvironmentType.WEB)) {
                        forBuild.add(ERuntimeEnvironmentType.WEB);
                    }

                    TaskScheduler scheduler = new TaskScheduler(forBuild, contexts, lookupModules);
                    scheduler.execute();
                }

            } else {
                if (envs.contains(ERuntimeEnvironmentType.SERVER)) {
                    if (wasCancelled()) {
                        return;
                    }
                    processEnvironment(contexts, lookupModules, ERuntimeEnvironmentType.SERVER);
                }
                if (envs.contains(ERuntimeEnvironmentType.COMMON_CLIENT)) {
                    if (wasCancelled()) {
                        return;
                    }
                    processEnvironment(contexts, lookupModules, ERuntimeEnvironmentType.COMMON_CLIENT);
                }
                if (envs.contains(ERuntimeEnvironmentType.EXPLORER)) {
                    if (wasCancelled()) {
                        return;
                    }
                    processEnvironment(contexts, lookupModules, ERuntimeEnvironmentType.EXPLORER);
                }
                if (envs.contains(ERuntimeEnvironmentType.WEB)) {
                    if (wasCancelled()) {
                        return;
                    }
                    processEnvironment(contexts, lookupModules, ERuntimeEnvironmentType.WEB);
                }
            }
        } finally {
            complete();
        }
    }

    protected VisitorProvider createVisitorProvider(ERuntimeEnvironmentType env) {
        return AdsVisitorProviders.newCompileableDefinitionsVisitorProvider(env);
    }

    protected abstract ProcessFlow beforeLookup(ERuntimeEnvironmentType env);

    protected boolean processAllEnvironments(Collection<? extends RadixObject> contexts, List<Module> lookupAdsModules) {
        return false;
    }

    protected void processEnvironment(Collection<? extends RadixObject> contexts, List<Module> lookupAdsModules, ERuntimeEnvironmentType e) {
        //this.currentEnvironment = e;
        ProcessFlow flow = beforeLookup(e);
        this.process(contexts, createVisitorProvider(e), buildEnv.getBuildDisplayer(), flow);
        if (wasCancelled()) {
            return;
        }
        afterLookup(flow, lookupAdsModules);
    }

    public abstract String getDisplayName();
}
