/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QWidget;


public class AutoResizingTable extends QTableWidget{
    
    public AutoResizingTable(){
        super();
        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
    }

    public AutoResizingTable(final QWidget parentWidget) {
        super(parentWidget);
        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
    }

    public AutoResizingTable(final int rows, final int columns) {
        super(rows, columns);
        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
    }

    public AutoResizingTable(final int rows, final int columns, final QWidget parentWidget) {
        super(rows, columns, parentWidget);
        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
    }
            
    @Override
    public QSize sizeHint() {
        final QSize size = super.sizeHint();        
        int width = 0;
        for (int i=0; i<columnCount(); i++){
            width += horizontalHeader().sectionSize(i);
        }
        if (verticalHeader().isVisible()){
            width += verticalHeader().width();
        }        
        
        int height = 0;
        for (int i=0; i<rowCount(); i++){
            height += verticalHeader().sectionSize(i);
        }
        if (horizontalHeader().isVisible()){
            height += horizontalHeader().height();
        }
        if (width>size.width()){
            size.setWidth(width);
        }
        if (height>size.height()){
            size.setHeight(height);
        }
        return size;
    }
    
    

}
