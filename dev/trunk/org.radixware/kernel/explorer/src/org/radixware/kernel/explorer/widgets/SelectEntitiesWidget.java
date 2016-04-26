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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.QSignalEmitter.Signal0;
import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QBoxLayout;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.GroupRestrictions;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.explorer.dialogs.EntityEditorDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.views.selector.Selector;


public class SelectEntitiesWidget extends QWidget {
    
    private final class ListWidget extends QListWidget {
        public final Signal0 focusChanged = new Signal0();
        
        public ListWidget(final QWidget parent) {
            super(parent);
        }
        
        @Override
        protected void focusInEvent(QFocusEvent event) {
            super.focusInEvent(event);
            focusChanged.emit();
        }

        @Override
        protected void focusOutEvent(QFocusEvent event) {
            super.focusOutEvent(event);
            focusChanged.emit();
        }

        @Override
        protected void keyPressEvent(final QKeyEvent event) {
            final boolean ctrl = event.modifiers().isSet(Qt.KeyboardModifier.ControlModifier);
            final int key = event.key();
            if(ctrl) {
                if(key == Qt.Key.Key_Up.value()) {
                    SelectEntitiesWidget.this.moveUp();
                } else if(key == Qt.Key.Key_Down.value()){
                    SelectEntitiesWidget.this.moveDown();
                } else if(key == Qt.Key.Key_Delete.value()) {
                    SelectEntitiesWidget.this.clearAllSelection();
                }
                return;
            }
            
            if(key == Qt.Key.Key_Delete.value()) {
                SelectEntitiesWidget.this.removeObject();
                return;
            }
            
            super.keyPressEvent(event);
        }

        @Override
        protected void mouseDoubleClickEvent(QMouseEvent event) {
            final QListWidgetItem item = itemAt(event.pos());
            
            if(item != null) {
                final EntityModel entity = (EntityModel) item.data(Qt.ItemDataRole.UserRole);
                new EntityEditorDialog(entity).execDialog();                
            } else {            
                super.mouseDoubleClickEvent(event);
            }
        }        
    }
    
    private class EventListener extends QEventFilter{
        
        public EventListener(final QObject parent){
            super(parent);
            setProcessableEventTypes(EnumSet.of(QEvent.Type.KeyPress));
        }
        
        @Override
        public boolean eventFilter(final QObject target, final QEvent event) {
            if(target==selector) {//NOPMD                
                if(event instanceof QKeyEvent) {
                    final QKeyEvent keyPress = (QKeyEvent) event;
                    final boolean ctrl = keyPress.modifiers().isSet(Qt.KeyboardModifier.ControlModifier);

                    final int key = keyPress.key();
                    if(ctrl) {
                        if(key == Qt.Key.Key_Right.value()) {//NOPMD
                            selectObject();
                            return true;
                        }
                    }
                }
            }
            return false;
        }        
        
    }
       
    private final static Qt.Alignment TOP_ALIGN = new Qt.Alignment(AlignmentFlag.AlignTop);
    private enum ButtonType { L2R, R2L, UP, DOWN, CLEAR}; // left-to-right, right-to-left
    
    private final GroupModel groupModel;
    private final Selector selector;
    private final ListWidget list = new ListWidget((QWidget)this);
    private final EventListener eventListener = new EventListener(list);
       
    private QToolButton btnAddToList;
    private QToolButton btnRemoveFromList;
    private QToolButton btnMoveUp;
    private QToolButton btnMoveDown;
    private QToolButton btnClear;
    private boolean showListSorters = true;
    private QBoxLayout sortersLayout;
    
    public SelectEntitiesWidget(final GroupModel groupModel, final QWidget parent, final boolean enableSplitter) {
        super(parent);
        this.groupModel = groupModel;
        final GroupRestrictions restrictions = this.groupModel.getRestrictions();
        if(restrictions != null) {
            restrictions.setEditorRestricted(true);
            
        }
        selector = (Selector) groupModel.createView();
        selector.setSizePolicy(Policy.Minimum, Policy.Maximum);        
        setUpUi(enableSplitter);
        selector.installEventFilter(eventListener);
    }
    
    private void setUpUi(final boolean enableSplitter) {
        final QBoxLayout mainLayout = new QHBoxLayout();
        mainLayout.setAlignment(TOP_ALIGN);
        ((QWidget)this).setLayout(mainLayout);
        mainLayout.setMargin(0);
        
        if(enableSplitter) {
            final CustomSplitter splitter = new CustomSplitter(this);
            splitter.addWidget(buildLeftPanel());
            splitter.addWidget(buildRightPanel());
            buildButtons((CustomSplitter.CustomSplitterHandle)splitter.handle(1));
            
            mainLayout.addWidget(splitter);
            splitter.setSizePolicy(Policy.Preferred, Policy.Expanding);
           
        } else {
            mainLayout.addWidget(buildLeftPanel());
            final QLayout buttons = buildButtons();
            mainLayout.addLayout(buttons);
            ((QBoxLayout)buttons).insertSpacing(0, new QLabel("").sizeHint().height() + buttons.widgetSpacing());
            mainLayout.addWidget(buildRightPanel());
        }
        
//        selector.setFocus();
//        watchButtons();
    }
    
