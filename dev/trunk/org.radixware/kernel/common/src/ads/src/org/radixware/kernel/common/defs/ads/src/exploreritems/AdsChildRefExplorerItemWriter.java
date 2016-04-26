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

import org.radixware.kernel.common.defs.ads.explorerItems.AdsChildRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;


class AdsChildRefExplorerItemWriter extends AdsSelectorExplorerItemWriter<AdsChildRefExplorerItemDef> {

    public AdsChildRefExplorerItemWriter(JavaSourceSupport support, AdsChildRefExplorerItemDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }
    static final char[] EXPLORER_ITEM_META_SERVER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_SERVER_PACKAGE_NAME, "RadChildRefExplorerItemDef".toCharArray(), '.');
    static final char[] EXPLORER_ITEM_META_EXPLORER_CLASS_NAME = CharOperations.merge(WriterUtils.EXPLORER_ITEMS_META_EXPLORER_PACKAGE_NAME, "RadChildRefExplorerItemDef".toCharArray(), '.');

    @Override
    protected void writeInnerSpecific(ERuntimeEnvironmentType env, CodePrinter printer) {
        switch (env) {
            case SERVER:

                /**
                public RadChildRefExplorerItemDef(
                final Id             id,
                final Id             ownerDefId,
                final Id             classId,
                final Id             selectorPresentationId,
                final RadConditionDef      condition,
                final Id             childReferenceId,
                final Restrictions		 restrictions,
                final Id 			 classCatalogId
                )
                 */
                super.writeInnerSpecific(env, printer);
                WriterUtils.writeIdUsage(printer, def.getChildReferenceId());
                printer.printComma();
                printer.println();
                break;
            case WEB:
            case EXPLORER:
            case COMMON_CLIENT:
                super.writeInnerSpecific(env, printer);
        }
    }

    @Override
    protected void writeMetaClassName(ERuntimeEnvironmentType env, CodePrinter printer) {
        switch (env) {
            case SERVER:
                printer.print(EXPLORER_ITEM_META_SERVER_CLASS_NAME);
                break;
            case WEB:
            case EXPLORER:
            case COMMON_CLIENT:
                printer.print(EXPLORER_ITEM_META_EXPLORER_CLASS_NAME);
                break;
        }
    }
}
