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

package org.radixware.kernel.common.client.enums;


public enum EFontStyle {
    NORMAL("normal"),ITALIC("italic"),OBLIQUE("oblique");
    
    private final String cssPropertyValue;
    
    private EFontStyle(final String propValue){
        cssPropertyValue = propValue;
    }
    
    public String getCssPropertyValue(){
        return cssPropertyValue;
    }    
    
    public static EFontStyle forIntValue(final int i){
        for (EFontStyle style: EFontStyle.values()){
            if (style.ordinal()==i){
                return style;
            }
        }
        throw new IllegalArgumentException("Font style with index "+String.valueOf(i)+" was not found");
    }    
}
