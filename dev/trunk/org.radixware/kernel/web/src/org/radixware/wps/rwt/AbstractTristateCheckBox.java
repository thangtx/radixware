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

package org.radixware.wps.rwt;

import java.awt.Color;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EFontStyle;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.client.types.PriorityArray;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EKeyEvent;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.rwt.InputBox.InvalidStringValueException;
import org.radixware.wps.rwt.ValueEditor.ValueChangeListener;
import org.radixware.wps.rwt.events.HtmlEvent;
import org.radixware.wps.rwt.events.KeyDownEventFilter;
import org.radixware.wps.rwt.events.KeyDownHtmlEvent;
import org.radixware.wps.text.WpsTextOptions;

import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.ValEditorController;


abstract class AbstractTristateCheckBox<T, V extends EditMask> extends UIObject implements IValEditor<T,V> {
    
    private static class InternalValueChangeListener<T> implements ValueEditor.ValueChangeListener<T> {

        private List<ValueEditor.ValueChangeListener<T>> listeners = null;

        @Override
        public void onValueChanged(final T oldValue, final T newValue) {
            if (listeners != null) {
                final List<ValueEditor.ValueChangeListener<T>> listenersList = new LinkedList<>(listeners);
                for (ValueChangeListener<T> l : listenersList) {
                    l.onValueChanged(oldValue, newValue);
                }
            }
        }

        public void addValueChangeListener(final ValueEditor.ValueChangeListener<T> listener) {
            if (listeners == null) {
                listeners = new LinkedList<>();
                listeners.add(listener);
            } else if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }

        public void removeValueChangeListener(final ValueEditor.ValueChangeListener<T> listener) {
            if (listeners != null) {
                listeners.remove(listener);
            }
        }
    }    

    private final class ValEditorControllerImpl extends ValEditorController<T, V>{
        
        public ValEditorControllerImpl(final IClientEnvironment env){
            super(env);
            AbstractTristateCheckBox.this.addValueChangeListener(new InputBox.ValueChangeListener<T>(){
                @Override
                public void onValueChanged(T oldValue, T newValue) {
                    ValEditorControllerImpl.this.valueEdited(newValue);
                }
                
            });
        }

        @Override
        protected IValEditor<T, V> createValEditor() {
            return AbstractTristateCheckBox.this;
        }

        @Override
        public ValEditorController<T, V> getController() {
            return this;
        }
        
    }

    @Override
    public ValEditorController<T, V> getController() {
        return editorController;
    }

    @Override
    public void setInitialValue(final T value) {
       this.initialValue = value;
    }

    @Override
    public T getInitialValue() {
        return initialValue;
                
    }

    private final static class DisplayControllerImpl implements InputBox.DisplayController<Boolean> {

        @Override
        public String getDisplayValue(Boolean value, boolean isFocused, boolean isReadOnly) {
            if (value == null) {
                return "\u25FC";
            } else {
                return value.booleanValue() ? "\u2714" : " ";
            }
        }
    }

    private final static class ValueControllerImpl implements InputBox.ValueController<Boolean> {

        @Override
        public Boolean getValue(String text) throws InvalidStringValueException {
            Boolean res;
            if (text == null || text.isEmpty()) {
                res = null;
            } else if ("true".equals(text) || "Boolean.TRUE".equals(text) || "\u2714".equals(text)) {
                res = Boolean.TRUE;
            } else {
                res = Boolean.FALSE;
            }
            return res;
        }
    }

    private final class InternalInputBox extends InputBox<Boolean> {
        
        private WpsTextOptions textOptions;

        public InternalInputBox() {
            super(new DisplayControllerImpl(), new ValueControllerImpl());
            html.addClass("rwt-check-box");
        }

        @Override
        public void setTextOptions(final WpsTextOptions options) {
            super.setTextOptions(options.changeFontStyle(EFontStyle.NORMAL));
            textOptions = options;
            updateNullLabel();
            updateLabel();
        }

        public void updateNullLabel() {
            if (valueLabel != null) {
                valueLabel.setTextOptions(textOptions);              
                valueLabel.setBackground(null);
            }
        }

        public void updateLabel() {
            if (labelElement != null) {
                labelElement.setTextOptions(calculateLabelTextOptions(getTextOptionsMarkers()));
            }
        }
    }
    
