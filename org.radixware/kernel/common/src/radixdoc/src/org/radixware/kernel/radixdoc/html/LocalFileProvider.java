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

package org.radixware.kernel.radixdoc.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public abstract class LocalFileProvider extends FileProvider {

    private final String rootPath;
    private final File out;

    public LocalFileProvider(String rootPath, File out) {
        this.rootPath = rootPath;
        this.out = out;
    }

    @Override
    public InputStream getInputStream(String fileName) {
        try {
            return new FileInputStream(rootPath + "/" + fileName);
        } catch (FileNotFoundException e) {
//            Logger.getLogger(LocalFileProvider.class.getName()).log(Level.INFO, "", e);
            return null;
        }
    }

    @Override
    public File getOutput() {
        return out;
    }

    public String getRootPath() {
        return rootPath;
    }
}
