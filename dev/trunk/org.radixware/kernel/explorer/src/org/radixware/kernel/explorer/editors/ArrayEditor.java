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

package org.radixware.kernel.explorer.editors;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.ItemFlags;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.gui.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.*;//NOPMD
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrRef;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.utils.ArrFactory;
import org.radixware.kernel.common.client.views.IPropertyStorePossibility;
import org.radixware.kernel.common.client.views.IPropertyValueStorage;
import org.radixware.kernel.common.client.widgets.IToolButton;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.arreditor.AbstractArrayEditorDelegate;
import org.radixware.kernel.common.client.widgets.arreditor.ArrayItemEditingOptions;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.editors.valeditors.*;//NOPMD
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.types.QtUserData;

public class ArrayEditor extends AbstractArrayEditor implements IPropertyValueStorage {
    
    private static final int UNACCEPTABLE_INPUT_ROLE = Qt.ItemDataRole.UserRole + 1;
    
    private final static class EditItemEvent extends QEvent{
        
        private final int row;
        
        public EditItemEvent(final int row){
            super(QEvent.Type.User);
            this.row = row;
        }
        
        public int getRow(){
            return row;
        }
    }    
    
    private final static class Icons extends ClientIcon {//yremizov

        private Icons(final String fileName) {
            super(fileName, true);
        }
        public static final ClientIcon ADD_EMPTY_ITEM = new Icons("classpath:images/addEmpty.svg");
    }

    private class ArrayTableItem extends QItemDelegate {            

        private ValEditor currentEditor;        

        public boolean isActive() {
            return currentEditor != null;
        }

        @SuppressWarnings("LeakingThisInConstructor")
        public ArrayTableItem() {
            super();
            closeEditor.connect(this, "finishEdit()");
        }

        private void finishEdit() {
            currentEditor = null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public QWidget createEditor(final QWidget parent, final QStyleOptionViewItem option, final QModelIndex index) {
            if (isReadonly()) {
                return null;
            }
            final AbstractArrayEditorDelegate<ValEditor,QWidget> arrItemDelegete = 
                ArrayEditor.this.getFinalEditorDelegate();
            final ValEditor editor = arrItemDelegete.createEditor(parent,                    
                                                                                            ArrayEditor.this.getEnvironment(),
                                                                                            ArrayEditor.this.getItemEditingOptions(),
                                                                                            index.row(),
                                                                                            ArrayEditor.this.getValues(-1));
            editor.changeStateForGrid();
            arrItemDelegete.setValueToEditor(editor, getValue(index));
            if (editor.getLineEdit() != null) {
                editor.setFocus();
                editor.getLineEdit().selectAll();
                editor.getLineEdit().setCursorPosition(0);
            }
            currentEditor = editor;
            IPropertyStorePossibility psp = getPropertyStorePossibility();
            if (psp != null) {
                if (psp.canPropertyReadValue()) {
                    addLoadButton();
                }
                if (psp.canPropertySaveValue()) {
                    addSaveButton();
                }
            }
            if (RadPropertyDef.isPredefinedValuesSupported(arrType.getArrayItemType(), itemEditMask.getType())) {
                currentEditor.setPredefinedValues(ArrayEditor.this.predefinedValues);
            }
            currentEditor.valueChanged.connect(this, "afterEdit()", Qt.ConnectionType.QueuedConnection);
            return editor;
        }
        
        private Object getCurrentEditorValue(){
            return ArrayEditor.this.getFinalEditorDelegate().getValueFromEditor(currentEditor);
        }
        
        private void setCurrentEditorValue(final Object value){
            ArrayEditor.this.getFinalEditorDelegate().setValueToEditor(currentEditor, value);
        }

        private void addLoadButton() {
            if (currentEditor != null) {
                QToolButton loadValueButton = new QToolButton(currentEditor);
                loadValueButton.setToolTip(Application.translate("ArrayEditor", "Load Property Value"));
                loadValueButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.OPEN));
                loadValueButton.clicked.connect(this, "loadPropValue()");
                loadValueButton.setEnabled(!isReadonly());
                loadValueButton.setObjectName("loadValueButton");
                currentEditor.addButton(loadValueButton);
            }
        }

