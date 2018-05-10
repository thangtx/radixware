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
package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QAbstractItemView.SelectionBehavior;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QHeaderView.ResizeMode;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.editors.jmleditor.JmlProcessor;
import org.radixware.kernel.explorer.editors.jmleditor.autosave.UserFuncAutosaveManager;
import org.radixware.kernel.explorer.editors.xscmleditor.Highlighter;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.schemas.adsdef.UserFuncSourceVersions;
import org.radixware.schemas.xscml.JmlType;

public class AutosaveVersionsDialog extends ExplorerDialog {

    private final static int CREATE_DATETIME_COLUMN = 0;
    private final static int AUTHOR_COLUMN = 1;
    private final static JmlType EMPTY_SRC = JmlType.Factory.newInstance();
    

    private QTableWidget tableWidget;
    private final QPushButton btnDelete=new QPushButton();
    private final Map<String, UserFuncSourceVersions.SourceVersion> savedVersions;
    private final AdsUserFuncDef userFunc;
    private final Pid ufPid;
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm.ss.SSS");

    private final XscmlEditor previewWidget;
    private JmlType selectedVersionJml;

    public AutosaveVersionsDialog(final JmlEditor editor) {
        super(editor.getEnvironment(), editor);
        this.setWindowTitle(Application.translate("JmlEditor", Application.translate("JmlEditor", "Local History")));
        this.ufPid = editor.getActionProvider().getPid();
        savedVersions = UserFuncAutosaveManager.getInstance(getEnvironment()).getVersionsMap(this.ufPid);
        this.userFunc = editor.getUserFunc();
        previewWidget = createPreviewWidget(editor);
        createUI();
    }

