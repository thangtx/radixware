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

import com.trolltech.qt.QNoSuchEnumValueException;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.core.Qt.Key;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.gui.*;
import com.trolltech.qt.gui.QAbstractItemView.CursorAction;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.MatchOptions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.widgets.selector.SelectorModelDataLoader;
import org.radixware.kernel.common.client.widgets.selector.SelectorSortUtils;
import org.radixware.kernel.common.enums.ESelectorColumnSizePolicy;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.editors.valeditors.TristateCheckBoxStyle;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.utils.EQtStyle;
import org.radixware.kernel.explorer.utils.WidgetUtils;

import org.radixware.kernel.explorer.views.selector.Selector;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditor;


public class StandardSelectorWidgetController {
    
    private final static String STATE_KEY = "headerState";
    private final static String ROWS_LIMIT_FOR_SEARCH_CONFIG_PATH = 
        SettingNames.SYSTEM+"/"+SettingNames.SELECTOR_GROUP+"/"+SettingNames.Selector.COMMON_GROUP+"/"+SettingNames.Selector.Common.ROWS_LIMIT_FOR_SEARCH;
        
    final public SelectorModel model;
    final public QAbstractItemView view;
    final public Selector selector;
    private FindInSelectorDialog findDialog;
    private final IProgressHandle searchProgressHandle;
    private final SelectorModelDataLoader dataLoader;
    private final String searchConfirmationMessageTemplate;
    private final ItemDelegateWithFocusFrame selectionColumnDelegate;
    private final TristateCheckBoxStyle style;
    private int numberOfObjectsProcessedInSearch;
    private boolean locked, redrawBlocked;
    private final QEventsScheduler scheduler = new QEventsScheduler();
    
    private final ISelector.CurrentEntityHandler currentEntityHandler = new ISelector.CurrentEntityHandler() {

        @Override
        public void onSetCurrentEntity(final EntityModel entity) {
            StandardSelectorWidgetController.this.onSetCurrentEntity(entity);
        }

        @Override
        public void onLeaveCurrentEntity() {
            //Do nothing
            //throw new UnsupportedOperationException("Not supported yet.");
        }
    };

    public StandardSelectorWidgetController(final SelectorModel model, final QAbstractItemView view, final Selector selector) {
        super();
        final IClientEnvironment environment = selector.getEnvironment();
        searchProgressHandle = environment.getProgressHandleManager().newStandardProgressHandle();
        this.model = model;
        this.view = view;
        this.selector = selector;
        searchConfirmationMessageTemplate = 
            environment.getMessageProvider().translate("Selector", "%1s objects were loaded, but the required one was not found.\nIt is recommended to use filter to find specific objects.\nDo you want to continue searching among the next %2s objects?");
        dataLoader = new SelectorModelDataLoader(environment, selector);
        dataLoader.setConfirmationMessageText(searchConfirmationMessageTemplate);
        dataLoader.setDontAskButtonText(environment.getMessageProvider().translate("Selector", "Process All Required Objects"));
        
        view.setSelectionMode(QAbstractItemView.SelectionMode.NoSelection);
        view.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        
        // установить делегат
        view.setItemDelegate(new SelectorWidgetItemDelegate(view));
        style = new TristateCheckBoxStyle(view.parentWidget());
        view.setStyle(style);
        //selector.onSetCurrentEntity.connect(this, "onSetCurrentEntity(EntityModel)");
        selector.addCurrentEntityHandler(currentEntityHandler);
        selectionColumnDelegate = new ItemDelegateWithFocusFrame(view);
        selectionColumnDelegate.setFocusFrameVisible(true);
        applySettings();
    }
    
    private void updateIconSize(){
        final QSize size = view.iconSize();
        if (size.isValid()){
            model.setIconSize(size);
        }else{
            int pm = view.style().pixelMetric(QStyle.PixelMetric.PM_SmallIconSize, null, view);
            model.setIconSize(new QSize(pm, pm));
        }        
    }    

    private FindInSelectorDialog getFindDialog() {
        if (findDialog == null) {
            initFindDialog();
        }
        return findDialog;
    }

    private void initFindDialog() {
        findDialog = new FindInSelectorDialog(selector.getModel().getEnvironment(), selector, selector.getModel().getConfigStoreGroupName());
        findDialog.find.connect(findDialog, "accept()");
    }

    public void lockInput() {
        if (!locked && selector.getGroupModel() != null){
            if (!selector.getGroupModel().getRestrictions().getIsEditorRestricted()
                && !selector.isSplitterCollapsed()) {
                //optimization: block redraw only if editor can be opened
                redrawBlocked = true;
                selector.blockRedraw();
            }
            locked = true;
        }
    }
    
    public void unlockInput(){
        unlockInput(true);
    }

