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

import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef.EnabledSorting;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef.Parameter.ParameterEditOptions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.ParentRefType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;


public class AdsFilterWriter extends AbstractDefinitionWriter<AdsFilterDef> {

    public AdsFilterWriter(JavaSourceSupport support, AdsFilterDef filter, UsagePurpose usagePurpose) {
        super(support, filter, usagePurpose);
    }
    static final char[] FILTER_META_SERVER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_SERVER_PACKAGE_NAME, "RadFilterDef".toCharArray(), '.');
    public static final char[] FILTER_META_EXPLORER_CLASS_NAME = CharOperations.merge(WriterUtils.FILTERS_META_EXPLORER_PACKAGE_NAME, "RadFilterDef".toCharArray(), '.');
    private static final char[] ENABLED_SORTING_META_SERVER_CLASS_NAME = CharOperations.merge(FILTER_META_SERVER_CLASS_NAME, "EnabledSorting".toCharArray(), '.');
    private static final char[] PARAM_META_CLASS_NAME = CharOperations.merge(WriterUtils.FILTERS_META_EXPLORER_PACKAGE_NAME, "RadFilterParamDef".toCharArray(), '.');

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case WEB:
            case EXPLORER:
                /* public RadFilterDef(
                 final Id id,
                 final String name,
                 final Id classId,
                 final Id titleId,
                 final org.radixware.schemas.xscml.Sqml condition,
                 final Id[] enabledSortings,
                 final boolean cstSortingsEnabled,
                 final boolean baseSortingsEnabled,
                 final Id defaultSortingId,
                 RadFilterParamDef[] parameters,
                 RadEditorPageDef[] pages,            //страницы, объявленные в данном фильтре
                 RadEditorPages.PageOrder[] pageOrder */
                printer.enterBlock();
                final AdsEntityObjectClassDef clazz = def.getOwnerClass();
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                printer.println();
                printer.printStringLiteral(def.getName());
                printer.printComma();
                printer.println();
                WriterUtils.writeEnumFieldInvocation(printer, def.getClientEnvironment());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, clazz.getId());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, def.getTitleId());
                printer.printComma();
                printer.println();
                WriterUtils.writeSqmlAsXmlStr(printer, def.getCondition());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdArrayUsage(printer, def.getEnabledSortings().getEnabledSortingIds());
                printer.printComma();
                printer.println();
                printer.print(def.isCustomSortingEnabled());
                printer.printComma();
                printer.println();
                printer.print(def.isAnyBaseSortingEnabled());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, def.getDefaultSortingId());
                printer.printComma();
                printer.println();
                new WriterUtils.SameObjectArrayWriter<AdsFilterDef.Parameter>(PARAM_META_CLASS_NAME) {
                    @Override
                    public void writeItemConstructorParams(CodePrinter printer, AdsFilterDef.Parameter item) {
                        /* public RadFilterParamDef(
                         final Id id,            //собственный идентификатор
                         final String name,          //собственное имя
                         final Id titleId,       //если перекрыт заголовок - идентификатор строки
                         final Id ownerId,       //идентификатор класса (aec), в котором определен фильтр
                         final Id tableId,
                         final Id propertyId,
                         final EValType type,
                         final Id constSetId,
                         final String defaultValue,
                        
                         //Editing
                         final boolean isMandatory,
                         final boolean storeHistory,
                         final boolean isCustomDialog,
                         final Id customDialogId,
                         final boolean isCustomEditOnly,
                         final EditMask editMask,
                         final Id nullStringId,
                         final boolean isMemo,
                         //Parent selector
                         final Id referencedClassId,
                         final Id parentSelectorPresentationId
                         )*/
                        WriterUtils.writeIdUsage(printer, item.getId());
                        printer.printComma();
                        printer.println();
                        printer.printStringLiteral(item.getName());
                        printer.printComma();
                        printer.println();
                        WriterUtils.writeIdUsage(printer, item.getTitleId());
                        printer.printComma();
                        printer.println();
                        WriterUtils.writeIdUsage(printer, clazz.getId());
                        printer.printComma();
                        printer.println();
                        WriterUtils.writeIdUsage(printer, clazz.getEntityId());
                        printer.printComma();
                        printer.println();
                        WriterUtils.writeIdUsage(printer, null);
                        printer.printComma();
                        printer.println();
                        WriterUtils.writeEnumFieldInvocation(printer, item.getType().getTypeId());
                        printer.printComma();
                        printer.println();
                        AdsType type = item.getType().resolve(item).get();
                        if (type instanceof AdsEnumType) {
                            WriterUtils.writeIdUsage(printer, ((AdsEnumType) type).getSource().getId());
                        } else {
                            WriterUtils.writeNull(printer);
                        }
                        printer.printComma();
                        printer.println();
                        WriterUtils.writeRadixValAsStr(printer, item.getDefaultValue());
                        printer.printComma();
                        printer.println();
                        ParameterEditOptions eo = item.getEditOptions();

                        printer.print(eo.isNotNull());
                        printer.printComma();
                        printer.println();
                        printer.print(eo.isStoreEditHistory());
                        printer.printComma();
                        printer.println();
                        printer.print(eo.isShowDialogButton());
                        printer.printComma();
                        printer.println();
                        WriterUtils.writeIdUsage(printer, eo.getCustomDialogId(usagePurpose.getEnvironment()));
                        printer.printComma();
                        printer.println();
                        printer.print(eo.isCustomEditOnly());
                        printer.printComma();
                        printer.println();
                        EditMask em = eo.getEditMask();
                        if (em == null) {
                            WriterUtils.writeNull(printer);
                        } else {
                            writeCode(printer, em);
                        }
                        printer.printComma();
                        printer.println();
                        WriterUtils.writeIdUsage(printer, eo.getNullValTitleId());
                        printer.printComma();
                        printer.println();
                        printer.print(eo.isMemo());
                        printer.printComma();
                        printer.println();
                        if (type instanceof ParentRefType) {
                            AdsEntityObjectClassDef ref = ((ParentRefType) type).getSource();
                            if (ref != null) {
                                WriterUtils.writeIdUsage(printer, ref.getId());
                            } else {
                                WriterUtils.writeNull(printer);
                            }
                        } else {
                            WriterUtils.writeNull(printer);
                        }
                        printer.printComma();
                        printer.println();
                        WriterUtils.writeIdUsage(printer, eo.getParentSelectorPresentationId());
                    }
                }.write(printer, def.getParameters().list());
                printer.printComma();
                printer.println();
                writeCode(printer, def.getEditorPages());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdArrayUsage(printer, def.getUsedContextlessCommands().getCommandIds());
                printer.leaveBlock();
                return true;
            case SERVER:
                /**
                 * public RadFilterDef( final Id id, final Id ownerDefId, final
                 * String name, final Id titleId, final String condition, final
                 * String hint, final Id defaultSortingId, final boolean
                 * isAnyBaseSortingEnabled, final EnabledSorting[]
                 * enabledBaseSortings, final boolean isAnyCustomSortingEnabled,
                 * final String layerUri )
                 */
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                WriterUtils.writeIdUsage(printer, def.getOwnerClass().getId());
                printer.printComma();
                printer.printStringLiteral(def.getName());
                printer.printComma();
                WriterUtils.writeIdUsage(printer, def.getTitleId()); // by BAO
                printer.printComma();							 	 // by BAO
                WriterUtils.writeSqmlAsXmlStr(printer, def.getCondition());
                printer.printComma();
                WriterUtils.writeSqmlAsXmlStr(printer, def.getHint());
                printer.printComma();
                WriterUtils.writeIdUsage(printer, def.getDefaultSortingId());
                printer.printComma();
                printer.print(def.isAnyBaseSortingEnabled());
                printer.printComma();

                new WriterUtils.SameObjectArrayWriter<AdsFilterDef.EnabledSorting>(ENABLED_SORTING_META_SERVER_CLASS_NAME) {
                    @Override
                    public void writeItemConstructorParams(CodePrinter printer, EnabledSorting item) {
                        /**
                         * public EnabledSorting( final String hint, final Id
                         * sortingId )
                         */
                        WriterUtils.writeSqmlAsXmlStr(printer, item.getHint());
                        printer.printComma();
                        WriterUtils.writeIdUsage(printer, item.getSortingId());
                        printer.printComma();
                        printer.printStringLiteral(def.getLayer().getURI());
                    }
                }.write(printer, def.getEnabledSortings().list());
                printer.printComma();
                printer.print(def.isCustomSortingEnabled());
                printer.printComma();
                printer.printStringLiteral(def.getLayer().getURI());
                return true;
            default:
                return false;
        }
    }
}
