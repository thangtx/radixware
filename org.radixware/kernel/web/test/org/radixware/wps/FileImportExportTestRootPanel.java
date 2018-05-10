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

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.wps.HttpQuery;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.icons.images.WsIcons;
import org.radixware.wps.rwt.FileInput;
import org.radixware.wps.rwt.IMainView;
import org.radixware.wps.rwt.PushButton;
import org.radixware.wps.rwt.RootPanel;


class FileImportExportTestRootPanel extends RootPanel {

    private WpsEnvironment env;

    public FileImportExportTestRootPanel(WpsEnvironment env) {
        this.env = env;
    }

    @Override
    protected IDialogDisplayer getDialogDisplayer() {
        return env.getDialogDisplayer();
    }

    @Override
    public IMainView getExplorerView() {
        return null;
    }

    @Override
    public void closeExplorerView() {
    }

    @Override
    protected Runnable componentRendered(HttpQuery query) {
        return new Runnable() {

            @Override
            public void run() {
                createTestUI();
            }
        };
    }

    private void createTestUI() {
        PushButton generateAndLoad = new PushButton("Download Icon File");
        add(generateAndLoad);
        generateAndLoad.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                try {
                    InputStream stream = WsIcons.class.getResourceAsStream("collapsed.png");
                    File file = File.createTempFile("aaa", "aaa");
                    FileOutputStream out = new FileOutputStream(file);
                    try {
                        FileUtils.copyStream(stream, out);
                    } finally {
                        out.close();
                    }
                    env.sendFileToTerminal("Test Description", file, "image/png", false, true);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    env.messageException("E", "E", ex);
                }
            }
        });

        final FileInput fileUploader = new FileInput();
        add(fileUploader);
        fileUploader.setTop(100);
        fileUploader.addFileListener(new FileInput.FileListener() {

            @Override
            public void fileSelected(String fileName) {
                env.messageInformation("File selected", fileName);
                final CountDownLatch latch = new CountDownLatch(1);
                final String[] statusMessage = new String[1];
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                fileUploader.openFile(new FileInput.FileRequestCallback() {

                    @Override
                    public void done(InputStream fileInputStream) {
                        statusMessage[0] = "Done";
                        try {
                            FileUtils.copyStream(fileInputStream, out);
                        } catch (IOException ex) {
                            statusMessage[0] = "Exception";
                        }
                        latch.countDown();

                    }

                    @Override
                    public void failure(Exception e) {
                        statusMessage[0] = "Failure";
                        latch.countDown();
                    }
                });
                try {
                    latch.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(FileImportExportTestRootPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                env.messageInformation("Result", statusMessage[0]);
            }
        });
    }
}
