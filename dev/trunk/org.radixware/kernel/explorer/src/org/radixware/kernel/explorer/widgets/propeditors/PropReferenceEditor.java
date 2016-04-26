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

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ModifiedEntityModelFinder;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyReference;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;
import org.radixware.kernel.explorer.env.ExplorerIcon;

import org.radixware.kernel.explorer.views.selector.Selector;


public abstract class PropReferenceEditor extends PropEditor {
    
    private static class ValEditorFactoryImpl extends ValEditorFactory{
        
        private final static ValEditorFactoryImpl INSTANCE = new ValEditorFactoryImpl();
        
        private ValEditorFactoryImpl(){
            
        }

        @Override
        public ValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment environment, final QWidget parentWidget) {
            return new ValEditor<Reference>(environment, parentWidget, editMask, false, false){
                        @Override
                        protected boolean eq(final Reference value1, final Reference value2) {
                            return Reference.exactlyMatch(value1, value2);
                        }
                    };
        }
        
    }

    protected final QToolButton changeReferenceButton = initButton("onChangeReferenceButtonClick()", null, null, null, "tbChangeReference");
    private final QToolButton editButton = initButton("editEntity()", null, getEnvironment().getMessageProvider().translate("PropRefEditor", "Edit Object"), ExplorerIcon.getQIcon(ClientIcon.Definitions.EDITOR), "tbEdit");
    private final QToolButton insertButton = initButton("insertEntity()", null, getEnvironment().getMessageProvider().translate("PropRefEditor", "Open Object in Tree"), ExplorerIcon.getQIcon(Selector.Icons.INSERT_INTO_TREE), "tbInsert");

    protected PropReferenceEditor(final Property property) {
        this(property, getValEditorFactory());
    }
    
    protected PropReferenceEditor(final Property property, final ValEditorFactory valEditorFactory) {
        super(property, valEditorFactory);
        if (!getProperty().hasOwnValue()){
            getValEditor().setReadOnly(true);
        }
        getValEditor().getLineEdit().setReadOnly(true);
    }

    @Override
    public void setProperty(final Property property) {
        super.setProperty(property);
        if (property!=null){
            getValEditor().setReadOnly(!getProperty().hasOwnValue());
            getValEditor().getLineEdit().setReadOnly(true);
        }
    }
            
    public static ValEditorFactory getValEditorFactory(){
        return ValEditorFactoryImpl.INSTANCE;
    }

    protected final QToolButton initButton(final String methodName, final String text, final String toolTip, final QIcon icon, final String objectName) {
        final QAction action = new QAction(this);
        action.triggered.connect(this, methodName);
        if (text != null) {
            action.setText(text);
        }
        if (toolTip != null) {
            action.setToolTip(toolTip);
        }
        if (icon != null) {
            action.setIcon(icon);
        }
        final QToolButton button = getValEditor().addButton(text, action);
        button.setObjectName(objectName);
//        QToolButton button = WidgetUtils.createEditorButton(this, action);
//        getLayout().addWidget(button);
        return button;
    }

    abstract public void onChangeReferenceButtonClick();

    @Override
    @SuppressWarnings("unchecked")
    public void refresh(final ModelItem changedItem) {
        super.refresh(changedItem);
        final PropertyReference property = (PropertyReference) getProperty();
        changeReferenceButton.setVisible(!isReadonly());

        // refresh checkForInherit
        editButton.hide();
        insertButton.hide();

        if (property.canOpenEntityModelView()) {
            final RadParentRefPropertyDef def = (RadParentRefPropertyDef) property.getDefinition();
            editButton.show();
            final boolean isReadonly;

            if (def.getType() == EValType.OBJECT) {
                isReadonly = isReadonly() || !property.canModifyEntityObject();
            } else {
                final IView view = property.getOwner().getView();
                if (view!=null && view.getRestrictions().isRestrictedInView(ERestriction.UPDATE)){
                    isReadonly = true;
                }else if (!property.canModifyEntityObject()){                
                    isReadonly = true;
                }else {                                        
                    isReadonly = 
                        property.getOwner().findOwner( new ModifiedEntityModelFinder(property.getVal().getPid()) )!=null;
                }
            }

            if (isReadonly) {
                editButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.VIEW));
                editButton.setToolTip(getEnvironment().getMessageProvider().translate("PropRefEditor", "View Object"));
            } else {
                editButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.EDITOR));
                editButton.setToolTip(getEnvironment().getMessageProvider().translate("PropRefEditor", "Edit Object"));
            }

            if (property.canInsertEntityIntoTree() && !theSameEntityModel(property.getOwner().findNearestExplorerItemView())) {
                insertButton.show();
            }
        }
    }
    
    private boolean theSameEntityModel(final IExplorerItemView itemView){
        final PropertyReference property = (PropertyReference) getProperty();   
        final IExplorerItemView currentItemView = itemView.getExplorerTree().getCurrent().getView();
        if (property.getVal()!=null && currentItemView.isEntityView()){
            final EntityModel insertedEntityModel = (EntityModel)currentItemView.getModel();            
            return property.getVal().getPid().equals(insertedEntityModel.getPid());
        }        
        return false;
    }

    @Override
    public boolean setFocus(final Property property) {
        getValEditor().getLineEdit().setFocus();
        return true;
    }

    @SuppressWarnings("unused")
    private void editEntity() {
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

    @SuppressWarnings("unused")
    private void insertEntity() {
        final PropertyReference property = (PropertyReference) getProperty();
        try {
            property.insertEntityIntoTree();
        } catch (InterruptedException e) {
        } catch (Exception ex) {
            final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't insert into tree from property \'%s\': \n%s");
            processException(ex, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on inserting object"), msg);
        }
    }
    
    @Override
    public boolean eventFilter(final QObject source, final QEvent event) {
        if (event!=null && event.type() == QEvent.Type.FocusOut) {
            return false;
        }
        else{
            return super.eventFilter(source, event);
        }
    }
}
