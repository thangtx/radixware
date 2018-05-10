
/* Radix::SystemMonitor::DashContainerExplorerView - Desktop Executable*/

/*Radix::SystemMonitor::DashContainerExplorerView-Desktop Dynamic Class*/

package org.radixware.ads.SystemMonitor.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView")
public class DashContainerExplorerView  extends org.radixware.ads.SystemMonitor.explorer.AbsractDashWidgetExplorerView  {

	public static class NewChildWizard extends org.radixware.kernel.explorer.editors.jmleditor.dialogs.BaseWizard {

	    private final HistoricalDiagramPage diagramEditorPage;
	    private final TableSettingsPage tableSettingsPage;
	    private DashChildType childType = DashChildType:Diagram;
	    private boolean isHistorical = true;
	    private String tableTitle = "";
	    private boolean isShowDate = false;
	    private MetricKind metricKind;
	    private Dashboard dashboard;

	    public static enum ChildWizardPages {

	        CHILD_TYPE_PAGE, TABLE_SETTINGS_PAGE, DIAGRAM_SETTINGS_PAGE
	    };

	    public NewChildWizard(Dashboard dashboard, org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.MonitoringConfigHelper helper) {
	        super(helper.getAdsBridge().getDashboardView(),
	                (Explorer.Env::ExplorerSettings) helper.getAdsBridge().getDashboardView().getEnvironment().getConfigStore(),
	                "DiagramWizard");
	        this.dashboard = dashboard;
	        diagramEditorPage = new HistoricalDiagramPage(helper);
	        tableSettingsPage = new TableSettingsPage(helper.getAdsBridge().getDashboardView());
	        setPage(ChildWizardPages.CHILD_TYPE_PAGE.ordinal(), new ChildTypePage(this));
	        setPage(ChildWizardPages.TABLE_SETTINGS_PAGE.ordinal(), tableSettingsPage);
	        setPage(ChildWizardPages.DIAGRAM_SETTINGS_PAGE.ordinal(), diagramEditorPage);
	        setWindowTitle("Create Widget");
	        finished.connect(this, "storeSettings()");
	    }

	    public String getTableTitle() {
	        return tableTitle;
	    }

	    public MetricKind getTableMetricKind() {
	        return metricKind;
	    }

	    public boolean isTableShowDate() {
	        return isShowDate;
	    }

	    public DashChildType getChildType() {
	        return childType;
	    }

	    @SuppressWarnings("unused")
	    private void storeSettings() {
	        if (childType.equals(DashChildType:Table)) {
	            tableTitle = tableSettingsPage.getTableTitle();
	            metricKind = tableSettingsPage.getMetricKind();
	            isShowDate = tableSettingsPage.isShowDate();
	        } else if (childType.equals(DashChildType:Diagram) && diagramEditorPage.isComplete()) {
	             dashboard.fillDialogSettings(getMetricSettings());
	        }
	    }

	    @Override
	    public int nextId() {
	        if (currentId() == ChildWizardPages.CHILD_TYPE_PAGE.ordinal()) {
	            switch (childType) {
	                case DashChildType:Table:
	                    return ChildWizardPages.TABLE_SETTINGS_PAGE.ordinal();
	                case DashChildType:Diagram:
	                    return ChildWizardPages.DIAGRAM_SETTINGS_PAGE.ordinal();
	                case DashChildType:SysUnits:
	                    return -1;
	                case DashChildType:EventLog:
	                    return -1;
	                default:
	                    return -1;
	            }
	        }
	        return -1;
	    }

	    @Override
	    public void done(int result) {
	        if (childType.equals(DashChildType:Diagram) && isHistorical) {
	            if (result == 1) { //click Finish button in dialog page
	                if (!diagramEditorPage.isDateIntervalCorrect()) {
	                    dashboard.getEnvironment().messageError("Error in date interval", "Date from must be before Date to.");
	                    return;
	                }
	            }
	        }
	        super.done(result);
	    }

	    public org.radixware.kernel.common.client.dashboard.DiagramSettings getMetricSettings() {
	        return diagramEditorPage.getMetricSettings();
	    }

	    private class ChildTypePage extends com.trolltech.qt.gui.QWizardPage {

