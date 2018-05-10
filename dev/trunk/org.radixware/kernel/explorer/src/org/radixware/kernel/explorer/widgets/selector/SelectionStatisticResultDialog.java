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

import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.types.SelectorColumnsStatistic;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EAggregateFunction;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.widgets.AutoResizingTable;


public class SelectionStatisticResultDialog extends ExplorerDialog{        
    
    private final static class StatisticTable extends AutoResizingTable implements SelectorColumnsStatistic.ITable{
                
        private final ExplorerTextOptions headerCellOptions;
        private final ExplorerTextOptions cellOptions;
        
        public final QSignalEmitter.Signal0 onCornerClick = new QSignalEmitter.Signal0();
        
        public StatisticTable(final QWidget parent, final ExplorerTextOptions cellOptions){
            super(parent);
            this.headerCellOptions = cellOptions;
            this.cellOptions = cellOptions.changeAlignment(ETextAlignment.RIGHT);            
            setVisible(false);
        }

        @Override
        public void setDimension(final int rowCount, final int colCount) {            
            clear();
            clearSpans();
            setRowCount(rowCount);
            setColumnCount(colCount);
            setVisible(rowCount>0 && colCount>0);
            verticalHeader().setVisible(false);
            horizontalHeader().setVisible(false);
            setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectItems);
            setSelectionMode(QAbstractItemView.SelectionMode.NoSelection);
            verticalHeader().setResizeMode(QHeaderView.ResizeMode.ResizeToContents);
        }

        @Override
        public void addTitledRow(final int index, final String title) {
            setVerticalHeaderItem(index, createHeaderCell(title));            
            if (title!=null && !title.isEmpty()){
                verticalHeader().setVisible(true);
            }
        }

        @Override
        public void addTitledColumn(final int index, final String title) {
            horizontalHeader().setVisible(true);
            setHorizontalHeaderItem(index, createHeaderCell(title));
            horizontalHeader().setResizeMode(index, QHeaderView.ResizeMode.ResizeToContents);
        }

        @Override
        public void setCellData(final int rowIndex, final int colIndex, final String data) {
            setItem(rowIndex, colIndex, createCell(data));
        }

        @Override
        public void setColSpan(final int rowIndex, final int colIndex, final int span) {
            setSpan(rowIndex, 0, 1, span);
        }
        
        private QTableWidgetItem createCell(final String text){
            final QTableWidgetItem cell = new QTableWidgetItem(text);
            cellOptions.applyTo(cell);
            return cell;
        }
        
        private QTableWidgetItem createHeaderCell(final String text){
            final QTableWidgetItem cell = new QTableWidgetItem(text);
            headerCellOptions.applyTo(cell);
            return cell;            
        }

