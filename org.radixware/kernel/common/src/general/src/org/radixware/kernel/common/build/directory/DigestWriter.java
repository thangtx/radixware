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
package org.radixware.kernel.common.build.directory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.exceptions.RadixError;

import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Hex;

public class DigestWriter {

    public static final String DIGEST_ENTRY_PATH = "META-INF/RadixWare.Digest";
    public static final String VERSION_ENTRY_PATH = "META-INF/RadixWare.Version";
    public static final String BINARY_VERSION = "1.0";

    public static void writeDigestToFile(File file, byte[] digestBytes) {
        writeDigestToFile(file, digestBytes, true);
    }

    public static File writeDigestToFile(File file, byte[] digestBytes, boolean replaceSrcFile) {
        final String digest = Hex.encode(digestBytes);
        final File tempJarFile = new File(file.getParentFile(), file.getName() + ".tmp");
        JarFile zip = null;
        JarOutputStream tempJar = null;
        try {
            zip = new JarFile(file);
            List<String> entryNames = new LinkedList<>();

            boolean entryFound = false;
            boolean versionFound = false;
            for (Enumeration<JarEntry> entries = zip.entries(); entries.hasMoreElements();) {
                JarEntry entry = entries.nextElement();
                if (DIGEST_ENTRY_PATH.equals(entry.getName())) {
                    entryFound = true;
                }
                if (VERSION_ENTRY_PATH.equals(entry.getName())) {
                    versionFound = true;
                }
                if (entry.getName().startsWith("META-INF/") && entry.getName().toLowerCase().endsWith(".rsa")) {
                    Logger.getLogger(DigestWriter.class.getName()).logp(Level.INFO,"","", "{0}: Signature file {1} excluded from result jar", new Object[]{file.getPath(), entry.getName()});
                    continue;
                }
                entryNames.add(entry.getName());
            }
            boolean needUpdateSign = true;
            boolean needUpdateVersion = true;
            byte[] buffer = new byte[1024];
            int bytesRead;
            String oldSignature = null;
            if (!entryFound) {
                entryNames.add(DIGEST_ENTRY_PATH);
            } else {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try (InputStream entryStream = zip.getInputStream(zip.getEntry(DIGEST_ENTRY_PATH))) {

                    while ((bytesRead = entryStream.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                } finally {
                }
                byte[] existing = out.toByteArray();
                if (Arrays.equals(existing, digest.getBytes(FileUtils.XML_ENCODING))) {
                    Logger.getLogger(DigestWriter.class.getName()).logp(Level.INFO,"","", "{0}: No resign required", new Object[]{file.getPath()});
                    needUpdateSign = false;
                } else {
                    oldSignature = new String(existing, FileUtils.XML_ENCODING);
                }
            }
            if (!versionFound) {
                entryNames.add(VERSION_ENTRY_PATH);
            } else {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try (InputStream entryStream = zip.getInputStream(zip.getEntry(VERSION_ENTRY_PATH))) {

                    while ((bytesRead = entryStream.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                } finally {
                }
                byte[] existing = out.toByteArray();
                if (Arrays.equals(existing, BINARY_VERSION.getBytes(FileUtils.XML_ENCODING))) {
                    needUpdateVersion = false;
                } else {
                }
            }
            if (!needUpdateVersion && !needUpdateSign) {
                return null;
            }

            Collections.sort(entryNames);
            FileOutputStream fileOut = null;
            try {
                fileOut = new FileOutputStream(tempJarFile);
                tempJar = new JarOutputStream(fileOut);
                for (String name : entryNames) {
                    if (name.equals(DIGEST_ENTRY_PATH)) {
                        JarEntry digestEntry = new JarEntry(DIGEST_ENTRY_PATH);
                        tempJar.putNextEntry(digestEntry);
                        tempJar.write(digest.getBytes(FileUtils.XML_ENCODING));
                        tempJar.closeEntry();
                    } else if (name.equals(VERSION_ENTRY_PATH)) {
                        JarEntry digestEntry = new JarEntry(VERSION_ENTRY_PATH);
                        tempJar.putNextEntry(digestEntry);
                        tempJar.write(BINARY_VERSION.getBytes(FileUtils.XML_ENCODING));
                        tempJar.closeEntry();
                    } else {
                        JarEntry e = zip.getJarEntry(name);

                        try (InputStream entryStream = zip.getInputStream(e)) {
                            tempJar.putNextEntry(new JarEntry(name));
                            while ((bytesRead = entryStream.read(buffer)) != -1) {
                                tempJar.write(buffer, 0, bytesRead);
                            }
                        } finally {
                            tempJar.closeEntry();
                        }
                    }
                }
            } finally {
                try {
                    if (tempJar != null) {
                        tempJar.close();
                    }
                } catch (IOException e) {
                    throw new RadixError("Unable to sign file " + file.getPath(), e);
                }
                try {
                    if (fileOut != null) {
                        fileOut.close();
                    }
                } catch (IOException e) {
                }
            }
            if (replaceSrcFile) {
                FileUtils.copyFile(tempJarFile, file);
                Logger.getLogger(DigestWriter.class.getName()).logp(Level.INFO,"","", "{0}: File signature updated. Was: {1}, now: {2}", new Object[]{file.getPath(), (oldSignature == null ? "unsigned" : oldSignature), digest});
                return null;
            } else {
                return tempJarFile;
            }
        } catch (IOException e) {
            throw new RadixError("Unable to sign file " + file.getPath(), e);
        } finally {
            try {
                if (zip != null) {
                    zip.close();
                }
            } catch (IOException e) {
            }
            if (replaceSrcFile) {
                FileUtils.deleteFile(tempJarFile);
            }
        }
    }
}
