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

package org.radixware.kernel.designer.eas.client;

import java.sql.Timestamp;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.*;
import org.radixware.kernel.common.client.exceptions.StandardProblemHandler;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.models.AbstractBatchOperationResult;
import org.radixware.kernel.common.client.models.groupsettings.Filters;
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.InstantiatableClass;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.ModificationsList;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.eas.dialogs.EnterPasswordDialog;
import org.radixware.kernel.designer.eas.dialogs.ExceptionDialog;


class DesignerDialogFactory implements DialogFactory {

    @Override
    public IChangePasswordDialog newChangePasswordDialog(final IClientEnvironment environment) {
        return null;
    }
    
    @Override
    public ISelectEasSessionDialog newSelectEasSessionDialog(final IClientEnvironment environment, final IWidget parent){
        throw new UnsupportedOperationException("Not supported yet.");
    }    

    @Override
    public IEnterPasswordDialog newEnterPasswordDialog(IClientEnvironment ice) {
        return new EnterPasswordDialog();
    }

    @Override
    public IDialog newExceptionBoxDialog(IClientEnvironment environment, IWidget parent, String title, String message, String exceptionDetails, String exceptionTrace) {
        return new ExceptionDialog(environment, parent, title, message, exceptionDetails, exceptionTrace);
    }

    @Override
    public IDialog newMessageWithDetailsDialog(IClientEnvironment environment, IWidget parent, String title, String message, String details, EDialogIconType edit) {
        return new ExceptionDialog(environment, parent, title, message, "", details);
    }
        

    @Override
    public IFiltersManagerDialog newFiltersManagerDialog(IClientEnvironment ice, IWidget iw, Filters fltrs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ISortingsManagerDialog newSortingsManagerDialog(IClientEnvironment ice, IWidget iw, Sortings srtngs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ISelectInstantiatableClassDialog newInstantiatableClassDialog(IClientEnvironment environment, IWidget parentWidget, List<InstantiatableClass> classes, String configPrefix, boolean sortByTitles) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public ISelectInstantiatableClassDialog newInstantiatableClassDialog(IClientEnvironment environment, IWidget parentWidget, List<InstantiatableClass> classes, String configPrefix, boolean sortByTitles, boolean isMultiSelectEnabled) {
        throw new UnsupportedOperationException("Not supported yet.");
    }    

    @Override
    public IMandatoryPropertiesConfirmationDialog newMandatoryPropertiesConfirmationDialog(IClientEnvironment ice, IWidget iw, List<String> list) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IArrayEditorDialog newArrayEditorDialog(IClientEnvironment ice, EValType evt, Class<?> type, boolean bln, IWidget iw) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IArrayEditorDialog newArrayEditorDialog(IClientEnvironment ice, RadSelectorPresentationDef rspd, boolean bln, IWidget iw) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IArrayEditorDialog newArrayEditorDialog(Property prprt, IWidget iw) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IDisplayProblemsDialog newDisplayProblemsDialog(IClientEnvironment ice, IWidget iw, StandardProblemHandler sph) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IAskForApplyChangesDialog newAskForApplyChangesDialiog(IClientEnvironment ice, IWidget iw, ModificationsList ml) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IDateTimeDialog newDateTimeDialog(IClientEnvironment ice, IWidget iw, EditMaskDateTime emdt, Timestamp tmstmp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IDateTimeDialog newDateTimeDialog(IClientEnvironment ice, IWidget iw, String string, Timestamp tmstmp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public IListDialog newListDialog(IClientEnvironment environment, IWidget parent, String configPrefix){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public IBatchOperationResultDialog newBatchOperationResultDialog(final IClientEnvironment environment,final IWidget parent, final AbstractBatchOperationResult result) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IWaitForAadcMemberSwitchDialog newWaitForAadcMemberSwitchDialog(IClientEnvironment ice) {
        return new IWaitForAadcMemberSwitchDialog() {
            @Override
            public void display() {
            }
            @Override
            public IWaitForAadcMemberSwitchDialog.Button getPressedButton() {
                return null;
            }
            @Override
            public void finish() {                
            }
        };
    }
        
}
