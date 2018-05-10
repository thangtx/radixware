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

package org.radixware.kernel.common.client.dialogs;

import org.radixware.kernel.common.enums.EFileDialogOpenMode;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;
import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.common.enums.EMimeType;


public class FileDialogSettings implements IFileDialogSettings {

    private String title;
    private EnumSet<EMimeType> types;
    private EFileSelectionMode selectionMode;
    private String initialPath;
    private EFileDialogOpenMode openMode;
    private boolean showFullPath;

    public FileDialogSettings(final EFileDialogOpenMode openMode) {//default settings constructor
        this("File Dialog", EnumSet.noneOf(EMimeType.class), EFileSelectionMode.SELECT_FILE, null, openMode);
    }
    
    public FileDialogSettings(final IClientEnvironment environment, final EditMaskFilePath mask) {
        this(mask.getFileDialogTitle(environment.getDefManager()), mask.getMimeTypes(), mask.getSelectionMode(), mask.getInitialPath(), mask.getDialogMode());
    }    
    
    public FileDialogSettings(final String dialogTitle,
                              final EMimeType mimeType,
                              final EFileSelectionMode selectionMode,
                              final String initPath,
                              final EFileDialogOpenMode openMode) {//parameterized constructor
        this(dialogTitle, EnumSet.of(mimeType), selectionMode, initPath, openMode);
    }    

    public FileDialogSettings(final String dialogTitle,
                              final EnumSet<EMimeType> mimeTypes,
                              final EFileSelectionMode selectionMode,
                              final String initPath,
                              final EFileDialogOpenMode openMode) {//parameterized constructor
        if (dialogTitle == null) {
            this.title = "File Dialog";
        } else {
            this.title = dialogTitle;
        }
        types = mimeTypes==null ? null : EnumSet.copyOf(mimeTypes);
        this.selectionMode = selectionMode==null ? EFileSelectionMode.SELECT_FILE : selectionMode;
        this.openMode = openMode==null ? EFileDialogOpenMode.LOAD : openMode;
        
        if (initPath == null || initPath.isEmpty()) {
            initialPath = System.getProperty("user.home");
        } else {
            this.initialPath = initPath;
        }        
    }

    @Override
    public String getFileDialogTitle() {
        return title;
    }

    public void setFileDialogTitle(final String title) {
        this.title = title;
    }

    public void setFileSelectionMode(final EFileSelectionMode mode) {
        this.selectionMode = mode;
    }

    @Override
    public EFileSelectionMode getFileSelectionMode() {
        return selectionMode;
    }

    
    public void setMimeType(final EMimeType type) {
        this.types = type==null ? null : EnumSet.of(type);
    }
    
    public void setMimeTypes(final EnumSet<EMimeType> types){
        this.types = types==null ? null : EnumSet.copyOf(types);
    }

    @Override
    public EnumSet<EMimeType> getMimeTypes() {
        return types==null ? EnumSet.noneOf(EMimeType.class) : EnumSet.copyOf(types);
    }

    @Override
    public String getInitialPath() {
        return initialPath;
    }

    public void setInitialPath(final String path) {
        this.initialPath = path;        
    }
    
    public void setFileDialogOpenMode(final EFileDialogOpenMode openMode) {
        this.openMode = openMode;
    }

    @Override
    public EFileDialogOpenMode getFileDialogOpenMode() {
        return openMode;
    }

    
    public boolean getShowFullPath() {
        return showFullPath;
    }

    public void setShowFullPath(final boolean showFullPath) {
        this.showFullPath = showFullPath;
    }
    
}
