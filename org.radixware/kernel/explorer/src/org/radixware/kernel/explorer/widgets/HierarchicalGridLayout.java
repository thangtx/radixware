/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.widgets;

import org.radixware.kernel.explorer.types.Margins;
import com.trolltech.qt.core.QMargins;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QLayoutItemInterface;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QWidgetItem;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class HierarchicalGridLayout extends QLayout{
    
    private final static int QLAYOUTSIZE_MAX = Integer.MAX_VALUE/256/16;
    private final static int QWIDGETSIZE_MAX = 16777215;    
    private final static QSizePolicy.ControlTypes DEFAULT_CONTROL_TYPES = new QSizePolicy.ControlTypes(QSizePolicy.ControlType.DefaultType);
    private final static MathContext INTEGER_CONTEXT = new MathContext(0, RoundingMode.HALF_EVEN);
            
    public final static class ItemPosition{
        
        public final int row;
        public final int col;        
        public final int rowSpan;
        public final int colSpan;
        
        private ItemPosition(final int row, final int col, final int rowSpan, final int colSpan){
            this.row = row;
            this.col = col;
            this.rowSpan = rowSpan;
            this.colSpan = colSpan;
        }        
    }
    
    private final static class Paddings{
        
        public final static Paddings ZERO = new Paddings(0, 0);
        
        public final int left;
        public final int right;
        public final int summary;
        
        public Paddings(final int leftPadding, final int rightPadding){
            left = leftPadding;
            right = rightPadding;
            summary = left + right;
        }
        
        public Paddings getLeftExpanded(final int leftPadding){
            return leftPadding>left ? new Paddings(leftPadding, right) : this;
        }
        
        public Paddings getRightExpanded(final int rightPadding){
            return rightPadding>right ? new Paddings(left, rightPadding) : this;
        }
        
        public Paddings addMargins(final Margins margins){
            return margins==null ? this : new Paddings(left + margins.left, right + margins.right);
        }
        
        public Paddings addMargins(final QMargins margins){
            return margins==null ? this : new Paddings(left + margins.left(), right +margins.right());
        }
        
        public Paddings removeMargins(final Margins margins){
            return margins==null ? this : new Paddings(left - margins.left, right - margins.right);
        }
        
        public Paddings removeMargins(final QMargins margins){
            return margins==null ? this : new Paddings(left - margins.left(), right - margins.right());
        }
        
        @Override
        public String toString(){
            return "[left="+left+"; right="+right+"]";
        }
    }
    
    private final static class SizeInfo{
        
        public final Dimension minSize;
        public final Dimension maxSize;
        public final Dimension sizeHint;
                
        public SizeInfo(final Dimension minSize, final Dimension maxSize, final Dimension sizeHint){
            this.minSize = minSize;
            this.maxSize = maxSize;
            this.sizeHint = sizeHint;
        }
        
        public SizeInfo(final Cell cell){
            this(cell.minimumSize(), cell.maximumSize(), cell.sizeHint());
        }
    }
    
    private final static class LinkedSizeData{
        
        public final static LinkedSizeData EMPTY = new LinkedSizeData();
        
        private final List<SizeData> linkedSizes;
                
        private LinkedSizeData(){
            linkedSizes = Collections.<SizeData>emptyList();
        }
        
        public LinkedSizeData(final SizeData data){
            linkedSizes = new LinkedList<>();
            linkedSizes.add(data);
        }
        
        public void linkSizeData(final SizeData data){
            linkedSizes.add(data);
        }
        
        public void unlinkSizeData(final SizeData data){
            linkedSizes.remove(data);
        }
        
        public int getStretch(){
            int stretch = 0;
            for (SizeData data: linkedSizes){
                stretch = Math.max(stretch, data.stretch);
            }
            return stretch;
        }
        
        public int getMinimumSize(){
            int size = 0;
            for (SizeData data: linkedSizes){
                size = Math.max(size, data.minimumSize);
            }
            return size;
        }
        
        public int getSizeHint(){
            int size = 0;
            for (SizeData data: linkedSizes){
                size = Math.max(size, data.sizeHint);
            }
            return size;
        }
        
        public int getMaximumSize(){
            int size = 0;
            for (SizeData data: linkedSizes){
                size = Math.min(size, data.maximumSize);
            }
            return size;
        }
        
        public int getSpacing(){
            int spacing = 0;
            for (SizeData data: linkedSizes){
                spacing = Math.max(spacing, data.spacing);
            }
            return spacing;
        }
        
        public int getPadding(){
            int padding = 0;
            for (SizeData data: linkedSizes){
                padding = Math.max(padding, data.padding);
            }
            return padding;
        }
        
        public boolean isExpansive(){
            for (SizeData data: linkedSizes){
                if (data.isExpansive()){
                    return true;
                }
            }
            return false;
        }
        
        public boolean isEmpty(){
            for (SizeData data: linkedSizes){
                if (!data.isEmpty()){
                    return false;
                }
            }
            return true;
        }        

        public void expandStretch(final int s) {
            for (SizeData data: linkedSizes){
                data.expandStretch(s);
            }
        }

        public void expandMaximumSize(final int size) {
            for (SizeData data: linkedSizes){
                data.expandMaximumSize(size);
            }            
        }

        public void setMaximumSize(final int size) {
            for (SizeData data: linkedSizes){
                data.setMaximumSize(size);
            }
        }

        public void setSizeHint(final int size) {
            for (SizeData data: linkedSizes){
                data.setSizeHint(size);
            }
        }

        public void expandSizeHint(final int size) {
            for (SizeData data: linkedSizes){
                data.expandSizeHint(size);
            }
        }

        public void setMinimumSize(final int size) {
            for (SizeData data: linkedSizes){
                data.setMinimumSize(size);
            }
        }

        public void expandMinimumSize(final int size) {
            for (SizeData data: linkedSizes){
                data.expandMinimumSize(size);
            }
        }

        public void setSize(final int s) {
            for (SizeData data: linkedSizes){
                data.setSize(s);
            }
        }

        public void updateExpansive() {
            for (SizeData data: linkedSizes){
                data.updateExpansive();
            }
        }

        public void setEmpty(final boolean empty) {
            for (SizeData data: linkedSizes){
                data.setEmpty(empty);
            }
        }

        public void expandSpacing(final int s) {
            for (SizeData data: linkedSizes){
                data.expandSpacing(s);
            }
        }

        public void expandToCell(final int cellMax, final boolean cellExp, final boolean cellEmpty) {
            for (SizeData data: linkedSizes){
                data.expandToCell(cellMax, cellExp, cellEmpty);
            }
        }
        
        public void makeExpansive(){
            for (SizeData data: linkedSizes){
                data.makeExpansive();
            }
        }
        
        public void setStretch(final int stretch){
            for (SizeData data: linkedSizes){
                data.stretch = stretch;
            }
        }
    }
    
    private final static class SizeData{
        
        public static enum SizeType{Size, SizeHint, MinimumSize, MaximumSize};
        
        // parameters
        private int stretch;        
        private int sizeHint;
        private int maximumSize;
        private int minimumSize;
        private boolean expansive;
        private boolean empty;
        private int spacing;
        private int padding;

        // temporary storage
        private  boolean done;

        // result
        private int pos;
        private int size;
        
        private LinkedSizeData linkedData = LinkedSizeData.EMPTY;
        
        SizeData() {
            init(0, 0, QLAYOUTSIZE_MAX, LinkedSizeData.EMPTY);
        }
        
        public void init(final int stretchFactor, final int minSize, final int maxSize, final LinkedSizeData linkedSizeData){
            final boolean isLinkedDataEmpty = linkedSizeData==null || linkedSizeData==LinkedSizeData.EMPTY;
            if (isLinkedDataEmpty){
                stretch = stretchFactor;       
                minimumSize = minSize;
                sizeHint = minSize;            
                maximumSize = maxSize<=0 ? QLAYOUTSIZE_MAX : maxSize;
            }else{
                final int linkedSizesStretch = linkedSizeData.getStretch();
                if (stretchFactor>linkedSizesStretch){
                    stretch = stretchFactor;
                    linkedSizeData.setStretch(stretchFactor);
                }else{
                    stretch = linkedSizesStretch;
                }
                
                final int linkedSizesMin = linkedSizeData.getMinimumSize();                
                if (minSize>linkedSizesMin){
                    minimumSize = minSize;
                    linkedSizeData.setMinimumSize(minSize);
                }else{
                    minimumSize = linkedSizesMin;
                }
                
                final int linkedSizesHint = linkedSizeData.getSizeHint();
                if (minSize>linkedSizesHint){
                    sizeHint = minSize;
                    linkedSizeData.setSizeHint(minSize);
                }else{
                    sizeHint = linkedSizesHint;
                }
                
                maximumSize = linkedSizeData.getMaximumSize();
            }
            expansive = isLinkedDataEmpty ? false : linkedSizeData.isExpansive();
            empty = isLinkedDataEmpty ? true : linkedSizeData.isEmpty();
            spacing = isLinkedDataEmpty ? 0 : linkedSizeData.getSpacing();            
            linkedData = isLinkedDataEmpty ? LinkedSizeData.EMPTY : linkedSizeData;
            padding = isLinkedDataEmpty ? 0 : linkedSizeData.getPadding();
        }
        
        public boolean isExpansive(){
            return expansive;
        }     
        
        public int getStretch(){
            return stretch;
        }
                
        public void expandStretch(final int s){
            if (s>stretch){
                stretch = s;
            }
            linkedData.expandStretch(s);
        }
        
        public int getSizeHint(){
            return sizeHint;
        }
        
        public int getMinimumSize(){
            return empty ? minimumSize + padding : minimumSize; //in empty columns space value does not respected
        }
        
        public void expandMaximumSize(final int size){
            if (size>maximumSize){
                maximumSize = size;
            }
            linkedData.expandMaximumSize(size);
        }
        
        public void setMaximumSize(final int size){
            maximumSize = size;
            linkedData.setMaximumSize(size);            
        }        
        
        public int getMaximumSize(){
            return maximumSize==0 && isExpansive() ? QWIDGETSIZE_MAX : maximumSize;
        }

        public void setSizeHint(final int size){
            sizeHint = size;
            linkedData.setSizeHint(size);
        }
        
        public void expandSizeHint(final int size){
            if (size>sizeHint){
                sizeHint = size;
            }
            linkedData.expandSizeHint(size);
        }
        
        public void setMinimumSize(final int size){
            minimumSize = size;
            linkedData.setMinimumSize(size);
        }
        
        public void expandMinimumSize(final int size){
            if (size>minimumSize){
                minimumSize = size;
            }
            linkedData.expandMinimumSize(size);
        }
        
        public void setSize(final int s){
            size = s;
            linkedData.setSize(s);
        }
        
        public int getSize(){
            return size;
        }                

        public int smartSizeHint() {
            if (stretch > 0){
                return empty ? minimumSize + padding : minimumSize;
            }else{
                return empty ? sizeHint + padding : sizeHint;
            }
        }
        
        public int effectiveSpacer(final int uniformSpacer) {            
            return (uniformSpacer >= 0) ? uniformSpacer : spacing+padding;
        }
        
        public void updateExpansive(){
            expansive = expansive || stretch>0;
            linkedData.updateExpansive();
        }
        
        public boolean isEmpty(){
            return empty;
        }
        
        public void setEmpty(final boolean isEmpty){
            empty = isEmpty;
            linkedData.setEmpty(isEmpty);
        }
                        
        public void expandSpacing(final int s){
            if (s>spacing){
                spacing = s;
            }
            linkedData.expandSpacing(s);
        }
        
        public int getSpacing(){
            return spacing + padding;
        }
        
        public void expandPadding(final int p){
            if (p>padding){
                padding = p;
            }
        }
        
        public void clearPadding(){
            padding = 0;
        }
        
        public int getPadding(){
            return padding;
        }
        
        public int getPos(){
            return pos;
        }
        
        public void setPos(final int p){
            pos = p;
        }
        
        public void setDone(final boolean isDone){
            done = isDone;
        }
        
        public boolean isDone(){
            return done;
        }
        
        public int getSize(SizeType sizeType){
            switch(sizeType){
                case SizeHint:
                    return getSizeHint();
                case MaximumSize:
                    return getMaximumSize();
                case MinimumSize:
                    return getMinimumSize();
                default:
                    return getSize();
            }
        }
        
        /*
          Modify total maximum (max), total expansion (exp), and total empty
          when adding boxmax/boxexp.

          Expansive boxes win over non-expansive boxes.
          Non-empty boxes win over empty boxes.
        */        
        public void expandToCell(final int cellMax, final boolean cellExp, final boolean cellEmpty){
            if (expansive) {
                if (cellExp){
                    maximumSize = Math.max(maximumSize, cellMax);
                }
            } else {
                if (cellExp || (empty && (!cellEmpty || maximumSize == 0))){
                    maximumSize = cellMax;
                } else if (empty == cellEmpty){
                    maximumSize = Math.min(maximumSize, cellMax);
                }
            }
            expansive = expansive || cellExp;
            empty = empty && cellEmpty;
            linkedData.expandToCell(cellMax, cellExp, cellEmpty);
        }
        
        public void makeExpansive(){
            expansive = true;
            linkedData.makeExpansive();
        }
        
        public void link(final LinkedSizeData data){
            linkedData = data==null ? LinkedSizeData.EMPTY : data;
        }
        
        public void unlink(){
            linkedData = LinkedSizeData.EMPTY;
        }
        
        public void print(final StringBuilder out, final String padding){
            printValue("done",String.valueOf(done),out,padding);
            printValue("empty",String.valueOf(empty),out,padding);
            printValue("expansive",String.valueOf(expansive),out,padding);
            printValue("maximum",String.valueOf(maximumSize),out,padding);
            printValue("minimum",String.valueOf(minimumSize),out,padding);
            printValue("hint",String.valueOf(sizeHint),out,padding);
            printValue("spacing",String.valueOf(spacing),out,padding);
            printValue("padding",String.valueOf(this.padding),out,padding);
            printValue("stretch",String.valueOf(stretch),out,padding);
            printValue("padding",String.valueOf(padding),out,padding);
            printValue("pos",String.valueOf(pos),out,padding);
            printValue("size",String.valueOf(size),out,padding);                        
        }
        
        private static void printValue(final String name, final String value, final StringBuilder out, final String padding){
            out.append(padding);
            out.append(name);
            out.append(":\t\t");
            out.append(value);
            out.append("\n");
        }
    };
        
    private final static class Cell{
        
        private final static QRect TEMPORARY_RECT = new QRect();
        
        private final QLayoutItemInterface item;
        private HierarchicalGridLayout childLayout;
        private int row, col;
        private int torow, tocol;
        
        public Cell(final QLayoutItemInterface item){
            this.item = item;
        }
        
        public Cell(final QWidget widget){
            item = new QWidgetItem(widget);
        }        
        
        public Dimension sizeHint(){
            return qSize2Dimension(item.sizeHint()); 
        }
        
        public Dimension minimumSize(){ 
            return qSize2Dimension(item.minimumSize()); 
        }
        
        public Dimension maximumSize(){
            return qSize2Dimension(item.maximumSize()); 
        }
        
        public Qt.Orientations expandingDirections() { 
            return item.expandingDirections(); 
        }
        
        public boolean isEmpty() { 
            return item.isEmpty(); 
        }

        public boolean hasHeightForWidth() { 
            return item.hasHeightForWidth(); 
        }
        
        public int heightForWidth(final int w) { 
            return item.heightForWidth(w); 
        }

        public void setAlignment(final Qt.Alignment a) {             
            item.setAlignment(a); 
        }
        
        public void setGeometry(final QRect r) { 
            item.setGeometry(r);
        }
        
        public boolean setGeometry(final Rectangle geometry){
            item.setGeometry(writeTemporaryRect(geometry));
            return true;
        }
        
        public Qt.Alignment alignment() { 
            return item.alignment(); 
        }
        
        public QWidget getWidget(){
            return item.widget();
        }
        
        public QRect getGeometry(){
            return item.geometry();
        }
        
        public QSizePolicy.ControlTypes getControlTypes(){
            return item.controlTypes();
        }
        
        public QLayoutItemInterface item() { 
            return item; 
        }

        public int hStretch() { 
            return item.widget()==null ? 0: item.widget().sizePolicy().horizontalStretch();
        }
        
        public int vStretch() { 
            return item.widget()==null ? 0 : item.widget().sizePolicy().verticalStretch();
        }
        
        public int toRow(final int rr) { 
            return torow >= 0 ? torow : rr - 1; 
        }
        
        public int toCol(final int cc) { 
            return tocol >= 0 ? tocol : cc - 1; 
        }
        
        public int getColumn(){
            return col;
        }
        
        public int getRow(){
            return row;
        }
        
        public void setRow(final int r){
            row = r;
        }                
        
        public void setColumn(final int c){
            col = c;
        }
        
        public void setGeometry(final int r, final int c, final int toR, final int toC){
            row = r;
            torow = toR;
            col = c;
            tocol = toC;
        }

        public HierarchicalGridLayout getChildLayout() {
            return childLayout;
        }

        public void setChildLayout(final HierarchicalGridLayout childLayout) {
            this.childLayout = childLayout;
        }
        
        public Cell mapToColumn(final int column){
            final Cell mappedCell = new Cell(item);
            mappedCell.row = row;
            mappedCell.torow = torow;
            final int offset = column - col;
            mappedCell.col = column;
            mappedCell.tocol = mappedCell.tocol + offset;
            return mappedCell;
        }
        
        private static QRect writeTemporaryRect(final Rectangle rect){
            TEMPORARY_RECT.setRect(rect.x, rect.y, rect.width, rect.height);
            return TEMPORARY_RECT;
        }        
    }
    
    private static enum SizeDataState{
        OBSOLETE, CACLCULATED_FOR_SIZE_HINT, CALCULATED_FOR_DISTRIBUTION
    }
    
    private Margins margins = new Margins(0,0,0,0);    
    private Paddings paddings = Paddings.ZERO;
    
    private int rowCount;
    private int columnCount;
    
    private int horizontalSpacing = -1;
    private int verticalSpacing = -1;        
    private boolean hReversed;
    private boolean vReversed;
    private SizeDataState sizeDataState;
    private boolean needDistribute;
    private int recalculating;
    private boolean distributingGeometry;
    private boolean has_hfw;
    private boolean addVertical;
    private int hfw_width;
    private int hfw_height;
    private int hfw_minheight;    
    private int nextC;
    private int nextR;
    private WeakReference<HierarchicalGridLayout> parentLayoutRef;
    private final List<Cell> cells = new ArrayList<>();
    private List<SizeData> hfwData;
    private final List<SizeData> rowData = new ArrayList<>();
    private final List<SizeData> colData = new ArrayList<>();
    
    private final List<Integer> rStretch = new ArrayList<>();
    private final List<Integer> cStretch = new ArrayList<>();
    private final List<Integer> rMinHeights = new ArrayList<>();
    private final List<Integer> cMinWidths = new ArrayList<>();
    
    private final QSize temporarySize = new QSize(0,0);
       
    private Margins calcEffectiveMargins(){
        if (SystemTools.isOSX){
            int l = margins.left;
            int t = margins.top;
            int r = margins.right;
            int b = margins.bottom;
            
            int leftMost = Integer.MAX_VALUE;
            int topMost = Integer.MAX_VALUE;
            int rightMost = 0;
            int bottomMost = 0;

            QWidget w;
            QLayoutItemInterface item;
            for (Cell cell: cells) {
                w = cell.getWidget();
                if (w!=null) {
                    final boolean visualHReversed = hReversed != (w.layoutDirection() == Qt.LayoutDirection.RightToLeft);
                    final QRect lir = cell.getGeometry();
                    final QRect wr = w.geometry();
                    if (cell.getColumn() <= leftMost) {
                        if (cell.getColumn() < leftMost) {
                            // we found an item even closer to the margin, discard.
                            leftMost = cell.getColumn();
                            if (visualHReversed){
                                r = margins.right;
                            }else{
                                l = margins.left;
                            }
                        }
                        if (visualHReversed) {
                            r = Math.max(r, wr.right() - lir.right());
                        } else {
                            l = Math.max(l, lir.left() - wr.left());
                        }
                    }
                    if (cell.getRow() <= topMost) {
                        if (cell.getRow() < topMost) {
                            // we found an item even closer to the margin, discard.
                            topMost = cell.getRow();
                            if (vReversed){
                                b = margins.bottom;
                            }else{
                                t = margins.top;
                            }
                        }
                        if (vReversed){
                            b = Math.max(b, wr.bottom() - lir.bottom());
                        }else{
                            t = Math.max(t, lir.top() - wr.top());
                        }
                    }
                    if (cell.toCol(columnCount) >= rightMost) {
                        if (cell.toCol(columnCount) > rightMost) {
                            // we found an item even closer to the margin, discard.
                            rightMost = cell.toCol(columnCount);
                            if (visualHReversed){
                                l = margins.left;
                            } else{
                                r = margins.right;
                            }
                        }
                        if (visualHReversed) {
                            l = Math.max(l, lir.left() - wr.left());
                        } else {
                            r = Math.max(r, wr.right() - lir.right());
                        }

                    }
                    if (cell.toRow(rowCount) >= bottomMost) {
                        if (cell.toRow(rowCount) > bottomMost) {
                            // we found an item even closer to the margin, discard.
                            bottomMost = cell.toRow(rowCount);
                            if (vReversed){
                                t = margins.top;
                            } else {
                                b = margins.bottom;
                            }
                        }
                        if (vReversed){
                            t = Math.max(t, lir.top() - wr.top());
                        }else{
                            b = Math.max(b, wr.bottom() - lir.bottom());
                        }
                    }
                }
            }//for;
            return new Margins(l, t, r, b);
        }else{
            return margins;
        }        
    };
            
    /*
      Assumes that setupLayoutData() has been called, and that
      qGeomCalc() has filled in colData with appropriate values.
    */
    private void recalcHFW(final int w){
        /*
          Go through all children, using colData and heightForWidth()
          and put the results in hfwData.
        */
        if (hfwData==null)
            hfwData = new ArrayList<>(rowCount);
        setupHfwLayoutData();
        List<SizeData> rData = new ArrayList<>(hfwData);

        int h = 0;
        int mh = 0;
        for (int r = 0; r < rowCount; r++) {
            int spacing = rData.get(r).getSpacing();
            h += rData.get(r).getSizeHint() + spacing;
            mh += rData.get(r).getMinimumSize() + spacing;
        }

        hfw_width = w;
        hfw_height = Math.min(QLAYOUTSIZE_MAX, h);
        hfw_minheight = Math.min(QLAYOUTSIZE_MAX, mh);
    }    
    
    private int getHeightForWidth(final int w){
        setupLayoutData(null, distributingGeometry);
        if (!has_hfw)
            return -1;
        final Margins effectiveMargins = calcEffectiveMargins();

        int hMargins = effectiveMargins.left + effectiveMargins.right + paddings.summary;
        if (w - hMargins != hfw_width) {
            geomCalc(colData, 0, columnCount, 0, w - hMargins);
            recalcHFW(w - hMargins);
        }
        return hfw_height + effectiveMargins.top + effectiveMargins.bottom;
    }
    
    private int getMinimumHeightForWidth(final int w){
        getHeightForWidth(w);
        if (!has_hfw)
            return -1;
        final Margins effectiveMargins = calcEffectiveMargins();
        return hfw_minheight + effectiveMargins.top + effectiveMargins.bottom;
    }    
    
    final Dimension findSize(final SizeData.SizeType sizeType, final boolean distribution){
        
        setupLayoutData(null, distribution);

        int w = 0;
        int h = 0;

        for (int r = 0; r < rowCount; r++)
            h += rowData.get(r).getSize(sizeType) + rowData.get(r).getSpacing();
        for (int c = 0; c < columnCount; c++)
            w += colData.get(c).getSize(sizeType) + colData.get(c).getSpacing();

        w = Math.min(QLAYOUTSIZE_MAX, w);
        h = Math.min(QLAYOUTSIZE_MAX, h);

        return new Dimension(w, h);
    }
    
    private Qt.Orientations expandingDirectionsImpl(final boolean distribution){
        
        setupLayoutData(null, distribution);
        
        final Qt.Orientations result = new Qt.Orientations();

        for (int r = 0; r < rowCount; r++) {
            if (rowData.get(r).isExpansive()) {
                result.set(Qt.Orientation.Vertical);
                break;
            }
        }
        for (int c = 0; c < columnCount; c++) {
            if (colData.get(c).isExpansive()) {
                result.set(Qt.Orientation.Horizontal);
                break;
            }
        }
        return result;
    }  
    
    private void setSize(final int r, final int c){
        if (rowData.size() < r) {
            int newR = Math.max(r, rowCount);
            for (int i = rowCount; i < newR; i++) {
                rowData.add(new SizeData());
                rStretch.add(0);
                rMinHeights.add(0);
            }
        }
        if (colData.size() < c) {
            int newC = Math.max(c, columnCount);
            for (int i = columnCount; i < newC; i++) {
                colData.add(new SizeData());
                cStretch.add(0);
                cMinWidths.add(0);
            }
        }

        if (hfwData!=null && hfwData.size() < r) {
            hfwData.clear();
            hfwData = null;
            hfw_width = -1;
        }
        
        rowCount = r;
        columnCount = c;        
    }    
    
    private void setNextPosAfter(final int row, final int col){
        if (addVertical) {
            if (col > nextC || (col == nextC && row >= nextR)) {
                nextR = row + 1;
                nextC = col;
                if (nextR >= rowCount) {
                    nextR = 0;
                    nextC++;
                }
            }
        } else {
            if (row > nextR || (row == nextR && col >= nextC)) {
                nextR = row;
                nextC = col + 1;
                if (nextC >= columnCount) {
                    nextC = 0;
                    nextR++;
                }
            }
        }
    }    
    
    private void add(final Cell cell, final int row, final int col){
        expand(row + 1, col + 1);
        cell.setGeometry(row, col, row, col);
        cells.add(cell);
        setDirty();
        setNextPosAfter(row, col);
    }    
    
    private void add(final Cell cell, final int row1, final int row2, final int col1, final int col2){
        if (row2 >= 0 && row2 < row1){
            Logger.getLogger(HierarchicalGridLayout.class.getName()).warning("Multi-cell fromRow greater than toRow");
        }
        if (col2 >= 0 && col2 < col1){
            Logger.getLogger(HierarchicalGridLayout.class.getName()).warning("Multi-cell fromCol greater than toCol");
        }
        if (row1 == row2 && col1 == col2) {
            add(cell, row1, col1);
            return;
        }
        expand(row2 + 1, col2 + 1);
        cell.setGeometry(row1, col1, row2, col2);
        cells.add(cell);
        setDirty();
        setNextPosAfter(row2, col2<0 ? columnCount - 1 : col2);
    }    
    
    private void expand(final int rows, final int cols){
        setSize(Math.max(rows, rowCount), Math.max(cols, columnCount));
    }
    
    private void setDirty(){
        sizeDataState = SizeDataState.OBSOLETE;
        hfw_width = -1;
    }
    
    private boolean isDirty(){
        return sizeDataState==SizeDataState.OBSOLETE;
    }
    
    private void setupLayoutData(final List<LinkedSizeData> linkedColumnsData, final boolean distribution){
        
        if ((distribution && sizeDataState==SizeDataState.CALCULATED_FOR_DISTRIBUTION) 
            || (!distribution && sizeDataState==SizeDataState.CACLCULATED_FOR_SIZE_HINT)){
            return;
        }
        
        if (linkedColumnsData==null && recalculating==0){
            final HierarchicalGridLayout parentLayout = getParentLayout();
            if (parentLayout!=null && parentLayout.recalculating==0){
                recalculating++;
                try{
                    parentLayout.setDirty();
                    parentLayout.setupLayoutData(null, distribution);//delegate to parent
                }finally{
                    recalculating--;
                }
                return;
            }
        }
            
        recalculating++;        
                
        try{            

            final int hSpacing = horizontalSpacing();
            final int vSpacing = verticalSpacing();

            has_hfw = false;

            for (int i = 0; i < rowCount; i++) {
                rowData.get(i).init(rStretch.get(i), rMinHeights.get(i), rStretch.get(i)!=0 ? QLAYOUTSIZE_MAX : rMinHeights.get(i), null);
            }
            for (int i = 0; i < columnCount; i++) {
                final LinkedSizeData linkedData = linkedColumnsData == null || linkedColumnsData.size()<=i ? null : linkedColumnsData.get(i);
                colData.get(i).init(cStretch.get(i), cMinWidths.get(i), cStretch.get(i)!=0 ? QLAYOUTSIZE_MAX : cMinWidths.get(i), linkedData);
            }
            
            if (parentLayoutRef==null){                
                recalcPaddings();
            }

            try{
                final int n = cells.size();
                final List<SizeInfo> sizes = new ArrayList<>(n);

                boolean has_multi = false;

                /*
                    Grid of items. We use it to determine which items are
                    adjacent to which and compute the spacings correctly.
                */
                final List<Cell> grid = new ArrayList<>(rowCount * columnCount);
                for (int i=0,count=rowCount*columnCount; i<count; i++){
                    grid.add(null);
                }

                /*
                    Initialize 'sizes' and 'grid' data structures, and insert
                    non-spanning items to our row and column data structures.
                */
                for (Cell cell: cells) {
                    final SizeInfo cellSizeInfo = new SizeInfo(cell);
                    sizes.add(cellSizeInfo);

                    if (cell.hasHeightForWidth()){
                        has_hfw = true;
                    }

                    if (cell.getRow() == cell.toRow(rowCount)) {
                        addData(cell, cellSizeInfo, true, false);
                    } else {
                        initEmptyMultiBox(rowData, cell.getRow(), cell.toRow(rowCount));
                        has_multi = true;
                    }

                    if (cell.getColumn() == cell.toCol(columnCount)) {
                        addData(cell, cellSizeInfo, false, true);
                    } else {
                        initEmptyMultiBox(colData, cell.getColumn(), cell.toCol(columnCount));
                        has_multi = true;
                    }

                    for (int r = cell.getRow(); r <= cell.toRow(rowCount); ++r) {
                        for (int c = cell.getColumn(); c <= cell.toCol(columnCount); ++c) {
                            grid.set(gridIndex(r, c, columnCount), cell);
                        }
                    }            
                }                                

                setupSpacings(colData, 0, columnCount, grid, hSpacing, Qt.Orientation.Horizontal);
                setupSpacings(rowData, 0, rowCount, grid, vSpacing, Qt.Orientation.Vertical);                                

                setupChildLayoutData(distribution);
                /*
                    Insert multicell items to our row and column data structures.
                    This must be done after the non-spanning items to obtain a
                    better distribution in distributeMultiBox().
                */
                if (has_multi) {
                    Cell cell;
                    int col, toCol, row, toRow;
                    for (int i = 0; i < n; ++i) {
                        cell = cells.get(i);
                        row = cell.getRow();
                        col = cell.getColumn();
                        toRow = cell.toRow(rowCount);
                        toCol = cell.toCol(columnCount);
                        if (row != toRow){
                            distributeMultiBox(rowData, 
                                                        row, 
                                                        toRow, 
                                                        sizes.get(i).minSize.height,
                                                        sizes.get(i).sizeHint.height,
                                                        rStretch, 
                                                        cell.vStretch());
                        }
                        if (col != toCol){
                            if (!colData.get(col).isExpansive() 
                                && cell.expandingDirections().isSet(Qt.Orientation.Horizontal)){
                                for (int j=col+1; j<=toCol; j++){
                                    if (colData.get(j).isExpansive()){
                                        break;
                                    }
                                    if (j==toCol){
                                        colData.get(j).makeExpansive();
                                    }
                                }
                            }
                            distributeMultiBox(colData, 
                                                        col, 
                                                        toCol,
                                                        sizes.get(i).minSize.width,
                                                        sizes.get(i).sizeHint.width,
                                                        cStretch, 
                                                        cell.hStretch());
                        }
                    }
                }                
                                
                for (int i = 0; i < rowCount; i++){
                    rowData.get(i).updateExpansive();
                }
                
                for (int i = 0; i < columnCount; i++){
                    colData.get(i).updateExpansive();
                } 
            }finally{
                for (SizeData data: colData){
                    data.unlink();
                }
            }
        }finally{
            recalculating--;
        }
        
        margins = new Margins(getContentsMargins());
        
        sizeDataState = distribution ? SizeDataState.CALCULATED_FOR_DISTRIBUTION : SizeDataState.CACLCULATED_FOR_SIZE_HINT;
        needDistribute = true;
    }    
    
    private Paddings recalcPaddings(){
        
        paddings = Paddings.ZERO;
        
        for (SizeData column: colData){
            column.clearPadding();
        }
        //find max paddings
        for (Cell cell: cells){
            final HierarchicalGridLayout childLayout = cell.getChildLayout();            
            if (childLayout!=null && !cell.isEmpty() && !childLayout.cells.isEmpty()){
                final int startColumn = cell.getColumn();
                final int endColumn = cell.toCol(columnCount);
                
                final Paddings cellPaddings = 
                    childLayout.recalcPaddings().addMargins(childLayout.calcEffectiveMargins()).addMargins(getContentMargins(cell));
                                
                if (startColumn==0){
                    paddings = paddings.getLeftExpanded(cellPaddings.left);
                }else{
                    colData.get(startColumn - 1).expandPadding(cellPaddings.left);
                }
                
                if (endColumn==columnCount-1){
                    paddings = paddings.getRightExpanded(cellPaddings.right);
                }else{
                    colData.get(endColumn).expandPadding(cellPaddings.right);
                }
                
            }
        }
        //distribute max paddings
        distributeParentPaddings(paddings, false, false);
        return paddings;
    }
    
    private void distributeParentPaddings(final Paddings parentPaddings, final boolean expandLeft, final boolean expandRight){
        if (expandLeft){
            paddings = paddings.getLeftExpanded(parentPaddings.left);
        }
        if (expandRight){
            paddings = paddings.getRightExpanded(parentPaddings.right);
        }
        for (Cell cell: cells){
            final HierarchicalGridLayout childLayout = cell.getChildLayout();            
            if (childLayout!=null && !cell.isEmpty() && !childLayout.cells.isEmpty()){
                final int startColumn = cell.getColumn();
                final int endColumn = cell.toCol(columnCount);
                
                final Paddings cellPaddings = 
                    paddings.removeMargins(childLayout.calcEffectiveMargins()).removeMargins(getContentMargins(cell));
                childLayout.distributeParentPaddings(cellPaddings, startColumn==0, endColumn==columnCount-1);
            }
        }
    }
        
    private static QMargins getContentMargins(final Cell cell){
        final QWidget widget = cell.getWidget();
        if (widget!=null){
            return widget.contentsMargins();
        }
        return null;
    }
    
    private void setupChildLayoutData(final boolean exact){
        final Map<Integer, LinkedSizeData> linkedSizeData = new HashMap<>();
        for (Cell cell: cells){
            final HierarchicalGridLayout childLayout = cell.getChildLayout();            
            if (childLayout!=null && !cell.isEmpty() && !childLayout.cells.isEmpty()){
                final int startColumn = cell.getColumn();
                final int cellSpan = cell.toCol(columnCount) - startColumn + 1;
                final List<LinkedSizeData> mappedColunmns;
                if (cellSpan==childLayout.getNotEmptyColumnCount()){
                    mappedColunmns = new ArrayList<>();
                    LinkedSizeData mappedColumn;
                    for (int i=startColumn, count=Math.min(startColumn+cellSpan, columnCount()); i<count; i++){
                        mappedColumn = linkedSizeData.get(i);
                        if (mappedColumn==null){
                            mappedColumn = new LinkedSizeData(colData.get(i));
                            linkedSizeData.put(i, mappedColumn);
                        }
                        mappedColunmns.add(mappedColumn);
                    }
                }else{
                    LinkedSizeData mappedColumn = linkedSizeData.get(startColumn);
                    if (mappedColumn==null){
                        mappedColumn = new LinkedSizeData(colData.get(startColumn));
                        linkedSizeData.put(startColumn, mappedColumn);
                    }
                    mappedColunmns = Collections.singletonList(mappedColumn);
                }
                childLayout.setDirty();
                childLayout.setupLayoutData(mappedColunmns, exact);
                for (int i=0,count=Math.min(mappedColunmns.size(),childLayout.colData.size()); i<count; i++){
                    mappedColunmns.get(i).linkSizeData(childLayout.colData.get(i));
                }
            }
        }
        if (exact){
            SizeData columnData;
            for (Map.Entry<Integer,LinkedSizeData> entry: linkedSizeData.entrySet()){
                columnData = colData.get(entry.getKey());
                entry.getValue().unlinkSizeData(columnData);
                columnData.link(entry.getValue());
            }
        }
    }
    
    private void setupSpacings(final List<SizeData> chain,
                                             final int start,
                                             final int end,
                                             final List<Cell> grid, 
                                             final int fixedSpacing,
                                             final Qt.Orientation orientation){
        final int numRows = end;       // or columns if orientation is horizontal        
        final int startRow = start;
        final int numColumns = orientation == Qt.Orientation.Horizontal ? rowCount : columnCount;

        final QStyle style;
        if (fixedSpacing < 0) {
            final QWidget parentWidget = parentWidget();
            style = parentWidget==null ? null : parentWidget.style();
        }else{
            style = null;
        }

        Cell cell;
        for (int c = 0; c < numColumns; ++c) {
            Cell previousCell = null;
            int previousRow = -1;       // previous *non-empty* row

            for (int r = startRow; r < numRows; ++r) {
                if (chain.get(r).isEmpty()){
                    continue;
                }

                cell = getCell(grid, r, c, columnCount, orientation);
                if (previousRow != -1 && (cell==null || previousCell != cell)) {
                    int spacing = fixedSpacing;
                    if (spacing < 0) {
                        QSizePolicy.ControlTypes controlTypes1 = DEFAULT_CONTROL_TYPES;
                        QSizePolicy.ControlTypes controlTypes2 = DEFAULT_CONTROL_TYPES;
                        if (previousCell!=null)
                            controlTypes1 = previousCell.getControlTypes();
                        if (cell!=null)
                            controlTypes2 = cell.getControlTypes();

                        if ((orientation == Qt.Orientation.Horizontal && hReversed)
                             || (orientation == Qt.Orientation.Vertical && vReversed)){
                            QSizePolicy.ControlTypes types = controlTypes1;
                            controlTypes2 = controlTypes1;
                            controlTypes1 = types;
                        }

                        if (style!=null){
                            spacing = 
                                style.combinedLayoutSpacing(controlTypes1, controlTypes2, orientation, null, parentWidget());
                        }
                    } else {
                        if (orientation == Qt.Orientation.Vertical) {
                            Cell sibling = vReversed ? previousCell : cell;
                            if (sibling!=null) {
                                final QWidget widget = sibling.getWidget();
                                if (widget!=null){
                                    spacing = Math.max( spacing, sibling.getGeometry().top() - widget.geometry().top() );
                                }
                            }
                        }
                    }

                    chain.get(previousRow).expandSpacing(spacing);
                }

                previousCell = cell;
                previousRow = r;
            }
        }        
    }
    
    /*
    Similar to setupLayoutData(), but uses heightForWidth(colData)
    instead of sizeHint(). Assumes that setupLayoutData() and
    qGeomCalc(colData) has been called.
    */
    private void setupHfwLayoutData(){
        List<SizeData> rData = hfwData;
        for (int i = 0; i < rowCount; i++) {
            rData.set(i, rowData.get(i));
            rData.get(i).setSizeHint(rMinHeights.get(i));
            rData.get(i).setMinimumSize(rMinHeights.get(i));
        }

        Cell cell;
        for (int pass = 0; pass < 2; ++pass) {
            for (int i = 0; i < cells.size(); ++i) {
                cell = cells.get(i);
                int r1 = cell.getRow();
                int c1 = cell.getColumn();
                int r2 = cell.toRow(rowCount);
                int c2 = cell.toCol(columnCount);
                int w = colData.get(c2).getPos() + colData.get(c2).getSize() - colData.get(c1).getPos();

                if (r1 == r2) {
                    if (pass == 0)
                        addHfwData(cell, w);
                } else {
                    if (pass == 0) {
                        initEmptyMultiBox(rData, r1, r2);
                    } else {
                        final Dimension hint = cell.sizeHint();
                        final Dimension min = cell.minimumSize();
                        if (cell.hasHeightForWidth()) {
                            int hfwh = cell.heightForWidth(w);
                            if (hfwh > hint.height)
                                hint.height = hfwh;
                            if (hfwh > min.height)
                                min.height = hfwh;
                        }
                        distributeMultiBox(rData, r1, r2, min.height, hint.height, rStretch, cell.vStretch());
                    }
                }
            }
        }
        for (int i = 0; i < rowCount; i++){
            rData.get(i).updateExpansive();
        }
    }

    private void addData(final Cell cell, final SizeInfo sizes, final boolean r, final boolean c){                
        final QWidget widget = cell.getWidget();

        if (cell.isEmpty() && widget!=null){
            return;
        }

        if (c) {
            final SizeData data = colData.get(cell.getColumn());
            if (cStretch.get(cell.getColumn())!=0){
                data.expandStretch(cell.hStretch());
            }
            data.expandSizeHint(sizes.sizeHint.width);
            data.expandMinimumSize(sizes.minSize.width);
            data.expandToCell(sizes.maxSize.width, cell.expandingDirections().isSet(Qt.Orientation.Horizontal), cell.isEmpty());
        }
        if (r) {
            final SizeData data = rowData.get(cell.getRow());
            if (rStretch.get(cell.getRow())!=0){
                data.expandStretch(cell.hStretch());
            }
            data.expandSizeHint(sizes.sizeHint.height);
            data.expandMinimumSize(sizes.minSize.height);
            data.expandToCell(sizes.maxSize.height, cell.expandingDirections().isSet(Qt.Orientation.Vertical), cell.isEmpty());
        }
    }
    
    private void addHfwData(final Cell cell, final int width){
        final List<SizeData> rData = hfwData;
        if (cell.hasHeightForWidth()) {
            final int hint = cell.heightForWidth(width);
            rData.get(cell.getRow()).expandSizeHint(hint);
            rData.get(cell.getRow()).expandMinimumSize(hint);
        } else {
            final Dimension hint = cell.sizeHint();
            final Dimension minS = cell.minimumSize();
            rData.get(cell.getRow()).expandSizeHint(hint.height);
            rData.get(cell.getRow()).expandMinimumSize(minS.height);
        }
    }
    
    private void distribute(final Rectangle r){
        boolean visualHReversed = hReversed;
        final QWidget parent = parentWidget();
        if (parent!=null && parent.isRightToLeft())
            visualHReversed = !visualHReversed;               

        setupLayoutData(null,true);

        final Margins effectiveMargins = calcEffectiveMargins();         
        
        int rleft = r.x + effectiveMargins.left + paddings.left;
        int rtop = r.y + effectiveMargins.top;
        int rright = r.x + r.width - 1  - effectiveMargins.right - paddings.right;
        int rbottom = r.y + r.height - 1 - effectiveMargins.bottom;
        int rwidth = rright - rleft;
        int rheight = rbottom - rtop; 
        
        geomCalc(colData, 0, columnCount, rleft, rwidth);
                
        final List<SizeData> rData;
        if (has_hfw) {
            recalcHFW(rwidth);
            geomCalc(hfwData, 0, rowCount, rtop, rheight);
            rData = hfwData;
        } else {
            geomCalc(rowData, 0, rowCount, rtop, rheight);
            rData = rowData;
        }
        
        final QRect rect = this.geometry();

        final boolean reverse = ((rbottom > rect.bottom()) || (rbottom == rect.bottom()
                                                         && ((rright > rect.right()) != visualHReversed)));
        final int n = cells.size();
        Cell cell;
        for (int i = 0; i < n; ++i) {
            cell = cells.get(reverse ? n-i-1 : i);            
            final int r2 = cell.toRow(rowCount);
            final int c1 = cell.getColumn();
            final int c2 = cell.toCol(columnCount);

            int x = colData.get(c1).getPos();
            int y = rData.get(cell.getRow()).getPos();
            final int x2p = colData.get(c2).getPos() + colData.get(c2).getSize(); // x2+1
            final int y2p = rData.get(r2).getPos() + rData.get(r2).getSize();    // y2+1
            int w = x2p - x;
            final int h = y2p - y;
            
            if (cell.getChildLayout()!=null){
                final int leftPadding = c1==0 ? paddings.left : colData.get(c1-1).getPadding();
                final int rightPadding = c2==columnCount-1 ? paddings.right : colData.get(c2).getPadding();
                if (leftPadding>0){
                    x-=leftPadding;
                    w+=leftPadding;
                }
                if (rightPadding>0){
                    w+=rightPadding;
                }
            }

            if (visualHReversed){ 
                x = rleft + rright - x - w + 1;
            }
            
            if (vReversed){
                y = rtop + rbottom - y - h + 1;
            }
            final Rectangle newGeometry = new Rectangle(x, y, w, h);
            final HierarchicalGridLayout childLayout = cell.getChildLayout();
            if (childLayout!=null){
                childLayout.distributingGeometry = true;
            }
            try{
                if (cell.setGeometry(newGeometry)
                    && childLayout!=null
                    && !childLayout.isEmpty() 
                    && childLayout.sizeDataState==SizeDataState.CALCULATED_FOR_DISTRIBUTION 
                    && childLayout.needDistribute){
                    final QMargins contentMargins = getContentMargins(cell);
                    Rectangle cellRect;
                    if (contentMargins==null){
                        cellRect = new Rectangle(0, 0, w, h);
                    }else{
                        cellRect = new Rectangle(0, 0, w - contentMargins.left() - contentMargins.right(), h - contentMargins.top() - contentMargins.bottom() + 1);
                    }
                    if (childLayout.alignment().value()>0){
                        cellRect = childLayout.alignmentRect(cellRect);
                    }
                    childLayout.distribute(cellRect);
                }
            }finally{
                if (childLayout!=null){
                    childLayout.distributingGeometry = false;
                }
            }
        }
        needDistribute = false;
    }
        
    private int smartSpacing(final QStyle.PixelMetric pm){
        final QObject parent = this.parent();
        if (parent instanceof QWidget) {
            return ((QWidget)parent).style().pixelMetric(pm, null, (QWidget)parent);
        } else {
            return -1;
        }
    }
    
    private HierarchicalGridLayout getParentLayout(){        
        final HierarchicalGridLayout parentLayout = parentLayoutRef==null ? null : parentLayoutRef.get();
        if (parentLayout==null){
            return null;
        }else{
            for (Cell cell: parentLayout.cells){
                if (cell.getChildLayout()==this){
                    return parentLayout;
                }
            }
            parentLayoutRef = null;
            return null;
        }
    }
    
    private Cell getCellAtPosition(final int row, final int column){
        for (Cell cell: cells) {
            if (row >= cell.getRow() && row <= cell.toRow(rowCount)
                && column >= cell.getColumn() && column <= cell.toCol(columnCount)) {
                return cell;
            }
        }
        return null;        
    }
    
    private static void distributeMultiBox(final List<SizeData> chain,
                                                            final int start, 
                                                            final int end, 
                                                            final int minSize,
                                                            final int sizeHint, 
                                                            final List<Integer> stretchArray, 
                                                            final int stretch){  
        int w = 0;
        int wh = 0;
        int max = 0;

        SizeData data;
        for (int i = start; i <= end; i++) {
            data = chain.get(i);
            w += data.getMinimumSize();
            wh += data.getSizeHint();
            max += data.getMaximumSize();
            
            if (stretchArray.get(i) == 0){
                data.expandStretch(stretch);
            }

            if (i != end) {
                final int spacing = data.getSpacing();
                w += spacing;
                wh += spacing;
                max += spacing;
            }
        }

        if (max < minSize) { // implies w < minSize
            /*
              We must increase the maximum size of at least one of the
              items. qGeomCalc() will put the extra space in between the
              items. We must recover that extra space and put it
              somewhere. It does not really matter where, since the user
              can always specify stretch factors and avoid this code.
            */
            geomCalc(chain, start, end - start + 1, 0, minSize);
            int pos = 0;            
            for (int i = start; i <= end; i++) {
                data = chain.get(i);
                int nextPos = (i == end) ? minSize : chain.get(i + 1).getPos();
                int realSize = nextPos - pos;
                if (i != end)
                    realSize -= data.getSpacing();
                data.expandMinimumSize(realSize);
                data.expandMaximumSize(data.getMinimumSize());
                pos = nextPos;
            }
        } else if (w < minSize) {
            geomCalc(chain, start, end - start + 1, 0, minSize);
            for (int i = start; i <= end; i++) {
                data = chain.get(i);
                data.expandMinimumSize(data.getSize());
            }
        }

        if (wh < sizeHint) {
            geomCalc(chain, start, end - start + 1, 0, sizeHint);
            for (int i = start; i <= end; i++) {
                data = chain.get(i);
                data.expandSizeHint(data.getSize());
            }
        }        
        
    }
    
    private static void geomCalc(final List<SizeData> chain, final int start, final int count, final int pos, final int space){
        geomCalc(chain, start, count, pos, space, -1);
    }

    /*
      This is the main workhorse of the QGridLayout. It portions out
      available space to the chain's children.

      The calculation is done in fixed point: "fixed" variables are
      scaled by a factor of 256.

      If the layout runs "backwards" (i.e. RightToLeft or Up) the layout
      is computed mirror-reversed, and it's the caller's responsibility
      do reverse the values before use.

      chain contains input and output parameters describing the geometry.
      count is the count of items in the chain; pos and space give the
      interval (relative to parentWidget topLeft).
    */    
    private static void geomCalc(final List<SizeData> chain, final int start, final int count, final int pos, final int space, int spacer){
        
        int cHint = 0;
        int cMin = 0;
        int cMax = 0;
        int sumStretch = 0;
        int sumSpacing = 0;
        int expandingCount = 0;

        boolean allEmptyNonstretch = true;
        int pendingSpacing = -1;
        int spacerCount = 0;        

        SizeData data;
        
        for (int i = start; i < start + count; i++) {
            data = chain.get(i);
            data.setDone(false);
            cHint += data.smartSizeHint();
            cMin += data.getMinimumSize();
            cMax += data.getMaximumSize();
            sumStretch += data.getStretch();
            if (!data.isEmpty()) {
                /*
                    Using pendingSpacing, we ensure that the spacing for the last
                    (non-empty) item is ignored.
                */
                if (pendingSpacing >= 0) {
                    sumSpacing += pendingSpacing;
                    ++spacerCount;
                }
                pendingSpacing = data.effectiveSpacer(spacer);
            }
            if (data.isExpansive())
                expandingCount++;
            allEmptyNonstretch = allEmptyNonstretch && data.isEmpty() && !data.isExpansive() && data.getStretch() <= 0;
        }
        
        int extraspace = 0;
        if (space < cMin + sumSpacing) {
            /*
              Less space than minimumSize; take from the biggest first
            */

            int minSize = cMin + sumSpacing;

            // shrink the spacers proportionally
            if (spacer >= 0) {
                spacer = minSize > 0 ? spacer * space / minSize : 0;
                sumSpacing = spacer * spacerCount;
            }

            List<Integer> list = new ArrayList<>();

            for (int i = start; i < start + count; i++)
                list.add(chain.get(i).getMinimumSize());

            Collections.sort(list);

            int space_left = space - sumSpacing;

            int sum = 0;
            int idx = 0;
            int space_used=0;
            int current = 0;
            while (idx < count && space_used < space_left) {
                current = list.get(idx);
                space_used = sum + current * (count - idx);
                sum += current;
                ++idx;
            }
            --idx;
            int deficit = space_used - space_left;

            int items = count - idx;
            /*
             * If we truncate all items to "current", we would get "deficit" too many pixels. Therefore, we have to remove
             * deficit/items from each item bigger than maxval. The actual value to remove is deficitPerItem + remainder/items
             * "rest" is the accumulated error from using integer arithmetic.
            */
            int deficitPerItem = deficit/items;
            int remainder = deficit % items;
            int maxval = current - deficitPerItem;

            int rest = 0;
            for (int i = start; i < start + count; i++) {
                int maxv = maxval;
                rest += remainder;
                if (rest >= items) {
                    maxv--;
                    rest-=items;
                }
                data = chain.get(i);
                data.setSize(Math.min(data.getMinimumSize(), maxv));
                data.setDone(true);
            }
        } else if (space < cHint + sumSpacing) {
            /*
              Less space than smartSizeHint(), but more than minimumSize.
              Currently take space equally from each, as in Qt 2.x.
              Commented-out lines will give more space to stretchier
              items.
            */
            int n = count;
            int space_left = space - sumSpacing;
            int overdraft = cHint - space_left;

            // first give to the fixed ones:
            for (int i = start; i < start + count; i++) {
                data = chain.get(i);
                if (!data.isDone()
                    && data.getMinimumSize() >= data.smartSizeHint()) {
                    data.setSize(data.smartSizeHint());
                    data.setDone(true);
                    space_left -= data.smartSizeHint();
                    // sumStretch -= data->stretch;
                    n--;
                }
            }
            boolean finished = n==0;            
            BigDecimal fp_n = new BigDecimal(n);
            while (!finished) {
                finished = true;
                BigDecimal fp_over = new BigDecimal(overdraft);
                BigDecimal fp_w = BigDecimal.ZERO;
                BigDecimal roundedW;

                for (int i = start; i < start+count; i++) {
                    data = chain.get(i);
                    if (data.isDone()){
                        continue;
                    }
                    // if (sumStretch <= 0)
                    fp_w = fp_w.add(fp_over.divide(fp_n, RoundingMode.HALF_EVEN));
                    // else
                    //    fp_w += (fp_over * data->stretch) / sumStretch;
                    roundedW = fp_w.round(INTEGER_CONTEXT);
                    int w = roundedW.intValueExact();                    
                    data.setSize(data.smartSizeHint() - w);
                    fp_w = fp_w.subtract(roundedW); // give the difference to the next
                    if (data.getSize() < data.getMinimumSize()) {
                        data.setDone(true);
                        data.setSize(data.getMinimumSize());
                        finished = false;
                        overdraft -= data.smartSizeHint() - data.getMinimumSize();
                        fp_n = fp_n.subtract(BigDecimal.ONE);
                        break;
                    }
                }
            }
        } else { // extra space
            int n = count;
            int space_left = space - sumSpacing;
            // first give to the fixed ones, and handle non-expansiveness
            for (int i = start; i < start + count; i++) {
                data = chain.get(i);
                if (!data.isDone()
                    && (data.getMaximumSize() <= data.smartSizeHint()
                        || (!allEmptyNonstretch && data.isEmpty() &&
                            !data.isExpansive() && data.getStretch() == 0))) {
                    data.setSize(data.smartSizeHint());
                    data.setDone(true);
                    space_left -= data.getSize();
                    sumStretch -= data.getStretch();
                     if (data.isExpansive())
                         expandingCount--;
                    n--;
                }
            }
            extraspace = space_left;

            /*
              Do a trial distribution and calculate how much it is off.
              If there are more deficit pixels than surplus pixels, give
              the minimum size items what they need, and repeat.
              Otherwise give to the maximum size items, and repeat.

              Paul Olav Tvete has a wonderful mathematical proof of the
              correctness of this principle, but unfortunately this
              comment is too small to contain it.
            */
            int surplus, deficit;
            BigDecimal fp_n = new BigDecimal(n);
            do {
                surplus = deficit = 0;
                final BigDecimal fp_space = new BigDecimal(space_left);
                final BigDecimal fp_sumStretch = new BigDecimal(sumStretch);            
                final BigDecimal fp_expandingCount = new BigDecimal(expandingCount);                
                BigDecimal fp_w = BigDecimal.ZERO;
                BigDecimal rounded_w;
                
                for (int i = start; i < start + count; i++) {
                    data = chain.get(i);
                    if (data.isDone())
                        continue;
                    extraspace = 0;
                    if (sumStretch > 0) {
                        fp_w = fp_w.add( fp_space.multiply(new BigDecimal(data.getStretch())).divide(fp_sumStretch, RoundingMode.HALF_EVEN) );
                    } else if (expandingCount > 0) {
                        if (data.isExpansive()){
                            fp_w = fp_w.add(fp_space.divide(fp_expandingCount, RoundingMode.HALF_EVEN));
                        }                        
                    } else {
                        fp_w = fp_w.add(fp_space .divide(fp_n, RoundingMode.HALF_EVEN));
                    }
                    rounded_w = fp_w.round(INTEGER_CONTEXT);
                    int w = rounded_w.intValueExact();
                    data.setSize(w);
                    fp_w = fp_w.subtract(rounded_w); // give the difference to the next
                    if (w < data.smartSizeHint()) {
                        deficit +=  data.smartSizeHint() - w;
                    } else if (w > data.getMaximumSize()) {
                        surplus += w - data.getMaximumSize();
                    }
                }
                if (deficit > 0 && surplus <= deficit) {
                    // give to the ones that have too little
                    for (int i = start; i < start+count; i++) {
                        data = chain.get(i);
                        if (!data.isDone() && data.getSize() < data.smartSizeHint()) {
                            data.setSize(data.smartSizeHint());
                            data.setDone(true);
                            space_left -= data.smartSizeHint();
                            sumStretch -= data.getStretch();                            
                            if (data.isExpansive()){
                                expandingCount--;                                
                            }
                            fp_n = fp_n.subtract(BigDecimal.ONE);
                        }
                    }
                }
                if (surplus > 0 && surplus >= deficit) {
                    // take from the ones that have too much
                    for (int i = start; i < start + count; i++) {
                        data = chain.get(i);
                        if (!data.isDone() && data.getSize() > data.getMaximumSize()) {
                            data.setSize(data.getMaximumSize());
                            data.setDone(true);
                            space_left -= data.getMaximumSize();
                            sumStretch -= data.getStretch();
                            if (data.isExpansive()){
                                expandingCount--;                                
                            }
                            fp_n = fp_n.subtract(BigDecimal.ONE);
                        }
                    }                    
                }                
            } while (fp_n.compareTo(BigDecimal.ZERO) > 0 && surplus != deficit);
            if (fp_n.compareTo(BigDecimal.ZERO) == 0)
                extraspace = space_left;
        }

        /*
          As a last resort, we distribute the unwanted space equally
          among the spacers (counting the start and end of the chain). We
          could, but don't, attempt a sub-pixel allocation of the extra
          space.
        */
        int extra = extraspace / (spacerCount + 2);
        int p = pos + extra;
        for (int i = start; i < start+count; i++) {
            data = chain.get(i);
            data.setPos(p);
            p += data.getSize();
            if (!data.isEmpty())
                p += data.effectiveSpacer(spacer) + extra;
        }
    }            

    private static void initEmptyMultiBox(final List<SizeData> chain, final int start, final int end){
        SizeData data;
        for (int i = start; i <= end; i++) {
            data = chain.get(i);
            if (data.isEmpty() && data.getMaximumSize()== 0) // truly empty box
                data.setMaximumSize(QWIDGETSIZE_MAX);
            data.setEmpty(false);
        }
    }    
    
    private static Cell getCell(final List<Cell> grid, final int r, final int c, final int cc, final Qt.Orientation orientation){
        return grid.get(gridIndex(r, c, cc, orientation));
    }
    
    private static int gridIndex(final int r, final int c, final int cc){
        return gridIndex(r, c, cc, Qt.Orientation.Vertical);
    }
    
    private static int gridIndex(final int r, final int c, final int cc, final Qt.Orientation orientation){
        if (orientation == Qt.Orientation.Horizontal){
            return (c * cc) + r;
        }else{
            return (r * cc) + c;
        }
    }    
    
    private static String getPrintName(final QObject object){
        if (object.objectName()==null || object.objectName().isEmpty()){
            return object.getClass().getName();
        }else{
            return object.getClass().getName()+"/"+object.objectName();
        }
    }
    
    private boolean checkWidget(final QWidget w){        
        if (w==null) {            
            final String messageTemplate = "Cannot add null widget to %1$s";
            Logger.getLogger(getClass().getName()).warning(String.format(messageTemplate, getPrintName(this)));
            return false;
        }else if (w.nativeId()==0){
            final String messageTemplate = "Cannot add disposed widget %1$s to %2$s";
            Logger.getLogger(getClass().getName()).warning(String.format(messageTemplate, w.getClass().getName(), getPrintName(this)));
            return false;            
        }
        return true;
    }  
    
    private static Dimension qSize2Dimension(final QSize size){
        return new Dimension(size.width(), size.height());
    }
    
    private static Rectangle qRect2Rectangle(final QRect rect){
        return new Rectangle(rect.x(), rect.y(), rect.width(), rect.height());
    }    
    
    private QSize writeTemporarySize(final Dimension size){
        temporarySize.setWidth(size.width);
        temporarySize.setHeight(size.height);
        return temporarySize;
    }
        
    /**
     *             Constructors
     * 
     **/    
    public HierarchicalGridLayout() {
        super();
        expand(1, 1);
    }
    
    public HierarchicalGridLayout(final QWidget parent) {
        super(parent);
        expand(1, 1);
    }
    
    /**
     *             Public
     * 
     **/    
    
    public void setDefaultPositioning(final int n, final Qt.Orientation orient){
        if (orient == Qt.Orientation.Horizontal) {
            expand(1, n);
            addVertical = false;
        } else {
           expand(n,1);
            addVertical = true;
        }      
    }

    public void setHorizontalSpacing(final int spacing){
        horizontalSpacing = spacing;
        invalidate();
    }

    public int horizontalSpacing(){
        return horizontalSpacing>=0 ? horizontalSpacing : smartSpacing(QStyle.PixelMetric.PM_LayoutHorizontalSpacing);
    }    
    
    public void setVerticalSpacing(final int spacing){
        verticalSpacing = spacing;
        invalidate();
    }

    public int verticalSpacing(){
        return verticalSpacing>=0 ? verticalSpacing : smartSpacing(QStyle.PixelMetric.PM_LayoutVerticalSpacing);
    }    
    
    public void setSpacing(final int spacing){
        horizontalSpacing = spacing;
        verticalSpacing = spacing;
        invalidate();
    }

    public int spacing(){    
        int hSpacing = horizontalSpacing();
        return hSpacing == verticalSpacing() ? hSpacing : -1;
    }
    
    public int rowCount(){
        return rowCount;
    }

    public int columnCount(){
        return columnCount;
    }    
    
    private int getNotEmptyColumnCount(){
        int count = 0;
        for (int column=colData.size()-1; column>=0; column--){
            if (!colData.get(column).isEmpty() || columnMinWidth(column)>0){
                count++;
            }
        }
        return count;
    }
    
    private int columnMinWidth(final int column){
        return column<cMinWidths.size() ? cMinWidths.get(column) : 0;
    }
    
    public QLayoutItemInterface itemAtPosition(final int row, final int column){
        final Cell cell = getCellAtPosition(row, column);
        return cell==null ? null : cell.item();
    }
    
    public ItemPosition getItemPosition(final int index){
        if (index < cells.size()) {
            Cell cell =  cells.get(index);
            final int row = cell.getRow();
            final int col = cell.getColumn();
            final int toRow = cell.toRow(rowCount);
            final int toCol = cell.toCol(columnCount);
            return new ItemPosition(row, col, toRow - row + 1,  toCol - col + 1);
        } 
        
        return new ItemPosition(-1, -1, -1, -1);
    }        
    
    public QRect cellRect(final int row, final int col){

        if (row < 0 || row >= rowCount || col < 0 || col >= columnCount)
            return new QRect();

        final List<SizeData> rData = has_hfw && hfwData!=null ? hfwData : rowData;
        return new QRect(colData.get(col).getPos(), rData.get(row).getPos(),
                     colData.get(col).getSize(), rData.get(row).getSize());
    }        
        
    public void addItem(final QLayoutItemInterface item, final int row, final int column, final int rowSpan, final int columnSpan, final Qt.Alignment alignment){
        Cell cell = new Cell(item);
        if (alignment!=null){
            cell.setAlignment(alignment);
        }
        add(cell, row, (rowSpan < 0) ? -1 : row + rowSpan - 1, column, (columnSpan < 0) ? -1 : column + columnSpan - 1);
        invalidate();
    }
    
    public void addItem(final QLayoutItemInterface item, final int row, final int column, final int rowSpan, final int columnSpan){
        addItem(item, row, column, rowSpan, columnSpan,null);
    }
    
    public void addItem(final QLayoutItemInterface item, final int row, final int column, final int rowSpan){
        addItem(item, row, column, rowSpan, 1, null);
    }
    
    public void addItem(final QLayoutItemInterface item, final int row, final int column){
        addItem(item, row, column, 1, 1, null);
    }
        
    public void addWidget(final QWidget widget, final int row, final int column, final Qt.Alignment alignment){
        if (!checkWidget(widget)){
            return;
        }
        if (row < 0 || column < 0) {
            final String messageTemplate = "Cannot add %1$s to %2$s at row %3$s column %4$s";
            final String message = 
                String.format(messageTemplate, getPrintName(widget), getPrintName(this), String.valueOf(row), String.valueOf(column));
            Logger.getLogger(getClass().getName()).warning(message);
            return;
        }
        addChildWidget(widget);
        QWidgetItem widgetItem = new QWidgetItem(widget);
        addItem(widgetItem, row, column, 1, 1, alignment);
    }
    
    public void addWidget(final QWidget widget, final int row, final int column, final Qt.AlignmentFlag... alignments){
        if (alignments==null || alignments.length==0){
            addWidget(widget, row, column, (Qt.Alignment)null);
        }else{
            addWidget(widget, row, column, new Qt.Alignment(alignments));
        }
    }
    
    public void addWidget(final QWidget widget, final int row, final int column){
        addWidget(widget, row, column, (Qt.Alignment)null);
    }
    
    public void addWidget(final QWidget widget, final int fromRow, final int fromColumn, final int rowSpan, final int columnSpan, final Qt.Alignment alignment){
        if (!checkWidget(widget)){
            return;
        }
        int toRow = (rowSpan < 0) ? -1 : fromRow + rowSpan - 1;
        int toColumn = (columnSpan < 0) ? -1 : fromColumn + columnSpan - 1;
        addChildWidget(widget);
        Cell cell = new Cell(widget);
        if (alignment!=null){
            cell.setAlignment(alignment);
        }
        add(cell, fromRow, toRow, fromColumn, toColumn);
        invalidate();
    }
    
    public void addWidget(final QWidget widget, final int fromRow, final int fromColumn, final int rowSpan, final int columnSpan, final Qt.AlignmentFlag... alignments){
        if (alignments==null || alignments.length==0){
            addWidget(widget, fromRow, fromColumn, rowSpan, columnSpan, (Qt.Alignment)null);
        }else{
            addWidget(widget, fromRow, fromColumn, rowSpan, columnSpan, new Qt.Alignment(alignments));
        }
    }
    
    public void addWidget(final QWidget widget, final int fromRow, final int fromColumn, final int rowSpan, final int columnSpan){
        addWidget(widget, fromRow, fromColumn, rowSpan, columnSpan, (Qt.Alignment)null);
    }
    
    public void addLayout(final QLayout layout, final int row, final int column, final Qt.Alignment alignment){
        if (!adoptLayout(layout)){
            return;
        }
        Cell cell = new Cell(layout);
        if (alignment!=null){
            cell.setAlignment(alignment);
        }
        add(cell, row, column);
    }
    
    public void addLayout(final QLayout layout, final int row, final int column){
        addLayout(layout, row, column, null);
    }
    
    public void addLayout(final QLayout layout, final int row, final int column, final int rowSpan, final int columnSpan, final Qt.Alignment alignment){
        if (!adoptLayout(layout)){
            return;
        }
        Cell cell = new Cell(layout);
        if (alignment!=null){
            cell.setAlignment(alignment);
        }
        add(cell, row, (rowSpan < 0) ? -1 : row + rowSpan - 1, column, (columnSpan < 0) ? -1 : column + columnSpan - 1);
    }    
    
    public void addLayout(final QLayout layout, final int row, final int column, final int rowSpan, final int columnSpan){
        addLayout(layout, row, column, rowSpan, columnSpan, null);
    }
    
    public boolean linkChildLayout(final HierarchicalGridLayout layout, final int row, final int column){
        if (layout!=null && layout.getParentLayout()==null){
            final Cell cell = getCellAtPosition(row, column);
            if (cell!=null && cell.getChildLayout()==null){
                layout.parentLayoutRef = new WeakReference<>(this);
                cell.setChildLayout(layout);
                if (layout.columnCount()>0){
                    layout.invalidate();
                    invalidate();
                }
                return true;
            }
        }
        return false;
    }
    
    public void unlinkChildLayout(final HierarchicalGridLayout layout){
        if (layout!=null){
            for (Cell cell: cells){
                if (cell.getChildLayout()==layout){
                    cell.setChildLayout(null);
                    layout.parentLayoutRef = null;
                }
            }
        }
    }
    
    public void setRowStretch(final int row, final int stretch){
        expand(row + 1, 0); 
        rStretch.set(row, stretch);
        setDirty();        
        invalidate();
    }
    
    public int rowStretch(final int row){
        return rStretch.get(row);
    }
    
    public void setColumnStretch(final int column, final int stretch){
        expand(0, column + 1); 
        cStretch.set(column, stretch);
        setDirty();
        invalidate();
    }
    
    public int columnStretch(final int column){
        return cStretch.get(column);
    }
    
    public void setRowMinimumHeight(final int row, final int height){
        expand(row + 1, 0); 
        rMinHeights.set(row, height);
        setDirty();
        invalidate();
    }
        
    public int rowMinimumHeight(final int row){
        return rMinHeights.get(row);
    }
    
    public void setColumnMinimumWidth(final int column, final int width){
        expand(0, column + 1); 
        cMinWidths.set(column, width);
        setDirty();
        invalidate();
    }
    
    public int  columnMinimumWidth(final int column){
        return cMinWidths.get(column);
    }
    
    public void setOriginCorner(final Qt.Corner corner){
        hReversed = corner == Qt.Corner.BottomLeftCorner || corner == Qt.Corner.BottomRightCorner; 
        vReversed = corner == Qt.Corner.TopRightCorner || corner == Qt.Corner.BottomRightCorner;
    }
    
    public Qt.Corner originCorner() {        
        if (hReversed) {
            return vReversed ? Qt.Corner.BottomRightCorner : Qt.Corner.TopRightCorner;
        } else {
            return vReversed ? Qt.Corner.BottomLeftCorner : Qt.Corner.TopLeftCorner;
        }
    }
    
    /**
     *              QLayout implementation
     * 
     **/    
    
    @Override
    public void addItem(final QLayoutItemInterface item) {
        addItem(item, nextR, nextC);
    }

    @Override
    public int count() {
        return cells.size();
    }

    @Override
    public QLayoutItemInterface itemAt(final int index) {
        return index < cells.size() ? cells.get(index).item() : null;
    }   
    
    @Override
    public QSize sizeHint() {
        return writeTemporarySize(sizeHintImpl(distributingGeometry));
    }
    
    private Dimension sizeHintImpl(final boolean distribution){
        final Dimension result = findSize(SizeData.SizeType.SizeHint, distribution);
        final Margins effectiveMargins = calcEffectiveMargins();
        result.width += effectiveMargins.left + effectiveMargins.right + paddings.summary;
        result.height += effectiveMargins.top + effectiveMargins.bottom;
        return result;
    }

    @Override
    public QLayoutItemInterface takeAt(final int index) {
        if (index < cells.size()) {
            final Cell cell = cells.remove(index);
            if (cell!=null) {
                final QLayoutItemInterface item = cell.item();
                final QLayout l = item==null ? null : item.layout();
                if (l!=null && l.parent()==this) {
                    l.setParent(null);
                }
                if (cell.childLayout!=null){
                    final HierarchicalGridLayout childLayout = cell.childLayout;
                    if (childLayout.parentLayoutRef!=null && childLayout.parentLayoutRef.get()==this){
                        childLayout.parentLayoutRef = null;
                    }
                    cell.childLayout = null;
                }
                return item;
            }
        }
        return null;
    }

    @Override
    public QSize minimumSize() {            
        final Dimension result = findSize(SizeData.SizeType.MinimumSize, distributingGeometry);
        final Margins effectiveMargins = calcEffectiveMargins();
        temporarySize.setWidth(result.width + effectiveMargins.left + effectiveMargins.right + paddings.summary);
        temporarySize.setHeight(result.height + effectiveMargins.top + effectiveMargins.bottom);
        return temporarySize;
    }

    @Override
    public QSize maximumSize() {
        return writeTemporarySize(maximumSize(true, distributingGeometry));
    }
    
    private Dimension maximumSize(final boolean respectAlignment, final boolean distribution){
        Dimension result = findSize(SizeData.SizeType.MaximumSize, distribution);
        final Margins effectiveMargins = calcEffectiveMargins();
        if (!effectiveMargins.isEmpty()){
            result.width += effectiveMargins.left + effectiveMargins.right;
            result.height += effectiveMargins.top + effectiveMargins.bottom;
        }
        result.width += paddings.summary;
        final Qt.Alignment alignment = alignment();
        
        if (result.width>QLAYOUTSIZE_MAX ||                 
            (respectAlignment && WidgetUtils.isSetHorizontalAlignmentFlag(alignment))){
            result.width = QLAYOUTSIZE_MAX;
        }
        if (result.height>QLAYOUTSIZE_MAX || 
            (respectAlignment && WidgetUtils.isSetVerticalAlignmentFlag(alignment))){
            result.height = QLAYOUTSIZE_MAX;
        }
        return result;
    }

    @Override
    public int heightForWidth(final int w) {        
        return getHeightForWidth(w);
    }

    @Override
    public boolean hasHeightForWidth() {
        setupLayoutData(null, distributingGeometry);
        return has_hfw;
    }        

    @Override
    public int minimumHeightForWidth(final int w) {        
        return getMinimumHeightForWidth(w);
    }

    @Override
    public void setGeometry(final QRect rect){
        if (parentLayoutRef==null &&
            (isDirty() || needDistribute || !rect.equals(geometry()))) {
            distributeRaw(qRect2Rectangle(rect));
            super.setGeometry(rect);
        }
    }
        
    private void distributeRaw(final Rectangle rect){        
        final Rectangle cr = alignment().value()>0  ? alignmentRect(rect) : rect;
        distribute(cr);
    }
    
    private Rectangle alignmentRect(final Rectangle r){
        
        final Dimension size = sizeHintImpl(true);
        final Dimension max = maximumSize(false, true);
        Qt.Alignment alignment = alignment();
        final Qt.Orientations directions = expandingDirectionsImpl(true);
        
        if (directions.isSet(Qt.Orientation.Horizontal) || !WidgetUtils.isSetHorizontalAlignmentFlag(alignment)){
            size.width = Math.min(r.width, max.width);
        }
        
        if (directions.isSet(Qt.Orientation.Vertical) || !WidgetUtils.isSetVerticalAlignmentFlag(alignment)){
            size.height = Math.min(r.height, max.height);
        }else if (hasHeightForWidth()){
            int hfw = heightForWidth(size.width);
            if (hfw < size.height){
                size.height = Math.min(hfw, max.height);
            }
        }        
        
        if (r.width < size.width){
            size.width = r.width;
        }
        
        if (r.height < size.height){
            size.height = r.height;
        }
               
        int x = r.x;
        int y = r.y;

        if (alignment.isSet(Qt.AlignmentFlag.AlignBottom)){
            y += (r.height - size.height);
        } else if (!alignment.isSet(Qt.AlignmentFlag.AlignTop)){
            y += (r.height - size.height) / 2;
        }

        final QWidget parent = parentWidget();
        alignment = 
            QStyle.visualAlignment(parent==null ? QApplication.layoutDirection() : parent.layoutDirection(), alignment);
        
        if (alignment.isSet(Qt.AlignmentFlag.AlignRight)){
            x += (r.width - size.width);
        }else if (!alignment.isSet(Qt.AlignmentFlag.AlignLeft)){
            x += (r.width - size.width) / 2;
        }

        return new Rectangle(x, y, size.width, size.height);
    }    
    
    @Override
    public Qt.Orientations expandingDirections() {
        return expandingDirectionsImpl(distributingGeometry);
    }        

    @Override
    public void invalidate() {
        if (!distributingGeometry){
            setDirty();
        }
        super.invalidate();
    }        
    
    private void printColumns(final StringBuilder out, final String padding){
        for (int i=0; i<columnCount; i++){
            out.append(padding);
            out.append("column ");
            out.append(String.valueOf(i+1));
            out.append(": {\n");
            colData.get(i).print(out, padding+"\t");
            out.append(padding);
            out.append("}\n");
        }
    }
    
    private void printTitle(final StringBuilder out, final String padding){
        out.append(padding);
        if (getParentLayout()==null){
            out.append("Outer Layout ");
        }else{
            out.append("Inner Layout ");
        }
        out.append(String.valueOf(columnCount));
        out.append('x');
        out.append(String.valueOf(rowCount));        
    }
}
