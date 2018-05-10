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

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.radixware.kernel.common.compiler.lookup.AdsWorkspace;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.radixware.kernel.common.compiler.core.lookup.AdsNameEnvironment;
import org.radixware.kernel.common.compiler.core.lookup.LookupUtils;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

public class PlatformLib {

    final PlatformLibs libs;
    final ERuntimeEnvironmentType env;
    private static final Object lock = new Object();
    private static volatile Map<String, RadixPlatformClass> platformClasses;
    private volatile AdsWorkspace workspaceInstance;
    private volatile AdsNameEnvironment nameEnvironmentInstance;

    PlatformLib(PlatformLibs libs, ERuntimeEnvironmentType e) {
        this.env = e;
        this.libs = libs;
    }

    public void cleanup() {
        synchronized (lock) {
            workspaceInstance = null;
            nameEnvironmentInstance = null;
        }
    }

    public AdsWorkspace getAdsWorkspace() {
        synchronized (lock) {
            if (workspaceInstance == null) {
                workspaceInstance = new AdsWorkspace(getAdsNameEnvironment(), new AdsProblemReporter() {
                    @Override
                    public void isClassPathCorrect(char[][] wellKnownTypeName, CompilationUnitDeclaration compUnitDecl, Object location) {
                        //super.isClassPathCorrect(wellKnownTypeName, compUnitDecl, location); //To change body of generated methods, choose Tools | Templates.
                        //ignore
                    }
                }, true, true);
            }
            return workspaceInstance;
        }
    }

    public AdsNameEnvironment getAdsNameEnvironment() {
        synchronized (lock) {
            if (nameEnvironmentInstance == null) {
                nameEnvironmentInstance = new AdsNameEnvironment(libs.buildPath.getLayer(), env);
            }
            return nameEnvironmentInstance;
        }
    }

    public static void enableClassCaching(boolean enable) {
        synchronized (lock) {
            if (enable) {
                if (platformClasses == null) {
                    platformClasses = new HashMap<>();
                    cacheCounter++;
                }
            } else {
                if (platformClasses != null) {
                    platformClasses.clear();
                    platformClasses = null;
                }
            }
        }
    }

    public static boolean isClassCachingEnabled() {
        synchronized (lock) {
            return platformClasses != null;
        }
    }
    private static long cacheCounter = 0;

    public static long getCacheVersion() {
        synchronized (lock) {
            return cacheCounter;
        }
    }

    public RadixPlatformClass findPlatformClass(String name) {
        if (name == null) {
            return null;
        }
        synchronized (lock) {
            RadixPlatformClass result = null;
            if (platformClasses != null) {
                result = platformClasses.get(name);
            }
            if (result != null) {
                return result;
            }
            ReferenceBinding binding = LookupUtils.findPlatformClass(name, libs.buildPath.getLayer(), env);
            if (binding == null) {
                return null;
            }
            result = new RadixPlatformClass(this, binding);
            if (platformClasses != null) {
                platformClasses.put(name, result);
            }
            return result;
        }
    }
}
