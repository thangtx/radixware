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
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ImageManager;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.text.CustomTextOptions;
import org.radixware.kernel.common.client.text.ITextOptionsManager;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.client.text.MergedTextOptionsProvider;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.PriorityArray;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.icons.images.WsIcons;
import org.radixware.wps.rwt.TableLayout.Row.Cell;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.ICssStyledItem;
import org.radixware.kernel.common.html.ToolTip;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.text.ECssTextOptionsStyle;
import org.radixware.wps.text.WpsTextOptions;
import org.radixware.wps.views.editors.valeditors.IValEditor;


public class InputBox<T> extends UIObject implements ValueEditor<T> {        

    private static enum EAttrName {

        IsClearable("is_clearable"),
        IsValid("isvalid"),
        ValidationMessage("validationMessage"),
        ValueIsNull("value_is_null"),
        MaxSpinDownStepCount("maxSpinDownStepCount"),
        MaxSpinUpStepCount("maxSpinUpStepCount"),
        FocusedText("focusedtext"),
        InitialText("initialtext"),
        InputHandlers("inputHandlers"),
        ButtonCell("buttonCell");
        
        private final String attrName;

        private EAttrName(final String attrName) {
            this.attrName = attrName;
        }

        public String getAttrName() {
            return attrName;
        }
    }
    
    private class Cell2Button {

        private final TableLayout.Row.Cell cell;
        private final ButtonBase button;

        public Cell2Button(Cell cell, ButtonBase button) {
            this.cell = cell;
            this.button = button;
            this.button.addPropertyListener(new UIObjectPropertyListener() {
                @Override
                public void propertyChange(String name, Object oldValue, Object value) {
                    if (UIObject.Properties.VISIBLE.equals(name)) {
                        Cell2Button.this.cell.setVisible(Cell2Button.this.button.isVisible());
                    }
                    updataButtonsState();
                }
            });
            this.cell.setVisible(this.button.isVisible());
            updataButtonsState();
        }
    }    
    
    private final static int ICON_SIZE = 16;
    private TableLayout table = new TableLayout();    
    private TableLayout.Row row;
    private TableLayout.Row.Cell editorCell;
    private final TextField textField;
    private Label label;
    private ButtonBase clearButton = null;
    private ButtonBase spinUpButton = null;
    private ButtonBase spinDnButton = null;
    private Image valueImage = null;
    private InvalidValueIcon invalidValueIcon = null;
    private TableLayout.Row.Cell spinButtonCell = null;
    private TableLayout.Row.Cell clearButtonCell = null;
    private TableLayout.Row.Cell imageCell = null;
    private TableLayout.Row.Cell invalidValueIconCell = null;
    private List<Cell2Button> cells2Buttons = null;
    private PriorityArray<ButtonBase> buttons = null;
    private ClearController<T> clearController;
    private DisplayController<T> displayController;
    private SpinBoxController<T> spinBoxController;
    private ValueController<T> valueController;
    //private ListBox dropDownList;
    private DropDownDelegate<? extends UIObject> activeDropDown;
    private Map<DropDownDelegate<? extends UIObject>, ToolButton> dropDownDelegates;
    private T value;
    private String labelText;
    private Color labelColor;
    private UnacceptableInput unacceptableInput;
    private final DefaultValueChangeListener<T> valueChangelistener = new DefaultValueChangeListener<>();
    private final DefaultStartChangeValueListener startChangeValueListener =
            new DefaultStartChangeValueListener();
    private final DefaultFinishChangeValueListener finishChangeValueListener =
            new DefaultFinishChangeValueListener();
    private final DefaultUnacceptableInputListener unacceptableInputListener = 
            new DefaultUnacceptableInputListener();
    private String validationMessage = "";
    private WpsTextOptions textOptions;
    private EnumSet<ETextOptionsMarker> textOptionsMarkers = EnumSet.of(ETextOptionsMarker.EDITOR);
    private CustomTextOptions customTextOptions;
    private ITextOptionsProvider customTextOptionsProvider;
    private ITextOptionsProvider provider;
    private boolean isReadOnly;
    private boolean isEnabled = true;

    @Override
    public void addValueChangeListener(final ValueEditor.ValueChangeListener<T> listener) {
        valueChangelistener.addValueChangeListener(listener);
    }

    @Override
    public void removeValueChangeListener(final ValueEditor.ValueChangeListener<T> listener) {
        valueChangelistener.removeValueChangeListener(listener);
    }

    @Override
    public void addStartChangeValueListener(final StartChangeValueListener listener) {
        startChangeValueListener.addListener(listener);
    }

    @Override
    public void removeStartChangeValueListener(final StartChangeValueListener listener) {
        startChangeValueListener.removeListener(listener);
    }

    @Override
    public void addFinishChangeValueListener(final FinishChangeValueListener listener) {
        finishChangeValueListener.addListener(listener);
    }

    @Override
    public void removeFinishChangeValueListener(final FinishChangeValueListener listener) {
        finishChangeValueListener.removeListener(listener);
    }

    @Override
    public void addUnacceptableInputListener(final UnacceptableInputListener listener) {
        unacceptableInputListener.addListener(listener);
    }

    @Override
    public void removeUnacceptableInputListener(final UnacceptableInputListener listener) {
        unacceptableInputListener.removeListener(listener);
    }        

    private static class DefaultValueChangeListener<T> implements ValueEditor.ValueChangeListener<T> {

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

    private static class DefaultStartChangeValueListener implements ValueEditor.StartChangeValueListener {

        private List<ValueEditor.StartChangeValueListener> listeners = null;

        public void addListener(final ValueEditor.StartChangeValueListener listener) {
            if (listeners == null) {
                listeners = new LinkedList<>();
                listeners.add(listener);
            } else if (!listeners.contains(listener)) {
                listeners.remove(listener);
            }
        }

        public void removeListener(final ValueEditor.StartChangeValueListener listener) {
            if (listeners != null) {
                listeners.remove(listener);
            }
        }

        @Override
        public void onStartChangeValue() {
            if (listeners != null) {
                final List<ValueEditor.StartChangeValueListener> listenersList = new LinkedList<>(listeners);
                for (ValueEditor.StartChangeValueListener listener : listenersList) {
                    listener.onStartChangeValue();
                }
            }
        }
    }        

    private static class DefaultFinishChangeValueListener implements ValueEditor.FinishChangeValueListener {

        private List<ValueEditor.FinishChangeValueListener> listeners = null;

        public void addListener(final ValueEditor.FinishChangeValueListener listener) {
            if (listeners == null) {
                listeners = new LinkedList<>();
                listeners.add(listener);
            } else if (!listeners.contains(listener)) {
                listeners.remove(listener);
            }
        }