    public void unlockInput(final boolean setFocus) {
        if (locked) {
            if (redrawBlocked){
                selector.unblockRedraw();
                redrawBlocked = false;
            }
            scheduler.postScheduledEvents(view);
            if (setFocus){
                view.setFocus();
            }
            locked = false;
        }
    }
    
    public void postEventAfterUnlock(final QEvent event){
        scheduler.scheduleEvent(event);
    }

    public boolean isLocked() {
        return locked;
    }
    
    private QHeaderView header;

    public void setupHorizontalHeader(final QHeaderView header, final boolean enableSelection) {
        this.header = header;
        header.customContextMenuRequested.connect(this, "createAndShowPopupMenu(QPoint)");        
        header.setClickable(true);
        if (enableSelection){
            model.enableSelection();
            header.setResizeMode(0,QHeaderView.ResizeMode.Fixed);
            header.resizeSection(0, calcSelectionColumnWidth(header));
            view.setItemDelegateForColumn(0, selectionColumnDelegate);
            header.setSectionHidden(0, true);//TODO        
        }
    }
    
    private int calcSelectionColumnWidth(final QHeaderView header){
        final int hmargin = header.style().pixelMetric(QStyle.PixelMetric.PM_FocusFrameHMargin, null, header)+1;
        final QStyleOptionButton opt = new QStyleOptionButton();
        final int width = header.style().subElementRect(QStyle.SubElement.SE_ViewItemCheckIndicator, opt).width();        
        final EQtStyle gStyle = EQtStyle.detectStyle(header.style());
        final int offset;
        if (gStyle==EQtStyle.Windows || gStyle==EQtStyle.Plastique){
        //This styles paints checkbox with offset
            offset = 1;
        }else{
            offset = 0;
        }
        return width+hmargin*4+offset+1;//grid line width = 1
    }
    
    public void updateFirstVisibleColumnIndex(final QHeaderView header){
        final int columnsCount = header.count();
        int idx;
        for (int col = 0; col <= columnsCount; col++) {
            idx = header.logicalIndex(col);//logical index of column
            if (idx >= 0 && !header.isSectionHidden(idx)) {
                model.setFirstVisibleColumnIndex(idx);
                break;
            }
        }
    }

    public void restoreHorizontalHeaderSettings(final QHeaderView header) {        
        final ExplorerSettings settings = (ExplorerSettings) selector.getEnvironment().getConfigStore();
        settings.beginGroup(selector.getModel().getConfigStoreGroupName());
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.Selector.COLUMNS_GROUP);
        try {
            if (settings.contains(STATE_KEY)) {
                header.restoreState(settings.readQByteArray(STATE_KEY));
            }
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
        header.setStretchLastSection(false);
        header.setCascadingSectionResizes(false);
    }

    public void saveHorizontalHeaderSettings(final QHeaderView header) {
        model.disableSelection();
        final ExplorerSettings settings = (ExplorerSettings) selector.getEnvironment().getConfigStore();
        settings.beginGroup(selector.getModel().getConfigStoreGroupName());
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.Selector.COLUMNS_GROUP);
        try {
            settings.writeQByteArray(STATE_KEY, header.saveState());
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
        //Сохранение видимости колонок
        selector.getGroupModel().getSelectorColumns().storeSettings();
    }
    
