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
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QLayoutItemInterface;
import com.trolltech.qt.gui.QWidget;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


public class GridLayout extends QLayout{
    public static final int gridSize=16;
    private QWidget parent;
    private List<GridLayoutItem> diagrams;

    public GridLayout(QWidget parent){
        this.parent=parent;
        diagrams=new ArrayList<>();
    }
    
    @Override
    public void setGeometry(QRect qrect) {
        super.setGeometry(qrect);
        adjust();
    }
    
    public void adjust(){
         for (GridLayoutItem diagram: diagrams) {
             if(!diagram.getDiagram().isDragging()){
                int dX=parent.width()/16;
                int dY=parent.height()/16;           
                int x=dX*diagram.getColumn();
                int y=dY*diagram.getRow();
                int width= diagram.getColumnSpan()* dX;
                int height= diagram.getRowSpan()*dY;
                diagram.getDiagram().setGeometry(new QRect(x, y, width, height)); 
             }
        }
    }
    
    public int[] getFreeSpace(){           
        for(int row=0;row<gridSize;row++){              
            for(int column=0;column<gridSize;column++){
                GridLayoutItem it=getItem(column, row);
                if(it==null){
                    return new int[]{column,row };
                }
            } 
        }
        return null;
    }

    @Override
    public void addItem(QLayoutItemInterface qlii) {
        if(qlii!=null && qlii instanceof GridLayoutItem){
            GridLayoutItem item=(GridLayoutItem)qlii;
            int column=item.getColumn(),row=item.getRow();
            boolean isFindEmptyPlase=true;
            if(column==-1  || row==-1 ){             
                isFindEmptyPlase=false;
                for(row=0;row<gridSize;row++){              
                    for(column=0;column<gridSize;column++){
                        GridLayoutItem it=getItem(column, row);
                        if(it==null){
                            isFindEmptyPlase=true;
                            break;
                        }
                    } 
                    if(isFindEmptyPlase){
                        break;
                    }
                }
            }
            if(isFindEmptyPlase){
                item.setColumn(column);
                item.setRow(row);
                
                int freeSpace= getFreeRightNeighbourhood( item, column+1, item.getColumnSpan()-1)+1; 
                if(item.getColumnSpan()!=freeSpace){
                    item.setColumnSpan(freeSpace); 
                }
                freeSpace= getFreeBottomNeighbourhood( item, row+1, item.getRowSpan()-1)+1;
                if(item.getRowSpan()!=freeSpace){
                    item.setRowSpan(freeSpace); 
                }
                diagrams.add((GridLayoutItem)qlii);
                addWidget(((GridLayoutItem)qlii).getDiagram());
                ((GridLayoutItem)qlii).getDiagram().updateGeometry();
            }
        }
    }

    @Override
    public int count() {
        return diagrams.size();
    }

    @Override
    public QLayoutItemInterface itemAt(int i) {
        return i >= 0 && i < diagrams.size() ? diagrams.get(i) : null;
    }

    @Override
    public QSize sizeHint() {
      return new QSize(parent.width(),parent.height());//parent.sizeHint();
    }

    @Override
    public QLayoutItemInterface takeAt(int idx) {
        return idx >= 0 && idx < diagrams.size() ? diagrams.get(idx) : null;
    }
    
    public void deleteItem(DiagramPanel d){
        GridLayoutItem  item=getItem(d);
        diagrams.remove(item);        
    }
    
    public void updateIndexes(DiagramPanel diagramPanel,Point p){
        GridLayoutItem item=getItem( diagramPanel);
        if(item!=null){
            int index[]=getIndexByPoint(p.x, p.y);
            int newRowSpan=item.getRowSpan(), newColumnSpan=item.getColumnSpan();
            for(int row=index[1];row<index[1]+item.getRowSpan();row++){
                for(int column=index[0];column<index[0]+item.getColumnSpan();column++){
                    if(getItem(column, row)!=null){
                        if((column-index[0]==0) && (row-index[1]!=0)){
                            newRowSpan=row-1;
                            break;
                        }else{
                            newColumnSpan=Math.min(newColumnSpan, column-index[0]);
                        }
                    }
                }
            }
            for(int column=index[0];column<index[0]+newColumnSpan;column++){
                for(int row=index[1];row<index[1]+item.getRowSpan();row++){                        
                    if(getItem(column, row)!=null){
                         newRowSpan=Math.min(newRowSpan, row-index[1]);
                    }
                }
            }
            if(newRowSpan>0 && newColumnSpan>0){            
                if(newColumnSpan+index[0]>gridSize){
                    newColumnSpan=gridSize-index[0];
                }               
                if(newRowSpan+index[1]>gridSize){
                    newRowSpan=gridSize-index[1];
                }
                item.setColumn(index[0]);
                item.setRow( index[1]);
                item.setColumnSpan(newColumnSpan);
                item.setRowSpan( newRowSpan);
            }
        }
    }
        
    public GridLayoutItem getItem(DiagramPanel diagramPanel){
        for(GridLayoutItem d:diagrams){
            if(d.getDiagram().equals(diagramPanel)){
                return d;
            }
        }
        return null;
    }
    
    private int[] getIndexByPoint(int x,int y){
        float width=parent.width()/gridSize;
        float height=parent.height()/gridSize;
        int row=Math.round(x/width);
        int column=Math.round(y/height);
        return new int[]{row<16 ? row : 15, column<16 ? column : 15};
    }
    
    public GridLayoutItem getItem(int column,int row){
        for(GridLayoutItem d:diagrams){
            if(!d.getDiagram().isDragging() && (d.getColumn()<=column && d.getColumn()+d.getColumnSpan()>column) && 
                (d.getRow()<=row && d.getRow()+d.getRowSpan()>row)){
                return d;
            }
        }
        return null;
    }
    
