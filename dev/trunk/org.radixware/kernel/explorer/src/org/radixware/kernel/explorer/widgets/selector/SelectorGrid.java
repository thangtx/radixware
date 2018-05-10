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

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.core.Qt;
import org.radixware.kernel.explorer.widgets.ExplorerAction;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QAbstractItemView.CursorAction;
import com.trolltech.qt.gui.QAbstractItemView.EditTrigger;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QDesktopServices;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QScrollBar;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionHeader;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QWidget;
import java.awt.Dimension;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EHierarchicalSelectionMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.errors.ActivatingPropertyError;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelAsyncReader;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.client.widgets.selector.IMultiSelectionWidget;
import org.radixware.kernel.common.client.widgets.selector.ISelectorDataExportOptionsDialog;
import org.radixware.kernel.common.client.models.GroupModelCsvWriter;
import org.radixware.kernel.common.client.types.AggregateFunctionCall;
import org.radixware.kernel.common.client.models.GroupModelXlsxWriter;
import org.radixware.kernel.common.client.models.HierarchicalSelection;
import org.radixware.kernel.common.client.types.GroupRestrictions;
import org.radixware.kernel.common.client.types.SelectorColumnsStatistic;
import org.radixware.kernel.common.client.widgets.IToolButton;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.common.client.widgets.selector.SelectorModelDataLoader;
import org.radixware.kernel.common.enums.EAggregateFunction;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.enums.ESelectorColumnSizePolicy;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerMessageBox;
import org.radixware.kernel.explorer.env.ExplorerIcon;

import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.selector.Selector;
import org.radixware.kernel.explorer.widgets.AbstractGrid;
import org.radixware.kernel.explorer.widgets.FilteredMouseEvent;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditor;

public class SelectorGrid extends AbstractGrid implements IExplorerSelectorWidget, IMultiSelectionWidget {    
    
    private static final class VerticalHeader extends AbstractGrid.GridHeader{                
        
        private int currentRow = -1;
        private int width = -1;
        private QFontMetrics symbolFontMetrics;
        private final QStyleOptionHeader option = new QStyleOptionHeader();
        private final QStyle.State state = new QStyle.State(0);        
        
        public VerticalHeader(final AbstractGrid parent){
            super(parent);
        }
        
        public void setSymbolFontMetrics(final QFontMetrics fontMetrics){
            symbolFontMetrics = fontMetrics;
            if (width>-1){
                width = -1;
                updateGeometry();
            }
        }
        
        public void setCurrentRow(int row){
            if (currentRow!=row){
                currentRow = row;
            }
        }

        @Override
        protected void paintSection(final QPainter painter, final QRect rect, final int logicalIndex) {
            if (!rect.isValid())
                return;
            // get the state of the section
            initStyleOption(option);
            state.clearAll();
            if (isEnabled())
                state.set(QStyle.StateFlag.State_Enabled);                
            if (window().isActiveWindow())
                state.set(QStyle.StateFlag.State_Active);
            if (currentRow==logicalIndex){
                state.set(QStyle.StateFlag.State_On);
                state.set(QStyle.StateFlag.State_Sunken);
            }else{
                state.set(QStyle.StateFlag.State_Raised);
            }

            // setup the style options structure
            option.setRect(rect);
            option.setSection(logicalIndex);
            option.setState(state);
            option.setText(" ");

            // the section position
            int visual = visualIndex(logicalIndex);
            assert visual != -1;
            if (count() == 1)
                option.setPosition(QStyleOptionHeader.SectionPosition.OnlyOneSection);
            else if (visual == 0)
                option.setPosition(QStyleOptionHeader.SectionPosition.Beginning);                
            else if (visual == count() - 1)
                option.setPosition(QStyleOptionHeader.SectionPosition.End);
            else
                option.setPosition(QStyleOptionHeader.SectionPosition.Middle);
            option.setOrientation(Qt.Orientation.Vertical);
            option.setSelectedPosition(QStyleOptionHeader.SelectedPosition.NotAdjacent);
            // draw the section
            style().drawControl(QStyle.ControlElement.CE_Header, option, painter, this);
        }

        @Override
        protected void mousePressEvent(QMouseEvent mouseEvent) {
            //super.mousePressEvent(mouseEvent);
            mouseEvent.accept();
        }
                
        @Override
        public QSize sizeHint() {
            final QSize size = super.sizeHint();
            if (size.width()>0 && symbolFontMetrics!=null){
                size.setWidth(getWidth());
            }
            return size;
        }
        
        public int getWidth(){
            if (width < 0){
                final QStyleOptionHeader opt = new QStyleOptionHeader();
                initStyleOption(opt);
                opt.setSection(0);
                opt.setFontMetrics(symbolFontMetrics);
                opt.setText(String.valueOf(TableCornerButton.BUTTON_SYMBOL));
                final QSize size = style().sizeFromContents(QStyle.ContentsType.CT_HeaderSection, opt, new QSize(), this);
                width = size.width();
            }
            return width;
        }
    }
    
    private static final class VerticalHeaderFactory implements AbstractGrid.IVerticalHeaderFactory{

        @Override
        public QHeaderView createVerticalHeader(final AbstractGrid parent) {
            return new VerticalHeader(parent);
        }
        
    }
    
    private static enum EventType{FETCH_MORE, PAINT, REREAD, SELECTION_CHANGED};
    
    private static class SelectorGridEvent extends QEvent{
        
        private final EventType type;
        
        public SelectorGridEvent(final EventType type){
            super(QEvent.Type.User);
            this.type = type;
        }
        
        public EventType getType(){
            return type;
        }
        
    }
    
    private static final class FetchMoreEvent extends SelectorGridEvent{        
        
        private final boolean silently;
                
        public FetchMoreEvent(final boolean silently){
            super(EventType.FETCH_MORE);
            this.silently = silently;
        }        
        
        public boolean silentlyFetch(){
            return silently;
        }
    }
    
    private static final class IndexInfo implements PostponedMousePressEvent.IndexInfo<SelectorGrid>{
        
        private final int row;
        private final int column;
        
        public IndexInfo(final QModelIndex index){
            row = index.row();
            column = index.column();
        }

        @Override
        public QModelIndex getIndex(final SelectorGrid view) {
            return view.controller.model.index(row, column);
        }
        
    }
        
    private final static class PointInfo{
        
        public static final PointInfo UNKNOWN_POINT = new PointInfo(null, null, null, false);
        
        private final int row;
        private final int column;
        private final Id propertyId;
        private final Object propertyValue;
        private final boolean insideCheckBox;
        
        public PointInfo(final QModelIndex index, final Id propertyId, final Object propertyValue, final boolean insideCheckBox){
            row = index==null ? -1 : index.row();
            column = index==null ? -1 : index.column();
            this.propertyId = propertyId;
            this.propertyValue = propertyValue;
            this.insideCheckBox = insideCheckBox;
        }
        
        public boolean insideCheckBox(){
            return insideCheckBox;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 41 * hash + this.row;
            hash = 41 * hash + this.column;
            hash = 41 * hash + Objects.hashCode(this.propertyId);
            hash = 41 * hash + Objects.hashCode(this.propertyValue);
            hash = 41 * hash + (this.insideCheckBox ? 1 : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PointInfo other = (PointInfo) obj;
            if (this.row != other.row) {
                return false;
            }
            if (this.column != other.column) {
                return false;
            }
            if (!Objects.equals(this.propertyId, other.propertyId)) {
                return false;
            }
            if (!Objects.equals(this.propertyValue, other.propertyValue)) {
                return false;
            }
            if (this.insideCheckBox != other.insideCheckBox) {
                return false;
            }
            return true;
        }                
    }
    
    private class FilteredMouseEventListener extends QEventFilter{
        
        public FilteredMouseEventListener(final QObject parent){
            super(parent);
            setProcessableEventTypes(EnumSet.of(QEvent.Type.User));
        }
        
        @Override
        public boolean eventFilter(final QObject target, final QEvent event) {
            if (event instanceof FilteredMouseEvent) {
                SelectorGrid.this.processFilteredMouseEvent((FilteredMouseEvent)event);
                return true;
            }
            return false;
        }
    }    
    
    private class IndexIterator implements Iterator<QModelIndex>{
                
        private final int endRow;
        private QModelIndex currentIndex;    
        
        public IndexIterator(final QModelIndex startIndex, final QModelIndex endIndex){            
            currentIndex = startIndex;
            endRow = endIndex==null ? -1 : endIndex.row();
        }

        @Override
        public boolean hasNext() {
            return currentIndex!=null;
        }

        @Override
        public QModelIndex next() {
            if (currentIndex==null){
                throw new NoSuchElementException();
            }else{
                final QModelIndex nextIndex = currentIndex;
                if (nextIndex.row()==endRow){
                    currentIndex = null;
                }else{
                    currentIndex = SelectorGrid.this.model().index(currentIndex.row()+1, currentIndex.column());
                }
                return nextIndex;
            }
        }

        @Override
        public void remove() {
             throw new UnsupportedOperationException();
        }
        
    }

    private final static class Icons extends ExplorerIcon.CommonOperations {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        public static final Icons NEXT = new Icons("classpath:images/next.svg");
        public static final Icons PREVIOUS = new Icons("classpath:images/prev.svg");
        public static final Icons BEGIN = new Icons("classpath:images/begin.svg");
        public static final Icons END = new Icons("classpath:images/end.svg");
    }    
    
