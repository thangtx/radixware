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

package org.radixware.kernel.designer.common.dialogs.scmlnb;

import org.netbeans.api.lexer.PartType;
import org.netbeans.api.lexer.Token;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerInput;
import org.netbeans.spi.lexer.LexerRestartInfo;
import org.netbeans.spi.lexer.TokenFactory;


public class SQLScriptLexer implements Lexer<SQLScriptTokenId> {
    
    private static enum EState {
        //init state

        IS_BEGIN,
        //after ...THEN
        IS_AFTER_PREPROCESSOR,
        //inside of the script
        IS_I_SCRIPT,
        //inside of the preprocessor
        IS_I_PREPROCESSOR,
        //inside of the SQL*Plus
        IS_I_SQLPLUS,
        //enf of file
        IS_EOF;
    }
    private static final int EOF = LexerInput.EOF;
    private EState state = EState.IS_BEGIN;
    private final LexerRestartInfo<SQLScriptTokenId> info;
    private final TokenFactory<SQLScriptTokenId> tokenFactory;
    private final LexerInput input;
    private Token<SQLScriptTokenId> pendingToken = null;
    
    public SQLScriptLexer(LexerRestartInfo<SQLScriptTokenId> info) {
        this.info = info;
        this.input = info.input();
        this.tokenFactory = info.tokenFactory();
        if (info.state() instanceof InternalState) {
            this.state = ((InternalState) info.state()).state;
            this.pendingToken = ((InternalState) info.state()).pendingToken;
        }
    }
    
    private class InternalState {
        
        public InternalState(final EState state, final Token<SQLScriptTokenId> pendingToken) {
            assert state != null : "Attempt to construct InternalState with null EState";
            this.state = state;
            this.pendingToken = pendingToken;
        }
        EState state;
        Token<SQLScriptTokenId> pendingToken;
    }
    
    @Override
    public Token<SQLScriptTokenId> nextToken() {
        Token<SQLScriptTokenId> next = debugNextToken();
        return next;
    }
    
