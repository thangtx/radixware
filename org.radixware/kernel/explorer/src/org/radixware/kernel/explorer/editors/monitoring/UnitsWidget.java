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

import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.*;
import java.util.List;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.RadPresentationCommandDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.SelectEntityDialog;
import org.radixware.kernel.explorer.editors.monitoring.tree.TreeItem;
import org.radixware.kernel.explorer.editors.monitoring.tree.UnitsTree;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.views.Splitter;
import org.radixware.kernel.explorer.widgets.ExplorerAction;
import org.radixware.kernel.explorer.widgets.ExplorerMenu;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;

public class UnitsWidget extends ExplorerWidget {

    private UnitsTree unitsTree;
    private final EventLogPanel eventLogPanel;
    private QTimer timerMetric;
    private final Model sourceModel;
    private GroupModel eventLogWithContextModel;
    private final IdsGetter idsGetter;
    private final IProgressHandle prohressHand;
    private boolean isUpdateEnabled;
    private QToolButton btnShowCommands;

    public static enum EPropertyName {

        PARAM_CONTEXT_ID, PARAM_CONTEXT_TYPE,
    }

    public static enum EExplorerItemName {

        INSTANCE, UNIT, NET_CHANNEL, /*EVENTLOG,*/ EVENTLOG_WITH_CONTEXT
    }

    public interface IdsGetter {

        Id getPropId(EPropertyName propName);

        Id getExplorerItemId(EExplorerItemName explorerItemName);

        Id getInstanceTableId();

        Id getUnitTableId();

        Id getChannelTableId();

        Id getCommandId();
    }

    public static final class UnitsWidgetIcons extends ExplorerIcon.CommonOperations {

        private UnitsWidgetIcons(final String fileName) {
            super(fileName, true);
        }
        public static final UnitsWidgetIcons STARTED = new UnitsWidgetIcons("classpath:images/run.svg");
        public static final UnitsWidgetIcons STOPPED = new UnitsWidgetIcons("classpath:images/stop.svg");
        public static final UnitsWidgetIcons NOT_USED = new UnitsWidgetIcons("classpath:images/cancel.svg");
        public static final UnitsWidgetIcons INSTANCE_NOT_STARTED = new UnitsWidgetIcons("classpath:images/exceptionClass.svg");
        public static final UnitsWidgetIcons HANG = new UnitsWidgetIcons("classpath:images/percent.svg");
        public static final UnitsWidgetIcons UPDATE_PERIOD = new UnitsWidgetIcons("classpath:images/update_period.svg");
        public static final UnitsWidgetIcons UPDATE_DISABLED = new UnitsWidgetIcons("classpath:images/update_disabled.svg");
        public static final UnitsWidgetIcons CONTEXT_EVENT_LOG = new UnitsWidgetIcons("classpath:images/context_event_log.svg");
        public static final UnitsWidgetIcons COMMANDS = new UnitsWidgetIcons("classpath:images/command.svg");
    }

    public UnitsWidget(final QWidget parent, final Model model, final IdsGetter idsGetter) {
        super(model.getEnvironment(), parent);
        prohressHand = model.getEnvironment().getProgressHandleManager().newStandardProgressHandle();
        this.sourceModel = model;
        this.idsGetter = idsGetter;
        eventLogPanel = new EventLogPanel(this);
        final String settingsKey = SettingNames.SYSTEM + "/" + "system_monitor_unittree_update_metric_period_update_enabled";
        isUpdateEnabled = getEnvironment().getConfigStore().readBoolean(settingsKey, true);
    }

    public EventLogPanel getEventLogPanel() {
        return eventLogPanel;
    }

    public IdsGetter getIdsGetter() {
        return idsGetter;
    }

