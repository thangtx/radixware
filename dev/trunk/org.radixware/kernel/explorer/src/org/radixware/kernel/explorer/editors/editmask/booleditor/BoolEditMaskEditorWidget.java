/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.editors.editmask.booleditor;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.kernel.common.client.widgets.IEditMaskEditor;
import org.radixware.kernel.common.enums.ECompatibleTypesForBool;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;
import org.radixware.schemas.editmask.RadixEditMaskDocument;

public class BoolEditMaskEditorWidget extends ExplorerFrame implements IEditMaskEditor {

    private final IClientEnvironment environment;
    private QVBoxLayout mainLayout;
    private ECompatibleTypesForBool valueType = ECompatibleTypesForBool.DEFAULT;
    private final EnumMap<EEditMaskOption, IEditMaskEditorSetting> widgets
            = new EnumMap<EEditMaskOption, IEditMaskEditorSetting>(EEditMaskOption.class);

    public BoolEditMaskEditorWidget(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent);
        this.environment = environment;
        setUpUi(environment);
        this.setLayout(mainLayout);
        this.setFrameShape(Shape.StyledPanel);
    }

    private void setUpUi(IClientEnvironment environment) {
        mainLayout = new QVBoxLayout();
        mainLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
        final BooleanTitleValuesVisibleSetting titleValuesVisibleSettings = new BooleanTitleValuesVisibleSetting(environment, this);
        widgets.put(titleValuesVisibleSettings.getOption(), titleValuesVisibleSettings);
        mainLayout.addWidget(titleValuesVisibleSettings);
        
        QGroupBox gb = new QGroupBox(this);
        gb.setTitle(environment.getMessageProvider().translate("EditMask", "Select value"));
        QGridLayout gridLayout = new QGridLayout();
        
        final BooleanTrueValueSetting trueVal = new BooleanTrueValueSetting(environment, gb);
        widgets.put(trueVal.getOption(), trueVal);
        gridLayout.addWidget(trueVal, 0, 0);
        final BooleanFalseValueSetting falseVal = new BooleanFalseValueSetting(environment, gb);
        widgets.put(falseVal.getOption(), falseVal);
        gridLayout.addWidget(falseVal, 1, 0);
        final BooleanTrueTitleSetting trueTitle = new BooleanTrueTitleSetting(environment, gb);
        widgets.put(trueTitle.getOption(), trueTitle);
        gridLayout.addWidget(trueTitle, 0, 1);
        final BooleanFalseTitleSetting falseTitle = new BooleanFalseTitleSetting(environment, gb);
        widgets.put(falseTitle.getOption(), falseTitle);
        gridLayout.addWidget(falseTitle, 1, 1);
        gb.setLayout(gridLayout);
        mainLayout.addWidget(gb);
        gb.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
    }

    @Override
    public EditMask getEditMask() {
        final RadixEditMaskDocument xmlBeanInstance
                = RadixEditMaskDocument.Factory.newInstance();
        final org.radixware.schemas.editmask.EditMask editMask = xmlBeanInstance.addNewRadixEditMask();
        editMask.addNewBoolean();
        final Set<EEditMaskOption> keySet = widgets.keySet();
        for (EEditMaskOption key : keySet) {
            widgets.get(key).addToXml(editMask);
        }
        editMask.getBoolean().setValueType(valueType);
        return EditMask.loadFrom(xmlBeanInstance);
    }

    @Override
    public void setEditMask(EditMask editMask) {
        if (editMask instanceof EditMaskBool) {
            final org.radixware.schemas.editmask.RadixEditMaskDocument xmlBeanInstance
                    = org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
            final org.radixware.schemas.editmask.EditMask em
                    = xmlBeanInstance.addNewRadixEditMask();
            editMask.writeToXml(em);

            final Set<EEditMaskOption> keySet = widgets.keySet();
            for (EEditMaskOption key : keySet) {
                widgets.get(key).loadFromXml(em);
            }
            em.getBoolean().setValueType(((EditMaskBool)editMask).getValueType());
            valueType = ((EditMaskBool)editMask).getValueType();
        } else {
            throw new IllegalArgumentError("Instance of EditMaskBool expected");
        }
    }

    @Override
    public void setHiddenOptions(EnumSet<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for (EEditMaskOption key : options) {
            setting = widgets.get(key);
            if (setting != null) {
                setting.hide();
            }
        }
    }

    @Override
    public void setVisibleOptions(EnumSet<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for(EEditMaskOption key : options) {
            setting = widgets.get(key);
            if(setting != null) {
                setting.show();
            }
        }
    }

    @Override
    public void setEnabledOptions(EnumSet<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for(EEditMaskOption key : options) {
            setting = widgets.get(key);
            if(setting != null) {
                setting.setEnabled(true);
            }
        }
    }

    @Override
    public void setDisabledOptions(EnumSet<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for(EEditMaskOption key : options) {
            setting = widgets.get(key);
            if(setting != null) {
                setting.setDisabled(true);
            }
        }
    }

    @Override
    public boolean checkOptions() {
        boolean result = true;
//        TODO: values in the fields True value and False value cannot be equal or both be null.
        return result;
    }
}
