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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QHeaderView.ResizeMode;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.ISortingsManagerDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.models.groupsettings.IGroupSetting;
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.views.Splitter;
import org.radixware.kernel.explorer.views.selector.SortingsManager;


public class SortingsManagerDialog extends ExplorerDialog implements ISortingsManagerDialog {
    
    private static class SortingColumnsTable extends QTableWidget {
        public static final int COL_TITLE = 0;
        public static final int COL_ORDER = 1;
        private final String ascSign, descSign;
        private final DefManager defManager;
                
        public SortingColumnsTable(final IClientEnvironment environment, final QWidget parent) {
            super(parent);
            setColumnCount(2);
                        
            setHorizontalHeaderItem(COL_TITLE, new QTableWidgetItem(
                    environment.getMessageProvider().translate("SelectorAddons", "Column")
            ));
            setHorizontalHeaderItem(COL_ORDER, new QTableWidgetItem(
                    environment.getMessageProvider().translate("SelectorAddons", "Sort order")
            ));
            horizontalHeader().setResizeMode(COL_TITLE, ResizeMode.Stretch);
            horizontalHeader().setResizeMode(COL_ORDER, ResizeMode.ResizeToContents);
            setSelectionMode(SelectionMode.NoSelection);
            setSelectionBehavior(SelectionBehavior.SelectRows);
            
            ascSign = environment.getMessageProvider().translate("SelectorAddons", "Ascending");
            descSign = environment.getMessageProvider().translate("SelectorAddons", "Descending");            
            defManager = environment.getDefManager();
        }
        
        public void setSorting(final RadSortingDef sorting){
            for(int row=rowCount()-1;row>=0;row--)
                removeRow(row);
            if (sorting!=null){
                final RadClassPresentationDef classDef =  
                            defManager.getClassPresentationDef(sorting.getOwnerClassId());
                for (RadSortingDef.SortingItem sortingItem: sorting.getSortingColumns()){
                    add(sortingItem, classDef);
                }
            }
        }
        
        private void add(final RadSortingDef.SortingItem sortingItem, final RadClassPresentationDef classPresentation) {
            final int row = rowCount();
            if (classPresentation.isPropertyDefExistsById(sortingItem.propId)){
                setRowCount(row + 1);
                //column 0
                QTableWidgetItem item = new QTableWidgetItem();            
                item.setText(classPresentation.getPropertyDefById(sortingItem.propId).getTitle());            
                item.setFlags(new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled));
                setItem(row, COL_TITLE, item);
                //column 1
                final boolean isDesc = sortingItem.sortDesc;
                final String text = isDesc ? descSign : ascSign;
                item = new QTableWidgetItem();
                item.setFlags(new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled));
                item.setText(text);
                setItem(row, COL_ORDER, item);
            }
        }
        
    }    
    
    private final Splitter splitter;
    private final SortingsManager editor;    
    private RadSortingDef currentSorting;
    private final SortingColumnsTable columnsTable;
    
    
    public SortingsManagerDialog(final IClientEnvironment environment, final QWidget parent, final Sortings sortings){
        super(environment,parent,null);        
        splitter = new Splitter(this,(ExplorerSettings)getEnvironment().getConfigStore());
        setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
        editor = new SortingsManager(environment, this, sortings);
        columnsTable = new SortingColumnsTable(environment,splitter);
        setupUi();
        onChangeCurrentSorting(editor.getCurrentSetting());                
    }

    private void setupUi() {
        layout().addWidget(splitter);
        splitter.setOrientation(Orientation.Horizontal);
        splitter.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        splitter.addWidget(editor);
        splitter.addWidget(columnsTable);
        splitter.setChildrenCollapsible(false);
        splitter.setRatio(1. / 3.);
        addButton(EDialogButtonType.CLOSE).setDefault(true);        
        editor.currentSettingChanged.connect(this, "onChangeCurrentSorting(IGroupSetting)");
        setWindowTitle(getEnvironment().getMessageProvider().translate("SelectorAddons", "Sortings Manager"));
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.SORTING));
        
        rejectButtonClick.connect(this, "reject()");
        editor.applySetting.connect(this, "accept()");
    }
    
    @Override
    public void done(int result) {
        currentSorting = (RadSortingDef) editor.getCurrentSetting();
        editor.close();
        super.done(result);
    }    

    @Override
    public RadSortingDef getCurrentSorting() {
        return currentSorting==null ? (RadSortingDef)editor.getCurrentSetting() : currentSorting;
    }
    
    @SuppressWarnings("unused")
    private void onChangeCurrentSorting(final IGroupSetting setting) {
        columnsTable.setSorting((RadSortingDef)setting);
    }
    
}
