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
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.types.Bin;
import org.radixware.wps.rwt.InputBox.InvalidStringValueException;
import org.radixware.wps.rwt.InputBox.ValueController;
import org.radixware.wps.rwt.InputFormat;


public class ValBinEditorController extends InputBoxController<Bin,EditMaskNone> {
    
    public ValBinEditorController(final IClientEnvironment env, final LabelFactory factory){
        super(env,factory);
        setEditMask(new EditMaskNone());
    }
    
    public ValBinEditorController(final IClientEnvironment env){
        this(env,null);
    }

    @Override
    protected ValueController<Bin> createValueController() {
        return new ValueController<Bin>() {
            @Override
            public Bin getValue(final String text) throws InvalidStringValueException {
                try {
                    final String hexadecimalString = text.replace(" ", "");
                    if (hexadecimalString.isEmpty()) {
                        return null;
                    }
                    return ValueConverter.hexadecimalString2Bin(hexadecimalString, null);
                } catch (IllegalArgumentError error) {
                    throw new InvalidStringValueException(text);
                }                                
            }
        };
    }

    @Override
    protected InputFormat createInputFormat() {
        return InputFormat.Factory.binInputFormat();
    }
}