    private final IClientEnvironment environment;
    private final InternalInputBox inputBox = new InternalInputBox();
    private final InternalValueChangeListener<T> valueChangeListener = new InternalValueChangeListener<>();
    private final TableLayout table = new TableLayout();
    private final TableLayout.Row row;
    private PriorityArray<ButtonBase> buttons;
    private final Label valueLabel;
    private final TableLayout.Row.Cell editorCell, nullLabelCell;
    private boolean canBeNull = true;
    private String nullValueStr;
    private final ValEditorController<T, V> editorController;
    private String label = "";
    private Label labelElement = null;
    private TableLayout.Row.Cell labelCell = null;
    private boolean titleVisible;
    private String trueTitle;
    private String falseTitle;
    private T initialValue;
    
    public AbstractTristateCheckBox(final IClientEnvironment environment){
        this(environment,null);
    }

    public AbstractTristateCheckBox(final IClientEnvironment environment, final ValEditorController<T,V> controller) {
        super(new Div());        
        this.environment = environment;
        this.html.add(table.getHtml());
        table.html.addClass("rwt-tristate-checkbox-table");
        table.setParent(AbstractTristateCheckBox.this);
        row = table.addRow();
        editorCell = row.addCell();
        editorCell.add(inputBox);
        inputBox.getHtml().addClass("ui-corner-right");
        inputBox.setInputFormat(InputFormat.Factory.boolInputFormat());
        inputBox.addValueChangeListener(new ValueChangeListener<Boolean>() {
            @Override
            public void onValueChanged(Boolean oldValue, Boolean newValue) {
                valueChanged(oldValue,newValue);
            }
        });
        inputBox.getHtml().setAttr("tabindex", "0");
        inputBox.getHtml().setAttr("onmousedown", "$RWT.inputBox.checkBoxClick");
        inputBox.getHtml().setAttr("is_clearable", "false");//initial value is already null

        nullLabelCell = row.addCell();
        valueLabel = new Label("", true);
        valueLabel.setDefaultClassName("rwt-ui-element");
        nullLabelCell.add(valueLabel);
        valueLabel.setTextWrapDisabled(true);
        valueLabel.getHtml().setCss("margin-left", "8px");
        inputBox.updateNullLabel();

        final TableLayout.Row.Cell spacerCell = row.addCell();
        spacerCell.setHSizePolicy(UIObject.SizePolicy.EXPAND);//для выравнивания влево
        spacerCell.getHtml().addClass("rwt-tristate-checkbox-spacer-cell");
        nullValueStr = environment.getMessageProvider().translate("Value", "<not defined>");
        editorController = controller==null ? new ValEditorControllerImpl(environment) : controller;
        subscribeToEvent(new KeyDownEventFilter(EKeyEvent.VK_SPACE));
    }

    public void setLabelVisible(boolean visible) {
        if (isLabelVisible() != visible) {
            if (visible) {
                labelCell = row.addCell(0);
                labelElement = new Label();
                labelElement.setTextWrapDisabled(true);
                labelElement.html.setCss("padding-right", "3px");
                if (label != null) {
                    labelElement.setText(label);
                }
                labelCell.add(labelElement);
                inputBox.updateLabel();
                labelCell.setWidth(1);
            } else {
                row.remove(labelCell);
                labelCell = null;
                label = null;
            }
        }
    }

    public boolean isLabelVisible() {
        return labelElement != null;
    }

    public void setLabel(String label) {
        this.label = label;
        if (isLabelVisible()) {
            labelElement.setText(label);
        }
    }

    public String getLabel() {
        return label;
    }

    @Override
    public void addValueChangeListener(final ValueChangeListener<T> listener) {
        valueChangeListener.addValueChangeListener(listener);
    }

    @Override
    public void removeValueChangeListener(final ValueChangeListener<T> listener) {
        valueChangeListener.removeValueChangeListener(listener);
    }

    @Override
    public void addStartChangeValueListener(final StartChangeValueListener listener) {
        inputBox.addStartChangeValueListener(listener);
    }

    @Override
    public void removeStartChangeValueListener(final StartChangeValueListener listener) {
        inputBox.removeStartChangeValueListener(listener);
    }

    @Override
    public void addFinishChangeValueListener(final FinishChangeValueListener listener) {
        inputBox.addFinishChangeValueListener(listener);
    }

    @Override
    public void removeFinishChangeValueListener(final FinishChangeValueListener listener) {
        inputBox.removeFinishChangeValueListener(listener);
    }

    @Override
    public void addUnacceptableInputListener(final UnacceptableInputListener listener) {
        inputBox.addUnacceptableInputListener(listener);
    }

    @Override
    public void removeUnacceptableInputListener(final UnacceptableInputListener listener) {
        inputBox.removeUnacceptableInputListener(listener);
    }
        
    @Override
    public void setValue(final T value) {
        inputBox.setValue(mapFromValueToBoolean(value));
    }

    @Override
    public T getValue() {
        return mapFromBooleanToValue(inputBox.getValue());
    }

