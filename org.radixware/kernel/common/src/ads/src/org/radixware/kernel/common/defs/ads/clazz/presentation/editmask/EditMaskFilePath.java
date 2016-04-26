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
package org.radixware.kernel.common.defs.ads.clazz.presentation.editmask;

import java.util.Collection;
import java.util.EnumSet;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;

import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;

public class EditMaskFilePath extends EditMask implements ILocalizedDef {

    private EnumSet<EValType> compatibleTypes = EnumSet.of(EValType.STR, EValType.ARR_STR);
    private EFileSelectionMode mode;
    boolean isHandleInputAvailable;
    private EMimeType mimeType;
    private String fileDialogTitle;
    boolean checkIfPathExists;
    public Id fileDialogTitleId;
    public Id titleOwnerId;
    private boolean storeLastPath = true;

    public EditMaskFilePath(RadixObject context, boolean virtual) {
        super(context, virtual);
        mode = EFileSelectionMode.SELECT_FILE;
        isHandleInputAvailable = true;
        mimeType = null;
        fileDialogTitle = "File Dialog";
        checkIfPathExists = false;
        storeLastPath = true;
    }

    public EditMaskFilePath(RadixObject context, org.radixware.schemas.editmask.EditMaskFilePath xDef, boolean virtual) {
        super(context, virtual);
        mode = xDef.getSelectionMode();
        fileDialogTitle = xDef.getFileDialogTitle();
        mimeType = xDef.getMimeType();
        isHandleInputAvailable = xDef.getHandleInputAvailable();
        checkIfPathExists = xDef.getCheckIfPathExists();
        fileDialogTitleId = xDef.getFileDialogTitleId();
        titleOwnerId = xDef.getTitleOwnerId();
        storeLastPath = xDef.getStoreLastPath();
               
    }

    @Override
    public void appendTo(org.radixware.schemas.editmask.EditMask xDef) {
        org.radixware.schemas.editmask.EditMaskFilePath x = xDef.addNewFilePath();
        if (fileDialogTitle == null) {
            x.setFileDialogTitle("File Dialog");
        } else {
            x.setFileDialogTitle(fileDialogTitle);
        }
        x.setMimeType(mimeType);
        x.setCheckIfPathExists(checkIfPathExists);

        if (mode == null) {
            x.setSelectionMode(EFileSelectionMode.SELECT_FILE);
        } else {
            x.setSelectionMode(mode);
        }
        x.setHandleInputAvailable(isHandleInputAvailable);
        x.setStoreLastPath(storeLastPath);
        if (fileDialogTitleId != null) {
            x.setFileDialogTitleId(fileDialogTitleId);
        }
        if (titleOwnerId != null) {
            x.setTitleOwnerId(titleOwnerId);
        }
    }

    @Override
    public boolean isCompatible(EValType valType) {
        if (compatibleTypes.contains(valType)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.FILE_PATH;
    }

    @Override
    public void applyDbRestrictions() {
    }

    public void setSelectionMode(EFileSelectionMode mode) {
        this.mode = mode;
        modified();
    }

    public EFileSelectionMode getSelectionMode() {
        if (mode != null) {
            return mode;
        } else {
            return EFileSelectionMode.SELECT_FILE;
        }
    }

    public void setHandleInputAvailable(boolean enabled) {
        this.isHandleInputAvailable = enabled;
        modified();
    }

    public boolean getHandleInputAvailable() {
        return isHandleInputAvailable;
    }

    public void setFileDialogTitle(String title) {
        this.fileDialogTitle = title;
        modified();
    }

    public String getFileDialogTitle() {
        return fileDialogTitle;
    }

    public void setTitleId(Id id) {
        this.fileDialogTitleId = id;
        modified();
    }

    public Id getTitleId() {
        return fileDialogTitleId;
    }

    public Id getTitleOwnerId() {
        return this.titleOwnerId;
    }

    public void setTitleOwnerId(Id ownerId) {
        this.titleOwnerId = ownerId;
        modified();
    }

    public void setMimeType(EMimeType filter) {
        this.mimeType = filter;
        modified();
    }

    public EMimeType getMimeType() {
        return mimeType;
    }

    public boolean getCheckIfPathExists() {
        return checkIfPathExists;
    }

    public void setCheckIfPathExists(boolean check) {
        checkIfPathExists = check;
        modified();
    }

    public void setStoreLastPathInConfig(boolean store) {
        this.storeLastPath = store;
        modified();
    }

    public boolean getStoreLastPathInConfig() {
        return storeLastPath;
    }

    @Override
    protected void modified() {
        super.modified();
        update();
    }

    private void update() {
        if (getFileDialogTitle() == null || getFileDialogTitle().isEmpty()) {
            setFileDialogTitle("File Dialog");
        }
    }

    private EditMaskFilePath getEditMaskFilePath() {
        return this;
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        ids.add(new MultilingualStringInfo(this) {
            @Override
            public String getContextDescription() {
                return "File dialog title.";
            }

            @Override
            public Id getId() {
                return fileDialogTitleId;
            }

            @Override
            public EAccess getAccess() {
                AdsDefinition def = getEditMaskFilePath().getOwnerDef();
                return def == null ? EAccess.DEFAULT : def.getAccessMode();
            }

            @Override
            public void updateId(Id newId) {
                fileDialogTitleId = newId;
            }

            @Override
            public boolean isPublished() {
                AdsDefinition def = getEditMaskFilePath().getOwnerDef();
                return def == null ? false : def.isPublished();
            }
        });
    }

    @Override
    public AdsMultilingualStringDef findLocalizedString(Id stringId) {
        if (this == null || this.getOwnerDef() == null) {
            return null;
        } else {
            return this.getOwnerDef().findLocalizedString(stringId);
        }
    }
}
