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

package org.radixware.wps.dialogs;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.dialogs.ISelectEasSessionDialog;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.schemas.eas.SessionDescription;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.DecoratedLabel;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.Grid;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.VerticalBoxContainer;
import org.radixware.wps.text.WpsTextOptions;


public class SelectEasSessionDialog extends Dialog implements ISelectEasSessionDialog{
    
    private final static int SINGLE_SELECTION_HEIGHT = 230;
    private final static int MULTI_SELECTION_HEIGHT = 250;
    private final static int FULL_TABLE_WIDTH = 545;
    private final static int PART_TABLE_WIDTH = 530;
        
    private int selectionSize = 1;
    private final DecoratedLabel lbHeader;
    private final Label lbMessage;
    private final Label lbFooter = new Label();
    private final Grid grSessions = new Grid();
    private final WpsTextOptions textOptions;
    private final List<String> selectedSessions = new LinkedList<>();
    
    public SelectEasSessionDialog(final WpsEnvironment environment){
        super(environment.getDialogDisplayer(),null,false);
        final MessageProvider mp = environment.getMessageProvider();
        lbHeader = new DecoratedLabel(mp.translate("SelectEasSessionDialog", "The maximum number of opened sessions for this account has been exceeded.\nIt is necessary to terminate one of your previous sessions to create a new one."));
        lbMessage = new Label(mp.translate("SelectEasSessionDialog", "Select session you want to terminate:"));        
        textOptions = environment.getTextOptionsProvider().getDefault();
        setupUi(mp);
    }
    
    private void setupUi(final MessageProvider mp){        
        setWindowTitle(mp.translate("SelectEasSessionDialog", "Unable to Establish Connection"));
        final VerticalBoxContainer content = new VerticalBoxContainer();
        content.add(lbHeader);        
        lbHeader.setTextWrapDisabled(true);
        lbHeader.setHorizontalAlign(Alignment.CENTER);
        lbHeader.getHtml().removeClass("rwt-decorated-label");
        lbHeader.getHtml().removeClass("rwt-gradient-bar");
        lbHeader.setTextOptions(textOptions.changeBackgroundColor(null));
        content.addSpace();
        content.addSpace();
        content.add(lbMessage);        
        lbMessage.setTextOptions(textOptions.changeBackgroundColor(null));
        lbMessage.setAlign(Alignment.CENTER);
        grSessions.addColumn(mp.translate("SelectEasSessionDialog", "Selected"));
        grSessions.addColumn(mp.translate("SelectEasSessionDialog", "Creation Time"));
        grSessions.addColumn(mp.translate("SelectEasSessionDialog", "Idle Time"));
        grSessions.addColumn(mp.translate("SelectEasSessionDialog", "Station"));
        grSessions.addColumn(mp.translate("SelectEasSessionDialog", "Environment"));        
        grSessions.setStickToRight(true);      
        grSessions.setBorderVisible(false);
        grSessions.setSelectionMode(Grid.SelectionMode.ROW);        
        content.add(grSessions);
        content.addSpace();
        content.add(lbFooter);
        content.addSpace();
        lbFooter.setVisible(false);
        lbFooter.setTextOptions(textOptions.changeBackgroundColor(null));        
        content.setAutoSize(grSessions, true);                
        add(content);
        content.setTop(3);
        content.setLeft(3);
        content.getAnchors().setRight(new Anchors.Anchor(1, -3));
        content.getAnchors().setBottom(new Anchors.Anchor(1, -3));        
        addCloseAction(EDialogButtonType.OK).setEnabled(false);
        addCloseAction(EDialogButtonType.CANCEL);        
        updateHeight();
        updateWidth();
    }

    @Override
    public void setSessionsList(final List<SessionDescription> sessions) {
        grSessions.clearRows();
        boolean multipleEnvironments = false;
        final EditMaskDateTime creationTimeMask = 
            new EditMaskDateTime(EDateTimeStyle.SHORT, EDateTimeStyle.SHORT, null, null);
        final String idleTimeMaskTemplate = 
            getEnvironment().getMessageProvider().translate("SelectEasSessionDialog", "hh\"h\" mm\"m\"");        
        final EditMaskTimeInterval idleTimeMask = 
            new EditMaskTimeInterval(org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale.SECOND, idleTimeMaskTemplate);
        for (SessionDescription description: sessions){ 
            final Grid.Row row = grSessions.addRow();
            row.setUserData(description.getEncryptedId());
            row.getCell(0).setValue(Boolean.FALSE);
            final String creationTimeAsStr = 
                creationTimeMask.toStr(getEnvironment(), description.getCreationTime());            
            row.getCell(1).setValue(creationTimeAsStr);
            final String idleTimeAsStr = 
                idleTimeMask.toStr(getEnvironment(), Long.valueOf(description.getIdleSeconds()));            
            row.getCell(2).setValue(idleTimeAsStr);
            row.getCell(3).setValue(description.getStationName());
            final ERuntimeEnvironmentType environmentType = description.getEnvironment();
            final String envAsStr;
            if (environmentType==null){
                envAsStr = getEnvironment().getMessageProvider().translate("SelectEasSessionDialog","Unknown");
                multipleEnvironments = true;
            }else{
                envAsStr = environmentType.getName();
                if (environmentType!=ERuntimeEnvironmentType.WEB){
                    multipleEnvironments = true;
                }
            }
            row.getCell(4).setValue(envAsStr);
        }
        grSessions.getColumn(4).setVisible(multipleEnvironments);
        updateWidth();
    }

