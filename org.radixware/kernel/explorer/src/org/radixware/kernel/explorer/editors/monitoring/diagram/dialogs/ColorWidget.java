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

package org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs;

import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.dialogs.settings.ColoredFrame;


public class ColorWidget extends QWidget{
    private final QLabel labelTitle;
    private final ColoredFrame coloredFrame;
    
    public ColorWidget(final BaseDiagramEditor parent, final String gr, final String sub, final String n, final String description,QColor defaultColor) {
        labelTitle = new QLabel();
        labelTitle.setText(description);
        coloredFrame = new ColoredFrame(this, defaultColor, true);
        //coloredFrame.setColorSignal.connect(parent, "colorChanged(Object)");
    }
    
    public void addToParent(final int row, final QGridLayout gridLayout) {
        gridLayout.addWidget(labelTitle, row, 0);
        gridLayout.addWidget(coloredFrame, row, 1);
    }
    
    public QColor getColor() {
        return coloredFrame.getColor();
    }   
}
