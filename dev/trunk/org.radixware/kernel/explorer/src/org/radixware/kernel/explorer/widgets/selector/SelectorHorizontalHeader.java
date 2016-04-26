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

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionHeader;
import com.trolltech.qt.gui.QWidget;
import java.awt.Dimension;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.HeaderWithCheckBox;


final class SelectorHorizontalHeader extends HeaderWithCheckBox{
    
    private final static Qt.Alignment ALIGNMENT_LEFT = new Qt.Alignment(Qt.AlignmentFlag.AlignVCenter, Qt.AlignmentFlag.AlignLeft);
    private final static QCursor SPLIT_CURSOR = new QCursor(Qt.CursorShape.SplitHCursor);    
    private final QStyleOptionHeader options = new QStyleOptionHeader();    
    private final SelectorHeaderStyle style;
    private int resizingSectionIndex=-1;
    private int originalSize;
    private int firstPos;
    
    
    public SelectorHorizontalHeader(final QWidget parent, final SelectorModel model){
        super(Qt.Orientation.Horizontal, parent);        
        options.setOrientation(Qt.Orientation.Horizontal);
        options.setTextAlignment(ALIGNMENT_LEFT);
        setHighlightSections(false);
        setTextElideMode(Qt.TextElideMode.ElideRight);//RADIX-407
        setStretchLastSection(false);
        setMovable(true);
        setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
        style = new SelectorHeaderStyle(parent, model);
        setStyle(style);
    }
       
    @Override
    protected QSize sectionSizeFromContents(final int logicalIndex) {
        final QSize size = super.sectionSizeFromContents(logicalIndex);
        if (logicalIndex==0 && isSelectionEnabled()){
            size.setWidth(18);
        } else if (sortingIndicatorShown(logicalIndex)){
            options.setSection(logicalIndex);
            final QRect rect = style().subElementRect(QStyle.SubElement.SE_HeaderArrow, options, this);
            if (rect!=null && rect.isValid()){
                size.setWidth(size.width()+SelectorHeaderStyle.SORTING_INDICATOR_MARGIN+rect.width());
            }
        }
        return size;
    }

    @Override
    public QSize sizeHint() {
        final QSize size = super.sizeHint();
        final int columnCount = count();
        final int defaultSectionSize = defaultSectionSize();
        int width = frameWidth()*2;        
        for (int column = columnCount - 1; column >= 0; --column) {
            final int logical = logicalIndex(column);
            if (!isSectionHidden(logical)) {
                width += Math.max(sectionSizeFromContents(logical).width(), defaultSectionSize);
            }
        }
        if (width>size.width()){
            final Dimension sizeLimit = WidgetUtils.getWndowMaxSize();
            size.setWidth(Math.min(width, (int)sizeLimit.getWidth()));            
        }
        return size;
    }
                
    private boolean sortingIndicatorShown(final int logicalIndex){
        return model().headerData(logicalIndex, Qt.Orientation.Horizontal, Qt.ItemDataRole.UserRole+1)!=null;
    }
    
    @Override
    protected void mousePressEvent(final QMouseEvent e) {
        final boolean someButton = e.buttons().isSet(Qt.MouseButton.LeftButton);
        if (resizingSectionIndex<0 && someButton){
            final int pos = e.x();
            final int handle = sectionHandleAt(pos);            
            if (handle>-1){
                final int sectionToResize = findSectionToResize(handle);
                if (sectionToResize>-1){
                    originalSize = sectionSize(sectionToResize);
                    resizingSectionIndex = sectionToResize;
                    firstPos = pos;
                }
                e.accept();
                return;
            }
        }        
        if (someButton && isSelectionEnabled() && visualIndexAt(e.x())==0){
            setMovable(false);
            setClickable(false);
            try{
                super.mousePressEvent(e);
            }finally{
                setMovable(true);
                setClickable(true);
            }
        }else{
            super.mousePressEvent(e);
        }
    }

    @Override
    protected void mouseReleaseEvent(final QMouseEvent e) {
        if (resizingSectionIndex>-1){
            resizingSectionIndex = -1;
            e.accept();
        }else{
            super.mouseReleaseEvent(e);
        }
    }

