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

import java.util.HashMap;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.type.AdsType.TypeJavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsType.TypeJavaSourceSupport.TypeCodeWriter;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;

/**
 * Primitive java type representation
 *
 */
public final class JavaType extends FixedType {

    private static final JavaType BYTE = new JavaType(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_BYTE, EValType.INT, "Byte", "byteValue", "0");
    private static final JavaType BOOLEAN = new JavaType(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_BOOLEAN, EValType.BOOL, "Boolean", "booleanValue", "false");
    private static final JavaType CHAR = new JavaType(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_CHAR, EValType.CHAR, "Character", "charValue", "''");
    private static final JavaType DOUBLE = new JavaType(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_DOUBLE, EValType.NUM, "Double", "doubleValue", "0");
    private static final JavaType FLOAT = new JavaType(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_FLOAT, EValType.NUM, "Float", "floatValue", "0");
    private static final JavaType INT = new JavaType(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_INT, EValType.INT, "Integer", "intValue", "0");
    private static final JavaType LONG = new JavaType(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_LONG, EValType.INT, "Long", "longValue", "0");
    private static final JavaType SHORT = new JavaType(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_SHORT, EValType.INT, "Short", "shortValue", "0");
    private static final HashMap<String, JavaType> types = new HashMap<String, JavaType>();

    static {
        types.put(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_BYTE, BYTE);
        types.put(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_BOOLEAN, BOOLEAN);
        types.put(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_CHAR, CHAR);
        types.put(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_DOUBLE, DOUBLE);
        types.put(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_FLOAT, FLOAT);
        types.put(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_INT, INT);
        types.put(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_LONG, LONG);
        types.put(TypeJavaSourceSupport.PRIMITIVE_TYPE_NAME_SHORT, SHORT);
    }
    private char[] objectTypeName;
    private EValType conversionType;
    private char[] conversionFuncName;
    private final char[] defaultInitVal;

    private JavaType(String typeName, EValType conversionType, String objectTypeName, String conversionFuncName, String defaultInitVal) {
        super(typeName);
        this.objectTypeName = objectTypeName.toCharArray();
        this.conversionType = conversionType;
        this.conversionFuncName = conversionFuncName.toCharArray();
        this.defaultInitVal = defaultInitVal.toCharArray();
    }

    public EValType getConversionType() {
        return conversionType;
    }

    public static final class Factory {

        public static final JavaType forTypeName(String typeName) {
            return types.get(typeName);
        }
    }

    public class PrimitiveTypeJavaSourceSupport extends FixedNameSourceSupport {

        public class PrimitiveTypeCodeWriter extends TypeCodeWriter {

            private PrimitiveTypeCodeWriter(PrimitiveTypeJavaSourceSupport support, UsagePurpose purpose) {
                super(support, purpose);
            }

            public void writeConversionFromObjTypeCode(CodePrinter printer, String varName) {
                printer.print("((");
                printer.print(objectTypeName);
                printer.print(')');
                printer.print(varName);
                printer.print(").");
                printer.print(conversionFuncName);
                printer.print("()");
            }

            public void writeConversionToObjTypeCode(CodePrinter printer, String varName) {
                printer.print(objectTypeName);
                printer.print(".valueOf(");
                printer.print(varName);
                printer.print(")");
                /*                
                 printer.print("new ");
                 printer.print(objectTypeName);
                 printer.print('(');
                 printer.print(varName);
                 printer.print(")");
                 */
            }
        }

        public PrimitiveTypeJavaSourceSupport(String arg1) {
            super(arg1);
        }

        @Override
        public PrimitiveTypeCodeWriter getCodeWriter(UsagePurpose purpose) {
            return new PrimitiveTypeCodeWriter(this, purpose);
        }
    }

    @Override
    public TypeJavaSourceSupport getJavaSourceSupport() {
        return new PrimitiveTypeJavaSourceSupport(typeName);
    }

    public char[] getDefaultInitValAsCharArray() {
        return defaultInitVal;
    }
}
