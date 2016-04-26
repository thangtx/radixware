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

package org.radixware.kernel.explorer.editors.sqmleditor;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QWidget;


public class ParameterListWidget  extends QListWidget{

    private ParametersToolBar paramToolBar;

    ParameterListWidget(final QWidget parent){
        super(parent);
        setItemDelegate(new QItemDelegate(this));
    }

    void setParamToolBar(final ParametersToolBar paramToolBar){
         this.paramToolBar=paramToolBar;
    }

        /*@Override
        protected void currentChanged(QModelIndex current, QModelIndex previous) {
            super.currentChanged(current, previous);
            onParameterItemClick(current);
        }*/

        @Override
        protected void keyPressEvent(final QKeyEvent keyEvent) {
            final int key = keyEvent.key();
            final int modifier  = keyEvent.modifiers().value();
            if (isEditKey(key, modifier)){
                paramToolBar.editParameter();
            }
            else if (isCreateKey(key, modifier) || keyEvent.matches(QKeySequence.StandardKey.New)){
                paramToolBar.createParameter();
            }
            else if (isRemoveKey(key, modifier) || keyEvent.matches(QKeySequence.StandardKey.Delete)){
                paramToolBar.removeParameter();
            }
            else if (key==Qt.Key.Key_Up.value() && modifier==KeyboardModifier.ControlModifier.value()){
                paramToolBar.moveUpParameter();
            }
            else if (key==Qt.Key.Key_Down.value() && modifier==KeyboardModifier.ControlModifier.value()){
                paramToolBar.moveDownParameter();
            }            
            else{
                super.keyPressEvent(keyEvent);
            }
        }
        private boolean isEditKey(final int key, final int modifier){
            final int ctrl_modifier = KeyboardModifier.ControlModifier.value();
            return key==Qt.Key.Key_F2.value() ||
                      ( (key==Qt.Key.Key_Enter.value() || key==Qt.Key.Key_Return.value()) &&
                        modifier==ctrl_modifier
                       );
        }

        private boolean isCreateKey(final int key, final int modifier){
            final int no_modifiers = KeyboardModifier.NoModifier.value(),
                        pad_modifier = KeyboardModifier.KeypadModifier.value();

            return (key==Qt.Key.Key_Insert.value() && modifier==no_modifiers) ||
                      (key==Qt.Key.Key_Plus.value() && modifier==pad_modifier);
        }

        private boolean isRemoveKey(final int key, final int modifier){
            final int no_modifiers = KeyboardModifier.NoModifier.value(),
                        pad_modifier = KeyboardModifier.KeypadModifier.value();

            return (key==Qt.Key.Key_Delete.value() && modifier==no_modifiers) ||
                      (key==Qt.Key.Key_Minus.value() && modifier==pad_modifier);
        }

        @Override
        public QSize sizeHint(){
            final QSize size = super.sizeHint();
            //if (model()!=null && model().columnCount()>0){
                int width = sizeHintForColumn(0) + frameWidth()*2;
                if (verticalScrollBar().isVisible()){
                    width+=verticalScrollBar().width();
                }
                size.setWidth(width);
            //}
            return size;
        }   
}
