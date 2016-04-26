/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 * 
 * April 2015: adapted to work with Jml documents by Compass Plus Limited.
 */

package org.radixware.kernel.designer.common.dialogs.jmlnb;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.editor.settings.SimpleValueNames;
import org.netbeans.api.lexer.PartType;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.Utilities;
import org.netbeans.lib.editor.util.swing.DocumentUtilities;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;


class JmlBraceCompletion {

    static void onInsertChar(BaseDocument doc, int caretOffset, Caret caret, char ch) throws BadLocationException {
        if (!completionSettingEnabled()) {
            return;
        }

        if (ch == ')' || ch == ']' || ch == '(' || ch == '[' || ch == ';') {
            final TokenSequence<JmlTokenId> javaTS = enumerateTokens(doc, caretOffset, false);
            if (javaTS != null) {
                switch (javaTS.token().id()) {
                    case RPAREN:
                    case RBRACKET:
                        skipClosingBracket(doc, caret, ch, javaTS);
                        break;
                    case LPAREN:
                    case LBRACKET:
                        completeOpeningBracket(doc, caretOffset, caret, ch, javaTS);
                        break;
                    case SEMICOLON:
                        moveSemicolonIfPossible(doc, caretOffset, caret, javaTS);
                }
            }
        }
    }

    private static void moveSemicolonIfPossible(BaseDocument doc, int cursorPosition, Caret cursor,
            TokenSequence<JmlTokenId> ts) throws BadLocationException {
        int eolOffset = Utilities.getRowEnd(doc, cursorPosition);
        int lastParenPos = cursorPosition;
        int index = ts.index();
        // Move beyond semicolon
        while (ts.moveNext() && ts.offset() <= eolOffset) {
            Token<JmlTokenId> token = ts.token();
            switch (token.id()) {
                case RPAREN:
                    lastParenPos = ts.offset();
                    break;
                case WHITESPACE:
                    break;
                default:
                    return; //
            }
        }
        // Restore ts position
        ts.moveIndex(index);
        ts.moveNext();
        if (isForLoopSemicolon(ts) || posWithinAnyQuote(doc, cursorPosition)) {
            return;
        }
        doc.remove(cursorPosition, 1);
        doc.insertString(lastParenPos, ";", null); // NOI18N
        cursor.setDot(lastParenPos + 1);
    }

