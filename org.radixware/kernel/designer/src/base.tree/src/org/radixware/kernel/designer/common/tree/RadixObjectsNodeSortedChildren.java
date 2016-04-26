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

package org.radixware.kernel.designer.common.tree;

import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RenameEvent;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.utils.RadixObjectsUtils;

public class RadixObjectsNodeSortedChildren<T extends RadixObject> extends RadixObjectsNodeAbstractChildren<T> {

    public RadixObjectsNodeSortedChildren(RadixObjects<T> radixObjects) {
        super(radixObjects);
    }

    public RadixObjectsNodeSortedChildren() {
    }
    private final RadixObject.RenameListener renameListener = new RadixObject.RenameListener() {

        @Override
        public void onEvent(RenameEvent e) {
            update();
        }
    };

    @Override
    protected List<T> getOrderedList() {
        final List<T> list = getRadixObjects().list();
        RadixObjectsUtils.sortByName(list);

        for (RadixObject ro : list) {
            ro.addRenameListener(renameListener);
        }

        return list;
    }
}