    public void open() {
        timerMetric = new QTimer(this);
        timerMetric.timeout.connect(this, "metricTimeOut()");

        final Splitter splitter = new Splitter(this, (ExplorerSettings) getEnvironment().getConfigStore());
        splitter.setOrientation(Orientation.Vertical);
        final QVBoxLayout mainlayout = new QVBoxLayout();
        mainlayout.setMargin(0);

        final QVBoxLayout layout = new QVBoxLayout();
        mainlayout.setMargin(0);
        final QWidget w = new QWidget(this);

        try {
            final GroupModel instanceModel = (GroupModel) sourceModel.getChildModel(idsGetter.getExplorerItemId(EExplorerItemName.INSTANCE));
            final GroupModel allUnitsModel = (GroupModel) sourceModel.getChildModel(idsGetter.getExplorerItemId(EExplorerItemName.UNIT));
            final GroupModel channelsModel = (GroupModel) sourceModel.getChildModel(idsGetter.getExplorerItemId(EExplorerItemName.NET_CHANNEL));
            eventLogWithContextModel = (GroupModel) sourceModel.getChildModel(idsGetter.getExplorerItemId(EExplorerItemName.EVENTLOG_WITH_CONTEXT));

            unitsTree = new UnitsTree(this, sourceModel, instanceModel, allUnitsModel, channelsModel);

            eventLogPanel.open(eventLogWithContextModel);

            layout.addWidget(unitsTree.getTree());
            w.setLayout(layout);

            splitter.setRatio(2. / 3.);
            splitter.addWidget(w);
            splitter.addWidget(eventLogPanel.getWidget());
            mainlayout.addWidget(splitter);
            this.setLayout(mainlayout);
            metricTimeOut();
            startUpdatePeriodically();

            final String toolTip = Application.translate("SystemMonitoring", "Show Commands");
            btnShowCommands = createToolBtn(toolTip, "showCommands()", "btnShowCommands", ExplorerIcon.getQIcon(UnitsWidgetIcons.COMMANDS));
            btnShowCommands.setMenu(new ExplorerMenu(this));
        } catch (ServiceClientException ex) {
            sourceModel.showException(ex);
        } catch (InterruptedException ex) {
        }
    }

    public void curItemChanged(final QTreeWidgetItem item) {
        if (item instanceof TreeItem) {
            eventLogPanel.update((TreeItem) item);
            // eventLogPanel.updateContextTab((TreeItem) item) ;
        }
    }

    private void startUpdatePeriodically() {
        if (isUpdateEnabled) {
            final String settingsKey = SettingNames.SYSTEM + "/" + "system_monitor_unittree_update_metric_period";
            final int updMetricPeriod = getEnvironment().getConfigStore().readInteger(settingsKey, 600000);
            timerMetric.setInterval(updMetricPeriod);
            timerMetric.start();

            //settingsKey = SettingNames.SYSTEM + "/" + "system_monitor_unittree_update_eventlog_period";
            //int updEventLogPeriod = getEnvironment().getConfigStore().readInteger(settingsKey, 600000);
            //timerEventLog.setInterval(updEventLogPeriod);
            //timerEventLog.start();
        }
    }

    @SuppressWarnings("unused")
    private void metricTimeOut() {
        timerMetric.stop();
        final List<QTreeWidgetItem> decorateItems = unitsTree.getVisibleItems(unitsTree.getTree().invisibleRootItem());
        unitsTree.setDecorateItems(decorateItems);
        unitsTree.updateByCommand();
    }

    public QTimer getMetricTimer() {
        return timerMetric;
    }

    QTreeWidget getTree() {
        return unitsTree.getTree();
    }

    public void showEventLogPanel(final boolean isVisible) {
        eventLogPanel.setVisible(isVisible);
        final QTreeWidgetItem item = unitsTree.getTree().currentItem();
        if (isVisible && (item != null && item instanceof TreeItem)) {
            eventLogPanel.update((TreeItem) item);
        }
    }

