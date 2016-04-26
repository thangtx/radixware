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

package org.radixware.kernel.common.builder.check.ads.clazz;

import java.util.Arrays;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentMethodDef;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.common.enums.EAccess;


@RadixObjectCheckerRegistration
public class AdsTransparentMethodChecker extends AdsMethodChecker<AdsTransparentMethodDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsTransparentMethodDef.class;
    }
    private static final String THROWS_LIST_MISMATCH_MESSAGE = "Throws list does not match to throws list of published method";

    @Override
    public void check(AdsTransparentMethodDef method, IProblemHandler problemHandler) {
        super.check(method, problemHandler);
        AdsClassDef ownerClass = method.getOwnerClass();
        AdsTransparence t = ownerClass.getTransparence();
        if (t == null || !t.isTransparent()) {
            error(method, problemHandler, "Transparent methods are alowed only for transparent classes");
        } else {
            PlatformClassCache cache = this.getHistory().findItemByClass(PlatformClassCache.class);
            if (cache == null) {
                cache = new PlatformClassCache();
                this.getHistory().registerItemByClass(cache);
            }

            String publishedClassName = t.getPublishedName();
            RadixPlatformClass clazz = cache.findPlatformClass(method, publishedClassName);

            if (clazz != null) {
                //final AdsTypeDeclaration[] profile = method.getProfile().getNormalizedProfile();
                RadixPlatformClass.Method publishedMethod = null;
                //String methodName = new String(method.getPublishedMethodName());
                while (clazz != null) {
                    publishedMethod = clazz.findMethodByProfile(method);
                    if (publishedMethod != null) {
                        break;
                    }
                    AdsTypeDeclaration decl = clazz.getSuperclass();
                    if (decl != null) {
                        clazz = cache.findPlatformClass(method, decl.getExtStr());
                    } else {
                        break;
                    }
                }
                if (publishedMethod == null) {
                    error(method, problemHandler, "Published method not found in class " + publishedClassName);
                } else {
                    if (publishedMethod.isConstructor() && publishedMethod.isSynthetic()) {
                        error(method, problemHandler, "Implicit default constructor can not be published");
                    }
                    if (publishedMethod.isStatic() != method.getProfile().getAccessFlags().isStatic()) {
                        if (publishedMethod.isStatic()) {
                            error(method, problemHandler, "Published method is static");
                        } else {
                            error(method, problemHandler, "Published method is not static");
                        }
                    }

                    if (publishedMethod.getAccess() == EAccess.PRIVATE) {
                        error(method, problemHandler, "Published method has private access. Remove this publishing");
                    } else if (method.getAccessMode() != publishedMethod.getAccess()) {
                        error(method, problemHandler, "Published method accessibility is differs from method accessibility (must be " + publishedMethod.getAccess().getName() + ")");
                    }

                    if (publishedMethod.isAbstract() != method.getProfile().getAccessFlags().isAbstract()) {
                        if (publishedMethod.isAbstract()) {
                            error(method, problemHandler, "Method must be abstract because published method is abstract");
                        } else {
                            error(method, problemHandler, "Method must be concrete because published method is concrete");
                        }
                    }

                    if (!publishedMethod.isStatic() && publishedMethod.isFinal() != method.getProfile().getAccessFlags().isFinal()) {
                        if (publishedMethod.isFinal()) {
                            error(method, problemHandler, "Method must be final because published method is final");
                        } else {
                            error(method, problemHandler, "Published method is not final");
                        }
                    }

                    AdsTypeDeclaration[] publishedParams = publishedMethod.getParameterTypes();

                    AdsTypeDeclaration[] published = Arrays.copyOf(publishedParams, publishedParams.length + 1);
                    published[publishedParams.length] = publishedMethod.getReturnType() == null ? AdsTypeDeclaration.VOID : publishedMethod.getReturnType();

                    AdsTypeDeclaration[] normal = method.getProfile().getNormalizedProfile();
                    checkProfileCompatibility(method, normal, published, problemHandler);

                    AdsTypeDeclaration[] publishedThrows = publishedMethod.getExceptions();

                    AdsMethodThrowsList throwsList = method.getProfile().getThrowsList();
                    AdsTypeDeclaration[] methodThrows = new AdsTypeDeclaration[throwsList.size()];


                    if ((publishedThrows == null && methodThrows.length > 0) || (publishedThrows != null && methodThrows.length != publishedThrows.length)) {
                        error(method, problemHandler, THROWS_LIST_MISMATCH_MESSAGE);
                    } else {
                        for (int i = 0; i < methodThrows.length; i++) {
                            methodThrows[i] = throwsList.get(i).getException();
                        }
                        for (int i = 0; i < methodThrows.length; i++) {
                            AdsTypeDeclaration one = methodThrows[i];
                            boolean found = false;
                            for (int j = 0; j < publishedThrows.length; j++) {
                                AdsTypeDeclaration another = publishedThrows[j];
                                if (one.equalsTo(method, another)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                error(method, problemHandler, THROWS_LIST_MISMATCH_MESSAGE);
                                break;
                            }
                        }
                    }

                }
            } else {
                error(method, problemHandler, "Published class not found " + publishedClassName);
            }
        }
    }
}
