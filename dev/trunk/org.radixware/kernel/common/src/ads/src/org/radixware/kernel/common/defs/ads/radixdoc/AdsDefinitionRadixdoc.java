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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IInheritableTitledDefinition;
import org.radixware.kernel.common.defs.ads.ITitledDefinition;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.radixdoc.TypeDocument.Entry;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.radixdoc.DefaultMeta;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.kernel.common.radixdoc.RadixdocXmlPage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.graphs.GraphWalker;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.ElementList;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Ref;
import org.radixware.schemas.radixdoc.Table;

public abstract class AdsDefinitionRadixdoc<T extends AdsDefinition> extends RadixdocXmlPage<T> {    
    
    public static class AdsPageWriter<T extends AdsDefinition> extends PageWriter<T> {

        public AdsPageWriter(AdsDefinitionRadixdoc<T> page) {
            super(page);
        }

        protected final void documentType(ContentContainer root, TypeDocument typeDocument, Definition context) {
            final StringBuilder sb = new StringBuilder();

            for (final Entry typeDocEntry : typeDocument.getEntries()) {
                if (typeDocEntry.isString()) {
                    sb.append(typeDocEntry.getString());
                } else {
                    if (sb.length() > 0) {
                        addText(root, sb.toString());
                        sb.delete(0, sb.length());
                    }
                    final Ref ref = root.addNewRef();
                    ref.setPath(resolveTypeEntry(context, typeDocEntry));
                    if (!typeDocEntry.hasName()) {
                        addText(ref, typeDocEntry.getDefinition().getQualifiedName(context));
                    } else {
                        addText(ref, typeDocEntry.getString());
                    }
                    ref.setTitle(typeDocEntry.getDeclaration().getQualifiedName(context));
                }
            }

            if (sb.length() > 0) {
                addText(root, sb.toString());
                sb.delete(0, sb.length());
            }
        }
        
        protected String resolveTypeEntry(Definition context, Entry typeDocEntry) {
            String ref = page.resolve(context, typeDocEntry.getDefinition());
            
            if (!(typeDocEntry.getDefinition() instanceof AdsXmlSchemeDef) || !typeDocEntry.hasName()) {
                return ref;
            } else {
                return RadixdocUtils.resolveXsdNode(context, typeDocEntry, ref);
            }           
        }

//        protected final void documentType(ContentContainer root, AdsTypeDeclaration declaration, Definition context) {
//            final TypeDocument typeDocument = new TypeDocument();
//            typeDocument.addType(declaration, context);
//            documentType(root, typeDocument, context);
//        }
        protected void documentDescription(ContentContainer root, AdsDefinition definition) {
            AdsDefinition descriptionProvider = findOwnerDescriptionDefinition(definition);
            if (descriptionProvider == null) {
                descriptionProvider = definition;
            }
            if (descriptionProvider.getDescriptionId() != null || (descriptionProvider.getDescription() != null && !descriptionProvider.getDescription().isEmpty())) {
                if (descriptionProvider.getDescriptionId() != null) {
                    ILocalizingBundleDef bundle = null;
                    if (descriptionProvider.getDescriptionLocation() != null) {
                        bundle = descriptionProvider.getDescriptionLocation().findExistingLocalizingBundle();
                    }
                    if (bundle != null) {
                        addMslId(root, descriptionProvider.getDescriptionLocation(), bundle.getId(), descriptionProvider.getDescriptionId());
                    } else {
                        addText(root, descriptionProvider.getDescription());
                    }
                } else {
                    addText(root, descriptionProvider.getDescription());
                }
            }
        }

        protected static AdsDefinition findOwnerDescriptionDefinition(AdsDefinition definition) {
            AdsDefinition searchResult;
            if (definition.getDescriptionId() != null || (definition.getDescription() != null && !definition.getDescription().isEmpty())) {
                return definition;
            } else if ((searchResult = definition.getHierarchy().findOverwritten().get()) != null) {
                return findOwnerDescriptionDefinition(searchResult);
            } else if ((searchResult = definition.getHierarchy().findOverridden().get()) != null) {
                return findOwnerDescriptionDefinition(searchResult);
            }
            return null;
        }

