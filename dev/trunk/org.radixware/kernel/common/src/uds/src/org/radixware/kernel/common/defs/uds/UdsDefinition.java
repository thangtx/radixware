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

package org.radixware.kernel.common.defs.uds;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IFileBasedDef;
import org.radixware.kernel.common.defs.uds.module.Loader;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.schemas.commondef.DescribedDefinition;
import org.radixware.schemas.udsdef.UdsDefinitionDocument;


public abstract class UdsDefinition extends AdsDefinition implements IFileBasedDef {

    public UdsDefinition(org.radixware.schemas.commondef.Definition xDefinition) {
        super(xDefinition);
    }

    public UdsDefinition(org.radixware.schemas.commondef.DescribedDefinition xDefinition) {
        super(xDefinition);

        this.setName(xDefinition.getName());
        if (xDefinition.isSetDescription()) {
            this.setDescription(xDefinition.getDescription());
        }
    }

    public UdsDefinition(org.radixware.schemas.commondef.NamedDefinition xDefinition) {
        super(xDefinition);
        this.setName(xDefinition.getName());
    }

//    public UdsDefinition(Id id, String name, String description) {
//        super(id, name, description);
//    };
    public UdsDefinition(Id id, String name) {
        super(id, name);
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.USER_FUNC;
    }

//
//    @Override
//    public File getFile() {
//        final Definition ownerDefinition = getOwnerDefinition();
//        if (ownerDefinition instanceof UdsModule) {
//            return ((UdsModule) ownerDefinition).getDefinitions().getSourceFile(this);
//        } else {
//            return super.getFile();
//        }
//    }
//    @Override
//    public UdsModule getModule() {
//        return (UdsModule) super.getModule();
//    }
//
//    @Override
//    public void save() throws IOException {
//        if (getOwnerDefinition() instanceof UdsModule) {
//            getModule().getDefinitions().save(this);
//        } else {
//            super.save();
//        }
//    }
    public void appendTo(UdsDefinitionDocument.UdsDefinition xDef) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    public void appendTo(DescribedDefinition xDef) {
        synchronized (this) {
            super.appendTo(xDef, ESaveMode.NORMAL);
        }
    }
    private long modificationStamp;

    public void setFileLastModifiedTime(long stamp) {
        this.modificationStamp = stamp;
    }

    @Override
    public long getFileLastModifiedTime() {
        return this.modificationStamp;
    }

    @Override
    public boolean setName(String newName) {
        if (getOwnerDefinition() == getModule()) {
            String oldName = getName();
            File defFile = getFile();
            if (super.setName(newName)) {
                if (defFile != null) {
                    File newFile = getFile();
                    if (defFile.renameTo(newFile)) {
                        try {
                            save();
                        } catch (IOException ex) {
                            throw new RadixError("Unable to rename definition", ex);
                        }
                        return true;
                    } else {
                        super.setName(oldName);
                        throw new RadixError("Unable to rename definition. File rename failed");
                    }
                } else {
                    return true;

                }
            } else {
                return false;
            }
        } else {
            return super.setName(newName);
        }
    }
//
//    @Override
//    public boolean delete() {
//        final Definition ownerDefinition = getOwnerDefinition();
//        final File file;
//
//        if (ownerDefinition instanceof UdsModule) {
//            file = getFile();
//        } else {
//            file = null;
//        }
//
//        if (!super.delete()) {
//            return false;
//        }
//
//        if (file != null) {
//            try {
//                FileUtils.deleteFileExt(file);
//            } catch (IOException cause) {
//                throw new DefinitionError("Unable to delete definition file.", this, cause);
//            }
//        }
//
//
//        return true;
//    }

    public String getFileName() {
        return FileUtils.string2UniversalFileName(getName(), '-') + ".xml";
    }
    private static final int BUFFER_SIZE = 4096;

    private static byte[] getFileBytes(File file) throws IOException {
        FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ);

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            int count = 0;

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while ((count = channel.read(buffer)) >= 0) {
                out.write(buffer.array(), 0, count);
                buffer.clear();
            }
            return out.toByteArray();
        } finally {
            buffer.clear();
            channel.close();
        }
    }

    public static Id fileName2DefinitionId(File file) {
        InputStream stream = null;

        try {
            byte[] bytes = getFileBytes(file);
            stream = new ByteArrayInputStream(bytes);
            try {
                AdsDefinition def = Loader.loadFromStream(stream, file, false);
                if (def != null) {
                    return def.getId();
                } else {
                    return AdsDefinition.fileName2DefinitionId(file);
                }
            } catch (RadixError e) {
                try {
                    stream = new ByteArrayInputStream(bytes);
                    AdsDefinition def = org.radixware.kernel.common.defs.ads.module.Loader.loadFromStream(stream, file, true, false);
                    if (def != null) {
                        return def.getId();
                    } else {
                        return AdsDefinition.fileName2DefinitionId(file);
                    }
                } catch (IOException ex) {
                    return null;
                }
            }
        } catch (IOException ex) {
            return null;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    //ignore
                }
            }
        }
    }
}
