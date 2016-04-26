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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QDialog;
import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.client.dashboard.DiagramSettings;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;


public class DiagramEditorDialog  extends ExplorerDialog{
    private final BaseDiagramEditor diagramEditor;
    
    public DiagramEditorDialog(DiagramSettings metricSettings,/*MetricHistWidget*/ MonitoringConfigHelper helper,boolean isHistorical){
        super(helper.getAdsBridge().getDashboardView().getEnvironment(), helper.getAdsBridge().getDashboardView(), "DiagramEditorDialog");
        this.setWindowTitle(Application.translate("SystemMonitoring", "Diagram Editor"));
        if(isHistorical){
            diagramEditor = new HistoricalDiagramEditor(metricSettings, helper);
            ((HistoricalDiagramEditor)diagramEditor).rangeChanged.connect(this,"checkMetric()");
            this.setMinimumSize(300, 420);
        }else{            
            diagramEditor = new CorrelationDiagramEditor(metricSettings, helper);
            this.setMinimumSize(300, 350);
        }
        createUI();
    }
    
    public DiagramEditorDialog(DiagramSettings metricSettings,/*MetricHistWidget*/ MonitoringConfigHelper  helper, Set<Long> ids){
        super(helper.getAdsBridge().getDashboardView().getEnvironment(), helper.getAdsBridge().getDashboardView(), "DiagramMetricEditorDialog");
        this.setWindowTitle(Application.translate("SystemMonitoring", "Metric Editor"));
        diagramEditor = new CorrelationMetricEditor(helper, ids);
        ((CorrelationMetricEditor)diagramEditor).open(metricSettings);
        createUI();
    }
    
    public DiagramSettings getMetricSettings(){
        return diagramEditor.getMetricSettings();
    }
    
    private void createUI() {
        
        diagramEditor.metricSet.connect(this, "checkMetric()");
        
        dialogLayout().addWidget(diagramEditor);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        checkMetric();
        this.setWindowModality(Qt.WindowModality.WindowModal);
        this.setVisible(true);       
    } 
    
    private void checkMetric(){
        if(getButton(EDialogButtonType.OK)!=null)
            getButton(EDialogButtonType.OK).setEnabled(diagramEditor.isComplete());
    }   

    @Override
    public void done(final int result) {
        if (result==QDialog.DialogCode.Accepted.value()
            && (diagramEditor instanceof HistoricalDiagramEditor)
            && !((HistoricalDiagramEditor)diagramEditor).isDateIntervalCorrect()
           ){
            return;
        }
        super.done(result);
    }
}
