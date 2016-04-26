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

import java.util.ArrayList;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;


public class TypeSignature {

    public static String parseTypeName(char[] signature) {
        TypeResult res = new TypeResult();
        res.parseTypeName(signature, 0, false);
        return res.parsedType == null || res.parsedType.getTypeId() != EValType.JAVA_CLASS ? null : res.parsedType.getExtStr();
    }

    public static AdsTypeDeclaration parseTypeSignature(char[] className, char[] signature) {
        AdsTypeDeclaration decl = AdsTypeDeclaration.Factory.newPlatformClass(String.valueOf(className).replace('/', '.').replace('$', '.'));

        if (signature == null || signature.length == 0 || signature[0] != '<') {
            return decl;
        }
        return parseTypeSignature(decl, signature, 0).decl;
    }

    private static class Info {

        AdsTypeDeclaration decl;
        int nextPos;

        public Info(AdsTypeDeclaration decl, int nextPos) {
            this.decl = decl;
            this.nextPos = nextPos;
        }
    }

    private static Info parseTypeSignature(AdsTypeDeclaration decl, char[] signature, int start) {
        if (signature == null) {
            return new Info(decl, start);
        }
        boolean insideSignature = false;
        int argNameStart = -1;

        TypeResult res = new TypeResult();
        AdsTypeDeclaration.TypeArguments arguments = AdsTypeDeclaration.TypeArguments.Factory.newInstance(null);
        int i = start;
        AdsTypeDeclaration.TypeArgument.Derivation forceDerivation = null;
        for (; i < signature.length; i++) {
            char c = signature[i];
            if (insideSignature) {
                switch (c) {
                    case '>'://end of signature;
                        return new Info(decl.toGenericType(arguments), i);
                    case ':':
                    case 'L':
                    case 'T':
                        if (c == 'T') {
                            if (i + 1 < signature.length && signature[i + 1] == ':') {
                                continue;
                            }
                        }
                        int argNameLen = i - argNameStart;
                        i = res.parseTypeName(signature, c == 'L' || c == 'T' ? i : i + 1, insideSignature);
                        String argName = null;
                        if (argNameLen > 0) {
                            argName = String.valueOf(signature, argNameStart, argNameLen);
                        }
                        AdsTypeDeclaration.TypeArgument.Derivation derivation;
                        if (forceDerivation != null) {
                            derivation = forceDerivation;
                            forceDerivation = null;
                        } else {
                            derivation = argName == null ? AdsTypeDeclaration.TypeArgument.Derivation.NONE : AdsTypeDeclaration.TypeArgument.Derivation.EXTENDS;
                        }
                        AdsTypeDeclaration.TypeArgument arg = AdsTypeDeclaration.TypeArgument.Factory.newInstance(argName, res.parsedType, derivation);
                        arguments.add(arg);
                        argNameStart = i + 1;
                        break;
                    case '*':
                        arguments.add(AdsTypeDeclaration.TypeArgument.Factory.newInstance("?", AdsTypeDeclaration.Factory.newPlatformClass("java.lang.Object"), AdsTypeDeclaration.TypeArgument.Derivation.EXTENDS));
                        argNameStart = i + 1;
                        break;
                }
            } else {
                switch (c) {
                    case '<':
                        insideSignature = true;
                        if (signature.length > i + 1) {
                            switch (signature[i + 1]) {
                                case '+':
                                    argNameStart = i + 2;
                                    forceDerivation = AdsTypeDeclaration.TypeArgument.Derivation.EXTENDS;
                                    break;
                                default:
                                    argNameStart = i + 1;
                            }
                        }

                        break;
                }
            }

        }
        if (arguments.isEmpty()) {
            return new Info(decl, i);
        } else {
            return new Info(decl.toGenericType(arguments), i);
        }
    }

    private static class TypeResult {

        private AdsTypeDeclaration parsedType;

