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
package org.radixware.wps.views.editors.valeditors;

import java.nio.file.Paths;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EMimeType;

import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.uploading.FileUploader;

public class ValFilePathEditorController extends InputBoxController<String, EditMaskFilePath> {

    private InputBox.ValueController<String> valueController;
    private FileDialogButton fileDialogButton;
    private FileUploader fileUploader;
    private EMimeType mimeType;
    private boolean handleInputAvailable;

    private class FileDialogButton extends ToolButton {

        public FileDialogButton() {
            super();
            fileUploader = new FileUploader(getEnvironment(), this, null);
            this.setFileUploader(fileUploader);
            final Icon openDialogIcon =
                    getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.OPEN);
            setIcon(openDialogIcon);
            if (mimeType != null) {
                fileUploader.setAcceptedMimeType(mimeType.getValue());
            }
            this.addClickHandler(new IButton.ClickHandler() {
                @Override
                public void onClick(IButton source) {
                    edit();
                }
            });
        }
    }

    public ValFilePathEditorController(IClientEnvironment env) {
        super(env);
        EditMaskFilePath mask = new EditMaskFilePath();
        setEditMask(mask);
        fileDialogButton = new FileDialogButton();
        fileDialogButton.getHtml().setCss("cursor", "pointer");
        this.addButton(fileDialogButton);
    }

    public ValFilePathEditorController(IClientEnvironment env, EditMaskFilePath mask) {
        super(env);
        setEditMask(mask);
        fileDialogButton = new FileDialogButton();
        this.addButton(fileDialogButton);
    }

    @Override
    protected InputBox.ValueController<String> createValueController() {
        valueController = new InputBox.ValueController<String>() {
            @Override
            public String getValue(final String path) throws InputBox.InvalidStringValueException {
                final EditMaskFilePath maskFilePath = getEditMask();
                String p = maskFilePath.toStr(getEnvironment(), path);
                return Paths.get(p).normalize().toString();
            }
        };
        return valueController;
    }

    private void edit() {
        fileUploader.addSelectFileListener(new FileUploader.SelectFileListener() {
            @Override
            public void fileSelected(String fileName, Long fileSize) {
                getInputBox().setValue(fileName);
            }
        });
    }

    public void setValueController(InputBox.ValueController<String> valueController) {
        this.valueController = valueController;
        getInputBox().setReadOnly(isReadOnly() || !editMask.getHandleInputAvailable() || valueController == null);
    }

    @Override
    public void setEditMask(EditMaskFilePath editMask) {
        this.editMask = editMask;
        this.handleInputAvailable = editMask.getHandleInputAvailable();
        this.mimeType = editMask.getMimeType();
        updateButtons();
        super.setEditMask(editMask);

    }

    private void updateButtons() {
        if (getInputBox() != null) {
            getInputBox().setReadOnly(isReadOnly() || !handleInputAvailable);
        }
        if (mimeType != null) {
            fileUploader.setAcceptedMimeType(mimeType.getValue());
        }
    }

    @Override
    protected ValidationResult calcValidationResult(String value) {
        if (value != null && value.contains("[<:\"\'>?*]")) {
            return ValidationResult.INVALID;
        }
        return ValidationResult.ACCEPTABLE;
    }

    @Override
    protected void applyEditMask(final InputBox box) {
        super.applyEditMask(box);
        if (box != null) {
            box.updataButtonsState();
        }
    }
}
