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

package org.radixware.wps.text;

import java.util.EnumSet;


public enum ECssPropertyName {        
    
    FOREGROUND_COLOR("color"), 
    BACKGROUND_COLOR("background-color"), 
    TEXT_ALIGNMENT("text-align"),
    FONT_FAMILY("font-family"),
    FONT_SIZE("font-size"),
    FONT_STYLE("font-style"),
    FONT_WEIGHT("font-weight"),
    TEXT_DECORATION("text-decoration");
    
    private final String propertyName;

    private ECssPropertyName(final String propName) {
        propertyName = propName;
    }

    public String getPropertyName() {
        return propertyName;
    }
    
    public static final EnumSet<ECssPropertyName> FONT_PROPERTIES = 
        EnumSet.of(FONT_FAMILY,FONT_SIZE,FONT_WEIGHT,FONT_STYLE,TEXT_DECORATION);
}
