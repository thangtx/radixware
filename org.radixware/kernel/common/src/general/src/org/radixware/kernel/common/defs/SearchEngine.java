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
package org.radixware.kernel.common.defs;

import java.util.*;
import org.radixware.kernel.common.defs.Dependences.Dependence;
import org.radixware.kernel.common.defs.HierarchyWalker.Controller;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Modules;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.types.Id;

/**
 * Search engine. Allows to search definition in the scope of some context, by
 * some key, for example, by id, or by name of puplished java class. Required to
 * realize search in module, solves search by dependencies.
 *
 */
public abstract class SearchEngine<T extends Definition, KEY extends Object> {

    protected final Definition context;

    protected SearchEngine(Definition context) {
        this.context = context;
    }

    private T findInModule(KEY key, Module module) {
        SearchEngine<T, KEY> searchEngineInDependentModule = findEngine(module);
        if (searchEngineInDependentModule != null) {
            T found = searchEngineInDependentModule.findInsideByKey(key);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private List<T> walkResult(Module.HierarchyWalker<Module> walker, final Module rootModule, final Module excludeModule, final KEY key, final boolean findFirst, final List<T> result, final Set<Module> visitedModules, final Set<Layer> pathBreackers) {

        walker.go(rootModule, new HierarchyWalker.AbstractDefaultAcceptor<Module>() {
            @Override
            public void accept(Controller<Object> controller, Module module) {
                if (pathBreackers.contains(module.getLayer())) {
                    controller.pathStop();
                    return;
                }
                if (excludeModule == module) {
                    return;
                }
                if (visitedModules.contains(module)) {
                    return;
                }
                visitedModules.add(module);
                T found = findInModule(key, module);
                if (found != null) {
                    pathBreackers.add(module.getLayer());
                    result.add(found);
                    if (findFirst) {
                        controller.stop();
                    } else {
                        controller.pathStop();
                    }
                }
            }
        });
        if (!result.isEmpty()) {
            return result;
        }
        return null;
    }

    protected boolean shouldVisitDependence(Id moduleId, KEY key) {
        return true;
    }

    private List<T> findOutsideByKey(final KEY key, final boolean findFirst) {
        if (context == null || !context.isInBranch()) {
            return Collections.emptyList();
        }
        final Module thisModule = context.getModule();
        if (thisModule == null) {
            return Collections.emptyList();
        }

        // firstly, try to search in overwritten modules
        final Segment segment = thisModule.getSegment();
        if (segment == null) {
            return Collections.emptyList();
        }

        Module.HierarchyWalker<Module> walker = new Module.HierarchyWalker<>();
        final List<T> result = new LinkedList<>();
        Set<Module> visitedModules = new HashSet<>();
        final Set<Layer> pathBreackers = new HashSet<>();

        if (walkResult(walker, thisModule, thisModule, key, findFirst, result, visitedModules, pathBreackers) != null) {
            return result;
        }

        final Modules modules = segment.getModules();

        List<Module> dependentModules = new ArrayList<>(20);
        // secondary, try to search in exactly specified dependent modules (for optimization)

        for (Dependence dependence : thisModule.getDependences().list()) {
            final Id depId = dependence.getDependenceModuleId();
            if (!shouldVisitDependence(depId, key)) {
                continue;
            }
            dependentModules.clear();
            modules.findById(dependence.getSegmentType(), depId, false, dependentModules);
            for (int i = 0, len = dependentModules.size(); i < len; i++) {
                Module dependentModule = dependentModules.get(i);
                if (dependentModule != thisModule && !visitedModules.contains(dependentModule)) {

                    if (walkResult(walker, dependentModule, null, key, findFirst, result, visitedModules, pathBreackers) != null) {
                        if (findFirst) {
                            return result;
                        }
                    }
                }
            }
        }

        // then, try to search using extensions
        //IDependenceProvider dependenceProvider = context.getDependenceProvider();
//        if (dependenceProvider.getClass() == DefaultDependenceProvider.class) {
//            return result;
//        } else {
        final Map<Id, Dependence> moduleId2Dependence = context.getDependenceProvider().get();
        //context.getDependenceProvider().collect(moduleId2Dependence);

        for (Dependence dependence : moduleId2Dependence.values()) {
            dependentModules.clear();
            modules.findById(dependence.getSegmentType(), dependence.getDependenceModuleId(), false, dependentModules);
            for (int i = 0, len = dependentModules.size(); i < len; i++) {
                Module dependentModule = dependentModules.get(i);
                if (dependentModule != thisModule && !visitedModules.contains(dependentModule)) {
                    if (walkResult(walker, dependentModule, null, key, findFirst, result, visitedModules, pathBreackers) != null) {
                        if (findFirst) {
                            return result;
                        }
                    }
                }
            }
        }

        return result;
        //}
    }

    protected abstract T findInsideByKey(KEY key);

    protected abstract SearchEngine<T, KEY> findEngine(Module module);
    private static volatile boolean resolveFirst = false;

    public static void disableMultipleResolution() {
        resolveFirst = true;
    }

    public static void enableMultipleResolution() {
        resolveFirst = false;
    }

    public SearchResult<T> findByKey(KEY id) {
        T definition = findInsideByKey(id);
        if (definition != null) {
            return new SearchResult.Single<>(definition);
        }
        List<T> definitions = findOutsideByKey(id, resolveFirst);
        if (definitions != null && !definitions.isEmpty()) {
            return SearchResult.list(definitions);
        } else {
            return SearchResult.empty();
        }
    }
}
