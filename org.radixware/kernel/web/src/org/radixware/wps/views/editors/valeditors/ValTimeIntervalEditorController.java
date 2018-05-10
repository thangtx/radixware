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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval;
import org.radixware.wps.rwt.InputBox.InvalidStringValueException;
import org.radixware.wps.rwt.InputBox.ValueController;


public class ValTimeIntervalEditorController extends InputBoxController<Long,EditMaskTimeInterval> {
    
    public ValTimeIntervalEditorController(final IClientEnvironment env, final LabelFactory factory) {
        super(env,factory);
        setEditMask(new EditMaskTimeInterval(1L,null,null,null));
    }
    

    public ValTimeIntervalEditorController(final IClientEnvironment env) {
        this(env,null);
    }

    @Override
    protected ValueController<Long> createValueController() {
        return new ValueController<Long>() {

            @Override
            public Long getValue(String newText) throws InvalidStringValueException {
                final EditMaskTimeInterval mask = getEditMask();
                return mask.convertFromStringToLong(newText);
            }
        };
    }
    
    @Override
    protected String calcFocusedText(final Long value, final EditMaskTimeInterval editMask) {
        if (editMask.isSpecialValue(value)){
            return editMask.getInputTextForValue(Long.valueOf(0));
        }else{
            return editMask.getInputTextForValue(value);
        }
    }    
}
