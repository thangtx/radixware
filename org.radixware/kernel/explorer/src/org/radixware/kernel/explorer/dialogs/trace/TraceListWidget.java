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

package org.radixware.kernel.explorer.dialogs.trace;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTextStream;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QListView;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QSortFilterProxyModel;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QWidget;
import java.awt.Dimension;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceItem;


public final class TraceListWidget extends QListView implements ITokenProvider {
    
    private class TraceItemToken implements IToken{
        
        private final int row;
        
        public TraceItemToken(final int row){
            this.row = row;
        }

        @Override
        public String getValue() {
            final ExplorerTraceItem item = getTraceItem(row);
            return item==null ? "" : item.getMessageText();
        }

        @Override
        public void select() {
            TraceListWidget.this.setCurrentRow(row);
            TraceListWidget.this.scrollTo(TraceListWidget.this.model().index(row, 0), ScrollHint.PositionAtCenter);
        }
    }

    private class TraceListKeyListener extends QEventFilter {
        
        public TraceListKeyListener(final QObject parent){
            super(parent);
            setProcessableEventTypes(EnumSet.of(QEvent.Type.KeyPress));
        }

        @Override
        public boolean eventFilter(final QObject source, final QEvent event) {
            if (event instanceof QKeyEvent && event.type() == QEvent.Type.KeyPress
                    && ((QKeyEvent) event).key() == Qt.Key.Key_Return.value() && (source instanceof TraceListWidget)) {
                final ExplorerTraceItem item = ((TraceListWidget) source).getCurrentItem();
                if (item!=null){
                    MessageViewDialog.show(environment, item, (TraceListWidget) source);
                }
                return true;
            }
            return false;
        }
    }
    
    private static final class FilteredModel extends QSortFilterProxyModel{
        
        private final TraceFilter filter;
        
        public FilteredModel(final TraceListModel sourceModel, final TraceFilter filter, final QObject parent){
            super(parent);
            setSourceModel(sourceModel);
            this.filter = filter;
        }

        @Override
        protected boolean filterAcceptsRow(final int source_row, final QModelIndex source_parent) {
            final ExplorerTraceItem item = ((TraceListModel)sourceModel()).getTraceItem(source_row);
            return item == null ? false : filter.isAllowed(item);
        }                        
    }
    
    private static final class TraceItemDelegate extends QItemDelegate{
        
        private final QSize itemSizeHint = new QSize();
        private final TraceListModel traceModel;
        private final FilteredModel proxyModel;        
        
        public TraceItemDelegate(final TraceListModel traceModel, final FilteredModel proxyModel, final QObject parent){
            super(parent);
            this.traceModel = traceModel;
            this.proxyModel = proxyModel;
        }

        @Override
        public QSize sizeHint(final QStyleOptionViewItem option, final QModelIndex index) {
            if (index==null){
                return super.sizeHint(option, null);
            }
            final QModelIndex sourceIndex = proxyModel.mapToSource(index);
            if (sourceIndex==null){
                return super.sizeHint(option, index);
            }
            final ExplorerTraceItem item = traceModel.getTraceItem(sourceIndex.row());
            if (item==null){
                return super.sizeHint(option, index);
            }
            final Dimension cachedSize = item.getCachedSize();
            if (cachedSize==null){
                final QSize sizeHint = super.sizeHint(option, index);
                item.cacheSize(sizeHint);
                return sizeHint;
            }
            itemSizeHint.setWidth(cachedSize.width);
            itemSizeHint.setHeight(cachedSize.height);
            return itemSizeHint;
        }                
    }
        
    private final IClientEnvironment environment;
    private final TraceListModel traceModel;
    private final FilteredModel proxyModel;
    private final Finder finder;    
    private boolean firstShow = true;
    private int currentRow = -1;        
    

