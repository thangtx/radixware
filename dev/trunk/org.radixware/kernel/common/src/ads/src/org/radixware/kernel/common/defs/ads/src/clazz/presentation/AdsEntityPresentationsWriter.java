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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;

import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityPresentations;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef.ColumnsInfoItem;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;


class AdsEntityPresentationsWriter extends AdsEntityObjectPresentationsWriter<EntityPresentations> {

    private static final char[] RAD_REF_META_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadReferenceDef".toCharArray(), '.');

    public AdsEntityPresentationsWriter(JavaSourceSupport support, EntityPresentations presentations, UsagePurpose usagePurpose) {
        super(support, presentations, usagePurpose);
    }

    @Override
    protected void writeDefaultSelectorPresentationsId(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.writeIdUsage(printer, def.getDefaultSelectorPresentationId());
                break;
        }
    }

    @Override
    protected void writeObjectTitleFormatMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                AdsObjectTitleFormatDef otf = def.getObjectTitleFormat();
                if (otf != null) {
                    writeCode(printer, otf);
                } else {
                    super.writeObjectTitleFormatMeta(printer);
                }
                break;
        }
    }

    @Override
    protected void writeRestrictionsMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                writeCode(printer, def.getRestrictions());
                break;
        }
    }

    @Override
    protected void writeExplorerRefList(CodePrinter printer) {
        final DdsTableDef table = def.getOwnerClass().findTable(def);
        if (table == null) {
            super.writeExplorerRefList(printer);
        } else {
            Set<DdsReferenceDef> refs = table.collectOutgoingReferences();
            if (refs == null || refs.isEmpty()) {
                super.writeExplorerRefList(printer);
            } else {
                List<DdsReferenceDef> refList = new ArrayList<DdsReferenceDef>(refs);
                Collections.sort(refList, new Comparator<DdsReferenceDef>() {
                    @Override
                    public int compare(DdsReferenceDef o1, DdsReferenceDef o2) {
                        return o1.getId().compareTo(o2.getId());
                    }
                });
                new WriterUtils.SameObjectArrayWriter<DdsReferenceDef>(RAD_REF_META_CLASS_NAME) {

                    /*
                     * public o(final Id id,
                     final String name,//автогенерируемое имя
                     final Id ownerTableId,//идентификатор собственной таблицы
                     final Id referencedTableId//идентификатор родительской таблицы
                     final Id[] childPropertyIds,
                     String[] columnNames,
                     final Id[] parentPropertyIds,
                     String[] columnNames
                     )
                     */
                    @Override
                    public void writeItemConstructorParams(CodePrinter printer, DdsReferenceDef item) {
                        WriterUtils.writeIdUsage(printer, item.getId());
                        printer.printComma();
                        printer.printStringLiteral(item.getName());
                        printer.printComma();
                        WriterUtils.writeIdUsage(printer, item.getChildTableId());
                        printer.printComma();
                        WriterUtils.writeIdUsage(printer, item.getParentTableId());


                        List<DdsReferenceDef.ColumnsInfoItem> items = item.getColumnsInfo().list();
                        Id[] childColumnIds = new Id[items.size()];
                        String[] childColumnNames = new String[items.size()];
                        Id[] parentColumnIds = new Id[items.size()];
                        String[] parentColumnNames = new String[items.size()];

                        Collections.sort(items, new Comparator<DdsReferenceDef.ColumnsInfoItem>() {
                            @Override
                            public int compare(ColumnsInfoItem o1, ColumnsInfoItem o2) {
                                Id id1 = o1.getParentColumnId();
                                Id id2 = o2.getParentColumnId();
                                return id1.compareTo(id2);
                            }
                        });

                        for (int i = 0; i < childColumnIds.length; i++) {
                            DdsReferenceDef.ColumnsInfoItem ci = items.get(i);
                            childColumnIds[i] = ci.getChildColumnId();
                            DdsColumnDef c = ci.findChildColumn();
                            childColumnNames[i] = c == null ? "unknown" : c.getName();
                            parentColumnIds[i] = ci.getParentColumnId();
                            c = ci.findParentColumn();
                            parentColumnNames[i] = c == null ? "unknown" : c.getName();
                        }
                        printer.printComma();
                        WriterUtils.writeIdArrayUsage(printer, childColumnIds);
                        printer.printComma();
                        WriterUtils.writeStringArrayUsage(printer, childColumnNames);
                        printer.printComma();
                        WriterUtils.writeIdArrayUsage(printer, parentColumnIds);
                        printer.printComma();
                        WriterUtils.writeStringArrayUsage(printer, parentColumnNames);
                    }
                }.write(printer, refList);
            }
        }
    }
}
