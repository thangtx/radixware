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

package org.radixware.kernel.explorer.widgets.propeditors;

import com.trolltech.qt.QNoSuchEnumValueException;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.editors.property.PropEditorController;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyArr;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.client.types.CommonEditingHistory;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.ICommandToolButton;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.types.ICachableWidget;
import org.radixware.kernel.explorer.utils.WidgetUtils;

/**
 * PropEditor - редактор свойства Содержит внутри себя valEditor, который соответствует типу свойства.
 * Создание отдельных valEditor, объясняется необходимостью редакторов, не привязанных к конкретным Property.
 * Каждый PropEditor должен реализовать абстрактный метод initValEditor().
 *
 */
public abstract class PropEditor extends AbstractPropEditor implements ICachableWidget {
    
    private final static int COMMAND_BUTTONS_PRIORITY = Integer.MAX_VALUE - 100;

    private final static class SetFocusEvent extends QEvent {

        public SetFocusEvent() {
            super(QEvent.Type.User);
        }
    }

    private class DisplayStringProvider implements IDisplayStringProvider {

        @Override
        public String format(final EditMask mask, final Object value, final String defaultDisplayString) {
            final EEditMaskType maskType = mask == null ? null : mask.getType();
            final boolean isEditMaskCompatible =
                    maskType == EEditMaskType.LIST || maskType == EEditMaskType.ENUM || maskType == EEditMaskType.BOOL;
            final Property property = getProperty();
            final EValType valType = property.getType();
            final boolean isValTypeCompatible = valType.isArrayType() || valType==EValType.OBJECT || valType==EValType.XML;
            final boolean displayStringCanBeOverrided = isEditMaskCompatible || isValTypeCompatible || isReadonly();
            if (displayStringCanBeOverrided) {
                return getProperty().getOwner().getDisplayString(property.getId(), value, defaultDisplayString, !property.hasOwnValue());
            }
            return defaultDisplayString;
        }
    }

    private class PropertyValueTextOptionsProvaider implements ITextOptionsProvider {

        @Override
        public ExplorerTextOptions getOptions(final EnumSet<ETextOptionsMarker> markers, final ESelectorRowStyle style) {
            return PropEditor.this.getTextOptions(markers);
        }
    }

    private static class ChangeFontEventFilter extends QEventFilter {

        public ChangeFontEventFilter(final QObject parent) {
            super(parent);
            setProhibitedEventTypes(EnumSet.of(QEvent.Type.FontChange));
        }
    }
    
    private QHBoxLayout layout;
    private ValEditor valEditor;
    private boolean initComplete;
    private boolean inCache;
    private boolean wasBinded;    
    private List<QToolButton> customButtons;
    private ChangeFontEventFilter changeFontEventFilter;

    @SuppressWarnings("unchecked")
    protected PropEditor(final Property property, final ValEditorFactory valEditorFactory) {
        super(property);        
        createUi(valEditorFactory);
        controller.addPropertyValueChangeListener(new PropEditorController.PropertyValueChangeListener() {
            @Override
            public void onPropertyValueChanged() {
                getValEditor().updateHistory();
            }
        });
        initComplete = true;
    }

    public String getPropTitle() {
        // 20180313 Котрачев добавил этот метод только для 
        // org.radixware\kernel\explorer\src\org\radixware\kernel\explorer\webdriver\elements\UIElementPropertyGetters.java
        // UIElementPropertyGetters.isPropertyGetter
        // чтобы упростить клиенту поиск элемента по имени проперти.
        return this.getProperty().getTitle();
    }

    private void createUi(final ValEditorFactory valEditorFactory) {
        layout = WidgetUtils.createHBoxLayout(this);
        final EValType valType = ValueConverter.serverValType2ClientValType(getProperty().getType());
        valEditor =
            valEditorFactory.createValEditor(valType, getPropertyEditMask(), getEnvironment(), this);
        initValEditor();
        setSizePolicy(Policy.Expanding, Policy.Fixed);
        layout.addWidget(valEditor);
        addStandardButtons();
    }
    
    @SuppressWarnings("unchecked")
    protected void changeValEditor(final ValEditorFactory valEditorFactory){        
        clearValEditor(false);
        final List<IButton> standardButtons = controller.getStandardButtons();
        {
            final ValEditor editor = getValEditor();            
            for (IButton button: standardButtons){
                if (button instanceof QToolButton){
                    editor.removeButton((QToolButton)button, false);
                    ((QToolButton)button).setParent(this);
                }
            }

            layout.removeWidget(editor);
            editor.setParent(null);
            editor.close();
            editor.disposeLater();
        }
        final EValType valType = ValueConverter.serverValType2ClientValType(getProperty().getType());
        valEditor =
            valEditorFactory.createValEditor(valType, getPropertyEditMask(), getEnvironment(), this);
        initValEditor();
        layout.insertWidget(0, valEditor);  
        
        for (IButton button: standardButtons){
            if (button instanceof QToolButton){
                valEditor.addButton((QToolButton)button);
            }
        }
        addCommandButtons(controller.getCommandToolButtons());
        
        if (customButtons!=null){
            for (QToolButton button: customButtons){
                valEditor.addButton(button);
            }
        }
        
        if (wasBinded){
            valEditor.valueChanged.connect(controller, "onValueEdit(Object)");
        }
            
    }
    