    @Override
    protected void mouseMoveEvent(final QMouseEvent e) {
        final boolean someButton = e.buttons().isSet(Qt.MouseButton.LeftButton);
        if (resizingSectionIndex>-1){
            if (someButton){
                final int pos = e.x();
                if (pos>=0){
                    int delta = isRightToLeft() ? firstPos - pos : pos - firstPos;
                    if (isAfterStretchSection(resizingSectionIndex)){
                        delta = -delta;
                    }
                    resizeSection(resizingSectionIndex, Math.max(originalSize+delta, minimumSectionSize()));
                }
            }else{
                resizingSectionIndex = -1;                
            }
            e.accept();
        }else{
            final int pos = e.x();
            final int handle = sectionHandleAt(pos);            
            if (handle>-1){
                final boolean hasCursor = testAttribute(Qt.WidgetAttribute.WA_SetCursor);
                if (someButton){
                    if (hasCursor){
                        setCursor(null);
                        setAttribute(Qt.WidgetAttribute.WA_SetCursor, false);                    
                    }
                    
                }else{
                    if (findSectionToResize(handle)>-1){
                        if (!hasCursor){
                            setCursor(SPLIT_CURSOR);
                        }
                    }else if (hasCursor){
                        setCursor(null);
                        setAttribute(Qt.WidgetAttribute.WA_SetCursor, false);                    
                    }                    
                }
                e.accept();
                return;
            }else if (someButton && isSelectionEnabled() && visualIndexAt(pos)==0){
                e.accept();
                return;                
            }
            super.mouseMoveEvent(e);
        }
    }
    
    private boolean isAfterStretchSection(final int logicalIndex){
        final int visualIndex = visualIndex(logicalIndex);
        final int startIndex = isRightToLeft() ? visualIndex : 0;
        final int endIndex = isRightToLeft() ? count()-1 : visualIndex;
        for (int i=startIndex; i<=endIndex; i++){
            final int log = logicalIndex(i);
            if (resizeMode(log)==QHeaderView.ResizeMode.Stretch && !isSectionHidden(log)){
                return true;
            }
        }
        return false;
    };   
    
    private int sectionHandleAt(final int position){
        final int visual = visualIndexAt(position);
        if (visual == -1){
            return -1;
        }
        final int log = logicalIndex(visual);
        final int pos = sectionViewportPosition(log);
        final int grip = style().pixelMetric(QStyle.PixelMetric.PM_HeaderGripMargin, null, this);

        final boolean atLeft,atRight;
        if (isRightToLeft()){
            atLeft = (position > pos + sectionSize(log) - grip);
            atRight = position < pos + grip;
        }else{
            atLeft = position < pos + grip;
            atRight = (position > pos + sectionSize(log) - grip);            
        }     
        if (atLeft){
            return previousVisibleSection(log);
        }
        else if (atRight){
            return log;
        }else{
            return -1;
        }
    }
    
    private int previousVisibleSection(final int logicalIndex){
        int visual = visualIndex(logicalIndex);
        while(visual>-1){
            final int result = logicalIndex(--visual);
            if (!isSectionHidden(result)){
                return result;
            }
        }
        return -1;
    }
    
    private int nextVisibleSection(final int logicalIndex){
        int visual = visualIndex(logicalIndex);
        while(visual < this.count()) {
            final int result = logicalIndex(++visual);
            if (result<0){
                break;
            }
            if (!isSectionHidden(result)){
                return result;
            }
        }
        return -1;
    }
    
    private int findSectionToResize(final int startFrom){
        if (startFrom==0 && isSelectionEnabled()){
            return -1;
        }
        final boolean forward = isAfterStretchSection(startFrom);
        int section = forward ? nextVisibleSection(startFrom) : startFrom;
        while (section>-1 && resizeMode(section)!=QHeaderView.ResizeMode.Interactive){
            section = forward ? nextVisibleSection(section) : previousVisibleSection(section);
        }
        return section;
    }
    
    private boolean isSelectionEnabled(){
        return ((SelectorModel)model()).isSelectionEnabled();
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        setStyle(null);
        WidgetUtils.CustomStyle.release(style);
        super.closeEvent(event);
    }    
}
