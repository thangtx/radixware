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
package org.radixware.kernel.common.defs.ads.clazz;

import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.RadixObjectWriter;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.schemas.adsdef.ClassDefinition;

public class AdsWrapperClassDef extends AdsSqlClassDef {

    public static final String END_OF_VERSION_INFO_MARKER = "<END_OF_VERSION_INFO>";
    private final EClassType srcClassType;
    private final ClassDefinition xClassDef;

    public AdsWrapperClassDef(ClassDefinition xClass) {
        super(xClass);
        xClassDef = xClass;
        srcClassType = xClass.getType();

        if (EDefinitionIdPrefix.USER_DEFINED_REPORT.equals(getId().getPrefix()) && xClassDef.getDescription() != null) {
            String description = xClassDef.getDescription();
            if (description.contains(END_OF_VERSION_INFO_MARKER)) {
                String newDescription = description.substring(description.indexOf(END_OF_VERSION_INFO_MARKER) + END_OF_VERSION_INFO_MARKER.length());
                if (!newDescription.isEmpty() && !"null".equals(newDescription)) {
                    setDescription(newDescription);
                } else {
                    setDescription("");
                }
            }
        }
    }

    @Override
    public String getTypeTitle() {
        if (EDefinitionIdPrefix.USER_DEFINED_REPORT.equals(getId().getPrefix())) {
            return "User Defined Report";
        }
        return super.getTypeTitle();
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        //add information about versions
        if (EDefinitionIdPrefix.USER_DEFINED_REPORT.equals(getId().getPrefix()) && xClassDef != null && xClassDef.getDescription() != null) {
            String description = xClassDef.getDescription();
            if (description.contains(END_OF_VERSION_INFO_MARKER)) {
                sb.append("<br>Versions: <br>&nbsp;").append(description.substring(0, description.indexOf(END_OF_VERSION_INFO_MARKER)));
            }
        }
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.DYNAMIC;
    }

