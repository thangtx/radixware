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
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAbstractTableModel;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QClipboard;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QCommonStyle;
import com.trolltech.qt.gui.QCompleter;
import com.trolltech.qt.gui.QContentsMargins;
import com.trolltech.qt.gui.QContextMenuEvent;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QLayoutItemInterface;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QStringListModel;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionFrame;
import com.trolltech.qt.gui.QStyleOptionFrameV2;
import com.trolltech.qt.gui.QStyleOptionToolButton;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QToolTip;
import com.trolltech.qt.gui.QValidator;
import com.trolltech.qt.gui.QValidator.QValidationData;
import com.trolltech.qt.gui.QValidator.State;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QWidgetItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IListDialog;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval;
import org.radixware.kernel.common.client.meta.mask.validators.IInputValidator;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.text.CustomTextOptions;
import org.radixware.kernel.common.client.text.ITextOptionsManager;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.client.text.MergedTextOptionsProvider;
import org.radixware.kernel.common.client.types.EditingHistoryException;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.types.PriorityArray;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.inspector.Inspectable;
import org.radixware.kernel.explorer.inspector.InspectablePropertySetter;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.types.Margins;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;
import org.radixware.kernel.explorer.widgets.propeditors.IDisplayStringProvider;

/**
 * ValEditor - редактор для значения типа T. Поддерживает возможность установки свойства null (комбинация
 * клавиш <Ctrl>+space). Нажатие этой комбинации эквивалентно вызову setValue(null). Информация об отображении
 * свойства и об ограничениях на значения свойства задается с помощью EditMask, mandatory, readOnly. В
 * ValEditor в качестве основного элемента для ввода данных используется поле для ввода lineEdit. Некоторые
 * valEditor позволяют пользователю вводить значение с помощью этого поля (ValStrEditor, ValIntEditor,
 * ValDateTimeEditor, ValCharEditor, ValNumEditor, ValBinEditor, ValTimeIntervalEditor). Для предотвращения
 * некоректного ввода, эти редакторы создают объект типа QValidator. * В этих valEditor lineEdit может
 * находится в двух состояниях: 1. состояние редактирования, при получения фокуса ввода. 2. состояние
 * отображения значения, при потери фокуса. Данные отображаются в соответствии метода маски toStr(). Другая
 * группа valEditor использует lineEdit только для отображения значения свойства (ValBoolEditor,
 * AdvancedValBoolEditor, ValConstSetEditor, ValListEditor, ValRefEditor, ValArrEditor). Для изменения
 * свойства используется либо вспомогательный диалог, либо возможность выбора из списка. Для этих valEditor
 * lineEdit всегда установлен только для чтения. Значение поля readOnly не всегда совпадает со значением
 * lineEdit.isReadOnly().В редакторе есть два метода для изменения значения свойства: setOnlyValue и setValue.
 * SetOnlyValue используется при обработки пользовательского ввода, когда вводимые пользователем данные должны
 * изменить только внутрение данные редактора но не его внешний вид (вызывается в методе onTextEdited, который
 * подписывается на соответствующий сигнал). Метод setValue помимо изменения поля value также изменяет
 * отображаемые данные. После вызова setValue редактор принудительно теряет фокус. Возможно лучше отслеживать
 * в каком состоянии находился редактор перед вызовом setValue, и после вызова переводить его в это состояние.
 * При создании редакто всегда имеет значение null, которое всегда валидно с точки зрения маски (метод
 * mask.isValid), но может быть не корректным если mandator == true. В valEditor, которые поддерживают
 * редактирование с помощью lineEdit, возникают ситиуации когда вводимые пользователем данные не являеются
 * валидными с точки зрения маски и носят промежуточный характер. В случае потери фокуса редактора, данные
 * которые находятся в промежуточной форме, значение редактора не изменяется (то есть используется предыдущее
 * валидное значение).
 *
 */
public class ValEditor<T> extends ExplorerFrame {
    
    public final static int DEFAULT_BUTTON_PRIORITY = PriorityArray.DEFAULT_PRIORITY;
    
    protected final static String MAX_ITEMS_IN_DD_LIST_SETTING_KEY = 
        SettingNames.SYSTEM+"/"+SettingNames.EDITOR_GROUP+"/"+SettingNames.Editor.COMMON_GROUP+"/"+SettingNames.Editor.Common.DROP_DOWN_LIST_ITEMS_LIMIT;    

    public static enum EReactionToIntermediateInput {
        NONE,
        SHOW_WARNING,
        DISCARD_INPUT
    }
    
    private final static class SetFocusEvent extends QEvent{        
        public SetFocusEvent(){
            super(QEvent.Type.User);
        }
    }
    
    protected final static QStyle COMMON_STYLE = new QCommonStyle();

    protected interface IWidthHintCalculator {

        int getWidthHint(QFontMetrics fontMetrics, EditMask editMask, IClientEnvironment environment);
    }

    protected static class DefaultWidthHintCalculator implements IWidthHintCalculator {
        
        private final static class WidthCache{
            
            private final Map<String,Integer> widthForString = new HashMap<>();
            private final QFontMetrics fontMetrics;
            
            public WidthCache(final QFontMetrics fontMetrics){
                this.fontMetrics = fontMetrics;
            }
            
            public int getWidth(final String s){
                Integer width = widthForString.get(s);
                if (width==null){
                    width = fontMetrics.width(s);
                    widthForString.put(s, width);
                }
                return width;
            }            
        }
        
        private final Map<QFontMetrics, WidthCache> cache = new HashMap<>();

        private DefaultWidthHintCalculator() {
        }        
        
        public static final DefaultWidthHintCalculator INSTANCE = new DefaultWidthHintCalculator();

        @Override
        public int getWidthHint(final QFontMetrics fontMetrics, final EditMask editMask, final IClientEnvironment environment) {
            WidthCache widthCache = cache.get(fontMetrics);
            if (widthCache==null){
                widthCache = new WidthCache(fontMetrics);
                cache.put(fontMetrics, widthCache);
            }            
            final int minWidth = widthCache.getWidth("x") * 10;
            if (editMask == null) {
                return minWidth;
            } else {
                return Math.max(minWidth, widthCache.getWidth(editMask.toStr(environment, null)));
            }
        }
    }
    
    private static final class ValEditorLayout extends QHBoxLayout{                
        
        private static final class AdditionalWidgetItem extends QWidgetItem{            
            
            private final static QRect NULL_RECT = new QRect(0, 0, 0, 0);
            private final static QSize NULL_SIZE = new QSize(0, 0);
            private final static Qt.Orientations NULL_ORIENTATIONS = new Qt.Orientations(0);            
            
            private boolean isVisible = true;            
            private final QWidget widget;

            public AdditionalWidgetItem(final QWidget qw, final Qt.Alignment alignment) {
                super(qw);
                this.widget = qw;
                setAlignment(alignment);
            }

            @Override
            public QWidget widget() {                
                return isVisible ? super.widget() : null;
            }

            @Override
            public boolean isEmpty() {
                return isVisible ? super.isEmpty() : false;
            }

            @Override
            public QSize sizeHint() {
                return isVisible ? super.sizeHint() : NULL_SIZE;
            }

            @Override
            public QSize minimumSize() {
                return isVisible ? super.minimumSize() : NULL_SIZE;
            }

            @Override
            public int minimumHeightForWidth(int width) {
                return isVisible ? super.minimumHeightForWidth(width) : 0;
            }

            @Override
            public QSize maximumSize() {
                return isVisible ? super.maximumSize() : NULL_SIZE;
            }

            @Override
            public int heightForWidth(final int width) {
                return isVisible ? super.heightForWidth(width) : 0;
            }

            @Override
            public boolean hasHeightForWidth() {
                return isVisible ? super.hasHeightForWidth() : false;
            }

            @Override
            public QRect geometry() {
                return isVisible ? super.geometry() : NULL_RECT;
            }

            @Override
            public void setGeometry(final QRect qrect) {
                if (isVisible){
                    super.setGeometry(qrect);
                }
            }            

            @Override
            public Qt.Orientations expandingDirections() {
                return isVisible ? super.expandingDirections() : NULL_ORIENTATIONS;
            }                        
            
            public void setVisible(final boolean isVisible){
                this.isVisible = isVisible;
            }
            
            public boolean isVisible(){
                return isVisible;
            }
            
            public QWidget getWidget(){
                return widget;
            }
        }
        
        private static final Qt.Alignment LEFT_ALIGNMENT = new Qt.Alignment(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignVCenter);
        private static final Qt.Alignment RIGHT_ALIGNMENT = new Qt.Alignment(Qt.AlignmentFlag.AlignRight, Qt.AlignmentFlag.AlignVCenter);
        private final static EnumSet<QEvent.Type> PROHIBITED_EVENTS = EnumSet.of(QEvent.Type.Shortcut,
                                                                             QEvent.Type.ShortcutOverride,
                                                                             QEvent.Type.Wheel,
                                                                             QEvent.Type.KeyPress,
                                                                             QEvent.Type.KeyRelease,
                                                                             QEvent.Type.Enter,
                                                                             QEvent.Type.Leave,
                                                                             QEvent.Type.MouseButtonPress,
                                                                             QEvent.Type.MouseButtonDblClick,
                                                                             QEvent.Type.MouseButtonRelease,
                                                                             QEvent.Type.Paint
                                                                             );
                
        private final QEventFilter paintBlocker = new QEventFilter(this);
        private int primaryWidgetIndex = -1;
        private boolean isAdditionalWidgetsVisible = true; 
        private static int CREATE_COUNT  = 0;
        private static int REMOVE_COUNT  = 0;
        
        public ValEditorLayout(final QWidget parent){
            super(parent);
            setSpacing(0);
            setMargin(0);
            setAlignment(LEFT_ALIGNMENT);
            setSizeConstraint(QLayout.SizeConstraint.SetNoConstraint);
            paintBlocker.setProhibitedEventTypes(PROHIBITED_EVENTS);
        }
        
        public void setPrimaryWidget(final QWidget widget){
            if (primaryWidgetIndex>0){
                final QLayoutItemInterface item = itemAt(primaryWidgetIndex);
                if (item instanceof QWidgetItem){
                    removeItem(item);
                }else{
                    primaryWidgetIndex = 0;
                }
            }else{
                primaryWidgetIndex = 0;
            }
            insertWidget(primaryWidgetIndex,widget);            
        }
        
        public void insertAdditionalWidget(final int index, final QWidget widget){            
            if (widget==null || widget.nativeId()==0)
                return;
            addChildWidget(widget);
            if (index < 0){
                insertItem(count(), new AdditionalWidgetItem(widget,RIGHT_ALIGNMENT));
            }else{
                final Qt.Alignment alignment;
                if (index<=primaryWidgetIndex){
                    primaryWidgetIndex++;
                    alignment = LEFT_ALIGNMENT;
                }else{
                    alignment = RIGHT_ALIGNMENT;
                }
                insertItem(index, new AdditionalWidgetItem(widget, alignment));
            }
            CREATE_COUNT++;
        }
        
        public void removeAdditionalWidget(final QWidget widget){
            if (widget!=null && widget.nativeId()!=0){
                if (!isAdditionalWidgetsVisible){
                    widget.removeEventFilter(paintBlocker);
                }
                removeWidget(widget);
                REMOVE_COUNT++;
            }
        }

