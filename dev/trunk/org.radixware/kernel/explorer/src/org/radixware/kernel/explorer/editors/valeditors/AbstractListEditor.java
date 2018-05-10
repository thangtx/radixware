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

package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStringListModel;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOption;
import com.trolltech.qt.gui.QStyleOptionComboBox;
import com.trolltech.qt.gui.QStyleOptionComplex;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWheelEvent;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.types.RdxIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.propeditors.IDisplayStringProvider;

/**
 * The class provides an abstraction over kinds of combo-boxed value editors
 */
abstract class AbstractListEditor<T> extends ValEditor<T> {    
        
    private class ComboBox extends QComboBox{
        
        private class ItemDelegate extends QItemDelegate{
            
            private final QSize sizeHint = new QSize();
            private final String omitedMessageTemplate;
            private final Map<String,String> replacements = new HashMap<>();            
            
            public ItemDelegate(final QObject parent){
                super(parent);
                omitedMessageTemplate = 
                    AbstractListEditor.this.getEnvironment().getMessageProvider().translate("ValEditor", "<omitted symbols: %1$s>");
            }            

            @Override
            public QSize sizeHint(final QStyleOptionViewItem option, final QModelIndex index) {                
                final QSize defaultSize = super.sizeHint(option, index);
                final String text = (String)index.data(Qt.ItemDataRole.DisplayRole);
                final QWidget window = ComboBox.this.window();
                if (window!=null){
                    final QSize windowSize = window.size();                    
                    if (defaultSize.width()>windowSize.width()){                    
                        if (text.length()>100){
                            final QFontMetrics fontMetrics = option.fontMetrics();                        
                            final int omittedLength = text.length()-100;                        
                            final String newText = 
                                text.substring(0, 100)+String.format(omitedMessageTemplate, String.valueOf(omittedLength));                                                
                            final int newTextWidth = fontMetrics.boundingRect(newText).width();
                            final int textWidth = fontMetrics.boundingRect(text).width();
                            sizeHint.setHeight(defaultSize.height());
                            sizeHint.setWidth(defaultSize.width()-textWidth+newTextWidth);
                            replacements.put(text,newText);
                            return sizeHint;
                        }
                    }
                }
                replacements.remove(text);
                return defaultSize;
            }

            @Override
            protected void drawDisplay(final QPainter painter, 
                                       final QStyleOptionViewItem option, 
                                       final QRect rect, 
                                       final String text) {
                if (replacements.containsKey(text)){
                    super.drawDisplay(painter, option, rect, replacements.get(text));
                }else{
                    super.drawDisplay(painter, option, rect, text);
                }
            }
            
            public void clearReplacements(){
                replacements.clear();
            }
        }
        
        private class ComboBoxStyle extends WidgetUtils.CustomStyle{//RADIX-5375

            public ComboBoxStyle(){
                super(AbstractListEditor.ComboBox.this.parentWidget());
            }

            @Override
            public QRect subControlRect(final QStyle.ComplexControl control, final QStyleOptionComplex option, final int subControl, final QWidget widget) {
                if (control==QStyle.ComplexControl.CC_ComboBox &&
                    subControl==QStyle.SubControl.SC_ComboBoxListBoxPopup &&
                    (widget instanceof AbstractListEditor.ComboBox)){
                    final AbstractListEditor.ComboBox comboBox = (AbstractListEditor.ComboBox)widget;
                    final QRect resultRect = comboBox.rect();
                    resultRect.setWidth(comboBox.calcListBoxPopupWidth());                    
                    return resultRect;
                }
                return super.subControlRect(control, option, subControl, widget);
            }

            @Override
            public QRect subElementRect(final SubElement subElement, final QStyleOption option, final QWidget widget) {
                if (subElement==SubElement.SE_ComboBoxLayoutItem){
                    return option.rect();
                }
                return super.subElementRect(subElement, option, widget);
            }

