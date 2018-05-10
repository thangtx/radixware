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

import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.valeditors.ValArrEditor;
import org.radixware.schemas.editmask.EditMask;
import org.radixware.schemas.editmask.EditMaskFilePath;

final class FilePathMimeTypesSetting extends QWidget
        implements IEditMaskEditorSetting {

    private ValArrEditor listMimeTypeOptions;
    private EditMaskList listEditMask;
    List<EditMaskList.Item> items = new LinkedList<>();
    IClientEnvironment environment;

    public FilePathMimeTypesSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        this.environment = environment;
        setUpUi(environment);
    }

    private void setUpUi(IClientEnvironment environment) {
        listMimeTypeOptions = new ValArrEditor(environment, EValType.STR, null, this);
        listMimeTypeOptions.setMandatory(false);
        listMimeTypeOptions.setDuplicatesEnabled(false);
        for (EMimeType s : EMimeType.values()) {
            items.add(new EditMaskList.Item(s.getTitle(), s.getValue()));
        }
        final QHBoxLayout layout = new QHBoxLayout();
        layout.addWidget(listMimeTypeOptions);
        listMimeTypeOptions.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed);
        listEditMask = new EditMaskList(items, true, -1);
        listMimeTypeOptions.setEditMask(listEditMask);
        this.setLayout(layout);
    }

    @Override
    public void addToXml(EditMask editMask) {
        EditMaskFilePath.MimeTypes mt = EditMaskFilePath.MimeTypes.Factory.newInstance();
        if (listMimeTypeOptions.getValue() != null) {
            for (Object item : listMimeTypeOptions.getValue()) {
                mt.getItemList().add(EMimeType.getForValue((String) item));
            }
            editMask.getFilePath().setMimeTypes(mt);
        }
    }

    @Override
    public void loadFromXml(EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskFilePath editMaskFilePath
                = editMask.getFilePath();
        if (editMaskFilePath.isSetMimeTypes()) {
            ArrStr arr = new ArrStr();
            for (EMimeType mimeType : editMaskFilePath.getMimeTypes().getItemList()) {
                arr.add(mimeType.getValue());
            }
            listMimeTypeOptions.setValue(arr);
        } else {
            setDefaultValue();
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.FILEPATH_FILE_TYPE_FILTER;
    }

    @Override
    public void setDefaultValue() {
        listMimeTypeOptions.clear();
        ArrStr arr = new ArrStr();
        arr.add(EMimeType.IMAGE_JPEG.getValue());
        listMimeTypeOptions.setValue(arr);
    }

    @Override
    public Object getValue() {
        return listMimeTypeOptions.getValue();
    }
}
