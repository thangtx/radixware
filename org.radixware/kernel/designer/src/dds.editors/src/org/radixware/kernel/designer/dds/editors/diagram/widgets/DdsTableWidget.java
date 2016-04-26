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
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.designer.dds.editors.diagram.DdsModelDiagram;


class DdsTableWidget extends DdsDefinitionTitledNodeWidget {

    private static final Image TABLE_IMAGE_20 = DdsDefinitionIcon.TABLE.getImage(20);
    private static final Image TABLE_OVERWRITE_IMAGE_20 = DdsDefinitionIcon.TABLE_OVERWRITE.getImage(20);
    private static final Image VIEW_IMAGE_20 = DdsDefinitionIcon.VIEW.getImage(20);
    private static final Color TABLE_COLOR = new Color(0x3F, 0xA9, 0xF5);
    private static final Color VIEW_COLOR = new Color(0x79, 0xC9, 0x44);
    private final DdsTableDef table;
    private final Widget container;

    private static Image calcImage(DdsTableDef table) {
        if (table instanceof DdsViewDef) {
            return VIEW_IMAGE_20;
        } else if (table.isOverwrite()) {
            return TABLE_OVERWRITE_IMAGE_20;
        } else {
            return TABLE_IMAGE_20;
        }
    }

    protected DdsTableWidget(DdsModelDiagram diagram, DdsTableDef table) {
        super(diagram, table, calcImage(table));
        this.table = table;

        if (table instanceof DdsViewDef) {
            setHeaderBackground(VIEW_COLOR);
        } else {
            setHeaderBackground(TABLE_COLOR);
        }

        //getActions().addAction(diagram.getConnectAction());

        this.container = new Widget(diagram);
        container.setLayout(LayoutFactory.createVerticalFlowLayout());
        this.addChild(container);
        update();
    }

    @Override
    public final void update() {
        container.removeChildren();

        for (DdsColumnDef column : table.getColumns().get(EScope.ALL)) {
            final DdsColumnWidget columnWidget = new DdsColumnWidget(getScene(), column);
            container.addChild(columnWidget);
        }

        boolean isSomeIndexAdded = false;
        for (DdsIndexDef index : table.getIndices().get(EScope.ALL)) {
            if (!isSomeIndexAdded) {
                addHorizontalSeparator(container);
                isSomeIndexAdded = true;
            }
            final Image indexImage = index.getIcon().getImage();
            final ImageLabelWidget indexWidget = new ImageLabelWidget(getScene(), indexImage, index.getName());
            if (index.isSecondaryKey()) {
                indexWidget.setBold(true);
            }
            if (index.isDeprecated()) {
                indexWidget.setStrykeOut(true);
            }
            container.addChild(indexWidget);
        }

        boolean isSomeTriggerAdded = false;
        for (DdsTriggerDef trigger : table.getTriggers().get(EScope.ALL)) {
            if (!isSomeTriggerAdded) {
                addHorizontalSeparator(container);
                isSomeTriggerAdded = true;
            }

            final Image triggerImage = trigger.getIcon().getImage();
            final ImageLabelWidget triggerWidget = new ImageLabelWidget(getScene(), triggerImage, trigger.getPresentableName());
            if (trigger.isDeprecated()) {
                triggerWidget.setStrykeOut(true);
            }
            container.addChild(triggerWidget);
        }
    }

    public DdsTableDef getTable() {
        return table;
    }
}
