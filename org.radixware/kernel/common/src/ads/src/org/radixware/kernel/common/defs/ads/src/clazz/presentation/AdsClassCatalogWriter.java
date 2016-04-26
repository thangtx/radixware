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

import java.sql.Ref;
import java.util.List;

import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;

import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef.Topic;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;

/**
 *
 * RadClassPresentationDef.ClassCatalog[]

 * public ClassCatalog(
final Id id,
final ELinkMode linkMode,
final Item[] items)
 * public static enum ELinkMode{
VIRTUAL, FINAL
}
 * public ClassCatalog.Topic(final Id topicId, final Id ownerTopicId, final Id prevId, final Id titleOwnerId, final Id titleId)
 * public ClassCatalog.ClassRef(final Id classId, final Id ownerTopicId, final Id prevId, final boolean bInheritTitle, final Id titleOwnerId, final Id titleId)
 *
 */
public class AdsClassCatalogWriter extends AbstractDefinitionWriter<AdsClassCatalogDef> {

    static final char[] CLASS_CATALOG_META_SERVER_CLASS_NAME = CharOperations.merge(AdsClassPresentationsWriter.CLASS_PRESENTATIONS_SERVER_CLASS_NAME, "ClassCatalog".toCharArray(), '.');
    static final char[] CLASS_CATALOG_LINK_MODE_CLASS_NAME = CharOperations.merge(CLASS_CATALOG_META_SERVER_CLASS_NAME, "ELinkMode".toCharArray(), '.');
    private final char[] CLASS_CATALOG_ITEM_CLASS_NAME = CharOperations.merge(CLASS_CATALOG_META_SERVER_CLASS_NAME, "Item".toCharArray(), '.');
    private final char[] CLASS_CATALOG_TOPIC_CLASS_NAME = CharOperations.merge(CLASS_CATALOG_META_SERVER_CLASS_NAME, "Topic".toCharArray(), '.');
    private final char[] CLASS_CATALOG_CLASS_REF_CLASS_NAME = CharOperations.merge(CLASS_CATALOG_META_SERVER_CLASS_NAME, "ClassRef".toCharArray(), '.');

    public AdsClassCatalogWriter(JavaSourceSupport support, AdsClassCatalogDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                printer.print(CLASS_CATALOG_LINK_MODE_CLASS_NAME);
                printer.print('.');
                if (def.isVirtual()) {
                    printer.print("VIRTUAL");
                } else {
                    printer.print("FINAL");
                }
                printer.printComma();
                printer.print("new ");
                printer.print(CLASS_CATALOG_ITEM_CLASS_NAME);
                printer.enterBlock(1);
                printer.println("[]{");
                boolean first = true;
                AdsEntityObjectClassDef ownerClass = def.getOwnerClass();
                List<Topic> topics = def.getTopicList();
                for (Topic t : topics) {
                    if (first) {
                        first = false;
                    } else {
                        printer.printComma();
                        printer.println();
                    }
                    printer.print("new ");
                    printer.print(CLASS_CATALOG_TOPIC_CLASS_NAME);
                    printer.print('(');
                    WriterUtils.writeIdUsage(printer, t.getId());
                    printer.printComma();
                    WriterUtils.writeIdUsage(printer, t.getParentTopicId());
                    printer.printComma();
                    printer.print(String.valueOf(t.getOrder()));
                    printer.printComma();
                    WriterUtils.writeIdUsage(printer, ownerClass.getId());
                    printer.printComma();
                    WriterUtils.writeIdUsage(printer, t.getTitleId());
                    printer.print(')');
                }
                List<AdsClassCatalogDef.ClassReference> refs = def.getClassRefList();
                for (AdsClassCatalogDef.ClassReference r : refs) {
                    if (first) {
                        first = false;
                    } else {
                        printer.printComma();
                        printer.println();
                    }
                    printer.print("new ");
                    printer.print(CLASS_CATALOG_CLASS_REF_CLASS_NAME);
                    printer.print('(');
                    WriterUtils.writeIdUsage(printer, r.getClassId());
                    printer.printComma();
                    WriterUtils.writeIdUsage(printer, r.getParentTopicId());
                    printer.printComma();
                    printer.print(String.valueOf(r.getOrder()));
                    printer.printComma();
                    printer.print(r.isTitleInherited());
                    printer.printComma();
                    WriterUtils.writeIdUsage(printer, ownerClass.getId());
                    printer.printComma();
                    WriterUtils.writeIdUsage(printer, r.getTitleId());
                    printer.print(')');
                }

                printer.leaveBlock(1);
                printer.println('}');



                return true;
            default:
                return false;
        }
    }
}
