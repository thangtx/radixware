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

package org.radixware.kernel.common.defs.ads.src.enumeration;

import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;


public class AdsEnumItemWriter extends AbstractDefinitionWriter<AdsEnumItemDef> {

    static final char[] ITEM_META_CLASS_NAME = CharOperations.merge(AdsEnumWriter.ENUM_META_CLASS_NAME, "Item".toCharArray(), '.');

    public AdsEnumItemWriter(JavaSourceSupport support, AdsEnumItemDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        writeCode(printer, def.getOwnerEnum().getType(def.getOwnerEnum().getItemType(), null));
        printer.print('.');
        if ((def.isVirtual() || def.isOverwrite()) && def.getPlatformItemName() != null && !def.getOwnerEnum().isExtendable()) {
            printer.print(def.getPlatformItemName());
        } else {
            super.writeUsage(printer);
        }
    }

    @Override
    public boolean writeMeta(CodePrinter printer) {

        printer.print("new ");
        printer.print(ITEM_META_CLASS_NAME);
        printer.print('(');
        WriterUtils.writeIdUsage(printer, def.getId());
        printer.printComma();
        printer.printStringLiteral(def.getName());
        printer.printComma();
        WriterUtils.writeRadixValAsStr(printer, def.getValue());
        printer.printComma();
        WriterUtils.writeIdUsage(printer, def.getOwnerDef().getId());
        printer.printComma();
        WriterUtils.writeIdUsage(printer, def.getTitleId());
        printer.printComma();
        WriterUtils.writeIdUsage(printer, def.getIconId());
        printer.printComma();
        printer.print(def.getViewOrder());
        printer.printComma();
        WriterUtils.writeIdArrayUsage(printer, def.getDomains().getDomaindIds());
        printer.printComma();
        printer.print(def.isDeprecated());        
        printer.printComma();        
        this.getSupport().getCodeWriter(UsagePurpose.getPurpose(usagePurpose.getEnvironment(), JavaSourceSupport.CodeType.EXCUTABLE)).writeUsage(printer);
        printer.print(')');
        /*public Item(final Id id, final String name, final ValAsStr valAsStr, final Id ownerId, final Id titleId)*/
        return true;
    }
}
