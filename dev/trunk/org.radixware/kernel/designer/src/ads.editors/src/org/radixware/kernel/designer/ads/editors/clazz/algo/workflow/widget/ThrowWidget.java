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

import org.netbeans.api.visual.widget.*;
import java.awt.*;

import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ThrowPanel;



public class ThrowWidget extends BaseWidget<AdsThrowObject> {

    private final Image image = GraphUtil.IMAGE_THROW;
    
    public ThrowWidget(GraphSceneImpl scene, AdsThrowObject node) {
        super(scene, node, false);
    }
    
    @Override
    public Widget attachPin(AdsPin pin) {
        GraphSceneImpl scene = (GraphSceneImpl)getScene();        
        ImageWidget pinWidget = new ImagePinWidget(scene, pin, image);
        
        int width =  image.getWidth(null) + 18;
        int height = image.getHeight(null) + 18;
        
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        
        mainWidget.addChild(pinWidget);
        return pinWidget;
    }
    
    @Override
    protected boolean notifyEdited() {
        AdsThrowObject node = getNode();
        return EditorDialog.execute(new ThrowPanel(node));
    }    
        
}