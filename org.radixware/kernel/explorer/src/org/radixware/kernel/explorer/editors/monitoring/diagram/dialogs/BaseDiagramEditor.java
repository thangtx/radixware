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

import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.dashboard.DiagramSettings;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;


public abstract class BaseDiagramEditor extends QWidget{
    protected QLineEdit edTitle;
    protected /*MetricHistWidget*/ ExplorerWidget parent; 
    public QSignalEmitter.Signal0 metricSet= new QSignalEmitter.Signal0();
    //protected AbstractMetricSettings metricSettings;
    
    BaseDiagramEditor(/*MetricHistWidget*/ ExplorerWidget parent/*,AbstractMetricSettings metricSettings*/){
        super(parent);
        this.parent=parent;
        //this.metricSettings=metricSettings;
    }

    public abstract boolean isComplete();
    
    public abstract /*AbstractMetricSettings*/ DiagramSettings getMetricSettings();//{
    //   return metricSettings;
   //}    
}
