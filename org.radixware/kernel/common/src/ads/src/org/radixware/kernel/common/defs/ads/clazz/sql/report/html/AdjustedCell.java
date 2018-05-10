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
package org.radixware.kernel.common.defs.ads.clazz.sql.report.html;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
 
public class AdjustedCell implements Iterable<CellParagraph> {

    //private final AdsReportCell cell;

    private List<CellParagraph> cellLines;
    private boolean wasWrapped = false;
    private boolean wasSeparated = false;

    public AdjustedCell() {
        cellLines = new LinkedList<>();
    }

    public CellParagraph getParagraph(int index) {
        return cellLines.get(index);
    }
    
    public CellParagraph createParagraph(CellContents content){
        CellParagraph cellParagraph = new CellParagraph();
        cellParagraph.addLine(content);
        cellLines.add(cellParagraph);
        return cellParagraph;
    }
    
    public CellParagraph createParagraph(List<CellContents> content){
        CellParagraph cellParagraph = new CellParagraph();
        cellParagraph.addLine(content);
        cellLines.add(cellParagraph);
        return cellParagraph;
    }

    public int getParagraphsCount() {
        return cellLines.size();
    }
    
    public boolean wasWrapped() {
        return wasWrapped;
    }

    public void setWasWrapped(boolean wasWrapped) {
        this.wasWrapped = wasWrapped;
    }

    public boolean wasSeparated() {
        return wasSeparated;
    }

    public void setWasSeparated(boolean wasSeparated) {
        this.wasSeparated = wasSeparated;
    }

    @Override
    public Iterator<CellParagraph> iterator() {
        return cellLines.iterator();
    }
    
    public List<CellContents> getContentByLineInCell(int line){
        int currentLine = line;
        for (CellParagraph p : cellLines){
            int lines = p.getLinesCount();
            if (currentLine >=  lines) {
                currentLine -= lines;
            } else {
                return p.getContentsByLine(currentLine);
            }
        }
        return null;
    }
    
    public int getLinesCount() {
        int lines = 0;
        for (CellParagraph p : cellLines) {
            lines += p.getLinesCount();
        }
        return lines;
    }
    
    public double getLineHeight(CellParagraph cellParagraph, int line, double lineSpasing){
        double lineHeigh = 0;
        lineHeigh += cellParagraph.getLineContentHeight(cellLines.indexOf(cellParagraph) == 0, 
                cellLines.lastIndexOf(cellParagraph) == (getParagraphsCount() - 1), line, lineSpasing);
        return lineHeigh;
    }
    
    public double getContentHeight(double lineSpasing){
        double lineHeigh = 0;
        for (int p = 0; p < getParagraphsCount(); p++){
            CellParagraph cellParagraph = cellLines.get(p);
            lineHeigh += cellParagraph.getContentHeight(p == 0, p == (getParagraphsCount() - 1), lineSpasing);
        }
        return lineHeigh;
    }
    
    public void clear(){
        cellLines.clear();
    }
    
    public void removeFirst() {
        cellLines.remove(0);
    }
}
