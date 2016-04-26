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

package org.radixware.kernel.designer.environment.upload;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.repository.dds.DdsScript;
import org.radixware.kernel.common.repository.dds.DdsScriptsDir;


class DdsScriptsDirUploader {

    private final DdsScriptsDir scriptsDir;

    public DdsScriptsDirUploader(DdsScriptsDir scriptDir) {
        this.scriptsDir = scriptDir;
    }

    public void updateChildren() {
        final File[] fileArr = scriptsDir.collectFiles();

        // delete old scripts
        if (!scriptsDir.isEmpty()) {
            if (fileArr == null || fileArr.length == 0) {
                scriptsDir.clear();
            } else {
                final Set<File> fileSet = new HashSet<File>(Arrays.asList(fileArr));
                for (DdsScript script : scriptsDir) {
                    final File file = script.getFile();
                    if (!fileSet.contains(file)) {
                        scriptsDir.remove(script);
                    }
                }
            }
        }

        // add new scripts
        if (fileArr != null && fileArr.length > 0) {
            final Set<File> fileSet = new HashSet<File>(scriptsDir.size());
            for (DdsScript script : scriptsDir) {
                final File file = script.getFile();
                fileSet.add(file);
            }

            for (File file : fileArr) {
                if (!fileSet.contains(file)) {
                    scriptsDir.addFromFile(file);
                }
            }
        }
    }
}
