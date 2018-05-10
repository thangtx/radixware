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

import java.util.Objects;
import java.util.UUID;

public final class UIElementId {    
    
    public final static String JSON_KEY = "element-6066-11e4-a52e-4f735466cecf";
    
    private final String id;
    
    private UIElementId(final String asString){
        id = asString;
    }
    
    public static UIElementId fromString(final String asString){
        return new UIElementId(asString);
    }
    
    public static UIElementId newInstance(){
        return new UIElementId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final UIElementId other = (UIElementId) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }        
}
