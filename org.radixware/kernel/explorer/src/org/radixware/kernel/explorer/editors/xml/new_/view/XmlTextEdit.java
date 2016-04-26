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

package org.radixware.kernel.explorer.editors.xml.new_.view;

import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QSyntaxHighlighter;
import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextDocument;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.explorer.editors.xml.new_.XmlEditor;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;

public class XmlTextEdit extends QTextEdit {

    private final XmlHighlighter  highlighter = new XmlHighlighter(this.document());
    private final XmlEditor editor;

    public XmlTextEdit(final QWidget parent) {
        super(parent);
        editor = (XmlEditor) parent;
    }

    public void setXmlText(final String text) {
        setXmlText(text, editor.isReadOnly());
    }
    
    public void setXmlText(final String text, final boolean isReadOnly) {
        setText(text);
        setBackgroundColor(isReadOnly);
    }
    
    public void setXmlTextReadOnly(final boolean isReadOnly){
        super.setReadOnly(isReadOnly);
        setBackgroundColor(isReadOnly);
    }

    private void setBackgroundColor(final boolean isReadOnly) {
        final ExplorerTextOptions options;
        if (isReadOnly) {
            options = ExplorerTextOptions.Factory.getOptions(ETextOptionsMarker.READONLY_VALUE);
        } else {
            options = ExplorerTextOptions.getDefault();
        }

        if (options.getBackground() != null) {
            QColor bcolor = new QColor(options.getBackground());
            highlighter.setBackgroudColor(bcolor);
            final QPalette palette = new QPalette(viewport().palette());
            palette.setColor(viewport().backgroundRole(), bcolor);
            viewport().setPalette(palette);
        }
    }

    private static class XHRule {

        public final QRegExp pattern;
        public final QTextCharFormat format;

        public XHRule(QRegExp pattern, QTextCharFormat format) {
            this.pattern = pattern;
            this.format = format;
        }
    }

    private static class XmlHighlighter extends QSyntaxHighlighter {

        List<XmlTextEdit.XHRule> rules = new LinkedList<>();
        QRegExp commentStart;
        QRegExp commentEnd;
        QTextCharFormat commentFormat = new QTextCharFormat();
        QTextCharFormat tagFormat = new QTextCharFormat();
        QTextCharFormat atrFormat = new QTextCharFormat();
        QTextCharFormat piFormat = new QTextCharFormat();
        QTextCharFormat errFormat = new QTextCharFormat();

        public XmlHighlighter(final QTextDocument textDocument) {
            super(textDocument);
            QBrush brush;
            QRegExp pattern;
            XHRule rule;

            //tag
            brush = new QBrush(QColor.fromRgb(210, 125, 30), Qt.BrushStyle.SolidPattern);
            pattern = new QRegExp("</*[-:_A-Za-z0-9]+ *");
            tagFormat.setForeground(brush);
            rule = new XmlTextEdit.XHRule(pattern, tagFormat);
            rules.add(rule);

            pattern = new QRegExp("/*>");
            rule = new XmlTextEdit.XHRule(pattern, tagFormat);
            rules.add(rule);

            //attributes
            brush = new QBrush(QColor.fromRgb(60, 130, 245), Qt.BrushStyle.SolidPattern);
            pattern = new QRegExp("[-_:A-Za-z0-9]+=\\x0022.+\\x0022");
            atrFormat.setForeground(brush);
            rule = new XmlTextEdit.XHRule(pattern, atrFormat);
            rules.add(rule);

            //processing instructions
            brush = new QBrush(QColor.fromRgb(70, 170, 95), Qt.BrushStyle.SolidPattern);
            pattern = new QRegExp("<\\?.+\\?>");
            piFormat.setForeground(brush);
            rule = new XmlTextEdit.XHRule(pattern, piFormat);
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
            for (XmlTextEdit.XHRule rule : rules) {
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
