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

import java.awt.*;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ReturnPanel;


public class ReturnWidget extends BaseWidget<AdsReturnObject> {
    
    private final Image image = GraphUtil.IMAGE_RETURN;
    private final LabelWidget labelWidget;

    public ReturnWidget(GraphSceneImpl scene, AdsReturnObject node) {
        super(scene, node, false);

        mainWidget.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 0));
        mainWidget.setCheckClipping(true);

        labelWidget = new LabelWidget(getScene(), node.getName());
        labelWidget.setAlignment(LabelWidget.Alignment.CENTER);
        labelWidget.setVerticalAlignment(LabelWidget.VerticalAlignment.CENTER);
        labelWidget.setFont(new Font("Arial", Font.PLAIN, 10));
        labelWidget.setBackground(BODY_COLOR);
        labelWidget.setForeground(Color.BLACK);
        mainWidget.addChild(labelWidget);
    }
    
    @Override
    public Widget attachPin(AdsPin pin) {
        GraphSceneImpl scene = (GraphSceneImpl)getScene();        
        ImageWidget pinWidget = new ImagePinWidget(scene, pin, image);
        
        int width = image.getWidth(null) + 18;
        int height = image.getHeight(null) + 18 + labelWidget.getPreferredBounds().height;
        
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));

        mainWidget.addChild(0, pinWidget);
        return pinWidget;
    }

    @Override
    public void notifyModified() {
        super.notifyModified();
        getNode().syncOwnerPins();
    }

    @Override
    protected boolean notifyEdited() {
        AdsReturnObject node = getNode();
        boolean res = EditorDialog.execute(new ReturnPanel(node));
        if (res) {
            labelWidget.setLabel(node.getName());
        }
        return res;
    }
}