        public void setAdditionalWidgetsVisible(final boolean isVisible){
            if (isVisible!=isAdditionalWidgetsVisible){
                isAdditionalWidgetsVisible = isVisible;
                QLayoutItemInterface layoutItem;
                AdditionalWidgetItem widgetItem;
                QWidget widget;
                for (int i=0,count=count(); i<count; i++){
                    layoutItem = itemAt(i);
                    if (layoutItem instanceof AdditionalWidgetItem){
                        widgetItem = (AdditionalWidgetItem)layoutItem;
                        widget = widgetItem.getWidget();
                        if (widget!=null && widget.nativeId()!=0){
                            widgetItem.setVisible(isVisible);
                            if (isVisible){
                                widget.removeEventFilter(paintBlocker);
                            }else{
                                widget.installEventFilter(paintBlocker);
                            }                      
                        }
                    }
                }
                invalidate();
            }
        }
        
        public boolean isAdditionalWidgetsVisible(){
            return isAdditionalWidgetsVisible;
        }
        
        public int getPrimaryWidgetIndex(){
            return primaryWidgetIndex;
        }
    }
    
    private static final class ValEditorLayoutWrapper{//this class is used to protect from direct call some QHBoxLayout method
        
        private final ValEditorLayout layout;
        
        public ValEditorLayoutWrapper(final QWidget widget){
            layout = new ValEditorLayout(widget);
        }
                
        public void setPrimaryWidget(final QWidget widget){
            layout.setPrimaryWidget(widget);
        }
        
        public void insertAdditionalWidget(final int index, final QWidget widget){            
            layout.insertAdditionalWidget(index, widget);
        }
        
        public void removeAdditionalWidget(final QWidget widget){
            layout.removeAdditionalWidget(widget);
        }

        public void setAdditionalWidgetsVisible(final boolean isVisible){
            layout.setAdditionalWidgetsVisible(isVisible);
        }
        
        public boolean isAdditionalWidgetsVisible(){
            return layout.isAdditionalWidgetsVisible;
        }
        
        public int itemsCount(){
            return layout.count();
        }
        
        public int getFirstButtonIndex(){
            return layout.getPrimaryWidgetIndex()+1;
        }
    }
            
    private final class InternalLineEdit extends QLineEdit {

        private final static String TOOLTIP_STYLESHEET = "QToolTip { background-color: palegoldenrod; "
                + "border-color: red; border-width: 2px; border-style: solid; "
                + "border-radius: 4px;}";
        private final static int ICON_MARGIN = 2;
        private final static int VERTICAL_MARGIN = 1;//from qlineedit.cpp: #define verticalMargin 1
        private final static int HORIZONTAL_MARGIN = 1;//from qlineedit.cpp: #define horizontalMargin 2        
        private final QLabel lbInvalidValueIcon = new QLabel(this);
        private Margins textMargins = Margins.EMPTY;
        private final QStyleOptionFrame frameOptions = new QStyleOptionFrameV2();
        private final QSize temporarySize = new QSize();
        private int toolTipTimerId;
        private IWidthHintCalculator widthHintCalculator = DefaultWidthHintCalculator.INSTANCE;

        public InternalLineEdit(final QWidget parent) {
            super(parent);
            this.setFrame(false);
            setSizePolicy(Policy.Expanding, Policy.Expanding);
            setAutoFillBackground(true);
            setObjectName("lineEdit");

            final QHBoxLayout lineEditLayout = new QHBoxLayout(this);
            lineEditLayout.setContentsMargins(0, 0, ICON_MARGIN, 0);

            lbInvalidValueIcon.setVisible(false);
            final int icon_size = sizeHint().height() - 2 * ICON_MARGIN;
            lbInvalidValueIcon.setPixmap(ExplorerIcon.getQIcon(ExplorerIcon.TraceLevel.WARNING).pixmap(icon_size));
            lbInvalidValueIcon.setPalette(palette());
            lbInvalidValueIcon.setCursor(new QCursor(Qt.CursorShape.ArrowCursor));
            lbInvalidValueIcon.setFixedSize(icon_size, icon_size);
            lineEditLayout.addWidget(lbInvalidValueIcon, 0, Qt.AlignmentFlag.AlignRight);
            final String color = palette().color(backgroundRole()).name();
            lbInvalidValueIcon.setStyleSheet("QLabel {border: 0px solid " + color + ";}" + TOOLTIP_STYLESHEET);

            setAttribute(Qt.WidgetAttribute.WA_MacShowFocusRect, false);
        }

        public void showInvalidValueIcon(final String toolTip) {
            final int rightMargin = lbInvalidValueIcon.sizeHint().width() + ICON_MARGIN * 2;
            setTextMargins(0, 0, rightMargin, 0);
            textMargins = new Margins(0, 0, rightMargin, 0);
            lbInvalidValueIcon.setVisible(true);
            lbInvalidValueIcon.setToolTip(toolTip);
        }

        public void setInvalidValueIconToolTip(final String toolTip) {
            lbInvalidValueIcon.setToolTip(toolTip);
        }

        public void hideInvalidValueIcon() {
            lbInvalidValueIcon.setVisible(false);
            setTextMargins(0, 0, 0, 0);
            textMargins = Margins.EMPTY;
            lbInvalidValueIcon.setToolTip("");
        }

        @Override
        public QSize sizeHint() {
            //Вызов метода setStyleSheet изменяет текущий стиль (результат, возвращаемый style()),
            //что может приводить к изменению вертикального размера sizeHint, в случае когда предыдущем
            //стилем был "windows-vista". Для избежания непреднамеренного изменения размера расчет 
            //sizeHint перекрыт т.о. чтобы все время использовался один и тот же стиль (QCommonStyle).
            //Написано на основе перекрытого метода в qlineedit.cpp:            
            ensurePolished();
            final QContentsMargins margins = this.getContentsMargins();            
            final ExplorerFont font = ExplorerFont.Factory.getFont(font());
            final QFontMetrics fm = font.getQFontMetrics();
            final int h = Math.max(fm.lineSpacing(), 14) + 2 * VERTICAL_MARGIN
                    + textMargins.top + textMargins.bottom
                    + margins.top + margins.bottom;
            final int w = widthHintCalculator.getWidthHint(fm, getEditMask(), getEnvironment())
                    + 2 * HORIZONTAL_MARGIN
                    + textMargins.left + textMargins.right
                    + margins.left + margins.right; // "some"
            initStyleOption(frameOptions);
            final QSize globalStrut = QApplication.globalStrut();
            temporarySize.setHeight(Math.max(h, globalStrut.height()));
            temporarySize.setWidth(Math.max(w, globalStrut.width()));
            final QSize sizeHint =
                COMMON_STYLE.sizeFromContents(QStyle.ContentsType.CT_LineEdit, frameOptions, temporarySize, this);
            sizeHint.setWidth(sizeHint.width() - textMargins.right);
            return sizeHint;
        }

        /**
         * Событие mouseMoveEvent обрабатывается чтобы курсор имел форму IBeamCursor только в области
         * contentRect.
         *
         */
        @Override
        protected void focusInEvent(final QFocusEvent event) {
            if (event.reason() != Qt.FocusReason.PopupFocusReason) {
                onFocusIn();
            }
            if (testAttribute(Qt.WidgetAttribute.WA_MacShowFocusRect)) {
                setAttribute(WidgetAttribute.WA_MacShowFocusRect, false);
            }
            super.focusInEvent(event);
            final QCompleter completer = completer();
            if (completer != null) {
                completer.highlighted.disconnect(this);
                completer.highlightedIndex.disconnect(this);
            }
        }

        @Override
        protected void focusOutEvent(final QFocusEvent event) {
            if (event.reason() != Qt.FocusReason.PopupFocusReason) {
                onFocusOut();
            }
            super.focusOutEvent(event);
        }

        @Override
        protected void mouseReleaseEvent(final QMouseEvent event) {
            super.mouseReleaseEvent(event);
            onMouseClick();
        }

        @Override
        protected void mouseDoubleClickEvent(final QMouseEvent event) {
            if (!onMouseDoubleClick()){
                super.mouseDoubleClickEvent(event);
            }            
        }        

        @Override
        protected void contextMenuEvent(final QContextMenuEvent event) {
            QMenu menu = createStandardContextMenu();
            final List<QAction> actions = menu.actions();
            if (actions.size()>0){
                actions.get(0).triggered.disconnect();
                actions.get(0).triggered.connect(this,"copyToClipboard()");
                if (isReadOnly()){
                    for (int i=actions.size()-1; i>=1; i--){
                        menu.removeAction(actions.get(i));
                    }
                }
            }else if (actions.size()>5){
                actions.get(3).triggered.disconnect();
                actions.get(3).triggered.connect(this,"cutToClipboard()");
                actions.get(4).triggered.disconnect();
                actions.get(4).triggered.connect(this,"copyToClipboard()");                
                actions.get(5).triggered.disconnect();
                actions.get(5).triggered.connect(this,"pasteFromClipboard()");                
            }
            if (hasCustomContextMenu()) {
                menu = createCustomContextMenu(menu);
            }
            menu.exec(event.globalPos());
            menu.disposeLater();
        }

        public void scheduleShowValidationToolTip() {
            toolTipTimerId = startTimer(500);
        }

        @Override
        protected void timerEvent(final QTimerEvent timerEvent) {
            if (timerEvent.timerId() == toolTipTimerId) {
                killTimer(toolTipTimerId);
                toolTipTimerId = 0;
                showValidationToolTip();
            } else {
                super.timerEvent(timerEvent);
            }
        }

        public void showValidationToolTip() {
            final String toolTip = lbInvalidValueIcon.toolTip();
            if (toolTip != null
                    && !toolTip.isEmpty()
                    && lbInvalidValueIcon.isVisible()
                    && QApplication.activeWindow() == window()) {
                final QPoint toolTipPoint = mapToGlobal(lbInvalidValueIcon.rect().bottomLeft());
                QToolTip.showText(toolTipPoint, toolTip, lbInvalidValueIcon);
            }
        }

        public void setWidthHintCalculator(final IWidthHintCalculator newCalculator) {
            widthHintCalculator =
                    newCalculator == null ? DefaultWidthHintCalculator.INSTANCE : newCalculator;
            updateGeometry();
        }

        @Override
        protected void keyPressEvent(final QKeyEvent event) {            
            if (event.matches(QKeySequence.StandardKey.Copy)){
                copyToClipboard();
                event.accept();
                return;
            }
            if (!isReadOnly()){
                if (event.matches(QKeySequence.StandardKey.Cut)){
                    cutToClipboard();
                    event.accept();
                    return;
                }else if (event.matches(QKeySequence.StandardKey.DeleteEndOfLine) && echoMode()==EchoMode.Normal){
                    setSelection(cursorPosition(), displayText().length()-1);
                    cutToClipboard();
                    event.accept();
                    return;
                }else if (event.matches(QKeySequence.StandardKey.Paste)){
                    QClipboard.Mode mode = QClipboard.Mode.Clipboard;
                    if (SystemTools.isUnix()){
                        if (event.modifiers().isSet(Qt.KeyboardModifier.ControlModifier,Qt.KeyboardModifier.ShiftModifier)
                            && event.key() == Qt.Key.Key_Insert.value()){
                            mode = QClipboard.Mode.Selection;
                        }
                        pasteFromClipboard(mode);
                        event.accept();
                        return;
                    }
                }
            }
            super.keyPressEvent(event);
        }
        
