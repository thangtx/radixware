/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.dialogs.udsbrowser;

import java.io.File;
import java.util.Collections;
import java.util.List;


final class DownloadingResult {
    
    public final static DownloadingResult EMPTY = new DownloadingResult();
    
    private final List<File> files;
    private final int modulesCount;
    
    private DownloadingResult(){
        files = Collections.<File>emptyList();
        modulesCount = 0;
    }

    public DownloadingResult(final List<File> files, final int modulesCount) {
        this.files = files;
        this.modulesCount = modulesCount;
    }

    public List<File> getFiles() {
        return files;
    }

    public int getModulesCount() {
        return modulesCount;
    }

}
