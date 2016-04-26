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

package org.radixware.kernel.explorer.editors.xscmleditor;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QMimeData;
import com.trolltech.qt.core.Qt.DropAction;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.FocusReason;
import com.trolltech.qt.core.Qt.Key;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.gui.QTextCursor.MoveMode;
import com.trolltech.qt.gui.QTextCursor.MoveOperation;
import com.trolltech.qt.gui.QTextOption.WrapMode;
import com.trolltech.qt.gui.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.check.RadixProblem.ESeverity;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.editors.jmleditor.ErrorHightlighter;
import org.radixware.kernel.explorer.editors.jmleditor.IndentArrangement;
import org.radixware.kernel.explorer.editors.jmleditor.JmlProcessor;
import org.radixware.kernel.explorer.editors.jmleditor.completer.CompleterProcessor;
import org.radixware.kernel.explorer.editors.jmleditor.jmltags.JmlTag_DbEntity;
import org.radixware.kernel.explorer.editors.jmleditor.jmltags.JmlTag_LocalizedString;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlProcessor;
import org.radixware.kernel.explorer.widgets.ExplorerMenu;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument.AdsUserFuncDefinition;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.xscml.JmlType;


public class XscmlEditor extends AbstractXscmlEditor {
    
    private static final class UpdateTagNamesEvent extends QEvent{
        
        public UpdateTagNamesEvent(){
            super(QEvent.Type.User);
        }                
    }

    private TagProcessor tagConverter;
    public final static String STR_TAB = "    ";
    public final Signal1<JmlType> checkPastOnMlStrings = new Signal1<>();
    public Signal0 pastEnabled = new Signal0();
    public Signal0 updateUndoRedoBtns = new Signal0();
    public Signal0 checkBracket = new Signal0();
    public Signal0 clearHighlightBracket = new Signal0();
    private QTextCharFormat defaultCharFormat;
    private List<TagInfo> copyTags;
    private boolean isUndo = false;
    private AdsUserFuncDef userFunc;
    private CompleterProcessor completeProcessor;
    private ErrorHightlighter errorhighlighter;
    private final IndentArrangement indentArrangement;
    private final IClientEnvironment environment;
    //private LineNumberArea lineNumberArea;
    //private final QTextDocument document;

    @SuppressWarnings("LeakingThisInConstructor")
    public XscmlEditor(final IClientEnvironment environment, final TagProcessor converter, final QWidget parent) {
        super(parent);
        this.environment = environment;
        indentArrangement = new IndentArrangement();
        //lineNumberArea = new LineNumberArea(this);
        //this.document().blockCountChanged.connect(this,"updateLineNumberAreaWidth(Integer)");
       // this.document().u.connect(this,highlightCurrentLine()(Integer));
        //connect(this, SIGNAL(blockCountChanged(int)), this, SLOT(updateLineNumberAreaWidth(int)));
      // connect(this, SIGNAL(updateRequest(const QRect &, int)), this, SLOT(updateLineNumberArea(const QRect &, int)));

        
        final QFont font = new QFont();
        font.setFixedPitch(true);
        font.setPointSize(10);
        this.setFont(font);
        this.setTabStopWidth(20);
        //document = document();
        document().setCanMergeUndoCommands(false);
       // this.document().blockCountChanged.connect(this,"updateLineNumberAreaWidth(Integer)");
        //this..connect(this, "textChange()");
        if ((converter != null)) {
            this.tagConverter = converter;
            this.textChanged.connect(this, "textChange()");
            document().undoCommandAdded.connect(this, "undoTextChange()");
            this.setWordWrapMode(WrapMode.NoWrap);
            defaultCharFormat = this.currentCharFormat();
            completeProcessor = new CompleterProcessor(this, tagConverter);
        }
        this.show();
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }

    public void open(final AdsUserFuncDef userFunc) {
        this.userFunc = userFunc;
        this.setFocusPolicy(FocusPolicy.StrongFocus);
        setXcmlEditorText();
        errorhighlighter = new ErrorHightlighter(this);
        errorhighlighter.clearHightlightError();
        //this.document().undoCommandAdded.connect(this, "undoTextChange()");
    }

    public QTextCharFormat getDefaultCharFormat() {
        return defaultCharFormat;
    }

    public boolean isUndoTextEnabled() {
        return tagConverter.hystoryIndex > 0;
    }

    public boolean isRepoTextEnabled() {
        return this.tagConverter.hystoryIndex < tagConverter.tagListHystory.size() - 1;
    }

    public void setXcmlEditorText() {
        final QTextCursor tc = this.textCursor();
        try {
            //this.document().undoCommandAdded.connect(this, "undoTextChange()");
            this.blockSignals(true);
            this.document().blockSignals(true);
            tc.beginEditBlock();
            this.setUpdatesEnabled(false);
            this.clear();
            tagConverter.toHtml(tc, defaultCharFormat);
        } finally {
            this.setUpdatesEnabled(true);
            tc.endEditBlock();
            this.document().blockSignals(false);
            this.blockSignals(false);
            textChanged.emit();
            this.setTextCursor(tc);
            this.activateWindow();
            this.setFocus(FocusReason.MouseFocusReason);
            tc.dispose();
        }
    }

    public void clearHightlightError() {
        final QTextCursor tc = this.textCursor();
        try {
            errorhighlighter.clearHightlightError(tc);
        } finally {
            tc.dispose();
        }
    }

    public void hightlightError(int startErrPos, int endErrPos, final ESeverity severity) {
        errorhighlighter.hightlightError(startErrPos, endErrPos, severity);
    }

    public void updateTagsPos() {
        final QTextCursor tc = this.textCursor();
        try {
            if (!this.signalsBlocked()) {
                try{
                    this.blockSignals(true);
                    tagConverter.updateTagsPos(this.toPlainText(), tc, isUndo);
                }finally{
                    this.blockSignals(false);
                }
            } else {
                tagConverter.updateTagsPos(this.toPlainText(), tc, isUndo);
            }
        } finally {
            tc.dispose();
        }
    }

    protected void textChange() {
        //final QTextCursor tc = textCursor();
        //this.blockSignals(true);      
        //tagConverter.updateTagsPos(this.toPlainText(), tc, isUndo);
        //this.blockSignals(false);
        isUndo = false;
    }

