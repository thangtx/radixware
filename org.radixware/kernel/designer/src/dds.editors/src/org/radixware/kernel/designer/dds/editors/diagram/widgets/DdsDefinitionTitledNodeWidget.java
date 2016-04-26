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
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.util.Map;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject.EditStateChangedEvent;
import org.radixware.kernel.common.defs.RadixObject.RenameEvent;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.designer.dds.editors.diagram.DdsModelDiagram;

/**
 * {@linkplain DdsDefinition} node widget that displays definition name and icon in the header of the node.
 */
abstract class DdsDefinitionTitledNodeWidget extends DdsDefinitionNodeWidget {

    private final DdsDefinition.RenameListener renameListener = new DdsDefinition.RenameListener() {

        @Override
        public void onEvent(RenameEvent event) {
            DdsDefinitionTitledNodeWidget.this.labelWidget.setLabel(event.radixObject.getName());
        }
    };
    private final DdsDefinition.EditStateChangeListener editStateChangeListener = new DdsDefinition.EditStateChangeListener() {

        @Override
        public void onEvent(EditStateChangedEvent event) {
            DdsDefinitionTitledNodeWidget.this.updateHeaderLabelColor(event.radixObject.getEditState());
        }
    };
    private final static Color DARK_GREEN = new Color(0, 128, 0);

    private void updateHeaderLabelColor(Definition.EEditState editState) {
        Color headerLabelColor = editState.getColor();
        switch (editState) {
            case MODIFIED:
                headerLabelColor = Color.BLUE;
                break;
            case NEW:
                headerLabelColor = DARK_GREEN;
                break;
            case NONE:
                headerLabelColor = Color.BLACK;
                break;
            default:
                throw new IllegalStateException("Unknown edit state in " + DdsDefinitionTitledNodeWidget.class.getName());
        }
        this.labelWidget.setForeground(headerLabelColor);
    }
    private static final Color backgroundColor = new Color(0xFF, 0xFA, 0xC0);
    private static final Border headerImageBorder = BorderFactory.createEmptyBorder(1);
    private static final Border headerLabelBorder = BorderFactory.createEmptyBorder(2);
    private final LabelWidget labelWidget;
    private final Widget headerWidget;

    protected DdsDefinitionTitledNodeWidget(DdsModelDiagram diagram, DdsDefinition ddsDefinition, Image headerImage) {
        super(diagram, ddsDefinition);

        setBackground(backgroundColor);
        setLayout(LayoutFactory.createVerticalFlowLayout());

        headerWidget = new GradientWidget(diagram);
        headerWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());

        final ImageWidget imageWidget = new ImageWidget(diagram, headerImage);
        imageWidget.setBorder(headerImageBorder);
        headerWidget.addChild(imageWidget);

        headerWidget.addChild(new SeparatorWidget(this.getScene(), SeparatorWidget.Orientation.VERTICAL));

        final Widget leftSeparator = new Widget(diagram);
        leftSeparator.setPreferredSize(new Dimension(4, 0));
        headerWidget.addChild(leftSeparator);

        labelWidget = new LabelWidget(diagram, ddsDefinition.getName());
        labelWidget.setBorder(headerLabelBorder);
        labelWidget.setAlignment(LabelWidget.Alignment.CENTER);
        labelWidget.setVerticalAlignment(LabelWidget.VerticalAlignment.CENTER);
        Font font = diagram.getDefaultFont().deriveFont(Font.BOLD);
        if (ddsDefinition.isDeprecated()) {
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            font = new Font(attributes);
        }
        labelWidget.setFont(font);
        headerWidget.addChild(labelWidget);

        Widget rightSeparator = new Widget(diagram);
        rightSeparator.setPreferredSize(new Dimension(4, 0));
        headerWidget.addChild(rightSeparator);

        this.addChild(headerWidget);
        addHorizontalSeparator(this);

        updateHeaderLabelColor(ddsDefinition.getEditState());
        ddsDefinition.addRenameListener(renameListener);
        ddsDefinition.addEditStateListener(editStateChangeListener);
    }

    protected void addHorizontalSeparator(Widget into) {
        final Scene scene = this.getScene();
        into.addChild(new SeparatorWidget(this.getScene(), SeparatorWidget.Orientation.HORIZONTAL));
        final Widget emptyWidget = new Widget(scene);
        emptyWidget.setPreferredSize(new Dimension(0, 1));
        into.addChild(emptyWidget);
    }

    protected void setHeaderBackground(final Color color) {
        headerWidget.setBackground(color);
    }
}
