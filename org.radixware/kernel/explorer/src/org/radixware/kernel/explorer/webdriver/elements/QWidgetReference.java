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

package org.radixware.kernel.explorer.webdriver.elements;

import org.radixware.kernel.explorer.webdriver.WebDrvKeyEvents;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QPlainTextEdit;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QWidget;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.kernel.explorer.editors.valeditors.AdvancedValBoolEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValBoolEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;
import org.radixware.kernel.explorer.widgets.TabSet;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditor;
import org.radixware.kernel.explorer.widgets.propeditors.PropTextEditor;

final class QWidgetReference extends UIElementReference {        

    private final WeakReference<QWidget> widgetRef;    

    public QWidgetReference(final QWidget widget) {
        super(UIElementId.newInstance());
        widgetRef = new WeakReference<>(widget);
    }
    
    public QWidget getWidget() throws WebDrvException{
        final QWidget result = widgetRef.get();
        if (result==null || result.nativeId()==0){
            throw new WebDrvException(EWebDrvErrorCode.STALE_ELEMENT_REFERENCE);
        }else{
            return result;
        }
    }
    
    private static ValEditor getValEditor(final QWidget widget){
        if (widget instanceof ValEditor){
            return (ValEditor)widget;
        }else if (widget instanceof PropEditor){
            final PropEditor propEditor = (PropEditor)widget;
            return (ValEditor)propEditor.findChild(ValEditor.class);
        }else{
            return null;
        }
    }
    
    private static QLineEdit getLineEdit(final QWidget widget){
        if (widget instanceof QLineEdit){
            return (QLineEdit)widget;
        }else{
            final ValEditor valEditor = getValEditor(widget);
            if (valEditor==null
                || valEditor instanceof ValBoolEditor
                || valEditor instanceof AdvancedValBoolEditor){
                return null;
            }
            return valEditor.getLineEdit();
        }
    }

    @Override
    public boolean isSelected() throws WebDrvException {
        final QWidget widget = getWidget();
        if (widget instanceof QCheckBox){
            return ((QCheckBox)widget).isChecked();
        }else if (widget instanceof QAbstractButton){
            final QAbstractButton button = (QAbstractButton)widget;
            return button.isCheckable() && button.isChecked();
        }
        final ValEditor valEditor = getValEditor(widget);
        if (valEditor instanceof ValBoolEditor){
            return ((ValBoolEditor)valEditor).getValue()==Boolean.TRUE;
        }else if (valEditor instanceof AdvancedValBoolEditor){
            final Object value = valEditor.getValue();
            final EditMaskBool editMask = (EditMaskBool)valEditor.getEditMask();
            return value==editMask.getTrueValue();
        }else{
            return false;
        }        
    }

    @Override
    protected UIElementPropertySet getProperties() throws WebDrvException {
        return new UIElementPropertySet(getWidget());
    }   

