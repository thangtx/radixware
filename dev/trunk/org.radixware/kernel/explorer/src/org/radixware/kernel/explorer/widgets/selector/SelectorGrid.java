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
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.Qt;
import org.radixware.kernel.explorer.widgets.ExplorerAction;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QAbstractItemView.CursorAction;
import com.trolltech.qt.gui.QAbstractItemView.EditTrigger;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontDatabase;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QMoveEvent;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QScrollBar;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionHeader;
import com.trolltech.qt.gui.QWidget;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.exceptions.FileException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.EntityObjectsSelection;
import org.radixware.kernel.common.client.models.EntityObjectsWriter;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelAsyncReader;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.PropertyValuesWriteOptions;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.client.widgets.selector.IMultiSelectionWidget;
import org.radixware.kernel.common.client.widgets.selector.ISelectorWidgetDelegate;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.common.client.widgets.selector.SelectorModelDataLoader;
import org.radixware.kernel.common.enums.ESelectionMode;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.env.ExplorerIcon;

import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.selector.Selector;
import org.radixware.kernel.explorer.widgets.AbstractGrid;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditor;

public class SelectorGrid extends AbstractGrid implements IExplorerSelectorWidget, IMultiSelectionWidget {
        
    private final static class QTableCornerButton extends QAbstractButton {

        private final static String WINDOWS_FONT_FAMILY="Arial Unicode MS";
        public final static char BUTTON_SYMBOL = '\u2714';
                
        private final QStyleOptionHeader styleOptionHeader;        
        private boolean fixedPos;
        private int posX, posY;
        private boolean clickEnabled = true;
        private final QFontMetrics fontMetrics;
        private final QFont font;
        private final QStyle.State state = new QStyle.State(0);

        @SuppressWarnings("LeakingThisInConstructor")
        public QTableCornerButton() {
            super();
            styleOptionHeader = new QStyleOptionHeader();
            styleOptionHeader.setPosition(QStyleOptionHeader.SectionPosition.OnlyOneSection);
            styleOptionHeader.initFrom(this);
            font = new QFont(font());            
            if (SystemTools.isWindows && (new QFontDatabase().hasFamily(WINDOWS_FONT_FAMILY))){
                font.setFamily(WINDOWS_FONT_FAMILY);
            }
            font.setStyleStrategy(QFont.StyleStrategy.PreferAntialias);
            fontMetrics = new QFontMetrics(font);            
        }        

        @Override
        protected void paintEvent(final QPaintEvent event) {
            styleOptionHeader.initFrom(this);
            state.clearAll();
            if (isEnabled()) {
                state.set(QStyle.StateFlag.State_Enabled);
            }
            if (isActiveWindow()) {
                state.set(QStyle.StateFlag.State_Active);
            }
            state.set(QStyle.StateFlag.State_Raised);
            styleOptionHeader.setState(state);
            styleOptionHeader.setRect(rect());
            if (clickEnabled){
                styleOptionHeader.setFontMetrics(fontMetrics);                                
                styleOptionHeader.setText(String.valueOf(BUTTON_SYMBOL));
            }else{
                styleOptionHeader.setText("");
            }
            styleOptionHeader.setTextAlignment(Qt.AlignmentFlag.AlignCenter);
            final QPainter painter = new QPainter(this);
            painter.setFont(font);
            try {
                style().drawControl(QStyle.ControlElement.CE_Header, styleOptionHeader, painter, this);                
            } finally {
                painter.end();
            }
        }
        
        public QFontMetrics getSymbolFontMetrics(){
            return fontMetrics;
        }
        
        public void setFixedPos(final int x, final int y){
            posX = x;
            posY = y;
            fixedPos = true;
        }
        
        @Override
        protected void moveEvent(final QMoveEvent event) {
            super.moveEvent(event);
            final QPoint pos = pos();
            if (fixedPos){
                if (pos.x()!=posX && pos.y()!=posY){
                    move(posX, posY);
                }
            }
        }
        
        public void setClickEnabled(final boolean isEnabled){
            if (isEnabled!=clickEnabled){
                clickEnabled = isEnabled;
                repaint();
            }
        }
        
        public boolean isClickEnabled(){
            return clickEnabled;
        }        
    }
    
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
                opt.setText(String.valueOf(QTableCornerButton.BUTTON_SYMBOL));
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
    
    private static final class FetchMoreEvent extends QEvent{        
        public FetchMoreEvent(){
            super(QEvent.Type.User);
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
        public final ExplorerAction endAction;
        public final ExplorerAction exportAction;

        public Actions(final IClientEnvironment environment) {
            final MessageProvider mp = environment.getMessageProvider();
            findAction = createAction(environment, Icons.FIND, mp.translate("Selector", "Find..."), "onFindAction()");
            findNextAction = createAction(environment, Icons.FIND_NEXT, mp.translate("Selector", "Find Next"), "onFindNextAction()");
            nextAction = createAction(environment, Icons.NEXT, mp.translate("Selector", "Next"), "selectNextRow()");
            prevAction = createAction(environment, Icons.PREVIOUS, mp.translate("Selector", "Previous"), "selectPrevRow()");
            beginAction = createAction(environment, Icons.BEGIN, mp.translate("Selector", "First"), "selectFirstRow()");
            endAction = createAction(environment, Icons.END, mp.translate("Selector", "Last"), "selectLastRow()");
            exportAction = createAction(environment, ClientIcon.Selector.EXPORT, mp.translate("Selector", "Export Selector Content"), "export()");
        }

        private ExplorerAction createAction(final IClientEnvironment environment,
                final ClientIcon icon,
                final String title,
                final String slot) {
            final ExplorerAction action =
                    new ExplorerAction(ExplorerIcon.getQIcon(icon), title, SelectorGrid.this);
            WidgetUtils.updateActionToolTip(environment, action);
            action.triggered.connect(SelectorGrid.this, slot);
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
            exportAction.setEnabled(rows > 0);
            refreshExportActionToolTip();
        }
        
        private void refreshExportActionToolTip(){
            final MessageProvider mp = controller.selector.getEnvironment().getMessageProvider();
            final EntityObjectsSelection selection = controller.selector.getGroupModel().getSelection();
            if (selection.isEmpty()){
                exportAction.setToolTip(mp.translate("Selector", "Export Selector Content"));
            }else if (selection.isSingleObjectSelected()){
                exportAction.setToolTip(mp.translate("Selector", "Export Selected Object"));
            }else{
                exportAction.setToolTip(mp.translate("Selector", "Export Selected Objects"));
            }
        }

        private void close() {
            findAction.close();
            findNextAction.close();
            prevAction.close();
            nextAction.close();
            beginAction.close();
            endAction.close();
        }
    }
    
