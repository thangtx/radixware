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

package org.radixware.kernel.common.scml;

/**
 * Parser of SQL/Java codes.
 * Required to ignore tags in comments.
 * Supports Java and SQL sintax.
 */
public class CommentsAnalizer {

    /**
     * Supported source code languages.
     */
    private enum ELanguage {

        SQL,
        Java
    }
    private final ELanguage language;
    private final char STRING_QUOTE_CHAR;
    private final char SIMPLE_COMMENT_CHAR;

    private CommentsAnalizer(ELanguage language) {
        super();
        this.language = language;
        this.STRING_QUOTE_CHAR = (language == ELanguage.Java ? '"' : '\'');
        this.SIMPLE_COMMENT_CHAR = (language == ELanguage.Java ? '/' : '-');
    }

    public static final class Factory {

        private Factory() {
        }

        public static CommentsAnalizer newJavaCommentsAnalizer() {
            return new CommentsAnalizer(ELanguage.Java);
        }

        public static CommentsAnalizer newSqlCommentsAnalizer() {
            return new CommentsAnalizer(ELanguage.SQL);
        }
    }

    private static enum EState {

        NONE,
        IN_LINE_COMMENT,
        IN_STRING,
        IN_ESC,
        IN_START_OF_BLOCK_COMMENT,
        IN_BLOCK_COMMENT,
        IN_ORACLE_HINT;
    }
    private char cc = 0; // previous char
    private EState state = EState.NONE;

    /**
     * Reset parser to start new sequence.
     */
    public void reset() {
        cc = 0;
        state = EState.NONE;
    }

    /*
     * Simple comment - to the end of line,
     * after it it is required to ignore start of block comment,
     * and the end of block comments if we not in block comment.
     * Block comments are not embedded.
     * Oracle hints - not comments.
     */
    /**
     * Process next character.
     */
    public void process(final char c) {
        switch (state) {
            case NONE:
                if (c == STRING_QUOTE_CHAR) {
                    state = EState.IN_STRING;
                } else if (c == SIMPLE_COMMENT_CHAR && cc == SIMPLE_COMMENT_CHAR) {
                    state = EState.IN_LINE_COMMENT;
                } else if (c == '*' && cc == '/') {
                    state = EState.IN_START_OF_BLOCK_COMMENT;
                }
                break;
            case IN_STRING:
                if (c == '\\') {
                    state = EState.IN_ESC;
                } else if (c == STRING_QUOTE_CHAR) {
                    state = EState.NONE;
                }
                break;
            case IN_ESC:
                state = EState.IN_STRING;
                break;
            case IN_START_OF_BLOCK_COMMENT:
                if (c == '+' && language == ELanguage.SQL) {
                    state = EState.IN_ORACLE_HINT;
                } else {
                    state = EState.IN_BLOCK_COMMENT;
                }
                break;
            case IN_ORACLE_HINT:
            case IN_BLOCK_COMMENT:
                if (c == '/' && cc == '*') {
                    state = EState.NONE;
                }
                break;
            case IN_LINE_COMMENT:
                if (c == '\n') {
                    state = EState.NONE;
                }
                break;
            default:
                throw new IllegalStateException();
        }

        cc = c;
    }

    /**
     * Process next block of text.
     */
    public void process(final String s) {
        for (int i = 0, len = s.length(); i < len; i++) {
            char c = s.charAt(i);
            process(c);
        }
    }

    /**
     * Is the parser in comment now.
     */
    public boolean isInComment() {
        return state == state.IN_BLOCK_COMMENT || state == state.IN_LINE_COMMENT || state == EState.IN_START_OF_BLOCK_COMMENT;
    }

    /**
     * Is the parser in string now.
     */
    public boolean isInString() {
        return state == state.IN_STRING || state == EState.IN_ESC;
    }
}
