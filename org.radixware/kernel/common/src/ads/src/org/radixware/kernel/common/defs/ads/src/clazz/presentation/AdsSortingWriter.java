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

import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef.OrderBy;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.EOrder;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;


public class AdsSortingWriter extends AbstractDefinitionWriter<AdsSortingDef> {

    public AdsSortingWriter(JavaSourceSupport support, AdsSortingDef sorting, UsagePurpose usagePurpose) {
        super(support, sorting, usagePurpose);
    }
    static final char[] SORTING_META_SERVER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_SERVER_PACKAGE_NAME, "RadSortingDef".toCharArray(), '.');
    static final char[] SORTING_META_EXPLORER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadSortingDef".toCharArray(), '.');
    private static final char[] ORDER_BY_META_SERVER_CLASS_NAME = CharOperations.merge(SORTING_META_SERVER_CLASS_NAME, "Item".toCharArray(), '.');
    private static final char[] ORDER_BY_META_EXPLORER_CLASS_NAME = CharOperations.merge(SORTING_META_EXPLORER_CLASS_NAME, "SortingItem".toCharArray(), '.');

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case WEB:
            case EXPLORER:
                /**
                 * public RadSortingDef(
                final Id id,
                final String name,
                final Id classId,
                final Id titleId,
                final SortingItem[] columns
                )
                 */
                printer.enterBlock();
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                printer.println();
                printer.printStringLiteral(def.getName());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, def.getOwnerClass().getId());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, def.getTitleId());
                printer.printComma();
                printer.println();

                new WriterUtils.SameObjectArrayWriter<AdsSortingDef.OrderBy>(ORDER_BY_META_EXPLORER_CLASS_NAME) {

                    @Override
                    public void writeItemConstructorParams(CodePrinter printer, OrderBy item) {
                        /**
                         * public SortingItem(
                        final Id  propId_,
                        final boolean sortDesc_
                        )
                         */
                        WriterUtils.writeIdUsage(printer, item.getPropertyId());
                        printer.printComma();
                        printer.println();
                        printer.print(item.getOrder() == EOrder.DESC);
                    }
                }.write(printer, def.getOrder().list());
                printer.leaveBlock();
                return true;
            case SERVER:
                /**
                public RadSortingDef(
                final Id    id,
                final Id    ownerDefId,
                final String    name,
                final Id    titleId,
                final Item[]	orderBy,
                final String    hint
                final String    layerUri
                )
                 */
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                WriterUtils.writeIdUsage(printer, def.getOwnerClass().getId());
                printer.printComma();
                printer.printStringLiteral(def.getName());
                printer.printComma();
                WriterUtils.writeIdUsage(printer, def.getTitleId());
                printer.printComma();
                new WriterUtils.SameObjectArrayWriter<AdsSortingDef.OrderBy>(ORDER_BY_META_SERVER_CLASS_NAME) {

                    @Override
                    public void writeItemConstructorParams(CodePrinter printer, OrderBy item) {
                        //public Item(final Id columnId, final EOrder order)
                        WriterUtils.writeIdUsage(printer, item.getPropertyId());
                        printer.printComma();
                        WriterUtils.writeEnumFieldInvocation(printer, item.getOrder());
                    }
                }.write(printer, def.getOrder().list());
                printer.printComma();
                WriterUtils.writeSqmlAsXmlStr(printer, def.getHint());
                printer.printComma();
                printer.printStringLiteral(def.getLayer().getURI());
                return true;
            default:
                return false;

        }
    }
}