    public void updateSpan(DiagramPanel diagram,int oldWidth,int oldHeight,boolean isLeft,boolean isUp){
        changeVerticalSpan( diagram, oldHeight, isUp);
        changeHorizontalSpan(diagram, oldWidth, isLeft);
         //justifyLayout();
    }
    
    protected void changeHorizontalSpan(DiagramPanel diagram,int oldWidth,boolean isLeft){ 
        GridLayoutItem item=getItem(diagram);
        int delta=diagram.width()-oldWidth;
        int columnSpan=item.getColumnSpan();
        double columnSpace=oldWidth/columnSpan; 
        double span=delta/columnSpace; 
        int deltaSpan=(int)Math.round(span);
        if(deltaSpan!=0){
            int newSpan=columnSpan;
            int column=item.getColumn();
            if(delta>0){            
                if(isLeft){ 
                    for(int j=item.getRow();j<item.getRow()+item.getRowSpan();j++){
                        int index=0,size=Math.min(gridSize-1,column-deltaSpan);
                        for(int i=column-1;i>=size;i--){
                            GridLayoutItem  nextItem=getItem(i,j);
                            if(nextItem!=null){
                                break;
                            }else{
                                index++;
                            }
                        }
                        deltaSpan=Math.min(deltaSpan, index);
                    }
                }else{
                    /*for(int j=item.getRow();j<item.getRow()+item.getRowSpan();j++){
                        int index=0;
                        for(int i=column+columnSpan;i<column+columnSpan+deltaSpan;i++){
                            GridLayoutItem  nextItem=getItem(i,j);
                            if(nextItem!=null){
                                break;
                            }else{
                                index++;
                            }                            
                        }
                        deltaSpan=Math.min(deltaSpan, index);
                    } */
                    deltaSpan= getFreeRightNeighbourhood( item, column+columnSpan, deltaSpan);
                }
                if(isLeft){
                    item.setColumn(column-deltaSpan/*(newSpan-columnSpan)*/);
                }
                newSpan=newSpan+deltaSpan;
                item.setColumnSpan(newSpan==0 ? 1:newSpan);
            }else {  
                newSpan=columnSpan+deltaSpan;
                item.setColumnSpan(newSpan==0 ? 1:newSpan);
                if(isLeft){
                     item.setColumn(column-deltaSpan);
                }                           
            }
        }
    }
    
    private int getFreeRightNeighbourhood(GridLayoutItem item,int column,int deltaSpan){
        if(column==15){
            return 1; 
        }else{
            for(int j=item.getRow();j<item.getRow()+item.getRowSpan();j++){
                int index=0,size=Math.min(gridSize,column+deltaSpan);
                for(int i=column;i<size;i++){
                    GridLayoutItem  nextItem=getItem(i,j);
                    if(nextItem!=null){
                        break;
                    }else{
                        index++;
                    }                            
                }
                deltaSpan=Math.min(deltaSpan, index);
            } 
        }
        return deltaSpan;
    }
    
    protected void changeVerticalSpan(DiagramPanel diagram,int oldHeight,boolean isUp){ 
        GridLayoutItem item=getItem(diagram);
        int delta=diagram.height()-oldHeight;
        int rowSpan=item.getRowSpan();
        double rowSpace=oldHeight/rowSpan; 
        double span=delta/rowSpace; 
        int deltaSpan=(int)Math.round(span);
        if(deltaSpan!=0){
            int newSpan=rowSpan;
            int row=item.getRow();
            if(delta>0){            
                if(isUp){ 
                    for(int j=item.getColumn();j<item.getColumn()+item.getColumnSpan();j++){
                        int index=0,size=Math.min(gridSize-1,row-deltaSpan);
                        for(int i=row-1;i>=size;i--){
                            GridLayoutItem  nextItem=getItem(j,i);
                            if(nextItem!=null){
                                break;
                            }else{
                                index++;
                            }
                        } 
                        deltaSpan=Math.min(deltaSpan, index);
                    }
                }else{
                    /*for(int j=item.getColumn();j<item.getColumn()+item.getColumnSpan();j++){
                        int index=0;
                        for(int i=row+rowSpan;i<row+rowSpan+deltaSpan;i++){
                            GridLayoutItem  nextItem=getItem(j,i);
                            if(nextItem!=null){
                                break;
                            }else{
                                index++;
                            }
                        }
                        deltaSpan=Math.min(deltaSpan, index);
                    }*/
                    deltaSpan=getFreeBottomNeighbourhood(item,row+rowSpan, deltaSpan);
                }
                if(isUp){
                    item.setRow(row-deltaSpan);
                }
                newSpan=newSpan+deltaSpan;
                item.setRowSpan(newSpan==0 ? 1:newSpan);
            }else{ 
                newSpan=rowSpan+deltaSpan;
                item.setRowSpan(newSpan==0 ? 1:newSpan);
                if(isUp){
                     item.setRow(row-deltaSpan);
                }                                           
            }
        }
    }
    
     private int getFreeBottomNeighbourhood(GridLayoutItem item,int row,int deltaSpan){
        if(row==15){
            return 1; 
        }else{
            for(int j=item.getColumn();j<item.getColumn()+item.getColumnSpan();j++){
                int index=0,size=Math.min(gridSize,row+deltaSpan);                
                for(int i=row;i<size;i++){
                    GridLayoutItem  nextItem=getItem(j,i);
                    if(nextItem!=null){
                        break;
                    }else{
                        index++;
                    }                            
                }
                deltaSpan=Math.min(deltaSpan, index);
            } 
        }
        return deltaSpan;
    }
}
