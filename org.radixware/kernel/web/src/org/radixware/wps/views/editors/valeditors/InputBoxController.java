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

package org.radixware.wps.views.editors.valeditors;

import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IListDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.types.EditingHistoryException;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.html.ToolTip;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.ButtonBase;
import org.radixware.wps.rwt.DropDownEditHistoryDelegate;
import org.radixware.wps.rwt.DropDownListDelegate;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.InputFormat;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.ValueEditor.ValueChangeListener;
import org.radixware.wps.rwt.events.HtmlEvent;
import org.radixware.wps.settings.ISettingsChangeListener;


public abstract class InputBoxController<T, V extends EditMask> extends ValEditorController<T, V> {
    
    protected final static String MAX_ITEMS_IN_DD_LIST_SETTING_KEY = 
        SettingNames.SYSTEM+"/"+SettingNames.EDITOR_GROUP+"/"+SettingNames.Editor.COMMON_GROUP+"/"+SettingNames.Editor.Common.DROP_DOWN_LIST_ITEMS_LIMIT;    

    private static class InternalInputBox<V, M extends EditMask> extends InputBox<V> implements IValEditor<V, M> {

        private final IClientEnvironment env;
        private final InputBoxController<V, M> editorController;
        private V initialValue;

        private InternalInputBox(IClientEnvironment environment, DisplayController<V> controller, ValueController<V> valueController, InputBoxController<V, M> editorController) {
            super(controller, valueController);
            env = environment;
            this.editorController = editorController;
        }

        @Override
        public void addButton(final IButton button) {
            if (button instanceof ButtonBase) {
                addCustomButton((ButtonBase) button);
            }
        }
        
        @Override
        public void addButton(final IButton button, final int priority) {
            if (button instanceof ButtonBase) {
                addCustomButton((ButtonBase) button, priority);
            }
        }        

        @Override
        public void removeButton(final IButton button) {
            if (button instanceof ButtonBase) {
                removeCustomButton((ButtonBase)button);
            }
        }
                

        @Override
        public void setValidationResult(ValidationResult validationResult) {
            if (ValidationResult.ACCEPTABLE == validationResult) {
                setValidationMessage(null);
            } else {
                final String comment = validationResult.getInvalidValueReason().getMessage(env.getMessageProvider(), InvalidValueReason.EMessageType.Value);
                if (comment == null || comment.isEmpty()) {
                    setValidationMessage(env.getMessageProvider().translate("Value", "Current value is invalid"));
                } else {
                    setValidationMessage(comment);
                }
            }
        }

        @Override
        public ValidationResult getValidationResult() {
            final String message = getValidationMessage();
            if (message == null || message.isEmpty()) {
                return ValidationResult.ACCEPTABLE;
            } else {
                return ValidationResult.Factory.newInvalidResult(message);
            }
        }

        @Override
        public void refresh() {
            updateDisplayString();
        }

        @Override
        public ValEditorController<V, M> getController() {
            return editorController;
        }

        @Override
        protected Label createLabel() {

            Label label = ((InputBoxController) editorController).createLabel();
            if (label != null) {
                return label;
            }

            return super.createLabel();
        }

        @Override
        public void setReadOnly(final boolean readOnly) {
            super.setReadOnly(readOnly);
            editorController.afterChangeReadOnly();
        }

        @Override
        public void setInitialValue(final V value) {
            initialValue = value;
            setInitialText(editorController.calcInitialText(value, editorController.getEditMask()));
        }

        @Override
        public V getInitialValue() {
            return initialValue;
        }

        @Override
        protected IClientApplication getApplication() {
            return env.getApplication();
        }

        @Override
        protected void processHtmlEvent(final HtmlEvent event) {
            if (!editorController.processHtmlEvent(event)){
                super.processHtmlEvent(event);
            }
        }
    }

    private class EditMaskDisplayController implements InputBox.DisplayController<T> {

        @Override
        public String getDisplayValue(T value, boolean isFocused, boolean isReadOnly) {
            if (editMask != null) {
                return isFocused ? calcFocusedText(value, editMask) : editMask.toStr(getEnvironment(), value);
            } else {
                return value == null ? getEnvironment().getMessageProvider().translate("Value", "<not defined>") : value.toString();
            }
        }
    }
    
    public final static class TypifiedListWidgetItem<T> extends ListWidgetItem{
                        
