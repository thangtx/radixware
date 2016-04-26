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

package org.radixware.kernel.designer.common.general.editors.statistics;

import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.designer.common.general.editors.statistics.spi.IRadixObjectUsageCounter;
import org.radixware.kernel.designer.common.general.editors.statistics.spi.IStatisticsProcessor;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Layer;


@ServiceProviders({
    @ServiceProvider(service = IStatisticsProcessor.class),
    @ServiceProvider(service = IRadixObjectUsageCounter.class)})
public class RadixObjectUsageCounter implements IStatisticsProcessor, IRadixObjectUsageCounter {

    private final String USAGE_COUNTER = "UsageCounter";
    private final Map<ChangeListener, Object> changeListeners = new WeakHashMap<ChangeListener, Object>();

    @Override
    public void editorOpened(RadixObject radixObject) {
        Preferences preferences = NbPreferences.root().node(USAGE_COUNTER);
        while (radixObject != null) {
            String key = getKey(radixObject);
            if (key != null) {
                int cnt = preferences.getInt(key, 0);
                preferences.putInt(key, ++cnt);
            }
            radixObject = radixObject.getContainer();
        }
    }

    private String getKey(RadixObject radixObject) {
        if (radixObject instanceof Layer) {
            return ((Layer) radixObject).getURI();
        }
        if (radixObject instanceof Definition) {
            return ((Definition) radixObject).getId().toString();
        }
        return null;
    }

    @Override
    public int getUsageCount(RadixObject radixObject) {
        String key = getKey(radixObject);
        if (key == null) {
            return -1;
        }
        return NbPreferences.root().node(USAGE_COUNTER).getInt(key, 0);
    }

    @Override
    public void resetData(RadixObject radixObject) {
        String key = getKey(radixObject);
        if (key != null) {
            NbPreferences.root().node(USAGE_COUNTER).remove(key);
        }
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        changeListeners.put(listener, null);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        changeListeners.remove(listener);
    }
}
