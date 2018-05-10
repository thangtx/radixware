/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
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
import java.nio.charset.Charset;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.types.PropertyValuesWriteOptions;
import org.radixware.kernel.common.client.utils.CsvWriter;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.client.widgets.selector.ISelectorDataExportOptionsDialog;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.ESelectorColumnVisibility;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.RwtListDialog;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.CheckBox;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.FormBox;
import org.radixware.wps.rwt.Grid;
import org.radixware.wps.rwt.Grid.Cell;
import org.radixware.wps.rwt.GroupBox;
import org.radixware.wps.rwt.HorizontalBoxContainer;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.rwt.VerticalBoxContainer;
import org.radixware.wps.views.editors.valeditors.ValIntEditorController;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;

final class SelectorDataExportOptionsDialog extends Dialog implements ISelectorDataExportOptionsDialog{

    public static enum Features{ENCODING, TIMEZONEFORMAT};
    private final static int TITLE_COLUMN_INDEX = 0;
    private final static int FORMAT_COLUMN_INDEX = 1;
    private final static int CHECK_COLUMN_INDEX = 2;
    private final static String SETTINGS_KEY = "export_excel_params";   
    private final static EditMaskBool columnEditMask = new EditMaskBool();

    private final Id selectorPresentationId;
    private File file;
    private final GroupBox gbColumns = new GroupBox();
    private final ToolButton tbUp = new ToolButton();
    private final ToolButton tbDown = new ToolButton();
    private final ValListEditorController<String> veEncoding;
    private final ValListEditorController<String> veTimezone;
    private ValIntEditorController veMaxRows;
    private boolean isEncoding = false;
    private boolean isTimezone = false;
    private final CheckBox cbExportTitles = new CheckBox();
    private final Grid grid = new Grid();

