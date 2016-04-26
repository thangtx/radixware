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
 * Portions Copyrighted 2007 Sun Microsystems, Inc.
 * 
 * April 2015: adapted to work with Jml documents by Compass Plus Limited.
 */
package org.radixware.kernel.designer.common.dialogs.jmlnb;

import java.util.List;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.spi.editor.bracesmatching.BracesMatcher;
import org.netbeans.spi.editor.bracesmatching.BracesMatcherFactory;
import org.netbeans.spi.editor.bracesmatching.MatcherContext;
import org.netbeans.spi.editor.bracesmatching.support.BracesMatcherSupport;


public final class JmlBracesMatcher implements BracesMatcher, BracesMatcherFactory {

    private static final char [] PAIRS = new char [] { '(', ')', '[', ']', '{', '}' }; //NOI18N
    private static final JmlTokenId [] PAIR_TOKEN_IDS = new JmlTokenId [] {
        JmlTokenId.LPAREN, JmlTokenId.RPAREN,
        JmlTokenId.LBRACKET, JmlTokenId.RBRACKET,
        JmlTokenId.LBRACE, JmlTokenId.RBRACE
    };
    
    private final MatcherContext context;
    
    private int originOffset;
    private char originChar;
    private char matchingChar;
    private boolean backward;
    private List<TokenSequence<?>> sequences;
    
    public JmlBracesMatcher() {
        this(null);
    }

    private JmlBracesMatcher(MatcherContext context) {
        this.context = context;
    }
    
    // -----------------------------------------------------
    // BracesMatcher implementation
    // -----------------------------------------------------
    
    public int[] findOrigin() throws BadLocationException, InterruptedException {
        ((AbstractDocument) context.getDocument()).readLock();
        try {
            int [] origin = BracesMatcherSupport.findChar(
                context.getDocument(), 
                context.getSearchOffset(), 
                context.getLimitOffset(), 
                PAIRS
            );

            if (origin != null) {
                originOffset = origin[0];
                originChar = PAIRS[origin[1]];
                matchingChar = PAIRS[origin[1] + origin[2]];
                backward = origin[2] < 0;

                // Filter out block and line comments
                TokenHierarchy<Document> th = TokenHierarchy.get(context.getDocument());
                sequences = getEmbeddedTokenSequences(th, originOffset, backward, JmlTokenId.language());

                if (!sequences.isEmpty()) {
                    // Check special tokens
                    TokenSequence<?> seq = sequences.get(sequences.size() - 1);
                    seq.move(originOffset);
                    if (seq.moveNext()) {
                        if (seq.token().id() == JmlTokenId.BLOCK_COMMENT ||
                            seq.token().id() == JmlTokenId.LINE_COMMENT
                        ) {
                            return null;
                        }
                    }
                }

                return new int [] { originOffset, originOffset + 1 };
            } else {
                return null;
            }
        } finally {
            ((AbstractDocument) context.getDocument()).readUnlock();
        }
    }

    public int[] findMatches() throws InterruptedException, BadLocationException {
        ((AbstractDocument) context.getDocument()).readLock();
        try {
            if (!sequences.isEmpty()) {
                // Check special tokens
                TokenSequence<?> seq = sequences.get(sequences.size() - 1);
                seq.move(originOffset);
                if (seq.moveNext()) {
                    if (seq.token().id() == JmlTokenId.STRING_LITERAL) {
                        int offset = BracesMatcherSupport.matchChar(
                            context.getDocument(), 
                            backward ? originOffset : originOffset + 1, 
                            backward ? seq.offset() : seq.offset() + seq.token().length(), 
                            originChar,
                            matchingChar);
                        if (offset != -1) {
                            return new int [] { offset, offset + 1 };
                        } else {
                            return null;
                        }
                    }
                }

                // We are in plain java

                TokenHierarchy<Document> th = TokenHierarchy.get(context.getDocument());
                List<TokenSequence<?>> list;
                if (backward) {
                    list = th.tokenSequenceList(seq.languagePath(), 0, originOffset);
                } else {
                    list = th.tokenSequenceList(seq.languagePath(), originOffset + 1, context.getDocument().getLength());
                }

                JmlTokenId originId = getTokenId(originChar);
                JmlTokenId lookingForId = getTokenId(matchingChar);
                int counter = 0;

                for(TokenSequenceIterator tsi = new TokenSequenceIterator(list, backward); tsi.hasMore(); ) {
                    TokenSequence<?> sq = tsi.getSequence();

                    if (originId == sq.token().id()) {
                        counter++;
                    } else if (lookingForId == sq.token().id()) {
                        if (counter == 0) {
                            return new int [] { sq.offset(), sq.offset() + sq.token().length() };
                        } else {
                            counter--;
                        }
                    }
                }
            }

            return null;
        } finally {
            ((AbstractDocument) context.getDocument()).readUnlock();
        }
    }

    // -----------------------------------------------------
    // private implementation
    // -----------------------------------------------------
    
    private JmlTokenId getTokenId(char ch) {
        for(int i = 0; i < PAIRS.length; i++) {
            if (PAIRS[i] == ch) {
                return PAIR_TOKEN_IDS[i];
            }
        }
        return null;
    }
    
    public static List<TokenSequence<?>> getEmbeddedTokenSequences(
        TokenHierarchy<?> th, int offset, boolean backwardBias, Language<?> language
    ) {
        List<TokenSequence<?>> sequences = th.embeddedTokenSequences(offset, backwardBias);

        for(int i = sequences.size() - 1; i >= 0; i--) {
            TokenSequence<?> seq = sequences.get(i);
            if (seq.language() == language) {
                break;
            } else {
                sequences.remove(i);
            }
        }
        
        return sequences;
    }
    
    private static final class TokenSequenceIterator {
        
        private final List<TokenSequence<?>> list;
        private final boolean backward;
        
        private int index;
        
        public TokenSequenceIterator(List<TokenSequence<?>> list, boolean backward) {
            this.list = list;
            this.backward = backward;
            this.index = -1;
        }
        
        public boolean hasMore() {
            return backward ? hasPrevious() : hasNext();
        }

        public TokenSequence<?> getSequence() {
            assert index >= 0 && index < list.size() : "No sequence available, call hasMore() first."; //NOI18N
            return list.get(index);
        }
        
        private boolean hasPrevious() {
            boolean anotherSeq = false;
            
            if (index == -1) {
                index = list.size() - 1;
                anotherSeq = true;
            }
            
            for( ; index >= 0; index--) {
                TokenSequence<?> seq = list.get(index);
                if (anotherSeq) {
                    seq.moveEnd();
                }
                
                if (seq.movePrevious()) {
                    return true;
                }
                
                anotherSeq = true;
            }
            
            return false;
        }
        
        private boolean hasNext() {
            boolean anotherSeq = false;
            
            if (index == -1) {
                index = 0;
                anotherSeq = true;
            }
            
            for( ; index < list.size(); index++) {
                TokenSequence<?> seq = list.get(index);
                if (anotherSeq) {
                    seq.moveStart();
                }
                
                if (seq.moveNext()) {
                    return true;
                }
                
                anotherSeq = true;
            }
            
            return false;
        }
    } // End of TokenSequenceIterator class
    
    // -----------------------------------------------------
    // BracesMatcherFactory implementation
    // -----------------------------------------------------
    
    /** */
    public BracesMatcher createMatcher(MatcherContext context) {
        return new JmlBracesMatcher(context);
    }

}