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

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMenuBar;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QWidget;
import java.awt.Dimension;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.env.AdsVersion;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.errors.BrokenConnectionError;
import org.radixware.kernel.common.client.exceptions.CantUpdateVersionException;
import org.radixware.kernel.common.client.exceptions.KernelClassModifiedException;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.tree.IExplorerTreeManager;
import org.radixware.kernel.common.client.tree.IViewManager;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;

import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;


import org.radixware.kernel.common.client.tree.nodes.ChoosenEntityNode;
import org.radixware.kernel.common.client.tree.nodes.ExplorerTreeNode;
import org.radixware.kernel.common.client.tree.nodes.RootParagraphNode;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.utils.LeakedWidgetsDetector;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.BlockableWidget;
import org.radixware.kernel.explorer.views.IExplorerView;
import org.radixware.kernel.explorer.views.MainWindow;
import org.radixware.kernel.explorer.views.Splitter;
import org.radixware.kernel.explorer.widgets.ExplorerMenu;
import org.radixware.kernel.explorer.widgets.FilteredMouseEvent;
import org.radixware.schemas.clientstate.ClientState;
import org.radixware.schemas.clientstate.ExplorerTreeState;


public final class ExplorerTreeManager implements IExplorerTreeManager {

    private static class TreeWindow extends MainWindow {

        private final ExplorerTree explorerTree;
        private final QToolBar toolBar;
        private final List<QMenu> menus = new LinkedList<>();

        public TreeWindow(final ExplorerTree tree) {
            super();
            setContextMenuPolicy(Qt.ContextMenuPolicy.NoContextMenu);
            setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
            setObjectName("wndExplorerTree");
            final QSizePolicy sizePolicy = new QSizePolicy(QSizePolicy.Policy.Preferred, QSizePolicy.Policy.Preferred);
            sizePolicy.setHorizontalStretch((byte) 0);
            sizePolicy.setVerticalStretch((byte) 0);
            setSizePolicy(sizePolicy);
            {
                toolBar = addToolBar("tree tool bar");
                toolBar.setObjectName("tree tool bar");
                toolBar.addAction(Application.getInstance().getActions().connect);
                WidgetUtils.updateToolButtonObjectName(toolBar, Application.getInstance().getActions().connect);
                toolBar.addAction(Application.getInstance().getActions().disconnect);
                WidgetUtils.updateToolButtonObjectName(toolBar, Application.getInstance().getActions().disconnect);
                toolBar.addSeparator();
                addActionWithMenu(tree.getActions().getGoBackAction());
                addActionWithMenu(tree.getActions().getGoForwardAction());
                {
                    final QAction qAction = (QAction)tree.getActions().getRemoveCurrentNodeAction();
                    toolBar.addAction(qAction);
                    WidgetUtils.updateToolButtonObjectName(toolBar,qAction);
                }
                
                toolBar.setFloatable(false);
                toolBar.setMovable(false);
            }
            explorerTree = tree;
            setCentralWidget(tree);
            if (style().pixelMetric(QStyle.PixelMetric.PM_DefaultFrameWidth, null, tree)==0){
                setContentsMargins(4, 0, 0, 0);
            }            
        }
        
        private void addActionWithMenu(final Action action){
            final QAction qAction = (QAction)action;
            toolBar.addAction(qAction);
            WidgetUtils.updateToolButtonObjectName(toolBar, qAction);
            final QMenu menu = (QMenu)action.getActionMenu();
            if (menu!=null){
                LeakedWidgetsDetector.getInstance().registerTopLevelWidget(menu);
                menus.add(menu);
            }
        }
        
        public void setToolBarIconSize(final int width, final int height){
            if (toolBar.iconSize().width()!=width || toolBar.iconSize().height()!=height){
                toolBar.setIconSize(new QSize(width,height));
            }
        }
        
        public ExplorerTree getExplorerTree() {
            return explorerTree;
        }

