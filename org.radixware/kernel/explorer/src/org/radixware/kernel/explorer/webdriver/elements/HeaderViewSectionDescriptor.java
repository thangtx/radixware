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

package org.radixware.kernel.explorer.webdriver.elements;

import com.trolltech.qt.gui.QHeaderView;

final class HeaderViewSectionDescriptor implements UIElementDescriptor{
    
    private final long viewNativeId;
    private final int section;
    
    public HeaderViewSectionDescriptor(final QHeaderView view, final int section) {
        viewNativeId = view.nativeId();
        this.section = section;
    }    
    
    public int getSectionIndex(){
        return section;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (int) (this.viewNativeId ^ (this.viewNativeId >>> 32));
        hash = 79 * hash + this.section;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HeaderViewSectionDescriptor other = (HeaderViewSectionDescriptor) obj;
        if (this.viewNativeId != other.viewNativeId) {
            return false;
        }
        if (this.section != other.section) {
            return false;
        }
        return true;
    }        
}
