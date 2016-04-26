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

package org.radixware.kernel.explorer.macros.widgets;

import com.trolltech.qt.gui.QWidget;
import java.util.Collections;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.Application;


class ExplorerTreeWidgetPath extends QWidgetPath {

    //private static final ExplorerTreeWidgetPath INSTANCE = new ExplorerTreeWidgetPath();
    public ExplorerTreeWidgetPath() {
        super(Collections.<QWidgetDescriptor>emptyList());

    }

    @Override
    public QWidget findWidget() {
        if (Application.getTreeManager().getCurrentTree() instanceof QWidget) {
            return (QWidget) Application.getTreeManager().getCurrentTree();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Explorer tree";
    }
//    protected static ExplorerTreeWidgetPath getInstance() {
//        return INSTANCE;
//    }
}
