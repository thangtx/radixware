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

package org.radixware.kernel.explorer.widgets.propeditors;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QContextMenuEvent;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QTextOption.WrapMode;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.Objects;
import org.radixware.kernel.common.client.editors.property.PropEditorController;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyClob;
import org.radixware.kernel.common.client.models.items.properties.PropertyStr;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ExplorerTextEdit;


public class PropTextEditor extends AbstractPropEditor {

    private static final class ApplyTextOptionsEvent extends QEvent {

        private final ExplorerTextOptions options;

        public ApplyTextOptionsEvent(final ExplorerTextOptions newOptions) {
            super(QEvent.Type.User);
            options = newOptions;
        }

        public ExplorerTextOptions getOptions() {
            return options;
        }
    }
    private ExplorerTextOptions textOptions, scheduledToApplyOptions;    
    private long scheduledApplyOptionsEventId;
    private EnumSet<ETextOptionsMarker> textOptionsMarkers = EnumSet.of(ETextOptionsMarker.EDITOR);
    private String textEditStyleSheet = "";
    private final static int MINIMAL_EDITOR_HEIGHT = 80;
    private final QWidget buttonsContainer = new QWidget(this);
    private QVBoxLayout buttonsLayout;
    private ExplorerTextEdit textEdit;
    private String value;
    private boolean isInModificationState = false;
    private boolean wasClosed;

    public PropTextEditor(final PropertyStr property) {
        super(property);
        createUi();
        updateSettings();
    }

    public PropTextEditor(final PropertyClob property) {
        super(property);
        createUi();
        updateSettings();
    }

