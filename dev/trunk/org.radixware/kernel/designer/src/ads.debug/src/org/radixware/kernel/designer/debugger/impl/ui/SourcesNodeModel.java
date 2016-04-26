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

package org.radixware.kernel.designer.debugger.impl.ui;

import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.radixware.kernel.designer.debugger.impl.KernelSourcePath;


public class SourcesNodeModel extends AbstractNodeModel {

    public static final String SOURCE_ROOT =
            "org/netbeans/modules/debugger/resources/sourcesView/sources_16.png";

    @Override
    public String getDisplayName(Object node) throws UnknownTypeException {
        if (node instanceof KernelSourcePath) {
            return ((KernelSourcePath) node).getDisplayName();
        } else {
            return node.toString();
        }
    }

    @Override
    public String getIconBase(Object node) throws UnknownTypeException {
        return SOURCE_ROOT;
    }

    @Override
    public String getShortDescription(Object node) throws UnknownTypeException {
        return "";
    }
}
