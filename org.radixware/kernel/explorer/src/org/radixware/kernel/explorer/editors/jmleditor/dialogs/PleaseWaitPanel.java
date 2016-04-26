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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;


import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.env.Application;


public class PleaseWaitPanel extends QWidget{

    public PleaseWaitPanel(final QWidget parent){
        super(parent);
        this.setStyleSheet("border: 1px solid lightGray");
        this.setSizePolicy(Policy.Expanding, Policy.Expanding);
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setMargin(0);
        
        final QLabel lbPleaseWait=new QLabel(Application.translate("JmlEditor","Please Wait..."));
        lbPleaseWait.setAlignment(AlignmentFlag.AlignCenter);
        
        layout.addWidget(lbPleaseWait);
        setLayout(layout);
    }

}
