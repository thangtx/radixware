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

package org.radixware.kernel.explorer.views.selector;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QToolBar;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.views.ISelector.IToolBarsManager;


class ToolBarsManager extends QEventFilter implements IToolBarsManager {

    private static class CorrectPositionsEvent extends QEvent {

        public CorrectPositionsEvent() {
            super(QEvent.Type.User);
        }
    }
    
    private final List<QToolBar> toolBars = new ArrayList<>(4);
    private final Map<QToolBar, QPoint> toolbarsStandardPositions = new HashMap<>(8);
    private final QMainWindow mainWindow;
    private boolean customToolbarsPositions = false;
    private boolean updatePositionsScheduled = false;
    private QToolBar moovedToolBar;

    @SuppressWarnings("LeakingThisInConstructor")
    public ToolBarsManager(final QMainWindow mainWindow, final List<QToolBar> toolBars, final ClientSettings settings, final String settingName) {
        super(mainWindow);
        setProcessableEventTypes(EnumSet.of(QEvent.Type.MouseButtonRelease, QEvent.Type.ShowToParent));
        this.mainWindow = mainWindow;
        this.toolBars.addAll(toolBars);
        for (int i = 0; i < toolBars.size(); i++) {
            toolBars.get(i).installEventFilter(this);
        }
        settings.beginGroup(settingName);
        settings.beginGroup(SettingNames.SYSTEM);
        try {
            customToolbarsPositions = settings.readBoolean(Selector.CUSTOM_TOOLBARS_POSITION, false);
        } finally {
            settings.endGroup();
            settings.endGroup();
        }
    }

    @Override
    public void correctToolBarsPosition() {
        //DBP-1638
        //проверка что панель инструментов вмещается в селектор
        if (!customToolbarsPositions) {
            int right = toolBars.get(0).isHidden() ? 0 : toolBars.get(0).sizeHint().width();
            QToolBar toolBar;
            for (int i = 1; i < toolBars.size(); i++) {
                toolBar = toolBars.get(i);
                if (!toolBar.isHidden()) {
                    if (right + toolBar.sizeHint().width() > mainWindow.width()) {
                        mainWindow.insertToolBarBreak(toolBar);
                        right = toolBar.sizeHint().width();
                    } else {
                        toolBar.move(right, toolBars.get(i - 1).pos().y());
                        mainWindow.removeToolBarBreak(toolBar);
                        right += toolBar.sizeHint().width();
                    }
                } else {
                    mainWindow.removeToolBarBreak(toolBar);
                }
            }
            updateStandardToolbarsPositions();
        }
    }

    @Override
    public void sheduleCorrectToolBarsPosition() {
        if (!updatePositionsScheduled){
            updatePositionsScheduled = true;
            QApplication.postEvent(this, new CorrectPositionsEvent());
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof CorrectPositionsEvent) {
            if (updatePositionsScheduled && mainWindow.nativeId() != 0 && !toolBars.isEmpty()) {
                correctToolBarsPosition();
            }
            event.accept();
        } else {
            super.customEvent(event);
        }
    }

    private void updateStandardToolbarsPositions() {
        //QApplication.processEvents();//Обновить позицию toolBar
        for (int i = 0; i < toolBars.size(); i++) {
            toolbarsStandardPositions.put(toolBars.get(i), toolBars.get(i).pos());
        }
    }

    @Override
    public boolean eventFilter(QObject source, QEvent event) {
        if (source instanceof QToolBar && !customToolbarsPositions) {
            QToolBar toolbar = (QToolBar) source;
            if (event!=null && event.type() == QEvent.Type.MouseButtonRelease) {
                QPoint pos = toolbarsStandardPositions.get(toolbar);
                if (pos != null && !pos.equals(toolbar.pos())) {
                    moovedToolBar = toolbar;
                }
            } else if (event!=null && event.type() == QEvent.Type.ShowToParent && moovedToolBar == toolbar) {//NOPMD
                QPoint pos = toolbarsStandardPositions.get(toolbar);
                if (pos != null && !pos.equals(toolbar.pos())) {
                    moovedToolBar = null;
                    customToolbarsPositions = true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isToolBarsHaveCustomPositions() {
        return customToolbarsPositions;
    }

    @Override
    public void close() {
        updatePositionsScheduled = false;
        for (int i = 0; i < toolBars.size(); i++) {
            toolBars.get(i).removeEventFilter(this);
        }
        toolBars.clear();
        toolbarsStandardPositions.clear();
        moovedToolBar = null;
    }
}
