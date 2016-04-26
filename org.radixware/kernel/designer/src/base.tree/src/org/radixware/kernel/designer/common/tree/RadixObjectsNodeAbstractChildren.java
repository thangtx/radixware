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

import java.util.Collections;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import java.util.List;
import javax.swing.SwingUtilities;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;

public abstract class RadixObjectsNodeAbstractChildren<T extends RadixObject> extends Children.Keys<Object> {

    private RadixObjects<T> radixObjects;
    private RadixObjects.ContainerChangesListener containerChangesListener = null;

    public RadixObjectsNodeAbstractChildren(RadixObjects<T> radixObjects) {
        super();
        this.radixObjects = radixObjects;
        setKeys(Collections.singleton(new Object()));
    }

    public RadixObjectsNodeAbstractChildren() {
        this(null);
    }

    protected abstract List<T> getOrderedList();

    protected RadixObjects<T> getRadixObjects() {
        return radixObjects;
    }

    private void doUpdate() {
        final List<T> list = getOrderedList();
        setKeys(list);
    }

    protected void update() {
        if (SwingUtilities.isEventDispatchThread()) {
            doUpdate();
        } else {
            SwingUtilities.invokeLater(new Runnable() { 
                @Override
                public void run() {
                    doUpdate();
                }
            });
        }
    }

    @Override
    protected Node[] createNodes(Object object) {
        synchronized (this) {
            if (this.containerChangesListener == null) {
                this.containerChangesListener = new RadixObjects.ContainerChangesListener() {
                    @Override
                    public void onEvent(ContainerChangedEvent e) {
                        update();
                    }
                };
                getRadixObjects().getContainerChangesSupport().addEventListener(containerChangesListener);
            }
        }
        final Node[] result;
        if (object instanceof RadixObject) {
            final RadixObject radixObject = (RadixObject) object;
            final Node node = createNodeForObject(radixObject);
            result = new Node[]{node};
        } else {
            final List<T> list = getOrderedList();
            final int size = list.size();
            result = new Node[size];
            for (int i = 0; i < size; i++) {
                final T radixObject = list.get(i);
                result[i] = createNodeForObject(radixObject);
            }
        }
        return result;
    }

    protected Node createNodeForObject(RadixObject obj) {
        return NodesManager.findOrCreateNode(obj);
    }
}
