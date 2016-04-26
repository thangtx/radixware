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
package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;

class Ruler extends JComponent {

    public static final int THICKNESS_PX = 18;

    public enum EDirection {

        HORIZONTAL,
        VERTICAL
    }
    private final EDirection direction;
    private Color bgColor = Color.WHITE;
    private AdsReportFormDiagram diagram;

    public Ruler(AdsReportFormDiagram diagram, final EDirection direction) {
        super();
        this.direction = direction;
        this.diagram = diagram;
        setOpaque(true);
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(final Color bgColor) {
        if (!this.bgColor.equals(bgColor)) {
            this.bgColor = bgColor;
            repaint();
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Font font = getFont().deriveFont(Font.BOLD, 10);
        g.setFont(font);

        final int widthPx = getWidth();
        final int heightPx = getHeight();
        final int lengthPx = (direction == EDirection.HORIZONTAL ? widthPx : heightPx);
        g.setColor(bgColor);
        g.fillRect(0, 0, widthPx, heightPx);

        if (diagram.getMode() == AdsReportForm.Mode.GRAPHICS) {
            g.setColor(Color.GRAY);

            for (double mm = 1.0; MmUtils.mm2px(mm) < lengthPx; mm += 1.0) {
                final int px = MmUtils.mm2px(mm);
                if (direction == EDirection.HORIZONTAL) {
                    g.drawLine(px, heightPx - 3, px, heightPx);
                } else {
                    g.drawLine(widthPx - 3, px, widthPx, px);
                }
            }

            g.setColor(Color.BLACK);
            for (double mm = 2.5; MmUtils.mm2px(mm) < lengthPx; mm += 2.5) {
                final int px = MmUtils.mm2px(mm);
                if (direction == EDirection.HORIZONTAL) {
                    g.drawLine(px, heightPx - 8, px, heightPx - 5);
                } else {
                    g.drawLine(widthPx - 8, px, widthPx - 5, px);
                }

            }
        }else{
            g.setColor(Color.BLACK);
            for (int col = 1; char2Px(col) < lengthPx; col += 1) {
                final int px = char2Px(col);
                if (direction == EDirection.HORIZONTAL) {
                    g.drawLine(px, heightPx - 8, px, heightPx - 5);
                } else {
                    g.drawLine(widthPx - 8, px, widthPx - 5, px);
                }

            }
        }

        g.setColor(Color.BLACK);

        if (diagram.getMode() == AdsReportForm.Mode.GRAPHICS) {
            int iCm = 1;
            for (double mm = 10.0; MmUtils.mm2px(mm) < lengthPx; mm += 10.0) {
                final int px = MmUtils.mm2px(mm);
                if (direction == EDirection.HORIZONTAL) {
                    g.drawLine(px, heightPx - 5, px, heightPx);
                } else {
                    g.drawLine(widthPx - 5, px, widthPx, px);
                }
                final String text = String.valueOf(iCm);

                if (direction == EDirection.HORIZONTAL) {
                    final int textWidthPx = getFontMetrics(font).stringWidth(text);
                    g.drawString(text, px - textWidthPx / 2 + 1, heightPx - 9);
                }

                iCm++;
            }
        } else {
            int iCm = 1;
            for (int col = 10; char2Px(col) < lengthPx; col += 10) {
                final int px = char2Px(col);
                if (direction == EDirection.HORIZONTAL) {
                    g.drawLine(px, heightPx - 5, px, heightPx);
                } else {
                    g.drawLine(widthPx - 5, px, widthPx, px);
                }
                final String text = String.valueOf(iCm * 10);

                if (direction == EDirection.HORIZONTAL) {
                    final int textWidthPx = getFontMetrics(font).stringWidth(text);
                    g.drawString(text, px - textWidthPx / 2 + 1, heightPx - 9);
                } 

                iCm++;
            }
        }

        g.setColor(Color.BLACK);
        if (direction == EDirection.HORIZONTAL) {
            g.drawLine(0, heightPx - 1, widthPx, heightPx - 1);
        } else {
            g.drawLine(widthPx - 1, 0, widthPx - 1, heightPx);
        }
    }

    private int char2Px(int chars) {
        return direction == EDirection.HORIZONTAL ? TxtUtils.columns2Px(chars) : TxtUtils.rows2Px(chars);
    }

}
