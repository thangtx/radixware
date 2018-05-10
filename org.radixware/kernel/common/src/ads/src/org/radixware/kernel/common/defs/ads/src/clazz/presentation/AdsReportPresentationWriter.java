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
package org.radixware.kernel.common.defs.ads.src.clazz.presentation;

import java.util.Set;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ReportPresentations;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.enums.EPresentationAttrInheritance;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;

public class AdsReportPresentationWriter extends AbstractFormPresentationWriter<ReportPresentations> {

    public static final char[] EXPLORER_META_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadReportPresentationDef".toCharArray(), '.');

    AdsReportPresentationWriter(final JavaSourceSupport support, final ReportPresentations presentations, final UsagePurpose usagePurpose) {
        super(support, presentations, usagePurpose);
    }

    @Override
    public char[] getExplorerMetaClassName() {
        return EXPLORER_META_CLASS_NAME;
    }

    @Override
    protected boolean writeBaseFormId(final AdsClassDef thisClazz, final CodePrinter printer) {
        return true;
    }

    @Override
    protected boolean writeInheritanceMask(final CodePrinter printer, final Set<EPresentationAttrInheritance> mask) {
        return true;
    }

    @Override
    protected boolean writeContextMeta(AdsClassDef thisClazz, CodePrinter printer) {
        final AdsReportClassDef report = (AdsReportClassDef) thisClazz;

        // context parameter id
        printer.printComma();
        printer.println();
        WriterUtils.writeIdUsage(printer, report.getContextParameterId());

        // context class id
        printer.printComma();
        printer.println();
        WriterUtils.writeIdUsage(printer, report.getContextClassId());

        // is subreport
        printer.printComma();
        printer.println();
        printer.print(report.isSubreport());

        // description
        printer.printComma();
        printer.println();
        printer.printStringLiteral(report.getDescription());

        // is export to csv allowed
        printer.printComma();
        printer.println();
        printer.print(report.getCsvInfo() == null ? false : report.getCsvInfo().isExportToCsvEnabled());
        printer.printComma();
        printer.print(report.getXlsxReportInfo() == null ? false : report.getXlsxReportInfo().isExportToXlsxEnabled());
        printer.printComma();
        printer.print(report.getForm().isSupportsTxt());
        printer.printComma();
        printer.println();
        printer.print(report.getForm().getMultifileGroupLevel() >= 0);

        return true;
    }

    @Override
    protected int getBits() {
        final AdsReportClassDef report = (AdsReportClassDef) ownerClass;
        int bits = 0;
        if (report.walkTwiceOnExport()) {
            bits |= 0x2;
        }
        if (report.getForm().getMultifileGroupLevel() >= 0) {
            bits |= 0x1;
        }
        return bits;
    }

    @Override
    protected boolean writeCustomBody(CodePrinter printer) {
        printer.println('{');
        printer.enterBlock();
        printer.println("@Override");
        printer.println("protected org.radixware.kernel.common.client.models.Model createModelImpl(final org.radixware.kernel.common.client.IClientEnvironment environment) {");
        printer.enterBlock();
        printer.print("return new ");
        AdsType decl = def.getModel().getType(EValType.USER_CLASS, null);
        decl.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
        printer.println("(environment,this);");
        printer.leaveBlock();
        printer.println('}');
        printer.leaveBlock();
        printer.print('}');
        return true;
    }
}