    @Override
    public String getDisplayedText() throws WebDrvException {
        final QWidget widget = getWidget();
        if (!widget.isVisible()){
            return "";
        }
        if (widget instanceof QComboBox){
            return ((QComboBox)widget).currentText();
        }
        if (widget instanceof QPlainTextEdit){
            return ((QPlainTextEdit)widget).toPlainText();
        }
        if (widget instanceof QTextEdit){
            return ((QTextEdit)widget).toPlainText();
        }
        if (widget instanceof PropTextEditor){
            final PropTextEditor propEditor = (PropTextEditor)widget;
            final QTextEdit textEdit = (QTextEdit)propEditor.findChild(QTextEdit.class);
            return textEdit==null ? "" : textEdit.toPlainText();
        }
        final QLineEdit lineEdit = getLineEdit(widget);
        if (lineEdit!=null){
            return lineEdit.displayText();
        }
        final UIElementPropertySet properties = new UIElementPropertySet(widget);
        if (properties.containsProperty("text")){
            try{
                return properties.getValue("text");
            }catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException excepion){
                throw new WebDrvException("Failed to get value of \'text\' property", excepion);
            }
        }else{
            return "";
        }        
    }

    @Override
    public String getSimpleClassName() throws WebDrvException {
        for (Class cl=getWidget().getClass(); cl!=null; cl=cl.getSuperclass()){
            if (!cl.isAnonymousClass() && !cl.getSimpleName().isEmpty()){
                return cl.getSimpleName();
            }
        }
        return "QWidget";        
    }

    @Override
    public QRect getElementRect() throws WebDrvException {
        return getWidgetRect(getWidget());
    }

    @Override
    public boolean isEnabled() throws WebDrvException {
        return getWidget().isEnabled();
    }       

    @Override
    public void clickAtCenterPoint(final WebDrvUIElementsManager manager) throws WebDrvException {
        final QWidget widget = getWidget();
        ensureInViewArea(widget);
        clickAtPoint(widget, getClickPoint(widget));
    }

    @Override
    public void clearText() throws WebDrvException {
        final QWidget widget = getWidget();
        ensureInteractable(widget);
        if (widget instanceof QComboBox){
            final QComboBox comboBox = (QComboBox)widget;
            if (comboBox.isEditable()){
                comboBox.setFocus(Qt.FocusReason.ActiveWindowFocusReason);
                comboBox.clearEditText();
                return;
            }else{
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ELEMENT_STATE,"Element is not editable");
            }
        }else if (widget instanceof QPlainTextEdit){
            final QPlainTextEdit textEdit = (QPlainTextEdit)widget;
            if (textEdit.isReadOnly()){
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ELEMENT_STATE,"Element is not editable");
            }else{
                textEdit.setFocus(Qt.FocusReason.ActiveWindowFocusReason);
                textEdit.clear();
                return;
            }
        }
        if (widget instanceof QTextEdit){
            final QTextEdit textEdit = (QTextEdit)widget;
            if (textEdit.isReadOnly()){
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ELEMENT_STATE,"Element is not editable");
            }else{
                textEdit.setFocus(Qt.FocusReason.ActiveWindowFocusReason);
                textEdit.clear();
                return;
            }
        }
        if (widget instanceof PropTextEditor){
            final PropTextEditor propEditor = (PropTextEditor)widget;
            final QTextEdit textEdit = (QTextEdit)propEditor.findChild(QTextEdit.class);
            if (textEdit.isReadOnly()){
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ELEMENT_STATE,"Element is not editable");
            }else{
                textEdit.setFocus(Qt.FocusReason.ActiveWindowFocusReason);
                textEdit.clear();
                return;
            }
        }
        final QLineEdit lineEdit = getLineEdit(widget);
        if (lineEdit!=null){
            if (lineEdit.isReadOnly()){
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ELEMENT_STATE,"Element is not editable");
            }else{
                lineEdit.setFocus(Qt.FocusReason.ActiveWindowFocusReason);
                lineEdit.clear();
                return;
            }
        }
        throw new WebDrvException(EWebDrvErrorCode.INVALID_ELEMENT_STATE,"Element is not editable");
    }

    @Override
    public void sendKeys(final String keyEventsAsStr) throws WebDrvException {
        final QWidget widget = getWidget();
        ensureInteractable(widget);
        final WebDrvKeyEvents events = WebDrvKeyEvents.parse(keyEventsAsStr);
        if (QApplication.focusWidget()!=widget){
            widget.setFocus(Qt.FocusReason.ActiveWindowFocusReason);
        }
        if (widget instanceof QComboBox){
            final QComboBox comboBox = (QComboBox)widget;
            if (comboBox.isEditable()){
                comboBox.lineEdit().end(false);
            }
            events.post(widget);
            return;
        }        
        if (widget instanceof QPlainTextEdit){
            final QPlainTextEdit textEdit = (QPlainTextEdit)widget;
            if (!textEdit.isReadOnly()){
                textEdit.moveCursor(QTextCursor.MoveOperation.End, QTextCursor.MoveMode.MoveAnchor);
            }
            events.post(widget);
            return;            
        }
        if (widget instanceof QTextEdit){
            final QTextEdit textEdit = (QTextEdit)widget;
            if (!textEdit.isReadOnly()){
                textEdit.moveCursor(QTextCursor.MoveOperation.End, QTextCursor.MoveMode.MoveAnchor);
            }
            events.post(widget);
            return;
        }
        if (widget instanceof PropTextEditor){
            final PropTextEditor propEditor = (PropTextEditor)widget;
            final QTextEdit textEdit = (QTextEdit)propEditor.findChild(QTextEdit.class);
            if (!textEdit.isReadOnly()){
                textEdit.moveCursor(QTextCursor.MoveOperation.End, QTextCursor.MoveMode.MoveAnchor);
            }
            events.post(widget);
            return;
        }
        final QLineEdit lineEdit = getLineEdit(widget);
        if (lineEdit!=null){
            if (!lineEdit.isReadOnly()){
                lineEdit.end(false);
            }
            events.post(lineEdit);
            return;
        }
        events.post(widget);
    }        

	@Override
	public void scrollToView() throws WebDrvException {
		ensureInViewArea(this.getWidget());
	}

	@Override
	public boolean isDisplayed() throws WebDrvException {
		return this.getWidget().isVisible();
	}

    @Override
    public QPixmap getPixmap() throws WebDrvException {
        return QPixmap.grabWidget(this.getWidget());
    }

    @Override
    public boolean isInteractiveNow() throws WebDrvException {
        QWidget w = getWidget();
        return w.window() == findActiveWindow() && w.isEnabled() && w.isVisibleTo(w.window());
    }
}
