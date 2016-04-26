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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.wps.rwt.Alignment;

import org.radixware.wps.rwt.IMainView;
import org.radixware.wps.rwt.RootPanel;
import org.radixware.wps.rwt.Splitter;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.settings.ISettingsChangeListener;
import org.radixware.wps.views.NavigationView;

class MainView extends Splitter implements IMainView {

    private UIObject currentView = null;
    private UIObject navigator = null;
    private float splitterPart;//splitter position
    private final WpsEnvironment e = (WpsEnvironment) getEnvironment();
    private Alignment alignment;
    private final ISettingsChangeListener l = new ISettingsChangeListener() {
        @Override
        public void onSettingsChanged() {
            applySettings();
        }
    };

    public MainView(IClientEnvironment env) {
        html.setAttr("role", "mainView");
        setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        navigator = new NavigationView((WpsEnvironment) env);
        add(navigator);
        e.addSettingsChangeListener(l);

        addSplitterListener(new SplitterListener() {
            @Override
            public void partChanged(int part, float currentValue) {
                if (currentValue <= 0) {
                    splitterPart = 0.25f;
                } else {
                    splitterPart = currentValue;
                }
                final WpsSettings settings = e.getConfigStore();
                try {
                    settings.beginGroup(SettingNames.SYSTEM);
                    settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
                    settings.beginGroup(SettingNames.ExplorerTree.COMMON_GROUP);
                    settings.writeDouble(SettingNames.ExplorerTree.SPLITTER_POSITION, splitterPart);
                } finally {
                    settings.endGroup();
                    settings.endGroup();
                    settings.endGroup();
                }
            }
        });
    }

    private void applySettings() {
        float part = readPart();
        int cvIndex = currentView == null ? -1 : indexOf(currentView);
        setSplitterAlignment(alignment);
        setPart(cvIndex, part);
    }

    private float readPart() {
        final WpsSettings settings = e.getConfigStore();
        try {
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
            settings.beginGroup(SettingNames.ExplorerTree.COMMON_GROUP);
            Alignment a = Alignment.getForValue(settings.readInteger(SettingNames.ExplorerTree.Common.TREE_AREA, 0));
            if (!a.equals(getSplitterAlignemnt())) {
                alignment = a;
            }
            splitterPart = (float)settings.readDouble(SettingNames.ExplorerTree.SPLITTER_POSITION, 0.25f);
            float p = (splitterPart) <= 0.1f ? 0.25f :  splitterPart;
            return p;
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
    }

    @Override
    public UIObject getNavigator() {
        return navigator;
    }

    @Override
    public void setView(UIObject viewManager) {
        if (currentView != null) {
            remove(currentView);
        }
        currentView = viewManager;
        currentView.getHtml().setAttr("role", "currentView");
        add(viewManager);
        applySettings();
    }

    @Override
    public void close() {
        final RootPanel panel = findRoot();
        if (panel != null) {
            panel.closeExplorerView();
        }
        if (l != null) {
            e.removeSettingsChangeListener(l);
        }
    }

    @Override
    public void setPart(int index, float part) {
        if (navigator != null && currentView != null) {
            switch (alignment) {
                case LEFT:
                case TOP:
                    remove(currentView);
                    remove(navigator);
                    add(navigator);
                    Cell newNavigatorCell1 = findCell(navigator);
                    newNavigatorCell1.setPart(0);
                    currentView.getHtml().setAttr("role", "currentView");
                    add(currentView);
                    Cell newViewCell1 = findCell(currentView);
                    newViewCell1.setPart(part);
                    break;
                case RIGHT:
                case BOTTOM:
                    remove(currentView);
                    remove(navigator);
                    currentView.getHtml().setAttr("role", "currentView");
                    add(currentView);
                    Cell newViewCell = findCell(currentView);
                    newViewCell.setPart(0);
                    add(navigator);
                    Cell newNavigatorCell = findCell(navigator);
                    newNavigatorCell.setPart(part);
                    break;
            }
        } else {
            super.setPart(index, part);
        }
    }
}