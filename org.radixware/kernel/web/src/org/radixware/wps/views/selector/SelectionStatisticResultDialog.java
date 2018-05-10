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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.types.SelectorColumnsStatistic;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EAggregateFunction;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.Grid;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.VerticalBoxContainer;
import org.radixware.wps.text.WpsTextOptions;


public class SelectionStatisticResultDialog extends Dialog{
    
    private final static class StatisticTable extends Grid implements SelectorColumnsStatistic.ITable{

        private final WpsTextOptions cellOptions;
        
        public StatisticTable(final WpsTextOptions options){
            cellOptions = options;
        }
        
        @Override
        public void setDimension(int rowCount, int colCount) {
            this.clearRows();
            for (int i=getColumnCount()-1; i>=0; i--){
                removeColumn(i);
            }
            setVisible(false);
            setVisible(rowCount>0 && colCount>0);     
            setRowHeaderVisible(false);
            setHeaderVisible(false);
            setSelectionMode(SelectionMode.NONE);
            setSelectionStyle(EnumSet.noneOf(ESelectionStyle.class));            
        }

        @Override
        public void addTitledRow(final int index, final String title) {
            insertRow(index, title);
            if (title!=null && !title.isEmpty()){
                setRowHeaderVisible(true);
            }            
        }

        @Override
        public void addTitledColumn(final int index, final String title) {
            final Grid.Column column = addColumn(index, title);
            if (title!=null && !title.isEmpty()){
                setHeaderVisible(true);
            }
            column.setSizePolicy(EColumnSizePolicy.BY_CONTENT);
        }

        @Override
        public void setCellData(final int rowIndex, final int colIndex, final String data) {
            getRow(rowIndex).getCell(colIndex).setValue(data);
            getRow(rowIndex).getCell(colIndex).setTextOptions(cellOptions);
        }

        @Override
        public void setColSpan(final int rowIndex, final int colIndex, final int span) {
            final Row row = getRow(rowIndex);
            final String rowTitle = row.getTitle();
            removeRow(row);
            addRowWithSpannedCells(rowTitle);            
        }        
    }
    
    private final GroupModel groupModel;
    private final SelectorColumnsStatistic statistic;
    private final List<Id> columnsOrder;
    private final Map<Id,Integer> precisionByColumnId;
    private final Label lbRowsCount = new Label();
    private final StatisticTable table;

    public SelectionStatisticResultDialog(final GroupModel groupModel, 
                                                          final SelectorColumnsStatistic statistic,
                                                          final List<Id> columnsOrder,
                                                          final Map<Id,Integer> precisionByColumnId){
        super(((WpsEnvironment)groupModel.getEnvironment()).getDialogDisplayer(),
                groupModel.getDefinition().getId().toString()+"result_statistic",
                true,
                new Dialog.DialogGeometry(600, 300, -1, -1));        
        this.groupModel = groupModel;
        this.statistic = statistic;
        this.columnsOrder = columnsOrder;
        this.precisionByColumnId = Collections.unmodifiableMap(precisionByColumnId);        
        
        final WpsTextOptions cellOptions = 
            (WpsTextOptions)getEnvironment().getTextOptionsProvider().getOptions(EnumSet.of(ETextOptionsMarker.REGULAR_VALUE, ETextOptionsMarker.SELECTOR_ROW), ESelectorRowStyle.NORMAL);
        table = new StatisticTable(cellOptions.changeAlignment(ETextAlignment.RIGHT));
        setupUi(cellOptions);
    }
    
    private void setupUi(final WpsTextOptions cellOptions){        
        setWindowTitle(getEnvironment().getMessageProvider().translate("Selector", "Statistics for Selection"));        
        setWindowIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Selector.CALC_STATISTIC));
        statistic.writeToTable(groupModel, columnsOrder, precisionByColumnId, table, true);
        table.getColumn(0).setSizePolicy(IGrid.EColumnSizePolicy.WEAK_BY_CONTENT);
        for (int row=table.getRowCount()-1; row>=0; row--){
            table.getRow(row).getCell(0).setTextOptions(cellOptions.changeAlignment(ETextAlignment.LEFT));
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
        dialogLayout.add(lbRowsCount);        
        updateRowCountLabel();
        addCloseAction(EDialogButtonType.SAVE).addClickHandler(new IButton.ClickHandler(){
            @Override
            public void onClick(final IButton source) {
                onSaveButtonClick();
            }            
        });
        addCloseAction(EDialogButtonType.CLOSE);
    }

    @Override
    protected DialogResult onClose(String action, DialogResult actionResult) {
        return super.onClose(action, actionResult); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    private void updateRowCountLabel(){
        if (statistic.getRowsCount()>-1){
            lbRowsCount.setVisible(true);
            final String rowCountText = 
                SelectorColumnsStatistic.getAggregationFunctionTitle(EAggregateFunction.COUNT, getEnvironment().getMessageProvider())
                +": "+String.valueOf(statistic.getRowsCount());
            final WpsTextOptions labelOptions = 
                (WpsTextOptions)getEnvironment().getTextOptionsProvider().getOptions(EnumSet.of(ETextOptionsMarker.REGULAR_VALUE, ETextOptionsMarker.LABEL), null);
            lbRowsCount.setTextOptions(labelOptions);
            lbRowsCount.setText(rowCountText);            
        }else{
            lbRowsCount.setVisible(false);
        }      
    }    
    
    private static String convertToShortName(final String fullName){
        final String parts[] = fullName.split("::");
        if (parts!=null && parts.length>1){
            for (int i=parts.length-1; i>0; i--){
                if (!parts[i].isEmpty()){
                    return parts[i];
                }
            }
        }
        return fullName;
    }
    
    private void onSaveButtonClick(){
        final String className = convertToShortName(groupModel.getSelectorPresentationDef().getClassPresentation().getName());
        final String fileName = className+"_statistic.html";
        final File file = RadixLoader.getInstance().createTempFile(fileName);        
        try{
            FileUtils.writeToFile(file, statistic.exportToHtml(groupModel, columnsOrder, precisionByColumnId, true), "UTF-8");
        }catch(IOException exception){
            final String errorTitle = getEnvironment().getMessageProvider().translate("Selector", "Failed to Export Selection Statistic");
            getEnvironment().processException(errorTitle, exception);
        }
        ((WpsEnvironment)getEnvironment()).sendFileToTerminal(fileName, file, EMimeType.HTML_TEXT.getValue(), false, true);
    }
}
