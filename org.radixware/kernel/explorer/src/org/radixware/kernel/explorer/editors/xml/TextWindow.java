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

package org.radixware.kernel.explorer.editors.xml;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;

import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QSyntaxHighlighter;
import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QTextDocument;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QWidget;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;

public class TextWindow extends QWidget {

    private Ui_TextWindow txt = new Ui_TextWindow();
    private Map<String,String> currentPrefixMap;
    private boolean textModified = false;
    private XmlEditor editor;
    private XmlObject xml = null;
    private XHighlighter highlighter;
    private int errorLine = -1;

    public TextWindow(XmlEditor tree) {
        super((QWidget) null);
        editor = tree;
        editor.undoAction.setDisabled(true);
        editor.redoAction.setDisabled(true);
        editor.copyAction.setDisabled(true);
        editor.cutAction.setDisabled(true);
        txt.setupUi(this);
        txt.gridLayout.setMargin(0);
        txt.textEdit.document().contentsChanged.connect(this, "registerChanges()");
        txt.textEdit.document().undoAvailable.connect(this, "onUndoAvailable(boolean)");
        txt.textEdit.document().redoAvailable.connect(this, "onRedoAvailable(boolean)");
        txt.textEdit.copyAvailable.connect(this, "onCopyAvailable(boolean)");
        xml = tree.getCurrentDocument();
        highlighter = new XHighlighter(txt.textEdit.document());
    }

    @SuppressWarnings("unused")
    private void onCopyAvailable(boolean a) {
        QAction cut = editor.getMenuItem(editor.mEdit, "cutitem");
        cut.setDisabled(!a);
        editor.cutAction.setDisabled(!a);
        QAction copy = editor.getMenuItem(editor.mEdit, "copyitem");
        copy.setDisabled(!a);
        editor.copyAction.setDisabled(!a);
        QAction paste = editor.getMenuItem(editor.mEdit, "pasteitem");
        paste.setDisabled(!(QApplication.clipboard().text().length() > 0));
        editor.pasteAction.setDisabled(!(QApplication.clipboard().text().length() > 0));
    }

    @SuppressWarnings("unused")
    private void onUndoAvailable(boolean a) {
        QAction undo = editor.getMenuItem(editor.mEdit, "undoitem");
        if (a) {
            editor.undoAction.setDisabled(false);
            undo.setDisabled(false);
        } else {
            editor.undoAction.setDisabled(true);
            undo.setDisabled(true);
        }
    }

    @SuppressWarnings("unused")
    private void onRedoAvailable(boolean a) {
        QAction redo = editor.getMenuItem(editor.mEdit, "redoitem");
        if (a) {
            editor.redoAction.setDisabled(false);
            redo.setDisabled(false);
        } else {
            editor.redoAction.setDisabled(true);
            redo.setDisabled(true);
        }
    }

    public void clearText() {
        txt.textEdit.clear();
    }

    public void openText(XmlObject doc, boolean readonly, boolean savePretty, Map<String, String> prefixMap) {
        xml = doc;
        if (xml != null) {
            currentPrefixMap = prefixMap==null ? null : new HashMap<>(prefixMap);
            txt.textEdit.blockSignals(true);
            try {
                txt.textEdit.clear();
                setupBackground(readonly);//added by yremizov
                XmlOptions opt = new XmlOptions();
                if (savePretty) {
                    opt.setSavePrettyPrint();
                    XmlObject docCopy = doc.copy();
                    removeInnerNSDeclarations(docCopy);

                    opt.setSaveAggressiveNamespaces();
                    if (currentPrefixMap != null) {
                        opt.setSaveSuggestedPrefixes(currentPrefixMap);
                    }

                    txt.textEdit.document().setPlainText(docCopy.xmlText(opt));
                } else {
                    txt.textEdit.document().setPlainText(xml.xmlText(opt));
                }
                txt.textEdit.setReadOnly(readonly || savePretty);
            } finally {
                txt.textEdit.blockSignals(false);
                setModified(false);
            }
        }
    }

