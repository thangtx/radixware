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

import java.awt.Color;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.*;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrRef;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.views.ArrayEditorEventListener;
import org.radixware.kernel.common.client.views.IPropertyStorePossibility;
import org.radixware.kernel.common.client.views.IPropertyValueStorage;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IToolButton;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.arreditor.AbstractArrayEditorDelegate;
import org.radixware.kernel.common.client.widgets.arreditor.ArrayItemEditingOptions;
import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.types.*;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.AbstractContainer;
import org.radixware.wps.rwt.HorizontalBoxContainer;
import org.radixware.wps.rwt.Image;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.InputBox.InvalidStringValueException;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBoxContainer;
import org.radixware.wps.rwt.uploading.IUploadedDataReader;
import org.radixware.wps.rwt.uploading.LoadFileAction;

import org.radixware.wps.views.RwtAction;
import org.radixware.wps.views.editors.valeditors.InputBoxController;
import org.radixware.wps.views.editors.valeditors.ValEditorController;


public class ArrayEditor extends VerticalBoxContainer implements IPropertyValueStorage {

    public static interface StartCellModificationListener {

        void onStartModification(final int row);
    }

    private static class DefaultStartCellModificationListener implements StartCellModificationListener {

        private List<StartCellModificationListener> listeners;

        @Override
        public void onStartModification(final int row) {
            if (listeners != null) {
                final List<StartCellModificationListener> copy = new LinkedList<>(listeners);
                for (StartCellModificationListener listener : copy) {
                    listener.onStartModification(row);
                }
            }
        }

        public void addListener(final StartCellModificationListener listener) {
            if (listener != null) {
                if (listeners == null) {
                    listeners = new LinkedList<>();
                }
                listeners.add(listener);
            }
        }

        public void removeListener(final StartCellModificationListener listener) {
            if (listeners != null) {
                listeners.remove(listener);
            }
        }

        public void clearListeners() {
            if (listeners != null) {
                listeners.clear();
            }
        }
    }

    private static class InnerToolBar extends AbstractContainer {        

        private final static class Icons extends ClientIcon.CommonOperations {

            private Icons(final String fileName) {
                super(fileName, true);
            }
            public static final ClientIcon ADD_EMPTY_ITEM = new Icons("classpath:images/addEmpty.svg");
        }
        
        
        private final ToolBar mainToolBar = new ToolBar();
        private final ToolBar mooveToolBar = new ToolBar();
        private final IClientEnvironment environment;
        public final Action createAction;
        public final Action addNullItemAction;
        public final Action removeAction;
        public final Action clearAction;
        public final Action defineAction;
        public final Action upAction;
        public final Action downAction;

        public InnerToolBar(final IClientEnvironment environment) {
            super(new Div());
            this.environment = environment;
            final MessageProvider mp = environment.getMessageProvider();

            mainToolBar.setIconSize(20, 20);
            mainToolBar.setVertical();
            mainToolBar.getHtml().setCss("float", "none");
            mainToolBar.getHtml().setCss("margin-bottom", "30px");
            //mainToolBar.getHtml().setCss("padding", "5px");
            add(mainToolBar);

            mooveToolBar.setIconSize(20, 20);
            mooveToolBar.setVertical();
            mooveToolBar.getHtml().setCss("float", "none");
            //mooveToolBar.getHtml().setCss("padding", "5px");
            add(mooveToolBar);

            createAction = addAction(Icons.CREATE, mp.translate("ArrayEditor", "Insert New Item"), mainToolBar);
            addNullItemAction = new RwtAction(environment, Icons.ADD_EMPTY_ITEM);
            addNullItemAction.setToolTip(mp.translate("ArrayEditor", "Insert Empty Item"));
            removeAction = addAction(Icons.DELETE, mp.translate("ArrayEditor", "Delete Current Item"), mainToolBar);
            clearAction = addAction(Icons.DELETE_ALL, mp.translate("ArrayEditor", "Delete All Items"), mainToolBar);
            defineAction = addAction(null, null, mainToolBar);

            upAction = addAction(Icons.UP, mp.translate("ArrayEditor", "Move Item Up"), mooveToolBar);
            downAction = addAction(Icons.DOWN, mp.translate("ArrayEditor", "Move Item Down"), mooveToolBar);
            setWidth(35);
        }

        private Action addAction(final ClientIcon icon, final String toolTip, final ToolBar toolBar) {
            final RwtAction action = new RwtAction(environment, icon);
            action.setToolTip(toolTip);
            toolBar.addAction(action);            
            //final IToolButton button = toolBar.getWidgetForAction(action);            
            return action;
        }

