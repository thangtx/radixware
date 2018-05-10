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

package org.radixware.kernel.explorer.dialogs.settings;

import java.util.ArrayList;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.IExplorerSettings;

final class GroupColorSettingsWidget extends SettingsWidget {

    private final ArrayList<ColorSettingsWidget> colorsArrayList;
    private QGridLayout parentGridLayout;

    public GroupColorSettingsWidget(final IClientEnvironment environment, final QWidget parent, final String gr, final String sub, final QGridLayout parentGridLayout) {
        super(environment, parent, gr, sub, null);
        this.parentGridLayout = parentGridLayout;

        colorsArrayList = new ArrayList<>();
    }

    public ColorSettingsWidget addColorWidget(final int row, final String name, final String description) {
        ColorSettingsWidget w = new ColorSettingsWidget(getEnvironment(), this, group, subGroup, name, description);
        colorsArrayList.add(w);
        w.addToParent(row, parentGridLayout);
        return w;
    }

    @Override
    public void restoreDefaults() {
        for (SettingsWidget w : colorsArrayList) {
            w.restoreDefaults();
        }
    }

    @Override
    public void readSettings(IExplorerSettings src) {
        for (ColorSettingsWidget w : colorsArrayList) {
            w.readSettings(src);
        }
    }

    @Override
    public void writeSettings(IExplorerSettings dst) {
        for (ColorSettingsWidget w : colorsArrayList) {
            w.writeSettings(dst);
        }
    }
}