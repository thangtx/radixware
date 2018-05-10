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
import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.enums.EEnumDefinitionItemViewFormat;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.radixdoc.AbstractDefDocItem;
import org.radixware.schemas.radixdoc.AdsEnumDefDocItem;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.EnumItemDocEntries;
import org.radixware.schemas.radixdoc.EnumItemDocEntry;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Ref;
import org.radixware.schemas.radixdoc.Table;
import org.radixware.schemas.radixdoc.Text;


public class EnumRadixdoc extends AdsDefinitionRadixdoc<AdsEnumDef> {

    private class ItemsSummaryChapterFactory {

        private Block chapter;
        private AdsEnumDef source;
        private BlockBuilder container;

        public ItemsSummaryChapterFactory(AdsEnumDef source, BlockBuilder container) {
            this.source = source;
            this.container = container;
        }

        public final Block getChapter() {
            return getChapter(true);
        }

        public final Block getChapter(boolean create) {
            if (chapter == null && create) {
                chapter = createChapter();
            }
            return chapter;
        }

        public final Block document() {
            final HashSet<AdsEnumItemDef> used = new HashSet<>();

            List<AdsEnumItemDef> apiElements = getApiElements(getAllElements(source), used);
            if (!apiElements.isEmpty()) {
                documentElements(getChapter(), source, apiElements, getElementTypeName());
            }

            return getChapter(false);
        }

        protected void documentElements(ContentContainer container, AdsEnumDef cls, List<AdsEnumItemDef> apiElements, String name) {
            final Table table = container.addNewTable();
            prepareElementsTable(cls, table);
            for (final AdsEnumItemDef elem : apiElements) {
                documentElementSummary(elem, table);
            }
        }

        protected boolean isApiElement(AdsEnumItemDef elem) {
            return true;
        }

        protected final List<AdsEnumItemDef> getApiElements(List<AdsEnumItemDef> all, Set<AdsEnumItemDef> used) {
            final List<AdsEnumItemDef> apiElements = new ArrayList<>();
            for (final AdsEnumItemDef elem : all) {
                if (isApiElement(elem) && !used.contains(elem)) {
                    apiElements.add(elem);
                    used.add(elem);
                }
            }
            if (apiElements.isEmpty()) {
                return Collections.emptyList();
            }

            sort(apiElements);

            return apiElements;
        }