    private final static int DEFAULT_HEIGHT = 260;
    private final static String ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH =
            SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.ROWS_LIMIT_FOR_NAVIGATION;
    private final static String ROWS_LIMIT_FOR_RESTORING_POSITION_CONFIG_PATH =
            SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.ROWS_LIMIT_FOR_RESTORING_POSITION;

    public final class Actions {

        public final ExplorerAction findAction;
        public final ExplorerAction findNextAction;
        public final ExplorerAction prevAction;
        public final ExplorerAction nextAction;
        public final ExplorerAction beginAction;
        public final ExplorerAction exportAction;
        public final ExplorerAction endAction;
        public final ExplorerAction exportCsvAction;
        public final ExplorerAction exportExcelAction;
        public final ExplorerAction calcStatisticAction;

        public Actions(final IClientEnvironment environment) {
            final MessageProvider mp = environment.getMessageProvider();
            findAction = createAction(environment, Icons.FIND, mp.translate("Selector", "Find..."), "onFindAction()", "find");
            findNextAction = createAction(environment, Icons.FIND_NEXT, mp.translate("Selector", "Find Next"), "onFindNextAction()", "find_next");
            nextAction = createAction(environment, Icons.NEXT, mp.translate("Selector", "Next"), "selectNextRow()","next");
            prevAction = createAction(environment, Icons.PREVIOUS, mp.translate("Selector", "Previous"), "selectPrevRow()","previous");
            beginAction = createAction(environment, Icons.BEGIN, mp.translate("Selector", "First"), "selectFirstRow()","first");
            endAction = createAction(environment, Icons.END, mp.translate("Selector", "Last"), "selectLastRow()","last");
            exportAction = createAction(environment, ClientIcon.Selector.EXPORT, mp.translate("Selector", "Export Selector Content"), null, "export");
            exportCsvAction = createAction(environment, ClientIcon.Selector.EXPORTCSV, mp.translate("Selector", "Export Selector Content in CSV format"), "export()","export_csv");
            exportExcelAction = createAction(environment, ClientIcon.Selector.EXPORTXLSX, mp.translate("Selector", "Export Selector Content in XLSX format"), "exportExcel()","export_excel");
            calcStatisticAction = createAction(environment, ClientIcon.Selector.CALC_STATISTIC, mp.translate("Selector", "Statistics"), "calcSelectionStatistic()","statistics");
        }

        private ExplorerAction createAction(final IClientEnvironment environment,
                final ClientIcon icon,
                final String title,
                final String slot,
                final String objectName) {
            final ExplorerAction action =
                    new ExplorerAction(ExplorerIcon.getQIcon(icon), title, SelectorGrid.this);
            WidgetUtils.updateActionToolTip(environment, action);
            if (slot!=null){
                action.triggered.connect(SelectorGrid.this, slot);
            }
            action.setObjectName(objectName);
            return action;
        }

        public void refresh() {
            final boolean currentEntityDefined = controller.currentEntityDefined();
            final int rows = rowCount();
            final int cur = currentIndex() != null && currentEntityDefined ? currentIndex().row() : -1;
            prevAction.setEnabled(true);
            beginAction.setEnabled(true);
            nextAction.setEnabled(true);
            endAction.setEnabled(true);
            if (cur == -1 || cur == 0) {
                prevAction.setDisabled(true);
                beginAction.setDisabled(true);
            }
            if (cur == -1 || cur == rows - 1) {
                nextAction.setDisabled(true);
                endAction.setDisabled(true);
            }
            findAction.setEnabled(rows > 0 && currentEntityDefined);
            findNextAction.setEnabled(rows > 0 && currentEntityDefined);
            exportAction.setEnabled(rows > 0 && currentEntityDefined);
            exportCsvAction.setEnabled(rows > 0);
            exportExcelAction.setEnabled(rows > 0);
            calcStatisticAction.setEnabled(rows > 0 
                                                        && !getGroupModel().getRestrictions().getIsCalcStatisticRestricted()
                                                        && getSelection().isEmpty());
            refreshExportActionToolTip();
        }
        
        private void refreshExportActionToolTip(){
            final MessageProvider mp = controller.selector.getEnvironment().getMessageProvider();
            if (getSelection().isEmpty()){
                exportCsvAction.setToolTip(mp.translate("Selector", "Export Selector Content in CSV format"));
            }else if (getSelection().isSingleObjectSelected()){
                exportCsvAction.setToolTip(mp.translate("Selector", "Export Selected Object in CSV format"));
            }else{
                exportCsvAction.setToolTip(mp.translate("Selector", "Export Selected Objects in CSV format"));
            }
        }

        private void close() {
            findAction.close();
            findNextAction.close();
            prevAction.close();
            nextAction.close();
            beginAction.close();
            endAction.close();
            exportCsvAction.close();
            exportExcelAction.close();
            exportAction.close();
            calcStatisticAction.close();
        }
    }
    
    private static enum ESelectAction{SELECT,UNSELECT,INVERT};
    
    public final Actions actions;
    private final Selector selector;
    private final StandardSelectorWidgetController controller;
    private final SelectorModelDataLoader objectFinder;
    private final TableCornerButton cornerButton = new TableCornerButton(this);
    private boolean searching, sizeHintCalculating;
    private QEventsScheduler scheduler = new QEventsScheduler();
    private SelectorHorizontalHeader horizontalHeader;
    private boolean blockSelectionListener;
    private boolean geometryRecursionBlock;
    private boolean disableFetchMoreOnInsertRows;
    private final EnumSet<EventType> postedEvents = EnumSet.noneOf(EventType.class);
    private FilteredMouseEvent postponedMouseEvent;
    private PostponedMousePressEvent<SelectorGrid> postponedMousePressEvent;
    private long postedMousePressEventId;
    private final FilteredMouseEventListener filteredMouseEventListener = new FilteredMouseEventListener(this);
    
    private final HierarchicalSelection.ISelectionListener<SelectorNode> selectionHandler = 
            new HierarchicalSelection.ISelectionListener<SelectorNode>(){
                @Override
                public void afterSelectionChanged(final HierarchicalSelection<SelectorNode> selection) {
                    SelectorGrid.this.postSelectorGridEvent(EventType.SELECTION_CHANGED);
                }
            };
    
    private final StandardSelectorWidgetController.IndexIteratorFactory indexIteratorFactory = 
        new StandardSelectorWidgetController.IndexIteratorFactory(){
            @Override
            public Iterator<QModelIndex> create(final QModelIndex startIndex, final QModelIndex endIndex) {
                return new IndexIterator(startIndex, endIndex);
            }        
        };
    
    private final QEventFilter eventFilter = new QEventFilter(this){

        @Override
        public boolean eventFilter(QObject target, QEvent event) {
            if (target==SelectorGrid.this && event!=null){
                return SelectorGrid.this.filterQtEvent(event);
            }else{
                return super.eventFilter(target, event);
            }
        }
        
    };
    
    private final GroupModelAsyncReader.Listener asyncReaderListener =
            new GroupModelAsyncReader.Listener() {
        @Override
        public void readingWasFinished() {
            super.readingWasFinished();
            if (isEnabled()) {
                finishEdit();
                final QModelIndex currentIndex = currentIndex();
                final QModelIndex topIndex = controller.view.indexAt(new QPoint(1, 1));
                final int difference = currentIndex == null ? 0 : currentIndex.row() - topIndex.row();
                final int column = currentIndex==null ? horizontalHeader.getFirstVisibleColumnIndex() : currentIndex.column();
                lockInput();
                int idx = -1;

                final GroupModelAsyncReader asyncReader = getGroupModel().getAsyncReader();
                try {
                    final EntityModel currentEntityModel = selector.getCurrentEntity();
                    final Pid pid = currentEntityModel == null ? null : currentEntityModel.getPid();
                    asyncReader.writeEntitiesToGroupModel();
                    clearSpans();
                    controller.model.updateAll();
                    try {
                        idx = (pid == null) ? -1 : controller.model.findEntity(null, pid);
                    } catch (ServiceClientException | InterruptedException exception) {//NOPMD
                        //never thrown because no data read here
                    }
                    if (idx >= 0) {                        
                        final QModelIndex newIndex = model().index(idx, column);
                        controller.setItemDelegateCurrentIndex(newIndex);
                        controller.forcedEnterEntity(newIndex);
                    } else if (model().rowCount() > 0) {
                        final QModelIndex newIndex = model().index(0, column);
                        controller.setItemDelegateCurrentIndex(newIndex);
                        controller.enterEntity(newIndex);
                    }
                    
                    if (model().rowCount(null)==0 && getGroupModel().getEntitiesCount()>0){
                        disableFetchMoreOnInsertRows = true;
                        try{
                            controller.model.fetchMore(null);
                            controller.model.increaseRowsLimit();
                        }finally{
                            disableFetchMoreOnInsertRows = false;
                        }
                        return;
                    }
                    
                } finally {                    
                    unlockInput(false);                    
                    switch (asyncReader.getScrollPosition()) {
                        case CURRENT:
                            if (currentIndex != null) {
                                final QModelIndex newTopIndex = currentIndex.sibling(currentIndex.row() - difference, currentIndex.column());
                                controller.view.scrollTo(newTopIndex, ScrollHint.PositionAtTop);
                            }
                            break;
                        case TOP:
                            if (selector.getCurrentEntity() != null && currentIndex!=null) {
                                controller.enterEntity(currentIndex);
                            }
                            controller.view.scrollToTop();
                            break;
                        case BOTTOM:
                            controller.view.scrollToBottom();
                            break;
                    }
                }
                selector.refresh();
            }
        }
    };

