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

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.IModalDisplayable;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef.SelectorColumn;
import org.radixware.kernel.common.defs.ads.clazz.presentation.CreatePresentationsList.PresentationRef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.SelectorAddons;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EPresentationAttrInheritance;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.ESelectorColumnVisibility;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;

/**
 * ublic RadSelectorPresentationDef( final Id id, final String name, final Id
 * basePresentationId, final int inheritanceMask, final
 * RadPropertyPresentationDef[] propPresentations, final SelectorColumn[]
 * selectorColumns, final RadConditionDef condition, final Addons addons, final
 * Id[] editorPresentationIds, final Restrictions restrictions, final Id
 * classCatalogId )
 *
 */
public class AdsSelectorPresentationWriter extends AbstractDefinitionWriter<AdsSelectorPresentationDef> {

    static final char[] SELECTOR_PRESENTATION_META_SERVER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_SERVER_PACKAGE_NAME, "RadSelectorPresentationDef".toCharArray(), '.');
    public static final char[] SELECTOR_PRESENTATION_META_EXPLORER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadSelectorPresentationDef".toCharArray(), '.');
    private static final char[] SELECTOR_COLUMNS_META_SERVER_CLASS_NAME = CharOperations.merge(SELECTOR_PRESENTATION_META_SERVER_CLASS_NAME, "SelectorColumn".toCharArray(), '.');
    private static final char[] SELECTOR_COLUMNS_META_EXPLORER_CLASS_NAME = CharOperations.merge(SELECTOR_PRESENTATION_META_EXPLORER_CLASS_NAME, "SelectorColumn".toCharArray(), '.');
    private static final char[] ADDONS_META_SERVER_CLASS_NAME = CharOperations.merge(SELECTOR_PRESENTATION_META_SERVER_CLASS_NAME, "Addons".toCharArray(), '.');

    public AdsSelectorPresentationWriter(JavaSourceSupport support, AdsSelectorPresentationDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                return writeMetaImpl(printer);
            case WEB:
            case EXPLORER:
                WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
                printer.print("public final class ");
                //writeUsage(printer);
                printer.print(JavaSourceSupport.getMetaName(def, printer instanceof IHumanReadablePrinter));
                printer.print(" extends ");
                printer.print(SELECTOR_PRESENTATION_META_EXPLORER_CLASS_NAME);
                printer.enterBlock(1);
                printer.println("{");
                printer.print("public static final ");
                printer.print(SELECTOR_PRESENTATION_META_EXPLORER_CLASS_NAME);
                printer.print(" rdxMeta = ");
                //new AdsSelectorPresentationWriter(getSupport(), def, UsagePurpose.EXPLORER_META).writeCode(printer);
                if (!writeMetaImpl(printer)) {
                    return false;
                }
                printer.leaveBlock(1);

                printer.print("}");
                return true;
            default:
                return false;
        }

    }

    protected boolean writeMetaImpl(CodePrinter printer) {

        final SelectorPresentationFinalAttributes attributes = new SelectorPresentationFinalAttributes(def);

        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                printer.printStringLiteral(def.getName());
                printer.printComma();
                WriterUtils.writeIdUsage(printer, def.getBasePresentationId());
                printer.printComma();
                printer.print(EPresentationAttrInheritance.toBitField(attributes.finalInheritanceMask));
                printer.printComma();
                if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.COLUMNS) || attributes.finalColumns == null) {
                    WriterUtils.writeNull(printer);
                } else {
                    writeSelectorColumnsServerMeta(attributes.finalColumns, printer);
                }

                printer.printComma();
                if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.CONDITION) || attributes.finalCondition == null) {
                    WriterUtils.writeNull(printer);
                } else {
                    writeCode(printer, attributes.finalCondition);
                }

                printer.printComma();
                if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.ADDONS) || attributes.finalAddons == null) {
                    WriterUtils.writeNull(printer);
                } else {
                    writeAddonsServerMeta(attributes.finalAddons, printer);
                }
                printer.printComma();

                WriterUtils.writeIdArrayUsage(printer, def.getEditorPresentations().getIds());
                printer.printComma();

                WriterUtils.writeIdArrayUsage(printer, def.getCreatePresentationsList().getPresentationIds());
                printer.printComma();

                if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.RESTRICTIONS) || attributes.finalRestrictions == null) {
                    WriterUtils.writeNull(printer);
                } else {
                    writeCode(printer, attributes.finalRestrictions);
                }
                printer.printComma();
                if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.CLASS_CATALOG) || attributes.finalCreationClassCatalogId == null) {
                    WriterUtils.writeNull(printer);
                } else {
                    WriterUtils.writeIdUsage(printer, attributes.finalCreationClassCatalogId);
                }

                return true;
            case WEB:
            case EXPLORER:
                /**
                 * public RadSelectorPresentationDef( final Id id, final String
                 * name, final Id basePresentationId, final Id classId, final Id
                 * tableId, final Id titleId, final Id iconId, final Id[]
                 * contextlessCommandIds, final SelectorColumn[] columns, final
                 * Id[] sortingIds, final boolean isCustomSortingEnabled, final
                 * Id defaultSortingId, final Id[] filterIds, final boolean
                 * isFilterObligatory, final boolean isCustomFilterEnabled,
                 * final Id defaultFilterId, final Id defaultColorSchemeId,
                 * final long restrictionsMask, final Id[] enabledCommandIds,
                 * final long inheritanceMask, final Id creationPresentationId,
                 * final boolean isAutoExpandEnabled)
                 */
                printer.print("new ");
                //writeUsage(printer);
                printer.print(JavaSourceSupport.getMetaName(def, printer instanceof IHumanReadablePrinter));
                printer.println("();");

                if (!writeExplorerMetaConstructor(printer, attributes)) {
                    return false;
                }

                if (def.isUseDefaultModel()) {
                    printer.println("@Override");
                    printer.print("protected ");
                    printer.print(WriterUtils.EXPLORER_MODEL_CLASS_NAME);
                    printer.print(" createModelImpl(");
                    printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
                    printer.print(" userSession) {");
                    final AdsSelectorPresentationDef.ModelClassInfo modelInfo = def.findActualModelClass();
                    printer.print("    return new ");
                    writeCode(printer, modelInfo.clazz.getType(EValType.USER_CLASS, null));
                    if (modelInfo.useDefaultModel) {
                        printer.print('.');
                        printer.print(AdsModelClassDef.getDefaultModelLocalClassName(EClassType.GROUP_MODEL));
                    }
                    printer.println("(userSession,this);");
                    printer.println("}");
                }

                //Реализация методов ModelDefinitions