        public void setNullItemActionVisible(final boolean isVisible) {
            if (isVisible && mainToolBar.getWidgetForAction(addNullItemAction) == null) {
                mainToolBar.insertAction(removeAction, addNullItemAction);
            } else {
                mainToolBar.removeAction(addNullItemAction);
            }
        }
        
        public void addAction(final RwtAction action){
            mainToolBar.addAction(action);
        }
        
        public void removeAction(final RwtAction action){
            mainToolBar.removeAction(action);
        }
        
    }
        
    private final IClientEnvironment environment;
    private EditMask itemEditMask;
    private String noValueStr;
    private final EValType arrType;
    private final Class<?> valClass;
    private final ArrayEditorTable table;
    private final InnerToolBar toolBar;
    private final HorizontalBoxContainer editorArea = new HorizontalBoxContainer();
    private final HorizontalBoxContainer errorMessageArea = new HorizontalBoxContainer();    
    private final Label errorMessage = new Label();
    private boolean isItemsMovable = true;
    private boolean isDuplicatesEnabled = true;
    private boolean isReadOnly;
    private boolean isMandatory;
    private Boolean isItemMandatory;
    private AbstractArrayEditorDelegate<ValEditorController,UIObject> arrItemDelegate;
    private AbstractArrayEditorDelegate<ValEditorController,UIObject> defaultItemDelegate;
    private final PropertyArrRef propertyRef;
    private final RadSelectorPresentationDef presentation;
    private List<ArrayEditorEventListener> listeners;
    private final DefaultStartCellModificationListener startModificationListener = new DefaultStartCellModificationListener();
    private IPropertyStorePossibility storePossibility;
    private final ArrayEditorTable.IArrayChangeListener arrayChangeListener = new ArrayEditorTable.IArrayChangeListener() {
        @Override
        public void onChange(int i, Object newValue) {
            if (listeners != null) {
                for (ArrayEditorEventListener listener : listeners) {
                    listener.onCellChanged(i, newValue);
                }
            }
        }
    };
    private int minArrayItemsCount = -1;
    private int maxArrayItemsCount = -1;    
    private List<Object> predefinedValues;
    public ArrayEditor(final IClientEnvironment env, final EValType valType, final Class<?> valClass) {
        super();
        environment = env;
        itemEditMask = EditMask.newInstance(valType);
        arrType = valType.isArrayType() ? valType : valType.getArrayType();
        if (arrType == null) {
            throw new IllegalArgumentError("Can't create array editor for " + valType.getName() + " type");
        }
        this.valClass = valClass;

        propertyRef = null;
        presentation = null;

        table = new ArrayEditorTable(environment,
                arrType.getArrayItemType(),
                itemEditMask,
                getNoValueStr(),
                arrayChangeListener,
                startModificationListener);
        toolBar = new InnerToolBar(environment);
        setupUi();
    }

    public ArrayEditor(final PropertyArrRef property) {
        super();
        environment = property.getEnvironment();
        itemEditMask = EditMask.newCopy(property.getEditMask());
        itemEditMask.setNoValueStr(itemEditMask.getArrayItemNoValueStr(environment.getMessageProvider()));
        arrType = EValType.ARR_REF;
        isItemMandatory = property.isArrayItemMandatory();
        isDuplicatesEnabled = property.getDefinition().isDuplicatesEnabled();
        valClass = null;

        propertyRef = property;
        presentation = null;

        table = new ArrayEditorTable(environment,
                arrType.getArrayItemType(),
                itemEditMask,
                getNoValueStr(),
                arrayChangeListener,
                startModificationListener);
        toolBar = new InnerToolBar(environment);
        setNoValueStr(itemEditMask.getNoValueStr(environment.getMessageProvider()));
        setFirstArrayItemIndex(property.getFirstArrayItemIndex());
        setMaxArrayItemsCount(property.getMaxArrayItemsCount());
        setMinArrayItemsCount(property.getMinArrayItemsCount());
        setupUi();
    }

    public ArrayEditor(final IClientEnvironment environment, final RadSelectorPresentationDef presentation) {
        super();
        if (presentation == null) {
            throw new NullPointerException();
        }
        this.environment = environment;
        itemEditMask = new EditMaskNone();
        arrType = EValType.ARR_REF;
        valClass = null;

        propertyRef = null;
        this.presentation = presentation;

        table = new ArrayEditorTable(environment,
                arrType.getArrayItemType(),
                itemEditMask,
                getNoValueStr(),
                arrayChangeListener,
                startModificationListener);
        toolBar = new InnerToolBar(environment);
        setupUi();
    }
    
