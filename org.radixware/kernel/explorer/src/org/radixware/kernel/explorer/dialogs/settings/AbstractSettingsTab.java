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

package org.radixware.kernel.explorer.dialogs.settings;

import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QWidget;


abstract  class AbstractSettingsTab extends QWidget implements ISettingsPage{
    
    protected static final QGridLayout createLayout(){
        final QGridLayout layout = new QGridLayout();
        layout.setWidgetSpacing(16);
        layout.setContentsMargins(8, 20, 8, 0);
        layout.setAlignment(new Alignment(AlignmentFlag.AlignLeft,AlignmentFlag.AlignTop));
        return layout;
    }

}