    @Override
    public EDocGroup getDocGroup() {
        return EDocGroup.NONE;
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.SERVER);
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public JavaSourceSupport.CodeWriter getCodeWriter(JavaSourceSupport.UsagePurpose purpose) {
                return new WrapperClassCodeWriter(this, AdsWrapperClassDef.this, UsagePurpose.SERVER_EXECUTABLE);
            }
        }; //To change body of generated methods, choose Tools | Templates.
    }

    private static class WrapperClassCodeWriter extends RadixObjectWriter<AdsWrapperClassDef> {

        public WrapperClassCodeWriter(JavaSourceSupport support, AdsWrapperClassDef target, JavaSourceSupport.UsagePurpose usagePurpose) {
            super(support, target, usagePurpose);
        }

        @Override
        public void writeUsage(CodePrinter printer) {
            printer.print(def.getId());
        }

        @Override
        protected boolean writeExecutable(CodePrinter printer) {
            printer.print("@SuppressWarnings(\"unused\")");
            printer.print("private static final class ");
            printer.print(def.getId());
            if (EDefinitionIdPrefix.USER_DEFINED_REPORT.equals(def.getId().getPrefix())) {
                printer.println(" extends org.radixware.ads.mdlTSHSLPBGIJFDBPAOBIDEYAXWUI.server.pdcReport____________________ ");
            }
            printer.println("{");
            printer.print("private final Object wrappedInstance;");
            //write constructor
            printer.enterBlock();
            printer.print("public ");
            printer.print(def.getId());
            printer.println("(){");
            printer.enterBlock();

            if (def.getId() != null && def.getId().getPrefix() == EDefinitionIdPrefix.USER_DEFINED_REPORT) {
                printer.println("try {");
                printer.print("   wrappedInstance = ");
                WriterUtils.writeServerArteAccessMethodInvocation(def, printer);
                printer.print(".getDefManager().getClass(");
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.println(").newInstance();");
                printer.println("} catch (InstantiationException ex) {");
                printer.print("    throw new org.radixware.kernel.common.exceptions.DefinitionNotFoundError(");
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.println(", ex);");
                printer.println("} catch (IllegalAccessException ex) {");
                printer.print("    throw new org.radixware.kernel.common.exceptions.DefinitionNotFoundError(");
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.println(", ex);");
                printer.println("}");
            } else {
                printer.print("   wrappedInstance = ");
                WriterUtils.writeServerArteAccessMethodInvocation(def, printer);
                printer.print(".getDefManager().newClassInstance(");
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.println(", new Object[]{})");
            }
            printer.leaveBlock();
            printer.println("}");
            printer.leaveBlock();

            //write properties
            for (AdsPropertyDef prop : def.getProperties().get(ExtendableDefinitions.EScope.LOCAL)) {
                printer.print("public ");
                prop.getValue().getType().getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer, prop.getValue().getType(), prop);
                printer.print(" get");
                printer.print(prop.getName());
                printer.println("(){");
                writeMethodInvocation(printer, "get" + prop.getId(), null, prop.getValue().getType());
                printer.println("}");
                if (!prop.isReadOnly()) {
                    printer.print("public void set");
                    printer.print(prop.getName());
                    printer.print("(");
                    prop.getValue().getType().getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer, prop.getValue().getType(), prop);
                    printer.println(" val){");
                    writeMethodInvocation(printer, "get" + prop.getId(), Collections.singletonList(new AdsReportClassDef.ReportUtils.ParamInfo("val", prop.getValue().getType())), null);
                    printer.println("}");
                }
            }

            if (def.srcClassType == EClassType.REPORT) {
                //write wrapper for open and execute methods
                printer.println("public org.radixware.kernel.common.types.Id getId(){");
                writeMethodInvocation(printer, "getId", null, AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.types.Id"));
                printer.println('}');

                printer.println("public Object getGroupCondition(int groupIndex){");
                writeMethodInvocation(printer, "getGroupCondition", Collections.singletonList(new AdsReportClassDef.ReportUtils.ParamInfo("groupIndex", AdsTypeDeclaration.Factory.newPrimitiveType("int"))), AdsTypeDeclaration.Factory.newPlatformClass("Object"));
                printer.println('}');

                printer.println("public void assignParamValues(java.util.Map<org.radixware.kernel.common.types.Id, Object> paramId2Value){");
                writeMethodInvocation(printer, "assignParamValues", Collections.singletonList(new AdsReportClassDef.ReportUtils.ParamInfo("paramId2Value", AdsTypeDeclaration.Factory.newPlatformClass("java.util.Map<org.radixware.kernel.common.types.Id, Object>"))), null);
                printer.println('}');

                printer.println("public void open(java.util.Map<org.radixware.kernel.common.types.Id, Object> paramId2Value){");
                writeMethodInvocation(printer, "open", Collections.singletonList(new AdsReportClassDef.ReportUtils.ParamInfo("paramId2Value", AdsTypeDeclaration.Factory.newPlatformClass("java.util.Map<org.radixware.kernel.common.types.Id, Object>"))), null);
                printer.println('}');

                printer.println("public org.radixware.kernel.common.types.Id getContextParameterId(){");
                writeMethodInvocation(printer, "getContextParameterId", null, AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.types.Id"));
                printer.println('}');

                printer.println("public org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm createForm(){");
                writeMethodInvocation(printer, "createForm", null, AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm"));
                printer.println('}');
            }
            
            List<AdsMethodDef> local = def.getMethods().get(ExtendableDefinitions.EScope.LOCAL);
            for (AdsMethodDef method : def.getMethods().get(ExtendableDefinitions.EScope.ALL)) {
                if (method.getAccessFlags().getAccessMode() != EAccess.PUBLIC) {
                    continue;
                }
                List<AdsReportClassDef.ReportUtils.ParamInfo> info = new LinkedList<>();
                for (MethodParameter p : method.getProfile().getParametersList()) {
                    info.add(new AdsReportClassDef.ReportUtils.ParamInfo(p.getName(), p.getType()));
                }
                writeMethodDeclaration(printer, method.getName(), info, method.getProfile().getReturnValue().getType());
                printer.println('{');
                String mthName = local.contains(method) ? method.getId().toString() : method.getName();
                writeMethodInvocation(printer, mthName, info, method.getProfile().getReturnValue().getType());
                printer.println('}');
            }

            printer.println("}");
            return super.writeExecutable(printer); //To change body of generated methods, choose Tools | Templates.
        }

        private void writeMethodDeclaration(CodePrinter printer, String methodName, List<AdsReportClassDef.ReportUtils.ParamInfo> params, AdsTypeDeclaration resultType) {
            printer.print("public ");
            if (resultType != null && resultType != AdsTypeDeclaration.VOID) {
                resultType.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer, resultType, def);
            } else {
                printer.print("void");
            }
            printer.print(" ");
            printer.print(methodName);
            printer.print("(");
            boolean first = true;
            for (AdsReportClassDef.ReportUtils.ParamInfo info : params) {
                if (first) {
                    first = false;
                } else {
                    printer.printComma();
                }
                info.type.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer, info.type, def);
                printer.print(" ");
                printer.print(info.name);
            }
            printer.println(")");
        }

        private void writeMethodInvocation(CodePrinter printer, String methodName, List<AdsReportClassDef.ReportUtils.ParamInfo> params, AdsTypeDeclaration resultType) {
            printer.println("try {");
            printer.println("    try {");
            printer.print("         ");
            String primitivaTypeName = null;
            if (resultType != null && resultType != AdsTypeDeclaration.VOID) {
                printer.print("return (");
                final EValType typeId = resultType.getTypeId();
                if ((typeId == EValType.JAVA_TYPE) && (resultType.getExtStr() != null)) {
                    primitivaTypeName = resultType.getExtStr();
                    String type = calcType(primitivaTypeName);
                    printer.print("(");
                    printer.print(type);
                } else {
                    resultType.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer, resultType, def);
                }
                printer.print(") ");

            }
            printer.print("wrappedInstance.getClass().getMethod(\"" + methodName + "\"");

            if (params != null && params.size() > 0) {
                for (AdsReportClassDef.ReportUtils.ParamInfo a : params) {
                    printer.printComma();
                    if (a.type.getTypeId() == EValType.ARR_REF) {
                        CodePrinter type = CodePrinter.Factory.newJavaPrinter(printer);
                        a.type.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(type, a.type, def);
                        String strType = type.toString();
                        int index = strType.indexOf("<");
                        if (index != -1) {
                            strType = strType.substring(0, index);
                        }
                        printer.print(strType);
                    } else if (a.type.isGeneric()) {
                        a.type.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer, a.type.toRawType(), def);
                    } else {
                        a.type.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer, a.type, def);
                    }
                    printer.print(".class");
                }
            }
            printer.print(").invoke(wrappedInstance");
            if (params != null && params.size() > 0) {
                for (AdsReportClassDef.ReportUtils.ParamInfo a : params) {
                    printer.printComma();
                    printer.print(a.name);
                }
            }
            if (primitivaTypeName != null) {
                printer.print("))");
                printer.println("." + primitivaTypeName + "Value();");
            } else {
                printer.println(");");
            }

            printer.println("    } catch (IllegalAccessException ex) {");
            writeDynamicInvocationError(printer, methodName, def.getId().toString());
            printer.println("    } catch (IllegalArgumentException ex) {");
            writeDynamicInvocationError(printer, methodName, def.getId().toString());
            printer.println("    } catch (java.lang.reflect.InvocationTargetException ex) {");
            writeDynamicInvocationError(printer, methodName, def.getId().toString());
            printer.println("    }");
            printer.println("} catch (NoSuchMethodException ex) {");
            writeDynamicInvocationError(printer, methodName, def.getId().toString());
            printer.println("} catch (SecurityException ex) {");
            writeDynamicInvocationError(printer, methodName, def.getId().toString());
            printer.println("}");

        }

        private String calcType(String primitiveType) {
            switch (primitiveType) {
                case "int":
                    return "Integer";
                case "char":
                    return "Character";
                case "boolean":
                    return "Boolean";
                case "double":
                    return "Double";
                case "float":
                    return "Float";
                case "long":
                    return "Long";
                case "byte":
                    return "Byte";
                case "short":
                    return "Short";
                default:
                    return null;
            }
        }

        /* private EValType convertType(EValType valType,String extStr ) {
         switch (extStr) {
         case "char":
         valType = EValType.CHAR;
                    
         break;
         case "java.lang.String":
         valType = EValType.STR;
         break;
         case "byte":
         case "java.lang.Byte":
         valType = EValType.STR;
         break;
         case "int":
         case "long":
         case "short":
         case "java.lang.Integer":
         case "java.lang.Long":
         case "java.lang.Short":
         valType = EValType.INT;
         break;
         case "float":
         case "double":
         case "java.lang.Float":
         case "java.lang.Double":
         valType = EValType.NUM;
         break;
         case "boolean":
         //case "java.lang.Boolean":
         valType = EValType.BOOL;
         break;
         }
         return valType;
         }*/
        private void writeDynamicInvocationError(CodePrinter printer, String methodName, String classId) {
            printer.println("    throw new org.radixware.kernel.common.lang.DynamicInvocationError(\"Exception on dynamic invocation of method " + methodName + " of class " + classId + "\",ex);");
        }
    }
}
