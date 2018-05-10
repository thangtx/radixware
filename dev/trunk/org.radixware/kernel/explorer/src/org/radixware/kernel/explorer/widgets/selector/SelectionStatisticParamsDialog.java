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

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.types.AggregateFunctionCall;
import org.radixware.kernel.common.client.types.SelectorColumnsStatistic;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EAggregateFunction;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.exceptions.WrongFormatException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.widgets.AutoResizingTable;
import org.radixware.kernel.explorer.widgets.CenteredCheckBoxItemDelegate;
import org.radixware.kernel.explorer.widgets.HeaderWithCheckBox;


public class SelectionStatisticParamsDialog extends ExplorerDialog{
    
    private final static String SETTINGS_KEY = "statistic_params";        
    
    private final GroupModel groupModel;    
    private final List<Id> columnsOrder;     
    private final QTableWidget table = new AutoResizingTable(this);
    final List<AggregateFunctionCall> aggregateFunctions = new LinkedList<>();
    private final ExplorerTextOptions cellOptions;
    private final CenteredCheckBoxItemDelegate cellDelegate = new CenteredCheckBoxItemDelegate(this);
    private final QCheckBox cbRowsCount = new QCheckBox(this);
    private final HeaderWithCheckBox horizontalHeader;
    private final EditMaskInt precisionEditMask = 
        new EditMaskInt(0l,Long.valueOf(Integer.MAX_VALUE),(byte)0,null,1l,null,(byte)10);
    
    public SelectionStatisticParamsDialog(final GroupModel groupModel, 
                                                            final QWidget parent, 
                                                            final List<Id> columnsOrder){
        super(groupModel.getEnvironment(),parent,groupModel.getDefinition().getId().toString()+SETTINGS_KEY,600,250);
        this.groupModel = groupModel;        
        this.columnsOrder = columnsOrder; 
        cellOptions = 
            (ExplorerTextOptions)getEnvironment().getTextOptionsProvider().getOptions(EnumSet.of(ETextOptionsMarker.REGULAR_VALUE, ETextOptionsMarker.SELECTOR_ROW), ESelectorRowStyle.NORMAL);        
        horizontalHeader = new HeaderWithCheckBox(Qt.Orientation.Horizontal, table);
        setupUi();
    }
    
