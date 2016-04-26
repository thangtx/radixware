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

package org.radixware.kernel.starter.filecache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.radixware.kernel.starter.utils.FileUtils;


public class JarCacheEntry extends CacheEntry {

    private final JarFile jar;
    private final boolean deleteOnClose;

    public JarCacheEntry(final JarFile jar, final boolean deleteOnClose) throws IOException {
        this.jar = jar;
        this.deleteOnClose = deleteOnClose;
    }

    @Override
    public void close() throws IOException {
        jar.close();
        if (deleteOnClose) {
            FileUtils.deleteFile(new File(jar.getName()));
        }
    }

    @Override
    public byte[] getData(final String name) throws IOException {
        final JarEntry entry = jar.getJarEntry(name);
        if (entry == null) {
            return null;
        }
        final byte buf[] = new byte[(int) entry.getSize()];
        final InputStream in = jar.getInputStream(entry);
        try {
            int count = 0;
            while (count != buf.length) {
                final int r = in.read(buf, count, buf.length - count);
                if (r == -1) {
                    break;
                }
                count += r;
            }
        } finally {
            in.close();
        }
        return buf;
    }

    public String getFileName() {
        return jar.getName();
    }

    public JarFile getJarFile() {
        return jar;
    }
}
