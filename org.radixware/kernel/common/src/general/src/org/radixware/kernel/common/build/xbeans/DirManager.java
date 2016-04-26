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

package org.radixware.kernel.common.build.xbeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

interface DirManager {

    File createDir(File rootdir, String subdir);

    void removeDir(File dir);

    File createTempDir() throws IOException;

    void deleteObsoleteFiles(File rootDir, File srcDir, Set<?> seenFiles);

    List<String> list(File path);

    InputStream read(File f) throws FileNotFoundException;

    OutputStream write(File f) throws FileNotFoundException;

    void jarDir(File dir, File jar) throws IOException;
}