        protected void sort(List<AdsEnumItemDef> elements) {
            Collections.sort(elements, new Comparator<AdsEnumItemDef>() {
                @Override
                public int compare(AdsEnumItemDef o1, AdsEnumItemDef o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }

        protected void prepareElementsTable(AdsEnumDef enm, Table table) {
            final Table.Row head = table.addNewRow();
            head.setMeta("head");

            head.addNewCell().addNewText().setStringValue("View Index");

            head.addNewCell().addNewText().setStringValue("Name");
            head.addNewCell().addNewText().setStringValue("Value");
            head.addNewCell().addNewText().setStringValue("Title");
            head.addNewCell().addNewText().setStringValue("Domains");


            table.setStyle(DefaultStyle.MEMBERS);
        }

        protected void documentElementSummary(AdsEnumItemDef elem, Table table) {

            final Table.Row methodRow = table.addNewRow();

            if (!source.isPlatformEnumPublisher()) {
                methodRow.setStyle(DefaultStyle.INHERITED_ITEM);
            } else if (elem.getOwnerDef() != source) {
                methodRow.setStyle(DefaultStyle.INHERITED_ITEM);
            } else {
                if (elem.isPlatformItemPublisher()) {
                    if (elem.isOverwrite()) {
                        methodRow.setStyle(DefaultStyle.OVERWRITTEN);
                    } else {
                        methodRow.setStyle(DefaultStyle.INHERITED_ITEM);
                    }
                } else {
                    final SearchResult<AdsEnumItemDef> overwritten = elem.getHierarchy().findOverwritten();
                    if (overwritten.isEmpty()) {
                        methodRow.setStyle(DefaultStyle.OWN_ITEM);
                    } else {
                        methodRow.setStyle(DefaultStyle.OVERWRITTEN);
                    }
                }
            }

            final Table.Row.Cell indexCell = methodRow.addNewCell();
            getWriter().addText(indexCell, String.valueOf(elem.getViewOrder()));

            final Table.Row.Cell nameCell = methodRow.addNewCell();
            getWriter().addText(nameCell, elem.getName());

            final String value;
            if (source.getItemType() == EValType.INT && source.getItemsViewFormat() == EEnumDefinitionItemViewFormat.HEXADECIMAL) {
                value = "0x" + Long.toHexString((Long) elem.getValue().toObject(EValType.INT)).toUpperCase();
            } else {
                value = elem.getValue().toString();
            }
            final Table.Row.Cell valueCell = methodRow.addNewCell();
            valueCell.setStyle("");
            getWriter().addText(valueCell, value);

            final Table.Row.Cell descriptionCell = methodRow.addNewCell();
            final Block descritprionBlock = descriptionCell.addNewBlock();
            descritprionBlock.setStyle(DefaultStyle.DESCRIPTION);
            if (elem.getTitleId() != null && elem.findExistingLocalizingBundle() != null) {
                getWriter().addMslId(descritprionBlock, elem.findExistingLocalizingBundle().getId(), elem.getTitleId());
            } else {
                 getWriter().addText(descritprionBlock, "<Not Defined>");
            }

            final Table.Row.Cell domainCell = methodRow.addNewCell();


            Collection<Id> domainId = elem.getDomainIds();
            if (domainId == null || domainId.isEmpty()) {
                getWriter().addText(domainCell, "-");
            }
            String sNames = "";
            List<AdsPath> pathes = elem.getDomains().getUsedDomainPathes();

            for (AdsPath path : pathes) {
                Id idLst[] = path.asArray();
                if (idLst.length > 0) {
                    AdsDomainDef def = (AdsDomainDef) AdsUtils.findTopLevelDefById(elem.getModule().getSegment().getLayer(), idLst[0]);
                    String sName;
                    if (def != null) {
                        def = (AdsDomainDef) def.findComponentDefinition(idLst[idLst.length - 1]).get();
                        sName = def == null ? path.toString() : def.getQualifiedName();
                    } else {
                        sName = path.toString();
                    }
                    if (sNames.isEmpty()) {
                        sNames = sName;
                    } else {
                        domainCell.addNewText().setStringValue(", ");
                    }
                    
                    Ref enumRef = domainCell.addNewRef();
                    enumRef.setPath(resolve(source, def));
                    getWriter().addText(enumRef, sNames);
                }
            }
        }

        protected Block createChapter() {
            return getWriter().createChapter(container.getBlock(), getElementTypeName());
        }

        protected String getElementTypeName() {
            return "Enum Item";
        }

        protected List<AdsEnumItemDef> getAllElements(AdsEnumDef c) {
            return c.getItems().list(ExtendableDefinitions.EScope.LOCAL);
        }
    }

    private class EnumWriter extends AdsPageWriter<AdsEnumDef> {

        public EnumWriter(AdsDefinitionRadixdoc<AdsEnumDef> page) {
            super(page);
        }
    }

    public EnumRadixdoc(AdsEnumDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected AdsPageWriter<AdsEnumDef> createWriter() {
        return new EnumWriter(this);
    }

    @Override
    @SuppressWarnings("empty-statement")
    protected void documentDescriptionExtensions(ContentContainer container) {
        final Block name = container.addNewBlock();
        name.setStyle(DefaultStyle.SUB_NAME);
        if (source.isExtendable()) {
            getWriter().addStrTitle(name, " (Extendable publishing platform enumeration)");
        } else {
            getWriter().addStrTitle(name, " (Publishing platform enumeration)");
        }

        final Block itemType = container.addNewBlock();
        itemType.setStyle(DefaultStyle.DEFINITION);
        getWriter().addStrTitle(itemType, "Element value type:");
        Text titleText = itemType.addNewBlock().addNewText();
        titleText.setStringValue(getSource().getItemType().getName());

        if (source.isPlatformEnumPublisher()) {
            final Block titleDefinition = container.addNewBlock();
            titleDefinition.setStyle(DefaultStyle.DEFINITION);
            getWriter().addStrTitle(titleDefinition, "Published platform enumeration:");
            titleText = titleDefinition.addNewBlock().addNewText();
            titleText.setStringValue(source.getPublishedPlatformEnumName());
        }
        if (source.getValueRanges().getRegexp() != null) {
            final Block regexpDescription = container.addNewBlock();
            regexpDescription.setStyle(DefaultStyle.DEFINITION);
            getWriter().addStrTitle(regexpDescription, "Values cannot be used as values of the overriding enumerations if they match this regular expression:");

            final Block expDescription = container.addNewBlock();
            expDescription.setStyle(DefaultStyle.TEXT_MULTILINE);
            Text textExpression = expDescription.addNewBlock().addNewText();
            textExpression.setStringValue(source.getValueRanges().getRegexp());
        }
        final Block legendBlock = page.addNewBlock();
        legendBlock.setStyle(DefaultStyle.LEGEND);

        if (source.isPlatformEnumPublisher()) {
            final Block iconTextInheritedItemBlock = legendBlock.addNewBlock();
            iconTextInheritedItemBlock.setStyle(DefaultStyle.INLINE_TEXT);
            final Block iconInheritedItemBlock = iconTextInheritedItemBlock.addNewBlock();
            iconInheritedItemBlock.setStyle(DefaultStyle.ICON_IMAGE_INHERITED_ITEM);
            Block textBlockInheritedItem = iconTextInheritedItemBlock.addNewBlock();
            textBlockInheritedItem.setStyle(DefaultStyle.INLINE_TEXT);
            Text iconTextInheritedItem = textBlockInheritedItem.addNewText();
            iconTextInheritedItem.setStringValue(" - inherited items; ");

            final Block iconTextBlock = legendBlock.addNewBlock();
            iconTextBlock.setStyle(DefaultStyle.INLINE_TEXT);
            final Block iconBlockOverwrittenItem = iconTextBlock.addNewBlock();
            iconBlockOverwrittenItem.setStyle(DefaultStyle.ICON_IMAGE_OVERWRITTEN_ITEM);
            Block textBlockOverwrittenItem = iconTextBlock.addNewBlock();
            textBlockOverwrittenItem.setStyle(DefaultStyle.INLINE_TEXT);
            Text iconTextOverwrittenItem = textBlockOverwrittenItem.addNewText();
            iconTextOverwrittenItem.setStringValue(" - overwritten items; ");

            final Block iconTextOwnItemBlock = legendBlock.addNewBlock();
            iconTextOwnItemBlock.setStyle(DefaultStyle.INLINE_TEXT);
            final Block iconOwnItemBlock = iconTextOwnItemBlock.addNewBlock();
            iconOwnItemBlock.setStyle(DefaultStyle.ICON_IMAGE_OWN_ITEM);
            Block textBlocknOwnItem = iconTextOwnItemBlock.addNewBlock();
            textBlocknOwnItem.setStyle(DefaultStyle.INLINE_TEXT);
            Text iconTextnOwnItem = textBlocknOwnItem.addNewText();
            iconTextnOwnItem.setStringValue(" - own items. ");
        }
    }

    @Override
    protected void documentOverview(ContentContainer container) {
        super.documentOverview(container);
    }

    @Override
    protected void documentContent(final Page page) {
        final BlockBuilder summary = new BlockBuilder() {
            @Override
            public Block createElement() {
                final Block summaryChapter = page.addNewBlock();
                summaryChapter.setStyle(DefaultStyle.SUMMARY);
                return summaryChapter;
            }
        };

        final BlockBuilder summaryChapter = new BlockBuilder() {
            @Override
            public Block createElement() {
                final Block summaryChapter = summary.getBlock().addNewBlock();
                summaryChapter.setStyle(DefaultStyle.CHAPTER);
                getWriter().addStrTitle(summaryChapter, "Elements List");
                return summaryChapter;
            }
        };

        new ItemsSummaryChapterFactory(source, summaryChapter).document();
    }

    @Override
    public AbstractDefDocItem buildDocItem() {
        AdsEnumDefDocItem result = AdsEnumDefDocItem.Factory.newInstance();
        
        result.setId(source.getId());
        result.setName(source.getName());
        result.setTitleId(source.getTitleId());
        result.setDefinitionType(EDefType.ENUMERATION);
        result.setDescriptionId(source.getDescriptionId());
        result.setIsDepricated(source.isDeprecated());
        result.setIsFinal(source.isFinal());
        result.setIsPublished(source.isPublished());
        result.setItemsType(source.getItemType().getName());
        result.setIsOverwrite(source.isOverwrite());
        
        if (source.isPlatformEnumPublisher()) {
            result.setPlatformEnum(source.getPublishedPlatformEnumName());
        }
        
        if (source.getValueRanges() != null && source.getValueRanges().getRegexp() != null) {
            result.addNewRestrictions().setRegExp(source.getValueRanges().getRegexp());            
        }
        
        EnumItemDocEntries xItems = result.addNewEnumItemsDocEntries();
               
        List<AdsEnumItemDef> sortedItems = new ArrayList<>(source.getItems().list(ExtendableDefinitions.EScope.ALL));
        Collections.sort(sortedItems, new Comparator<AdsEnumItemDef>() {

            @Override
            public int compare(AdsEnumItemDef o1, AdsEnumItemDef o2) {
                
                Object obj1 = o1.getValue().toObject(source.getItemType());
                Object obj2 = o2.getValue().toObject(source.getItemType());                                              
                
                if (source.getItemType() == EValType.INT) {
                    return ((Long) obj1).compareTo((Long) obj2);
                } else {
                    String strVal1 = String.valueOf(obj1);
                    String strVal2 = String.valueOf(obj2);
                    
                    return strVal1.compareTo(strVal2);
                }
            }
        });
        
        for (AdsEnumItemDef item : sortedItems) {
            EnumItemDocEntry xItem = xItems.addNewEnumItemDocEntry();
            
            xItem.setViewOrder(String.valueOf(item.getViewOrder()));
            xItem.setName(item.getName());
            
            String value;
            if (source.getItemType() == EValType.INT && source.getItemsViewFormat() == EEnumDefinitionItemViewFormat.HEXADECIMAL) {
                value = "0x" + Long.toHexString((Long) item.getValue().toObject(EValType.INT)).toUpperCase();
            } else {
                value = item.getValue().toString();
            }
            
            xItem.setValue(value);
            xItem.setTitleId(item.getTitleId());
            xItem.setDescriptionId(item.getDescriptionId());
            xItem.setDomains(item.getDomainsString());
        }

        return result;
    }
}
