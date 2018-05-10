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

package org.radixware.kernel.common.defs.uds.files;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.defs.IFileSystemRadixObject;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.uds.IUdsDirectoryRadixObject;
import org.radixware.kernel.common.defs.uds.UdsDefinitionIcon;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;


public class UdsFile extends RadixObject implements IFileSystemRadixObject {

    public UdsFile(String name) {
        super(name);
    }

    @Override
    public UdsModule getModule() {
        return (UdsModule) super.getModule();
    }

    @Override
    public File getFile() {
        if (getContainer() == null){
            return null;
        }
        return new File(super.getFile(), getName());
    }
    
    public boolean isDirectory(){
        return false;
    }
    
    @Override
    public IUdsDirectoryRadixObject getOwnerDirectory() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof IUdsDirectoryRadixObject) {
                return (IUdsDirectoryRadixObject) owner;
            }
        }
        return null;
    }
    
    private long modificationStamp;

    public void setFileLastModifiedTime(long stamp) {
        this.modificationStamp = stamp;
    }

    public long getFileLastModifiedTime() {
        return this.modificationStamp;
    }

    public static final class Factory {
        
        public static UdsFile newInstance(final File file) {
            String name = file.getName();
            UdsFile uf;
            if (file.isDirectory()){
                uf = new UdsDirectory(name);
            } else {
                final boolean isXml = file.getName().toLowerCase().endsWith(".xml");
                if (isXml){
                    uf = new UdsXmlFile(name);
                } else {
                    uf = new UdsFile(name);
                    
                }
                uf.setFileLastModifiedTime(file.lastModified());
            }
            return uf;
        }
    }

    
    @Override
    public RadixObject getRadixObjectByFileName(String name) {
        if (name.equals(getName())){
            return this;
        }
        return null;
    }

    @Override
    public boolean delete() {
        final File file = getFile();
        if (!super.delete()) {
            return false;
        }

        if (file != null) {
            try {
                FileUtils.deleteFileExt(file);
            } catch (IOException cause) {
                throw new RadixObjectError("Unable to delete UDS file.", this, cause);
            }
        }
        return true;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    public AdsDefinition findById(Id id) {
        return null;
    }

    @Override
    public RadixIcon getIcon() {
        return UdsDefinitionIcon.FILE;
    }

    @Override
    public boolean setName(String name) {
        final String oldName = getName();
        if (Utils.equals(oldName, name)) {
            return false;
        }
        File file = getFile();
        
        if (file != null){
            try {
                FileUtils.rename(file, name, null);
            } catch (IOException cause) {
                StringBuilder sb = new StringBuilder("Unable to rename ");
                sb.append(isDirectory()? "folder" : "file");
                throw new RadixObjectError(sb.toString(), this, cause);
            }
        }
        
        return super.setName(name);
    }
}
