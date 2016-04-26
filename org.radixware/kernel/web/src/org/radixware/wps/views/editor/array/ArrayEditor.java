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

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.*;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrRef;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.ChoosableEntitiesFilter;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.views.ArrayEditorEventListener;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.views.IPropertyStorePossibility;
import org.radixware.kernel.common.client.views.IPropertyValueStorage;
import org.radixware.kernel.common.client.views.ISelectEntityDialog;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.types.*;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.rwt.AbstractContainer;
import org.radixware.wps.rwt.HorizontalBoxContainer;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.InputBox.InvalidStringValueException;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.uploading.IUploadedDataReader;
import org.radixware.wps.rwt.uploading.LoadFileAction;
import org.radixware.wps.utils.UIObjectUtils;

import org.radixware.wps.views.RwtAction;
import org.radixware.wps.views.editors.valeditors.InputBoxController;
import org.radixware.wps.views.editors.valeditors.ValEditorController;


public class ArrayEditor extends HorizontalBoxContainer implements IPropertyValueStorage {

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
    }
    private final IClientEnvironment environment;
    private EditMask itemEditMask;
    private String noValueStr;
    private final EValType arrType;
    private final Class<?> valClass;
    private final ArrayEditorTable table;
    private final InnerToolBar toolBar;
    private boolean isItemsMovable = true;
    private boolean isDuplicatesEnabled = true;
    private boolean isReadOnly;
    private boolean isMandatory;
    private Boolean isItemMandatory;
    private final PropertyArrRef propertyRef;
    private final RadSelectorPresentationDef presentation;
    private GroupModel group;
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

    @Override
    public void setPropertyStorePossibility(IPropertyStorePossibility isp) {
        this.storePossibility = isp;
    }

    @Override
    public IPropertyStorePossibility getPropertyStorePossibility() {
        return this.storePossibility;
    }

    private void setupUi() {
        add(table);
        add(toolBar);
        setAutoSize(table, true);
        setPadding(5);
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
        updateToolBar();
    }

    public int getCurrentIndex() {
        return table.getCurrentRow();
    }

    private ISelectEntityDialog createSelectEntityDialog(final Reference currentValue) throws InterruptedException {
        final GroupModel groupModel = getGroupModel(currentValue);
        if (groupModel != null) {
            final boolean canBeNull = !isNullItemInadmissible();
            return environment.getApplication().getStandardViewsFactory().newSelectEntityDialog(group, canBeNull);
        }
        return null;
    }
    
    private ArrRef calcTitles(final ArrRef arrRef) throws InterruptedException, ServiceClientException{
        if (arrRef==null
            || arrRef.isEmpty() 
            || propertyRef==null 
            || propertyRef.createContext() instanceof IContext.ContextlessSelect){
            return arrRef;
        }
        return propertyRef.updateTitles(arrRef);
    }
    
    private Reference calcTitle(final Reference ref) throws InterruptedException, ServiceClientException{
        if (ref==null){
            return null;
        }else{
            final ArrRef arrRefs = calcTitles(new ArrRef(ref));
            return arrRefs==null || arrRefs.size()!=1 ? null : arrRefs.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    private GroupModel getGroupModel(final Reference currentValue) {
        if (propertyRef == null && presentation == null) {
            throw new IllegalUsageError("Selector presentation not defined");
        }
        if (group == null) {
            try {
                if (propertyRef != null) {
                    group = propertyRef.openGroupModel();
                } else {
                    final Model holderModel = UIObjectUtils.findNearestModel(this);
                    if (holderModel==null){
                        group = GroupModel.openTableContextlessSelectorModel(getEnvironment(), presentation);
                    }else{
                        group = GroupModel.openTableContextlessSelectorModel(holderModel, presentation);
                    }
                }
            } catch (RuntimeException ex) {
                processOpenSelectorException(ex);
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
                processOpenSelectorException(ex);
                return null;
            } catch (InterruptedException ex) {//Group is empty - never thrown
                return null;
            }
        }

        if (!isDuplicatesEnabled()) {
            final List<Object> values = table.getCurrentValue();
            if (values != null) {
                final ChoosableEntitiesFilter filter = new ChoosableEntitiesFilter();
                if (currentValue != null) {
                    values.remove(currentValue);
                }
                for (Object value : values) {
                    if (value != null) {
                        filter.add(((Reference) value).getPid());
                    }
                }
                group.setEntitySelectionController(filter);
            }
        }
        return group;
    }

    private void processOpenSelectorException(final Throwable ex) {
        final MessageProvider mp = environment.getMessageProvider();
        final String err_title = mp.translate("ExplorerException", "Error on opening selector");
        final String err_msg = mp.translate("ExplorerException", "Can't open selector for \'%s\':\n%s");
        final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex);
        final String trace = reason + ":\n" + ClientException.exceptionStackToString(ex);
        final String title = propertyRef != null ? propertyRef.getTitle() : presentation.getTitle();
        environment.messageError(err_title, String.format(err_msg, title, reason));
        environment.getTracer().error(String.format(err_msg, title, reason));
        environment.getTracer().put(EEventSeverity.DEBUG, String.format(err_msg, title, trace),
                EEventSource.EXPLORER);
    }
    
    private void processCalcTitleException(final Throwable ex) {
        final MessageProvider mp = environment.getMessageProvider();
        final String err_title = mp.translate("ExplorerException", "Error on set value");
        final String err_msg = mp.translate("ExplorerException", "Failed to set value of \'%s\':\n%s");
        final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex);
        final String trace = reason + ":\n" + ClientException.exceptionStackToString(ex);
        final String title = propertyRef != null ? propertyRef.getTitle() : presentation.getTitle();
        environment.messageError(err_title, String.format(err_msg, title, reason));
        environment.getTracer().error(String.format(err_msg, title, reason));
        environment.getTracer().put(EEventSeverity.DEBUG, String.format(err_msg, title, trace),
                EEventSource.EXPLORER);
    }
    

    protected Object getDefaultItemValue(IClientEnvironment environment, final EValType type, final EditMask editMask) {
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
                        mask = ArrayEditorTable.excludeExistingItems(environment.getApplication(), mask, getValues(-1));
                    }
                    if (mask.getItems(environment.getApplication()).size() > 0) {
                        return mask.getItems(environment.getApplication()).getItem(0).getValue();
                    } else {
                        return null;
                    }
                } else if (editMask instanceof EditMaskFilePath) {
                    /*File[] roots = File.listRoots();
                     return roots[0].getAbsolutePath();*/
                    return null;

                } else if (editMask instanceof EditMaskBool) {
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
                        mask = ArrayEditorTable.excludeExistingItems(environment.getApplication(), mask, getValues(-1));
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
                /*if (editMask instanceof EditMaskBool) {
                 EditMaskBool editMaskBool = (EditMaskBool) editMask;
                 return editMaskBool.getValue();
                 } else {
                 throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
                 }*/
                return null;
            case ARR_REF:
                return null;
            default:
                throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
        }
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
            table.clearValue(row);
        } else if ("finishedit".equals(actionName) && table.isInEditingMode()) {
            finishEdit();
        } else if ("canceledit".equals(actionName) && table.isInEditingMode()) {
            table.finishEdit(false);
        }
        updateToolBar();
        super.processAction(actionName, actionParam);
    }

    private void processAddAction() {
        if (toolBar.createAction.isEnabled()) {
            finishEdit();
            final int newRow;
            if (arrType == EValType.ARR_REF) {
                try {
                    final ISelectEntityDialog dialog = createSelectEntityDialog(null);
                    if (dialog != null && dialog.execDialog() != DialogResult.REJECTED) {
                        final EntityModel entity = dialog.getSelectedEntity();
                        final Reference ref;
                        try{                        
                            ref = calcTitle(entity == null ? null : new Reference(entity));
                        }catch(InterruptedException exception){
                            return;
                        }catch(RuntimeException | ServiceClientException exception){
                            processCalcTitleException(exception);
                            return;
                        }
                        newRow = table.addRow(ref);
                    } else {
                        return;
                    }
                } catch (InterruptedException e) {
                    return;
                } catch (RuntimeException ex) {
                    processOpenSelectorException(ex);
                    return;
                } finally {
                    if (group != null) {
                        group.clean();
                    }
                }
                table.validateRow(newRow, isItemMandatory());
                updateToolBar();
                fireNewRow(newRow);
            } else {
                final Object itemValue = isNullItemInadmissible() ? getDefaultItemValue(getEnvironment(), arrType, itemEditMask) : null;
                if (itemValue == null && isNullItemInadmissible()) {
                    if (!allowNullItemAdditionWhenNullItemInadmissible()) {
                        return;
                    }
                }
                newRow = table.addRow(itemValue);
                updateToolBar();
                fireNewRow(newRow);
                if (!isReadOnly()) {
                    startEdit(null);
                }
            }
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
            updateToolBar();
            fireNewRow(newRow);
        }
    }

    private void processRemoveAction() {
        if (toolBar.removeAction.isEnabled() && table.getCurrentRow() > -1) {
            final int row = table.getCurrentRow();
            table.removeRow(row);
            updateToolBar();
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
                updateToolBar();
                fireRowsRemoved(0, count);
                return true;
            }
        }
        return false;
    }

    private void processDefineAction() {
        if (toolBar.defineAction.isEnabled()) {
            if (table.isDefined() && table.setUndefined(getNoValueStr())) {
                updateToolBar();
                fireDefineValue();
            } else if (!table.isDefined()) {
                table.setDefined();
                updateToolBar();
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
                updateToolBar();
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
                updateToolBar();
            }
        }
    }

    private void updateToolBar() {
        if (table.isDefined()) {
            final int lastRow = table.getRowsCount() - 1;
            toolBar.upAction.setEnabled(isItemsMovable && !isReadOnly && table.getCurrentRow() > 0);
            toolBar.downAction.setEnabled(isItemsMovable && !isReadOnly && table.getCurrentRow() > -1 && table.getCurrentRow() < lastRow);
        } else {
            toolBar.upAction.setEnabled(false);
            toolBar.downAction.setEnabled(false);
        }
        updateDefinedAction(table.isDefined());
        toolBar.clearAction.setEnabled(table.isDefined() && !isReadOnly && table.getRowsCount() > 0);
        toolBar.removeAction.setEnabled(table.isDefined() && !isReadOnly && table.getCurrentRow() > -1);
        toolBar.createAction.setEnabled(!isReadOnly);
        toolBar.addNullItemAction.setEnabled(!isNullItemInadmissible() && !isReadOnly);
    }

    public final void updateDefinedAction(final boolean isDefined) {
        if (isDefined) {
            toolBar.defineAction.setIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.CLEAR));
            toolBar.defineAction.setToolTip(environment.getMessageProvider().translate("Value", "Clear Value"));
            toolBar.defineAction.setEnabled(!isMandatory && !isReadOnly);
        } else {
            toolBar.defineAction.setIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.ValueModification.DEFINE));
            toolBar.defineAction.setToolTip(environment.getMessageProvider().translate("ArrayEditor", "Define Value"));
            toolBar.defineAction.setEnabled(!isReadOnly);
        }
    }

    @SuppressWarnings("unchecked")
    public void setCurrentValue(final Arr value) {
        if (value == null) {
            table.setUndefined(getNoValueStr());
        } else {
            table.setCurrentValue(value);
            table.validateArray(isItemMandatory());
            if (table.getRowsCount() > 0) {
                table.setCurrentRow(0);
            }
        }
        updateToolBar();
    }

    @SuppressWarnings("unchecked")
    public Arr getCurrentValue() {
        if (currentValueIsNull()) {
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
        final List<Object> array = getValues(-1);

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
                return true;
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
                }
            }

        }
        return true;
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
        updateToolBar();
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
        if (isMandatory && !table.isDefined()) {
            table.setDefined();
        }
        updateToolBar();
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
        return isReadOnly;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        getHtml().setAttr("readonly", isReadOnly);
        updateToolBar();
    }

    public final void setOperationsVisible(final boolean isVisible) {
        toolBar.setVisible(isVisible);
    }

    @SuppressWarnings("unchecked")
    private void startEdit(final String initialText) {
        /* if (arrType == EValType.ARR_BOOL) {
         if (" ".equals(initialText)) {//spacebar
         table.editBooleanValue(table.getCurrentRow(), isNullItemInadmissible());
         }
         } else*/ {
            final ValEditorController controller = table.startEdit(isDuplicatesEnabled(), isNullItemInadmissible());
            if (controller != null && arrType == EValType.ARR_REF) {
                final ToolButton button = new ToolButton();
                button.setToolTip(environment.getMessageProvider().translate("ArrayEditor", "Select Object"));
                final ClientIcon icon = ClientIcon.Definitions.SELECTOR;
                button.setIcon(environment.getApplication().getImageManager().getIcon(icon));
                button.addClickHandler(new IButton.ClickHandler() {
                    @Override
                    public void onClick(final IButton source) {
                        try {
                            final ISelectEntityDialog dialog = createSelectEntityDialog((Reference) controller.getValue());
                            if (dialog != null && dialog.execDialog() != DialogResult.REJECTED) {
                                final EntityModel entity = dialog.getSelectedEntity();
                                final Reference ref;
                                try{
                                    ref = calcTitle(entity == null ? null : new Reference(entity));
                                }catch(InterruptedException exception){
                                    return;
                                }catch(RuntimeException | ServiceClientException exception){
                                    processCalcTitleException(exception);
                                    return;
                                }
                                controller.setValue(ref);
                            }
                        } catch (InterruptedException e) {//NOPMD
                        } catch (RuntimeException ex) {
                            processOpenSelectorException(ex);
                        } finally {
                            if (group != null) {
                                group.clean();
                            }
                        }
                    }
                });
                controller.addButton(button);
            } else if (controller instanceof InputBoxController && initialText != null && !initialText.isEmpty()) {
                final InputBox.ValueController valController = ((InputBoxController) controller).getValueController();
                if (valController != null) {
                    try {
                        controller.setValue(valController.getValue(initialText));
                    } catch (InvalidStringValueException ex) {//NOPMD
                        //ignoring
                    }
                }
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
}
