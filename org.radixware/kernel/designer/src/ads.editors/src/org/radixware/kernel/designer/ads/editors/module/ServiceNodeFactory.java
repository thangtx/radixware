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

package org.radixware.kernel.designer.ads.editors.module;

import java.util.Collections;
import java.util.List;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.module.MetaInfServicesCatalog.Service;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


final class ServiceNodeFactory extends DefinitionNodeFactory {

    public ServiceNodeFactory(AdsModule module, ERuntimeEnvironmentType environment) {
        super(module, environment);
    }

    @Override
    protected boolean createKeys(List<AdsPath> toPopulate) {

        List<Service> services = Collections.<Service>emptyList();

        if (getModule().getServicesCatalog() != null) {
            services = getModule().getServicesCatalog().getServices(getEnvironment());
        }

        if (!services.isEmpty()) {
            for (final Service service : services) {
                if (!service.getImplementations(getEnvironment()).isEmpty()) {
                    toPopulate.add(service.getInterfaceIdPath());
                }
            }
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(AdsPath key) {
        return new ServiceNode(key, getModule(), getEnvironment());
    }
}
