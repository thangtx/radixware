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

public class MouseClickEventFilter extends MouseEventFilter<MouseClickEventFilter> {

    public MouseClickEventFilter(EKeyEvent button, EKeyboardModifier modifier) {
        super(button, modifier, EHtmlEventType.CLICK);
    }

    public MouseClickEventFilter(EKeyEvent button, EnumSet<EKeyboardModifier> modifiers) {
        super(button, modifiers, EHtmlEventType.CLICK);
    }

    public MouseClickEventFilter(EKeyboardModifier modifier) {
        super(modifier, EHtmlEventType.CLICK);
    }

    public MouseClickEventFilter(EnumSet<EKeyboardModifier> modifiers) {
        super(modifiers, EHtmlEventType.CLICK);
    }
    
    public MouseClickEventFilter(EKeyEvent button) {
        super(button, EHtmlEventType.CLICK);
    }
}
