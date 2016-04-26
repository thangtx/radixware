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
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import java.awt.BasicStroke;
import org.radixware.kernel.common.defs.ads.ui.UiProperties;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EPenStyle;
import org.radixware.kernel.common.defs.ads.ui.enums.EViewMode;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawItemWidgets;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;

public class TableItem extends Item {

    public static final TableItem DEFAULT = new TableItem();

   
    public TableItem() {
        super(Group.GROUP_ITEM_WIDGETS, NbBundle.getMessage(TableItem.class, "Table"), AdsMetaInfo.TABLE_WIDGET_CLASS);
    }
   

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        Shape clip = gr.getClip();
        gr.clipRect(r.x, r.y, r.width, r.height);
        DrawUtil.drawPlainRect(gr, r, DrawUtil.COLOR_DARK, 1, DrawUtil.COLOR_BASE);

        Rectangle rect = r.getBounds();
        AdsItemWidgetDef def = (AdsItemWidgetDef) node;
        if (def == null) {
            return;
        }
        boolean hasRows = (def.getRows() != null) && (def.getRows().size() > 0);
        boolean hasColumns = (def.getColumns() != null) && (def.getColumns().size() > 0);
        if (hasColumns) {
            drawColumns(gr, rect, hasRows, node);
        }
        if (hasRows) {
            drawRows(gr, r.getBounds(), hasColumns, node);
        }

        AdsUIProperty.BooleanProperty showGrid = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "showGrid");
        AdsUIProperty.EnumValueProperty gridStyle = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "gridStyle");
        if (showGrid.value) {
            drawGridLines(gr, rect, (AdsItemWidgetDef) node, (EPenStyle) gridStyle.value, hasColumns);
        }
        gr.setClip(clip);
    }

    private void drawColumns(Graphics2D gr, Rectangle rect, boolean hasRows, RadixObject node) {
        if (hasRows) {
            rect.x = rect.x + DrawItemWidgets.ROW_WIDTH;
            rect.width = rect.width - DrawItemWidgets.ROW_WIDTH;
        }
        DrawItemWidgets.paintBackgroundWidgetsWithColumn(gr, rect.getBounds(), node);
    }

    private void drawRows(Graphics2D gr, Rectangle rectRow, boolean hasColumns, RadixObject node) {
        rectRow.x = rectRow.x + 1;
        rectRow.y = rectRow.y + 1;
        rectRow.width = DrawItemWidgets.ROW_WIDTH;
        rectRow.height = rectRow.height - 2;
        drawRowTitle(gr, rectRow, node, hasColumns);
    }

    private void drawRowTitle(Graphics2D gr, Rectangle rect, RadixObject node, boolean hasColumn) {
        AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
        DrawUtil.drawPlainRect(gr, rect, DrawUtil.COLOR_BUTTON, 1, DrawUtil.COLOR_BUTTON);
        int pos = rect.y;
        rect.y = rect.y - 1;
        rect.height = DrawItemWidgets.ROW_HEIGHT;
        DrawItemWidgets.drawBackgroundForTitle(gr, rect, 1, 2);
        if (hasColumn) {
            rect.y = rect.y + DrawItemWidgets.COLUMN_HEIGHT;
            DrawUtil.drawLine(gr, rect.x, rect.y, rect.x + rect.width, rect.y, DrawUtil.COLOR_DARK);
        }

        AdsItemWidgetDef widget = (AdsItemWidgetDef) node;
        for (int i = 0, size = widget.getRows().size(); i < size; i++) {
            DrawItemWidgets.drawBackgroundForTitle(gr, rect, 1, 2);
            Shape clip = gr.getClip();
            gr.clipRect(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
            DrawItemWidgets.drawText(gr, rect.getBounds(), widget.getRows().get(i), enabled.value, false, EViewMode.ListMode, false);
            gr.setClip(clip);
            rect.y = rect.y + rect.height;
            DrawUtil.drawLine(gr, rect.x + 4, rect.y, rect.x + rect.width - 4, rect.y, DrawUtil.COLOR_DARK);
            DrawUtil.drawLine(gr, rect.x + 4, rect.y + 1, rect.x + rect.width - 4, rect.y + 1, DrawUtil.COLOR_BASE);

        }
        DrawUtil.drawLine(gr, rect.x + DrawItemWidgets.COLUMN_WIDTH - 1, pos, rect.x + DrawItemWidgets.COLUMN_WIDTH - 1, pos + DrawItemWidgets.COLUMN_HEIGHT, DrawUtil.COLOR_DARK);
    }

    private void drawGridLines(Graphics2D gr, Rectangle rect, AdsItemWidgetDef node, EPenStyle penStyle, boolean hasColumn) {
        if (!((node.getRows() != null) && (node.getRows().size() > 0)
                && (node.getColumns() != null) && (node.getColumns().size() > 0))) {
            return;
        }
        Stroke oldStr = gr.getStroke();
        setStrokeForGridLines(gr, penStyle);

        if (hasColumn) {
            rect.y = rect.y + DrawItemWidgets.COLUMN_HEIGHT;
        }
        rect.height = 0;
        rect.width = 0;

        if (node.getRows() != null) {
            rect.height = node.getRows().size() * DrawItemWidgets.ROW_HEIGHT + 1;
        }
        if (node.getColumns() != null) {
            rect.width = node.getColumns().size() * DrawItemWidgets.COLUMN_WIDTH + 1;
        }
        DrawUtil.drawPlainRect(gr, rect, DrawUtil.COLOR_DARK, 1, null);
        if (node.getRows() != null) {
            for (int i = 0, size = node.getRows().size() - 1; i < size; i++) {
                int y = rect.y + DrawItemWidgets.COLUMN_HEIGHT * (i + 1);
                DrawUtil.drawLine(gr, rect.x, y, rect.x + rect.width - 1, y, DrawUtil.COLOR_DARK);
            }
        }
        if (node.getColumns() != null) {
            for (int i = 0, size = node.getColumns().size() - 1; i < size; i++) {
                int x = rect.x + DrawItemWidgets.ROW_WIDTH * (i + 1);
                DrawUtil.drawLine(gr, x, rect.y, x, rect.y + rect.height - 1, DrawUtil.COLOR_DARK);
            }
        }
        gr.setStroke(oldStr);
    }

    private void setStrokeForGridLines(Graphics2D gr, EPenStyle penStyle) {

        if (penStyle == EPenStyle.SolidLine) {
            return;
        } else if (penStyle == EPenStyle.DashLine) {
            float[] dashPattern = {4.0f, 5.0f};
            gr.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0));
        } else if (penStyle == EPenStyle.DotLine) {
            float[] dashPattern = {1.0f, 2.0f};
            gr.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0));
        } else if (penStyle == EPenStyle.DashDotLine) {
            float[] dashPattern = {4.0f, 5.0f, 1.0f, 5.0f};
            gr.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0));
        } else if (penStyle == EPenStyle.DashDotDotLine) {
            float[] dashPattern = {4.0f, 5.0f, 1.0f, 5.0f, 1.0f, 5.0f};
            gr.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0));
        } else if (penStyle == EPenStyle.DashDotDotLine) {
            float[] dashPattern = {0.0f};
            gr.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0));
        }

    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        Rectangle rect = r.getBounds();
        rect.x = rect.x + DrawItemWidgets.ROW_WIDTH - 1;
        rect.width = rect.width - DrawItemWidgets.ROW_WIDTH;
        DrawItemWidgets.paint(gr, rect, node, true);
    }
}
