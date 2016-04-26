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
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.wps.rwt.InputBox;


public class ValCharEditorController extends ValStrEditorController{
    
    public ValCharEditorController(final IClientEnvironment environment){
        super(environment);
    }
    
    @Override
    protected void applyEditMask(InputBox box) {
        super.applyEditMask(box); 
        box.setMaxLength(1);
    }

    @Override
    public void setEditMask(EditMaskStr editMask) {
        if (editMask.getInputMask().getLength()>1){
            final EditMaskStr newMask = (EditMaskStr)EditMask.newCopy(editMask);
            newMask.setInputMask("");
            super.setEditMask(newMask);
        }
        else{
            super.setEditMask(editMask);
        }                
    }
    
    
}