        private boolean copyToClipboard(){
            if (echoMode()==EchoMode.Normal){
                final String selection = selectedText();
                if (!selection.isEmpty()){
                    final String textToCopy = ValEditor.this.beforeCopyToClipboard(selection);
                    if (textToCopy==null){
                        return false;
                    }
                    if (!textToCopy.isEmpty()){
                        QApplication.clipboard().setText(textToCopy);
                    }
                    return true;
                }
            }
            return false;
        } 
        
        private boolean cutToClipboard(){
            if (copyToClipboard()){
                del();
                return true;
            }
            return false;
        }
        
        private void pasteFromClipboard(){
            pasteFromClipboard(QClipboard.Mode.Clipboard);
        }
        
        private void pasteFromClipboard(final QClipboard.Mode mode){
            if (!isReadOnly()){
                final String text = QApplication.clipboard().text(mode);
                if (!text.isEmpty() && ValEditor.this.beforePasteFromClipboard(text)){
                    paste();
                }
            }
        }
    }

    private class InternalValidator extends QValidator {

        private IInputValidator inputValidator;
        private boolean enabled;
        private String currentText, selectedText = "";

        public InternalValidator(final QObject obj) {
            super(obj);
        }

        public void setInputValidator(final IInputValidator validator) {
            inputValidator = validator;
        }

        public void setCheckingEnabled(final boolean isEnabled) {
            this.enabled = isEnabled;
        }

        public void setCurrentText(final String text) {
            currentText = text;
        }

        public void setSelectedText(final String text) {
            selectedText = text;
        }

        @Override
        @SuppressWarnings("PMD.MissingBreakInSwitch")
        public State validate(final QValidationData data) {            
            if (!enabled || inputValidator == null) {
                return State.Acceptable;
            }
            final String valAsStr = data.string;
            final ValidationResult result = inputValidator.validate(getEnvironment(), valAsStr, data.position);

            switch (result.getState()) {
                case ACCEPTABLE:
                    return State.Acceptable;
                case INTERMEDIATE:
                    return State.Intermediate;
                default: {
                    if (currentText != null
                            && valAsStr != null
                            && valAsStr.length() < currentText.length()) {
                        if (selectedText == null || selectedText.isEmpty()) {
                            //Количество символов всегда можно уменьшить
                            return State.Intermediate;
                        } else {
                            //Проверка должна работать когда количество символов уменьшается за счет замены выделенного текста
                            if (valAsStr.length() <= currentText.length() - selectedText.length()) {
                                return State.Intermediate;
                            } else {
                                return State.Invalid;
                            }
                        }
                    }
                    showInvalidValueToolTip(result.getInvalidValueReason().getMessage(getEnvironment().getMessageProvider(), InvalidValueReason.EMessageType.Value));
                    return State.Invalid;
                }
            }
        }

        private void showInvalidValueToolTip(final String toolTip) {
            if (!QToolTip.isVisible() && getLineEdit() != null && toolTip != null && !toolTip.isEmpty()) {
                final QPoint toolTipPoint = getLineEdit().mapToGlobal(getLineEdit().rect().bottomLeft());
                QToolTip.showText(toolTipPoint, toolTip, getLineEdit());
            }
        }
    }

    private final class CompletionModel extends QAbstractTableModel {

        public final static int FILTER_MAX_COUNT = 10;
        private IEditingHistory editingHistory;
        private boolean isFiltered = false;

        public CompletionModel(final QObject parent) {
            super(parent);
        }

        public void setEditingHistoryFiltered(final boolean flag) {
            if (isFiltered != flag) {
                isFiltered = flag;
                reset();
            }
        }

        public void setEditingHistory(final IEditingHistory editingHistory) {
            this.editingHistory = editingHistory;
        }

        public void addValue(final T value) {
            if (ValEditor.this.calcValidationResult(value) == ValidationResult.ACCEPTABLE) {
                editingHistory.addEntry(ValEditor.this.valueAsStr(value));
                reset();
            }
        }

        public String getRealValueAsStr(final int index) {
            return editingHistory.getEntry(index);
        }

        @Override
        public Object data(final QModelIndex modelIndex, final int role) {
            try {
                final int row = modelIndex.row();
                if (canShowData(row)) {
                    if (role == Qt.ItemDataRole.DisplayRole || role == Qt.ItemDataRole.EditRole) {
                        return ValEditor.this.getStringToShow(
                                ValEditor.this.stringAsValue(getRealValueAsStr(row)));
                    } else if (role == Qt.ItemDataRole.UserRole) {
                        return getRealValueAsStr(row);
                    } else if (role == Qt.ItemDataRole.FontRole) {
                        return QApplication.font();
                    }
                }
                return null;
            } catch (RuntimeException ex) {
                Application.getInstance().getTracer().error(ex);
                return null;
            }
        }

        /**
         * Функция принятия решения о наложении фильтрации [R(x,y) = NOT x OR y]
         *
         * @param row номер строки, по которому данные распологаются в модели
         * @return <i>true</i> - показать данные, <i>false</i> - отфильтровать данные
         */
        private boolean canShowData(final int row) {
            return !isFiltered | row < FILTER_MAX_COUNT;
        }

        @Override
        public int columnCount(final QModelIndex qmi) {
            return 1;
        }

        @Override
        public int rowCount(final QModelIndex qmi) {
            if (isFiltered) {
                return Math.min(FILTER_MAX_COUNT, editingHistory.getSize());
            }
            return editingHistory.getSize();
        }
    } //CompletionModel class

    private static final class ApplyTextOptionsEvent extends QEvent {

        private final ExplorerTextOptions options;

        public ApplyTextOptionsEvent(ExplorerTextOptions newOptions) {
            super(QEvent.Type.User);
            options = newOptions;
        }

        public final ExplorerTextOptions getOptions() {
            return options;
        }
    }
    
    protected final static String LINE_EDIT_SS = "QLineEdit, QLabel {color: %s;}";
    private final static int MIN_BUTTON_WIDTH = 20;
    private EditMask editMask;
    private boolean mandatory;
    private boolean readOnly;
    protected T value;
    private T previousValue /*Значение, которое было до начала редактирования*/;
    private boolean isInModificationState = false;
    private boolean isAutoValidationEnabled = true;
    private EReactionToIntermediateInput reactionToIntermediateInput = EReactionToIntermediateInput.SHOW_WARNING;
    private ValidationResult internalValueStatus = ValidationResult.ACCEPTABLE, externalValueStatus = ValidationResult.ACCEPTABLE;
    private ExplorerTextOptions textOptions, scheduledToApplyOptions;
    private long sheduledApplyOptionsEventId = 0;
    private long scheduledFocusInEventId = 0;
    private EnumSet<ETextOptionsMarker> textOptionsMarkers = EnumSet.of(ETextOptionsMarker.EDITOR);
    private CustomTextOptions customTextOptions;
    private ITextOptionsProvider customTextOptionsProvider;
    private ITextOptionsProvider provider;
    private int leftMargin = 2;
    private boolean inGrid = false;    
    private boolean widthByContentMode;
    private boolean isHighlightedFrame = false;
    private final int frameWidth;
    protected final List<QPushButton> pButtons = new ArrayList<>();        
    private final PriorityArray<QWidget> additionalWidgets = new PriorityArray<>(false,false);
    private final ValEditorLayoutWrapper layout = new ValEditorLayoutWrapper(this);
    protected final QToolButton clearBtn, editingHistoryBtn;    
    private final QToolButton predefValuesBtn;
    //private final InternalLineEdit lineEdit;
    private final InternalLineEdit internalLineEdit;
    private final QLineEdit actualLineEdit;
    private final InternalValidator validator = new InternalValidator(this);
    protected IEditingHistory editingHistory = null;
    //protected EPropertyValueStorePossibility possibility;
    private final CompletionModel completionModelHistory;
    private final QStringListModel completionModelPredef;
    private IDisplayStringProvider displayStringProvider;
    protected List<T> predefinedValues = null;
    private String lineEditStyleSheet = "";
    private int lastKey;
    private boolean wasClosed;
    private UnacceptableInput unacceptableInput;
    private String dialogTitle;
    private int maxPredefinedValuesInPopup = -1;    
    
    protected final QEventFilter inputEventListener = new QEventFilter(this){
        @Override
        public boolean eventFilter(final QObject target, final QEvent event) {
            return ValEditor.this.eventFilter(target, event);
        }
    };
    
    /**
     * Сигнал срабатывает при программном или пользовательском изменении значения свойства.
     */
    public final com.trolltech.qt.QSignalEmitter.Signal1<Object> valueChanged = new com.trolltech.qt.QSignalEmitter.Signal1<>();
    public final com.trolltech.qt.QSignalEmitter.Signal1<ValidationResult> validationResultChanged = new com.trolltech.qt.QSignalEmitter.Signal1<>();
    public final com.trolltech.qt.QSignalEmitter.Signal1<Object> editingFinished = new Signal1<>();

