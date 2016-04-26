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

package org.radixware.kernel.common.client.dashboard;

/**
 *
 * @author npopov
 */
public class DashPropContext {
    
    private final EContext context;
    private final String dashGuid;
    private final String widgetGuid;

    public DashPropContext(EContext context, String dashGuid, String widgetGuid) {
        this.context = context;
        this.dashGuid = dashGuid;
        this.widgetGuid = widgetGuid;
    }

    public DashPropContext(EContext context) {
        this(context, null, null);
    }
    
    public static enum EContext {

        WIDGET,
        WIDGET_REF,
        DASHBOARD,
        DASHBOARD_REF
    }

    public EContext getContext() {
        return context;
    }

    public String getDashGuid() {
        return dashGuid;
    }

    public String getWidgetGuid() {
        return widgetGuid;
    }
}
