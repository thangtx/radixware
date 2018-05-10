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

package org.radixware.kernel.explorer.tester;

import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.QSettings.Format;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QStatusBar;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval;
import org.radixware.kernel.common.client.trace.ClientTraceItem;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.env.ImageManager;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceItem;
import org.radixware.kernel.explorer.tester.providers.ExplorerTreeTests;
import org.radixware.kernel.explorer.tester.tests.CloseTestResult;
import org.radixware.kernel.explorer.tester.tests.CreationDialogTestResult;
import org.radixware.kernel.explorer.tester.tests.CustomTestResult;
import org.radixware.kernel.explorer.tester.tests.FilterSingleTestResult;
import org.radixware.kernel.explorer.tester.tests.FiltersTestResult;
import org.radixware.kernel.explorer.tester.tests.InsertionTestResult;
import org.radixware.kernel.explorer.tester.tests.PropDialogsSingleEntityTestResult;
import org.radixware.kernel.explorer.tester.tests.TabTestResult;
import org.radixware.kernel.explorer.tester.tests.TestResult;
import org.radixware.kernel.explorer.tester.tests.TestResultEvent;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.ViewHolder;
import org.radixware.schemas.testreport.EventListItem;
import org.radixware.schemas.testreport.EventListType;
import org.radixware.schemas.testreport.NameType;
import org.radixware.schemas.testreport.ReportItemType;
import org.radixware.schemas.testreport.ReportItemsList;
import org.radixware.schemas.testreport.ResultType;
import org.radixware.schemas.testreport.TraceListItem;
import org.radixware.schemas.testreport.TraceListType;


public class TesterWindow extends ExplorerDialog {

