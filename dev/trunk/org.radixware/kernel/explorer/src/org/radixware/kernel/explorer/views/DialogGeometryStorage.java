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

package org.radixware.kernel.explorer.views;

import com.trolltech.qt.core.QByteArray;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.explorer.env.ExplorerSettings;


final class DialogGeometryStorage {

    private DialogGeometryStorage() {
    }

    public static QByteArray readDialogGeometry(final Model model) {
        final ExplorerSettings settings = (ExplorerSettings) model.getEnvironment().getConfigStore();
        settings.beginGroup(model.getConfigStoreGroupName());
        settings.beginGroup(SettingNames.SYSTEM);
        try {
            return settings.readQByteArray("dialogGeometry");
        } finally {
            settings.endGroup();
            settings.endGroup();
        }
    }

    public static void writeDialogGeometry(final Model model, final QByteArray geometry) {
        final ExplorerSettings settings = (ExplorerSettings) model.getEnvironment().getConfigStore();
        settings.beginGroup(model.getConfigStoreGroupName());
        settings.beginGroup(SettingNames.SYSTEM);
        try {
            settings.writeQByteArray("dialogGeometry", geometry);
        } finally {
            settings.endGroup();
            settings.endGroup();
        }
    }
}