        public void removeListener(final ValueEditor.FinishChangeValueListener listener) {
            if (listener != null) {
                listeners.remove(listener);
            }
        }

        @Override
        public void onFinishChangeValue(final boolean valueAccepted) {
            if (listeners != null) {
                final List<ValueEditor.FinishChangeValueListener> listenersList = new LinkedList<>(listeners);
                for (ValueEditor.FinishChangeValueListener listener : listenersList) {
                    listener.onFinishChangeValue(valueAccepted);
                }
            }
        }
    }
    
    private static class DefaultUnacceptableInputListener implements ValueEditor.UnacceptableInputListener{
        
        private List<ValueEditor.UnacceptableInputListener> listeners = null;
        
        public void addListener(final ValueEditor.UnacceptableInputListener listener){
            if (listeners == null){
                listeners = new LinkedList<>();
                listeners.add(listener);
            }else if (!listeners.contains(listener)){
                listeners.remove(listener);
            }
        }
        
        public void removeListener(final ValueEditor.UnacceptableInputListener listener){
            if (listeners != null){
                listeners.remove(listener);
            }
        }

        @Override
        public void onUnacceptableInputChanged(final UnacceptableInput previous, final UnacceptableInput current) {
            if (listeners != null ){
                final List<ValueEditor.UnacceptableInputListener> listenersList = new LinkedList<>(listeners);
                for (ValueEditor.UnacceptableInputListener listener : listenersList){
                    listener.onUnacceptableInputChanged(previous, current);
                }
            }
        }
                        
    }

    public interface ClearController<T> {

        public T clear();
    }

    public interface SpinBoxController<T> {

        public T getNext(T value, int delta);

        public T getPrev(T value, int delta);

        public void updateButtons(final InputBox box, T value);
    }

    public static abstract class DropDownDelegate<U extends UIObject> {

        private U dropDown;

        protected abstract U createUIObject(final InputBox box, final DisplayController displayController);

        protected abstract ToolButton createDropDownButton();

        protected abstract void updateButton(ToolButton button, InputBox inputBox);

        protected List<String> getInputHandlers(final InputBox box) {
            return Collections.emptyList();
        }

        protected void handleHotKey(final String hotKey, final InputBox box) {
        }

        protected void handleMouseWheel(final int wheel, final InputBox box) {
        }

        public U getDropDown() {
            return dropDown;
        }

        protected String getActiveElementName() {
            return null;
        }

        public void afterClose(final InputBox box) {
            if (dropDown != null) {
                final RootPanel root = box.findRoot();
                root.remove(dropDown);
                dropDown = null;
            }
        }

        @SuppressWarnings("unchecked")
        public final void expose(final InputBox box) {
            final RootPanel root = box.findRoot();
            if (root != null) {
                dropDown = createUIObject(box, box.displayController);
                root.add(dropDown);
                dropDown.getHtml().addClass("rwt-drop-down");
                dropDown.getHtml().setAttr("active_element", getActiveElementName());
                dropDown.getHtml().setAttr("handler_id", box.getHtml().getId());
                dropDown.getHtml().setAttr("relativeElem", "textFieldCell");
                box.getHtml().setAttr("relativeElemId", box.getHtmlId());
                dropDown.getHtml().setCss("position", "absolute");
                dropDown.getHtml().layout("$RWT.dropDown.layout");
                box.activeDropDown = this;
                dropDown.setVisible(false);
            }
        }

        protected boolean canChangeValue(final InputBox box) {
            return !box.isReadOnly;
        }
    }

    public interface DisplayController<T> {

        public String getDisplayValue(T value, boolean isFocused, boolean isReadOnly);
    }

    public static class InvalidStringValueException extends Exception {
        
        private static final long serialVersionUID = -1414868135507115430L;
        
        private final InvalidValueReason reason;

        public InvalidStringValueException(final String message) {
            super(message);
            reason = InvalidValueReason.Factory.createForInvalidValue(message);
        }
        
        public InvalidStringValueException(final MessageProvider mp, final InvalidValueReason reason){
            super(reason.getMessage(mp, InvalidValueReason.EMessageType.Value));
            this.reason = reason;
        }
        
        public InvalidValueReason getReason(){
            return reason;
        }
    }

    public interface ValueController<T> {

        public T getValue(String text) throws InvalidStringValueException;
    }

    public InputBox() {
        this(null);
    }

    public InputBox(final ValueController<T> valueController) {
        this(new DisplayController<T>() {
            @Override
            public String getDisplayValue(T value, boolean isFocused, boolean isReadOnly) {
                return value == null ? "<value is not set>" : value.toString();
            }
        }, valueController);
    }

    public void setPassword(boolean password) {
        textField.setPassword(password);
    }

    public boolean isPassword() {
        return textField.isPassword();
    }

