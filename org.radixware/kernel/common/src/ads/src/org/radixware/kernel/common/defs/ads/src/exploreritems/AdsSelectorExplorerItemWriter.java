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

package org.radixware.kernel.common.defs.ads.src.exploreritems;

import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.EExplorerItemAttrInheritance;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.CodePrinter;


abstract class AdsSelectorExplorerItemWriter<T extends AdsSelectorExplorerItemDef> extends AdsExplorerItemWriter<T> {

    public AdsSelectorExplorerItemWriter(JavaSourceSupport support, T target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    @Override
    protected void writeInnerSpecific(ERuntimeEnvironmentType env, CodePrinter printer) {
        switch (env) {
            case SERVER:
                WriterUtils.writeIdUsage(printer, def.getClassId());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, def.getSelectorPresentationId());
                printer.printComma();
                printer.println();
                writeCode(printer, def.getCondition());
                printer.printComma();
                printer.println();
                break;
            case WEB:
            case EXPLORER:
            case COMMON_CLIENT:
                if (def.isTitleInherited()) {
                    WriterUtils.writeNull(printer);
                } else {
                    WriterUtils.writeIdUsage(printer, def.getTitleId());
                }
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, def.getClassId());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, def.getSelectorPresentationId());
                printer.printComma();
                printer.println();
                break;
        }
    }

    @Override
    protected void writeFooterSpecific(ERuntimeEnvironmentType env, CodePrinter printer) {

        switch (env) {
            case SERVER:
                if (def.isRestrictionInherited()) {
                    WriterUtils.writeNull(printer);
                } else {
                    writeCode(printer, def.getOwnRestrictions());
                }
                printer.printComma();
                printer.println();
                if (def.isClassCatalogInherited()) {
                    WriterUtils.writeNull(printer);
                } else {
                    WriterUtils.writeIdUsage(printer, def.getCreationClassCatalogId());
                }
                break;
            case WEB:
            case EXPLORER:
            case COMMON_CLIENT:
                writeCode(printer, def.getOwnRestrictions());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdArrayUsage(printer, def.getOwnRestrictions().getEnabledCommandIds());
                printer.printComma();
                printer.println();
                printer.print(EExplorerItemAttrInheritance.toBitField(def.getInheritanceMask()));
                printer.printComma();
                printer.print(def.isVisibleInTree());
                break;
        }

    }
}