	        public ChildTypePage(Explorer.Qt.Types::QWidget parent) {
	            setParent(parent);
	            com.trolltech.qt.gui.QVBoxLayout layout = new com.trolltech.qt.gui.QVBoxLayout();
	            this.setObjectName("ChooseChildTypePage");
	            this.setTitle("Select Widget Type");
	            com.trolltech.qt.gui.QGroupBox groupBox = new com.trolltech.qt.gui.QGroupBox("");
	            groupBox.setObjectName("groupBox");

	            RadioButton rbHistoricalValue = new RadioButton("Metric Value History");
	            rbHistoricalValue.setObjectName("rbHistoricalValue");
	            rbHistoricalValue.clicked.connect(this, "historicalValueSelected()");

	            RadioButton rbCorrelation = new RadioButton("Proportion of Metric Current Values");
	            rbCorrelation.setObjectName("rbCorrelation");
	            rbCorrelation.clicked.connect(this, "correlationSelected()");

	            RadioButton rbTableValue = new RadioButton("Table of Metric Current Values");
	            rbTableValue.setObjectName("rbTableValue");
	            rbTableValue.clicked.connect(this, "tableValueSelected()");
	            
	            RadioButton rbSysUnitsValue = new RadioButton("System Units");
	            rbSysUnitsValue.setObjectName("rbSysUnitsValue");
	            rbSysUnitsValue.clicked.connect(this, "sysUnitsValueSelected()");

	            RadioButton rbEventLogValue = new RadioButton("Event log");
	            rbEventLogValue.setObjectName("rbEventLogValue");
	            rbEventLogValue.clicked.connect(this, "eventLogSelected()");

	            rbHistoricalValue.setChecked(true);

	            com.trolltech.qt.gui.QVBoxLayout vbox = new com.trolltech.qt.gui.QVBoxLayout();
	            vbox.setObjectName("vbox");
	            vbox.addWidget(rbHistoricalValue);
	            vbox.addWidget(rbSysUnitsValue);
	            vbox.addWidget(rbCorrelation);
	            vbox.addWidget(rbTableValue);
	            vbox.addWidget(rbEventLogValue);
	            vbox.addStretch(1);
	            groupBox.setLayout(vbox);
	            layout.addWidget(groupBox);
	            this.setLayout(layout);
	            this.setFinalPage(false);
	        }

	        @SuppressWarnings("unused")
	        private void tableValueSelected() {
	            childType = DashChildType:Table;
	            setFinalPage(false);
	            update();
	        }

	        @SuppressWarnings("unused")
	        private void historicalValueSelected() {
	            childType = DashChildType:Diagram;
	            isHistorical = true;
	            setFinalPage(false);
	            update();
	        }

	        @SuppressWarnings("unused")
	        private void correlationSelected() {
	            childType = DashChildType:Diagram;
	            isHistorical = false;
	            setFinalPage(false);
	            update();
	        }

	        @SuppressWarnings("unused")
	        private void sysUnitsValueSelected() {
	            childType = DashChildType:SysUnits;
	            setFinalPage(true);
	            update();
	        }

	        @SuppressWarnings("unused")
	        private void eventLogSelected() {
	            childType = DashChildType:EventLog;
	            setFinalPage(true);
	            update();
	        }

	        private class RadioButton extends com.trolltech.qt.gui.QRadioButton {

	            RadioButton(String s) {
	                super(s);
	            }

	            @Override
	            protected void mouseDoubleClickEvent(com.trolltech.qt.gui.QMouseEvent e) {
	                super.mouseDoubleClickEvent(e);
	//                DiagramWizard.this.next();
	            }
	        }
	    }

	    private class TableSettingsPage extends com.trolltech.qt.gui.QWizardPage {

	        private DashTableExplorerSettingsWidget dtWidget;

	        public TableSettingsPage(Explorer.Qt.Types::QWidget parent) {
	            setParent(parent);
	            setLayout(new com.trolltech.qt.gui.QVBoxLayout());
	            dtWidget = new DashTableExplorerSettingsWidget(((Explorer.Widgets::ExplorerWidget) parent).Environment, this);
	            dtWidget.open();
	            layout().addWidget(dtWidget);
	            dtWidget.DashTableExplorerSettingsWidget:Widget:metricKindPropEditor.edited.connect(this, "editMetricKind()");
	            dtWidget.DashTableExplorerSettingsWidget:Widget:titlePropEditor.edited.connect(this, "editTableSettings()");
	        }

