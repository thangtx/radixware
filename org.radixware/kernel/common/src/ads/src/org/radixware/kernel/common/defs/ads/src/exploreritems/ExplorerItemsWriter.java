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

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.RadixObjectWriter;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.src.WriterUtils.ObjectArrayWriter;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;


public class ExplorerItemsWriter extends RadixObjectWriter<ExplorerItems> {

    public ExplorerItemsWriter(JavaSourceSupport support, ExplorerItems target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }
    private static final char[] EXPLORER_ITEM_META_SERVER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_SERVER_PACKAGE_NAME, "RadExplorerItemDef".toCharArray(), '.');
    private static final char[] EXPLORER_ITEM_META_EXPLORER_CLASS_NAME = CharOperations.merge(WriterUtils.EXPLORER_ITEMS_META_EXPLORER_PACKAGE_NAME, "RadExplorerItemDef".toCharArray(), '.');

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        final ERuntimeEnvironmentType env = usagePurpose.getEnvironment();
        switch (env) {
            case SERVER:
            case WEB:
            case EXPLORER:
            case COMMON_CLIENT:
                printer.enterBlock(1);
                EScope scope;
                //if (/*def.getOwnerDefinition() instanceof AdsEditorPresentationDef || */((def.getOwnerDefinition() instanceof AdsParagraphExplorerItemDef) && ((AdsParagraphExplorerItemDef) def.getOwnerDefinition()).isTopLevelDefinition())) {
                    scope = EScope.LOCAL_AND_OVERWRITE;
//                } else {
//                    scope = EScope.LOCAL_AND_OVERWRITE;
//                }
                new ObjectArrayWriter<AdsExplorerItemDef>(env == ERuntimeEnvironmentType.SERVER ? EXPLORER_ITEM_META_SERVER_CLASS_NAME : EXPLORER_ITEM_META_EXPLORER_CLASS_NAME) {
                    @Override
                    public void writeItemConstructor(CodePrinter printer, AdsExplorerItemDef item) {
                        ExplorerItemsWriter.this.writeCode(printer, item);
                    }
                }.write(printer, def.getChildren().get(scope, new IFilter<AdsExplorerItemDef>() {
                    @Override
                    public boolean isTarget(AdsExplorerItemDef radixObject) {
                        if (env == ERuntimeEnvironmentType.SERVER) {
                            return true;
                        }
                        ERuntimeEnvironmentType se = radixObject.getClientEnvironment();
                        switch (se) {
                            case COMMON_CLIENT:
                                return true;
                            default:
                                return se == env;
                        }
                    }
                }));
                printer.leaveBlock(1);
                printer.println();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        //don use directly
    }
}