    public boolean updateColumnsSizePolicy(final QHeaderView header){        
        int stretchColumnIndex = -1;
        final List<SelectorColumnModelItem> columns = model.getSelectorColumns();        
        final boolean isSelectionEnabled = model.isSelectionEnabled();
        final int columnsCount = isSelectionEnabled ? columns.size()+1 : columns.size();
        final List<QHeaderView.ResizeMode> newColumnsResizeMode = new ArrayList<>(columnsCount);
        if (isSelectionEnabled){
            newColumnsResizeMode.add(null);
        }
        for (int i=(isSelectionEnabled ? 1 : 0); i<columnsCount; i++){
            final int colIndex = isSelectionEnabled ? i - 1 : i;
            final QHeaderView.ResizeMode columnResizeMode = getResizeMode(columns.get(colIndex));
            newColumnsResizeMode.add(header.resizeMode(i)==columnResizeMode ? null : columnResizeMode);
            if (columnResizeMode==QHeaderView.ResizeMode.Stretch && !header.isSectionHidden(i)){
                if (stretchColumnIndex>=0){
                    newColumnsResizeMode.set(stretchColumnIndex, QHeaderView.ResizeMode.Interactive);
                }
                stretchColumnIndex = i;
            }
        }
        if (stretchColumnIndex<0){
            int lastVisibleColumnIdx=-1;
            for (int i=columnsCount-1; i>=(isSelectionEnabled ? 1 : 0); i--){
                final int logicalIndex=header.logicalIndex(i);
                final int colIndex = isSelectionEnabled ? logicalIndex - 1 : logicalIndex;
                if (lastVisibleColumnIdx<0 && !header.isSectionHidden(logicalIndex)){
                    lastVisibleColumnIdx = logicalIndex;
                }
                if (getResizeMode(columns.get(colIndex))==QHeaderView.ResizeMode.Interactive
                    && !header.isSectionHidden(logicalIndex)){
                    if (header.resizeMode(logicalIndex)==QHeaderView.ResizeMode.Stretch){
                        newColumnsResizeMode.set(logicalIndex, null);                        
                    }else{
                        newColumnsResizeMode.set(logicalIndex, QHeaderView.ResizeMode.Stretch);
                    }
                    stretchColumnIndex = i;
                    break;
                }
            }
            if (stretchColumnIndex<0 && lastVisibleColumnIdx>=0){
                if (header.resizeMode(lastVisibleColumnIdx)==QHeaderView.ResizeMode.Stretch){
                    newColumnsResizeMode.set(lastVisibleColumnIdx, null);                        
                }else{
                    newColumnsResizeMode.set(lastVisibleColumnIdx, QHeaderView.ResizeMode.Stretch);
                }
            }
        }
        boolean columnSizePolicyChanged = false;
        for (int i=0; i<columnsCount; i++){
            QHeaderView.ResizeMode columnResizeMode = newColumnsResizeMode.get(i);
            if (columnResizeMode!=null){
                header.setResizeMode(i, columnResizeMode);
                columnSizePolicyChanged = true;
            }
        }            
        return columnSizePolicyChanged;
    }        

    public static QHeaderView.ResizeMode getResizeMode(final SelectorColumnModelItem column){
        ESelectorColumnSizePolicy sizePolicy = column.getSizePolicy();
        if (sizePolicy==ESelectorColumnSizePolicy.AUTO){
            sizePolicy = column.getAutoSizePolicy();
        }
        switch(sizePolicy){
            case RESIZE_BY_CONTENT:
                return QHeaderView.ResizeMode.ResizeToContents;
            case STRETCH:
                return QHeaderView.ResizeMode.Stretch;
            default:
                return QHeaderView.ResizeMode.Interactive;
        }
    }
    
    public void refresh(final Property property) {
        final QModelIndex currentIndex = view.currentIndex();
        if (model.isSubscribedToProperty(property) && currentIndex != null) {
            final QModelIndex index = findIndexForProperty(property);
            if (index != null) {
                final PropEditor editor = getCurrentPropEditor();
                if (editor != null && index.equals(currentIndex)) {
                    editor.refresh(property);
                } else {
                    view.update(index);
                }
            }
        }
    }
    
    public boolean currentEntityDefined(){
        if (selector.getCurrentEntity()==null && !selector.getGroupModel().isEmpty()){            
            final QModelIndex currentIndex = view.currentIndex();
            return currentIndex!=null && model.isBrokenEntity(currentIndex);
        }else{
            return true;
        }
    }
    
    public void refresh(){
        if (currentEntityDefined()){
            setItemDelegateFrameVisible(true);
       }
       else{
            setItemDelegateFrameVisible(false);
            view.clearSelection();            
       }        
    }
    
    private void enterEntityImpl(final QModelIndex index, final boolean forced) {
        final EntityModel entity = model.getEntity(index);    
        if (entity != null && index != null && (selector.getCurrentEntity() != entity || forced)) {
            try {
                lockInput();
                view.setCurrentIndex(index);
                view.scrollTo(index, QAbstractItemView.ScrollHint.EnsureVisible);
                final QModelIndex parent = index.parent();
                if (askToFetchMore(parent) && model.canReadMore(parent)) {//RADIX-2968
                    model.fetchMore(parent);
                }
                if (entity instanceof BrokenEntityModel){
                    selector.showException( new BrokenEntityObjectException( (BrokenEntityModel)entity ) );
                }
                else{                    
                    if (selector.getCurrentEntity()!=entity) {
                        //Текущая entity уже могла быть установлена вызовом view.setCurrentIndex(index);                    
                        selector.setCurrentEntity(entity);
                    }
                }
            } finally {
                unlockInput(!forced);
            }
        } else if (index != null) {
            view.update(index);
        }
    }
    
    public void enterEntity(final QModelIndex index) {
        enterEntityImpl(index, false);
    }
    
    public void forcedEnterEntity(final QModelIndex index) {
        enterEntityImpl(index, true);
    }
        
    public void readAll(QModelIndex index) throws ServiceClientException, InterruptedException {
        try {
            lockInput();
            while (model.canReadMore(index)) {
                model.readMore(index);
            }
        } finally {
            unlockInput();
        }
    }

