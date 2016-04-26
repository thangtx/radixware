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
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;


public class Pkcs11ConfigIntOption extends ValIntEditor implements IPkcsSetting<Long> {
    
    private final Pkcs11Config.Field field;
    private String fieldName = "";
    
    public Pkcs11ConfigIntOption(final IClientEnvironment environment, 
                                 final QWidget parent,
                                 final EditMaskInt editMask,
                                 final boolean isMandatory,
                                 final Pkcs11Config.Field field) {
        super(environment, parent, editMask, isMandatory, false);
        this.field = field;
    }

    @Override
    public void readConfiguration(final Pkcs11Config config) {
        final String configValue = config.getFieldValue(field);
        if (configValue!=null && !configValue.isEmpty()){
            setValue(Long.valueOf(configValue));
        }        
    }

    @Override
    public void writeConfiguration(final Pkcs11Config config) {
        final Long value = getValue();
        if (value==null){
            config.setFieldValue(field, null);
        }else{
            config.setFieldValue(field, String.valueOf(value));
        }
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
        return getValue() == null;
    }
}