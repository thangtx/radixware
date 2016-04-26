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

package org.radixware.wps.rwt;


public enum Alignment {
  
    LEFT(0,"left"),
    RIGHT(1,"right"),
    CENTER(2,"center"),
    BASELINE(3,"baseline"),
    BOTTOM(4,"bottom"),
    MIDDLE(5,"middle"),
    SUB(6,"sub"),
    SUPER(7,"super"),
    TEXT_BOTTOM(8,"text-bottom"),
    TEXT_TOP(9,"text-top"),
    TOP(10,"top"),
    JUSTIFY(11,"justify"),
    START(12,"start"),
    END(13,"end"),
    INHERIT(14,"inherit");
    
    private final long val;
    private final String cssValue;
    
    private Alignment(final long i, final String cssVal) {
        this.val = i;
        cssValue = cssVal;
    }

    public static Alignment getForValue(long i) {
        Alignment a = Alignment.LEFT;//by default
        for (Alignment align : Alignment.values()) {
            if (align.getValue() == i) {
                a = align;
            }
        }
        return a;
    }
    
    public static Alignment getForCssValue(final String value){
        for (Alignment align : Alignment.values()) {
            if (align.getCssValue().equals(value)) {
                return align;
            }
        }
        throw new IllegalArgumentException("Unknown css alignment \'"+value+"\'");        
    }

    public static long getForName(String name) {
        long i = 0;
        for (Alignment align : Alignment.values()) {
            if (align.name().equals(name)) {
                i = align.getValue();
            }
        }
        return i;
    }

    public long getValue() {
        return val;
    }
    
    public String getCssValue(){
        return cssValue;
    }
    
    
}
