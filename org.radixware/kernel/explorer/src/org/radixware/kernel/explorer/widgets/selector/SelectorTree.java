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

import org.radixware.kernel.explorer.widgets.ExplorerAction;
import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.KeyboardModifiers;
import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QAbstractItemDelegate.EndEditHint;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOption;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QTreeView;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import org.radixware.kernel.common.client.Clipboard;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EHierarchicalSelectionMode;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.AdsVersion;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.errors.ActivatingPropertyError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.HierarchicalSelection;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.GroupRestrictions;
import org.radixware.kernel.common.client.types.ModelRestrictions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.SelectorController;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.client.widgets.selector.IMultiSelectionWidget;
import org.radixware.kernel.common.client.widgets.selector.SelectorModelDataLoader;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ESelectorColumnSizePolicy;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.explorer.dialogs.ExplorerMessageBox;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.utils.ItemDelegatePainter;

import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.selector.Selector;
import org.radixware.kernel.explorer.widgets.FilteredMouseEvent;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditor;

public class SelectorTree extends QTreeView implements IExplorerSelectorWidget, IMultiSelectionWidget {

    private final static String ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH =
            SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.ROWS_LIMIT_FOR_NAVIGATION;
    private final static String ROWS_LIMIT_FOR_RESTORING_POSITION_CONFIG_PATH =
            SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.ROWS_LIMIT_FOR_RESTORING_POSITION;    
    private final static boolean IS_MULTIPLE_SELECTION_ALLOWED =
        SystemPropUtils.getBooleanSystemProp("org.radixware.kernel.explorer.unlock-unfinished-features", false);

    private final static class Icons extends ExplorerIcon.CommonOperations {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        public static final Icons NEXT = new Icons("classpath:images/next.svg");
        public static final Icons PREVIOUS = new Icons("classpath:images/prev.svg");
        public static final Icons BEGIN = new Icons("classpath:images/begin.svg");
        public static final Icons END = new Icons("classpath:images/end.svg");
        public static final Icons CREATE_CHILD = new Icons("classpath:images/add_child.svg");
        public static final Icons CREATE_WITH_POPUP = new Icons("classpath:images/add_with_popup.svg");
        public static final Icons PASTE_CHILD = new Icons("classpath:images/paste_child.svg");
        public static final Icons PASTE_WITH_POPUP = new Icons("classpath:images/paste_with_popup.svg");
        public static final Icons PASTE_SMALL = new Icons("classpath:images/paste_small.svg");
    }
    
    private static enum EventType{READ_MORE(true, false), 
                                                   GET_CHILD_GROUP(false,false), 
                                                   LAYOUT_ITEMS(true, true), 
                                                   RESIZE_SELECTION_COLUMN(true, true), 
                                                   COLUMN_RESIZED(true, true), 
                                                   SELECTION_CHANGED(true, true), 
                                                   REREAD(false, true);
        private final boolean ignoreWhenDisabled;
        private final boolean resendIfBlocked;
        
        private EventType(final boolean ignoreWhenDisabled, final boolean resendIfBlocked){
            this.ignoreWhenDisabled = ignoreWhenDisabled;
            this.resendIfBlocked = resendIfBlocked;
        }
        
        public boolean ignoreWhenDisabled(){
            return ignoreWhenDisabled;
        }
        
        public boolean resendIfBlocked(){
            return resendIfBlocked;
        }        
    };
    
    private static class SelectorTreeEvent extends QEvent{
        
        private final EventType eventType;
        
        public SelectorTreeEvent(final EventType eventType){
            super(QEvent.Type.User);
            this.eventType = eventType;
        }
        
        public EventType getSelectorTreeEventType(){
            return eventType;
        }
    }

    private final static class ReadMoreEvent extends SelectorTreeEvent {

        private QModelIndex parent;
        private boolean restored;
        public final boolean collapseOnFail;
        public final boolean checkForRowsLimit;
        public final String parentIndexPath;

        public ReadMoreEvent(final String parentPath, final boolean collapseOnFail, final boolean checkForRowsLimit) {
            super(EventType.READ_MORE);
            this.parentIndexPath = parentPath;
            this.collapseOnFail = collapseOnFail;
            this.checkForRowsLimit = checkForRowsLimit;
        }

        public ReadMoreEvent(final ReadMoreEvent source) {
            this(source.parentIndexPath, source.collapseOnFail, source.checkForRowsLimit);
        }

        public boolean isParentIndexValid(final SelectorModel model) {
            return getParentIndex(model) != null || parentIndexPath.equals(new ArrStr().toString());
        }

        public QModelIndex getParentIndex(final SelectorModel model) {
            if (parent == null && !restored) {
                restored = true;
                final ArrStr arr = ArrStr.fromValAsStr(parentIndexPath);
                final Stack<Pid> path = new Stack<>();
                for (String pidAsStr : arr) {
                    path.push(Pid.fromStr(pidAsStr));
                }
                int row;
                QModelIndex index = null;
                GroupModel group;
                while (!path.isEmpty()) {
                    if (model.rowCount(index) == 0) {//cant restore index                        
                        return null;
                    }
                    group = model.getChildGroup(index);
                    row = group.findEntityByPid(path.pop());
                    if (row < 0) {
                        return null;
                    } else {
                        index = model.index(row, 0, index);
                    }
                }
                parent = index;
            }
            return parent;
        }
    };

    private final static class GetChildGroupEvent extends SelectorTreeEvent {

        public final QModelIndex index;
        public final List<Integer> rows;

        public GetChildGroupEvent(final QModelIndex index, final List<Integer> rows) {
            super(EventType.GET_CHILD_GROUP);
            this.index = index;
            this.rows = rows;
        }
    };

    private final static class IndexInfo implements PostponedMousePressEvent.IndexInfo<SelectorTree>{
        
        private final ArrStr path;
        private final int column;
        
        public IndexInfo(final ArrStr path, final int column){
            this.path = path;
            this.column = column;
        }

