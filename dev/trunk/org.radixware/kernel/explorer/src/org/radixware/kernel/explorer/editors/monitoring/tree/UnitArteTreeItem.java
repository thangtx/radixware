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

import com.trolltech.qt.core.Qt.ItemDataRole;
import java.text.DecimalFormat;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.explorer.editors.monitoring.UnitsWidget;
import org.radixware.kernel.explorer.env.Application;


public class UnitArteTreeItem extends UnitTreeItem {

    public UnitArteTreeItem( UnitsWidget.IdsGetter idsGetter, final MetricInfoGetter.UnitInfo unitInfo, final boolean isInstanceStarted,final Model groupModel) {
        super( idsGetter,unitInfo, isInstanceStarted,groupModel);
    }
    
    @Override
    public void updateMetricDate(MetricInfoGetter metricInfo) {
        super.updateMetricDate(metricInfo);
        MetricInfoGetter.UnitInfo unitInfo=metricInfo.getUnitInfo(getId());
        if(unitInfo!=null){
            MetricData md = (MetricData) this.data(1, ItemDataRole.UserRole);
            Double unitArteSessionCnt = unitInfo.getUnitArteSessionCnt();
            Long maxArteInstCnt = unitInfo.getMaxArteInstCnt();
            if (md == null) {
                md = new MetricData(MetricData.Type.DIAGRAM);
                if (unitArteSessionCnt != null && maxArteInstCnt != 0) {
                    Double val=unitArteSessionCnt / maxArteInstCnt * 100;
                    md.setVal(val);
                    DecimalFormat f = new DecimalFormat("0.0");
                    md.setText(f.format(val)+"%");
                }
                this.setData(1, ItemDataRole.UserRole, md);
                //String toolTip = Application.translate("SystemMonitoring", "The relation between the number of ARTE instances used by unit and the maximum number of ARTE instances that can be used by unit"); //"Количество инстанций ARTE, используемых модулем, по отношению к максимальному количеству ARTE инстанций, которые могут быть использованы модулем"
                String toolTip = Application.translate("SystemMonitoring", "The relation between the number of ARTE instances used by unit and the maximum permitted number"); //"Отношение количества инстанций ARTE, используемых модулем, к максимально допустимому"
                this.setToolTip(1, toolTip);
            }else{               
                if (unitArteSessionCnt != null && maxArteInstCnt != 0) {
                    Double val=unitArteSessionCnt / maxArteInstCnt * 100;
                    md.setVal(val);
                    DecimalFormat f = new DecimalFormat("0.0");
                    md.setText(f.format(val)+"%");
                }
                updateText(1);
            }
        }
    }
}
