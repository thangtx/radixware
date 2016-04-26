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

package org.radixware.kernel.explorer.macros.gui;

import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.macros.MacroPlayer;
import org.radixware.kernel.explorer.macros.MacroRecorder;
import org.radixware.kernel.explorer.macros.Macros;
import org.radixware.kernel.explorer.macros.actions.IMacroAction;


public final class ExplorerMacrosWindow extends QMainWindow {

    private static final class Icons extends ClientIcon {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        public static final Icons START_RECORD = new Icons("classpath:images/start_record_macros.svg");
        public static final Icons STOP_RECORD = new Icons("classpath:images/stop_record_macros.svg");
        public static final Icons PLAY = new Icons("classpath:images/run_macros.svg");
    }

    private final class Actions {

        public final QAction recordAction;
        public final QAction playAction;

        public Actions() {
            recordAction = new QAction(ExplorerIcon.getQIcon(Icons.START_RECORD), "Start record", ExplorerMacrosWindow.this);
            playAction = new QAction(ExplorerIcon.getQIcon(Icons.PLAY), "Run macros", ExplorerMacrosWindow.this);
            recordAction.triggered.connect(ExplorerMacrosWindow.this, "onRecord()");
            playAction.triggered.connect(ExplorerMacrosWindow.this, "onPlay()");
        }

        public void refresh() {
            if (recordOn) {
                recordAction.setIcon(ExplorerIcon.getQIcon(Icons.STOP_RECORD));
                recordAction.setToolTip("Stop Record");
                playAction.setEnabled(false);
            } else {
                recordAction.setIcon(ExplorerIcon.getQIcon(Icons.START_RECORD));
                recordAction.setToolTip("Start Record");
                playAction.setEnabled(macrosList.count() > 0);
            }
        }
    }
    private final QListWidget macrosList = new QListWidget();
    private final Actions actions = new Actions();
    private boolean recordOn;
    private Macros currentMacros;
    private MacroPlayer player;

    public ExplorerMacrosWindow(final IClientEnvironment environment) {
        super((QWidget) null);
        setCentralWidget(macrosList);
        final QToolBar toolBar = addToolBar("macrosToolBar");
        toolBar.addAction(actions.recordAction);
        toolBar.addAction(actions.playAction);
        actions.refresh();
        player = new MacroPlayer(environment);
        player.finished.connect(this, "onPlayFinished()");
    }

    @SuppressWarnings("unused")
    private void onRecord() {
        if (recordOn) {
            MacroRecorder.getInstance().stopRecord();
            MacroRecorder.getInstance().newAction.disconnect();
        } else {
            macrosList.clear();
            currentMacros = new Macros("Новый макрос", null);
            MacroRecorder.getInstance().newAction.connect(this, "onMacroActionRecorded(IMacroAction)");
            MacroRecorder.getInstance().startRecord(currentMacros);
        }
        recordOn = !recordOn;
        actions.refresh();
    }

    @SuppressWarnings("unused")
    private void onPlay() {
        if (currentMacros != null) {
            player.runMacros(currentMacros);
        }
    }

    @SuppressWarnings("unused")
    private void onPlayFinished() {
        Application.messageInformation("On Play Finished");
    }

    @SuppressWarnings("unused")
    private void onMacroActionRecorded(final IMacroAction action) {
        macrosList.addItem(action.getTitle());
    }
}
