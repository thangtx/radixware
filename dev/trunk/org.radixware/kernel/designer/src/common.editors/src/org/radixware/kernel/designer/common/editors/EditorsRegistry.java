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

package org.radixware.kernel.designer.common.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;


public class EditorsRegistry {

    private static boolean loaded = false;

    private static String getKey(RadixObject radixObject) {
        if (radixObject instanceof Definition) {
            final Definition def = (Definition) radixObject;
            final Module module = def.getModule();
            if (module != null && module.getSegment() != null && module.getSegment().getLayer() != null) {
                final Id[] idPath = def.getIdPath();
                if (idPath != null && idPath.length != 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(module.getSegment().getLayer().getURI());
                    sb.append(';');
                    if (def != module) {
                        sb.append(module.getId().toString());
                        sb.append(';');
                    }
                    for (Id id : idPath) {
                        sb.append(id.toString());
                        sb.append(';');
                    }
                    return sb.toString();
                }
            }
        }
        return null;
    }
    private static final int MAX = 10;
    private static final List<String> keys = new ArrayList<String>(MAX);

    public static void onScanned(final RadixObject radixObject) {
        final String key = getKey(radixObject);
        if (key != null) {
            final boolean contains;
            synchronized (keys) {
                if (!loaded) {
                    loaded = true;
                    load();
                }
                contains = keys.contains(key);
            }
            if (contains) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        EditorsManager.getDefault().open(radixObject);
                    }
                });
            }
        }
    }

    public static void onOpened(RadixObject radixObject) {
        final String key = getKey(radixObject);
        if (key != null) {
            synchronized (keys) {
                keys.remove(key);
                keys.add(key);
                while (keys.size() > MAX) {
                    keys.remove(0);
                }
                save();
            }
        }
    }

    public static void onClosed(RadixObject radixObject) {
        final String key = getKey(radixObject);
        if (key != null) {
            synchronized (keys) {
                keys.remove(key);
                save();
            }
        }
    }
    private static final String PREFERENCES_KEY = "EditorsRegistry";
    private static final String KEYS = "Keys";

    private static void load() {
        try {
            final Preferences preferences = Utils.findPreferences(PREFERENCES_KEY);
            if (preferences != null) {
                final String keysAsStr = preferences.get(KEYS, "");
                if (keysAsStr != null && !keysAsStr.isEmpty()) {
                    final String[] keysAsArr = keysAsStr.split("\n");
                    keys.addAll(Arrays.asList(keysAsArr));
                }
            }
        } catch (BackingStoreException ex) {
            ex.printStackTrace();
        }
    }

    private static void save() {
        final Preferences preferences = Utils.findOrCreatePreferences(PREFERENCES_KEY);
        final StringBuffer sb = new StringBuffer();
        for (String key : keys) {
            sb.append(key);
            sb.append('\n');
        }
        final String keysAsStr = sb.toString();
        preferences.put(KEYS, keysAsStr);

        try {
            Preferences.userRoot().flush();
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
        }
    }
}
