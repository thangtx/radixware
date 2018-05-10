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
package org.radixware.wps.views.editor.array;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.*;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.widgets.arreditor.AbstractArrayEditorDelegate;
import org.radixware.kernel.common.client.widgets.arreditor.ArrayItemEditingOptions;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;
import org.radixware.wps.rwt.AbstractContainer;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.TableLayout;

import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.ValueEditor;

import org.radixware.wps.views.editors.valeditors.*;

final class ArrayEditorTable extends AbstractContainer {

    public static interface IArrayChangeListener {

        public void onChange(final int i, final Object newValue);
    }

    private final static class ArrItem {

        private final Object value;
        private final UnacceptableInput input;
        private final AbstractArrayEditorDelegate delegate;
        private final ArrayItemEditingOptions options;

        public ArrItem(final UnacceptableInput inp) {
            this.input = inp;
            value = null;
            delegate = null;
            options = null;
        }
        
        public ArrItem(final AbstractArrayEditorDelegate delegate, final ArrayItemEditingOptions options, final Object value) {
            this.input = null;
            this.value = value;
            this.delegate = delegate;
            this.options = options;
        }        

        public Object getValue() {
            return value;
        }


        public UnacceptableInput getUnacceptableInput() {
            return input;
        }

        public boolean isValidInput() {
            return input == null;
        }

        public String getVisibleText(final IClientEnvironment environment) {
            if (isValidInput()) {
                return delegate.getDisplayTextForValue(environment, options, value);
            } else {
                return input.getText();
            }
        }
    }

    private TableLayout table;
    private Html lbNotDefined;
    private int currentRow = -1;
    private int startIndex = 1;
    private ArrayList<ArrItem> array;
    private final EValType itemType;
    private EditMask itemEditMask;
    private ValEditorController editorController;
    private AbstractArrayEditorDelegate<ValEditorController,UIObject> arrItemDelegate;    
    private ArrayItemEditingOptions arrItemOptions;
    private final IClientEnvironment environment;
    private final IArrayChangeListener arrayChangeListener;
    private final ArrayEditor.StartCellModificationListener startCellModificationListener;
    private final ValueEditor.StartChangeValueListener startChangeValueListener = new ValueEditor.StartChangeValueListener() {
        @Override
        public void onStartChangeValue() {
            if (startCellModificationListener != null) {
                final int row = getCurrentRow();
                if (row > -1) {
                    startCellModificationListener.onStartModification(row);
                }
            }
        }
    };

    public ArrayEditorTable(final IClientEnvironment environment,
            final EValType itemType,
            final EditMask itemEditMask,
            final String noValueStr,
            final IArrayChangeListener changeListener,
            final ArrayEditor.StartCellModificationListener startModificationListener) {
        super(new Div());
        this.itemType = itemType;
        this.itemEditMask = itemEditMask;
        this.environment = environment;
        arrayChangeListener = changeListener;
        this.startCellModificationListener = startModificationListener;
        getHtml().addClass("rwt-array-editor-table");
        setBorderBoxSizingEnabled(true);
        setUndefinedImpl(noValueStr);
        html.layout("$RWT.arrayEditor.layout");
    }

    public int addRow(final AbstractArrayEditorDelegate delegate, final ArrayItemEditingOptions options, final Object value) {
        return addRowImpl(new ArrItem(delegate, options, value));
    }

    public int addRow(final UnacceptableInput input) {
        return addRowImpl(new ArrItem(input));
    }

    private int addRowImpl(final ArrItem arrItem) {
        setDefined();
        final TableLayout.Row row = table.addRow(table.getRowCount());
        final TableLayout.Row.Cell headerCell = createCell(row, String.valueOf(table.getRows().size()));
        headerCell.getHtml().addClass("rwt-array-editor-header-cell");

        final TableLayout.Row.Cell contentCell;

        contentCell = createCell(row, arrItem.getVisibleText(environment));
        contentCell.getChildren().get(0).getHtml().setCss("padding", "4px");

        contentCell.getHtml().setAttr("onclick", "$RWT.arrayEditor.onCellClick");
        contentCell.getHtml().removeClass("rwt-ui-background");
        contentCell.getHtml().addClass("rwt-array-editor-value-cell");
        contentCell.getHtml().setAttr("row", table.getRowCount() - 1);

        this.array.add(arrItem);
        if (arrItem.isValidInput()) {
            if (arrayChangeListener != null) {
                arrayChangeListener.onChange(array.size() - 1, arrItem.getValue());
            }
        } else {
            contentCell.getHtml().addClass("rwt-array-editor-invalid-value-cell");
            contentCell.setToolTip(arrItem.getUnacceptableInput().getMessageText());
        }
        setCurrentRow(table.getRowCount() - 1);

        return table.getRowCount() - 1;
    }

