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

package org.radixware.kernel.designer.ads.editors.clazz.forms.common;

import java.awt.Color;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.RectangularSelectDecorator;
import org.netbeans.api.visual.action.RectangularSelectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;


public class SelectDecorator implements RectangularSelectDecorator {

    public static WidgetAction createRectangularSelectAction (ObjectScene scene, LayerWidget interractionLayer) {
        assert scene != null;
        return ActionFactory.createRectangularSelectAction(new SelectDecorator(scene), interractionLayer, ActionFactory.createObjectSceneRectangularSelectProvider(scene));
    }

    public static WidgetAction createRectangularSelectAction (ObjectScene scene, LayerWidget interractionLayer, RectangularSelectProvider provider) {
        assert scene != null;
        return ActionFactory.createRectangularSelectAction(new SelectDecorator(scene), interractionLayer, provider);
    }

    private static final Color COLOR_SELECTED = new Color (0x447BCD);
    private static final int MINI_THICKNESS = 3;

    private Scene scene;

    public SelectDecorator (Scene scene) {
        this.scene = scene;
    }

    @Override
    public Widget createSelectionWidget () {
        Widget widget = new Widget (scene);
        widget.setOpaque(false);
        widget.setBorder(BorderFactory.createRoundedBorder(MINI_THICKNESS, MINI_THICKNESS, MINI_THICKNESS, MINI_THICKNESS, null, COLOR_SELECTED));
        return widget;
    }

}
