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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.CreatePresentationsList;
import org.radixware.kernel.common.defs.ads.clazz.presentation.SelectorAddons;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;

/**
 *
 * @author npopov
 */
public class SelectorPresentationRadixdoc extends PresentationDefRadixdoc {

    AdsSelectorPresentationDef selectorDef = (AdsSelectorPresentationDef) source;

    public SelectorPresentationRadixdoc(AdsSelectorPresentationDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void writeDefSpecificInfo(ContentContainer overview) {
        ContentContainer innerContent = overview.addNewBlock();
        innerContent.setStyle(DefaultStyle.NAMED);

        writeGeneralAttributes(innerContent);
        writeContextlessCommandsInfo(innerContent);
        writeSelectorColumns(innerContent, selectorDef.getColumns());
        writeAddonsInfo(innerContent, selectorDef.getAddons());

        final long restrs = ERestriction.INSERT_INTO_TREE.getValue()
                | ERestriction.INSERT_ALL_INTO_TREE.getValue()
                | ERestriction.RUN_EDITOR.getValue()
                | ERestriction.EDITOR.getValue()
                | ERestriction.CREATE.getValue()
                | ERestriction.DELETE.getValue()
                | ERestriction.DELETE_ALL.getValue()
                | ERestriction.UPDATE.getValue()
                | ERestriction.MULTIPLE_COPY.getValue()
                | ERestriction.CONTEXTLESS_USAGE.getValue();
        writeRestrictionsInfo(innerContent, restrs);
    }

    private void writeGeneralAttributes(ContentContainer overview) {
        Table generalAttrTable = getWriter().addGeneralAttrTable(overview);
        AdsSelectorPresentationDef baseSelector = selectorDef.findBaseSelectorPresentation().get();
        if (baseSelector != null) {
            getWriter().addStr2RefRow(generalAttrTable, "Base selector presentation", baseSelector, baseSelector.getOwnerDef());
            getWriter().addStr2BoolRow(generalAttrTable, "Inherit creation class catalog", selectorDef.isCreationClassCatalogInherited());
            getWriter().addStr2BoolRow(generalAttrTable, "Inherit custom view", selectorDef.isCustomViewInherited());
        } else {
            getWriter().addAllStrRow(generalAttrTable, "Base selector presentation", "<Not Defined>");
        }

        Table.Row presForEditingRow = generalAttrTable.addNewRow();
        presForEditingRow.addNewCell().addNewText().setStringValue("Editor presentations for editing");
        Table.Row.Cell presForEditingValueCell = presForEditingRow.addNewCell();
        List<AdsEditorPresentationDef> editorsList = new LinkedList<>();
        for (AdsSelectorPresentationDef.EditorPresentations.PresentationInfo presInfo : selectorDef.getEditorPresentations().getPresentationInfos()) {
            AdsEditorPresentationDef editor = presInfo.findPresentation();
            if (editor != null) {
                editorsList.add(editor);
            }
        }
        writeColumnSeparatedList(presForEditingValueCell, editorsList);

        Table.Row presForCreationRow = generalAttrTable.addNewRow();
        presForCreationRow.addNewCell().addNewText().setStringValue("Editor presentations for creation");
        Table.Row.Cell presForCreationValueCell = presForCreationRow.addNewCell();
        editorsList.clear();
        for (CreatePresentationsList.PresentationRef presRef : selectorDef.getCreatePresentationsList().getPresentationRefs()) {
            AdsEditorPresentationDef editor = presRef.findEditorPresentation();
            if (editor != null) {
                editorsList.add(editor);
            }
        }
        writeColumnSeparatedList(presForCreationValueCell, editorsList);

        AdsClassCatalogDef classCatalog = selectorDef.findCreationClassCatalog().get();
        if (classCatalog != null) {
            getWriter().addStr2RefRow(generalAttrTable, "Creation class catalog", classCatalog, classCatalog.getOwnerDef());
        } else {
            getWriter().addAllStrRow(generalAttrTable, "Creation class catalog", "<Not Defined>");
        }
        if (!source.isCustomViewInherited()) {
            getWriter().addStr2BoolRow(generalAttrTable, "Use custom view for Desktop", selectorDef.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER));
            getWriter().addStr2BoolRow(generalAttrTable, "Use custom view for Web", selectorDef.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB));
        }

