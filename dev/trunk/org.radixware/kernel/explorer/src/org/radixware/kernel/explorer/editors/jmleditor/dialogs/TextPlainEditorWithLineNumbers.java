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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPlainTextEdit;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QTextBlock;
import com.trolltech.qt.gui.QTextEdit_ExtraSelection;
import com.trolltech.qt.gui.QTextFormat;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author npopov
 */
public class TextPlainEditorWithLineNumbers extends QPlainTextEdit {

    private final LineNumberArea lineNumberArea;

    public TextPlainEditorWithLineNumbers(QWidget parent) {
        super(parent);
        lineNumberArea = new LineNumberArea(this);

        blockCountChanged.connect(this, "updateLineNumberAreaWidth(int)");
        updateRequest.connect(this, "updateLineNumberArea(QRect,int)");
        cursorPositionChanged.connect(this, "highlightCurrentLine()");
        
        updateLineNumberAreaWidth(0);
        highlightCurrentLine();
    }

    private int getLineNumberAreaWidth() {
        int digits = 1;
        int max = Math.max(1, blockCount());
        while (max >= 10) {
            max /= 10;
            ++digits;
        }
        return 5 + fontMetrics().width('9') * digits;
    }

    private void updateLineNumberAreaWidth(int newBlockCount) {
        setViewportMargins(getLineNumberAreaWidth(), 0, 0, 0);
    }

    private void updateLineNumberArea(QRect rect, int dy) {
        if (dy != 0) {
            lineNumberArea.scroll(0, dy);
        } else {
            lineNumberArea.update(0, rect.y(), lineNumberArea.width(), rect.height());
        }

        if (rect.contains(viewport().rect())) {
            updateLineNumberAreaWidth(0);
        }
    }

    @Override
    protected void resizeEvent(QResizeEvent e) {
        super.resizeEvent(e);

        QRect cr = contentsRect();
        lineNumberArea.setGeometry(new QRect(cr.left(), cr.top(), getLineNumberAreaWidth(), cr.height()));
    }

    private void highlightCurrentLine() {
        List<QTextEdit_ExtraSelection> extraSelections = new ArrayList<>();

        if (!isReadOnly()) {
            QTextEdit_ExtraSelection selection = new QTextEdit_ExtraSelection();

            QColor lineColor = new QColor(com.trolltech.qt.core.Qt.GlobalColor.yellow).lighter(160);

            selection.format().setBackground(new QBrush(lineColor));
            selection.format().setProperty(QTextFormat.Property.FullWidthSelection.value(), Boolean.TRUE);
            selection.setCursor(textCursor());
            selection.cursor().clearSelection();
            extraSelections.add(selection);
        }

        setExtraSelections(extraSelections);
    }

    void lineNumberAreaPaintEvent(QPaintEvent event) {
        QPainter painter = new QPainter(lineNumberArea);
        painter.fillRect(event.rect(), com.trolltech.qt.core.Qt.GlobalColor.lightGray);

        QTextBlock block = firstVisibleBlock();
        int blockNumber = block.blockNumber();
        int top = (int) blockBoundingGeometry(block).translated(contentOffset()).top();
        int bottom = top + (int) blockBoundingRect(block).height();

        while (block.isValid() && top <= event.rect().bottom()) {
            if (block.isVisible() && bottom >= event.rect().top()) {
                String number = Integer.toString(blockNumber + 1);
                painter.setPen(new QColor(com.trolltech.qt.core.Qt.GlobalColor.black));
                painter.drawText(0, top, lineNumberArea.width(), fontMetrics().height(),
                        Qt.AlignmentFlag.AlignRight.value(), number);
            }

            block = block.next();
            top = bottom;
            bottom = top + (int) blockBoundingRect(block).height();
            ++blockNumber;
        }
    }

    private static class LineNumberArea extends QWidget {

        private final TextPlainEditorWithLineNumbers codeEditor;

        public LineNumberArea(TextPlainEditorWithLineNumbers editor) {
            super(editor);
            codeEditor = editor;
        }

        @Override
        public QSize sizeHint() {
            return new QSize(codeEditor.getLineNumberAreaWidth(), 0);
        }

        @Override
        protected void paintEvent(QPaintEvent event) {
            codeEditor.lineNumberAreaPaintEvent(event);
        }
    };
}