    @Override
    public boolean isReadOnly() {
        return inputBox.isReadOnly();
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        inputBox.setReadOnly(readOnly);
    }
    
    @Override
    public void addButton(final IButton button) {
        addButton(button, IValEditor.DEFAULT_BUTTON_PRIORITY);
    }    

    @Override
    public void addButton(final IButton button, final int priority) {
        if (button instanceof ButtonBase) {
            final ButtonBase btn = (ButtonBase) button;
            final int index = registerButton(btn, priority);
            if (index>0){
                final TableLayout.Row.Cell cellForButton = row.addCell(index);
                cellForButton.getHtml().setAttr("buttonCell", "true");
                cellForButton.add(btn);
                setupButton(btn);
                cellForButton.setWidth(14);
            }
        }
    }

    @Override
    public void removeButton(final IButton button) {
        if (button instanceof ButtonBase && unregisterButton((ButtonBase)button)) {
            final List<TableLayout.Row.Cell> cells = row.getCells();
            TableLayout.Row.Cell cell;
            for (int i=0,count=cells.size(); i<count; i++){
                cell = cells.get(i);
                if (!cell.getChildren().isEmpty() && cell.getChildren().get(0)==button){
                    row.remove(cell);
                    break;
                }
            }
        }
    }
    
    private int registerButton(final ButtonBase button, final Integer priority){
        if (buttons==null){
            buttons = new PriorityArray<>(false, false);
        }
        final int buttonIndex;
        if (priority==null){
            buttonIndex = buttons.addWithLowestPriority(button);            
        }else{
            buttonIndex = buttons.addWithPriority(button, priority);            
        }
        return buttonIndex<0 ? -1 : buttonIndex+getFirstButtonCellIndex();
    }
    
    private boolean unregisterButton(final ButtonBase button){
        if (buttons!=null && buttons.remove(button)){
            if (buttons.isEmpty()){
                buttons = null;
            }
            return true;
        }
        return false;
    }    
    
    private int getFirstButtonCellIndex(){
        return labelCell==null ? 3 : 4;
    }

    @Override
    public void setButtonsVisible(final boolean isVisible) {
        if (isVisible){
            table.getHtml().removeClass("rwt-hidden-buttons");
        }else{
            table.getHtml().addClass("rwt-hidden-buttons");
        }
    }

    @Override
    public boolean isButtonsVisible() {
        return !table.getHtml().containsClass("rwt-hidden-buttons");
    }

    @Override
    public void setValidationResult(final ValidationResult validationResult) {
        if (ValidationResult.ACCEPTABLE == validationResult) {
            inputBox.setValidationMessage(null);
        } else {
            final String comment = 
                validationResult.getInvalidValueReason().getMessage(getEnvironment().getMessageProvider(), InvalidValueReason.EMessageType.Value);
            if (comment == null || comment.isEmpty()) {
                inputBox.setValidationMessage(environment.getMessageProvider().translate("Value", "Current value is invalid"));
            } else {
                inputBox.setValidationMessage(comment);
            }
        }
    }

    @Override
    public ValidationResult getValidationResult() {
        final String message = inputBox.getValidationMessage();
        if (message == null || message.isEmpty()) {
            return ValidationResult.ACCEPTABLE;
        } else {
            return ValidationResult.Factory.newInvalidResult(message);
        }
    }

    @Override
    public boolean hasAcceptableInput() {
        return true;
    }

    @Override
    public UnacceptableInput getUnacceptableInput() {
        return null;
    }

    @Override
    public void setInputText(final String inputText) {
        //empty implementation
    }

    @Override
    public boolean checkInput(String messageTitle, String firstMessageLine) {
        return true;
    }

    @Override
    public boolean checkInput() {
        return true;
    }                

    @Override
    public void refresh() {
        inputBox.updateDisplayString();
    }

    @Override
    public UIObject findObjectByHtmlId(final String id) {
        final UIObject result = super.findObjectByHtmlId(id);
        return result == null ? table.findObjectByHtmlId(id) : result;
    }

    public boolean canBeNull() {
        return canBeNull;
    }

    public void setCanBeNull(final boolean canBeNull) {
        if (canBeNull != this.canBeNull) {
            this.canBeNull = canBeNull;
            inputBox.getHtml().setAttr("is_clearable", canBeNull && getValue() != null);
        }
    }

    public String getNullValueString() {
        return nullValueStr;
    }

    public void setNullValueString(final String newStr) {
        nullValueStr = newStr;
        updateNullLabel(mapFromValueToBoolean(getValue()));
    }

    public InputBox<Boolean> getInputBox() {
        return inputBox;
    }