        public TypifiedListWidgetItem(final String text, final T value){
            super(text, value);
        }
        
        public TypifiedListWidgetItem(final String text, final T value, final Icon icon){
            super(text, value, icon);
        }        

        @Override
        @SuppressWarnings("unchecked")
        public T getValue() {
            return (T)super.getValue();
        }                
    }    
    
    private final static class DropDownPredefinedValuesDelegate<T> extends DropDownListDelegate<T> { //used in InputBox
        
        private List<DropDownListItem<T>> listBoxItems;
        private final IClientEnvironment environment;
        
        public DropDownPredefinedValuesDelegate(final IClientEnvironment environment) {
            this.environment = environment;
            setDisplayCurrentItemInDropDownList(true);
        }
                
        @Override
        protected List<DropDownListItem<T>> getItems() {
            return listBoxItems;
        }

        public void setPredefinedValues(List<DropDownListItem<T>> listBoxItems){
            this.listBoxItems = listBoxItems;
        }
        
        @Override
        protected ToolButton createDropDownButton() {
            final ToolButton button = new ToolButton();
            button.setIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.FAVORITES_POPUP));
            button.setIconHeight(16);
            button.setIconWidth(16);
            button.setObjectName("tbPredefValues");
            return button;
        }  
    }
    
    private class SettingsChangeListener implements ISettingsChangeListener{
        @Override
        public void onSettingsChanged() {
            InputBoxController.this.onSettingsChanged();
        }        
    }
    
    protected abstract InputBox.ValueController<T> createValueController();
    protected IEditingHistory editingHistory = null;
    private DropDownEditHistoryDelegate<T> dropDownHistoryDelegate;
    private DropDownPredefinedValuesDelegate<T> dropDownPredefinedValuesDelegate;
    private EValType valType;
    protected List<T> predefinedValues = null;
    private final LabelFactory labelFactory;
    private ISettingsChangeListener settingsListener;
    private boolean isEnabled = true;
    private int maxPredefinedValuesInPopup = -1;
    private ButtonBase showPredefinedValuesButton;
    private String dialogTitle;   
    
    public InputBoxController(final IClientEnvironment env, final LabelFactory labelFactory){
        super(env);
        this.labelFactory = labelFactory;
        getValEditor();
    }

    public InputBoxController(final IClientEnvironment env) {
        this(env,null);
    }

    protected void setupValEditor(InputBox<T> inputBox) {
        if (!isMandatory()) {
            inputBox.setClearController(new InputBox.ClearController<T>() {
                @Override
                public T clear() {
                    return null;
                }
            });
        } else {
            inputBox.setClearController(null);
        }        
    }

    protected final InputBox<T> getInputBox() {
        if (getValEditor() instanceof InputBox){
            return (InputBox<T>) getValEditor();
        }else{
            return null;
        }        
    }
    
    protected List<DropDownListDelegate.DropDownListItem<T>> createPredefinedValueItems(InputBox<T> box, List<T> predefinedValuesList){
        List<DropDownListDelegate.DropDownListItem<T>> listBoxItems = new LinkedList<>(); 
        
        for (T listItem : predefinedValuesList){
            final InputBox.DisplayController<T> displayController = box.getDisplayController();
            String title = displayController.getDisplayValue(listItem, false, box.isReadOnly());
            if (title == null) {
                title = String.valueOf(listItem);
            }
            listBoxItems.add(new DropDownListDelegate.DropDownListItem<>(title, listItem));
        }
        return listBoxItems;
    }

    @Override
    public void setHtmlToolTip(ToolTip toolTip) {
        getValEditor().setHtmlToolTip(toolTip);
    }
       
    public void setLabelVisible(boolean visible) {
        final InputBox box = getInputBox();
        if (box!=null){
            box.setLabelVisible(visible);
        }
    }

    public boolean isLabelVisible() {
        final InputBox box = getInputBox();
        return box==null ? false : box.isLabelVisible();
    }

    public boolean getLabelVisible() {
        return isLabelVisible();
    }

    public String getLabel() {
        final InputBox box = getInputBox();
        return box==null ? null : box.getLabel();        
    }

    public void setLabel(String label) {
        final InputBox box = getInputBox();
        if (box!=null){
            box.setLabel(label);
        }
    }

    protected Label createLabel() {
        return labelFactory==null ? null : labelFactory.createLabel();
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        if (getValEditor() instanceof UIObject){
            ((UIObject)getValEditor()).setEnabled(isEnabled);
        }else{
            final InputBox box = getInputBox();
            if (box!=null){
                box.setEnabled(isEnabled);
            }
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    protected InputFormat createInputFormat() {
        return InputFormat.Factory.inputFormatFromEditMask(getEditMask(), getEnvironment());
    }

    protected void applyEditMask(InputBox box) {
        if (box!=null){
            box.setInputFormat(createInputFormat());
            if (box.hasAcceptableInput()){
                box.setFocusedText(calcFocusedText(getValue(), editMask));
            }
            box.setInitialText(calcInitialText(getInitialValue(), editMask));
            box.refreshTextOptions();
        }
    }

    protected InputBox.DisplayController<T> createDisplayController() {
        return new EditMaskDisplayController();
    }

    @Override
    protected IValEditor<T, V> createValEditor() {
        InternalInputBox<T, V> box = new InternalInputBox<>(getEnvironment(), createDisplayController(), createValueController(), this);
        box.addValueChangeListener(new ValueChangeListener<T>() {
            @Override
            public void onValueChanged(T oldValue, T newValue) {
                InputBoxController.this.valueEdited(newValue);
            }
        });
        setupValEditor(box);
        if (editMask != null) {
            applyEditMask(box);
        }
        return box;
    }        

    protected String calcFocusedText(final T value, final V editMask) {
        if (editMask == null) {
            return value == null ? "" : null;
        } else {
            return editMask.isSpecialValue(value) ? "" : null;
        }
    }

    protected String calcInitialText(final T initialValue, final V editMask) {
        if (editMask == null) {
            return null;
        }
        final String focusedText = calcFocusedText(initialValue, editMask);
        if (focusedText == null) {
            if (editMask.isSpecialValue(initialValue)) {
                return "";
            } else {
                return editMask.toStr(getEnvironment(), initialValue);
            }
        } else {
            return focusedText;
        }
    }

    @Override
    protected ValidationResult calcValidationResult(T value) {
        ValidationResult result = super.calcValidationResult(value);        
        if (result == ValidationResult.ACCEPTABLE && !isReadOnly()) {
            final InputBox box = getInputBox();
            if (box!=null && !box.isPrintable()){
                final InvalidValueReason reason = InvalidValueReason.Factory.createForValueWithNonprintableChars(getEnvironment());
                result = ValidationResult.Factory.newInvalidResult(reason);
            }
        }
        return result;
    }        

    @Override
    public void setEditMask(V editMask) {
        super.setEditMask(editMask);
        applyEditMask(getInputBox());
    }

    @Override
    public void close() {
        try {
            if (editingHistory != null) {
                editingHistory.flush();
            }
        } catch (EditingHistoryException ex) {
            getEnvironment().getTracer().error(ex);
        } finally {
            super.close();
        }

    }

    @Override
    protected void valueEdited(T value) {
        super.valueEdited(value);
        final InputBox<T> box = getInputBox();
        if (box!=null){
            box.setFocusedText(calcFocusedText(value, getEditMask()));
        }
        updateHistory();
    }

    public void setEditHistory(final IEditingHistory history, EValType type) {
        this.valType = type;
        if (history == null) {
            try {
                if (editingHistory != null) {
                    editingHistory.flush();
                }
            } catch (EditingHistoryException ex) {
                getEnvironment().getTracer().error(ex);
            }
            this.editingHistory = null;
        } else {
            this.editingHistory = history;
        }
        final InputBox<T> box = getInputBox();
        if (box!=null) {
            if (dropDownHistoryDelegate == null) {
                dropDownHistoryDelegate = new DropDownEditHistoryDelegate<>(getInputBox(), history, type);
                box.addDropDownDelegate(dropDownHistoryDelegate);
            } else {
                dropDownHistoryDelegate.updateItems(box, history, type);
            }
            updateHistory();
        }
    }

    public IEditingHistory getEditHistory() {
        return editingHistory;
    }

    public void updateHistory() {
        if (getValue() != null && dropDownHistoryDelegate != null && getEditHistory() != null && valType != null) {
            if (!dropDownHistoryDelegate.containsValue(getValue())) {
                String text = calcFocusedText(getValue(), editMask);

                if (text == null && editMask != null) {
                    text = editMask.toStr(getEnvironment(), getValue());
                }
                editingHistory.addEntry(text);                
                dropDownHistoryDelegate.updateItems(getInputBox(), editingHistory, valType);
            }
            try {
                editingHistory.flush();
            } catch (EditingHistoryException ex) {
                getEnvironment().getTracer().error(ex);
            }
        }
    }

    @Override
    public void setMandatory(final boolean mandatory) {
        if (isMandatory()!=mandatory){
            super.setMandatory(mandatory);
            final InputBox<T> box = getInputBox();
            if (box!=null){
                if (!isMandatory()) {
                    box.setClearController(new InputBox.ClearController<T>() {
                        @Override
                        public T clear() {
                            return null;
                        }
                    });
                } else {
                    box.setClearController(null);
                }
            }
        }
    }

    @Override
    public final void setReadOnly(final boolean readOnly) {//use afterChangeReadOnly
        super.setReadOnly(readOnly);
    }

    public void setPredefinedValues(final List<T> predefValues) {
        if (predefValues == null) {
            if (predefinedValues != null) {
                predefinedValues.clear();
                refreshShowPredefinedValuesButton();
            }
        } else {
            if (predefinedValues == null) {
                predefinedValues = new LinkedList<>();
            } else {
                predefinedValues.clear();
            }

            for (T t : predefValues) {
                if (!editMask.isSpecialValue(t) && calcValidationResult(t) == ValidationResult.ACCEPTABLE) {
                    predefinedValues.add(t);
                }
            }
            refreshShowPredefinedValuesButton();
        }
        if (predefinedValues!=null && !predefinedValues.isEmpty()){
            subscribeToChangeSettingsEvent();
        }else {
            unsubscribeFromChangeSettingsEvent();
        }
    }
    
    public final void refreshShowPredefinedValuesButton(){        
        final InputBox<T> box = getInputBox();
        if (box!=null){
            final boolean isButtonVisible = predefinedValues != null && !predefinedValues.isEmpty() && !isReadOnly();        
            if (isButtonVisible){
                if (predefinedValues.size()>getMaxPredefinedValuesInDropDownList()){
                    if (dropDownPredefinedValuesDelegate!=null){
                        box.removeDropDownDelegate(dropDownPredefinedValuesDelegate);
                        dropDownPredefinedValuesDelegate = null;
                    }
                    if (showPredefinedValuesButton==null){
                        createPredefinedValuesButton();
                    }
                }else{
                    if (showPredefinedValuesButton!=null){
                        box.removeCustomButton(showPredefinedValuesButton);
                        showPredefinedValuesButton = null;
                    }
                    if (dropDownPredefinedValuesDelegate==null){
                        dropDownPredefinedValuesDelegate = new DropDownPredefinedValuesDelegate<>(getEnvironment());
                        box.addDropDownDelegate(dropDownPredefinedValuesDelegate);                        
                    }
                    dropDownPredefinedValuesDelegate.setPredefinedValues(createPredefinedValueItems(box, predefinedValues));
                }
            }else{
                if (showPredefinedValuesButton!=null){
                    box.removeCustomButton(showPredefinedValuesButton);
                    showPredefinedValuesButton = null;
                }            
                if (dropDownPredefinedValuesDelegate!=null){
                    box.removeDropDownDelegate(dropDownPredefinedValuesDelegate);
                    dropDownPredefinedValuesDelegate = null;
                }
            }
        }
    }
    
    private ButtonBase createPredefinedValuesButton(){
        showPredefinedValuesButton = new ToolButton();
        showPredefinedValuesButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.FAVORITES));    
        showPredefinedValuesButton.setToolTip(getEnvironment().getMessageProvider().translate("Value", "Show predefined values"));
        showPredefinedValuesButton.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(final IButton source) {
                showPredefinedValuesDlg();
            }
        });
        showPredefinedValuesButton.setObjectName("tbPredefValues");
        return showPredefinedValuesButton;
    }
    
    
    public List<T> getPredefinedValues () {
        return predefinedValues == null ? Collections.<T>emptyList() : Collections.unmodifiableList(this.predefinedValues);
    }    
    
    public final int getMaxPredefinedValuesInDropDownList(){
        if (maxPredefinedValuesInPopup<0){
            return getEnvironment().getConfigStore().readInteger(MAX_ITEMS_IN_DD_LIST_SETTING_KEY);
        }else{
            return maxPredefinedValuesInPopup;
        }
    }    
    
    public final void setMaxPredefinedValuesInDropDownList(final int max){
        if (maxPredefinedValuesInPopup!=max){
            maxPredefinedValuesInPopup = max;
            refreshShowPredefinedValuesButton();
        }
    }    
    
    private void showPredefinedValuesDlg(){
        final InputBox<T> box = getInputBox();
        if (box!=null){
            final List<DropDownListDelegate.DropDownListItem<T>> dropDownItems = 
                createPredefinedValueItems(box, predefinedValues);
            final List<TypifiedListWidgetItem<T>> listItems = new LinkedList<>();
            TypifiedListWidgetItem<T> listItem;
            for (DropDownListDelegate.DropDownListItem<T> dropDownItem: dropDownItems){
                listItem = new TypifiedListWidgetItem<>(dropDownItem.getText(), dropDownItem.getValue(), dropDownItem.getIcon());
                listItems.add(listItem);
            }
            if (!listItems.isEmpty()){
                final int selectedItem = selectItem(listItems, -1);
                if (selectedItem>-1){
                    onSelectListWidgetItem(listItems.get(selectedItem));
                }
            }
        }
    }
    
    protected final int selectItem(final List<TypifiedListWidgetItem<T>> items, final int currentIndex){
        if (items==null || items.isEmpty()){
            return -1;
        }
        final IListDialog dialog = 
            getEnvironment().getApplication().getDialogFactory().newListDialog(getEnvironment(), getInputBox(), getSelectValueDialogConfigPrefix());        
        final List<ListWidgetItem> rawItems = new LinkedList<>();
        rawItems.addAll(items);
        dialog.setItems(rawItems);
        dialog.setFeatures(EnumSet.of(IListWidget.EFeatures.FILTERING, IListWidget.EFeatures.MANUAL_SORTING));
        if (dialogTitle!=null){
            dialog.setWindowTitle(dialogTitle);
        }
        if (currentIndex>=0){
            dialog.setCurrentRow(currentIndex);
        }
        beforeShowSelectValueDialog(dialog);
        if (dialog.execDialog()==IDialog.DialogResult.ACCEPTED){
            return dialog.getCurrentRow();
        }else{
            return -1;
        }
    }
    
    protected void onSelectListWidgetItem(final TypifiedListWidgetItem<T> item){
        final T newValue = item.getValue();        
        if (getEditMask().validate(getEnvironment(),newValue)==ValidationResult.ACCEPTABLE) {
            setValue(item.getValue());
        }        
    }    
    
    public final String getDialogTitle(){
        return dialogTitle;
    }
    
    public final void setDialogTitle(final String title){
        dialogTitle = title;
    }    
    
    protected String getSelectValueDialogConfigPrefix(){
        return getClass().getName();
    }
    
    protected void beforeShowSelectValueDialog(final IListDialog dialog){
        
    }
    
    protected final void subscribeToChangeSettingsEvent(){
        if (settingsListener==null){
            settingsListener = new SettingsChangeListener();
            ((WpsEnvironment)getEnvironment()).addSettingsChangeListener(settingsListener);
        }
    }
    
    protected final void unsubscribeFromChangeSettingsEvent(){
        if (settingsListener!=null){
            ((WpsEnvironment)getEnvironment()).removeSettingsChangeListener(settingsListener);
            settingsListener = null;
        }
    }
    
    protected void onSettingsChanged(){
        refreshShowPredefinedValuesButton();
    }
    
    protected void afterChangeReadOnly() {
        final InputBox box = getInputBox();
        if (box!=null && !box.isPrintable()){
            doValidation();
            box.refreshTextOptions();
            refreshShowPredefinedValuesButton();
        }        
    }

    public final InputBox.ValueController<T> getValueController() {
        final InputBox<T> box = getInputBox();
        return box == null ? null : box.getValueController();
    }

    @Override
    public ValEditorController<T, V> getController() {
        return this;
    }
    
    public final void setDisplayController(final InputBox.DisplayController<T> c){
        final InputBox<T> box = getInputBox();
        if (box!=null){
            box.setDisplayController(c);
        }
    }
    
    public final InputBox.DisplayController<T> getDisplayController(){
        final InputBox<T> box = getInputBox();
        return box==null ? null : box.getDisplayController();
    }
    
    protected boolean processHtmlEvent(final HtmlEvent event) {
        return false;
    }
}
