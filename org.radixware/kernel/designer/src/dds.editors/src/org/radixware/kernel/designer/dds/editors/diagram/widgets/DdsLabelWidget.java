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
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.radixware.kernel.common.defs.dds.DdsDefinitionPlacement;
import org.radixware.kernel.common.defs.dds.DdsLabelDef;
import org.radixware.kernel.common.defs.dds.IPlacementSupport;
import org.radixware.kernel.designer.dds.editors.diagram.DdsModelDiagram;


class DdsLabelWidget extends DdsDefinitionNodeWidget {

    private static final Color textColor = new Color(0x98, 0x98, 0x98);
    //private static final Border resizeBorder = BorderFactory.createResizeBorder(5);
    //private static final DdsLabelWidgetResizeProvider resizeProvider = new DdsLabelWidget.DdsLabelWidgetResizeProvider();
    //private static final WidgetAction resizeAction = ActionFactory.createResizeAction(null, resizeProvider);
    private final DdsLabelDef ddsLabel;
    private final JLabel jLabel;
    private final ComponentWidget componentWidget;

    protected DdsLabelWidget(DdsModelDiagram diagram, DdsLabelDef ddsLabel) {
        super(diagram, ddsLabel);

        this.ddsLabel = ddsLabel;

        final DdsDefinitionPlacement placement = getScalePlacement();//((IPlacementSupport) ddsLabel).getPlacement();

        this.setPreferredLocation(new Point(placement.getPosX(), placement.getPosY()));
        this.setMinimumSize(new Dimension(30, 20));
        this.setLayout(LayoutFactory.createOverlayLayout());

        jLabel = new JLabel(ddsLabel.getText(), JLabel.CENTER);
        jLabel.setForeground(textColor);
        componentWidget = new ComponentWidget(diagram, jLabel);
        updateFont();
        this.addChild(componentWidget);
    }

    private void updateFont() {
        java.awt.Font font = this.getScene().getDefaultFont();
        font = font.deriveFont((float) ddsLabel.getFont().getSizePx());
        if (ddsLabel.getFont().isBold()) {
            font = font.deriveFont(java.awt.Font.BOLD);
        }
        if (ddsLabel.getFont().isItalic()) {
            font = font.deriveFont(java.awt.Font.ITALIC);
        }
        jLabel.setFont(font);
    }

    @Override
    public void setSelected(boolean isSelected) {
        if (isSelected) {
            super.setSelected(isSelected);
        } else {
            setBorder(BorderFactory.createEmptyBorder());
        }
    }

//    private static class DdsLabelWidgetResizeProvider implements ResizeProvider {
//
//        @Override
//        public void resizingStarted(Widget widget) {
//            // do nothing
//        }
//
//        @Override
//        public void resizingFinished(Widget widget) {
//            DdsLabelWidget labelWidget = (DdsLabelWidget) widget;
//            Rectangle bounds = labelWidget.getBounds();
//            labelWidget.ddsLabel.setWidth(bounds.width);
//            labelWidget.ddsLabel.setHeight(bounds.height);
//        }
//    }
    @Override
    public void update() {
        jLabel.setText(ddsLabel.getText());
        updateFont();
        componentWidget.setPreferredSize(jLabel.getPreferredSize());
    }
}
