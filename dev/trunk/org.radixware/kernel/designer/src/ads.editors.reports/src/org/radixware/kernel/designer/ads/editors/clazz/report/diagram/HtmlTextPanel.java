package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.font.LineMetrics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.AdjustedCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.CellContents;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.CellParagraph;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.CellsUtils;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.HtmlParser;
import org.radixware.kernel.common.enums.EReportCellHAlign;

public class HtmlTextPanel extends JPanel {

    private final AdsReportCell cell;
    private String text;
    private TextMetric textMetric;

    public HtmlTextPanel(AdsReportCell cell) {
        this.cell = cell;
    }

    @Override
    protected void paintComponent(Graphics g) {
        init();
        int y = getVerticalAliganPosition();
        for (int l = 0; l < textMetric.getLineCount(); l++) {
            LineFontsMetric lineMetric = textMetric.getLineFontsMetric(l);
            int x = getHorizontalAliganPosition(lineMetric);
            for (ContentMetric content : lineMetric.getCellContens()) {
                if (content.getStartX() >= 0 && content.getStartY() >= 0) {
                    Font currentFont = content.getFont();
                    if (content.getBgColor() != null){
                        g.setColor(content.getBgColor());
                        g.fillRect(content.getStartX() + x,
                                (content.getStartY() + y) - content.getAscent(),
                                (int) content.getWidth(),
                                (int) content.getHeight());
                    }
                    g.setFont(currentFont);
                    g.setColor(content.getFgColor() == null ? cell.getFgColor() : content.getFgColor());
                    if (lineMetric.isJustify()) {
                        String[] splitText = content.getSplitText();
                        Point p = new Point(content.getStartX() + x, content.getStartY() + y);
                        FontMetrics fontMetrics = getFontMetrics(content.getFont());
                        for (String s : splitText) {
                            g.drawString(s, p.x, p.y);
                            p.x += lineMetric.getSpaceWidth() + fontMetrics.stringWidth(s);
                        }
                    } else {
                        String text = content.getText();
                        g.drawString(text == null ? "" : text, content.getStartX() + x, content.getStartY() + y);
                    }
                }
            }
        }
    }

    private void createFonts(List<CellContents> list, LineFontsMetric metric, Font defaultFont) {
        for (CellContents cont : list) {
            ContentMetric contentMetric = new ContentMetric(cont, defaultFont);
            contentMetric.setFont(AdsReportWidgetUtils.reportFont2JavaFont(cont.getFont(), this));
            FontMetrics fontMetrics = getFontMetrics(contentMetric.getFont());
            metric.setLeading(fontMetrics.getLeading());
            metric.setDescent(fontMetrics.getDescent());
            metric.setAscent(fontMetrics.getAscent());
            metric.addContent(cont, contentMetric);
        }
    }

    private void wrapWords(AdjustedCell adjustedCell, int maxWidthCol) {
        for (int p = 0; p < adjustedCell.getParagraphsCount(); p++) {
            CellParagraph cellParagraph = adjustedCell.getParagraph(p);
            for (int l = 0; l < cellParagraph.getLinesCount(); l++) {
                List<CellContents> list = cellParagraph.getContentsByLine(l);
                int lineWidthCol = 0, wordWidthCol = 0;
                int insPos[] = new int[]{0, 0};
                for (int j = 0; j < list.size(); j++) {
                    CellContents content = list.get(j);
                    Font font = AdsReportWidgetUtils.reportFont2JavaFont(content.getFont(), this);
                    FontMetrics fontMetrics = getFontMetrics(font);

                    String text = content.getText();
                    boolean inQuotes = false;
                    if (text != null) {
                        for (int i = 0; i < text.length(); i++) {
                            final char c = text.charAt(i);
                            if (c == '\n') {
                                i++;
                                insPos[0] = i;
                                insPos[1] = j;
                                CellsUtils.splitContent(cellParagraph, insPos, l);
                                lineWidthCol = 0;
                                wordWidthCol = 0;
                                insPos[0] = 0;
                                break;
                            } else {
                                int charWidthCol = fontMetrics.charWidth(c);
                                if (lineWidthCol > 0 && lineWidthCol + charWidthCol > maxWidthCol && c != ' ') {
                                    if (insPos[0] == 0) {
                                        insPos[0] = i;
                                        insPos[1] = j;
                                        wordWidthCol = 0;
                                    }
                                    CellsUtils.splitContent(cellParagraph, insPos, l);
                                    i++;
                                    insPos[0] = 0;
                                    lineWidthCol = wordWidthCol;
                                    break;
                                }

                                lineWidthCol += charWidthCol;

                                if (c == '(' || c == '{' || c == '[' || c == '$' || (c == '"' && !inQuotes)) {
                                    wordWidthCol = charWidthCol;
                                    insPos[0] = i;
                                    insPos[1] = j;
                                } else if (c == ' ' || c == ';' /*|| c == ',' || c == ':' || c == '.'*/ || c == ')' || c == ']' || c == '}' || c == '!' || c == '?' || c == '%' || (c == '"' && inQuotes) || c == '-') {
                                    wordWidthCol = 0;
                                    insPos[0] = i + 1;
                                    insPos[1] = j;
                                } else if ((c == ',' || c == ':' || c == '.') && (!CellsUtils.isNumber(text, i + 1))) {
                                    wordWidthCol = 0;
                                    insPos[0] = i + 1;
                                    insPos[1] = j;
                                } else {
                                    wordWidthCol += charWidthCol;
                                }
                                if (c == '"') {
                                    inQuotes = !inQuotes;
                                }

                            }
                        }

                    }
                }
            }
        }
    }