        @Override
        protected void closeEvent(final QCloseEvent event) {
            super.closeEvent(event);
            for (QMenu menu: menus){
                menu.disposeLater();
            }
        }
        
        
    }
    
    private final static class FileDialogHandler implements QFileDialog.DialogHandler{
        /*On OSX when a native file dialog is opened, shortcuts for actions inside the dialog (cut, paste, ...)
          continue to go through the same menu items which claimed those shortcuts.
          http://forums.macrumors.com/showthread.php?t=1249452. 
          So we need to add menu items to make those shortcuts work.
        */
        private final QMenu editorMenu;
        private final List<QAction> actions = new LinkedList<>();
        private final QAction copy = new QAction(null);
        private final QAction cut = new QAction(null);
        private final QAction paste = new QAction(null);
        private final QAction selectAll = new QAction(null);
        private QAction separator;
        
        public FileDialogHandler(final QMenu editorMenu){
            this.editorMenu = editorMenu;
            copy.setShortcut(QKeySequence.StandardKey.Copy);
            actions.add(copy);
            cut.setShortcut(QKeySequence.StandardKey.Cut);
            actions.add(cut);
            paste.setShortcut(QKeySequence.StandardKey.Paste);
            actions.add(paste);
            selectAll.setShortcut(QKeySequence.StandardKey.SelectAll);
            actions.add(selectAll);
            for (QAction action: actions){
                action.setMenuRole(QAction.MenuRole.TextHeuristicRole);
            }
            translate();
        }
                
        public void translate(){
            copy.setText(com.trolltech.qt.core.QCoreApplication.translate("QMenuBar","Copy"));
            cut.setText(com.trolltech.qt.core.QCoreApplication.translate("QMenuBar","Cut"));
            paste.setText(com.trolltech.qt.core.QCoreApplication.translate("QMenuBar","Paste"));
            selectAll.setText(com.trolltech.qt.core.QCoreApplication.translate("QMenuBar","Select All"));
        }

        @Override
        public List<String> beforeOpen(final QFileDialog.AcceptMode mode, 
                                       final String caption, 
                                       final String dir, 
                                       final String filter, 
                                       final QFileDialog.Options options) {
            separator = editorMenu.isEmpty() ? null : editorMenu.addSeparator();
            for (QAction action: actions){
                editorMenu.addAction(action);
            }
            return null;
        }

        @Override
        public List<String> afterOpen(final QFileDialog.AcceptMode mode, 
                                      final String caption, 
                                      final String dir, 
                                      final String filter, 
                                      final QFileDialog.Options options, 
                                      final List<String> result) {
            for (QAction action: actions){
                editorMenu.removeAction(action);
            }
            if (separator!=null){
                editorMenu.removeAction(separator);
                separator = null;
            }
            return result;
        }
        
    }
    
    private final class InternalBlockableWidget extends BlockableWidget{
        
        public InternalBlockableWidget(final IClientEnvironment environment){
            super(environment);
        }

        @Override
        protected void filteredMouseEvent(final FilteredMouseEvent event) {
            if (treeWindow!=null 
                && (event.getFilteredEventType()==QEvent.Type.MouseButtonPress || event.getFilteredEventType()==QEvent.Type.MouseButtonDblClick)){
                event.accept();
                QApplication.sendEvent(treeWindow.getExplorerTree(), new FilteredMouseEvent(event));
            }
        }
    }

    public final class Actions {

        public final QAction changeRoot;

