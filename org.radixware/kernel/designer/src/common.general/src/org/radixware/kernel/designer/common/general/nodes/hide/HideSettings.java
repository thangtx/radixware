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

package org.radixware.kernel.designer.common.general.nodes.hide;

import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;


public class HideSettings {

    private HideSettings() {
    }

    public static boolean isHidable(Node node) {
        final RadixObject radixObject = getRadixObject(node);
        if (radixObject != null) {
            if (radixObject instanceof Layer) {
                return true;
            }
            if (radixObject instanceof Module) {
                return true;
            }
        }
        return false;
    }

    private static boolean isModuleOrAbove(RadixObject radixObject) {
        if (radixObject instanceof Module) {
            return true;
        }
        if (radixObject instanceof Segment) {
            return true;
        }
        if (radixObject instanceof Layer) {
            return true;
        }
        if (radixObject instanceof Branch) {
            return true;
        }
        return false;
    }

    private static RadixObject getRadixObject(Node node) {
        RadixObject radixObject = null;
        if (radixObject == null) {
            radixObject = node.getLookup().lookup(RadixObject.class);
        }
        return radixObject;
    }

    public static boolean hasHidableChildren(Node node) {
        RadixObject radixObject = getRadixObject(node);
        if (radixObject != null) {
            return isModuleOrAbove(radixObject) && !(radixObject instanceof Module);
        }
        return false;
    }
}
