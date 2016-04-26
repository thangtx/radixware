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

package org.radixware.kernel.explorer.editors.scmleditor;

import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QTextCursor.MoveMode;
import com.trolltech.qt.gui.QTextCursor.MoveOperation;
import com.trolltech.qt.gui.QTextDocument.FindFlag;
import com.trolltech.qt.gui.QTextDocument.FindFlags;
import org.radixware.kernel.explorer.dialogs.FindAndReplaceDialog;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerSettings;


 class TextSearcher {
     private FindAndReplaceDialog findDialog;
     private final ScmlTextEditor textEditor;
    // private final ScmlEditor parent;

     TextSearcher(final ScmlTextEditor textEditor){
         //this.parent=parent;
         this.textEditor=textEditor;
     }
     
    void setFindDialog(FindAndReplaceDialog d) {
        findDialog = d;
    }

    void find() {
        final ExplorerSettings settings = (ExplorerSettings)textEditor.getEnvironment().getConfigStore();
        FindAndReplaceDialog dialog = new FindAndReplaceDialog(textEditor, settings, "jmlEditor/findDialog", false);
        dialog.setWindowTitle(Application.translate("JmlEditor", "Find") + "...");
        findDialog = dialog;
        dialog.find.connect(this, "findText()");
        dialog.exec();        
    }

    void findNext() {
        if (findDialog == null) {
            return;
        }
        if (findDialog.getFindWhat() == null) {
            return;
        }
        String findText = findDialog.getFindWhat();
        FindFlags flags = getFindOptions();
        if ((findText != null) && (flags != null) && (!textEditor.find(findText, flags))) {
            exposeMessageInformation(findText);
        } else {
            isFindTextInTag(textEditor.textCursor(), false);
        }
    }

    void replace() {
        final ExplorerSettings settings = (ExplorerSettings) textEditor.getEnvironment().getConfigStore();
        FindAndReplaceDialog dialog = new FindAndReplaceDialog(textEditor, settings, "jmlEditor/findAndReplaseDialog", true);
        dialog.setWindowTitle(Application.translate("JmlEditor", "Replace") + "...");
        findDialog = dialog;
        dialog.find.connect(this, "findText()");
        dialog.replace.connect(this, "replaceText()");
        dialog.replaceAll.connect(this, "replaceTextAll()");
        dialog.exec();
    }

    private void isFindTextInTag(final QTextCursor tc, boolean replace) {
        if (textEditor.isCursorOnTag(tc.position(), true)) {
            TagView tag = textEditor.selectTagUnderCursor(tc);
            if (replace) {
                textEditor.getTagConverter().deleteTag(tag);
                tc.removeSelectedText();
                tc.setCharFormat(textEditor.getDefaultCharFormat());
            }
        }
    }

    @SuppressWarnings("unused")
    private void findText() {
        String findText = findDialog.getFindWhat();
        FindFlags flags = getFindOptions();
        if (!textEditor.find(findText, flags)) {
            exposeMessageInformation(findText);
        } else {
            isFindTextInTag(textEditor.textCursor(), false);
            textEditor.focusWidget();
        }
    }

    @SuppressWarnings("unused")
    private void replaceTextAll() {
        FindFlags flags = getFindOptions();
        flags.clear(FindFlag.FindBackward);
        String findWhat = findDialog.getFindWhat();
        String replaceWith = findDialog.getReplaceWith();

        QTextCursor tc = textEditor.textCursor();
        tc.setPosition(0);
        textEditor.setTextCursor(tc);
        while (textEditor.find(findDialog.getFindWhat(), flags)) {
            replaceText(flags, findWhat, replaceWith);
            textEditor.setTextCursor(textEditor.textCursor());
        }
        findDialog.accept();
        textEditor.setCursorInEditor(textEditor.toPlainText().length());
        textEditor.setFocusInText();
    }

    @SuppressWarnings("unused")
    private void replaceText() {        
        String findWhat = findDialog.getFindWhat();
        String replaceWith = findDialog.getReplaceWith();
        if (findWhat.equals(replaceWith)) {
            return;
        }
        FindFlags flags = getFindOptions();
        replaceText(flags, findWhat, replaceWith);
        if ((flags.value() & FindFlag.FindBackward.value()) > 0) {
            textEditor.textCursor().movePosition(MoveOperation.Left, MoveMode.MoveAnchor, replaceWith.length());
        }
        textEditor.setTextCursor(textEditor.textCursor());
        textEditor.focusWidget();
    }



    private void replaceText(FindFlags flags, String findWhat, String replaceWith) {
        if (textEditor.textCursor().hasSelection()) {
            replaseSelectedText(replaceWith);
        } else {
            if (textEditor.find(findWhat, flags)) {
                replaseSelectedText(replaceWith);
            } else {
                exposeMessageInformation(findWhat);
            }
        }
    }

    private void replaseSelectedText(String replaceWith) {
        QTextCursor tc = textEditor.textCursor();
        try {
            textEditor.undoTextChange();
            textEditor.blockSignals(true);
            textEditor.document().blockSignals(true);
            tc.beginEditBlock();

            isFindTextInTag(tc, true);
            tc.insertText(replaceWith);
        } finally {
            tc.endEditBlock();
            textEditor.document().blockSignals(false);
            textEditor.blockSignals(false);
            textEditor.textChanged.emit();
        }

    }

    private FindFlags getFindOptions() {
        FindFlags flags = new FindFlags();
        if (findDialog.isWholeWordChecked()) {
            flags.set(FindFlag.FindWholeWords);
        }
        if (findDialog.isMatchCaseChecked()) {
            flags.set(FindFlag.FindCaseSensitively);
        }
        if (!findDialog.isForwardChecked()) {
            flags.set(FindFlag.FindBackward);
        }
        return flags;
    }

    private void exposeMessageInformation(String text) {
        String title = Application.translate("ExplorerDialog", "Information");
        String message = Application.translate("ExplorerDialog", "String \'%s\' not found");
        Application.messageInformation(title, String.format(message, text));
    }
}