        @SuppressWarnings("LeakingThisInConstructor")
        public Actions() {
            super();
            changeRoot = new QAction(null);
            changeRoot.setIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.TREES));
            changeRoot.triggered.connect(this, "changeRootAction()");
            translate();
        }

        @SuppressWarnings("unused")
        private void changeRootAction() {
            if (treeWindow != null) {
                final QMainWindow parentWindow = (QMainWindow) treeWindow.window();
                final ExplorerRoot explorerRoot = findExplorerRootById(Application.getConnectionOptions().selectExplorerRootId());
                if (explorerRoot != null && closeAll(false)) {
                    try {
                        ExplorerTreeManager.this.openTreeImpl(explorerRoot, (IWidget) parentWindow);
                    } catch (InterruptedException ex) {
                    } catch (ServiceClientException ex) {
                        environment.processException(ex);
                    }
                }
            }
        }

        public void translate() {
            changeRoot.setText(Application.translate("ExplorerTree", "Change Explorer Root"));
            changeRoot.setToolTip(Application.translate("ExplorerTree", "change explorer root"));
        }
    }
        
    
    public final Actions actions = new Actions();    
    private final IProgressHandle openingTreeProgress;
    private List<ExplorerRoot> explorerRoots;
    private IViewManager viewManager;
    private TreeWindow treeWindow;
    private MainWindow mainWindow;
    private Qt.AlignmentFlag treeAlignment;
    private ExplorerMenu treeMenu;
    private ExplorerMenu selectorMenu;
    private ExplorerMenu editorMenu;
    private FileDialogHandler fileDialogHandler;
    
    private final IClientEnvironment environment;

    public ExplorerTreeManager(final IClientEnvironment environment) {
        this.environment = environment;        
        openingTreeProgress = environment.getProgressHandleManager().newProgressHandle();
    }

    @Override
    public IExplorerTree openTree(final List<ExplorerRoot> explorerRoots, final IWidget parentWindow, final ClientState clientState) throws ServiceClientException, InterruptedException {
        if (treeWindow != null) {
            throw new UnsupportedOperationException("opening of second explorer tree is not supported yet");
        }
        if (explorerRoots.isEmpty()) {
            return null;
        }
        this.explorerRoots = explorerRoots;        
        final ExplorerTreeState treeState = clientState==null ? null : clientState.getExplorerTree();        
        Id storedExplorerRootId = null;
        if (treeState==null){
            storedExplorerRootId = restoreExplorerRootId();
        }else{
            final List<Id> storedExplorerRootIds = treeState.getRootExplorerItemIds();
            if (storedExplorerRootIds!=null && !storedExplorerRootIds.isEmpty()){
                for (ExplorerRoot root: explorerRoots){
                    if (storedExplorerRootIds.contains(root.getId())){
                        storedExplorerRootId = root.getId();
                        break;
                    }
                }
            }
        }
        ExplorerRoot explorerRoot = storedExplorerRootId==null ? null : findExplorerRootById(storedExplorerRootId);
        
        if (explorerRoot==null && ExplorerRoot.getDefault(environment) != null) {
            explorerRoot = findExplorerRootById(ExplorerRoot.getDefault(environment).getId());
        }
        if (explorerRoot==null){
            if (Application.getConnectionOptions() != null) {
                explorerRoot = findExplorerRootById(Application.getConnectionOptions().getExplorerRootId(explorerRoots));
            } else {
                explorerRoot = explorerRoots.get(0);
            }
        }
        if (explorerRoot==null){
            return null;
        }else{
            final IExplorerTree tree = openTreeImpl(explorerRoot, parentWindow);
            if (treeState!=null){
                tree.restoreStateFromXml(treeState);
            }
            return tree;
        }
    }
    
    private ExplorerRoot findExplorerRootById(final Id explorerRootId){
        for (ExplorerRoot eRoot: explorerRoots){
            if (eRoot.getId().equals(explorerRootId)){
                return eRoot;
            }
        }
        return null;
    }
    
    private Id restoreExplorerRootId(){
        if (RunParams.needToRestoreContext()){
            final ClientSettings settings = environment.getConfigStore();
            final String storedExplorerRootId;
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
            settings.beginGroup(SettingNames.ExplorerTree.COMMON_GROUP);
            try {
                storedExplorerRootId = settings.readString(SettingNames.ExplorerTree.Common.ROOT,null);
            } finally {
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
            }
            return storedExplorerRootId==null || storedExplorerRootId.isEmpty() ? null : Id.Factory.loadFrom(storedExplorerRootId);
        }
        return null;
    }
    
    public void refreshViewManagerMenus() {
        if (viewManager != null) {
            ((ExplorerViewManager)viewManager).setMenu(selectorMenu, editorMenu);
        }
    }

    
    private IExplorerTree openTreeImpl(final ExplorerRoot explorerRoot, final IWidget parentWindow) throws ServiceClientException, InterruptedException {
        final RadParagraphDef rootParagraph = explorerRoot.getParagraphDef();
        final String title = Application.translate("Wait Dialog", "Opening \'%s\'");
        openingTreeProgress.startProgress(String.format(title, rootParagraph.getTitle()), false);
        mainWindow = (MainWindow) parentWindow;
        try {
            if (viewManager == null && parentWindow != null) {
                viewManager = new ExplorerViewManager(environment, selectorMenu, editorMenu, (QWidget) parentWindow);
            }            
            final ExplorerTree tree = new ExplorerTree(environment, (QWidget) parentWindow);
            tree.setViewManager(viewManager);
            final List<Id> visibleExplorerItems = explorerRoot.getVisibleExplorerItems();
            treeWindow = new TreeWindow(tree);            
            final Splitter splitter;
            if (parentWindow != null) {
                splitter = initSplitter(false,true);
                splitter.setVisible(false);
            } else {
                splitter = null;
            }
            try {
                final RootParagraphNode node = new RootParagraphNode(tree, rootParagraph);
                openingTreeProgress.setText(String.format(title, rootParagraph.getTitle()));
                tree.open(node, visibleExplorerItems, explorerRoot);
                if (treeMenu != null) {
                    initTreeMenu();
                    treeMenu.setEnabled(true);
                }
            } finally {
                if (splitter != null) {
                    splitter.setVisible(true);
                }
            }            
            Application.getInstance().getActions().settingsChanged.connect(this, "applySettings()");
            updateToolBarIconSize();
            initSplitter(true,false);
            return tree;
        } finally {
            openingTreeProgress.finishProgress();
        }
    }
    
    @SuppressWarnings("unused")
    private void applySettings(){
        initSplitter(false,false);
        updateToolBarIconSize();          
    }
    
    private void updateToolBarIconSize(){
        if (treeWindow!=null){
            final ClientSettings settings =  this.environment.getConfigStore();
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.SELECTOR_GROUP);
            settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
            try{
                final Dimension iconSize = settings.readSize(SettingNames.Selector.Common.ICON_SIZE_IN_SELECTOR_TOOLBARS);
                if (iconSize!=null){
                    treeWindow.setToolBarIconSize(iconSize.width, iconSize.height);
                }
            }finally{
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
            }
        }
    }

    @Override
    public IExplorerTree getCurrentTree() {
        return treeWindow != null ? treeWindow.getExplorerTree() : null;
    }

    @Override
    public void updateVersion(final Collection<Id> changedDefinitions) throws CantUpdateVersionException {
        //check if all data saved in opened views
        final AdsVersion version = environment.getApplication().getDefManager().getAdsVersion();
        if (viewManager != null && version.isSupported() && !viewManager.canSafetyClose()) {
           return;
        }
        if (treeWindow != null && !treeWindow.getExplorerTree().getRootNodes().isEmpty()) {
            boolean needForRelogin = false;
            boolean refillStarted = false;
            treeWindow.getExplorerTree().lock();
            final IProgressHandle progress = environment.getProgressHandleManager().newProgressHandle();
            progress.startProgress(Application.translate("ExplorerMessage", "Updating Version"), false);            
            try {
                final IExplorerTreeNode currentNode;
                {
                    for (IExplorerTreeNode rootNode : treeWindow.getExplorerTree().getRootNodes()) {
                        if (changedDefinitions.contains(rootNode.getView().getDefinitionId())) {
                            needForRelogin = true;
                            break;
                        }
                    }

                    currentNode = treeWindow.getExplorerTree().getCurrent();
                    if (currentNode != null) {
                        //Check if definitions linked with parent nodes (relative to current node) was changed
                        for (IExplorerTreeNode parentNode = currentNode.getParentNode(); parentNode != null; parentNode = parentNode.getParentNode()) {
                            if (changedDefinitions.contains(parentNode.getView().getDefinitionId())
                                    || changedDefinitions.contains(parentNode.getView().getDefinitionOwnerClassId())) {
                                //check for choosen entity
                                if (parentNode == currentNode.getParentNode() && (currentNode instanceof ChoosenEntityNode)) {
                                    final ChoosenEntityNode choosenEntityNode = (ChoosenEntityNode) currentNode;
                                    final ExplorerTreeNode ownerNode = choosenEntityNode.getParentNode();
                                    if (choosenEntityNode.isValid()
                                            && ownerNode.isValid()
                                            && choosenEntityNode.getView().getDefinitionOwnerClassId().equals(ownerNode.getView().getDefinitionOwnerClassId())) {
                                        continue;
                                    }
                                }
                                //find last changed and  first unchanged parent node
                                IExplorerTreeNode unchangedParentNode, lastChangedParentNode = parentNode;
                                for (unchangedParentNode = parentNode.getParentNode(); unchangedParentNode != null; unchangedParentNode = unchangedParentNode.getParentNode()) {
                                    if (!changedDefinitions.contains(unchangedParentNode.getView().getDefinitionId())
                                            && !changedDefinitions.contains(unchangedParentNode.getView().getDefinitionOwnerClassId())) {
                                        break;
                                    } else {
                                        lastChangedParentNode = unchangedParentNode;
                                    }
                                }
                                if (unchangedParentNode == null) {
                                    needForRelogin = true;
                                }
                                if (lastChangedParentNode != null && currentNode.getView()!=null) {
                                    final String title = Application.translate("ExplorerMessage", "Confirm to Update Version");
                                    final String question = Application.translate("ExplorerMessage", "Changes in \'%s\' detected.\nCurrent child item '%s' maybe absent after update.\nDo you want to continue update?");
                                    final String formattedQuestion = String.format(question, lastChangedParentNode.getView().getTitle(), currentNode.getView().getTitle());
                                    if (!Application.messageConfirmation(title, formattedQuestion)) {
                                        return;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }

                if (viewManager != null) {
                    viewManager.closeAll();
                }

                //actualize definitions manager
                try{
                    version.switchToTargetVersion();
                }catch(CantUpdateVersionException exception){
                    if (exception instanceof KernelClassModifiedException==false){
                        environment.processException(exception);
                    }
                    if (version.isSupported() && viewManager!=null && currentNode!=null && currentNode.isValid()){
                        //Reopening current node
                        viewManager.openView(currentNode, true);
                    }
                }
                //refill tree
                refillStarted = true;
                treeWindow.getExplorerTree().refill(progress, needForRelogin);
                refillStarted = false;
            } catch (BrokenConnectionError error) {
                environment.messageError(Application.translate("ExplorerMessage", "Can't Update Version"), error.getMessage());                
                Application.getInstance().getActions().forcedDisconnect.trigger();
            } finally {
                progress.finishProgress();
                if (refillStarted){//Some uncatched exception in refill
                    Application.getInstance().getActions().forcedDisconnect.trigger();                    
                }else if (treeWindow != null) {
                    treeWindow.getExplorerTree().unlock();
                }
            }
        } else {
            version.switchToTargetVersion();
        }
    }

    private boolean needToRemoveNode(final IExplorerTreeNode node, final Collection<Pid> removedEntitiesPids) {
        if (node.isValid() && node.getView().isEntityView()) {
            final EntityModel entity = (EntityModel) node.getView().getModel();
            if (removedEntitiesPids.contains(entity.getPid())) {
                return true;
            }
            if (entity.getContext() instanceof IContext.ReferencedChoosenEntityEditing) {
                //if entity was inserted into tree from property reference
                //we must check if property owner was not removed
                final IContext.ReferencedChoosenEntityEditing choosenEntityContext =
                        (IContext.ReferencedChoosenEntityEditing) entity.getContext();
                if (removedEntitiesPids.contains(choosenEntityContext.ownerPid)) {
                    return true;
                }
                //then we must check if some parent for owner was removed
                for (Pid removedEntityPid : removedEntitiesPids) {
                    if (choosenEntityContext.ownerEntityIsChildFor(removedEntityPid)) {
                        return true;
                    }
                }
            } else if (entity.getContext() instanceof IContext.ChoosenEntityEditing) {
                final IContext.ChoosenEntityEditing choosenEntityContext = (IContext.ChoosenEntityEditing) entity.getContext();
                for (Pid removedEntityPid : removedEntitiesPids) {
                    if (choosenEntityContext.isChildEntity(removedEntityPid)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean nodeInCurrentContext(final IExplorerTree tree, final IExplorerTreeNode node) {
        for (IExplorerTreeNode contextNode = tree.getCurrent(); contextNode != null; contextNode = contextNode.getParentNode()) {
            if (contextNode.getIndexInExplorerTree() == node.getIndexInExplorerTree()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterEntitiesRemoved(final Collection<Pid> removedEntitiesPids, final IWidget currentView) {//RADIX-2425
        if (treeWindow != null
                && !treeWindow.getExplorerTree().getRootNodes().isEmpty()
                && removedEntitiesPids != null
                && !removedEntitiesPids.isEmpty()) {
            final boolean saveCurrentNode = currentView != null && ((IExplorerView) currentView).asQWidget() != null;
            final ExplorerTree tree = treeWindow.getExplorerTree();
            for (IExplorerTreeNode rootNode : tree.getRootNodes()) {
                final List<IExplorerTreeNode> allNodes = rootNode.getChildNodesRecursively();
                for (IExplorerTreeNode node : allNodes) {
                    if (needToRemoveNode(node, removedEntitiesPids) && (!saveCurrentNode || !nodeInCurrentContext(tree, node))) {
                        tree.removeNode(node);
                    }
                }
            }
        }
    }

    @Override
    public IExplorerTree openSubTree(final IExplorerTreeNode node, final IWidget parentWindow) {
        final ExplorerTree sourceTree = (ExplorerTree) node.getExplorerTree();
        return sourceTree.openSubTree((ExplorerTreeNode) node, (QWidget) parentWindow);
    }

    @Override
    public void writeStateToXml(final ClientState xml) {
        if (treeWindow!=null){
            xml.setExplorerTree(treeWindow.getExplorerTree().writeStateToXml());
        }
    }
        
    @Override
    public boolean closeAll(final boolean forced) {
        if (treeWindow != null && !treeWindow.getExplorerTree().closeModel(forced) && !forced){
            return false;
        }
        saveSplitterPos();
        Application.getInstance().getActions().settingsChanged.disconnect(this);
        if (viewManager != null) {
            viewManager.closeAll();
        }
        if (treeWindow != null) {
            if (treeWindow.getExplorerTree().isLocked()) {
                treeWindow.getExplorerTree().unlockForcedly();
            }  
            treeWindow.getExplorerTree().close();
            final QWidget parent = treeWindow.parentWidget();
            treeWindow.close();
            treeWindow = null;
            ((Application)environment.getApplication()).getActions().settingsChanged.disconnect(this);
            if (parent != null) {
                parent.repaint();//repaint parent with no splitter
            }
            if (treeMenu != null) {
                treeMenu.setDisabled(true);
            }            
        }
        mainWindow = null;
        if (fileDialogHandler!=null){
            QFileDialog.removeDialogHandler(fileDialogHandler);
            fileDialogHandler = null;
        }        
        return true;
    }

    public void setupMainMenu(final QMenuBar menuBar) {

        treeMenu = new ExplorerMenu(Application.translate("MainMenu", "&Tree"),menuBar);
        menuBar.addMenu(treeMenu);
        treeMenu.setDisabled(true);

        selectorMenu = new ExplorerMenu(Application.translate("MainMenu", "&Selector"),menuBar);
        menuBar.addMenu(selectorMenu);
        selectorMenu.setDisabled(true);

        editorMenu = new ExplorerMenu(Application.translate("MainMenu", "&Editor"),menuBar);
        menuBar.addMenu(editorMenu);
        editorMenu.setDisabled(true);
        if (SystemTools.isOSX){
            fileDialogHandler = new FileDialogHandler(editorMenu);
            QFileDialog.installDialogHandler(fileDialogHandler);
        }
    }

    @Override
    public void translate() {
        if (treeMenu!=null){
            treeMenu.setTitle(Application.translate("MainMenu", "&Tree"));
        }
        if (selectorMenu!=null){
            selectorMenu.setTitle(Application.translate("MainMenu", "&Selector"));
        }
        if (editorMenu!=null){
            editorMenu.setTitle(Application.translate("MainMenu", "&Editor"));
        }
        actions.translate();
        if (fileDialogHandler!=null){
            fileDialogHandler.translate();
        }
    }

    private void initTreeMenu() {
        if (treeWindow != null) {
            treeMenu.clear();
            treeMenu.removeAllActions();
            final ExplorerTree tree = treeWindow.getExplorerTree();
            treeMenu.addAction(tree.getActions().getRemoveCurrentNodeAction());
            treeMenu.addAction(tree.getActions().getRemoveChildChoosenObjectsAction());
            treeMenu.addAction(tree.getActions().getGoToParentNodeAction());
            treeMenu.addAction(tree.getActions().getGoToCurrentNodeAction());
            treeMenu.addSeparator();
            treeMenu.addAction(actions.changeRoot);
        }
    }

    private Splitter initSplitter(final boolean restorePosition, final boolean forceRecreate) {
        if (mainWindow == null) {
            return null;
        }
        
        final ExplorerSettings settings = (ExplorerSettings)environment.getConfigStore();
        final Qt.AlignmentFlag treeArea;
        double splitterPosFactor;
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
        try {
            splitterPosFactor = settings.readDouble(SettingNames.ExplorerTree.SPLITTER_POSITION, 1. / 3.);
            if (splitterPosFactor == 0) {
                splitterPosFactor = 1. / 3.;
            }
            settings.beginGroup(SettingNames.ExplorerTree.COMMON_GROUP);
            try {
                treeArea = settings.readAlignmentFlag(SettingNames.ExplorerTree.Common.TREE_AREA, Qt.AlignmentFlag.AlignLeft);
            } finally {
                settings.endGroup();
            }
        } finally {
            settings.endGroup();
            settings.endGroup();
        }
        
        final boolean treeAlignmentChanged = treeAlignment!=treeArea  || getSplitter()==null;
        final Splitter splitter;
        final BlockableWidget blockableWidget;
        final boolean needToCreateSplitter = treeAlignmentChanged || forceRecreate;
        if (needToCreateSplitter){
            if (mainWindow.centralWidget() != null) {
                blockableWidget = (BlockableWidget) mainWindow.centralWidget();
                final Splitter currentSplitter = (Splitter) blockableWidget.getWidget();
                if (currentSplitter != null) {
                    splitterPosFactor = saveSplitterPos();
                    QApplication.removePostedEvents(currentSplitter, QEvent.Type.User.value());
                    currentSplitter.disposeLater();
                }
            } else {
                blockableWidget = new InternalBlockableWidget(environment);
                mainWindow.setCentralWidget(blockableWidget);
            }

            splitter = new Splitter(blockableWidget,settings);
            if (treeArea==Qt.AlignmentFlag.AlignRight || treeArea==Qt.AlignmentFlag.AlignLeft){
                splitter.setOrientation(Qt.Orientation.Horizontal);
            }else{
                splitter.setOrientation(Qt.Orientation.Vertical);
            }
            final int uncollapsibleIndex;
            if (treeArea==Qt.AlignmentFlag.AlignRight || treeArea==Qt.AlignmentFlag.AlignBottom){
                splitter.addWidget((QWidget) viewManager);
                splitter.addWidget(treeWindow);
                uncollapsibleIndex = 0;
            }else{
                splitter.addWidget(treeWindow);
                splitter.addWidget((QWidget) viewManager);
                uncollapsibleIndex = 1;
            }
            splitter.setCollapsible(uncollapsibleIndex, false);            
        }else{
            splitter = getSplitter();
            if (splitter.count()==1){
                if (treeArea==Qt.AlignmentFlag.AlignRight || treeArea==Qt.AlignmentFlag.AlignBottom){
                    splitter.addWidget(treeWindow);                    
                }else{
                    splitter.insertWidget(0, treeWindow);
                }
            }
            blockableWidget = (BlockableWidget) mainWindow.centralWidget();
        }        

        final int splitterPos;
        switch (treeArea) {
            case AlignRight: {
                splitterPos = mainWindow.width() - (int) (mainWindow.width() * splitterPosFactor);
                break;
            }
            case AlignTop: {
                splitterPos = (int) (mainWindow.height() * splitterPosFactor);
                break;
            }
            case AlignBottom: {
                splitterPos = mainWindow.height() - (int) (mainWindow.height() * splitterPosFactor);
                break;
            }
            default: {
                splitterPos = (int) (mainWindow.width() * splitterPosFactor);
            }
        }
        if (needToCreateSplitter){
            treeAlignment = treeArea;
            blockableWidget.setWidget(splitter);
        }
        if (restorePosition || needToCreateSplitter){
            if (treeAlignmentChanged){
                splitter.moveToPositionLater(splitterPos);
            }else{
                splitter.moveToPosition(splitterPos);
            }
        }
        return splitter;
    }
    
    
    private double saveSplitterPos() {
        if (mainWindow != null && mainWindow.centralWidget() != null) {
            final Splitter splitter = getSplitter();
            final ExplorerSettings settings = (ExplorerSettings)environment.getConfigStore();                        
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
            try {
                final double splitterPosFactor;
                final int splitterPos;
                switch (treeAlignment) {
                    case AlignRight: {
                        splitterPos = mainWindow.width() - splitter.getCurrentPosition();
                        break;
                    }
                    case AlignBottom: {
                        splitterPos = mainWindow.height() - splitter.getCurrentPosition();
                        break;
                    }
                    default: {
                        splitterPos = splitter.getCurrentPosition();
                    }
                }
                if (splitter.orientation() == Qt.Orientation.Horizontal) {
                    splitterPosFactor = (double) splitterPos / (double) mainWindow.width();
                } else {
                    splitterPosFactor = (double) splitterPos / (double) mainWindow.height();
                }
                settings.writeDouble(SettingNames.ExplorerTree.SPLITTER_POSITION, splitterPosFactor);
                return splitterPosFactor;
            } finally {
                settings.endGroup();
                settings.endGroup();
            }
        }
        return 0;
    }
    
    private Splitter getSplitter(){
        if (mainWindow!=null && mainWindow.centralWidget() instanceof BlockableWidget){
            return (Splitter)((BlockableWidget) mainWindow.centralWidget()).getWidget();
        }else{
            return null;
        }
    }
}