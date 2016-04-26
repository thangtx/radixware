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

package org.radixware.wps.rwt;

import org.radixware.wps.HttpQuery;


public class Events {

    public static final String ACTION_EVENT_NAME = "_ae";
    private static final String RENDERED_EVENT_NAME = "_re";
    private static final String ACTION_EVENT_ACTION_NAME = "_a";
    public static final String ACTION_EVENT_ACTION_PARAM = "_p";
    public static final String EVENT_NAME_FOCUSED = "_wfc";
    public static final String EVENT_NAME_DEFOCUSED = "_wfl";
    public static final String EVENT_NAME_ONCLICK = "click";
    public static final String EVENT_NAME_ONDBLCLICK = "dblclick";
    public static final String EVENT_NAME_KEY_DOWN = "key";
    public static final String EVENT_NAME_TICK = "timer_tick";

    public static boolean isRenderedEvent(String eventName) {
        return RENDERED_EVENT_NAME.equals(eventName);
    }

    public static boolean isFocusedEvent(String eventName) {
        return EVENT_NAME_FOCUSED.equals(eventName);
    }
    public static boolean isTimerEvent(String eventName) {
        return EVENT_NAME_TICK.equals(eventName);
    }

    public static boolean isDefocusedEvent(String eventName) {
        return EVENT_NAME_DEFOCUSED.equals(eventName);
    }

    public static boolean isActionEvent(String eventName) {
        return ACTION_EVENT_NAME.equals(eventName);
    }

    public static String getActionName(HttpQuery.EventSet query) {
        return query.get(ACTION_EVENT_ACTION_NAME);
    }
}
