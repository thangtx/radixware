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

import com.trolltech.qt.core.QByteArray;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.explorer.editors.scmleditor.completer.CompleterProcessor;
import com.trolltech.qt.core.QMimeData;
import com.trolltech.qt.core.Qt.DropAction;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.FocusReason;
import com.trolltech.qt.core.Qt.Key;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QContextMenuEvent;
import com.trolltech.qt.gui.QDragLeaveEvent;
import com.trolltech.qt.gui.QDropEvent;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QTextCursor.MoveMode;
import com.trolltech.qt.gui.QTextCursor.MoveOperation;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QTextOption.WrapMode;
import com.trolltech.qt.gui.QWidget;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.editors.scml.AbstractScml;
import org.radixware.kernel.explorer.editors.scml.IScml;
import org.radixware.kernel.explorer.editors.scml.IScmlCompletionProvider;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scml.ScmlTag;


public class ScmlTextEditor extends QTextEdit {

    public static final String STR_TAB = "    ";
    public Signal0 pastEnabled = new Signal0();
    public Signal0 updateUndoRedoBtns = new Signal0();
    private TagProcessor tagConverter;
    private CompleterProcessor completeProcessor;
    private final IClientEnvironment environment;
    private final QTextCharFormat defaultCharFormat;
    private List<TagView> copyTags;
    private boolean isUndo = false;
    private int clickPos = 0;
    private boolean mousePessed = false;

    public ScmlTextEditor(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        this.environment = environment;
        defaultCharFormat = this.currentCharFormat();

        this.setFont(getDefaultFont());
        this.setTabStopWidth(20);
        this.document().setCanMergeUndoCommands(false);
        this.setWordWrapMode(WrapMode.NoWrap);
        this.setFocusPolicy(FocusPolicy.StrongFocus);
        //errorhighlighter = new ErrorHightlighter(this);
        //if ((tagProcessor != null)) {
        this.tagConverter = new TagProcessor();
        this.textChanged.connect(this, "textChange()");
        this.document().undoCommandAdded.connect(this, "undoTextChange()");
        //}
        this.show();
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }

    public void open(AbstractScml scml, IScmlCompletionProvider[] completionProvider) {
        completeProcessor = new CompleterProcessor(this, tagConverter, completionProvider);
        tagConverter.open(scml);
        final QTextCursor tc = this.textCursor();
        try {
            this.blockSignals(true);
            this.document().blockSignals(true);
            textCursor().beginEditBlock();
            this.setUpdatesEnabled(false);
            this.clear();
            tagConverter.toHtml(tc, defaultCharFormat, scml.getItems());
        } finally {
            this.setUpdatesEnabled(true);
            textCursor().endEditBlock();
            this.document().blockSignals(false);
            this.blockSignals(false);
            textChanged.emit();
            this.setTextCursor(tc);
            this.activateWindow();
            this.setFocus(FocusReason.MouseFocusReason);
        }
    }

    public QTextCharFormat getDefaultCharFormat() {
        return defaultCharFormat;
    }

    @SuppressWarnings("unused")
    public void undoTextChange() {
        List<TagView> tagList = new ArrayList<>();
        if ((tagConverter.tagListHystory != null) && (tagConverter.tagListHystory.size() > 0)) {
            for (int i = 0; i < tagConverter.getCurrentTagList().size(); i++) {
                tagList.add(tagConverter.getCurrentTagList().get(i).copy());
            }
            tagConverter.tagListHystory.add(tagConverter.hystoryIndex + 1, tagList);
            tagConverter.hystoryIndex = tagConverter.hystoryIndex + 1;

            //if (tagConverter instanceof JmlProcessor) {
            //    ((JmlProcessor) tagConverter).setTagInfoMap();
            //}
        }
    }

    public void setCursorInEditor(int pos) {
        //if(pos>=0 && pos<toPlainText().length()){
        final QTextCursor tc = textCursor();
        tc.setPosition(pos);
        this.setTextCursor(tc);
        moveCursor(MoveMode.MoveAnchor, pos);//check position on tag
        //}
    }

    public TagView getTagUnderCursor(final int pos) {
        TagView tag = null;
        if (isCursorOnTag(pos - 1, false) || (isCursorOnTag(pos + 1, false))) {
            tag = tagConverter.getTagInfoForCursorMove(pos, false);
            if (tag == null) {
                tag = tagConverter.getTagInfoForCursorMove(pos + 2, false);
            }
        }
        return tag;
    }

    private QFont getDefaultFont() {
        QFont font = new QFont();
        font.setFixedPitch(true);
        font.setPointSize(10);
        return font;
    }

