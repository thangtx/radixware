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

package org.radixware.kernel.common.utils;

import java.io.File;

public class FilePathPrefix {

    private final String pathPrefix;

    public FilePathPrefix(String pathPrefix) {
        this.pathPrefix = preparePrefix(pathPrefix);
    }
    
    public static String preparePrefix(String prefix){
        String result = prefix.replace(File.separatorChar, '/');
        if (!result.endsWith("/")) {
            result += "/";
        }
        return result;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public String getFilePath(File f) {
        if (f != null) {
            return pathPrefix + f.getName();
        }
        return null;
    }
}
