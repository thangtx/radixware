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

import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsBaseObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsEdge;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsPin;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.scml.CodePrinter;


public class AdsEdgeWriter extends AdsBaseWriter<AdsEdge> {

    public AdsEdgeWriter(JavaSourceSupport support, AdsEdge edge, UsagePurpose usagePurpose) {
        super(support, edge, usagePurpose);
    }

    @Override
    protected void writeBody(CodePrinter printer) {
        AdsEdge edge = def;

        final String trace = String.valueOf(edge.getTraceSource());
        if (trace != null && trace.compareTo("") != 0) {
            printer.print("trace(");
            WriterUtils.writeEnumFieldInvocation(printer, edge.getTraceSeverity());
            printer.println(", ");

            printer.enterBlock();
            printer.println("new TraceProducer() {");

            printer.enterBlock();
            printer.println("public String getMessage() throws Throwable {");

            printer.enterBlock();
            writeCode(printer, edge.getTraceSource());
            printer.leaveBlock();

            printer.println();
            printer.println("}");
            printer.leaveBlock();

            printer.println("}.getMessage()");
            printer.leaveBlock();

            printer.println(");");
        }

        final AdsPin targetPin = edge.getTarget();
        final AdsBaseObject targetNode = (AdsBaseObject)targetPin.getOwnerDefinition();

        final AdsPin sourcePin = edge.getSource();
        final AdsBaseObject sourceNode = (AdsBaseObject)sourcePin.getOwnerDefinition();

        String debug = "";
        if (sourceNode.getName() != null && !sourceNode.getName().equals(""))
            debug += sourceNode.getName();
        debug += "(type: " + sourceNode.getTypeTitle() + ", id: " + sourceNode.getId() + ")";
        if (sourcePin.getName() != null && !sourcePin.getName().equals(""))
            debug += "::" + sourcePin.getName();
        debug += " - ";
        if (targetNode.getName() != null && !targetNode.getName().equals(""))
            debug += targetNode.getName();
        debug += "(type: " + targetNode.getTypeTitle() + ", id: " + targetNode.getId() + ")";

        printer.print("trace(");
        WriterUtils.writeEnumFieldInvocation(printer, EEventSeverity.DEBUG);
        printer.print(", ");
        printer.printStringLiteral(debug);
        printer.println(");");

        printer.println("return 0;");
    }
}
