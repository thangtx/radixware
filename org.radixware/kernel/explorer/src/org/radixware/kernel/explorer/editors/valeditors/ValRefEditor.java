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

package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCompleter;
import com.trolltech.qt.gui.QDialog.DialogCode;
import com.trolltech.qt.gui.QStringListModel;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.EntityObjectTitles;
import org.radixware.kernel.common.client.eas.EntityObjectTitlesProvider;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.IEntitySelectionController;
import org.radixware.kernel.common.client.models.IPresentationChangedHandler;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.RawEntityModelData;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.RefEditingHistory;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.dialogs.EntityEditorDialog;
import org.radixware.kernel.explorer.dialogs.SelectEntityDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.models.EntitiesListModel;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class ValRefEditor extends AbstractListEditor<Reference> implements IPresentationChangedHandler {
    
    private final static List<String> DUMMY_ITEMS = Arrays.<String>asList("item1", "item2");

    private IEntitySelectionController selectionController;
    private GroupModel.SelectionListener selectionListener;
    private GroupModel group = null;
    private EntityModel entity = null;
    private EntitiesListModel listModel;
    private final QToolButton selectBtn, editBtn;
    private boolean showSelectBtn;
    private boolean showEditBtn;
    private boolean dropDownBtnVisible;
    private boolean predefinedValuesAreValid = false;
    private final List<String> predefinedValuesTitles = new ArrayList<>();
    private final QStringListModel completionModelHistory = new QStringListModel();
    public final QSignalEmitter.Signal0 beforeEditorOpened = new QSignalEmitter.Signal0();
    public final QSignalEmitter.Signal0 editorClosed = new QSignalEmitter.Signal0();
    public final QSignalEmitter.Signal0 beforeSelectorOpened = new QSignalEmitter.Signal0();
    public final QSignalEmitter.Signal0 selectorClosed = new QSignalEmitter.Signal0();
    
    public ValRefEditor(IClientEnvironment environment, QWidget parent, final boolean mandatory, final boolean readonly, final boolean showSelectObjectBtn, final boolean showEditObjectBtn) {
        this(environment, parent, new EditMaskRef(), mandatory, readonly, showSelectObjectBtn, showEditObjectBtn);
    }

    public ValRefEditor(IClientEnvironment environment, QWidget parent, final boolean mandatory, final boolean readonly) {
        this(environment, parent, new EditMaskRef(), mandatory, readonly, true, true);
    }

    public ValRefEditor(IClientEnvironment environment, QWidget parent) {
        this(environment, parent, new EditMaskRef(), true, false, true, true);
    }

    public ValRefEditor(IClientEnvironment environment, QWidget parent, EditMaskRef mask, final boolean mandatory, final boolean readonly) {
        this(environment, parent, mask, mandatory, readonly, true, true);
    }

    public ValRefEditor(IClientEnvironment environment, QWidget parent, EditMaskRef mask, final boolean mandatory, final boolean readonly, final boolean showSelectObjectBtn, final boolean showEditObjectBtn) {
        super(environment, parent, mask==null ? new EditMaskRef() : mask , mandatory, readonly);
        {
            final QAction action = new QAction(ValRefEditor.this);
            action.triggered.connect(ValRefEditor.this, "select()");
            action.setToolTip(environment.getMessageProvider().translate("ValRefEditor", "Select Object"));
            action.setIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.SELECTOR));
            action.setObjectName("select_object");
            selectBtn = addButton(null, action);
        }
        {
            final QAction action = new QAction(ValRefEditor.this);
            action.triggered.connect(ValRefEditor.this, "edit()");
            action.setObjectName("edit_object");
            editBtn = addButton(null, action);
        }
        this.showSelectBtn = showSelectObjectBtn;
        this.showEditBtn = showEditObjectBtn;
        updateButtons();
        invalidateGroupModel();
        updateComboBoxLook();
    }

    public void setSelectorPresentation(final Id classId, final Id presentationId) {
        final EditMaskRef copyMask = getEditMaskRef();
        copyMask.setSelectorPresentationId(presentationId);
        copyMask.setCondition((SqmlExpression) null);
        setEditMask(copyMask);
        invalidateGroupModel();
        setValue(null);
        updateButtons();
    }

    public void setSelectorPresentation(final RadSelectorPresentationDef presentation) {
        final EditMaskRef copyMask = getEditMaskRef();
        copyMask.setSelectorPresentationId(presentation==null ? null : presentation.getId());
        copyMask.setCondition((SqmlExpression) null);                
        setEditMask(copyMask);
        invalidateGroupModel();
        setValue(null);
        updateButtons();
    }

    public void setCondition(final org.radixware.schemas.xscml.Sqml condition) {
        final EditMaskRef copyMask = getEditMaskRef();
        copyMask.setCondition(condition == null ? null : (org.radixware.schemas.xscml.Sqml) condition.copy());
        setEditMask(copyMask);
        if (group != null) {
            try {
                group.setCondition(condition);
            } catch (ServiceClientException ex) {
                getEnvironment().processException(ex);
            } catch (InterruptedException ex) {
            }
        }
    }

    public void setCondition(final SqmlExpression condition) {
        final EditMaskRef copyMask = getEditMaskRef();
        copyMask.setCondition(condition == null ? null : condition.asXsqml());
        setEditMask(copyMask);
        if (group != null) {
            try {
                group.setCondition(condition);
            } catch (ServiceClientException ex) {
                getEnvironment().processException(ex);
            } catch (InterruptedException ex) {
            }
        }
    }

    public final void setDefaultFilterId(final Id filterId) {
        final EditMaskRef copyMask = getEditMaskRef();
        copyMask.setDefaultFilterId(filterId);
        setEditMask(copyMask);
    }

    public final void setDefaultSortingId(final Id sortingId, final Id filterId) {
        final EditMaskRef copyMask = getEditMaskRef();
        copyMask.setDefaultSortingId(sortingId, filterId);
        setEditMask(copyMask);

    }

    public final boolean isDefinedDefaultFilterId() {
        final EditMaskRef copyMask = getEditMaskRef();
        return copyMask.isDefinedDefaultFilter();
    }

    public final Id getDefaultFilterId() {
        final EditMaskRef copyMask = getEditMaskRef();
        return copyMask.getDefaultFilterId();
    }

    public final boolean isDefinedDefaultSortingId(final Id filterId) {
        final EditMaskRef copyMask = getEditMaskRef();
        return copyMask.isDefinedDefaultSorting(filterId);
    }

    public final Id getDefaultSortingId(final Id filterId) {
        final EditMaskRef copyMask = getEditMaskRef();
        return copyMask.getDefaultSortingId(filterId);
    }

    public void setEntitySelectionController(final IEntitySelectionController controller) {
        selectionController = controller;
        if (group != null) {
            group.setEntitySelectionController(controller);
        }
    }
    
    public void setEntitySelectionListener(final GroupModel.SelectionListener listener){
        selectionListener = listener;
        if (group != null){
            group.addSelectionListener(listener);
        }
    }

    public EntityModel getEntityModel() {
        if (entity == null) {
            entity = createEntityModel();
        }
        return entity;
    }

    public void setEditorPresentations(final Id classId, final List<Id> presentationIds) {
        final EditMaskRef copyMask = getEditMaskRef();
        copyMask.setEditorPresentationIds(presentationIds);
        setEditMask(copyMask);
        updateButtons();
    }

    public void setEditorPresentations(final List<RadEditorPresentationDef> presentations) {
        final EditMaskRef copyMask = getEditMaskRef();
        List<Id> presentationIds = new LinkedList<>();
        if (presentations!=null){
            for (RadEditorPresentationDef presentation : presentations) {
                if (presentation != null) {
                    presentationIds.add(presentation.getId());
                }
            }
        }
        copyMask.setEditorPresentationIds(presentationIds);
        setEditMask(copyMask);
        entity = null;
        updateButtons();
    }

    public void setEditorPresentation(final Id classId, final Id presentationId) {
        final EditMaskRef copyMask = getEditMaskRef();
        copyMask.setEditorPresentationId(presentationId);
        setEditMask(copyMask);
        updateButtons();
    }

    public void setEditorPresentation(RadEditorPresentationDef presentation) {
        final EditMaskRef copyMask = getEditMaskRef();
        copyMask.setEditorPresentationId(presentation==null ? null : presentation.getId());
        setEditMask(copyMask);
        entity = null;
        updateButtons();
    }

    public final boolean isSelectButtonHidden() {
        return !showSelectBtn;
    }

    public final void setSelectButtonHidden(boolean hidden) {
        this.showSelectBtn = !hidden;
        updateButtons();
    }

    public final boolean isEditButtonHidden() {
        return !showEditBtn;
    }

    public final void setEditButtonHidden(boolean hidden) {
        this.showEditBtn = !hidden;
        updateButtons();
    }

    @Override
    public void setMandatory(boolean mandatory) {
        super.setMandatory(mandatory);
        updateButtons();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        updateButtons();
    }

    public void select() {
        final GroupModel groupModel = getGroupModel();
        if (groupModel==null){
            if (getSelectorPresentation()==null){
                throw new IllegalStateException("selector presentation is not defined");
            }
            //TODO trace message
            return;
        }
        try {
            final SelectEntityDialog dialog = new SelectEntityDialog(groupModel, !isMandatory());
            beforeSelectorOpened.emit();
            if (DialogCode.resolve(dialog.exec()).equals(DialogCode.Accepted)) {
                entity = dialog.selectedEntity;
                setValue(entity == null ? null : new Reference(entity));
                updateButtons();
                updateHistory();
            }
            selectorClosed.emit();
        } finally {
            groupModel.clean();
        }
    }

    @Override
    public void clear() {
        super.clear();
        updateButtons();
    }

    public void edit() {
        if (getEditorPresentationIds() == null) {
            throw new IllegalStateException("none editor presentation is defined");
        }
        if (getValue() == null) {
            throw new IllegalStateException("value is not defined");
        }
        EntityModel model = getEntityModel();
        if (model == null) {
            return;
        }
        if (model.getContext() instanceof IContext.SelectorRow) {//RADIX-7956            
            model = createEntityModel();
            if (model == null) {
                return;
            }
            if (entity != null) {
                model.activateCopy(entity);
            }
        }
        final EntityEditorDialog dialog = new EntityEditorDialog(model);
        beforeEditorOpened.emit();
        if (DialogCode.resolve(dialog.exec()).equals(DialogCode.Accepted)) {
            entity = dialog.getEntityModel();
            blockSignals(true);
            setValue(new Reference(entity));
            blockSignals(false);
        }
        editorClosed.emit();
    }

    private EntityModel createEntityModel() {
        if (getValue() == null || getValue().getPid() == null) {
            return null;
        }
        final Collection<Id> editorPresentationIds = getEditorPresentationIds();
        if (editorPresentationIds.isEmpty()) {
            return null;
        }
        try {
            Id ownerClassId = null;
            EditMaskRef copyMask = getEditMaskRef();
            for (Id editorPresentationId : copyMask.getEditorPresentationIds()) {
                if (editorPresentationId != null) {
                    RadEditorPresentationDef presentation = getEnvironment().getApplication().getDefManager().getEditorPresentationDef(editorPresentationId);
                    ownerClassId = presentation.getOwnerClassId();
                }
            }

            final EntityModel model = 
                EntityModel.openContextlessModel(getEnvironment(), getValue().getPid(), ownerClassId, editorPresentationIds, WidgetUtils.findNearestModel(this));

            ((IContext.Entity) model.getContext()).setPresentationChangedHandler(this);
            if (isReadOnly()) {
                model.getRestrictions().setUpdateRestricted(true);
            }
            return model;
        } catch (ServiceClientException ex) {
            getEnvironment().processException(ex);
        } catch (InterruptedException ex) {
        }
        return null;
    }

    private EditMaskRef getEditMaskRef() {
        return (EditMaskRef) getEditMask();
    }

    private RadSelectorPresentationDef getSelectorPresentation() {
        EditMaskRef copyMask = getEditMaskRef();
        if (copyMask == null) {
            return null;
        }
        try {
            if (copyMask.getSelectorPresentationId() != null) {
                RadSelectorPresentationDef selectorPresentation = getEnvironment().getApplication().getDefManager().getSelectorPresentationDef(copyMask.getSelectorPresentationId());
                return selectorPresentation;
            } else {
                return null;
            }
        } catch (DefinitionNotFoundError ex) {
            return null;
        }

    }

    public GroupModel getGroupModel() {
        if (group == null) {
            if (getSelectorPresentation() == null) {
                return null;
            }
            final Model holderModel = WidgetUtils.findNearestModel(this);
            if (holderModel==null){
                group = GroupModel.openTableContextlessSelectorModel(getEnvironment(), getSelectorPresentation());
            }else{
                group = GroupModel.openTableContextlessSelectorModel(holderModel, getSelectorPresentation());
            }      
        }
        if (!setupGroupModel(group)) {
            return null;
        }
        return group;
    }
    
    private void invalidateGroupModel(){
        group = null;
    }

    private boolean setupGroupModel(final GroupModel group){
        EditMaskRef copyMask = getEditMaskRef();
        group.applyEditMaskSettings(copyMask);
        
        if (selectionController != null) {
            group.setEntitySelectionController(selectionController);
        }
        
        if (selectionListener != null){
            group.addSelectionListener(selectionListener);
        }
        return true;
    }

    @Override
    public void setValue(final Reference value) {
        final boolean sameValue = Reference.exactlyMatch(getValue(), value);
        super.setValue(value);
        if (entity != null && (value == null || !entity.getPid().equals(value.getPid()))) {
            entity = null;
        }
        if (!sameValue){
            updateButtons();
            refreshTextOptions();
            updateComboBoxLook();
        }
    }

    @Override
    protected void customEvent(QEvent event) {
        super.customEvent(event); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void applyTextOptions(ExplorerTextOptions options) {
        super.applyTextOptions(options); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    @Override
    protected ValidationResult calcValidationResult(final Reference value) {
        if (getEditMask() != null) {
            return getEditMaskRef().validate(getEnvironment(), value);
        }
        return value == null || !value.isBroken() ? ValidationResult.ACCEPTABLE : ValidationResult.INVALID;
    }

    public void reread() {
        EditMaskRef copyMask = getEditMaskRef();
        if (copyMask.getEditorPresentationIds() != null && !copyMask.getEditorPresentationIds().isEmpty() && value != null) {
            final EntityModel model = getEntityModel();
            try {
                model.read();
                setValue(new Reference(model.getPid(), model.getTitle()));
            } catch (ServiceClientException ex) {
                model.showException(ex);
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    public EntityModel onChangePresentation(final RawEntityModelData rawData,
            final Id newPresentationClassId,
            final Id newPresentationId) {
        setUpdatesEnabled(false);
        try {
            final IContext.Entity context = (IContext.Entity) entity.getContext();
            final RadEditorPresentationDef presentation = getEnvironment().getApplication().getDefManager().getEditorPresentationDef(newPresentationId);
            final EntityModel model = presentation.createModel(context);
            model.activate(rawData);
            entity = model;
            return entity;
        } finally {
            setUpdatesEnabled(true);
        }
    }

    private void updateButtons() {
        if (canSelectEntityObject()){
            if (getEditMaskRef().isUseDropDownList()){
                selectBtn.setVisible(false);
            }else{
                selectBtn.setVisible(true);
            }            
        }else{
            selectBtn.setVisible(false);
        }
        if (listModel!=null){
            setListModel(null);//clear items in combobox and hide drop down button
            dropDownBtnVisible = false;
            listModel = null;
        }
        updateComboItems(false);
        if (canOpenEntityObjectView()){
            editBtn.setVisible(true);
            if (isReadOnly() || getValue().isModificationRestricted()) {
                editBtn.setToolTip(getEnvironment().getMessageProvider().translate("ValRefEditor", "View Object"));
                editBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.VIEW));
            } else {
                editBtn.setToolTip(getEnvironment().getMessageProvider().translate("ValRefEditor", "Edit Object"));
                editBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.EDITOR));
            }
        }else{
            editBtn.setVisible(false);
        }
        editingHistoryBtn.setVisible(!isReadOnly() && editingHistory != null && !editingHistory.isEmpty());
    }

    @Override
    protected void onMouseClick() {        
        if (QApplication.keyboardModifiers().value()==Qt.KeyboardModifier.ControlModifier.value()
            && isEnabled() && isEmbeddedWidgetsVisible()
            && editBtn.isVisible() && editBtn.isEnabled() && canOpenEntityObjectView()){
            edit();
        }else{
            super.onMouseClick();
        }
    }        

    private boolean canSelectEntityObject() {
        return showSelectBtn && !isReadOnly() && isGroupModelAccessible();
    }
    
    protected boolean isGroupModelAccessible(){
        return getGroupModel()!=null;
    }

    private boolean canOpenEntityObjectView() {
        boolean validEnvironment = false;
        for (Id id : getEditorPresentationIds()) {
            if (getEnvironment().getApplication().getDefManager().getEditorPresentationDef(id).getRuntimeEnvironmentType() == ERuntimeEnvironmentType.COMMON_CLIENT
                    || getEnvironment().getApplication().getDefManager().getEditorPresentationDef(id).getRuntimeEnvironmentType() == ERuntimeEnvironmentType.EXPLORER) {
                validEnvironment = true;
            }
        }
        return !getEditorPresentationIds().isEmpty()
                && getValue() != null
                && getValue().isValid()
                && !getValue().isEditorRestricted()
                && validEnvironment
                && showEditBtn;
    }

    private List<Id> getEditorPresentationIds() {
        EditMaskRef copyMask = getEditMaskRef();
        List<Id> editorPresentatonIds = copyMask.getEditorPresentationIds();
        if (editorPresentatonIds == null || editorPresentatonIds.isEmpty()) {
            return Collections.emptyList();
        } else {
            return editorPresentatonIds;
        }
    }

    @Override
    public void setEditingHistory(final IEditingHistory history) {
        if (history == null) {
            super.setEditingHistory(null);
        } else if (history instanceof RefEditingHistory) {
            this.editingHistory = history;
            final QCompleter completer = new QCompleter();
            final QStringListModel model = new QStringListModel();
            completer.setModel(model);
            completer.setCompletionMode(QCompleter.CompletionMode.UnfilteredPopupCompletion);
            completer.highlighted.disconnect();
            completer.highlightedIndex.disconnect();
            completer.activatedIndex.connect(this, "onSelectCompletion(QModelIndex)");
            getLineEdit().setCompleter(completer);
        }
        editingHistoryBtn.setVisible(!isReadOnly() && editingHistory != null && !editingHistory.isEmpty());
    }

    @Override
    protected QAbstractItemModel getHistoryModel() {
        return completionModelHistory;
    }

    @Override
    protected void onSelectCompletion(final QModelIndex filterModelIndex) {
        if (filterModelIndex != null && editingHistory instanceof RefEditingHistory) {
            final Reference currentValue = ((RefEditingHistory) editingHistory).getReference(filterModelIndex.row());
            setOnlyValue(currentValue);
            if (!inModificationState()) {
                updateHistory();
            }
            refresh();
        }
        setFocus();
    }

    @Override
    public void updateHistory() {
        if (editingHistory != null && getValue() != null && getValue().isValid()) {
            final RefEditingHistory history = (RefEditingHistory) editingHistory;
            history.addReference(getValue());
        }
    }

    private void updateModel(final RefEditingHistory history) {
        final QCompleter completer = getLineEdit().completer();
        final QStringListModel model = (QStringListModel) completer.model();
        final List<String> referencesToShow = new LinkedList<>();
        final List<Reference> refs = history.getReferences();
        for (Reference r : refs) {
            referencesToShow.add(getStringToShow(r));
        }
        model.setStringList(referencesToShow);
        completer.setModel(model);
    }

    @Override
    void setCompleterUnfiltered(boolean flag) {
        if (flag && editingHistory != null) {
            final RefEditingHistory history = (RefEditingHistory) editingHistory;
            updateModel(history);
        }
    }

    @Override
    protected List<String> getPredefinedValuesTitles() {
        final List<Reference> predefValues = new ArrayList<>(getPredefinedValues());

        if (!predefinedValuesAreValid) {
            final RadSelectorPresentationDef presentationDef = getSelectorPresentation();
            if (presentationDef==null){
                //TODO trace message
                return Collections.<String>emptyList();
            }
            final EntityObjectTitlesProvider titlesProvider =
                    new EntityObjectTitlesProvider(getEnvironment(), presentationDef.getTableId(), presentationDef.getId());

            for (Reference r : predefValues) {
                titlesProvider.addEntityObjectReference(r);
            }
            try {
                final EntityObjectTitles titles = titlesProvider.getTitles();
                for (int i = 0; i < predefValues.size(); i++) {
                    final Pid objPid = predefValues.get(i).getPid();
                    if (titles.isEntityObjectAccessible(objPid)) {
                        predefValues.set(i, titles.getEntityObjectReference(objPid));
                        predefinedValuesTitles.add(titles.getEntityObjectTitle(objPid));
                    } else {
                        predefValues.remove(i);
                    }
                }
            } catch (InterruptedException ex) {
                return Collections.<String>emptyList();
            } catch (ServiceClientException ex) {
                getEnvironment().getTracer().error(ex);
                return Collections.<String>emptyList();
            }
        }
        predefinedValuesAreValid = true;
        return Collections.unmodifiableList(predefinedValuesTitles);
    }

    @Override
    public void setPredefinedValues(final List<Reference> predefValues) {
        super.setPredefinedValues(predefValues);
        predefinedValuesAreValid = false;
        predefinedValuesTitles.clear();
    }

    @Override
    protected boolean isButtonCanChangeValue(QAbstractButton button) {
        if (button == editBtn) {//NOPMD
            return false;
        } else if (button == selectBtn || button == editingHistoryBtn) {//NOPMD
            return !isReadOnly();
        } else {
            return super.isButtonCanChangeValue(button);
        }
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        if (editMask!=null){
            final EditMaskRef currentMask = getEditMaskRef();
            final EditMaskRef newMask = (EditMaskRef) EditMaskRef.newCopy(editMask);
            final boolean presentationChanged;
            if (currentMask == null) {
                presentationChanged = true;
            }else{
                presentationChanged = 
                    !Utils.equals(currentMask.getSelectorPresentationId(), newMask.getSelectorPresentationId());
            }
            if (presentationChanged) {
                setValue(null);
            }
            super.setEditMask(newMask);
            if (!newMask.sameSelection(currentMask)){
                invalidateGroupModel();
            }
            updateButtons();
   
        }
    }

    @Override
    protected final void updateComboItems(final boolean updateStyleSheet) {
        final boolean canUseDropDownList = getEditMaskRef().isUseDropDownList() && canSelectEntityObject();
        if (dropDownBtnVisible!=canUseDropDownList){
            if (canUseDropDownList){
                addItems(DUMMY_ITEMS);
            }else{
                clearItems();                
            }
            updateComboBoxLook();
            dropDownBtnVisible = canUseDropDownList;
        }
    }        
    
    private void updateComboBoxLook(){
        updateComboBoxLook(-1, true, true);
    }

    @Override
    protected final int getMaxItemsInPopup() {
        return -1;
    }
    
    @Override
    protected final void onActivatedIndex(final int index) {
        if (listModel !=null && index>=0 && index<listModel.rowCount(null)){
            final EntityModel entityModel = listModel.getEntityModel(index);
            if (entityModel!=null){
                onSelectValueFromDropDownList(new Reference(entityModel));
            }
        }
    }
    
    protected void onSelectValueFromDropDownList(final Reference value){
        setValue(value);
    }

    @Override
    protected boolean beforeShowPopup() {
        final GroupModel groupModel = getGroupModel();
        if (groupModel==null){
            return false;
        }
        listModel = new EntitiesListModel(groupModel,this,true);            
        updateListModelTextOptions(listModel);
        setListModel(listModel);
        return super.beforeShowPopup();
    }
    
    @Override
    protected void afterShowPopup() {
        getLineEdit().setText(getStringToShow(getValue()));
        getLineEdit().home(false);
        super.afterShowPopup();
    }    
    
    @Override
    protected void afterHidePopup() {
        if (listModel!=null){
            listModel.cancelAsyncDataLoading();
            listModel.setDataLoadingBlocked(true);            
        }        
        updateComboBoxLook();
        super.afterHidePopup();
    }    
    
    private void updateListModelTextOptions(final EntitiesListModel listModel){
        final EnumSet<ETextOptionsMarker> markers = getTextOptionsMarkers();
        markers.remove(ETextOptionsMarker.UNDEFINED_VALUE);
        markers.remove(ETextOptionsMarker.MANDATORY_VALUE);
        markers.remove(ETextOptionsMarker.READONLY_VALUE);
        markers.remove(ETextOptionsMarker.OVERRIDDEN_VALUE);
        markers.remove(ETextOptionsMarker.INHERITED_VALUE);
        final ExplorerTextOptions listOptions = calculateTextOptions(markers);
        listModel.setTextOptions(listOptions);        
    }
 
    
}