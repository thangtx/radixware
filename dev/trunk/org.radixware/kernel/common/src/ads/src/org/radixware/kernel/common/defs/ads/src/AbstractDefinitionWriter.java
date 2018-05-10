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

package org.radixware.kernel.common.defs.ads.src;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;
import org.radixware.kernel.common.types.Id;


public abstract class AbstractDefinitionWriter<T extends Definition & IJavaSource> extends RadixObjectWriter<T> {

    protected AbstractDefinitionWriter(JavaSourceSupport support, T target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    protected void writeCustomMarker(CodePrinter printer, String mark) {
        super.writeCustomMarker(def, printer, mark);
    }

    protected String getLocatorMarker() {
        return null;
    }

    

    @Override
    protected final void writeLocator(CodePrinter printer) {
        writeLocator(printer, getLocatorMarker());
    }

    @Override
    protected final void writeLocator(CodePrinter printer, String marker) {
        WriterUtils.enterHumanUnreadableBlock(printer);
        printer.print(DEFINITION_LOCATOR_START_MARKER);
        Id[] path = def.getIdPath();
        if (path != null) {
            for (int i = 0; i < path.length; i++) {
                printer.print(path[i]);
                if (i < path.length - 1) {
                    printer.print('-');
                }
            }
        }
        if (marker != null) {
            printer.print(":");
            printer.print(marker);

        }
        printer.print('-');
        printer.println(DEFINITION_LOCATOR_END_MARKER);
        WriterUtils.leaveHumanUnreadableBlock(printer);
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        printer.print(JavaSourceSupport.getName(def, printer instanceof IHumanReadablePrinter));
    }
}
