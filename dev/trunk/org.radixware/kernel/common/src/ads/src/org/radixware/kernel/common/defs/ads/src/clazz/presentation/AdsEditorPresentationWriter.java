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

import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IModalDisplayable;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef.PropertyAttributesSet;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.EPresentationAttrInheritance;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;

public class AdsEditorPresentationWriter extends AbstractDefinitionWriter<AdsEditorPresentationDef> {

    static final char[] EDITOR_PRESENTATION_META_SERVER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_SERVER_PACKAGE_NAME, "RadEditorPresentationDef".toCharArray(), '.');
    public static final char[] EDITOR_PRESENTATION_META_EXPLORER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadEditorPresentationDef".toCharArray(), '.');

    public AdsEditorPresentationWriter(JavaSourceSupport support, AdsEditorPresentationDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                return writeMetaImpl(printer, false);
            case WEB:
            case EXPLORER:
                WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
                printer.print("public final class ");
                //writeUsage(printer);
                printer.print(JavaSourceSupport.getName(def, printer instanceof IHumanReadablePrinter, true));
                printer.print(JavaSourceSupport.META_CLASS_SUFFIX);
                printer.enterBlock(1);
                printer.println("{");


                /*  AdsEditorPresentationDef epr = def.getHierarchy().findOverwritten().get();
                 boolean isOverwrite = epr != null;
                 */
                //RADIX-RADIX-8369
                if (def.isUseDefaultModel()/* && !isOverwrite*/) {
                    printer.print("private static final class ");
                    printer.print(JavaSourceSupport.getName(def, printer instanceof IHumanReadablePrinter, true));
                    printer.print("_DEF extends ");
                    printer.print(EDITOR_PRESENTATION_META_EXPLORER_CLASS_NAME);
                    printer.enterBlock();
                    printer.println('{');
                    printer.print(JavaSourceSupport.getName(def, printer instanceof IHumanReadablePrinter, true));
                    printer.enterBlock();
                    printer.println("_DEF(){");
                    printer.print("super(");
                    if (!writeMetaImpl(printer, true)) {
                        return false;
                    }
                    printer.println(");");
                    printer.leaveBlock();
                    printer.println("}");

                    printer.println("@Override");
                    printer.print("protected ");
                    printer.print(WriterUtils.EXPLORER_MODEL_CLASS_NAME);
                    printer.print(" createModelImpl(");
                    printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
                    printer.print(" userSession) {");

                    printer.print("    return new ");
                    AdsEditorPresentationDef.ModelClassInfo modelInfo = def.findActualModelClass();

                    writeCode(printer, modelInfo.clazz.getType(EValType.USER_CLASS, null));
                    if (modelInfo.useDefaultModel) {
                        printer.print(".");
                        printer.print(JavaSourceSupport.getName(modelInfo.clazz, printer instanceof IHumanReadablePrinter));
                        printer.print("_DefaultModel");
                        if (modelInfo.basePresentationId != null) {
                            printer.print('.');
                            printer.print(modelInfo.basePresentationId);
                            printer.print("_ModelAdapter");
                        }
                    }
                    printer.println("(userSession,this);");
                    printer.println("}");
                    printer.leaveBlock();
                    printer.println("}");
                    printer.print("public static final ");
                    printer.print(EDITOR_PRESENTATION_META_EXPLORER_CLASS_NAME);
                    printer.print(" rdxMeta = new ");
                    printer.print(JavaSourceSupport.getName(def, printer instanceof IHumanReadablePrinter, true));
                    printer.println("_DEF(); ");
                } else {
                    printer.print("public static final ");
                    printer.print(EDITOR_PRESENTATION_META_EXPLORER_CLASS_NAME);
                    printer.print(" rdxMeta = ");
                    //new AdsEditorPresentationWriter(getSupport(), def, UsagePurpose.EXPLORER_META).writeCode(printer);
                    if (!writeMetaImpl(printer, false)) {
                        return false;
                    }
                }
                printer.leaveBlock(1);
                printer.printlnSemicolon();
                printer.print("}");
                return true;
            default:
                return false;
        }
    }
    private static final String SERVER_PROPERTY_PRESENTATION_ATTRIBUTES_CLASS = "org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes";
    private static final String CLIENT_PROPERTY_PRESENTATION_ATTRIBUTES_CLASS = "org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes";

    protected boolean writeMetaImpl(CodePrinter printer, boolean paramsOnly) {
        EditorPresentationFinalAttributes attributes = new EditorPresentationFinalAttributes(def);

        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                /**
                 * public RadEditorPresentationDef( final Id id, final String
                 * name, final ERuntimeEnvironmentType environmentType, final Id
                 * basePresentationId, final int inheritanceMask, final
                 * RadEntityTitleFormatDef objectTitleFormat, final
                 * RadExplorerItemDef[] children, final Restrictions
                 * restrictions, final Id[] forbiddenProps, final
                 * EEditorPresentationRightsInheritanceMode rightsInheritMode,
                 * final Id inheritRightsFromPresId )
                 */
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                printer.println();
                printer.printStringLiteral(def.getName());
                printer.printComma();
                printer.println();
                WriterUtils.writeEnumFieldInvocation(printer, def.getClientEnvironment());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, def.getBasePresentationId());
                printer.printComma();
                printer.println();
                printer.print(EPresentationAttrInheritance.toBitField(attributes.finalInheritanceMask));
                printer.printComma();
                printer.println();

                if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.OBJECT_TITLE_FORMAT) || attributes.finalTitleFormat == null) {
                    WriterUtils.writeNull(printer);
                } else {
                    writeCode(printer, attributes.finalTitleFormat);
                }
                printer.printComma();
                printer.println();

