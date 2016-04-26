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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.designer.dds.editors.diagram.DdsModelDiagram;


class DdsReferenceWidget extends ConnectionWidget implements IDdsDefinitionWidget {

    private final DdsReferenceDef ref;
    private static final DdsReferenceReconnectProvider reconnectProvider = new DdsReferenceReconnectProvider();

    public DdsReferenceWidget(DdsModelDiagram diagram, DdsReferenceDef ref) {
        super(diagram);
        this.ref = ref;

        this.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        this.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
        this.setPaintControlPoints(true);
        this.setControlPointShape(PointShape.SQUARE_FILLED_BIG);
        this.setCheckClipping(true);

        update();

        this.getActions().addAction(diagram.createSelectAction());
       // if (!ref.isReadOnly()) {
            this.getActions().addAction(ActionFactory.createReconnectAction(reconnectProvider));
        //}

        DdsWidgetPopupMenu popupMenu = new DdsWidgetPopupMenu(diagram, ref);
        this.getActions().addAction(ActionFactory.createPopupMenuAction(popupMenu));
        getActions().addAction(ActionFactory.createEditAction(DdsDefinitionEditProvider.Factory.getDefault()));

        this.setRouter(diagram.getReferencesRouter());
    }
    private static final Stroke BASIC_STROKE = new BasicStroke();
    private static final Stroke DOUBLE_WIDTH_STROKE = new BasicStroke(2.0f);
    private static final Stroke DOTTED_STROKE = new BasicStroke(1, 0, 0, 1, new float[]{2, 2}, 0);

    @Override
    public final void update() {
        DdsModelDiagram diagram = (DdsModelDiagram) getScene();

        DdsDefinition child = ref.findExtChildTable();
        if (child == null) {
            child = ref.findChildTable(ref);
        }
        DdsDefinition parent = ref.findExtParentTable();
        if (parent == null) {
            parent = ref.findParentTable(ref);
        }

        Widget childWidget = (child != null ? diagram.findWidget(child) : null); // child can be not found or placed in another model
        Anchor childAnchor = diagram.getAnchor(childWidget);
        this.setSourceAnchor(childAnchor);

        Widget parentWidget = (child == parent ? childWidget : (parent != null ? diagram.findWidget(parent) : null));
        Anchor parentAnchor = (child == parent ? childAnchor : diagram.getAnchor(parentWidget));
        this.setTargetAnchor(parentAnchor);

        if (childAnchor.getRelatedWidget() != null && parentAnchor.getRelatedWidget() != null) {
            if (ref.isGeneratedInDb()) {
                this.setLineColor(Color.BLUE);
            } else {
                this.setLineColor(Color.DARK_GRAY);
            }
        } else {
            this.setLineColor(Color.RED);
        }

        if (ref.getType() == DdsReferenceDef.EType.MASTER_DETAIL) {
            this.setStroke(DOTTED_STROKE);
        } else if (ref.getDeleteMode() == EDeleteMode.CASCADE) {
            this.setStroke(DOUBLE_WIDTH_STROKE);
        } else {
            this.setStroke(BASIC_STROKE);
        }
    }

    public DdsReferenceDef getReference() {
        return ref;
    }
}
