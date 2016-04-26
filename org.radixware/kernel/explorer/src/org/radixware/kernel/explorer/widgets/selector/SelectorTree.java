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
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QTreeView;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.radixware.kernel.common.client.Clipboard;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
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
import org.radixware.kernel.common.client.widgets.selector.SelectorModelDataLoader;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;

import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.selector.Selector;
import org.radixware.kernel.explorer.widgets.FilteredMouseEvent;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditor;

public class SelectorTree extends QTreeView implements IExplorerSelectorWidget {

    private final static String ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH =
            SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.ROWS_LIMIT_FOR_NAVIGATION;
    private final static String ROWS_LIMIT_FOR_RESTORING_POSITION_CONFIG_PATH =
            SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.ROWS_LIMIT_FOR_RESTORING_POSITION;

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

    private final static class ReadMoreEvent extends QEvent {

        private QModelIndex parent;
        private boolean restored;
        public final boolean collapseOnFail;
        public final boolean checkForRowsLimit;
        public final String parentIndexPath;

        public ReadMoreEvent(final String parentPath, final boolean collapseOnFail, final boolean checkForRowsLimit) {
            super(QEvent.Type.User);
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

    private final static class GetChildGroupEvent extends QEvent {

        public final QModelIndex index;
        public final int start, end;

        public GetChildGroupEvent(final QModelIndex index, final int start, final int end) {
            super(QEvent.Type.User);
            this.index = index;
            this.start = start;
            this.end = end;
        }
    };

    private final static class LayoutItemsEvent extends QEvent {

        public LayoutItemsEvent() {
            super(QEvent.Type.User);
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

            findAction = createAction(Icons.FIND, mp.translate("Selector", "Find..."), null);
            findAction.triggered.connect(SelectorTree.this.controller, "showFindDialog()");

            findNextAction = createAction(Icons.FIND_NEXT, mp.translate("Selector", "Find Next"), null);
            findNextAction.triggered.connect(SelectorTree.this.controller, "findNext()");

            nextAction = createAction(Icons.NEXT, mp.translate("Selector", "Next"), "selectNextRow()");

            prevAction = createAction(Icons.PREVIOUS, mp.translate("Selector", "Previous"), "selectPrevRow()");

            beginAction = createAction(Icons.BEGIN, mp.translate("Selector", "First"), "selectFirstRow()");

            endAction = createAction(Icons.END, mp.translate("Selector", "Last"), "selectLastRow()");

            createChildAction = createAction(Icons.CREATE_CHILD, mp.translate("Selector", "Create Child Object..."), "createChildEntity()");

            createSiblingAction = createAction(Icons.CREATE, mp.translate("Selector", "Create Object..."), null);

            pasteChildAction = createAction(Icons.PASTE_CHILD, mp.translate("Selector", "Paste Child Object..."), "pasteChildEntity()");

            pasteSiblingAction = createAction(Icons.PASTE, mp.translate("Selector", "Paste Object..."), null);
        }

        private ExplorerAction createAction(final ClientIcon icon, final String title, final String slot) {
            final ExplorerAction action =
                    new ExplorerAction(ExplorerIcon.getQIcon(icon), title, SelectorTree.this);
            WidgetUtils.updateActionToolTip(getEnvironment(), action);
            if (slot != null) {
                action.triggered.connect(SelectorTree.this, slot);
            }
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
    
    private class FilteredMouseEventListener extends QEventFilter{
        
        public FilteredMouseEventListener(final QObject parent){
            super(parent);
            setProcessableEventTypes(EnumSet.of(QEvent.Type.User));
        }
        
        @Override
        public boolean eventFilter(final QObject target, final QEvent event) {
            if (event instanceof FilteredMouseEvent) {
                final QEvent.Type type = ((FilteredMouseEvent) event).getFilteredEventType();
                if (type == QEvent.Type.MouseButtonPress) {
                    scheduledClick = true;
                } else if (type == QEvent.Type.MouseButtonDblClick && !scheduledClick) {
                    scheduledDoubleClick = true;
                }
            }
            return false;
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
    private boolean scheduledDoubleClick;
    private boolean scheduledClick;
    private boolean blockCustomEvents;
    private SelectorHorizontalHeader header;
    private final FilteredMouseEventListener filteredMouseEventListener = new FilteredMouseEventListener(this);
    private final List<String> scheduledRead = new ArrayList<>();
    private final static int DEFAULT_HEIGHT = 260;

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
        if (!controller.isLocked()) {
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
        } else {
            super.timerEvent(event);
        }
    }

    private IClientEnvironment getEnvironment() {
        return selector.getEnvironment();
    }

    @Override
    public boolean eventFilter(final QObject target, final QEvent event) {
        if (event instanceof FilteredMouseEvent) {
            final QEvent.Type type = ((FilteredMouseEvent) event).getFilteredEventType();
            if (type == QEvent.Type.MouseButtonPress) {
                scheduledClick = true;
            } else if (type == QEvent.Type.MouseButtonDblClick && !scheduledClick) {
                scheduledDoubleClick = true;
            }
        }
        return false;
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof FilteredMouseEvent) {
            event.accept();
            final QEvent.Type type = ((FilteredMouseEvent) event).getFilteredEventType();
            if (type == QEvent.Type.MouseButtonPress) {
                scheduledClick = true;
            } else if (type == QEvent.Type.MouseButtonDblClick && !scheduledClick) {
                scheduledDoubleClick = true;
            }
        }
        if (blockCustomEvents || !isEnabled() || model() == null) {
            event.ignore();
            if (event instanceof ReadMoreEvent) {
                final ReadMoreEvent readEvent = (ReadMoreEvent) event;
                scheduledRead.remove(readEvent.parentIndexPath);
            } else if (event instanceof LayoutItemsEvent) {
                Application.processEventWhenEasSessionReady(this, new LayoutItemsEvent());
            }
            return;
        }
        if (controller.isLocked()) {
            event.ignore();
            if (event instanceof ReadMoreEvent) {
                final ReadMoreEvent readEvent = (ReadMoreEvent) event;
                Application.processEventWhenEasSessionReady(this, new ReadMoreEvent(readEvent));
            } else if (event instanceof LayoutItemsEvent) {
                Application.processEventWhenEasSessionReady(this, new LayoutItemsEvent());
            }
            return;
        }
        if (event.type() == QEvent.Type.User && event instanceof ReadMoreEvent) {
            final ReadMoreEvent readEvent = (ReadMoreEvent) event;
            //lockInput();
            try {
                blockCustomEvents = true;
                event.accept();
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
                    actions.refresh();//moveNext
                }
            } catch (InterruptedException exception) {
            } catch (ServiceClientException exception) {
                controller.processErrorOnReceivingData(exception);
            } catch (Exception exception) {
                controller.processErrorOnReceivingData(exception);
            } finally {
                //unlockInput();
                scheduledRead.remove(readEvent.parentIndexPath);
                blockCustomEvents = false;
            }
        } else if (event.type() == QEvent.Type.User && event instanceof GetChildGroupEvent) {
            final GetChildGroupEvent getChildEvent = (GetChildGroupEvent) event;
            try {
                blockCustomEvents = true;
                event.accept();
                for (int i = getChildEvent.start; i <= getChildEvent.end; i++) {
                    controller.model.getChildGroup(controller.model.index(i, 0, getChildEvent.index));
                }
            } catch (Throwable exception) {
                if (ClientException.isSystemFault(exception)) {
                    getEnvironment().processException(exception);
                } else {
                    final String error = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), exception) + "\n" + ClientException.exceptionStackToString(exception);
                    getEnvironment().getTracer().debug(error);
                }
            } finally {
                blockCustomEvents = false;
            }
        } else if (event.type() == QEvent.Type.User && event instanceof LayoutItemsEvent) {
            event.accept();
            if (controller.model.isLocked()) {
                Application.processEventWhenEasSessionReady(this, new LayoutItemsEvent());
            } else {
                super.doItemsLayout();
            }
        }
    }

