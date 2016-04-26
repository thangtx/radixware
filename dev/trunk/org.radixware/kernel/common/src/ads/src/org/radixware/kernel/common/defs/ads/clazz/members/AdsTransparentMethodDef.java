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
package org.radixware.kernel.common.defs.ads.clazz.members;

import java.lang.ref.WeakReference;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.platform.TypeSignature;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;
import org.radixware.schemas.adsdef.AccessRules.Transparence;

public class AdsTransparentMethodDef extends AdsMethodDef {

    public static class Factory {

        public static AdsTransparentMethodDef newInstance() {
            return new AdsTransparentMethodDef("newTransparentMethod", false);
        }

        public static AdsTransparentMethodDef newConstructorInstance() {
            return new AdsTransparentMethodDef("newTransparentMethod", true);
        }

        public static AdsTransparentMethodDef newTemporaryInstance(RadixObject container) {
            AdsTransparentMethodDef m = newInstance();
            m.setContainer(container);
            return m;
        }

        public static AdsTransparentMethodDef newConstructorTemporaryInstance(RadixObject container) {
            AdsTransparentMethodDef m = newConstructorInstance();
            m.setContainer(container);
            return m;
        }
    }

    private class AdsMethodTransparence extends AdsTransparence {

        public AdsMethodTransparence(AdsDefinition context, Transparence xDef) {
            super(context, xDef);
        }

        public AdsMethodTransparence(AdsDefinition context, String publishedName, boolean isExtendable) {
            super(context, publishedName, isExtendable);
        }

        @Override
        public void setPublishedName(String publishedName) {

//            AdsClassDef ownerClass = getOwnerClass();
//            if (ownerClass != null) {
//                AdsTransparence t = ownerClass.getTransparence();
//                if (t != null && t.isTransparent() && t.getPublishedName().equals(AdsCursorClassDef.PLATFORM_CLASS_NAME)) {
//                    if (publishedName.equals("close()V")) {
//                        AdsMethodDef another = ownerClass.getMethods().getLocal().findById(AdsSystemMethodDef.ID_STMT_CLOSE);
//                        if (another != null && another != AdsTransparentMethodDef.this) {
//                            return;
//                        }
//                        RadixObject c = AdsTransparentMethodDef.this.getContainer();
//                        @SuppressWarnings("unchecked")
//                        Definitions<AdsDefinition> defs = (Definitions<AdsDefinition>) c;
//                        defs.remove(AdsTransparentMethodDef.this);
//                        AdsTransparentMethodDef.this.setId(AdsSystemMethodDef.ID_STMT_CLOSE);
//                        defs.add(AdsTransparentMethodDef.this);
//                    }
//                }
//            }
            super.setPublishedName(publishedName);
            AdsTransparentMethodDef.this.fireNameChange();
        }
    }
    private final AdsMethodTransparence transparence;

    AdsTransparentMethodDef(AbstractMethodDefinition xMethod) {
        super(xMethod);
        transparence = new AdsMethodTransparence(this, xMethod.getAccessRules().getTransparence());
    }

    AdsTransparentMethodDef(String name, boolean isConstructor) {
        super(name, isConstructor);
        transparence = new AdsMethodTransparence(this, name, false);
    }

    @Override
    public EMethodNature getNature() {
        return EMethodNature.USER_DEFINED;
    }

    @Override
    public AdsTransparence getTransparence() {
        return transparence;
    }

    public char[] getPublishedMethodName() {
        char[] methodSignature = transparence.getPublishedName().toCharArray();
        for (int i = 0; i < methodSignature.length; i++) {
            char c = methodSignature[i];
            if (c == '(' || (c == '<' && i > 0)) {
                return CharOperation.subarray(methodSignature, 0, i);
            }
        }
        return methodSignature;
    }

    public char[] getPublishedMethodDescriptor() {
        char[] methodSignature = transparence.getPublishedName().toCharArray();
        for (int i = 0; i < methodSignature.length; i++) {
            char c = methodSignature[i];
            if (c == '(' || (c == '<' && i > 0)) {
                return CharOperation.subarray(methodSignature, i, methodSignature.length);
            }
        }
        return methodSignature;
    }

    public void updateProfile() {
        final AdsTypeDeclaration[] decls = calcPublishedProfile();
        if (decls != null && decls.length > 0) {
            getProfile().getParametersList().clear();
            for (int i = 0; i < decls.length - 1; i++) {
                final MethodParameter p = MethodParameter.Factory.newInstance("arg" + String.valueOf(i), decls[i]);
                getProfile().getParametersList().add(p);
            }

            if (!isConstructor()) {
                getProfile().getReturnValue().setType(decls[decls.length - 1]);
            }
        }
        setName(String.valueOf(getPublishedMethodName()));
    }

    public AdsTypeDeclaration[] calcPublishedProfile() {
        char[] sign = getPublishedMethodDescriptor();
        if (sign == null) {
            return null;
        }
        return TypeSignature.parseMethodSignature(sign);

    }
    private WeakReference<RadixPlatformClass.Method> methodRef = null;
    private long usedCacheVersion = -1;
    private final Object methodLock = new Object();

    public RadixPlatformClass.Method findPublishedMethod() {
        synchronized (methodLock) {
            if (PlatformLib.isClassCachingEnabled()) {
                if (usedCacheVersion != PlatformLib.getCacheVersion()) {
                    final RadixPlatformClass.Method method = findPublishedMethodImpl();
                    if (method != null) {
                        methodRef = new WeakReference<>(method);
                    }
                    usedCacheVersion = PlatformLib.getCacheVersion();
                }
                return methodRef == null ? null : methodRef.get();
            } else {
                return findPublishedMethodImpl();
            }
        }
    }

    @Override
    public boolean isFinal() {
        final RadixPlatformClass.Method method = findPublishedMethod();
        if (method == null) {
            return super.isFinal();
        } else {
            return method.isFinal();
        }
    }

    @Override
    public boolean canChangeFinality() {
        boolean result = super.canChangeFinality();
        return result? findPublishedMethod() == null: result;
    }
    
    private RadixPlatformClass.Method findPublishedMethodImpl() {
        if (isInBranch()) {
            final AdsClassDef ownerClass = getOwnerClass();
            if (ownerClass == null) {
                return null;
            }
            final AdsTransparence t = ownerClass.getTransparence();
            if (t == null || !t.isTransparent()) {
                return null;
            }
            final String className = t.getPublishedName();
            RadixPlatformClass clazz = ((AdsSegment) getLayer().getAds()).getBuildPath().getPlatformLibs().getKernelLib(getUsageEnvironment()).findPlatformClass(className);
            if (clazz != null) {
                RadixPlatformClass.Method publishedMethod = null;
                while (clazz != null) {
                    publishedMethod = clazz.findMethodByProfile(this);
                    if (publishedMethod != null) {
                        break;
                    }
                    clazz = clazz.getSuperClass();

                }
                return publishedMethod;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public EAccess getMinimumAccess() {
        RadixPlatformClass.Method method = findPublishedMethod();
        if (method == null) {
            return super.getMinimumAccess();
        } else {
            return method.getAccess();
        }
    }

    @Override
    public boolean isProfileable() {
        return false;
    }

    public String getTypeTitle() {
        return "Platform Method Wrapper";
    }
}
