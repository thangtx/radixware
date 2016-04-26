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
package org.radixware.kernel.designer.common.tree.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import org.netbeans.modules.subversion.Subversion;

public class RefreshAction implements PropertyChangeListener {

    private List<String> files;

    RefreshAction(List<String> files) {
        this.files = files;
    }

    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        Subversion s = org.netbeans.modules.subversion.Subversion.getInstance();
        for (String sFile : files) {
            File f = new File(sFile);
            s.getStatusCache().refreshRecursively(f);
        }
    }

}