    public SelectorTree(final Selector parentView, final SelectorModel model) {
        super(parentView);

        this.selector = parentView;
        final IClientEnvironment environment = selector.getEnvironment();
        controller = new StandardSelectorWidgetController(model, this, selector);
        header = new SelectorHorizontalHeader(this, model);
        setHeader(header);

        final MessageProvider messageProvider = environment.getMessageProvider();
        final String confirmMovingToLastObjectMessage =
                messageProvider.translate("Selector", "Number of loaded objects is %1s.\nDo you want to load next %2s objects?");
        allDataLoader = new SelectorModelDataLoader(environment, selector);
        
        allDataLoader.setConfirmationMessageText(confirmMovingToLastObjectMessage);
        allDataLoader.setProgressHeader(messageProvider.translate("Selector", "Moving to Last Object"));
        allDataLoader.setProgressTitleTemplate(messageProvider.translate("Selector", "Moving to Last Object...\nNumber of Loaded Objects: %1s"));
        allDataLoader.setDontAskButtonText(messageProvider.translate("Selector", "Load All Objects"));

        rereadProgressHandle = environment.getProgressHandleManager().newStandardProgressHandle();
        final String confirmRestoringPositionMessage =
                messageProvider.translate("Selector", "Number of loaded objects is %1s.\nDo you want to continue restoring position?");
        currentObjectFinder = new SelectorModelDataLoader(environment,  selector);
        
        currentObjectFinder.setConfirmationMessageText(confirmRestoringPositionMessage);
        restoringPositionMessageTemplate = messageProvider.translate("Selector", "Restoring Position...\nNumber of Loaded Objects: %1s");
        restoringExpandedItemsMessageTemplate = messageProvider.translate("Selector", "Restoring Expanded Items...\nNumber of Loaded Objects: %1s");
        currentObjectFinder.setDontAskButtonText(messageProvider.translate("Selector", "Load All Required Objects"));
        currentObjectFinder.setProgressHandle(rereadProgressHandle);
        currentObjectFinder.setStartProgress(false);

        expandedObjectFinder = new SelectorModelDataLoader(environment,  selector);
        expandedObjectFinder.setConfirmationMessageText(confirmRestoringPositionMessage);
        expandedObjectFinder.setProgressTitleTemplate(restoringExpandedItemsMessageTemplate);
        expandedObjectFinder.setLoadingLimit(-1);
        expandedObjectFinder.setProgressHandle(rereadProgressHandle);
        expandedObjectFinder.setStartProgress(false);

        actions = new Actions();
    }

