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

package org.radixware.kernel.designer.common.general.nodes.hide;

import java.util.List;
import java.util.prefs.Preferences;
import org.openide.nodes.Node;
import org.openide.util.NbPreferences;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;


public class HideSettings {
    public static final String HIDDEN_NODES = "HiddenNodes";

    private HideSettings() {
    }

    public static boolean isHidable(Node node) {
        return isHidable(getRadixObject(node));
    }
    
    private static boolean isHidable(RadixObject radixObject) {
        if (radixObject != null) {
            if (radixObject instanceof Layer) {
                return true;
            }
            if (radixObject instanceof Module) {
                return true;
            }
        }
        return false;
    }

    private static boolean isModuleOrAbove(RadixObject radixObject) {
        if (radixObject instanceof Module) {
            return true;
        }
        if (radixObject instanceof Segment) {
            return true;
        }
        if (radixObject instanceof Layer) {
            return true;
        }
        if (radixObject instanceof Branch) {
            return true;
        }
        return false;
    }

    private static RadixObject getRadixObject(Node node) {
        RadixObject radixObject = null;
        if (radixObject == null) {
            radixObject = node.getLookup().lookup(RadixObject.class);
        }
        return radixObject;
    }

    public static boolean hasHidableChildren(Node node) {
        RadixObject radixObject = getRadixObject(node);
        if (radixObject != null) {
            return isModuleOrAbove(radixObject) && !(radixObject instanceof Module);
        }
        return false;
    }
    
    public static void collectHidableRadixObjects(Branch b, final List<RadixObject> list) {
        collectHidableRadixObjects(b, list, null);
    }
    
    public static void collectHidableRadixObjects(RadixObject context, final List<RadixObject> list, final IFilter filter) {
        if (context == null || list == null) {
            return;
        }
        list.clear();
        context.visit(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                list.add(radixObject);
            }
        }, new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return isHidable(radixObject) && (filter == null || filter.isTarget(radixObject));
            }
        });
    }
    
    private static String getNodeKey(RadixObject radixObject) {
        StringBuilder sb = new StringBuilder(radixObject.getName());
        RadixObject ownerObject = radixObject.getContainer();
        while (ownerObject != null) {
            if (ownerObject.getName().length() > 0) {
                sb.insert(0, "/");
                sb.insert(0, ownerObject.getName());
            }
            ownerObject = ownerObject.getContainer();
        }
        return sb.toString();
    }

    public static void storeHidden(boolean hidden, RadixObject radixObject) {
        if (hidden) {
            hide(false, radixObject);
        } else {
            restore(false, radixObject);
        }
    }
    
    public static void hide(boolean auto, RadixObject radixObject) {
        if (auto) {
            Preferences pref = NbPreferences.root().node(HIDDEN_NODES);
            String key = getNodeKey(radixObject);
            if (!pref.getBoolean(key, false)) {
                NbPreferences.root().node(HIDDEN_NODES).putBoolean(getNodeKey(radixObject), false);
            }
        } else {
            NbPreferences.root().node(HIDDEN_NODES).putBoolean(getNodeKey(radixObject), true);
        }
    }
    
    public static void restore(boolean auto, RadixObject radixObject) {
        if (auto) {
            //remove if it is automatically hidden
            Preferences pref = NbPreferences.root().node(HIDDEN_NODES);
            String key = getNodeKey(radixObject);
            if (!pref.getBoolean(key, false)) {
                pref.remove(key);
            }
        } else {
            NbPreferences.root().node(HIDDEN_NODES).remove(getNodeKey(radixObject));
        }
    }
    
    public static boolean isHidden(RadixObject radixObject) {
        return NbPreferences.root().node(HIDDEN_NODES).get(getNodeKey(radixObject), null) != null;
    }
    
    public static boolean readHidden(RadixObject radixObject) {
        return NbPreferences.root().node(HIDDEN_NODES).getBoolean(getNodeKey(radixObject), false);
    }
}
