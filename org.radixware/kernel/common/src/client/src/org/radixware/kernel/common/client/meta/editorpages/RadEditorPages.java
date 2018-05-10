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

package org.radixware.kernel.common.client.meta.editorpages;

import java.util.*;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.meta.IModelDefinition;
import org.radixware.kernel.common.types.Id;

public class RadEditorPages {

    public static final class PageOrder {

        final public int level;
        final public Id id;

        public PageOrder(final int level, final Id id) {
            this.level = level;
            this.id = id;
        }
    }
    private final Map<Id, RadEditorPageDef> pagesById;
    private final Map<Id,Id> parentPageIdByChildPageId = new HashMap<>();
    private final List<RadEditorPageDef> topLevelPages;
    private final PageOrder[] pageOrder;
    private final IEditorPagesHolder holder;

    public RadEditorPages(final PageOrder[] pageOrder, final IEditorPagesHolder owner) {
        topLevelPages = new ArrayList<>(pageOrder != null ? pageOrder.length : 0);
        this.pageOrder = pageOrder;
        holder = owner;
        if (pageOrder != null && pageOrder.length > 0) {
            pagesById = new LinkedHashMap<>(pageOrder.length * 2);
            linkPages(0, topLevelPages);
        } else {
            pagesById = new LinkedHashMap<>();
        }
    }

    private int linkPages(int index, List<RadEditorPageDef> pages) {
        if (pageOrder.length <= index) {
            return 0;
        }
        int result = 0;
        final int page_count = pageOrder.length,
                level = pageOrder[index].level;
        RadEditorPageDef parentPage, page;
        for (int i = index; i < page_count && pageOrder[i].level >= level; i++) {
            page = holder.findEditorPageById(pageOrder[i].id);
            if (page!=null){
                if ((i + 1) < page_count && pageOrder[i + 1].level > level) {
                    List<RadEditorPageDef> childPages = new ArrayList<>();
                    int count = linkPages(i + 1, childPages);
                    parentPage = page.createCopyWithSubPages(childPages);
                    for (RadEditorPageDef childPage: childPages){
                        parentPageIdByChildPageId.put(childPage.getId(), parentPage.getId());
                    }
                    pagesById.put(parentPage.getId(), parentPage);
                    pages.add(parentPage);
                    i += count;
                    result += count+1;
                } else if (pageOrder[i].level == level) {
                    pages.add(page);
                    pagesById.put(page.getId(), page);
                    result++;
                }
            }
        }
        return result;
    }

    public RadEditorPageDef getPageById(Id pageId) {
        if (pagesById.containsKey(pageId)) {
            return pagesById.get(pageId);
        }
        throw new NoDefinitionWithSuchIdError((IModelDefinition) holder, NoDefinitionWithSuchIdError.SubDefinitionType.EDITOR_PAGE, pageId);
    }
    
    public RadEditorPageDef getParentPageById(final Id pageId){
        final Id parentPageId = parentPageIdByChildPageId.get(pageId);
        return parentPageId==null ? null : getPageById(parentPageId);
    }

    public RadEditorPageDef findPageByName(String pageName) {
        for (Map.Entry<Id, RadEditorPageDef> entry : pagesById.entrySet()) {
            if (entry.getValue().getName().equals(pageName)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public boolean isEditorPageExists(Id pageId) {
        return pagesById.containsKey(pageId);
    }

    public List<RadEditorPageDef> getTopLevelPages() {
        return Collections.unmodifiableList(topLevelPages);
    }

    public Collection<Id> getAllPagesIds() {
        return pagesById.keySet();
    }

    public boolean isPropertyDefined(final Id propertyId) {
        RadStandardEditorPageDef stdPage;
        RadCustomEditorPageDef cstPage;
        for (RadEditorPageDef page : pagesById.values()) {
            if (page instanceof RadStandardEditorPageDef) {
                stdPage = (RadStandardEditorPageDef) page;
                if (stdPage.isPropertyDefined(propertyId)) {
                    return true;
                }
            } else if (page instanceof RadCustomEditorPageDef) {
                cstPage = (RadCustomEditorPageDef) page;
                if (cstPage.getPropertyIds().contains(propertyId)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public Collection<Id> findPagesWithProperty(final Id propertyId){
        final List<Id> pages = new LinkedList<>();
        RadStandardEditorPageDef stdPage;
        RadCustomEditorPageDef cstPage;
        for (RadEditorPageDef page : pagesById.values()) {
            if (page instanceof RadStandardEditorPageDef) {
                stdPage = (RadStandardEditorPageDef) page;
                if (stdPage.isPropertyDefined(propertyId)) {
                    pages.add(page.getId());
                }
            } else if (page instanceof RadCustomEditorPageDef) {
                cstPage = (RadCustomEditorPageDef) page;
                if (cstPage.getPropertyIds().contains(propertyId)) {
                    pages.add(page.getId());
                }
            }
        }
        return pages;
    }
}
