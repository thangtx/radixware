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
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsExtTableDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef.ColumnInfo;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.designer.dds.editors.diagram.DdsModelDiagram;


class DdsExtTableWidget extends DdsDefinitionTitledNodeWidget {

    private static final Color headerColor = new Color(0xBE, 0xCD, 0xD4);
    private static final Color errorHeaderColor = new Color(0xFF, 0x7B, 0xAD);
    private static final Image EXT_TABLE_IMAGE_20 = DdsDefinitionIcon.EXT_TABLE.getImage(20);
    private final DdsExtTableDef extTable;
    private final Widget container;

    protected DdsExtTableWidget(DdsModelDiagram diagram, DdsExtTableDef extTable) {
        super(diagram, extTable, EXT_TABLE_IMAGE_20);
        this.extTable = extTable;

        //getActions().addAction(diagram.getConnectAction());

        this.container = new Widget(diagram);
        this.addChild(container);
        container.setLayout(LayoutFactory.createVerticalFlowLayout());
        update();
    }

    @Override
    public final void update() {
        container.removeChildren();

        DdsTableDef table = extTable.findTable();
        if (table != null) {
            this.setHeaderBackground(headerColor);
            for (ColumnInfo columnInfo : table.getPrimaryKey().getColumnsInfo()) {
                DdsColumnDef column = columnInfo.findColumn();
                if (column != null) {
                    DdsColumnWidget columnWidget = new DdsColumnWidget(getScene(), column);
                    container.addChild(columnWidget);
                }
            }
        } else {
            this.setHeaderBackground(errorHeaderColor);
            final LabelWidget labelWidget = new LabelWidget(getScene(), "Source table not found"); // TODO: translate
            container.addChild(labelWidget);
        }
    }

    public DdsExtTableDef getExtTable() {
        return extTable;
    }
}
