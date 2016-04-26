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

package org.radixware.kernel.common.defs.ads.clazz.enumeration;

import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsClassWriter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public class AdsEnumClassWriter extends AdsClassWriter<AdsEnumClassDef> {

    public AdsEnumClassWriter(JavaSourceSupport support, AdsEnumClassDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    private boolean writeConstructors(CodePrinter printer, List<AdsFieldParameterDef> params) {

        if (params.size() > 0) {
            printer.print("private ");
            writeUsage(printer);
            printer.print(" (");

            boolean first = true;
            for (AdsFieldParameterDef param : params) {
                if (first) {
                    first = false;
                } else {
                    printer.printComma();
                    printer.printSpace();
                }

                AdsTypeDeclaration type = param.getValue().getType();
                getCodeWriter(type, def).writeCode(printer);

                printer.printSpace();
                printer.print(param.getId());
            }
            printer.print(") {");
            printer.enterBlock();

            for (AdsFieldParameterDef param : params) {
                printer.print("\nthis.");
                printer.print(param.getId());
                printer.print("$Val = ");
                printer.print(param.getId());
                printer.print(";");
            }

            printer.println();
            printer.leaveBlock();
            printer.println('}');
        }
        return true;
    }

    private boolean writeProperties(CodePrinter printer, List<AdsFieldParameterDef> params) {

        if (params.size() > 0) {
            for (AdsFieldParameterDef param : params) {

                AdsTypeDeclaration type = param.getValue().getType();

                printer.print("private final ");
                getCodeWriter(type, def).writeCode(printer);
                printer.printSpace();
                printer.print(param.getId());
                printer.print("$Val");
                printer.printlnSemicolon();

                printer.print("public ");
                getCodeWriter(type, def).writeCode(printer);
                printer.print(" get");
                printer.print(param.getId());
                printer.print(" () { return ");
                printer.print(param.getId());
                printer.println("$Val; }");
                printer.println();
            }
        }
        return true;
    }

    @Override
    protected void writeClassDefinitionMethod(CodePrinter printer) {
        printer.print(TEXT_ENUM);
    }

    @Override
    protected void writeSuperInterfaceUsageMethod(CodePrinter printer) {
        printer.print(TEXT_IMPLEMENTS);
    }

    @Override
    protected void writeExecutableBody(CodePrinter printer) {
        writeCode(printer, def.getFields());
//        writeCode(printer, def.getFieldStruct());
        List<AdsFieldParameterDef> params = def.getFieldStruct().getOrdered();
        if (!params.isEmpty()) {
            writeProperties(printer, params);
            writeConstructors(printer, params);
        }
        super.writeExecutableBody(printer);
    }
}