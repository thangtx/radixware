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

package org.radixware.kernel.designer.environment.tasks;

import java.awt.Image;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;


class LayerScanningScope extends SelectionScanningScope {

    public LayerScanningScope() {
        super("Current Layer", "Show tasks for selected object's layer", RadixObjectIcon.LAYER.getImage());
    }

    @Override
    protected RadixObject filter(RadixObject obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Layer) {
            return (Layer) obj;
        }
        if (obj instanceof Segment) {
            return ((Segment) obj).getLayer();
        }
        Module module = obj.getModule();
        if (module != null) {
            Segment segment = module.getSegment();
            return segment == null ? null : segment.getLayer();
        }
        return null;
    }

    public static final SelectionScanningScope create() {
        return new LayerScanningScope();
    }
}