    @SuppressWarnings("unused")
    public void undoTextChange() {
        if (!this.signalsBlocked()) {
            final List<TagInfo> tagList = new ArrayList<>();
            if ((tagConverter.tagListHystory != null) && (tagConverter.tagListHystory.size() > 0)) {
                for (int i = 0; i < tagConverter.getCurrentTagList().size(); i++) {
                    tagList.add(tagConverter.getCurrentTagList().get(i).copy());
                }
                tagConverter.tagListHystory.add(tagConverter.hystoryIndex + 1, tagList);
                tagConverter.hystoryIndex = tagConverter.hystoryIndex + 1;

                if (tagConverter instanceof JmlProcessor) {
                    ((JmlProcessor) tagConverter).setTagInfoMap();
                }
            }
        }
        updateTagsPos();
    }

    private void calcIdentForBrace(final boolean isLeftBrace) {
        final QTextCursor tc = this.textCursor();
        try {
            final int pos = tc.position();
            String parsingStr = this.toPlainText().substring(0, pos);                        

            String brase = isLeftBrace ? "{" : "}";
            if (!parsingStr.isEmpty()) {
                int spaceSize = 0;
                while (parsingStr.endsWith(" ")) {
                    parsingStr = parsingStr.substring(0, parsingStr.length() - 1);
                    spaceSize++;
                }
                if (isEndWith(parsingStr, "\n")) {
                    tc.movePosition(MoveOperation.Left, MoveMode.KeepAnchor, spaceSize);
                    String res = indentArrangement.calcIndent(parsingStr, isLeftBrace, !isLeftBrace, false, null);
                    brase = res + brase;
                }
            }
            if (tc.hasSelection()) {
                deleteSelectedText(tc);
            }
            tc.insertText(brase, defaultCharFormat);
            this.setTextCursor(tc);
        } finally {
            tc.dispose();
        }
    }

    private void calcIdentForCase(final int caseStart) {
        final QTextCursor tc = this.textCursor();
        try {
            int pos = tc.position();
            final String parsingStr = this.toPlainText().substring(0, caseStart);
            final String caseStr;
            String res = "";
            if (!parsingStr.isEmpty()) {
                caseStr = this.toPlainText().substring(caseStart, pos);
                int spaceSize = 0;
                String str = parsingStr;
                while (str.endsWith(" ")) {
                    str = str.substring(0, str.length() - 1);
                    spaceSize++;
                }
                tc.setPosition(caseStart);
                tc.movePosition(MoveOperation.Left, MoveMode.KeepAnchor, spaceSize);

                final int size = tagConverter.getCurrentTagList().size();
                final long[][] tagPos = new long[size][2];
                for (int i = 0; i < tagConverter.getCurrentTagList().size(); i++) {
                    TagInfo tagInfo = tagConverter.getCurrentTagList().get(i);
                    tagPos[i][0] = tagInfo.getStartPos();
                    tagPos[i][1] = tagInfo.getEndPos();
                }

                if (isEndWith(str, "\n")) {
                    res = indentArrangement.calcIndent(parsingStr + caseStr + ":", false, false, true, tagPos);
                }
            }
            try {
                undoTextChange();
                this.blockSignals(true);
                tc.beginEditBlock();
                if (tc.hasSelection()) {
                    pos -= tc.selectedText().length();
                    deleteSelectedText(tc);
                }
                tc.insertText(res, defaultCharFormat);
                tc.setPosition(pos + res.length());
                tc.insertText(":", defaultCharFormat);
            } finally {
                tc.endEditBlock();
                //undoTextChange();
                this.blockSignals(false);
                this.setTextCursor(tc);
            }
        } finally {
            tc.dispose();
        }
    }

    private void calcIdentForEnter() {
        final QTextCursor tc = this.textCursor();
        try {
            final int pos = tc.position();
            final String parsingStr = this.toPlainText().substring(0, pos);
            String res = "";
            if (!parsingStr.isEmpty()) {
                String str = new String(this.toPlainText().substring(pos));
                boolean isOpenBrace = isStartWith(str, "{");
                boolean isCloseBrace = isStartWith(str, "}");
                res = indentArrangement.calcIndent(parsingStr, isOpenBrace, isCloseBrace, false, null);
            }
            if (tc.hasSelection()) {
                deleteSelectedText(tc);
            }
            tc.insertText("\n" + res, this.defaultCharFormat);
            this.setTextCursor(tc);
        } finally {
            tc.dispose();
        }
    }

    private int isCase() {
        final QTextCursor tc = this.textCursor();
        try {
            final int pos = tc.position();
            final String str = this.toPlainText().substring(0, pos);
            int n;
            if ((n = str.lastIndexOf("case")) != -1) {
                final int start = n;
                String s = new String(str.substring(n - 1, n));

                if ((n == 0 || s.equals(" ") || s.equals("\n"))) {
                    s = new String(str.substring(n + 4, n + 5));
                }
                if ((n + 4 < str.length())
                        && ((s.equals(" ")) || s.equals("\n"))) {
                    n = n + 5;
                    s = new String(str.substring(n, str.length()));
                    if (s.indexOf("\n") == -1) {
                        return start;
                    }
                    /*while(n< str.length() && !str.substring(n, n+1).equals(":")&& !str.substring(n, n+1).equals("\n")){
                     str = str.substring(0,str.length()-1);
                     }
                     if(n< str.length() && str.substring(n, n+1).equals(":"))
                     return start;*/
                }
            }
            return -1;
        } finally {
            tc.dispose();
        }
    }

    private boolean isStartWith(String string, final String s) {
        while (string.startsWith(" ") && string.length() > 0) {
            string = string.substring(1);
        }
        return string.startsWith(s);
    }

    private boolean isEndWith(String string, final String s) {
        while (string.endsWith(" ") && string.length() > 0) {
            string = string.substring(0, string.length() - 1);
        }
        return string.endsWith(s);
    }

