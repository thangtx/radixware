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

package org.radixware.kernel.common.defs.ads.common;

import java.util.Arrays;
import java.util.HashMap;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeType;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArgument;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArguments;
import org.radixware.kernel.common.defs.ads.type.ArgumentType;


public class JavaSignatures {

    private static final HashMap<String, char[]> primitives = new HashMap<>();
    private static final char[] NO_SIGN = "Lerror;".toCharArray();
    private static final char[] VOID = "V".toCharArray();
    private static final char[] INT = "Ljava.lang.Long;".toCharArray();
    private static final char[] CHAR = "Ljava.lang.Character;".toCharArray();
    private static final char[] STR = "Ljava.lang.String;".toCharArray();
    private static final char[] NUM = "Ljava.math.BigDecimal;".toCharArray();
    private static final char[] BOOL = "Ljava.lang.Boolean;".toCharArray();
    private static final char[] INIT = "<init>".toCharArray();
    private static final char[] EMPTY = "".toCharArray();

    static {
        primitives.put("boolean", new char[]{'Z'});
        primitives.put("byte", new char[]{'B'});
        primitives.put("char", new char[]{'C'});
        primitives.put("short", new char[]{'S'});
        primitives.put("int", new char[]{'I'});
        primitives.put("long", new char[]{'J'});
        primitives.put("float", new char[]{'F'});
        primitives.put("double", new char[]{'D'});
    }

    public static char[] generateSignature(AdsClassDef context, AdsPropertyDef property) {
        final char[] name;
        final char[] result = generateSignature(context, property.getValue().getType());

        AdsPropertyDef ovr = property;
        AdsPropertyDef test = property;

        while (ovr != null) {
            ovr = ovr.getHierarchy().findOverridden().get();
            if (ovr != null) {
                test = ovr;
            }
        }
        name = test instanceof AdsTransparentPropertyDef ? ((AdsTransparentPropertyDef) test).getPublishedPropertyName() : test.getId().toCharArray();

        final char[] signature = CharOperation.concat(name, result);

        return signature;
    }

    public static char[] generateSignature(AdsClassDef context, AdsMethodDef method, boolean withName) {
        char[] name;

        char[] result;
        AdsMethodDef.Profile profile = method.getProfile();
        if (method.isConstructor()) {
            if (withName) {
                name = INIT;
            } else {
                name = EMPTY;
            }

            result = VOID;
        } else {
            if (withName) {
                AdsMethodDef ovr = method;
                AdsMethodDef test = method;
                while (ovr != null) {
                    ovr = ovr.getHierarchy().findOverridden().get();
                    if (ovr != null) {
                        test = ovr;
                    }
                }


                name = test instanceof AdsTransparentMethodDef ? ((AdsTransparentMethodDef) test).getPublishedMethodName() : test.getId().toCharArray();
            } else {
                name = EMPTY;
            }
            result = generateSignature(context, method.getProfile().getReturnValue().getType());
        }
        int signatureSize = name.length + result.length;
        AdsMethodParameters methodParams = profile.getParametersList();
        char[][] params = new char[methodParams.size()][];
        for (int i = 0; i < params.length; i++) {
            params[i] = generateSignature(context, methodParams.get(i).getType());
            signatureSize += params[i].length;
        }
        char[] signature = new char[signatureSize + 2];
        System.arraycopy(name, 0, signature, 0, name.length);
        int dest = name.length;
        signature[dest] = '(';
        dest++;
        for (int i = 0; i < params.length; i++) {
            System.arraycopy(params[i], 0, signature, dest, params[i].length);
            dest += params[i].length;
        }
        signature[dest] = ')';
        dest++;
        if (result.length > 0) {
            System.arraycopy(result, 0, signature, dest, result.length);
        }
        return signature;
    }

    public static char[] checkArray(char[] sign, int dimCount) {
        if (dimCount == 0) {
            return sign;
        } else {
            char[] arr = new char[dimCount];
            Arrays.fill(arr, '[');
            return CharOperation.concat(arr, sign);
        }
    }

    public static char[] generateSignature(AdsDefinition referenceContext, AdsTypeDeclaration decl) {
        if (decl == AdsTypeDeclaration.VOID) {
            return VOID;
        }
        if (decl == AdsTypeDeclaration.UNDEFINED) {
            return NO_SIGN;
        }
        if (!decl.isTypeArgument()) {
            switch (decl.getTypeId()) {
                case JAVA_CLASS:
                    StringBuilder main = new StringBuilder();
                    main.append('L');
                    main.append(decl.getExtStr());
                    TypeArguments args = decl.getGenericArguments();
                    if (args != null && !args.isEmpty()) {
                        StringBuilder builder = new StringBuilder();
                        for (TypeArgument a : args.getArgumentList()) {
                            if (a.getType() == null) {//unbounded wildcard
                                builder.append("<*>");
                            } else {
                                char[] paramSign = generateSignature(referenceContext, a.getType());
                                if (paramSign != null) {
                                    builder.append(paramSign);
                                }
                            }
                        }
                        if (builder.length() > 0) {
                            main.append('<');
                            main.append(builder);
                            main.append(">;");
                        }
                    } else {
                        main.append(';');
                    }
                    return checkArray(main.toString().toCharArray(), decl.getArrayDimensionCount());
                case JAVA_TYPE:
                    char[] res = primitives.get(decl.getExtStr());
                    return res == null ? NO_SIGN : checkArray(res, decl.getArrayDimensionCount());
                case INT:
                    if (decl.getExtStr() == null && decl.getPath() == null) {
                        return checkArray(INT, decl.getArrayDimensionCount());
                    }
                    break;
                case CHAR:
                    if (decl.getExtStr() == null && decl.getPath() == null) {
                        return checkArray(CHAR, decl.getArrayDimensionCount());
                    }
                    break;
                case STR:
                    if (decl.getExtStr() == null && decl.getPath() == null) {
                        return checkArray(STR, decl.getArrayDimensionCount());
                    }
                    break;

                case BOOL:
                    return checkArray(BOOL, decl.getArrayDimensionCount());
                case NUM:
                    return checkArray(NUM, decl.getArrayDimensionCount());
            }
        }
        AdsType type = decl.resolve(referenceContext).get();
        if (type == null) {
            return NO_SIGN;
        } else if (type != null) {
            AdsType.TypeJavaSourceSupport support = type.getJavaSourceSupport();
            char[] typeName = support.getQualifiedTypeName(UsagePurpose.getPurpose(referenceContext.getUsageEnvironment(), CodeType.EXCUTABLE), false);
            char[] result = new char[typeName.length + 2];
            System.arraycopy(typeName, 0, result, 1, typeName.length);
            result[0] = type instanceof ArgumentType ? 'T' : 'L';
            result[result.length - 1] = ';';
            return checkArray(result, decl.getArrayDimensionCount());
        } else {
            return NO_SIGN;
        }
    }

    public static boolean isCorrectJavaIdentifier(String identifier) {
        if (identifier != null && !identifier.isEmpty()) {
            if (Character.isJavaIdentifierStart(identifier.charAt(0))) {
                for (int i = 1; i < identifier.length(); ++i) {
                    if (!Character.isJavaIdentifierPart(identifier.charAt(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