        @Override
        public QModelIndex getIndex(final SelectorTree view) {
            try{
                final QModelIndex index = view.pathToIndex(path, false);
                return index==null ? null : view.controller.model.index(index.row(), column, index.parent());
            }catch(InterruptedException | ServiceClientException exception){
                return null;
            }
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }            
            final IndexInfo other = (IndexInfo) obj;
            if (this.column != other.column) {
                return false;
            }            
            if (!Objects.equals(this.path, other.path)) {
                return false;
            }
            return true;
        }
    }
    
    private final static class PointInfo{
        
        public static final PointInfo UNKNOWN_POINT = new PointInfo(null, null, null, false);
        
        private final ArrStr indexPath;
        private final Id propertyId;
        private final Object propertyValue;
        private final boolean insideCheckBox;
        
        public PointInfo(final ArrStr indexPath, final Id propertyId, final Object propertyValue, final boolean insideCheckBox){
            this.indexPath = indexPath;
            this.propertyId = propertyId;
            this.propertyValue = propertyValue;
            this.insideCheckBox = insideCheckBox;
        }
        
        public boolean insideCheckBox(){
            return insideCheckBox;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 61 * hash + Objects.hashCode(this.indexPath);
            hash = 61 * hash + Objects.hashCode(this.propertyId);
            hash = 61 * hash + Objects.hashCode(this.propertyValue);
            hash = 61 * hash + (this.insideCheckBox ? 1 : 0);
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
            if (!Objects.equals(this.indexPath, other.indexPath)) {
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
    
    private final static class PostponedRereadCall{
                
        private Collection<Pid> pids;
        private ArrStr parentPath;
        
        public PostponedRereadCall(final QModelIndex index,
                                                  final SelectorModel model,
                                                  final Collection<Pid> pids){
            if (index == null) {
                parentPath = null;
            }else{
                parentPath = new ArrStr();
                EntityModel entityModel;
                for (QModelIndex idx = index; idx != null; idx = idx.parent()) {
                    entityModel = model.getEntity(idx);
                    if (entityModel == null) {
                        parentPath.clear();
                    } else {
                        parentPath.add(0, entityModel.getPid().toStr());                        
                    }
                }
                if (parentPath.isEmpty()){
                    parentPath = null;
                }
            }
            this.pids = pids;
        }
        
        public boolean merge(final PostponedRereadCall other){
            if (parentPath==null || other.parentPath==null){
                parentPath = null;
                pids = other.pids;
                return true;
            }
            if (other.parentPath.equals(parentPath)){
                pids = other.pids;
                return true;
            }
            if (isChildPath(parentPath, other.parentPath)){
                pids = other.pids;
                return true;                
            }
            if (isChildPath(other.parentPath, parentPath)){
                parentPath = other.parentPath;
                pids = other.pids;
                return true;
            }
            return false;
        }    
        
        private static boolean isChildPath(final ArrStr parent, final ArrStr child){
            if (child.size()>parent.size()){
                for (int i=0,count=parent.size(); i<count; i++){
                    if (!parent.get(i).equals(child.get(i))){
                        return false;
                    }
                }
                return true;
            }else{
                return false;
            }
        }

        public QModelIndex getParentIndex(final SelectorModel model) {            
            int row;
            QModelIndex index = null;
            GroupModel group;
            for (String pidAsStr: parentPath){
                if (model.rowCount(index) == 0) {//cant restore index                        
                    return null;
                }
                group = model.getChildGroup(index);
                row = group.findEntityByPid(Pid.fromStr(pidAsStr));
                if (row<0){
                    return null;
                }else{
                    index = model.index(row, 0, index);
                }
            }
            return index;
        }

        public Collection<Pid> getPids() {
            return pids;
        }    
        
        public boolean rereadAll(){
            return parentPath==null && pids==null;
        }
    }
    
    private final class IndexIterator implements Iterator<QModelIndex>{
        
        private final QModelIndex endIndex;
        private QModelIndex currentIndex;
        
        public IndexIterator(final QModelIndex startIndex, final QModelIndex endIndex){
            this.endIndex = endIndex;
            currentIndex = startIndex;
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
                if (endIndex!=null && endIndex.internalId()==currentIndex.internalId()){
                    currentIndex = null;
                }else{
                    currentIndex = SelectorTree.this.indexBelow(currentIndex);
                }
                return nextIndex;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }        
    }

    public final class Actions {

        private boolean blockRefresh;
        public final ExplorerAction findAction;
        public final ExplorerAction findNextAction;
        public final ExplorerAction prevAction;
        public final ExplorerAction nextAction;
        public final ExplorerAction beginAction;
        public final ExplorerAction endAction;
        public final ExplorerAction createChildAction;
        public final ExplorerAction createSiblingAction;
        public final ExplorerAction pasteChildAction;
        public final ExplorerAction pasteSiblingAction;

        public Actions() {
            final MessageProvider mp = getEnvironment().getMessageProvider();

            findAction = createAction(Icons.FIND, mp.translate("Selector", "Find..."), null,"find");
            findAction.triggered.connect(SelectorTree.this.controller, "showFindDialog()");

            findNextAction = createAction(Icons.FIND_NEXT, mp.translate("Selector", "Find Next"), null,"find_next");
            findNextAction.triggered.connect(SelectorTree.this.controller, "findNext()");

            nextAction = createAction(Icons.NEXT, mp.translate("Selector", "Next"), "selectNextRow()","next");

            prevAction = createAction(Icons.PREVIOUS, mp.translate("Selector", "Previous"), "selectPrevRow()","previous");

            beginAction = createAction(Icons.BEGIN, mp.translate("Selector", "First"), "selectFirstRow()","first");

            endAction = createAction(Icons.END, mp.translate("Selector", "Last"), "selectLastRow()","last");

            createChildAction = createAction(Icons.CREATE_CHILD, mp.translate("Selector", "Create Child Object..."), "createChildEntities()","create_child");

            createSiblingAction = createAction(Icons.CREATE, mp.translate("Selector", "Create Object..."), null,"create_sibling");

            pasteChildAction = createAction(Icons.PASTE_CHILD, mp.translate("Selector", "Paste Child Object..."), "pasteChildEntity()","paste_child");

            pasteSiblingAction = createAction(Icons.PASTE, mp.translate("Selector", "Paste Object..."), null,"paste_sibling");
        }

        private ExplorerAction createAction(final ClientIcon icon, final String title, final String slot, final String objectName) {
            final ExplorerAction action =
                    new ExplorerAction(ExplorerIcon.getQIcon(icon), title, SelectorTree.this);
            WidgetUtils.updateActionToolTip(getEnvironment(), action);
            if (slot != null) {
                action.triggered.connect(SelectorTree.this, slot);
            }
            action.setObjectName(objectName);
            return action;
        }
        
        void blockRefresh(){
            blockRefresh = true;
        }
        
        void unblockRefresh(){
            blockRefresh = false;
        }

        public void refresh() {
            if (!blockRefresh) {
                blockRefresh = true;
                try {
                    final int rowCount = model().rowCount();
                    final QModelIndex current = currentIndex();
                    prevAction.setEnabled(current != null && indexAbove(current) != null);
                    beginAction.setEnabled(current != null && indexAbove(current) != null);
                    nextAction.setEnabled(current != null && indexBelow(current) != null);
                    endAction.setEnabled(current != null && indexBelow(current) != null);
                    findAction.setEnabled(rowCount > 0);
                    findNextAction.setEnabled(rowCount > 0);
                    {
                        final IClientEnvironment environment = getEnvironment();
                        final SelectorModel model = (SelectorModel) model();
                        final QModelIndex currentIndex = currentIndex();
                        GroupModel childGroup = null, group = null;
                        if (currentIndex != null && model != null) {
                            try {
                                group = model.getChildGroup(currentIndex.parent());
                                childGroup = model.getChildGroup(currentIndex);
                            } catch (Throwable exception) {
                                final EntityModel parentEntity = model.getEntity(currentIndex);
                                final String title =
                                        environment.getMessageProvider().translate("ExplorerException", "Error on creating child group model for parent object \"%s\":\n %s");
                                environment.getTracer().put(EEventSeverity.DEBUG,
                                        String.format(title, parentEntity.getTitle(),
                                        ClientException.exceptionStackToString(exception)),
                                        EEventSource.EXPLORER);
                            }
                        }
                        final boolean canCreateSibling =
                                group != null && SelectorTree.this.canCreateInGroup(group, model.getEntity(currentIndex.parent()), currentIndex.parent());
                        final boolean canCreateChild = SelectorTree.this.isEnabled() && childGroup != null
                                && SelectorTree.this.canCreateInGroup(childGroup, model.getEntity(currentIndex), currentIndex);
                        setupGroupActions(createSiblingAction,
                                createChildAction,
                                selector.getActions().getCreateAction(),
                                canCreateSibling,
                                canCreateChild,
                                getTitleOfCreateObjectActionImpl(group),
                                getTitleOfCreateChildObjectActionImpl(childGroup),
                                Icons.CREATE_WITH_POPUP,
                                Icons.CREATE,
                                btnCreateSibling);

                        final Clipboard clipboard = environment.getClipboard();
                        final boolean canPasteSibling =
                                canCreateSibling && clipboard.size() > 0 && clipboard.isCompatibleWith(group);
                        final boolean canPasteChild =
                                canCreateChild && clipboard.size() > 0 && clipboard.isCompatibleWith(childGroup);
                        setupGroupActions(pasteSiblingAction,
                                pasteChildAction,
                                selector.getActions().getPasteAction(),
                                canPasteSibling,
                                canPasteChild,
                                getTitleOfPasteObjectActionImpl(group, clipboard),
                                getTitleOfPasteChildObjectActionImpl(childGroup, clipboard),
                                Icons.PASTE_WITH_POPUP,
                                Icons.PASTE_SMALL,
                                btnPasteSibling);
                    }
                } finally {
                    blockRefresh = false;
                }
            }//if(!refreshing)
        }

        private void setupGroupActions(final QAction currentGroupAction,
                final QAction childGroupAction,
                final Action defaultSelectorAction,
                final boolean isCurrentGroupActionEnabled,
                final boolean isChildGroupActionEnabled,
                final String currentGroupActionTitle,
                final String childGroupActionTitle,
                final ClientIcon popupIcon,
                final ClientIcon defaultIcon,
                final QToolButton currentGroupActionBtn) {
            childGroupAction.setEnabled(isChildGroupActionEnabled);
            childGroupAction.setText(childGroupActionTitle);
            WidgetUtils.updateActionToolTip(getEnvironment(), childGroupAction);
            currentGroupAction.setVisible(isChildGroupActionEnabled);
            currentGroupAction.setEnabled(isCurrentGroupActionEnabled);
            currentGroupAction.setText(currentGroupActionTitle);
            defaultSelectorAction.setVisible(!isChildGroupActionEnabled);
            defaultSelectorAction.setText(currentGroupActionTitle);
            defaultSelectorAction.setToolTip(currentGroupActionTitle);
            if (currentGroupActionBtn != null && currentGroupActionBtn.nativeId() != 0 && isChildGroupActionEnabled) {
                currentGroupActionBtn.removeAction(currentGroupAction);
                currentGroupActionBtn.removeAction(childGroupAction);
                currentGroupActionBtn.setIcon(null);
                if (isCurrentGroupActionEnabled) {
                    currentGroupActionBtn.setDefaultAction(currentGroupAction);
                    currentGroupActionBtn.addAction(childGroupAction);
                    final QIcon icon = ExplorerIcon.getQIcon(popupIcon, defaultIcon, currentGroupActionBtn);
                    currentGroupActionBtn.setIcon(icon);
                    currentGroupActionBtn.setAutoRaise(true);
                } else {
                    currentGroupActionBtn.setDefaultAction(childGroupAction);
                }
                currentGroupActionBtn.setPopupMode(QToolButton.ToolButtonPopupMode.InstantPopup);
            }
        }

        private void close() {
            findAction.close();
            findNextAction.close();
            prevAction.close();
            nextAction.close();
            beginAction.close();
            endAction.close();
            createChildAction.close();
            createSiblingAction.close();
            pasteChildAction.close();
            pasteSiblingAction.close();
        }
    }
    
    private final class FilteredMouseEventListener extends QEventFilter{
        
        public FilteredMouseEventListener(final QObject parent){
            super(parent);
            setProcessableEventTypes(EnumSet.of(QEvent.Type.User));
        }
        
        @Override
        public boolean eventFilter(final QObject target, final QEvent event) {
            if (event instanceof FilteredMouseEvent) {
                SelectorTree.this.processFilteredMouseEvent((FilteredMouseEvent)event);
            }
            return false;
        }
    }
    
    private final class ExpandedItems implements Iterable<QModelIndex>{
        
        private final List<QModelIndex> items = new ArrayList<>();
        private final Map<Integer, List<QModelIndex>> expandedItemsByNestingLevel = new HashMap<>();
        private final Map<QModelIndex, List<QModelIndex>> parentsByItem = new HashMap<>();
        private int maxVisibleNestingLevel = 0;

        public void clear() {
            items.clear();
            expandedItemsByNestingLevel.clear();
            parentsByItem.clear();
            maxVisibleNestingLevel = 0;
        }

        public void remove(final QModelIndex index, final boolean cascade) {
            for (int i=items.size()-1; i>=0; i--){
                if ((cascade && WidgetUtils.isParentIndex(index, items.get(i)))
                    || SelectorTree.indexesEqual(index, items.get(i))){
                    remove(i);
                }
            }
        }
        
        public void remove(final int index) {
            final QModelIndex removingIndex = items.get(index);
            final List<QModelIndex> parents = parentsByItem.remove(removingIndex);
            final int nestingLevel = parents==null ? 0 : parents.size();
            final boolean removingAtLastNestingLevel = nestingLevel==expandedItemsByNestingLevel.size()-1;
            final List<QModelIndex> expandedItems = getExpandedItemsForNestingLevel(nestingLevel);
            expandedItems.remove(removingIndex);
            if (expandedItems.isEmpty() && removingAtLastNestingLevel){
                expandedItemsByNestingLevel.remove(nestingLevel);
            }
            items.remove(index);
            if ((nestingLevel+1)==maxVisibleNestingLevel || expandedItems.isEmpty()){
                final int previousMaxVisibleNestingLevel = maxVisibleNestingLevel;
                if (removingAtLastNestingLevel){
                    if (expandedItems.isEmpty()){
                        maxVisibleNestingLevel--;
                    }
                }else{
                    maxVisibleNestingLevel = calculateMaxVisibleNestingLevel(nestingLevel>0 ? nestingLevel-1 : 0);
                }
                if (previousMaxVisibleNestingLevel!=maxVisibleNestingLevel){
                    afterChangeMaxVisibleNestingLevel();
                }
            }
        }
        
        public void add(final QModelIndex index) {
            if (!contains(index)){
                final List<QModelIndex> parents = new LinkedList<>();                
                for (QModelIndex parent=index.parent(); parent!=null; parent=parent.parent()){                    
                    parents.add(parent);
                }
                final int nestingLevel = parents.size();
                if (nestingLevel>0){
                    parentsByItem.put(index, parents);
                }
                getExpandedItemsForNestingLevel(nestingLevel).add(index);
                items.add(index);
                final int previousMaxVisibleNestingLevel = maxVisibleNestingLevel;
                if (nestingLevel<expandedItemsByNestingLevel.size()){
                    maxVisibleNestingLevel = calculateMaxVisibleNestingLevel(nestingLevel > 0 ? nestingLevel-1 : 0);
                }else{
                    maxVisibleNestingLevel = Math.max(maxVisibleNestingLevel, nestingLevel);
                }
                if (previousMaxVisibleNestingLevel!=maxVisibleNestingLevel){
                    afterChangeMaxVisibleNestingLevel();
                }
            }
        }
        
        private int calculateMaxVisibleNestingLevel(final int toLevel){
            List<QModelIndex> expandedItems;
            List<QModelIndex> parents;
            for (int nestingLevel=expandedItemsByNestingLevel.size()-1; nestingLevel>=toLevel; nestingLevel--){
                expandedItems = getExpandedItemsForNestingLevel(nestingLevel);
                for (QModelIndex expandedItem: expandedItems){
                    parents = parentsByItem.get(expandedItem);
                    if (parents==null || items.containsAll(parents)){
                        return nestingLevel+1;
                    }
                }
            }
            return 0;
        }
        
        private List<QModelIndex> getExpandedItemsForNestingLevel(final int nestingLevel){
            List<QModelIndex> expandedItemsForNestingLevel = expandedItemsByNestingLevel.get(nestingLevel);
            if (expandedItemsForNestingLevel==null){
                expandedItemsForNestingLevel = new LinkedList<>();
                expandedItemsByNestingLevel.put(nestingLevel, expandedItemsForNestingLevel);
            }
            return expandedItemsForNestingLevel;
        }
        
        public boolean contains(final QModelIndex index){
            for (QModelIndex expandedItemIndex: items){
                if (SelectorTree.indexesEqual(index, expandedItemIndex)){
                    return true;
                }
            }
            return false;
        }
        
        public void set(final int index, final QModelIndex itemIndex){
            final QModelIndex replacedIndex = items.get(index);
            final List<QModelIndex> parents = parentsByItem.remove(replacedIndex);
            final int nestingLevel = parents==null ? 0 : parents.size();
            final List<QModelIndex> expandedItems = getExpandedItemsForNestingLevel(nestingLevel);
            final int indexInExpandedItems = expandedItems.indexOf(replacedIndex);
            if (indexInExpandedItems>-1){
                expandedItems.set(indexInExpandedItems, itemIndex);
            }
            if (parents!=null){
                parentsByItem.put(itemIndex, parents);
            }
            items.set(index, itemIndex);
        }
        
        public int size(){
            return items.size();
        }
        
        public QModelIndex get(final int index){
            return items.get(index);
        }

        @Override
        public Iterator<QModelIndex> iterator() {
            return items.iterator();
        }
        
        public int getMaxVisibleNestingLevel(){
            return maxVisibleNestingLevel;
        }
        
        private void afterChangeMaxVisibleNestingLevel(){
            SelectorTree.this.scheduleResizeSelectionColumn();
        }
    }     
    
    public final Actions actions;
    protected final Selector selector;
    protected final StandardSelectorWidgetController controller;
    private final SelectorModelDataLoader allDataLoader;
    private final SelectorModelDataLoader currentObjectFinder;
    private final SelectorModelDataLoader expandedObjectFinder;
    private final IProgressHandle rereadProgressHandle;
    private final String restoringExpandedItemsMessageTemplate;
    private final String restoringPositionMessageTemplate;
    private QToolButton btnCreateSibling;
    private QToolButton btnPasteSibling;
    private String createChildObjectCustomTitle;
    private String createObjectCustomTitle;
    private String pasteChildObjectCustomTitle;
    private String pasteObjectCustomTitle;
    private QModelIndex currentForFetch = null;
    private FilteredMouseEvent postponedMouseEvent;
    private final EnumSet<EventType> postedEvents = EnumSet.noneOf(EventType.class);
    private boolean blockResizeSelectionColumnEvent;
    private boolean blockCustomEvents;
    private boolean blockEditEvent;
    private boolean mousePressAtDecoration;
    private boolean updateGeometriesRecursionBlock;
    private boolean processColumnResizedRecursionBlock;    
    private final Set<Integer> columnsToUpdate = new HashSet<>();
    private final SelectorTreeVerticalHeader verticalHeader;
    private SelectorHorizontalHeader horizontalHeader;
    private final TableCornerButton cornerButton = new TableCornerButton(this);
    private final FilteredMouseEventListener filteredMouseEventListener = new FilteredMouseEventListener(this);
    private final List<String> scheduledRead = new ArrayList<>();
    private final QSize temporarySize = new QSize();
    private final ExpandedItems expandedItems = new ExpandedItems();
    private PostponedMousePressEvent<SelectorTree> postponedMousePressEvent;
    private long postedMousePressEventId;
    private List<PostponedRereadCall> postponedRereads;    
    private ExplorerTextOptions normalStyleTextOptions;
    private final QStyleOptionViewItem branchesBackgroundStyleOptions = new QStyleOptionViewItem();    
    private boolean multiSelectionRestricted;
    private final HierarchicalSelection.ISelectionListener<SelectorNode> selectionHandler = 
            new HierarchicalSelection.ISelectionListener<SelectorNode>(){
                @Override
                public void afterSelectionChanged(final HierarchicalSelection<SelectorNode> selection) {
                    SelectorTree.this.postSelectorTreeEvent(EventType.SELECTION_CHANGED);
                }
            };
    private final StandardSelectorWidgetController.IndexIteratorFactory indexIteratorFactory = 
            new StandardSelectorWidgetController.IndexIteratorFactory(){
                @Override
                public Iterator<QModelIndex> create(final QModelIndex startIndex, final QModelIndex endIndex) {
                    return new IndexIterator(startIndex, endIndex);
                }        
            };
    private final QRect temporaryRect = new QRect();
    private final QStyleOption temporaryStyleOption = new QStyleOption();
    private final static int DEFAULT_HEIGHT = 260;
    
    public SelectorTree(final Selector parentView, final SelectorModel model) {
        super(parentView);
        this.selector = parentView;
        final IClientEnvironment environment = selector.getEnvironment();
        controller = new StandardSelectorWidgetController(model, this, selector);
        createHorizontalHeader(model);

        final MessageProvider messageProvider = environment.getMessageProvider();
        final String confirmMovingToLastObjectMessage =
                messageProvider.translate("Selector", "Number of loaded objects is %1s.\nDo you want to load next %2s objects?");
        allDataLoader = new SelectorModelDataLoader(environment);
        
        allDataLoader.setConfirmationMessageText(confirmMovingToLastObjectMessage);
        allDataLoader.setProgressHeader(messageProvider.translate("Selector", "Moving to Last Object"));
        allDataLoader.setProgressTitleTemplate(messageProvider.translate("Selector", "Moving to Last Object...\nNumber of Loaded Objects: %1s"));
        allDataLoader.setDontAskButtonText(messageProvider.translate("Selector", "Load All Objects"));

        rereadProgressHandle = environment.getProgressHandleManager().newStandardProgressHandle();
        final String confirmRestoringPositionMessage =
                messageProvider.translate("Selector", "Number of loaded objects is %1s.\nDo you want to continue restoring position?");
        currentObjectFinder = new SelectorModelDataLoader(environment);
        
        currentObjectFinder.setConfirmationMessageText(confirmRestoringPositionMessage);
        restoringPositionMessageTemplate = messageProvider.translate("Selector", "Restoring Position...\nNumber of Loaded Objects: %1s");
        restoringExpandedItemsMessageTemplate = messageProvider.translate("Selector", "Restoring Expanded Items...\nNumber of Loaded Objects: %1s");
        currentObjectFinder.setDontAskButtonText(messageProvider.translate("Selector", "Load All Required Objects"));
        currentObjectFinder.setProgressHandle(rereadProgressHandle);
        currentObjectFinder.setStartProgress(false);

        expandedObjectFinder = new SelectorModelDataLoader(environment);
        expandedObjectFinder.setConfirmationMessageText(confirmRestoringPositionMessage);
        expandedObjectFinder.setProgressTitleTemplate(restoringExpandedItemsMessageTemplate);
        expandedObjectFinder.setLoadingLimit(-1);
        expandedObjectFinder.setProgressHandle(rereadProgressHandle);
        expandedObjectFinder.setStartProgress(false);

        actions = new Actions();          
        verticalHeader = new SelectorTreeVerticalHeader(this, model, cornerButton.getSymbolFontMetrics());
    }
    
    private SelectorHorizontalHeader createHorizontalHeader(final SelectorModel model){
        if (AdsVersion.belowRadixWareVersion("2.1.17.10.8")){
            horizontalHeader = new SelectorHorizontalHeader(this, model, true, false);
        }else{
            horizontalHeader = new SelectorHorizontalHeader(this, model, true);
        }
        setHeader(horizontalHeader);   
        horizontalHeader.sectionResized.disconnect();
        horizontalHeader.sectionResized.connect(this,"afterColumnResized(int, int, int)");
        return horizontalHeader;
    }

    @Override
    public void bind() {
        normalStyleTextOptions = 
            (ExplorerTextOptions)getEnvironment().getTextOptionsProvider().getOptions(EnumSet.of(ETextOptionsMarker.SELECTOR_ROW), ESelectorRowStyle.NORMAL);        
        
        int rowCount = controller.model.rowCount(null);

        if (rowCount == 0 && controller.model.canReadMore(null)) {
            try {
                controller.model.readMore(null);
            } catch (InterruptedException exception) {
            } catch (ServiceClientException exception) {
                controller.processErrorOnReceivingData(exception);
            }
            rowCount = controller.model.rowCount(null);
        }

        for (int i = 0; i < rowCount; i++) {
            final QModelIndex currentIndex = controller.model.index(i, 0);
            try {
                controller.model.hasChildren(currentIndex);
            } catch (Throwable ex) {
                final EntityModel parentEntity = controller.model.getEntity(currentIndex);
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on creating child group model for parent object \"%s\":\n %s");
                getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                        String.format(title, parentEntity.getTitle(),
                        ClientException.exceptionStackToString(ex)),
                        EEventSource.EXPLORER);
            }
        }
        getSelection().addListener(selectionHandler);
        controller.setSelectionModeListener(new StandardSelectorWidgetController.ISelectionModeListener() {
            @Override
            public void afterSwitchSelectionMode(boolean isSelectionEnabled) {
                SelectorTree.this.afterSwitchSelectionMode(isSelectionEnabled);
            }
        });
        setModel(controller.model);       

        final List<SelectorColumnModelItem> columns = controller.model.getSelectorColumns();
        horizontalHeader.restoreSettings();
        if (horizontalHeader.count() != columns.size()) {            
            createHorizontalHeader(controller.model);                        
        }
                
        horizontalHeader.checkBoxClicked.connect(this,"onHHeaderCheckBoxClick()");
        controller.refreshCornerWidget(cornerButton, isSelectionEnabled(), isSelectionAllowed());
        controller.updateHeaderCheckState(horizontalHeader, !isMultiSelectionRestriced());
        
        controller.setupSelectionColumnDelegate();
        horizontalHeader.bind();
        if (controller.model.isSelectionEnabled()){
            setTreePosition(1);
        }
        //set current row after horizontal header was binded
        if (selector.isDisabled()) {
            clear();
        } else {
            selectFirstRow();
        }        
        updateColumnSpan(null, 0, getRootGroupModel().getEntitiesCount() - 1);
        doubleClicked.connect(controller, "processDoubleClick(QModelIndex)");
        collapsed.connect(this, "onCollapsed(QModelIndex)");
        expanded.connect(this, "onExpanded(QModelIndex)");
        Application.getInstance().getActions().settingsChanged.connect(this, "applySettings()");
        horizontalHeader.sectionResized.connect(this, "finishEdit()");
        horizontalHeader.sectionMoved.connect(this, "onSectionMoved(int, int, int)");
        horizontalHeader.sectionClicked.connect(this, "onHHeaderClicked(int)");
        horizontalHeader.sectionVisibilityChanged.connect(this, "onSectionVisibilityChanged(int , boolean)");
        horizontalHeader.resizeColumnByContent.connect(this,"onResizeColumnByContent(int)");        
        final QWidget viewport = viewport();
        if (viewport != null) {//RADIX-7253
            viewport.installEventFilter(filteredMouseEventListener);
        }
        
        verticalHeader.doubleClicked.connect(this,"onVHeaderDoubleClicked(QModelIndex)");        
        cornerButton.clicked.connect(this,"onCornerButtonClick()");
        
        horizontalScrollBar().setValue(0);
        if (!selector.isDisabled()){
            updateSelectionMode();            
        }
    }
    
    @SuppressWarnings("unused")
    private void onCornerButtonClick(){
        controller.switchSelectionMode(horizontalHeader);
    }
    
    @SuppressWarnings("unused")
    private void onHHeaderCheckBoxClick(){
        controller.invertSelection(horizontalHeader, getSelectAllMode());
    }    

    @SuppressWarnings("unused")
    private void onHHeaderClicked(final int section) {
        controller.processColumnHeaderClicked(section);
    }
    
    @SuppressWarnings("unused")
    private void onVHeaderDoubleClicked(final QModelIndex index){
        controller.processRowHeaderDoubleClicked(index);
    }    
        
    private boolean isSelectionEnabled(){
        return controller.isSelectionEnabled(horizontalHeader);
    }
    
    private void updateSelectionMode(){
        controller.updateSelectionMode(horizontalHeader, isSelectionAllowed());
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
    
    private void afterSwitchSelectionMode(final boolean isSelectionEnabled){
        setTreePosition(isSelectionEnabled ? 0 : 1);
        update();
        if (isSelectionEnabled){
            resizeColumnToContents(0);
        }
        controller.refreshCornerWidget(cornerButton, isSelectionEnabled, isSelectionAllowed());
    }
        
    private boolean isSelectionAllowed(){
        return  IS_MULTIPLE_SELECTION_ALLOWED
                   && controller.model.isSelectionEnabled()
                   && !isMultiSelectionRestriced() 
                   && isEnabled()                
                   && !getRootGroupModel().isEmpty();
    }
    
    private EHierarchicalSelectionMode getSelectAllMode(){
        if (isMultiSelectionRestriced()){
            return null;
        }else{
            final SelectorNode rootNode = controller.model.getSelectorNode(null);
            final EnumSet<EHierarchicalSelectionMode> selectionModes = 
                controller.model.calcAllSelectionModes(rootNode);
            if (selectionModes.contains(EHierarchicalSelectionMode.ALL_NESTED_OBJECTS_CASCADE)){
                return EHierarchicalSelectionMode.ALL_NESTED_OBJECTS_CASCADE;
            }
            if (selectionModes.contains(EHierarchicalSelectionMode.EXPLICIT_NESTED_OBJECTS)){
                return EHierarchicalSelectionMode.EXPLICIT_NESTED_OBJECTS;
            }
            return null;
        }
    }

    @Override
    public final boolean setMultiSelectionEnabled(final boolean enabled) {
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

    public final void setMultiSelectionRestricted(final boolean isRestricted){
        if (multiSelectionRestricted!=isRestricted){
            multiSelectionRestricted = isRestricted;  
            controller.refreshCornerWidget(cornerButton, isSelectionEnabled(), isSelectionAllowed());
        }
    }
    
    public final boolean isMultiSelectionRestriced(){
        return multiSelectionRestricted;
    }
    
    @Override
    public void lockInput() {
        controller.lockInput();
        blockCustomEvents = true;
    }

    @Override
    public void unlockInput() {
        controller.unlockInput();
        blockCustomEvents = false;
    }

    @Override
    protected void paintEvent(final QPaintEvent event) {
        if (!controller.isLocked() || ExplorerMessageBox.isSomeMessageBoxActive()) {
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
            super.paintEvent(event);
        }
    }

    @Override
    protected void timerEvent(final QTimerEvent event) {
        if (controller.isLocked()) {
            final int timerId = event.timerId();
            killTimer(timerId);
            controller.postEventAfterUnlock(new QTimerEvent(timerId));
        } else if  (postponedMousePressEvent!=null && event.timerId()==postponedMousePressEvent.getClickTimerId()){
            killTimer(event.timerId());
            postedMousePressEventId = postponedMousePressEvent.post(this);
            postponedMousePressEvent = null;
        } else {
            super.timerEvent(event);
        }
    }

    protected final IClientEnvironment getEnvironment() {
        return selector.getEnvironment();
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof FilteredMouseEvent) {
            event.accept();
            processFilteredMouseEvent(new FilteredMouseEvent((FilteredMouseEvent)event, viewport()));
        }else if (event instanceof PostponedMousePressEvent){
            event.accept();
            processPostponedMouseClickEvent((PostponedMousePressEvent)event);
        }else if (!isEnabled() || model()==null){//ignore event
            event.accept();
            if (event instanceof ReadMoreEvent) {
                final ReadMoreEvent readEvent = (ReadMoreEvent) event;
                scheduledRead.remove(readEvent.parentIndexPath);
            } else if (event instanceof SelectorTreeEvent) {
                final EventType eventType = ((SelectorTreeEvent)event).getSelectorTreeEventType();
                if (eventType.ignoreWhenDisabled()){
                    postedEvents.remove(eventType);
                }
            }
        } else if (blockCustomEvents || controller.isLocked()) {//resend event
            event.accept();
            if (event instanceof ReadMoreEvent) {
                final ReadMoreEvent readEvent = (ReadMoreEvent) event;
                if (blockCustomEvents){
                    scheduledRead.remove(readEvent.parentIndexPath);
                }else{
                    Application.processEventWhenEasSessionReady(this, new ReadMoreEvent(readEvent));
                }
            } else if (event instanceof SelectorTreeEvent) {
                final EventType eventType = ((SelectorTreeEvent)event).getSelectorTreeEventType();
                if (eventType.resendIfBlocked()){
                    Application.processEventWhenEasSessionReady(this, new SelectorTreeEvent(eventType));
                }
            }
        }else if (event instanceof ReadMoreEvent) {
            event.accept();
            processReadMoreEvent((ReadMoreEvent) event);
        } else if (event instanceof GetChildGroupEvent) {
            event.accept();
            processGetChildGroupEvent((GetChildGroupEvent) event);
        } else if (event instanceof SelectorTreeEvent) {
            event.accept();
            final EventType eventType = ((SelectorTreeEvent)event).getSelectorTreeEventType();
            switch (eventType){
                case LAYOUT_ITEMS:
                    processItemsLayoutEvent();
                    break;
                case RESIZE_SELECTION_COLUMN:
                    processResizeSelectionColumnEvent();
                    break;
                case COLUMN_RESIZED:{
                    if (postedEvents.contains(EventType.COLUMN_RESIZED)){
                        try{
                            processColumnResizedEvent();
                        }finally{
                            postedEvents.remove(EventType.COLUMN_RESIZED);
                        }
                    }
                    break;
                }
                case SELECTION_CHANGED:{                    
                    processSelectionChangedEvent();
                    break;
                }
                case REREAD:{
                    executePostponedRereads(true);
                    break;
                }
            }
        }
    }
        
    private void processReadMoreEvent(final ReadMoreEvent  readEvent){
        try {
            blockCustomEvents = true;
            if (readEvent.isParentIndexValid(controller.model)) {
                final QModelIndex parentIndex = readEvent.getParentIndex(controller.model);
                final int prevRowCount = controller.model.rowCount(parentIndex);
                if (parentIndex == null && readEvent.checkForRowsLimit
                        && controller.model.getRowsLimit() <= prevRowCount) {
                    final String message =
                            getEnvironment().getMessageProvider().translate("Selector", "Number of loaded objects is %1s.\nYou may want to use filter to find specific object");
                    getEnvironment().messageInformation(null, String.format(message, controller.model.getTotalRowsCount()));
                    controller.model.increaseRowsLimit();
                }
                final boolean failOnRead = !controller.model.readMore(parentIndex);
                if (readEvent.collapseOnFail) {
                    if (failOnRead && controller.model.rowCount(parentIndex) == 0) {
                        setExpanded(parentIndex, false);
                        dataChanged(parentIndex, parentIndex);
                    } else {
                        final int newRowCount = controller.model.rowCount(parentIndex);
                        for (int i = prevRowCount; i < newRowCount; i++) {
                            controller.model.hasChildren(controller.model.index(i, 0, parentIndex));
                        }
                    }
                }
                if (isExpanded(parentIndex) && !expandedItems.contains(parentIndex)){
                    verticalHeader.expand(parentIndex);
                    expandedItems.add(parentIndex);
                }
                actions.refresh();//moveNext
            }
        } catch (InterruptedException exception) {
        } catch (ServiceClientException exception) {
            controller.processErrorOnReceivingData(exception);
        } catch (Exception exception) {
            controller.processErrorOnReceivingData(exception);
        } finally {
            scheduledRead.remove(readEvent.parentIndexPath);
            blockCustomEvents = false;
        }        
    }
    
    private void processGetChildGroupEvent(final GetChildGroupEvent getChildEvent){
        final SelectorModel model = controller.model;
        final List<QModelIndex> indexes = new ArrayList<>();        
        {
            QModelIndex index;
            for (int row: getChildEvent.rows) {
                index = model.index(row, 0, getChildEvent.index);
                if (model.getCachedChildGroup(index)==null
                    && (model.hasChildren(index) || model.canCreateChild(index))){
                    indexes.add(index);
                }
            }
        }
        if (!indexes.isEmpty()){
            lockInput();
            try {
                for (QModelIndex index: indexes) {
                    model.getChildGroup(index);
                }
            } catch (Throwable exception) {
                if (ClientException.isSystemFault(exception)) {
                    getEnvironment().processException(exception);
                } else {
                    final String error = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), exception) + "\n" + ClientException.exceptionStackToString(exception);
                    getEnvironment().getTracer().debug(error);
                }
            } finally {
                unlockInput();
            }
        }
    }
    
    private void processItemsLayoutEvent(){
        if (postedEvents.contains(EventType.LAYOUT_ITEMS)){
            postedEvents.remove(EventType.LAYOUT_ITEMS);
            if (controller.model.isLocked()) {
                postSelectorTreeEvent(EventType.LAYOUT_ITEMS);
            } else {
                super.doItemsLayout();
            }
        }
    }
    
    private void processResizeSelectionColumnEvent(){
        if (postedEvents.contains(EventType.RESIZE_SELECTION_COLUMN)){
            postedEvents.remove(EventType.RESIZE_SELECTION_COLUMN);
            if (controller.model.isLocked() || blockResizeSelectionColumnEvent) {
                postSelectorTreeEvent(EventType.RESIZE_SELECTION_COLUMN);
            } else {
                resizeColumnToContents(0);
            }            
        }
    }
    
    private void processSelectionChangedEvent(){
        if (postedEvents.contains(EventType.SELECTION_CHANGED)){
            postedEvents.remove(EventType.SELECTION_CHANGED);
            controller.updateHeaderCheckState(horizontalHeader, !isMultiSelectionRestriced());
            if (isSelectionAllowed()){
                updateSelectionMode();
                final int rowCount = model().rowCount();
                final int columnCount = model().columnCount();
                if (rowCount>0){
                    dataChanged(model().index(0, 0), model().index(rowCount-1, columnCount-1));
                }
            }
            actions.refresh();
        }
    }    
    
    private void executePostponedRereads(final boolean restoreCurrentIndex){
        if (controller.changingCurrentEntity()){
            Application.processEventWhenEasSessionReady(this, new SelectorTreeEvent(EventType.REREAD));
        }
        if (postponedRereads!=null && !postponedRereads.isEmpty()){
            final List<PostponedRereadCall> copyPostponedRereds = new LinkedList<>(postponedRereads);
            
            final ArrStr currentPath;
            final int currentColumn;
            if (restoreCurrentIndex){
                final QModelIndex currentIndex = currentIndex();
                currentPath = indexToPath(currentIndex);
                currentColumn = currentIndex==null ? horizontalHeader.getFirstVisibleColumnIndex() : currentIndex.column();
            }else{
                currentPath = null;
                currentColumn = -1;
            }
            postponedRereads = null;
            try{
                QModelIndex parentIndex;
                PostponedRereadCall rereadCall;
                for (int i=0,count=copyPostponedRereds.size(); i<count; i++){
                    rereadCall = copyPostponedRereds.get(i);
                    parentIndex = rereadCall.getParentIndex(controller.model);
                    if (parentIndex==null){
                        if (rereadCall.rereadAll()){
                            reread();
                        }
                    }else{
                        if (i==count-1){
                            rereadImpl(parentIndex, restoreCurrentIndex ? null : copyPostponedRereds.get(i).getPids());
                        }else{
                            rereadImpl(parentIndex, null);
                        }                        
                    }
                }
            }catch(InterruptedException exception){
                return;
            }catch(ServiceClientException exception){
                if (ClientException.isSystemFault(exception)) {
                    getEnvironment().processException(exception);
                } else {
                    final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on receiving data");
                    getEnvironment().getTracer().error(title, exception);
                }
            }finally{
                if (currentPath!=null && !currentPath.isEmpty()){
                    try{
                        final QModelIndex index = pathToIndex(currentPath, true);
                        final QModelIndex actualIndex = index==null ? null : model().index(index.row(), currentColumn, index.parent());
                        if (actualIndex!=null){
                            controller.enterEntity(actualIndex);
                            scrollTo(actualIndex);
                        }
                    }catch(InterruptedException exception){
                        return;
                    }catch(ServiceClientException exception){
                        if (ClientException.isSystemFault(exception)) {
                            getEnvironment().processException(exception);
                        } else {
                            final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on receiving data");
                            getEnvironment().getTracer().error(title, exception);
                        }
                    }
                    finally{
                        controller.model.increaseRowsLimit();
                    }
                }
            }
        }
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
    
    private void scheduleResizeSelectionColumn(){
        if (isSelectionEnabled()){
            postSelectorTreeEvent(EventType.RESIZE_SELECTION_COLUMN);
        }
    }    
    
    private boolean postSelectorTreeEvent(final EventType eventType){
        if (postedEvents.contains(eventType)){
            return false;
        }else{
            postedEvents.add(eventType);
            Application.processEventWhenEasSessionReady(this, new SelectorTreeEvent(eventType));
            return true;
        }
    }    

    @Override
    public void refresh(final ModelItem item) {        
        if (model() != null) {
            if (item instanceof Property) {
                final Property property = (Property) item;
                controller.refresh(property);
                final QModelIndex index = controller.findIndexForProperty(property);
                if (index != null) {
                    if (!horizontalHeader.isSectionHidden(index.column())
                            && horizontalHeader.resizeMode(index.column()) == QHeaderView.ResizeMode.ResizeToContents) {
                        resizeColumnToContents(index.column());
                    }
                }
            }
            actions.refresh();
        }
    }
    
    @SuppressWarnings("unused")
    private void onSectionVisibilityChanged(int section, boolean isVisible){
        horizontalHeader.updateFirstVisibleColumnIndex();
    }
    
    @SuppressWarnings("unused")
    private void onResizeColumnByContent(int sectionIndex){        
        final int width = Math.max(sizeHintForColumn(sectionIndex), horizontalHeader.sectionSizeFromContents(sectionIndex).width());
        if (width>0){
            horizontalHeader.resizeSection(sectionIndex, width);
            final SelectorColumnModelItem column = controller.model.getSelectorColumn(sectionIndex);
            if (column!=null && column.getColumnDef().getSizePolicy()==ESelectorColumnSizePolicy.RESIZE_BY_CONTENT){
                column.setSizePolicy(ESelectorColumnSizePolicy.RESIZE_BY_CONTENT);
            }                  
        }        
    }    
    
    private void updateSpan() {
        updateColumnSpan(null, 0, getRootGroupModel().getEntitiesCount() - 1);
    }

    protected final void updateColumnSpan(final QModelIndex parentIndex, final int startRow, final int endRow) {
        for (int row = startRow; row <= endRow; row++) {
            if (controller.model.isBrokenEntity(controller.model.index(row, 0, parentIndex))) {
                if (!isFirstColumnSpanned(row, parentIndex)) {
                    setFirstColumnSpanned(row, parentIndex, true);
                }
            } else if (isFirstColumnSpanned(row, parentIndex)) {
                setFirstColumnSpanned(row, parentIndex, false);
            }
        }
    }

    @SuppressWarnings("unused")
    private void onSectionMoved(final int logicalIndex, final int oldVisualIndex, final int newVisualIndex) {
        finishEdit();
        if (oldVisualIndex == 0 || newVisualIndex == 0) {
            header().blockSignals(true);
            try {
                header().moveSection(newVisualIndex, oldVisualIndex);
            } finally {
                header().blockSignals(false);
            }
        }
    }
    private Action.ActionListener createRedirector = new Action.ActionListener() {
        @Override
        public void triggered(Action action) {
            selector.getActions().getCreateAction().trigger();
        }
    };
    private Action.ActionListener pasteRedirector = new Action.ActionListener() {
        @Override
        public void triggered(Action action) {
            selector.getActions().getPasteAction().trigger();
        }
    };

    @Override
    public void setupSelectorToolBar(final IToolBar toolBar) {
        {
            final Action createAction = selector.getActions().getCreateAction();
            toolBar.insertAction(createAction, actions.beginAction);
            toolBar.insertAction(createAction, actions.prevAction);
            toolBar.insertAction(createAction, actions.nextAction);
            toolBar.insertAction(createAction, actions.endAction);
            toolBar.insertAction(createAction, actions.findAction);
            toolBar.insertAction(createAction, actions.createSiblingAction);

            actions.createSiblingAction.addActionListener(createRedirector);

            btnCreateSibling = (QToolButton) ((QToolBar) toolBar).widgetForAction(actions.createSiblingAction);
            btnCreateSibling.addAction(actions.createChildAction);
        }
        {
            final Action pasteAction = selector.getActions().getPasteAction();
            toolBar.insertAction(pasteAction, actions.pasteSiblingAction);
            actions.pasteSiblingAction.addActionListener(pasteRedirector);
            btnPasteSibling = (QToolButton) ((QToolBar) toolBar).widgetForAction(actions.pasteSiblingAction);
            btnPasteSibling.addAction(actions.pasteChildAction);
        }
        actions.refresh();
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
        if (actions.createSiblingAction.isEnabled()) {
            menu.insertAction(createAction, actions.createSiblingAction);
            menu.insertAction(createAction, actions.createChildAction);
            final Action pasteAction = selector.getActions().getPasteAction();
            menu.insertAction(pasteAction, actions.pasteSiblingAction);
            menu.insertAction(pasteAction, actions.pasteChildAction);
        }
    }

    public void selectNextRow() {
        if (currentIndex() != null) {
            final QModelIndex target = indexBelow(currentIndex());
            if (target!=null){
                controller.changeCurrentEntity(target, false, false);
            }
        }
    }

    public void selectPrevRow() {
        if (currentIndex() != null) {
            final QModelIndex target = indexAbove(currentIndex());
            if (target!=null){
                controller.changeCurrentEntity(target, false, false);
            }
        }
    }

    public void selectFirstRow() {
        if (model().rowCount() > 0) {
            final int column = currentIndex() != null ? currentIndex().column() : horizontalHeader.getFirstVisibleColumnIndex();
            controller.changeCurrentEntity(model().index(0, column), false, false);
        }
    }

    public void selectLastRow() {
        if (!controller.isLocked() && model().rowCount() > 0) {
            final SelectorModel model = controller.model;
            final int rowsLoadingLimit =
                    selector.getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH, 1000);
            allDataLoader.setLoadingLimit(rowsLoadingLimit);
            allDataLoader.resetCounters();
            int loadedRows = 0;
            final int column = currentIndex() != null ? currentIndex().column() : horizontalHeader.getFirstVisibleColumnIndex();
            QModelIndex target = null;
            int row;
            controller.lockInput();
            try {
                do {
                    try {
                        loadedRows += allDataLoader.loadAll(new SelectorWidgetDelegate(model, target));
                    } catch (InterruptedException exception) {
                        loadedRows += model.rowCount(target);
                        target = null;
                        break;
                    } catch (ServiceClientException exception) {
                        model.showErrorOnReceivingData(exception);
                        target = null;
                        break;
                    }

                    row = model().rowCount(target) - 1;
                    target = model().index(row, 0, target);
                    //Вычитывание раскрытых элементов выше последнего
                    for (QModelIndex expandedItem : expandedItems) {
                        if (StandardSelectorWidgetController.compareIndexes(expandedItem, target) < 0) {
                            try {
                                loadedRows += allDataLoader.loadAll(new SelectorWidgetDelegate(model, expandedItem));
                            } catch (InterruptedException exception) {
                                loadedRows += model.rowCount(target);
                                target = null;
                                break;
                            } catch (ServiceClientException exception) {
                                model.showErrorOnReceivingData(exception);
                                target = null;
                                break;
                            }
                        }
                    }
                } while (target != null && isExpanded(target) && model().rowCount(target) > 0);
            } finally {
                controller.unlockInput();
            }
            try {
                if (target != null ) {
                    if (column == 0) {
                        controller.changeCurrentEntity(target, false, false);
                    } else {
                        controller.changeCurrentEntity(model().index(target.row(), column, target.parent()), false, false);
                    }
                }
            } finally {
                if (loadedRows > 0) {
                    controller.model.increaseRowsLimit();
                }
            }
        }
    }
    
    private ModelRestrictions getRestrictions(final GroupModel group) {
        final GroupRestrictions restrictions = new GroupRestrictions(group);
        if (!group.settingsWasRead()){//group was never read
            //read to receive actual restrictions
            selector.blockRedraw();
            try {
                group.readCommonSettings();
            } catch (InterruptedException exception) {
                restrictions.add(Restrictions.CONTEXTLESS_SELECT);
            } catch (ServiceClientException exception) {
                final String title = selector.getModel().getEnvironment().getMessageProvider().translate("ExplorerException", "Error on receiving data");
                selector.getModel().getEnvironment().getTracer().error(title, exception);
                restrictions.add(Restrictions.CONTEXTLESS_SELECT);
            }finally{
                selector.unblockRedraw();
            }
        }
        restrictions.add(group.getRestrictions());
        return restrictions;
    }    

    public EntityModel createChildEntity() {
        final SelectorModel model = controller.model;
        final QModelIndex current = currentIndex();
        if (current != null) {
            try {
                final GroupModel group = model.getChildGroup(current);
                if (group != null && selector.canChangeCurrentEntity(false)) {
                    return doCreateChildEntity(group);
                }
            } catch (InterruptedException e) {
            } catch (Exception ex) {
                selector.getModel().showException(getEnvironment().getMessageProvider().translate("Selector", "Failed to create object"), ex);
            }
        }
        return null;
    }
    
    private EntityModel doCreateChildEntity(final GroupModel group) throws ServiceClientException, InterruptedException{
        final EntityModel result = SelectorController.createEntity(group, null, this);
        if (result != null) {
            if (group.getSelectorPresentationDef().isRestoringPositionEnabled()) {
                reread(currentIndex(), result.getPid());
            } else {
                reread(currentIndex(), (Pid)null);
            }
            selector.notifyEntityObjectsCreated(Collections.singletonList(result));
        }
        return result;        
    }
    
    public List<EntityModel> createChildEntities() {
        final SelectorModel model = controller.model;
        final QModelIndex current = currentIndex();
        if (current != null) {
            try {
                final GroupModel group = model.getChildGroup(current);
                if (group != null && selector.canChangeCurrentEntity(false)) {                    
                    if (group.getRestrictions().getIsMultipleCreateRestricted()){
                        final EntityModel newEntity = doCreateChildEntity(group);
                        return newEntity==null ? Collections.<EntityModel>emptyList() : Collections.singletonList(newEntity);
                    }else{
                        final List<EntityModel> newEntities = SelectorController.createEntities(group, this);
                        if (newEntities!=null && !newEntities.isEmpty()){
                            if (group.getSelectorPresentationDef().isRestoringPositionEnabled()) {
                                final List<Pid> pids = new LinkedList<>();
                                for (EntityModel newEntity: newEntities){
                                    if (newEntity.getPid()!=null){
                                        pids.add(newEntity.getPid());
                                    }
                                }
                                reread(currentIndex(), pids);
                            } else {
                                reread(currentIndex(), (Pid)null);
                            }
                            selector.notifyEntityObjectsCreated(newEntities);
                            return newEntities;
                        }
                    }                    
                }
            } catch (InterruptedException e) {
            } catch (Exception ex) {
                selector.getModel().showException(getEnvironment().getMessageProvider().translate("Selector", "Failed to create object"), ex);
            }
        }
        return Collections.emptyList();
    }    

    public void pasteChildEntity() {
        final SelectorModel model = controller.model;
        final QModelIndex current = currentIndex();
        if (current != null) {
            try {
                final GroupModel groupModel = model.getChildGroup(current);
                if (groupModel != null && selector.canChangeCurrentEntity(false)) {
                    final List<EntityModel> result = SelectorController.paste(groupModel, this);
                    if (result.size() == 1
                            && groupModel.getSelectorPresentationDef().isRestoringPositionEnabled()) {
                        final Pid pid = result.get(0).getPid();
                        reread(currentIndex(), pid);
                    } else if (!result.isEmpty()) {
                        reread(currentIndex(), (Pid)null);
                    }
                    if (!result.isEmpty()) {
                        selector.notifyEntityObjectsCreated(result);
                    }
                }
            } catch (InterruptedException e) {
            } catch (Exception ex) {
                selector.getModel().showException(getEnvironment().getMessageProvider().translate("Selector", "Failed to create object"), ex);
            }
        }
    }

    @Override
    public void entityRemoved(final Pid pid) {
        final QModelIndex index = controller.findEntityIndex(pid);
        //Строчка уже была удалена (например, в результате перечитывания из afterDelete)
        if (index != null) {
            final QModelIndex parentIndex = index.parent();
            final int removedRow = index.row();
            //Обновление индекса элемента для текущей закачки
            if (indexesEqual(index, currentForFetch)) {
                currentForFetch = null;
            } else if (currentForFetch != null
                    && indexesEqual(currentForFetch.parent(), parentIndex)
                    && currentForFetch.row() > removedRow) {
                currentForFetch =
                        model().index(currentForFetch.row() - 1, currentForFetch.column(), parentIndex);
            }         
            actions.blockRefresh();
            try{//Удаление строки
                controller.processEntityRemoved(pid);
            }finally{
                actions.unblockRefresh();
            }
            actions.refresh();
            controller.lockInput();
            try {                
                updateColumnSpan(parentIndex, removedRow, controller.model.rowCount(parentIndex) - 1);
            } finally {
                controller.unlockInput();
            }
            updateSelectionMode();
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
        if (style().styleHint(QStyle.StyleHint.SH_Q3ListViewExpand_SelectMouseType, null, this) == QEvent.Type.MouseButtonPress.value()){
            final QPoint pos = event.pos();
            final QModelIndex index = decorationIndexAt(pos);
            if (index!=null ){
                mousePressAtDecoration = true;
                expandOrCollapse(index, pos);
                event.ignore();
                return;
            }
        }
        if (!processMousePressEvent(event)){
            processMouseClickEvent(event);
        }
    }

    @Override
    protected void mouseReleaseEvent(final QMouseEvent event) {        
        if (mousePressAtDecoration){
            mousePressAtDecoration = false;
            return;
        }
        if (style().styleHint(QStyle.StyleHint.SH_Q3ListViewExpand_SelectMouseType, null, this) == QEvent.Type.MouseButtonRelease.value()){
            final QPoint pos = event.pos();
            final QModelIndex index = decorationIndexAt(pos);
            if (index!=null){
                expandOrCollapse(index, pos);
                event.ignore();
                return;
            }
        }
        super.mouseReleaseEvent(event);
    }
            
    private boolean processMousePressEvent(final QMouseEvent event){
        if (postedMousePressEventId!=0){
            Application.removeScheduledEvent(postedMousePressEventId);
            postedMousePressEventId = 0;            
        }
        final QModelIndex clickIndex = indexAt(event.pos());
        if (!visualRect(clickIndex).contains(event.x(), event.y())){
            return false;
        }
        final GroupModel groupModel = clickIndex==null ? null : controller.model.getChildGroup(clickIndex.parent());
        final GroupRestrictions restrictions = groupModel==null ? null : groupModel.getRestrictions();
        final boolean canOpenModalEditor = 
            restrictions!=null && !restrictions.getIsRunEditorRestricted() && restrictions.getIsEditorRestricted();
        if (canOpenModalEditor && isMouseClickWithCtrl(event)){            
            final EntityModel clickModel = controller.model.getEntity(clickIndex);
            final boolean isBrokenModel = clickModel instanceof BrokenEntityModel;
            final boolean canOpenEntityView = 
                clickModel!=null && (controller.model.isBrokenEntity(clickIndex) || clickModel.canOpenEntityView());
            if (canOpenEntityView
                && (selector.getCurrentEntity()!=clickModel || selector.getActions().getRunEditorDialogAction().isEnabled())){
                final Pid clickPid = clickModel.getPid();
                final IndexInfo clickIndexInfo = new IndexInfo(indexToPath(clickIndex), clickIndex.column());
                if (postponedMousePressEvent==null){
                    postponeMouseClick(event, clickIndexInfo, clickPid);
                }else{
                    killTimer(postponedMousePressEvent.getClickTimerId());                    
                    if (postponedMousePressEvent.sameIndex(clickIndexInfo)
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
                        postponeMouseClick(event, clickIndexInfo, clickPid);
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
    
    private void postponeMouseClick(final QMouseEvent clickEvent, final IndexInfo clickIndexInfo, final Pid clickModelPid){
        clickEvent.accept();
        final int mouseClickTimerId = startTimer(QApplication.doubleClickInterval());
        postponedMousePressEvent = new PostponedMousePressEvent<>(clickEvent, clickIndexInfo, clickModelPid, mouseClickTimerId);
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
        final QModelIndex index = indexAt(event.pos());
        final PointInfo mousePressPoint = getPointInfo(event.pos(),index);
        if (index!=null){
            final EntityModel parentObject = controller.model.getEntity(index);
            if (parentObject!=null){
                controller.processMousePressEvent(event, isSelectionAllowed(), isSelectionEnabled(), indexIteratorFactory);
            }
        }
        if (event.isAccepted()) {
            //process click at tree indicator to collapse/expand branch but ignore edit event
            blockEditEvent = true;
            try{
                super.mousePressEvent(event);
            }finally{
                blockEditEvent = false;
            }
        }
        actions.refresh();
        final QModelIndex currentIndex = currentIndex();
        if (currentIndex!=null){
            verticalHeader.setCurrentRow(currentIndex);
            update(currentIndex);
        }
        if (postponedRereads!=null){
            executePostponedRereads(true);
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
    
    private PointInfo getPointInfo(final QPoint point, final QModelIndex index){        
        if (index==null){
            return PointInfo.UNKNOWN_POINT;
        }
        final ArrStr indexPath = indexToPath(index);
        final Property property = indexPath==null ? null : controller.model.getProperty(index);
        if (property==null){
            return new PointInfo(indexPath, null, null, false);
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
        return new PointInfo(indexPath, property.getId(), cellValue, pointInsideCheckBox);
    }
    
    private void processPostponedMouseButtonDblClick(){        
        final QModelIndex index = currentIndex();
        if (index != null) {
            if (model().flags(index).isSet(Qt.ItemFlag.ItemIsEditable)) {
                if (!controller.inEditingMode()) {
                    controller.openEditor(index);
                }
            }else{
                setExpanded(index, !isExpanded(index));
            }
        }  
    }
    
    private void processPostponedMouseButtonRelease(final PointInfo mousePressPoint,
                                                                                 final QMouseEvent releaseEvent){        
        if (mousePressPoint.insideCheckBox()){
            final QModelIndex index = indexAt(releaseEvent.pos());
            final PointInfo mouseReleasePoint = getPointInfo(releaseEvent.pos(),index);
            if (mouseReleasePoint.equals(mousePressPoint)){
                Application.processEventWhenEasSessionReady(viewport(), releaseEvent);
            }            
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

    @Override
    protected void mouseMoveEvent(QMouseEvent event) {
        if (event.buttons().isSet(MouseButton.RightButton) || event.buttons().isSet(MouseButton.LeftButton)) {
            //препятствуем перемещению мыши с нажатой кнопкой
            event.ignore();
        } else {
            super.mouseMoveEvent(event);
        }
    }

    @Override
    protected QModelIndex moveCursor(final CursorAction action, final KeyboardModifiers modifiers) {
        if (controller.canMoveCursor(action, state())) {
            final QModelIndex index = super.moveCursor(action, modifiers);
            return controller.afterMoveCursor(index, isSelectionAllowed(), isSelectionEnabled(), indexIteratorFactory, modifiers);
        }
        return null;
    }

    @Override
    protected void currentChanged(final QModelIndex current, final QModelIndex previous) {        
        finishEdit();
        controller.processCurrentChanged(current, previous, header());
        actions.refresh();
        verticalHeader.setCurrentRow(current);
        update(current);//Чтобы отрисовалась рамка текущей ячейки
    }        

    @SuppressWarnings("unused")
    private void onExpanded(final QModelIndex index) {
        if (controller.model.hasChildren(index)) {
            expandedItems.add(index);
            final boolean wasLocked;
            if (!controller.isLocked()) {
                controller.lockInput();
                wasLocked = true;
            } else {
                wasLocked = false;
            }
            boolean childGroupExists = false;
            try {
                controller.model.getChildGroup(index);
                childGroupExists = true;
                verticalHeader.expand(index);
            } catch (RuntimeException ex) {
                final EntityModel parentEntity = controller.model.getEntity(index);
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on creating child group model for parent object \'%s\'");
                getEnvironment().getTracer().error(String.format(title, parentEntity.getTitle()), ex);
                getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                        String.format(title, parentEntity.getTitle(),
                        ClientException.exceptionStackToString(ex)),
                        EEventSource.EXPLORER);
                getEnvironment().messageException(getEnvironment().getMessageProvider().translate("ExplorerException", "Error on Receiving Data"), ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex), ex);
            } finally {
                if (!childGroupExists) {
                    blockSignals(true);
                    try {
                        collapse(index);
                    } finally {
                        blockSignals(false);
                    }
                }
                if (wasLocked) {
                    controller.unlockInput();
                }
            }
        } else {
            blockSignals(true);
            try {
                collapse(index);
            } finally {
                blockSignals(false);
            }
        }
    }

    @SuppressWarnings("unused")
    private void onCollapsed(final QModelIndex index) {
        if (index.equals(currentForFetch)) {
            currentForFetch = currentForFetch.parent();
        }
        expandedItems.remove(index, false);
        actions.refresh();//moveNext
        verticalHeader.collapse(index);
    }

    @Override
    public void rereadAndSetCurrent(final Pid pid) throws InterruptedException, ServiceClientException {
        final QModelIndex current = currentIndex();
        if (pid == null || current == null) {
            reread(null, (Pid)null);
        } else {
            reread(current.parent(), pid);
        }
    }
    
    @Override
    public void rereadAndSetCurrent(final Collection<Pid> pids) throws InterruptedException, ServiceClientException {
        final QModelIndex current = currentIndex();
        if (pids == null || current == null) {
            reread(null, (Pid)null);
        } else {
            reread(current.parent(), pids);
        }
    }    

    @Override
    public void reread() throws InterruptedException, ServiceClientException {        
        
        if (controller.changingCurrentEntity()){
            postponeReread(null, null);
            return;
        }
        
        beforeReread();
        final SelectorModel model = controller.model;

        lockInput();
        try {
            model.reread(null);
        } finally {
            unlockInput();
        }
        if (model.rowCount(null) > 0) {
            controller.enterEntity(model.index(0, 0));
        }        
        updateSelectionMode();
        selector.refresh();
    }

    protected void beforeReread() {
        final SelectorModel model = controller.model;

        model.unsubscribeProperties(this);

        setEnabled(true);
        expandedItems.clear();
        currentForFetch = null;
    }

    public void rereadChildrenForCurrentEntity() throws InterruptedException, ServiceClientException {
        final QModelIndex current = currentIndex();
        if (current != null) {
            rereadImpl(current, null);
        }
    }
        
    public void reread(final QModelIndex parent, final Pid pid) throws InterruptedException, ServiceClientException {
        rereadImpl(parent, pid==null ? Collections.<Pid>emptyList() : Collections.singleton(pid));
    }
    
    public void reread(final QModelIndex parent, final Collection<Pid> pids) throws InterruptedException, ServiceClientException {
        rereadImpl(parent, pids==null ? Collections.<Pid>emptyList(): pids);
    }

    private void rereadImpl(final QModelIndex parent, final Collection<Pid> pids) throws InterruptedException, ServiceClientException {
        //Restore item that was current before reread if pids is empty collection 
        //Do not set current item after reread if pids is null
        
        final QModelIndex normalizedParent;
        if (parent==null || parent.column()==0){
            normalizedParent = parent;
        }else{
            normalizedParent = controller.model.index(parent.row(), 0,  parent.parent());
        }
        
        if (controller.changingCurrentEntity()){
            postponeReread(normalizedParent, pids==null ? Collections.<Pid>emptyList() : null);
            return;
        }
        
        final QModelIndex current = currentIndex();
        final boolean readingChildrenOfCurrent = current!=null && indexesEqual(current, normalizedParent);
        
        final SelectorModel model = (SelectorModel) model();
        model.unsubscribeProperties(this);

        //сохранили путь до текущего элемента        
        final int column = current==null ? horizontalHeader.getFirstVisibleColumnIndex() : current.column();        
        final Stack<Pid> parentPath = new Stack<>();
        final List<Pid> pidsToSearch = new LinkedList<>();
        if (pids!=null && !pids.isEmpty()){
            pidsToSearch.addAll(pids);
        }else if (pids!=null /*pids is empty*/ && normalizedParent==null && current!=null){
            final EntityModel currentEntityModel = controller.model.getEntity(current);
            if (currentEntityModel!=null){
                indexToPath(parentPath, current.parent());
                pidsToSearch.add(currentEntityModel.getPid());
            }
        }
        
        if (normalizedParent!=null){
            indexToPath(parentPath, normalizedParent);
        }              

        final List<Stack<Pid>> itemsToExpand = new ArrayList<>();//раскрытые элементы, которые находятся ниже текущего

        {//Сохранение раскрытых элементов
            QModelIndex index;
            for (int i = expandedItems.size() - 1; i >= 0; i--) {
                index = expandedItems.get(i);
                if (WidgetUtils.isParentIndex(normalizedParent, index)){
                    if (isExplicitlyExpanded(index)){
                        itemsToExpand.add(indexToPath(new Stack<Pid>(), index));
                    }
                    if (index.equals(currentForFetch)) {
                        currentForFetch = currentForFetch.parent();
                    }
                    expandedItems.remove(i);                    
                }
            }
        }
        final int rowsLoadingLimit =
                selector.getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_FOR_RESTORING_POSITION_CONFIG_PATH, 300);
        selector.blockRedraw();
        blockResizeSelectionColumnEvent = true;
        rereadProgressHandle.startProgress(null, true);        
        try {
            lockInput();
            try {
                if (isExpanded(normalizedParent)) {
                    collapse(normalizedParent);
                } 
                final GroupModel groupToReread = model.getChildGroup(normalizedParent);
                if (groupToReread.getEntitiesCount()==0 && groupToReread.hasMoreRows()){
                    //child group was not read at this moment so we do not need to reread it.
                    //Just reread group restrictions
                    groupToReread.readCommonSettings();
                }
                if (pidsToSearch.isEmpty()) {
                    //перечитывание
                    model.reread(normalizedParent);
                } else {
                    //очистка загруженных данных
                    model.reset(normalizedParent);
                    //загрузка первой порции новых данных
                    currentObjectFinder.setLoadingLimit(rowsLoadingLimit);
                    currentObjectFinder.setProgressTitleTemplate(restoringPositionMessageTemplate);
                    currentObjectFinder.resetCounters();
                    currentObjectFinder.loadMore(new SelectorWidgetDelegate(model, normalizedParent));
                }
            } finally {
                unlockInput();
                if (normalizedParent!=null){
                    this.update(normalizedParent);
                    this.dataChanged(normalizedParent, normalizedParent);
                }
            }

            QApplication.processEvents();//Обработка событий GetChildGroupEvent
            boolean needToRestartProgress = false;
            if (!itemsToExpand.isEmpty()) {
                //восстанление раскрытых элементов
                if (itemsToExpand.size() > 1) {
                    rereadProgressHandle.setMaximumValue(itemsToExpand.size());
                    rereadProgressHandle.setValue(0);
                }
                QModelIndex index = null;
                expandedObjectFinder.resetCounters();
                for (Stack<Pid> item : itemsToExpand) {
                    try {
                        index = pathToIndex(item, false, expandedObjectFinder);
                    } catch (InterruptedException exception) {
                        if (rereadProgressHandle.wasCanceled()) {
                            rereadProgressHandle.finishProgress();
                            needToRestartProgress = true;
                        }
                        break;
                    }
                    if (index != null) {
                        expand(index);
                        //Чтение данных в раскрытом элементе
                        if (model.hasChildren(index) && model.rowCount(index) == 0) {
                            try {
                                if (!expandedObjectFinder.loadMore(new SelectorWidgetDelegate(model, index))) {
                                    setExpanded(index, false);
                                }
                            } catch (InterruptedException exception) {
                                if (rereadProgressHandle.wasCanceled()) {
                                    rereadProgressHandle.finishProgress();
                                    needToRestartProgress = true;
                                }
                                setExpanded(index, false);
                                break;
                            } catch (ServiceClientException exception) {
                                setExpanded(index, false);
                            }
                        }
                        if (rereadProgressHandle.wasCanceled()) {
                            rereadProgressHandle.finishProgress();
                            needToRestartProgress = true;
                            break;
                        }
                    }
                    if (itemsToExpand.size() > 1) {
                        rereadProgressHandle.incValue();
                    }
                    rereadProgressHandle.setText(String.format(restoringExpandedItemsMessageTemplate, String.valueOf(expandedObjectFinder.getLoadedObjectsCount())));
                }
            }
            
            if (pids==null
                || (readingChildrenOfCurrent && pids.isEmpty())){
                actions.refresh();                
                return;
            }

            if (needToRestartProgress) {
                rereadProgressHandle.startProgress(null, true);
            }
            if (rereadProgressHandle.getMaximumValue() > 0) {
                rereadProgressHandle.setMaximumValue(0);
                rereadProgressHandle.setValue(-1);
            }           
            final QModelIndex currentItem;
            {//восстанавление текущего элемента                
                final boolean parentPathIsEmpty = parentPath.isEmpty() ;
                final QModelIndex parentIndex = parentPathIsEmpty ? null : pathToIndex(parentPath, null, true, currentObjectFinder);
                final boolean parentNodeFound = parentPath.isEmpty();
                final QModelIndex index;
                if (parentIndex!=null && (!parentNodeFound || pidsToSearch.isEmpty())){
                    index = parentIndex;
                }else{
                    if (parentIndex==null && (!parentNodeFound || pidsToSearch.isEmpty())){
                        index = null;
                    }else{
                        final QModelIndex childIndex = 
                                findByPid(controller.model, pidsToSearch, parentIndex, currentObjectFinder);                        
                        if (!parentPathIsEmpty && childIndex==null && model.rowCount(parentIndex) > 0){
                            index = model.index(model.rowCount(parentIndex) - 1, 0, parentIndex);
                        }else{
                            index = childIndex;
                        }
                    }                    
                }
                if (index != null) {
                    currentItem = index;
                } else if (model.rowCount(normalizedParent) > 0) {
                    currentItem = model.index(0, 0, normalizedParent);
                } else if (normalizedParent != null) {
                    currentItem = normalizedParent;
                } else if (model.rowCount(null) > 0) {
                    scrollTo(model.index(0, 0));
                    return;
                } else {
                    return;//в дереве нет элементов
                }
            }
            if (currentItem != null) {
                //вычитывание всех раскрытых элементов над текущим и переход к текущему
                boolean wasLocked = false;
                try {
                    for (QModelIndex expandedItem : expandedItems) {
                        if (StandardSelectorWidgetController.compareIndexes(expandedItem, currentItem) < 0) {
                            if (!wasLocked) {
                                wasLocked = true;
                                lockInput();
                            }
                            currentObjectFinder.loadAll(new SelectorWidgetDelegate(model, expandedItem));
                        }
                    }
                } finally {
                    if (wasLocked) {
                        unlockInput();
                    }
                }
                try {                    
                    final QModelIndex actualIndex = model.index(currentItem.row(), column, currentItem.parent());
                    controller.enterEntity(actualIndex);
                    scrollTo(actualIndex);
                } finally {
                    model.increaseRowsLimit();
                }
            }
        } finally {
            blockResizeSelectionColumnEvent = false;
            final EntityModel currentEntityModel = model.getEntity(currentIndex());
            if (currentEntityModel!=null){
                model.subscribeProperties(currentEntityModel, this);
            }            
            rereadProgressHandle.finishProgress();
            selector.unblockRedraw();
            updateSelectionMode();
        }
    }
    
    private void postponeReread(final QModelIndex parent, final Collection<Pid> pids){
        final PostponedRereadCall rereadCall = new PostponedRereadCall(parent, controller.model, pids);
        if (parent==null){
            postponedRereads = new LinkedList<>();
            Application.processEventWhenEasSessionReady(this, new SelectorTreeEvent(EventType.REREAD));
        }else{
            boolean merged = false;
            if (postponedRereads==null){
                postponedRereads = new LinkedList<>();               
                Application.processEventWhenEasSessionReady(this, new SelectorTreeEvent(EventType.REREAD));
            }else{
                for (PostponedRereadCall postponedReread: postponedRereads){
                    if (postponedReread.merge(rereadCall)){
                        merged = true;
                        break;
                    }
                }
            }
            if (!merged){
                postponedRereads.add(rereadCall);
            }
        }
    }

    protected void readAll(final QModelIndex index) throws InterruptedException, ServiceClientException {
        final SelectorModel model = (SelectorModel) model();
        final boolean wasLocked;
        if (!controller.isLocked()) {
            lockInput();
            wasLocked = true;
        } else {
            wasLocked = false;
        }
        try {
            while (model.canReadMore(index)) {
                model.readMore(index);
            }
        } finally {
            if (wasLocked) {
                unlockInput();
            }
        }
        updateSelectionMode();
    }
    
    protected boolean canCreateInGroup(final GroupModel group, final EntityModel parentEntityModel, final QModelIndex parentIndex){
        return !getRestrictions(group).getIsCreateRestricted();
    }

    @Override
    public void afterPrepareCreate(final EntityModel entity) {
        if (!(entity.getContext() instanceof IContext.InSelectorCreating)) {
            if (entity.getContext() == null) {
                throw new IllegalArgumentException("Invalid entity context.\n"
                        + IContext.InSelectorCreating.class.getSimpleName()
                        + " context expected, but no context found.");
            } else {
                throw new IllegalArgumentException("Invalid entity context.\n"
                        + IContext.InSelectorCreating.class.getSimpleName()
                        + " context expected, but "
                        + entity.getContext().getClass().getSimpleName()
                        + " context found.");
            }
        }

        final GroupModel contextGroup = ((IContext.InSelectorCreating) entity.getContext()).group;
        final QModelIndex currentIndex = currentIndex();
        final QModelIndex parentIndex = currentIndex != null ? currentIndex.parent() : null;
        final SelectorModel model = (SelectorModel) model();
        final EntityModel parentEntity;
        if (currentIndex == null) {
            if (model.rowCount() == 0) {
                parentEntity = null;
            } else {
                throw new IllegalUsageError("Cannot find parent entity");
            }
        } else if (contextGroup == selector.getGroupModel()) {
            //создание сущности в текущей группе
            parentEntity = model.getEntity(parentIndex);
        } else if (contextGroup == model.getChildGroup(currentIndex)) {
            //создание сущности в дочерней группе
            parentEntity = model.getEntity(currentIndex);
        } else {
            throw new IllegalUsageError("Cannot find parent entity");
        }

        final Map<Id, Object> propertyValues = new HashMap<>();
        model.fillConditionalProperties(parentEntity, propertyValues);
        for (Map.Entry<Id, Object> entry : propertyValues.entrySet()) {
            entity.getProperty(entry.getKey()).setValueObject(entry.getValue());
        }
    }

    @Override
    public void clear() {
        expandedItems.clear();
        controller.clearData(this, cornerButton, horizontalHeader);
    }

    @Override
    public boolean setFocus(final Property property) {
        final QModelIndex index = controller.findIndexForProperty(property);
        if (index != null) {
            setCurrentIndex(index);
            controller.openEditor(index);
            final PropEditor editor = controller.getCurrentPropEditor();
            if (editor != null) {
                editor.setFocus();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void rowsAboutToBeRemoved(final QModelIndex parentIndex, final int startRow, final int endRow) {
        for (int row=startRow; row<=endRow; row++){
            expandedItems.remove(controller.model.index(row,0,parentIndex), true);
        }
        final int count = endRow - startRow + 1;
        QModelIndex expandedItem;
        for (int i = expandedItems.size() - 1; i >= 0; i--) {
            expandedItem = expandedItems.get(i);
            if (indexesEqual(expandedItem.parent(), parentIndex) && expandedItem.row() > endRow) {
                expandedItems.set(i,model().index(expandedItem.row() - count, 0, parentIndex));
            }
        }
        super.rowsAboutToBeRemoved(parentIndex, startRow, endRow);
    }        
       
    @Override
    protected void rowsInserted(final QModelIndex parent, final int start, final int end) {
        final List<Integer> rows = new ArrayList<>();
        QModelIndex index;
        for (int row=start; row<=end; row++){
            index = controller.model.index(row, 0, parent);
            if (controller.model.hasChildren(index) || controller.model.canCreateChild(index)){
                rows.add(row);
            }
        }
        if (!rows.isEmpty()){
            QCoreApplication.postEvent(this, new GetChildGroupEvent(parent, rows));
        }
        finishEdit();
        super.rowsInserted(parent, start, end);
        updateColumnSpan(parent, start, end);
    }

    //Блокировка контекстного поиска
    @Override
    public void keyboardSearch(String search) {
        controller.processKeyboardSearch();
    }

    public final void applySettings() {
        normalStyleTextOptions = 
            (ExplorerTextOptions)getEnvironment().getTextOptionsProvider().getOptions(EnumSet.of(ETextOptionsMarker.SELECTOR_ROW), ESelectorRowStyle.NORMAL);        
        controller.applySettings();
        horizontalHeader.applySettings();        
        update();
        if (isSelectionEnabled()){
            resizeColumnToContents(0);
        }
    }
    
    @Override
    public void closeEvent(final QCloseEvent event) {
        final QWidget viewport = (QWidget) findChild(QWidget.class, "qt_scrollarea_viewport");
        if (viewport != null) {
            viewport.removeEventFilter(filteredMouseEventListener);
        }

        for (SelectorColumnModelItem column : controller.model.getSelectorColumns()) {
            column.unsubscribe(this);
        }
        scheduledRead.clear();
        postponedRereads = null;
        controller.model.unsubscribeProperties(this);

        {//disconnecxting signals for GC
            actions.close();
            header().disconnect();
        }
        horizontalHeader.saveSettings();
        horizontalHeader.close();
        if (model() != null) {
            getSelection().removeListener(selectionHandler);
            setModel(null);            
            controller.model.clear();
            controller.model.dispose();
        }
        postedEvents.clear();
        expandedItems.clear();
        super.closeEvent(event);
    }

    @Override
    public void finishEdit() {
        final PropEditor propEditor = controller.getCurrentPropEditor();
        if (propEditor != null) {
            propEditor.finishEdit();
            closeEditor(propEditor, EndEditHint.SubmitModelCache);
        }
    }

    @Override
    protected boolean edit(final QModelIndex index, final EditTrigger trigger, final QEvent event/*can be null*/) {
        if (blockEditEvent){
            return false;
        }else{
            if (isMouseClickWithCtrl(event)){
                if (postponedMousePressEvent!=null){
                    postponedMousePressEvent.setEdit((QMouseEvent)event);
                    return false;
                }          
            }
            return editImpl(index, trigger, event);
        }
    }
    
    private boolean editImpl(final QModelIndex index, final EditTrigger trigger, final QEvent event){
        final boolean processed = 
            controller.processEditEvent(event, index, isSelectionEnabled(), indexIteratorFactory);
        if ((event==null || event.isAccepted())
            && super.edit(index, trigger, event)){
            if (!processed){
                update();
            }
            return true;
        }
        return processed;        
    }

    @Override
    protected void keyPressEvent(final QKeyEvent event) {
        final EHierarchicalSelectionMode selectAllMode;
        if (isSelectionAllowed()){
            selectAllMode = null;
        }else{
            selectAllMode = getSelectAllMode();
        }
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
    protected void closeEditor(final QWidget editor, final EndEditHint endEditHint) {
        if (endEditHint == EndEditHint.RevertModelCache && (editor instanceof IModelWidget)) {
            ((IModelWidget) editor).refresh(null);
        }
        commitData(editor);
        super.closeEditor(editor, endEditHint);
        editor.close();
    }

    @Override
    protected void drawBranches(final QPainter painter, final QRect rect, final QModelIndex modelIndex) {        
        branchesBackgroundStyleOptions.setRect(rect);
        ItemDelegatePainter.getInstance().drawBackground(painter, branchesBackgroundStyleOptions, normalStyleTextOptions.getBackground());
        super.drawBranches(painter, rect, modelIndex);
    }        

    @Override
    protected void drawRow(final QPainter painter, final QStyleOptionViewItem options, final QModelIndex index) {
        if (!isSelectionEnabled() && controller.model.isBrokenEntity(index)){
            final QModelIndex delegateInex = controller.model.index(index.row(), 1, index.parent());
            final QRect rect = options.rect();
            final int branchesWidth = visualRect(delegateInex).x();
            temporaryRect.setRect(0, rect.y(), branchesWidth, rect.height());
            drawBranches(painter, temporaryRect, delegateInex);
            rect.adjust(branchesWidth, 0, 0, 0);
            options.setRect(rect);
            itemDelegate().paint(painter, options, delegateInex);
        }else{
            super.drawRow(painter, options, index);
        }
        if (!selector.isInternalPaintingActive()) {
            final SelectorModel model = controller.model;
            {
                final ArrStr path = indexToPath(index);
                final String indexPath = path==null ? null : path.toString();
                if (isExpanded(index) && model.rowCount(index) == 0 && path!=null && !scheduledRead.contains(indexPath)) {
                    Application.processEventWhenEasSessionReady(this, new ReadMoreEvent(indexPath, true, true));
                    scheduledRead.add(indexPath);

                    if (safelyCheckIfCanReadMore(index)) {
                        currentForFetch = index;
                    }
                }
            }

            for (QModelIndex idx = index; idx != null; idx = idx.parent()) {
                final ArrStr path = indexToPath(idx.parent());
                final String idxPath = path==null ? null : path.toString();
                if (isLastChild(idx) && model.canReadMore(idx.parent()) && path!=null && !scheduledRead.contains(idxPath)) {
                    Application.processEventWhenEasSessionReady(this, new ReadMoreEvent(idxPath, false, true));
                    scheduledRead.add(idxPath);
                    break;
                }
            }
        }
    }        
    
    private boolean safelyCheckIfCanReadMore(final QModelIndex index){//no request to server
        final SelectorModel model = controller.model;
        if (index==null){
            return model.canReadMore(null);
        }
        final GroupModel group = model.getCachedChildGroup(index);
        if (group==null){
            return !model.errorWasDetected(index) && model.hasChildren(index);
        }else{
            return model.canReadMore(index);
        }
    }

    @Override
    protected void scrollContentsBy(final int deltaX, final int deltaY) {
        if (controller.isLocked()) {
            if (verticalHeader!=null){
                verticalHeader.verticalScrollBar().setValue(verticalScrollBar().value());
            }            
        }else{
            final QModelIndex after = indexAt(viewport().rect().bottomRight());
            final SelectorModel model = controller.model;
            while (currentForFetch != null
                    && (after == null
                    || levelForIndex(after) < levelForIndex(currentForFetch)
                    || (levelForIndex(after) == levelForIndex(currentForFetch) && after.row() > currentForFetch.row()))) {
                if (model.canReadMore(currentForFetch)) {
                    final String idxPath = indexToPath(currentForFetch).toString();
                    QCoreApplication.sendEvent(this, new ReadMoreEvent(idxPath, false, true));
                    return;
                } else {
                    currentForFetch = currentForFetch.parent();
                }
            }
            if (verticalHeader!=null){
                verticalHeader.verticalScrollBar().setValue(verticalScrollBar().value());
            }
            super.scrollContentsBy(deltaX, deltaY);        
        }
    }
    
    public final int getRowSizeHint(final QModelIndex index){
        return indexRowSizeHint(index);
    }

    @Override
    public int sizeHintForColumn(final int column) {
        if (column==0 && isSelectionEnabled()){
            executeDelayedItemsLayout();
            ensurePolished();
            final int selectionCellWidth = horizontalHeader.getSelectionColumnWidth();
            final int nestingLevel = expandedItems.getMaxVisibleNestingLevel();
            return selectionCellWidth + (nestingLevel + 1) * this.indentation();
        }
        return super.sizeHintForColumn(column);
    }

    @Override
    public QSize sizeHint() {
        this.indexRowSizeHint(currentForFetch);
        final QSize size = super.sizeHint();
        size.setHeight(DEFAULT_HEIGHT);
        final int width = horizontalHeader.sizeHint().width()+frameWidth()*2;
        if (size.width()<width){
            final Dimension sizeLimit = WidgetUtils.getWndowMaxSize();
            size.setWidth(Math.min(width, (int)sizeLimit.getWidth()));
        }
        return size;
    }

    @Override
    protected void updateGeometries() {
        if (!updateGeometriesRecursionBlock){
            updateGeometriesRecursionBlock = true;
            try{
                super.updateGeometries();
                if (verticalHeader!=null && horizontalHeader!=null){
                    updateContentGeometries();
                }                
            }finally{
                updateGeometriesRecursionBlock = false;
            }
        }
    }
    
    private void updateContentGeometries(){
        final boolean isEmpty  = controller.model.rowCount(null)==0;
        if (isEmpty){
            verticalHeader.setVisible(false);
            cornerButton.setVisible(false);
        }else{
            verticalHeader.setVisible(true);
            cornerButton.setVisible(true);
        }
        final int width =  isEmpty ? 0 : verticalHeader.width();
        final int height = horizontalHeader.isHidden() ? 0 : horizontalHeader.sizeHint().height();

        final boolean reverse = isRightToLeft();
        if (reverse) {
            setViewportMargins(0, height, width, 0);
        } else {
            setViewportMargins(width, height, 0, 0);
        }

        final QRect vg = viewport().geometry();
        final int verticalLeft = reverse ? vg.right() + 1 : (vg.left() - width);        
        verticalHeader.setGeometry(verticalLeft, vg.top(), width, vg.height());

        final int horizontalTop = vg.top() - height;
        horizontalHeader.setGeometry(vg.left(), horizontalTop, vg.width(), height);              

        if (!isEmpty){
            cornerButton.setFixedSize(width, height);
            cornerButton.setFixedPos( verticalHeader.pos().x(), horizontalHeader.pos().y() );
            cornerButton.move(verticalHeader.pos().x(), horizontalHeader.pos().y());
        }
    }
    
    private void processColumnResizedEvent(){
        if (!processColumnResizedRecursionBlock){
            processColumnResizedRecursionBlock = true;            
            try{
                if (!columnsToUpdate.isEmpty()){
                    updateGeometries();
                    final Rectangle rect = new Rectangle();
                    final QWidget viewport = viewport();
                    final int viewportHeight = viewport.height();
                    final int viewportWidth = viewport.width();
                    for (Integer column: columnsToUpdate) {
                        int x = columnViewportPosition(column);
                        if (isRightToLeft())
                            rect.add(new Rectangle(0, 0, x + columnWidth(column), viewportHeight));
                        else
                            rect.add(new Rectangle(x, 0, viewportWidth - x, viewportHeight));
                    }                    
                    if (!rect.isEmpty()){
                        viewport.update(WidgetUtils.awtRect2QRect(rect,temporaryRect));
                    }
                }
            }finally{
                columnsToUpdate.clear();
                processColumnResizedRecursionBlock = false;                
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void afterColumnResized(int column, int oldSize, int newSize){
        columnsToUpdate.add(column);
        if (updateGeometriesRecursionBlock){
            processColumnResizedEvent();            
        }else {
            postSelectorTreeEvent(EventType.COLUMN_RESIZED);
        }
    }
    
    private boolean expandOrCollapse(final QModelIndex index, final QPoint point){
        final QAbstractItemView.State state = state();
        if ((state != QAbstractItemView.State.NoState && state != QAbstractItemView.State.EditingState)
            || !viewport().rect().contains(point)){
            return true;
        }
        if (controller.model.hasChildren(index)){
            final QModelIndex treeIndex;
            if (index.column()==0){
                treeIndex = index;
            }else{
                treeIndex = controller.model.index(index.row(),0,index.parent());
            }
            if (controller.model.rowCount(treeIndex)==0){
                try{
                    if (!controller.model.readMore(treeIndex)){
                        //remove tree indicator
                        dataChanged(treeIndex, treeIndex);
                        doItemsLayout();
                        return false;
                    }
                }catch(ServiceClientException ex){
                    controller.processErrorOnReceivingData(ex);
                    return false;
                }catch(InterruptedException ex){
                    return false;
                }
            }
            if (isExpanded(treeIndex)){
                collapse(treeIndex);
            }else{
                expand(treeIndex);
            }
            updateGeometries();
            viewport().update();
            return true;
        }
        return false;        
    }
    
    private QModelIndex decorationIndexAt(final QPoint point){
        executeDelayedItemsLayout();
        final QModelIndex index = indexAt(point);
        if (index==null || index.column()!=treePosition()){
            return null;
        }
        final QRect returning = indexDecorationRect(index);
        return returning.contains(point) ? index : null;
    }    

    private QRect indexDecorationRect(final QModelIndex index){
        final int indent = indentation();
        final int itemIndentation = (levelForIndex(index)+1)*indent;
        final int treeColumn = treePosition();
        final int position = horizontalHeader.sectionViewportPosition(treeColumn);
        final int size = horizontalHeader.sectionSize(treeColumn);        
        final QRect visualRect = visualRect(index);        

        if (isRightToLeft()){
            temporaryRect.setRect(position+size-itemIndentation, visualRect.y(), indent, visualRect.height());
        }else{
            temporaryRect.setRect(position+itemIndentation-indent, visualRect.y(), indent, visualRect.height());
        }        
        temporaryStyleOption.initFrom(this);
        temporaryStyleOption.setRect(temporaryRect);
        return style().subElementRect(QStyle.SubElement.SE_TreeViewDisclosureItem, temporaryStyleOption, this);
    }
            
    private Stack<Pid> indexToPath(Stack<Pid> path, final QModelIndex index) {
        if (path == null) {
            path = new Stack<>();
        }
        final SelectorModel model = (SelectorModel) model();
        for (QModelIndex idx = index; idx != null; idx = idx.parent()) {
            if (model.getEntity(idx) == null) {
                path.clear();
                break;
            } else {
                path.push(model.getEntity(idx).getPid());
            }
        }
        return path;
    }

    public final ArrStr indexToPath(final QModelIndex index) {
        if (index == null) {
            return new ArrStr();
        }
        final SelectorModel model = (SelectorModel) model();
        final ArrStr path = new ArrStr();
        EntityModel entityModel;
        for (QModelIndex idx = index; idx != null; idx = idx.parent()) {
            entityModel = model.getEntity(idx);
            if (entityModel == null) {
                return null;
            } else {
                path.add(entityModel.getPid().toStr());
            }
        }
        return path;
    }

    public final QModelIndex pathToIndex(final ArrStr path, final boolean nearest) throws InterruptedException, ServiceClientException {
        if (path == null || path.isEmpty()) {
            return null;
        }
        final Stack<Pid> parsedPath = new Stack<>();
        for (String pathItem : path) {
            parsedPath.push(Pid.fromStr(pathItem));
        }
        final SelectorModelDataLoader dataLoader = new SelectorModelDataLoader(getEnvironment());
        dataLoader.setConfirmationMessageText(getEnvironment().getMessageProvider().translate("Selector", "Confirm?"));
        dataLoader.setLoadingLimit(-1);
        try {
            return pathToIndex(parsedPath, nearest, dataLoader);
        } finally {
            controller.model.increaseRowsLimit();
        }
    }

    private QModelIndex pathToIndex(final Stack<Pid> path, final boolean nearest, final SelectorModelDataLoader dataLoader) throws InterruptedException, ServiceClientException {
        return pathToIndex(path, null, nearest, dataLoader);
    }
    
    private QModelIndex findByPid(final SelectorModel model, final Collection<Pid> pids, final QModelIndex parent, final SelectorModelDataLoader dataLoader) throws InterruptedException {
        if (parent!=null){
            expand(parent);
        }
        
        try {
            if (model.hasChildren(parent) && model.rowCount(parent) == 0 && !dataLoader.loadMore(new SelectorWidgetDelegate(model, parent))) {
                return null;
            }
        } catch (ServiceClientException exception) {
            controller.processErrorOnReceivingData(exception);
            return null;
        }
        
        int row;
        try {
            row = dataLoader.findObjectByPid(new SelectorWidgetDelegate(model, parent), pids);
        } catch (ServiceClientException exception) {
            controller.processErrorOnReceivingData(exception);
            row = -1;
        }        
        return row >= 0 ? model.index(row, 0, parent) : null;        
    }

    private QModelIndex pathToIndex(final Stack<Pid> path, final QModelIndex startWith, final boolean nearest, final SelectorModelDataLoader dataLoader) throws InterruptedException {
        final SelectorModel model = (SelectorModel) model();
        QModelIndex index = startWith, next;        
        int row;
        lockInput();
        try {
            while (!path.isEmpty()) {
                next = findByPid(model, Collections.singleton(path.peek()), index, dataLoader);
                if (next==null){
                    if (nearest) {
                        if (model.rowCount(index) > 0) {
                            return model.index(model.rowCount(index) - 1, 0, index);
                        } else {
                            return index;
                        }
                    }else{
                        return null;
                    }
                }else{
                    path.pop();
                    index = next;
                }
            }
        } finally {
            unlockInput();
        }
        return index;
    }

    private static int levelForIndex(final QModelIndex index) {
        int level = 0;
        for (QModelIndex parent = index.parent(); parent != null; parent = parent.parent()) {
            level++;
        }
        return level;
    }

    private static boolean indexesEqual(final QModelIndex index1, final QModelIndex index2) {
        if (index1==index2){//NOPMD
            return true;
        }
        return index1 == null ? index2 == null : index2 != null && index1.internalId() == index2.internalId();
    }

    private static int compareIndexes(final QModelIndex index1, final QModelIndex index2) {
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

    private boolean isLastChild(final QModelIndex index) {
        return index != null ? model().rowCount(index.parent()) == index.row() + 1 : false;
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    private String getTitleOfCreateChildObjectActionImpl(final GroupModel groupModel) {
        if (createChildObjectCustomTitle == null || createChildObjectCustomTitle.isEmpty()) {
            if (groupModel == null || !groupModel.getSelectorPresentationDef().getClassPresentation().hasObjectTitle()) {
                return getEnvironment().getMessageProvider().translate("Selector", "Create Child Object...");
            } else {
                final String childGroupTitle =
                        groupModel.getSelectorPresentationDef().getClassPresentation().getObjectTitle();
                final String actionTitle =
                        String.format(getEnvironment().getMessageProvider().translate("Selector", "Create subobject \"%s\"..."), childGroupTitle);
                return ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), actionTitle);
            }
        } else {
            return ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), createChildObjectCustomTitle);
        }
    }

    public final String getTitleOfCreateChildObjectAction() {
        final SelectorModel model = (SelectorModel) model();
        final QModelIndex currentIndex = currentIndex();
        final GroupModel childGroup;
        if (currentIndex != null && model != null) {
            try {
                childGroup = model.getChildGroup(currentIndex);
            } catch (Throwable exception) {
                final EntityModel parentEntity = model.getEntity(currentIndex);
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on creating child group model for parent object \"%s\":\n %s");
                getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                        String.format(title, parentEntity.getTitle(),
                        ClientException.exceptionStackToString(exception)),
                        EEventSource.EXPLORER);
                return getTitleOfCreateChildObjectActionImpl(null);
            }
        } else {
            childGroup = null;
        }
        return getTitleOfCreateChildObjectActionImpl(childGroup);
    }

    public final void setTitleOfCreateChildObjectAction(final String title) {
        createChildObjectCustomTitle = title;
        if (model() != null) {
            actions.refresh();
        }
    }

    private String getTitleOfCreateObjectActionImpl(final GroupModel groupModel) {
        if (createObjectCustomTitle == null || createObjectCustomTitle.isEmpty()) {
            if (groupModel == null || !groupModel.getSelectorPresentationDef().getClassPresentation().hasObjectTitle()) {
                return getEnvironment().getMessageProvider().translate("Selector", "Create Object...");
            } else {
                final String groupTitle = groupModel.getSelectorPresentationDef().getClassPresentation().getObjectTitle();
                final String actionTitle =
                        String.format(getEnvironment().getMessageProvider().translate("Selector", "Create object \"%s\"..."), groupTitle);
                return ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), actionTitle);
            }
        } else {
            return ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), createObjectCustomTitle);
        }
    }

    public final String getTitleOfCreateObjectAction() {
        final SelectorModel model = (SelectorModel) model();
        final QModelIndex currentIndex = currentIndex();
        final GroupModel group;
        if (currentIndex != null && model != null) {
            try {
                group = model.getChildGroup(currentIndex.parent());
            } catch (Throwable exception) {
                final EntityModel parentEntity = model.getEntity(currentIndex.parent());
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on creating child group model for parent object \"%s\":\n %s");
                getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                        String.format(title, parentEntity.getTitle(),
                        ClientException.exceptionStackToString(exception)),
                        EEventSource.EXPLORER);
                return getTitleOfCreateObjectActionImpl(null);
            }
        } else {
            group = null;
        }
        return getTitleOfCreateObjectActionImpl(group);
    }

    public final void setTitleOfCreateObjectAction(final String title) {
        createObjectCustomTitle = title;
        if (model() != null) {
            actions.refresh();
        }
    }

    public final String getTitleOfPasteObjectAction() {
        final SelectorModel model = (SelectorModel) model();
        final QModelIndex currentIndex = currentIndex();
        final GroupModel group;
        if (currentIndex != null && model != null) {
            try {
                group = model.getChildGroup(currentIndex.parent());
            } catch (Throwable exception) {
                final EntityModel parentEntity = model.getEntity(currentIndex.parent());
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on creating child group model for parent object \"%s\":\n %s");
                getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                        String.format(title, parentEntity.getTitle(),
                        ClientException.exceptionStackToString(exception)),
                        EEventSource.EXPLORER);
                return getTitleOfPasteObjectActionImpl(null, getEnvironment().getClipboard());
            }
        } else {
            group = null;
        }
        return getTitleOfPasteObjectActionImpl(group, getEnvironment().getClipboard());
    }

    private String getTitleOfPasteObjectActionImpl(final GroupModel groupModel, final Clipboard clipboard) {
        if (pasteObjectCustomTitle == null || pasteObjectCustomTitle.isEmpty()) {
            final MessageProvider mp = getEnvironment().getMessageProvider();
            if (groupModel == null || clipboard.size() == 0) {
                return mp.translate("Selector", "Paste");
            } else if (clipboard.size() == 1) {
                final String title = clipboard.iterator().next().getTitle();
                final String toolTipFormat = mp.translate("Selector", "Paste Object '%1$s'...");
                final String toolTip = String.format(toolTipFormat, title);
                //return toolTip.length() > 100 ? mp.translate("Selector", "Paste Object...") : toolTip;
                return toolTip;
            } else {
                return mp.translate("Selector", "Paste Objects...");
            }
        } else {
            return ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), pasteObjectCustomTitle);
        }
    }

    public final void setTitleOfPasteObjectAction(final String title) {
        pasteObjectCustomTitle = title;
        if (model() != null) {
            actions.refresh();
        }
    }

    public final String getTitleOfPasteChildObjectAction() {
        final SelectorModel model = (SelectorModel) model();
        final QModelIndex currentIndex = currentIndex();
        final GroupModel childGroup;
        if (currentIndex != null && model != null) {
            try {
                childGroup = model.getChildGroup(currentIndex);
            } catch (Throwable exception) {
                final EntityModel parentEntity = model.getEntity(currentIndex);
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on creating child group model for parent object \"%s\":\n %s");
                getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                        String.format(title, parentEntity.getTitle(),
                        ClientException.exceptionStackToString(exception)),
                        EEventSource.EXPLORER);
                return getTitleOfPasteChildObjectActionImpl(null, getEnvironment().getClipboard());
            }
        } else {
            childGroup = null;
        }
        return getTitleOfPasteChildObjectActionImpl(childGroup, getEnvironment().getClipboard());
    }

    private String getTitleOfPasteChildObjectActionImpl(final GroupModel groupModel, final Clipboard clipboard) {
        if (pasteChildObjectCustomTitle == null || pasteChildObjectCustomTitle.isEmpty()) {
            final MessageProvider mp = getEnvironment().getMessageProvider();
            if (groupModel == null || clipboard.size() == 0) {
                return mp.translate("Selector", "Paste");
            } else if (clipboard.size() == 1) {
                final String title = clipboard.iterator().next().getTitle();
                final String toolTipFormat = mp.translate("Selector", "Paste Child Object '%1$s'...");
                final String toolTip = String.format(toolTipFormat, title);
                return toolTip.length() > 100 ? mp.translate("Selector", "Paste Child Object...") : toolTip;
            } else {
                return mp.translate("Selector", "Paste Child Objects...");
            }
        } else {
            return ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), pasteChildObjectCustomTitle);
        }
    }

    public final void setTitleOfPasteChildObjectAction(final String title) {
        pasteChildObjectCustomTitle = title;
        if (model() != null) {
            actions.refresh();
        }
    }
    
    public final int getNestingLevelOfEntityModel(final EntityModel entityModel){
        return controller.model.getNestingLevelOfEntityModel(entityModel);
    }
    
    public final int getNestingLevelOfGroupModel(final GroupModel groupModel){
        return controller.model.getNestingLevelOfGroupModel(groupModel);
    }

    @Override
    public void scrollTo(final QModelIndex index, final ScrollHint scrollHint) {
        if (scrollHint == ScrollHint.EnsureVisible) {
            boolean wasExpanded = false;
            for (QModelIndex parent = index.parent(); parent != null; parent = parent.parent()) {
                if (!isExpanded(model().index(parent.row(), 0, parent.parent()))) {
                    setExpanded(model().index(parent.row(), 0, parent.parent()), true);
                    wasExpanded = true;
                }
            }
            if (wasExpanded) {
                QApplication.processEvents();
            }
        }
        super.scrollTo(index, scrollHint);
    }

    @Override
    public void doItemsLayout() {
        if (getEnvironment().getEasSession().isBusy()) {
            postSelectorTreeEvent(EventType.LAYOUT_ITEMS);
        } else {
            super.doItemsLayout();
            if (controller != null && model() != null && controller.model.isLocked()) {
                //Когда модель селектора заблокирована метод hasChildren может вернуть неправильный результат, который кэшируется.
                //Чтобы обновить кэш планируем повторный вызов метода doItemsLayout
                postSelectorTreeEvent(EventType.LAYOUT_ITEMS);
            }
        }
    }

    Collection<String> getExpandedItems() {
        final List<ArrStr> allExpandedItems = new ArrayList<>(expandedItems.size());
        ArrStr expandedItemPath;
        int maxExpandedItemLevel = 0;
        for (QModelIndex expandedItemIndex : expandedItems) {
            expandedItemPath = indexToPath(expandedItemIndex);
            Collections.reverse(expandedItemPath);
            if (expandedItemPath != null && !expandedItemPath.isEmpty()) {
                if (maxExpandedItemLevel < expandedItemPath.size()) {
                    maxExpandedItemLevel = expandedItemPath.size();
                }
                allExpandedItems.add(expandedItemPath);
            }
        }
        if (maxExpandedItemLevel == 0) {
            return Collections.<String>emptyList();
        }
        Collections.sort(allExpandedItems, new Comparator<ArrStr>() {
            @Override
            public int compare(final ArrStr arr1, final ArrStr arr2) {
                return Integer.valueOf(arr1.size()).compareTo(arr2.size());
            }
        });
        final List<String> packedExpandedItems = new ArrayList<>();
        final Set<String> currentBrunch = new HashSet<>();
        ArrStr currentItemPath;
        String currentItemPathAsStr;
        ArrStr parentItemPath;
        String lastPathItem;
        int parentItemIndex;
        for (int level = 1; level <= maxExpandedItemLevel; level++) {
            currentBrunch.clear();
            while (!allExpandedItems.isEmpty() && allExpandedItems.get(0).size() == level) {
                currentItemPath = allExpandedItems.get(0);
                lastPathItem = currentItemPath.get(level - 1);
                currentBrunch.add(lastPathItem);
                allExpandedItems.remove(0);
                currentItemPathAsStr = currentItemPath.toString();
                if (level == 1) {
                    packedExpandedItems.add(currentItemPathAsStr);
                } else {
                    parentItemPath = new ArrStr(currentItemPath);
                    parentItemPath.remove(level - 1);
                    parentItemIndex = packedExpandedItems.indexOf(parentItemPath.toString());
                    if (parentItemIndex >= 0) {
                        packedExpandedItems.set(parentItemIndex, currentItemPathAsStr);
                    } else {
                        packedExpandedItems.add(currentItemPathAsStr);
                    }
                }
            }
            for (int i = allExpandedItems.size() - 1; i >= 0; i--) {
                currentItemPath = allExpandedItems.get(i);
                if (!currentBrunch.contains(currentItemPath.get(level - 1))) {
                    allExpandedItems.remove(i);
                }
            }
        }
        return Collections.unmodifiableList(packedExpandedItems);
    }

    void expandItems(final Collection<String> items) throws InterruptedException, ServiceClientException {
        if (items != null) {
            ArrStr itemPath;
            QModelIndex itemIndex;
            for (String itemPathAsStr : items) {
                if (itemPathAsStr != null && !itemPathAsStr.isEmpty()) {
                    try {
                        itemPath = ArrStr.fromValAsStr(itemPathAsStr);
                    } catch (WrongFormatError error) {
                        final String traceMessage =
                                getEnvironment().getMessageProvider().translate("TraceMessage", "Unable to restore expanded item from string \'%1$s\':\n%2$s");
                        final String reason = ClientException.exceptionStackToString(error);
                        getEnvironment().getTracer().warning(String.format(traceMessage, itemPathAsStr, reason));
                        continue;
                    }
                    Collections.reverse(itemPath);
                    itemIndex = pathToIndex(itemPath, false);
                    if (itemIndex != null) {
                        expand(itemIndex);
                    }
                }
            }
        }
    }
    
    private boolean isExplicitlyExpanded(final QModelIndex index){
        for (QModelIndex idx = index; idx!=null; idx=idx.parent()){
            if (!isExpanded(idx)){
                return false;
            }
        }
        return true;
    }
    
    protected final HierarchicalSelection<SelectorNode> getSelection(){
        return controller.model.getSelection();
    }
    
    private GroupModel getRootGroupModel(){
        return controller.model.getRootGroupModel();
    }
    
    public final void setAdditionalSelectionModes(final List<EHierarchicalSelectionMode> mode){
        controller.model.setDefaultAdditionalSelectionModes(mode);
    }
    
    public final List<EHierarchicalSelectionMode> getAdditionalSelectionModes(){
        return controller.model.getDefaultAdditionalSelectionModes();
    }       

    public final void setPrimarySelectionMode(final EnumSet<EHierarchicalSelectionMode> mode){
        controller.model.setDefaultPrimarySelectionMode(mode);
    }
    
    public final EnumSet<EHierarchicalSelectionMode> getPrimarySelectionMode(){
        return controller.model.getDefaultPrimarySelectionMode();
    }
    
    @Override
    protected void disposed() {
        controller.disposed();
        super.disposed();
    }               
}
