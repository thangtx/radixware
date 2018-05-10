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
package org.radixware.kernel.designer.common.dialogs.radixdoc;

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
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Pair;

public class RadixdocTreeModulesModel implements TreeModel {

    private final RadixObject root;
    private final List<Pair<String, Id>> exludedModules;
    private final List<String> includedLayers;
    private final boolean needUds;
    private final boolean needLocalizingLayers;

    public RadixdocTreeModulesModel(RadixObject root, List<Pair<String, Id>> exludedModules, List<String> includedLayers, boolean needUds, boolean needLocalizingLayers) {
        this.root = root;
        this.exludedModules = exludedModules;
        this.includedLayers = includedLayers;
        this.needUds = needUds;
        this.needLocalizingLayers = needLocalizingLayers;
    }

    @Override
    public RadixObject getRoot() {
        return root;
    }

    @Override
    public RadixObject getChild(Object parent, int index) {
        if (parent instanceof Branch) {
            Branch br = (Branch) parent;
            return getActualLayresList(br).get(index);
        }
        if (parent instanceof Layer) {
            Layer lr = (Layer) parent;
            List<Segment> segments = new ArrayList<>();
            segments.add(lr.getAds());
            segments.add(lr.getDds());
            if (needUds) {
                segments.add(lr.getUds());
            }
            return segments.get(index);
        }
        if (parent instanceof AdsSegment || parent instanceof DdsSegment) {
            Segment sg = (Segment) parent;
            return getActualModulesList(sg).get(index);
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
            return getActualLayresList(br).size();
        } else if (node instanceof Layer) {
            return 2 + (needUds ? 1 : 0);
        } else if (node instanceof AdsSegment || node instanceof DdsSegment) {
            Segment sg = (Segment) node;
            return getActualModulesList(sg).size();
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

    private List<Layer> getActualLayresList(Branch br) {
        List<Layer> layers = new ArrayList<>();
        for (Layer layer : br.getLayers()) {
            if (includedLayers != null && !includedLayers.contains(layer.getURI())) {
                continue;
            }

            if (layer.isLocalizing() && !needLocalizingLayers) {
                continue;
            }
            
            layers.add(layer);
        }

        return layers;
    }

    private List<Module> getActualModulesList(Segment sg) {
        List<Module> modules = new ArrayList<>();
        loop:
        for (int i = 0; i < sg.getModules().size(); i++) {
            Module module = (Module) sg.getModules().get(i);
            if (exludedModules != null) {
                for (Pair<String, Id> entry : exludedModules) {
                    if (module.getLayer().getURI().equals(entry.getFirst()) && entry.getSecond() == module.getId()) {
                        continue loop;
                    }
                }
            }
            modules.add(module);
        }

        return modules;
    }
}
