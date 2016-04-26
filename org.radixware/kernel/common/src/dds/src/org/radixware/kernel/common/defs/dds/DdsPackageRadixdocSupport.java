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
package org.radixware.kernel.common.defs.dds;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef.ReliesOnTableInfo;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
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

public class DdsPackageRadixdocSupport extends DdsDefinitionRadixdoc<DdsPackageDef> {

    DdsPackageRadixdocSupport(DdsPackageDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    public void documentOverview(ContentContainer container) {
        final Block overview = container.addNewBlock();
        overview.setStyle(DefaultStyle.CHAPTER);
        getWriter().appendStyle(overview, DefaultStyle.CHAPTER, DefaultStyle.OVERVIEW);
        getWriter().addStrTitle(overview, "Overview");
        if (source.getDescriptionId() != null || !source.getDescription().isEmpty()) {
            getWriter().documentDescription(overview, source);
        }
        List<String> modifiers = new ArrayList<>();
        if (getSource().isDeprecated()) {
            modifiers.add("Deprecated");
        }
        if (!modifiers.isEmpty()) {
            Table propEdit = overview.addNewTable();
            propEdit.setStyle(DefaultStyle.MEMBERS);
            Table.Row headerRow = propEdit.addNewRow();
            headerRow.setMeta("head");
            Table.Row.Cell headerCell = headerRow.addNewCell();
            headerCell.setColspan(2);
            headerCell.addNewText().setStringValue("General attributes");
            getWriter().addAllStrRow(propEdit, "Modifiers", modifiers.toString().substring(1, modifiers.toString().length() - 1));
        }
        writePurityLevel(source.getPurityLevel(), overview);
        if (getSource() instanceof DdsPackageDef) {
            documentDetail(overview);
        }
    }

    public void writePurityLevel(DdsPurityLevel purityLevel, Block block) {
        Block purityLevelBlock = block.addNewBlock();
        Text purityLevelText = purityLevelBlock.addNewText();
        purityLevelText.setMeta(DefaultStyle.TITLE);
        purityLevelText.setStringValue("Purity Levels");
        Table purityLevelTable = block.addNewTable();
        purityLevelTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row purityLevelRow = purityLevelTable.addNewRow();
        purityLevelRow.setMeta("head");
        purityLevelRow.addNewCell().addNewText().setStringValue("Purity Level");
        purityLevelRow.addNewCell().addNewText().setStringValue("Value");

        boolean autoPurLev = !purityLevel.isRNDS() && !purityLevel.isRNPS()
                && !purityLevel.isWNDS() && !purityLevel.isWNPS() && !purityLevel.isTrust();
        if (autoPurLev) {
            Table.Row autoRow = purityLevelTable.addNewRow();
            autoRow.addNewCell().addNewText().setStringValue(" Auto ");
            autoRow.addNewCell().addNewText().setStringValue(autoPurLev ? "\u2714" : " ");
        } else {
            if (purityLevel.isWNDS()) {
                Table.Row wndsRow = purityLevelTable.addNewRow();
                wndsRow.addNewCell().addNewText().setStringValue(" WNDS ");
                wndsRow.addNewCell().addNewText().setStringValue(purityLevel.isWNDS() ? "\u2714" : " ");
            }
            if (purityLevel.isRNDS()) {
                Table.Row rndsRow = purityLevelTable.addNewRow();
                rndsRow.addNewCell().addNewText().setStringValue(" RNDS ");
                rndsRow.addNewCell().addNewText().setStringValue(purityLevel.isRNDS() ? "\u2714" : " ");
            }
            if (purityLevel.isWNPS()) {
                Table.Row wnpsRow = purityLevelTable.addNewRow();
                wnpsRow.addNewCell().addNewText().setStringValue(" WNPS ");
                wnpsRow.addNewCell().addNewText().setStringValue(purityLevel.isWNPS() ? "\u2714" : " ");
            }
            if (purityLevel.isRNPS()) {
                Table.Row rnpsRow = purityLevelTable.addNewRow();
                rnpsRow.addNewCell().addNewText().setStringValue(" RNPS ");
                rnpsRow.addNewCell().addNewText().setStringValue(purityLevel.isRNPS() ? "\u2714" : " ");
            }
            if (purityLevel.isTrust()) {
                Table.Row trustRow = purityLevelTable.addNewRow();
                trustRow.addNewCell().addNewText().setStringValue(" TRUST ");
                trustRow.addNewCell().addNewText().setStringValue(purityLevel.isTrust() ? "\u2714" : " ");
            }
        }
    }

    private void documentDetail(final Block block) {
        Text title = block.addNewText();
        title.setMeta(DefaultStyle.TITLE);
        title.setStringValue("Functions");
        Block typeBlock = block.addNewBlock();
        Table nameListTable = typeBlock.addNewTable();
        nameListTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row headerNameRow = nameListTable.addNewRow();
        Table.Row.Cell headerNameCell = headerNameRow.addNewCell();
        headerNameCell.setColspan(2);
        Table.Row.Cell headerDescriptionCell = headerNameRow.addNewCell();
        headerNameRow.setMeta("head");
        headerNameCell.addNewText().setStringValue(" Name ");
        headerDescriptionCell.addNewText().setStringValue(" Description ");
        boolean funcExists = false;
        for (DdsPlSqlObjectItemDef item : source.getHeader().getItems()) {
            if (item instanceof DdsPrototypeDef) {
                DdsFunctionDef funcDef = ((DdsPrototypeDef) item).getFunction();
                if (funcDef.isPublic() && funcDef.isGeneratedInDb()) {
                    funcExists = true;
                    Table.Row funcRow = nameListTable.addNewRow();
                    Table.Row.Cell typeCell = funcRow.addNewCell();
                    Table.Row.Cell nameCell = funcRow.addNewCell();
                    if (item.isDeprecated()) {
                        nameCell.setStyle(DefaultStyle.DEPRECATED);
                    }
                    EValType resType = ((DdsPrototypeDef) item).getFunction().getResultValType();
                    final Ref ref = nameCell.addNewRef();
                    ref.setPath(resolve(source, item));
                    final RadixIconResource icon = new RadixIconResource(item.getIcon());
                    ref.addNewResource().setSource(icon.getKey());
                    addResource(icon);
                    if (resType != null) {
                        typeCell.addNewText().setStringValue(resType.getName());
                        getWriter().addText(ref, item.getName());
                    } else {
                        typeCell.addNewText().setStringValue("Void");
                        getWriter().addText(ref, item.getName());
                    }
                    getWriter().documentDescription(nameCell, item);
                    Table.Row.Cell descriptionCell = funcRow.addNewCell();
                    String descript = ((DdsPrototypeDef) item).getFunction().getDescription();
                    if (!descript.isEmpty()) {
                        descriptionCell.addNewText().setStringValue(descript);
                    } else {
                        descriptionCell.addNewText().setStringValue(" ");
                    }
                }
            }
        }
        if (!funcExists) {
            Table.Row funcRow = nameListTable.addNewRow();
            Table.Row.Cell funcCell = funcRow.addNewCell();
            funcCell.setColspan(3);
            funcCell.addNewText().setStringValue("<Not Defined>");
        }
        writeFunctionDetail(page);
    }

    private void writeFunctionDetail(Page page) {
        if (!source.getHeader().getItems().isEmpty()) {
            Block detailBlock = page.addNewBlock();
            getWriter().appendStyle(detailBlock, DefaultStyle.CHAPTER);
            Text titleText = detailBlock.addNewText();
            titleText.setMeta(DefaultStyle.TITLE);
            titleText.setStringValue("Function Detail");
            for (int i = 0; i < source.getHeader().getItems().list().size(); i++) {
                DdsPlSqlObjectItemDef item = source.getHeader().getItems().list().get(i);
                if (item instanceof DdsPrototypeDef) {
                    DdsFunctionDef funcDef = ((DdsPrototypeDef) item).getFunction();
                    if (funcDef.isPublic() && funcDef.isGeneratedInDb()) {
                        EValType resType = ((DdsPrototypeDef) item).getFunction().getResultValType();
                        final Block descriptionBlock = detailBlock.addNewBlock();
                        getWriter().appendStyle(descriptionBlock, DefaultStyle.CHAPTER);
                        getWriter().setAttribute(descriptionBlock, DefaultAttributes.ANCHOR, item.getId().toString());
                        Block nameBlock = descriptionBlock.addNewBlock();
                        nameBlock.setStyle(DefaultStyle.NAMED);
                        Block block = nameBlock.addNewBlock();
                        block.setStyle(DefaultStyle.TITLE);
                        Table typesTable = descriptionBlock.addNewTable();
                        typesTable.setStyle(DefaultStyle.MEMBERS);
                        Table.Row typesRow = typesTable.addNewRow();
                        try {
                            String dbType = ((DdsPrototypeDef) item).getFunction().getResultDbType();
                            if (resType != null) {
                                block.addNewText().setStringValue(resType.getName() + " ");
                                typesRow.addNewCell().addNewText().setStringValue("Result Type : " + resType.getName() + " (" + dbType + ")");
                            } else {
                                block.addNewText().setStringValue("Void ");
                                typesRow.addNewCell().addNewText().setStringValue("Result Type : Void");
                            }
                        } catch (DefinitionNotFoundError ex) {
                        }
                        final RadixIconResource icon = new RadixIconResource(item.getIcon());
                        block.addNewResource().setSource(icon.getKey());
                        addResource(icon);
                        Text nameText = block.addNewText();
                        nameText.setStyle(DefaultStyle.IDENTIFIER);
                        nameText.setStringValue(item.getName());
                        List<String> modifiers = new ArrayList<>();
                        if (funcDef.isDeprecated()) {
                            modifiers.add("Deprecated");
                        }
                        List<DdsTableDef> cacheResult = new ArrayList<>();
                        if (funcDef.isCachedResult()) {
                            for (ReliesOnTableInfo reliesOn : funcDef.getReliesOnTablesInfo()) {
                                cacheResult.add(reliesOn.getTable());
                            }
                        }
                        if (!modifiers.isEmpty() || !cacheResult.isEmpty() || funcDef.isDeterministic()) {
                            Table descriptionTable = descriptionBlock.addNewTable();
                            descriptionTable.setStyle(DefaultStyle.MEMBERS);
                            Table.Row headerRow = descriptionTable.addNewRow();
                            headerRow.setMeta("head");
                            Table.Row.Cell headerCell = headerRow.addNewCell();
                            headerCell.setColspan(2);
                            headerCell.addNewText().setStringValue("General attributes");
                            if (!modifiers.isEmpty()) {
                                getWriter().addAllStrRow(descriptionTable, "Modifiers", modifiers.toString().substring(1, modifiers.toString().length() - 1));
                            }
                            if (!cacheResult.isEmpty()) {
                                Table.Row cacheResRow = descriptionTable.addNewRow();
                                Table.Row.Cell cacheResultCell = cacheResRow.addNewCell();
                                cacheResultCell.addNewText().setStringValue("Cache Result");
                                Table.Row.Cell cacheResCell = cacheResRow.addNewCell();
                                Text cacheResText = cacheResCell.addNewText();
                                for (int j = 0; j < cacheResult.size(); j++) {
                                    if (cacheResText.getStringValue().isEmpty()) {
                                        Ref cacheResRef = cacheResCell.addNewRef();
                                        cacheResRef.setPath(resolve(source, cacheResult.get(j)));
                                        cacheResRef.addNewText().setStringValue(cacheResult.get(j).getName());
                                        if (j != cacheResult.size() - 1) {
                                            cacheResCell.addNewText().setStringValue(", ");
                                        }
                                    }
                                }
                            }
                            getWriter().addStr2BoolRow(descriptionTable, "Deterministic", funcDef.isDeterministic());
                        }
                        DdsDefinitions<DdsParameterDef> parameters = ((DdsPrototypeDef) item).getFunction().getParameters();

                        if (!parameters.isEmpty()) {
                            Block paramBlock = descriptionBlock.addNewBlock();
                            Text paramText = paramBlock.addNewText();
                            paramText.setMeta(DefaultStyle.TITLE);
                            paramText.setStringValue("Parameters");

                            Table paramTable = descriptionBlock.addNewTable();
                            paramTable.setStyle(DefaultStyle.MEMBERS);
                            Table.Row headParamRow = paramTable.addNewRow();
                            headParamRow.setMeta("head");
                            headParamRow.addNewCell().addNewText().setStringValue("Name");
                            headParamRow.addNewCell().addNewText().setStringValue("Type");
                            headParamRow.addNewCell().addNewText().setStringValue("Database Type");
                            headParamRow.addNewCell().addNewText().setStringValue("Direction");
                            headParamRow.addNewCell().addNewText().setStringValue("Default");
                            headParamRow.addNewCell().addNewText().setStringValue("Description");

                            for (DdsParameterDef param : ((DdsPrototypeDef) item).getFunction().getParameters()) {

                                Table.Row paramRow = paramTable.addNewRow();
                                paramRow.addNewCell().addNewText().setStringValue(param.getName());
                                paramRow.addNewCell().addNewText().setStringValue(param.getValType().getName());
                                paramRow.addNewCell().addNewText().setStringValue(param.getDbType());
                                EParamDirection dirt = param.getDirection();
                                if (dirt.equals(EParamDirection.IN)) {
                                    paramRow.addNewCell().addNewText().setStringValue("In");
                                }
                                if (dirt.equals(EParamDirection.OUT)) {
                                    paramRow.addNewCell().addNewText().setStringValue("Out");
                                }
                                if (dirt.equals(EParamDirection.BOTH)) {
                                    paramRow.addNewCell().addNewText().setStringValue("In/Out");
                                }
                                if (!param.getDefaultVal().isEmpty()) {
                                    paramRow.addNewCell().addNewText().setStringValue(param.getDefaultVal());
                                } else {
                                    paramRow.addNewCell().addNewText().setStringValue("<Not Defined>");
                                }
                                if (!param.getDescription().isEmpty()) {
                                    paramRow.addNewCell().addNewText().setStringValue(param.getDescription());
                                } else {
                                    paramRow.addNewCell().addNewText().setStringValue(" ");
                                }
                            }
                        }
                        DdsPurityLevel purityLevel = ((DdsPrototypeDef) item).getFunction().getPurityLevel();
                        writePurityLevel(purityLevel, descriptionBlock);
                    }
                }
            }
        }
    }
}
