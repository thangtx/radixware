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

package org.radixware.kernel.designer.ads.editors.clazz.forms.common;

import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;


public class DrawUtil {

    public final static int DEFAULT_ARC_WIDTH = 7;
    public final static int DEFAULT_ARC_HEIGHT = 7;
    public final static Color COLOR_WINDOW_TEXT = new Color(0, 0, 0);
    public final static Color COLOR_BUTTON = new Color(240, 240, 240);
    public final static Color COLOR_LIGHT = new Color(255, 255, 255);
    public final static Color COLOR_MID_LIGHT = new Color(227, 227, 227);
    public final static Color COLOR_DARK = new Color(160, 160, 160);
    public final static Color COLOR_MID = new Color(160, 160, 160);
    public final static Color COLOR_TEXT = new Color(0, 0, 0);
    public final static Color COLOR_BRIGHT_TEXT = new Color(255, 255, 255);
    public final static Color COLOR_BUTTON_TEXT = new Color(0, 0, 0);
    public final static Color COLOR_BASE = new Color(255, 255, 255);
    public final static Color COLOR_WINDOW = new Color(240, 240, 240);
    public final static Color COLOR_SHADOW = new Color(105, 105, 105);
    public final static Color COLOR_HIGLIGHT = new Color(51, 153, 255);
    public final static Color COLOR_HIGLIGHTED_TEXT = new Color(255, 255, 255);
    public final static Color COLOR_LINK = new Color(0, 0, 255);
    public final static Color COLOR_LINK_VISITED = new Color(255, 0, 255);
    public final static Color COLOR_ALTERNATE_BASE = new Color(246, 246, 246);
    public final static Color COLOR_NO_ROLE = new Color(0, 0, 0);
    public final static Color COLOR_TOOLTIP_BASE = new Color(255, 255, 220);
    public final static Color COLOR_TOOLTIP_TEXT = new Color(0, 0, 0);
//    final static public Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 10);
    public final static Font DEFAULT_FONT = new Font("MS Shell Dlg", Font.PLAIN, 12);
    static public FontMetrics DEFAULT_FONT_METRICS = null;

    public static void drawShadeLine(Graphics2D gr, int x1, int y1, int x2, int y2, boolean sunken /*= true*/, int lineWidth /*= 1*/, int midLineWidth /*= 0*/) {
        if (!(gr != null && lineWidth >= 0 && midLineWidth >= 0)) {
            return;
        }

        int tlw = lineWidth * 2 + midLineWidth; // total line width
        Color oldColor = gr.getColor(); // save pen

        if (sunken) {
            gr.setColor(COLOR_DARK);
        } else {
            gr.setColor(COLOR_LIGHT);
        }

        if (y1 == y2) { // horizontal line
            int y = y1 - tlw / 2;
            if (x1 > x2) { // swap x1 and x2
                int t = x1;
                x1 = x2;
                x2 = t;
            }
            x2--;
            for (int i = 0; i < lineWidth; i++) { // draw top shadow
                gr.drawPolyline(new int[]{x1 + i, x1 + i, x2 - i}, new int[]{y + tlw - 1 - i, y + i, y + i}, 3);
            }

            if (midLineWidth > 0) {
                gr.setColor(COLOR_MID);
                for (int i = 0; i < midLineWidth; i++) // draw lines in the middle
                {
                    gr.drawLine(x1 + lineWidth, y + lineWidth + i, x2 - lineWidth, y + lineWidth + i);
                }
            }

            if (sunken) {
                gr.setColor(COLOR_LIGHT);
            } else {
                gr.setColor(COLOR_DARK);
            }

            for (int i = 0; i < lineWidth; i++) { // draw bottom shadow
                gr.drawPolyline(new int[]{x1 + i, x2 - i, x2 - i}, new int[]{y + tlw - i - 1, y + tlw - i - 1, y + i + 1}, 3);
            }
        } else if (x1 == x2) { // vertical line
            int x = x1 - tlw / 2;
            if (y1 > y2) { // swap y1 and y2
                int t = y1;
                y1 = y2;
                y2 = t;
            }
            y2--;
            for (int i = 0; i < lineWidth; i++) { // draw left shadow
                gr.drawPolyline(new int[]{x + i, x + i, x + tlw - 1}, new int[]{y2, y1 + i, y1 + i}, 3);
            }
            if (midLineWidth > 0) {
                gr.setColor(COLOR_MID);
                for (int i = 0; i < midLineWidth; i++) // draw lines in the middle
                {
                    gr.drawLine(x + lineWidth + i, y1 + lineWidth, x + lineWidth + i, y2);
                }
            }

            if (sunken) {
                gr.setColor(COLOR_LIGHT);
            } else {
                gr.setColor(COLOR_DARK);
            }

            for (int i = 0; i < lineWidth; i++) { // draw right shadow
                gr.drawPolyline(new int[]{x + lineWidth, x + tlw - i - 1, x + tlw - i - 1}, new int[]{y2 - i, y2 - i, y1 + lineWidth}, 3);
            }
        }

        gr.setColor(oldColor);
    }

