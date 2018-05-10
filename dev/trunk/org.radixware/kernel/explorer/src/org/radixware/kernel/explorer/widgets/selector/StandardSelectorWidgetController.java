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
import com.trolltech.qt.core.QAbstractItemModel;
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
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EHierarchicalSelectionMode;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.errors.SettingPropertyValueError;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.EntityObjectsSelection;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.HierarchicalSelection;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.MatchOptions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.utils.ThreadDumper;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.widgets.selector.ISelectorWidget;
import org.radixware.kernel.common.client.widgets.selector.SelectorModelDataLoader;
import org.radixware.kernel.common.client.widgets.selector.SelectorSortUtils;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.ESelectorColumnSizePolicy;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.editors.valeditors.TristateCheckBoxStyle;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;

import org.radixware.kernel.explorer.views.selector.Selector;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditor;


public class StandardSelectorWidgetController {       
    
    private final static String ROWS_LIMIT_FOR_SEARCH_CONFIG_PATH = 
        SettingNames.SYSTEM+"/"+SettingNames.SELECTOR_GROUP+"/"+SettingNames.Selector.COMMON_GROUP+"/"+SettingNames.Selector.Common.ROWS_LIMIT_FOR_SEARCH;
    
    private final static String ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH =
            SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.ROWS_LIMIT_FOR_NAVIGATION;    
    
    private final static String MULTIPLE_SELECTION_ENABLED_BY_DEFAULT_CONFIG_PATH =
            SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.MULTIPLE_SELECTION_MODE_ENABLED_BY_DEFAULT;
    
    public interface ISelectionModeListener{
        void afterSwitchSelectionMode(final boolean isSelectionEnabled);
    }
    
    public static interface IndexIteratorFactory{
        Iterator<QModelIndex> create(final QModelIndex startIndex, final QModelIndex endIndex);
    }
    
    private static enum ESelectAction{SELECT_SINGLE_OBJECT,
                                                       SELECT_EXPLICIT_NESTED_OBJECTS,
                                                       SELECT_NESTED_OBJECTS_CASCADE,
                                                       UNSELECT_SINGLE_OBJECT,
                                                       UNSELECT_EXPLICIT_NESTED_OBJECTS,
                                                       UNSELECT_NESTED_OBJECTS_CASCADE,
                                                       INVERT_SINGLE_OBJECT_SELECTION};
    
    private final static class SelectionModeMenu{        
        
        private final QMenu menu;
        private final EnumMap<ESelectAction,QAction> selectActions = new EnumMap<>(ESelectAction.class);
        
        public SelectionModeMenu(final QAbstractItemView parent, final MessageProvider mp){
            menu = new QMenu(parent);
            addAction(mp.translate("Selector", "Select object at current level"), ESelectAction.SELECT_SINGLE_OBJECT);
            addAction(mp.translate("Selector", "Select nested objects"), ESelectAction.SELECT_EXPLICIT_NESTED_OBJECTS);
            addAction(mp.translate("Selector", "Select all nested objects cascade"), ESelectAction.SELECT_NESTED_OBJECTS_CASCADE);
            addAction(mp.translate("Selector", "Unselect Object at current level"), ESelectAction.UNSELECT_SINGLE_OBJECT);
            addAction(mp.translate("Selector", "Unselect nested objects"), ESelectAction.UNSELECT_EXPLICIT_NESTED_OBJECTS);
            addAction(mp.translate("Selector", "Unselect all nested objects cascade"), ESelectAction.UNSELECT_NESTED_OBJECTS_CASCADE);
        }
        
        private void addAction(final String actionText, final ESelectAction selectAction){
            final QAction action = menu.addAction(actionText);
            action.setData(selectAction);
            selectActions.put(selectAction, action);
        }
        
