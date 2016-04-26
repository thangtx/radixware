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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Allows to make file operations by external tools.
 * For example, by Netbeans FileObject class.
 * That allows, for example, automatically make SVN rename then file renamed, etc.
 * For more information, see realization in RadixWare Designer: FileOperationsImpl class.
 */
public class FileOperations {

    protected FileOperations() {
    }
    private static volatile FileOperations registered = null;

    public static void register(FileOperations fileOperations) {
        if (fileOperations == null) {
            throw new NullPointerException();
        }
        if (registered != null) {
            throw new IllegalStateException("Attempt to register FileOperations twice");
        }
        registered = fileOperations;
    }

    protected static FileOperations getDefault() {
        FileOperations result = registered;
        if (result == null) {
            result = new FileOperations();
        }
        return result;
    }

    /**
     * Change file name within a parent folder.
     * See org.openide.filesystems.FileObject.rename(...);
     */
    protected void rename(final File src, final String newBaseName, String newExt) throws IOException {
        final String newName = (newExt != null && !newExt.isEmpty() ? newBaseName + "." + newExt : newBaseName);
        final File dest = new File(src.getParent(), newName);
        if (src.isDirectory()) {
            renameDirectory(src, dest);
        } else {
            renameFile(src, dest);
        }
    }

    /**
     * Rename source directory to destination one.
     * @throws IOException if source is not directory, or destination already exist, or unable to rename.
     */
    private static void renameDirectory(final File src, final File dest) throws IOException {
        final String error = "Unable to rename directory '" + src.getAbsolutePath() + "' to '" + dest.getAbsolutePath() + "'";
        if (!src.isDirectory()) {
            throw new FileNotFoundException(error + ": source directory not found.");
        }
        if (dest.exists()) {
            throw new IOException(error + ": destination already exist.");
        }
        if (!src.renameTo(dest)) {
            throw new IOException(error + ".");
        }
    }

    /**
     * Rename source file to destination one.
     * If destination file already exist it will deleted before.
     */
    private static void renameFile(final File src, final File dest) throws IOException {
        final String error = "Unable to rename file '" + src.getAbsolutePath() + "' to '" + dest.getAbsolutePath() + "'";
        if (!src.isFile()) {
            throw new FileNotFoundException(error + ": source file not found.");
        }
        if (!src.canRead()) {
            throw new IOException(error + ": unable to read source file.");
        }
        if (dest.isFile() && !dest.delete()) {
            throw new IOException(error + ": unable to delete destination file.");
        }
        if (!src.renameTo(dest)) {
            throw new IOException(error + ".");
        }
    }

    /**
     * Delete file or directory (recursively).
     * See org.openide.filesystems.FileObject.delete();
     */
    protected void deleteFile(final File file) throws IOException {
        if (!FileUtils.deleteFile(file)) {
            throw new IOException("Unable to delete file '" + file.getPath() + "'.");
        }
    }

    protected OutputStream getOutputStream(final File file) throws IOException {
        final File parent = file.getParentFile();
        mkDirs(parent);
        return new FileOutputStream(file);
    }

    protected void mkDirs(File dir) throws IOException {
        if (!dir.mkdirs()) {
            if (!dir.isDirectory()) {
                throw new IOException("Unable to create directory '" + dir.getAbsolutePath() + "'.");
            }
        }
    }
}
