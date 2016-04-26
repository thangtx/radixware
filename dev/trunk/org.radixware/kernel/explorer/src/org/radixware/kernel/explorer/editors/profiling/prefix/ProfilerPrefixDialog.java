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

package org.radixware.kernel.explorer.editors.profiling.prefix;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;


public class ProfilerPrefixDialog extends ExplorerDialog {

    private PrefixTree prefixTree;

    public ProfilerPrefixDialog(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, "Choose Profiler Prefixes");
        this.setWindowTitle(Application.translate("ProfilerDialog", "Choose Profiler Prefixes"));
        createUi();
    }

    private void createUi() {
        prefixTree = new PrefixTree(this, null);
        this.layout().addWidget(prefixTree);
    }

    public String getSelectedPrefixes() {
        return prefixTree.getSelectedPrefixes();
    }
}
