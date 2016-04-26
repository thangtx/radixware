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

package org.radixware.kernel.reporteditor.common;

import java.util.EventListener;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.userreport.common.IUserDefChangeSuppert;
import org.radixware.kernel.common.userreport.repository.UserReport.ReportVersion;
import org.radixware.kernel.common.userreport.repository.UserReport.ReportVersions;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlSchemes;
import org.radixware.kernel.common.userreport.repository.role.AppRoles;


public class VersionChangeSuppert implements IUserDefChangeSuppert{
     private final ChangeSupport changeSupport;// = new ChangeSupport(this);
     
     public VersionChangeSuppert(final ReportVersion reportVersion){
         changeSupport = new ChangeSupport(reportVersion);
     }
     
     public VersionChangeSuppert(final ReportVersions reportVersions){
         changeSupport = new ChangeSupport(reportVersions);
     }
     
     public VersionChangeSuppert(final AppRoles appRoles){
         changeSupport = new ChangeSupport(appRoles);
     }
     
     public VersionChangeSuppert(final MsdlSchemes msdlSchemes){
         changeSupport = new ChangeSupport(msdlSchemes);
     }

    @Override
    public void removeChangeListener(final EventListener listener) {
        changeSupport.removeChangeListener((ChangeListener)listener);
    }

    @Override
    public void addChangeListener(final EventListener listener) {
        changeSupport.addChangeListener((ChangeListener)listener);
    }
    
     @Override
    public void fireChange(){
        changeSupport.fireChange();
    }
    
     @Override
    public boolean hasListeners() {
        return changeSupport.hasListeners();
    }
    
}
