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
package org.radixware.kernel.common.dialogs.chooseobject;

import javax.swing.Icon;

/**
 *
 * @author dlastochkin
 */
public class SelectableObjectDelegate {

    private final ISelectableObject object;

    public SelectableObjectDelegate(ISelectableObject object) {
        this.object = object;
    }   

    public ISelectableObject getObject() {
        return object;
    }

    public String getTitle() {
        return object == null ? null : object.getTitle();      
    }
    
    public Icon getIcon() {
        return object == null && object.getIcon() == null ? null : object.getIcon();
    }
    
    public String getLocation() {
        return object.getLocation();
    }
    
    public Icon getLocationIcon() {
        return object == null && object.getLocationIcon() == null ? null : object.getLocationIcon();
    }
    
    @Override
    public String toString() {
        return getTitle();
    }

}