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

package org.radixware.wps.views.selector;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.types.AggregateFunctionCall;
import org.radixware.kernel.common.client.types.SelectorColumnsStatistic;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.enums.EAggregateFunction;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.exceptions.WrongFormatException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.CheckBox;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.Grid;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.IGrid.ESelectionStyle;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBoxContainer;
import org.radixware.wps.text.WpsTextOptions;
import org.radixware.wps.views.editors.valeditors.IValEditor;


public class SelectionStatisticParamsDialog extends Dialog{
    
    private final static class CheckableCellRenderer extends Grid.EditMaskRenderer{
                       
        public CheckableCellRenderer(final IClientEnvironment environment, Grid grid, final Grid.Cell cell){
            super(new EditMaskBool(), grid, environment, cell);
        }

        @Override
        protected IValEditor createValEditor() {
            final IValEditor editor = super.createValEditor();
            //center horizontally
            ((UIObject) editor).getHtml().setCss("width", "20px");
            ((UIObject) editor).getHtml().setCss("margin", "auto");            
            //center vertically
            ((UIObject) editor).getHtml().setCss("padding-top", "3px");
            return editor;
        }
    }
    
    private final class RendererProvider implements IGrid.CellRendererProvider{                
        
        private final EditMask precisionEditMask = 
            new EditMaskInt(0l,Long.valueOf(Integer.MAX_VALUE),(byte)0,null,1l,null,(byte)10);

        @Override
        public IGrid.CellRenderer newCellRenderer(final int r, final int c) {
            if (c==0){
                return new Grid.DefaultRenderer();
            }
            final Grid ownerGrid = SelectionStatisticParamsDialog.this.table;
            final IClientEnvironment env = SelectionStatisticParamsDialog.this.getEnvironment();
            final Grid.Cell cell = ownerGrid.getRow(r).getCell(c);
            if (c<ownerGrid.getColumnCount()-1){
                return new CheckableCellRenderer(env, ownerGrid, cell);
            }else{
                return new Grid.EditMaskRenderer(precisionEditMask, table, env, cell);
            }            
        }
        
    }

    private final static String SETTINGS_KEY = "statistic_params";
    
    private final GroupModel groupModel;
    private final List<Id> columnsOrder;
    private final WpsTextOptions cellOptions;
    private final Grid table = new Grid();
    private final CheckBox cbRowsCount = new CheckBox();
    private final IGrid.SelectableColumnHeaderCell.SelectionListener columnSelectionListener = new IGrid.SelectableColumnHeaderCell.SelectionListener(){
        @Override
        public void selectionChanged(final IGrid.SelectableColumnHeaderCell cell, 
                                                    final Boolean isSelected,
                                                    final boolean changedByUser) {
            if (changedByUser && isSelected!=null){
                final EAggregateFunction function = (EAggregateFunction)cell.getUserData();
                onColumnSelectionChanged(function.ordinal()+1, isSelected);
            }
        }
    };
    
    private boolean internalCellChange;
    
    public SelectionStatisticParamsDialog(final GroupModel groupModel,                                                             
                                                            final List<Id> columnsOrder){
        super(((WpsEnvironment)groupModel.getEnvironment()).getDialogDisplayer(),
                groupModel.getDefinition().getId().toString()+SETTINGS_KEY,
                true,
                new Dialog.DialogGeometry(600, 300, -1, -1));
        this.groupModel = groupModel;        
        this.columnsOrder = columnsOrder; 
        cellOptions = 
            (WpsTextOptions)getEnvironment().getTextOptionsProvider().getOptions(EnumSet.of(ETextOptionsMarker.REGULAR_VALUE, ETextOptionsMarker.SELECTOR_ROW), ESelectorRowStyle.NORMAL);        
        setupUi();
    }  
    
