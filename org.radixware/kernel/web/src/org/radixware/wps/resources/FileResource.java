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

package org.radixware.wps.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import org.radixware.kernel.common.client.eas.resources.IFileResource;
import org.radixware.kernel.common.client.exceptions.TerminalResourceException;
import org.radixware.kernel.common.enums.EFileAccessType;
import org.radixware.kernel.common.enums.EFileOpenMode;
import org.radixware.kernel.common.enums.EFileOpenShareMode;
import org.radixware.kernel.common.enums.EFileSeekOriginType;


public class FileResource implements IFileResource {

    private RandomAccessFile file;
    private boolean eof = false;
    private final int id;
    private final String filePath;

    public FileResource(final String fileName, final EFileOpenMode openMode, final EFileOpenShareMode share) throws IOException, TerminalResourceException {
        if (fileName == null || fileName.isEmpty()) {
            throw new TerminalResourceException("file name was not defined");
        }
        filePath = fileName;
        final File f = new File(fileName);
        id = f.hashCode();
        final String mode;
        final long seek;
        switch (share) {
            case NONE: {
                if (openMode == EFileOpenMode.CREATE) {
                    if (!f.createNewFile()) {
                        throw new TerminalResourceException("file " + " \"" + fileName + "\" already exists");
                    }
                    return;
                } else {
                    throw new IOException("File open mode \'" + openMode.getValue() + "\' is incompatible with file share mode \'" + share.getValue() + "\'");
                }
            }
            case READ: {
                if (openMode == EFileOpenMode.READ) {
                    mode = "r";
                    seek = 0;
                } else {
                    throw new TerminalResourceException("File open mode \'" + openMode.getValue() + "\' is incompatible with file share mode \'" + share.getValue() + "\'");
                }
                break;
            }
            case WRITE:
            case READ_AND_WRITE: {
                mode = "rws";
                switch (openMode) {
                    case CREATE: {
                        if (f.exists()) {
                            throw new TerminalResourceException("file " + " \"" + fileName + "\" already exists");
                        }
                        seek = 0;
                        break;
                    }
                    case TRUNCATE: {
                        if (!f.exists() || !f.isFile()) {
                            throw new TerminalResourceException("file " + " \"" + fileName + "\" is not exists");
                        }
                        if (!f.delete()) {
                            throw new TerminalResourceException("Cant overwrite file " + " \"" + fileName + "\" ");
                        }
                        seek = 0;
                        break;
                    }
                    case TRUNCATE_OR_CREATE: {
                        if (f.exists()) {
                            if (!f.isFile()) {
                                throw new TerminalResourceException(" \"" + fileName + "\" is not a file");
                            } else {
                                if (!f.delete()) {
                                    throw new TerminalResourceException("Cant overwrite file " + " \"" + fileName + "\" ");
                                }
                            }
                        }
                        seek = 0;
                        break;
                    }
                    case APPEND: {
                        if (!f.exists() || !f.isFile()) {
                            throw new TerminalResourceException("file " + " \"" + fileName + "\" is not exists");
                        }
                        seek = f.length();
                        break;
                    }
                    case APPEND_OR_CREATE: {
                        seek = f.length();
                        break;
                    }
                    default:
                        throw new TerminalResourceException("File open mode \'" + openMode.getValue() + "\' is not supported.");
                }
                break;//case WRITE: case READ_AND_WRITE:
            }
            default:
                throw new TerminalResourceException("File share open mode \'" + share.getValue() + "\' is not supported.");
        }
        file = new RandomAccessFile(f, mode);
        //   file.getChannel().
        if (seek > 0) {
            file.seek(seek);
        }
    }

    @Override
    public String getId() {
        return getClass().getName() + "@" + id;
    }

    @Override
    public boolean isOpened() {
        return file != null;
    }
    
    public String getFilePath(){
        return filePath;
    }

    @Override
    public boolean isEof() throws IOException, TerminalResourceException {
        if (file != null) {
            return eof || file.getFilePointer() == file.length();
        }
        throw new TerminalResourceException("file resource was not opened for reading");
    }

    @Override
    public int read(final byte[] buf) throws IOException, TerminalResourceException {
        if (file != null) {
            int len = file.read(buf);
            eof = len < 0;
            return len;
        }
        throw new TerminalResourceException("file resource was not opened for reading");
    }

