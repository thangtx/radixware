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
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EFileDialogOpenMode;
import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.Id;

public final class EditMaskFilePath extends EditMask {

    private static final EnumSet<EValType> SUPPORTED_VALTYPES
            = EnumSet.of(EValType.STR, EValType.ARR_STR);
    private EFileSelectionMode selectionMode = EFileSelectionMode.SELECT_FILE;//недоступно на web
    private EFileDialogOpenMode dialogMode;
    boolean isHandleInputAvailable;
    private EnumSet<EMimeType> mimeTypes;
    private String fileDialogTitle;//недоступно на web
    private String initialPath;
    private boolean checkIfPathExists;
    private Id fileDialogTitleId;
    private Id titleOwnerId;
    private boolean storeLastPath = true;

    public EditMaskFilePath() {
        this(EFileSelectionMode.SELECT_FILE, true, null, true, "File Dialog", false);
    }

    public EditMaskFilePath(final EFileSelectionMode mode,
            final boolean handleInput,
            final EMimeType mimeType,
            final boolean store,
            final Id fileDialogTitleId,
            final boolean checkIfPathExists) {
        super();
        if (mimeType != null) {
            mimeTypes = EnumSet.of(mimeType);
        }

        this.isHandleInputAvailable = handleInput;
        this.selectionMode = mode == null ? EFileSelectionMode.SELECT_FILE : mode;
        this.fileDialogTitleId = fileDialogTitleId;
        this.checkIfPathExists = checkIfPathExists;
        this.storeLastPath = store;
    }

    public EditMaskFilePath(final EFileSelectionMode mode,
            final boolean handleInput,
            final EMimeType mimeType,
            final boolean store,
            final String title,
            final boolean checkIfPathExists) {
        super();
        if (mimeType != null) {
            mimeTypes = EnumSet.of(mimeType);
        }

        this.isHandleInputAvailable = handleInput;
        this.selectionMode = mode == null ? EFileSelectionMode.SELECT_FILE : mode;
        this.fileDialogTitle = title;
        this.checkIfPathExists = checkIfPathExists;
        this.storeLastPath = store;
    }

    public EditMaskFilePath(final EditMaskFilePath source) {
        super();
        this.mimeTypes = source.mimeTypes;
        this.isHandleInputAvailable = source.isHandleInputAvailable;
        this.selectionMode = source.selectionMode;
        this.dialogMode = source.dialogMode;
        this.fileDialogTitle = source.fileDialogTitle;
        this.checkIfPathExists = source.checkIfPathExists;
        this.fileDialogTitleId = source.fileDialogTitleId;
        this.initialPath = source.initialPath;
    }

    protected EditMaskFilePath(final org.radixware.schemas.editmask.EditMaskFilePath editMask) {
        super();
        this.fileDialogTitle = editMask.getFileDialogTitle();
        if (editMask.getMimeTypes() != null) {
            final List<EMimeType> typesFromXml = editMask.getMimeTypes().getItemList();
            if (typesFromXml != null && !typesFromXml.isEmpty()) {
                mimeTypes = EnumSet.copyOf(typesFromXml);
            }
        } else if (editMask.getMimeType() != null) {
            mimeTypes = EnumSet.of(editMask.getMimeType());
        }
        this.initialPath = editMask.getInitialPath();
        this.isHandleInputAvailable = editMask.getHandleInputAvailable();
        if (editMask.getSelectionMode() == null) {
            this.selectionMode = EFileSelectionMode.SELECT_FILE;
        } else {
            this.selectionMode = editMask.getSelectionMode();
        }
        if (editMask.getFileDialogMode() != null) {
            this.dialogMode = editMask.getFileDialogMode();
        }
        this.checkIfPathExists = editMask.getCheckIfPathExists();
        this.fileDialogTitleId = editMask.getFileDialogTitleId();
        this.storeLastPath = editMask.getStoreLastPath();
    }

