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
import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.CaseSensitivity;
import com.trolltech.qt.core.Qt.ScrollBarPolicy;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QListView;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPalette.ColorRole;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup.DefInfo;
import org.radixware.kernel.explorer.editors.jmleditor.completer.ItemDelegate;
import org.radixware.kernel.explorer.env.Application;


public abstract class BaseChoosePanel extends QWidget {

    private ScrolledList list;
    private FilterEditLine edFilter;
    protected IChooseDefFromList parent;
    private PleaseWaitPanel lbPleaseWait;
    private ItemDelegate delegate;
    static final int LOAD_ITEM_COUNT = 50;
    private List<DefInfo> defList;

    public BaseChoosePanel(final IChooseDefFromList parent) {
        this.parent = parent;
    }

    void setFocusToFilter() {
        edFilter.setFocus();
    }

    @SuppressWarnings("unused")
    protected void onItemClick(final QModelIndex modelIndex) {
        parent.onItemClick(modelIndex);
    }

    @SuppressWarnings("unused")
    protected void onItemDoubleClick(final QModelIndex modelIndex) {
        parent.onItemDoubleClick(modelIndex);
    }

    protected void createList() {
        list = new ScrolledList(this);
        list.setObjectName("ChooseList");
        list.clicked.connect(this, "onItemClick(QModelIndex)");
        list.activated.connect(this, "onItemClick(QModelIndex)");
        list.doubleClicked.connect(this, "onItemDoubleClick(QModelIndex)");
        list.setIconSize(new QSize(22, 22));
        list.setAlternatingRowColors(true);
        list.setItemDelegate(new ItemDelegate(list));
        list.setVerticalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOn);

        final BaseModelList model = createModelList(list.getLoadedDefsCount(), null);
        model.setOwnerList(list);
        list.setModel(model);

