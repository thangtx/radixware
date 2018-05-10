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

package org.radixware.wps.views.editor.property;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ModifiedEntityModelFinder;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyReference;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.rwt.InputBox.DisplayController;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.ValEditorFactory;
import org.radixware.wps.views.editors.valeditors.ValReferenceEditorController;


public abstract class PropReferenceEditor extends PropEditor {
    
    private static interface IValRefEditorDelegate{
        void onInsertEntity();
        void onEditEntity();
        void onSelectEntity();
        GroupModel getGroupModel();
        void afterSelectReferenceFromDropDownList(final Reference ref);
    }    
        
    private static class ValRefEditorFactory extends ValEditorFactory{  
        
        private class InternalValReferenceEditorController extends ValReferenceEditorController {

            private final ToolButton insertButton;
            private IValRefEditorDelegate delegate;
            private boolean canEdit;
            private boolean viewOnEdit = true;
            private boolean canSelect;
            private boolean isGroupModelAccessible;
            private String selectButtonToolTip;
            private ClientIcon selectButtonIcon;

            public InternalValReferenceEditorController(final IClientEnvironment environment) {
                super(environment);
                insertButton = new ToolButton();
                insertButton.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(final IButton source) {
                        if (delegate!=null){
                            delegate.onInsertEntity();
                        }                    
                    }
                });

