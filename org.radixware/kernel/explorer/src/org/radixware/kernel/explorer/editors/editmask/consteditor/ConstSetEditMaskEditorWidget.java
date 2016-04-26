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

package org.radixware.kernel.explorer.editors.editmask.consteditor;

import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumMap;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.widgets.IEditMaskEditor;

import org.radixware.kernel.common.enums.EEditMaskEnumOrder;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;


public class ConstSetEditMaskEditorWidget extends ExplorerFrame implements IEditMaskEditor {
    private final ConstSetSortSetting sortSettings;
    private final ConstSetListCorrectionSetting correctionSettings;
    private final RadEnumPresentationDef enumPresentation;
    
    public ConstSetEditMaskEditorWidget(final IClientEnvironment environment,
                                        final QWidget parent,
                                        final RadEnumPresentationDef enumPresentation) {
        super(environment,parent);
        this.enumPresentation = enumPresentation;
        this.setFrameShape(Shape.StyledPanel);
        
        final QVBoxLayout layout = new QVBoxLayout();
        sortSettings = new ConstSetSortSetting(environment, this);
        correctionSettings = new ConstSetListCorrectionSetting(environment, this, enumPresentation);
        correctionSettings.onSort((EEditMaskEnumOrder)sortSettings.getValue());
        layout.addWidget(sortSettings);
        layout.addWidget(correctionSettings);
        this.setLayout(layout);
        
        sortSettings.itemChanged().connect(correctionSettings, "onSort(EEditMaskEnumOrder)");
    }
    
    @Override
    public EditMask getEditMask() {
        final org.radixware.schemas.editmask.RadixEditMaskDocument xmlBean = 
                    org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
        final org.radixware.schemas.editmask.EditMask em = xmlBean.addNewRadixEditMask();
        em.addNewEnum();
        correctionSettings.addToXml(em);
        sortSettings.addToXml(em);
        correctionSettings.onSort((EEditMaskEnumOrder)sortSettings.getValue());
        return EditMask.loadEditMaskConstSetFrom(enumPresentation.getId(), xmlBean);
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        if(editMask instanceof EditMaskConstSet) {
            final org.radixware.schemas.editmask.RadixEditMaskDocument xmlBean = 
                    org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
            final org.radixware.schemas.editmask.EditMask em = xmlBean.addNewRadixEditMask();
            editMask.writeToXml(em);
            sortSettings.loadFromXml(em);
            correctionSettings.loadFromXml(em);
            correctionSettings.onSort((EEditMaskEnumOrder)sortSettings.getValue());
        } else {
            throw new IllegalArgumentError("Instance of EditMaskConstSet was expected");
        }
    }

    @Override
    public void setHiddenOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption o : options) {
            if(sortSettings.getOption() == o) {
                sortSettings.hide();
            }
            if(correctionSettings.getOption() == o) {
                correctionSettings.hide();
            }
        }
    }

    @Override
    public void setVisibleOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption o : options) {
            if(sortSettings.getOption() == o) {
                sortSettings.show();
            }
            if(correctionSettings.getOption() == o) {
                correctionSettings.show();
            }
        }
    }

    @Override
    public void setEnabledOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption o : options) {
            if(sortSettings.getOption() == o) {
                sortSettings.setEnabled(true);
            }
            if(correctionSettings.getOption() == o) {
                correctionSettings.setEnabled(true);
            }
        }
    }

    @Override
    public void setDisabledOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption o : options) {
            if(sortSettings.getOption() == o) {
                sortSettings.setDisabled(true);
            }
            if(correctionSettings.getOption() == o) {
                correctionSettings.setDisabled(true);
            }
        }
    }

    @Override
    public boolean checkOptions() {
        return true;
    }
}
