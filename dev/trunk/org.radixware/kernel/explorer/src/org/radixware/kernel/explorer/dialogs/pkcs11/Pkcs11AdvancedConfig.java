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
import com.trolltech.qt.gui.QPlainTextEdit;
import com.trolltech.qt.gui.QTextOption.WrapMode;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.connections.Pkcs11Config;


public class Pkcs11AdvancedConfig extends QGroupBox implements IPkcsSetting<String> {

    private final QPlainTextEdit textBox;            
    private String fieldName = "";
    
    public Pkcs11AdvancedConfig(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        setCheckable(true);
        setChecked(false);
        setTitle(environment.getMessageProvider().translate("PKCS11", "Advanced"));
        
        final QVBoxLayout layout = new QVBoxLayout();
        setLayout(layout);
        textBox = new QPlainTextEdit(this);
        textBox.setTabStopWidth(textBox.tabStopWidth() / 4);
        textBox.setWordWrapMode(WrapMode.NoWrap);        
        layout.addWidget(textBox);
    }

    @Override
    public void readConfiguration(final Pkcs11Config config) {
        final String value = config.getFieldValue(Pkcs11Config.Field.ADVANCED);
        if (value==null || value.isEmpty()){
            setChecked(false);
        }else{
            setChecked(true);
            textBox.setPlainText(value);            
        }
    }

    @Override
    public void writeConfiguration(final Pkcs11Config config) {
        final String text = isChecked() ? textBox.toPlainText() : null;
        if (text==null || text.isEmpty()){
            config.setFieldValue(Pkcs11Config.Field.ADVANCED, null);
        }else{
            config.setFieldValue(Pkcs11Config.Field.ADVANCED, text);            
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
        return !isChecked() || textBox.toPlainText().isEmpty();
    }
    
    @Override
    public String getValue() {
        return textBox.toPlainText();
    }
}
