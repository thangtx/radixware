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
package org.radixware.kernel.common.compiler.core.lookup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.radixware.kernel.common.compiler.lookup.AdsWorkspace;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.compiler.core.AdsLookup;
import org.radixware.kernel.common.compiler.core.lookup.locations.AdsModuleDevPackageLocation;
import org.radixware.kernel.common.compiler.core.lookup.locations.JreJarPackageLocation;
import org.radixware.kernel.common.compiler.core.lookup.locations.KernelModulePackageLocation;
import org.radixware.kernel.common.compiler.core.lookup.locations.PackageLocation;
import org.radixware.kernel.common.defs.Definition;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.SERVER;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.WEB;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.kernel.KernelModule;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;

public class AdsJavaPackage {

    private Map<String, AdsJavaPackage> knownChildren;
    private List<PackageLocation> locations;
    private AdsJavaPackage parentPackage;
    private char[] ownName;
    private ERuntimeEnvironmentType env;
    private Layer context;

    public AdsJavaPackage(AdsJavaPackage parent, char[] ownName) {
        this.knownChildren = new LinkedHashMap<>();
        this.parentPackage = parent;
        this.ownName = ownName;
        this.env = parentPackage.env;
    }

    public AdsJavaPackage(Layer context, ERuntimeEnvironmentType env) {
        this.knownChildren = new LinkedHashMap<>();
        this.parentPackage = null;
        this.ownName = new char[]{};
        this.env = env;
        this.context = context;
    }

    public ERuntimeEnvironmentType getEnvironmentType() {
        return env;
    }

    public NameEnvironmentAnswer findAnswer(char[][] typeName) {
        final char[][] fullName = getFullName();
        NameEnvironmentAnswer lastAnswer = null;

        for (final PackageLocation location : new ArrayList<>(getLocations())) {
            NameEnvironmentAnswer answer = location.findAnswer(fullName, typeName);
            if (answer != null) {
                lastAnswer = answer;
                break;
            }

        }
        return lastAnswer;
    }

    public NameEnvironmentAnswer findAnswer(Definition defintion, String suffix) {

        for (final PackageLocation location : new ArrayList<>(getLocations())) {
            NameEnvironmentAnswer answer = location.findAnswer(defintion, suffix);
            if (answer != null) {
                return answer;
            }
        }
        return null;
    }

    public AdsJavaPackage findPackage(char[][] packageName) {
        return findPackage(packageName, 0);
    }

    private AdsJavaPackage findPackage(char[][] packageName, int startIndex) {
        if (packageName == null) {
            return null;
        }
        if (startIndex >= packageName.length) {
            return null;
        }
        AdsJavaPackage reference = findPackage(packageName[startIndex]);
        if (reference != null) {
            if (startIndex + 1 < packageName.length) {
                return reference.findPackage(packageName, startIndex + 1);
            } else {
                return reference;
            }
        } else {
            return null;
        }
    }

    public AdsJavaPackage findPackageInCashe(char[] packageName) {
        String packageNameAsStr = String.valueOf(packageName);
        return knownChildren.get(packageNameAsStr);
    }

    public AdsJavaPackage findPackage(char[] packageName) {
        if (parentPackage == null && !inited) {
            getLocations();
        }
        return findPackageInCashe(packageName);
    }

    public AdsJavaPackage findOrCreatePackage(char[] packageName) {
        String packageNameAsStr = String.valueOf(packageName);
        AdsJavaPackage pkg = knownChildren.get(packageNameAsStr);
        if (pkg == null) {
            pkg = new AdsJavaPackage(this, packageName);
            knownChildren.put(packageNameAsStr, pkg);
        }
        return pkg;
    }

    public void addLocation(PackageLocation location) {
        synchronized (this) {
            if (locations == null) {
                locations = new ArrayList<>(3);
            }
            if (!locations.contains(location)) {
                locations.add(location);
            }
        }
    }

    private char[][] getFullName() {
        List<char[]> names = new LinkedList<>();
        AdsJavaPackage ref = this;
        while (ref != null) {
            if (ref.parentPackage == null) {//root
                break;
            }
            names.add(0, ref.ownName);
            ref = ref.parentPackage;
        }
        return names.toArray(new char[names.size()][]);
    }
    private Set<Module> addedModules = new HashSet<>();
    private boolean inited = false;