    private void createUi() {
        final EValType valType = controller.getProperty().getDefinition().getType();
        if (valType != EValType.STR && valType != EValType.CLOB) {
            throw new IllegalArgumentException("String or CLOB property expected, but " + valType.getName() + " property got");
        }
        
        textOptions = ExplorerTextOptions.EMPTY;
        textOptionsMarkers.add(ETextOptionsMarker.UNDEFINED_VALUE);

        textEdit = new ExplorerTextEdit(this) {
            @Override
            public QSize sizeHint() {
                final QSize size = super.sizeHint();
                if (size.height() - 200 > MINIMAL_EDITOR_HEIGHT) {
                    size.setHeight(size.height() - 200);
                } else {
                    size.setHeight(MINIMAL_EDITOR_HEIGHT);
                }
                return size;
            }
        };
        textEdit.textChanged.connect(this, "onTextChanged()");
        if (getProperty().isReadonly()) {
            textOptionsMarkers.add(ETextOptionsMarker.READONLY_VALUE);
        }
        if (getProperty().isMandatory()) {
            textOptionsMarkers.add(ETextOptionsMarker.MANDATORY_VALUE);
        }

        controller.addFocusListener(new PropEditorController.FocusListener() {
            @Override
            public void focused() {                
                onFocusIn();
            }

            @Override
            public void unfocused() {
                onFocusOut();
            }
        });
        textEdit.setFrameShadow(QFrame.Shadow.Sunken);
        textEdit.setFrameShape(QFrame.Shape.StyledPanel);
        textEdit.setSizePolicy(Policy.Expanding, Policy.Expanding);
        textEdit.setAutoFillBackground(true);
        textEdit.setWordWrapMode(WrapMode.NoWrap);
        focusListener.setProcessableEventTypes(EnumSet.of(QEvent.Type.FocusIn, QEvent.Type.FocusOut, QEvent.Type.KeyPress));
        textEdit.installEventFilter(focusListener);
        textEdit.setObjectName("textEdit");        
        final QHBoxLayout layout = WidgetUtils.createHBoxLayout(this);
        buttonsContainer.setObjectName("rx_buttonsContainer");
        buttonsLayout = WidgetUtils.createVBoxLayout(buttonsContainer);
        layout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignVCenter));
        layout.addWidget(textEdit);
        layout.addWidget(buttonsContainer);
        layout.setSpacing(4);
        setLayout(layout);

        buttonsLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
        buttonsLayout.setSpacing(2);

        setFocusPolicy(Qt.FocusPolicy.StrongFocus);//Необходимо для смены фокуса по клавише Tab
        setFocusProxy(textEdit);//Необходимо для программной установки фокуса
        refreshTextOptions();
        addStandardButtons();
    }
    
    private void onFocusIn(){
        try {
            updateValueMarkers(false);
            if (value == null && !isReadonly()) {
                setOnlyText("");
            }
        } finally {
            repaint();
        }        
    }
    
    private void onFocusOut(){
        if (inModificationState()){
            setModificationState(false);
        }
        updateValueMarkers(true);
        repaint();
    }
    
    @SuppressWarnings("unused")
    private void onTextChanged(){        
        final String newText = textEdit.toPlainText();        
        if (getEditMask().validate(getEnvironment(), newText)== ValidationResult.ACCEPTABLE){//NOPMD                
            setOnlyValue(newText);
        }else{
            setOnlyText(value);
        }
    }
    
    private void setModificationState(final boolean inModification){
        isInModificationState = inModification;        
    }
    
    private boolean inModificationState(){
        return isInModificationState;
    }
    
    private boolean setOnlyValue(final String newValue){
        if (Objects.equals(value, newValue)) {
            return false;
        }
        if (!inModificationState() && textEdit.hasFocus()) {
            setModificationState(true);
        }

        this.value = newValue;
        controller.onValueEdit(newValue);
        return true;
    }

    protected ExplorerTextOptions calculateTextOptions(final EnumSet<ETextOptionsMarker> markers) {        
        final ExplorerTextOptions options = 
                (ExplorerTextOptions) getProperty().getValueTextOptions().getOptions(markers);
        if (options == null) {
            final StringBuilder traceMessage = new StringBuilder();
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
            if (scheduledApplyOptionsEventId!=0){
                Application.removeScheduledEvent(scheduledApplyOptionsEventId);
            }
            QApplication.removePostedEvents(this, QEvent.Type.User.value());
            scheduledToApplyOptions = newOptions;
            scheduledApplyOptionsEventId = 
                Application.processEventWhenEasSessionReady(this, new ApplyTextOptionsEvent(newOptions));
        }
    }

    private void applyTextOptionsInternal(final ExplorerTextOptions options) {
        if (options != null) {
            applyTextOptions(options);
            textOptions = options;
        }
    }

    private void applyTextOptions(final ExplorerTextOptions options) {
        final QTextEdit edit = getTextEditor();
        if (edit != null) {
            if (options.getFont() != null && !options.getFont().equals(textOptions.getFont())) {
                if (textEditStyleSheet != null && !textEditStyleSheet.isEmpty()) {
                    edit.setStyleSheet(null);
                    textEditStyleSheet = "";
                }
                edit.setFont(options.getQFont());
            }
            final String newStyleSheet;
            if (isEnabled()) {
                newStyleSheet = options.getStyleSheet();
            } else {
                newStyleSheet = "";
            }
            changeTextEditStyleSheet(newStyleSheet);
        }
    }

    protected final boolean changeTextEditStyleSheet(final String newStyleSheet) {
        if (!Objects.equals(newStyleSheet, textEditStyleSheet)) {
            textEditStyleSheet = newStyleSheet;
            final QFont expectedFont = textEdit.font();
            textEdit.setStyleSheet(textEditStyleSheet);
            if (!expectedFont.toString().equals(getTextEditor().font().toString())) {
                getTextEditor().setStyleSheet(null);
                getTextEditor().setFont(expectedFont);
                getTextEditor().setStyleSheet(textEditStyleSheet);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addButton(final IButton btn) {
        if (btn instanceof QToolButton){
            final QToolButton button = (QToolButton) btn;
            button.setFixedWidth(20);
            button.setSizePolicy(new QSizePolicy(Policy.Fixed, Policy.Fixed));
            button.setAttribute(WidgetAttribute.WA_DeleteOnClose);
            button.setFocusPolicy(FocusPolicy.NoFocus);
            buttonsLayout.addWidget(button);
            button.pressed.connect(textEdit, "setFocus()");
        }
    }

    public QTextEdit getTextEditor() {
        return textEdit;
    }

    private EditMaskStr getEditMask() {
        return (EditMaskStr) getPropertyEditMask();
    }

    private boolean setValue(final String newValue) {
        if (setOnlyValue(newValue)){
            updateTextEdit();
            textEdit.clearFocus();
            updateValueMarkers(true);
            return true;            
        }
        updateValueMarkers(true);
        return false;
    }
    
    private void updateTextEdit(){
        final String text = getEditMask().toStr(getProperty().getEnvironment(), value);
        if (isReadonly()) {
            final String displayText = getProperty().getOwner().getDisplayString(
                    getProperty().getId(), getProperty().getValueObject(), text, !getProperty().hasOwnValue());
            setOnlyText(displayText);
        } else {
            setOnlyText(text);
        }        
    }
    
    private void setOnlyText(final String text){        
        textEdit.blockSignals(true);
        try{
            textEdit.setText(text);
        }finally{
            textEdit.blockSignals(false);
        }                
    }

    @Override
    public boolean event(final QEvent event) {
        if (event.type() == QEvent.Type.EnabledChange) {
            try{
                if (!wasClosed) {
                    applyTextOptionsInternal(calculateTextOptions(textOptionsMarkers));
                }
            }catch(RuntimeException exception){
                getEnvironment().getTracer().error(exception);
            }
        }
        return super.event(event);
    }        

    @Override
    public boolean eventFilter(final QObject obj, final QEvent event) {
        if (event.type() == QEvent.Type.KeyPress
                && (event instanceof QKeyEvent)
                && !isReadonly()
                && !getProperty().isMandatory()
                && !wasClosed) {
            final QKeyEvent keyEvent = (QKeyEvent) event;
            final boolean isControl = keyEvent.modifiers().value() == KeyboardModifier.ControlModifier.value()
                    || (keyEvent.modifiers().value() == KeyboardModifier.MetaModifier.value() && SystemTools.isOSX);
            final boolean isSpace = keyEvent.key() == Qt.Key.Key_Space.value();
            if (isControl && isSpace) {
                try{
                    if (getProperty().getValueObject()!=null){
                        getProperty().setValueObject(null);
                    }else{
                        setValue(null);
                    }
                }catch(RuntimeException exception){
                    getEnvironment().getTracer().error(exception);
                }                    
                return true;
            }
        }
        return super.eventFilter(obj, event);
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof ApplyTextOptionsEvent) {
            event.accept();
            final ExplorerTextOptions newOptions = ((ApplyTextOptionsEvent) event).getOptions();            
            if (nativeId() != 0 && getTextEditor().nativeId() != 0 && scheduledToApplyOptions!=null && !wasClosed) {
                try {
                    applyTextOptionsInternal(newOptions);
                } catch (RuntimeException exception) {
                    getEnvironment().getTracer().error(exception);
                }
            }
        } else {
            super.customEvent(event);
        }
    }

    @Override
    public boolean setFocus(final Property property) {
        if (property != null && property.getId().equals(getProperty().getId())) {
            textEdit.setFocus();
            return true;
        }
        return false;
    }

    @Override
    protected void closeEditor() {
        textEdit.close();
        wasClosed = true;
        scheduledToApplyOptions = null;
        if (scheduledApplyOptionsEventId!=0){
            Application.removeScheduledEvent(scheduledApplyOptionsEventId);
            scheduledApplyOptionsEventId = 0;
        }        
    }

    private boolean applyTextOptionsMarkers(final EnumSet<ETextOptionsMarker> newMarkers, final boolean force) {
        if (newMarkers != null && !textOptionsMarkers.equals(newMarkers)) {
            textOptionsMarkers = newMarkers.clone();
            if (force) {
                if (scheduledApplyOptionsEventId!=0){
                    Application.removeScheduledEvent(scheduledApplyOptionsEventId);
                    scheduledApplyOptionsEventId = 0;
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

    protected final void updateValueMarkers(final boolean force) {
        final EnumSet<ETextOptionsMarker> markers = getProperty().getTextOptionsMarkers();
        if (value == null && (textEdit == null || !textEdit.hasFocus() || textEdit.isReadOnly())) {
            markers.add(ETextOptionsMarker.UNDEFINED_VALUE);
        } else {
            markers.remove(ETextOptionsMarker.UNDEFINED_VALUE);
        }
        if (value == null && getProperty().isMandatory()) {
            markers.add(ETextOptionsMarker.MANDATORY_VALUE);
        } else {
            markers.remove(ETextOptionsMarker.MANDATORY_VALUE);
        }
        if (!textOptionsMarkers.equals(markers)) {
            applyTextOptionsMarkers(markers, force);
        }
    }

    @Override
    protected final void updateSettings() {
        updateValueMarkers(true);
    }

    @Override
    protected void updateEditor(final Object currentValue, final PropEditorOptions options) {
        if (!setValue((String) currentValue)){            
            updateTextEdit();
        }
        textEdit.setToolTip(options.getTooltip());
        textEdit.setReadOnly(options.isReadOnly());
        buttonsContainer.setVisible(options.isEnabled());
        if (options.getEditMask() instanceof EditMaskStr){
            final EditMaskStr mask = (EditMaskStr)options.getEditMask();
            textEdit.setMaxLength(mask.isMaxLengthDefined() ? mask.getMaxLength() : -1);
        }else{
            textEdit.setMaxLength(-1);
        }
        textEdit.clearFocus();
    }
       
    @Override
    protected final Object getCurrentValueInEditor() {
        return value;
    }

    @Override
    public AlignmentFlag getTitleAlignment() {
        return Qt.AlignmentFlag.AlignTop;
    }

    @Override
    public void setHighlightedFrame(final boolean isHighlighted) {
        if (isHighlighted) {
            final int px = QApplication.style().pixelMetric(QStyle.PixelMetric.PM_DefaultFrameWidth);
            final String color = QApplication.palette().color(QPalette.ColorRole.Highlight).name();
            setStyleSheet("QTextEdit {border: " + px + "px solid " + color + ";}");
        } else {
            setStyleSheet(null);
        }
    }

    @Override
    public boolean isHighlightedFrame() {
        return styleSheet() != null && !styleSheet().isEmpty();
    }

    @Override
    protected void contextMenuEvent(final QContextMenuEvent event) {
        if (textEdit.isReadOnly()) {
            final QMenu menu = textEdit.createStandardContextMenu();
            //removing Undo, Redo actions and separator
            for (int i = 1; i <= 3; i++) {
                menu.removeAction(menu.actions().get(0));
            }
            menu.exec(event.globalPos());
            menu.disposeLater();
        } else {
            super.contextMenuEvent(event);
        }
    }
}
