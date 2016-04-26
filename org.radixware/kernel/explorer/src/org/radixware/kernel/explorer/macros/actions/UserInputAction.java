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

package org.radixware.kernel.explorer.macros.actions;

import org.radixware.kernel.explorer.macros.widgets.QWidgetPath;


abstract class UserInputAction implements IMacroAction{

    protected final QWidgetPath widgetPath;

    protected UserInputAction(final QWidgetPath widget){
        widgetPath = widget;
    }

    @Override
    public boolean ignoreEvent() {
        return false;
    }

    public boolean canExecuteNow() {
        return widgetPath.findWidget()!=null;
    }

}
