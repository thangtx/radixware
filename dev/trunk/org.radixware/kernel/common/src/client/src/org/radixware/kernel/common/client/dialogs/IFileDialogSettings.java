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

import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.common.enums.EMimeType;


public interface IFileDialogSettings {

    public enum EFileDialogOpenMode {

        LOAD, SAVE
    }

    public String getFileDialogTitle();

    public void setFileDialogTitle(String title);

    public void setFileSelectionMode(EFileSelectionMode mode);

    public EFileSelectionMode getFileSelectionMode();

    public void setMimeType(EMimeType type);

    public EMimeType getMimeType();

    public String getInitialPath();

    public void setInitialPath(String path);
    
    public void setFileDialogOpenMode(EFileDialogOpenMode openMode);
    
    public EFileDialogOpenMode getFileDialogOpenMode();
}
