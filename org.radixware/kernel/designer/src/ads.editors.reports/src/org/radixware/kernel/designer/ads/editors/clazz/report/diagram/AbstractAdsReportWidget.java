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

import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.JComponent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.enums.EReportBorderStyle;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.selection.SelectionEvent;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;

public abstract class AbstractAdsReportWidget extends JComponent {

    private final RadixObject radixObject;
    private final AdsReportFormDiagram diagram;

    public AbstractAdsReportWidget(final AdsReportFormDiagram diagram, final RadixObject radixObject) {
        super();
        this.diagram = diagram;
        this.radixObject = radixObject;
    }

    public RadixObject getRadixObject() {
        return radixObject;
    }

    public void edit() {
        EditorsManager.getDefault().open(radixObject);
        //update();  // RADIX-3370
        //repaint(); // --- // ---
    }

    public AdsReportFormDiagram getDiagram() {
        return diagram;
    }

    public AdsReportForm.Mode getDiagramMode() {
        return getDiagram().getMode();
    }

    protected abstract void update();

    protected void fireSelectionChanged(SelectionEvent event) {
        for (Container container = this.getParent(); container != null; container = container.getParent()) {
            if (container instanceof AdsReportFormDiagram) {
                final AdsReportFormDiagram formDiagram = (AdsReportFormDiagram) container;
                formDiagram.fireSelectionChanged(event);
            }
        }
    }
    private static final Stroke DOTTED_STROKE = new BasicStroke(1, 0, 0, 1, new float[]{2, 2}, 0);
    private static final Stroke HASHED_STROKE = new BasicStroke(1, 0, 0, 1, new float[]{7, 2}, 0);
    private static final Stroke SOLID_STROKE = new BasicStroke();
    private static final int BORDER_ALPHA = 197;

    public void paintBorder(final AdsReportAbstractAppearance.Border border, final Graphics g, final int width, final int height) {
        if (getDiagramMode() != AdsReportForm.Mode.GRAPHICS  || border == null || !border.isDisplayed()) {
            return;
        }
        
        final Graphics2D g2 = (Graphics2D) g;
        float topThickness = 0;
        float rightThickness = 0;
        float leftThickness = 0;

        if (border.isOnTop()) {
            Color c = border.getTopColor();
            g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), BORDER_ALPHA));
            topThickness = getRealBorderThickness(border.getTopThicknessMm());
            setStroke(g2, border.getTopStyle(), topThickness);
            g2.draw(new Line2D.Float(0, 0, width, 0));
        }

        if (border.isOnRight()) {
            Color c = border.getRightColor();
            g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), BORDER_ALPHA));
            rightThickness = getRealBorderThickness(border.getRightThicknessMm());
            setStroke(g2, border.getRightStyle(), rightThickness);
            g2.draw(new Line2D.Float(width - 1, topThickness/2 + rightThickness/2, width - 1, height));
        }

        if (border.isOnLeft()) {
            Color c = border.getLeftColor();
            g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), BORDER_ALPHA));
            leftThickness = getRealBorderThickness(border.getLeftThicknessMm());
            setStroke(g2, border.getLeftStyle(), leftThickness);
            g2.draw(new Line2D.Float(0, topThickness/2 + leftThickness/2, 0, height));
        }
        
        if (border.isOnBottom()) {
            Color c = border.getBottomColor();
            g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), BORDER_ALPHA));
            float thickness = getRealBorderThickness(border.getBottomThicknessMm());
            setStroke(g2, border.getBottomStyle(), thickness);
            g2.draw(new Line2D.Float(leftThickness/2 + thickness/2, height - 1, width - rightThickness/2 - thickness/2 - 1, height - 1));
        }

        g2.setStroke(SOLID_STROKE);       
    }  
    
    private void setStroke(final Graphics2D g2, EReportBorderStyle style, float thickness) {

        float dashThickness = thickness > 0 ? thickness: 1;
        switch (style) {
            case DASHED:
                g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{dashThickness, dashThickness / 2}, 0));
                break;
            case DOTTED:
                g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{dashThickness / 2, dashThickness / 2}, 0));
                break;
            default:
                g2.setStroke(new BasicStroke(thickness));
        }
    }
    
    private float getRealBorderThickness(double thickness){
        float result = MmUtils.mm2px(thickness);
        return result > 0 ? result * 2 : 1;
    }
    
}