    private void removeInnerNSDeclarations(XmlObject copy) {
        XmlCursor c = copy.newCursor();
        while (!c.currentTokenType().isEnddoc()) {
            int token = c.currentTokenType().intValue();
            if (token == XmlCursor.TokenType.INT_NAMESPACE) {
                c.removeXml();
            }
            c.toNextToken();
        }
    }

    private void setupBackground(final boolean isReadonly) {//added by yremizov
        final ExplorerTextOptions options;
        if (isReadonly){
            options = ExplorerTextOptions.Factory.getOptions(ETextOptionsMarker.READONLY_VALUE);
        }
        else{
            options = ExplorerTextOptions.getDefault();
        }

        if (options.getBackground()!=null) {
            QColor bcolor = new QColor(options.getBackground());
            highlighter.setBackgroudColor(bcolor);
            final QPalette palette = new QPalette(txt.textEdit.viewport().palette());
            palette.setColor(txt.textEdit.viewport().backgroundRole(), bcolor);
            txt.textEdit.viewport().setPalette(palette);
        }
    }

    public void cutSelection() {
        txt.textEdit.cut();
    }

    public void copySelection() {
        txt.textEdit.copy();
    }

    public void pasteFromClipboard() {
        txt.textEdit.paste();
    }

    public void undoOperation() {
        txt.textEdit.undo();
    }

    public void redoOperation() {
        txt.textEdit.redo();
    }
    
    public void formatTextOperation(){
        final String messageTitle = 
            editor.getEnvironment().getMessageProvider().translate("XmlEditor", "Confirm to Format XML");
        final String messageConfirmation = 
            editor.getEnvironment().getMessageProvider().translate("XmlEditor", "Performing this operation may lead to loss of data.\nDo you want to continue?");
        if (editor.getEnvironment().messageConfirmation(messageTitle, messageConfirmation)){
            final XmlObject xml;
            try{
                xml = XmlObject.Factory.parse(getText());
            }catch(XmlException exception){
                return;
            }
            txt.textEdit.blockSignals(true);
            try {
                txt.textEdit.clear();
                XmlOptions opt = new XmlOptions();
                opt.setSavePrettyPrint();
                opt.setSaveAggressiveNamespaces();
                if (currentPrefixMap != null) {
                    opt.setSaveSuggestedPrefixes(currentPrefixMap);
                }
                removeInnerNSDeclarations(xml);
                txt.textEdit.document().setPlainText(xml.xmlText(opt));
            } finally {
                txt.textEdit.blockSignals(false);
                setModified(false);
            }
        }
    }

    public boolean isModified() {
        return textModified;
    }

    public void setModified(boolean m) {
        textModified = m;
    }

    public String getText() {
        return txt.textEdit.toPlainText();
    }

    public QTextDocument getDocument() {
        return txt.textEdit.document();
    }

    public QTextEdit getTextEditor() {
        return txt.textEdit;
    }

    @SuppressWarnings("unused")
    private void registerChanges() {
        if (xml != null) {
            this.setModified(true);
            if (errorLine != -1) {
                highlightError(QTextCharFormat.UnderlineStyle.NoUnderline, QColor.transparent, errorLine);
                errorLine = -1;
            }
        }
    }

    public void selectErrorLine(XmlError error) {
        if (error != null) {
            highlightError(QTextCharFormat.UnderlineStyle.WaveUnderline, QColor.red, error.getLine());
            errorLine = error.getLine();
        }
    }

    private void highlightError(QTextCharFormat.UnderlineStyle under, QColor color, int line) {
        QTextCharFormat errFormat = new QTextCharFormat();
        errFormat.setUnderlineColor(color);
        errFormat.setUnderlineStyle(under);
        QTextCursor cursor = new QTextCursor(txt.textEdit.textCursor());

        cursor.movePosition(QTextCursor.MoveOperation.Start);
        for (int i = 1; i <= line - 1; i++) {
            cursor.movePosition(QTextCursor.MoveOperation.EndOfLine);
            cursor.movePosition(QTextCursor.MoveOperation.NextCharacter);
        }
        cursor.select(QTextCursor.SelectionType.BlockUnderCursor);
        cursor.setCharFormat(errFormat);
    }