    @Override
    public boolean event(final QEvent event) {
        if(isReadOnly()) {
            return super.event(event);
        }
        if ((event instanceof QKeyEvent) && (event.type() == QEvent.Type.KeyPress)) {
            final QKeyEvent keyEvent = (QKeyEvent) event;
            if (keyEvent.key() == Key.Key_Backtab.value() || keyEvent.key() == Key.Key_Tab.value()) {
                final QTextCursor tc = textCursor();
                try {
                    processTab(tc, keyEvent.key() == Key.Key_Backtab.value());
                    return true;
                } finally {
                    tc.dispose();
                }
            }
        } else if (SystemTools.isOSX && event instanceof QInputMethodEvent) {
            //Если на OsX печатать в редакторе на русской расскладке клавиатуры то, 
            //вместо QKeyEvent генерируется QInputMethodEvent. В следствии этого
            //обработчик keyPressEvent не срабатывает, что позволяет нарушить правильную работу тегов.
            //Для защиты от этого игнорируем QInputMethodEvent, если курсор стоит на теге.
            final QTextCursor tc = textCursor();
            try {
                if(mousePessed || !canTypeSymbols(tc.position())) {
                    return false;
                } else {
                    if(tc.hasSelection()) {
                        deleteSelectedText(tc);
                    }
                    this.setCurrentCharFormat(defaultCharFormat);
                }
            } finally {
                tc.dispose();
            }
        }
        return super.event(event);
    }

    @Override
    protected void keyPressEvent(final QKeyEvent e) {      
        if (mousePessed) {
            return;
        }
        clearHighlightBracket.emit();
        final boolean isCtrl = e.modifiers().isSet(KeyboardModifier.ControlModifier)
                || (e.modifiers().isSet(KeyboardModifier.MetaModifier) && SystemTools.isOSX);
        final QTextCursor tc = textCursor();
        try {
            final boolean isCopy = e.matches(QKeySequence.StandardKey.Copy);
            final boolean isCut = e.matches(QKeySequence.StandardKey.Cut);
            final boolean isPast = e.matches(QKeySequence.StandardKey.Paste);
            final boolean isCopyCutPast = isCopy || isCut || isPast;
            if (isReadOnly()) {
                if (isCopy) {
                    processCopyPast(isCut, isCopy);
                }
                return;
            }
            
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
                /*if (e.key() == Key.Key_Backtab.value() || e.key() == Key.Key_Tab.value()) {
                 //insertText(str_tab);
                 processTab(tc, e.key() == Key.Key_Backtab.value() );
                 checkBracket.emit();
                 return;
                 } else */ 
                if (isCtrl && (e.key() == Key.Key_Space.value())||
                         ((SystemTools.isOSX || SystemTools.isWindows)  && isCtrl && (e.key() == Key.Key_Backslash.value()))) {
                    completeProcessor.exposeCompleter();
                    //System.out.print("ctrl+space!");
                    return;
                }else if (isCtrl && (e.key() == Key.Key_S.value())){
                    super.keyPressEvent(e);
                    return;
                }
            }

            final boolean isHomeKey = e.key() == Key.Key_Home.value();
            final boolean isEndKey = e.key() == Key.Key_End.value();
            //OsX: Home/End key (sequence: fn+left arrow/right arrow) move cursor to start/end of line.
            //See moveCursor(QKeyEvent e, QTextCursor tc). Default super.keyPressEvent() not invoked in this case.
            final boolean isOsXMoveToHomeOrEndPressed = SystemTools.isOSX && (isHomeKey || isEndKey);
            final boolean isUndoEvent = e.matches(QKeySequence.StandardKey.Undo);
            final boolean isRedoEvent = e.matches(QKeySequence.StandardKey.Redo) || (isCtrl && ((e.key() == Key.Key_Y.value())));
            final boolean isStandardKey = isCopyCutPast || isUndoEvent || isRedoEvent;
            if ((e.key() == Key.Key_Enter.value()) || (e.key() == Key.Key_Return.value())) {
                calcIdentForEnter();
                checkBracket.emit();
                return;
            } else if (e.key() == Key.Key_BraceRight.value() || e.key() == Key.Key_BraceLeft.value()) {
                calcIdentForBrace(e.key() == Key.Key_BraceLeft.value());
                checkBracket.emit();
                return;
            } else if (e.key() == Key.Key_Colon.value()) {
                int caseStart;
                if ((caseStart = isCase()) != -1) {
                    calcIdentForCase(caseStart);
                    checkBracket.emit();
                    return;
                }
            }

            //delete selected text
            if (tc.hasSelection() && ((!e.text().isEmpty() && (e.key() != Key.Key_Escape.value())) || ((e.key() == Key.Key_Delete.value())) || (e.key() == Key.Key_Backspace.value())) && (!isStandardKey)) { // ��� ������� � ������ �������
                deleteSelectedText(e, tc);
                checkBracket.emit();
                return;
            }
            //delete tag
            if (((e.key() == Key.Key_Delete.value()) || (e.key() == Key.Key_Backspace.value())) && (isCursorOnTag(tc.position(), true)) && (!tc.hasSelection())) {//������ ������� Delete ��� Backspase
                if (deleteTag(tc, e)) {
                    checkBracket.emit();
                    return;
                }
            }
            //обработка copy or cut, past срабатывает автоматически
            if (isCopyCutPast) {
                processCopyPast(isCut, isCopy);
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
                    checkBracket.emit();
                    return;
                }
            }
            //�������� �� modifiers
            final boolean isShortcut = isCtrl && (e.key() == Key.Key_E.value()); // CTRL+E

