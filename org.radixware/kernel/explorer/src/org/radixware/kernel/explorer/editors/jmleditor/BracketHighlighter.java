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

import com.trolltech.qt.gui.QTextBlock;
import com.trolltech.qt.gui.QTextCursor;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.explorer.editors.xscmleditor.Highlighter;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;

public class BracketHighlighter {

    private final XscmlEditor editText;
    private final Highlighter hightlighter;
    protected int[] blockPositions;

    BracketHighlighter(final XscmlEditor editText, final Highlighter hightlighter) {
        this.editText = editText;
        this.hightlighter = hightlighter;
    }

    void init() {
        editText.checkBracket.connect(this, "checkBracket()");
        editText.clearHighlightBracket.connect(this, "clearHighlightBracket()");
    }

    void checkBracket() {
        if (!editText.signalsBlocked()) {
            QTextCursor tc = editText.textCursor();
            int pos = tc.position();
            try {
                editText.blockSignals(true);
                tc.setPosition(pos);
                tc.movePosition(QTextCursor.MoveOperation.Left, QTextCursor.MoveMode.KeepAnchor, 1);
                String leftChar = tc.selectedText();
                tc.setPosition(pos);
                tc.movePosition(QTextCursor.MoveOperation.Right, QTextCursor.MoveMode.KeepAnchor, 1);
                String rightChar = tc.selectedText();
                if (!checkBracket(tc, pos - 1, leftChar)) {
                    checkBracket(tc, pos, rightChar);
                }
            } finally {
                editText.blockSignals(false);
            }
        }
    }

    private boolean checkBracket(QTextCursor tc, int pos, String s) {
        if (isOpenBracket(s) || isCloseBracket(s)) {
            String text = editText.toPlainText();
            blockPositions = calcBlockStartEndPosition(text, s, pos);
            bracketHighlight(tc, text);
            return true;
        }
        return false;
    }

    void clearHighlightBracket() {
        if (blockPositions != null) {
            QTextCursor tc = editText.textCursor();
            try {
                editText.blockSignals(true);
                hightlighter.clearBracketIndex();
                boolean onOneLine = isOnOneLine(editText.toPlainText(), blockPositions);
                if (!onOneLine) {
                    if (blockPositions[0] >= 0 && blockPositions[0] < editText.toPlainText().length()) {
                        highlightBlock(tc, blockPositions[0]);
                    }
                }
                if (blockPositions[1] >= 0 && blockPositions[1] < editText.toPlainText().length()) {
                    highlightBlock(tc, blockPositions[1]);
                }
            } finally {
                editText.blockSignals(false);
            }
        }
    }

    private void bracketHighlight(QTextCursor tc, String text) {
        boolean onOneLine = isOnOneLine(text, blockPositions);
        int index0 = calcIndexInLine(text, blockPositions[0]);
        if (!onOneLine && index0 >= 0) {
            hightlighter.setBracketIndex(index0, -1);
            highlightBlock(tc, blockPositions[0]);
            index0 = -1;
        }
        int index1 = calcIndexInLine(text, blockPositions[1]);
        hightlighter.setBracketIndex(index0, index1);
        if (index1 >= 0) {
            highlightBlock(tc, blockPositions[1]);
        }
    }

    private void highlightBlock(QTextCursor tc, int pos) {
        tc.setPosition(pos);
        QTextBlock block = tc.block();
        hightlighter.rehighlightBlock(block);
    }

    private boolean isOnOneLine(String text, int[] blockPositions) {
        int length = text.length();
        if (blockPositions[0] != -1 && blockPositions[1] != -1
                && blockPositions[0] < length && blockPositions[1] < length) {
            if (blockPositions[0] < blockPositions[1]) {
                text = text.substring(blockPositions[0], blockPositions[1]);
            } else {
                text = text.substring(blockPositions[1], blockPositions[0]);
            }
            return text.indexOf('\n') == -1;
        }
        return false;
    }

    private int calcIndexInLine(String text, int blockPositions) {
        if (blockPositions >= 0 && blockPositions < text.length()) {
            String s1 = text.substring(0, blockPositions);
            int index = s1.lastIndexOf('\n');
            if (index != -1) {
                blockPositions = blockPositions - (index + 1);
            }
            return blockPositions;
        }
        return -1;
    }

    private static int[] calcBlockStartEndPosition(String text, String s, int pos) {
        int[] blockPos = new int[2];
        blockPos[0] = pos;
        blockPos[1] = calcPairBracketPos(text, s, pos);
        return blockPos;
    }

    private static int calcPairBracketPos(String text, String s, int pos) {
        List<String> brakets = new ArrayList<>();
        brakets.add(s);
        int res = pos;
        if (isOpenBracket(s)) {
            text = text.substring(pos);
            List<String> lexs = IndentArrangement.scan(text, null);
            int index = 1;
            while (index < lexs.size() && !brakets.isEmpty()) {
                String lex = lexs.get(index);
                if (isOpenBracket(lex)) {
                    brakets.add(lex);
                } else if (isCloseBracket(lex)) {
                    int n = brakets.size() - 1;
                    String lastBracket = brakets.get(n);
                    if (isPairBracket(lastBracket, lex)) {
                        brakets.remove(n);
                    }
                }
                res += lex.length();
                index++;
            }
            return brakets.isEmpty() ? res : -1;
        } else if (isCloseBracket(s)) {
            text = new String(text.substring(0, pos));
            List<String> lexs = IndentArrangement.scan(text, null);
            int index = lexs.size() - 1;
            while (index >= 0 && !brakets.isEmpty()) {
                String lex = lexs.get(index);
                if (isCloseBracket(lex)) {
                    brakets.add(lex);
                } else if (isOpenBracket(lex)) {
                    int n = brakets.size() - 1;
                    String lastBracket = brakets.get(n);
                    if (isPairBracket(lex, lastBracket)) {
                        brakets.remove(n);
                    }
                }
                res -= lex.length();
                index--;
            }
            return brakets.isEmpty() ? res : -1;
        }
        return -1;
    }

    private static boolean isPairBracket(String openBracket, String closeBracket) {
        return "{".equals(openBracket) && "}".equals(closeBracket)
                || "[".equals(openBracket) && "]".equals(closeBracket)
                || "(".equals(openBracket) && ")".equals(closeBracket);
    }

    private static boolean isOpenBracket(String lex) {
        return "{".equals(lex) || "[".equals(lex) || "(".equals(lex);
    }

    private static boolean isCloseBracket(String lex) {
        return "}".equals(lex) || "]".equals(lex) || ")".equals(lex);
    }
}
