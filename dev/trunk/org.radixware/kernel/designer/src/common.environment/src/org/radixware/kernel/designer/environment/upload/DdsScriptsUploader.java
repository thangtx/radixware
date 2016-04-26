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

package org.radixware.kernel.designer.environment.upload;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.dds.DdsScripts;


class DdsScriptsUploader extends RadixObjectUploader<DdsScripts> {

    public DdsScriptsUploader(DdsScripts scripts) {
        super(scripts);
    }

    public DdsScripts getScripts() {
        return getRadixObject();
    }

    @Override
    public void close() {
        try {
            reload();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public long getRememberedFileTime() {
        final DdsScripts scripts = getScripts();
        return scripts.getFileLastModifiedTime();
    }

    @Override
    public void reload() throws IOException {
        final DdsScripts scripts = getScripts();
        scripts.reload();
    }

    @Override
    public void updateChildren() {
        final DdsScripts scripts = getScripts();

        final DdsScriptsDirUploader preScriptsUpdater = new DdsScriptsDirUploader(scripts.getDbScripts().getPreScripts());
        preScriptsUpdater.updateChildren();

        final DdsScriptsDirUploader postScriptsUpdater = new DdsScriptsDirUploader(scripts.getDbScripts().getPostScripts());
        postScriptsUpdater.updateChildren();

        final DdsScriptsDirUploader upgradeScriptsUpdater = new DdsScriptsDirUploader(scripts.getDbScripts().getUpgradeScripts());
        upgradeScriptsUpdater.updateChildren();
    }

    // ===========================================
    @Override
    public RadixObject uploadChild(File file) throws IOException {
        throw new IllegalStateException();
    }
}
