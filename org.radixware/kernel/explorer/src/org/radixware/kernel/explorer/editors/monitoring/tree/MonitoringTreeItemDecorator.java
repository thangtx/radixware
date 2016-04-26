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

package org.radixware.kernel.explorer.editors.monitoring.tree;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.monitoringcommand.SysMonitoringRq;
import org.radixware.schemas.monitoringcommand.SysMonitoringRs;


public interface MonitoringTreeItemDecorator {
    
    public static final List<MonitoringTreeItemDecoration> HIDE_ITEM_DECORATIONS = new ArrayList<>();

    public void prepare(SysMonitoringRq rq,SysMonitoringRs rs);
    
    //public boolean isAppropriate(TreeItem item);

    public List<MonitoringTreeItemDecoration> decorate(TreeItem item);
    
   // public List<Id> getPropIds(TreeItem item);
    
    //public List<String> getMetricKinds();
}