    @Override
    public void write(byte[] content) throws IOException, TerminalResourceException {
        if (file != null) {
            file.write(content);
        } else {
            throw new TerminalResourceException("file resource was not opened for writing");
        }
    }

    @Override
    public long seek(final long pos, EFileSeekOriginType seekOrigin) throws IOException, TerminalResourceException {
        if (file == null) {
            throw new TerminalResourceException("file resource was not opened");
        }
        final long seek;
        switch (seekOrigin) {
            case FROM_BEGIN:
                seek = pos;
                break;
            case FROM_CURRENT:
                seek = file.getFilePointer() + pos;
                break;
            case FROM_END:
                seek = file.length() - pos;
                break;
            default:
                throw new TerminalResourceException("file seek origin type \'" + seekOrigin.getValue() + "\' is notsupported");
        }
        file.seek(seek);
        return file.getFilePointer();
    }

    @Override
    public void close() throws TerminalResourceException {
        if (file != null) {
            try {
                file.close();
            } catch (IOException ex) {
                throw new TerminalResourceException("cannot close file ", ex);
            }
        } else {
            throw new TerminalResourceException("file resource was not opened");
        }
    }

    @Override
    public void free() throws TerminalResourceException {
        if (isOpened()) {
            close();
        }
    }

    public static String select(org.radixware.schemas.eas.FileSelectRq request) {
        return null;
    }


    public static boolean checkAccess(final String filePath, EFileAccessType accessType) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        final File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return false;
        }
        switch (accessType) {
            case READ:
                return file.canRead();
            case WRITE:
                return file.canWrite();
            case ALL:
                return file.canRead() && file.canWrite();
            default:
                return true;
        }
    }

    public static long getSize(final String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return 0;
        }
        final File file = new File(filePath);
        return file.length();
    }

    public static void delete(final String fileName) throws TerminalResourceException {
        if (fileName == null || fileName.isEmpty()) {
            throw new TerminalResourceException("file name was not defined");
        }
        File file = new File(fileName);
        if (!file.exists() || !file.isFile()) {
            throw new TerminalResourceException("file \"" + fileName + "\" is not exists");
        }
        if (!file.delete()) {
            throw new TerminalResourceException("cannot delete file \"" + fileName + "\"");
        }
    }

    public static void move(final String srcFileName, final String dstFileName, final boolean overwrite) throws TerminalResourceException {
        if (srcFileName == null || srcFileName.isEmpty()) {
            throw new TerminalResourceException("source file name was not defined");
        }
        if (dstFileName == null || dstFileName.isEmpty()) {
            throw new TerminalResourceException("destination file name was not defined");
        }
        final File srcFile = new File(srcFileName),
                dstFile = new File(dstFileName);
        if (!srcFile.exists() || !srcFile.isFile()) {
            throw new TerminalResourceException("source file \"" + srcFileName + "\" is not exists");
        }
        if (dstFile.exists()) {
            if (overwrite) {
                if (!dstFile.delete()) {
                    throw new TerminalResourceException("destination file is already exists and cannot be removed");
                }
            } else {
                throw new TerminalResourceException("destination file \"" + srcFileName + "\" is already exists");
            }
        }
        if (!srcFile.renameTo(dstFile)) {
            throw new TerminalResourceException("cannot complete move operation");
        }
    }

    public static void copy(final String srcFileName, final String dstFileName, final boolean overwrite) throws IOException, TerminalResourceException {
        if (srcFileName == null || srcFileName.isEmpty()) {
            throw new TerminalResourceException("source file name was not defined");
        }
        if (dstFileName == null || dstFileName.isEmpty()) {
            throw new TerminalResourceException("destination file name was not defined");
        }
        final File srcFile = new File(srcFileName),
                dstFile = new File(dstFileName);
        if (!srcFile.exists() || !srcFile.isFile()) {
            throw new TerminalResourceException("source file \"" + srcFileName + "\" is not exists");
        }
        if (dstFile.exists()) {
            if (overwrite) {
                if (!dstFile.delete()) {
                    throw new TerminalResourceException("destination file already exists and cannot be removed");
                }
            } else {
                throw new TerminalResourceException("destination file \"" + srcFileName + "\" is already exists");
            }
        }
        FileChannel in = null, out = null;
        try {
            in = new FileInputStream(srcFile).getChannel();
            out = new FileOutputStream(dstFile).getChannel();

            long size = in.size();
            MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);

            out.write(buf);

        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }    
}