    public void showFindDialog(){
        final QModelIndex currentIndex = view.currentIndex();
        if (currentIndex != null) {
            final GroupModel group = model.getRootGroupModel();
            final String defTitle;
            if (group.getSelectorPresentationDef().hasTitle()) {
                defTitle = group.getSelectorPresentationDef().getTitle();
            } else if (group.getSelectorPresentationDef().getClassPresentation().hasGroupTitle()) {
                defTitle = group.getSelectorPresentationDef().getClassPresentation().getGroupTitle();
            } else {
                defTitle = group.getSelectorPresentationDef().getClassPresentation().getClassTitle();
            }
            final FindInSelectorDialog dialog = getFindDialog();
            if (defTitle != null) {
                final String title = selector.getEnvironment().getMessageProvider().translate("Selector", "Find in \'%s\'");
                dialog.setWindowTitle(String.format(title, defTitle));
            }
            final int columnIndex;
            if (model.isSelectionEnabled()){
                columnIndex = currentIndex.column()==0 ? 0 : currentIndex.column()-1;
            }else{
                columnIndex = currentIndex.column();
            }
            dialog.setSelectorColumns(model.getSelectorColumns(), columnIndex);            
            if (dialog.exec() == QDialog.DialogCode.Accepted.value() && getFindDialog().getColumnIdx() > -1) {
                try{
                    selectFirstMatchRow(false);
                }
                catch(InterruptedException exception){
                    //search was interrupted - nothing to do
                }
                catch(ServiceClientException exception){
                    processErrorOnReceivingData(exception);
                }
            }
        }
    }

    public void findNext(){
        if (getFindDialog().result()==QDialog.DialogCode.Accepted.value()){
            try{
                selectFirstMatchRow(true);
            }
            catch(InterruptedException exception){
                //search was interrupted - nothing to do
            }
            catch(ServiceClientException exception){
                processErrorOnReceivingData(exception);
            }                        
        }
        else{
            showFindDialog();
        }
    }

