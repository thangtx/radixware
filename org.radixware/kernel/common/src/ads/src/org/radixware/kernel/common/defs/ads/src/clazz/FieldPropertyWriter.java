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

package org.radixware.kernel.common.defs.ads.src.clazz;

import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldPropertyDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassFieldsBindingProcessor;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.RadixType;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;


public class FieldPropertyWriter<T extends AdsFieldPropertyDef> extends AdsPropertyWriter<T> {

    @Override
    protected void writeAccessMethodName(CodePrinter printer) {
        if (def.getNature() == EPropNature.FIELD_REF) {
            printer.print("FieldEntity");
        } else {
            printer.print("Field");
        }
    }

    public FieldPropertyWriter(JavaSourceSupport support, T property, UsagePurpose usagePurpose) {
        super(support, property, usagePurpose);
    }

    private boolean writeJDBCAccessSuffix(EValType typeId, CodePrinter printer) {
        switch (typeId) {
            case INT:
                printer.print("Long");
                return true;
            case NUM:
                printer.print("BigDecimal");
                return true;
            case STR:
                printer.print("String");
                return true;
            case DATE_TIME:
                printer.print("Timestamp");
                return true;
            case BLOB:
                printer.print("Blob");
                return true;
            case CLOB:
                printer.print("Clob");
                return true;
            case BOOL:
                printer.print("Boolean");
                return true;
            case CHAR:
                printer.print("Char");
                return true;
            case BIN:
                printer.print("Bin");
                return true;
            default:
                return false;
        }
    }

    private boolean writeSimplePropRead(EValType typeId, CodePrinter printer) {
        printer.print("get");
        if (!writeJDBCAccessSuffix(typeId, printer)) {
            return false;
        }
        printer.print("(");
        printer.print(AdsSqlClassFieldsBindingProcessor.getFieldIndexVarName(def.getId()));
        printer.print(");\n");
        return true;
    }

    @Override
    protected boolean writeInitialization(CodePrinter printer) {
        printer.println("@SuppressWarnings(\"unused\")");
        printer.print("private int ");
        printer.print(AdsSqlClassFieldsBindingProcessor.getFieldIndexVarName(def.getId()));
        printer.println(";");
        return true;
    }

    @Override
    protected boolean writeStdGetterBody(CodePrinter printer) {
        EValType typeId = def.getValue().getType().getTypeId();
        if (typeId == null) {
            return false;
        }
        //if (ownerClass instanceof AdsCursorClassDef || ownerClass instanceof AdsReportClassDef) {
        if (def.getNature() == EPropNature.FIELD_REF) {
            printer.print("\t\treturn (");
            getTypeWriter().writeCode(printer);
            printer.print(")getFieldEntity(org.radixware.kernel.common.types.Id.Factory.loadFrom(\"");
            printer.print(propId);
            printer.print("\"));\n\t");
        } else {
            switch (typeId) {
                case INT:
                case CHAR:
                case STR:
                    AdsType type = def.getValue().getType().resolve(def).get();
                    if (type instanceof AdsEnumType) {
                        AdsType simpleType = RadixType.Factory.newInstance(typeId);
                        simpleType.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
                        printer.print(" x = ");
                        writeSimplePropRead(typeId, printer);
                        printer.println();
                        printer.print("return x == null ? null : ");
                        type.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
                        printer.print(".getForValue(x);\n");
                    } else {
                        if (!writeDefaultStdGetBodyForCursor(typeId, printer)) {
                            return false;
                        }
                    }
                    break;
                case ARR_BOOL: case ARR_INT:
                case ARR_CHAR: case ARR_STR:
                case ARR_NUM: case ARR_DATE_TIME:
                case ARR_BIN: case ARR_REF:
                    printer.print("return ");
                    printer.print("(");
                    getTypeWriter().writeCode(printer);
                    printer.print(") ");
                    printer.print("getArrObject");
                    printer.print("(");
                    printer.print(AdsSqlClassFieldsBindingProcessor.getFieldIndexVarName(def.getId()));
                    printer.print(", ");
                    printer.print(new String(WriterUtils.RADIX_VAL_TYPE_CLASS_NAME) + "." + typeId.toString());
                    printer.print(");\n");
                    return true;
                default:
                    if (!writeDefaultStdGetBodyForCursor(typeId, printer)) {
                        return false;
                    }
            }
        }
        return true;
    }

    private boolean writeDefaultStdGetBodyForCursor(EValType typeId, CodePrinter printer) {
        printer.print("return ");
        return writeSimplePropRead(typeId, printer);
    }

    @Override
    protected boolean writeStdSetterBody(CodePrinter printer) {
        AdsType type = def.getValue().getType().resolve(def).get();
        EValType typeId = def.getValue().getType().getTypeId();
        //final int sqlIndex = def.calcIndex();
        String sqlIndex = AdsSqlClassFieldsBindingProcessor.getFieldIndexVarName(def.getId());
        if (def.getNature() == EPropNature.FIELD_REF) {
            printer.print("\t\tsetFieldEntity(org.radixware.kernel.common.types.Id.Factory.loadFrom(\"");
            printer.print(propId);
            printer.print("\"),val);\n");
        } else {
            if ((typeId == EValType.INT || typeId == EValType.STR || typeId == EValType.CHAR) && (type instanceof AdsEnumType)) {
                printer.print("\n\t\tupdate");
                if (typeId == EValType.INT) {
                    printer.print("Long(");
                } else if (typeId == EValType.CHAR) {
                    printer.print("Char(");
                } else {
                    printer.print("String(");
                }

                printer.print(sqlIndex);
                printer.print(", val.");
                if (typeId == EValType.INT) {
                    printer.print("getValue().longValue()");
                } else /*if (typeId == EValType.CHAR) {
                 printer.print("getValue()");
                 } else*/ {
                    printer.print("getValue()");
                }
                printer.print(");\n");
            } else if (typeId == EValType.ARR_BOOL || typeId == EValType.ARR_INT || 
                    typeId == EValType.ARR_CHAR ||  typeId == EValType.ARR_STR || 
                    typeId == EValType.ARR_NUM || typeId == EValType.ARR_DATE_TIME || 
                    typeId == EValType.ARR_BIN || typeId == EValType.ARR_REF){
                printer.print("\n\t\tupdateArrObject");
                printer.print("(");
                printer.print(sqlIndex);
                printer.print(", val, ");
                printer.print(new String(WriterUtils.RADIX_VAL_TYPE_CLASS_NAME) + "." + typeId.toString());
                printer.print(");\n");
                
            } else {
                printer.print("\n\t\tupdate");
                if (!writeJDBCAccessSuffix(typeId, printer)) {
                    return false;
                }
                printer.print("(");
                printer.print(sqlIndex);
                printer.print(", val);\n");
            }
        }

        return true;
    }
}
