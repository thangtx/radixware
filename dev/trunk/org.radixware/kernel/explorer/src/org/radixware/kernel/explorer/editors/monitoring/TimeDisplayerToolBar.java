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

package org.radixware.kernel.explorer.editors.monitoring;

import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QToolBar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.radixware.kernel.explorer.env.Application;


public class TimeDisplayerToolBar {
    
    private QLabel lbLatUpdateTimeUnits;
    private QLabel lbLatUpdateTimeEventLog;
    private QToolBar toolBar;
    private final UnitsWidget parent;
    private final static String sLastUpdateTimeUnits=Application.translate("SystemMonitoring", "Last update time for units tree:");
    private final static String sLastUpdateTimeEventLogs=Application.translate("SystemMonitoring", "for event log:");
            
    public TimeDisplayerToolBar(final UnitsWidget parent){
        this.parent = parent;
        createUi();
    }
    
    private void createUi() {
        toolBar = new QToolBar(parent);
        toolBar.setObjectName("EditorToolBar");

        lbLatUpdateTimeUnits=new QLabel(toolBar);
        //String s=Application.translate("SystemMonitoring", "Last update time for units tree:"+" ");
        //lbLatUpdateTimeUnits.setText(s);        
        lbLatUpdateTimeEventLog=new QLabel(toolBar);
        //s=Application.translate("SystemMonitoring", "; "+"for event logs:"+" ");
        //lbLatUpdateTimeEventLog.setText(s);   
        

        toolBar.addWidget(lbLatUpdateTimeUnits);
        toolBar.addWidget(lbLatUpdateTimeEventLog);        
    }
    
    public void setLastUpdateTimeUnits(){
        //Timestamp time=parent.getEnvironment().getCurrentServerTime();
        final Calendar curDateTime=Calendar.getInstance();        
        final SimpleDateFormat dateTimeFormat =new SimpleDateFormat("HH:mm:ss",parent.getEnvironment().getLocale());
        lbLatUpdateTimeUnits.setText(sLastUpdateTimeUnits+" "+dateTimeFormat.format(curDateTime.getTime()));
    }
    
    public void setLastUpdateTimeEventLog(){
        //Timestamp time=parent.getEnvironment().getCurrentServerTime();
        final Calendar curDateTime=Calendar.getInstance();
        final SimpleDateFormat dateTimeFormat =new SimpleDateFormat("HH:mm:ss",parent.getEnvironment().getLocale());
        lbLatUpdateTimeEventLog.setText("; "+sLastUpdateTimeEventLogs+" "+dateTimeFormat.format(curDateTime.getTime()));
    }
    
    public QToolBar getToolBar() {
        return toolBar;
    }
    
}
