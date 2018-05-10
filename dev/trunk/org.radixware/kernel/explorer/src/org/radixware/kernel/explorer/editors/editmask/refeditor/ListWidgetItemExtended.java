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

package org.radixware.kernel.explorer.editors.editmask.refeditor;

import org.radixware.kernel.common.client.widgets.ListWidgetItem;

class ListWidgetItemExtended extends ListWidgetItem {

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ListWidgetItem) {
            ListWidgetItem curObject = (ListWidgetItem) obj;
            if (curObject.getValue() == null && this.getValue() == null) {
                if (curObject.getUserData() == null && this.getUserData() == null) {
                    return true;
                } else return curObject.getUserData() != null && this.getUserData() != null && curObject.getUserData().equals(this.getUserData());
            } else {
                if (curObject.getValue() != null && this.getValue() != null) {
                    return curObject.getValue().equals(this.getValue());
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}