    public void removeRow(final int row) {
        if (table != null && row > -1 && row < table.getRowCount()) {
            if (row == currentRow) {
                finishEdit(false);
            }
            table.removeRow(table.getRow(row));
            for (int i = table.getRowCount() - 1; i >= row; i--) {
                setHeaderText(i, String.valueOf(i + startIndex));
                table.getCell(i, 1).getHtml().setAttr("row", i);
            }
            array.remove(row);
            if (row == currentRow) {
                finishEdit(false);
                currentRow = -1;
                if (table.getRowCount() > 0 && row >= table.getRowCount()) {
                    setCurrentRow(table.getRowCount() - 1);
                } else if (row < table.getRowCount()) {
                    setCurrentRow(row);
                }
            }
            if (table.getRowCount() == 0) {
                setTabIndex(1);
                setFocused(true);
            }
        }
    }

    public void setCurrentRow(final int row) {
        if (row < 0 || row >= table.getRowCount() || row == currentRow) {
            return;
        }
        setTabIndex(-1);
        if (currentRow > -1) {
            finishEdit(true);
            table.getCell(currentRow, 0).getHtml().removeClass("rwt-array-editor-header-current-cell");
            table.getCell(currentRow, 1).getHtml().removeClass("rwt-array-editor-value-current-cell");
            table.getCell(currentRow, 1).getHtml().removeClass("rwt-ui-selected-item");
            table.getCell(currentRow, 1).getHtml().setAttr("onkeypress", null);
            table.getCell(currentRow, 1).setTabIndex(-1);
        }
        currentRow = row;
        table.getCell(currentRow, 0).getHtml().addClass("rwt-array-editor-header-current-cell");
        table.getCell(currentRow, 1).getHtml().setAttr("onkeypress", "$RWT.arrayEditor.onKeyPress");
        table.getCell(currentRow, 1).setTabIndex(1);
        table.getCell(currentRow, 1).setFocused(true);
    }

    private static TableLayout.Row.Cell createCell(final TableLayout.Row row, final String text) {
        final TableLayout.Row.Cell cell = row.addCell();
        setCellText(cell, text);
        return cell;
    }

    private static void setCellText(final TableLayout.Row.Cell cell, final String text) {
        final AbstractContainer container = new AbstractContainer(new Div());
        container.getHtml().removeClass("rwt-ui-background");
        final Label label = new Label(text, true);
        label.getHtml().removeClass("rwt-ui-element-text");
        label.getHtml().addClass("rwt-ui-element");
        label.getHtml().setCss("white-space", "nowrap");
        container.add(label);
        cell.clear();
        cell.add(container);
    }

    public final void setDefined() {
        if (table == null) {
            setTabIndex(1);
            getHtml().remove(lbNotDefined);
            lbNotDefined = null;
            table = new TableLayout();
            table.getHtml().setCss("overflow-y", "auto");
            table.getHtml().setAttr("ondblclick", "$RWT.arrayEditor.onDblClick");
            add(table);
            getHtml().setCss("background", null);
            getHtml().setAttr("isdefined", "true");
            array = new ArrayList<>();
            setFocused(true);
        }
    }

    public final boolean setUndefined(final String noValueStr) {
        if (table != null) {
            final String msg = environment.getMessageProvider().translate("ArrayEditor", "Do you really want to delete array?");
            final String title = environment.getMessageProvider().translate("ArrayEditor", "Confirm To Delete Array");
            if (table.getRowCount() == 0 || environment.messageConfirmation(title, msg)) {
                finishEdit(false);
                remove(table);
                setUndefinedImpl(noValueStr);
            } else {
                return false;
            }
        } else if (lbNotDefined != null) {
            lbNotDefined.getChildAt(0).setInnerText(noValueStr);
        }
        return true;
    }

    private void setUndefinedImpl(final String noValueStr) {
        table = null;
        lbNotDefined = new Div();
        lbNotDefined.addClass("rwt-array-editor-label-not-defined");
        final Html span = new Html("span");
        lbNotDefined.add(span);
        span.setInnerText(noValueStr);
        getHtml().add(lbNotDefined);
        getHtml().setCss("background", "#DDD");
        getHtml().setAttr("isdefined", "false");
        array = null;
        setTabIndex(1);
        setFocused(true);
        currentRow = -1;
    }

