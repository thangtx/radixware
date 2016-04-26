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

package org.radixware.wps.views.editor;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.GroupBox;
import org.radixware.wps.rwt.UIObject;


public class ChildPagesWidget extends GroupBox implements IModelWidget{    
    
    private final List<EditorPageModelItem> pages = new LinkedList<>();
    private final IModelWidget widget;
    private final StandardEditorPage ownerWidget;
    
    public ChildPagesWidget(final WpsEnvironment env, final StandardEditorPage parent, final IView parentView, final List<EditorPageModelItem> editorPageModelItems, final Id ownerPageId){
        ownerWidget = parent;
        if (editorPageModelItems==null || editorPageModelItems.isEmpty()){
            throw new IllegalArgumentException("No editor pages defined");
        }
        pages.addAll(editorPageModelItems);
        if (pages.size()>1){
            widget = new TabSet(env, parentView, pages, ownerPageId);
        }else {
            widget = new EditorPage(pages.get(0));
        }
        add((UIObject)widget);
        setBorderBoxSizingEnabled(true);
    }

    @Override
    public void refresh(final ModelItem modelItem) {
        final int numberOfVisiblePages = getNumberOfVisiblePages();     
        if (numberOfVisiblePages==1){
            final EditorPageModelItem page = findFirstVisibleEditorPage();            
            setTitle(page.getTitle());
        }else{
            setTitle(null);
        }
        setFrameVisible(numberOfVisiblePages==1);
        if (numberOfVisiblePages>0 && !isVisible()){
            setVisible(true);
        }else if (numberOfVisiblePages==0 && isVisible()){
            setVisible(true);
        }
        ownerWidget.updateGeometry();
    }    

    @Override
    public boolean setFocus(Property property) {
        return widget==null ? false: widget.setFocus(property);
    }

    @Override
    public void bind() {
        if (widget!=null){            
            widget.bind();
            refresh(null);
            subscibe();
        }
    }
    
    public void finishEdit(){
        if (widget instanceof TabSet) {
            ((TabSet) widget).finishEdit();
        } else if (widget instanceof EditorPage) {
            ((EditorPage) widget).finishEdit();
        }
    }
    
    public void reread(){
        if (widget instanceof TabSet) {
            ((TabSet) widget).reread();
        } else if (widget instanceof EditorPage) {
            ((EditorPage) widget).reread();
        }        
    }
    
    public void close(){        
        unsubscibe();
        if (widget instanceof TabSet) {
            ((TabSet) widget).close();
        } else if (widget instanceof EditorPage) {
            ((EditorPage) widget).close();
        }    
    }
    
    private int getNumberOfVisiblePages(){
        int result = 0;
        for (EditorPageModelItem page: pages){
            if (page.isVisible()){
                result++;
            }
        }
        return result;
    }
    
    private void subscibe(){
        for (EditorPageModelItem page: pages){
            page.subscribe(this);
        }        
    }
    
    private void unsubscibe(){
        for (EditorPageModelItem page: pages){
            page.subscribe(this);
        }        
    }    
    
    private EditorPageModelItem findFirstVisibleEditorPage(){
        for (EditorPageModelItem page: pages){
            if (page.isVisible()){
                return page;
            }
        }
        return null;
    }

}