    @Override
    public void bind() {
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

        setModel(controller.model);

        if (selector.isDisabled()) {
            clear();
        } else {
            selectFirstRow();
        }

        final List<SelectorColumnModelItem> columns = controller.model.getSelectorColumns();
        controller.restoreHorizontalHeaderSettings(header);
        if (header.count() != columns.size()) {
            final SelectorHorizontalHeader previous = header;
            header = new SelectorHorizontalHeader(this, controller.model);
            setHeader(header);
            previous.close();
        }
        controller.setupHorizontalHeader(header,false);
        header.sectionClicked.connect(this, "onHeaderClicked(int)");

        {
            for (int i = 0; i < columns.size(); ++i) {
                if (i > 0) {
                    setColumnHidden(i, !columns.get(i).isVisible());
                } else {
                    final SelectorColumnModelItem firstColumn = columns.get(0);
                    if (firstColumn.isForbidden()) {
                        firstColumn.setForbidden(false);
                    }
                    if (!firstColumn.isVisible()) {
                        firstColumn.setVisible(true);
                    }
                    setColumnHidden(0, false);
                }
            }

            for (int i = 0; i < columns.size(); ++i) {
                columns.get(i).subscribe(this);
            }
        }

        controller.updateColumnsSizePolicy(header);
        controller.updateFirstVisibleColumnIndex(header);
        updateSpan(null, 0, controller.model.getRootGroupModel().getEntitiesCount() - 1);
        doubleClicked.connect(controller, "processDoubleClick(QModelIndex)");
        collapsed.connect(this, "onCollapsed(QModelIndex)");
        expanded.connect(this, "onExpanded(QModelIndex)");
        Application.getInstance().getActions().settingsChanged.connect(this, "applySettings()");
        header.sectionResized.connect(this, "finishEdit()");
        header.sectionMoved.connect(this, "onSectionMoved(int, int, int)");

        final QWidget viewport = (QWidget) findChild(QWidget.class, "qt_scrollarea_viewport");
        if (viewport != null) {//RADIX-7253
            viewport.installEventFilter(filteredMouseEventListener);
        }
    }