    @Override
    public void lockInput() {
        controller.lockInput();
    }

    @Override
    public void unlockInput() {
        controller.unlockInput();
    }
        
    private void unlockInput(final boolean setFocus) {
        controller.unlockInput(setFocus);
    }    

    @Override
    protected void paintEvent(final QPaintEvent event) {
        if (controller!=null && controller.isLocked() && !ExplorerMessageBox.isSomeMessageBoxActive()) {
            if (!postedEvents.contains(EventType.PAINT)){
                postedEvents.add(EventType.PAINT);
                controller.postEventAfterUnlock(new SelectorGridEvent(EventType.PAINT));
            }
        }else if (sizeHintCalculating){
            if (!postedEvents.contains(EventType.PAINT)){
                postedEvents.add(EventType.PAINT);
                QApplication.postEvent(this, new SelectorGridEvent(EventType.PAINT));
            }
        } else {
            try {
                if (itemDelegate() instanceof SelectorWidgetItemDelegate) {
                    final ClientSettings settings = selector.getEnvironment().getConfigStore();
                    settings.beginGroup(SettingNames.SYSTEM);
                    settings.beginGroup(SettingNames.SELECTOR_GROUP);
                    settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
                    final int sliderValue = settings.readInteger(SettingNames.Selector.Common.SLIDER_VALUE);
                    settings.endGroup();
                    settings.endGroup();
                    settings.endGroup();

                    ((SelectorWidgetItemDelegate) itemDelegate()).setEvenRowBgColorFactor(sliderValue);
                }
                controller.model.setTextOptionsCacheEnabled(true);
                super.paintEvent(event);
                controller.model.setTextOptionsCacheEnabled(false);
            } catch (RuntimeException exception) {
                selector.getEnvironment().getTracer().error(exception);
            }
        }
    }

    private GroupModel getGroupModel() {
        return controller.model.getRootGroupModel();
    }

    private boolean canProcessEvent(final QEvent.Type eventType) {
        if (controller != null) {
            if (eventType == QEvent.Type.MetaCall) {
                return !controller.isLocked() && !inSearchState() && isEnabled() && !selector.getEnvironment().getEasSession().isBusy();
            } else if (eventType == QEvent.Type.Resize || eventType == QEvent.Type.Timer) {
                return !controller.isLocked() && !inSearchState() && !selector.getEnvironment().getEasSession().isBusy();
            }
        }
        return true;
    }
    
    public boolean filterQtEvent(final QEvent event){
        if (canProcessEvent(event.type())){
            return false;
        }else if (controller != null && controller.isLocked() && event instanceof QResizeEvent) {
            final QResizeEvent resizeEvent = (QResizeEvent) event;
            controller.postEventAfterUnlock(new QResizeEvent(resizeEvent.size(), resizeEvent.oldSize()));
        } else if (event instanceof QTimerEvent) {
            final int timerId = ((QTimerEvent) event).timerId();
            killTimer(timerId);
            if (controller!=null && controller.isLocked()){
                controller.postEventAfterUnlock(new QTimerEvent(timerId));
            }else if (selector.getEnvironment().getEasSession().isBusy()){
                Application.processEventWhenEasSessionReady(this, new QTimerEvent(timerId));
            }else if (inSearchState()){
                scheduler.scheduleEvent(new QTimerEvent(timerId));
            }
        }
        return true;        
    }

    @Override
    protected void customEvent(final QEvent event) {
       if (event instanceof FetchMoreEvent){
            event.accept();
            if (postedEvents.contains(EventType.FETCH_MORE)){
                postedEvents.remove(EventType.FETCH_MORE);
                if (model()!=null){
                    fetchMore(((FetchMoreEvent)event).silentlyFetch());
                }
            }
        } else if (event instanceof SelectorGridEvent){
            event.accept();
            final EventType type = ((SelectorGridEvent)event).getType();
            switch(type){
                case PAINT:{
                    processPostponedPaintEvent();
                    break;
                }
                case REREAD:{
                    processRereadEvent();
                    break;
                }
                case SELECTION_CHANGED:{
                    processSelectionChangedEvent();
                    break;
                }
            }
        } else if (event instanceof PostponedMousePressEvent){
            event.accept();
            processPostponedMouseClickEvent((PostponedMousePressEvent)event);
        } else if (event instanceof FilteredMouseEvent){
            event.accept();
            processFilteredMouseEvent(new FilteredMouseEvent((FilteredMouseEvent)event, viewport()));
        }  else{
            super.customEvent(event);
        }
    }
    
    private boolean postSelectorGridEvent(final EventType eventType){
        if (postedEvents.contains(eventType)){
            return false;
        }else{
            postedEvents.add(eventType);
            Application.processEventWhenEasSessionReady(this, new SelectorGridEvent(eventType));
            return true;
        }
    }
    
    private void processSelectionChangedEvent(){
        if (postedEvents.contains(EventType.SELECTION_CHANGED)){
            postedEvents.remove(EventType.SELECTION_CHANGED);
            updateHeaderCheckState();
            if (isSelectionAllowed()){
                updateSelectionMode();
                final int lastRow = model().rowCount();
                final int lastColumn = model().columnCount();        
                dataChanged(controller.model.index(0, 0), controller.model.index(lastRow-1, lastColumn-1));
            }            
        }
    }
    
    private void processRereadEvent(){
        if (postedEvents.contains(EventType.REREAD)){            
            if (controller.isLocked() || controller.changingCurrentEntity()){                
                postSelectorGridEvent(EventType.REREAD);
            }else {
                executePostponedReread();
            } 
        }
    }
    
    private boolean processPostponedPaintEvent(){
        if (postedEvents.contains(EventType.PAINT)){
            if (controller.isLocked()) {                
                controller.postEventAfterUnlock(new SelectorGridEvent(EventType.PAINT));
                return false;
            }else if (sizeHintCalculating){                
                QApplication.postEvent(this, new SelectorGridEvent(EventType.PAINT));
                return false;
            } else {
                postedEvents.remove(EventType.PAINT);
                repaint();
                return true;
            }            
        }else{
            return false;
        }
    }
    