    @SuppressWarnings("LeakingThisInConstructor")
    public ValEditor(final IClientEnvironment environment,
            final QWidget parent,
            final EditMask editMask,
            final boolean mandatory,
            final boolean readOnly) {
        this(environment, parent, editMask, mandatory, readOnly, null);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public ValEditor(final IClientEnvironment environment,
            final QWidget parent,
            final EditMask editMask,
            final boolean mandatory,
            final boolean readOnly,
            final QLineEdit customLineEdit) {
        super(environment, parent);

        this.editMask = EditMask.newCopy(editMask);
        this.mandatory = mandatory;
        this.readOnly = readOnly;

        if (customLineEdit == null) {
            internalLineEdit = new InternalLineEdit(this);
            actualLineEdit = internalLineEdit;
        } else {
            internalLineEdit = null;
            actualLineEdit = customLineEdit;
        }
        actualLineEdit.setReadOnly(readOnly);
        actualLineEdit.setText(getNoValueStr());
        actualLineEdit.textEdited.connect(this, "onTextEdited(String)");
        actualLineEdit.textChanged.connect(this, "onTextChanged(String)");
        actualLineEdit.selectionChanged.connect(this, "onSelectionChanged()");
        inputEventListener.setProcessableEventTypes(EnumSet.of(QEvent.Type.KeyPress, QEvent.Type.Resize));
        actualLineEdit.installEventFilter(this);        
        layout.setPrimaryWidget(actualLineEdit);

        setFrameShadow(QFrame.Shadow.Sunken);
        setFrameShape(QFrame.Shape.StyledPanel);

        frameWidth = frameWidth() * 2;

        setSizePolicy(Policy.Expanding, Policy.Fixed);
        setFocusPolicy(Qt.FocusPolicy.StrongFocus);//Необходимо для смены фокуса по клавише Tab
        setFocusProxy(actualLineEdit);//Необходимо для программной установки фокуса
        {
            editingHistoryBtn = 
                createPredefinedButton(environment.getMessageProvider().translate("Value", "Show Editing History"), 
                                           ClientIcon.CommonOperations.EDITING_HISTORY, "showCompletitions()", "editing_history");
        }
        {
            clearBtn = 
                createPredefinedButton(environment.getMessageProvider().translate("Value", "Clear Value"), 
                                           ClientIcon.CommonOperations.CLEAR, "clear()","clear_value");
        }
        {
            predefValuesBtn = 
                createPredefinedButton(environment.getMessageProvider().translate("Value", "Show predefined values"), 
                                           ClientIcon.CommonOperations.FAVORITES_POPUP, "showPredefinedValues()", "predefined_values");
        }
        completionModelHistory = new CompletionModel(this);
        completionModelPredef = new QStringListModel(this);
        provider = environment.getTextOptionsProvider();
        textOptions = ExplorerTextOptions.EMPTY;
        textOptionsMarkers.add(ETextOptionsMarker.UNDEFINED_VALUE);
        if (readOnly) {
            textOptionsMarkers.add(ETextOptionsMarker.READONLY_VALUE);
        }
        if (mandatory) {
            textOptionsMarkers.add(ETextOptionsMarker.MANDATORY_VALUE);
        }
        refreshTextOptions();
    }
    
    private final QToolButton createPredefinedButton(final String toolTip, 
                                                                               final ClientIcon icon, 
                                                                               final String clickHandler,
                                                                               final String objectName){
        final QAction action = new QAction(this);
        action.triggered.connect(this, clickHandler);
        action.setToolTip(toolTip);
        action.setIcon(ExplorerIcon.getQIcon(icon));
        final QToolButton button = WidgetUtils.createEditorButton(this, action);
        setupToolButton(button);
        final int buttonIndex = additionalWidgets.addWithLowestPriority(button)+layout.getFirstButtonIndex();
        layout.insertAdditionalWidget(buttonIndex, button);
        button.pressed.connect(actualLineEdit, "setFocus()");
        button.setVisible(false);
        button.setObjectName("rx_tbtn_"+objectName);
        return button;
    }

    @SuppressWarnings("unused")
    protected void onTextEdited(final String newText) {
        final InvalidValueReason oldInvalidValueReason = 
            unacceptableInput==null ? null : unacceptableInput.getReason();
        final ValidationResult currentInputTextStatus = validateInputText(newText);
        final InvalidValueReason currentInvalidValueReason =
            currentInputTextStatus==ValidationResult.ACCEPTABLE ? null : currentInputTextStatus.getInvalidValueReason();//NOPMD
        if (currentInputTextStatus==ValidationResult.ACCEPTABLE){//NOPMD
            unacceptableInput = null;
        }else{
            unacceptableInput = 
                new UnacceptableInput(getEnvironment(), currentInputTextStatus.getInvalidValueReason(), newText);
        }
        if (isValidationOnInputEnabled()){
            if (oldInvalidValueReason!=currentInvalidValueReason){//NOPMD
                afterChangeValueStatus();
            }
        }else if (oldInvalidValueReason!=null 
                  && textOptionsMarkers.contains(ETextOptionsMarker.INVALID_VALUE)){
            afterChangeValueStatus();
        }
    }

    @SuppressWarnings("unused")
    protected void onTextChanged(final String newText) {
        validator.setCurrentText(getValue() == null ? null : newText);

    }

    @SuppressWarnings("unused")
    private void onSelectionChanged() {
        if (getLineEdit() != null) {
            validator.setSelectedText(getLineEdit().selectedText());
        }
    }

    protected void onFocusIn() {
        if (getLineEdit() != null) {
            getLineEdit().selectAll();
            updateValueMarkers(false);
            repaint();
        }
    }

    @SuppressWarnings("PMD.MissingBreakInSwitch")
    protected void onFocusOut() {
        if (inModificationState()) {
            doValidation();
            //In modification state cannot use getValidationResult()
            if ( hasAcceptableInput()
                 && externalValueStatus == ValidationResult.ACCEPTABLE //NOPMD
                 && internalValueStatus == ValidationResult.ACCEPTABLE //NOPMD
               ) {
                editingFinished.emit(value);
                updateHistory();
            } else {                
                switch (getReactionToIntermediateInput()) {
                    case DISCARD_INPUT: {
                        revertModifications();
                        break;
                    }
                    case SHOW_WARNING: {
                        if (hasAcceptableInput()){
                            if (internalLineEdit != null) {
                                internalLineEdit.scheduleShowValidationToolTip();
                            }
                        }else{
                            afterChangeValueStatus();
                        }
                        break;
                    }
                    default: {//NOPMD
                    }
                }
            }
            setModificationState(false);
        }else if (!hasAcceptableInput()
                  && getReactionToIntermediateInput()==EReactionToIntermediateInput.SHOW_WARNING){//NOPMD
            afterChangeValueStatus();
        }
        clearBtn.setVisible(!isMandatory() && !isReadOnly() && (getValue() != null || !hasAcceptableInput()));
        updateValueMarkers(true);
        repaint();
    }

    public void updateHistory() {
        if (editingHistory != null && value != null) {
            completionModelHistory.addValue(value);
        }
    }

    public final boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(final boolean mandatory) {
        this.mandatory = mandatory;
        clearBtn.setVisible(!isMandatory() && !isReadOnly() && (getValue() != null || !hasAcceptableInput()));
        updateValueMarkers(false);
    }

    public final boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
        clearBtn.setVisible(!isMandatory() && !isReadOnly() && (getValue() != null || !hasAcceptableInput()));
        editingHistoryBtn.setVisible(!isReadOnly() && editingHistory != null && !editingHistory.isEmpty());
        predefValuesBtn.setVisible(!isReadOnly() && predefinedValues != null && !predefinedValues.isEmpty());
        if (getValidationResult() != ValidationResult.ACCEPTABLE) {
            afterChangeValueStatus();
        }
        updateReadOnlyMarker();
    }

    @Inspectable(delegate = "org.radixware.kernel.explorer.inspector.delegates.EditMaskDelegate" ,name = "editMask")
    public final EditMask getEditMask() {
        return EditMask.newCopy(editMask);
    }

    @InspectablePropertySetter(name = "editMask")
    public void setEditMask(final EditMask editMask) {
        this.editMask = EditMask.newCopy(editMask);
        // update noValueString
        if (getValue() == null) {
            setOnlyText(getStringToShow(null), null);
            getLineEdit().clearFocus();
        }
        doValidation();
    }

    public final EReactionToIntermediateInput getReactionToIntermediateInput() {
        return reactionToIntermediateInput;
    }

    public final void setReactionToIntermediateInput(final EReactionToIntermediateInput reaction) {
        if (reaction == null) {
            throw new NullPointerException("value of reaction cannot be null");
        }
        reactionToIntermediateInput = reaction;
    }

    public final boolean isAutoValidationEnabled() {
        return isAutoValidationEnabled;
    }

    public final void setAutoValidationEnabled(final boolean isEnabled) {
        isAutoValidationEnabled = isEnabled;
    }

    public final boolean isValidationOnInputEnabled() {
        return reactionToIntermediateInput == EReactionToIntermediateInput.DISCARD_INPUT;
    }
    
    protected final boolean addWidget(final QWidget widget, final int priority) {
        final int widgetIndex = additionalWidgets.addWithPriority(widget, priority);
        if (widgetIndex>=0){
            layout.insertAdditionalWidget(widgetIndex+layout.getFirstButtonIndex(), widget);
            return true;
        }else{
            return false;
        }
    }

    protected final void addWidget(final QWidget widget) {
        final int widgetIndex = additionalWidgets.addWithHighestPriority(widget);
        if (widgetIndex>=0){
            layout.insertAdditionalWidget(widgetIndex+layout.getFirstButtonIndex(), widget);
        }
    }
    
    protected final void insertWidgetToFront(final QWidget widget){
        layout.insertAdditionalWidget(0, widget);
    }
    
    @Deprecated//use insertWidgetToFront or addWidget with priority instead
    protected final void insertWidget(final int index, final QWidget widget){
        if (index>=layout.getFirstButtonIndex()){
            if (additionalWidgets.addWithLowestPriority(widget)<0){
                return;
            }
        }
        layout.insertAdditionalWidget(index, widget);
    }
    
    protected final void removeWidget(final QWidget widget){
        additionalWidgets.remove(widget);
        layout.removeAdditionalWidget(widget);
    }
    
    protected final int widgetsCount(){
        return layout.itemsCount();
    }
    
    protected final void setPrimaryWidget(final QWidget widget){
        layout.setPrimaryWidget(widget);
    }

    public final QLineEdit getLineEdit() {
        return actualLineEdit;
    }

    public final String getNoValueStr() {
        if (getEditMask() == null) {
            return "";
        }
        return getEditMask().toStr(getEnvironment(), null);
    }

    public void setFrame(final boolean frame) {
        if (frame) {
            setFrameShadow(QFrame.Shadow.Sunken);
            setFrameShape(QFrame.Shape.StyledPanel);
        } else {
            setFrameShadow(QFrame.Shadow.Plain);
            setFrameShape(QFrame.Shape.NoFrame);
        }
    }

    public void changeStateForGrid() {
        setFrame(false);
        if (getLineEdit() != null) {
            setSizePolicy(Policy.MinimumExpanding, Policy.MinimumExpanding);
            setAutoFillBackground(true);
            final String color = getLineEdit().palette().color(getLineEdit().backgroundRole()).name();
            setStyleSheet(styleSheet() + "QFrame:{background-color: " + color + "};");
            setFocusPolicy(Qt.FocusPolicy.ClickFocus);
        }
        inGrid = true;
        setAdjustWidthByContentModeEnabled(true);
    }
    
    public final void setAdjustWidthByContentModeEnabled(final boolean enabled){
        if (widthByContentMode!=enabled){
            widthByContentMode = enabled;
            updateGeometry();
        }
    }
    
    public final boolean isAdjustWidthByContentModeEnabled(){
        return widthByContentMode;
    }

    public final void setHighlightedFrame(final boolean isHighlighted) {
        if (isHighlightedFrame != isHighlighted) {
            isHighlightedFrame = isHighlighted;
            repaint();
        }
    }

    public final boolean isHighlightedFrame() {
        return isHighlightedFrame;
    }

    public final ValidationResult getValidationResult() {
        if (inModificationState() && !isValidationOnInputEnabled()) {
            return ValidationResult.ACCEPTABLE;
        } else {
            if (hasAcceptableInput()){
                return externalValueStatus == ValidationResult.ACCEPTABLE ? internalValueStatus : externalValueStatus;
            }else if (isValidationOnInputEnabled()){
                return ValidationResult.Factory.newIntermediateResult(getUnacceptableInput().getReason());
            }else{
                final QLineEdit lineEdit = getLineEdit();
                if (lineEdit!=null && lineEdit.hasFocus()){
                    return ValidationResult.ACCEPTABLE;
                }else{
                    return ValidationResult.Factory.newIntermediateResult(getUnacceptableInput().getReason());
                }
            }
        }
    }

    public final void setValidationResult(final ValidationResult newValueStatus) {
        if (externalValueStatus != newValueStatus) {//NOPMD
            final ValidationResult oldResult = getValidationResult();
            externalValueStatus = newValueStatus;
            if (oldResult != getValidationResult()) {//NOPMD
                afterChangeValueStatus();
            }
        }
    }
    
    public final boolean hasAcceptableInput(){
        return unacceptableInput==null;
    }
    
    public final UnacceptableInput getUnacceptableInput(){
        return unacceptableInput;
    }
    
    public final boolean checkInput(final String messageTitle, final String firstMessageLine){
        if (hasAcceptableInput()){
            return true;
        }else{
            getUnacceptableInput().showMessage(messageTitle, firstMessageLine);
            setFocus();
            return false;
        }
    }
    
    public final boolean checkInput(){
        return checkInput(null, null);
    }
    
