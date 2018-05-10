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

package org.radixware.kernel.explorer.env.session.resources;

import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.QSettings;
import com.trolltech.qt.gui.QFileDialog;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.resources.AbstractFileDirResource;
import org.radixware.kernel.common.utils.SystemTools;


final class FileDirResource extends AbstractFileDirResource{
    
    private final static FileDirResource INSTANCE = new FileDirResource();
    
    private FileDirResource(){
    }
    
    public static FileDirResource getInstance(){
        return INSTANCE;
    }
    
    @Override
    public String select(final IClientEnvironment environment, final String title, final String dirName) {
        final String startDir = dirName==null || dirName.isEmpty() ? QDir.homePath() : dirName;
        environment.getProgressHandleManager().blockProgress();
        try{
            return QFileDialog.getExistingDirectory(null, title, startDir);
        }finally{
            environment.getProgressHandleManager().unblockProgress();
        }
    }
    
    @Override
    public String getUserDownloadsDirPath() {
        if (SystemTools.isWindows){
            final QSettings registry = new QSettings("HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders",QSettings.Format.NativeFormat);
            final String path = (String)registry.value("{374DE290-123F-4565-9164-39C4925E467B}");
            return path==null || path.isEmpty() ? super.getUserDownloadsDirPath() : path;
        }else{
            return super.getUserDownloadsDirPath();
        }
   }             
}
