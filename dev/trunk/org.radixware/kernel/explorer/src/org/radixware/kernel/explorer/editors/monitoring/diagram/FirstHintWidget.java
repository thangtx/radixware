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

package org.radixware.kernel.explorer.editors.monitoring.diagram;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class FirstHintWidget extends QWidget{
    private final MetricHistWidget parent;
    
    public FirstHintWidget(final MetricHistWidget parent){
        this.parent=parent;
        createUi();
    }
    
    private void createUi(){
        final QHBoxLayout layout=new QHBoxLayout();
        String text=Application.translate("SystemMonitoring", "Click");
        final QLabel lbPred=new QLabel(text,this);
        text=Application.translate("SystemMonitoring", "to add diagram and select metrics");
        final QLabel lbPost=new QLabel(text,this);
        final QIcon icon=ExplorerIcon.getQIcon(ExplorerIcon.CommonOperations.CREATE);

        final QPushButton btnAdd=new QPushButton(this);
        btnAdd.setIconSize(new QSize(24, 24));
        btnAdd.setIcon(icon);
        btnAdd.clicked.connect(parent, "addDiagram()");
        layout.addWidget(lbPred);   
        layout.addWidget(btnAdd); 
        layout.addWidget(lbPost,1, Qt.AlignmentFlag.AlignLeft); 
        setLayout(layout);
    }
    
}
