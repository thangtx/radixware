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

package org.radixware.kernel.common.builder.check.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Dependences.Dependence;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.kernel.KernelModule;
import org.radixware.kernel.common.types.Id;


public abstract class ModuleChecker<T extends Module> extends DefinitionChecker<T> {

    public ModuleChecker() {
    }

    @Override
    public void check(T module, IProblemHandler problemHandler) {
        super.check(module, problemHandler);
        Layer l = module.getLayer();
        boolean isLocalizing = l != null && l.isLocalizing();

        boolean istest = module.isTest();
        for (Dependence dependence : module.getDependences()) {
            List<Module> deps = dependence.findDependenceModule(module);

            if (deps.isEmpty()) {
                error(module, problemHandler, "Dependence module(s) not found: #" + dependence.getDependenceModuleId());
            } else {
                if (!isLocalizing && !(module instanceof UdsModule) && !module.isWarningSuppressed(Module.ModuleWarningSuppressionSupport.DEPENDS_FROM_TEST_MODULE)) {
                    boolean isUserDefEnv = BuildOptions.UserModeHandlerLookup.getUserModeHandler() != null;
                    for (Module dep : deps) {
                        if (dep.isTest() && !istest && !isUserDefEnv) {
                            warning(module, problemHandler, Module.ModuleWarningSuppressionSupport.DEPENDS_FROM_TEST_MODULE, dep.getQualifiedName());
                        }
                    }
                }
            }
        }
        if (istest && !module.getName().contains("Testing")) {
            warning(module, problemHandler, "It is recommended for test modules to be named as '*Testing*'");
        }
        if (!(module instanceof UdsModule) && !(module instanceof KernelModule)) {
            CheckOptions options = getCheckOptions();
            if (options != null && options.isCheckModuleDependences()) {
                checkForObsoleteDependencies(module, problemHandler);
            }
        }
    }

    private void checkForObsoleteDependencies(T module, IProblemHandler ph) {
        final List<Definition> allDeps = new ArrayList<Definition>(41);
        final Set<Id> actualDeps = new HashSet<Id>();
        module.visit(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                if (radixObject instanceof Module) {
                    return;
                }
                allDeps.clear();
                radixObject.collectDirectDependences(allDeps);
                for (Definition def : allDeps) {
                    if (def != null) {
                        final Module m = def.getModule();
                        if (m != null) {
                            actualDeps.add(m.getId());
                        }
                    }
                }
            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());
        //Set<Id> currentDeps = module.getDependences().getModuleIds();
        Layer layer = module.getLayer();
        if (layer == null || !layer.isLocalizing()) {
            for (Dependence dep : module.getDependences()) {
                if (dep.isForced()) {
                    continue;
                }
                final Id id = dep.getDependenceModuleId();
                if (!actualDeps.contains(id)) {

                    final List<Module> ms = module.getDependences().findModuleById(id);
                    for (Module m : ms) {
                        if ((module instanceof AdsModule && ((AdsModule) module).isCompanionOf(m)) || m instanceof KernelModule) {
                            continue;
                        }
                        final String message = "Unused dependency: " + (m == null ? "#" + id : m.getQualifiedName());
                        warning(module, ph, message);
                    }
                }
            }
        }
    }
//    @Override
//    protected void idDuplicationDetected(T module, Definition oldDefinition, IProblemHandler problemHandler) {
//        if (oldDefinition instanceof Module) {
//            Module oldModule = (Module) oldDefinition;
//            if (module.getSegment().getLayer() != oldModule.getSegment().getLayer()) {
//                return;
//            }
//        }
//
//        super.idDuplicationDetected(module, oldDefinition, problemHandler);
//    }
}
