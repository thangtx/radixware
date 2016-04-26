/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.defs.ads.radixdoc;

import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef.TitleItem;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;

/**
 *
 * @author npopov
 */
public class EditorPresentationRadixdoc extends PresentationDefRadixdoc {

    AdsEditorPresentationDef editorDef = (AdsEditorPresentationDef) source;

    public EditorPresentationRadixdoc(AdsEditorPresentationDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void writeDefSpecificInfo(ContentContainer overview) {
        ContentContainer innerContent = overview.addNewBlock();
        innerContent.setStyle(DefaultStyle.NAMED);

        writeGeneralAttributes(innerContent);

        List<AdsEditorPageDef> editorPages = editorDef.getEditorPages().getAll(ExtendableDefinitions.EScope.ALL);
        getWriter().writeElementsList(innerContent, editorPages, "Editor Pages");

        List<AdsExplorerItemDef> children = editorDef.getExplorerItems().getChildren().getAll(ExtendableDefinitions.EScope.ALL);
        getWriter().writeExplorerChildrensList(innerContent, children, "Children");

        writeInheritPropertyAttributesInfo(innerContent);
        writeContextlessCommandsInfo(innerContent);
        writeObjectTitleFormatInfo(innerContent);
        final long restrs = ERestriction.CREATE.getValue() | ERestriction.DELETE.getValue() | ERestriction.UPDATE.getValue();
        writeRestrictionsInfo(innerContent, restrs);
    }

    private void writeGeneralAttributes(ContentContainer overview) {
        Table genericAttrTable = getWriter().addGeneralAttrTable(overview);
        AdsEditorPresentationDef baseEditor = editorDef.findBasePresentation().get();
        final boolean isBaseEditorInherited = baseEditor != null;
        getWriter().addStr2RefRow(genericAttrTable, "Base presentation", baseEditor, source.getOwnerClass());

        Table.Row rightsRow = genericAttrTable.addNewRow();
        rightsRow.addNewCell().addNewText().setStringValue("Rights inheritance mode");
        Table.Row.Cell rightsVal = rightsRow.addNewCell();
        if (editorDef.isRightsInheritanceModeInherited()) {
            rightsVal.addNewText().setStringValue("From base presentation");
        } else {
            EEditorPresentationRightsInheritanceMode mode = editorDef.getRightInheritanceMode();
            if (EEditorPresentationRightsInheritanceMode.FROM_DEFINED.equals(mode)) {
                rightsVal.addNewText().setStringValue("From defined presentation: ");
                AdsEditorPresentationDef definePres = editorDef.findRightsInheritanceDefinePresentation().get();
                if (definePres != null) {
                    getWriter().addRef(rightsVal, definePres, definePres.getModule());
                }
            } else if (EEditorPresentationRightsInheritanceMode.FROM_REPLACED.equals(mode)) {
                rightsVal.addNewText().setStringValue("From replaced presentation");
            } else {
                rightsVal.addNewText().setStringValue(mode.getName());
            }
        }

        getWriter().addStr2RefRow(genericAttrTable, "Replaced presentation", editorDef.findReplacedEditorPresentation().get(), source.getOwnerClass());

        if (isBaseEditorInherited) {
            getWriter().addStr2BoolRow(genericAttrTable, "Inherit children", editorDef.isExplorerItemsInherited());
            getWriter().addStr2BoolRow(genericAttrTable, "Inherit custom view", editorDef.isCustomViewInherited());
            getWriter().addStr2BoolRow(genericAttrTable, "Inherit editor pages", editorDef.isEditorPagesInherited());
        } else {
            getWriter().addStr2BoolRow(genericAttrTable, "Use custom view for Desktop", editorDef.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER));
            getWriter().addStr2BoolRow(genericAttrTable, "Use custom view for Web", editorDef.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB));
        }

        getWriter().addAllStrRow(genericAttrTable, "Client Environment", editorDef.getClientEnvironment().getName());
        getWriter().addStr2BoolRow(genericAttrTable, "Use default model", editorDef.isUseDefaultModel());
        getWriter().addStr2MslIdRow(genericAttrTable, "Display instead of null", editorDef.getObjectTitleFormat().getLocalizingBundleId(), editorDef.getObjectTitleFormat().getNullValTitleId());

        if (!source.isUseDefaultModel()) {
            getWriter().addStr2RefRow(genericAttrTable, "Model Class", editorDef.getModel(), editorDef);
        }
        getWriter().addStr2BoolRow(genericAttrTable, "Inherit property pesentation attributes", editorDef.isPropertyPresentationAttributesInherited());
    }

    private void writeInheritPropertyAttributesInfo(ContentContainer innerContent) {
        if (!editorDef.getPropertyPresentationAttributesCollection().isEmpty()) {
            Table presentAttrTable = getWriter().setBlockCollapsibleAndAddTable(innerContent.addNewBlock(), "Presentable Attributes",
                    "Name",
                    "Presentable",
                    "Not null",
                    "Visibility",
                    "Edit Possibility",
                    "Title");

            for (AdsEditorPresentationDef.PropertyAttributesSet prop : editorDef.getPropertyPresentationAttributesCollection().getAll(ExtendableDefinitions.EScope.ALL)) {
                Table.Row row = presentAttrTable.addNewRow();
                AdsPropertyDef propDef = null;
                if (prop.getLocal() != null) {
                    propDef = prop.getLocal().findProperty();
                }
                row.addNewCell().addNewText().setStringValue(propDef != null ? propDef.getName() : "");
                row.addNewCell().addNewText().setStringValue(prop.getPresentable() != null ? getWriter().boolAsStr(prop.getPresentable()) : "");
                row.addNewCell().addNewText().setStringValue(prop.getMandatory() != null ? getWriter().boolAsStr(prop.getMandatory()) : "");
                row.addNewCell().addNewText().setStringValue(prop.getVisibility() != null ? prop.getVisibility().getName() : "");
                row.addNewCell().addNewText().setStringValue(prop.getEditPossibility() != null ? prop.getEditPossibility().toString() : "");
                if (propDef != null && prop.getTitleId() != null && propDef.getLocalizingBundleId() != null) {
                    getWriter().addMslId(row.addNewCell(), propDef.getLocalizingBundleId(), prop.getTitleId());
                }
            }
        }
    }

    private void writeObjectTitleFormatInfo(ContentContainer innerContent) {
        if (!editorDef.getObjectTitleFormat().getItems().isEmpty()) {
            Table titleFormatTable = getWriter().setBlockCollapsibleAndAddTable(innerContent.addNewBlock(), "Object Title Format", "Property", "Format");
            for (TitleItem item : editorDef.getObjectTitleFormat().getItems()) {
                AdsPropertyDef prop = item.findProperty();
                if (prop != null) {
                    Table.Row row = titleFormatTable.addNewRow();
                    getWriter().addRef(row.addNewCell(), prop, prop.getModule());
                    if (item.getPatternId() != null && editorDef.getObjectTitleFormat().getLocalizingBundleId() != null) {
                        getWriter().addMslId(row.addNewCell(), editorDef.getObjectTitleFormat().getLocalizingBundleId(), item.getPatternId());
                    } else {
                        row.addNewCell().addNewText().setStringValue(item.getPattern());
                    }
                }
            }
        }
    }
}
