/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.defs.ads.radixdoc;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef.OrderBy;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.schemas.radixdoc.Ref;

public class SortingRadixdoc extends AdsDefinitionRadixdoc<AdsSortingDef> {

    public SortingRadixdoc(AdsSortingDef source, Page page, DocumentOptions options) {
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
        Block usedPropertiesBlock = container.addNewBlock();
        writeUsedProperties(usedPropertiesBlock);
    }

    public void writeUsedProperties(Block block) {
        final Block usedPropBlock = block.addNewBlock();
        usedPropBlock.setStyle(DefaultStyle.CHAPTER);
        getWriter().addStrTitle(usedPropBlock, "Used Properties");
        final Table propTable = usedPropBlock.addNewTable();
        propTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row propHeaderRow = propTable.addNewRow();
        propHeaderRow.setStyle(DefaultStyle.SUB_HEAD);
        Table.Row.Cell propHeaderCell = propHeaderRow.addNewCell();
        propHeaderCell.setColspan(3);
        propHeaderCell.addNewText().setStringValue("Property");
        for (OrderBy order : source.getOrder()) {
            Table.Row propRow = propTable.addNewRow();
            Table.Row.Cell propTypeCell = propRow.addNewCell();
            propTypeCell.setStyle("modifiers");
            AdsPropertyDef prop = order.findProperty();
            final TypeDocument typeProp = new TypeDocument();
            typeProp.addType(prop.getValue().getType(), prop);
            getWriter().documentType(propTypeCell, typeProp, prop);
            Table.Row.Cell propCell = propRow.addNewCell();
            Ref ref = propCell.addNewRef();
            ref.setPath(resolve(source, prop));
            final RadixIconResource modelClassicon = new RadixIconResource(prop.getIcon());
            ref.addNewResource().setSource(modelClassicon.getKey());
            addResource(modelClassicon);
            getWriter().addText(ref, prop.getName());
            getWriter().addText(propCell," ("+order.getOrder().getName()+")");
        }
    }
}