    private void setupUi(){
        final MessageProvider mp = getEnvironment().getMessageProvider();
        setWindowTitle(mp.translate("Selector", "Statistics for Selection"));
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Selector.CALC_STATISTIC));
        table.setHorizontalHeader(horizontalHeader);
        table.verticalHeader().setVisible(false);
        table.horizontalHeader().setVisible(true);        
        table.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectItems);
        table.setSelectionMode(QAbstractItemView.SelectionMode.NoSelection);        
        table.setColumnCount(EAggregateFunction.values().length+1);        
        horizontalHeader.checkBoxClicked.connect(this, "onVHeaderCheck(int, boolean)");
        int colCount = 1;        
        table.setHorizontalHeaderItem(0, createHHeaderCell(mp.translate("Selector", "Column"),null));
        horizontalHeader.setResizeMode(0, QHeaderView.ResizeMode.Interactive);
        String functionTitle;
        QTableWidgetItem headerCell;
        for(EAggregateFunction function: EAggregateFunction.values()){
            if (function!=EAggregateFunction.COUNT){
                functionTitle = SelectorColumnsStatistic.getAggregationFunctionTitle(function, mp);
                headerCell = 
                    createHHeaderCell(ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), functionTitle), function);
                table.setHorizontalHeaderItem(colCount, headerCell);
                horizontalHeader.setSectionUserCheckable(colCount, true);                
                horizontalHeader.setSectionCheckState(colCount, Qt.CheckState.Unchecked);
                horizontalHeader.setResizeMode(colCount, QHeaderView.ResizeMode.ResizeToContents);
                table.setItemDelegateForColumn(colCount, cellDelegate);
                colCount++;                
            }
        }
        table.setHorizontalHeaderItem(colCount, createHHeaderCell(mp.translate("Selector", "Values Accuracy"),null));
        horizontalHeader.setResizeMode(colCount, QHeaderView.ResizeMode.ResizeToContents);
        colCount++;
        int rowCount = 0;        
        for (Id columnId: columnsOrder){
            final SelectorColumnModelItem column = groupModel.getSelectorColumn(columnId);
            final RadPropertyDef propertyDef = column.getPropertyDef();
            table.setRowCount(rowCount+1); 
            table.setItem(rowCount, 0, createVHeaderCell(column.getTitle(), columnId));                
            for (int col=1; col<colCount-1; col++){
                table.setItem(rowCount, col, createCell(false, columnId));
            }
            table.setCellWidget(rowCount, colCount-1, createPrecisionEditor(propertyDef));
            rowCount++;
        }
        dialogLayout().addWidget(table);
        table.resizeColumnToContents(0);
        cbRowsCount.setText(mp.translate("Selector", "Calculate number of rows"));
        cbRowsCount.setCheckState(Qt.CheckState.Checked);
        cbRowsCount.stateChanged.connect(this,"updateOKButton()");
        dialogLayout().addWidget(cbRowsCount);
        addButton(EDialogButtonType.OK).addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(final IButton source) {
                onOkButton();
            }
        });
        addButton(EDialogButtonType.CANCEL);
        rejectButtonClick.connect(this,"reject()");
        readFromConfig();
        table.cellChanged.connect(this, "onCellChanged(int, int)");
        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        updateHeaderCheck();
        updatePrecisionEditors();
        updateOKButton();
    }
    
    private void onOkButton(){
        for (int row=0; row<table.rowCount(); row++){
            final int precision = getPrecision(row);
            for (int column=1; column<table.columnCount()-1; column++){
                final EAggregateFunction function = 
                    (EAggregateFunction)table.horizontalHeaderItem(column).data(Qt.ItemDataRole.UserRole);
                if (table.item(row, column).checkState()==Qt.CheckState.Checked){
                    final Id columnId = (Id)table.item(row, column).data(Qt.ItemDataRole.UserRole);
                    aggregateFunctions.add(new AggregateFunctionCall(columnId, function, precision));
                }
            }
        }
        if (cbRowsCount.checkState()==Qt.CheckState.Checked){
            aggregateFunctions.add(new AggregateFunctionCall(null, EAggregateFunction.COUNT));
        }
        writeToConfig(aggregateFunctions);
        accept();
    }
    
    @SuppressWarnings("unused")
    private void onVHeaderCheck(final int col, final boolean checked){
        table.cellChanged.disconnect(this);
        try{
            for (int row=0; row<table.rowCount(); row++){
                table.item(row, col).setCheckState(checked ? Qt.CheckState.Checked : Qt.CheckState.Unchecked);
            }
            if (table.horizontalHeaderItem(col).data(Qt.ItemDataRole.UserRole)==EAggregateFunction.AVG){
                updatePrecisionEditors();
            }
        }finally{
            table.cellChanged.connect(this, "onCellChanged(int, int)");
        }
    }
    
    @SuppressWarnings("unused")
    private void onCellChanged(final int row, final int col){
        updateHeaderCheck(col);
        updatePrecisionEditor(row);
        updateOKButton();
    }
    
    private void updateHeaderCheck(){
        for (int col=1; col<table.columnCount()-1; col++){
            updateHeaderCheck(col);
        }
    }
    
    private void updateHeaderCheck(final int col){
        boolean allChecked = true;
        boolean allUnchecked = true;
        boolean isChecked;
        for (int row=0; row<table.rowCount(); row++){
            isChecked =  table.item(row, col).checkState()==Qt.CheckState.Checked;
            if (isChecked){
                allUnchecked = false;
            }else{
                allChecked = false;
            }
        }
        if (allChecked){
            horizontalHeader.setSectionCheckState(col, Qt.CheckState.Checked);
        }else if (allUnchecked){
            horizontalHeader.setSectionCheckState(col, Qt.CheckState.Unchecked);
        }else{
            horizontalHeader.setSectionCheckState(col, Qt.CheckState.PartiallyChecked);
        }        
    }
    
    private void updatePrecisionEditors(){
        for (int row=0; row<table.rowCount(); row++){
            updatePrecisionEditor(row);
        }
    }
    
    private void updatePrecisionEditor(final int row){
        for (int col=1; col<table.columnCount()-1; col++){
            if (table.horizontalHeaderItem(col).data(Qt.ItemDataRole.UserRole)==EAggregateFunction.AVG){
                getPrecisionEditor(row).setEnabled(table.item(row, col).checkState()==Qt.CheckState.Checked);
                break;
            }
        }
    }
    
    public List<AggregateFunctionCall> getAggregateFunctions(){
        return Collections.unmodifiableList(aggregateFunctions);
    }
    
    private QTableWidgetItem createHHeaderCell(final String text, final EAggregateFunction function){
        final QTableWidgetItem cell = createHeaderCell(text);
        if (function!=null){
            cell.setData(Qt.ItemDataRole.UserRole, function);
        }
        return cell;
    }
    
    private QTableWidgetItem createVHeaderCell(final String text, final Id columnId){
        final QTableWidgetItem cell = createHeaderCell(text);
        cell.setFlags(Qt.ItemFlag.ItemIsEnabled);
        cell.setData(Qt.ItemDataRole.UserRole, columnId);
        return cell;
    }    
    
    private QTableWidgetItem createCell(final boolean checked, final Id columnId){
        final QTableWidgetItem cell = new QTableWidgetItem();
        cell.setFlags(Qt.ItemFlag.ItemIsEditable, Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsUserCheckable);
        cell.setCheckState(checked ? Qt.CheckState.Checked : Qt.CheckState.Unchecked);        
        cell.setData(Qt.ItemDataRole.UserRole, columnId);
        return cell;
    }

    private QTableWidgetItem createHeaderCell(final String text){
        final QTableWidgetItem cell = new QTableWidgetItem(text);
        cellOptions.applyTo(cell);
        return cell;
    }
    
    private boolean isSomeFunctionSelected(){
        if (cbRowsCount.checkState()==Qt.CheckState.Checked){
            return true;
        }
        for (int row=0; row<table.rowCount(); row++){
            for (int column=1; column<table.columnCount()-1; column++){
                if (table.item(row, column).checkState()==Qt.CheckState.Checked){
                    return true;
                }
            }
        }
        return false;
    }
    
    private void updateOKButton(){
        getButton(EDialogButtonType.OK).setEnabled(isSomeFunctionSelected());
    }
    
    private ValIntEditor createPrecisionEditor(final RadPropertyDef propertyDef){
        final ValIntEditor editor = new ValIntEditor(getEnvironment(), table, precisionEditMask, true, false);
        if (propertyDef.getEditMask() instanceof EditMaskNum){
            final EditMaskNum propertyEditMask = (EditMaskNum)propertyDef.getEditMask();
            if (propertyEditMask.getPrecision()>-1){
                editor.setValue(Long.valueOf(propertyEditMask.getPrecision()));
            }else{
                editor.setValue(Long.valueOf(AggregateFunctionCall.DEFAULT_PRECISION));
            }
        }else{
            editor.setValue(Long.valueOf(AggregateFunctionCall.DEFAULT_PRECISION));
        }
        return editor;
    }
    
    private int getPrecision(final int row){
        final ValIntEditor editor = getPrecisionEditor(row);
        return editor.isEnabled() ? editor.getValue().intValue() : -1;
    }
    
    private void setPrecision(final int row, final int precision){
        getPrecisionEditor(row).setValue(Long.valueOf(precision));
    }
    
    private ValIntEditor getPrecisionEditor(final int row){
        return (ValIntEditor)table.cellWidget(row, table.columnCount()-1);
    }
    
    private void writeToConfig(final List<AggregateFunctionCall> functions){
        if (!functions.isEmpty()){
            final ClientSettings settings = getEnvironment().getConfigStore();
            settings.beginGroup(groupModel.getDefinition().getId().toString());
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.SELECTOR_GROUP);
            try{
                settings.writeString(SETTINGS_KEY, AggregateFunctionCall.writeToString(functions));
            }finally{
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
            }
        }
    }
    
    private void readFromConfig(){
        final ClientSettings settings = getEnvironment().getConfigStore();
        settings.beginGroup(groupModel.getDefinition().getId().toString());
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.SELECTOR_GROUP);
        final String functionsAsStr;
        try{
            functionsAsStr = settings.readString(SETTINGS_KEY, "");
        }finally{
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
        if (functionsAsStr!=null && !functionsAsStr.isEmpty()){
            final List<AggregateFunctionCall> functions;        
            try{
                functions = AggregateFunctionCall.parseListFromString(functionsAsStr);
            }catch(WrongFormatException exception){                
                getEnvironment().getTracer().debug(exception);
                return;
            }
            
            for (int row=0; row<table.rowCount(); row++){
                final Id columnId = (Id)table.item(row, 0).data(Qt.ItemDataRole.UserRole);
                for (int column=1; column<table.columnCount()-1; column++){
                    final EAggregateFunction function = 
                        (EAggregateFunction)table.horizontalHeaderItem(column).data(Qt.ItemDataRole.UserRole);                    
                    if (AggregateFunctionCall.findFunctionCall(columnId, function, functions)>-1){
                        table.item(row, column).setCheckState(Qt.CheckState.Checked);
                    }else{
                        table.item(row, column).setCheckState(Qt.CheckState.Unchecked);
                    }
                }                                
                final int precision = AggregateFunctionCall.getPrecisionForColumn(columnId, functions);
                setPrecision(row, precision);                
            }            
            
            boolean hasRowCountCall = false;
            for (AggregateFunctionCall function: functions){
                if (function.getFunction()==EAggregateFunction.COUNT){
                    hasRowCountCall = true;
                    break;
                }
            }
            cbRowsCount.setCheckState(hasRowCountCall ? Qt.CheckState.Checked : Qt.CheckState.Unchecked);
        }
    }
    
}
