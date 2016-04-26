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
package org.radixware.kernel.common.client.meta.mask;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.Id;

public final class EditMaskFilePath extends EditMask {

    private static final EnumSet<EValType> SUPPORTED_VALTYPES =
            EnumSet.of(EValType.STR, EValType.ARR_STR);
    EFileSelectionMode mode;//недоступно на web
    boolean isHandleInputAvailable;
    private EMimeType mimeType;
    private String fileDialogTitle;//недоступно на web
    boolean checkIfPathExists;
    public Id fileDialogTitleId;
    public Id titleOwnerId;
    private boolean storeLastPath = true;

    public EditMaskFilePath() {
        this(EFileSelectionMode.SELECT_FILE, true, null, true, "File Dialog", false);
    }

    public EditMaskFilePath(EFileSelectionMode mode, final boolean handleInput,
            final EMimeType mimeType, final boolean store, Id fileDialogTitleId, boolean checkIfPathExists) {
        super();
        this.mimeType = mimeType;

        this.isHandleInputAvailable = handleInput;
        if (mode == null) {
            mode = EFileSelectionMode.SELECT_FILE;
        }
        this.mode = mode;
        this.fileDialogTitleId = fileDialogTitleId;
        this.checkIfPathExists = checkIfPathExists;
        this.storeLastPath = store;
    }

    public EditMaskFilePath(EFileSelectionMode mode, final boolean handleInput,
            final EMimeType mimeType, final boolean store, String title, boolean checkIfPathExists) {
        super();
        this.mimeType = mimeType;

        this.isHandleInputAvailable = handleInput;
        if (mode == null) {
            mode = EFileSelectionMode.SELECT_FILE;
        }
        this.mode = mode;
        this.fileDialogTitle = title;
        this.checkIfPathExists = checkIfPathExists;
        this.storeLastPath = store;
    }

    public EditMaskFilePath(EditMaskFilePath source) {
        super();
        this.mimeType = source.mimeType;
        this.isHandleInputAvailable = source.isHandleInputAvailable;
        this.mode = source.mode;
        this.fileDialogTitle = source.fileDialogTitle;
        this.checkIfPathExists = source.checkIfPathExists;
        this.fileDialogTitleId = source.fileDialogTitleId;
    }

    protected EditMaskFilePath(final org.radixware.schemas.editmask.EditMaskFilePath editMask) {
        super();
        this.fileDialogTitle = editMask.getFileDialogTitle();
        this.mimeType = editMask.getMimeType();
        this.isHandleInputAvailable = editMask.getHandleInputAvailable();
        this.mode = editMask.getSelectionMode();
        this.checkIfPathExists = editMask.getCheckIfPathExists();
        this.fileDialogTitleId = editMask.getFileDialogTitleId();
        this.storeLastPath = editMask.getStoreLastPath();
    }

