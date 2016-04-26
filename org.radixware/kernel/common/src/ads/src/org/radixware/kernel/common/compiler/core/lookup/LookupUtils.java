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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.radixware.kernel.common.compiler.lookup.AdsWorkspace;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformEnum;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.utils.Utils;

public class LookupUtils {

    public static List<String> collectLibraryClasses(final Layer root, final ERuntimeEnvironmentType env) {

        final List<String> result = new LinkedList<>();
        Layer.HierarchyWalker.walk(root, new Layer.HierarchyWalker.Acceptor<Object>() {
            @Override
            public void accept(Layer.HierarchyWalker.Controller<Object> controller, Layer prevLayer) {
                collectLibraryClasses(root, env, result);
                if (env.isClientEnv() && env != ERuntimeEnvironmentType.COMMON_CLIENT) {
                    collectLibraryClasses(root, ERuntimeEnvironmentType.COMMON_CLIENT, result);
                }
                if (env != ERuntimeEnvironmentType.COMMON) {
                    collectLibraryClasses(root, ERuntimeEnvironmentType.COMMON, result);
                }
            }
        });
        return result;
    }

    public static byte[] getClassFileBytes(final Layer root, final String name, final ERuntimeEnvironmentType env) {
        return Layer.HierarchyWalker.walk(root, new Layer.HierarchyWalker.Acceptor<byte[]>() {
            @Override
            public void accept(Layer.HierarchyWalker.Controller<byte[]> controller, Layer prevLayer) {
                byte[] result = getClassFileBytesFromLayer(root, name, env);
                if (result != null) {
                    controller.setResultAndStop(result);
                    return;
                }
                if (env.isClientEnv() && env != ERuntimeEnvironmentType.COMMON_CLIENT) {
                    result = getClassFileBytesFromLayer(root, name, ERuntimeEnvironmentType.COMMON_CLIENT);
                    if (result != null) {
                        controller.setResultAndStop(result);
                        return;
                    }
                }
                if (env != ERuntimeEnvironmentType.COMMON) {
                    result = getClassFileBytesFromLayer(root, name, ERuntimeEnvironmentType.COMMON);
                    if (result != null) {
                        controller.setResultAndStop(result);
                        return;
                    }
                }
            }
        });
    }

    private static void collectLibraryClasses(final Layer root, final ERuntimeEnvironmentType env, final List<String> list) {
        final AdsNameEnvironment environment = ((AdsSegment) root.getAds()).getBuildPath().getPlatformLibs().getKernelLib(env).getAdsNameEnvironment();
        environment.invokeRequest(new AdsNameEnvironment.NameRequest() {
            @Override
            public boolean accept(char[][] packageName, char[] className, AdsNameEnvironment.ClassDataProvider provider) {
                list.add(String.valueOf(CharOperation.concatWith(packageName, className, '.')).replace('$', '.'));
                return false;
            }
        });
    }

    private static byte[] getClassFileBytesFromLayer(final Layer root, final String searchName, final ERuntimeEnvironmentType env) {
        final AdsNameEnvironment environment = ((AdsSegment) root.getAds()).getBuildPath().getPlatformLibs().getKernelLib(env).getAdsNameEnvironment();
        final byte[][] result = new byte[1][];
        environment.invokeRequest(new AdsNameEnvironment.NameRequest() {
            @Override
            public boolean accept(char[][] packageName, char[] className, AdsNameEnvironment.ClassDataProvider provider) {
                String name = String.valueOf(CharOperation.concatWith(packageName, className, '.'));
                if (Utils.equals(name, searchName)) {
                    result[0] = provider.getClassFileBytes();
                    return true;
                }
                return false;
            }
        });
        return result[0];
    }

    public static Map<String, EValType> collectLibraryEnumerations(final Layer root) {

        final Map<String, EValType> result = new HashMap<>();
        Layer.HierarchyWalker.walk(root, new Layer.HierarchyWalker.Acceptor<Object>() {
            @Override
            public void accept(Layer.HierarchyWalker.Controller<Object> controller, Layer prevLayer) {
                collectLibraryEnumerations(root, result);
            }
        });
        return result;
    }

