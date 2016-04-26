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

package org.radixware.kernel.explorer.tree;

import com.trolltech.qt.core.QAbstractItemModel;
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
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionViewItem;

import com.trolltech.qt.gui.QTreeView;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.awt.Color;
import java.awt.Rectangle;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.tree.ExplorerTreeController;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.tree.IExplorerTreePresenter;
import org.radixware.kernel.common.client.tree.IViewManager;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;

import org.radixware.kernel.common.client.tree.nodes.ChoosenEntityNode;
import org.radixware.kernel.common.client.tree.nodes.ExplorerItemNode;
import org.radixware.kernel.common.client.tree.nodes.ExplorerTreeNode;
import org.radixware.kernel.common.client.tree.nodes.RootParagraphNode;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.utils.ItemDelegatePainter;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.BlockableWidget;
import org.radixware.kernel.explorer.widgets.ExplorerAction;
import org.radixware.kernel.explorer.widgets.FilteredMouseEvent;


public class ExplorerTree extends QTreeView implements IExplorerTree {
    
    private static class Presenter implements IExplorerTreePresenter{
        
        private final ExplorerTree tree;
        
        public Presenter(final ExplorerTree tree){
            this.tree = tree;
        }

        @Override
        public IExplorerTree getView() {
            return tree;
        }        
    }

    //Need for incrase distance between tree items to improve look of icons
    private static class ExplorerTreeItemDelegate extends QItemDelegate {

        private static final int ADDINTIOAL_SPACE = 2;
        private long indexUnderMouseCursor;
        private final QRect tmpRect = new QRect();
        private final int textMargin;
        
        public ExplorerTreeItemDelegate(final QObject parent){
            super(parent);
            textMargin = QApplication.style().pixelMetric(QStyle.PixelMetric.PM_FocusFrameHMargin); 
        }

        @Override
        public QSize sizeHint(final QStyleOptionViewItem option, final QModelIndex index) {
            final QSize result = super.sizeHint(option, index);
            if (result.height()<option.decorationSize().height()){
                result.setHeight(option.decorationSize().height());
            }
            result.setHeight(result.height() + ADDINTIOAL_SPACE);
            return result;
        }

        @Override
        public void paint(final QPainter painter, final QStyleOptionViewItem option, final QModelIndex index) {            
            final QAbstractItemModel model = index.model();
            final String text = (String)model.data(index, Qt.ItemDataRole.DisplayRole);
            final QFont font = (QFont)model.data(index, Qt.ItemDataRole.FontRole);
            final QFontMetrics fontMetrics = new QFontMetrics(font);            
            option.setFont(font);
            option.setFontMetrics(fontMetrics);
            
            //option.setFontMetrics(font.getQFontMetrics());
            final Integer textAlignment = (Integer)model.data(index, Qt.ItemDataRole.TextAlignmentRole);
            if (textAlignment!=null){
                option.setDisplayAlignment(new Qt.Alignment(textAlignment.intValue()));
            }
            final QColor textColor = (QColor)model.data(index, Qt.ItemDataRole.ForegroundRole);
            if (textColor!=null){
                final QPalette palette = new QPalette(option.palette());
                palette.setColor(QPalette.ColorRole.Text, textColor);                
                option.setPalette(palette);
            }            
            final QColor backgroundColor;
            if (index.internalId()==indexUnderMouseCursor){
                final QColor highlightColor = 
                    option.palette().color(QPalette.ColorGroup.Active, QPalette.ColorRole.Highlight);
                backgroundColor = 
                    ExplorerTextOptions.getDarker(ExplorerTextOptions.qtColor2awtColor(highlightColor), -75);                
            }else{
                backgroundColor = (QColor)model.data(index, Qt.ItemDataRole.BackgroundRole);
            }
            final QRect rect = option.rect();
            // prepare
            painter.save();
            try{
                painter.setClipRect(rect);

                // get the data and the rectangles        

                //final QPixmap pixmap;
                final Rectangle decorationRect;        
                final QIcon icon = (QIcon)model.data(index,Qt.ItemDataRole.DecorationRole);            
                if (icon==null) {            
                    decorationRect = null;
                } else {
                    decorationRect = new Rectangle(0, 0, option.decorationSize().width(), option.decorationSize().height());
                }

                final Rectangle displayRect;
                if (text==null || text.isEmpty()){
                    displayRect = null;
                }else{
                    displayRect = new Rectangle(0, 0, WidgetUtils.calcTextWidth(text, fontMetrics, true), fontMetrics.height());
                }

                // do the layout
                final ItemDelegatePainter.CellLayout layout = 
                    ItemDelegatePainter.getInstance().doLayout(option, new ItemDelegatePainter.CellLayout(null, decorationRect, displayRect), textMargin);
                                
                // draw the item     
                ItemDelegatePainter.getInstance().drawBackground(painter, option, backgroundColor);
                if (icon !=null ){
                    ItemDelegatePainter.getInstance().drawDecoration(painter, option, icon, layout.decorationRect);
                }
                ItemDelegatePainter.getInstance().drawDisplay(painter, option, new Rectangle(layout.textRect), font, text, textMargin, hasClipping());                                
                drawFocus(painter, option, WidgetUtils.awtRect2QRect(layout.textRect, tmpRect));
                // done
            }finally{
                painter.restore();
            }                    
        }
        
