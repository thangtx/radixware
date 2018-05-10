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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance.Border;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportDbImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroup;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportPropertyCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSpecialCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSummaryCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportTextCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.enums.EReportSummaryCellType;
import org.radixware.kernel.common.types.Id;

public class AdsReportCellWidget extends AdsReportSelectableWidget {

    private final AdsImageDef imageDef;

    public AdsReportCellWidget(final AdsReportFormDiagram diagram, final AdsReportWidget cell) {
        super(diagram, cell);

        if (cell instanceof AdsReportImageCell) {
            final AdsReportImageCell imageCell = (AdsReportImageCell) cell;
            imageDef = imageCell.findImage();
        } else {
            imageDef = null;
        }
    }
    /*public static final int EDGE_SIZE_PX = 5;
     private static final Color SNAP_COLOR = Color.RED.darker();
     private final AdsReportWidget reportWidget;
     private boolean selected = false;
     private final CellMouseListener mouseListener;
     private int row;
     private int column;
    
     public int getRow(){
     return row;
     }
    
     public int getColumn(){
     return column;
     }
    
     public void setRow(int row){
     this.row=row;
     }
    
     public void setColumn(int column){
     this.column=column;
     }

     public AdsReportCellWidget(AdsReportWidget cell) {
     super(cell);
     this.reportWidget = cell;
     setOpaque(true);
     mouseListener = new CellMouseListener(this);
     addMouseListener(mouseListener);
     addMouseMotionListener(mouseListener);
     }

     public boolean isSelected() {
     return selected;
     }

     public void setSelected(boolean selected) {
     if (this.selected != selected) {
     this.selected = selected;
     if (selected) {
     requestFocus();
     }
     repaint();
     fireSelectionChanged();
     }        
        
     final AdsReportBandSubWidget ownerBandSubWidget = (AdsReportBandSubWidget) getParent();
     if (ownerBandSubWidget != null) {
     ownerBandSubWidget.setComponentZOrder(this, 0);
     System.out.println(this.getColumn());
     }
     }

     @Override
     public void update() {
     final int leftPx = MmUtils.mm2px(reportWidget.getLeftMm());
     final int topPx = MmUtils.mm2px(reportWidget.getTopMm());
     setLocation(leftPx, topPx);

     final int widthPx = MmUtils.mm2px(reportWidget.getWidthMm());
     final int heightPx = MmUtils.mm2px(reportWidget.getHeightMm());
     setSize(widthPx, heightPx);
     }

     protected void paintBackground(Graphics g) {
     final int width = getWidth();
     final int height = getHeight();

     g.setColor(reportWidget.getBgColor());
     g.fillRect(0, 0, width, height);
     }

     private static void drawEdge(Graphics g, int x, int y, boolean snap) {
     g.setColor(snap ? SNAP_COLOR : Color.GRAY);
     g.drawRect(x, y, EDGE_SIZE_PX, EDGE_SIZE_PX);
     g.setColor(Color.WHITE);
     g.fillRect(x + 1, y + 1, EDGE_SIZE_PX - 1, EDGE_SIZE_PX - 1);
     }

     private void paintEdges(Graphics g) {
     final int width = getWidth() - 1;
     final int height = getHeight() - 1;

     if (selected) {
     g.setColor(Color.GRAY);
     g.drawRect(EDGE_SIZE_PX / 2, EDGE_SIZE_PX / 2, width - EDGE_SIZE_PX + 1, height - EDGE_SIZE_PX + 1);
     //g.drawRect(0, 0, width, height);

     // top
     drawEdge(g, (width - EDGE_SIZE_PX) / 2, 0, reportWidget.isSnapTopEdge());
     // right
     drawEdge(g, width - EDGE_SIZE_PX, (height - EDGE_SIZE_PX) / 2, false);
     // bottom
     drawEdge(g, (width - EDGE_SIZE_PX) / 2, height - EDGE_SIZE_PX,reportWidget.isSnapBottomEdge());
     // left
     drawEdge(g, 0, (height - EDGE_SIZE_PX) / 2, false);
     // top left
     drawEdge(g, 0, 0, reportWidget.isSnapTopEdge());
     // top right
     drawEdge(g, width - EDGE_SIZE_PX, 0, reportWidget.isSnapTopEdge());
     // bottom left
     drawEdge(g, 0, height - EDGE_SIZE_PX, reportWidget.isSnapBottomEdge());
     // bottom right
     drawEdge(g, width - EDGE_SIZE_PX, height - EDGE_SIZE_PX,reportWidget.isSnapBottomEdge());
     } else {
     g.setColor(reportWidget.isSnapTopEdge() ? SNAP_COLOR : Color.GRAY);

     // top left
     g.drawLine(0, 0, EDGE_SIZE_PX, 0);
     g.drawLine(0, 0, 0, EDGE_SIZE_PX);

     // top right
     g.drawLine(width - EDGE_SIZE_PX, 0, width, 0);
     g.drawLine(width, 0, width, EDGE_SIZE_PX);

     g.setColor(reportWidget.isSnapBottomEdge() ? SNAP_COLOR : Color.GRAY);

     // bottom left
     g.drawLine(0, height, EDGE_SIZE_PX, height);
     g.drawLine(0, height - EDGE_SIZE_PX, 0, height);

     // bottom right
     g.drawLine(width - EDGE_SIZE_PX, height, width, height);
     g.drawLine(width, height - EDGE_SIZE_PX, width, height);
     }
     }*/

