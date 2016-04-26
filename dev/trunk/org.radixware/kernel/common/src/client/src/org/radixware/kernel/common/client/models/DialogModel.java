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

package org.radixware.kernel.common.client.models;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException;
import org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.ICustomDialog;

public abstract class DialogModel extends Model {

    public DialogModel(IClientEnvironment environment,ICustomDialog def) {
        super(environment,def);
    }

    public IDialog getDialogView() {
        if (getView() != null) {
            return ((IDialog) getView());
        }
        return null;
    }

    public final IContext.Dialog getDialogContext() {
        if (getContext() != null) {
            return (IContext.Dialog) getContext();
        }
        return null;
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        if (getView() != null) {
            ((IDialog) getView()).setWindowTitle(getWindowTitle());
        }
    }

    @Override
    public void setIcon(Icon icon) {
        super.setIcon(icon);
        if (getView() != null) {
            ((IDialog) getView()).setWindowIcon(icon);
        }
    }
    
    public boolean beforeCloseDialog(final int closeResult){
        return true;
    }
    
    public final void acceptDialog(){
        if (getView()!=null){
            getDialogView().acceptDialog();
        }
    }
    
    public final void rejectDialog(){
        if (getView()!=null){
            getDialogView().rejectDialog();
        }
    }

    public void afterCloseDialog() {        
    }
    
    public boolean beforeCloseDialog(final IDialog.DialogResult result){
        if (result==IDialog.DialogResult.ACCEPTED || result==IDialog.DialogResult.APPLY){
            finishEdit();
            try{
                checkPropertyValues();
            }catch(PropertyIsMandatoryException | InvalidPropertyValueException exception){
                showException(exception);
                exception.goToProblem(this);
                return false;
            }
        }
        return true;
    }
}