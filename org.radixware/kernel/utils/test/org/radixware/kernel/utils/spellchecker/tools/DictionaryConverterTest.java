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

package org.radixware.kernel.utils.spellchecker.tools;

import java.io.InputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class DictionaryConverterTest {

    public DictionaryConverterTest() {
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
     * Test of loadDictionary method, of class Dictionary.
     */
    @Test
    public void testLoadDictionary() throws Exception {
        InputStream stream = getClass().getResourceAsStream("russian.aff.koi");
        assertNotNull(stream);
        Affixes affixes = Affixes.readAffixes(stream, "KOI8");
        assertNotNull(affixes);
        stream = getClass().getResourceAsStream("base.koi");
        DictionaryConverter dict = DictionaryConverter.loadDictionary(stream, "KOI8", affixes);
        assertNotNull(dict);
        System.out.println(dict);
    }
}
