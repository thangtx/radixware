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
package org.radixware.kernel.common.radixdoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.radixdoc.*;

public abstract class RadixdocXmlPage<T extends RadixObject> implements IRadixdocPage<T> {

    protected interface IProxyFactory<T> {

        T createElement();
    }

    protected abstract class BlockBuilder implements IProxyFactory<Block> {

        private Block block = null;

        public Block getBlock() {
            if (block == null) {
                block = createElement();
            }
            return block;
        }
    }

    protected static class PageWriter<T extends RadixObject> {

        protected final RadixdocXmlPage<T> page;

        public PageWriter(RadixdocXmlPage<T> page) {
            this.page = page;
        }

        public final Attribute setAttribute(DocElement element, String name, String value) {
            final Attribute attr = element.addNewAttribute();
            attr.setName(name);
            attr.setStringValue(value);

            return attr;
        }

        public final String getAttribute(DocElement element, String name) {
            for (final Attribute attribute : element.getAttributeList()) {
                if (Objects.equals(attribute.getName(), name)) {
                    return attribute.getStringValue();
                }
            }
            return null;
        }

        public final Text addStrTitle(ContentContainer element, String title) {
            final Text titleElement = element.addNewText();
            titleElement.setStringValue(title);
            titleElement.setMeta(DefaultMeta.Text.TITLE);

            return titleElement;
        }

        public final Block addBlockTitle(ContentContainer element) {
            final Block titleElement = element.addNewBlock();
            titleElement.setStyle(DefaultStyle.TITLE);
            return titleElement;
        }

        public Text addText(ContentContainer element, String value) {
            return addText(element, value, true);
        }

        public final Text addText(ContentContainer element, String value, boolean escape) {
            final Text text = element.addNewText();
            text.setStringValue(value);
            if (!escape) {
                text.setEscape(escape);
            }

            return text;
        }

        public final Text addCode(ContentContainer element, String value) {
            final Text text = element.addNewText();
            text.setMeta(DefaultMeta.Text.CODE);
            text.setStringValue(value);
            return text;
        }

        public final Text addMslId(ContentContainer element, Id bundleId, Id strId) {
            return addMslId(element, null, bundleId, strId);
        }
        
        public final Text addMslId(ContentContainer element, Definition mslOwnerDef, Id bundleId, Id strId) {
            final Text text = element.addNewText();
            text.setEscape(false);
            text.setMeta(DefaultMeta.Text.MLSID);
            String  mslIdString = "";
            if(mslOwnerDef != null && mslOwnerDef.getModule() != null) {
                mslIdString += mslOwnerDef.getModule().getLayer().getURI() + "/" + mslOwnerDef.getModule().getSegment().getName().toLowerCase() + "/" + mslOwnerDef.getModule().getName() + " ";
            }
            mslIdString += bundleId.toString() + " " + strId.toString();
            text.setStringValue(mslIdString);
            return text;
        }

        public final Text addLocalizedText(ContentContainer element, ERadixdocPhrase phrase) {
            final Text text = element.addNewText();
            text.setStringValue(page.getPhraseId(phrase));
            text.setMeta(DefaultMeta.Text.LOCALIZED);
            return text;
        }

        public final Block createChapter(ContentContainer container, String title) {
            return createChapter(container, title, 0);
        }

        public Block createChapter(ContentContainer container, String title, int level) {
            final Block chapter = container.addNewBlock();
            chapter.setStyle(DefaultStyle.CHAPTER);
            final Attribute ref = chapter.addNewAttribute();
            ref.setName(DefaultAttributes.TABLE_CONTENT_ITEM);
            ref.setStringValue(title);

            final Attribute levelAttr = chapter.addNewAttribute();
            levelAttr.setName(DefaultAttributes.TABLE_CONTENT_ITEM_LEVEL);
            levelAttr.setStringValue(String.valueOf(level));

            return chapter;
        }

        public final Ref addRef(ContentContainer root, RadixObject object, Definition context) {
            final Ref ref = root.addNewRef();
            ref.setPath(page.resolve(context, object));
            ref.setTitle(object.getQualifiedName(context));
            ref.addNewText().setStringValue(object.getName());

            return ref;
        }

        public final <T extends DocElement> T appendStyle(T elem, String... styles) {
            final StringBuilder sb = new StringBuilder();

            if (elem.isSetStyle() && elem.getStyle() != null) {
                sb.append(elem.getStyle()).append(" ");
            }

            boolean first = true;

            for (final String style : styles) {
                if (first) {
                    first = false;
                } else {
                    sb.append(" ");
                }

                sb.append(style);
            }

            elem.setStyle(sb.toString());
            return elem;
        }

        public final Text appendStyle(Text elem, String... styles) {
            final StringBuilder sb = new StringBuilder();

            if (elem.isSetStyle() && elem.getStyle() != null) {
                sb.append(elem.getStyle()).append(" ");
            }

            boolean first = true;

            for (final String style : styles) {
                if (first) {
                    first = false;
                } else {
                    sb.append(" ");
                }

                sb.append(style);
            }

            elem.setStyle(sb.toString());
            return elem;
        }
    }

    protected abstract class SummaryChapterFactory<T extends RadixObject, Y extends RadixObject> {

        private Block chapter;
        private T source;

