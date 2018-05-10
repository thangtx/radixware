/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QMimeData;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QClipboard;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QMouseEvent;


final class ExplorerTextEditController {
    
    private final static int SYSTEM_KEYS[]
            = {Qt.Key.Key_Back.value(), Qt.Key.Key_Up.value(), Qt.Key.Key_Down.value(),
                Qt.Key.Key_Left.value(), Qt.Key.Key_Right.value(), Qt.Key.Key_Select.value(),
                Qt.Key.Key_No.value(), Qt.Key.Key_Backspace.value(), Qt.Key.Key_Delete.value()};

    private final static QKeySequence.StandardKey[] SYSTEM_KEY_SEQUENCES
            = {QKeySequence.StandardKey.SelectAll, QKeySequence.StandardKey.Copy,
                QKeySequence.StandardKey.SelectPreviousPage, QKeySequence.StandardKey.SelectNextPage,
                QKeySequence.StandardKey.MoveToPreviousPage, QKeySequence.StandardKey.MoveToNextPage,
                QKeySequence.StandardKey.Undo, QKeySequence.StandardKey.Redo,
                QKeySequence.StandardKey.Cut,
                QKeySequence.StandardKey.Delete, QKeySequence.StandardKey.DeleteEndOfLine,
                QKeySequence.StandardKey.DeleteStartOfWord, QKeySequence.StandardKey.DeleteEndOfWord};

    private int maxLength = -1;

    public void setMaxLength(final int length) {
        maxLength = length;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public boolean canInsertFromMimeData(final QMimeData source, final String currentText, final String selectedText) {
        if (source!=null && source.hasText()) {
            final int insertingTextLength = source.text().length();
            final int currentTextLength = currentText==null ? 0 : currentText.length();
            final int selectedTextLength = selectedText==null ? 0 : selectedText.length();
            return insertingTextLength>0
                    && (maxLength < 0 || maxLength >= (currentTextLength + insertingTextLength - selectedTextLength));
        } else {
            return false;
        }
    }        
            
    public boolean filterKeyPressEvent(final QKeyEvent event, final String currentText, final String selectedText) {
        if (isSystemKey(event.key()) || isSystemKeySequence(event)) {
            return false;
        } else if (event.matches(QKeySequence.StandardKey.Paste)){
            final QClipboard.Mode mode = QClipboard.Mode.Clipboard;
            return !canInsertFromMimeData(QApplication.clipboard().mimeData(mode), currentText, selectedText);
        }else if (maxLength<0){
            return false;
        }else{
            final String keyText = event.text();
            final int newLength = currentText.length() + (keyText == null ? 1 : keyText.length());
            return maxLength<newLength;
        }
    }
    
    public boolean filterMouseButtonReleaseEvent(final QMouseEvent event, final String currentText, final String selectedText){
        if (event.button()==Qt.MouseButton.MidButton && QApplication.clipboard().supportsSelection()){
            return !canInsertFromMimeData(QApplication.clipboard().mimeData(QClipboard.Mode.Selection), currentText, selectedText);
        }else{
            return false;
        }
    }

    private static boolean isSystemKey(final int keyCode) {
        for (int systemKeyCode : SYSTEM_KEYS) {
            if (keyCode == systemKeyCode) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSystemKeySequence(final QKeyEvent e) {
        for (QKeySequence.StandardKey keySequence : SYSTEM_KEY_SEQUENCES) {
            if (e.matches(keySequence)) {
                return true;
            }
        }
        return false;
    }
}
