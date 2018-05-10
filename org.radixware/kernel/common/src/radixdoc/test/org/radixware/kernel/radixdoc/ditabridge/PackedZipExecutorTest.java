/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.radixdoc.ditabridge;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.Assert;
import org.junit.Test;
import org.radixware.kernel.radixdoc.enums.EDecorationProperties;


public class PackedZipExecutorTest {
    
    @Test
    public void lifeCycleTest() throws IOException {
        try(final InputStream is = this.getClass().getResourceAsStream("test.zip");
            final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            System.err.println("Start...");
            PackedZipExecutor.unpackAndExecute(is,baos,"-i","docSrc/userguide.ditamap","-f","pdf");
            System.err.println("End");

//            try(final OutputStream  fos = new FileOutputStream("c:/tmp/zzz.zip")) {
//                baos.writeTo(fos);  fos.flush();
//            }
            
            try(final InputStream zipIs = new ByteArrayInputStream(baos.toByteArray());
                final ZipInputStream zis = new ZipInputStream(zipIs)) {
                ZipEntry ze;

                while ((ze = zis.getNextEntry()) != null) {
                    Assert.assertTrue(ze.getName().equals("userguide.pdf"));
                }
            }
            System.err.println("Success!");
        }
    }

    @Test
    public void staticMethodsTest() throws IOException {
        try(final InputStream is = this.getClass().getResourceAsStream("decorationOk.zip");
            final ZipInputStream zis = new ZipInputStream(is);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final PrintStream pwr = new PrintStream(baos)) {
            
            final PackedZipExecutor.DecorationRepo repo = new PackedZipExecutor.DecorationRepo(zis);
            
            try{PackedZipExecutor.uploadAttributes(null,pwr);                
                Assert.fail("Mandatory exception was not detected (null 1-st argument)");
            } catch (IllegalArgumentException exc) {
            }
            try{PackedZipExecutor.uploadAttributes(repo,null);                
                Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
            } catch (IllegalArgumentException exc) {
            }
            
            PackedZipExecutor.uploadAttributes(repo,pwr);
        }
    }
    
    @Test
    public void decorationRepoTest() throws IOException {
        try(final InputStream is = this.getClass().getResourceAsStream("decorationOk.zip");
            final ZipInputStream zis = new ZipInputStream(is)) {
            
            final PackedZipExecutor.DecorationRepo repo = new PackedZipExecutor.DecorationRepo(zis);
            
            int count = 0;
            for (PackedZipExecutor.DecorationItem item : repo) {
                count++;
                Assert.assertNotNull(item.content);
                Assert.assertNotNull(item.target);
                Assert.assertNotNull(item.name);
            }
            Assert.assertEquals(count,2);
            Assert.assertEquals(repo.getProperty(EDecorationProperties.COMPANY_NAME),"test company");
            Assert.assertEquals(repo.getProperty(EDecorationProperties.COMPANY_NAME),"first test company second");
            Assert.assertEquals(repo.getProperty(EDecorationProperties.LAYER_VERSION_BASE),"first ${unknown} second");
            Assert.assertEquals(repo.getProperty(EDecorationProperties.LAYER_VERSION_FULL),"first ${testVersion second}");
            
            try {repo.getProperty(null);
               Assert.fail("Mandatory exception was not detected (null 1-st argument)"); 
            } catch (IllegalArgumentException exc) {
            }
            try {repo.getProperty("");
               Assert.fail("Mandatory exception was not detected (empty 1-st argument)"); 
            } catch (IllegalArgumentException exc) {
            }
            try {repo.getProperty(EDecorationProperties.COMPANY_NAME,null);
               Assert.fail("Mandatory exception was not detected (null 2-nd argument)"); 
            } catch (IllegalArgumentException exc) {
            }
            try {repo.getProperty(EDecorationProperties.COMPANY_NAME,"");
               Assert.fail("Mandatory exception was not detected (empty 2-nd argument)"); 
            } catch (IllegalArgumentException exc) {
            }
        }
        
        try {new PackedZipExecutor.DecorationRepo(null);
           Assert.fail("Mandatory exception was not detected (null 1-st argument)"); 
        } catch (IllegalArgumentException exc) {
        }

        try {decorationTest("decorationErr1.zip");
           Assert.fail("Mandatory exception was not detected (missing style.properties)"); 
        } catch (IllegalArgumentException exc) {
        }
        try {decorationTest("decorationErr2.zip");
           Assert.fail("Mandatory exception was not detected (empty style.properties)"); 
        } catch (IllegalArgumentException exc) {
        }
        try {decorationTest("decorationErr3.zip");
           Assert.fail("Mandatory exception was not detected (unknown key in the style.properties)"); 
        } catch (IllegalArgumentException exc) {
        }
        try {decorationTest("decorationErr4.zip");
           Assert.fail("Mandatory exception was not detected (key in the style.properties is references to unknown zip part)"); 
        } catch (IllegalArgumentException exc) {
        }
    }
    
    private void decorationTest(final String name) throws IOException {
        try(final InputStream is = this.getClass().getResourceAsStream(name);
            final ZipInputStream zis = new ZipInputStream(is)) {
            
            new PackedZipExecutor.DecorationRepo(zis);
        }
    }
}