    protected void textChange() {
        this.blockSignals(true);
        this.document().blockSignals(true);
        final QTextCursor tc = textCursor();
        tagConverter.updateTagsPos(this.toPlainText(), tc, isUndo);
        this.document().blockSignals(false);
        this.blockSignals(false);
        isUndo = false;
    }

    @Override
    protected void keyPressEvent(QKeyEvent e) {
        if (mousePessed) {
            return;
        }
        final boolean isCtrl = e.modifiers().value() == KeyboardModifier.ControlModifier.value()
                || (e.modifiers().value() == KeyboardModifier.MetaModifier.value() && SystemTools.isOSX);
        final QTextCursor tc = textCursor();
        if ((completeProcessor.getCompleter() != null) && completeProcessor.getWasOpen()) {
            if ((e.key() == Key.Key_Return.value()) || (e.key() == Key.Key_Enter.value())
                    || (e.key() == Key.Key_Escape.value()) || (e.key() == Key.Key_Tab.value())
                    || (e.key() == Key.Key_Backtab.value())) {
                e.ignore();
                //wasOpen=false;
                if ((e.key() != Key.Key_Return.value()) && (e.key() != Key.Key_Enter.value())) {
                    completeProcessor.setWasOpen(false);
                }
                return;
            }
        } else {
            if (e.key() == Key.Key_Tab.value()) {
                //insertText(str_tab);
                insertTab(tc);
                return;
            } else if (isCtrl && (e.key() == Key.Key_Space.value())) {
                completeProcessor.exposeCompleter();
                //System.out.print("ctrl+space!");
                return;
            }
        }

        final boolean isCopy = e.matches(QKeySequence.StandardKey.Copy);
        final boolean isCut = e.matches(QKeySequence.StandardKey.Cut);
        final boolean isPast = e.matches(QKeySequence.StandardKey.Paste);
        final boolean isCopyCutPaste = isCopy || isCut || isPast;
        final boolean isUndoEvent = e.matches(QKeySequence.StandardKey.Undo);
        final boolean isRedoEvent = e.matches(QKeySequence.StandardKey.Redo) || (isCtrl && ((e.key() == Key.Key_Y.value())));//ТАК НАДА!!!
        final boolean isStandardKey = isCopyCutPaste || isUndoEvent || isRedoEvent;
        /*if ((e.key() == Key.Key_Enter.value()) || (e.key() == Key.Key_Return.value())) {
         calcIdentForEnter();
         return;
         } else if (e.key() == Key.Key_BraceRight.value() || e.key() == Key.Key_BraceLeft.value()) {
         calcIdentForBrace(e.key() == Key.Key_BraceLeft.value());
         return;
         } else if (e.key() == Key.Key_Colon.value()) {
         int caseStart;
         if ((caseStart = isCase()) != -1) {
         calcIdentForCase(caseStart);
         return;
         }
         }*/

        //delete selected text
        if ((!isReadOnly()) && tc.hasSelection() && ((!e.text().isEmpty() && (e.key() != Key.Key_Escape.value())) || ((e.key() == Key.Key_Delete.value())) || (e.key() == Key.Key_Backspace.value())) && (!isStandardKey)) { // ��� ������� � ������ �������
            deleteSelectedText(e, tc);
            return;
        }
        //delete tag
        if ((!isReadOnly()) && ((e.key() == Key.Key_Delete.value()) || (e.key() == Key.Key_Backspace.value())) && (isCursorOnTag(tc.position(), true)) && (!tc.hasSelection())) {//������ ������� Delete ��� Backspase
            if (deleteTag(tc, e)) {
                return;
            }
        }
        //обработка copy or cut, past срабатывает автоматически
        if (isCopyCutPaste) {
            processCopyPaste(isCut, isCopy);
        }
        ////////////////////////
      /* if(e.matches(QKeySequence.StandardKey.Undo)||e.matches(QKeySequence.StandardKey.Redo)){//((isCtrl!=0)&& ((e.key()==Key.Key_Z.value())||(e.key()==Key.Key_Y.value()))){//right temporarily
         e.ignore();//right temporarily
         return;//right temporarily
         }*/
        ///////////////////////
        //undo
        if (isUndoEvent) {
            if (!undoText()) {
                return;
            }
        }
        //�������� �� modifiers
        final boolean isShortcut = isCtrl && (e.key() == Key.Key_E.value()); // CTRL+E

        if ((completeProcessor.getCompleter() == null) || !isShortcut) { // dont process the shortcut when we have a completer
            if ((e.key() != Key.Key_Left.value()) && (e.key() != Key.Key_Right.value()) && (e.key() != Key.Key_Up.value()) && (e.key() != Key.Key_Down.value())
                    && (!isCopyCutPaste) && ((e.modifiers().value() & (/*KeyboardModifier.ControlModifier.value() |*/KeyboardModifier.AltModifier.value())) == 0)
                    && (!tc.hasSelection())) {
                this.setCurrentCharFormat(defaultCharFormat);
            }
            if ((!isCopy) && (!isCut)) {
                if (e.key() == Key.Key_Backspace.value()) {
                    tc.deletePreviousChar();//because of Ctrl+backSpace
                } else {
                    //System.out.println("super.keyPressEvent(e)");
                    super.keyPressEvent(e);
                }
            }
        }
        //redo
        if (isRedoEvent) {//((isCtrl!=0)&& (e.key()==Key.Key_Y.value())){
            redoText();
        }
        if (isUndoEvent) {
            updateUndoRedoBtns.emit();
        }
        //move to left || right || up || down
        //if(moveCursor(e,tc))
        //    return;

        moveCursor(e, tc);
        completeProcessor.showCompleter(e, tc.position());

    }

