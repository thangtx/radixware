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

package org.radixware.kernel.explorer.views;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyle.StateFlag;
import com.trolltech.qt.gui.QStyleOption;
import com.trolltech.qt.gui.QWidget;


public class ToolBarSeparator extends QWidget{
    
    private Qt.Orientation orientation = Qt.Orientation.Vertical;
    
    public ToolBarSeparator(final QWidget parent){
        super(parent);  
        setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Minimum);
    }
    
    protected  QStyleOption getStyleOption(){
        final QStyleOption styleOption = new QStyleOption();
        styleOption.initFrom(this);
        if (orientation==Qt.Orientation.Vertical){
            final QStyle.State state = styleOption.state();           
            state.set(StateFlag.State_Horizontal);
            styleOption.setState(state);
        }
        return styleOption;
    }
    
    public final void setOrientation(final Qt.Orientation orientation){
        this.orientation = orientation;
        update();
    }
    
    public final Qt.Orientation getOrientation(){
        return orientation;
    }

    @Override
    protected void paintEvent(QPaintEvent paintEvent) {
        style().drawPrimitive(QStyle.PrimitiveElement.PE_IndicatorToolBarSeparator, getStyleOption(), new QPainter(this), parentWidget());
    }

    @Override
    public QSize sizeHint() {
        final int extent = style().pixelMetric(QStyle.PixelMetric.PM_ToolBarSeparatorExtent, getStyleOption(), parentWidget());
        return new QSize(extent, extent);
    } 
}