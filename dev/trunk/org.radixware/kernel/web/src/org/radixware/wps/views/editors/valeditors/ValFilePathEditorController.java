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

import java.io.IOException;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.common.enums.EMimeType;

import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.uploading.FileUploader;
import org.radixware.wps.rwt.uploading.IUploadedDataReader;

public class ValFilePathEditorController extends InputBoxController<String, EditMaskFilePath> {

    private final FileUploader fileUploader;
    private final FileDialogButton fileDialogButton = new FileDialogButton();

    private class FileDialogButton extends ToolButton {

        public FileDialogButton() {
            super();
            final Icon openDialogIcon =
                    getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.OPEN);
            setIcon(openDialogIcon);
            this.addClickHandler(new IButton.ClickHandler() {
                @Override
                public void onClick(IButton source) {
                    edit();
                }
            });
        }
    }

    public ValFilePathEditorController(final IClientEnvironment env) {
        this(env, new EditMaskFilePath(), null);
    }

    public ValFilePathEditorController(final IClientEnvironment env, final EditMaskFilePath mask) {
        this(env, mask, null);
    }
    
    public ValFilePathEditorController(final IClientEnvironment env, final EditMaskFilePath mask, final LabelFactory factory) {
        super(env,factory);
        setEditMask(mask==null ? new EditMaskFilePath() : mask);
        fileDialogButton.getHtml().setCss("cursor", "pointer");        
        this.addButton(fileDialogButton);        
        fileUploader = new FileUploader(getEnvironment(), fileDialogButton, null);
        fileDialogButton.setFileUploader(fileUploader);        
        fileDialogButton.setObjectName("tbSelectFile");
        updateInputBox();
        updateAcceptedMimeType();
    }

    @Override
    protected InputBox.ValueController<String> createValueController() {
        return new InputBox.ValueController<String>() {
            @Override
            public String getValue(final String path) throws InputBox.InvalidStringValueException {
                return getEditMask().toStr(getEnvironment(), path);
            }
        };
    }

    private void edit() {
        fileUploader.addSelectFileListener(new FileUploader.SelectFileListener() {
            @Override
            public void fileSelected(String fileName, Long fileSize) {
                getInputBox().setValue(fileName);
            }
        });
    }

    @Override
    public void setEditMask(final EditMaskFilePath editMask) {
        this.editMask = editMask;
        updateInputBox();
        updateAcceptedMimeType();
        updateFileDialogButton();
        super.setEditMask(editMask);
    }

    private void updateInputBox() {
        if (getInputBox() != null) {
            getInputBox().setReadOnly(isReadOnly() || !getEditMask().getHandleInputAvailable());
        }
    }
    
    private void updateFileDialogButton(){
        fileDialogButton.setVisible(!isReadOnly() && getEditMask().getSelectionMode()==EFileSelectionMode.SELECT_FILE);
    }
    
    private void updateAcceptedMimeType(){
        if (fileUploader!=null){
            final String acceptedTypes;
            final EnumSet<EMimeType> types = getEditMask().getMimeTypes();
            if (types==null || types.isEmpty()){
                acceptedTypes = null;
            }else{
                final StringBuilder acceptedTypesBuilder = new StringBuilder();
                for (EMimeType type: types){
                    if (type!=EMimeType.ALL_SUPPORTED && type!=EMimeType.ALL_FILES){
                        if (acceptedTypesBuilder.length()>0){
                            acceptedTypesBuilder.append(',');
                        }
                        acceptedTypesBuilder.append('.');
                        acceptedTypesBuilder.append(type.getExt());
                    }
                }
                acceptedTypes = acceptedTypesBuilder.toString();
            }
            fileUploader.setAcceptedMimeType(acceptedTypes);
        }
    }

    @Override
    protected void afterChangeReadOnly() {
        super.afterChangeReadOnly();
        updateFileDialogButton();
    }

    @Override
    public void setEnabled(final boolean isEnabled) {
        super.setEnabled(isEnabled);
        fileUploader.setEnabled(isEnabled);
    }        

    public void uploadAndReadSelectedFile (IUploadedDataReader reader){
        if (fileUploader != null) {
            try {
                fileUploader.uploadAndReadSelectedFile(reader, true);
            } catch (IOException ex) {
                Logger.getLogger(ValFilePathEditorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    protected void applyEditMask(final InputBox box) {
        super.applyEditMask(box);
        if (box != null) {
            box.updataButtonsState();
        }
    }
}
