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

package org.radixware.kernel.common.compiler.xbeans;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.apache.xmlbeans.ResourceLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemCompiler;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.radixware.kernel.common.build.xbeans.XbeansSchemaCodePrinter;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlFilerImpl;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.JarFiles;


public class XbeansTypeSystemBridgeTest {

    public XbeansTypeSystemBridgeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

//    @Test
//    public void testCreateTs() throws XmlException, IOException {
//        String name = "kernel_common_xsd_types_xsd";
//
//        SchemaTypeSystemImpl impl = new SchemaTypeSystemImpl(new ResourceLoader() {
//            @Override
//            public InputStream getResourceAsStream(String string) {
//                try {
//                    String path = "schemaorg_apache_xmlbeans/system/" + string;
//                    File file = new File("/home/akrylov/ssd/radix/dev/trunk/org.radixware/kernel/common/bin/xb_types.jar");
//                    ZipFile zip = new ZipFile(file);
//                    ZipEntry e = zip.getEntry(path);
//                    byte[] bytes = FileUtils.getZipEntryByteContent(e, zip);
//                    zip.close();
//                    return new ByteArrayInputStream(bytes);
//                } catch (ZipException ex) {
//                    Logger.getLogger(XbeansTypeSystemBridgeTest.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    Logger.getLogger(XbeansTypeSystemBridgeTest.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                return null;
//            }
//
//            @Override
//            public void close() {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        }, name, null);
//
//        impl.resolve();
//        final List<String> allns = new LinkedList<>();
//        AdsXmlSchemaInfo.XmlDefinitionLookuper lookuper = new AdsXmlSchemaInfo.XmlDefinitionLookuper() {
//            Map<String, List<SchemaDocument.Schema>> schemas = null;
//
//            @Override
//            public AdsXmlSchemaInfo.XmlDefinitionLookuper.Answer lookup(String targetNamespace) {
//                if (schemas == null) {
//                    schemas = new HashMap<>();
//                    File[] files = new File("/home/akrylov/ssd/radix/dev/trunk/org.radixware/kernel/common/xsd/").listFiles(new FileFilter() {
//                        @Override
//                        public boolean accept(File pathname) {
//                            return pathname.getName().endsWith(".xsd");
//                        }
//                    });
//                    if (files != null) {
//                        for (File file : files) {
//                            try {
//                                SchemaDocument.Schema xDoc = SchemaDocument.Factory.parse(file).getSchema();
//                                List<SchemaDocument.Schema> list = schemas.get(xDoc.getTargetNamespace());
//                                if (list == null) {
//                                    list = new LinkedList<>();
//                                    schemas.put(xDoc.getTargetNamespace(), list);
//                                    allns.add(xDoc.getTargetNamespace());
//                                }
//                                list.add(xDoc);
//                            } catch (XmlException ex) {
//                                Logger.getLogger(XbeansTypeSystemBridgeTest.class.getName()).log(Level.SEVERE, null, ex);
//                            } catch (IOException ex) {
//                                Logger.getLogger(XbeansTypeSystemBridgeTest.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                        }
//                    }
//                }
//                List<SchemaDocument.Schema> list = schemas.get(targetNamespace);
//                if (list == null) {
//                    return null;
//                }
//                return new Answer(null, list.toArray(new SchemaDocument.Schema[list.size()]));
//            }
//        };
//
//
//        File baseDir = new File("/home/akrylov/xsdtstest/");
//        lookuper.lookup("");
//
//        AdsXmlSchemaInfo info = new AdsXmlSchemaInfo(new String[]{"http://schemas.radixware.org/types.xsd"}, lookuper);
//        long t = System.currentTimeMillis();
//        SchemaTypeSystem[] tss = info.buildTypeSystem();
//
//        for (SchemaTypeSystem ts : tss) {
//            SchemaTypeSystemCompiler.generateTypes(ts, new AdsXmlFilerImpl(null), new XmlOptions()
//                    .setSchemaCodePrinter(new XbeansSchemaCodePrinter()).setGenerateJavaVersion("1.5"));
//        }
//        System.out.println(System.currentTimeMillis() - t);
//
//
//    }
}