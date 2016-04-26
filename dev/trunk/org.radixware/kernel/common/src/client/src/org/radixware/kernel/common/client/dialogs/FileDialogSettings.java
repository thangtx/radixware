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

import java.io.File;
import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.common.enums.EMimeType;


public class FileDialogSettings implements IFileDialogSettings {


    private String title;
    private EMimeType type;
    private EFileSelectionMode selectionMode;
    private String initialPath;
    private EFileDialogOpenMode openMode;
    private boolean showFullPath;

    public FileDialogSettings(EFileDialogOpenMode openMode) {//default settings constructor
        this("File Dialog", null, EFileSelectionMode.SELECT_FILE, null, openMode);
    }

    public FileDialogSettings(String dialogTitle, EMimeType mimeType,
            EFileSelectionMode selectionMode, String initPath,
            EFileDialogOpenMode openMode) {//parameterized constructor
        if (dialogTitle == null) {
            this.title = "File Dialog";
        } else {
            this.title = dialogTitle;
        }
        this.type = mimeType;
        if (selectionMode == null) {
            this.selectionMode = EFileSelectionMode.SELECT_FILE;
        } else {
            this.selectionMode = selectionMode;
        }
        if (initPath == null || initPath.isEmpty()) {
            initialPath = System.getProperty("user.home");
        } else {
            this.initialPath = initPath;
        }
        if (openMode == null) {
            this.openMode = EFileDialogOpenMode.LOAD;
        }
        this.openMode = openMode;
        updateSettings();
    }

    @Override
    public String getFileDialogTitle() {
        return title;
    }

    @Override
    public void setFileDialogTitle(String title) {
        this.title = title;
        updateSettings();
    }

    @Override
    public void setFileSelectionMode(EFileSelectionMode mode) {
        this.selectionMode = mode;
        updateSettings();
    }

    @Override
    public EFileSelectionMode getFileSelectionMode() {
        return selectionMode;
    }

    @Override
    public void setMimeType(EMimeType type) {
        this.type = type;
        updateSettings();
    }

    @Override
    public EMimeType getMimeType() {
        return type;
    }

    @Override
    public String getInitialPath() {
        return initialPath;
    }

    @Override
    public void setInitialPath(String path) {
        this.initialPath = path;
        updateSettings();
    }
    
    @Override
    public void setFileDialogOpenMode(EFileDialogOpenMode openMode) {
        this.openMode = openMode;
        updateSettings();
    }

    @Override
    public EFileDialogOpenMode getFileDialogOpenMode() {
        return openMode;
    }

    public boolean getShowFullPath() {
        return showFullPath;
    }

    public void setShowFullPath(boolean showFullPath) {
        this.showFullPath = showFullPath;
        updateSettings();
    }
    
    private void updateSettings(){

    }
}
