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

import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QDialogButtonBox.StandardButton;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.traceprofile.ITraceProfileEditor;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.trace.ITraceDialogPresenter;
import org.radixware.kernel.common.client.trace.TraceDialogController;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.TraceProfileEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceBuffer;

import org.radixware.kernel.explorer.utils.WidgetUtils;


public class TraceDialog extends ExplorerDialog {

    private TraceView traceView;
    private final QTabWidget tabWidget = new QTabWidget(this);    
    private final ClientSettings settings;
    private final TraceDialogController controller;        

    public TraceDialog(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, null);
        settings = environment.getConfigStore();
        setWindowFlags(WidgetUtils.WINDOW_FLAGS_FOR_DIALOG);
        setWindowTitle(Application.translate("TraceDialog", "Trace"));
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.TRACE));
        setMinimumSize(800, 500);
        setupUi();
        controller = new TraceDialogController(environment, new ITraceDialogPresenter(){

            @Override
            public ITraceProfileEditor createTraceProfileEditor(final IClientEnvironment environment) {
                final TraceProfileEditor editor = new TraceProfileEditor(environment, null, false);
                tabWidget.addTab(editor, environment.getMessageProvider().translate("TraceDialog", "Profile Settings"));
                return editor;
            }

            @Override
            public void setProfileEditorEnabled(final boolean isEnabled) {
                tabWidget.setTabEnabled(1, isEnabled);
            }        
        });                                            
    }

    @Override
    public void closeEvent(final QCloseEvent event) {
        settings.writeInteger(SettingNames.SYSTEM + "/TraceDialog/TraceFilter", traceView.getTraceFilterSeverity().getValue().intValue());
        settings.setValue(SettingNames.SYSTEM + "/TraceDialog/ContinueTracing", traceView.getContinueTracing() ? "1" : "0");
        super.closeEvent(event);
    }

    private void setupUi() {
        final QVBoxLayout vBoxLayout = dialogLayout();
        //this.setLayout(vBoxLayout);

        vBoxLayout.addWidget(tabWidget);

        traceView = new TraceView(getEnvironment(), (ExplorerTraceBuffer)getEnvironment().getTracer().getBuffer());
        tabWidget.addTab(traceView, Application.translate("TraceDialog", "Log"));

        vBoxLayout.addSpacing(3);
        final QHBoxLayout hBoxLayout = new QHBoxLayout();
        vBoxLayout.addLayout(hBoxLayout);

        hBoxLayout.addWidget(traceView.getCheckBox());
        hBoxLayout.addSpacing(20);

        final QLabel showLabel = new QLabel(Application.translate("TraceDialog", "Keep"));
        hBoxLayout.addWidget(showLabel);
        hBoxLayout.addWidget(traceView.getSpinBox());
        final QLabel itemsLabel = new QLabel(Application.translate("TraceDialog", "items"));
        hBoxLayout.addWidget(itemsLabel);

        hBoxLayout.addStretch();

        final QDialogButtonBox buttonBox = new QDialogButtonBox(this);
        hBoxLayout.addWidget(buttonBox);
        final QPushButton closeButton = buttonBox.addButton(StandardButton.Close);
        closeButton.setText(Application.translate("TraceDialog", "&Close"));
        closeButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.EXIT));
        closeButton.clicked.connect(this, "hide()");
        traceView.setFocus();
    }

    public void init() {
        traceView.refreshControls();
        controller.updateEventSources();
    }

    public void clear() {
        controller.onDisconnect();
    }
}