    protected ArrayItemEditingOptions getItemEditingOptions(){
        if (propertyRef==null){
            if (presentation==null){
                return new ArrayItemEditingOptions(arrType.getArrayItemType(), itemEditMask, isNullItemInadmissible(), isDuplicatesEnabled());
            }else{
                return new ArrayItemEditingOptions(arrType.getArrayItemType(), itemEditMask, isNullItemInadmissible(), isDuplicatesEnabled(), presentation);
            }
        }else{
            return new ArrayItemEditingOptions(arrType.getArrayItemType(), itemEditMask, isNullItemInadmissible(), isDuplicatesEnabled(), propertyRef);
        }
    }
    
    public AbstractArrayEditorDelegate<ValEditorController,UIObject> getEditorDelegate(){
        return arrItemDelegate;
    }
    
    public void setEditorDelegate(final AbstractArrayEditorDelegate<ValEditorController,UIObject> delegate){
        arrItemDelegate = delegate;
    }
    
    private AbstractArrayEditorDelegate<ValEditorController,UIObject> getFinalEditorDelegate(){
        final AbstractArrayEditorDelegate<ValEditorController,UIObject> delegate = getEditorDelegate();
        if (delegate==null){
            if (defaultItemDelegate==null){
                if (arrType==EValType.ARR_REF){
                    defaultItemDelegate = new ArrayRefEditorDelegate();
                }else{
                    defaultItemDelegate = new ArrayEditorDelegate();
                }
            }
            return defaultItemDelegate;
        }else{
            return delegate;
        }
    }

    @Override
    public void setPropertyStorePossibility(IPropertyStorePossibility isp) {
        this.storePossibility = isp;
    }

    @Override
    public IPropertyStorePossibility getPropertyStorePossibility() {
        return this.storePossibility;
    }

    private void setupUi() {
        editorArea.add(table);
        editorArea.add(toolBar);
        editorArea.setAutoSize(table, true);        
        
        final Image errorIcon = new Image();
        errorIcon.setIcon((WpsIcon)getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Message.WARNING));
        errorIcon.getHtml().setCss("max-height", "16px");
        errorIcon.getHtml().setCss("padding-top", "5px");
        errorIcon.setHeight(16);
        errorIcon.setWidth(16);        
        
        errorMessage.setForeground(Color.red);
        errorMessage.setFont(errorMessage.getFont().changeWeight(EFontWeight.BOLD));
        errorMessage.getHtml().setCss("padding-top", "7px");
        
        errorMessageArea.setMinimumHeight(21);
        errorMessageArea.setHeight(21);
        errorMessageArea.getHtml().setCss("max-height", "21px");
        errorMessageArea.add(errorIcon);
        errorMessageArea.addSpace(5);
        errorMessageArea.add(errorMessage);
        errorMessageArea.setAutoSize(errorMessage, true);
        
        add(editorArea);
        add(errorMessageArea);
        setAutoSize(editorArea, true);
        
