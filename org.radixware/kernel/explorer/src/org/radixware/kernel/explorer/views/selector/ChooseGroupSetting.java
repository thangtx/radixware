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

package org.radixware.kernel.explorer.views.selector;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOption;
import com.trolltech.qt.gui.QStyleOptionComboBox;
import com.trolltech.qt.gui.QStyleOptionComplex;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.Stack;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.IResponseListener;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.models.groupsettings.GroupSettings;
import org.radixware.kernel.common.client.models.groupsettings.IGroupSetting;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ImageManager;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;


public class ChooseGroupSetting<T extends IGroupSetting> extends ExplorerFrame {
    
    private static class ItemsLoadedEvent extends QEvent{
        
        public ItemsLoadedEvent(){
            super(QEvent.Type.User);
        }

    }
    
    private class ComboBox extends QComboBox implements IResponseListener{
                
        private class ComboBoxStyle extends WidgetUtils.CustomStyle{//RADIX-5375

            public ComboBoxStyle(){
                super(ComboBox.this.parentWidget());
            }

            @Override
            public QRect subControlRect(final ComplexControl control, final QStyleOptionComplex option, final int subControl, final QWidget widget) {
                if (control==ComplexControl.CC_ComboBox &&
                    subControl==QStyle.SubControl.SC_ComboBoxListBoxPopup &&
                    (widget instanceof QComboBox)){
                        final QRect resultRect = ComboBox.this.rect();
                        resultRect.setWidth(ComboBox.this.calcListBoxPopupWidth());
                        return resultRect;
                    }
                else{
                    return super.subControlRect(control, option, subControl, widget);
                }
            }
            
            @Override
            public QRect subElementRect(SubElement subElement, QStyleOption option, QWidget widget) {
                if (subElement==SubElement.SE_ComboBoxLayoutItem){
                    return option.rect();
                }
                return super.subElementRect(subElement, option, widget);
            }            
        }
        
        private final GroupSettingsTree<T> tree;
        //private IGroupSetting selectedAddon;
        private T selectedAddon;
        private boolean openSettingsManager;
        private boolean popupShown;
        private boolean dontHidePopup;
        private long startLoadingTime;
        private RequestHandle loadingSettingsRequestHandle;
        private final ComboBoxStyle style = new ComboBoxStyle();
        
        private final QLineEdit lineEdit = new QLineEdit(this) {

            @Override
            protected void mouseReleaseEvent(QMouseEvent event) {
                super.mouseReleaseEvent(event);
                showPopup();
            }
        };