    @SuppressWarnings("fallthrough")
    private Token<SQLScriptTokenId> debugNextToken() {
        if (pendingToken != null) {
            final Token<SQLScriptTokenId> result = tokenFactory.createToken(pendingToken.id(), pendingToken.length(), pendingToken.partType());
            pendingToken = null;
            return result;
        }
        TOPLEVEL_SCAN:
        while (true) {
            switch (state) {
                case IS_AFTER_PREPROCESSOR:
                case IS_BEGIN:
                    switch (input.read()) {
                        case '#'://preprocessor mark
                            if (matchCaseInsensitive(input, "ELSE") || matchCaseInsensitive(input, "ENDIF")) {
                                state = EState.IS_AFTER_PREPROCESSOR;
                            } else if (matchCaseInsensitive(input, "IF")) {
                                state = EState.IS_I_PREPROCESSOR;
                            } else {
                                return tokenFactory.createToken(SQLScriptTokenId.SQL_TEXT);
                            }
                            return tokenFactory.createToken(SQLScriptTokenId.PREPROCESSOR_MARK);
                        case '!'://script mark
                            if (state == EState.IS_BEGIN) {
                                state = EState.IS_I_SCRIPT;
                                return tokenFactory.createToken(SQLScriptTokenId.SCRIPT_MARK);
                            }
                        case '-'://possible single line comment
                            Token<SQLScriptTokenId> slCommentToken = matchSlComment();
                            if (slCommentToken != null) {//single line comment
                                return slCommentToken;
                            } else {//not a comment, switch to SQL*Plus
                                state = EState.IS_I_SQLPLUS;
                                input.backup(1);
                            }
                            break;
                        case '/'://possible block comment
                            final Token<SQLScriptTokenId> mlCommentToken = matchMlComment();
                            if (mlCommentToken != null) {//comment
                                return mlCommentToken;
                            } else {//not a comment, switch to SQL*Plus
                                state = EState.IS_I_SQLPLUS;
                                input.backup(1);
                            }
                            break;
                        case ' '://it's allowed to have few whitespaces ot tab's prior to the script or preprocessor mark
                        case '\t':
                        case '\r':
                            input.consumeNewline();//comment does not eat new line, so this is a case when comment occured in IS_BEGIN state
                        case '\n':
                            return tokenFactory.createToken(SQLScriptTokenId.WHITESPACE);
                        case EOF:
                            if (input.readLength() > 1) {
                                throw new IllegalStateException("Unrecognized input before EOF: " + input.readText());
                            }
                            if (input.readLength() == 0) {
                                return null;
                            }
                            state = EState.IS_EOF;
                            return tokenFactory.createToken(SQLScriptTokenId.EOF);
                        default://no script or preprocessor, switching to SQL*Plus
                            input.backup(1);
                            state = EState.IS_I_SQLPLUS;
                    }
                    break;
                case IS_I_PREPROCESSOR://read line with preprocessor and switch to begin state
                    while (true) {
                        int c;
                        c = input.read();
                        switch (c) {
                            case '-'://possible single line comment
                                final int lenghtBeforeSlComment = input.readLength() - 1;//lengh of the preprocessor before comment
                                Token<SQLScriptTokenId> slCommentToken = matchSlComment();
                                if (slCommentToken != null) {//single line comment
                                    if (lenghtBeforeSlComment > 0) {//there was preprocessor before comment
                                        pendingToken = slCommentToken;//return preprocessor before comment and set comment as pending token
                                        return tokenFactory.createToken(SQLScriptTokenId.PREPROCESSOR, lenghtBeforeSlComment);
                                    } else {//simply return comment
                                        return slCommentToken;
                                    }
                                }
                                break;
                            case '/'://possible block comment
                                final int lenghtBeforeMlComment = input.readLength() - 1;//lengh of the preprocessor before comment
                                final Token<SQLScriptTokenId> mlCommentToken = matchMlComment();
                                if (mlCommentToken != null) {//comment
                                    if (lenghtBeforeMlComment > 0) {//there was preprocessor before comment
                                        pendingToken = mlCommentToken;//return preprocessor before comment and set comment as pending token
                                        return tokenFactory.createToken(SQLScriptTokenId.PREPROCESSOR, lenghtBeforeMlComment);
                                    } else {//simply return comment
                                        return mlCommentToken;
                                    }
                                }
                                break;
                            case '\t':
                            case ' ':
                                if (matchCaseInsensitive(input, "THEN")) {
                                    state = EState.IS_AFTER_PREPROCESSOR;
                                    return tokenFactory.createToken(SQLScriptTokenId.PREPROCESSOR);
                                } else {
                                    continue;
                                }
                            case '\r':
                                input.consumeNewline();
                            case '\n':
                                state = EState.IS_BEGIN;
                                return tokenFactory.createToken(SQLScriptTokenId.PREPROCESSOR);
                            case EOF:
                                //state = EState.IS_BEGIN;
                                if (input.readLength() > 0) {
                                    return tokenFactory.createToken(SQLScriptTokenId.PREPROCESSOR);
                                } else {
                                    return null;
                                }
                        }
                    }
                case IS_I_SCRIPT://read line with script and switch to begin state
                    while (true) {
                        switch (input.read()) {
                            case '-'://possible single line comment
                                final int lenghtBeforeSlComment = input.readLength() - 1;//lengh of the script before comment
                                Token<SQLScriptTokenId> slCommentToken = matchSlComment();
                                if (slCommentToken != null) {//single line comment
                                    if (lenghtBeforeSlComment > 0) {//there was script before comment
                                        pendingToken = slCommentToken;//return script before comment and set comment as pending token
                                        return tokenFactory.createToken(SQLScriptTokenId.SCRIPT, lenghtBeforeSlComment);
                                    } else {//simply return comment
                                        return slCommentToken;
                                    }
                                }
                                break;
                            case '/'://possible block comment
                                final int lenghtBeforeMlComment = input.readLength() - 1;//lengh of the script before comment
                                final Token<SQLScriptTokenId> mlCommentToken = matchMlComment();
                                if (mlCommentToken != null) {//block comment
                                    if (lenghtBeforeMlComment > 0) {//there was script before comment
                                        pendingToken = mlCommentToken;//return script before comment and set comment as pending token
                                        return tokenFactory.createToken(SQLScriptTokenId.SCRIPT, lenghtBeforeMlComment);
                                    } else {//simply return comment
                                        return mlCommentToken;
                                    }
                                }
                                break;
                            case '\r':
                                input.consumeNewline();
                            case '\n':
                                state = EState.IS_BEGIN;
                                return tokenFactory.createToken(SQLScriptTokenId.SCRIPT);
                            case EOF:
                                if (input.readLength() > 0) {
                                    return tokenFactory.createToken(SQLScriptTokenId.SCRIPT);
                                } else {
                                    return null;
                                }
                        }
                    }
                case IS_I_SQLPLUS://read SQL*Plus statement until single '/' on a line
                    while (true) {
                        switch (input.read()) {
                            case '#':
                                state = EState.IS_BEGIN;
                                input.backup(1);
                                continue TOPLEVEL_SCAN;
                            case '-'://possible single line comment
                                final int lenghtBeforeSlComment = input.readLength() - 1;//lengh of the SQL*Plus before comment
                                Token<SQLScriptTokenId> slCommentToken = matchSlComment();
                                if (slCommentToken != null) {//single line comment
                                    if (lenghtBeforeSlComment > 0) {//there was SQL*Plus before comment
                                        pendingToken = slCommentToken;//return SQL*Plus before comment and set comment as pending token
                                        return tokenFactory.createToken(SQLScriptTokenId.SQL_TEXT, lenghtBeforeSlComment);
                                    } else {//simply return comment
                                        return slCommentToken;
                                    }
                                }
                            case '/'://possible block comment
                                final int lenghtBeforeMlComment = input.readLength() - 1;//lengh of the SQL*Plus before comment
                                final Token<SQLScriptTokenId> mlCommentToken = matchMlComment();
                                if (mlCommentToken != null) {//block comment
                                    if (lenghtBeforeMlComment > 0) {//there was SQL*Plus before comment
                                        pendingToken = mlCommentToken;//return SQL*Plus before comment and set comment as pending token
                                        return tokenFactory.createToken(SQLScriptTokenId.SQL_TEXT, lenghtBeforeMlComment);
                                    } else {//simply return comment
                                        return mlCommentToken;
                                    }
                                }
                            case '\'':
                                scanLiteral(input, '\'');
                                return tokenFactory.createToken(SQLScriptTokenId.STRING_LITERAL);
                            case '\"':
                                scanLiteral(input, '\"');
                                return tokenFactory.createToken(SQLScriptTokenId.STRING_LITERAL);
                            case '\r':
                                input.consumeNewline();
                            case '\n':
                                if (input.read() == '/') {//possible end of statment
                                    switch (input.read()) {
                                        case '\r':
                                            input.consumeNewline();
                                        case '\n':
                                            state = EState.IS_BEGIN;
                                            break;
                                        case EOF:
                                            state = EState.IS_EOF;
                                            break;
                                        default:
                                            input.backup(2);
                                        
                                    }
                                } else {//not an end of statement, return character to input and contnue parsing
                                    input.backup(1);
                                }
                                return tokenFactory.createToken(SQLScriptTokenId.WHITESPACE);
                            case EOF:
                                if (input.readLength() == 0) {
                                    return null;
                                }
                                return tokenFactory.createToken(SQLScriptTokenId.SQL_TEXT);
                            default:
                                input.backup(1);
                                if (scanWord(input)) {
                                    if (SQLKeywords.isKeyWord(input.readText().toString())) {
                                        return tokenFactory.createToken(SQLScriptTokenId.SQL_KEYWORD);
                                    } else {
                                        return tokenFactory.createToken(SQLScriptTokenId.SQL_TEXT);
                                    }
                                } else if (scanWhiteSpace(input)) {
                                    return tokenFactory.createToken(SQLScriptTokenId.WHITESPACE);
                                } else {
                                    return tokenFactory.createToken(SQLScriptTokenId.SQL_TEXT);//unknown
                                }
                        }
                    }
                case IS_EOF:
                    return null;
            }
        }
    }
    