        public void setIndexUnderMouseCursor(final long index){
            indexUnderMouseCursor = index;
        }
        
        public long getIndexUnderMouseCursor(){
            return indexUnderMouseCursor;
        }
    }

    private final static class Icons extends ClientIcon {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        public final static Icons REMOVE_FROM_TREE = new Icons("classpath:images/remove_from_tree.svg");
        public final static Icons REMOVE_ALL_FROM_TREE = new Icons("classpath:images/remove_all_from_tree.svg");
        public final static Icons OPEN_PARENT = new Icons("classpath:images/go_to_parent.svg");
        public final static Icons GO_TO_CURRENT = new Icons("classpath:images/go_to_current.svg");
    }
    
    private final static class SilentlyCleanEntityModelController extends CleanModelController{
        
        private final Pid silentPid;
        
        public SilentlyCleanEntityModelController(final Pid pid){
            this.silentPid = pid;
        }

        @Override
        public boolean needToCheckMandatoryProperties(final Model model) {
            if (model instanceof EntityModel && Objects.equals(silentPid, ((EntityModel)model).getPid())){
                return false;
            }
            return true;
        }                        
    }

    public final class Actions implements IExplorerTree.Actions{

        public final ExplorerAction remove;
        public final ExplorerAction removeAll;
        public final ExplorerAction goToOwner;
        public final ExplorerAction goToCurrent;

        @SuppressWarnings("LeakingThisInConstructor")
        public Actions() {
            super();
            remove = new ExplorerAction(ExplorerIcon.getQIcon(Icons.REMOVE_FROM_TREE),
                    getEnvironment().getMessageProvider().translate("ExplorerTree", "Close in Tree"), ExplorerTree.this);
            remove.setToolTip(getEnvironment().getMessageProvider().translate("ExplorerTree", "Close Object in Tree"));
            remove.triggered.connect(this, "removeAction()");
            remove.setDisabled(true);
            removeAll = new ExplorerAction(ExplorerIcon.getQIcon(Icons.REMOVE_ALL_FROM_TREE),
                    getEnvironment().getMessageProvider().translate("ExplorerTree", "Close All in Tree"), ExplorerTree.this);
            removeAll.setToolTip(getEnvironment().getMessageProvider().translate("ExplorerTree", "Close All Objects in Tree"));
            removeAll.triggered.connect(this, "removeAllAction()");
            removeAll.setDisabled(true);
            
            goToOwner = new ExplorerAction(ExplorerIcon.getQIcon(Icons.OPEN_PARENT), getEnvironment().getMessageProvider().translate("ExplorerTree", "Go To Parent Branch"), ExplorerTree.this);
            goToOwner.setToolTip(getEnvironment().getMessageProvider().translate("ExplorerTree", "Go To Parent Branch"));
            goToOwner.triggered.connect(this, "goToOwnerAction()");
            goToOwner.setDisabled(true);
            
            goToCurrent = new ExplorerAction(ExplorerIcon.getQIcon(Icons.GO_TO_CURRENT), getEnvironment().getMessageProvider().translate("ExplorerTree", "Go To Current Item"), ExplorerTree.this);
            goToCurrent.setToolTip(getEnvironment().getMessageProvider().translate("ExplorerTree", "Go To Current Item"));
            goToCurrent.triggered.connect(this, "goToCurrentAction()");
            goToCurrent.setDisabled(true);
        }

        @SuppressWarnings("unused")
        private void removeAction() {
            removeCurrentNode();
        }
        
        @SuppressWarnings("unused")
        private void goToCurrentAction(){
            goToNode(currentNode());
        }

        @SuppressWarnings("unused")
        private void removeAllAction() {
            lock();
            try {
                if (canLeaveNode(false,null)) {
                    treeModel().getRootNode().getView().removeChoosenEntities();
                }
                if (currentNode() != treeModel().getRootNode()) {
                    enterNode(treeModel().findIndexByNode(treeModel().getRootNode()), closeCurrent);
                }
            } finally {
                unlock();
            }
            removeAll.setDisabled(true);
        }

        @SuppressWarnings("unused")
        private void goToOwnerAction() {
            if (currentNode() != null && currentNode().getParentNode() != null) {
                setCurrent(currentNode().getParentNode());
            }
        }
        
        

        protected void disconnectAll() {
            remove.disconnect();
            removeAll.disconnect();
            goToOwner.disconnect();
            goToCurrent.disconnect();
        }

        protected void disableAll() {
            remove.setDisabled(true);
            removeAll.setDisabled(true);
            goToOwner.setDisabled(true);
            goToCurrent.setDisabled(true);
        }
        