    @SuppressWarnings("unused")
    private void onHeaderClicked(final int section) {
        if (QApplication.keyboardModifiers().isSet(Qt.KeyboardModifier.ControlModifier) && section >= 0) {
            resizeColumnToContents(section);
        } else {
            controller.processColumnHeaderClicked(section);
        }
    }

    @Override
    public void refresh(final ModelItem item) {
        final SelectorModel model = (SelectorModel) model();
        if (model != null) {
            if (item instanceof SelectorColumnModelItem) {
                final int idx = model.getSelectorColumns().indexOf(item);
                if (idx > 0) {
                    boolean columnVisibleChanged = false;
                    final SelectorColumnModelItem column = model.getSelectorColumns().get(idx);
                    if (column.isVisible() == header.isSectionHidden(idx)) {
                        setColumnHidden(idx, !column.isVisible());
                        columnVisibleChanged = true;
                        controller.updateFirstVisibleColumnIndex(header);
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
                    if (!header.isSectionHidden(index.column())
                            && header.resizeMode(index.column()) == QHeaderView.ResizeMode.ResizeToContents) {
                        resizeColumnToContents(index.column());
                    }
                }
            }
            actions.refresh();
        }
    }

    private void updateSpan(final QModelIndex parentIndex, final int startRow, final int endRow) {
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
            if (target != null && selector.leaveCurrentEntity(false)) {
                controller.enterEntity(target);
            }
        }
    }

    public void selectPrevRow() {
        if (currentIndex() != null) {
            final QModelIndex target = indexAbove(currentIndex());
            if (target != null && selector.leaveCurrentEntity(false)) {
                controller.enterEntity(target);
            }
        }
    }

    public void selectFirstRow() {
        if (model().rowCount() > 0) {
            final int column = currentIndex() != null ? currentIndex().column() : 0;
            if (selector.leaveCurrentEntity(false)) {
                controller.enterEntity(model().index(0, column));
            }
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
            final int column = currentIndex() != null ? currentIndex().column() : 0;
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
                        if (compareIndexes(expandedItem, target) < 0) {
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
                if (target != null && selector.leaveCurrentEntity(false)) {
                    if (column == 0) {
                        controller.enterEntity(target);
                    } else {
                        controller.enterEntity(model().index(target.row(), column, target.parent()));
                    }
                }
            } finally {
                if (loadedRows > 0) {
                    controller.model.increaseRowsLimit();
                }
            }
        }
    }

