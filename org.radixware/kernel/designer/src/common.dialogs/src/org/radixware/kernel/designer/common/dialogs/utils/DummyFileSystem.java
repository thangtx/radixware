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

package org.radixware.kernel.designer.common.dialogs.utils;

import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.util.actions.SystemAction;

/**
 * Radix virtual file system. Allows to create instances of RadixFileObject.
 *
 */
class DummyFileSystem extends FileSystem {

    private DummyFileSystem() {
    }

    @Override
    public FileObject findResource(String name) {
        return null;
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
        return true;
    }
    private static final DummyFileSystem RADIX_FILE_SYSTEM_INSTANCE = new DummyFileSystem();

    public static DummyFileSystem getDefault() {
        return RADIX_FILE_SYSTEM_INSTANCE;
    }
}