        public ComboBox(final QWidget parent, 
                        final GroupSettings<T> addons, 
                        final EnumSet<GroupSettingsTree.ShowMode> showMode,
                        final String openSettingsManagerTitle){
            super(parent);
            tree = new GroupSettingsTree<T>(getEnvironment(), this, addons, showMode) {

                private QTreeWidgetItem openSettingsManagerItem;
                
                private void selectItem(final QModelIndex index){
                    if (isLoadingItems()){
                        dontHidePopup = true;
                        return;
                    }
                    if (index!=null){
                        if (model().rowCount(index) > 0) {
                            dontHidePopup = true;
                        } else {
                            if (itemFromIndex(index) == openSettingsManagerItem) {
                                openSettingsManager = true;
                            } else {
                                openSettingsManager = false;
                                selectedAddon = getSettingForIndex(index);
                            }
                        }
                    }
                }

                @Override
                protected QBrush getItemForeground(final IGroupSetting setting) {
                    return null;//use default (black) for all settings and do not check if setting is valid
                }                

                @Override
                protected void keyPressEvent(QKeyEvent event) {
                    if (event.key() == Qt.Key.Key_Enter.value() || event.key() == Qt.Key.Key_Return.value()) {
                        selectItem(currentIndex());
                        afterSelectItem();
                    }
                    super.keyPressEvent(event);
                }                                

                @Override
                protected void mousePressEvent(final QMouseEvent event) {
                    selectItem(indexAt(event.pos()));
                    super.mousePressEvent(event);
                }
                
                @Override
                protected void mouseMoveEvent(final QMouseEvent event) {
                    if (event.buttons().isSet(Qt.MouseButton.LeftButton)){                        
                        selectItem(indexAt(event.pos()));
                    }
                    super.mouseMoveEvent(event);
                }

                @Override
                protected void mouseReleaseEvent(final QMouseEvent event) {
                    selectItem(indexAt(event.pos()));
                    super.mouseReleaseEvent(event);
                }                

                @Override
                protected void mouseDoubleClickEvent(final QMouseEvent event) {
                    if (isLoadingItems()){
                        dontHidePopup = true;                        
                    }else{
                        final QModelIndex index = indexAt(event.pos());
                        if (index != null) {
                            dontHidePopup = model().rowCount(index) > 0;
                        }
                    }
                    super.mouseDoubleClickEvent(event);
                }

                @Override
                public void refill() {
                    super.refill();
                    if (addons.canOpenSettingsManager() && ChooseGroupSetting.this.canOpenSettingsManager()) {
                        openSettingsManagerItem = createServiceItem(openSettingsManagerTitle);
                        addTopLevelItem(openSettingsManagerItem);
                    }
                }

                @Override
                protected boolean settingIsVisible(T setting) {
                    return super.settingIsVisible(setting) && ChooseGroupSetting.this.isSettingVisible(setting);
                }
                               
                @Override
                public QSize sizeHint() {
                    int height = 0;
                    final Stack<QTreeWidgetItem> stack = new Stack<>();
                    for (int i = topLevelItemCount() - 1; i >= 0; i--) {
                        stack.push(topLevelItem(i));
                    }
                    QTreeWidgetItem item;
                    while (!stack.isEmpty()) {
                        item = stack.pop();
                        height += this.sizeHintForIndex(this.indexFromItem(item)).height();
                        for (int i = item.childCount() - 1; i >= 0; i--) {
                            stack.push(item.child(i));
                        }
                    }
                    height += getContentsMargins().top + getContentsMargins().bottom;
                    final int maxHeight = QApplication.desktop().availableGeometry().height() / 2;
                    return new QSize(super.sizeHint().width(), Math.min(height, maxHeight));
                }
            };
            tree.refill();             
            setModel(tree.model());
            setView(tree);   
            setFrame(false);            
            setEditable(count() > 1);            
            setSizeAdjustPolicy(QComboBox.SizeAdjustPolicy.AdjustToMinimumContentsLengthWithIcon);            
            setStyle(style);
            lineEdit.setReadOnly(true);
            //Устанавливаем свой LineEdit, чтобы иметь возможность программно
            //изменять текст (метод QComboBox.setEditedText() не работает когда editable=false).
            setLineEdit(lineEdit);
            lineEdit.setFrame(false);            
        }

        @Override
        public void hidePopup() {            
            if (popupShown) {
                if (!dontHidePopup) {                    
                    super.hidePopup();
                    popupShown = false;
                    if (!openSettingsManager) {                        
                        final Id selectedAddonId = selectedAddon != null ? selectedAddon.getId() : null;
                        final Id currentAddonId = currentAddon != null ? currentAddon.getId() : null;
                        if (!Utils.equals(selectedAddonId, currentAddonId)) {
                            currentAddon = selectedAddon;
                            addonChanged.emit(currentAddon);
                        }
                    }
                } else {
                    dontHidePopup = false;
                }
                activated();
                if (openSettingsManager) {
                    openSettingsManager = false;
                    ChooseGroupSetting.this.openSettingsManager.emit();
                }
            }
        }
        
