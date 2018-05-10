
package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.CellContents;

public class ContentMetric {
    CellContents content;
    Point startPoint = new Point(-1,-1);
    Font font;
    private int width = 0;
    private int height = 0;
    private int ascent = 0;
    String[] splitText;

    public ContentMetric(CellContents content, Font defaultFont) {
        this.font = defaultFont;
        this.content = content;
    }
    
    public CellContents getContent() {
        return content;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        if (font != null) {
            this.font = font;
        }
    }

    public String getText() {
        return content.getText();
    }

    public Color getFgColor(){
        return content.getFgColor();
    }
    
    public Color getBgColor(){
        return content.getBgColor();
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint.x = startPoint.x;
        this.startPoint.y = startPoint.y;
    }

    public int getStartX() {
        return startPoint.x;
    }
    
    public int getStartY() {
        return startPoint.y;
    }
    
    
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpaceCount() {
        calcSplitText();
        return splitText.length > 0 ? splitText.length - 1 : 0;
    }

    public String[] getSplitText() {
        return splitText;
    }
    
    public void calcSplitText(){
        if (splitText == null) {
            String s = getText();
            if (s != null && !s.isEmpty()) {
                splitText = s.split(" ", -1);
            } else {
                splitText = new String[0];
            }
        }
    }

    public int getAscent() {
        return ascent;
    }

    public void setAscent(int ascent) {
        this.ascent = ascent;
    }

}
