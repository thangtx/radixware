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
package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import java.awt.Component;
import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidgetContainer;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.enums.EReportLayout;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;

public abstract class ReportLayoutProcessor {

    protected final AdsReportBaseContainer reportWidgetContainer;
    private boolean lock = false;
    protected static final int DEFAULT_HIGHLIGHT_RECT_SIZE = 0;

    protected enum APPEND_MODE {

        REPLACE,
        ADD,
        HORIZONTAL,
        VERTICAL
    }

    protected ReportLayoutProcessor(final AdsReportBaseContainer reportWidgetContainer) {
        this.reportWidgetContainer = reportWidgetContainer;
    }

    abstract void justifyLayout();

    abstract EReportLayout getType();

    abstract InsertionInfo indexFromPoint(Point p, AdsReportSelectableWidget curcell);

    abstract void updateIndex(AdsReportSelectableWidget widget, InsertionInfo info);

    abstract void highlightInsertPlace(InsertionInfo info);

    abstract void changeSpan(AdsReportSelectableWidget cellWidget, int oldWidth, int oldHeight, boolean isUp, boolean isLeft);

    abstract EReportLayout type();

    void setLock(boolean b) {
        lock = b;
    }

    boolean isLock() {
        return lock;
    }

    protected int checkOnDraggedColumns(int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            int dragCnt = 0, cnt = 0;
            for (AdsReportSelectableWidget w : reportWidgetContainer.getCellWidgets()) {
                AdsReportWidget cell = w.getCell();
                //if(cell.getColumn()==i){
                if (cell.getColumn() <= i && cell.getColumn() + cell.getColumnSpan() > i) {
                    if (w.isDragged()) {
                        dragCnt++;
                    }
                    cnt++;
                }
            }
            if (cnt - dragCnt == 0) {
                for (AdsReportSelectableWidget w : reportWidgetContainer.getCellWidgets()) {
                    AdsReportWidget cell = w.getCell();
                    if (cell.getColumn() > i) {
                        cell.setColumn(cell.getColumn() - 1);
                    }
                }
                i--;
                columnCount--;
            }
        }
        return columnCount;
    }

    protected int checkOnDraggedRows(int rowCount) {
        for (int i = 0; i < rowCount; i++) {
            int cnt = 0, dragCnt = 0;
            for (AdsReportSelectableWidget w : reportWidgetContainer.getCellWidgets()) {
                AdsReportWidget cell = w.getCell();
                //if(cell.getRow()==i || cell.getRow()+cell.getRowSpan()-1==i){
                if (cell.getRow() <= i && cell.getRow() + cell.getRowSpan() > i) {
                    if (w.isDragged()) {
                        dragCnt++;
                    }
                    cnt++;
                }
            }
            if (cnt - dragCnt == 0) {
                for (AdsReportSelectableWidget w : reportWidgetContainer.getCellWidgets()) {
                    AdsReportWidget cell = w.getCell();
                    if (cell.getRow() > i) {
                        cell.setRow(cell.getRow() - 1);
                    }
                }
                i--;
                rowCount--;
            }
        }
        return rowCount;
    }

    protected void shiftCells(AdsReportSelectableWidget widget, APPEND_MODE mode, int column, int row, int shift) {
        if (mode != APPEND_MODE.REPLACE) {
            for (AdsReportSelectableWidget w : reportWidgetContainer.getCellWidgets()) {
                AdsReportWidget cell = w.getCell();
                if (widget == null || (widget != null && !widget.getCell().equals(cell))) {
                    if (mode == APPEND_MODE.VERTICAL && cell.getRow() >= row) {
                        cell.setRow(cell.getRow() + shift);
                    }
                }
                if (mode == APPEND_MODE.HORIZONTAL && cell.getColumn() >= column) {
                    cell.setColumn(cell.getColumn() + shift);
                }
            }
        }
    }

    protected void changeHorizontalSpan(AdsReportSelectableWidget cellWidget, int oldWidth, boolean isLeft) {
        int delta = cellWidget.getWidth() - oldWidth;
        int columnSpan = cellWidget.getCell().getColumnSpan();
        final double columnSpace = oldWidth / columnSpan;
        double span = delta / columnSpace;
        int deltaSpan = (int) Math.round(span);
        if (deltaSpan != 0) {
            int newSpan = columnSpan + deltaSpan;
            if (newSpan == 0) {
                newSpan = 1;
            }
            cellWidget.getCell().setColumnSpan(newSpan);
            int column = cellWidget.getCell().getColumn();
            if (delta > 0) {
                boolean isClear = true;
                if (isLeft) {
                    for (int i = column - 1; i >= column - deltaSpan; i--) {
                        AdsReportSelectableWidget widget = findWidgetByIndex(i, cellWidget.getCell().getRow(), cellWidget);
                        if (widget != null) {
                            isClear = false;
                            break;
                        }
                    }
                } else {
                    for (int i = column + columnSpan; i < column + columnSpan + deltaSpan; i++) {
                        AdsReportSelectableWidget widget = findWidgetByIndex(i, cellWidget.getCell().getRow(), cellWidget);
                        if (widget != null) {
                            isClear = false;
                            break;
                        }
                    }
                }
                if (isClear) {
                    if (isLeft) {
                        cellWidget.getCell().setColumn(column - deltaSpan);
                    }
                    return;
                }
                if (!isLeft) {
                    column = column + 1;
                }
                shiftCells(cellWidget, APPEND_MODE.HORIZONTAL, column, cellWidget.getCell().getRow(), deltaSpan);
            } else {
                column = column + 1;
                for (int i = column; i < column - deltaSpan; i++) {
                    boolean needDeleteColumn = true;
                    for (AdsReportSelectableWidget w : reportWidgetContainer.getCellWidgets()) {
                        AdsReportWidget cell = w.getCell();
                        if (cell.getColumn() <= i && cell.getColumn() + cell.getColumnSpan() > i) {
                            needDeleteColumn = false;
                            break;
                        }
                    }
                    if (needDeleteColumn) {
                        shiftCells(cellWidget, APPEND_MODE.HORIZONTAL, i, 0, -1);
                    } else if (isLeft) {
                        cellWidget.getCell().setColumn(column);
                    }
                }
            }
        }
    }

    protected void changeVerticalSpan(AdsReportSelectableWidget cellWidget, int oldHeight, boolean isUp) {
        int delta = cellWidget.getHeight() - oldHeight;
        int rowSpan = cellWidget.getCell().getRowSpan();
        final double rowSpace = oldHeight / rowSpan;
        double span = delta / rowSpace;
        int deltaSpan = (int) Math.round(span);
        if (deltaSpan != 0) {
            int newSpan = rowSpan + deltaSpan;
            if (newSpan == 0) {
                newSpan = 1;
            }
            cellWidget.getCell().setRowSpan(newSpan);
            int row = cellWidget.getCell().getRow();
            if (delta > 0) {
                boolean isClear = true;
                if (isUp) {
                    for (int i = row - 1; i >= row - deltaSpan; i--) {
                        AdsReportSelectableWidget widget = findWidgetByIndex(cellWidget.getCell().getColumn(), i, cellWidget);
                        if (widget != null) {
                            isClear = false;
                            break;
                        }
                    }
                } else {
                    for (int i = row + rowSpan; i < row + rowSpan + deltaSpan; i++) {
                        AdsReportSelectableWidget widget = findWidgetByIndex(cellWidget.getCell().getColumn(), i, cellWidget);
                        if (widget != null) {
                            isClear = false;
                            break;
                        }
                    }
                }
                if (isClear) {
                    if (isUp) {
                        cellWidget.getCell().setRow(row - deltaSpan);
                    }
                    return;
                }
                if (!isUp) {
                    row = row + 1;
                }
                shiftCells(cellWidget, APPEND_MODE.VERTICAL, cellWidget.getCell().getColumn(), row, deltaSpan);
            } else {
                row = row + 1;
                for (int i = row; i < row - deltaSpan; i++) {
                    boolean needDeleteRow = true;
                    for (AdsReportSelectableWidget w : reportWidgetContainer.getCellWidgets()) {
                        AdsReportWidget cell = w.getCell();
                        if (cell.getRow() <= i && cell.getRow() + cell.getRowSpan() > i) {
                            needDeleteRow = false;
                            break;
                        }
                    }
                    if (needDeleteRow) {
                        shiftCells(cellWidget, APPEND_MODE.VERTICAL, 0, i, -1);
                    } else if (isUp) {
                        cellWidget.getCell().setRow(row);
                    }
                }
            }
        }
    }

    protected InsertionInfo checkOnAdd(AdsReportSelectableWidget curcell, AdsReportSelectableWidget widget, Point point) {
        if (widget != null && (widget instanceof AdsReportBaseContainer)) {
            //AdsReportWidget containerCell=widget.getCell();

            if (widget.getDiagramMode() == AdsReportForm.Mode.GRAPHICS) {
                double[] containerPos = getContainetPositionMM(widget);
                if (curcell != null) {
                    AdsReportWidget cell = curcell.getCell();
                    point = new Point(MmUtils.mm2px(cell.getLeftMm() - containerPos[0]), MmUtils.mm2px(cell.getTopMm() - containerPos[1]));
                }
            } else {
                int[] containerPos = getContainetPositionTxt(widget);
                if (curcell != null) {
                    AdsReportWidget cell = curcell.getCell();
                    point = new Point(TxtUtils.columns2Px(cell.getLeftColumn() - containerPos[0]), TxtUtils.rows2Px(cell.getTopRow() - containerPos[1]));
                }
            }
            InsertionInfo info = ((AdsReportBaseContainer) widget).getReportLayout().indexFromPoint(point, curcell);
            if (info != null && info.getParent() == null) {
                info.setParent((AdsReportBaseContainer) widget);
            }
            return info;
        }
        return null;
    }

    protected double[] getContainetPositionMM(AdsReportSelectableWidget containerCell) {
        double[] res = new double[2];
        res[0] = containerCell.getCell().getLeftMm();
        res[1] = containerCell.getCell().getTopMm();
        Component parent = containerCell.getParent();
        while (parent != null && (parent instanceof AdsReportBaseContainer) && ((AdsReportSelectableWidget) parent).getCell() != null) {
            res[0] += ((AdsReportSelectableWidget) parent).getCell().getLeftMm();
            res[1] += ((AdsReportSelectableWidget) parent).getCell().getTopMm();
            parent = parent.getParent();
        }
        return res;
    }

    protected int[] getContainetPositionTxt(AdsReportSelectableWidget containerCell) {
        int[] res = new int[2];
        res[0] = containerCell.getCell().getLeftColumn();
        res[1] = containerCell.getCell().getTopRow();
        Component parent = containerCell.getParent();
        while (parent != null && (parent instanceof AdsReportBaseContainer) && ((AdsReportSelectableWidget) parent).getCell() != null) {
            res[0] += ((AdsReportSelectableWidget) parent).getCell().getLeftColumn();
            res[1] += ((AdsReportSelectableWidget) parent).getCell().getTopRow();
            parent = parent.getParent();
        }
        return res;
    }

    protected void add(AdsReportSelectableWidget curcell, AdsReportBaseContainer widget) {
        IReportWidgetContainer oldCellContainer = ((AdsReportBaseContainer) curcell.getParent()).getReportWidgetContainer();
        oldCellContainer.getWidgets().remove(curcell.getCell());
        AdsReportWidgetContainer cellContainer = (AdsReportWidgetContainer) widget.getCell();
        if (cellContainer != null) {
            cellContainer.getWidgets().add(curcell.getCell());
            widget.addCellWidget(curcell);
        }
    }

    public static final class Factory {

        public static ReportLayoutProcessor newInstance(final AdsReportBaseContainer reportWidgetContainer) {
            if (reportWidgetContainer != null && reportWidgetContainer.getReportWidgetContainer() != null) {
                EReportLayout layout = reportWidgetContainer.getReportWidgetContainer().getLayout();
                if (layout == EReportLayout.HORIZONTAL) {
                    return new HorizontalLayoutProcessor(reportWidgetContainer);
                } else if (layout == EReportLayout.VERTICAL) {
                    return new VerticalLayoutProcessor(reportWidgetContainer);
                } else if (layout == EReportLayout.GRID) {
                    return new GridLayoutProcessor(reportWidgetContainer);
                }
            }
            return new FreeLayoutProcessor(reportWidgetContainer);
        }

    }

    protected int getIndexMM(double posMm, double size, int count) {
        //double posMm=MmUtils.px2mm( x+space);
        //posMm=reportWidgetContainer.getCell()!=null?posMm-reportWidgetContainer.getCell().getLeftMm():posMm;
        if (count > 0) {
            double space = size / count;
            if (posMm > space * (count - 1)) {
                return count - 1;
            } else if (posMm <= 0) {
                return 0;
            } else {
                for (int i = 0; i < count; i++) {
                    double x = space * i;
                    if (posMm >= x && posMm < x + space) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    protected int getIndexTxt(int posTxt, double size, int count) {
        if (count > 0) {
            double space = size / count;
            if (posTxt > space * (count - 1)) {
                return count - 1;
            } else if (posTxt <= 0) {
                return 0;
            } else {
                for (int i = 0; i < count; i++) {
                    double x = space * i;
                    if (posTxt >= x && posTxt < x + space) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    protected List<Size> getRowsSize(int rowCnt, AdsReportSelectableWidget curcell) {
        List<Size> rowsSize = new LinkedList<>();
        for (int i = 0; i < rowCnt; i++) {
            for (AdsReportSelectableWidget w : reportWidgetContainer.getCellWidgets()) {
                if (w != null && !w.equals(curcell)) {
                    AdsReportWidget cell = w.getCell();
                    if (cell.getRow() == i) {
                        Size rowSize = new Size(MmUtils.mm2px(cell.getTopMm()), MmUtils.mm2px(cell.getHeightMm()), cell.getRowSpan());
                        rowsSize.add(rowSize);
                        break;
                    }
                }
            }
        }
        return rowsSize;
    }

    protected AdsReportSelectableWidget findWidgetByIndex(int column, int row, AdsReportSelectableWidget curcell) {
        for (AdsReportSelectableWidget w : reportWidgetContainer.getCellWidgets()) {
            if (!w.equals(curcell)) {
                AdsReportWidget cell = w.getCell();
                if ((cell.getColumn() <= column && cell.getColumn() + cell.getColumnSpan() > column)
                        && (cell.getRow() <= row && cell.getRow() + cell.getRowSpan() > row)) {
                    return w;
                }
            }
        }
        return null;
    }

    public class Size {

        private double start;
        private double width;
        private int snap = 1;

        Size(double start, double width, int snap) {
            this.start = start;
            this.width = width;
            this.snap = snap;
        }

        double getStart() {
            return start;
        }

        double getWidth() {
            return width;
        }

        int getSnap() {
            return snap;
        }

    }

    public class InsertionInfo {

        private int[] idx;

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final InsertionInfo other = (InsertionInfo) obj;
            if (!Arrays.equals(this.idx, other.idx)) {
                return false;
            }
            if (!Objects.equals(this.parent, other.parent)) {
                return false;
            }
            if (this.mode != other.mode) {
                return false;
            }
            if (this.isAdding != other.isAdding) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return 3;
        }
        private AdsReportBaseContainer parent;
        private APPEND_MODE mode;
        private boolean isAdding = false;
        //private int span=0;

        InsertionInfo(int[] idx, APPEND_MODE mode, AdsReportBaseContainer parent) {
            if (idx == null) {
                this.idx = null;
            } else {
                this.idx = Arrays.copyOf(idx, idx.length);
            }
            this.parent = parent;
            this.mode = mode;
        }

        //InsertionInfo(int[] idx,APPEND_MODE mode,AdsReportBaseContainer parent,int span){
        //   this(idx,mode,parent);
        //this.span=span;
        //}
        int[] getIdx() {
            if (idx == null) {
                return null;
            }
            return Arrays.copyOf(idx, idx.length);
        }

        boolean isAdding() {
            return isAdding;
        }

        void setIsAdding(boolean isAdding) {
            this.isAdding = isAdding;
        }

        AdsReportBaseContainer getParent() {
            return parent;
        }

        void setParent(AdsReportBaseContainer parent) {
            this.parent = parent;
        }

        APPEND_MODE getMode() {
            return mode;
        }

        void setMode(APPEND_MODE mode) {
            this.mode = mode;
        }

        //int getSpan(){
        //   return span;
        //}
    }

    public static class ReportLayoutEvent extends RadixEvent {

        private int index;
        private final AdsReportBandSubWidget band;

        protected ReportLayoutEvent(AdsReportBandSubWidget band, int index) {
            this.index = index;
            this.band = band;
        }

        AdsReportBandSubWidget getBandSubWidget() {
            return band;
        }

        int getIndex() {
            return index;
        }
    }

    public static class AddColumnEvent extends ReportLayoutEvent {

        public AddColumnEvent(AdsReportBandSubWidget band, int index) {
            super(band, index);
        }

    }

    public static class RemoveColumnEvent extends ReportLayoutEvent {

        public RemoveColumnEvent(AdsReportBandSubWidget band, int index) {
            super(band, index);
        }

    }

    public interface IReportLayoutListener extends IRadixEventListener<ReportLayoutEvent> {
    }

    protected final RadixEventSource<IReportLayoutListener, ReportLayoutEvent> reportLayoutSupport = new RadixEventSource<>();

    public void addChangeListener(IReportLayoutListener l) {
        reportLayoutSupport.addEventListener(l);
    }

    public void removeChangeListener(IReportLayoutListener l) {
        reportLayoutSupport.removeEventListener(l);
    }
}

/* protected Integer getIndex(List<GridLayoutProcessor.Size> sizes, int p){
 int size=sizes.size(); 
 int index=0;
 for(int i=0;i<sizes.size();i++){//Size columnSize:columnsSize){
 //while (index<sizes.size()){
 GridLayoutProcessor.Size s=sizes.get(i);
 if(p>=s.getStart()  && p<=s.getStart()  + s.getWidth() ){
 double space=s.getWidth()/s.getSnap();
 for(int j=0;j<s.getSnap();j++){
 if(p>=s.getStart() +space*j && p<=s.getStart()  +space*j + space)
 break;
 index++;
 }
 return index;
 }
 index=index+s.getSnap();
 }
 if(size>0){
 GridLayoutProcessor.Size s=sizes.get(size-1);
 if(p>=s.getStart()+s.getWidth())
 return index-sizes.get(size-1).getSnap();
 else if(p<=0)
 return 0;
 }
 return  -1;
 }
     
 protected List<Size> getColumnsSize(int columnCnt, AdsReportSelectableWidget curcell){
 List<Size> columnsSize=new  LinkedList<>();
 for(int i=0;i<columnCnt;i++){
 for(AdsReportSelectableWidget w:reportWidgetContainer.getCellWidgets()){
 if(w!=null && !w.equals(curcell)){
 AdsReportWidget cell=w.getCell();
 if(cell.getColumn()==i){
 Size columnSize=new Size(MmUtils.mm2px(cell.getLeftMm()),MmUtils.mm2px(cell.getWidthMm()),cell.getColumnSpan());
 columnsSize.add(columnSize);
 break;
 }
 }
 }
 }
 return  columnsSize;
 }*/