    public static void drawShadeLine(Graphics2D gr, Point p1, Point p2, boolean sunken, int lineWidth, int midLineWidth) {
        drawShadeLine(gr, p1.x, p1.y, p2.x, p2.y, sunken, lineWidth, midLineWidth);
    }

    public static void drawShadeRect(Graphics2D gr, int x, int y, int w, int h, boolean sunken /*= false*/, int lineWidth /*= 1*/, int midLineWidth /*= 0*/, Paint fill /*= 0*/) {
        if (w == 0 || h == 0) {
            return;
        }
        if (!(w > 0 && h > 0 && lineWidth >= 0 && midLineWidth >= 0)) {
            return;
        }

        Color oldColor = gr.getColor();

        if (sunken) {
            gr.setColor(COLOR_DARK);
        } else {
            gr.setColor(COLOR_LIGHT);
        }

        int x1 = x, y1 = y, x2 = x + w - 1, y2 = y + h - 1;

        if (lineWidth == 1 && midLineWidth == 0) {// standard shade rectangle
            gr.drawRect(x1, y1, w - 2, h - 2);

            if (sunken) {
                gr.setColor(COLOR_LIGHT);
            } else {
                gr.setColor(COLOR_DARK);
            }

            // draw bottom/right lines
            gr.drawLine(x1 + 1, y1 + 1, x2 - 2, y1 + 1);
            gr.drawLine(x1 + 1, y1 + 2, x1 + 1, y2 - 2);
            gr.drawLine(x1, y2, x2, y2);
            gr.drawLine(x2, y1, x2, y2 - 1);

        } else { // more complicated
            int m = lineWidth + midLineWidth;
            int j = 0, k = m;
            for (int i = 0; i < lineWidth; i++) { // draw top shadow
                gr.drawLine(x1 + i, y2 - i, x1 + i, y1 + i);
                gr.drawLine(x1 + i, y1 + i, x2 - i, y1 + i);
                gr.drawLine(x1 + k, y2 - k, x2 - k, y2 - k);
                gr.drawLine(x2 - k, y2 - k, x2 - k, y1 + k);
                k++;
            }

            gr.setColor(COLOR_MID);
            j = lineWidth * 2;
            for (int i = 0; i < midLineWidth; i++) { // draw lines in the middle
                gr.drawRect(x1 + lineWidth + i, y1 + lineWidth + i, w - j - 1, h - j - 1);
                j += 2;
            }

            if (sunken) {
                gr.setColor(COLOR_LIGHT);
            } else {
                gr.setColor(COLOR_DARK);
            }

            k = m;
            for (int i = 0; i < lineWidth; i++) { // draw bottom shadow
                gr.drawLine(x1 + 1 + i, y2 - i, x2 - i, y2 - i);
                gr.drawLine(x2 - i, y2 - i, x2 - i, y1 + i + 1);
                gr.drawLine(x1 + k, y2 - k, x1 + k, y1 + k);
                gr.drawLine(x1 + k, y1 + k, x2 - k, y1 + k);
                k++;
            }
        }

        if (fill != null) {
            int tlw = lineWidth + midLineWidth;
            Paint oldPaint = gr.getPaint();
            gr.setPaint(fill);
            gr.fillRect(x + tlw, y + tlw, w - 2 * tlw, h - 2 * tlw);
            gr.setPaint(oldPaint);
        }

        gr.setColor(oldColor); // restore pen
    }

