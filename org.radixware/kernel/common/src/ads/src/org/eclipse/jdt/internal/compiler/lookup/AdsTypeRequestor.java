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

package org.eclipse.jdt.internal.compiler.lookup;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.ISourceType;
import org.eclipse.jdt.internal.compiler.impl.ITypeRequestor;


public class AdsTypeRequestor implements ITypeRequestor {

    AdsLookupEnvironment lookupEnvironment;
    private static final Map<String, IBinaryType> binariesCache = new HashMap<>();
    private boolean cacheBinaryTypes = false;
    private boolean useCachedBinaries = false;

    public AdsTypeRequestor(boolean cacheBinaryTypes, boolean useCachedBinaries) {
//        this.cacheBinaryTypes = cacheBinaryTypes;
//        this.useCachedBinaries = useCachedBinaries;
    }

    public static void resetBinariesCache() {
        synchronized (binariesCache) {
            binariesCache.clear();
        }
    }

    @Override
    public void accept(IBinaryType binaryType, PackageBinding packageBinding, AccessRestriction accessRestriction) {
        if (binaryType instanceof AdsMissingBinaryType) {
            ReferenceBinding binding = packageBinding.getType0(binaryType.getName());
            if (binding == null) {
                packageBinding.addType(new AdsProblemReferenceBinding(((AdsMissingBinaryType) binaryType), ProblemReasons.NotFound));
            }else{
                binding.tagBits |= TagBits.HasMissingType;
            }
        } else {
            if (cacheBinaryTypes) {
                String name = String.valueOf(binaryType.getName());
                synchronized (binariesCache) {
                    if (!binariesCache.containsKey(name)) {
                        binariesCache.put(name, binaryType);
                    }
                }
            }
            lookupEnvironment.createBinaryTypeFrom(binaryType, packageBinding, accessRestriction);
        }
    }

    public void attachLookupEnvironment(AdsLookupEnvironment env) {
        this.lookupEnvironment = env;
        if (useCachedBinaries) {
            synchronized (binariesCache) {
                for (IBinaryType type : binariesCache.values()) {
                    env.cacheBinaryType(type, null);
                }
            }
        }
    }

    @Override
    public void accept(ICompilationUnit unit, AccessRestriction accessRestriction) {
        if (unit instanceof AdsCompilationUnit) {
            lookupEnvironment.acceptCompilationUnit((AdsCompilationUnit) unit);
        }
    }

    @Override
    public void accept(ISourceType[] sourceType, PackageBinding packageBinding, AccessRestriction accessRestriction) {
    }
}
