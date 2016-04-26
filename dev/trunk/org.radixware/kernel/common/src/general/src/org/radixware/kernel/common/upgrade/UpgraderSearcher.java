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

package org.radixware.kernel.common.upgrade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.services.RadixServiceRegistry;


class UpgraderSearcher {

    private Map<IRadixObjectUpgraderFactory, List<IRadixObjectUpgrader>> upgraderFactory2upgraders = null;

    public IRadixObjectUpgraderFactory findUpgraderFactory(org.w3c.dom.Element root) throws XmlException, IOException {
        if (upgraderFactory2upgraders == null) {
            upgraderFactory2upgraders = new HashMap<IRadixObjectUpgraderFactory, List<IRadixObjectUpgrader>>();

            final Iterator<IRadixObjectUpgraderFactory> iterator = RadixServiceRegistry.getDefault().iterator(IRadixObjectUpgraderFactory.class);
            while (iterator.hasNext()) {
                final IRadixObjectUpgraderFactory upgraderFactory = iterator.next();
                upgraderFactory2upgraders.put(upgraderFactory, new ArrayList());
            }
        }

        IRadixObjectUpgraderFactory result = null;
        for (IRadixObjectUpgraderFactory candidate : upgraderFactory2upgraders.keySet()) {
            if (candidate.isSupported(root)) {
                if (result == null) {
                    result = candidate;
                } else {
                    throw new IllegalStateException("To many upgrader factories for '" + root.getNodeName() + "': " + result.getClass().getName() + " and " + candidate.getClass().getName());
                }
            }
        }
        return result;
    }

    public IRadixObjectUpgrader getUpgrader(IRadixObjectUpgraderFactory upgraderFactory, int toVersion) {
        final List<IRadixObjectUpgrader> list = upgraderFactory2upgraders.get(upgraderFactory);
        while (list.size() < toVersion) {
            list.add(null);
        }
        IRadixObjectUpgrader result = list.get(toVersion-1);
        if (result == null) {
            result = upgraderFactory.createUpgrader(toVersion);
            list.set(toVersion-1, result);
        }
        return result;
    }

    public List<IRadixObjectUpgrader> getAndRemoveProcessedUpgraders() {
        final List<IRadixObjectUpgrader> result = new ArrayList<IRadixObjectUpgrader>();
        for (List<IRadixObjectUpgrader> list : upgraderFactory2upgraders.values()) {
            for (IRadixObjectUpgrader upgrader : list) {
                if (upgrader != null) {
                    result.add(upgrader);
                }
            }
            list.clear();
        }
        return result;
    }
}
