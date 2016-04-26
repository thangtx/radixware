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

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.CharOperations;


public class AdsEnumType extends AdsDefinitionType {

    @Override
    protected void check(RadixObject referenceContext, ERuntimeEnvironmentType env, IProblemHandler problemHandler) {
        super.check(referenceContext, env, problemHandler);
    }

    public static class Array extends AdsEnumType {

        public Array(AdsEnumDef source) {
            super(source);
        }

        @Override
        public String getQualifiedName(RadixObject context) {
            return "Arr<" + super.getQualifiedName(context) + ">";
        }

        @Override
        public String getName() {
            return "Arr<" + super.getName() + ">";
        }
    }

    public static class Factory {

        public static final AdsEnumType newInstance(AdsEnumDef enumeration) {
            return new AdsEnumType(enumeration);
        }

        public static final AdsEnumType.Array newArrayInstance(AdsEnumDef enumeration) {
            return new AdsEnumType.Array(enumeration);
        }
    }

    public AdsEnumType(AdsEnumDef source) {
        super(source);
    }
    private static final char[][] NO_PACKAGE = new char[][]{"???".toCharArray()};
    private static final char[] NO_TYPE = "???".toCharArray();

    @Override
    public TypeJavaSourceSupport getJavaSourceSupport() {
        return new TypeJavaSourceSupport(this) {

            @Override
            public char[][] getPackageNameComponents(UsagePurpose env) {
                if (source == null) {
                    return NO_PACKAGE;
                }
                if (AdsEnumType.this instanceof AdsEnumType.Array) {
                    return JavaSourceSupport.getPackageNameComponents(source, UsagePurpose.getPurpose(ERuntimeEnvironmentType.COMMON, env.getCodeType()));
                }
                AdsEnumDef src = getSource();
                if (src == null) {
                    return new char[][]{"???".toCharArray()};
                } else if (src.isPlatformEnumPublisher() && !src.isExtendable() && env.getCodeType()==CodeType.EXCUTABLE) {
                    String name = src.getPublishedPlatformEnumName();
                    if (name == null) {
                        return NO_PACKAGE;
                    } else {
                        String[] names = name.split("\\.");
                        if (names.length == 0) {
                            return NO_PACKAGE;
                        } else {
                            char[][] result = new char[names.length - 1][];
                            for (int i = 0; i < names.length - 1; i++) {
                                result[i] = names[i].toCharArray();
                            }
                            return result;
                        }
                    }
                } else {
                    return JavaSourceSupport.getPackageNameComponents(source, UsagePurpose.getPurpose(ERuntimeEnvironmentType.COMMON, env.getCodeType()));
                }
            }

            @Override
            public char[] getLocalTypeName(UsagePurpose env) {
                if (AdsEnumType.this instanceof AdsEnumType.Array) {
                    return CharOperations.merge(source.getId().toCharArray(), "Arr".toCharArray(), '.');
                } else {
                    AdsEnumDef src = getSource();
                    if (src == null) {
                        return NO_TYPE;
                    } else {
                        if (env.isExecutable()) {
                            if (src.isPlatformEnumPublisher() && !src.isExtendable()) {
                                String name = src.getPublishedPlatformEnumName();
                                if (name == null) {
                                    return NO_TYPE;
                                } else {
                                    String[] names = name.split("\\.");
                                    if (names.length == 0) {
                                        return NO_TYPE;
                                    } else {
                                        return names[names.length - 1].toCharArray();
                                    }
                                }
                            } else {
                                return source.getId().toCharArray();
                            }
                        } else {
                            return CharOperations.merge(source.getId().toCharArray(), META_CLASS_SUFFIX);
                        }
                    }
                }
            }
        };
    }    

    @Override
    public AdsEnumDef getSource() {
        return (AdsEnumDef) source;
    }
}
