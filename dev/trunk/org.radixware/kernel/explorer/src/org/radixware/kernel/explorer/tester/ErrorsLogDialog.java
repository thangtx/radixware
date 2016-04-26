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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QSplitter;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.dialogs.trace.TraceFilter;
import org.radixware.kernel.explorer.dialogs.trace.TraceListWidget;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceItem;

import org.radixware.kernel.explorer.tester.tests.TestResultEvent;


class ErrorsLogDialog extends ExplorerDialog {

    private final static String DIALOG_KEY = "TesterDialog-ErrorsDialog";
    private final static String GEOMETRY_KEY = "Geometry";

    ErrorsLogDialog(IClientEnvironment environment, QWidget p, List<TestResultEvent> list, List<ExplorerTraceItem> traceList) {
        super(environment, p);
        this.setMinimumSize(450, 200);
        setWindowTitle(environment.getMessageProvider().translate("TesterDialog", "Events On Test Process"));

        QVBoxLayout l = dialogLayout();
        final ExplorerSettings settings = (ExplorerSettings)getEnvironment().getConfigStore();
        if (list.size() > 0 && traceList.size() > 0) {
            QWidget table = fillTable(list);

            final TraceListWidget traceListWidget = new TraceListWidget(environment, new TraceFilter(environment,this), this);
            traceListWidget.addTraceItems(traceList);

            QSplitter splitter = new QSplitter(Qt.Orientation.Vertical);
            splitter.addWidget(table);
            splitter.addWidget(traceListWidget);


            l.addWidget(splitter);
        } else if (list.size() > 0) {
            QWidget table = fillTable(list);

            l.addWidget(table);
        } else if (traceList.size() > 0) {
            final TraceListWidget traceListWidget = new TraceListWidget(environment, new TraceFilter(environment,this), this);
            traceListWidget.addTraceItems(traceList);
            l.addWidget(traceListWidget);
        }


        QPushButton b = new QPushButton(getEnvironment().getMessageProvider().translate("TesterDialog", "OK"));
        b.clicked.connect(this, "forceClose()");
        b.setFixedSize(70, 25);
        l.addWidget(b);
        l.setAlignment(b, AlignmentFlag.AlignRight);
        l.setContentsMargins(2, 2, 2, 2);

        this.finished.connect(this, "onClosing(Integer)");

        loadSettings();
    }

    private QWidget fillTable(List<TestResultEvent> list) {
        QFrame frame = new QFrame(this);
        QVBoxLayout layout = new QVBoxLayout(frame);
        layout.setContentsMargins(2, 2, 2, 2);

        for (TestResultEvent item : list) {
            QLabel label = new QLabel(item.type + ": ");
            label.setSizePolicy(Policy.Preferred, Policy.Maximum);
            layout.addWidget(label);

            QTextEdit textEdit = new QTextEdit();
            textEdit.setSizePolicy(Policy.Expanding, Policy.Expanding);
            QTextCursor cursor = textEdit.textCursor();
            cursor.insertText(item.message);
            cursor.insertBlock();
            cursor.insertBlock();

            if (item.stack != null && !item.stack.isEmpty()) {
                cursor.insertText("Stack:");
                cursor.insertBlock();
                cursor.insertBlock();
                cursor.insertText(item.stack);
            }

            QTextCursor newCursor = new QTextCursor(textEdit.document());
            textEdit.setTextCursor(newCursor);

            layout.addWidget(textEdit);
        }

        return frame;
    }

    private void loadSettings() {
        final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(TesterWindow.mainTrKey);
        settings.beginGroup(DIALOG_KEY);

        if (settings.contains(DIALOG_KEY + GEOMETRY_KEY)) {
            restoreGeometry(settings.readQByteArray(DIALOG_KEY));
        }

        settings.endGroup();
        settings.endGroup();
        settings.endGroup();
    }

    private void onClosing(Integer result) {
        saveSettings();
    }

    private void saveSettings() {
        ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(TesterWindow.mainTrKey);
        settings.beginGroup(DIALOG_KEY);

        settings.writeQByteArray(DIALOG_KEY + GEOMETRY_KEY, saveGeometry());

        settings.endGroup();
        settings.endGroup();
        settings.endGroup();
    }
}
