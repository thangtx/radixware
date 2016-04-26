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

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.valeditors.TristateCheckBoxStyle;
import org.radixware.kernel.explorer.env.Application;

import org.radixware.kernel.explorer.types.QtUserData;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditor;

/**
 * Делегат для модели-обертки.
 * Делегат не использует методы setModelData и setEditorData для передачи
 * значения из редактора в свойство и обратно. Эта связь реализована на
 * уровне PropEditor при обработке событий focusIn/focusOut.
 * В некоторых случаях, когда редактор удаляется, не происходит событие focusOut.
 * В гриде передача значения из редактора в свойство происходит в методе
 * setModelData.
 *
 */
public class WrapModelDelegate extends ItemDelegateWithFocusFrame {
    
    private static class RefreshActivePropEditorEvent extends QEvent{        
        public RefreshActivePropEditorEvent(){
            super(QEvent.Type.User);
        }
    }

    private PropEditor activePropEditor;
    private final int propertyRole;
    private boolean editorWithFrame;

    public WrapModelDelegate(final QObject parent) {
        super(parent);
        propertyRole = ItemDataRole.UserRole;
    }

    public WrapModelDelegate(final int propertyDataRole, final QObject parent) {
        super(parent);
        propertyRole = propertyDataRole;
    }
    
    public void setEditorFrameVisible(final boolean isVisible){
        editorWithFrame = isVisible;
    }
    
    public boolean isEditorFrameVisible(){
        return editorWithFrame;
    }

    @Override
    public QWidget createEditor(final QWidget parent, final QStyleOptionViewItem option, final QModelIndex index) {
        final Object indexData = index.model().data(index, propertyRole);
        Property property = null;
        if (indexData instanceof Property) {
            property = (Property) indexData;
        } else if (indexData instanceof QtUserData) {
            final Object userData = ((QtUserData) indexData).get();
            if (userData instanceof Property) {
                property = (Property) userData;
            }
        }
        if (property != null) {
            try{
                final IPropEditor editor = property.createPropertyEditor();
                if (editor instanceof PropEditor){
                    final PropEditor propEditor = (PropEditor) editor;
                    propEditor.setParent(parent);
                    propEditor.setGeometry(option.rect());
                    propEditor.refresh(property);
                    propEditor.changeStateForGrid();
                    if (editorWithFrame){
                        propEditor.setHighlightedFrame(true);
                    }
                    propEditor.show();
                    if (property.getDefinition().getType().isArrayType()
                            || property.getDefinition().getType() == EValType.PARENT_REF
                            || property.getDefinition().getType() == EValType.OBJECT) {//перевести курсор в начало строки редактирования
                        QApplication.postEvent(this, new RefreshActivePropEditorEvent());
                    } else {
                        propEditor.setFocus();
                        propEditor.selectAll();
                    }
                    //propEditor.edited.connect(propEditor,"onValueEdit()");
                    setActivePropEditor(propEditor);
                    return propEditor;
                }else if (editor!=null){
                    final String message = 
                        property.getEnvironment().getMessageProvider().translate("TraceMessage", "The instance of PropEditor class required to edit cell");
                    property.getEnvironment().getTracer().debug(message);
                    editor.close();
                    if (editor instanceof QObject){
                        ((QObject)editor).disposeLater();
                    }
                }
            }catch(RuntimeException ex){
                final String messageTemplate = 
                    property.getEnvironment().getMessageProvider().translate("TraceMessage", "Failed to create editor of '%1$s' property");
                final String message = String.format(messageTemplate, property.getTitle());
                property.getEnvironment().getTracer().error(message,ex);
            }
        }        
        setActivePropEditor(null);
        return null;
    }

    
    @Override
    protected void drawCheck(final QPainter painter, final QStyleOptionViewItem option, final QRect rect, final CheckState state) {
        option.setRect(rect);
        TristateCheckBoxStyle.drawCheckBox(painter, option, false, state);
    }

    @Override
    public void setModelData(final QWidget editor, final QAbstractItemModel arg1, final QModelIndex arg2) {
        if (activePropEditor != null) {//TWRBS-1620
            if (activePropEditor.nativeId()==0){
                setActivePropEditor(null);
                return;
            }
            activePropEditor.edited.disconnect();
            setActivePropEditor(null);
            if (editor instanceof PropEditor) {
                try{
                    ((PropEditor) editor).finishEdit();
                }catch(RuntimeException exception){
                    if (((PropEditor) editor).getEnvironment()==null){
                        Application.getInstance().getTracer().error(exception);
                    }else{
                        ((PropEditor) editor).getEnvironment().getTracer().error(exception);
                    }
                }
            }
            super.setModelData(editor, arg1, arg2);
        }
    }

    final public PropEditor getActivePropEditor() {
        return activePropEditor==null || activePropEditor.nativeId()==0 ? null : activePropEditor;
    }

    @Override
    public void updateEditorGeometry(final QWidget editor, final QStyleOptionViewItem option, final QModelIndex index) {
        super.updateEditorGeometry(editor, option, index);
        editor.setGeometry(option.rect());
    }

    @Override
    protected void customEvent(final QEvent qevent) {
        if (qevent instanceof RefreshActivePropEditorEvent){
            qevent.accept();
            if (activePropEditor!=null && activePropEditor.nativeId()>0){
                activePropEditor.refresh(null);
            }            
            return;
        }
        super.customEvent(qevent);
    }
    
    private void setActivePropEditor(final PropEditor propEditor){
        activePropEditor = propEditor;
    }    
}
