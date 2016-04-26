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

import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.editors.monitoring.UnitsWidget.UnitsWidgetIcons;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget.MetricHistIcons;
import org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.CfgUpdatePeriodDialog;
import org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.SaveToFileDialog;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class MetricHistToolBar {

    private final QToolBar toolBar;
    //private QToolButton btnUpdate;
    private QToolButton btnAdd;
    private QToolButton btnConfig;
    private QToolButton btnNewTab;
    private QToolButton btnSaveToFile;
    private QToolButton btnLoadFromFile;
    private final MetricHistWidget parent;

    MetricHistToolBar(final MetricHistWidget parent) {
        toolBar = new QToolBar(parent);
        this.parent = parent;
        createUi();
    }

    private void createUi() {
        toolBar.setObjectName("EditorToolBar");

        String toolTip = Application.translate("SystemMonitoring", "Add Diagram");
        btnAdd = createToolBtn(toolTip, "actAdd()", "btnAdd", ExplorerIcon.getQIcon(ExplorerIcon.CommonOperations.CREATE));        
        toolTip = Application.translate("SystemMonitoring", "Set Update Period");
        btnConfig = createToolBtn(toolTip, "actChangeConfig()", "btnConfig", ExplorerIcon.getQIcon(UnitsWidgetIcons.UPDATE_PERIOD));        
        toolTip = Application.translate("SystemMonitoring", "Add New Tab");
        btnNewTab = createToolBtn(toolTip, "actAddNewTab()", "btnNewTab", ExplorerIcon.getQIcon(MetricHistIcons.ADD_TAB));
        toolTip = Application.translate("SystemMonitoring", "Save to File");
        btnSaveToFile = createToolBtn(toolTip, "btnSaveToFile_Clicked()", "btnSaveToFile", ExplorerIcon.getQIcon(MetricHistIcons.IMG_EXPORT));
        toolTip = Application.translate("SystemMonitoring", "Load from File");
        btnLoadFromFile = createToolBtn(toolTip, "btnLoadFromFile_Clicked()", "btnLoadFromFile", ExplorerIcon.getQIcon(MetricHistIcons.IMG_IMPORT));

        toolBar.addWidget(btnAdd);
        toolBar.addWidget(btnConfig);
        toolBar.addWidget(btnNewTab);
        toolBar.addSeparator();
        toolBar.addWidget(btnSaveToFile);
        toolBar.addWidget(btnLoadFromFile);
    }
    
    @SuppressWarnings("unused")
    private void btnSaveToFile_Clicked() {
        final SaveToFileDialog choosePeriod = new SaveToFileDialog(parent.getEnvironment(),parent);
        if (choosePeriod.exec() == 1) {
            parent.saveTofile(choosePeriod.getFileName(),choosePeriod.saveCurrentPage());
        }
    } 
    
    
    @SuppressWarnings("unused")
    private void btnLoadFromFile_Clicked() {
        final String text = Application.translate("SystemMonitoring", "Import from File"); 
        final String filename = QFileDialog.getOpenFileName(parent,text,QDir.homePath(),new com.trolltech.qt.gui.QFileDialog.Filter("XML Files (*.xml)"));//chooseFile(QFileDialog.FileMode.ExistingFile, QFileDialog.AcceptMode.AcceptOpen, "Import from File");
        parent.readFromFile(filename);
    }
    
    public void setBtnAddEnabled(final boolean enable){
        btnAdd.setEnabled(enable);
    }
   
    private QToolButton createToolBtn(final String toolTip, final String connect,final String objName, final QIcon icon) {
        final QToolButton btn = new QToolButton();
        btn.setObjectName(objName);
        btn.setAutoRaise(true);
        btn.setToolTip(toolTip);
        btn.setIcon(icon);
        btn.setIconSize(new QSize(24, 24));
        btn.clicked.connect(this, connect);
        return btn;
    }

    @SuppressWarnings("unused")
    private void actAdd() {
        parent.addDiagram();
    }

    @SuppressWarnings("unused")
    private void actChangeConfig() {       
        final CfgUpdatePeriodDialog choosePeriod = new CfgUpdatePeriodDialog(parent.getEnvironment(),parent);
        if (choosePeriod.exec() == 1) {
            if(parent.getMetricTimer().isActive()){
                parent.getMetricTimer().stop();
            }
            final boolean isUpdate=choosePeriod.isUpdate();
            if(isUpdate){
                final int updateMetricPeriod=choosePeriod.getMetricsPeriod();
                parent.getMetricTimer().start(updateMetricPeriod); 

                final ClientSettings settings = parent.getEnvironment().getConfigStore();
                final String settingsKey = SettingNames.SYSTEM + "/" + "system_monitor_productivity_update_metric_period";
                settings.writeInteger(settingsKey, updateMetricPeriod);
            }
            setBtnUpdateState(isUpdate);
            parent.setIsUpdate(isUpdate);
        }
    }    
     
    public void setBtnUpdateState(final boolean isUpdate){
        final QIcon icon=isUpdate?ExplorerIcon.getQIcon(UnitsWidgetIcons.UPDATE_PERIOD):
                ExplorerIcon.getQIcon(UnitsWidgetIcons.UPDATE_DISABLED);
        btnConfig.setIcon(icon);
    }

    public QToolBar getToolBar() {
        return toolBar;
    }
    
    private void actAddNewTab(){
        parent.addTab();
    }
}
