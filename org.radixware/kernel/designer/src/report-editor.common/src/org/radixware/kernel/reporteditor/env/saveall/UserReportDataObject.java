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

package org.radixware.kernel.reporteditor.env.saveall;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataLoader;
import org.openide.loaders.DataLoaderPool;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Cookie;
import org.openide.util.HelpCtx;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;

/**
 * Radix DataObject, allows to realize 'Save All', save on exit.
 *
 */
class UserReportDataObject extends DataObject {

    public UserReportDataObject( final FileObject fileObject, final DataLoader loader) throws DataObjectExistsException {
        super(fileObject, loader);
    }

    public UserReport getUserReport() {
        final UserReportFileObject radixFileObject = (UserReportFileObject) getPrimaryFile();
        return radixFileObject.getUserReport();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public <T extends Cookie> T getCookie( final Class<T> type) {
        final Object obj = getLookup().lookup(type);
        if (Node.Cookie.class.isInstance(obj)) {
            return type.cast(obj);
        } else {
            return null;
        }
    }

    @Override
    protected DataObject handleCopy(final DataFolder f) throws IOException {
        throw new RadixObjectError("Attempt to copy RadixDataObject.", getUserReport());
    }

    @Override
    protected DataObject handleCreateFromTemplate(final DataFolder df, final String name) throws IOException {
        throw new RadixObjectError("Attempt to create from template RadixDataObject.", getUserReport());
    }

    @Override
    protected void handleDelete() throws IOException {
        throw new RadixObjectError("Attempt to delete RadixDataObject.", getUserReport());
    }

    @Override
    protected FileObject handleMove(final DataFolder df) throws IOException {
        throw new RadixObjectError("Attempt to move RadixDataObject.", getUserReport());
    }

    @Override
    protected FileObject handleRename(final String name) throws IOException {
        throw new RadixObjectError("Attempt to rename RadixDataObject.", getUserReport());
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
        final UserReport report = getUserReport();
        return UserExtensionManager.getInstance().findOrCreateNode(report);
    }

    public static DataObject find(final UserReport userReport) {
        final FileObject fileObject = RadixFileSystem.getDefault().findOrCreateFileObject(userReport);
        try {
            DataLoaderPool.setPreferredLoader(fileObject, UserReportDataLoader.getDefault());
        } catch (IOException cause) {
            throw new RadixObjectError("Unable to create data object.", userReport, cause);
        }

        try {
            final DataObject dataObject = DataObject.find(fileObject);
            return dataObject;
        } catch (DataObjectNotFoundException cause) {
            throw new RadixObjectError("Unable to create data object.", userReport, cause);
        }
    }
}
