/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.models.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import org.radixware.kernel.common.client.meta.editorpages.IEditorPagesHolder;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPages;
import org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ModelWithPages;
import org.radixware.kernel.common.types.Id;


public abstract class ModelItemInEditorPage extends ModelItem{
    
    public ModelItemInEditorPage(final Model owner, final Id id){
        super(owner, id);        
    }
    
    protected final List<EditorPageModelItem> findDependentEditorPages(){
        if (owner instanceof ModelWithPages && !inSelectorRow()){
            final ModelWithPages ownerModel = (ModelWithPages)owner;
            final Stack<Id>  pagesStack = new Stack<>();
            final List<EditorPageModelItem> dependentPages = new ArrayList<>();            
            final RadEditorPages editorPages = ((IEditorPagesHolder)owner.getDefinition()).getEditorPages();        
            for (Id pageId: editorPages.getAllPagesIds()){
                final RadEditorPageDef pageDef = editorPages.getPageById(pageId);
                if (pageDef instanceof RadStandardEditorPageDef
                    && registeredInEditorPage((RadStandardEditorPageDef)pageDef)){
                    pagesStack.push( pageDef.getId() );
                }
            }
            while (!pagesStack.isEmpty()){
                final Id pageId= pagesStack.pop();
                final EditorPageModelItem pageModel = ownerModel.getEditorPage(pageId);
                if (!dependentPages.contains(pageModel)){
                    dependentPages.add(pageModel);
                }
                for (EditorPageModelItem parentPage = pageModel.getParentPage(); parentPage!=null; parentPage=parentPage.getParentPage()){
                    if (!dependentPages.contains(parentPage)){
                        dependentPages.add(parentPage);
                    }
                }
                final RadEditorPageDef  parentPageDef = editorPages.getParentPageById(pageId);
                if (parentPageDef!=null){
                    pagesStack.push(parentPageDef.getId());
                }
            }
            return dependentPages;
        }else{
            return Collections.emptyList();
        }
    }    
    
    private boolean inSelectorRow() {
        return owner.getContext() instanceof IContext.SelectorRow;
    }
    
    private boolean registeredInEditorPage(final RadStandardEditorPageDef pageDef){
        final Collection<RadStandardEditorPageDef.PageItem> pageItems =
                pageDef.getRootPropertiesGroup().getPageItems();
        for (RadStandardEditorPageDef.PageItem item : pageItems) {
            if (getId().equals(item.getItemId())){
                return true;
            }
        }
        return false;
    }        

}
