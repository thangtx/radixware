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

import java.awt.*;
import org.netbeans.api.visual.widget.*;

import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;


public class ImagePinWidget extends ImageWidget {
    
    public ImagePinWidget(GraphSceneImpl scene, AdsPin pin, Image image) {
        super(scene, image);        
        setBorder(BorderFactory.createEmptyBorder());
        //connection provider
        getActions().addAction(scene.getConnectAction());
    }    
}
