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

package org.radixware.kernel.common.defs.ads.module;

import java.util.*;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.EmptyList;
import org.radixware.kernel.common.utils.FilteredList;
import org.radixware.schemas.adsdef.UsageDescription;
import org.radixware.schemas.adsdef.UsagesDocument.Usages;


public class UsagesSupport {

    private class DefinitionIdComparator<T extends Definition> implements Comparator<T> {

        @Override
        public int compare(T o1, T o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }

    private class DefinitionPathComparator<T extends Definition> implements Comparator<T> {

        @Override
        public int compare(T o1, T o2) {
            Id[] p = o1.getIdPath();
            StringBuilder sb = new StringBuilder();
            for (Id id : p) {
                sb.append(id.toString()).append('-');
            }
            String s1 = sb.toString();
            p = o2.getIdPath();
            sb.setLength(0);
            for (Id id : p) {
                sb.append(id.toString()).append('-');
            }
            String s2 = sb.toString();
            return s1.compareTo(s2);
        }
    }

    private static class UsageInfo {

        final Layer layer;
        final AdsModule module;

        public UsageInfo(Layer layer, AdsModule module) {
            this.layer = layer;
            this.module = module;
        }
    }

    private static UsageInfo calcUsageInfo(AdsDefinition def) {
        AdsModule module = def.getModule();
        if (module == null) {
            return null;
        }
        Segment s = module.getSegment();
        if (s == null) {
            return null;
        }
        Layer layer = s.getLayer();
        if (layer == null) {
            return null;
        }
        return new UsageInfo(layer, module);
    }
    private final AdsModule thisModule;

    public UsagesSupport(AdsModule module) {
        this.thisModule = module;
    }

