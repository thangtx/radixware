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

import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsChildRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsEntityExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphLinkExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.CodePrinter;


public abstract class AdsExplorerItemWriter<T extends AdsExplorerItemDef> extends AbstractDefinitionWriter<T> {

    public static final class Factory {

        public static final AdsExplorerItemWriter newInstance(JavaSourceSupport support, AdsExplorerItemDef def, UsagePurpose usagePurpose) {
            if (def instanceof AdsChildRefExplorerItemDef) {
                return new AdsChildRefExplorerItemWriter(support, (AdsChildRefExplorerItemDef) def, usagePurpose);
            } else if (def instanceof AdsEntityExplorerItemDef) {
                return new AdsEntityExplorerItemWriter(support, (AdsEntityExplorerItemDef) def, usagePurpose);
            } else if (def instanceof AdsParentRefExplorerItemDef) {
                return new AdsParentRefExplorerItemWriter(support, (AdsParentRefExplorerItemDef) def, usagePurpose);
            } else if (def instanceof AdsParagraphLinkExplorerItemDef) {
                return new AdsParagraphLinkWriter(support, (AdsParagraphLinkExplorerItemDef) def, usagePurpose);
            } else if (def instanceof AdsParagraphExplorerItemDef) {
                return new AdsParagraphWriter(support, (AdsParagraphExplorerItemDef) def, usagePurpose);
            } else {
                throw new IllegalArgumentException("Unexpected context for code writer");
            }
        }
    }

    public AdsExplorerItemWriter(JavaSourceSupport support, T target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    private static class C {

        void test() {
        }
    }

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        final ERuntimeEnvironmentType env = usagePurpose.getEnvironment();
        switch (env) {
            case SERVER:
            case WEB:
            case EXPLORER:
            case COMMON_CLIENT:
                printer.print("new ");
                writeMetaClassName(env, printer);
                printer.print('(');
                if (env == ERuntimeEnvironmentType.SERVER && def.getDefinitionType() == EDefType.PARAGRAPH) {
                    WriterUtils.writeReleaseAccessorInMetaClass(printer);
                    printer.printComma();
                }
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                printer.enterBlock(1);
                printer.println();
                if (def.isTopLevelDefinition()) {
                    boolean writeOwnerId = true;
                    if (def instanceof AdsParagraphExplorerItemDef) {
                        AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) def;
                        if (env == ERuntimeEnvironmentType.SERVER && par.isRoot()) {
                            writeOwnerId = false;
                        }
                    }
                    if (writeOwnerId) {
                        WriterUtils.writeIdUsage(printer, def.getId());
                        printer.printComma();
                    }
                } else {
                    AdsParagraphExplorerItemDef paragraph = def.findOwnerExplorerRoot();
                    if (paragraph != null) {
                        WriterUtils.writeIdUsage(printer, paragraph.getId());
                    } else {
                        AdsEditorPresentationDef presentation = def.findOwnerEditorPresentation();
                        if (presentation != null) {
                            WriterUtils.writeIdUsage(printer, presentation.getOwnerClass().getId());
                        } else {
                            WriterUtils.writeNull(printer);
                        }
                    }
                    printer.printComma();
                }

                writeInnerSpecific(env, printer);
                writeFooterSpecific(env, printer);
                printer.leaveBlock(1);
                printer.print(')');
                return true;
            default:
                return false;
        }
    }

    protected abstract void writeInnerSpecific(ERuntimeEnvironmentType env, CodePrinter printer);

    protected abstract void writeFooterSpecific(ERuntimeEnvironmentType env, CodePrinter printer);

    protected abstract void writeMetaClassName(ERuntimeEnvironmentType env, CodePrinter printer);
}