    private void insertTab(final QTextCursor tc) {
        if (tc.hasSelection()) {
            String s = this.toPlainText();
            int start = tc.selectionStart();
            start = s.substring(0, start).lastIndexOf("\n");
            start = start == -1 ? 0 : start + 1;

            int end = s.substring(tc.selectionEnd()).indexOf("\n");
            end = end == -1 ? s.length() : end + tc.selectionEnd();

            s = s.substring(start, end);
            /*tc.setPosition(start);
             tc.insertText(str_tab, defaultCharFormat);
             int n;*/

            undoTextChange();
            try {
                this.blockSignals(true);
                this.document().blockSignals(true);
                tc.beginEditBlock();

                tc.setPosition(start);
                tc.insertText(STR_TAB, defaultCharFormat);
                int n = s.indexOf('\n');
                while (n != -1) {
                    n++;
                    start = n + start + STR_TAB.length();
                    tc.setPosition(start);
                    tc.insertText(STR_TAB, defaultCharFormat);
                    s = s.substring(n);
                    n = s.indexOf('\n');
                }

            } finally {
                tc.endEditBlock();
                this.document().blockSignals(false);
                this.blockSignals(false);
                this.textChanged.emit();
                this.setFocus();
            }
        } else {
            tc.insertText(STR_TAB, defaultCharFormat);
        }
    }

    @SuppressWarnings("unused")
    private void undoTextFromContextMenu() {
        undoText();
        this.undo();
        updateUndoRedoBtns.emit();
    }

    public boolean undoText() {
        if ((tagConverter.hystoryIndex - 1) >= 0) {
            if (this.textCursor().hasSelection()) {
                this.textCursor().clearSelection();
            }
            this.tagConverter.hystoryIndex = this.tagConverter.hystoryIndex - 1;
            isUndo = true;
            completeProcessor.setWasOpen(false);
            //updateUndoRedoBtns.emit();
            return true;
        }
        return false;
    }

    public boolean isUndoTextEnabled() {
        return tagConverter.hystoryIndex > 0;
    }

    public boolean isRepoTextEnabled() {
        return this.tagConverter.hystoryIndex < tagConverter.tagListHystory.size() - 1;
    }

    public void redoText() {
        if (this.tagConverter.hystoryIndex < tagConverter.tagListHystory.size() - 1) {
            isUndo = true;
            this.tagConverter.hystoryIndex = this.tagConverter.hystoryIndex + 1;
            this.redo();
            completeProcessor.setWasOpen(false);
            //System.out.println("hystoryIndex: "+tagConverter.hystoryIndex);
            //System.out.println("hystorySize: "+tagConverter.tagListHystory.size());
        }
        updateUndoRedoBtns.emit();
    }

    @Override
    protected void focusInEvent(final QFocusEvent e) {
        if (completeProcessor.getWasOpen()) {
            if (!completeProcessor.getCompleter().popup().isVisible()) {
                completeProcessor.getCompleter().popup().show();
            } else {
                completeProcessor.getCompleter().setWidget(this);
                completeProcessor.cancelWaiter();
            }
        } else {
            this.setFocusPolicy(FocusPolicy.StrongFocus);
        }
        super.focusInEvent(e);
        /*QEvent.Type.fMouseButtonRelease
         if(e.type()==.){
        
         }*/
    }

    @Override
    protected void focusOutEvent(final QFocusEvent e) {
        if (!this.hasFocus()) {
            completeProcessor.setWasOpen(false);
        }
        super.focusOutEvent(e);
    }
    //устанавливаем позицию курсора на теге

    @Override
    protected void mouseReleaseEvent(QMouseEvent e) {
        completeProcessor.setWasOpen(false);
        clickPos = textCursor().position();
        super.mouseReleaseEvent(e);
        moveCursor();
        mousePessed = false;
        /*if (isCursorOnTag(clickPos, false)) {
         TagView tag = tagConverter.getTagInfoForCursorMove(clickPos, false);//getTagUnderCursor(clickPos);
         if (tag != null) {
         completeProcessor.exposeQuickCompleter(tag);
         }
         }*/
    }    

