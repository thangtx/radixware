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
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.editors.jmleditor.completer.HtmlCompletionItem;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class ListModelForRadixObj extends BaseModelList {

    private List<RadixObject> defList;

    public ListModelForRadixObj(final QListView ownerList) {
        this.defList = new ArrayList<>();
        this.ownerList = ownerList;
    }

    public void setDefList(final List<RadixObject> defList) {
        this.defList = defList;
        this.dataChanged.emit(index(0, 0), index(rowCount(null) - 1, 0));
        this.reset();
        Collections.sort(this.defList, new RadixObjectComparator());
    }

    public List<RadixObject> getDefList() {
        return defList;
    }

    @Override
    public int rowCount(final QModelIndex arg0) {
        return defList.size();
    }

    @Override
    public Object data(final QModelIndex index, final int role) {
        if (index != null) {
            if (role == Qt.ItemDataRole.DisplayRole) {
                String moduleName = "";
                if (defList.get(index.row()).getModule() != null) {
                    moduleName = defList.get(index.row()).getModule().getQualifiedName();
                }
                final int scrollWidth = ownerList.verticalScrollBar().isVisible() ? ownerList.verticalScrollBar().size().width() : 0;
                final int width = ownerList.childrenRect().width() - ownerList.iconSize().width() - scrollWidth - 10;

                final QFont font = ownerList.font();
                final QFontMetrics fontMetrics = new QFontMetrics(font);
                font.setBold(true);
                final QFontMetrics boldMetrics = new QFontMetrics(font);
                font.setBold(false);
                if (defList.get(index.row()) instanceof AdsMethodDef) {
                    final AdsMethodDef m = (AdsMethodDef) defList.get(index.row());
                    final HtmlCompletionItem item = new HtmlCompletionItem(m.getProfile().getName(), moduleName, fontMetrics, boldMetrics,m.isDeprecated());
                    return item.getText(width,false);
                    //return HtmlItemCreator.createTable(m.getProfile().getName(), moduleName, ownerList.font(), width);// m.getProfile().getProfileString();
                } else if(defList.get(index.row()) instanceof Definition){
                    final Definition definition=(Definition)defList.get(index.row());                    
                    final HtmlCompletionItem item = new HtmlCompletionItem(definition.getName(), moduleName, fontMetrics, boldMetrics,definition.isDeprecated());
                    return item.getText(width,false);
                    //return HtmlItemCreator.createTable(defList.get(index.row()).getName(), moduleName, ownerList.font(), width);
                }else if(defList.get(index.row()) instanceof AdsTypeDeclaration){
                    final AdsTypeDeclaration typeDeclar=(AdsTypeDeclaration)defList.get(index.row());
                    final HtmlCompletionItem item = new HtmlCompletionItem(typeDeclar.getName(), moduleName, fontMetrics, boldMetrics,false/*typeDeclar.isDeprecated()*/);
                    return item.getText(width,false);
                }
            } else if ((role == Qt.ItemDataRole.DecorationRole) &&(index.column() == 0) ) {
                 return getIcon(defList.get(index.row()));
            }
        }
        return null;
    }

    private QIcon getIcon(final RadixObject def) {
        //QIcon icon=null;
        if (def instanceof AdsClassDef) {
            return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_CLASS);
        } else if (def instanceof AdsMethodDef) {
            return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_METHOD);
        } else if (def instanceof AdsPropertyDef) {
            return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_PROP);
        } else if ((def instanceof AdsEnumDef) || (def instanceof AdsEnumItemDef)) {
            final EValType type = (def instanceof AdsEnumDef) ? ((AdsEnumDef) def).getItemType() : ((AdsEnumItemDef) def).getOwnerEnum().getItemType();
            switch (type) {
                case INT:
                    return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_CONSTSET_INT);
                //break;
                case STR:
                    return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_CONSTSET_STR);
                //break;
                default:
                    return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.Definitions.CONSTSET);
            }
        } else if ((def instanceof AdsXmlSchemeDef) || (def instanceof AdsTypeDeclaration)) {
            return ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_XML);
        }
        return null;
    }

    static class RadixObjectComparator implements Comparator<RadixObject> {

        @Override
        public int compare(final RadixObject o1, final RadixObject o2) {
            if (o1.getName() != null && o2.getName() != null) {
                return o1.getName().compareTo(o2.getName());
            }
            return -1;
        }
    }
}