    public static void drawShadeRect(Graphics2D gr, Rectangle r, boolean sunken, int lineWidth, int midLineWidth, Paint fill) {
        drawShadeRect(gr, r.x, r.y, r.width, r.height, sunken, lineWidth, midLineWidth, fill);
    }

    public static void drawShadePanel(Graphics2D gr, int x, int y, int w, int h, boolean sunken, int lineWidth, Paint fill) {
        if (w == 0 || h == 0) {
            return;
        }
        if (!(w > 0 && h > 0 && lineWidth >= 0)) {
            return;
        }

        Color shade = COLOR_DARK;
        Color light = COLOR_LIGHT;

        if (fill != null) {
            if (fill.equals(shade)) {
                shade = COLOR_SHADOW;
            }
            if (fill.equals(light)) {
                light = COLOR_MID_LIGHT;
            }
        }

        Color oldPen = gr.getColor(); // save pen

        if (sunken) {
            gr.setColor(shade);
        } else {
            gr.setColor(light);
        }

        int x1, y1, x2, y2;
        int i;
        x1 = x;
        y1 = y2 = y;
        x2 = x + w - 2;

        for (i = 0; i < lineWidth; i++) { // top shadow
            gr.drawLine(x1, y1++, x2--, y2++);
        }

        x2 = x1;
        y1 = y + h - 2;

        for (i = 0; i < lineWidth; i++) { // left shadow
            gr.drawLine(x1++, y1, x2++, y2--);
        }

        if (sunken) {
            gr.setColor(light);
        } else {
            gr.setColor(shade);
        }

        x1 = x;
        y1 = y2 = y + h - 1;
        x2 = x + w - 1;

        for (i = 0; i < lineWidth; i++) { // bottom shadow
            gr.drawLine(x1++, y1--, x2, y2--);
        }

        x1 = x2;
        y1 = y;
        y2 = y + h - lineWidth - 1;

        for (i = 0; i < lineWidth; i++) { // right shadow
            gr.drawLine(x1--, y1++, x2--, y2);
        }

        if (fill != null) { // fill with fill color
            Paint oldPaint = gr.getPaint();
            gr.setPaint(fill);
            gr.fillRect(x + lineWidth, y + lineWidth, w - lineWidth * 2, h - lineWidth * 2);
            gr.setPaint(oldPaint);
        }

        gr.setColor(oldPen); // restore pen
    }

    public static void drawShadePanel(Graphics2D gr, Rectangle r, boolean sunken, int lineWidth, Paint fill) {
        drawShadePanel(gr, r.x, r.y, r.width, r.height, sunken, lineWidth, fill);
    }

    public static Dimension textSize(Graphics2D gr, String text) {
        if (text == null || text.isEmpty()) {
            return new Dimension();
        }

        FontMetrics metrics = gr.getFontMetrics(DEFAULT_FONT);
        int h = metrics.getHeight();
        int w = metrics.stringWidth(text);

        return new Dimension(w, h);
    }

    public static void drawText(Graphics2D gr, Rectangle r, EAlignment hAlign, EAlignment vAlign, boolean enabled, String text) {
        drawText(gr, r, hAlign, vAlign, enabled, DRAW_LEFT_TO_RIGHT, text);
    }

