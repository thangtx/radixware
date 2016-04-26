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

package org.radixware.wps.rwt;

import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.wps.views.editors.valeditors.ValEditorController;


public final class AdvancedTristateCheckBox<T> extends AbstractTristateCheckBox<T, EditMaskBool>{
    
    public AdvancedTristateCheckBox(final IClientEnvironment environment, final ValEditorController<T,EditMaskBool> controller){
        super(environment,controller);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T mapFromBooleanToValue(final Boolean b) {
        if (b==null){
            return null;
        }
        final EditMaskBool editMask = getController().getEditMask();
        if (b.booleanValue()) {
            return (T)editMask.getTrueValue();
        } else{
            return (T)editMask.getFalseValue();
        }
    }

    @Override
    protected Boolean mapFromValueToBoolean(final T value) {
        if (value==null){
            return null;
        }
        final EditMaskBool editMask = getController().getEditMask();
        if (Objects.equals(value, editMask.getTrueValue())){
            return Boolean.TRUE;
        }else if (Objects.equals(value, editMask.getFalseValue())){
            return Boolean.FALSE;
        }else if (value instanceof Boolean){
            return (Boolean)value;
        }else{
            return Boolean.FALSE;
        }
    }
}