    @Override
    protected void mousePressEvent(QMouseEvent e) {
        mousePessed = true;
        super.mousePressEvent(e);
    }

    private boolean moveCursorToTagEnd(final QTextCursor tc, final MoveMode moveMode, int pos) {
        final TagView tag = tagConverter.getTagInfoForCursorMove(pos, false);
        if (tag == null) {
            return false;
        }
        pos = pos + 1;
        final int tagSize = (int) tag.getEndPos() - pos;
        tc.movePosition(MoveOperation.Right, moveMode, tagSize);
        return true;
    }

    private boolean moveCursorToTagBegin(final QTextCursor tc, final MoveMode moveMode, int pos) {
        pos = pos + 1;
        TagView tag = tagConverter.getTagInfoForCursorMove(pos, false);
        if (tag == null) {
            return false;
        }
        final int tagSize = pos - (int) tag.getStartPos();
        tc.movePosition(MoveOperation.Left, moveMode, tagSize);
        return true;
    }

    private boolean moveCursor(QKeyEvent e, QTextCursor tc) {
        //move to left
        if (e.key() == Key.Key_Left.value()) {
            final MoveMode moveMode = (e.modifiers().value() & KeyboardModifier.ShiftModifier.value()) != 0 ? MoveMode.KeepAnchor : MoveMode.MoveAnchor;
            tc.movePosition(MoveOperation.Left, moveMode);
            if (isCursorOnTag(tc.position(), false)) {
                moveCursorToTagBegin(tc, moveMode, tc.position());
                this.setTextCursor(tc);
                return true;
            }
        }
        //move to right
        if (e.key() == Key.Key_Right.value()) {
            final MoveMode moveMode = (e.modifiers().value() & KeyboardModifier.ShiftModifier.value()) != 0 ? MoveMode.KeepAnchor : MoveMode.MoveAnchor;
            tc.movePosition(MoveOperation.Right, moveMode);
            if (isCursorOnTag(tc.position(), false)) {
                moveCursorToTagEnd(tc, moveMode, tc.position());
                this.setTextCursor(tc);
                return true;
            }
        }
        //move to up or down
        if ((e.key() == Key.Key_Up.value()) || (e.key() == Key.Key_Down.value())) {
            final MoveMode moveMode = tc.hasSelection() ? MoveMode.KeepAnchor : MoveMode.MoveAnchor;
            if (e.key() == Key.Key_Down.value()) {
                tc.movePosition(MoveOperation.Down, moveMode);
            } else {
                tc.movePosition(MoveOperation.Up, moveMode);
            }
            moveCursor(moveMode, tc.position());
            return true;
        }
        return false;
    }

    private void moveCursor() {
        completeProcessor.setWasOpen(false);//wasOpen=false;
        this.setFocusPolicy(FocusPolicy.ClickFocus);
        final QTextCursor tc = textCursor();
        MoveMode moveMode = tc.hasSelection() ? MoveMode.KeepAnchor : MoveMode.MoveAnchor;

        if (tc.hasSelection()) {
            if (isCursorOnTag(tc.selectionStart(), false)) {
                moveCursor(moveMode, tc.selectionStart());
            }
            if (isCursorOnTag(tc.selectionEnd(), false)) {
                moveCursor(moveMode, tc.selectionEnd());
            }
        } else {
            moveCursor(moveMode, tc.position());
        }
    }

    private void moveCursor(final MoveMode moveMode, int pos) {
        pos = pos + 1;        
        final TagView tag = tagConverter.getTagInfoForCursorMove(pos, false);
        if (tag == null) {
            return;
        }
        final QTextCursor tc = textCursor();
        final int end = (int) tag.getEndPos() - pos, start = pos - (int) tag.getStartPos();
        //курсор расположен ближе к концу тега
        if (start >= end) {
            if (pos == tc.position() + 1) {
                final int tagSize = (int) tag.getEndPos() - pos;
                tc.movePosition(MoveOperation.Right, moveMode, tagSize);
            } else {
                final int p = tc.position() + 1;
                tc.setPosition((int) tag.getEndPos() - 1);
                MoveOperation moveOperation;
                if ((tag.getEndPos() - p) < 0) {
                    moveOperation = MoveOperation.Right;
                } else {
                    moveOperation = MoveOperation.Left;
                }
                final long movepos = (tag.getEndPos() - p) > 0 ? tag.getEndPos() - p : (tag.getEndPos() - p) * (-1);
                tc.movePosition(moveOperation, moveMode, (int) movepos);
            }
        } //курсор расположен ближе к началу тега
        else {
            if (pos == tc.position() + 1) {
                final int tagSize = pos - (int) tag.getStartPos();
                tc.movePosition(MoveOperation.Left, moveMode, tagSize);
            } else {
                final long p = tc.position() + 1;
                tc.setPosition((int) tag.getStartPos() - 1);
                MoveOperation moveOperation;
                if ((tag.getStartPos() - p) < 0) {
                    moveOperation = MoveOperation.Right;
                } else {
                    moveOperation = MoveOperation.Left;
                }
                final long movepos = (tag.getStartPos() - p) > 0 ? tag.getStartPos() - p : (tag.getStartPos() - p) * (-1);
                tc.movePosition(moveOperation, moveMode, (int) movepos);
            }
        }
        this.setTextCursor(tc);
    }

