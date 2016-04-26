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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.client.enums.ETextDecoration;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerFont;


public class ChooseDomainPanel extends ChooseDefinitionPanel{
    private QTreeWidget tree;
    
    ChooseDomainPanel(final IChooseDefFromList parent, final Collection<AdsUserFuncDef.Lookup.DefInfo> allowedDefinitions, final AdsUserFuncDef userFunc, final Set<EDefType> templList, final boolean isDbEntity,final boolean isParentRef){    
        super( parent, allowedDefinitions, userFunc, templList, isDbEntity, isParentRef);     
        setFilteredComponent(tree);
        this.layout().addWidget(tree);
    }
    
    @Override
    protected void createList(){
        tree=new QTreeWidget(this);
        tree.setHeaderHidden(true);
        //changed by yremizov:
        //old code:
        /*
        HtmlItemDelegate delegate = new HtmlItemDelegate();
        tree.setItemDelegateForColumn(0, delegate);
        */
        //new code:
        //tree.setItemDelegateForColumn(0, new SqmlDefinitionsTreeItemDelegate());
        //end of changes
        tree.setObjectName("ChooseList");
        tree.itemClicked.connect(this, "onItemClick(QTreeWidgetItem,Integer)");
        tree.itemActivated.connect(this, "onItemClick(QTreeWidgetItem,Integer)");
        tree.itemDoubleClicked.connect(this, "onItemDoubleClick(QTreeWidgetItem,Integer)");
        tree.setIconSize(new QSize(22,22));
        tree.setAlternatingRowColors(true);
        tree.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOn);

        tree.setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);
        tree.setForegroundRole(QPalette.ColorRole.NoRole);
        tree.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);

        if(tree.model().index(0, 0)!=null){
            tree.setCurrentIndex(tree.model().index(0, 0));
            tree.clicked.emit(tree.currentIndex());
        }
        tree.setVisible(false);
    }
    
    @SuppressWarnings("unused")
    private void onItemClick(final QTreeWidgetItem item,final Integer column){
        if(item instanceof DomainItem){
            final AdsDefinition def=((DomainItem)item).getDomen();
            parent.setSelectedDefinition( def);
        }        
    }

    @SuppressWarnings("unused")
    private void onItemDoubleClick(final QTreeWidgetItem item,final Integer column){
         if(item instanceof DomainItem){
            parent.onItemDoubleClick(tree.currentIndex());
         }
    }
    
    public AdsDefinition getSelectedDomain() {
        if (tree.currentItem() instanceof DomainItem) {
            final AdsDefinition def = ((DomainItem) tree.currentItem()).getDomen();
            return def;
        }
        return null;
    }

    @Override
    protected void fillList(final Collection<AdsUserFuncDef.Lookup.DefInfo> allowedDefinitions) {
        setVisibleForList(true);
        if (allowedDefinitions != null) {
            update(allowedDefinitions);
        }
        this.setEnabled(true);
        setFocusToFilter();
    }
    
    private void update(final Collection<AdsUserFuncDef.Lookup.DefInfo> allowedDefinitions){
        final List<QTreeWidgetItem> list=new ArrayList<>();
        for(AdsUserFuncDef.Lookup.DefInfo def:allowedDefinitions){
            final AdsDefinition adsdef=AdsUserFuncDef.Lookup.findTopLevelDefinition(userFunc, def.getPath()[0]);            
            if(adsdef!=null && (adsdef instanceof AdsDomainDef)  ){
                final AdsDomainDef domain=(AdsDomainDef)adsdef;
                final DomainItem item=createDomainItem(domain,def.getModuleName()+"::"+def.getName());                
                list.add(item);
                if(!((AdsDomainDef)adsdef).getChildDomains().isEmpty()){
                    collectChildDomain(((AdsDomainDef)adsdef).getChildDomains(),item);
                }
            }
        }
        tree.addTopLevelItems(list);
    }
    
    private void collectChildDomain(final Definitions<AdsDomainDef> childDomain,final QTreeWidgetItem item){
        for(AdsDomainDef domain:childDomain){
            final DomainItem childitem=createDomainItem( domain, domain.getName());            
            item.addChild(childitem);
            if(!(domain.getChildDomains().isEmpty())){
                collectChildDomain(domain.getChildDomains(),childitem);
            }
        }        
    }
    
    private DomainItem createDomainItem(final AdsDomainDef domain,final String text){
        final DomainItem childitem=new DomainItem(domain);
        childitem.setIcon(0, ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_DOMAIN));
        //changed by yremizov
        //old code:
        /*
        String text=domain.getName();
        if(domain.isDeprecated()){
            text="<s>"+text+"</s>";
        }
        childitem.setText(0,text);
        */
        //new code:
        childitem.setText(0,text);
        if(domain.isDeprecated()){
            final ExplorerFont strikeOut = 
                ExplorerFont.Factory.getFont(childitem.font(0)).changeDecoration(EnumSet.of(ETextDecoration.STRIKE_OUT));
            childitem.setFont(0, strikeOut.getQFont());
            //childitem.setData(0, SqmlDefinitionsTreeItemDelegate.SELECTED_ITEM_FOREGROUND_ROLE, QColor.red);
        }
        //end of changes
        return childitem;
    }
    
    @Override
    protected void setVisibleForList(final boolean visible){
        super.setVisibleForList(visible);
        tree.setVisible(visible);
    }
    
    class DomainItem extends QTreeWidgetItem{
        private final AdsDomainDef domen;
        
        DomainItem(final AdsDomainDef domen){
            super();
            this.domen=domen;
        }
        
        AdsDomainDef getDomen(){
            return domen;
        }
    }
    
}
