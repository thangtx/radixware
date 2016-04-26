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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.views.IExplorerView;
import org.radixware.kernel.explorer.views.StandardEditorPage;


public class ChildPagesWidget extends QGroupBox implements IExplorerModelWidget{
    
    private final static int CONTENTS_MARGIN = 6;
    
    private final IClientEnvironment environment;
    private final List<EditorPageModelItem> pages = new LinkedList<>();
    private final IExplorerModelWidget widget;  
    private final QSize size = new QSize();
    private final StandardEditorPage ownerWidget;
    private boolean wasBinded;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public ChildPagesWidget(final IClientEnvironment environment, final StandardEditorPage parent, final IExplorerView parentView, final List<EditorPageModelItem> editorPageModelItems, final String name){
        super(parent);
        ownerWidget = parent;
        if (editorPageModelItems==null || editorPageModelItems.isEmpty()){
            throw new IllegalArgumentException("No editor pages defined");
        }
        this.environment = environment;
        pages.addAll(editorPageModelItems);
        if (pages.size()>1){
            widget = new TabSet(environment, parentView, pages, name);
            widget.asQWidget().setParent(this);
        }else {
            widget = new EditorPage(pages.get(0));
            widget.asQWidget().setParent(this);
        }
        final QLayout layout = new QVBoxLayout(this);
        layout.addWidget(widget.asQWidget());
    }        

    @Override
    public void refresh(final ModelItem modelItem) {        
        final int numberOfVisiblePages = getNumberOfVisiblePages();
        setFlat(numberOfVisiblePages>1);
        if (widget instanceof TabSet){
            ((TabSet)widget).setTabBarVisible(numberOfVisiblePages>1);
        }        
        if (numberOfVisiblePages==1){
            final EditorPageModelItem page = findFirstVisibleEditorPage();            
            setTitle(page.getTitle());
            layout().setContentsMargins(CONTENTS_MARGIN, CONTENTS_MARGIN, CONTENTS_MARGIN, CONTENTS_MARGIN);            
        }else{
            setTitle(null);
            layout().setContentsMargins(0, 0, 0, 0);
        }
        if (numberOfVisiblePages>0 && isHidden()){
            setHidden(false);
            final QWidget ownerPageWidget = ownerWidget.getWidget()==null ? null : (QWidget)ownerWidget.getWidget();            
            if (ownerPageWidget!=null){
                ownerPageWidget.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed);
            }
            ownerWidget.updateGeometry();
        }else if (numberOfVisiblePages==0 && !isHidden()){
            setHidden(true);
            final QWidget ownerPageWidget = ownerWidget.getWidget()==null ? null : (QWidget)ownerWidget.getWidget();
            if (ownerPageWidget!=null){
                ownerPageWidget.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
            }
            ownerWidget.updateGeometry();            
        }
    }
    
    @Override
    public boolean setFocus(final Property property) {
        return widget==null ? false: widget.setFocus(property);
    }

    @Override
    public void bind() {
        if (widget!=null){            
            widget.bind();
            applySettings();
            Application.getInstance().getActions().settingsChanged.connect(this, "applySettings()");
            refresh(null);
            subscibe();
            wasBinded = true;
        }
    }

    @Override
    public QWidget asQWidget() {
        return this;
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
    
    private void applySettings() {
        //применение настроек размеров значков и шрифта
        final ClientSettings settings = environment.getConfigStore();
        final QFont tabFont;
        
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.EDITOR_GROUP);
        settings.beginGroup(SettingNames.Editor.COMMON_GROUP);
        try{
            tabFont = 
                ExplorerSettings.getQFont(settings.readString(SettingNames.Editor.Common.FONT_IN_TABS));
        }finally{
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
        setFont(tabFont);
    }   

    @Override
    protected void paintEvent(final QPaintEvent event) {
        if ((title()!=null && !title().isEmpty()) || !isFlat() || isCheckable()){
            super.paintEvent(event);
        }
    }        

    @Override
    protected void closeEvent(final QCloseEvent event) {
        if (widget!=null){
            Application.getInstance().getActions().settingsChanged.disconnect(this);
            unsubscibe();            
            widget.asQWidget().close();
        }
        super.closeEvent(event);        
    }

    @Override
    public QSize minimumSizeHint() {        
        if (wasBinded){
            final int numberOfVisiblePages = getNumberOfVisiblePages();
            if (numberOfVisiblePages>1){
                return widget.asQWidget().minimumSizeHint();
            }else if (numberOfVisiblePages==1){
                final QSize widgetSizeHint = widget.asQWidget().minimumSizeHint();            
                final QSize defaultSizeHint = super.minimumSizeHint();
                int height = defaultSizeHint.height() + widgetSizeHint.height() + CONTENTS_MARGIN*2;
                int width = widgetSizeHint.width() + CONTENTS_MARGIN*2;
                size.setHeight(height);            
                size.setWidth(Math.max(defaultSizeHint.width(), width));
                return size;               
            }
        }
        return super.minimumSizeHint();
    }

    @Override
    public QSize sizeHint() {
        if (wasBinded){
            final int numberOfVisiblePages = getNumberOfVisiblePages();
            if (numberOfVisiblePages>1){
                return widget.asQWidget().sizeHint();
            }else if (numberOfVisiblePages==1){
                final QSize widgetSizeHint = widget.asQWidget().sizeHint();            
                final QSize defaultSizeHint = super.minimumSizeHint();//size hint for title
                int height = defaultSizeHint.height() + widgetSizeHint.height() + CONTENTS_MARGIN*2;
                int width = widgetSizeHint.width() + CONTENTS_MARGIN*2;
                size.setHeight(height);            
                size.setWidth(Math.max(defaultSizeHint.width(), width));
                return size;               
            }         
        }
        return super.sizeHint();        
    }    
}
