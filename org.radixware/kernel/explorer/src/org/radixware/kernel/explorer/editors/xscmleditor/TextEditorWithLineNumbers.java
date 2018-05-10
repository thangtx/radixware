/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.editors.xscmleditor;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QTextBlock;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QTextCursor.MoveOperation;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QWidget;

public class TextEditorWithLineNumbers extends QTextEdit {

    private final LineNumberArea lineNumberArea;
    private long lastUpdateTime;
    private final int widthOfNumber = fontMetrics().width('9');
    private final static QColor backgroundColor = new QColor(233, 232, 226);
    private int indent = 0;

    public TextEditorWithLineNumbers(QWidget parent) {
        super(parent);
        lineNumberArea = new LineNumberArea(this);

        document().blockCountChanged.connect(this, "updateBlockCount()");
        verticalScrollBar().valueChanged.connect(this, "updateLineNumberArea(int)");
        textChanged.connect(this, "updateLineNumberArea()");

        updateLineNumberAreaWidth();
    }
    
    public void setIndent(int indent) {
        this.indent = indent;
    }

    private void updateBlockCount() {
        ensureCursorVisible();
    }

    private int lineNumberAreaWidth() {
        int digits = 1;
        int max = Math.max(1, this.document().blockCount());
        while (max >= 10) {
            max /= 10;
            ++digits;
        }
        return indent + 5 + widthOfNumber * (digits);
    }

    private void updateLineNumberAreaWidth() {
        setViewportMargins(lineNumberAreaWidth(), 0, 0, 0);
    }

    private void updateLineNumberArea(int slider_pos) {
        lastUpdateTime = 0;
        updateLineNumberArea();
    }

    private void updateLineNumberArea() {
        if (System.currentTimeMillis() - lastUpdateTime > 100) {
            verticalScrollBar().setSliderPosition(verticalScrollBar().sliderPosition());
            QRect rect = contentsRect();
            lineNumberArea.update(0, rect.y(), lineNumberArea.width(), rect.height());

            int dy = verticalScrollBar().sliderPosition();
            if (dy > -1) {
                lineNumberArea.scroll(0, dy);
            }

            int first_block_id = getFirstVisibleBlockId();
            if (first_block_id == 0 || textCursor().block().blockNumber() == first_block_id - 1) {
                verticalScrollBar().setSliderPosition((int) (dy - document().documentMargin()));
            }
            lastUpdateTime = System.currentTimeMillis();
        }
        updateLineNumberAreaWidth();
    }

    @Override
    protected void resizeEvent(QResizeEvent e) {
        super.resizeEvent(e);
        QRect cr = contentsRect();
        lineNumberArea.setGeometry(new QRect(cr.left(), cr.top(), lineNumberAreaWidth(), cr.height()));
    }

    private int getFirstVisibleBlockId() {
        QTextCursor curs = new QTextCursor(document());
        curs.movePosition(MoveOperation.Start);

        for (int i = 0; i < document().blockCount(); i++) {
            QRect r2 = document().documentLayout().blockBoundingRect(curs.block()).translated(
                    viewport().geometry().x(), viewport().geometry().y() - (verticalScrollBar().sliderPosition())).toRect();
            if (viewport().geometry().contains(r2)) {
                return i;
            }
            curs.movePosition(MoveOperation.NextBlock);
        }

        return 0;
    }
  
    private void lineNumberAreaPaintEvent(QPaintEvent event) {
        verticalScrollBar().setSliderPosition(verticalScrollBar().sliderPosition());
                 
        QPainter painter = new QPainter(lineNumberArea);
        painter.fillRect(event.rect(), backgroundColor);
        int blockNumber = getFirstVisibleBlockId();
        
        QTextBlock block = document().findBlockByNumber(blockNumber);
        QTextBlock prev_block = (blockNumber > 0) ? document().findBlockByNumber(blockNumber - 1) : block;
        int translate_y = (blockNumber > 0) ? -verticalScrollBar().sliderPosition() : 0;

        int top = viewport().geometry().top() + (int) ((blockNumber == 0)
                ? document().documentMargin() - 1 - verticalScrollBar().sliderPosition()
                : document().documentLayout().blockBoundingRect(prev_block).translated(0, translate_y).intersected(new QRectF(viewport().geometry())).height());

        int bottom = top + (int) document().documentLayout().blockBoundingRect(block).height();

        while (block.isValid() && top <= event.rect().bottom()) {
            if (block.isVisible() && bottom >= event.rect().top()) {
                painter.setPen(QColor.black);
                painter.drawText(-5, top, lineNumberArea.width(), fontMetrics().height(), Qt.AlignmentFlag.AlignRight.value(), Integer.toString(blockNumber + 1));
            }

            block = block.next();
            top = bottom;
            bottom = top + (int) document().documentLayout().blockBoundingRect(block).height();
            ++blockNumber;
        }

    }

    private static class LineNumberArea extends QWidget {

        private final TextEditorWithLineNumbers codeEditor;
        private final QSize size = new QSize();

        public LineNumberArea(TextEditorWithLineNumbers editor) {
            super(editor);
            codeEditor = editor;
            size.setHeight(0);
        }

        @Override
        public QSize sizeHint() {
            size.setWidth(codeEditor.lineNumberAreaWidth());
            return size;
        }

        @Override
        protected void paintEvent(QPaintEvent event) {
            codeEditor.lineNumberAreaPaintEvent(event);
        }
    };
}