    @Override
    protected void mouseDoubleClickEvent(QMouseEvent e) {
        if ((!showTagEditor()) && (!this.isReadOnly())) {
            tagConverter.tagListHystory.remove(tagConverter.tagListHystory.size() - 1);
            tagConverter.hystoryIndex = tagConverter.hystoryIndex - 1;
            if (getTagUnderCursor(textCursor().position()) == null) {
                super.mouseDoubleClickEvent(e);
            } else {
                selectTagUnderCursor(textCursor());
            }
        }
    }

    private boolean showTagEditor() {
        final QTextCursor tc = textCursor();
        undoTextChange();
        TagView tag = getTagUnderCursor(clickPos);
        if ((tag != null)/* && (tag.isValid())*/) {
            selectTag(tag, tc);
            if (((ScmlTag) tag.getScmlItem()).edit(this) /*showEditDialog(this, tagConverter.showMode)*/) {
                //перерисовать тег
                try {
                    this.document().blockSignals(true);
                    tc.beginEditBlock();
                    insertHtmlTag(tag, "", tc);
                } finally {
                    tc.endEditBlock();
                    this.document().blockSignals(false);
                    this.textChanged.emit();
                }
                return true;
            }
            if (!tagConverter.getCurrentTagList().contains(tag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void contextMenuEvent(QContextMenuEvent event) {
        final QTextCursor tc = cursorForPosition(event.pos());
        TagView tag = getTagUnderCursor(tc.position());
        boolean enable = ((tag == null) || /*(!tag.isValid()) ||*/ (textCursor().hasSelection())) ? false : true;
        if (enable) {
            this.setTextCursor(tc);
        }

        QMenu contextMenu = this.createStandardContextMenu(event.globalPos());
        boolean canPast = false;
        if ((contextMenu.actions() != null) && (contextMenu.actions().size() > 5) && (!enable)) {
            canPast = contextMenu.actions().get(5).isEnabled();
        }
        boolean canRedo = tagConverter.hystoryIndex < tagConverter.tagListHystory.size() - 1;
        boolean canUndo = false;//(this.tagConverter.hystoryIndex-1)>=0;
        QMenu newContextMenu = new XcmlContextMenu(this, textCursor().hasSelection(), canUndo, canRedo, canPast, enable);
        newContextMenu.exec(event.globalPos());
        if (enable) {
            moveCursor();
        }
    }

    @Override
    protected void dropEvent(QDropEvent e) {
        final QTextCursor tc1 = cursorForPosition(e.pos());
        if (isCursorOnTag(tc1.position(), false)) {
            dragLeaveEvent(new QDragLeaveEvent());
            return;
        }
        String str = null;
        if (e.dropAction() == DropAction.MoveAction) {
            str = cutText();
        } else if (e.dropAction() == DropAction.CopyAction) {
            str = copyText();
        }
        if (str == null) {
            dragLeaveEvent(new QDragLeaveEvent());
            return;
        }
        this.setTextCursor(tc1);
        textChanged.emit();
        QMimeData mimeData = super.createMimeDataFromSelection();
        mimeData.setText(str);
        insertFromMimeData(mimeData);
        dragLeaveEvent(new QDragLeaveEvent());
    }

    public TagView selectTagUnderCursor(QTextCursor tc) {
        final int pos = tc.position();
        final TagView tag = tagConverter.getTagInfoForCursorMove(pos, true);
        selectTag(tag, tc);
        this.setTextCursor(tc);
        return tag;
    }

    private void selectTag(final TagView tag, final QTextCursor tc) {
        if (tag == null) {
            return;
        }
        tc.setPosition((int) tag.getStartPos() - 1);
        final int tagLen = (int) (tag.getEndPos() - tag.getStartPos());
        tc.movePosition(MoveOperation.Right, MoveMode.KeepAnchor, tagLen);
    }

    private TagView getDeleteTag(final QTextCursor tc, final int key) {
        TagView tag = null;
        if (key == Key.Key_Delete.value()) {
            if (!tc.atBlockEnd()) {
                tc.movePosition(MoveOperation.Right);
                tag = tagConverter.getTagInfoForCursorMove(tc.position() + 1, false, false);//
            }
        } else {
            if (!tc.atBlockStart()) {
                tag = tagConverter.getTagInfoForCursorMove(tc.position() + 1, true, true);//
                if (tag != null) {
                    tc.movePosition(MoveOperation.Left);
                }
            }
        }
        return tag;
    }

    public void clearText() {
        final QTextCursor tc = this.textCursor();
        tc.setPosition(0);
        tc.movePosition(MoveOperation.Right, MoveMode.KeepAnchor, this.toPlainText().length());
        try {
            this.blockSignals(true);
            this.document().blockSignals(true);
            tc.beginEditBlock();
            deleteSelectedText(tc);
        } finally {
            tc.endEditBlock();
            this.document().blockSignals(false);
            this.blockSignals(false);
            this.textChanged.emit();
        }
    }

    private void deleteSelectedText(QKeyEvent e, QTextCursor tc) {
        try {
            this.blockSignals(true);
            this.document().blockSignals(true);
            tc.beginEditBlock();
            deleteSelectedText(tc);
            if ((e.key() != Key.Key_Delete.value()) && (e.key() != Key.Key_Backspace.value())) {
                this.setCurrentCharFormat(defaultCharFormat);
                super.keyPressEvent(e);
            }
        } finally {
            tc.endEditBlock();
            this.document().blockSignals(false);
            this.blockSignals(false);
            this.textChanged.emit();
        }
    }

    //public void deleteSelectedText() {
    //    deleteSelectedText(this.textCursor());
    //}
    public void deleteSelectedText(QTextCursor tc) {
        if (!tc.hasSelection()) {
            return;
        }
        undoTextChange();
        final int startSlection = tc.selectionStart();
        final int endSelection = tc.selectionEnd();
        final List<TagView> selectedTagList = tagConverter.getTagListInSelection(startSlection + 1, endSelection + 1, true);

        tagConverter.deleteTags(selectedTagList);
        deleteSelectedText(tc, selectedTagList);
    }

    private void deleteSelectedText(final QTextCursor tc, final List<TagView> tags) {
        if (!tags.isEmpty()) {
            int start = tc.selectionStart(), end = tc.selectionEnd();
            if (tc.selectionStart() > tc.selectionEnd()) {//если текст выделили справа на лево
                start = tc.selectionEnd();
                end = tc.selectionStart();
            }
            start = Math.min((int) tags.get(0).getStartPos() - 1, start);
            end = Math.max((int) tags.get(tags.size() - 1).getEndPos() - 1, end);
            tc.setPosition(start);
            final int tagLen = end - start;
            tc.movePosition(MoveOperation.Right, MoveMode.KeepAnchor, tagLen);
        }
        if ((!this.signalsBlocked()) && (!this.document().signalsBlocked())) {
            try {
                this.blockSignals(true);
                this.document().blockSignals(true);
                tc.beginEditBlock();
                tc.removeSelectedText();
            } finally {
                tc.endEditBlock();
                this.document().blockSignals(false);
                this.blockSignals(false);
                textChanged.emit();
            }
        } else {
            tc.removeSelectedText();
        }
    }

    private boolean deleteTag(final QTextCursor tc, final QKeyEvent e) {
        int pos = tc.position();
        TagView deletingTag = getDeleteTag(tc, e.key());
        if ((deletingTag == null) /*&& (!completeProcessor.getWasOpen())*/) {
            if (!completeProcessor.getWasOpen()) {
                super.keyPressEvent(e);
                return true;
            }
        } else {
            //System.out.println("delete tag");
            undoTextChange();
            tc.setPosition(pos);
            deletingTag = getDeleteTag(tc, e.key());

            if (deletingTag != null) {
                try {
                    this.document().blockSignals(true);
                    tc.setPosition((int) deletingTag.getStartPos() - 1);
                    tc.movePosition(MoveOperation.Right, MoveMode.KeepAnchor, (int) (deletingTag.getEndPos() - deletingTag.getStartPos()));
                    tc.removeSelectedText();
                    //if(completeProcessor.getWasOpen()){
                    //   completeProcessor.getCompleter().popup().hide();
                    //   completeProcessor.setWasOpen(false);
                    //}
                    return true;
                } finally {
                    this.document().blockSignals(false);
                    tagConverter.deleteTag(deletingTag);
                    textChanged.emit();
                }
            }
        }
        return false;
        //if(completeProcessor.getWasOpen())
        //    completeProcessor.showCompleter(e);
    }

    /* Удалить тег (для Completer Processor)
     * tc -  TextCursor
     * tag - удаляемый тег
     */
    public void deleteTag(final QTextCursor tc, final TagView tag) {
        if (tag == null) {
            return;
        }
        tc.setPosition((int) tag.getEndPos() - 1);
        tc.movePosition(MoveOperation.Left, MoveMode.KeepAnchor, (int) (tag.getEndPos() - tag.getStartPos()));
        tc.removeSelectedText();
        tagConverter.deleteTag(tag);
    }

    /* public void prepareInsertNewTag(){
     undoTextChange();
     }*/
    // вставить в редактор простой текст (для SqmlEditor)
    public void insertText(String s) {
        QTextCursor tc = textCursor();
        if (tc.hasSelection()) {
            deleteSelectedText(tc);
        }
        undoTextChange();
        try {
            //tc.insertText(s,defaultCharFormat);
            this.blockSignals(true);
            this.document().blockSignals(true);
            tc.beginEditBlock();
            tc.insertText(s, defaultCharFormat);
            // endUndoCommand(tc);
        } finally {
            tc.endEditBlock();
            this.document().blockSignals(false);
            this.blockSignals(false);
            this.textChanged.emit();
            this.setFocus();
        }
    }

    public void insertTag(final TagView tag, String suffix) {
        if (tag == null) {
            return;
        }
        final QTextCursor tc = textCursor();
        /*if (tagConverter instanceof SqmlProcessor) {
         suffix = suffix + " ";
         }*/
        try {
            if (tc.hasSelection()) {
                deleteSelectedText(tc);
            }
            undoTextChange();
            this.blockSignals(true);
            this.document().blockSignals(true);
            tc.beginEditBlock();
            insertHtmlTag(tag, suffix, tc);
            tagConverter.insertTag(tag);
            //endUndoCommand(tc);
        } finally {
            tc.endEditBlock();
            this.document().blockSignals(false);
            this.blockSignals(false);
            this.textChanged.emit();
        }
        this.setTextCursor(tc);
        this.setFocus();
    }

    //вставить тег из посказки (Для CompleterProcessor)
    public void insertTag(IScmlItem item, QTextCursor tc, String space) {

        //final TagInfo newTag =  tagConverter.createCompletionTag(item, textCursor().position() + 1);
        final TagView newTag = new TagView((ScmlTag) item, textCursor().position() + 1);//tagConverter.createCompletionTag(item, textCursor().position() + 1);
        //if (newTag != null) {
            if (tc.hasSelection()) {
                deleteSelectedText(tc);
            }
            insertHtmlTag(newTag, space, tc);
            tagConverter.insertTag(newTag);
        //}
    }

    private void insertHtmlTag(TagView newTag, String space, QTextCursor tc) {
        QTextCharFormat c = newTag.getCharFormat();
        tc.insertText(newTag.getDisplayString(), c);
        tc.insertText(space, defaultCharFormat);
    }

    public boolean isCursorOnTag(final int pos, final boolean isDelete) {
        return (tagConverter.getTagInfoForCursorMove(pos + 1, isDelete) != null);
    }

    public void exposeCompleter() {
        this.completeProcessor.exposeCompleter();
    }

    /* public void updateTagsFormat() {
     QTextCursor tc = textCursor();
     List<TagView> tagList = tagConverter.getCurrentTagList();
     for (int j = 0; j < tagList.size(); j++) {
     TagView tag = tagList.get(j);
     tag.updateCharFormat();
     tc.setPosition((int) tag.getStartPos() - 1);
     tc.movePosition(QTextCursor.MoveOperation.Right, QTextCursor.MoveMode.KeepAnchor, (int) (tag.getEndPos() - tag.getStartPos()));
     insertHtmlTag(tag, "", tc);
     }
     }*/
    private void processCopyPaste(boolean isCut, boolean isCopy) {
        if (isCut) {
            cutText();
            pastEnabled.emit();
        } else if (isCopy) {
            copyText();
            pastEnabled.emit();
        }
    }

    public String copyText() {
        QTextCursor tc = textCursor();
        if ((!tc.hasSelection() || (tagConverter.hystoryIndex >= tagConverter.tagListHystory.size()))) {
            return null;
        }
        String str = saveSelectionToClipboard(tc, false);
        return str;
    }

    public String cutText() {
        final QTextCursor tc = textCursor();
        if (!tc.hasSelection()) {
            return null;
        }
        undoTextChange();
        String str = saveSelectionToClipboard(tc, true);
        tagConverter.deleteTags(this.copyTags);
        deleteSelectedText(tc, this.copyTags);
        return str;
    }

    private String saveSelectionToClipboard(QTextCursor tc, boolean isForDelete) {
        int startSelection = tc.selectionStart() + 1, endSelection = tc.selectionEnd() + 1;
        this.copyTags = new ArrayList<TagView>(tagConverter.getTagListInSelection(startSelection, endSelection, isForDelete));
        String html = getStrFromXml(tc);

        QMimeData mimeData = new QMimeData();//super.createMimeDataFromSelection();
        mimeData.setText(super.createMimeDataFromSelection().text());
        try {
            mimeData.setData("text/xscml", new QByteArray(html.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException ex) {
            //ignore
        }
        QApplication.clipboard().setMimeData(mimeData);
        return html;
    }

    private String getStrFromXml(QTextCursor tc) {
        try {
            if (tc.hasSelection()) {
                AbstractScml scml = tagConverter.strToScml(this.toPlainText(), copyTags, this.textCursor(), tc.selectionStart(), tc.selectionEnd());
                XmlObject xml = scml.saveToXml();
                StringWriter w = new StringWriter();
                xml.save(w);
                String res = w.toString();
                //String res = tagConverter.getStrFromXml(this.toPlainText(), this.copyTags, this.textCursor());
                return res;
            }
        } catch (IOException ex) {
            Logger.getLogger(ScmlTextEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public void pastText() {
        this.paste();
    }

    /* public void updateTagsName() {
     if ((tagConverter.tagListHystory == null) || (tagConverter.tagListHystory.isEmpty())) {
     return;
     }
     tagConverter.clearHystory();
     this.setUndoRedoEnabled(false);
     final QTextCursor tc = textCursor();
     int size = tagConverter.getCurrentTagList().size();
     for (int i = 0; i < size; i++) {
     final TagView tag = tagConverter.getCurrentTagList().get(i);
     tc.setPosition((int) tag.getStartPos() - 1);
     tc.movePosition(MoveOperation.Right, MoveMode.KeepAnchor, tag.getDisplayString().length());
     if (tagConverter.changeTagName(tag)) {
     insertHtmlTag(tag, "", tc);
     }
     }
     this.setUndoRedoEnabled(true);
     }*/
    public void updateTagsFormat() {
        QTextCursor tc = textCursor();
        List<TagView> tagList = tagConverter.getCurrentTagList();
        for (int j = 0; j < tagList.size(); j++) {
            TagView tag = tagList.get(j);
            //tag.updateCharFormat(environment);
            tc.setPosition((int) tag.getStartPos() - 1);
            tc.movePosition(QTextCursor.MoveOperation.Right, QTextCursor.MoveMode.KeepAnchor, (int) (tag.getEndPos() - tag.getStartPos()));
            insertHtmlTag(tag, "", tc);
        }
    }

    @Override
    protected QMimeData createMimeDataFromSelection() {
        return super.createMimeDataFromSelection();
    }

    public IScml getScml() {
        QTextCursor tc = textCursor();
        tc.setPosition(0);
        return tagConverter.strToScml(toPlainText(), tc);
    }

    @Override
    protected void insertFromMimeData(QMimeData source) {

        String s = null;
        if (source.hasFormat("text/xscml")) {
            QByteArray data = source.data("text/xscml");
            if (data != null) {
                try {
                    s = new String(data.toByteArray(), "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    //ignore
                }
            }
        }
        if (s == null || s.isEmpty()) {
            s = source.text();
        }

        if ((s != null) && (!s.equals(""))) {
            final QTextCursor tc = this.textCursor();
            strToHtml(tc, s);
        } else {
            super.insertFromMimeData(source);
        }
    }

    public void strToHtml(QTextCursor tc, String str) {
        if (tc.hasSelection()) {
            deleteSelectedText(tc);
        }
        AbstractScml scml = (AbstractScml) tagConverter.getScml().newInstance();
        scml.loadFromStr(str);
        strToHtml(scml, tc);

        int pos = tc.position();
        try {//раскраска тегов
            this.blockSignals(true);
            this.document().blockSignals(true);
            updateTagsFormat();
        } finally {
            tc.endEditBlock();
            this.document().blockSignals(false);
            this.blockSignals(false);
        }
        tc.setPosition(pos);
        this.setTextCursor(tc);

    }

    public void strToHtml(AbstractScml scml, QTextCursor tc) {
        try {
            this.blockSignals(true);
            this.document().blockSignals(true);
            tc.beginEditBlock();
            tagConverter.addHystory(false);
            tagConverter.toHtml(tc, defaultCharFormat, scml.getItems());
        } finally {
            this.document().blockSignals(false);
            this.blockSignals(false);
            textChanged.emit();
            redoAvailable.emit(false);
        }
    }

    public void setFocusInText() {
        activateWindow();
        setFocus(FocusReason.MouseFocusReason);
    }

    public TagProcessor getTagConverter() {
        return tagConverter;
    }
    //public void setTagConverter(TagProcessor1 tagProcessor){
    //    this.tagConverter=tagProcessor;
    //}    
}