    private void initValEditor(){
        {
            valEditor.setObjectName("ValEditor");
            if (valEditor.getLineEdit() != null) {
                valEditor.getLineEdit().installEventFilter(focusListener);
            }
            if (valEditor.getDisplayStringProvider() == null) {//Если в методе init не был установлен другой DisplayStringProvider.
                valEditor.setDisplayStringProvider(new DisplayStringProvider());
            }
            valEditor.setAutoValidationEnabled(false);
            valEditor.setTextOptionsProvider(new PropertyValueTextOptionsProvaider());
            valEditor.installEventFilter(getChangeFontEventFilter());
        }
        if (getProperty().getDefinition().storeHistory()) {
            final StringBuilder settingPath = new StringBuilder(getProperty().getOwner().getDefinition().getId().toString());
            settingPath.append('/');
            settingPath.append(SettingNames.SYSTEM);
            settingPath.append('/');
            settingPath.append(getProperty().getDefinition().getId().toString());
            enableEditingHistory(settingPath.toString());
        }
        {
            setFocusPolicy(Qt.FocusPolicy.StrongFocus);
            setFocusProxy(valEditor.getLineEdit());
        }        
    }

    @Override    
    @SuppressWarnings("unchecked")
    void setProperty(final Property property) {
        super.setProperty(property);
        if (property != null) {
            controller.addPropertyValueChangeListener(new PropEditorController.PropertyValueChangeListener() {
                @Override
                public void onPropertyValueChanged() {
                    getValEditor().updateHistory();
                }
            });
            if (valEditor.getDisplayStringProvider() == null) {//Если в методе init не был установлен другой DisplayStringProvider.
                valEditor.setDisplayStringProvider(new DisplayStringProvider());
            }
            valEditor.setTextOptionsProvider(new PropertyValueTextOptionsProvaider());
            if (getProperty().getDefinition().storeHistory() && !(getProperty() instanceof PropertyArr)) {
                final StringBuilder settingPath = new StringBuilder(getProperty().getOwner().getDefinition().getId().toString());
                settingPath.append('/');
                settingPath.append(SettingNames.SYSTEM);
                settingPath.append('/');
                settingPath.append(getProperty().getDefinition().getId().toString());
                enableEditingHistory(settingPath.toString());
            }
            {//add command buttons in the order like at first time
                addCommandButtons(controller.getCommandToolButtons());
           }
            valEditor.installEventFilter(getChangeFontEventFilter());
            setObjectName("propEditor #"+property.getId().toString());
            //setWindowTitle(property.getTitle());
            
        }
    }
    
    protected int getCommandButtonPriority(final QToolButton commandButton){
        return COMMAND_BUTTONS_PRIORITY;
    }
    
    EEditMaskType getEditMaskType(){
        return valEditor.getEditMask().getType();
    }
    
    protected void enableEditingHistory(final String settingPath) {
        setEditingHistory(new CommonEditingHistory(getEnvironment(), settingPath));
    }

    public final QToolButton addButton(final String text, final QAction action) {
        final QToolButton button = valEditor.addButton(text, action);
        if (initComplete) {
            if (customButtons == null) {
                customButtons = new LinkedList<>();
            }
            customButtons.add(button);
        }
        return button;
    }

    public final void addButton(final QToolButton button) {
        valEditor.addButton(button);
        if (initComplete) {
            if (customButtons == null) {
                customButtons = new LinkedList<>();
            }
            customButtons.add(button);
        }
    }

    @Override
    public void addButton(final IButton button) {
        addButton((QToolButton) button);
    }
        
    @SuppressWarnings("unchecked")//invalid java warning on controller.getCommandToolButtons() call
    @Override
    protected void addCommandButtons(final List<ICommandToolButton> commandButtons) {
        final List<ICommandToolButton> buttons = new ArrayList<>(commandButtons);
        Collections.reverse(buttons);
        QToolButton commandToolButton;
        for (ICommandToolButton commandButton : buttons) {
            commandToolButton = (QToolButton)commandButton;
            valEditor.addButton(commandToolButton, getCommandButtonPriority(commandToolButton));
        }
    }
    
