/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
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

import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QWidget;


public class TextView extends QTextEdit{

    public TextView(){
        super();
        init();
    }
    
    public TextView(final QWidget parent){
        super(parent);    
        init();
    }
    
    
    public TextView(final String text, final QWidget parent){
        super(text,parent);
        init();
    }
    
    private void init(){
        final QPalette palette = new QPalette(palette());
        palette.setColor(QPalette.ColorRole.Base, palette.color(QPalette.ColorRole.Window));        
        setPalette(palette);
        setReadOnly(true);
    }
    
}
