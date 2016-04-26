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

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.netbeans.api.visual.widget.*;

import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;


public class EmptyWidget extends BaseWidget<AdsEmptyObject> {
    
    public EmptyWidget(GraphSceneImpl scene, AdsEmptyObject node) {
        super(scene, node, false);
    }
    
    @Override
    public Widget attachPin(AdsPin pin) {
        GraphSceneImpl scene = (GraphSceneImpl)getScene();
        Widget pinWidget = new PinWidget(scene, pin);

        setPreferredSize(new Dimension(30, 30));
        setMinimumSize(new Dimension(30, 30));
        setMaximumSize(new Dimension(30, 30));
                
        mainWidget.addChild(pinWidget);
        return pinWidget;        
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
    
    @Override
    protected boolean notifyEdited() {
        return false;
    }    
}
