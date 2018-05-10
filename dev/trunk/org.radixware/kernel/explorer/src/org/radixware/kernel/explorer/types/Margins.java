/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.types;

import com.trolltech.qt.gui.QContentsMargins;


public final class Margins {
    
    public final static Margins EMPTY = new Margins(0, 0, 0, 0);
    
    public final int left;
    public final int top;
    public final int right;
    public final int bottom;

    public Margins(final int left, final int top, final int right, final int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public Margins(final QContentsMargins copy) {
        left = copy.left;
        top = copy.top;
        right = copy.right;
        bottom = copy.bottom;
    }

    public boolean isEmpty() {
        return left == 0 && top == 0 && right == 0 && bottom == 0;
    }

    public Margins expand(final int left, final int top, final int right, final int bottom) {
        if (left == 0 && top == 0 && right == 0 && bottom == 0) {
            return this;
        }
        return new Margins(left + this.left, top + this.top, right + this.right, bottom + this.bottom);
    }

}
