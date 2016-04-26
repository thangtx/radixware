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

import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.radixdoc.DefaultAttributes;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.schemas.radixdoc.ElementList;
import org.radixware.schemas.radixdoc.Ref;
import org.radixware.schemas.radixdoc.Resource;
import org.radixware.schemas.radixdoc.Text;


public class DomainRadixdoc extends AdsDefinitionRadixdoc<AdsDomainDef> {

    public DomainRadixdoc(AdsDomainDef source, Page page, DocumentOptions options) {
        super(source, page, options);
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
        name.append(source.getTypeTitle()).append(' ').append(source.getName());
        Text nameDomain = getWriter().addStrTitle(titleBlock, name.toString());
        if (source.isDeprecated()) {
            nameDomain.setStyle(DefaultStyle.DEPRECATED);
        } else {
            nameDomain.setStyle(DefaultStyle.TITLE);
        }
        return headChapter;
    }

    private void writeChildsDefsInTitle(ElementList list, AdsDomainDef parentDef) {

        for (AdsDomainDef child : parentDef.getChildDomains().list()) {
            if (child.isPublished()) {

                final ElementList.Item item = list.addNewItem();
                final Block childsDefChapter = item.addNewBlock();
                childsDefChapter.setStyle(DefaultStyle.HEAD);
                final Block titleChildsDefBlock = childsDefChapter.addNewBlock();

                if (child.isDeprecated()) {
                    titleChildsDefBlock.setStyle(DefaultStyle.DEPRECATED);
                } else {
                    titleChildsDefBlock.setStyle(DefaultStyle.TREE_TITLE);
                }

                Ref nameChildRef = titleChildsDefBlock.addNewRef();
                nameChildRef.setPath(resolve(parentDef.getChildDomains(), child));

                getWriter().addStrTitle(nameChildRef, child.getName());

                if (!child.getChildDomains().isEmpty()) {
                    writeChildsDefsInTitle(item.addNewList(), child);
                }
            }
        }
    }

    @Override
    protected void documentDescriptionExtensions(ContentContainer container) {

        final Block structureBlock = container.addNewBlock();

        structureBlock.setStyle(DefaultStyle.CHAPTER);
        getWriter().addStrTitle(structureBlock, "Domain structure");

        ElementList parentList = structureBlock.addNewList();
        final Block parentNameBlock = parentList.addNewItem().addNewBlock();
        parentNameBlock.setStyle(DefaultStyle.DOMAIN_STRUCTURE);

        Ref nameDomain = parentNameBlock.addNewRef();
        nameDomain.setPath(resolve(source, source));
        getWriter().addStrTitle(nameDomain, source.getName());
        if (source.isDeprecated()) {
            nameDomain.setStyle(DefaultStyle.DEPRECATED);
        } else {
            nameDomain.setStyle(DefaultStyle.DOMAIN_TITLE);
        }

        if (!source.getChildDomains().list().isEmpty()) {
            final ElementList childList = parentNameBlock.addNewList();
            parentList.setStyle(DefaultStyle.TREE_TITLE);
            writeChildsDefsInTitle(childList, source);
        }

        final Block parentDescriptionBlock = container.addNewBlock();
        parentDescriptionBlock.setStyle(DefaultStyle.CHAPTER);
        getWriter().addStrTitle(parentDescriptionBlock, source.getName());
        getWriter().setAttribute(parentDescriptionBlock, DefaultAttributes.ANCHOR, source.getId().toString());
        if (source.isFinal()) {
            final Block deprecatedBlock = parentDescriptionBlock.addNewBlock();
            deprecatedBlock.setStyle(DefaultStyle.DOMAIN_DESCRIPTION);
            getWriter().addStrTitle(deprecatedBlock, "Domain is final");
        }

        final Block titleDescription = parentDescriptionBlock.addNewBlock();
        titleDescription.setStyle(DefaultStyle.DOMAIN_DESCRIPTION);
        final StringBuilder parentDetail = new StringBuilder();
        if (source.getTitle(EIsoLanguage.ENGLISH) != null) {
            parentDetail.append("Title: ").append(source.getTitle(EIsoLanguage.ENGLISH));
            getWriter().addStrTitle(titleDescription, parentDetail.toString());
        } else {
            parentDetail.append("Title: <Not defined>");
            getWriter().addStrTitle(titleDescription, parentDetail.toString());
        }
        if (!source.getChildDomains().list().isEmpty()) {
            childDomainDescription(container, source);
        }
    }

    protected void childDomainDescription(ContentContainer container, AdsDomainDef parentDef) {

        for (AdsDomainDef child : parentDef.getChildDomains().list()) {
            if (child.isPublished()) {
                final Block childDescriptionBlock = container.addNewBlock();
                getWriter().setAttribute(childDescriptionBlock, DefaultAttributes.ANCHOR, child.getId().toString());
                childDescriptionBlock.setStyle(DefaultStyle.CHAPTER);
                getWriter().addStrTitle(childDescriptionBlock, child.getName());
                childDomainDescription(container, child);

                if (child.isFinal()) {
                    final Block deprecatedBlock = childDescriptionBlock.addNewBlock();
                    deprecatedBlock.setStyle(DefaultStyle.DOMAIN_DESCRIPTION);
                    getWriter().addStrTitle(deprecatedBlock, "Domain is final");
                }

                final Block titleDescription = childDescriptionBlock.addNewBlock();
                titleDescription.setStyle(DefaultStyle.DOMAIN_DESCRIPTION);
                final StringBuilder detailBlock = new StringBuilder();
                if (child.getTitle(EIsoLanguage.ENGLISH) != null) {
                    detailBlock.append("Title: ").append(child.getTitle(EIsoLanguage.ENGLISH));
                    getWriter().addStrTitle(titleDescription, detailBlock.toString());
                } else {
                    detailBlock.append("Title: <Not defined> ");
                    getWriter().addStrTitle(titleDescription, detailBlock.toString());
                }
            }
        }
    }
}
