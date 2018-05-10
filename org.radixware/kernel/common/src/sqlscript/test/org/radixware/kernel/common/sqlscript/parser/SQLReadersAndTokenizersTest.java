/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.sqlscript.parser;

import java.io.IOException;
import java.io.Reader;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author achernomyrdin
 */
public class SQLReadersAndTokenizersTest {
    @Test
    public void StringReaderTest() throws IOException {
        final StringReader rdr = new StringReader("");
        final char[] buffer = new char[100];
        
        Assert.assertEquals(rdr.getIndex(),0);
        Assert.assertEquals(rdr.getRest(),"");
        Assert.assertTrue(rdr.isEof());
        Assert.assertEquals(rdr.read(buffer),-1);
        
        final StringReader oneCharRdr = new StringReader("x");
        
        Assert.assertEquals(oneCharRdr.getIndex(),0);
        Assert.assertEquals(oneCharRdr.getRest(),"x");
        Assert.assertFalse(oneCharRdr.isEof());
        
        Assert.assertEquals(oneCharRdr.read(buffer),1);
        Assert.assertEquals(oneCharRdr.getIndex(),1);
        Assert.assertEquals(oneCharRdr.getRest(),"");
        
        try{new StringReader(null);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{oneCharRdr.read((char[])null,0,1);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
    }

    @Test
    public void TokenizerTest() throws IOException {
        try(final Reader rdr = new StringReader("token1 token2 \"token3\" token4")) {
            final Tokenizer t = new Tokenizer(rdr);
            
            Assert.assertEquals(t.getReader(),rdr);
            Assert.assertEquals(t.getRest(),"token1 token2 \"token3\" token4");
            Assert.assertEquals(t.getRest(),"");
        }
    
        try(final Reader rdr = new StringReader("token1 token2 \"token3\" token4")) {
            final Tokenizer t = new Tokenizer(rdr);

            Assert.assertEquals(t.nextToken(),"token1");
            Assert.assertEquals(t.nextToken(),"token2");
            Assert.assertEquals(t.nextToken(),"token3");
            Assert.assertEquals(t.nextToken(),"token4");
            Assert.assertNull(t.nextToken());
            Assert.assertNull(t.nextToken());
        }

        try(final Reader rdr = new StringReader("token1 token2 \"token3\" token4")) {
            final Tokenizer t = new Tokenizer(rdr);

            Assert.assertEquals(t.nextToken(),"token1");
            Assert.assertEquals(t.nextToken(),"token2");
            Assert.assertEquals(t.nextToken(),"token3");
            Assert.assertEquals(t.nextToken(),"token4");
            Assert.assertNull(t.nextToken());
            Assert.assertNull(t.nextToken());
        }
        
        try(final Reader rdr = new StringReader("token1 token2 \"token3\" token4")) {
            final Tokenizer t = new Tokenizer(rdr);

            Assert.assertEquals(t.makeToken("","",true),"\"\"");
            Assert.assertEquals(t.makeToken("AB\"C","",true),"AB\\\"C");
            Assert.assertEquals(t.makeToken("AB\\\"C","\'",true),"AB\\\\\\\"C");
            
            Assert.assertEquals(t.makeToken("","",false),"");
            Assert.assertEquals(t.makeToken("AB\"C","",false),"AB\"C");
            Assert.assertEquals(t.makeToken("AB\\\"C","\'",false),"AB\\\\\"C");
        }
        
        try{new Tokenizer(null);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
        
        try(final Reader rdr = new StringReader("token1 token2 \"token3\" token4")) {
            final Tokenizer t = new Tokenizer(rdr);
            
            try{t.makeToken(null,"",true);
                Assert.fail("Mandatory exception was not detected (null 1-st argument)");
            } catch (IllegalArgumentException exc) {
            }
        }
    }

    @Test
    public void StringTokenizerTest() throws IOException {
        final StringTokenizer st = new StringTokenizer("token1 token2 \"token3\" token4");
        
        int count = 0;
        while (st.hasMoreTokens()) {
            Assert.assertNotNull(st.nextToken());
            count++;
        }
        Assert.assertEquals(count,4);
    }
}
