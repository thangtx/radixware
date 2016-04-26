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

package org.radixware.kernel.designer.common.editors.jml.vtag;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.jml.JmlTagEventCode;


public class VJmlTagEventCode<T extends JmlTagEventCode> extends VJmlTagLocalizedString<T> {

    public VJmlTagEventCode(T tag) {
        super(tag);
    }

    @Override
    public String getTokenName() {
        final EEventSeverity eventSeverity = this.getTag().getEventSeverity();
        
        if (eventSeverity != null) {
            return "tag-eventcode-" + eventSeverity.getName().toLowerCase();
        } else {
            return "tag-eventcode";
        }
    }
}
