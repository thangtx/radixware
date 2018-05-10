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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.schemas.product.Directory;
import org.radixware.schemas.product.DirectoryDocument;

public class DirectoryFileSigner {

    public static void main(final String[] args) {
        try {
            if (args.length < 1) {
                throw new RadixError("Command line must be: DirectoryFile [KeyAliase Password [KeyStore]]");
            }
            PrivateKey privKey = null;
            if (args.length > 1) {
                final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                String ksfn;
                if (args.length < 4) {
                    ksfn = System.getProperty("user.home") + File.separator + ".keystore";
                } else {
                    ksfn = args[3];
                }
                final FileInputStream ksfnStream = new FileInputStream(ksfn);
                try {
                    keyStore.load(ksfnStream, args[2].toCharArray());
                } finally {
                    ksfnStream.close();
                }

                final Key key = keyStore.getKey(args[1], args[2].toCharArray());
                if (key == null) {
                    throw new RadixError("Key " + args[1] + " not found");
                }
                if (!(key instanceof PrivateKey)) {
                    throw new RadixError("Key " + args[1] + " is not private");
                }
                privKey = (PrivateKey) key;
            }
            new DirectoryFileSigner().process(args[0], privKey);
        } catch (Exception ex) {
            Logger.getLogger(DirectoryFileSigner.class.getName()).log(Level.SEVERE, "Error: ", ex);
        }
    }

    public void process(final String directoryFileName, final PrivateKey key) throws IOException, NoSuchAlgorithmException, XmlException, InvalidKeyException, SignatureException {
        final File directory = new File(directoryFileName);
        if (!directory.canWrite()) {
            throw new RadixError("The file is not accessible: " + directory.getAbsolutePath());
        }
        final DirectoryDocument doc = DirectoryDocument.Factory.parse(directory);

        Signature signer = null;
        if (key != null) {
            signer = Signature.getInstance("SHA1with" + key.getAlgorithm());
            signer.initSign(key);
        }
        final Charset utf16 = Charset.forName("UTF-16");
        final File homeDir = directory.getParentFile();

        if (doc.getDirectory() != null && doc.getDirectory().getFileGroups() != null) {
            for (Directory.FileGroups.FileGroup group : doc.getDirectory().getFileGroups().getFileGroupList()) {
                for (Directory.FileGroups.FileGroup.File file : group.getFileList()) {
                    File fsFile = new File(homeDir, file.getName());
                    final byte[] digest = calcFileDigest(fsFile);
                    file.setDigest(digest);

                    if (file.getName().toLowerCase().endsWith(".jar")) {
                        DigestWriter.writeDigestToFile(fsFile, digest);
                    }

                    if (signer != null) {
                        signer.update(file.getName().getBytes(utf16));
                        signer.update(digest);
                    }
                }
            }

            if (signer != null) {
                final org.radixware.schemas.product.Signature sign = doc.getDirectory().addNewSignature();
                sign.setAlgorithm(signer.getAlgorithm());
                sign.setByteArrayValue(signer.sign());
            }
        }

        final XmlOptions opt = new XmlOptions();
        opt.setSaveNamespacesFirst();
        opt.setUseDefaultNamespace();
        opt.setSavePrettyPrint();
        doc.save(directory, opt);
    }
    private static final Object bufferLock = new Object();
    private static WeakReference<byte[]> bufferRef = null;

    private static byte[] getBuffer() {
        synchronized (bufferLock) {
            byte[] result = null;
            if (bufferRef != null) {
                result = bufferRef.get();
            }
            if (result == null) {
                result = new byte[10240];
                bufferRef = new WeakReference<byte[]>(result);
            }
            return result;
        }
    }

    private static InputStream getManifestInputStream(InputStream entryStream) throws IOException {
        try {
            String text = FileUtils.readTextStream(entryStream, FileUtils.XML_ENCODING);
            String[] lines = text.split("\n");
            StringBuilder result = new StringBuilder();
            if (lines != null) {
                for (int i = 0; i < lines.length; i++) {
                    if (lines[i].startsWith("Ant-Version:")) {
                        lines[i] = "Ant-Version: Suppressed by RadixWare";
                    } else if (lines[i].startsWith("Created-By:")) {
                        lines[i] = "Created-By: Suppressed by RadixWare";
                    }
                    result.append(lines[i]).append("\n");
                }
            }
            byte[] bytes = result.toString().getBytes(FileUtils.XML_ENCODING);
            return new ByteArrayInputStream(bytes);
        } finally {
            entryStream.close();
        }
    }

        
    private static boolean canIgnoreCalcDigestForFile(final String zipEntryName) {
        if (zipEntryName.startsWith("META-INF/")) {
            final String zipEntryNameAsLower = zipEntryName.toLowerCase();
            final boolean isSertificateWithPublicKey = zipEntryNameAsLower.endsWith(".rsa");
            final boolean isSignature = zipEntryNameAsLower.endsWith(".sf");
            return isSertificateWithPublicKey || isSignature;
        }
        return false;
    }
    