    private void setupButton(final ButtonBase button) {
        button.setIconHeight(12);
        button.setIconWidth(12);
        button.setWidth(14);
        button.setHeight(19);
        button.getHtml().setCss("margin-left", "8px");
        button.getHtml().addClass("ui-corner-left");
        button.getHtml().addClass("ui-corner-right");
    }

    private void valueChanged(final Boolean oldValue, final Boolean newValue) {
        inputBox.setForeground(newValue == null ? Color.gray : null);
        inputBox.getHtml().setAttr("is_clearable", canBeNull && newValue != null);
        updateNullLabel(newValue);
        inputBox.updateDisplayString();
        final T oldTValue = mapFromBooleanToValue(oldValue);
        final T newTValue = mapFromBooleanToValue(newValue);        
        valueChangeListener.onValueChanged(oldTValue, newTValue);        
    }

    private void updateNullLabel(final Boolean value) {
        if (value == null) {
            valueLabel.setText(nullValueStr);
            valueLabel.setVisible(true);
        } else {
            if (isValueTitleVisible()) {
                valueLabel.setVisible(true);
                final String labelText = value == Boolean.TRUE ? getTrueTitle() : (value == Boolean.FALSE ? getFalseTitle() : "<not defined>");
                valueLabel.setText(labelText);
            } else {
                valueLabel.setVisible(false);
            }
        }
    }

    @Override
    protected String[] clientCssRequired() {
        return Utils.merge(super.clientCssRequired(), inputBox.clientCssRequired());
    }

    @Override
    protected String[] clientScriptsRequired() {
        return Utils.merge(super.clientScriptsRequired(), inputBox.clientScriptsRequired());
    }

    @Override
    public boolean addTextOptionsMarkers(ETextOptionsMarker... markers) {
        return inputBox.addTextOptionsMarkers(markers);
    }

    @Override
    public boolean removeTextOptionsMarkers(ETextOptionsMarker... markers) {
        return inputBox.removeTextOptionsMarkers(markers);
    }

    @Override
    public boolean setTextOptionsMarkers(ETextOptionsMarker... markers) {
        return inputBox.setTextOptionsMarkers(markers);
    }

    @Override
    public EnumSet<ETextOptionsMarker> getTextOptionsMarkers() {
        return inputBox.getTextOptionsMarkers();
    }

    @Override
    public void setTextOptionsProvider(final ITextOptionsProvider textOptionsProvider) {
        inputBox.setTextOptionsProvider(textOptionsProvider);
    }

    @Override
    public void setDefaultTextOptions(final WpsTextOptions options) {
        inputBox.setDefaultTextOptions(options);
    }

    @Override
    public void setTextOptionsForMarker(final ETextOptionsMarker marker, final WpsTextOptions options) {
        inputBox.setTextOptionsForMarker(marker, options);
    }

    @Override
    public void refreshTextOptions() {
        inputBox.refreshTextOptions();
    }

    @Override
    public WpsTextOptions getTextOptions() {
        return inputBox.getTextOptions();
    }

    public void setTrueTitle(String trueTitle) {
        this.trueTitle = trueTitle;
    }

    public void setFalseTitle(String falseTitle) {
        this.falseTitle = falseTitle;
    }

    public void setValueTitleVisible(boolean visible) {
        titleVisible = visible;
    }

    public boolean isValueTitleVisible() {
        return this.titleVisible;
    }

    public String getTrueTitle() {
        return trueTitle;
    }

    public String getFalseTitle() {
        return falseTitle;
    }    
    
    protected abstract T mapFromBooleanToValue(final Boolean b);
    
    protected abstract Boolean mapFromValueToBoolean(final T value);

    @Override
    protected void processHtmlEvent(final HtmlEvent event) {
        if (event instanceof KeyDownHtmlEvent){
            final KeyDownHtmlEvent keyEvent = (KeyDownHtmlEvent)event;
            if (keyEvent.getButton()==EKeyEvent.VK_SPACE.getValue() || keyEvent.getKeyboardModifiers().isEmpty()){
                changeValue();
                return;
            }
        }        
        super.processHtmlEvent(event);
    }
    
    private void changeValue(){
        if (!isReadOnly() && isEnabled()){
            final Boolean currentValue = mapFromValueToBoolean(getValue());
            final Boolean newValue;
            if (currentValue==null){
                newValue = Boolean.TRUE;
            }else if (currentValue.equals(Boolean.FALSE)){
                newValue = getController().isMandatory() ? Boolean.TRUE : null;
            }else{
                newValue = Boolean.FALSE;
            }
            setValue(mapFromBooleanToValue(newValue));
        }
    }
}
