package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.html.CellContents;

public class LineFontsMetric {
    HashMap<CellContents,ContentMetric> contents = new HashMap<>();
    private int leading = 0;
    private int descent = 0;
    private int ascent = 0;
    private int spaceWidth = 0;
    private boolean isJustify = false;
    
    private Point startPoint;
    private int width = 0;
    
    public void addContent(CellContents contens, ContentMetric content){
        contents.put(contens, content);
    }
    
    public ContentMetric getContent(CellContents contens){
        return contents.get(contens);
    }
    
    public Collection<ContentMetric> getCellContens(){
        return contents.values();
    }
    
    public int getContentSize(){
        return contents.size();
    }
    
    public int getLeading() {
        return leading;
    }

    public void setLeading(int leading) {
        this.leading = Math.max(this.leading, leading);
    }

    public int getDescent() {
        return descent;
    }

    public void setDescent(int descent) {
        this.descent = Math.max(this.descent, descent);
    }

    public int getAscent() {
        return ascent;
    }

    public void setAscent(int ascent) {
        this.ascent = Math.max(this.ascent, ascent);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return ascent + leading + descent;
    }


    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }
    
    public int getSpaceCount() {
        int spaceCount = 0;
        for (CellContents content : contents.keySet()) {
            ContentMetric contentMetric = getContent(content);
            spaceCount += contentMetric.getSpaceCount() ;
        }
        return spaceCount;
    }
    
        
    public int getSpaceWidth() {
        return spaceWidth;
    }

    public void setSpaceWidth(int spaceWidth) {
        this.spaceWidth = spaceWidth;
    }

    public boolean isJustify() {
        return isJustify;
    }

    public void setJustify(boolean isJustify) {
        this.isJustify = isJustify;
    }
}