    private static enum ESelectAction{SELECT,UNSELECT,INVERT};
    
    public final Actions actions;
    private final Selector selector;
    private final StandardSelectorWidgetController controller;
    private final SelectorModelDataLoader allDataLoader;
    private final SelectorModelDataLoader objectFinder;
    private final QTableCornerButton corner = new QTableCornerButton();
    private boolean searching, sizeHintCalculating;
    private QEventsScheduler scheduler = new QEventsScheduler();
    private SelectorHorizontalHeader horizontalHeader;
    private final static int DEFAULT_HEIGHT = 260;
    private boolean blockSelectionListener;
    private boolean geometryRecursionBlock;
    private boolean fetchMoreScheduled;
    private boolean disableFetchMoreOnInsertRows;
    
    private final GroupModel.SelectionListener selectionHandler = new GroupModel.SelectionListener(){
        @Override
        public void afterSelectionChanged(final EntityObjectsSelection selection) {
            if (!blockSelectionListener)
                updateHeaderCheckState();
        }
    };
    private final GroupModelAsyncReader.Listener asyncReaderListener =
            new GroupModelAsyncReader.Listener() {
        @Override
        public void readingWasFinished() {
            super.readingWasFinished();
            if (isEnabled()) {
                finishEdit();
                QModelIndex currentIndex = currentIndex();
                final QModelIndex topIndex = controller.view.indexAt(new QPoint(1, 1));
                final int difference = currentIndex == null ? 0 : currentIndex.row() - topIndex.row();
                final int column = currentIndex==null ? controller.getFirstVisibleColumn() : currentIndex.column();
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
                } finally {
                    unlockInput(false);
                    fetchMore(true);
                    switch (asyncReader.getScrollPosition()) {
                        case CURRENT:
                            if (currentIndex != null) {
                                final QModelIndex newTopIndex = currentIndex.sibling(currentIndex.row() - difference, currentIndex.column());
                                controller.view.scrollTo(newTopIndex, ScrollHint.PositionAtTop);
                            }
                            break;
                        case TOP:
                            if (selector.getCurrentEntity() != null) {
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

    private static class PaintEvent extends QEvent {

        public PaintEvent() {
            super(QEvent.Type.User);
        }
    }

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
        if (controller!=null && controller.isLocked()) {
            controller.postEventAfterUnlock(new PaintEvent());            
        }else if (sizeHintCalculating){
            QApplication.postEvent(this, new PaintEvent());
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

    @Override
    public boolean event(final QEvent event) {
        if (canProcessEvent(event.type())) {
            return super.event(event);
        } else if (controller != null && controller.isLocked() && event instanceof QResizeEvent) {
            final QResizeEvent resizeEvent = (QResizeEvent) event;
            controller.postEventAfterUnlock(new QResizeEvent(resizeEvent.size(), resizeEvent.oldSize()));
            return false;
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
            return false;
        } else {
            return false;
        }
    }        

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof PaintEvent) {
            if (controller.isLocked()) {
                event.ignore();
                controller.postEventAfterUnlock(new PaintEvent());
            }else if (sizeHintCalculating){
                event.ignore();
                QApplication.postEvent(this, new PaintEvent());
            } else {
                event.accept();
                repaint();
            }
        } else if (event instanceof FetchMoreEvent){
            event.accept();
            fetchMore(false);
        } else {
            super.customEvent(event);
        }
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public SelectorGrid(final Selector parentView, final SelectorModel model) {
        super(parentView,new VerticalHeaderFactory());
        this.selector = parentView;
        final List<QObject> children = new LinkedList<>(children());
        final IClientEnvironment environment = selector.getEnvironment();
        horizontalHeader = new SelectorHorizontalHeader(this, model);
        setHorizontalHeader(horizontalHeader);
        controller = new StandardSelectorWidgetController(model, this, selector);
        final MessageProvider messageProvider = environment.getMessageProvider();
        final String confirmMovingToLastObjectMessage =
                messageProvider.translate("Selector", "Number of loaded objects is %1s.\nDo you want to load next %2s objects?");
        allDataLoader = new SelectorModelDataLoader(environment, selector);
        
        allDataLoader.setConfirmationMessageText(confirmMovingToLastObjectMessage);
        allDataLoader.setProgressHeader(messageProvider.translate("Selector", "Moving to Last Object"));
        allDataLoader.setProgressTitleTemplate(messageProvider.translate("Selector", "Moving to Last Object...\nNumber of Loaded Objects: %1s"));
        allDataLoader.setDontAskButtonText(messageProvider.translate("Selector", "Load All Objects"));
        final String confirmRestoringPositionMessage =
                messageProvider.translate("Selector", "Number of loaded objects is %1s.\nDo you want to continue restoring position?");
        objectFinder = new SelectorModelDataLoader(environment, selector);
        
        objectFinder.setConfirmationMessageText(confirmRestoringPositionMessage);
        objectFinder.setProgressHeader(messageProvider.translate("Selector", "Restoring Position"));
        objectFinder.setProgressTitleTemplate(messageProvider.translate("Selector", "Restoring Position...\nNumber of Loaded Objects: %1s"));
        objectFinder.setDontAskButtonText(messageProvider.translate("Selector", "Load All Required Objects"));
        actions = new Actions(environment);
        verticalHeader().sectionDoubleClicked.connect(this, "onVHeaderClicked()");
        verticalHeader().setHighlightSections(true);
        ((VerticalHeader)verticalHeader).setSymbolFontMetrics(corner.getSymbolFontMetrics());
        horizontalHeader().sectionClicked.connect(this, "onHHeaderClicked(int)");
        for (QObject child : children) {
            if (child instanceof QAbstractButton) {
                ((QWidget) child).setVisible(false);//hide standard corner widget
                break;
            }
        }
        
        setCornerWidget(corner);
        corner.clicked.connect(this,"cornerClicked()");
        
        final QRect vg = viewport().geometry();
        corner.setFixedPos( (isRightToLeft() ? vg.right() + 1 : vg.left()), vg.top() );
        refreshCornerWidgetToolTip();
        setWordWrap(false);
        setModel(controller.model);
    }

    @SuppressWarnings("unused")
    private void onVHeaderClicked() {
        if (selector.getActions().getInsertAction().isEnabled()) {
            selector.getActions().getInsertAction().trigger();
        } else if (currentIndex() != null) {
            EntityModel entity = controller.model.getEntity(currentIndex());
            if (entity != null) {
                selector.entityActivated.emit(entity);
            }
        }
    }

    @SuppressWarnings("unused")
    private void onHHeaderClicked(final int section) {
        if (section>0 || !controller.model.isSelectionEnabled()){
            final int columnIndex = controller.model.isSelectionEnabled() ? section - 1 : section;
            if (QApplication.keyboardModifiers().isSet(Qt.KeyboardModifier.ControlModifier) && columnIndex >= 0) {
                resizeColumnToContents(section);
            } else {
                controller.processColumnHeaderClicked(columnIndex);
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void onHHeaderCheckBoxClick(){
        finishEdit();
        final GroupModel groupModel = getGroupModel();
        if (horizontalHeader.getSectionCheckState(0)==Qt.CheckState.Checked){
            if (!selectAllRows()){
                updateHeaderCheckState();
            }
        }else{
            if (confirmToClearSelection()){
                groupModel.getSelection().clear();
            }
            updateHeaderCheckState();
        }
    }
    
    private boolean selectAllRows(){        
        final GroupModel groupModel = getGroupModel();
        final boolean hasMoreRows = groupModel.hasMoreRows();
        if (hasMoreRows){
            if (groupModel.getRestrictions().getIsSelectAllRestricted()){
                return false;
            }else{
                groupModel.getSelection().selectAllObjectsInGroup();
            }
        }else{
            final EntityObjectsSelection selection = groupModel.getSelection();
            blockSelectionListener = true;
            try{
                if (selection.getSelectionMode()==ESelectionMode.EXCLUSION){
                    selection.clear();
                }
                final GroupModelReader reader = new GroupModelReader(groupModel);
                for (EntityModel entity: reader){
                    if (groupModel.getEntitySelectionController().isEntityChoosable(entity)){
                        selection.selectObject(entity.getPid());
                    }
                }
            }finally{
                blockSelectionListener = false;
            }
            updateHeaderCheckState();
        }
        setSelectionEnabled(true);
        return true;
    }

    @Override
    public void bind() {
        {
            final GroupModelAsyncReader asyncReader = getGroupModel().getAsyncReader();
            asyncReader.addListener(asyncReaderListener);
        }
        {
            if (!selector.isDisabled() && (!getGroupModel().isEmpty() || getGroupModel().hasMoreRows())) {
                restorePosition();
            } else {
                selector.refresh();
            }
        }
        final List<SelectorColumnModelItem> columns = controller.model.getSelectorColumns();
        {
            controller.restoreHorizontalHeaderSettings(horizontalHeader());
            if (horizontalHeader().count() != columns.size()) {
                final SelectorHorizontalHeader previousHeader = horizontalHeader;
                horizontalHeader = new SelectorHorizontalHeader(this, controller.model);
                setHorizontalHeader(horizontalHeader);
                previousHeader.close();
            }
            controller.setupHorizontalHeader(horizontalHeader,true);
            horizontalHeader.setSectionUserCheckable(0, true);            
            horizontalHeader.checkBoxClicked.connect(this,"onHHeaderCheckBoxClick()");
            updateHeaderCheckState();
        }
        {
            boolean visible = false;
            for (int i = 0; i < columns.size(); ++i) {
                if (!visible) {
                    visible = columns.get(i).isVisible();
                }
                setColumnHidden(i+1, !columns.get(i).isVisible());
            }
            //
            if (!visible && !columns.isEmpty()) {
                final SelectorColumnModelItem firstColumn = columns.get(0);
                if (firstColumn.isForbidden()) {
                    firstColumn.setForbidden(false);
                }
                if (!firstColumn.isVisible()) {
                    firstColumn.setVisible(true);
                }
                setColumnHidden(1, false);
            }
            for (int i = 0; i < columns.size(); ++i) {
                columns.get(i).subscribe(this);
            }
        }
        {
            controller.updateColumnsSizePolicy(horizontalHeader());
            updateCurrentColumn();//RADIX-2513
            controller.updateFirstVisibleColumnIndex(horizontalHeader());
            updateSpan(0, getGroupModel().getEntitiesCount() - 1);
            doubleClicked.connect(controller, "processDoubleClick(QModelIndex)");
            Application.getInstance().getActions().settingsChanged.connect(this, "applySettings()");
            horizontalHeader().sectionMoved.connect(this, "onSectionMoved(int, int, int)");
            horizontalHeader().sectionResized.connect(this, "finishEdit()");
            getGroupModel().addSelectionListener(selectionHandler);
            controller.model.increaseRowsLimit();
        }
        if (selector.isDisabled()) {
            clear();
        }        
        
        corner.setFixedSize(((VerticalHeader)verticalHeader()).getWidth(), getHorizontalHeaderHeight());
        
        verticalHeader().blockSignals(false);
    }

    @Override
    public void refresh(final ModelItem item) {        
        if (item instanceof SelectorColumnModelItem) {
            final SelectorModel model = controller.model;
            final int idx = model.getSelectorColumns().indexOf(item) + 1;
            boolean columnVisibleChanged = false;
            if (idx >= 1) {
                final QHeaderView header = horizontalHeader();
                final SelectorColumnModelItem column = model.getSelectorColumns().get(idx-1);
                if (column.isVisible() == header.isSectionHidden(idx)) {
                    setColumnHidden(idx, !column.isVisible());
                    columnVisibleChanged = true;
                    if (!column.isVisible() && currentIndex() != null && currentIndex().column() == idx) { //RADIX-2513                
                        updateCurrentColumn();
                    }
                    controller.updateFirstVisibleColumnIndex(header);
                    controller.lockInput();
                    try {
                        clearSpans();
                        updateSpan(0, getGroupModel().getEntitiesCount());
                    } finally {
                        controller.unlockInput();
                    }
                }
                if (columnVisibleChanged
                    || header.resizeMode(idx) != StandardSelectorWidgetController.getResizeMode(column)) {
                    controller.updateColumnsSizePolicy(header);
                }
                update();
            }
        } else if (item instanceof Property) {
            final Property property = (Property) item;
            controller.refresh(property);
            final QModelIndex index = controller.findIndexForProperty(property);
            if (index != null) {
                updateColumnsWidth();
            }
        }
        if (item == null && model() != null) {
            controller.refresh();
            refreshCornerWidget();
            refreshHeaderCheckable();
        }
        actions.refresh();
    }
    
    private boolean isSelectionAllowed(){
        return !selector.getGroupModel().getRestrictions().getIsMultipleSelectionRestricted() 
            && isEnabled()
            && !selector.getGroupModel().isEmpty();        
    }
    
    private void refreshCornerWidget(){
        final boolean isSelectionEnabled = isSelectionAllowed();
        if (isSelectionEnabled && !corner.isClickEnabled()){
            corner.setClickEnabled(true);            
            corner.clicked.connect(this,"cornerClicked()");
            refreshCornerWidgetToolTip();
        }else if (!isSelectionEnabled){
            controller.disableSelection();
            corner.setClickEnabled(false);
            corner.clicked.disconnect();
            refreshCornerWidgetToolTip();
        }
    }

    public void refreshCornerWidgetToolTip(){
        final MessageProvider mp = selector.getEnvironment().getMessageProvider();
        if (corner.isClickEnabled()){
            if (controller.isSelectionEnabled()){
                corner.setToolTip(mp.translate("Selector", "Disable Multiple Selection Mode"));
            }else{
                corner.setToolTip(mp.translate("Selector", "Enable Multiple Selection Mode"));
            }            
        }else{
            corner.setToolTip("");
        }
    }    
    
    private void refreshHeaderCheckable(){
        final boolean canSelectAll = !selector.getGroupModel().getRestrictions().getIsMultipleSelectionRestricted()
                                    && (!selector.getGroupModel().hasMoreRows() || !selector.getGroupModel().getRestrictions().getIsSelectAllRestricted());
        if (horizontalHeader.getSectionCheckState(0)==Qt.CheckState.Unchecked){
            horizontalHeader.setSectionUserCheckable(0, canSelectAll);
        }else{
            horizontalHeader.setSectionUserCheckable(0, true);
        }        
    }
    
    public void updateHeaderCheckState(){
        final EntityObjectsSelection selection = getGroupModel().getSelection();
        if (selection.getSelectionMode()==ESelectionMode.NO_SELECTION || selection.isEmpty()){
            horizontalHeader.setSectionCheckState(0, Qt.CheckState.Unchecked);
        }else if (selection.isAllObjectsSelected()){
            horizontalHeader.setSectionCheckState(0, Qt.CheckState.Checked);
        }else{
            horizontalHeader.setSectionCheckState(0, Qt.CheckState.PartiallyChecked);
        }                
        refreshHeaderCheckable();
        if (controller.isSelectionEnabled()){
            final int rowsCount = model().rowCount();
            if (rowsCount>0){
                dataChanged(model().index(0, 0), model().index(rowsCount-1, 0));
            }
        }
        selector.actions.refreshAfterChangeSelection();
        actions.refreshExportActionToolTip();
    } 
    
    private void updateCurrentColumn() {//set current index to first visible column
        final QModelIndex currentIndex = currentIndex();
        if (currentIndex != null) {//update current column index
            final int column = controller.getFirstVisibleColumn();
            final QModelIndex newIndex = controller.model.index(currentIndex.row(), column, null);
            setCurrentIndex(newIndex);
            controller.setItemDelegateCurrentIndex(newIndex);
        }
    }   
    
    private int getCurrentColumn(){
        final QModelIndex currentIndex = currentIndex();
        return currentIndex==null ? controller.getFirstVisibleColumn() : currentIndex.column();
    }

    private void updateSpan(final int startRow, final int endRow) {
        final int columnsCount = horizontalHeader().count();
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
        for (int col = 0, columnsCount = horizontalHeader().count(); col <= columnsCount; col++) {
            int idx = horizontalHeader().logicalIndex(col);//logical index of column
            if (idx >= 0 && !horizontalHeader().isSectionHidden(idx)) {
                firstVisualIndex = col;
                break;
            }
        }

        if (oldVisualIndex == firstVisualIndex || newVisualIndex == firstVisualIndex) {
            controller.updateFirstVisibleColumnIndex(horizontalHeader());
            controller.lockInput();
            try {
                clearSpans();
                updateSpan(0, getGroupModel().getEntitiesCount());
            } finally {
                controller.unlockInput();
            }
            update();
        }
        controller.updateColumnsSizePolicy(horizontalHeader());
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
            menu.addAction(actions.exportAction);
        } else {
            final QAction beforeAction = allActions.get(showFilterAndOrderToolBarActionIndex + 1);
            ((QMenu) menu).insertAction(beforeAction, actions.exportAction);
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
            scheduleFetchMore();
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
            final SelectorModel model = controller.model;
            final int rowsLoadingLimit =
                    selector.getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH, 1000);
            allDataLoader.setLoadingLimit(rowsLoadingLimit);
            allDataLoader.resetCounters();
            int loadedRows = -1;
            disableFetchMoreOnInsertRows = true;
            try {
                controller.lockInput();
                try {
                    loadedRows = allDataLoader.loadAll(new SelectorWidgetDelegate(model, null));
                } catch (InterruptedException exception) {
                    loadedRows = model.rowCount();
                } catch (ServiceClientException exception) {
                    model.showErrorOnReceivingData(exception);
                    loadedRows = -1;
                } finally {
                    controller.unlockInput();
                }
                if (loadedRows > 0 && selector.leaveCurrentEntity(false)) {
                    setCurrentRow(loadedRows - 1);
                }
            } finally {
                if (loadedRows > 0) {
                    controller.model.increaseRowsLimit();
                }
                disableFetchMoreOnInsertRows = false;
            }
        }
    }

    @SuppressWarnings("unused")
    private void export() {
        final SelectorDataExportOptionsDialog dialog =
                new SelectorDataExportOptionsDialog(getGroupModel(), this);
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            final File csvFile = dialog.getFile();
            final MessageProvider messageProvider = selector.getEnvironment().getMessageProvider();
            final String exceptionTitle = messageProvider.translate("Selector", "Failed to Export Objects");
            final PropertyValuesWriteOptions writeOptions = dialog.getPropertyValuesWriteOptions();
            final List<SelectorColumnModelItem> columns = new LinkedList<>();
            if (dialog.exportColumnTitles()) {
                for (Id columnId : writeOptions.getColumnsToExport()) {
                    columns.add(getGroupModel().getSelectorColumn(columnId));
                }
            }
            final EntityObjectsWriter writer;
            try {
                writer =
                        EntityObjectsWriter.Factory.newCsvWriter(csvFile, writeOptions, dialog.getCsvFormatOptions(), columns);
            } catch (FileNotFoundException exception) {
                final FileException exceptionToShow =
                        new FileException(selector.getEnvironment(), FileException.EExceptionCode.FILE_NOT_FOUND, csvFile.getAbsolutePath());
                getGroupModel().showException(exceptionTitle, exceptionToShow);
                return;
            } catch (UnsupportedEncodingException exception) {
                getGroupModel().showException(exceptionTitle, exception);
                return;
            }
            final String confirmMovingToExportMessage =
                    messageProvider.translate("Selector", "Number of loaded objects is %1s.\nDo you want to load next %2s objects?");
            final SelectorModelDataLoader dataLoader = new SelectorModelDataLoader(selector.getEnvironment(), this.selector);
            
            dataLoader.setConfirmationMessageText(confirmMovingToExportMessage);
            final int rowsLoadingLimit =
                    selector.getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH, 1000);
            final int maxRows = dialog.getMaxRows();
            dataLoader.setLoadingLimit(maxRows > 0 ? -1 : rowsLoadingLimit);
            dataLoader.resetCounters();
            final EntityObjectsSelection selection = selector.getGroupModel().getSelection().getNormalized();
            disableFetchMoreOnInsertRows = true;
            final IProgressHandle progressHandle =
                    selector.getEnvironment().getProgressHandleManager().newStandardProgressHandle();            
            try {
                progressHandle.setTitle(messageProvider.translate("Selector", "Exporting Objects"));
                if (selection.isEmpty()){
                    progressHandle.startProgress(messageProvider.translate("Selector", "Exporting Objects..."), true);
                }else{
                    progressHandle.startProgress(messageProvider.translate("Selector", "Exporting Selected Objects..."), true);
                }
                final String progressMessageTemplate =
                        messageProvider.translate("Selector", "Exporting Objects...\nNumber of Exported Objects: %1s");
                int numberOfExportedObjects = 0;
                if (selection.getSelectionMode()==ESelectionMode.INCLUSION){
                    final Collection<Pid> selectedObjects = selection.getSelectedObjects();                    
                    for (Pid selectedObject: selectedObjects){
                        final int row = getGroupModel().findEntityByPid(selectedObject);
                        if (row>-1){
                            final EntityModel entity;
                            try{
                                entity = getGroupModel().getEntity(row);
                                try{
                                    writer.writeEntityObject(entity);
                                    if (numberOfExportedObjects % 30 == 0) {
                                        writer.flush();
                                    }                                    
                                }catch(IOException exception){
                                    getGroupModel().showException(exceptionTitle, new FileException(selector.getEnvironment(), FileException.EExceptionCode.CANT_WRITE, csvFile.getAbsolutePath()));
                                    return;                                    
                                }
                                numberOfExportedObjects++;
                                if (numberOfExportedObjects>=maxRows){
                                    break;
                                }
                                progressHandle.setText(String.format(progressMessageTemplate, String.valueOf(numberOfExportedObjects)));
                            }catch(BrokenEntityObjectException | InterruptedException exception){
                                continue;
                            }catch(ServiceClientException exception){
                                selector.getEnvironment().getTracer().error(exception);
                                continue;
                            }
                        }
                    }
                }else{
                    try {
                        int row = 0;                        
                        final ISelectorWidgetDelegate swDelegate = new SelectorWidgetDelegate(controller.model, null);
                        do {
                            for (int count = controller.model.rowCount(null); row < count && (row < maxRows || maxRows < 0) && !progressHandle.wasCanceled(); row++) {
                                final QModelIndex currentIdx = controller.model.index(row, 0);
                                if (!controller.model.isBrokenEntity(currentIdx)) {
                                    final EntityModel entity = controller.model.getEntity(currentIdx);
                                    if (!selection.isEmpty() && !selection.isObjectSelected(entity)){                                    
                                        continue;
                                    }
                                    try {
                                        writer.writeEntityObject(entity);
                                        if (row % 30 == 0) {
                                            writer.flush();
                                        }
                                    } catch (IOException exception) {
                                        getGroupModel().showException(exceptionTitle, new FileException(selector.getEnvironment(), FileException.EExceptionCode.CANT_WRITE, csvFile.getAbsolutePath()));
                                        return;
                                    }
                                    numberOfExportedObjects++;
                                }
                            }
                            progressHandle.setText(String.format(progressMessageTemplate, String.valueOf(row)));
                            controller.model.increaseRowsLimit();
                        } while (!progressHandle.wasCanceled() && (row < maxRows || maxRows < 0) && dataLoader.loadMore(swDelegate));
                    } catch (ServiceClientException exception) {
                        getGroupModel().showException(exceptionTitle, exception);
                    } catch (InterruptedException exception) {
                    } finally {
                        controller.model.increaseRowsLimit();                        
                    }
                }
                final String title = messageProvider.translate("Selector", "Export Complete");
                final String messageTemplate = messageProvider.translate("Selector", "%1$s objects were successfully exported");
                selector.getEnvironment().messageInformation(title, String.format(messageTemplate, String.valueOf(numberOfExportedObjects)));                
            } finally {                
                progressHandle.finishProgress();
                try {
                    writer.close();
                } catch (IOException exception) {
                    getGroupModel().showException(exceptionTitle, new FileException(selector.getEnvironment(), FileException.EExceptionCode.CANT_WRITE, csvFile.getAbsolutePath()));
                }                
                disableFetchMoreOnInsertRows = false;
            }
        }
    }

    @Override
    public void entityRemoved(final Pid pid) {
        controller.processEntityRemoved(pid);
        controller.lockInput();
        try {
            clearSpans();
            updateSpan(0, getGroupModel().getEntitiesCount() - 1);
            updateColumnsWidth();
        } finally {
            controller.unlockInput();
        }
    }

    public void setCurrentRow(final int row) {
        try {
            controller.setCurrentRow(row);
        } catch (InterruptedException exception) {
        } catch (ServiceClientException exception) {
            controller.processErrorOnReceivingData(exception);
        }
    }

    @Override
    protected void mousePressEvent(final QMouseEvent event) {
        final QModelIndex currentIndex = currentIndex();
        final int row = currentIndex==null ? -1 : currentIndex.row();
        if (controller.processMousePressEvent(event)) {            
            if (row>-1 && isSelectionAllowed()){
                if (event.modifiers().isSet(Qt.KeyboardModifier.ShiftModifier))
                    changeSelectionToCurrentRow(row, ESelectAction.SELECT);
                else if (event.modifiers().isSet(Qt.KeyboardModifier.ControlModifier))
                    changeSelectionToCurrentRow(row, ESelectAction.UNSELECT);
            }
        }else{
            final QModelIndex index = indexAt(event.pos());            
            if (controller.isSelectionEnabled() && index!=null && index.column()==0){
                finishEdit();
                return;
            }
            final boolean changingIndex = index != null && !verticalHeader().rect().contains(event.x(), event.y()) && !index.equals(currentIndex());            
            final boolean editorOpened = controller.getCurrentPropEditor() != null;
            super.mousePressEvent(event);
            if (changingIndex || !editorOpened) {
                openEditor(index);
            }            
        }
    }

    @Override
    protected void mouseMoveEvent(QMouseEvent event) {
        if (event.buttons().isSet(Qt.MouseButton.RightButton) || event.buttons().isSet(Qt.MouseButton.LeftButton)) //препятствуем перемещению мыши с нажатой кнопкой
        {
            event.ignore();
        } else {
            super.mouseMoveEvent(event);
        }
    }

    @Override
    protected QModelIndex moveCursor(final CursorAction action, final Qt.KeyboardModifiers mod) {
        if (controller.canMoveCursor(action, state())) {
            final QModelIndex index = super.moveCursor(action, mod);            
            final QModelIndex actualIndex = controller.afterMoveCursor(index);
            if (actualIndex!=null && isSelectionAllowed() && mod.isSet(Qt.KeyboardModifier.ShiftModifier)){                
                if (changeSelection(controller.selector.getGroupModel().getSelection(), actualIndex, ESelectAction.INVERT)){
                    afterSelectionChanged(actualIndex.row(), actualIndex.row());
                }
            }
            return actualIndex;
        }
        return null;
    }

    @Override
    protected void keyPressEvent(final QKeyEvent event) {
        if (event.matches(QKeySequence.StandardKey.Find)) {
            actions.findAction.trigger();
        } else if (event.matches(QKeySequence.StandardKey.FindNext)) {
            actions.findNextAction.trigger();
        } else if (event.matches(QKeySequence.StandardKey.SelectAll) && isSelectionAllowed()){
            if (selectAllRows()){
                updateHeaderCheckState();
            }
        } else if (event.key()==Qt.Key.Key_Insert.value() 
                   && event.modifiers().value()==0 
                   && isSelectionAllowed() && currentIndex()!=null                   
                   && !inEditingMode()
                  ){
            if (changeSelection(controller.selector.getGroupModel().getSelection(), currentIndex(), ESelectAction.INVERT)){
                afterSelectionChanged(currentIndex().row(), currentIndex().row());
                moveCursor(CursorAction.MoveDown, new Qt.KeyboardModifiers(0));
            }
        }
        else {
            super.keyPressEvent(event);
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
        controller.processCurrentChanged(current, previous, horizontalHeader());
        actions.refresh();        
    }

    @Override
    public void rereadAndSetCurrent(final Pid pid) throws InterruptedException, ServiceClientException {
        if (pid == null && currentIndex() != null) {
            final EntityModel entity = controller.model.getEntity(currentIndex());
            rereadInternal(entity.getPid());
        } else {
            rereadInternal(pid);
        }        
    }

    @Override
    public void reread() throws InterruptedException, ServiceClientException {
        rereadInternal(null);
    }

    @Override
    protected boolean edit(final QModelIndex index, final EditTrigger trigger, final QEvent event) {
        if (controller.getCurrentPropEditor() != null) {
            if (index.equals(currentIndex())) {
                return true;                
            } else {
                finishEdit();
            }
        }
        final boolean onChangeSelection = controller.isSelectionEnabled() 
                                          && index!=null 
                                          && index.column()==0;
        if (onChangeSelection){
            finishEdit();
        }
        if (onChangeSelection        
            && event instanceof QMouseEvent
            && (((QMouseEvent)event).modifiers().isSet(Qt.KeyboardModifier.ShiftModifier)
                || ((QMouseEvent)event).modifiers().isSet(Qt.KeyboardModifier.ControlModifier))){
            if (((QMouseEvent)event).modifiers().isSet(Qt.KeyboardModifier.ShiftModifier)){
                return !changeSelectionToCurrentRow(index.row(), ESelectAction.SELECT);
            }else{
                return !changeSelectionToCurrentRow(index.row(), ESelectAction.UNSELECT);
            }
        }else{
            if (super.edit(index, trigger, event)){
                if (onChangeSelection){
                    afterSelectionChanged(index.row(), index.row());
                }else{
                    update();
                }
                return true;
            }else{
                return false;
            }
        }
    }
    
    private boolean changeSelectionToCurrentRow(final int fromRow, final ESelectAction action){
        if (currentIndex()==null){
            return false;
        }else{                        
            final int currentRow = currentIndex().row();
            final int endRow = Math.max(currentRow, fromRow);
            final int startRow = Math.min(currentRow, fromRow);
            final EntityObjectsSelection selection = controller.selector.getGroupModel().getSelection();
            boolean selectionChanged = false;
            for (int row=startRow; row<=endRow; row++){                
                final QModelIndex rowIndex = controller.model.index(row, 0);
                if (changeSelection(selection, rowIndex, action)){
                    selectionChanged = true;
                }
            }
            if (selectionChanged){
                afterSelectionChanged(startRow,endRow);
            }
            return selectionChanged;
        }
    }
    
    private boolean changeSelection(final EntityObjectsSelection selection, final QModelIndex index, final ESelectAction action){
        if (!controller.model.isBrokenEntity(index)){
            final EntityModel entity = controller.model.getEntity(index);
            final Pid entityPid = entity.getPid();
            if (selector.getGroupModel().getEntitySelectionController().isEntityChoosable(entity)){
                switch(action){
                    case INVERT:
                        selection.invertSelection(entityPid);
                        return true;
                    case SELECT:{
                        if (!selection.isObjectSelected(entityPid)){
                            selection.selectObject(entityPid);
                            return true;
                        }
                        break;
                    }
                    case UNSELECT:{
                        if (selection.isObjectSelected(entityPid)){
                            selection.unselectObject(entityPid);
                            return true;
                        }
                        break;                        
                    }                        
                }
            }
        }
        return false;
    }
    
    private void afterSelectionChanged(final int topRow, final int bottomRow){
        setSelectionEnabled(true);
        final int lastColumn = model().columnCount();
        dataChanged(controller.model.index(topRow, 0), controller.model.index(bottomRow, lastColumn));
    }
        

    @Override
    public void afterPrepareCreate(final EntityModel entity) {
    }

    private void rereadInternal(final Pid pid) throws InterruptedException, ServiceClientException {
        finishEdit();
        setEnabled(true);
        final int column = getCurrentColumn();
        lockInput();
        int idx = -1;
        try {            
            clearSpans();            
            verticalScrollBar().setValue(0);//RADIX-9939 reset value to avoid redundant read on setRange call in updateGeometry.
            if (pid != null) {
                controller.model.reset(null);
                final int rowsLoadingLimit =
                        selector.getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_FOR_RESTORING_POSITION_CONFIG_PATH, 300);
                objectFinder.setLoadingLimit(rowsLoadingLimit);
                objectFinder.resetCounters();
                enterSearchState();
                try {
                    idx = objectFinder.findObjectByPid(new SelectorWidgetDelegate(controller.model, null), pid);
                } catch (ServiceCallException exception) {
                    controller.processErrorOnReceivingData(exception);
                } finally {
                    exitSearchState();
                }
            } else {
                controller.model.reread(null);
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
        finishEdit();
        ((SelectorModel) model()).clear();
        actions.refresh();        
        setEnabled(false);
        update();        
        refreshCornerWidget();
    }

    @Override
    public boolean setFocus(final Property property) {
        final QModelIndex index = controller.findIndexForProperty(property);
        if (index != null) {
            setCurrentIndex(index);
            openEditor(index);
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
            && !disableFetchMoreOnInsertRows && !inSearchState()){
            scheduleFetchMore();
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

    private void updateColumnsWidth() {
        final QHeaderView header = horizontalHeader();
        for (int i = 0, count = header.count(); i < count; i++) {
            if (!header.isSectionHidden(i)
                    && header.resizeMode(i) == QHeaderView.ResizeMode.ResizeToContents) {
                resizeColumnToContents(i);
            }
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
        getGroupModel().removeSelectionListener(selectionHandler);
        controller.model.unsubscribeProperties(this);        
        
        {//disconnecting signals for GC
            corner.disconnect();
            actions.close();
            horizontalHeader().disconnect();
        }

        if (model() != null) {
            if (selector.getModel() != null) {                
                controller.saveHorizontalHeaderSettings(horizontalHeader());
                savePosition();
            }
            //setModel(null);
            controller.model.clear();
            controller.model.dispose();
            getGroupModel().getAsyncReader().clean();
        }
        horizontalHeader.close();
        controller.close();
        super.closeEvent(event);
    }

    @Override
    public QSize sizeHint() {
        final QSize size = super.sizeHint();
        size.setHeight(DEFAULT_HEIGHT);        
        final int width = horizontalHeader.sizeHint().width()+(corner.isVisible() ? corner.width() : 0)+frameWidth()*2;
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
        sizeHintCalculating = true;
        try {
            if (horizontalHeader.resizeMode(column) != QHeaderView.ResizeMode.ResizeToContents
                    || horizontalHeader.isSectionHidden(column)) {
                return super.sizeHintForColumn(column);
            }
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
        } finally {
            sizeHintCalculating = false;
        }
    }
    
    private int getHorizontalHeaderHeight(){
        final int height = Math.max(horizontalHeader.minimumHeight(), horizontalHeader.sizeHint().height());
        return Math.min(height, horizontalHeader.maximumHeight());
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
            corner.setVisible(width>0 && height>0);            
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

    @SuppressWarnings("unused")
    private void cornerClicked(){
        if (!isEnabled() || getGroupModel().isEmpty()){
            return;
        }
        finishEdit();
        final QModelIndex index = currentIndex();
        final int currentRow = index==null ? -1 : index.row();
        int currentColumn = index==null ? -1 : getCurrentColumn();
        if (controller.isSelectionEnabled()){
            final EntityObjectsSelection selection = getGroupModel().getSelection();
            if (!selection.isEmpty()){
                if (confirmToClearSelection()){
                    selection.clear();
                    if (!selection.isEmpty()){
                        return;//cleaning selection was rejected in some handler
                    }
                }else{
                    return;
                }
            }
            setSelectionEnabled(false);            
        }else{
            setSelectionEnabled(true);
        }
        if (currentColumn>=0){
            final QModelIndex newIndex = controller.model.index(currentRow, currentColumn);
            setCurrentIndex(newIndex);
            controller.setItemDelegateCurrentIndex(newIndex);
        }else{
            updateCurrentColumn();
        }
        controller.updateFirstVisibleColumnIndex(horizontalHeader);
        refreshCornerWidgetToolTip();        
        controller.lockInput();
        try {
            clearSpans();
            updateSpan(0, getGroupModel().getEntitiesCount());
        } finally {
            controller.unlockInput();
        }
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
            if (selector.getGroupModel().getRestrictions().getIsMultipleSelectionRestricted()){
                return false;
            }else{
                setSelectionEnabled(true);
            }
        }else{
            setSelectionEnabled(false);
        }
        return true;
    }        
    
    private void setSelectionEnabled(final boolean isEnabled){
        if (isEnabled!=controller.isSelectionEnabled()){
            if (isEnabled){
                controller.enableSelection();
            }else{
                controller.disableSelection();
            }
            controller.updateFirstVisibleColumnIndex(horizontalHeader);
        }
    }
    
    private boolean inEditingMode(){
        return itemDelegate() instanceof WrapModelDelegate
            && ((WrapModelDelegate)itemDelegate()).getActivePropEditor()!=null;
    }
            
    private void scheduleFetchMore(){
        if (!fetchMoreScheduled){
            fetchMoreScheduled = true;
            Application.processEventWhenEasSessionReady(this, new FetchMoreEvent());            
        }
    }
    
    private void fetchMore(final boolean silently) {
        fetchMoreScheduled = false;        
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
}