        list.setSelectionMode(SelectionMode.SingleSelection);
        list.setForegroundRole(ColorRole.NoRole);
        list.setHorizontalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOff);
        QPalette pal = list.palette().clone();
        //CDE and Motif have equals color for both roles, Windows Vista - not.
        //Set color of Inactive current item = color of Active item for simplicity
        pal.setColor(QPalette.ColorGroup.Inactive, ColorRole.Highlight, 
                     pal.color(QPalette.ColorGroup.Active, ColorRole.Highlight));
        list.setPalette(pal);

        if (list.model().index(0, 0) != null) {
            list.setCurrentIndex(list.model().index(0, 0));
            list.clicked.emit(list.currentIndex());
        }
        list.setVisible(false);
    }

    protected abstract BaseModelList createModelList(final int count, final Collection<DefInfo> definitionList);

    protected void setVisibleForList(final boolean visible) {
        lbPleaseWait.setVisible(!visible);
        if (list != null) {
            list.setVisible(visible);
        }
        //double index=list.size().height()/22;
    }

    protected void setDefinitionList(int count) {
        int curRow = 0;
        if (list.currentIndex() != null) {
            curRow = list.currentIndex().row();
        }

        final BaseModelList model = createModelList(count, list.getDefinitionList());
        model.setOwnerList(list);
        list.setModel(model);

        QModelIndex curIndex = null;
        if (model.rowCount() > curRow) {
            curIndex = model.index(curRow, 0);
        }
        setCurItem(curIndex);
        //list.clicked.emit(list.currentIndex());
        //edFilter.clear();
         /*if(list.model().rowCount()>0){
         if (curIndex!=null) {
         list.setCurrentIndex(curIndex);
         } else{
         list.setCurrentIndex(list.model().index(0, 0));
         }
         list.clicked.emit(list.currentIndex());
         }*/
        //edFilter.clear();
        //edFilter.textChanged.emit("");
    }

    protected boolean checkName(String pattern, final String name) {
        pattern += "*";
        final QRegExp reg = new QRegExp(pattern);
        reg.setCaseSensitivity(CaseSensitivity.CaseInsensitive);
        reg.setPatternSyntax(QRegExp.PatternSyntax.Wildcard);
        String nameToMatchTo = name;
        int parentIndex = nameToMatchTo.indexOf('(');
        if (parentIndex > 0) {
            int spaceIndex = nameToMatchTo.indexOf(' ');
            if (spaceIndex > 0 && parentIndex > spaceIndex) {
                //lib user function choose.
                nameToMatchTo = nameToMatchTo.substring(spaceIndex + 1, parentIndex);
            }
        }
        return reg.exactMatch(nameToMatchTo);
    }

    protected List<DefInfo> getDefList() {
        return defList;
    }

    protected void updateModelForDef(final List<DefInfo> defList) {
        this.defList = defList;
        list.setDefinitionList(defList);
        //QModelIndex curIndex=list.currentIndex();
        final ListModel model = (ListModel) list.model();
        final List<DefInfo> filteredlist = new ArrayList<>();
        if (defList != null) {
            int index = 0;
            for (DefInfo def : defList) {
                if (index >= list.getLoadedDefsCount()) {
                    break;
                }
                filteredlist.add(def);
                index++;
            }
        }
        model.setDefList(filteredlist);
        if (!filteredlist.isEmpty()) {
            setCurIndex(0);
        } else {
            setCurItem(null);
        }
    }

    private void setCurIndex(final int row) {
        final QModelIndex modelIndex = list.model().index(row, 0);
        list.setCurrentIndex(modelIndex);
        onItemClick(modelIndex);
    }

    protected void updateModelForObj(final List<RadixObject> defList) {
        final QModelIndex curIndex = list.currentIndex();
        final ListModelForRadixObj model = (ListModelForRadixObj) list.model();
        model.setDefList(defList);
        setCurItem(curIndex);
    }

    private void setCurItem(QModelIndex curIndex) {
        if (list.model().rowCount() > 0) {
            if (curIndex != null && curIndex.row() < list.model().rowCount()) {
                list.setCurrentIndex(curIndex);
            } else {
                curIndex = list.model().index(0, 0);
                list.setCurrentIndex(curIndex);
            }
            list.clicked.emit(curIndex);
        } else {
            parent.setCurItem(null);
        }
        //list.setUpdatesEnabled(true);
    }

    public void setLoadedDefsCount(final int count) {
        list.setLoadedDefsCount(count);
    }

    protected void createListUi() {
        final QVBoxLayout layout = new QVBoxLayout();

        final QLabel labelDefList = new QLabel(Application.translate("JmlEditor", "Definitions Found:"), this);

        lbPleaseWait = new PleaseWaitPanel(this);
        lbPleaseWait.setObjectName("lbPleaseWait");
        createList();
        
        final QLabel labelFilter = new QLabel(Application.translate("JmlEditor", "Definition name:"), this);
        edFilter = new FilterEditLine(this, list);
        edFilter.textChanged.connect(this, "findTextChanged(String)");

        layout.addWidget(labelFilter);
        layout.addWidget(edFilter);
        layout.addWidget(labelDefList);
        layout.addWidget(lbPleaseWait);
        if (list != null) {
            layout.addWidget(list);
        }
        this.setLayout(layout);
    }

    @SuppressWarnings("unused")
    protected abstract void findTextChanged(final String s);

    class ScrolledList extends QListView {

        private int index = 50;
        private Collection<DefInfo> definitionList;

        private void scrolled(final int i) {
            if ((this.verticalScrollBar().maximum() > 0) && (i >= this.verticalScrollBar().maximum()) && index >= 0) {
                //setUpdatesEnabled(false);                
                index += LOAD_ITEM_COUNT;
                BaseChoosePanel.this.setDefinitionList(index);
                QModelIndex scrolledIndex = model().index(i, 0);
                scrollTo(scrolledIndex, ScrollHint.PositionAtCenter);
                //setUpdatesEnabled(true);                
            }
        }

        ScrolledList(final QWidget parent) {
            super(parent);
            this.verticalScrollBar().valueChanged.connect(this, "scrolled(int)");
        }

        int getLoadedDefsCount() {
            return index;
        }

        void setLoadedDefsCount(int i) {
            index = i;
        }

        Collection<DefInfo> getDefinitionList() {
            return definitionList;
        }

        void setDefinitionList(final Collection<DefInfo> definitionList) {
            this.definitionList = definitionList;
        }

        @Override
        protected void keyPressEvent(QKeyEvent qke) {
            if (qke.key() == Qt.Key.Key_Return.value()) {
                onItemDoubleClick(currentIndex());
            } else {
                super.keyPressEvent(qke);
            }
        }
    }

    /* protected boolean checkName(final String name,List<String> serchMaskList){
     int charIndex=0,nameLen=name.length();
     boolean isStar=false;
     for(int j=0,size=serchMaskList.size();j<size;j++){
     if(serchMaskList.get(j).equals("?")){
     charIndex++;
     }else if(serchMaskList.get(j).equals("*")){
     isStar=true;
     }else{
     if(isStar){
     int pos;
     if((pos=name.toLowerCase().indexOf(serchMaskList.get(j).toLowerCase(), charIndex))!=-1){
     charIndex=pos+serchMaskList.get(j).length();
     }else{
     return false;
     }
     }else{
     if((nameLen<charIndex+serchMaskList.get(j).length()))return false;
     if(!serchMaskList.get(j).equalsIgnoreCase(name.substring(charIndex,charIndex+serchMaskList.get(j).length()))){
     return false;
     }
     charIndex=charIndex+serchMaskList.get(j).length();
     }
     isStar=false;
     }
     }
     return true;
     }

     protected List<String> parseFindString(String findString){
     List<String> res=new ArrayList<String>();
     int n=0,i=0,size=findString.length();
     for(i=0;i<size;i++){
     if((findString.charAt(i)=='?')||(findString.charAt(i)=='*')){
     if(i>n)
     res.add(findString.substring(n,i));
     res.add(findString.substring(i,i+1));
     n=i+1;
     }
     }
     if(i>n)
     res.add(findString.substring(n,i));
     return res;
     }*/
    
    public void setFilteredComponent(final QAbstractItemView filteredComponent) {
        if(edFilter != null) {
            edFilter.setFilteredComponent(filteredComponent);
        }
    }
    
    class FilterEditLine extends QLineEdit {
        
        private QAbstractItemView filteredComponent;

        FilterEditLine(final QWidget parent, final QAbstractItemView filteredComponent) {
            super(parent);
            this.filteredComponent = filteredComponent;
        }

        public void setFilteredComponent(QAbstractItemView filteredComponent) {
            this.filteredComponent = filteredComponent;
        }

        @Override
        protected void keyPressEvent(final QKeyEvent qke) {
            if (qke.key() == Qt.Key.Key_Up.value() || qke.key() == Qt.Key.Key_Down.value() || qke.key() == Qt.Key.Key_Left.value() || qke.key() == Qt.Key.Key_Right.value()) {
                QApplication.postEvent(filteredComponent, new QKeyEvent(qke.type(), qke.key(), qke.modifiers()));
            } else if (qke.key() == Qt.Key.Key_Return.value()) {
                onItemDoubleClick(filteredComponent.currentIndex());
            } else {
                super.keyPressEvent(qke);
            }
        }
    }
}
