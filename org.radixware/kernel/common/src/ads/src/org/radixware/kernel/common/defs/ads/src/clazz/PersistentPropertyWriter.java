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

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentPropertyDef.ParentPath;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ColumnProperty;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;


abstract class PersistentPropertyWriter<T extends AdsPropertyDef> extends AdsPropertyWriter<T> {

    PersistentPropertyWriter(JavaSourceSupport support, T property, UsagePurpose usagePurpose) {
        super(support, property, usagePurpose);
    }

    static abstract class XmlConvertablePropertyWriter<T extends AdsPropertyDef> extends PersistentPropertyWriter<T> {

        XmlConvertablePropertyWriter(JavaSourceSupport support, T property, UsagePurpose usagePurpose) {
            super(support, property, usagePurpose);
        }

        @Override
        protected boolean writeExtendedValueConversionAfterGetFromSystemHandler(String varName, CodePrinter printer) {
            if (def.getValue().getType().getTypeId() == EValType.STR && def instanceof ColumnProperty) {
                DdsColumnDef c = ((ColumnProperty) def).getColumnInfo().findColumn();
                if (c == null) {
                    return false;
                }
                EValType realType = c.getValType();

                if (realType == EValType.CLOB) {
                    printer.print("if(");
                    printer.print(varName);
                    printer.print(" instanceof ");
                    getTypeWriter().writeCode(printer); 
                    printer.print("){ return (");
                    getTypeWriter().writeCode(printer);
                    printer.print(")");
                    printer.print(varName);
                    printer.println(";}");
                    printer.print("else if(");
                    printer.print(varName);
                    printer.println(" != null){");
                    printer.enterBlock();

                    printer.print(varName);
                    printer.print(" = ");
                    printer.print("org.radixware.kernel.server.utils.SrvValAsStr.toStr(");
                    WriterUtils.writeServerArteAccessMethodInvocation(def, printer);
                    printer.printComma();
                    printer.print(varName);
                    printer.printComma();
                    printer.print(new String(WriterUtils.RADIX_VAL_TYPE_CLASS_NAME) + "." + EValType.CLOB.name());
                    printer.println(");");
                    
                    printer.leaveBlock();
                    printer.println("}");
                    return true;
                }
            }
            if (def.getValue().getType().getTypeId() == EValType.XML && (def instanceof ColumnProperty || def instanceof AdsUserPropertyDef)) {
                EValType realType;
                if (def instanceof ColumnProperty) {
                    DdsColumnDef c = ((ColumnProperty) def).getColumnInfo().findColumn();
                    if (c == null) {
                        return false;
                    }
                    realType = c.getValType();
                } else {
                    realType = EValType.CLOB;
                }

                if (realType == EValType.CLOB || realType == EValType.BLOB) {
                    printer.print("if(");
                    printer.print(varName);
                    printer.print(" instanceof ");
                    getTypeWriter().writeCode(printer); 
                    printer.print("){ return (");
                    getTypeWriter().writeCode(printer);
                    printer.print(")");
                    printer.print(varName);
                    printer.println(";}");
                    printer.print("else if(");
                    printer.print(varName);
                    printer.println(" != null){");
                    printer.enterBlock();

                    printer.print("java.io.");
                    if (realType == EValType.BLOB) {
                        printer.print("InputStream");
                    } else {
                        printer.print("Reader");
                    }
                    printer.println(" stream = null;");
                    printer.println("try{");
                    printer.enterBlock();
                    
                    printer.print("if(");
                    if (realType == EValType.CLOB) {
                        printer.print("((java.sql.Clob)");
                    } else {
                        printer.print("((java.sql.Blob)");
                    }
                    printer.print(varName);
                    printer.println(").length() == 0) {");
                    printer.enterBlock();
                    printer.print(varName);
                    printer.print(" = ");
                    WriterUtils.writeNull(printer);
                    printer.printlnSemicolon();
                    printer.leaveBlock();
                    printer.println("} else {");
                    printer.enterBlock();
                    
                    printer.print("stream = ");
                    if (realType == EValType.CLOB) {
                        printer.print("((java.sql.Clob)");
                        printer.print(varName);
                        printer.println(").getCharacterStream();");
                    } else {
                        printer.print("((java.sql.Blob)");
                        printer.print(varName);
                        printer.println(").getBinaryStream();");
                    }
                    printer.print(varName);
                    printer.print(" = ");
                    getTypeWriter().writeCode(printer);

                    printer.println(".Factory.parse(stream,new org.apache.xmlbeans.XmlOptions().setCharacterEncoding(\"UTF-8\"));");
                    printer.print("refinePropVal(");
                    WriterUtils.writeAutoVariable(printer, propId);
                    printer.printComma();
                    printer.print(varName);
                    printer.println(",false);");
                    printer.leaveBlock();
                    printer.println("}");
                    printer.leaveBlock();
                    printer.println("}catch(java.sql.SQLException e){");
                    printer.println("   throw new org.radixware.kernel.server.exceptions.DatabaseError(\"Unable to parse xml data\",e);");
                    printer.print("}");
                    printer.println("catch(org.apache.xmlbeans.XmlException e){");
                    printer.println("   throw new org.radixware.kernel.common.exceptions.WrongFormatError(\"Unable to parse xml data\",e);");
                    printer.print("}");
                    printer.println("catch(java.io.IOException e){");
                    printer.println("   throw new org.radixware.kernel.common.exceptions.WrongFormatError(\"Unable to read xml data\",e);");
                    printer.print("}");
                    printer.println("finally{");
                    printer.println("    if(stream!=null){");
                    printer.println("       try{");
                    printer.println("           stream.close();");
                    printer.println("       }catch(java.io.IOException e){");
                    printer.println("       }");
                    printer.println("    }");
                    printer.println("}");
                    printer.leaveBlock();
                    printer.println("}");
                    return true;
                }
            }
            return super.writeExtendedValueConversionAfterGetFromSystemHandler(varName, printer);
        }

