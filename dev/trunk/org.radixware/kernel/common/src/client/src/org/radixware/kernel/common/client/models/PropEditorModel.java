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
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IPropEditorDialog;
import org.radixware.kernel.common.enums.EEditPossibility;

public abstract class PropEditorModel extends DialogModel {

    public PropEditorModel(IClientEnvironment environment,IPropEditorDialog def) {
        super(environment,def);
    }

    public IPropEditorDialog getPropEditorView() {
        if (getView() != null) {
            return ((IPropEditorDialog) getView());
        }
        return null;
    }

    public final IContext.PropEditorContext getPropEditorContext() {
        if (getContext() instanceof IContext.PropEditorContext) {
            return (IContext.PropEditorContext) getContext();
        }
        return null;
    }

    public final Property getSourceProperty() {
        if (getContext() instanceof IContext.PropEditorContext) {
            return getPropEditorContext().property;
        }
        return null;
    }

    @Override
    public String getTitle() {
        if (super.getTitle() == null && getSourceProperty() != null) {
            return getSourceProperty().getTitle();
        }
        return super.getTitle();
    }

    @Override
    public void setTitle(String newTitle) {
        super.setTitle(newTitle);
        if (getPropEditorView() != null) {
            getPropEditorView().setWindowTitle(getWindowTitle());
        }
    }

    @Override
    public void setIcon(Icon icon) {
        super.setIcon(icon);
        if (getPropEditorView() != null) {
            getPropEditorView().setWindowIcon(icon);
        }
    }
    
    public boolean isReadOnly(){
        if (getSourceProperty()==null){
            return false;
        }else{
            return getSourceProperty().isReadonly() ||
                   (!getSourceProperty().hasOwnValue() && getSourceProperty().isValueDefined()) ||
                   getSourceProperty().getEditPossibility()== EEditPossibility.PROGRAMMATICALLY;
        }
    }
}