    static String getCellDisplayName(final AdsReportCell cell, final EIsoLanguage lng) {
        switch (cell.getCellType()) {
            case EXPRESSION:
                return cell.getName();
            case IMAGE:
                final AdsReportImageCell imageCell = (AdsReportImageCell) cell;
                final AdsImageDef image = imageCell.findImage();
                final Id imageId = imageCell.getImageId();
                final String imageName = (image != null ? image.getName() : "#" + imageId);
                return "<" + imageName + ">";
            case DB_IMAGE:
                final AdsReportDbImageCell dbImageCell = (AdsReportDbImageCell) cell;
                final AdsPropertyDef dataProperty = dbImageCell.findDataProperty();
                final Id dataPropertyId = dbImageCell.getDataPropertyId();
                final String dataPropertyName = (dataProperty != null ? dataProperty.getName() : "#" + dataPropertyId);
                return "<" + dataPropertyName + ">";
            case PROPERTY:
                final AdsReportPropertyCell propertyCell = (AdsReportPropertyCell) cell;
                final AdsPropertyDef property = propertyCell.findProperty();
                final Id propertyId = propertyCell.getPropertyId();
                return "[" + (property != null ? property.getName() : "#" + propertyId) + "]";
            case SPECIAL:
                final AdsReportSpecialCell specialCell = (AdsReportSpecialCell) cell;
                switch (specialCell.getSpecialType()) {
                    case CURRENT_GROUP_RECORD_COUNT:
                    case TOTAL_RECORD_COUNT:
                        return "<COUNT>";
                    case GENERATION_TIME:
                        return "<TIME>";
                    case PAGE_NUMBER:
                        return "<PAGE>";
                    case FILE_PAGE_COUNT:
                        return "<FILE PAGE COUNT>";
                    case TOTAL_PAGE_COUNT:
                        return "<TOTAL PAGE COUNT>";
                    case FILE_COUNT:
                        return "<FILE COUNT>";
                    case FILE_NUMBER:
                        return "<FILE NUMBER>";
                    case SUB_ITEM_COUNT:
                        return "<INDEX>";
                    case TOTAL_PAGE_NUMBER:
                        return "<OVERALL FILE NUMBER>";

                    default:
                        throw new IllegalStateException("Unsupportet special cell type: " + specialCell.getSpecialType());
                }
            case SUMMARY:
                final AdsReportSummaryCell summaryCell = (AdsReportSummaryCell) cell;
                final AdsPropertyDef prop = summaryCell.findProperty();
                final String propName = (prop != null ? prop.getName() : "#" + summaryCell.getPropertyId());
                final EReportSummaryCellType summaryType = summaryCell.getSummaryType();
                final String prefix = (summaryType == EReportSummaryCellType.SUM ? "Σ" : summaryType.getValue());
                final AdsReportGroup group = summaryCell.findSummaryGroup();
                final int groupCount = summaryCell.getGroupCount();
                final String postfix = (group != null ? ", " + group.getName() : (groupCount > 0 ? ", " + groupCount : ""));
                return prefix + "(" + propName + postfix + ")";
            case TEXT:
                final AdsReportTextCell textCell = (AdsReportTextCell) cell;
                if (textCell.getTextId() != null) {
                    final AdsMultilingualStringDef mls = textCell.findText();
                    if (mls == null) {
                        return "#" + textCell.getTextId();
                    } else {
                        final String value = mls.getValue(lng);
                        if (value != null && !value.isEmpty()) {
                            return mls.getValue(lng);
                        }
                    }
                }
                return "<Empty>";
            case CHART:
                final AdsReportChartCell chartCell = (AdsReportChartCell) cell;
                final String str3D = chartCell.getChartSeries() != null
                        && !chartCell.getChartSeries().isEmpty()
                        && chartCell.getChartSeries().is3D() ? "3D" : "";
                return "<" + str3D + chartCell.getChartType().getValue() + "Chart>";
            default:
                throw new IllegalStateException("Unsupportet cell type: " + cell.getCellType());
        }
    }
    