    private void createUI() {
        this.setMinimumSize(100, 100);

        createTable();

        final QHBoxLayout tableLayout = new QHBoxLayout();
        btnDelete.setObjectName("btnDeleteVersion");
        btnDelete.setParent(this);
        btnDelete.setToolTip(Application.translate("JmlEditor", "Delete Version"));
        btnDelete.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.DELETE));
        btnDelete.setIconSize(new QSize(22, 22));
        btnDelete.setShortcut(QKeySequence.StandardKey.Delete);
        btnDelete.clicked.connect(this, "deleteVersion()");
        btnDelete.setEnabled(!savedVersions.isEmpty());

        tableLayout.addWidget(tableWidget);
        tableLayout.addWidget(btnDelete, 0, Qt.AlignmentFlag.AlignTop);
        dialogLayout().addLayout(tableLayout);
        
        dialogLayout().addWidget(previewWidget);  

        final QAbstractButton btnLoadAsCurrent = (QAbstractButton) addButton(EDialogButtonType.OK);
        btnLoadAsCurrent.setObjectName("btnLoadAsCurrent");
        btnLoadAsCurrent.clicked.connect(this, "loadAsCurrent()");
        btnLoadAsCurrent.setText(Application.translate("JmlEditor", "Load as Current"));
        btnLoadAsCurrent.setEnabled(false);

        addButton(EDialogButtonType.CANCEL);
        rejectButtonClick.connect(this, "reject()");
        
        tableWidget.currentItemChanged.connect(this, "tableCellChanged()");
        tableWidget.selectRow(0);
    }

    private XscmlEditor createPreviewWidget(JmlEditor editor) {
        final JmlProcessor converter = new JmlProcessor(editor);
        final XscmlEditor xscmlEditor = new XscmlEditor(getEnvironment(), converter, this);
        new Highlighter(getEnvironment(), xscmlEditor, converter, "org.radixware.explorer/S_E/SYNTAX_JML/");
        xscmlEditor.setReadOnly(true);
        return xscmlEditor;
    }

    private void openJmlPreview(JmlType jml) {
        final Jml versionJml = Jml.Factory.loadFrom(userFunc, jml, "user_func_preview");
        previewWidget.getTagConverter().open(versionJml);
        previewWidget.open(userFunc);
        versionJml.delete();
    }
    
    @SuppressWarnings("unused")
    private void deleteVersion() {
        if (tableWidget.currentItem() != null) {
            if (getEnvironment().messageConfirmation(
                    Application.translate("JmlEditor", "Confirm Delete Version"),
                    Application.translate("JmlEditor", "Delete selected version?"))) {
                String verName = (String) tableWidget.item(tableWidget.currentRow(), CREATE_DATETIME_COLUMN).data(Qt.ItemDataRole.UserRole);
                savedVersions.remove(verName);
                UserFuncAutosaveManager.getInstance(getEnvironment()).removeVersion(ufPid, verName);
                tableWidget.removeRow(tableWidget.currentRow());
                tableWidget.selectRow(0);
            }
        }
    }

    @SuppressWarnings("unused")
    private void tableCellChanged() {
        if (tableWidget.currentItem() != null) {
            openJmlPreview(getSelectedJmlImpl());
        } else {
            openJmlPreview(EMPTY_SRC);
        }
        getButton(EDialogButtonType.OK).setEnabled(tableWidget.currentItem() != null);
        btnDelete.setEnabled(tableWidget.currentItem() != null);
        tableWidget.setFocus();
    }
    
    protected void createTable() {
        tableWidget = new QTableWidget(this);
        tableWidget.setEditTriggers(QAbstractItemView.EditTrigger.NoEditTriggers);
        tableWidget.setColumnCount(2);
        tableWidget.setHorizontalHeaderLabels(Arrays.asList(
                Application.translate("JmlEditor", "Created/Modified"),
                Application.translate("JmlEditor", "Author"))
        );
        tableWidget.setSelectionBehavior(SelectionBehavior.SelectRows);
        tableWidget.verticalHeader().setVisible(false);
        tableWidget.setSelectionMode(SelectionMode.SingleSelection);

        fillTable();
        
        tableWidget.resizeColumnsToContents();
        tableWidget.horizontalHeader().setResizeMode(CREATE_DATETIME_COLUMN, ResizeMode.ResizeToContents);
        tableWidget.horizontalHeader().setResizeMode(AUTHOR_COLUMN, ResizeMode.Stretch);
    }

    private void fillTable() {
        if (savedVersions != null) {
            int rowCount = 0;
            for (String versionName : savedVersions.keySet()) {
                addRow(savedVersions.get(versionName), rowCount++);
            }
            tableWidget.sortByColumn(CREATE_DATETIME_COLUMN, Qt.SortOrder.AscendingOrder);
        }
    }

    private void finishEdit() {
        final QTableWidgetItem curItem = tableWidget.currentItem();
        tableWidget.setCurrentItem(null);
        tableWidget.setCurrentItem(curItem);
    }

    @Override
    public void accept() {
        finishEdit();
        super.accept();
    }

    @Override
    public void reject() {
        finishEdit();
        super.reject();
    }
    
    private JmlType getSelectedJmlImpl() {
        String verName = (String) tableWidget.item(tableWidget.currentRow(), CREATE_DATETIME_COLUMN).data(Qt.ItemDataRole.UserRole);
        return savedVersions.get(verName);
    }
    
    public JmlType getSelectedJml() {
        return (JmlType) selectedVersionJml.copy();
    }

    @SuppressWarnings("unused")
    private void loadAsCurrent() {
        if (tableWidget.currentItem() != null) {
            selectedVersionJml = getSelectedJmlImpl();
        }
        if (getEnvironment().messageConfirmation(
                Application.translate("JmlEditor", "Confirm Load Current Version"),
                Application.translate("JmlEditor", "Load selected version? Current version will be lost."))) {
            accept();
        }
    }

    protected void addRow(UserFuncSourceVersions.SourceVersion srcVersion, final int rowCount) {
        tableWidget.insertRow(rowCount);

        tableWidget.setItem(rowCount, AUTHOR_COLUMN, new QTableWidgetItem(srcVersion.getLastUpdateUserName()));

        final Calendar creationTime = srcVersion.getLastUpdateTime();
        final CreationDataTimeItem itemCreateDateTime = new CreationDataTimeItem(creationTime);
        itemCreateDateTime.setData(Qt.ItemDataRole.UserRole, srcVersion.getName());
        tableWidget.setItem(rowCount, CREATE_DATETIME_COLUMN, itemCreateDateTime);
    }

    private class CreationDataTimeItem extends QTableWidgetItem {

        private final long creationDateTime;

        public CreationDataTimeItem(Calendar date) {
            super(DATE_FORMAT.format(date.getTime()));
            creationDateTime = date.getTimeInMillis();
        }

        @Override
        public boolean operator_less(final QTableWidgetItem qtwi) {
            if (qtwi instanceof CreationDataTimeItem) {
                return Long.compare(creationDateTime, ((CreationDataTimeItem) qtwi).creationDateTime) > 0;
            }
            return super.operator_less(qtwi);
        }
    }

}
