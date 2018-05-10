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
import java.util.Iterator;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IDefinitionContainer;
import org.radixware.kernel.common.defs.uds.IUdsDirectoryRadixObject;
import org.radixware.kernel.common.defs.uds.UdsDefinitionIcon;
import org.radixware.kernel.common.defs.uds.UdsDefinitionsUtils;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;

public class UdsDirectory extends UdsFile implements IUdsDirectoryRadixObject, IDefinitionContainer {
    private final UdsFiles files;
    
    public UdsDirectory(String name) {
        super(name);
        files = new UdsFiles();
    }
    
    public final File[] collectFiles() {
        File file = getDirectory();
        if (file != null){
            return file.listFiles();
        }
        return null;
    }
    

    public UdsFiles getFiles() {
        return files;
    }
    
    @Override
    public File getDirectory() {
        return getFile();
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public boolean remove(RadixObject radixObject) {
        return getFiles().remove(radixObject);
    }

    @Override
    public void add(RadixObject radixObject) {
        getFiles().add(radixObject);
    }

    @Override
    public Iterator<RadixObject> iterator() {
        return getFiles().iterator();
    }
    
    public class UdsFiles extends RadixObjects<RadixObject> implements IDefinitionContainer{
        private boolean loading = false;
        
        public UdsFiles() {
            super(UdsDirectory.this);
        }
        
        public AdsDefinition findById(Id id){
            if (id == null || isEmpty()) {
                return null;
            }

            for (RadixObject radixObject : this) {
                if (radixObject instanceof AdsDefinition) {
                    AdsDefinition def = (AdsDefinition) radixObject;
                    if (id.compareTo(def.getId()) == 0) {
                        return def;
                    }
                } else if (radixObject instanceof UdsFile) {
                    AdsDefinition result = ((UdsFile) radixObject).findById(id);
                    if (result != null) {
                        return result;
                    }
                } else if (radixObject instanceof UdsFileRadixObjects) {
                    AdsDefinition result = ((UdsFileRadixObjects) radixObject).findById(id);
                    if (result != null) {
                        return result;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onAdd(RadixObject object) {
            if (!loading) {
                if (object.getFile() != null) {
                    try {
                        object.save();
                    } catch (IOException cause) {
                        throw new RadixObjectError("Unable to save UDS file.", object, cause);
                    }
                    getModule().getDependences().addRequired();
                }
            }
        }

        public void setLoading(boolean loading) {
            this.loading = loading;
        }
    }

    @Override
    public RadixObject getRadixObjectByFileName(String name) {
        RadixObject radixObject = UdsDefinitionsUtils.getRadixObjectByFileName(name, getFiles().list());
        if (radixObject != null){
            return radixObject;
        }
        return super.getRadixObjectByFileName(name);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        getFiles().visit(visitor, provider);
    }
    
    @Override
    public AdsDefinition findById(Id id) {
        return getFiles().findById(id);
    }
    
    public boolean containsFile(File file) {
        if (file != null && file.exists()) {
            for (RadixObject rd : this) {
                if (file.equals(rd.getFile())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public RadixIcon getIcon() {
        return UdsDefinitionIcon.FOLDER;
    }

    @Override
    public void save() throws IOException {
        File newFile = getFile();
        if (newFile != null && !newFile.exists()){
            newFile.mkdirs();
        }
        setEditState(EEditState.NONE);
    }
    
   
}