    private void setupUi(){
        final MessageProvider mp = getEnvironment().getMessageProvider();
        setWindowTitle(mp.translate("Selector", "Statistics for Selection"));        
        setWindowIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Selector.CALC_STATISTIC));
        table.setHeaderVisible(true);
        table.setSelectionMode(Grid.SelectionMode.NONE);
        table.setSelectionStyle(EnumSet.noneOf(ESelectionStyle.class));
        table.setRendererProvider(new RendererProvider());
        table.addColumn(mp.translate("Selector", "Column")).setSizePolicy(IGrid.EColumnSizePolicy.WEAK_BY_CONTENT);
        final EditMaskBool cellEditMask = new EditMaskBool();
        String fuctionTitle;
        for(EAggregateFunction function: EAggregateFunction.values()){
            if (function!=EAggregateFunction.COUNT){
                fuctionTitle = SelectorColumnsStatistic.getAggregationFunctionTitle(function, mp);
                final IGrid.AbstractColumn column =                         
                    table.addColumn(ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), fuctionTitle),
                                              createSelectableHeaderCell());
                column.setUserData(function);
                column.setSizePolicy(IGrid.EColumnSizePolicy.BY_CONTENT);
                column.getEditingOptions().setEditingMode(IGrid.ECellEditingMode.NULL_VALUE_RESTRICTED);
                column.getEditingOptions().setEditMask(cellEditMask);
            }
        }
        final IGrid.AbstractColumn precisionColumn = table.addColumn(mp.translate("Selector", "Values Accuracy"));                
        precisionColumn.setSizePolicy(IGrid.EColumnSizePolicy.STRETCH);
        precisionColumn.getEditingOptions().setEditingMode(IGrid.ECellEditingMode.NULL_VALUE_RESTRICTED);        
        for (Id columnId: columnsOrder){
            final SelectorColumnModelItem column = groupModel.getSelectorColumn(columnId);
            final RadPropertyDef propertyDef = column.getPropertyDef();
            final Grid.Row row = table.addRow();
            row.getCell(0).setValue(column.getTitle());
            row.getCell(0).setUserData(columnId);          
            for (int col=1; col<table.getColumnCount()-1; col++){
                row.getCell(col).setValue(Boolean.FALSE);
                row.getCell(col).setUserData(columnId);
            }
            row.getCell(table.getColumnCount()-1).setValue(getPropertyPrecision(propertyDef));
        }  
        final VerticalBoxContainer dialogLayout = new VerticalBoxContainer();
        add(dialogLayout);
        dialogLayout.getAnchors().setTop(new Anchors.Anchor(0, 3));
        dialogLayout.getAnchors().setLeft(new Anchors.Anchor(0, 3));
        dialogLayout.getAnchors().setBottom(new Anchors.Anchor(1, -3));
        dialogLayout.getAnchors().setRight(new Anchors.Anchor(1, -3));        
        dialogLayout.add(table);
        dialogLayout.setAutoSize(table, true);
        dialogLayout.addSpace();
        cbRowsCount.setText(mp.translate("Selector", "Calculate number of rows"));
        cbRowsCount.setSelected(true);
        cbRowsCount.addSelectionStateListener(new CheckBox.SelectionStateListener() {
            @Override
            public void onSelectionChange(CheckBox cb) {
                updateOKButton();
            }
        });
        dialogLayout.add(cbRowsCount);
        addCloseAction(EDialogButtonType.OK);
        addCloseAction(EDialogButtonType.CANCEL);
        readFromConfig();
        table.addCellValueChangeListener(new Grid.CellValueChangeListener() {
            @Override
            public void onValueChanged(final Grid.Cell cell, final Object oldValue, final Object newValue) {
                onCellChanged(table.getRowIndex(cell.getRow()), cell.getCellIndex());                
            }
        });
        updateHeaderCheck();
        updatePrecisionEditors();
        updateOKButton();        
    }

    @Override
    protected DialogResult onClose(final String action, final DialogResult actionResult) {
         if (actionResult==DialogResult.ACCEPTED){
             writeToConfig(getAggregateFunctions());
         }
         return actionResult;
    }
    
    public List<AggregateFunctionCall> getAggregateFunctions(){
        final List<AggregateFunctionCall> aggregateFunctions = new LinkedList<>();        
        for (int row=0; row<table.getRowCount(); row++){
            final int precision = getPrecision(row);
            for (int column=1; column<table.getColumnCount()-1; column++){
                final EAggregateFunction function = 
                    (EAggregateFunction)table.getColumn(column).getUserData();
                final Grid.Cell cell = table.getRow(row).getCell(column);
                if (cell.getValue()==Boolean.TRUE){
                    final Id columnId = (Id)cell.getUserData();
                    aggregateFunctions.add(new AggregateFunctionCall(columnId, function, precision));
                }
            }
        }
        if (cbRowsCount.isSelected()){
            aggregateFunctions.add(new AggregateFunctionCall(null, EAggregateFunction.COUNT));
        }
        return aggregateFunctions;
    }
    
    private boolean isSomeFunctionSelected(){
        if (cbRowsCount.isSelected()){
            return true;
        }
        for (int row=0; row<table.getRowCount(); row++){
            for (int column=1; column<table.getColumnCount()-1; column++){                
                if (table.getRow(row).getCell(column).getValue()==Boolean.TRUE){
                    return true;
                }
            }
        }
        return false;
    }    
        
    private void onCellChanged(final int row, final int col){
        if (!internalCellChange && col<table.getColumnCount()-1){
            updateHeaderCheck(col);
            updatePrecisionEditor(row);
            updateOKButton();
        }
    }    
    
    private void onColumnSelectionChanged(final int colIndex, final boolean isSelected){        
        internalCellChange = true;
        try{
            for (int row=0; row<table.getRowCount(); row++){
                table.getRow(row).getCell(colIndex).setValue(isSelected);
                updatePrecisionEditor(row);
            }
        }finally{
            internalCellChange = false;
        }
        updateOKButton();
    }
    
    private void updateHeaderCheck(){
        for (int col=1; col<table.getColumnCount()-1; col++){
            updateHeaderCheck(col);
        }
    }        
    
    private void updateHeaderCheck(final int col){
        boolean allChecked = true;
        boolean allUnchecked = true;
        boolean isChecked;
        for (int row=0; row<table.getRowCount(); row++){
            isChecked =  table.getRow(row).getCell(col).getValue()==Boolean.TRUE;
            if (isChecked){
                allUnchecked = false;
            }else{
                allChecked = false;
            }
        }
        final IGrid.SelectableColumnHeaderCell headerCell =
            (IGrid.SelectableColumnHeaderCell)table.getColumn(col).getHeaderCell();
        if (allChecked){
            headerCell.setSelected();            
        }else if (allUnchecked){
            headerCell.setUnselected();
        }else{
            headerCell.setPartiallySelected();
        }
    }
    
    private void updatePrecisionEditors(){
        for (int row=0; row<table.getRowCount(); row++){
            updatePrecisionEditor(row);
        }
    }        
    
    private void updatePrecisionEditor(final int row){
        for (int col=1; col<table.getColumnCount()-1; col++){
            if (table.getColumn(col).getUserData()==EAggregateFunction.AVG){
                final boolean isPrecisionEnabled = table.getRow(row).getCell(col).getValue()==Boolean.TRUE;
                final Grid.Cell precisionCell = table.getRow(row).getCell(table.getColumnCount()-1);                
                precisionCell.getEditingOptions().setEditingMode(isPrecisionEnabled ? IGrid.ECellEditingMode.NULL_VALUE_RESTRICTED : IGrid.ECellEditingMode.READ_ONLY);
                precisionCell.setEnabled(isPrecisionEnabled);
                break;
            }
        }
    }    
    
    private void updateOKButton(){
        setActionEnabled(EDialogButtonType.OK, isSomeFunctionSelected());
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
            
            for (int r=0; r<table.getRowCount(); r++){
                final Grid.Row row = table.getRow(r);
                final Id columnId = (Id)row.getUserData();
                for (int column=1; column<table.getColumnCount()-1; column++){
                    final EAggregateFunction function = 
                        (EAggregateFunction)table.getColumn(column).getUserData();
                    final boolean isChecked = 
                        AggregateFunctionCall.findFunctionCall(columnId, function, functions)>-1;
                    row.getCell(column).setValue(isChecked);
                }
                final int precision = AggregateFunctionCall.getPrecisionForColumn(columnId, functions);
                setPrecision(r, precision);
            }            
            
            boolean hasRowCountCall = false;
            for (AggregateFunctionCall function: functions){
                if (function.getFunction()==EAggregateFunction.COUNT){
                    hasRowCountCall = true;
                    break;
                }
            }
            cbRowsCount.setSelected(hasRowCountCall);
        }
        
    }
    
    private int getPrecision(final int row){
        return ((Long)table.getRow(row).getCell(table.getColumnCount()-1).getValue()).intValue();
    }
    
    private void setPrecision(final int row, final int precision){
        table.getRow(row).getCell(table.getColumnCount()-1).setValue(Long.valueOf(precision));        
    }    
    
    private IGrid.AbstractColumnHeaderCell createSelectableHeaderCell(){
        final IGrid.SelectableColumnHeaderCell cell = new IGrid.SelectableColumnHeaderCell();
        cell.addSelectionListener(columnSelectionListener);
        return cell;
    }
    
    private static Long getPropertyPrecision(final RadPropertyDef propertyDef){
        if (propertyDef.getEditMask() instanceof EditMaskNum){
            final EditMaskNum propertyEditMask = (EditMaskNum)propertyDef.getEditMask();
            if (propertyEditMask.getPrecision()>-1){
                return Long.valueOf(propertyEditMask.getPrecision());
            }else{
                return Long.valueOf(AggregateFunctionCall.DEFAULT_PRECISION);
            }
        }else{
            return Long.valueOf(AggregateFunctionCall.DEFAULT_PRECISION);
        }
    }    
}