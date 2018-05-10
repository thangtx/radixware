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
package org.radixware.kernel.radixdoc.generator;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.utils.FileUtils;

public abstract class DocumentationGenerator {

    protected static File getOutputDir(RadixdocGenerationTask task, EIsoLanguage lang) throws IOException {
        // outputDir
        File outputDir;
        if (task.getTargetLangs().size() > 1) {
            outputDir = new File(task.getTargetDir(), lang.getValue());
            if (!outputDir.exists()) {
                FileUtils.mkDirs(outputDir);
            }
        } else {
            outputDir = task.getTargetDir();
        }

        return outputDir;
    }
}
