/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.defs.ads.radixdoc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef.Parameter;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.enums.EPropertyValueStorePossibility;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.radixdoc.DefaultAttributes;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Ref;
import org.radixware.schemas.radixdoc.Table;
import org.radixware.schemas.radixdoc.Text;

public class FilterRadixdoc extends AdsDefinitionRadixdoc<AdsFilterDef> {

    public FilterRadixdoc(AdsFilterDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    public void documentDescriptionExtensions(ContentContainer container) {
    }

    @Override
    public void documentOverview(ContentContainer container) {

        final Block overview = container.addNewBlock();
        overview.setStyle(DefaultStyle.CHAPTER);
        getWriter().appendStyle(overview, DefaultStyle.CHAPTER, DefaultStyle.OVERVIEW);
        getWriter().addStrTitle(overview, "Overview");
        final Block innerContent = overview.addNewBlock();
        innerContent.setStyle(DefaultStyle.NAMED);
        if (source.getDescriptionId() != null || source.getDescription() != null && !source.getDescription().isEmpty()) {
            getWriter().documentDescription(innerContent, source);
        }
        Table attrTable = overview.addNewTable();
        attrTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row.Cell headerCell = attrTable.addNewRow().addNewCell();
        headerCell.setStyle(DefaultStyle.SUB_HEAD);
        headerCell.setColspan(2);
        headerCell.addNewText().setStringValue("General Attributes");

        if (source.getTitleId() != null) {
            getWriter().addStr2MslIdRow(attrTable, "Title", source.getLocalizingBundleId(), source.getTitleId());
        }
        List<String> modifiers = new ArrayList<>();
        if (getSource().isFinal()) {
            modifiers.add("Final");
        }
        if (getSource().isPublished()) {
            modifiers.add("Published");
        }
        if (!modifiers.isEmpty()) {
            getWriter().addAllStrRow(attrTable, "Modifiers", modifiers.toString().substring(1, modifiers.toString().length() - 1));
        }
        getWriter().addAllStrRow(attrTable, "Environment", source.getClientEnvironment().getName());
        getWriter().addStr2BoolRow(attrTable, "Use custom view for Desktop", source.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER));
        getWriter().addStr2BoolRow(attrTable, "Use custom view for Web", source.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB));

        Table.Row modelClassRow = attrTable.addNewRow();
        modelClassRow.addNewCell().addNewText().setStringValue("Filter model class");
        Table.Row.Cell modelClassCell = modelClassRow.addNewCell();
        Ref modelClassRef = modelClassCell.addNewRef();
        modelClassRef.setPath(resolve(source, source.getModel()));
        final RadixIconResource modelClassicon = new RadixIconResource(source.getModel().getIcon());
        modelClassRef.addNewResource().setSource(modelClassicon.getKey());
        addResource(modelClassicon);
        getWriter().addText(modelClassRef, source.getModel().getQualifiedName());

        final Table contextlessCommTable = overview.addNewTable();
        contextlessCommTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row contextlessCommHeaderRow = contextlessCommTable.addNewRow();
        contextlessCommHeaderRow.setStyle(DefaultStyle.SUB_HEAD);
        contextlessCommHeaderRow.addNewCell().addNewText().setStringValue("Contextless Command");

        List<Definition> allDependecies = new LinkedList<>();
        source.getUsedContextlessCommands().collectDependences(allDependecies);
        boolean contCommExist = false;
        for (Definition def : allDependecies) {
            if (def instanceof AdsContextlessCommandDef) {
                contCommExist = true;
                Table.Row commRow = contextlessCommTable.addNewRow();
                Table.Row.Cell commCell = commRow.addNewCell();
                Ref commRef = commCell.addNewRef();
                commRef.setPath(resolve(def, def));
                final RadixIconResource icon = new RadixIconResource(def.getIcon());
                commRef.addNewResource().setSource(icon.getKey());
                addResource(icon);
                getWriter().addText(commRef, def.getQualifiedName());
            }
        }
        if (contCommExist == false) {
            contextlessCommTable.addNewRow().addNewCell().addNewText().setStringValue("<Not Defined>");
        }

