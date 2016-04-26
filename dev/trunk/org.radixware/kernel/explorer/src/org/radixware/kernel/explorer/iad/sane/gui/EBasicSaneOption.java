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

package org.radixware.kernel.explorer.iad.sane.gui;


enum EBasicSaneOption {
    
    SCAN_SOURCE("source",true),
    SCAN_FILM_TYPE("film-type",true),
    NEGATIVE("negative",true),
    SCAN_MODE("mode",true,"Color"),
    BIT_DEPTH("depth",true,"8"),
    THRESHOLD("threshold",true),
    SCAN_RESOLUTION("resolution",true,"600"),
    SCAN_X_RESOLUTION("x-resolution",true),
    SCAN_Y_RESOLUTION("y-resolution",true),
    PREVIEW("preview",false),    
    SCAN_TL_X("tl-x",false),
    SCAN_TL_Y("tl-y",false),
    SCAN_BR_X("br-x",false),
    SCAN_BR_Y("br-y",false),
    BRIGHTNESS("brightness",true),    
    CONTRAST("contrast",true),
    GAMMA_VECTOR_R("red-gamma-table",true),
    GAMMA_VECTOR_G("green-gamma-table",true),
    GAMMA_VECTOR_B("blue-gamma-table",true),
    BLACK_LEVEL("black-level",true),
    WHITE_LEVEL("white-level",true);
        
    private final String optionName;
    private final boolean hasGui;
    private final String defaultValue;
    
    private EBasicSaneOption(final String optionName, final boolean gui){
        this.optionName = optionName;
        hasGui = gui;
        defaultValue = null;        
    }    
    
    private EBasicSaneOption(final String optionName, final boolean gui, final String valueAsStr){
        this.optionName = optionName;
        hasGui = gui;
        defaultValue = valueAsStr;
    }
    
    public boolean hasGui(){
        return hasGui;
    }
    
    public String optionName(){
        return optionName;
    }
    
    public String getDefaultValueAsString(){
        return defaultValue;
    }
    
    public static EBasicSaneOption findForOptionName(final String optionName){
        for (EBasicSaneOption basicOption: EBasicSaneOption.values()){
            if (basicOption.optionName.equals(optionName)){
                return basicOption;
            }
        }
        return null;
    }
}
