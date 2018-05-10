/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.scml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.junit.Test;
import org.junit.Assert;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.types.Id;

/**
 *
 * @author achernomyrdin
 */
public class CodePrinterTest {
    @Test
    public void collectionTest() throws IOException {
        final CodePrinter container = CodePrinter.Factory.newSqlPrinter();
        
        try(final InputStream is = this.getClass().getResourceAsStream("oracle.txt")) {
            basicTest(CodePrinter.Factory.newSqlPrinter(EDatabaseType.ORACLE),loadString(is),EDatabaseType.ORACLE);
        }
        try(final InputStream is = this.getClass().getResourceAsStream("postgresenterprise.txt")) {
            basicTest(CodePrinter.Factory.newSqlPrinter(EDatabaseType.ENTERPRISEDB),loadString(is),EDatabaseType.ENTERPRISEDB);
        }
        try(final InputStream is = this.getClass().getResourceAsStream("postgresql.txt")) {
            basicTest(CodePrinter.Factory.newSqlPrinter(EDatabaseType.POSTGRESQL),loadString(is),EDatabaseType.POSTGRESQL);
        }
        
        try(final InputStream is = this.getClass().getResourceAsStream("oracle.txt")) {
            basicTest(CodePrinter.Factory.newSqlPrinter(container,EDatabaseType.ORACLE),loadString(is),EDatabaseType.ORACLE);
        }
        try(final InputStream is = this.getClass().getResourceAsStream("postgresenterprise.txt")) {
            basicTest(CodePrinter.Factory.newSqlPrinter(container,EDatabaseType.ENTERPRISEDB),loadString(is),EDatabaseType.ENTERPRISEDB);
        }
        try(final InputStream is = this.getClass().getResourceAsStream("postgresql.txt")) {
            basicTest(CodePrinter.Factory.newSqlPrinter(container,EDatabaseType.POSTGRESQL),loadString(is),EDatabaseType.POSTGRESQL);
        }
    }
    
    private String loadString(final InputStream is) throws IOException {
        if (is == null) {
            throw new IllegalArgumentException("Input stream can't be null!");
        }
        else {
            try(final Reader rdr = new InputStreamReader(is)) {
                final char[] buffer = new char[1000000];
                return new String(buffer,0,rdr.read(buffer)).replace("\r\n","\n");
            }
        }
    }
    
    private void basicTest(final CodePrinter cp, final String result, final EDatabaseType awaited) {
        Assert.assertEquals(cp.getProperty(CodePrinter.DATABASE_TYPE),awaited);
        Assert.assertNull(cp.getProperty("unknown"));
        
        cp.clear();
        Assert.assertEquals(cp.length(),0);
        
        cp.enterBlock().print("String ").print(new StringBuilder("StringBuilder ")).print(new StringBuilder("StringBuffer "));
        cp.print(new CharSequence() {
            final String value = "CharSequence ";
            @Override public int length() {return value.length();}
            @Override public char charAt(int index) {return value.charAt(index);}
            @Override public CharSequence subSequence(int start, int end) {return value.substring(start,end);}
        });

        cp.print(120).print(' ').print(240L).print(' ').print(true).print(' ').print(false).print(' ').print(Id.Factory.loadFrom("idTest")).print(' ');
        cp.printColon().printComma().printError().printMinus().printSpace().printlnSemicolon();
        cp.leaveBlock();
        cp.printStringLiteral("String & ' literal\n string").printCommandSeparator();
        
        Assert.assertTrue(cp.length() > 0);
        Assert.assertEquals(cp.toString(),result);
        Assert.assertArrayEquals(cp.getContents(),result.toCharArray());
        Assert.assertEquals(cp.getLineNumber(cp.length()),4);
        
        cp.reset();
        Assert.assertEquals(cp.length(),0);
    }
}
