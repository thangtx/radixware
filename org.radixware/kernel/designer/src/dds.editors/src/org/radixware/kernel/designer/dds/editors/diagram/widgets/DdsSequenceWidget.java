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
import java.awt.Image;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.designer.dds.editors.diagram.DdsModelDiagram;


class DdsSequenceWidget extends DdsDefinitionTitledNodeWidget {

    private static final Color headerColor = new Color(0xFF, 0x93, 0x1E);
    private static final Image SEQUENCE_IMAGE_20 = DdsDefinitionIcon.SEQUENCE.getImage(20);
    private static final Border border = BorderFactory.createEmptyBorder(8);

    protected DdsSequenceWidget(DdsModelDiagram diagram, DdsSequenceDef sequence) {
        super(diagram, sequence, SEQUENCE_IMAGE_20);

        setHeaderBackground(headerColor);

        LabelWidget label = new LabelWidget(diagram, "Sequence");
        label.setBorder(border);
        label.setAlignment(LabelWidget.Alignment.CENTER);
        this.addChild(label);
    }

    @Override
    public void update() {
        // NOTHING
    }
}