        final Table sortTable = overview.addNewTable();
        sortTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row sortHeaderRow = sortTable.addNewRow();
        sortHeaderRow.setStyle(DefaultStyle.SUB_HEAD);
        Table.Row.Cell sortHeaderCell = sortHeaderRow.addNewCell();
        sortHeaderCell.setColspan(2);
        sortHeaderCell.addNewText().setStringValue("Sortings");
        
        AdsSortingDef defaultSorting = source.findDefaultSorting();
        Table.Row defSortRow = sortTable.addNewRow();
        defSortRow.addNewCell().addNewText().setStringValue("Default sorting");
        Table.Row.Cell defSortCell = defSortRow.addNewCell();
        if (defaultSorting != null) {
            Ref sortRef = defSortCell.addNewRef();
            sortRef.setPath(resolve(source, defaultSorting));
            if (defaultSorting.getIcon() != null) {
                final RadixIconResource icon = new RadixIconResource(defaultSorting.getIcon());
                sortRef.addNewResource().setSource(icon.getKey());
                addResource(icon);
            }
            getWriter().addText(sortRef, defaultSorting.getQualifiedName());
        } else {
            getWriter().addText(defSortCell, "<Not Defined>");
        }
        
        getWriter().addStr2BoolRow(sortTable, "Allow usage of custom sortings", source.isCustomSortingEnabled());
        Table.Row baseSortRow = sortTable.addNewRow();
        baseSortRow.addNewCell().addNewText().setStringValue("Base sortings");
        if (source.isAnyBaseSortingEnabled()) {
            baseSortRow.addNewCell().addNewText().setStringValue(" Any base sorting enabled");
        } else {
            Table.Row.Cell baseSortCell = baseSortRow.addNewCell();
            Text baseSortText = baseSortCell.addNewText();
            int enm = 0;
            for (AdsFilterDef.EnabledSorting sort : source.getEnabledSortings()) {
                if (baseSortText.getStringValue().isEmpty()) {
                    Ref ref = baseSortCell.addNewRef();
                    ref.setPath(resolve(source, sort.findSorting()));
                    ref.addNewText().setStringValue(sort.findSorting().getName());
                }
                if (enm++ != source.getEnabledSortings().list().size() - 1) {
                    baseSortCell.addNewText().setStringValue(", ");
                }
            }
        }
        if (!source.getParameters()
                .list().isEmpty()) {
            Block filterParamBlock = container.addNewBlock();
            writeFiltersParameters(filterParamBlock);
        }
    }

    public void writeFiltersParameters(Block block) {
        final Block paramBlock = block.addNewBlock();
        paramBlock.setStyle(DefaultStyle.CHAPTER);
        getWriter().addStrTitle(paramBlock, "Filter Parameters");

        Block nameParamBlock = paramBlock.addNewBlock();
        nameParamBlock.setStyle(DefaultStyle.DESCRIPTION);
        Table paramTable = nameParamBlock.addNewTable();
        paramTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row paramRow = paramTable.addNewRow();
        paramRow.setStyle(DefaultStyle.SUB_HEAD);
        Table.Row.Cell paramCell = paramRow.addNewCell();
        paramCell.addNewText().setStringValue("Filter Parameter");
        paramCell.setColspan(2);
        List<Parameter> paramList = source.getParameters().list();
        for (int i = 0; i < source.getParameters().list().size(); i++) {

            Table.Row paramNameRow = paramTable.addNewRow();
            Table.Row.Cell paramTypeCell = paramNameRow.addNewCell();
            paramTypeCell.setStyle("modifiers");
            final TypeDocument typeParam = new TypeDocument();
            typeParam.addType(paramList.get(i).getType(), paramList.get(i));
            getWriter().documentType(paramTypeCell, typeParam, paramList.get(i));

            Table.Row.Cell paramNameCell = paramNameRow.addNewCell();
            Ref paramRef = paramNameCell.addNewRef();
            paramRef.setPath(resolve(source, paramList.get(i)));
            final RadixIconResource icon = new RadixIconResource(paramList.get(i).getIcon());
            paramRef.addNewResource().setSource(icon.getKey());
            addResource(icon);
            paramRef.addNewText().setStringValue(paramList.get(i).getName());
        }
        final Block detailBlock = paramBlock.addNewBlock();
        getWriter().addStrTitle(detailBlock, "Parameters Detail");
        for (int i = 0; i < source.getParameters().list().size(); i++) {
            Block nameBlock = detailBlock.addNewBlock();
            nameBlock.setStyle(DefaultStyle.NAMED);
            Block commonBlock = nameBlock.addNewBlock();
            commonBlock.setStyle(DefaultStyle.TITLE);
            final TypeDocument typeParam = new TypeDocument();
            typeParam.addType(paramList.get(i).getType(), paramList.get(i)).addString(" ");
            getWriter().documentType(commonBlock, typeParam, paramList.get(i));
            final RadixIconResource icon = new RadixIconResource(paramList.get(i).getIcon());
            commonBlock.addNewResource().setSource(icon.getKey());
            addResource(icon);
            Text nameText = commonBlock.addNewText();
            nameText.setStyle(DefaultStyle.IDENTIFIER);
            nameText.setStringValue(paramList.get(i).getName());
            getWriter().setAttribute(commonBlock, DefaultAttributes.ANCHOR, paramList.get(i).getId().toString());
            Block descripBlock = nameBlock.addNewBlock();
            if (paramList.get(i).getDescriptionId() != null || (paramList.get(i).getDescription() != null && !paramList.get(i).getDescription().isEmpty())) {
                if (paramList.get(i).getDescriptionId() != null && paramList.get(i).getLocalizingBundleId() != null) {
                    getWriter().addMslId(descripBlock, paramList.get(i).getLocalizingBundleId(), paramList.get(i).getDescriptionId());
                } else {
                    getWriter().addText(descripBlock, paramList.get(i).getDescription());
                }
            }
            Block attrBlock = nameBlock.addNewBlock();
            attrBlock.setStyle(DefaultStyle.DESCRIPTION);
            Table attrTable = attrBlock.addNewTable();
            attrTable.setStyle(DefaultStyle.MEMBERS);
            Table.Row.Cell headerCell = attrTable.addNewRow().addNewCell();
            headerCell.setStyle(DefaultStyle.SUB_HEAD);
            headerCell.setColspan(2);
            headerCell.addNewText().setStringValue("General Attributes");

            if (paramList.get(i).getType() != null) {
                Table.Row typeRow = attrTable.addNewRow();
                Table.Row.Cell paramTypeCell = typeRow.addNewCell();
                paramTypeCell.addNewText().setStringValue("Type");
                Table.Row.Cell typeCell = typeRow.addNewCell();
                final TypeDocument typePar = new TypeDocument();
                typePar.addType(paramList.get(i).getType(), paramList.get(i));
                getWriter().documentType(typeCell, typePar, paramList.get(i));
            } else {
                getWriter().addAllStrRow(attrTable, "Type", "<Undefined>");
            }
            if (paramList.get(i).getDefaultValue() != null) {
                getWriter().addAllStrRow(attrTable, "Default Value", paramList.get(i).getDefaultValue().toString());
            } else {
                getWriter().addAllStrRow(attrTable, "Default Value", "<Not Defined>");
            }
            List<String> modifiers = new ArrayList<>();
            if (paramList.get(i).isFinal()) {
                modifiers.add("Final");
            }
            if (paramList.get(i).isPublished()) {
                modifiers.add("Published");
            }
            if (!modifiers.isEmpty()) {
                getWriter().addAllStrRow(attrTable, "Modifiers", modifiers.toString().substring(1, modifiers.toString().length() - 1));
            }
            if (paramList.get(i).getTitleId() != null) {
                getWriter().addStr2MslIdRow(attrTable, "Title", paramList.get(i).getLocalizingBundleId(), paramList.get(i).getTitleId());
            }
            if (paramList.get(i).getEditOptions().getNullValTitleId() != null) {
                getWriter().addStr2MslIdRow(attrTable, "Display instead of '<Not Defined>'", paramList.get(i).getLocalizingBundleId(), paramList.get(i).getEditOptions().getNullValTitleId());
            } else {
                getWriter().addAllStrRow(attrTable, "Display instead of '<Not Defined>'", "");
            }
            if (paramList.get(i).getEditOptions().findParentSelectorPresentation() != null) {
                getWriter().addAllStrRow(attrTable, "Parent Selector Presentation", paramList.get(i).getEditOptions().findParentSelectorPresentation().getName());
            }
            getWriter().addAllStrRow(attrTable, "Edit environment", paramList.get(i).getEditOptions().getEditEnvironment().getName());
            if (paramList.get(i).getEditOptions().getEditMask() != null) {
                getWriter().addAllStrRow(attrTable, "Input mask type", paramList.get(i).getEditOptions().getEditMask().getType().getAsStr());
            } else {
                getWriter().addAllStrRow(attrTable, "Input mask type", "Default edit mask");
            }
            if (!EPropertyValueStorePossibility.NONE.equals(paramList.get(i).getEditOptions().getValueStorePossibility())) {
                getWriter().addAllStrRow(attrTable, "Value import/export", paramList.get(i).getEditOptions().getValueStorePossibility().getName());
            }
            getWriter().addStr2BoolRow(attrTable, "Use custom dialog", paramList.get(i).getEditOptions().isShowDialogButton());
            getWriter().addStr2BoolRow(attrTable, "Custom edit only", paramList.get(i).getEditOptions().isCustomEditOnly());

            if (paramList.get(i).getEditOptions().findCustomDialog(ERuntimeEnvironmentType.EXPLORER) != null) {
                AdsDefinition custDialExp = paramList.get(i).getEditOptions().findCustomDialog(ERuntimeEnvironmentType.EXPLORER).getDialogDef();
                Table.Row custDialRow = attrTable.addNewRow();
                Table.Row.Cell custDialCell = custDialRow.addNewCell();
                custDialCell.addNewText().setStringValue("Exporer variant");
                Table.Row.Cell custDialNameCell = custDialRow.addNewCell();
                Ref custDialRef = custDialNameCell.addNewRef();
                custDialRef.setPath(resolve(source, custDialExp));
                final RadixIconResource custDialIcon = new RadixIconResource(custDialExp.getIcon());
                custDialRef.addNewResource().setSource(custDialIcon.getKey());
                addResource(custDialIcon);
                custDialRef.addNewText().setStringValue(custDialExp.getName());
            }
            if (paramList.get(i).getEditOptions().findCustomDialog(ERuntimeEnvironmentType.WEB) != null) {
                AdsDefinition custDialWeb = paramList.get(i).getEditOptions().findCustomDialog(ERuntimeEnvironmentType.WEB).getDialogDef();
                Table.Row custDialRow = attrTable.addNewRow();
                Table.Row.Cell custDialCell = custDialRow.addNewCell();
                custDialCell.addNewText().setStringValue("Web variant");
                Table.Row.Cell custDialNameCell = custDialRow.addNewCell();
                Ref custDialRef = custDialNameCell.addNewRef();
                custDialRef.setPath(resolve(source, custDialWeb));
                final RadixIconResource custDialIcon = new RadixIconResource(custDialWeb.getIcon());
                custDialRef.addNewResource().setSource(custDialIcon.getKey());
                addResource(custDialIcon);
                custDialRef.addNewText().setStringValue(custDialWeb.getName());
            }
            getWriter().addStr2BoolRow(attrTable, "Not null", paramList.get(i).getEditOptions().isNotNull());
            getWriter().addStr2BoolRow(attrTable, "Store edit history", paramList.get(i).getEditOptions().isStoreEditHistory());
        }
    }
}
