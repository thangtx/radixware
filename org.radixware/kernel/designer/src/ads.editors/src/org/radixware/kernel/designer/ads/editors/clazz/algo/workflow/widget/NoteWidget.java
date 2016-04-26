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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.widget;

import java.awt.Color;
import java.awt.Font;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.border.BorderFactory;

import org.netbeans.api.visual.widget.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.util.actions.SystemAction;

import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.*;


public class NoteWidget extends BaseWidget<AdsNoteObject> {
    
    private final Widget textWidget;
        
    public NoteWidget(GraphSceneImpl scene, AdsNoteObject node) {
        super(scene, node, false);

        Widget verWidget = new Widget(getScene());
        verWidget.setBorder(BorderFactory.createLineBorder(1, BORDER_COLOR));
        verWidget.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.JUSTIFY, 0));
        verWidget.setBackground(new Color(255, 255, 204));
        verWidget.setCheckClipping(true);
        verWidget.setOpaque(true);
        mainWidget.addChild(verWidget, 100);

        Widget topWidget = new Widget(getScene());
        verWidget.addChild(topWidget, 50);

        Widget horWidget = new Widget(getScene());
        horWidget.setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.JUSTIFY, 0));
        verWidget.addChild(horWidget);

        Widget leftWidget = new Widget(getScene());
        horWidget.addChild(leftWidget, 50);

        textWidget = new Widget(getScene());
        textWidget.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.LEFT_TOP, 0));
        horWidget.addChild(textWidget);
        
        Widget rightWidget = new Widget(getScene());
        horWidget.addChild(rightWidget, 50);

        Widget bottomWidget = new Widget(getScene());
        verWidget.addChild(bottomWidget, 50);

        update(node);
    }

    private void update(AdsNoteObject node) {
        final String text = node.getText();
        textWidget.removeChildren();
        if (!Utils.emptyOrNull(text)) {
            String[] labels = text.split("\n");
            for (String l: labels) {
                LabelWidget labelWidget = new LabelWidget(getScene(), l);
                labelWidget.setAlignment(LabelWidget.Alignment.CENTER);
                labelWidget.setVerticalAlignment(LabelWidget.VerticalAlignment.CENTER);
                labelWidget.setFont(new Font("Arial", Font.PLAIN, 10));
                labelWidget.setBackground(BODY_COLOR);
                labelWidget.setForeground(Color.BLACK);
                textWidget.addChild(labelWidget);
            }
        }
    }
    @Override
    protected boolean notifyEdited() {
        AdsNoteObject node = getNode();
        boolean res = EditorDialog.execute(new NotePanel(node));
        if (res) {
            update(node);
        }
        return res;
    }

    @Override
    protected List<Action> getActionList() {
        Action cutAction = (Action)SystemAction.get(CutAction.class);
        Action copyAction = (Action)SystemAction.get(CopyAction.class);
        Action deleteAction = (Action)SystemAction.get(DeleteAction.class);
        return Arrays.asList(
                new EditAction(),
                null,
                cutAction,
                copyAction,
                null,
                deleteAction
                );
    }
}