    private HtmlTextPanel htmlTextPanel;

    protected void paintName(final Graphics g) {
        if (!(reportWidget instanceof AdsReportCell)) {
            return;
        }

        final AdsReportCell cell = (AdsReportCell) reportWidget;
        if (imageDef != null) {
            final int width = getWidth() - 1;
            final int height = getHeight() - 1;
            final Image image = imageDef.getIcon().getImage(width, height);
            g.drawImage(image, 0, 0, null);
            return;
        }

        final AdsReportAbstractAppearance.Font cellFont = cell.getFont();
        final Font font = getDiagramMode() == AdsReportForm.Mode.GRAPHICS ? AdsReportWidgetUtils.reportFont2JavaFont(cellFont, this) : TxtUtils.getFont();
        g.setFont(font);

        final EIsoLanguage lng = getOwnerBandWidget().getOwnerFormDiagram().getLanguage();
        final String name;
        if (cell instanceof AdsReportImageCell) {
            //npopov: imageDef is null, we don't need to call getCellDisplayName() 
            //and search for imageDef to calculate name. Searching for definition in this paint
            //method causes infinite recursion in processing request for loading module images.
            //So I moved searching for imageDef to constructor
            final String imageName = "#" + ((AdsReportImageCell) cell).getImageId();
            name = "<" + imageName + ">";
        } else {
            name = getCellDisplayName(cell, lng);
        }

        g.setColor(cell.getFgColor());
        Point targetLocation;
        Border border = cell.getBorder();
        boolean borderInsetsExist = border != null && border.isDisplayed();
        
        int top = borderInsetsExist && border.isOnTop() ? MmUtils.mm2px(border.getTopThicknessMm()) : 0;
        int left = borderInsetsExist && border.isOnLeft() ? MmUtils.mm2px(border.getLeftThicknessMm()) : 0;
        int bottom = borderInsetsExist && border.isOnBottom() ? MmUtils.mm2px(border.getBottomThicknessMm()) : 0;
        int right = borderInsetsExist && border.isOnRight() ? MmUtils.mm2px(border.getRightThicknessMm()) : 0;
        
        final Insets insets = new Insets(top, left, bottom, right);
        if (getDiagramMode() == AdsReportForm.Mode.GRAPHICS && cell.getCellType() == EReportCellType.TEXT) {
            if (htmlTextPanel == null){
                htmlTextPanel = new HtmlTextPanel(cell);
            }
            htmlTextPanel.setText(name);
            int width = getWidth()- insets.left - insets.right;  
            int height = getHeight()- insets.top;
            htmlTextPanel.setSize(new Dimension(width, height));
            SwingUtilities.paintComponent(g, htmlTextPanel, this, insets.left, insets.top, width, height);
        } else {
            final FontMetrics fontMetrics = getFontMetrics(font);
            final int textWidth = fontMetrics.stringWidth(name);
            targetLocation = getFirstPosition(cell, textWidth, fontMetrics.getAscent(), fontMetrics.getDescent(), insets);
            g.drawString(name, targetLocation.x, targetLocation.y);
        }
    }

