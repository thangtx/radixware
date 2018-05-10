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

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractCheckableSetting;
import org.radixware.schemas.editmask.EditMask;


final class FilePathCheckIfPathExistsSetting extends AbstractCheckableSetting implements IEditMaskEditorSetting{

    public FilePathCheckIfPathExistsSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        final String title = environment.getMessageProvider().translate("EditMask", "Path must exist");
        this.setText(title);
        this.setChecked(false);
    }
    
    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.FILEPATH_PATH_EXIST;
    }

    @Override
    public void addToXml(EditMask editMask) {
        editMask.getFilePath().setCheckIfPathExists(this.isChecked());
    }

    @Override
    public void loadFromXml(EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskFilePath ems = editMask.getFilePath();
        if (ems.getCheckIfPathExists()){
            this.setChecked(ems.getCheckIfPathExists());
        } else {
            setDefaultValue();
        }
    }

    @Override
    public void setDefaultValue() {
        this.setChecked(false);
    }

}