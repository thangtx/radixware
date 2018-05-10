package org.radixware.kernel.common.defs.ads.clazz.sql.report.html;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.enums.EReportTextFormat;

public class CellsUtils {

    public static void createLines(CellContents content, List<CellContents> line, AdjustedCell adjustedCell) {
        String text = content.getText();
        char[] characters = text.toCharArray();
        StringBuilder lineBuilder = new StringBuilder();
        for (char c : characters) {
            if (c == '\n') {
                CellContents prev = new CellContents(content);
                prev.setText(lineBuilder.toString());
                line.add(prev);
                adjustedCell.createParagraph(line);

                line.clear();
                lineBuilder.setLength(0);
                continue;
            }
            lineBuilder.append(c);
        }

        if (lineBuilder.length() != 0) {
            CellContents last = new CellContents(content);
            last.setText(lineBuilder.toString());
            line.add(last);
        }
    }

    public static void splitContent(CellParagraph adjustedCell, int[] insPos, int line) {
        int charIndex = insPos[0];
        int contentIndex = insPos[1];

        CellContents content = adjustedCell.getContentsByLine(line).get(contentIndex);
        splitContent(adjustedCell, content, charIndex, line);
        int newContentIndex = 0;
        for (int i = contentIndex + 1; i < adjustedCell.getContentsByLine(line).size(); i++) {
            CellContents c = adjustedCell.getContentsByLine(line).get(i);
            newContentIndex++;
            adjustedCell.moveToNexLine(c, line, newContentIndex);
            i--;
        }
    }

    public static void splitContent(CellParagraph adjustedCell, CellContents content, int charIndex, int line) {
        String text = content.getText();
        content.setText(new String(text.substring(0, charIndex)));

        CellContents secondContent = new CellContents(content);
        secondContent.setText(new String(text.substring(charIndex)));

        adjustedCell.insertCellContent(0, secondContent, line + 1);
    }

    public static boolean isNumber(String content, int charPos) {
        if ((charPos < content.length()) && ((content.charAt(charPos) == '0')
                || (content.charAt(charPos) == '1') || (content.charAt(charPos) == '2')
                || (content.charAt(charPos) == '3') || (content.charAt(charPos) == '4')
                || (content.charAt(charPos) == '5') || (content.charAt(charPos) == '6')
                || (content.charAt(charPos) == '7') || (content.charAt(charPos) == '8')
                || (content.charAt(charPos) == '9'))) {
            return true;
        }
        return false;
    }

    public static List<CellContents> getCellContant(final AdsReportCell cell) {
        String text = cell.getRunTimeContent();
        AdsReportAbstractAppearance.Font font = cell.getFont();
        List<CellContents> cellContants = getCellContant(cell, text, font);
        //String runTimeContent="";
        StringBuilder sb = new StringBuilder();
        for (CellContents cellContent : cellContants) {
            sb.append(cellContent.getText());
            //runTimeContent+=cellContent.getText();
        }
        cell.setRunTimeContent(sb.toString());
        return cellContants;
    }

    public static List<CellContents> getCellContant(final AdsReportCell cell, String text, AdsReportAbstractAppearance.Font font) {
        List<CellContents> cellContants = new ArrayList();
        if (text != null && !text.isEmpty()) {
            Color bgFont = cell.isBgColorInherited() ? null : cell.getBgColor();
            Color fgFont = cell.isFgColorInherited() ? null : cell.getFgColor();
            CellContents content = new CellContents(font, "", fgFont, bgFont);
            if (cell.getTextFormat() == EReportTextFormat.PLAIN) {
                CellContents cellContent = new CellContents(content);
                cellContent.setText(text);
                cellContants.add(cellContent);
            } else {
                cellContants.addAll(HtmlParser.parseHtmlText(text, new CellContents(content)));
            }
        }

        return cellContants;
    }

    public static void createNewParagraph(AdjustedCell adjustedCell, int[] insPos, CellParagraph paragraph, int line) {
        int charIndex = insPos[0];
        int contentIndex = insPos[1];

        CellContents content = paragraph.getContentsByLine(line).get(contentIndex);

        String text = content.getText();
        content.setText(new String(text.substring(0, charIndex)));

        CellContents secondContent = new CellContents(content);
        secondContent.setText(new String(text.substring(charIndex)));
        adjustedCell.createParagraph(secondContent);
    }
}
