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

package org.radixware.kernel.common.defs.ads.platform;

import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;


public class PlatformLibs {

    final BuildPath buildPath;
    private PlatformLib server = null, explorer = null, common = null, common_client = null, web = null;
    private static PlatformLib jre = null;
    PlatformLib jreLib = new PlatformLib(null, ERuntimeEnvironmentType.COMMON);

    PlatformLibs(BuildPath buildPath) {
        this.buildPath = buildPath;
    }

    public PlatformLib getKernelLib(ERuntimeEnvironmentType env) {
        synchronized (this) {
            switch (env) {
                case SERVER:
                    if (server == null) {
                        server = new PlatformLib(this, env);
                    }
                    return server;
                case COMMON_CLIENT:
                    if (common_client == null) {
                        common_client = new PlatformLib(this, env);
                    }
                    return common_client;
                case WEB:
                    if (web == null) {
                        web = new PlatformLib(this, env);
                    }
                    return web;
                case EXPLORER:
                    if (explorer == null) {
                        explorer = new PlatformLib(this, env);
                    }
                    return explorer;
                case COMMON:
                    if (common == null) {
                        common = new PlatformLib(this, env);
                    }
                    return common;
                default:
                    throw new DefinitionError("Unsupported environment");
            }
        }
    }

    protected PlatformLibs getFromPrevLayer() {
        final Layer root = buildPath.getSegment().getLayer();
        return Layer.HierarchyWalker.walk(root, new Layer.HierarchyWalker.Acceptor<PlatformLibs>() {
            @Override
            public void accept(Layer.HierarchyWalker.Controller<PlatformLibs> controller, Layer layer) {
                if (layer != root) {
                    controller.setResultAndStop(((AdsSegment) layer.getAds()).getBuildPath().getPlatformLibs());
                }
            }
        });
    }
}
