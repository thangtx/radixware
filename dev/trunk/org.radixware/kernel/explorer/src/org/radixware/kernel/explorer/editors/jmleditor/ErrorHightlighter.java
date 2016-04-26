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

package org.radixware.kernel.explorer.editors.jmleditor;

import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextCharFormat.UnderlineStyle;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QTextCursor.MoveMode;
import com.trolltech.qt.gui.QTextCursor.MoveOperation;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.check.RadixProblem.ESeverity;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;


public class ErrorHightlighter {

    private final XscmlEditor textEditor;

    public ErrorHightlighter(final XscmlEditor textEditor) {
        this.textEditor = textEditor;
    }

    public void clearHightlightError() {
        final QTextCursor tc = textEditor.textCursor();
        try {
            textEditor.blockSignals(true);
            //textEditor.document().blockSignals(true);
            tc.beginEditBlock();
            final int pos = tc.position();

            clearHightlightError(tc);

            tc.setPosition(pos);
            textEditor.setTextCursor(tc);
        } finally {
            tc.endEditBlock();
            //textEditor.document().blockSignals(false);
            textEditor.blockSignals(false);
        }
    }

    public void clearHightlightError(final QTextCursor tc) {
        tc.setPosition(0);
        tc.movePosition(MoveOperation.NextCharacter, MoveMode.KeepAnchor, textEditor.toPlainText().length());
        final QTextCharFormat f = new QTextCharFormat();
        f.setUnderlineStyle(UnderlineStyle.NoUnderline);
        try {
            tc.mergeCharFormat(f);
        } catch (Throwable ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }

        if (textEditor.getTagConverter().tagListHystory.size() > 0) {
            final List<TagInfo> tagList = textEditor.getTagConverter().getCurrentTagList();
            hightlightErrorTags(tagList, null);
        }
    }

    public void hightlightError(int startErrPos, final int endErrPos, final ESeverity severity) {
        if(startErrPos < 0 || endErrPos > textEditor.toPlainText().length()) {
            return;
        }
        final QTextCursor tc = textEditor.textCursor();
        try {
            QColor color;
            if (severity == ESeverity.ERROR) {
                color = QColor.red;
            } else {
                color = QColor.darkYellow;
            }

            final List<TagInfo> tagList = textEditor.getTagConverter().getTagListInSelection(startErrPos, endErrPos, false);
            int startTag, endTag;
            for (TagInfo tag : tagList) {
                startTag = (int) tag.getStartPos() - 1;
                endTag = (int) tag.getEndPos() - 1;
                if (startTag - startErrPos > 0) {
                    setFormat(tc, startErrPos, startTag - startErrPos, color, UnderlineStyle.WaveUnderline);
                }
                setFormat(tc, startTag, endTag - startTag, color, UnderlineStyle.SpellCheckUnderline);
                startErrPos = endTag;
            }
            if (!tagList.isEmpty()) {
                final TagInfo tag = tagList.get(tagList.size() - 1);
                if (tag.getEndPos() - 1 < endErrPos) {
                    startErrPos = (int) tag.getEndPos() - 1;
                }
            }
            if (endErrPos - startErrPos > 0 && startErrPos >= 0 && endErrPos >= 0 &&
               !( textEditor.isCursorOnTag(endErrPos, false)||textEditor.isCursorOnTag(startErrPos, false) )) {
                setFormat(tc, startErrPos, endErrPos - startErrPos, color, UnderlineStyle.WaveUnderline);
            }
            textEditor.setTextCursor(tc);
        } finally {
            tc.dispose();
        }
    }

    private void setFormat(final QTextCursor tc, final int startPos, final int endPos, final QColor color, final UnderlineStyle underlineStyle) {
        tc.setPosition(startPos);
        tc.movePosition(MoveOperation.Right, MoveMode.KeepAnchor, endPos);
        final QTextCharFormat f = new QTextCharFormat();
        f.setUnderlineColor(color);
        f.setUnderlineStyle(underlineStyle);
        tc.mergeCharFormat(f);
    }

    private void hightlightErrorTags(final List<TagInfo> tagList, final QColor color) {
        if ((tagList != null) && (!tagList.isEmpty())) {
            final int size = tagList.size();
            for (int i = 0; i < size; i++) {
                final QTextCursor tcerr = textEditor.textCursor();
                tcerr.setPosition((int) tagList.get(i).getStartPos() - 1);
                tcerr.movePosition(MoveOperation.Right, MoveMode.KeepAnchor, (int) (tagList.get(i).getEndPos() - tagList.get(i).getStartPos()));
                final QTextCharFormat f = tagList.get(i).getCharFormat();
                if (color != null) {
                    f.setUnderlineColor(color);
                    f.setUnderlineStyle(UnderlineStyle.SpellCheckUnderline);
                }
                tcerr.setCharFormat(f);
                textEditor.setTextCursor(tcerr);
            }
        }
    }
}
