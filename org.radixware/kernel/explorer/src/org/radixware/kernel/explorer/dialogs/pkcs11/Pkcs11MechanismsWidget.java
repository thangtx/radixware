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

import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.connections.Pkcs11Config;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;


public final class Pkcs11MechanismsWidget extends QGroupBox implements IPkcsSetting<String>  {
    
    private final QRadioButton enabler;
    private final QRadioButton disabler;
    private final ValStrEditor options;
    private String fieldName = "";
    
    public Pkcs11MechanismsWidget(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        setCheckable(true);
        setChecked(false);
        setTitle(environment.getMessageProvider().translate("PKCS11", "Mechanisms"));
        
        enabler = new QRadioButton(this);
        enabler.setText(environment.getMessageProvider().translate("PKCS11", "Enable"));
        enabler.setChecked(true);
        
        
        disabler = new QRadioButton(this);
        disabler.setText(environment.getMessageProvider().translate("PKCS11", "Disable"));
        
        options = new ValStrEditor(environment, this);
        options.setMandatory(false);
                
        final QVBoxLayout layout = new QVBoxLayout();
        layout.addWidget(enabler);
        layout.addWidget(disabler);
        layout.addWidget(options);
        setLayout(layout);
        
    }

    @Override
    public void readConfiguration(final Pkcs11Config config) {
        String mechs = config.getFieldValue(Pkcs11Config.Field.DISABLED_MECH);
        if (mechs!=null && !mechs.isEmpty()){
            disabler.setChecked(true);
        }else{
            mechs = config.getFieldValue(Pkcs11Config.Field.ENABLED_MECH);
            if (mechs!=null && !mechs.isEmpty()){
                enabler.setChecked(true);
            }else{
                return;
            }
        }
        options.setValue(mechs);
    }

    @Override
    public void writeConfiguration(final Pkcs11Config config) {
        final Pkcs11Config.Field field = getField();
        if (field==null){
            config.setFieldValue(Pkcs11Config.Field.DISABLED_MECH, null);
            config.setFieldValue(Pkcs11Config.Field.ENABLED_MECH, null);
        }else{
            if (field==Pkcs11Config.Field.DISABLED_MECH){
                config.setFieldValue(Pkcs11Config.Field.DISABLED_MECH, getValue());
                config.setFieldValue(Pkcs11Config.Field.ENABLED_MECH, null);
            }else{
                config.setFieldValue(Pkcs11Config.Field.DISABLED_MECH, null);
                config.setFieldValue(Pkcs11Config.Field.ENABLED_MECH, getValue());
            }
        }
    }
   
    private Pkcs11Config.Field getField() {
        if(isChecked()) {
            return (enabler.isChecked() ? Pkcs11Config.Field.ENABLED_MECH : Pkcs11Config.Field.DISABLED_MECH);
        } else {
            return null;
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
        return !isChecked() || options.getValue() == null || options.getValue().isEmpty();
    }

    @Override
    public String getValue() {
        return options.getValue();
    }
}