    public boolean isDefined() {
        return table != null;
    }

    public int getCurrentRow() {
        return table == null ? -1 : currentRow;
    }

    public int getRowsCount() {
        return table == null ? 0 : table.getRowCount();
    }

    public void clearRows() {
        if (table != null) {
            setTabIndex(1);
            finishEdit(false);
            table.clearRows();
            array.clear();
            currentRow = -1;
        }
    }

    public void setEditMask(final EditMask editMask) {
        itemEditMask = editMask;
        final ArrayList arr = getCurrentValue();
        if (arr != null && !arr.isEmpty()) {
            Object itemValue;
            for (int i = 0; i < arr.size(); i++) {
                itemValue = arr.get(i);
                if (itemEditMask instanceof EditMaskBool) {
                    if (((EditMaskBool) itemEditMask).getValueTitleVisible()) {
                        String title = ((EditMaskBool) itemEditMask).toStr(environment, itemValue);
                        setText(i, title);
                    } else {
                        setText(i, "");
                    }
                } else {
                    setText(i, itemEditMask.toStr(getEnvironment(), itemValue));
                }
            }
        }
    }

    public ArrayList<Object> getCurrentValue() {
        if (array == null) {
            return null;
        } else {
            final ArrayList<Object> value = new ArrayList<>();
            for (ArrItem arrItem : array) {
                value.add(arrItem.getValue());
            }
            return value;
        }
    }

    public void setCurrentValue(final AbstractArrayEditorDelegate delegate, final ArrayItemEditingOptions options, final List<Object> values) {
        clearRows();
        for (Object value : values) {
            addRow(delegate, options, value);
        }
    }

    private ValidationResult validate(final Object value, final boolean isMandatory) {
        ValidationResult result;
        if ((value instanceof Reference) && ((Reference) value).isBroken()) {
            result = ValidationResult.INVALID;
        } else {
            result = itemEditMask.validate(getEnvironment(), value);
        }

        if (result == ValidationResult.ACCEPTABLE && (value == null && isMandatory)) {
            result = ValidationResult.INVALID;
        }
        return result;
    }

    public ValidationResult validateRow(final int row, final boolean isMandatory) {
        final ArrItem arrItem = this.array.get(row);
        if (arrItem.isValidInput()) {
            final ValidationResult result = validate(arrItem.getValue(), isMandatory);
            {
                if (result == ValidationResult.ACCEPTABLE) {
                    table.getCell(row, 1).getHtml().removeClass("rwt-array-editor-invalid-value-cell");
                    table.getCell(row, 1).setToolTip(null);
                } else {
                    table.getCell(row, 1).getHtml().addClass("rwt-array-editor-invalid-value-cell");
                    final String reasonAsStr = 
                        result.getInvalidValueReason().getMessage(environment.getMessageProvider(), InvalidValueReason.EMessageType.Value);
                    table.getCell(row, 1).setToolTip(reasonAsStr);
                }
            }
            return result;
        } else {
            table.getCell(row, 1).getHtml().addClass("rwt-array-editor-invalid-value-cell");
            table.getCell(row, 1).setToolTip(arrItem.getUnacceptableInput().getMessageText());
            return ValidationResult.Factory.newInvalidResult(arrItem.getUnacceptableInput().getReason());
        }
    }

    public void validateArray(final boolean isMandatory) {
        if (isDefined()) {
            for (int row = 0, count = table.getRowCount(); row < count; row++) {
                validateRow(row, isMandatory);
            }
        }
    }

    private String getText(final int row) {
        final AbstractContainer content = (AbstractContainer) table.getCell(row, 1).getChildren().get(0);
        return ((Label) content.getChildren().get(0)).getText();
    }

    public void setText(final int row, final String text) {
        final AbstractContainer content = (AbstractContainer) table.getCell(row, 1).getChildren().get(0);
        ((Label) content.getChildren().get(0)).setText(text);
    }

    private void setHeaderText(final int row, final String text) {
        final AbstractContainer content = (AbstractContainer) table.getCell(row, 0).getChildren().get(0);
        ((Label) content.getChildren().get(0)).setText(text);
    }

