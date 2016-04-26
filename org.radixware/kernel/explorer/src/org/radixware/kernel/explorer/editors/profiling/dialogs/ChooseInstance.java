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

package org.radixware.kernel.explorer.editors.profiling.dialogs;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.profiling.ProfilerWidget;
import org.radixware.kernel.explorer.editors.profiling.ProfilerWidget.ProfilEditorIcons;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class ChooseInstance extends ExplorerDialog {

    private QListWidget listWidget;
    private final List<Long> selectedInstances;
    private final Map<Long, String> instances;
    private QToolButton btnCheck;
    private QToolButton btnUncheck;

    public ChooseInstance(final ProfilerWidget editor) {
        super(editor.getEnvironment(), editor, "ChoceInstatcesForProfilerWidget");
        //this.editor=editor;                
        this.setWindowTitle(Application.translate("ProfilerDialog", "Select Instances"));
        this.instances = editor.getInstances();
        this.selectedInstances = new ArrayList<>();
        if (editor.getSelectedInstanceIds() != null) {
            selectedInstances.addAll(editor.getSelectedInstanceIds());
        }
        createUI();
    }

    private void createUI() {
        this.setMinimumSize(200, 200);

        final QToolBar toolBar = createToolBar();
        final QLabel lb = new QLabel(Application.translate("ProfilerDialog", "Instance List:"), this);
        createInstanceList();

        dialogLayout().setContentsMargins(10, 0, 10, 10);
        dialogLayout().addWidget(toolBar);
        dialogLayout().addWidget(lb);
        dialogLayout().addWidget(listWidget);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        //this.setWindowModality(WindowModality.WindowModal);
        this.setVisible(true);
    }

    private QToolBar createToolBar() {
        final QToolBar toolBar = new QToolBar(this);
        btnCheck = createToolButton(Application.translate("ProfilerDialog", "Select All"), ExplorerIcon.getQIcon(ProfilEditorIcons.CHECK_ALL), "checkAll()");
        btnUncheck = createToolButton(Application.translate("ProfilerDialog", "Unselect All"), ExplorerIcon.getQIcon(ProfilEditorIcons.UNCHECK_ALL), "uncheckAll()");
        toolBar.addWidget(btnCheck);
        toolBar.addWidget(btnUncheck);
        return toolBar;
    }

    private QToolButton createToolButton(final String toolTip, final QIcon icon, final String strClicked) {
        final QToolButton btn = new QToolButton();
        btn.setAutoRaise(true);
        btn.setToolTip(toolTip);
        btn.setIcon(icon);
        btn.setIconSize(new QSize(20, 20));
        btn.clicked.connect(this, strClicked);
        return btn;
    }

    @SuppressWarnings("unused")
    private void checkAll() {
        changeCheck(true);
    }

    @SuppressWarnings("unused")
    private void uncheckAll() {
        changeCheck(false);
    }

    private void changeCheck(final boolean isChecked) {
        for (int i = 0; i < listWidget.count(); i++) {
            listWidget.item(i).setCheckState(isChecked ? CheckState.Checked : CheckState.Unchecked);
        }
    }

    private void createInstanceList() {
        listWidget = new QListWidget(this);
        listWidget.itemChanged.connect(this, "onItemChanged(QListWidgetItem)");

        final SortedSet<Long> sortedset = new TreeSet<>(instances.keySet());
        final Iterator<Long> it = sortedset.iterator();

        while (it.hasNext()) {
            final Long id = it.next();
            final InstanceWidgetItem item = new InstanceWidgetItem(id);
            final String title = instances.get(id);
            item.setText(title);
            final CheckState checkState = selectedInstances.isEmpty() || selectedInstances.contains(id) ? CheckState.Checked : CheckState.Unchecked;
            item.setCheckState(checkState);
            listWidget.addItem(item);
        }
    }

    @SuppressWarnings("unused")
    private void onItemChanged(final QListWidgetItem widgetItem) {
        for (int i = 0, size = listWidget.count(); i < size; i++) {
            final QListWidgetItem item = listWidget.item(i);
            if (item.checkState() == CheckState.Checked) {
                getButton(EDialogButtonType.OK).setEnabled(true);
                return;
            }
        }
        getButton(EDialogButtonType.OK).setEnabled(false);
    }

    @Override
    public void accept() {
        selectedInstances.clear();
        final int size = listWidget.count();
        for (int i = 0; i < size; i++) {
            final InstanceWidgetItem item = (InstanceWidgetItem) listWidget.item(i);
            if (item.checkState() == CheckState.Checked) {
                final Long inst = item.getInsatnce();
                selectedInstances.add(inst);
            }
        }
        super.accept();
    }

    public List<Long> getSelectedInstance() {
        return selectedInstances.isEmpty() ? null : selectedInstances;
    }

    private class InstanceWidgetItem extends QListWidgetItem {

        private final Long instanceId;

        InstanceWidgetItem(final Long instanceId) {
            this.instanceId = instanceId;
        }

        Long getInsatnce() {
            return instanceId;
        }
    }
}
