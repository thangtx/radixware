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

package org.radixware.kernel.explorer.views;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QFocusEvent;


import com.trolltech.qt.core.Qt;

import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QClipboard;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMenuBar;
import com.trolltech.qt.gui.QStyle;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.IEditor;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.ICommandToolBar;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.AuditLogDialog;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ComponentModificationRegistrator;
import org.radixware.kernel.explorer.widgets.ExplorerAction;
import org.radixware.kernel.common.client.widgets.IModifableComponent;
import org.radixware.kernel.common.client.widgets.IModificationListener;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.explorer.widgets.ExplorerMenu;
import org.radixware.kernel.explorer.widgets.ExplorerToolBar;
import org.radixware.kernel.explorer.widgets.QWidgetProxy;
import org.radixware.kernel.explorer.widgets.commands.CommandToolBar;

/**
 * Базовый класс для стандартного редактора (StandardEditor) и
 * кастомных редакторов созданных DAC-дизайнером.
 * Содержит реализацию стандартных операций, панели инструментов и комманд.
 */
public abstract class Editor extends MainWindow implements IExplorerView, IEditor, IModificationListener {

    private final static String EDITOR_GROUP_NAME_KEY = SettingNames.SYSTEM + "/MainWindow";

    public final static class Icons extends ExplorerIcon.CommonOperations {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        final public static ClientIcon NEED_FOR_SAVE = ClientIcon.Editor.NEED_FOR_SAVE;
        public static final ClientIcon AUDIT = ClientIcon.Editor.AUDIT;
    }
    
    private static final class SetupMenu extends QEvent{        
        public SetupMenu(){
            super(QEvent.Type.User);
        }        
    }

    /**
     *Набор стандартых дейсвий, которые могут быть выполнены в редакторе
     */
    public final class Actions extends IEditor.Actions {

        @Override
        protected Action createAction(ClientIcon icon, String title) {
            return new ExplorerAction(ExplorerIcon.getQIcon(icon),
                    title, Editor.this);
        }

        public Actions(ActionController controller) {
            super(controller);
        }
    }
    public final Actions actions;
    protected final QWidget content = new QWidget(this);
    final public Signal1<QWidget> opened = new Signal1<>();    
    final protected Signal0 closed = new Signal0();
    final public Signal0 entityRemoved = new Signal0();
    final public Signal0 entityUpdated = new Signal0();
    final public Signal1<Boolean> modifiedStateChanged = new Signal1<>();
    private final IClientEnvironment environment;
    private final ActionController actionController;
    private final EditorController editorController;
    private final EditorUIController editorUIController;    

    protected Editor(IClientEnvironment environment) {
        this.environment = environment;
        //Редактор создается на основе QMainWindow,
        //чтобы можно было использовать QToolBar

        setCentralWidget(content);
        setContextMenuPolicy(Qt.ContextMenuPolicy.NoContextMenu);


        actionController = new ActionController(this) {

            @Override
            protected void modifiedStateChanged(boolean modified) {
                Editor.this.modifiedStateChanged.emit(Boolean.valueOf(modified));
            }

            @Override
            protected void execAuditLogDialog(IClientEnvironment env, IEditor editor, Id tableId, Pid pid, String title) {
                AuditLogDialog.execAuditLogDialog(env, (QWidget) editor, tableId, pid, title);
            }
        };
        actions = new Actions(actionController);
        editorUIController = new EditorUIController(this) {

            @Override
            protected IToolBar createToolBar() {
                final ExplorerToolBar tb = new ExplorerToolBar(EDITOR_GROUP_NAME_KEY);
                addToolBar(tb);
                return tb;
            }

            @Override
            protected ICommandToolBar createCommandToolBar(final IClientEnvironment env, final IEditor editor) {
                final CommandToolBar tb = new CommandToolBar(env, (QWidget) editor);
                addToolBar(tb);
                return tb;
            }

            @Override
            protected void putTextToSystemClipboard(final String text) {
                QApplication.clipboard().setText(text, QClipboard.Mode.Clipboard);
            }
            
            

            @Override
            protected void applySettings(IClientEnvironment env, IToolBar toolBar, ICommandToolBar commandToolBar) {
                final ExplorerSettings settings = (ExplorerSettings) env.getConfigStore();

                settings.beginGroup(SettingNames.SYSTEM);
                settings.beginGroup(SettingNames.EDITOR_GROUP);
                settings.beginGroup(SettingNames.Editor.COMMON_GROUP);
                final QSize iconSize = settings.readQSize(SettingNames.Editor.Common.ICON_SIZE_IN_TOOLBARS);
                commandToolBar.setButtonSize(iconSize.width(), iconSize.height());
                if (toolBar != null) {
                    toolBar.setIconSize(iconSize.width(), iconSize.height());
                }
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
            }

            @Override
            protected void restoreState(ClientSettings settings, String key) {
                Editor.this.restoreState(((ExplorerSettings) settings).readQByteArray(key));
            }
        };
        editorController = new EditorController(this, editorUIController) {

            @Override
            protected void entityUpdated() {
                entityUpdated.emit();
            }

            @Override
            protected void entityRemoved() {
                entityRemoved.emit();
            }

            @Override
            protected void notifyClosed() {
                closed.emit();
            }

            @Override
            protected void cleanUpEditorAfterClose(EntityModel entity) {                
                clearEditorMenu();
                WidgetUtils.closeChildrenEmbeddedViews(entity, content);
            }

            @Override
            protected List<IEmbeddedView> findChildrenViews() {
                return WidgetUtils.findExplorerViews(Editor.this);
            }            
        };
    }

