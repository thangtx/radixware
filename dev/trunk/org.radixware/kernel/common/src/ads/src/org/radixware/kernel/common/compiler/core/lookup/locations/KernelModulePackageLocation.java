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
package org.radixware.kernel.common.compiler.core.lookup.locations;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.compiler.core.AdsLookup;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.repository.kernel.KernelModule;
import org.radixware.kernel.common.utils.CharOperations;

public class KernelModulePackageLocation extends JarBasedPackageLocation {

    private static final Map<KernelModule, KernelModulePackageLocation> cache = new WeakHashMap<>();

    private static long lastUsageTime = 0;
    private static long MAX_INACTIVE_INTERVAL = 60000;
 
    public static void resetCaches() {
        synchronized (cache) {
            cache.clear();
        }
    }

    public static KernelModulePackageLocation getInstance(KernelModule module) {
        synchronized (cache) {
            final long currentTime = System.currentTimeMillis();
            if (currentTime - lastUsageTime > MAX_INACTIVE_INTERVAL) {
                cache.clear();
            }
            try {
                if (module == null) {
                    return new KernelModulePackageLocation(null);
                }
                KernelModulePackageLocation loc = cache.get(module);
                if (loc == null) {
                    loc = new KernelModulePackageLocation(module);
                    cache.put(module, loc);
                }
                return loc;
            } finally {
                lastUsageTime = System.currentTimeMillis();
            }
        }
    }

    public static KernelModulePackageLocation getDefaultInstance(RadixObject context) {
        final Layer radix = context.getBranch().getLayers().findByURI("org.radixware");
        if (radix != null) {
            final KernelModule module = (KernelModule) radix.getKernel().getModules().findById(AdsLookup.RADIX_KERNEL_COMMON);
            if (module == null) {
                return null;
            }
            return getInstance(module);
        } else {//for test purpose
            Layer base = context.getBranch().getLayers().getInOrder().get(0);
            final KernelModule module = (KernelModule) base.getKernel().getModules().findById(AdsLookup.RADIX_KERNEL_COMMON);
            if (module == null) {
                return null;
            }
            return getInstance(module);
        }
    }
    private WeakReference<KernelModule> kernelModule;
    private JreJarPackageLocation jreRef;

    private KernelModulePackageLocation(KernelModule kernelModule) {
        super(kernelModule == null ? null : kernelModule.getLayer());
        this.kernelModule = new WeakReference<>(kernelModule);
        if (kernelModule == null || kernelModule.getId() == AdsLookup.RADIX_KERNEL_COMMON) {
            jreRef = JreJarPackageLocation.getInstance();
        }
    }

    @Override
    public boolean containsPackageName(char[][] packageName) {
        return super.containsPackageName(packageName);
    }

    @Override
    protected List<IJarDataProvider> getJarFiles() {
        if (kernelModule != null) {
            KernelModule module = kernelModule.get();
            if (module != null) {
                IRepositoryModule moduleRepo = module.getRepository();
                if (moduleRepo == null) {
                    return Collections.emptyList();
                } else {
                    return moduleRepo.getBinaries();
                }
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }

    private abstract class RadixTypeRetriever {

        private NameEnvironmentAnswer answer;
        private boolean noAnswer;

        public NameEnvironmentAnswer get() {
            if (noAnswer) {
                return null;
            }
            if (answer == null) {
                answer = search();
            }
            if (answer == null) {
                noAnswer = true;
            }
            return answer;

        }

        protected abstract NameEnvironmentAnswer search();
    }
    private final RadixTypeRetriever[] radixTypes = new RadixTypeRetriever[]{
        new RadixTypeRetriever() {
            @Override
            protected NameEnvironmentAnswer search() {
                return jreRef.findAnswer(TypeConstants.JAVA_LANG, new char[][]{TypeConstants.JAVA_LANG_BOOLEAN[2]});
            }
        },
        new RadixTypeRetriever() {
            @Override
            protected NameEnvironmentAnswer search() {
                return jreRef.findAnswer(TypeConstants.JAVA_LANG, new char[][]{TypeConstants.JAVA_LANG_CHARACTER[2]});
            }
        },
        new RadixTypeRetriever() {
            @Override
            protected NameEnvironmentAnswer search() {
                return jreRef.findAnswer(new char[][]{TypeConstants.JAVA, "sql".toCharArray()}, new char[][]{"Timestamp".toCharArray()});
            }
        },
        new RadixTypeRetriever() {
            @Override
            protected NameEnvironmentAnswer search() {
                return jreRef.findAnswer(TypeConstants.JAVA_LANG, new char[][]{TypeConstants.JAVA_LANG_LONG[2]});
            }
        },
        new RadixTypeRetriever() {
            @Override
            protected NameEnvironmentAnswer search() {
                return jreRef.findAnswer(new char[][]{TypeConstants.JAVA, "math".toCharArray()}, new char[][]{"BigDecimal".toCharArray()});
            }
        }, new RadixTypeRetriever() {
            @Override
            protected NameEnvironmentAnswer search() {
                return jreRef.findAnswer(TypeConstants.JAVA_LANG, new char[][]{TypeConstants.JAVA_LANG_STRING[2]});
            }
        },};

    @Override
    public NameEnvironmentAnswer findAnswer(char[][] packageName, char[][] typeName) {
        if (jreRef != null) {
            int radixTypeIdx = -1;
            if (typeName.length == 1 && packageName.length == AdsLookup.RADIX_TYPES_PACKAGE.length && (radixTypeIdx = AdsLookup.isRadixSystemType(typeName[0])) >= 0) {
                boolean matches = true;
                for (int i = 0; i < packageName.length; i++) {
                    if (!CharOperations.equals(packageName[i], AdsLookup.RADIX_TYPES_PACKAGE[i])) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    return radixTypes[radixTypeIdx].get();
                }
            }
        }
        return super.findAnswer(packageName, typeName); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NameEnvironmentAnswer findAnswer(Definition definition, String suffix) {
        return null;
    }
}
