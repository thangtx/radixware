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

package org.radixware.kernel.designer.dds.editors.diagram.widgets;

import java.awt.Color;
import java.awt.GradientPaint;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;


class GradientWidget extends Widget {

    private boolean initialized = false;

    public GradientWidget(Scene scene) {
        super(scene);
        setOpaque(true);
    }

    @Override
    protected void paintBackground() {
        if (!initialized) {
            initialized = true;
            if (getBackground() instanceof Color) {
                Color backgroundColor = (Color) getBackground();
                final GradientPaint gradient = new GradientPaint(0, 0, Color.WHITE, getBounds().width, 0, backgroundColor);
                setBackground(gradient);
            }
        }
        super.paintBackground();
    }
}
