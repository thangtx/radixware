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

package org.radixware.wps.dialogs;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.traceprofile.ITraceProfileEditor;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.trace.ITraceDialogPresenter;
import org.radixware.kernel.common.client.trace.TraceDialogController;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.AbstractContainer;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.TabLayout;
import org.radixware.wps.trace.TraceProfileEditor;
import org.radixware.wps.views.trace.TraceView;


public class TraceDialog extends Dialog {

    private final TabLayout tab = new TabLayout();
    private final TraceView traceView;
    private final TraceDialogController controller;
    private final WpsSettings settings;

    public TraceDialog(final WpsEnvironment environment, final AbstractContainer parent) {
        super(environment.getDialogDisplayer(),
              environment.getMessageProvider().translate("TraceDialog", "Trace"),
              true);
        getHtml().setAttr("dlgId", "traceDialog");
        parent.add(TraceDialog.this);
        settings = environment.getConfigStore();
        traceView = new TraceView(environment, environment.getTracer().getBuffer());
        setupUi();
        controller = new TraceDialogController(environment, new ITraceDialogPresenter() {
            @Override
            public ITraceProfileEditor createTraceProfileEditor(final IClientEnvironment environment) {
                final TraceProfileEditor editor = new TraceProfileEditor(environment, false);
                final TabLayout.Tab page = tab.addTab(environment.getMessageProvider().translate("TraceDialog", "Profile Settings"));
                page.add(editor);
                editor.getHtml().setCss("height", "100%");
                editor.getHtml().setCss("overflow", "auto");
                return editor;
            }

            @Override
            public void setProfileEditorEnabled(final boolean isEnabled) {
                tab.getTab(1).setEnabled(isEnabled);
            }
        });
        traceView.addMaxItemsCountBox(TraceDialog.this);
        init();
    }

    private void setupUi() {
        add(tab);
        tab.addTab(getEnvironment().getMessageProvider().translate("TraceDialog", "Log")).add(traceView);
        addCloseAction(EDialogButtonType.CLOSE);
        addCloseButtonListener(new CloseButtonListener() {

            @Override
            public void onClose(EDialogButtonType button, DialogResult result) {
                close(result);
            }
        });
        setWidth(800);
        setHeight(500);
    }

    public final void init() {
        controller.updateEventSources();
    }

    public void clear() {
        controller.onDisconnect();
    }

    @Override
    public void close(DialogResult result) {
        settings.writeInteger(SettingNames.SYSTEM + "/TraceDialog/TraceFilter", traceView.getSeverity().getValue().intValue());
        settings.setValue(SettingNames.SYSTEM + "/TraceDialog/MaxItemCount", getEnvironment().getTracer().getBuffer().getMaxSize());
        super.close(result);
    }
}