        private int parseTypeName(char[] source, int typeStart, boolean inArgument) {
            this.parsedType = null;
            int dimensionCount = 0;
            int objectTypeStart = -1;
            int objectTypeEnd = -1;
            int i = typeStart;
            boolean isArgumentType = false;


            for (; i < source.length; i++) {
                char c = source[i];
                if (objectTypeStart < 0) {
                    switch (c) {
                        case 'Z':
                            //boolean
                            parsedType = AdsTypeDeclaration.Factory.newPrimitiveType("boolean");

                            break;
                        case 'B':// byte
                            parsedType = AdsTypeDeclaration.Factory.newPrimitiveType("byte");

                            break;
                        case 'C'://	 char
                            parsedType = AdsTypeDeclaration.Factory.newPrimitiveType("char");

                            break;
                        case 'S'://	 short
                            parsedType = AdsTypeDeclaration.Factory.newPrimitiveType("short");

                            break;
                        case 'I'://	 int
                            parsedType = AdsTypeDeclaration.Factory.newPrimitiveType("int");

                            break;
                        case 'J'://	 long
                            parsedType = AdsTypeDeclaration.Factory.newPrimitiveType("long");

                            break;
                        case 'F'://	 float
                            parsedType = AdsTypeDeclaration.Factory.newPrimitiveType("float");

                            break;
                        case 'D'://	 double
                            parsedType = AdsTypeDeclaration.Factory.newPrimitiveType("double");

                            break;
                        case 'L':// fully-qualified-class ;	 fully-qualified-class
                            objectTypeStart = i + 1;
                            break;
                        case 'T':// fully-qualified-class ;	 fully-qualified-class
                            objectTypeStart = i + 1;
                            isArgumentType = true;
                            break;
                        case '[':// type	 type[]
                            dimensionCount++;
                            break;

                    }
                    if (parsedType != null) {
                        break;
                    }
                } else {
                    if (objectTypeEnd >= 0) {
                        i--;
                        break;
                    }
                    switch (c) {
                        case '<':
                            createRawType(source, objectTypeStart, i, isArgumentType);
                            Info info = parseTypeSignature(parsedType, source, i);
                            parsedType = info.decl;
                            i = info.nextPos;
                            break;
                        case '>':
                        case ';':
                            objectTypeEnd = i;
                            break;
                    }
                }
            }
            if (parsedType == null) {
                createRawType(source, objectTypeStart, objectTypeEnd, isArgumentType);
            }
            if (dimensionCount > 0) {
                parsedType = parsedType.toArrayType(dimensionCount);
            }
            return i;
        }

        private void createRawType(char[] source, int objectTypeStart, int objectTypeEnd, boolean isArgumentType) {
            if (objectTypeEnd >= 0) {
                String className = String.valueOf(source, objectTypeStart, objectTypeEnd - objectTypeStart).replace('/', '.').replace('$', '.');
                String normalClassName = className.replace('/', '.');
                EValType radixType = null;
                switch (normalClassName) {
                    case "java.lang.String":
                        radixType = EValType.STR;
                        break;
                    case "java.lang.Long":
                        radixType = EValType.INT;
                        break;
                    case "java.lang.Boolean":
                        radixType = EValType.BOOL;
                        break;
                    case "java.sql.Timestamp":
                        radixType = EValType.DATE_TIME;
                        break;
                    case "java.sql.Blob":
                        radixType = EValType.BLOB;
                        break;
                    case "java.sql.Clob":
                        radixType = EValType.CLOB;
                        break;
                    case "java.lang.Character":
                        radixType = EValType.CHAR;
                        break;
                    case "org.radixware.kernel.common.types.ArrBin":
                        radixType = EValType.ARR_BIN;
                        break;
                    case "org.radixware.kernel.common.types.ArrBool":
                        radixType = EValType.ARR_BOOL;
                        break;
                    case "org.radixware.kernel.common.types.ArrChar":
                        radixType = EValType.ARR_CHAR;
                        break;
                    case "org.radixware.kernel.common.types.ArrDateTime":
                        radixType = EValType.ARR_DATE_TIME;
                        break;
                    case "org.radixware.kernel.common.types.ArrInt":
                        radixType = EValType.ARR_INT;
                        break;
                    case "org.radixware.kernel.common.types.ArrNum":
                        radixType = EValType.ARR_NUM;
                        break;
                    case "org.radixware.kernel.common.types.ArrStr":
                        radixType = EValType.ARR_STR;
                        break;
                    case "org.radixware.kernel.common.types.Bin":
                        radixType = EValType.BIN;
                        break;
                }

                if (isArgumentType) {
                    parsedType = AdsTypeDeclaration.Factory.newTypeParam(normalClassName);
                } else {
                    if (radixType != null) {
                        parsedType = AdsTypeDeclaration.Factory.newInstance(radixType);
                    } else {
                        parsedType = AdsTypeDeclaration.Factory.newPlatformClass(normalClassName);
                    }
                }
            } else {
                parsedType = AdsTypeDeclaration.UNDEFINED;
            }
        }
    }

