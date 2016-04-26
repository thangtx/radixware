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

package org.radixware.kernel.explorer.dialogs.pkcs11;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.connections.Pkcs11Config;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;


public final class Pkcs11ConfigStrOption extends ValStrEditor implements IPkcsSetting<String> {
    
    private final Pkcs11Config.Field field;
    private String fieldName = "";
    
    public Pkcs11ConfigStrOption(final IClientEnvironment environment, 
                                 final QWidget parent, 
                                 final EditMaskStr editMask, 
                                 final Pkcs11Config.Field field) {
        super(environment, parent, editMask, true, false);
        this.field = field;
        setValue("");
    }

    @Override
    public void readConfiguration(final Pkcs11Config config) {
        setValue(config.getFieldValue(field));
    }

    @Override
    public void writeConfiguration(final Pkcs11Config config) {
        config.setFieldValue(field, getValue());
    }       
    
    @Override
    public void setName(final String name) {
        fieldName = name;
    }

    @Override
    public String getName() {
        return fieldName;
    }

    @Override
    public boolean isEmpty() {
        final String val = getValue();
        return val == null || val.isEmpty();
    }
}
