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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.util.Map;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 * Widget with label and icon on the left.
 */
public class ImageLabelWidget extends Widget {

    private static class LabelWidgetWithUnderlineSupport extends LabelWidget {

        private boolean isUnderline = false;

        public LabelWidgetWithUnderlineSupport(Scene scene, String label) {
            super(scene, label);
        }
        private boolean isStrikeOut = false;

        public boolean isUnderline() {
            return isUnderline;
        }

        public boolean isStrikeOut() {
            return false;
        }

        public void setStrykeOut(boolean isStrikeOut) {
            if (this.isStrikeOut != isStrikeOut) {
                this.isStrikeOut = isStrikeOut;
                this.revalidate(true);
            }
        }

        @Override
        protected void paintWidget() {
            if (isStrikeOut) {
                Font font = getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                setFont(new Font(attributes));
            }
            super.paintWidget();

            if (isUnderline) {
                final Graphics2D graphics = getGraphics();
                final Rectangle bounds = getBounds();
                graphics.drawLine(bounds.x, bounds.y + bounds.height - 2, bounds.x + bounds.width, bounds.y + bounds.height - 2);
            }
        }

        public void setUnderline(boolean isUnderline) {
            if (this.isUnderline != isUnderline) {
                this.isUnderline = isUnderline;
                this.revalidate(true);
            }
        }
    }
    private final ImageWidget imageWidget;
    private final LabelWidgetWithUnderlineSupport labelWidget;
    private boolean isBold = false;

    public ImageLabelWidget(Scene scene, Image image, String label) {
        super(scene);

        setLayout(LayoutFactory.createHorizontalFlowLayout());

        final Widget leftSeparator = new Widget(scene);
        leftSeparator.setPreferredSize(new Dimension(1, 0));
        this.addChild(leftSeparator);

        imageWidget = new ImageWidget(scene, image);
        this.addChild(imageWidget);

        final Widget centerSeparator = new Widget(scene);
        centerSeparator.setPreferredSize(new Dimension(4, 0));
        this.addChild(centerSeparator);

        labelWidget = new LabelWidgetWithUnderlineSupport(scene, label);
        labelWidget.setVerticalAlignment(LabelWidget.VerticalAlignment.CENTER);
        this.addChild(labelWidget);

        final Widget rightSeparator = new Widget(scene);
        rightSeparator.setPreferredSize(new Dimension(4, 0));
        this.addChild(rightSeparator);
    }

    public String getLabel() {
        return labelWidget.getLabel();
    }

    public void setLabel(String title) {
        this.labelWidget.setLabel(title);
    }

    public Color getLabelForeground() {
        return labelWidget.getForeground();
    }

    public void setLabelForeground(Color color) {
        labelWidget.setForeground(color);
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean isBold) {
        if (this.isBold != isBold) {
            this.isBold = isBold;
            if (isBold) {
                labelWidget.setFont(this.getScene().getDefaultFont().deriveFont(Font.BOLD));
            } else {
                labelWidget.setFont(this.getScene().getDefaultFont());
            }
        }
    }

    public boolean isUnderline() {
        return labelWidget.isUnderline();
    }

    public void setUnderline(boolean underline) {
        labelWidget.setUnderline(underline);
    }

    public boolean isStrikeOut() {
        return labelWidget.isStrikeOut();
    }

    public void setStrykeOut(boolean isStrikeOut) {
        labelWidget.setStrykeOut(isStrikeOut);
    }
}