    public void saveUsages(AdsDefinition user, Usages xDef) {
        final Layer thisLayer = thisModule.getSegment().getLayer();
        if (thisLayer != null) {
            class UsageType {

                boolean isOverride = false;
                boolean isOverwrite = false;
                boolean isInherit = false;
            }
            final Map<AdsDefinition, UsageType> found = new HashMap<>();
            final FilteredList<Definition> list = new FilteredList<>(new EmptyList<Definition>(), new IFilter<Definition>() {
                @Override
                public boolean isTarget(Definition radixObject) {

                    if (radixObject instanceof AdsDefinition) {
                        AdsDefinition adsdef = (AdsDefinition) radixObject;
                        if (found.containsKey(adsdef)) {
                            return false;
                        }
                        final AdsModule module = adsdef.getModule();
                        if (module != null && module != thisModule) {
                            Segment segment = module.getSegment();
                            if (segment != null) {
                                Layer layer = segment.getLayer();
                                if (layer != null && (layer == thisLayer || thisLayer.isHigherThan(layer))) {
                                    found.put(adsdef, new UsageType());
                                    return true;
                                }
                            }
                        }
                    }
                    return false;
                }
            });
            List<AdsDefinition> defList = user == null ? thisModule.getDefinitions().list(new IFilter<AdsDefinition>() {
                @Override
                public boolean isTarget(AdsDefinition radixObject) {
                    return radixObject.getDefinitionType() != EDefType.ROLE;
                }
            }) : Collections.singletonList(user);
            for (AdsDefinition def : defList) {
                def.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        radixObject.collectDirectDependences(list);

                    }
                }, VisitorProviderFactory.createDefaultVisitorProvider());
            }
            for (AdsDefinition def : defList) {
                def.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        if (radixObject instanceof AdsDefinition) {
                            AdsDefinition adsDef = (AdsDefinition) radixObject;
                            AdsDefinition.Hierarchy<AdsDefinition> h = adsDef.getHierarchy();
                            AdsDefinition ovr = h.findOverridden().get();
                            if (ovr != null) {
                                UsageType ut = found.get(ovr);
                                if (ut == null) {
                                    ut = new UsageType();
                                    found.put(adsDef, ut);
                                }
                                ut.isOverride = true;
                            }
                            ovr = h.findOverwritten().get();
                            if (ovr != null) {
                                UsageType ut = found.get(ovr);
                                if (ut == null) {
                                    ut = new UsageType();
                                    found.put(adsDef, ut);
                                }
                                ut.isOverwrite = true;
                            }
                            if (adsDef instanceof AdsClassDef) {
                                AdsClassDef superClass = ((AdsClassDef) adsDef).getInheritance().findSuperClass().get();
                                if (superClass != null) {
                                    UsageType ut = found.get(superClass);
                                    if (ut == null) {
                                        ut = new UsageType();
                                        found.put(superClass, ut);
                                    }
                                    ut.isInherit = true;
                                }
                            } else if (adsDef instanceof AdsPresentationDef) {
                                AdsPresentationDef base = ((AdsPresentationDef) adsDef).findBasePresentation().get();
                                if (base != null) {
                                    UsageType ut = found.get(base);
                                    if (ut == null) {
                                        ut = new UsageType();
                                        found.put(base, ut);
                                    }
                                    ut.isInherit = true;
                                }
                            }
                        }
                    }
                }, VisitorProviderFactory.createDefaultVisitorProvider());

            }


            Map<Layer, Map<AdsModule, List<AdsDefinition>>> usages = new HashMap<>();
            for (AdsDefinition adsdef : found.keySet()) {
                final UsageInfo info = calcUsageInfo(adsdef);
                if (info != null) {
                    Map<AdsModule, List<AdsDefinition>> usagesForLayer = usages.get(info.layer);
                    if (usagesForLayer == null) {
                        usagesForLayer = new HashMap<>();
                        usages.put(info.layer, usagesForLayer);
                    }
                    List<AdsDefinition> deflist = usagesForLayer.get(info.module);
                    if (deflist == null) {
                        deflist = new LinkedList<>();
                        usagesForLayer.put(info.module, deflist);
                    }
                    deflist.add(adsdef);
                }
            }

            if (!usages.isEmpty()) {
                List<Layer> layers = new ArrayList<>(usages.keySet());
                Collections.sort(layers, new Comparator<Layer>() {
                    @Override
                    public int compare(Layer o1, Layer o2) {
                        return o1.getURI().compareTo(o2.getURI());
                    }
                });
                final Comparator<AdsDefinition> defComparator = new DefinitionPathComparator<>();
                final Comparator<AdsModule> moduleComparator = new DefinitionIdComparator<>();
                for (Layer l : layers) {
                    final Usages.Layer xLayer = xDef.addNewLayer();

                    xLayer.setName(l.getName());
                    xLayer.setURI(l.getURI());

                    final Map<AdsModule, List<AdsDefinition>> moduleDefs = usages.get(l);

                    List<AdsModule> modules = new ArrayList<>(moduleDefs.keySet());
                    Collections.sort(modules, moduleComparator);

                    final List<Usages.Layer.Module> xModules = new ArrayList<>(modules.size());
                    for (AdsModule m : modules) {
                        List<AdsDefinition> defs = moduleDefs.get(m);
                        if (defs == null) {
                            continue;
                        }
                        Collections.sort(defs, defComparator);

                        Usages.Layer.Module xModule = Usages.Layer.Module.Factory.newInstance();
                        xModules.add(xModule);
                        xModule.setId(m.getId());
                        xModule.setName(m.getName());
                        final List<UsageDescription> xUsagesList = new ArrayList<>(defs.size());
                        for (AdsDefinition def : defs) {
                            if (!def.isPublished() || def.getModule() == thisModule) {
                                continue;
                            }
                            UsageDescription xUsage = UsageDescription.Factory.newInstance();
                            UsageType ut = found.get(def);
                            def.appendToUsage(xUsage);
                            xUsagesList.add(xUsage);
                            if (ut.isInherit || ut.isOverride || ut.isOverwrite) {
                                xUsage.setIsExtension(true);
                            }
                        }
                        xModule.setUsageArray(xUsagesList.toArray(new UsageDescription[xUsagesList.size()]));
                    }
                    xLayer.setModuleArray(xModules.toArray(new Usages.Layer.Module[xModules.size()]));
                }
            }
        }
    }
}