        private void afterSelectItem(){
            if (!popupShown){
                if (openSettingsManager){
                    openSettingsManager = false;
                    ChooseGroupSetting.this.openSettingsManager.emit();                
                }
                else{
                    final Id selectedAddonId = selectedAddon != null ? selectedAddon.getId() : null;
                    final Id currentAddonId = currentAddon != null ? currentAddon.getId() : null;
                    if (!Utils.equals(selectedAddonId, currentAddonId)) {
                        currentAddon = selectedAddon;
                        addonChanged.emit(currentAddon);
                    }                
                }
            }
        }

        public void setText(final String text) {
            lineEdit.setText(text);
        }

        public boolean isMandatory() {
            return tree.addons.isObligatory();
        }
        
        private void addPleaseWaitItem(final String text){            
            final QTreeWidgetItem item = new QTreeWidgetItem(tree);
            item.setText(0, text);
            item.setTextAlignment(0, Qt.AlignmentFlag.AlignCenter.value());
            item.setFlags(Qt.ItemFlag.NoItemFlags);
            tree.addTopLevelItem(item);
        }        
        
        private void initPleaseWaitItems(){            
            tree.clear();
            tree.setRootIsDecorated(false);
            tree.setIndentation(0);
            addPleaseWaitItem("");
            addPleaseWaitItem("");
            addPleaseWaitItem(getEnvironment().getMessageProvider().translate("Wait Dialog", "Please Wait..."));
            addPleaseWaitItem("");
            addPleaseWaitItem("");
            setCurrentIndex(-1);
        }

        @Override
        public void showPopup() {            
            try{
                if (isLoadingItems()){
                    super.showPopup();
                    popupShown = true;
                    return;
                }else if (!ChooseGroupSetting.this.isSettingsLoaded()){
                    initPleaseWaitItems();
                    loadingSettingsRequestHandle = ChooseGroupSetting.this.startSettingsLoading();
                    startLoadingTime = System.currentTimeMillis();
                    if (loadingSettingsRequestHandle!=null){
                        loadingSettingsRequestHandle.addListener(this);
                        super.showPopup();
                        popupShown = true;
                        return;
                    }
                } if (!tree.addons.isEmpty()) {
                    prepareToShow();
                    final T current = getCurrentAddon();
                    super.showPopup();
                    popupShown = true;
                    if (current != null) {
                        tree.setCurrent(current);
                    }
                    activated();
                }
            }catch(RuntimeException exception){
                getEnvironment().getTracer().error(exception);
            }
        }
        
        private void prepareToShow(){
            openSettingsManager = false;
            selectedAddon = currentAddon;
            dontHidePopup = false;
            tree.refill();
            if (!isMandatory() && !ChooseGroupSetting.this.isMandatory()) {
                insertItem(0, noItemText);
            }
            tree.setMinimumHeight(tree.sizeHint().height());
        }
        
        private boolean isLoadingItems(){
            return loadingSettingsRequestHandle!=null && !loadingSettingsRequestHandle.isDone();
        }

        public boolean isPopupShown() {
            return popupShown;
        }

        public void updateStyleSheet() {
            final StringBuffer styleSheet = new StringBuffer();
            styleSheet.append("QComboBox {border: 0px;\n");
            {
                styleSheet.append("border: 0px;\n");
                styleSheet.append("padding-top: 0px;\n");
                styleSheet.append("padding-bottom: 0px;\n");
            }
            {
                styleSheet.append("}\n");                
                styleSheet.append("QAbstractItemView {\n");
                styleSheet.append("border: 2px solid darkgray;\n");
                styleSheet.append("background-color: ");
                styleSheet.append(palette().color(backgroundRole()).name());
                styleSheet.append(";\n");
            }
            if (tree.addons.isEmpty()) {
                styleSheet.append("}\n");
                styleSheet.append("QComboBox::drop-down {\n");
                styleSheet.append("width: 0px\n");
            }
            styleSheet.append("}\n");            
            setStyleSheet(styleSheet.toString());
            if (currentAddon!=null && !currentAddon.isValid()){
                lineEdit.setStyleSheet("color: red");
            }    
            else{
                lineEdit.setStyleSheet("");
            }            
        }
        