    @SuppressWarnings("LeakingThisInConstructor")
    public TraceListWidget(final IClientEnvironment environment, final TraceFilter filter, final QWidget parent) {
        super(parent);
        this.environment = environment;                        
        setIconSize(new QSize(16,16));
        setSpacing(1);
        traceModel = 
            new TraceListModel(environment.getTracer().getBuffer().getMaxSize(), this, environment);
        proxyModel = new FilteredModel(traceModel, filter, this);
        setItemDelegate(new TraceItemDelegate(traceModel, proxyModel, parent));
        setModel(proxyModel);
        finder = new Finder(environment, this);
        filter.filterChanged.connect(this, "updateTraceList()");        
        this.installEventFilter(new TraceListKeyListener(this));        
    }        
    
    private ExplorerTraceItem getCurrentItem(){
        return getTraceItem(currentRow);
    }
    
    @Override
    protected void currentChanged(final QModelIndex current, final QModelIndex previous) {
        currentRow = current==null ? -1 : current.row();
    }

    @Override
    protected void mouseDoubleClickEvent(final QMouseEvent event) {
        super.mouseDoubleClickEvent(event);
        final ExplorerTraceItem item = getTraceItem(indexAt(event.pos()));
        if (item!=null){
            MessageViewDialog.show(environment, item, this);
        }
    }
    
    private ExplorerTraceItem getTraceItem(final QModelIndex index){
        if (index==null){
            return null;
        }else{
            final QModelIndex sourceIndex = proxyModel.mapToSource(index);
            return sourceIndex==null ? null : traceModel.getTraceItem(sourceIndex.row());
        }
    }
    
    private ExplorerTraceItem getTraceItem(final int row){
         return getTraceItem(getIndexFromRow(row));
    }

    public void showFindDialog() {
        finder.find();
    }

    public void findNext() {
        finder.findNext();
    }

    public void addTraceItems(final List<ExplorerTraceItem> traceItems) {
        traceModel.addTraceItems(traceItems);     
        update();
    }

    @SuppressWarnings("unused")
    private void updateTraceList() {
        proxyModel.invalidate();
        update();
    }

    public void writeData(final QTextStream textStream) {
        for (int i = 0; i < traceModel.rowCount(); ++i) {
            final ExplorerTraceItem item = traceModel.getTraceItem(i);
            textStream.writeString(item.getFormattedMessage() + "\r\n");
        }
    }

    @Override
    public IToken getNextToken() {
        return new TraceItemToken(currentRow);
    }

    @Override
    public IToken getPrevToken() {
        return new TraceItemToken(currentRow);
    }

    @Override
    public boolean hasNextToken() {        
        if (proxyModel.rowCount()== 0) {
            return false;
        }
        currentRow++;
        if (currentRow >= proxyModel.rowCount()) {
            final List<QModelIndex> list = this.selectedIndexes();
            currentRow = list.isEmpty() ? -1 : list.get(0).row();
            return false;
        }
        return true;
    }

    @Override
    public boolean hasPrevToken() {       
        if (proxyModel.rowCount()== 0) {
            return false;
        }
        currentRow--;
        if (currentRow < 0) {            
            final List<QModelIndex> list = this.selectedIndexes();
            currentRow = list.isEmpty() ? -1 : list.get(0).row();
            return false;
        }
        return true;        
    }
    
    private QModelIndex getIndexFromRow(final int row){
        return row>=0 ? proxyModel.index(row, 0, null) : null;
    }
    
    public boolean setCurrentRow(final int row){
        final QModelIndex index = getIndexFromRow(row);
        if (index==null){
            return false;
        }else{
            setCurrentIndex(index);
            return true;
        }
    }        

    @Override
    protected void showEvent(final QShowEvent event) {
        if (event.type() == QEvent.Type.Show && firstShow) {
            for (int row = 0; row < proxyModel.rowCount(); row++) {
                if (setCurrentRow(row)){
                    firstShow = false;
                    break;                       
                }
            }
        }
        super.showEvent(event);
    }
    
    public void clear(){
        traceModel.clear();
        update();
    }
    
    public void setMaxSize(final int maxSize){
        traceModel.setMaxSize(maxSize);
        update();
    }    
}
