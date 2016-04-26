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

import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef.TitleItem;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;

public class AdsObjectTitleFormatWriter extends AbstractDefinitionWriter<AdsObjectTitleFormatDef> {

    public AdsObjectTitleFormatWriter(JavaSourceSupport support, AdsObjectTitleFormatDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }
    private static final char[] OTF_META_SERVER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_SERVER_PACKAGE_NAME, "RadEntityTitleFormatDef".toCharArray(), '.');
    private static final char[] TITLE_ITEM_META_SERVER_CLASS_NAME = CharOperations.merge(OTF_META_SERVER_CLASS_NAME, "TitleItem".toCharArray(), '.');

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                /**
                 * public RadEntityTitleFormatDef(final Id ownerEntityId,final
                 * TitleItem[] format, final Id nullTitleId)
                 */
                printer.print("new ");
                printer.print(OTF_META_SERVER_CLASS_NAME);
                printer.print('(');
                WriterUtils.writeIdUsage(printer, def.getOwnerClass().getId());
                printer.printComma();
                new WriterUtils.SameObjectArrayWriter<AdsObjectTitleFormatDef.TitleItem>(TITLE_ITEM_META_SERVER_CLASS_NAME) {

                    @Override
                    public void writeItemConstructorParams(CodePrinter printer, TitleItem item) {
                        /**
                         * public TitleItem(final Id ownerEntityId,final Id
                         * propId, final Id formatId){ public TitleItem(final Id
                         * propId, final String formatStr)
                         */
                        if (item.isMultilingual()) {
                            WriterUtils.writeIdUsage(printer, def.getOwnerClass().getId());
                            printer.printComma();
                            WriterUtils.writeIdUsage(printer, item.getPropertyId());
                            printer.printComma();
                            WriterUtils.writeIdUsage(printer, item.getPatternId());
                        } else {
                            WriterUtils.writeIdUsage(printer, item.getPropertyId());
                            printer.printComma();
                            printer.printStringLiteral(item.getPattern());
                        }
                    }
                }.write(printer, def.getItems().list());
                printer.printComma();
                WriterUtils.writeIdUsage(printer, def.getNullValTitleId());

//                RadEntityTitleFormatDef {
//
//    public enum DefinitionContextType {
//
//        ENTITY,
//        PROPERTY,
//        EDITOR_PRESENTATION
//    }         
                printer.printComma();
                printer.print(OTF_META_SERVER_CLASS_NAME);
                printer.print(".DefinitionContextType.");

                AdsDefinition ownerDef = def.getOwnerDef();
                switch (ownerDef.getDefinitionType()) {
                    case CLASS_PROPERTY:
                        printer.print("PROPERTY");
                        break;
                    case EDITOR_PRESENTATION:
                        printer.print("EDITOR_PRESENTATION");
                        break;
                    default:
                        printer.print("ENTITY");
                }
                printer.print(')');
                return true;
            default:
                return false;
        }
    }
}
