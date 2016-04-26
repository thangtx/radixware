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

package org.radixware.kernel.designer.common.general.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IOverridable;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.Hierarchy;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;


public class DefinitionsUtils extends RadixObjectsUtils {

    protected DefinitionsUtils() {
    }

    public static Definition findOverwrite(Definition definition) {
        if (definition instanceof AdsDefinition) {
            final AdsDefinition adsDefinition = (AdsDefinition) definition;
            if (adsDefinition instanceof IOverwritable) {
                final Hierarchy<AdsDefinition> hierarchy = adsDefinition.getHierarchy();
                if (hierarchy != null) {
                    return hierarchy.findOverwritten().get();
                }
            }
        } else if (definition instanceof DdsTableDef) {
            final DdsTableDef table = (DdsTableDef) definition;
            return table.findOverwritten();
        } else if (definition instanceof Module) {
            final Module module = (Module) definition;
            return module.findOverwritten();
        }
        return null;
    }

    public static Definition findOverride(Definition definition) {
        if ((definition instanceof AdsDefinition) && (definition instanceof IOverridable)) {
            final AdsDefinition adsDefinition = (AdsDefinition) definition;
            final Hierarchy<AdsDefinition> hierarchy = adsDefinition.getHierarchy();
            if (hierarchy != null) {
                return hierarchy.findOverridden().get();
            }
        }
        return null;
    }

    /**
     * @return true if top is upper than bottom by overriding level, false
     * otherwise.
     */
    public static boolean isOverridesOrOverwrites(Definition top, Definition bottom) {
        final Definition overwrite = findOverwrite(top);
        if (overwrite != null) {
            if (overwrite == bottom) {
                return true;
            }
            if (isOverridesOrOverwrites(overwrite, bottom)) {
                return true;
            }
        }

        final Definition override = findOverride(top);
        if (override != null) {
            if (override == bottom) {
                return true;
            }
            if (isOverridesOrOverwrites(override, bottom)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return list of definitions that are top on overriding level
     */
    public static List<Definition> getTop(Collection<? extends Definition> defs) {
        final Map<Id, List<Definition>> id2defs = new HashMap<Id, List<Definition>>();

        for (Definition def : defs) {
            final Id id = def.getId();
            List<Definition> olds = id2defs.get(id);
            boolean needAdd = true;
            if (olds != null) {
                for (int i = olds.size() - 1; i >= 0; i--) {
                    final Definition old = olds.get(i);
                    if (isOverridesOrOverwrites(old, def)) {
                        needAdd = false;
                        break;
                    }
                }
                if (needAdd) {
                    for (int i = olds.size() - 1; i >= 0; i--) {
                        final Definition old = olds.get(i);
                        if (isOverridesOrOverwrites(def, old)) {
                            olds.remove(i);
                        }
                    }
                }
            } else {
                olds = new ArrayList<Definition>(1);
                id2defs.put(id, olds);
            }

            if (needAdd) {
                olds.add(def);
            }
        }

        final List<Definition> result = new ArrayList<Definition>(defs.size());
        for (Definition def : defs) {
            final Id id = def.getId();
            final List<Definition> olds = id2defs.get(id);
            if (olds.contains(def)) {
                result.add(def);
            }
        }

        return result;
    }

    /**
     * Collect all definitions by specified visitor provider from specified
     * context and bellow (children).
     *
     * @return list of definitions that are top on overriding level.
     */
    public static List<Definition> collectTopInside(RadixObject context, VisitorProvider visitorProvider) {
        final List<Definition> list = collectAllInside(context, visitorProvider);
        return getTop(list);
    }

    /**
     * Collect all definitions by specified visitor provider, that visible from
     * specified context.
     *
     * @return list of definitions that are top on overriding level.
     */
    public static Collection<Definition> collectTopAround(RadixObject context, VisitorProvider visitorProvider) {
        final List<Definition> list = collectAllAround(context, visitorProvider);
        return getTop(list);
    }

    public static Collection<Definition> collectForOverwrite(Definition context, VisitorProvider visitorProvider) {
        assert context != null;
        assert visitorProvider != null;

        final Module module = context.getModule();
        assert module != null;

        final List<Module> allowedModules = new ArrayList<Module>();
        for (Module overwrittenModule = module.findOverwritten(); overwrittenModule != null; overwrittenModule = overwrittenModule.findOverwritten()) {
            allowedModules.add(overwrittenModule);
        }

        final DefinitionCollector collector = new DefinitionCollector();

        for (Module allowedModule : allowedModules) {
            allowedModule.visit(collector, visitorProvider);
        }

        return getTop(collector.getResult());
    }

    private static final class FindUsagesVisitor implements IVisitor {

        private final Set<RadixObject> collection;
        private final RadixObject object;

        public FindUsagesVisitor(RadixObject object) {
            this.collection = new HashSet<>();
            this.object = object;
        }

        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject != null) {
                final List<Definition> dependencies = new ArrayList<>();
                radixObject.collectDependences(dependencies);

                for (final Definition depend : dependencies) {
                    if (object == depend) {
                        collection.add(radixObject);
                    }
                }
            }
        }

        public Set<RadixObject> getCollection() {
            return collection;
        }
    }

    /**
     *
     * @deprecated replaced by {@link  UsagesFinder}
     */
    @Deprecated
    public static List<RadixObject> findUsages(RadixObject object) {
        if (object == null) {
            return null;
        }

        final FindUsagesVisitor visitor = new FindUsagesVisitor(object);
        final VisitorProvider provider = new AdsVisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (Thread.interrupted()) {
                    cancel();
                }
                return !isCancelled();
            }

            @Override
            public boolean isContainer(RadixObject object) {
                return !isCancelled() && super.isContainer(object);
            }
        };

        final Branch branch = object.getBranch();
        if (branch != null) {
            branch.visit(visitor, provider);
        }

        return new ArrayList<>(visitor.getCollection());
    }
}