//    @Override
//    public Model createModel() {
//        final Id modelClassId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_GROUP_MODEL_CLASS);
//        try {
//            Class<Model> classModel = Environment.getDefManager().getDefinitionModelClass(modelClassId);
//            Constructor<Model> constructor = classModel.getConstructor(RadSelectorPresentationDef.class);
//            return constructor.newInstance(this);
//        } catch (Throwable e) {
//            throw new ModelCreationError(ModelCreationError.ModelType.GROUP_MODEL, this, null, e);
//        }
//    }
//


                return true;
            default:
                return false;
        }

    }

    private boolean writeExplorerMetaConstructor(CodePrinter printer, SelectorPresentationFinalAttributes attributes) {
        printer.print("private ");
        printer.enterBlock();
        //writeUsage(printer);
        printer.print(JavaSourceSupport.getMetaName(def, printer instanceof IHumanReadablePrinter));
        printer.println("(){");
        printer.print("super(");
        WriterUtils.writeIdUsage(printer, def.getId());
        printer.printComma();
        printer.println();
        printer.printStringLiteral(def.getName());
        printer.printComma();
        printer.println();
        //>>>>>>>>>>>RADIX-7065>>>>>>>>>>>
        WriterUtils.writeEnumFieldInvocation(printer, def.getClientEnvironment());
        printer.printComma();
        printer.println();
        //<<<<<<<<<<<RADIX-7065<<<<<<<<<<<
        WriterUtils.writeIdUsage(printer, def.getBasePresentationId());
        printer.printComma();
        printer.println();
        final AdsEntityObjectClassDef clazz = def.getOwnerClass();
        WriterUtils.writeIdUsage(printer, clazz.getId());
        printer.printComma();
        printer.println();
        WriterUtils.writeIdUsage(printer, clazz.getEntityId());
        printer.printComma();
        printer.println();
        if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.TITLE) || attributes.finalTitleId == null) {
            WriterUtils.writeNull(printer);
        } else {
            WriterUtils.writeIdUsage(printer, attributes.finalTitleId);
        }

        printer.printComma();
        printer.println();
        if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.ICON) || attributes.finalIconId == null) {
            WriterUtils.writeNull(printer);
        } else {
            WriterUtils.writeIdUsage(printer, def.getIconId());
        }

        printer.printComma();
        printer.println();

        WriterUtils.writeIdArrayUsage(printer, def.getUsedContextlessCommands().getCommandIds());
        printer.printComma();
        printer.println();
        if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.ADDONS) || attributes.finalAddons == null) {
            WriterUtils.writeNull(printer);
            printer.printComma();
            printer.println();
            printer.print(false);
            printer.printComma();
            printer.println();
            WriterUtils.writeNull(printer);
            printer.printComma();
            printer.println();
            WriterUtils.writeNull(printer);
            printer.printComma();
            printer.println();
            printer.print(false);
            printer.printComma();
            printer.println();
            printer.print(false);
            printer.printComma();
            printer.println();
            WriterUtils.writeNull(printer);
//            printer.printComma();
//            printer.println();
//            WriterUtils.writeNull(printer);
        } else {
            if (attributes.finalAddons.isAnyBaseSortingEnabled()) {
                WriterUtils.writeNull(printer);
            } else {
                WriterUtils.writeIdArrayUsage(printer, attributes.finalAddons.getEnabledSortingIds(), false);
            }
            printer.printComma();
            printer.println();
            printer.print(attributes.finalAddons.isCustomSortingEnabled());
            printer.printComma();
            printer.println();
            WriterUtils.writeIdUsage(printer, attributes.finalAddons.getDefaultSortingId());
            printer.printComma();
            printer.println();
            if (attributes.finalAddons.isAnyBaseFilterEnabled()) {
                WriterUtils.writeNull(printer);
            } else {
                WriterUtils.writeIdArrayUsage(printer, attributes.finalAddons.getEnabledFilterIds(), false);
            }
            printer.printComma();
            printer.println();
            printer.print(attributes.finalAddons.isFilterObligatory());
            printer.printComma();
            printer.println();
            printer.print(attributes.finalAddons.isCustomFilterEnabled());
            printer.printComma();
            printer.println();
            WriterUtils.writeIdUsage(printer, attributes.finalAddons.getDefaultFilterId());
//            printer.printComma();
//            printer.println();
//            WriterUtils.writeIdUsage(printer, attributes.finalAddons.getDefaultColorSchemeId());
        }

        printer.printComma();
        printer.println();
        if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.RESTRICTIONS) || attributes.finalRestrictions == null) {
            printer.print(0L);
            printer.printComma();
            WriterUtils.writeNull(printer);
        } else {
            writeCode(printer, attributes.finalRestrictions);
            printer.printComma();
            printer.println();
            if (attributes.finalRestrictions.isDenied(ERestriction.ANY_COMMAND)) {
                WriterUtils.writeIdArrayUsage(printer, attributes.finalRestrictions.getEnabledCommandIds());
            } else {
                WriterUtils.writeNull(printer);
            }
        }
        printer.printComma();
        printer.println();
        printer.print(EPresentationAttrInheritance.toBitField(attributes.finalInheritanceMask));
        printer.printComma();
        printer.println();

        final List<PresentationRef> refs = def.getCreatePresentationsList().getPresentationRefs();
        final List<Id> ids = new LinkedList<Id>();
        for (PresentationRef ref : refs) {
            final AdsEditorPresentationDef epr = ref.findEditorPresentation();
            if (epr == null) {
                return false;
            }
            final ERuntimeEnvironmentType env = epr.getClientEnvironment();
            if (env == ERuntimeEnvironmentType.COMMON_CLIENT || env == usagePurpose.getEnvironment()) {
                ids.add(epr.getId());
            }
        }
        WriterUtils.writeIdArrayUsage(printer, ids);
        printer.printComma();
        printer.println();

        final List<AdsSelectorPresentationDef.EditorPresentations.PresentationInfo> infos = def.getEditorPresentations().getPresentationInfos();
        ids.clear();
        for (AdsSelectorPresentationDef.EditorPresentations.PresentationInfo ref : infos) {
            final AdsEditorPresentationDef epr = ref.findPresentation();
            if (epr == null) {
                return false;
            }
            final ERuntimeEnvironmentType env = epr.getClientEnvironment();
            if (env == ERuntimeEnvironmentType.COMMON_CLIENT || env == usagePurpose.getEnvironment()) {
                ids.add(epr.getId());
            }
        }

        WriterUtils.writeIdArrayUsage(printer, ids);
        printer.printComma();
        printer.println();

        printer.print(def.isAutoExpandEnabled());
        printer.printComma();
        printer.print(def.isRestorePositionEnabled());

        printer.printComma();
        printer.print(attributes.isAutoSortClasses);

        final IModalDisplayable.ModialViewSizeInfo sizeInfo = def.getModialViewSizeInfo(usagePurpose.getEnvironment());
        if (sizeInfo != null) {
            printer.printComma();
            printer.print(sizeInfo.getWidth());
            printer.printComma();
            printer.print(sizeInfo.getHeight());
        }


        printer.println(");");
        printer.print("columns = ");
        if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.COLUMNS) || attributes.finalColumns == null) {
            printer.print("null;");
        } else {
            writeSelectorColumnsExplorerMeta(attributes.finalColumns, printer);
        }

        printer.leaveBlock();
        printer.printlnSemicolon();
        printer.println('}');

        return true;

    }

    private void writeSelectorColumnsServerMeta(RadixObjects<SelectorColumn> columns, CodePrinter printer) {
        /**
         * public SelectorColumn( final Id propId, final boolean isVisible)
         */
        new WriterUtils.SameObjectArrayWriter<AdsSelectorPresentationDef.SelectorColumn>(SELECTOR_COLUMNS_META_SERVER_CLASS_NAME) {
            @Override
            public void writeItemConstructorParams(CodePrinter printer, SelectorColumn item) {
                WriterUtils.writeIdUsage(printer, item.getPropertyId());
                printer.printComma();
                printer.print(item.getVisibility() != ESelectorColumnVisibility.NEVER);
            }
        }.write(printer, columns.list());
    }

    private void writeSelectorColumnsExplorerMeta(RadixObjects<SelectorColumn> columns, CodePrinter printer) {
        /**
         * public SelectorColumn( final Id propId, final boolean isVisible)
         */
        new WriterUtils.SameObjectArrayWriter<AdsSelectorPresentationDef.SelectorColumn>(SELECTOR_COLUMNS_META_EXPLORER_CLASS_NAME) {
            @Override
            public void writeItemConstructorParams(CodePrinter printer, SelectorColumn item) {
                WriterUtils.writeIdUsage(printer, item.getPropertyId());
                printer.printComma();
                WriterUtils.writeEnumFieldInvocation(printer, item.getVisibility());
                printer.printComma();
                WriterUtils.writeEnumFieldInvocation(printer, item.getAlign());
                printer.printComma();
                WriterUtils.writeEnumFieldInvocation(printer, item.getSizePolicy());
                printer.printComma();
                WriterUtils.writeIdUsage(printer, item.getTitleId());
            }
        }.write(printer, columns.list());
    }

    private void writeAddonsServerMeta(SelectorAddons addons, CodePrinter printer) {
        /**
         * public Addons( final Id defaultSortingId, final boolean
         * isAnyBaseSortingEnabled, final Id[] enabledBaseSortingIds, final
         * boolean isAnyCustomUserSortingEnabled, final Id defaultFilterId,
         * final boolean isAnyBaseFilterEnabled, final Id[]
         * enabledBaseFilterIds, final boolean isFilterObligatory, final boolean
         * isAnyCustomFilterEnabled, final String defaultHint, final Id
         * defaultColorSchemeId final String layerUri)
         */
        printer.print("new ");
        printer.print(ADDONS_META_SERVER_CLASS_NAME);
        printer.print('(');
        WriterUtils.writeIdUsage(printer, addons.getDefaultSortingId());
        printer.printComma();
        printer.print(addons.isAnyBaseSortingEnabled());
        printer.printComma();
        if (addons.isAnyBaseSortingEnabled()) {
            WriterUtils.writeNull(printer);
        } else {
            WriterUtils.writeIdArrayUsage(printer, addons.getEnabledSortingIds(), false);
        }
        printer.printComma();
        printer.print(addons.isCustomSortingEnabled());
        printer.printComma();

        WriterUtils.writeIdUsage(printer, addons.getDefaultFilterId());
        printer.printComma();
        printer.print(addons.isAnyBaseFilterEnabled());
        printer.printComma();
        if (addons.isAnyBaseFilterEnabled()) {
            WriterUtils.writeNull(printer);
        } else {
            WriterUtils.writeIdArrayUsage(printer, addons.getEnabledFilterIds(), false);
        }
        printer.printComma();
        printer.print(addons.isFilterObligatory());
        printer.printComma();
        printer.print(addons.isCustomFilterEnabled());
        printer.printComma();

        WriterUtils.writeSqmlAsXmlStr(printer, addons.getDefaultHint());
        printer.printComma();

//        WriterUtils.writeIdUsage(printer, addons.getDefaultColorSchemeId());
//        printer.printComma();
        printer.printStringLiteral(addons.getLayer().getURI());
        printer.print(')');
    }
}