        protected static AdsDefinition findOwnerTitleDefinition(AdsDefinition definition) {
            if (definition instanceof ITitledDefinition) {
                AdsDefinition searchResult;
                ITitledDefinition titledDef = (ITitledDefinition) definition;
                if (titledDef.getTitleId() != null) {
                    return definition;
                } else if ((searchResult = definition.getHierarchy().findOverwritten().get()) != null) {
                    return findOwnerTitleDefinition(searchResult);
                } else if ((searchResult = definition.getHierarchy().findOverridden().get()) != null) {
                    return findOwnerTitleDefinition(searchResult);
                }
            }
            return null;
        }

        private void prepareTable(Table table, String[] headers, Integer[] colSpans) {
            table.setStyle(DefaultStyle.MEMBERS);
            Table.Row header = table.addNewRow();
            header.setMeta("head");
            if (colSpans != null) {
                int colNum = 0;
                for (String headerName : headers) {
                    Table.Row.Cell cell = header.addNewCell();
                    cell.addNewText().setStringValue(headerName);
                    cell.setColspan(colSpans[colNum++]);
                }
            } else {
                for (String headerName : headers) {
                    Table.Row.Cell cell = header.addNewCell();
                    cell.addNewText().setStringValue(headerName);
                }
            }
        }

        public Table addNewTable(ContentContainer detailBlock, String[] headers, Integer[] colSpans) {
            Table table = detailBlock.addNewTable();
            prepareTable(table, headers, colSpans);
            return table;
        }

        public Table addNewTable(ContentContainer detailBlock, String... headers) {
            return addNewTable(detailBlock, headers, null);
        }

        public Table addGeneralAttrTable(ContentContainer detailBlock) {
            Table generalAttrsTable = addNewTable(detailBlock, new String[]{"General Attributes"}, new Integer[]{2});
            appendStyle(generalAttrsTable, DefaultStyle.GENERAL_ATTRIBUTES);
            return generalAttrsTable;
        }                

        protected boolean isApiElement(AdsDefinition elem) {
            return elem.isPublished() && !elem.getAccessMode().isLess(EAccess.PROTECTED);
        }

        public void writeElementsList(ContentContainer container, Collection<? extends AdsDefinition> elements, String elemTypeName) {
            writeElementsList(container, elements, elemTypeName, null);
        }

        public void writeExplorerChildrensList(ContentContainer container, Collection<? extends AdsExplorerItemDef> elements, String elemTypeName) {
            writeElementsList(container, elements, elemTypeName, new ElementHandler<AdsExplorerItemDef>() {
                @Override
                public void handle(ContentContainer container, AdsExplorerItemDef elem) {
                    container.addNewText().setStringValue(elem.getClass().getSimpleName() + " ");
                }
            });
        }

        public void writeElementsList(ContentContainer container, Collection<? extends AdsDefinition> elements, String elemTypeName, ElementHandler handler) {
            if (elements == null || elements.isEmpty()) {
                return;
            }

            Table elemsTable = null;
            boolean isApiElemFinded = false;
            for (AdsDefinition elem : elements) {
                if (isApiElement(elem)) {
                    if (!isApiElemFinded) {
                        elemsTable = addNewTable(container, elemTypeName);
                        isApiElemFinded = true;
                    }
                    if (elemsTable != null) {
                        final Table.Row row = elemsTable.addNewRow();
                        final Table.Row.Cell nameCell = row.addNewCell();
                        final RadixIconResource resource = new RadixIconResource(elem.getIcon());
                        nameCell.addNewResource().setSource(resource.getKey());
                        page.addResource(resource);
                        if (handler != null) {
                            handler.handle(nameCell, elem);
                        }
                        addRef(nameCell, elem, elem.getModule());
                    }
                }
            }
        }

