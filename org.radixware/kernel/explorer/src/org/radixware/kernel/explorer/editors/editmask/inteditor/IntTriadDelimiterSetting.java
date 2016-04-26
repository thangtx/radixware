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

package org.radixware.kernel.explorer.editors.editmask.inteditor;

import com.trolltech.qt.gui.QWidget;
import java.text.DecimalFormatSymbols;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.kernel.explorer.editors.AbstractTriadDelimiter;


final class IntTriadDelimiterSetting extends AbstractTriadDelimiter {
    

    public IntTriadDelimiterSetting(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, getDefaultValue());
    }
        
    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.INT_TRIAD_DELIMETER;
    }

    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final ETriadDelimeterType type = (ETriadDelimeterType) delimTypes.itemData(delimTypes.currentIndex());
        switch(type) {
            case DEFAULT: case NONE: break;
            case SPECIFIED:
                editMask.getInt().setTriadDelimeter(valueEditor.getValue());
                break;
        }
        editMask.getInt().setTriadDelimeterType(type);
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskInt emi = editMask.getInt();
        final ETriadDelimeterType type = emi.getTriadDelimeterType();
        
        switch(type) {
            case NONE: 
                delimTypes.setCurrentIndex(0);
                valueEditor.setValue("");
                valueEditor.setEnabled(false);
                break;
            case DEFAULT: 
                delimTypes.setCurrentIndex(1);
                final DecimalFormatSymbols fmt = DecimalFormatSymbols.getInstance(env.getLocale());
                valueEditor.setValue(String.valueOf(fmt.getDecimalSeparator()));
                valueEditor.setEnabled(false);
                break;
            case SPECIFIED:
                delimTypes.setCurrentIndex(2);
                valueEditor.setValue(editMask.getInt().getTriadDelimeter());
                valueEditor.setEnabled(true);
                break;
        }
    }
    
    private static String getDefaultValue() {
        final EditMaskInt em = new EditMaskInt();
        String val = String.valueOf(em.getCustomTriadDelimeter());
        
        if (val.charAt(0) == '\0') {
            val = "";
        }
        return val;
    }

   
    @Override
    public Object getValue() {
        return valueEditor.getValue();
    }
}
