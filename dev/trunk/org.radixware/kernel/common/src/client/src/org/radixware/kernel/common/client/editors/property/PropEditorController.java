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

package org.radixware.kernel.common.client.editors.property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.errors.SettingPropertyValueError;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrRef;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.models.items.properties.PropertyValue;
import org.radixware.kernel.common.client.models.items.properties.SimpleProperty;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.client.views.IPropertyStorePossibility;
import org.radixware.kernel.common.client.views.IPropertyValueStorage;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.ICommandToolButton;
import org.radixware.kernel.common.client.widgets.IModificationListener;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EPropertyValueStorePossibility;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * AbstractPropEditor - абстрактный редактор свойства Содержит сигналы, кнопку изменения признака собственного
 * значения, кнопку вызова кастомного диалога редактора, и кнопки команд свойства, Реализованы базовый функции
 * установки значения свойства, обновление состояния кнопок и их обработчики. О внешнем виде самого редактора
 * здесь ничего неизвестно
 *
 *
 */
public abstract class PropEditorController<T extends IPropEditor & IWidget> extends PropVisualizerController<T> implements IPropertyValueStorage {

    public interface FocusListener {

        public void focused();

        public void unfocused();
    }

    public interface EditActionListener {

        public void edited(Object obj);

        public void disconnected();
    }

    public interface PropertyValueChangeListener {

        void onPropertyValueChanged();
    }
    protected IButton ownValueBtn;//, inheritancePathBtn;
    protected IButton customDialogBtn;
    protected IButton loadFromFileBtn;
    protected IButton saveToFileBtn;
    protected final List<ICommandToolButton> commandButtons = new ArrayList<>();
    private final List<EditActionListener> editListeners = new LinkedList<>();
    private final List<FocusListener> focusListeners = new LinkedList<>();
    private List<PropertyValueChangeListener> propertyChangeListeners;
    private Object previousVal;
    private UnacceptableInput previousUnacceptableInput;
    private boolean wasEdited;
    private boolean ignoreFocusEvents;
    private boolean inGrid = false;
    private boolean wasClosed = false;
    private boolean internalUpdate;
    private PropertyProxy propertyProxy;
    private IPropertyStorePossibility storePossibility;

    protected abstract IModificationListener findParentModificationListener();

    public IButton getOwnValButton() {
        return ownValueBtn;
    }

    public IButton getCustomDialogButton() {
        return customDialogBtn;
    }

    public IButton getLoadFromFileButton() {
        return loadFromFileBtn;
    }

    public IButton getSaveToFileButton() {
        return saveToFileBtn;
    }

    public List<ICommandToolButton> getCommandToolButtons() {
        return Collections.unmodifiableList(commandButtons);
    }

    protected final T getPropEditor() {
        return getWidget();
    }

    public final void addFocusListener(FocusListener listener) {
        synchronized (focusListeners) {
            if (!focusListeners.contains(listener)) {
                focusListeners.add(listener);
            }
        }
    }

    public final void removeFocusListener(FocusListener listener) {
        synchronized (focusListeners) {
            focusListeners.remove(listener);
        }
    }

    public final void addEditActionListener(EditActionListener listener) {
        synchronized (editListeners) {
            if (!editListeners.contains(listener)) {
                editListeners.add(listener);
            }
        }
    }

    public final void removeEditActionListener(EditActionListener listener) {
        synchronized (editListeners) {
            editListeners.remove(listener);
            listener.disconnected();
        }
    }

    public final void addPropertyValueChangeListener(final PropertyValueChangeListener listener) {
        if (listener != null) {
            if (propertyChangeListeners == null) {
                propertyChangeListeners = new LinkedList<>();
            }
            propertyChangeListeners.add(listener);
        }
    }

    public final void removePropertyValueChangeListener(final PropertyValueChangeListener listener) {
        if (propertyChangeListeners != null && listener != null) {
            propertyChangeListeners.remove(listener);
        }
    }

