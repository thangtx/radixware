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

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.netbeans.api.java.lexer.JavadocTokenId;
import org.netbeans.api.lexer.InputAttributes;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.LanguagePath;
import org.netbeans.api.lexer.PartType;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.spi.lexer.LanguageEmbedding;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;


public enum JmlTokenId implements TokenId {

    ERROR(null, "error"),
    IDENTIFIER(null, "identifier"),

    ABSTRACT("abstract", "keyword"),
    ASSERT("assert", "keyword-directive"),
    BOOLEAN("boolean", "keyword"),
    BREAK("break", "keyword-directive"),
    BYTE("byte", "keyword"),
    CASE("case", "keyword-directive"),
    CATCH("catch", "keyword-directive"),
    CHAR("char", "keyword"),
    CLASS("class", "keyword"),
    CONST("const", "keyword"),
    CONTINUE("continue", "keyword-directive"),
    DEFAULT("default", "keyword-directive"),
    DO("do", "keyword-directive"),
    DOUBLE("double", "keyword"),
    ELSE("else", "keyword-directive"),
    ENUM("enum", "keyword"),
    EXTENDS("extends", "keyword"),
    FINAL("final", "keyword"),
    FINALLY("finally", "keyword-directive"),
    FLOAT("float", "keyword"),
    FOR("for", "keyword-directive"),
    GOTO("goto", "keyword-directive"),
    IF("if", "keyword-directive"),
    IMPLEMENTS("implements", "keyword"),
    IMPORT("import", "keyword"),
    INSTANCEOF("instanceof", "keyword"),
    INT("int", "keyword"),
    INTERFACE("interface", "keyword"),
    LONG("long", "keyword"),
    NATIVE("native", "keyword"),
    NEW("new", "keyword"),
    PACKAGE("package", "keyword"),
    PRIVATE("private", "keyword"),
    PROTECTED("protected", "keyword"),
    PUBLIC("public", "keyword"),
    RETURN("return", "keyword-directive"),
    SHORT("short", "keyword"),
    STATIC("static", "keyword"),
    STRICTFP("strictfp", "keyword"),
    SUPER("super", "keyword"),
    SWITCH("switch", "keyword-directive"),
    SYNCHRONIZED("synchronized", "keyword"),
    THIS("this", "keyword"),
    THROW("throw", "keyword-directive"),
    THROWS("throws", "keyword"),
    TRANSIENT("transient", "keyword"),
    TRY("try", "keyword-directive"),
    VOID("void", "keyword"),
    VOLATILE("volatile", "keyword"),
    WHILE("while", "keyword-directive"),

    INT_LITERAL(null, "number"),
    LONG_LITERAL(null, "number"),
    FLOAT_LITERAL(null, "number"),
    DOUBLE_LITERAL(null, "number"),
    CHAR_LITERAL(null, "character"),
    STRING_LITERAL(null, "string"),

    TRUE("true", "literal"),
    FALSE("false", "literal"),
    NULL("null", "literal"),

    LPAREN("(", "separator"),
    RPAREN(")", "separator"),
    LBRACE("{", "separator"),
    RBRACE("}", "separator"),
    LBRACKET("[", "separator"),
    RBRACKET("]", "separator"),
    SEMICOLON(";", "separator"),
    COMMA(",", "separator"),
    DOT(".", "separator"),

    EQ("=", "operator"),
    GT(">", "operator"),
    LT("<", "operator"),
    BANG("!", "operator"),
    TILDE("~", "operator"),
    QUESTION("?", "operator"),
    COLON(":", "operator"),
    EQEQ("==", "operator"),
    LTEQ("<=", "operator"),
    GTEQ(">=", "operator"),
    BANGEQ("!=","operator"),
    AMPAMP("&&", "operator"),
    BARBAR("||", "operator"),
    PLUSPLUS("++", "operator"),
    MINUSMINUS("--","operator"),
    PLUS("+", "operator"),
    MINUS("-", "operator"),
    STAR("*", "operator"),
    SLASH("/", "operator"),
    AMP("&", "operator"),
    BAR("|", "operator"),
    CARET("^", "operator"),
    PERCENT("%", "operator"),
    LTLT("<<", "operator"),
    GTGT(">>", "operator"),
    GTGTGT(">>>", "operator"),
    PLUSEQ("+=", "operator"),
    MINUSEQ("-=", "operator"),
    STAREQ("*=", "operator"),
    SLASHEQ("/=", "operator"),
    AMPEQ("&=", "operator"),
    BAREQ("|=", "operator"),
    CARETEQ("^=", "operator"),
    PERCENTEQ("%=", "operator"),
    LTLTEQ("<<=", "operator"),
    GTGTEQ(">>=", "operator"),
    GTGTGTEQ(">>>=", "operator"),

    ELLIPSIS("...", "special"),
    AT("@", "special"),

    WHITESPACE(null, "whitespace"),
    LINE_COMMENT(null, "comment"), // Token includes ending new-line
    BLOCK_COMMENT(null, "comment"),
    JAVADOC_COMMENT(null, "comment"),

    // Errors
    INVALID_COMMENT_END("*/", "error"),
    FLOAT_LITERAL_INVALID(null, "number"),

    //Tags
    TAG(null, "tag");


    private final String fixedText;

    private final String primaryCategory;

    JmlTokenId(String fixedText, String primaryCategory) {
        this.fixedText = fixedText;
        this.primaryCategory = primaryCategory;
    }

    public String fixedText() {
        return fixedText;
    }

    public String primaryCategory() {
        return primaryCategory;
    }

    private static final Language<JmlTokenId> language = new LanguageHierarchy<JmlTokenId>() {

        @Override
        protected String mimeType() {
            return "text/x-jml";
        }

        @Override
        protected Collection<JmlTokenId> createTokenIds() {
            return EnumSet.allOf(JmlTokenId.class);
        }

        @Override
        protected Map<String,Collection<JmlTokenId>> createTokenCategories() {
            Map<String,Collection<JmlTokenId>> cats = new HashMap<String,Collection<JmlTokenId>>();
            // Additional literals being a lexical error
            cats.put("error", EnumSet.of(
                JmlTokenId.FLOAT_LITERAL_INVALID
            ));
            // Literals category
            EnumSet<JmlTokenId> l = EnumSet.of(
                JmlTokenId.INT_LITERAL,
                JmlTokenId.LONG_LITERAL,
                JmlTokenId.FLOAT_LITERAL,
                JmlTokenId.DOUBLE_LITERAL,
                JmlTokenId.CHAR_LITERAL
            );
            l.add(JmlTokenId.STRING_LITERAL);
            cats.put("literal", l);

            return cats;
        }

        @Override
        protected Lexer<JmlTokenId> createLexer(LexerRestartInfo<JmlTokenId> info) {
            return new JmlLexer(info);
        }

        @Override
        protected LanguageEmbedding<?> embedding(
        Token<JmlTokenId> token, LanguagePath languagePath, InputAttributes inputAttributes) {
            // Test language embedding in the block comment
            switch (token.id()) {
                case JAVADOC_COMMENT:
                    return LanguageEmbedding.create(JavadocTokenId.language(), 3,
                            (token.partType() == PartType.COMPLETE) ? 2 : 0);
                case STRING_LITERAL:
                    return null;
            }
            return null; // No embedding
        }

//        protected CharPreprocessor createCharPreprocessor() {
//            return CharPreprocessor.createUnicodeEscapesPreprocessor();
//        }

    }.language();

    public static Language<JmlTokenId> language() {
        return language;
    }

}