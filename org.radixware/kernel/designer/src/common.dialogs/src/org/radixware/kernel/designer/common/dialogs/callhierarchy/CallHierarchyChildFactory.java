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

package org.radixware.kernel.designer.common.dialogs.callhierarchy;

import java.util.List;
import java.util.Map;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesCfg;
import org.radixware.kernel.designer.common.dialogs.usages.UsagesFinder;


final class CallHierarchyChildFactory extends ChildFactory<RadixObject> {

    private final RadixObject object;

    public CallHierarchyChildFactory(RadixObject method) {
        this.object = method;
    }

    @Override
    protected boolean createKeys(List<RadixObject> toPopulate) {
        if (object != null) {
            RadixObject method = object.getDefinition();
            if (method instanceof AdsMethodDef) {
                final UsagesFinder finder = new UsagesFinder(new FindUsagesCfg((AdsMethodDef) method));

                final Map<RadixObject, List<RadixObject>> result = finder.search();
                
                toPopulate.addAll(UsagesFinder.toList(result));
            }
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(RadixObject key) {
        return new CallHierarchyNode(Children.create(new CallHierarchyChildFactory(key), true), key);
    }
}