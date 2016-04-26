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

import com.trolltech.qt.QNoNativeResourcesException;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.gui.QFrame.Shape;
import com.trolltech.qt.gui.QLayout.SizeConstraint;
import com.trolltech.qt.gui.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.eas.IResponseListener;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.*;
import org.radixware.kernel.explorer.editors.profiling.TabWidget;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;
import org.radixware.schemas.monitoringSettings.MonitoringSettingsDocument.MonitoringSettings;
import org.radixware.schemas.monitoringSettings.*;
import org.radixware.schemas.xscml.Sqml;


public class MetricHistWidget extends ExplorerWidget {

    public static final QColor defaultErrorColor = new QColor(255, 170, 170);
    public static final QColor defaultWarningColor = new QColor(255, 204, 170);
    public static final QColor defaultNornalColor = QColor.blue;
    private static final String MONITORING_SETTING_NAME = "system_monitor_productivity_opened_metrics";
    private TabWidget tabWidget;
    private final MetricHistToolBar toolBar;
    private final FirstHintWidget emptyWidget;
    private GroupModel metricsGroup = null;
    private int clickedTab;
    private final List<MetricPageWidget> pages = new LinkedList<>();
    private final IdsGetter idsGetter;
    private QTimer timerUpdateMetric;
    private final Model sourceGroup;
    private boolean isUpdate;

    public static final class MetricHistIcons extends ExplorerIcon.CommonOperations {

        private MetricHistIcons(final String fileName) {
            super(fileName, true);
        }
        public static final MetricHistIcons STARTED = new MetricHistIcons("classpath:images/run.svg");
        public static final MetricHistIcons STOPPED = new MetricHistIcons("classpath:images/stop.svg");
        public static final MetricHistIcons NOT_USED = new MetricHistIcons("classpath:images/cancel.svg");
        public static final MetricHistIcons INSTANCE_NOT_STARTED = new MetricHistIcons("classpath:images/exeptionClass.svg");
        public static final MetricHistIcons HANG = new MetricHistIcons("classpath:images/percent.svg");
        public static final MetricHistIcons VALUE_SCALE = new MetricHistIcons("classpath:images/edit.svg");
        public static final MetricHistIcons TIME_SCALE = new MetricHistIcons("classpath:images/time_scale.svg");
        public static final MetricHistIcons ADD_TAB = new MetricHistIcons("classpath:images/addEmpty.svg");
        public static final MetricHistIcons IMG_EXPORT = new MetricHistIcons("classpath:images/export_r.svg");
        public static final MetricHistIcons IMG_IMPORT = new MetricHistIcons("classpath:images/import_r.svg");
    }

    public static enum EDiagramType {

        STATISTIC, DOT, STEP, CORRELATION
    }

    public static enum EPropertyName {

        METRIC_STATE_ID, METRIC_TYPE_ID, METRIC_STATE_SENSOR_TITLE, METRIC_KIND,
        METRIC_VIEW_STATE_ID, METRIC_VIEW_AVG_VAL, METRIC_VIEW_END_VAL,
        //metrictype
        METRIC_VIEW_TYPE_ID, METRIC_VIEW_TYPE_TITLE, METRIC_VIEW_KIND,
        AVG_VAL, MAX_VAL, MIN_VAL,
        END_TIME, BEG_TIME, END_VAL, BEG_VAL, SINCE,
        LOW_WARN_VAL, LOW_ERROR_VAL, HIGH_WARN_VAL, HIGH_ERROR_VAL,
        METRIC_TYPE_INSTANCE_ID, METRIC_TYPE_NETCHENNEL_ID, METRIC_TYPE_UNIT_ID,
        METRIC_TYPE_SYSTEM_ID, METRIC_TYPE_SERVICE_URI, METRIC_TYPE_TIMING_SECTION
    }

    public interface IdsGetter {

        public Id getMetricTypeChildId();

        public Id getMetricStateChildId();

        public Id getMetricHistChildId();

        public Id getStateViewChildId();

        public Id getPropIdByName(EPropertyName propName);

        public Id getMetricEventDomainId();

        public Id getMetricStatisticDomainId();

        public Id getMetricPointDomainId();

        public Id getMetricKindEnumId();
    }

    public MetricHistWidget(final QWidget parent, final IdsGetter idsGetter, final Model sourceGroup) {
        super(sourceGroup.getEnvironment(), parent);
        this.sourceGroup = sourceGroup;
        this.idsGetter = idsGetter;
        emptyWidget = new FirstHintWidget(this);

        String settingsKey = SettingNames.SYSTEM + "/" + "system_monitor_productivity_statictic_period_update_enabled";
        isUpdate = getEnvironment().getConfigStore().readBoolean(settingsKey, true);

        QVBoxLayout mainlayout = new QVBoxLayout();
        mainlayout.setMargin(0);
        mainlayout.setWidgetSpacing(0);

        toolBar = new MetricHistToolBar(this);
        MetricPageWidget scrollArea = createScrollArea(this, true);
        pages.add(scrollArea);

        mainlayout.addWidget(toolBar.getToolBar(), 0, AlignmentFlag.AlignTop);
        mainlayout.addWidget(scrollArea);
        this.setLayout(mainlayout);

        tabWidget = new TabWidget(this, new MyTabBar(), getEnvironment(), true);
        tabWidget.setObjectName("MonitoringTabWidget");
        tabWidget.onCloseTab.connect(this, "removePage(Integer)");
        tabWidget.onLastTabRemain.connect(this, "hideTabs()");
        tabWidget.currentChanged.connect(this, "currentTabChanged()");
        tabWidget.setVisible(false);
        this.setAcceptDrops(true);
    }

