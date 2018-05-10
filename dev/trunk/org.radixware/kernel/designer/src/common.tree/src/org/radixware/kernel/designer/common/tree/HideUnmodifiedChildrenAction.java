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

import java.lang.ref.WeakReference;
import java.util.List;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.designer.common.tree.actions.HideUnmodifiedModulesAction;

public class HideUnmodifiedChildrenAction extends HideUnmodifiedModulesAction {

    private WeakReference<RadixObjectFilterNode.HidableChildren> children;

    RadixObjectFilterNode.HidableChildren getChildren() {
        return children == null ? null : children.get();
    }

    void setChildren(RadixObjectFilterNode.HidableChildren children) {
        this.children = new WeakReference<>(children);
    }

    @Override
    protected void collectRadixObjects(RadixObject context, List<RadixObject> list) {
        final RadixObjectFilterNode.HidableChildren ch;
        if (children != null && (ch = children.get()) != null) {
            final RadixObject obj = ch.getOriginal().getLookup().lookup(RadixObject.class);
            if (obj != null) {
                super.collectRadixObjects(obj, list, new IFilter() {

                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        if (obj instanceof Branch) {
                            return radixObject instanceof Layer;
                        }
                        if (obj instanceof Segment) {
                            return radixObject instanceof Module;
                        }
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public String getName() {
        return "Hide Unmodified";
    }    
    
}
