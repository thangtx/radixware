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

package org.radixware.kernel.common.defs.ads.platform;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.radixware.kernel.common.compiler.core.lookup.LookupUtils;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;


public class BuildPath extends RadixObject {

    private PlatformPublishers platformPublishers = null;
    private PlatformXml platformXml = null;
    private PlatformLibs platformLibs = null;

    public PlatformPublishers getPlatformPublishers() {
        synchronized (this) {
            if (platformPublishers == null) {
                platformPublishers = new PlatformPublishers(this);
            }
            return platformPublishers;
        }
    }

    public BuildPath(AdsSegment owner) {
        setContainer(owner);
    }

    public PlatformLibs getPlatformLibs() {
        synchronized (this) {
            if (platformLibs == null) {
                platformLibs = new PlatformLibs(this);
            }
            return platformLibs;
        }
    }

    public void cleanup() {
        for (ERuntimeEnvironmentType sc : ERuntimeEnvironmentType.values()) {
            getPlatformLibs().getKernelLib(sc).cleanup();
        }
        if (platformXml != null) {
            platformXml.cleanup();
        }
    }

    @Override
    public String getName() {
        return "Build Path";
    }

    public AdsSegment getSegment() {
        return (AdsSegment) getContainer();
    }
    private static final Map<String, AdsTypeDeclaration> platformClassName2ItsSuperClassName = new HashMap<String, AdsTypeDeclaration>(19);
    private static final Map<String, AdsTypeDeclaration[]> platformClassName2ItsSuperInterfaces = new HashMap<>(19);

    public final AdsTypeDeclaration getPlatformClassBaseClass(String platformClassName, ERuntimeEnvironmentType env) {
        synchronized (lock) {
            AdsTypeDeclaration superClass = platformClassName2ItsSuperClassName.get(platformClassName);
            if (superClass == null) {
                ReferenceBinding clazz = LookupUtils.findPlatformClass(platformClassName, getLayer(), env);
                if (clazz != null && clazz.superclass() != null) {
                    superClass = AdsTypeDeclaration.Factory.newInstance(clazz.superclass());
                } else {
                    return null;
                }
                if (superClass != null) {
                    platformClassName2ItsSuperClassName.put(platformClassName, superClass);
                }
            }
            return superClass;
        }
    }
    private static final Object lock = new Object();
    private static final AdsTypeDeclaration[] NO_INTERFACES = new AdsTypeDeclaration[0];

    public final AdsTypeDeclaration[] getPlatformClassBaseInterfaces(String platformClassName, ERuntimeEnvironmentType env) {
        synchronized (lock) {
            AdsTypeDeclaration[] superIfaces = platformClassName2ItsSuperInterfaces.get(platformClassName);
            if (superIfaces == null) {
                ReferenceBinding clazz = LookupUtils.findPlatformClass(platformClassName, getLayer(), env);
                if (clazz != null && clazz.superInterfaces() != null && clazz.superInterfaces().length > 0) {
                    superIfaces = new AdsTypeDeclaration[clazz.superInterfaces().length];
                    for (int i = 0; i < superIfaces.length; i++) {
                        superIfaces[i] = AdsTypeDeclaration.Factory.newInstance(clazz.superInterfaces()[i]);
                    }
                } else {
                    superIfaces = NO_INTERFACES;
                }
                platformClassName2ItsSuperInterfaces.put(platformClassName, superIfaces);
            }
            return superIfaces;
        }
    }

    File[] getKernelDirs() {
        final ArrayList<File> dirs = new ArrayList<>();
        Layer layer = getSegment().getLayer();
        if (layer != null) {
            final File layerDir = layer.getDirectory();
            if (layerDir != null && layerDir.exists()) {
                File kernelDir = new File(layerDir, "kernel");
                if (kernelDir.exists() && kernelDir.isDirectory()) {
                    dirs.add(kernelDir);
                }
            }
        }

        final File[] kernelDirs = new File[dirs.size()];
        dirs.toArray(kernelDirs);
        return kernelDirs;
    }

    public PlatformXml getPlatformXml() {
        synchronized (this) {
            if (platformXml == null) {
                platformXml = new PlatformXml(this);
            }
            return platformXml;
        }
    }

    public static ArrayList<String> getKernelModules(ERuntimeEnvironmentType env) {
        ArrayList<String> lookup = new ArrayList<>();

        lookup.add("common");

        if (env == ERuntimeEnvironmentType.SERVER) {
            lookup.add("server");
        }

        if (env == ERuntimeEnvironmentType.EXPLORER) {
            lookup.add("explorer");
        }
        return lookup;
    }

    @Override
    protected boolean isPersistent() {
        return false;
    }
}