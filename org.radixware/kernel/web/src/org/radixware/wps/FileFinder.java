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

package org.radixware.wps;

import java.io.File;


final class FileFinder {
    
    private FileFinder(){        
    }

    public static String findFile(final String originalName) {
        if (originalName==null || originalName.isEmpty()){
            return null;
        }
        final File file = new File(originalName);
        if (!file.exists()) {//try other files
            final File configFile = new File(WebServerRunParams.getConfigFile());
            if (configFile.exists()) {
                final File parentDir = configFile.getParentFile();
                final File tryFileName = new File(parentDir, originalName);
                if (tryFileName.exists()) {
                    return tryFileName.getAbsolutePath();
                } else {
                    return originalName;
                }
            } else {
                return originalName;
            }
        } else {
            return file.getAbsolutePath();
        }
    }
}
