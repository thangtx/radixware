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

package org.radixware.kernel.release.fs;

import java.io.File;
import org.radixware.kernel.starter.radixloader.RadixLoader;


public class RfsUtils {

    public static File createTmpDir() {
        File file = RadixLoader.getInstance().createTempFile("rfs_cache");
        file.delete();
        file.mkdirs();
        return file;
    }
    public static File createTmpFile() {
        return RadixLoader.getInstance().createTempFile("rfs_tmp");        
    }
}