    private void executePostponedReread(){
        try{
            rereadAndSetCurrent((Pid)null);
        }catch(InterruptedException exception){
            //ignore
        }catch(ServiceClientException exception){
            if (ClientException.isSystemFault(exception)) {
                selector.getEnvironment().processException(exception);
            } else {
                final String title = selector.getEnvironment().getMessageProvider().translate("ExplorerException", "Error on receiving data");
                selector.getEnvironment().getTracer().error(title, exception);
            }
        }finally{
            postedEvents.remove(EventType.REREAD);
        }
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public SelectorGrid(final Selector parentView, final SelectorModel model) {
        super(parentView,new VerticalHeaderFactory());
        this.selector = parentView;        
        final List<QObject> children = new LinkedList<>(children());
        final IClientEnvironment environment = selector.getEnvironment();
        createHorizontalHeader(model);
        controller = new StandardSelectorWidgetController(model, this, selector);
        final MessageProvider messageProvider = environment.getMessageProvider();
        
        final String confirmRestoringPositionMessage =
                messageProvider.translate("Selector", "Number of loaded objects is %1s.\nDo you want to continue restoring position?");
        objectFinder = new SelectorModelDataLoader(environment);        
        objectFinder.setConfirmationMessageText(confirmRestoringPositionMessage);
        objectFinder.setProgressHeader(messageProvider.translate("Selector", "Restoring Position"));
        objectFinder.setProgressTitleTemplate(messageProvider.translate("Selector", "Restoring Position...\nNumber of Loaded Objects: %1s"));
        objectFinder.setDontAskButtonText(messageProvider.translate("Selector", "Load All Required Objects"));
        actions = new Actions(environment);
        verticalHeader().setHighlightSections(true);
        ((VerticalHeader)verticalHeader).setSymbolFontMetrics(cornerButton.getSymbolFontMetrics());        
        for (QObject child : children) {
            if (child instanceof QAbstractButton) {
                ((QWidget) child).setVisible(false);//hide standard corner widget
                break;
            }
        }
        
        final QWidget viewport = viewport();
        if (viewport != null) {
            viewport.installEventFilter(filteredMouseEventListener);
        }
        
        cornerButton.clicked.connect(this,"onCornerButtonClick()");
        
        final QRect vg = viewport().geometry();
        cornerButton.setFixedPos( (isRightToLeft() ? vg.right() + 1 : vg.left()), vg.top() );
        setWordWrap(false);
        setModel(controller.model);
        eventFilter.setProcessableEventTypes(EnumSet.of(QEvent.Type.MetaCall, QEvent.Type.Timer, QEvent.Type.Resize));
    }
    
    private SelectorHorizontalHeader createHorizontalHeader(final SelectorModel model){
        horizontalHeader = new SelectorHorizontalHeader(this, model,  false);
        setHorizontalHeader(horizontalHeader);
        return horizontalHeader;
    }
    
    @SuppressWarnings("unused")
    private void onVHeaderDoubleClicked(final int section) {
        controller.processRowHeaderDoubleClicked(controller.model.index(section, 0));
    }

    @SuppressWarnings("unused")
    private void onHHeaderClicked(final int section) {
        controller.processColumnHeaderClicked(section);
    }    

    @SuppressWarnings("unused")
    private void onCornerButtonClick(){
        controller.switchSelectionMode(horizontalHeader);
    }    
    
    @SuppressWarnings("unused")
    private void onHHeaderCheckBoxClick(){
        controller.invertSelection(horizontalHeader, EHierarchicalSelectionMode.EXPLICIT_NESTED_OBJECTS);
    }
    
    @Override
    public void bind() {
        {
            final GroupModelAsyncReader asyncReader = getGroupModel().getAsyncReader();
            asyncReader.addListener(asyncReaderListener);
        }
        final List<SelectorColumnModelItem> columns = controller.model.getSelectorColumns();
        {            
            horizontalHeader.restoreSettings();
            if (horizontalHeader.count() != columns.size()) {                
                createHorizontalHeader(controller.model);
            }
            horizontalHeader.checkBoxClicked.connect(this,"onHHeaderCheckBoxClick()");            
        }
        controller.setupSelectionColumnDelegate();
        horizontalHeader.bind();        
        {//set current row after horizontal header was binded
            if (!selector.isDisabled() && (!getGroupModel().isEmpty() || getGroupModel().hasMoreRows())) {
                restorePosition();
            } else {
                selector.refresh();
            }
        }        
        updateHeaderCheckState();
        {
            updateCurrentColumn();//RADIX-2513
            updateSpan(0, getGroupModel().getEntitiesCount() - 1);
            doubleClicked.connect(controller, "processDoubleClick(QModelIndex)");
            Application.getInstance().getActions().settingsChanged.connect(this, "applySettings()");
            horizontalHeader.sectionMoved.connect(this, "onSectionMoved(int, int, int)");
            horizontalHeader.sectionResized.connect(this, "finishEdit()");
            horizontalHeader.sectionClicked.connect(this, "onHHeaderClicked(int)");
            horizontalHeader.sectionVisibilityChanged.connect(this, "onSectionVisibilityChanged(int , boolean)");
            horizontalHeader.resizeColumnByContent.connect(this,"onResizeColumnByContent(int)");            
            controller.model.increaseRowsLimit();
        }
        getSelection().addListener(selectionHandler);
        controller.setSelectionModeListener(new StandardSelectorWidgetController.ISelectionModeListener() {
            @Override
            public void afterSwitchSelectionMode(boolean isSelectionEnabled) {
                SelectorGrid.this.afterSwitchSelectionMode(isSelectionEnabled);
            }
        });        
        verticalHeader.sectionDoubleClicked.connect(this,"onVHeaderDoubleClicked(int)");
        if (selector.isDisabled()) {
            clear();
        }
        
        cornerButton.setFixedSize(((VerticalHeader)verticalHeader()).getWidth(), getHorizontalHeaderHeight());
        
        verticalHeader().blockSignals(false);
        horizontalScrollBar().setValue(0);
        controller.model.setDefaultAdditionalSelectionModes(Collections.<EHierarchicalSelectionMode>emptyList());
        
        if (!selector.isDisabled()) {
            updateSelectionMode();
        }        
    }

    @Override
    public void refresh(final ModelItem item) {        
        if (item instanceof Property) {
            final Property property = (Property) item;
            controller.refresh(property);
            final QModelIndex index = controller.findIndexForProperty(property);
            if (index != null) {
                updateColumnsWidth();
            }
        }
        if (item == null && model() != null) {
            controller.refresh();
            controller.refreshCornerWidget(cornerButton, isSelectionEnabled(), isSelectionAllowed());
            controller.updateHeaderCheckState(horizontalHeader, canSelectAll());
        }
        actions.refresh();
    }
    
    @SuppressWarnings("unused")
    private void onSectionVisibilityChanged(final int sectionIndex, final boolean isVisible){        
        if (!isVisible && currentIndex() != null && currentIndex().column() == sectionIndex) { //RADIX-2513                
            updateCurrentColumn();
        }
        horizontalHeader.updateFirstVisibleColumnIndex();
        controller.lockInput();
        try {
            clearSpans();
            updateSpan(0, getGroupModel().getEntitiesCount());
        } finally {
            controller.unlockInput();
        }        
    }
    
    @SuppressWarnings("unused")
    private void onResizeColumnByContent(int sectionIndex){        
        final int width = Math.max(calcSizeHintForColumn(sectionIndex), horizontalHeader.sectionSizeFromContents(sectionIndex).width());
        if (width>0){
            horizontalHeader.resizeSection(sectionIndex, width);
            final SelectorColumnModelItem column = controller.model.getSelectorColumn(sectionIndex);
            if (column!=null && column.getColumnDef().getSizePolicy()==ESelectorColumnSizePolicy.RESIZE_BY_CONTENT){
                column.setSizePolicy(ESelectorColumnSizePolicy.RESIZE_BY_CONTENT);
            }                  
        }        
    }
    
    private boolean isSelectionAllowed(){
        return !selector.getGroupModel().getRestrictions().getIsMultipleSelectionRestricted() 
            && isEnabled()
            && !selector.getGroupModel().isEmpty();        
    } 
        
    private void updateHeaderCheckState(){        
        controller.updateHeaderCheckState(horizontalHeader, canSelectAll());
        if (isSelectionEnabled()){
            final int rowsCount = model().rowCount();
            if (rowsCount>0){
                dataChanged(model().index(0, 0), model().index(rowsCount-1, 0));
            }
        }        
        actions.refreshExportActionToolTip();
    } 
    
    private void updateCurrentColumn() {//set current index to first visible column
        final QModelIndex currentIndex = currentIndex();
        if (currentIndex != null) {//update current column index
            final int column = horizontalHeader.getFirstVisibleColumnIndex();
            final QModelIndex newIndex = controller.model.index(currentIndex.row(), column, null);
            setCurrentIndex(newIndex);
            controller.setItemDelegateCurrentIndex(newIndex);
        }
    }   
    
    private int getCurrentColumn(){
        final QModelIndex currentIndex = currentIndex();
        return currentIndex==null ? horizontalHeader.getFirstVisibleColumnIndex() : currentIndex.column();
    }

    private void updateSpan(final int startRow, final int endRow) {
        final int columnsCount = horizontalHeader.count();
        for (int row = startRow; row <= endRow; row++) {
            if (controller.model.isBrokenEntity(controller.model.index(row, 0)) && columnsCount > 1) {
                setSpan(row, controller.model.getFirstVisibleColumnIndex(), 1, columnsCount);
            } else if (columnSpan(row, controller.model.getFirstVisibleColumnIndex()) > 1) {
                setSpan(row, controller.model.getFirstVisibleColumnIndex(), 1, 1);
            }
        }
    }

    @SuppressWarnings("unused")
    private void onSectionMoved(final int logicalIndex, final int oldVisualIndex, final int newVisualIndex) {
        finishEdit();
        int firstVisualIndex = 0;
        for (int col = 0, columnsCount = horizontalHeader.count(); col <= columnsCount; col++) {
            int idx = horizontalHeader.logicalIndex(col);//logical index of column
            if (idx >= 0 && !horizontalHeader.isSectionHidden(idx)) {
                firstVisualIndex = col;
                break;
            }
        }

        if (oldVisualIndex == firstVisualIndex || newVisualIndex == firstVisualIndex) {
            horizontalHeader.updateFirstVisibleColumnIndex();            
            controller.lockInput();
            try {
                clearSpans();
                updateSpan(0, getGroupModel().getEntitiesCount());
            } finally {
                controller.unlockInput();
            }
            update();
        }
    }

    @Override
    public void setupSelectorToolBar(final IToolBar toolBar) {
        final Action createAction = selector.getActions().getCreateAction();
        toolBar.insertAction(createAction, actions.beginAction);
        toolBar.insertAction(createAction, actions.prevAction);
        toolBar.insertAction(createAction, actions.nextAction);
        toolBar.insertAction(createAction, actions.endAction);
        toolBar.insertAction(createAction, actions.findAction);
        toolBar.addAction(actions.exportAction);
        toolBar.getWidgetForAction(actions.exportAction).setPopupMode(IToolButton.ToolButtonPopupMode.InstantPopup);
        QMenu exportMenu = new QMenu(this);
        exportMenu.addAction(actions.exportCsvAction);
        exportMenu.addAction(actions.exportExcelAction);
        actions.exportAction.setMenu(exportMenu);
        toolBar.addAction(actions.calcStatisticAction);
    }

    @Override
    public void setupSelectorMenu(final IMenu menu) {
        final Action createAction = selector.getActions().getCreateAction();
        menu.insertAction(createAction, actions.beginAction);
        menu.insertAction(createAction, actions.prevAction);
        menu.insertAction(createAction, actions.nextAction);
        menu.insertAction(createAction, actions.endAction);
        menu.insertAction(createAction, actions.findAction);
        menu.insertAction(createAction, actions.findNextAction);
        menu.insertSeparator(createAction);
        final Action showFilterAndOrderToolBarAction =
                selector.getActions().getShowFilterAndOrderToolBarAction();
        final List<QAction> allActions = ((QMenu) menu).actions();
        final int showFilterAndOrderToolBarActionIndex = allActions.indexOf(showFilterAndOrderToolBarAction);
        if (showFilterAndOrderToolBarActionIndex < 1 || showFilterAndOrderToolBarActionIndex == allActions.size() - 1) {
            menu.addAction(actions.exportCsvAction);
            menu.addAction(actions.exportExcelAction);
            menu.addAction(actions.calcStatisticAction);
        } else {
            final QAction beforeAction = allActions.get(showFilterAndOrderToolBarActionIndex + 1);
            ((QMenu) menu).insertAction(beforeAction, actions.exportExcelAction);
            ((QMenu) menu).insertAction(actions.exportExcelAction, actions.exportCsvAction); 
            ((QMenu) menu).insertAction(actions.exportCsvAction, actions.calcStatisticAction);
        }
    }

    private void restorePosition() {
        /*        final ExplorerSettings settings = Environment.getConfigStore();
         final String key = getGroupModel().getConfigStoreGroupName() + "/" + SettingNames.SYSTEM + "/" + LAST_SELECTED_KEY_NAME;
         if (settings.contains(key)){
         final String pidAsStr = settings.readString(key, "");
         if (!pidAsStr.isEmpty()){
         final Id tableId = getGroupModel().getSelectorPresentationDef().getTableId();
         final Pid pid = new Pid(tableId, pidAsStr);
         final int idx = controller.model.findEntity(null,pid);

         if (idx>=0){
         controller.enterEntity(model().index(idx, 0));
         }
         else if (model().rowCount()>0){
         controller.enterEntity(model().index(model().rowCount()-1, 0));
         }

         }
         else{
         selectFirstRow();
         }
         }
         else{
         selectFirstRow();
         }        */
        if (model().rowCount(null)>0){//case when data was loaded into GroupModel before opening selector
            scheduleFetchMore(false);
        }
        selectFirstRow();
    }

    private void savePosition() {
        /*        if (getGroupModel() != null) {            
         final QModelIndex currentIndex = currentIndex();
         if (currentIndex != null) {
         final EntityModel entity = controller.model.getEntity(currentIndex);
         if (entity != null) {
         final ClientSettings settings = selector.getEnvironment().getConfigStore();
         final String key = getGroupModel().getConfigStoreGroupName() + "/" + SettingNames.SYSTEM + "/" + LAST_SELECTED_KEY_NAME;
         settings.writeString(key, entity.getPid().toString());
         }
         }
         }*/
    }

    @SuppressWarnings("unused")
    private void onFindAction() {
        enterSearchState();
        try {
            controller.showFindDialog();
        } finally {
            exitSearchState();
        }
    }

    @SuppressWarnings("unused")
    private void onFindNextAction() {
        enterSearchState();
        try {
            controller.findNext();
        } finally {
            exitSearchState();
        }
    }

    public void selectNextRow() {
        if (currentIndex() != null) {
            setCurrentRow(currentIndex().row() + 1);
        }
    }

    public void selectPrevRow() {
        if (currentIndex() != null) {
            setCurrentRow(currentIndex().row() - 1);
        }
    }

    public void selectFirstRow() {
        setCurrentRow(0);
    }

    public void selectLastRow() {
        if (!controller.isLocked()) {
            final MessageProvider messageProvider  = selector.getEnvironment().getMessageProvider();
            final String loadingDialogTitle =  messageProvider.translate("Selector", "Moving to Last Object");
            final String loadingDialogMessage =  
                messageProvider.translate("Selector", "Moving to Last Object...\nNumber of Loaded Objects: %1s");
            final String dontAskText = messageProvider.translate("Selector", "Load All Objects");            
            final int loadedRows = loadAllRows(loadingDialogTitle, loadingDialogMessage, dontAskText);
            if (loadedRows > 0 && selector.leaveCurrentEntity(false)) {
                setCurrentRow(loadedRows - 1);
            }
        }
    }
    
    private int loadAllRows(final String title, final String message, final String dontAskBtnText){
        final IClientEnvironment environment = selector.getEnvironment();
        final SelectorModelDataLoader allDataLoader = new SelectorModelDataLoader(environment);
        final String confirmMovingToLastObjectMessage =
            environment.getMessageProvider().translate("Selector", "Number of loaded objects is %1s.\nDo you want to load next %2s objects?");
        allDataLoader.setConfirmationMessageText(confirmMovingToLastObjectMessage);
        allDataLoader.setProgressHeader(title);
        allDataLoader.setProgressTitleTemplate(message);
        allDataLoader.setDontAskButtonText(dontAskBtnText);
        final int rowsLoadingLimit =
            environment.getConfigStore().readInteger(ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH, 1000);        
        allDataLoader.setLoadingLimit(rowsLoadingLimit);
        allDataLoader.resetCounters();
        controller.lockInput();
        disableFetchMoreOnInsertRows = true;
        int loadedRows = -1;        
        try {
            loadedRows = allDataLoader.loadAll(new SelectorWidgetDelegate(controller.model, null));
        } catch (InterruptedException exception) {
            loadedRows = controller.model.rowCount();
        } catch (ServiceClientException exception) {
            controller.model.showErrorOnReceivingData(exception);
            loadedRows = -1;
        } finally {
            disableFetchMoreOnInsertRows = false;
            if (loadedRows > 0) {
                controller.model.increaseRowsLimit();
            }            
            controller.unlockInput();
        }
        return loadedRows;
    }

    @SuppressWarnings("unused")
    private void export() {
        final GroupModelCsvWriter writer = new GroupModelCsvWriter(getGroupModel()) {
            @Override
            protected ISelectorDataExportOptionsDialog createExportOptionsDialog() {
                return new SelectorDataExportOptionsDialog(getGroupModel(), SelectorGrid.this, EMimeType.CSV,
                        EnumSet.of(SelectorDataExportOptionsDialog.Features.ENCODING));
            }
        };
        disableFetchMoreOnInsertRows = true;
        try{
            writer.write(selector);
        }finally{
            disableFetchMoreOnInsertRows = false;
        }
    }
    
    @SuppressWarnings("unused")
    private void exportExcel() {
        final GroupModelXlsxWriter writer = new GroupModelXlsxWriter(getGroupModel()) {
            @Override
            protected ISelectorDataExportOptionsDialog createExportOptionsDialog() {
                return new SelectorDataExportOptionsDialog(getGroupModel(), SelectorGrid.this, EMimeType.APP_MSOFFICE_SPREADSHEET_X,
                        EnumSet.of(SelectorDataExportOptionsDialog.Features.OPENFILESETTING, SelectorDataExportOptionsDialog.Features.TIMEZONEFORMAT));
            }
        };
        disableFetchMoreOnInsertRows = true;
        try{
            File file = writer.write(selector);
            if (file != null && writer.getOptions().getNeedToOpen()) {
                QDesktopServices.openUrl(QUrl.fromUserInput(file.getAbsolutePath()));
            }
        }finally{
            disableFetchMoreOnInsertRows = false;
        }
    }
    
    protected List<AggregateFunctionCall> getSelectionStatisticParams(){
        final List<Id> visibleColumnsByOrder = getVisibleColumnsOrder();
        final List<Id> compatibleColumns = 
            SelectorColumnsStatistic.getCompatibleColumns(getGroupModel(), visibleColumnsByOrder);
        if (compatibleColumns.isEmpty()){            
            return Collections.<AggregateFunctionCall>singletonList(new AggregateFunctionCall(null, EAggregateFunction.COUNT));
        }
        final SelectionStatisticParamsDialog paramsDialog = 
            new SelectionStatisticParamsDialog(getGroupModel(), this, compatibleColumns);
        if (paramsDialog.exec()==QDialog.DialogCode.Accepted.value()){
            return paramsDialog.getAggregateFunctions();
        }else{
            return Collections.emptyList();
        }
    }
    
    protected boolean confirmToCalcStatistic(final List<AggregateFunctionCall> functionCalls){
        final IClientEnvironment environment = selector.getEnvironment();
        final String confirmationTitle =
                environment.getMessageProvider().translate("Selector", "Confirm to Proceed Operation");
        final String confirmationMessage =
                environment.getMessageProvider().translate("Selector", "This operation may take a lot of time.\nDo you really want to proceed the operation?");
        return environment.messageConfirmation(confirmationTitle, confirmationMessage);        
    }
    
    protected void showSelectionStatistic(final List<AggregateFunctionCall> functionCalls, final SelectorColumnsStatistic statistic){
        final IClientEnvironment environment = selector.getEnvironment();
        if (statistic.getAggregateFunctions().isEmpty()){
            if (statistic.getRowsCount()>-1){
                final String rowCountText = 
                    SelectorColumnsStatistic.getAggregationFunctionTitle(EAggregateFunction.COUNT, environment.getMessageProvider())
                    +": "+String.valueOf(statistic.getRowsCount());
                final String messageTitle = environment.getMessageProvider().translate("Selector", "Statistics for Selection");
                environment.messageInformation(messageTitle, rowCountText);
            }
        }else{
            final Map<Id,Integer> precisionByColumnId = new HashMap<>();
            for (AggregateFunctionCall function: functionCalls){
                if (function.getPrecision()>-1){
                    precisionByColumnId.put(function.getColumnId(), function.getPrecision());
                }
            }            
            final SelectionStatisticResultDialog dialog = 
                new SelectionStatisticResultDialog(getGroupModel(), this, statistic, getVisibleColumnsOrder(), precisionByColumnId, true);
            dialog.exec();
        }
    }
    
    protected final List<Id> getVisibleColumnsOrder(){
        final List<Id> columnIds = new LinkedList<>();
        for (int i=1; i<horizontalHeader.count(); i++){
            final int columnIndex = horizontalHeader.logicalIndex(i);
            final Id columnId = 
                (Id)controller.model.headerData(columnIndex, Qt.Orientation.Horizontal, Qt.ItemDataRole.UserRole);
            columnIds.add(columnId);
        }
        return columnIds;
    }
    
    public void calcSelectionStatistic(){
        final List<AggregateFunctionCall> aggregateFunctions = getSelectionStatisticParams();
        if (aggregateFunctions!=null 
            && !aggregateFunctions.isEmpty() 
            && (!getGroupModel().hasMoreRows() || confirmToCalcStatistic(aggregateFunctions))){
            final SelectorColumnsStatistic statistic;
            try{
                statistic = getGroupModel().calcStatistic(aggregateFunctions);
            }catch(ServiceClientException ex){
                getGroupModel().showException(ex);
                return;
            }catch(InterruptedException ex){
                return;
            }
            showSelectionStatistic(aggregateFunctions, statistic);
        }
    }

    @Override
    public void entityRemoved(final Pid pid) {
        controller.processEntityRemoved(pid);
        controller.lockInput();
        try {
            clearSpans();
            updateSpan(0, getGroupModel().getEntitiesCount() - 1);
            updateSelectionMode();
            updateColumnsWidth();
        } finally {
            controller.unlockInput();
        }
    }

    public void setCurrentRow(final int row) {
        final int column;
        final QModelIndex currentIndex = currentIndex();
        if (currentIndex== null){
            column = horizontalHeader.getFirstVisibleColumnIndex();            
        }else{
            column = currentIndex.column();
        }          
        try {
            controller.setCurrentRow(row, column);
        } catch (InterruptedException exception) {
        } catch (ServiceClientException exception) {
            controller.processErrorOnReceivingData(exception);
        }
    }

    @Override
    protected void mousePressEvent(final QMouseEvent event) {        
        if (!processMousePressEvent(event)){
            processMouseClickEvent(event);
        }
    }
    
    private boolean processMousePressEvent(final QMouseEvent event){
        if (postedMousePressEventId!=0){
            Application.removeScheduledEvent(postedMousePressEventId);
            postedMousePressEventId = 0;            
        }
        final QModelIndex clickIndex = indexAt(event.pos());
        final GroupRestrictions restrictions = clickIndex==null ? null : getGroupModel().getRestrictions();
        final boolean canOpenModalEditor = 
            restrictions!=null && !restrictions.getIsRunEditorRestricted() && restrictions.getIsEditorRestricted();        
        if (canOpenModalEditor && isMouseClickWithCtrl(event)){
            final int row = clickIndex.row();
            final EntityModel clickModel = controller.model.getEntity(clickIndex);
            final boolean isBrokenModel = clickModel instanceof BrokenEntityModel;
            final boolean canOpenEntityView = 
                clickModel!=null && (isBrokenModel || clickModel.canOpenEntityView());            
            if (canOpenEntityView
                && (selector.getCurrentEntity()!=clickModel || selector.getActions().getRunEditorDialogAction().isEnabled())){
                final Pid clickPid = clickModel.getPid();
                if (postponedMousePressEvent==null){
                    postponeMouseClick(event, clickIndex, clickPid);
                }else{
                    killTimer(postponedMousePressEvent.getClickTimerId());
                    final QModelIndex postponedClickIndex = postponedMousePressEvent.getIndex(this);
                    if (postponedClickIndex!=null 
                        && postponedClickIndex.row()==row 
                        && Objects.equals(postponedMousePressEvent.getPid(), clickPid)){
                        postponedMousePressEvent = null;
                        if (selector.getCurrentEntity()!=clickModel) {
                            processMouseClickEvent(event);
                        }
                        if (isBrokenModel){
                            final BrokenEntityMessageDialog dialog = 
                                new BrokenEntityMessageDialog(selector.getEnvironment(), (BrokenEntityModel)clickModel, this);
                            dialog.exec();
                        }else if (selector.getActions().getRunEditorDialogAction().isEnabled()){
                            selector.getActions().getRunEditorDialogAction().trigger();
                        }
                    }else{
                        postponeMouseClick(event, clickIndex, clickPid);
                    }
                }
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }        
    }
    
    private static boolean isMouseClickWithCtrl(final QEvent event){
        if (event instanceof QMouseEvent){
            final QMouseEvent mouseEvent = (QMouseEvent)event;
            return mouseEvent.button()==Qt.MouseButton.LeftButton
                       && mouseEvent.modifiers().isSet(Qt.KeyboardModifier.ControlModifier);
        }else{
            return false;
        }
    }

    @Override
    protected void mouseDoubleClickEvent(final QMouseEvent event) {
        if (processMousePressEvent(event)){
            event.accept();
        }else{
            postponedMouseEvent = null;
            super.mouseDoubleClickEvent(event);
        }
    }
    
    private void postponeMouseClick(final QMouseEvent clickEvent, final QModelIndex index, final Pid clickModelPid){
        clickEvent.accept();
        final int mouseClickTimerId = startTimer(QApplication.doubleClickInterval());
        postponedMousePressEvent = new PostponedMousePressEvent<>(clickEvent, new IndexInfo(index), clickModelPid, mouseClickTimerId);
    }           
    
    private void processFilteredMouseEvent(final FilteredMouseEvent event){
        if (model()==null){
            return;
        }
        final QEvent.Type type = event.getFilteredEventType();
        final QEvent.Type postponedType = 
            postponedMouseEvent==null ? null : postponedMouseEvent.getFilteredEventType();
        if ((type == QEvent.Type.MouseButtonPress && postponedType!=QEvent.Type.MouseButtonRelease)
            || (type == QEvent.Type.MouseButtonDblClick && postponedType!=QEvent.Type.MouseButtonPress)
            || (type == QEvent.Type.MouseButtonRelease && event.getButton()==Qt.MouseButton.LeftButton)) {            
            postponedMouseEvent = event;
        }        
    }
    
    @SuppressWarnings("unchecked")
    private void processPostponedMouseClickEvent(PostponedMousePressEvent event){
        if (postedMousePressEventId!=0){
            postedMousePressEventId = 0;
            processMouseClickEvent(event.getOriginalEvent());
            if (event.isEdit()){
                final QModelIndex clickIndex = event.getIndex(this);
                if (clickIndex!=null){
                    editImpl(clickIndex, QAbstractItemView.EditTrigger.NoEditTriggers, event.getEditEvent());
                }
            }
        }
    }  
    
    private void processMouseClickEvent(final QMouseEvent event){        
        postponedMouseEvent = null;
        final int verticalScroll = verticalScrollBar().value();
        final int horizontalScroll = horizontalScrollBar().value();        
        final PointInfo mousePressPoint = getPointInfo(event.pos());
        controller.processMousePressEvent(event, 
                                                             isSelectionAllowed(), 
                                                             isSelectionEnabled(), 
                                                             indexIteratorFactory);
        if (event.isAccepted()) {
            super.mousePressEvent(event);
        }
        
        if (postedEvents.contains(EventType.REREAD)){
            executePostponedReread();
        }
        if (postponedMouseEvent!=null && model()!=null){
            final QEvent.Type postponedMouseEventType = postponedMouseEvent.getFilteredEventType();
            if (postponedMouseEventType==QEvent.Type.MouseButtonDblClick){
                postponedMouseEvent = null;
                processPostponedMouseButtonDblClick();
            }else if (postponedMouseEventType==QEvent.Type.MouseButtonRelease){                
                final QMouseEvent mouseEvent = postponedMouseEvent.createFilteredEvent();
                postponedMouseEvent = null;
                if (verticalScroll<=verticalScrollBar().maximum()){
                    verticalScrollBar().setValue(verticalScroll);
                }else{
                    return;
                }
                if (horizontalScroll<=horizontalScrollBar().maximum()){
                    horizontalScrollBar().setValue(horizontalScroll);
                }else{
                    return;
                }
                processPostponedMouseButtonRelease(mousePressPoint, mouseEvent);
            }else{
                postponedMouseEvent = null;
            }
        }        
    }
    
    private void processPostponedMouseButtonDblClick(){        
        final QModelIndex index = currentIndex();
        if (index != null) {
            if (model().flags(index).isSet(Qt.ItemFlag.ItemIsEditable)) {
                if (!controller.inEditingMode()) {
                    controller.openEditor(index);
                }
            }
        }  
    }
    
    private void processPostponedMouseButtonRelease(final PointInfo mousePressPoint,
                                                                                 final QMouseEvent releaseEvent){        
        if (mousePressPoint.insideCheckBox()){
            final PointInfo mouseReleasePoint = getPointInfo(releaseEvent.pos());
            if (mouseReleasePoint.equals(mousePressPoint)){
                Application.processEventWhenEasSessionReady(viewport(), releaseEvent);
            }            
        }
    }    
    
    private PointInfo getPointInfo(final QPoint point){
        final QModelIndex index = indexAt(point);
        if (index==null){
            return PointInfo.UNKNOWN_POINT;
        }        
        final Property property = controller.model.getProperty(index);
        if (property==null){
            return new PointInfo(index, null, null, false);
        }
        Object cellValue;
        try{
            cellValue = property.getValueObject();
        }catch(ActivatingPropertyError ex){
            cellValue = ex;
        }
        final boolean pointInsideCheckBox;
        if (itemDelegate() instanceof SelectorWidgetItemDelegate){
            final SelectorWidgetItemDelegate itemDelegate = (SelectorWidgetItemDelegate)itemDelegate();
            final QStyleOptionViewItem options = viewOptions();
            options.setRect(visualRect(index));
            pointInsideCheckBox = itemDelegate.posInsideCheckbox(point, model(), options, index);                
        }else{
            pointInsideCheckBox = StandardSelectorWidgetController.getCheckState(model(), index)!=null;
        }
        return new PointInfo(index, property.getId(), cellValue, pointInsideCheckBox);
    }

    @Override
    protected void timerEvent(final QTimerEvent event) {
        if (postponedMousePressEvent!=null && event.timerId()==postponedMousePressEvent.getClickTimerId()){
            killTimer(event.timerId());
            postedMousePressEventId = postponedMousePressEvent.post(this);
            postponedMousePressEvent = null;
        }else{
            super.timerEvent(event);
        }
    }

    @Override
    protected void mouseMoveEvent(final QMouseEvent event) {
        if (event.buttons().isSet(Qt.MouseButton.RightButton) || event.buttons().isSet(Qt.MouseButton.LeftButton)){ //препятствуем перемещению мыши с нажатой кнопкой
            event.ignore();
        } else {
            super.mouseMoveEvent(event);
        }
    }

    @Override
    protected QModelIndex moveCursor(final CursorAction action, final Qt.KeyboardModifiers modifiers) {
        if (controller.canMoveCursor(action, state())) {
            final QModelIndex index = super.moveCursor(action, modifiers);
            return controller.afterMoveCursor(index, isSelectionAllowed(), isSelectionEnabled(), indexIteratorFactory, modifiers);
        }
        return null;
    }

    @Override
    protected void keyPressEvent(final QKeyEvent event) {
        final EHierarchicalSelectionMode selectAllMode = 
                isSelectionAllowed() ? EHierarchicalSelectionMode.EXPLICIT_NESTED_OBJECTS : null;
        if (!controller.processKeyPressEvent(event, selectAllMode, horizontalHeader)){
            if (event.matches(QKeySequence.StandardKey.Find)) {
                actions.findAction.trigger();
            } else if (event.matches(QKeySequence.StandardKey.FindNext)) {
                actions.findNextAction.trigger();
            } else if (event.key()==Qt.Key.Key_Insert.value()
                       && event.modifiers().value()==0
                       && isSelectionAllowed()
                       && currentIndex()!=null
                       && !controller.inEditingMode()
                       && controller.invertSelection(currentIndex())
                      ){
                moveCursor(CursorAction.MoveDown, new Qt.KeyboardModifiers(0));
            } else {
                super.keyPressEvent(event);
            }
        }
    }

    @Override
    protected void horizontalScrollbarValueChanged(final int value) {
        //do nothing. block loading data
    }

    @Override
    protected void currentChanged(final QModelIndex current, final QModelIndex previous) {
        finishEdit();
        ((VerticalHeader)verticalHeader).setCurrentRow(current==null ? -1 : current.row());
        controller.processCurrentChanged(current, previous, horizontalHeader);
        actions.refresh();        
    }

    @Override
    public void rereadAndSetCurrent(final Pid pid) throws InterruptedException, ServiceClientException {
        if (pid == null && currentIndex() != null) {
            final EntityModel entity = controller.model.getEntity(currentIndex());
            rereadInternal(Collections.singleton(entity.getPid()));
        } else {
            rereadInternal(pid==null ? null : Collections.singleton(pid));
        }        
    }

    @Override
    public void rereadAndSetCurrent(Collection<Pid> pids) throws InterruptedException, ServiceClientException {
        if (pids == null && currentIndex() != null) {
            final EntityModel entity = controller.model.getEntity(currentIndex());
            rereadInternal(Collections.singleton(entity.getPid()));
        } else {
            rereadInternal(pids==null ? null : pids);
        }
    }    

    @Override
    public void reread() throws InterruptedException, ServiceClientException {
        rereadInternal(null);
    }

    @Override
    protected boolean edit(final QModelIndex index, final EditTrigger trigger, final QEvent event/*can be null*/) {
        if (isMouseClickWithCtrl(event)){
            if (postponedMousePressEvent!=null){
                postponedMousePressEvent.setEdit((QMouseEvent)event);
                return false;
            }
        }
        return editImpl(index, trigger, event);
    }
    
    private boolean editImpl(final QModelIndex index, final EditTrigger trigger, final QEvent event){
        final boolean processed = 
            controller.processEditEvent(event, 
                                                     index, 
                                                     isSelectionEnabled(), 
                                                     indexIteratorFactory);
        if ((event==null || event.isAccepted())
            && super.edit(index, trigger, event)){
            if (!processed){
                update();
            }
            return true;
        }
        return processed;
    }
    
    private void afterSwitchSelectionMode(final boolean isSelectionEnabled){        
        update();
        if (isSelectionEnabled){
            resizeColumnToContents(0);
        }
        controller.refreshCornerWidget(cornerButton, isSelectionEnabled, isSelectionAllowed());
    }     
        
    @Override
    public void afterPrepareCreate(final EntityModel entity) {
    }

    private void rereadInternal(final Collection<Pid> pids) throws InterruptedException, ServiceClientException {
        if (controller.changingCurrentEntity()){
            postSelectorGridEvent(EventType.REREAD);
        }
        finishEdit();
        setEnabled(true);
        final int column = getCurrentColumn();
        lockInput();
        int idx = -1;
        try {
            clearSpans();            
            verticalScrollBar().setValue(0);//RADIX-9939 reset value to avoid redundant read on setRange call in updateGeometry.
            if (pids == null) {
                controller.model.reread(null);
            } else {
                controller.model.reset(null);
                final int rowsLoadingLimit =
                        selector.getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_FOR_RESTORING_POSITION_CONFIG_PATH, 300);
                objectFinder.setLoadingLimit(rowsLoadingLimit);
                objectFinder.resetCounters();
                enterSearchState();
                try {
                    idx = objectFinder.findObjectByPid(new SelectorWidgetDelegate(controller.model, null), pids);
                } catch (ServiceCallException exception) {
                    controller.processErrorOnReceivingData(exception);
                } finally {
                    exitSearchState();
                }                
            }
            if (idx >= 0) {
                controller.enterEntity(model().index(idx, column));
            } else if (model().rowCount() > 0) {
                controller.enterEntity(model().index(0, column));
            }
        } finally {
            //update second time to ignore loading in enterEntity method            
            controller.model.increaseRowsLimit();
            unlockInput();
        }        
        selector.refresh();
        updateSelectionMode();
        //RADIX-10273 (need in case of direct call readAndSetCurrent method)
        updateRowsSize();
        updateColumnsWidth();
    }
        
    private void enterSearchState(){
        searching = true;
    }
    
    private void exitSearchState(){
        searching = false;
        scheduler.postScheduledEvents(this);        
    }
    
    private boolean inSearchState(){
        return searching;
    }    

    @Override
    public void clear() {
        controller.clearData(this, cornerButton, horizontalHeader);
    }

    @Override
    public boolean setFocus(final Property property) {
        final QModelIndex index = controller.findIndexForProperty(property);
        if (index != null) {
            setCurrentIndex(index);
            controller.openEditor(index);
            final PropEditor editor = ((WrapModelDelegate) itemDelegate(index)).getActivePropEditor();
            if (editor != null) {
                editor.setFocus();
            }
            return true;
        }
        return false;
    }        

    @Override
    protected void rowsInserted(final QModelIndex parent, final int start, final int end) {        
        if ((!isVisible() || !verticalScrollBar().isVisible())
            && !controller.loadingRows() 
            && !disableFetchMoreOnInsertRows 
            && !inSearchState()){
            scheduleFetchMore(false);
        }else{
            updateEditorGeometries();
        }
        actions.refresh();
        updateSpan(start, end);
        if (start > 1) {
            updateRowsSize();
            updateColumnsWidth();
        }
    }

    //Блокировка контекстного поиска
    @Override
    public void keyboardSearch(String search) {
        controller.processKeyboardSearch();
    }

    @Override
    public final void applySettings() {
        controller.applySettings();
        update();
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {

        for (SelectorColumnModelItem column : controller.model.getSelectorColumns()) {
            column.unsubscribe(this);
        }

        getGroupModel().getAsyncReader().removeListener(asyncReaderListener);
        if (model()!=null){
            getSelection().removeListener(selectionHandler);
        }        
        controller.model.unsubscribeProperties(this);                
        {//disconnecting signals for GC
            cornerButton.disconnect();
            actions.close();
            horizontalHeader.disconnect();
        }
        
        horizontalHeader.saveSettings();
        horizontalHeader.close();
        if (model() != null) {
            if (selector.getModel() != null) {                
                savePosition();
            }
            //setModel(null);            
            getSelection().close();
            controller.model.clear();
            controller.model.dispose();
            getGroupModel().getAsyncReader().clean();
        }
        postedEvents.clear();
        super.closeEvent(event);
    }

    @Override
    public QSize sizeHint() {
        final QSize size = super.sizeHint();
        size.setHeight(DEFAULT_HEIGHT);        
        final int width = horizontalHeader.sizeHint().width()+(cornerButton.isVisible() ? cornerButton.width() : 0)+frameWidth()*2;
        if (size.width()<width){
            final Dimension sizeLimit = WidgetUtils.getWndowMaxSize();
            size.setWidth(Math.min(width, (int)sizeLimit.getWidth()));
        }
        return size;
    }            

    @Override
    public int sizeHintForRow(final int row) {
        sizeHintCalculating = true;
        try {
            final int columnCount = horizontalHeader.count();
            int rowSizeHint = 0;
            for (int column = columnCount - 1; column >= 0; --column) {
                int logical = horizontalHeader.logicalIndex(column);
                if (!horizontalHeader.isSectionHidden(logical)) {
                    final QModelIndex index = controller.model.index(row, column, null);
                    rowSizeHint = Math.max(rowSizeHint, controller.model.getHeightHint(index));
                }
            }
            return rowSizeHint;
        } finally {
            sizeHintCalculating = false;
        }
    }

    @Override
    public int sizeHintForColumn(final int column) {
        if (horizontalHeader.resizeMode(column) != QHeaderView.ResizeMode.ResizeToContents
                || horizontalHeader.isSectionHidden(column)) {
            sizeHintCalculating = true;
            try{
                return super.sizeHintForColumn(column);
            }finally{
                sizeHintCalculating = false;
            }
        }else{
            return calcSizeHintForColumn(column);
        }
    }
    
    private int calcSizeHintForColumn(final int column){
        sizeHintCalculating = true;
        try{
            final int rowCount = verticalHeader().count();
            int columnSizeHint = 0;
            for (int row = rowCount - 1; row >= 0; --row) {
                final QModelIndex index = controller.model.index(row, column, null);
                if (!controller.model.isBrokenEntity(index)) {
                    columnSizeHint =
                            Math.max(columnSizeHint, controller.model.getSizeHint(index, false).width());
                }
            }
            return columnSizeHint;            
        }finally {
            sizeHintCalculating = false;
        }
    }
    
    private int getHorizontalHeaderHeight(){
        final int height = Math.max(horizontalHeader.minimumHeight(), horizontalHeader.sizeHint().height());
        return Math.min(height, horizontalHeader.maximumHeight());
    }
    
    private void updateColumnsWidth(){        
        for (int i = 0, count = horizontalHeader.count(); i < count; i++) {
            if (!horizontalHeader.isSectionHidden(i)
                    && horizontalHeader.resizeMode(i) == QHeaderView.ResizeMode.ResizeToContents) {
                resizeColumnToContents(i);                
            }
        }        
    }        

    @Override
    protected void updateGeometries() {
        if (geometryRecursionBlock) {
            return;
        }
        geometryRecursionBlock = true;
        {
            int width = Math.max(verticalHeader.minimumWidth(), verticalHeader.sizeHint().width());
            width = Math.min(width, verticalHeader.maximumWidth());            
            final int height = getHorizontalHeaderHeight();
            final boolean reverse = isRightToLeft();
            if (reverse) {
                setViewportMargins(0, height, width, 0);
            } else {
                setViewportMargins(width, height, 0, 0);
            }

            // update headers

            final QRect vg = viewport().geometry();

            final int verticalLeft = reverse ? vg.right() + 1 : (vg.left() - width);
            verticalHeader.setGeometry(verticalLeft, vg.top(), width, vg.height());
            final int horizontalTop = vg.top() - height;
            horizontalHeader.setGeometry(vg.left(), horizontalTop, vg.width(), height);
            cornerButton.setVisible(width>0 && height>0);            
        }

        QSize vsize;
        final int horizontalLength;
        final int verticalLength;
        {
            vsize = viewport().size();
            final QSize max = maximumViewportSize();
            horizontalLength = horizontalHeader.length();
            verticalLength = verticalHeader.length();
            if (max.width() >= horizontalLength && max.height() >= verticalLength) {
                vsize = max;
            }
        }
        int columnsInViewport = 0;
        final int columnCount;
        {
            // horizontal scroll bar
            columnCount = horizontalHeader.count();
            final int viewportWidth = vsize.width();
            for (int width = 0, column = columnCount - 1; column >= 0; --column) {
                int logical = horizontalHeader.logicalIndex(column);
                if (!horizontalHeader.isSectionHidden(logical)) {
                    width += horizontalHeader.sectionSize(logical);
                    if (width > viewportWidth) {
                        break;
                    }
                    ++columnsInViewport;
                }
            }
        }
        {
            final QScrollBar horizontalScrollBar = horizontalScrollBar();
            if (horizontalScrollBar != null) {
                if (horizontalScrollMode() == QAbstractItemView.ScrollMode.ScrollPerItem) {
                    final int visibleColumns = columnCount - horizontalHeader.hiddenSectionCount();
                    horizontalScrollBar.setRange(0, visibleColumns - columnsInViewport);
                    horizontalScrollBar.setPageStep(columnsInViewport);
                    if (columnsInViewport >= visibleColumns) {
                        horizontalHeader.setOffset(0);
                    }
                    horizontalScrollBar.setSingleStep(1);
                } else { // ScrollPerPixel
                    horizontalScrollBar.setPageStep(vsize.width());
                    horizontalScrollBar.setRange(0, horizontalLength - vsize.width());
                    horizontalScrollBar.setSingleStep(Math.max(vsize.width() / (columnsInViewport + 1), 2));
                }
            }
        }

        final int rowCount;
        int rowsInViewport = 0;
        {
            // vertical scroll bar
            rowCount = verticalHeader.count();
            final int viewportHeight = vsize.height();
            for (int height = 0, row = rowCount - 1; row >= 0; --row) {
                height += verticalHeader.sectionSize(row);
                //height+=26;
                if (height > viewportHeight) {
                    break;
                }
                ++rowsInViewport;
            }
        }
        final QScrollBar verticalScrollBar;
        {
            verticalScrollBar = verticalScrollBar();
            if (verticalScrollBar != null) {
                if (verticalScrollMode() == QAbstractItemView.ScrollMode.ScrollPerItem) {
                    final int visibleRows = rowCount;
                    verticalScrollBar.setRange(0, visibleRows - rowsInViewport);
                    verticalScrollBar.setPageStep(rowsInViewport);
                    if (rowsInViewport >= visibleRows) {
                        verticalHeader.setOffset(0);
                    }
                    verticalScrollBar.setSingleStep(1);
                } else { // ScrollPerPixel
                    verticalScrollBar.setPageStep(vsize.height());
                    verticalScrollBar.setRange(0, verticalLength - vsize.height());
                    verticalScrollBar.setSingleStep(Math.max(vsize.height() / (rowsInViewport + 1), 2));
                }
            }
        }
        geometryRecursionBlock = false;
    }
    
    private boolean confirmToClearSelection(){
        final IClientEnvironment environment = selector.getEnvironment();
        final String title = environment.getMessageProvider().translate("ExplorerMessage", "Confirm to Clear Selection");
        final String message = environment.getMessageProvider().translate("ExplorerMessage", "Do you really want to clear selection?");
        return environment.messageConfirmation(title, message);
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @Override
    public boolean setMultiSelectionEnabled(final boolean enabled) {
        if (enabled){
            if (!isSelectionAllowed()){
                return false;
            }else{
                return controller.enableSelection(horizontalHeader);
            }
        }else{
            return controller.disableSelection(horizontalHeader);
        }
    }
    
    private boolean canSelectAll(){
        return !selector.getGroupModel().getRestrictions().getIsMultipleSelectionRestricted();
    }
    
    private boolean isSelectionEnabled(){
        return controller.isSelectionEnabled(horizontalHeader);
    }
    
    private void updateSelectionMode(){
        controller.updateSelectionMode(horizontalHeader, isSelectionAllowed());
    }
            
    private void scheduleFetchMore(final boolean silently){
        if (!postedEvents.contains(EventType.FETCH_MORE)){
            postedEvents.add(EventType.FETCH_MORE);
            Application.processEventWhenEasSessionReady(this, new FetchMoreEvent(silently));            
        }
    }        
    
    private void fetchMore(final boolean silently) {
        final QAbstractItemModel model = model();
        if (!model.canFetchMore(null))
            return;
        
        final int last = model.rowCount(null) - 1;
        
        final QRect viewPortRect = viewport().rect();
        if (last < 0) {            
            readMore((int)Math.ceil(viewPortRect.height()/24.0),silently);
        }else{                        
            for (int i=0,count=model.columnCount(); i<count; i++){
                final QModelIndex index = model.index(last, i, null);
                final QRect rect = visualRect(index);
                if (!rect.isEmpty() && viewPortRect.intersects(rect)){
                    final int emptyArea = viewPortRect.height()-rect.bottom();
                    readMore((int)Math.ceil(emptyArea/(float)rect.height()),silently);
                    break;
                }
            }
        }
    }
    
    private void readMore(final int bufferSize, final boolean silently){        
        final QAbstractItemModel model = model();
        final GroupModel groupModel = getGroupModel();
        final int rowsCount = model.rowCount(null)+(bufferSize<=0 ? 1 : bufferSize);
        if (groupModel.getEntitiesCount()>=rowsCount){
            model.fetchMore(null);
            return;
        }
        //preload data
        final IProgressHandle progressHandle;
        if (silently){
            progressHandle = null;
        }else{
            progressHandle = groupModel.getEnvironment().getProgressHandleManager().newStandardProgressHandle();
            final String message = 
                groupModel.getEnvironment().getMessageProvider().translate("Selector", "Loading data...");
            progressHandle.startProgress(message, true);
        }      
        try{
            EntityModel lastEntityModel = null;        
            do{
                try{
                    lastEntityModel = groupModel.getEntity(groupModel.getEntitiesCount());
                }catch(BrokenEntityObjectException exception){

                }catch(InterruptedException exception){
                    break;
                }catch(ServiceClientException exception){
                    controller.model.showErrorOnReceivingData(exception);
                    break;
                }
            }while(groupModel.getEntitiesCount()<rowsCount
                   && lastEntityModel!=null
                   && groupModel.hasMoreRows()
                   && (progressHandle==null || !progressHandle.wasCanceled()));
        }finally{
            if (progressHandle!=null){
                progressHandle.finishProgress();
            }
        }
        model.fetchMore(null);
        updateRowsSize();
    }
    
    protected final HierarchicalSelection<SelectorNode> getSelection(){
        return controller.model.getSelection();
    }
    
    @Override
    protected void disposed() {
        controller.disposed();
        super.disposed();
    }    
}
