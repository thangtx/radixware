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
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QCompleter;
import com.trolltech.qt.gui.QDialog.DialogCode;
import com.trolltech.qt.gui.QStringListModel;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.EntityObjectTitles;
import org.radixware.kernel.common.client.eas.EntityObjectTitlesProvider;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
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
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.RefEditingHistory;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.dialogs.EntityEditorDialog;
import org.radixware.kernel.explorer.dialogs.SelectEntityDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class ValRefEditor extends ValEditor<Reference> implements IPresentationChangedHandler {

    private IEntitySelectionController selectionController;
    private GroupModel.SelectionListener selectionListener;
    private GroupModel group = null;
    private EntityModel entity = null;
    private final QToolButton selectBtn, editBtn;
    private boolean predefinedValuesAreValid = false;
    private boolean showSelectBtn = true;
    private boolean showEditBtn = true;
    private final List<String> predefinedValuesTitles = new ArrayList<>();
    private final QStringListModel completionModelHistory = new QStringListModel();
    public final QSignalEmitter.Signal0 beforeEditorOpened = new QSignalEmitter.Signal0();
    public final QSignalEmitter.Signal0 editorClosed = new QSignalEmitter.Signal0();
    public final QSignalEmitter.Signal0 beforeSelectorOpened = new QSignalEmitter.Signal0();
    public final QSignalEmitter.Signal0 selectorClosed = new QSignalEmitter.Signal0();

    @SuppressWarnings("LeakingThisInConstructor")
    public ValRefEditor(IClientEnvironment environment, QWidget parent, final boolean mandatory, final boolean readonly, final boolean showSelectObjectBtn, final boolean showEditObjectBtn) {
        super(environment, parent, new EditMaskRef(), mandatory, readonly);
        {
            final QAction action = new QAction(this);
            action.triggered.connect(this, "select()");
            action.setToolTip(environment.getMessageProvider().translate("ValRefEditor", "Select Object"));
            action.setIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.SELECTOR));
            selectBtn = addButton(null, action);
        }
        {
            final QAction action = new QAction(this);
            action.triggered.connect(this, "edit()");
            editBtn = addButton(null, action);
        }
        showSelectBtn = showSelectObjectBtn;
        showEditBtn = showEditObjectBtn;
        getLineEdit().setReadOnly(true);//NOPMD this method is final
        updateButtons();
    }

    public ValRefEditor(IClientEnvironment environment, QWidget parent, final boolean mandatory, final boolean readonly) {
        this(environment, parent, mandatory, readonly, true, true);
    }

    public ValRefEditor(IClientEnvironment environment, QWidget parent) {
        this(environment, parent, true, false);
    }

    public ValRefEditor(IClientEnvironment environment, QWidget parent, EditMaskRef mask, final boolean mandatory, final boolean readonly) {
        super(environment, parent, mask==null ? new EditMaskRef() : mask , mandatory, readonly);
        {
            final QAction action = new QAction(ValRefEditor.this);
            action.triggered.connect(ValRefEditor.this, "select()");
            action.setToolTip(environment.getMessageProvider().translate("ValRefEditor", "Select Object"));
            action.setIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.SELECTOR));
            selectBtn = addButton(null, action);
        }
        {
            final QAction action = new QAction(ValRefEditor.this);
            action.triggered.connect(ValRefEditor.this, "edit()");
            editBtn = addButton(null, action);
        }
        getLineEdit().setReadOnly(true);
        updateButtons();
    }

    public ValRefEditor(IClientEnvironment environment, QWidget parent, EditMaskRef mask, final boolean mandatory, final boolean readonly, final boolean showSelectObjectBtn, final boolean showEditObjectBtn) {
        super(environment, parent, mask==null ? new EditMaskRef() : mask , mandatory, readonly);
        {
            final QAction action = new QAction(ValRefEditor.this);
            action.triggered.connect(ValRefEditor.this, "select()");
            action.setToolTip(environment.getMessageProvider().translate("ValRefEditor", "Select Object"));
            action.setIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.SELECTOR));
            selectBtn = addButton(null, action);
        }
        {
            final QAction action = new QAction(ValRefEditor.this);
            action.triggered.connect(ValRefEditor.this, "edit()");
            editBtn = addButton(null, action);
        }
        this.showSelectBtn = showSelectObjectBtn;
        this.showEditBtn = showEditObjectBtn;
        getLineEdit().setReadOnly(true);
        updateButtons();
    }

    public void setSelectorPresentation(final Id classId, final Id presentationId) {
        final EditMaskRef copyMask = getCurrentEditMaskCopy();
        copyMask.setSelectorPresentationId(presentationId);
        copyMask.setCondition((SqmlExpression) null);
        setEditMask(copyMask);
        group = null;
        setValue(null);
        updateButtons();
    }

    public void setSelectorPresentation(final RadSelectorPresentationDef presentation) {
        final EditMaskRef copyMask = getCurrentEditMaskCopy();
        copyMask.setSelectorPresentationId(presentation==null ? null : presentation.getId());
        copyMask.setCondition((SqmlExpression) null);        
        
        setEditMask(copyMask);
        group = null;
        setValue(null);
        updateButtons();
    }

    public void setCondition(final org.radixware.schemas.xscml.Sqml condition) {
        final EditMaskRef copyMask = getCurrentEditMaskCopy();
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
        final EditMaskRef copyMask = getCurrentEditMaskCopy();
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
        final EditMaskRef copyMask = getCurrentEditMaskCopy();
        copyMask.setDefaultFilterId(filterId);
        setEditMask(copyMask);
    }

    public final void setDefaultSortingId(final Id sortingId, final Id filterId) {
        final EditMaskRef copyMask = (EditMaskRef) EditMaskRef.newCopy(getEditMask());
        copyMask.setDefaultSortingId(sortingId, filterId);
        setEditMask(copyMask);

    }

    public final boolean isDefinedDefaultFilterId() {
        final EditMaskRef copyMask = (EditMaskRef) getEditMask();
        return copyMask.isDefinedDefaultFilter();
    }

    public final Id getDefaultFilterId() {
        final EditMaskRef copyMask = (EditMaskRef) getEditMask();
        return copyMask.getDefaultFilterId();
    }

    public final boolean isDefinedDefaultSortingId(final Id filterId) {
        final EditMaskRef copyMask = (EditMaskRef) getEditMask();
        return copyMask.isDefinedDefaultSorting(filterId);
    }

    public final Id getDefaultSortingId(final Id filterId) {
        final EditMaskRef copyMask = (EditMaskRef) getEditMask();
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
        final EditMaskRef copyMask = getCurrentEditMaskCopy();
        copyMask.setEditorPresentationIds(presentationIds);
        setEditMask(copyMask);
        updateButtons();
    }

    public void setEditorPresentations(final List<RadEditorPresentationDef> presentations) {
        final EditMaskRef copyMask = getCurrentEditMaskCopy();
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
        final EditMaskRef copyMask = (EditMaskRef) EditMaskRef.newCopy(getEditMask());
        copyMask.setEditorPresentationId(presentationId);
        setEditMask(copyMask);
        updateButtons();
    }

    public void setEditorPresentation(RadEditorPresentationDef presentation) {
        final EditMaskRef copyMask = (EditMaskRef) EditMaskRef.newCopy(getEditMask());
        copyMask.setEditorPresentationId(presentation==null ? null : presentation.getId());
        setEditMask(copyMask);
        entity = null;
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
            if (groupModel != null) {
                groupModel.clean();
            }
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
            EditMaskRef copyMask = (EditMaskRef) EditMaskRef.newCopy(getEditMask());
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

    private EditMaskRef getCurrentEditMaskCopy() {
        return (EditMaskRef) getEditMask();
    }

    private RadSelectorPresentationDef getSelectorPresentation() {
        EditMaskRef copyMask = getCurrentEditMaskCopy();
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
                throw new IllegalStateException("selector presentation is not defined");
            }
            final Model holderModel = WidgetUtils.findNearestModel(this);
            if (holderModel==null){
                group = GroupModel.openTableContextlessSelectorModel(getEnvironment(), getSelectorPresentation());
            }else{
                group = GroupModel.openTableContextlessSelectorModel(holderModel, getSelectorPresentation());
            }      
        }
        try {
            if (!setupGroupModel(group)) {
                return null;
            }
        } catch (InterruptedException ex) {
            return null;
        }
        return group;
    }

    private boolean setupGroupModel(final GroupModel group) throws InterruptedException {
        EditMaskRef copyMask = getCurrentEditMaskCopy();
        org.radixware.schemas.xscml.Sqml condition = copyMask.getCondition();
        if (condition != null) {
            try {
                group.setCondition(condition);
            } catch (ObjectNotFoundError err) {
                getEnvironment().processException(err);
                return false;
            } catch (ServiceClientException ex) {
                group.showException(ex);
                return false;
            }
        }
        boolean customInitialFilter = copyMask.isDefinedDefaultFilter();
        if (customInitialFilter) {
            group.getFilters().setDefaultFilterId(copyMask.getDefaultFilterId());
        }

        Map<Id, Id> defaultSortingIdByFilterId = copyMask.getDefaultSortingIdByFilterId();
        if (defaultSortingIdByFilterId != null && !defaultSortingIdByFilterId.isEmpty()) {
            final Sortings sortings = group.getSortings();
            for (Map.Entry<Id, Id> entry : copyMask.getDefaultSortingIdByFilterId().entrySet()) {
                sortings.setDefaultSortingId(entry.getValue(), entry.getKey());
            }
        }

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
        super.setValue(value);
        if (entity != null && (value == null || !entity.getPid().equals(value.getPid()))) {
            entity = null;
        }
        updateButtons();
        refreshTextOptions();
    }

    @Override
    protected ValidationResult calcValidationResult(final Reference value) {
        if (getEditMask() != null) {
            return getCurrentEditMaskCopy().validate(getEnvironment(), value);
        }
        return value == null || !value.isBroken() ? ValidationResult.ACCEPTABLE : ValidationResult.INVALID;
    }

    public void reread() {
        EditMaskRef copyMask = getCurrentEditMaskCopy();
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

    //Ð¾Ð±Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ðµ ÐºÐ½Ð¾Ð¿Ð¾Ðº
    private void updateButtons() {
        selectBtn.setVisible(canSelectEntityObject());
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

    private boolean canSelectEntityObject() {
        return showSelectBtn && getSelectorPresentation() != null && !isReadOnly();
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
        EditMaskRef copyMask = getCurrentEditMaskCopy();
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
            final EntityObjectTitlesProvider titlesProvider =
                    new EntityObjectTitlesProvider(getEnvironment(), getSelectorPresentation().getTableId(), getSelectorPresentation().getId());

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
    public void setEditMask(EditMask editMask) {
        EditMaskRef currentMask = getCurrentEditMaskCopy();
        EditMaskRef newMask = (EditMaskRef) EditMaskRef.newCopy(editMask);
        if (currentMask != null) {
            if (!Utils.equals(currentMask.getSelectorPresentationId(), newMask.getSelectorPresentationId())) {
                newMask.setCondition((SqmlExpression) null);
                group = null;
                setValue(null);
            }
        }
        super.setEditMask(newMask);
        updateButtons();
    }
}