    public void updateView(final AbstaractMetricView metricView, final AbstractMetricSettings newMetricSettings) {
        if (metricView != null) {
            metricView.update(newMetricSettings);
        }
        saveDiagramsToCfg();
    }

    private MetricPageWidget createScrollArea(final QWidget parent, final boolean isFrameVisible) {
        MetricPageWidget area = new MetricPageWidget(parent);
        if (!isFrameVisible) {
            area.setFrameShape(Shape.NoFrame);
        }
        area.setWindowTitle("QScrollArea");
        area.setWidgetResizable(true);

        QWidget widget = new PageWidget(this, area);
        widget.setMinimumSize(100, 100);
        GridLayout layout = new GridLayout(widget);
        layout.setSizeConstraint(SizeConstraint.SetMinimumSize);
        Alignment alignment = new Alignment(AlignmentFlag.AlignTop.value() | AlignmentFlag.AlignLeft.value());
        layout.setAlignment(alignment);
        widget.setLayout(layout);

        area.setWidget(widget);
        area.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded);
        area.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded);

        return area;
    }

    public AbstaractMetricView findView(final DiagramPanel diagramPanel) {
        MetricPageWidget curPage = getCurrentPage();
        if (curPage != null) {
            List<AbstaractMetricView> metricViews = curPage.getMetricViews();
            for (AbstaractMetricView metricView : metricViews) {
                if (metricView.getDiagram() != null && metricView.getDiagram().equals(diagramPanel)) {
                    return metricView;
                }
            }
        }
        return null;
    }

    private MetricPageWidget getCurrentPage() {
        MetricPageWidget res = null;
        if (pages != null && !pages.isEmpty()) {
            if (tabWidget.currentIndex() >= 0) {
                int index = tabWidget.currentIndex();
                if (index < pages.size()) {
                    res = pages.get(index);
                }
            } else {
                res = pages.get(0);
            }
        }
        return res;
    }

    public void open() {
        timerUpdateMetric = new QTimer(this);
        timerUpdateMetric.timeout.connect(this, "metricTimeOut()");
        try {
            metricsGroup = (GroupModel) sourceGroup.getChildModel(/*idsGetter.getMetricTypeChildId()*/idsGetter.getStateViewChildId());
            openDiagram();
            toolBar.setBtnAddEnabled(metricsGroup != null);
            toolBar.setBtnUpdateState(isUpdate);
        } catch (ServiceClientException ex) {
            metricsGroup.showException(ex);
        } catch (InterruptedException ex) {
        }
        startUpdatePeriodically();
    }

    private void openDiagram() {
        boolean hasDiagram = false;
        final ClientSettings settings = getEnvironment().getConfigStore();
        String settingsKey = SettingNames.SYSTEM + "/" + MONITORING_SETTING_NAME;//+"/"+MonitoringSettingNames.PAGE_COUNT;
        String str = settings.readString(settingsKey, null);
        if (str != null && !str.isEmpty()) {
            try {
                MonitoringSettingsDocument xMonitoringSettingsDoc = MonitoringSettingsDocument.Factory.parse(str);
                MonitoringSettings xMonitoringSettings = xMonitoringSettingsDoc.getMonitoringSettings();
                hasDiagram = loadDiagramsFromXml(xMonitoringSettings,false);
            } catch (XmlException ex) {
                Logger.getLogger(MetricHistWidget.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!hasDiagram) {
            MetricPageWidget curPage = getCurrentPage();
            if (curPage != null && curPage.widget() != null) {
                QWidget widget = curPage.widget();
                widget.layout().addWidget(emptyWidget);
            }
        }
    }

    private boolean loadDiagramsFromXml(final MonitoringSettings xMonitoringSettings,boolean merge) {
        boolean hasDiagram = false;
        
        if (xMonitoringSettings.getPageList() != null && xMonitoringSettings.getPageList().size() > 1) {
            if (tabWidget.count() <= 1) {
                  showTabs();
            }
            for (int j = 0; j < xMonitoringSettings.getPageList().size(); j++) {
                MonitoringPage xPage = xMonitoringSettings.getPageList().get(j);
                String pageName = xPage.getName();
                if(pages.get(0).getMetricViews().isEmpty() && (tabWidget.count() <= 1) && j==0){
                    tabWidget.setCurrentIndex(0);                    
                } else{
                    createNewTab(pageName, tabWidget.count());
                }
                fillDiagrams(xPage);
                tabWidget.setCurrentIndex(0);
                if (!hasDiagram && !xPage.getDiagramList().isEmpty()) {
                    hasDiagram = true;
                }
            }
        } else if (!xMonitoringSettings.getPageList().isEmpty()) { 
            MonitoringPage xPage = xMonitoringSettings.getPageList().get(0);            
            if(!merge){               
                if(!pages.get(0).getMetricViews().isEmpty() && (tabWidget.count() <= 1)){
                   showTabs();
                   createNewTab(xPage.getName(), tabWidget.count());
                } else if (tabWidget.count() > 1){
                   createNewTab(xPage.getName(), tabWidget.count());
                }                
            }
            fillDiagrams(xPage);
            if (!hasDiagram && !xPage.getDiagramList().isEmpty()) {
                hasDiagram = true;
            }
        }
        return hasDiagram;
    }

    private void fillDiagrams(final MonitoringPage xPage) {
        if (xPage != null && !xPage.getDiagramList().isEmpty()) {
            List<Long> ids=new ArrayList<>();
            for (AbstaractMetricView mv : getCurrentPage().getMetricViews()) {
                if(mv instanceof MetricView){
                  ids.addAll(mv.getMetricStateId());
                }
            }
            for (int i = 0; i < xPage.getDiagramList().size(); i++) {
                try { 
                    boolean canAdd=true;
                    MonitoringDiagram xDiagram = xPage.getDiagramList().get(i);
                    if(!xDiagram.getIsPie() && !xDiagram.getMetricSettingsList().isEmpty()){
                        if(ids.contains(xDiagram.getMetricSettingsList().get(0).getMetricStateId())){
                            showMsgAlreadyExists();
                            canAdd=false;
                        }
                    }
                    if(canAdd){
                        AbstractMetricSettings metricSettings = createMetricSettings(xDiagram);
                        if (metricSettings != null) {
                            int[] pos = calcPos(xDiagram);
                            if(pos==null){
                                showMsgNoFreeSpace();
                                break;
                            }else{
                                addDiagram(metricSettings, pos);
                            }
                        }
                    }
                } catch (ServiceClientException ex) {
                    metricsGroup.showException(ex);
                } catch (InterruptedException ex) {
                } catch (BrokenEntityObjectException exception) {
                    continue;//ignoring broken metric entity model
                }
            }
        }
    }
    
    private int[] calcPos(final MonitoringDiagram xDiagram){
        int[] pos = null;
        if (xDiagram.isSetPosition()) {
            MonitoringDiagramPosition xPosition = xDiagram.getPosition();
            int column = xPosition.isSetColumn() ? (int) xPosition.getColumn() : -1;
            int row = xPosition.isSetRow() ? (int) xPosition.getRow() : -1;                                                     
            int columnSpan = xPosition.isSetColumnSpan() ? (int) xPosition.getColumnSpan() : 2;
            int rowSpan = xPosition.isSetRowSpan() ? (int) xPosition.getRowSpan() : 2;                            
            pos = new int[]{column, row, columnSpan, rowSpan};                            
        } 
        GridLayout gridLayout=((GridLayout) getCurrentPage().widget().layout());
        if(pos!=null && getCurrentPage()!=null && gridLayout.getItem( pos[0], pos[1])!=null){ 
            int[] newPos=gridLayout.getFreeSpace();
            if(newPos!=null){
                pos[0] = newPos[0];
                pos[1] = newPos[1];
                pos[2] = 1;
                pos[3] = 1;
            }else{
                pos=null;
            }
        }
        return pos;
    }

    private AbstractMetricSettings createMetricSettings(final MonitoringDiagram xDiagram) throws InterruptedException, ServiceClientException, BrokenEntityObjectException {
        AbstractMetricSettings metricSettings = null;
        boolean isCorrelationDiagram = xDiagram.getIsPie();
        if (!isCorrelationDiagram) {
            if (!xDiagram.getMetricSettingsList().isEmpty()) {
                MonitoringMetricSettings settings = xDiagram.getMetricSettingsList().get(0);
                Long strMetricStateId = settings.getMetricStateId();
                String strMetricStatePid = settings.getMetricStatePid();
                boolean isHistogram = false;
                boolean isShowChangeState = true;
                double[] valScale = null;
                if (xDiagram.isSetValueRange()) {
                    MonitoringValueRange xValRange = xDiagram.getValueRange();
                    double valScaleMin = xValRange.getMinValue();
                    double valScaleMax = xValRange.getMaxValue();
                    valScale = new double[]{valScaleMin, valScaleMax};
                }
                if (xDiagram.isSetIsShowChangeRange()) {
                    isShowChangeState = xDiagram.getIsShowChangeRange();
                }
                if (xDiagram.isSetIsHistogram()) {
                    isHistogram = xDiagram.getIsHistogram();
                }

                if (strMetricStatePid != null && strMetricStateId != -1) {
                    Pid pidState = new Pid(metricsGroup.getSelectorPresentationDef().getTableId(), strMetricStatePid);
                    int indexstate = metricsGroup.readToEntity(pidState);
                    EntityModel metricState = metricsGroup.getEntity(indexstate);
                    if (metricState != null) {
                        HistoricalMetricSettings hmetricSettings = new HistoricalMetricSettings(metricState);
                        hmetricSettings.loadFromXml(settings);
                        hmetricSettings.setIsHistogram(isHistogram);
                        hmetricSettings.setIsShowChangeRange(isShowChangeState);
                        hmetricSettings.setAutoValueRange(valScale == null);
                        if (valScale != null) {
                            hmetricSettings.setValueScale(valScale[0], valScale[1]);
                        }
                        if (xDiagram.isSetTimeRange()) {
                            hmetricSettings.setPeriod(xDiagram.getTimeRange().getTimePeriod(), (int) xDiagram.getTimeRange().getPeriodUnit());
                        }
                        metricSettings = hmetricSettings;
                    } else {
                        Application.messageError("Can't find metric with pid=" + strMetricStatePid + "!");
                    }
                }
            }
        } else {
            metricSettings = new CorrelationMetricSettings();
            metricSettings.setTitle(xDiagram.getName());
            ((CorrelationMetricSettings)metricSettings).setPercent(xDiagram.getIsPercent());
            ((CorrelationMetricSettings)metricSettings).setShowValues(xDiagram.getIsShowVals());
            if (!xDiagram.getMetricSettingsList().isEmpty()) {
                for (MonitoringMetricSettings settings : xDiagram.getMetricSettingsList()) {
                    //Long strMetricStateId = settings.getMetricStateId();                    
                    String strMetricStatePid = settings.getMetricStatePid();
                    if (strMetricStatePid != null) {
                        Pid pidState = new Pid(metricsGroup.getSelectorPresentationDef().getTableId(), strMetricStatePid);
                        int indexstate = metricsGroup.readToEntity(pidState);
                        EntityModel metricState = metricsGroup.getEntity(indexstate);
                        if (metricState != null) {
                            MetricSettings ms = new MetricSettings(metricState);
                            ms.loadFromXml(settings);
                            ((CorrelationMetricSettings) metricSettings).getMetricSettingsList().add(ms);
                        } else {
                            Application.messageError("Can't find metric with pid=" + strMetricStatePid + "!");
                        }
                    }
                }
            }

        }
        return metricSettings;
    }

    private void startUpdatePeriodically() {
        if (isUpdate) {
            String settingsKey = SettingNames.SYSTEM + "/" + "system_monitor_productivity_update_metric_period";
            int updMetricPeriod = getEnvironment().getConfigStore().readInteger(settingsKey, 60000);
            timerUpdateMetric.start(updMetricPeriod);
        }
    }

    @SuppressWarnings("unused")
    private void metricTimeOut() {
        try {
            timerUpdateMetric.stop();
            List<MetricPageWidget> oldPages = new LinkedList<>();
            oldPages.addAll(pages);
            List<Long> ids = new ArrayList<>();
            for (int j = 0; j < oldPages.size(); j++) {
                MetricPageWidget curPage = oldPages.get(j);
                if (curPage != null) {
                    QWidget panel = curPage.widget();
                    if (panel != null && curPage.updatesEnabled() && curPage.getMetricViews().size() > 0) {
                        for (AbstaractMetricView mv : curPage.getMetricViews()) {
                            ids.addAll(mv.getMetricStateId());
                        }
                    }
                }
            }
            if (!ids.isEmpty()) {
                Sqml sqml = createCondition(ids);
                metricsGroup.reset();
                metricsGroup.removeCondition();
                metricsGroup.setCondition(sqml);                
                metricsGroup.readMoreAsync().addListener(new MetricStateResponseListener(oldPages));
            } else {
                timerUpdateMetric.start();
            }
        } catch (ServiceClientException ex) {
            Logger.getLogger(MetricHistWidget.class.getName()).log(Level.SEVERE, null, ex);
            timerUpdateMetric.start();
        } catch (InterruptedException ex) {
            timerUpdateMetric.start();
        }
    }

    private Sqml createCondition(final List<Long> ids) {
        Id propId = idsGetter.getPropIdByName(EPropertyName.METRIC_VIEW_STATE_ID);
        Id tableId = metricsGroup.getSelectorPresentationDef().getTableId();
        Sqml xSqml = Sqml.Factory.newInstance();
        Sqml.Item.PropSqlName propName = xSqml.addNewItem().addNewPropSqlName();
        propName.setOwner(Sqml.Item.PropSqlName.Owner.THIS);
        propName.setPropId(propId);
        propName.setTableId(tableId);
        xSqml.addNewItem().setSql(" in( ");
        for (int i = 0, size = ids.size(); i < size; i++) {
            Sqml.Item.TypifiedValue typifiedValue = xSqml.addNewItem().addNewTypifiedValue();
            typifiedValue.setPropId(propId);
            typifiedValue.setTableId(tableId);
            typifiedValue.setValue(ids.get(i).toString());
            if (i < size - 1) {
                xSqml.addNewItem().setSql(", ");
            }
        }
        xSqml.addNewItem().setSql(" ) ");
        return xSqml;
    }

    private void updateMetricViews(final List<MetricPageWidget> oldPages) {
        for (int j = 0; j < oldPages.size(); j++) {
            List<AbstaractMetricView> metricViewsOld = new LinkedList<>();
            MetricPageWidget curPage = oldPages.get(j);
            if (curPage != null) {
                QWidget panel = curPage.widget();
                if (panel != null && curPage.updatesEnabled() && curPage.getMetricViews().size() > 0) {
                    metricViewsOld.addAll(curPage.getMetricViews());
                    if (metricViewsOld.size() > 0) {
                        curPage.setUpdatesEnabled(false);
                    }
                    for (int i = 0; i < metricViewsOld.size(); i++) {
                        AbstaractMetricView metricView = metricViewsOld.get(i);
                        metricView.updateAsinc(metricsGroup, i == (metricViewsOld.size() - 1));
                    }
                }
            }
        }
    }
    
    private void showMsgNoFreeSpace(){
        String title = Application.translate("SystemMonitoring", "Can't add diagram");
        String msg = Application.translate("SystemMonitoring", "There is no space for adding!");
        Application.messageInformation(title, msg);
    }
    
    private void showMsgAlreadyExists(){
        String title = Application.translate("SystemMonitoring", "Can't add diagram");
        String msg = Application.translate("SystemMonitoring", "Diagramm already exists on this page!");
        Application.messageInformation(title, msg);
    }

    public void addDiagram() {
//        MetricPageWidget curPage = getCurrentPage();
//        if (curPage != null && curPage.widget() != null && curPage.widget().layout() != null) {
//            int[] pos = ((GridLayout) curPage.widget().layout()).getFreeSpace();
//            if (pos == null) {
//                showMsgNoFreeSpace();
//            } else {
//                DiagramWizard choceObj = new DiagramWizard(this);
//                if (choceObj.exec() == 1 && choceObj.getMetricSettings() != null) {
//                    addDiagram(choceObj.getMetricSettings(), null);
//                }
//                saveDiagramsToCfg();
//            }
//        }
    }

    public MetricView createMetricView(final MetricSettings metricSettings) {
        MetricPageWidget curPage = getCurrentPage();
        return new MetricView(metricSettings, this, curPage, metricsGroup.getEnvironment(), idsGetter, curPage.getMetricViews().size());
    }

    public EntityModel getMetricViewByMetricState(final EntityModel metricModels, final boolean interactive) {
        EntityModel metricView = null;
        try {
            Long stateId = (Long) metricModels.getProperty(idsGetter.getPropIdByName(EPropertyName.METRIC_STATE_ID)).getValueObject();
            List<Long> stateIds = new ArrayList<>();
            stateIds.add(stateId);
            Sqml sqml = createCondition(stateIds);
            metricsGroup.reset();
            metricsGroup.removeCondition();
            metricsGroup.setCondition(sqml);
            metricsGroup.reread();
            metricView = metricsGroup.getEntity(0);
        } catch (BrokenEntityObjectException | ServiceClientException ex) {
            if (interactive) {
                getEnvironment().processException(ex);
            } else {
                Logger.getLogger(MetricHistWidget.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (InterruptedException ex) {
        }
        return metricView;
    }

    public EntityModel chooseMetricState(final List<Long> ids) {
        EntityModel metricModels = null;
//        try {
//            if (ids == null) {
//                ids = new ArrayList<>();
//                for (int j = 0; j < pages.size(); j++) {
//                    MetricPageWidget page = pages.get(j);
//                    if (page != null) {
//                        for (int i = 0; i < page.getMetricViews().size(); i++) {
//                            AbstaractMetricView metricView = page.getMetricViews().get(i);
//                            if (metricView instanceof MetricView) {
//                                ids.addAll(page.getMetricViews().get(i).getMetricStateId());
//                            }
//                        }
//                    }
//                }
//            }
//            GroupModel metricType = (GroupModel) sourceGroup.getChildModel(idsGetter.getMetricTypeChildId());
//            ChooseMetricWizard chooseInst = new ChooseMetricWizard(this, metricType, ids);
//            if (chooseInst.exec() == QDialog.DialogCode.Accepted.value()) {
//                metricModels = chooseInst.getSelectedMetricState();
//            }
//
//        } catch (ServiceClientException ex) {
//            Logger.getLogger(MetricHistWidget.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InterruptedException ex) {
//        }
        return metricModels;
    }

    private AbstaractMetricView addDiagram(final AbstractMetricSettings metricSettings,final int[] pos) {
        MetricPageWidget curPage = getCurrentPage();
        if (curPage != null) {
            AbstaractMetricView metricView = null;
            int index = curPage.getMetricViews().size();
            if (metricSettings instanceof CorrelationMetricSettings) {
                metricView = new CorrelationMetricView(metricSettings, this, curPage, metricsGroup.getEnvironment(), idsGetter, index);
            } else if (metricSettings instanceof HistoricalMetricSettings) {
                metricView = new MetricView(metricSettings, this, curPage, metricsGroup.getEnvironment(), idsGetter, index);
            }

            if (metricView != null) {
                QWidget scrollWidget = curPage.widget();
                if (emptyWidget.parent() != null) {
                    scrollWidget.layout().removeWidget(emptyWidget);
                    emptyWidget.setParent(null);
                }

                metricView.getDiagram().setParent(scrollWidget);
                metricView.update(/*begTime, endTime,*/metricView.getMetricSettings());
                curPage.getMetricViews().add(metricView);
                GridLayoutItem item = new GridLayoutItem(metricView.getDiagram());                
                item.setPositions(pos);
                ((GridLayout) scrollWidget.layout()).addItem(item);
                metricView.getDiagram().setSelected(true);
            }

            return metricView;
        }
        return null;
    }

    void saveDiagramsToCfg() {
        final ClientSettings settings = getEnvironment().getConfigStore();
        String settingsKey = SettingNames.SYSTEM + "/" + MONITORING_SETTING_NAME;
        MonitoringSettingsDocument xMonitoringSettingsDoc = MonitoringSettingsDocument.Factory.newInstance();
        appendTo(xMonitoringSettingsDoc);
        settings.writeString(settingsKey, xMonitoringSettingsDoc.toString());
    }

    private void appendTo(final MonitoringSettingsDocument xMonitoringSettingsDoc) {
        MonitoringSettings xMonitoringSettings = xMonitoringSettingsDoc.addNewMonitoringSettings();
        for (int j = 0; j < pages.size(); j++) {
            appendTo(xMonitoringSettings, j);
        }
    }

    private void appendTo(final MonitoringSettings xMonitoringSettings, final int pageIndex) {
        MonitoringPage xPage = xMonitoringSettings.addNewPage();
        MetricPageWidget curPage = pages.get(pageIndex);
        if (curPage != null && curPage.widget() != null) {
            if (pages.size() > 1) {
                xPage.setName(tabWidget.tabText(pageIndex));
            }
            for (int i = 0; i < curPage.getMetricViews().size(); i++) {
                MonitoringDiagram xMetricDiagram = xPage.addNewDiagram();
                AbstaractMetricView metricView = curPage.getMetricViews().get(i);
                GridLayoutItem item = ((GridLayout) curPage.widget().layout()).getItem(metricView.getDiagram());
                if (item != null) {
                    item.saveToXml(xMetricDiagram.addNewPosition());
                    metricView.appendTo(xMetricDiagram);
                }
            }
        }
    }

    public void saveTofile(final String filename, final boolean saveCurrentPage) {
        if (filename == null || filename.isEmpty()) {
            return;
        }
        MonitoringSettingsDocument xMonitoringSettingsDoc = MonitoringSettingsDocument.Factory.newInstance();
        if (saveCurrentPage) {
            int index = tabWidget.currentIndex() < 0 ? 0 : tabWidget.currentIndex();
            if (index < pages.size()) {
                MonitoringSettings xMonitoringSettings = xMonitoringSettingsDoc.addNewMonitoringSettings();
                appendTo(xMonitoringSettings, index);
            }
        } else {
            appendTo(xMonitoringSettingsDoc);
        }
        try {
            StringWriter w = new StringWriter();
            xMonitoringSettingsDoc.save(w);
            String string = w.toString();
            final Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
            out.write(string);
            out.close();
        } catch (FileNotFoundException e) {
            String title = Application.translate("ExplorerDialog", "Error!");
            String message = Application.translate("ExplorerDialog", "File \'%s\' not found");
            Application.messageException(title, String.format(message, filename), e);
            Logger.getLogger(JmlEditor.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException ex) {
            String title = Application.translate("ExplorerDialog", "Error!");
            String message = Application.translate("ExplorerDialog", "IOException");
            Application.messageException(title, String.format(message, filename), ex);
            Logger.getLogger(JmlEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void readFromFile(final String filename) {
        if (filename == null || filename.isEmpty()) {
            return;
        }
        String res = getTextFromFile(filename);
        MonitoringSettings xMonitoringSettings = getMonitoringSettingsType(res);
        //clear();
        rereadMetrics(xMonitoringSettings);
        boolean merge=false;
        if(xMonitoringSettings.getPageList().size()==1 && (tabWidget.count()>1 || !pages.get(0).getMetricViews().isEmpty())){
            String text = Application.translate("SystemMonitoring", "Import on current page?");
            merge=Application.messageConfirmation(text);
        }
        loadDiagramsFromXml(xMonitoringSettings,merge);
        saveDiagramsToCfg();
    }

    private void rereadMetrics(final MonitoringSettings xMonitoringSettings) {
        List<Long> ids = new ArrayList<>();
        if (xMonitoringSettings != null && !xMonitoringSettings.getPageList().isEmpty()) {
            for (MonitoringPage xPage : xMonitoringSettings.getPageList()) {
                if (!xPage.getDiagramList().isEmpty()) {
                    for (MonitoringDiagram xDiagram : xPage.getDiagramList()) {
                        if (!xDiagram.getMetricSettingsList().isEmpty()) {
                            for (MonitoringMetricSettings xSettings : xDiagram.getMetricSettingsList()) {
                                ids.add(xSettings.getMetricStateId());
                            }
                        }
                    }
                }
            }
        }
        try {
            Sqml sqml = createCondition(ids);
            metricsGroup.reset();
            metricsGroup.removeCondition();
            metricsGroup.setCondition(sqml);
            metricsGroup.reread();
        } catch (ServiceClientException ex) {
            metricsGroup.showException(ex);
        } catch (InterruptedException ex) {
        }
    }

    private void clear() {
        hideTabs();
        if (pages.get(0) != null && !pages.get(0).metricViews.isEmpty()) {
            List<AbstaractMetricView> metricViews = pages.get(0).metricViews;
            for (int i = metricViews.size() - 1; i >= 0; i--) {
                metricViews.get(i).remove();

            }
        }
    }

    private String getTextFromFile(final String filename) {
        String res = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                res += str + "\n";
            }
            in.close();
        } catch (FileNotFoundException e) {
            String title = Application.translate("ExplorerDialog", "Error!");
            String message = Application.translate("ExplorerDialog", "File \'%s\' not found");
            Application.messageException(title, String.format(message, filename), e);
            Logger.getLogger(JmlEditor.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException ex) {
            String title = Application.translate("ExplorerDialog", "Error!");
            String message = Application.translate("ExplorerDialog", "IOException");
            Application.messageException(title, String.format(message, filename), ex);
            Logger.getLogger(JmlEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    private MonitoringSettings getMonitoringSettingsType(String str) {
        MonitoringSettings monitoringSettings = null;
        try {
            MonitoringSettingsDocument xMonitoringSettingsDoc = MonitoringSettingsDocument.Factory.parse(str);
            monitoringSettings = xMonitoringSettingsDoc.getMonitoringSettings();//JmlType.Factory.parse(str);
        } catch (XmlException e) {
            try {
                MonitoringPage xPage = MonitoringPage.Factory.parse(str);
                MonitoringSettingsDocument xMonitoringSettingsDoc = MonitoringSettingsDocument.Factory.newInstance();
                monitoringSettings = xMonitoringSettingsDoc.addNewMonitoringSettings();
                monitoringSettings.setPageArray(0, xPage);
            } catch (XmlException ex) {
            }
        }
        if (monitoringSettings == null) {
            monitoringSettings = MonitoringSettings.Factory.newInstance();
        }
        return monitoringSettings;
    }

    public QTimer getMetricTimer() {
        return timerUpdateMetric;
    }

    public IdsGetter getIdsGetter() {
        return idsGetter;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(final boolean isUpdate) {
        this.isUpdate = isUpdate;
        final ClientSettings settings = getEnvironment().getConfigStore();
        String settingsKey = SettingNames.SYSTEM + "/" + "system_monitor_productivity_statictic_period_update_enabled";
        settings.writeBoolean(settingsKey, isUpdate);
    }

    public void removePage(final Integer index) {
        if (index < pages.size() && index >= 0) {
            pages.remove(index.intValue());
            saveDiagramsToCfg();
        }
    }

    @SuppressWarnings("unused")
    private void editTabName() {
        if (clickedTab >= 0 && clickedTab < tabWidget.count()) {
            TabAddingDialog dialog = new TabAddingDialog(metricsGroup.getEnvironment(), this, tabWidget.tabText(clickedTab), Application.translate("SystemMonitoring", "Edit tab name"));
            if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
                tabWidget.setTabText(clickedTab, dialog.getTabName());
                saveDiagramsToCfg();
            }
        }
    }

    public void addTab() {
        TabAddingDialog dialog = new TabAddingDialog(metricsGroup.getEnvironment(), this, "", Application.translate("SystemMonitoring", "Create New Tab"));
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            if (tabWidget.count() <= 1) {
                showTabs();
            }
            createNewTab(dialog.getTabName(), tabWidget.count());
            saveDiagramsToCfg();
        }
    }

    public void insertTab() {
        TabAddingDialog dialog = new TabAddingDialog(metricsGroup.getEnvironment(), this, "", Application.translate("SystemMonitoring", "Create New Tab"));
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            if (tabWidget.count() <= 1) {
                showTabs();
            }
            createNewTab(dialog.getTabName(), clickedTab + 1);
            saveDiagramsToCfg();
        }
    }

    private void createNewTab(String tabName, int index) {
        MetricPageWidget area = createScrollArea(this, false);
        tabWidget.insertTab(index, area, tabName);
        tabWidget.setCurrentIndex(index);
        pages.add(index, area);
    }

    @SuppressWarnings("unused")
    private void closeOtherTabs() {
        String title = Application.translate("SystemMonitoring", "Confirm to Close Tab");
        String msg = Application.translate("SystemMonitoring", "Do you really want to close tabs?");
        if (getEnvironment().messageConfirmation(title, msg)) {
            for (int i = tabWidget.count() - 1; i >= 0; i--) {
                if (i != clickedTab) {
                    tabWidget.closeTab(i);
                }
            }
        }
    }
    
    private void showTabs() {
        if (!pages.isEmpty()) {
            tabWidget = new TabWidget(this, new MyTabBar(), getEnvironment(), true);
            tabWidget.onCloseTab.connect(this, "removePage(Integer)");
            tabWidget.onLastTabRemain.connect(this, "hideTabs()");
            tabWidget.currentChanged.connect(this, "currentTabChanged()");;
            MetricPageWidget scrollArea = pages.get(0);
            this.layout().removeWidget(scrollArea);
            scrollArea.setParent(null);      
            tabWidget.addTab(scrollArea, "General" /*Application.translate("SystemMonitoring", "General")*/);                
            this.layout().addWidget(tabWidget);
            tabWidget.setVisible(true);
            scrollArea.setFrameShape(Shape.NoFrame);            
        }
    }
    
    @SuppressWarnings("unused")
    private void hideTabs() {
        if (!pages.isEmpty()) {
            MetricPageWidget scrollArea = pages.get(0);
            for (int i = tabWidget.count() - 1; i >= 0; i--) {
                tabWidget.closeTab(i);
            }
            this.layout().removeWidget(tabWidget);
            tabWidget.setParent(null);

            scrollArea.setFrameShape(Shape.StyledPanel);
            this.layout().addWidget(scrollArea);
            scrollArea.setVisible(true);
        }
    }

    @SuppressWarnings("unused")
    private void currentTabChanged() {
        MetricPageWidget page = getCurrentPage();
        if (page != null && page.widget() != null) {
            page.widget().repaint();
            page.widget().update();
            page.repaint();
            page.update();
            page.setVisible(true);
        }
    }

    public void onClosed() {
        timerUpdateMetric.stop();
    }

    public void clearSelections() {
        MetricPageWidget page = getCurrentPage();
        if (page != null) {
            for (AbstaractMetricView mv : page.getMetricViews()) {
                mv.getDiagram().setSelected(false);
            }
        }
    }

    public class MetricPageWidget extends QScrollArea {

        private final List<AbstaractMetricView> metricViews = new LinkedList<>();

        MetricPageWidget(final QWidget parent) {
            super(parent);
        }

        List<AbstaractMetricView> getMetricViews() {
            return metricViews;
        }
    }

    class MyTabBar extends QTabBar {

        @Override
        protected void mousePressEvent(QMouseEvent qme) {
            super.mousePressEvent(qme);
            if (qme.button() == MouseButton.RightButton) {
                QMenu menu = new QMenu();
                clickedTab = this.tabAt(qme.pos());
                QAction actEditTabName = new QAction(Application.translate("SystemMonitoring", "Edit tab name"), this);
                actEditTabName.triggered.connect(MetricHistWidget.this, "editTabName()");
                QAction actInsertTab = new QAction(Application.translate("SystemMonitoring", "Insert tab"), this);
                actInsertTab.triggered.connect(MetricHistWidget.this, "insertTab()");
                QAction actCloseOtherTabs = new QAction(Application.translate("SystemMonitoring", "Close other tabs"), this);
                actCloseOtherTabs.triggered.connect(MetricHistWidget.this, "closeOtherTabs()");
                menu.addAction(actEditTabName);
                menu.addAction(actInsertTab);
                menu.addAction(actCloseOtherTabs);
                menu.exec(qme.globalPos());
            }
        }
    }

    public class MetricStateResponseListener implements IResponseListener {

        List<MetricPageWidget> oldPages;

        MetricStateResponseListener(final List<MetricPageWidget> oldPages) {
            this.oldPages = oldPages;
        }

        @Override
        public void registerRequestHandle(RequestHandle handle) {
        }

        @Override
        public void unregisterRequestHandle(RequestHandle handle) {
        }

        @Override
        public void onResponseReceived(XmlObject response, RequestHandle handle) {
            if (metricsGroup.hasMoreRows()) {
                metricsGroup.readMoreAsync().addListener(new MetricStateResponseListener(oldPages));
            } else {
                try {
                    updateMetricViews(oldPages);
                    timerUpdateMetric.start();
                } catch (QNoNativeResourcesException ex) {
                }
            }
        }

        @Override
        public void onServiceClientException(ServiceClientException exception, RequestHandle handle) {
            timerUpdateMetric.start();
        }

        @Override
        public void onRequestCancelled(XmlObject request, RequestHandle handler) {
            timerUpdateMetric.start();
        }
    }
}