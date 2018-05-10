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

package org.radixware.kernel.explorer.dialogs.trace;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;

import org.radixware.kernel.explorer.utils.WidgetUtils;


final class XmlTreeDialog extends QDialog {

    private XmlTree xmlTree = new XmlTree(this);
    private Finder finder;
    private final IClientEnvironment environment;

    public XmlTreeDialog(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        setWindowFlags(WidgetUtils.WINDOW_FLAGS_FOR_DIALOG);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        this.environment = environment;
        this.setMinimumSize(360, 280);
        this.setWindowTitle(Application.translate("TraceDialog", "Xml Tree"));
        finder = new Finder(environment,xmlTree);
        setupUi();        
        final ExplorerSettings settings = (ExplorerSettings)environment.getConfigStore();
        if (settings.contains(SettingNames.SYSTEM + "/TraceDialog/XmlTreeDialog")) {
            this.restoreGeometry(settings.readQByteArray(SettingNames.SYSTEM + "/TraceDialog/XmlTreeDialog"));
        }
    }

    @Override
    public void closeEvent(final QCloseEvent event) {
        final ExplorerSettings settings = (ExplorerSettings)environment.getConfigStore();
        settings.writeQByteArray(SettingNames.SYSTEM + "/TraceDialog/XmlTreeDialog", this.saveGeometry());
        super.closeEvent(event);
    }

    private void setupUi() {
        QVBoxLayout vBoxLayout = new QVBoxLayout();
        this.setLayout(vBoxLayout);

        QHBoxLayout hBoxLayout = new QHBoxLayout();
        vBoxLayout.addLayout(hBoxLayout);

        QPushButton findButton = new QPushButton(this);
        hBoxLayout.addWidget(findButton);
        findButton.setMinimumSize(110, 20);
        findButton.setText(Application.translate("TraceDialog", "&Find"));
        findButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FIND));
        findButton.setShortcut(QKeySequence.StandardKey.Find);
        findButton.clicked.connect(finder, "find()");

        QPushButton findNextButton = new QPushButton(this);
        hBoxLayout.addWidget(findNextButton);
        findNextButton.setMinimumSize(110, 20);
        findNextButton.setText(Application.translate("TraceDialog", "Find &Next"));
        findNextButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FIND_NEXT));
        findNextButton.setShortcut(QKeySequence.StandardKey.FindNext);
        findNextButton.clicked.connect(finder, "findNext()");

        hBoxLayout.addStretch();

        vBoxLayout.addWidget(xmlTree);
    }

    public void setXml(String xml) {
        xmlTree.setXml(xml);
    }
}
