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
package org.radixware.kernel.explorer.editors;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemDelegate;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QStyledItemDelegate;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QFormLayout;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QSpinBox;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.traceprofile.ITraceProfileEditor;
import org.radixware.kernel.common.client.editors.traceprofile.ITraceProfileEventSourceOptionsEditor;
import org.radixware.kernel.common.client.editors.traceprofile.ITraceProfileTreePresenter;
import org.radixware.kernel.common.client.editors.traceprofile.TraceProfileTreeController;
import org.radixware.kernel.common.client.editors.traceprofile.TraceProfileTreeNode;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.trace.RadixTraceOptions;
import org.radixware.kernel.common.trace.TraceProfile;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;

/**
 * Редактор профиля трассировки (Radix::Explorer.Widgets::TraceProfileEditor).
 * Реализация интерфейса {@link ITraceProfileEditor}, в окружении Explorer с
 * использованием инстанции {@link TraceProfileTreeController}.
 *
 * @see ITraceProfileEditor
 */
public final class TraceProfileEditor extends ExplorerWidget implements ITraceProfileEditor {

    private final static class ProfileTreeItem extends QTreeWidgetItem implements IWidget {

        private static final QBrush GRAY = new QBrush(QColor.gray);
        private static final QBrush BLACK = new QBrush(QColor.black);

        private String objectName;

        public ProfileTreeItem() {
            super();
        }

        public ProfileTreeItem(final ProfileTreeItem parent) {
            super(parent);
        }

        private ProfileTree getProfileTree() {
            return (ProfileTree) treeWidget();
        }

        public void update(final TraceProfileTreeNode<ProfileTreeItem> node, 
                                    final String inheritanceTitle, 
                                    final QSize iconSize) {

            setText(0, node.getTitle());
            setToolTip(node.getEventSource());
            final TraceProfileTreeController.EventSeverity eventSeverity = node.getEventSeverity();
            final QIcon eventSeverityIcon = ExplorerIcon.getQIcon(eventSeverity.getIcon());
            if (node.eventSeverityWasInherited()) {
                setText(1, inheritanceTitle);
                setIcon(1, getDisabledIcon(eventSeverity.getValue(), eventSeverityIcon, iconSize));
                setData(1, Qt.ItemDataRole.UserRole, null);
                setForeground(1, GRAY);
            } else {
                setText(1, eventSeverity.getTitle());
                setData(1, Qt.ItemDataRole.UserRole, eventSeverity.getValue());
                setIcon(1, eventSeverityIcon);
                setForeground(1, BLACK);
            }            
            if (TraceProfileTreeController.eventSourceHasExtOptions(node.getEventSource())) {
                if (node.eventSeverityWasInherited() || !EEventSeverity.DEBUG.getName().equals(node.getEventSeverity().getValue())) {
                    setForeground(2, GRAY);
                    setText(2, getProfileTree().messageProvider.translate("TraceDialog", "<set 'Debug' level explicitly to define>"));
                    setData(2, Qt.ItemDataRole.UserRole, "");
                } else if (node.getOptions() == null || node.getOptions().toString().isEmpty()) {
                    setForeground(2, GRAY);
                    setText(2, getProfileTree().messageProvider.translate("TraceDialog", "<double-click to edit>"));
                    setData(2, Qt.ItemDataRole.UserRole, "");
                } else {
                    setForeground(2, BLACK);
                    setText(2, node.getOptions().toString());
                    setData(2, Qt.ItemDataRole.UserRole, node.getOptions().toString());
                }
            } else {
                setText(2, "");
                setData(2, Qt.ItemDataRole.UserRole, TraceProfileTreeController.OPTOINS_ARE_UNSUPPORTED);
            }
            if (node.isReadOnly()) {
                setFlags(Qt.ItemFlag.ItemIsSelectable);
            } else {
                setFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEditable);
            }
        }

        @Override
        public Object findChild(final Class<?> childClass, final String childObjectName) {
            final Stack<ProfileTreeItem> stack = new Stack<>();
            ProfileTreeItem item, childItem;
            stack.add(this);
            while (!stack.isEmpty()) {
                item = stack.pop();
                for (int i = 0, count = item.childCount(); i < count; i++) {
                    childItem = (ProfileTreeItem) item.child(i);
                    if (Objects.equals(childItem.getObjectName(), childObjectName)) {
                        return childItem;
                    } else {
                        stack.push(childItem);
                    }
                }
            }
            return null;
        }

