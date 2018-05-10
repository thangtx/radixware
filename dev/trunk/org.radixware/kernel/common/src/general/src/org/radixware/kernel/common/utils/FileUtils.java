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
package org.radixware.kernel.common.utils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang.StringUtils;
import org.radixware.kernel.common.exceptions.ShouldNeverHappenError;
import org.radixware.kernel.common.repository.fs.JarFileDataProvider;

public class FileUtils {

    private static final int BUFFER_SIZE = 8192;
    public static final String XML_ENCODING = "UTF-8";
    public static final String SQL_ENCODING = "UTF-8";
    public static final String LOG_ENCODING = "UTF-8";
    public static final String DIRECTORY_XML_FILE_NAME = "directory.xml";
    public static final String LICENSE_TXT_FILE_NAME = "license.txt";
    public static final String DEFINITIONS_XML_FILE_NAME = "definitions.xml";
    public static final String SQML_DEFINITIONS_XML_FILE_NAME = "sqmldefs.xml";
    public static final String LICENSES_XML_FILE_NAME = "licenses.xml";
    private static final String JAVA_IO_TEMP_DIR_PROP_NAME = "java.io.tmpdir";
    private static final String JAVA_IO_TEMP_DIR_PATH = System.getProperty(JAVA_IO_TEMP_DIR_PROP_NAME);

    public static List<File> getFilesList(final File directory) throws IOException {
        final List<File> list = new ArrayList();
        for (final File fileEntry : directory.listFiles()) {
            if (!fileEntry.isDirectory()) {
                list.add(fileEntry);
            }
        }
        return list;
    }

    /**
     * Copy or merge two directories.
     */
    public static void copyDirectory(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                FileUtils.mkDirs(targetLocation);
            }