        public interface ElementHandler<T> {

            public void handle(ContentContainer container, T elem);
        }

        public ContentContainer addCollapsibleBlock(ContentContainer collapsibleBlock, String blockTitle) {
            collapsibleBlock.setMeta(DefaultMeta.Block.COLLAPSIBLE);
            Block titleBlock = collapsibleBlock.addNewBlock();
            titleBlock.addNewText().setStringValue(blockTitle);
            titleBlock.setStyle(DefaultStyle.TITLE);
            titleBlock.setMeta(DefaultMeta.Text.TITLE);
            return collapsibleBlock;
        }

        public Table setBlockCollapsibleAndAddTable(ContentContainer collapsibleBlock, String tableTitle, String... headers) {
            addCollapsibleBlock(collapsibleBlock, tableTitle);
            Block tableContent = collapsibleBlock.addNewBlock();
            tableContent.setMeta(DefaultMeta.Block.CONTENT);
            return addNewTable(tableContent, headers);
        }

        public void addStr2RefRow(Table detailsTable, String rowName, RadixObject refObject, Definition context) {
            Table.Row parentPropRow = detailsTable.addNewRow();
            parentPropRow.addNewCell().addNewText().setStringValue(rowName);
            Ref ref = parentPropRow.addNewCell().addNewRef();
            if (refObject != null) {
                ref.setTitle(refObject.getQualifiedName());
                ref.setPath(page.resolve(context, refObject));
                addText(ref, refObject.getQualifiedName());
            }
        }
        
        public void addRefRow(Table detailsTable, String rowName, RadixObject refObject, Definition context) {
            Table.Row parentPropRow = detailsTable.addNewRow();           
            Ref ref = parentPropRow.addNewCell().addNewRef();
            if (refObject != null) {
                ref.setTitle(refObject.getQualifiedName());
                ref.setPath(page.resolve(context, refObject));
                addText(ref, refObject.getQualifiedName());
            }
        }

        public void addAllStrRow(Table detailsTable, String... values) {
            Table.Row newRow = detailsTable.addNewRow();
            for (String value : values) {
                newRow.addNewCell().addNewText().setStringValue(value);
            }
        }

        public void addStr2MslIdRow(Table detailsTable, String property, Id bundleId, Id strId) {
            Table.Row newRow = detailsTable.addNewRow();
            newRow.addNewCell().addNewText().setStringValue(property);
            if (bundleId != null && strId != null) {
                addMslId(newRow.addNewCell(), bundleId, strId);
            }
        }

        public void addStr2BoolRow(Table detailsTable, String property, Boolean value) {
            Table.Row newRow = detailsTable.addNewRow();
            newRow.addNewCell().addNewText().setStringValue(property);
            newRow.addNewCell().addNewText().setStringValue(boolAsStr(value));
        }

        public String boolAsStr(boolean boolVal) {
//            return boolVal ? "\u2714" : "\u2718";
            return boolVal ? "Yes" : "No";
        }

        public String getRestrictionsAsStr(final long restrictionsMask) {
            StringBuilder restrictions = new StringBuilder();
            for (ERestriction restr : ERestriction.fromBitField(restrictionsMask)) {
                restrictions.append(restr.getAsStr());
                restrictions.append(", ");
            }
            if (restrictions.length() > 0) {
                restrictions.setLength(restrictions.length() - 2);
            }
            return restrictions.toString();
        }
    }
    private AdsPageWriter<T> writer;