            if ((completeProcessor.getCompleter() == null) || !isShortcut) { // dont process the shortcut when we have a completer
                if (isNotIgnoredKey(e.key())
                        && (!isCopyCutPast) && ((e.modifiers().value() & (/*KeyboardModifier.ControlModifier.value() |*/ KeyboardModifier.AltModifier.value())) == 0)
                        && (!tc.hasSelection())) {
                    this.setCurrentCharFormat(defaultCharFormat);
                }
                if ((!isCopy) && (!isCut) && !isOsXMoveToHomeOrEndPressed) {                     
                    if (e.key() == Key.Key_Backspace.value() /*&& completeProcessor.getCompleter() != null*/) {
                        tc.movePosition(MoveOperation.Left, MoveMode.KeepAnchor, 1);
                        deleteSelectedText(e, tc);
                        //deleteSelectedText();
                        //tc.deletePreviousChar();//because of Ctrl+backSpace
                        //setTextCursor(tc);
                    } else{
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
            checkBracket.emit();
            completeProcessor.showCompleter(e);
        } finally {
            tc.dispose();
        }

    }
    
    private boolean isNotIgnoredKey(final int key) {
        return Key.Key_Left.value() != key
                && Key.Key_Right.value() != key
                && Key.Key_Up.value() != key
                && Key.Key_Down.value() != key
                && Key.Key_Shift.value() != key
                && Key.Key_Delete.value() != key;
    }

    private void processTab(final QTextCursor tc, final boolean isBackTab) {
        if (tc.hasSelection()) {
            String s = this.toPlainText();
            int start = tc.selectionStart();
            String subStr = s.substring(0, start);
            start = subStr.lastIndexOf("\n");
            start = start == -1 ? 0 : start + 1;
            subStr = new String(s.substring(tc.selectionEnd()));
            int end = subStr.indexOf("\n");
            end = end == -1 ? s.length() : end + tc.selectionEnd();
            s = s.substring(start, end);
            if (!isBackTab || isBackTab && s.startsWith(STR_TAB)) {
                undoTextChange();
                try {
                    this.blockSignals(true);
                    tc.beginEditBlock();

                    while (start < end) {
                        tc.setPosition(start);
                        setTabInEditor(tc, isBackTab);
                        s = toPlainText().substring(tc.position());
                        int n = s.indexOf("\n");
                        if (n == -1) {
                            break;
                        }
                        start = tc.position() + n + 1;
                    }
                } finally {
                    tc.endEditBlock();
                    //undoTextChange();
                    this.blockSignals(false);
                    this.textChanged.emit();
                    this.setFocus();
                }
            }
        } else {
            /*String s = this.toPlainText();
             int start = tc.position();
             String subStr= new String(s.substring(0, start));
             start = subStr.lastIndexOf("\n");
             start = start == -1 ?   0 : start + 1;
             //int end = s.substring(tc.position()).indexOf("\n");
             //end = end == -1 ? s.length() : end + tc.position();
             //s=s.substring(start, end);
             tc.setPosition(start);*/
            setTabInEditor(tc, isBackTab);
            //tc.insertText(STR_TAB, defaultCharFormat);
        }
    }

    private boolean setTabInEditor(final QTextCursor tc, boolean isShiftPressed) {
        if (isShiftPressed) {
            tc.movePosition(MoveOperation.Right, MoveMode.KeepAnchor, STR_TAB.length());
            if (tc.selectedText().equals(STR_TAB)) {
                tc.removeSelectedText();
                return true;
            }
        } else {
            tc.insertText(STR_TAB, defaultCharFormat);
            tc.setPosition(tc.position());
            return true;
        }
        return false;
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
            //System.out.print("undoText hystoryIndex: "+tagConverter.hystoryIndex);
            //System.out.println("  undoText hystorySize: "+tagConverter.tagListHystory.size());
            return true;
        }
        return false;
    }

    public void redoText() {
        if (this.tagConverter.hystoryIndex < tagConverter.tagListHystory.size() - 1) {
            isUndo = true;
            this.tagConverter.hystoryIndex = this.tagConverter.hystoryIndex + 1;
            this.redo();
            completeProcessor.setWasOpen(false);
            //System.out.print("redo hystoryIndex: "+tagConverter.hystoryIndex);
            //System.out.println("  redo hystorySize: "+tagConverter.tagListHystory.size());
        }
        updateUndoRedoBtns.emit();
    }