	        public String getTableTitle() {
	            dtWidget.Model.title.finishEdit();
	            return dtWidget.Model.title.Value;
	        }

	        public MetricKind getMetricKind() {
	            return dtWidget.Model.metricKind.Value;
	        }

	        public boolean isShowDate() {
	            return dtWidget.Model.isShowDate.Value.booleanValue();
	        }

	        @SuppressWarnings("unused")
	        public void editMetricKind() {
	            this.completeChanged.emit();
	        }

	        @SuppressWarnings("unused")
	        public void editTableSettings() {
	            this.completeChanged.emit();
	        }

	        @Override
	        public boolean isComplete() {
	            return dtWidget.Model.isComplete();
	        }
	    }

	    public class HistoricalDiagramPage extends com.trolltech.qt.gui.QWizardPage {

	        private com.trolltech.qt.gui.QVBoxLayout layout;
	        private org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.MonitoringConfigHelper helper;
	        private org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.BaseDiagramEditor diagramEditor;

	        public HistoricalDiagramPage(org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.MonitoringConfigHelper helper) {
	//            super(DiagramWizard.this);
	            this.helper = helper;
	            this.setObjectName("HistoricalDiagramPage");
	            this.setTitle(Explorer.Env::Application.translate("SystemMonitoring", "Diagram Settings"));
	            this.setFinalPage(true);
	            layout = new com.trolltech.qt.gui.QVBoxLayout();
	            layout.setMargin(0);
	            this.setLayout(layout);
	        }

	        @Override
	        public void initializePage() {
	            if (isHistorical) {
	                diagramEditor = new org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.HistoricalDiagramEditor(null, helper);
	                org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.HistoricalDiagramEditor histEditor =
	                        (org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.HistoricalDiagramEditor) diagramEditor;
	                dashboard.fillDiagramEditor(histEditor);
	                histEditor.rangeChanged.connect(this, "metricSet()");
	            } else {
	                diagramEditor = new org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.CorrelationDiagramEditor(null, helper);
	            }
	            diagramEditor.metricSet.connect(this, "metricSet()");
	            layout.addWidget(diagramEditor);
	        }

	        private void clear() {
	            if (layout.indexOf(diagramEditor) != -1) {
	                layout.removeWidget(diagramEditor);
	            }
	            diagramEditor.hide();
	            layout.update();
	        }

	        public boolean isDateIntervalCorrect() {
	            if (isHistorical) {
	                if (diagramEditor != null) {
	                    return ((org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.HistoricalDiagramEditor) diagramEditor).isDateIntervalCorrect();
	                }
	            }
	            return false;
	        }

	        @SuppressWarnings("unused")
	        private void metricSet() {
	            this.completeChanged.emit();
	        }

	        @Override
	        public boolean isComplete() {
	            return diagramEditor != null ? diagramEditor.isComplete() : false;
	        }

	        public org.radixware.kernel.common.client.dashboard.DiagramSettings getMetricSettings() {
	            return diagramEditor.getMetricSettings();
	        }

	        @Override
	        public void cleanupPage() {
	            clear();
	        }
	    }
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::DashContainerExplorerView:Properties-Properties*/

	/*Radix::SystemMonitor::DashContainerExplorerView:model-Dynamic Property*/



