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
package org.radixware.kernel.server.trace;

import java.io.File;
import java.util.Objects;

public final class FileLogOptions {

    private final File dir;
    private final String name;
    private final int maxFileSizeBytes;
    private final int rotationCount;
    private final boolean rotateDaily;
    private final boolean writeContextToFile;

    public FileLogOptions(final File dir, final String name, final int maxFileSizeBytes, final int rotationCount, final boolean rotateDaily, final boolean writeContextToFile) {
        this.dir = dir;
        this.name = name;
        this.maxFileSizeBytes = maxFileSizeBytes;
        this.rotationCount = rotationCount;
        this.rotateDaily = rotateDaily;
        this.writeContextToFile = writeContextToFile;
    }

    public File getDir() {
        return dir;
    }

    public int getMaxFileSizeBytes() {
        return maxFileSizeBytes;
    }

    public String getName() {
        return name;
    }

    public int getRotationCount() {
        return rotationCount;
    }

    public boolean isRotateDaily() {
        return rotateDaily;
    }

    public boolean isWriteContextToFile() {
        return writeContextToFile;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileLogOptions other = (FileLogOptions) obj;
        if (!Objects.equals(this.dir, other.dir)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.maxFileSizeBytes != other.maxFileSizeBytes) {
            return false;
        }
        if (this.rotationCount != other.rotationCount) {
            return false;
        }
        if (this.rotateDaily != other.rotateDaily) {
            return false;
        }
        if (this.writeContextToFile != other.writeContextToFile) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.dir);
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + this.maxFileSizeBytes;
        hash = 97 * hash + this.rotationCount;
        hash = 97 * hash + (this.rotateDaily ? 1 : 0);
        hash = 97 * hash + (this.writeContextToFile ? 1 : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "{\n"
                + "\tPath: " + getDir().getAbsolutePath() + "\n"
                + "\tName: " + getName() + "\n"
                + "\tMax file size (Kb): " + String.valueOf(1.0 * getMaxFileSizeBytes() / 1024) + "\n"
                + "\tMax files count: " + getRotationCount() + "\n"
                + "\tRotate daily: " + isRotateDaily() + "\n"
                + "\tWrite context to file: " + isRotateDaily() + "\n"
                + "}";
    }
}