    public EntityModel createChildEntity() {
        final SelectorModel model = controller.model;
        final QModelIndex current = currentIndex();
        if (current != null) {
            try {
                final GroupModel group = model.getChildGroup(current);
                if (group != null && selector.canChangeCurrentEntity(false)) {
                    final EntityModel result = SelectorController.createEntity(group, null, this);
                    if (result != null) {
                        if (group.getSelectorPresentationDef().isRestoringPositionEnabled()) {
                            reread(currentIndex(), result.getPid());
                        } else {
                            reread(currentIndex(), null);
                        }
                        selector.notifyEntityObjectsCreated(Collections.singletonList(result));
                    }
                    return result;
                }
            } catch (InterruptedException e) {
            } catch (Exception ex) {
                selector.getModel().showException(getEnvironment().getMessageProvider().translate("Selector", "Failed to create object"), ex);
            }
        }
        return null;
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
                        reread(currentIndex(), null);
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
            //Обновление индексов раскрытых элементов
            final List<QModelIndex> newExpanded = new ArrayList<>(expandedItems.size());
            QModelIndex expandedItem;
            for (int i = expandedItems.size() - 1; i >= 0; i--) {
                expandedItem = expandedItems.get(i);
                if (indexesEqual(expandedItem, index)) {
                    expandedItems.remove(i);
                } else if (indexesEqual(expandedItem.parent(), parentIndex) && expandedItem.row() > removedRow) {
                    expandedItems.remove(i);
                    newExpanded.add(model().index(expandedItem.row() - 1, 0, parentIndex));
                }
            }            
            actions.blockRefresh();
            try{//Удаление строки
                controller.processEntityRemoved(pid);
            }finally{
                actions.unblockRefresh();
            }
            actions.refresh();
            expandedItems.addAll(newExpanded);
            controller.lockInput();
            try {
                updateSpan(parentIndex, removedRow, controller.model.rowCount(parentIndex) - 1);
            } finally {
                controller.unlockInput();
            }
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
        scheduledDoubleClick = false;
        scheduledClick = false;
        if (!controller.processMousePressEvent(event)) {
            final QModelIndex index = indexAt(event.pos());
            final boolean insideCell = visualRect(index).contains(event.x(), event.y());//Курсор внутри ячейки а не на "плюсике"
            final boolean changingIndex =
                    index != null
                    && !index.equals(currentIndex());
            final boolean editorOpened = controller.getCurrentPropEditor() != null;
            if (event.isAccepted()) {
                super.mousePressEvent(event);
            }
            if (insideCell && (changingIndex || !editorOpened)) {
                openEditor(index);
            } else if (changingIndex) {
                repaint(visualRect(index));
            }
        } else if (event.isAccepted()) {
            super.mousePressEvent(event);
        }
        if (scheduledDoubleClick && model() != null) {
            scheduledDoubleClick = false;
            final QModelIndex index = currentIndex();
            if (index != null) {
                if (model().flags(index).isSet(Qt.ItemFlag.ItemIsEditable)) {
                    if (controller.getCurrentPropEditor() == null) {
                        openEditor(index);
                    }
                } else {
                    setExpanded(index, !isExpanded(index));
                }
            }
        }
    }

    @Override
    protected void mouseDoubleClickEvent(QMouseEvent qme) {
        scheduledDoubleClick = false;
        scheduledClick = false;
        super.mouseDoubleClickEvent(qme);
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
    protected QModelIndex moveCursor(final CursorAction action, final KeyboardModifiers mod) {
        if (controller.canMoveCursor(action, state())) {
            final QModelIndex index = super.moveCursor(action, mod);
            return controller.afterMoveCursor(index);
        }
        return null;
    }

    @Override
    protected void currentChanged(final QModelIndex current, final QModelIndex previous) {        
        finishEdit();
        controller.processCurrentChanged(current, previous, header());
        actions.refresh();
        update(current);//Чтобы отрисовалась рамка текущей ячейки
    }
    
    private final List<QModelIndex> expandedItems = new ArrayList<>();

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
        expandedItems.remove(index);
        actions.refresh();//moveNext
    }

    @Override
    public void rereadAndSetCurrent(final Pid pid) throws InterruptedException, ServiceClientException {
        final QModelIndex current = currentIndex();
        if (pid == null || current == null) {
            reread(null, null);
        } else {
            reread(current.parent(), pid);
        }
    }

    @Override
    public void reread() throws InterruptedException, ServiceClientException {
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
            reread(current, null);
        }
    }
    
    private int getFirstVisibleColumn(){
        final int columnsCount = header().count();
        int idx;
        for (int col = 0; col <= columnsCount; col++) {
            idx = header().logicalIndex(col);//logical index of column
            if (idx >= 0 && !header().isSectionHidden(idx)) {
                return idx;
            }
        }
        return 0;
    }    

