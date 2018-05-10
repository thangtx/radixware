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

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionHeader;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QTreeView;
import org.radixware.kernel.explorer.text.ExplorerFont;


final class SelectorTreeVerticalHeader extends QTreeView{
    
    private final static class ItemDelegate extends QItemDelegate{
        
        private final QStyleOptionHeader styleOption = new QStyleOptionHeader();
        private final SelectorTree tree;
        private final QStyle.State state = new QStyle.State(QStyle.StateFlag.State_None);
        private final QSize sizeHint = new QSize(0, 0);
        private final int width;
        private long currentRowParentIndexId;
        private int currentRow;
        
        public ItemDelegate(final SelectorTree selectorTree, final QFontMetrics symbolFontMetrics){
            super(selectorTree);
            tree = selectorTree;
            styleOption.setOrientation(Qt.Orientation.Vertical);
            styleOption.setSelectedPosition(QStyleOptionHeader.SelectedPosition.NotAdjacent);
            styleOption.setText(String.valueOf(TableCornerButton.BUTTON_SYMBOL));
            
            if (symbolFontMetrics==null){
                final ExplorerFont font = ExplorerFont.Factory.getFont(tree.font()).getBold();
                final QFontMetrics fontMetrics = font.getQFontMetrics();
            }else{
                styleOption.setFontMetrics(symbolFontMetrics);
            }
            
            initStyleOption(false);
            width =
                tree.style().sizeFromContents(QStyle.ContentsType.CT_HeaderSection, styleOption, new QSize(), tree).width();
            styleOption.setText("  ");
        }
        
        private void initStyleOption(final boolean isCurrentRow){
            styleOption.initFrom(tree);
            styleOption.setSection(0);
            state.clearAll();
            state.set(QStyle.StateFlag.State_None);
            if (tree.isEnabled()){
                state.set(QStyle.StateFlag.State_Enabled);
            }
            if (tree.window().isActiveWindow()){
                state.set(QStyle.StateFlag.State_Active);
            }            
            if (isCurrentRow){
                state.set(QStyle.StateFlag.State_On);
                state.set(QStyle.StateFlag.State_Sunken);
            }else{
                state.set(QStyle.StateFlag.State_Raised);
            }                        
            styleOption.setState(state);            
        }

        @Override
        public void paint(final QPainter painter,
                                  final QStyleOptionViewItem itemStyleOption,
                                  final QModelIndex index) {
            
            initStyleOption(isCurrentRow(index));

            // setup the style options structure
            final QRect rect = itemStyleOption.rect();
            rect.setWidth(width);
            styleOption.setRect(rect);

            // the section position
            if (index.parent()==null && index.row()==0){
                styleOption.setPosition(QStyleOptionHeader.SectionPosition.Beginning);
            }else{
                styleOption.setPosition(QStyleOptionHeader.SectionPosition.Middle);
            }

            // draw the section
            tree.style().drawControl(QStyle.ControlElement.CE_Header, styleOption, painter, tree);
        }

        @Override
        public QSize sizeHint(final QStyleOptionViewItem itemStyleOption, final QModelIndex index) {            
            sizeHint.setHeight(tree.getRowSizeHint(index));
            sizeHint.setWidth(width);
            return sizeHint;
        }                
        
        public int getWidth(){
            return width;
        }
        
        private boolean isCurrentRow(final QModelIndex index){
            return currentRow>=0 
                      && index!=null
                      && currentRowParentIndexId==(index.parent()==null ? 0 : index.parent().internalId())
                      && currentRow==index.row();
        }
        
        public boolean setCurrentRow(final QModelIndex current){
            final long newRowParentIndexId = current==null || current.parent()==null ? 0 : current.parent().internalId();
            final int newRow = current==null ? -1 : current.row();
            if (newRowParentIndexId!=currentRowParentIndexId || newRow!=currentRow){
                currentRowParentIndexId = newRowParentIndexId;
                currentRow = newRow;
                return true;
            }else{
                return false;
            }
        }
    }    
    
    private final ItemDelegate delegate;

    public SelectorTreeVerticalHeader(final SelectorTree selectorTree, final SelectorModel model, final QFontMetrics symbolFontMetrics){
        super(selectorTree);
        setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
        setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
        setModel(model);
        setIndentation(0);
        final QHeaderView header = header();
        header.setVisible(false);
        for (int i=1, count=header().count(); i<count; i++){
            header.setSectionHidden(i, true);
        }
        delegate = new ItemDelegate(selectorTree, symbolFontMetrics);
        final int width = delegate.getWidth();
        header.setFixedSize(0, width);
        setItemDelegate(delegate);
        setFixedWidth(width);
        setFrameShape(QFrame.Shape.NoFrame);        
    }
    
    public void setCurrentRow(final QModelIndex index){
        if (delegate.setCurrentRow(index)){
            update(index);
        }
    }

    @Override
    public QSize sizeHint() {
        final QSize defautlSizeHint = super.sizeHint();
        defautlSizeHint.setWidth(delegate.getWidth());
        return defautlSizeHint;
    }

    @Override
    public QSize minimumSizeHint() {
        final QSize defautlSizeHint = super.sizeHint();
        defautlSizeHint.setWidth(delegate.getWidth());
        return defautlSizeHint;
    }        
    
    @Override
    protected void drawBranches(QPainter painter, QRect rect, QModelIndex index) {
        //do not draw branches
    }
    
    @Override
    protected boolean edit(final QModelIndex index, final EditTrigger trigger, final QEvent event) {
        return false;
    }
}
