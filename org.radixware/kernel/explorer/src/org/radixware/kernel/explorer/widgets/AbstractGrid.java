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

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Key;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QTableView;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QAbstractItemDelegate.EndEditHint;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionHeader;
import org.radixware.kernel.common.client.errors.SettingPropertyValueError;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditor;
import org.radixware.kernel.explorer.widgets.selector.WrapModelDelegate;

public abstract class AbstractGrid extends QTableView implements IModelWidget {
    
    private static final class EnableUpdateGeometries extends QEvent{            
        public EnableUpdateGeometries(){
            super(QEvent.Type.User);
        }            
    }

    protected static class GridHeader extends QHeaderView {
                
        private final QStyleOptionHeader options = new QStyleOptionHeader();
        private final QSize size = new QSize();
        private final AbstractGrid grid;
        private int sectionsCount = -1;
        private int minSectionHeight;
        private boolean eventScheduled;

        public GridHeader(final AbstractGrid parent) {
            super(Qt.Orientation.Vertical, parent);
            grid = parent;
            options.setOrientation(Qt.Orientation.Vertical);
            minSectionHeight = calcHeaderHeight();
            setClickable(true);            
            setResizeMode(QHeaderView.ResizeMode.Fixed);
        }
        
        

        @Override
        protected void mousePressEvent(final QMouseEvent mouseEvent) {
            grid.mousePressEvent(mouseEvent);
        }

        public void updateSectionsGeometry() {
            minSectionHeight = calcHeaderHeight();
            sectionsCount = -1;
            updateGeometries();//resizeSections();
        }
        
        public void resizeSectionsByContent(){
            updateGeometries();
        }

        @Override
        protected void updateGeometries() {
            try{
                if (sectionsCount!=count()){
                    final int newSectionsCount = count();
                    if (sectionsCount<newSectionsCount){
                        sectionsCount = -1;//wasReread
                    }
                    for (int i=sectionsCount+1; i<newSectionsCount; i++){
                        final int rowSize = grid.sizeHintForRow(i);
                        resizeSection(i, Math.max(rowSize, minSectionHeight));
                    }
                    sectionsCount = newSectionsCount;
                    if (!eventScheduled){
                        eventScheduled = true;
                        QApplication.postEvent(this, new EnableUpdateGeometries());
                    }
                }
            }catch(Throwable ex){
                Application.getInstance().getTracer().error(ex);
                super.updateGeometries();
            }
        }

        @Override
        protected void customEvent(final QEvent event) {
            if (event instanceof EnableUpdateGeometries){
                event.accept();
                eventScheduled = false;
                sectionsCount = -1;
            }
            super.customEvent(event);
        }        
        
        private int calcHeaderHeight(){
            final ExplorerFont boldFont = ExplorerFont.Factory.getFont(font()).getBold();
            options.setFontMetrics(boldFont.getQFontMetrics());            
            final QSize headerSize = 
                grid.style().sizeFromContents(QStyle.ContentsType.CT_HeaderSection, options, size, grid);
            return headerSize.height();
        }        
    }
    
    public static interface IVerticalHeaderFactory{
        QHeaderView createVerticalHeader(AbstractGrid grid);
    }
    
    protected final QHeaderView verticalHeader;

    public AbstractGrid(final QWidget parent) {
        this(parent,null);
    }
    
    protected AbstractGrid(final QWidget parent, final IVerticalHeaderFactory headerFactory) {
        super(parent);
        verticalHeader = headerFactory==null ? new GridHeader(this) : headerFactory.createVerticalHeader(this);
        verticalHeader.blockSignals(true);
        setVerticalHeader(verticalHeader);        
    }

    public int rowCount() {
        return (model() == null ? 0 : model().rowCount());
    }

    public void finishEdit() {
        final PropEditor propEditor = ((WrapModelDelegate) itemDelegate()).getActivePropEditor();
        if (propEditor != null && !propEditor.isDisposed()) {
            propEditor.finishEdit();
            closeEditor(propEditor, EndEditHint.SubmitModelCache);
        }
    }

    @Override
    protected void keyPressEvent(final QKeyEvent keyEvent) {
        //Copy to clipboard real property value DBP-1658
        if (keyEvent.matches(QKeySequence.StandardKey.Copy) && model() != null) {
            final Object obj = model().data(currentIndex(), Qt.ItemDataRole.UserRole);
            if (obj instanceof Property) {
                QApplication.clipboard().setText(((Property) obj).getValueAsString());
            }
            keyEvent.accept();
            return;
        }
        final boolean isNoMod = (keyEvent.modifiers().value() == KeyboardModifier.NoModifier.value() || keyEvent.modifiers().value() == KeyboardModifier.KeypadModifier.value());
        final boolean isEnter = keyEvent.key() == Key.Key_Enter.value() || keyEvent.key() == Key.Key_Return.value();        
        if (isNoMod && isEnter && currentIndex() != null) {
            openEditor(currentIndex());
        }else{
            super.keyPressEvent(keyEvent);
            //Ctrl+Space
            final boolean isControl = keyEvent.modifiers().value() == KeyboardModifier.ControlModifier.value()
                    || (keyEvent.modifiers().value() == KeyboardModifier.MetaModifier.value() && SystemTools.isOSX);
            final boolean isSpace = keyEvent.key() == Key.Key_Space.value();
            if (isControl && isSpace) {
                final QModelIndex index = this.currentIndex();
                if (index == null) {
                    return;
                }
                final Property property = (Property) this.model().data(index, Qt.ItemDataRole.UserRole);
                if (property != null
                        && property.getDefinition().getType() == EValType.BOOL
                        && canEditPropertyValue(property) && !property.isMandatory()) {
                    try {
                        property.setValueObject(null);
                    } catch (Exception ex) {
                        property.getEnvironment().processException(new SettingPropertyValueError(property, ex));
                    }
                }
            }
        }
    }

    private static boolean canEditPropertyValue(final Property property) {
        return !property.isReadonly()
                && (property.hasOwnValue() || !property.isValueDefined())
                && !property.isCustomEditOnly()
                && property.getEditPossibility() != EEditPossibility.PROGRAMMATICALLY;
    }

    protected void openEditor(final QModelIndex index) {
        if (index == null || model() == null || !model().flags(index).isSet(Qt.ItemFlag.ItemIsEditable)) {
            return;
        }
        final Property property = (Property) model().data(index, Qt.ItemDataRole.UserRole);
        if (property != null && property.getDefinition().getType() != EValType.BOOL) {
            edit(index);
        }
    }
    
    

    public void applySettings() {
        ((GridHeader)verticalHeader).updateSectionsGeometry();
    }
    
    public void updateRowsSize(){
        ((GridHeader)verticalHeader).resizeSectionsByContent();
    }

    @Override
    protected void closeEditor(final QWidget editor, final EndEditHint endEditHint) {        
        if (editor!=null && editor.nativeId()>0){
            if (endEditHint == EndEditHint.RevertModelCache && (editor instanceof IWidget)) {
                ((IModelWidget) editor).refresh(null);
            }
            commitData(editor);
        }
        super.closeEditor(editor, endEditHint);
        if (editor!=null && editor.nativeId()>0){
            editor.close();
        }
        setFocus();
    }
}