    public void reread(final QModelIndex parent, final Pid pid) throws InterruptedException, ServiceClientException {
        final SelectorModel model = (SelectorModel) model();
        model.unsubscribeProperties(this);

        //сохранили путь до текущего элемента
        final QModelIndex current = currentIndex();
        final int column = current==null ? getFirstVisibleColumn() : current.column();
        final Stack<Pid> path = new Stack<>();
        if (pid != null) {
            path.push(pid);
        } else if (parent == null) {
            indexToPath(path, current);
        }

        if (parent != null) {
            indexToPath(path, parent);
        }

        final List<Stack<Pid>> itemsToExpand = new ArrayList<>();//раскрытые элементы, которые находятся ниже текущего

        {//Сохранение раскрытых элементов
            QModelIndex index;
            for (int i = expandedItems.size() - 1; i >= 0; i--) {
                index = expandedItems.get(i);
                if (WidgetUtils.isParentIndex(parent, index)) {
                    itemsToExpand.add(indexToPath(new Stack<Pid>(), index));
                }
                if (index.equals(currentForFetch)) {
                    currentForFetch = currentForFetch.parent();
                }
                expandedItems.remove(i);
            }
        }
        final int rowsLoadingLimit =
                selector.getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_FOR_RESTORING_POSITION_CONFIG_PATH, 300);
        selector.blockRedraw();
        rereadProgressHandle.startProgress(null, true);
        try {
            lockInput();
            try {
                if (isExpanded(parent)) {
                    collapse(parent);
                }
                if (path.isEmpty()) {
                    //перечитывание
                    model.reread(parent);
                } else {
                    //очистка загруженных данных
                    model.reset(parent);
                    //загрузка первой порции новых данных
                    currentObjectFinder.setLoadingLimit(rowsLoadingLimit);
                    currentObjectFinder.setProgressTitleTemplate(restoringPositionMessageTemplate);
                    currentObjectFinder.resetCounters();
                    currentObjectFinder.loadMore(new SelectorWidgetDelegate(model, parent));
                }
            } finally {
                unlockInput();
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

            if (needToRestartProgress) {
                rereadProgressHandle.startProgress(null, true);
            }
            if (rereadProgressHandle.getMaximumValue() > 0) {
                rereadProgressHandle.setMaximumValue(0);
                rereadProgressHandle.setValue(-1);
            }
            final QModelIndex currentItem;
            {//восстанавление текущего элемента
                //objectFinder.setProgressHeader(selector.getEnvironment().getMessageProvider().translate("Selector", "Restoring Position"));
                final QModelIndex index = path.isEmpty() ? null : pathToIndex(path, null, true, currentObjectFinder);
                if (index != null) {
                    currentItem = index;
                } else if (model.rowCount(parent) > 0) {
                    currentItem = model.index(0, 0, parent);
                } else if (parent != null) {
                    currentItem = parent;
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
                        if (compareIndexes(expandedItem, currentItem) < 0) {
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
            rereadProgressHandle.finishProgress();
            selector.unblockRedraw();
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
        ((SelectorModel) model()).clear();
        actions.refresh();
        update();
        setEnabled(false);
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
        QCoreApplication.postEvent(this, new GetChildGroupEvent(parent, start, end));
        finishEdit();
        super.rowsInserted(parent, start, end);
        updateSpan(parent, start, end);
    }

    //Блокировка контекстного поиска
    @Override
    public void keyboardSearch(String search) {
        controller.processKeyboardSearch();
    }

    public final void applySettings() {
        controller.applySettings();
        update();
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
        controller.model.unsubscribeProperties(this);

        {//disconnecxting signals for GC
            actions.close();
            header().disconnect();
        }

        if (model() != null) {
            if (selector.getModel() != null) {
                controller.saveHorizontalHeaderSettings(header());
            }
            setModel(null);
            controller.model.clear();
            controller.model.dispose();
        }
        header.close();
        expandedItems.clear();
        controller.close();
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
    protected boolean edit(QModelIndex index, EditTrigger trigger, QEvent event) {
        if (index != null && controller.getCurrentPropEditor() != null) {
            if (!index.equals(currentIndex())) {
                finishEdit();
            } else {
                return true;
            }
        }
        return super.edit(index, trigger, event);
    }

    @Override
    protected void keyPressEvent(QKeyEvent event) {
        if (!controller.processKeyPressEvent(event)) {

            if (event.matches(QKeySequence.StandardKey.Find)) {
                actions.findAction.trigger();
            } else if (event.matches(QKeySequence.StandardKey.FindNext)) {
                actions.findNextAction.trigger();
            } else {
                super.keyPressEvent(event);
            }

            if (controller.isEditEvent(event)) {
                openEditor(currentIndex());
            }
        }
    }

    private void openEditor(final QModelIndex index) {
        if (index == null || model() == null || !model().flags(index).isSet(Qt.ItemFlag.ItemIsEditable)) {
            return;
        }
        final Property property = (Property) model().data(index, Qt.ItemDataRole.UserRole);
        if (property != null && property.getDefinition().getType() != EValType.BOOL) {
            edit(index);
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
    protected void drawRow(QPainter painter, QStyleOptionViewItem options, QModelIndex index) {
        super.drawRow(painter, options, index);
        if (!selector.isInternalPaintingActive()) {
            final SelectorModel model = controller.model;
            final String indexPath = indexToPath(index).toString();
            if (isExpanded(index) && model.rowCount(index) == 0 && !scheduledRead.contains(indexPath)) {
                Application.processEventWhenEasSessionReady(this, new ReadMoreEvent(indexPath, true, true));
                scheduledRead.add(indexPath);

                if (safelyCheckIfCanReadMore(index)) {
                    currentForFetch = index;
                }
            }

            for (QModelIndex idx = index; idx != null; idx = idx.parent()) {
                if (isLastChild(idx) && model.canReadMore(idx.parent()) && !scheduledRead.contains(indexPath)) {
                    final String idxPath = indexToPath(idx.parent()).toString();
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
            return;
        }
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
        super.scrollContentsBy(deltaX, deltaY);
    }

    @Override
    public QSize sizeHint() {
        this.indexRowSizeHint(currentForFetch);
        final QSize size = super.sizeHint();
        size.setHeight(DEFAULT_HEIGHT);
        final int width = header.sizeHint().width()+frameWidth()*2;
        if (size.width()<width){
            final Dimension sizeLimit = WidgetUtils.getWndowMaxSize();
            size.setWidth(Math.min(width, (int)sizeLimit.getWidth()));
        }
        return size;        
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
        final SelectorModelDataLoader dataLoader = new SelectorModelDataLoader(getEnvironment(), this.selector);
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

    private QModelIndex pathToIndex(final Stack<Pid> path, final QModelIndex startWith, final boolean nearest, final SelectorModelDataLoader dataLoader) throws InterruptedException {
        final SelectorModel model = (SelectorModel) model();
        QModelIndex index = startWith;
        int row;
        lockInput();
        try {
            while (!path.isEmpty()) {
                if (index != null) {
                    expand(index);
                }

                try {
                    if (model.hasChildren(index) && model.rowCount(index) == 0 && !dataLoader.loadMore(new SelectorWidgetDelegate(model, index))) {
                        return nearest ? index : null;
                    }
                } catch (ServiceClientException exception) {
                    controller.processErrorOnReceivingData(exception);
                    return nearest ? index : null;
                }

                try {
                    row = dataLoader.findObjectByPid(new SelectorWidgetDelegate(model, index), path.pop());
                } catch (ServiceClientException exception) {
                    controller.processErrorOnReceivingData(exception);
                    row = -1;
                }
                if (row >= 0) {
                    index = model.index(row, 0, index);
                } else if (nearest) {
                    if (model.rowCount(index) > 0) {
                        return model.index(model.rowCount(index) - 1, 0, index);
                    } else {
                        return index;
                    }
                } else {
                    return null;
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
            Application.processEventWhenEasSessionReady(this, new LayoutItemsEvent());
        } else {
            super.doItemsLayout();
            if (controller != null && model() != null && controller.model.isLocked()) {
                //Когда модель селектора заблокирована метод hasChildren может вернуть неправильный результат, который кэшируется.
                //Чтобы обновить кэш планируем повторный вызов метода doItemsLayout
                Application.processEventWhenEasSessionReady(this, new LayoutItemsEvent());
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
}
