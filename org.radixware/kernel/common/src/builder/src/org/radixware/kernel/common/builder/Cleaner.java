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

import java.io.File;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.builder.api.IBuildDisplayer;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.build.Clean;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.IJavaModule;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Branch;



class Cleaner extends AbstractAdsBuilder {

    @Override
    public void prepare() {
        //ignore
    }

    @Override
    public void complete() {
        //ignore
    }
    Clean clean;

    @Override
    public String getDisplayName() {
        return clean.getDisplayName();
    }

    Cleaner(Branch branch, EnumSet<ERuntimeEnvironmentType> envs, IBuildEnvironment buildEnv, BuildOptions options) {
        super(branch, envs, buildEnv, options);
        this.clean = new Clean(buildEnv.getFlowLogger());
    }

    @Override
    protected String getProcessName(ERuntimeEnvironmentType env) {
        return "Clean...";
    }

    @Override
    protected void build(RadixObject radixObject, ProcessFlow flow) {
        assert true : "Should not be called";
    }
    private boolean isCancelled = false;

    @Override
    public boolean wasCancelled() {
        return isCancelled || (buildEnv.getFlowLogger().getCancellable() != null && buildEnv.getFlowLogger().getCancellable().wasCancelled());
    }

    private <T extends Module & IJavaModule> void process(List<Module> lookupModules, ERuntimeEnvironmentType e, ProcessFlow flow) {
        final IBuildDisplayer buildDisplayer = buildEnv.getBuildDisplayer();
        final IProgressHandle handle = buildDisplayer.getProgressHandleFactory().createHandle(e == null ? "Clean..." : "Clean (" + e.getName() + ")...", new Cancellable() {
            @Override
            public boolean cancel() {
                isCancelled = true;
                return true;
            }

            @Override
            public boolean wasCancelled() {
                return Cleaner.this.wasCancelled();
            }
        });
        try {
            handle.start();
            handle.switchToDeterminate(lookupModules.size());
            int counter = 0;
            for (Module m : lookupModules) {
                if (wasCancelled()) {
                    return;
                }
                if (m.isReadOnly()) {
                    continue;
                }

                if (m instanceof IJavaModule) {
                    final T module = (T) m;
                    performClean(module, e);
                    if (module instanceof AdsModule) {
                        final File imgJar = new File(((AdsModule) module).getImages().getDirectory(), "img.jar");
                        if (imgJar.exists()) {
                            imgJar.delete();
                        }
                    }
                }
                moduleProcessed(m);
                handle.progress(m.getQualifiedName(), counter++);
            }
            afterLookup(flow, lookupModules);
        } finally {
            handle.finish();
        }
    }

    private <T extends Module & IJavaModule> void performClean(T module, ERuntimeEnvironmentType e) {
        if (e == null) {
            clean.cleanAll(module, buildEnv.getBuildProblemHandler());
        } else {
            clean.clean(module, e, buildEnv.getBuildProblemHandler());
        }
    }

    @Override
    protected void afterLookup(ProcessFlow flow, List<Module> lookupModules) {
        super.afterLookup(flow, lookupModules);
        clean.report();
    }

    @Override
    protected void processEnvironment(Collection<? extends RadixObject> contexts, List<Module> lookupModules, final ERuntimeEnvironmentType e) {
        //this.currentEnvironment = e;
        process(lookupModules, e, new ProcessFlow() {
            @Override
            public ERuntimeEnvironmentType getEnvironment() {
                return e;
            }
        });
    }

    @Override
    protected boolean processAllEnvironments(Collection<? extends RadixObject> contexts, List<Module> lookupModules) {
        process(lookupModules, null, new ProcessFlow() {
            @Override
            public ERuntimeEnvironmentType getEnvironment() {
                return null;
            }
        });
        return true;
    }

    @Override
    protected ProcessFlow beforeLookup(final ERuntimeEnvironmentType e) {
        return new ProcessFlow() {
            @Override
            public ERuntimeEnvironmentType getEnvironment() {
                return e;
            }
        };
    }
}