	protected org.radixware.ads.SystemMonitor.explorer.DashContainer model=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:model",line=338)
	private final  org.radixware.ads.SystemMonitor.explorer.DashContainer getModel() {
		return model;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:model",line=344)
	private final   void setModel(org.radixware.ads.SystemMonitor.explorer.DashContainer val) {
		model = val;
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:tabBar-Dynamic Property*/



	protected com.trolltech.qt.gui.QTabBar tabBar=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tabBar",line=366)
	private final  com.trolltech.qt.gui.QTabBar getTabBar() {
		return tabBar;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tabBar",line=372)
	private final   void setTabBar(com.trolltech.qt.gui.QTabBar val) {
		tabBar = val;
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:tabWidget-Dynamic Property*/



	protected com.trolltech.qt.gui.QTabWidget tabWidget=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tabWidget",line=394)
	private final  com.trolltech.qt.gui.QTabWidget getTabWidget() {

		if (internal[tabWidget] != null) {
		    return internal[tabWidget];
		}

		internal[tabWidget] = new com.trolltech.qt.gui.QTabWidget(this);
		tabBar = (com.trolltech.qt.gui.QTabBar) internal[tabWidget].findChild(com.trolltech.qt.gui.QTabBar.class);
		tabBar.setObjectName("DashContainerTabBar");
		internal[tabWidget].setLayout(new com.trolltech.qt.gui.QVBoxLayout());
		internal[tabWidget].setTabsClosable(true);
		internal[tabWidget].tabCloseRequested.connect(this, idof[DashContainerExplorerView:onTabCloseButtonClick].toString() + "(int)");
		internal[tabWidget].currentChanged.connect(this, idof[DashContainerExplorerView:onCurTabChanged].toString() + "(int)");

		tabBar.setContextMenuPolicy(com.trolltech.qt.core.Qt.ContextMenuPolicy.CustomContextMenu);
		tabBar.customContextMenuRequested.connect(this, idof[DashContainerExplorerView:onTabBarMenuRequest].toString() + "(QPoint)");

		loadChildViews();

		return internal[tabWidget];
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:tbCloseCurTab-Dynamic Property*/



	protected com.trolltech.qt.gui.QToolButton tbCloseCurTab=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tbCloseCurTab",line=434)
	private final  com.trolltech.qt.gui.QToolButton getTbCloseCurTab() {
		return tbCloseCurTab;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tbCloseCurTab",line=440)
	private final   void setTbCloseCurTab(com.trolltech.qt.gui.QToolButton val) {
		tbCloseCurTab = val;
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:tbAddTabLocatedOnTabBar-Dynamic Property*/



	protected com.trolltech.qt.gui.QToolButton tbAddTabLocatedOnTabBar=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tbAddTabLocatedOnTabBar",line=462)
	private final  com.trolltech.qt.gui.QToolButton getTbAddTabLocatedOnTabBar() {
		return tbAddTabLocatedOnTabBar;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tbAddTabLocatedOnTabBar",line=468)
	private final   void setTbAddTabLocatedOnTabBar(com.trolltech.qt.gui.QToolButton val) {
		tbAddTabLocatedOnTabBar = val;
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:tbAddTabLocatedOnMainToolBar-Dynamic Property*/



	protected com.trolltech.qt.gui.QToolButton tbAddTabLocatedOnMainToolBar=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tbAddTabLocatedOnMainToolBar",line=490)
	private final  com.trolltech.qt.gui.QToolButton getTbAddTabLocatedOnMainToolBar() {
		return tbAddTabLocatedOnMainToolBar;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tbAddTabLocatedOnMainToolBar",line=496)
	private final   void setTbAddTabLocatedOnMainToolBar(com.trolltech.qt.gui.QToolButton val) {
		tbAddTabLocatedOnMainToolBar = val;
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:tabIndexBase-Dynamic Property*/



	protected int tabIndexBase=1;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tabIndexBase",line=518)
	private final  int getTabIndexBase() {
		return tabIndexBase;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tabIndexBase",line=524)
	private final   void setTabIndexBase(int val) {
		tabIndexBase = val;
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:tbRenameCurTab-Dynamic Property*/



	protected com.trolltech.qt.gui.QToolButton tbRenameCurTab=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tbRenameCurTab",line=546)
	private final  com.trolltech.qt.gui.QToolButton getTbRenameCurTab() {
		return tbRenameCurTab;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tbRenameCurTab",line=552)
	private final   void setTbRenameCurTab(com.trolltech.qt.gui.QToolButton val) {
		tbRenameCurTab = val;
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:tbAddChild-Dynamic Property*/



	protected com.trolltech.qt.gui.QToolButton tbAddChild=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tbAddChild",line=574)
	private final  com.trolltech.qt.gui.QToolButton getTbAddChild() {
		return tbAddChild;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:tbAddChild",line=580)
	private final   void setTbAddChild(com.trolltech.qt.gui.QToolButton val) {
		tbAddChild = val;
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:Methods-Methods*/

	/*Radix::SystemMonitor::DashContainerExplorerView:getWidget-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:getWidget",line=590)
	public published  org.radixware.ads.SystemMonitor.explorer.DashWidget getWidget () {
		return model;
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:getInnerContent-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:getInnerContent",line=598)
	public published  com.trolltech.qt.gui.QWidget getInnerContent () {
		return tabWidget;
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:DashContainerExplorerView-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:DashContainerExplorerView",line=605)
	public  DashContainerExplorerView (org.radixware.ads.SystemMonitor.explorer.DashContainer widget) {
		super(widget);
		model = widget;
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:loadChildViews-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:loadChildViews",line=613)
	public  void loadChildViews () {
		while (tabWidget.count() > 0) {
		    tabWidget.removeTab(0);
		}

		createFictiveTab();

		for (DashContainer.Tab tab : model.tabs) {
		    onTabAdded(tab);
		}

		tabWidget.setCurrentIndex(((DashContainer)model).currentTabIndex.intValue());
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:onChildAdded-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:onChildAdded",line=630)
	public  void onChildAdded (org.radixware.ads.SystemMonitor.explorer.DashWidget child, org.radixware.ads.SystemMonitor.explorer.DashContainer.Tab tab) {
		AbsractDashWidgetExplorerView view = child.getExplorerView();
		int idx = model.tabs.indexOf(tab);
		view.setParent(tabWidget.widget(idx));
		model.resizeWidgets();
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:onChildRemoved-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:onChildRemoved",line=640)
	public  void onChildRemoved (org.radixware.ads.SystemMonitor.explorer.DashWidget child, org.radixware.ads.SystemMonitor.explorer.DashContainer.Tab tab) {
		child.getExplorerView().setParent(null);
		child.getExplorerView().close();
		child.getExplorerView().dispose();
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:onTabCloseButtonClick-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:onTabCloseButtonClick",line=649)
	private final  void onTabCloseButtonClick (int index) {
		if(index == -1) {
		    index = tabWidget.currentIndex();
		}
		boolean dialogResult = getWidget().getDashboard().getEnvironment().messageConfirmation(model.tabs.get(index).title, "Do you really want to close this page?");
		if (dialogResult) {
		    model.removeTab(model.tabs.get(index));
		}
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:getCurrentTabIndex-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:getCurrentTabIndex",line=662)
	public  int getCurrentTabIndex () {
		return tabWidget.currentIndex();
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:onTabsCountChanged-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:onTabsCountChanged",line=669)
	private final  void onTabsCountChanged () {
		assert tabBar.count() > tabIndexBase;

		final int fictiveTabIndex = tabBar.count() - 1;
		if (tabBar.currentIndex() == fictiveTabIndex) {
		    //Fictive tab must'not be current
		    tabBar.setCurrentIndex(fictiveTabIndex - tabIndexBase);
		}

		final boolean isTabBarVisible = model.tabs.size() > 1;
		tabBar.setVisible(isTabBarVisible);

		if (tbCloseCurTab != null && tbRenameCurTab != null) {
		    tbCloseCurTab.setEnabled(isTabBarVisible);
		    tbRenameCurTab.setEnabled(model.tabs.size() > 0);
		}

		//Simple way to change default close tab button tooltip.
		String btnTooltip = "Close Page";
		for (int pageIdx = 0; pageIdx < tabBar.count(); pageIdx++) {
		    Explorer.Qt.Types::QWidget btn = tabBar.tabButton(pageIdx, com.trolltech.qt.gui.QTabBar.ButtonPosition.RightSide);
		    if (btn != null && btn != tbAddTabLocatedOnTabBar) {
		        btn.setToolTip(btnTooltip);
		    } else {
		        btn = tabBar.tabButton(pageIdx, com.trolltech.qt.gui.QTabBar.ButtonPosition.LeftSide); //OsX case, button on left side
		        if (btn != null && btn != tbAddTabLocatedOnTabBar) {
		            btn.setToolTip(btnTooltip);
		        }
		    }
		}
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:onTabAdded-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:onTabAdded",line=704)
	public  void onTabAdded (org.radixware.ads.SystemMonitor.explorer.DashContainer.Tab tabModel) {
		Explorer.Qt.Types::QWidget newTabView;
		if (tabModel instanceof DashContainer.TabError == false) {
		    newTabView = new QWidget(tabWidget) {
		        @Override
		        protected void resizeEvent(com.trolltech.qt.gui.QResizeEvent qre) {
		            setFocus(); //cancel edit mode of widget, if it enabled
		            model.resizeWidgets();
		        }
		    };

		    newTabView.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.ClickFocus);
		    com.trolltech.qt.gui.QVBoxLayout vbox = new com.trolltech.qt.gui.QVBoxLayout();
		    com.trolltech.qt.core.Qt.Alignment alligment = new com.trolltech.qt.core.Qt.Alignment(com.trolltech.qt.core.Qt.AlignmentFlag.AlignTop);
		    vbox.setAlignment(alligment);
		    vbox.setContentsMargins(0, 0, 0, 0);
		    newTabView.setLayout(vbox);
		} else {
		    newTabView = new ErrorView(getWidget().getDashboard().getEnvironment());
		    ((ErrorView) newTabView).setMessage("Error loading the page. The configuration does not exists or the rights to it are insufficient.");
		}

		for (DashWidget dw : tabModel.children) {
		    AbsractDashWidgetExplorerView view = dw.getExplorerView();
		    view.setParent(newTabView);
		    view.setVisible(true);
		}

		tabWidget.insertTab(tabWidget.count() - tabIndexBase, newTabView, tabModel.title);
		tabWidget.setCurrentWidget(newTabView);
		onTabsCountChanged();
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:onTabBarMenuRequest-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:onTabBarMenuRequest",line=740)
	private final  void onTabBarMenuRequest (com.trolltech.qt.core.QPoint point) {
		if (point == null) {
		    return;
		}
		int tabIndex = tabBar.tabAt(point);
		if (tabIndex >= model.tabs.size()) {//ignore fictive tab with addTab button
		    return;
		}
		com.trolltech.qt.gui.QMenu menu = new com.trolltech.qt.gui.QMenu(tabWidget);
		com.trolltech.qt.gui.QAction renameAct = new com.trolltech.qt.gui.QAction("Rename", menu);

		com.trolltech.qt.core.QSignalMapper signalMapper = new com.trolltech.qt.core.QSignalMapper(this);
		signalMapper.mappedInteger.connect(model, idof[DashContainer:renameTab].toString() + "(int)");
		signalMapper.setMapping(renameAct, tabIndex);
		renameAct.triggered.connect(signalMapper, "map()");

		menu.addAction(renameAct);
		menu.exec(tabWidget.mapToGlobal(point));
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:onTabRename-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:onTabRename",line=763)
	public  boolean onTabRename (int tabNum) {
		DashTabExplorerSettings dialog = new DashTabExplorerSettings(getWidget().getDashboard().getEnvironment());
		DashContainer.Tab tab = model.tabs.get(tabNum);
		dialog.Model.title.Value = tab.title;
		Client.Views::DialogResult result = dialog.execDialog();
		if (result == Client.Views::DialogResult.ACCEPTED) {
		    tab.title = dialog.Model.title.Value;
		    tabBar.setTabText(tabNum, tab.title);
		    return true;
		}
		 return false;
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:closeWidget-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:closeWidget",line=780)
	protected  void closeWidget () {
		DashContainer parent = (DashContainer) getWidget();
		while(!parent.tabs.isEmpty()) {
		    model.removeTab(model.tabs.get(0));
		}
		tabWidget.setParent(null);
		tabWidget.close();
		tabWidget.dispose();
		super.closeWidget();
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:renderSeverity-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:renderSeverity",line=795)
	public  void renderSeverity () {
		if (getWidget().parent != null) {
		    super.renderSeverity();
		}
		for (DashContainer.Tab tab : model.tabs) {
		    Utils::Color color = DashUtils.getTextForegroundColorBySeverity(tab.getTabSeverity());
		    tabBar.setTabTextColor(model.tabs.indexOf(tab), new com.trolltech.qt.gui.QColor(color.RGB));
		}

	}

	/*Radix::SystemMonitor::DashContainerExplorerView:updateInnerView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:updateInnerView",line=810)
	public published  void updateInnerView () {
		for (DashContainer.Tab tab : model.tabs) {
		    for (DashWidget dw : tab.children) {
		        dw.getExplorerView().updateView();
		    }
		}
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:prepareToClose-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:prepareToClose",line=822)
	public  void prepareToClose () {
		DashContainer parent = (DashContainer) getWidget();
		for (DashContainer.Tab tab : parent.tabs) {
		    for (DashWidget widget : tab.children) {
		        widget.getExplorerView().prepareToClose();
		    }
		}
		super.prepareToClose();
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:onTabRemoved-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:onTabRemoved",line=835)
	public  void onTabRemoved (org.radixware.ads.SystemMonitor.explorer.DashContainer.Tab tabToDelete, int index) {
		tabWidget.removeTab(index);
		onTabsCountChanged();
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:onAddTabButtonClick-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:onAddTabButtonClick",line=843)
	private final  void onAddTabButtonClick () {
		DashTabCreateExplorerDialog dialog = new DashTabCreateExplorerDialog(model.getDashboard().getEnvironment());
		dialog.Model.container = model;
		dialog.execDialog();
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:renameCurTab-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:renameCurTab",line=852)
	private final  void renameCurTab () {
		model.renameTab(tabWidget.currentIndex());
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:closeCurTab-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:closeCurTab",line=859)
	private final  void closeCurTab () {
		onTabCloseButtonClick(tabWidget.currentIndex());
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:createButtonsPanel-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:createButtonsPanel",line=867)
	protected  void createButtonsPanel (com.trolltech.qt.gui.QWidget header, boolean isRootContainerWidget, com.trolltech.qt.gui.QHBoxLayout hboxHeader) {
		super.createButtonsPanel(header, isRootContainerWidget, hboxHeader);
		if (getWidget().parent == null) {

		    if (getWidget().getDashboard().isCurrentUserDashManager()) {
		        com.trolltech.qt.gui.QToolButton tbExport = createToolButton(true, "Export Configuration", idof[Explorer.Icons::Export]);
		        com.trolltech.qt.core.QSignalMapper signalMapper = new com.trolltech.qt.core.QSignalMapper(this);
		        signalMapper.setMapping(tbExport, 1); // "1" to save settings
		        signalMapper.mappedInteger.connect(getWidget().getDashboard(), idof[Dashboard:exportSettings].toString() + "(int)");
		        tbExport.clicked.connect(signalMapper, "map()");
		        addWidgetToToolbar(1, tbExport);

		        com.trolltech.qt.gui.QToolButton tbImport = createToolButton(true, "Import Configuration", idof[Explorer.Icons::import]);
		        tbImport.clicked.connect(getWidget().getDashboard(), idof[Dashboard:importSettings].toString() + "()");
		        addWidgetToToolbar(1, tbImport);
		    }

		    if (!DashConfigType:CommonRef.equals(getWidget().getDashboard().getConfigType())) {
		        tbCloseCurTab = createToolButton(true, "Close Current Page", idof[Explorer.Icons::clear]);
		        tbCloseCurTab.clicked.connect(this, idof[DashContainerExplorerView:closeCurTab].toString() + "()");
		        addWidgetToToolbar(1, tbCloseCurTab);
		        if (model.tabs.size() <= 1) {
		            tbCloseCurTab.setEnabled(false);
		        }

		        tbRenameCurTab = createToolButton(true, "Rename Current Page", idof[Explorer.Icons::editor]);
		        tbRenameCurTab.clicked.connect(this, idof[DashContainerExplorerView:renameCurTab].toString() + "()");
		        addWidgetToToolbar(1, tbRenameCurTab);
		        if (model.tabs.size() < 1) {
		            tbRenameCurTab.setEnabled(false);
		        }

		        tbAddTabLocatedOnMainToolBar = createToolButton(true, "Add Page", idof[addEmpty]);
		        tbAddTabLocatedOnMainToolBar.clicked.connect(this, idof[DashContainerExplorerView:onAddTabButtonClick].toString() + "()");
		        addWidgetToToolbar(1, tbAddTabLocatedOnMainToolBar);

		        if (getWidget().getDashboard().isCurrentUserDashManager()) {
		            tbAddChild = createToolButton(true, "Add Widget", idof[Explorer.Icons::add]);
		            tbAddChild.clicked.connect(model, idof[DashContainer:onAddChildButtonClick].toString() + "()");
		            addWidgetToToolbar(0, tbAddChild);
		        }
		    }
		}
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:onChangeDashState-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:onChangeDashState",line=916)
	public  void onChangeDashState (org.radixware.ads.SystemMonitor.common.DashboardState prevState, org.radixware.ads.SystemMonitor.common.DashboardState newState) {
		super.onChangeDashState(prevState, newState);

		if (selectConfigWidget != null) {
		    if (DashboardState:MODIFIED == newState) {
		        selectConfigWidget.Model.setSaveBtnEnabled(true);
		    } else if (DashboardState:NEW == newState) {
		        selectConfigWidget.Model.setSaveBtnEnabled(false);
		    }
		}
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:createFictiveTab-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:createFictiveTab",line=931)
	private final  void createFictiveTab () {
		if (model.getDashboard().getConfigType() != DashConfigType:CommonRef) {
		    tabIndexBase = 1;
		    tabWidget.addTab(new QWidget(tabWidget), "");
		    tbAddTabLocatedOnTabBar = createToolButton(true, "Add Page", idof[Explorer.Icons::add]);
		    tbAddTabLocatedOnTabBar.clicked.connect(this, idof[DashContainerExplorerView:onAddTabButtonClick].toString() + "()");
		    tbAddTabLocatedOnTabBar.setFixedSize(17, 17);
		    tabBar.setTabEnabled(0, false);
		    if (tabBar.tabButton(0, com.trolltech.qt.gui.QTabBar.ButtonPosition.RightSide) != null) {
		        tabBar.setTabButton(0, com.trolltech.qt.gui.QTabBar.ButtonPosition.RightSide, tbAddTabLocatedOnTabBar);
		    } else {
		        tabBar.setTabButton(0, com.trolltech.qt.gui.QTabBar.ButtonPosition.LeftSide, tbAddTabLocatedOnTabBar);
		    }
		} else {
		    tabIndexBase = 0;
		}
	}

	/*Radix::SystemMonitor::DashContainerExplorerView:onCurTabChanged-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashContainerExplorerView:onCurTabChanged",line=952)
	private final  void onCurTabChanged (int index) {
		if (tbAddChild != null && index < model.tabs.size()) {
		    tbAddChild.setDisabled(model.tabs.get(index) instanceof DashContainer.TabError);
		}
	}


}

/* Radix::SystemMonitor::DashContainerExplorerView - Desktop Meta*/

/*Radix::SystemMonitor::DashContainerExplorerView-Desktop Dynamic Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class DashContainerExplorerView_mi{
}

/* Radix::SystemMonitor::DashContainerExplorerView - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class DashContainerExplorerView - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Переименовать текущую страницу");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Rename Current Page");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2KLY4A7HNJFGTE2L5BYHSDLBD4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Соотношение текущих значений метрик");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Proportion of Metric Current Values");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls32HBYBUEM5AEDHZUVRRJ4E5XGA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при загрузке страницы. Конфигурация не существует либо на неё нет прав.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error loading the page. The configuration does not exists or the rights to it are insufficient.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4ERBS7TITFBWVKNST2F22RQ4LE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вы действительно хотите закрыть эту страницу?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Do you really want to close this page?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4KQDRGKSGFBKFBK2BLSDCRBPCU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка задания интервала времени");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error in date interval");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4SUQFNWAAZGBBHC3VYTYDIV62I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт конфигурации");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Configuration");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5KI2OTFXI5H5HC6ORE4BWNAL5U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Закрыть страницу");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Close Page");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5PNBWVRRFFA2JNJQTORVQYUH6E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Журнал событий");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event log");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls62F4C3S5GVDW5KLYCXESHVJ5SQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Добавить виджет");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Add Widget");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7AXX3AB3YVCJ7GGAJPYRREN4U4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"История значений метрики");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Metric Value History");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7NKWPHENORGBVGUWHJPYRT5DGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модули системы");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System Units");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZLSEQT4MFBGFJD7XIS6RTRC7E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Переименовать");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Rename");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFXLQRFWDW5F6NO3LHZYW3MYA7Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Закрыть текущую страницу");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Close Current Page");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHSRQAXIJEZFYPN7KM5HG52DRSM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время начала интервала должно быть раньше времени конца интервала.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Date from must be before Date to.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHXINGCGSTJC5VKHLRYZ5NP37K4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,null,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таблица текущих значений метрик");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Table of Metric Current Values");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ7AXNBQXWVGTPFYW7L5A43CCZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Добавить страницу");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Add Page");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJK6X4KOSC5HEXM5CYW47A64NRQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Добавить страницу");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Add Page");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQGK6LQHVXVAURHGZD6DNLQXKJE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт конфигурации");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Configuration");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRUU6MNNNTBACJDZA7LYSQB2V74"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Создание виджета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Create Widget");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRZOFTM6BYBEARGN3ZHSPNLOCQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выбор типа виджета: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Select Widget Type");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS23PFAS5JZESVMVLV4VDKZE5LY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(DashContainerExplorerView - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcDXPKKOXQYRFL7PQHHWFRQDBLU4"),"DashContainerExplorerView - Localizing Bundle",$$$items$$$);
}
