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

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.PageOrder;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.RadixObjectWriter;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;


public class AdsEditorPagesWriter extends RadixObjectWriter<EditorPages> {

    public AdsEditorPagesWriter(JavaSourceSupport support, EditorPages target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }
    private static final char[] PAGE_ORDER_EXPLORER_CLASS_NAME = CharOperations.merge(AdsEditorPageWriter.EDITOR_PRESENTATION_PAGE_META_EXPLORER_PACKAGE_NAME, "RadEditorPages.PageOrder".toCharArray(), '.');

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        final ERuntimeEnvironmentType env = usagePurpose.getEnvironment();
        switch (env) {
            case SERVER:

                if (def.getOwnerDefinition() instanceof AdsEditorPresentationDef) {
                    new WriterUtils.SameObjectArrayWriter<AdsEditorPageDef>(AdsEditorPageWriter.EDITOR_PRESENTATION_PAGE_META_SERVER_CLASS_NAME) {

                        @Override
                        public void writeItemConstructorParams(CodePrinter printer, AdsEditorPageDef item) {
                            writeCode(printer, item);
                        }
                    }.write(printer, def.get(EScope.LOCAL_AND_OVERWRITE));
                }
                return true;
            case WEB:
            case EXPLORER:

                new WriterUtils.ObjectArrayWriter<AdsEditorPageDef>(AdsEditorPageWriter.EDITOR_PRESENTATION_PAGE_META_EXPLORER_CLASS_NAME) {

                    @Override
                    public void writeItemConstructor(CodePrinter printer, AdsEditorPageDef item) {

                        writeCode(printer, item);
                    }
                }.write(printer, def.get(EScope.LOCAL_AND_OVERWRITE, new IFilter<AdsEditorPageDef>() {

                    @Override
                    public boolean isTarget(AdsEditorPageDef radixObject) {
                        ERuntimeEnvironmentType e = radixObject.getClientEnvironment();
                        return e == ERuntimeEnvironmentType.COMMON_CLIENT || env == e;
                    }
                }));
                //write page order array
                printer.printComma();
                printer.println();


//                new WriterUtils.SameObjectArrayWriter<EditorPages.PageOrder.Item>(PAGE_ORDER_EXPLORER_CLASS_NAME) {
//
//                    @Override
//                    public void writeItemConstructorParams(CodePrinter printer, Item item) {
//                        /**
//                         * public PageOrder(final int level, final Id id){
//                        this.level = level;
//                        this.id = id;
//                        }
//                         */
//                        printer.print(item.getLevel());
//                        printer.printComma();
//                        WriterUtils.writeIdUsage(printer, item.getPageId());
//                    }
//                }.write(printer, def.getOrder().getItems());
                if (!writePageOrder(env, def.getOrder(), 1, printer)) {
                    return false;
                }

                return true;
            default:
                return false;
        }
    }

    private boolean writePageOrder(ERuntimeEnvironmentType env, PageOrder order, int level, CodePrinter printer) {
        if (level == 1) {
            printer.print("new ");
            printer.print(PAGE_ORDER_EXPLORER_CLASS_NAME);
            printer.enterBlock();
            printer.println("[]{");            
        }
        boolean isFirst = level == 1;
        for (OrderedPage page : order) {
            AdsEditorPageDef ref = page.findPage();
            if (ref == null) {
                return false;
            } else {
                ERuntimeEnvironmentType pageEnv = ref.getClientEnvironment();
                if (pageEnv != ERuntimeEnvironmentType.COMMON_CLIENT && pageEnv != env) {
                    continue;
                }
            }
            if (isFirst) {
                isFirst = false;
            } else {
                printer.printComma();
                printer.println();
            }
            printer.print("new ");
            printer.print(PAGE_ORDER_EXPLORER_CLASS_NAME);
            printer.print('(');
            printer.print(level);
            printer.printComma();
            WriterUtils.writeIdUsage(printer, page.getPageId());
            printer.print(')');
            printer.enterBlock();
            if (!writePageOrder(env, page.getSubpages(), level + 1, printer)) {
                return false;
            }
            printer.leaveBlock();
        }
        if (level == 1) {
            printer.leaveBlock();
            printer.println('}');
        }
        return true;
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        //don use directly
    }
}