    public static final class Icons extends ExplorerIcon.CommonOperations {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        public static final Icons BEGIN_TEST = new Icons("classpath:images/begintest.svg");
        public static final Icons STOP_TEST = new Icons("classpath:images/test_stop.svg");
        public static final Icons OPEN_REPORT = new Icons("classpath:images/open.svg");
        public static final Icons SAVE_REPORT = new Icons("classpath:images/saveReport.svg");
        public static final Icons TESTER_WINDOW = new Icons("classpath:images/tester.svg");
        public static final Icons SETTINGS = new Icons("classpath:images/appearance_settings.svg");
    }
    static final String mainTrKey = "TesterDialog";
    private final String keyGeometryPostfix = "Settings";
    private final String resultFilterKey = "ResultFilter";
    private final QPushButton pbStartTest = new QPushButton(this);
    private final QPushButton pbStopTest = new QPushButton(this);
    private final QPushButton pbOpenReport = new QPushButton(this);
    private final QPushButton pbSaveReport = new QPushButton(this);
    private final QPushButton pbEditSettings = new QPushButton(this);
    private final TesterEngine engine;
    private final ViewHolder viewHolder;
    private boolean testing;
    private final QTreeWidget tree = new QTreeWidget();
    private final QMainWindow win = new QMainWindow();
    public QAction beginTestAction = new QAction(ExplorerIcon.getQIcon(Icons.BEGIN_TEST), Application.translate("TesterDialog", "Begin Test"), this);
    public QAction stopTestAction = new QAction(ExplorerIcon.getQIcon(Icons.STOP_TEST), Application.translate("TesterDialog", "Stop Test"), this);
    public QAction openReportAction = new QAction(ExplorerIcon.getQIcon(Icons.OPEN_REPORT), Application.translate("TesterDialog", "Open report"), this);
    public QAction saveReportAction = new QAction(ExplorerIcon.getQIcon(Icons.SAVE_REPORT), Application.translate("TesterDialog", "Save report"), this);
    public QAction exitAction = new QAction(ExplorerIcon.getQIcon(ClientIcon.Dialog.EXIT), Application.translate("TesterDialog", "Exit"), this);
    public QAction clearAction = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CLEAR), Application.translate("TesterDialog", "Clear"), this);
    public QAction editSettingsAction = new QAction(ExplorerIcon.getQIcon(Icons.SETTINGS), Application.translate("TesterDialog", "Settings..."), this);
    public Signal0 testFinished = new Signal0();
    public QStatusBar statusBar;
    public QLabel allPassedTestCounterLabel;
    public QLabel allFailedTestCounterLabel;
    public QLabel allWarnedTestCounterLabel;
    public QLabel allGoodTestCounterLabel;
    public ResultFilterBox resultFilterBox;
    private boolean disableAutoScroll = false;

    public TesterWindow(IClientEnvironment environment) {
        super(environment, (QWidget) environment.getMainWindow());
        setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
        win.setParent(this);
        engine = new TesterEngine(environment);
        viewHolder = new ViewHolder(environment);
        this.setWindowIcon(ExplorerIcon.getQIcon(Icons.TESTER_WINDOW));
        this.setMinimumSize(500, 200);

        this.setWindowTitle(Application.translate("TesterDialog", "Tester Dialog"));
        setWindowFlags(WidgetUtils.WINDOW_FLAGS_FOR_DIALOG);

        setupActions();
        setupTable();

        statusBar = new QStatusBar(win);

        QLabel allPassedTestLabel = new QLabel(Application.translate("TesterDialog", "Total tests:"), statusBar) {
            @Override
            protected void mousePressEvent(QMouseEvent ev) {
                resultFilterBox.setCurrentItem(TesterConstants.RESUL_FILTER_ALL.getTitle());
                onResultFilterActivatedIndex(0);
            }
        };
        allPassedTestCounterLabel = new QLabel("0", statusBar) {
            @Override
            protected void mousePressEvent(QMouseEvent ev) {
                resultFilterBox.setCurrentItem(TesterConstants.RESUL_FILTER_ALL.getTitle());
                onResultFilterActivatedIndex(0);
            }
        };
        statusBar.addPermanentWidget(allPassedTestLabel);
        statusBar.addPermanentWidget(allPassedTestCounterLabel);

        QLabel allGoodTestText = new QLabel(Application.translate("TesterDialog", "Passed:")) {
            @Override
            protected void mousePressEvent(QMouseEvent ev) {
                resultFilterBox.setCurrentItem(TesterConstants.RESUL_FILTER_PASSED.getTitle());
                onResultFilterActivatedIndex(1);
            }
        };
        QLabel allGoodTestLabel = new QLabel(statusBar);
        allGoodTestLabel.setPixmap(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_OK).pixmap(16, 16));
        allGoodTestCounterLabel = new QLabel("0", statusBar) {
            @Override
            protected void mousePressEvent(QMouseEvent ev) {
                resultFilterBox.setCurrentItem(TesterConstants.RESUL_FILTER_PASSED.getTitle());
                onResultFilterActivatedIndex(1);
            }
        };
        statusBar.addPermanentWidget(allGoodTestLabel);
        statusBar.addPermanentWidget(allGoodTestText);
        statusBar.addPermanentWidget(allGoodTestCounterLabel);

        QLabel allFailedTestText = new QLabel(Application.translate("TesterDialog", "Errors:"), statusBar) {
            @Override
            protected void mousePressEvent(QMouseEvent ev) {
                resultFilterBox.setCurrentItem(TesterConstants.RESUL_FILTER_ERRORS.getTitle());
                onResultFilterActivatedIndex(2);
            }
        };
        QLabel allFailedTestLabel = new QLabel(statusBar);
        allFailedTestLabel.setPixmap(ExplorerIcon.getQIcon(ClientIcon.TraceLevel.ERROR).pixmap(16, 16));

        allFailedTestCounterLabel = new QLabel("0", statusBar) {
            @Override
            protected void mousePressEvent(QMouseEvent ev) {
                resultFilterBox.setCurrentItem(TesterConstants.RESUL_FILTER_ERRORS.getTitle());
                onResultFilterActivatedIndex(2);
            }
        };
        statusBar.addPermanentWidget(allFailedTestLabel);
        statusBar.addPermanentWidget(allFailedTestText);
        statusBar.addPermanentWidget(allFailedTestCounterLabel);

        QLabel allWarnedTestText = new QLabel(Application.translate("TesterDialog", "Warnings:"), statusBar) {
            @Override
            protected void mousePressEvent(QMouseEvent ev) {
                resultFilterBox.setCurrentItem(TesterConstants.RESUL_FILTER_WARNINGS.getTitle());
                onResultFilterActivatedIndex(3);

            }
        };
        QLabel allWarnedTestLabel = new QLabel(statusBar);
        allWarnedTestLabel.setPixmap(ExplorerIcon.getQIcon(ClientIcon.TraceLevel.WARNING).pixmap(16, 16));
        allWarnedTestCounterLabel = new QLabel("0", statusBar) {
            @Override
            protected void mousePressEvent(QMouseEvent ev) {
                resultFilterBox.setCurrentItem(TesterConstants.RESUL_FILTER_WARNINGS.getTitle());
                onResultFilterActivatedIndex(3);
            }
        };
        statusBar.addPermanentWidget(allWarnedTestLabel);
        statusBar.addPermanentWidget(allWarnedTestText);
        statusBar.addPermanentWidget(allWarnedTestCounterLabel);

        QToolBar tools = new QToolBar(Application.translate("TesterDialog", "Tool Bar"), win);
        tools.setFloatable(false);
        tools.setObjectName("MainToolBar");
        win.addToolBar(tools);
        win.setCentralWidget(tree);

        tree.itemSelectionChanged.connect(this, "treeItemChanged()");
        tree.verticalScrollBar().valueChanged.connect(this, "scrollTo(int)");
        win.setStatusBar(statusBar);

        pbStartTest.setToolTip(Application.translate("TesterDialog", "Start Test"));
        pbStopTest.setToolTip(Application.translate("TesterDialog", "Stop Test"));
        pbStartTest.clicked.connect(this, "startTesting()");
        pbStopTest.clicked.connect(engine, "finishTesting()");
        pbStartTest.setIcon(beginTestAction.icon());
        pbStopTest.setIcon(stopTestAction.icon());
        pbStopTest.setEnabled(false);
        viewHolder.setVisible(false);
        engine.finished.connect(this, "onTestingFinished()");

        pbEditSettings.setToolTip(Application.translate("TesterDialog", "Edit Test Settings"));
        pbEditSettings.clicked.connect(this, "editSettings()");
        pbEditSettings.setIcon(editSettingsAction.icon());

        tools.addWidget(pbStartTest);
        tools.addWidget(pbStopTest);
        tools.addSeparator();
        tools.addWidget(pbEditSettings);

        pbOpenReport.setToolTip(Application.translate("TesterDialog", "Open Test Report From Xml-file"));
        pbSaveReport.setToolTip(Application.translate("TesterDialog", "Save Test Report to Xml-file"));
        pbOpenReport.clicked.connect(this, "loadReportFromXml()");
        pbSaveReport.clicked.connect(this, "saveReportToXml()");
        pbOpenReport.setIcon(openReportAction.icon());
        pbSaveReport.setIcon(saveReportAction.icon());

        tools.addWidget(pbOpenReport);
        tools.addWidget(pbSaveReport);
        tools.addSeparator();

        resultFilterBox = new ResultFilterBox(tools);
        resultFilterBox.addItem(TesterConstants.RESUL_FILTER_ALL.getTitle());
        resultFilterBox.addItem(TesterConstants.RESUL_FILTER_ERRORS.getTitle());
        resultFilterBox.addItem(TesterConstants.RESUL_FILTER_WARNINGS.getTitle());
        resultFilterBox.addItem(TesterConstants.RESUL_FILTER_PASSED.getTitle());
        resultFilterBox.connectToActivatedIndex(this, "onResultFilterActivatedIndex(Integer)");
        tools.addWidget(resultFilterBox);

        layout().addWidget(win);
        layout().setContentsMargins(0, 0, 0, 0);

        this.finished.connect(this, "onClosing(Integer)");

        win.setVisible(true);
        loadSettings();
    }

    private void setupTable() {
        tree.setColumnCount(5);
        LinkedList<String> titles = new LinkedList<>();
        prepareHeaders(titles);
        tree.setHeaderLabels(titles);
        tree.setColumnWidth(0, 150);
        tree.setColumnWidth(1, 120);
        tree.setColumnWidth(2, 120);
        tree.setColumnWidth(3, 90);
        tree.itemDoubleClicked.connect(this, "clickOnItem(QTreeWidgetItem, Integer)");
    }

    @SuppressWarnings("unused")
    private void onResultFilterActivatedIndex(Integer index) {
        if (index > -1 && index < resultFilterBox.getCount()) {
            final String resultFilter = resultFilterBox.getCurrentItem();
            boolean showAll = resultFilter.equals(TesterConstants.RESUL_FILTER_ALL.getTitle());
            boolean showErrors = resultFilter.equals(TesterConstants.RESUL_FILTER_ERRORS.getTitle());
            boolean showWarnings = resultFilter.equals(TesterConstants.RESUL_FILTER_WARNINGS.getTitle());
            boolean showPassed = resultFilter.equals(TesterConstants.RESUL_FILTER_PASSED.getTitle());

            for (int i = 0, size = tree.topLevelItemCount(); i < size; i++) {
                QTreeWidgetItem topItem = tree.topLevelItem(i);
                topItem.setHidden(shouldBeHidden(topItem, showAll, showErrors, showWarnings, showPassed));
            }
        }
    }

    @SuppressWarnings("unused")
    private void treeItemChanged() {
        if (tree.currentItem() != null) {
            disableAutoScroll = true;
        } else {
            disableAutoScroll = false;
        }
    }

    @SuppressWarnings("unused")
    private void scrollTo(int value) {
        if (value != tree.verticalScrollBar().maximum()) {
            disableAutoScroll = true;
        } else {
            disableAutoScroll = false;
        }
    }

    private boolean shouldBeHidden(QTreeWidgetItem item, boolean showAll, boolean showErrors, boolean showWarnings, boolean showPassed) {
        QColor foreground = item.foreground(0).color();
        if (!showAll) {
            if (showErrors) {
                return !foreground.equals(QColor.red);
            } else if (showWarnings) {
                return !foreground.equals(QColor.darkYellow);
            } else if (showPassed) {
                return foreground.equals(QColor.red) || foreground.equals(QColor.darkYellow);
            }
        }
        return false;
    }

    private void clickOnItem(QTreeWidgetItem item, Integer index) {
        if (item != null) {
            Object data = item.data(2, Qt.ItemDataRole.UserRole);
            if (data != null && data instanceof TestResultItem) {
                ((TestResultItem) data).callResultDialog();
            }
        }
    }

    private void prepareHeaders(LinkedList<String> titles) {
        titles.add(Application.translate("TesterDialog", "Name"));
        titles.add(Application.translate("TesterDialog", "Type of element"));
        titles.add(Application.translate("TesterDialog", "Result"));
        titles.add(Application.translate("TesterDialog", "Time"));
        titles.add(Application.translate("TesterDialog", "Path"));
    }

    private void setupActions() {
        beginTestAction.triggered.connect(this, "startTesting()");
        stopTestAction.triggered.connect(engine, "finishTesting()");
        clearAction.triggered.connect(this, "clearTestTable()");
        openReportAction.triggered.connect(this, "loadReportFromXml()");
        saveReportAction.triggered.connect(this, "saveReportToXml()");
        editSettingsAction.triggered.connect(this, "editSettings()");
    }

    private class Writer implements ITestResultsWriter {

        final private ExplorerSettings settings;
        private long passed = 0;
        private long error = 0;
        private long warning = 0;
        private long good = 0;

        Writer() {
            settings = (ExplorerSettings) TesterWindow.this.getEnvironment().getConfigStore();
        }

        private String getSettingsGroup(final TestResult result) {
            if (result.type.equals(TesterConstants.OBJ_EDITOR.getTitle()) || result.type.equals(TesterConstants.OBJ_PAGE.getTitle())) {
                return SettingNames.ExplorerTree.EDITOR_GROUP;
            } else if (result.type.equals(TesterConstants.OBJ_SELECTOR.getTitle())) {
                return SettingNames.ExplorerTree.SELECTOR_GROUP;
            } else if (result.type.equals(TesterConstants.OBJ_PARAGRAPH.getTitle())) {
                return SettingNames.ExplorerTree.PARAGRAPH_GROUP;
            } else {
                return "";
            }
        }

        private QTreeWidgetItem findLastAddedEditorOrSelector(boolean forCloseResult, String type, List<QTreeWidgetItem> branch) {
            int index = branch.size() - 1;
            if (index >= 0) {
                QTreeWidgetItem result = branch.get(index);

                if (forCloseResult) {
                    boolean stop = false;
                    while (!stop && index > 0) {
                        index--;
                        result = branch.get(index);
                        if (result.text(1).equals(type)) {
                            if (result.childCount() > 0 && !result.child(result.childCount() - 1).text(0).equals(TesterConstants.TEST_CLOSING.getTitle())) {
                                stop = true;
                            }
                        }
                    }
                } else if (groupsHeads.isEmpty() || !(groupsHeads.get(groupsHeads.size() - 1).equals(TesterConstants.OBJ_SELECTOR.getTitle()))) {
                    while (!result.text(1).equals(TesterConstants.OBJ_SELECTOR.getTitle())
                            && !result.text(1).equals(TesterConstants.OBJ_EDITOR.getTitle())) {
                        index--;
                        result = branch.get(index);
                    }
                } else if (groupsHeads.get(groupsHeads.size() - 1).equals(TesterConstants.OBJ_SELECTOR.getTitle())) {
                    while (!result.text(1).equals(TesterConstants.OBJ_SELECTOR.getTitle())) {
                        index--;
                        result = branch.get(index);
                    }
                }
                return result;

            }
            return null;
        }

        private QTreeWidgetItem findOrCreateItem(QTreeWidgetItem editorTopItem, String name) {
            boolean stop = false;
            int i = 0;
            while (!stop && i < editorTopItem.childCount()) {
                if (editorTopItem.child(i).text(0).equals(name)) {
                    stop = true;
                } else {
                    i++;
                }
            }
            if (stop) {
                return editorTopItem.child(i);
            } else {
                QTreeWidgetItem result = new QTreeWidgetItem(editorTopItem);
                result.setText(0, name);
                editorTopItem.addChild(result);
                return result;
            }
        }

        private QTreeWidgetItem findAmongChildren(QTreeWidgetItem lastTopItem, String name) {
            boolean stop = false;
            int f = lastTopItem.childCount() - 1;
            while (f > -1 && !stop) {
                if (lastTopItem.child(f).text(0).equals(name)) {
                    stop = true;
                    return lastTopItem.child(f);
                } else {
                    f--;
                }
            }
            return null;
        }

        @Override
        public void writeResult(TestResult result) {
            passed++;

            QTreeWidgetItem subTest = new QTreeWidgetItem();
            if (result.hasErrors()) {
                subTest.setForeground(0, new QBrush(QColor.red));
                error++;
            } else if (result.hasWarnings()) {
                subTest.setForeground(0, new QBrush(QColor.darkYellow));
                warning++;
            } else if (result.hasInfoMessages()) {
                subTest.setForeground(0, new QBrush(QColor.darkGreen));
            } else if (result.result.equals(TesterConstants.RESULT_FAIL_TIME_LIMIT.getTitle())) {
                warning++;
                subTest.setForeground(0, new QBrush(QColor.darkYellow));
            } else {
                good++;
            }

            allPassedTestCounterLabel.setText(Long.valueOf(passed).toString());
            allFailedTestCounterLabel.setText(Long.valueOf(error).toString());
            allWarnedTestCounterLabel.setText(Long.valueOf(warning).toString());
            allGoodTestCounterLabel.setText(Long.valueOf(good).toString());

            String groupName = getSettingsGroup(result);
            final QFont font;

            if (!groupName.isEmpty()) {
                settings.beginGroup(SettingNames.SYSTEM);
                settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
                settings.beginGroup(groupName);

                font = settings.readQFont(SettingNames.ExplorerTree.Common.FONT);

            } else {
                font = QApplication.font();
            }


            if (pageGroupStarted && !creationDialogGroupStarted) {
                Object lastPage = getPageHead();
                if (lastPage != null) {
                    QTreeWidgetItem branchOwner = lastAddedPageTestItem;
                    int kidsCount = branchOwner.childCount();
                    List<QTreeWidgetItem> kids = new ArrayList<>();
                    for (int i = 0; i <= kidsCount - 1; i++) {
                        kids.add(branchOwner.child(i));
                    }
                    writing(result, subTest, font, groupName, kids, branchOwner);
                }
            } else if (creationDialogGroupStarted) {
                Object lastCreationItem = getCreationDialogHead();
                if (lastCreationItem != null && lasdAddedCreationDialogItem != null) {
                    QTreeWidgetItem branchOwner = lasdAddedCreationDialogItem;
                    int kidsCount = branchOwner.childCount();
                    List<QTreeWidgetItem> kids = new ArrayList<>();
                    for (int i = 0; i <= kidsCount - 1; i++) {
                        kids.add(branchOwner.child(i));
                    }
                    writing(result, subTest, font, groupName, kids, branchOwner);
                }
            } else {
                int topCount = tree.topLevelItemCount();
                List<QTreeWidgetItem> tops = new ArrayList<>();
                for (int i = 0; i <= topCount - 1; i++) {
                    tops.add(tree.topLevelItem(i));
                }
                writing(result, subTest, font, groupName, tops, null);
            }


            if (!groupName.isEmpty()) {
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
            }
        }

        private void writing(TestResult result, QTreeWidgetItem subTest, QFont font, String groupName, List<QTreeWidgetItem> branch, QTreeWidgetItem owner) {
            final String resultFilter = resultFilterBox.getCurrentItem();
            boolean showAll = resultFilter.equals(TesterConstants.RESUL_FILTER_ALL.getTitle());
            boolean showErrors = resultFilter.equals(TesterConstants.RESUL_FILTER_ERRORS.getTitle());
            boolean showWarnings = resultFilter.equals(TesterConstants.RESUL_FILTER_WARNINGS.getTitle());
            boolean showPassed = resultFilter.equals(TesterConstants.RESUL_FILTER_PASSED.getTitle());

            if (result instanceof CloseTestResult
                    || result instanceof FiltersTestResult
                    || result instanceof FilterSingleTestResult
                    || result instanceof CustomTestResult
                    || result instanceof PropDialogsSingleEntityTestResult /*|| result instanceof CreationDialogTestResult*/) {
                QTreeWidgetItem lastTopItem;
                if (result instanceof CloseTestResult) {
                    lastTopItem = openedViews.pop();
                    if (creationDialogGroupStarted
                            && lasdAddedCreationDialogItem.parent() != null
                            && lasdAddedCreationDialogItem.parent().text(0).equals(TesterConstants.TEST_CREATION_SELECT_DIALOG.getTitle())) {
                        lasdAddedCreationDialogItem = lasdAddedCreationDialogItem.parent();
                    }
                } else {
                    lastTopItem = openedViews.peek();

                    //lastTopItem.child(0) - always "opening" test tree item
                    if (lastTopItem.child(0).text(2).equals(TesterConstants.RESULT_FAIL_ALREADY_OPENED.getTitle())) {
                        openedViews.pop();
                    }
                }

                if (lastTopItem != null) {
                    if (result instanceof FilterSingleTestResult) {
                        QTreeWidgetItem filtersItem = findAmongChildren(lastTopItem, TesterConstants.TEST_FILTERS.getTitle());
                        if (filtersItem != null) {
                            filtersItem.addChild(subTest);
                        }
                    } else {
                        lastTopItem.addChild(subTest);
//                        if (result instanceof CreationDialogTestResult && !creationDialogGroupStarted){
//                            lasdAddedCreationDialogItem = subTest;
//                        }
                    }

                    if (result instanceof FilterSingleTestResult) {
                        subTest.setText(0, result.title);
                        subTest.setIcon(0, result.icon);
                    } else if (result instanceof PropDialogsSingleEntityTestResult) {
                        PropDialogsSingleEntityTestResult singleResult = (PropDialogsSingleEntityTestResult) result;
                        subTest.setText(0, TesterConstants.TEST_PROP_DIALOG.getTitle());
                        int size = singleResult.getTestedPropertiesCount();
                        for (int i = 0; i < size; i++) {
                            QTreeWidgetItem singleItem = new QTreeWidgetItem();
                            singleItem.setText(0, singleResult.getProperty(i).getTitle());
                            singleItem.setText(2, singleResult.getResult(i));
                            singleItem.setText(3, singleResult.getTimeStr(i));
                            if (singleResult.getResult(i).equals(TesterConstants.RESULT_FAIL_TIME_LIMIT.getTitle())) {
                                singleItem.setForeground(0, new QBrush(QColor.red));
                                subTest.setForeground(0, new QBrush(QColor.red));
                                lastTopItem.setForeground(0, new QBrush(QColor.red));
                                error++;
                            }
                            subTest.addChild(singleItem);
                        }
                    } else {
                        subTest.setText(0, result.operation);
                    }
                    if (!result.getEventsLog().isEmpty()
                            || !result.getTraceEventsLog().isEmpty()) {
                        TestResultItem resultItem = new TestResultItem(result, tree);
                        subTest.setData(2, Qt.ItemDataRole.UserRole, resultItem);
                        lastTopItem.setForeground(0, subTest.foreground(0));

                    } else if (result.result.equals(TesterConstants.RESULT_FAIL_TIME_LIMIT.getTitle())) {
                        if (!lastTopItem.foreground(0).color().equals(QColor.red)) {
                            lastTopItem.setForeground(0, subTest.foreground(0));
                        }
                    }
                    subTest.setText(2, result.result);
                    subTest.setText(3, result.time);

                    hideIfNessaccery(lastTopItem, showAll, showErrors, showWarnings, showPassed);
                }

            } else if (!groupsHeads.isEmpty()) {
                QTreeWidgetItem editorTopItem = null;
                String lastGroupHead = groupsHeads.get(groupsHeads.size() - 1);

                if (lastGroupHead.equals(TesterConstants.OBJ_SELECTOR.getTitle()) && result.isForEntityModel) {
                    editorTopItem = new QTreeWidgetItem();
                    if (owner == null) {
                        writeDownResult(editorTopItem);
                    } else {
                        owner.addChild(editorTopItem);
                    }
                    if (result.operation.equals(TesterConstants.TEST_OPENING.getTitle())) {
                        openedViews.push(editorTopItem);
                    }

                    editorTopItem.setText(0, result.title);
                    editorTopItem.setIcon(0, result.icon);
                    editorTopItem.setText(1, result.type);
                    editorTopItem.setFont(0, font);

                } else if (result instanceof CreationDialogTestResult
                        && creationDialogGroupStarted
                        && lasdAddedCreationDialogItem != null
                        && lasdAddedCreationDialogItem.text(0).equals(TesterConstants.TEST_CREATION_SELECT_DIALOG.getTitle())) {
                    editorTopItem = lasdAddedCreationDialogItem;
                } else {
                    editorTopItem = findLastAddedEditorOrSelector(false, result.type, branch);
                    if (editorTopItem == null) {
                        editorTopItem = new QTreeWidgetItem();
                        if (result.operation.equals(TesterConstants.TEST_OPENING.getTitle())) {
                            openedViews.push(editorTopItem);
                        }

                        editorTopItem.setText(0, result.title);
                        editorTopItem.setIcon(0, result.icon);
                        editorTopItem.setText(1, result.type);
                        editorTopItem.setFont(0, font);
                        if (owner == null) {
                            writeDownResult(editorTopItem);
                        } else {
                            owner.addChild(editorTopItem);
                        }
                    }
                }

                if (result instanceof TabTestResult) {
                    QTreeWidgetItem pagesItem = findOrCreateItem(editorTopItem, TesterConstants.OBJ_ALL_PAGES.getTitle());

                    pagesItem.addChild(subTest);
                    subTest.setText(0, result.title);
                    subTest.setIcon(0, result.icon);
                    this.lastAddedPageTestItem = subTest;
                    subTest.setText(1, result.type);
                    subTest.setFont(0, font);

                } else {
                    editorTopItem.addChild(subTest);
                    if (result instanceof CreationDialogTestResult /*&& !creationDialogGroupStarted*/) {
                        lasdAddedCreationDialogItem = subTest;
                    }

                    subTest.setText(0, result.operation);
                }

                if (!result.getEventsLog().isEmpty()
                        || !result.getTraceEventsLog().isEmpty()) {
                    TestResultItem resultItem = new TestResultItem(result, tree);
                    subTest.setData(2, Qt.ItemDataRole.UserRole, resultItem);
                    editorTopItem.setForeground(0, subTest.foreground(0));
                } else if (result.result.equals(TesterConstants.RESULT_FAIL_TIME_LIMIT.getTitle())) {
                    if (!editorTopItem.foreground(0).color().equals(QColor.red)) {
                        editorTopItem.setForeground(0, subTest.foreground(0));
                    }
                }

                hideIfNessaccery(editorTopItem, showAll, showErrors, showWarnings, showPassed);

                subTest.setText(2, result.result);
                subTest.setText(3, result.time);

                if (result instanceof InsertionTestResult) {
                    InsertionTestResult insResult = (InsertionTestResult) result;
                    List<RadClassPresentationDef> insertedClasses = insResult.getInsertedClasses();
                    final QFont classesFont;
                    if (insertedClasses.size() > 0) {
                        settings.endGroup();
                        settings.beginGroup(SettingNames.ExplorerTree.EDITOR_GROUP);
                        classesFont = settings.readQFont(SettingNames.ExplorerTree.Common.FONT);
                    } else {
                        classesFont = QApplication.font();
                    }
                    for (RadClassPresentationDef p : insertedClasses) {
                        QTreeWidgetItem classDefItem = new QTreeWidgetItem();
                        if (!p.getClassTitle().isEmpty()) {
                            classDefItem.setText(0, p.getClassTitle());
                        } else {
                            classDefItem.setText(0, p.getSimpleName());
                        }
                        classDefItem.setIcon(0, (QIcon) p.getIcon());
                        classDefItem.setToolTip(0, "");
                        classDefItem.setFont(0, classesFont);
                        subTest.addChild(classDefItem);
                    }
                    if (insertedClasses.size() > 0) {
                        settings.endGroup();
                        settings.beginGroup(groupName);
                    }
                }

            } else {
                QTreeWidgetItem title = new QTreeWidgetItem();
                title.setText(0, result.title);
                title.setIcon(0, result.icon);
                title.setText(1, result.type);
                title.setFont(0, font);

                if (result.operation.equals(TesterConstants.TEST_OPENING.getTitle())) {
                    openedViews.push(title);
                }

                title.addChild(subTest);
                subTest.setText(0, result.operation);
                if (!result.getEventsLog().isEmpty()
                        || !result.getTraceEventsLog().isEmpty()) {
                    TestResultItem resultItem = new TestResultItem(result, tree);
                    subTest.setData(2, Qt.ItemDataRole.UserRole, resultItem);
                    title.setForeground(0, subTest.foreground(0));
                } else if (result.result.equals(TesterConstants.RESULT_FAIL_TIME_LIMIT.getTitle())) {
                    if (!title.foreground(0).color().equals(QColor.red)) {
                        title.setForeground(0, subTest.foreground(0));
                    }
                }
                subTest.setText(3, result.time);
                subTest.setText(2, result.result);
                title.setText(4, result.path);

                if (owner == null) {
                    writeDownResult(title);
                } else {
                    owner.addChild(title);
                }

                hideIfNessaccery(title, showAll, showErrors, showWarnings, showPassed);
            }
        }

        private void writeDownResult(QTreeWidgetItem item) {
            tree.addTopLevelItem(item);
            if (tree.selectedItems().isEmpty()) {
                tree.scrollToBottom();
            } else {
                if (disableAutoScroll) {
                    
                } else {
                    tree.scrollToBottom();
                }
            }
        }

        private void hideIfNessaccery(QTreeWidgetItem item, boolean showAll, boolean showErrors, boolean showWarnings, boolean showPassed) {
            QColor foreground = item.foreground(0).color();
            if (!showAll) {
                if (showErrors) {
                    item.setHidden(!foreground.equals(QColor.red));
                } else if (showWarnings) {
                    item.setHidden(!foreground.equals(QColor.darkYellow));
                } else if (showPassed) {
                    item.setHidden(foreground.equals(QColor.red) || foreground.equals(QColor.darkYellow));
                }
            }
        }
        private Stack<QTreeWidgetItem> openedViews = new Stack<QTreeWidgetItem>();//to look for "owner" of filter test or closing test
        private Stack<String> groupsHeads = new Stack<String>();//group heads types: Selector, Page etc.
        private boolean pageGroupStarted = false;
        private boolean creationDialogGroupStarted = false;
        private QTreeWidgetItem lastAddedPageTestItem;
        private QTreeWidgetItem lasdAddedCreationDialogItem;

        private Object getPageHead() {
            for (int index = groupsHeads.size() - 1; index >= 0; index--) {
                String obj = groupsHeads.get(index);
                if (obj != null && obj.equals(TesterConstants.OBJ_PAGE.getTitle())) {
                    return obj;
                }
            }
            return null;
        }

        private Object getCreationDialogHead() {
            for (int index = groupsHeads.size() - 1; index >= 0; index--) {
                String obj = groupsHeads.get(index);
                if (obj != null && obj.equals(TesterConstants.OBJ_SELECTOR_CREATIONDIALOG.getTitle())) {
                    return obj;
                }
            }
            return null;
        }

        public void enterTestGoup(String groupHead) {
            groupsHeads.push(groupHead);
            if (TesterConstants.OBJ_PAGE.getTitle().equals(groupHead)) {
                this.pageGroupStarted = true;
            }
            if (TesterConstants.OBJ_SELECTOR_CREATIONDIALOG.getTitle().equals(groupHead)) {
                this.creationDialogGroupStarted = true;
            }
        }

        public void exitTestGoup() {
            if (!groupsHeads.isEmpty()) {
                String poped = groupsHeads.pop();
                if (TesterConstants.OBJ_PAGE.getTitle().equals(poped)) {
                    this.pageGroupStarted = false;
                }
                if (TesterConstants.OBJ_SELECTOR_CREATIONDIALOG.getTitle().equals(poped)) {
                    this.creationDialogGroupStarted = groupsHeads.size() > 0 && TesterConstants.OBJ_SELECTOR_CREATIONDIALOG.getTitle().equals(groupsHeads.peek());
                }
            }
        }
    }
    private TestsOptions currentOptions;

    private TestsOptions getOptions() {
        currentOptions = readTestOptionsFromConfig();
        return currentOptions;
    }

    private TestsOptions readTestOptionsFromConfig() {
        ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
        TestsOptions options = new TestsOptions();
        try {
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(TesterWindow.mainTrKey);
            settings.beginGroup(TesterSettingsDialog.dialogKey);

            options.openingTimeBoundary = loadTimeSettingsForKey(settings, TesterSettingsDialog.OPN_TIME);
            options.insertsTimeBoundary = loadTimeSettingsForKey(settings, TesterSettingsDialog.INS_TIME);
            options.filtersTimeBoundary = loadTimeSettingsForKey(settings, TesterSettingsDialog.FLT_TIME);
            options.pagesTimeBoundary = loadTimeSettingsForKey(settings, TesterSettingsDialog.PGS_TIME);
            options.closingTimeBoundary = loadTimeSettingsForKey(settings, TesterSettingsDialog.CLS_TIME);
            options.propDialogTimeBoundary = loadTimeSettingsForKey(settings, TesterSettingsDialog.PROPDLG_TIME);
            options.creationDialogTimeBoundary = loadTimeSettingsForKey(settings, TesterSettingsDialog.CREATION_TIME);

            options.testInserts = loadUsageSettingsForKey(settings, TesterSettingsDialog.TEST_INS);
            options.testFilters = loadUsageSettingsForKey(settings, TesterSettingsDialog.TEST_FLT);
            options.testPages = loadUsageSettingsForKey(settings, TesterSettingsDialog.TEST_PGS);
            options.testPropDialog = loadUsageSettingsForKey(settings, TesterSettingsDialog.TEST_PROPDLG);
            options.testCreationDialog = loadUsageSettingsForKey(settings, TesterSettingsDialog.TEST_CREATION);

            long ins_count = loadCountSettingsForKey(settings, TesterSettingsDialog.INS_COUNT);
            options.inserts = options.testInserts ? (ins_count <= 0 ? -1 : ins_count) : 0;

            long filters_count = loadCountSettingsForKey(settings, TesterSettingsDialog.FLT_COUNT);
            options.filtersCount = options.testFilters ? (filters_count <= 0 ? -1 : filters_count) : 0;

            long pages_count = loadCountSettingsForKey(settings, TesterSettingsDialog.PGS_COUNT);
            options.pagesCount = options.testPages ? (pages_count <= 0 ? -1 : pages_count) : 0;
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
        return options;
    }
    private ConnectionOptions connectionOptions;

    @SuppressWarnings("unused")
    private void startTesting() {
        tree.clear();
        allPassedTestCounterLabel.setText("0");
        allFailedTestCounterLabel.setText("0");
        allWarnedTestCounterLabel.setText("0");
        allGoodTestCounterLabel.setText("0");
        engine.clearResultWriters();

        connectionOptions = Application.getConnectionOptions();

        engine.setTestsOptions(getOptions());
        engine.addResultsWriter(new Writer());
        pbStartTest.setEnabled(false);
        pbStopTest.setEnabled(true);
        testing = true;
        engine.startTesting(new ExplorerTreeTests(getEnvironment(), null));
    }

    public void startTestring(String settingsFileName) {
        assert (settingsFileName != null);
        loadSettings(settingsFileName);
        startTesting();
    }

    public void startTesting(TestsOptions options) {
        this.currentOptions = options;
        startTesting();
    }

    @SuppressWarnings("unused")
    private void onTestingFinished() {
        testing = false;
        pbStartTest.setEnabled(true);
        pbStopTest.setEnabled(false);

        testFinished.emit();
    }

    private void editSettings() {
        TesterSettingsDialog dialog = new TesterSettingsDialog(getEnvironment(), this, getOptions());
        dialog.exec();
    }

    @Override
    public void done(int result) {
        if (!testing) {//пока идет тестирование диалог закрыть нельзя
            super.done(result);
        }
    }

    @SuppressWarnings("unused")
    private void clearTestTable() {
        tree.clear();
    }

    private void loadReportFromXml() {
        QFileDialog dialog = new QFileDialog(this, tr("Open report"), QDir.homePath(), tr("XML Document (*.xml)"));
        dialog.setAcceptMode(QFileDialog.AcceptMode.AcceptOpen);
        dialog.setFileMode(QFileDialog.FileMode.ExistingFile);

        if (QDialog.DialogCode.resolve(dialog.exec()).equals(QDialog.DialogCode.Accepted)) {
            File report_file = new File(dialog.selectedFiles().get(0));
            report_file.setReadable(true);
            try {
                org.radixware.schemas.testreport.TestReportDocument testReport = org.radixware.schemas.testreport.TestReportDocument.Factory.parse(report_file);
                org.radixware.schemas.testreport.TestReportType rootTag = testReport.getTestReport();

                allPassedTestCounterLabel.setText(Long.valueOf(rootTag.getTotalTests()).toString());
                allFailedTestCounterLabel.setText(Long.valueOf(rootTag.getErroniousTests()).toString());
                allGoodTestCounterLabel.setText(Long.valueOf(rootTag.getPassedTests()).toString());
                allWarnedTestCounterLabel.setText(Long.valueOf(rootTag.getWarnedTests()).toString());

                resultFilterBox.setCurrentItem(rootTag.getResultFilter());

                TestsOptions options = getOptions();
                options.testInserts = rootTag.getTestInserts();
                options.testFilters = rootTag.getTestFilters();
                options.testPages = rootTag.getTestPages();
                options.testPropDialog = rootTag.getTestPropDialog();
                options.testCreationDialog = rootTag.getTestCreationDialog();
                if (rootTag.getInsertsCount() != null) {
                    options.inserts = rootTag.getInsertsCount().longValue();
                } else {
                    options.inserts = options.testInserts ? -1 : 0;
                }

                if (rootTag.getFiltersCount() != null) {
                    options.filtersCount = rootTag.getFiltersCount().longValue();
                } else {
                    options.filtersCount = options.testFilters ? -1 : 0;
                }

                if (rootTag.getPagesCount() != null) {
                    options.pagesCount = rootTag.getPagesCount().longValue();
                } else {
                    options.pagesCount = options.testPages ? -1 : 0;
                }

                options.openingTimeBoundary = rootTag.getOpeningTime();
                options.closingTimeBoundary = rootTag.getClosingTime();
                options.insertsTimeBoundary = rootTag.getInsertsTime();
                options.filtersTimeBoundary = rootTag.getFiltersTime();
                options.pagesTimeBoundary = rootTag.getPagesTime();
                options.propDialogTimeBoundary = rootTag.getPropertyDialogTime();
                options.creationDialogTimeBoundary = rootTag.getCreationDialogTime();

                List<ReportItemType> items = rootTag.getItemList();
                tree.clear();

                for (ReportItemType reportItem : items) {
                    QTreeWidgetItem treeItem = new QTreeWidgetItem();
                    tree.addTopLevelItem(treeItem);
                    loadItemSettings(reportItem, treeItem);
                    if (reportItem.getSubItems() != null) {
                        loadSubItemsInfo(reportItem, treeItem);
                    }
                }
                onResultFilterActivatedIndex(resultFilterBox.getCurrentIndex());
            } catch (XmlException xmle) {
                QMessageBox.critical(this, Application.translate("TesterDialog", "Xml exception"), xmle.getMessage());
            } catch (IOException io) {
                QMessageBox.critical(this, Application.translate("TesterDialog", "Input/Output exception"), io.getMessage());
            }
        }
    }

    private void loadSubItemsInfo(ReportItemType reportItem, QTreeWidgetItem treeItem) {
        ReportItemsList children = reportItem.getSubItems();
        List<ReportItemType> childrenList = children.getItemList();
        for (ReportItemType child : childrenList) {
            QTreeWidgetItem treeChild = new QTreeWidgetItem();
            treeItem.addChild(treeChild);
            loadItemSettings(child, treeChild);
            if (child.getSubItems() != null) {
                loadSubItemsInfo(child, treeChild);
            }
        }
    }

    private void loadItemSettings(ReportItemType reportItem, QTreeWidgetItem treeItem) {
        NameType name = reportItem.getName();
        treeItem.setText(0, name.getName());
        if (name.getIconPath() != null && !name.getIconPath().isEmpty()) {
            Id iconId = Id.Factory.loadFrom(name.getIconPath());
            QIcon icon = (QIcon) getEnvironment().getApplication().getDefManager().getImage(iconId);
            if (icon != null) {
                treeItem.setIcon(0, icon);
            }
        }

        if (name.getFont() != null && !name.getFont().isEmpty()) {
            QFont font = new QFont();
            if (font.fromString(name.getFont())) {
                treeItem.setFont(0, font);
            }
        }

        if (reportItem.getColor() != null && !reportItem.getColor().isEmpty()) {
            QColor color = new QColor(reportItem.getColor());
            treeItem.setForeground(0, new QBrush(color));
        }
        if (reportItem.getType() != null && !reportItem.getType().isEmpty()) {
            treeItem.setText(1, reportItem.getType());
        }
        if (reportItem.getTime() != null && !reportItem.getTime().isEmpty()) {
            treeItem.setText(3, reportItem.getTime());
        }
        if (reportItem.getPath() != null && !reportItem.getPath().isEmpty()) {
            treeItem.setText(4, reportItem.getPath());
        }

        if (reportItem.getResult() != null) {
            ResultType result = reportItem.getResult();
            if (result.getResultText() != null && !result.getResultText().isEmpty()) {
                treeItem.setText(2, result.getResultText());
            }
            List<TestResultEvent> eventList = new ArrayList<>();
            if (result.getEventList() != null) {
                List<EventListItem> resultEventList = result.getEventList().getEventItemList();
                if (resultEventList.size() > 0) {
                    for (EventListItem eli : resultEventList) {
                        eventList.add(new TestResultEvent(eli.getMessage(), eli.getName(), eli.getStack()));
                    }
                }
            }
            List<ExplorerTraceItem> traceList = new ArrayList<>();
            try {
                if (result.getTraceList() != null) {
                    List<TraceListItem> resultTraceList = result.getTraceList().getTraceItemList();
                    if (resultTraceList.size() > 0) {
                        for (TraceListItem tli : resultTraceList) {        
                            traceList.add(new ExplorerTraceItem(tli.getSeverity(), tli.getMessage()));
                        }
                    }
                }
            } catch (RuntimeException ex) {
                getEnvironment().getTracer().put(ex);
            }

            if (eventList.size() > 0 || traceList.size() > 0) {
                final TestResultItem testResultItem = new TestResultItem(getEnvironment(), eventList, traceList, tree);
                treeItem.setData(2, Qt.ItemDataRole.UserRole, testResultItem);
            }
        }
    }
    private final String JUNIT_PREFIX = "Explorer Tests by ";
    private String connectionInfo = "";

    public XmlObject getJUnitTestReport() {
        connectionInfo = "";
        if (connectionOptions != null) {
            StringBuilder connectionInfoBuilder = new StringBuilder();
            connectionInfoBuilder.append("User name: ");
            connectionInfoBuilder.append("\n");
            connectionInfoBuilder.append(connectionOptions.getUserName());
            connectionInfoBuilder.append("\n");
            connectionInfoBuilder.append("Station name: ");
            connectionInfoBuilder.append("\n");
            connectionInfoBuilder.append(connectionOptions.getStationName());
            connectionInfoBuilder.append("\n");
            connectionInfoBuilder.append("Connection name: ");
            connectionInfoBuilder.append("\n");
            connectionInfoBuilder.append(connectionOptions.getName());
            connectionInfoBuilder.append("\n");
            connectionInfo = connectionInfoBuilder.toString();
        }

        org.radixware.schemas.junittestreport.TestsuitesDocument report = org.radixware.schemas.junittestreport.TestsuitesDocument.Factory.newInstance();
        org.radixware.schemas.junittestreport.TestSuites rootTag = report.addNewTestsuites();

        int tsIndex = 1;
        for (int i = 0, size = tree.topLevelItemCount(); i < size; i++) {
            QTreeWidgetItem topItem = tree.topLevelItem(i);
            org.radixware.schemas.junittestreport.TestSuites.Testsuite suite = rootTag.addNewTestsuite();

            String nameIndex = String.format("%1$05d", tsIndex);
            String nameStr = topItem.text(0).replace(".", "_");
            String name = nameIndex + " " + nameStr;
            final String userName = connectionOptions.getUserName();

            suite.setName(JUNIT_PREFIX + userName + "." + name);
            tsIndex++;

            suite.setTests(new BigInteger(String.valueOf(topItem.childCount())));
            suite.setTime(Double.valueOf(getTotalTime(topItem).toString()) / 1000);
            suite.setFailures(BigInteger.valueOf(getFailuresCount(topItem)));
            suite.setErrors(BigInteger.valueOf(getErrorsCount(topItem)));

            int childIndex = 1;
            int casesIndex = 1;
            for (int c = 0, childCount = topItem.childCount() - 1; c <= childCount; c++) {
                QTreeWidgetItem child = topItem.child(c);
                String operation = child.text(0);
                if (operation != null && !operation.isEmpty()
                        && (operation.equals(TesterConstants.TEST_OPENING.getTitle())
                        || operation.equals(TesterConstants.TEST_CLOSING.getTitle())
                        || operation.equals(TesterConstants.TEST_INSERTIONS.getTitle())
                        || operation.equals(TesterConstants.TEST_FILTERS.getTitle())
                        || operation.equals(TesterConstants.TEST_SINGLE_ENTITY_PROPS.getTitle())
                        || operation.equals(TesterConstants.TEST_CREATION_DIALOG.getTitle()))) {
                    org.radixware.schemas.junittestreport.TestSuites.Testsuite.Testcase testcase = suite.addNewTestcase();
                    addTestCase(testcase, child, casesIndex);
                    casesIndex++;
                } else {
                    addChildSuite(userName, suite.getName(), rootTag, child, "", childIndex);
                    childIndex++;
                }
            }
        }

        return report;
    }

    private void addChildSuite(final String username, final String parentName, org.radixware.schemas.junittestreport.TestSuites rootTag, QTreeWidgetItem item, String packageName, int i) {
        org.radixware.schemas.junittestreport.TestSuites.Testsuite suite = rootTag.addNewTestsuite();

        String name = String.format("%1$05d", i) + " " + item.text(0).replace(".", "_");
        suite.setName(parentName + "/" + name);
        suite.setTests(new BigInteger(String.valueOf(item.childCount())));

        suite.setTime(Double.valueOf(getTotalTime(item).toString()) / 1000);

        suite.setFailures(BigInteger.valueOf(getFailuresCount(item)));
        suite.setErrors(BigInteger.valueOf(getErrorsCount(item)));

        int childIndex = 1;
        int casesIndex = 1;
        for (int c = 0, childCount = item.childCount() - 1; c <= childCount; c++) {
            QTreeWidgetItem child = item.child(c);
            String operation = child.text(0);
            if (operation != null && !operation.isEmpty()
                    && (operation.equals(TesterConstants.TEST_OPENING.getTitle())
                    || operation.equals(TesterConstants.TEST_CLOSING.getTitle())
                    || operation.equals(TesterConstants.TEST_INSERTIONS.getTitle())
                    || operation.equals(TesterConstants.TEST_FILTERS.getTitle())
                    || operation.equals(TesterConstants.TEST_SINGLE_ENTITY_PROPS.getTitle())
                    || operation.equals(TesterConstants.TEST_CREATION_DIALOG.getTitle()))) {
                org.radixware.schemas.junittestreport.TestSuites.Testsuite.Testcase testcase = suite.addNewTestcase();
                addTestCase(testcase, child, casesIndex);
                casesIndex++;
            } else {
                addChildSuite(username, suite.getName(), rootTag, child, packageName + "." + name, childIndex);
                childIndex++;
            }
        }
    }

    private void addTestCase(org.radixware.schemas.junittestreport.TestSuites.Testsuite.Testcase testcase, QTreeWidgetItem item, int c) {
        testcase.setName(String.format("%1$05d", c) + " " + item.text(0));

        EditMaskTimeInterval timeInterval =
                new EditMaskTimeInterval(Scale.MILLIS.longValue(), "hh:mm:ss:zzzz", null, null);
        Long timeLong = timeInterval.convertFromStringToLong(item.text(3));
        Double timeDouble = Double.valueOf(timeLong.toString());
        testcase.setTime(Double.toString(timeDouble / 1000));

        String resultStr = item.text(2);
        final boolean hasFailer = resultStr != null && !resultStr.isEmpty()
                && (resultStr.equals(TesterConstants.RESULT_CLOSING_FAIL.getTitle())
                || resultStr.equals(TesterConstants.RESULT_FAIL_EXCEPTION.getTitle())
                || resultStr.equals(TesterConstants.RESULT_FAIL_INTERRUPTED.getTitle())
                || resultStr.equals(TesterConstants.RESULT_FAIL_TIME_LIMIT.getTitle())
                || resultStr.equals(TesterConstants.RESULT_PROPDIALOG_FAIL.getTitle()));
        if (hasFailer) {
            org.radixware.schemas.junittestreport.FailureDocument.Failure failure = testcase.addNewFailure();
            failure.setType("Failure");
            StringBuilder failerStrBuilder = new StringBuilder();
            failerStrBuilder.append(resultStr);
            failure.setMessage(failerStrBuilder.toString());
        }
        Object data = item.data(2, Qt.ItemDataRole.UserRole);
        if (data != null && data instanceof TestResultItem) {
            TestResultItem resultItem = (TestResultItem) data;
            if (resultItem.errorsLog.size() > 0) {
                StringBuilder systemErrBuilder = new StringBuilder();
                for (TestResultEvent event : resultItem.errorsLog) {
                    systemErrBuilder.append(event.type);
                    systemErrBuilder.append("\n");
                    systemErrBuilder.append(event.message);
                    systemErrBuilder.append("\n");
                    systemErrBuilder.append("Stack: ");
                    systemErrBuilder.append("\n");
                    systemErrBuilder.append(event.stack);
                    systemErrBuilder.append("\n");
                    systemErrBuilder.append("\n");
                }
                org.radixware.schemas.junittestreport.SystemErrDocument.SystemErr systemErr = testcase.addNewSystemErr();
                final XmlCursor cursor = systemErr.newCursor();
                try {
                    cursor.toEndToken();
                    cursor.insertChars(systemErrBuilder.toString());
                } finally {
                    cursor.dispose();
                }
            }

            final StringBuilder systemOutBuilder = new StringBuilder();

            if (hasFailer) {
                systemOutBuilder.append(connectionInfo);
                String path = "";
                if (item.text(4) != null && !item.text(4).isEmpty()) {
                    path = item.text(4);
                } else {
                    if (item.parent() != null) {
                        QTreeWidgetItem parent = item.parent();
                        String parentPath = parent.text(4);
                        for (; (parentPath == null || parentPath.isEmpty()) && parent.parent() != null; parent = parent.parent()) {
                            parentPath = parent.text(4);
                        }
                        if (parentPath == null || parentPath.isEmpty()) {
                            parentPath = getPathFromPreviousItems(parent);
                        }
                        path = parentPath;
                    } else {
                        path = getPathFromPreviousItems(item);
                    }
                }

                if (!path.isEmpty()) {
                    systemOutBuilder.append("\n");
                    systemOutBuilder.append("\n");
                    systemOutBuilder.append(path);
                    systemOutBuilder.append("\n");
                    systemOutBuilder.append("\n");
                }
                if (resultStr.equals(TesterConstants.RESULT_PROPDIALOG_FAIL.getTitle())) {
                    String propertyName = item.text(0);
                    String infoStr = "Property name:";
                    systemOutBuilder.append(infoStr);
                    systemOutBuilder.append(propertyName);
                    systemOutBuilder.append("\n");
                    systemOutBuilder.append("\n");
                }
            }

            if (resultItem.traceLog.size() > 0) {
                for (ClientTraceItem tevent : resultItem.traceLog) {
                    systemOutBuilder.append(tevent.toString());
                    systemOutBuilder.append("\n");
                    systemOutBuilder.append("\n");
                }
                org.radixware.schemas.junittestreport.SystemOutDocument.SystemOut systemOut = testcase.addNewSystemOut();
                final XmlCursor cursor = systemOut.newCursor();
                try {
                    cursor.toEndToken();
                    cursor.insertChars(systemOutBuilder.toString());
                } finally {
                    cursor.dispose();
                }
                //systemOut.getDomNode().setNodeValue(systemOutBuilder.toString());
            }
        }
    }

    private String getPathFromPreviousItems(QTreeWidgetItem parent) {
        String parentPath = parent.text(4);
        if (parentPath.isEmpty()) {
            String parentName = parent.text(0);
            QTreeWidgetItem prevItem = parent.treeWidget().itemAbove(parent);
            if (prevItem != null) {
                String prevPath = prevItem.text(4);
                while ((prevPath == null || prevPath.isEmpty()) && prevItem != null) {
                    prevItem = prevItem.treeWidget().itemAbove(prevItem);
                    prevPath = prevItem != null ? prevItem.text(4) : "";
                }
                return prevPath + "/" + parentName;
            }
        }
        return parentPath;
    }

    private long getErrorsCount(QTreeWidgetItem parent) {
        long res = 0;
        String operation = parent.text(0);
        if (operation != null && !operation.isEmpty()
                && (operation.equals(TesterConstants.TEST_OPENING.getTitle())
                || operation.equals(TesterConstants.TEST_CLOSING.getTitle())
                || operation.equals(TesterConstants.TEST_INSERTIONS.getTitle())
                || operation.equals(TesterConstants.TEST_FILTERS.getTitle())
                || operation.equals(TesterConstants.TEST_PAGE.getTitle()))) {
            Object data = parent.data(2, Qt.ItemDataRole.UserRole);
            if (data != null && data instanceof TestResultItem) {
                TestResultItem resultItem = (TestResultItem) data;
                res += resultItem.errorsLog.size();
                for (ClientTraceItem ti : resultItem.traceLog) {
                    if (ti.getSeverity()==EEventSeverity.ERROR) {
                        res++;
                    }
                }
            }
        }

        if (parent.childCount() > 0) {
            for (int i = 0, size = parent.childCount() - 1; i <= size; i++) {
                res += getErrorsCount(parent.child(i));
            }
        }

        return res;
    }

    private long getFailuresCount(QTreeWidgetItem parent) {
        long res = 0;
        String operation = parent.text(0);
        if (operation != null && !operation.isEmpty()
                && (operation.equals(TesterConstants.TEST_OPENING.getTitle())
                || operation.equals(TesterConstants.TEST_CLOSING.getTitle())
                || operation.equals(TesterConstants.TEST_INSERTIONS.getTitle())
                || operation.equals(TesterConstants.TEST_FILTERS.getTitle())
                || operation.equals(TesterConstants.TEST_PAGE.getTitle()))) {
            String resultStr = parent.text(2);
            if (resultStr != null && !resultStr.isEmpty()
                    && (resultStr.equals(TesterConstants.RESULT_CLOSING_FAIL.getTitle())
                    || resultStr.equals(TesterConstants.RESULT_FAIL_EXCEPTION.getTitle())
                    || resultStr.equals(TesterConstants.RESULT_FAIL_INTERRUPTED.getTitle())
                    || resultStr.equals(TesterConstants.RESULT_FAIL_TIME_LIMIT.getTitle()))) {
                res++;
            }
        }
        if (parent.childCount() > 0) {
            for (int i = 0, size = parent.childCount() - 1; i <= size; i++) {
                res += getFailuresCount(parent.child(i));
            }
        }
        return res;
    }

    private Long getTotalTime(QTreeWidgetItem parent) {
        Long res = Long.valueOf("0");
        String timeStr = parent.text(3);
        if (timeStr != null && !timeStr.isEmpty()) {
            EditMaskTimeInterval timeInterval =
                    new EditMaskTimeInterval(Scale.MILLIS.longValue(), "hh:mm:ss:zzzz", null, null);

            res += timeInterval.convertFromStringToLong(timeStr);
        }
        if (parent.childCount() > 0) {
            for (int i = 0, size = parent.childCount() - 1; i <= size; i++) {
                String childName = parent.child(i).text(0);
                if (childName.equals(TesterConstants.TEST_OPENING.getTitle())
                        || childName.equals(TesterConstants.TEST_CLOSING.getTitle())
                        || childName.equals(TesterConstants.TEST_INSERTIONS.getTitle())
                        || childName.equals(TesterConstants.TEST_FILTERS.getTitle())
                        || childName.equals(TesterConstants.TEST_PAGE.getTitle())) {
                    res += getTotalTime(parent.child(i));
                }
            }
        }
        return res;
    }

    private void saveReportToXml() {
        org.radixware.schemas.testreport.TestReportDocument testReport = org.radixware.schemas.testreport.TestReportDocument.Factory.newInstance();
        org.radixware.schemas.testreport.TestReportType rootTag = testReport.addNewTestReport();

        rootTag.setTotalTests(Long.valueOf(allPassedTestCounterLabel.text()));
        rootTag.setPassedTests(Long.valueOf(allGoodTestCounterLabel.text()));
        rootTag.setErroniousTests(Long.valueOf(allFailedTestCounterLabel.text()));
        rootTag.setWarnedTests(Long.valueOf(allWarnedTestCounterLabel.text()));

        rootTag.setResultFilter(resultFilterBox.getCurrentItem());

        TestsOptions options = getOptions();
        rootTag.setTestInserts(options.testInserts);
        rootTag.setTestFilters(options.testFilters);
        rootTag.setTestPages(options.testPages);
        rootTag.setTestPropDialog(options.testPropDialog);
        rootTag.setTestCreationDialog(options.testCreationDialog);

        rootTag.setInsertsCount(BigInteger.valueOf(options.inserts));
        rootTag.setFiltersCount(BigInteger.valueOf(options.filtersCount));
        rootTag.setPagesCount(BigInteger.valueOf(options.pagesCount));

        rootTag.setOpeningTime(options.openingTimeBoundary);
        rootTag.setClosingTime(options.closingTimeBoundary);
        rootTag.setInsertsTime(options.insertsTimeBoundary);
        rootTag.setFiltersTime(options.filtersTimeBoundary);
        rootTag.setPagesTime(options.pagesTimeBoundary);
        rootTag.setPropertyDialogTime(options.propDialogTimeBoundary);
        rootTag.setCreationDialogTime(options.creationDialogTimeBoundary);

        for (int i = 0, size = tree.topLevelItemCount(); i < size; i++) {
            QTreeWidgetItem topItem = tree.topLevelItem(i);
            ReportItemType reportItem = rootTag.addNewItem();

            saveItemSettings(topItem, reportItem);

            ReportItemsList children = reportItem.addNewSubItems();
            for (int c = 0, childCount = topItem.childCount(); c < childCount; c++) {
                ReportItemType childItem = children.addNewItem();
                saveChildItem(childItem, topItem.child(c));
            }
        }

        QFileDialog dialog = new QFileDialog(this, tr("Save report"), QDir.homePath(), tr("XML Document (*.xml)"));
        dialog.setAcceptMode(QFileDialog.AcceptMode.AcceptSave);

        if (QDialog.DialogCode.resolve(dialog.exec()).equals(QDialog.DialogCode.Accepted)) {
            File report_file = new File(dialog.selectedFiles().get(0));
            report_file.setWritable(true);
            try {
                XmlOptions opt = new XmlOptions();
                opt.setSaveAggressiveNamespaces();
                testReport.save(report_file, opt);
            } catch (IOException io) {
                QMessageBox.critical(this, Application.translate("TesterDialog", "Input/Output exception"), io.getMessage());
            }
        }
    }

    private void saveChildItem(ReportItemType reportItem, QTreeWidgetItem treeItem) {
        saveItemSettings(treeItem, reportItem);

        Object data = treeItem.data(2, Qt.ItemDataRole.UserRole);
        if (data != null && data instanceof TestResultItem) {
            TestResultItem result = (TestResultItem) data;
            if (result.errorsLog != null && result.errorsLog.size() > 0
                    || result.traceLog != null && result.traceLog.size() > 0) {
                ResultType resultItem = reportItem.addNewResult();
                resultItem.setResultText(treeItem.text(2));

                if (result.errorsLog != null && result.errorsLog.size() > 0) {
                    EventListType eventList = resultItem.addNewEventList();

                    for (TestResultEvent e : result.errorsLog) {
                        EventListItem eItem = eventList.addNewEventItem();
                        eItem.setMessage(e.message);
                        eItem.setName(e.type);
                        if (e.stack != null && !e.stack.isEmpty()) {
                            eItem.setStack(e.stack);
                        }
                    }
                }

                if (result.traceLog != null && result.traceLog.size() > 0) {
                    TraceListType traceList = resultItem.addNewTraceList();

                    for (ClientTraceItem t : result.traceLog) {
                        TraceListItem tItem = traceList.addNewTraceItem();
                        tItem.setMessage(t.getFormattedMessage());
                        tItem.setSeverity(t.getSeverity());
                    }
                }
            }
        } else if (treeItem.text(2) != null && !treeItem.text(2).isEmpty()) {
            ResultType resultItem = reportItem.addNewResult();
            resultItem.setResultText(treeItem.text(2));
        }

        if (treeItem.childCount() > 0) {
            ReportItemsList children = reportItem.addNewSubItems();
            for (int c = 0, childCount = treeItem.childCount(); c < childCount; c++) {
                ReportItemType childItem = children.addNewItem();
                saveChildItem(childItem, treeItem.child(c));
            }
        }
    }

    private void saveItemSettings(QTreeWidgetItem treeItem, ReportItemType reportItem) {
        saveItemName(treeItem, reportItem);
        reportItem.setColor(treeItem.foreground(0).color().name());
        reportItem.setType(treeItem.text(1));
        reportItem.setTime(treeItem.text(3));
        reportItem.setPath(treeItem.text(4));
    }

    private void saveItemName(QTreeWidgetItem treeItem, ReportItemType reportItem) {
        NameType nameType = reportItem.addNewName();
        nameType.setName(treeItem.text(0));
        if (treeItem.icon(0) != null) {
            final Id imageId = ImageManager.findCachedIconIdByCacheKey(treeItem.icon(0).cacheKey());
            if (imageId != null) {
                String imageIdStr = imageId.toString();

                if (!imageIdStr.isEmpty()) {
                    nameType.setIconPath(imageIdStr);
                }
            }
        }
        nameType.setFont(treeItem.font(0).toString());
    }

    private void onClosing(Integer result) {
        saveSettings();
    }

    private void saveSettings() {
        ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();

        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(mainTrKey);

        settings.writeQByteArray(mainTrKey + keyGeometryPostfix, saveGeometry());
        settings.writeString(resultFilterKey, resultFilterBox.getCurrentItem());

        settings.endGroup();
        settings.endGroup();
    }

    private void loadSettings(String fileName) {
        ExplorerSettings settings = new ExplorerSettings(getEnvironment(), fileName, Format.IniFormat);
        loadSettings(settings);
    }

    private void loadSettings(ExplorerSettings settings) {
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(mainTrKey);

        if (settings.contains(mainTrKey + keyGeometryPostfix)) {
            restoreGeometry(settings.readQByteArray(mainTrKey + keyGeometryPostfix));
        }
        TestsOptions options = getOptions();
        settings.beginGroup(TesterSettingsDialog.dialogKey);
        options.openingTimeBoundary = loadTimeSettingsForKey(settings, TesterSettingsDialog.OPN_TIME);
        options.insertsTimeBoundary = loadTimeSettingsForKey(settings, TesterSettingsDialog.INS_TIME);
        options.filtersTimeBoundary = loadTimeSettingsForKey(settings, TesterSettingsDialog.FLT_TIME);
        options.pagesTimeBoundary = loadTimeSettingsForKey(settings, TesterSettingsDialog.PGS_TIME);
        options.closingTimeBoundary = loadTimeSettingsForKey(settings, TesterSettingsDialog.CLS_TIME);
        options.propDialogTimeBoundary = loadTimeSettingsForKey(settings, TesterSettingsDialog.PROPDLG_TIME);
        options.creationDialogTimeBoundary = loadTimeSettingsForKey(settings, TesterSettingsDialog.CREATION_TIME);

        options.testInserts = loadUsageSettingsForKey(settings, TesterSettingsDialog.TEST_INS);
        options.testFilters = loadUsageSettingsForKey(settings, TesterSettingsDialog.TEST_FLT);
        options.testPages = loadUsageSettingsForKey(settings, TesterSettingsDialog.TEST_PGS);
        options.testPropDialog = loadUsageSettingsForKey(settings, TesterSettingsDialog.TEST_PROPDLG);
        options.testCreationDialog = loadUsageSettingsForKey(settings, TesterSettingsDialog.TEST_CREATION);

        long ins_count = loadCountSettingsForKey(settings, TesterSettingsDialog.INS_COUNT);
        options.inserts = options.testInserts ? (ins_count <= 0 ? -1 : ins_count) : 0;

        long filters_count = loadCountSettingsForKey(settings, TesterSettingsDialog.FLT_COUNT);
        options.filtersCount = options.testFilters ? (filters_count <= 0 ? -1 : filters_count) : 0;

        long pages_count = loadCountSettingsForKey(settings, TesterSettingsDialog.PGS_COUNT);
        options.pagesCount = options.testPages ? (pages_count <= 0 ? -1 : pages_count) : 0;

        settings.endGroup();

        String resultFilter = settings.readString(resultFilterKey, TesterConstants.RESUL_FILTER_ALL.getTitle());
        resultFilterBox.setCurrentItem(resultFilter);

        settings.endGroup();
        settings.endGroup();
    }

    private void loadSettings() {
        final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
        loadSettings(settings);
    }

    private long loadTimeSettingsForKey(ExplorerSettings settings, final String key) {
        if (settings.contains(key)) {
            String timeStr = settings.readString(key, "1000");
            Long time = Long.valueOf(timeStr);
            return time;
        }
        return 1000;
    }

    private boolean loadUsageSettingsForKey(ExplorerSettings settings, final String key) {
        if (settings.contains(key)) {
            Boolean usage = settings.readBoolean(key, false);
            return usage;
        }
        return false;
    }

    private long loadCountSettingsForKey(ExplorerSettings settings, final String key) {
        if (settings.contains(key)) {
            String countStr = settings.readString(key, "0");
            Long count = Long.valueOf(countStr);
            return count;
        }
        return 0;
    }
}
