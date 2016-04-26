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

/*
 * StatisticsImagePanel.java
 *
 * Created on Oct 13, 2009, 5:25:47 PM
 */
package org.radixware.kernel.designer.ads.localization.dialog;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.math.BigDecimal;
import java.util.List;
import org.openide.util.NbBundle;
import org.radixware.kernel.designer.ads.localization.dialog.StatisticsPanel.StatisticsInfo;


public class StatisticsImagePanel extends javax.swing.JPanel {

    private List<StatisticsInfo> statistic;
    private final static int SPACE = 4;
    private final static int INDENT_X = 30;
    private final static int INDENT_Y = 20;
    private final static int MAX_COLUMN_WIDTH = 60;
    private final static int MIN_COLUMN_WIDTH = 38;
    private int column_width;
    private Rectangle paintAreaRect;

    /**
     * Creates new form StatisticsImagePanel
     */
    public StatisticsImagePanel() {
        initComponents();
        paintAreaRect = new Rectangle(0, 0, this.getPreferredSize().width, this.getPreferredSize().height);//this.getBounds();
        calcPaintRect();
    }


    public void open(List<StatisticsInfo> statist) {
        this.statistic = statist;
        column_width = getColumnWidth(paintAreaRect.width, statist.size());
    }
    
    private void calcPaintRect() {
        paintAreaRect.x = INDENT_X;
        paintAreaRect.y = INDENT_Y;
        paintAreaRect.width -= INDENT_X * 2;
        paintAreaRect.height -= INDENT_Y * 2;
    }

    @Override
    public void paint(final Graphics gr) {
        ((Graphics2D) gr).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final int size;
        if (statistic != null){
            size = statistic.size();
        } else {
            size = 0;
        }
        final Color savedPen = gr.getColor();
        paintAreaRect = this.getBounds();
        calcPaintRect();
        column_width = getColumnWidth(paintAreaRect.width, size);

        drawBase((Graphics2D) gr, paintAreaRect);

        int pos_x = paintAreaRect.x + 1;
        for (int i = 0; i < size; i++) {
            final Color c = statistic.get(i).getColor();
            gr.setColor(c);
            drawColumn(gr, statistic.get(i).getPersent(), pos_x, paintAreaRect.height, column_width, paintAreaRect.height - (paintAreaRect.y + INDENT_Y));
            pos_x += column_width;
        }
        gr.setColor(savedPen);
    }

    private void drawColumn(final Graphics gr, final BigDecimal persent, final int pos_x, final int pos_y, final int column_width, final int whole_height) {
        final int height = whole_height * persent.intValue() / 100 - 1;

        gr.fillRect(pos_x, pos_y - height, column_width, height);

        final Color savedPen = gr.getColor();
        gr.setColor(Color.blue);
        final String s = persent.toString() + "%";
        final Dimension textDimension = getTextDimension(gr, s);
        final int str_pos_x = pos_x + (column_width - textDimension.width) / 2;
        final int str_pos_y = pos_y - height - SPACE;
        gr.drawString(s, str_pos_x, str_pos_y);
        gr.setColor(savedPen);
    }

    private int getColumnWidth(final int whole_width, final int size) {
        if (size <= 0) return 0;
        
        int width = whole_width / size;
        if (width > MAX_COLUMN_WIDTH) {
            width = MAX_COLUMN_WIDTH;
        } else if (width < MIN_COLUMN_WIDTH) {
            width = MIN_COLUMN_WIDTH;
            if (width * size > whole_width) {
                final int panel_width = this.getPreferredSize().width + (width * size - whole_width) + INDENT_X * 2;
                this.setPreferredSize(new Dimension(panel_width, this.getPreferredSize().height));
                this.setMinimumSize(new Dimension(panel_width, this.getPreferredSize().height));
                this.setMaximumSize(new Dimension(panel_width, this.getPreferredSize().height));
            }
        }
        return width;
    }

    private void drawBase(final Graphics2D gr, final Rectangle rect) {
        gr.setColor(Color.black);
        gr.drawLine(rect.x, rect.y, rect.x, rect.height);
        gr.drawLine(rect.x, rect.height, rect.width, rect.height);

        String s = "%";
        Dimension textDimension = getTextDimension(gr, s);
        int text_width = textDimension.width + SPACE;
        gr.drawString(s, rect.x - text_width, rect.y);

        s = NbBundle.getMessage(StatisticsImagePanel.class, "LANGS");
        textDimension = getTextDimension(gr, s);
        text_width = textDimension.width;
        final int text_height = textDimension.height + SPACE;
        gr.drawString(s, rect.width - text_width, rect.height + text_height);

        final Stroke oldStr = gr.getStroke();
        final float[] dashPattern = {4.0f, 5.0f};
        gr.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0));
        gr.drawLine(rect.x, rect.y + INDENT_Y, rect.width, rect.y + INDENT_Y);
        gr.setStroke(oldStr);

        s = "100";
        textDimension = getTextDimension(gr, s);
        text_width = textDimension.width + SPACE;
        gr.drawString(s, rect.x - text_width, rect.y + INDENT_Y);
    }

    private Dimension getTextDimension(final Graphics graphics, final String text) {
        final FontMetrics fontMetrics = graphics.getFontMetrics(graphics.getFont());
        return new Dimension(fontMetrics.stringWidth(text), fontMetrics.getHeight());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMaximumSize(new java.awt.Dimension(10000, 300));
        setMinimumSize(new java.awt.Dimension(400, 300));
        setPreferredSize(new java.awt.Dimension(400, 300));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
