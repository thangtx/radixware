/*
* Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.wps.rwt.events;

import java.util.EnumSet;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;
import org.radixware.kernel.common.enums.EKeyEvent;


public class MouseDoubleClickEventFilter extends MouseEventFilter<MouseDoubleClickEventFilter>{

    public MouseDoubleClickEventFilter(EKeyboardModifier modifier) {
        super(modifier, EHtmlEventType.DOUBLECLICK);
    }

    public MouseDoubleClickEventFilter(EKeyEvent button, EKeyboardModifier modifier) {
        super(button, modifier, EHtmlEventType.DOUBLECLICK);
    }

    public MouseDoubleClickEventFilter(EnumSet<EKeyboardModifier> modifiers) {
        super(modifiers, EHtmlEventType.DOUBLECLICK);
    }

    public MouseDoubleClickEventFilter(EKeyEvent button, EnumSet<EKeyboardModifier> modifiers) {
        super(button, modifiers, EHtmlEventType.DOUBLECLICK);
    }

    public MouseDoubleClickEventFilter(EKeyEvent button) {
        super(button, EHtmlEventType.DOUBLECLICK);
    }
}