    private void selectFirstMatchRow(final boolean next) throws InterruptedException, ServiceClientException{
        QModelIndex currentIndex = view.currentIndex();
        if (currentIndex != null) {
            final FindInSelectorDialog dialog = getFindDialog();
            final boolean forward = dialog.isForwardChecked();
            final int searchColumnIndex = model.isSelectionEnabled() ? dialog.getColumnIdx()+1 : dialog.getColumnIdx();
            final MatchOptions matchOptions = 
                new MatchOptions(dialog.isMatchCaseChecked(), dialog.isWholeWordChecked());            
            final String searchString = dialog.getFindWhat();
            final boolean isBooleanColumn = model.getSelectorColumns().get(searchColumnIndex).getPropertyDef().getType() == EValType.BOOL;            
            QModelIndex first = model.index(currentIndex.row(), searchColumnIndex, currentIndex.parent());
            final int rowsLoadingLimit = 
                selector.getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_FOR_SEARCH_CONFIG_PATH, 100);
            numberOfObjectsProcessedInSearch = 0;
            dataLoader.setLoadingLimit(rowsLoadingLimit);
            dataLoader.resetCounters();
            searchProgressHandle.startProgress(selector.getEnvironment().getMessageProvider().translate("Wait Dialog", "Searching..."), true);            
            try {
                if (next) {
                    first = forward ? getNextIndex(first) : getPrevIndex(first);
                }                
                Property property;
                String displayText;
                for (currentIndex = first; currentIndex != null; currentIndex = forward ? getNextIndex(currentIndex) : getPrevIndex(currentIndex)) {
                    numberOfObjectsProcessedInSearch++;
                    property = (Property) model.data(currentIndex, ItemDataRole.UserRole);
                    if (property!=null){
                        displayText = model.getTextToDisplay(property, -1/*don`t cut text*/);
                        if (displayText!=null && property.valueMatchesToSearchString(displayText, searchString, matchOptions)){
                            if (dataLoader.getLoadedObjectsCount()>0){
                                model.increaseRowsLimit();
                            }
                            try{
                                enterEntity(currentIndex);
                            }finally{
                                if (dataLoader.getLoadedObjectsCount()>0){
                                    model.increaseRowsLimit();
                                }
                            }
                            return;                            
                        }
                    }
                }
            } finally {
                searchProgressHandle.finishProgress();
            }

            final String title = selector.getEnvironment().getMessageProvider().translate("ExplorerDialog", "Information");
            final String message;
            if (isBooleanColumn) {
                message = selector.getEnvironment().getMessageProvider().translate("ExplorerDialog", "Value not found");
            } else {
                message = selector.getEnvironment().getMessageProvider().translate("ExplorerDialog", "String \'%s\' not found");
            }
            selector.getEnvironment().messageInformation(title, String.format(message, getFindDialog().getFindWhat()));
            showFindDialog();
        }
    }

    public final QModelIndex getNextIndex(final QModelIndex index) throws InterruptedException, ServiceClientException {
        if (model.hasChildren(index)) {
            for (int row=0; hasNextRow(index, row) ; row++){
                if (searchProgressHandle.wasCanceled()){
                    return null;
                }
                if (model.rowCount(index) > row /*need double check!*/ 
                    && !model.isBrokenEntity(model.index(row, index.column(), index)) ) {
                    return model.index(row, index.column(), index);
                }
            }
            if (model.canReadMore(index)){
                return null;//operation was cancelled by user
            }
        }
        QModelIndex parent;
        for (QModelIndex idx = index; idx != null; idx = idx.parent()) {
            parent = idx.parent();
            for (int row=idx.row() + 1; hasNextRow(parent, row); row++){
                if (searchProgressHandle.wasCanceled()){
                    return null;
                }
                if (model.rowCount(parent) > row /*need double check!*/ 
                    && !model.isBrokenEntity(model.index(row, index.column(), parent)) ) {
                    return model.index(row, index.column(), parent);
                }
            }
            if (model.canReadMore(parent)){
                return null;//operation was cancelled by user
            }            
        }
        return null;
    }
    
    private boolean hasNextRow(final QModelIndex parent, final int row) throws ServiceClientException, InterruptedException{
        return model.rowCount(parent) > row || (model.canReadMore(parent) && readMoreInSearchOperation(parent));
    }
    
    private boolean readMoreInSearchOperation(final QModelIndex index) throws ServiceClientException, InterruptedException{
        final String waitDialogText = 
            selector.getEnvironment().getMessageProvider().translate("Selector", "Searching...\nNumber of Processed Objects: %1s");
        searchProgressHandle.setText(String.format(waitDialogText, numberOfObjectsProcessedInSearch));
        return dataLoader.loadMore(new SelectorWidgetDelegate(model, index));
    }

    public final QModelIndex getPrevIndex(final QModelIndex index) throws InterruptedException, ServiceClientException {
        
        final QModelIndex parent = index.parent();
        QModelIndex prev = null;
        int prevRow;
        for (prevRow = index.row() - 1; prevRow>-1; prevRow--){
           prev = model.index(prevRow, index.column(), parent);
           if (!model.isBrokenEntity(prev)){
               break;
           }
        }
        
        if (prev!=null && prevRow > -1) {
            while (model.hasChildren(prev)) {
                //Формально так правильно, но нужно ли так делать?
                readAll(prev);
                if (model.rowCount(prev) > 0) {
                    int row;
                    for (row = model.rowCount(prev) - 1; row>-1; row--){
                        if (!model.isBrokenEntity(index)){
                            prev = model.index(row, index.column(), prev);
                            break;
                        }                        
                    }
                    if (row<0){
                        return prev;
                    }
                    
                } else {
                    return prev;
                }
            }
            return prev;
        } else if (parent != null) {
            return model.index(parent.row(), index.column(), parent.parent());
        } else {
            return null;
        }
    }

    public final void applySettings() {
        if (view.itemDelegate() instanceof SelectorWidgetItemDelegate) {
            final SelectorWidgetItemDelegate delegate = (SelectorWidgetItemDelegate) view.itemDelegate();
            final ExplorerSettings settings = (ExplorerSettings) selector.getEnvironment().getConfigStore();
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.SELECTOR_GROUP);
            settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
            try {
                setItemDelegateCellFrameColor(settings.readQColor(SettingNames.Selector.Common.FRAME_COLOR));
                setItemDelegateRowFrameColor(settings.readQColor(SettingNames.Selector.Common.ROW_FRAME_COLOR));
            } finally {
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
            }
            delegate.applySettings(view);
        }
        model.setTextMargin(QApplication.style().pixelMetric(QStyle.PixelMetric.PM_FocusFrameHMargin));
        updateIconSize();
    }

    public void setCurrentRow(final int row) throws ServiceClientException, InterruptedException {
        if (locked) {
            return;
        }
        final int column;
        final QModelIndex currentIndex = view.currentIndex();
        if (currentIndex== null){
            if (header==null){
                column = model.isSelectionEnabled() ? 1 : 0;
            }else{
                column = getFirstVisibleColumn();
            }
        }else{
            column = currentIndex.column();
        }        
        final QModelIndex parent = currentIndex==null ? null : currentIndex.parent();

        lockInput();
        try {
            while (row >= model.rowCount(parent) && model.canReadMore(parent)) {
                model.readMore(parent);
            }

            if (row >= model.rowCount(parent)) {
                return;
            }
                        
            if (selector.leaveCurrentEntity(false)) {                
                enterEntity(model.index(row, column, parent));                
            }
        } finally {
            unlockInput();
        }
    }

    public boolean processMousePressEvent(final QMouseEvent event) {
        //super.mousePressEvent(event);
        //event.ignore();
        final QModelIndex index = view.indexAt(event.pos());
        if (index == null) {
            return true;
        } else if (!view.visualRect(index).contains(event.x(), event.y())) {
            //DBP-1655 Щелкнули не на загаловке элемента, а например на "плюсике"
            return false;
        }
        final QModelIndex currentIndex = view.currentIndex();
        if (currentIndex != null && index.internalId() == currentIndex.internalId() && !locked) {
            return false;
        }                        
        final boolean someButton = event.buttons().isSet(MouseButton.RightButton) || event.buttons().isSet(MouseButton.LeftButton);
        if (someButton && !locked) {
            if (isSelectionEnabled() && index.column()==0){
                return false;
            }
            model.setCurrentColumnIndex(index.column());
            lockInput();
            try {
                if (selector.leaveCurrentEntity(false)) {                    
                    enterEntity(index);
                }else{
                    event.ignore();
                }
            } catch (Throwable ex) {
                selector.getEnvironment().processException(ex);
            } finally {
                unlockInput();
            }
        }

        return true;
    }
    
    public void processDoubleClick(final QModelIndex index){
        if (model.getEntity(index) instanceof BrokenEntityModel){
            new BrokenEntityMessageDialog(selector.getEnvironment(), (BrokenEntityModel)model.getEntity(index), view).exec();
        }
    }

    public boolean processKeyPressEvent(final QKeyEvent event) {
        if (event.matches(QKeySequence.StandardKey.Copy) && model != null) {
            //DBP-1658
            final Object obj = model.data(view.currentIndex(), Qt.ItemDataRole.UserRole);
            if (obj instanceof Property) {
                QApplication.clipboard().setText(((Property) obj).getValueAsString());
            }
            event.accept();
            return true;
        }

        final Key key;
        try {
            key = Key.resolve(event.key());
        } catch (QNoSuchEnumValueException exception) {
            return false;
        }
        if (key == Key.Key_Right) {
            final QModelIndex index = view.currentIndex();
            if (index.column() < (model.columnCount() - 1)) {
                view.setCurrentIndex(model.index(index.row(), index.column() + 1, index.parent()));
            }
            return true;
        }

        if (key == Key.Key_Left) {
            final QModelIndex index = view.currentIndex();
            if (index.column() > 0) {
                view.setCurrentIndex(model.index(index.row(), index.column() - 1, index.parent()));
            }
            return true;
        }

        return false;
    }

    public boolean isEditEvent(final QKeyEvent event) {
        final KeyboardModifier modifier;
        final Key key;
        try {
            modifier = KeyboardModifier.resolve(event.modifiers().value());
            key = Key.resolve(event.key());
        } catch (QNoSuchEnumValueException exception) {
            return false;
        }
        boolean isNoMod = (modifier == KeyboardModifier.NoModifier || modifier == KeyboardModifier.KeypadModifier);
        boolean isEnter = key == Key.Key_Enter || key == Key.Key_Return;
        return isNoMod && isEnter && view.currentIndex() != null;
    }

    public void processCurrentChanged(final QModelIndex newIndex, final QModelIndex prevIndex, final QHeaderView header) {
        if (!locked) {
            if (newIndex != null) {
                model.setCurrentColumnIndex(newIndex.column());
            }
            if (prevIndex==null || !indexesEqual(newIndex, prevIndex)){
                setItemDelegateCurrentIndex(newIndex);
                try{
                    enterEntity(newIndex);
                }catch(Throwable exception){
                    selector.getEnvironment().processException(exception);
                }
            }else{
                setItemDelegateCurrentIndex(newIndex);
            }
        }else if (!indexesEqual(newIndex, prevIndex)){
            setItemDelegateCurrentIndex(newIndex);
        }
        if (newIndex != null && header != null) {
            header.headerDataChanged(Qt.Orientation.Horizontal, newIndex.column(), newIndex.column());
        }
        if (prevIndex != null) {
            view.update(prevIndex);//remove focus frame in previous cell
            if (header != null) {
                header.headerDataChanged(Qt.Orientation.Horizontal, prevIndex.column(), prevIndex.column());
            }
        }
        //check if newIndex cell is visible
        if (newIndex != null
                && prevIndex != null
                && newIndex.row() == prevIndex.row()
                && Utils.equals(newIndex.parent(), prevIndex.parent())
                && !view.visibleRegion().intersects(view.visualRect(newIndex))) {
            view.scrollTo(newIndex, QAbstractItemView.ScrollHint.EnsureVisible);
        }
        else if (newIndex!=null){
            view.update(newIndex);//draw focus frame in current cell
        }
    }

    public void processKeyboardSearch() {
        final PropEditor editor = getCurrentPropEditor();
        if (editor != null) {
            editor.setFocus();
        }
    }

    public QModelIndex findEntityIndex(final Pid pid) {
        final QModelIndex currentIndex = view.currentIndex();
        if (currentIndex == null) {
            return null;
        }
        final QModelIndex parent = currentIndex.parent();
        final GroupModel group = model.getChildGroup(parent);
        final int row = group.findEntityByPid(pid);
        if (row < 0) {
            return null;
        }
        return model.index(row, 0, parent);
    }

    public void processEntityRemoved(final Pid pid) {
        final QModelIndex currentIndex = view.currentIndex();
        if (currentIndex == null) {
            return;
        }
        int currentRow = currentIndex.row();
        final int currentColumn = currentIndex.column();
        final QModelIndex parent = currentIndex.parent();
        final GroupModel group = model.getChildGroup(parent);
        final int removedRow = group.findEntityByPid(pid);
        if (removedRow < 0) {
            return;
        }
        final boolean removingCurrentRow = removedRow==currentRow;
        if (removingCurrentRow) {
            selector.getModel().finishEdit();
        }        
        lockInput();
        try {
            
            if (!model.removeRow(removedRow, currentIndex.parent())) {
                return;
            }

            if (model.rowCount(parent) == 0) {
                if (parent == null) {
                    selector.leaveCurrentEntity(true);
                    selector.refresh();
                } else {
                    selector.leaveCurrentEntity(true);
                    enterEntity(model.index(parent.row(), currentColumn, parent.parent()));
                }
                return;
            }

            if (askToFetchMore(parent) && model.canReadMore(parent)) {//RADIX-2968
                model.fetchMore(parent);
            }
            
            if (removingCurrentRow){
                if (currentRow > removedRow) {
                    --currentRow;
                }

                if (currentRow >= group.getEntitiesCount()) {
                    --currentRow;
                }

                if (selector.leaveCurrentEntity(true)) {
                    enterEntity(model.index(currentRow, currentColumn, parent));
                }
            }
        } finally {
            unlockInput();
        }
    }

    public boolean canMoveCursor(final CursorAction action, final QAbstractItemView.State state) {
        return !view.isHidden()
                && state != QAbstractItemView.State.EditingState &&
                selector.getCurrentEntity()!=null;
    }

    public QModelIndex afterMoveCursor(final QModelIndex newIndex) {
        if (newIndex == null || locked) {
            return null;
        }

        if (view.currentIndex() != null && newIndex.internalId() == view.currentIndex().internalId()) {
            return newIndex;
        }
        
        if (isSelectionEnabled() && newIndex.column()==0){
            return null;
        }

        lockInput();
        try {
            if (selector.leaveCurrentEntity(false)) {
                enterEntity(newIndex);
            } else {
                return null;
            }
        } catch (Throwable ex) {
            selector.getEnvironment().processException(ex);
            return null;
        } finally {
            unlockInput();
        }
        return newIndex;
    }

    private boolean isLastChild(final QModelIndex index) {
        return index != null ? model.rowCount(index.parent()) == index.row() + 1 : false;
    }

    @SuppressWarnings("unused")
    private void onSetCurrentEntity(final EntityModel entity) {
        if (entity != null) {
            model.subscribeProperties(entity, selector.getSelectorWidget());
        } else {
            model.unsubscribeProperties(selector.getSelectorWidget());
        }
    }

    @SuppressWarnings("unused")
    private void createAndShowPopupMenu(final QPoint point) {
        final QMenu menu = new QMenu(selector.getEnvironment().getMessageProvider().translate("Selector", "Columns visibility"), header);
        QAction action;

        int disabledColumnsCount = 0;
        for (SelectorColumnModelItem column : model.getSelectorColumns()) {
            if (!column.isVisible()) {
                ++disabledColumnsCount;
            }
        }

        boolean onlyOneColumnVisible = model.getSelectorColumns().size() - disabledColumnsCount == 1;

        for (SelectorColumnModelItem column : model.getSelectorColumns()) {
            if (!column.isForbidden()){
                action = new QAction(column.getTitle(), null);
                action.setCheckable(true);
                action.setChecked(column.isVisible());
                action.triggered.connect(column, "setVisible(boolean)");
                if (menu.isEmpty() && view instanceof QTreeView) {
                    action.setChecked(true);
                    action.setEnabled(false);
                    onlyOneColumnVisible = false;
                } else if (column.isVisible() && onlyOneColumnVisible) {
                    action.setEnabled(false);
                }
                menu.addAction(action);
            }
        }
        menu.exec(header.mapToGlobal(point));
    }
        
    public void processColumnHeaderClicked(final int columnIndex){
        final Id colId  = model.getSelectorColumns().get(columnIndex).getId();
        final RadClassPresentationDef classPresentation = 
            selector.getEnvironment().getDefManager().getClassPresentationDef(selector.getModel().getDefinition().getOwnerClassId());
        if(classPresentation != null) {
            final RadPropertyDef propDef = classPresentation.getPropertyDefById(colId);
            if(propDef != null && propDef.canBeUsedInSorting()) {
                final boolean isShiftKeyPressed = 
                    QApplication.keyboardModifiers().isSet(Qt.KeyboardModifier.ShiftModifier);
                SelectorSortUtils.applySort((GroupModel)selector.getModel(), colId, isShiftKeyPressed);
            }
        }
    }

    public QModelIndex findIndexForProperty(final Property property) {
        final QModelIndex current = view.currentIndex();
        if (current != null && property != null) {
            final QModelIndex parent = current.parent();
            final Id propertyId = property.getId();
            Property prop;
            QModelIndex index;
            for (int i = model.columnCount() - 1; i >= 0; i--) {
                index = model.index(current.row(), i, parent);
                prop = (Property) model.data(index, ItemDataRole.UserRole);
                if (prop != null && propertyId.equals(prop.getId())) {
                    return index;
                }
            }
        }
        return null;
    }

    public PropEditor getCurrentPropEditor() {
        return ((WrapModelDelegate) view.itemDelegate()).getActivePropEditor();
    }

    private boolean askToFetchMore(final QModelIndex parentIndex) {
        //based on void QAbstractItemViewPrivate::_q_fetchMore() (qabstractitemview.cpp)
        final int lastRow = model.rowCount(parentIndex) - 1;
        if (lastRow > 0) {
            final QRect visibleRegion = view.viewport().rect();
            QModelIndex index;
            QRect itemRect;
            for (int column = 0; column < model.columnCount(parentIndex); column++) {
                index = model.index(lastRow, column, parentIndex);
                itemRect = view.visualRect(index);
                if (visibleRegion.intersects(itemRect)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public final void processErrorOnReceivingData(final Throwable exception) {
        final String title = selector.getModel().getEnvironment().getMessageProvider().translate("ExplorerException", "Error on receiving data");
//        selector.getModel().getEnvironment().getTracer().error(title, exception);        
        selector.getModel().showException(title, exception);
    }
    
    private static boolean indexesEqual(final QModelIndex index1, final QModelIndex index2) {
        return index1 == null ? index2 == null : index2 != null && index1.internalId() == index2.internalId();
    }    
        
    public void enableSelection(){
        if (header!=null){
            header.setSectionHidden(0, false);
            view.update();
        }
    }
    
    public void disableSelection(){
        if (header!=null){
            header.setSectionHidden(0, true);
            view.update();
        }
    }
    
    public boolean isSelectionEnabled(){
        return model.isSelectionEnabled() && header!=null && !header.isSectionHidden(0);
    }        
    
    private void setItemDelegateFrameVisible(final boolean isFrameVisible){
        if (view.itemDelegate() instanceof ItemDelegateWithFocusFrame){
            ((ItemDelegateWithFocusFrame)view.itemDelegate()).setFocusFrameVisible(isFrameVisible);
        }
        selectionColumnDelegate.setFocusFrameVisible(isFrameVisible);
    }
    
    private void setItemDelegateCellFrameColor(final QColor cellFrameColor){
        if (view.itemDelegate() instanceof ItemDelegateWithFocusFrame){
            ((ItemDelegateWithFocusFrame)view.itemDelegate()).setCurrentCellFrameColor(cellFrameColor);
        }
        selectionColumnDelegate.setCurrentCellFrameColor(cellFrameColor);
    }
    
    private void setItemDelegateRowFrameColor(final QColor rowFrameColor){
        if (view.itemDelegate() instanceof ItemDelegateWithFocusFrame){
            ((ItemDelegateWithFocusFrame)view.itemDelegate()).setCurrentRowFrameColor(rowFrameColor);
        }
        selectionColumnDelegate.setCurrentRowFrameColor(rowFrameColor);
    }    
    
    public void setItemDelegateCurrentIndex(final QModelIndex index){
        if (view.itemDelegate() instanceof ItemDelegateWithFocusFrame){
            ((ItemDelegateWithFocusFrame)view.itemDelegate()).setCurrentIndex(index);
        }
        selectionColumnDelegate.setCurrentIndex(index);
    }
    
    public int getFirstVisibleColumn(){        
        final int columnsCount = header.count();
        int idx;        
        for (int col = isSelectionEnabled() ? 1 : 0; col <= columnsCount; col++) {
            idx = header.logicalIndex(col);//logical index of column
            if (idx >= 0 && !header.isSectionHidden(idx)) {
                return idx;
            }
        }
        return 0;
    }    
    
    public void close(){
        view.setStyle(null);
        WidgetUtils.CustomStyle.release(style);
    }
}