        @Override
        public int width() {
            return sizeHint(0).width() + sizeHint(1).width();
        }

        @Override
        public int height() {
            return sizeHint(0).height();
        }

        @Override
        public void setToolTip(final String toolTipText) {
            setToolTip(0, toolTipText);
        }

        @Override
        public void setEnabled(final boolean enabled) {
            final Qt.ItemFlags flags = new Qt.ItemFlags(flags().value());
            if (enabled) {
                flags.set(Qt.ItemFlag.ItemIsEnabled);
            } else {
                flags.clear(Qt.ItemFlag.ItemIsEnabled);
            }
            setFlags(flags);
        }

        @Override
        public boolean isEnabled() {
            return flags().isSet(Qt.ItemFlag.ItemIsEnabled);
        }

        @Override
        public boolean isDisposed() {
            return nativeId() == 0;
        }

        @Override
        public String getObjectName() {
            return objectName;
        }

        @Override
        public void setObjectName(final String name) {
            objectName = name;
        }

        @Override
        public boolean isVisible() {
            return !isHidden();
        }

        @Override
        public void setVisible(final boolean visible) {
            setHidden(!visible);
        }

        @Override
        public IPeriodicalTask startTimer(TimerEventHandler handler) {
            throw new UnsupportedOperationException("startTimer is not supported.");
        }