        if (source.isUseDefaultModel()) {
            getWriter().addStr2BoolRow(generalAttrTable, "Use default model", true);
        } else {
            getWriter().addStr2RefRow(generalAttrTable, "Model Class", source.getModel(), source.getModel().getOwnerDef());
        }
        getWriter().addAllStrRow(generalAttrTable, "Client Environment", selectorDef.getClientEnvironment().getName());
        getWriter().addStr2BoolRow(generalAttrTable, "Enable auto expand", selectorDef.isAutoExpandEnabled());
        getWriter().addStr2BoolRow(generalAttrTable, "Enable position restore", selectorDef.isRestorePositionEnabled());
        if (selectorDef.isColumnsInherited()) {
            getWriter().addStr2BoolRow(generalAttrTable, "Selector columns inherited", true);
        }
    }

    private void writeSelectorColumns(ContentContainer innerContent, RadixObjects<AdsSelectorPresentationDef.SelectorColumn> columns) {
        if (selectorDef.isColumnsInherited()) {
            return;
        }
        Table selectorColumns = getWriter().setBlockCollapsibleAndAddTable(innerContent.addNewBlock(),
                "Selector Columns",
                "Visibility",
                "Property",
                "Align",
                "Size Policy",
                "English Title",
                "Russian Title"
        );
        for (AdsSelectorPresentationDef.SelectorColumn col : columns) {
            Table.Row row = selectorColumns.addNewRow();
            row.addNewCell().addNewText().setStringValue(col.getVisibility().getName());
            getWriter().addRef(row.addNewCell(), col.findProperty(), col.findProperty().getModule());
            row.addNewCell().addNewText().setStringValue(col.getAlign().getName());
            row.addNewCell().addNewText().setStringValue(col.getSizePolicy().getName());
            row.addNewCell().addNewText().setStringValue(col.getTitle(EIsoLanguage.ENGLISH));
            row.addNewCell().addNewText().setStringValue(col.getTitle(EIsoLanguage.RUSSIAN));
        }
    }

    private void writeAddonsInfo(ContentContainer innerContent, SelectorAddons addons) {
        if (selectorDef.isAddonsInherited()) {
            return;
        }

        Table addonsTable = getWriter().setBlockCollapsibleAndAddTable(innerContent.addNewBlock(), "Filters, Sortings");
        AdsFilterDef defaultFilter = addons.findDefaultFilter();
        if (defaultFilter != null) {
            getWriter().addStr2RefRow(addonsTable, "Default filter", defaultFilter, defaultFilter.getOwnerDef());
        }

        if (addons.isAnyBaseFilterEnabled()) {
            getWriter().addStr2BoolRow(addonsTable, "Any base filter enabled", true);
        } else {
            Table.Row filtersRow = addonsTable.addNewRow();
            filtersRow.addNewCell().addNewText().setStringValue("Only specified base filters enabled");
            Table.Row.Cell filtersCell = filtersRow.addNewCell();
            List<AdsFilterDef> filters = new ArrayList<>();
            for (Id filterId : addons.getEnabledFilterIds()) {
                AdsFilterDef filterDef = addons.findFilterById(filterId);
                if (filterDef != null) {
                    filters.add(filterDef);
                }
            }
            writeColumnSeparatedList(filtersCell, filters);
        }

        getWriter().addStr2BoolRow(addonsTable, "Allow usage of custom filters", addons.isCustomFilterEnabled());

        if (addons.isFilterObligatory()) {
            getWriter().addStr2BoolRow(addonsTable, "Filter is obligatory", true);
        } else {
            AdsSortingDef defaultSorting = addons.findDefaultSorting();
            if (defaultSorting != null) {
                getWriter().addStr2RefRow(addonsTable, "Default sorting", defaultSorting, defaultSorting.getOwnerDef());
            } else {
                getWriter().addAllStrRow(addonsTable, "Default sorting", "<Not Defined>");
            }

            getWriter().addStr2BoolRow(addonsTable, "Allow usage of custom sortings", addons.isCustomSortingEnabled());

            if (addons.isAnyBaseSortingEnabled()) {
                getWriter().addStr2BoolRow(addonsTable, "Any base sorting enabled", true);
            } else {
                Table.Row sortingsRow = addonsTable.addNewRow();
                sortingsRow.addNewCell().addNewText().setStringValue("Only specified base sortings enabled");
                Table.Row.Cell sortingsCell = sortingsRow.addNewCell();
                List<AdsSortingDef> sortings = new ArrayList<>();
                for (Id sortingId : addons.getEnabledSortingIds()) {
                    AdsSortingDef sortingDef = addons.findSortingById(sortingId);
                    if (sortingDef != null) {
                        sortings.add(sortingDef);
                    }
                }
                writeColumnSeparatedList(sortingsCell, sortings);
            }
        }
    }

    private void writeColumnSeparatedList(ContentContainer container, Collection<? extends AdsDefinition> elements) {
        String prefix = "";
        boolean first = true;
        for (AdsDefinition def : elements) {
            if (def != null) {
                container.addNewText().setStringValue(prefix);
                if (first) {
                    prefix = ", ";
                    first = false;
                }
                getWriter().addRef(container, def, def.getOwnerDef());
            }
        }
    }
}
