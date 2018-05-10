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

package org.radixware.kernel.explorer.editors.profiling;

import org.radixware.kernel.explorer.editors.profiling.dialogs.ChooseInstance;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QMessageBox.Icon;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import java.util.List;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.explorer.editors.profiling.ProfilerWidget.ProfilEditorIcons;
import org.radixware.kernel.explorer.editors.profiling.dialogs.FilterByPercentDialog;
import org.radixware.kernel.explorer.editors.profiling.dialogs.SaveToFileDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;



public class ToolBar {

    private QToolBar toolBar;
    private ProfilerWidget editor;
    private QToolButton btnReread;
    private QToolButton btnRemoveSelection;
    private QToolButton btnClear;
    private QToolButton btnShowSummary;
    private QToolButton btnSaveToFile;
    private QToolButton btnClassificationMode;
    private ValEditor<String> edSystemInstances;
    private ValEditor<String> edFilterByPercent;

    public ToolBar(final ProfilerWidget editor) {
        this.editor = editor;
        createToolBar();
    }

    public final void createToolBar() {
        toolBar = new QToolBar(editor);
        toolBar.setObjectName("EditorToolBar");

        String toolTip = Application.translate("ProfilerDialog", "Reread from Database");
        btnReread = createToolBtn(toolTip, "actReread()", "btnReread", ExplorerIcon.getQIcon(ProfilEditorIcons.REREAD));

        toolTip = Application.translate("ProfilerDialog", "Remove Checked Items");
        btnRemoveSelection = createToolBtn(toolTip, "actRemoveSelection()", "btnRemoveSelection", ExplorerIcon.getQIcon(ProfilEditorIcons.DEL_ITEMS));
        setRemoveSelectionEnable(false);

        toolTip = Application.translate("ProfilerDialog", "Clear");
        btnClear = createToolBtn(toolTip, "actClear()", "btnClear", ExplorerIcon.getQIcon(ProfilEditorIcons.DELETE_ALL));

        toolTip = Application.translate("ProfilerDialog", "Show Total Statistics");
        btnShowSummary = createToolBtn(toolTip, "actShowSummary()", "btnShowSummary", ExplorerIcon.getQIcon(ProfilEditorIcons.SUM_MODE));

        toolTip = Application.translate("ProfilerDialog", "Export");
        btnSaveToFile = createToolBtn(toolTip, "actSaveToFile()", "btnSaveToFile", ExplorerIcon.getQIcon(ProfilEditorIcons.SAVE));
        setSaveToFileEnabled(false);

        final QLabel lbInstanceFilter = new QLabel(" " + Application.translate("ProfilerDialog", "Filter by Instances:"));
        edSystemInstances = new ValEditor<>(editor.getEnvironment(), toolBar, new EditMaskNone(), false, true);
        edSystemInstances.setObjectName("editLine");
        edSystemInstances.setReadOnly(true);
        final QAction action = new QAction(toolBar);
        action.triggered.connect(this, "actFilterByInstance()");
        edSystemInstances.valueChanged.connect(this, "valInstanceFilterChanged()");
        edSystemInstances.addButton("...", action);
        setInstancesValue(editor.getSelectedInstanceIds());

        final QLabel lbPercentFilter = new QLabel("  " + Application.translate("ProfilerDialog", "Filter by Duration (%):"));
        edFilterByPercent = new ValEditor<>(editor.getEnvironment(), toolBar, new EditMaskNone(), false, true);
        edFilterByPercent.setObjectName("editLine");
        edFilterByPercent.setReadOnly(true);
        final QAction actPercentFilter = new QAction(toolBar);
        actPercentFilter.triggered.connect(this, "actFilterByPercent()");
        edFilterByPercent.valueChanged.connect(this, "valPercentFilterChanged()");
        edFilterByPercent.addButton("...", actPercentFilter);
        setPercentFilterValue(editor.getPercentFilter());

        toolTip =/*Environment.translate("ProfilerDialog","Change Classification Mode")+": "+*/ Application.translate("ProfilerDialog", "Show Absolute Value");
        btnClassificationMode = createToolBtn(toolTip, "actChangeClassificationMode()", "btnClassificationMode", ExplorerIcon.getQIcon(ProfilEditorIcons.PERSENT_MODE));
        btnClassificationMode.setCheckable(true);
        btnClassificationMode.setChecked(true);

        toolBar.addWidget(btnReread);
        toolBar.addWidget(btnRemoveSelection);
        toolBar.addWidget(btnClear);
        toolBar.addWidget(btnShowSummary);
        toolBar.addSeparator();
        toolBar.addWidget(btnSaveToFile);
        toolBar.addSeparator();
        toolBar.addWidget(lbInstanceFilter);
        toolBar.addWidget(edSystemInstances);
        toolBar.addWidget(lbPercentFilter);
        toolBar.addWidget(edFilterByPercent);
        toolBar.addSeparator();
        toolBar.addWidget(btnClassificationMode);
    }