    private void fireFocusChange(boolean in) {
        synchronized (focusListeners) {
            for (FocusListener l : focusListeners) {
                if (in) {
                    l.focused();
                } else {
                    l.unfocused();
                }
            }
        }
    }

    private void fireEditAction(Object value) {
        synchronized (editListeners) {
            for (EditActionListener l : editListeners) {
                l.edited(value);
            }
        }
    }

    private void firePropertyValueChanged() {
        if (propertyChangeListeners != null) {
            for (PropertyValueChangeListener listener : propertyChangeListeners) {
                listener.onPropertyValueChanged();
            }
        }
    }

    private IButton createToolButton(String text, String objectName, IButton.ClickHandler clickHandler) {
        final IButton button = getEnvironment().getApplication().getWidgetFactory().newToolButton();
        if (text != null) {
            button.setTitle(text);
        }
        if (objectName != null) {
            button.setObjectName(objectName);
        }
        if (clickHandler != null) {
            button.addClickHandler(clickHandler);
        }
        return button;
    }

    public PropEditorController(final Property property, final T editor) {
        this(editor);
        setProperty(property);
    }

    public PropEditorController(final T editor) {
        super(null, editor);
    }

    private void createButtons() {
        customDialogBtn = createToolButton("...", "btnCustomDialog", new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                onExecPropEditorDialog();
            }
        });
        ownValueBtn = createToolButton(null, "btnOwnValue", new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                onOwnValueClicked();
            }
        });
        if (getProperty() instanceof SimpleProperty) {
            loadFromFileBtn = createLoadButton();
            saveToFileBtn = createSaveButton();
        }
    }

    @Override
    public final void setProperty(final Property property) {
        super.setProperty(property);
        if (property != null) {
            if (ownValueBtn == null) {
                createButtons();
            }
            List<Id> commandIds = property.getEnabledCommands();
            Command cmd;
            ICommandToolButton button;
            for (Id commandId : commandIds) {
                cmd = property.getOwner().getCommand(commandId);
                button = cmd.createToolButton(property.getId());
                button.setObjectName("command button #" + commandId.toString());
                commandButtons.add(button);
            }
        } else {
            if (ownValueBtn != null) {
                ownValueBtn.setVisible(false);
                customDialogBtn.setVisible(false);
                if (loadFromFileBtn != null) {
                    loadFromFileBtn.setVisible(false);
                }
                if (saveToFileBtn != null) {
                    saveToFileBtn.setVisible(false);
                }
            }
        }
    }

    private IButton createLoadButton() {
        IButton lb = createToolButton(null, "btnLoadFromFile", new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                onLoadFormFileButtonClicked();
            }
        });
        lb.setIcon(getIcon(ClientIcon.CommonOperations.OPEN));
        lb.setEnabled(!isReadonly());
        lb.setToolTip(getEnvironment().getMessageProvider().translate("PropertyEditor", "Load from File"));
        return lb;
    }

    private IButton createSaveButton() {
        IButton sb = createToolButton(null, "btnSaveToFile", new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                onSaveToFileButtonClicked();
            }
        });
        sb.setIcon(getIcon(ClientIcon.CommonOperations.SAVE));
        sb.setToolTip(getEnvironment().getMessageProvider().translate("PropertyEditor", "Save to File"));
        return sb;
    }

    @Override
    public void setPropertyStorePossibility(IPropertyStorePossibility isp) {
        this.storePossibility = isp;
    }

    @Override
    public IPropertyStorePossibility getPropertyStorePossibility() {
        return this.storePossibility;
    }

    // проверка, вызывается при принятии решении о отображении ownValBtn
    private boolean testInherit() {
        final Property property = getProperty();
        Model owner = property.getOwner();
        return property.getDefinition().isInheritable()
                && (property.getDefinition().getType() != EValType.OBJECT
                || ((owner instanceof EntityModel) && ((EntityModel) owner).isExists()));
    }

    private boolean testCustomDialog() {
        final Property property = getProperty();
        return property.getDefinition().customDialog()
                && property.getOwner().canUsePropEditorDialog(property.getId());
    }

    private Icon getIcon(ClientIcon icon) {
        return getEnvironment().getApplication().getImageManager().getIcon(icon);
    }

    private void updateOwnValueBtn() {
        final Property property = getProperty();
        if (testInherit() && !property.isReadonly() && property.getInheritableValue() != null
                && //!property.isCustomEditOnly() && //by BAO 07.07.10 to allow use ownValueBtn with custom dialog button
                property.getEditPossibility() != EEditPossibility.PROGRAMMATICALLY) {

            ownValueBtn.setVisible(true);
            if (property.getDefinition().isInheritable()) {
                if (property.hasOwnValue()) {
                    ownValueBtn.setToolTip(getEnvironment().getMessageProvider().translate("PropertyEditor", "Inherit Value"));
                    ownValueBtn.setIcon(getIcon(ClientIcon.ValueModification.INHERIT));
                } else {
                    ownValueBtn.setToolTip(getEnvironment().getMessageProvider().translate("PropertyEditor", "Override Value"));
                    ownValueBtn.setIcon(getIcon(ClientIcon.ValueModification.OVERRIDE));
                }
            } else {
                if (property.isValueDefined()) {
                    ownValueBtn.setToolTip(getEnvironment().getMessageProvider().translate("PropertyEditor", "Set to None"));
                    ownValueBtn.setIcon(getIcon(ClientIcon.ValueModification.CLEAR));
                } else {
                    ownValueBtn.setToolTip(getEnvironment().getMessageProvider().translate("PropertyEditor", "Define Value"));
                    ownValueBtn.setIcon(getIcon(ClientIcon.ValueModification.DEFINE));
                }
            }
        } else {
            ownValueBtn.setVisible(false);
        }
    }

    @Override
    protected String getToolTip() {
        final String hint = getProperty().getHint();
        final PropertyValue.InheritableValue inheritableValue = getProperty().getInheritableValue();
        if (testInherit() && inheritableValue != null && !getProperty().hasOwnValue()) {
            final String toolTip;
            if (hint != null && !hint.isEmpty()) {
                toolTip = getEnvironment().getMessageProvider().translate("PropertyEditor", "%s\nInherited from %s\'%s\'");
            } else {
                toolTip = getEnvironment().getMessageProvider().translate("PropertyEditor", "Inherited from %s\'%s\'");
            }
            final String classTitle = inheritableValue.getOwnerClassDef().getObjectTitle().isEmpty() ? "" : inheritableValue.getOwnerClassDef().getObjectTitle() + ": ";
            final String entityTitle = inheritableValue.getOwnerEntityTitle();
            if (hint != null && !hint.isEmpty()) {
                return String.format(toolTip, hint, classTitle, entityTitle);
            } else {
                return String.format(toolTip, classTitle, entityTitle);
            }
        }
        return hint;
    }

    @SuppressWarnings("unused")
    private void onOwnValueClicked() {
        try {
            final Property property = getProperty();
            property.setHasOwnValue(!property.hasOwnValue());
        } catch (InterruptedException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        } catch (Exception ex) {
            final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on editing property \'%s\': \n%s");
            processException(ex, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on editing property"), msg);
        }
    }

    private void onLoadFormFileButtonClicked() {
        final SimpleProperty prop = (SimpleProperty) getProperty();
        Object value = getPropertyStorePossibility().readPropertyValue(null);
        prop.setValueObject(value);
    }

    private void onSaveToFileButtonClicked() {
        final SimpleProperty prop = (SimpleProperty) getProperty();
        Object value = prop.getValueObject();
        getPropertyStorePossibility().writePropertyValue(value);
    }

    @SuppressWarnings("unused")
    private void onExecPropEditorDialog() {
        finishEdit();
        //editor may be closed after set property value
        if (!getPropEditor().isDisposed()) {
            try {
                getProperty().execPropEditorDialog();
            } catch (Exception ex) {
                final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on executing custom dialog for \'%s\': \n%s");
                processException(ex, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on editing property"), msg);
            }
        }
    }

    public final void setPropertyProxy(final PropertyProxy proxy) {
        propertyProxy = proxy;
    }

    private PropertyProxy getPropertyProxy() {
        return propertyProxy;
    }

    @Override
    public Property getProperty() {
        return getPropertyProxy() == null ? super.getProperty() : getPropertyProxy().getProperty();
    }

    public final Object getPropertyValue() {
        //Для некоторых свойств может потребоваться дополнительное
        //преобразование класса значения свойства в класс, потдерживаемый
        //редактором свойства (valEditor)
        return getPropertyProxy() == null ? getProperty().getValueObject() : getPropertyProxy().getPropertyValue();
    }    

    public final Object getPropertyInitialValue() {
        return getPropertyProxy() == null ? getProperty().getInitialValue() : getPropertyProxy().getPropertyInitialValue();
    }
    
    public final UnacceptableInput getPropertyUnacceptableInput(){
        return getPropertyProxy() == null ? getProperty().getUnacceptableInput() : getPropertyProxy().getPropertyUnacceptableInput();
    }

    //Наследники могут подменять editMask
    public final EditMask getPropertyEditMask() {
        return getPropertyProxy() == null ? getProperty().getEditMask() : getPropertyProxy().getPropertyEditMask();
    }

    public final boolean isReadonly() {
        final Property property = getProperty();

        return (getPropertyProxy() == null ? property.isReadonly() : getPropertyProxy().isPropertyReadonly())
                || (!property.hasOwnValue() && property.isValueDefined())
                || property.isCustomEditOnly()
                || property.getEditPossibility() == EEditPossibility.PROGRAMMATICALLY
                || (property instanceof PropertyArrRef && !((PropertyArrRef) property).canOpenParentSelector())
                || (property instanceof PropertyRef && !((PropertyRef) property).canOpenParentSelector());
    }

    public void processException(final Throwable ex, final String title, final String msg) {        
        getProperty().getOwner().showException(ex);
    }

    public String getFocusedStyleSheet() {
        return "";
    }

    @Override
    public void bind() {
        for (ICommandToolButton button : commandButtons) {
            button.bind();
        }
        super.bind();
    }

    @Override
    protected void updateVisualizer() {
        final Property finalProperty = getProperty();
        Object currentValue = getPropertyValue();
        if (currentValue instanceof IKernelEnum) {
            currentValue = ((IKernelEnum) currentValue).getValue();
        }        
        
        previousVal = currentValue instanceof XmlObject ? ((XmlObject) currentValue).xmlText() : currentValue;
        previousUnacceptableInput = getPropertyUnacceptableInput();

        if (customDialogBtn != null) {
            customDialogBtn.setVisible(testCustomDialog() && finalProperty.canOpenPropEditorDialog());
        }
        if (finalProperty instanceof SimpleProperty) {
            final EPropertyValueStorePossibility pvsp = ((SimpleProperty) finalProperty).getPropertyValueStorePossibility();
            saveToFileBtn.setVisible((pvsp == EPropertyValueStorePossibility.FILE_SAVE || pvsp == EPropertyValueStorePossibility.FILE_SAVE_AND_LOAD) && currentValue != null);
            loadFromFileBtn.setVisible((pvsp == EPropertyValueStorePossibility.FILE_LOAD || pvsp == EPropertyValueStorePossibility.FILE_SAVE_AND_LOAD) && !isReadonly());
        }
        final PropEditorOptions editorData = new PropEditorOptions();
        editorData.setTooltip(getToolTip());
        editorData.setEditMask(getPropertyEditMask());
        editorData.setMandatory(finalProperty.isMandatory() || !finalProperty.isOwnValueAcceptable(null));
        editorData.setReadOnly(isReadonly());
        if (RadPropertyDef.isPredefinedValuesSupported(finalProperty.getType(), getPropertyEditMask().getType())) {
            editorData.setPredefinedValues(finalProperty.getPredefinedValues());
        }
        editorData.setUnacceptableInput(getPropertyUnacceptableInput());
        //editorData.setInvalidInputText(finalProperty.getInvalidInputText());
        internalUpdate = true;
        try {
            updateEditor(currentValue, editorData);
        } finally {
            internalUpdate = false;
        }
        wasEdited = false;
        updateOwnValueBtn();
    }

    public void onValueEdit(final Object value) {
        if (!internalUpdate) {
            notifyPropertyStartEdit();
            aboutToModifyPropertyValue();
            fireEditAction(value);
        }
    }

    private void notifyPropertyStartEdit() {
        if (!wasEdited) {
            getProperty().onStartEditValue();
            wasEdited = true;
        }
    }
    private boolean currentValueModified;

    private void aboutToModifyPropertyValue() {
        if (needToNotifyOnModificationStateChanged()) {
            final Property finalProperty = getProperty();
            Object value = getCurrentValueInEditor();
            if (value instanceof XmlObject) {
                value = ((XmlObject) value).xmlText();
            }
            if (value == null && finalProperty.isMandatory()) {
                return;
            }
            if (!finalProperty.isOwnValueAcceptable(value)) {
                return;
            }
            final IModificationListener listener = findParentModificationListener();
            if (listener != null) {
                Object initialValue = getPropertyInitialValue();
                if (initialValue instanceof XmlObject) {
                    initialValue = ((XmlObject) initialValue).xmlText();
                }
                final boolean isModified = !Utils.equals(value, initialValue);
                if (currentValueModified && !isModified) {
                    currentValueModified = false;
                    listener.notifyPropertyModificationStateChanged(finalProperty, false);
                } else if (!currentValueModified && isModified) {
                    currentValueModified = true;
                    listener.notifyPropertyModificationStateChanged(finalProperty, true);
                }
            }
        }
    }

    public final void onValueModificationStarted() {
        if (needToNotifyOnModificationStateChanged()) {
            final IModificationListener listener = findParentModificationListener();
            if (listener != null) {
                listener.notifyPropertyModificationStateChanged(getProperty(), true);
            }
        }
        notifyPropertyStartEdit();
    }

    public final void onValueModificationDiscarded() {
        if (needToNotifyOnModificationStateChanged()) {
            if (valuesEquals(getCurrentValueInEditor(), getPropertyInitialValue())) {
                final IModificationListener listener = findParentModificationListener();
                if (listener != null) {
                    listener.notifyPropertyModificationStateChanged(getProperty(), false);
                }
            }
        }
    }

    private boolean needToNotifyOnModificationStateChanged() {
        final Property finalProperty = getProperty();
        if ((finalProperty.getOwner() instanceof EntityModel) && !isReadonly() && finalProperty.getType() != EValType.BOOL) {
            final EntityModel entity = (EntityModel) finalProperty.getOwner();
            return !entity.isNew() && entity.isExists();
        }
        return false;
    }

    private void discardPropertyModification() {//TWRBS-3372
        refresh(getProperty());
        onValueModificationDiscarded();
    }

    private boolean equalsToPreviousState(final Object value, final UnacceptableInput unacceptableInput) {
        if (!Objects.equals(previousUnacceptableInput, unacceptableInput)){
            return false;
        }
        if (value instanceof XmlObject) {
            return Utils.equals(((XmlObject) value).xmlText(), previousVal);
        }
        return Objects.equals(value, previousVal);
    }

    @SuppressWarnings("unchecked")
    private static boolean valuesEquals(final Object value1, final Object value2) {
        if (value1 instanceof XmlObject && value2 instanceof XmlObject) {
            return ((XmlObject) value1).xmlText().equals(((XmlObject) value2).xmlText());
        }
        if (value1 instanceof Comparable && value2 instanceof Comparable) {
            return ((Comparable) value1).compareTo(value2) == 0;
        }
        return Objects.equals(value1, value2);
    }

    /**
     * Передать значение из редактора в свойства. Вызывается в случае потери фокуса редактора или его
     * уничтожении.
     *
     */
    public final void finishEdit() {
        final Property property = getProperty();
        if (property != null && property.isActivated()) {
            ignoreFocusEvents = true;//to avoid recursive call of finishEdit(). TWRBS-1620
            try {
                if (isReadonly()) {
                    property.onFinishEditValue(false);
                    return;
                }                

                if (property.getOwner() instanceof EntityModel) {
                    final EntityModel entity = (EntityModel) property.getOwner();
                    if (!entity.isNew() && !entity.isExists()) {
                        //entity was removed: do not write property value
                        property.onFinishEditValue(false);
                        return;
                    }
                }
                
                final UnacceptableInput unacceptableInput = getUnacceptableInput();                
                final Object value = getCurrentValueInEditor();
                                
                if (equalsToPreviousState(value,unacceptableInput)) {
                    refresh(property);
                    property.onFinishEditValue(false);
                    return;//с предыдущего refresh ничего не менялось
                }
                
                if (unacceptableInput==null){
                    property.removeUnacceptableInputRegistration();
                }else{
                    if (unacceptableInput.equals(getPropertyUnacceptableInput())){
                        refresh(property);
                    }else{
                        property.registerUnacceptableInput(unacceptableInput);                        
                    }
                    property.onFinishEditValue(false);
                    return;                    
                }
                
                /*
                 * TWRBS-2197 if
                 * (getPropertyEditMask().validate(getEnvironment(), value) !=
                 * ValidationResult.ACCEPTABLE) { getEnvironment().alarmBeep();
                 * refresh(property);//устанавливаем прежнее значение
                 * property.onFinishEditValue(false); return; }
                 */
                if (!property.isOwnValueAcceptable(value)) {
                    getEnvironment().alarmBeep();
                    discardPropertyModification();//устанавливаем прежнее значение
                    property.onFinishEditValue(false);
                    return;
                }
                boolean propertyValueChanged = false;
                try {
                    if (value != null 
                        || property.hasOwnValue() 
                        || property.getDefinition().isInheritable()) {
                        final Object previousValue = getPropertyValue();                        
                        property.setValueObject(value);
                        currentValueModified = false;
                        if (valuesEquals(previousValue, getPropertyValue())) {
                            //Текущее значение свойства не изменилось (изменение было отклонено в сеттере)
                            discardPropertyModification();//устанавливаем прежнее значение
                        } else {
                            propertyValueChanged = true;
                            firePropertyValueChanged();
                        }
                    }
                } catch (Exception ex) {
                    getEnvironment().processException(new SettingPropertyValueError(property, ex));
                }
                property.onFinishEditValue(propertyValueChanged);
            } finally {
                ignoreFocusEvents = false;
            }
        }
    }

    public boolean focusEvent(boolean focusIn, boolean isPopupFocus) {
        if (!isPopupFocus && !ignoreFocusEvents) {
            fireFocusChange(focusIn);
            if (!focusIn && getProperty() != null) {
                finishEdit();
            }
        }
        return false;
    }

    public void focusOutEvent(boolean isPopupFocus) {
        if (!isPopupFocus && !ignoreFocusEvents && getProperty() != null) {
            currentValueModified = false;
            finishEdit();
        }
    }

    @Override
    public boolean close() {
        if (wasClosed) {
            return true;
        }
        wasClosed = true;
        closeEditor();
        clear();
        return super.close();
    }

    public final void clear() {
        for (ICommandToolButton button : commandButtons) {
            button.close();
        }
        commandButtons.clear();
        editListeners.clear();
        focusListeners.clear();
        propertyProxy = null;
        propertyChangeListeners = null;
        previousVal = null;
        previousUnacceptableInput = null;
        ignoreFocusEvents = false;
        inGrid = false;
        internalUpdate = false;
    }

    protected abstract void closeEditor();

    protected abstract void updateEditor(Object value, PropEditorOptions options);

    protected abstract Object getCurrentValueInEditor();
    
    protected abstract UnacceptableInput getUnacceptableInput();
    
    public void changeStateForGrid() {
        for (ICommandToolButton commandButton : commandButtons) {
            commandButton.refresh(null);
        }
        inGrid = true;
    }

    public boolean isInGrid() {
        return inGrid;
    }
}
