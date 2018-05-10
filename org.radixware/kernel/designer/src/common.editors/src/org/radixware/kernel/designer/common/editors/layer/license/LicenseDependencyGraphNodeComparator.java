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
package org.radixware.kernel.designer.common.editors.layer.license;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.dependency.OrientedGraph;

public class LicenseDependencyGraphNodeComparator implements Comparator<OrientedGraph.Node> {

    private static final Map<ERepositorySegmentType, Integer> segmentComprassionHelp = new HashMap<>();

    static {
        segmentComprassionHelp.put(ERepositorySegmentType.DDS, 0);
        segmentComprassionHelp.put(ERepositorySegmentType.ADS, 1);
        segmentComprassionHelp.put(ERepositorySegmentType.UDS, 2);
        segmentComprassionHelp.put(ERepositorySegmentType.KERNEL, 3);
    }

    @Override
    public int compare(OrientedGraph.Node o1, OrientedGraph.Node o2) {
        Object obj1 = o1.getObject();
        Object obj2 = o2.getObject();

        if (obj1 instanceof Module && obj2 instanceof Layer.License) {
            return -1;
        }
        
        if (obj2 instanceof Module && obj1 instanceof Layer.License) {
            return 1;
        }

        if (obj1 instanceof Module && obj2 instanceof Module) {
            ERepositorySegmentType seg1 = ((Module) obj1).getSegmentType();
            ERepositorySegmentType seg2 = ((Module) obj2).getSegmentType();
            if (seg1 != seg2) {
                return segmentComprassionHelp.get(seg1).compareTo(segmentComprassionHelp.get(seg2));
            }
        }

        return o1.getName().compareTo(o2.getName());
    }

}
