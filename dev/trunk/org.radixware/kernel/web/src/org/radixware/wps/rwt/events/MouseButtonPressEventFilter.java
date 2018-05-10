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


public class MouseButtonPressEventFilter extends MouseEventFilter<MouseButtonPressEventFilter>{

    public MouseButtonPressEventFilter(EKeyboardModifier modifier) {
        super(modifier, EHtmlEventType.MOUSEDOWN);
    }

    public MouseButtonPressEventFilter(EKeyEvent button, EKeyboardModifier modifier) {
        super(button, modifier, EHtmlEventType.MOUSEDOWN);
    }

    public MouseButtonPressEventFilter(EnumSet<EKeyboardModifier> modifiers) {
        super(modifiers, EHtmlEventType.MOUSEDOWN);
    }

    public MouseButtonPressEventFilter(EKeyEvent button, EnumSet<EKeyboardModifier> modifiers) {
        super(button, modifiers, EHtmlEventType.MOUSEDOWN);
    }

    public MouseButtonPressEventFilter(EKeyEvent button) {
        super(button, EHtmlEventType.MOUSEDOWN);
    }
}