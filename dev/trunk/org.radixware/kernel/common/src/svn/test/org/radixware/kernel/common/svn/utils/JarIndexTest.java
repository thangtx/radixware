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

package org.radixware.kernel.common.svn.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.junit.Test;
import static org.junit.Assert.*;
import org.radixware.kernel.common.build.directory.DirectoryFileSigner;
import org.radixware.kernel.common.utils.Hex;


public class JarIndexTest {

    byte[] readJar(File file, List<String> names, List<byte[]> bytes) throws ZipException, IOException, NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-1");
        final byte buf[] = new byte[10240];
        final ZipFile jar = new ZipFile(file);
        try {
            final Enumeration<? extends ZipEntry> entries_enum = jar.entries();
            final List<ZipEntry> entries = new LinkedList<ZipEntry>();
            while (entries_enum.hasMoreElements()) {
                entries.add(entries_enum.nextElement());
            }
            Collections.sort(entries, new Comparator<ZipEntry>() {

                @Override
                public int compare(final ZipEntry entry1, final ZipEntry entry2) {
                    return entry1.getName().compareTo(entry2.getName());
                }
            });
            for (ZipEntry entry : entries) {
                if (!entry.isDirectory()) {
                    names.add(entry.getName());
                    final InputStream entry_in = jar.getInputStream(entry);
                    byte[] result = new byte[4096];
                    int totalBytes = 0;
                    for (;;) {
                        final int l = entry_in.read(buf);
                        if (l < 0) {
                            break;
                        }
                        digest.update(buf, 0, l);

                        if (totalBytes + l > result.length) {
                            byte[] dummy = new byte[Math.max(result.length * 2, totalBytes + l + 11)];
                            System.arraycopy(result, 0, dummy, 0, result.length);
                            result = dummy;
                        }
                        System.arraycopy(buf, 0, result, totalBytes, l);
                        totalBytes += l;
                    }

                    if (totalBytes < result.length) {
                        byte[] dummy = new byte[totalBytes];
                        System.arraycopy(result, 0, dummy, 0, totalBytes);
                        result = dummy;
                    }
                    bytes.add(result);
                }
            }
        } finally {
            jar.close();
        }
        return digest.digest();
    }

    @Test
    public void calcDigest() throws IOException, NoSuchAlgorithmException {
        List<String> names1 = new LinkedList<String>();
        List<byte[]> bytes1 = new LinkedList<byte[]>();
        byte[] bytes = readJar(new File("/home/akrylov/Desktop/11.jar"), names1, bytes1);
        String asStr1 = Hex.encode(bytes);
        List<String> names2 = new LinkedList<String>();
        List<byte[]> bytes2 = new LinkedList<byte[]>();
        bytes = readJar(new File("/home/akrylov/Desktop/12.jar"), names2, bytes2);
        String asStr2 = Hex.encode(bytes);
        //assertEquals(asStr1, asStr2);
        String[] arr1 = names1.toArray(new String[0]);
        String[] arr2 = names2.toArray(new String[0]);
        assertArrayEquals(arr1, arr2);
        for (int i = 0; i < arr1.length; i++) {
            assertArrayEquals(arr1[i], bytes1.get(i), bytes2.get(i));
        }
    }
}