            @Override
            public int pixelMetric(final PixelMetric pixelMetric, final QStyleOption option, final QWidget widget) {
                if (pixelMetric==PixelMetric.PM_MenuVMargin){
                    return 0;
                }
                return super.pixelMetric(pixelMetric, option, widget);
            }            
        }

        private boolean popupIsVisible;        
        private final ItemDelegate itemDelegate = new ItemDelegate(this);
        private final AbstractListEditor.ComboBox.ComboBoxStyle style = new AbstractListEditor.ComboBox.ComboBoxStyle();
        private final QObject viewEventFilter = new QObject(this){
            @Override
            public boolean eventFilter(final QObject target, final QEvent event) {
                if (event!=null && event.type()==QEvent.Type.Show && target instanceof QWidget){
                    final QWidget popup = ((QWidget)target).parentWidget();
                    if  (popup!=null) {                        
                        final QRect visibleRect = visibleRegion().boundingRect();
                        final QPoint point = visibleRect.topLeft();
                        int delta = visibleRect.x() - x();                      
                        if (delta>0){
                            popup.move(mapToGlobal(point).x(), popup.y());
                        }
                    }
                }
                return false;
            }            
        };

        public ComboBox(final QWidget parent){
            super(parent);
            setStyle(style);
            setInsertPolicy(InsertPolicy.NoInsert);
            final QAbstractItemView view = view();
            view.setVerticalScrollMode(QAbstractItemView.ScrollMode.ScrollPerItem);
            view.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded);   
            view.setItemDelegate(itemDelegate);
            view.installEventFilter(viewEventFilter);
        }

        @Override
        public void showPopup() {
            if (canChangeValue() && !editBtn.isVisible() && AbstractListEditor.this.beforeShowPopup()){
                view().doItemsLayout();
                WidgetUtils.pauseAsyncGroupModelReadersBeforeShowDropDownList(this);
                popupIsVisible = true;
                super.showPopup();
                AbstractListEditor.this.afterShowPopup();
            }            
        }
        
        @Override
        public void hidePopup() {            
            super.hidePopup();
            WidgetUtils.resumeAsyncGroupModelReadersAfterHideDropDownList(this);
            if (popupIsVisible && AbstractListEditor.this.focusPolicy() == Qt.FocusPolicy.StrongFocus) {
            //Метод hidePopup вызывается в том числе и при закрытии редактора.
            //Восстановить фокус нужно только если действительно был показан выпадающий список.
            //Если редактор расположен внутри таблицы, то фокус восстанавливать не нужно.
                AbstractListEditor.this.setFocus();
            }
            if (popupIsVisible){
                popupIsVisible = false;
                AbstractListEditor.this.afterHidePopup();
            }
        }

        @Override
        protected void wheelEvent(final QWheelEvent event) {
            //Do not process this event: RADIX-7110
        }

        private int calcListBoxPopupWidth(){
            int width = visibleRegion().boundingRect().width();
            final QAbstractItemView view = view();
            final com.trolltech.qt.core.QAbstractItemModel model = view.model();
            for (int row = count()-1; row >=0  ; --row) {
                width = Math.max(width, view.sizeHintForIndex(model.index(row,0)).width());
            }
            final QStyleOptionComboBox opt = new QStyleOptionComboBox();
            initStyleOption(opt);
            QSize tmp = new QSize(width, 0);
            tmp = style().sizeFromContents(QStyle.ContentsType.CT_ComboBox, opt, tmp, this);
            return tmp.width();
        }
        
        public void afterChangeItems(){
            itemDelegate.clearReplacements();
        }

        @Override
        public QSize sizeHint() {
            final QSize size = super.sizeHint();
            final QLineEdit lineEdit = getLineEdit();
            if (lineEdit!=null){
                size.setWidth(lineEdit.sizeHint().width());
            }
            return size;
        }                

        @Override
        protected void closeEvent(final QCloseEvent event) {
            view().removeEventFilter(viewEventFilter);
            setStyle(null);
            super.closeEvent(event);
        }

