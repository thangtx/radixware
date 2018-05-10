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

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.RadixObjectWriter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;

public class AdsLibUserFuncWrapper extends AdsMethodDef {

    private final String libName;
    private final String libUfName;
    //public static final String LIB_USERFUNC_PREFIX="luf";
    //public static final String LIB_USERFUNC_ID_SEPARATOR="*";
    //public static final String LIB_USERFUNC_NAME_SEPARATOR="$";

    public AdsLibUserFuncWrapper(AbstractMethodDefinition xMeth, String libName) {
        super(xMeth);
        this.libName = libName;
        libUfName = xMeth.getName();
    }

    public void updateProfile(AdsMethodDef.Profile newProfile) {
        if (newProfile.getParametersList() != null) {
            this.getProfile().getParametersList().clear();
            for (MethodParameter param : newProfile.getParametersList().list()) {
                this.getProfile().getParametersList().add(MethodParameter.Factory.newInstance(param));
            }
        }
        if (newProfile.getThrowsList() != null) {
            this.getProfile().getThrowsList().clear();
            for (AdsMethodThrowsList.ThrowsListItem throwItem : newProfile.getThrowsList().list()) {
                this.getProfile().getThrowsList().add(AdsMethodThrowsList.ThrowsListItem.Factory.newInstance(throwItem.getException()));
            }
        }
        this.getProfile().getReturnValue().setType(newProfile.getReturnValue().getType());
        this.getProfile().setName(newProfile.getName());
    }

    public boolean isEqualTo(String pid) {
        int index = pid.indexOf("~");
        if (index != -1) {
            String lName = pid.substring(0, index);
            String lUfName = pid.substring(index + 1);
            return libName.equals(lName) && libUfName.equals(lUfName);
        }
        return false;
    }

    @Override
    public String getTypeTitle() {
        return "Library User Function";
    }

    public String getLibName() {
        return libName;
    }

    public String getFuncName() {
        return libUfName;
    }

    public String calsDisplayMethodName() {
        return libName + "::" + libUfName;
    }

    @Override
    public EMethodNature getNature() {
        return EMethodNature.USER_DEFINED; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public JavaSourceSupport.CodeWriter getCodeWriter(JavaSourceSupport.UsagePurpose purpose) {
                return new AdsLibUserFuncWrapper.WrapperClassCodeWriter(this, AdsLibUserFuncWrapper.this, JavaSourceSupport.UsagePurpose.SERVER_EXECUTABLE);
            }
        }; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EDocGroup getDocGroup() {
        return EDocGroup.NONE;
    }
    
    private static class WrapperClassCodeWriter extends RadixObjectWriter<AdsLibUserFuncWrapper> {

        public WrapperClassCodeWriter(JavaSourceSupport support, AdsLibUserFuncWrapper target, JavaSourceSupport.UsagePurpose usagePurpose) {
            super(support, target, usagePurpose);
        }

        @Override
        public void writeUsage(CodePrinter printer) {
            printer.print(def.getId());
        }

        @Override
        protected boolean writeExecutable(CodePrinter printer) {
            //AdsMethodDef method=((AdsUserFuncDef)def).findMethod();
            List<AdsReportClassDef.ReportUtils.ParamInfo> info = new LinkedList<>();
            for (MethodParameter p : def.getProfile().getParametersList()) {
                info.add(new AdsReportClassDef.ReportUtils.ParamInfo(p.getName(), /*AdsTypeDeclaration.Factory.newPlatformClass("java.lang.Object")*/ p.getType()));
            }
            String methodName = def.getId().toString();///*def.isFreeForm() ? def.getName() : */def.getId().toString().replace("*", "_");
            AdsTypeDeclaration resultType = def.getProfile().getReturnValue().getType();
            AdsMethodThrowsList throwsList = def.getProfile().getThrowsList();
            writeMethodDeclaration(printer, methodName, info, resultType, throwsList);
            printer.println('{');
            printer.println("try{");
            if (resultType != AdsTypeDeclaration.VOID) {
                if (resultType.getTypeId() == EValType.JAVA_TYPE && resultType.getArrayDimensionCount() == 0) {
                    String primitivaTypeName = resultType.getExtStr();
                    String type = calcType(primitivaTypeName);
                    printer.print(type);
                    printer.print(" res= (");
                    printer.print(type);
                    printer.print(")");
                    writeCall(printer);
                    printer.print("return ");
                    //printer.print(type);
                    printer.println("res." + primitivaTypeName + "Value();");
                } else {
                    printer.print("return ");
                    printer.print("(");
                    resultType.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer, resultType, def);
                    printer.print(")");
                    writeCall(printer);

                }

            } else {
                writeCall(printer);
            }
            for (AdsMethodThrowsList.ThrowsListItem item : def.getProfile().getThrowsList()) {
                printer.print("}catch(");
                writeCode(printer, item.getException(), def);
                printer.println(" $$$e$$$){ throw $$$e$$$;");
            }
            printer.println("}catch(RuntimeException $$$e$$$){");
            printer.enterBlock();
            printer.println("throw (RuntimeException)$$$e$$$;");
            printer.leaveBlock();
            printer.println("}catch(Exception $$$e$$$){");
            printer.enterBlock();
            printer.println("throw new org.radixware.kernel.common.exceptions.AppError(\"Library function invocation error\",$$$e$$$);");
            printer.leaveBlock();
            printer.println("}");
            printer.println('}');
            return super.writeExecutable(printer);
        }

        private void writeCall(CodePrinter printer) {

            printer.enterBlock();
            printer.print("org.radixware.ads.mdlU7Z6KRE4ZBGKPHT5ZUVW4UBNMM.server.aecAM6HEX43SFCQTMG23G7JHOBCMA.mthM2Y5W7JZSZANXA5YMVJMJGSYJY(");
            printer.printStringLiteral(def.getId().toString());

            for (MethodParameter p : def.getProfile().getParametersList()) {
                printer.print(",");
                printer.print(p.getName());
            }
            printer.println(");");
            printer.leaveBlock();

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

        private void writeMethodDeclaration(CodePrinter printer, String methodName, List<AdsReportClassDef.ReportUtils.ParamInfo> params, AdsTypeDeclaration resultType, AdsMethodThrowsList throwsList) {
            printer.print("public static ");
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

            if (throwsList != null && throwsList.list().size() > 0) {
                printer.print(" throws ");
                first = true;
                for (AdsMethodThrowsList.ThrowsListItem info : throwsList) {
                    if (first) {
                        first = false;
                    } else {
                        printer.printComma();
                    }
                    AdsTypeDeclaration decl = info.getException();
                    if (decl == null) {
                        printer.printError();
                    } else {
                        info.getException().getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer, info.getException(), def);
                    }
                }
            }
        }
    }
}