        private ESelectAction exec(final QPoint globalPos, final EnumSet<ESelectAction> accessibleActions){
            for (Map.Entry<ESelectAction,QAction> entry: selectActions.entrySet()){
                entry.getValue().setVisible(accessibleActions.contains(entry.getKey()));
            }
            final QAction action = menu.exec(globalPos);
            final Object actionData = action==null ? null : action.data();            
            return actionData instanceof ESelectAction ? (ESelectAction)actionData : null;
        }
    }
                   
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
    private int changingCurrentEntity;
    private boolean locked, changingLock, redrawBlocked, loadingRows;   
    private boolean isSelectionEnabled;
    private final QEventsScheduler scheduler = new QEventsScheduler();
    private final SelectionModeMenu selectionMenu;
    private ISelectionModeListener selectionModelListener;
    
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
        selectionMenu = new SelectionModeMenu(view,environment.getMessageProvider());
        this.selector = selector;
        searchConfirmationMessageTemplate = 
            environment.getMessageProvider().translate("Selector", "%1s objects were loaded, but the required one was not found.\nIt is recommended to use filter to find specific objects.\nDo you want to continue searching among the next %2s objects?");
        dataLoader = new SelectorModelDataLoader(environment);
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
        selectionColumnDelegate = new SelectionItemDelegate(view);
        selectionColumnDelegate.setFocusFrameVisible(true);
        if (environment.getConfigStore().readBoolean(MULTIPLE_SELECTION_ENABLED_BY_DEFAULT_CONFIG_PATH, false)){
            isSelectionEnabled = true;
        }        
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
            if (changingLock){
                selector.getEnvironment().getTracer().debug("Unable to lock selector\n"+ThreadDumper.dumpSync());
                return;
            }            
            changingLock = true;
            try{
                if (needToBlockRedraw()) {
                    //optimization: block redraw only if editor can be opened
                    selector.blockRedraw();
                    redrawBlocked = true;                    
                }
                locked = true;
            }finally{
                changingLock = false;
            }
        }
    }
    
    private boolean needToBlockRedraw(){
        if (selector.getGroupModel().getRestrictions().getIsEditorRestricted() || selector.isSplitterCollapsed()){
            final EntityModel currentEntityModel = selector.getCurrentEntity();
            return currentEntityModel!=null  && currentEntityModel.isEdited();
        }else{
            return true;
        }
    }
    
    public void unlockInput(){
        unlockInput(true);
    }

    public void unlockInput(final boolean setFocus) {        
        if (locked) {
            if (changingLock){
                selector.getEnvironment().getTracer().debug("Unable to unlock selector\n"+ThreadDumper.dumpSync());
                return;
            }
            changingLock = true;
            try{
                if (redrawBlocked){
                    selector.unblockRedraw();
                    redrawBlocked = false;
                }
                scheduler.postScheduledEvents(view);
                if (setFocus){
                    view.setFocus();
                }
                locked = false;
            }finally{
                changingLock = false;
            }
        }
    }
    
    public void postEventAfterUnlock(final QEvent event){
        scheduler.scheduleEvent(event);
    }

    public boolean isLocked() {
        return locked;
    }        
    
    public void setupSelectionColumnDelegate(){
        view.setItemDelegateForColumn(0, selectionColumnDelegate);
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
    
    private boolean enterEntityImpl(final QModelIndex index, final boolean forced) {
        final EntityModel entity = index==null ? null : model.getEntity(index);    
        if (entity != null && (selector.getCurrentEntity() != entity || forced)) {
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
                }else{
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
        return entity!=null && selector.getCurrentEntity()==entity;
    }
    
    public boolean enterEntity(final QModelIndex index) {
        changingCurrentEntity++;
        try{
            return enterEntityImpl(index, false);
        }finally{
            changingCurrentEntity--;
        }
    }
    
    public boolean forcedEnterEntity(final QModelIndex index) {
        changingCurrentEntity++;
        try{
            return enterEntityImpl(index, true);
        }finally{
            changingCurrentEntity--;
        }
    }
    
    public boolean changeCurrentEntity(final QModelIndex index,
                                                          final boolean forcedLeaveCurrentEntity,
                                                          final boolean forcedSetCurrentEntity){
        changingCurrentEntity++;
        try{
            if (selector.leaveCurrentEntity(forcedLeaveCurrentEntity) || forcedSetCurrentEntity){
                return index==null ? true : enterEntityImpl(index, forcedSetCurrentEntity);
            }else{
                return false;
            }
        }finally{
            changingCurrentEntity--;
        }
    }
    
    public boolean changingCurrentEntity(){
        return changingCurrentEntity>0;
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
            final int searchColumnIndexInSelector = dialog.getColumnIdx();
            final int searchColumnIndexInModel = model.isSelectionEnabled() ? searchColumnIndexInSelector+1 : searchColumnIndexInSelector;
            final MatchOptions matchOptions = 
                new MatchOptions(dialog.isMatchCaseChecked(), dialog.isWholeWordChecked());            
            final String searchString = dialog.getFindWhat();
            final boolean isBooleanColumn = model.getSelectorColumns().get(searchColumnIndexInSelector).getPropertyDef().getType() == EValType.BOOL;            
            QModelIndex first = model.index(currentIndex.row(), searchColumnIndexInModel, currentIndex.parent());
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
                    if (model.isBrokenPropertyValue(currentIndex)){
                        property = null;
                    }else{
                        property = (Property) model.data(currentIndex, ItemDataRole.UserRole);
                    }
                    if (property!=null){
                        displayText = model.getTextToDisplay(property, -1/*don`t cut text*/);
                        if (displayText!=null && property.valueMatchesToSearchString(displayText, searchString, matchOptions)){
                            if (dataLoader.getLoadedObjectsCount()>0){
                                model.increaseRowsLimit();
                            }
                            if (changeCurrentEntity(currentIndex, false, true) && dataLoader.getLoadedObjectsCount()>0 ){
                                model.increaseRowsLimit();
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
        model.clearTextOptionsCache();
        updateIconSize();
    }

    public void setCurrentRow(final int row, final int column) throws ServiceClientException, InterruptedException {
        if (locked) {
            return;
        }
        
        final QModelIndex currentIndex = view.currentIndex();
        final QModelIndex parent = currentIndex==null ? null : currentIndex.parent();

        lockInput();
        try {
            while (row >= model.rowCount(parent) && model.canReadMore(parent)) {
                model.readMore(parent);
            }

            if (row >= model.rowCount(parent)) {
                return;
            }
            changeCurrentEntity(model.index(row, column, parent), false, false);
        } finally {
            unlockInput();
        }
    }

    public boolean processMousePressEvent(final QMouseEvent event, 
                                                                final boolean isSelectionAllowed,
                                                                final boolean isSelectionEnabled,
                                                                final IndexIteratorFactory indexIteratorFactory
                                                                ) {
        final QModelIndex index = view.indexAt(event.pos());
        if (index == null) {
            return true;
        }
        final boolean insideCell = view.visualRect(index).contains(event.x(), event.y());
        final QModelIndex currentIndex = view.currentIndex();
        final boolean changingRow = currentIndex==null || index.internalId() != currentIndex.internalId();        
        final boolean changingIndex = changingRow || index.column()!=currentIndex.column();
        if (!insideCell){
            processMouseClick(insideCell, changingIndex, index);
            event.ignore();
            return true;
        }
        if (!changingRow && !locked){
            processMouseClick(insideCell, changingIndex, index);
            event.ignore();
            return true;
        }
               
        final boolean someButton = event.buttons().isSet(MouseButton.RightButton) || event.buttons().isSet(MouseButton.LeftButton);
        if (someButton && !locked) {
            if (isSelectionEnabled && index.column()==0){
                selector.getModel().finishEdit();
                event.ignore();
                return true;
            }
            model.setCurrentColumnIndex(index.column());
            lockInput();
            try {
                changeCurrentEntity(index, false, false);
                event.ignore();                
            } catch (Throwable ex) {
                selector.getEnvironment().processException(ex);
            } finally {
                unlockInput();
            }
        }
        if (currentIndex!=null && isSelectionAllowed){
            processSelectionEvent(event, index, currentIndex, indexIteratorFactory, null);
        }
        return true;
    }
    
    private void processMouseClick(final boolean insideCell, final boolean changingIndex, final QModelIndex newIndex){
        if (newIndex!=null){
            final boolean clickInsideSelectionColumn = newIndex.column()==0 && model.isSelectionEnabled();
            if (clickInsideSelectionColumn){
                selector.getModel().finishEdit();
            }else{
                if (insideCell && (changingIndex || !inEditingMode())){
                    if (changingIndex){
                        view.setCurrentIndex(newIndex);
                    }
                    openEditor(newIndex);
                }else if (changingIndex){
                    view.repaint(view.visualRect(newIndex));
                }
            }
        }
    }
    
    private boolean processSelectionEvent(final QMouseEvent event, 
                                                              final QModelIndex eventIndex, 
                                                              final QModelIndex currentIndex,
                                                              final IndexIteratorFactory indexIteratorFactory,
                                                              final ESelectAction defaultSelectAction
                                                             ){
        final ESelectAction selectAction;
        final boolean multipleSelection;        
        if (event.modifiers().isSet(Qt.KeyboardModifier.ShiftModifier)){
            selectAction = ESelectAction.SELECT_SINGLE_OBJECT;            
            multipleSelection = true;
        }else if (event.modifiers().isSet(Qt.KeyboardModifier.ControlModifier)){
            selectAction = ESelectAction.UNSELECT_SINGLE_OBJECT;
            multipleSelection = true;
        }else{
            selectAction = defaultSelectAction;
            multipleSelection = false;
        }
        if (selectAction!=null){
            final QModelIndex startIndex;
            final QModelIndex endIndex;
            if (multipleSelection){
                if (compareIndexes(currentIndex, eventIndex)<0){
                    startIndex = currentIndex;
                    endIndex = eventIndex;                
                }else{
                    startIndex = eventIndex;
                    endIndex = currentIndex;                
                }
            }else{
                startIndex = eventIndex;
                endIndex = eventIndex;
            }
            if (event.button()==MouseButton.LeftButton){
                final Iterator<QModelIndex> iterator = indexIteratorFactory.create(startIndex, endIndex);
                return changeSelection(iterator, selectAction, true);
            }else if (event.button()==MouseButton.RightButton){
                final EnumSet<ESelectAction> hierarchySelectActions;
                if (multipleSelection){
                    final Iterator<QModelIndex> iterator = indexIteratorFactory.create(startIndex, endIndex);
                    hierarchySelectActions = calcAdditionalSelectActionsForInterval(selectAction, iterator);
                }else{
                    hierarchySelectActions = calcAdditionalSelectActionsForObject(selectAction, eventIndex);
                }
                final ESelectAction hierarchySelectAction;
                if (hierarchySelectActions.isEmpty()){
                    hierarchySelectAction = null;
                }else{
                    hierarchySelectAction =  chooseSelectAction(event.globalPos(), hierarchySelectActions);
                }
                if (hierarchySelectAction!=null){
                    if (multipleSelection){
                        final Iterator<QModelIndex> iterator = indexIteratorFactory.create(startIndex, endIndex);
                        return changeSelection(iterator, hierarchySelectAction, false);
                    }else{
                        return changeSelection(model.getSelection(), eventIndex, hierarchySelectAction);
                    }
                }
            }
        }
        return false;
    }
    
    private EnumSet<ESelectAction> calcAdditionalSelectActionsForInterval(final ESelectAction selectAction,
                                                                                                                final Iterator<QModelIndex> iterator){
        final HierarchicalSelection<SelectorNode> selection = model.getSelection();
        final EnumSet<ESelectAction> hierarchySelectActions = EnumSet.noneOf(ESelectAction.class);
        final int maxSelectActionsCount = EHierarchicalSelectionMode.values().length;
        QModelIndex index;
        GroupModel childGroup;
        EntityObjectsSelection childGroupSelection;
        SelectorNode node;
        List<EHierarchicalSelectionMode> additionalSelectionMode;
        while(hierarchySelectActions.size()<maxSelectActionsCount && iterator.hasNext()){
            index = iterator.next();
            node = model.getSelectorNode(index);
            if (node!=null){
                additionalSelectionMode = model.calcAdditionalSelectionModes(node);
                if (selectAction==ESelectAction.SELECT_SINGLE_OBJECT){
                    appendSelectActions(hierarchySelectActions, additionalSelectionMode);
                }else if (selectAction==ESelectAction.UNSELECT_SINGLE_OBJECT){
                    appendUnselectActions(hierarchySelectActions, additionalSelectionMode);
                }else{
                    throw new IllegalArgumentException("Unexpected value of selectAction ("+selectAction.name()+")");
                }
            }
        }
        return hierarchySelectActions;
    }
    
    private EnumSet<ESelectAction> calcAdditionalSelectActionsForObject(final ESelectAction selectAction,
                                                                                                              final QModelIndex index){
        final HierarchicalSelection<SelectorNode> selection = model.getSelection();
        final EnumSet<ESelectAction> hierarchySelectActions = EnumSet.noneOf(ESelectAction.class);
        final SelectorNode node = model.getSelectorNode(index);
        if (node==null){
            return hierarchySelectActions;
        }
        
        final List<EHierarchicalSelectionMode> additionalSelectionMode = model.calcAdditionalSelectionModes(node);
        if (additionalSelectionMode.isEmpty()){
            return hierarchySelectActions;
        }
        switch(selectAction){
            case INVERT_SINGLE_OBJECT_SELECTION:{
                if (additionalSelectionMode.contains(EHierarchicalSelectionMode.EXPLICIT_NESTED_OBJECTS)){
                    final GroupModel childGroup = model.getChildGroup(index);
                    final EntityObjectsSelection childGroupSelection = childGroup==null ? null : childGroup.getSelection();
                    if (childGroupSelection!=null){
                        if (childGroupSelection.isAllObjectsSelected()){
                            hierarchySelectActions.add(ESelectAction.UNSELECT_EXPLICIT_NESTED_OBJECTS);
                        }else{
                            hierarchySelectActions.add(ESelectAction.SELECT_EXPLICIT_NESTED_OBJECTS);
                        }
                    }
                }
                if (additionalSelectionMode.contains(EHierarchicalSelectionMode.ALL_NESTED_OBJECTS_CASCADE)){
                    if (selection.hasRecursiveSelection(node)){
                        hierarchySelectActions.add(ESelectAction.UNSELECT_NESTED_OBJECTS_CASCADE);
                    }else{
                        if (selection.isSomeChildNodeSelected(node, true)){
                            hierarchySelectActions.add(ESelectAction.UNSELECT_NESTED_OBJECTS_CASCADE);
                        }
                        hierarchySelectActions.add(ESelectAction.SELECT_NESTED_OBJECTS_CASCADE);
                    }
                }
                if (additionalSelectionMode.contains(EHierarchicalSelectionMode.SINGLE_OBJECT)){
                    if (selection.isSelected(node)){
                        hierarchySelectActions.add(ESelectAction.SELECT_SINGLE_OBJECT);
                    }else{
                        hierarchySelectActions.add(ESelectAction.UNSELECT_SINGLE_OBJECT);
                    }
                }
                break;
            }case SELECT_SINGLE_OBJECT: {
                appendSelectActions(hierarchySelectActions, additionalSelectionMode);
                break;
            }case UNSELECT_SINGLE_OBJECT:{
                appendUnselectActions(hierarchySelectActions, additionalSelectionMode);
                break;
            }default:
                throw new IllegalArgumentException("Unexpected value of selectAction ("+selectAction.name()+")");
        }
        return hierarchySelectActions;
    }
    
    private static void appendSelectActions(final EnumSet<ESelectAction> actions,
                                                               final List<EHierarchicalSelectionMode> allowedActions){
        if (allowedActions.contains(EHierarchicalSelectionMode.EXPLICIT_NESTED_OBJECTS)){
            actions.add(ESelectAction.SELECT_EXPLICIT_NESTED_OBJECTS);
        }
        if (allowedActions.contains(EHierarchicalSelectionMode.ALL_NESTED_OBJECTS_CASCADE)){
            actions.add(ESelectAction.SELECT_NESTED_OBJECTS_CASCADE);
        }
        if (allowedActions.contains((EHierarchicalSelectionMode.SINGLE_OBJECT))){
            actions.add(ESelectAction.SELECT_SINGLE_OBJECT);
        }        
    }
    
    private static void appendUnselectActions(final EnumSet<ESelectAction> actions,
                                                                   final List<EHierarchicalSelectionMode> allowedActions){    
        if (allowedActions.contains(EHierarchicalSelectionMode.EXPLICIT_NESTED_OBJECTS)){
            actions.add(ESelectAction.UNSELECT_EXPLICIT_NESTED_OBJECTS);
        }
        if (allowedActions.contains(EHierarchicalSelectionMode.ALL_NESTED_OBJECTS_CASCADE)){
            actions.add(ESelectAction.UNSELECT_NESTED_OBJECTS_CASCADE);
        }
        if (allowedActions.contains((EHierarchicalSelectionMode.SINGLE_OBJECT))){
            actions.add(ESelectAction.UNSELECT_SINGLE_OBJECT);
        }
    }    
    
    public boolean processEditEvent(final QEvent event,
                                                    final QModelIndex index,
                                                    final boolean isSelectionEnabled,
                                                    final IndexIteratorFactory indexIteratorFactory){        
        final QModelIndex currentIndex = view.currentIndex();
        if (inEditingMode()) {
            if (index!=null && index.equals(currentIndex)) {
                if (event!=null){
                    event.ignore();
                }
                return true;
            } else {
                selector.getModel().finishEdit();
            }
        }        
        final boolean onChangeSelection = isSelectionEnabled && index!=null && index.column()==0;        
        if (onChangeSelection  && currentIndex!=null && event instanceof QMouseEvent){
            final QMouseEvent mouseEvent = (QMouseEvent)event;
            mouseEvent.ignore();
            return processSelectionEvent(mouseEvent, index, currentIndex, indexIteratorFactory, ESelectAction.INVERT_SINGLE_OBJECT_SELECTION);
        }else{
            return false;
        }
    }
    
    public void processDoubleClick(final QModelIndex index){
        if (model.getEntity(index) instanceof BrokenEntityModel){
            new BrokenEntityMessageDialog(selector.getEnvironment(), (BrokenEntityModel)model.getEntity(index), view).exec();
        }
    }

    boolean processKeyPressEvent(final QKeyEvent event,
                                                   final EHierarchicalSelectionMode selectAllMode,
                                                   final SelectorHorizontalHeader horizontalHeader) {
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

        if (event.matches(QKeySequence.StandardKey.SelectAll)){
            selectAllObjects(horizontalHeader, selectAllMode);
            return true;
        }
        
        if (isEditEvent(event) && !inEditingMode() && openEditor(view.currentIndex())){
            return true;
        }
        
        //Ctrl+Space
        final boolean isControl = event.modifiers().value() == KeyboardModifier.ControlModifier.value()
                || (event.modifiers().value() == KeyboardModifier.MetaModifier.value() && SystemTools.isOSX);
        final boolean isSpace = event.key() == Key.Key_Space.value();
        if (isControl && isSpace) {
            final QModelIndex index = view.currentIndex();
            if (index != null) {
                final Object obj = model.data(view.currentIndex(), Qt.ItemDataRole.UserRole);
                if (obj instanceof Property) {
                    final Property property = (Property) obj;
                    if (property.getDefinition().getType() == EValType.BOOL
                        && canEditPropertyValue(property) && !property.isMandatory()) {
                        try {
                            property.setValueObject(null);
                            return true;
                        } catch (Exception ex) {
                            property.getEnvironment().processException(new SettingPropertyValueError(property, ex));
                        }
                    }                    
                }
            }
        }                
        return false;
    }
    
    private static boolean canEditPropertyValue(final Property property) {
        return !property.isReadonly()
                && (property.hasOwnValue() || !property.isValueDefined())
                && !property.isCustomEditOnly()
                && property.getEditPossibility() != EEditPossibility.PROGRAMMATICALLY;
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
            final QModelIndex removedRowIndex = model.index(removedRow, 0, currentIndex.parent());
            final SelectorNode node = model.getSelectorNode(removedRowIndex);
            if (node==null){
                return;
            }
            model.getSelection().clear(node, true, true, true);
            if (!model.removeRow(removedRow, currentIndex.parent())) {
                return;
            }

            if (model.rowCount(parent) == 0) {
                if (parent == null) {
                    selector.leaveCurrentEntity(true);
                    selector.refresh();
                } else {
                    changeCurrentEntity(model.index(parent.row(), currentColumn, parent.parent()), true, false);
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
                
                changeCurrentEntity(model.index(currentRow, currentColumn, parent), true, false);
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

    public QModelIndex afterMoveCursor(final QModelIndex newIndex,
                                                            final boolean isSelectionAllowed,
                                                            final boolean isSelectionEnabled,
                                                            final IndexIteratorFactory indexIteratorFactory,
                                                            final Qt.KeyboardModifiers modifiers) {
        if (newIndex == null || locked) {
            return null;
        }

        if (view.currentIndex() != null && newIndex.internalId() == view.currentIndex().internalId()) {
            return newIndex;
        }            
        
        if (isSelectionEnabled && newIndex.column()==0){
            return null;
        }

        lockInput();
        try {
            if (!changeCurrentEntity(newIndex, false, false)){
                return null;
            }
        } catch (Throwable ex) {
            selector.getEnvironment().processException(ex);
            return null;
        } finally {
            unlockInput();
        }
        if (isSelectionAllowed && modifiers.isSet(Qt.KeyboardModifier.ShiftModifier)){
            final QModelIndex currentIndex = view.currentIndex();
            if (currentIndex!=null){
                final Iterator<QModelIndex> iterator = indexIteratorFactory.create(currentIndex, currentIndex);
                changeSelection(iterator, ESelectAction.INVERT_SINGLE_OBJECT_SELECTION, true);
            }
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
        
    public void processColumnHeaderClicked(final int section){        
        if (section>0){
            final int columnIndex = section - 1;
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
    }
    
    public void processRowHeaderDoubleClicked(final QModelIndex index){
        final EntityModel entityModel = model.getEntity(index);
        if (entityModel!=null 
            && entityModel instanceof BrokenEntityModel==false
            && selector.insertEntity(entityModel)==null
            && enterEntity(index)){
            selector.getActions().getEntityActivatedAction().trigger();
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
        final QAbstractItemDelegate delegate = view.itemDelegate();
        if (delegate instanceof WrapModelDelegate){
            return ((WrapModelDelegate)delegate).getActivePropEditor();
        }else{
            return null;
        }        
    }
    
    public boolean inEditingMode(){
        return getCurrentPropEditor()!=null;
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
    
    public static Qt.CheckState getCheckState(final QAbstractItemModel model, final QModelIndex index){
        // make sure that the item is checkable
        Qt.ItemFlags flags = model.flags(index);
        if (!flags.isSet(Qt.ItemFlag.ItemIsUserCheckable) || !flags.isSet(Qt.ItemFlag.ItemIsEnabled)){
            return null;
        }

        final Object checkData = model.data(index, Qt.ItemDataRole.CheckStateRole);
        if (checkData instanceof Integer){
            try{
                return Qt.CheckState.resolve((Integer)checkData);
            }catch(QNoSuchEnumValueException ex){
                return null;
            }
        }else if (checkData instanceof Qt.CheckState){
            return (Qt.CheckState)checkData;
        }else{
            return null;
        }
    }
    
    boolean selectAllObjects(final SelectorHorizontalHeader horizontalHeader, EHierarchicalSelectionMode selectionMode){
        if (selectionMode!=null && selectionMode!=EHierarchicalSelectionMode.SINGLE_OBJECT){
            final boolean cascade = selectionMode==EHierarchicalSelectionMode.ALL_NESTED_OBJECTS_CASCADE;
            return selectAllChildObjects(null, cascade, horizontalHeader);
        }else{
            return false;
        }
    }

    void invertSelection(final SelectorHorizontalHeader horizontalHeader, final EHierarchicalSelectionMode selectionMode){
        if (selectionMode!=null && selectionMode!=EHierarchicalSelectionMode.SINGLE_OBJECT){
            final boolean cascade = selectionMode==EHierarchicalSelectionMode.ALL_NESTED_OBJECTS_CASCADE;
            if (horizontalHeader.getSectionCheckState(0)==Qt.CheckState.Checked){
                selectAllChildObjects(null, cascade, horizontalHeader);
            }else{
                if (confirmToClearSelection()){
                    if (cascade){
                        model.getSelection().clear(null, true, true, true);
                    }else{
                        model.getSelection().unselectAllChildNodes(null, false);
                    }
                }else{
                    updateHeaderCheckState(horizontalHeader, true);
                }
            }
        }
    }    
    
    boolean switchSelectionMode(final SelectorHorizontalHeader horizontalHeader){
        if (!view.isEnabled() || model.getRootGroupModel().isEmpty()){
            return false;
        }
        selector.getModel().finishEdit();
        if (isSelectionEnabled(horizontalHeader)){
            final HierarchicalSelection<SelectorNode> selection = model.getSelection();
            if (!selection.isEmpty()){
                if (confirmToClearSelection()){
                    selection.clear(null, true, true, true);
                    if (!selection.isEmpty()){
                        return false;//cleaning selection was rejected in some handler
                    }
                }else{
                    return false;
                }
            }
            isSelectionEnabled = false;
            return setSelectionEnabled(false, horizontalHeader);            
        }else{
            isSelectionEnabled = true;
            return setSelectionEnabled(true, horizontalHeader);
        }
    }
    
    private boolean confirmToClearSelection(){
        final IClientEnvironment environment = selector.getEnvironment();
        final String title = environment.getMessageProvider().translate("ExplorerMessage", "Confirm to Clear Selection");
        final String message = environment.getMessageProvider().translate("ExplorerMessage", "Do you really want to clear selection?");
        return environment.messageConfirmation(title, message);
    }    
    
    private int getCurrentColumn(final SelectorHorizontalHeader horizontalHeader){
        final QModelIndex currentIndex = view.currentIndex();
        return currentIndex==null ? horizontalHeader.getFirstVisibleColumnIndex() : currentIndex.column();
    }
    
    private void updateCurrentColumn(final SelectorHorizontalHeader horizontalHeader) {//set current index to first visible column
        final QModelIndex currentIndex = view.currentIndex();
        if (currentIndex != null) {//update current column index
            final int column = horizontalHeader.getFirstVisibleColumnIndex();
            final QModelIndex newIndex = model.index(currentIndex.row(), column, currentIndex.parent());
            view.setCurrentIndex(newIndex);
            setItemDelegateCurrentIndex(newIndex);
        }
    }
    
    public final boolean openEditor(final QModelIndex index) {
        if (index == null || model == null || !model.flags(index).isSet(Qt.ItemFlag.ItemIsEditable)) {
            return false;
        }
        final Property property = (Property) model.data(index, Qt.ItemDataRole.UserRole);
        if (property != null && property.getDefinition().getType() != EValType.BOOL) {
            view.edit(index);
            return true;
        }
        return false;
    }
    
    boolean isSelectionEnabled(final SelectorHorizontalHeader horizontalHeader){
        return model.isSelectionEnabled() && !horizontalHeader.isSectionHidden(0);        
    }    
        
    void refreshCornerWidget(final TableCornerButton cornerButton,
                                            final boolean isSelectionEnabled,
                                            final boolean isSelectionAllowed){
        cornerButton.setClickEnabled(isSelectionAllowed);
        refreshCornerWidgetToolTip(cornerButton, isSelectionEnabled);
    }
    
    private void refreshCornerWidgetToolTip(final TableCornerButton cornerButton, final boolean isSelectionEnabled){
        final MessageProvider mp = selector.getEnvironment().getMessageProvider();
        if (cornerButton.isClickEnabled()){
            if (isSelectionEnabled){
                cornerButton.setToolTip(mp.translate("Selector", "Disable Multiple Selection Mode"));
            }else{
                cornerButton.setToolTip(mp.translate("Selector", "Enable Multiple Selection Mode"));
            }            
        }else{
            cornerButton.setToolTip("");
        }        
    }
    
    private boolean setSelectionEnabled(final boolean isEnabled, 
                                                           final SelectorHorizontalHeader horizontalHeader){
        if (isEnabled!=isSelectionEnabled(horizontalHeader)){
            final QModelIndex index = view.currentIndex();
            final QModelIndex parentIndex = index==null ? null : index.parent();
            final int currentRow = index==null ? -1 : index.row();
            final int currentColumn = index==null ? -1 : getCurrentColumn(horizontalHeader);            
            horizontalHeader.setSectionHidden(0, !isEnabled);
            if (currentColumn>=0 && currentRow>=0){
                final QModelIndex newIndex = model.index(currentRow, currentColumn, parentIndex);
                view.setCurrentIndex(newIndex);
                setItemDelegateCurrentIndex(newIndex);
            }else{
                updateCurrentColumn(horizontalHeader);
            }
            horizontalHeader.updateFirstVisibleColumnIndex();
            if (selectionModelListener!=null){
                selectionModelListener.afterSwitchSelectionMode(isEnabled);
            }
            return true;
        }
        return false;
    }    
    
    private boolean selectAllChildObjects(final QModelIndex parentIndex, 
                                                            final boolean cascade,
                                                            final SelectorHorizontalHeader horizontalHeader){
        final Stack<QModelIndex> indexes = new Stack<>();                
        QModelIndex currentParentIndex, childIndex;
        GroupModel groupModel;
        SelectorModelDataLoader dataLoader = null;
        //preload objects if necessary
        indexes.push(parentIndex);
        while (!indexes.isEmpty()){
            currentParentIndex = indexes.pop();
            groupModel = model.getChildGroup(currentParentIndex);
            if (groupModel!=null && groupModel.getRestrictions().getIsSelectAllRestricted()){
                if (dataLoader==null){
                    final MessageProvider messageProvider  = selector.getEnvironment().getMessageProvider();
                    final String loadingDialogTitle =  messageProvider.translate("Selector", "Selecting All Objects");
                    final String loadingDialogMessage =  
                        messageProvider.translate("Selector", "Selecting All Objects...\nNumber of Loaded Objects: %1s");
                    final String dontAskText = messageProvider.translate("Selector", "Load All Objects");                    
                    dataLoader = createDataLoader(loadingDialogTitle, loadingDialogMessage, dontAskText);
                }
                if (groupModel.hasMoreRows()
                    && (!loadAllRows(dataLoader, currentParentIndex) || groupModel.hasMoreRows())){
                    return false;
                }
                if (cascade){
                    for (int i=0,count=model.rowCount(currentParentIndex); i<count; i++){
                        childIndex = model.index(i, 0, parentIndex);                        
                        if (!model.isBrokenEntity(childIndex)){
                            indexes.push(childIndex);
                        }
                    }
                }
            }
        }
        //select objects
        final HierarchicalSelection<SelectorNode> selection = model.getSelection();        
        EntityModel entityModel;
        SelectorNode node;
        indexes.push(parentIndex);
        while (!indexes.isEmpty()){
            currentParentIndex = indexes.pop();
            groupModel = model.getChildGroup(currentParentIndex);
            if (groupModel!=null){
                if (groupModel.getRestrictions().getIsSelectAllRestricted()){
                    groupModel.getSelection().clear();
                    for (int i=0,count=model.rowCount(currentParentIndex); i<count; i++){
                        childIndex = model.index(i, 0, parentIndex);
                        node = model.getSelectorNode(childIndex);
                        if (node!=null && !model.isBrokenEntity(childIndex)){
                            entityModel = model.getEntity(childIndex);
                            if (entityModel!=null && groupModel.getEntitySelectionController().isEntityChoosable(entityModel)){
                                selection.select(node);
                            }
                            indexes.push(childIndex);
                        }
                    }
                }else{
                    node = model.getSelectorNode(currentParentIndex);
                    selection.selectAllChildNodes(node, cascade);
                }
            }
            if (groupModel!=null && groupModel.hasMoreRows() && groupModel.getRestrictions().getIsSelectAllRestricted()){
                if (dataLoader==null){
                    final MessageProvider messageProvider  = selector.getEnvironment().getMessageProvider();
                    final String loadingDialogTitle =  messageProvider.translate("Selector", "Selecting All Objects");
                    final String loadingDialogMessage =  
                        messageProvider.translate("Selector", "Selecting All Objects...\nNumber of Loaded Objects: %1s");
                    final String dontAskText = messageProvider.translate("Selector", "Load All Objects");                    
                    dataLoader = createDataLoader(loadingDialogTitle, loadingDialogMessage, dontAskText);
                }
                if (!loadAllRows(dataLoader, currentParentIndex) || groupModel.hasMoreRows()){
                    return false;
                }
                if (cascade){
                    for (int i=0,count=model.rowCount(currentParentIndex); i<count; i++){
                        indexes.push(model.index(i, 0, parentIndex));
                    }
                }
            }            
        }
        setSelectionEnabled(true, horizontalHeader);
        return true;
    }
    
    void clearData(final ISelectorWidget selectorWidget,
                          final TableCornerButton cornerButton,
                          final SelectorHorizontalHeader horizontalHeader){
        selector.getModel().finishEdit();
        model.clear();
        selectorWidget.refresh(null);
        view.setEnabled(false);
        refreshCornerWidget(cornerButton, false, false);
        updateSelectionMode(horizontalHeader, false);
        view.update();
    }
    
    boolean enableSelection(final SelectorHorizontalHeader horizontalHeader){
        isSelectionEnabled = true;
        return updateSelectionMode(horizontalHeader, true);
    }
    
    boolean disableSelection(final SelectorHorizontalHeader horizontalHeader){
        isSelectionEnabled = false;
        model.getSelection().clear(null, true, true, true);
        return updateSelectionMode(horizontalHeader, false);
    }
    
    boolean updateSelectionMode(final SelectorHorizontalHeader horizontalHeader,
                                            final boolean isSelectionAllowed){
        final boolean isEnabled = isSelectionEnabled(horizontalHeader);
        if (isEnabled!=isSelectionAllowed){
            if (isSelectionAllowed){
                setSelectionEnabled(isSelectionEnabled || !model.getSelection().isEmpty(), horizontalHeader);
                return isSelectionEnabled(horizontalHeader);
            }else{
                setSelectionEnabled(false, horizontalHeader);
                return !isSelectionEnabled(horizontalHeader);
            }
        }
        return false;
    }
    
    private boolean loadAllRows(final String title, 
                                       final String message, 
                                       final String dontAskBtnText,
                                       final QModelIndex parentIndex){
        final SelectorModelDataLoader allDataLoader = createDataLoader(title, message, dontAskBtnText);
        return loadAllRows(dataLoader, parentIndex);
    }
    
    private SelectorModelDataLoader createDataLoader(final String title, final String message, final String dontAskBtnText){
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
        return allDataLoader;
    }
    
    private boolean loadAllRows(final SelectorModelDataLoader loader, final QModelIndex parentIndex){
        lockInput();
        loadingRows = true;
        int loadedRows = -1;
        try {
            loadedRows = loader.loadAll(new SelectorWidgetDelegate(model, parentIndex));
        } catch (InterruptedException exception) {
            loadedRows = model.rowCount();
        } catch (ServiceClientException exception) {
            model.showErrorOnReceivingData(exception);
            loadedRows = -1;
        } finally {
            loadingRows = false;
            if (loadedRows > 0) {
                model.increaseRowsLimit();
            }            
            unlockInput();
        }
        return !model.canReadMore(parentIndex);
    }
            
    void updateHeaderCheckState(final SelectorHorizontalHeader horizontalHeader, final boolean canSelectAll){
        if (model.isSelectionEnabled()){
            if (model.getSelection().isEmpty()){
                horizontalHeader.setSectionCheckState(0, Qt.CheckState.Unchecked);
            }else if (model.getSelection().isAllObjectsSelected()){
                horizontalHeader.setSectionCheckState(0, Qt.CheckState.Checked);
            }else{
                horizontalHeader.setSectionCheckState(0, Qt.CheckState.PartiallyChecked);
            }
            horizontalHeader.setSectionUserCheckable(0, canSelectAll && canSelectAll());
            selector.actions.refresh();
        }
    }
    
    private boolean canSelectAll(){
        if (model.isSelectionEnabled()){
            final SelectorNode rootNode = model.getSelectorNode(null);
            final EnumSet<EHierarchicalSelectionMode> selectionModes = model.calcAllSelectionModes(rootNode);            
            return selectionModes.contains(EHierarchicalSelectionMode.ALL_NESTED_OBJECTS_CASCADE)
                      || selectionModes.contains(EHierarchicalSelectionMode.EXPLICIT_NESTED_OBJECTS);
        }else{
            return false;
        }
    }
    
    public boolean loadingRows(){
        return loadingRows;
    }
    
    private boolean changeSelection(final Iterator<QModelIndex> indexIterator, 
                                                     final ESelectAction selectAction,
                                                     final boolean isPrimarySelectAction){
        final HierarchicalSelection<SelectorNode> selection = model.getSelection();
        boolean selectionChanged = false;
        QModelIndex index;
        SelectorNode node;
        EnumSet<EHierarchicalSelectionMode> primarySelectionMode;
        List<EHierarchicalSelectionMode> additionalSelectionModes;
        ESelectAction currentGroupSelectAction;
        ESelectAction hierarchySelectAction;
        while (indexIterator.hasNext()) {
            index = indexIterator.next();
            node = model.getSelectorNode(index);
            if (isPrimarySelectAction){
                primarySelectionMode = model.calcPrimarySelectionMode(node);
                if (primarySelectionMode.isEmpty()){
                    continue;
                }
                if (primarySelectionMode.contains(EHierarchicalSelectionMode.SINGLE_OBJECT)){
                    currentGroupSelectAction = selectAction;
                }else{
                    currentGroupSelectAction = null;
                }
                hierarchySelectAction = 
                        chooseHierarchySelectAction(selectAction, primarySelectionMode, index);
                if ((currentGroupSelectAction!=null || hierarchySelectAction!=null)
                    && changeSelection(selection, index, currentGroupSelectAction, hierarchySelectAction)){
                    selectionChanged = true;
                }
            }else{
                additionalSelectionModes = model.calcAdditionalSelectionModes(node);
                if (additionalSelectionModes.isEmpty()){
                    continue;
                }
                if (isSingleObjectSelectAction(selectAction)){
                    if (additionalSelectionModes.contains(EHierarchicalSelectionMode.SINGLE_OBJECT)
                        && changeSelection(selection, index, selectAction, null)){
                        selectionChanged = true;
                    }
                }else{
                    hierarchySelectAction = getCompatibleHierarchyAction(selectAction, additionalSelectionModes);
                    if (hierarchySelectAction!=null
                        && changeSelection(selection, index, null, hierarchySelectAction)){
                        selectionChanged = true;
                    }
                }
            }
        }
        return selectionChanged;
    }    
    
    private boolean changeSelection(final HierarchicalSelection<SelectorNode> selection,                                                    
                                                     final QModelIndex index,
                                                     final ESelectAction selectAction){
        if (isSingleObjectSelectAction(selectAction)){
            return changeSelection(selection, index, selectAction, null);
        }else{
            return changeSelection(selection, index, null, selectAction);
        }
    }    
    
    private boolean changeSelection(final HierarchicalSelection<SelectorNode> selection,                                                    
                                                     final QModelIndex index,
                                                     final ESelectAction currentGroupSelectAction,
                                                     final ESelectAction hierarchySelectAction){
        if (model.isBrokenEntity(index)){
            return false;
        }else{
            final EntityModel entity = model.getEntity(index);
            final SelectorNode node = model.getSelectorNode(index);            
            final GroupModel groupModel = entity==null ? null : model.getOwnerGroupModel(entity);
            if (groupModel==null || node==null){
                return false;
            }
            boolean selectionChanged = false;
            if (currentGroupSelectAction!=null
                && !groupModel.getRestrictions().getIsMultipleSelectionRestricted()
                && groupModel.getEntitySelectionController().isEntityChoosable(entity)){
                switch(currentGroupSelectAction){
                    case INVERT_SINGLE_OBJECT_SELECTION:{
                        if (selection.invertSelection(node)){
                            selectionChanged = true;
                        }
                        break;
                    }
                    case SELECT_SINGLE_OBJECT:{
                        if (!selection.select(node)){
                            selectionChanged = true;
                        }
                        break;
                    }
                    case UNSELECT_SINGLE_OBJECT:{
                        if (selection.unselect(node)){
                            selectionChanged = true;
                        }
                        break;
                    }
                }
            }
            if (hierarchySelectAction!=null){
                switch(hierarchySelectAction){
                    case SELECT_EXPLICIT_NESTED_OBJECTS:
                        return selection.selectAllChildNodes(node,false) || selectionChanged;
                    case UNSELECT_EXPLICIT_NESTED_OBJECTS:
                        return selection.unselectAllChildNodes(node, false) || selectionChanged;
                    case SELECT_NESTED_OBJECTS_CASCADE:
                        return selection.selectAllChildNodes(node, true) || selectionChanged;
                    case UNSELECT_NESTED_OBJECTS_CASCADE:
                        return selection.unselectAllChildNodes(node, true) || selectionChanged;
                }
            }
            return selectionChanged;
        }
    }
    
    public final boolean invertSelection(final QModelIndex index){
        return changeSelection(model.getSelection(), index, ESelectAction.INVERT_SINGLE_OBJECT_SELECTION, null);
    }
    
    private ESelectAction chooseHierarchySelectAction(final ESelectAction singleObjectSelectAction,
                                                                                 final EnumSet<EHierarchicalSelectionMode> accessibleSelectActions,
                                                                                 final QModelIndex index){
        if (accessibleSelectActions==null
            || accessibleSelectActions.isEmpty()
            || (accessibleSelectActions.size()==1 && accessibleSelectActions.contains(EHierarchicalSelectionMode.SINGLE_OBJECT))){
            return null;
        }else{
            switch (singleObjectSelectAction){
                case SELECT_SINGLE_OBJECT:
                    return getHierarchySelectAction(accessibleSelectActions);
                case UNSELECT_SINGLE_OBJECT:
                    return getHierarchyUnselectAction(accessibleSelectActions);
                case INVERT_SINGLE_OBJECT_SELECTION:
                    final SelectorNode node = index==null ? null : model.getSelectorNode(index);
                    if (node==null){
                        return null;
                    }else{
                        if (model.getSelection().isSelected(node)){
                            return getHierarchyUnselectAction(accessibleSelectActions);
                        }else{
                            return getHierarchySelectAction(accessibleSelectActions);
                        }
                    }
                default:
                    return null;
            }
        }
    }
    
    private static ESelectAction getCompatibleHierarchyAction(final ESelectAction sourceAction, 
                                                                                            final Collection<EHierarchicalSelectionMode> accessibleSelectActions){
        switch (sourceAction){
            case SELECT_EXPLICIT_NESTED_OBJECTS:
            case SELECT_NESTED_OBJECTS_CASCADE:
                return getHierarchySelectAction(accessibleSelectActions);
            case UNSELECT_EXPLICIT_NESTED_OBJECTS:
            case UNSELECT_NESTED_OBJECTS_CASCADE:
                return getHierarchyUnselectAction(accessibleSelectActions);
            default:
                return null;
        }                    
    }
            
    
    private static ESelectAction getHierarchySelectAction(final Collection<EHierarchicalSelectionMode> accessibleSelectActions){
        if (accessibleSelectActions.contains(EHierarchicalSelectionMode.ALL_NESTED_OBJECTS_CASCADE)){
            return ESelectAction.SELECT_NESTED_OBJECTS_CASCADE;
        }else if (accessibleSelectActions.contains(EHierarchicalSelectionMode.EXPLICIT_NESTED_OBJECTS)){
            return ESelectAction.SELECT_EXPLICIT_NESTED_OBJECTS;
        }else{
            return null;
        }
    }
    
    private static ESelectAction getHierarchyUnselectAction(final Collection<EHierarchicalSelectionMode> accessibleSelectActions){
        if (accessibleSelectActions.contains(EHierarchicalSelectionMode.ALL_NESTED_OBJECTS_CASCADE)){
            return ESelectAction.UNSELECT_NESTED_OBJECTS_CASCADE;
        }else if (accessibleSelectActions.contains(EHierarchicalSelectionMode.EXPLICIT_NESTED_OBJECTS)){
            return ESelectAction.UNSELECT_EXPLICIT_NESTED_OBJECTS;
        }else{
            return null;
        }
    }
    
    private static boolean isSingleObjectSelectAction(final ESelectAction selectAction){
        return selectAction==ESelectAction.SELECT_SINGLE_OBJECT
                   || selectAction==ESelectAction.UNSELECT_SINGLE_OBJECT
                   || selectAction==ESelectAction.INVERT_SINGLE_OBJECT_SELECTION;
    }
    
    private ESelectAction chooseSelectAction(final QPoint menuPoint, 
                                                                         final EnumSet<ESelectAction> accessibleSelectActions){
        if (accessibleSelectActions.size()==1){
            return accessibleSelectActions.iterator().next();
        }else{
            return selectionMenu.exec(menuPoint, accessibleSelectActions);
        }
    }                
  
    public void setSelectionModeListener(final ISelectionModeListener listener){
        selectionModelListener = listener;
    }
    
    public static int compareIndexes(final QModelIndex index1, final QModelIndex index2) {
        final List<Integer> path1 = new ArrayList<>();
        final List<Integer> path2 = new ArrayList<>();

        for (QModelIndex index = index1; index != null; index = index.parent()) {
            path1.add(0, index.row());
        }

        for (QModelIndex index = index2; index != null; index = index.parent()) {
            path2.add(0, index.row());
        }

        for (int i = 0; i < path1.size(); i++) {
            if (i >= path2.size() || path1.get(i) > path2.get(i)) {
                return 1;
            } else if (path1.get(i) < path2.get(i)) {
                return -1;
            }
        }

        return 0;
    }
    
        
    public void disposed(){
        WidgetUtils.CustomStyle.release(style);
    }
}