    public void reread() {
        prohressHand.startProgress(Application.translate("SystemMonitoring", "Rereading..."), true);
        try {
            unitsTree.getTree().setUpdatesEnabled(false);
            unitsTree.clear();
            try {
                unitsTree.reread();
            } finally {
                unitsTree.getTree().setUpdatesEnabled(true);
            }
        } catch (InterruptedException ex) {
        }
        prohressHand.finishProgress();

    }

    public void openEventLogDialog() {
        TreeItem item = (TreeItem) unitsTree.getTree().currentItem();
        if ((item != null) /*&& item.getEntityModel() != null*/) {
            if (eventLogWithContextModel.getView() != null) {
                eventLogWithContextModel.getView().close(true);
            }
            eventLogWithContextModel.getProperty(idsGetter.getPropId(UnitsWidget.EPropertyName.PARAM_CONTEXT_ID)).setValueObject(item.getId().toString());
            eventLogWithContextModel.getProperty(idsGetter.getPropId(UnitsWidget.EPropertyName.PARAM_CONTEXT_TYPE)).setValueObject(item.getContextType().getValue());

            final SelectEntityDialog dialog = new SelectEntityDialog(eventLogWithContextModel, false);
            com.trolltech.qt.gui.QDialog.DialogCode.resolve(dialog.exec()).equals(com.trolltech.qt.gui.QDialog.DialogCode.Accepted);
        }
    }

    public void onClosed() {
        timerMetric.stop();
        eventLogWithContextModel.clean();
    }

    public boolean isUpdateEnabled() {
        return isUpdateEnabled;
    }

    public void setIsUpdateEnabled(final boolean isUpdate) {
        this.isUpdateEnabled = isUpdate;
        final ClientSettings settings = getEnvironment().getConfigStore();
        final String settingsKey = SettingNames.SYSTEM + "/" + "system_monitor_unittree_update_metric_period_update_enabled";
        settings.writeBoolean(settingsKey, isUpdate);
    }

    public UnitsTree getUnitsTree() {
        return unitsTree;
    }

    public QToolButton getCommandBtn() {
        return btnShowCommands;
    }

    private QToolButton createToolBtn(final String toolTip, final String connect, final String objName, final QIcon icon) {
        final QToolButton btn = new QToolButton();
        btn.setObjectName(objName);
        btn.setAutoRaise(true);
        btn.setToolTip(toolTip);
        btn.setIcon(icon);
        btn.clicked.connect(this, connect);
        return btn;
    }

    @SuppressWarnings("unused")
    private void showCommands() {
        final QTreeWidgetItem item = getTree().currentItem();
        if (item != null && item instanceof TreeItem) {
            final EntityModel entity = ((TreeItem) item).getEntityModel();
            if (entity == null) {
                return;
            }
            final List<Id> commandIds = entity.getAccessibleCommandIds();
            final ExplorerMenu menu = (ExplorerMenu) btnShowCommands.menu();
            menu.clear();
            if (!commandIds.isEmpty()) {
                for (Id cmdId : commandIds) {
                    final Command command = entity.getCommand(cmdId);
                    if (command.isVisible()
                            && (!(command.getDefinition() instanceof RadPresentationCommandDef)
                            || (((RadPresentationCommandDef) command.getDefinition()).scope != ECommandScope.PROPERTY))) {
                        final ExplorerAction act = new ExplorerAction(command.getIcon(), command.getTitle(), this);
                        act.setEnabled(command.isEnabled());
                        act.addActionListener(new ExecCommandListener(command));
                        menu.addAction((Action) act);
                    }
                }
                if (menu.isEmpty()) {
                    final String str = Application.translate("SystemMonitoring", "<no accessible commands>");
                    final QAction act = new QAction(str, this);
                    act.setEnabled(false);
                    menu.addAction(act);
                }
                btnShowCommands.showMenu();
            }
        }
    }

    private class ExecCommandListener implements Action.ActionListener {

        private final Command command;

        ExecCommandListener(final Command command) {
            this.command = command;
        }

        @Override
        public void triggered(final Action action) {
            command.execute();
        }
    };
}
