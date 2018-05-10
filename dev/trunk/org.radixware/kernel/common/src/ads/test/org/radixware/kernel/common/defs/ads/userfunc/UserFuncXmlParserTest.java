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
package org.radixware.kernel.common.defs.ads.userfunc;

import java.io.File;
import org.radixware.kernel.common.defs.ads.userfunc.xml.UdsXmlParser;
import org.radixware.kernel.common.defs.ads.userfunc.xml.UserFuncImportInfo;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.radixware.kernel.common.defs.ads.userfunc.xml.IUdsXmlParser;
import org.radixware.kernel.common.defs.ads.userfunc.xml.ParseInfo;
import org.radixware.kernel.common.defs.ads.userfunc.xml.UserFuncLibInfo;
import org.radixware.kernel.common.utils.FileUtils;

/**
 *
 * @author npopov
 *
 * 3 types of xml * 3 types of sources = 9 test cases
 */
@RunWith(Parameterized.class)
public final class UserFuncXmlParserTest {

    private final IParseStrategy strategy;
    private final IUdsXmlParser parser = UdsXmlParser.Factory.newInstance();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {new ParseFromFileStrategy()}, {new ParseFromStreamStrategy()}, {new ParseFromStringStrategy()}
        });
    }

    public UserFuncXmlParserTest(IParseStrategy strategy) {
        this.strategy = strategy;
    }

    private interface IParseStrategy {

        List<ParseInfo> parse(IUdsXmlParser parser, InputStream is) throws XmlException, IOException;
    }

    private static final class ParseFromFileStrategy implements IParseStrategy {

        @Override
        public List<ParseInfo> parse(IUdsXmlParser parser, InputStream is) throws XmlException, IOException {
            File f = null;
            try {
                f = File.createTempFile("testParseUfFromXml", "xml");
                Files.copy(is, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return parser.parse(f);
            } finally {
                if (f != null) {
                    f.delete();
                }
            }
        }
    }

    private static final class ParseFromStreamStrategy implements IParseStrategy {

        @Override
        public List<ParseInfo> parse(IUdsXmlParser parser, InputStream is) throws XmlException, IOException {
            return parser.parse(is);
        }

    }

    private static final class ParseFromStringStrategy implements IParseStrategy {

        @Override
        public List<ParseInfo> parse(IUdsXmlParser parser, InputStream is) throws XmlException, IOException {
            return parser.parse(FileUtils.readTextStream(is, "UTF-8"));
        }

    }

    @Test
    public void testPacket() throws XmlException, IOException {
        InputStream in = getClass().getResourceAsStream("Packet.xml");
        assertNotNull(in);
        List<ParseInfo> infos = strategy.parse(parser, in);
        assertEquals("Error on parse LargeFile", 4, infos.size());
        for (ParseInfo info : infos) {
            checkUfInfo(info);
        }
    }

    @Test
    public void testAdsUserFuncDefinition() throws XmlException, IOException {
        InputStream in = getClass().getResourceAsStream("AdsUserFuncDefinition.xml");
        assertNotNull(in);
        List<ParseInfo> infos = strategy.parse(parser, in);
        assertEquals("Error on parse AdsUserFuncDefinition", 1, infos.size());
        for (ParseInfo info : infos) {
            checkUfInfo(info);
        }
    }

    @Test
    public void testUserFunc() throws XmlException, IOException {
        InputStream in = getClass().getResourceAsStream("UserFunc.xml");
        assertNotNull(in);
        List<ParseInfo> infos = strategy.parse(parser, in);
        assertEquals("Error on parse UserFunc", 1, infos.size());
        for (ParseInfo info : infos) {
            checkUfInfo(info);
        }
    }

    private void checkUfInfo(ParseInfo info) {
        assertNotNull("Name is null", info.getName());
        if (info instanceof UserFuncImportInfo){
            UserFuncImportInfo funcInfo = (UserFuncImportInfo) info;
            assertNotNull("Jml source is null", funcInfo.getSource());
            if (funcInfo.getOwnerEntityId() == null) {
                assertNotNull("OwnerClassId is null", funcInfo.getOwnerClassId());
            }
            assertNotNull("PropId is null", funcInfo.getPropId());
            assertNotNull("ClassId is null", funcInfo.getClassId());
            assertNotNull("MethodId is null", funcInfo.getClassId());
        }
        if (info instanceof UserFuncLibInfo){
            UserFuncLibInfo libInfo = (UserFuncLibInfo) info;
            for (UserFuncImportInfo uf : libInfo.getUserFuncs()){
                checkUfInfo(uf);
            }
        }
    }
}
