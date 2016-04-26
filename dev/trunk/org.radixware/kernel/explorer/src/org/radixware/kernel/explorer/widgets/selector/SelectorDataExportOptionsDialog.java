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

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.FileException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.types.PropertyValuesWriteOptions;
import org.radixware.kernel.common.client.utils.CsvWriter;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.ESelectorColumnVisibility;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValListEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.utils.WidgetUtils;


final class SelectorDataExportOptionsDialog extends ExplorerDialog{
    
    private final static int TITLE_COLUMN_INDEX = 0;
    private final static int FORMAT_COLUMN_INDEX = 1;
    private final static int CHECK_COLUMN_INDEX = 2;

    private final QLabel lbFile = 
        new QLabel(getEnvironment().getMessageProvider().translate("Selector", "&File path:"), this);    
    private final ValStrEditor veFile = new ValStrEditor(getEnvironment(), this);
    private final ValIntEditor veMaxRows = new ValIntEditor(getEnvironment(), this);
    private final ValListEditor veEncoding;
    private final QGroupBox gbColumns = 
        new QGroupBox(getEnvironment().getMessageProvider().translate("Selector", "Choose columns to export:"),this);
    private final QTableWidget tbColumns = new QTableWidget(gbColumns){

        @Override
        protected void keyPressEvent(final QKeyEvent event) {
            final boolean isControl = 
                event.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value();
            final int currentRow = tbColumns.currentRow();
            if (event.key()==Qt.Key.Key_Space.value()){
                final QTableWidgetItem checkItem = tbColumns.item(currentRow, CHECK_COLUMN_INDEX);
                final boolean isChecked = checkItem.checkState()==Qt.CheckState.Checked;
                checkItem.setCheckState( isChecked ? Qt.CheckState.Unchecked : Qt.CheckState.Checked);
            }else if (event.key()==Qt.Key.Key_Up.value() && isControl && currentRow>0){
                swapRows(currentRow, currentRow-1);
                tbColumns.setCurrentCell(currentRow-1, TITLE_COLUMN_INDEX);
            }else if (event.key()==Qt.Key.Key_Down.value() && isControl && currentRow<tbColumns.rowCount()-1){
                swapRows(currentRow, currentRow+1);
                tbColumns.setCurrentCell(currentRow+1, TITLE_COLUMN_INDEX);
            }else{
                super.keyPressEvent(event);
            }
        }
        
    };
    private final QCheckBox cbExportTitles = 
        new QCheckBox(getEnvironment().getMessageProvider().translate("Selector", "Export column &headers"), this);
    
    private final QToolButton tbUp = new QToolButton(gbColumns);
    private final QToolButton tbDown = new QToolButton(gbColumns);    
    private final Id selectorPresentationId;        
    
    private String initialResulFileDirectory;
    private PropertyValuesWriteOptions resultWriteOptions;
    private CsvWriter.FormatOptions resultCsvFormatOptions;    
    private File resultFile;
    private int maxRows;
    private boolean exportColumnTitles;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public SelectorDataExportOptionsDialog(final GroupModel group, final QWidget widget){
        super(group.getEnvironment(), widget);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        final List<EditMaskList.Item> encodingItems = new LinkedList<>();
        final Map<String,Charset> allCharsets = Charset.availableCharsets();
        String currentCharsetName = "UTF-8";
        for (Map.Entry<String,Charset> entry: allCharsets.entrySet()){            
            encodingItems.add(new EditMaskList.Item(entry.getValue().displayName(), entry.getKey()));
            if (Charset.defaultCharset().name().equals(entry.getKey())){
               currentCharsetName = entry.getKey();
            }
        }
        veEncoding = new ValListEditor(getEnvironment(), this, encodingItems);
        veEncoding.setValue(currentCharsetName);
        setupUi();
        selectorPresentationId = group.getDefinition().getId();
        final Map<Id,PropertyValuesWriteOptions.ExportFormat> restoredFormats = new HashMap<>();
        final List<Id> restoredColumnsOrder = restoreOptionsFromConfig(restoredFormats);
        fillColumnsTable(group,restoredColumnsOrder,restoredFormats);
        tbColumns.itemChanged.connect(this,"itemChanged(QTableWidgetItem)");
        tbColumns.setCurrentCell(0, 1);
    }
    
