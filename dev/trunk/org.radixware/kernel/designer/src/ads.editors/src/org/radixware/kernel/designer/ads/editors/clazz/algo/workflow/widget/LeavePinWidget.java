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

import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.border.BorderFactory;

import java.awt.*;
import org.netbeans.api.visual.widget.*;

import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette.Item;


public class LeavePinWidget extends Widget {
    
    static final Color BORDER_COLOR = Color.BLACK;
    static final Color TEXT_COLOR = Color.BLACK;
    static final Color PIN_COLOR = Color.RED;
    
    private LabelWidget labelWidget;
    private Widget pinWidget;
    private ImageWidget imageWidget;
    
    public LeavePinWidget(GraphSceneImpl scene, AdsPin pin) {
        super(scene);
        
        setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 1));
        setCheckClipping(true);
        
        labelWidget = new LabelWidget(scene);
        labelWidget.setAlignment (LabelWidget.Alignment.CENTER);
        labelWidget.setVerticalAlignment (LabelWidget.VerticalAlignment.CENTER);
        labelWidget.setFont(new Font("Arial", Font.PLAIN, 7));
        labelWidget.setForeground(TEXT_COLOR);
        addChild(labelWidget);
        
        pinWidget = new Widget(scene);
        pinWidget.setPreferredSize(new Dimension(9, 9));
        pinWidget.setBorder(BorderFactory.createLineBorder(1, BORDER_COLOR));
        pinWidget.setLayout(LayoutFactory.createOverlayLayout());
        pinWidget.setBackground(PIN_COLOR);
        pinWidget.setForeground(PIN_COLOR);        
        pinWidget.setOpaque(true);  
        addChild(pinWidget);

        // connection provider
        pinWidget.getActions().addAction(scene.getConnectAction());
        
        imageWidget = new ImageWidget(scene);
        pinWidget.addChild(imageWidget);
        
        sync(pin);
    }
    
    public Widget getPinWidget() {
        return pinWidget;
    }
    
    public void sync(AdsPin pin) {
//        labelWidget.setLabel(pin.getName());
//        if (pin.getIcon() != null)
//            imageWidget.setImage(Item.getPin(pin.getIconName()));
        labelWidget.setLabel(pin.getName().isEmpty() ? null : pin.getName());
        imageWidget.setImage(pin.getIconName() != null ? Item.getPin(pin.getIconName()) : null);
        repaint();
    }
    
    public void sync() {
        GraphSceneImpl scene = (GraphSceneImpl)getScene();
        AdsPin pin = (AdsPin)scene.findObject(this);
        assert pin != null;
        sync(pin);
    }    
}
