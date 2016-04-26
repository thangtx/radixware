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

import java.util.List;


public enum ECssTextOptionsStyle {
    
    DEFAULT_UI("rwt-ui-element"),
    DEFAULT_LABEL("rwt-ui-element-text"),
    REGULAR_VALUE("rwt-regular-value"),
    REGULAR_TITLE("rwt-regular-value-title"),
    READONLY_VALUE("rwt-readonly-value"),
    REQUIRED_VALUE_TITLE("rwt-required-value-title"),
    INHERITED_VALUE("rwt-inherited-value"),
    OVERRIDED_VALUE("rwt-overrided-value"),
    INVALID_VALUE("rwt-invalid-value"),
    UNDEFINED_VALUE("rwt-undefined-value"),
    INVALID_VALUE_TITLE("rwt-invalid-value-title");
    
    private final String name;
    private ECssTextOptionsStyle(final String name){
        this.name = name;
    }
    
    public String getStyleName(){
        return name;
    }
    
    static ECssTextOptionsStyle findActualStyle(final List<String> classes){
        ECssTextOptionsStyle actualStyle = null;
        for (String clazz: classes){
            for (ECssTextOptionsStyle style: ECssTextOptionsStyle.values()){
                if (style.getStyleName().equals(clazz)){
                    if (actualStyle==null || actualStyle.ordinal()<style.ordinal()){
                        actualStyle = style;
                    }
                    break;
                }
            }
        }
        return actualStyle;
    }
}