            final String[] childrenNames = sourceLocation.list();
            for (String childrenName : childrenNames) {
                copyDirectory(
                        new File(sourceLocation, childrenName),
                        new File(targetLocation, childrenName));
            }
        } else {
            copyFile(sourceLocation, targetLocation);
        }
    }

    /**
     * Copy or merge two directories.
     */
    public static void copyDirectory(File sourceLocation, File targetLocation, FileFilter filter) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                FileUtils.mkDirs(targetLocation);
            }

            final File[] children = sourceLocation.listFiles(filter);
            for (File child : children) {
                copyDirectory(child, new File(targetLocation, child.getName()), filter);
            }
        } else {
            copyFile(sourceLocation, targetLocation);
        }
    }

    /**
     * Delete directory recursively.
     *
     * @return true if successfully, false otherwise.
     */
    public static boolean deleteDirectory(File path) {
        if (path != null && path.exists()) {
            final String[] names = path.list();
            if (names != null) {
                for (int i = 0; i < names.length; i++) {
                    File file = new File(path, names[i]);
                    if (file.isDirectory()) {
                        if (!deleteDirectory(file)) {
                            return false;
                        }
                    } else {
                        if (!deleteFile(file)) {
                            return false;
                        }
                    }
                }
            }
            return deleteFileImpl(path);
        } else {
            return false;
        }
    }

    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            return deleteDirectory(file);
        }
        return deleteFileImpl(file);
    }

    /**
     * Delete file.
     *
     * @return true if successfully, false otherwise.
     */
    private static boolean deleteFileImpl(File file) {
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

    public static boolean moveFile(final File fromFile, final File toFile) throws IOException {
        if (fromFile.renameTo(toFile)) {
            return true;
        }
        copyFile(fromFile, toFile);
        if (!toFile.exists()) {
            return false;
        }
        return deleteFile(fromFile);
    }

    /**
     * Delete file or directory recursively.
     *
     * @return true if successfully, false otherwise.
     */
    public static boolean deleteFileOrDirectory(File path) {
        if (path == null || !path.exists()) {
            return false;
        }
        if (path.isDirectory()) {
            return deleteDirectory(path);
        }
        return deleteFile(path);
    }

    public static void mkDirs(File dir) throws IOException {
        FileOperations.getDefault().mkDirs(dir);
    }

    /**
     * Delete file using registeren file operation processor.
     *
     * @throws IOException if unable to delete file
     */
    public static void deleteFileExt(File file) throws IOException {
        FileOperations.getDefault().deleteFile(file);
    }

    public static OutputStream getOutputStream(final File file) throws IOException {
        return FileOperations.getDefault().getOutputStream(file);
    }

    public static class DefaultFileOperations extends FileOperations {

        public DefaultFileOperations() {
        }
    }

    public static OutputStream getOutputStreamNoLock(final File file) throws IOException {
        return new DefaultFileOperations().getOutputStream(file);
    }

    /**
     * Copy file. If destination file already exist it will be rewrited.
     */
    public static void copyFile(final File fileFrom, final File fileTo) throws IOException {
        FileInputStream from = null;
        OutputStream to = null;

        try {
            from = new FileInputStream(fileFrom);
            to = FileOperations.getDefault().getOutputStream(fileTo);

            final byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = from.read(buffer)) != -1) {
                to.write(buffer, 0, bytesRead);
            }
        } finally {
            if (from != null) {
                from.close();
            }
            if (to != null) {
                to.close();
            }
        }
    }

    public static boolean unpackZipStream(InputStream zipData, File dir, Collection<String> entries) {

        java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(zipData);
        java.util.zip.ZipEntry entry;
        try {
            while ((entry = zis.getNextEntry()) != null) {
                entries.add(entry.getName());
                java.io.File output = new java.io.File(dir, entry.getName());

                try {
                    java.io.FileOutputStream out = new java.io.FileOutputStream(output);
                    try {
                        org.radixware.kernel.common.utils.FileUtils.copyStream(zis, out);
                    } finally {
                        try {
                            out.close();
                        } catch (IOException ex) {
                        }
                    }
                } catch (IOException ex) {
                }
            }
        } catch (IOException ex) {
            return false;
        } finally {
            try {
                zis.close();
            } catch (IOException ex) {
            }
        }
        return true;
    }

    public static interface EntryFilter {

        boolean accept(ZipEntry entry);
    }

    public static boolean repackagekZipStream(InputStream inData, OutputStream outZip, EntryFilter filter) {

        java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(inData);
        java.util.zip.ZipEntry inEntry;

        ZipOutputStream zout = new ZipOutputStream(outZip);
        try {
            while ((inEntry = zis.getNextEntry()) != null) {
                if (filter.accept(inEntry)) {
                    ZipEntry outEntry = new ZipEntry(inEntry.getName());
                    zout.putNextEntry(outEntry);
                    try {
                        try {
                            org.radixware.kernel.common.utils.FileUtils.copyStream(zis, zout);
                        } catch (IOException ex) {
                            return false;
                        }
                    } finally {
                        zout.closeEntry();
                    }
                }
            }
        } catch (IOException ex) {
            return false;
        } finally {
            try {
                zis.close();
            } catch (IOException ex) {
            }
            try {
                zout.finish();
            } catch (IOException ex) {
            }
        }
        return true;
    }

    public static void copyStream(final InputStream stramFrom, final OutputStream streamTo) throws IOException {
        final byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;

        while ((bytesRead = stramFrom.read(buffer)) != -1) {
            streamTo.write(buffer, 0, bytesRead);
        }
    }

    /**
     * Get file base name (without parent folder and extension).
     *
     * @return, for example, "test" for "c:\dir\test.txt";
     */
    public static String getFileBaseName(final File file) {
        final String fileNameExt = file.getName();
        final int pos = fileNameExt.lastIndexOf('.');
        final String result = (pos >= 0 ? fileNameExt.substring(0, pos) : fileNameExt);
        return result;
    }

    public static String getFileBaseName(final String fileNameWithExt) {
        final int pos = fileNameWithExt.lastIndexOf('.');
        final String result = (pos >= 0 ? fileNameWithExt.substring(0, pos) : fileNameWithExt);
        return result;
    }

    /**
     * @return, for example, "txt" for "c:\dir\test.txt", empty string for
     * "c:\dir\test"
     */
    public static String getFileExt(final File file) {
        final String fileNameExt = file.getName();
        final int pos = fileNameExt.lastIndexOf('.');
        final String result = (pos >= 0 && pos < fileNameExt.length() - 1 ? fileNameExt.substring(pos + 1) : "");
        return result;
    }

    public static String getFileExt(final String fileNameWithExt) {
        final int pos = fileNameWithExt.lastIndexOf('.');
        final String result = (pos >= 0 && pos < fileNameWithExt.length() - 1 ? fileNameWithExt.substring(pos + 1) : "");
        return result;
    }

    public static String readTextFile(final File file, final String encoding) throws IOException {
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(file);
            return readTextStream(fileStream, encoding);
        } finally {
            if (fileStream != null) {
                fileStream.close();
            }
        }
    }

    public static String readTextStream(final InputStream textStream, final String encoding) throws IOException {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(textStream, encoding);

            final char[] buf = new char[BUFFER_SIZE];
            final StringBuilder results = new StringBuilder();
            int count = reader.read(buf);

            while (count >= 0) {
                results.append(buf, 0, count);
                count = reader.read(buf);
            }

            return results.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static byte[] readBinaryFile(final File file) throws IOException {
        final FileInputStream reader = new FileInputStream(file);
        try {
            final int len = (int) file.length();
            final byte[] result = new byte[len];
            if (reader.read(result) == len) {
                return result;
            } else {
                throw new IOException("File '" + file.getAbsolutePath() + "' was externally modified during reading.");
            }
        } finally {
            reader.close();
        }
    }

    public static byte[] readBinaryStream(final InputStream stream) throws IOException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] bytes = new byte[4096];
            int count = stream.read(bytes);

            while (count >= 0) {
                out.write(bytes, 0, count);
                count = stream.read(bytes);
            }
            return out.toByteArray();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * generates file path elements list
     */
    private static List<String> getPathList(File f) {
        List<String> l = new ArrayList();
        File r;
        try {
            r = f.getCanonicalFile();
            while (r != null) {
                l.add(r.getName());
                r = r.getParentFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            l = null;
        }
        return l;
    }

    /**
     * match file path component list
     */
    private static String matchPathLists(List<String> r, List<String> f) {
        int i;
        int j;
        String s;

        s = "";
        i = r.size() - 1;
        j = f.size() - 1;

        while ((i >= 0) && (j >= 0) && (r.get(i).equals(f.get(j)))) {
            i--;
            j--;
        }

        for (; i >= 0; i--) {
            s += ".." + File.separator;
        }

        for (; j >= 1; j--) {
            s += f.get(j) + File.separator;
        }

        s += f.get(j);
        return s;
    }

    public static String getRelativePath(File home, File f) {
        final List<String> homelist = getPathList(home);
        final List<String> filelist = getPathList(f);
        final String result = matchPathLists(homelist, filelist);
        return result;
    }

    public static void writeBytes(File file, byte[] bytes) throws IOException {
        OutputStream to = null;
        try {
            to = FileOperations.getDefault().getOutputStream(file);
            to.write(bytes, 0, bytes.length);
            to.flush();
        } finally {
            if (to != null) {
                to.close();
            }
        }
    }

    public static void writeBytes(OutputStream to, byte[] bytes) throws IOException {
        try {
            to.write(bytes, 0, bytes.length);
            to.flush();
        } finally {
            if (to != null) {
                to.close();
            }
        }
    }

    public static void writeString(OutputStream to, String chars, String encoding) throws IOException {
        byte[] bytes = chars.getBytes(encoding);
        writeBytes(to, bytes);
    }

    public static void writeString(File file, String chars, String encoding) throws IOException {
        byte[] bytes = chars.getBytes(encoding);
        writeBytes(file, bytes);
    }

    /**
     * Change file name within a parent folder. See
     * org.openide.filesystems.FileObject.rename(...);
     */
    public static void rename(final File src, final String newBaseName, String newExt) throws IOException {
        FileOperations.getDefault().rename(src, newBaseName, newExt);
    }

    public static byte[] getInputStreamAsByteArray(InputStream stream, int length) throws IOException {
        byte[] contents;
        if (length == -1) {
            contents = new byte[0];
            int contentsLength = 0;
            int amountRead = -1;
            do {
                int amountRequested = Math.max(stream.available(), 4096);

                // resize contents if needed
                if (contentsLength + amountRequested > contents.length) {
                    System.arraycopy(contents, 0,
                            contents = new byte[contentsLength + amountRequested], 0, contentsLength);
                }

                // read as many bytes as possible
                amountRead = stream.read(contents, contentsLength,
                        amountRequested);

                if (amountRead > 0) {
                    // remember length of contents
                    contentsLength += amountRead;
                }
            } while (amountRead != -1);

            // resize contents if necessary
            if (contentsLength < contents.length) {
                System.arraycopy(contents, 0,
                        contents = new byte[contentsLength], 0, contentsLength);
            }
        } else {
            contents = new byte[length];
            int len = 0;
            int readSize = 0;
            while ((readSize != -1) && (len != length)) {
                // See PR 1FMS89U
                // We record first the read size. In this case len is the actual
                // read size.
                len += readSize;
                readSize = stream.read(contents, len, length - len);
            }
        }

        return contents;
    }

    public static boolean isParentOf(final File dir, final File file) {
        if (file == null || dir == null) {
            return false;
        }
        for (File f = file.getParentFile(); f != null; f = f.getParentFile()) {
            if (dir.equals(f)) {
                return true;
            }
        }
        return false;
    }

    public static HashMap<String, byte[]> readJar(File file) throws IOException {
        HashMap<String, byte[]> result = new HashMap<String, byte[]>();
        ZipFile zip = null;
        try {
            zip = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                byte[] bytes = JarFileDataProvider.getZipEntryByteContent(e, zip);
                if (bytes == null) {
                    bytes = new byte[0];
                }
                result.put(e.getName(), bytes);
            }
        } finally {
            if (zip != null) {
                try {
                } catch (Exception e) {
                    zip.close();
                }
            }
        }
        return result;
    }
    private static final int READ_BLOCK_SIZE = 4 * 1024;

    private static byte[] readJarEntryData(final ZipInputStream jar) throws IOException {
        final ByteArrayOutputStream bstream = new ByteArrayOutputStream(32 * READ_BLOCK_SIZE);
        final byte[] block = new byte[READ_BLOCK_SIZE];
        int blockSize;
        for (;;) {
            blockSize = jar.read(block);
            if (blockSize == -1) {
                break;
            }
            if (blockSize != 0) {
                bstream.write(block, 0, blockSize);
            }
        }
        return bstream.toByteArray();
    }

    public static HashMap<String, byte[]> readJar(InputStream stream) throws IOException {
        HashMap<String, byte[]> result = new HashMap<>();
        JarInputStream zip = null;
        try {
            zip = new JarInputStream(stream);
            JarEntry e = zip.getNextJarEntry();
            while (e != null) {
                if (!e.isDirectory()) {
                    byte[] bytes = readJarEntryData(zip);
                    if (bytes == null) {
                        bytes = new byte[0];
                    }
                    result.put(e.getName(), bytes);
                }
                e = zip.getNextJarEntry();
            }
        } finally {
            if (zip != null) {
                try {
                } catch (Exception e) {
                    zip.close();
                }
            }
        }
        return result;
    }

    public static String findFirstChangeInJars(File oldJar, File newJar) throws IOException {
        final HashMap<String, byte[]> one = readJar(oldJar);
        final HashMap<String, byte[]> another = readJar(newJar);
        for (String n : one.keySet()) {
            if (!another.containsKey(n)) {
                return "Deleted '" + n + "'";
            } else {
                byte[] bytes1 = one.get(n);
                byte[] bytes2 = another.get(n);
                if (!Arrays.equals(bytes1, bytes2)) {
                    return "Modified '" + n + "'";
                }
            }
        }
        for (String n : another.keySet()) {
            if (!one.containsKey(n)) {
                return "Added '" + n + "'";
            }
        }
        return null;
    }

    public static byte[] getZipEntryByteContent(ZipEntry ze, ZipFile zip) throws IOException {
        InputStream stream = null;
        InputStream zis = null;
        try {
            zis = zip.getInputStream(ze);
            if (zis != null) {
                stream = new BufferedInputStream(zis);
                return FileUtils.getInputStreamAsByteArray(stream, (int) ze.getSize());
            } else {
                return new byte[0];
            }
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
            if (zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static File[] getFilesByPrefixAndPostfix(final File dir, final String prefix, final String postfix) {
        return dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().startsWith(prefix) && file.getName().endsWith(postfix);
            }
        });
    }
