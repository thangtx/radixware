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

package org.radixware.kernel.common.defs.ads.src.clazz.sql;

import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.createBlock;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.createMessageSend;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsMethodWriter;
import org.radixware.kernel.common.scml.CodePrinter;


public class AdsSqlClassExecuteReportWriter extends AdsMethodWriter<AdsMethodDef> {

    private final AdsReportClassDef report;

    public AdsSqlClassExecuteReportWriter(JavaSourceSupport support, AdsMethodDef method, JavaSourceSupport.UsagePurpose usagePurpose) {
        super(support, method, usagePurpose);
        this.report = (AdsReportClassDef) method.getOwnerClass();
    }

    @Override
    protected void writeBody(CodePrinter cp) {
        cp.enterBlock();

        cp.println("try {");
        cp.enterBlock();
        cp.print(AdsSystemMethodDef.ID_REPORT_OPEN.toString() + "(");

        boolean paramPrinted = false;
        for (AdsParameterPropertyDef param : report.getInputParameters()) {
            if (paramPrinted) {
                cp.print(", ");
            }
            cp.print(param.getName());
            paramPrinted = true;
        }

        cp.println(");");
        cp.println("export(stream, format);");
        cp.leaveBlock();
        cp.println("} finally {");
        cp.println("\tclose();");
        cp.println("}");

        cp.leaveBlock();
    }

    public static Statement[] createStatements(AdsReportClassDef report) {
        TryStatement tryStmt = new TryStatement();
        MessageSend callOpenReport = createMessageSend(ThisReference.implicitThis(), AdsSystemMethodDef.ID_REPORT_OPEN.toCharArray());
        callOpenReport.arguments = new Expression[report.getInputParameters().size()];
        for (int i = 0; i < callOpenReport.arguments.length; i++) {
            callOpenReport.arguments[i] = SqlClassVariables.nameRef(report.getInputParameters().get(i).getName());
        }

        MessageSend callExport = createMessageSend(ThisReference.implicitThis(), "export", SqlClassVariables.nameRef("stream"), SqlClassVariables.nameRef("format"));
        tryStmt.tryBlock = createBlock(callOpenReport, callExport);
        tryStmt.finallyBlock = createBlock(createMessageSend(ThisReference.implicitThis(), "close"));

        return new Statement[]{tryStmt};
    }
}