    @Override
    public void setSelectionSize(final int size) {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        selectionSize = size;
        selectedSessions.clear();
        if (selectionSize>1){
            grSessions.getColumn(0).getEditingOptions().setEditMask(new EditMaskBool());
            grSessions.getColumn(0).getEditingOptions().setEditingMode(IGrid.ECellEditingMode.NULL_VALUE_RESTRICTED);            
            grSessions.getColumn(0).setFixedWidth(80);
            grSessions.getColumn(0).setVisible(true);
            grSessions.setSelectionMode(Grid.SelectionMode.NONE);            
            lbHeader.setText(mp.translate("SelectEasSessionDialog", "The maximum number of opened sessions for this account has been exceeded.\nIt is necessary to terminate some of your previous sessions to create a new one."));
            lbMessage.setText(mp.translate("SelectEasSessionDialog", "Select sessions you want to terminate:"));
            grSessions.addCellValueChangeListener(new Grid.CellValueChangeListener() {
                @Override
                public void onValueChanged(final Grid.Cell cell, final Object oldValue, final Object newValue) {
                    if (cell.getCellIndex()==0 && newValue instanceof Boolean){
                        selectionChanged(grSessions.getRowIndex(cell.getRow()), newValue==Boolean.TRUE);
                    }                   
                }
            });
            lbFooter.setVisible(true);
            updateFooter();
        }else{
            grSessions.getColumn(0).getEditingOptions().setEditMask(null);
            grSessions.setSelectionMode(Grid.SelectionMode.ROW);
            grSessions.getColumn(0).setVisible(false);
            grSessions.addCurrentRowListener(new Grid.CurrentRowListener() {
                @Override
                public void currentRowChanged(Grid.Row oldRow, Grid.Row newRow) {
                    selectionChanged(newRow==null ? -1 : grSessions.getRowIndex(newRow), true);
                }

                @Override
                public boolean beforeChangeCurrentRow(Grid.Row oldRow, Grid.Row newRow) {
                    return true;
                }
            });
            grSessions.addDoubleClickListener(new Grid.DoubleClickListener() {
                @Override
                public void onDoubleClick(Grid.Row row, Grid.Cell cell) {
                    if (row!=null){
                        selectionChanged(grSessions.getRowIndex(row),true);
                        SelectEasSessionDialog.this.acceptDialog();
                    }                    
                }
            });
            lbHeader.setText(mp.translate("SelectEasSessionDialog", "The maximum number of opened sessions for this account has been exceeded.\nIt is necessary to terminate one of your previous sessions to create a new one."));
            lbMessage.setText(mp.translate("SelectEasSessionDialog", "Select session you want to terminate:"));
            lbFooter.setVisible(false);
        }        
        updateOkButton();
        updateHeight();
        updateWidth();
    }

    @Override
    public List<String> getSelectedSessionIds() {
        return Collections.unmodifiableList(selectedSessions);
    }
    
    private void updateFooter(){
        if (lbFooter.isVisible()){
            final int remains = selectedSessions.size()>selectionSize ? 0 : selectionSize-selectedSessions.size();
            final String template = 
                getEnvironment().getMessageProvider().translate("SelectEasSessionDialog", "Selected: %1$s. Remained: %2$s");
            lbFooter.setText(String.format(template, selectedSessions.size(), remains));
        }
    }
    
    private void updateOkButton(){
        getButton(EDialogButtonType.OK).setEnabled(selectedSessions.size()>=selectionSize);
    }
    
    private void selectionChanged(final int row, final boolean isSelected){
        if (isSelected){
            selectSession(row);
        }else{
            removeSelection(row);
        }
        updateOkButton();
        updateFooter();
    }
    
    private void selectSession(final int row){
        if (selectionSize==1){
            selectedSessions.clear();
        }
        if (row>-1){
            selectedSessions.add((String)grSessions.getRow(row).getUserData());
        }
    }
    
    private void removeSelection(final int row){
        if (row>-1){
            selectedSessions.remove((String)grSessions.getRow(row).getUserData());
        }
    }
    
    private void updateWidth(){
        boolean allColumnsVisible = true;
        for (int i=0, count=grSessions.getColumnCount(); i<count; i++){
            if (!grSessions.getColumn(i).isVisible()){
                allColumnsVisible = false;
                break;
            }
        }
        if (allColumnsVisible){
            setMinimumWidth(FULL_TABLE_WIDTH);
            setMaxWidth(FULL_TABLE_WIDTH);
        }else{
            setMinimumWidth(PART_TABLE_WIDTH);
            setMaxWidth(PART_TABLE_WIDTH);            
        }
    }
    
    private void updateHeight(){
        if (selectionSize==1){
            setHeight(SINGLE_SELECTION_HEIGHT);
            setMinimumHeight(SINGLE_SELECTION_HEIGHT);
        }else{
            setHeight(MULTI_SELECTION_HEIGHT);
            setMinimumHeight(MULTI_SELECTION_HEIGHT);            
        }
    }
}