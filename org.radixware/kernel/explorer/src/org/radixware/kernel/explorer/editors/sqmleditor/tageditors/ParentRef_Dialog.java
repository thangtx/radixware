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

package org.radixware.kernel.explorer.editors.sqmleditor.tageditors;

import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReference;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class ParentRef_Dialog extends ExplorerDialog {

    private final QTreeWidget tree = new QTreeWidget(this);

    public ParentRef_Dialog(final IClientEnvironment environment, final QWidget w, final List<ISqmlDefinition> defs, final EDefinitionDisplayMode displayMode) {
        super(environment, w, environment.getMessageProvider().translate("SqmlEditor", "Parent Property")/*"ParentRef Dialog"*/);
        this.setWindowTitle(environment.getMessageProvider().translate("SqmlEditor", "Parent Property"));//������������ ��������
        createUI(defs, displayMode);
    }

    private void createUI(final List<ISqmlDefinition> defs, final EDefinitionDisplayMode displayMode) {
        tree.setObjectName("tree");
        fillTree(defs, displayMode);
        tree.setHeaderHidden(true);

        dialogLayout().addWidget(tree);
        addButtons(EnumSet.of(EDialogButtonType.CLOSE), true);
    }

    private void fillTree(final List<ISqmlDefinition> defs, final EDefinitionDisplayMode displayMode) {
        if (defs != null && !defs.isEmpty()) {
            QTreeWidgetItem item = null;
            for (ISqmlDefinition definition : defs) {
                item = item == null ? new QTreeWidgetItem(tree) : new QTreeWidgetItem(item);
                item.setIcon(0, icon(definition));
                item.setText(0, text(definition, displayMode));
                item.setExpanded(true);
            }
        }
    }

    private static QIcon icon(final ISqmlDefinition item) {
        if (item instanceof ISqmlTableReference) {
            final ISqmlTableDef tableDef = ((ISqmlTableReference) item).findReferencedTable();
            return tableDef.getIcon() != null ? ExplorerIcon.getQIcon(tableDef.getIcon()) : null;
        } else {
            return item.getIcon() != null ? ExplorerIcon.getQIcon(item.getIcon()) : null;
        }
    }

    private static String text(final ISqmlDefinition item, final EDefinitionDisplayMode showMode) {
        if (item instanceof ISqmlTableDef) {
            if (showMode == EDefinitionDisplayMode.SHOW_TITLES) {
                return item.getTitle();
            } else {
                return ((ISqmlTableDef) item).hasAlias() ? ((ISqmlTableDef) item).getAlias() : item.getDisplayableText(showMode);
            }
        } else if (item instanceof ISqmlTableReference) {
            final ISqmlTableDef tableDef = ((ISqmlTableReference) item).findReferencedTable();
            return tableDef.getDisplayableText(showMode);
        } else if (item instanceof ISqmlColumnDef) {
            return showMode == EDefinitionDisplayMode.SHOW_TITLES ? item.getTitle() : item.getShortName();
        } else {
            return item.getDisplayableText(showMode);
        }
    }
    /*private void fillTree(EDefinitionDisplayMode displayMode){
    if((items!=null)&&(items.size()>0)){
    QTreeWidgetItem parentItem=new QTreeWidgetItem(tree);
    parentItem.setIcon(0, icon(items.get(0)));
    parentItem.setText(0, text(items.get(0),displayMode));
    parentItem.setExpanded(true);
    for(int i=1,size=items.size();i<size;i++){
    QTreeWidgetItem item=new QTreeWidgetItem(parentItem);
    item.setText(0, text(items.get(i),displayMode));
    item.setIcon(0, icon(items.get(i)));
    item.setExpanded(true);
    parentItem=item;
    }
    }
    }

    private QIcon icon(Object item){
    if (item instanceof RadReferenceDef){
    RadReferenceDef ref = (RadReferenceDef)item;
    try{
    return  ref.getReferencedClassDef().getIcon();
    }
    catch (DefinitionError ex){
    return new QIcon("classpath:images/table.svg");
    }
    }else if(item instanceof RadPropertyDef){
    return ExplorerIcon.Definitions.PROPERTY.getQIcon();
    }else{
    return ExplorerIcon.Definitions.TABLE.getQIcon();
    }
    }

    private String text(Object item,EDefinitionDisplayMode showMode){
    RadClassPresentationDef classDef;
    if (item instanceof RadReferenceDef){
    RadReferenceDef ref = (RadReferenceDef)item;
    classDef=ref.getReferencedClassDef();
    //String name=classDef.getName()==null ? ("#"+ classDef.getId()) : classDef.getName();
    return //getDisplaiedName(name,classDef.getGroupTitle(),classDef.hasGroupTitle(),showMode);
    }else if(item instanceof RadClassPresentationDef){
    classDef = (RadClassPresentationDef)item;
    String name=classDef.getName()==null ? ("#"+ classDef.getId()) : classDef.getName();
    return getDisplaiedName(name,classDef.getGroupTitle(),classDef.hasGroupTitle(),showMode);
    }else if(item instanceof RadPropertyDef){
    RadPropertyDef prop=(RadPropertyDef)item;
    String name=prop.getName()==null ? ("#"+ prop.getId()) : prop.getName();
    return getDisplaiedName(name,prop.getTitle(),prop.hasTitle(),showMode);
    }
    return "<???>";
    }

    private String getDisplaiedName(String name,String title,boolean hasTitle,EDefinitionDisplayMode displayMode){
    if(displayMode==EDefinitionDisplayMode.SHOW_TITLES){
    return hasTitle ? title : name;
    }else if((displayMode==EDefinitionDisplayMode.SHOW_SHORT_NAMES)&&(name.indexOf("::")!=-1)){
    return TagInfo.getNameWithoutModule(name);
    }
    return name;
    //return
    }*/

    /*private String getDisplNameWithoutModul(String name){
    String res=name;
    int start=0;
    if((start=name.lastIndexOf("::"))!=-1){
    String s=name.substring(0,start);
    int n=s.lastIndexOf(" ")==-1 ? 0:  s.lastIndexOf(" ")+1;
    res=name.substring(0,n)+name.substring(start+2,name.length());
    }
    return res;
    }*/
}