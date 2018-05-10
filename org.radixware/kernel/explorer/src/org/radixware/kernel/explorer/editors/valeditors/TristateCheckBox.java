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

package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionButton;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.utils.WidgetUtils;


final class TristateCheckBox extends QCheckBox{    
        
    private QPalette actualPalette;
    private final TristateCheckBoxStyle style;

    public TristateCheckBox(final QWidget parent) {
        super(parent);
        style = new TristateCheckBoxStyle(parent);
        setStyle(style);
    }


    private QPalette getActualPalette(){
        return actualPalette==null ? palette() : actualPalette;
    }

    public void applyPalette(final QPalette palette){
        actualPalette = palette;
        setPalette(palette);
    }

    @Override
    public void paintEvent(final QPaintEvent event){
        final QPainter p = new QPainter(this);
        final QStyleOptionButton opt = new QStyleOptionButton();            
        initStyleOption(opt);
        opt.setPalette(getActualPalette());
        style().drawControl(QStyle.ControlElement.CE_CheckBox, opt, p, this);            
    }        

    public QStyleOptionButton getSyleOption() {
        final QStyleOptionButton option = new QStyleOptionButton();            
        initStyleOption(option);
        option.setPalette(getActualPalette());
        return option;
    }        

    @Override
    public QSize sizeHint() {
        //default size hint width is greater
        final QStyleOptionButton option = getSyleOption();
        int width = style().subElementRect(QStyle.SubElement.SE_CheckBoxIndicator, option).width();
        width += 2;//bevel width
        return new QSize(width, super.sizeHint().height());
    } 

    @Override
    protected void disposed() {
        WidgetUtils.CustomStyle.release(style);
        super.disposed();        
    }
    
    
}