    private void setupUi(){
        final MessageProvider mp = getEnvironment().getMessageProvider();
        setWindowTitle(mp.translate("Selector", "Export Options"));
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Selector.EXPORT));
        final QGridLayout layout = new QGridLayout();
        {//file path
            ExplorerTextOptions.Factory.getLabelOptions(ETextOptionsMarker.MANDATORY_VALUE).applyTo(lbFile);
            lbFile.setBuddy(veFile);
            layout.addWidget(lbFile, 0, 0);        
            final QAction chooseFileAction = new QAction("...",veFile);
            chooseFileAction.triggered.connect(this,"onChooseFile()");
            veFile.addButton("...", chooseFileAction);
            veFile.valueChanged.connect(this,"filePathChanged()");
            veFile.setMandatory(true);
            layout.addWidget(veFile, 0, 1);
            veFile.setFocus();
        }
        {//encoding
            final QLabel lbEncoding = 
                new QLabel(mp.translate("Selector", "File &character set:"), this);
            WidgetUtils.applyDefaultTextOptions(lbEncoding);            
            lbEncoding.setBuddy(veEncoding);
            layout.addWidget(lbEncoding, 1, 0);
            veEncoding.setMandatory(true);
            layout.addWidget(veEncoding, 1, 1);
        }
        {//max rows
            final QLabel lbMaxRows = 
                new QLabel(mp.translate("Selector", "&Maximum rows:"), this);
            WidgetUtils.applyDefaultTextOptions(lbMaxRows);
            lbMaxRows.setBuddy(veMaxRows);
            layout.addWidget(lbMaxRows, 2, 0);
            final EditMaskInt mask = new EditMaskInt();
            mask.setMinValue(Long.valueOf(1));
            mask.setMaxValue(Long.valueOf(Integer.MAX_VALUE));
            mask.setNoValueStr(mp.translate("Selector", "<unbounded>"));
            veMaxRows.setEditMask(mask);
            layout.addWidget(veMaxRows, 2, 1);
        }
        dialogLayout().addLayout(layout);
        {//columns selector            
            final QVBoxLayout buttonsLayout = new QVBoxLayout(null);
            buttonsLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignCenter, Qt.AlignmentFlag.AlignVCenter));
            buttonsLayout.setContentsMargins(3, 0, 3, 0);
            buttonsLayout.setWidgetSpacing(3);
            tbUp.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.UP));
            tbDown.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DOWN));
            tbUp.clicked.connect(this,"moveColumnUp()");
            tbDown.clicked.connect(this,"moveColumnDown()");
            buttonsLayout.addWidget(tbUp);
            buttonsLayout.addWidget(tbDown);           
            tbColumns.currentItemChanged.connect(this,"currentItemChanged()");
            tbColumns.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
            tbColumns.setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);            
            tbColumns.verticalHeader().setResizeMode(QHeaderView.ResizeMode.ResizeToContents);
            final QHBoxLayout hLayout = WidgetUtils.createHBoxLayout(gbColumns);
            hLayout.addWidget(tbColumns);
            hLayout.addLayout(buttonsLayout);
            ExplorerTextOptions.Factory.getLabelOptions(ETextOptionsMarker.REGULAR_VALUE).applyTo(gbColumns);
            gbColumns.setSizePolicy(QSizePolicy.Policy.MinimumExpanding, QSizePolicy.Policy.MinimumExpanding);                        
            dialogLayout().addWidget(gbColumns);
        }
        {//export column titles
            WidgetUtils.applyDefaultTextOptions(cbExportTitles);
            cbExportTitles.setChecked(true);
            dialogLayout().addWidget(cbExportTitles);
        }
        {//tab order setup 
            QWidget.setTabOrder(veFile, veEncoding);
            QWidget.setTabOrder(veEncoding, veMaxRows);
            QWidget.setTabOrder(veMaxRows, tbColumns);
            QWidget.setTabOrder(tbColumns, cbExportTitles);
        }
        setConfirmToReject(true);
        addButton(EDialogButtonType.OK).setEnabled(false);
        addButton(EDialogButtonType.CANCEL);
        acceptButtonClick.connect(this,"onAccept()");
        rejectButtonClick.connect(this,"reject()");
    }
    
    private void fillColumnsTable(final GroupModel group, List<Id> restoredColumnsOrder, Map<Id, PropertyValuesWriteOptions.ExportFormat> restoredFormats){
        tbColumns.setColumnCount(3);
        tbColumns.horizontalHeader().setVisible(true);
        final MessageProvider mp = getEnvironment().getMessageProvider();
        tbColumns.setHorizontalHeaderItem(CHECK_COLUMN_INDEX, createTableHeaderItem(mp.translate("Selector", "Export")));
        tbColumns.setHorizontalHeaderItem(TITLE_COLUMN_INDEX, createTableHeaderItem(mp.translate("Selector", "Column")));
        tbColumns.setHorizontalHeaderItem(FORMAT_COLUMN_INDEX, createTableHeaderItem(mp.translate("Selector", "Output Format")));
        tbColumns.verticalHeader().setVisible(false);
        tbColumns.horizontalHeader().setResizeMode(CHECK_COLUMN_INDEX, QHeaderView.ResizeMode.ResizeToContents);
        tbColumns.horizontalHeader().setResizeMode(TITLE_COLUMN_INDEX, QHeaderView.ResizeMode.Stretch);
        tbColumns.horizontalHeader().setResizeMode(FORMAT_COLUMN_INDEX, QHeaderView.ResizeMode.ResizeToContents);
        tbColumns.horizontalHeader().setDefaultAlignment(Qt.AlignmentFlag.AlignCenter);
        final RadSelectorPresentationDef.SelectorColumns columns = 
                group.getSelectorPresentationDef().getSelectorColumns();
        
        final List<Id> orderedColumnIds = new LinkedList<>();
        for (Id columnId: restoredColumnsOrder){
            final RadSelectorPresentationDef.SelectorColumn column = columns.findColumn(columnId);
            if (column!=null && column.getVisibility()!=ESelectorColumnVisibility.NEVER){
                orderedColumnIds.add(columnId);
            }
        }
        for (RadSelectorPresentationDef.SelectorColumn column: columns){
            if (!orderedColumnIds.contains(column.getPropertyId()) && column.getVisibility()!=ESelectorColumnVisibility.NEVER){
                orderedColumnIds.add(column.getPropertyId());
            }
        }
        for (Id columnId: orderedColumnIds){
            final PropertyValuesWriteOptions.ExportFormat format = restoredFormats.get(columnId);
            final SelectorColumnModelItem column = group.getSelectorColumn(columnId);
            if (!column.isForbidden()){
                final boolean doExport = format!=null || !restoredFormats.containsKey(columnId);
                addSelectorColumn(column,format,doExport);
            }
        }
        tbColumns.resizeRowsToContents();
    }
    
    private QTableWidgetItem createTableHeaderItem(final String title){
        final QTableWidgetItem item = new QTableWidgetItem(title);
        ExplorerTextOptions.getDefault().applyTo(item);
        return item;
    }
    
    private void addSelectorColumn(final SelectorColumnModelItem column, final PropertyValuesWriteOptions.ExportFormat format, final boolean doExport){
        final int row = tbColumns.rowCount();
        tbColumns.setRowCount(row+1);
        {
            final QTableWidgetItem item = new QTableWidgetItem(); 
            item.setFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable);
            ExplorerTextOptions.getDefault().applyTo(item);
            item.setText(column.getTitle());            
            tbColumns.setItem(row, TITLE_COLUMN_INDEX, item);
        }
        {
            final QTableWidgetItem item = new QTableWidgetItem();
            item.setFlags(Qt.ItemFlag.ItemIsEnabled);
            tbColumns.setItem(row, FORMAT_COLUMN_INDEX, item);
            final EValType valType = column.getPropertyDef().getType();
            final boolean isEnum = column.getPropertyDef().getConstSet()!=null;
            tbColumns.setCellWidget(row, FORMAT_COLUMN_INDEX, createFormatEditor(valType, isEnum, format));
        }
        {
            final QTableWidgetItem item = new QTableWidgetItem();
            item.setTextAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignCenter).value());
            item.setFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsUserCheckable);            
            item.setData(Qt.ItemDataRole.UserRole, column.getId());
            tbColumns.setItem(row, CHECK_COLUMN_INDEX, item);
            if (doExport && (format!=null || column.isVisible())){
                item.setCheckState(Qt.CheckState.Checked);
            }else{
                item.setCheckState(Qt.CheckState.Unchecked);
                itemChanged(item);                
            }
        }        
    }
    
    private ValListEditor createFormatEditor(final EValType valType, final boolean isEnum, final PropertyValuesWriteOptions.ExportFormat defaultFormat){
        final List<PropertyValuesWriteOptions.ExportFormat> formatItems = new LinkedList<>();        
        if (valType==EValType.BOOL){
            formatItems.add(PropertyValuesWriteOptions.ExportFormat.BOOLEAN_TO_NUMBER);
            formatItems.add(PropertyValuesWriteOptions.ExportFormat.BOOLEAN_TO_STRING);
        }else{
            formatItems.add(PropertyValuesWriteOptions.ExportFormat.PROPERTY_VALUE);
        }   
        if (!valType.isArrayType()){
            if (isEnum){
                formatItems.add(PropertyValuesWriteOptions.ExportFormat.ENUM_TITLE);
            }
            if (valType==EValType.XML || valType==EValType.BIN || valType==EValType.STR){
                formatItems.add(PropertyValuesWriteOptions.ExportFormat.BASE64);
            }
        }
        if (valType!=EValType.BOOL){
            formatItems.add(PropertyValuesWriteOptions.ExportFormat.DISPLAYED_TEXT);
        }
        final EditMaskList editMask = 
            createEditMaskForExportFormatItems(formatItems, getEnvironment().getMessageProvider());
        final ValListEditor editor = new ValListEditor(getEnvironment(), null, editMask, true, false);
        if (formatItems.contains(defaultFormat)){
            editor.setValue(defaultFormat.asString());
        }else{
            editor.setValue(formatItems.get(0).asString());
        }
        editor.changeStateForGrid();
        editor.valueChanged.connect(this,"onChangeFormat()");
        return editor;
    }
    
    private static EditMaskList createEditMaskForExportFormatItems(final List<PropertyValuesWriteOptions.ExportFormat> formats, final MessageProvider mp){
        final List<EditMaskList.Item> editMaskItems = new LinkedList<>();
        for (PropertyValuesWriteOptions.ExportFormat format: formats){
            final String title = PropertyValuesWriteOptions.ExportFormat.getDisplayName(format, mp);
            editMaskItems.add(new EditMaskList.Item(title, format.asString()));
        }
        return new EditMaskList(editMaskItems);
    }

    private List<Id> restoreOptionsFromConfig(final Map<Id,PropertyValuesWriteOptions.ExportFormat> formatByColumnId){
        final ClientSettings config = getEnvironment().getConfigStore();
        config.beginGroup(SettingNames.SYSTEM+"/"+getClass().getSimpleName());
        try{
            initialResulFileDirectory = config.readString("initialPath", null);
            final String charsetName = config.readString("charset",null);
            if (charsetName!=null && !charsetName.isEmpty() && Charset.isSupported(charsetName)){
                veEncoding.setValue(charsetName);
            }
            config.beginGroup(selectorPresentationId.toString());
            try{
                final int restoredMaxRows = config.readInteger("maxRows", 0);
                if (restoredMaxRows>0){
                    veMaxRows.setValue(Long.valueOf(restoredMaxRows));
                }
                final boolean exportTitles = config.readBoolean("exportTitles", true);
                cbExportTitles.setChecked(exportTitles);
                final int columnsCount = config.beginReadArray("columns");
                try{
                    final List<Id> restoredColumnsOrder = new LinkedList<>();
                    for (int i=0; i<columnsCount; i++){
                        config.setArrayIndex(i);
                        final Id columnId = readColumnOptions(config, formatByColumnId);
                        if (columnId!=null && !restoredColumnsOrder.contains(columnId)){
                            restoredColumnsOrder.add(columnId);
                        }
                    }
                    return restoredColumnsOrder;
                }finally{
                    config.endArray();
                }
            }finally{
                config.endGroup();
            }
        }finally{
            config.endGroup();
        }        
    }
    
    private Id readColumnOptions(final ClientSettings config, final Map<Id,PropertyValuesWriteOptions.ExportFormat> formatByColumnId){
        final Id columnId = config.readId("columnId");
        if (columnId!=null){
            final String formatAsStr = config.readString("format",null);
            if (formatAsStr==null){
                formatByColumnId.put(columnId, null);
            }else{
                final PropertyValuesWriteOptions.ExportFormat format = 
                    PropertyValuesWriteOptions.ExportFormat.getFromString(formatAsStr);
                if (format!=null){
                    formatByColumnId.put(columnId, format);
                }

            }
            return columnId;
        }
        return null;
    }
    
    private void storeOptionsToConfig(){
        final ClientSettings config = getEnvironment().getConfigStore();
        config.beginGroup(SettingNames.SYSTEM+"/"+getClass().getSimpleName());
        try{            
            if (initialResulFileDirectory!=null && !initialResulFileDirectory.isEmpty()){
                config.writeString("initialPath", initialResulFileDirectory);
            }
            config.writeString("charset",String.valueOf(veEncoding.getValue()));
            config.beginGroup(selectorPresentationId.toString());
            try{
                config.writeInteger("maxRows", veMaxRows.getValue()==null ? 0 : veMaxRows.getValue().intValue());
                config.writeBoolean("exportTitles", cbExportTitles.isChecked());
                final int rowsCount = tbColumns.rowCount();
                config.remove("columns");
                config.beginWriteArray("columns");
                try{
                    for (int row=0; row<rowsCount; row++){
                        config.setArrayIndex(row);
                        writeColumnOptions(config,row);
                    }
                }finally{
                    config.endArray();
                }
            }finally{
                config.endGroup();
            }
        }finally{
            config.endGroup();
        }
    }
    
    private void writeColumnOptions(final ClientSettings config, final int row){
        final QTableWidgetItem checkItem = tbColumns.item(row, CHECK_COLUMN_INDEX);
        final Id columnId = (Id)checkItem.data(Qt.ItemDataRole.UserRole);
        config.writeId("columnId", columnId);
        if (checkItem.checkState()== Qt.CheckState.Checked){
            final ValListEditor editor = (ValListEditor)tbColumns.cellWidget(row, FORMAT_COLUMN_INDEX);
            config.writeString("format", (String)editor.getValue());
        }
    }
    
    private void swapRows(final int row1, final int row2){       
        final Id columnId = (Id)tbColumns.item(row1, CHECK_COLUMN_INDEX).data(Qt.ItemDataRole.UserRole);
        final Qt.CheckState checkState = tbColumns.item(row1, CHECK_COLUMN_INDEX).checkState();
        final String text = tbColumns.item(row1,TITLE_COLUMN_INDEX).text();
        final ValListEditor editorRow1 = (ValListEditor)tbColumns.cellWidget(row1, FORMAT_COLUMN_INDEX);
        final ValListEditor editorRow2 = (ValListEditor)tbColumns.cellWidget(row2, FORMAT_COLUMN_INDEX);
        final EditMask mask = editorRow1.getEditMask();
        final String format = (String)editorRow1.getValue();
        
        tbColumns.item(row1,CHECK_COLUMN_INDEX).setData(Qt.ItemDataRole.UserRole,tbColumns.item(row2, CHECK_COLUMN_INDEX).data(Qt.ItemDataRole.UserRole));
        tbColumns.item(row1,CHECK_COLUMN_INDEX).setCheckState(tbColumns.item(row2, CHECK_COLUMN_INDEX).checkState());
        tbColumns.item(row1, TITLE_COLUMN_INDEX).setText(tbColumns.item(row2,TITLE_COLUMN_INDEX).text());
        editorRow1.setEditMask(editorRow2.getEditMask());
        editorRow1.setValue(editorRow2.getValue());

        tbColumns.item(row2, CHECK_COLUMN_INDEX).setData(Qt.ItemDataRole.UserRole,columnId);
        tbColumns.item(row2, CHECK_COLUMN_INDEX).setCheckState(checkState);
        tbColumns.item(row2, TITLE_COLUMN_INDEX).setText(text);
        editorRow2.setEditMask(mask);
        editorRow2.setValue(format);
    }
    
    @SuppressWarnings("unused")
    private void onChooseFile(){
        final String title = getEnvironment().getMessageProvider().translate("Selector", "Export to");
        final String filter = getEnvironment().getMessageProvider().translate("Selector", "CSV files (%s)");
        final QFileDialog.Filter fileFilter = new QFileDialog.Filter(String.format(filter, "*.csv"));
        final String dir;
        if (initialResulFileDirectory!=null && 
            !initialResulFileDirectory.isEmpty() &&
            new File(initialResulFileDirectory).exists()){
            dir = initialResulFileDirectory;
        }else{
            dir = QDir.homePath();
        }
        final String fileName = QFileDialog.getSaveFileName(this, title, dir,fileFilter);
        if (fileName!=null && !fileName.isEmpty()){
            final File file = new File(fileName);
            if (file.getName().indexOf('.')<0){
                veFile.setValue(fileName+".csv");
            }else{        
                veFile.setValue(fileName);
            }
        }
        final File file = new File(fileName).getParentFile();
        if (file!=null && file.exists()){
            initialResulFileDirectory = file.getAbsolutePath();
        }
    }
    
    @SuppressWarnings("unused")
    private void moveColumnUp(){
        swapRows(tbColumns.currentRow(), tbColumns.currentRow()-1);
        tbColumns.setCurrentCell(tbColumns.currentRow()-1, tbColumns.currentColumn());
    }
    
    @SuppressWarnings("unused")
    private void moveColumnDown(){
        swapRows(tbColumns.currentRow(), tbColumns.currentRow()+1);
        tbColumns.setCurrentCell(tbColumns.currentRow()+1, tbColumns.currentColumn());
    }
    
    @SuppressWarnings("unused")
    private void onChangeFormat(){
        tbColumns.resizeColumnToContents(FORMAT_COLUMN_INDEX);
    }
    
    @SuppressWarnings("unused")
    private void itemChanged(final QTableWidgetItem item){
        if (item.flags().isSet(Qt.ItemFlag.ItemIsUserCheckable)){
            final int row = item.row();
            final QTableWidgetItem titleItem = tbColumns.item(row, TITLE_COLUMN_INDEX);
            final ValListEditor editor = (ValListEditor)tbColumns.cellWidget(row, FORMAT_COLUMN_INDEX);
            if (item.checkState()==Qt.CheckState.Checked){
                ExplorerTextOptions.getDefault().applyTo(titleItem);
                editor.setEnabled(true);                
            }else{
                ExplorerTextOptions.getDefault().changeForegroundColor(Color.gray).applyTo(titleItem);
                editor.setEnabled(false);
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void currentItemChanged(){
        tbUp.setEnabled(tbColumns.currentRow()>0);
        tbDown.setEnabled(tbColumns.currentRow()<tbColumns.rowCount()-1);
    }
    
    @SuppressWarnings("unused")
    private void filePathChanged(){
        if (veFile.getValue()!=null && !veFile.getValue().isEmpty()){
            getButton(EDialogButtonType.OK).setEnabled(true);
            WidgetUtils.applyDefaultTextOptions(lbFile);
        }else{
            getButton(EDialogButtonType.OK).setEnabled(false);
            ExplorerTextOptions.Factory.getLabelOptions(ETextOptionsMarker.MANDATORY_VALUE).applyTo(lbFile);
        }
    }
    
    @SuppressWarnings("unused")
    private void onAccept(){
        final String filePath = veFile.getValue();
        resultFile = new File(filePath);
        final String exceptionTitle = 
            getEnvironment().getMessageProvider().translate("Selector", "Failed to Export");
        if (!resultFile.exists()){
            try{
                resultFile.createNewFile();
            }catch(IOException exception){
                getEnvironment().processException(exceptionTitle, new FileException(getEnvironment(), FileException.EExceptionCode.CANT_CREATE, filePath));
                return;
            }
        }
        if (!resultFile.isFile()){
            getEnvironment().processException(exceptionTitle, new FileException(getEnvironment(), FileException.EExceptionCode.NOT_FILE, filePath));
            return;
        }
        if (!resultFile.canWrite()){
            getEnvironment().processException(exceptionTitle, new FileException(getEnvironment(), FileException.EExceptionCode.CANT_WRITE, filePath));
            return;
        }
        resultCsvFormatOptions = new CsvWriter.FormatOptions((String)veEncoding.getValue());
        maxRows = veMaxRows.getValue()==null ? -1 : veMaxRows.getValue().intValue();
        final List<Id> columnIds = new LinkedList<>();
        final Map<Id,String> formatNameByColumnId = new HashMap<>();
        for(int row=0, count=tbColumns.rowCount(); row<count; row++){
            if (tbColumns.item(row, CHECK_COLUMN_INDEX).checkState()==Qt.CheckState.Checked){
                final Id columnId = (Id)tbColumns.item(row, CHECK_COLUMN_INDEX).data(Qt.ItemDataRole.UserRole);
                final ValListEditor editor = (ValListEditor)tbColumns.cellWidget(row, FORMAT_COLUMN_INDEX);
                columnIds.add(columnId);
                formatNameByColumnId.put(columnId, (String)editor.getValue());
            }
        }
        resultWriteOptions = new PropertyValuesWriteOptions(columnIds);
        for (Map.Entry<Id,String> entry: formatNameByColumnId.entrySet()){
            final PropertyValuesWriteOptions.ExportFormat format =
                    PropertyValuesWriteOptions.ExportFormat.getFromString(entry.getValue());
            resultWriteOptions.setFormatForColumn(entry.getKey(), format);
        }
        exportColumnTitles = cbExportTitles.isChecked();
        storeOptionsToConfig();
        accept();
    }
        
    public File getFile(){
        return resultFile;
    }    
    
    public PropertyValuesWriteOptions getPropertyValuesWriteOptions(){
        return resultWriteOptions;
    }
    
    public CsvWriter.FormatOptions getCsvFormatOptions(){
        return resultCsvFormatOptions;
    }
    
    public int getMaxRows(){
        return maxRows;
    }
    
    public boolean exportColumnTitles(){
        return exportColumnTitles;
    }
}