    public void attachLocations(Module contextModule, AdsWorkspace workspace) {
        synchronized (this) {
            final List<PackageLocation> diff = new LinkedList<>();
            if (!inited || locations == null) {
                inited = true;
                locations = new ArrayList<>(100);
                Layer.HierarchyWalker walker = new Layer.HierarchyWalker();
                walker.go(context, new Layer.HierarchyWalker.AbstractDefaultAcceptor<Layer>() {
                    @Override
                    public void accept(HierarchyWalker.Controller<Object> cntrlr, Layer t) {
                        for (Module km : t.getKernel().getModules()) {
                            if (km.getId() == AdsLookup.RADIX_KERNEL_COMMON && km.getLayer().getURI().equals("org.radixware")) {
                                continue;
                            }
                            KernelModule module = (KernelModule) km;
                            if (!module.isSuitableForEnvironment(env)) {
                                continue;
                            }
                            diff.add(KernelModulePackageLocation.getInstance(module));
                        }
                    }
                });
                JreJarPackageLocation jreJars = JreJarPackageLocation.getInstance();

                KernelModulePackageLocation kernelCommon = KernelModulePackageLocation.getDefaultInstance(context);
                if (kernelCommon != null) {
                    diff.add(kernelCommon);
                }

                diff.add(jreJars);
            }
            if (contextModule instanceof AdsModule) {
                if (!addedModules.contains(contextModule)) {
                    createLocations(workspace, (AdsModule) contextModule, diff);
                    addedModules.add(contextModule);
                }
                //implicit dependences
                if (env == ERuntimeEnvironmentType.SERVER) {
                    //find nearest to workspaceContext;
                    Layer layer = this.context;
                    final AdsModule module = (AdsModule) layer.getAds().getModules().findById(Id.Factory.loadFrom("mdlPEKYFVDRVZHGZCBQQDY2NOYFOY"));
                    if (module != null && !addedModules.contains(module)) {
                        createLocations(workspace, module, diff);
                        addedModules.add(module);
                    }
                }
            }
            for (PackageLocation location : diff) {
                location.createPackages(this);
            }
        }
    }

    private void createLocations(AdsWorkspace ws, AdsModule module, Collection<PackageLocation> locs) {

        switch (env) {
            case SERVER:
                locs.add(new AdsModuleDevPackageLocation(module, ws, ERuntimeEnvironmentType.SERVER));
                locs.add(new AdsModuleDevPackageLocation(module, ws, ERuntimeEnvironmentType.COMMON));
                break;
            case EXPLORER:
                locs.add(new AdsModuleDevPackageLocation(module, ws, ERuntimeEnvironmentType.EXPLORER));
                locs.add(new AdsModuleDevPackageLocation(module, ws, ERuntimeEnvironmentType.COMMON_CLIENT));
                locs.add(new AdsModuleDevPackageLocation(module, ws, ERuntimeEnvironmentType.COMMON));
                break;
            case WEB:
                locs.add(new AdsModuleDevPackageLocation(module, ws, ERuntimeEnvironmentType.WEB));
                locs.add(new AdsModuleDevPackageLocation(module, ws, ERuntimeEnvironmentType.COMMON_CLIENT));
                locs.add(new AdsModuleDevPackageLocation(module, ws, ERuntimeEnvironmentType.COMMON));
                break;
            case COMMON:
                locs.add(new AdsModuleDevPackageLocation(module, ws, ERuntimeEnvironmentType.COMMON));
                break;
            case COMMON_CLIENT:
                locs.add(new AdsModuleDevPackageLocation(module, ws, ERuntimeEnvironmentType.COMMON_CLIENT));
                locs.add(new AdsModuleDevPackageLocation(module, ws, ERuntimeEnvironmentType.COMMON));
                break;
        }

    }

    private List<PackageLocation> getLocations() {
        synchronized (this) {
            if (parentPackage != null) {
                return locations;
            } else {
                attachLocations(null, null);
                return locations;
            }
        }
    }

    public boolean invokeRequest(AdsNameEnvironment.NameRequest request) {
        if (request instanceof AdsNameEnvironment.PackageNameRequest) {
            char[][] name = ((AdsNameEnvironment.PackageNameRequest) request).getPackageName();
            if (name != null && name.length > 0) {
                AdsJavaPackage p = this;
                for (int i = 0; i < name.length; i++) {
                    AdsJavaPackage next = p.findPackage(name[i]);
                    if (next != null) {
                        p = next;
                    } else {
                        return false;
                    }
                }
                if (p != null) {
                    for (PackageLocation location : p.getLocations()) {
                        if (location.invokeRequest(request)) {
                            return true;
                        }
                    }
                }
                return false;
            }

        }
        for (PackageLocation location : getLocations()) {
            if (location.invokeRequest(request)) {
                return true;
            }
        }

//        if (knownChildren != null) {
//            for (AdsJavaPackage l : knownChildren.values()) {
//                if (l.invokeRequest(request)) {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    public boolean invokeRequest(AdsNameEnvironment.XmlNameRequest request) {
        for (PackageLocation location : getLocations()) {
            if (location.invokeRequest(request)) {
                return true;
            }
        }
//        if (knownChildren != null) {
//            for (AdsJavaPackage l : knownChildren.values()) {
//                if (l.invokeRequest(request)) {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    @Override
    public String toString() {
        return "(AdsJavaPackage) " + String.valueOf(CharOperations.merge(this.getFullName(), '.'));
    }
}