    protected ValidationResult validateInputText(final String text){
        return ValidationResult.ACCEPTABLE;
    }
    
    /*
    protected final void setInputTextValidationResult(final ValidationResult newInputTextStatus){
        if (inputTextStatus != newInputTextStatus){//NOPMD
            final ValidationResult oldStatus = getValidationResult();
            inputTextStatus = newInputTextStatus;
            final ValidationResult newStatus = getValidationResult();
            if (oldStatus != newStatus){//NOPMD
                afterChangeValueStatus();
            }
        }
    }*/    
    

    protected ValidationResult calcValidationResult(final T value) {
        return getEditMask().validate(getEnvironment(), value);
    }

    protected void setInputValidator(final IInputValidator validator) {
        if (getLineEdit() != null) {
            if (validator == null) {
                getLineEdit().setValidator(null);
            } else {
                this.validator.setInputValidator(validator);
                if (getLineEdit().validator() != this.validator) {
                    getLineEdit().setValidator(this.validator);
                }
                this.validator.setCheckingEnabled(true);
            }
        }
    }

    protected final void doValidation() {
        if (isAutoValidationEnabled()) {
            if (hasAcceptableInput()){
                final ValidationResult currentStatus = getValidationResult();
                final ValidationResult newStatus = calcValidationResult(getValue());
                if (newStatus != currentStatus || internalValueStatus != newStatus) {//NOPMD
                    internalValueStatus = newStatus;
                    afterChangeValueStatus();
                    validationResultChanged.emit(getValidationResult());
                }
            }
        }
    }

    private boolean afterChangeValueStatus() {
        if (getValidationResult() == ValidationResult.ACCEPTABLE || !userCanChangeValue()) {
            if (internalLineEdit != null) {
                internalLineEdit.hideInvalidValueIcon();
            }
            if (inModificationState()) {
                QToolTip.hideText();
            }
            return removeTextOptionsMarkers(ETextOptionsMarker.INVALID_VALUE);
        } else {
            if (internalLineEdit != null) {
                internalLineEdit.showInvalidValueIcon(getInvalidValueToolTip());
            }
            return addTextOptionsMarkers(ETextOptionsMarker.INVALID_VALUE);
        }
    }

    @SuppressWarnings("PMD.ConsecutiveLiteralAppends")
    private String getInvalidValueToolTip() {
        final StringBuilder toolTipHtml = new StringBuilder(256);
        toolTipHtml.append("<table border=\"0\" width=\"100%\">");
        if (getReactionToIntermediateInput() == EReactionToIntermediateInput.DISCARD_INPUT
                && inModificationState() && !eq(value, previousValue)) {
            toolTipHtml.append("<tr><th width=\"100%\">");
            toolTipHtml.append(getEnvironment().getMessageProvider().translate("ExplorerMessage", "Attention!"));
            toolTipHtml.append("</th></tr>");
            toolTipHtml.append("<tr><th width=\"100%\">");
            toolTipHtml.append(getEnvironment().getMessageProvider().translate("ExplorerMessage", "Your changes will be discarded!"));
            toolTipHtml.append("</th></tr>");
        }
        toolTipHtml.append("<tr><td width=\"80%\">");
        final String comment = 
            getValidationResult().getInvalidValueReason().getMessage(getEnvironment().getMessageProvider(), InvalidValueReason.EMessageType.Value);
        if (comment == null || comment.isEmpty()) {
            toolTipHtml.append(getEnvironment().getMessageProvider().translate("Value", "Current value is invalid"));
        } else {
            toolTipHtml.append(comment);
        }
        toolTipHtml.append("</td>");
        toolTipHtml.append("<td width=\"10%\"></td><td width=\"10%\"></td></tr>");
        toolTipHtml.append("<tr><td width=\"100%\"></td></tr>");
        toolTipHtml.append("</table>");

        return toolTipHtml.toString();
    }

    private void revertModifications() {
        setValue(previousValue);
    }

    private void setModificationState(final boolean inModification) {
        if (inModification) {
            previousValue = value;
        }
        isInModificationState = inModification;
        if (internalLineEdit != null) {
            internalLineEdit.setInvalidValueIconToolTip(getInvalidValueToolTip());
        }
        if (!isValidationOnInputEnabled()) {
            afterChangeValueStatus();
        }
    }

    protected final boolean inModificationState() {
        return isInModificationState;
    }

    private QStyleOptionFrameV2 getStyleOptionFrame() {
        final QStyleOptionFrameV2 opt = new QStyleOptionFrameV2();
        opt.initFrom(internalLineEdit);
        final QStyle.State state = opt.state();

        switch (frameShadow()) {
            case Sunken: {
                state.set(QStyle.StateFlag.State_Sunken);
                break;
            }
            case Raised: {
                state.set(QStyle.StateFlag.State_Raised);
                break;
            }
        }
        if (hasFocus() || isHighlightedFrame()) {
            state.set(QStyle.StateFlag.State_HasFocus);
        }
        opt.setMidLineWidth(0);
        opt.setState(state);
        opt.setRect(frameRect());
        opt.setLineWidth(frameWidth());
        opt.setFeatures(QStyleOptionFrameV2.FrameFeature.Flat);

        if (isHighlightedFrame()) {
            final QColor color = QApplication.palette().color(QPalette.ColorRole.Highlight);
            final QPalette palette = new QPalette(opt.palette());
            palette.setColor(QPalette.ColorRole.Window, color);
            palette.setColor(QPalette.ColorRole.Dark, color);
            palette.setColor(QPalette.ColorRole.Light, color);
            palette.setColor(QPalette.ColorRole.Shadow, color);
            palette.setColor(QPalette.ColorRole.Midlight, color);
            palette.setColor(QPalette.ColorRole.Highlight, color);
            opt.setPalette(palette);
        }
        return opt;
    }

    @Override
    protected void paintEvent(final QPaintEvent event) {
        if (frameShape() == QFrame.Shape.NoFrame) {
            super.paintEvent(event);
        } else {
            final QPainter painter = new QPainter(this);
            final QStyleOptionFrame styleOptionFrame = getStyleOptionFrame();
            if (isNativePaintingStyle()) {
                COMMON_STYLE.drawPrimitive(QStyle.PrimitiveElement.PE_PanelLineEdit, styleOptionFrame, painter, this);
            } else {
                style().drawPrimitive(QStyle.PrimitiveElement.PE_PanelLineEdit, styleOptionFrame, painter, this);
            }

            if (((style() instanceof com.trolltech.qt.gui.QPlastiqueStyle) || (style() instanceof com.trolltech.qt.gui.QCleanlooksStyle))
                    && parentWidget() != null) {
                // Shaded corner dots
                final QPen oldPen = painter.pen();
                final QPoint points[] = new QPoint[12];
                final QRect rect = rect();
                painter.setPen(parentWidget().palette().color(QPalette.ColorRole.Window));
                points[0] = new QPoint(rect.left(), rect.top() + 1);
                points[1] = new QPoint(rect.left(), rect.bottom() - 1);
                points[2] = new QPoint(rect.left() + 1, rect.top());
                points[3] = new QPoint(rect.left() + 1, rect.bottom());
                points[4] = new QPoint(rect.right(), rect.top() + 1);
                points[5] = new QPoint(rect.right(), rect.bottom() - 1);
                points[6] = new QPoint(rect.right() - 1, rect.top());
                points[7] = new QPoint(rect.right() - 1, rect.bottom());
                points[8] = new QPoint(rect.left(), rect.top());
                points[9] = new QPoint(rect.left(), rect.bottom());
                points[10] = new QPoint(rect.right(), rect.top());
                points[11] = new QPoint(rect.right(), rect.bottom());
                for (QPoint point : points) {
                    painter.drawPoint(point);
                }
                painter.setPen(oldPen);
            }
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof ApplyTextOptionsEvent) {            
            event.accept();
            if (sheduledApplyOptionsEventId==0){
                return;
            }else{
                sheduledApplyOptionsEventId = 0;
            }
            if (isVisible()) {
                final ExplorerTextOptions newOptions = ((ApplyTextOptionsEvent) event).getOptions();
                if (nativeId() != 0 && getLineEdit().nativeId() != 0 && scheduledToApplyOptions != null && !wasClosed) {
                    scheduledToApplyOptions = null;
                    try {
                        applyTextOptionsInternal(newOptions);
                    } catch (RuntimeException exception) {
                        getEnvironment().getTracer().error(exception);
                    }
                }
            }
        } else if (event instanceof SetFocusEvent){            
            event.accept();
            if (scheduledFocusInEventId==0){
                return;
            }else{
                scheduledFocusInEventId = 0;
            }
            if (!wasClosed() && isVisible() && isEnabled()){
                final QLineEdit lineEdit = getLineEdit();
                if (lineEdit==null){
                    setFocus();                    
                }else{
                    lineEdit.setFocus();
                }
            }
        } else {
            super.customEvent(event);
        }
    }

    @Override
    protected void showEvent(final QShowEvent event) {
        if (scheduledToApplyOptions != null) {
            final ExplorerTextOptions newOptions = scheduledToApplyOptions;
            scheduledToApplyOptions = null;
            applyTextOptionsInternal(newOptions);
        }
        super.showEvent(event);
    }

    @Override
    public QSize sizeHint() {
        final QSize size = super.sizeHint();
        size.setHeight(getLineEdit().sizeHint().height() + frameWidth);
        if (isAdjustWidthByContentModeEnabled()) {
            size.setWidth(getWidthForCurrentValue() + 8);
        }
        return size;
    }

    @Override
    public QSize minimumSizeHint() {
        final QSize size = super.minimumSizeHint();
        size.setHeight(sizeHint().height());
        return size;
    }

    private int getWidthForCurrentValue() {//RADIX-728, RADIX-373
        //layout.activate();
        //this.updateGeometry();
        final QFontMetrics fm = getLineEdit().fontMetrics();
        final Qt.LayoutDirection direction =
                isRightToLeft() ? Qt.LayoutDirection.RightToLeft : Qt.LayoutDirection.LeftToRight;
        final Qt.Alignment align = QStyle.visualAlignment(direction, getLineEdit().alignment());
        align.clear(Qt.AlignmentFlag.AlignVCenter);
        align.clear(Qt.AlignmentFlag.AlignHCenter);
        final int width = fm.boundingRect(0, 0, 2000, 2000, align.value(), getLineEdit().text()).width();
        final int delta = width() - getLineEdit().width(); //ширина, различных кнопочек
        final int contentWidth = width + delta;
        return frameShape() == QFrame.Shape.NoFrame ? contentWidth : contentWidth + frameWidth;
    }

    public T getValue() {
        return value;
    }

    protected boolean eq(final T value1, final T value2) {
        return (value1 == null ? value2 == null : value1.equals(value2));
    }

    protected boolean setOnlyValue(final T value) {        
        if (eq(this.value, value)) {
            if (!hasAcceptableInput()){
                unacceptableInput = null;
                afterChangeValueStatus();
                refresh();
            }            
            return false;
        }
        
        if (!hasAcceptableInput()){
            unacceptableInput = null;
            afterChangeValueStatus();
        }        
        
        if (!inModificationState() && getLineEdit() != null && getLineEdit().hasFocus()) {
            setModificationState(true);
        }

        this.value = value;
        setValidationResult(ValidationResult.ACCEPTABLE);

        if (!inModificationState() || isValidationOnInputEnabled()) {
            doValidation();
        }

        if (getValidationResult() != ValidationResult.ACCEPTABLE && internalLineEdit != null) {
            internalLineEdit.setInvalidValueIconToolTip(getInvalidValueToolTip());
            if (inModificationState() && isValidationOnInputEnabled() && !eq(this.value, previousValue)) {
                internalLineEdit.showValidationToolTip();
            }
        }

        setCompleterUnfiltered(false);

        valueChanged.emit(value);
        return true;
    }

