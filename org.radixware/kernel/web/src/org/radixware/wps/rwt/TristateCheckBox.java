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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.wps.views.editors.valeditors.ValEditorController;


public final class TristateCheckBox extends AbstractTristateCheckBox<Boolean,EditMaskNone>{

    public TristateCheckBox(final IClientEnvironment environment) {
        super(environment);
    }
    
    public TristateCheckBox(final IClientEnvironment environment, ValEditorController<Boolean,EditMaskNone> controller) {
        super(environment,controller);
    }

    @Override
    protected Boolean mapFromBooleanToValue(final Boolean b) {
        return b;
    }

    @Override
    protected Boolean mapFromValueToBoolean(final Boolean value) {
        return value;
    }
   
}
