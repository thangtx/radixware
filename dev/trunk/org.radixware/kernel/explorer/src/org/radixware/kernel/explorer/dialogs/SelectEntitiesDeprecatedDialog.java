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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QDialogButtonBox.StandardButton;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.errors.CantOpenSelectorError;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.widgets.SelectEntitiesWidget;

@Deprecated
public class SelectEntitiesDeprecatedDialog extends ExplorerDialog {
    private final SelectEntitiesWidget multiselector;
    
    public final Signal1<ArrRef> selectionAccepted = new Signal1<>();
    public final Signal1<List<EntityModel>> selectionListAccepted = new Signal1<>();
    
    
    public SelectEntitiesDeprecatedDialog(final GroupModel groupModel, final QWidget parent, final boolean enableSplitter) throws InterruptedException {
        super(groupModel.getEnvironment(), parent);
        setAttribute(WidgetAttribute.WA_DeleteOnClose);   
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.EXPLORER));
        setWindowTitle(groupModel.getWindowTitle());
        try {
            multiselector = new SelectEntitiesWidget(groupModel, parent, enableSplitter);
            multiselector.setShowSorters(true);
            
        } catch (CantOpenSelectorError error) {
            if (error.getCause() instanceof InterruptedException) {
                throw (InterruptedException) error.getCause();
            } else {
                throw error;
            }
        }
        
        layout().addWidget(multiselector);
        multiselector.setSizePolicy(Policy.MinimumExpanding, Policy.Fixed);
        
        addButtons(EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL),true);
    }
    
    @Override
    public void accept() {
        selectionAccepted.emit(multiselector.getReferences());
        selectionListAccepted.emit(multiselector.getEntities());
        super.accept();
    }
    
    public ArrRef getSelectedEntitiesAsArray() {
        return multiselector.getReferences();
    }
    
    public List<EntityModel> getSelectedEntitiesAsList() {
        return multiselector.getEntities();
    }
    
    public void setPredefinedObjects(List<EntityModel> entities) {
        multiselector.setPredefinedEntities(entities);
    } 
}