    protected final void setOnlyText(final String text, final String inputMask) {
        final QLineEdit lineEdit = getLineEdit();
        if (lineEdit != null) {
            final QCompleter completer = lineEdit.completer();
            lineEdit.blockSignals(true);
            try {
                if (completer != null) {
                    lineEdit.setCompleter(null);
                }
                if (inputMask != null) {
                    lineEdit.setInputMask(inputMask);
                }
                lineEdit.setText(text);
                validator.setCurrentText(getValue() == null ? null : text);
                if (completer != null) {
                    lineEdit.setCompleter(completer);
                }
            } finally {
                lineEdit.blockSignals(false);
            }
        }
    }

    public void setValue(final T value) {
        if (setOnlyValue(value)) {
            refresh();
            //colorNullValued(value);
            updateValueMarkers(true);
        }
    }

    public void refresh() { 
        validator.setCheckingEnabled(false);
        try {
            final QLineEdit lineEdit = getLineEdit();
            if (hasAcceptableInput()){
                setOnlyText(getStringToShow(value), null);
            }else{
                if (lineEdit!=null){
                    lineEdit.setText(unacceptableInput.getText());
                }
            }
            if (lineEdit!=null){
                lineEdit.setCursorPosition(0);
                lineEdit.clearFocus();                
            }
        } finally {
            validator.setCheckingEnabled(true);
        }

        clearBtn.setVisible(!isMandatory() && !isReadOnly() && (getValue() != null || !hasAcceptableInput()));
    }

    protected String getStringToShow(final Object value) {
        final String defaultString = getEditMask().toStr(getEnvironment(), value);
        if (displayStringProvider == null) {
            return defaultString;
        }
        return displayStringProvider.format(getEditMask(), value, defaultString);
    }

    @Override
    public boolean eventFilter(final QObject obj, final QEvent event) {
        if (event != null && event.type() == QEvent.Type.KeyPress && (event instanceof QKeyEvent) && !readOnly) {
            final QKeyEvent keyEvent = (QKeyEvent) event;
            final boolean isControl = keyEvent.modifiers().value() == KeyboardModifier.ControlModifier.value()
                    || (keyEvent.modifiers().value() == KeyboardModifier.MetaModifier.value() && SystemTools.isOSX);
            final boolean isSpace = keyEvent.key() == Qt.Key.Key_Space.value();
            if (isControl && isSpace && !mandatory) {
                setValue(null);
                return true;
            } else if (isControl && keyEvent.key() == Qt.Key.Key_Down.value() && editingHistory != null) {
                showCompletitions();
                return true;
            }
            lastKey = keyEvent.key();
            return false;
        }
        lastKey = 0;
        if (event != null && event.type() == QEvent.Type.Resize) {
            int margin = 2;
            for (QPushButton button : pButtons) {
                if (getLineEdit().contentsRect().height() > 0) {
                    button.setFixedHeight(getLineEdit().contentsRect().height() - 4);
                }
                button.move(margin, 2);
                margin += button.width() + 2;
            }
            updateButtonsSize();
            return false;
        }
        return super.eventFilter(obj, event);
    }

    public final QToolButton addButton(final String text, final QAction action) {
        final QToolButton button = WidgetUtils.createEditorButton(this, action);
        if (text != null) {
            button.setText(text);
        }
        addButton(button);
        return button;
    }
    
    public final void addButton(final QToolButton button) {
        addButton(button, DEFAULT_BUTTON_PRIORITY);
    }

    public final void addButton(final QToolButton button, final int priority) {
        setupToolButton(button);
        if (addWidget(button, priority)){
            button.pressed.connect(getLineEdit(), "setFocus()");
            updateReadOnlyMarker();            
        }
    }
    
    public final void addFirstButton(final QToolButton button){
        addButton(button, Integer.MAX_VALUE);
    }
    
    public final void removeButton(final QToolButton button) {
        removeButton(button, true);
    }
    
    public final void removeButton(final QToolButton button, final boolean dispose){
        if (additionalWidgets.remove(button)){
            layout.removeAdditionalWidget(button);
            button.setParent(null);
            if (dispose){
                button.close();
            }
            updateReadOnlyMarker();
        }
    }

    private void setupToolButton(final QToolButton button) {
        button.setParent(this);
        final int buttonSize = getLineEdit().sizeHint().height();
        button.setFixedWidth(buttonSize);
        button.setFixedHeight(buttonSize);
        button.setSizePolicy(new QSizePolicy(Policy.Fixed, Policy.Fixed));
        button.setAttribute(WidgetAttribute.WA_DeleteOnClose);
        button.setFocusPolicy(FocusPolicy.NoFocus);
        updateButtonIconSize(button);
    }

    public QPushButton insertButton(final String buttonText) {
        final QLineEdit editor = getLineEdit();
        final QPushButton button = new QPushButton(buttonText, editor);
        final QFont font = button.font();
        font.setPointSize(font.pointSize() - 1);
        button.setFont(font);
        button.setContentsMargins(0, 0, 0, 0);
        button.setParent(editor);
        button.setSizePolicy(Policy.Expanding, Policy.Ignored);
        button.setFocusPolicy(FocusPolicy.NoFocus);
        pButtons.add(button);
        if (editor.sizeHint().height() > 0) {
            button.setFixedHeight(editor.sizeHint().height() - 2);
        }
        button.move(leftMargin, 2);
        leftMargin += button.sizeHint().width() + 2;
        editor.setContentsMargins(leftMargin, 0, 0, 0);
        updateReadOnlyMarker();
        return button;
    }

    protected final int getContentMargin() {
        return leftMargin;
    }

    protected boolean hasCustomContextMenu() {
        return false;
    }

    protected QMenu createCustomContextMenu(final QMenu standardMenu) {
        return standardMenu;
    }
    
    protected static QAction createAction(final Object parent, final String methodName) {
        final QAction action = new QAction(null);
        action.triggered.connect(parent, methodName);
        return action;        
    }

    protected static QAction createAction(final Object parent, final String methodName, final String objectName) {
        final QAction action = new QAction(null);
        action.triggered.connect(parent, methodName);        
        action.setObjectName(objectName);
        return action;
    }

    protected final void updateValueMarkers(final boolean force) {
        final QLineEdit editor = getLineEdit();
        final EnumSet<ETextOptionsMarker> markers = textOptionsMarkers.clone();
        if (getValue() == null && (editor == null || !editor.hasFocus() || editor.isReadOnly())) {
            markers.add(ETextOptionsMarker.UNDEFINED_VALUE);
        } else {
            markers.remove(ETextOptionsMarker.UNDEFINED_VALUE);
        }
        if (getValue() == null && isMandatory()) {
            markers.add(ETextOptionsMarker.MANDATORY_VALUE);
        } else {
            markers.remove(ETextOptionsMarker.MANDATORY_VALUE);
        }
        if (!textOptionsMarkers.equals(markers)) {
            applyTextOptionsMarkers(markers, force);
        }
    }

    protected boolean isButtonCanChangeValue(final QAbstractButton button) {
        return !button.isHidden() && button.isEnabled();
    }

    protected void updateReadOnlyMarker() {
        if (userCanChangeValue()) {
            removeTextOptionsMarkers(ETextOptionsMarker.READONLY_VALUE);
        } else {
            addTextOptionsMarkers(ETextOptionsMarker.READONLY_VALUE);
        }
    }