        private int calcListBoxPopupWidth(){
            final QSize iconSize = iconSize();
            final int iconWidth = iconSize.width() + 4;
            final QFontMetrics fm = fontMetrics();
            int width = width();

            for (int i = count()-1; i >=0  ; --i) {
                if (itemIcon(i)==null) {
                    width = Math.max(width, fm.boundingRect(itemText(i)).width());
                } else {
                    width = Math.max(width, fm.boundingRect(itemText(i)).width() + iconWidth);
                }
            }

            final QStyleOptionComboBox opt = new QStyleOptionComboBox();
            initStyleOption(opt);
            QSize tmp = new QSize(width, 0);
            tmp = style().sizeFromContents(QStyle.ContentsType.CT_ComboBox, opt, tmp, this);
            return tmp.width();
        }
        

        @Override
        protected void closeEvent(final QCloseEvent event) {
            if (loadingSettingsRequestHandle!=null){
                loadingSettingsRequestHandle.removeListener(this);
                loadingSettingsRequestHandle.cancel();
            }
            popupShown = false;
            loadingSettingsRequestHandle = null;
            setStyle(null);
            WidgetUtils.CustomStyle.release(style);
            super.closeEvent(event);
        }

        @Override
        public void registerRequestHandle(final RequestHandle handle) {
        }

        @Override
        public void unregisterRequestHandle(final RequestHandle handle) {
        }

        @Override
        public void onResponseReceived(final XmlObject response, final RequestHandle handle) {
            onLoadingFinished();
        }

        @Override
        public void onServiceClientException(final ServiceClientException exception, final RequestHandle handle) {            
            onLoadingFinished();
        }

        @Override
        public void onRequestCancelled(final XmlObject request, final RequestHandle handler) {            
            onLoadingFinished();
        }        
        