//                if (def.isExplorerItemsInherited()) {
//                    WriterUtils.writeNull(printer);
//                } else {
                writeCode(printer, def.getExplorerItems());
//                }
                printer.printComma();
                printer.println();

                if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.RESTRICTIONS) || attributes.finalRestrictions == null) {
                    WriterUtils.writeNull(printer);
                } else {
                    writeCode(printer, attributes.finalRestrictions);
                }
                printer.printComma();
                printer.println();

                // property restrictions
                writeRadPropertyPresentationAttributes(printer, true);

//                if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.FORBIDDEN_PROPS)) {
//                    WriterUtils.writeNull(printer);
//                } else {
//                    List<Id> ids = def.getForbiddenProperties().getForbiddenPropIds();
//                    if (ids.isEmpty()) {
//                        WriterUtils.writeNull(printer);
//                    } else {
//                        WriterUtils.writeIdArrayUsage(printer, ids);
//                    }
//                }
                printer.printComma();
                printer.println();
                if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.RIGHTS_INHERITANCE_MODE)) {
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    printer.println();
                    WriterUtils.writeNull(printer);
                } else {
                    WriterUtils.writeEnumFieldInvocation(printer, def.getRightInheritanceMode());
                    printer.printComma();
                    printer.println();
                    WriterUtils.writeIdUsage(printer, def.getRightsSourceEditorPresentationId());
                }
                if (!def.isExplorerItemsInherited()) {
                    List<Id> ids = def.getExplorerItems().getInheritedExplorerItemIds();
                    Collections.sort(ids);
                    printer.printComma();
                    WriterUtils.writeIdArrayUsage(printer, ids);
                }

                return true;
            case EXPLORER:
            case WEB:
                /**
                 * RadEditorPresentationDef(final Id id, final String name,
                 * final Id basePresentationId, final Id classId, final Id
                 * tableId, final Id titleId, final Id iconId, final
                 * RadEditorPageDef[] editorPages, final
                 * RadEditorPages.PageOrder[] pageOrder, final
                 * RadExplorerItemDef[] explorerItems, final Id[]
                 * contextlesCommandsIds, final long restrictionsMask, final
                 * Id[] enabledCommandIds, final long inheritanceMask)
                 */
                if (!paramsOnly) {
                    printer.print("new ");
                    printer.print(EDITOR_PRESENTATION_META_EXPLORER_CLASS_NAME);
                    printer.print('(');
                }
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                printer.println();
                printer.printStringLiteral(def.getName());
                printer.printComma();
                WriterUtils.writeEnumFieldInvocation(printer, def.getClientEnvironment());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, def.getBasePresentationId());
                printer.printComma();
                printer.println();
                AdsEntityObjectClassDef clazz = def.getOwnerClass();
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
                    WriterUtils.writeIdUsage(printer, attributes.finalIconId);
                }
                printer.printComma();
                printer.println();
                if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.PAGES) || attributes.finalEditorPages == null) {
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    printer.println();
                    WriterUtils.writeNull(printer);

                } else {
                    writeCode(printer, attributes.finalEditorPages);
                }
                printer.printComma();
                printer.println();
                writeCode(printer, def.getExplorerItems());
                printer.printComma();
                WriterUtils.writeExplorerItemsOrderAndVisibility(printer, def.getExplorerItems());

                printer.printComma();
                printer.println();
                WriterUtils.writeIdArrayUsage(printer, def.getUsedContextlessCommands().getCommandIds());
                printer.printComma();
                printer.println();
                if (attributes.finalInheritanceMask.contains(EPresentationAttrInheritance.RESTRICTIONS) || attributes.finalRestrictions == null) {
                    printer.print(0L);
                    printer.printComma();
                    printer.println();
                    WriterUtils.writeNull(printer);
                } else {

                    writeCode(printer, attributes.finalRestrictions);
                    printer.printComma();
                    printer.println();
                    if (def.getRestrictions().isDenied(ERestriction.ANY_COMMAND)) {
                        WriterUtils.writeIdArrayUsage(printer, attributes.finalRestrictions.getEnabledCommandIds());
                    } else {
                        WriterUtils.writeNull(printer);
                    }
                }
                printer.printComma();
                printer.println();

                // write RadPropertyPresentationAttributes[]
                writeRadPropertyPresentationAttributes(printer, false);

                printer.printComma();
                printer.println();

                printer.print(EPresentationAttrInheritance.toBitField(attributes.finalInheritanceMask));

                final IModalDisplayable.ModialViewSizeInfo sizeInfo = def.getModialViewSizeInfo(usagePurpose.getEnvironment());
                if (sizeInfo != null) {
                    printer.printComma();
                    printer.print(sizeInfo.getWidth());
                    printer.printComma();
                    printer.print(sizeInfo.getHeight());
                }
                if (!def.isExplorerItemsInherited()) {
                    List<Id> ids = def.getExplorerItems().getInheritedExplorerItemIds();
                    Collections.sort(ids);
                    printer.printComma();
                    WriterUtils.writeIdArrayUsage(printer, ids);
                }
                if (!paramsOnly) {
                    printer.print(')');
                }
                return true;

            default:
                return false;
        }
    }

    private void writeRadPropertyPresentationAttributes(CodePrinter printer, boolean server) {
        final List<PropertyAttributesSet> restrictions = def.getPropertyPresentationAttributesCollection().getAll(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE);
        if (!restrictions.isEmpty()) {
            printer.print("new ");
            printer.print(server ? SERVER_PROPERTY_PRESENTATION_ATTRIBUTES_CLASS : CLIENT_PROPERTY_PRESENTATION_ATTRIBUTES_CLASS);
            printer.println("[] {");
            printer.enterBlock();
            boolean first = true;
            for (final PropertyAttributesSet restriction : restrictions) {
                if (restriction.isEmpty()) {
                    continue;
                }

                if (first) {
                    first = false;
                } else {
                    printer.printComma();
                    printer.println();
                }

                if (server) {
                    writeServerRadPropertyPresentationAttributes(printer, restriction);
                } else {
                    writeClientRadPropertyPresentationAttributes(printer, restriction);
                }
            }
            printer.println();
            printer.leaveBlock();
            printer.print("}");
        } else {
            printer.print('(');
            printer.print(server ? SERVER_PROPERTY_PRESENTATION_ATTRIBUTES_CLASS : CLIENT_PROPERTY_PRESENTATION_ATTRIBUTES_CLASS);
            printer.print("[]) ");
            WriterUtils.writeNull(printer);
        }
    }

    private void writeServerRadPropertyPresentationAttributes(CodePrinter printer, PropertyAttributesSet restriction) {
        printer.print("new ");
        printer.print(SERVER_PROPERTY_PRESENTATION_ATTRIBUTES_CLASS);
        printer.print('(');
        WriterUtils.writeIdUsage(printer, restriction.getPropertyId());

        printer.printComma();
        printer.printSpace();
        WriterUtils.writeBoolean(printer, restriction.getPresentable());

        printer.printComma();
        printer.printSpace();
        WriterUtils.writeEnumFieldInvocation(printer, restriction.getEditPossibility());

        printer.printComma();
        printer.printSpace();
        WriterUtils.writeBoolean(printer, restriction.getMandatory());

        printer.print(')');
    }

    private void writeClientRadPropertyPresentationAttributes(CodePrinter printer, AdsEditorPresentationDef.PropertyAttributesSet restriction) {
        printer.print("new ");
        printer.print(CLIENT_PROPERTY_PRESENTATION_ATTRIBUTES_CLASS);
        printer.print('(');
        WriterUtils.writeIdUsage(printer, restriction.getPropertyId());

        printer.printComma();
        printer.printSpace();
        WriterUtils.writeEnumFieldInvocation(printer, restriction.getVisibility());

        printer.printComma();
        printer.printSpace();
        WriterUtils.writeEnumFieldInvocation(printer, restriction.getEditPossibility());

        printer.printComma();
        printer.printSpace();
        WriterUtils.writeBoolean(printer, restriction.getMandatory());

        printer.printComma();
        printer.printSpace();
        WriterUtils.writeIdUsage(printer, restriction.getTitleId());

        printer.printComma();
        printer.printSpace();
        final AdsDefinition titleOwnerDefinition = restriction.findTitleOwnerDefinition();
        WriterUtils.writeIdUsage(printer, titleOwnerDefinition != null ? titleOwnerDefinition.getId() : null);

        printer.print(')');
    }
}