    @SuppressWarnings("unused")
    private void valInstanceFilterChanged() {
        final QFontMetrics fm = edSystemInstances.fontMetrics();
        int width = fm.boundingRect(edSystemInstances.getValue()).width() + 33;
        if (width > 500) {
            width = 500;
            edSystemInstances.setToolTip(edSystemInstances.getValue());
        }
        edSystemInstances.setFixedWidth(width);
    }

    @SuppressWarnings("unused")
    private void valPercentFilterChanged() {
        final QFontMetrics fm = edFilterByPercent.fontMetrics();
        final int width = fm.boundingRect(edFilterByPercent.getValue()).width() + 33;
        edFilterByPercent.setFixedWidth(width);
    }

    public final void setRemoveSelectionEnable(final boolean isEnable) {
        btnRemoveSelection.setEnabled(isEnable);
    }

    public final void setSaveToFileEnabled(final boolean isEnable) {
        btnSaveToFile.setEnabled(isEnable);
    }

    public void setToolBarEnable(final boolean isEnable) {
        btnShowSummary.setEnabled(isEnable);
        btnClear.setEnabled(isEnable);
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
    private void actReread() {
        editor.reread();
    }

    @SuppressWarnings("unused")
    private void actRemoveSelection() {
        if (exposeMessageInformation()) {
            editor.removeItems(true);
            setRemoveSelectionEnable(false);
        }
    }

    @SuppressWarnings("unused")
    private void actClear() {
        if (exposeMessageInformation()) {
            editor.clearTree();
            setRemoveSelectionEnable(false);
        }
    }

    private boolean exposeMessageInformation() {
        final String title = Application.translate("ExplorerDialog", "Question");
        final String message = Application.translate("ExplorerDialog", "Do you really want to delete time sections from  Database?");
        final QMessageBox.StandardButtons buttons = new QMessageBox.StandardButtons();
        buttons.set(QMessageBox.StandardButton.Yes);
        buttons.set(QMessageBox.StandardButton.No);
        buttons.set(QMessageBox.StandardButton.Cancel);
        final QMessageBox.StandardButton answer = Application.messageBox(title, message, Icon.NoIcon, buttons);
        return answer.equals(QMessageBox.StandardButton.Yes);
    }

    @SuppressWarnings("unused")
    private void actShowSummary() {
        editor.chageMode();
    }

    @SuppressWarnings("unused")
    private void actSaveToFile() {
        final SaveToFileDialog saveToFileDialog = new SaveToFileDialog(editor);
        saveToFileDialog.exec();
    }

    @SuppressWarnings("unused")
    private void actFilterByInstance() {
        final ChooseInstance chooseInst = new ChooseInstance(editor);
        if (chooseInst.exec() == QDialog.DialogCode.Accepted.value()) {
            final List<Long> selectedInstances = chooseInst.getSelectedInstance();
            setInstancesValue(selectedInstances);
            editor.setSelectedInstanceIds(selectedInstances);
        }
    }

    @SuppressWarnings("unused")
    private void actFilterByPercent() {
        final FilterByPercentDialog filterByPercentDialog = new FilterByPercentDialog(editor);
        if (filterByPercentDialog.exec() == 1) {
            final Double filterByPercentVal = filterByPercentDialog.getPercentFilterVal();
            setPercentFilterValue(filterByPercentVal);
            editor.setPercentFilter(filterByPercentVal);
        }
    }

    @SuppressWarnings("unused")
    private void actChangeClassificationMode() {
        editor.getCurTree().getTree().setSortingEnabled(false);
        
        final boolean isPercentMode = btnClassificationMode.isChecked();
        final String toolTip = isPercentMode ? /*Environment.translate("ProfilerDialog","Change Classification Mode")+
                ": "+*/ Application.translate("ProfilerDialog", "Show Absolute Value")
                : /*Environment.translate("ProfilerDialog","Change Classification Mode")+
                ": "+*/ Application.translate("ProfilerDialog", "Show Percent");
        btnClassificationMode.setToolTip(toolTip);
        editor.setClassificationMode(isPercentMode);
        
        editor.getCurTree().getTree().setSortingEnabled(true);
    }

    private void setInstancesValue(final List<Long> selectedInstances) {
        if ((selectedInstances == null) || (selectedInstances.size() == editor.getInstances().size())) {
            edSystemInstances.setValue(Application.translate("ProfilerDialog", "<all instances>"));
        } else {
            final String s = intsListToStr(selectedInstances);
            edSystemInstances.setValue(s);
        }
        edSystemInstances.adjustSize();
    }

    private void setPercentFilterValue(final Double val) {
        if (val == 0.0) {
            edFilterByPercent.setValue(Application.translate("ProfilerDialog", "<not defined>"));
        } else {
            final String s = " > " + val.toString() + "% ";
            edFilterByPercent.setValue(s);
        }
        edFilterByPercent.adjustSize();
    }

    private String intsListToStr(final List<Long> selectedInstances) {
        String res = "";
        final int size = selectedInstances.size();
        for (int i = 0; i < size; i++) {
            res += editor.getInstances().get(selectedInstances.get(i));
            if (i < size - 1) {
                res += ";";
            }
        }
        return res;
    }

    public QToolBar getToolBar() {
        return toolBar;
    }
}
