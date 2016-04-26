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

package org.radixware.kernel.common.defs.ads.type;

import org.radixware.kernel.common.defs.Definition;

/**
 * Java class representation
 *
 */
public final class JavaClassType extends FixedType {

    public static final class Factory {

        public static JavaClassType forClassName(String className) {
            return new JavaClassType(className, null);
        }

        public static JavaClassType forClassName(String className, Definition context) {
            return new JavaClassType(className, context);
        }
    }
    protected final Definition context;

    private JavaClassType(String typeName, Definition context) {
        super(typeName);
        this.context = context;
    }

//    @Override
//    public boolean isSubclassOf(AdsType type) {
//
//        if (this.equals(type) || isObject()) {
//            return true;
//        }
//
//        if (type instanceof JavaClassType) {
//            JavaClassType javaType = (JavaClassType) type;
//
//            if (Utils.equals(typeName, javaType.typeName)) {
//                return true;
//            }
//
//            JavaClassesInterfacing interfacing = new JavaClassesInterfacing(javaType.getJavaClassName(), getPlatformLib());
//            return interfacing.isSuperFor(typeName);
//        }
//
//        return false;
//    }
//
//    private boolean isObject() {
//        return "java.lang.Object".equals(getJavaClassName());
//    }
//
//    private PlatformLib getPlatformLib() {
//        if (context != null) {
//            AdsSegment ads = (AdsSegment) context.getLayer().getAds();
//
//            ERuntimeEnvironmentType environmentType = ERuntimeEnvironmentType.COMMON;
//
//            if (context instanceof IEnvDependent) {
//                environmentType = ((IEnvDependent) context).getUsageEnvironment();
//            }
//            return ads.getBuildPath().getPlatformLibs().getKernelLib(environmentType);
//        }
//        return PlatformLibs.getJreLib();
//    }
    public String getJavaClassName() {
        return typeName;
    }
}
