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

import java.awt.Image;
import java.awt.Dimension;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.*;

import java.util.List;
import java.util.Arrays;
import javax.swing.Action;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;


public class ForkWidget extends BaseWidget<AdsForkObject> {
    
    private final Image image = GraphUtil.IMAGE_FORK;
    
    public ForkWidget(GraphSceneImpl scene, AdsForkObject node) {
        super(scene, node, true);

        int width = image.getWidth(null) + 18;
        int height = image.getHeight(null) + 36 - 2;
        ImageWidget iconWidget = new ImageWidget(getScene(), image);
        
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        
        iconWidget.setBorder(BorderFactory.createEmptyBorder());

        mainWidget.addChild(iconWidget);
    }

    @Override
    protected List<Action> getActionList() {
        return Arrays.asList(
                (Action)SystemAction.get(CutAction.class),
                (Action)SystemAction.get(CopyAction.class),
                null,
                (Action)SystemAction.get(DeleteAction.class)
                );
    }

    
}
