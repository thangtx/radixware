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

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.wps.HttpQuery;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.Grid.Row;
import org.radixware.wps.rwt.*;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.rwt.tree.Tree;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;


public class TimerTicksTestRootPanel extends RootPanel {

    private final WpsEnvironment env;

    public TimerTicksTestRootPanel(WpsEnvironment env) {
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

    private void createTestUI() {
        final PushButton flyIngButton = new PushButton("Click Me!");
        add(flyIngButton);
        final int pc[] = new int[]{0};
        final IPeriodicalTask task = flyIngButton.startTimer(new TimerEventHandler() {
            @Override
            public void processTimerEvent(IPeriodicalTask task) {
                pc[0]++;
                int left = flyIngButton.getLeft();

                left++;

                flyIngButton.setLeft(left);
                flyIngButton.setText("Ticks processed: " + pc[0]);
            }
        });
        flyIngButton.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                if (task.isActive()) {
                    task.pause();
                } else {
                    task.resume();
                }
            }
        });

        final PushButton resetButton = new PushButton("Reset!");
        add(resetButton);
        resetButton.setTop(100);
        resetButton.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                flyIngButton.killTimer(task);
            }
        });
        InputBox<String> input = new InputBox<>();
        add(input);
        input.setValueController(new InputBox.ValueController<String>() {
            @Override
            public String getValue(String text) throws InputBox.InvalidStringValueException {
                return text;
            }
        });
        input.setTop(150);
        input.setValue("Hello");

        final Label label = new Label();
        add(label);
        label.setTop(200);
        input.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {
            @Override
            public void onValueChanged(String oldValue, String newValue) {
                label.setText(newValue);
            }
        });
    }

    @Override
    protected Runnable componentRendered(HttpQuery query) {
        return new Runnable() {
            @Override
            public void run() {
                createTestUI();
            }
        }; //To change body of generated methods, choose Tools | Templates.
    }
}