    private boolean scanWhiteSpace(LexerInput input) {
        int c;
        while (true) {
            c = input.read();
            if (c == EOF || !Character.isWhitespace(c)) {
                input.backup(1);
                return input.readLength() > 0;
            }
        }
    }
    
    private boolean scanWord(LexerInput input) {
        int c;
        while (true) {
            c = input.read();
            if (c == EOF) {
                input.backup(1);
                return input.readLength() > 0;
            }
            if (input.readLength() == 0 && Character.isJavaIdentifierStart(c) || Character.isJavaIdentifierPart(c)) {
                continue;
            } else {
                if (input.readLength() > 1) {
                    input.backup(1);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
    
    private void scanLiteral(final LexerInput input, char endChar) {
        int c;
        while (true) {
            c = input.read();
            if (c == EOF || c == '\r' || c == '\n') {
                input.backup(1);
                return;
            } else if (c == endChar) {
                return;
            }
        }
    }
    
    private static boolean matchCaseInsensitive(LexerInput input, final String word) {
        int c;
        int readCount = 0;
        while ((c = input.read()) != EOF) {
            readCount++;
            if (c != word.charAt(readCount - 1)) {
                input.backup(readCount);
                return false;
            }
            if (readCount == word.length()) {
                return true;
            }
        }
        readCount++;//EOF
        input.backup(readCount);
        return false;
    }

    /**
     * Tries to match multline comment, assuming that the open '/' is already
     * read from input, so actually it matches comment without first slash
     *
     * @return token if comment was matched and read from input and null if no
     * comment was found and all characters read by this method were returned in
     * input.
     */
    private Token<SQLScriptTokenId> matchMlComment() {
        final int lenghtBeforeComment = input.readLength() - 1;
        if (input.read() == '*') {//multi line comment
            while (true) {
                switch (input.read()) {
                    case '*':
                        if (input.read() == '/') {//end of multi line comment
                            return tokenFactory.createToken(SQLScriptTokenId.BLOCK_COMMENT, input.readLength() - lenghtBeforeComment);
                        } else {
                            input.backup(1);
                        }
                        break;
                    case EOF:
                        return tokenFactory.createToken(SQLScriptTokenId.BLOCK_COMMENT, input.readLength() - lenghtBeforeComment, PartType.START);
                }
            }
        } else {
            input.backup(1);
            return null;
        }
    }

    /**
     * Same as {@link TestLexer#matchMlComment() } but for single line comment
     *
     * @return token if comment was matched and null if not
     */
    @SuppressWarnings("fallthrough")
    private Token<SQLScriptTokenId> matchSlComment() {
        final int lenghtBeforeComment = input.readLength() - 1;
        if (input.read() == '-') {//single line comment
            while (true) {
                switch (input.read()) {
                    case '\r':
                        input.consumeNewline();
                    case '\n':
                    case EOF:
                        return tokenFactory.createToken(SQLScriptTokenId.LINE_COMMENT, input.readLength() - lenghtBeforeComment);
                }
            }
        } else {//not a comment. return read characer to input
            input.backup(1);
            return null;
        }
    }
    
    @Override
    public Object state() {
        return new InternalState(state, pendingToken);
    }
    
    @Override
    public void release() {
        //do nothing
    }
}
