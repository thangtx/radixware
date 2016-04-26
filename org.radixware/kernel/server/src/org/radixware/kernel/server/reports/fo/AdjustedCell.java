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
package org.radixware.kernel.server.reports.fo;

import java.util.LinkedList;
import java.util.List;
 
public class AdjustedCell {

    //private final AdsReportCell cell;

    private List<List<CellContents>> cellLines;
    private boolean wasWrapped = false;
    private boolean wasSeparated = false;

    public AdjustedCell() {
        cellLines = new LinkedList<>();
    }

    public List<CellContents> getContentsByLine(int line) {
        return cellLines.get(line);
    }

    void addCellContent(CellContents content, int line) {
        if (line == cellLines.size()) {
            List<CellContents> lineContents = new LinkedList<>();
            cellLines.add(lineContents);
        }
        List<CellContents> lineContents = cellLines.get(line);
        lineContents.add(content);
    }

    public void insertCellContent(int index, CellContents content, int line) {
        if (line == cellLines.size()) {
            List<CellContents> lineContents = new LinkedList<>();
            cellLines.add(lineContents);
        }
        List<CellContents> lineContents = cellLines.get(line);
        lineContents.add(index, content);
    }

    /*void changeLine(CellContents content,int line,int newLine){
     List<CellContents> lineContents=cellLines.get(line);
     lineContents.remove(content);
     if(newLine==cellLines.size()){
     List<CellContents> newLineContents=new LinkedList<>();
     cellLines.add(newLineContents);
     }
     List<CellContents> newLineContents=cellLines.get(newLine);
     newLineContents.add(0,content);
     }*/
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

    void insertLine(int line) {
        List<CellContents> lineContents = new LinkedList<>();
        cellLines.add(line, lineContents);
    }

    void addLine(List<CellContents> lineContents) {
        //List<CellContents> lineContents=new LinkedList<>();
        cellLines.add(lineContents);
    }

    public int getLineCount() {
        return cellLines.size();
    }

    public List<List<CellContents>> getLineCellContents() {
        return cellLines;
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

    boolean wasWrapped() {
        return wasWrapped;
    }

    void setWasWrapped(boolean wasWrapped) {
        this.wasWrapped = wasWrapped;
    }

    boolean wasSeparated() {
        return wasSeparated;
    }

    void setWasSeparated(boolean wasSeparated) {
        this.wasSeparated = wasSeparated;
    }
}
