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

package org.radixware.kernel.designer.environment.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.openide.explorer.ExtendedDelete;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.tree.RadixObjectNodeDeleteAction;


public class RadixExtendedDelete implements ExtendedDelete {

    private final Lock lock = new ReentrantLock();

    public RadixExtendedDelete() {
    }

    private boolean deleteOnLock(Node[] nodes) {
        final List<RadixObject> selectedObjects = new ArrayList<RadixObject>();
        for (int i = 0; i < nodes.length; i++) {
            final Node node = nodes[i];
            final RadixObjectNodeDeleteAction.Cookie cookie = node.getCookie(RadixObjectNodeDeleteAction.Cookie.class);
            if (cookie != null) {
                selectedObjects.add(cookie.getRadixObject());
            } else {
                return false;
            }
        }
        DialogUtils.deleteObjects(selectedObjects);
        return true;
    }

    @Override
    public boolean delete(Node[] nodes) throws IOException {
        if (lock.tryLock()) { // it is possible to press Del several time, bug in Netbeans DeleteAction.
            try {
                return deleteOnLock(nodes);
            } finally {
                lock.unlock();
            }
        } else {
            return true;
        }
    }
}
