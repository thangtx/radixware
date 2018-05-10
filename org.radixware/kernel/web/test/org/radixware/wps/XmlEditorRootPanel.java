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

package org.radixware.wps;

import java.io.IOException;
import java.io.InputStream;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.wps.WebServer;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.CheckBox;
import org.radixware.wps.rwt.IMainView;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.PushButton;
import org.radixware.wps.rwt.RootPanel;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.uploading.FileUploader;
import org.radixware.wps.rwt.uploading.IUploadedDataReader;
import org.radixware.wps.rwt.uploading.LoadFileAction;
import org.radixware.wps.views.editor.xml.view.XmlEditor;


public class XmlEditorRootPanel extends RootPanel {

    private final WpsEnvironment env;
    private String xmlSchemaAsStr;
    private XmlEditor xmlEditor;

    public XmlEditorRootPanel(WpsEnvironment env) {
        this.env = env;
        this.env.checkThreadState();
        ((WebServer.WsThread) Thread.currentThread()).userSession = env;
    }

    @Override
    protected IDialogDisplayer getDialogDisplayer() {
        return env.getDialogDisplayer();
    }

    @Override
    public IMainView getExplorerView() {
        throw new UnsupportedOperationException("Explorer view is not supported by TestRoot");
    }

    @Override
    public void closeExplorerView() {
        throw new UnsupportedOperationException("Explorer view is not supported by TestRoot");
    }

    public void createTestUI() {        
        final InputBox<String> schemaPathEditor = new InputBox<>();
        add(schemaPathEditor);
        schemaPathEditor.setWidth(400);
        schemaPathEditor.setValue(env.getMessageProvider().translate("XmlEditor", "<Select Xml Schema>"));
        schemaPathEditor.setClearController(new InputBox.ClearController<String>() {
            @Override
            public String clear() {
                xmlSchemaAsStr = null;
                return env.getMessageProvider().translate("XmlEditor", "<Select Xml Schema>");
            }
        });
        final IUploadedDataReader schemaReader = new IUploadedDataReader() {
            @Override
            public void readData(InputStream stream, String fileName, long fileSize) {
                try {
                    xmlSchemaAsStr = FileUtils.readTextStream(stream, "UTF-8");
                } catch (IOException exception) {
                    env.processException(exception);
                }
            }
        };
        final LoadFileAction action =
                new LoadFileAction(env, ClientIcon.CommonOperations.OPEN, schemaReader);
        final ToolButton openSchemaFileButton = new ToolButton(action);
        schemaPathEditor.addCustomButton(openSchemaFileButton);
        action.addActionPresenter(openSchemaFileButton);
        action.getUploader().addUploadCompleteListener(new FileUploader.UploadCompleteListener() {
            @Override
            public void afterUploadingComplete(String fileName, long fileSize, IUploadedDataReader reader) {
                schemaPathEditor.setValue(fileName);
            }
        });
        final CheckBox cbReadOnly = new CheckBox();
        cbReadOnly.setText(env.getMessageProvider().translate("XmlEditor", "Text is read only"));
        add(cbReadOnly);
        final PushButton pbOpenXmlEditor = new PushButton(env.getMessageProvider().translate("XmlEditor", "Open XmlEditor"));
        pbOpenXmlEditor.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                if (xmlEditor != null) {
                    XmlEditorRootPanel.this.remove(xmlEditor);
                }
                xmlEditor = new XmlEditor(env, xmlSchemaAsStr, cbReadOnly.isSelected());
                XmlEditorRootPanel.this.add(xmlEditor);
                xmlEditor.setTop(70);
                xmlEditor.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
            }
        });
        cbReadOnly.addSelectionStateListener(new CheckBox.SelectionStateListener() {
            @Override
            public void onSelectionChange(CheckBox cb) {
                if (xmlEditor != null) {
                    xmlEditor.setReadOnly(cb.isSelected());
                }
            }
        });
        add(pbOpenXmlEditor);
    }
}