    public InputBox(final DisplayController<T> displayController, final ValueController<T> valueController) {
        super(new Div());
        html.addClass("rwt-input-box");
        this.html.add(table.getHtml());
        table.setParent(InputBox.this);
        this.row = table.addRow();
        this.editorCell = row.addCell();
        this.editorCell.add(textField = new TextField(""));
        this.editorCell.getHtml().addClass("ui-corner-left");        
        this.displayController = displayController;
        setValueController(valueController);
        setHeight(20);
        updateDisplayString();
        textField.addTextListener(new TextField.TextChangeListener() {
            @Override
            public void textChanged(final String oldText, final String newText) {
                onTextChanged(newText);
            }
        });
        textField.addStartTextChangeListener(new TextField.StartTextChangeListener() {
            @Override
            public void startTextChange(final String changedText) {
                if (InputBox.this.valueController != null) {
                    try {
                        InputBox.this.valueController.getValue(changedText);
                    } catch (InvalidStringValueException ex) {
                        InputBox.this.textField.setStartModificationActionEnabled(true);
                        return;
                    }
                    startChangeValueListener.onStartChangeValue();
                }
            }
        });
        textField.addFinishTextChangeListener(new TextField.FinishTextChangeListener() {
            @Override
            public void finishTextChange(final boolean accepted) {
                finishChangeValueListener.onFinishChangeValue(accepted);
            }
        });
        html.layout("$RWT.inputBox.layout");
        updateButtonsStyle();
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusEvent(UIObject target, boolean focused) {
                markAsFocused(focused);
            }
        });
        textField.setClientOnFocusHandler("$RWT.inputBox.textFieldFocusIn");
        textField.setClientOnBlurHandler("$RWT.inputBox.textFieldFocusOut");
        html.setAttr(EAttrName.ValidationMessage.getAttrName(), "");
        html.setAttr(EAttrName.IsValid.getAttrName(), "true");
        html.setAttr(EAttrName.IsClearable.getAttrName(), "false");
        textField.getHtml().setAttr(EAttrName.ValueIsNull.getAttrName(), "true");
        textField.setHeight(19);
        //html.setAttr("onmousewheel", "$RWT.inputBox.mousewheel");RADIX-7110
        html.setAttr("ondblclick", "$RWT.inputBox.dblclick");
        setClientHandler("requestFocus", "$RWT.inputBox.requestFocus");
        provider = getEnvironment().getTextOptionsProvider();
        textOptions = WpsTextOptions.getDefault((WpsEnvironment) getEnvironment());
        textOptionsMarkers.add(ETextOptionsMarker.UNDEFINED_VALUE);
        refreshTextOptions();
    }
    
    private void onTextChanged(final String inputText){
        if (InputBox.this.valueController != null) {
            try {
                InputBox.this.html.setAttr(EAttrName.IsValid.getAttrName(), true);
                if (!setValueInternal(InputBox.this.valueController.getValue(inputText))) {
                    finishChangeValueListener.onFinishChangeValue(false);
                }
            } catch (InvalidStringValueException ex) {
                enterUnacceptableInputMode(ex.getReason(), inputText);
                setFocusedText(null);
                if (clearButton != null) {
                    clearButton.setVisible(!isReadOnly);
                }
                finishChangeValueListener.onFinishChangeValue(false);
            }
        }        
    }

    public final void setValueController(final ValueController<T> valueController) {
        this.valueController = valueController;
        updateTextFieldReadOnlyMarker();
    }

    public void setLabelVisible(boolean labelVisible) {
        if (labelVisible != isLabelVisible()) {
            if (labelVisible) {
                label = createLabel();
                Cell cell = row.addCell(0);
                cell.getHtml().addClass("rwt-value-label");
                cell.add(label);
                label.getHtml().setCss("padding-right", "3px");
                label.setTextWrapDisabled(true);
                cell.getHtml().setCss("width", "1px");
                cell.getHtml().setCss("background-color", "transparent");
                cell.getHtml().setCss("border-left", "none");
                cell.getHtml().setCss("border-left", "none");
                cell.getHtml().setCss("border-top", "none");
                cell.getHtml().setCss("border-bottom", "none");
                refreshTextOptions();
            } else {
                this.label.setParent(null);
                this.label.getHtml().remove();
                row.remove(row.getCell(0));
                this.label = null;
            }
            updateButtonsStyle();
        }
    }

    public void setLabel(String labelText) {
        this.labelText = labelText;
        if (label != null) {
            label.setText(labelText);
        }
    }

    public void setLabelColor(final Color color) {
        labelColor = color;
        if (label != null) {
            refreshTextOptions();
        }
    }

    public Color getLabelColor() {
        return labelColor;
    }

    public String getLabel() {
        return labelText;
    }

    protected Label createLabel() {
        Label lbl = new Label();
        if (labelText != null) {
            lbl.setText(labelText);
        }
        return lbl;
    }

    public boolean getLabelVisible() {
        return isLabelVisible();
    }

    public final boolean isLabelVisible() {
        return label != null;
    }

    public final ValueController<T> getValueController() {
        return valueController;
    }

    public void setClientOnFocusHandler(final String method_name) {
        html.setAttr("rwt_f_focusIn", method_name);
    }

    public void setClientOnBlurHandler(final String method_name) {
        html.setAttr("rwt_f_focusOut", method_name);
    }

    @Override
    protected String[] clientScriptsRequired() {
        return Utils.merge(new String[]{"org/radixware/wps/rwt/jquery.hotkeys-0.7.9.min.js", "org/radixware/wps/rwt/inputBox.js"}, table.clientScriptsRequired());
    }

    private void markAsFocused(boolean focused) {
        if (focused) {
            html.addClass("rwt-input-box-focused");
        } else {
            html.removeClass("rwt-input-box-focused");
        }
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        table.visit(visitor);
        if (activeDropDown != null) {
            activeDropDown.getDropDown().visit(visitor);
        }
    }

    public void setDisplayController(final DisplayController<T> c) {
        this.displayController = c;
        updateDisplayString();
    }

    public DisplayController<T> getDisplayController() {
        return displayController;
    }

    public final String getValidationMessage() {
        return validationMessage;
    }

    public final void setValidationMessage(final String validationMessage) {
        this.validationMessage = validationMessage;
        updateInvalidValueIcon();
    }

    @Override
    public boolean hasAcceptableInput() {
        return unacceptableInput==null;
    }

    @Override
    public UnacceptableInput getUnacceptableInput() {
        return unacceptableInput;
    }        

    @Override
    public boolean checkInput(final String messageTitle, final String firstMessageLine) {
        if (hasAcceptableInput()){
            return true;
        }else{
            getUnacceptableInput().showMessage(messageTitle, firstMessageLine);
            setFocused(true);
            return false;
        }
    }

    @Override
    public boolean checkInput() {
        return checkInput(null, null);
    }    

    @Override
    public void setHtmlToolTip(ToolTip toolTip) {
        editorCell.setHtmlToolTip(toolTip); 
    }
     
    private void updateInvalidValueIcon() {
        final RootPanel root = findRoot();
        if ((!isReadOnly() || validationMessage == null) && root != null) {
            if (validationMessage != null && !validationMessage.isEmpty()) {
                if (invalidValueIconCell == null) {
                    invalidValueIcon = new InvalidValueIcon(root);
                    invalidValueIcon.html.setAttr("onmousedown", "$RWT.defaultMousedown");
                    int invalidValueIconCellIndex=1;
                    if (imageCell!=null){
                        invalidValueIconCellIndex++;
                    }
                    if (label!=null){
                        invalidValueIconCellIndex++;
                    }
                    invalidValueIconCell = row.addCell(invalidValueIconCellIndex);
                    invalidValueIconCell.add(invalidValueIcon);
                    invalidValueIconCell.setWidth(ICON_SIZE + 6);
                    invalidValueIconCell.getHtml().setCss("border-left-width", "0px");
                    setIconCellBackground(invalidValueIconCell, textOptions);
                    addTextOptionsMarkers(ETextOptionsMarker.INVALID_VALUE);
                }
                invalidValueIcon.setTooltipText(validationMessage);
            } else if (invalidValueIconCell != null) {
                row.remove(invalidValueIconCell);
                invalidValueIcon.close();
                invalidValueIconCell = null;
                invalidValueIcon = null;
                removeTextOptionsMarkers(ETextOptionsMarker.INVALID_VALUE);
            }
            html.setAttr(EAttrName.ValidationMessage.getAttrName(), validationMessage);
            html.setAttr(EAttrName.IsValid.getAttrName(), validationMessage == null || validationMessage.isEmpty());
            updateButtonsStyle();
        }
    }

    private void enterUnacceptableInputMode(final InvalidValueReason reason, final String inputText) {
        if (reason!=null) {
            enterUnacceptableInputMode(new UnacceptableInput(getEnvironment(), reason, inputText));
        }
    }
    
    private void enterUnacceptableInputMode(final UnacceptableInput input) {
        if (!Utils.equals(unacceptableInput, input)){
            html.setAttr(EAttrName.IsValid.getAttrName(), false);
            setValidationMessage(input.getMessageText());
            final UnacceptableInput previous = unacceptableInput;
            unacceptableInput = input;
            unacceptableInputListener.onUnacceptableInputChanged(previous, input);
            setFocusedText(null);
        }
    }
    
    @Override
    public void setInputText(final String inputText) {
        if (!Objects.equals(textField.getText(), inputText)){
            textField.setTextNoFire(inputText);
            onTextChanged(inputText);
        }
    }

    private void leaveUnacceptableInputMode() {
        if (unacceptableInput!=null){
            html.setAttr(EAttrName.IsValid.getAttrName(), true);
            setValidationMessage("");
            final UnacceptableInput previous = unacceptableInput;
            unacceptableInput = null;
            unacceptableInputListener.onUnacceptableInputChanged(previous, null);
        }
    }

    public final void setMaxLength(final int maxLength) {
        textField.setMaxLength(maxLength);
    }

    public final void setInputFormat(final InputFormat inputFormat) {
        textField.setInputFormat(inputFormat);
    }

    public final void setFocusedText(final String focusedText) {
        html.setAttr(EAttrName.FocusedText.getAttrName(), focusedText);
    }

    public final void setInitialText(final String initialText) {
        textField.getHtml().setAttr(EAttrName.InitialText.getAttrName(), initialText);
    }

    @Override
    public void setForeground(Color c) {
        super.setForeground(c);
        textField.setForeground(c);
    }

    @Override
    public void setParent(final UIObject newParent) {
        final UIObject prevRoot = findRoot();
        super.setParent(newParent);
        if (prevRoot != findRoot() && getValidationMessage() != null) {//NOPMD
            updateInvalidValueIcon();
        }
    }

    @Override
    public UIObject findObjectByHtmlId(final String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        result = table.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        if (activeDropDown != null) {
            result = activeDropDown.getDropDown().findObjectByHtmlId(id);
        }
        return result;
    }

    public void setClearController(ClearController<T> clearController) {
        if (clearController != null) {
            if (this.clearController == null) {
                clearButton = new ToolButton();
                clearButton.setIcon(getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.CLEAR));
                clearButton.setToolTip(getEnvironment().getMessageProvider().translate("Value", "Clear Value"));
                clearButton.getHtml().addClass("rwt-clear-button");
                clearButton.setObjectName("tbClear");
                clearButtonCell = row.addCell(registerButton(clearButton, null));
                clearButtonCell.getHtml().setAttr(EAttrName.ButtonCell.getAttrName(), true);                
                clearButtonCell.add(clearButton);
                clearButton.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(IButton source) {
                        setValue(InputBox.this.clearController.clear());
                    }
                });
                setupButton(clearButton);
            }
            this.clearController = clearController;
        } else {
            if (clearButton != null) {
                unregisterButton(clearButton);
                row.remove(clearButtonCell);
                clearButton = null;
                clearButtonCell = null;
            }
            this.clearController = null;
        }
        html.setAttr(EAttrName.IsClearable.getAttrName(), clearController != null);
        updataButtonsState();
        updateValuesMarker();
    }

    public boolean isClearable() {
        return clearController != null;
    }

    private static class SpinButton extends ToolButton {

        public SpinButton(boolean down) {
            html.setCss("float", "none");
            setWidth(14);

            setIconWidth(8);
            setIconHeight(5);
            if (down) {
                setHeight(8);
                html.addClass("rwt-input-box-spin-dn-button");
            } else {
                setHeight(8);
                html.addClass("rwt-input-box-spin-up-button");
            }
        }
    }

    public final void setMaxSpinUpStepCount(final int count) {
        html.setAttr(EAttrName.MaxSpinUpStepCount.getAttrName(), count);
        if (spinUpButton != null) {
            spinUpButton.setEnabled(textField.isEnabled() && count > 0);
        }
    }

    public final int getMaxSpinUpStepCount() {
        return Integer.parseInt(html.getAttr(EAttrName.MaxSpinUpStepCount.getAttrName()));
    }

    public final void setMaxSpinDownStepCount(final int count) {
        html.setAttr(EAttrName.MaxSpinDownStepCount.getAttrName(), count);
        if (spinDnButton != null) {
            spinDnButton.setEnabled(textField.isEnabled() && count > 0);
        }
    }

    public final int getMaxSpinDownStepCount() {
        return Integer.parseInt(html.getAttr(EAttrName.MaxSpinDownStepCount.getAttrName()));
    }

    public final void showSpinButtons(final boolean show) {
        if (spinButtonCell != null) {
            spinButtonCell.setVisible(show);
        }
    }

    public void setSpinBoxController(SpinBoxController<T> spinBoxController) {
        if (spinBoxController != null) {
            if (spinButtonCell == null) {
                
                spinUpButton = new SpinButton(false);
                spinUpButton.setObjectName("tbSpinUp");
                spinUpButton.setIcon(WsIcons.SPIN_UP);

                spinDnButton = new SpinButton(true);
                spinDnButton.setObjectName("tbSpinDown");
                spinDnButton.setIcon(WsIcons.SPIN_DOWN);                                

                spinUpButton.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(IButton source) {
                        setValue(InputBox.this.spinBoxController.getNext(getValue(), 1));
                    }
                });
                spinDnButton.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(IButton source) {
                        setValue(InputBox.this.spinBoxController.getPrev(getValue(), 1));
                    }
                });
                
                if (buttons==null){
                    buttons = new PriorityArray<>(false, false);
                }
                final int index = buttons.addWithHighestPriority(spinUpButton);
                spinButtonCell = row.addCell(index+getFirstButtonCellIndex());
                spinButtonCell.getHtml().setAttr(EAttrName.ButtonCell.getAttrName(), true);
                
                spinButtonCell.add(spinUpButton);
                spinButtonCell.add(spinDnButton);
                spinButtonCell.setWidth(14);

            }
            this.spinBoxController = spinBoxController;
        } else {
            if (spinButtonCell != null) {
                if (buttons!=null){
                    buttons.remove(spinUpButton);
                }
                row.remove(spinButtonCell);
                spinButtonCell = null;
                spinUpButton = null;
                spinDnButton = null;
            }
            this.spinBoxController = null;
        }
        updateInputHandlers();
        updataButtonsState();

    }    

    private void setupButton(ButtonBase button) {
        if (button.getIconHeight() > 12) {
            button.setIconHeight(12);
        }
        if (button.getIconWidth() > 12) {
            button.setIconWidth(12);
        }
        button.setWidth(14);
        button.setHeight(19);
        TableLayout.Row.Cell buttonCell = (TableLayout.Row.Cell) button.getParent();
        buttonCell.setWidth(14);
        if (cells2Buttons == null) {
            cells2Buttons = new LinkedList<>();
        }
        cells2Buttons.add(new Cell2Button(buttonCell, button));
    }

    public ToolButton addDropDownDelegate(final DropDownDelegate<? extends UIObject> delegate) {
        final ToolButton button = delegate.createDropDownButton();
        if (dropDownDelegates == null) {
            dropDownDelegates = new HashMap<>();
        }
        if (delegate instanceof DropDownEditHistoryDelegate) {
            button.setIcon(getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.EDITING_HISTORY));
            button.setIconSize(ICON_SIZE, ICON_SIZE);
            button.setObjectName("btValHistory");
        } 
        dropDownDelegates.put(delegate, button);
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final IButton source) {
                delegate.expose(InputBox.this);
            }
        });
        updateInputHandlers();
        addCustomButton(button,Integer.MIN_VALUE);
        return button;
    }
    
    public void removeDropDownDelegate(final DropDownDelegate<? extends UIObject> delegate){
        if (dropDownDelegates!=null){
            final ToolButton button = dropDownDelegates.remove(delegate);
            if (button!=null){
                if (dropDownDelegates.isEmpty()){
                    dropDownDelegates = null;
                }
                removeCustomButton(button);
                updateInputHandlers();
            }
        }
    }

    private void updateInputHandlers() {
        if (dropDownDelegates == null || dropDownDelegates.isEmpty() || spinBoxController != null) {
            html.setAttr(EAttrName.InputHandlers.getAttrName(), null);
        } else {
            final List<String> allHandlers = new LinkedList<>();
            for (DropDownDelegate<? extends UIObject> delegate : dropDownDelegates.keySet()) {
                final List<String> handlers = delegate.getInputHandlers(this);
                for (String handle : handlers) {
                    if (!allHandlers.contains(handle)) {
                        allHandlers.add(handle);
                    }
                }
            }
            if (allHandlers.isEmpty()) {
                html.setAttr(EAttrName.InputHandlers.getAttrName(), null);
            } else {
                final StringBuilder inputHandlers = new StringBuilder();
                inputHandlers.append('[');
                for (String handle : allHandlers) {
                    if (inputHandlers.length() > 1) {
                        inputHandlers.append(',');
                    }
                    inputHandlers.append('\'');
                    inputHandlers.append(handle);
                    inputHandlers.append('\'');
                }
                inputHandlers.append(']');
                html.setAttr(EAttrName.InputHandlers.getAttrName(), inputHandlers.toString());
            }
        }
    }

    public final void closeActiveDropDown() {
        if (activeDropDown != null) {
            activeDropDown.afterClose(this);
            activeDropDown = null;
            InputBox.this.getHtml().setAttr("relativeElemId", null);
        }
    }

    public void setValueIcon(final Icon valueIcon) {
        if (valueIcon != null) {
            if (imageCell == null) {
                valueImage = new Image();
                valueImage.setHeight(ICON_SIZE);
                valueImage.setWidth(ICON_SIZE);
                imageCell = row.addCell(0);
                imageCell.add(valueImage);
                imageCell.getHtml().addClass("ui-corner-left");
                imageCell.getHtml().addClass("rwt-value-icon");
                setIconCellBackground(imageCell, textOptions);
                editorCell.getHtml().removeClass("ui-corner-left");
                editorCell.getHtml().setCss("border-left-width", "0px");
            }
            valueImage.setIcon((WpsIcon) valueIcon);
        } else if (imageCell != null) {
            row.remove(imageCell);
            imageCell = null;
            valueImage = null;
            editorCell.getHtml().addClass("ui-corner-left");
            editorCell.getHtml().setCss("border-left-width", null);
        }
    }

    public final void updateDisplayString() {
        if (unacceptableInput==null){
            textField.setTextNoFire(displayController.getDisplayValue(value, false, isReadOnly()));
        }else{
            textField.setTextNoFire(unacceptableInput.getText());
        }
    }

    public T getValue() {
        return value;
    }

    private boolean equals(final T value1, final T value2) {
        if (value1 instanceof Reference && value2 instanceof Reference){
            return Reference.exactlyMatch((Reference)value1, (Reference)value2);
        }        
        return Objects.equals(normalizeToCompare(value1), normalizeToCompare(value2));
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")//used in equals method
    private static Object normalizeToCompare(final Object value) {
        return value instanceof XmlObject ? ((XmlObject) value).xmlText() : value;
    }

    private boolean updateValue(final T value) {
        if (!equals(this.value, value)) {
            if (!hasAcceptableInput()) {
                leaveUnacceptableInputMode();
            }
            T oldValue = this.value;
            this.value = value;            
            valueChangelistener.onValueChanged(oldValue, this.value);
            if (spinBoxController != null) {
                spinBoxController.updateButtons(this, value);
            }
            textField.getHtml().setAttr(EAttrName.ValueIsNull.getAttrName(), value == null ? "true" : null);
            if (value == null) {
                setValueIcon(null);
            }
            updataButtonsState();
            return true;
        } else {
            if (!hasAcceptableInput()) {
                leaveUnacceptableInputMode();
                valueChangelistener.onValueChanged(value, value);
            }
            return false;
        }
    }

    private boolean setValueInternal(final T value) {
        final boolean result = updateValue(value);
        //Новое значение может совпадать с текущим по equals, но отличаться в строковом представлении.        
        //Поэтому строковое представление нужно обновить независимо от результата updateValue.
        updateDisplayString();
        updateValuesMarker();
        return result;
    }

    public void setValue(final T value) {
        setValueInternal(value);
    }

    @Override
    public void processAction(final String actionName, final String actionParam) {
        if (!isReadOnly()) {
            if ("clear".equals(actionName)) {
                setValue(null);
            } else if ("close-drop-down".equals(actionName)) {
                closeActiveDropDown();
            } else if (spinBoxController != null && spinButtonCell.isVisible()) {
                if ("increment".equals(actionName)) {
                    final int delta;
                    if (actionParam == null || actionParam.isEmpty()) {
                        delta = 0;
                    } else {
                        try {
                            delta = Integer.parseInt(actionParam);
                        } catch (NumberFormatException exception) {
                            return;
                        }
                    }
                    setValue(delta > 0 ? spinBoxController.getNext(value, delta) : spinBoxController.getPrev(value, -delta));
                } else {
                    final T curValue, newValue;
                    if (actionParam == null || actionParam.isEmpty()) {
                        curValue = null;
                    } else {
                        try {
                            curValue = valueController.getValue(actionParam);
                        } catch (InvalidStringValueException ex) {
                            return;
                        }
                    }
                    switch (actionName) {
                        case "get-next":
                            newValue = spinBoxController.getNext(curValue, 1);
                            break;
                        case "get-next10":
                            newValue = spinBoxController.getNext(curValue, 10);
                            break;
                        case "get-prev":
                            newValue = spinBoxController.getPrev(curValue, 1);
                            break;
                        case "get-prev10":
                            newValue = spinBoxController.getPrev(curValue, 10);
                            break;
                        default:
                            return;
                    }
                    textField.editText(displayController.getDisplayValue(newValue, true, isReadOnly()));
                    if (spinBoxController != null) {
                        spinBoxController.updateButtons(this, newValue);
                    }
                }
            } else if ("hotkey".equals(actionName) && dropDownDelegates != null) {
                for (DropDownDelegate delegate : dropDownDelegates.keySet()) {
                    if (delegate.getInputHandlers(this).contains(actionParam)) {
                        delegate.handleHotKey(actionParam, this);
                    }
                }
            } else if ("mousewheel".equals(actionName) && dropDownDelegates != null) {
                final int wheel =
                        actionParam == null || actionParam.isEmpty() ? 0 : Integer.parseInt(actionParam);
                if (wheel != 0) {
                    for (DropDownDelegate delegate : dropDownDelegates.keySet()) {
                        if (delegate.getInputHandlers(this).contains("wheel")) {
                            delegate.handleMouseWheel(wheel, this);
                        }
                    }
                }
            } else if ("wasNullValue".equals(actionName)) {
                EnumSet<ETextOptionsMarker> markers = textOptionsMarkers.clone();
                if ("true".equals(actionParam)) {
                    if (markers.contains(ETextOptionsMarker.UNDEFINED_VALUE)) {
                        markers.remove(ETextOptionsMarker.UNDEFINED_VALUE);
                    }
                    WpsTextOptions options = calculateTextOptions(markers);
                    textField.setTextOptions(options);
                } else if ("false".equals(actionParam)) {
                    WpsTextOptions options = calculateTextOptions(markers);
                    textField.setTextOptions(options);
                }
            }else{
                super.processAction(actionName, actionParam);
            }
        } else {
            super.processAction(actionName, actionParam);
        }
    }
    
    private static boolean isButtonCell(TableLayout.Row.Cell cell){
        return cell.getHtml().getBooleanAttr(EAttrName.ButtonCell.getAttrName());
    }

    private void updateButtonsStyle() {
        final boolean isButtonsVisible = isButtonsVisible();
        TableLayout.Row.Cell cell, lastCell = null;        
        int stopIndex = isLabelVisible() ? 1 : 0;

        for (int i = row.getCells().size() - 1; i >= stopIndex; i--) {
            cell = row.getCells().get(i);
            if (lastCell == null 
                && cell.isVisible() 
                && (!isButtonCell(cell) || isButtonsVisible)) {
                lastCell = cell;
            } else {
                cell.getHtml().removeClass("ui-corner-right");
                cell.getHtml().removeClass("last");
            }
        }

        if (lastCell != null) {
            lastCell.getHtml().addClass("ui-corner-right");
            lastCell.getHtml().addClass("last");
        }

        if (cells2Buttons != null) {
            for (Cell2Button c2b : cells2Buttons) {
                if (c2b.cell == lastCell) {
                    c2b.button.getHtml().addClass("ui-corner-right");
                } else {
                    c2b.button.getHtml().removeClass("ui-corner-right");
                }
            }
        }
    }

    public final boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        if (this.isReadOnly != readOnly) {
            isReadOnly = readOnly;
            updateTextFieldReadOnlyMarker();
            if (readOnly) {
                html.setAttr(EAttrName.IsValid.getAttrName(), true);
                html.setAttr(EAttrName.ValidationMessage.getAttrName(), "");
            } else {
                html.setAttr(EAttrName.ValidationMessage.getAttrName(), validationMessage);
                html.setAttr(EAttrName.IsValid.getAttrName(), validationMessage == null || validationMessage.isEmpty());
            }
            if (spinBoxController != null) {
                spinBoxController.updateButtons(this, value);
            }
            updataButtonsState();
            updateReadOnlyMarker();
        }
    }

    public boolean isPrintable() {
        final String displayValue = displayController.getDisplayValue(value, false, isReadOnly());
        return displayValue == null || displayValue.matches("[^\\p{Cc}\\p{Cn}]*");
    }

    public void updataButtonsState() {
        final boolean isValid = validationMessage == null || validationMessage.isEmpty();
        if (clearButton != null) {
            clearButton.setVisible(!isReadOnly && (value != null || !isValid));
        }
        if (dropDownDelegates != null) {
            for (Map.Entry<DropDownDelegate<? extends UIObject>, ToolButton> entry : dropDownDelegates.entrySet()) {
                entry.getKey().updateButton(entry.getValue(), this);
            }
        }
        updateButtonsStyle();
    }
    
    public void addCustomButton(final ButtonBase button) {
        addCustomButton(button, IValEditor.DEFAULT_BUTTON_PRIORITY);
    }

    public void addCustomButton(final ButtonBase button, final int priority) {        
        final int index = registerButton(button, priority);
        if (index>=0){
            final TableLayout.Row.Cell cellForButton = row.addCell(index);
            cellForButton.getHtml().setAttr(EAttrName.ButtonCell.getAttrName(), true);        
            cellForButton.add(button);
            setupButton(button);
            updataButtonsState();
            updateReadOnlyMarker();
        }
    }
    
    public void removeCustomButton(final ButtonBase button){
        if (unregisterButton(button)){
            for (int i=row.getCellsCount()-1; i>=0; i--){
                if (row.getCell(i).getChildren().contains(button)){
                    row.remove(row.getCell(i));
                    updataButtonsState();
                    updateReadOnlyMarker();
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
        int buttonCellIndex = 1;
        if (imageCell!=null){
            buttonCellIndex++;
        }
        if (label!=null){
            buttonCellIndex++;
        }
        if (invalidValueIconCell!=null){
            buttonCellIndex++;
        }
        return buttonCellIndex;
    }

    private static class InvalidValueIcon extends UIObject {

        private final Image image = new Image();
        private final UIObject tooltip = new UIObject(new Div());

        public InvalidValueIcon(final RootPanel root) {
            super(new Div());
            image.setWidth(ICON_SIZE);
            image.setHeight(ICON_SIZE);
            ClientIcon clientIcon = ClientIcon.TraceLevel.WARNING;

            if (root.getApplication() != null) {
                ImageManager manager = root.getApplication().getImageManager();
                final WpsIcon icon = (WpsIcon) manager.getIcon(clientIcon);
                image.setIcon(icon);
            }
            html.add(image.getHtml());
            image.setParent(InvalidValueIcon.this);
            image.getHtml().setCss("vertical-align", "middle");
            image.getHtml().setAttr("tooltip_id", tooltip.getHtmlId());
            image.getHtml().setAttr("onmouseover", "$RWT.tooltip.onMouseOver");
            image.getHtml().setAttr("onmouseout", "$RWT.tooltip.onMouseOut");
            image.getHtml().addClass("rwt-invalid-value-icon");
            root.add(tooltip);
            tooltip.getHtml().addClass("rwt-invalid-value-reason");
            tooltip.getHtml().setCss("position", "absolute");
            tooltip.setVisible(false);
        }

        public void setTooltipText(final String text) {
            tooltip.getHtml().setInnerText(text);
        }

        public void close() {
            ((AbstractContainer) tooltip.getParent()).remove(tooltip);
        }
    }

    public void setTextFieldClientHandler(String customEventName, String code) {
        textField.setClientHandler(customEventName, code);
    }

    @Override
    public void setFocused(boolean focused) {
        textField.setFocused(focused);
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        if (this.isEnabled != isEnabled) {

            this.isEnabled = isEnabled;
            textField.setEnabled(isEnabled);
            updateTextFieldReadOnlyMarker();
            if (cells2Buttons != null && !cells2Buttons.isEmpty()) {
                for (Cell2Button cb : cells2Buttons) {
                    cb.button.setEnabled(isEnabled);
                    if (cb.button.getFileUploader() != null) {
                        cb.button.getFileUploader().setEnabled(isEnabled);
                    }
                }
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    protected ICssStyledItem getFontOptionsHolder() {
        return textField.getFontOptionsHolder();
    }

    @Override
    protected ICssStyledItem getBackgroundHolder() {
        return textField.getBackgroundHolder();
    }

    @Override
    protected ICssStyledItem getForegroundHolder() {
        return textField.getForegroundHolder();
    }

    private void updateValuesMarker() {
        final EnumSet<ETextOptionsMarker> markers = textOptionsMarkers.clone();
        if (getValue() == null) {
            markers.add(ETextOptionsMarker.UNDEFINED_VALUE);
        } else {
            markers.remove(ETextOptionsMarker.UNDEFINED_VALUE);
        }
        if (getValue() == null && !isClearable()) {
            markers.add(ETextOptionsMarker.MANDATORY_VALUE);
        } else {
            markers.remove(ETextOptionsMarker.MANDATORY_VALUE);
        }
        Color editFgrn = calculateEditOptions(markers).getForegroundColor();
        this.textField.getHtml().setAttr("editcolor", color2Str(editFgrn));
        if (!textOptionsMarkers.equals(markers)) {
            applyTextOptionsMarkers(markers);
        }
    }

    private WpsTextOptions calculateEditOptions(EnumSet<ETextOptionsMarker> markers) {
        EnumSet<ETextOptionsMarker> textMarkers = markers.clone();
        if (textMarkers.contains(ETextOptionsMarker.UNDEFINED_VALUE)) {
            textMarkers.remove(ETextOptionsMarker.UNDEFINED_VALUE);
        }
        return calculateTextOptions(textMarkers);
    }

    protected boolean isButtonCanChangeValue(final ButtonBase button) {
        return button.isVisible() && button.isEnabled();
    }

    protected void updateReadOnlyMarker() {
        boolean isSomeButtonAccessible = false;
        if (isReadOnly()) {
            if (dropDownDelegates != null) {
                for (DropDownDelegate delegate : dropDownDelegates.keySet()) {
                    if (delegate.canChangeValue(this)) {
                        isSomeButtonAccessible = true;
                        break;
                    }
                }
            }
            if (!isSomeButtonAccessible && cells2Buttons != null) {
                for (Cell2Button cell2Button : cells2Buttons) {
                    if (isButtonCanChangeValue(cell2Button.button)) {
                        isSomeButtonAccessible = true;
                        break;
                    }
                }
            }
        }
        if (isReadOnly() && !isSomeButtonAccessible) {
            addTextOptionsMarkers(ETextOptionsMarker.READONLY_VALUE);
        } else {
            removeTextOptionsMarkers(ETextOptionsMarker.READONLY_VALUE);
        }
    }

    private void updateTextFieldReadOnlyMarker(){
        textField.setReadOnly(isReadOnly || valueController==null || !isEnabled);
    }

    public final boolean addTextOptionsMarkers(final ETextOptionsMarker... markers) {
        final EnumSet<ETextOptionsMarker> newMarkers = textOptionsMarkers.clone();
        newMarkers.addAll(Arrays.asList(markers));
        return applyTextOptionsMarkers(newMarkers);
    }

    public final boolean removeTextOptionsMarkers(final ETextOptionsMarker... markers) {
        final EnumSet<ETextOptionsMarker> newMarkers = textOptionsMarkers.clone();
        newMarkers.removeAll(Arrays.asList(markers));
        return applyTextOptionsMarkers(newMarkers);
    }

    public final boolean setTextOptionsMarkers(final ETextOptionsMarker... markers) {
        final EnumSet<ETextOptionsMarker> newMarkers = EnumSet.noneOf(ETextOptionsMarker.class);
        newMarkers.addAll(Arrays.asList(markers));
        return applyTextOptionsMarkers(newMarkers);
    }

    public final EnumSet<ETextOptionsMarker> getTextOptionsMarkers() {
        return textOptionsMarkers.clone();
    }

    private void initTextOptionsProvider() {
        final ITextOptionsManager manager = getEnvironment().getApplication().getTextOptionsManager();
        final ITextOptionsProvider defaultProvider = getEnvironment().getTextOptionsProvider();
        if (customTextOptionsProvider == null) {
            if (customTextOptions == null) {
                provider = defaultProvider;
            } else {
                provider = new MergedTextOptionsProvider(defaultProvider, customTextOptions, manager);
            }
        } else {
            final ITextOptionsProvider mergedProvider =
                    new MergedTextOptionsProvider(defaultProvider, customTextOptionsProvider, manager);
            if (customTextOptions == null) {
                provider = mergedProvider;
            } else {
                provider = new MergedTextOptionsProvider(mergedProvider, customTextOptions, manager);
            }
        }
    }

    public final void setTextOptionsProvider(final ITextOptionsProvider textOptionsProvider) {
        if (customTextOptionsProvider != textOptionsProvider) {//NOPMD
            customTextOptionsProvider = textOptionsProvider;
            initTextOptionsProvider();
            refreshTextOptions();
        }
    }

    private CustomTextOptions getCustomTextOptions() {
        if (customTextOptions == null) {
            customTextOptions = new CustomTextOptions();
            initTextOptionsProvider();
        }
        return customTextOptions;
    }

    public final void setDefaultTextOptions(final WpsTextOptions options) {
        if (options != null || customTextOptions != null) {
            final CustomTextOptions customOptions = getCustomTextOptions();
            if (!Objects.equals(customOptions.getDefaultOptions(), options)) {
                customOptions.setDefaultOptions(options);
                refreshTextOptions();
            }
        }
    }

    public final void setTextOptionsForMarker(final ETextOptionsMarker marker, final WpsTextOptions options) {
        if (marker != null && (options != null || customTextOptions != null)) {
            final CustomTextOptions customOptions = getCustomTextOptions();
            final EnumSet<ETextOptionsMarker> markers = EnumSet.of(marker);
            if (!Objects.equals(options, customOptions.getOptions(markers, null))) {
                customOptions.putOptions(options, markers);
                refreshTextOptions();
            }
        }
    }

    public final WpsTextOptions getTextOptions() {
        return textOptions;
    }

    private boolean applyTextOptionsMarkers(final EnumSet<ETextOptionsMarker> newMarkers) {
        if (!textOptionsMarkers.equals(newMarkers)) {
            textOptionsMarkers = newMarkers.clone();
            refreshTextOptions();
            return true;
        }
        return false;
    }

    public final void refreshTextOptions() {
        setTextOptions(calculateTextOptions(textOptionsMarkers));
        if (label != null) {
            label.setTextOptions(calculateLabelTextOptions(textOptionsMarkers));
        }
    }

    protected WpsTextOptions calculateLabelTextOptions(final EnumSet<ETextOptionsMarker> markers) {
        final EnumSet<ETextOptionsMarker> labelMarkers = markers.clone();
        labelMarkers.remove(ETextOptionsMarker.EDITOR);
        labelMarkers.add(ETextOptionsMarker.LABEL);
        WpsTextOptions options = (WpsTextOptions) provider.getOptions(labelMarkers, null);
        if (options.getBackgroundColor() != null) {
            options = options.changeBackgroundColor(null);
        }
        return labelColor == null ? options : options.changeForegroundColor(labelColor);
    }

    protected WpsTextOptions calculateTextOptions(final EnumSet<ETextOptionsMarker> markers) {
        return (WpsTextOptions) provider.getOptions(markers, null);
    }

    @Override
    public void setTextOptions(final WpsTextOptions options) {
        if (options == null) {
            clearTextOptions();
            final WpsTextOptions defaultOptions = WpsTextOptions.getDefault((WpsEnvironment) getEnvironment());
            applyTextOptions(defaultOptions);
        } else if (textOptions.equals(options) && textOptions.getPredefinedCssStyles().equals(options.getPredefinedCssStyles())) {
            if (!isReadOnly()){
                final boolean isValid = validationMessage == null || validationMessage.isEmpty();            
                if (isValid){
                    textField.getForegroundHolder().removeClass(ECssTextOptionsStyle.INVALID_VALUE.getStyleName());
                }else{
                    //resend class because it was removed in javascript on focus out event
                    textField.getForegroundHolder().removeClass(ECssTextOptionsStyle.INVALID_VALUE.getStyleName());
                    textField.getForegroundHolder().addClass(ECssTextOptionsStyle.INVALID_VALUE.getStyleName());                    
                }
            }                
        }else{
            clearTextOptions();
            applyTextOptions(options);            
        }
    }
    
    public final boolean isButtonsVisible(){
        return !table.getHtml().containsClass("rwt-hidden-buttons");
    }
    
    public final void setButtonsVisible(final boolean isVisible){
        if (isVisible==table.getHtml().containsClass("rwt-hidden-buttons")){
            if (isVisible){
                table.getHtml().removeClass("rwt-hidden-buttons");
            }else{
                table.getHtml().addClass("rwt-hidden-buttons");
            }
            updateButtonsStyle();
        }
    }    
    
    private void applyTextOptions(final WpsTextOptions options){
        super.setTextOptions(options);
        final boolean isValid = validationMessage == null || validationMessage.isEmpty();
        if (options.getPredefinedCssStyles().isEmpty()){
            editorCell.setBackground(options.getBackgroundColor());
            if (isValid || isReadOnly()){
                textField.setForeground(options.getForegroundColor());
            }else{
                textField.setForeground(null);
                textField.getForegroundHolder().addClass(ECssTextOptionsStyle.INVALID_VALUE.getStyleName());
            }
        }else{
            if (options.getBackgroundColor()!=null){
                editorCell.setBackground(null);
            }
            if (options.getForegroundColor()!=null){
                textField.setForeground(null);
            }
            final EnumSet<ECssTextOptionsStyle> styles = options.getPredefinedCssStyles();
            for (ECssTextOptionsStyle style :styles){
                editorCell.getBackgroundHolder().addClass(style.getStyleName());
                textField.getForegroundHolder().addClass(style.getStyleName());
            }            
        }
        if (imageCell!=null){
            setIconCellBackground(imageCell, options);
        }
        if (invalidValueIconCell!=null){
            setIconCellBackground(invalidValueIconCell, options);
        }
        textOptions = options;
    }
    
    private static void setIconCellBackground(final Cell iconCell, final WpsTextOptions options){
        if (options.getPredefinedCssStyles().isEmpty()){
            iconCell.setBackground(options.getBackgroundColor());
        }else{
            iconCell.setBackground(null);
            final EnumSet<ECssTextOptionsStyle> styles = options.getPredefinedCssStyles();
            for (ECssTextOptionsStyle style :styles){
                iconCell.getBackgroundHolder().addClass(style.getStyleName());
            }
        }
    }
    
    private void clearTextOptions(){
        if (textOptions!=null && !textOptions.getPredefinedCssStyles().isEmpty()){
            final EnumSet<ECssTextOptionsStyle> styles = textOptions.getPredefinedCssStyles();
            for (ECssTextOptionsStyle style :styles){
                if (imageCell!=null){
                    imageCell.getBackgroundHolder().removeClass(style.getStyleName());                    
                }
                if (invalidValueIconCell!=null){
                    invalidValueIconCell.getBackgroundHolder().removeClass(style.getStyleName());
                }
                editorCell.getBackgroundHolder().removeClass(style.getStyleName());
                textField.getForegroundHolder().removeClass(style.getStyleName());
            }
        }
        textField.getForegroundHolder().removeClass(ECssTextOptionsStyle.INVALID_VALUE.getStyleName());
    }
}