    @Override
    public String toStr(final IClientEnvironment environment, final Object o) {
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
    public ValidationResult validate(final IClientEnvironment environment, final Object o) {
        if (o == null) {
            return ValidationResult.ACCEPTABLE;
        }
        if (o instanceof Arr) {
            return validateArray(environment, (Arr) o);
        }
        if (o instanceof String) {//result of a string validation depends on Environment.
            if (checkIfPathExists && environment.getApplication().getRuntimeEnvironmentType() == ERuntimeEnvironmentType.EXPLORER) {
                final String filePath = (String) o;
                final Path path;
                try {
                    path = Paths.get(filePath);
                } catch (InvalidPathException ex) {
                    return ValidationResult.Factory.newIntermediateResult(InvalidValueReason.Factory.createForWrongFormatValue(environment));
                }
                if (path == null) {
                    return ValidationResult.Factory.newIntermediateResult(InvalidValueReason.Factory.createForWrongFormatValue(environment));
                }
                final File file;
                try {
                    file = path.toFile();
                } catch (UnsupportedOperationException ex) {
                    return ValidationResult.Factory.newIntermediateResult(InvalidValueReason.Factory.createForWrongFormatValue(environment));
                }
                final boolean isDirectory;
                final boolean isFile;
                try {
                    if (!file.exists()) {
                        final String reason
                                = environment.getMessageProvider().translate("ExplorerMessage", "File does not exist");
                        return ValidationResult.Factory.newIntermediateResult(reason);
                    }
                    isDirectory = file.isDirectory();
                    isFile = file.isFile();
                } catch (SecurityException ex) {
                    final String reason
                            = environment.getMessageProvider().translate("ExplorerMessage", "File does not exist");
                    return ValidationResult.Factory.newIntermediateResult(reason);
                }
                if (getSelectionMode() == EFileSelectionMode.SELECT_DIRECTORY && !isDirectory) {
                    final String reason
                            = environment.getMessageProvider().translate("ExplorerMessage", "Value is not path to directory");
                    return ValidationResult.Factory.newIntermediateResult(reason);
                } else if (getSelectionMode() == EFileSelectionMode.SELECT_FILE && !isFile) {
                    final String reason
                            = environment.getMessageProvider().translate("ExplorerMessage", "Value is not path to file");
                    return ValidationResult.Factory.newIntermediateResult(reason);
                }
            }
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
        final org.radixware.schemas.editmask.EditMaskFilePath editMaskFilePath = editMask.addNewFilePath();
        editMaskFilePath.setSelectionMode(selectionMode == null ? EFileSelectionMode.SELECT_FILE : selectionMode);
        if (dialogMode != null) {
            editMaskFilePath.setFileDialogMode(dialogMode);
        }
        if (fileDialogTitle != null) {
            editMaskFilePath.setFileDialogTitle(fileDialogTitle);
        } else {
            fileDialogTitle = "File Dialog";
            editMaskFilePath.setFileDialogTitle(fileDialogTitle);
        }
        if (mimeTypes != null && !mimeTypes.isEmpty()) {
            if (editMaskFilePath.getMimeTypes() == null) {
                editMaskFilePath.addNewMimeTypes();
            }
            final org.radixware.schemas.editmask.EditMaskFilePath.MimeTypes xmlMimeTypes = editMaskFilePath.getMimeTypes();

            for (EMimeType mimeType : mimeTypes) {
                xmlMimeTypes.getItemList().add(mimeType);
            }
        }
        if (titleOwnerId != null) {
            editMaskFilePath.setTitleOwnerId(titleOwnerId);
        }
        if (fileDialogTitleId != null) {
            editMaskFilePath.setFileDialogTitleId(titleOwnerId);
        }
        if (initialPath != null) {
            editMaskFilePath.setInitialPath(initialPath);
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
        if (!Objects.equals(this.mimeTypes, other.mimeTypes)) {
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
        if (!Objects.equals(this.selectionMode, other.selectionMode)) {
            return false;
        }
        if (!Objects.equals(this.dialogMode, other.dialogMode)) {
            return false;
        }
        if (!Objects.equals(this.fileDialogTitleId, other.fileDialogTitleId)) {
            return false;
        }
        if (!Objects.equals(this.titleOwnerId, other.titleOwnerId)) {
            return false;
        }
        if (!Objects.equals(this.initialPath, other.initialPath)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.fileDialogTitle);
        hash = 29 * hash + Objects.hashCode(this.mimeTypes);
        hash = 29 * hash + Objects.hashCode(this.selectionMode);
        hash = 29 * hash + Objects.hashCode(this.dialogMode);
        hash = 29 * hash + (this.isHandleInputAvailable ? 1 : 0);
        hash = 29 * hash + (this.checkIfPathExists ? 1 : 0);
        hash = 29 * hash + (this.storeLastPath ? 1 : 0);
        hash = 29 * hash + Objects.hashCode(this.fileDialogTitleId);
        hash = 29 * hash + Objects.hashCode(this.titleOwnerId);
        hash = 29 * hash + Objects.hashCode(this.initialPath);
        hash = 29 * hash + super.hashCode();
        return hash;
    }

    public void setSelectionMode(final EFileSelectionMode mode) {
        if (mode != null && mode != this.selectionMode) {
            this.selectionMode = mode;
            afterModify();
        }
    }

    public EFileSelectionMode getSelectionMode() {
        return selectionMode == null ? EFileSelectionMode.SELECT_FILE : selectionMode;
    }

    public EFileDialogOpenMode getDialogMode() {
        if (dialogMode == null) {
            return checkIfPathExists ? EFileDialogOpenMode.LOAD : EFileDialogOpenMode.SAVE;
        } else {
            return dialogMode;
        }
    }

    public void setDialogMode(final EFileDialogOpenMode dialogMode) {
        if (dialogMode != null && dialogMode != this.dialogMode) {
            this.dialogMode = dialogMode;
            afterModify();
        }
    }

    public void setHandleInputAvailable(final boolean enabled) {
        if (enabled != isHandleInputAvailable) {
            this.isHandleInputAvailable = enabled;
            afterModify();
        }
    }

    public boolean getHandleInputAvailable() {
        return isHandleInputAvailable;
    }

    public void setStoreLastPathInConfig(final boolean store) {
        if (this.storeLastPath != store) {
            this.storeLastPath = store;
            afterModify();
        }
    }

    public boolean getStoreLastPathInConfig() {
        return storeLastPath;
    }

    public String getInitialPath() {
        return initialPath;
    }

    public void setInitialPath(final String initialPath) {
        if (!Objects.equals(initialPath, this.initialPath)) {
            this.initialPath = initialPath;
            afterModify();
        }
    }

    public void setFileDialogTitle(final String title) {
        if (title != null && !title.equals(fileDialogTitle)) {
            this.fileDialogTitle = title;
            afterModify();
        }
    }

    public void setMimeType(final EMimeType filter) {
        mimeTypes = filter == null ? null : EnumSet.of(filter);
        afterModify();
    }

    public void setMimeTypes(final EnumSet<EMimeType> types) {
        if (!Objects.equals(mimeTypes, types)) {
            mimeTypes = types == null ? null : EnumSet.copyOf(types);
            afterModify();
        }
    }

    public EnumSet<EMimeType> getMimeTypes() {
        return mimeTypes == null ? EnumSet.noneOf(EMimeType.class) : EnumSet.copyOf(mimeTypes);
    }

    public boolean getCheckIfPathExists() {
        return checkIfPathExists;
    }

    public void setCheckIfPathExists(final boolean check) {
        if (checkIfPathExists != check) {
            checkIfPathExists = check;
            afterModify();
        }
    }

    public String getFileDialogTitle(final DefManager manager) {
        if (fileDialogTitle == null && fileDialogTitleId != null && titleOwnerId != null) {
            fileDialogTitle = manager.getMlStringValue(titleOwnerId, fileDialogTitleId);
        }
        return fileDialogTitle;
    }

    public Id getTitleOwnerId() {
        return this.titleOwnerId;
    }

    public void setTitleOwnerId(final Id ownerId) {
        this.titleOwnerId = ownerId;
        afterModify();
    }
}
