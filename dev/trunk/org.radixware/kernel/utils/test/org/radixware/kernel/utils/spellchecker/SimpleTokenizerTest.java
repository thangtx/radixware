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

package org.radixware.kernel.utils.spellchecker;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.radixware.kernel.utils.spellchecker.SimpleTokenizer.Token;


public class SimpleTokenizerTest {

    public SimpleTokenizerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of nextWord method, of class SimpleTokenizer.
     */
    @Test
    public void testTokenize() {
        SimpleTokenizer tokenizer = new SimpleTokenizer("This method wasn't checked for errors");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(6, tokens.size());

    }
    
    @Test
    public void testFormatter() {
        SimpleTokenizer tokenizer = new SimpleTokenizer("%4$2s %3$2s %2$2s %1$2s");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(4, tokens.size());
        
        tokenizer = new SimpleTokenizer("%+10.4f");
        tokens = tokenizer.tokenize();
        assertEquals(1, tokens.size());
        
        tokenizer = new SimpleTokenizer("last statement: %(,.2f");
        tokens = tokenizer.tokenize();
        assertEquals(3, tokens.size());
        
        tokenizer = new SimpleTokenizer("Unable to open file '%1$s': %2$s");
        tokens = tokenizer.tokenize();
        assertEquals(6, tokens.size());
        
        tokenizer = new SimpleTokenizer("Duke's Birthday: %1$tm %1$te,%1$tY");
        tokens = tokenizer.tokenize();
        assertEquals(5, tokens.size());
        
        tokenizer = new SimpleTokenizer("Duke's Birthday: %1$tm %<te,%<tY");
        tokens = tokenizer.tokenize();
        assertEquals(5, tokens.size());
        
        tokenizer = new SimpleTokenizer("%ta %tb %td %tT %tZ %tY");
        tokens = tokenizer.tokenize();
        assertEquals(6, tokens.size());
        
        tokenizer = new SimpleTokenizer("The value of index is: %d%n");
        tokens = tokenizer.tokenize();
        assertEquals(7, tokens.size());

        tokenizer = new SimpleTokenizer("%-10.4f%n%n");
        tokens = tokenizer.tokenize();
        assertEquals(3, tokens.size());
        
        tokenizer = new SimpleTokenizer("%tl:%tM %tp%n");
        tokens = tokenizer.tokenize();
        assertEquals(4, tokens.size());
        
        tokenizer = new SimpleTokenizer("%+,8d%n%n");
        tokens = tokenizer.tokenize();
        assertEquals(3, tokens.size());
        
        tokenizer = new SimpleTokenizer("Order with OrdId : %d and Amount: %d is missing");
        tokens = tokenizer.tokenize();
        assertEquals(9, tokens.size());
        
        tokenizer = new SimpleTokenizer("%.3f%n");
        tokens = tokenizer.tokenize();
        assertEquals(2, tokens.size());
        
        tokenizer = new SimpleTokenizer("%(d, %(d, %(6d");
        tokens = tokenizer.tokenize();
        assertEquals(3, tokens.size());
        
        tokenizer = new SimpleTokenizer("Точность по умолчанию: %f%n");
        tokens = tokenizer.tokenize();
        assertEquals(5, tokens.size());
        
        tokenizer = new SimpleTokenizer("Local time: %tT");
        tokens = tokenizer.tokenize();
        assertEquals(3, tokens.size());
        
        tokenizer = new SimpleTokenizer("%1$05d %2$05d");
        tokens = tokenizer.tokenize();
        assertEquals(2, tokens.size());
        
        tokenizer = new SimpleTokenizer("Three numbers after decimal: %1$.3f");
        tokens = tokenizer.tokenize();
        assertEquals(5, tokens.size());
        
        tokenizer = new SimpleTokenizer("Month: %1$tB Day: %1$te Year: %1$tY");
        tokens = tokenizer.tokenize();
        assertEquals(6, tokens.size());
        
        tokenizer = new SimpleTokenizer("Year: %1$tY %1$ty %1$tj");
        tokens = tokenizer.tokenize();
        assertEquals(4, tokens.size());
    }
}