        @Override
        public void killTimer(IPeriodicalTask task) {
            throw new UnsupportedOperationException("killTimer is not supported yet.");
        }
    }

    private final static class ProfileTreeItemDelegate extends QItemDelegate {

        private static class ShowPopupEvent extends QEvent {

            public ShowPopupEvent() {
                super(QEvent.Type.User);
            }
        }

        private static class EventSeverityEditor extends QComboBox {

            private boolean popupShown;

            public EventSeverityEditor(final QWidget parent) {
                super(parent);
                setFrame(false);
            }

            @Override
            protected void customEvent(final QEvent qevent) {
                if (qevent instanceof ShowPopupEvent) {
                    qevent.accept();
                    if (isVisible()) {
                        popupShown = true;
                        showPopup();
                    }
                }
                super.customEvent(qevent);
            }

            @Override
            public void hidePopup() {
                if (popupShown) {
                    popupShown = false;
                } else {
                    super.hidePopup();
                }
            }
        }
        private final List<TraceProfileTreeController.EventSeverity> eventSeverityList;
        private final String inheritedEventSeverityTitle;
        public final Signal3<String, String, String> eventSeverityChanged = new Signal3<>();

        public ProfileTreeItemDelegate(final QObject parent, final List<TraceProfileTreeController.EventSeverity> eventSeverityList, final MessageProvider messageProvider) {
            super(parent);
            this.eventSeverityList = eventSeverityList;
            inheritedEventSeverityTitle = messageProvider.translate("TraceDialog", "Inherit");
        }

        @Override
        public QWidget createEditor(final QWidget parent, final QStyleOptionViewItem options, final QModelIndex index) {
            final QComboBox editor = new EventSeverityEditor(parent);
            for (QModelIndex parentIndex = index.parent(); parentIndex != null; parentIndex = parentIndex.parent()) {
                //find overrided event severity
                final Object data = parentIndex.sibling(parentIndex.row(), 1).data(Qt.ItemDataRole.UserRole);
                if (data != null) {
                    final QIcon regularIcon
                            = (QIcon) parentIndex.sibling(parentIndex.row(), 1).data(Qt.ItemDataRole.DecorationPropertyRole);
                    final QIcon disabledIcon = getDisabledIcon(data, regularIcon, options.decorationSize());
                    editor.addItem(disabledIcon, inheritedEventSeverityTitle, null);
                    editor.setItemData(0, QColor.gray, Qt.ItemDataRole.ForegroundRole);
                    break;
                }
            }
            for (TraceProfileTreeController.EventSeverity eventSeverity : eventSeverityList) {
                final QIcon severityIcon = ExplorerIcon.getQIcon(eventSeverity.getIcon());
                editor.addItem(severityIcon, eventSeverity.getTitle(), eventSeverity.getValue());
            }
            return editor;
        }

        @Override
        public void setModelData(final QWidget editor, final QAbstractItemModel model, final QModelIndex index) {
            //do nothing here
        }

        @Override
        public void setEditorData(final QWidget editor, final QModelIndex index) {
            if (editor instanceof QComboBox) {
                final QComboBox comboBox = (QComboBox) editor;
                final int valueIndex
                        = comboBox.findData(index.data(Qt.ItemDataRole.UserRole), Qt.ItemDataRole.UserRole);
                comboBox.setCurrentIndex(valueIndex == -1 ? 0 : valueIndex);
                comboBox.currentIndexChanged.connect(this, "finishEdit()");
                QApplication.postEvent(comboBox, new ShowPopupEvent());
            }
        }

        @Override
        public void updateEditorGeometry(final QWidget editor, final QStyleOptionViewItem option, final QModelIndex index) {
            editor.setGeometry(option.rect());
        }

        @SuppressWarnings("unused")
        private void finishEdit() {
            final QSignalEmitter signalSender = QObject.signalSender();
            if (signalSender instanceof QComboBox && parent() instanceof ProfileTree) {
                final ProfileTree profileTree = (ProfileTree) parent();
                final QComboBox comboBox = (QComboBox) signalSender;
                final int valueIndex = comboBox.currentIndex();
                final QModelIndex index = profileTree.currentIndex();
                final String eventSource = (String) index.sibling(index.row(), 0).data(Qt.ItemDataRole.UserRole);
                final String eventSeverity = (String) comboBox.itemData(valueIndex);
                final String options = (String) index.sibling(index.row(), 2).data(Qt.ItemDataRole.UserRole);
                comboBox.disconnect();
                profileTree.closeEditor(comboBox);
                eventSeverityChanged.emit(eventSource, eventSeverity, options);
            }
        }
    }

    private final static class OptionsTreeItemDelegate extends QStyledItemDelegate {

        final ProfileTree profileTree;

        public OptionsTreeItemDelegate(final QObject parent, final ProfileTree profileTree) {
            super(parent);
            this.profileTree = profileTree;
        }

        @Override
        public QWidget createEditor(final QWidget parent, final QStyleOptionViewItem options, final QModelIndex index) {
            final Object optsStrObject = index.data(Qt.ItemDataRole.UserRole);
            if (TraceProfileTreeController.OPTOINS_ARE_UNSUPPORTED.equals(optsStrObject)) {
                return null;
            }
            final Object eventSourceObj = index.sibling(index.row(), 0).data(Qt.ItemDataRole.UserRole);
            if (eventSourceObj instanceof String){
                profileTree.getController().editExtOptions((String)eventSourceObj);
            }
            return null;
        }
        @Override
        public void setModelData(final QWidget editor, final QAbstractItemModel model, final QModelIndex index) {
        }

        @Override
        public void setEditorData(final QWidget editor, final QModelIndex index) {
        }

        @Override
        public void updateEditorGeometry(final QWidget editor, final QStyleOptionViewItem option, final QModelIndex index) {
            editor.setGeometry(option.rect());
        }
    }
    
    private final static class DbTraceExtOptions extends ExplorerDialog implements ITraceProfileEventSourceOptionsEditor{
        
        final QSpinBox spMinLogDbDuration = new QSpinBox(this);
        final QLineEdit leQryMask = new QLineEdit("", this);
        final QCheckBox cbLogPlan = new QCheckBox("", this);
        final QCheckBox cbLogStack = new QCheckBox("", this);
        TraceProfile.EventSourceOptions resultOptions;
        
        public DbTraceExtOptions(final TraceProfile.EventSourceOptions options, 
                                                final IClientEnvironment environment,
                                                final QWidget parent){
            super(environment, parent, false);            
            setupUI();
            readOptions(options);
            resultOptions = new TraceProfile.EventSourceOptions(options.toString());
        }
        
        private void setupUI(){
            final MessageProvider mp = getEnvironment().getMessageProvider();
            setWindowTitle(mp.translate("TraceDialog", "Edit Options"));
            final QFormLayout formLayout = new QFormLayout();

            spMinLogDbDuration.setMinimum(-1);
            spMinLogDbDuration.setMaximum(999999);
            formLayout.addRow(mp.translate("TraceDialog", "Minimum duration of loggable database operation (ms)"), spMinLogDbDuration);
                        
            leQryMask.setPlaceholderText("<*>");
            formLayout.addRow(mp.translate("TraceDialog", "Query mask (* - any characters)"), leQryMask);

            formLayout.addRow(mp.translate("TraceDialog", "Log plan"), cbLogPlan);
            formLayout.addRow(mp.translate("TraceDialog", "Log thread stack"), cbLogStack);
            
            dialogLayout().addLayout(formLayout);

            addButton(EDialogButtonType.OK).addClickHandler(new IButton.ClickHandler(){

                @Override
                public void onClick(final IButton source) {
                    resultOptions = writeOptions();
                    accept();
                }
                
            });
            addButtons(EnumSet.of(EDialogButtonType.CANCEL), true);
            
            setAutoSize(true);
            setDisposeAfterClose(true);
        }
        
        private void readOptions(final TraceProfile.EventSourceOptions opts){
            final Object curMinLoggableDbOpDurationMs = opts.getOption(RadixTraceOptions.MIN_LOGGABLE_DB_OPERATION_DURATION_MILLIS);
            spMinLogDbDuration.setValue(curMinLoggableDbOpDurationMs == null ? -1 : ((Long) curMinLoggableDbOpDurationMs).intValue());            
            final Object maskObj = opts.getOption(RadixTraceOptions.DB_QRY_MASK);
            leQryMask.setText(maskObj == null ? "" : maskObj.toString());
            cbLogPlan.setChecked(opts.hasOption(RadixTraceOptions.LOG_PLAN));
            cbLogStack.setChecked(opts.hasOption(RadixTraceOptions.LOG_STACK));
        }
        
        private TraceProfile.EventSourceOptions writeOptions(){
            final Map<String, Object> newOptMap = new HashMap<>();
            if (spMinLogDbDuration.value() >= 0) {
                newOptMap.put(RadixTraceOptions.MIN_LOGGABLE_DB_OPERATION_DURATION_MILLIS, spMinLogDbDuration.value());
            }
            if (cbLogPlan.isChecked()) {
                newOptMap.put(RadixTraceOptions.LOG_PLAN, null);
            }
            if (cbLogStack.isChecked()) {
                newOptMap.put(RadixTraceOptions.LOG_STACK, null);
            }
            if (leQryMask.text() != null && leQryMask.text().length() > 0) {
                newOptMap.put(RadixTraceOptions.DB_QRY_MASK, leQryMask.text());
            }
            return new TraceProfile.EventSourceOptions(newOptMap);
        }

        @Override
        public TraceProfile.EventSourceOptions getOptions() {
            return resultOptions;
        }
    }
    
    private final static class EasTraceExtOptions extends ExplorerDialog implements ITraceProfileEventSourceOptionsEditor{
        
        final QCheckBox cbLogPresentCalc = new QCheckBox(this);
        TraceProfile.EventSourceOptions resultOptions;
        
        public EasTraceExtOptions(final TraceProfile.EventSourceOptions options, 
                                                final IClientEnvironment environment,
                                                final QWidget parent){
            super(environment, parent, false);
            setupUI();
            cbLogPresentCalc.setChecked(options.hasOption(RadixTraceOptions.LOG_PRES_CALC));
            resultOptions = new TraceProfile.EventSourceOptions(options.toString());
        }
        
        private void setupUI(){
            final MessageProvider mp = getEnvironment().getMessageProvider();
            setWindowTitle(mp.translate("TraceDialog", "Edit Options"));
            cbLogPresentCalc.setText(mp.translate("TraceDialog", "Log calculation of editor presentation"));
            dialogLayout().addWidget(cbLogPresentCalc);
            addButton(EDialogButtonType.OK).addClickHandler(new IButton.ClickHandler(){
                @Override
                public void onClick(final IButton source) {
                    if (cbLogPresentCalc.isChecked()){
                        resultOptions = 
                            new TraceProfile.EventSourceOptions(Collections.singletonMap(RadixTraceOptions.LOG_PRES_CALC, null));
                    }else{
                        resultOptions = 
                            new TraceProfile.EventSourceOptions(Collections.<String,Object>emptyMap());
                    }
                    accept();
                }
            });
            
            addButtons(EnumSet.of(EDialogButtonType.CANCEL), true);
            
            setAutoSize(true);
            setDisposeAfterClose(true);            
        }

        @Override
        public TraceProfile.EventSourceOptions getOptions() {
            return resultOptions;
        }
        
    }

    private final static class ProfileTree extends QTreeWidget implements ITraceProfileTreePresenter<ProfileTreeItem> {

        private final QFont regularFont = font(), boldFont;
        private final String inheritanceTitle;
        private final MessageProvider messageProvider;
        private final static int DEFAULT_HEIGHT = 260;
        private final IClientEnvironment environment;
        private final TraceProfileEditor editor;

        public ProfileTree(final TraceProfileEditor editor, final IClientEnvironment environment) {
            super(editor);
            this.editor = editor;
            this.environment = environment;
            this.messageProvider = environment.getMessageProvider();
            regularFont.setBold(false);
            boldFont = ExplorerFont.Factory.getFont(regularFont).getBold().getQFont();
            setColumnCount(3);
            inheritanceTitle = messageProvider.translate("TraceDialog", "Inherit");
            final String eventSourceColumnTitle = messageProvider.translate("TraceDialog", "Event Source");
            final String eventSeverityColumnTitle = messageProvider.translate("TraceDialog", "Event Severity");
            final String optionsColumnTitle = messageProvider.translate("TraceDialog", "Options");
            setHeaderLabels(new LinkedList<>(Arrays.<String>asList(eventSourceColumnTitle, eventSeverityColumnTitle, optionsColumnTitle)));
            header().setDefaultAlignment(Qt.AlignmentFlag.AlignHCenter);
            header().setResizeMode(1, QHeaderView.ResizeMode.ResizeToContents);
            header().setResizeMode(1, QHeaderView.ResizeMode.ResizeToContents);
            header().setResizeMode(0, QHeaderView.ResizeMode.Stretch);
            header().setMovable(false);
            setUniformRowHeights(true);
            setSelectionBehavior(SelectionBehavior.SelectRows);
            setSelectionMode(SelectionMode.SingleSelection);

            setItemDelegateForColumn(0, new QStyledItemDelegate(this) {//restrict  editing
                @Override
                public QWidget createEditor(final QWidget parent, final QStyleOptionViewItem option, final QModelIndex index) {
                    return null;
                }
            });
            setItemDelegateForColumn(2, new OptionsTreeItemDelegate(editor, this));
        }

        public IClientEnvironment getEnvironment() {
            return environment;
        }
        
        public TraceProfileTreeController<ProfileTreeItem> getController(){
            return editor.getController();
        }

        @Override
        public ProfileTreeItem createTreeNodeWidget(final TraceProfileTreeNode<ProfileTreeItem> treeNode) {
            final ProfileTreeItem result;
            if (treeNode.getParentNode() == null) {
                result = new ProfileTreeItem();
                addTopLevelItem(result);
            } else {
                result = new ProfileTreeItem(treeNode.getParentNode().getWidget());
            }
            result.setData(0, Qt.ItemDataRole.UserRole, treeNode.getEventSource());
            result.setData(2, Qt.ItemDataRole.UserRole, treeNode.getOptions().toString());
            if (treeNode.getEventSeverity() != null) {
                result.update(treeNode, inheritanceTitle, getIconSize());
            }
            return result;
        }

        @Override
        public void destroyPresentations() {
            this.takeTopLevelItem(0);
        }

        @Override
        public void presentWidget(final TraceProfileTreeNode<ProfileTreeItem> childNode) {
            final ProfileTreeItem treeItem = childNode.getWidget();
            final boolean needForBoldFont
                    = (!childNode.eventSeverityWasInherited() || childNode.hasChildWithSpecifiedEventSeverity()) && !childNode.isReadOnly();
            treeItem.update(childNode, inheritanceTitle, getIconSize());
            if (needForBoldFont && treeItem.font(0) != boldFont) {
                treeItem.setFont(0, boldFont);
            } else if (!needForBoldFont && treeItem.font(0) != regularFont) {
                treeItem.setFont(0, regularFont);
            }
        }

        public void closeEditor(final QWidget editor) {
            super.closeEditor(editor, QAbstractItemDelegate.EndEditHint.SubmitModelCache);
        }

        private QSize getIconSize() {
            return viewOptions().decorationSize();
        }

        @Override
        public QSize sizeHint() {
            final QSize size = super.sizeHint();
            size.setHeight(DEFAULT_HEIGHT);
            size.setWidth(header().length() + 100 + frameWidth() * 2);
            return size;
        }

        @Override
        public QSize minimumSizeHint() {
            final QSize size = super.minimumSizeHint();
            size.setHeight(DEFAULT_HEIGHT);
            size.setWidth(header().length() + frameWidth() * 2);
            return size;
        }

        @Override
        public ITraceProfileEventSourceOptionsEditor createEventSourceOptionsEditor(final EEventSource eventSource, 
                                                                                                                             final EEventSeverity eventSeverity, 
                                                                                                                             final TraceProfile.EventSourceOptions options) {
            if (eventSeverity==EEventSeverity.DEBUG){
                switch(eventSource){
                    case ARTE_DB:
                        return new DbTraceExtOptions(options, environment, this);
                    case EAS:
                        return new EasTraceExtOptions(options, environment, this);
                    default:
                        return null;
                }
            }else{
                return null;
            }
        }       
    }

    private final ProfileTree profileTree;
    private final TraceProfileTreeController<ProfileTreeItem> controller;
    private static final QAbstractItemView.EditTriggers EDIT_TRIGGERS
            = new QAbstractItemView.EditTriggers(QAbstractItemView.EditTrigger.DoubleClicked,
                    QAbstractItemView.EditTrigger.SelectedClicked);
    private static final Map<Object, QIcon> DISABLED_ICONS_CACHE = new HashMap<>(8);

    private static QIcon getDisabledIcon(final Object key, final QIcon regularIcon, final QSize iconSize) {
        QIcon result = DISABLED_ICONS_CACHE.get(key);
        if (result == null && regularIcon != null) {
            result = new QIcon(regularIcon.pixmap(iconSize, QIcon.Mode.Disabled, QIcon.State.On));
            DISABLED_ICONS_CACHE.put(key, result);
        }
        return result;
    }

    /**
     * Конструктор редактора профиля трассировки.
     *
     * @param environment текущее окружение
     * @param parentWidget виджет-владелец
     * @param isReadOnly признак работы в режиме "только чтение"
     */
    public TraceProfileEditor(final IClientEnvironment environment, final QWidget parentWidget, final boolean isReadOnly) {
        super(environment, parentWidget);
        profileTree = new ProfileTree(this, environment);
        setLayout(WidgetUtils.createVBoxLayout(this));
        layout().addWidget(profileTree);
        controller = new TraceProfileTreeController<>(environment, profileTree);//Заполнение дерева
        afterOpen();
        setReadOnly(isReadOnly);
    }

    private void afterOpen() {
        profileTree.topLevelItem(0).setExpanded(true);
        profileTree.resizeColumnToContents(1);
        final ProfileTreeItemDelegate itemDelegate
                = new ProfileTreeItemDelegate(profileTree, TraceProfileTreeController.getEventSeverityItemsByOrder(getEnvironment()), getEnvironment().getMessageProvider());
        profileTree.setItemDelegateForColumn(1, itemDelegate);
        itemDelegate.eventSeverityChanged.connect(this, "onChangeEventSeverety(String,String,String)");
    }
    
    TraceProfileTreeController<ProfileTreeItem> getController(){
        return controller;
    }

    @SuppressWarnings("unused")
    private void onChangeEventSeverety(final String eventSource, final String eventSeverity, final String options) {
        controller.changeEventSeverity(eventSource, eventSeverity, options);
        profileTree.update();
    }

    /**
     * Устанавливает профиль трассировки. Реализация метода {@link ITraceProfileEditor#setTraceProfile(java.lang.String)
     * }. При вызове управление передается методу {@link TraceProfileTreeController#setProfile(java.lang.String)
     * }.
     *
     * @param traceProfileAsStr строковое представление профиля трассировки.
     * Значение равное <code>null</code> или <code>""</code> равносильно
     * значению <code>"None"</code>.
     * @throws org.radixware.kernel.common.exceptions.WrongFormatError если
     * переданная строка имеет неправильный формат или не найдено описание для
     * указанного в ней источника события или уровеня важности сообщения.
     * @see #getTraceProfile()
     */
    @Override
    public void setTraceProfile(final String traceProfileAsStr) {
        controller.setProfile(traceProfileAsStr);
    }

    /**
     * Возвращает строковое представление профиля трассировки. Реализация метода {@link ITraceProfileEditor#getTraceProfile()
     * }, возвращает результат работы
     * {@link TraceProfileTreeController#getProfile() }.
     *
     * @return строковое представление профиля трассировки. Значение не может
     * быть <code>null</code> или пустой строкой.
     * @see #setTraceProfile(java.lang.String)
     */
    @Override
    public String getTraceProfile() {
        return controller.getProfile();
    }

    /**
     * Добавляет обработчик изменения уровня важности события пользователем.
     * Реализация вызывает метод {@link TraceProfileTreeController#addListener(ITraceProfileEditor.IEventSeverityChangeListener)
     * }.
     *
     * @param listener инстанция обработчика, который необходимо
     * зарегистрировать
     * @see #removeListener(ITraceProfileEditor.IEventSeverityChangeListener)
     */
    @Override
    public void addListener(final IEventSeverityChangeListener listener) {
        controller.addListener(listener);
    }

    /**
     * Удаляет обработчик изменения уровня важности события пользователем.
     * Реализация вызывает метод {@link TraceProfileTreeController#removeListener(ITraceProfileEditor.IEventSeverityChangeListener)
     * }
     *
     * @param listener инстанция обработчика, зарегистрированного в методе {@link #addListener(IEventSeverityChangeListener)
     * }
     * @
     * see #addListener(ITraceProfileEditor.IEventSeverityChangeListener)
     */
    @Override
    public void removeListener(final IEventSeverityChangeListener listener) {
        controller.removeListener(listener);
    }

    /**
     * Возвращает признак того, что уровень важности сообщения был изменен
     * пользователем. Метод возвращает результат вызова {@link TraceProfileTreeController#isEdited()
     * }.
     *
     * @return <code>true</code>, если пользователь изменил уровень важности
     * события для хотябы одного источника, иначе - <code>false</code>
     */
    @Override
    public boolean isEdited() {
        return controller.isEdited();
    }

    /**
     * Возвращает признак работы редактора в режиме "только чтение"
     *
     * @return <code>true</code> если редактор находится в режиме "только
     * чтение" и <code>false</code> в противном случае.
     * @see #setReadOnly(boolean)
     */
    @Override
    public boolean isReadOnly() {
        return profileTree.editTriggers().isSet(EDIT_TRIGGERS);
    }

    /**
     * Устанавливает режим "только чтение". Если переданный параметр равен
     * <code>true</code>, то пользователь не сможет изменять текущие значения
     * уровней важности событий.
     *
     * @param isReadOnly логическое значение включающее или выключающее режим
     * "только чтение"
     * @see #isReadOnly()
     */
    @Override
    public void setReadOnly(final boolean isReadOnly) {
        if (isReadOnly) {
            profileTree.setEditTriggers(QAbstractItemView.EditTrigger.NoEditTriggers);
        } else {
            profileTree.setEditTriggers(EDIT_TRIGGERS);
        }
    }

    /**
     * Обновляет текущий набор источников событий. Реализация вызывает метод {@link TraceProfileTreeController#rereadEventSources()
     * }.
     *
     * @param profileAsStr строковое представление профиля трассировки, который
     * следует установить после обновления набора источников событий. Если
     * значение равно <codr>null</code>, то будет установлен тот профиль
     * трассировки, который был на момент вызова метода.
     */
    @Override
    public void rereadEventSources(final String profileAsStr) {
        setUpdatesEnabled(false);
        try {
            DISABLED_ICONS_CACHE.clear();
            controller.rereadEventSources(profileAsStr);
            ((ProfileTreeItemDelegate) profileTree.itemDelegateForColumn(1)).eventSeverityChanged.disconnect();
            afterOpen();
        } finally {
            setUpdatesEnabled(true);
        }
    }

    /**
     * Устанавливает набор источников событий, с запрещенной трассировкой.
     * Реализация вызывает метод {@link TraceProfileTreeController#setRestrictedEventSources(java.util.Collection)
     * }.
     *
     * @param eventSources набор c именами источников событий
     */
    @Override
    public void setRestrictedEventSources(final Collection<String> eventSources) {
        controller.setRestrictedEventSources(eventSources);
    }

    /**
     * Возвращает набор имен всех источников событий, отображаемых на данный
     * момент в редакторе. Реализация возвращает результат работы метода {@link TraceProfileTreeController#getEventSources()
     * }.
     *
     * @return набор имен всех источников событий
     */
    @Override
    public Collection<String> getEventSources() {
        return controller.getEventSources();
    }
}
