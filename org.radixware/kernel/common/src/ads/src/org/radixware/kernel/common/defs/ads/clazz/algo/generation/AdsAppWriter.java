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

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;


public class AdsAppWriter extends AdsBaseWriter<AdsAppObject> {

    private final static String INVOKE = "invoke";
    private final static String RESUME = "resume";

    public AdsAppWriter(JavaSourceSupport support, AdsAppObject app, UsagePurpose usagePurpose) {
        super(support, app, usagePurpose);
    }

    private void writeAppClass(CodePrinter printer) {
        if (!def.isUserObject())
            printer.print(def.getClazz());
        else {
            AdsClassDef app = def.getUserDef();
            assert app != null: "execution class is not found";
            writeUsage(printer, AdsTypeDeclaration.Factory.newInstance(app), def);
        }
    }

    @Override
    public boolean writeExecutable(CodePrinter printer) {
        if (!super.writeExecutable(printer))
            return false;
/*
        final Id id = def.getId();

        writeLocator(printer, PRE_EXECUTE_NAME);
        printer.println("private void executeBlock" + id + PRE_EXECUTE_NAME + "() throws java.lang.Throwable {");
        printer.enterBlock();

        writeCode(printer, def.getPreExecute());
        printer.println();

        printer.leaveBlock();
        printer.println("};");

        writeLocator(printer, POST_EXECUTE_NAME);
        printer.println("private void executeBlock" + id + POST_EXECUTE_NAME + "() throws java.lang.Throwable {");
        printer.enterBlock();

        writeCode(printer, def.getPostExecute());
        printer.println();

        printer.leaveBlock();
        printer.println("};");
*/
        return true;
    }

    @Override
    protected void writeBody(CodePrinter printer) {
        final Id id = def.getId();

        String invoke = INVOKE;
        String resume = RESUME;

        if (def.isUserObject()) {
            AdsClassDef app = def.getUserDef();
            //assert app != null: "execution class is not found";
            if (app!= null && app.getMethods() != null && app.getMethods().getLocal() != null)
                for (AdsMethodDef mth: app.getMethods().getLocal()) {
                    if (INVOKE.equals(mth.getName()))
                        invoke = mth.getId().toString();
                    if (RESUME.equals(mth.getName()))
                        resume = mth.getId().toString();
                }
        }

        printer.println("if (getCurrentThread().state == AlgorithmExecutor.Thread.ACTIVE) {");
        printer.enterBlock();
        writeCustomMarker(printer, AdsAppObject.PRE_EXECUTE_NAME);
        writeCode(printer, def.getPreExecute());
        printer.leaveBlock();
        printer.println();
        printer.println("};");

        printer.println("int output = -1;");
        printer.println("if (getCurrentThread().state == AlgorithmExecutor.Thread.ACTIVE) {");
        printer.enterBlock();
        printer.print("output = "); writeAppClass(printer); printer.println("." + invoke + "(this);");
        printer.println("if (output < 0) {");
        printer.enterBlock();
        printer.println("getCurrentThread().state = AlgorithmExecutor.Thread.WAITING;");
        printer.println("return output;");
        printer.leaveBlock();
        printer.println("}");
        printer.leaveBlock();
        printer.println("} else if (getCurrentThread().state == AlgorithmExecutor.Thread.RESUME) {");
        printer.enterBlock();
        printer.print("output = "); writeAppClass(printer); printer.println("." + resume + "(this);");
        
        printer.println("if (output < 0) {");
        printer.enterBlock();
        
        writeCustomMarker(printer, AdsAppObject.OVERDUE_EXECUTE_NAME);
        writeCode(printer, def.getOnOverdue());
        printer.println();
        printer.println("getCurrentThread().state = AlgorithmExecutor.Thread.WAITING;");
        printer.println("return output;");
        
        printer.leaveBlock();
        printer.println("} else");
        
        printer.enterBlock();
        printer.println("getCurrentThread().state = AlgorithmExecutor.Thread.ACTIVE;");
        printer.leaveBlock();
        
        printer.leaveBlock();
        printer.println("}");

        printer.println("if (getCurrentThread().state == AlgorithmExecutor.Thread.ACTIVE) {");
        printer.enterBlock();
        writeCustomMarker(printer, AdsAppObject.POST_EXECUTE_NAME);
        writeCode(printer, def.getPostExecute());
        printer.leaveBlock();
        printer.println();
        printer.println("};");

        printer.println("return output;");
    }
}
