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

package org.radixware.kernel.explorer.editors.scml;

import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QIcon;


public class ScmlAnnotation implements IScmlAnnotation{
     private QColor bgColor;
     private QColor fgColor;     
     private final IScmlItem item_;
     private final int offset_;
     private String toolTip_;
     private QIcon icon_;
     
     public ScmlAnnotation(final IScmlItem item, int offset){
         bgColor=QColor.red;
         fgColor=null;
         item_=item;
         offset_=offset;
     }     

    public IScmlItem getItem() {
        return item_;
    }

    @Override
    public int getItemIndex() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getItemOffset() {
       return offset_;
    }

    @Override
    public QColor getBackgroundColor() {
        return bgColor;
    }
    
    public void setBackgroundColor(QColor color) {
        bgColor=color;
    }

    @Override
    public QColor getForegroundColor() {
        return fgColor;
    }
    
    public void setForegroundColor(QColor color) {
        fgColor=color;
    }

    @Override
    public String getToolTip() {
        return toolTip_;
    }
    
    public void setToolTip(String toolTip){
        toolTip_=toolTip;
    }

    @Override
    public QIcon getIcon() {
        return icon_;
    }
    
    public void setIcon(QIcon icon){
        icon_=icon;
    }
    
}