    private Point getFirstPosition(AdsReportCell cell, int textWidth, int ascent, int descent, Insets insets) {
        final int left;

        final int baseLine;
        if (getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
            switch (cell.getHAlign()) {
                case CENTER:
                    left = (MmUtils.mm2px(cell.getWidthMm() - cell.getMarginLeftMm() - cell.getMarginRightMm()/*reportWidget.getMarginMm() * 2*/) - textWidth) / 2;
                    break;
                case RIGHT:
                    left = MmUtils.mm2px(cell.getWidthMm() - cell.getMarginRightMm()) - textWidth - insets.right;
                    break;
                default:
                    left = MmUtils.mm2px(cell.getMarginLeftMm()) + insets.left;
                    break;
            }
            switch (cell.getVAlign()) {
                case MIDDLE:
                    baseLine = (MmUtils.mm2px(cell.getHeightMm()) - insets.top - insets.bottom) / 2
                            + (ascent + descent) / 2 - descent;
                    break;
                case BOTTOM:
                    baseLine = MmUtils.mm2px(cell.getHeightMm() - cell.getMarginBottomMm()) - descent - insets.bottom;
                    break;
                default:
                    baseLine = MmUtils.mm2px(cell.getMarginTopMm()) + insets.top + ascent;
                    break;

            }
        } else {
            switch (cell.getHAlign()) {
                case CENTER:
                    left = (TxtUtils.columns2Px(cell.getWidthCols() - cell.getMarginLeftCols() - cell.getMarginRightCols()/*reportWidget.getMarginMm() * 2*/) - textWidth) / 2;
                    break;
                case RIGHT:
                    left = TxtUtils.columns2Px(cell.getWidthCols() - cell.getMarginRightCols()) - textWidth;
                    break;
                default:
                    left = TxtUtils.columns2Px(cell.getMarginLeftCols());
                    break;
            }

            switch (cell.getVAlign()) {
                case MIDDLE:
                    baseLine = TxtUtils.rows2Px(cell.getHeightRows()) / 2
                            + (ascent + descent) / 2 - descent;
                    break;
                case BOTTOM:
                    baseLine = TxtUtils.rows2Px(cell.getHeightRows() - cell.getMarginBottomRows()) - descent;
                    break;
                default:
                    baseLine = TxtUtils.rows2Px(cell.getMarginTopRows()) + ascent;
                    break;

            }

        }
        return new Point(left, baseLine);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        paintBackground(g);
        paintName(g);
        paintEdges(g);
        paintBorder(reportWidget.getBorder(), g, getWidth(), getHeight());
    }

    @Override
    public boolean isVisible() {
        if (((AdsReportCell) reportWidget).isDiagramModeSupported(getDiagramMode())) {
            return super.isVisible();
        } else {
            return false;
        }
    }

    @Override
    protected void paintBackground(final Graphics g) {
        if (reportWidget != null) {
            
            final int width = getWidth();
            final int height = getHeight();

            g.setColor(reportWidget.getBgColor());
            g.fillRect(0, 0, width, height);
        }
    }
    
    

    /*public AdsReportBandWidget getOwnerBandWidget() {
     final AdsReportBandSubWidget ownerBandSubWidget = (AdsReportBandSubWidget) getParent();
     if (ownerBandSubWidget != null) {
     return ownerBandSubWidget.getOwnerBandWidget();
     } else {
     return null;
     }
     }

     public AdsReportWidget getCell() {
     return reportWidget;
     }
     }
     }*/
}
