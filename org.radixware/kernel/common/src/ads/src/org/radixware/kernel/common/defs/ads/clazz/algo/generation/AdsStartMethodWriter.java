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

package org.radixware.kernel.common.defs.ads.clazz.algo.generation;

import org.radixware.kernel.common.defs.ads.src.clazz.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoStartMethodDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;


public class AdsStartMethodWriter extends AdsMethodWriter<AdsAlgoStartMethodDef> {

    public AdsStartMethodWriter(JavaSourceSupport support, AdsAlgoStartMethodDef method, UsagePurpose usagePurpose) {
        super(support, method, usagePurpose);
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        final AdsAlgoStartMethodDef m = (AdsAlgoStartMethodDef)def;
        printer.print(m.getSubNature().getName());
    }

    private String correctName(String name, EParamDirection direction) {
        if (direction.equals(EParamDirection.OUT) || direction.equals(EParamDirection.BOTH))
            return name + "[0]";
        return name;
    }

    @Override
    protected void writeBody(CodePrinter printer) {
        final AdsAlgoStartMethodDef m = (AdsAlgoStartMethodDef)def;
        printer.enterBlock();

        final AdsAlgoClassDef algo = (AdsAlgoClassDef)m.getOwnerClass();

        final CodePrinter before = CodePrinter.Factory.newJavaPrinter(printer);
        final CodePrinter after = CodePrinter.Factory.newJavaPrinter(printer);

        for (AdsAlgoClassDef.Param p: algo.getParams()) {
            final Id id = p.getId();
            final String name = p.getName();
            final AdsTypeDeclaration type = p.getType();
            final EParamDirection direction = p.getDirection();
            if (direction.equals(EParamDirection.IN) || direction.equals(EParamDirection.BOTH)) {
                if (direction.equals(EParamDirection.BOTH)) {
                    before.println("if (" + name + " != null && " + name + ".length > 0)");
                    before.enterBlock();
                }
                before.print("executor.setData(");
                WriterUtils.writeIdUsage(before, id);
                before.println(", " + correctName(name, direction) + ");");
                if (direction.equals(EParamDirection.BOTH)) {
                    before.leaveBlock();
                }
            }
            if (direction.equals(EParamDirection.OUT) || direction.equals(EParamDirection.BOTH)) {
                after.println("if (" + name + " != null && " + name + ".length > 0)");
                after.enterBlock();
                after.print(correctName(name, direction) + " = (");
                writeUsage(after, type, def);
                after.print(")executor.getData(");
                WriterUtils.writeIdUsage(after, id);
                after.println(");");
                after.leaveBlock();
            }
        }

        switch (m.getSubNature()) {
            case EXECUTE:
                printer.print("Arte arte = "); WriterUtils.writeServerArteAccessMethodInvocation(def, printer); printer.println(";");
                printer.print("AlgorithmExecutor executor = new AlgorithmExecutor(arte, null, null, true, ");
                WriterUtils.writeIdUsage(printer, algo.getId());
                printer.println(", getWorkingClassId(), clientData);");
                printer.print(before.getContents());
                printer.println("execute(executor);");
                printer.print(after.getContents());
                break;
            case STARTPROCESS:
                printer.print("Arte arte = "); WriterUtils.writeServerArteAccessMethodInvocation(def, printer); printer.println(";");
                printer.print("AlgorithmExecutor executor = new AlgorithmExecutor(arte, process, null, false, ");
                WriterUtils.writeIdUsage(printer, algo.getId());
                printer.println(", getWorkingClassId(), clientData);");
                printer.print(before.getContents());
                printer.println("boolean $result;");
                printer.println("try { ");
                printer.enterBlock();
                printer.println("$result = execute(executor);");
                printer.leaveBlock();
                printer.println("} catch (Throwable e) {");
                printer.enterBlock();
                printer.println("$result = false;");
                printer.leaveBlock();
                printer.println("};");
                printer.print(after.getContents());
                printer.println("return $result;");
                break;
            case START:
                printer.print("Arte arte = "); WriterUtils.writeServerArteAccessMethodInvocation(def, printer); printer.println(";");
                printer.print("AlgorithmExecutor executor = new AlgorithmExecutor(arte, null, type, false, ");
                WriterUtils.writeIdUsage(printer, algo.getId());
                printer.println(", getWorkingClassId(), clientData);");
                printer.print(before.getContents());
                printer.println("execute(executor);");
                printer.print(after.getContents());
                printer.print("return (");
                writeCode(printer, algo.getProcessType(), m);
                printer.println(")executor.process;");
                break;
        }

        printer.leaveBlock(1);
    }
}
