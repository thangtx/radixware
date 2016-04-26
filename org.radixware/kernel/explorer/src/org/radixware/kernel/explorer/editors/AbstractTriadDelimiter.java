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

package org.radixware.kernel.explorer.editors;

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.text.DecimalFormatSymbols;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;


public abstract class AbstractTriadDelimiter extends QWidget
                             implements IEditMaskEditorSetting {
    protected final ValStrEditor valueEditor;
    protected final QComboBox delimTypes;
    protected final IClientEnvironment env;

    @SuppressWarnings("LeakingThisInConstructor")
    public AbstractTriadDelimiter(final IClientEnvironment environment, final QWidget parent, final String initialValue) {
        super(parent);
        env = environment;      
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setMargin(0);
        setLayout(layout);
        
        final QLabel lblType = new QLabel(environment.getMessageProvider().translate("EditMask", "Thousands separator type:"));
        lblType.setParent((QWidget)this);
        layout.addWidget(lblType);
        
        delimTypes = new QComboBox((QWidget)this);
        for(ETriadDelimeterType e : ETriadDelimeterType.values()) {
            delimTypes.addItem(getLocalizedOption(env.getMessageProvider(), e), e);
        }
        delimTypes.activatedIndex.connect(this, "onChangeType(Integer)");
        layout.addWidget(delimTypes);
                
        final QLabel lblDelimiter = new QLabel(environment.getMessageProvider().translate("EditMask", "Thousands separator:"));
        lblDelimiter.setParent((QWidget)this);
        layout.addWidget(lblDelimiter);
        
        valueEditor = new ValStrEditor(environment, this);
        final EditMaskStr editMask = new EditMaskStr();
        editMask.setMaxLength(1);
        valueEditor.setEditMask(editMask);
        if (initialValue==null){
            setDefaultValue();            
        }else{
            valueEditor.setValue(initialValue);
        }
        layout.addWidget(valueEditor);
        onChangeType(0);
    }
    
    @Override
    public final void setDefaultValue() {
        final EditMaskInt em = new EditMaskInt();
        String val = String.valueOf(em.getCustomTriadDelimeter());
        
        if (val.charAt(0) == '\0') {
            val = "";
        }
        valueEditor.setValue(val);
    }

   
    @Override
    public Object getValue() {
        return valueEditor.getValue();
    }

    private void onChangeType(final Integer index) {
        final ETriadDelimeterType type = (ETriadDelimeterType) delimTypes.itemData(index);
        switch(type) {
            case NONE:
                valueEditor.setValue("");
                valueEditor.setEnabled(false);
                break;
            case DEFAULT:
                final DecimalFormatSymbols fmt = DecimalFormatSymbols.getInstance(env.getLocale());
                valueEditor.setValue(String.valueOf(fmt.getDecimalSeparator()));
                valueEditor.setEnabled(false);
                break;
            case SPECIFIED:
                valueEditor.setValue("");
                valueEditor.setEnabled(true);
                break;
        }
    }
    
    private static String getLocalizedOption(final MessageProvider msgProvider, final ETriadDelimeterType delimType) {
        switch(delimType) {
            case DEFAULT:
                return msgProvider.translate("EditMask", "Use locale defaults");
            case NONE:
                return msgProvider.translate("EditMask", "None");
            case SPECIFIED:
                return msgProvider.translate("EditMask", "Specify");
            default:
                throw new IllegalArgumentException("Unknown delimiter type");
        }
    }
}