    @Override
    public void refresh(final ModelItem changedItem) {
        if (getProperty()!=null){
            super.refresh(changedItem);
            getValEditor().refresh();
        }
    }

    @Override
    public void bind() {
        super.bind();
        valEditor.valueChanged.connect(controller, "onValueEdit(Object)");
        Application.getInstance().getActions().settingsChanged.connect(this, "updateSettings()");
        wasBinded = true;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void updateEditor(final Object value, final PropEditorOptions options) {
        final ValEditor finalValEditor = getValEditor();
        final boolean valEditorFocused = finalValEditor.hasFocus();
        finalValEditor.setToolTip(options.getTooltip());
        finalValEditor.setValue(value);
        finalValEditor.setEditMask(options.getEditMask());
        finalValEditor.setMandatory(options.isMandatory());
        finalValEditor.setReadOnly(options.isReadOnly());
        finalValEditor.setEmbeddedWidgetsVisible(options.isEnabled());
        if (RadPropertyDef.isPredefinedValuesSupported(getProperty().getType(), options.getEditMask().getType())){
            finalValEditor.setPredefinedValues(options.getPredefinedValues());
        }
        final UnacceptableInput unacceptableInput = options.getUnacceptableInput(); 
        if (unacceptableInput!=null){
            final String invalidText = unacceptableInput.getText();
            if (invalidText!=null && !invalidText.isEmpty()){
                finalValEditor.setInputText(invalidText);
            }
        }
        final String dialogTitle = 
                ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), getProperty().getTitle());
        finalValEditor.setDialogTitle(dialogTitle);
        finalValEditor.setMaxPredefinedValuesInDropDownList(getProperty().getMaxPredefinedValuesInDropDownList());
        if (valEditorFocused && !finalValEditor.hasFocus()){
            finalValEditor.scheduleSetFocus();
        }
    }

    @Override
    protected void updateSettings() {
        final Property finalProperty = getProperty();
        final ValidationResult state = finalProperty.getOwner().getPropertyValueState(finalProperty.getId());
        final ValEditor finalValEditor = getValEditor();
        final boolean valEditorFocused = finalValEditor.hasFocus();
        finalValEditor.setValidationResult(state);
        finalValEditor.refreshTextOptions();
        finalValEditor.refreshShowPredefinedValuesButton();
        finalValEditor.refresh();
        if (valEditorFocused && !finalValEditor.hasFocus()){
            finalValEditor.scheduleSetFocus();
        }
    }

    @Override
    protected Object getCurrentValueInEditor() {
        return getValEditor().getValue();
    }

    protected QHBoxLayout getLayout() {
        return layout;
    }

    protected ValEditor getValEditor() {
        return valEditor;
    }

    protected EnumSet<ETextOptionsMarker> getTextOptionsMarkers(final EnumSet<ETextOptionsMarker> valEditorMarkers) {
        final EnumSet<ETextOptionsMarker> propertyMarkers = getProperty().getTextOptionsMarkers();
        if (!valEditorMarkers.contains(ETextOptionsMarker.UNDEFINED_VALUE)) {
            propertyMarkers.remove(ETextOptionsMarker.UNDEFINED_VALUE);
        }        
        if (getValEditor()!=null && !getValEditor().hasAcceptableInput()){
            propertyMarkers.add(ETextOptionsMarker.INVALID_VALUE);
        }else if (!valEditorMarkers.contains(ETextOptionsMarker.INVALID_VALUE)) {
            propertyMarkers.remove(ETextOptionsMarker.INVALID_VALUE);
        }            
        return propertyMarkers;
    }

    private ExplorerTextOptions getTextOptions(final EnumSet<ETextOptionsMarker> valEditorMarkers) {
        return (ExplorerTextOptions) getProperty().getValueTextOptions().getOptions(getTextOptionsMarkers(valEditorMarkers));
    }

    public void changeStateForGrid() {
        getValEditor().changeStateForGrid();
//        for (CommandToolButton commandButton : commandButtons) {
//            commandButton.refresh(null);
//        }
//        inGrid = true;
        controller.changeStateForGrid();
        valEditor.refreshTextOptions();
    }
    
    public void setAdjustWidthByContentModeEnabled(final boolean enabled){
        getValEditor().setAdjustWidthByContentModeEnabled(enabled);
    }
    
    public boolean isAdjustWidthByContentModeEnabled(){
        return getValEditor().isAdjustWidthByContentModeEnabled();
    }    

    private QObject getChangeFontEventFilter() {
        if (changeFontEventFilter == null) {
            changeFontEventFilter = new ChangeFontEventFilter(this);
        }
        return changeFontEventFilter;
    }

    @Override
    public QSize sizeHint() {
        return valEditor.sizeHint();
    }

    @Override
    public boolean setFocus(final Property property) {
        QApplication.postEvent(this, new SetFocusEvent());
        return true;
    }

    @Override
    protected void keyPressEvent(final QKeyEvent event) {
        final Qt.Key key;
        if (!controller.isInGrid()) {//QTableView has own handles of Enter and Escape keys
            try {
                key = Qt.Key.resolve(event.key());
            } catch (QNoSuchEnumValueException exception) {
                super.keyPressEvent(event);
                return;
            }
            if (key == Qt.Key.Key_Enter || key == Qt.Key.Key_Return) {
                final int pos = getCursorPosition();
                controller.finishEdit();
                returnFocus(pos);
            } else if (key == Qt.Key.Key_Escape) {
                controller.refresh(getProperty());
                returnFocus(-1);
            }
        }
        getValEditor().eventFilter(this, event);
        super.keyPressEvent(event);
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {        
        Application.getInstance().getActions().settingsChanged.disconnect(this);
        clearValEditor(true);
        super.closeEvent(event);
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof SetFocusEvent) {
            event.accept();
            final QWidget editor = getValEditor();
            if (editor != null && editor.nativeId() != 0) {
                editor.setFocus();
            }
            return;
        }
        super.customEvent(event); //To change body of generated methods, choose Tools | Templates.
    }

    private int getCursorPosition() {
        return getValEditor().getLineEdit() == null ? -1 : getValEditor().getLineEdit().cursorPosition();
    }

    private void returnFocus(final int cursorPos) {
        setFocus();
        final QLineEdit lineEdit = getValEditor().getLineEdit();
        if (lineEdit != null) {
            lineEdit.setSelection(0, 0);
            lineEdit.selectionChanged.emit();//This signal does not emitted automatically. Qt bug?
            if (cursorPos > 0 && cursorPos < lineEdit.text().length()) {
                lineEdit.setCursorPosition(cursorPos);
            } else {
                lineEdit.end(false);
            }
        }
    }

    public void selectAll() {
        final QLineEdit lineEdit = getValEditor().getLineEdit();
        if (lineEdit != null// &&
                //			getValEditor().getLineEdit().text()!=null &&
                //			!getValEditor().getLineEdit().text().isEmpty()
                ) {
            lineEdit.selectAll();
            lineEdit.setCursorPosition(0);
        }
    }

    @Override
    protected void closeEditor() {
        final ValEditor editor = getValEditor();
        if (editor != null) {
            if (editor.getLineEdit() != null) {
                editor.getLineEdit().removeEventFilter(focusListener);
            }
            editor.close();
        }
    }

    @Override
    protected UnacceptableInput getUnacceptableInput() {
        final ValEditor editor = getValEditor();
        return editor==null ? null : editor.getUnacceptableInput();
    }

    @Override
    @SuppressWarnings("unchecked")//invalid java warning on controller.getCommandToolButtons() call
    void clear() {
        clearValEditor(true);
        super.clear();
        //it must be after setParent(null) call
        final ValEditor editor = getValEditor();
        editor.setDefaultTextOptions(null);
        editor.setValue(null);
    }
    
    @SuppressWarnings("unchecked")//invalid java warning on controller.getCommandToolButtons() call
    private void clearValEditor(final boolean disposeButtons){
        final ValEditor editor = getValEditor();
        editor.setTextOptionsProvider(null);
        editor.setDisplayStringProvider(null);
        editor.setHighlightedFrame(false);
        setEditingHistory(null);
        if (customButtons != null) {
            for (QToolButton customButton : customButtons) {
                editor.removeButton(customButton, disposeButtons);
            }
            customButtons = null;
        }
        valEditor.valueChanged.disconnect(controller);
        valEditor.removeEventFilter(getChangeFontEventFilter());
        Application.getInstance().getActions().settingsChanged.disconnect(this);
        final List<ICommandToolButton> commandButtons = controller.getCommandToolButtons();
        for (ICommandToolButton button : commandButtons) {
            editor.removeButton((QToolButton) button, disposeButtons);
        }
    }

    @Override
    public void setHighlightedFrame(final boolean isHighlighted) {
        final ValEditor valEditor = getValEditor();
        if (valEditor != null) {
            if (isHighlighted){
                valEditor.setFrame(true);
            }else if (controller.isInGrid()){
                valEditor.setFrame(false);
            }
            valEditor.setHighlightedFrame(isHighlighted);
        }
    }

    public void setEditingHistory(final IEditingHistory history) {
        getValEditor().setEditingHistory(history);
    }

    public void setInCache(final boolean inCache) {
        this.inCache = inCache;
    }

    @Override
    public boolean isInCache() {
        return inCache;
    }
}