    public static AdsTypeDeclaration parseGenericTypeName(char[] typeSignature) {
        char[] erasure = Signature.getTypeErasure(typeSignature);
        int dim = Signature.getArrayCount(erasure);
        if (dim > 0) {
            erasure = Signature.getElementType(erasure);
        }
        AdsTypeDeclaration type;
        if (erasure.length == 1) {
            char c = erasure[0];
            switch (c) {
                case 'Z':
                    //boolean
                    type = AdsTypeDeclaration.Factory.newPrimitiveType("boolean");

                    break;
                case 'B':// byte
                    type = AdsTypeDeclaration.Factory.newPrimitiveType("byte");

                    break;
                case 'C'://	 char
                    type = AdsTypeDeclaration.Factory.newPrimitiveType("char");

                    break;
                case 'S'://	 short
                    type = AdsTypeDeclaration.Factory.newPrimitiveType("short");

                    break;
                case 'I'://	 int
                    type = AdsTypeDeclaration.Factory.newPrimitiveType("int");

                    break;
                case 'J'://	 long
                    type = AdsTypeDeclaration.Factory.newPrimitiveType("long");

                    break;
                case 'F'://	 float
                    type = AdsTypeDeclaration.Factory.newPrimitiveType("float");

                    break;
                case 'D'://	 double
                    type = AdsTypeDeclaration.Factory.newPrimitiveType("double");
                    break;
                case 'V'://	 double
                    type = AdsTypeDeclaration.VOID;
                    break;
                default:
                    type = AdsTypeDeclaration.Factory.newTypeParam(String.valueOf(c));
                    break;
            }
        } else {
            final char[] qualifier = Signature.getSignatureQualifier(erasure);
            final char[] name = Signature.getSignatureSimpleName(erasure);
            CharOperation.replace(name, '.', '$');
            erasure = CharOperation.concat(qualifier, name, '.');

            final String normalClassName = String.valueOf(erasure).replace('/', '.');
            EValType radixType = null;
            switch (normalClassName) {
                case "java.lang.String":
                    radixType = EValType.STR;
                    break;
                case "java.lang.Long":
                    radixType = EValType.INT;
                    break;
                case "java.lang.Boolean":
                    radixType = EValType.BOOL;
                    break;
                case "java.sql.Timestamp":
                    radixType = EValType.DATE_TIME;
                    break;
                case "java.sql.Blob":
                    radixType = EValType.BLOB;
                    break;
                case "java.sql.Clob":
                    radixType = EValType.CLOB;
                    break;
                case "java.lang.Character":
                    radixType = EValType.CHAR;
                    break;
                case "org.radixware.kernel.common.types.ArrBin":
                    radixType = EValType.ARR_BIN;
                    break;
                case "org.radixware.kernel.common.types.ArrBool":
                    radixType = EValType.ARR_BOOL;
                    break;
                case "org.radixware.kernel.common.types.ArrChar":
                    radixType = EValType.ARR_CHAR;
                    break;
                case "org.radixware.kernel.common.types.ArrDateTime":
                    radixType = EValType.ARR_DATE_TIME;
                    break;
                case "org.radixware.kernel.common.types.ArrInt":
                    radixType = EValType.ARR_INT;
                    break;
                case "org.radixware.kernel.common.types.ArrNum":
                    radixType = EValType.ARR_NUM;
                    break;
                case "org.radixware.kernel.common.types.ArrStr":
                    radixType = EValType.ARR_STR;
                    break;
                case "org.radixware.kernel.common.types.Bin":
                    radixType = EValType.BIN;
                    break;
            }
            if (radixType != null) {
                type = AdsTypeDeclaration.Factory.newInstance(radixType);
            } else {
                type = AdsTypeDeclaration.Factory.newPlatformClass(normalClassName);
            }
        }
        char[][] parameters = Signature.getTypeParameters(typeSignature);

        if (parameters != null && parameters.length > 0) {
            AdsTypeDeclaration.TypeArguments args = AdsTypeDeclaration.TypeArguments.Factory.newInstance(type);
            for (int i = 0; i < parameters.length; i++) {
                AdsTypeDeclaration decl = parseGenericTypeName(typeSignature);
                args.add(AdsTypeDeclaration.TypeArgument.Factory.newInstance(decl));
            }
            type = type.toGenericType(args);
        }
        if (dim > 0) {
            return type.toArrayType(dim);
        }
        return type;
    }

    public static AdsTypeDeclaration[] parseMethodSignature(char[] signature) {
        char[][] parameterTypes = Signature.getParameterTypes(signature);
        char[] returnType = Signature.getReturnType(signature);

        AdsTypeDeclaration[] result = new AdsTypeDeclaration[parameterTypes.length + 1];
        for (int i = 0; i < parameterTypes.length; i++) {
            result[i] = parseGenericTypeName(parameterTypes[i]);
        }
        result[parameterTypes.length] = parseGenericTypeName(returnType);

        return result;
//        
//        final ArrayList<AdsTypeDeclaration> decls = new ArrayList<>();
//        final TypeResult res = new TypeResult();
//        boolean inParams = false;
//        int inGenericArgs = 0;
//
//        loop:
//        for (int i = 0; i < signature.length; i++) {
//            char c = signature[i];
//            switch (c) {
//                case '('://enter params
//                    inParams = true;
//                    break;
//                case ')'://leave params
//                    inParams = false;
//                    break;
//                case 'V':
//                    if (!inParams) {
//                        decls.add(AdsTypeDeclaration.VOID);
//                        break loop;
//                    }
//                    break;
//                case '<':
//                    ++inGenericArgs;
//                    break;
//                case '>':
//                    --inGenericArgs;
//                    break;
//                default:
//                    if (inGenericArgs > 0) {
//                        break;
//                    }
//
//                    i = res.parseTypeName(signature, i, false);
//                    decls.add(res.parsedType);
//                    if (!inParams) {
//                        break loop;
//                    }
//
//            }
//        }
//        AdsTypeDeclaration[] arr = new AdsTypeDeclaration[decls.size()];
//        decls.toArray(arr);
//        return arr;
    }

    public static AdsTypeDeclaration parsePropertyType(char[] typeName) {
        TypeResult res = new TypeResult();

        res.parseTypeName(typeName, 0, false);
        return res.parsedType;
    }
}