        @Override
        protected void disposed() {
            WidgetUtils.CustomStyle.release(style);
            super.disposed();
        }
    }
    
    private final static class ApplyStyleSheetEvent extends QEvent{
        
        public final String styleSheet;
        
        public ApplyStyleSheetEvent(final String styleSheet){
            super(QEvent.Type.User);
            this.styleSheet = styleSheet;
        }
        
    }    
    
    private final ComboBox combo = new ComboBox(this);
    private final QToolButton editBtn;
    
    private int activatedIndex = -1;
    private int nullItemIndex = -1;
    private int activatedIndexTimer = 0;
    private String selfStyleSheet;
    private boolean changeFontInternal, changingStylesheet;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public AbstractListEditor(final IClientEnvironment env, final QWidget parent,
            final EditMask editMask,
            final boolean mandatory,
            final boolean readOnly) {
        super(env, parent, editMask, mandatory, readOnly);
        combo.setObjectName("comboBox");
        combo.setLineEdit(getLineEdit());//it will remove lineEdit from this.layout
        combo.setSizeAdjustPolicy(QComboBox.SizeAdjustPolicy.AdjustToMinimumContentsLengthWithIcon);
        combo.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);                
        getLineEdit().setReadOnly(true);
        combo.setAttribute(Qt.WidgetAttribute.WA_InputMethodEnabled, false);//RADIX-8632 lineEdit is readOnly so no input support
        getLineEdit().setFocusProxy(null);
        combo.activatedIndex.connect(this,"onActivatedIndexInComboBox(int)");
        combo.setFocusProxy(getLineEdit());
        combo.setFrame(false);
        
        combo.setContextMenuPolicy(Qt.ContextMenuPolicy.NoContextMenu);
        setFocusProxy(combo);
        setPrimaryWidget(combo);

        final QAction action = new QAction(this);
        action.triggered.connect(this, "showDialogList()");
        action.setObjectName("select");
        editBtn = addButton("...", action);
        editBtn.setToolTip(getEnvironment().getMessageProvider().translate("Value", "Edit Value"));
    }
    
    protected final void setListModel(final QAbstractItemModel model){
        combo.setModel(model==null ? new QStringListModel(combo) : model);
    }
    
    /**
     * Implements logic of updating a list items which may be caused by adding elements, removing elements, etc.
     */
    abstract protected void updateComboItems(final boolean updateStyleSheet);
    
    /**
     * Defines behavior on a event of element activation.
     * @param index activated element index
     */
    abstract protected void onActivatedIndex(int index);
    
    protected boolean beforeShowPopup(){
        return true;
    }
    
    protected void afterShowPopup(){
        
    }
    
    protected void afterHidePopup(){        
    }
    
    /**
     * Returns a combo box
     * @return 
     */
    /*protected final QComboBox getComboBox() {        
        return combo;
    }*/
    
    protected final void clearItems(){
        combo.clear();
        afterChangeItems();
    }
    
    protected final void addItems(final List<String> itemTitles){
        combo.addItems(itemTitles);
        afterChangeItems();
    }
    
    protected final void setItems(final List<String> itemTitles){
        combo.clear();
        combo.addItems(itemTitles);
        afterChangeItems();
    }
    
    protected final void addItems(final List<QIcon> icons, final List<String> titles){
        for (int i = 0; i < titles.size(); ++i) {
            combo.addItem(icons.get(i), titles.get(i));            
        }
        afterChangeItems();
    }
    
    protected final void setItems(final List<QIcon> icons, final List<String> titles){
        combo.clear();
        addItems(icons, titles);    
    }
    
    protected final void addListWidgetItems(final List<ListWidgetItem> items){
        for (int i = 0; i < items.size(); ++i) {
            final ListWidgetItem item = items.get(i);
            final Icon icon = item.getIcon();
            if (icon==null){
                combo.addItem(item.getText(), item);
            }else{
                combo.addItem(ExplorerIcon.getQIcon(icon), item.getText(), item);
            }
            final String toolTip = item.getToolTip();
            if (toolTip!=null && !toolTip.isEmpty()){
                combo.setItemData(i, toolTip, Qt.ItemDataRole.ToolTipRole);
            }
            final String itemName = item.getName();
            if (itemName!=null && !itemName.isEmpty()){
                combo.setItemData(i, itemName, WidgetUtils.MODEL_ITEM_ROW_NAME_DATA_ROLE);
                combo.setItemData(i, itemName, WidgetUtils.MODEL_ITEM_CELL_NAME_DATA_ROLE);                                
            }
            final Object itemValue = item.getValue();
            if (itemValue instanceof String){
                combo.setItemData(i, itemValue, WidgetUtils.MODEL_ITEM_CELL_VALUE_DATA_ROLE);
                combo.setItemData(i, Boolean.FALSE, WidgetUtils.MODEL_ITEM_CELL_VALUE_IS_NULL_DATA_ROLE);
            }else if (itemValue==null){
                combo.setItemData(i, Boolean.TRUE, WidgetUtils.MODEL_ITEM_CELL_VALUE_IS_NULL_DATA_ROLE);
            }
        }
        afterChangeItems();        
    }
    
    protected final void setListWidgetItems(final List<ListWidgetItem> items){
        combo.clear();
        addListWidgetItems(items);
    }
    
    private void afterChangeItems(){
        combo.afterChangeItems();
    }
    
    protected final int getItemsCount(){
        return combo.count();
    }        
    
    protected final void updateComboBoxLook(final int newIndex, final boolean nullItemIsPresent, final boolean updateStyleSheet) {
        if (!isMandatory() && !isReadOnly() && !nullItemIsPresent && getValue() != null) {//RADIX-3454
            nullItemIndex = combo.count();
            combo.addItem(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CLEAR), getStringToShow(null));
        }else{
            nullItemIndex = -1;
        }
        combo.setCurrentIndex(newIndex);
        if (getValue() == null) {
            getLineEdit().setText(getStringToShow(null));
            getLineEdit().clearFocus();
        }else if (newIndex<0){
            getLineEdit().setText(getStringToShow(getValue()));
        }
        getLineEdit().home(false);
        if (updateStyleSheet){
            updateStyleSheet(false);
        }
    }
    
    @SuppressWarnings("PMD.ConsecutiveLiteralAppends")
    private void updateStyleSheet(final boolean forced) {
        final EnumSet<ETextOptionsMarker> markers = getTextOptionsMarkers();
        markers.remove(ETextOptionsMarker.UNDEFINED_VALUE);
        markers.remove(ETextOptionsMarker.MANDATORY_VALUE);
        markers.remove(ETextOptionsMarker.READONLY_VALUE);
        markers.remove(ETextOptionsMarker.OVERRIDDEN_VALUE);
        markers.remove(ETextOptionsMarker.INHERITED_VALUE);
        final ExplorerTextOptions listOptions = calculateTextOptions(markers);        
        final StringBuilder styleSheet = new StringBuilder(180);
        styleSheet.append("QComboBox {");
        {
            styleSheet.append("border: 0px;\n");
            styleSheet.append("padding-top: 0px;\n");
            styleSheet.append("padding-bottom: 0px;\n");
        }
        {
            styleSheet.append("}\n");
            styleSheet.append("QComboBox QAbstractItemView {\n");
            styleSheet.append("border: 2px solid darkgray;\n");
            if (listOptions.getBackground()!=null){
                styleSheet.append("background-color: ");
                styleSheet.append(listOptions.getBackground().name());
                styleSheet.append(";\n");
            }
            if (listOptions.getForeground()!=null){
                styleSheet.append("color: ");
                styleSheet.append(listOptions.getForeground().name());
                styleSheet.append(";\n");
            }
        }
        final boolean canChangeValue = canChangeValue();
        final boolean isShowDialogListBtnVisible = isShowListDialogButtonVisible();        
        if (!canChangeValue || isShowDialogListBtnVisible) {
            styleSheet.append("}\n");
            styleSheet.append("QComboBox::drop-down {\n");
            styleSheet.append("width: 0px\n");
        }
        styleSheet.append("}\n");
        changeSelfStyleSheet(styleSheet.toString(),forced);
        editBtn.setVisible(isShowDialogListBtnVisible);
    }
    
    private void changeSelfStyleSheet(final String newStyleSheet, final boolean forced){
        if (!Objects.equals(newStyleSheet, selfStyleSheet) || forced){//optimization
            selfStyleSheet = newStyleSheet;
            changingStylesheet = true;
            try{
                setStyleSheet(selfStyleSheet);
            }finally{
                changingStylesheet = false;
            }
        }        
    }
    
    @SuppressWarnings("unused")
    private void onActivatedIndexInComboBox(final int index){
        activatedIndex = index;
        activatedIndexTimer = startTimer(20);//wait some time until popup window actually closed
    }        

    @Override
    protected final void onChangeFontEvent(final ExplorerFont font) {
        if (!changingStylesheet){
            super.onChangeFontEvent(font);
        }
    }
            
    private boolean canChangeValue() {
        final int itemsCount = getItemsCount();
        return !isReadOnly()
                && itemsCount > 0
                && (itemsCount > 1 || getValue() == null || getEditMask().validate(getEnvironment(),getValue())!=ValidationResult.ACCEPTABLE || !isMandatory());
    }
    
    private boolean isShowListDialogButtonVisible(){
        final int limit = getMaxItemsInPopup();
        if (limit>=0){
            int itemsCount = getItemsCount();
            if (nullItemIndex>=0){
                itemsCount--;
            }
            return !isReadOnly()
                && isEnabled()
                && isEmbeddedWidgetsVisible()
                && itemsCount > 0
                && (itemsCount > 1 || getValue() == null || getEditMask().validate(getEnvironment(),getValue())!=ValidationResult.ACCEPTABLE || !isMandatory())
                && itemsCount>limit;
            
        }else{
            return false;
        }
    }
    
    protected int getMaxItemsInPopup(){
        return getEnvironment().getConfigStore().readInteger(MAX_ITEMS_IN_DD_LIST_SETTING_KEY);
    }    
    
    public final void refreshShowListDialogButton(){
        updateStyleSheet(false);
    }
        
    @SuppressWarnings("unused")
    private void showDialogList(){
        final List<ListWidgetItem> items = new LinkedList<>();
        final int indexInComboBox = combo.currentIndex();
        int currentIndex = -1;
        for (int i=0,count=combo.count(); i<count; i++){
            if (i!=nullItemIndex){
                final ListWidgetItem item;            
                if (combo.itemData(i, Qt.ItemDataRole.UserRole) instanceof ListWidgetItem){
                    item = (ListWidgetItem)combo.itemData(i, Qt.ItemDataRole.UserRole);
                }else{
                    if (combo.itemIcon(i)==null){
                        item = new ListWidgetItem(combo.itemText(i));
                    }else{
                        item = new ListWidgetItem(combo.itemText(i), null, new RdxIcon(combo.itemIcon(i)));
                    }
                    if (combo.itemData(i,Qt.ItemDataRole.ToolTipRole) instanceof String){
                        item.setToolTip((String)combo.itemData(i,Qt.ItemDataRole.ToolTipRole));
                    }
                }
                items.add(item);
                if (i==indexInComboBox){
                    currentIndex = items.size()-1;
                }
            }
        }
        final int selectedItemIndex = selectItem(items, currentIndex);
        if (selectedItemIndex>=0){
            if (nullItemIndex>-1 && nullItemIndex<=selectedItemIndex){
                onActivatedIndex(selectedItemIndex+1);
            }else{
                onActivatedIndex(selectedItemIndex);
            }
        }
    }
    
    @Override
    public void setReadOnly(final boolean readOnly) {
        if (readOnly!=isReadOnly()){
            super.setReadOnly(readOnly);
            updateStyleSheet(false);
            updateComboItems(true);
        }
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        if (!getEditMask().equals(editMask)){
            super.setEditMask(editMask);
            updateComboItems(true);
        }
    }

    @Override
    public void setMandatory(final boolean mandatory) {
        if (mandatory!=isMandatory()){
            super.setMandatory(mandatory);
            updateComboItems(true);
        }
    }

    @Override
    protected void timerEvent(final QTimerEvent event) {
        if (event.timerId()==activatedIndexTimer){
            killTimer(activatedIndexTimer);
            activatedIndexTimer = 0;
            event.accept();
            onActivatedIndex(activatedIndex);
            activatedIndex = -1;
        }
        super.timerEvent(event); //To change body of generated methods, choose Tools | Templates.
    }
            
    @Override
    public boolean eventFilter(final QObject obj, final QEvent event) {
        if (isMandatory()) {
            return super.eventFilter(obj, event);
        }
        if (isReadOnly()) {
            return super.eventFilter(obj, event);
        }
        if (!(event instanceof QKeyEvent) || event.type() != QEvent.Type.KeyPress) {
            return super.eventFilter(obj, event);
        }
        final QKeyEvent keyEvent = (QKeyEvent) event;
        final boolean isControl = keyEvent.modifiers().isSet(Qt.KeyboardModifier.ControlModifier)
            || (keyEvent.modifiers().isSet(Qt.KeyboardModifier.MetaModifier) && SystemTools.isOSX);
        final boolean isSpace = keyEvent.key() == Qt.Key.Key_Space.value();
        final boolean isEnter = keyEvent.key() == Qt.Key.Key_Enter.value()
                || keyEvent.key() == Qt.Key.Key_Return.value();

        if (isControl && isSpace) {
            setValue(null);
        }
        if (isEnter) //On Enter combobox make activate 0  index. Qt bug?
        {
            return true;
        } else {
            return super.eventFilter(obj, event);
        }
    }

    @Override
    protected void applyFont(final ExplorerFont newFont) {
        if (newFont!=null){
            final QLineEdit editor = getLineEdit();
            final String styleSheet = editor.styleSheet();
            if (!styleSheet.isEmpty()){
                changeLineEditStyleSheet("");
            }
            editor.setFont(newFont.getQFont());
            combo.setFont(newFont.getQFont());        
            if (!changeFontInternal && !styleSheet.isEmpty()){
                changeLineEditStyleSheet(styleSheet);
            }
        }
    }                

    @Override
    protected void applyTextOptions(final ExplorerTextOptions options) {        
        changeFontInternal = true;
        if (options.getFont()!=null){
            try{
                applyFont(options.getFont());
            }finally{
                changeFontInternal = false;
            }
        }
        if (isEnabled()){
            final StringBuffer styleSheet = new StringBuffer();
            styleSheet.append("QLineEdit {");
            styleSheet.append(options.getStyleSheet());
            styleSheet.append("}\nQAbstractItemView {");
            styleSheet.append(options.getStyleSheet());
            styleSheet.append('}');
            //on windows it is required scheduled change styleSheet
            QApplication.postEvent(this, new ApplyStyleSheetEvent(styleSheet.toString()));
        }else{
            changeLineEditStyleSheet("");
        }
        updateStyleSheet(true);
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof ApplyStyleSheetEvent){
            event.accept();
            if (nativeId()!=0 && combo.nativeId()!=0 && !wasClosed()){
                if (isEnabled()){
                    changeLineEditStyleSheet(((ApplyStyleSheetEvent)event).styleSheet);
                }else{
                    changeLineEditStyleSheet("");
                }
            }
        }else{
            super.customEvent(event);
        }
    }
    
    @Override
    protected void onMouseClick() {
        if (isEnabled() && isEmbeddedWidgetsVisible()){
            combo.showPopup();
        }
    }    

    @Override
    public void setDisplayStringProvider(final IDisplayStringProvider displayStringProvider) {
        super.setDisplayStringProvider(displayStringProvider);
        updateComboItems(false);
    }    

    @Override
    protected void closeEvent(final QCloseEvent event) {        
        super.closeEvent(event);
        combo.close();
    }       
}
