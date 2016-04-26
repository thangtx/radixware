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

import java.util.Collection;
import java.util.EnumSet;
import org.netbeans.api.lexer.InputAttributes;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.LanguagePath;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.spi.lexer.LanguageEmbedding;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;


public enum SQLScriptTokenId implements TokenId {

    SCRIPT("script"),
    SCRIPT_MARK("script"),
    PREPROCESSOR("preprocessor"),
    PREPROCESSOR_MARK("preprocessor"),
    SQL_TEXT("sql-text"),
    SQL_KEYWORD("sql-keyword"),
    STRING_LITERAL("string-literal"),
    LINE_COMMENT("comment"),
    BLOCK_COMMENT("comment"),
    WHITESPACE("whitespace"),
    EOF("eof");
    private final String primaryCatgory;

    private SQLScriptTokenId(String primaryCatgory) {
        this.primaryCatgory = primaryCatgory;
    }

    @Override
    public String primaryCategory() {
        return primaryCatgory;
    }
    private static final Language<SQLScriptTokenId> language = new LanguageHierarchy<SQLScriptTokenId>() {

        @Override
        protected Collection<SQLScriptTokenId> createTokenIds() {
            return EnumSet.allOf(SQLScriptTokenId.class);
        }

        @Override
        protected Lexer<SQLScriptTokenId> createLexer(LexerRestartInfo<SQLScriptTokenId> info) {
            return new SQLScriptLexer(info);
        }

        @Override
        protected LanguageEmbedding<?> embedding(Token<SQLScriptTokenId> token, LanguagePath languagePath, InputAttributes inputAttributes) {
            return null;
        }

        @Override
        protected String mimeType() {
            return "text/x-sql";
        }
    }.language();

    public static Language<SQLScriptTokenId> getLanguage() {
        return language;
    }
}