    public SelectorDataExportOptionsDialog(final GroupModel group, EnumSet<Features> features) {
        super(((WpsEnvironment) group.getEnvironment()).getDialogDisplayer(), null, true, new Dialog.DialogGeometry(600, 350, 0, 0));        
        isTimezone = features.contains(Features.TIMEZONEFORMAT);
        isEncoding = features.contains(Features.ENCODING);
        
        this.selectorPresentationId = group.getDefinition().getId();
        if (isEncoding) {
            final Map<String, Charset> allCharsets = Charset.availableCharsets();
            String currentCharsetName = "UTF-8";
            final String utf8WithBomTitle = 
                group.getEnvironment().getMessageProvider().translate("Selector","UTF-8 with BOM character");        
            final EditMaskList maskList = new EditMaskList();
            for (Map.Entry<String, Charset> entry : allCharsets.entrySet()) {
                maskList.addItem(entry.getValue().displayName(), entry.getKey());
                if (Charset.defaultCharset().name().equals(entry.getKey())) {
                    currentCharsetName = entry.getKey();
                }
                if ("UTF-8".equals(entry.getKey())){
                    maskList.addItem(utf8WithBomTitle, CsvWriter.UTF_8_WITH_BOM);
                }            
            }
            veEncoding = new ValListEditorController<>(getEnvironment(), maskList);
            veEncoding.setMandatory(true);
            veEncoding.setValue(currentCharsetName);
        } else {
            veEncoding = null;
        }
        if (isTimezone) {
            veTimezone = createTimeZoneEditor(PropertyValuesWriteOptions.TimeZoneFormat.SERVER_TIMEZONE);
            veTimezone.setMandatory(true);
            restoreTimeZoneFromConfig();
            veTimezone.getValEditor().addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {

                @Override
                public void onValueChanged(String oldValue, String newValue) {
                    SelectorDataExportOptionsDialog.this.veTimeZoneValueChanged(oldValue, newValue);
                }
            });
        } else {
            veTimezone = null;
        }
        setupUi();
        final Map<Id, PropertyValuesWriteOptions.ExportFormat> restoredFormats = new HashMap<>();
        final List<Id> restoredColumnsOrder = restoreOptionsFromConfig(restoredFormats);
        fillColumnsTable(group, restoredColumnsOrder, restoredFormats);
    }

    private void setupUi() {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        setWindowTitle(mp.translate("Selector", "Export Options"));
        VerticalBoxContainer content = new VerticalBoxContainer();
        content.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);

        FormBox optionsBox = new FormBox();
        if (isEncoding) {
            optionsBox.addLabledEditor(mp.translate("Selector", "File character set:"), (UIObject) veEncoding.getValEditor());
        }
        if (isTimezone) {
            optionsBox.addLabledEditor(mp.translate("Selector", "Timezone:"), (UIObject)veTimezone.getValEditor());
            ((UIObject)veTimezone.getValEditor()).setVisible(false);
        }

        veMaxRows = new ValIntEditorController(getEnvironment());
        veMaxRows.setMandatory(true);
        final EditMaskInt maxRowsEditMask = new EditMaskInt();
        maxRowsEditMask.setMinValue(1);
        maxRowsEditMask.setMaxValue(Integer.MAX_VALUE);
        maxRowsEditMask.setNoValueStr(mp.translate("Selector", "<unbounded>"));
        veMaxRows.setEditMask(maxRowsEditMask);
        optionsBox.addLabledEditor(mp.translate("Selector", "Maximum rows:"), (UIObject) veMaxRows.getValEditor());

        content.add(optionsBox);
        content.addSpace(10);
        gbColumns.setTitle(mp.translate("Selector", "Choose columns to export:"));

        
        
        
        final VerticalBoxContainer buttonsLayout = new VerticalBoxContainer();
        buttonsLayout.setAlignment(Alignment.CENTER);

        tbUp.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.UP));
        tbUp.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                moveColumnUp();
            }
        });

        tbDown.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.DOWN));
        tbDown.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                moveColumnDown();
            }
        });

        grid.setSelectionMode(Grid.SelectionMode.ROW);
        buttonsLayout.setSizePolicy(SizePolicy.PREFERRED, SizePolicy.PREFERRED);
        buttonsLayout.setWidth(28);

        tbUp.setBorderBoxSizingEnabled(true);
        tbDown.setBorderBoxSizingEnabled(true);
        tbUp.setIconWidth(16);
        tbUp.setIconHeight(16);
        tbDown.setIconWidth(16);
        tbDown.setIconHeight(16);
        
        buttonsLayout.add(tbUp);
        buttonsLayout.add(tbDown);

        grid.addCurrentRowListener(new Grid.CurrentRowListener() {

            @Override
            public void currentRowChanged(Grid.Row oldRow, Grid.Row newRow) {
                refreshBtnState();
            }

            @Override
            public boolean beforeChangeCurrentRow(Grid.Row oldRow, Grid.Row newRow) {
                return true;
            }
        });

        HorizontalBoxContainer hBoxContainter = new HorizontalBoxContainer();
        hBoxContainter.add(grid);
        hBoxContainter.addSpace();
        hBoxContainter.add(buttonsLayout);
        hBoxContainter.addSpace();
        hBoxContainter.setAutoSize(grid, true);
        hBoxContainter.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);

        gbColumns.add(hBoxContainter);
        cbExportTitles.setText(getEnvironment().getMessageProvider().translate("Selector", "Export column headers"));
        content.add(gbColumns);
        gbColumns.setTop(10);
        content.setAutoSize(gbColumns, true);
        content.add(cbExportTitles);
        add(content);

        addCloseAction(EDialogButtonType.OK).addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                onAccept();
            }
        });
        addCloseAction(EDialogButtonType.CANCEL);
        content.getAnchors().setLeft(new Anchors.Anchor(0, 5));
        content.getAnchors().setRight(new Anchors.Anchor(1, -5));
        content.getAnchors().setTop(new Anchors.Anchor(0, 5));
        content.getAnchors().setBottom(new Anchors.Anchor(1, -5));
    }
    
        private ValListEditorController<String> createTimeZoneEditor(final PropertyValuesWriteOptions.TimeZoneFormat defaultFormat) {
        final List<PropertyValuesWriteOptions.TimeZoneFormat> formatItems = new LinkedList<>();   
        for (PropertyValuesWriteOptions.TimeZoneFormat format : PropertyValuesWriteOptions.TimeZoneFormat.values()) {
            formatItems.add(format);
        }
        final EditMaskList editMask = 
            createEditMaskForTimeZoneFormatItems(formatItems, getEnvironment());
        final ValListEditorController<String> editor = new ValListEditorController<>(getEnvironment(), editMask);
        if (formatItems.contains(defaultFormat)){
            editor.setValue(defaultFormat.asString());
        }else{
            editor.setValue(formatItems.get(0).asString());
        }
        return editor;
    }
    
    private EditMaskList createEditMaskForTimeZoneFormatItems(final List<PropertyValuesWriteOptions.TimeZoneFormat> formats, final IClientEnvironment env){
        MessageProvider mp = env.getMessageProvider();
        final List<EditMaskList.Item> editMaskItems = new LinkedList<>();
        for (PropertyValuesWriteOptions.TimeZoneFormat format: formats){
            StringBuilder title = new StringBuilder(PropertyValuesWriteOptions.TimeZoneFormat.getDisplayName(format, mp));
            if (format.equals(PropertyValuesWriteOptions.TimeZoneFormat.SERVER_TIMEZONE)) {
                title.append(" (").append(env.getServerTimeZoneInfo().getTimeZoneDisplayName(TimeZone.LONG, env.getLocale())).append(')');
            } else if (format.equals(PropertyValuesWriteOptions.TimeZoneFormat.CLIENT_TIMEZONE)) {
                TimeZone clientTimeZone = TimeZone.getDefault();
                title.append(" (").append(displayTimeZone(clientTimeZone)).append(')') ;
            }
            editMaskItems.add(new EditMaskList.Item(title.toString(), format.asString()));
        }
        return new EditMaskList(editMaskItems);
    }
    
    @SuppressWarnings("unused")
    private void veTimeZoneValueChanged(String oldVal, String newVal) {
        if (newVal.toString().equals(PropertyValuesWriteOptions.TimeZoneFormat.ANOTHER.asString())) {
            RwtListDialog tzDialog = new RwtListDialog(getEnvironment(), null);
            tzDialog.setFeatures(EnumSet.of(IListWidget.EFeatures.FILTERING));
            String[] ids = TimeZone.getAvailableIDs();
            List<ListWidgetItem> availableTimeZones = new LinkedList<>();
            for (String id : ids) {
                String displayName = displayTimeZone(TimeZone.getTimeZone(id));
                availableTimeZones.add(new ListWidgetItem(displayName, id)); 
            }
            tzDialog.setItems(availableTimeZones);                 
            if (tzDialog.execDialog()== DialogResult.ACCEPTED) {
                EditMaskList editMask = veTimezone.getEditMask();
                ListWidgetItem currentTZ = tzDialog.getCurrent();
                boolean containsCurrent = false;
                for (EditMaskList.Item item :editMask.getItems()) {
                    if (item.getValue().equals(currentTZ.getValue())) {
                        containsCurrent = true;
                    }
                }
                if (!containsCurrent) {
                    editMask.addItem(editMask.getItems().size() - 1, currentTZ.getText(), currentTZ.getValue());
                    veTimezone.setEditMask(editMask);
                }
                veTimezone.getController().setValue(currentTZ.getValue().toString());
            } else {
                veTimezone.setValue(oldVal);
            }
        }
    }
    
    private String displayTimeZone(TimeZone tz) {
        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
                          - TimeUnit.HOURS.toMinutes(hours);
        // avoid -4:-30 issue
        minutes = Math.abs(minutes);
        String result = "";
        if (hours > 0) {
                result = String.format("(GMT+%d:%02d) %s", hours, minutes, tz.getID());
        } else {
                result = String.format("(GMT%d:%02d) %s", hours, minutes, tz.getID());
        }
        return result;
    }


    private void moveColumnUp() {
        Grid.Cell currentSell = grid.getCurrentCell();
        int curRowIndex = grid.getRowIndex(currentSell.getRow());
        grid.swapRows(curRowIndex - 1, curRowIndex);
        grid.setCurrentCell(grid.getCell(curRowIndex - 1, grid.getCurrentCell().getColumn().getIndex()));
        refreshBtnState();
    }

    private void moveColumnDown() {
        final Grid.Cell currentSell = grid.getCurrentCell();
        int curRowIndex = grid.getRowIndex(currentSell.getRow());
        int newRowIndex = curRowIndex + 1;
        grid.swapRows(newRowIndex, curRowIndex);
        grid.setCurrentCell(grid.getCell(curRowIndex + 1, grid.getCurrentCell().getColumn().getIndex()));
        refreshBtnState();
    }

    private List<Id> restoreOptionsFromConfig(final Map<Id, PropertyValuesWriteOptions.ExportFormat> formatByColumnId) {
        final ClientSettings config = getEnvironment().getConfigStore();
        config.beginGroup(SettingNames.SYSTEM + "/" + getClass().getSimpleName());
        try {
            final String charsetName = config.readString("charset", null);
            try{
                if (charsetName!=null && !charsetName.isEmpty() && isEncoding
                    && (CsvWriter.UTF_8_WITH_BOM.equals(charsetName) || Charset.isSupported(charsetName))){
                    veEncoding.setValue(charsetName);
                }
            }catch(IllegalArgumentException exception){
                //ignore
            }
            config.beginGroup(selectorPresentationId.toString());
            try {
                final int restoredMaxRows = config.readInteger("maxRows", 0);
                if (restoredMaxRows > 0) {
                    veMaxRows.setValue(Long.valueOf(restoredMaxRows));
                }
                final boolean exportTitles = config.readBoolean("exportTitles", true);
                cbExportTitles.setSelected(exportTitles);
                final int columnsCount = config.beginReadArray("columns");
                try {
                    final List<Id> restoredColumnsOrder = new LinkedList<>();
                    for (int i = 0; i < columnsCount; i++) {
                        config.setArrayIndex(i);
                        final Id columnId = readColumnOptions(config, formatByColumnId);
                        if (columnId != null && !restoredColumnsOrder.contains(columnId)) {
                            restoredColumnsOrder.add(columnId);
                        }
                    }
                    return restoredColumnsOrder;
                } finally {
                    config.endArray();
                }
            } finally {
                config.endGroup();
            }
        } finally {
            config.endGroup();
        }
    }

    private Id readColumnOptions(final ClientSettings config, final Map<Id, PropertyValuesWriteOptions.ExportFormat> formatByColumnId) {
        final Id columnId = config.readId("columnId");
        if (columnId != null) {
            final String formatAsStr = config.readString("format", null);
            if (formatAsStr == null) {
                formatByColumnId.put(columnId, null);
            } else {
                final PropertyValuesWriteOptions.ExportFormat format
                        = PropertyValuesWriteOptions.ExportFormat.getFromString(formatAsStr);
                if (format != null) {
                    formatByColumnId.put(columnId, format);
                }
            }
            return columnId;
        }
        return null;
    }

    private void fillColumnsTable(final GroupModel group, List<Id> restoredColumnsOrder, Map<Id, PropertyValuesWriteOptions.ExportFormat> restoredFormats) {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        grid.addColumn(mp.translate("Selector", "Column"));
        grid.addColumn(mp.translate("Selector", "Output Format"));
        final Grid.Column exportColumn = grid.addColumn(mp.translate("Selector", "Export"));

        exportColumn.getEditingOptions().setEditMask(columnEditMask);
        exportColumn.getEditingOptions().setEditingMode(IGrid.ECellEditingMode.NULL_VALUE_RESTRICTED);
        grid.setColumnsHeaderAlignment(Alignment.CENTER);

        grid.getColumn(TITLE_COLUMN_INDEX).setSizePolicy(IGrid.EColumnSizePolicy.STRETCH);
        grid.getColumn(FORMAT_COLUMN_INDEX).setFixedWidth(165);
        grid.getColumn(CHECK_COLUMN_INDEX).setSizePolicy(IGrid.EColumnSizePolicy.BY_CONTENT);

        final RadSelectorPresentationDef.SelectorColumns columns
                = group.getSelectorPresentationDef().getSelectorColumns();

        final List<Id> orderedColumnIds = new LinkedList<>();
        for (Id columnId : restoredColumnsOrder) {
            final RadSelectorPresentationDef.SelectorColumn column = columns.findColumn(columnId);
            if (column != null && column.getVisibility() != ESelectorColumnVisibility.NEVER) {
                orderedColumnIds.add(columnId);
            }
        }
        for (RadSelectorPresentationDef.SelectorColumn column : columns) {
            if (!orderedColumnIds.contains(column.getPropertyId()) && column.getVisibility() != ESelectorColumnVisibility.NEVER) {
                orderedColumnIds.add(column.getPropertyId());
            }
        }
        for (Id columnId : orderedColumnIds) {
            final PropertyValuesWriteOptions.ExportFormat format = restoredFormats.get(columnId);
            final SelectorColumnModelItem column = group.getSelectorColumn(columnId);
            if (!column.isForbidden()) {
                final boolean doExport = format != null || !restoredFormats.containsKey(columnId);
                addSelectorColumn(column, format, doExport);
            }
        }

        grid.setCurrentCell(grid.getCell(0, 0));
    }

    private void addSelectorColumn(final SelectorColumnModelItem column, final PropertyValuesWriteOptions.ExportFormat format, final boolean doExport) {
        Grid.Row row = grid.addRow();
        row.getCell(TITLE_COLUMN_INDEX).setValue(column.getTitle());
        final EValType valType = column.getPropertyDef().getType();
        final boolean isEnum = column.getPropertyDef().getConstSet() != null;
        Grid.Cell formatCell = row.getCell(FORMAT_COLUMN_INDEX);
        formatCell.getEditingOptions().setEditingMode(IGrid.ECellEditingMode.NULL_VALUE_RESTRICTED);
        applyListEditMaskToCell(formatCell, valType, isEnum, format);

        Cell checkIndexCell = row.getCell(CHECK_COLUMN_INDEX);
        checkIndexCell.setUserData(column.getId());
        grid.addCellValueChangeListener(new Grid.CellValueChangeListener() {

            @Override
            public void onValueChanged(Cell cell, Object oldValue, Object newValue) {
                if (cell.getCellIndex() == CHECK_COLUMN_INDEX) {
                    Grid.Row row = cell.getRow();
                    if (newValue.equals(true)) {
                        row.getCell(TITLE_COLUMN_INDEX).setEnabled(true);
                        row.getCell(FORMAT_COLUMN_INDEX).getEditingOptions().setEditingMode(IGrid.ECellEditingMode.NULL_VALUE_RESTRICTED);
                    } else {
                        row.getCell(TITLE_COLUMN_INDEX).setEnabled(false);
                        row.getCell(FORMAT_COLUMN_INDEX).getEditingOptions().setEditingMode(IGrid.ECellEditingMode.READ_ONLY);
                    }
                }
            }
        });
        if (doExport && (format != null || column.isVisible())) {
            checkIndexCell.setValue(true);
        } else {
            checkIndexCell.setValue(false);
        }

    }

    private void applyListEditMaskToCell(Grid.Cell cell, final EValType valType, final boolean isEnum, final PropertyValuesWriteOptions.ExportFormat defaultFormat) {
        final List<PropertyValuesWriteOptions.ExportFormat> formatItems = new LinkedList<>();
        if (valType == EValType.BOOL) {
            formatItems.add(PropertyValuesWriteOptions.ExportFormat.BOOLEAN_TO_NUMBER);
            formatItems.add(PropertyValuesWriteOptions.ExportFormat.BOOLEAN_TO_STRING);
        } else {
            formatItems.add(PropertyValuesWriteOptions.ExportFormat.PROPERTY_VALUE);
        }
        if (!valType.isArrayType()) {
            if (isEnum) {
                formatItems.add(PropertyValuesWriteOptions.ExportFormat.ENUM_TITLE);
            }
            if (valType == EValType.XML || valType == EValType.BIN || valType == EValType.STR) {
                formatItems.add(PropertyValuesWriteOptions.ExportFormat.BASE64);
            }
        }
        if (valType != EValType.BOOL) {
            formatItems.add(PropertyValuesWriteOptions.ExportFormat.DISPLAYED_TEXT);
        }
        if (valType == EValType.DATE_TIME && isTimezone) {
            ((UIObject)veTimezone.getValEditor()).setVisible(true);
        }
        EditMaskList editMask = createEditMaskForExportFormatItems(formatItems, getEnvironment().getMessageProvider());
        cell.getEditingOptions().setEditMask(editMask);
        if (formatItems.contains(defaultFormat)) {
            cell.setValue(defaultFormat.asString());
        } else if (valType == EValType.PARENT_REF && formatItems.contains(PropertyValuesWriteOptions.ExportFormat.DISPLAYED_TEXT)) {
            cell.setValue(PropertyValuesWriteOptions.ExportFormat.DISPLAYED_TEXT.asString());
        } else {
            cell.setValue(formatItems.get(0).asString());
        }
    }

    private static EditMaskList createEditMaskForExportFormatItems(final List<PropertyValuesWriteOptions.ExportFormat> formats, final MessageProvider mp) {
        final List<EditMaskList.Item> editMaskItems = new LinkedList<>();
        for (PropertyValuesWriteOptions.ExportFormat format : formats) {
            final String title = PropertyValuesWriteOptions.ExportFormat.getDisplayName(format, mp);
            editMaskItems.add(new EditMaskList.Item(title, format.asString()));
        }
        return new EditMaskList(editMaskItems);
    }

    private void refreshBtnState() {
        int gridIndex = grid.getRowIndex(grid.getCurrentRow());
        tbUp.setEnabled(gridIndex > 0);
        tbDown.setEnabled(gridIndex < grid.getRowCount() - 1);
    }

    private void onAccept() {
        storeOptionsToConfig();
        SelectorDataExportOptionsDialog.this.close(DialogResult.ACCEPTED);
        final String tmp_file_prefix="selector_data_"+selectorPresentationId.toString()+"_";
        file = RadixLoader.getInstance().createTempFile(tmp_file_prefix);
        if (isTimezone) {
            writeTimeZoneToConfig(veTimezone.getValue());
        }
    }

    @Override
    public PropertyValuesWriteOptions getPropertyValuesWriteOptions() {
        final List<Id> columnIds = new LinkedList<>();
        final Map<Id, String> formatNameByColumnId = new HashMap<>();
        for (int row = 0, count = grid.getRowCount(); row < count; row++) {
            if ((boolean) grid.getCell(row, CHECK_COLUMN_INDEX).getValue() == true) {
                final Id columnId = (Id) grid.getCell(row, CHECK_COLUMN_INDEX).getUserData();
                String curFormatValue = grid.getCell(row, FORMAT_COLUMN_INDEX).getValue().toString();
                columnIds.add(columnId);
                formatNameByColumnId.put(columnId, curFormatValue);
            }
        }
        PropertyValuesWriteOptions resultWriteOptions = new PropertyValuesWriteOptions(columnIds);
                PropertyValuesWriteOptions.TimeZoneFormat timeZoneFormat;
        if (isTimezone) {
            timeZoneFormat = PropertyValuesWriteOptions.TimeZoneFormat.getFromString(String.valueOf(veTimezone.getValue()));
            if (timeZoneFormat == null) { //if other format is used getFormString() return null
                timeZoneFormat = PropertyValuesWriteOptions.TimeZoneFormat.ANOTHER;
            }
        } else {
            timeZoneFormat = null;
        }
        resultWriteOptions = new PropertyValuesWriteOptions(columnIds, timeZoneFormat);
        if (PropertyValuesWriteOptions.TimeZoneFormat.ANOTHER.equals(timeZoneFormat)) {
            resultWriteOptions.setTimeZone(TimeZone.getTimeZone(veTimezone.getValue()));
        }
        if (isTimezone) {
            writeTimeZoneToConfig(String.valueOf(veTimezone.getValue()));
        }
        for (Map.Entry<Id, String> entry : formatNameByColumnId.entrySet()) {
            final PropertyValuesWriteOptions.ExportFormat format
                    = PropertyValuesWriteOptions.ExportFormat.getFromString(entry.getValue());
            resultWriteOptions.setFormatForColumn(entry.getKey(), format);
        }
        return resultWriteOptions;
    }

    private void storeOptionsToConfig() {
        final ClientSettings config = getEnvironment().getConfigStore();
        config.beginGroup(SettingNames.SYSTEM + "/" + getClass().getSimpleName());
        try {
            if (isEncoding) {
                config.writeString("charset", String.valueOf(veEncoding.getValue()));
            }
            config.beginGroup(selectorPresentationId.toString());
            try {
                config.writeInteger("maxRows", veMaxRows.getValue() == null ? 0 : veMaxRows.getValue().intValue());
                config.writeBoolean("exportTitles", cbExportTitles.isSelected());
                final int rowsCount = grid.getRowCount();
                config.remove("columns");
                config.beginWriteArray("columns");
                try {
                    for (int row = 0; row < rowsCount; row++) {
                        config.setArrayIndex(row);
                        writeColumnOptions(config, row);
                    }
                } finally {
                    config.endArray();
                }
            } finally {
                config.endGroup();
            }
        } finally {
            config.endGroup();
        }
    }

    private void writeColumnOptions(final ClientSettings config, final int row) {
        Grid.Cell checkCell = grid.getCell(row, CHECK_COLUMN_INDEX);
        final Id columnId = (Id) checkCell.getUserData();
        config.writeId("columnId", columnId);
        if ((boolean) checkCell.getValue() == true) {
            String curFormatValue = grid.getCell(row, FORMAT_COLUMN_INDEX).getValue().toString();
            config.writeString("format", curFormatValue);
        }
    }

    @Override
    public CsvWriter.FormatOptions getCsvFormatOptions() {
        return new CsvWriter.FormatOptions(veEncoding.getValue());
    }

    @Override
    public int getMaxRows() {
        return veMaxRows.getValue() == null ? -1 : veMaxRows.getValue().intValue();
    }

    @Override
    public boolean exportColumnTitles() {
        return cbExportTitles.isSelected();
    }

    private void writeTimeZoneToConfig(final String timeZone){
        if (timeZone != null && !timeZone.isEmpty()){
            final ClientSettings settings = getEnvironment().getConfigStore();
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.SELECTOR_GROUP);
            try{
                settings.writeString(SETTINGS_KEY, timeZone);
            }finally{
                settings.endGroup();
                settings.endGroup();
            }
        }
    }
    
     private void restoreTimeZoneFromConfig(){
        final ClientSettings settings = getEnvironment().getConfigStore();
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.SELECTOR_GROUP);
        final String timeZone;
        try{
            timeZone = settings.readString(SETTINGS_KEY, "");
        }finally{
            settings.endGroup();
            settings.endGroup();
        }
        if (timeZone!=null && !timeZone.isEmpty()){  
            if (PropertyValuesWriteOptions.TimeZoneFormat.getFromString(timeZone) != null) {
                veTimezone.setValue(timeZone);
            } else {
                EditMaskList editMask = veTimezone.getEditMask();
                TimeZone tz = TimeZone.getTimeZone(timeZone);
                editMask.addItem(editMask.getItems().size() - 1, displayTimeZone(tz), timeZone);
                veTimezone.setEditMask(editMask);
                veTimezone.setValue(timeZone);
            }
        }
    }
    
    @Override
    public File getFile() {
        return file;
    }        
}