    private void init() {
        final AdsReportAbstractAppearance.Font cellFont = cell.getFont().copy();
        cellFont.setSilentModified(true);
        Font font = AdsReportWidgetUtils.reportFont2JavaFont(cellFont, this);

        AdjustedCell adjustedCell = new AdjustedCell();
        List<CellContents> cellContant = CellsUtils.getCellContant(cell, text, cellFont);
        List<CellContents> line = new ArrayList<>();
        for (CellContents content : cellContant) {
            CellsUtils.createLines(content, line, adjustedCell);
        }
        if (!line.isEmpty()) {
            adjustedCell.createParagraph(line);
        }
        int cellWidth = getWidth();
        if (cell.isWrapWord()) {
            wrapWords(adjustedCell, cellWidth);
        }

        Point targetLocation = new Point(0, 0);
        Point startPoint = new Point(targetLocation);

        TextMetric textMetric = new TextMetric(this);
        for (CellParagraph cellParagraph : adjustedCell){
            for (int l = 0; l < cellParagraph.getLinesCount(); l++) {
                List<CellContents> list = cellParagraph.getContentsByLine(l);
                LineFontsMetric metric = new LineFontsMetric();
                createFonts(list, metric, font);
                metric.setJustify(cell.getHAlign() == EReportCellHAlign.JUSTIFY && !cellParagraph.isLastLineInParagraph(l));


                targetLocation.y += metric.getAscent();

                metric.setStartPoint(new Point(startPoint.x, targetLocation.y));

                int lineWidth = 0;
                for (CellContents content : list) {
                    ContentMetric contentMetric = metric.getContent(content);
                    String text = contentMetric.getText();
                    FontMetrics fontMetrics = getFontMetrics(contentMetric.getFont());
                    int textWidth;
                    if (metric.isJustify()) {
                        //textWidth = fontMetrics.stringWidth(text.replaceAll(" ", ""));
                        textWidth = 0;
                        contentMetric.calcSplitText();
                        for (String s : contentMetric.getSplitText()) {
                            textWidth += fontMetrics.stringWidth(s);
                        }
                    } else {
                        textWidth = fontMetrics.stringWidth(text);
                    }
                    contentMetric.setWidth(textWidth);
                    contentMetric.setHeight(fontMetrics.getHeight());
                    contentMetric.setAscent(fontMetrics.getAscent());
                    lineWidth += textWidth;
                }

                if (metric.isJustify()) {
                    int scpaceCount = metric.getSpaceCount();
                    FontMetrics fontMetrics = getFontMetrics(font);
                    metric.setSpaceWidth( Math.max((cellWidth - lineWidth - 2) / (scpaceCount > 0 ? scpaceCount : 1), fontMetrics.charWidth(' ')) );
                }

                for (CellContents content : list) {
                    ContentMetric contentMetric = metric.getContent(content);
                    int textWidth = contentMetric.getWidth();

                    contentMetric.setStartPoint(targetLocation);
                    if (metric.isJustify()) {
                        int currentWidth = textWidth + metric.getSpaceWidth() * contentMetric.getSpaceCount();
                        targetLocation.x += currentWidth;
                        contentMetric.setWidth(currentWidth);
                    } else {
                        targetLocation.x += textWidth;
                    }

                }
                targetLocation.y += metric.getLeading() + metric.getDescent();
                metric.setWidth(targetLocation.x - startPoint.x);
                targetLocation.x = startPoint.x;

                textMetric.addFontMetric(metric);
            }
        }
        this.textMetric = textMetric;

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text != null && !text.equals(this.text)) {
            this.text = text;
            init();
        }
    }
    
    private int getHorizontalAliganPosition(LineFontsMetric lineMetrics) {
        if (lineMetrics == null) {
            return 0;
        }
        
        final int width = MmUtils.mm2px(cell.getWidthMm() - cell.getMarginLeftMm() - cell.getMarginRightCols());

        int x;
        final int xmin = MmUtils.mm2px(cell.getMarginLeftMm());


        switch (cell.getHAlign()) {
            case CENTER:
                x = (width - lineMetrics.getWidth()) / 2 + xmin;
                break;
            case RIGHT:
                x = MmUtils.mm2px(cell.getWidthMm() - cell.getMarginRightMm()) - lineMetrics.getWidth();
                break;
            default:
                x = xmin;
                break;
        }
       
        return x;
    }
    
    private int getVerticalAliganPosition(){
        if (textMetric == null) {
            return 0;
        }
        
        final int ytop = MmUtils.mm2px(cell.getMarginTopMm());
        int y = 0;
        switch (cell.getVAlign()) {
            case MIDDLE:
                y = MmUtils.mm2px(cell.getHeightMm()) / 2 - (textMetric.height) / 2;
                break;
            case BOTTOM:
                y = MmUtils.mm2px(cell.getHeightMm() - cell.getMarginBottomMm()) - textMetric.height;
                break;
            default:
                y = ytop;
                break;

        }
        return Math.max(ytop, y);
    }

}
