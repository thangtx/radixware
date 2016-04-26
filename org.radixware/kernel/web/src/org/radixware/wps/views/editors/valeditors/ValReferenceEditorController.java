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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.IEntitySelectionController;
import org.radixware.kernel.common.client.models.IPresentationChangedHandler;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.RawEntityModelData;
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.common.client.types.EditingHistoryException;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.types.RefEditingHistory;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.DropDownEditHistoryDelegate;
import org.radixware.wps.rwt.InputBox.ValueController;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.utils.UIObjectUtils;
import org.radixware.wps.views.dialog.EntityEditorDialog;
import org.radixware.wps.views.dialog.SelectEntityDialog;


public class ValReferenceEditorController extends InputBoxController<Reference, EditMaskRef> implements IPresentationChangedHandler {

    private ToolButton selectButton;
    private ToolButton editButton;
    private IEntitySelectionController selectionController;
    private GroupModel.SelectionListener selectionListener;
    private EntityModel entity;
    private GroupModel group;
    private DropDownEditHistoryDelegate<Reference> dropDownHistoryDelegate;
    private EValType valType;

    public ValReferenceEditorController(IClientEnvironment env) {
        super(env);
        selectButton = new ToolButton();
        selectButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(IButton source) {
                select();
            }
        });

        editButton = new ToolButton();
        editButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(IButton source) {
                editEntity();
            }
        });
        editButton.setToolTip(getEnvironment().getMessageProvider().translate("PropRefEditor", "Edit Object"));
        editButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Definitions.EDITOR));
        editButton.setObjectName("tbEdit");

        addButton(selectButton);
        addButton(editButton);
        setEditMask(new EditMaskRef());
    }

    protected String getSelectButtonToolTip() {
        return getEnvironment().getMessageProvider().translate("ValReferenceEditorController", "Select Object");
    }

    protected ClientIcon getSelectButtonIcon() {
        return ClientIcon.Definitions.SELECTOR;
    }

    @Override
    protected ValueController<Reference> createValueController() {
        return null;
    }

    public void editEntity() {
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
        beforeEditorOpened();
        if (dialog.execDialog() == DialogResult.ACCEPTED) {
            entity = dialog.getEntityModel();
            setValue(new Reference(entity));
            updateHistory();
        }
        afterEditorClosed();
    }

    public void beforeEditorOpened() {
        //empty implementation
        //for override only
    }

    public void afterEditorClosed() {
        //empty implementation
        //for override only
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

    public void setSelectorPresentation(final Id classId, final Id presentationId) {
        final EditMaskRef copyMask = (EditMaskRef) EditMaskRef.newCopy(getEditMask());
        copyMask.setSelectorPresentationId(presentationId);
        copyMask.setCondition((SqmlExpression) null);
        setEditMask(copyMask);
        group = null;
        setValue(null);//updateButtons called from setValue
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

    public void setEditorPresentation(final Id classId, final Id presentationId) {
        final EditMaskRef copyMask = getCurrentEditMaskCopy();
        copyMask.setEditorPresentationId(presentationId);
        setEditMask(copyMask);        
        updateButtons();
    }

    @Override
    public void updateHistory() {
        if (editingHistory != null && getValue() != null && getValue().isValid()) {
            final RefEditingHistory history = (RefEditingHistory) editingHistory;

            history.addReference(getValue());
            dropDownHistoryDelegate.updateItems(getInputBox(), editingHistory, valType);
        }
        try {
            if (editingHistory != null) {
                editingHistory.flush();
            }
        } catch (EditingHistoryException ex) {
            getEnvironment().getTracer().error(ex);
        }
    }

    @Override
    public void setEditHistory(final IEditingHistory history, final EValType type) {
        valType = ValueConverter.serverValType2ClientValType(type);
        if (history == null) {
            super.setEditHistory(null, valType);
        } else if (history instanceof RefEditingHistory) {

            editingHistory = history;
            if (dropDownHistoryDelegate == null) {
                dropDownHistoryDelegate = new DropDownEditHistoryDelegate<>(getInputBox(), history, type);
                getInputBox().addDropDownDelegate(dropDownHistoryDelegate);
            } else {
                dropDownHistoryDelegate.updateItems(getInputBox(), history, type);
            }
            updateHistory();
        }
    }

    @Override
    public IEditingHistory getEditHistory() {
        return editingHistory;
    }

    public void setEditorPresentation(final RadEditorPresentationDef presentation) {
        final EditMaskRef copyMask = getCurrentEditMaskCopy();
        copyMask.setEditorPresentationId(presentation==null ? null : presentation.getId());
        setEditMask(copyMask);
        entity = null;
        updateButtons();
    }

    @Override
    public void setValue(Reference value) {
        super.setValue(value);
        updateButtons();
    }

    public void setCondition(org.radixware.schemas.xscml.Sqml condition) {
        EditMaskRef copyMask = getCurrentEditMaskCopy();
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

    public void setCondition(SqmlExpression condition) {
        EditMaskRef copyMask = getCurrentEditMaskCopy();
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

    public void setEntitySelectionController(final IEntitySelectionController controller) {
        selectionController = controller;
        if (group != null && controller!=null) {
            group.setEntitySelectionController(controller);
        }
    }
    
    public void setEntitySelectionListener(final GroupModel.SelectionListener listener) {
        selectionListener = listener;
        if (group != null && listener!=null) {
            group.addSelectionListener(listener);
        }
    }    

    public final void setDefaultFilterId(final Id filterId) {
        EditMaskRef copyMask = (EditMaskRef) EditMaskRef.newCopy(getEditMask());
        copyMask.setDefaultFilterId(filterId);
        setEditMask(copyMask);
    }

    public final void setDefaultSortingId(final Id sortingId, final Id filterId) {
        EditMaskRef copyMask = (EditMaskRef) EditMaskRef.newCopy(getEditMask());
        copyMask.setDefaultSortingId(sortingId, filterId);
        setEditMask(copyMask);
    }

    public final boolean isDefinedDefaultFilterId() {
        EditMaskRef copyMask = getEditMask();
        return copyMask.isDefinedDefaultFilter();
    }

    public final Id getDefaultFilterId() {
        EditMaskRef copyMask = getEditMask();
        return copyMask.getDefaultFilterId();
    }

    public final boolean isDefinedDefaultSortingId(final Id filterId) {
        EditMaskRef copyMask =  getEditMask();
        return copyMask.isDefinedDefaultSorting(filterId);
    }

    public final Id getDefaultSortingId(final Id filterId) {
        EditMaskRef copyMask = getEditMask();
        return copyMask.getDefaultSortingId(filterId);
    }

    @Override
    public void setMandatory(boolean mandatory) {
        super.setMandatory(mandatory);
        updateButtons();
    }

    @Override
    protected void afterChangeReadOnly() {
        super.afterChangeReadOnly();
        updateButtons();
    }

    public void select() {
        final GroupModel groupModel = getGroupModel();
        try {
            final SelectEntityDialog dialog = new SelectEntityDialog(((WpsEnvironment) getEnvironment()).getDialogDisplayer(), groupModel, !isMandatory());
            beforeSelectorOpened();
            if (dialog.execDialog() == DialogResult.ACCEPTED) {
                entity = dialog.selectedEntity;
                setValue(entity == null ? null : new Reference(entity));
                updateButtons();
                updateHistory();
            }
        } finally {
            if (groupModel != null) {
                groupModel.clean();
            }
        }
    }

    public void beforeSelectorOpened() {
        //for override only
    }

    public GroupModel getGroupModel() {
        if (group == null) {
            if (getSelectorPresentation() == null) {
                throw new IllegalStateException("selector presentation is not defined");
            }
            final Model holderModel = UIObjectUtils.findNearestModel(this.getInputBox());
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

    public EntityModel getEntityModel() {
        if (entity == null) {
            entity = createEntityModel();
        }
        return entity;
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
            EditMaskRef copyMask = getCurrentEditMaskCopy();
            for (Id editorPresentationId : copyMask.getEditorPresentationIds()) {
                if (editorPresentationId != null) {
                    RadEditorPresentationDef presentation = getEnvironment().getApplication().getDefManager().getEditorPresentationDef(editorPresentationId);
                    ownerClassId = presentation.getOwnerClassId();
                }
            }

            final EntityModel model = 
                EntityModel.openContextlessModel(getEnvironment(), getValue().getPid(), ownerClassId, editorPresentationIds, UIObjectUtils.findNearestModel(getInputBox()));
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

    private void updateButtons() {
        selectButton.setToolTip(getSelectButtonToolTip());
        selectButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(getSelectButtonIcon()));
        selectButton.setVisible(isSelectButtonVisible());
        editButton.setVisible(isEditButtonVisible());
        if (isViewOnlyOnEdit()) {
            editButton.setToolTip(getEnvironment().getMessageProvider().translate("ValRefEditor", "View Object"));
            editButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.VIEW));
        } else {
            editButton.setToolTip(getEnvironment().getMessageProvider().translate("ValRefEditor", "Edit Object"));
            editButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Definitions.EDITOR));
        }
    }

    protected boolean isEditButtonVisible() {
        boolean validEnvironment = false;
        for (Id id : getEditorPresentationIds()) {
            if (getEnvironment().getApplication().getDefManager().getEditorPresentationDef(id).getRuntimeEnvironmentType() == ERuntimeEnvironmentType.COMMON_CLIENT
                    || getEnvironment().getApplication().getDefManager().getEditorPresentationDef(id).getRuntimeEnvironmentType() == ERuntimeEnvironmentType.WEB) {
                validEnvironment = true;
            }
        }
        return !getEditorPresentationIds().isEmpty()
                && getValue() != null
                && getValue().isValid()
                && !getValue().isEditorRestricted()
                && validEnvironment;
    }

    private Collection<Id> getEditorPresentationIds() {
        EditMaskRef copyMask = getCurrentEditMaskCopy();
        List<Id> editorPresentatonIds = copyMask.getEditorPresentationIds();
        if (editorPresentatonIds == null || editorPresentatonIds.isEmpty()) {
            return Collections.emptyList();
        } else {
            return editorPresentatonIds;
        }
    }

    protected boolean isViewOnlyOnEdit() {
        return isReadOnly() 
               || (getValue()!=null && !getValue().isBroken() && getValue().isModificationRestricted());
    }

    protected boolean isSelectButtonVisible() {
        return getSelectorPresentation() != null && !isReadOnly();
    }

    private RadSelectorPresentationDef getSelectorPresentation() {
        EditMaskRef copyMask = getCurrentEditMaskCopy();
        if (copyMask!=null && copyMask.getSelectorPresentationId() != null) {
            RadSelectorPresentationDef selectorPresentation = getEnvironment().getApplication().getDefManager().getSelectorPresentationDef(copyMask.getSelectorPresentationId());
            return selectorPresentation;
        } else {
            return null;
        }
    }

    @Override
    public EntityModel onChangePresentation(final RawEntityModelData rawEntityModelData,
            final Id newPresentationClassId,
            final Id newPresentationId) {

        final IContext.Entity context = (IContext.Entity) entity.getContext();
        final RadEditorPresentationDef presentation = getEnvironment().getApplication().getDefManager().getEditorPresentationDef(newPresentationId);
        final EntityModel model = presentation.createModel(context);
        model.activate(rawEntityModelData);
        entity = model;
        return entity;
    }

    public void reread() {
        EditMaskRef copyMask = getCurrentEditMaskCopy();
        if (copyMask!=null && copyMask.getEditorPresentationIds() != null && !copyMask.getEditorPresentationIds().isEmpty() && getValue() != null) {
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
    public final void setEditMask(EditMaskRef editMask) {
        EditMaskRef currentMask = getCurrentEditMaskCopy();
        EditMaskRef newMask = (EditMaskRef) EditMaskRef.newCopy(editMask);
        if(currentMask!=null)
        if (!Utils.equals(currentMask.getSelectorPresentationId(), newMask.getSelectorPresentationId())) {
            newMask.setCondition((SqmlExpression) null);
            group = null;
            setValue(null);
        }
        super.setEditMask(newMask);
        updateButtons();
    }

    @Override
    public void refresh() {
        super.refresh();
        updateButtons();
    }

    private EditMaskRef getCurrentEditMaskCopy() {
        return getEditMask();
    }
}
