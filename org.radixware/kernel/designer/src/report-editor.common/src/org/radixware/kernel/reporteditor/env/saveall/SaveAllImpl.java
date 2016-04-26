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

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.IRemoveListener;
import org.radixware.kernel.common.defs.RadixObject.Registry.IModifiedSetChangeListener;
import org.radixware.kernel.common.defs.RadixObject.Registry.ModifiedSetChangedEvent;
import org.radixware.kernel.common.defs.RadixObject.RemovedEvent;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;

/**
 * Support of 'Save All' for Radix objects. Netbeans required
 * dataobject.setModified(true) to enable Save All button. It is realized
 * RadixFileSystem, that provided RadixFileObject and RadixDataObject for any
 * RadixObject. SaveAllImpl listen changes in RadixObject.getRegistry(), that
 * fire events when RadixObject changed edit state to MODIFIED or NONE.
 *
 */
final public class SaveAllImpl {

    private final static SaveAllImpl INSTANCE = new SaveAllImpl();

    private SaveAllImpl() {
        RadixObject.getRegistry().getChangeSupport().addEventListener(listener);
        UserExtensionManager.getInstance().getModifiedSupport().addListener(reportListener);
    }

    public static SaveAllImpl getDefault() {
        return INSTANCE;
    }
    // listerer for changes in collection of modified RadixObject.
    private final IModifiedSetChangeListener listener = new IModifiedSetChangeListener() {

        @Override
        public void onEvent(final ModifiedSetChangedEvent event) {
            // Status not changed during loading (always NEW, and NONE after loading).

            final RadixObject radixObject = event.getRadixObject();
            final boolean modified = (radixObject.getEditState() != RadixObject.EEditState.NONE);

            if (modified && !radixObject.isInBranch()) {
                final RadixObjectError error = new RadixObjectError("Attemp to modify removed object.", radixObject);
                DialogUtils.messageError(error);
            } else {
                updateModified(radixObject, modified);
            }
        }
    };
    private final UserExtensionManagerCommon.IReportModifiedListener reportListener = new UserExtensionManagerCommon.IReportModifiedListener() {

        @Override
        public void reportModificationStateChanged(final UserReport src) {

            final boolean modified = src.isModified();


            updateModified(src, modified);

        }
    };
    private final IRemoveListener removeListener = new RadixObject.IRemoveListener() {

        @Override
        public void onEvent(final RemovedEvent event) {
            final RadixObject radixObject = event.radixObject;
            updateModified(radixObject, false);
        }
    };

    private synchronized void updateModified(final RadixObject radixObject, final boolean modified) {
        if (modified) {
            radixObject.getRemoveSupport().addEventListener(removeListener);
        } else {
            final FileObject fileObject = RadixFileSystem.getDefault().findExistedFileObject(radixObject);
            if (fileObject == null) {
                // data object not created
                return;
            }
            radixObject.getRemoveSupport().removeEventListener(removeListener);
        }

        final DataObject dataObject = RadixDataObject.find(radixObject);
        dataObject.setModified(modified);
    }

    private synchronized void updateModified(final UserReport report, final boolean modified) {
        if (!modified) {
            final FileObject fileObject = RadixFileSystem.getDefault().findExistedFileObject(report);
            if (fileObject == null) {
                // data object not created
                return;
            }
        }
        final DataObject dataObject = UserReportDataObject.find(report);
        try {
            dataObject.setModified(modified);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