    private static boolean isForLoopSemicolon(TokenSequence<JmlTokenId> ts) {
        Token<JmlTokenId> token = ts.token();
        if (token == null || token.id() != JmlTokenId.SEMICOLON) {
            return false;
        }
        int parenDepth = 0; // parenthesis depth
        int braceDepth = 0; // brace depth
        boolean semicolonFound = false; // next semicolon
        int tsOrigIndex = ts.index();
        try {
            while (ts.movePrevious()) {
                token = ts.token();
                switch (token.id()) {
                    case LPAREN:
                        if (parenDepth == 0) { // could be a 'for ('
                            while (ts.movePrevious()) {
                                token = ts.token();
                                switch (token.id()) {
                                    case WHITESPACE:
                                    case BLOCK_COMMENT:
                                    case JAVADOC_COMMENT:
                                    case LINE_COMMENT:
                                        break; // skip
                                    case FOR:
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                            return false;
                        } else { // non-zero depth
                            parenDepth--;
                        }
                        break;

                    case RPAREN:
                        parenDepth++;
                        break;

                    case LBRACE:
                        if (braceDepth == 0) { // unclosed left brace
                            return false;
                        }
                        braceDepth--;
                        break;

                    case RBRACE:
                        braceDepth++;
                        break;

                    case SEMICOLON:
                        if (semicolonFound) { // one semicolon already found
                            return false;
                        }
                        semicolonFound = true;
                        break;
                }
            }
        } finally {
            // Restore orig TS's location
            ts.moveIndex(tsOrigIndex);
            ts.moveNext();
        }
        return false;
    }

    /**
     * Hook called after a character *ch* was backspace-deleted from *doc*. The
     * function possibly removes bracket or quote pair if appropriate.
     *
     * @param doc the document
     * @param caretOffset position of the change
     * @param caret caret
     * @param ch the character that was deleted
     */
    static void charBackspaced(BaseDocument doc, int caretOffset, Caret caret, char ch) throws BadLocationException {
        if (!completionSettingEnabled()) {
            return;
        }
        // Get token (forward bias) behind removed char
        TokenSequence<JmlTokenId> ts = enumerateTokens(doc, caretOffset, false);
        if (ts == null) {
            return;
        }
        if (ch == '(' || ch == '[') {
            switch (ts.token().id()) {
                case RPAREN:
                    if (tokenBalance(doc, JmlTokenId.LPAREN) != 0) {
                        doc.remove(caretOffset, 1);
                    }
                    break;
                case RBRACKET:
                    if (tokenBalance(doc, JmlTokenId.LBRACKET) != 0) {
                        doc.remove(caretOffset, 1);
                    }
                    break;
            }
        } else if (ch == '\"') {
            if (ts.token().id() == JmlTokenId.STRING_LITERAL && ts.offset() == caretOffset) {
                doc.remove(caretOffset, 1);
            }
        } else if (ch == '\'') {
            if (ts.token().id() == JmlTokenId.CHAR_LITERAL && ts.offset() == caretOffset) {
                doc.remove(caretOffset, 1);
            }
        }
    }

    static int tokenBalance(BaseDocument doc, JmlTokenId leftTokenId) {
        JmlTokenBalance tb = JmlTokenBalance.get(doc);
        if (!tb.isTracked(JmlTokenId.language())) {
            tb.addTokenPair(JmlTokenId.language(), JmlTokenId.LPAREN, JmlTokenId.RPAREN);
            tb.addTokenPair(JmlTokenId.language(), JmlTokenId.LBRACKET, JmlTokenId.RBRACKET);
            tb.addTokenPair(JmlTokenId.language(), JmlTokenId.LBRACE, JmlTokenId.RBRACE);
        }
        int balance = tb.balance(JmlTokenId.language(), leftTokenId);
        assert (balance != Integer.MAX_VALUE);
        return balance;
    }

    /**
     * Resolve whether pairing right curly should be added automatically at the
     * caret position or not.
     * <br>
     * There must be only whitespace or line comment or block comment between
     * the caret position and the left brace and the left brace must be on the
     * same line where the caret is located.
     * <br>
     * The caret must not be "contained" in the opened block comment token.
     *
     * @param doc document in which to operate.
     * @param caretOffset offset of the caret.
     * @return true if a right brace '}' should be added or false if not.
     */
    static boolean isAddRightBrace(BaseDocument doc, int caretOffset)
            throws BadLocationException {
        if (!completionSettingEnabled()) {
            return false;
        }
        if (tokenBalance(doc, JmlTokenId.LBRACE) <= 0) {
            return false;
        }
        int caretRowStartOffset = Utilities.getRowStart(doc, caretOffset);
        TokenSequence<JmlTokenId> ts = enumerateTokens(doc, caretOffset, true);
        if (ts == null) {
            return false;
        }
        boolean first = true;
        do {
            if (ts.offset() < caretRowStartOffset) {
                return false;
            }
            switch (ts.token().id()) {
                case WHITESPACE:
                case LINE_COMMENT:
                    break;
                case BLOCK_COMMENT:
                case JAVADOC_COMMENT:
                    if (first && caretOffset > ts.offset() && caretOffset < ts.offset() + ts.token().length()) {
                        // Caret contained within block comment -> do not add anything
                        return false;
                    }
                    break; // Skip
                case LBRACE:
                    return true;
            }
            first = false;
        } while (ts.movePrevious());
        return false;
    }

    /**
     * Returns position of the first unpaired closing paren/brace/bracket from
     * the caretOffset till the end of caret row. If there is no such element,
     * position after the last non-white character on the caret row is returned.
     */
    @SuppressWarnings("fallthrough")
    static int getRowOrBlockEnd(BaseDocument doc, int caretOffset, boolean[] insert) throws BadLocationException {
        int rowEnd = Utilities.getRowLastNonWhite(doc, caretOffset);
        if (rowEnd == -1 || caretOffset >= rowEnd) {
            return caretOffset;
        }
        rowEnd += 1;
        int parenBalance = 0;
        int braceBalance = 0;
        int bracketBalance = 0;
        TokenSequence<JmlTokenId> ts = enumerateTokens(doc, caretOffset, false);
        if (ts == null) {
            return caretOffset;
        }
        while (ts.offset() < rowEnd) {
            switch (ts.token().id()) {
                case SEMICOLON:
                    return ts.offset() + 1;
                case LPAREN:
                    parenBalance++;
                    break;
                case RPAREN:
                    if (parenBalance-- == 0) {
                        return ts.offset();
                    }
                case LBRACE:
                    braceBalance++;
                    break;
                case RBRACE:
                    if (braceBalance-- == 0) {
                        return ts.offset();
                    }
                case LBRACKET:
                    bracketBalance++;
                    break;
                case RBRACKET:
                    if (bracketBalance-- == 0) {
                        return ts.offset();
                    }
            }
            if (!ts.moveNext()) {
                break;
            }
        }

        insert[0] = false;
        return rowEnd;
    }

    private static TokenSequence<JmlTokenId> enumerateTokens(Document doc, int offset, boolean backwardBias) {
        TokenHierarchy<?> hi = TokenHierarchy.get(doc);
        List<TokenSequence<?>> tsList = hi.embeddedTokenSequences(offset, backwardBias);
        // Go from inner to outer TSes
        for (int i = tsList.size() - 1; i >= 0; i--) {
            TokenSequence<?> ts = tsList.get(i);
            if (ts.languagePath().innerLanguage() == JmlTokenId.language()) {
                TokenSequence<JmlTokenId> javaInnerTS = (TokenSequence<JmlTokenId>) ts;
                return javaInnerTS;
            }
        }
        return null;
    }

    private static int braceBalance(BaseDocument doc)
            throws BadLocationException {
        return tokenBalance(doc, JmlTokenId.LBRACE);
    }
    private static void skipClosingBracket(BaseDocument doc, Caret caret, char rightBracketChar,
            TokenSequence<JmlTokenId> innerTS) throws BadLocationException {
        JmlTokenId bracketId = bracketCharToId(rightBracketChar);
        int caretOffset = caret.getDot();
        if (isSkipClosingBracket(doc, caretOffset, bracketId)) {
            doc.remove(caretOffset - 1, 1);
            caret.setDot(caretOffset); // skip closing bracket
        }
    }
    private static Set<JmlTokenId> STOP_TOKENS_FOR_SKIP_CLOSING_BRACKET = EnumSet.of(JmlTokenId.LBRACE, JmlTokenId.RBRACE, JmlTokenId.SEMICOLON);

   
    static boolean isSkipClosingBracket(BaseDocument doc, int caretOffset, JmlTokenId rightBracketId) throws BadLocationException {
        if (caretOffset == doc.getLength()) {
            return false; // no skip in this case
        }
        boolean skipClosingBracket = false; // by default do not remove
        TokenSequence<JmlTokenId> javaTS = enumerateTokens(doc, caretOffset, false);
        if (javaTS != null && javaTS.token().id() == rightBracketId) {
            JmlTokenId leftBracketId = matching(rightBracketId);
            // Skip all the brackets of the same type that follow the last one
            do {
                if (STOP_TOKENS_FOR_SKIP_CLOSING_BRACKET.contains(javaTS.token().id()) || (javaTS.token().id() == JmlTokenId.WHITESPACE && javaTS.token().text().toString().contains("\n"))) {
                    while (javaTS.token().id() != rightBracketId && javaTS.movePrevious());
                    break;
                }
            } while (javaTS.moveNext());
            int braceBalance = 0; // balance of '{' and '}'
            int bracketBalance = -1; // balance of the brackets or parenthesis
            int lastRBracketIndex = javaTS.index();
            boolean finished = false;
            while (!finished && javaTS.movePrevious()) {
                JmlTokenId id = javaTS.token().id();
                switch (id) {
                    case LPAREN:
                    case LBRACKET:
                        if (id == leftBracketId) {
                            bracketBalance++;
                            if (bracketBalance == 0) {
                                if (braceBalance != 0) {
                                 bracketBalance = 1;
                                }
                                finished = true;
                            }
                        }
                        break;

                    case RPAREN:
                    case RBRACKET:
                        if (id == rightBracketId) {
                            bracketBalance--;
                        }
                        break;

                    case LBRACE:
                        braceBalance++;
                        if (braceBalance > 0) { // stop on extra left brace
                            finished = true;
                        }
                        break;

                    case RBRACE:
                        braceBalance--;
                        break;
                }
            }

            skipClosingBracket = bracketBalance != 0;

        }
        return skipClosingBracket;
    }

    /**
     * Check for various conditions and possibly add a pairing bracket. to the
     * already inserted.
     *
     * @param doc the document
     * @param caretOffset position of the opening bracket (already in the doc)
     * @param caret caret
     * @param bracket the bracket that was inserted
     */
    private static void completeOpeningBracket(BaseDocument doc, int caretOffset, Caret caret, char bracketChar,
            TokenSequence<JmlTokenId> innerTS) throws BadLocationException {
        if (isCompletablePosition(doc, caretOffset + 1)) {
            doc.insertString(caretOffset + 1, String.valueOf(matching(bracketChar)), null);
            caret.setDot(caretOffset + 1);
        }
    }

    private static boolean isEscapeSequence(BaseDocument doc, int caretOffset) throws BadLocationException {
        if (caretOffset <= 0) {
            return false;
        }
        char previousChar = doc.getChars(caretOffset - 1, 1)[0];
        return previousChar == '\\';
    }

    /**
     * Called to decide whether either single bracket should be inserted or
     * whether bracket pair should be inserted instead. It's called before
     * anything was inserted into the document.
     *
     * @param doc the document
     * @param caretOffset position of the opening bracket (already in the doc)
     * @param caret caret
     * @param bracket the character that was inserted
     * @return true if the method serviced insertions or removals or false if
     * there's nothing special to be done and the parent should handle the
     * insertion regularly.
     */
    static boolean completeQuote(BaseDocument doc, int caretOffset, Caret caret, char bracket)
            throws BadLocationException {
        if (!completionSettingEnabled()) {
            return false;
        }
        if (isEscapeSequence(doc, caretOffset)) { // \" or \' typed
            return false;
        }
        // Examine token id at the caret offset
        TokenSequence<JmlTokenId> javaTS = enumerateTokens(doc, caretOffset, true);
        JmlTokenId id = (javaTS != null) ? javaTS.token().id() : null;
        int lastNonWhite = Utilities.getRowLastNonWhite(doc, caretOffset);
        // eol - true if the caret is at the end of line (ignoring whitespaces)
        boolean eol = lastNonWhite < caretOffset;

        // If caret within comment return false
        boolean caretInsideToken = (id != null)
                && (javaTS.offset() + javaTS.token().length() > caretOffset
                || javaTS.token().partType() == PartType.START);
        if (caretInsideToken && (id == JmlTokenId.BLOCK_COMMENT
                || id == JmlTokenId.JAVADOC_COMMENT
                || id == JmlTokenId.LINE_COMMENT)) {
            return false;
        }
        boolean completablePosition = isQuoteCompletablePosition(doc, caretOffset);
        boolean insideString = caretInsideToken
                && (id == JmlTokenId.STRING_LITERAL
                || id == JmlTokenId.CHAR_LITERAL);
        if (insideString) {
            if (eol) {
                return false; // do not complete
            } else {
                //#69524
                char chr = doc.getChars(caretOffset, 1)[0];
                if (chr == bracket) {
                    //#83044
                    if (caretOffset > 0) {
                        javaTS.move(caretOffset - 1);
                        if (javaTS.moveNext()) {
                            id = javaTS.token().id();
                            if (id == JmlTokenId.STRING_LITERAL
                                    || id == JmlTokenId.CHAR_LITERAL) {
                                doc.insertString(caretOffset, String.valueOf(bracket), null); //NOI18N
                                doc.remove(caretOffset, 1);
                                return true;
                            }
                        }
                    }
                    //end of #83044
                }
                //end of #69524
            }
        }

        if ((completablePosition && !insideString) || eol) {
            doc.insertString(caretOffset, String.valueOf(bracket) + bracket, null); //NOI18N
            return true;
        }

        return false;
    }

    /**
     * Checks whether caretOffset is a position at which bracket and quote
     * completion is performed. Brackets and quotes are not completed everywhere
     * but just at suitable places .
     *
     * @param doc the document
     * @param caretOffset position to be tested
     */
    private static boolean isCompletablePosition(BaseDocument doc, int caretOffset)
            throws BadLocationException {
        if (caretOffset == doc.getLength()) // there's no other character to test
        {
            return true;
        } else {
            // test that we are in front of ) , " or '
            char chr = doc.getChars(caretOffset, 1)[0];
            return (chr == ')'
                    || chr == ','
                    || chr == '\"'
                    || chr == '\''
                    || chr == ' '
                    || chr == ']'
                    || chr == '}'
                    || chr == '\n'
                    || chr == '\t'
                    || chr == ';');
        }
    }

    private static boolean isQuoteCompletablePosition(BaseDocument doc, int caretOffset)
            throws BadLocationException {
        if (caretOffset == doc.getLength()) { // there's no other character to test
            return true;
        } else {
            // test that we are in front of ) , " or ' ... etc.
            int eolOffset = Utilities.getRowEnd(doc, caretOffset);
            if (caretOffset == eolOffset || eolOffset == -1) {
                return false;
            }
            int firstNonWhiteFwdOffset = Utilities.getFirstNonWhiteFwd(doc, caretOffset, eolOffset);
            if (firstNonWhiteFwdOffset == -1) {
                return false;
            }
            char chr = doc.getChars(firstNonWhiteFwdOffset, 1)[0];
            return (chr == ')'
                    || chr == ','
                    || chr == '+'
                    || chr == '}'
                    || chr == ';');
        }
    }

    /**
     * Returns true if bracket completion is enabled in options.
     */
    static boolean completionSettingEnabled() {
        Preferences prefs = MimeLookup.getLookup(ScmlEditor.JML_MIME_TYPE).lookup(Preferences.class);
        return prefs.getBoolean(SimpleValueNames.COMPLETION_PAIR_CHARACTERS, false);
    }

    /**
     * Returns for an opening bracket or quote the appropriate closing
     * character.
     */
    private static char matching(char bracket) {
        switch (bracket) {
            case '(':
                return ')';
            case '[':
                return ']';
            case '\"':
                return '\"'; // NOI18N
            case '\'':
                return '\'';
            default:
                return ' ';
        }
    }

    private static JmlTokenId matching(JmlTokenId id) {
        switch (id) {
            case LPAREN:
                return JmlTokenId.RPAREN;
            case LBRACKET:
                return JmlTokenId.RBRACKET;
            case RPAREN:
                return JmlTokenId.LPAREN;
            case RBRACKET:
                return JmlTokenId.LBRACKET;
            default:
                return null;
        }
    }

    private static JmlTokenId bracketCharToId(char bracket) {
        switch (bracket) {
            case '(':
                return JmlTokenId.LPAREN;
            case ')':
                return JmlTokenId.RPAREN;
            case '[':
                return JmlTokenId.LBRACKET;
            case ']':
                return JmlTokenId.RBRACKET;
            case '{':
                return JmlTokenId.LBRACE;
            case '}':
                return JmlTokenId.RBRACE;
            default:
                throw new IllegalArgumentException("Not a bracket char '" + bracket + '\'');
        }
    }

    /**
     * posWithinString(doc, pos) iff position *pos* is within a string literal
     * in document doc.
     *
     * @param doc the document
     * @param caretOffset position to be tested (before '\n' gets inserted into
     * doc.
     */
    static boolean posWithinString(BaseDocument doc, int caretOffset) {
        return posWithinQuotes(doc, caretOffset, '"', JmlTokenId.STRING_LITERAL);
    }

    /**
     * Generalized posWithingString to any token and delimiting character. It
     * works for tokens are delimited by *quote* and extend up to the other
     * *quote* or whitespace in case of an incomplete token.
     *
     * @param doc the document
     * @param caretOffset position of typed quote
     */
    static boolean posWithinQuotes(BaseDocument doc, int caretOffset, char quote, JmlTokenId tokenId) {
        TokenSequence<JmlTokenId> javaTS = enumerateTokens(doc, caretOffset - 1, false);
        if (javaTS != null) {
            if (caretOffset == 0) {
                return false;
            }
            return javaTS.token().id() == tokenId
                    && (caretOffset - javaTS.offset() == 1
                    || DocumentUtilities.getText(doc).charAt(caretOffset - 1) != quote);
        }
        return false;
    }

    static boolean posWithinAnyQuote(BaseDocument doc, int caretOffset) {
        TokenSequence<JmlTokenId> javaTS = enumerateTokens(doc, caretOffset - 1, false);
        if (javaTS != null) {
            JmlTokenId id = javaTS.token().id();
            if (id == JmlTokenId.STRING_LITERAL
                    || id == JmlTokenId.CHAR_LITERAL) {
                char ch = DocumentUtilities.getText(doc).charAt(caretOffset - 1);
                return (caretOffset - javaTS.offset() == 1 || (ch != '"' && ch != '\''));
            }
        }
        return false;
    }

    static boolean isUnclosedStringAtLineEnd(BaseDocument doc, int caretOffset) {
        int lastNonWhiteOffset;
        try {
            lastNonWhiteOffset = Utilities.getRowLastNonWhite(doc, caretOffset);
        } catch (BadLocationException e) {
            return false;
        }
        TokenSequence<JmlTokenId> javaTS = enumerateTokens(doc, lastNonWhiteOffset, true);
        if (javaTS != null) {
            return (javaTS.token().id() == JmlTokenId.STRING_LITERAL);
        }
        return false;
    }
}
