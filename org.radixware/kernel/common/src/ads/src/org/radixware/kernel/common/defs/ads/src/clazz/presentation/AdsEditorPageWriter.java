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

import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef.PagePropertyRef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsProperiesGroupDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IPagePropertyGroup;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;


public class AdsEditorPageWriter extends AbstractDefinitionWriter<AdsEditorPageDef> {

    public AdsEditorPageWriter(JavaSourceSupport support, AdsEditorPageDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }
    static final char[] EDITOR_PRESENTATION_PAGE_META_SERVER_CLASS_NAME = CharOperations.merge(AdsEditorPresentationWriter.EDITOR_PRESENTATION_META_SERVER_CLASS_NAME, "Page".toCharArray(), '.');
    static final char[] EDITOR_PRESENTATION_PAGE_META_EXPLORER_PACKAGE_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "editorpages".toCharArray(), '.');
    static final char[] EDITOR_PRESENTATION_PAGE_META_EXPLORER_CLASS_NAME = CharOperations.merge(EDITOR_PRESENTATION_PAGE_META_EXPLORER_PACKAGE_NAME, "RadEditorPageDef".toCharArray(), '.');
    private static final char[] CONTAINER_PAGE_CLASS_NAME = CharOperations.merge(EDITOR_PRESENTATION_PAGE_META_EXPLORER_PACKAGE_NAME, "RadContainerEditorPageDef".toCharArray(), '.');
    private static final char[] CUSTOM_PAGE_CLASS_NAME = CharOperations.merge(EDITOR_PRESENTATION_PAGE_META_EXPLORER_PACKAGE_NAME, "RadCustomEditorPageDef".toCharArray(), '.');
    private static final char[] STD_PAGE_CLASS_NAME = CharOperations.merge(EDITOR_PRESENTATION_PAGE_META_EXPLORER_PACKAGE_NAME, "RadStandardEditorPageDef".toCharArray(), '.');
    private static final char[] STD_PAGE_ITEM_CLASS_NAME = CharOperations.merge(STD_PAGE_CLASS_NAME, "PageItem".toCharArray(), '.');
    private static final char[] STD_PAGE_GROUP_CLASS_NAME = CharOperations.merge(STD_PAGE_CLASS_NAME, "PropertiesGroup".toCharArray(), '.');

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        final ERuntimeEnvironmentType env = usagePurpose.getEnvironment();
        AdsDefinition context = def.getOwnerDef();
        switch (env) {
            case SERVER:
                if (context instanceof AdsEditorPresentationDef) {
                    /**
                     * public Page(final Id id, final Id[] usedPropIds)
                     */
                    WriterUtils.writeIdUsage(printer, def.getId());
                    printer.printComma();
                    WriterUtils.writeIdArrayUsage(printer, def.getProperties().getIds());
                }
                return true;
            case WEB:
            case EXPLORER:
                /*
                 * public RadEditorPageDef(final Id id, final String name, final
                 * Id titleOwnerId, final Id titleId, final Id iconId, final
                 * boolean visible
                 */
                printer.print(" new ");
                switch (def.getType()) {
                    case CONTAINER:
                        printer.print(CONTAINER_PAGE_CLASS_NAME);
                        break;
                    case CUSTOM:
                        printer.print(CUSTOM_PAGE_CLASS_NAME);
                        break;
                    case STANDARD:
                        printer.print(STD_PAGE_CLASS_NAME);
                        break;
                }
                printer.print('(');
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                printer.printStringLiteral(def.getName());
                printer.printComma();
                if (def.isTitleInherited()) {
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                } else {
                    WriterUtils.writeIdUsage(printer, def.getOwnerClass().getId());
                    printer.printComma();
                    WriterUtils.writeIdUsage(printer, def.getTitleId());
                    printer.printComma();
                }
                if (def.isIconInherited()) {
                    WriterUtils.writeNull(printer);
                } else {
                    WriterUtils.writeIdUsage(printer, def.getIconId());
                }

                switch (def.getType()) {
                    case CONTAINER:
                        printer.printComma();
                        WriterUtils.writeIdUsage(printer, def.getEmbeddedExplorerItemId());
                        break;
                    case CUSTOM:
                        printer.printComma();
                        WriterUtils.writeIdArrayUsage(printer, def.getProperties().getIds());
                        printer.printComma();
                        WriterUtils.writeIdUsage(printer, def.getCustomViewId(env));
                        break;
                    case STANDARD:
                        //PageItem array required
                        printer.printComma();
                        writePageItems(def.getProperties(), printer, env);
                        printer.printComma();
                        new WriterUtils.SameObjectArrayWriter<AdsProperiesGroupDef>(STD_PAGE_GROUP_CLASS_NAME) {

                            @Override
                            public void writeItemConstructorParams(CodePrinter printer, AdsProperiesGroupDef group) {
                                WriterUtils.writeIdUsage(printer, group.getId());
                                printer.printComma();
                                printer.printStringLiteral(group.getName());
                                printer.printComma();
                                WriterUtils.writeIdUsage(printer, group.getTitleId());
                                printer.printComma();
                                WriterUtils.writeIdUsage(printer, def.getOwnerClass().getId());
                                printer.printComma();
                                printer.print(false);
                                printer.printComma();
                                printer.print(group.isShowFrame());
                                printer.printComma();
                                writePageItems(group, printer, env);

                            }
                        }.write(printer, def.getProperties().getProperiesGroups());
                        break;
                }
                printer.print(')');

                return true;
            default:
                return false;
        }
    }
    
    private void writePageItems(final IPagePropertyGroup group, final CodePrinter printer, final ERuntimeEnvironmentType env) {
        new WriterUtils.SameObjectArrayWriter<AdsEditorPageDef.PagePropertyRef>(STD_PAGE_ITEM_CLASS_NAME) {

            @Override
            public void writeItemConstructorParams(CodePrinter printer, PagePropertyRef item) {
                if (item.getId() != null) {
                    WriterUtils.writeIdUsage(printer, item.getId());
                } else if (item.getGroupDef() != null) {
                    WriterUtils.writeIdUsage(printer, item.getGroupDef().getId());
                }
                printer.printComma();
                printer.print(item.getColumn());
                printer.printComma();
                printer.print(item.getRow());
                printer.printComma();
                printer.print(item.getColumnSpan());
                printer.printComma();
                printer.print(item.isGlueToLeft());
                printer.printComma();
                printer.print(item.isGlueToRight());

            }
        }.write(printer, group.list(new IFilter<PagePropertyRef>() {

            @Override
            public boolean isTarget(PagePropertyRef radixObject) {
                AdsDefinition propCandidate = radixObject.findItem();
                if (propCandidate instanceof AdsPropertyDef) {
                    AdsPropertyDef prop = (AdsPropertyDef) propCandidate;
                    return prop.isTransferableAsMeta(env);
                } else {
                    return true;
                }
            }
        }));
    }
}
