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

public class ContextMenuHtmlEvent extends InputEvent{

    private final String params;
    
    public ContextMenuHtmlEvent(int button, EnumSet<EKeyboardModifier> keyboardModifiers, String params) {
        super(button, keyboardModifiers);
        this.params = params;
    }
    
    public final String getParams() {
        return params;
    }
}
