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
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPlainTextEdit;
import com.trolltech.qt.gui.QWidget;

public class ExplorerPlainTextEdit extends QPlainTextEdit {

    private final ExplorerTextEditController controller = new ExplorerTextEditController();

    public ExplorerPlainTextEdit(final QWidget parent) {
        super(parent);
    }

    public void setMaxLength(final int length) {
        controller.setMaxLength(length);
    }

    public int getMaxLength() {
        return controller.getMaxLength();
    }
    
    private String getSelectedText(){
        return textCursor().selectedText();
    }

    @Override
    protected boolean canInsertFromMimeData(final QMimeData source) {
        return !isReadOnly() && controller.canInsertFromMimeData(source, toPlainText(), getSelectedText());
    }
        
    @Override
    protected void keyPressEvent(final QKeyEvent event) {
        if (!isReadOnly() && controller.filterKeyPressEvent(event, toPlainText(), getSelectedText())){
            event.ignore();
        }else{
            super.keyPressEvent(event);
        }
    }

    @Override
    protected void mouseReleaseEvent(final QMouseEvent event) {
        if (!isReadOnly() && controller.filterMouseButtonReleaseEvent(event, toPlainText(), getSelectedText())){
            event.ignore();
        }else{
            super.mousePressEvent(event);
        }
    }        
}
