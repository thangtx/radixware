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
import java.awt.Image;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.border.BorderFactory;

import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.netbeans.api.visual.widget.*;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;

import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.VarPanel;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesAction;


public class VarWidget extends BaseWidget<AdsVarObject> {
    
    private ImageWidget iconWiget;
    private LabelWidget labelWidget;
        
    public VarWidget(GraphSceneImpl scene, AdsVarObject node) {
        super(scene, node, false);

        setPreferredSize(new Dimension(80, 66));
        setMinimumSize(new Dimension(80, 66));
        
        Widget bodyWidget = new Widget(getScene());
        bodyWidget.setBorder(BorderFactory.createRoundedBorder(10, 10, BODY_COLOR, BORDER_COLOR));
        bodyWidget.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 0));
        bodyWidget.setCheckClipping(true);
        mainWidget.addChild(bodyWidget, 100);
        
        Widget topWidget = new Widget(getScene());
        bodyWidget.addChild(topWidget, 50);
 
        iconWiget = new ImageWidget(getScene());
        iconWiget.setImage(getImage(node));
        bodyWidget.addChild(iconWiget);

        labelWidget = new LabelWidget(getScene(), node.getName());
        labelWidget.setAlignment (LabelWidget.Alignment.CENTER);
        labelWidget.setVerticalAlignment (LabelWidget.VerticalAlignment.CENTER);
        labelWidget.setFont(new Font("Arial", Font.PLAIN, 10));
        labelWidget.setBackground(BODY_COLOR);
        labelWidget.setForeground(Color.BLUE);        
        bodyWidget.addChild(labelWidget);
        
        Widget bottomWidget = new Widget(getScene());
        bodyWidget.addChild(bottomWidget, 50);
    }
    
    @Override
    protected boolean notifyEdited() {
        AdsVarObject node = getNode();
        boolean res = EditorDialog.execute(new VarPanel(node));
        if (res) {
            iconWiget.setImage(getImage(node));
            labelWidget.setLabel(node.getName());
        }
        return res;
    }
    
    public Image getImage(AdsVarObject node) {
        RadixIcon radixIcon = node.getIcon();
        return radixIcon != null ? radixIcon.getImage(16, 16) : null;
    }

    @Override
    protected List<Action> getActionList() {
        Action cutAction = (Action)SystemAction.get(CutAction.class);
        Action copyAction = (Action)SystemAction.get(CopyAction.class);
        Action deleteAction = (Action)SystemAction.get(DeleteAction.class);
        Action findUsages = SystemAction.get(FindUsagesAction.class);
        return Arrays.asList(
                new EditAction(),
                null,
                findUsages,
                null,
                cutAction,
                copyAction,
                null,
                deleteAction
                );
    }

}
