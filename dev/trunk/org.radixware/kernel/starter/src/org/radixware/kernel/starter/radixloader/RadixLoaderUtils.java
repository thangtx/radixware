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

package org.radixware.kernel.starter.radixloader;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.starter.meta.LayerMeta;


public class RadixLoaderUtils {

    public static List<String> listAllLayersInDir(final File dir) {
        if (dir == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                final File childDir = new File(dir, name);
                if (childDir.exists() && childDir.isDirectory() && new File(childDir, LayerMeta.LAYER_XML_FILE).exists()) {
                    return true;
                }
                return false;
            }
        }));
    }
}
