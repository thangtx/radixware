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

package org.radixware.kernel.common.client.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.editorpages.IEditorPagesHolder;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.types.Id;


public abstract class ModelWithPages extends Model{
    
    protected ModelWithPages(final IClientEnvironment environment, final IEditorPagesHolder definition){
        super(environment,definition);
    }
    
    private Map<Id, EditorPageModelItem> editorPages = null;
    private Collection<Id> unrestrictedEditorPages;
    
    @Override
    public IEditorPagesHolder getDefinition() {
        return (IEditorPagesHolder)super.getDefinition();
    }

    public final EditorPageModelItem getEditorPage(Id id) {
        if (editorPages == null) {
            editorPages = new HashMap<>();
        }
        EditorPageModelItem page = editorPages.get(id);
        if (page == null) {
            RadEditorPageDef pageDef = getDefinition().getEditorPages().getPageById(id);
            page = pageDef.newModelItem(this);
            editorPages.put(id, page);
            if (isEditorPageRestrictedByServer(id)){
                page.setRestricted(true);
                final String traceMessage = 
                    getEnvironment().getMessageProvider().translate("TraceMessage", "Displaying of editor page '%s' #%s was restricted by server");
                getEnvironment().getTracer().debug(String.format(traceMessage, page.getTitle(), page.getId().toString()));
            }
        }
        return page;
    }
    
    void updateServerEditorPageRestrictions(final RawEntityModelData.EnabledEditorPages enabledEditorPages){
        if (enabledEditorPages.isAllPagesEnabled() && unrestrictedEditorPages!=null){
            unrestrictedEditorPages = null;
            if (editorPages!=null){
                final Collection<EditorPageModelItem> pages = new LinkedList<>(editorPages.values());
                for (EditorPageModelItem page: pages){
                    page.setRestricted(false);
                }
            }
        }else if (!enabledEditorPages.isAllPagesEnabled()){
            if (unrestrictedEditorPages==null){
                unrestrictedEditorPages = new LinkedList<>();
            }else{
                unrestrictedEditorPages.clear();
            }
            unrestrictedEditorPages.addAll(enabledEditorPages.getEnabledPages());
            if (editorPages!=null){
                final Collection<EditorPageModelItem> pages = new LinkedList<>(editorPages.values());
                boolean isRestricted;
                for (EditorPageModelItem page: pages){
                    isRestricted = !unrestrictedEditorPages.contains(page.getId());
                    if (isRestricted){
                        final String traceMessage = 
                            getEnvironment().getMessageProvider().translate("TraceMessage", "Displaying of editor page '%s' #%s was restricted by server");
                        getEnvironment().getTracer().debug(String.format(traceMessage, page.getTitle(), page.getId().toString()));                        
                    }
                    page.setRestricted(isRestricted);
                }
            }
        }
    }
    
    private boolean isEditorPageRestrictedByServer(final Id pageId){
        return unrestrictedEditorPages!=null && !unrestrictedEditorPages.contains(pageId);
    }

    public final boolean isEditorPageExists(final Id pageId) {
        return (editorPages != null && editorPages.containsKey(pageId)) || getDefinition().getEditorPages().isEditorPageExists(pageId);
    }
    
    protected final void cleanPages(){
        if (editorPages != null) {
            for (Entry<Id, EditorPageModelItem> entry : editorPages.entrySet()) {
                entry.getValue().unsubscribeAll();
            }
            editorPages.clear();
        }
    }
    
    public void afterOpenEditorPageView(final Id pageId){
    }
    
    public void afterActivateEditorPage(final Id pageId){
    }
    
    void copyServerEditorPageRestrictions(final ModelWithPages source){
        if (source.unrestrictedEditorPages==null){
            unrestrictedEditorPages = null;
        }else{
            unrestrictedEditorPages = new LinkedList<>(source.unrestrictedEditorPages);
        }
    }
    
    @Override
    public void clean() {
        super.clean();
        unrestrictedEditorPages = null;
        cleanPages();        
    }
}