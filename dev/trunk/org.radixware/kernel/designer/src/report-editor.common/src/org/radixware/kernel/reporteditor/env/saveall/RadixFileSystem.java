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

import java.util.Map;
import java.util.WeakHashMap;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.userreport.repository.UserReport;

/**
 * Radix virtual file system. Allows to create instances of RadixFileObject.
 *
 */
class RadixFileSystem extends FileSystem {

    private RadixFileSystem() {
    }

    @Override
    public FileObject findResource(String name) {
        return null;
    }
    private final Map<RadixObject, RadixFileObject> radixObject2FileObject = new WeakHashMap<>();
    private final Map<UserReport, UserReportFileObject> userReport2FileObject = new WeakHashMap<>();

    public synchronized FileObject findExistedFileObject(RadixObject radixObject) {
        return radixObject2FileObject.get(radixObject);
    }

    public synchronized FileObject findExistedFileObject(UserReport radixObject) {
        return userReport2FileObject.get(radixObject);
    }

    public synchronized FileObject findOrCreateFileObject(Object object) {
        if (object instanceof RadixObject) {
            RadixObject radixObject = (RadixObject) object;
            RadixFileObject fileObject = radixObject2FileObject.get(radixObject);
            if (fileObject == null) {
                fileObject = new RadixFileObject(radixObject);
                radixObject2FileObject.put(radixObject, fileObject);
            }
            return fileObject;
        } else if (object instanceof UserReport) {
            UserReport radixObject = (UserReport) object;
            UserReportFileObject fileObject = userReport2FileObject.get(radixObject);
            if (fileObject == null) {
                fileObject = new UserReportFileObject(radixObject);
                userReport2FileObject.put(radixObject, fileObject);
            }
            return fileObject;
        } else {
            return null;
        }


    }

    @Override
    public SystemAction[] getActions() {
        return new SystemAction[]{};
    }

    @Override
    public String getDisplayName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public FileObject getRoot() {
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
    private static final RadixFileSystem RADIX_FILE_SYSTEM_INSTANCE = new RadixFileSystem();

    public static RadixFileSystem getDefault() {
        return RADIX_FILE_SYSTEM_INSTANCE;
    }
}
