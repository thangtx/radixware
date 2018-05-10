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
package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.jmleditor.autosave.UserFuncAutosaveManager;
import org.radixware.kernel.explorer.env.Application;

/**
 *
 * @author npopov
 */
public class AutosaveSettingsDialog extends ExplorerDialog {

    private static final int SAVE_INTERVAL_MIN_VAL = 3;
    private static final int SAVE_INTERVAL_MAX_VAL = 600;
    private static final int MAX_STORED_VERSIONS_CNT_FOR_FUNC_MIN_VAL = 1;
    private static final int MAX_STORED_VERSIONS_CNT_FOR_FUNC_MAX_VAL = 100;
    private static final int MAX_STORED_VERSIONS_CNT_ALL_MIN_VAL = 1;
    private static final int MAX_STORED_VERSIONS_CNT_ALL_MAX_VAL = 500;
    
    private final QCheckBox enableAutosaveCheckBox;
    private final QSpinBox saveIntervalSpin;
    private final QSpinBox maxStoredVersionsForFuncSpin;
    private final QSpinBox maxStoredVersionsAllSpin;
    
    
    
    public AutosaveSettingsDialog(IClientEnvironment environment, QWidget parent) {
        super(environment, parent);
        this.setWindowTitle(Application.translate("JmlEditor", Application.translate("JmlEditor", "Autosave Settings")));
        enableAutosaveCheckBox = new QCheckBox(this);
        saveIntervalSpin = new QSpinBox(this);
        maxStoredVersionsForFuncSpin = new QSpinBox(this);
        maxStoredVersionsAllSpin = new QSpinBox(this);
        createUI();
    }
    
    private void createUI() {
        this.setMinimumSize(100, 100);
        
        UserFuncAutosaveManager instance = UserFuncAutosaveManager.getInstance(getEnvironment());
        
        enableAutosaveCheckBox.setChecked(instance.isAutosaveEnabled());
        enableAutosave_Toggled(instance.isAutosaveEnabled());
        enableAutosaveCheckBox.toggled.connect(this, "enableAutosave_Toggled(Boolean)");
        
        saveIntervalSpin.setMinimum(SAVE_INTERVAL_MIN_VAL);
        saveIntervalSpin.setMaximum(SAVE_INTERVAL_MAX_VAL);
        saveIntervalSpin.setSuffix(" " + Application.translate("JmlEditor", "sec"));
        saveIntervalSpin.setValue(instance.getSaveIntervalSec());
        saveIntervalSpin.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Preferred);
        
        maxStoredVersionsForFuncSpin.setMinimum(MAX_STORED_VERSIONS_CNT_FOR_FUNC_MIN_VAL);
        maxStoredVersionsForFuncSpin.setMaximum(MAX_STORED_VERSIONS_CNT_FOR_FUNC_MAX_VAL);
        maxStoredVersionsForFuncSpin.setValue(instance.getMaxStoredVersionsForFunc());
        maxStoredVersionsForFuncSpin.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Preferred);
        
        maxStoredVersionsAllSpin.setMinimum(MAX_STORED_VERSIONS_CNT_ALL_MIN_VAL);
        maxStoredVersionsAllSpin.setMaximum(MAX_STORED_VERSIONS_CNT_ALL_MAX_VAL);
        maxStoredVersionsAllSpin.setValue(instance.getMaxStoredVersionsAll());
        maxStoredVersionsAllSpin.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Preferred);
        
        QGridLayout mainLayout = new QGridLayout();
        mainLayout.addWidget(new QLabel(Application.translate("JmlEditor", "Autosave enabled") + ":", this), 0, 0);
        mainLayout.addWidget(enableAutosaveCheckBox, 0, 1);
        mainLayout.addWidget(new QLabel(Application.translate("JmlEditor", "Autosave interval") + ":", this), 1, 0);
        mainLayout.addWidget(saveIntervalSpin, 1, 1);
        mainLayout.addWidget(new QLabel(Application.translate("JmlEditor", "Stored versions per function") + ":", this), 2, 0);
        mainLayout.addWidget(maxStoredVersionsForFuncSpin, 2, 1);
        mainLayout.addWidget(new QLabel(Application.translate("JmlEditor", "Total number of stored versions") + ":", this), 3, 0);
        mainLayout.addWidget(maxStoredVersionsAllSpin, 3, 1);
        
        dialogLayout().addLayout(mainLayout);
        
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
    }
    
    @SuppressWarnings("unused")
    private void enableAutosave_Toggled(Boolean state) {
        saveIntervalSpin.setEnabled(state);
    }

    @Override
    public void accept() {
        UserFuncAutosaveManager instance = UserFuncAutosaveManager.getInstance(getEnvironment());
        instance.setOptions(enableAutosaveCheckBox.isChecked(), saveIntervalSpin.value(), maxStoredVersionsForFuncSpin.value(), maxStoredVersionsAllSpin.value());
        super.accept();
    }
    
}
