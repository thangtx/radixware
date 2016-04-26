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

package org.radixware.kernel.common.defs.ads.src.userfunc;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsMethodWriter;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.defs.ads.userfunc.IUserFuncDef;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagDbEntity;
import org.radixware.kernel.common.scml.CodePrinter;


public class AdsUserFuncWriter extends AbstractDefinitionWriter<AdsUserFuncDef> {

    public static boolean writeUfExecutable(IUserFuncDef ufDef, CodePrinter printer, UsagePurpose usagePurpose, JavaSourceSupport.CodeWriter outer) {
        final Definition def = (Definition) ufDef;
        WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
        WriterUtils.writeMetaAnnotation(printer, def, false);        
        printer.print("public final class ");
        printer.print(def.getId());
        printer.println('{');
        WriterUtils.writeServerArteAccessMethodDeclaration(def, JavaSourceSupport.CodeType.EXCUTABLE, printer);
        printer.println();
        printer.println("@SuppressWarnings(\"unused\")");
        printer.print("private static ");
        printer.print(JavaSourceSupport.RADIX_SERVER_TYPES_PACKAGE_NAME);
        printer.print('.');
        printer.print(JavaSourceSupport.RADIX_TYPE_ENTITY);
        printer.print(" ");
        printer.print(JmlTagDbEntity.UF_OWNER_FIELD_NAME);
        printer.printlnSemicolon();

        printer.enterBlock();


//
//        //write Owner load method
//        printer.print("private static ");
//        printer.print(ufDef.getOwnerClassName());
//        printer.println(" __load__owner__instance__ () {");
//        printer.println("   org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(");
//        WriterUtils.writeServerArteAccessMethodInvocation((AdsDefinition) ufDef, printer);
//        WriterUtils.writeIdUsage(printer, ufDef.getOwnerEntityId());
//        printer.print(",");
//        printer.printStringLiteral(ufDef.getOwnerPid());
//        printer.println(";");
//        printer.print("try{\n    return ");
//        printer.print("(");
//        printer.print(ufDef.getOwnerClassName());
//        printer.print(")");
//        WriterUtils.writeServerArteAccessMethodInvocation((AdsDefinition) ufDef, printer);
//        printer.println(".getEntityObject(pid,null,false);");
//
//        printer.println("}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){");
//        printer.println("return null;");
//        printer.println("}");
//        printer.println("}");


        //write uds wrappers
        if (ufDef instanceof AdsUserFuncDef) {
            final List<AdsDefinition> wrappers = ((AdsUserFuncDef) ufDef).getUsedWrappers();
            for (AdsDefinition wd : wrappers) {
                if (wd instanceof IJavaSource) {
                    ((IJavaSource) wd).getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
                }
            }
        }

        final AdsMethodDef method = ufDef.findMethod();
        //writeCustomMarker(method, printer, "UFM");
        AdsMethodWriter.printProfileErasure(outer, printer, method, def, true, true);
        printer.enterBlock();
        printer.println('{');
        printer.enterBlock(1);
        final Jml source = ufDef.getSource();
        if (source != null) {
            outer.writeCode(printer, source);
        } else {
            printer.println("throw new UnsupportedOperationException(\"NOT IMPLEMENTED\");");
        }
        printer.leaveBlock(1);
        printer.leaveBlock();
        printer.println();
        printer.println('}');
        printer.leaveBlock();
        printer.println();
        printer.println('}');
        return true;
    }

    public AdsUserFuncWriter(JavaSourceSupport support, AdsUserFuncDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    @Override
    protected boolean writeExecutable(CodePrinter printer) {
        return writeUfExecutable(def, printer, usagePurpose, this);
//        WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
//        printer.print("public final class ");
//        printer.print(def.getId());
//        printer.println('{');
//        WriterUtils.writeServerArteAccessMethodDeclaration(def, JavaSourceSupport.CodeType.EXCUTABLE, printer);
//        printer.enterBlock();
//        AdsMethodDef method = def.findMethod();
//        //writeCustomMarker(method, printer, "UFM");
//        AdsMethodWriter.printProfileErasure(this, printer, method, def, true, true);
//        printer.enterBlock();
//        printer.println('{');
//        printer.enterBlock(1);
//        Jml source = def.getSource();
//        if (source != null) {
//            writeCode(printer, source);
//        } else {
//            printer.println("throw new UnsupportedOperationException(\"NOT IMPLEMENTED\");");
//        }
//        printer.leaveBlock(1);
//        printer.leaveBlock();
//        printer.println();
//        printer.println('}');
//        printer.leaveBlock();
//        printer.println();
//        printer.println('}');
//        return true;
    }
}
