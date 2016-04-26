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
import java.awt.Dimension;
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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsExceptionClassDef;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.CatchPanel;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog;


public class CatchWidget extends BaseWidget<AdsCatchObject> {
    
    private LabelWidget labelWidget;
    private LabelWidget exceptionWidget;
    
    public CatchWidget(GraphSceneImpl scene, AdsCatchObject node) {
        super(scene, node, true);

        setPreferredSize(new Dimension(100, 100));
        setMinimumSize(new Dimension(100, 100));
        
        Widget bodyWidget = new Widget(getScene());
        bodyWidget.setBorder(BorderFactory.createRoundedBorder(10, 10, BODY_COLOR, BORDER_COLOR));
        bodyWidget.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 0));
        bodyWidget.setCheckClipping(true);
        mainWidget.addChild(bodyWidget, 100);
        
        Widget topWidget = new Widget(getScene());
        bodyWidget.addChild(topWidget, 50);
 
        ImageWidget iconWiget = new ImageWidget(getScene());
        iconWiget.setImage(GraphUtil.getImage(node));
        bodyWidget.addChild(iconWiget);

        labelWidget = new LabelWidget(getScene(), node.getName());
        labelWidget.setAlignment (LabelWidget.Alignment.CENTER);
        labelWidget.setVerticalAlignment (LabelWidget.VerticalAlignment.CENTER);
        labelWidget.setFont(new Font("Arial", Font.PLAIN, 10));
        labelWidget.setBackground(BODY_COLOR);
        labelWidget.setForeground(Color.RED);
        bodyWidget.addChild(labelWidget);
        
        AdsExceptionClassDef exDef = node.getExceptionDef();
        exceptionWidget = new LabelWidget(getScene(), "(" + (exDef == null ? "..." : exDef.getName()) + ")");
        exceptionWidget.setAlignment (LabelWidget.Alignment.CENTER);
        exceptionWidget.setVerticalAlignment (LabelWidget.VerticalAlignment.CENTER);
        exceptionWidget.setFont(new Font("Arial", Font.PLAIN, 8));
        exceptionWidget.setBackground(BODY_COLOR);
        exceptionWidget.setForeground(Color.GREEN);
        bodyWidget.addChild(exceptionWidget);

        Widget bottomWidget = new Widget(getScene());
        bodyWidget.addChild(bottomWidget, 50);
    }

    @Override
    protected List<Action> getActionList() {
        AdsBaseObject node = getNode();

        Action cutAction = (Action)SystemAction.get(CutAction.class);
        Action copyAction = (Action)SystemAction.get(CopyAction.class);
        Action deleteAction = (Action)SystemAction.get(DeleteAction.class);

        return Arrays.asList(
                new EditAction(),
                null,
                cutAction,
                copyAction,
                null,
                new SelectInExplorerAction() {
                    @Override
                    protected RadixObject getObject() {
                        return getNode().getPage();
                    }
                },
                null,
                deleteAction
                );
    }
    
    @Override
    protected boolean notifyEdited() {
        AdsCatchObject node = getNode();
        boolean res = EditorDialog.execute(new CatchPanel(node));
        if (res) {
            labelWidget.setLabel(node.getName());
            AdsExceptionClassDef exDef = node.getExceptionDef();
            exceptionWidget.setLabel("(" + (exDef == null ? "..." : exDef.getName()) + ")");
        }
        return res;
    }
        
}
