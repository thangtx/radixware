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
import javax.swing.event.ChangeListener;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;

public class RadixObjectsNodeChildren<T extends RadixObject> extends RadixObjectsNodeAbstractChildren<T> implements Index {

    public RadixObjectsNodeChildren(RadixObjects<T> radixObjects) {
        super(radixObjects);
    }

    // final - because not sorted and indexed
    @Override
    protected final List<T> getOrderedList() {
        return getRadixObjects().list();
    }

    private class RadixObjectsNodeIndex extends Index.Support {

        @Override
        public Node[] getNodes() {
            return RadixObjectsNodeChildren.this.getNodes();
        }

        @Override
        public int getNodesCount() {
            return RadixObjectsNodeChildren.this.getNodesCount();
        }

        @Override
        public void reorder(int[] perm) {
            RadixObjectsNodeChildren.this.getRadixObjects().reorder(perm);
        }
    }
    private final Index index = new RadixObjectsNodeIndex();

    @Override
    public void reorder(int[] perm) {
        index.reorder(perm);
    }

    @Override
    public void reorder() {
        index.reorder();
    }

    @Override
    public void removeChangeListener(ChangeListener chl) {
        index.removeChangeListener(chl);
    }

    @Override
    public void moveUp(int x) {
        index.moveUp(x);
    }

    @Override
    public void moveDown(int x) {
        index.moveDown(x);
    }

    @Override
    public void move(int x, int y) {
        index.move(x, y);
    }

    @Override
    public int indexOf(Node node) {
        return index.indexOf(node);
    }

    @Override
    public void exchange(int x, int y) {
        index.exchange(x, y);
    }

    @Override
    public void addChangeListener(ChangeListener chl) {
        index.addChangeListener(chl);
    }
}