    @Override
    public Actions getActions() {
        return actions;
    }

    @Override
    public final EntityModel getEntityModel() {
        return getController().getEntity();
    }

    @Override
    public Model getModel() {
        return getController().getEntity();
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return getController().getViewRestrictions();
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return environment;
    }
    
    protected IMenu editorMenu;
    private boolean selfMenu;
    private boolean isEditorMenuHidden;

    @Override
    public void setToolBarHidden(final boolean hidden) {
        editorUIController.setToolBarHidden(hidden);
    }

    @Override
    public void setCommandBarHidden(final boolean hidden) {
        editorUIController.setCommandBarHidden(hidden);
    }

    public void setToolButtonsSize(int size) {
        editorUIController.setToolButtonsSize(size);
    }

    public int getToolButtonsSize() {
        return editorUIController.getToolButtonsSize();
    }

    public void setMenu(final IMenu menu) {
        clearEditorMenu();
        editorMenu = menu;
        if (getEntityModel()!=null){//was opened
            QApplication.postEvent(this, new SetupMenu());
        }
    }
    
    private void clearEditorMenu(){
        if (editorMenu instanceof ExplorerMenu){
            ((ExplorerMenu)editorMenu).removeAllActions();
        }        
    }

    public IMenu getMenu() {
        return editorMenu;
    }

    public QToolBar getToolBar() {
        return (QToolBar) editorUIController.getToolBar();
    }

    public QToolBar getCommandBar() {
        return (QToolBar) editorUIController.getCommandBar();
    }

    private void setupMenu() {        
        selfMenu = false;
        if (editorMenu instanceof QMenu) {
            final QMenu asQMenu = (QMenu)editorMenu;
            if (asQMenu.parent() == null) {
                final QMenuBar menuBar = menuBar();
                final Qt.WindowFlags menuFlags = asQMenu.windowFlags();
                asQMenu.setParent(menuBar);
                asQMenu.setWindowFlags(menuFlags);
                if (isEditorMenuHidden){
                    menuBar.setVisible(false);
                }else{
                    menuBar.addMenu(asQMenu);
                    menuBar.setVisible(true);
                }
                selfMenu = true;
            }
            editorMenu.removeAllActions();
            editorMenu.addAction(actions.getDeleteAction());
            editorMenu.addAction(actions.getCopyAction());
            editorMenu.addAction(actions.getUpdateAction());
            editorMenu.addAction(actions.getCancelChangesAction());
            editorMenu.addAction(actions.getRereadAction());            
            if (RunParams.isDevelopmentMode()){
                editorMenu.insertSeparator(null);
                editorMenu.addAction(actions.getCopyEditorPresIdAction());
            }
            editorMenu.setEnabled(true);
        }
    }

    @Override
    public void setMenuHidden(final boolean hidden) {
        if (editorMenu != null) {            
            if (hidden) {
                menuBar().clear();
                menuBar().setVisible(false);
            } else {
                menuBar().addMenu((QMenu)editorMenu);
                menuBar().setVisible(true);
            }
            isEditorMenuHidden = hidden;
        }
    }

