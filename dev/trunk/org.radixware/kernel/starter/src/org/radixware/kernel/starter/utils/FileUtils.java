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

package org.radixware.kernel.starter.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;


public class FileUtils {

    static public void removeRecursively(File file) throws RadixLoaderException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                removeRecursively(f);
            }
        }
        if (!deleteFile(file)) {
            throw new RadixLoaderException("Can't delete " + file.getPath());
        }
    }

    public static byte[] readFile(final File f) throws RadixLoaderException {
        try {
            final FileInputStream in = new FileInputStream(f);
            try {
                if (f.length() > Integer.MAX_VALUE) {
                    throw new RadixLoaderException("File " + f.getAbsolutePath() + " is too big");
                }
                final int len = (int) f.length();
                final byte[] bytes = new byte[len];
                int readed = 0;
                while (readed < len) {
                    final int lastReaded = in.read(bytes, readed, len - readed);
                    if (lastReaded < 0) {
                        throw new RadixLoaderException("Can't load file: " + f.getAbsolutePath());
                    }
                    readed += lastReaded;
                }
                return bytes;
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    // do nothing
                }
            }
        } catch (IOException ex) {
            throw new RadixLoaderException("Can't load file: " + f.getAbsolutePath(), ex);
        }
    }

    /**
     * Delete file.
     *
     * @return true if successfully, false otherwise.
     */
    public static boolean deleteFile(File file) {
        int counter = 0;
        while (counter < 10) {
            if (file.delete()) {
                return true;
            }
            if (!file.exists()) {
                return true;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
            counter++;

        }
        return false;
    }

    public static byte[] calcFileDigest(InputStream stream, final boolean ignoreCR) throws IOException, NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-1");

        final byte[] buffer = new byte[1024];
        for (;;) {
            int l = stream.read(buffer);
            if (l < 0) {
                break;
            }

            if (ignoreCR) {
                int count = 0;
                for (int i = 0, j = 0; i < l; i++) {
                    if (buffer[i] == 0xD) {
                        count++;
                    } else {
                        if (i != j) {
                            buffer[j] = buffer[i];
                        }
                        j++;
                    }
                }
                l = l - count;
            }
            digest.update(buffer, 0, l);
        }
        return digest.digest();
    }

    public static byte[] calcFileDigest(byte[] bytes, final boolean ignoreCR) throws IOException, NoSuchAlgorithmException {
        return calcFileDigest(new ByteArrayInputStream(bytes), ignoreCR);
    }

    public static byte[] readJarDigest(JarFile jarFile) throws IOException {
        final ZipEntry entry = jarFile.getEntry("META-INF/RadixWare.Digest");
        if (entry != null) {
            try (InputStream in = jarFile.getInputStream(entry)) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[512];
                int count = -1;
                while ((count = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, count);
                }
                return HexConverter.fromHex(new String(out.toByteArray(), "UTF-8"));
            }
        }
        return null;
    }
}
