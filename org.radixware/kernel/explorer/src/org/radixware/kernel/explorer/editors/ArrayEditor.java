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
import com.trolltech.qt.gui.QDialog.DialogCode;
import com.trolltech.qt.gui.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.*;//NOPMD
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.EntityObjectsSelection;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrRef;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.ChoosableEntitiesFilter;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.views.IPropertyStorePossibility;
import org.radixware.kernel.common.client.views.IPropertyValueStorage;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ESelectionMode;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrBin;
import org.radixware.kernel.common.types.ArrBool;
import org.radixware.kernel.common.types.ArrChar;
import org.radixware.kernel.common.types.ArrDateTime;
import org.radixware.kernel.common.types.ArrInt;
import org.radixware.kernel.common.types.ArrNum;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.dialogs.SelectEntitiesDialog;
import org.radixware.kernel.explorer.dialogs.SelectEntityDialog;
import org.radixware.kernel.explorer.editors.valeditors.*;//NOPMD
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.types.QtUserData;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public final class ArrayEditor extends AbstractArrayEditor implements IPropertyValueStorage {
    
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

    private final EValType arrType;
    private final Class<?> valClass;
    private final ArrayTableItem delegate;
    private QToolButton addNullItemBtn;
    private EditMask itemEditMask;
    public final Signal2<Integer, Object> rowEdited = new Signal2<>();
    private IPropertyStorePossibility storePossibility;
    private QModelIndex indexModel;
    
    private final static class Icons extends ClientIcon {//yremizov

        private Icons(final String fileName) {
            super(fileName, true);
        }
        public static final ClientIcon ADD_EMPTY_ITEM = new Icons("classpath:images/addEmpty.svg");
    }

    private final static class ValRefEditor extends ValEditor<Reference> {

        public ValRefEditor(IClientEnvironment environment, final QWidget parent, final EditMask editMask, final boolean isMandatory, final boolean isReadonly) {
            super(environment, parent, editMask, isMandatory, isReadonly);
            getLineEdit().setReadOnly(true);
        }
    }

    private class ArrayTableItem extends QItemDelegate {            

        private ValEditor currentEditor;
        private GroupModel group = null;
        private final PropertyArrRef propertyRef;
        private final RadSelectorPresentationDef presentation;

        public boolean isActive() {
            return currentEditor != null;
        }

        @SuppressWarnings("LeakingThisInConstructor")
        public ArrayTableItem(final PropertyArrRef property) {
            super();
            propertyRef = property;
            presentation = null;
            closeEditor.connect(this, "finishEdit()");
        }

        @SuppressWarnings("LeakingThisInConstructor")
        public ArrayTableItem(final RadSelectorPresentationDef pres) {
            super();
            propertyRef = null;
            presentation = pres;
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
            indexModel = index;
            final ValEditor editor;
            if (itemEditMask instanceof EditMaskConstSet) {
                EditMaskConstSet mask = (EditMaskConstSet) itemEditMask;
                if (!isDuplicatesEnabled()) {
                    mask =
                        excludeExistingItems(getEnvironment().getApplication(), mask, getValues(index.row()));
                }
                editor = new ValConstSetEditor(getEnvironment(), parent, mask, isNullItemInadmissible(), isReadonly());
            } else {
                switch (arrType) {
                    case ARR_STR:
                    case ARR_CHAR:
                        if (itemEditMask instanceof EditMaskStr) {
                            if (arrType == EValType.ARR_STR) {
                                editor = new ValStrEditor(getEnvironment(), parent, (EditMaskStr) itemEditMask, isNullItemInadmissible(), isReadonly());
                            } else {
                                editor = new ValCharEditor(getEnvironment(), parent, (EditMaskStr) itemEditMask, isNullItemInadmissible(), isReadonly());
                            }
                        } else if (itemEditMask instanceof EditMaskList) {
                            editor = new ValListEditor(getEnvironment(), parent, (EditMaskList) itemEditMask, isNullItemInadmissible(), isReadonly());
                        } else if (itemEditMask instanceof EditMaskBool) {
                            editor = new AdvancedValBoolEditor(getEnvironment(), parent, (EditMaskBool) itemEditMask, isNullItemInadmissible(), isReadonly());
                        } else if (itemEditMask instanceof EditMaskFilePath) {
                            editor = new ValFilePathEditor(getEnvironment(), parent, (EditMaskFilePath) itemEditMask, isNullItemInadmissible(), isReadonly());
                        } else {
                            throw new IllegalUsageError("Edit mask \'" + itemEditMask.getClass().getName() + "\' is not applicable for \'" + arrType.toString() + "\' type");
                        }

                        break;
                    case ARR_CLOB:
                        if (itemEditMask instanceof EditMaskStr) {
                            editor = new ValStrEditor(getEnvironment(), parent, (EditMaskStr) itemEditMask, isNullItemInadmissible(), isReadonly());
                        } else {
                            throw new IllegalUsageError("Edit mask \'" + itemEditMask.getClass().getName() + "\' is not applicable for \'" + arrType.toString() + "\' type");
                        }
                        break;
                    case ARR_BIN:
                    case ARR_BLOB:
                        editor = new ValBinEditor(getEnvironment(), parent, (EditMaskNone) itemEditMask, isNullItemInadmissible(), isReadonly());
                        break;
                    case ARR_INT:
                        if (itemEditMask instanceof EditMaskInt) {
                            editor = new ValIntEditor(getEnvironment(), parent, (EditMaskInt) itemEditMask, isNullItemInadmissible(), isReadonly());
                        } else if (itemEditMask instanceof EditMaskList) {
                            editor = new ValListEditor(getEnvironment(), parent, (EditMaskList) itemEditMask, isNullItemInadmissible(), isReadonly());
                        } else if (itemEditMask instanceof EditMaskTimeInterval) {
                            editor = new ValTimeIntervalEditor(getEnvironment(), parent, (EditMaskTimeInterval) itemEditMask, isNullItemInadmissible(), isReadonly());
                        } else if (itemEditMask instanceof EditMaskBool) {
                            editor = new AdvancedValBoolEditor(getEnvironment(), parent, (EditMaskBool) itemEditMask, isNullItemInadmissible(), isReadonly());
                        } else {
                            throw new IllegalUsageError("Edit mask \'" + itemEditMask.getClass().getName() + "\' is not applicable for \'" + arrType.toString() + "\' type");
                        }
                        break;
                    case ARR_DATE_TIME:
                        if (itemEditMask instanceof EditMaskDateTime) {
                            editor = new ValDateTimeEditor(getEnvironment(), parent, (EditMaskDateTime) itemEditMask, isNullItemInadmissible(), isReadonly());
                        } else if (itemEditMask instanceof EditMaskTimeInterval) {
                            editor = new ValTimeIntervalEditor(getEnvironment(), parent, (EditMaskTimeInterval) itemEditMask, isNullItemInadmissible(), isReadonly());
                        } else {
                            throw new IllegalUsageError("Edit mask \'" + itemEditMask.getClass().getName() + "\' is not applicable for \'" + arrType.toString() + "\' type");
                        }
                        break;
                    case ARR_NUM:
                        if (itemEditMask instanceof EditMaskNum) {
                            editor = new ValNumEditor(getEnvironment(), parent, (EditMaskNum) itemEditMask, isNullItemInadmissible(), isReadonly());
                        } else if (itemEditMask instanceof EditMaskBool) {
                            editor = new AdvancedValBoolEditor(getEnvironment(), parent, (EditMaskBool) itemEditMask, isNullItemInadmissible(), isReadonly());
                        } else {
                            throw new IllegalUsageError("Edit mask \'" + itemEditMask.getClass().getName() + "\' is not applicable for \'" + arrType.toString() + "\' type");
                        }
                        break;
                    case ARR_BOOL:
                        if (itemEditMask instanceof EditMaskBool) {
                            editor = new AdvancedValBoolEditor(getEnvironment(), parent, (EditMaskBool) itemEditMask, isNullItemInadmissible(), isReadonly());
                        } else {
                            throw new IllegalUsageError("Edit mask \'" + itemEditMask.getClass().getName() + "\' is not applicable for \'" + arrType.toString() + "\' type");
                        }
                        break;
                    case ARR_REF: {
                        editor = new ValRefEditor(getEnvironment(), parent, getEditMask(), isNullItemInadmissible(), isReadonly());
                        final QAction action = new QAction(this);
                        action.setToolTip(Application.translate("ArrayEditor", "Select Object"));
                        action.setIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.SELECTOR));
                        action.triggered.connect(this, "selectEntity()");
                        editor.addButton(null, action);
                        break;
                    }
                    default:
                        throw new IllegalUsageError("type \'" + arrType.toString() + "\' is not supported");
                }
            }
            editor.changeStateForGrid();
            editor.setValue(getValue(index));
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
                currentEditor.setValue(psp.readPropertyValue(null));
            }
        }

        @SuppressWarnings({"unused","unchecked"})
        private void savePropValue() {
            IPropertyStorePossibility psp = getPropertyStorePossibility();
            if (currentEditor != null && psp != null) {
                Object val = currentEditor.getValue();
                psp.writePropertyValue(val);
            }
        }

        @SuppressWarnings("unused")
        private void afterEdit() {
            if (currentEditor != null
                    &&/*tool button pressed, but not keyboard input*/ ((currentEditor.getValue() == null && !currentEditor.hasFocus()) || currentEditor.getLineEdit().isReadOnly())) {
                ArrayEditor.this.closeEditor();
            }
        }

        @SuppressWarnings("unchecked")
        public GroupModel getGroupModel(final boolean excludeCurrent) {
            if (propertyRef == null && presentation == null) {
                throw new IllegalUsageError("Selector presentation not defined");
            }
            if (group == null) {
                try {
                    if (propertyRef != null) {
                        group = propertyRef.openGroupModel();
                    } else {
                        final Model holderModel = WidgetUtils.findNearestModel(ArrayEditor.this);
                        if (holderModel==null){
                            group = GroupModel.openTableContextlessSelectorModel(getEnvironment(), presentation);                            
                        }else{
                            group = GroupModel.openTableContextlessSelectorModel(holderModel, presentation);
                        }
                    }
                } catch (RuntimeException ex) {
                    processException(ex,false);
                    return null;
                }
            } else if (propertyRef != null) {
                try {
                    group.reset();
                    group.setCondition(propertyRef.getCondition());
                    final Map<Id, Object> propertyValues = propertyRef.getGroupPropertyValues();
                    for (Map.Entry<Id, Object> propertyValue : propertyValues.entrySet()) {
                        group.getProperty(propertyValue.getKey()).setValueObject(propertyValue.getValue());
                    }
                } catch (ServiceClientException ex) {//Group is empty - never thrown
                    processException(ex,false);
                    return null;
                } catch (InterruptedException ex) {//Group is empty - never thrown
                    return null;
                }
            }

            if (!isDuplicatesEnabled()) {
                final List<Object> values = getValues(-1);
                final ChoosableEntitiesFilter filter = new ChoosableEntitiesFilter();
                if (currentEditor != null && !excludeCurrent) {
                    if (getCurrentItem() != null) {
                        values.remove(getValueForItem(getCurrentItem()));
                    } else {
                        values.remove(currentEditor.getValue());
                    }
                }
                for (Object value : values) {
                    if (value != null) {
                        filter.add(((Reference) value).getPid());
                    }
                }
                group.setEntitySelectionController(filter);
            }
            return group;
        }

        public boolean canSelectNoEntity() {
            return !isNullItemInadmissible();
        }

        public void processException(final Throwable ex, final boolean calcTitle) {
            final String err_title;
            final String err_msg;
            if (calcTitle){
                err_title = Application.translate("ExplorerException", "Error on set value");
                err_msg = Application.translate("ExplorerException", "Failed to set value of \'%s\':\n%s");
            }else{
                err_title = Application.translate("ExplorerException", "Error on opening selector");
                err_msg = Application.translate("ExplorerException", "Can't open selector for \'%s\':\n%s");
            }
            final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex);
            final String trace = reason + ":\n" + ClientException.exceptionStackToString(ex);
            final String title = propertyRef != null ? propertyRef.getTitle() : presentation.getTitle();
            Application.messageError(err_title, String.format(err_msg, title, reason));
            getEnvironment().getTracer().error(String.format(err_msg, title, reason));
            getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(err_msg, title, trace),
                    EEventSource.EXPLORER);
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
                    valEditor.setValue(getValue(index));
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
                Object value = valEditor.getValue();                
                if (unacceptableInput==null){
                    model.setData(index, valEditor.getEditMask().toStr(getEnvironment(), value));
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

        @SuppressWarnings("unused")
        private void selectEntity() {
            final GroupModel groupModel = delegate.getGroupModel(false);
            if (groupModel != null) {
                try {

                    final SelectEntityDialog dialog = new SelectEntityDialog(groupModel, canSelectNoEntity());
                    if (DialogCode.resolve(dialog.exec()).equals(DialogCode.Rejected)) {
                        return;
                    }
                    final EntityModel entity = dialog.selectedEntity;
                    Reference newValue = entity == null ? null : new Reference(entity);
                    if (newValue!=null){
                        final ArrRef arrRefs;
                        try{
                            arrRefs = calcTitles(new ArrRef(newValue));
                        }catch(InterruptedException exception){
                            return;
                        }catch(ServiceClientException exception){
                            processException(exception,true);
                            return;
                        }
                        if (arrRefs!=null && arrRefs.size()==1){
                            newValue = arrRefs.get(0);
                        }else{
                            return;
                        }                                
                    }
                    
                    if (currentEditor == null || currentEditor.nativeId() == 0) {
                        final QTableWidgetItem currentItem = getCurrentItem();
                        if (currentItem != null) {
                            currentItem.setText(itemEditMask.toStr(getEnvironment(), newValue));
                            currentItem.setData(Qt.ItemDataRole.UserRole, newValue);
                            commit();
                        }
                    } else {
                        ((ValRefEditor) currentEditor).setValue(newValue);
                    }
                } catch (RuntimeException ex) {
                    processException(ex,false);
                } finally {
                    groupModel.clean();
                }
            }
        }

        public void commit() {
            if (currentEditor != null) {
                currentEditor.disconnect();
                currentEditor.close();
                this.commitData.emit(currentEditor);
                finishEdit();
            }
        }
        
        public ArrRef calcTitles(final ArrRef arrRefs) throws InterruptedException, ServiceClientException{
            if (arrRefs==null 
                || arrRefs.isEmpty() 
                || propertyRef==null 
                || propertyRef.createContext() instanceof IContext.ContextlessSelect){
                return arrRefs;
            }
            return propertyRef.updateTitles(arrRefs);
        }
    }
    
    private List<Object> predefinedValues;

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
        delegate = new ArrayTableItem((PropertyArrRef) null);
        setDelegate(delegate);
        setItemMoveMode(EItemMoveMode.DRAG_DROP);
        if (arrType == EValType.ARR_REF) {
            addCreateNullItemBtn();
        }
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
        delegate = new ArrayTableItem(property);
        setDelegate(delegate);
        setItemMoveMode(EItemMoveMode.DRAG_DROP);
        setValue(property.getVal());
        addCreateNullItemBtn();
        setFirstArrayItemIndex(property.getFirstArrayItemIndex());
        setMinArrayItemsCount(property.getMinArrayItemsCount());
        setMaxArrayItemsCount(property.getMaxArrayItemsCount());
    }

    public ArrayEditor(final IClientEnvironment environment, final RadSelectorPresentationDef presentation,
            final QWidget parent) {
        super(environment, parent);
        if (presentation == null) {
            throw new NullPointerException();
        }
        arrType = EValType.ARR_REF;
        valClass = null;
        itemEditMask = EditMask.newInstance(arrType);
        updateItemPrototype();
        delegate = new ArrayTableItem(presentation);
        setDelegate(delegate);
        setItemMoveMode(EItemMoveMode.DRAG_DROP);
        addCreateNullItemBtn();
    }

    public int getCurrentIndex() {
        if (indexModel!=null){
            return indexModel.row();
        }
        return 0;
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
        delegate.commit();
        itemEditMask = EditMask.newCopy(editMask);
        itemEditMask.setNoValueStr(itemEditMask.getArrayItemNoValueStr(null));
        final ArrayList array = getValue();
        if (array != null && !array.isEmpty()) {
            Object itemValue;
            for (int i = 0; i < array.size(); i++) {
                itemValue = array.get(i);
                getItem(i).setText(itemEditMask.toStr(getEnvironment(), itemValue));
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
        switch (type) {
            case ARR_BIN:
            case BIN:
            case ARR_BLOB:
            case BLOB:
                return new ArrBin();
            case ARR_BOOL:
            case BOOL:
                return new ArrBool();
            case ARR_CHAR:
            case CHAR:
                return new ArrChar();
            case ARR_DATE_TIME:
            case DATE_TIME:
                return new ArrDateTime();
            case ARR_INT:
            case INT:
                return new ArrInt();
            case ARR_NUM:
            case NUM:
                return new ArrNum();
            case ARR_REF:
            case PARENT_REF:
                return new ArrRef();
            case ARR_STR:
            case STR:
            case ARR_CLOB:
            case CLOB:
                return new ArrStr();
            default:
                throw new IllegalUsageError("Can't create array of \'" + type.getName() + "\' type");
        }
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
                item.setText(itemEditMask.getArrayItemNoValueStr(Application.getInstance().getMessageProvider()));
            } else {
                //final boolean checkState = (Boolean) itemValue;
                Boolean state = null;
                if (itemValue.equals(((EditMaskBool) itemEditMask).getTrueValue())) {
                    state = true;
                    item.setText(((EditMaskBool) itemEditMask).getTrueTitle(getEnvironment().getDefManager()));
                } else if (itemValue.equals(((EditMaskBool) itemEditMask).getFalseValue())) {
                    state = false;
                    item.setText(((EditMaskBool) itemEditMask).getFalseTitle(getEnvironment().getDefManager()));
                } else {
                    throw new IllegalArgumentException("Value " + itemValue + " does not match any value in EditMaskBool.");
                }
                item.setCheckState(state ? Qt.CheckState.Checked : Qt.CheckState.Unchecked);
            }
        } else {
            item.setText(itemEditMask.toStr(getEnvironment(), itemValue));
        }

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
            if (value != null) {
                if (value instanceof Boolean) {
                    Object val = value.equals(Boolean.TRUE) ? ((EditMaskBool) itemEditMask).getTrueValue() : ((EditMaskBool) itemEditMask).getFalseValue();
                    //item.setText(((EditMaskBool)itemEditMask).toStr(getEnvironment(), val));
                    if (value.equals(Boolean.TRUE)) {
                        item.setText(((EditMaskBool) itemEditMask).getTrueTitle(getEnvironment().getDefManager()));
                    } else {
                        item.setText(((EditMaskBool) itemEditMask).getFalseTitle(getEnvironment().getDefManager()));
                    }

                    return val;
                } else {

                    return value;
                }
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
        if (arrType == EValType.ARR_REF) {
            final GroupModel group = delegate.getGroupModel(true);
            if (group != null) {
                try {
                    final SelectEntitiesDialog dialog = new SelectEntitiesDialog(group, delegate.canSelectNoEntity());
                    if (!DialogCode.resolve(dialog.exec()).equals(DialogCode.Rejected)) {
                        if (dialog.clearButtonWasClicked()){
                            newItems.add(createItemForValue(null));
                        }else{
                            final List<EntityModel> selection = dialog.getSelectedEntities();
                            ArrRef arrRefs = new ArrRef();
                            for (EntityModel selectedObject: selection){
                                arrRefs.add(new Reference(selectedObject));
                            }
                            try{
                               arrRefs = delegate.calcTitles(arrRefs);
                            }catch(InterruptedException exception){
                                return newItems;
                            }catch(ServiceClientException exception){
                                delegate.processException(exception,true);
                                return newItems;
                            }
                            for (Reference ref: arrRefs){
                                if (ref!=null && ref.isValid()){
                                    newItems.add(createItemForValue(ref));
                                }
                            }
                        }
                    }
                } catch (RuntimeException ex) {
                    delegate.processException(ex,false);
                    return null;
                } finally {
                    group.clean();
                }
            }
        } else {
            final Object itemValue = isNullItemInadmissible() ? getDefaultItemValue(getEnvironment(), arrType, itemEditMask) : null;
            newItems.add(createItemForValue(itemValue));
        }
        return newItems;
    }

    private static EditMaskConstSet excludeExistingItems(final IClientApplication application, final EditMaskConstSet sourceMask, final List<Object> itemValues) {
        final EditMaskConstSet resultMask = (EditMaskConstSet) EditMask.newCopy(sourceMask);
        final EValType enumType = resultMask.getRadEnumPresentationDef(application).getItemType();
        final RadEnumPresentationDef.Items excludedItems = resultMask.getExcludedItems(application);
        ValAsStr valAsStr;
        RadEnumPresentationDef.Item item;
        for (Object itemValue : itemValues) {
            valAsStr = ValAsStr.Factory.newInstance(itemValue, enumType);
            item = resultMask.getRadEnumPresentationDef(application).getItems().findItemByValue(valAsStr);
            if (item != null) {
                excludedItems.addItem(item);
            }
        }
        resultMask.setItems(sourceMask.getItems(application));
        resultMask.setExcludedItems(excludedItems);
        return resultMask;
    }

    @SuppressWarnings("PMD.MissingBreakInSwitch")
    private Object getDefaultItemValue(IClientEnvironment environment, final EValType type, final EditMask editMask) {
        switch (type) {
            case ARR_STR:
            case ARR_CHAR:
                if (editMask instanceof EditMaskStr) {
                    return type == EValType.ARR_STR ? "" : Character.valueOf(' ');
                } else if (editMask instanceof EditMaskList) {
                    final EditMaskList mask = (EditMaskList) editMask;
                    if (mask.getItems().size() > 0) {
                        return mask.getItems().get(0).getValue();
                    } else {
                        return null;
                    }
                } else if (editMask instanceof EditMaskConstSet) {
                    EditMaskConstSet mask = (EditMaskConstSet) editMask;
                    if (!isDuplicatesEnabled()) {
                        mask = excludeExistingItems(environment.getApplication(), mask, getValues(-1));
                    }
                    if (mask.getItems(environment.getApplication()).size() > 0) {
                        return mask.getItems(environment.getApplication()).getItem(0).getValue();
                    } else {
                        return null;
                    }
                } else if (editMask instanceof EditMaskBool) {
                    return null;
                } else if (editMask instanceof EditMaskFilePath) {
                    /*File[] roots = File.listRoots();
                     return roots[0].getAbsolutePath();*/
                    return null;
                } else {
                    throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
                }
            case ARR_CLOB:
                if (editMask instanceof EditMaskStr) {
                    return "";
                } else {
                    throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
                }
            case ARR_BIN:
            case ARR_BLOB:
                return new Bin(new byte[]{});
            case ARR_INT:
                if (editMask instanceof EditMaskInt) {
                    final Long zero = Long.valueOf(0);
                    if (editMask.validate(environment, zero) == ValidationResult.ACCEPTABLE) {
                        return zero;
                    } else {
                        final EditMaskInt editMaskInt = (EditMaskInt) editMask;
                        return Long.valueOf(Math.min(Math.abs(editMaskInt.getMinValue()), Math.abs(editMaskInt.getMaxValue())));
                    }
                } else if (editMask instanceof EditMaskList) {
                    final EditMaskList mask = (EditMaskList) editMask;
                    if (mask.getItems().size() > 0) {
                        return mask.getItems().get(0).getValue();
                    } else {
                        return null;
                    }
                } else if (editMask instanceof EditMaskConstSet) {
                    EditMaskConstSet mask = (EditMaskConstSet) editMask;
                    if (!isDuplicatesEnabled()) {
                        mask = excludeExistingItems(environment.getApplication(), mask, getValues(-1));
                    }
                    if (mask.getItems(environment.getApplication()).size() > 0) {
                        return mask.getItems(environment.getApplication()).getItem(0).getValue();
                    } else {
                        return null;
                    }
                } else if (editMask instanceof EditMaskBool) {
                    return null;
                } else {
                    throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
                }
            case ARR_DATE_TIME:
                if (editMask instanceof EditMaskDateTime) {
                    final Timestamp serverTime = environment.getCurrentServerTime();
                    if (editMask.validate(environment, serverTime) == ValidationResult.ACCEPTABLE) {
                        return serverTime;
                    }
                    final Timestamp zero = new Timestamp(0);
                    if (editMask.validate(environment, zero) == ValidationResult.ACCEPTABLE) {
                        return zero;
                    }
                    return ((EditMaskDateTime) editMask).getMaximumTime();
                } else {
                    throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
                }
            case ARR_NUM:
                if (editMask instanceof EditMaskNum) {
                    if (editMask.validate(environment, BigDecimal.ZERO) == ValidationResult.ACCEPTABLE) {
                        return BigDecimal.ZERO;
                    } else {
                        final EditMaskNum editMaskNum = (EditMaskNum) editMask;
                        if (editMaskNum.getMinValue().abs().compareTo(editMaskNum.getMaxValue().abs()) < 0) {
                            return editMaskNum.getMinValue();
                        } else {
                            return editMaskNum.getMaxValue();
                        }
                    }
                } else if (editMask instanceof EditMaskBool) {
                    return null;
                } else {
                    throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
                }
            case ARR_BOOL:
                if (editMask instanceof EditMaskBool) {
                    return null;
                } else if (editMask instanceof EditMaskNone) {
                    return null;
                } else {
                    throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
                }
            case ARR_REF:
                return null;
            default:
                throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
        }
    }

    @Override
    protected void finishEdit() {
        if (delegate != null) {
            delegate.commit();
        }
        super.finishEdit();
    }

    @Override
    protected boolean inEditState() {
        return delegate.isActive() || super.inEditState();
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
                item.setText(itemEditMask.getArrayItemNoValueStr(Application.getInstance().getMessageProvider()));
                item.setData(Qt.ItemDataRole.UserRole, null);
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
                    item.setText(itemEditMask.getArrayItemNoValueStr(Application.getInstance().getMessageProvider()));
                } else {
                    currentVal = Boolean.TRUE;
                    item.setText(((EditMaskBool) itemEditMask).getTrueTitle(getEnvironment().getDefManager()));
                }
            } else if (item.checkState() == Qt.CheckState.Unchecked) {
                currentVal = Boolean.FALSE;                
                item.setText(((EditMaskBool) itemEditMask).getFalseTitle(getEnvironment().getDefManager()));

            } else {
                currentVal = null;                
                item.setText(itemEditMask.getArrayItemNoValueStr(Application.getInstance().getMessageProvider()));
            }
            item.setData(Qt.ItemDataRole.UserRole, currentVal);
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