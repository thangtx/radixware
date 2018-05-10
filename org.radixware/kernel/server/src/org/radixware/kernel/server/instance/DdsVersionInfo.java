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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixLoader;

public class DdsVersionInfo {

    private final List<DdsVersionInfoItem> items;

    public DdsVersionInfo(List<DdsVersionInfoItem> items) {
        this.items = new ArrayList<>(items);
        Collections.sort(this.items, new Comparator<DdsVersionInfoItem>() {

            private final List<LayerMeta> layerUrisFromBottom = RadixLoader.getInstance().getCurrentRevisionMeta().getAllLayersSortedFromBottom();

            @Override
            public int compare(DdsVersionInfoItem o1, DdsVersionInfoItem o2) {
                return indexOf(o1.getLayerUri()) - indexOf(o2.getLayerUri());
            }

            private int indexOf(final String layerUri) {
                for (int i = 0; i < layerUrisFromBottom.size(); i++) {
                    if (layerUrisFromBottom.get(i).getUri().equalsIgnoreCase(layerUri)) {
                        return i;
                    }
                }
                return -1;
            }

        });
    }

    public List<DdsVersionInfoItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public String getVersionAsString() {
        final StringBuilder sb = new StringBuilder();
        for (DdsVersionInfoItem item : items) {
            if (sb.length() > 0) {
                sb.append(RevisionMeta.VERSIONS_STR_SEPARATOR);
            }
            sb.append(item.getLayerUri());
            sb.append("=");
            sb.append(item.getVersion());
        }
        return sb.toString();
    }
}
