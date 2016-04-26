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

package org.radixware.kernel.designer.ads.editors.pages;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyUsageSupport.PropertyRef;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;


public class EditorPagePropTableCellRenderer extends DefaultTableCellRenderer {

    private static class LabelBorder implements Border {

        private boolean drawLeft, drawRight;
        private Color color;

        public LabelBorder(EditorPagePropTableModel.PageItem item, int r, int c) {
            if (item != null) {
                color = Color.gray;
                if (item.table == null) {
                    drawLeft = true;
                    drawRight = true;
                } else {
                    if (item.table.isSpanStart(r, c)) {
                        drawRight = false;
                        drawLeft = true;
                    } else if (item.table.isSpanMid(r, c)) {
                        drawRight = false;
                        drawLeft = false;
                    } else if (item.table.isSpanLast(r, c)) {
                        drawRight = true;
                        drawLeft = false;
                    } else {
                        drawLeft = true;
                        drawRight = true;
                    }
                }
            } else {
                drawLeft = true;
                drawRight = true;
                color = Color.lightGray;
            }


        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(color);
            g.drawLine(x, y, x + width - 1, y);
            g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
            if (drawLeft) {
                g.drawLine(x, y, x, y + height - 1);
            }
            if (drawRight) {
                g.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
            }
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(0, 3, 0, 0);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        final Component comp = super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
        if (value != null) {
            final EditorPagePropTableModel.PageItem propertyRef = (EditorPagePropTableModel.PageItem) value;
            LabelBorder border = new LabelBorder(propertyRef, row, column);

            ((JLabel) comp).setHorizontalAlignment(LEFT);
            ((JLabel) comp).setIcon(null);
            ((JLabel) comp).setHorizontalTextPosition(TRAILING);


            if (border.drawLeft) {
                ((JLabel) comp).setText(propertyRef.getDisplayName());
                if (propertyRef.isGlutToLeft()) {
                    ((JLabel) comp).setHorizontalAlignment(LEFT);
                    ((JLabel) comp).setIcon(RadixWareDesignerIcon.ARROW.LEFT.getIcon(13, 13));
                    ((JLabel) comp).setHorizontalTextPosition(TRAILING);
                }
            }
            if (border.drawRight) {
                if (propertyRef.isGlutToRight()) {
                    ((JLabel) comp).setHorizontalAlignment(RIGHT);
                    ((JLabel) comp).setIcon(RadixWareDesignerIcon.ARROW.RIGHT.getIcon(13, 13));
                    ((JLabel) comp).setHorizontalTextPosition(LEADING);
                }
            }
            ((JLabel) comp).setBorder(border);
            if (!propertyRef.isValid()) {
                comp.setForeground(Color.RED);
            } else {
                comp.setForeground(null);
            }
            if (!isSelected) {
                ((JLabel) comp).setBackground(Color.white);
            }



        } else {
            ((JLabel) comp).setHorizontalAlignment(LEFT);
            ((JLabel) comp).setIcon(null);
            ((JLabel) comp).setHorizontalTextPosition(TRAILING);
            ((JLabel) comp).setBorder(new LabelBorder(null, row, column));
            if (!isSelected) {
                ((JLabel) comp).setBackground(Color.lightGray);
            }
        }
        return comp;
    }
}