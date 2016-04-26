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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;


public abstract class RadixObjectWriter<T extends RadixObject & IJavaSource> extends CodeWriter {

    public static final char[] DEFINITION_LOCATOR_START_MARKER = "/*##RD-".toCharArray();
    public static final char[] DEFINITION_LOCATOR_END_MARKER = "RD##*/".toCharArray();
    protected final T def;

    protected void writeCustomMarker(Definition def, CodePrinter printer, String mark) {
        printer.print(DEFINITION_LOCATOR_START_MARKER);
        //printer.print(def.getId().toCharArray());

        Id[] path = def.getIdPath();
        if (path != null) {
            for (int i = 0; i < path.length; i++) {
                printer.print(path[i]);
                if (i < path.length - 1) {
                    printer.print('-');
                }
            }
        }
        printer.print(':');
        printer.print(mark);
        printer.print('-');
        printer.println(DEFINITION_LOCATOR_END_MARKER);
    }

    protected RadixObjectWriter(JavaSourceSupport support, T target, UsagePurpose usagePurpose) {
        super(support, usagePurpose);
        def = target;
    }

    private void writeSummary(CodePrinter printer) {
        printer.println();
        printer.print(JavaSourceSupport.MULTY_LINE_COMMENT_START);
        printer.print(def.getQualifiedName());
        printer.print('-');
        printer.print(def.getTypeTitle());
        printer.println(JavaSourceSupport.MULTY_LINE_COMMENT_END);
    }

    protected void writeLocator(CodePrinter printer) {
    }

    protected void writeLocator(CodePrinter printer, String marker) {
    }

    @Override
    public boolean writeCode(CodePrinter printer) {        
        writeSummary(printer);
        writeLocator(printer);
        switch (usagePurpose.getCodeType()) {
            case EXCUTABLE:
                return writeExecutable(printer);
            case META:
                return writeMeta(printer);
            case ADDON:
                return writeAddon(printer);
            default:
                return false;
        }
    }

    protected boolean writeExecutable(CodePrinter printer) {
        return false;
    }

    protected boolean writeMeta(CodePrinter printer) {
        return false;
    }

    protected boolean writeAddon(CodePrinter printer) {
        return false;
    }
}