    public void applySettings() {
        //обновляем настройки значков в toolBar
        final ExplorerSettings settings = (ExplorerSettings) environment.getConfigStore();

        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.EDITOR_GROUP);
        settings.beginGroup(SettingNames.Editor.COMMON_GROUP);
        final QSize iconSize = settings.readQSize(SettingNames.Editor.Common.ICON_SIZE_IN_TOOLBARS);
        editorUIController.setToolButtonsSize(iconSize.height());
        settings.endGroup();
        settings.endGroup();
        settings.endGroup();
    }

    @Override
    public void open(Model model_) {
        getController().open(model_);
        afterOpen();

    }

    private void afterOpen() {
        //createToolBars();
        QApplication.postEvent(this, new SetupMenu());

        setTabOrder(content, getToolBar());
        setTabOrder(getToolBar(), getCommandBar());

        Application.getInstance().getActions().settingsChanged.connect(this, "applySettings()");
        content.setFocus();
    }

    public void open(final Model model_, final ComponentModificationRegistrator registrator) {
        open(model_);
        getController().setModificationRegistractor(registrator);
    }
    //private boolean wasClosed;

    @Override
    public boolean close(boolean forced) {
        if (getController().close(forced)) {
            super.close();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return getController().canSafelyClose(this,cleanController);
    }        

    @Override
    protected void closeEvent(QCloseEvent event) {
        getController().setClosed();
        closeHandlers.clear();
        openHandlers.clear();
        super.closeEvent(event);
    }

    @Override
    public void finishEdit() {
        getController().finishEdit();
    }

    @Override
    public boolean setFocusedProperty(final Id propertyId) {
        return false;
    }

    //Операции редактора
    @Override
    public boolean delete() throws ServiceClientException {
        return getController().delete();
    }

    @Override
    public boolean applyChanges() throws ServiceClientException, ModelException {
        return getController().applyChanges();
    }

    @Override
    public void cancelChanges() {
        getController().cancelChanges();
    }

    @Override
    public void reread() throws ServiceClientException {
//        try {
//            entity.read();
//        } catch (InterruptedException ex) {
//        }
        getController().reread();
    }

    public void copy() {
//        finishEdit();
//        entity.getEnvironment().getClipboard().push(entity);
        getController().copy();
    }

    @Override
    public void visitChildren(Visitor visitor, boolean recursively) {
        getController().visitChildren(visitor, recursively);
    }        

    @Override
    protected void focusInEvent(QFocusEvent event) {
        super.focusInEvent(event);
        content.setFocus();
    }

    @Override
    public QSize sizeHint() {//RADIX-373
        final QSize size = super.sizeHint();//без учета панели меню
        int height = size.height();
        if (selfMenu && menuWidget() != null) {
            height += menuBar().sizeHint().height();
            height += menuBar().style().styleHint(QStyle.StyleHint.SH_MainWindow_SpaceBelowMenuBar);
        }
        size.setHeight(height);
        return size;
    }

    @Override
    protected void keyPressEvent(final QKeyEvent event) {
        if (event.matches(QKeySequence.StandardKey.Save) && actions.getUpdateAction().isEnabled() && getEntityModel().isExists()) {
            actions.getUpdateAction().trigger();
        } else {
            super.keyPressEvent(event);
        }
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    //Реализация интерфейса IModificationListener
    @Override
    public void childComponentWasClosed(final IModifableComponent childComponent) {
        getController().childComponentWasClosed(childComponent);
    }

    @Override
    public void notifyComponentModificationStateChanged(final IModifableComponent childComponent, final boolean modified) {
        getController().notifyComponentModificationStateChanged(childComponent, modified);
    }

    @Override
    public void notifyPropertyModificationStateChanged(final Property property, final boolean modified) {
        getController().notifyPropertyModificationStateChanged(property, modified);
    }

    @Override
    public IView findParentView() {
        return QWidgetProxy.findParentView(this);
    }

    @Override
    public IWidget getParentWindow() {
        return QWidgetProxy.getParentWindow(this);
    }

    @Override
    public boolean hasUI() {
        return asQWidget() != null && asQWidget().nativePointer() != null;
    }

    @Override
    public void refresh() {
        actions.refresh();
    }

    @Override
    public boolean isDisabled() {
        return !asQWidget().isEnabled();
    }
    private final List<CloseHandler> closeHandlers = new LinkedList<>();
    private final CloseHandler defaultCloseHandler = new CloseHandler() {

        @Override
        public void onClose() {
            synchronized (closeHandlers) {
                for (CloseHandler c : closeHandlers) {
                    c.onClose();
                }
            }
        }
    };

    @Override
    public void addCloseHandler(CloseHandler handler) {
        synchronized (closeHandlers) {
            if (!closeHandlers.contains(handler)) {
                if (closeHandlers.isEmpty()) {
                    closed.connect(defaultCloseHandler, "onClose()");
                }
                closeHandlers.add(handler);
            }
        }
    }

    @Override
    public void removeCloseHandler(CloseHandler handler) {
        synchronized (closeHandlers) {
            closeHandlers.remove(handler);
        }
    }
    private final List<OpenHandler> openHandlers = new LinkedList<>();
    private final OpenHandler defaultOpenHandler = new OpenHandler() {

        @Override
        public void afterOpen() {
            synchronized (openHandlers) {
                for (OpenHandler c : openHandlers) {
                    c.afterOpen();
                }
            }
        }
    };

    @Override
    public void addOpenHandler(OpenHandler handler) {
        synchronized (openHandlers) {
            if (!openHandlers.contains(handler)) {
                if (openHandlers.isEmpty()) {
                    opened.connect(defaultOpenHandler, "afterOpen()");
                }
                openHandlers.add(handler);
            }
        }
    }

    @Override
    public void removeOpenHandler(OpenHandler handler) {
        synchronized (openHandlers) {
            openHandlers.remove(handler);
        }
    }

    @Override
    public void addEditorListener(EditorListener l) {
        getController().addEditorListener(l);
    }

    @Override
    public void removeEditorListener(EditorListener l) {
        getController().removeEditorListener(l);
    }

    @Override
    public EditorController getController() {
        return editorController;
    }

    @Override
    protected void customEvent(QEvent event) {
        if (event instanceof SetupMenu){
            event.accept();
            setupMenu();
        }else{
            super.customEvent(event);
        }
    }
    
    
}