        private boolean isChoosenEntityExists(final List<IExplorerTreeNode> nodes) {
            for (IExplorerTreeNode node : nodes) {
                if (node.isValid() && node.getView().isChoosenObject()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public ExplorerAction getRemoveCurrentNodeAction() {
            return remove;
        }

        @Override
        public ExplorerAction getRemoveChildChoosenObjectsAction() {
            return removeAll;
        }

        @Override
        public ExplorerAction getGoToParentNodeAction() {
            return goToOwner;
        }    

        @Override
        public Action getGoToCurrentNodeAction() {
            return goToCurrent;
        }                
    }
        
    
    private final Actions actions;
    private final List<IExplorerTreeNode> nodesToRemove = new ArrayList<>();
    private final List<QModelIndex> expandedItems = new ArrayList<>();
    private final ExplorerTreeSettings treeSettings;
    private IViewManager viewManager;
    private final Collection<Id> accessibleExplorerItems = new ArrayList<>();
    private ExplorerRoot explorerRoot;
    private boolean closeCurrent = true;
    private boolean hasModel;
    private boolean scheduledClick, scheduledDblClick;
    private final IClientEnvironment environment;
    private final ExplorerTreeController controller = new ExplorerTreeController(new Presenter(this));
    private final ExplorerTreeItemDelegate itemDelegate = new ExplorerTreeItemDelegate(this);
    private BlockableWidget blockableParent;
    private IExplorerTreeNode nodeUnderMouseCursor;
    private int locked = 0;
    private boolean eventProcessing = false;
    private final QEventFilter eventBlocker = new QEventFilter(this) {
        @Override
        public boolean eventFilter(final QObject source, final QEvent event) {
            return event instanceof FilteredMouseEvent==false;//RADIX-7253
        }
    };
    
    public ExplorerTree(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        eventBlocker.setProcessableEventTypes(EnumSet.of(QEvent.Type.User));
        this.environment = environment;
        this.actions = new Actions();
        treeSettings = new ExplorerTreeSettings(new ExplorerSettings(environment, environment.getConfigStore()), this);        
        header().setVisible(false);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        setMouseTracking(true);
    }
    
    private BlockableWidget findBlockableParent() {
        for (QWidget parent = parentWidget(); parent != null; parent = parent.parentWidget()) {
            if (parent instanceof BlockableWidget) {
                return (BlockableWidget) parent;
            }
        }
        return null;
    }    

    public ExplorerTree openSubTree(final ExplorerTreeNode node, final QWidget parentWidget) {
        final ExplorerTree newTree = new ExplorerTree(environment, parentWidget);
        newTree.accessibleExplorerItems.addAll(accessibleExplorerItems);
        newTree.explorerRoot = explorerRoot;
        newTree.lock();
        final Stack<Id> explorerItemIds = new Stack<>();
        ExplorerTreeNode newParentNode = null;
        for (ExplorerTreeNode parentNode = node; parentNode != null; parentNode = parentNode.getParentNode()) {
            if ((parentNode instanceof RootParagraphNode) || (parentNode instanceof ChoosenEntityNode)) {
                newParentNode = parentNode.clone(newTree);
                break;
            } else {
                explorerItemIds.push(parentNode.getExplorerItemId());
            }
        }
        if (newParentNode != null) {
            ExplorerTreeNode newChildNode = newParentNode;
            Id currentExplorerItemId;
            List<IExplorerTreeNode> childNodes;
            while (!explorerItemIds.isEmpty()) {
                currentExplorerItemId = explorerItemIds.pop();
                childNodes = newChildNode.getChildNodes();
                for (IExplorerTreeNode childNode : childNodes) {
                    if (((ExplorerTreeNode) childNode).getExplorerItemId().equals(currentExplorerItemId)) {
                        newChildNode = (ExplorerTreeNode) childNode;
                        break;
                    }
                }
            }
            newTree.setModel(new ExplorerTreeModel(newChildNode, environment, newTree));
            newTree.unlock();
            return newTree;
        }
        return null;
    }

    public void open(final RootParagraphNode rootParagraph, final List<Id> accessibleExplorerItems, final ExplorerRoot explorerRoot){
        this.accessibleExplorerItems.clear();
        this.accessibleExplorerItems.addAll(accessibleExplorerItems);
        this.explorerRoot = explorerRoot;
        setModel(new ExplorerTreeModel(rootParagraph, environment, this));
    }

    @Override
    public void setModel(final QAbstractItemModel model) {
        if (!(model instanceof ExplorerTreeModel)) {
            throw new IllegalArgumentException("Model of explorer tree  expected");
        }
        blockableParent = findBlockableParent();
        setupColorSettings();

        setIconSize(getIconSize());
        setItemDelegate(itemDelegate);
        super.setModel(model);
        hasModel = true;
        final QModelIndex index = treeModel().index(0, 0);//init parent index
        treeModel().getRootNode().getChildNodes();//init child nodes
        lock();
        try {
            enterNode(index, closeCurrent);
        } finally {
            unlock();
        }
        expand(treeModel().getRootNode());
        setFocus();
        resizeColumnToContents(0);
        Application.getInstance().getActions().settingsChanged.connect(this, "applySettings()");
        expanded.connect(this, "onExpanded(QModelIndex)");
        collapsed.connect(this, "onCollapsed(QModelIndex)");
        treeSettings.restore();
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return environment;
    }

    private ExplorerTreeModel treeModel() {
        if (hasModel) {
            return (ExplorerTreeModel) model();            
        }else{
            throw new IllegalUsageError("Model of explorer tree was not set");
        }
        
    }

    public void setViewManager(final IViewManager viewManager) {
        this.viewManager = viewManager;
        if (hasModel) {
            final IExplorerTreeNode current = currentNode();
            if (current != null && viewManager != null) {
                viewManager.openView(current, false);
            }
        }
    }

    @Override
    public IViewManager getViewManager() {
        return viewManager;
    }

    @SuppressWarnings("unused")
    private void onExpanded(final QModelIndex index) {
        resizeColumnToContents(0);
        expandedItems.add(index);
    }

    @SuppressWarnings("unused")
    private void onCollapsed(final QModelIndex index) {
        resizeColumnToContents(0);
        expandedItems.remove(index);
    }

    private QSize getIconSize() {
        final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
        QSize size;

        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
        settings.beginGroup(SettingNames.ExplorerTree.COMMON_GROUP);
        size = settings.readQSize(SettingNames.ExplorerTree.Common.ICON_SIZE);
        settings.endGroup();
        settings.endGroup();
        settings.endGroup();

        return size;
    }

    private void setupColorSettings() {
        final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
        QColor color, fontcolor, backgroundcolor;

        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
        settings.beginGroup(SettingNames.ExplorerTree.COMMON_GROUP);
        try {
            color = settings.readQColor(SettingNames.ExplorerTree.Common.TREE_BACKGROUND);  //.value(SettingNames.ExplorerTree.Common.TREE_BACKGROUND, DefaultSettings.defaultTreeBackgroundColor);
            fontcolor = settings.readQColor(SettingNames.ExplorerTree.Common.TREE_SELECTED_ITEM_FONT_COLOR);
            backgroundcolor = settings.readQColor(SettingNames.ExplorerTree.Common.TREE_SELECTED_ITEM_BACKGROUND);
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }

        QPalette palette = new QPalette(palette());
        palette.setColor(QPalette.ColorRole.Base, color);

        this.viewport().setPalette(palette);

        palette = new QPalette(palette());
        palette.setColor(QPalette.ColorRole.Highlight, backgroundcolor);
        palette.setColor(QPalette.ColorRole.HighlightedText, fontcolor);

        this.setPalette(palette);
        final Color hoverBackground = ExplorerTextOptions.getDarkerColor(ExplorerTextOptions.qtColor2awtColor(backgroundcolor), -25);
        //setStyleSheet("QTreeView::item:hover{background-color:"+ExplorerTextOptions.color2Str(hoverBackground)+";}");        
        setStyleSheet("item {background-color: "+ExplorerTextOptions.color2Str(hoverBackground)+";} ");
    }

    public void applySettings() {
        setIconSize(getIconSize());
        setupColorSettings();
        update();
    }

    public boolean closeModel(final boolean forced) {
        if (!hasModel) {
            return true;
        }        
        if (canLeaveNode(forced,null) || forced) {
            storeExplorerRootId();
            treeSettings.store();
            nodesToRemove.clear();
            expandedItems.clear();
            hasModel = false;
            super.setModel(null);
            actions.disableAll();
            actions.disconnectAll();
            Application.getInstance().getActions().settingsChanged.disconnect(this);
            return true;
        }
        return false;
    }
    
    private void storeExplorerRootId(){
        final ClientSettings settings = getEnvironment().getConfigStore();
        if (RunParams.needToRestoreContext()){
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
            settings.beginGroup(SettingNames.ExplorerTree.COMMON_GROUP);
            try {
                settings.writeString(SettingNames.ExplorerTree.Common.ROOT, explorerRoot.getId().toString());
            } finally {
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
            }       
        }
    }

    @Override
    public List<IExplorerTreeNode> getRootNodes() {
        final List<IExplorerTreeNode> result = new ArrayList<>();
        if (hasModel) {
            result.add(treeModel().getRootNode());
        }
        return result;
    }

    @Override
    public void setNodeVisible(final IExplorerTreeNode node, final boolean isVisible) {
        final IExplorerTreeNode parentNode = node.getParentNode();
        if (parentNode == null) {
            setRowHidden(0, null, !isVisible);
            return;
        }
        final QModelIndex index = treeModel().findIndexByNode(parentNode);
        if (index != null) {
            final int row = parentNode.getChildNodes().indexOf(node);
            setRowHidden(row, index, !isVisible);
        }
    }

    @Override
    public boolean isNodeVisible(final IExplorerTreeNode node) {
        final IExplorerTreeNode parentNode = node.getParentNode();
        if (parentNode == null) {
            return isRowHidden(0, null);
        }
        final QModelIndex index = treeModel().findIndexByNode(parentNode);
        if (index != null) {
            final int row = parentNode.getChildNodes().indexOf(node);
            return !isRowHidden(row, index);
        }
        throw new IllegalArgumentException("cannot find node in tree model");
    }

    @Override
    public void update(final IExplorerTreeNode node) {
        if (!isLocked() && hasModel) {
            final QModelIndex index = treeModel().findIndexByNode(node);
            if (index != null) {
                update(index);
            }
        }
    }        

    @Override
    public void lock() {
        if (locked == 0) {
            if (blockableParent != null) {
                blockableParent.blockRedraw();
            } else {
                setDisabled(true);
                window().setUpdatesEnabled(false);
            }
            installEventFilter(eventBlocker);

            locked = 1;
        } else {
            locked++;
        }
    }

    @Override
    public void unlock() {
        if (isLocked()) {
            locked--;
            if (locked == 0) {
                removeEventFilter(eventBlocker);

                if (blockableParent != null) {
                    blockableParent.unblockRedraw();
                } else {
                    setDisabled(false);
                    window().setUpdatesEnabled(true);
                    window().repaint();
                    repaint();
                }
            }
        }
    }

    public void unlockForcedly() {
        if (isLocked()) {
            locked = 1;
            unlock();
        }
    }

    public boolean isLocked() {
        return locked > 0;
    }    

    @Override
    protected void paintEvent(final QPaintEvent event) {
        if (!isLocked() && !eventProcessing) {
            eventProcessing = true;
            try {
                super.paintEvent(event);
            } finally {
                eventProcessing = false;
            }
        }
    }

    @Override
    protected void timerEvent(final QTimerEvent event) {
        if (!isLocked()) {
            super.timerEvent(event);
        }
    }

    private boolean canRemoveNow(final QModelIndex indexToRemove) {
        for (QModelIndex index = currentIndex(); index != null; index = index.parent()) {
            if (index.equals(indexToRemove)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void removeNode(final IExplorerTreeNode node) {
        final QModelIndex index = treeModel().findIndexByNode(node);
        if (index != null) {
            if (!canRemoveNow(index)) {
                if (enteringNode) {//Removing current node inside of "enteringNode" method
                    //just mark for remove here
                    nodesToRemove.add(node);
                    return;
                }
                lock();
                try {
                    if (canLeaveNode(false,null)) {
                        nodesToRemove.add(node);
                        enterNode(index, closeCurrent);
                    } else {
                        return;
                    }
                } finally {
                    unlock();
                }
                setFocus();
            } else {
                expandedItems.remove(index);
                treeModel().removeNode(node);
            }
            controller.refreshActions();
        }
    }

    @Override
    public IExplorerTreeNode addChoosenEntity(final ExplorerTreeNode parent, final EntityModel entity, final int index) {
        final ChoosenEntityNode result = new ChoosenEntityNode(this, entity, parent);        
        lock();
        try {
            treeModel().insertNode(parent, result, index, true);
            result.getChildNodes();//init child nodes
        } finally {
            unlock();
        }
        controller.refreshActions();
        return result;
    }

    @Override
    public IExplorerTreeNode addUserExplorerItem(final ExplorerTreeNode parent, final RadExplorerItemDef userItem, final int index) {
        final ExplorerItemNode treeNode = new ExplorerItemNode(this, parent, userItem.getId());
        lock();
        try {
            treeModel().insertNode(parent, treeNode, index, false);
        } finally {
            unlock();
        }        
        return treeNode;
    }        

    @Override
    public boolean setCurrent(final IExplorerTreeNode node) {
        if (node == null) {
            return false;
        }
        final QModelIndex index = treeModel().findIndexByNode(node);
        if (index == null) {
            return false;
        }
        lock();
        try {
            if (canLeaveNode(false,node)) {
                enterNode(index, closeCurrent);
            } else {
                return false;
            }
        } finally {
            unlock();
        }
        setFocus();
        return true;
    }

    @Override
    public IExplorerTreeNode getCurrent() {
        return treeModel().findNodeByIndex(currentIndex());
    }

    @Override
    public IExplorerTreeNode findNodeByExplorerItemId(Id explorerItemId) {
        final List<Id> lookedNodes = new ArrayList<>();
        final Stack<IExplorerTreeNode> nodesToLook = new Stack<>();
        IExplorerTreeNode currentNode;
        Id currentExplorerItemId;
        for (IExplorerTreeNode rootNode : getRootNodes()) {
            lookedNodes.clear();
            nodesToLook.clear();
            nodesToLook.push(rootNode);
            while (!nodesToLook.isEmpty()) {
                currentNode = nodesToLook.pop();
                if (currentNode.isValid()) {
                    currentExplorerItemId = currentNode.getView().getExplorerItemId();
                    if (currentExplorerItemId.equals(explorerItemId)) {
                        return currentNode;
                    } else {
                        if (!lookedNodes.contains(currentExplorerItemId)) {
                            nodesToLook.addAll(currentNode.getChildNodes());
                            lookedNodes.add(explorerItemId);
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public IExplorerTreeNode findNodeByPosition(final int xPos, final int yPos) {
        return treeModel().findNodeByIndex(indexAt(new QPoint(xPos, yPos)));
    }

    @Override
    public void expand(final IExplorerTreeNode node) {
        lock();
        try {
            final List<IExplorerTreeNode> childNodes = node.getChildNodes();//init childs
            //ensure parent nodes expanded
            final List<QModelIndex> parentIndexes = new ArrayList<>();
            for (IExplorerTreeNode parentNode = node.getParentNode(); parentNode != null; parentNode = parentNode.getParentNode()) {
                parentIndexes.add(0, treeModel().findIndexByNode(parentNode));
            }
            for (QModelIndex index : parentIndexes) {
                if (!isExpanded(index)) {
                    expand(index);
                }
            }
            //expand node
            expand(treeModel().findIndexByNode(node));
            for (IExplorerTreeNode childNode : childNodes) {
                childNode.getChildNodes();//init ChildNodes
            }
        } finally {
            unlock();
        }
    }

    @Override
    public void collapse(final IExplorerTreeNode node) {
        collapse(treeModel().findIndexByNode(node));
    }

    @Override
    public boolean isExpanded(final IExplorerTreeNode node) {
        return isExpanded(treeModel().findIndexByNode(node));
    }

    @Override
    public boolean isExplorerItemAccessible(final Id explorerItemId) {
        return accessibleExplorerItems.contains(explorerItemId);
    }

    private IExplorerTreeNode currentNode() {
        final QModelIndex index = currentIndex();
        return index != null ? treeModel().findNodeByIndex(index) : null;
    }

    private boolean canLeaveNode(final boolean forced, final IExplorerTreeNode nextNode) {
        final IExplorerTreeNode currentNode;
        if (viewManager != null && Utils.equals(currentNode(), viewManager.getCurrentNode())) {
            currentNode = viewManager.getCurrentNode();
        } else {
            currentNode = null;
        }

        if (currentNode != null && currentNode.isValid()) {
            final Model model = currentNode.getView().getModel();
            if (model != null && !forced) {
                if (sameNodes(currentNode,nextNode)){
                    final Pid pid = ((EntityModel)nextNode.getView().getModel()).getPid();
                    return model.canSafelyClean(new SilentlyCleanEntityModelController(pid));
                }else{
                    return model.canSafelyClean(CleanModelController.DEFAULT_INSTANCE);
                }                
            }
        }
        return true;
    }
    
    private static boolean sameNodes(final IExplorerTreeNode currentNode, final IExplorerTreeNode nextNode){
        if (currentNode==null || nextNode==null || !currentNode.isValid() || !nextNode.isValid()){
            return false;
        }
        final Model currentModel = currentNode.getView()==null ? null : currentNode.getView().getModel();
        final Model nextModel = nextNode.getView()==null ? null : nextNode.getView().getModel();
        if (currentModel instanceof GroupModel && currentModel.getView()!=null && nextModel instanceof EntityModel){
            final EntityModel currentEntityModel = ((ISelector)currentModel.getView()).getCurrentEntity();
            return currentEntityModel!=null && Objects.equals(currentEntityModel.getPid(), ((EntityModel)nextModel).getPid());
        }
        return false;
    }
    
    private boolean enteringNode;

    private void enterNode(final QModelIndex index, final boolean closeCurrent) {
        IExplorerTreeNode targetNode = treeModel().findNodeByIndex(index);
        if (targetNode != null) {
            if (enteringNode) {
                throw new IllegalStateException("Recursive call is not supported");
            }
            enteringNode = true;
            try {
                targetNode = getActualTargetNode(targetNode);
                {//clean removed nodes
                    QModelIndex indexToRemove;
                    for (IExplorerTreeNode node : nodesToRemove) {
                        indexToRemove = treeModel().findIndexByNode(node);
                        if (indexToRemove != null) {
                            expandedItems.remove(indexToRemove);
                            treeModel().removeNode(node);
                        }
                    }
                    nodesToRemove.clear();
                }

                final QModelIndex actualIndex = treeModel().findIndexByNode(targetNode);

                setCurrentIndex(actualIndex);
                if (environment.getStatusBar()!=null){
                    environment.getStatusBar().setCurrentExplorerTreeNode(targetNode);
                }

                /*         We can safely apply updates here (if any) but it takes too long time to do it automatically

                if (Environment.getDefManager().isOldVersionModeEnabled() &&
                !Environment.getDefManager().isKernelChanged()
                ){
                final Collection<Id> changedDefinitions = Environment.getDefManager().checkForUpdates();
                try{
                Environment.getTreeManager().updateVersion(changedDefinitions);
                return;
                }
                catch(CantUpdateVersionException ex){
                //continue open in old version
                }
                }
                 */
                        
                if (viewManager != null) {
                    viewManager.openView(targetNode, closeCurrent);
                    if (!hasModel){//RADIX-9674 
                        return;
                    }
                }
                if (!nodesToRemove.isEmpty() && nodeWasRemoved(targetNode)) {
                    enteringNode = false;
                    enterNode(actualIndex.parent(), closeCurrent);
                } else {
                    controller.refreshActions();                    
                }
            } finally {
                enteringNode = false;
            }
        }
    }

    private boolean nodeWasRemoved(final IExplorerTreeNode nodeForCheck) {
        for (IExplorerTreeNode node = nodeForCheck; node != null; node = node.getParentNode()) {
            if (nodesToRemove.contains(node)) {
                return true;
            }
        }
        return false;
    }

    private IExplorerTreeNode getActualTargetNode(final IExplorerTreeNode candidate) {
        if (nodesToRemove.isEmpty() || !nodeWasRemoved(candidate)) {
            return candidate;
        }

        final IExplorerTreeNode parent = candidate.getParentNode();
        IExplorerTreeNode result = null,
                child;
        final List<IExplorerTreeNode> childs = parent.getChildNodes();
        final int itemCount = childs.size();

        //search for not removed sibiling node below candidate
        for (int i = childs.indexOf(candidate) + 1; i < itemCount && result == null; i++) {
            child = childs.get(i);
            if (!nodeWasRemoved(child)) {
                result = child;
            }
        }

        if (result == null) //search for not removed sibiling node above candidate
        {
            for (int i = childs.indexOf(candidate) - 1; i >= 0 && result == null; i--) {
                child = childs.get(i);
                if (!nodeWasRemoved(child)) {
                    result = child;
                }
            }
        }

        if (result == null) {
            //if appropriate sibiling node was not found than return parent node
            for (IExplorerTreeNode node = parent; result == null && node != null; node = node.getParentNode()) {
                if (!nodeWasRemoved(node)) {
                    result = node;
                }
            }
        }

        return result;
    }
    
    public final void goToNode(final IExplorerTreeNode node){
        if (node!=null){
            controller.ensurePathExpanded(node);
            final QModelIndex index = treeModel().findIndexByNode(node);
            if (index!=null){
                scrollTo(index, ScrollHint.PositionAtCenter);
            }
        }
    }

    public void removeCurrentNode() {
        controller.removeCurrentNode();
        resizeColumnToContents(0);
    }        

    @Override
    protected void mousePressEvent(final QMouseEvent event) {
        scheduledClick = false;
        scheduledDblClick = false;
        final QModelIndex index = indexAt(event.pos());        
        if (index != null && !visualRect(index).contains(event.x(), event.y())) {
            final IExplorerTreeNode node = treeModel().findNodeByIndex(index);
            if (node != null 
                && node.getChildNodes().size() > 0
                && expandOnMousePress()) {
                setExpanded(index, !isExpanded(index));                
            }
            return;
        }
        QModelIndex curIndex = currentIndex();
        final boolean someButton = event.buttons().isSet(MouseButton.RightButton) || event.buttons().isSet(MouseButton.LeftButton);
        if (index != null) {
            event.accept();
            lock();
            try {
                if (!index.equals(curIndex) && someButton && !canLeaveNode(false,treeModel().findNodeByIndex(index))) {
                    return;
                }
                if (!index.equals(curIndex)) {
                    enterNode(index, closeCurrent);                    
                }
            } catch (Throwable ex) {
                if (viewManager != null) {
                    viewManager.closeCurrentView();
                }
                unlock();
                getEnvironment().processException(ex);
            } finally {                
                unlock();
                curIndex = currentIndex();
            }
            setFocus();
        }
        if (expandOnMousePress()){
            if (scheduledDblClick && model()!=null){
                final IExplorerTreeNode node = treeModel().findNodeByIndex(curIndex);
                if (node != null && node.getChildNodes().size() > 0) {
                    setExpanded(curIndex, !isExpanded(curIndex));
                }
            }
        }
    }    
    
    private boolean expandOnMousePress(){
        return style().styleHint(QStyle.StyleHint.SH_Q3ListViewExpand_SelectMouseType, null, this)==QEvent.Type.MouseButtonPress.value();
    }

    @Override
    protected void mouseDoubleClickEvent(QMouseEvent event) {
        scheduledClick = false;
        scheduledDblClick = false;
        final QModelIndex index = indexAt(event.pos());        
        final boolean leftButton = event.buttons().isSet(MouseButton.LeftButton);
        if (index != null) {
            event.accept();
            final IExplorerTreeNode node = treeModel().findNodeByIndex(index);
            if (leftButton && node != null && node.getChildNodes().size() > 0) {
                setExpanded(index, !isExpanded(index));
            }            
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof FilteredMouseEvent){
            final QEvent.Type type = ((FilteredMouseEvent)event).getFilteredEventType();
            if (type==QEvent.Type.MouseButtonPress){
                scheduledClick = true;
            }else if (type==QEvent.Type.MouseButtonDblClick && !scheduledClick){
                scheduledDblClick = true;
            }
        }else{
            super.customEvent(event);
        }
    }    

    @Override
    protected void mouseMoveEvent(final QMouseEvent event) {
        if (event.buttons().isSet(MouseButton.RightButton) || event.buttons().isSet(MouseButton.LeftButton)) {
            event.ignore();
        } else {            
            super.mouseMoveEvent(event);
        }
        if (hasModel){
            final QModelIndex index = this.indexAt(event.pos());
            final long indexId = index==null ? 0 : index.internalId();
            if (indexId!=itemDelegate.getIndexUnderMouseCursor()){
                itemDelegate.setIndexUnderMouseCursor(indexId);
                final QModelIndex prevIndex = treeModel().findIndexByNode(nodeUnderMouseCursor);
                if (prevIndex!=null){
                    dataChanged(prevIndex, prevIndex);
                }
                if (index!=null){
                    dataChanged(index, index);
                }
                nodeUnderMouseCursor = treeModel().findNodeByIndex(index);            
            }
        }
    }

    @Override
    protected void leaveEvent(QEvent event) {
        super.leaveEvent(event);
        if (hasModel){
            itemDelegate.setIndexUnderMouseCursor(0);
            if (nodeUnderMouseCursor!=null){
                final QModelIndex prevIndex = treeModel().findIndexByNode(nodeUnderMouseCursor);
                if (prevIndex!=null){
                    dataChanged(prevIndex, prevIndex);
                }
            }
        }
        nodeUnderMouseCursor = null;
    }
    
    

    @Override
    protected QModelIndex moveCursor(final CursorAction arg0, final KeyboardModifiers arg1) {
        final QModelIndex index = super.moveCursor(arg0, arg1);
        if (index != null && index != currentIndex()) {
            lock();
            try {
                if (canLeaveNode(false,treeModel().findNodeByIndex(index))) {
                    enterNode(index, closeCurrent);
                } else {
                    return null;
                }
            } catch (Throwable ex) {
                if (viewManager != null) {
                    viewManager.closeCurrentView();
                }
                unlock();
                getEnvironment().processException(ex);
            } finally {
                unlock();
            }
            setFocus();
        }
        return null;
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        if (hasModel) {
            //closeModel was not called
            treeSettings.store();
            actions.disableAll();
            actions.disconnectAll();
            Application.getInstance().getActions().settingsChanged.disconnect(this);
            nodesToRemove.clear();
            expandedItems.clear();
            hasModel = false;
            super.setModel(null);
        }
        nodeUnderMouseCursor = null;
        super.closeEvent(event);
    }

    @SuppressWarnings("UseSpecificCatch")
    public final void refill(final IProgressHandle progress, final boolean relogin) {
        if (relogin) {
            if (progress != null) {
                progress.setText(getEnvironment().getMessageProvider().translate("ExplorerTree", "Updating list of accessible explorer items"));
            }
            accessibleExplorerItems.clear();
            try {                
                accessibleExplorerItems.addAll(explorerRoot.getVisibleExplorerItems());
            } catch (Exception exception) {
                final String errMessage = getEnvironment().getMessageProvider().translate("ExplorerError", "Can't get a list of accessible explorer items: %s\n%s");
                final String errReason = ClientException.getExceptionReason(environment.getMessageProvider(), exception);
                final String errStack = ClientException.exceptionStackToString(exception);
                environment.getTracer().error(String.format(errMessage, errReason, errStack));
                accessibleExplorerItems.add(treeModel().getRootNode().getOwnerDefinitionId());
            }
        }
        if (progress != null) {
            progress.setText(getEnvironment().getMessageProvider().translate("ExplorerTree", "Preparing for Update"));
        }
        final List<Stack<Integer>> nodesToExpand = new ArrayList<>();
        for (QModelIndex index : expandedItems) {
            nodesToExpand.add(indexToPath(index));
        }
        expandedItems.clear();

        final Stack<Integer> currentNode = indexToPath(currentIndex());

        setVisible(false);
        try {
            treeModel().reinit(progress);
        } finally {
            setVisible(true);
        }

        if (progress != null) {
            progress.setText(getEnvironment().getMessageProvider().translate("ExplorerTree", "Finishing Update"));
            progress.setMaximumValue(nodesToExpand.size());
        }

        QModelIndex index;
        for (int i = 0; i < nodesToExpand.size(); i++) {
            if (progress != null) {
                progress.setMaximumValue(i);
            }
            index = pathToIndex(nodesToExpand.get(i));
            if (index != null) {
                expand(index);
            }
        }

        final QModelIndex currentIndex = pathToIndex(currentNode);
        if (currentIndex != null) {
            if (progress != null) {
                progress.setText(getEnvironment().getMessageProvider().translate("ExplorerTree", "Reopening Current Item"));
                progress.setValue(0);
                progress.setMaximumValue(0);
            }
            enterNode(currentIndex, true);
        }
    }

    private Stack<Integer> indexToPath(final QModelIndex index) {
        final Stack<Integer> path = new Stack<>();
        IExplorerTreeNode node;
        for (QModelIndex idx = index; idx != null; idx = idx.parent()) {
            node = treeModel().findNodeByIndex(idx);
            if (node == null) {
                path.clear();
                return path;
            }
            path.push(node.hashCode());
        }
        return path;
    }

    private QModelIndex pathToIndex(final Stack<Integer> path) {
        Integer hashCode;
        List<IExplorerTreeNode> childNodes;
        QModelIndex currentIndex = null;
        IExplorerTreeNode currentNode = null, node;
        boolean childNodeFound;

        while (!path.isEmpty()) {
            hashCode = path.pop();
            childNodes = currentNode == null ? getRootNodes() : currentNode.getChildNodes();
            childNodeFound = false;
            for (int i = 0; i < childNodes.size(); i++) {
                node = childNodes.get(i);
                if (node.hashCode() == hashCode) {
                    currentNode = node;
                    currentIndex = model().index(i, 0, currentIndex);
                    childNodeFound = true;
                    break;
                }
            }
            if (!childNodeFound) {
                break;
            }
        }

        return currentIndex;
    }

    @Override
    public Actions getActions() {
        return actions;
    }        
}