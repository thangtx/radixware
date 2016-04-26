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

import java.util.ArrayList;
import java.util.List;


public class CorrelationMetricSettings extends MetricSettings/*AbstractMetricSettings*/{
    private List<MetricSettings> metricSettingsList;
    private boolean isShowValues=false;
    private boolean isPercent=false;
    
    public CorrelationMetricSettings(){
        metricSettingsList=new ArrayList<>();
    }
    
    public  List<MetricSettings> getMetricSettingsList(){
        return metricSettingsList;
    }
    
     public void setShowValues(boolean isAutoRange) {
        this.isShowValues=isAutoRange;
    }
    
    public boolean  isShowValues() {
        return isShowValues;
    }
    
     public void setPercent(boolean isAutoRange) {
        this.isPercent=isAutoRange;
    }
    
    public boolean  isPercent() {
        return isPercent;
    }
}
