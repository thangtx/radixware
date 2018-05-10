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

package org.radixware.kernel.designer.environment.saveall;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataLoader;
import org.openide.loaders.DataLoaderPool;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Cookie;
import org.openide.util.HelpCtx;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;

/**
 * Radix DataObject, allows to realize 'Save All', save on exit.
 */
class RadixDataObject extends DataObject {

    public RadixDataObject(FileObject fileObject, DataLoader loader) throws DataObjectExistsException {
        super(fileObject, loader);
    }

    public RadixObject getRadixObject() {
        final RadixFileObject radixFileObject = (RadixFileObject) getPrimaryFile();
        return radixFileObject.getRadixObject();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public <T extends Cookie> T getCookie(Class<T> type) {
        Object obj = getLookup().lookup(type);
        if (Node.Cookie.class.isInstance(obj)) {
            return type.cast(obj);
        } else {
            return null;
        }
    }

    @Override
    protected DataObject handleCopy(DataFolder f) throws IOException {
        throw new RadixObjectError("Attempt to copy RadixDataObject.", getRadixObject());
    }

    @Override
    protected DataObject handleCreateFromTemplate(DataFolder df, String name) throws IOException {
        throw new RadixObjectError("Attempt to create from template RadixDataObject.", getRadixObject());
    }

    @Override
    protected void handleDelete() throws IOException {
        throw new RadixObjectError("Attempt to delete RadixDataObject.", getRadixObject());
    }

    @Override
    protected FileObject handleMove(DataFolder df) throws IOException {
        throw new RadixObjectError("Attempt to move RadixDataObject.", getRadixObject());
    }

    @Override
    protected FileObject handleRename(String name) throws IOException {
        throw new RadixObjectError("Attempt to rename RadixDataObject.", getRadixObject());
    }

    @Override
    public boolean isCopyAllowed() {
        return false;
    }

    @Override
    public boolean isDeleteAllowed() {
        return false;
    }

    @Override
    public boolean isMoveAllowed() {
        return false;
    }

    @Override
    public boolean isRenameAllowed() {
        return false;
    }

    @Override
    protected Node createNodeDelegate() {
        final RadixObject radixObject = getRadixObject();
        return NodesManager.findOrCreateNode(radixObject);
    }

    public static DataObject find(RadixObject radixObject) {
        final FileObject fileObject = RadixFileSystem.getDefault().findOrCreateFileObject(radixObject);
        try {
            DataLoaderPool.setPreferredLoader(fileObject, RadixDataLoader.getDefault());
        } catch (IOException cause) {
            throw new RadixObjectError("Unable to create data object.", radixObject, cause);
        }

        try {
            final DataObject dataObject = DataObject.find(fileObject);
            return dataObject;
        } catch (DataObjectNotFoundException cause) {
            throw new RadixObjectError("Unable to create data object.", radixObject, cause);
        }
    }
    
    public static DataObject[] findAll(RadixObject ro) {
        ArrayList<DataObject> list = new ArrayList<>();
        DataObject dataObject = find(ro);
        if (dataObject != null) {
            list.add(dataObject);            
        }
        File file = AdsUtils.calcHumanReadableFile(ro);
        if (file != null && file.exists()) {
            FileObject fo = FileUtil.toFileObject(file);
            if (fo != null) {
                try {
                    DataObject dobj = DataObject.find(fo);
                    if (dobj != null) {
                        list.add(dobj);
                    }
                } catch (DataObjectNotFoundException ex) {
                    Logger.getLogger(RadixDataObject.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
        return list.toArray(new DataObject[list.size()]);
    }
}