                insertButton.setToolTip(getEnvironment().getMessageProvider().translate("PropRefEditor", "Open Object in Tree"));
                insertButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Selector.INSERT_INTO_TREE));
                insertButton.setObjectName("tbInsert");
                addButton(insertButton);
            }

            public final void setValRefEditorHandler(final IValRefEditorDelegate handler){
                this.delegate = handler;
            }

            public final void setIsEditButtonVisible(final boolean isVisible){
                canEdit = isVisible;
            }

            @Override
            protected boolean isEditButtonVisible() {
                return canEdit;
            }

            public final void setViewOnlyOnEdit(final boolean isViewOnly){
                viewOnEdit = isViewOnly;
            }

            @Override
            protected boolean isViewOnlyOnEdit() {
                return viewOnEdit;
            }

            public final void setSelectButtonVisible(final boolean isVisible){
                canSelect = isVisible;
            }

            @Override
            protected boolean isSelectButtonVisible() {
                return canSelect && !getEditMask().isUseDropDownList();
            }                        
            
            public final void setGroupModelAccessible(final boolean isAccessible){
                this.isGroupModelAccessible = isAccessible;
            }

            @Override
            protected boolean isGroupModelAccessible() {
                return isGroupModelAccessible;
            }                        

            public void setInsertButtonVisible(final boolean isVisible) {
                insertButton.setVisible(isVisible);
            }

            @Override
            public GroupModel getGroupModel() {
                return delegate==null ? super.getGroupModel() : delegate.getGroupModel();
            }                        

            @Override
            public void editEntity() {
                if (delegate!=null){
                    delegate.onEditEntity();
                }
            }

            @Override
            public void select() {
                if (delegate!=null){
                    delegate.onSelectEntity();
                }
            }

            @Override
            protected void onSelectValueFromDropDownList(final Reference value) {
                if (delegate==null){
                    super.onSelectValueFromDropDownList(value);
                }else{
                    delegate.afterSelectReferenceFromDropDownList(value);
                }
            }                        

            public final void setSelectButtonToolTip(final String toolTip){
                selectButtonToolTip = toolTip;
            }

            @Override
            public String getSelectButtonToolTip() {
                return selectButtonToolTip == null ? super.getSelectButtonToolTip() : selectButtonToolTip;
            }

            public final void setSelectButtonIcon(final ClientIcon icon){
                selectButtonIcon = icon;
            }

            @Override
            protected ClientIcon getSelectButtonIcon() {
                return selectButtonIcon==null ? super.getSelectButtonIcon() : selectButtonIcon;
            }

            @Override
            protected DisplayController<Reference> createDisplayController() {
                return new PropertyDisplayController<>(super.createDisplayController(),property);
            }

            @Override
            protected Label createLabel() {
                return PropEditor.LabelComponentFactory.getDefault().createPropLabelComponent(property);
            }
            
            
        }

        
        private final Property property;
        
        public ValRefEditorFactory(final Property property){
            this.property = property;
        }

        @Override
        public IValEditor createValEditor(EValType valType, EditMask editMask, IClientEnvironment env) {
            ValReferenceEditorController controller = new InternalValReferenceEditorController(env);
            controller.setEditMask((EditMaskRef)editMask);
            IValEditor editor = controller.getValEditor();
            return editor;
        }    
    }
    
    private final IValRefEditorDelegate delegate = new IValRefEditorDelegate() {

        @Override
        public void onInsertEntity() {
            insertEntity();
        }

        @Override
        public void onEditEntity() {
            editEntity();
        }

        @Override
        public void onSelectEntity() {
            selectEntity();
        }

        @Override
        public GroupModel getGroupModel() {
            return PropReferenceEditor.this.getGroupModel();
        }

        @Override
        public void afterSelectReferenceFromDropDownList(final Reference ref) {
            PropReferenceEditor.this.afterSelectReferenceFromDropDownList(ref);
        }        
    };

    public PropReferenceEditor(final Property property) {
        super(property, new ValRefEditorFactory(property));
        getEditorController().setValRefEditorHandler(delegate);        
    }

    protected void insertEntity() {
        final PropertyReference property = (PropertyReference) getProperty();
        try {
            property.insertEntityIntoTree();
        } catch (InterruptedException e) {
        } catch (Exception ex) {
            final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't insert into tree from property \'%s\': \n%s");
            processException(ex, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on inserting object"), msg);
        }
    }

    protected void editEntity() {
        final PropertyReference property = (PropertyReference) getProperty();
        try {
            property.openEntityEditor();
        } catch (InterruptedException e) {
        } catch (ObjectNotFoundError objectNotFound) {
            final Model owner = getProperty().getOwner();
            if ((owner instanceof EntityModel) && objectNotFound.inContextOf((EntityModel) owner)) {
                owner.showException(getEnvironment().getMessageProvider().translate("ExplorerError", "Can't Open Editor"), objectNotFound);
            } else {
                final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't edit \'%s\': \n%s");
                processException(objectNotFound, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on editing property"), msg);
            }
        } catch (Exception ex) {
            final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't edit \'%s\': \n%s");
            processException(ex, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on editing property"), msg);
        }
    }

    protected abstract void selectEntity();
    
    protected GroupModel getGroupModel(){
        return null;
    }
    
    protected void afterSelectReferenceFromDropDownList(final Reference newValue) {        
    }

    @Override
    public void refresh(ModelItem item) {
        super.refresh(item);
        final PropertyReference property = (PropertyReference) controller.getProperty();
        boolean isReadOnly = controller.isReadonly();
        boolean isGroupModelAccessible = property.getType() != EValType.OBJECT;        
        boolean canEdit = false;
        boolean canInsert = false;
        boolean viewOnEdit = true;        
        if (property.canOpenEntityModelView()) {
            canEdit = true;
            final RadParentRefPropertyDef def = (RadParentRefPropertyDef) property.getDefinition();            

            if (def.getType() == EValType.OBJECT) {
                viewOnEdit = controller.isReadonly() || !property.canModifyEntityObject();                
            } else {
                final IView view = property.getOwner().getView();
                if (view!=null && view.getRestrictions().isRestrictedInView(ERestriction.UPDATE)){
                    viewOnEdit = true;
                }
                else if (!property.canModifyEntityObject()){
                    viewOnEdit = true;
                } else{
                    viewOnEdit = 
                        property.getOwner().findOwner( new ModifiedEntityModelFinder(property.getVal().getPid()) )!=null;
                }
            }

            if (property.canInsertEntityIntoTree() && 
                property.getOwner().findNearestExplorerItemView() != null &&
                !theSameEntityModel(property.getOwner().findNearestExplorerItemView())) {
                canInsert = true;
            }
        }
        getEditorController().setIsEditButtonVisible(canEdit);
        getEditorController().setViewOnlyOnEdit(viewOnEdit);
        getEditorController().setSelectButtonVisible(isSelectButtonVisible());
        getEditorController().setInsertButtonVisible(canInsert);
        getEditorController().setSelectButtonToolTip(getSelectButtonToolTip());
        getEditorController().setSelectButtonIcon(getSelectButtonIcon());
        getEditorController().setGroupModelAccessible(isGroupModelAccessible);
        getValEditor().setReadOnly(isReadOnly);
        getValEditor().refresh();
    }
    
    private boolean theSameEntityModel(final IExplorerItemView itemView){
        final PropertyReference property = (PropertyReference) getProperty();
        final IExplorerTreeNode currentTreeNode = itemView.getExplorerTree().getCurrent();
        final IExplorerItemView currentItemView = currentTreeNode==null ? null : currentTreeNode.getView();
        if (property.getVal()!=null && currentItemView!=null && currentItemView.isEntityView()){
            final EntityModel insertedEntityModel = (EntityModel)currentItemView.getModel();            
            return property.getVal().getPid().equals(insertedEntityModel.getPid());
        }        
        return false;
    }

    protected boolean isSelectButtonVisible() {
        return !controller.isReadonly() && !controller.isInheritedValue();
    }

    protected String getSelectButtonToolTip() {
        return null;
    }

    protected ClientIcon getSelectButtonIcon() {
        return null;
    }

    private ValRefEditorFactory.InternalValReferenceEditorController getEditorController() {
        return (ValRefEditorFactory.InternalValReferenceEditorController) getValEditor();
    }
}