    public static void drawText(Graphics2D gr, Rectangle r, EAlignment hAlign, EAlignment vAlign, boolean enabled, int drawMode, String text) {
        drawText(gr, r, hAlign, vAlign, enabled, drawMode, null, text);
    }
    public static final int DRAW_LEFT_TO_RIGHT = 0;
    public static final int DRAW_TOP_TO_BOTTOM = 1;
    public static final int DRAW_BOTTOM_TO_TOP = 2;

    public static void drawText(Graphics2D gr, Rectangle r, EAlignment hAlign, EAlignment vAlign, boolean enabled, int drawMode, Paint fill, String text) {
        if (text == null || text.isEmpty()) {
            return;
        }

        Color savedPen = gr.getColor();
        Font savedFont = gr.getFont();

        gr.setFont(DEFAULT_FONT);
        FontMetrics metrics = gr.getFontMetrics();

        gr.setColor(!enabled ? COLOR_DARK : COLOR_BUTTON_TEXT);

        if (drawMode == DRAW_LEFT_TO_RIGHT) {
            int h = metrics.getHeight();
            int w = metrics.stringWidth(text);
            int x = r.x, y = r.y;

            if (hAlign.equals(EAlignment.AlignHCenter)) {
                x += (r.width - w) / 2;
            } else if (hAlign.equals(EAlignment.AlignRight)) {
                x += r.width - w;
            }

            if (vAlign.equals(EAlignment.AlignVCenter)) {
                y += (r.height + h) / 2 - 1;
            } else if (vAlign.equals(EAlignment.AlignBottom)) {
                y += r.height - 1;
            } else if (vAlign.equals(EAlignment.AlignTop)) {
                y += h - 1;
            }

            if (fill != null) { // fill with fill color
                Paint oldPaint = gr.getPaint();
                gr.setPaint(fill);
                gr.fillRect(x - 1, y - h - 1, w + 2, h + 2);
                gr.setPaint(oldPaint);
            }

            gr.drawString(text, x, y - metrics.getDescent());
            //gr.drawRect(x, y - h, w, h);
        }

        if (drawMode == DRAW_BOTTOM_TO_TOP) {
            int h = metrics.stringWidth(text);
            int w = metrics.getHeight();
            int x = r.x, y = r.y + r.height;

            if (hAlign.equals(EAlignment.AlignLeft)) {
                x += w - 1;
            } else if (hAlign.equals(EAlignment.AlignHCenter)) {
                x += (r.width + w) / 2 - 1;
            } else if (hAlign.equals(EAlignment.AlignRight)) {
                x += r.width - w;
            }

            if (vAlign.equals(EAlignment.AlignTop)) {
                y -= r.height + h;
            } else if (vAlign.equals(EAlignment.AlignVCenter)) {
                y -= (r.height - h) / 2;
            } else if (vAlign.equals(EAlignment.AlignBottom)) {
                y -= 0;
            }

            if (fill != null) { // fill with fill color
                Paint oldPaint = gr.getPaint();
                gr.setPaint(fill);
                gr.fillRect(x - h - 1, y - 1, w + 2, h + 2);
                gr.setPaint(oldPaint);
            }

            AffineTransform at = new AffineTransform();
            at.rotate(-0.5 * Math.PI);

            gr.setFont(DEFAULT_FONT.deriveFont(at));
            gr.drawString(text, x - metrics.getDescent(), y);
        }

        if (drawMode == DRAW_TOP_TO_BOTTOM) {
            int h = metrics.stringWidth(text);
            int w = metrics.getHeight();
            int x = r.x, y = r.y;

            if (hAlign.equals(EAlignment.AlignLeft)) {
                x += 0;
            } else if (hAlign.equals(EAlignment.AlignHCenter)) {
                x += (r.width - w) / 2 - 1;
            } else if (hAlign.equals(EAlignment.AlignRight)) {
                x += r.width - w - 1;
            }

            if (vAlign.equals(EAlignment.AlignTop)) {
                y += 0;
            } else if (vAlign.equals(EAlignment.AlignVCenter)) {
                y += (r.height - h) / 2;
            } else if (vAlign.equals(EAlignment.AlignBottom)) {
                y += r.height - w;
            }

            if (fill != null) { // fill with fill color
                Paint oldPaint = gr.getPaint();
                gr.setPaint(fill);
                gr.fillRect(x - 1, y - 1, w + 2, h + 2);
                gr.setPaint(oldPaint);
            }

            AffineTransform at = new AffineTransform();
            at.rotate(+0.5 * Math.PI);

            gr.setFont(DEFAULT_FONT.deriveFont(at));
            gr.drawString(text, x + metrics.getDescent(), y);
        }

        gr.setColor(savedPen);
        gr.setFont(savedFont);
    }

