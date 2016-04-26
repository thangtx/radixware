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

import org.openide.nodes.Children;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;


public abstract class RadixObjectsNode extends RadixObjectNode {

    private RadixObjects.ContainerChangesListener containerChangeListener;

    protected <T extends RadixObject> RadixObjectsNode(final RadixObjects<T> radixObjects, final Children children) {
        super(radixObjects, (radixObjects.isEmpty() ? Children.LEAF : children));

        // remove expand possibility for empty collections.
        if (radixObjects.isEmpty()) {
            containerChangeListener = new RadixObjects.ContainerChangesListener() {

                @Override
                public void onEvent(ContainerChangedEvent e) {
                    RadixObjectsNode.this.setChildren(children);
                    radixObjects.getContainerChangesSupport().removeEventListener(this);
                }
            };
            radixObjects.getContainerChangesSupport().addEventListener(containerChangeListener);
        }
    }

    @Override
    protected RadixObjectEditCookie createEditCookie() {
        return null;
    }
}
