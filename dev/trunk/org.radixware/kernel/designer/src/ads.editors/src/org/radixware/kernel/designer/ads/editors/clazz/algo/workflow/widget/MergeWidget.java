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

import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.*;
import java.awt.*;

import org.netbeans.api.visual.layout.LayoutFactory;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.*;


public class MergeWidget extends BaseWidget<AdsMergeObject> {
    
    private final Image image = GraphUtil.IMAGE_MERGE;
    private LabelWidget labelWidget;
    
    public MergeWidget(GraphSceneImpl scene, AdsMergeObject node) {
        super(scene, node, true);

        int width = image.getWidth(null) + 18;
        int height = image.getHeight(null) + 36 - 2;
        ImageWidget iconWidget = new ImageWidget(getScene(), image);
        iconWidget.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 0));
        iconWidget.setCheckClipping(true);
        
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        
        iconWidget.setBorder(BorderFactory.createEmptyBorder());
        mainWidget.addChild(iconWidget, 100);

        Widget topWidget = new Widget(getScene());
        iconWidget.addChild(topWidget, 50);

        labelWidget = new LabelWidget(getScene(), String.valueOf(node.getM()) + "/n");
        labelWidget.setAlignment (LabelWidget.Alignment.CENTER);
        labelWidget.setVerticalAlignment (LabelWidget.VerticalAlignment.CENTER);
        labelWidget.setFont(new Font("Arial", Font.PLAIN, 10));
        labelWidget.setBackground(BODY_COLOR);
        labelWidget.setForeground(Color.RED);
        iconWidget.addChild(labelWidget);

        Widget bottomWidget = new Widget(getScene());
        iconWidget.addChild(bottomWidget, 50);
    }

    @Override
    protected boolean notifyEdited() {
        AdsMergeObject node = getNode();
        boolean res = EditorDialog.execute(new MergePanel(node));
        if (res) {
            labelWidget.setLabel(String.valueOf(node.getM()) + "/n");
        }
        return res;
    }

}
