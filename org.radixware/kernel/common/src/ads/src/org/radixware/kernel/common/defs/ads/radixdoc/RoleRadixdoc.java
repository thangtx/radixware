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
package org.radixware.kernel.common.defs.ads.radixdoc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationsMember;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityObjectPresentations;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphLinkExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.enums.EDrcServerResource;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.radixdoc.DefaultAttributes;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Ref;
import org.radixware.schemas.radixdoc.Table;
import org.radixware.schemas.radixdoc.Text;

public class RoleRadixdoc extends AdsDefinitionRadixdoc<AdsRoleDef> {

    boolean isRootsExists = false;

    public RoleRadixdoc(AdsRoleDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void documentContent(Page page) {
        Block ancestorsblock = page.addNewBlock();
        if (!source.collectAllAncestors().isEmpty()) {
            writeAncestors(ancestorsblock);
        }
        Block resourcesblock = page.addNewBlock();
        Text resText = ancestorsblock.addNewText();
        resText.setStringValue("Resources");
        resText.setStyle(DefaultStyle.TITLE);
        writeServerResources(resourcesblock);
        explorerRootsDescription(resourcesblock);
        writeContextlessCommands(resourcesblock);
        presentationsDescription(resourcesblock);
    }

    private void writeAncestors(Block block) {
        final Block mainBlock = block.addNewBlock();
        mainBlock.setStyle(DefaultStyle.CHAPTER);
        getWriter().addStrTitle(mainBlock, "Ancestors");
        Block ancestorsBlock = mainBlock.addNewBlock();
        ancestorsBlock.setStyle(DefaultStyle.DEFINITION);
        final Table ancestorsTable = ancestorsBlock.addNewTable();
        for (AdsRoleDef ancestor : source.collectAllAncestors()) {
            Table.Row ancestorsRow = ancestorsTable.addNewRow();
            Table.Row.Cell ancestorsCell = ancestorsRow.addNewCell();
            Ref ancRef = ancestorsCell.addNewRef();
            ancRef.setPath(resolve(source, ancestor));
            getWriter().addText(ancRef, ancestor.getQualifiedName());
        }
    }

    private void writeServerResources(Block block) {
        final Block servResourcesBlock = block.addNewBlock();
        servResourcesBlock.setStyle(DefaultStyle.CHAPTER);
        getWriter().addStrTitle(servResourcesBlock, "Server Resources");

        final Table serverResTable = servResourcesBlock.addNewTable();
        serverResTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row headerResRow = serverResTable.addNewRow();
        headerResRow.setStyle(DefaultStyle.SUB_HEAD);

        headerResRow.addNewCell().addNewText().setStringValue("Server Resource");
        if (!source.collectAllAncestors().isEmpty()) {
            headerResRow.addNewCell().addNewText().setStringValue("Inherited Rights");
            headerResRow.addNewCell().addNewText().setStringValue("Own Rights");
            headerResRow.addNewCell().addNewText().setStringValue("Total Rights");

            for (EDrcServerResource res : EDrcServerResource.values()) {
                Table.Row serverResRow = serverResTable.addNewRow();
                serverResRow.addNewCell().addNewText().setStringValue(res.getAsStr());
                String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.SERVER_RESOURCE, res);
                stringForRights(serverResRow, hash);
            }

        } else {
            headerResRow.addNewCell().addNewText().setStringValue("Rights");
            for (EDrcServerResource res : EDrcServerResource.values()) {
                Table.Row serverResRow = serverResTable.addNewRow();
                serverResRow.addNewCell().addNewText().setStringValue(res.getAsStr());
                Text resRightValue = serverResRow.addNewCell().addNewText();
                String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.SERVER_RESOURCE, res);
                Restrictions current = source.getOnlyCurrentResourceRestrictions(hash);

                if (current != null) {
                    resRightValue.setStringValue(current.isDenied(ERestriction.ACCESS) ? " " : "\u2714");
                } else {
                    resRightValue.setStringValue(" ");
                }
            }
        }
    }

    private void explorerRootsDescription(Block block) {
        final Block explorerBlock = block.addNewBlock();
        explorerBlock.setStyle(DefaultStyle.CHAPTER);
        getWriter().addStrTitle(explorerBlock, "Explorer Roots");

        final Table explorerRootTable = explorerBlock.addNewTable();
        explorerRootTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row headerExplItemRow = explorerRootTable.addNewRow();
        headerExplItemRow.setStyle(DefaultStyle.SUB_HEAD);
        headerExplItemRow.addNewCell().addNewText().setStringValue("Explorer Root");
        if (!source.collectAllAncestors().isEmpty()) {
            headerExplItemRow.addNewCell().addNewText().setStringValue("Inherited Rights");
            headerExplItemRow.addNewCell().addNewText().setStringValue("Own Rights");
            headerExplItemRow.addNewCell().addNewText().setStringValue("Total Rights");
        } else {
            headerExplItemRow.addNewCell().addNewText().setStringValue("Rights");
        }
        List<Definition> allDependecies = new LinkedList<>();
        source.collectDependences(allDependecies);
        for (Definition def : allDependecies) {
            if (def instanceof AdsParagraphExplorerItemDef) {
                AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) def;
                if (par.isRoot()) {
                    isRootsExists = true;
                    Id defId = par.getId();
                    String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EXPLORER_ROOT_ITEM, defId, null);
                    Table.Row itemsRootRow = explorerRootTable.addNewRow();
                    Table.Row.Cell itemsCell = itemsRootRow.addNewCell();
                    Ref ref = itemsCell.addNewRef();
                    final RadixIconResource icon = new RadixIconResource(par.getIcon());
                    ref.addNewResource().setSource(icon.getKey());
                    addResource(icon);
                    ref.setPath(resolve(par, par));
                    getWriter().addText(ref, par.getQualifiedName());
                    getWriter().documentDescription(itemsCell, par);
                    if (!source.collectAllAncestors().isEmpty()) {
                        stringForRights(itemsRootRow, hash);
                    } else {
                        Restrictions current = source.getOnlyCurrentResourceRestrictions(hash);
                        if (current != null) {
                            itemsRootRow.addNewCell().addNewText().setStringValue("\u2714");
                        } else {
                            itemsRootRow.addNewCell().addNewText().setStringValue("");
                        }
                    }
                }
            }
        }
        if (!isRootsExists) {
            Table.Row itemsRootRow = explorerRootTable.addNewRow();
            itemsRootRow.addNewCell().addNewText().setStringValue("<Not Defined>");
            itemsRootRow.addNewCell().addNewText().setStringValue(" ");
            if (!source.collectAllAncestors().isEmpty()) {
                itemsRootRow.addNewCell().addNewText().setStringValue(" ");
                itemsRootRow.addNewCell().addNewText().setStringValue(" ");
            }
        } else {
            Table.Row itemHeaderRow = explorerRootTable.addNewRow();
            itemHeaderRow.setStyle(DefaultStyle.SUB_HEAD);
            itemHeaderRow.addNewCell().addNewText().setStringValue("Explorer Item");
            if (!source.collectAllAncestors().isEmpty()) {
                itemHeaderRow.addNewCell().addNewText().setStringValue("Inherited Rights");
                itemHeaderRow.addNewCell().addNewText().setStringValue("Own Rights");
                itemHeaderRow.addNewCell().addNewText().setStringValue("Total Rights");
            } else {
                itemHeaderRow.addNewCell().addNewText().setStringValue("Rights");
            }
            List<AdsParagraphExplorerItemDef> collectRootLst = new ArrayList<>();
            writeExplorerRootsWithItems(explorerRootTable, collectRootLst);
        }
    }
    private Id rootId;

    public void writeExplorerItems(Table table, List<Id> collectLst, AdsExplorerItemDef explorerItem) {
        if (collectLst.contains(explorerItem.getId())) {
            return;
        }
        collectLst.add(explorerItem.getId());

        if (explorerItem instanceof AdsParagraphExplorerItemDef) {
            AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) explorerItem;
            if (par.isRoot()) {
                rootId = par.getId();
            }
            List<AdsExplorerItemDef> items = par.getExplorerItems().getChildren().get(EScope.ALL);
            for (AdsExplorerItemDef item : items) {
                Table.Row row = table.addNewRow();
                Table.Row.Cell cell = row.addNewCell();
                cell.setStyle(DefaultStyle.TREE);
                final RadixIconResource icon = new RadixIconResource(item.getIcon());
                cell.addNewResource().setSource(icon.getKey());
                addResource(icon);
                cell.addNewText().setStringValue(item.getName());
                Id subItemId = item.getId();
                String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EXPLORER_ROOT_ITEM, rootId, subItemId);
                stringForRights(row, hash);
                writeExplorerItems(table, collectLst, item);
            }
        } else if (explorerItem instanceof AdsParagraphLinkExplorerItemDef) {
            AdsParagraphLinkExplorerItemDef def = (AdsParagraphLinkExplorerItemDef) explorerItem;
            AdsParagraphExplorerItemDef par2 = def.findReferencedParagraph();
            if (par2 != null) {
                if (par2.isRoot()) {
                    rootId = par2.getId();
                }
                List<AdsExplorerItemDef> items = par2.getExplorerItems().getChildren().get(EScope.ALL);
                for (AdsExplorerItemDef item : items) {
                    Table.Row row = table.addNewRow();
                    Table.Row.Cell cell = row.addNewCell();
                    cell.setStyle(DefaultStyle.TREE);
                    final RadixIconResource icon = new RadixIconResource(item.getIcon());
                    cell.addNewResource().setSource(icon.getKey());
                    addResource(icon);
                    cell.addNewText().setStringValue(item.getName());
                    Id subItemId = item.getId();
                    String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EXPLORER_ROOT_ITEM, rootId, subItemId);
                    stringForRights(row, hash);
                    writeExplorerItems(table, collectLst, item);
                }
            }
        }
    }

    private void writeExplorerRootsWithItems(Table table, List<AdsParagraphExplorerItemDef> list) {
        List<Definition> allDependecies = new LinkedList<>();
        source.collectDependences(allDependecies);
        for (Definition def : allDependecies) {
            if (def instanceof AdsParagraphExplorerItemDef) {
                AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) def;
                if (par.isRoot()) {
                    isRootsExists = true;
                    String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.EXPLORER_ROOT_ITEM, par.getId(), null);
                    Table.Row itemsRootRow = table.addNewRow();
                    Table.Row.Cell itemsCell = itemsRootRow.addNewCell();
                    final RadixIconResource icon = new RadixIconResource(par.getIcon());
                    itemsCell.addNewResource().setSource(icon.getKey());
                    addResource(icon);
                    getWriter().addText(itemsCell, par.getQualifiedName());
                    getWriter().setAttribute(itemsCell, DefaultAttributes.ANCHOR, par.getId().toString());
                    list.add(par);
                    if (!source.collectAllAncestors().isEmpty()) {
                        stringForRights(itemsRootRow, hash);
                    } else {
                        Restrictions current = source.getOnlyCurrentResourceRestrictions(hash);
                        if (current != null) {
                            itemsRootRow.addNewCell().addNewText().setStringValue("\u2714");
                        } else {
                            itemsRootRow.addNewCell().addNewText().setStringValue("");
                        }
                    }
                }
                List<Id> collectLst = new ArrayList<>();
                writeExplorerItems(table, collectLst, par);
            }
        }
    }

    private void writeContextlessCommands(Block block) {

        final Block contextlessCommdsBlock = block.addNewBlock();
        contextlessCommdsBlock.setStyle(DefaultStyle.CHAPTER);
        getWriter().addStrTitle(contextlessCommdsBlock, "Contextless Commands");

        final Table contextlessCommdsTable = contextlessCommdsBlock.addNewTable();
        contextlessCommdsTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row headerRow = contextlessCommdsTable.addNewRow();
        headerRow.setStyle(DefaultStyle.SUB_HEAD);
        headerRow.addNewCell().addNewText().setStringValue("Command");
        if (!source.collectAllAncestors().isEmpty()) {
            headerRow.addNewCell().addNewText().setStringValue("Inherited Rights");
            headerRow.addNewCell().addNewText().setStringValue("Own Rights");
            headerRow.addNewCell().addNewText().setStringValue("Total Rights");
        } else {
            headerRow.addNewCell().addNewText().setStringValue("Rights");
        }
        boolean defExists = false;
        List<Definition> allDependecies = new LinkedList<>();
        source.collectDependences(allDependecies);
        for (Definition def : allDependecies) {
            if (def instanceof AdsContextlessCommandDef) {
                defExists = true;
                Id defId = def.getId();
                Table.Row nameCommandRow = contextlessCommdsTable.addNewRow();
                nameCommandRow.addNewCell().addNewText().setStringValue(def.getQualifiedName());
                String hash = AdsRoleDef.generateResHashKey(EDrcResourceType.CONTEXTLESS_COMMAND, defId, null);
                if (!source.collectAllAncestors().isEmpty()) {
                    stringForRights(nameCommandRow, hash);
                } else {
                    Restrictions current = source.getOnlyCurrentResourceRestrictions(hash);
                    if (current != null) {
                        nameCommandRow.addNewCell().addNewText().setStringValue(current.isDenied(ERestriction.ACCESS) ? " " : "\u2714");
                    } else {
                        nameCommandRow.addNewCell().addNewText().setStringValue(" ");
                    }
                }
            }
        }
        if (!defExists) {
            Table.Row nameCommandRow = contextlessCommdsTable.addNewRow();
            nameCommandRow.addNewCell().addNewText().setStringValue("<Not Defined>");
            nameCommandRow.addNewCell().addNewText().setStringValue(" ");
            if (!source.collectAllAncestors().isEmpty()) {
                nameCommandRow.addNewCell().addNewText().setStringValue(" ");
                nameCommandRow.addNewCell().addNewText().setStringValue(" ");
            }
        }
    }

    private void stringForRights(Table.Row row, String hash) {
        Restrictions prior = source.getOverwriteAndAncestordResourceRestrictions(hash, null);
        Restrictions current = source.getOnlyCurrentResourceRestrictions(hash);
        Restrictions total;
        if (current == null) {
            total = prior;
        } else {
            total = current;
        }
        Text resPriorValue = row.addNewCell().addNewText();
        Text resCurValue = row.addNewCell().addNewText();
        Text resTotalValue = row.addNewCell().addNewText();
        resPriorValue.setStringValue(prior.isDenied(ERestriction.ACCESS) ? "Prohibited" : "Allowed");
        if (current != null) {
            resCurValue.setStringValue(current.isDenied(ERestriction.ACCESS) ? "Prohibited" : "Allowed");
        } else {
            resCurValue.setStringValue("Inherit");
        }
        resTotalValue.setStringValue(total.isDenied(ERestriction.ACCESS) ? "Prohibited" : "Allowed");
    }

    private void presentationsDescription(Block block) {
        final Block presentationsBlock = block.addNewBlock();
        presentationsBlock.setStyle(DefaultStyle.CHAPTER);
        getWriter().addStrTitle(presentationsBlock, "Presentations");
        Table presentationsTable = presentationsBlock.addNewTable();
        presentationsTable.setStyle(DefaultStyle.MEMBERS);
        Table.Row headerPresentationsRow = presentationsTable.addNewRow();
        headerPresentationsRow.setStyle(DefaultStyle.SUB_HEAD);
        headerPresentationsRow.addNewCell().addNewText().setStringValue("Entity Class");
        boolean defExists = false;
        List<Definition> allDependecies = new LinkedList<>();
        source.collectDependences(allDependecies);
        HashSet<AdsEntityObjectClassDef> ownerEntities = new HashSet<>();
        for (Definition def : allDependecies) {
            if (def instanceof AdsSelectorPresentationDef || def instanceof AdsEditorPresentationDef) {
                AdsPresentationsMember presentationsMember = (AdsPresentationsMember) def;
                ownerEntities.add((AdsEntityObjectClassDef) presentationsMember.getOwnerClass());
            }
        }
        for (AdsEntityObjectClassDef owner : ownerEntities) {
            defExists = true;
            Table.Row nameRow = presentationsTable.addNewRow();
            Table.Row.Cell nameCell = nameRow.addNewCell();
            Ref nameRef = nameCell.addNewRef();
            nameRef.setPath(resolve(owner, owner));
            getWriter().addText(nameRef, owner.getQualifiedName());

            final Block detailBlock = presentationsBlock.addNewBlock();
            detailBlock.setStyle(DefaultStyle.CHAPTER);
            final Block nameBlock = detailBlock.addNewBlock();
            nameBlock.setStyle(DefaultStyle.TITLE);
            nameBlock.addNewText().setStringValue(owner.getName());
            getWriter().setAttribute(nameBlock, DefaultAttributes.ANCHOR, owner.getId().toString());
            EntityObjectPresentations presentations = owner.getPresentations();
            Block presentBlock = detailBlock.addNewBlock();
            Table presentTable = presentBlock.addNewTable();
            presentTable.setStyle(DefaultStyle.MEMBERS);
            Table.Row headerRow = presentTable.addNewRow();
            headerRow.setStyle(DefaultStyle.SUB_HEAD);
            headerRow.addNewCell().addNewText().setStringValue("Presentation");
            headerRow.addNewCell().addNewText().setStringValue("Own");
            headerRow.addNewCell().addNewText().setStringValue("Rights");
            headerRow.addNewCell().addNewText().setStringValue("Allowed commands");
            headerRow.addNewCell().addNewText().setStringValue("Allowed children");
            headerRow.addNewCell().addNewText().setStringValue("Allowed pages");
            Table.Row editorRow = presentTable.addNewRow();
            Table.Row.Cell editorCell = editorRow.addNewCell();
            editorCell.setColspan(7);
            Text editorPresentText = editorCell.addNewText();
            editorPresentText.setStyle(DefaultStyle.IDENTIFIER);
            editorPresentText.setStringValue("Editor Presentation");

            boolean editExists = false;
            for (AdsEditorPresentationDef editor : presentations.getEditorPresentations().getAll(ExtendableDefinitions.EScope.ALL)) {
                String editorHash = AdsRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION, editor.getOwnerDef().getId(), editor.getId());
                Restrictions current = source.getOnlyCurrentResourceRestrictions(editorHash);
                if (current != null) {
                    editExists = true;
                    editorPresentationDescription(editor, presentations, current, presentTable, editorHash);
                }
            }
            if (!editExists) {
                Table.Row editItemsRow = presentTable.addNewRow();
                Table.Row.Cell rightCell = editItemsRow.addNewCell();
                rightCell.setColspan(6);
                rightCell.addNewText().setStringValue("<Not Defined>");
            }
            Table.Row selectorHeaderRow = presentTable.addNewRow();
            selectorHeaderRow.setStyle(DefaultStyle.SUB_HEAD);
            selectorHeaderRow.addNewCell().addNewText().setStringValue("Presentation");
            selectorHeaderRow.addNewCell().addNewText().setStringValue("Own");
            selectorHeaderRow.addNewCell().addNewText().setStringValue("Rights");
            Table.Row.Cell enabCommCell = selectorHeaderRow.addNewCell();
            enabCommCell.setColspan(3);
            enabCommCell.addNewText().setStringValue("Allowed commands");

            Table.Row selectorRow = presentTable.addNewRow();
            Table.Row.Cell selectorCell = selectorRow.addNewCell();
            selectorCell.setColspan(6);
            Text selectPresentText = selectorCell.addNewText();
            selectPresentText.setStyle(DefaultStyle.IDENTIFIER);
            selectPresentText.setStringValue("Selector presentation");

            boolean selectExists = false;
            for (AdsSelectorPresentationDef selector : presentations.getSelectorPresentations().getAll(ExtendableDefinitions.EScope.ALL)) {
                String selectorHash = AdsRoleDef.generateResHashKey(EDrcResourceType.SELECTOR_PRESENTATION, selector.getOwnerDef().getId(), selector.getId());
                Restrictions current = source.getOnlyCurrentResourceRestrictions(selectorHash);
                if (current != null) {
                    selectExists = true;
                    selectorPresentationDescription(selector, current, presentTable, selectorHash);
                }
            }
            if (!selectExists) {
                Table.Row selectItemsRow = presentTable.addNewRow();
                Table.Row.Cell rightCell = selectItemsRow.addNewCell();
                rightCell.setColspan(6);
                rightCell.addNewText().setStringValue("<Not Defined>");
            }

        }
        if (!defExists) {
            Table.Row row = presentationsTable.addNewRow();
            row.addNewCell().addNewText().setStringValue("<Not Defined>");
        }
    }

    private void editorPresentationDescription(AdsEditorPresentationDef editor, EntityObjectPresentations presentations, Restrictions restriction, Table table, String hash) {

        Table.Row editorItemsRow = table.addNewRow();
        Table.Row.Cell editorItemsCell = editorItemsRow.addNewCell();
        getWriter().addText(editorItemsCell, editor.getQualifiedName());

        Text resValue = editorItemsRow.addNewCell().addNewText();
        if (restriction != null) {
            resValue.setStringValue("\u2714");
        } else {
            resValue.setStringValue("");
        }
        long res;
        res = ERestriction.ACCESS.getValue()
                | ERestriction.CREATE.getValue()
                | ERestriction.DELETE.getValue()
                | ERestriction.UPDATE.getValue()
                | ERestriction.VIEW.getValue()
                | ERestriction.ANY_COMMAND.getValue()
                | ERestriction.ANY_CHILD.getValue()
                | ERestriction.ANY_PAGES.getValue();

        Table.Row.Cell rightCell = editorItemsRow.addNewCell();

        stringForRightsInPresentations(rightCell, hash, res);
        enabledCommandsRightsForEditorPresentations(editorItemsRow, hash, presentations);
        enabledChildrenRights(editorItemsRow, hash, editor);
        enabledPagesRights(editorItemsRow, hash, editor);
    }

    private void selectorPresentationDescription(AdsSelectorPresentationDef selector, Restrictions current, Table table, String hash) {

        Table.Row selectorItemsRow = table.addNewRow();
        Table.Row.Cell selectorItemsCell = selectorItemsRow.addNewCell();
        getWriter().addText(selectorItemsCell, selector.getQualifiedName());

        Text resValue = selectorItemsRow.addNewCell().addNewText();
        if (current != null) {
            resValue.setStringValue("\u2714");
        } else {
            resValue.setStringValue("");
        }
        long res;
        res = ERestriction.ACCESS.getValue()
                | ERestriction.CREATE.getValue()
                | ERestriction.DELETE_ALL.getValue()
                | ERestriction.ANY_COMMAND.getValue();
        Table.Row.Cell rightCell = selectorItemsRow.addNewCell();

        stringForRightsInPresentations(rightCell, hash, res);
        enabledCommandsRightsForSelectorPresentations(selectorItemsRow, hash, selector);

    }
    
    private List<AdsScopeCommandDef> getCommands(ExtendableDefinitions<AdsScopeCommandDef> lookup, Restrictions restr, final boolean condition) {
        final List<AdsScopeCommandDef> commands = new ArrayList<>();
        if (lookup != null) {
            if (condition) {
                commands.addAll(lookup.get(EScope.LOCAL_AND_OVERWRITE));
            } else {
                List<Id> comPriorId = restr.getEnabledCommandIds();
                if (!comPriorId.isEmpty()) {
                    for (Id commId : comPriorId) {
                        AdsScopeCommandDef comm = lookup.findById(commId, EScope.ALL).get();
                        if (comm != null) {
                            commands.add(comm);
                        }
                    }
                }
            }
        }
        return commands;
    }
    
    private void appendInfoAboutRadixObjectRights(List<? extends RadixObject> objects, Table.Row.Cell rightCell, String title) {
        if (objects.isEmpty()) {
            return;
        }
        Block inheritRestrBlock = rightCell.addNewBlock();
        Text nameInheritText = inheritRestrBlock.addNewText();
        nameInheritText.setStyle(DefaultStyle.IDENTIFIER);
        nameInheritText.setStringValue(title);
        Text inheritRestrText = inheritRestrBlock.addNewText();
        StringBuilder inheritRights = new StringBuilder();

        for (int j = 0; j < objects.size(); j++) {
            inheritRights.append(objects.get(j).getName());
            if (j != objects.size() - 1) {
                inheritRights.append(", ");

            }
        }
        inheritRestrText.setStringValue(inheritRights.toString());
    }
    
    private void enabledCommandsRightsForSelectorPresentations(Table.Row row, String hash, AdsSelectorPresentationDef selector) {
        Table.Row.Cell rightCell = row.addNewCell();
        rightCell.setColspan(3);

        Restrictions prior = source.getOverwriteAndAncestordResourceRestrictions(hash, null);
        Restrictions current = source.getOnlyCurrentResourceRestrictions(hash);
        Restrictions total;
        if (current == null) {
            total = prior;
        } else {
            total = current;
        }

        ExtendableDefinitions<AdsScopeCommandDef> lookup = selector.getCommandsLookup();
        List<AdsScopeCommandDef> inheritList = getCommands(lookup, prior, !prior.isDenied(ERestriction.ANY_COMMAND));        
        appendInfoAboutRadixObjectRights(inheritList, rightCell, "Inherit: ");
        
        if (current != null) {
            List<AdsScopeCommandDef> currList = getCommands(lookup, current, !current.isDenied(ERestriction.ANY_COMMAND) && !current.isDenied(ERestriction.ACCESS));
            appendInfoAboutRadixObjectRights(currList, rightCell, "Own: ");
        }

        List<AdsScopeCommandDef> totalList = getCommands(lookup, total, !total.isDenied(ERestriction.ANY_COMMAND) && !total.isDenied(ERestriction.ACCESS));
        appendInfoAboutRadixObjectRights(totalList, rightCell, "Total: ");
    }

    private void enabledCommandsRightsForEditorPresentations(Table.Row row, String hash, EntityObjectPresentations presentations) {

        Table.Row.Cell rightCell = row.addNewCell();

        Restrictions prior = source.getOverwriteAndAncestordResourceRestrictions(hash, null);
        Restrictions current = source.getOnlyCurrentResourceRestrictions(hash);
        Restrictions total = current == null ? prior : current;
        
        ExtendableDefinitions<AdsScopeCommandDef> lookup = presentations.getCommands();
        List<AdsScopeCommandDef> inheritList = getCommands(lookup, prior, !prior.isDenied(ERestriction.ANY_COMMAND));
        appendInfoAboutRadixObjectRights(inheritList, rightCell, "Inherit: ");
        
        if (current != null) {
            List<AdsScopeCommandDef> currList = getCommands(lookup, current, !current.isDenied(ERestriction.ANY_COMMAND) && !current.isDenied(ERestriction.ACCESS));
            appendInfoAboutRadixObjectRights(currList, rightCell, "Own: ");
        }
        
        List<AdsScopeCommandDef> totalList = getCommands(lookup, total, !total.isDenied(ERestriction.ANY_COMMAND) && !total.isDenied(ERestriction.ACCESS));
        appendInfoAboutRadixObjectRights(totalList, rightCell, "Total: ");
    }
    
    List<AdsExplorerItemDef> getExplorerItems(ExplorerItems items, Restrictions restr, final boolean condition) {
        List<AdsExplorerItemDef> defs = new ArrayList<>();
        if (items != null) {
            if (condition) {
                defs.addAll(items.getChildren().get(EScope.ALL));
            } else {
                List<Id> childPriorId = restr.getEnabledChildIds();
                if (!childPriorId.isEmpty()) {
                    for (int i = 0; i < childPriorId.size(); i++) {
                        AdsExplorerItemDef item = items.getChildren().findById(childPriorId.get(i), EScope.LOCAL_AND_OVERWRITE).get();
                        if (item != null) {
                            defs.add(item);
                        }
                    }
                }
            }
        }
        return defs;
    }

    private void enabledChildrenRights(Table.Row row, String hash, AdsEditorPresentationDef editor) {
        Table.Row.Cell rightCell = row.addNewCell();
        Restrictions prior = source.getOverwriteAndAncestordResourceRestrictions(hash, null);
        Restrictions current = source.getOnlyCurrentResourceRestrictions(hash);
        Restrictions total = current == null ? prior : current;
        
        ExplorerItems items = editor.getExplorerItems();
        List<AdsExplorerItemDef> inheritList = getExplorerItems(items, prior, !prior.isDenied(ERestriction.ANY_CHILD));
        appendInfoAboutRadixObjectRights(inheritList, rightCell, "Inherit: ");
        
        if (current != null) {
            List<AdsExplorerItemDef> currList = getExplorerItems(items, current, 
                    !current.isDenied(ERestriction.ANY_CHILD) && !current.isDenied(ERestriction.ACCESS));
            appendInfoAboutRadixObjectRights(currList, rightCell, "Own: ");
        }
        
        List<AdsExplorerItemDef> totalList = getExplorerItems(items, total, !total.isDenied(ERestriction.ANY_CHILD) && !total.isDenied(ERestriction.ACCESS));
        appendInfoAboutRadixObjectRights(totalList, rightCell, "Total: ");
    }
    
    private List<AdsEditorPageDef> getEditorPages(EditorPages pages, Restrictions restr, final boolean condition) {
        List<AdsEditorPageDef> defs = new ArrayList<>();
        if (pages != null) {
            if (condition) {
                defs.addAll(pages.get(EScope.ALL));
            } else {
                List<Id> pagePriorId = restr.getEnabledPageIds();
                if (!pagePriorId.isEmpty()) {
                    for (int i = 0; i < pagePriorId.size(); i++) {
                        AdsEditorPageDef page_ = pages.findById(pagePriorId.get(i), EScope.LOCAL_AND_OVERWRITE).get();
                        if (page_ != null) {
                            defs.add(page_);
                        }
                    }
                }
            }
        }
        return defs;
    }

    private void enabledPagesRights(Table.Row row, String hash, AdsEditorPresentationDef editor) {
        Table.Row.Cell rightCell = row.addNewCell();
        Restrictions prior = source.getOverwriteAndAncestordResourceRestrictions(hash, null);
        Restrictions current = source.getOnlyCurrentResourceRestrictions(hash);
        Restrictions total;
        if (current == null) {
            total = prior;
        } else {
            total = current;
        }
        EditorPages pages = editor.getEditorPages();
        List<AdsEditorPageDef> inheritList = getEditorPages(pages, prior, !prior.isDenied(ERestriction.ANY_PAGES));
        appendInfoAboutRadixObjectRights(inheritList, rightCell, "Inherit: ");
        

        if (current != null) {
            List<AdsEditorPageDef> currList = getEditorPages(pages, current, 
                    !current.isDenied(ERestriction.ANY_PAGES) && !current.isDenied(ERestriction.ACCESS));
            appendInfoAboutRadixObjectRights(currList, rightCell, "Own: ");
        }
        
        List<AdsEditorPageDef> totalList = getEditorPages(pages, total, 
                !total.isDenied(ERestriction.ANY_PAGES) && !total.isDenied(ERestriction.ACCESS));
        appendInfoAboutRadixObjectRights(totalList, rightCell, "Total: ");
    }

    private void stringForRightsInPresentations(Table.Row.Cell cell, String hash, Long res) {
        Restrictions prior = source.getOverwriteAndAncestordResourceRestrictions(hash, null);
        Restrictions current = source.getOnlyCurrentResourceRestrictions(hash);
        Restrictions total;
        if (current == null) {
            total = prior;
        } else {
            total = current;
        }
        List<ERestriction> priorRestr = new ArrayList<>();
        List<ERestriction> currRestr = new ArrayList<>();
        List<ERestriction> totalRestr = new ArrayList<>();

        for (ERestriction restr : ERestriction.fromBitField(res)) {
            if (!prior.isDenied(restr)) {
                priorRestr.add(restr);
            }
        }
        if (!priorRestr.isEmpty()) {
            Block priorRestrBlock = cell.addNewBlock();
            Text nameInheritText = priorRestrBlock.addNewText();
            nameInheritText.setStyle(DefaultStyle.IDENTIFIER);
            nameInheritText.setStringValue("Inherit: ");
            Text priorRestrText = priorRestrBlock.addNewText();

            StringBuilder priorRights = new StringBuilder();

            for (int i = 0; i < priorRestr.size(); i++) {
                priorRights.append(priorRestr.get(i).getAsStr());
                if (i != priorRestr.size() - 1) {
                    priorRights.append(", ");
                }
            }
            priorRestrText.setStringValue(priorRights.toString());
        }

        if (current != null) {
            for (ERestriction restr : ERestriction.fromBitField(res)) {
                if (!current.isDenied(restr)) {
                    currRestr.add(restr);
                }
            }
            if (!currRestr.isEmpty()) {
                Block currRestrBlock = cell.addNewBlock();
                Text nameOwnText = currRestrBlock.addNewText();
                nameOwnText.setStyle(DefaultStyle.IDENTIFIER);
                nameOwnText.setStringValue("Own: ");
                Text currRestrText = currRestrBlock.addNewText();
                StringBuilder currRights = new StringBuilder();

                for (int i = 0; i < currRestr.size(); i++) {
                    currRights.append(currRestr.get(i).getAsStr());
                    if (i != currRestr.size() - 1) {
                        currRights.append(", ");
                    }
                }
                currRestrText.setStringValue(currRights.toString());
            }
        }

        for (ERestriction restr : ERestriction.fromBitField(res)) {
            if (!total.isDenied(restr)) {
                totalRestr.add(restr);
            }
        }
        if (!totalRestr.isEmpty()) {
            Block totalRestrBlock = cell.addNewBlock();
            Text nameTotalText = totalRestrBlock.addNewText();
            nameTotalText.setStyle(DefaultStyle.IDENTIFIER);
            nameTotalText.setStringValue("Total: ");
            Text totalRestrText = totalRestrBlock.addNewText();
            StringBuilder totalRights = new StringBuilder();

            for (int i = 0; i < totalRestr.size(); i++) {
                totalRights.append(totalRestr.get(i).getAsStr());
                if (i != totalRestr.size() - 1) {
                    totalRights.append(", ");
                }
            }
            totalRestrText.setStringValue(totalRights.toString());
        }
    }
}
