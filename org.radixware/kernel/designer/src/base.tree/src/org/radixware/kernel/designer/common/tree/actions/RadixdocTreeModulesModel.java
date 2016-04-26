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

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.resources.icons.RadixIcon;

public class RadixdocTreeModulesModel implements TreeModel {

    private final RadixObject root;

    public RadixdocTreeModulesModel(RadixObject root) {
        this.root = root;
    }

    @Override
    public RadixObject getRoot() {
        return root;
    }

    @Override
    public RadixObject getChild(Object parent, int index) {
        if (parent instanceof Branch) {
            List<Layer> layers = new ArrayList<>();
            Branch br = (Branch) parent;
            for (Layer layer : br.getLayers()) {
                layers.add(layer);
            }
            return layers.get(index);
        }
        if (parent instanceof Layer) {
            Layer lr = (Layer) parent;
            List<Segment> segments = new ArrayList<>();
            segments.add(lr.getAds());
            segments.add(lr.getDds());
            return segments.get(index);
        }
        if (parent instanceof AdsSegment || parent instanceof DdsSegment) {
            Segment sg = (Segment) parent;
            return sg.getModules().get(index);
        }
        return null;
    }

    public String getName(RadixObject node) {
        return node.getName();
    }

    public RadixIcon getIcon(RadixObject node) {
        return node.getIcon();
    }

    @Override
    public int getChildCount(Object node) {
        if (node instanceof Branch) {
            Branch br = (Branch) node;
            return br.getLayers().size();
        } else if (node instanceof Layer) {
            return 2;
        } else if (node instanceof AdsSegment || node instanceof DdsSegment) {
            Segment sg = (Segment) node;
            return sg.getModules().size();
        }
        return 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof Module;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        String[] children = ((RadixObject) parent).getFile().list();
        if (children == null) {
            return -1;
        }
        String childname = ((RadixObject) child).getName();
        for (int i = 0; i < children.length; i++) {
            if (childname.equals(children[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
    }
}
