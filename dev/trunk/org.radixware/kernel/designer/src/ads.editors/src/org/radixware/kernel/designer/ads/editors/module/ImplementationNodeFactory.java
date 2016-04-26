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

import java.util.List;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.module.MetaInfServicesCatalog.Service;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


final class ImplementationNodeFactory extends DefinitionNodeFactory{

    private final AdsPath interfaceIdPath;

    public ImplementationNodeFactory(AdsModule module, ERuntimeEnvironmentType environment, AdsPath interfaceName) {
        super(module, environment);

        this.interfaceIdPath = interfaceName;
    }

    @Override
    protected boolean createKeys(List<AdsPath> toPopulate) {

        final Service service = getModule().getServicesCatalog().findService(interfaceIdPath);

        if (service != null) {
            for (final AdsPath impl : service.getImplementations(getEnvironment())) {
                toPopulate.add(impl);
            }
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(AdsPath key) {
        return new ImplementationNode(key, getModule());
    }
}