    private QLayout buildButtons() {
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setAlignment(TOP_ALIGN);
        btnAddToList = createButton(ButtonType.L2R);
        layout.addWidget(btnAddToList);
        btnRemoveFromList = createButton(ButtonType.R2L);
        layout.addWidget(btnRemoveFromList);
       
        return layout;
    }
    
    private void buildButtons(final CustomSplitter.CustomSplitterHandle handle) {
        btnAddToList = createButton(ButtonType.L2R);
        handle.addWidget(btnAddToList);
        btnRemoveFromList = createButton(ButtonType.R2L);
        handle.addWidget(btnRemoveFromList);
        handle.alignWigetsInside();
    }
    
    @SuppressWarnings("PMD.MissingBreakInSwitch")
    private QToolButton createButton(final ButtonType btnType) {
        final QToolButton button = new QToolButton(this);
        QIcon icon;
        switch(btnType) {
            case L2R:
                icon = ExplorerIcon.getQIcon(ClientIcon.CommonOperations.RIGHT);
                button.clicked.connect((QWidget)this, "selectObject()");
                break;
            case R2L:
                icon = ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DELETE);
                button.clicked.connect((QWidget)this, "removeObject()");
                break;
            case UP:
                icon = ExplorerIcon.getQIcon(ClientIcon.CommonOperations.UP);
                button.clicked.connect((QWidget)this, "moveUp()");
                break;
            case DOWN:
                icon = ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DOWN);
                button.clicked.connect((QWidget)this, "moveDown()");
                break;
            case CLEAR:
                icon = ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CLEAR);
                button.clicked.connect((QWidget)this, "clearAllSelection()");
                break;
            default:
                icon = null;
        }
        button.setIcon(icon);
        button.setCursor(new QCursor(Qt.CursorShape.ArrowCursor));
        return button;
    }
    
    private QWidget buildLeftPanel() {
        final QWidget panel = new QWidget((QWidget)this);
        final QLayout layout = new QVBoxLayout();
        layout.setMargin(0);
        layout.setAlignment(TOP_ALIGN);
        panel.setLayout(layout);
        final String title = groupModel.getEnvironment().getMessageProvider().translate("SelectorAddons", "Available %s:");
        final QLabel lblAvailable = new QLabel(
                String.format(title, ClientValueFormatter.capitalizeIfNecessary(groupModel.getEnvironment(), groupModel.getTitle().toLowerCase())),
                this);
        layout.addWidget(lblAvailable);
        layout.addWidget(selector);
        lblAvailable.setBuddy(selector);
        selector.setSizePolicy(Policy.Ignored, Policy.Preferred);
        selector.entityActivated.connect(this, "onEntityActivated(EntityModel)");
        selector.onSetCurrentEntity.connect(this, "listContainsEntityWatcher(org.radixware.kernel.common.client.models.EntityModel)");
        selector.onFocusChanged.connect(this, "watchButtons()");
        
        final ExplorerMenu selectorMenu = new ExplorerMenu(groupModel.getEnvironment().getMessageProvider().translate("MainMenu", "&Selector"));
        selector.setMenu(selectorMenu, null);
        selector.open(groupModel);
        return panel;
    }
    
    private QWidget buildRightPanel() {
        final QWidget panel = new QWidget((QWidget)this);
        final QBoxLayout layout = new QHBoxLayout();
        layout.setAlignment(TOP_ALIGN);
        layout.setMargin(0);
        panel.setLayout(layout);
        
        final QLayout innerLayout = new QVBoxLayout();
        innerLayout.setAlignment(TOP_ALIGN);
        innerLayout.setMargin(0);
        final String title = groupModel.getEnvironment().getMessageProvider().translate("SelectorAddons", "Selected %s:");
        final QLabel lblSelected = new QLabel(
                String.format(title, ClientValueFormatter.capitalizeIfNecessary(groupModel.getEnvironment(), groupModel.getTitle().toLowerCase())),
                this);
        
        innerLayout.addWidget(lblSelected);
        innerLayout.addWidget(list);
        lblSelected.setBuddy(list);
        
        sortersLayout = new QVBoxLayout();
        sortersLayout.setAlignment(TOP_ALIGN);
        btnClear = createButton(ButtonType.CLEAR);
        btnMoveUp = createButton(ButtonType.UP);
        btnMoveUp.setVisible(showListSorters);
        btnMoveDown = createButton(ButtonType.DOWN);
        btnMoveDown.setVisible(showListSorters);
        sortersLayout.addWidget(btnClear);
        sortersLayout.addWidget(btnMoveUp);
        sortersLayout.addWidget(btnMoveDown);
        layout.addLayout(innerLayout);
        layout.addLayout(sortersLayout);
        sortersLayout.insertSpacing(0, new QLabel("").sizeHint().height() + sortersLayout.widgetSpacing());

        list.setSizePolicy(Policy.Ignored, Policy.Expanding);
        list.itemSelectionChanged.connect((QWidget)this, "watchButtons()");
        list.focusChanged.connect((QWidget)this, "watchButtons()");
        
        return panel;
    }
    
    private void onEntityActivated(final EntityModel entity) {
        if(entity != null && !listContainsEntity(entity)) {
            final QListWidgetItem newListItem = new QListWidgetItem(entity.getTitle());
            newListItem.setData(Qt.ItemDataRole.UserRole, entity);
            newListItem.setIcon(ExplorerIcon.getQIcon(entity.getIcon()));
            list.addItem(newListItem);
            btnAddToList.setDisabled(true);
        }
    }
    
            
    private void selectObject() {
        final EntityModel currentEntity = selector.getCurrentEntity();
        onEntityActivated(currentEntity);
    }
    
    private void removeObject() {
        final int currentItem = list.currentRow();
        if(currentItem > -1) {
            final QListWidgetItem item = list.takeItem(currentItem);
            listContainsEntityWatcher((EntityModel)item.data(Qt.ItemDataRole.UserRole));
            item.dispose();
            
        }
        watchButtons();
    }
    
    private void clearAllSelection() {
        final IClientEnvironment env = groupModel.getEnvironment();
        final String title = env.getMessageProvider().translate("ExplorerDialog", "Confirmation");
        final String message = env.getMessageProvider()
                .translate("SelectEntityDialog", "Do you want to reset selection?");
        
        if(env.messageConfirmation(title, message)) {
            list.clear();
        }
    }
    
    private void listContainsEntityWatcher(final EntityModel entityModel) {
        if(btnAddToList != null) {
            btnAddToList.setDisabled(listContainsEntity(entityModel));
        }
    }
    
    private boolean listContainsEntity(final EntityModel entityModel) {
        for(int i = 0; i < list.count(); i++) {
            final EntityModel entity = (EntityModel) list.item(i).data(Qt.ItemDataRole.UserRole);
            if(entity.equals(entityModel)) {
                return true;
            }
        }
        return false;
    }
    
    private void watchButtons() {
        final EntityModel currentEntity = selector.getCurrentEntity();
        btnAddToList.setEnabled(
                /*selector.hasFocus() 
                && */groupModel.getEntitySelectionController().isEntityChoosable(currentEntity)
                && !listContainsEntity(currentEntity));
        btnRemoveFromList.setEnabled(list.hasFocus() && list.count() > 0);
        if(showListSorters) {
            btnMoveDown.setEnabled(list.hasFocus() && list.currentRow() > -1 && list.currentRow() < list.count() - 1);
            btnMoveUp.setEnabled(list.hasFocus() && list.currentRow() > 0);
        }
    }
    
    /**
     * Returns selection as list of entity models
     * @return 
     */
    public List<EntityModel> getEntities() {
        final List<EntityModel> result = new ArrayList<>();
        final int size = list.count();
        final QAbstractItemModel model = list.model();
        for(int i = 0; i < size; i++) {
            result.add( (EntityModel)model.data(i, 0, Qt.ItemDataRole.UserRole) );
        }
        return result;
    }
    
    /**
     * Returns selection as array of references
     * @return 
     */
    public ArrRef getReferences() {
        final ArrRef array = new ArrRef();
        final int size = list.count();
        final QAbstractItemModel model = list.model();
        for(int i = 0; i < size; i++) {
            final EntityModel entity = (EntityModel)model.data(i, 0, Qt.ItemDataRole.UserRole);
            array.add( new Reference(entity.getPid()) );
        }
        return array;
    }

    @SuppressWarnings("unused")    
    private void moveUp() {
        move(-1);
    }
    
    @SuppressWarnings("unused")
    private void moveDown() {
        move(1);
    }
    
    private void move(final int step) {
        final int currentIndex = list.currentRow();
        final QListWidgetItem currentItem = list.takeItem(currentIndex);
        list.insertItem(currentIndex + step, currentItem);
        list.setCurrentRow(currentIndex + step);
        watchButtons();
    }
    
    /**
     * Shows or hides sort buttons
     * @param visible 
     */
    public void setShowSorters(final boolean visible) {
        btnMoveDown.setVisible(visible);
        btnMoveUp.setVisible(visible);
//        sortersLayout.setContentsMargins(visible ? defaultMargin : new QContentsMargins(0, 0, 0, 0));
    }
    
    /**
     * Sets or reset a list of selected values
     * @param entities a list of predefined values (null to reset)
     */
    public void setPredefinedEntities(final List<EntityModel> entities) {
        if(entities == null) {
            clearAllSelection();
        } else {
            for(EntityModel em : entities) {
                onEntityActivated(em);
            }
        }
    }
    
}
