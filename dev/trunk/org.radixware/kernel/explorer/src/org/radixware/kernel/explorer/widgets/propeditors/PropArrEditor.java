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

import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EWidgetMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.errors.SettingPropertyValueError;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyArr;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;
import org.radixware.kernel.explorer.env.ExplorerIcon;

public class PropArrEditor<T extends Arr> extends PropEditor {

    private static class ValEditorFactoryImpl extends ValEditorFactory{
        
        public static final ValEditorFactoryImpl INSTANCE = new ValEditorFactoryImpl();
        
        private ValEditorFactoryImpl(){            
        }

        @Override
        public ValEditor createValEditor(final EValType valType, 
                                         final EditMask editMask, 
                                         final IClientEnvironment environment, 
                                         final QWidget parentWidget) {
            return new ValEditor<>(environment, parentWidget, editMask, false, false);
        }        
    }
    
    private final QToolButton editButton;
    private boolean isEditButtonVisible = true;

    @SuppressWarnings("LeakingThisInConstructor")
    public PropArrEditor(final Property property) {
        super(property, ValEditorFactoryImpl.INSTANCE);

        final QAction action = new QAction(this);
        action.triggered.connect(this, "edit()");
        action.setObjectName("edit");
        editButton = getValEditor().addButton(null, action);

        getValEditor().getLineEdit().setReadOnly(true);
    }

    @Override
    public void refresh(final ModelItem changedItem) {
        super.refresh(changedItem);
        final Arr value = getValEditor().getValue() != null ? (Arr) getValEditor().getValue() : null;
        if (isReadonly()) {
            editButton.setToolTip(getEnvironment().getMessageProvider().translate("PropArrEditor", "View Array"));
            editButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.VIEW));
            editButton.setVisible(value != null
                    && !value.isEmpty()
                    && isEditButtonVisible
                    && !getProperty().isCustomEditOnly());
        } else {
            editButton.setToolTip(getEnvironment().getMessageProvider().translate("PropArrEditor", "Edit Array"));
            editButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.EDIT));
            editButton.setVisible(isEditButtonVisible && !controller.isInheritedValue());
        }

    }

    @Override
    public boolean setFocus(final Property property) {
        getValEditor().getLineEdit().setFocus();
        return true;
    }

    @SuppressWarnings({"unused","unchecked"})
    private void edit() {
        final PropertyArr property = (PropertyArr) getProperty();
        final IArrayEditorDialog dialog = property.getEditorDialog(getValEditor());
        dialog.setReadonly(isReadonly());
        if (dialog.execDialog() != DialogResult.REJECTED) {
            final Arr newValue = dialog.getCurrentValue();
            if (property.isOwnValueAcceptable(newValue)){
                try {
                    property.setValueObject(newValue);
                } catch (Exception ex) {
                    getEnvironment().processException(new SettingPropertyValueError(property, ex));
                }
                getValEditor().getLineEdit().setText(property.getValueAsString());
                if (!Utils.equals(getPropertyValue(), getCurrentValueInEditor())) {//case when bind method was not called (ex. org.radixware.kernel.explorer.widgets.selector.WrapModelDelegate).
                    refresh(property);
                }
            }
        }
    }

    public final void setEditButtonVisible(final boolean isVisible) {
        isEditButtonVisible = isVisible;
        refresh(getProperty());
    }
    
    @Override
    public final EWidgetMarker getWidgetMarker() {
        return EWidgetMarker.ARR_PROP_EDITOR;
    }    
}