        public SummaryChapterFactory(T source) {
            this.source = source;
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

        public final Block document(List<T> hierarchy) {
            final HashSet<Y> used = new HashSet<>();

            List<Y> apiElements = getApiElements(getAllElements(source), used);
            if (!apiElements.isEmpty()) {
                documentElements(getChapter(), source, apiElements, getElementTypeName());
            }

            Table inherited = null;
            for (final T c : hierarchy) {
                if (c != source) {
                    apiElements = getApiElements(getAllElements(c), used);
                    if (!apiElements.isEmpty()) {
                        if (inherited == null) {
                            inherited = getChapter().addNewTable();
                            prepareInheritTable(inherited);
                        }
                        documentElements(createInheritRow(inherited, c), c, apiElements, getElementTypeName());
                    }
                }
            }

            return getChapter(false);
        }

        protected void documentElements(ContentContainer container, T cls, List<Y> apiElements, String name) {
            final Table table = container.addNewTable();
            prepareElementsTable(cls, table);
            for (final Y elem : apiElements) {
                documentElementSummary(elem, table);
            }
        }

        protected ContentContainer createInheritRow(Table inherited, T cls) {
            final Table.Row row = inherited.addNewRow();
            final Table.Row.Cell cell = row.addNewCell();
            final Block collapsable = cell.addNewBlock();
            collapsable.setMeta(DefaultMeta.Block.COLLAPSIBLE);

            final Block titleBlock = collapsable.addNewBlock();
            titleBlock.setStyle(DefaultStyle.TITLE);
            titleBlock.setMeta(DefaultMeta.Text.TITLE);

            titleBlock.addNewText().setStringValue("From ");
            final Ref ref = titleBlock.addNewRef();
            ref.addNewText().setStringValue(cls.getQualifiedName(source));
            ref.setPath(resolve(source, cls));

            return collapsable;
        }

        protected boolean isApiElement(Y elem) {
            return true;
        }

        protected final List<Y> getApiElements(List<Y> all, Set<Y> used) {
            final List<Y> apiElements = new ArrayList<>();
            for (final Y elem : all) {
                if (isApiElement(elem) && !used.contains(elem)) {
                    apiElements.add(elem);
                    used.add(elem);
                }
            }
            if (apiElements.isEmpty()) {
                return Collections.EMPTY_LIST;
            }

            sort(apiElements);

            return apiElements;
        }

        protected void sort(List<Y> elements) {
            Collections.sort(elements, new Comparator<Y>() {
                @Override
                public int compare(Y o1, Y o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }

        protected void prepareInheritTable(Table table) {
            table.setStyle(DefaultStyle.MEMBERS);
        }

        protected abstract void prepareElementsTable(T cls, Table table);

        protected abstract void documentElementSummary(Y elem, Table table);

        protected abstract Block createChapter();

        protected abstract String getElementTypeName();

        protected abstract List<Y> getAllElements(T c);
    }
    private final DocumentOptions options;
    protected final T source;
    protected final Page page;
    private Set<String> resources;

    public RadixdocXmlPage(T source, Page page, DocumentOptions options) {
        this.resources = new HashSet<>();
        this.options = options;
        this.source = source;
        this.page = page;
    }

    @Override
    public String resolve(RadixObject from, RadixObject target) {
        return options.getResolver().resolve(from, target);
    }

    @Override
    public String getIdentifier(RadixObject target) {
        return options.getResolver().getIdentifier(target);
    }

    @Override
    public T getSource() {
        return source;
    }

    @Override
    public String getPhraseId(ERadixdocPhrase key) {
        return options.getDictionary().getPhraseId(key);
    }

    public DocumentOptions getOptions() {
        return options;
    }

    protected void addResource(String key, String path) {
        if (!resources.contains(key)) {

            final Page.ResourceItem resource = page.addNewResourceItem();

            resource.setKey(key);
            resource.setPath(path);

            resources.add(key);
        }
    }

    public void addResource(IResource resource) {
        addResource(resource.getKey(), resource.getPath());
    }

    @Override
    public final Page getRadixdocPage() {
        return page;
    }

    protected abstract PageWriter<T> getWriter();

    protected Block documentHead(Page page) {
        final Block headChapter = page.addNewBlock();
        headChapter.setStyle(DefaultStyle.HEAD);
        final Block subTitleBlock = headChapter.addNewBlock();
        subTitleBlock.setStyle(DefaultStyle.SUB_TITLE);
        subTitleBlock.addNewText().setStringValue("Module: ");
        final Ref moduleRef = subTitleBlock.addNewRef();
        moduleRef.setPath(resolve(source, source.getModule()));
        moduleRef.addNewText().setStringValue(source.getModule().getQualifiedName());
        if (source.getOwnerDefinition() != null && source.getOwnerDefinition() != source.getModule()) {
            final Block parentDefBlock = headChapter.addNewBlock();
            parentDefBlock.setStyle(DefaultStyle.SUB_TITLE);
            parentDefBlock.addNewText().setStringValue("Parent Definition: ");
            final Ref parentRef = parentDefBlock.addNewRef();
            parentRef.setPath(resolve(source.getOwnerDefinition().getModule(), source.getOwnerDefinition()));
            parentRef.addNewText().setStringValue(source.getOwnerDefinition().getQualifiedName());
        }
        final Block titleBlock = headChapter.addNewBlock();
        titleBlock.setStyle(DefaultStyle.TITLE);
        final Resource icon = titleBlock.addNewResource();
        final RadixIconResource iconResource = new RadixIconResource(source.getIcon());
        icon.setSource(iconResource.getKey());
        addResource(iconResource);
        final StringBuilder name = new StringBuilder();
        name.append(source.getTypeTitle()).append(' ').append(source.getName());
        getWriter().addStrTitle(titleBlock, name.toString());
        return headChapter;
    }
}