    /*
    public static void drawWinButton(Graphics2D gr, int x, int y, int w, int h, boolean sunken, Paint fill) {
    // TODO:
    }

    public static void drawWinButton(Graphics2D gr, Rectangle r, boolean sunken, Paint fill) {
    // TODO:
    }

    public static void drawWinPanel(Graphics2D gr, int x, int y, int w, int h, boolean sunken, Paint fill) {
    // TODO:
    }

    public static void drawWinPanel(Graphics2D gr, Rectangle r, boolean sunken, Paint fill) {
    // TODO:
    }
     */
    public static void drawPlainRect(Graphics2D gr, int x, int y, int w, int h, Color c, int lineWidth, Paint fill) {
        if (w == 0 || h == 0) {
            return;
        }
        if (!(w > 0 && h > 0 && lineWidth >= 0)) {
            return;
        }

        Color savedPen = gr.getColor();
        Paint savedPaint = gr.getPaint();

        gr.setColor(c);
        for (int i = 0; i < lineWidth; i++) {
            gr.drawRect(x + i, y + i, w - i * 2 - 1, h - i * 2 - 1);
        }

        if (fill != null) { // fill with fill color
            gr.setPaint(fill);
            gr.fillRect(x + lineWidth, y + lineWidth, w - lineWidth * 2, h - lineWidth * 2);
        }

        gr.setColor(savedPen);
        gr.setPaint(savedPaint);
    }

    public static void drawPlainRect(Graphics2D gr, Rectangle r, Color c, int lineWidth, Paint fill) {
        drawPlainRect(gr, r.x, r.y, r.width, r.height, c, lineWidth, fill);
    }

    public static void drawPlainRoundRect(Graphics2D gr, int x, int y, int w, int h, Color c, int lineWidth, Paint fill) {
        if (w == 0 || h == 0) {
            return;
        }
        if (!(w > 0 && h > 0 && lineWidth >= 0)) {
            return;
        }

        Color savedPen = gr.getColor();
        Paint savedPaint = gr.getPaint();

        gr.setColor(c);
        for (int i = 0; i < lineWidth; i++) {
            gr.drawRoundRect(x + i, y + i, w - i * 2 - 1, h - i * 2 - 1, DrawUtil.DEFAULT_ARC_WIDTH, DrawUtil.DEFAULT_ARC_HEIGHT);
        }

        if (fill != null) { // fill with fill color
            gr.setPaint(fill);
            gr.fillRoundRect(x + lineWidth, y + lineWidth, w - lineWidth * 2, h - lineWidth * 2, DrawUtil.DEFAULT_ARC_WIDTH, DrawUtil.DEFAULT_ARC_HEIGHT);
        }

        gr.setColor(savedPen);
        gr.setPaint(savedPaint);
    }

    public static void drawPlainRoundRect(Graphics2D gr, Rectangle r, Color c, int lineWidth, Paint fill) {
        drawPlainRoundRect(gr, r.x, r.y, r.width, r.height, c, lineWidth, fill);
    }

    public static void drawLine(Graphics2D gr, int x1, int y1, int x2, int y2, Color color) {
        Paint oldPaint = gr.getPaint();
        gr.setPaint(color);
        gr.drawLine(x1, y1, x2, y2);
        gr.setPaint(oldPaint);
    }
}