//    private static final char[] ILLEGAL_CHARS = new char[]{
//        ':',
//        '\\',
//        '/',
//        '*',
//        '?',
//        '>', '<'};

    public static String string2UniversalFileName(String string, char illegalCharReplacement) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = string.length(); i < len; i++) {
            char c = string.charAt(i);
            switch (c) {
                case ':':
                case '\\':
                case '"':
                case '|':
                case '/':
                case '*':
                case '?':
                case '>':
                case '<':
                case '\n':
                    c = illegalCharReplacement;
                    break;
            }
            sb.append(c);
        }
        String testString = sb.toString();
        try {
            StringBuilder checkString = new StringBuilder();
            checkString.append(testString).append(".ext");

            byte[] bytes = checkString.toString().getBytes("UTF-8");

            while (bytes.length > 150) {
                testString = testString.substring(0, testString.length() - 1);
                checkString.setLength(0);
                checkString.append(testString).append(".ext");
                bytes = checkString.toString().getBytes("UTF-8");
            }
            return testString;
        } catch (UnsupportedEncodingException ex) {
        }

        return sb.toString();
    }

    public static String string2UniversalFileNameNoExt(String string, char illegalCharReplacement) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = string.length(); i < len; i++) {
            char c = string.charAt(i);
            switch (c) {
                case ':':
                case '\\':
                case '"':
                case '|':
                case '/':
                case '*':
                case '?':
                case '>':
                case '<':
                case '\n':
                    c = illegalCharReplacement;
                    break;
            }
            sb.append(c);
        }
        String testString = sb.toString();
        try {
            StringBuilder checkString = new StringBuilder();
            checkString.append(testString);

            byte[] bytes = checkString.toString().getBytes("UTF-8");

            while (bytes.length > 150) {
                testString = testString.substring(0, testString.length() - 1);
                checkString.setLength(0);
                checkString.append(testString);
                bytes = checkString.toString().getBytes("UTF-8");
            }
            return testString;
        } catch (UnsupportedEncodingException ex) {
        }

        return sb.toString();
    }

    public static String convert2UniqueFileName(File contextDir, String fileName) {
        File file = new File(contextDir, fileName);
        if (file.exists()) {
            int extStart = fileName.lastIndexOf(".");
            if (extStart > 0) {
                String name = fileName.substring(0, extStart);
                String ext = fileName.substring(extStart);
                for (int index = 1;; index++) {
                    String newFileName = name + String.valueOf(index) + ext;
                    file = new File(contextDir, newFileName);
                    if (!file.exists()) {
                        return newFileName;
                    }

                }
            } else {

                for (int index = 1;; index++) {
                    String newFileName = fileName + String.valueOf(index);
                    file = new File(contextDir, newFileName);
                    if (!file.exists()) {
                        return newFileName;
                    }

                }
            }
        } else {
            return fileName;
        }
    }

    static public void writeToFile(final File file, final String data) throws IOException {
        appendOrWriteToFile(file, data, false);
    }

    static public void writeToFile(final File file, final String data, final String charset) throws IOException {
        appendOrWriteToFile(file, data, false, charset);
    }

    static public void appendToFile(final File file, final String data, final String charset) throws IOException {
        appendOrWriteToFile(file, data, true, charset);
    }

    static public void appendToFile(final File file, final String data) throws IOException {
        appendOrWriteToFile(file, data, true);
    }

    static private void appendOrWriteToFile(final File file, final String data, final boolean append, final String charset) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset));
            writer.write(data.toCharArray());
        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }
    }

    static private void appendOrWriteToFile(final File file, final String data, final boolean append) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, append));
            writer.write(data.toCharArray());
        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * Use {@link FileUtils#createTempFileViaRadixLoader(String)} if you don't
     * need suffix
     *
     * @param prefix file name before randomly generated name part
     * @param suffix file extension
     * @return
     */
    public static File createTempFile(final String prefix, String suffix) {
        suffix = StringUtils.defaultIfBlank(suffix, "tmp");
        final File tmpFile = new File(JAVA_IO_TEMP_DIR_PATH, prefix + "-" + UUID.randomUUID().toString() + "." + suffix);
        return tmpFile;
    }

    public static File createTempFileViaRadixLoader(final String prefix) {
        try {
            Class<?> radixLoaderClass = Class.forName("org.radixware.kernel.starter.radixloader.RadixLoader");
            Method getInstanceMethod = radixLoaderClass.getDeclaredMethod("getInstance");
            Object radixLoader = getInstanceMethod.invoke(null);
            if (radixLoader != null) {
                Method createTempFileMethod = radixLoaderClass.getDeclaredMethod("createTempFile", String.class);
                final File file = (File) createTempFileMethod.invoke(radixLoader, prefix);
                return file;
            } else {
                final File file = File.createTempFile(prefix, "");
                file.deleteOnExit();
                return file;
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException ex) {
            throw new ShouldNeverHappenError(ex);
        }
    }
}