        @Override
        protected boolean isUseExtendedValueConversionBeforePassToSystemHandler() {
            return true;
        }

        @Override
        protected boolean writeExtendedValueConversionBeforePassToSystemHandler(String varName, CodePrinter printer) {

            return super.writeExtendedValueConversionAfterGetFromSystemHandler(varName, printer);
        }
    }

    static class RegularPropertyWriter<T extends AdsPropertyDef> extends XmlConvertablePropertyWriter<T> {

        RegularPropertyWriter(JavaSourceSupport support, T property, UsagePurpose usagePurpose) {
            super(support, property, usagePurpose);
        }

        @Override
        protected void writeAccessMethodName(CodePrinter printer) {
            printer.print("NativeProp");
        }
    }

    static class UserPropertyWriter<T extends AdsPropertyDef> extends XmlConvertablePropertyWriter<T> {

        UserPropertyWriter(JavaSourceSupport support, T property, UsagePurpose usagePurpose) {
            super(support, property, usagePurpose);
        }

        @Override
        protected void writeAccessMethodName(CodePrinter printer) {
            printer.print("UserProp");
        }
    }

    static class ParentPropertyWriter extends PersistentPropertyWriter<AdsParentPropertyDef> {

        ParentPropertyWriter(JavaSourceSupport support, AdsParentPropertyDef property, UsagePurpose usagePurpose) {
            super(support, property, usagePurpose);
        }

        private String writeParentCalculation(CodePrinter printer, boolean write) {
            ParentPath parentPath = def.getParentInfo().getParentPath();

            AdsClassDef propOwner = def.getOwnerClass();

            int counter = 0;
            String varName = "this";

            for (Id id : parentPath.getRefPropIds()) {
                AdsPropertyDef ref = propOwner.getProperties().findById(id, EScope.ALL).get();
                if (ref == null) {
                    return null;
                }
                //if (ref.getValue().getType().getTypeId() == EValType.PARENT_REF) {
                    AdsType type = ref.getValue().getType().resolve(getSupport().getCurrentRoot()).get();
                    if (type instanceof AdsClassType.EntityObjectType) {
                        propOwner = ((AdsClassType.EntityObjectType) type).getSource();
                        if (propOwner == null) {
                            return null;
                        }

                        type.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
                        printer.printSpace();
                        String newVarName = "p" + String.valueOf(counter);
                        counter++;
                        printer.print(newVarName);
                        printer.print('=');
                        printer.print(varName);
                        printer.print('.');
                        printer.print(id.toCharArray());
                        printer.printlnSemicolon();
                        printer.print("if(");
                        printer.print(newVarName);
                        printer.print("==null) return");
                        if (write) {
                            printer.printlnSemicolon();
                        } else {
                            printer.println(" null;");
                        }
                        varName = newVarName;
                    } else {
                        return null;
                    }
//                } else {
//                    return null;
//                }

            }

            return varName;
        }

        @Override
        protected boolean writeStdGetterBody(CodePrinter printer) {
            printer.println("try{");
            printer.enterBlock();
            String varName = writeParentCalculation(printer, false);
            if (varName != null) {
                printer.print("return ");
                printer.print(varName);
                printer.print(" == null ? null : ");
                printer.print(varName);
                printer.print('.');
                printer.print(def.getParentInfo().getOriginalPropertyId().toCharArray());
                printer.leaveBlock(1);
                printer.printlnSemicolon();


                printer.println("}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError ex){");
                printer.enterBlock();
                printer.println("return null;");
                printer.leaveBlock();
                printer.println("}");
                return true;
            } else {
                return false;
            }
        }

        
        @Override
        protected boolean writeStdSetterBody(CodePrinter printer) {
            String varName = writeParentCalculation(printer, true);
            if (varName != null) {
                printer.print(varName);
                printer.print('.');
                printer.print(def.getParentInfo().getOriginalPropertyId().toCharArray());
                printer.print('=');
                printer.print(TEXT_SETTER_PARAM_VARIABLE_NAME);
                printer.leaveBlock(1);
                printer.printlnSemicolon();
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void writeAccessMethodName(CodePrinter printer) {
            throw new IllegalUsageError("The method writeAccessMethodName() is not expected to use");
        }
    }
}
