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

package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.webkit.QWebSettings;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;



public class ValListEditor extends AbstractListEditor {

    public ValListEditor(final IClientEnvironment environment, final QWidget parent, final EditMaskList editMask, final boolean mandatory, final boolean readOnly) {
        super(environment, parent, editMask, mandatory, readOnly);
        updateComboItems(false);
    }

    public ValListEditor(final IClientEnvironment environment, final QWidget parent, final List<EditMaskList.Item> items){
        this (environment,parent, new EditMaskList(items),true,false);
    }
    
    @Override
    protected void updateComboItems(final boolean updateStylesheet) {
        final EditMaskList maskList = (EditMaskList) getEditMask();
        final List<String> names = new ArrayList<>();

        final Object currentValue = getValue();
        int newIndex = -1;
        boolean nullItemIsPresent = false;
        for (int i = 0; i < maskList.getItems().size(); ++i) {
            final Object newValue = maskList.getItems().get(i).getValue();
            final String title = getStringToShow(newValue);
            if (newValue == null) {
                nullItemIsPresent = true;
            }
            if (!isReadOnly()) {
                if (eq(newValue, currentValue)) {
                    newIndex = i;
                }
                names.add(title);
            } else if (eq(newValue, currentValue)) {
                names.add(title);
                newIndex = 0;
                break;
            }
        }
        clearItems();
        addItems(names);
        updateComboBoxLook(newIndex, nullItemIsPresent, updateStylesheet);        
    }

    @Override
    protected void onActivatedIndex(final int index) {
        if (index < 0) {
            return;
        }
        final EditMaskList mask = (EditMaskList) getEditMask();
        if (index == mask.getItems().size()) {
            setValue(null);
        } else {
            final Object newValue = mask.getItems().get(index).getValue();
            if (getEditMask().validate(getEnvironment(), newValue)==ValidationResult.ACCEPTABLE) {
                setValue(newValue);
            } else {
                updateComboItems(true);
            }
        }
    }
    
    @Override
    public void setValue(final Object value) {
        if (setOnlyValue(value)){
            updateComboItems(false);//optimization - update style sheet only once in refreshColorSettings() call
            clearBtn.setVisible(!isMandatory() && !isReadOnly() && getValue() != null);
            updateValueMarkers(true);
        }
    }
}