    private boolean userCanChangeValue() {
        if (isReadOnly()) {
            for (QWidget widget : additionalWidgets) {
                if (widget instanceof QToolButton
                    && isButtonCanChangeValue((QToolButton)widget)){
                    return true;
                }
            }
            for (QPushButton button : pButtons) {
                if (isButtonCanChangeValue(button)) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public final boolean addTextOptionsMarkers(final ETextOptionsMarker... markers) {
        final EnumSet<ETextOptionsMarker> newMarkers = textOptionsMarkers.clone();
        newMarkers.addAll(Arrays.asList(markers));
        return applyTextOptionsMarkers(newMarkers, false);
    }

    public final boolean removeTextOptionsMarkers(final ETextOptionsMarker... markers) {
        final EnumSet<ETextOptionsMarker> newMarkers = textOptionsMarkers.clone();
        newMarkers.removeAll(Arrays.asList(markers));
        return applyTextOptionsMarkers(newMarkers, false);
    }

    public final boolean setTextOptionsMarkers(final ETextOptionsMarker... markers) {
        final EnumSet<ETextOptionsMarker> newMarkers = EnumSet.noneOf(ETextOptionsMarker.class);
        newMarkers.addAll(Arrays.asList(markers));
        return applyTextOptionsMarkers(newMarkers, false);
    }

    public final EnumSet<ETextOptionsMarker> getTextOptionsMarkers() {
        return textOptionsMarkers.clone();
    }

    private void initTextOptionsProvider() {
        final ITextOptionsManager manager = getEnvironment().getApplication().getTextOptionsManager();
        final ITextOptionsProvider defaultProvider = getEnvironment().getTextOptionsProvider();
        if (customTextOptionsProvider == null) {
            if (customTextOptions == null) {
                provider = defaultProvider;
            } else {
                provider = new MergedTextOptionsProvider(defaultProvider, customTextOptions, manager);
            }
        } else {
            final ITextOptionsProvider mergedProvider =
                    new MergedTextOptionsProvider(defaultProvider, customTextOptionsProvider, manager);
            if (customTextOptions == null) {
                provider = mergedProvider;
            } else {
                provider = new MergedTextOptionsProvider(customTextOptions, mergedProvider, manager);
            }
        }
    }

    public final void setTextOptionsProvider(final ITextOptionsProvider textOptionsProvider) {
        if (customTextOptionsProvider != textOptionsProvider) {//NOPMD
            customTextOptionsProvider = textOptionsProvider;
            initTextOptionsProvider();
            refreshTextOptions();
        }
    }

    private CustomTextOptions getCustomTextOptions() {
        if (customTextOptions == null) {
            customTextOptions = new CustomTextOptions();
            initTextOptionsProvider();
        }
        return customTextOptions;
    }

    public final void setDefaultTextOptions(final ExplorerTextOptions options) {
        if (options != null || customTextOptions != null) {
            if (options == null) {
                customTextOptions = null;
                initTextOptionsProvider();
                refreshTextOptions();
            } else {
                final CustomTextOptions customOptions = getCustomTextOptions();
                final ExplorerTextOptions curOptions = (ExplorerTextOptions) customOptions.getDefaultOptions();
                if (!Objects.equals(curOptions, options)) {
                    customOptions.setDefaultOptions(options);
                    refreshTextOptions();
                }
            }
        }
    }

    public final void setTextOptionsForMarker(final ETextOptionsMarker marker, final ExplorerTextOptions options) {
        if (marker != null && (options != null || customTextOptions != null)) {
            final CustomTextOptions customOptions = getCustomTextOptions();
            final EnumSet<ETextOptionsMarker> markers = EnumSet.of(marker);
            if (!Objects.equals(options, customOptions.getOptions(markers, null))) {
                customOptions.putOptions(options, markers);
                refreshTextOptions();
            }
        }
    }

    public final ExplorerTextOptions getTextOptions() {
        return scheduledToApplyOptions == null ? textOptions : scheduledToApplyOptions;
    }

    private boolean applyTextOptionsMarkers(final EnumSet<ETextOptionsMarker> newMarkers, final boolean force) {
        if (newMarkers != null && !textOptionsMarkers.equals(newMarkers)) {
            textOptionsMarkers = newMarkers.clone();
            if (force) {
                if (sheduledApplyOptionsEventId != 0) {
                    Application.removeScheduledEvent(sheduledApplyOptionsEventId);
                    sheduledApplyOptionsEventId = 0;
                }
                scheduledToApplyOptions = null;
                applyTextOptionsInternal(calculateTextOptions(textOptionsMarkers));
            } else {
                refreshTextOptions();
            }
            return true;
        }
        return false;
    }

    protected ExplorerTextOptions calculateTextOptions(final EnumSet<ETextOptionsMarker> markers) {
        final ExplorerTextOptions options = (ExplorerTextOptions) provider.getOptions(markers, null);
        if (options == null) {
            final StringBuilder traceMessage = new StringBuilder(256);
            traceMessage.append("Unable to get text options.\n");
            if (markers == null) {
                traceMessage.append("Text options markers is null");
            } else if (markers.isEmpty()) {
                traceMessage.append("Text options markers is empty");
            } else {
                traceMessage.append("Text options markers: [");
                for (ETextOptionsMarker marker : markers) {
                    traceMessage.append(marker.name());
                    traceMessage.append(' ');
                }
            }
            traceMessage.append("]\nText options provider is ");
            traceMessage.append(provider.getClass().getName());
            traceMessage.append("\nCurrent settings group is '");
            traceMessage.append(getEnvironment().getConfigStore().group());
            traceMessage.append('\'');
            getEnvironment().getTracer().warning(traceMessage.toString());
        }
        return options;
    }

    public final void refreshTextOptions() {
        postApplyTextOptionsEvent(calculateTextOptions(textOptionsMarkers));
    }

    private void postApplyTextOptionsEvent(final ExplorerTextOptions newOptions) {
        if (scheduledToApplyOptions != null && scheduledToApplyOptions.equals(newOptions)) {
            return;
        }
        if (scheduledToApplyOptions != null || !textOptions.equals(newOptions)) {
            if (sheduledApplyOptionsEventId != 0) {
                Application.removeScheduledEvent(sheduledApplyOptionsEventId);
                sheduledApplyOptionsEventId = 0;
            }
            scheduledToApplyOptions = newOptions;
            sheduledApplyOptionsEventId =
                    Application.processEventWhenEasSessionReady(this, new ApplyTextOptionsEvent(newOptions));
        }
    }

    private void applyTextOptionsInternal(final ExplorerTextOptions options) {
        if (options != null) {
            applyTextOptions(options);
            textOptions = options;
        }
    }

    protected void applyTextOptions(final ExplorerTextOptions options) {
        final QLineEdit edit = getLineEdit();
        if (edit != null) {
            if (options.getFont() != null && !options.getFont().equals(textOptions.getFont())) {
                if (lineEditStyleSheet != null && !lineEditStyleSheet.isEmpty()) {
                    edit.setStyleSheet(null);
                    lineEditStyleSheet = "";
                }
                edit.setFont(options.getQFont());
            }
            final String newStyleSheet;
            if (isEnabled()) {
                newStyleSheet = "QLineEdit, QTextEdit { " + options.getStyleSheet() + " }";
            } else {
                newStyleSheet = "";
            }
            changeLineEditStyleSheet(newStyleSheet);
        }
    }

    private void applyFontInternal(final ExplorerFont font) {
        if (!Objects.equals(textOptions.getFont(), font)) {
            applyFont(font);
            textOptions = textOptions.changeFont(font);
        }
    }

    protected void applyFont(final ExplorerFont newFont) {
        if (newFont != null) {
            final QLineEdit edit = getLineEdit();
            if (edit != null) {
                if (lineEditStyleSheet != null && !lineEditStyleSheet.isEmpty()) {
                    edit.setStyleSheet(null);
                    edit.setFont(newFont.getQFont());
                    changeStyleSheet(edit, lineEditStyleSheet, newFont.getQFont());
                } else {
                    edit.setFont(newFont.getQFont());
                }
            }
        }
    }

    //Если у виджета верхнего уровня используется stylesheet (или stylesheet был задан всему приложению), 
    //то установка stylesheet вложенному виджету может сбросить настройки шрифта.
    //Данный метод проверяет настройки шрифта после изменения stylesheet. Если они не соответствуют ожидаемым, то производится перенастройка.
    protected static void changeStyleSheet(final QWidget widget, final String stylesheet) {
        changeStyleSheet(widget, stylesheet, null);
    }

    protected static void changeStyleSheet(final QWidget widget, final String stylesheet, final QFont font) {
        final QFont expectedFont = font == null ? widget.font() : font;
        widget.setStyleSheet(stylesheet);
        if (!expectedFont.toString().equals(widget.font().toString())) {
            widget.setStyleSheet(null);
            widget.setFont(expectedFont);
            widget.setStyleSheet(stylesheet);
        }
    }

    @Override
    public boolean event(QEvent e) {
        if (e.type() == QEvent.Type.EnabledChange) {
            try {
                if (!wasClosed && !afterChangeValueStatus()) {
                    applyTextOptions(textOptions);
                }
            } catch (RuntimeException exception) {
                getEnvironment().getTracer().error(exception);
            }
        } else if (e.type() == QEvent.Type.FontChange) {
            final boolean result = super.event(e);
            if (!wasClosed && result) {
                try {
                    onChangeFontEvent(ExplorerFont.Factory.getFont(font()));
                } catch (RuntimeException exception) {
                    getEnvironment().getTracer().error(exception);
                }
            }
            return result;
        }
        return super.event(e);
    }

    protected void onChangeFontEvent(final ExplorerFont font) {
        final CustomTextOptions customOptions = getCustomTextOptions();
        if (customOptions.getDefaultOptions() == null) {
            customOptions.setDefaultOptions(ExplorerTextOptions.Factory.getOptions(font));
        } else {
            customOptions.setDefaultOptions(customOptions.getDefaultOptions().changeFont(font));
        }
        if (scheduledToApplyOptions == null) {
            applyFontInternal(font);
        } else {
            postApplyTextOptionsEvent(scheduledToApplyOptions.changeFont(font));
        }
    }

    protected final boolean changeLineEditStyleSheet(final String newStyleSheet) {
        if (!Objects.equals(newStyleSheet, lineEditStyleSheet)) {//optimization
            lineEditStyleSheet = newStyleSheet;
            changeStyleSheet(getLineEdit(), lineEditStyleSheet);
            return true;
        } else {
            return false;
        }
    }

    private void updateButtonsSize() {
        if (getLineEdit() != null) {
            final int buttonHeight = getLineEdit().sizeHint().height();
            final int buttonWidth = Math.max(buttonHeight, MIN_BUTTON_WIDTH);
            QToolButton button;
            for (QWidget widget : additionalWidgets) {
                if (widget instanceof QToolButton){
                    button = (QToolButton)widget;
                    button.setFixedHeight(buttonHeight);
                    button.setFixedWidth(buttonWidth);
                    updateButtonIconSize(button);
                }
            }
            clearBtn.setFixedHeight(buttonHeight);
            clearBtn.setFixedWidth(buttonWidth);
            updateButtonIconSize(clearBtn);
            editingHistoryBtn.setFixedHeight(buttonHeight);
            editingHistoryBtn.setFixedWidth(buttonWidth);
            updateButtonIconSize(editingHistoryBtn);
            predefValuesBtn.setFixedHeight(buttonHeight);
            predefValuesBtn.setFixedWidth(buttonWidth);
            updateButtonIconSize(predefValuesBtn);
        }
    }

    private static void updateButtonIconSize(final QToolButton button) {
        final QSize currentIconSize = button.iconSize();
        final QStyleOptionToolButton option = new QStyleOptionToolButton();
        option.initFrom(button);
        final int fw =
                button.style().proxy().pixelMetric(QStyle.PixelMetric.PM_DefaultFrameWidth, option, button);
        final QRect workArea = option.rect().adjusted(fw, fw, -fw, -fw);
        final int iconSize = Math.min(workArea.height(), workArea.width());
        if (currentIconSize.width() != iconSize || currentIconSize.height() != iconSize) {
            button.setIconSize(new QSize(iconSize, iconSize));
        }
    }

    protected void onMouseClick() {
    }

    protected boolean onMouseDoubleClick() {
        if (isEnabled() && isEmbeddedWidgetsVisible()){
            QToolButton button;
            for (QWidget widget : additionalWidgets) {
                if (widget instanceof QToolButton && widget!=clearBtn){
                    button = (QToolButton)widget;
                    if (button.isVisible()) {
                        button.click();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void clear() {
        setValue(null);
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        pButtons.clear();
        additionalWidgets.clear();
        QApplication.clipboard().disconnect(internalLineEdit);
        wasClosed = true;
        if (sheduledApplyOptionsEventId != 0) {
            Application.removeScheduledEvent(sheduledApplyOptionsEventId);
            sheduledApplyOptionsEventId = 0;
        }
        if (scheduledFocusInEventId != 0){
            Application.removeScheduledEvent(scheduledFocusInEventId);
            scheduledFocusInEventId = 0;
        }
        QApplication.removePostedEvents(this, QEvent.Type.User.value());
        scheduledToApplyOptions = null;
        try {
            if (editingHistory != null) {
                editingHistory.flush();
            }
        } catch (EditingHistoryException ex) {
            getEnvironment().getTracer().error(ex);
        } finally {
            super.closeEvent(event);
        }
    }

    @SuppressWarnings("PMD.MissingBreakInSwitch")
    public static ValEditor createForValType(IClientEnvironment userSession, final EValType valType, final EditMask mask, final boolean isMandatory, final boolean isReadonly, final QWidget parent) {
        if (valType.isArrayType()) {
            final ValEditor editor =
                    new ValArrEditor(userSession, valType, null, parent, isMandatory, isReadonly);
            editor.setEditMask(mask);
            return editor;
        } else if (mask.getType() == null) {
            switch (valType) {
                case BIN:
                case BLOB:
                    return new ValBinEditor(userSession, parent, (EditMaskNone) mask, isMandatory, isReadonly);
                case BOOL:
                    return new ValBoolEditor(userSession, parent, (EditMaskNone) mask, isMandatory, isReadonly);
                case XML:
                    return new ValXmlEditor(userSession, parent, isMandatory, isReadonly, false);
                default:
                    throw new IllegalArgumentException("Can't create editor for " + valType.getName() + " type");
            }
        } else {
            switch (mask.getType()) {
                case DATE_TIME:
                    return new ValDateTimeEditor(userSession, parent, (EditMaskDateTime) mask, isMandatory, isReadonly);
                case ENUM:
                    return new ValConstSetEditor(userSession, parent, (EditMaskConstSet) mask, isMandatory, isReadonly);
                case INT:
                    return new ValIntEditor(userSession, parent, (EditMaskInt) mask, isMandatory, isReadonly);
                case LIST:
                    return new ValListEditor(userSession, parent, (EditMaskList) mask, isMandatory, isReadonly);
                case NUM:
                    return new ValNumEditor(userSession, parent, (EditMaskNum) mask, isMandatory, isReadonly);
                case STR:
                    if (valType == EValType.CHAR) {
                        return new ValCharEditor(userSession, parent, (EditMaskStr) mask, isMandatory, isReadonly);
                    } else {
                        return new ValStrEditor(userSession, parent, (EditMaskStr) mask, isMandatory, isReadonly);
                    }
                case TIME_INTERVAL:
                    return new ValTimeIntervalEditor(userSession, parent, (EditMaskTimeInterval) mask, isMandatory, isReadonly);
                case BOOL:
                    return new AdvancedValBoolEditor(userSession, parent, (EditMaskBool) mask, isMandatory, isReadonly);//AdvancedEditor debug
                case FILE_PATH:
                    return new ValFilePathEditor(userSession, parent, (EditMaskFilePath) mask, isMandatory, isReadonly);
                case OBJECT_REFERENCE:
                    return new ValRefEditor(userSession, parent, (EditMaskRef) mask, isMandatory, isReadonly);
                default:
                    throw new IllegalArgumentException("Can't create editor for " + mask.getType().getName() + " edit mask");
            }
        }
    }

    public void setEditingHistory(final IEditingHistory history) {
        if (history == null) {
            try {
                if (editingHistory != null) {
                    editingHistory.flush();
                }
            } catch (EditingHistoryException ex) {
                getEnvironment().getTracer().error(ex);
            }
            this.editingHistory = null;
            getLineEdit().setCompleter(null);
        } else {
            this.editingHistory = history;
            completionModelHistory.setEditingHistory(editingHistory);
            final QCompleter completer = new QCompleter();
            completer.setModel(completionModelHistory);
            getLineEdit().setCompleter(completer);
            completer.highlighted.disconnect();
            completer.highlightedIndex.disconnect();
            completer.activatedIndex.connect(this, "onSelectCompletion(QModelIndex)");
        }
        editingHistoryBtn.setVisible(!isReadOnly() && editingHistory != null && !editingHistory.isEmpty());
    }

    public IEditingHistory getEditingHistory() {
        return editingHistory;
    }

    protected void showCompletitions() {
        final QCompleter completer = getLineEdit().completer();

        if (!isReadOnly() && completer != null) {
            completer.activatedIndex.disconnect();
            completer.activatedIndex.connect(this, "onSelectCompletion(QModelIndex)");
            completer.setModel(getHistoryModel());
            setCompleterUnfiltered(true);
            completer.complete();
        }
        //isInModificationState = true;
    }

    protected String valueAsStr(final T value) {
        return null;
    }

    protected T stringAsValue(final String stringVal) {
        return null;
    }

    protected void onSelectCompletion(final QModelIndex filterModelIndex) {
        if (filterModelIndex != null) {
            final T currentValue = stringAsValue(
                    String.valueOf(
                    filterModelIndex.data(Qt.ItemDataRole.UserRole)));
            setOnlyValue(currentValue);
            refresh();  // RADIX-6098: поскольку комплитер устанавливает текст в поле редактирования
            // форсированно, то необходимо сбросить текущую Qt-маску поля ввода для корректного отображения значения
            if (!inModificationState()) {
                updateHistory();
            }
        }
    }

    public IDisplayStringProvider getDisplayStringProvider() {
        return displayStringProvider;
    }

    public void setDisplayStringProvider(final IDisplayStringProvider displayStringProvider) {
        this.displayStringProvider = displayStringProvider;
    }

    void setCompleterUnfiltered(final boolean flag) {
        final QCompleter completer = getLineEdit().completer();
        if (completer != null) {
            if (editingHistory != null && completionModelHistory != null) {
                completionModelHistory.setEditingHistoryFiltered(flag);
            }
            //Если flag=false, это значит, что автодополнение должно происходить в обычном режиме
            //  с фильтрацией по введенным символам
            //Если flag=true, то включается режим, при котором показываются 10 последних значений
            //  (независимо от текущего ввода)
            completer.setCompletionMode(
                    flag
                    ? QCompleter.CompletionMode.UnfilteredPopupCompletion
                    : QCompleter.CompletionMode.PopupCompletion);
        }
    }

    public void setPredefinedValues(final List<T> predefValues) {
        if (predefValues == null) {
            if (predefinedValues != null) {
                predefinedValues.clear();
            }
        } else {
            if (predefinedValues == null) {
                predefinedValues = new LinkedList<>();
            } else {
                predefinedValues.clear();
            }

            for (T t : predefValues) {
                if (editMask.isSpecialValue(t) || calcValidationResult(t) != ValidationResult.ACCEPTABLE) {
                    continue;
                } else {
                    predefinedValues.add(t);
                }
            }
            QCompleter completer = getLineEdit().completer();
            if (completer == null) {
                completer = new QCompleter();
                getLineEdit().setCompleter(completer);
            }

        }
        refreshShowPredefinedValuesButton();        
    }

    public List<T> getPredefinedValues() {
        return predefinedValues == null ? Collections.<T>emptyList() : Collections.unmodifiableList(this.predefinedValues);
    }

    protected List<String> getPredefinedValuesTitles() {
        if (predefinedValues == null) {
            return Collections.<String>emptyList();
        } else {
            final List<String> result = new LinkedList<>();
            for (T t : predefinedValues) {
                result.add(getStringToShow(t));
            }
            return result;
        }
    }

    @SuppressWarnings("unused")
    private void showPredefinedValues() {
        if (!isReadOnly() && predefinedValues!=null){
            if (predefinedValues.size()>getMaxPredefinedValuesInDropDownList()){
                final List<String> titles = getPredefinedValuesTitles();
                final List<ListWidgetItem> items = new LinkedList<>();
                for (String title: titles){
                    items.add(new ListWidgetItem(title));
                }
                final int selectedItemIndex = selectItem(items, -1);
                if (selectedItemIndex>=0){
                    choosePredefinedValue(selectedItemIndex);
                }
            }else{
                final QCompleter completer = getLineEdit().completer();
                if (completer != null) {
                    completer.activatedIndex.disconnect();                    
                    completer.activatedIndex.connect(this, "onPredefValueSelect(QModelIndex)");                    
                    completionModelPredef.setStringList(getPredefinedValuesTitles());
                    completer.setModel(completionModelPredef);
                    setCompleterUnfiltered(true);
                    setFocus();
                    completer.complete();
                }
            }            
        }
    }

    @SuppressWarnings("unused")
    private void onPredefValueSelect(final QModelIndex index) {
        if (index != null) {
            final QStringListModel model = (QStringListModel) getLineEdit().completer().model();
            final int valueIndex = model.stringList().indexOf((String) index.data());
            choosePredefinedValue(valueIndex);
        }
    }
    
    private void choosePredefinedValue(final int valueIndex){
        final T newValue = predefinedValues.get(valueIndex);
        setOnlyValue(newValue);
        if (!inModificationState()) {
            updateHistory();
        }        
    }
    
    public final void refreshShowPredefinedValuesButton(){
        final boolean isButtonVisible = predefinedValues != null && !predefinedValues.isEmpty() && !isReadOnly();
        predefValuesBtn.setHidden(!isButtonVisible);
        if (isButtonVisible){
            final QIcon btnIcon;
            if (predefinedValues.size()>getMaxPredefinedValuesInDropDownList()){
                btnIcon = ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FAVORITES);                
            }else{
                btnIcon = ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FAVORITES_POPUP);
            }
            predefValuesBtn.setIcon(btnIcon);
        }
    }
    
    public final int getMaxPredefinedValuesInDropDownList(){
        if (maxPredefinedValuesInPopup<0){
            return getEnvironment().getConfigStore().readInteger(MAX_ITEMS_IN_DD_LIST_SETTING_KEY);
        }else{
            return maxPredefinedValuesInPopup;
        }
    }
    
    public final void setMaxPredefinedValuesInDropDownList(final int max){
        if (maxPredefinedValuesInPopup!=max){
            maxPredefinedValuesInPopup = max;
            refreshShowPredefinedValuesButton();
        }
    }
    
    public final boolean isEmbeddedWidgetsVisible(){
        return layout.isAdditionalWidgetsVisible();
    }
    
    public final void setEmbeddedWidgetsVisible(final boolean isVisible){
        if (isVisible!=layout.isAdditionalWidgetsVisible()){
            layout.setAdditionalWidgetsVisible(isVisible);
            updateGeometry();
            repaint();
        }
    }
    
    public final String getDialogTitle(){
        return dialogTitle;
    }
    
    public final void setDialogTitle(final String title){
        dialogTitle = title;
    }        
    
    protected void beforeShowSelectValueDialog(final IListDialog dialog){
        
    }
            
    protected String getSelectValueDialogConfigPrefix(){
        return getClass().getName();
    }    
    
    protected final int selectItem(final List<ListWidgetItem> items, final int currentIndex){
        final IListDialog dialog = 
            getEnvironment().getApplication().getDialogFactory().newListDialog(getEnvironment(), this, getSelectValueDialogConfigPrefix());
        dialog.setItems(items);
        dialog.setFeatures(EnumSet.of(IListWidget.EFeatures.FILTERING, IListWidget.EFeatures.MANUAL_SORTING));
        if (dialogTitle!=null){
            dialog.setWindowTitle(dialogTitle);
        }
        if (currentIndex>=0){
            dialog.setCurrentRow(currentIndex);
        }
        beforeShowSelectValueDialog(dialog);
        if (dialog.execDialog(this)==IDialog.DialogResult.ACCEPTED){
            return dialog.getCurrentRow();
        }else{
            return -1;
        }
    }
    
    protected QAbstractItemModel getHistoryModel() {
        return completionModelHistory;
    }

    protected final boolean isNativePaintingStyle() {
        final String currentStyleName = Application.getCurrentStyleName();
        return "windowsvista".equals(currentStyleName) || "windowsxp".equals(currentStyleName);
    }

    protected void setWidthHintCalculator(final IWidthHintCalculator newCalculator) {
        if (internalLineEdit != null) {
            internalLineEdit.setWidthHintCalculator(newCalculator);
        }
        updateGeometry();
    }
            
    public void scheduleSetFocus(){
        if (scheduledFocusInEventId==0){
            scheduledFocusInEventId = Application.processEventWhenEasSessionReady(this, new SetFocusEvent());
        }
    }      

    protected final int getLastKey() {
        return lastKey;
    }

    protected final boolean wasClosed() {
        return wasClosed;
    }
    
    protected String beforeCopyToClipboard(final String text){
        return text;
    };
    
    protected boolean beforePasteFromClipboard(final String text){
        return true;
    }
    
    public final void setInputText(final String inputText){
        if (inputText!=null && !inputText.isEmpty()){
            final QLineEdit lineEdit = getLineEdit();
            if (lineEdit!=null){                        
                setReactionToIntermediateInput(ValEditor.EReactionToIntermediateInput.DISCARD_INPUT);
                try{
                    lineEdit.setText(inputText);
                    lineEdit.textEdited.emit(inputText);
                }finally{
                    setReactionToIntermediateInput(ValEditor.EReactionToIntermediateInput.SHOW_WARNING);
                }
                //lineEdit.setText(unacceptableInput.getText());
            }        
        }
    }      
    
    protected final boolean isInGrid(){
        return inGrid;
    }        
}