    @Override
    public String toStr(IClientEnvironment environment, Object o) {
        if (o == null) {
            return getNoValueStr(environment.getMessageProvider());
        } else if (o instanceof Arr) {
            return arrToStr(environment, (Arr) o);
        } else if (o instanceof String) {
            File f = new File((String) o);
            if (f.exists()) {
                try {
                    return f.getCanonicalPath().toString();
                } catch (IOException ex) {
                    Logger.getLogger(EditMaskFilePath.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return o.toString();
    }

    @Override
    public ValidationResult validate(IClientEnvironment environment, Object o) {
        if (o == null) {
            return ValidationResult.ACCEPTABLE;
        }
        if (o instanceof Arr) {
            return validateArray(environment, (Arr) o);
        }
        if (o instanceof String) {//result of a string validation depends on Environment.
            /*String newpath = o.toString();
             if (checkIfPathExists) {
             if (newpath.contains("[\\\\/</>*?|'\":]")){//[\\\\/:*?\\\"<>|]
             return ValidationResult.INVALID;
             }
             if (mode == EFileSelectionMode.SELECT_DIRECTORY) {//check it for web
             Path path = Paths.get(newpath);
             if (path.toFile().exists() && path.toFile().isDirectory()) {
                        
             return ValidationResult.ACCEPTABLE;
             }
             } else {
             File file = Paths.get(newpath).toFile();
             if (file.exists() && file.isFile()) {
             return ValidationResult.ACCEPTABLE;
             }
             }
             } else {
             return ValidationResult.ACCEPTABLE;
             }*/
            return ValidationResult.ACCEPTABLE;
        }
        return ValidationResult.Factory.newInvalidResult(InvalidValueReason.Factory.createForInvalidValueType(environment));
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.FILE_PATH;
    }

    @Override
    public EnumSet<EValType> getSupportedValueTypes() {
        return SUPPORTED_VALTYPES;
    }

    @Override
    public void writeToXml(org.radixware.schemas.editmask.EditMask editMask) {
        org.radixware.schemas.editmask.EditMaskFilePath editMaskFilePath = editMask.addNewFilePath();
        if (mode != null) {
            editMaskFilePath.setSelectionMode(mode);
        } else {
            mode = EFileSelectionMode.SELECT_FILE;
            editMaskFilePath.setSelectionMode(mode);
        }
        if (fileDialogTitle != null) {
            editMaskFilePath.setFileDialogTitle(fileDialogTitle);
        } else {
            fileDialogTitle = "File Dialog";
            editMaskFilePath.setFileDialogTitle(fileDialogTitle);
        }
        if (mimeType != null) {
            editMaskFilePath.setMimeType(mimeType);
        }
        if (titleOwnerId != null) {
            editMaskFilePath.setTitleOwnerId(titleOwnerId);
        }
        if (fileDialogTitleId != null) {
            editMaskFilePath.setFileDialogTitleId(titleOwnerId);
        }
        editMaskFilePath.setHandleInputAvailable(isHandleInputAvailable);
        editMaskFilePath.setCheckIfPathExists(checkIfPathExists);
        editMaskFilePath.setStoreLastPath(storeLastPath);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EditMaskFilePath other = (EditMaskFilePath) obj;
        if (!Objects.equals(this.fileDialogTitle, other.fileDialogTitle)) {
            return false;
        }
        if (!Objects.equals(this.mimeType, other.mimeType)) {
            return false;
        }
        if (this.storeLastPath != other.storeLastPath) {
            return false;
        }
        if (this.isHandleInputAvailable != other.isHandleInputAvailable) {
            return false;
        }
        if (this.checkIfPathExists != other.checkIfPathExists) {
            return false;
        }
        if (!Objects.equals(this.mode, other.mode)) {
            return false;
        }
        if (!Objects.equals(this.fileDialogTitleId, other.fileDialogTitleId)) {
            return false;
        }
        if (!Objects.equals(this.titleOwnerId, other.titleOwnerId)) {
            return false;
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.fileDialogTitle);
        hash = 29 * hash + Objects.hashCode(this.mimeType);
        hash = 29 * hash + Objects.hashCode(this.mode);
        hash = 29 * hash + (this.isHandleInputAvailable ? 1 : 0);
        hash = 29 * hash + (this.checkIfPathExists ? 1 : 0);
        hash = 29 * hash + (this.storeLastPath ? 1 : 0);
        hash = 29 * hash + Objects.hashCode(this.fileDialogTitleId);
        hash = 29 * hash + Objects.hashCode(this.titleOwnerId);
        hash = 29 * hash + super.hashCode();
        return hash;
    }

    public void setSelectionMode(EFileSelectionMode mode) {
        this.mode = mode;
        afterModify();
    }

    public EFileSelectionMode getSelectionMode() {
        if (mode != null) {
            return mode;
        } else {
            return EFileSelectionMode.SELECT_FILE;
        }
    }

    @Override
    protected void afterModify() {
        super.afterModify();
        update();
    }

    public void setHandleInputAvailable(boolean enabled) {
        this.isHandleInputAvailable = enabled;
        afterModify();
    }

    public boolean getHandleInputAvailable() {
        return isHandleInputAvailable;
    }

    public void setStoreLastPathInConfig(boolean store) {
        this.storeLastPath = store;
        afterModify();
    }

    public boolean getStoreLastPathInConfig() {
        return storeLastPath;
    }

    public void setFileDialogTitle(String title) {
        this.fileDialogTitle = title;
        afterModify();
    }

    public void setMimeType(EMimeType filter) {
        this.mimeType = filter;
        afterModify();
    }

    public EMimeType getMimeType() {
        return mimeType;
    }

    public boolean getCheckIfPathExists() {
        return checkIfPathExists;
    }

    public void setCheckIfPathExists(boolean check) {
        checkIfPathExists = check;
        afterModify();
    }

    private void update() {
        if (getFileDialogTitleAsStr() == null || getFileDialogTitleAsStr().isEmpty()) {
            setFileDialogTitle("File");
        }
    }

    public String getFileDialogTitleAsStr() {
        return this.fileDialogTitle;
    }

    public String getFileDialogTitle(DefManager manager) {
        if (fileDialogTitle == null && fileDialogTitleId != null && titleOwnerId != null) {
            fileDialogTitle = manager.getMlStringValue(titleOwnerId, fileDialogTitleId);
        }
        return fileDialogTitle;
    }

    public Id getTitleOwnerId() {
        return this.titleOwnerId;
    }

    public void setTitleOwnerId(Id ownerId) {
        this.titleOwnerId = ownerId;
        afterModify();
    }
}
