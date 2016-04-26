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

package org.radixware.kernel.explorer.editors.editmask.numeditor;

import com.trolltech.qt.gui.QWidget;
import java.text.DecimalFormatSymbols;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.kernel.explorer.editors.AbstractTriadDelimiter;
import org.radixware.schemas.editmask.EditMask;


final class NumTriadDelimiterSetting extends AbstractTriadDelimiter {

    public NumTriadDelimiterSetting(final IClientEnvironment env, final QWidget parent) {
        super(env, parent, null);
    }
    
    @Override
    public void addToXml(final EditMask editMask) {
        final ETriadDelimeterType type = (ETriadDelimeterType) delimTypes.itemData(delimTypes.currentIndex());
        switch(type) {
            case DEFAULT: case NONE: break;
            case SPECIFIED:
                editMask.getNum().setTriadDelimeter(valueEditor.getValue());
                break;
        }
        editMask.getNum().setTriadDelimeterType(type);
    }

    @Override
    public void loadFromXml(final EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskNum emn = editMask.getNum();
        final ETriadDelimeterType type = emn.getTriadDelimeterType();
        
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
                valueEditor.setValue(editMask.getNum().getTriadDelimeter());
                valueEditor.setEnabled(true);
                break;
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.NUM_TRIAD_DELIMITER;
    }
    
}
