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
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.netbeans.api.visual.widget.*;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.border.BorderFactory;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject.Prop;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog.EditorPanel;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette.AppItem;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette.Item;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class AppWidget extends BaseWidget<AdsAppObject> {
    
    private LabelWidget labelWidget;
        
    public AppWidget(GraphSceneImpl scene, AdsAppObject node) {
        super(scene, node, true);
        
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
        
        Widget bottomWidget = new Widget(getScene());
        bodyWidget.addChild(bottomWidget, 50);
    }

    private boolean checkProps(){
        AdsAppObject node = getNode();
        for (Prop prop: node.getProps()) {
            AdsTypeDeclaration type = prop.getType();
            if (type.getPath() != null) {
                AdsType resolvedType = type.resolve(node).get();
                if (resolvedType == null) {
                    DialogUtils.messageError("Unable reslove type for property " + prop.getName() + " (type = " + type.getName(node) + ")");
                    return false;
                }
            }
        }
        return true;
    }

    private static EditorPanel<AdsAppObject> getPanel(AdsAppObject node) {
        AppItem item = (AppItem)Item.getItem(node);
        return item.getPanel(node);
    }

    private static void sync(AdsAppObject node) {
        AppItem item = (AppItem)Item.getItem(node);
        item.sync(node);
    }

    @Override
    protected boolean notifyEdited() {
        if (!checkProps())
            return false;
        AdsAppObject node = getNode();
        EditorPanel<AdsAppObject> panel = getPanel(node);
        if (panel != null) {
            boolean res = EditorDialog.execute(panel);
            if (res) {
                labelWidget.setLabel(node.getName());
                node.fireNodeUpdate();
                sync(node);
            }
            return res;
        }
        return false;
    }

    @Override
    protected List<Action> getActionList() {
        Action cutAction = (Action)SystemAction.get(CutAction.class);
        Action copyAction = (Action)SystemAction.get(CopyAction.class);
        Action deleteAction = (Action)SystemAction.get(DeleteAction.class);
        return Arrays.asList(
                new EditAction(),
                null,
                new ProfileAction(),
                null,
                cutAction,
                copyAction,
                null,
                deleteAction
                );
    }
}
