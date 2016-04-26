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

import java.util.EnumSet;


public enum EFontWeight {
    LIGHT(25,"lighter"),
    NORMAL(50,"normal"),
    DEMI_BOLD(63,"600"),
    BOLD(75,"bold"),
    BOLDER(87,"bolder");
    
    private final int qtValue;
    private final String cssValue;
    
    private EFontWeight(final int qt, final String css){
        qtValue = qt;
        cssValue = css;
    }
    
    public int getQtValue(){
        return qtValue;
    }
    
    public String getCssPropertyValue(){
        return cssValue;
    }
    
    public static EFontWeight forValue(final int value){
        if (value<0){
            throw new IllegalArgumentException("negative number is not allowed");
        }
        EFontWeight lighter = LIGHT;
        if (value<=lighter.qtValue){
            return lighter;
        }
        for ( EFontWeight weigth: EnumSet.complementOf(EnumSet.of(LIGHT)) ){
            if (value<weigth.qtValue){
                return value-lighter.qtValue < weigth.qtValue-value ? lighter : weigth;
            }
            lighter = weigth;
        }
        return BOLDER;
    }
    
    public static EFontWeight forCssValue(final String value){
        for ( EFontWeight weigth: EFontWeight.values() ){
            if (weigth.getCssPropertyValue().equals(value)){
                return weigth;
            }
        }
        final int valueAsInt;
        try{
            valueAsInt = Integer.parseInt(value);
        }catch(NumberFormatException exception){
            return NORMAL;
        }
        if (valueAsInt<400){
            return LIGHT;
        }else if (valueAsInt<600){
            return DEMI_BOLD;
        }else if (valueAsInt==600){
            return BOLD;
        }else{
            return BOLDER;
        }
    }
    
}