package org.radixware.kernel.common.defs.ads.clazz.sql.report.html;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CellParagraph implements Iterable<List<CellContents>>{
    private final List<List<CellContents>> cellLines;

    public CellParagraph() {
        cellLines =  new LinkedList<>();
    }
    
    public void addLine(List<CellContents> contents) {
        cellLines.add(new LinkedList<>(contents));
    }
    
    public void addLine(CellContents contents) {
        LinkedList list = new LinkedList<>();
        list.add(contents);
        addLine(list);
    }
    
    public List<CellContents> getContentsByLine(int line) {
        return cellLines.get(line);
    }
    
    public void insertCellContent(int index, CellContents content, int line) {
        if (line == cellLines.size()) {
            List<CellContents> lineContents = new LinkedList<>();
            cellLines.add(lineContents);
        }
        List<CellContents> lineContents = cellLines.get(line);
        lineContents.add(index, content);
    }
    
    public void moveToNexLine(CellContents content, int line, int index) {
        List<CellContents> lineContents = cellLines.get(line);
        lineContents.remove(content);
        line++;
        if (line == cellLines.size()) {
            List<CellContents> newLineContents = new LinkedList<>();
            cellLines.add(newLineContents);
        }
        List<CellContents> newLineContents = cellLines.get(line);
        newLineContents.add(index, content);
    }
    
    public double getLineFontSize(int line) {
        List<CellContents> lineContents = cellLines.get(line);
        double maxFontSize = 0;
        for (CellContents contants : lineContents) {
            double fontSize = contants.getFont().getSizeMm();
            maxFontSize = Math.max(fontSize, maxFontSize);
        }
        return maxFontSize;
    }
    
     public int getLinesCount() {
        return cellLines.size();
    }

    @Override
    public Iterator<List<CellContents>> iterator() {
        return cellLines.iterator();
    }
    
    public double getContentHeight(boolean isFirst, boolean isLast, double lineSpasing){
        double lineHeigh = 0;
        for (int l = 0; l < getLinesCount(); l++) {
            lineHeigh += getLineContentHeight(isFirst, isLast, l, lineSpasing);
        }
        return lineHeigh;
    }
    
    public double getLineContentHeight(boolean isFirst, boolean isLast, int line, double lineSpasing){
        double realLineSpacing = lineSpasing;
        if (isFirst && line == 0) {
            realLineSpacing -= lineSpasing/2;
        }
        if (isLast && isLastLineInParagraph(line)) {
            realLineSpacing -= lineSpasing/2;
        }
        return getLineFontSize(line) + realLineSpacing;
    }
    
    public boolean isLastLineInParagraph(int line){
        return line == getLinesCount() - 1;
    }
    
    public void removeFirst() {
        cellLines.remove(0);
    }
}