    private static void collectLibraryEnumerations(final Layer root, final Map<String, EValType> list) {
        final AdsNameEnvironment environment = ((AdsSegment) root.getAds()).getBuildPath().getPlatformLibs().getKernelLib(ERuntimeEnvironmentType.COMMON).getAdsNameEnvironment();
        environment.invokeRequest(new AdsNameEnvironment.NameRequest() {
            @Override
            public boolean accept(char[][] packageName, char[] className, AdsNameEnvironment.ClassDataProvider provider) {
                if (provider != null) {
                    IBinaryType bt = provider.getAnswer().getBinaryType();
                    EValType valType;
                    if (bt != null && (valType = RadixPlatformEnum.getPlatformEnumType(bt)) != null) {
                        list.put(String.valueOf(CharOperation.concatWith(packageName, className, '.')), valType);
                    }
                }
                return false;
            }
        });
    }

    public static ReferenceBinding findPlatformClass(String name, final Layer root, final ERuntimeEnvironmentType env) {
        if (name == null) {
            return null;
        }
        name = name.replace('$', '.');
        String[] names = name.split("\\.");
        final char[][] compundTypeName = new char[names.length][];
        for (int i = 0; i < names.length; i++) {
            compundTypeName[i] = names[i].toCharArray();
        }
        Layer.HierarchyWalker.Acceptor<ReferenceBinding> acceptor = new Layer.HierarchyWalker.Acceptor<ReferenceBinding>() {
            @Override
            public void accept(Layer.HierarchyWalker.Controller<ReferenceBinding> controller, Layer prevLayer) {

                ReferenceBinding result = findPlatformClassInLayer(compundTypeName, prevLayer, env);
                if (result != null) {
                    controller.setResultAndStop(result);
                } else {
                    if (env.isClientEnv() && env != ERuntimeEnvironmentType.COMMON_CLIENT) {
                        result = findPlatformClassInLayer(compundTypeName, prevLayer, ERuntimeEnvironmentType.COMMON_CLIENT);
                        if (result != null) {
                            controller.setResultAndStop(result);
                        } else {
                            if (env != ERuntimeEnvironmentType.COMMON) {
                                result = findPlatformClassInLayer(compundTypeName, prevLayer, ERuntimeEnvironmentType.COMMON_CLIENT);
                                if (result != null) {
                                    controller.setResultAndStop(result);
                                }
                            }
                        }
                    }
                }

            }
        };
        return Layer.HierarchyWalker.walk(root, acceptor);
    }

    private static ReferenceBinding findPlatformClassInLayer(char[][] compoundTypeName, final Layer layer, ERuntimeEnvironmentType env) {
        final AdsWorkspace ws = ((AdsSegment) layer.getAds()).getBuildPath().getPlatformLibs().getKernelLib(env).getAdsWorkspace();
        PackageBinding packageBinding = ws.getLookupEnvironment().getPackage(new char[][]{compoundTypeName[0]});
        if (packageBinding == null) {
            return null;
        }
        int index = 1;
        while (packageBinding != null) {
            Binding next = packageBinding.getTypeOrPackage(compoundTypeName[index]);
            if (next instanceof PackageBinding) {
                index++;
                packageBinding = (PackageBinding) next;
            } else if (next instanceof ReferenceBinding) {
                ReferenceBinding type = (ReferenceBinding) next;
                for (int i = index + 1; i < compoundTypeName.length; i++) {
                    type = type.getMemberType(compoundTypeName[i]);
                    if (type == null) {
                        return null;
                    }
                }
                return type;
            } else {
                return null;
            }
        }
        return null;
    }

    public static EAccess getAccess(int modifiers) {
        if (Flags.isPublic(modifiers)) {
            return EAccess.PUBLIC;
        } else if (Flags.isProtected(modifiers)) {
            return EAccess.PROTECTED;
        } else if (Flags.isPrivate(modifiers)) {
            return EAccess.PRIVATE;
        } else {
            return EAccess.DEFAULT;
        }
    }
}
