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

package org.radixware.kernel.explorer.editors.monitoring.diagram;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.Orientations;
import com.trolltech.qt.gui.QLayoutItem;
import org.radixware.schemas.monitoringSettings.MonitoringDiagramPosition;


public class GridLayoutItem extends QLayoutItem{
    private final DiagramPanel diagram;
    private int row=-1;
    private int column=-1;
    private int rowSpan=2;
    private int columnSpan=2;
    
    
    GridLayoutItem(final DiagramPanel diagram){
        this.diagram=diagram;
    }
    
    /*public void setPositions(MonitoringDiagramPosition xPosition){
        if(xPosition!=null){
            if(xPosition.isSetColumn())
                setColumn((int)xPosition.getColumn());
            if(xPosition.isSetRow())
                setRow((int)xPosition.getRow());
            if(xPosition.isSetColumnSpan())
                setColumnSpan((int)xPosition.getColumnSpan());
            if(xPosition.isSetRowSpan())
                setRowSpan((int)xPosition.getRowSpan());
        }
    }*/
    
    public void setPositions(final int[] pos){
        if(pos!=null){
            column=pos[0];
            row=pos[1];
            columnSpan= pos[2]==0 ? 1 : pos[2];
            rowSpan= pos[3]==0 ? 1 : pos[3];
        }
    }
    
    public void saveToXml(final MonitoringDiagramPosition position){
        position.setColumn(column);
        position.setRow(row);
        position.setColumnSpan(columnSpan);
        position.setRowSpan(rowSpan);
    }
    
    public int getRow(){
        return row;
    }
    
    public void setRow(final int row){
        this.row=row;
    }
    
    public int getColumn(){
        return column;
    }
    
    public void setColumn(final int column){
        this.column=column;
    }
    
    public int getRowSpan(){
        return rowSpan;
    }
    
    public void setRowSpan(final int rowSpan){
        this.rowSpan=rowSpan;
    }
    
    public int getColumnSpan(){
        return columnSpan;
    }
    
    public void setColumnSpan(final int columnSpan){
        this.columnSpan=columnSpan;
    }
    
    public DiagramPanel getDiagram(){
        return diagram;
    }
    
    @Override
    public Orientations expandingDirections() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public QRect geometry() {
        return diagram.geometry();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public QSize maximumSize() {
       return diagram.maximumSize();
    }

    @Override
    public QSize minimumSize() {
         return diagram.minimumSize();
    }

    @Override
    public void setGeometry(final QRect qrect) {
       diagram.setGeometry(qrect);
    }

    @Override
    public QSize sizeHint() {
        return diagram.sizeHint();
    }
    
}
