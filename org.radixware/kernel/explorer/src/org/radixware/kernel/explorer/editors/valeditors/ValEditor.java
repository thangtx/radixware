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
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
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
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QLineEdit.TextMargins;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
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
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
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

    public static enum EReactionToIntermediateInput {
        NONE,
        SHOW_WARNING,
        DISCARD_INPUT
    }
    
    protected final static QStyle COMMON_STYLE = new QCommonStyle();

    protected interface IWidthHintCalculator {

        int getWidthHint(QFontMetrics fontMetrics, EditMask editMask, IClientEnvironment environment);
    }

    protected static class DefaultWidthHintCalculator implements IWidthHintCalculator {

        private DefaultWidthHintCalculator() {
        }
        public static final DefaultWidthHintCalculator INSTANCE = new DefaultWidthHintCalculator();

        @Override
        public int getWidthHint(final QFontMetrics fontMetrics, final EditMask editMask, final IClientEnvironment environment) {
            final int minWidth = fontMetrics.width("x") * 10;
            if (editMask == null) {
                return minWidth;
            } else {
                return Math.max(minWidth, fontMetrics.width(editMask.toStr(environment, null)));
            }
        }
    }

    private final class InternalLineEdit extends QLineEdit {

        private final static String TOOLTIP_STYLESHEET = "QToolTip { background-color: palegoldenrod; "
                + "border-color: red; border-width: 2px; border-style: solid; padding: 3px; "
                + "border-radius: 4px;}";
        private final static int ICON_MARGIN = 2;
        private final static int VERTICAL_MARGIN = 1;//from qlineedit.cpp: #define verticalMargin 1
        private final static int HORIZONTAL_MARGIN = 1;//from qlineedit.cpp: #define horizontalMargin 2        
        private final QLabel lbInvalidValueIcon = new QLabel(this);
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
            lbInvalidValueIcon.setVisible(true);
            lbInvalidValueIcon.setToolTip(toolTip);
        }

        public void setInvalidValueIconToolTip(final String toolTip) {
            lbInvalidValueIcon.setToolTip(toolTip);
        }

        public void hideInvalidValueIcon() {
            lbInvalidValueIcon.setVisible(false);
            setTextMargins(0, 0, 0, 0);
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
            final TextMargins textMargins = this.getTextMargins();
            final ExplorerFont font = ExplorerFont.Factory.getFont(font());
            final QFontMetrics fm = font.getQFontMetrics();
            final int h = Math.max(fm.lineSpacing(), 14) + 2 * VERTICAL_MARGIN
                    + textMargins.top + textMargins.bottom
                    + margins.top + margins.bottom;
            final int w = widthHintCalculator.getWidthHint(fm, getEditMask(), getEnvironment())
                    + 2 * HORIZONTAL_MARGIN
                    + textMargins.left + textMargins.right
                    + margins.left + margins.right; // "some"
            QStyleOptionFrame opt = new QStyleOptionFrameV2();
            initStyleOption(opt);
            final QSize sizeHint =
                    COMMON_STYLE.sizeFromContents(QStyle.ContentsType.CT_LineEdit, opt, new QSize(w, h).
                    expandedTo(QApplication.globalStrut()), this);
            sizeHint.setWidth(sizeHint.width() - getTextMargins().right);
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
            onMouseDoubleClick();
        }

        @Override
        protected void contextMenuEvent(final QContextMenuEvent event) {
            QMenu menu = createStandardContextMenu();
            final List<QAction> actions = menu.actions();
            if (isReadOnly() && actions.size()>0){
                actions.get(0).triggered.disconnect();
                actions.get(0).triggered.connect(this,"copyToClipboard()");
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
            if (event.matches(QKeySequence.StandardKey.Cut)){
                cutToClipboard();
                event.accept();
                return;
            }
            if (event.matches(QKeySequence.StandardKey.DeleteEndOfLine)){
                if (echoMode()==EchoMode.Normal && !isReadOnly()){                                        
                    setSelection(cursorPosition(), displayText().length()-1);
                    cutToClipboard();
                    event.accept();
                    return;
                }
            }
            if (event.matches(QKeySequence.StandardKey.Paste)){
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
                    showInvalidValueToolTip(result.getInvalidValueReason().toString());
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
    private EnumSet<ETextOptionsMarker> textOptionsMarkers = EnumSet.of(ETextOptionsMarker.EDITOR);
    private CustomTextOptions customTextOptions;
    private ITextOptionsProvider customTextOptionsProvider;
    private ITextOptionsProvider provider;
    private int leftMargin = 2;
    private boolean inGrid = false;
    private boolean isHighlightedFrame = false;
    private final int frameWidth;
    protected final List<QPushButton> pButtons = new ArrayList<>();
    private final List<QToolButton> buttons = new ArrayList<>();
    private final QHBoxLayout layout = WidgetUtils.createHBoxLayout(this);
    protected final QToolButton clearBtn, editingHistoryBtn, predefValuesBtn;
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
        layout.setAlignment(new Qt.Alignment(new Qt.AlignmentFlag[]{AlignmentFlag.AlignLeft, AlignmentFlag.AlignVCenter}));
        layout.addWidget(actualLineEdit);

        layout.setSizeConstraint(QLayout.SizeConstraint.SetNoConstraint);

        setFrameShadow(QFrame.Shadow.Sunken);
        setFrameShape(QFrame.Shape.StyledPanel);

        frameWidth = frameWidth() * 2;

        setSizePolicy(Policy.Expanding, Policy.Fixed);
        setFocusPolicy(Qt.FocusPolicy.StrongFocus);//Необходимо для смены фокуса по клавише Tab
        setFocusProxy(actualLineEdit);//Необходимо для программной установки фокуса

        {
            final QAction action = new QAction(this);
            action.triggered.connect(this, "showCompletitions()");
            action.setToolTip(environment.getMessageProvider().translate("Value", "Show Editing History"));
            action.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.EDITING_HISTORY));
            editingHistoryBtn = WidgetUtils.createEditorButton(this, action);
            setupToolButton(editingHistoryBtn);
            getLayout().addWidget(editingHistoryBtn, 0, new Alignment(Qt.AlignmentFlag.AlignRight));
            editingHistoryBtn.pressed.connect(actualLineEdit, "setFocus()");
            editingHistoryBtn.setObjectName("btnEditingHistory");
            editingHistoryBtn.setVisible(false);
        }

        {
            final QAction action = new QAction(this);
            action.triggered.connect(this, "clear()");
            action.setToolTip(environment.getMessageProvider().translate("Value", "Clear Value"));
            action.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CLEAR));
            clearBtn = WidgetUtils.createEditorButton(this, action);
            setupToolButton(clearBtn);
            getLayout().addWidget(clearBtn, 0, new Alignment(Qt.AlignmentFlag.AlignRight));
            clearBtn.pressed.connect(actualLineEdit, "setFocus()");
            clearBtn.setObjectName("btnClear");
            clearBtn.setVisible(false);
        }
        {
            final QAction action = new QAction(this);
            action.triggered.connect(this, "showPredefinedValues()");
            action.setToolTip(environment.getMessageProvider().translate("Value", "Show predefined values"));
            action.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FAVORITES));
            predefValuesBtn = WidgetUtils.createEditorButton(this, action);
            setupToolButton(predefValuesBtn);
            getLayout().addWidget(predefValuesBtn, 0, new Alignment(Qt.AlignmentFlag.AlignRight));
            predefValuesBtn.pressed.connect(actualLineEdit, "setFocus()");
            predefValuesBtn.setVisible(false);
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

    public final EditMask getEditMask() {
        return EditMask.newCopy(editMask);
    }

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

    protected final QHBoxLayout getLayout() {
        return layout;
    }

    protected final void addWidget(final QWidget widget) {
        getLayout().insertWidget(/*getLayout().count() - */1, widget);
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
        final String comment = getValidationResult().getInvalidValueReason().toString();
        if (comment == null || comment.isEmpty()) {
            toolTipHtml.append(getEnvironment().getMessageProvider().translate("Value", "Current value is invalid"));
        } else {
            toolTipHtml.append(getValidationResult().getInvalidValueReason().toString());
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
        if (isInGrid()) {
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
        setupToolButton(button);
        buttons.add(button);
        final int index = getLayout().count() - 2;//Кнопки очистить и история редактирования всегда последнии
        getLayout().insertWidget(index, button, 0, new Alignment(Qt.AlignmentFlag.AlignRight));
        button.pressed.connect(getLineEdit(), "setFocus()");
        updateReadOnlyMarker();
    }
    
    public final void addFirstButton(final QToolButton button){
        final int index = getLayout().count() - buttons.size();
        setupToolButton(button);
        buttons.add(0,button);
        getLayout().insertWidget(index, button, 0, new Alignment(Qt.AlignmentFlag.AlignRight));
        button.pressed.connect(getLineEdit(), "setFocus()");
        updateReadOnlyMarker();        
    }

    public final void removeButton(final QToolButton button) {
        buttons.remove(button);
        getLayout().removeWidget(button);
        button.setParent(null);
        button.close();
        updateReadOnlyMarker();
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
        action.triggered.connect(parent, "onChangeValueClick()");
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
            final List<QToolButton> allButtons = new LinkedList<>(buttons);
            allButtons.add(clearBtn);
            allButtons.add(predefValuesBtn);
            allButtons.add(editingHistoryBtn);
            for (QToolButton button : allButtons) {
                if (isButtonCanChangeValue(button)) {
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
                provider = new MergedTextOptionsProvider(mergedProvider, customTextOptions, manager);
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
                QApplication.removePostedEvents(this, QEvent.Type.User.value());
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
            QApplication.removePostedEvents(this, QEvent.Type.User.value());
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
            for (QToolButton button : buttons) {
                button.setFixedHeight(buttonHeight);
                button.setFixedWidth(buttonWidth);
                updateButtonIconSize(button);
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

    protected void onMouseDoubleClick() {
        for (QToolButton button : buttons) {
            if (button.isVisible()) {
                button.click();
                return;
            }
        }
    }

    public void clear() {
        setValue(null);
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        pButtons.clear();
        buttons.clear();
        QApplication.clipboard().disconnect(internalLineEdit);
        wasClosed = true;
        if (sheduledApplyOptionsEventId != 0) {
            Application.removeScheduledEvent(sheduledApplyOptionsEventId);
            sheduledApplyOptionsEventId = 0;
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
        predefValuesBtn.setHidden(predefinedValues == null || predefinedValues.isEmpty() || isReadOnly());
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
        final QCompleter completer = getLineEdit().completer();
        if (!isReadOnly() && completer != null) {
            completer.activatedIndex.disconnect();
            completer.activatedIndex.connect(this, "onPredefValueSelect(QModelIndex)");
            completionModelPredef.setStringList(getPredefinedValuesTitles());
            completer.setModel(completionModelPredef);
            setCompleterUnfiltered(true);
            setFocus();
            completer.complete();
        }
    }

    @SuppressWarnings("unused")
    private void onPredefValueSelect(final QModelIndex index) {
        if (index != null) {
            final QStringListModel model = (QStringListModel) getLineEdit().completer().model();
            final int valueIndex = model.stringList().indexOf((String) index.data());
            final T newValue = predefinedValues.get(valueIndex);


            setOnlyValue(newValue);
            if (!inModificationState()) {
                updateHistory();
            }
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