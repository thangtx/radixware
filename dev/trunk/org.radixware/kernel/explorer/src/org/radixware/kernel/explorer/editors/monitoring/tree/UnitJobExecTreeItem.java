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
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.text.DecimalFormat;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.explorer.editors.monitoring.UnitsWidget;
import org.radixware.kernel.explorer.editors.monitoring.tree.MetricData.Range;
import org.radixware.kernel.explorer.env.Application;


public class UnitJobExecTreeItem extends UnitTreeItem {    

    public UnitJobExecTreeItem( UnitsWidget.IdsGetter idsGetter,MetricInfoGetter.UnitInfo unitInfo, final boolean isInstanceStarted, final Model groupModel, QTreeWidgetItem parent) {
        super(idsGetter, unitInfo, isInstanceStarted, groupModel, parent);
    }
    
    @Override
    public void updateMetricDate(MetricInfoGetter metricInfo) {
        super.updateMetricDate(metricInfo);
        MetricData md = (MetricData) this.data(1, ItemDataRole.UserRole);
        MetricInfoGetter.UnitInfo unitInfo=metricInfo.getUnitInfo(getId());
        if(unitInfo!=null){
            Double jobExecCnt=unitInfo.getJobExecCnt();
            Double jobWaitCnt=unitInfo.getJobWaitCnt();
            Long maxJobCnt = unitInfo.getMaxJobExecCnt();
            if(md == null){
                md = new MetricData(MetricData.Type.DIAGRAM);
                if (jobExecCnt != null && maxJobCnt != 0) {
                    Double val=jobExecCnt / maxJobCnt * 100;
                    md.setVal(val);
                    DecimalFormat f = new DecimalFormat("0.0");
                    md.setText(f.format(val)+"%");
                }
                this.setData(1, ItemDataRole.UserRole, md);
                String toolTip = Application.translate("SystemMonitoring", "The relation between the number of executing jobs and the maximum number of jobs");//Количество исполняющихся заданий по отношению к максимальному количеству заданий
                this.setToolTip(1, toolTip);
            }else {                
                if (jobExecCnt != null && maxJobCnt != 0) {
                    Double val=jobExecCnt / maxJobCnt * 100;
                    md.setVal(val);
                    DecimalFormat f = new DecimalFormat("0.0");
                    md.setText(f.format(val)+"%");
                }
            }

            md = (MetricData) this.data(2, ItemDataRole.UserRole);
            if (md == null) {
                md = new MetricData(MetricData.Type.HTML);
                md.setVal(jobWaitCnt);
                this.setData(2, ItemDataRole.UserRole, md);
                this.setText(2, createHtml(md.getVal(), md.getRange()));
                String toolTip = Application.translate("SystemMonitoring", "Average number of waiting jobs");//Количество ожидающих заданий
                this.setToolTip(2, toolTip);
            }else{
                md.setVal(jobWaitCnt);
            }
            updateText(1);
            updateText(2);
        }
    }
  
    private String createHtml(Double val, Range range) {
        if(val!=null){
            DecimalFormat f = new DecimalFormat("0.0");
            String text = f.format(val);
            if (range == Range.ERROR) {
                text = "<font color=\"#FF0000\">" + text + "</font>";
            } else if (range == Range.WARNING) {
                text = "<font color=\"#808000\">" + text + "</font>";
            }
            return "(" + text + ")";
        }
        return "";
    }
}
