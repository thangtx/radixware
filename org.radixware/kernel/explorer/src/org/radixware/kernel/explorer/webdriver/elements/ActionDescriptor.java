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

import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QWidget;

final class ActionDescriptor implements UIElementDescriptor{
    
    private final long ownerWidgetNativeId;
    private final long actionNativeId;
    
    public ActionDescriptor(final QWidget ownerWidget, final QAction action){
        ownerWidgetNativeId = ownerWidget.nativeId();
        actionNativeId = action.nativeId();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int) (this.ownerWidgetNativeId ^ (this.ownerWidgetNativeId >>> 32));
        hash = 83 * hash + (int) (this.actionNativeId ^ (this.actionNativeId >>> 32));
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
        final ActionDescriptor other = (ActionDescriptor) obj;
        if (this.ownerWidgetNativeId != other.ownerWidgetNativeId) {
            return false;
        }
        if (this.actionNativeId != other.actionNativeId) {
            return false;
        }
        return true;
    }
}
