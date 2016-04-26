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

package org.radixware.kernel.common.sqlscript.parser;


public class SQLPosition {
    private int    index;
    private int    line;
    private int    column;
    private int    sourceLine;
    private String srcName;

    public SQLPosition() {
        index = 0;
        line = -1;
        column = -1;
        sourceLine = -1;
    }

    public SQLPosition(int pIndex, int pLine, int pColumn, int pSourceLine, String pSrcName) {
        index= pIndex;
        line = pLine;
        column = pColumn;
        sourceLine = pSourceLine;
        srcName = pSrcName;
    }

    public SQLPosition(SQLPosition pos) {
        index = pos.index;
        line = pos.line;
        column = pos.column;
        sourceLine = pos.sourceLine;
        srcName = pos.srcName;        
    }    
    
    
    public void setColumn(int column) {
        this.column = column;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setSourceLine(int sourceLine) {
        this.sourceLine = sourceLine;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public int getColumn() {
        return column;
    }

    public int getIndex() {
        return index;
    }

    public int getLine() {
        return line;
    }

    public int getSourceLine() {
        return sourceLine;
    }

    public String getSrcName() {
        return srcName;
    }

    public boolean isEmpty() {
        return line == -1 || column == -1;
    }

    @Override
    public String toString() {
        if (srcName!=null && !srcName.isEmpty())
            return  "source "   + srcName +
                    ", line "   + Integer.toString(line) +
                    ", column " + Integer.toString(column);
        else
            return toStringLC();
    }

    public String toStringLC() {
        return  "line "  + Integer.toString(line) +
                ", column " + Integer.toString(column);
    }

    public SQLPosition fork() {
        return new SQLPosition(index, line, column, sourceLine, srcName);
    }
}