    public static byte[] calcFileDigest(final File file, final boolean isJar, final boolean ignoreCR) throws IOException, NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-1");

        if (isJar) {
            final ZipFile jar = new ZipFile(file);
            try {
                final Enumeration<? extends ZipEntry> entries_enum = jar.entries();
                final List<String> entryNames = new LinkedList<>();

                boolean versionFound = false;
                while (entries_enum.hasMoreElements()) {
                    ZipEntry e = entries_enum.nextElement();
                    if (DigestWriter.VERSION_ENTRY_PATH.equals(e.getName())) {
                        versionFound = true;
                    }
                    entryNames.add(e.getName());
                }
                if (!versionFound) {
                    entryNames.add(DigestWriter.VERSION_ENTRY_PATH);
                }
                Collections.sort(entryNames);

                for (String entryName : entryNames) {
                    if (DigestWriter.VERSION_ENTRY_PATH.equals(entryName) && !versionFound) {
                        digest.update(DigestWriter.BINARY_VERSION.getBytes("UTF-8"));
                        digest.update(DigestWriter.VERSION_ENTRY_PATH.getBytes("UTF-8"));
                        continue;
                    }
                    ZipEntry entry = jar.getEntry(entryName);

                    if (!entry.isDirectory()) {
                        final InputStream entry_in;
                        if (DigestWriter.DIGEST_ENTRY_PATH.equals(entry.getName())) {
                            continue;
                        }

                        if (canIgnoreCalcDigestForFile(entry.getName())) {
                            continue;
                        }
                        
                        if ("META-INF/MANIFEST.MF".equals(entry.getName())) {
                            entry_in = getManifestInputStream(jar.getInputStream(entry));
                        } else {
                            entry_in = jar.getInputStream(entry);
                        }
                        try {
                            synchronized (bufferLock) {
                                final byte[] buf = getBuffer();
                                for (;;) {
                                    final int l = entry_in.read(buf);
                                    if (l < 0) {
                                        break;
                                    }
                                    digest.update(buf, 0, l);
                                }
                            }
                        } finally {
                            try {
                                entry_in.close();
                            } catch (IOException e) {
                            }
                        }
                        //protection against resource moving
                        //(there were problems with icons when they were been moved from explorer to common package)
                        digest.update(entry.getName().getBytes("UTF-8"));
                    }
                }
            } finally {
                try {
                    jar.close();
                } catch (IOException e) {
                }
            }
        } else {
            final FileInputStream in = new FileInputStream(file);
            try {
                synchronized (bufferLock) {
                    final byte[] buf = getBuffer();
                    for (;;) {
                        int l = in.read(buf);
                        if (l < 0) {
                            break;
                        }

                        if (ignoreCR) {
                            int count = 0;
                            for (int i = 0, j = 0; i < l; i++) {
                                if (buf[i] == 0xD) {
                                    count++;
                                } else {
                                    if (i != j) {
                                        buf[j] = buf[i];
                                    }
                                    j++;
                                }
                            }
                            l = l - count;
                        }
                        digest.update(buf, 0, l);
                    }
                }
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        return digest.digest();
    }

    public static byte[] calcFileDigest(final File file, final boolean isJar) throws IOException, NoSuchAlgorithmException {
        return calcFileDigest(file, isJar, false);
    }

    public static byte[] calcFileDigest(final File file) throws IOException, NoSuchAlgorithmException {
        return calcFileDigest(file, file.getName().toLowerCase().endsWith(".jar"), file.getName().toLowerCase().endsWith(".xml"));
    }

    public static byte[] readJarDigest(File jarFile) throws IOException {
        JarFile jar = new JarFile(jarFile);
        try {
            return readJarDigest(jar);
        } finally {
            jar.close();
        }
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
                return fromHex(new String(out.toByteArray(), "UTF-8"));
            }
        }
        return null;
    }

    static private byte[] fromHex(String hex) {
        byte[] data = new byte[hex.length() / 2];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return data;
    }
}
