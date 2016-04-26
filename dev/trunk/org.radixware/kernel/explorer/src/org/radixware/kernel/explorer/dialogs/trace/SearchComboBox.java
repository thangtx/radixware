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

package org.radixware.kernel.explorer.dialogs.trace;

import com.trolltech.qt.gui.QComboBox;


public class SearchComboBox extends QComboBox {

    public SearchComboBox() {
        super();
        this.setEditable(true);
    }
    
    public void setTopString(String str) {
        for (int i = 0; i < this.count(); ++i) {
            if (this.itemText(i).equals(str)) {
                this.removeItem(i);
                this.insertItem(0, str);
                this.setCurrentIndex(0);
                return;
            }
        }
        this.insertItem(0, str);
        this.setCurrentIndex(0);
        if (this.count() > 5)
            this.removeItem(5);
    }
    
}
