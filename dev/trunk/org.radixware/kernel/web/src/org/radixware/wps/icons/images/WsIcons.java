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

package org.radixware.wps.icons.images;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.radixware.wps.icons.WpsIcon;


public class WsIcons {

    private static final Map<String, WpsIcon> stdIcons = new HashMap<String, WpsIcon>();

    private static WpsIcon createIcon(String path, boolean isSvg) {
        WpsIcon icon = new WpsIcon("icons/" + path, isSvg);
        stdIcons.put(path, icon);
        return icon;
    }
    public static final WpsIcon ADD = createIcon("add.svg", true);
    public static final WpsIcon CLEAR = createIcon("clear.svg", true);
    public static final WpsIcon CONNECT = createIcon("connect.svg", true);
    public static final WpsIcon EXPANDED = createIcon("expanded.svg", true);
    public static final WpsIcon COLLAPSED = createIcon("collapsed.svg", true);
    public static final WpsIcon WINDOW_HEADER = createIcon("wh1.png", false);
    public static final WpsIcon EMPTY = createIcon("empty.png", false);
    public static final WpsIcon SPIN_UP = createIcon("spin-up.svg", true);
    public static final WpsIcon SPIN_DOWN = createIcon("spin-down.svg", true);
    public static final WpsIcon LEAF = createIcon("leaf.svg", true);
    public static final WpsIcon WINDOW_CLOSE = createIcon("window-close.svg", true);

    public static InputStream getIconInputStream(String url) {
        if (url.startsWith("/icons/")) {
            String fileName = url.substring(7);
            return WsIcons.class.getResourceAsStream(fileName);
        } else if (url.startsWith("icons/")) {
            String fileName = url.substring(6);
            return WsIcons.class.getResourceAsStream(fileName);
        } else {
            return null;
        }
    }

    public static WpsIcon findIcon(String path) {
        synchronized (stdIcons) {
            return stdIcons.get(path);
        }
    }
}