        toolBar.createAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                processAddAction();
            }
        });
        toolBar.addNullItemAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                processAddEmptyItemAction();
            }
        });
        toolBar.removeAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                processRemoveAction();
            }
        });
        toolBar.clearAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                processClearAction();
            }
        });
        toolBar.defineAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                processDefineAction();
            }
        });
        toolBar.upAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                processUpAction();
            }
        });
        toolBar.downAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                processDownAction();
            }
        });
        toolBar.setNullItemActionVisible(arrType == EValType.ARR_REF);

        html.addClass("rwt-array-editor");
        html.setAttr("readonly", false);
        html.setAttr("onkeydown", "$RWT.arrayEditor.onKeyDown");
        setMinimumWidth(300);
        setMinimumHeight(220);
        updateUi();
    }

    public int getCurrentIndex() {
        return table.getCurrentRow();
    }
    
    public void setCurrentIndex(final int index){
        table.setCurrentRow(index);
    }

    @Override
    public UIObject findObjectByHtmlId(final String id) {
        UIObject result = toolBar.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }        
        result = table.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        if (editorArea.getHtmlId().equals(id)){
            return this;
        }
        return super.findObjectByHtmlId(id);
    }

    @Override
    public void visit(final Visitor visitor) {
        table.visit(visitor);
        toolBar.visit(visitor);
        super.visit(visitor);
    }

    @Override
    public void setFocused(boolean focused) {
        table.setFocused(focused);
    }

    @Override
    public void processAction(String actionName, String actionParam) {
        if ("click".equals(actionName) && actionParam != null && !actionParam.isEmpty()) {
            try {
                final int row = Integer.parseInt(actionParam);
                if (row == table.getCurrentRow()) {
                    if (!isReadOnly() && !table.isInEditingMode()) {
                        startEdit(null);
                    }
                    return;
                } else {
                    finishEdit();
                    table.setCurrentRow(row);
                }
            } catch (NumberFormatException exception) {
            }
        } else if ("dblclick".equals(actionName) && table.isDefined() && isReadOnly() && table.getCurrentRow() > -1) {
            fireDoubleClick(table.getCurrentRow());
        } else if ("up".equals(actionName) && table.isDefined() && table.getCurrentRow() > 0) {
            finishEdit();
            table.setCurrentRow(table.getCurrentRow() - 1);
        } else if ("down".equals(actionName) && table.isDefined() && table.getCurrentRow() < (table.getRowsCount() - 1)) {
            finishEdit();
            table.setCurrentRow(table.getCurrentRow() + 1);
        } else if ("home".equals(actionName) && table.isDefined() && table.getCurrentRow() > 0) {
            finishEdit();
            table.setCurrentRow(0);
        } else if ("end".equals(actionName) && table.isDefined() && table.getCurrentRow() < (table.getRowsCount() - 1)) {
            finishEdit();
            table.setCurrentRow(table.getRowsCount() - 1);
        } else if ("move-up".equals(actionName)) {
            processUpAction();
        } else if ("move-down".equals(actionName)) {
            processDownAction();
        } else if ("move-home".equals(actionName) && table.getCurrentRow() > 0 && toolBar.upAction.isEnabled()) {
            finishEdit();
            table.swapRows(table.getCurrentRow(), 0);
            table.setCurrentRow(0);
        } else if ("move-end".equals(actionName) && table.getCurrentRow() < (table.getRowsCount() - 1) && toolBar.downAction.isEnabled()) {
            finishEdit();
            table.swapRows(table.getCurrentRow(), table.getRowsCount() - 1);
            table.setCurrentRow(table.getRowsCount() - 1);
        } else if ("add".equals(actionName) && toolBar.createAction.isEnabled()) {
            processAddAction();
        } else if ("del".equals(actionName) && toolBar.removeAction.isEnabled()) {
            processRemoveAction();
        } else if ("clear".equals(actionName) && toolBar.clearAction.isEnabled()) {
            processClearAction();
        } else if ("edit".equals(actionName) && table.getCurrentRow() > -1 && !isReadOnly() && !table.isInEditingMode()) {
            startEdit(actionParam);
            return;
        } else if ("null".equals(actionName) && table.getCurrentRow() > -1 && !isReadOnly() && !table.isInEditingMode() && !isNullItemInadmissible()) {
            final int row = table.getCurrentRow();
            table.clearValue(getFinalEditorDelegate(), getItemEditingOptions(), row);
        } else if ("finishedit".equals(actionName) && table.isInEditingMode()) {
            finishEdit();
        } else if ("canceledit".equals(actionName) && table.isInEditingMode()) {
            table.finishEdit(false);
        }
        updateUi();
        super.processAction(actionName, actionParam);
    }

    private void processAddAction() {
        if (toolBar.createAction.isEnabled()) {            
            finishEdit();
            final List<Object> newValues = 
                getFinalEditorDelegate().createNewValues(this, environment, getItemEditingOptions(), getValues(-1));
            if (newValues!=null && !newValues.isEmpty()){
                for (Object value: newValues){
                    addValue(value);
                }
                updateUi();
            }
        }
    }
    
    private void addValue(final Object value){
        if (value != null || !isNullItemInadmissible() || allowNullItemAdditionWhenNullItemInadmissible()) {
            final int newRow = table.addRow(getFinalEditorDelegate(), getItemEditingOptions(), value);        
            fireNewRow(newRow);
        }
    }

    protected boolean allowNullItemAdditionWhenNullItemInadmissible() {
        return true;
    }

    private void processAddEmptyItemAction() {
        if (toolBar.addNullItemAction.isEnabled()) {
            finishEdit();
            final int newRow = table.addRow(null);
            table.validateRow(newRow, isItemMandatory());
            updateUi();
            fireNewRow(newRow);
        }
    }

    private void processRemoveAction() {
        if (toolBar.removeAction.isEnabled() && table.getCurrentRow() > -1) {
            final int row = table.getCurrentRow();
            table.removeRow(row);
            updateUi();
            fireRowsRemoved(row, 1);
        }
    }

    private boolean processClearAction() {
        if (toolBar.clearAction.isEnabled() && table.getRowsCount() > 0) {
            final String msg
                    = environment.getMessageProvider().translate("ArrayEditor", "Do you really want to delete all array items?");
            final String title
                    = environment.getMessageProvider().translate("ArrayEditor", "Confirm To Clear Array");
            if (environment.messageConfirmation(title, msg)) {
                final int count = table.getRowsCount();
                table.clearRows();
                table.setFocused(true);
                updateUi();
                fireRowsRemoved(0, count);
                return true;
            }
        }
        return false;
    }

    private void processDefineAction() {
        if (toolBar.defineAction.isEnabled()) {
            if (table.isDefined() && table.setUndefined(getNoValueStr())) {
                updateUi();
                fireDefineValue();
            } else if (!table.isDefined()) {
                table.setDefined();
                updateUi();
                fireUndefineValue();
            }
        }
    }

    private void processUpAction() {
        if (toolBar.upAction.isEnabled()) {
            final int row = table.getCurrentRow();
            if (row > 0) {
                finishEdit();
                table.swapRows(row, row - 1);
                table.setCurrentRow(row - 1);
                updateUi();
            }
        }
    }

    private void processDownAction() {
        if (toolBar.downAction.isEnabled()) {
            final int row = table.getCurrentRow();
            if (row > -1 && row < table.getRowsCount() - 1) {
                finishEdit();
                table.swapRows(row, row + 1);
                table.setCurrentRow(row + 1);
                updateUi();
            }
        }
    }

    private void updateUi() {
        updateToolBar();
        updateErrorMessage();
    }
    
    private void updateToolBar(){
        if (table.isDefined()) {
            final int lastRow = table.getRowsCount() - 1;
            toolBar.upAction.setEnabled(isItemsMovable && !isReadOnly() && table.getCurrentRow() > 0);
            toolBar.downAction.setEnabled(isItemsMovable && !isReadOnly() && table.getCurrentRow() > -1 && table.getCurrentRow() < lastRow);
        } else {
            toolBar.upAction.setEnabled(false);
            toolBar.downAction.setEnabled(false);
        }        
        updateDefinedAction(table.isDefined());
        final int itemsCount = table.getRowsCount();
        final boolean moreOrEqThanMax = maxArrayItemsCount >= 0 && itemsCount  >= maxArrayItemsCount;
        final boolean lessOrEqThanMin = minArrayItemsCount > 0 && itemsCount  <= minArrayItemsCount;
        toolBar.clearAction.setEnabled(table.isDefined() && !isReadOnly() && itemsCount  > 0);
        toolBar.removeAction.setEnabled(table.isDefined() && !isReadOnly() && table.getCurrentRow() > -1 && !lessOrEqThanMin);
        toolBar.createAction.setEnabled(!isReadOnly() && !moreOrEqThanMax);
        toolBar.addNullItemAction.setEnabled(!isNullItemInadmissible() && !isReadOnly());
    }
    
    private void updateErrorMessage() {
        if (table.isDefined()){
            boolean lengthIsInvalid = false;        
            final String max = getEnvironment().getMessageProvider().translate("ArrayEditor", "Maximum items count is %d");
            final String min = getEnvironment().getMessageProvider().translate("ArrayEditor", "Minimum items count is %d");
            StringBuilder sb = new StringBuilder();
            final int itemsCount = table.getRowsCount();

            if(maxArrayItemsCount >= 0 && itemsCount > maxArrayItemsCount) {
                sb.append(String.format(max, getMaxArrayItemsCount()));
                lengthIsInvalid = true;
            }

            if(minArrayItemsCount >= 0 && itemsCount < minArrayItemsCount) {
                sb.append(System.getProperty("line.separator"));
                sb.append(String.format(min, getMinArrayItemsCount()));
                lengthIsInvalid = true;
            }

            if(lengthIsInvalid) {
                errorMessage.setText(sb.toString().trim());            
            }        
            errorMessageArea.setVisible(lengthIsInvalid);
        }else{
            errorMessageArea.setVisible(false);
        }
    }
    
    public final void updateDefinedAction(final boolean isDefined) {
        if (isDefined) {
            toolBar.defineAction.setIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.CLEAR));
            toolBar.defineAction.setToolTip(environment.getMessageProvider().translate("Value", "Clear Value"));
            toolBar.defineAction.setEnabled(!isMandatory && !isReadOnly());
        } else {
            toolBar.defineAction.setIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.ValueModification.DEFINE));
            toolBar.defineAction.setToolTip(environment.getMessageProvider().translate("ArrayEditor", "Define Value"));
            toolBar.defineAction.setEnabled(!isReadOnly());
        }
    }

    @SuppressWarnings("unchecked")
    public void setCurrentValue(final Arr value) {
        if (value == null) {
            table.setUndefined(getNoValueStr());
        } else {
            table.setCurrentValue(getFinalEditorDelegate(), getItemEditingOptions(), value);
            table.validateArray(isItemMandatory());
            if (table.getRowsCount() > 0) {
                table.setCurrentRow(0);
            }
        }
        updateUi();
    }

    @SuppressWarnings("unchecked")
    public Arr getCurrentValue() {
        if (currentValueIsNull()) {
            return null;
        }
        final List<Object> array = getValues(-1);
        if (array==null){
            return null;
        }

        Arr value;
        if (valClass != null) {//enums
            try {
                value = (Arr) valClass.newInstance();
            } catch (InstantiationException | IllegalAccessException exception) {
                getEnvironment().processException(exception);
                return null;
            }
        } else {
            value = createEmptyArr(arrType);
        }        

        for (Object item : array) {
            value.add(item);
        }

        return value;
    }

    private static Arr createEmptyArr(EValType type) {
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

    private List<Object> getValues(final int exceptItem) {
        finishEdit();
        final List<Object> values = table.getCurrentValue();
        if (values != null && exceptItem > -1) {
            values.remove(exceptItem);
        }
        return values;
    }

    public final boolean currentValueIsNull() {
        return !table.isDefined();
    }

    public final Object getSelectedValue() {
        finishEdit();
        final int currentRow = table.getCurrentRow();
        return currentRow > -1 ? table.getCurrentValue().get(currentRow) : null;
    }

    public final boolean isEmpty() {
        return !table.isDefined() || table.getRowsCount() == 0;
    }

    public boolean checkForDuplicates() {
        if (!isDuplicatesEnabled()) {
            final List currentValue = getValues(-1);
            if (currentValue != null) {
                final int size = currentValue.size();
                for (int i = 0; i < size; i++) {
                    for (int j = i + 1; j < size; j++) {
                        if (Utils.equals(currentValue.get(i), currentValue.get(j))) {
                            finishEdit();
                            final MessageProvider mp = environment.getMessageProvider();
                            final String title = mp.translate("ExplorerMessage", "Duplicate Values are Not Allowed");
                            final String message = mp.translate("ExplorerMessage", "Duplicate values detected for %s and %s items");
                            final int startIndex = table.getStartIndex();
                            environment.messageError(title, String.format(message, i + startIndex, j + startIndex));
                            table.setCurrentRow(i);
                            startEdit(null);
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean checkValues(final boolean quiet) {
        if (table.isDefined()) {
            if (quiet) {
                for (int row = table.getRowsCount() - 1; row >= 0; row--) {
                    if (table.validateRow(row, isItemMandatory()) != ValidationResult.ACCEPTABLE) {
                        return false;
                    }
                }
                return isLengthInRange();
            } else {
                final List<Integer> rows = getRowsWithInvalidValues();
                if (rows.size() > 1) {
                    final StringBuilder indexBuilder = new StringBuilder();
                    final int startIndex = table.getStartIndex();
                    for (Integer row : rows) {
                        if (indexBuilder.length() > 0) {
                            indexBuilder.append(", ");
                        }
                        indexBuilder.append(String.valueOf(row + startIndex));
                    }
                    final MessageProvider mp = environment.getMessageProvider();
                    final String title = mp.translate("ExplorerMessage", "Value is Invalid");
                    final String message = mp.translate("ExplorerMessage", "Values of items with index %1$s is invalid");
                    environment.messageError(title, String.format(message, String.valueOf(indexBuilder.toString())));
                    table.setCurrentRow(rows.get(0));
                    startEdit(null);
                    return false;
                } else if (rows.size() == 1) {
                    final MessageProvider mp = environment.getMessageProvider();
                    final String title = mp.translate("ExplorerMessage", "Value is Invalid");
                    final String message = mp.translate("ExplorerMessage", "Value of item with index %1$s is invalid");
                    final int itemIndex = rows.get(0) + table.getStartIndex();
                    environment.messageError(title, String.format(message, String.valueOf(itemIndex)));
                    table.setCurrentRow(rows.get(0));
                    startEdit(null);
                    return false;
                } else if (!isLengthInRange()){                    
                    final MessageProvider mp = environment.getMessageProvider();
                    final String title = mp.translate("ExplorerMessage", "Value is Invalid");
                    final String message = errorMessage.getText();
                    environment.messageError(title, message);
                    return false;
                }
            }

        }
        return true;
    }
    
    public boolean isLengthInRange() {
        if (currentValueIsNull()) {
            return true;//Значение null проверяется отдельно
        } else {
            final int itemsCount = table.getRowsCount();
            if (getMaxArrayItemsCount() >= 0 && itemsCount  > getMaxArrayItemsCount()) {
                return false;
            }
            if (getMinArrayItemsCount() > 0 && itemsCount < getMinArrayItemsCount()) {
                return false;
            }
            return true;
        }
    }    

    public final List<Integer> getRowsWithInvalidValues() {
        final List<Integer> rows = new LinkedList<>();
        for (int row = 0, count = table.getRowsCount(); row < count; row++) {
            if (table.validateRow(row, isItemMandatory()) != ValidationResult.ACCEPTABLE) {
                rows.add(row);
            }
        }
        return rows;
    }

    public final void setNoValueStr(final String newString) {
        noValueStr = newString;
        if (!currentValueIsNull()) {
            toolBar.defineAction.setToolTip(getNoValueStr());
        } else if (!table.isDefined()) {
            table.setUndefined(noValueStr);//update label text
        }
    }

    private String getNoValueStr() {
        if (noValueStr == null || noValueStr.isEmpty()) {
            return environment.getMessageProvider().translate("Value", "<not defined>");
        } else {
            return noValueStr;
        }
    }

    private boolean isNullItemInadmissible() {
        final boolean predefinedMandatory = isItemMandatory == null ? isMandatory : isItemMandatory.booleanValue();
        final List<Object> values = table.getCurrentValue();
        final boolean hasNullValue = values != null && values.contains(null);
        return predefinedMandatory || (!isDuplicatesEnabled && hasNullValue);
    }

    public boolean isDuplicatesEnabled() {
        return isDuplicatesEnabled;
    }

    public void setDuplicatesEnabled(boolean isDuplicatesEnabled) {
        this.isDuplicatesEnabled = isDuplicatesEnabled;
    }

    public boolean isItemsMovable() {
        return isItemsMovable;
    }

    public void setItemsMovable(boolean isItemsMovable) {
        this.isItemsMovable = isItemsMovable;
        updateUi();
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
        if (isMandatory && !table.isDefined()) {
            table.setDefined();
        }
        updateUi();
        table.validateArray(isItemMandatory());
    }

    public final boolean isItemMandatory() {
        return isItemMandatory == null ? isMandatory() : isItemMandatory.booleanValue();
    }

    public void setItemMandatory(final boolean isMandatory) {
        isItemMandatory = Boolean.valueOf(isMandatory);
        table.validateArray(isItemMandatory());
    }

    public void setEditMask(EditMask editMask) {
        itemEditMask = EditMask.newCopy(editMask);
        itemEditMask.setNoValueStr(itemEditMask.getArrayItemNoValueStr(null));
        table.setEditMask(itemEditMask);
        table.validateArray(isItemMandatory());
        setNoValueStr(editMask.getNoValueStr(environment.getMessageProvider()));
    }

    public EditMask getEditMask() {
        return EditMask.newCopy(itemEditMask);
    }

    public boolean isReadOnly() {
        if (isReadOnly){
            return true;
        }
        if (itemEditMask instanceof EditMaskFilePath){
            final EditMaskFilePath editMaskFilePath = (EditMaskFilePath)itemEditMask;
            return editMaskFilePath.getSelectionMode()==EFileSelectionMode.SELECT_DIRECTORY 
                       && !editMaskFilePath.getHandleInputAvailable();
        }else{
            return false;
        }
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        getHtml().setAttr("readonly", isReadOnly);
        updateUi();
    }

    public final void setOperationsVisible(final boolean isVisible) {
        toolBar.setVisible(isVisible);
    }
    
    public final void setFirstArrayItemIndex(final int index){
        table.setStartIndex(index);
    }
    
    public final int getFirstArrayItemIndex(){
        return table.getStartIndex();
    }

    @SuppressWarnings("unchecked")
    private void startEdit(final String initialText) {
        final ArrayItemEditingOptions editingOptions = getItemEditingOptions();
        final ValEditorController controller = table.startEdit(getFinalEditorDelegate(), editingOptions);
        if (controller instanceof InputBoxController && initialText != null && !initialText.isEmpty()) {
            final InputBox.ValueController valController = ((InputBoxController) controller).getValueController();
            if (valController != null) {
                try {
                    controller.setValue(valController.getValue(initialText));
                } catch (InvalidStringValueException ex) {//NOPMD
                    //ignoring
                }
            }
        }
        if (predefinedValues!=null && !predefinedValues.isEmpty()){
            ((InputBoxController)controller).setPredefinedValues(predefinedValues);
        }
        IPropertyStorePossibility sp = getPropertyStorePossibility();
        if (sp != null) {
            if (sp.canPropertyReadValue()) {
                addLoadButton(controller);
            }
            if (sp.canPropertySaveValue()) {
                addSaveButton(controller);
            }
        }
    }

    private void addLoadButton(final ValEditorController controller) {
        ToolButton loadButton = new ToolButton();

        IUploadedDataReader reader = new IUploadedDataReader() {
            @Override
            @SuppressWarnings("unchecked")
            public void readData(final InputStream stream, final String fileName, final long fileSize) {
                Object o = getPropertyStorePossibility().readPropertyValue(stream);
                controller.setValue(o);
            }
        };
        final LoadFileAction action = new LoadFileAction(environment, reader);
        action.addActionPresenter((RwtAction.IActionPresenter) loadButton);
        loadButton.addAction(action);
        loadButton.setToolTip(environment.getMessageProvider().translate("ArrayEditor", "Load Value"));
        loadButton.setIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.OPEN));
        controller.addButton(loadButton);
    }

    private void addSaveButton(final ValEditorController controller) {
        ToolButton saveButton = new ToolButton();
        saveButton.setToolTip(environment.getMessageProvider().translate("ArrayEditor", "Save Value"));
        saveButton.setIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.SAVE));
        saveButton.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                if (getPropertyStorePossibility() != null) {
                    Object val = controller.getValue();
                    getPropertyStorePossibility().writePropertyValue(val);
                }
            }
        });

        controller.addButton(saveButton);
    }

    public void finishEdit() {
        if (table.isInEditingMode()) {
            table.finishEdit(true);
            final int row = table.getCurrentRow();
            table.validateRow(row, isItemMandatory());
        }
    }

    public void addEventListener(final ArrayEditorEventListener listener) {
        if (listeners == null) {
            listeners = new LinkedList<>();
            listeners.add(listener);
        } else if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeEventListener(final ArrayEditorEventListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    private void fireDoubleClick(final int row) {
        if (listeners != null) {
            for (ArrayEditorEventListener listener : listeners) {
                listener.onCellDoubleClick(row);
            }
        }
    }

    private void fireRowsRemoved(final int row, final int count) {
        if (listeners != null) {
            for (ArrayEditorEventListener listener : listeners) {
                listener.onRowsRemoved(row, count);
            }
        }
    }

    private void fireNewRow(final int row) {
        if (listeners != null) {
            for (ArrayEditorEventListener listener : listeners) {
                listener.onRowsInserted(row, 1);
            }
        }
    }

    private void fireDefineValue() {
        if (listeners != null) {
            for (ArrayEditorEventListener listener : listeners) {
                listener.onDefineValue();
            }
        }
    }

    private void fireUndefineValue() {
        if (listeners != null) {
            for (ArrayEditorEventListener listener : listeners) {
                listener.onUndefineValue();
            }
        }
    }

    public void addStartCellModificationListener(StartCellModificationListener listener) {
        startModificationListener.addListener(listener);
    }

    public void removeStartCellModificationListener(StartCellModificationListener listener) {
        startModificationListener.removeListener(listener);
    }
    
    public int getMinArrayItemsCount() {
        return minArrayItemsCount;
    }

    public void setMinArrayItemsCount(int minArrayItemsCount) {
        this.minArrayItemsCount = minArrayItemsCount;
    }

    public int getMaxArrayItemsCount() {
        return maxArrayItemsCount;
    }
     
    public void setMaxArrayItemsCount(final int maxArrayItemsCount) {
        this.maxArrayItemsCount = maxArrayItemsCount;
    }
    
    public void setPredefinedValues(final List<Object> values) {
        predefinedValues = values == null ? Collections.emptyList() : new ArrayList<>(values);
    }
    
    public void addCustomAction(final Action action){
        toolBar.addAction((RwtAction)action);
    }
    
    public void removeCustomAction(final Action action){
        toolBar.removeAction((RwtAction)action);
    }    
}