    private boolean moveCursorToTagEnd(final QTextCursor tc, final MoveMode moveMode, int pos) {
        final TagInfo tag = tagConverter.getTagInfoForCursorMove(pos, false);
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
        TagInfo tag = tagConverter.getTagInfoForCursorMove(pos, false);
        if (tag == null) {
            return false;
        }
        final int tagSize = pos - (int) tag.getStartPos();
        tc.movePosition(MoveOperation.Left, moveMode, tagSize);
        return true;
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
    private int clickPos = 0;
    //устанавливаем позицию курсора на теге

    @Override
    protected void mouseReleaseEvent(QMouseEvent e) {
        completeProcessor.setWasOpen(false);
        clickPos = cursorForPosition(e.pos()).position();
        super.mouseReleaseEvent(e);
        moveCursor();
        mousePessed = false;
        if (!isReadOnly() && (tagConverter instanceof JmlProcessor) && isCursorOnTag(clickPos, false)) {
            ((JmlProcessor) tagConverter).toXml(toPlainText(), textCursor());
            TagInfo tag = tagConverter.getTagInfoForCursorMove(clickPos, false);//getTagUnderCursor(clickPos);
            if (tag != null && !(tag instanceof JmlTag_LocalizedString) && !(tag instanceof JmlTag_DbEntity)) {
                completeProcessor.exposeQuickCompleter(tag);
            }
        }
    }
    private boolean mousePessed = false;

    @Override
    protected void mousePressEvent(QMouseEvent e) {
        mousePessed = true;
        super.mousePressEvent(e);
        clearHighlightBracket.emit();
        checkBracket.emit();
    }

    private boolean moveCursor(QKeyEvent e, QTextCursor tc) {
        final boolean isHomeKey = e.key() == Key.Key_Home.value();
        final boolean isEndKey = e.key() == Key.Key_End.value();
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
        
        //move to start/end line on fn+arrow pressed (OsX)
        if(SystemTools.isOSX && (isHomeKey || isEndKey)) {
            final MoveMode moveMode = (e.modifiers().value() & KeyboardModifier.ShiftModifier.value()) != 0 ? MoveMode.KeepAnchor : MoveMode.MoveAnchor;
            if (isHomeKey) {
                tc.movePosition(MoveOperation.StartOfLine, moveMode);
            } else {
                tc.movePosition(MoveOperation.EndOfLine, moveMode);
            }
            this.setTextCursor(tc);
            return true;
        }
        return false;
    }

    private void moveCursor() {
        completeProcessor.setWasOpen(false);//wasOpen=false;
        this.setFocusPolicy(FocusPolicy.ClickFocus);
        final QTextCursor tc = textCursor();
        try {
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
        } finally {
            tc.dispose();
        }
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
        try {
            undoTextChange();
            TagInfo tag = getTagUnderCursor(clickPos);
            if ((tag != null) && (tag.isValid())) {
                selectTag(tag, tc);
                if (tag.showEditDialog(this, tagConverter.showMode)) {
                    //перерисовать тег
                    try {
                        this.blockSignals(true);
                        //this.document().blockSignals(true);
                        tc.beginEditBlock();
                        insertHtmlTag(tag, "", tc);
                    } finally {
                        tc.endEditBlock();
                        undoTextChange();
                        //this.document().blockSignals(false);
                        this.blockSignals(false);
                        this.textChanged.emit();
                        //endEditBlock(tc);
                    }
                    return true;
                }
                if (!tagConverter.getCurrentTagList().contains(tag)) {
                    return true;
                }
            }
            return false;
        } finally {
            tc.dispose();
        }
    }

    public TagInfo getTagUnderCursor(final int pos) {
        TagInfo tag = null;
        if (isCursorOnTag(pos - 1, false) || (isCursorOnTag(pos + 1, false)) || (isCursorOnTag(pos, false))) {
            tag = tagConverter.getTagInfoForCursorMove(pos, false);
            if (tag == null) {
                tag = tagConverter.getTagInfoForCursorMove(pos + 2, false);
            }
        }
        return tag;
    }

    @Override
    protected void contextMenuEvent(QContextMenuEvent event) {
        final QTextCursor tc = cursorForPosition(event.pos());
        TagInfo tag = getTagUnderCursor(tc.position());
        boolean enable = ((tag == null) || (!tag.isValid()) || (textCursor().hasSelection())) ? false : true;
        if (enable) {
            this.setTextCursor(tc);
            enable = (tagConverter instanceof SqmlProcessor) || (tag instanceof JmlTag_DbEntity) || (tag instanceof JmlTag_LocalizedString);
        }

        QMenu contextMenu = this.createStandardContextMenu(event.globalPos());
        boolean canPast = false;
        if ((contextMenu.actions() != null) && (contextMenu.actions().size() > 5) && (!enable)) {
            canPast = contextMenu.actions().get(5).isEnabled();
        }
        boolean canRedo = tagConverter.hystoryIndex < tagConverter.tagListHystory.size() - 1;
        boolean canUndo = false;//(this.tagConverter.hystoryIndex-1)>=0;
        ExplorerMenu newContextMenu = new XcmlContextMenu(this, textCursor().hasSelection(), canUndo, canRedo, canPast, enable);
        newContextMenu.exec(event.globalPos());
        newContextMenu.removeAllActions();
        //if (enable) {
        moveCursor();
        //}
    }

    @Override
    protected void dropEvent(QDropEvent e) {
        final QTextCursor tc = cursorForPosition(e.pos());
        if (isCursorOnTag(tc.position(), false)) {
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
        this.setTextCursor(tc);
        textChanged.emit();
        QMimeData mimeData = new QMimeData();//super.createMimeDataFromSelection();
        mimeData.setHtml(str);
        mimeData.setText(tc.selectedText());
        insertFromMimeData(mimeData);
        dragLeaveEvent(new QDragLeaveEvent());
    }

    public void setCursorInEditor(int pos) {
        //if(pos>=0 && pos<toPlainText().length()){
        final QTextCursor tc = textCursor();
        tc.setPosition(pos);
        this.setTextCursor(tc);
        moveCursor(MoveMode.MoveAnchor, pos);//check position on tag
        //}
    }

    private void moveCursor(final MoveMode moveMode, int pos) {
        pos = pos + 1;
        final QTextCursor tc = textCursor();
        try {
            final TagInfo tag = tagConverter.getTagInfoForCursorMove(pos, false);
            if (tag == null) {
                return;
            }
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
        } finally {
            tc.dispose();
        }
    }

    public TagInfo selectTagUnderCursor(QTextCursor tc) {
        final int pos = tc.position();
        final TagInfo tag = tagConverter.getTagInfoForCursorMove(pos, true);
        selectTag(tag, tc);
        this.setTextCursor(tc);
        return tag;
    }

    private void selectTag(final TagInfo tag, final QTextCursor tc) {
        if (tag == null) {
            return;
        }
        tc.setPosition((int) tag.getStartPos() - 1);
        final int tagLen = (int) (tag.getEndPos() - tag.getStartPos());
        tc.movePosition(MoveOperation.Right, MoveMode.KeepAnchor, tagLen);
    }

    private TagInfo getDeleteTag(final QTextCursor tc, final int key) {
        TagInfo tag = null;
        if (key == Key.Key_Delete.value()) {
            if (!tc.atBlockEnd()) {
                tag = tagConverter.getTagInfoForCursorMove(tc.position() + 1, true, false);//
                if(tag != null) {
                    tc.movePosition(MoveOperation.Right);
                }
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

    private void deleteSelectedText(QKeyEvent e, QTextCursor tc) {
        try {
            undoTextChange();
            this.blockSignals(true);
            //this.document().blockSignals(true);
            tc.beginEditBlock();
            deleteSelectedText(tc);
            if ((e.key() != Key.Key_Delete.value()) && (e.key() != Key.Key_Backspace.value())) {
                this.setCurrentCharFormat(defaultCharFormat);
                super.keyPressEvent(e);
            }
        } finally {
            tc.endEditBlock();
            //undoTextChange();
            //this.document().blockSignals(false);
            this.blockSignals(false);
            this.textChanged.emit();
            //endEditBlock(tc);
        }
    }

    void deleteSelectedText() {
        deleteSelectedText(this.textCursor());
    }

    public void deleteSelectedText(QTextCursor tc) {
        if (!tc.hasSelection()) {
            return;
        }
        undoTextChange();
        final int startSlection = tc.selectionStart();
        final int endSelection = tc.selectionEnd();
        final List<TagInfo> selectedTagList = tagConverter.getTagListInSelection(startSlection + 1, endSelection + 1, true);

        tagConverter.deleteTagList(selectedTagList);
        deleteSelectedText(tc, selectedTagList);
    }

    private void deleteSelectedText(final QTextCursor tc, final List<TagInfo> tags) {
        if ((tags != null) && (tags.size() > 0)) {
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
                //this.document().blockSignals(true);
                tc.beginEditBlock();
                tc.removeSelectedText();
            } finally {
                tc.endEditBlock();
                //undoTextChange();
                //this.document().blockSignals(false);
                this.blockSignals(false);
                textChanged.emit();
                //endEditBlock(tc);
            }
        } else {
            tc.removeSelectedText();
        }
    }

    private boolean deleteTag(final QTextCursor tc, final QKeyEvent e) {
        int pos = tc.position();
        TagInfo deletingTag = getDeleteTag(tc, e.key());
        if ((deletingTag == null) /*&& (!completeProcessor.getWasOpen())*/) {
            if (!completeProcessor.getWasOpen()) {
                super.keyPressEvent(e);
                return true;
            }
        } else {
            undoTextChange();
            tc.setPosition(pos);
            deletingTag = getDeleteTag(tc, e.key());

            if (deletingTag != null) {
                try {
                    tagConverter.deleteTag(deletingTag);
                    this.blockSignals(true);
                    tc.setPosition((int) deletingTag.getStartPos() - 1);
                    tc.movePosition(MoveOperation.Right, MoveMode.KeepAnchor, (int) (deletingTag.getEndPos() - deletingTag.getStartPos()));
                    tc.removeSelectedText();
                    //if(completeProcessor.getWasOpen()){
                    //   completeProcessor.getCompleter().popup().hide();
                    //    completeProcessor.setWasOpen(false);
                    //}
                    return true;
                } finally {
                    this.blockSignals(false);
                    this.textChanged.emit();
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
    public void deleteTag(final QTextCursor tc, final TagInfo tag) {
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
    @Override
    public void insertText(String s) {
        QTextCursor tc = textCursor();
        try {
            if (tc.hasSelection()) {
                deleteSelectedText(tc);
            }
            undoTextChange();
            try {
                this.blockSignals(true);
                tc.beginEditBlock();
                tc.insertText(s, defaultCharFormat);
            } finally {
                tc.endEditBlock();
                //undoTextChange();
                this.blockSignals(false);
                this.textChanged.emit();
                this.setFocus();
            }
        } finally {
            tc.dispose();
        }
    }

    @Override
    public void insertTag(final TagInfo tag, String suffix) {
        if (tag == null) {
            return;
        }
        setFocusInText();
        this.setFocus();
        final QTextCursor tc = textCursor();
        try {
            if (tagConverter instanceof SqmlProcessor) {
                suffix = suffix + " ";
            }
            try {
                if (tc.hasSelection()) {
                    deleteSelectedText(tc);
                }
                undoTextChange();
                this.blockSignals(true);
                //this.document().blockSignals(true);
                tc.beginEditBlock();
                insertHtmlTag(tag, suffix, tc);
                tagConverter.insertTagToTagList(tag);
                //endUndoCommand(tc);
            } finally {
                tc.endEditBlock();
                //undoTextChange();
                this.blockSignals(false);
                this.textChanged.emit();
                //endEditBlock(tc);
            }
            this.setTextCursor(tc);
            this.setFocus();
        } finally {
            tc.dispose();
        }
    }

    /*//вставить тег из посказки (Для CompleterProcessor)
     public void replaseTag(Scml.Item item,QTextCursor tc, String space){
     final TagInfo newTag= ((JmlProcessor)tagConverter).createCompletionTag(item, textCursor().position()+1);
     if(newTag!=null){
     if(tc.hasSelection()){
     deleteSelectedText(tc);
     }
     insertHtmlTag(newTag,space,tc);
     tagConverter.insertTagToTagList(newTag);
     }
     }*/
    //вставить тег из посказки (Для CompleterProcessor)
    public void insertTagFromCompleter(Scml.Item item, QTextCursor tc, String space, int[] tagPos) {
        final TagInfo newTag = ((JmlProcessor) tagConverter).createCompletionTag(item, textCursor().position() + 1);
        if (newTag != null) {
            if (tc.hasSelection()) {
                deleteSelectedText(tc);
            }
            insertHtmlTag(newTag, space, tc);
            
            if(tagPos[0] < 0) {
               tagPos[0] = tagConverter.insertTagToTagList(newTag);
            } else {
               tagConverter.insertTagToTagList(newTag, tagPos[0]);
            }
            tagPos[0]++;
        }
    }
    
    private void insertHtmlTag(TagInfo newTag, String space, QTextCursor tc) {
        QTextCharFormat c = newTag.getCharFormat();
        tc.insertText(newTag.getDisplayName(), c);
        tc.insertText(space, defaultCharFormat);
    }

    public boolean isCursorOnTag(final int pos, final boolean isDelete) {
        return (tagConverter.getTagInfoForCursorMove(pos + 1, isDelete) != null);
    }
    
    public boolean canTypeSymbols(final int pos) {
        TagInfo tag = tagConverter.getTagInfoForCursorMove(pos + 1, false);
        if(tag == null) {
            return true;
        } else {
            return tag.getStartPos() == pos + 1;
        }
    }

    public void exposeCompleter() {
        this.completeProcessor.exposeCompleter();
    }

    public void updateTagsFormat() {
        tagConverter.clearHystory();
        this.setUndoRedoEnabled(false);
        QTextCursor tc = textCursor();
        try {
            IProgressHandle ph = getEnvironment().getProgressHandleManager().newStandardProgressHandle();
            ph.startProgress(null, false);
            QApplication.processEvents();
            try {
                List<TagInfo> tagList = tagConverter.getCurrentTagList();
                ph.setMaximumValue(tagList.size() - 1);
                for (int j = 0; j < tagList.size(); j++) {
                    TagInfo tag = tagList.get(j);
                    tag.updateCharFormat();
                    tc.setPosition((int) tag.getStartPos() - 1);
                    tc.movePosition(QTextCursor.MoveOperation.Right, QTextCursor.MoveMode.KeepAnchor, (int) (tag.getEndPos() - tag.getStartPos()));
                    insertHtmlTag(tag, "", tc);
                    if (j >= 200 && j % 200 == 0) {
                        QApplication.processEvents();
                    }
                    ph.setValue(j);
                }
                this.setUndoRedoEnabled(true);
            } finally {
                tc.dispose();
                ph.finishProgress();
            }
        } finally {
            tc.dispose();
        }
    }

    private void processCopyPast(boolean isCut, boolean isCopy) {
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
        try {
            if ((!tc.hasSelection() || (tagConverter.hystoryIndex >= tagConverter.tagListHystory.size()))) {
                return null;
            }
            String str = saveSelectionToClipboard(tc, false, false);
            return str;
        } finally {
            tc.dispose();
        }
    }

    public String copyAsXmlText() {
        QTextCursor tc = textCursor();
        try {
            if ((!tc.hasSelection() || (tagConverter.hystoryIndex >= tagConverter.tagListHystory.size()))) {
                return null;
            }
            String str = saveSelectionToClipboard(tc, false, true);
            return str;
        } finally {
            tc.dispose();
        }
    }

    public String cutText() {
        final QTextCursor tc = textCursor();
        try {
            if (!tc.hasSelection()) {
                return null;
            }
            undoTextChange();
            String str = saveSelectionToClipboard(tc, true, false);
            tagConverter.deleteTagList(this.copyTags);
            deleteSelectedText(tc, this.copyTags);
            return str;
        } finally {
            tc.dispose();
        }
    }

    private String saveSelectionToClipboard(QTextCursor tc, boolean isForDelete, boolean copyAsXml) {
        String str = tc.selectedText();
        String html = null;
        //if(copyAsXml){
        int startSelection = tc.selectionStart() + 1, endSelection = tc.selectionEnd() + 1;
        this.copyTags = new ArrayList<>(tagConverter.getTagListInSelection(startSelection, endSelection, isForDelete));
        if (!copyTags.isEmpty()) {
            html = getStrFromXml(tc);
        }
        //}        
        if (html != null) {
            QMimeData mimeData = new QMimeData();//super.createMimeDataFromSelection();
            mimeData.setText(copyAsXml ? html : super.createMimeDataFromSelection().text());
            mimeData.setHtml(html);
            QApplication.clipboard().setMimeData(mimeData);
        } else {
            QMimeData mimeData = super.createMimeDataFromSelection();
            //mimeData.setText(str);
            QApplication.clipboard().setMimeData(mimeData);
        }
        return html == null ? str : html;
    }

    private String getStrFromXml(QTextCursor tc) {
        String res = tagConverter.getStrFromXml(this.toPlainText(), this.copyTags, this.textCursor());
        return res;
    }

    public void pastText() {
        moveCursor();
        this.paste();
    }

    public void updateTagsName() {
        if ((tagConverter.tagListHystory == null) || (tagConverter.tagListHystory.isEmpty())) {
            return;
        }
        tagConverter.clearHystory();
        this.setUndoRedoEnabled(false);
        final QTextCursor tc = textCursor();
        try {
            int size = tagConverter.getCurrentTagList().size();
            for (int i = 0; i < size; i++) {
                final TagInfo tag = tagConverter.getCurrentTagList().get(i);
                tc.setPosition((int) tag.getStartPos() - 1);
                tc.movePosition(MoveOperation.Right, MoveMode.KeepAnchor, tag.getDisplayName().length());
                if (tagConverter.changeTagName(tag)) {
                    insertHtmlTag(tag, "", tc);
                    updateTagsPos();
                }
            }
            this.setUndoRedoEnabled(true);
        } finally {
            tc.dispose();
        }
    }

    /* @Override
     protected QMimeData createMimeDataFromSelection() {
     QMimeData mimeData = super.createMimeDataFromSelection();
     return mimeData;
     }*/
    @Override
    protected void insertFromMimeData(QMimeData source) {
        String s = null;
        if (source.hasHtml()) {
            s = source.html();
        } else if (source.hasText()) {
            s = source.text();
        }
        if (s != null) {
            if (!s.equals("")) {
                s = replaceIllegalCharacters(s);
                final QTextCursor tc = this.textCursor();
                try {
                    strToHtml(tc, s, source.text());
                    int pos = tc.position();
                    try {//раскраска тегов
                        this.blockSignals(true);
                        updateTagsPos();
                        //updateTagsFormat(tc);
                    } finally {
                        tc.endEditBlock();
                        //undoTextChange();
                        this.blockSignals(false);
                    }
                    tc.setPosition(pos);
                    this.setTextCursor(tc);
                    setFocusInText();
                } finally {
                    tc.dispose();
                }
            }
        } else {
            super.insertFromMimeData(source);
        }
    }
    
    public static String replaceIllegalCharacters(String s) {
        return s.replace("\t", STR_TAB);
    }

    public void strToHtml(QTextCursor tc, String str, String text) {
        if (tagConverter instanceof JmlProcessor) {
            JmlType jmlType;
            try {
                AdsUserFuncDefinitionDocument xDoc = AdsUserFuncDefinitionDocument.Factory.parse(str);
                AdsUserFuncDefinition xDef = xDoc.getAdsUserFuncDefinition();
                if (xDef != null) {
                    jmlType = xDef.getSource();
                    if (xDef.getStrings() != null && !jmlType.getItemList().isEmpty()) {
                        AdsLocalizingBundleDef b = userFunc.findLocalizingBundle();
                        for (JmlType.Item item : jmlType.getItemList()) {
                            if (item.isSetLocalizedString()) {
                                for (LocalizedString xStr : xDef.getStrings().getStringList()) {
                                    if (xStr.getId() != null && item.getLocalizedString().getStringId().equals(xStr.getId().toString())) {
                                        AdsMultilingualStringDef s = AdsMultilingualStringDef.Factory.loadFrom(xStr);
                                        AdsMultilingualStringDef copyStr = s.cloneString(b);
                                        item.getLocalizedString().setStringId(copyStr.getId().toString());
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    checkPastOnMlStrings.emit(jmlType);
                } else {
                    jmlType = JmlType.Factory.newInstance();
                    jmlType.addNewItem().setJava(text);
                }
            } catch (XmlException e) {
                try {
                    jmlType = JmlType.Factory.parse(str);
                    checkPastOnMlStrings.emit(jmlType);
                } catch (XmlException ex) {
                    jmlType = JmlType.Factory.newInstance();
                    jmlType.addNewItem().setJava(text);
                }
            }
            if (!text.isEmpty() && (jmlType.getItemList() == null || jmlType.getItemList().isEmpty())) {
                jmlType = JmlType.Factory.newInstance();
                jmlType.addNewItem().setJava(text);
            }
            strToHtml(jmlType, tc);
        } else {
            org.radixware.schemas.xscml.Sqml sqml;
            try {
                sqml = org.radixware.schemas.xscml.Sqml.Factory.parse(str);
            } catch (XmlException e) {
                sqml = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
                sqml.addNewItem().setSql(text);
            }
            if (!text.isEmpty() && (sqml.getItemList() == null || sqml.getItemList().isEmpty())) {
                sqml = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
                sqml.addNewItem().setSql(text);
            }
            if (tc.hasSelection()) {
                deleteSelectedText(tc);
            }
            try {
                this.blockSignals(true);
                tc.beginEditBlock();
                ((SqmlProcessor) tagConverter).toHtml(tc, defaultCharFormat, sqml);
            } finally {
                this.blockSignals(false);
                textChanged.emit();
            }
        }
    }

    private void strToHtml(JmlType jmlType, QTextCursor tc) {
        strToHtml(jmlType, tc, null);
    }

    public void strToHtml(JmlType jmlType, QTextCursor tc, String srcName) {
        Jml jml = Jml.Factory.loadFrom(userFunc, jmlType, srcName);
        if (tc.hasSelection()) {
            deleteSelectedText(tc);
        }
        try {
            this.blockSignals(true);
            tc.beginEditBlock();
            ((JmlProcessor) tagConverter).toHtml(tc, defaultCharFormat, jml);
        } finally {
            this.blockSignals(false);
            textChanged.emit();
        }
        jml.delete();

    }

    public void setFocusInText() {
        activateWindow();
        setFocus(FocusReason.MouseFocusReason);
    }

    @Override
    public TagProcessor getTagConverter() {
        return tagConverter;
    }

    @Override
    public void setTagConverter(TagProcessor tagProcessor) {
        this.tagConverter = tagProcessor;
    }
    
    public void scheduleUpdateTagNames(){
        QApplication.postEvent(this, new UpdateTagNamesEvent());
    }
    
    

    /* private void endUndoCommand(final QTextCursor tc){
     QTextCharFormat c=new QTextCharFormat();
     c.setFontWordSpacing(0);
     c.setFontWeight(0);
     c.setForeground(this.charFormat.background());
     //inserting this character to guarantee end of undo command
     tc.insertText("\u200B",c);//"\u200B"http://www.fileformat.info/info/unicode/char/200b/index.htm
     tc.deletePreviousChar();
     }*/
    /*      @Override
     protected void closeEvent(final QCloseEvent closeEvent) {
     pastEnabled.disconnect(); 
     updateUndoRedoBtns.disconnect(); 
     checkBracket.disconnect(); 
     clearHighlightBracket.disconnect(); 
     if(copyTags!=null){
     copyTags.clear();
     }
     if(tagConverter!=null)
     tagConverter.clearHystory();
     }*/

    @Override
    protected void customEvent(final QEvent qevent) {
        if (qevent instanceof UpdateTagNamesEvent){
            qevent.accept();
            updateTagsName();
        }else{
            super.customEvent(qevent);
        }        
    }
    
   /* int lineNumberAreaWidth(){
        int digits = 1;
        int max = qMax(1, blockCount());
        while (max >= 10) {
            max /= 10;
            ++digits;
        }

        int space = 3 + fontMetrics().width(QLatin1Char('9')) * digits;

        return space;
    }
    
    void updateLineNumberAreaWidth(final int  newBlockCount ){
        setViewportMargins(lineNumberAreaWidth(), 0, 0, 0);
    }
    
    void updateLineNumberArea(final QRect rect,final int dy){
        if (dy>0){
            lineNumberArea.scroll(0, dy);
        }else{
            lineNumberArea.update(0, rect.y(), lineNumberArea.width(), rect.height());
        }
        if (rect.contains(viewport().rect())){
            updateLineNumberAreaWidth(0);
        }
    }
    
    @Override
    protected void resizeEvent(final QResizeEvent e){
        super.resizeEvent(e);
        QRect cr = contentsRect();
        lineNumberArea.setGeometry(new QRect(cr.left(), cr.top(), lineNumberAreaWidth(), cr.height()));
    }

    @Override
    protected void paintEvent(QPaintEvent e) {
        super.paintEvent(e); //To change body of generated methods, choose Tools | Templates.
    }

    
    
     void lineNumberAreaPaintEvent(final QPaintEvent event){
        QPainter painter=new QPainter(lineNumberArea);
        painter.fillRect(event.rect(), QColor.lightGray);

   //lineNumberAreaPaintEvent() вызывается из LineNumberArea всякий раз при получении события рисования. Начинаем рисование фона виджета.

        QTextBlock block = this.document().firstBlock();//firstVisibleBlock();
        int blockNumber = block.blockNumber();
        
        int top = (int) this.document().blockBoundingGeometry(block).translated(contentOffset()).top();
        int bottom = top + (int) blockBoundingRect(block).height();

  // Теперь пройдёмся по всем видимым строкам и отрисуем для каждой строки номер в дополнительной области. Заметьте, что при редактировании обычного текста каждая строка будет состоять из одного QTextBlock; тем не менее, если включен перенос строк, то строка может занимать несколько строк в области просмотра редактора текста.

   //Получим верхнюю и нижнюю y-координаты первого текстового блока, и подгоним эти значения к высоте текущего текстового блока в каждой итерации цикла.

        while (block.isValid() && top <= event.rect().bottom()) {
            if (block.isVisible() && bottom >= event.rect().top()) {
                String number = String.valueOf(blockNumber + 1);
                painter.setPen(QColor.black);
                painter.drawText(0, top, lineNumberArea.width(), fontMetrics().height(),
                                 Qt.AlignmentFlag.AlignRight, number);
            }

            block = block.next();
            top = bottom;
            bottom = top + (int) blockBoundingRect(block).height();
            ++blockNumber;
        }
    }*/
}