        private void onLoadingFinished(){            
            loadingSettingsRequestHandle = null;
            if (popupShown){
                if (System.currentTimeMillis()-startLoadingTime<500){
                    Application.sleep(500);
                }
                tree.setRootIsDecorated(true);
                tree.setIndentation(20);
                prepareToShow();
            }else{
                tree.setRootIsDecorated(true);
                tree.setIndentation(20);                
            }         
        }
    }
    //private IGroupSetting currentAddon;
    private T currentAddon;
    private final ComboBox comboBox;    
    private final QToolButton editButton = new QToolButton(this);
    private final QToolButton clearButton = new QToolButton(this);    
    public final Signal1<IGroupSetting> addonChanged = new Signal1<>();
    public final Signal0 editButtonClicked = new Signal0();
    public final Signal0 openSettingsManager = new Signal0();    
    private String noItemText;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public ChooseGroupSetting(final IClientEnvironment environment, 
                              final QWidget parent, 
                              final GroupSettings<T> addons, 
                              final EnumSet<GroupSettingsTree.ShowMode> showMode,
                              final String openSettingsManagerTitle) {
        super(environment, parent);
        comboBox = new ComboBox(this, addons, showMode, openSettingsManagerTitle);
        //comboBox.setFixedWidth(180);
        comboBox.activated.connect(this, "activated()");
        {
            clearButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CLEAR));
            clearButton.clicked.connect(this, "clearSetting()");
            clearButton.setVisible(!addons.isEmpty() && !comboBox.isMandatory());
            clearButton.setFixedWidth(20);
            clearButton.setFixedHeight(comboBox.lineEdit().sizeHint().height());
            clearButton.setSizePolicy(new QSizePolicy(Policy.Fixed, Policy.Fixed));
            clearButton.setAttribute(WidgetAttribute.WA_DeleteOnClose);
            clearButton.setFocusPolicy(FocusPolicy.NoFocus);
            //editButton.pressed.connect(comboBox,"setFocus()");
        }
        {
            editButton.setText("...");
            editButton.clicked.connect(editButtonClicked);
            editButton.setVisible(addons.canOpenSettingsManager());
            editButton.setFixedWidth(20);
            editButton.setFixedHeight(comboBox.lineEdit().sizeHint().height());
            editButton.setSizePolicy(new QSizePolicy(Policy.Fixed, Policy.Fixed));
            editButton.setAttribute(WidgetAttribute.WA_DeleteOnClose);
            editButton.setFocusPolicy(FocusPolicy.NoFocus);
            editButton.pressed.connect(comboBox, "setFocus()");
        }
        setSizePolicy(Policy.MinimumExpanding, Policy.Fixed);
        setLayout(WidgetUtils.createHBoxLayout(this));
        layout().addWidget(comboBox);
        layout().addWidget(clearButton);
        layout().addWidget(editButton);
        noItemText = "<None>";
        setFrameShadow(QFrame.Shadow.Sunken);
        setFrameShape(QFrame.Shape.StyledPanel);
    }

    public T getCurrentAddon() {
        return currentAddon;
    }

    public void setCurrentAddon(T current) {
        currentAddon = current;
        activated();
    }

    public void setNoItemText(final String text) {
        noItemText = text;
        if (currentAddon == null) {
            comboBox.setText(noItemText);
            activated();
        }
    }
    
    private void activated() {
        if (!comboBox.isPopupShown()) {
            updateIcon();
        }
        if (currentAddon != null) {
            comboBox.setText(currentAddon.hasTitle() ? currentAddon.getTitle() : currentAddon.getName());
            clearButton.setVisible(!comboBox.isMandatory() && !isMandatory());
        } else {
            comboBox.setText(noItemText);
            clearButton.setVisible(false);
        }
        comboBox.selectedAddon = currentAddon;
    }

    private void updateIcon() {
        final int idx = comboBox.currentIndex();
        comboBox.setItemIcon(idx, currentAddon == null ? null : ImageManager.getQIcon(currentAddon.getIcon()));
    }

    @SuppressWarnings("unchecked")
    public void setCurrentIndex(final int idx) {
        comboBox.setCurrentIndex(idx);
        currentAddon = (T) comboBox.itemData(idx, Qt.ItemDataRole.UserRole);
        activated();
    }

    public void setEditButtonVisible(final boolean visible) {
        editButton.setVisible(visible);
    }

    public boolean isEditButtonVisible() {
        return editButton.isVisible();
    }
    
    protected boolean isSettingVisible(final T setting){
        return true;
    }
    
    protected boolean isSettingsLoaded(){
        return true;
    }
    
    protected RequestHandle startSettingsLoading(){
        return null;
    }

    protected boolean canOpenSettingsManager() {
        return true;
    }    
    
    protected boolean isMandatory(){
        return false;
    }

    public void setEditButtonHint(final String hint) {
        editButton.setToolTip(hint);
    }

    public final void refresh() {
        comboBox.updateStyleSheet();
        activated();
    }

    public void clearSetting() {
        setCurrentAddon(null);
        addonChanged.emit(null);
    }

    @Override
    protected void showEvent(final QShowEvent showEvent) {
        refresh();
        super.showEvent(showEvent);
        clearButton.setVisible(!clearButton.isHidden() && !comboBox.isMandatory() && !isMandatory());
    }
        
    @Override
    protected void resizeEvent(final QResizeEvent resizeEvent) {
        super.resizeEvent(resizeEvent);
        clearButton.setFixedHeight(comboBox.lineEdit().height());
        editButton.setFixedHeight(comboBox.lineEdit().height());
    }

    @Override
    public QSize sizeHint() {
        final QSize size = super.sizeHint();
        size.setHeight(comboBox.lineEdit().sizeHint().height() + frameWidth() * 2);
        return size;
    }
    

    @Override
    protected void closeEvent(QCloseEvent event) {
        comboBox.close();
        currentAddon = null;
        comboBox.selectedAddon = null;
        addonChanged.disconnect();
        openSettingsManager.disconnect();
        editButtonClicked.disconnect();
        super.closeEvent(event);
    }
}