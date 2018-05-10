/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.instance;

import java.util.Date;

/**
 *
 * @author dsafonov
 */
public class DdsVersionInfoItem {

    private final String layerUri;
    private final String version;
    private final Date upgradeDate;
    private final String upgradeToVersion;
    private final Date upgradeStartTime;
    private final String prevCompatibleVersion;

    public DdsVersionInfoItem(String layerUri, String version, Date upgradeDate, String upgradeToVersion, Date upgradeStartTime, String prevCompatibleVersion) {
        this.layerUri = layerUri;
        this.version = version;
        this.upgradeDate = upgradeDate;
        this.upgradeToVersion = upgradeToVersion;
        this.upgradeStartTime = upgradeStartTime;
        this.prevCompatibleVersion = prevCompatibleVersion;
    }

    public String getLayerUri() {
        return layerUri;
    }

    public String getVersion() {
        return version;
    }

    public Date getUpgradeDate() {
        return upgradeDate;
    }

    public String getUpgradeToVersion() {
        return upgradeToVersion;
    }

    public Date getUpgradeStartTime() {
        return upgradeStartTime;
    }

    public String getPrevCompatibleVersion() {
        return prevCompatibleVersion;
    }

}