        @Override
        public void selectAll() {
            onCornerClick.emit();
        }
    }
    
    private final static String DEFAULT_DIR_SETTING_NAME = SettingNames.SYSTEM+"/"+
                                                                                            SettingNames.SELECTOR_GROUP+"/"+
                                                                                            SettingNames.Selector.SELECTION_STATISTIC_EXPORT_DIR;
    
    private final GroupModel groupModel;
    private final SelectorColumnsStatistic statistic;
    private final List<Id> columnsOrder;
    private final Map<Id,Integer> precisionByColumnId;
    private final QLabel lbRowsCount = new QLabel(this);
    private final StatisticTable table;
    private boolean isLandscape = true;

    public SelectionStatisticResultDialog(final GroupModel groupModel, 
                                                          final QWidget parent, 
                                                          final SelectorColumnsStatistic statistic,
                                                          final List<Id> columnsOrder,
                                                          final Map<Id,Integer> precisionByColumnId,
                                                          final boolean isLandscape){
        super(groupModel.getEnvironment(),parent,groupModel.getDefinition().getId().toString()+"result_statistic",600,300);
        this.groupModel = groupModel;
        this.statistic = statistic;
        this.columnsOrder = columnsOrder;
        this.precisionByColumnId = Collections.unmodifiableMap(precisionByColumnId);
        this.isLandscape = isLandscape;
        final ExplorerTextOptions cellOptions = 
            (ExplorerTextOptions)getEnvironment().getTextOptionsProvider().getOptions(EnumSet.of(ETextOptionsMarker.REGULAR_VALUE, ETextOptionsMarker.SELECTOR_ROW), ESelectorRowStyle.NORMAL);
        table = new StatisticTable(this,cellOptions);
        setupUi();
    }
    
    private void setupUi(){
        setWindowTitle(getEnvironment().getMessageProvider().translate("Selector", "Statistics for Selection"));        
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Selector.CALC_STATISTIC));
        statistic.writeToTable(groupModel, columnsOrder, precisionByColumnId, table, isLandscape);
        if (isLandscape){
            table.verticalHeader().setVisible(false);
            table.horizontalHeader().setResizeMode(0, QHeaderView.ResizeMode.Interactive);
            for (int row=table.rowCount()-1; row>=0; row--){
                table.item(row, 0).setTextAlignment(Qt.AlignmentFlag.AlignLeft.value());
            }
        }
        dialogLayout().addWidget(table);
        table.resizeColumnToContents(0);
        dialogLayout().addWidget(lbRowsCount);
        updateRowCountLabel();
        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        addButton(EDialogButtonType.SAVE).addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(final IButton source) {
                onSaveButtonClick();
            }
        });        
        addButton(EDialogButtonType.CLOSE);
        rejectButtonClick.connect(this, "reject()");        
    }      
    
    private void updateRowCountLabel(){
        if (isLandscape && statistic.getRowsCount()>-1){
            lbRowsCount.setVisible(true);
            final String rowCountText = 
                SelectorColumnsStatistic.getAggregationFunctionTitle(EAggregateFunction.COUNT, getEnvironment().getMessageProvider())
                +": "+String.valueOf(statistic.getRowsCount());
            final ExplorerTextOptions labelOptions = 
                (ExplorerTextOptions)getEnvironment().getTextOptionsProvider().getOptions(EnumSet.of(ETextOptionsMarker.REGULAR_VALUE, ETextOptionsMarker.LABEL), null);
            lbRowsCount.setText(rowCountText);
            labelOptions.applyTo(lbRowsCount);
        }else{
            lbRowsCount.setVisible(false);
        }      
    }
    
    private void onSaveButtonClick(){
        final MessageProvider mp = getEnvironment().getMessageProvider();
        final String title = mp.translate("Selector", "Save to file");
        final String filter = mp.translate("Selector", "Html files (*.html)");        
        final String defaultDir = 
            getEnvironment().getConfigStore().readString(DEFAULT_DIR_SETTING_NAME,System.getProperty("user.home"));        
        final String fileName = QFileDialog.getSaveFileName(this, title, defaultDir, new QFileDialog.Filter(filter));
        if (fileName==null || fileName.isEmpty()) {
            return;
        }        
        final String fileExt = FileUtils.getFileExt(fileName);
        final File file = fileExt.isEmpty() ? new File(fileName+".html") : new File(fileName);
        try{
            FileUtils.writeToFile(file, statistic.exportToHtml(groupModel, columnsOrder, precisionByColumnId, isLandscape), "UTF-8");
        }catch(IOException exception){
            final String errorTitle = mp.translate("Selector", "Failed to Export Selection Statistic");
            getEnvironment().processException(errorTitle, exception);
        }
        getEnvironment().getConfigStore().writeString(DEFAULT_DIR_SETTING_NAME, file.getAbsolutePath());
    }
    
    @SuppressWarnings("unused")
    private void onTableCornerClick(){
        isLandscape = !isLandscape;
        statistic.writeToTable(groupModel, columnsOrder, precisionByColumnId, table, isLandscape);
        updateRowCountLabel();
    }
}
