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

package org.radixware.kernel.common.defs.ads.clazz.sql.report.html;

import java.awt.Color;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;


public class CellContents {
     private  AdsReportAbstractAppearance.Font font;
     private  Color fgColor;
     private  Color bgColor;
     private  String text;

     CellContents(AdsReportAbstractAppearance.Font font,String text,Color fgColor,Color bgColor){
        this.font=font;
        this.text=text;
        this.fgColor=fgColor;
        this.bgColor=bgColor;
     }
     
     public CellContents(CellContents cellContant){
         this(cellContant.getFont().copy(),cellContant.getText(),cellContant.getFgColor(),cellContant.getBgColor());
     }

     public AdsReportAbstractAppearance.Font getFont(){
        return font;
     }

     void setFont(AdsReportAbstractAppearance.Font font){
        this.font=font;
     }

     public String getText(){
        return text;
     }

     public void setText(String contant){
        this.text=contant;
     } 
     
     public Color getFgColor(){
        return fgColor;
     }

     void setFgColor(Color color){
        this.fgColor=color;
     } 
     
     public Color getBgColor(){
        return bgColor;
     }

     void setBgColor(Color color){
        this.bgColor=color;
     } 
}