    public void swapRows(final int i, final int j) {
        if (i >= 0 && j >= 0 && i < table.getRowCount() && j < table.getRowCount() && i != j) {
            final String keepString = getText(j);
            final ArrItem keepValue = array.get(j);
            setText(j, getText(i));
            array.set(j, array.get(i));
            setText(i, keepString);
            array.set(i, keepValue);
        }
    }

    private List<Object> getValues(final int exceptItem) {
        final List<Object> values = getCurrentValue();
        if (values != null && exceptItem > -1) {
            values.remove(exceptItem);
        }
        return values;
    }

    public ValEditorController startEdit(final AbstractArrayEditorDelegate<ValEditorController,UIObject> delegate,
                                                         final ArrayItemEditingOptions options) {
        final int row = getCurrentRow();
        if (row > -1) {
            editorController = createteEditor(row, delegate, options);
            if (editorController != null) {
                arrItemDelegate = delegate;
                arrItemOptions = options;
                table.getCell(row, 1).clear();
                final UIObject editor = (UIObject) editorController.getValEditor();
                editorController.addStartChangeValueListener(startChangeValueListener);
                table.getCell(row, 1).add(editor);
                getHtml().setAttr("editing", row);
                editor.setFocused(true);
            }
            return editorController;
        } else {
            return null;
        }
    }

    public void finishEdit(final boolean accept) {
        if (editorController != null && arrItemDelegate!=null && arrItemOptions!=null) {
            editorController.removeStartChangeValueListener(startChangeValueListener);
            final int row = getCurrentRow();
            final ArrItem newItem;
            if (accept) {
                if (editorController.hasAcceptableInput()) {
                    final Object editorValue = arrItemDelegate.getValueFromEditor(editorController);
                    newItem = new ArrItem(arrItemDelegate, arrItemOptions, editorValue);
                } else {
                    newItem = new ArrItem(editorController.getUnacceptableInput());
                }
            } else {
                newItem = array.get(row);
            }
            getHtml().setAttr("editing", null);
            setCellText(table.getCell(row, 1), newItem.getVisibleText(environment));
            array.set(row, newItem);
            if (arrayChangeListener != null && newItem.isValidInput()) {
                arrayChangeListener.onChange(row, newItem.getValue());
            }
            table.getCell(row, 1).setFocused(true);
            final AbstractContainer container = (AbstractContainer) table.getCell(row, 1).getChildren().get(0);
            container.getChildren().get(0).getHtml().setCss("padding", "4px");
            editorController.close();
            editorController = null;
            arrItemDelegate = null;
            arrItemOptions = null;
        }
    }

    public boolean isInEditingMode() {
        return editorController != null;
    }

    @SuppressWarnings("unchecked")
    public ValEditorController createteEditor(final int row,
                                                                 final AbstractArrayEditorDelegate<ValEditorController,UIObject> delegate,
                                                                 final ArrayItemEditingOptions options){
        final ValEditorController controller = delegate.createEditor(this, environment, options, row, getValues(-1));
        final ArrItem item = array.get(row);
        if (item.isValidInput()){
            final Object value = item.getValue();
            if (itemEditMask instanceof EditMaskBool) {
                final Boolean val;
                if (value == null) {
                    val = null;
                }else{
                    val = value.equals(((EditMaskBool) itemEditMask).getTrueValue());
                }
                delegate.setValueToEditor(controller, val);
            } else {
                delegate.setValueToEditor(controller, value);
            }
        }else{
            controller.setInputText(item.getUnacceptableInput().getText());
        }
        return controller;
    }

    public void clearValue(final AbstractArrayEditorDelegate delegate, final ArrayItemEditingOptions options, final int row) {
        finishEdit(false);
        final ArrItem nullItem = new ArrItem(delegate, options,  null);
        array.set(row, nullItem);
        if (arrayChangeListener != null) {
            arrayChangeListener.onChange(row, null);

        }
        setText(row, nullItem.getVisibleText(environment));
    }

    @Override
    public void setFocused(boolean focused) {
        if (isDefined() && getCurrentRow() > -1) {
            table.getCell(getCurrentRow(), 1).setFocused(focused);
        } else {
            super.setFocused(focused);
        }
    }

    public void setStartIndex(final int newIndex) {
        if (newIndex!=startIndex){
            startIndex = newIndex;
            if (table!=null){
                for (int i = 0, count=table.getRowCount(); i <count; i++) {
                    setHeaderText(i, String.valueOf(i + startIndex));                    
                }            
            }
        }
    }

    public int getStartIndex() {
        return startIndex;
    }
}
