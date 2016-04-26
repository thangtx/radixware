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


public enum ETextAlignment {
    LEFT("left"),CENTER("center"),RIGHT("right");
    
    private final String cssPropertyValue;
    
    private ETextAlignment(final String value){
        cssPropertyValue = value;
    }
    
    public String getCssPropertyValue(){
        return cssPropertyValue;
    }    
    
    public static ETextAlignment fromStr(final String asStr){
        for (ETextAlignment alignment: ETextAlignment.values()){
            if (alignment.name().equals(asStr)){
                return alignment;
            }
        }
        throw new IllegalArgumentException("Failed to parse text alignment \'"+asStr+"\'");
    }    
}
