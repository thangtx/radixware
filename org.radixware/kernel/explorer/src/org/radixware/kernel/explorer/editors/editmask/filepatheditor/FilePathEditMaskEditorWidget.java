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
package org.radixware.kernel.explorer.editors.editmask.filepatheditor;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;
import org.radixware.kernel.common.client.widgets.IEditMaskEditor;
import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;
import org.radixware.schemas.editmask.RadixEditMaskDocument;

public class FilePathEditMaskEditorWidget extends ExplorerFrame implements IEditMaskEditor {

    private final IClientEnvironment environment;
    private QVBoxLayout mainLayout;
    private final EnumMap<EEditMaskOption, IEditMaskEditorSetting> widgets
            = new EnumMap<>(EEditMaskOption.class);
    FilePathMimeTypesSetting filePathMimeTypes;

    public FilePathEditMaskEditorWidget(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent);
        this.environment = environment;
        setUpUi(environment);
        this.setLayout(mainLayout);
        this.setFrameShape(Shape.StyledPanel);
    }

    private void setUpUi(final IClientEnvironment environment) {
        mainLayout = new QVBoxLayout();
        mainLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));

        QGroupBox gb = new QGroupBox(this);
        gb.setTitle(environment.getMessageProvider().translate("EditMask", "Selection mode"));
        QVBoxLayout verticalLayout = new QVBoxLayout();

        final FilePathSelectionModeSetting filePathSelectionMode = new FilePathSelectionModeSetting(environment, this);
        widgets.put(EEditMaskOption.FILEPATH_SELECTION_MODE, filePathSelectionMode);
        verticalLayout.addWidget(filePathSelectionMode);

        filePathMimeTypes = new FilePathMimeTypesSetting(environment, this);
        widgets.put(EEditMaskOption.FILEPATH_FILE_TYPE_FILTER, filePathMimeTypes);
        verticalLayout.addWidget(filePathMimeTypes);
        filePathMimeTypes.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Minimum);

        filePathSelectionMode.itemChanged().connect(this, "onChangeSelectionMode(EFileSelectionMode)");
        gb.setLayout(verticalLayout);
        mainLayout.addWidget(gb);

        final FilePathDialogOpenModeSetting filePathDialogOpenModeSetting = new FilePathDialogOpenModeSetting(environment, this);
        widgets.put(EEditMaskOption.FILEPATH_FILE_DIALOG_OPEN_MODE, filePathDialogOpenModeSetting);
        mainLayout.addWidget(filePathDialogOpenModeSetting);
        
        final FilePathDialogTitleSetting filePathDialogTitleSetting = new FilePathDialogTitleSetting(environment, this);
        widgets.put(EEditMaskOption.FILEPATH_FILE_DIALOG_TITLE, filePathDialogTitleSetting);
        mainLayout.addWidget(filePathDialogTitleSetting);

        QHBoxLayout horizontalLayout = new QHBoxLayout();

        final FilePathCheckIfPathExistsSetting filePathCheckIfPathExists = new FilePathCheckIfPathExistsSetting(environment, this);
        widgets.put(EEditMaskOption.FILEPATH_PATH_EXIST, filePathCheckIfPathExists);
        horizontalLayout.addWidget(filePathCheckIfPathExists);

        final FilePathHandleInputAvailableSetting filePathHandleInputAvailable = new FilePathHandleInputAvailableSetting(environment, this);
        widgets.put(EEditMaskOption.FILEPATH_FREE_INPUTAVAILABLE, filePathHandleInputAvailable);
        horizontalLayout.addWidget(filePathHandleInputAvailable);

        mainLayout.addLayout(horizontalLayout);
        
        final FilePathStoreLastPathSetting filePathStoreLastPath = new FilePathStoreLastPathSetting(environment, this);
        widgets.put(EEditMaskOption.FILEPATH_INIT_PATH, filePathStoreLastPath);
        mainLayout.addWidget(filePathStoreLastPath);
    }

    @Override
    public EditMask getEditMask() {
        final RadixEditMaskDocument xmlBeanInstance
                = RadixEditMaskDocument.Factory.newInstance();
        final org.radixware.schemas.editmask.EditMask editMask = xmlBeanInstance.addNewRadixEditMask();
        editMask.addNewFilePath();
        final Set<EEditMaskOption> keySet = widgets.keySet();
        for (EEditMaskOption key : keySet) {
            widgets.get(key).addToXml(editMask);
        }
        return EditMask.loadFrom(xmlBeanInstance);
    }

    @Override
    public void setEditMask(EditMask editMask) {
        if (editMask instanceof EditMaskFilePath) {
            final org.radixware.schemas.editmask.RadixEditMaskDocument xmlBeanInstance
                    = org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
            final org.radixware.schemas.editmask.EditMask em
                    = xmlBeanInstance.addNewRadixEditMask();
            editMask.writeToXml(em);

            final Set<EEditMaskOption> keySet = widgets.keySet();
            for (EEditMaskOption key : keySet) {
                widgets.get(key).loadFromXml(em);
            }
        } else {
            throw new IllegalArgumentError("Instance of EditMaskFilePath expected");
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
        for (EEditMaskOption key : options) {
            setting = widgets.get(key);
            if (setting != null) {
                setting.show();
            }
        }
    }

    @Override
    public void setEnabledOptions(EnumSet<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for (EEditMaskOption key : options) {
            setting = widgets.get(key);
            if (setting != null) {
                setting.setEnabled(true);
            }
        }
    }

    @Override
    public void setDisabledOptions(EnumSet<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for (EEditMaskOption key : options) {
            setting = widgets.get(key);
            if (setting != null) {
                setting.setDisabled(true);
            }
        }
    }

    @Override
    public boolean checkOptions() {
        return true;
    }

    @SuppressWarnings("unused")
    private void onChangeSelectionMode(EFileSelectionMode selectionMode) {
        if (selectionMode.equals(EFileSelectionMode.SELECT_FILE)) {
            filePathMimeTypes.setEnabled(true);
        } else {
            filePathMimeTypes.setDisabled(true);
        }
    }

}