        private void addSaveButton() {
            if (currentEditor != null) {
                QToolButton saveValueButton = new QToolButton(currentEditor);
                saveValueButton.setToolTip(Application.translate("ArrayEditor", "Save Property Value"));
                saveValueButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.SAVE));
                saveValueButton.clicked.connect(this, "savePropValue()");
                saveValueButton.setEnabled(!isReadonly());
                saveValueButton.setObjectName("saveValueButton");
                currentEditor.addButton(saveValueButton);
            }
        }

        @SuppressWarnings({"unused","unchecked"})        
        private void loadPropValue() {
            IPropertyStorePossibility psp = getPropertyStorePossibility();
            if (currentEditor != null && psp != null) {
                setCurrentEditorValue(psp.readPropertyValue(null));
            }
        }

        @SuppressWarnings({"unused","unchecked"})
        private void savePropValue() {
            IPropertyStorePossibility psp = getPropertyStorePossibility();
            if (currentEditor != null && psp != null) {
                Object val = getCurrentEditorValue();
                psp.writePropertyValue(val);
            }
        }

        @SuppressWarnings("unused")
        private void afterEdit() {
            if (currentEditor != null
                    &&/*tool button pressed, but not keyboard input*/ ((getCurrentEditorValue()== null && !currentEditor.hasFocus()) || currentEditor.getLineEdit().isReadOnly())) {
                ArrayEditor.this.closeEditor();
            }
        }       

        @Override
        protected void drawCheck(QPainter painter, QStyleOptionViewItem option, QRect rect, Qt.CheckState state) {
            super.drawCheck(painter, option, rect, state);
            option.setRect(rect);
            TristateCheckBoxStyle.drawCheckBox(painter, option, false, state);
        }

        private Object getValue(final QModelIndex index) {
            final Object value = index.data(Qt.ItemDataRole.UserRole);
            if (arrType == EValType.ARR_BIN || arrType == EValType.ARR_BLOB) {
                return value instanceof String ? new Bin((String) value) : null;
            } else if (arrType == EValType.ARR_NUM && !isAdvancedBooleanEditorUsed()) {
                return value instanceof String ? new BigDecimal((String) value) : null;
            } else if (value instanceof QtUserData) {
                return ((QtUserData) value).get();
            } else {
                return value;
            }
        }
        
        private UnacceptableInput getUnacceptableInput(final QModelIndex index){
            return (UnacceptableInput)index.data(UNACCEPTABLE_INPUT_ROLE);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void setEditorData(final QWidget editor, final QModelIndex index) {            
            if (editor instanceof ValEditor) {
                final ValEditor valEditor = (ValEditor)editor;
                final UnacceptableInput unacceptableInput = getUnacceptableInput(index);
                if (unacceptableInput==null){
                    ArrayEditor.this.getFinalEditorDelegate().setValueToEditor(valEditor, getValue(index));
                }else{
                    final String invalidText = unacceptableInput.getText();
                    if (invalidText!=null && !invalidText.isEmpty()){
                        valEditor.setInputText(invalidText);
                    }
                }
            }else{
                super.setEditorData(editor, index);
            }
        }

        @Override
        public void setModelData(QWidget editor, QAbstractItemModel model, QModelIndex index) {
            if (editor instanceof ValEditor) {
                final ValEditor valEditor = (ValEditor) editor;
                final UnacceptableInput unacceptableInput = valEditor.getUnacceptableInput();
                final AbstractArrayEditorDelegate<ValEditor,QWidget> delegate = ArrayEditor.this.getFinalEditorDelegate();
                Object value = delegate.getValueFromEditor(valEditor);
                if (unacceptableInput==null){
                    final ArrayItemEditingOptions options = ArrayEditor.this.getItemEditingOptions();
                    final IClientEnvironment environment = ArrayEditor.this.getEnvironment();
                    model.setData(index, delegate.getDisplayTextForValue(environment, options, value));
                    model.setData(index, null, Qt.ItemDataRole.ToolTipRole);
                }else{
                    model.setData(index, unacceptableInput.getText());
                    model.setData(index, unacceptableInput.getMessageText(), Qt.ItemDataRole.ToolTipRole);
                }
                
                boolean useAdvancedEditor = isAdvancedBooleanEditorUsed();
                if (useAdvancedEditor) {
                    value = value instanceof Boolean ? (Boolean) value : null;
                } else if (arrType == EValType.ARR_BIN || arrType == EValType.ARR_BLOB) {
                    value = value instanceof Bin ? ((Bin) value).getAsBase64() : null;
                } else if (arrType == EValType.ARR_NUM && !useAdvancedEditor) {
                    value = value instanceof BigDecimal ? ((BigDecimal) value).toString() : null;
                }

                if (value != null && (itemEditMask instanceof EditMaskConstSet) && valClass != null) {
                    final RadEnumPresentationDef.Items items =
                            ((EditMaskConstSet) itemEditMask).getItems(getEnvironment().getApplication());
                    final RadEnumPresentationDef.Item item = items.findItemByValue((Comparable) value);
                    if (item != null) {
                        value = item.getConstant();
                    }
                }
                model.setData(index, value, Qt.ItemDataRole.UserRole);
                model.setData(index, unacceptableInput, UNACCEPTABLE_INPUT_ROLE);
                validateRow(index.row());
                rowEdited.emit(Integer.valueOf(index.row()), value);
            } else {
                super.setModelData(editor, model, index);
            }
            if (currentEditor != null && currentEditor.nativeId() != 0) {
                currentEditor.disconnect();
            }
            currentEditor = null;
            ArrayEditor.this.focusTable();
        }

        public void commit() {
            if (currentEditor != null) {
                currentEditor.disconnect();
                currentEditor.close();
                this.commitData.emit(currentEditor);
                finishEdit();
            }
        }        
    }
        
    private final EValType arrType;
    private final Class<?> valClass;
    private final ArrayTableItem tableItemDelegate;
    private final PropertyArrRef propertyRef;
    private final RadSelectorPresentationDef presentation;    
    private AbstractArrayEditorDelegate<ValEditor,QWidget> arrItemDelegate;
    private AbstractArrayEditorDelegate<ValEditor,QWidget> defaultItemDelegate;
    private List<Object> predefinedValues;
    private QToolButton addNullItemBtn;    
    private EditMask itemEditMask;
    private IPropertyStorePossibility storePossibility;
    public final Signal2<Integer, Object> rowEdited = new Signal2<>();

    public ArrayEditor(final IClientEnvironment environment, final EValType valType,
            final Class<?> valClass,
            QWidget parent) {
        super(environment, parent);
        itemEditMask = EditMask.newInstance(valType);
        arrType = valType.isArrayType() ? valType : valType.getArrayType();
        if (arrType == null) {
            throw new IllegalArgumentError("Can't create array editor for " + valType.getName() + " type");
        }
        this.valClass = valClass;
        tableItemDelegate = new ArrayTableItem();
        setDelegate(tableItemDelegate);
        setItemMoveMode(EItemMoveMode.DRAG_DROP);
        if (arrType == EValType.ARR_REF) {
            addCreateNullItemBtn();
        }
        propertyRef = null;
        presentation = null;
    }

    public ArrayEditor(PropertyArrRef property, QWidget parent) {
        super(property.getEnvironment(), parent,
                property.isReadonly(),
                property.isMandatory(),
                property.getDefinition().isDuplicatesEnabled());
        valClass = null;
        setItemMandatory(property.isArrayItemMandatory());
        arrType = EValType.ARR_REF;
        itemEditMask = EditMask.newCopy(property.getEditMask());
        itemEditMask.setNoValueStr(itemEditMask.getArrayItemNoValueStr(null));
        tableItemDelegate = new ArrayTableItem();
        setDelegate(tableItemDelegate);
        setItemMoveMode(EItemMoveMode.DRAG_DROP);
        setValue(property.getVal());
        addCreateNullItemBtn();
        setFirstArrayItemIndex(property.getFirstArrayItemIndex());
        setMinArrayItemsCount(property.getMinArrayItemsCount());
        setMaxArrayItemsCount(property.getMaxArrayItemsCount());
        propertyRef = property;
        presentation = null;
    }

    public ArrayEditor(final IClientEnvironment environment, final RadSelectorPresentationDef presentation, final QWidget parent) {
        super(environment, parent);
        if (presentation == null) {
            throw new NullPointerException();
        }
        arrType = EValType.ARR_REF;
        valClass = null;
        itemEditMask = EditMask.newInstance(arrType);
        updateItemPrototype();
        tableItemDelegate = new ArrayTableItem();
        setDelegate(tableItemDelegate);
        setItemMoveMode(EItemMoveMode.DRAG_DROP);
        addCreateNullItemBtn();
        propertyRef = null;
        this.presentation = presentation;
    }
    
    protected ArrayItemEditingOptions getItemEditingOptions(){
        if (propertyRef==null){
            if (presentation==null){
                return new ArrayItemEditingOptions(arrType.getArrayItemType(), itemEditMask, isNullItemInadmissible(), isDuplicatesEnabled());
            }else{
                return new ArrayItemEditingOptions(EValType.PARENT_REF, itemEditMask, isNullItemInadmissible(), isDuplicatesEnabled(), presentation);
            }
        }else{
            return new ArrayItemEditingOptions(EValType.PARENT_REF, itemEditMask, isNullItemInadmissible(), isDuplicatesEnabled(), propertyRef);
        }
    }
    
    private AbstractArrayEditorDelegate<ValEditor,QWidget> getFinalEditorDelegate(){
        final AbstractArrayEditorDelegate<ValEditor,QWidget> delegate = getEditorDelegate();
        if (delegate==null){
            if (defaultItemDelegate==null){
                if (arrType==EValType.ARR_REF){
                    defaultItemDelegate= new ArrayRefEditorDelegate();
                }else{
                    defaultItemDelegate= new ArrayEditorDelegate();
                }                                
            }
            return defaultItemDelegate;
        }else{
            return delegate;
        }
    }
    
    public AbstractArrayEditorDelegate<ValEditor, QWidget> getEditorDelegate(){
        return arrItemDelegate;
    }
    
    public void setEditorDelegate(final AbstractArrayEditorDelegate<ValEditor, QWidget> delegate){
        arrItemDelegate = delegate;
    }

    public int getCurrentIndex() {
        return getCurrentRow();
    }
    
    public void setCurrentIndex(final int index) {
        setCurrentRow(index);
    }    

    private void addCreateNullItemBtn() {
        addNullItemBtn = new QToolButton(this);
        insertToolButton(addNullItemBtn, 1);
        addNullItemBtn.setObjectName("addNullItemBtn");
        addNullItemBtn.setIcon(ExplorerIcon.getQIcon(Icons.ADD_EMPTY_ITEM));
        addNullItemBtn.setToolTip(Application.translate("ArrayEditor", "Insert Empty Item"));
        addNullItemBtn.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.TabFocus);
        QWidget.setTabOrder(getToolButtonForAction(addAction), addNullItemBtn);
        addNullItemBtn.clicked.connect(this, "onCreateNullItem()");
    }

    @SuppressWarnings("unused")
    private void onCreateNullItem() {
        final List<QTableWidgetItem> newItems = new ArrayList<>(1);
        newItems.add(createItemForValue(null));
        insertNewItems(newItems);
    }

    @Override
    protected void afterRefreshUi() {
        if (addNullItemBtn != null) {
            addNullItemBtn.setEnabled(!isNullItemInadmissible() && !isReadonly());
        }
    }

    public void setEditMask(EditMask editMask) {
        tableItemDelegate.commit();
        itemEditMask = EditMask.newCopy(editMask);
        itemEditMask.setNoValueStr(itemEditMask.getArrayItemNoValueStr(null));
        final ArrayList array = getValue();
        if (array != null && !array.isEmpty()) {            
            Object itemValue;
            final AbstractArrayEditorDelegate<ValEditor,QWidget> delegate = getFinalEditorDelegate();
            final ArrayItemEditingOptions options = getItemEditingOptions();
            final IClientEnvironment environment = getEnvironment();                    
            for (int i = 0; i < array.size(); i++) {
                itemValue = array.get(i);
                getItem(i).setText(delegate.getDisplayTextForValue(environment, options, itemValue));
            }
        }
        setNoValueStr(editMask.getNoValueStr(null));
    }

    @Override
    public void setPropertyStorePossibility(IPropertyStorePossibility isp) {
        this.storePossibility = isp;
    }

    @Override
    public IPropertyStorePossibility getPropertyStorePossibility() {
        return this.storePossibility;
    }

    public EditMask getEditMask() {
        return EditMask.newCopy(itemEditMask);
    }

    public void setCurrentValue(Arr value) {
        setValue(value);
    }

    @SuppressWarnings({"unchecked", "UseSpecificCatch"})
    public Arr getCurrentValue() {
        if (currentValueIsNull()) {
            return null;
        }

        Arr value = null;
        if (valClass != null) {//enums
            try {
                value = (Arr) valClass.newInstance();
            } catch (Exception exception) {
                getEnvironment().processException(exception);
            }
        } else {
            value = createEmptyArr(arrType);
        }
        if (inEditState()){
            closeEditor();
        }
        final ArrayList array = getValue();

        for (Object item : array) {
            value.add(item);
        }
        return value;
    }

    public ValidationResult validateCurrentValue() {
        if (isLengthInRange()) {
            final Arr currentValue = getCurrentValue();
            if (currentValue != null) {
                ValidationResult validation;
                for (int row = 0, size = currentValue.size(); row < size; row++) {
                    validation = validateRow(row);
                    if (validation != ValidationResult.ACCEPTABLE) {
                        return validation;
                    }
                }
            }
            return ValidationResult.ACCEPTABLE;
        } else {
            return ValidationResult.INVALID;
        }
    }

    public ValidationResult validateCurrentValueSize() {
        return isLengthInRange() ? ValidationResult.ACCEPTABLE : ValidationResult.INVALID;
    }

    public static Arr createEmptyArr(EValType type) {
        return ArrFactory.DEFAULT.createArray(type);
    }

    @Override
    protected QTableWidgetItem createItemForValue(final Object itemValue) {
        final Object value;
        if (arrType == EValType.ARR_BIN || arrType == EValType.ARR_BLOB) {
            value = itemValue instanceof Bin ? ((Bin) itemValue).getAsBase64() : null;
        } else if (arrType == EValType.ARR_NUM && !isAdvancedBooleanEditorUsed()) {
            value = itemValue instanceof BigDecimal ? ((BigDecimal) itemValue).toString() : null;
        } else if (itemValue instanceof IKernelEnum) {
            value = new QtUserData(itemValue);
        } else {
            value = itemValue;
        }
        final QTableWidgetItem item = super.createItemForValue(value);

        if (isAdvancedBooleanEditorUsed()) {
            if (itemValue == null) {
                item.setCheckState(Qt.CheckState.PartiallyChecked);
            } else {
                //final boolean checkState = (Boolean) itemValue;
                Boolean state = null;
                if (itemValue.equals(((EditMaskBool) itemEditMask).getTrueValue())) {
                    state = true;
                } else if (itemValue.equals(((EditMaskBool) itemEditMask).getFalseValue())) {
                    state = false;
                } else {
                    throw new IllegalArgumentException("Value " + itemValue + " does not match any value in EditMaskBool.");
                }
                item.setCheckState(state ? Qt.CheckState.Checked : Qt.CheckState.Unchecked);
            }
        }
        
        item.setText(getFinalEditorDelegate().getDisplayTextForValue(getEnvironment(), getItemEditingOptions(), itemValue));

        if (value instanceof Reference && ((Reference) value).isBroken()) {
            item.setForeground(new QBrush(QColor.red));
        }
        validateItem(item, null, itemValue);
        return item;
    }

    private ValidationResult validateItem(final QTableWidgetItem item, final UnacceptableInput unacceptableInput, final Object value) {
        final ExplorerTextOptions textOptions;        
        ValidationResult result;
        if (unacceptableInput==null){
            if ((value instanceof Reference) && ((Reference) value).isBroken()) {
                result = ValidationResult.INVALID;
            } else {
                result = itemEditMask.validate(getEnvironment(), value);
            }            
        }else{
            result = ValidationResult.Factory.newInvalidResult(unacceptableInput.getReason());
        }
        
        if (result == ValidationResult.ACCEPTABLE && (value == null && isItemMandatory())) {
            result = ValidationResult.INVALID;
        }
        if (result == ValidationResult.ACCEPTABLE) {
            textOptions = ExplorerTextOptions.getDefault();
        } else {
            textOptions = ExplorerTextOptions.Factory.getOptions(ETextOptionsMarker.INVALID_VALUE);
        }
        item.setFont(textOptions.getQFont());
        item.setForeground(new QBrush(textOptions.getForeground()));
        item.setBackground(new QBrush(textOptions.getBackground()));
        return result;
    }

    private ValidationResult validateRow(final int row) {
        final QTableWidgetItem item = getItem(row);
        final Object value = getValueForItem(item);
        return validateItem(item, getUnacceptableInputForItem(item), value);
    }
    
    private UnacceptableInput getUnacceptableInputForItem(final QTableWidgetItem item){
        return (UnacceptableInput)item.data(UNACCEPTABLE_INPUT_ROLE);
    }

    @Override
    protected Object getValueForItem(final QTableWidgetItem item) {
        Object value = super.getValueForItem(item);
        boolean useAdvancedEditor = isAdvancedBooleanEditorUsed();
        if (useAdvancedEditor) {
            if (value instanceof Boolean) {
                return value.equals(Boolean.TRUE) ? ((EditMaskBool) itemEditMask).getTrueValue() : ((EditMaskBool) itemEditMask).getFalseValue();
            } else {
                return value;
            }
        } else if (arrType == EValType.ARR_BIN || arrType == EValType.ARR_BLOB) {
            value = value instanceof String ? new Bin((String) value) : null;
        } else if (arrType == EValType.ARR_NUM && !useAdvancedEditor) {
            value = value instanceof String ? new BigDecimal((String) value) : null;
        }
        return value;
    }

    @Override
    protected ItemFlags getItemFlags() {
        final Qt.ItemFlags flags = super.getItemFlags();
        if (isAdvancedBooleanEditorUsed()) {
            flags.set(Qt.ItemFlag.ItemIsUserCheckable);
        }
        if (!isReadonly()) {
            if (isAdvancedBooleanEditorUsed()) {
                if (!isNullItemInadmissible()) {
                    flags.set(Qt.ItemFlag.ItemIsTristate);
                }
            } else {
                flags.set(Qt.ItemFlag.ItemIsEditable);
            }
        }
        return flags;
    }

    @Override
    protected List<QTableWidgetItem> createNewItems() {
        final List<QTableWidgetItem> newItems = new ArrayList<>(1);
        final List<Object> newValues = 
            getFinalEditorDelegate().createNewValues(this, getEnvironment(), getItemEditingOptions(), getValues(-1));
        if (newValues!=null){
            for (Object val: newValues){
                newItems.add(createItemForValue(val));
            }
        }
        return newItems;
    }

    @Override
    protected void finishEdit() {
        if (tableItemDelegate != null) {
            tableItemDelegate.commit();
        }
        super.finishEdit();
    }

    @Override
    protected boolean inEditState() {
        return tableItemDelegate.isActive() || super.inEditState();
    }

    @Override
    protected boolean onKeyEvent(final QKeyEvent keyEvent) {
        final boolean isCtrl = keyEvent.modifiers().value() == KeyboardModifier.ControlModifier.value()
                               || (keyEvent.modifiers().value() == KeyboardModifier.MetaModifier.value() && SystemTools.isOSX);
        if (keyEvent.key() == Qt.Key.Key_Space.value()
                && isCtrl
                && getValue().size() > 0 && isAdvancedBooleanEditorUsed()) {
            final QTableWidgetItem item = getCurrentItem();
            if (!isNullItemInadmissible() && item != null) {
                item.setCheckState(Qt.CheckState.PartiallyChecked);
                item.setData(Qt.ItemDataRole.UserRole, null);
                item.setText(getFinalEditorDelegate().getDisplayTextForValue(getEnvironment(), getItemEditingOptions(), null));
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected void customEvent(QEvent event) {
        if (event instanceof EditItemEvent){
            event.accept();
            final int row = ((EditItemEvent)event).getRow();
            openEditor(row);
        }else{
            super.customEvent(event);
        }
    }    

    @Override
    protected void onDataChanged(final QTableWidgetItem item) {
        if (isAdvancedBooleanEditorUsed()) {
            final Object previousVal = getValueForItem(item);
            final Boolean currentVal;
            if (item.checkState() == Qt.CheckState.Checked) {
                if (previousVal != null && previousVal.equals(((EditMaskBool) itemEditMask).getFalseValue()) && !isNullItemInadmissible()) {
                    item.setCheckState(Qt.CheckState.PartiallyChecked);
                    currentVal = null;                    
                } else {
                    currentVal = Boolean.TRUE;
                }
            } else if (item.checkState() == Qt.CheckState.Unchecked) {
                currentVal = Boolean.FALSE;                
            } else {
                currentVal = null;                
            }
            item.setData(Qt.ItemDataRole.UserRole, currentVal);
            item.setText(getFinalEditorDelegate().getDisplayTextForValue(getEnvironment(), getItemEditingOptions(), currentVal));
            validateRow(item.row());
            rowEdited.emit(Integer.valueOf(item.row()), currentVal);            
        }
    }

    public void setEditingHistory(final IEditingHistory history) {
//        delegate.closeEditor
    }

    public void setPredefinedValues(final List<Object> values) {
        predefinedValues = values == null ? Collections.emptyList() : new ArrayList<>(values);
    }
    
    public void addCustomAction(final Action action){        
        insertCustomAction((QAction)action);
    }
    
    public void removeCustomAction(final Action action){
        removeCustomAction((QAction)action);
    }

    private boolean isAdvancedBooleanEditorUsed() {
        return (itemEditMask != null && itemEditMask instanceof EditMaskBool && (arrType == EValType.ARR_BOOL || arrType == EValType.ARR_INT || arrType == EValType.ARR_NUM || arrType == EValType.ARR_STR));
    }

    private boolean isLengthInRange() {
        if (currentValueIsNull()) {
            return true;//Значение null проверяется отдельно
        } else {
            if (getMaxArrayItemsCount() >= 0 && length() > getMaxArrayItemsCount()) {
                return false;
            }
            if (getMinArrayItemsCount() > 0 && length() < getMinArrayItemsCount()) {
                return false;
            }
            return true;
        }
    }
    
    public boolean checkItems(){
        final Arr currentValue = getCurrentValue();
        if (currentValue != null) {
            final boolean isItemMandatory = isItemMandatory();
            final boolean isStringType = currentValue instanceof ArrStr && getEditMask() instanceof EditMaskStr;
            final boolean isEmptyStrAllowed = isStringType ? ((EditMaskStr)getEditMask()).isEmptyStringAllowed() : false;
            for (int row = 0, size = currentValue.size(); row < size; row++) {
                final Object arrItem = currentValue.get(row);
                if (arrItem==null && isItemMandatory
                    || (isStringType && !isEmptyStrAllowed && arrItem!=null && ((String)arrItem).isEmpty())){
                    final String message = 
                        getEnvironment().getMessageProvider().translate("ArrayEditor", "Value at row %1$s was not defined");
                    showInvalidValueMessage(message, row, null);                    
                    return false;
                }
                final ValidationResult validation = validateRow(row);
                if (validation != ValidationResult.ACCEPTABLE) {//NOPMD
                    final String message = 
                        getEnvironment().getMessageProvider().translate("ArrayEditor", "Value at row %1$s is invalid.\n%2$s");
                    final String reason = 
                        validation.getInvalidValueReason().getMessage(getEnvironment().getMessageProvider(), InvalidValueReason.EMessageType.Value);
                    showInvalidValueMessage(message, row, reason);
                    return false;
                }
            }
        }
        return true;
    }
    
    private void showInvalidValueMessage(final String messageTemplate, final int row, final String reason){        
        final String title = getEnvironment().getMessageProvider().translate("ArrayEditor", "Value is Invalid");
        final int rowIndex = row + getFirstArrayItemIndex();
        final String message;
        if (reason==null || reason.isEmpty()){
            message = String.format(messageTemplate, String.valueOf(rowIndex));
        }else{
            message = String.format(messageTemplate, String.valueOf(rowIndex), reason);
        }        
        getEnvironment().messageError(title, String.format(message, String.valueOf(rowIndex), reason));
        QApplication.postEvent(this, new EditItemEvent(row));
    }
}