    private static class XHRule {

        public final QRegExp pattern;
        public final QTextCharFormat format;

        public XHRule(QRegExp pattern, QTextCharFormat format) {
            this.pattern = pattern;
            this.format = format;
        }
    }

    private static class XHighlighter extends QSyntaxHighlighter {

        List<XHRule> rules = new LinkedList<>();
        QRegExp commentStart;
        QRegExp commentEnd;
        QTextCharFormat commentFormat = new QTextCharFormat();
        QTextCharFormat tagFormat = new QTextCharFormat();
        QTextCharFormat atrFormat = new QTextCharFormat();
        QTextCharFormat piFormat = new QTextCharFormat();
        QTextCharFormat errFormat = new QTextCharFormat();

        public XHighlighter(QTextDocument p) {
            super(p);
            QBrush brush;
            QRegExp pattern;
            XHRule rule;

            //tag
            brush = new QBrush(QColor.fromRgb(210, 125, 30), Qt.BrushStyle.SolidPattern);
            pattern = new QRegExp("</*[-:_A-Za-z0-9]+ *");
            tagFormat.setForeground(brush);
            rule = new XHRule(pattern, tagFormat);
            rules.add(rule);

            pattern = new QRegExp("/*>");
            rule = new XHRule(pattern, tagFormat);
            rules.add(rule);

            //attributes

            brush = new QBrush(QColor.fromRgb(60, 130, 245), Qt.BrushStyle.SolidPattern);
            pattern = new QRegExp("[-_:A-Za-z0-9]+=\\x0022.+\\x0022");
            atrFormat.setForeground(brush);
            rule = new XHRule(pattern, atrFormat);
            rules.add(rule);

            //processing instructions

            brush = new QBrush(QColor.fromRgb(70, 170, 95), Qt.BrushStyle.SolidPattern);
            pattern = new QRegExp("<\\?.+\\?>");
            piFormat.setForeground(brush);
            rule = new XHRule(pattern, piFormat);
            rules.add(rule);

            //comments
            brush = new QBrush(QColor.darkGray, Qt.BrushStyle.SolidPattern);
            commentFormat.setForeground(brush);
            commentStart = new QRegExp("<!--");
            commentEnd = new QRegExp("-->");

            //err
            brush = new QBrush(QColor.red, Qt.BrushStyle.SolidPattern);
            errFormat.setForeground(brush);
        }

        @Override
        public void highlightBlock(String text) {
            for (XHRule rule : rules) {
                QRegExp expression = rule.pattern;
                int index = expression.indexIn(text);
                while (index >= 0) {
                    int length = expression.matchedLength();
                    setFormat(index, length, rule.format);
                    index = expression.indexIn(text, index + length);
                }
            }

            setCurrentBlockState(0);

            int startIndex = 0;
            if (previousBlockState() != 1) {
                startIndex = commentStart.indexIn(text);
            }
            while (startIndex >= 0) {
                int endIndex = commentEnd.indexIn(text, startIndex);
                int commentLength;
                if (endIndex == -1) {
                    setCurrentBlockState(1);
                    commentLength = text.length() - startIndex;
                } else {
                    commentLength = endIndex - startIndex + commentEnd.matchedLength();
                }
                setFormat(startIndex, commentLength, commentFormat);
                startIndex = commentStart.indexIn(text, startIndex + commentLength);
            }
        }

        public void setBackgroudColor(final QColor color) {//added by yremizov
            for (XHRule rule : rules) {
                rule.format.setBackground(new QBrush(color));
            }
            rehighlight();
        }
    }
}
