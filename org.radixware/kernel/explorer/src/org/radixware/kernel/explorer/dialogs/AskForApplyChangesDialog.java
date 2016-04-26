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
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IAskForApplyChangesDialog;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.views.ModificationsList;
import org.radixware.kernel.common.client.views.ModificationsList.ModifiedObject;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public final class AskForApplyChangesDialog extends ExplorerDialog implements IAskForApplyChangesDialog{
    
    private final ModificationsList modifications;
    private final QTreeWidget twModifications = new QTreeWidget(this);
    
    public AskForApplyChangesDialog(final IClientEnvironment environment, final QWidget parentWidget, final ModificationsList modificationsList){
        super(environment,parentWidget);
        modifications = modificationsList;
        setupUi();
    }
    
    private void setupUi(){
        final MessageProvider mp = getEnvironment().getMessageProvider();
        setWindowTitle(mp.translate("ExplorerDialog","Confirm to Apply Changes"));
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Editor.SAVE));
        {
            final String headerText = 
                mp.translate("ExplorerDialog", "The following objects have been modified. Do you want to save them before closing?");        
            final QLabel lbHeader = new QLabel(headerText, this);
            lbHeader.setAlignment(Qt.AlignmentFlag.AlignHCenter);
            lbHeader.setWordWrap(false);
            final QFont headerFont = new QFont(lbHeader.font());
            headerFont.setBold(true);
            headerFont.setPointSize(10);
            lbHeader.setFont(headerFont);
            dialogLayout().addWidget(lbHeader);
        }
        {
            twModifications.setObjectName("twModifications");
            twModifications.header().setVisible(false);
            final List<ModifiedObject> modifiedObjects = modifications.getModifiedObjectsTree();
            for (ModifiedObject modifiedObject: modifiedObjects){
                createTreeItem(modifiedObject, null);
            }            
            dialogLayout().addWidget(twModifications);            
            if (twModifications.topLevelItemCount()>0){
                twModifications.expandAll();
                twModifications.setCurrentItem(twModifications.topLevelItem(0));
            }
        }
        {
            final QPushButton button = (QPushButton)addButton(EDialogButtonType.YES);
            button.setObjectName("btn_save_selected");
            button.setText(mp.translate("ExplorerDialog", "&Save Selected"));
            button.setIcon(ExplorerIcon.getQIcon(ClientIcon.Editor.NEED_FOR_SAVE));
            button.setDefault(true);
            button.clicked.connect(this,"accept()");
        }
        {
            final QPushButton button = (QPushButton)addButton(EDialogButtonType.NO_TO_ALL);
            button.setObjectName("btn_do_not_save");
            button.setText(mp.translate("ExplorerDialog", "Do &Not Save"));
            button.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CANCEL));
            button.clicked.connect(this,"onDoNotSaveClick()");
        }
        {
            final QPushButton button = (QPushButton)addButton(EDialogButtonType.CANCEL);
            button.setObjectName("btn_do_not_close");
            button.setText(mp.translate("ExplorerDialog", "Do Not &Close"));
            button.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_CANCEL));            
            button.clicked.connect(this,"reject()");
        }
    }
    
    private QTreeWidgetItem createTreeItem(final ModifiedObject modifiedObject, final QTreeWidgetItem parentItem){
        final QTreeWidgetItem item = 
            parentItem==null ? new QTreeWidgetItem(twModifications) : new QTreeWidgetItem(parentItem);
        item.setText(0, modifiedObject.getTitle());
        item.setIcon(0, ExplorerIcon.getQIcon(modifiedObject.getIcon()));
        item.setFlags(new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled,Qt.ItemFlag.ItemIsUserCheckable));
        item.setCheckState(0, Qt.CheckState.Checked);
        item.setData(0, Qt.ItemDataRole.UserRole, modifiedObject);
        for (ModifiedObject child: modifiedObject.getChildren()){
            createTreeItem(child, item);
        }
        return item;
    }

    @Override
    public List<ModifiedObject> getItemsToApplyChanges() {
        final List<ModifiedObject> objectsToSave = new LinkedList<ModifiedObject>();        
        for (int i=0,count=twModifications.topLevelItemCount(); i<count; i++){
            addCheckedItems(twModifications.topLevelItem(i), objectsToSave);
        }
        return Collections.<ModifiedObject>unmodifiableList(objectsToSave);
    }
    
    private static void addCheckedItems(final QTreeWidgetItem item, final List<ModifiedObject> objectsToSave){
        if (item.checkState(0)==Qt.CheckState.Checked){
            objectsToSave.add((ModifiedObject)item.data(0, Qt.ItemDataRole.UserRole));
        }
        for (int i=0,count=item.childCount(); i<count; i++){
            addCheckedItems(item.child(i), objectsToSave);
        }
    }
    
    private static void uncheckItems(final QTreeWidgetItem item){
        item.setCheckState(0, Qt.CheckState.Unchecked);
        for (int i=0,count=item.childCount(); i<count; i++){
            uncheckItems(item.child(i));
        }
    }
        
    @SuppressWarnings("unused")
    private void onDoNotSaveClick(){
        for (int i=0,count=twModifications.topLevelItemCount(); i<count; i++){
            uncheckItems(twModifications.topLevelItem(i));
        }
        accept();
    }    
}
