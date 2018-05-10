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
import org.radixware.kernel.common.meta.InputMask;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.InputBox.ValueController;

public class ValCharEditorController extends InputBoxController<Character, EditMaskStr> {
    
    public ValCharEditorController(final IClientEnvironment environment, final LabelFactory factory) {
        super(environment, factory);
        setEditMask(new EditMaskStr());
    }
    
    public ValCharEditorController(final IClientEnvironment environment) {
        this(environment, null);
    }
    
    protected void applyEditMask(InputBox box) {
        super.applyEditMask(box);
        final EditMaskStr editMask = getEditMask();
        box.setPassword(editMask.isPassword());
        box.setMaxLength(1);
    }
    
    public void setEditMask(EditMaskStr editMask) {
        if (editMask.getInputMask().getLength() > 1) {
            final EditMaskStr newMask = (EditMaskStr) EditMask.newCopy(editMask);
            newMask.setInputMask("");
            setEditMaskStr(newMask);
        } else {
            setEditMaskStr(editMask);
        }        
    }
    
    @Override
    protected ValueController<Character> createValueController() {
        return new ValueController<Character>() {
            
            @Override
            public Character getValue(final String text) throws InputBox.InvalidStringValueException {
                if (text != null && !text.isEmpty()) {
                    return text.charAt(0);
                } else {
                    return null;
                }
            }
        };        
    }    
    
    private void setEditMaskStr(final EditMaskStr editMask) {
        if (!editMask.getInputMask().isEmpty() && editMask.getInputMask().getPattern().contains("\u0000")) {
            final EditMaskStr newEditMask = (EditMaskStr) EditMask.newCopy(editMask);
            //0 code is not supported in HTML, so replacing that symbol with special space
            final String newPattern = editMask.getInputMask().getPattern().replace('\0', '\u2007');
            newEditMask.setInputMask(InputMask.Factory.newInstance(newPattern));
            super.setEditMask(newEditMask);
        } else {
            super.setEditMask(editMask);
        }
    }

    @Override
    protected String calcFocusedText(Character value, EditMaskStr editMask) {
         if (editMask.getInputMask().isEmpty()) {
            return super.calcFocusedText(value, editMask);
        } else {
            return editMask.getInputMask().applyTo(String.valueOf(value));
        }
    }
    
}
