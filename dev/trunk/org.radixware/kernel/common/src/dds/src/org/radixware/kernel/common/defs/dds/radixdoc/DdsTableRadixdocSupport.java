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
package org.radixware.kernel.common.defs.dds.radixdoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsColumnInfo;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.enums.EDdsConstraintDbOption;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.radixdoc.DefaultAttributes;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.kernel.common.radixdoc.RadixdocUtils;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Ref;
import org.radixware.schemas.radixdoc.Resource;
import org.radixware.schemas.radixdoc.Table;
import org.radixware.schemas.radixdoc.Text;

public class DdsTableRadixdocSupport extends DdsDefinitionRadixdoc<DdsTableDef> {

    private static final Id AUDIT_TRIGGER_DESCRIPTION_ID = Id.Factory.loadFrom("mlsNDNKDELXRRF5LKEEN4SC35L4GI");

    public DdsTableRadixdocSupport(DdsTableDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    public IRadixdocPage document(org.radixware.schemas.radixdoc.Page page, DocumentOptions options) {
        return new DdsDefinitionRadixdoc(getSource(), page, options);
    }

    @Override
    protected Block documentHead(Page page) {
        final Block headChapter = page.addNewBlock();
        headChapter.setStyle(DefaultStyle.HEAD);
        final Block subTitleBlock = headChapter.addNewBlock();
        subTitleBlock.setStyle(DefaultStyle.SUB_TITLE);
        subTitleBlock.addNewText().setStringValue("Module ");
        final Ref moduleRef = subTitleBlock.addNewRef();
        moduleRef.setPath(resolve(source, source.getModule()));
        moduleRef.addNewText().setStringValue(source.getModule().getQualifiedName());
        final Block titleBlock = headChapter.addNewBlock();
        titleBlock.setStyle(DefaultStyle.TITLE);
        final Resource icon = titleBlock.addNewResource();
        final RadixIconResource iconResource = new RadixIconResource(source.getIcon());
        icon.setSource(iconResource.getKey());
        addResource(iconResource);
        final StringBuilder name = new StringBuilder();
        if (source.isDeprecated()) {
            getWriter().appendStyle(titleBlock, DefaultStyle.DEPRECATED);
        }
        name.append(source.getTypeTitle()).append(' ').append(source.getName());
        getWriter().addStrTitle(titleBlock, name.toString());
        return headChapter;
    }

    @Override
    public void documentContent(Page page) {
        if (getSource() instanceof DdsTableDef) {
            Block block = page.addNewBlock();
            block.setStyle(DefaultStyle.CHAPTER);
            Text text = block.addNewText();
            text.setStringValue("Columns");
            text.setMeta("title");
            Table table = block.addNewTable();
            Table.Row head = table.addNewRow();
            head.setMeta("head");
            head.addNewCell().addNewText().setStringValue("Name");
            head.addNewCell().addNewText().setStringValue("Database Name");
            head.addNewCell().addNewText().setStringValue("Type");
            head.addNewCell().addNewText().setStringValue("Database Type");
            head.addNewCell().addNewText().setStringValue("PK");
            head.addNewCell().addNewText().setStringValue("Not Null");
            head.addNewCell().addNewText().setStringValue("FK");
            head.addNewCell().addNewText().setStringValue("Sequence");
            head.addNewCell().addNewText().setStringValue("Description");

            final DdsTableDef ddsTable = (DdsTableDef) getSource();
            for (DdsColumnDef column : ddsTable.getColumns().get(ExtendableDefinitions.EScope.ALL)) {
                final Table.Row row = table.addNewRow();
                Table.Row.Cell nameCell = row.addNewCell();
                nameCell.addNewText().setStringValue(column.getName());
                if (column.isDeprecated()) {
                    nameCell.setStyle(DefaultStyle.DEPRECATED);
                }
                getWriter().setAttribute(nameCell, DefaultAttributes.ANCHOR, getIdentifier(column));
                row.addNewCell().addNewText().setStringValue(column.getDbName());
                row.addNewCell().addNewText().setStringValue(column.getValType().getName());
                row.addNewCell().addNewText().setStringValue(column.getDbType());
                row.addNewCell().addNewText().setStringValue(column.isPrimaryKey() ? "\u2714" : "");
                row.addNewCell().addNewText().setStringValue(column.isNotNull() ? "\u2714" : "");
                row.addNewCell().addNewText().setStringValue(column.isForeignKey() ? "\u2714" : "");
                final Table.Row.Cell sequenceCell = row.addNewCell();
                Text sequenceName = sequenceCell.addNewText();
                try {
                    Ref ref = sequenceCell.addNewRef();
                    ref.setPath(resolve(source, column.getSequence()));
                    getWriter().addText(ref, column.getSequence().getName());
                } catch (DefinitionNotFoundError ex) {
                    sequenceName.setStringValue("-");
                }
                if (column.getDescriptionId() != null && column.findExistingLocalizingBundle() != null) {
                    getWriter().addMslId(row.addNewCell(), column.findExistingLocalizingBundle().getId(), column.getDescriptionId());
                } else {
                    getWriter().addText(row.addNewCell(), "-");
                }
            }
            final Block detailChapter = page.addNewBlock();
            detailChapter.setStyle(DefaultStyle.DETAIL);
            documentDetail(detailChapter);
            table.setStyle(DefaultStyle.MEMBERS);

            Map<String, Set<DdsReferenceDef>> referencesMap = new HashMap<>();
            Set<DdsReferenceDef> references = (ddsTable.collectIncomingReferences());
            if (!references.isEmpty()) {
                referencesMap.put("Incoming reference", references);
            }
            references = (ddsTable.collectOutgoingReferences());
            if (!references.isEmpty()) {
                referencesMap.put("Outgoing reference", references);
            }
            if (!references.isEmpty()) {
                Block refBlock = detailChapter.addNewBlock();
                refBlock.setStyle(DefaultStyle.CHAPTER);

                block = refBlock.addNewBlock();
                final Block referenceBlock = block.addNewBlock();
                Text titleText = referenceBlock.addNewText();
                titleText.setStyle(DefaultStyle.TITLE);
                titleText.setStringValue("References");
                for (Map.Entry<String, Set<DdsReferenceDef>> e : referencesMap.entrySet()) {
                    Table refTable = referenceBlock.addNewTable();
                    Table.Row headRow = refTable.addNewRow();
                    headRow.setStyle(DefaultStyle.SUB_HEAD);
                    headRow.addNewCell().addNewText().setStringValue(e.getKey());
                    for (DdsReferenceDef reference : e.getValue()) {
                        DdsTableDef parentTable = reference.findParentTable(ddsTable);
                        DdsTableDef childTable = reference.findParentTable(ddsTable);
                        refTable.setStyle(DefaultStyle.MEMBERS);
                        Table.Row.Cell refCell = refTable.addNewRow().addNewCell();
                        if (parentTable != null && childTable != null) {
                            Ref ref = refCell.addNewRef();
                            ref.setId(parentTable.getId().toString());
                            ref.setTitle(parentTable.getQualifiedName());
                            ref = refCell.addNewRef();
                            ref.setId(childTable.getId().toString());
                            ref.setTitle(childTable.getQualifiedName());
                            ref.setPath(resolve(source, reference));
                            Ref refrsRef = ref.addNewRef();
                            refrsRef.setPath(resolve(source, reference));
                            getWriter().addText(refrsRef, reference.getName());
                        } else {
                            text = refCell.addNewText();
                            text.setStringValue(reference.getName());
                            text.setStyle(DefaultStyle.HEAD);
                        }
                    }
                }
            }
        }
    }

    private void documentDetail(final Block detail) {
        final Block optionBlock = detail.addNewBlock();
        getWriter().appendStyle(optionBlock, DefaultStyle.CHAPTER);
        Text optionText = optionBlock.addNewText();
        optionText.setMeta(DefaultStyle.TITLE);
        optionText.setStringValue("Extensions Options");

        Block optnBlock = optionBlock.addNewBlock();
        Table optnTable = optnBlock.addNewTable();
        optnTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row headerOptnRow = optnTable.addNewRow();
        headerOptnRow.setMeta("head");
        headerOptnRow.addNewCell().addNewText().setStringValue("Option");
        headerOptnRow.addNewCell().addNewText().setStringValue("Value");
        Set<EDdsTableExtOption> extOption = source.getExtOptions();
        for (EDdsTableExtOption opt : EDdsTableExtOption.values()) {
            Table.Row supportUserPropRow = optnTable.addNewRow();
            supportUserPropRow.addNewCell().addNewText().setStringValue(opt.getAsStr());
            supportUserPropRow.addNewCell().addNewText().setStringValue(extOption.contains(opt) ? "\u2714" : "\u2718");
        }

        final Block indxBlock = detail.addNewBlock();
        getWriter().appendStyle(indxBlock, DefaultStyle.CHAPTER);
        Text indxText = indxBlock.addNewText();
        indxText.setMeta(DefaultStyle.TITLE);
        indxText.setStringValue("Indices");
        Block typeBlock = indxBlock.addNewBlock();
        Table indxTable = typeBlock.addNewTable();
        indxTable.setStyle(DefaultStyle.MEMBERS);

        Table.Row headerRow = indxTable.addNewRow();
        headerRow.setMeta("head");
        headerRow.addNewCell().addNewText().setStringValue("Name");
        headerRow.addNewCell().addNewText().setStringValue("Database Name");
        headerRow.addNewCell().addNewText().setStringValue("Columns");
        headerRow.addNewCell().addNewText().setStringValue("Database Options");

        addIndexInfo(indxTable, source.getPrimaryKey());
        if (!source.getIndices().get(ExtendableDefinitions.EScope.ALL).isEmpty()) {
            for (DdsIndexDef indx : source.getIndices().getAll(ExtendableDefinitions.EScope.ALL)) {
                addIndexInfo(indxTable, indx);
            }
        }
        if (!source.getTriggers().get(ExtendableDefinitions.EScope.ALL).isEmpty()) {
            final Block descriptionBlock = detail.addNewBlock();
            getWriter().appendStyle(descriptionBlock, DefaultStyle.CHAPTER);
            Text overviewText = descriptionBlock.addNewText();
            overviewText.setMeta(DefaultStyle.TITLE);

            overviewText.setStringValue("Triggers ");
            Block triggerTypeBlock = descriptionBlock.addNewBlock();
            Table trgTable = triggerTypeBlock.addNewTable();
            trgTable.setStyle(DefaultStyle.MEMBERS);

            Table.Row triggerHeaderRow = trgTable.addNewRow();
            triggerHeaderRow.setMeta("head");
            triggerHeaderRow.addNewCell().addNewText().setStringValue("Name");
            triggerHeaderRow.addNewCell().addNewText().setStringValue("Columns");
            triggerHeaderRow.addNewCell().addNewText().setStringValue("Database Name");
            triggerHeaderRow.addNewCell().addNewText().setStringValue("Actuation Time");
            triggerHeaderRow.addNewCell().addNewText().setStringValue("Type");
            triggerHeaderRow.addNewCell().addNewText().setStringValue("Triggering Events");
            triggerHeaderRow.addNewCell().addNewText().setStringValue("Description");

            for (DdsTriggerDef trg : source.getTriggers().getAll(ExtendableDefinitions.EScope.ALL)) {
                Table.Row trgRow = trgTable.addNewRow();
                trgRow.addNewCell().addNewText().setStringValue(trg.getName());
                final Table.Row.Cell columnsCell = trgRow.addNewCell();
                Text columnName = columnsCell.addNewText();
                if (trg.getColumnsInfo().isEmpty()) {
                    columnsCell.addNewText().setStringValue(" - ");
                }
                for (int i = 0; i < trg.getColumnsInfo().list().size(); i++) {
                    DdsColumnInfo column = trg.getColumnsInfo().list().get(i);
                    if (columnName.getStringValue().isEmpty()) {
                        Ref ref = columnsCell.addNewRef();
                        ref.setPath(resolve(source, column.getColumn()));
                        ref.addNewText().setStringValue(column.getColumn().getName());
                        if (i != trg.getColumnsInfo().list().size() - 1) {
                            columnsCell.addNewText().setStringValue(", ");
                        }
                    }
                }
                trgRow.addNewCell().addNewText().setStringValue(trg.getDbName());
                trgRow.addNewCell().addNewText().setStringValue(getAsStrForActuationTime(trg.getActuationTime().name()));
                trgRow.addNewCell().addNewText().setStringValue(getAsStrForTriggerType(trg.getType().name()));

                final Table.Row.Cell trgEventCell = trgRow.addNewCell();
                Text trgEventName = trgEventCell.addNewText();

                if (trg.getTriggeringEvents().isEmpty()) {
                    trgEventName.setStringValue("<Not Defined>");
                }
                if (trg.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_DELETE)) {
                    trgEventName.setStringValue("Delete");
                }
                if (trgEventName.getStringValue().isEmpty()) {
                    if (trg.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_INSERT)) {
                        trgEventName.setStringValue("Insert");
                    }
                } else {
                    if (trg.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_INSERT)) {
                        trgEventName.setStringValue(trgEventName.getStringValue() + ", " + "Insert");
                    }
                }
                if (trgEventName.getStringValue().isEmpty()) {
                    if (trg.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_UPDATE)) {
                        trgEventName.setStringValue("Update");
                    }
                } else {
                    if (trg.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_UPDATE)) {
                        trgEventName.setStringValue(trgEventName.getStringValue() + ", " + "Update");
                    }
                }

                if (trg.getDescriptionId() != null && trg.findExistingLocalizingBundle() != null) {
                    getWriter().addMslId(trgRow.addNewCell(), trg.findExistingLocalizingBundle().getId(), trg.getDescriptionId());
                } else {
                    if (trg.getType() == DdsTriggerDef.EType.FOR_AUDIT) {
                        getWriter().addMslId(trgRow.addNewCell(), RadixdocUtils.getRadixdocOwnerDef(trg.getLayer()), RadixdocConventions.LOCALIZING_BUNDLE_ID, AUDIT_TRIGGER_DESCRIPTION_ID);
                    } else {
                        getWriter().addText(trgRow.addNewCell(), "-");
                    }
                }
            }
        }
    }

    private void addIndexInfo(Table indxTable, DdsIndexDef indx) {
        Table.Row indxRow = indxTable.addNewRow();
        final Table.Row.Cell nameCell = indxRow.addNewCell();
        if (indx.isDeprecated()) {
            nameCell.setStyle(DefaultStyle.DEPRECATED);
        }
        nameCell.addNewText().setStringValue(indx.getName());

        final Table.Row.Cell dbNameCell = indxRow.addNewCell();
        dbNameCell.addNewText().setStringValue(indx.getDbName());

        final Table.Row.Cell indxCell = indxRow.addNewCell();
        Text columnName = indxCell.addNewText();

        for (int i = 0; i < indx.getColumnsInfo().list().size(); i++) {
            DdsColumnInfo column = indx.getColumnsInfo().list().get(i);
            if (columnName.getStringValue().isEmpty()) {
                Ref ref = indxCell.addNewRef();
                ref.setPath(resolve(source, column.getColumn()));
                ref.addNewText().setStringValue(column.getColumn().getName());
                if (i != indx.getColumnsInfo().list().size() - 1) {
                    indxCell.addNewText().setStringValue(", ");
                }
            }
        }
        Set<EDdsConstraintDbOption> indexDbOptions = indx.getUniqueConstraint() != null ? indx.getUniqueConstraint().getDbOptions() : new HashSet<EDdsConstraintDbOption>();
        List<String> dbOpt = new ArrayList<>();
        for (EDdsConstraintDbOption dbOptName : EDdsConstraintDbOption.values()) {
            if (indexDbOptions.contains(dbOptName)) {
                dbOpt.add(dbOptName.getName());
            }
        }
        final Table.Row.Cell optCell = indxRow.addNewCell();
        getWriter().addText(optCell, dbOpt.toString().substring(1, dbOpt.toString().length() - 1));
    }

    public String getAsStrForTriggerType(String name) {
        switch (DdsTriggerDef.EType.valueOf(name)) {
            case FOR_USER_PROPS:
                return "For user properties";
            case FOR_AUDIT:
                return "For audit";
            case NONE:
                return "None";
            default:
                return "not defined";
        }
    }

    public String getAsStrForActuationTime(String name) {
        switch (DdsTriggerDef.EActuationTime.valueOf(name)) {
            case BEFORE:
                return "Before";
            case AFTER:
                return "After";
            case INSTEAD_OF:
                return "Instead of";
            default:
                return "not defined";
        }
    }
}
