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

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.util.List;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.RadPresentationCommandDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.monitoring.UnitsWidget.UnitsWidgetIcons;
import org.radixware.kernel.explorer.editors.monitoring.tree.TreeItem;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.widgets.ExplorerAction;
import org.radixware.kernel.explorer.widgets.ExplorerMenu;


public class UnitsToolBar {
    private QToolButton btnUpdate;
    private QToolButton btnModalSelector;
    private QToolButton btnConfig;
    private QToolButton btnTurnOnEventPanel;
    private QToolButton btnShowCommands;
    private QToolBar toolBar;
    private final UnitsWidget parent;

    public UnitsToolBar(final UnitsWidget parent) {
        this.parent = parent;
        createUi();
    }

    private void createUi() {
        toolBar = new QToolBar(parent);
        toolBar.setObjectName("EditorToolBar");

        String toolTip = Application.translate("SystemMonitoring", "Reread from Database");
        btnUpdate = createToolBtn(toolTip, "update()", "btnUpdate", ExplorerIcon.getQIcon(ExplorerIcon.CommonOperations.REREAD));

        toolTip = Application.translate("SystemMonitoring", "Show Modal Dialog With Event Log for Current Item");
        btnModalSelector = createToolBtn(toolTip, "showModalSelector()", "btnModalSelector", ExplorerIcon.getQIcon(UnitsWidgetIcons.CONTEXT_EVENT_LOG));
        btnModalSelector.setEnabled(false);
        
        toolTip = Application.translate("SystemMonitoring", "Set Update Period");
        btnConfig = createToolBtn(toolTip, "changeConfig()", "btnConfig",ExplorerIcon.getQIcon(UnitsWidgetIcons.UPDATE_PERIOD) );

//        toolTip = Application.translate("SystemMonitoring", "Turn off Event Panel");
//        btnTurnOnEventPanel = createToolBtn(toolTip, "turnOnEventPanel()", "btnTurnOnEventPanel", parent.getEventLogIcon());
//        btnTurnOnEventPanel.setCheckable(true);
//        btnTurnOnEventPanel.setChecked(true);
        
        toolTip = Application.translate("SystemMonitoring", "Show Commands");
        btnShowCommands = createToolBtn(toolTip, "showCommands()", "btnShowCommands", ExplorerIcon.getQIcon(UnitsWidgetIcons.COMMANDS));
        btnShowCommands.setMenu(new ExplorerMenu());
        
        toolBar.addWidget(btnUpdate);        
        toolBar.addWidget(btnConfig);
        toolBar.addWidget(btnModalSelector);
        toolBar.addWidget(btnTurnOnEventPanel);
        toolBar.addSeparator();
        toolBar.addWidget(btnShowCommands);
    }

    private QToolButton createToolBtn(final String toolTip, final String connect, final String objName, final QIcon icon) {
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
    private void update() {
        //QApplication.postEvent(parent.getUnitsTree(), new UnitsTree.RereadEvent());
         parent.reread();
    }

    @SuppressWarnings("unused")
    private void showModalSelector() {
        parent.openEventLogDialog();
    }

    @SuppressWarnings("unused")
    private void changeConfig() {
        final ConfigDialog choosePeriod = new ConfigDialog(parent.getEnvironment(),parent);
        if (choosePeriod.exec() == 1) {
            if(parent.getMetricTimer().isActive()){
                parent.getMetricTimer().stop();
            }            
            final boolean isUpdate=choosePeriod.isUpdate();
            if(isUpdate){
                final int updateMetricPeriod=choosePeriod.getMetricsPeriod();
                parent.getMetricTimer().start(updateMetricPeriod);

                final ClientSettings settings = parent.getEnvironment().getConfigStore();
                final String settingsKey = SettingNames.SYSTEM + "/" + "system_monitor_unittree_update_metric_period";
                settings.writeInteger(settingsKey, updateMetricPeriod);            
            }
            setBtnUpdateState(isUpdate);
            parent.setIsUpdateEnabled(isUpdate);
        }
    }
    
    public void setBtnUpdateState(final boolean isUpdate){
        final QIcon icon=isUpdate?ExplorerIcon.getQIcon(UnitsWidgetIcons.UPDATE_PERIOD):
                ExplorerIcon.getQIcon(UnitsWidgetIcons.UPDATE_DISABLED);
        btnConfig.setIcon(icon);
    }

    @SuppressWarnings("unused")
    private void turnOnEventPanel() {
        final boolean isVisible = btnTurnOnEventPanel.isChecked();
        btnModalSelector.setEnabled(!isVisible);
        parent.showEventLogPanel(isVisible);
    }
    
    @SuppressWarnings("unused")
    private void showCommands(){
        final QTreeWidgetItem item= parent.getTree().currentItem();
        if (item != null && item instanceof TreeItem) {
            final EntityModel entity=((TreeItem) item).getEntityModel();
            if (entity == null) {
                return;
            }
            final List<Id> commandIds=entity.getAccessibleCommandIds();
            final ExplorerMenu menu=(ExplorerMenu)btnShowCommands.menu();
            menu.clear();
            if(!commandIds.isEmpty()){
                for (Id cmdId : commandIds) {
                    final Command command = entity.getCommand(cmdId);
                    if (!(command.getDefinition() instanceof RadPresentationCommandDef)
                            || (((RadPresentationCommandDef) command.getDefinition()).scope != ECommandScope.PROPERTY)) {
                        final ExplorerAction act=new ExplorerAction(command.getIcon(),command.getTitle(),parent);
                        act.setEnabled(command.isEnabled());
                        act.addActionListener(new ExecCommandListener(command));
                        menu.addAction((Action)act);
                    }
                }
                if(menu.isEmpty()){
                    final String str  = Application.translate("SystemMonitoring", "<no accessible commands>");
                    final QAction act=new QAction(str,parent);
                    act.setEnabled(false);
                    menu.addAction(act);
                }
                btnShowCommands.showMenu();
            }            
        }
    }
    
    private class ExecCommandListener implements Action.ActionListener{ 
        private final Command command;
        ExecCommandListener(final Command command){
            this.command=command;
        }

        @Override
        public void triggered(final Action action) {
            command.execute();
        }
    };        

    QToolBar getToolBar() {
        return toolBar;
    }
    
    /*public void setUpdBtnEnable(boolean isEnabled){
        btnUpdate.setEnabled(isEnabled);
    }*/
}
