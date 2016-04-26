/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.ISelectEasSessionDialog;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.schemas.eas.SessionDescription;


public class SelectEasSessionDialog extends ExplorerDialog implements ISelectEasSessionDialog{
    
    private int selectionSize = 1;
    private final QLabel lbHeader;
    private final QLabel lbMessage;
    private final QLabel lbFooter = new QLabel(this);
    private final QTableWidget twSession;
    private final List<String> selectedSessions = new LinkedList<>();
    
    public SelectEasSessionDialog(final IClientEnvironment environment, final QWidget parent){
        super(environment,parent);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose,true);        
        final MessageProvider mp = environment.getMessageProvider();        
        lbHeader = new QLabel(mp.translate("SelectEasSessionDialog", "The maximum number of opened sessions for this account has been exceeded.\nIt is necessary to terminate one of your previous sessions to create a new one.") , this);
        lbMessage = new QLabel(mp.translate("SelectEasSessionDialog", "Select session you want to terminate:") , this);
        twSession = new QTableWidget(this){
            @Override
            public QSize sizeHint() {
                final QSize size = super.sizeHint();
                int width = 0;
                for (int i=0,count=columnCount(); i<count; i++){
                    if (!horizontalHeader().isSectionHidden(i)){
                        width+=columnWidth(i);                        
                    }
                }
                if (verticalScrollBar().isVisible()){
                    width+=verticalScrollBar().width();
                }
                width+=4;
                if (size.width()<width){
                    size.setWidth(width);
                }
                return size;
            }

            @Override
            protected boolean edit(QModelIndex index, QAbstractItemView.EditTrigger trigger, QEvent event) {
                if (trigger==QAbstractItemView.EditTrigger.DoubleClicked){
                    SelectEasSessionDialog.this.itemDoubleClicked(index.row());
                }
                return super.edit(index, trigger, event); //To change body of generated methods, choose Tools | Templates.
            }
            
            
        };
        setupUi(mp);
    }
    
    private void setupUi(final MessageProvider mp){         
        setWindowTitle(mp.translate("SelectEasSessionDialog", "Unable to establish connection"));
        dialogLayout().addWidget(lbHeader);
        dialogLayout().addWidget(lbMessage);
        lbMessage.setAlignment(Qt.AlignmentFlag.AlignCenter);
        twSession.setColumnCount(5);
        twSession.verticalHeader().setVisible(false);        
        twSession.horizontalHeader().setVisible(true);
        twSession.horizontalHeader().setHighlightSections(false);
        twSession.setHorizontalHeaderItem(0, new QTableWidgetItem(mp.translate("SelectEasSessionDialog", "Selected")));
        twSession.setHorizontalHeaderItem(1, new QTableWidgetItem(mp.translate("SelectEasSessionDialog", "Creation Time")));
        twSession.setHorizontalHeaderItem(2, new QTableWidgetItem(mp.translate("SelectEasSessionDialog", "Idle Time")));
        twSession.setHorizontalHeaderItem(3, new QTableWidgetItem(mp.translate("SelectEasSessionDialog", "Station")));
        twSession.setHorizontalHeaderItem(4, new QTableWidgetItem(mp.translate("SelectEasSessionDialog", "Environment")));
        
        for (int i=0, count=twSession.columnCount(); i<count; i++){
            twSession.horizontalHeader().setResizeMode(i, QHeaderView.ResizeMode.ResizeToContents);
        }        
        twSession.setEditTriggers(QAbstractItemView.EditTrigger.NoEditTriggers);
        twSession.setShowGrid(false);
        twSession.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        twSession.setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);        
        twSession.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);        
        dialogLayout().addWidget(twSession);
        dialogLayout().addWidget(lbFooter);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        getButton(EDialogButtonType.OK).setEnabled(false);
        setAutoWidth(true);
    }

    @Override
    public void setSessionsList(final List<SessionDescription> sessions) {
        twSession.clearContents();
        boolean multipleEnvironments = false;
        final EditMaskDateTime creationTimeMask = 
            new EditMaskDateTime(EDateTimeStyle.SHORT, EDateTimeStyle.SHORT, null, null);
        final String idleTimeMaskTemplate = 
            getEnvironment().getMessageProvider().translate("SelectEasSessionDialog", "hh\"h\" mm\"m\"");
        final EditMaskTimeInterval idleTimeMask = 
            new EditMaskTimeInterval(org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale.SECOND, idleTimeMaskTemplate);
        int row = 0;
        for (SessionDescription description: sessions){    
            twSession.setRowCount(row+1);
            twSession.setItem(row, 0, createTableCheckableItem(description.getEncryptedId()));
            final String creationTimeAsStr = 
                creationTimeMask.toStr(getEnvironment(), description.getCreationTime());
            twSession.setItem(row, 1, createTableTextItem(creationTimeAsStr));
            final String idleTimeAsStr = 
                idleTimeMask.toStr(getEnvironment(), Long.valueOf(description.getIdleSeconds()));
            twSession.setItem(row, 2, createTableTextItem(idleTimeAsStr));
            twSession.setItem(row, 3, createTableTextItem(description.getStationName()));
            final ERuntimeEnvironmentType environmentType = description.getEnvironment();
            final String envAsStr;
            if (environmentType==null){
                envAsStr = getEnvironment().getMessageProvider().translate("SelectEasSessionDialog","Unknown");
                multipleEnvironments = true;
            }else{
                envAsStr = environmentType.getName();
                if (environmentType!=ERuntimeEnvironmentType.EXPLORER){
                    multipleEnvironments = true;
                }
            }
            twSession.setItem(row, 4, createTableTextItem(envAsStr));
            row++;
        }
        if (multipleEnvironments){
            twSession.horizontalHeader().setResizeMode(4, QHeaderView.ResizeMode.Stretch);            
        }else{
            twSession.horizontalHeader().setResizeMode(3, QHeaderView.ResizeMode.Stretch);
            twSession.horizontalHeader().setSectionHidden(4, true);
        }
        twSession.resizeColumnsToContents();        
        updateGeometry();
    }
    
    private static QTableWidgetItem createTableTextItem(final String text){
        final QTableWidgetItem item = new QTableWidgetItem(text);
        item.setFlags(Qt.ItemFlag.ItemIsEnabled,Qt.ItemFlag.ItemIsSelectable);
        return item;
    }
    
    private static QTableWidgetItem createTableCheckableItem(final String sessionId){
        final QTableWidgetItem item = new QTableWidgetItem();
        item.setData(Qt.ItemDataRole.UserRole, sessionId);
        item.setFlags(Qt.ItemFlag.ItemIsEnabled,Qt.ItemFlag.ItemIsEditable,Qt.ItemFlag.ItemIsSelectable,Qt.ItemFlag.ItemIsUserCheckable);
        item.setCheckState(Qt.CheckState.Unchecked);
        return item;
    }
    

    @Override
    public void setSelectionSize(final int size) {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        selectionSize = size;
        selectedSessions.clear();
        twSession.clearSelection();
        twSession.disconnect();
        if (selectionSize>1){
            twSession.setSelectionMode(QAbstractItemView.SelectionMode.MultiSelection);
            twSession.horizontalHeader().setSectionHidden(0, false);
            twSession.setCurrentIndex(null);
            twSession.itemChanged.connect(this, "itemChanged(QTableWidgetItem)");            
            lbHeader.setText(mp.translate("SelectEasSessionDialog", "The maximum number of opened sessions for this account has been exceeded.\nIt is necessary to terminate some of your previous sessions to create a new one."));
            lbMessage.setText(mp.translate("SelectEasSessionDialog", "Select sessions you want to terminate:"));
            lbFooter.setVisible(true);
            updateFooter();
        }else{
            twSession.setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);
            twSession.horizontalHeader().setSectionHidden(0, true);                        
            lbHeader.setText(mp.translate("SelectEasSessionDialog", "The maximum number of opened sessions for this account has been exceeded.\nIt is necessary to terminate one of your previous sessions to create a new one."));
            lbMessage.setText(mp.translate("SelectEasSessionDialog", "Select session you want to terminate:"));
            lbFooter.setVisible(false);
        }
        twSession.itemSelectionChanged.connect(this,"selectionChanged()");
        twSession.updateGeometry();        
        updateOkButton();        
    }

    @Override
    protected void showEvent(final QShowEvent event) {
        super.showEvent(event);
        twSession.updateGeometry();
    }        

    @Override
    public List<String> getSelectedSessionIds() {
        return Collections.unmodifiableList(selectedSessions);
    }
    
    @SuppressWarnings("unused")
    private void itemChanged(final QTableWidgetItem item){
        if (item.flags().isSet(Qt.ItemFlag.ItemIsUserCheckable)){
            updateSelection();
        }
    }
    
    private void updateSelection(){
        twSession.blockSignals(true);
        int scrollValue = twSession.verticalScrollBar().isVisible() ? twSession.verticalScrollBar().value() : -1;
        try{
            twSession.clearSelection();
            selectedSessions.clear();
            for (int row=0, count=twSession.rowCount(); row<count; row++){
                if (twSession.item(row, 0).checkState()==Qt.CheckState.Checked){
                    selectSession(row);
                    twSession.selectRow(row);
                }
            }            
            updateFooter();
            updateOkButton();
            twSession.setCurrentIndex(null);
        }finally{
            twSession.blockSignals(false);
        }
        if (scrollValue>-1){
            twSession.verticalScrollBar().setValue(scrollValue);
        }        
    }
        
    private void itemDoubleClicked(final int row){
        if (selectionSize==1){
            selectSession(row);
            accept();
        }
    }
    
    @SuppressWarnings("unused")
    private void selectionChanged(){
        if (selectionSize>1){
            updateSelection();
        }else{
            selectSession(twSession.currentRow());
            twSession.updateGeometry();
            updateOkButton();
        }
    }
    
    private void selectSession(final int row){
        if (selectionSize==1){
            selectedSessions.clear();
        }
        selectedSessions.add((String)twSession.item(row, 0).data(Qt.ItemDataRole.UserRole));
    }
    
    private void updateFooter(){
        final int remains = selectedSessions.size()>selectionSize ? 0 : selectionSize-selectedSessions.size();
        final String template = 
            getEnvironment().getMessageProvider().translate("SelectEasSessionDialog", "Selected: %1$s. Remained: %2$s");
        lbFooter.setText(String.format(template, selectedSessions.size(), remains));
    }
    
    private void updateOkButton(){
        getButton(EDialogButtonType.OK).setEnabled(selectedSessions.size()>=selectionSize);
    }

}