    public AdsDefinitionRadixdoc(T source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    protected AdsPageWriter<T> createWriter() {
        return new AdsPageWriter<>(this);
    }

    @Override
    public void buildPage() {
        page.setTitle(source.getName());
        StringBuilder pageName  = new StringBuilder();
        boolean first = true;
        for(Id id : source.getIdPath()){
            if(first)
                first = false;
            else
                pageName.append("-");
            pageName.append(id.toString());
        }
        page.setName(pageName.toString());
        page.setTopLevel(isTopLevelDefinition());

        final RadixIconResource iconResource = new RadixIconResource(source.getIcon());
        addResource(iconResource);

        page.setIcon(iconResource.getKey());

        documentHead(page);
        documentDescription(page);

        documentContent(page);
    }

    protected Block documentDescription(Page page) {
        final Block descriptionChapter = page.addNewBlock();
        descriptionChapter.setStyle(DefaultStyle.DESCRIPTION);

        documentHierarchy(descriptionChapter);

        documentDescriptionExtensions(descriptionChapter);

        documentOverview(descriptionChapter);

        return descriptionChapter;
    }

    protected void documentOverview(ContentContainer container) {
        final Block overview = container.addNewBlock();
        getWriter().appendStyle(overview, DefaultStyle.CHAPTER, DefaultStyle.OVERVIEW);
        getWriter().addStrTitle(overview, "Overview");

        getWriter().documentDescription(overview, source);

        writeDefSpecificInfo(overview);
    }

    protected void writeDefSpecificInfo(ContentContainer overview) {
        //overwrite
    }

    protected void documentDescriptionExtensions(ContentContainer container) {
        if (source instanceof ITitledDefinition && ((ITitledDefinition) source).getTitleId() != null) {
            final Block titleDefinition = container.addNewBlock();
            titleDefinition.setStyle(DefaultStyle.DEFINITION);
            getWriter().addStrTitle(titleDefinition, "Title:");
            AdsDefinition titleProviderDef = getWriter().findOwnerTitleDefinition(source);
            if (titleProviderDef == null) {
                titleProviderDef = source;
            }
            ILocalizingBundleDef bundle = null;
            if (titleProviderDef instanceof IInheritableTitledDefinition) {
                if (((IInheritableTitledDefinition) titleProviderDef).findOwnerTitleDefinition() != null) {
                    bundle = ((IInheritableTitledDefinition) titleProviderDef).findOwnerTitleDefinition().findExistingLocalizingBundle();
                }
            } else {
                bundle = titleProviderDef.findExistingLocalizingBundle();
            }
            if (bundle != null) {
                getWriter().addMslId(titleDefinition, titleProviderDef, bundle.getId(), ((ITitledDefinition) titleProviderDef).getTitleId());
            } else {
                getWriter().addText(titleDefinition, "<Not Defined>");
            }
        }
    }

    protected void documentHierarchy(ContentContainer container) {

        final List<T> hierarchy = new LinkedList<>();
        new GraphWalker<T>().depthWalk(new GraphWalker.NodeFilter<T>() {
            @Override
            protected boolean accept(T node, int level) {
                hierarchy.add(0, node);
                return true;
            }

            @Override
            protected Collection<T> collectNodes(T source) {
                final AdsDefinition.Hierarchy<T> classHierarchy = source.getHierarchy();
                final SearchResult<T> overwritten = classHierarchy.findOverwritten();
                final List<T> result = new ArrayList<>();
                if (!overwritten.isEmpty()) {
                    result.addAll(overwritten.all());
                    return result;
                }

                return result;
            }
        }, source);

        if (hierarchy.size() > 1) {
            final ElementList hierarchyList = container.addNewList();
            hierarchyList.setMeta(DefaultMeta.List.HIERARCHY);
            for (final T node : hierarchy) {
                final ElementList.Item item = hierarchyList.addNewItem();
                if (node == source) {
                    item.addNewText().setStringValue(node.getQualifiedName());
                } else {
                    final Ref ref = item.addNewRef();
                    ref.addNewText().setStringValue(node.getQualifiedName());
                    ref.setPath(resolve(source, node));
                }
            }
        }
    }

    protected void documentContent(Page page) {
    }

    protected boolean isTopLevelDefinition() {
        return source.isTopLevelDefinition();
    }

    @Override
    protected AdsPageWriter<T> getWriter() {
        synchronized (this) {
            if (writer == null) {
                writer = createWriter();
            }
            return writer;
        }
    }
}
